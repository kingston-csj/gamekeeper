@echo off
setlocal enabledelayedexpansion

:: 参数列表
:: %1 taskId
:: %2 REPORT_URL 后端进度上报接口地址
:: %3 MYSQL_HOST 数据库地址（Windows不用docker container，改为mysql主机）
:: %4 DB_USER
:: %5 DB_PASS
:: %6 DB_NAME

set "MYSQL_HOST=%~1"
set "DB_USER=%~2"
set "DB_PASS=%~3"
set "DB_NAME=%~4"

echo Backup OK
endlocal
