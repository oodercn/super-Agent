# SuperAgent P2P 网络协议文档 - v0.7.3

## 1. 协议概述

SuperAgent P2P 网络协议是 v0.7.3 版本的核心协议，用于实现 SuperAgent 节点在广域网环境下的安全通信和技能共享。该协议基于去中心化设计理念，支持节点自动发现、技能共享、数据传输和协同工作。

v0.7.3 版本在 v0.7.0 基础上进行了全面升级，新增了 DiscoveryProtocol、LoginProtocol、CollaborationProtocol、OfflineService 和 EventBus 等核心特性。

### 1.1 协议目标

- 实现 SuperAgent 节点在广域网环境下的去中心化通信
- 提供强安全认证机制，确保节点身份和数据传输的安全性
- 支持跨网络、跨设备的技能安全共享和调用
- 提供节点自动发现和网络自组织能力
- 确保网络的可靠性、可扩展性和安全性
- **v0.7.3 新增**：支持离线模式和自动同步
- **v0.7.3 新增**：统一事件管理机制

### 1.2 v0.7.3 升级内容

| 新增特性 | 说明 |
|---------|------|
| DiscoveryProtocol | 多路节点发现协议（UDP/DHT/SkillCenter/mDNS） |
| LoginProtocol | 本地认证协议，支持离线认证 |
| CollaborationProtocol | 场景组协作协议，支持任务分配 |
| OfflineService | 离线服务，支持网络断开时继续运行 |
| EventBus | 事件总线，统一事件管理 |

### 1.3 协议适用范围

- 广域网内的 SuperAgent 节点通信
- 跨网络、跨设备的技能共享和调用
- 企业级分布式 SuperAgent 网络
- 个人设备间的安全数据同步和协同
- 离线环境下的节点通信

## 2. 网络架构

### 2.1 网络拓扑

SuperAgent P2P 网络采用自组织的无中心拓扑结构：

- **动态拓扑**：节点可以随时加入或离开网络
- **分层发现**：新节点通过引导节点和 DHT 自动发现其他节点
- **智能路由**：基于距离向量和链路状态的混合路由算法
- **负载均衡**：任务和请求根据节点能力分布式处理
- **故障恢复**：当节点故障时，网络自动重新路由请求

### 2.2 节点角色

| 角色 | 描述 | 职责 |
|------|------|------|
| 普通节点 | 基本的 SuperAgent 实例 | 提供和使用技能，参与网络通信 |
| 技能提供者 | 提供特定技能的节点 | 注册和共享技能，响应技能调用请求 |
| 技能使用者 | 调用其他节点技能的节点 | 发现和调用远程技能 |
| 引导节点 | 长期在线的稳定节点 | 帮助新节点加入网络 |
| 安全节点 | 提供安全认证服务的节点 | 验证节点身份，签发临时认证令牌 |

### 2.3 v0.7.3 架构增强

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        P2P 网络架构 v0.7.3                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      EventBus (事件总线)                             │   │
│  │  - NodeDiscoveredEvent  - NodeJoinedEvent  - NodeLeftEvent          │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      │                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    协议层                                            │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │   │
│  │  │Discovery    │ │Login        │ │Collaboration│ │Offline      │   │   │
│  │  │Protocol     │ │Protocol     │ │Protocol     │ │Service      │   │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                      │                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    P2P 网络层                                        │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                    │   │
│  │  │ DHT         │ │ NAT穿透     │ │ 路由算法    │                    │   │
│  │  │ (Kademlia)  │ │ (ICE/STUN)  │ │ (混合)      │                    │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘                    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.4 广域网适配

- **NAT 穿透**：支持 STUN、TURN 和 ICE 协议
- **防火墙适配**：智能检测防火墙类型
- **网络质量评估**：实时评估网络质量
- **带宽自适应**：根据网络带宽自动调整传输速率
- **延迟优化**：使用地理感知路由

## 3. DiscoveryProtocol（节点发现协议）

### 3.1 发现方法

| 方法 | 适用场景 | 延迟 | v0.7.3 增强 |
|------|---------|------|------------|
| UDP Broadcast | 局域网 | 低 | 支持多播组 |
| DHT (Kademlia) | 广域网 | 中 | 优化路由表 |
| SkillCenter API | 中心化目录 | 低 | 支持缓存 |
| mDNS/DNS-SD | 服务发现 | 低 | 新增支持 |

### 3.2 发现流程

```
节点启动
    │
    ├── 1. UDP Broadcast (局域网)
    │       └── 广播 DISCOVERY_REQUEST
    │
    ├── 2. DHT 查询 (广域网)
    │       └── 查询 Kademlia 路由表
    │
    ├── 3. SkillCenter API (中心化)
    │       └── 查询节点目录
    │
    └── 4. mDNS/DNS-SD (服务发现)
            └── 发布/发现服务
    │
    ▼
汇总发现结果
    │
    ├── 去重
    ├── 验证
    └── 排序
    │
    ▼
建立连接
    │
    └── 按优先级建立 P2P 连接
```

