# Agent 协议文档 - v0.7.3

## 1. 协议概述

Agent 协议是 SuperAgent 系统中 MCP Agent、Route Agent 和 End Agent 之间的通信协议，属于南向协议的具体实现。v0.7.3 版本的 Agent 协议在 v0.7.0 基础上进行了全面升级，新增节点发现、本地认证、场景组协作、离线支持和事件总线等核心特性。

### 1.1 协议目标

- 实现 Agent 之间的高效、安全通信
- 支持广域网环境下的 Agent 协同工作
- 提供标准化的 Agent 接口和消息格式
- 确保 Agent 身份的真实性和通信的安全性
- 支持 Agent 动态发现和网络自组织
- 提供灵活的扩展机制，适应不同的应用场景
- **v0.7.3 新增**：支持离线模式和自动同步
- **v0.7.3 新增**：统一事件管理机制

### 1.2 v0.7.3 升级内容

| 新增特性 | 说明 |
|---------|------|
| DiscoveryProtocol | 节点发现协议，支持 UDP/DHT/SkillCenter/mDNS |
| LoginProtocol | 本地认证协议，支持离线认证和会话管理 |
| CollaborationProtocol | 场景组协作协议，支持任务分配和状态同步 |
| OfflineService | 离线服务，支持网络断开时的场景运行 |
| EventBus | 事件总线，统一事件管理和模块解耦 |

### 1.3 协议适用范围

- 广域网内的 Agent 通信
- 跨网络、跨设备的 Agent 协同
- 企业级分布式 Agent 系统
- 个人设备间的安全 Agent 通信
- 跨组织的 Agent 网络协作
- 离线环境下的 Agent 运行

## 2. Agent 架构

### 2.1 Agent 类型

SuperAgent 系统包含三种类型的 Agent：

| Agent 类型 | 描述 | 职责 |
|------------|------|------|
| MCP Agent | 主控智能体 | 资源管理、任务调度、安全认证 |
| Route Agent | 路由智能体 | 消息路由、负载均衡、网络管理 |
| End Agent | 终端智能体 | 与外部设备和系统交互、数据采集和执行 |

### 2.2 通信模式

Agent 之间的通信模式包括：

- **星型通信**：所有 Agent 与 MCP Agent 直接通信
- **链式通信**：Agent 之间通过 Route Agent 进行链式通信
- **网状通信**：Agent 之间直接通信，形成网状网络
- **混合通信**：结合多种通信模式，适应不同场景

### 2.3 v0.7.3 架构增强

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Agent 协议架构 v0.7.3                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      EventBus (事件总线)                             │   │
│  │  - SceneGroupCreatedEvent  - MemberJoinedEvent  - PrimaryChangedEvent│   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      │                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    核心协议层                                        │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │   │
│  │  │Discovery    │ │Login        │ │Collaboration│ │Offline      │   │   │
│  │  │Protocol     │ │Protocol     │ │Protocol     │ │Service      │   │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      │                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    Agent 通信层                                      │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                    │   │
│  │  │ MCP Agent   │ │Route Agent  │ │ End Agent   │                    │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘                    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## 3. 协议格式

### 3.1 消息格式

Agent 协议采用 JSON 格式，确保数据结构的一致性和可解析性：

```json
{
  "protocol_version": "0.7.3",
  "command_id": "uuid",
  "timestamp": "2026-02-20T12:00:00Z",
  "source": {
    "component": "string",
    "id": "string",
    "type": "mcp|route|end"
  },
  "destination": {
    "component": "string",
    "id": "string",
    "type": "mcp|route|end"
  },
  "operation": "string",
  "payload": {},
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number",
    "security_level": "high|medium|low",
    "trace_id": "string",
    "offline_mode": false
  },
  "signature": "digital_signature",
  "token": "session_token"
}
```

### 3.2 字段说明

| 字段 | 类型 | 必选 | 说明 |
|------|------|------|------|
| protocol_version | string | 是 | 协议版本，固定为 "0.7.3" |
| command_id | string | 是 | 命令唯一标识，UUID 格式 |
| timestamp | string | 是 | 命令发送时间，ISO 8601 格式 |
| source | object | 是 | 命令发送方信息 |
| destination | object | 是 | 命令接收方信息 |
| operation | string | 是 | 操作类型 |
| payload | object | 是 | 操作参数 |
| metadata | object | 否 | 元数据信息 |
| metadata.offline_mode | boolean | 否 | **v0.7.3 新增** 离线模式标识 |
| signature | string | 是 | 数字签名 |
| token | string | 是 | 会话令牌 |

