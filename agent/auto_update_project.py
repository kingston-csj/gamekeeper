import requests
import time

# ====================== 配置区 ======================
POLL_URL = "http://106.55.7.154:8080/api/fetchProjectUpdate"
# URL模板，占位符 project
URL_TPL = "http://192.168.1.123:9090/buildByToken/build?job=project%E5%A4%96%E7%BD%91%E4%B8%80%E9%94%AE%E6%9B%B4%E6%96%B0%E9%87%8D%E5%90%AF&token=project"
POLL_INTERVAL = 10
last_data = None
# ====================================================

def trigger_jenkins(placeholder_val):
    jenkins_url = URL_TPL.replace("project", placeholder_val)
    try:
        # 超时10秒，防止jenkins无响应卡住轮询
        resp = requests.post(jenkins_url, timeout=10)
        print(f"[Jenkins] url={jenkins_url}")
        if resp.status_code == 201:
            print("[Jenkins] ✅ 构建入队成功")
        else:
            print(f"[Jenkins] status={resp.status_code}, resp={resp.text}")
    except Exception as e:
        print(f"[Jenkins] ❌ 请求异常: {str(e)}")


def poll_loop():
    print(f"开始轮询 {POLL_URL}, 间隔 {POLL_INTERVAL}s")
    while True:
        try:
            resp = requests.get(POLL_URL, timeout=10)
            if resp.status_code != 200:
                print(f"[Poll] HTTP错误码: {resp.status_code}")
                time.sleep(POLL_INTERVAL)
                continue
            json_data = resp.json()
            data = json_data.get("data")

            # 修复：先判断类型，只有字符串才strip
            need_trigger = False
            if isinstance(data, str):
                data_stripped = data.strip()
                if data_stripped != "":
                    need_trigger = True
            # data不为None，但不是字符串（数字等）直接跳过
            if need_trigger:
                print(f"[Poll] 📥 获取任务 data={data}")
                trigger_jenkins(data)
        except Exception as e:
            print(f"[Poll] ❌ 轮询异常: {str(e)}")

        time.sleep(POLL_INTERVAL)


if __name__ == "__main__":
    poll_loop()
