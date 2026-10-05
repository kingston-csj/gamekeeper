import asyncio
import os
import threading
import requests
import logging
import time
import gzip
from typing import Dict, Any
import sys
from fastapi import FastAPI, BackgroundTasks, HTTPException, Request
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import boto3
import traceback
from boto3.s3.transfer import TransferConfig

from config_reader import load_config
from scheduler.task_scheduler import start_scheduler

# ========== 日志初始化 ==========
logger = logging.getLogger("ops-agent")
logger.setLevel(logging.INFO)
formatter = logging.Formatter("%(asctime)s - %(name)s - %(levelname)s - %(message)s")

# 控制台handler
console_handler = logging.StreamHandler()
console_handler.setFormatter(formatter)
# 文件handler
file_handler = logging.FileHandler("agent.log", encoding="utf-8")
file_handler.setFormatter(formatter)

# 挂载到root logger
root_logger = logging.getLogger()
root_logger.setLevel(logging.INFO)
if not root_logger.handlers:
    root_logger.addHandler(console_handler)
    root_logger.addHandler(file_handler)

current_env = os.getenv('APP_ENV', 'dev')
config = load_config(current_env)
logger.info(f"启动成功，env={current_env}")

app = FastAPI(title="Ops Agent")


# ========== 全局异常捕获中间件 ==========
@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    import traceback
    stack_msg = traceback.format_exc()
    logger.error(f"全局未捕获异常: {exc}\n堆栈:\n{stack_msg}")
    return JSONResponse(
        status_code=500,
        content={"detail": "服务内部异常", "err": str(exc)}
    )


# ========== 模型定义 ==========
class DbOpTaskCreate(BaseModel):
    taskId: str
    dbName: str


class ServerPowerTaskCreate(BaseModel):
    taskId: str
    serverName: str
    operation: str


class LogDownloadReq(BaseModel):
    log_root: str
    sub_path: str


# 白名单
ALLOW_TASK_TYPES = {"mysql_backup", "mysql_truncate"}

# s3 client全局实例
s3_client = None
s3_transfer_config = TransferConfig(multipart_chunksize=10 * 1024 * 1024)


def report_to_admin(task_id: str, progress: float, message: str, report_url: str):
    """统一上报进度/状态到SpringBoot后台"""
    payload = {
        "progress": round(progress, 2),
        "data": message
    }
    real_url = report_url.replace("{taskId}", task_id)
    try:
        resp = requests.post(real_url, json=payload, timeout=10)
        logger.info(f"上报响应 code={resp.status_code}, resp={resp.text}")
    except Exception as e:
        logger.error(f"上报后台失败 taskId={task_id}, err={str(e)}")


class Boto3UploadCallback:
    """boto3上传回调，上报50~100%进度到SpringBoot"""

    def __init__(self, task_id: str, total_size: int, admin_report_url: str):
        self.task_id = task_id
        self.total_size = total_size
        self.admin_report_url = admin_report_url
        self.transferred = 0
        self.lock = threading.Lock()

    def __call__(self, bytes_amount):
        with self.lock:
            self.transferred += bytes_amount
            pct = 50 + (self.transferred / self.total_size) * 50
            report_to_admin(self.task_id, pct, "",
                            self.admin_report_url)


