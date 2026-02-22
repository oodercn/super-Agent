#!/bin/sh
# ooderNexus 启动脚本

INSTALL_DIR="/opt/ooder-nexus"
JAR_FILE="$INSTALL_DIR/lib/ooder-nexus.jar"
CONFIG_FILE="$INSTALL_DIR/config/application.yml"
LOGS_DIR="$INSTALL_DIR/logs"
PID_FILE="/var/run/ooder-nexus.pid"

# Java 参数
JAVA_OPTS="-Xmx128m -Xms64m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 检查 Java
if command -v java >/dev/null 2>&1; then
    JAVA_CMD="java"
elif [ -x "$INSTALL_DIR/jre/bin/java" ]; then
    JAVA_CMD="$INSTALL_DIR/jre/bin/java"
else
    echo "错误: 未找到 Java 运行时环境"
    exit 1
fi

# 创建日志目录
mkdir -p $LOGS_DIR

# 启动服务
echo "正在启动 ooderNexus..."
cd $INSTALL_DIR

nohup $JAVA_CMD $JAVA_OPTS \
    -Dspring.config.location=file:$CONFIG_FILE \
    -Dlogging.file.path=$LOGS_DIR \
    -jar $JAR_FILE \
    > /dev/null 2>&1 &

# 保存 PID
PID=$!
echo $PID > $PID_FILE

echo "ooderNexus 已启动 (PID: $PID)"
echo "日志文件: $LOGS_DIR/system.log"
