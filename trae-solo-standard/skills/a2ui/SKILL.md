# A2UI Skill

## 技能概述

A2UI Skill 是一个标准的 Skills 实现，提供图生代码功能，采用 ooder-a2ui 格式生成前端界面，通过 web API 调用 A2UI 服务和 ooder-agent 服务，支持四分离设计原则。

## 使用场景

- **图生代码**：将 UI 设计图片转换为前端代码
- **Web 服务集成**：通过 HTTP API 与 A2UI 服务和 ooder-agent 服务集成
- **节省 Token**：利用分级加载机制和 web 调用，减少 Token 消耗
- **四分离设计**：支持属性、样式、事件、行为的分离设计
- **trae-solo IDE 集成**：专为 trae-solo IDE 优化，提供无缝的技能调用体验

## 工作原理

### 三层分级加载机制

A2UI Skill 采用三层分级加载机制：

1. **1 级：元数据** - `skill.yaml` 文件，包含技能的基本信息，轻量级加载
2. **2 级：说明文档** - 本文件 (`SKILL.md`)，包含详细的使用说明，按需加载
3. **3 级及以上** - 脚本和资源文件，按需加载执行

### 四分离设计原则

A2UI Skill 实现了 Ooder A2UI 的四分离设计原则：

1. **属性（Properties）**：组件的配置属性和数据模型
2. **样式（Styles）**：组件的 CSS 样式
3. **事件（Events）**：组件的交互事件
4. **行为（Behaviors）**：组件的行为规则和约束

### 技能执行流程

1. trae-solo IDE 读取 `skill.yaml` 了解技能基本信息
2. 当用户请求匹配技能描述时，IDE 读取本文件获取详细说明
3. IDE 根据需要执行 `scripts/` 目录中的脚本
4. 脚本通过 HTTP API 调用相关服务：
   - **mcpAgent**：作为 aibridge 统一处理 LLM 需求
   - **routeAgent**：作为场景核心，管理 endAgent 组和命令广播
   - **ooder-agent**：处理具体业务逻辑
   - **A2UI**：处理图生代码功能
5. 将执行结果返回给 IDE，由 IDE 展示给用户

## 使用方式

### 在 trae-solo IDE 中使用

1. **安装技能**：将 `a2ui` 目录复制到 trae-solo IDE 的 skills 目录中
2. **调用技能**：在 IDE 中直接输入技能调用指令，例如：
   ```
   使用 A2UI 技能生成 UI 界面，图片为 "https://example.com/login-design.png"，格式为 html
   ```

### 命令行使用

```bash
# 生成 UI 界面
bash scripts/generate-ui.sh --image <图片URL> --format <输出格式> --theme <主题>

# 预览 UI 界面
bash scripts/preview-ui.sh --uiCode <UI代码> --format <代码格式>

# 获取组件列表
bash scripts/get-components.sh

# 获取技能信息
bash scripts/info.sh
```

## 功能列表

| 功能名称 | 描述 | 参数 |
|---------|------|------|
| generate-ui | 生成 UI 界面 | image_url: 图片URL, options: 生成选项 |
| preview-ui | 预览 UI 界面 | uiCode: UI代码, format: 代码格式(html/js) |
| get-components | 获取组件列表 | 无 |
| get-component-details | 获取组件详细信息 | component_id: 组件ID |
| create-view | 创建视图 | view_name: 视图名称, components: 组件列表, layout: 布局配置 |
| submit-data | 提交数据 | target: 目标, data: 数据, options: 选项 |
| info | 获取技能信息 | 无 |

## 使用步骤

### 步骤 1：准备环境

确保 A2UI 服务正在运行：
- A2UI 服务：`http://localhost:8081/api/a2ui`

### 步骤 2：在 trae-solo IDE 中配置

1. 打开 trae-solo IDE
2. 进入技能管理界面
3. 点击 "添加技能"，选择 `a2ui` 目录
4. 确认技能加载成功

### 步骤 3：执行功能