### 3.3 发现接口

```java
public interface DiscoveryProtocol {
    
    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);
    
    CompletableFuture<List<PeerInfo>> discoverByUDP();
    
    CompletableFuture<List<PeerInfo>> discoverByDHT();
    
    CompletableFuture<List<PeerInfo>> discoverBySkillCenter();
    
    CompletableFuture<List<PeerInfo>> discoverByMDNS();
}
```

## 4. LoginProtocol（登录协议）

### 4.1 认证模式

| 模式 | 说明 | 适用场景 |
|------|------|---------|
| 在线认证 | 通过 SkillCenter 认证 | 有网络连接 |
| 本地认证 | 使用本地缓存认证 | 离线环境 |
| 域认证 | 通过企业域认证 | 企业环境 |

### 4.2 认证接口

```java
public interface LoginProtocol {
    
    CompletableFuture<Session> login(LoginRequest request);
    
    CompletableFuture<Session> getSession(String userId);
    
    CompletableFuture<Void> logout(String userId);
    
    CompletableFuture<Boolean> validateToken(String token);
    
    CompletableFuture<Token> refreshToken(String refreshToken);
}
```

### 4.3 会话数据结构

```java
public class Session {
    
    private String sessionId;
    private String userId;
    private Token accessToken;
    private Token refreshToken;
    private long createdAt;
    private long expiresAt;
    private Map<String, Object> attributes;
    private boolean offline;
}
```

## 5. CollaborationProtocol（协作协议）

### 5.1 协作接口

```java
public interface CollaborationProtocol {
    
    CompletableFuture<Void> joinSceneGroup(String groupId, JoinRequest request);
    
    CompletableFuture<Void> leaveSceneGroup(String groupId);
    
    CompletableFuture<TaskInfo> receiveTask(String groupId);
    
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);
    
    CompletableFuture<Void> syncState(String groupId, SceneGroupState state);
}
```

### 5.2 任务协作流程

```
PRIMARY                           BACKUP
   │                                │
   │  1. 任务入队                    │
   │                                │
   │  2. receiveTask()  ────────────▶│
   │                                │
   │                                │  3. 执行任务
   │                                │
   │  4. submitTaskResult() ◀───────│
   │                                │
   │  5. 结果验证                    │
   │                                │
   │  6. 发布 TaskCompletedEvent    │
```

## 6. OfflineService（离线服务）

### 6.1 离线接口

```java
public interface OfflineService {
    
    void enableOfflineMode();
    
    void disableOfflineMode();
    
    boolean isOfflineMode();
    
    CompletableFuture<SyncResult> syncNow();
    
    List<PendingChange> getPendingChanges();
    
    OfflineStatus getOfflineStatus();
}
```

### 6.2 离线流程

```
网络断开检测
    │
    ▼
启用离线模式
    │
    ├── 使用本地缓存
    ├── 暂存变更
    └── 发布 OfflineModeEnabledEvent
    │
    ▼
网络恢复检测
    │
    ▼
自动同步
    │
    ├── 冲突检测
    ├── 冲突解决
    ├── 状态合并
    └── 发布 SyncCompletedEvent
```

## 7. EventBus（事件总线）

### 7.1 事件类型

| 事件类型 | 说明 |
|---------|------|
| NodeDiscoveredEvent | 节点发现 |
| NodeJoinedEvent | 节点加入 |
| NodeLeftEvent | 节点离开 |
| SkillRegisteredEvent | 技能注册 |
| SkillInvokedEvent | 技能调用 |
| NetworkDisconnectedEvent | 网络断开 |
| NetworkConnectedEvent | 网络连接 |
| OfflineModeEnabledEvent | 离线模式启用 |
| SyncCompletedEvent | 同步完成 |

### 7.2 事件接口

```java
public interface EventBus {
    
    <T> void subscribe(Class<T> eventType, Consumer<T> handler);
    
    <T> void unsubscribe(Class<T> eventType, Consumer<T> handler);
    
    void publish(Object event);
    
    <T> CompletableFuture<Void> publishAsync(T event);
}
```

## 8. 安全认证机制

### 8.1 节点身份认证

#### 8.1.1 身份标识

- **节点 ID**：每个节点拥有唯一的加密身份标识，基于 ECC 生成
- **公钥证书**：节点使用 X.509 格式的公钥证书
- **证书链**：支持多级证书链

#### 8.1.2 认证流程

1. **初始认证**：新节点加入网络时，通过引导节点进行初始身份验证
2. **挑战-响应**：使用基于 ECDSA 的挑战-响应机制
3. **证书验证**：验证节点证书的有效性和完整性
4. **信任建立**：建立节点间的双向信任关系
5. **临时令牌**：颁发短期有效的会话令牌

