# Ooder Agent SDK 0.7.2 升级指南

## 1. 升级概览

### 1.1 版本特性

Ooder Agent SDK 0.7.2 版本带来了全新的南北向分层架构，为开发者提供了更清晰、更灵活、更强大的Agent开发框架。

| 特性 | 描述 |
|------|------|
| **南北向分层架构** | 核心抽象层、南向服务层、北向服务层三层分离 |
| **用户-组织-域模型** | 支持多租户、多组织、多域的复杂业务场景 |
| **增强场景组** | 自组网、LLM介入、离线运行、多点分支 |
| **增强北向协议** | 命令增强、异步处理、状态追踪、重试机制 |
| **ooder-common深度集成** | VFS、组织管理、消息服务、集群管理、MCP服务 |

### 1.2 升级影响

| 影响范围 | 影响程度 | 说明 |
|----------|----------|------|
| **架构变更** | 🔴 高 | 新增南北向分层架构 |
| **API变更** | 🟡 中 | 新增接口，保持向后兼容 |
| **配置变更** | 🟡 中 | 新增配置项，旧配置兼容 |
| **依赖变更** | 🟢 低 | 新增ooder-common依赖 |

## 2. 架构变更

### 2.1 南北向分离

0.7.2 版本采用南北向分层架构：

```
┌─────────────────────────────────────────────────────────────────┐
│                        应用层（Application）                      │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      北向服务层（Northbound）                     │
│  - UDP/P2P/Gossip  - 域级安全  - 技能分享  - 增强协议            │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      南向服务层（Southbound）                     │
│  - HTTP/确定性网络  - 基础认证  - 增强场景组  - LLM介入          │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      核心抽象层（Core）                          │
│  - 连接/协议/传输  - 身份/权限/加密  - 消息/状态/事件            │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 核心抽象层

新增核心抽象层，提供统一的基础接口：

| 抽象模块 | 核心接口 | 说明 |
|----------|----------|------|
| **网络抽象** | CoreConnection, CoreProtocol, CoreTransport | 统一的网络连接和协议接口 |
| **安全抽象** | CoreIdentity, CorePermission, CoreEncryption | 统一的身份和权限接口 |
| **协作抽象** | CoreMessage, CoreState, CoreEvent | 统一的消息和事件接口 |

### 2.3 服务层重组

| 原模块 | 新位置 | 说明 |
|--------|--------|------|
| **UDPSDK** | 北向网络服务 | UDP通信移至北向 |
| **SecurityManager** | 南向安全服务 | 基础安全保留南向 |
| **SceneGroupManager** | 南向协作服务 | 场景组增强后保留南向 |
| **ProtocolHub** | 北向协议中心 | 协议管理移至北向 |

## 3. 升级步骤

### 3.1 依赖更新

在 `pom.xml` 中更新依赖：

```xml
<properties>
    <ooder.version>2.1</ooder.version>
</properties>

<dependencies>
    <dependency>
        <groupId>net.ooder</groupId>
        <artifactId>agent-sdk</artifactId>
        <version>0.7.2</version>
    </dependency>
    
    <dependency>
        <groupId>net.ooder</groupId>
        <artifactId>ooder-common-client</artifactId>
        <version>${ooder.version}</version>
    </dependency>
    
    <dependency>
        <groupId>net.ooder</groupId>
        <artifactId>ooder-vfs-web</artifactId>
        <version>${ooder.version}</version>
    </dependency>
    
    <dependency>
        <groupId>net.ooder</groupId>
        <artifactId>ooder-server</artifactId>
        <version>${ooder.version}</version>
    </dependency>
</dependencies>
```

### 3.2 配置迁移

#### 3.2.1 新增配置文件

创建 `application-northbound.properties` 和 `application-southbound.properties`：

```properties
# application-southbound.properties
ooder.sdk.south.network.http-timeout=30000
ooder.sdk.south.network.http-max-connections=100
ooder.sdk.south.network.mcp-endpoint-port=7070

ooder.sdk.south.security.jwt-secret=your-secret-key
ooder.sdk.south.security.jwt-expiration=86400000

