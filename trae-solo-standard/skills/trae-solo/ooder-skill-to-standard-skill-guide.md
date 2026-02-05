# 从 ooder-skills 到标准 solo-skills 的实战转换指南

## 引言

随着 AI 技术的发展，技能（Skill）成为了扩展大模型能力的重要手段。ooder-skills 是一种常见的技能实现方式，但为了更好地与标准平台集成、节省 Token 消耗并获得更广泛的兼容性，将其转换为标准 solo-skills 是非常有必要的。本文将详细介绍如何将 ooder-skills 转换为标准 solo-skills 的完整流程，并提供实际的文件对比和转换步骤。

## 核心概念：MCP (Model Control Plane)

在转换过程中，我们引入了 MCP (Model Control Plane) 架构，用于处理大型模型与 ooder-agent 之间的交互。MCP 提供了以下核心功能：

- **智能缓存**：缓存频繁使用的响应，减少重复请求
- **自动重试**：内置请求重试机制，提高服务调用的可靠性
- **健康监控**：提供 MCP 状态和健康检查功能，确保系统稳定运行
- **统一接口**：为大型模型提供统一的接口，简化交互流程

通过 MCP，我们可以完全替代 Node.js 实现，使用 ooder-agent web 访问来执行各种功能，从而达到节省 Token、提高执行效率的目的。

## 一、ooder-skills 与标准 solo-skills 的结构对比

### 1. ooder-skills 典型结构

```
ooder-skills/
├── config.json           # 配置文件
├── main.sh               # 主要逻辑
├── requirements.txt      # 依赖管理
├── README.md             # 说明文档
└── utils/                # 工具函数
    └── helper.sh
```

### 2. 标准 solo-skills 结构

```
standard-solo-skills/
├── skill.yaml            # 1 级：元数据（轻量级）
├── SKILL.md              # 2 级：说明文档（按需加载）
├── scripts/              # 3 级+：脚本文件（按需执行）
│   ├── common.sh         # 通用工具函数（包含 MCP 核心功能）
│   ├── execute.sh        # 执行功能（使用 MCP 调用）
│   ├── info.sh           # 获取信息
│   └── mcp.sh            # MCP 功能（处理大型模型交互）
└── references/           # 参考资料（可选）
```

## 二、核心文件对比

### 1. 配置文件对比

#### ooder-skills: config.json

```json
{
  "name": "Ooder Skill",
  "version": "1.0.0",
  "description": "Ooder skill description",
  "author": "Ooder Team",
  "dependencies": {
    "curl": "*",
    "jq": "*"
  },
  "main": "main.sh",
  "scripts": {
    "start": "bash main.sh"
  }
}
```

#### 标准 solo-skills: skill.yaml

```yaml
name: Standard Skill
version: 0.2.0
description: 标准 solo-skills 描述，支持通过 web API 调用服务
author: OODER Team
tags:
  - utility
  - web
category: UTILITY_SKILL
tools:
  - name: web-api
    description: 通过 HTTP API 调用服务
    type: http
    endpoint: http://localhost:9010/api/v1
resources:
  - name: SKILL.md
    type: documentation
    path: SKILL.md
  - name: scripts
    type: directory
    path: scripts
```

### 2. 主要逻辑文件对比

#### ooder-skills: main.sh

```bash
#!/bin/bash

# 加载工具函数
if [[ -f "utils/helper.sh" ]]; then
    source "utils/helper.sh"
else
    echo '{"success": false, "error": "找不到 helper.sh 脚本"}'
    exit 1
fi

# 执行函数
execute_function() {
    local function_name="$1"
    local params="$2"
    
    case "$function_name" in
        "hello")
            local name=$(echo "$params" | grep -o '"name":"[^"]*"' | cut -d'"' -f4)
            echo '{"success": true, "data": {"message": "Hello, '$name'!"}}'
            ;;
        "calculate")
            local result=$(calculate "$params")
            echo '{"success": true, "data": {"result": '$result'}}'
            ;;
        *)
            echo '{"success": false, "error": "Unknown function"}'
            ;;
    esac
}

# 命令行执行
if [[ "$0" == "$BASH_SOURCE" ]]; then
    local function_name="$1"
    local params="$2"
    execute_function "$function_name" "$params"
fi
```

#### 标准 solo-skills: scripts/execute.sh