### 8.2 数据传输安全

- **传输加密**：节点间通信使用 TLS 1.3 加密
- **端到端加密**：敏感数据使用端到端加密
- **数据完整性**：使用 SHA-256 哈希算法
- **防重放攻击**：使用时间戳和随机数
- **流量混淆**：支持流量混淆

### 8.3 访问控制

- **基于角色的访问控制**：根据节点角色设置不同的访问权限
- **技能访问控制**：技能提供者可以设置技能的访问权限
- **数据访问控制**：用户可以控制数据的共享范围
- **节点白名单**：支持设置信任节点白名单

## 9. 通信机制

### 9.1 消息格式

```json
{
  "type": "message_type",
  "version": "0.7.3",
  "timestamp": "2026-02-20T12:00:00Z",
  "sender": "node_id",
  "receiver": "node_id",
  "payload": {},
  "metadata": {
    "security_level": "high",
    "priority": "medium",
    "ttl": 64,
    "offline_mode": false
  },
  "signature": "digital_signature",
  "token": "session_token"
}
```

### 9.2 消息类型

| 消息类型 | 描述 | 方向 | 安全级别 |
|----------|------|------|----------|
| DISCOVERY_REQUEST | 节点发现请求 | 单播/广播 | 中 |
| DISCOVERY_RESPONSE | 节点发现响应 | 单播 | 中 |
| HEARTBEAT | 心跳消息 | 广播 | 低 |
| SKILL_REGISTER | 技能注册 | 广播 | 高 |
| SKILL_DISCOVER | 技能发现请求 | 广播 | 中 |
| SKILL_INVOKE | 技能调用请求 | 单播 | 高 |
| SKILL_RESPONSE | 技能调用响应 | 单播 | 高 |
| AUTH_CHALLENGE | 认证挑战 | 单播 | 高 |
| AUTH_RESPONSE | 认证响应 | 单播 | 高 |
| OFFLINE_SYNC | 离线同步 | 单播 | 高 |

## 10. 技能共享机制

### 10.1 技能注册

1. 节点准备技能元数据
2. 生成技能的数字签名
3. 对技能代码进行加密
4. 向网络广播 SKILL_REGISTER 消息
5. 其他节点接收并验证技能信息

### 10.2 技能发现

- **被动发现**：接收其他节点的技能注册消息
- **主动发现**：广播 SKILL_DISCOVER 消息
- **技能目录**：维护本地技能目录
- **分布式索引**：使用 DHT 实现技能的分布式索引

### 10.3 技能调用

1. 调用方节点在本地技能目录中查找目标技能
2. 找到提供该技能的节点，发送 SKILL_INVOKE 消息
3. 提供方节点验证令牌和参数，执行技能
4. 提供方节点发送 SKILL_RESPONSE 消息
5. 调用方节点验证签名，处理执行结果

## 11. 配置参数

| 配置项 | 描述 | 默认值 |
|--------|------|--------|
| p2p.enabled | 是否启用 P2P 网络 | true |
| p2p.port | P2P 网络端口 | 7777 |
| p2p.discoveryInterval | 发现间隔（秒） | 30 |
| p2p.heartbeatInterval | 心跳间隔（秒） | 10 |
| p2p.maxConnections | 最大连接数 | 100 |
| p2p.bootstrapNodes | 引导节点列表 | [] |
| p2p.securityEnabled | 是否启用安全机制 | true |
| p2p.securityLevel | 安全级别 | medium |
| p2p.useDHT | 是否使用 DHT | true |
| p2p.natTraversal | 是否启用 NAT 穿透 | true |
| p2p.offlineMode | 是否启用离线模式 | true |

## 12. 错误处理

### 12.1 网络错误

- **连接失败**：自动重试，尝试连接其他节点
- **超时处理**：设置合理的超时时间
- **网络分区**：检测网络分区，自动重连
- **NAT 穿透失败**：尝试多种 NAT 穿透方法

### 12.2 安全错误

- **认证失败**：返回明确的认证错误
- **授权失败**：返回权限错误
- **签名验证失败**：拒绝处理消息
- **令牌过期**：自动重新获取令牌

### 12.3 离线错误

| 错误码 | 描述 | 处理策略 |
|--------|------|---------|
| 3001 | 离线模式限制 | 提示用户 |
| 3002 | 同步冲突 | 冲突解决 |
| 3003 | 离线数据过期 | 重新获取 |

## 13. 参考资料

- [SuperAgent 核心协议文档](../main/protocol-main.md)
- [Agent 协议文档](../agent/agent-protocol.md)
- [Kademlia: A Peer-to-Peer Information System](https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf)
- [TLS 1.3 Protocol](https://tools.ietf.org/html/rfc8446)