ooder.sdk.south.collaboration.auto-join-enabled=true
ooder.sdk.south.collaboration.llm-provider=openai
ooder.sdk.south.collaboration.offline-mode-enabled=true
```

```properties
# application-northbound.properties
ooder.sdk.north.network.udp-port=9001
ooder.sdk.north.network.p2p-enabled=true
ooder.sdk.north.network.gossip-fanout=3

ooder.sdk.north.security.domain-key-algorithm=AES-256
ooder.sdk.north.security.p2p-encryption-enabled=true

ooder.sdk.north.protocol.retry-max-attempts=3
ooder.sdk.north.protocol.timeout-default=30000
```

#### 3.2.2 配置兼容性

| 0.6.6 配置 | 0.7.2 配置 | 兼容性 |
|------------|------------|--------|
| `ooder.sdk.network.*` | `ooder.sdk.south.network.*` | ✅ 兼容 |
| `ooder.sdk.security.*` | `ooder.sdk.south.security.*` | ✅ 兼容 |
| `ooder.sdk.monitoring.*` | `ooder.sdk.south.monitoring.*` | ✅ 兼容 |

### 3.3 代码适配

#### 3.3.1 AgentFactory 更新

```java
import net.ooder.sdk.agent.factory.AgentFactory;
import net.ooder.sdk.config.SDKConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

ApplicationContext context = new AnnotationConfigApplicationContext(SDKConfiguration.class);
AgentFactory.setApplicationContext(context);

EndAgent endAgent = AgentFactory.createEndAgent("agent-1", "Test Agent", capabilities);
```

#### 3.3.2 使用南向服务

```java
import net.ooder.sdk.service.south.SouthNetworkService;
import net.ooder.sdk.service.south.SouthSecurityService;
import net.ooder.sdk.service.south.SouthCollaborationService;

@Autowired
private SouthNetworkService southNetworkService;

@Autowired
private SouthSecurityService southSecurityService;

@Autowired
private SouthCollaborationService southCollaborationService;

public void example() {
    HttpResponse response = southNetworkService.sendHttpRequest(request).join();
    
    AuthenticationResult authResult = southSecurityService.authenticate("admin", "password").join();
    
    southCollaborationService.autoJoinNetwork().join();
}
```

#### 3.3.3 使用北向服务

```java
import net.ooder.sdk.service.north.NorthNetworkService;
import net.ooder.sdk.service.north.NorthSecurityService;
import net.ooder.sdk.service.north.NorthCollaborationService;

@Autowired
private NorthNetworkService northNetworkService;

@Autowired
private NorthSecurityService northSecurityService;

@Autowired
private NorthCollaborationService northCollaborationService;

public void example() {
    UdpResult result = northNetworkService.sendUdpMessage("target:9001", data).join();
    
    boolean hasPermission = northSecurityService.checkDomainPermission(userId, domainId, permission).join();
    
    ShareResult shareResult = northCollaborationService.shareSkill(skillId, targetDomain).join();
}
```

#### 3.3.4 使用增强协议

```java
import net.ooder.sdk.protocol.EnhancedProtocolHub;
import net.ooder.sdk.protocol.EnhancedCommandPacket;

@Autowired
private EnhancedProtocolHub protocolHub;