1. **生成 UI 界面**：
   ```bash
   bash scripts/generate-ui.sh --image "https://example.com/login-design.png" --format html --theme light
   ```

2. **预览 UI 界面**：
   ```bash
   bash scripts/preview-ui.sh --uiCode "<div class=\"login-form\">...</div>" --format html
   ```

3. **获取组件列表**：
   ```bash
   bash scripts/get-components.sh
   ```

### 步骤 4：处理结果

脚本执行后，会返回 JSON 格式的结果，包含：
- `success`：执行是否成功
- `data`：执行结果数据
- `metadata`：执行元数据（如执行时间、使用的功能等）

在 trae-solo IDE 中，结果会以友好的方式展示给用户。

## 注意事项

1. **服务依赖**：确保 A2UI 服务正在运行，并且可以通过指定的 URL 访问

2. **网络连接**：技能执行需要网络连接，确保网络正常

3. **参数格式**：
   - `image` 参数必须是可访问的 URL 或有效的 Base64 编码
   - `uiCode` 参数必须是有效的 UI 代码

4. **性能考虑**：
   - 对于复杂的 UI 生成，可能需要较长时间
   - 建议对大图片进行适当压缩后再使用

5. **错误处理**：
   - 如果服务不可用，脚本会返回错误信息
   - 如果参数格式错误，脚本会返回详细的错误提示

## 脚本说明

### 脚本列表

- **generate-ui.sh**：生成 UI 界面
- **preview-ui.sh**：预览 UI 界面
- **get-components.sh**：获取组件列表
- **info.sh**：获取技能信息
- **common.sh**：通用工具函数

### 脚本工作原理

1. 解析命令行参数
2. 构建 HTTP 请求
3. 调用相应的 API 端点
4. 处理响应结果
5. 格式化并返回结果

## API 端点

### A2UI 服务

- **生成 UI 代码**：`POST http://localhost:8081/api/a2ui/generate-ui`
- **预览 UI 界面**：`POST http://localhost:8081/api/a2ui/preview-ui`
- **获取组件列表**：`GET http://localhost:8081/api/a2ui/components`
- **获取组件详细信息**：`GET http://localhost:8081/api/a2ui/components/{id}`
- **创建视图**：`POST http://localhost:8081/api/a2ui/views`
- **健康检查**：`GET http://localhost:8081/api/a2ui/health`

### ooder-agent 服务

- **执行命令**：`POST http://localhost:8080/api/ooder-agent/execute`
- **获取状态**：`GET http://localhost:8080/api/ooder-agent/status`
- **健康检查**：`GET http://localhost:8080/api/ooder-agent/health`

### mcpAgent 服务

- **执行 LLM 查询**：`POST http://localhost:8082/api/mcp-agent/llm/query`
- **执行命令**：`POST http://localhost:8082/api/mcp-agent/execute`
- **获取状态**：`GET http://localhost:8082/api/mcp-agent/status`
- **健康检查**：`GET http://localhost:8082/api/mcp-agent/health`
- **查找 Agent**：`POST http://localhost:8082/api/mcp-agent/agent/find`

### routeAgent 服务

- **注册 endAgent**：`POST http://localhost:8083/api/route-agent/register`
- **广播命令**：`POST http://localhost:8083/api/route-agent/broadcast`
- **执行命令**：`POST http://localhost:8083/api/route-agent/execute`
- **获取组内成员**：`GET http://localhost:8083/api/route-agent/groups/{group_id}/members`
- **查询命令结果**：`GET http://localhost:8083/api/route-agent/commands/{command_id}/result`
- **获取状态**：`GET http://localhost:8083/api/route-agent/status`
- **健康检查**：`GET http://localhost:8083/api/route-agent/health`

## 示例

### 示例 1：生成 UI 界面

**命令**：
```bash
bash scripts/generate-ui.sh --image "https://example.com/login-design.png" --format html --theme light
```

