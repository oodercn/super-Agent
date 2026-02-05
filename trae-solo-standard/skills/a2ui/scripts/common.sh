#!/bin/bash

# 通用工具函数

# 服务端点配置
A2UI_ENDPOINT="http://localhost:8081/api/a2ui"
OODER_AGENT_ENDPOINT="http://localhost:8080/api/ooder-agent"
MCP_AGENT_ENDPOINT="http://localhost:8082/api/mcp-agent"
ROUTE_AGENT_ENDPOINT="http://localhost:8083/api/route-agent"

# 南下协议配置
SOUTHBOUND_PROTOCOL_VERSION="0.6.3"

# 日志级别
LOG_LEVEL="info"

# 检测操作系统类型
OS_TYPE=$(uname -s 2>/dev/null || echo "Windows")
IS_WINDOWS=false
if [[ "$OS_TYPE" == *"Windows"* || "$OS_TYPE" == "MINGW"* || "$OS_TYPE" == "CYGWIN"* ]]; then
    IS_WINDOWS=true
fi

# 打印日志
log() {
    local level="$1"
    local message="$2"
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S" 2>/dev/null || echo "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" 2>/dev/null || echo "$(date)")
    
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
        echo '{"success": false, "error": "缺少 curl 命令，请安装"}'
        exit 1
    fi
    
    # 检查 jq
    if ! check_command "jq"; then
        log "warning" "缺少 jq 命令，某些功能可能无法正常工作"
    fi
    
    log "info" "依赖项检查完成"
}

# 发送 HTTP 请求
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
    if [[ "$method" == "POST" ]]; then
        if [[ -n "$headers" ]]; then
            response=$(curl -s -X POST -H "Content-Type: application/json" -H "$headers" -d "$data" "$url")
        else
            response=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url")
        fi
    else
        response=$(curl -s -X GET "$url")
    fi
    
    local status_code=$(curl -s -o /dev/null -w "%{http_code}" -X "$method" -H "Content-Type: application/json" ${data:+-d "$data"} "$url")
    
    log "debug" "响应状态码: $status_code"
    log "debug" "响应数据: $response"
    
    if [[ "$status_code" -lt 200 || "$status_code" -ge 300 ]]; then
        log "error" "HTTP 请求失败，状态码: $status_code"
        echo '{"success": false, "error": "HTTP 请求失败，状态码: $status_code", "response": "'"$response"'"}'
        return 1
    fi
    
    echo "$response"
    return 0
}

# 调用 ooder-agent 服务
call_ooder_agent() {
    local function_name="$1"
    local parameters="$2"
    
    log "info" "调用 ooder-agent 服务: $function_name"
    
    local payload=$(build_json \
        "function" "$function_name" \
        "parameters" "$parameters"
    )
    
    local request_data=$(build_southbound_request \
        "$function_name" \
        "$payload" \
        "mcpAgent" \
        "mcp-agent-1" \
        "ooderAgent" \
        "ooder-agent-1"
    )
    
    local response=$(send_http_request "POST" "$OODER_AGENT_ENDPOINT/execute" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "warning" "ooder-agent 服务调用失败，将使用模拟模式"
        return 1
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

# 构建南下协议格式请求
build_southbound_request() {
    local operation="$1"
    local payload="$2"
    local source_component="$3"
    local source_id="$4"
    local destination_component="$5"
    local destination_id="$6"
    local priority="${7:-medium}"
    local timeout="${8:-30}"
    local retry_count="${9:-3}"
    
    local command_id=$(generate_uuid)
    local timestamp=$(date +"%Y-%m-%dT%H:%M:%SZ" 2>/dev/null || echo "$(Get-Date -Format 'yyyy-MM-ddTHH:mm:ssZ')" 2>/dev/null || echo "$(date)")
    
    local request_data=$(build_json \
        "protocol_version" "$SOUTHBOUND_PROTOCOL_VERSION" \
        "command_id" "$command_id" \
        "timestamp" "$timestamp" \
        "source" "{\"component\":\"$source_component\",\"id\":\"$source_id\"}" \
        "destination" "{\"component\":\"$destination_component\",\"id\":\"$destination_id\"}" \
        "operation" "$operation" \
        "payload" "$payload" \
        "metadata" "{\"priority\":\"$priority\",\"timeout\":$timeout,\"retry_count\":$retry_count}"
    )
    
    echo "$request_data"
}

# 生成 UUID
generate_uuid() {
    if command -v uuidgen &> /dev/null; then
        uuidgen
    elif command -v powershell &> /dev/null; then
        powershell -Command "[guid]::NewGuid().ToString()"
    else
        # 简单的 UUID 生成
        echo "$(date +%s)-$(cat /dev/urandom | tr -dc 'a-f0-9' | fold -w 12 | head -n 1)"
    fi
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
        status_code=$(curl -s -o /dev/null -w "%{http_code}" "$endpoint/components" 2>/dev/null || curl -s -o /dev/null -w "%{http_code}" "$endpoint/status" 2>/dev/null)
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

# 获取模拟数据
get_mock_data() {
    local data_type="$1"
    
    case "$data_type" in
        "components")
            echo '[{"id": "button", "name": "Button", "category": "基础组件", "description": "按钮组件"}, {"id": "input", "name": "Input", "category": "基础组件", "description": "输入框组件"}, {"id": "panel", "name": "Panel", "category": "容器组件", "description": "面板组件"}, {"id": "tabs", "name": "Tabs", "category": "容器组件", "description": "标签页组件"}, {"id": "form", "name": "Form", "category": "容器组件", "description": "表单组件"}]'
            ;;
        "component-details")
            echo '{"id": "button", "name": "Button", "category": "基础组件", "description": "按钮组件", "properties": {"text": "按钮文本", "type": "按钮类型", "disabled": "是否禁用"}, "styles": {"color": "按钮颜色", "fontSize": "字体大小", "padding": "内边距"}, "events": {"click": "点击事件"}, "behaviors": {"hover": "悬停效果", "active": "激活效果"}}'
            ;;
        "generate-ui")
            echo '{"uiCode": "<div class=\"login-form\"><input type=\"text\" placeholder=\"用户名\" /><input type=\"password\" placeholder=\"密码\" /><button>登录</button></div>", "components": ["Input", "Button", "Panel"], "previewUrl": "http://localhost:8081/api/a2ui/preview-ui?code=..."}'
            ;;
        "create-view")
            echo '{"viewId": "1", "viewName": "LoginForm", "components": ["Input", "Button"], "layout": {"type": "form", "columns": 2}, "status": "created"}'
            ;;
        "submit-data")
            echo '{"success": true, "message": "数据提交成功", "data": {"id": 1, "status": "processed"}}'
            ;;
        *)
            echo '{}'
            ;;
    esac
}