public void example() {
    EnhancedCommandPacket packet = EnhancedCommandPacket.builder()
        .protocolType("MCP_AGENT")
        .commandType("SHARE_SKILL")
        .priority(CommandPriority.HIGH)
        .retryPolicy(new RetryPolicy(3, 1000, 2.0))
        .timeoutPolicy(new TimeoutPolicy(30000, TimeoutAction.RETRY))
        .payload(Map.of("skillId", "skill-001"))
        .build();
    
    CommandResult result = protocolHub.handleEnhancedCommand(packet);
    
    CommandTrace trace = protocolHub.traceCommand(packet.getPacketId());
}
```

## 4. 兼容性说明

### 4.1 API兼容性

| API | 0.6.6 | 0.7.2 | 兼容性 | 说明 |
|-----|-------|-------|--------|------|
| `AgentFactory.createEndAgent()` | ✅ | ✅ | 完全兼容 | 无需修改 |
| `AgentFactory.createRouteAgent()` | ✅ | ✅ | 完全兼容 | 无需修改 |
| `AgentFactory.createMcpAgent()` | ✅ | ✅ | 完全兼容 | 无需修改 |
| `UDPSDK.send()` | ✅ | ✅ | 完全兼容 | 移至北向服务 |
| `SecurityManager.authenticate()` | ✅ | ✅ | 完全兼容 | 移至南向服务 |
| `SceneGroupManager.create()` | ✅ | ⚠️ | 部分兼容 | 增强为南向协作服务 |
| `ProtocolHub.handleCommand()` | ✅ | ✅ | 扩展兼容 | 增强为北向协议中心 |

### 4.2 配置兼容性

| 配置项 | 0.6.6 | 0.7.2 | 兼容性 |
|--------|-------|-------|--------|
| `ooder.sdk.network.broadcast-address` | ✅ | ✅ | 完全兼容 |
| `ooder.sdk.network.default-port` | ✅ | ✅ | 完全兼容 |
| `ooder.sdk.security.jwt.secret` | ✅ | ✅ | 完全兼容 |
| `ooder.sdk.monitoring.enabled` | ✅ | ✅ | 完全兼容 |

### 4.3 数据兼容性

| 数据类型 | 0.6.6 | 0.7.2 | 兼容性 |
|----------|-------|-------|--------|
| Agent配置 | ✅ | ✅ | 完全兼容 |
| 网络拓扑 | ✅ | ✅ | 完全兼容 |
| 终端设备 | ✅ | ✅ | 完全兼容 |
| 路由信息 | ✅ | ✅ | 完全兼容 |

## 5. 迁移示例

### 5.1 网络模块迁移

#### 5.1.1 UDP通信迁移

**0.6.6 版本**：
```java
UDPSDK udp = new UDPSDK(9001);
udp.send("target:9002", data);
```

**0.7.2 版本**：
```java
@Autowired
private NorthNetworkService northNetworkService;

UdpResult result = northNetworkService.sendUdpMessage("target:9002", data).join();
```

#### 5.1.2 HTTP通信迁移

**0.6.6 版本**：
```java
HttpClient client = new HttpClient();
HttpResponse response = client.post("http://target/api", data);
```

**0.7.2 版本**：
```java
@Autowired
private SouthNetworkService southNetworkService;

HttpRequest request = new HttpRequest("http://target/api", "POST", data);
HttpResponse response = southNetworkService.sendHttpRequest(request).join();
```

### 5.2 安全模块迁移

#### 5.2.1 认证迁移

**0.6.6 版本**：
```java
SecurityManager securityManager = new SecurityManagerImpl(storageManager);
AuthenticationResult result = securityManager.authenticate("admin", "password").join();
```

**0.7.2 版本**：
```java
@Autowired
private SouthSecurityService southSecurityService;

AuthenticationResult result = southSecurityService.authenticate("admin", "password").join();
```

#### 5.2.2 权限检查迁移

**0.6.6 版本**：
```java
AuthorizationResult result = securityManager.authorize("admin", "terminal", "read").join();
```

**0.7.2 版本**：
```java
@Autowired
private SouthSecurityService southSecurityService;

boolean hasPermission = southSecurityService.checkPermission("admin", "terminal", "read").join();
```

### 5.3 协作模块迁移

#### 5.3.1 场景组迁移

**0.6.6 版本**：
```java
SceneGroupManager manager = new SceneGroupManagerImpl();
SceneGroup group = manager.create("scene-001", config).join();
manager.join("scene-001", "agent-001").join();
```

**0.7.2 版本**：
```java
@Autowired
private SouthCollaborationService southCollaborationService;

EnhancedSceneGroup group = southCollaborationService.createSceneGroup("scene-001", config).join();
group.autoJoinNetwork().join();
```

#### 5.3.2 协议迁移

**0.6.6 版本**：
```java
ProtocolHub hub = new ProtocolHubImpl();
CommandPacket packet = CommandPacket.of("MCP", "EXECUTE");
CommandResult result = hub.handleCommand(packet);
```

**0.7.2 版本**：
```java
@Autowired
private EnhancedProtocolHub protocolHub;

