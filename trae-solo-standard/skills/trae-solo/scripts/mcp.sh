#!/bin/bash

# MCP (Model Control Plane) 脚本
# 用于处理大型模型到 ooder-agent 的交互

# 加载通用工具函数
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [[ -f "$SCRIPT_DIR/common.sh" ]]; then
    source "$SCRIPT_DIR/common.sh"
else
    echo "{\"success\": false, \"error\": \"找不到 common.sh 脚本\"}"
    exit 1
fi

# 主函数
main() {
    # 检查依赖项
    check_dependencies
    
    # 解析命令行参数
    local action=""
    local function_name=""
    local params="{}"
    local display_mode="a2ui"
    local priority="normal"
    local timeout="30"
    
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --action)
                action="$2"
                shift 2
                ;;
            --function)
                function_name="$2"
                shift 2
                ;;
            --params)
                params="$2"
                shift 2
                ;;
            --displayMode)
                display_mode="$2"
                shift 2
                ;;
            --priority)
                priority="$2"
                shift 2
                ;;
            --timeout)
                timeout="$2"
                shift 2
                ;;
            *)
                log "warning" "未知参数: $1"
                shift
                ;;
        esac
    done
    
    # 验证必要参数
    if [[ -z "$action" ]]; then
        echo "{\"success\": false, \"error\": \"缺少 action 参数\"}"
        exit 1
    fi
    
    # 根据 action 执行不同操作
    case "$action" in
        "call")
            if [[ -z "$function_name" ]]; then
                echo "{\"success\": false, \"error\": \"call 操作需要 function 参数\"}"
                exit 1
            fi
            execute_call "$function_name" "$params" "$display_mode" "$priority" "$timeout"
            ;;
        "status")
            check_status
            ;;
        "health")
            check_health
            ;;
        "clear-cache")
            clear_cache
            ;;
        "list-functions")
            list_functions
            ;;
        *)
            echo "{\"success\": false, \"error\": \"未知的 action: $action\"}"
            exit 1
            ;;
    esac
}

# 执行 MCP 调用
execute_call() {
    local function_name="$1"
    local params="$2"
    local display_mode="$3"
    local priority="$4"
    local timeout="$5"
    
    log "info" "执行 MCP 调用: $function_name"
    log "info" "优先级: $priority"
    log "info" "超时时间: $timeout 秒"
    
    # 验证 params JSON 格式
    if ! validate_json "$params"; then
        echo "{\"success\": false, \"error\": \"params 参数不是有效的 JSON 格式\"}"
        return 1
    fi
    
    # 使用 MCP 功能调用 ooder-agent
    local response=$(mcp_call "$function_name" "$params" "$display_mode")
    
    # 检查响应
    if [[ "$response" == *"success"* ]]; then
        log "info" "MCP 调用成功"
    else
        log "error" "MCP 调用失败"
    fi
    
    echo "$response"
}

# 检查 MCP 状态
check_status() {
    log "info" "检查 MCP 状态"
    
    # 构建请求数据
    local request_data=$(build_json 
        "action" "status"
        "timestamp" "$(date +%s)"
    )
    
    # 发送请求
    local response=$(send_http_request "GET" "$OODER_AGENT_ENDPOINT/mcp/status" "$request_data")
    
    # 处理响应
    if [[ "$response" == *"success"* ]]; then
        # 添加 MCP 状态信息
        local status_info=$(build_json 
            "mcp_status" "active"
            "cache_dir" "$CACHE_DIR"
            "max_retries" "$MAX_RETRIES"
        )
        
        if [[ -n "$(command -v jq)" ]]; then
            local success=$(echo "$response" | jq -r '.success')
            local data=$(echo "$response" | jq -c '.data')
            local error=$(echo "$response" | jq -r '.error // empty')
            
            # 合并数据
            if [[ -n "$data" && "$data" != "null" ]]; then
                local merged_data=$(echo "$data" | jq -c ". + $status_info")
            else
                local merged_data="$status_info"
            fi
            
            echo "$(build_response "$success" "$merged_data" "$error" '')"
        else
            echo "$response"
        fi
    else
        echo "$response"
    fi
}

# 检查健康状态
check_health() {
    log "info" "检查 MCP 健康状态"
    
    # 检查服务可用性
    local ooder_agent_status=1
    local a2ui_status=1
    
    check_service_availability "$OODER_AGENT_ENDPOINT" "ooder-agent"
    ooder_agent_status=$?
    
    check_service_availability "$A2UI_ENDPOINT" "A2UI"
    a2ui_status=$?
    
    # 检查缓存目录
    local cache_status=1
    if [[ -d "$CACHE_DIR" ]]; then
        cache_status=0
    fi
    
    # 构建健康状态响应
    local health_data=$(build_json 
        "ooder_agent" "$((ooder_agent_status == 0 ? 1 : 0))"
        "a2ui" "$((a2ui_status == 0 ? 1 : 0))"
        "cache" "$((cache_status == 0 ? 1 : 0))"
        "timestamp" "$(date +%s)"
        "version" "1.0.0"
    )
    
    echo "$(build_response "true" "$health_data" "" '')"
}

# 清理缓存
clear_cache() {
    log "info" "清理 MCP 缓存"
    
    if [[ -d "$CACHE_DIR" ]]; then
        rm -rf "$CACHE_DIR"/*
        log "info" "缓存已清理"
        echo "{\"success\": true, \"message\": \"缓存已清理\"}"
    else
        log "warning" "缓存目录不存在"
        echo "{\"success\": true, \"message\": \"缓存目录不存在\"}"
    fi
}

# 列出可用函数
list_functions() {
    log "info" "列出可用函数"
    
    # 构建请求数据
    local request_data=$(build_json 
        "action" "list"
    )
    
    # 发送请求
    local response=$(send_http_request "GET" "$OODER_AGENT_ENDPOINT/mcp/functions" "$request_data")
    
    # 处理响应
    if [[ "$response" == *"success"* ]]; then
        echo "$response"
    else
        # 如果服务不可用，返回默认函数列表
        local default_functions=$(build_json 
            "functions" "[\"hello\", \"calculate\", \"generate\", \"transform\", \"analyze\"]"
            "source" "default"
        )
        echo "$(build_response "true" "$default_functions" "" '')"
    fi
}

# 执行主函数
main "$@"