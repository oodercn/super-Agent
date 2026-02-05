#!/bin/bash

# A2UI 图转代码功能脚本

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
    local image=""
    local format="html"
    local theme="light"
    local title="Architecture Diagram"
    
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --image)
                image="$2"
                shift 2
                ;;
            --format)
                format="$2"
                shift 2
                ;;
            --theme)
                theme="$2"
                shift 2
                ;;
            --title)
                title="$2"
                shift 2
                ;;
            *)
                log "warning" "未知参数: $1"
                shift
                ;;
        esac
    done
    
    # 验证参数
    if [[ -z "$image" ]]; then
        echo "{\"success\": false, \"error\": \"缺少 image 参数\"}"
        exit 1
    fi
    
    log "info" "A2UI 图转代码"
    log "info" "图片 URL: $image"
    log "info" "输出格式: $format"
    log "info" "主题: $theme"
    log "info" "标题: $title"
    
    # 构建请求数据
    local request_data=$(build_json \
        "image" "$image" \
        "format" "$format" \
        "options" "{\"theme\": \"$theme\", \"title\": \"$title\"}"
    )
    
    # 发送请求
    local start_time=$(date +%s%3N)
    local response=$(send_http_request "POST" "$A2UI_ENDPOINT/generate-ui" "$request_data")
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
                "image" "$image" \
                "format" "$format" \
                "theme" "$theme" \
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
        local error_message="A2UI 图转代码失败"
        if [[ -n "$response" ]]; then
            error_message="$response"
        fi
        
        local metadata=$(build_json \
            "image" "$image" \
            "format" "$format" \
            "theme" "$theme" \
            "executionTime" "$execution_time" \
            "skillId" "trae-solo"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
