#!/bin/bash

echo "========================================"
echo "  ApexOS v1.0.0 GraalVM Native Image 构建"
echo "========================================"
echo ""

if ! command -v native-image &> /dev/null; then
    echo "[错误] 未检测到 native-image 命令"
    echo "请安装 GraalVM 并配置 native-image"
    echo ""
    echo "安装步骤:"
    echo "1. 下载 GraalVM: https://www.graalvm.org/downloads/"
    echo "2. 设置 JAVA_HOME 指向 GraalVM"
    echo "3. 安装 native-image: gu install native-image"
    exit 1
fi

echo "[1/4] 检测环境..."
echo "GraalVM: $(java -version 2>&1 | head -n 1)"
echo "Native Image: $(native-image --version 2>&1 | head -n 1)"
echo ""

echo "[2/4] 清理旧构建..."
mvn clean -q
echo "[OK] 清理完成"
echo ""

echo "[3/4] 编译 Native Image (这可能需要几分钟)..."
echo ""

mvn -Pnative -DskipTests package 2>&1 | tee build.log

if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] Native Image 构建失败"
    echo "请查看 build.log 获取详细错误信息"
    exit 1
fi

echo ""
echo "[4/4] 验证构建结果..."
if [ -f "target/apexos-native" ]; then
    SIZE=$(ls -lh target/apexos-native | awk '{print $5}')
    echo "[OK] Native Image 构建成功!"
    echo ""
    echo "========================================"
    echo "  构建完成"
    echo "========================================"
    echo ""
    echo "输出文件: target/apexos-native"
    echo "文件大小: $SIZE"
    echo ""
    echo "运行方式:"
    echo "  ./target/apexos-native"
    echo ""
else
    echo "[错误] 未找到构建产物"
    exit 1
fi
