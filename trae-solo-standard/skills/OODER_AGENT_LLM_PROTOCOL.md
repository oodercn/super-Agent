# ooderAgent LLM 通讯协议

## 1. 协议概述

ooderAgent LLM 通讯协议是一套基于 MCP (Model Control Plane) 架构的通讯规范，旨在实现大模型与 ooderAgent 之间的高效交互，以及 ooderAgent 内部组件（routeAgent、endAgent）之间的协同工作。

### 1.1 设计目标

- **统一管理**：通过 mcpAgent 统一处理 LLM 需求，提高资源利用率
- **灵活扩展**：支持动态添加和移除 endAgent，组成员不固定
- **可靠性**：实现错误处理和重试机制，确保 LLM 请求的可靠性
- **异步处理**：支持命令的异步执行和结果查询，提高系统响应速度
- **节省 Token**：通过分级加载机制和按需请求，减少 Token 消耗

### 1.2 核心组件

| 组件 | 角色 | 功能描述 |
|------|------|----------|
| mcpAgent | AIBridge | 作为 aibridge 统一处理 LLM 需求，汇总 routeAgent 的请求，统一与 LLM 完成交互 |
| routeAgent | Scene Core | 作为场景核心，通过内部建组的方式与 endAgent 完成交互，管理命令的广播和执行结果的收集 |
| endAgent | Executor | 执行具体的业务逻辑，响应 routeAgent 广播的命令 |
| Skills | Bridge | 作为技能桥接，连接用户需求和 ooderAgent 功能 |

## 2. 技术原理

### 2.1 MCP 架构

MCP (Model Control Plane) 架构是 ooderAgent LLM 通讯协议的核心技术基础，它将大模型的请求处理和管理逻辑分离出来，形成一个专门的控制平面。

#### 2.1.1 分层设计

```
+---------------------+
|     用户/IDE        |
+---------------------+
          |
+---------------------+
|     Skills          |
+---------------------+
          |
+---------------------+
|     mcpAgent        |
|    (AIBridge)       |
+---------------------+
          |
+---------------------+
|    routeAgent       |
|   (Scene Core)      |
+---------------------+
          |
+---------------------+
|    endAgent Pool    |
|   (Executors)       |
+---------------------+
```

#### 2.1.2 核心流程

1. **请求发起**：用户通过 Skills 发起 LLM 请求
2. **请求处理**：mcpAgent 接收并处理 LLM 请求，汇总需求
3. **命令分发**：routeAgent 根据场景将请求转换为命令，广播给 endAgent
4. **命令执行**：endAgent 执行命令并返回结果
5. **结果汇总**：routeAgent 汇总执行结果
6. **结果返回**：mcpAgent 将结果返回给 Skills 和用户

### 2.2 分级加载机制

采用 3 级加载机制，实现按需加载，节省 Token：

1. **1 级**：元数据（skill.yaml），轻量级加载
2. **2 级**：说明文档（SKILL.md），按需加载
3. **3 级及以上**：脚本和资源文件，按需加载执行

### 2.3 异步通讯模式

采用异步通讯模式，提高系统响应速度：

1. **命令发送**：routeAgent 广播命令后立即返回命令 ID
2. **异步执行**：endAgent 异步执行命令
3. **结果查询**：通过命令 ID 异步查询执行结果
4. **状态通知**：支持执行状态的实时通知

## 3. 协议内容

### 3.1 通讯格式

所有通讯均采用 JSON 格式，确保数据结构的一致性和可解析性。

### 3.1.1 南下协议格式（v0.6.3）

南下协议是指从 ooderAgent 向底层服务或组件发送的指令协议，采用统一的 JSON 格式：

```json
{
  "protocol_version": "0.6.3",
  "command_id": "uuid",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "string",
    "id": "string"
  },
  "destination": {
    "component": "string",
    "id": "string"
  },
  "operation": "string",
  "payload": {
    // 操作参数
  },
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number"
  }
}
```

#### 字段说明