## 4. 操作类型

### 4.1 核心操作

| 操作类型 | 功能描述 | 目标组件 |
|----------|----------|----------|
| agent.discover | 发现网络中的 Agent | routeAgent |
| agent.register | 注册 Agent 到网络 | mcpAgent |
| agent.status | 获取 Agent 状态 | any |
| agent.command | 发送命令到 Agent | any |
| agent.heartbeat | 发送心跳消息 | any |
| agent.security.authenticate | Agent 身份认证 | mcpAgent |
| agent.network.join | 加入 Agent 网络 | routeAgent |

### 4.2 v0.7.3 新增操作

| 操作类型 | 功能描述 | 目标组件 |
|----------|----------|----------|
| agent.discovery.udp | UDP 广播发现 | routeAgent |
| agent.discovery.dht | DHT 节点发现 | routeAgent |
| agent.login.local | 本地认证 | mcpAgent |
| agent.login.session | 会话管理 | mcpAgent |
| agent.collaboration.task | 任务分配 | routeAgent |
| agent.collaboration.sync | 状态同步 | routeAgent |
| agent.offline.enable | 启用离线模式 | any |
| agent.offline.sync | 离线数据同步 | any |
| agent.event.subscribe | 事件订阅 | any |
| agent.event.publish | 事件发布 | any |

### 4.3 扩展操作

| 操作类型 | 功能描述 | 目标组件 |
|----------|----------|----------|
| agent.skill.register | 注册技能 | mcpAgent |
| agent.skill.discover | 发现技能 | routeAgent |
| agent.skill.invoke | 调用技能 | endAgent |
| agent.data.transfer | 传输数据 | any |

## 5. 安全机制

### 5.1 身份认证

- **身份生成**：每个 Agent 使用 ECC 算法生成密钥对
- **证书管理**：Agent 可以向信任的 CA 申请身份证书
- **证书验证**：Agent 之间通信时验证对方证书
- **证书链**：支持多级证书链
- **v0.7.3 新增**：本地认证支持，离线环境下可使用本地缓存的认证信息

### 5.2 数据加密

- **传输加密**：使用 TLS 1.3 加密所有 Agent 间通信
- **端到端加密**：对敏感数据使用 AES-256 进行端到端加密
- **密钥管理**：使用 ECDH 算法进行密钥交换

### 5.3 访问控制

- **基于角色的访问控制**：根据 Agent 角色设置不同的访问权限
- **基于策略的访问控制**：根据预定义策略控制资源访问
- **权限审计**：记录所有权限相关的操作

## 6. DiscoveryProtocol（节点发现协议）

### 6.1 发现方法

| 方法 | 适用场景 | 延迟 |
|------|---------|------|
| UDP Broadcast | 局域网 | 低 |
| DHT (Kademlia) | 广域网 | 中 |
| SkillCenter API | 中心化目录 | 低 |
| mDNS/DNS-SD | 服务发现 | 低 |

### 6.2 发现流程

```
1. 启动时执行多路发现
   ├── UDP Broadcast (局域网)
   ├── DHT 查询 (广域网)
   ├── SkillCenter API (中心化)
   └── mDNS/DNS-SD (服务发现)
   
2. 汇总发现结果
   └── 去重、验证、排序
   
3. 建立连接
   └── 按优先级建立 P2P 连接
```

### 6.3 API 接口

```java
public interface DiscoveryProtocol {
    
    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);
    
    CompletableFuture<List<PeerInfo>> discoverByUDP();
    
    CompletableFuture<List<PeerInfo>> discoverByDHT();
    
    CompletableFuture<List<PeerInfo>> discoverBySkillCenter();
}
```

## 7. LoginProtocol（登录协议）

### 7.1 认证模式

| 模式 | 说明 | 适用场景 |
|------|------|---------|
| 在线认证 | 通过 SkillCenter 认证 | 有网络连接 |
| 本地认证 | 使用本地缓存认证 | 离线环境 |
| 域认证 | 通过企业域认证 | 企业环境 |

### 7.2 会话管理

