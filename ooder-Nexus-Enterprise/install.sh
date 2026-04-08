#!/bin/bash

echo "========================================"
echo "  ApexOS v1.0.0 安装程序"
echo "  MIT License - Open Source AI Agent OS"
echo "========================================"
echo ""

OS="$(uname -s)"
case "${OS}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    CYGWIN*)    MACHINE=Cygwin;;
    MINGW*)     MACHINE=MinGw;;
    *)          MACHINE="UNKNOWN:${OS}"
esac

echo "[1/6] 检测系统环境: $MACHINE"

if ! command -v java &> /dev/null; then
    echo "[错误] 未检测到 Java 运行环境"
    echo "请安装 Java 21 或更高版本"
    echo "下载地址: https://adoptium.net/"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "[错误] Java 版本过低，需要 Java 21 或更高版本"
    echo "当前版本: $(java -version 2>&1 | head -n 1)"
    exit 1
fi
echo "[OK] Java 版本: $(java -version 2>&1 | head -n 1)"

if ! command -v mvn &> /dev/null; then
    echo "[错误] 未检测到 Maven"
    echo "请安装 Maven 3.6+ "
    echo "下载地址: https://maven.apache.org/download.cgi"
    exit 1
fi
echo "[OK] Maven 版本: $(mvn -version 2>&1 | head -n 1)"

echo ""
echo "[2/6] 创建必要目录..."
mkdir -p logs
mkdir -p data
mkdir -p config
mkdir -p plugins
echo "[OK] 目录创建完成"

echo ""
echo "[3/6] 编译项目..."
mvn clean package -DskipTests -q
if [ $? -ne 0 ]; then
    echo "[错误] 编译失败，请检查错误信息"
    exit 1
fi
echo "[OK] 编译完成"

echo ""
echo "[4/6] 检查配置文件..."
if [ ! -f "config/application.yml" ]; then
    if [ -f "src/main/resources/application.yml" ]; then
        cp src/main/resources/application.yml config/
        echo "[OK] 已复制默认配置文件到 config/"
    fi
else
    echo "[OK] 配置文件已存在"
fi

echo ""
echo "[5/6] 设置文件权限..."
chmod +x start.sh 2>/dev/null
chmod +x stop.sh 2>/dev/null
chmod -R 755 logs data config plugins 2>/dev/null
echo "[OK] 权限设置完成"

echo ""
echo "[6/6] 安装完成检查..."
JAR_FILE=$(find target -name "apexos-*.jar" -type f | head -n 1)
if [ -z "$JAR_FILE" ]; then
    echo "[错误] 未找到编译后的 JAR 文件"
    exit 1
fi
echo "[OK] JAR 文件: $JAR_FILE"

echo ""
echo "========================================"
echo "  ApexOS v1.0.0 安装成功!"
echo "========================================"
echo ""
echo "启动方式:"
echo "  ./start.sh          # 前台启动"
echo "  ./start.sh -d       # 后台启动"
echo ""
echo "停止服务:"
echo "  ./stop.sh"
echo ""
echo "配置文件:"
echo "  config/application.yml"
echo ""
echo "日志目录:"
echo "  logs/"
echo ""