| 字段 | 类型 | 必选 | 说明 |
|------|------|------|------|
| protocol_version | string | 是 | 协议版本，固定为 "0.6.3" |
| command_id | string | 是 | 命令唯一标识，UUID 格式 |
| timestamp | string | 是 | 命令发送时间，ISO 8601 格式 |
| source | object | 是 | 命令发送方信息 |
| source.component | string | 是 | 发送方组件类型（mcpAgent/routeAgent/endAgent） |
| source.id | string | 是 | 发送方组件 ID |
| destination | object | 是 | 命令接收方信息 |
| destination.component | string | 是 | 接收方组件类型 |
| destination.id | string | 是 | 接收方组件 ID |
| operation | string | 是 | 操作类型 |
| payload | object | 是 | 操作参数 |
| metadata | object | 否 | 元数据信息 |
| metadata.priority | string | 否 | 优先级，默认为 "medium" |
| metadata.timeout | number | 否 | 超时时间（秒），默认为 30 |
| metadata.retry_count | number | 否 | 重试次数，默认为 3 |

### 3.1.3 统一请求格式

所有组件间的请求均采用南下协议v0.6.3格式：

```json
{
  "protocol_version": "0.6.3",
  "command_id": "uuid",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "string",
    "id": "string"
  },
  "destination": {
    "component": "string",
    "id": "string"
  },
  "operation": "string",
  "payload": {
    // 具体操作参数
  },
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number"
  }
}
```

### 3.1.4 统一响应格式

所有组件间的响应均采用标准格式：

```json
{
  "protocol_version": "0.6.3",
  "command_id": "uuid",
  "correlation_id": "request_command_id",
  "timestamp": "2026-01-23T12:00:00Z",
  "status": "success|error",
  "data": {
    // 具体响应数据
  },
  "error": {
    "code": "error_code",
    "message": "error_message"
  },
  "metadata": {
    // 元数据
  }
}
```

### 3.2 组件间通讯协议

#### 3.2.1 Skills ↔ mcpAgent 通讯

##### Skills → mcpAgent

| 操作 | 路径 | 方法 | 请求数据 | 响应数据 |
|------|------|------|----------|----------|
| 执行 LLM 查询 | /api/mcp-agent/llm/query | POST | `{"skill_id": "string", "query": "string", "parameters": "object"}` | `{"result": "string", "tokens_used": "number"}` |
| 执行命令 | /api/mcp-agent/execute | POST | `{"function": "string", "parameters": "object"}` | `{"result": "object"}` |
| 获取状态 | /api/mcp-agent/status | GET | N/A | `{"status": "string", "uptime": "number"}` |
| 健康检查 | /api/mcp-agent/health | GET | N/A | `{"status": "healthy"}` |
| 查找 Agent | /api/mcp-agent/agent/find | POST | `{"skill_id": "string"}` | `{"agent_id": "string"}` |

##### mcpAgent → Skills

| 事件 | 数据结构 | 描述 |
|------|----------|------|
| LLM 查询完成 | `{"query_id": "string", "result": "string", "tokens_used": "number"}` | LLM 查询执行完成 |
| 命令执行完成 | `{"command_id": "string", "result": "object", "execution_time": "number"}` | 命令执行完成 |
| 错误通知 | `{"error_code": "string", "error_message": "string", "request_id": "string"}` | 执行过程中发生错误 |

#### 3.2.2 mcpAgent ↔ routeAgent 通讯

##### mcpAgent → routeAgent