```java
public interface LoginProtocol {
    
    CompletableFuture<Session> login(LoginRequest request);
    
    CompletableFuture<Session> getSession(String userId);
    
    CompletableFuture<Void> logout(String userId);
    
    CompletableFuture<Boolean> validateToken(String token);
}
```

### 7.3 域策略

```yaml
domainPolicy:
  userId: user-001
  scenes:
    - sceneId: auth
      permissions: [read, write]
    - sceneId: messaging
      permissions: [read]
```

## 8. CollaborationProtocol（协作协议）

### 8.1 任务分配

```java
public interface CollaborationProtocol {
    
    CompletableFuture<Void> joinSceneGroup(String groupId, JoinRequest request);
    
    CompletableFuture<TaskInfo> receiveTask(String groupId);
    
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);
    
    CompletableFuture<Void> syncState(String groupId, SceneGroupState state);
}
```

### 8.2 状态同步

| 同步类型 | 触发条件 | 数据内容 |
|---------|---------|---------|
| 全量同步 | 成员加入 | 完整状态 |
| 增量同步 | 状态变更 | 变更部分 |
| 心跳同步 | 定时 | 状态摘要 |

## 9. OfflineService（离线服务）

### 9.1 离线模式

```java
public interface OfflineService {
    
    void enableOfflineMode();
    
    void disableOfflineMode();
    
    boolean isOfflineMode();
    
    CompletableFuture<SyncResult> syncNow();
    
    List<PendingChange> getPendingChanges();
}
```

### 9.2 离线流程

```
网络断开
    │
    ▼
启用离线模式
    │
    ├── 暂存状态变更
    ├── 使用本地缓存数据
    └── 记录操作日志
    │
    ▼
网络恢复
    │
    ▼
自动同步
    │
    ├── 冲突检测
    ├── 冲突解决
    └── 状态合并
```

## 10. EventBus（事件总线）

### 10.1 事件类型

| 事件类型 | 说明 |
|---------|------|
| SceneGroupCreatedEvent | 场景组创建 |
| MemberJoinedEvent | 成员加入 |
| MemberLeftEvent | 成员离开 |
| PrimaryChangedEvent | 主节点变更 |
| TaskAssignedEvent | 任务分配 |
| TaskCompletedEvent | 任务完成 |
| NetworkDisconnectedEvent | 网络断开 |
| NetworkConnectedEvent | 网络连接 |
| OfflineModeEnabledEvent | 离线模式启用 |
| SyncCompletedEvent | 同步完成 |

### 10.2 事件接口

```java
public interface EventBus {
    
    <T> void subscribe(Class<T> eventType, Consumer<T> handler);
    
    <T> void unsubscribe(Class<T> eventType, Consumer<T> handler);
    
    void publish(Object event);
    
    <T> CompletableFuture<Void> publishAsync(T event);
}
```

## 11. 广域网适配

### 11.1 网络适配

- **NAT 穿透**：支持 STUN、TURN 和 ICE 协议
- **防火墙适配**：智能检测防火墙类型
- **网络质量评估**：实时评估网络质量
- **带宽自适应**：根据网络带宽自动调整传输速率

### 11.2 容错机制

- **连接重试**：自动重试失败的连接
- **超时处理**：设置合理的超时时间
- **网络分区**：检测网络分区，自动重连
- **故障转移**：当 Agent 故障时自动转移任务
- **v0.7.3 新增**：离线模式自动切换

## 12. 错误处理

### 12.1 错误码

| 错误码 | 错误描述 | 处理策略 |
|--------|----------|----------|
| 1000 | 参数错误 | 直接返回错误 |
| 1001 | 认证失败 | 引导重新认证 |
| 1002 | 权限不足 | 直接返回错误 |
| 1003 | 资源不存在 | 直接返回错误 |
| 1004 | 请求超时 | 指数退避重试 |
| 1005 | 网络错误 | 尝试其他路径 |
| 2000 | 安全验证失败 | 引导重新认证 |
| 2003 | 广域网连接失败 | 尝试 NAT 穿透 |
| 3001 | 离线模式限制 | 提示用户 |
| 3002 | 同步冲突 | 冲突解决 |
| 3003 | 离线数据过期 | 重新获取 |

## 13. 参考资料

- [SuperAgent 核心协议文档](../main/protocol-main.md)
- [P2P 协议文档](../p2p/p2p-protocol.md)
- [场景组协议文档](./scene-group-protocol.md)
- [技能发现协议文档](../skill/skill-discovery-protocol.md)
