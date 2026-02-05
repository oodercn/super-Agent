#!/bin/bash

# 获取技能信息脚本

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
    
    log "info" "获取技能信息"
    
    # 发送请求
    local start_time=$(date +%s%3N)
    local response=$(send_http_request "GET" "$OODER_AGENT_ENDPOINT/info" "")
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
                "function" "info" \
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
        local error_message="获取技能信息失败"
        if [[ -n "$response" ]]; then
            error_message="$response"
        fi
        
        local metadata=$(build_json \
            "function" "info" \
            "executionTime" "$execution_time" \
            "skillId" "trae-solo"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
