#!/bin/sh
# ooderNexus OpenWrt 一键安装脚本
# 版本: 2.0.0
# 支持: OpenWrt 21.02+

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置
VERSION="2.0"
INSTALL_DIR="/opt/ooder-nexus"
DATA_DIR="$INSTALL_DIR/data"
LOGS_DIR="$INSTALL_DIR/logs"
DOWNLOAD_URL="https://github.com/oodercn/ooder-Nexus/releases/download/v${VERSION}"

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查系统要求
check_requirements() {
    log_info "检查系统要求..."
    
    # 检查 OpenWrt 版本
    if [ -f /etc/openwrt_release ]; then
        . /etc/openwrt_release
        log_info "检测到 OpenWrt 版本: $DISTRIB_RELEASE"
    else
        log_error "未检测到 OpenWrt 系统"
        exit 1
    fi
    
    # 检查可用内存
    MEM_AVAILABLE=$(free | grep Mem | awk '{print $7}')
    if [ "$MEM_AVAILABLE" -lt 65536 ]; then  # 64MB
        log_warn "可用内存不足 64MB，建议创建交换分区"
        read -p "是否创建 512MB 交换分区? (y/n): " create_swap
        if [ "$create_swap" = "y" ]; then
            create_swap_partition
        fi
    fi
    
    # 检查存储空间
    DISK_AVAILABLE=$(df /opt | tail -1 | awk '{print $4}')
    if [ "$DISK_AVAILABLE" -lt 102400 ]; then  # 100MB
        log_error "存储空间不足 100MB，请清理空间或挂载 USB 存储"
        exit 1
    fi
    
    log_info "系统检查通过"
}

# 创建交换分区
create_swap_partition() {
    log_info "创建 512MB 交换分区..."
    dd if=/dev/zero of=/swap bs=1M count=512 2>/dev/null
    mkswap /swap
    swapon /swap
    echo '/swap none swap sw 0 0' >> /etc/fstab
    log_info "交换分区创建完成"
}

# 安装依赖
install_dependencies() {
    log_info "安装依赖..."
    opkg update
    
    # 检查并安装必要的包
    for pkg in wget curl tar; do
        if ! command -v $pkg >/dev/null 2>&1; then
            log_info "安装 $pkg..."
            opkg install $pkg
        fi
    done
    
    # 检查 Java 环境
    if ! command -v java >/dev/null 2>&1; then
        log_info "安装 Java 环境..."
        opkg install java-jre || {
            log_warn "OpenWrt 软件源中没有 Java，将使用嵌入式 JRE"
        }
    fi
    
    log_info "依赖安装完成"
}

# 下载安装包
download_package() {
    log_info "下载 ooderNexus v${VERSION}..."
    
    # 检测架构
    ARCH=$(uname -m)
    case $ARCH in
        x86_64|amd64)
            ARCH_NAME="x86_64"
            ;;
        aarch64|arm64)
            ARCH_NAME="aarch64"
            ;;
        armv7l|armhf)
            ARCH_NAME="armv7"
            ;;
        mips|mipsel)
            ARCH_NAME="mips"
            ;;
        *)
            log_warn "未知架构: $ARCH，使用通用版本"
            ARCH_NAME="generic"
            ;;
    esac
    
    PACKAGE_NAME="ooder-nexus-${VERSION}-openwrt-${ARCH_NAME}.tar.gz"
    DOWNLOAD_LINK="${DOWNLOAD_URL}/${PACKAGE_NAME}"
    
    # 创建临时目录
    TEMP_DIR=$(mktemp -d)
    cd $TEMP_DIR
    
    # 下载
    log_info "下载地址: $DOWNLOAD_LINK"
    wget --progress=bar:force -O $PACKAGE_NAME "$DOWNLOAD_LINK" 2>&1 || {
        log_error "下载失败，尝试备用地址..."
        wget --progress=bar:force -O $PACKAGE_NAME "${DOWNLOAD_URL}/ooder-nexus-${VERSION}-openwrt.tar.gz" 2>&1 || {
            log_error "下载失败，请检查网络连接"
            exit 1
        }
    }
    
    log_info "下载完成"
}

