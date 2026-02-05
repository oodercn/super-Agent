@echo off

echo 启动 Nexus 服务...

rem 检查是否已构建
if not exist "target\nexus-0.6.5.jar" (
    echo 正在构建项目...
    mvn clean package -DskipTests
    if %errorlevel% neq 0 (
        echo 构建失败！
        pause
        exit /b %errorlevel%
    )
)

echo 启动 Nexus 服务...
java -jar target\nexus-0.6.5.jar

pause