async def run_mysql_backup(task_id: str, db_name: str, container_name: str, db_user: str, db_pass: str, backup_dir: str,
                           admin_report_url: str):
    try:
        os.makedirs(backup_dir, exist_ok=True)
        ts = time.strftime("%Y%m%d_%H%M%S")
        local_sql = os.path.join(backup_dir, f"{db_name}_{ts}.sql")
        local_gz = local_sql + ".gz"

        # 区分平台，选择脚本
        if sys.platform.startswith("win"):
            script_name = "mysql_backup.bat"
            cmd_prefix = ["cmd.exe", "/c"]
        else:
            script_name = "mysql_backup.sh"
            cmd_prefix = ["/bin/bash"]

        script_path = os.path.join(os.getcwd(), "scripts", script_name)
        if not os.path.exists(script_path):
            raise Exception(f"备份脚本不存在: {script_path}")

        # 参数顺序完全不变，和sh/bat保持一致
        proc_args = [
            *cmd_prefix,
            script_path,
            task_id,
            admin_report_url,
            container_name,
            str(db_user),
            str(db_pass),
            str(db_name),
            local_sql
        ]

        proc = await asyncio.create_subprocess_exec(
            *proc_args,
            stdout=asyncio.subprocess.PIPE,
            stderr=asyncio.subprocess.PIPE
        )
        stdout, stderr = await proc.communicate()
        ret_code = proc.returncode

        if ret_code != 0:
            raise Exception(f"备份脚本执行失败 code:{ret_code}, stderr:{stderr.decode('utf-8', errors='ignore')}")

        if not os.path.exists(local_sql):
            raise Exception(f"本地sql备份文件不存在: {local_sql}")

        # ========== 打包成gz，同步更新进度到60% ==========
        report_to_admin(task_id, 60, "sql导出完成，正在压缩为gz", admin_report_url)

        # 压缩放到线程池，避免阻塞async事件循环
        await asyncio.to_thread(_gzip_file, local_sql, local_gz)
        logger.info(f"sql压缩完成, taskId={task_id}, dbName={db_name}")

        # 压缩完成后可选：删除原始sql，节省磁盘
        os.remove(local_sql)

        if not os.path.exists(local_gz):
            raise Exception(f"gz压缩失败，文件不存在: {local_gz}")

        total_file_size = os.path.getsize(local_gz)

        bucket = config["s3"]["bucketName"]
        object_name = f"backup/{os.path.basename(local_gz)}"
        callback = Boto3UploadCallback(task_id, total_file_size, admin_report_url)

        # boto3上传放到线程池，不阻塞事件循环
        await asyncio.to_thread(
            s3_client.upload_file,
            Filename=local_gz,
            Bucket=bucket,
            Key=object_name,
            Callback=callback,
            Config=s3_transfer_config
        )
        logger.info(f"sql上传到s3完成, taskId={task_id}, dbName={db_name}")
        # 全部完成，上报100%
        report_to_admin(task_id, 100, f"{object_name}", admin_report_url)

    except Exception as e:
        stack_msg = traceback.format_exc()
        logger.error(f"run_mysql_backup任务异常 taskId={task_id}, err={e}\n堆栈:\n{stack_msg}")
        report_to_admin(task_id, -1, f"任务异常: {stack_msg}", admin_report_url)


async def run_mysql_clean(task_id: str, db_name: str, container_name: str, db_user: str, db_pass: str,
                          admin_report_url: str):
    try:
        # 区分平台，选择脚本
        if sys.platform.startswith("win"):
            script_name = "mysql_clean.bat"
            cmd_prefix = ["cmd.exe", "/c"]
        else:
            script_name = "mysql_clean.sh"
            cmd_prefix = ["/bin/bash"]

        script_path = os.path.join(os.getcwd(), "scripts", script_name)
        if not os.path.exists(script_path):
            raise Exception(f"清库脚本不存在: {script_path}")

        # 参数顺序完全不变，和sh/bat保持一致
        proc_args = [
            *cmd_prefix,
            script_path,
            container_name,
            str(db_user),
            str(db_pass),
            str(db_name),
        ]

        proc = await asyncio.create_subprocess_exec(
            *proc_args,
            stdout=asyncio.subprocess.PIPE,
            stderr=asyncio.subprocess.PIPE
        )
        stdout, stderr = await proc.communicate()
        ret_code = proc.returncode

        if ret_code != 0:
            raise Exception(f"备份脚本执行失败 code:{ret_code}, stderr:{stderr.decode('utf-8', errors='ignore')}")

        # 全部完成，上报100%
        report_to_admin(task_id, 100, f"", admin_report_url)

    except Exception as e:
        stack_msg = traceback.format_exc()
        logger.error(f"run_mysql_backup任务异常 taskId={task_id}, err={e}\n堆栈:\n{stack_msg}")
        report_to_admin(task_id, -1, f"任务异常: {stack_msg}", admin_report_url)


