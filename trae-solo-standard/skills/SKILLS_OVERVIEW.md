# Skills 功能分析与命令构建

## 技能目录结构

```
skills/
├── a2ui/           # A2UI 图生代码技能
├── skill-b/        # 数据提交服务技能
└── trae-solo/      # Trae Solo 实用工具技能
```

## 技能功能分析

### 1. A2UI 技能

**版本**：0.3.0
**类别**：UI_SKILL
**描述**：提供图生代码功能，采用ooder-a2ui格式生成前端界面，支持四分离设计原则

**核心功能**：

| 功能名称 | 描述 | 参数 | 返回类型 |
|---------|------|------|----------|
| get-components | 获取A2UI组件列表 | 无 | array |
| generate-ui | 根据图片生成UI代码 | image_url: string<br>options: object | object |
| get-component-details | 获取组件详细信息 | component_id: string | object |
| create-view | 创建视图 | view_name: string<br>components: array<br>layout: object | object |
| submit-data | 提交数据到A2UI服务 | target: string<br>data: object<br>options: object | object |

**支持的工具**：
- a2ui-api：通过HTTP API调用A2UI服务
- ooder-agent：通过HTTP API调用ooder-agent服务
- mcp-agent：通过HTTP API调用mcpAgent服务（作为aibridge处理LLM需求）
- route-agent：通过HTTP API调用routeAgent服务（场景核心，管理endAgent）

### 2. SKILL B 技能

**版本**：0.1.0
**类别**：service
**描述**：数据提交服务，负责将处理后的数据提交到目标系统

**核心功能**：

| 功能名称 | 描述 | 参数 | 返回类型 |
|---------|------|------|----------|
| submit-data | 提交数据到目标系统 | target: string (必填)<br>data: object (必填)<br>options: object (可选) | object |
| info | 获取技能信息 | 无 | object |

**执行环境**：
- bash
- curl

**依赖服务**：
- ooder-agent：http://localhost:9010/api/v1

### 3. Trae Solo 技能

**版本**：0.2.0
**类别**：UTILITY_SKILL
**描述**：连接实用功能，运行结果通过skills-a2ui节点展示，支持A2UI图转代码技术集成

**支持的工具**：
- web-api：通过HTTP API调用ooder-agent服务
- a2ui-api：通过HTTP API调用A2UI服务

**脚本**：
- a2ui-generate.sh：A2UI代码生成
- execute.sh：执行命令
- info.sh：获取技能信息
- mcp.sh：MCP相关功能

## MCP 架构下的命令构建

### 命令格式

```bash
# 通过 mcp-agent.sh 执行技能功能
bash scripts/mcp-agent.sh <action> <skill_id> [<parameters>]

# 直接执行技能脚本
bash <skill_directory>/scripts/<function_name>.sh [<parameters>]
```

### 核心命令

#### 1. mcpAgent 相关命令

| 命令 | 描述 | 参数 | 示例 |
|------|------|------|------|
| execute-llm-query | 执行LLM查询 | skill_id: 技能ID<br>query: 查询语句<br>parameters: 参数对象 | `bash mcp-agent.sh execute-llm-query "a2ui" "如何优化表单组件" "{}"` |
| register-end-agent | 注册endAgent | agent_id: 代理ID<br>commands: 支持的命令列表<br>endpoint: 端点URL | `bash mcp-agent.sh register-end-agent "agent-1" '["get-components", "generate-ui"]' "http://localhost:8084/api/end-agent"` |
| broadcast-command | 广播命令到组内成员 | group_id: 组ID<br>command: 命令名称<br>parameters: 参数对象 | `bash mcp-agent.sh broadcast-command "group-1" "get-components" "{}"` |
| send-command | 向指定endAgent发送命令 | agent_id: 代理ID<br>command: 命令名称<br>parameters: 参数对象 | `bash mcp-agent.sh send-command "agent-1" "generate-ui" '{"image_url": "https://example.com/ui.png"}'` |
| query-result | 查询命令执行结果 | command_id: 命令ID | `bash mcp-agent.sh query-result "cmd-123"` |
| get-status | 获取mcpAgent状态 | 无 | `bash mcp-agent.sh get-status` |

#### 2. A2UI 技能命令

| 命令 | 描述 | 参数 | 示例 |
|------|------|------|------|
| get-components | 获取A2UI组件列表 | 无 | `bash a2ui/scripts/get-components.sh` |
| generate-ui | 根据图片生成UI代码 | --image: 图片URL<br>--format: 输出格式<br>--theme: 主题 | `bash a2ui/scripts/generate-ui.sh --image "https://example.com/login.png" --format html --theme light` |
| get-component-details | 获取组件详细信息 | --id: 组件ID | `bash a2ui/scripts/get-component-details.sh --id "button"` |
| create-view | 创建视图 | --name: 视图名称<br>--components: 组件列表<br>--layout: 布局配置 | `bash a2ui/scripts/create-view.sh --name "LoginForm" --components '["Input", "Button"]' --layout '{"type": "form"}'` |
| submit-data | 提交数据到A2UI服务 | --target: 目标<br>--data: 数据<br>--options: 选项 | `bash a2ui/scripts/submit-data.sh --target "database" --data '{"id": 1, "name": "Test"}' --options '{"mode": "insert"}'` |
| info | 获取技能信息 | 无 | `bash a2ui/scripts/info.sh` |

#### 3. SKILL B 技能命令

| 命令 | 描述 | 参数 | 示例 |
|------|------|------|------|
| submit-data | 提交数据到目标系统 | --target: 目标系统类型<br>--data: 要提交的数据<br>--options: 提交选项 | `bash skill-b/scripts/submit-data.sh --target "database" --data '{"id": 1, "name": "Test"}' --options '{"mode": "insert"}'` |
| info | 获取技能信息 | 无 | `bash skill-b/scripts/info.sh` |