| 操作 | 路径 | 方法 | 请求数据 | 响应数据 |
|------|------|------|----------|----------|
| 注册 endAgent | /api/route-agent/register | POST | `{"agent_id": "string", "commands": "array", "endpoint": "string"}` | `{"status": "registered", "agent_id": "string"}` |
| 广播命令 | /api/route-agent/broadcast | POST | `{"group_id": "string", "command": "string", "parameters": "object"}` | `{"command_id": "string", "status": "broadcasted"}` |
| 执行命令 | /api/route-agent/execute | POST | `{"agent_id": "string", "command": "string", "parameters": "object"}` | `{"command_id": "string", "status": "executing"}` |
| 获取组内成员 | /api/route-agent/groups/{group_id}/members | GET | N/A | `{"members": "array"}` |
| 查询命令结果 | /api/route-agent/commands/{command_id}/result | GET | N/A | `{"status": "completed", "result": "object"}` |
| 获取状态 | /api/route-agent/status | GET | N/A | `{"status": "string", "uptime": "number"}` |
| 健康检查 | /api/route-agent/health | GET | N/A | `{"status": "healthy"}` |

##### routeAgent → mcpAgent

| 事件 | 数据结构 | 描述 |
|------|----------|------|
| 命令执行完成 | `{"command_id": "string", "result": "object", "execution_time": "number"}` | 命令执行完成 |
| 成员状态变更 | `{"group_id": "string", "member_id": "string", "status": "online|offline"}` | 成员状态发生变更 |
| 错误通知 | `{"error_code": "string", "error_message": "string", "request_id": "string"}` | 执行过程中发生错误 |

#### 3.2.3 routeAgent ↔ endAgent 通讯

##### routeAgent → endAgent

| 操作 | 路径 | 方法 | 请求数据 | 响应数据 |
|------|------|------|----------|----------|
| 发送命令 | /api/end-agent/execute | POST | `{"command": "string", "parameters": "object", "command_id": "string"}` | `{"status": "accepted|rejected", "reason": "string"}` |
| 心跳检测 | /api/end-agent/health | GET | N/A | `{"status": "healthy", "agent_id": "string"}` |

##### endAgent → routeAgent

| 操作 | 路径 | 方法 | 请求数据 | 响应数据 |
|------|------|------|----------|----------|
| 注册 | /api/route-agent/register | POST | `{"agent_id": "string", "commands": "array", "endpoint": "string"}` | `{"status": "registered", "group_id": "string"}` |
| 上报状态 | /api/route-agent/status | POST | `{"agent_id": "string", "status": "online", "resources": "object"}` | `{"status": "updated"}` |
| 返回结果 | /api/route-agent/result | POST | `{"command_id": "string", "status": "completed|failed", "result": "object", "error": "object"}` | `{"status": "received"}` |

### 3.3 命令体系

#### 3.3.1 命令格式

```json
{
  "command_id": "uuid",
  "name": "command_name",
  "parameters": {
    // 命令参数
  },
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number"
  }
}
```

#### 3.3.2 核心命令列表

| 命令 | 描述 | 适用组件 | 参数 |
|------|------|----------|------|
| get-components | 获取组件列表 | A2UI | N/A |
| generate-ui | 生成 UI 代码 | A2UI | `{"image_url": "string", "options": "object"}` |
| get-component-details | 获取组件详细信息 | A2UI | `{"component_id": "string"}` |
| create-view | 创建视图 | A2UI | `{"view_name": "string", "components": "array", "layout": "object"}` |
| submit-data | 提交数据 | A2UI, SKILL B | `{"target": "string", "data": "object", "options": "object"}` |
| execute-llm-query | 执行 LLM 查询 | mcpAgent | `{"skill_id": "string", "query": "string", "parameters": "object"}` |
| register-end-agent | 注册 endAgent | routeAgent | `{"agent_id": "string", "commands": "array", "endpoint": "string"}` |
| broadcast-command | 广播命令 | routeAgent | `{"group_id": "string", "command": "string", "parameters": "object"}` |

### 3.4 状态管理

#### 3.4.1 组件状态

| 组件 | 状态 | 描述 |
|------|------|------|
| mcpAgent | healthy, degraded, unhealthy | 服务健康状态 |
| routeAgent | healthy, degraded, unhealthy | 服务健康状态 |
| endAgent | online, offline, busy, error | 代理运行状态 |

#### 3.4.2 命令状态