def _gzip_file(src: str, dst: str):
    """同步gzip压缩函数，放到to_thread里执行"""
    with open(src, "rb") as f_in:
        with gzip.open(dst, "wb") as f_out:
            f_out.writelines(f_in)


async def run_server_power(task_id: str, serverName: str, operation: str,
                           admin_report_url: str):
    try:
        # 区分平台，选择脚本
        if sys.platform.startswith("win"):
            pass
        else:
            script_name = "server_power.sh"
            cmd_prefix = ["/bin/bash"]

            script_path = os.path.join(os.getcwd(), "scripts", script_name)
            if not os.path.exists(script_path):
                raise Exception(f"服务器操作脚本不存在: {script_path}")

            # 参数顺序完全不变，和sh/bat保持一致
            proc_args = [
                *cmd_prefix,
                script_path,
                serverName,
                str(operation),
            ]

            proc = await asyncio.create_subprocess_exec(
                *proc_args,
                stdout=asyncio.subprocess.PIPE,
                stderr=asyncio.subprocess.PIPE
            )
            stdout, stderr = await proc.communicate()
            ret_code = proc.returncode

            if ret_code != 0:
                raise Exception(
                    f"服务器操作脚本执行失败 code:{ret_code}, stderr:{stderr.decode('utf-8', errors='ignore')}")
        # 全部完成，上报100%
        report_to_admin(task_id, 100, f"执行完毕", admin_report_url)

    except Exception as e:
        stack_msg = traceback.format_exc()
        logger.error(f"run_mysql_backup任务异常 taskId={task_id}, err={e}\n堆栈:\n{stack_msg}")
        report_to_admin(task_id, -1, f"任务异常: {stack_msg}", admin_report_url)


@app.post("/api/mysql_backup")
async def create_mysql_backup_task(req: DbOpTaskCreate, bg: BackgroundTasks):
    """SpringBoot后台调用此接口下发备份任务"""
    try:
        logger.info(f"收到备份任务请求, taskId={req.taskId}, dbName={req.dbName}")
        admin_report_url = config["admin"]["async_report_url"]
        bg.add_task(
            run_mysql_backup,
            req.taskId,
            req.dbName,
            "mysql",
            config["mysql"]["user_name"],
            config["mysql"]["password"],
            "backup",
            admin_report_url
        )
        return {"ok": True, "taskId": req.taskId, "msg": "备份任务已接收"}
    except Exception as e:
        import traceback
        stack_msg = traceback.format_exc()
        logger.error(f"/api/mysql_backup 接口异常: {e}\n堆栈:\n{stack_msg}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/mysql_clean")
async def create_mysql_clean_task(req: DbOpTaskCreate, bg: BackgroundTasks):
    """SpringBoot后台调用此接口下发清库任务"""
    try:
        logger.info(f"收到请库任务请求, taskId={req.taskId}, dbName={req.dbName}")
        admin_report_url = config["admin"]["async_report_url"]
        bg.add_task(
            run_mysql_clean,
            req.taskId,
            req.dbName,
            "mysql",
            config["mysql"]["user_name"],
            config["mysql"]["password"],
            admin_report_url
        )
        return {"ok": True, "taskId": req.taskId, "msg": "备份任务已接收"}
    except Exception as e:
        import traceback
        stack_msg = traceback.format_exc()
        logger.error(f"/api/mysql_backup 接口异常: {e}\n堆栈:\n{stack_msg}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/server_power")
async def server_power_task(req: ServerPowerTaskCreate, bg: BackgroundTasks):
    """SpringBoot后台调用此接口下发服务器电源操作任务"""
    try:
        logger.info(f"收到服务器操作任务请求, taskId={req.taskId}, serverName={req.serverName}")
        admin_report_url = config["admin"]["async_report_url"]
        bg.add_task(
            run_server_power,
            req.taskId,
            req.serverName,
            req.operation,
            admin_report_url
        )
        return {"ok": True, "taskId": req.taskId, "msg": "服务器电源操作任务已接收"}
    except Exception as e:
        import traceback
        stack_msg = traceback.format_exc()
        logger.error(f"/api/server_power 接口异常: {e}\n堆栈:\n{stack_msg}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/api/log_list")
