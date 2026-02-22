# Route Agent Nexus 架构说明

## 1. 架构概述

Route Agent 是 Super Agent 系统中的路由节点，负责在 MCP Agent 和 End Agent 之间转发请求和响应。基于 Nexus 架构，Route Agent 提供了高效的消息路由、负载均衡和连接管理功能，是整个 Agent 网络的重要中间层。

### 1.1 核心组件

| 组件 | 职责 | 实现文件 |
|------|------|----------|
| RouteAgentService | 核心服务，负责请求转发和路由管理 | src/main/java/net/ooder/examples/routeagent/service/RouteAgentService.java |
| SceneManagementService | 场景管理，负责场景的加入、离开和消息转发 | src/main/java/net/ooder/examples/routeagent/service/SceneManagementService.java |
| LlmIntegrationService | LLM 集成服务，提供与大语言模型的交互能力 | src/main/java/net/ooder/examples/routeagent/service/LlmIntegrationService.java |
| 北上协议处理器 | 处理从 End Agent 上传的请求 | 集成在 RouteAgentService 中 |
| 南下协议处理器 | 向下发送命令到 End Agent | 集成在 RouteAgentService 中 |
| 负载均衡器 | 提供请求负载均衡功能 | 集成在 RouteAgentService 中 |

### 1.2 系统架构图

```
+----------------------------------+
|         MCP Agent                |
+----------------------------------+
|              ↑↓                  |
+----------------------------------+
|         Route Agent              |
+----------------------------------+
|                                  |
|  +----------------------------+  |
|  |  RouteAgentService         |  |
|  |  (核心路由服务)             |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  SceneManagementService    |  |
|  |  (场景管理)                 |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  LlmIntegrationService     |  |
|  |  (LLM 集成服务)             |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  负载均衡器                 |  |
|  +----------------------------+  |
|                                  |
+----------------------------------+
|              ↑↓                  |
+----------------------------------+
|         End Agent                |
+----------------------------------+
```

## 2. 核心功能模块

### 2.1 路由管理

- **功能说明**：管理与 MCP Agent 和 End Agent 的连接，实现请求和响应的转发
- **实现原理**：
  - 维护与 MCP Agent 的长连接
  - 管理与多个 End Agent 的连接池
  - 实现请求的路由和转发逻辑
- **关键流程**：
  1. 启动时向 MCP Agent 注册
  2. 监听 End Agent 的连接请求
  3. 转发 End Agent 的请求到 MCP Agent
  4. 转发 MCP Agent 的命令到 End Agent

### 2.2 负载均衡

- **功能说明**：在多个 End Agent 之间分配请求，提高系统整体性能和可靠性
- **实现策略**：
  - 轮询 (Round Robin)
  - 随机 (Random)
  - 最少连接 (Least Connection)
  - 权重 (Weighted)
- **关键参数**：
  - 健康检查间隔
  - 超时阈值
  - 重试次数

### 2.3 场景管理

- **功能说明**：管理场景成员关系，转发场景相关消息
- **核心功能**：
  - 处理场景加入请求
  - 处理场景离开请求
  - 转发场景消息到相关 End Agent
  - 维护场景成员列表
- **实现原理**：
  - 使用 ConcurrentHashMap 存储场景信息
  - 基于场景 ID 进行消息路由
  - 支持场景消息的广播和定向发送

### 2.4 LLM 集成

- **功能说明**：提供与大语言模型的集成能力，支持智能路由决策
- **核心功能**：
  - LLM 服务管理
  - 智能路由决策
  - 自然语言处理
  - 对话管理
- **实现原理**：
  - 集成主流 LLM API
  - 实现 LLM 调用的缓存和重试机制
  - 提供 LLM 能力的标准化接口

### 2.5 安全传输

- **功能说明**：确保 Agent 之间通信的安全性
- **实现原理**：
  - 支持 TLS 加密传输
  - 实现证书验证
  - 提供消息签名和验证
  - 支持访问控制

## 3. 技术实现细节

### 3.1 依赖管理

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.0 | 应用框架 |
| Spring Cloud Gateway | - | API 网关 |
| agent-sdk | 0.7.3 | Agent 核心 SDK |
| scene-engine | 0.7.3 | 场景管理引擎 |
| FastJSON | - | JSON 序列化 |
| JWT | - | 身份认证 |

### 3.2 关键类与方法

#### RouteAgentService

| 方法 | 功能 | 参数说明 | 返回值 |
|------|------|----------|--------|
| start() | 启动 Route Agent | 无 | void |
| registerWithMcp() | 向 MCP Agent 注册 | mcpAddress: String<br>mcpPort: int | boolean |
| forwardToMcp() | 转发请求到 MCP Agent | request: Request | Response |
| forwardToEndAgent() | 转发命令到 End Agent | command: Command<br>endAgentId: String | boolean |
| addEndAgent() | 添加 End Agent | endAgentId: String<br>address: String<br>port: int | boolean |
| removeEndAgent() | 移除 End Agent | endAgentId: String | boolean |

#### SceneManagementService

| 方法 | 功能 | 参数说明 | 返回值 |
|------|------|----------|--------|
| joinScene() | 加入场景 | sceneId: String<br>endAgentId: String | boolean |
| leaveScene() | 离开场景 | sceneId: String<br>endAgentId: String | boolean |
| forwardSceneCommand() | 转发场景命令 | sceneId: String<br>command: SceneCommand | boolean |
| getSceneEndAgents() | 获取场景中的 End Agent | sceneId: String | List<String> |
| updateSceneStatus() | 更新场景状态 | sceneId: String<br>status: String | boolean |

#### LlmIntegrationService