EnhancedCommandPacket packet = EnhancedCommandPacket.builder()
    .protocolType("MCP")
    .commandType("EXECUTE")
    .build();

CommandResult result = protocolHub.handleEnhancedCommand(packet);
```

## 6. 新功能使用

### 6.1 用户-组织-域模型

```java
import net.ooder.sdk.domain.DomainService;
import net.ooder.sdk.domain.OrganizationService;
import net.ooder.sdk.user.UserMcpAgent;

@Autowired
private DomainService domainService;

@Autowired
private OrganizationService organizationService;

public void example() {
    Domain userDomain = domainService.createDomain("用户域", DomainType.USER, userId).join();
    
    Organization org = organizationService.getOrgById("org-001").join();
    
    UserMcpAgent userMcp = UserMcpAgentFactory.createPersonalMcpAgent(userId, config);
}
```

### 6.2 增强场景组

```java
import net.ooder.sdk.service.south.EnhancedSceneGroup;

@Autowired
private SouthCollaborationService southCollaborationService;

public void example() {
    EnhancedSceneGroup group = southCollaborationService.createSceneGroup("scene-001", config).join();
    
    group.autoJoinNetwork().join();
    
    LLMDecision decision = group.consultLLM("网络配置优化", context).join();
    
    group.enableOfflineMode().join();
    
    BranchResult result = group.executeBranchScenario("scenario-001", conditions).join();
}
```

### 6.3 增强北向协议

```java
import net.ooder.sdk.protocol.EnhancedProtocolHub;
import net.ooder.sdk.protocol.EnhancedCommandPacket;
import net.ooder.sdk.protocol.RetryPolicy;
import net.ooder.sdk.protocol.TimeoutPolicy;

@Autowired
private EnhancedProtocolHub protocolHub;

public void example() {
    EnhancedCommandPacket packet = EnhancedCommandPacket.builder()
        .protocolType("MCP_AGENT")
        .commandType("SHARE_SKILL")
        .priority(CommandPriority.HIGH)
        .retryPolicy(new RetryPolicy(3, 1000, 2.0))
        .timeoutPolicy(new TimeoutPolicy(30000, TimeoutAction.RETRY))
        .payload(Map.of("skillId", "skill-001"))
        .build();
    
    CommandResult result = protocolHub.handleEnhancedCommand(packet);
    
    CommandTrace trace = protocolHub.traceCommand(packet.getPacketId());
    
    if (!result.isSuccess()) {
        CommandResult retryResult = protocolHub.retryCommand(packet.getPacketId()).join();
    }
}
```

## 7. 故障排除

### 7.1 常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| **依赖冲突** | ooder-common版本不匹配 | 确保ooder-common版本为2.1 |
| **配置未生效** | 配置文件路径错误 | 检查配置文件位置 |
| **服务注入失败** | Spring上下文未初始化 | 确保正确初始化ApplicationContext |
| **协议处理失败** | 协议处理器未注册 | 注册相应的协议处理器 |

### 7.2 调试建议

1. **启用调试日志**：
```properties
logging.level.net.ooder.sdk=DEBUG
```

2. **检查服务状态**：
```java
@Autowired
private SouthNetworkService southNetworkService;

boolean isRunning = southNetworkService.isRunning();
```

3. **追踪命令执行**：
```java
CommandTrace trace = protocolHub.traceCommand(commandId);
System.out.println("Trace: " + trace);
```

## 8. 总结

Ooder Agent SDK 0.7.2 版本通过南北向分层架构，提供了更清晰、更灵活、更强大的Agent开发框架：

1. **架构升级**：南北向分层，职责清晰
2. **功能增强**：增强场景组、增强协议
3. **向后兼容**：保持API和配置兼容
4. **平滑迁移**：提供详细的迁移指南

---

**Ooder Agent SDK 0.7.2** - 构建智能、协作、安全的Agent生态系统！
