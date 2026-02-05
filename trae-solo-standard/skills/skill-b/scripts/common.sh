#!/bin/bash

# 通用工具函数

# 配置
OODER_AGENT_URL="http://localhost:9010/api/v1"
TIMEOUT=30
RETRY_COUNT=3
RETRY_DELAY=1000

# 日志级别
LOG_LEVEL="info" # debug, info, warn, error

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 日志函数
log() {
    local level=$1
    local message=$2
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    
    case $level in
        "debug")
            if [[ "$LOG_LEVEL" == "debug" ]]; then
                echo -e "${BLUE}[DEBUG] ${timestamp}${NC} ${message}"
            fi
            ;;
        "info")
            if [[ "$LOG_LEVEL" == "debug" || "$LOG_LEVEL" == "info" ]]; then
                echo -e "${GREEN}[INFO] ${timestamp}${NC} ${message}"
            fi
            ;;
        "warn")
            if [[ "$LOG_LEVEL" == "debug" || "$LOG_LEVEL" == "info" || "$LOG_LEVEL" == "warn" ]]; then
                echo -e "${YELLOW}[WARN] ${timestamp}${NC} ${message}"
            fi
            ;;
        "error")
            echo -e "${RED}[ERROR] ${timestamp}${NC} ${message}"
            ;;
        *)
            echo -e "${GREEN}[INFO] ${timestamp}${NC} ${message}"
            ;;
    esac
}

# 错误处理函数
handle_error() {
    local error_message=$1
    local exit_code=${2:-1}
    
    log "error" "错误: $error_message"
    log "error" "退出代码: $exit_code"
    
    # 可以在这里添加更多错误处理逻辑，如发送告警等
    
    return $exit_code
}

# 重试函数
retry() {
    local command=$1
    local max_attempts=${2:-$RETRY_COUNT}
    local delay=${3:-$RETRY_DELAY}
    local attempt=1
    local exit_code=0
    
    while [[ $attempt -le $max_attempts ]]; do
        log "info" "尝试执行命令 (尝试 $attempt/$max_attempts): $command"
        
        # 执行命令并捕获退出代码
        eval "$command"
        exit_code=$?
        
        if [[ $exit_code -eq 0 ]]; then
            log "info" "命令执行成功"
            return 0
        else
            log "warn" "命令执行失败，退出代码: $exit_code"
            
            if [[ $attempt -lt $max_attempts ]]; then
                log "info" "等待 ${delay}ms 后重试..."
                sleep $((delay / 1000))
                attempt=$((attempt + 1))
            else
                log "error" "达到最大重试次数 ($max_attempts)，命令执行失败"
                return $exit_code
            fi
        fi
    done
}

# 发送 HTTP 请求函数
send_http_request() {
    local method=$1
    local url=$2
    local data=$3
    local headers=$4
    local timeout=${5:-$TIMEOUT}
    
    local curl_command="curl -X $method -s -w '\n%{http_code}'"
    
    # 添加超时
    curl_command+=" --max-time $timeout"
    
    # 添加头部
    if [[ -n "$headers" ]]; then
        for header in $headers; do
            curl_command+=" -H '$header'"
        done
    else
        # 默认头部
        curl_command+=" -H 'Content-Type: application/json'"
    fi
    
    # 添加数据
    if [[ -n "$data" ]]; then
        curl_command+=" -d '$data'"
    fi
    
    # 添加 URL
    curl_command+=" '$url'"
    
    log "debug" "发送 HTTP 请求: $curl_command"
    
    # 执行请求并捕获响应
    local response=$(eval "$curl_command")
    local exit_code=$?
    
    if [[ $exit_code -ne 0 ]]; then
        log "error" "HTTP 请求失败: $exit_code"
        return $exit_code
    fi
    
    # 分离响应体和状态码
    local body=$(echo "$response" | head -n -1)
    local status_code=$(echo "$response" | tail -n 1)
    
    log "debug" "HTTP 状态码: $status_code"
    log "debug" "HTTP 响应体: $body"
    
    # 检查状态码
    if [[ $status_code -lt 200 || $status_code -ge 300 ]]; then
        log "error" "HTTP 请求失败，状态码: $status_code"
        log "error" "响应体: $body"
        return 1
    fi
    
    # 返回响应体
    echo "$body"
    return 0
}