| 方法 | 功能 | 参数说明 | 返回值 |
|------|------|----------|--------|
| init() | 初始化 LLM 服务 | 无 | void |
| getRouteDecision() | 获取路由决策 | request: Request | RouteDecision |
| processNaturalLanguage() | 处理自然语言请求 | request: NlpRequest | NlpResponse |
| manageConversation() | 管理对话上下文 | conversationId: String<br>message: String | ConversationResponse |

### 3.3 配置说明

#### 核心配置文件

| 配置文件 | 路径 | 用途 |
|----------|------|------|
| application.yml | src/main/resources/application.yml | Spring Boot 应用配置 |
| agent.properties | src/main/resources/agent.properties | Agent 相关配置 |

#### 关键配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| agent.id | Agent 唯一标识 | route-agent-001 |
| agent.port | UDP 监听端口 | 9002 |
| agent.http.port | HTTP 服务端口 | 8081 |
| agent.mcp.address | MCP Agent 地址 | localhost |
| agent.mcp.port | MCP Agent 端口 | 9010 |
| agent.heartbeat.interval | 心跳间隔（毫秒） | 30000 |
| agent.load.balancing.strategy | 负载均衡策略 | round-robin |

## 4. 通信协议

### 4.1 与 MCP Agent 通信

**消息类型**：
- 注册请求 (REGISTER_REQUEST)
- 心跳请求 (HEARTBEAT_REQUEST)
- 命令转发 (COMMAND_FORWARD)
- 状态上报 (STATUS_REPORT)

**消息格式**：
```json
{
  "type": "COMMAND_FORWARD",
  "timestamp": 1678901234567,
  "data": {
    "requestId": "req-001",
    "sourceAgentId": "end-agent-001",
    "targetAgentId": "mcp-agent-001",
    "action": "INVOKE_SKILL",
    "parameters": {
      "skillId": "skill-001",
      "args": {}
    }
  }
}
```

### 4.2 与 End Agent 通信

**消息类型**：
- 注册响应 (REGISTER_RESPONSE)
- 心跳响应 (HEARTBEAT_RESPONSE)
- 命令下发 (COMMAND)
- 场景消息 (SCENE_MESSAGE)
- 配置更新 (CONFIG_UPDATE)

**消息格式**：
```json
{
  "type": "COMMAND",
  "timestamp": 1678901234567,
  "data": {
    "commandId": "cmd-001",
    "targetAgentId": "end-agent-001",
    "action": "INVOKE_SKILL",
    "parameters": {
      "skillId": "skill-001",
      "args": {}
    }
  }
}
```

## 5. 部署与运行

### 5.1 构建步骤

```bash
# 在 route-agent 目录执行
mvn clean package
```

### 5.2 运行方式

```bash
# 直接运行
java -jar target/route-agent-1.0.0-SNAPSHOT.jar

# 带参数运行
java -jar target/route-agent-1.0.0-SNAPSHOT.jar --agent.id=route-agent-001 --agent.mcp.address=192.168.1.100
```

### 5.3 监控与管理

- **HTTP 管理接口**：`http://localhost:8081/console`
- **场景管理页面**：`http://localhost:8081/console/pages/scenario-management.html`
- **日志文件**：`logs/route-agent.log`

## 6. 扩展性设计

### 6.1 插件机制

Route Agent 支持通过插件机制扩展功能：

1. **路由策略插件**：实现自定义路由算法
2. **负载均衡插件**：实现自定义负载均衡策略
3. **协议插件**：扩展通信协议
4. **安全插件**：实现自定义安全机制

### 6.2 高可用性

- 支持集群部署
- 实现主备切换机制
- 提供健康检查和故障转移

### 6.3 性能优化

- 使用 Netty 处理高性能网络通信
- 实现连接池管理
- 使用缓存减少重复计算
- 优化消息序列化和反序列化

## 7. 故障排查

### 7.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 连接 MCP Agent 失败 | 网络问题或 MCP Agent 未运行 | 检查网络连接和 MCP Agent 状态 |
| 转发请求失败 | End Agent 离线或网络问题 | 检查 End Agent 状态和网络连接 |
| 负载均衡失效 | 配置错误或节点状态异常 | 检查负载均衡配置和节点状态 |
| 场景消息转发失败 | 场景不存在或成员列表错误 | 检查场景配置和成员状态 |

### 7.2 日志分析

- **INFO**：正常运行信息
- **WARN**：警告信息，需要关注
- **ERROR**：错误信息，需要立即处理
- **DEBUG**：调试信息，用于问题定位

## 8. 版本兼容性

| 版本 | 兼容的 Agent SDK 版本 | 兼容的 Scene Engine 版本 |
|------|----------------------|--------------------------|
| v0.7.3 | 0.7.3 | 0.7.3 |
| v0.7.0 | 0.7.0 | 0.7.0 |

## 9. 安全最佳实践

1. **使用 TLS 加密**：确保所有 Agent 之间的通信都使用 TLS 加密
2. **证书管理**：定期更新和轮换证书
3. **访问控制**：实现基于角色的访问控制
4. **审计日志**：记录所有关键操作的审计日志
5. **漏洞扫描**：定期进行安全漏洞扫描

## 10. 未来规划

1. **增强智能路由能力**：基于 AI 实现更智能的路由决策
2. **支持更多协议**：扩展支持 gRPC、WebSocket 等协议
3. **优化负载均衡**：实现更复杂的负载均衡算法
4. **增强监控能力**：提供更详细的系统监控指标
5. **支持边缘计算**：优化在边缘设备上的运行性能

---

**注意**：本架构说明基于 Super Agent v0.7.3 协议实现，后续版本可能会有变更。