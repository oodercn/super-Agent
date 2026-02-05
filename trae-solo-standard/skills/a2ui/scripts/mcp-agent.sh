#!/bin/bash

# mcpAgent 脚本 - 作为 aibridge 统一处理 LLM 需求

# 加载通用工具函数
if [[ -f "common.sh" ]]; then
    source "common.sh"
else
    echo "错误: 找不到 common.sh 文件"
    exit 1
fi

# mcpAgent 配置
MCP_AGENT_ENDPOINT="http://localhost:8082/api/mcp-agent"
ROUTE_AGENT_ENDPOINT="http://localhost:8083/api/route-agent"

# 组管理
GROUPS=()
ONLINE_MEMBERS=()

# 初始化 mcpAgent
init_mcp_agent() {
    log "info" "初始化 mcpAgent..."
    
    # 检查 mcpAgent 服务可用性
    check_service_availability "$MCP_AGENT_ENDPOINT" "mcpAgent"
    
    # 检查 routeAgent 服务可用性
    check_service_availability "$ROUTE_AGENT_ENDPOINT" "routeAgent"
    
    log "info" "mcpAgent 初始化完成"
}

# 注册 endAgent
register_end_agent() {
    local agent_id="$1"
    local commands="$2"
    local endpoint="$3"
    
    log "info" "注册 endAgent: $agent_id"
    
    local request_data=$(build_json \
        "agent_id" "$agent_id" \
        "commands" "$commands" \
        "endpoint" "$endpoint"
    )
    
    local response=$(send_http_request "POST" "$ROUTE_AGENT_ENDPOINT/register" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "error" "endAgent 注册失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 广播命令到组内成员
broadcast_command() {
    local group_id="$1"
    local command="$2"
    local parameters="$3"
    
    log "info" "广播命令到组 $group_id: $command"
    
    # 查询在线成员
    local online_members=$(get_online_members "$group_id")
    if [[ $? -ne 0 ]]; then
        log "error" "获取在线成员失败"
        return 1
    fi
    
    log "info" "在线成员: $online_members"
    
    # 构建广播请求
    local request_data=$(build_json \
        "group_id" "$group_id" \
        "command" "$command" \
        "parameters" "$parameters"
    )
    
    local response=$(send_http_request "POST" "$ROUTE_AGENT_ENDPOINT/broadcast" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "error" "命令广播失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 获取组内在线成员
get_online_members() {
    local group_id="$1"
    
    log "info" "获取组 $group_id 的在线成员"
    
    local response=$(send_http_request "GET" "$ROUTE_AGENT_ENDPOINT/groups/$group_id/members")
    
    if [[ $? -ne 0 ]]; then
        log "error" "获取在线成员失败"
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
        log "error" "查询 mcpAgent 状态失败"
        return 1
    fi
    
    # 2. 根据技能查找 mcpAgent id
    local agent_id=$(get_agent_id_by_skill "$skill_id")
    if [[ $? -ne 0 ]]; then
        log "error" "根据技能查找 mcpAgent id 失败"
        return 1
    fi
    
    # 3. 构建 LLM 查询请求
    local request_data=$(build_json \
        "agent_id" "$agent_id" \
        "query" "$query" \
        "parameters" "$parameters"
    )
    
    local response=$(send_http_request "POST" "$MCP_AGENT_ENDPOINT/llm/query" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "error" "LLM 查询执行失败"
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
        log "error" "获取 mcpAgent 状态失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 根据技能查找 mcpAgent id
get_agent_id_by_skill() {
    local skill_id="$1"
    
    log "info" "根据技能查找 mcpAgent id: $skill_id"
    
    local request_data=$(build_json \
        "skill_id" "$skill_id"
    )
    
    local response=$(send_http_request "POST" "$MCP_AGENT_ENDPOINT/agent/find" "$request_data")
    
    if [[ $? -ne 0 ]]; then
        log "error" "根据技能查找 mcpAgent id 失败"
        return 1
    fi
    
    # 提取 agent_id
    if command -v jq &> /dev/null; then
        local agent_id=$(echo "$response" | jq -r '.agent_id')
        if [[ "$agent_id" != "null" ]]; then
            echo "$agent_id"
            return 0
        fi
    fi
    
    log "error" "无法从响应中提取 agent_id"
    return 1
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
        log "error" "向 endAgent 发送命令失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 异步查询命令执行结果
query_command_result() {
    local command_id="$1"
    
    log "info" "查询命令执行结果: $command_id"
    
    local response=$(send_http_request "GET" "$ROUTE_AGENT_ENDPOINT/commands/$command_id/result")
    
    if [[ $? -ne 0 ]]; then
        log "error" "查询命令执行结果失败"
        return 1
    fi
    
    echo "$response"
    return 0
}

# 主函数入口
main() {
    # 初始化 mcpAgent
    init_mcp_agent
    
    # 处理命令行参数
    local action="$1"
    shift
    
    case "$action" in
        "register-end-agent")
            register_end_agent "$@"
            ;;
        "broadcast-command")
            broadcast_command "$@"
            ;;
        "get-online-members")
            get_online_members "$@"
            ;;
        "execute-llm-query")
            execute_llm_query "$@"
            ;;
        "get-status")
            get_mcp_agent_status
            ;;
        "send-command")
            send_command_to_end_agent "$@"
            ;;
        "query-result")
            query_command_result "$@"
            ;;
        *)
            echo "用法: $0 <action> [参数]"
            echo "可用 action:"
            echo "  register-end-agent <agent_id> <commands> <endpoint>"
            echo "  broadcast-command <group_id> <command> <parameters>"
            echo "  get-online-members <group_id>"
            echo "  execute-llm-query <skill_id> <query> <parameters>"
            echo "  get-status"
            echo "  send-command <agent_id> <command> <parameters>"
            echo "  query-result <command_id>"
            exit 1
            ;;
    esac
}

# 如果直接执行此脚本，则运行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main "$@"
fi