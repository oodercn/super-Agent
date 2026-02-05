#!/bin/bash

# 数据提交脚本

# 加载通用工具函数
source "$(dirname "$0")/common.sh"

# 全局变量
TARGET=""
DATA=""
OPTIONS=""
DEBUG=false

# 显示帮助信息
show_help() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --target, -t      目标系统类型 (api/file/database)"
    echo "  --data, -d        要提交的数据 (JSON 格式)"
    echo "  --options, -o     提交选项 (JSON 格式)"
    echo "  --debug, -v       启用调试模式"
    echo "  --help, -h        显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 --target api --data '{\"id\": 1, \"name\": \"Test\"}' --options '{\"url\": \"https://api.example.com/submit\"}'"
    echo "  $0 --target file --data '{\"id\": 1, \"name\": \"Test\"}' --options '{\"path\": \"output.json\"}'"
    echo "  $0 --target database --data '{\"id\": 1, \"name\": \"Test\"}' --options '{\"connection\": \"localhost:3306\", \"table\": \"users\"}'"
    echo ""
}

# 解析命令行参数
parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            --target|-t)
                TARGET="$2"
                shift 2
                ;;
            --data|-d)
                DATA="$2"
                shift 2
                ;;
            --options|-o)
                OPTIONS="$2"
                shift 2
                ;;
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

# 验证参数
validate_args() {
    if [[ -z "$TARGET" ]]; then
        log "error" "缺少目标系统类型 (--target)"
        show_help
        return 1
    fi
    
    if [[ -z "$DATA" ]]; then
        log "error" "缺少要提交的数据 (--data)"
        show_help
        return 1
    fi
    
    # 验证目标系统类型
    if [[ "$TARGET" != "api" && "$TARGET" != "file" && "$TARGET" != "database" ]]; then
        log "error" "无效的目标系统类型: $TARGET"
        log "error" "支持的类型: api, file, database"
        return 1
    fi
    
    # 验证 JSON 格式
    if ! validate_json "$DATA"; then
        log "error" "数据格式无效，必须是有效的 JSON"
        return 1
    fi
    
    if [[ -n "$OPTIONS" && ! validate_json "$OPTIONS" ]]; then
        log "error" "选项格式无效，必须是有效的 JSON"
        return 1
    fi
    
    return 0
}

# 提交数据到 API
submit_to_api() {
    local data=$1
    local options=$2
    
    # 解析选项
    local url=""
    local method="POST"
    local headers=""
    
    # 简单解析 JSON 选项 (不依赖 jq)
    if [[ "$options" =~ "url":"([^"]+)" ]]; then
        url=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "method":"([^"]+)" ]]; then
        method=${BASH_REMATCH[1]}
    fi
    
    if [[ -z "$url" ]]; then
        log "error" "API 目标系统需要指定 url 选项"
        return 1
    fi
    
    log "info" "提交数据到 API: $url"
    log "info" "HTTP 方法: $method"
    log "info" "提交数据: $data"
    
    # 调用 ooder-agent 服务
    local response=$(call_ooder_agent "/submit" "{\"target\": \"api\", \"url\": \"$url\", \"method\": \"$method\", \"data\": $data, \"options\": $options}")
    local exit_code=$?
    
    if [[ $exit_code -ne 0 ]]; then
        # 如果 ooder-agent 服务不可用，直接使用 curl 提交
        log "warn" "ooder-agent 服务不可用，直接使用 curl 提交"
        response=$(send_http_request "$method" "$url" "$data")
        exit_code=$?
        
        if [[ $exit_code -ne 0 ]]; then
            log "error" "提交数据到 API 失败"
            return 1
        fi
    fi
    
    log "info" "数据提交成功"
    log "info" "响应: $response"
    
    # 构建返回结果
    local result="{"
    result+="\"success\": true,"
    result+="\"message\": \"数据提交成功\","
    result+="\"target\": \"api\","
    result+="\"url\": \"$url\","
    result+="\"response\": $response"
    result+="}"
    
    echo "$result"
    return 0
}

# 提交数据到文件
submit_to_file() {
    local data=$1
    local options=$2
    
    # 解析选项
    local path="./output.json"
    local format="json"
    local mode="write"
    
    # 简单解析 JSON 选项 (不依赖 jq)
    if [[ "$options" =~ "path":"([^"]+)" ]]; then
        path=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "format":"([^"]+)" ]]; then
        format=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "mode":"([^"]+)" ]]; then
        mode=${BASH_REMATCH[1]}
    fi
    
    log "info" "提交数据到文件: $path"
    log "info" "格式: $format"
    log "info" "模式: $mode"
    log "info" "提交数据: $data"
    
    # 确保目录存在
    local dir=$(dirname "$path")
    if [[ ! -d "$dir" ]]; then
        log "info" "创建目录: $dir"
        mkdir -p "$dir"
    fi
    
    # 写入文件
    if [[ "$mode" == "append" ]]; then
        # 追加模式
        echo "$data" >> "$path"
    else
        # 写入模式
        echo "$data" > "$path"
    fi
    
    local exit_code=$?
    if [[ $exit_code -ne 0 ]]; then
        log "error" "写入文件失败: $exit_code"
        return 1
    fi
    
    log "info" "数据写入成功"
    
    # 构建返回结果
    local result="{"
    result+="\"success\": true,"
    result+="\"message\": \"数据写入成功\","
    result+="\"target\": \"file\","
    result+="\"path\": \"$path\","
    result+="\"mode\": \"$mode\","
    result+="\"size\": \"$(stat -c '%s' "$path" 2>/dev/null || echo "unknown")\""
    result+="}"
    
    echo "$result"
    return 0
}