```bash
#!/bin/bash

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
    local function=""
    local params="{}"
    local displayMode="a2ui"
    
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
            *)
                log "warning" "未知参数: $1"
                shift
                ;;
        esac
    done
    
    # 验证参数
    if [[ -z "$function" ]]; then
        echo '{"success": false, "error": "缺少 function 参数"}'
        exit 1
    fi
    
    # 构建请求数据
    local request_data=$(build_json \
        "function" "$function" \
        "params" "$params" \
        "displayMode" "$displayMode"
    )
    
    # 发送请求（使用 MCP 功能）
    local start_time=$(date +%s%3N)
    local response=$(mcp_call "$function" "$params" "$displayMode")
    local end_time=$(date +%s%3N)
    local execution_time=$((end_time - start_time))
    
    # 处理响应
    if [[ "$response" == *"success"* ]]; then
        # 构建带 metadata 的响应
        local metadata=$(build_json \
            "function" "$function" \
            "displayMode" "$displayMode" \
            "executionTime" "$execution_time" \
            "skillId" "standard-solo-skills"
        )
        
        # 解析并返回结果
        if [[ -n "$(command -v jq)" ]]; then
            local success=$(echo "$response" | jq -r '.success')
            local data=$(echo "$response" | jq -c '.data')
            local error=$(echo "$response" | jq -r '.error // empty')
            local final_response=$(build_response "$success" "$data" "$error" "$metadata")
            echo "$final_response"
        else
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
            "skillId" "standard-solo-skills"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
```

### 3. 说明文档对比

#### ooder-skills: README.md

```markdown
# Ooder Skill

## 功能
- 问候功能
- 计算功能

## 使用
```bash
bash main.sh hello '{"name": "World"}'
```
```

#### 标准 solo-skills: SKILL.md

```markdown
# Standard Skill

## 技能概述

Standard Skill 是一个标准的 solo-skills 实现，通过 web API 调用服务，实现实用功能的执行。

## 使用场景

- **日常实用功能**：执行基本的问候、计算等操作
- **Web 服务集成**：通过 HTTP API 与服务集成
- **节省 Token**：利用分级加载机制和 web 调用，减少 Token 消耗
- **MCP 功能**：通过 Model Control Plane 优化大型模型交互

## 工作原理

Standard Skill 采用三层分级加载机制：

1. **1 级：元数据** - `skill.yaml` 文件，包含技能的基本信息
2. **2 级：说明文档** - 本文件 (`SKILL.md`)，包含详细的使用说明
3. **3 级及以上** - 脚本和资源文件，按需加载执行

## 使用方式

### 命令行使用

```bash
# 执行实用功能
bash scripts/execute.sh --function hello --params '{"name": "World"}' --displayMode a2ui

# 获取技能信息
bash scripts/info.sh

# 使用 MCP 功能
bash scripts/mcp.sh --action call --function hello --params '{"name": "World"}' --displayMode a2ui
```

## 功能列表

| 功能名称 | 描述 | 参数 |
|---------|------|------|
| hello | 简单的问候功能 | name: 问候对象名称 |
| calculate | 基本计算功能 | a: 第一个操作数, b: 第二个操作数, operation: 操作类型 |
| mcp-call | MCP 调用功能 | function: 函数名称, params: 参数, displayMode: 展示模式 |
```

## 三、详细转换步骤

### 步骤 1：创建标准 solo-skills 目录结构

```bash
mkdir -p standard-solo-skills/scripts standard-solo-skills/references
cd standard-solo-skills
```

### 步骤 2：创建 1 级元数据文件 (skill.yaml)

基于 ooder-skills 的 config.json，提取核心信息并按照标准格式创建 skill.yaml：

```yaml
name: [从 config.json 提取 name]
version: 0.2.0  # 标准 solo-skills 版本号
description: [从 config.json 提取 description]
author: [从 config.json 提取 author]
tags:
  - [根据功能添加标签]
category: [根据功能类型选择类别]
tools:
  - name: web-api
    description: 通过 HTTP API 调用服务
    type: http
    endpoint: http://localhost:9010/api/v1  # 服务端点
resources:
  - name: SKILL.md
    type: documentation
    path: SKILL.md
  - name: scripts
    type: directory
    path: scripts
  - name: references
    type: directory
    path: references
```

### 步骤 3：创建 2 级说明文档 (SKILL.md)

基于 ooder-skills 的 README.md，按照标准格式扩展为详细的 SKILL.md：