**预期输出**：
```json
{
  "success": true,
  "data": {
    "function": "generate-ui",
    "image": "https://example.com/login-design.png",
    "format": "html",
    "theme": "light",
    "uiCode": "<div class=\"login-form theme-light\">...</div>",
    "components": ["Button", "Input", "Panel", "Label"]
  },
  "metadata": {
    "function": "generate-ui",
    "executionTime": 4567,
    "skillId": "a2ui"
  }
}
```

### 示例 2：预览 UI 界面

**命令**：
```bash
bash scripts/preview-ui.sh --uiCode "<div class=\"login-form\">...</div>" --format html
```

**预期输出**：
```json
{
  "success": true,
  "data": {
    "function": "preview-ui",
    "format": "html",
    "previewUrl": "http://localhost:8081/api/a2ui/preview-ui?code=..."
  },
  "metadata": {
    "function": "preview-ui",
    "executionTime": 123,
    "skillId": "a2ui"
  }
}
```

### 示例 3：获取组件列表

**命令**：
```bash
bash scripts/get-components.sh
```

**预期输出**：
```json
{
  "success": true,
  "data": {
    "function": "get-components",
    "components": [
      {"name": "Button", "category": "基础组件"},
      {"name": "Input", "category": "基础组件"},
      {"name": "Panel", "category": "布局组件"},
      {"name": "Tabs", "category": "导航组件"}
    ]
  },
  "metadata": {
    "function": "get-components",
    "executionTime": 45,
    "skillId": "a2ui"
  }
}
```

### 示例 4：在 trae-solo IDE 中使用

**输入**：
```
使用 A2UI 技能生成登录界面，图片为 "https://example.com/login.png"
```

**预期输出**：
```
执行结果：
UI 代码已生成，包含以下组件：Button、Input、Panel、Label
执行时间：4567ms
预览地址：http://localhost:8081/api/a2ui/preview-ui?code=...
```

### 示例 5：使用 mcpAgent 执行 LLM 查询

**输入**：
```
使用 A2UI 技能通过 mcpAgent 查询如何优化表单组件
```

**预期输出**：
```
执行结果：
LLM 查询结果：
表单组件优化建议：
1. 使用响应式布局，适配不同屏幕尺寸
2. 添加表单验证和错误提示
3. 实现表单数据的本地存储
4. 优化表单提交的用户体验
5. 添加表单填写进度指示

执行时间：1234ms
通过 mcpAgent 执行，使用的 LLM 模型：gpt-4
```

**命令行示例**：
```bash
# 通过 mcpAgent 执行 LLM 查询
bash scripts/mcp-agent.sh execute-llm-query "a2ui" "如何优化表单组件" "{}"
```

## MCP 架构设计

### mcpAgent（Model Control Plane Agent）

**功能**：作为 aibridge 统一处理 LLM 需求，汇总 routeAgent 的 LLM 请求，统一与 LLM 完成交互。

**工作原理**：
1. **状态管理**：维护自身状态和可用的 LLM 服务状态
2. **请求路由**：根据技能 ID 和请求类型，选择合适的 LLM 服务
3. **请求聚合**：汇总多个 routeAgent 的 LLM 请求，批量处理以提高效率
4. **响应缓存**：缓存频繁使用的 LLM 响应，减少重复请求
5. **错误处理**：实现重试机制和故障转移，确保 LLM 请求的可靠性

**API 端点**：
- **执行 LLM 查询**：`POST http://localhost:8082/api/mcp-agent/llm/query`
- **获取状态**：`GET http://localhost:8082/api/mcp-agent/status`
- **健康检查**：`GET http://localhost:8082/api/mcp-agent/health`

### routeAgent（路由 Agent）

**功能**：作为场景核心，通过内部建组的方式与 endAgent 完成交互，管理命令的广播和执行结果的收集。

**工作原理**：
1. **组管理**：创建和管理 endAgent 组，支持动态添加和移除成员
2. **命令广播**：向组内在线成员广播命令，处理 endAgent 的响应（不支持命令、接收成功）
3. **结果收集**：异步收集命令执行结果，处理超时和失败情况
4. **状态监控**：监控 endAgent 的在线状态和健康状况

