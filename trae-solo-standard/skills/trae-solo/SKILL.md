# Trae Solo Skill

## 技能概述

Trae Solo 是一个标准的 Skills 实现，基于博文中的架构原理，专为 trae-solo IDE 设计，通过 web API 调用 ooder-agent 服务，实现实用功能的执行和 A2UI 图转代码技术的集成。

## 使用场景

- **日常实用功能**：执行基本的问候、计算、时间获取等操作
- **A2UI 图转代码**：将 UI 设计图片转换为前端代码，支持多种格式和主题
- **Web 服务集成**：通过 HTTP API 与 ooder-agent 和 A2UI 服务集成
- **节省 Token**：利用分级加载机制和 web 调用，减少 Token 消耗
- **trae-solo IDE 集成**：专为 trae-solo IDE 优化，提供无缝的技能调用体验

## 工作原理

Trae Solo 采用博文中描述的三层分级加载机制：

1. **1 级：元数据** - `skill.yaml` 文件，包含技能的基本信息，轻量级加载
2. **2 级：说明文档** - 本文件 (`SKILL.md`)，包含详细的使用说明，按需加载
3. **3 级及以上** - 脚本和资源文件，按需加载执行

技能执行流程：
1. trae-solo IDE 读取 `skill.yaml` 了解技能基本信息
2. 当用户请求匹配技能描述时，IDE 读取本文件获取详细说明
3. IDE 根据需要执行 `scripts/` 目录中的脚本
4. 脚本通过 HTTP API 调用 ooder-agent 和 A2UI 服务
5. 将执行结果返回给 IDE，由 IDE 展示给用户

## 使用方式

### 在 trae-solo IDE 中使用

1. **安装技能**：将 `trae-solo` 目录复制到 trae-solo IDE 的 skills 目录中
2. **调用技能**：在 IDE 中直接输入技能调用指令，例如：
   ```
   使用 Trae Solo 技能执行 hello 功能，参数为 {"name": "World"}
   ```

### 命令行使用

```bash
# 执行实用功能
bash scripts/execute.sh --function <功能名称> --params <参数JSON> --displayMode <展示模式>

# 使用A2UI图转代码功能
bash scripts/a2ui-generate.sh --image <图片URL> --format <输出格式> --theme <主题>

# 获取技能信息
bash scripts/info.sh

# 使用 MCP 功能
bash scripts/mcp.sh --action <操作> [--function <功能名称>] [--params <参数JSON>] [--displayMode <展示模式>]
```

### MCP 功能使用

MCP (Model Control Plane) 功能提供了更高级的模型交互能力：

```bash
# 执行 MCP 调用
bash scripts/mcp.sh --action call --function hello --params '{"name": "World"}' --displayMode a2ui

# 检查 MCP 状态
bash scripts/mcp.sh --action status

# 检查健康状态
bash scripts/mcp.sh --action health

# 清理缓存
bash scripts/mcp.sh --action clear-cache

# 列出可用函数
bash scripts/mcp.sh --action list-functions
```

### 功能列表

| 功能名称 | 描述 | 参数 |
|---------|------|------|
| hello | 简单的问候功能 | name: 问候对象名称 |
| calculate | 基本计算功能 | a: 第一个操作数, b: 第二个操作数, operation: 操作类型(add/subtract/multiply/divide) |
| datetime | 获取当前日期时间 | 无 |
| info | 获取技能信息 | 无 |
| a2ui-generate | A2UI图转代码 | image: 图片URL, format: 输出格式(html/js/json), theme: 主题(light/dark/purple) |

## 使用步骤

### 步骤 1：准备环境

确保以下服务正在运行：
- ooder-agent 服务：`http://localhost:9010/api/v1`
- A2UI 服务：`http://localhost:8081/api/a2ui`

### 步骤 2：在 trae-solo IDE 中配置

1. 打开 trae-solo IDE
2. 进入技能管理界面
3. 点击 "添加技能"，选择 `trae-solo` 目录
4. 确认技能加载成功

### 步骤 3：执行功能

1. **执行 hello 功能**：
   ```bash
   bash scripts/execute.sh --function hello --params '{"name": "World"}' --displayMode a2ui
   ```

2. **执行计算功能**：
   ```bash
   bash scripts/execute.sh --function calculate --params '{"a": 10, "b": 5, "operation": "add"}' --displayMode a2ui
   ```

3. **使用 A2UI 功能**：
   ```bash
   bash scripts/a2ui-generate.sh --image "https://example.com/login-design.png" --format html --theme light
   ```