#### 4. Trae Solo 技能命令

| 命令 | 描述 | 参数 | 示例 |
|------|------|------|------|
| a2ui-generate | 生成A2UI代码 | --image: 图片URL<br>--options: 生成选项 | `bash trae-solo/scripts/a2ui-generate.sh --image "https://example.com/ui.png" --options '{"format": "html"}'` |
| execute | 执行命令 | --command: 命令名称<br>--params: 命令参数 | `bash trae-solo/scripts/execute.sh --command "get-components" --params "{}"` |
| info | 获取技能信息 | 无 | `bash trae-solo/scripts/info.sh` |
| mcp | 执行MCP相关功能 | --action: 操作名称<br>--params: 操作参数 | `bash trae-solo/scripts/mcp.sh --action "execute-llm-query" --params '{"skill_id": "a2ui", "query": "如何优化表单组件"}'` |

## MCP 架构调用流程

### 1. 通过 mcpAgent 调用技能

```
+-----------------+     +-----------------+     +-----------------+     +-----------------+
|                 |     |                 |     |                 |     |                 |
|  用户/IDE        |---->|  mcpAgent       |---->|  routeAgent     |---->|  目标技能       |
|                 |     | (AIBridge)      |     | (Scene Core)    |     | (endAgent)      |
|                 |     |                 |     |                 |     |                 |
+-----------------+     +-----------------+     +-----------------+     +-----------------+
```

### 2. 调用步骤

1. **查询 mcpAgent 状态**：确保 mcpAgent 服务正常运行
   ```bash
   bash mcp-agent.sh get-status
   ```

2. **根据技能查找 mcpAgent id**：确定要使用的技能
   ```bash
   # 内部执行，通过技能ID查找对应的agent
   ```

3. **向指定 endAgent 发送执行命令及参数**：
   ```bash
   bash mcp-agent.sh send-command "agent-id" "function-name" '{"param1": "value1", "param2": "value2"}'
   ```

4. **等待返回或异步查询结果**：
   ```bash
   bash mcp-agent.sh query-result "command-id"
   ```

## 技能注册与发现

### 技能注册

当技能被添加到 `skills` 目录后，trae-solo IDE 会自动发现并加载这些技能。技能的注册信息包含在每个技能目录下的 `skill.yaml` 文件中。

### 技能发现

- **IDE 发现**：trae-solo IDE 会扫描 `skills` 目录，读取每个技能的 `skill.yaml` 文件
- **mcpAgent 发现**：mcpAgent 会通过 routeAgent 管理已注册的 endAgent

## 命令执行示例

### 示例 1：使用 A2UI 技能生成 UI 代码

```bash
# 直接执行
bash skills/a2ui/scripts/generate-ui.sh --image "https://example.com/login.png" --format html --theme light

# 通过 mcpAgent 执行
bash skills/a2ui/scripts/mcp-agent.sh send-command "a2ui-agent" "generate-ui" '{"image_url": "https://example.com/login.png", "options": {"format": "html", "theme": "light"}}'
```

### 示例 2：使用 SKILL B 提交数据

```bash
# 直接执行
bash skills/skill-b/scripts/submit-data.sh --target "database" --data '{"id": 1, "name": "Test"}' --options '{"mode": "insert"}'

# 通过 mcpAgent 执行
bash skills/a2ui/scripts/mcp-agent.sh send-command "skill-b-agent" "submit-data" '{"target": "database", "data": {"id": 1, "name": "Test"}, "options": {"mode": "insert"}}'
```

### 示例 3：执行 LLM 查询

```bash
# 通过 mcpAgent 执行 LLM 查询
bash skills/a2ui/scripts/mcp-agent.sh execute-llm-query "a2ui" "如何优化表单组件" '{"context": "A2UI 四分离设计原则"}'
```

## 错误处理与故障排除

### 常见错误

| 错误类型 | 可能原因 | 解决方案 |
|---------|---------|---------|
| 服务不可用 | 相关服务未运行 | 启动对应的服务（如 a2ui、ooder-agent、mcpAgent） |
| 参数错误 | 参数格式不正确 | 检查参数格式，确保符合要求 |
| 网络连接 | 网络连接失败 | 检查网络连接，确保服务可访问 |
| 权限问题 | 脚本执行权限不足 | 赋予脚本执行权限：`chmod +x script.sh` |

### 故障排除步骤

1. **检查服务状态**：确保所有依赖服务都在运行
2. **验证网络连接**：确保服务可通过指定的 URL 访问
3. **检查参数格式**：确保提供的参数格式正确
4. **查看日志**：检查脚本执行日志，了解具体错误信息
5. **使用模拟模式**：在 Windows 环境或服务不可用时，脚本会自动使用模拟模式

## 最佳实践

1. **使用 mcpAgent 调用**：对于复杂任务，建议通过 mcpAgent 调用，以获得更好的错误处理和重试机制
2. **参数验证**：在调用前验证参数格式，确保符合要求
3. **错误处理**：实现适当的错误处理逻辑，处理可能的失败情况
4. **超时设置**：对于可能耗时较长的操作，设置合理的超时时间
5. **缓存策略**：对于频繁使用的结果，考虑使用缓存以提高性能

## 版本兼容性

| 技能 | 最低 mcpAgent 版本 | 最低 routeAgent 版本 | 说明 |
|------|------------------|---------------------|------|
| A2UI | 0.2.0 | 0.1.0 | 支持所有核心功能 |
| SKILL B | 0.1.0 | 0.1.0 | 支持数据提交功能 |
| Trae Solo | 0.1.0 | 0.1.0 | 支持基本工具功能 |
