@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo   ApexOS v1.0.0 安装程序
echo   MIT License - Open Source AI Agent OS
echo ========================================
echo.

echo [1/6] 检测系统环境: Windows

where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [错误] 未检测到 Java 运行环境
    echo 请安装 Java 21 或更高版本
    echo 下载地址: https://adoptium.net/
    exit /b 1
)

for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VER=%%v
    goto :check_java
)
:check_java
set JAVA_VER=%JAVA_VER:"=%
for /f "delims=. tokens=1" %%v in ("%JAVA_VER%") do set JAVA_MAJOR=%%v

if %JAVA_MAJOR% lss 21 (
    echo [错误] Java 版本过低，需要 Java 21 或更高版本
    echo 当前版本: %JAVA_VER%
    exit /b 1
)
echo [OK] Java 版本: %JAVA_VER%

where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [错误] 未检测到 Maven
    echo 请安装 Maven 3.6+
    echo 下载地址: https://maven.apache.org/download.cgi
    exit /b 1
)
echo [OK] Maven 已安装

echo.
echo [2/6] 创建必要目录...
if not exist "logs" mkdir logs
if not exist "data" mkdir data
if not exist "config" mkdir config
if not exist "plugins" mkdir plugins
echo [OK] 目录创建完成

echo.
echo [3/6] 编译项目...
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [错误] 编译失败，请检查错误信息
    exit /b 1
)
echo [OK] 编译完成

echo.
echo [4/6] 检查配置文件...
if not exist "config\application.yml" (
    if exist "src\main\resources\application.yml" (
        copy "src\main\resources\application.yml" "config\" >nul
        echo [OK] 已复制默认配置文件到 config\
    )
) else (
    echo [OK] 配置文件已存在
)

echo.
echo [5/6] 安装完成检查...
dir /b target\apexos-*.jar >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [错误] 未找到编译后的 JAR 文件
    exit /b 1
)
for %%f in (target\apexos-*.jar) do set JAR_FILE=%%f
echo [OK] JAR 文件: !JAR_FILE!

echo.
echo ========================================
echo   ApexOS v1.0.0 安装成功!
echo ========================================
echo.
echo 启动方式:
echo   start.bat          # 启动服务
echo.
echo 停止服务:
echo   关闭命令行窗口或按 Ctrl+C
echo.
echo 配置文件:
echo   config\application.yml
echo.
echo 日志目录:
echo   logs\
echo.
pause