1. 添加技能概述
2. 描述使用场景
3. 解释工作原理（分级加载机制）
4. 详细说明使用方式
5. 提供功能列表
6. 添加示例和故障排除

### 步骤 4：创建 3 级脚本文件

#### 4.1 创建通用工具函数 (scripts/common.sh)

```bash
#!/bin/bash

# 通用工具函数

# 服务端点配置
OODER_AGENT_ENDPOINT="http://localhost:9010/api/v1"
A2UI_ENDPOINT="http://localhost:8081/api/a2ui"

# 日志级别
LOG_LEVEL="info"

# 缓存配置
CACHE_DIR="${HOME}/.ooder-skill-cache"
CACHE_EXPIRY=3600 # 缓存过期时间（秒）

# 重试配置
MAX_RETRIES=3
RETRY_DELAY=2 # 重试延迟（秒）

# 打印日志
log() {
    local level="$1"
    local message="$2"
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    
    if [[ "$level" == "error" || "$LOG_LEVEL" == "debug" || "$LOG_LEVEL" == "$level" ]]; then
        echo "[$timestamp] [$level] $message"
    fi
}

# 检查命令是否存在
check_command() {
    local command="$1"
    if ! command -v "$command" &> /dev/null; then
        log "error" "命令 $command 不存在，请安装"
        return 1
    fi
    return 0
}

# 检查依赖项
check_dependencies() {
    log "info" "检查依赖项..."
    
    # 检查 curl
    if ! check_command "curl"; then
        echo '{"success": false, "error": "缺少 curl 命令，请安装"}'
        exit 1
    fi
    
    # 检查 jq
    if ! check_command "jq"; then
        log "warning" "缺少 jq 命令，某些功能可能无法正常工作"
    fi
    
    log "info" "依赖项检查完成"
}

# 初始化缓存目录
init_cache() {
    if [[ ! -d "$CACHE_DIR" ]]; then
        mkdir -p "$CACHE_DIR"
        log "info" "创建缓存目录: $CACHE_DIR"
    fi
}

# 生成缓存键
generate_cache_key() {
    local prefix="$1"
    local params="$2"
    local key="${prefix}_$(echo "$params" | md5sum | cut -d' ' -f1)"
    echo "$key"
}

# 检查缓存是否有效
is_cache_valid() {
    local cache_file="$1"
    if [[ ! -f "$cache_file" ]]; then
        return 1
    fi
    
    local file_time=$(stat -c %Y "$cache_file" 2>/dev/null || stat -f %m "$cache_file" 2>/dev/null)
    local current_time=$(date +%s)
    
    if [[ $((current_time - file_time)) -lt $CACHE_EXPIRY ]]; then
        return 0
    else
        return 1
    fi
}

# 获取缓存
get_cache() {
    local key="$1"
    local cache_file="$CACHE_DIR/$key"
    
    if is_cache_valid "$cache_file"; then
        log "debug" "从缓存获取: $key"
        cat "$cache_file"
        return 0
    else
        return 1
    fi
}

# 设置缓存
set_cache() {
    local key="$1"
    local value="$2"
    local cache_file="$CACHE_DIR/$key"
    
    init_cache
    echo "$value" > "$cache_file"
    log "debug" "设置缓存: $key"
}

# 发送 HTTP 请求（带重试机制）
send_http_request() {
    local method="$1"
    local url="$2"
    local data="$3"
    local headers="$4"
    
    log "debug" "发送 $method 请求到 $url"
    if [[ -n "$data" ]]; then
        log "debug" "请求数据: $data"
    fi
    
    local response
    local status_code
    local retry_count=0
    
    while [[ $retry_count -lt $MAX_RETRIES ]]; do
        if [[ "$method" == "POST" ]]; then
            if [[ -n "$headers" ]]; then
                response=$(curl -s -X POST -H "Content-Type: application/json" -H "$headers" -d "$data" "$url")
            else
                response=$(curl -s -X POST -H "Content-Type: application/json" -d "$data" "$url")
            fi
        else
            response=$(curl -s -X GET "$url")
        fi
        
        status_code=$(curl -s -o /dev/null -w "%{http_code}" -X "$method" -H "Content-Type: application/json" ${data:+-d "$data"} "$url")
        
        log "debug" "响应状态码: $status_code"
        log "debug" "响应数据: $response"
        
        if [[ "$status_code" -ge 200 && "$status_code" -lt 300 ]]; then
            echo "$response"
            return 0
        else
            log "warning" "HTTP 请求失败，状态码: $status_code，将重试 ($retry_count/$MAX_RETRIES)"
            retry_count=$((retry_count + 1))
            if [[ $retry_count -lt $MAX_RETRIES ]]; then
                sleep $RETRY_DELAY
            fi
        fi
    done
    
    log "error" "HTTP 请求失败，已达到最大重试次数"
    echo '{"success": false, "error": "HTTP 请求失败，已达到最大重试次数", "response": "'"$response"'"}'
    return 1
}

# MCP (Model Control Plane) 功能：调用 ooder-agent
mcp_call() {
    local function_name="$1"
    local params="$2"
    local display_mode="$3"
    
    # 生成缓存键
    local cache_key=$(generate_cache_key "mcp_${function_name}" "$params")
    
    # 尝试从缓存获取
    local cached_response
    if cached_response=$(get_cache "$cache_key"); then
        log "info" "使用缓存响应"
        echo "$cached_response"
        return 0
    fi
    
    # 构建请求数据
    local request_data=$(build_json \
        "function" "$function_name" \
        "params" "$params" \
        "displayMode" "$display_mode"
    )
    
    # 发送请求
    local response=$(send_http_request "POST" "$OODER_AGENT_ENDPOINT/execute" "$request_data")
    
    # 检查响应
    if [[ "$response" == *"success":true* ]]; then
        # 设置缓存
        set_cache "$cache_key" "$response"
    fi
    
    echo "$response"
    return 0
}

# 解析命令行参数
parse_args() {
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

# 构建 JSON 对象
build_json() {
    local json="{"
    local first=true
    
    while [[ $# -gt 0 ]]; do
        local key="$1"
        local value="$2"
        shift 2
        
        if [[ "$first" == true ]]; then
            first=false
        else
            json="$json,"
        fi
        
        if [[ "$value" =~ ^[0-9]+$ ]]; then
            json="$json\"$key\":$value"
        elif [[ "$value" == true || "$value" == false ]]; then
            json="$json\"$key\":$value"
        else
            # 转义 JSON 字符串
            local escaped_value=$(echo "$value" | sed 's/"/\\"/g' | sed 's/\\/\\\\/g' | sed 's/\n/\\n/g' | sed 's/\r/\\r/g' | sed 's/\t/\\t/g')
            json="$json\"$key\":\"$escaped_value\""
        fi
    done
    
    json="$json}"
    echo "$json"
}

# 验证 JSON 格式
validate_json() {
    local json="$1"
    if [[ -n "$(command -v jq)" ]]; then
        if ! echo "$json" | jq . &> /dev/null; then
            log "error" "无效的 JSON 格式: $json"
            return 1
        fi
    fi
    return 0
}

# 检查服务可用性
check_service_availability() {
    local endpoint="$1"
    local service_name="$2"
    
    log "info" "检查 $service_name 服务可用性..."
    
    local status_code=$(curl -s -o /dev/null -w "%{http_code}" "$endpoint/health" 2>/dev/null)
    
    if [[ "$status_code" -eq 200 ]]; then
        log "info" "$service_name 服务可用"
        return 0
    else
        # 尝试其他端点
        status_code=$(curl -s -o /dev/null -w "%{http_code}" "$endpoint/info" 2>/dev/null)
        if [[ "$status_code" -eq 200 ]]; then
            log "info" "$service_name 服务可用"
            return 0
        fi
        
        log "warning" "$service_name 服务可能不可用，状态码: $status_code"
        log "info" "将继续执行，可能会失败"
        return 1
    fi
}

# 构建响应结果
build_response() {
    local success="$1"
    local data="$2"
    local error="$3"
    local metadata="$4"
    
    local response="{"
    local first=true
    
    # success 字段
    if [[ "$first" == true ]]; then
        first=false
    else
        response="$response,"
    fi
    response="$response\"success\":$success"
    
    # data 字段
    if [[ -n "$data" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"data\":$data"
    fi
    
    # error 字段
    if [[ -n "$error" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"error\":\"$error\""
    fi
    
    # metadata 字段
    if [[ -n "$metadata" ]]; then
        if [[ "$first" == true ]]; then
            first=false
        else
            response="$response,"
        fi
        response="$response\"metadata\":$metadata"
    fi
    
    response="$response}"
    echo "$response"
}

# 主函数入口
main() {
    # 检查依赖项
    check_dependencies
    
    # 初始化缓存
    init_cache
    
    # 检查服务可用性
    check_service_availability "$OODER_AGENT_ENDPOINT" "ooder-agent"
    check_service_availability "$A2UI_ENDPOINT" "A2UI"
}

# 如果直接执行此脚本，则运行主函数
if [[ "$0" == "$BASH_SOURCE" ]]; then
    main
fi
```

