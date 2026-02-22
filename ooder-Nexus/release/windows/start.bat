@echo off
chcp 65001 >nul
echo ==========================================
echo   ooderNexus 2.0.0-openwrt-preview
echo   Windows 启动脚本
echo ==========================================
echo.

:: 检查 Java 环境
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到 Java 环境，请先安装 Java 8 或更高版本
    pause
    exit /b 1
)

echo [信息] Java 环境检测通过
echo [信息] 正在启动 ooderNexus 服务...
echo [信息] 访问地址: http://localhost:8081/console/index.html
echo.

:: 启动服务
java -jar ooder-nexus-2.0.0-openwrt-preview.jar

pause
