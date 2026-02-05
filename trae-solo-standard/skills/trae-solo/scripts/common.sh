#!/bin/bash

# 通用工具函数

# 服务端点配置
OODER_AGENT_ENDPOINT="http://localhost:9010/api/v1"
A2UI_ENDPOINT="http://localhost:8081/api/a2ui"

# 日志级别
LOG_LEVEL="info"

# 缓存配置
CACHE_DIR="${HOME}/.ooder-skill-cache"
CACHE_EXPIRY=3600 # 缓存过期时间（秒）

# 重试配置
MAX_RETRIES=3
RETRY_DELAY=2 # 重试延迟（秒）

# 打印日志
log() {
    local level="$1"
    local message="$2"
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    
    if [[ "$level" == "error" || "$LOG_LEVEL" == "debug" || "$LOG_LEVEL" == "$level" ]]; then
        echo "[$timestamp] [$level] $message"
    fi
}

# 检查命令是否存在
check_command() {
    local command="$1"
    if ! command -v "$command" &> /dev/null; then
        log "error" "命令 $command 不存在，请安装"
        return 1
    fi
    return 0
}

# 检查依赖项
check_dependencies() {
    log "info" "检查依赖项..."
    
    # 检查 curl
    if ! check_command "curl"; then
        echo "{\"success\": false, \"error\": \"缺少 curl 命令，请安装\"}"
        exit 1
    fi
    
    # 检查 jq
    if ! check_command "jq"; then
        log "warning" "缺少 jq 命令，某些功能可能无法正常工作"
    fi
    
    log "info" "依赖项检查完成"
}

# 初始化缓存目录
init_cache() {
    if [[ ! -d "$CACHE_DIR" ]]; then
        mkdir -p "$CACHE_DIR"
        log "info" "创建缓存目录: $CACHE_DIR"
    fi
}

# 生成缓存键
generate_cache_key() {
    local prefix="$1"
    local params="$2"
    local key="${prefix}_$(echo "$params" | md5sum | cut -d' ' -f1)"
    echo "$key"
}

# 检查缓存是否有效
is_cache_valid() {
    local cache_file="$1"
    if [[ ! -f "$cache_file" ]]; then
        return 1
    fi
    
    local file_time=$(stat -c %Y "$cache_file" 2>/dev/null || stat -f %m "$cache_file" 2>/dev/null)
    local current_time=$(date +%s)
    
    if [[ $((current_time - file_time)) -lt $CACHE_EXPIRY ]]; then
        return 0
    else
        return 1
    fi
}

# 获取缓存
get_cache() {
    local key="$1"
    local cache_file="$CACHE_DIR/$key"
    
    if is_cache_valid "$cache_file"; then
        log "debug" "从缓存获取: $key"
        cat "$cache_file"
        return 0
    else
        return 1
    fi
}

# 设置缓存
set_cache() {
    local key="$1"
    local value="$2"
    local cache_file="$CACHE_DIR/$key"
    
    init_cache
    echo "$value" > "$cache_file"
    log "debug" "设置缓存: $key"
}

# 发送 HTTP 请求（带重试机制）
send_http_request() {
    local method="$1"
    local url="$2"
    local data="$3"
    local headers="$4"
    
    log "debug" "发送 $method 请求到 $url"
    if [[ -n "$data" ]]; then
        log "debug" "请求数据: $data"
    fi
    
    local response
    local status_code
    local retry_count=0
    
    while [[ $retry_count -lt $MAX_RETRIES ]]; do
        if [[ "$method" == "POST" ]]; then
            if [[ -n "$headers" ]]; then
                response=$(curl -s -X POST -H "Content-Type: application/json" -H "$headers" -d "$data" "$url")
            else
                response=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url")
            fi
        else
            response=$(curl -s -X GET "$url")
        fi
        
        status_code=$(curl -s -o /dev/null -w "%{http_code}" -X "$method" -H "Content-Type: application/json" ${data:+-d "$data"} "$url")
        
        log "debug" "响应状态码: $status_code"
        log "debug" "响应数据: $response"
        
        if [[ "$status_code" -ge 200 && "$status_code" -lt 300 ]]; then
            echo "$response"
            return 0
        else
            log "warning" "HTTP 请求失败，状态码: $status_code，将重试 ($retry_count/$MAX_RETRIES)"
            retry_count=$((retry_count + 1))
            if [[ $retry_count -lt $MAX_RETRIES ]]; then
                sleep $RETRY_DELAY
            fi
        fi
    done
    
    log "error" "HTTP 请求失败，已达到最大重试次数"
    echo "{\"success\": false, \"error\": \"HTTP 请求失败，已达到最大重试次数\", \"response\": \"$response\"}"
    return 1
}

