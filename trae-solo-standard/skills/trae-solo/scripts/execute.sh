#!/bin/bash

# 执行实用功能脚本

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
    local function=""
    local params="{}"
    local displayMode="a2ui"
    local a2uiTheme="light"
    
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --function)
                function="$2"
                shift 2
                ;;
            --params)
                params="$2"
                shift 2
                ;;
            --displayMode)
                displayMode="$2"
                shift 2
                ;;
            --a2uiTheme)
                a2uiTheme="$2"
                shift 2
                ;;
            *)
                log "warning" "未知参数: $1"
                shift
                ;;
        esac
    done
    
    # 验证参数
    if [[ -z "$function" ]]; then
        echo "{\"success\": false, \"error\": \"缺少 function 参数\"}"
        exit 1
    fi
    
    # 验证 params JSON 格式
    if ! validate_json "$params"; then
        echo "{\"success\": false, \"error\": \"params 参数不是有效的 JSON 格式\"}"
        exit 1
    fi
    
    log "info" "执行功能: $function"
    log "info" "参数: $params"
    log "info" "展示模式: $displayMode"
    log "info" "A2UI 主题: $a2uiTheme"
    
    # 构建请求数据
    local request_data=$(build_json \
        "function" "$function" \
        "params" "$params" \
        "displayMode" "$displayMode" \
        "a2uiTheme" "$a2uiTheme"
    )
    
    # 发送请求（使用 MCP 功能）
    local start_time=$(date +%s%3N)
    local response=$(mcp_call "$function" "$params" "$displayMode")
    local end_time=$(date +%s%3N)
    local execution_time=$((end_time - start_time))
    
    # 检查响应
    if [[ "$response" == *"success"* ]]; then
        # 解析响应
        if [[ -n "$(command -v jq)" ]]; then
            # 使用 jq 美化响应
            local success=$(echo "$response" | jq -r '.success')
            local data=$(echo "$response" | jq -c '.data')
            local error=$(echo "$response" | jq -r '.error // empty')
            
            # 构建带 metadata 的响应
            local metadata=$(build_json \
                "function" "$function" \
                "displayMode" "$displayMode" \
                "executionTime" "$execution_time" \
                "skillId" "trae-solo"
            )
            
            local final_response=$(build_response "$success" "$data" "$error" "$metadata")
            echo "$final_response"
        else
            # 直接返回原始响应
            echo "$response"
        fi
    else
        # 构建错误响应
        local error_message="执行功能失败"
        if [[ -n "$response" ]]; then
            error_message="$response"
        fi
        
        local metadata=$(build_json \
            "function" "$function" \
            "displayMode" "$displayMode" \
            "executionTime" "$execution_time" \
            "skillId" "trae-solo"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