| 状态 | 描述 | 后续状态 |
|------|------|----------|
| pending | 命令已创建，等待执行 | executing |
| broadcasting | 命令正在广播中 | broadcasted |
| broadcasted | 命令已广播，等待响应 | executing |
| executing | 命令正在执行中 | completed, failed |
| completed | 命令执行成功 | N/A |
| failed | 命令执行失败 | retry, cancelled |
| cancelled | 命令已取消 | N/A |
| timeout | 命令执行超时 | retry, failed |

#### 3.4.3 Scene 版本管理

| 版本状态 | 描述 | 操作权限 |
|----------|------|----------|
| draft | 草稿版本，正在编辑中 | 可编辑，不可执行 |
| published | 已发布版本，可执行 | 不可编辑，可执行 |
| archived | 已归档版本，历史版本 | 只读，不可执行 |

#### 3.4.4 VFS 事务状态

| 事务状态 | 描述 | 操作 |
|----------|------|------|
| started | 事务已开始，数据准备中 | 继续写入数据 |
| in_progress | 事务进行中，数据写入中 | 继续写入数据 |
| committed | 事务已提交，数据完整写入 | 完成事务 |
| rolled_back | 事务已回滚，数据未写入 | 清理临时数据 |

### 3.5 错误处理

#### 3.5.1 错误码

| 错误码 | 描述 | 重试策略 |
|--------|------|----------|
| 1001 | 服务不可用 | 指数退避重试 |
| 1002 | 参数错误 | 不重试，返回错误 |
| 1003 | 执行超时 | 有限重试 |
| 1004 | 权限不足 | 不重试，返回错误 |
| 1005 | 网络错误 | 指数退避重试 |
| 1006 | 资源不足 | 延迟重试 |
| 1007 | 命令不支持 | 不重试，返回错误 |
| 1008 | 内部错误 | 有限重试 |

#### 3.5.2 重试机制

- **指数退避**：重试间隔按指数增长（1s, 2s, 4s, 8s...）
- **最大重试次数**：默认为 3 次，可配置
- **超时时间**：默认为 30s，可配置
- **失败处理**：超过最大重试次数后，返回失败结果

## 4. 执行流程

### 4.1 LLM 执行流程

1. **查询 mcpAgent 状态**：确保 mcpAgent 服务正常运行
2. **根据技能查找 mcpAgent id**：确定要使用的技能对应的 agent
3. **构建 LLM 查询请求**：根据用户需求构建查询请求
4. **发送查询请求**：通过 mcpAgent 发送查询请求
5. **等待查询结果**：支持同步等待和异步查询
6. **处理查询结果**：将结果返回给用户

#### 4.1.1 执行示例

```bash
# 1. 查询 mcpAgent 状态
bash mcp-agent.sh get-status

# 2. 执行 LLM 查询
bash mcp-agent.sh execute-llm-query "a2ui" "如何优化表单组件" '{"context": "A2UI 四分离设计原则"}'

# 3. 处理查询结果
# 同步方式：直接获取结果
# 异步方式：通过命令 ID 查询结果
bash mcp-agent.sh query-result "cmd-123"
```

### 4.2 命令执行流程

1. **构建命令**：根据用户需求构建命令
2. **查询在线成员**：获取组内在线的 endAgent
3. **广播命令**：向在线成员广播命令
4. **处理响应**：处理 endAgent 的响应（接受或拒绝）
5. **执行命令**：endAgent 执行命令
6. **返回结果**：endAgent 异步返回执行结果
7. **汇总结果**：routeAgent 汇总执行结果
8. **返回结果**：将结果返回给用户

#### 4.2.1 执行示例

```bash
# 1. 广播命令到组内成员
bash mcp-agent.sh broadcast-command "group-1" "get-components" "{}"

# 2. 向指定 endAgent 发送命令
bash mcp-agent.sh send-command "agent-1" "generate-ui" '{"image_url": "https://example.com/ui.png"}'

# 3. 查询命令执行结果
bash mcp-agent.sh query-result "cmd-123"
```

### 4.3 技能调用流程