# MCP (Model Control Plane) 功能：调用 ooder-agent
mcp_call() {
    local function_name="$1"
    local params="$2"
    local display_mode="$3"
    
    # 生成缓存键
    local cache_key=$(generate_cache_key "mcp_${function_name}" "$params")
    
    # 尝试从缓存获取
    local cached_response
    if cached_response=$(get_cache "$cache_key"); then
        log "info" "使用缓存响应"
        echo "$cached_response"
        return 0
    fi
    
    # 构建请求数据
    local request_data=$(build_json \
        "function" "$function_name" \
        "params" "$params" \
        "displayMode" "$display_mode"
    )
    
    # 发送请求
    local response=$(send_http_request "POST" "$OODER_AGENT_ENDPOINT/execute" "$request_data")
    
    # 检查响应
    if [[ "$response" == *"success":true* ]]; then
        # 设置缓存
        set_cache "$cache_key" "$response"
    fi
    
    echo "$response"
    return 0
}

# 解析命令行参数
parse_args() {
    local args=()
    local current_key
    
    for arg in "$@"; do
        if [[ "$arg" == --* ]]; then
            current_key=$(echo "$arg" | sed 's/^--//')
            args["$current_key"]=""
        elif [[ -n "$current_key" ]]; then
            args["$current_key"]="$arg"
            current_key=""
        fi
    done
    
    echo "${args[@]}"
}

# 构建 JSON 对象
build_json() {
    local json="{"
    local first=true
    
    while [[ $# -gt 0 ]]; do
        local key="$1"
        local value="$2"
        shift 2
        
        if [[ "$first" == true ]]; then
            first=false
        else
            json="$json,"
        fi
        
        if [[ "$value" =~ ^[0-9]+$ ]]; then
            json="$json\"$key\":$value"
        elif [[ "$value" == true || "$value" == false ]]; then
            json="$json\"$key\":$value"
        else
            # 转义 JSON 字符串
            local escaped_value=$(echo "$value" | sed 's/"/\\"/g' | sed 's/\\/\\\\/g' | sed 's/\n/\\n/g' | sed 's/\r/\\r/g' | sed 's/\t/\\t/g')
            json="$json\"$key\":\"$escaped_value\""
        fi
    done
    
    json="$json}"
    echo "$json"
}

# 验证 JSON 格式
validate_json() {
    local json="$1"
    if [[ -n "$(command -v jq)" ]]; then
        if ! echo "$json" | jq . &> /dev/null; then
            log "error" "无效的 JSON 格式: $json"
            return 1
        fi
    fi
    return 0
}

# 检查服务可用性
check_service_availability() {
    local endpoint="$1"
    local service_name="$2"
    
    log "info" "检查 $service_name 服务可用性..."
    
    local status_code=$(curl -s -o /dev/null -w "%{http_code}" "$endpoint/health" 2>/dev/null)
    
    if [[ "$status_code" -eq 200 ]]; then
        log "info" "$service_name 服务可用"
        return 0
    else
        # 尝试其他端点
        status_code=$(curl -s -o /dev/null -w "%{http_code}" "$endpoint/info" 2>/dev/null)
        if [[ "$status_code" -eq 200 ]]; then
            log "info" "$service_name 服务可用"
            return 0
        fi
        
        log "warning" "$service_name 服务可能不可用，状态码: $status_code"
        log "info" "将继续执行，可能会失败"
        return 1
    fi
}

# 构建响应结果
build_response() {
    local success="$1"
    local data="$2"
    local error="$3"
    local metadata="$4"
    
    local response="{"
    local first=true
    
    # success 字段
    if [[ "$first" == true ]]; then
        first=false
    else
        response="$response,"
    fi
    response="$response\"success\":$success"
    
    # data 字段
    if [[ -n "$data" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"data\":$data"
    fi
    
    # error 字段
    if [[ -n "$error" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"error\":\"$error\""
    fi
    
    # metadata 字段
    if [[ -n "$metadata" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"metadata\":$metadata"
    fi
    
    response="$response}"
    echo "$response"
}

# 主函数入口
main() {
    # 检查依赖项
    check_dependencies
    
    # 初始化缓存
    init_cache
    
    # 检查服务可用性
    check_service_availability "$OODER_AGENT_ENDPOINT" "ooder-agent"
    check_service_availability "$A2UI_ENDPOINT" "A2UI"
}

# 如果直接执行此脚本，则运行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main
fi