### 步骤 4：处理结果

脚本执行后，会返回 JSON 格式的结果，包含：
- `success`：执行是否成功
- `data`：执行结果数据
- `metadata`：执行元数据（如执行时间、使用的功能等）

在 trae-solo IDE 中，结果会以友好的方式展示给用户。

## 注意事项

1. **服务依赖**：确保 ooder-agent 和 A2UI 服务正在运行，并且可以通过指定的 URL 访问

2. **网络连接**：技能执行需要网络连接，确保网络正常

3. **参数格式**：
   - `params` 参数必须是有效的 JSON 格式
   - `image` 参数必须是可访问的 URL 或有效的 Base64 编码

4. **性能考虑**：
   - 对于复杂的 A2UI 转换，可能需要较长时间
   - 建议对大图片进行适当压缩后再使用

5. **错误处理**：
   - 如果服务不可用，脚本会返回错误信息
   - 如果参数格式错误，脚本会返回详细的错误提示
   - 在 trae-solo IDE 中，错误信息会以友好的方式展示

## 脚本说明

### 脚本列表

- **execute.sh**：执行实用功能
- **a2ui-generate.sh**：使用 A2UI 图转代码功能
- **info.sh**：获取技能信息
- **mcp.sh**：MCP (Model Control Plane) 功能，处理大型模型到 ooder-agent 的交互
- **common.sh**：通用工具函数，包含缓存、重试机制和 MCP 核心功能

### 脚本工作原理

1. 解析命令行参数
2. 构建 HTTP 请求
3. 调用相应的 API 端点
4. 处理响应结果
5. 格式化并返回结果

## API 端点

### ooder-agent 服务

- **执行功能**：`POST http://localhost:9010/api/v1/execute`
- **获取信息**：`GET http://localhost:9010/api/v1/info`
- **健康检查**：`GET http://localhost:9010/api/v1/health`

### A2UI 服务

- **生成 UI 代码**：`POST http://localhost:8081/api/a2ui/generate-ui`
- **预览 UI 界面**：`POST http://localhost:8081/api/a2ui/preview-ui`
- **获取组件列表**：`GET http://localhost:8081/api/a2ui/components`
- **健康检查**：`GET http://localhost:8081/api/a2ui/health`

## 示例

### 示例 1：执行问候功能

**命令**：
```bash
bash scripts/execute.sh --function hello --params '{"name": "Alice"}' --displayMode a2ui
```

**预期输出**：
```json
{
  "success": true,
  "data": {
    "message": "Hello, Alice!",
    "function": "hello",
    "timestamp": 1678901234567
  },
  "metadata": {
    "function": "hello",
    "displayMode": "a2ui",
    "executionTime": 123,
    "skillId": "trae-solo"
  }
}
```

### 示例 2：使用 A2UI 功能

**命令**：
```bash
bash scripts/a2ui-generate.sh --image "https://example.com/dashboard.png" --format html --theme dark
```

**预期输出**：
```json
{
  "success": true,
  "data": {
    "function": "a2ui-generate",
    "image": "https://example.com/dashboard.png",
    "format": "html",
    "theme": "dark",
    "a2uiResult": {
      "success": true,
      "format": "html",
      "code": "<div class=\"dashboard-container theme-dark\">...</div>",
      "components": ["Button", "Input", "Panel", "Label"]
    }
  },
  "metadata": {
    "function": "a2ui-generate",
    "executionTime": 4567,
    "skillId": "trae-solo"
  }
}
```

### 示例 3：在 trae-solo IDE 中使用

**输入**：
```
使用 Trae Solo 技能执行 calculate 功能，计算 10 加 5
```

**预期输出**：
```
执行结果：15
执行时间：123ms
```

## 优势

1. **节省 Token**：通过分级加载机制和 web API 调用，减少 Token 消耗
2. **标准化**：完全遵循博文中的 Skills 标准规范
3. **高效执行**：利用 HTTP API 直接调用服务，执行速度快
4. **可扩展性**：易于添加新功能和集成新服务
5. **安全可靠**：通过服务端执行，避免在沙盒环境中执行复杂操作
6. **trae-solo IDE 优化**：专为 trae-solo IDE 设计，提供无缝集成体验
7. **多格式支持**：A2UI 功能支持 HTML、JavaScript、JSON 等多种输出格式
8. **多主题支持**：A2UI 功能支持 Light、Dark、Purple 等多种主题
9. **MCP 功能**：提供高级的模型控制平面，优化大型模型到 ooder-agent 的交互
10. **智能缓存**：实现响应缓存，减少重复请求，提高执行效率
11. **自动重试**：内置请求重试机制，提高服务调用的可靠性
12. **健康监控**：提供 MCP 状态和健康检查功能，确保系统稳定运行