# 调用 ooder-agent 服务
call_ooder_agent() {
    local endpoint=$1
    local data=$2
    local method=${3:-"POST"}
    
    local url="$OODER_AGENT_URL$endpoint"
    
    log "info" "调用 ooder-agent 服务: $url"
    
    local response=$(retry "send_http_request '$method' '$url' '$data'" 3 2000)
    local exit_code=$?
    
    if [[ $exit_code -ne 0 ]]; then
        log "error" "调用 ooder-agent 服务失败"
        return $exit_code
    fi
    
    echo "$response"
    return 0
}

# 验证 JSON 格式
validate_json() {
    local json=$1
    
    # 使用 jq 验证 JSON 格式
    if command -v jq &> /dev/null; then
        echo "$json" | jq . > /dev/null 2>&1
        return $?
    else
        log "warn" "jq 命令不可用，跳过 JSON 验证"
        # 简单检查 JSON 格式
        if [[ "$json" =~ ^\{.*\}$ || "$json" =~ ^\[.*\]$ ]]; then
            return 0
        else
            return 1
        fi
    fi
}

# 解析命令行参数
parse_args() {
    local args=($@)
    local parsed_args=()
    local i=0
    
    while [[ $i -lt ${#args[@]} ]]; do
        local arg=${args[$i]}
        
        if [[ $arg == --* ]]; then
            # 长参数
            local key=${arg:2}
            local value=""
            
            if [[ $((i + 1)) -lt ${#args[@]} && ! ${args[$((i + 1))]} == --* ]]; then
                value=${args[$((i + 1))]}
                i=$((i + 1))
            fi
            
            parsed_args["$key"]="$value"
        elif [[ $arg == -* ]]; then
            # 短参数
            local key=${arg:1}
            local value=""
            
            if [[ $((i + 1)) -lt ${#args[@]} && ! ${args[$((i + 1))]} == -* ]]; then
                value=${args[$((i + 1))]}
                i=$((i + 1))
            fi
            
            parsed_args["$key"]="$value"
        else
            # 位置参数
            parsed_args+=($arg)
        fi
        
        i=$((i + 1))
    done
    
    echo "${parsed_args[@]}"
}

# 检查依赖
check_dependencies() {
    local dependencies=($@)
    local missing_deps=()
    
    for dep in "${dependencies[@]}"; do
        if ! command -v $dep &> /dev/null; then
            missing_deps+=($dep)
        fi
    done
    
    if [[ ${#missing_deps[@]} -gt 0 ]]; then
        log "error" "缺少依赖: ${missing_deps[*]}"
        return 1
    else
        log "info" "所有依赖都已满足"
        return 0
    fi
}

# 生成唯一 ID
generate_uuid() {
    if command -v uuidgen &> /dev/null; then
        uuidgen
    else
        # 简单的 UUID 生成
        echo "$(date +%s)-$(cat /dev/urandom | tr -dc 'a-f0-9' | fold -w 8 | head -n 1)"
    fi
}

# 显示帮助信息
display_help() {
    local script_name=$(basename "$0")
    
    echo "Usage: $script_name [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --help, -h     Display this help message"
    echo "  --debug        Enable debug mode"
    echo "  --timeout      Set timeout (seconds)"
    echo "  --retry-count  Set retry count"
    echo "  --retry-delay  Set retry delay (milliseconds)"
    echo ""
}

# 主函数，用于测试通用函数
main() {
    log "info" "测试通用工具函数"
    
    # 测试日志函数
    log "debug" "这是一条调试日志"
    log "info" "这是一条信息日志"
    log "warn" "这是一条警告日志"
    log "error" "这是一条错误日志"
    
    # 测试错误处理函数
    handle_error "测试错误" 1
    
    # 测试依赖检查
    check_dependencies "curl" "bash"
    
    # 测试 UUID 生成
    log "info" "生成的 UUID: $(generate_uuid)"
    
    log "info" "通用工具函数测试完成"
}

# 如果直接运行此脚本，则执行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main "$@"
fi

# 导出函数
export -f log
export -f handle_error
export -f retry
export -f send_http_request
export -f call_ooder_agent
export -f validate_json
export -f parse_args
export -f check_dependencies
export -f generate_uuid
export -f display_help
export -f main