# 主函数入口
main() {
    # 检查依赖项
    check_dependencies
    
    # 检查服务可用性
    check_service_availability "$A2UI_ENDPOINT" "A2UI"
    check_service_availability "$OODER_AGENT_ENDPOINT" "ooder-agent"
}

# 调用 mcpAgent 服务
call_mcp_agent() {
    local function_name="$1"
    local parameters="$2"
    
    log "info" "调用 mcpAgent 服务: $function_name"
    
    local request_data=$(build_json \
        "function" "$function_name" \
        "parameters" "$parameters"
    )
    
    local response=$(send_http_request "POST" "$MCP_AGENT_ENDPOINT/execute" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "warning" "mcpAgent 服务调用失败，将使用模拟模式"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 执行 LLM 查询
execute_llm_query() {
    local skill_id="$1"
    local query="$2"
    local parameters="$3"
    
    log "info" "执行 LLM 查询 - 技能: $skill_id"
    
    # 1. 查询 mcpAgent 状态
    local status=$(get_mcp_agent_status)
    if [[ $? -ne 0 ]]; then
        log "warning" "查询 mcpAgent 状态失败，将使用模拟模式"
    fi
    
    # 2. 构建 LLM 查询请求
    local request_data=$(build_json \
        "skill_id" "$skill_id" \
        "query" "$query" \
        "parameters" "$parameters"
    )
    
    local response=$(call_mcp_agent "execute_llm_query" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "warning" "LLM 查询执行失败，将使用模拟数据"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 获取 mcpAgent 状态
get_mcp_agent_status() {
    log "info" "获取 mcpAgent 状态"
    
    local response=$(send_http_request "GET" "$MCP_AGENT_ENDPOINT/status")
    
    if [[ $? -ne 0 ]]; then
        log "warning" "获取 mcpAgent 状态失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 向指定 endAgent 发送执行命令
send_command_to_end_agent() {
    local agent_id="$1"
    local command="$2"
    local parameters="$3"
    
    log "info" "向 endAgent $agent_id 发送命令: $command"
    
    local request_data=$(build_json \
        "agent_id" "$agent_id" \
        "command" "$command" \
        "parameters" "$parameters"
    )
    
    local response=$(send_http_request "POST" "$ROUTE_AGENT_ENDPOINT/execute" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "warning" "向 endAgent 发送命令失败，将使用模拟模式"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 主函数入口
main() {
    # 检查依赖项
    check_dependencies
    
    # 检查服务可用性
    check_service_availability "$A2UI_ENDPOINT" "A2UI"
    check_service_availability "$OODER_AGENT_ENDPOINT" "ooder-agent"
    check_service_availability "$MCP_AGENT_ENDPOINT" "mcpAgent"
    check_service_availability "$ROUTE_AGENT_ENDPOINT" "routeAgent"
}

# 如果直接执行此脚本，则运行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main
fi
