set -e

# ========== 配置项，按需修改 ==========
PROJECT_DIR=$(pwd)
PID_FILE="${PROJECT_DIR}/agent.pid"
MAIN_SCRIPT="main.py"
# ======================================

cd "${PROJECT_DIR}" || { echo "项目目录不存在: ${PROJECT_DIR}"; exit 1; }

# 1. 安装pipenv依赖，指定使用系统python3.11，跳过Pipfile里的python版本要求
echo ">>> 检查并安装pipenv依赖"
if [ ! -f "Pipfile.lock" ]; then
    echo "ERROR: Pipfile.lock 不存在，请先在开发环境生成"
    exit 1
fi
pipenv install --ignore-pipfile --python 3.12

# 2. 检查旧进程，存在则终止
if [ -f "${PID_FILE}" ]; then
    OLD_PID=$(cat "${PID_FILE}")
    if ps -p "${OLD_PID}" > /dev/null; then
        echo ">>> 发现正在运行的进程 PID:${OLD_PID}，准备停止"
        kill "${OLD_PID}"
        sleep 2
        # 兜底强制杀残留
        if ps -p "${OLD_PID}" > /dev/null; then
            kill -9 "${OLD_PID}"
        fi
        echo ">>> 旧进程已停止"
    fi
    rm -f "${PID_FILE}"
fi

# 3. 后台启动，日志交给python logger自身管理
echo ">>> 启动 agent 服务"
export APP_ENV=test
nohup pipenv run python3 "${MAIN_SCRIPT}" > /dev/null 2>&1 &
NEW_PID=$!
echo "${NEW_PID}" > "${PID_FILE}"

echo ">>> Agent 已启动，PID=${NEW_PID}"