## 限制

1. **网络依赖**：需要网络连接才能调用 API 服务
2. **服务可用性**：依赖 ooder-agent 和 A2UI 服务的可用性
3. **参数大小**：对于大图片等参数，可能受到 HTTP 请求大小限制
4. **执行时间**：对于复杂的 A2UI 转换，可能需要较长时间

## 故障排除

### 常见问题

1. **服务不可用**
   - 检查服务是否正在运行
   - 检查网络连接是否正常
   - 验证服务 URL 是否正确
   - 尝试访问健康检查端点：`http://localhost:9010/api/v1/health`

2. **参数错误**
   - 确保 `params` 参数是有效的 JSON 格式
   - 确保 `image` 参数是可访问的 URL

3. **执行超时**
   - 对于复杂的 A2UI 转换，可能需要更长时间
   - 考虑压缩图片大小

4. **结果格式错误**
   - 检查服务返回的 JSON 格式是否正确
   - 验证脚本是否正确处理响应

5. **trae-solo IDE 集成问题**
   - 确保技能目录结构正确
   - 检查 `skill.yaml` 文件格式是否正确
   - 重启 trae-solo IDE 后重新加载技能

## 版本历史

- **0.3.0**：更新 VFS SDK 和 A2UI 功能，集成 ES6 模块支持，优化构建系统，添加完整的文档套件
- **0.2.0**：集成 A2UI 图转代码功能，优化 web API 调用，添加 trae-solo IDE 集成支持
- **0.1.0**：初始版本，实现基本实用功能

## 最新更新内容

### VFS (Virtual File System) 更新

- **增强的文件同步机制**：优化了 VFS 与本地文件系统的同步流程，提高了文件传输速度和可靠性
- **完整的文件管理功能**：支持文件上传、下载、删除、重命名等操作
- **文件监控**：实时监控文件变化，自动触发同步
- **错误处理**：改进了错误处理机制，提供更详细的错误信息

### SDK 更新

- **ES6 模块支持**：集成了完整的 ES6 模块系统，支持现代 JavaScript 语法
- **Tree Shaking 优化**：针对现代打包工具（Webpack、Rollup）进行了优化，减少了打包体积
- **TypeScript 类型定义**：提供了完整的 TypeScript 类型定义，提升开发体验
- **向后兼容性**：保持与遗留 `ood.Class` 系统的完全兼容，支持渐进式迁移
- **构建系统优化**：使用 Webpack 4 构建，提供开发和生产版本

### A2UI (AI to UI) 更新

- **增强的图转代码能力**：优化了 AI 模型，提高了代码生成的准确性和质量
- **多格式支持**：支持 HTML、JavaScript、JSON 等多种输出格式
- **多主题支持**：内置 Light、Dark、Purple 三种主题，可根据需求自定义
- **组件库扩展**：增加了更多 UI 组件，包括 Button、Input、Dialog、TreeGrid、Tabs 等 60+ 组件
- **响应式设计**：生成的代码支持响应式布局，适配不同设备尺寸

### 文档更新

- **完整的开源文档套件**：添加了全面的 API 参考、使用指南和示例
- **AI 辅助学习框架**：提供了 AI 辅助学习的详细文档
- **贡献指南**：添加了详细的贡献指南和行为准则
- **测试指南**：提供了完整的测试套件和测试指南
- **架构文档**：添加了系统架构图和设计说明

### 系统优化

- **性能优化**：提升了 API 调用速度和响应时间
- **安全性增强**：改进了错误处理和异常捕获机制
- **可维护性**：优化了代码结构，提高了可维护性
- **部署便捷性**：简化了部署流程，提供了详细的部署指南

## 标准合规性

Trae Solo 技能完全按照博文中的标准 Skills 方案实现：

1. **分级加载机制**：实现了 1 级元数据、2 级文档、3 级脚本的分级加载
2. **标准化接口**：遵循标准的技能调用接口
3. **Web API 集成**：通过 web API 调用服务，节省 Token
4. **沙盒安全**：脚本在安全的沙盒环境中执行
5. **可移植性**：可在任何支持标准 Skills 规范的平台上使用

## 联系方式

- **作者**：OODER Team
- **邮箱**：team@ooder.ai
- **文档**：https://docs.ooder.ai/skills/trae-solo
- **GitHub**：https://github.com/ooderai/ooder-public