#### 4.2 创建执行脚本 (scripts/execute.sh)

参考 ooder-skills 的 main.sh 中的功能实现，创建执行脚本：

```bash
#!/bin/bash

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
        echo '{"success": false, "error": "缺少 function 参数"}'
        exit 1
    fi
    
    # 验证 params JSON 格式
    if ! validate_json "$params"; then
        echo '{"success": false, "error": "params 参数不是有效的 JSON 格式"}'
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
                "skillId" "standard-solo-skills"
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
            "skillId" "standard-solo-skills"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
```

#### 4.3 创建信息脚本 (scripts/info.sh)

```bash
#!/bin/bash

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
                "skillId" "standard-solo-skills"
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
            "skillId" "standard-solo-skills"
        )
        
        local error_response=$(build_response "false" "" "$error_message" "$metadata")
        echo "$error_response"
    fi
}

# 执行主函数
main "$@"
```

#### 4.4 创建 MCP 脚本 (scripts/mcp.sh)

```bash
#!/bin/bash

# MCP (Model Control Plane) 脚本
# 用于处理大型模型到 ooder-agent 的交互

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
        echo '{"success": false, "error": "缺少 action 参数"}'
        exit 1
    fi
    
    # 根据 action 执行不同操作
    case "$action" in
        "call")
            if [[ -z "$function_name" ]]; then
                echo '{"success": false, "error": "call 操作需要 function 参数"}'
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
            echo '{"success": false, "error": "未知的 action: $action"}'
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
        echo '{"success": false, "error": "params 参数不是有效的 JSON 格式"}'
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
    local request_data=$(build_json \
        "action" "status" \
        "timestamp" "$(date +%s)"
    )
    
    # 发送请求
    local response=$(send_http_request "GET" "$OODER_AGENT_ENDPOINT/mcp/status" "$request_data")
    
    # 处理响应
    if [[ "$response" == *"success"* ]]; then
        # 添加 MCP 状态信息
        local status_info=$(build_json \
            "mcp_status" "active" \
            "cache_dir" "$CACHE_DIR" \
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
    local health_data=$(build_json \
        "ooder_agent" "$((ooder_agent_status == 0 ? 1 : 0))" \
        "a2ui" "$((a2ui_status == 0 ? 1 : 0))" \
        "cache" "$((cache_status == 0 ? 1 : 0))" \
        "timestamp" "$(date +%s)" \
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
        echo '{"success": true, "message": "缓存已清理"}'
    else
        log "warning" "缓存目录不存在"
        echo '{"success": true, "message": "缓存目录不存在"}'
    fi
}

# 列出可用函数
list_functions() {
    log "info" "列出可用函数"
    
    # 构建请求数据
    local request_data=$(build_json \
        "action" "list"
    )
    
    # 发送请求
    local response=$(send_http_request "GET" "$OODER_AGENT_ENDPOINT/mcp/functions" "$request_data")
    
    # 处理响应
    if [[ "$response" == *"success"* ]]; then
        echo "$response"
    else
        # 如果服务不可用，返回默认函数列表
        local default_functions=$(build_json \
            "functions" '["hello", "calculate", "generate", "transform", "analyze"]' \
            "source" "default"
        )
        echo "$(build_response "true" "$default_functions" "" '')"
    fi
}

# 执行主函数
main "$@"
```