1. **用户发起请求**：用户通过自然语言发起技能调用请求
2. **技能匹配**：Skills 系统匹配最适合的技能
3. **构建参数**：根据用户请求构建技能参数
4. **调用技能**：通过 mcpAgent 调用技能
5. **执行流程**：执行 LLM 查询或命令执行流程
6. **结果处理**：处理执行结果，转换为用户友好的格式
7. **返回结果**：将结果返回给用户

#### 4.3.1 执行示例

```bash
# 通过 Skills 调用 A2UI 技能生成 UI 代码
bash skills/a2ui/scripts/generate-ui.sh --image "https://example.com/login.png" --format html --theme light

# 通过 mcpAgent 调用技能
bash skills/a2ui/scripts/mcp-agent.sh send-command "a2ui-agent" "generate-ui" '{"image_url": "https://example.com/login.png", "options": {"format": "html", "theme": "light"}}'
```

## 5. 安全规范

### 5.1 认证与授权

- **API 认证**：所有 API 调用必须携带有效的认证令牌
- **权限控制**：基于角色的权限控制，限制不同组件的操作权限
- **访问控制**：限制 API 访问来源，只允许授权的组件访问

### 5.2 数据安全

- **数据加密**：所有通讯数据采用 HTTPS 加密传输
- **敏感信息保护**：敏感信息（如 API 密钥）不得明文传输
- **数据验证**：所有输入数据必须经过验证，防止注入攻击

### 5.3 安全审计

- **日志记录**：所有操作必须记录详细的审计日志
- **操作追踪**：追踪每个操作的发起者、执行过程和结果
- **异常检测**：实时检测异常操作，防止安全漏洞

## 6. 性能优化

### 6.1 缓存策略

- **LLM 响应缓存**：缓存频繁使用的 LLM 响应
- **组件状态缓存**：缓存 endAgent 的状态信息
- **命令结果缓存**：缓存重复执行的命令结果
- **Scene 缓存**：缓存 Scene 配置和状态，提高加载速度

### 6.2 负载均衡

- **动态负载**：根据 endAgent 的资源使用情况分配任务
- **健康检查**：定期检查 endAgent 的健康状态
- **故障转移**：当 endAgent 故障时，自动转移任务到健康的 endAgent

### 6.3 资源管理

- **资源限制**：限制每个 endAgent 的资源使用
- **资源监控**：实时监控系统资源使用情况
- **资源回收**：及时回收闲置资源

## 7. VFS 高级特性

### 7.1 VFS 事务约束

#### 7.1.1 事务管理

- **事务概念**：VFS 支持简单的事务管理，确保数据的完整性
- **事务边界**：以命令执行为单位，一个命令对应一个事务
- **原子性**：确保数据要么完整写入，要么完全不写入
- **一致性**：确保数据从一个一致状态转换到另一个一致状态
- **隔离性**：事务之间相互隔离，避免数据干扰
- **持久性**：事务提交后，数据持久化存储

#### 7.1.2 事务处理流程

1. **事务开始**：命令执行开始时，创建事务
2. **数据准备**：将数据写入临时存储区域
3. **数据验证**：验证数据的完整性和合法性
4. **事务提交**：验证通过后，将数据提交到正式存储
5. **事务回滚**：验证失败时，回滚事务，清理临时数据

#### 7.1.3 事务约束

- **数据完整性**：确保写入 VFS 的数据是完整的，避免部分写入
- **并发控制**：处理多个进程同时写入的情况，避免数据冲突
- **错误处理**：当写入失败时，自动回滚事务
- **日志记录**：记录所有事务操作，支持故障恢复

### 7.2 全局地址列表

#### 7.2.1 地址管理

- **全局地址列表**：建立和维护 VFS 地址的全局列表
- **地址格式**：采用统一的地址格式，包含协议、主机、端口和路径
- **地址分类**：
  - 任务地址区域：存储任务相关的数据
  - 结果地址区域：存储命令执行结果
  - 配置地址区域：存储系统配置
  - 日志地址区域：存储操作日志

