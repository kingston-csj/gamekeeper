@echo off
setlocal enabledelayedexpansion

:: 参数列表
:: %1 taskId
:: %2 REPORT_URL 后端进度上报接口地址
:: %3 MYSQL_HOST 数据库地址（Windows不用docker container，改为mysql主机）
:: %4 DB_USER
:: %5 DB_PASS
:: %6 DB_NAME
:: %7 OUTPUT_SQL_FILE

set "TASK_ID=%~1"
set "REPORT_URL=%~2"
set "MYSQL_HOST=%~3"
set "DB_USER=%~4"
set "DB_PASS=%~5"
set "DB_NAME=%~6"
set "OUTPUT_SQL=%~7"

:: 上报进度0%：开始执行
curl -s -X POST "%REPORT_URL%" ^
-H "Content-Type:application/json" ^
-d "{\"taskId\":\"%TASK_ID%\",\"progress\":0,\"message\":\"bat脚本开始执行mysqldump\"}"

:: 执行 mysqldump
mysqldump -h127.0.0.1 -u%DB_USER% -p%DB_PASS% --default-character-set=utf8mb4 --routines --triggers --single-transaction %DB_NAME% > "%OUTPUT_SQL%"
if %errorlevel% neq 0 (
    :: 执行失败，上报 progress=-1
    curl -s -X POST "%REPORT_URL%" ^
    -H "Content-Type:application/json" ^
    -d "{\"taskId\":\"%TASK_ID%\",\"progress\":-1,\"message\":\"mysqldump导出失败，errorlevel:%errorlevel%\"}"
    exit /b 1
)

:: 导出成功，上报50%
curl -s -X POST "%REPORT_URL%" ^
-H "Content-Type:application/json" ^
-d "{\"taskId\":\"%TASK_ID%\",\"progress\":50,\"message\":\"本地mysqldump导出完成，准备minio上传\"}"

echo Backup OK
endlocal
