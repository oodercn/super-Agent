#!/bin/bash

APP_NAME="ApexOS"
APP_VERSION="1.0.0"
JAR_NAME="apexos-${APP_VERSION}.jar"
PID_FILE="logs/apexos.pid"
LOG_FILE="logs/apexos.log"
DAEMON_MODE=false

while getopts "d" opt; do
    case $opt in
        d) DAEMON_MODE=true ;;
        *) echo "用法: $0 [-d]"; exit 1 ;;
    esac
done

echo "========================================"
echo "  $APP_NAME v$APP_VERSION"
echo "========================================"
echo ""

if [ ! -f "target/$JAR_NAME" ]; then
    JAR_FILE=$(find target -name "apexos-*.jar" -type f | head -n 1)
    if [ -z "$JAR_FILE" ]; then
        echo "[错误] 未找到 JAR 文件，请先运行 ./install.sh"
        exit 1
    fi
else
    JAR_FILE="target/$JAR_NAME"
fi

if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID > /dev/null 2>&1; then
        echo "[警告] $APP_NAME 已在运行中 (PID: $PID)"
        exit 0
    else
        rm -f "$PID_FILE"
    fi
fi

mkdir -p logs

JAVA_OPTS="-Xms512m -Xmx1024m"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=config/"

if [ "$DAEMON_MODE" = true ]; then
    echo "[启动] 以守护进程模式启动 $APP_NAME..."
    nohup java --enable-preview $JAVA_OPTS -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &
    echo $! > "$PID_FILE"
    sleep 2
    if ps -p $(cat "$PID_FILE") > /dev/null 2>&1; then
        echo "[OK] $APP_NAME 启动成功 (PID: $(cat $PID_FILE))"
        echo "[日志] tail -f $LOG_FILE"
    else
        echo "[错误] $APP_NAME 启动失败，请查看日志"
        cat "$LOG_FILE"
        exit 1
    fi
else
    echo "[启动] 以前台模式启动 $APP_NAME..."
    echo "[提示] 按 Ctrl+C 停止服务"
    echo ""
    java --enable-preview $JAVA_OPTS -jar "$JAR_FILE"
fi