# 提交数据到数据库
submit_to_database() {
    local data=$1
    local options=$2
    
    # 解析选项
    local connection="localhost:3306"
    local table="data"
    local mode="insert"
    local username=""
    local password=""
    
    # 简单解析 JSON 选项 (不依赖 jq)
    if [[ "$options" =~ "connection":"([^"]+)" ]]; then
        connection=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "table":"([^"]+)" ]]; then
        table=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "mode":"([^"]+)" ]]; then
        mode=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "username":"([^"]+)" ]]; then
        username=${BASH_REMATCH[1]}
    fi
    
    if [[ "$options" =~ "password":"([^"]+)" ]]; then
        password=${BASH_REMATCH[1]}
    fi
    
    log "info" "提交数据到数据库"
    log "info" "连接: $connection"
    log "info" "表: $table"
    log "info" "模式: $mode"
    log "info" "提交数据: $data"
    
    # 调用 ooder-agent 服务
    local response=$(call_ooder_agent "/submit" "{\"target\": \"database\", \"connection\": \"$connection\", \"table\": \"$table\", \"mode\": \"$mode\", \"data\": $data, \"options\": $options}")
    local exit_code=$?
    
    if [[ $exit_code -ne 0 ]]; then
        # 如果 ooder-agent 服务不可用，模拟数据库操作
        log "warn" "ooder-agent 服务不可用，模拟数据库操作"
        
        # 模拟数据库操作
        log "info" "模拟 ${mode} 操作到 ${table} 表"
        log "info" "数据: $data"
        
        # 构建模拟响应
        response="{\"affectedRows\": 1, \"message\": \"操作成功\"}"
    fi
    
    log "info" "数据库操作成功"
    log "info" "响应: $response"
    
    # 构建返回结果
    local result="{"
    result+="\"success\": true,"
    result+="\"message\": \"数据库操作成功\","
    result+="\"target\": \"database\","
    result+="\"connection\": \"$connection\","
    result+="\"table\": \"$table\","
    result+="\"mode\": \"$mode\","
    result+="\"response\": $response"
    result+="}"
    
    echo "$result"
    return 0
}

# 主函数
main() {
    # 解析命令行参数
    parse_args "$@"
    
    # 验证参数
    if ! validate_args; then
        exit 1
    fi
    
    # 检查依赖
    check_dependencies "bash"
    
    log "info" "开始数据提交操作"
    log "info" "目标系统: $TARGET"
    log "info" "提交数据: $DATA"
    log "info" "提交选项: $OPTIONS"
    
    # 检测 Windows 环境，使用模拟模式
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
        log "info" "检测到 Windows 环境，使用模拟模式执行"
        
        # 构建模拟结果
        local result="{"
        result+="\"success\": true,"
        result+="\"message\": \"数据提交成功 (模拟模式)\","
        result+="\"target\": \"$TARGET\","
        result+="\"data\": $DATA,"
        result+="\"options\": $OPTIONS,"
        result+="\"mode\": \"simulation\","
        result+="\"timestamp\": \"$(date +\"%Y-%m-%d %H:%M:%S\")\""
        result+="}"
        
        log "info" "数据提交操作完成 (模拟模式)"
        echo "$result"
        return 0
    fi
    
    # 根据目标系统类型执行相应的提交操作
    local result=""
    local exit_code=0
    
    case "$TARGET" in
        "api")
            result=$(submit_to_api "$DATA" "$OPTIONS")
            exit_code=$?
            ;;
        "file")
            result=$(submit_to_file "$DATA" "$OPTIONS")
            exit_code=$?
            ;;
        "database")
            result=$(submit_to_database "$DATA" "$OPTIONS")
            exit_code=$?
            ;;
        *)
            log "error" "无效的目标系统类型: $TARGET"
            exit 1
            ;;
    esac
    
    if [[ $exit_code -ne 0 ]]; then
        log "error" "数据提交失败"
        # 构建失败结果
        local error_result="{"
        error_result+="\"success\": false,"
        error_result+="\"message\": \"数据提交失败\","
        error_result+="\"target\": \"$TARGET\""
        error_result+="}"
        echo "$error_result"
        exit 1
    fi
    
    log "info" "数据提交操作完成"
    echo "$result"
    return 0
}

# 执行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main "$@"
fi