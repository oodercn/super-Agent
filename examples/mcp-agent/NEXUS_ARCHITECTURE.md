# MCP Agent Nexus 架构说明

## 1. 架构概述

MCP (Master Control Plane) Agent 是 Super Agent 系统的核心控制节点，负责管理和协调整个 Agent 网络。基于 Nexus 架构，MCP Agent 提供了统一的控制平面，实现了场景管理、技能协调和节点发现等核心功能。

### 1.1 核心组件

| 组件 | 职责 | 实现文件 |
|------|------|----------|
| McpAgentManager | 核心管理器，负责节点发现和心跳管理 | src/main/java/net/ooder/examples/mcpagent/manager/McpAgentManager.java |
| SceneManagementManager | 场景管理，负责场景的创建、加入和管理 | src/main/java/net/ooder/examples/mcpagent/manager/SceneManagementManager.java |
| 北上协议处理器 | 处理从 Route Agent 上传的请求 | 集成在 McpAgentManager 中 |
| 南下协议处理器 | 向下发送命令到 Route Agent | 集成在 McpAgentManager 中 |
| 场景引擎 | 提供场景管理的核心功能 | 依赖 scene-engine 0.7.3 |

### 1.2 系统架构图

```
+----------------------------------+
|         MCP Agent                |
+----------------------------------+
|                                  |
|  +----------------------------+  |
|  |  McpAgentManager           |  |
|  |  (核心管理器)               |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  SceneManagementManager    |  |
|  |  (场景管理)                 |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  北上协议处理器             |  |
|  +----------------------------+  |
|  |                            |  |
|  +----------------------------+  |
|  |  南下协议处理器             |  |
|  +----------------------------+  |
|                                  |
+----------------------------------+
|              ↑↓                  |
+----------------------------------+
|         Route Agent              |
+----------------------------------+
```

## 2. 核心功能模块

### 2.1 节点发现与管理

- **功能说明**：自动发现网络中的 Route Agent 和 End Agent，并维护节点状态
- **实现原理**：
  - 使用 UDP 广播进行节点发现
  - 基于心跳机制检测节点在线状态
  - 维护节点注册表，包含节点 ID、状态、能力等信息
- **关键流程**：
  1. 启动时监听 UDP 端口（默认 9010）
  2. 接收 Route Agent 的注册请求
  3. 定期发送心跳检测
  4. 处理节点状态变更

### 2.2 场景管理

- **功能说明**：创建、管理和协调场景，支持多 Agent 协同工作
- **核心功能**：
  - 场景创建与配置
  - 场景成员管理（加入/离开）
  - 场景消息分发
  - 场景状态同步
- **实现原理**：
  - 使用 ConcurrentHashMap 存储场景信息
  - 基于场景 ID 进行消息路由
  - 支持场景生命周期管理

### 2.3 命令路由与转发

- **功能说明**：根据目标节点和命令类型，将命令转发到正确的 Route Agent
- **实现原理**：
  - 维护路由表，记录节点与 Route Agent 的映射关系
  - 根据命令目标选择最佳路由
  - 支持命令重试和故障转移

### 2.4 安全管理

- **功能说明**：提供基本的安全认证和授权机制
- **实现原理**：
  - 基于 JWT 进行身份认证
  - 支持节点证书验证
  - 实现基本的访问控制

## 3. 技术实现细节

### 3.1 依赖管理

| 依赖 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.0 | 应用框架 |
| agent-sdk | 0.7.3 | Agent 核心 SDK |
| scene-engine | 0.7.3 | 场景管理引擎 |
| Spring Data JPA | - | 数据持久化 |
| H2 Database | - | 嵌入式数据库 |
| FastJSON | - | JSON 序列化 |

### 3.2 关键类与方法

#### McpAgentManager

| 方法 | 功能 | 参数说明 | 返回值 |
|------|------|----------|--------|
| start() | 启动 MCP Agent | 无 | void |
| registerRouteAgent() | 注册 Route Agent | agentId: String<br>address: String<br>port: int | boolean |
| sendHeartbeat() | 发送心跳检测 | agentId: String | boolean |
| forwardCommand() | 转发命令 | command: Command<br>targetAgentId: String | boolean |

#### SceneManagementManager

