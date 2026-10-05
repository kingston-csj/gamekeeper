#!/bin/bash
# 参数说明
# $1: CONTAINER_NAME MySQL容器ID/名称
# $2: DB_USER
# $3: DB_PASSWORD
# $4: DB_NAME
if [ $# -ne 4 ]; then
    echo "参数错误！用法: $0 CONTAINER_NAME DB_USER DB_PASSWORD DB_NAME"
    exit 1
fi

CONTAINER_NAME="$1"
DB_USER="$2"
DB_PASSWORD="$3"
DB_NAME="$4"

# 函数：显示错误信息并退出
error_exit() {
    echo "ERROR: $1" >&2
    exit 1
}

# 1. 检查容器是否存在
if ! sudo docker inspect "$CONTAINER_NAME" &>/dev/null; then
    error_exit "容器 '$CONTAINER_NAME' 不存在，请检查名称或ID"
fi

# 2. 检查容器是否在运行，未运行则启动
if [ "$(sudo docker inspect -f '{{.State.Running}}' "$CONTAINER_NAME")" != "true" ]; then
    echo "容器 '$CONTAINER_NAME' 未运行，尝试启动..."
    if ! sudo docker start "$CONTAINER_NAME"; then
        error_exit "启动容器 '$CONTAINER_NAME' 失败"
    fi
    # 等待MySQL就绪
    sleep 5
fi

# 3. 检查目标数据库是否存在
echo "检查数据库 '$DB_NAME' 是否存在..."
DB_EXISTS=$(sudo docker exec "$CONTAINER_NAME" mysql -u"$DB_USER" -p"$DB_PASSWORD" -N -e "SHOW DATABASES LIKE '$DB_NAME'" 2>/dev/null | grep -c "$DB_NAME")
if [ "$DB_EXISTS" -eq 0 ]; then
    error_exit "数据库 '$DB_NAME' 不存在，无需清除"
fi

# 跳过交互式确认（agent调用，不能read等待输入）
echo "开始清除数据库 '$DB_NAME'..."
sudo docker exec "$CONTAINER_NAME" sh -c "
mysql -u'$DB_USER' -p'$DB_PASSWORD' -e '
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
' 2>/dev/null
"

# 4. 验证结果
echo "验证清除结果..."
DB_AFTER=$(sudo docker exec "$CONTAINER_NAME" mysql -u"$DB_USER" -p"$DB_PASSWORD" -N -e "SHOW DATABASES LIKE '$DB_NAME'" 2>/dev/null | grep -c "$DB_NAME")
if [ "$DB_AFTER" -eq 1 ]; then
    echo "SUCCESS: 数据库 '$DB_NAME' 已成功清除（重建空库）"
    exit 0
else
    error_exit "清除数据库 '$DB_NAME' 失败，请手动检查"
fi
