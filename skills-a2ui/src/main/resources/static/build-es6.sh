#!/bin/bash

# OOD ES6 模块化构建脚本
# 用于构建和验证 ES6 模块系统

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Node.js
if ! command -v node &> /dev/null; then
    log_error "Node.js 未安装，请先安装 Node.js"
    exit 1
fi

# 检查 npm
if ! command -v npm &> /dev/null; then
    log_error "npm 未安装，请先安装 npm"
    exit 1
fi

log_info "Node.js 版本: $(node --version)"
log_info "npm 版本: $(npm --version)"

# 步骤 1: 安装依赖
log_info "步骤 1/5: 安装依赖..."
if [ ! -d "node_modules" ]; then
    npm install
    log_success "依赖安装完成"
else
    log_info "依赖已存在，跳过安装"
fi

# 步骤 2: 清理旧的构建产物
log_info "步骤 2/5: 清理旧的构建产物..."
rm -rf dist
mkdir -p dist
log_success "清理完成"

# 步骤 3: 构建开发版本
log_info "步骤 3/5: 构建开发版本..."
npm run build:dev
log_success "开发版本构建完成"

# 步骤 4: 构建生产版本
log_info "步骤 4/5: 构建生产版本..."
npm run build
log_success "生产版本构建完成"

# 步骤 5: 验证构建产物
log_info "步骤 5/5: 验证构建产物..."

# 检查必需的文件
required_files=(
    "dist/ood-es6.js"
    "dist/ood-compat.js"
    "dist/ood-es6.min.js"
    "dist/ood-compat.min.js"
)

all_exist=true
for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        log_success "✓ $file"
    else
        log_error "✗ $file (缺失)"
        all_exist=false
    fi
done

if [ "$all_exist" = false ]; then
    log_error "构建验证失败：缺少必需的文件"
    exit 1
fi

# 检查文件大小
log_info "文件大小检查:"
for file in dist/*.js; do
    if [ -f "$file" ]; then
        size=$(du -h "$file" | cut -f1)
        echo "  $file: $size"
    fi
done

# 完成
echo ""
log_success "==================================="
log_success "构建和验证完成！"
log_success "==================================="
echo ""
log_info "下一步："
log_info "  1. 打开 test-cookies.html 测试兼容性"
log_info "  2. 打开 index-webpack-test.html 测试完整系统"
log_info "  3. 运行 npm run analyze 分析打包体积"
log_info "  4. 运行 npm run dev 启动开发服务器"
echo ""
