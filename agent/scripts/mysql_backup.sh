#!/bin/bash
set -e

# 参数说明
# $1: taskId
# $2: springboot后台上报url
# $3: container_name
# $4: db_user
# $5: db_pass
# $6: db_name
# $7: output_sql_file

TASK_ID="$1"
REPORT_URL="$2"
CONTAINER="$3"
DB_USER="$4"
DB_PASS="$5"
DB_NAME="$6"
OUTPUT="$7"

# 上报开始
curl -s -X POST "${REPORT_URL}" \
-H "Content-Type:application/json" \
-d "{\"taskId\":\"${TASK_ID}\",\"progress\":0,\"message\":\"shell开始执行mysqldump\"}"

# mysqldump导出
docker exec "${CONTAINER}" sh -c "mysqldump -u${DB_USER} -p${DB_PASS} --default-character-set=utf8mb4 --routines --triggers --single-transaction ${DB_NAME}" > "${OUTPUT}"

# 本地备份完成，上报50%
curl -s -X POST "${REPORT_URL}" \
-H "Content-Type:application/json" \
-d "{\"taskId\":\"${TASK_ID}\",\"progress\":50,\"message\":\"本地mysqldump导出完成，准备minio上传\"}"