# 安装
do_install() {
    log_info "开始安装..."
    
    # 停止现有服务
    if [ -f /etc/init.d/ooder-nexus ]; then
        log_info "停止现有服务..."
        /etc/init.d/ooder-nexus stop 2>/dev/null || true
    fi
    
    # 备份数据
    if [ -d $DATA_DIR ]; then
        log_info "备份现有数据..."
        BACKUP_DIR="/tmp/ooder-nexus-backup-$(date +%Y%m%d%H%M%S)"
        cp -r $DATA_DIR $BACKUP_DIR
        log_info "数据已备份到: $BACKUP_DIR"
    fi
    
    # 创建安装目录
    mkdir -p $INSTALL_DIR
    cd $INSTALL_DIR
    
    # 解压
    log_info "解压安装包..."
    tar -xzf ${TEMP_DIR}/${PACKAGE_NAME} --strip-components=1
    
    # 创建数据目录
    mkdir -p $DATA_DIR
    mkdir -p $LOGS_DIR
    
    # 恢复数据
    if [ -d "$BACKUP_DIR" ]; then
        log_info "恢复数据..."
        cp -r $BACKUP_DIR/* $DATA_DIR/
        rm -rf $BACKUP_DIR
    fi
    
    # 设置权限
    chmod +x $INSTALL_DIR/bin/*.sh 2>/dev/null || true
    chmod +x $INSTALL_DIR/init.d/ooder-nexus
    
    # 安装启动脚本
    cp $INSTALL_DIR/init.d/ooder-nexus /etc/init.d/
    chmod +x /etc/init.d/ooder-nexus
    
    # 启用开机启动
    /etc/init.d/ooder-nexus enable
    
    log_info "安装完成"
}

# 启动服务
start_service() {
    log_info "启动 ooderNexus 服务..."
    /etc/init.d/ooder-nexus start
    
    # 等待服务启动
    sleep 3
    
    # 检查服务状态
    if pgrep -f "ooder-nexus" >/dev/null; then
        log_info "服务启动成功"
    else
        log_error "服务启动失败，请检查日志: $LOGS_DIR/error.log"
        exit 1
    fi
}

# 显示安装信息
show_info() {
    IP=$(ip route get 1.1.1.1 2>/dev/null | grep -oP 'src \K\S+' || echo "路由器IP")
    
    echo ""
    echo "========================================"
    echo -e "${GREEN}ooderNexus 安装成功!${NC}"
    echo "========================================"
    echo ""
    echo "访问地址:"
    echo "  - Web 控制台: http://${IP}:8091/console/index.html"
    echo "  - API 文档:   http://${IP}:8091/swagger-ui.html"
    echo ""
    echo "安装目录: $INSTALL_DIR"
    echo "数据目录: $DATA_DIR"
    echo "日志目录: $LOGS_DIR"
    echo ""
    echo "常用命令:"
    echo "  - 启动服务: /etc/init.d/ooder-nexus start"
    echo "  - 停止服务: /etc/init.d/ooder-nexus stop"
    echo "  - 重启服务: /etc/init.d/ooder-nexus restart"
    echo "  - 查看状态: /etc/init.d/ooder-nexus status"
    echo ""
    echo "========================================"
}

# 清理
cleanup() {
    if [ -d "$TEMP_DIR" ]; then
        rm -rf $TEMP_DIR
    fi
}

# 主函数
main() {
    echo "========================================"
    echo "ooderNexus OpenWrt 一键安装脚本"
    echo "版本: $VERSION"
    echo "========================================"
    echo ""
    
    # 检查是否以 root 运行
    if [ "$(id -u)" -ne 0 ]; then
        log_error "请以 root 用户运行此脚本"
        exit 1
    fi
    
    # 执行安装步骤
    check_requirements
    install_dependencies
    download_package
    do_install
    start_service
    show_info
    cleanup
    
    log_info "安装脚本执行完毕"
}

# 运行主函数
main "$@"
