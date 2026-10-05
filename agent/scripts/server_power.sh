#!/bin/bash
set -e

# 参数校验，必须传2个参数：项目目录、操作指令
if [ $# -ne 2 ]; then
    echo "用法: $0 项目目录 操作指令"
    echo "示例: $0 /home/ubuntu/gameapp restart"
    exit 1
fi

# 第一层：进入工具根目录
TOOL_ROOT="/home/ubuntu/"
cd "${TOOL_ROOT}"

# 第二层：进入传入的项目目录
PROJECT_DIR="$1"
OPER="$2"

if [ ! -d "${PROJECT_DIR}" ]; then
    echo "ERROR: 项目目录 ${PROJECT_DIR} 不存在"
    exit 1
fi
cd "${PROJECT_DIR}"

SERVICE_SCRIPT="./service.sh"
if [ ! -f "${SERVICE_SCRIPT}" ]; then
    echo "ERROR: ${SERVICE_SCRIPT} 文件不存在"
    exit 1
fi
if [ ! -x "${SERVICE_SCRIPT}" ]; then
    echo "ERROR: ${SERVICE_SCRIPT} 缺少执行权限"
    exit 1
fi

./service.sh "${OPER}"