**API 端点**：
- **注册 endAgent**：`POST http://localhost:8083/api/route-agent/register`
- **广播命令**：`POST http://localhost:8083/api/route-agent/broadcast`
- **执行命令**：`POST http://localhost:8083/api/route-agent/execute`
- **获取组内成员**：`GET http://localhost:8083/api/route-agent/groups/{group_id}/members`
- **查询命令结果**：`GET http://localhost:8083/api/route-agent/commands/{command_id}/result`

### endAgent（终端 Agent）

**功能**：执行具体的业务逻辑，响应 routeAgent 广播的命令。

**工作原理**：
1. **注册入网**：向 routeAgent 注册，汇报支持的命令列表
2. **命令执行**：接收并执行 routeAgent 广播的命令
3. **结果返回**：将命令执行结果异步返回给 routeAgent
4. **状态维护**：定期向 routeAgent 汇报自身状态

## 优势

1. **节省 Token**：通过分级加载机制和 web API 调用，减少 Token 消耗
2. **标准化**：完全遵循标准 Skills 规范
3. **高效执行**：利用 HTTP API 直接调用服务，执行速度快
4. **可扩展性**：易于添加新功能和集成新服务
5. **安全可靠**：通过服务端执行，避免在沙盒环境中执行复杂操作
6. **trae-solo IDE 优化**：专为 trae-solo IDE 设计，提供无缝集成体验
7. **多格式支持**：支持 HTML、JavaScript、JSON 等多种输出格式
8. **多主题支持**：支持 Light、Dark、Purple 等多种主题
9. **MCP 架构**：通过 mcpAgent 统一管理 LLM 需求，提高资源利用率
10. **灵活的命令体系**：通过 routeAgent 和 endAgent 的组管理，实现灵活的命令广播和执行

## 限制

1. **网络依赖**：需要网络连接才能调用 API 服务
2. **服务可用性**：依赖 A2UI 服务的可用性
3. **参数大小**：对于大图片等参数，可能受到 HTTP 请求大小限制
4. **执行时间**：对于复杂的 UI 生成，可能需要较长时间

## 故障排除

### 常见问题

1. **服务不可用**
   - 检查 A2UI 服务是否正在运行
   - 检查网络连接是否正常
   - 验证服务 URL 是否正确
   - 尝试访问健康检查端点：`http://localhost:8081/api/a2ui/health`

2. **参数错误**
   - 确保 `image` 参数是可访问的 URL 或有效的 Base64 编码
   - 确保 `uiCode` 参数是有效的 UI 代码

3. **执行超时**
   - 对于复杂的 UI 生成，可能需要较长时间
   - 考虑压缩图片大小

4. **结果格式错误**
   - 检查服务返回的 JSON 格式是否正确
   - 验证脚本是否正确处理响应

5. **trae-solo IDE 集成问题**
   - 确保技能目录结构正确
   - 检查 `skill.yaml` 文件格式是否正确
   - 重启 trae-solo IDE 后重新加载技能

## 版本历史

- **0.4.0**：集成 mcpAgent 和 routeAgent 服务，实现 MCP 架构，统一处理 LLM 需求
- **0.3.0**：实现四分离设计原则，集成 ooder-agent 服务，添加新功能
- **0.2.0**：集成 A2UI 服务，优化 web API 调用，添加 trae-solo IDE 集成支持
- **0.1.0**：初始版本，实现基本功能

## 标准合规性

A2UI Skill 完全按照标准 Skills 方案实现：

1. **分级加载机制**：实现了 1 级元数据、2 级文档、3 级脚本的分级加载
2. **标准化接口**：遵循标准的技能调用接口
3. **Web API 集成**：通过 web API 调用服务，节省 Token
4. **沙盒安全**：脚本在安全的沙盒环境中执行
5. **可移植性**：可在任何支持标准 Skills 规范的平台上使用

## 联系方式

- **作者**：OODER Team
- **邮箱**：team@ooder.ai
- **文档**：https://docs.ooder.ai/skills/a2ui
- **GitHub**：https://github.com/ooderai/ooder-public