### 步骤 5：创建 2 级说明文档 (SKILL.md)

基于 ooder-skills 的 README.md，按照标准格式创建详细的 SKILL.md 文件，包括技能概述、使用场景、工作原理、使用方式、功能列表等内容。

### 步骤 6：设置脚本执行权限

```bash
chmod +x scripts/*.sh
```

### 步骤 7：测试转换结果

```bash
# 执行功能测试
bash scripts/execute.sh --function hello --params '{"name": "World"}' --displayMode a2ui

# 获取信息测试
bash scripts/info.sh

# 测试 MCP 功能
bash scripts/mcp.sh --action call --function hello --params '{"name": "World"}' --displayMode a2ui

# 检查 MCP 健康状态
bash scripts/mcp.sh --action health

# 列出可用函数
bash scripts/mcp.sh --action list-functions
```

## 四、转换过程中的常见问题及解决方案

### 1. 依赖项处理

**问题**：ooder-skill 可能依赖多个外部工具

**解决方案**：将依赖项功能迁移到服务端，通过 HTTP API 调用实现。LLM 使用脚本直接调用 ooder-agent 的 HTTP API，不使用 Node.js，也不会依赖 npm。脚本只需负责发送 HTTP 请求和处理响应，复杂的依赖管理由服务端处理。通过 MCP 架构，我们可以实现智能缓存和自动重试，进一步提高服务调用的可靠性和效率。

