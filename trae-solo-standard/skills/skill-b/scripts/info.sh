#!/bin/bash

# 获取技能信息脚本

# 加载通用工具函数
source "$(dirname "$0")/common.sh"

# 显示帮助信息
show_help() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --debug, -v       启用调试模式"
    echo "  --help, -h        显示此帮助信息"
    echo ""
}

# 解析命令行参数
parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            --debug|-v)
                DEBUG=true
                LOG_LEVEL="debug"
                shift
                ;;
            --help|-h)
                show_help
                exit 0
                ;;
            *)
                echo "未知参数: $1"
                show_help
                exit 1
                ;;
        esac
    done
}

# 获取技能信息
get_skill_info() {
    local skill_info="{"
    skill_info+="\"name\": \"SKILL B\","
    skill_info+="\"description\": \"数据提交服务，负责将处理后的数据提交到目标系统\","
    skill_info+="\"version\": \"0.1.0\","
    skill_info+="\"author\": \"OODER Team\","
    skill_info+="\"created\": \"2026-01-22\","
    skill_info+="\"updated\": \"2026-01-22\","
    skill_info+="\"type\": \"service\","
    skill_info+="\"categories\": [\"data\", \"service\", \"core\"],"
    skill_info+="\"tags\": [\"data-submission\", \"database\", \"api\", \"file\"],"
    skill_info+="\"functions\": ["
    skill_info+="  {"
    skill_info+="    \"name\": \"submit-data\","
    skill_info+="    \"description\": \"提交数据到目标系统\","
    skill_info+="    \"parameters\": ["
    skill_info+="      {"
    skill_info+="        \"name\": \"target\","
    skill_info+="        \"type\": \"string\","
    skill_info+="        \"required\": true,"
    skill_info+="        \"description\": \"目标系统类型 (api/file/database)\""
    skill_info+="      },"
    skill_info+="      {"
    skill_info+="        \"name\": \"data\","
    skill_info+="        \"type\": \"object\","
    skill_info+="        \"required\": true,"
    skill_info+="        \"description\": \"要提交的数据\""
    skill_info+="      },"
    skill_info+="      {"
    skill_info+="        \"name\": \"options\","
    skill_info+="        \"type\": \"object\","
    skill_info+="        \"required\": false,"
    skill_info+="        \"description\": \"提交选项\""
    skill_info+="      }"
    skill_info+="    ],"
    skill_info+="    \"returnType\": \"object\","
    skill_info+="    \"returnDescription\": \"提交结果\""
    skill_info+="  },"
    skill_info+="  {"
    skill_info+="    \"name\": \"info\","
    skill_info+="    \"description\": \"获取技能信息\","
    skill_info+="    \"parameters\": [],"
    skill_info+="    \"returnType\": \"object\","
    skill_info+="    \"returnDescription\": \"技能信息\""
    skill_info+="  }"
    skill_info+="]"
    skill_info+="}"
    
    echo "$skill_info"
}

# 主函数
main() {
    # 解析命令行参数
    parse_args "$@"
    
    log "info" "获取 SKILL B 技能信息"
    
    # 获取技能信息
    local skill_info=$(get_skill_info)
    
    log "info" "技能信息获取成功"
    
    # 输出生成的技能信息
    echo "$skill_info"
}

# 执行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main "$@"
fi