| 方法 | 功能 | 参数说明 | 返回值 |
|------|------|----------|--------|
| createScene() | 创建场景 | sceneId: String<br>sceneName: String<br>description: String | boolean |
| joinScene() | 加入场景 | sceneId: String<br>agentId: String<br>memberType: String | boolean |
| leaveScene() | 离开场景 | sceneId: String<br>agentId: String | boolean |
| handleSceneMessage() | 处理场景消息 | sceneId: String<br>message: SceneMessage | boolean |
| updateSceneStatus() | 更新场景状态 | sceneId: String<br>status: String | boolean |

### 3.3 配置说明

#### 核心配置文件

| 配置文件 | 路径 | 用途 |
|----------|------|------|
| application.yml | src/main/resources/application.yml | Spring Boot 应用配置 |
| agent.properties | src/main/resources/agent.properties | Agent 相关配置 |

#### 关键配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| agent.id | Agent 唯一标识 | mcp-agent-001 |
| agent.port | UDP 监听端口 | 9010 |
| agent.http.port | HTTP 服务端口 | 8080 |
| agent.heartbeat.interval | 心跳间隔（毫秒） | 30000 |
| agent.heartbeat.timeout | 心跳超时（毫秒） | 60000 |

## 4. 通信协议

### 4.1 北上协议

**方向**：End Agent → Route Agent → MCP Agent

**消息类型**：
- 注册请求 (REGISTER_REQUEST)
- 心跳请求 (HEARTBEAT_REQUEST)
- 技能调用请求 (SKILL_INVOKE_REQUEST)
- 场景加入请求 (SCENE_JOIN_REQUEST)
- 状态上报 (STATUS_REPORT)

**消息格式**：
```json
{
  "type": "REGISTER_REQUEST",
  "timestamp": 1678901234567,
  "data": {
    "agentId": "route-agent-001",
    "agentType": "ROUTE",
    "address": "192.168.1.100",
    "port": 9002,
    "capabilities": ["ROUTING", "LOAD_BALANCING"]
  }
}
```

### 4.2 南下协议

**方向**：MCP Agent → Route Agent → End Agent

**消息类型**：
- 注册响应 (REGISTER_RESPONSE)
- 心跳响应 (HEARTBEAT_RESPONSE)
- 命令下发 (COMMAND)
- 场景创建 (SCENE_CREATE)
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
# 在 mcp-agent 目录执行
mvn clean package
```

### 5.2 运行方式

```bash
# 直接运行
java -jar target/mcp-agent-1.0.0-SNAPSHOT.jar

# 带参数运行
java -jar target/mcp-agent-1.0.0-SNAPSHOT.jar --agent.id=mcp-agent-001 --agent.port=9010
```

### 5.3 监控与管理

- **HTTP 管理接口**：`http://localhost:8080/console`
- **场景管理页面**：`http://localhost:8080/console/pages/scenario-management.html`
- **日志文件**：`logs/mcp-agent.log`

## 6. 扩展性设计

### 6.1 插件机制

MCP Agent 支持通过插件机制扩展功能：

1. **技能插件**：实现自定义技能
2. **协议插件**：扩展通信协议
3. **存储插件**：使用不同的存储后端

### 6.2 高可用性

- 支持集群部署
- 实现主备切换机制
- 提供负载均衡能力

### 6.3 性能优化

- 使用线程池处理并发请求
- 实现消息队列缓冲
- 优化网络传输效率

## 7. 故障排查

### 7.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 节点注册失败 | 网络连接问题 | 检查网络配置和防火墙设置 |
| 心跳超时 | 网络延迟或节点故障 | 检查节点状态和网络连接 |
| 命令转发失败 | 路由表错误 | 检查路由配置和节点状态 |
| 场景创建失败 | 权限不足 | 检查场景配置和权限设置 |

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

1. **使用安全的通信协议**：建议使用 TLS 加密传输
2. **定期更新密钥**：避免密钥泄露
3. **限制访问权限**：只开放必要的端口和接口
4. **监控异常行为**：及时发现和处理异常情况
5. **定期备份配置**：防止配置丢失

## 10. 未来规划

1. **增强场景管理能力**：支持更复杂的场景定义和管理
2. **添加 AI 调度能力**：基于 AI 优化资源分配和任务调度
3. **扩展协议支持**：支持更多的通信协议和标准
4. **提供更多监控指标**：增强系统可观测性
5. **优化性能**：进一步提升系统性能和稳定性

---

**注意**：本架构说明基于 Super Agent v0.7.3 协议实现，后续版本可能会有变更。