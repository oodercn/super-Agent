@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

set APP_NAME=ApexOS
set APP_VERSION=1.0.0
set JAR_NAME=apexos-%APP_VERSION%.jar

echo ========================================
echo   %APP_NAME% v%APP_VERSION%
echo ========================================
echo.

if exist "target\%JAR_NAME%" (
    set JAR_FILE=target\%JAR_NAME%
) else (
    for %%f in (target\apexos-*.jar) do set JAR_FILE=%%f
)

if not defined JAR_FILE (
    echo [错误] 未找到 JAR 文件，请先运行 install.bat
    pause
    exit /b 1
)

echo [启动] 正在启动 %APP_NAME%...
echo [提示] 按 Ctrl+C 停止服务
echo.

set JAVA_OPTS=-Xms512m -Xmx1024m
set JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF-8
set JAVA_OPTS=%JAVA_OPTS% -Dspring.config.location=config/

java --enable-preview %JAVA_OPTS% -jar %JAR_FILE%
