#!/bin/bash

# 获取组件列表脚本

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
    
    log "info" "获取组件列表"
    
    # 1. 通过 mcpAgent 执行 LLM 查询，获取组件列表
    local llm_query="获取 A2UI 组件列表"
    local llm_parameters="{}"
    
    local llm_response=$(execute_llm_query "a2ui" "$llm_query" "$llm_parameters")
    
    # 2. 检测 Windows 环境，使用模拟模式
    if [[ "$IS_WINDOWS" == "true" ]]; then
        log "info" "检测到 Windows 环境，使用模拟模式执行"
        
        # 获取模拟数据
        local components=$(get_mock_data "components")
        
        # 构建响应
        local start_time=$(date +%s%3N)
        local end_time=$((start_time + 100))
        local execution_time=$((end_time - start_time))
        
        local metadata=$(build_json \
            "function" "get-components" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$components" "" "$metadata")
        echo "$final_response"
        return 0
    fi
    
    # 3. 发送请求到 A2UI 服务
    local start_time=$(date +%s%3N)
    local response=$(send_http_request "GET" "$A2UI_ENDPOINT/components" "")
    local end_time=$(date +%s%3N)
    local execution_time=$((end_time - start_time))
    
    # 4. 检查响应
    if [[ $? -eq 0 && -n "$response" ]]; then
        # 构建带 metadata 的响应
        local metadata=$(build_json \
            "function" "get-components" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$response" "" "$metadata")
        echo "$final_response"
    else
        # 使用模拟数据
        log "info" "服务调用失败，使用模拟数据"
        local mock_components=$(get_mock_data "components")
        
        local metadata=$(build_json \
            "function" "get-components" \
            "executionTime" "$execution_time" \
            "skillId" "a2ui"
        )
        
        local final_response=$(build_response "true" "$mock_components" "" "$metadata")
        echo "$final_response"
    fi
}

# 执行主函数
main "$@"