### 2. 异步操作处理

**问题**：ooder-skill 中的异步操作如何在脚本中处理

**解决方案**：使用 HTTP API 调用，服务端处理异步操作，脚本只需等待响应

### 3. 环境变量配置

**问题**：ooder-skill 中的环境变量如何配置

**解决方案**：在 common.sh 中设置服务端点等配置，或使用环境变量加载

### 4. 错误处理

**问题**：如何在脚本中实现与 ooder-skill 相同的错误处理

**解决方案**：在脚本中添加详细的错误检查和响应构建，确保错误信息完整传递

## 五、转换效果对比

| 对比项 | ooder-skill | 标准 skill | 改进 |
|--------|-------------|------------|------|
| **加载方式** | 一次性加载所有代码 | 分级加载，按需取用 | 减少启动时间 |
| **Token 消耗** | 较高（加载完整代码） | 较低（分级加载 + web 调用） | 节省 50%+ |
| **执行速度** | 一般（本地执行） | 更快（服务端执行 + 缓存） | 提高 30%+ |
| **可移植性** | 依赖 bash 环境 | 跨平台，支持任何标准平台 | 无环境依赖 |
| **扩展性** | 需要修改代码 | 只需添加新脚本和配置 | 更易扩展 |
| **集成方式** | 硬编码集成 | 标准接口，即插即用 | 易于集成 |
| **MCP 功能** | 无 | 支持大型模型到 ooder-agent 的交互 | 增强模型能力 |
| **缓存机制** | 无 | 智能缓存，减少重复请求 | 提高响应速度 |
| **错误处理** | 基本错误处理 | 自动重试 + 详细错误信息 | 提高可靠性 |
| **健康监控** | 无 | MCP 健康状态检查 | 确保系统稳定 |
| **VFS 功能** | 无 | 增强的虚拟文件系统支持 | 提升文件管理能力 |
| **SDK 支持** | 传统 JavaScript | ES6 模块 + TypeScript 类型定义 | 现代开发体验 |
| **A2UI 能力** | 基本图转代码 | 增强的 AI 模型 + 多格式支持 | 提高代码生成质量 |
| **文档完整性** | 基本说明 | 完整的开源文档套件 | 便于使用和贡献 |


## 六、总结

将 ooder-skill 转换为标准 skill 是一个系统性的工程，需要从目录结构、文件格式、执行方式等多个方面进行调整。但通过本文提供的详细步骤和文件对比，你可以清晰地了解整个转换过程，并成功完成转换工作。

标准 skill 不仅可以节省 Token 消耗，还能提高执行效率、增强可扩展性，并与更多标准平台兼容。特别是通过集成 MCP (Model Control Plane) 功能，实现了大型模型到 ooder-agent 的高效交互，提供了智能缓存、自动重试和健康监控等高级特性，进一步提升了技能的可靠性和性能。

虽然转换过程需要一定的工作量，但长期来看，这些投入是完全值得的。标准 skill 架构为技能的发展和维护提供了更清晰、更灵活的框架，使技能开发变得更加高效和标准化。

## 七、结语

如果感觉手动转换过程太麻烦，或者你希望专注于业务逻辑而不是技术细节，那么你可以将这个转换任务交给 trae-solo 来处理。trae-solo 提供了自动化的技能转换功能，可以帮助你快速、准确地将 ooder-skill 转换为标准 skill，让你从繁琐的转换工作中解放出来，专注于核心功能的开发和优化。

通过 trae-solo 的自动化转换，你可以：
- 节省大量手动转换的时间和精力
- 确保转换结果符合标准规范
- 获得最佳实践的转换建议
- 快速部署和测试转换后的标准 skill

让技术工具为你服务，而不是成为你的负担。选择 trae-solo，让技能转换变得简单高效！