async def log_list(log_root: str, sub_path: str = ""):
    try:
        target_dir = safe_join(log_root, sub_path)
        if not os.path.isdir(target_dir):
            raise HTTPException(404, f"目录不存在: {target_dir}")

        entries = []
        for name in os.listdir(target_dir):
            fp = os.path.join(target_dir, name)
            stat_info = os.stat(fp)
            entries.append({
                "name": name,
                "is_dir": os.path.isdir(fp),
                "size": stat_info.st_size,
                "mtime": stat_info.st_mtime
            })
        # 文件夹排在前面
        entries.sort(key=lambda x: (not x["is_dir"], x["name"]))
        return {"ok": True, "data": entries}
    except HTTPException:
        raise
    except Exception as e:
        logger.exception("list log dir error")
        raise HTTPException(500, str(e))


from fastapi.responses import StreamingResponse

@app.get("/api/log_stat")
async def log_stat(log_root: str, sub_path: str = ""):
    """查询单个日志文件的元信息（大小等），供后台判断内联预览还是下载"""
    try:
        target_file = safe_join(log_root, sub_path)
        if not os.path.isfile(target_file):
            raise HTTPException(404, f"文件不存在: {target_file}")

        stat_info = os.stat(target_file)
        entry = {
            "name": os.path.basename(target_file),
            "is_dir": False,
            "size": stat_info.st_size,
            "mtime": stat_info.st_mtime
        }
        return {"ok": True, "data": [entry]}
    except HTTPException:
        raise
    except Exception as e:
        logger.exception("stat log file error")
        raise HTTPException(500, str(e))


@app.get("/api/log_download")
async def log_download(log_root: str, sub_path: str):
    try:
        target_file = safe_join(log_root, sub_path)
        if not os.path.isfile(target_file):
            raise HTTPException(404, "文件不存在")

        # 取下载瞬间的大小做快照：既作为Content-Length告诉下游(admin/浏览器)文件真实长度，
        # 又用它限制实际发送的字节数，避免日志仍在写入时长度与响应体不一致
        file_size = os.path.getsize(target_file)

        def stream_file():
            remaining = file_size
            with open(target_file, "rb") as f:
                while remaining > 0:
                    chunk = f.read(min(1024 * 1024, remaining))
                    if not chunk:
                        break
                    remaining -= len(chunk)
                    yield chunk

        filename = os.path.basename(target_file)
        return StreamingResponse(
            stream_file(),
            media_type="application/octet-stream",
            headers={
                "Content-Disposition": f'attachment; filename="{filename}"',
                "Content-Length": str(file_size)
            }
        )
    except HTTPException:
        raise
    except Exception as e:
        logger.exception("download log error")
        raise HTTPException(500, str(e))


def safe_join(rel_root: str, sub_path: str):
    # 展开家目录
    home = os.path.expanduser("~")
    root = os.path.abspath(os.path.join(home, rel_root))
    full = os.path.abspath(os.path.join(root, sub_path))
    if not full.startswith(root):
        raise HTTPException(status_code=403, detail="路径越界禁止访问")
    return full


@app.on_event("startup")
def startup_event():
    global s3_client
    try:
        # 初始化boto3 s3 client对接minio
        s3_cfg = config["s3"]
        s3_client = boto3.client(
            "s3",
            endpoint_url=s3_cfg["endpoint"],
            aws_access_key_id=s3_cfg["accessKey"],
            aws_secret_access_key=s3_cfg["secretKey"],
            region_name=s3_cfg["region"]
        )
        bucket = s3_cfg["bucketName"]
        try:
            s3_client.head_bucket(Bucket=bucket)
        except Exception:
            s3_client.create_bucket(Bucket=bucket, CreateBucketConfiguration={'LocationConstraint': s3_cfg["region"]})

        # 启动指标定时采集
        start_scheduler(config["admin"]["metric_report_url"])
    except Exception as e:
        import traceback
        stack_msg = traceback.format_exc()
        logger.error(f"startup启动异常: {e}\n堆栈:\n{stack_msg}")
        raise


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("main:app", host="0.0.0.0", port=config["server"]["http_port"])
    logger.info("agent进程启动结束")
