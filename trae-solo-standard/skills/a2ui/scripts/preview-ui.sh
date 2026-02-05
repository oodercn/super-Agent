#!/bin/bash

# 预览 UI 界面脚本

# 加载通用工具函数
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [[ -f "$SCRIPT_DIR/common.sh" ]]; then
    source "$SCRIPT_DIR/common.sh"
else
    echo '{"success": false, "error": "找不到 common.sh 脚本"}'
    exit 1
fi

# 主函数
main() {
    # 检查依赖项
    check_dependencies
    
    # 解析命令行参数
    local uiCode=""
    local format="html"
    
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --uiCode)
                uiCode="$2"
                shift 2
                ;;
            --format)
                format="$2"
                shift 2
                ;;
            *)
                log "warning" "未知参数: $1"
                shift
                ;;
        esac
    done
    
    # 验证参数
    if [[ -z "$uiCode" ]]; then
        echo '{"success": false, "error": "缺少 uiCode 参数"}'
        exit 1
    fi
    
    log "info" "预览 UI 界面"
    log "info" "格式: $format"
    
    # 构建请求数据
    local request_data=$(build_json \
        "uiCode" "$uiCode" \
        "format" "$format"
    )
    
    # 发送请求
    local start_time=$(date +%s%3N)
    local response=$(send_http_request "POST" "$A2UI_ENDPOINT/preview-ui" "$request_data")
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
                "function" "preview-ui" \
                "format" "$format" \
                "executionTime" "$execution_time" \
                "skillId" "a2ui"
            )
            
            local final_response=$(build_response "$success" "$data" "$error" "$metadata")
            echo "$final_response"
        else
            # 直接返回原始响应
            echo "$response"
        fi
    else
        # 构建错误响应
        local error_message="预览 UI 界面失败"
        if [[ -n "$response" ]]; then
            error_message="$response"
        fi
        
        local metadata=$(build_json \
            "function" "preview-ui" \
            "format" "$format" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