#### 7.2.2 地址映射

- **命令 ID 映射**：将命令 ID 映射到对应的结果数据地址
- **endAgent 映射**：将 endAgent ID 映射到其负责的数据区域
- **Scene 映射**：将 Scene ID 映射到其配置和状态数据地址

#### 7.2.3 故障恢复

- **地址解析**：当 endAgent 离线、忙或故障时，通过命令 ID 解析获取结果数据地址
- **数据访问**：直接访问结果数据地址，获取执行结果
- **负载均衡**：当 endAgent 不可用时，自动选择其他可用的 endAgent 处理请求
- **容错机制**：当数据地址不可用时，从备份地址获取数据

## 8. Scene 版本管理

### 8.1 版本控制

- **版本标识**：使用语义化版本号（如 1.0.0）标识 Scene 版本
- **版本存储**：每个版本的 Scene 配置存储在 VFS 的不同路径
- **版本历史**：记录 Scene 的版本历史，支持版本回溯
- **版本比较**：提供版本间的差异比较功能

### 8.2 版本操作

- **创建版本**：基于现有版本创建新的版本
- **编辑版本**：编辑草稿版本的 Scene 配置
- **发布版本**：将草稿版本发布为可执行版本
- **归档版本**：将旧版本归档，保留历史记录
- **回滚版本**：回滚到之前的版本

### 8.3 版本兼容性

- **向前兼容**：新版本兼容旧版本的配置格式
- **向后兼容**：旧版本能够识别新版本的基本结构
- **版本迁移**：提供工具支持版本间的配置迁移
- **兼容性检查**：在版本切换时，检查兼容性问题


## 9. 部署与集成

### 9.1 部署架构

#### 9.1.1 本地部署

```
+---------------------+
|     trae-solo IDE   |
+---------------------+
          |
+---------------------+
|     Skills 目录     |
+---------------------+
          |
+---------------------+
|  ooderAgent 服务    |
| +-----------------+ |
| |   mcpAgent      | |
| |   routeAgent    | |
| |   endAgent      | |
| +-----------------+ |
+---------------------+
```

#### 9.1.2 分布式部署

```
+---------------------+
|     trae-solo IDE   |
+---------------------+
          |
+---------------------+
|     Skills 目录     |
+---------------------+
          |
+---------------------+
|  Load Balancer      |
+---------------------+
          |
+---------------------+
|  ooderAgent Cluster |
| +-----------------+ |
| |   mcpAgent      | |
| |   routeAgent    | |
| |   endAgent Pool | |
| +-----------------+ |
+---------------------+
```

### 9.2 集成方式

#### 9.2.1 与 trae-solo IDE 集成

1. **技能安装**：将技能目录复制到 trae-solo IDE 的 skills 目录
2. **技能加载**：IDE 自动扫描并加载技能
3. **技能调用**：通过自然语言或命令行调用技能

#### 9.2.2 与第三方系统集成

1. **API 集成**：通过 RESTful API 与第三方系统集成
2. **Webhook 集成**：通过 Webhook 接收事件通知
3. **SDK 集成**：使用官方 SDK 与系统集成

## 10. 版本兼容性

### 10.1 版本管理

- **协议版本**：v0.6.3（南下协议统一版本）
- **向后兼容**：确保新版本兼容旧版本的协议
- **版本协商**：支持组件间的版本协商

### 10.2 兼容性矩阵

| 组件 | 最低版本 | 推荐版本 | 说明 |
|------|----------|----------|------|
| mcpAgent | 0.2.0 | 1.0.0 | 支持所有核心功能 |
| routeAgent | 0.1.0 | 1.0.0 | 支持命令广播和结果收集 |
| endAgent | 0.1.0 | 1.0.0 | 支持命令执行和状态上报 |
| Skills | 0.3.0 | 1.0.0 | 支持技能桥接和用户交互 |
| 南下协议 | 0.6.0 | 0.6.3 | 统一南下协议版本 |

## 11. 最佳实践

### 11.1 组件设计

