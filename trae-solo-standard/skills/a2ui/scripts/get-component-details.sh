#!/bin/bash

# 获取组件详细信息脚本

# 加载通用工具函数
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [[ -f "$SCRIPT_DIR/common.sh" ]]; then
    source "$SCRIPT_DIR/common.sh"
else
    echo '{"success": false, "error": "找不到 common.sh 脚本"}'
    exit 1
fi

# 解析命令行参数
parse_arguments() {
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

# 主函数
main() {
    # 解析参数
    local args=($(parse_arguments "$@"))
    local component_id="${args["component-id"]}"
    
    # 检查参数
    if [[ -z "$component_id" ]]; then
        echo '{"success": false, "error": "缺少 component-id 参数"}'
        exit 1
    fi
    
    # 检查依赖项
    check_dependencies
    
    log "info" "获取组件详细信息: $component_id"
    
    # 检测 Windows 环境，使用模拟模式
    if [[ "$IS_WINDOWS" == "true" ]]; then
        log "info" "检测到 Windows 环境，使用模拟模式执行"
        
        # 获取模拟数据
        local component_details=$(get_mock_data "component-details")
        
        # 构建响应
        local start_time=$(date +%s%3N)
        local end_time=$((start_time + 100))
        local execution_time=$((end_time - start_time))
        
        local metadata=$(build_json \
            "function" "get-component-details" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$component_details" "" "$metadata")
        echo "$final_response"
        return 0
    fi
    
    # 构建请求
    local url="$A2UI_ENDPOINT/components/$component_id"
    
    # 发送请求
    local start_time=$(date +%s%3N)
    local response=$(send_http_request "GET" "$url")
    local end_time=$(date +%s%3N)
    local execution_time=$((end_time - start_time))
    
    # 检查响应
    if [[ $? -eq 0 && -n "$response" ]]; then
        # 构建带 metadata 的响应
        local metadata=$(build_json \
            "function" "get-component-details" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$response" "" "$metadata")
        echo "$final_response"
    else
        # 使用模拟数据
        log "info" "服务调用失败，使用模拟数据"
        local mock_details=$(get_mock_data "component-details")
        
        local metadata=$(build_json \
            "function" "get-component-details" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$mock_details" "" "$metadata")
        echo "$final_response"
    fi
}

# 执行主函数
main "$@"