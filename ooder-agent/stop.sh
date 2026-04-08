#!/bin/bash

APP_NAME="ApexOS"
PID_FILE="logs/apexos.pid"

echo "========================================"
echo "  停止 $APP_NAME"
echo "========================================"
echo ""

if [ ! -f "$PID_FILE" ]; then
    echo "[警告] 未找到 PID 文件，服务可能未运行"
    exit 0
fi

PID=$(cat "$PID_FILE")

if ! ps -p $PID > /dev/null 2>&1; then
    echo "[警告] 进程 $PID 不存在，清理 PID 文件"
    rm -f "$PID_FILE"
    exit 0
fi

echo "[停止] 正在停止 $APP_NAME (PID: $PID)..."
kill $PID

for i in {1..10}; do
    if ! ps -p $PID > /dev/null 2>&1; then
        rm -f "$PID_FILE"
        echo "[OK] $APP_NAME 已停止"
        exit 0
    fi
    sleep 1
    echo "[等待] 等待进程退出... ($i/10)"
done

echo "[警告] 进程未响应，强制终止..."
kill -9 $PID
rm -f "$PID_FILE"
echo "[OK] $APP_NAME 已强制停止"