- **职责单一**：每个组件只负责一项核心功能
- **松耦合**：组件间通过 API 通讯，减少直接依赖
- **可测试**：组件设计应便于单元测试和集成测试
- **可监控**：实现详细的监控和日志记录

### 11.2 性能优化

- **批量处理**：批量处理 LLM 请求，减少 API 调用次数
- **异步处理**：对于耗时操作，采用异步处理方式
- **缓存策略**：合理使用缓存，减少重复计算
- **资源限制**：设置合理的资源限制，防止资源耗尽

### 11.3 错误处理

- **优雅降级**：当部分服务不可用时，实现优雅降级
- **错误重试**：对于可重试的错误，实现自动重试机制
- **错误监控**：实时监控错误率，及时发现问题
- **错误通知**：重要错误及时通知相关人员

### 11.4 安全管理

- **最小权限**：遵循最小权限原则，只授予必要的权限
- **安全审计**：定期进行安全审计，发现并修复安全漏洞
- **加密传输**：所有通讯采用加密传输
- **访问控制**：限制 API 访问来源

## 12. 未来展望

### 12.1 功能扩展

- **多模型支持**：支持多种 LLM 模型的同时使用
- **智能路由**：根据请求类型和内容智能选择最佳的 endAgent
- **自动扩展**：根据负载自动扩展 endAgent 数量
- **学习能力**：通过机器学习优化命令执行和 LLM 查询

### 12.2 技术演进

- **标准化**：推动协议的标准化，促进生态系统的发展
- **开源社区**：建立开源社区，共同完善协议和实现
- **云原生**：支持云原生部署，实现弹性伸缩
- **边缘计算**：支持边缘计算场景，减少延迟

### 12.3 应用场景

- **智能开发**：辅助开发者进行代码生成、代码优化等
- **内容创作**：辅助内容创作者进行文案生成、创意设计等
- **数据分析**：辅助分析师进行数据处理、可视化等
- **智能客服**：提供智能客服服务，处理用户咨询

## 13. 附录

### 13.1 术语表

| 术语 | 解释 |
|------|------|
| MCP | Model Control Plane，模型控制平面 |
| LLM | Large Language Model，大语言模型 |
| routeAgent | 路由代理，作为场景核心管理命令分发 |
| endAgent | 终端代理，执行具体的业务逻辑 |
| mcpAgent | 模型控制平面代理，统一处理 LLM 需求 |
| Skills | 技能，连接用户需求和 ooderAgent 功能 |
| Token | 大语言模型的计费单位 |
| 四分离设计 | A2UI 的设计原则，分离属性、样式、事件、行为 |
| 南下协议 | ooderAgent 向底层服务或组件发送指令的统一协议 |
| 北上协议 | 底层服务或组件向 ooderAgent 返回结果的统一协议 |
| VFS | Virtual File System，虚拟文件系统 |
| Scene | 场景，ooderAgent 中的业务场景定义 |
| A2UI | AI to UI，从图片生成 UI 代码的技术 |
| ooderAgent | 基于 MCP 架构的智能代理系统 |
| 分级加载 | 按需加载技能资源的机制，分为元数据、文档、脚本等层级 |
| 命令体系 | ooderAgent 内部的命令格式和执行机制 |
| 事务约束 | VFS 中确保数据完整性的机制 |
| 全局地址列表 | VFS 中管理数据地址的全局索引 |

### 13.2 参考资源

- [ooderAgent 架构设计文档](https://docs.ooder.ai/architecture)
- [A2UI 技术文档](https://docs.ooder.ai/a2ui)
- [Skills 开发指南](https://docs.ooder.ai/skills)
- [trae-solo IDE 使用手册](https://docs.ooder.ai/trae-solo)

### 13.3 联系方式

- **官方网站**：https://ooder.ai
- **文档中心**：https://docs.ooder.ai
- **GitHub**：https://github.com/ooderai
- **社区论坛**：https://forum.ooder.ai
- **技术支持**：support@ooder.ai
