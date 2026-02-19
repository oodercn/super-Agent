# Ooder Agent SDK 0.7.2 完整开发计划

**文档版本**: 2.0  
**更新日期**: 2026-02-17  
**版本号**: 0.7.2 (保持不变)

---

## 1. 概述

### 1.1 文档目的

本文档整合了以下内容：
1. API覆盖率报告中的缺失接口
2. ooder-Nexus项目的协作需求
3. 测试用例补充计划

### 1.2 当前状态

| 指标 | 当前值 | 目标值 |
|------|--------|--------|
| 实现覆盖率 | **86.2%** | 95%+ |
| 测试覆盖率 | **35%** | 80%+ |
| 协作需求满足 | **100%** | 100% |

### 1.3 开发阶段规划

| 阶段 | 内容 | 工作量 | 优先级 |
|------|------|--------|--------|
| 第一阶段 | 南向协议实现 (P0) | 6天 | 最高 |
| 第二阶段 | 离线与分享服务 (P0) | 5天 | 最高 |
| 第三阶段 | 核心服务实现 (P0) | 9天 | 高 |
| 第四阶段 | 属性补充与事件总线 (P1) | 3.5天 | 中 |
| 第五阶段 | 安全服务实现 (P1) | 4天 | 中 |
| 第六阶段 | 测试用例补充 | 5天 | 最后 |

**总工作量**: 32.5天

---

## 2. 第一阶段：南向协议实现 (P0)

### 2.1 DiscoveryProtocol 增强

**任务ID**: PROTO-001  
**工作量**: 2天  
**优先级**: P0 (最高)

**当前状态**: 已有基础实现，需要增强

**增强内容**:
```java
public interface DiscoveryProtocol {
    // 现有方法
    CompletableFuture<DiscoveryResult> discover(DiscoveryRequest request);
    CompletableFuture<List<PeerInfo>> listDiscoveredPeers();
    
    // 新增方法
    CompletableFuture<PeerInfo> discoverMcp();
    void addDiscoveryListener(DiscoveryEventListener listener);
    void startBroadcast();
    void stopBroadcast();
    boolean isBroadcasting();
}
```

**新增DTO**:
- `DiscoveryEventListener` - 发现事件监听器
- `PeerInfo.capabilities` - 能力标签属性

### 2.2 LoginProtocol 增强

**任务ID**: PROTO-002  
**工作量**: 1.5天  
**优先级**: P0 (最高)

**当前状态**: 已有基础实现，需要增强

**增强内容**:
```java
public interface LoginProtocol {
    // 现有方法
    CompletableFuture<LoginResult> login(LoginRequest request);
    CompletableFuture<Void> logout(String sessionId);
    CompletableFuture<SessionInfo> getSession(String sessionId);
    
    // 新增方法
    CompletableFuture<LoginResult> autoLogin(AutoLoginConfig config);
    CompletableFuture<SessionInfo> validateSession(String sessionId);
    CompletableFuture<Void> refreshSession(String sessionId);
    CompletableFuture<DomainPolicy> getDomainPolicy(String userId);
    void addLoginListener(LoginEventListener listener);
}
```

**新增DTO**:
- `AutoLoginConfig` - 自动登录配置
- `DomainPolicy` - 域策略
- `LoginEventListener` - 登录事件监听器

### 2.3 CollaborationProtocol 增强

**任务ID**: PROTO-003  
**工作量**: 2.5天  
**优先级**: P0 (最高)

**当前状态**: 已有基础实现，需要增强

**增强内容**:
```java
public interface CollaborationProtocol {
    // 现有方法
    CompletableFuture<SceneGroupInfo> joinSceneGroup(String groupId, JoinRequest request);
    CompletableFuture<Void> leaveSceneGroup(String groupId);
    
    // 新增方法
    CompletableFuture<Void> acceptInvitation(String invitationId);
    CompletableFuture<Void> declineInvitation(String invitationId);
    CompletableFuture<List<Invitation>> getPendingInvitations();
    CompletableFuture<TaskInfo> receiveTask(String groupId);
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);
    CompletableFuture<List<TaskInfo>> getPendingTasks(String groupId);
    CompletableFuture<Void> syncState(String groupId, Map<String, Object> state);
    CompletableFuture<Map<String, Object>> getGroupState(String groupId);
    CompletableFuture<List<MemberInfo>> getGroupMembers(String groupId);
    void addCollaborationListener(CollaborationEventListener listener);
}
```

**新增DTO**:
- `Invitation` - 邀请信息
- `TaskInfo` - 任务信息
- `TaskResult` - 任务结果
- `MemberInfo` - 成员信息
- `CollaborationEventListener` - 协作事件监听器

---

## 3. 第二阶段：离线与分享服务 (P0)

### 3.1 OfflineService 增强

**任务ID**: SVC-001  
**工作量**: 2天  
**优先级**: P0 (最高)

**当前状态**: 已有基础实现，需要增强

**增强内容**:
```java
public interface OfflineService {
    // 现有方法 (已实现)
    CompletableFuture<Void> enableOfflineMode();
    CompletableFuture<Void> disableOfflineMode();
    boolean isOfflineMode();
    boolean isNetworkAvailable();
    CompletableFuture<List<OfflineCapability>> getOfflineCapabilities();
    CompletableFuture<List<PendingSync>> getPendingSyncItems();
    CompletableFuture<SyncResult> syncNow();
    CompletableFuture<Void> syncItem(String syncId);
    void addNetworkListener(NetworkStateListener listener);
    void removeNetworkListener(NetworkStateListener listener);
    void addSyncListener(SyncStateListener listener);
    void removeSyncListener(SyncStateListener listener);
}
```

**测试覆盖**: 已创建 `OfflineServiceImplTest`

### 3.2 SkillShareService 新增

**任务ID**: SVC-002  
**工作量**: 3天  
**优先级**: P0 (最高)

**当前状态**: 未实现

**接口定义**:
```java
package net.ooder.sdk.api.share;

public interface SkillShareService {
    CompletableFuture<SharePrepareResult> prepareShare(String skillId, ShareConfig config);
    CompletableFuture<Void> sendShareInvitation(String targetPeerId, String shareId);
    CompletableFuture<Void> acceptShareInvitation(String invitationId);
    CompletableFuture<Void> declineShareInvitation(String invitationId);
    CompletableFuture<List<ShareInvitation>> getPendingShareInvitations();
    CompletableFuture<ShareProgress> getShareProgress(String shareId);
    CompletableFuture<Void> cancelShare(String shareId);
    CompletableFuture<List<ShareRecord>> getShareHistory();
    void addShareListener(ShareEventListener listener);
}
```

**DTO定义**:
- `SharePrepareResult` - 分享准备结果
- `ShareConfig` - 分享配置
- `ShareInvitation` - 分享邀请
- `ShareProgress` - 分享进度
- `ShareRecord` - 分享记录
- `ShareEventListener` - 分享事件监听器

---

## 4. 第三阶段：核心服务实现 (P0)

### 4.1 API层服务实现

| 任务ID | 接口名称 | 包路径 | 方法数 | 工作量 | 依赖 |
|--------|----------|--------|--------|--------|------|
| IMPL-001 | StorageService | api.storage | 8 | 1天 | 无 |
| IMPL-002 | SecurityService | api.security | 6 | 1天 | StorageService |
| IMPL-003 | NetworkService | api.network | 10 | 1天 | 无 |
| IMPL-004 | LlmService | api.llm | 8 | 1天 | 无 |

### 4.2 内部服务实现

| 任务ID | 接口名称 | 包路径 | 方法数 | 工作量 | 依赖 |
|--------|----------|--------|--------|--------|------|
| IMPL-005 | HeartbeatService | service.heartbeat | 4 | 0.5天 | NetworkService |
| IMPL-006 | DiscoveryService | service.discovery | 6 | 0.5天 | NetworkService |
| IMPL-007 | AgentService | service.agent | 8 | 1天 | HeartbeatService |
| IMPL-008 | MetadataService | service.metadata | 6 | 0.5天 | StorageService |
| IMPL-009 | SceneService | service.scene | 8 | 1天 | MetadataService |

### 4.3 技能与安全服务实现

| 任务ID | 接口名称 | 包路径 | 方法数 | 工作量 | 依赖 |
|--------|----------|--------|--------|--------|------|
| IMPL-010 | SkillService | service.skill | 8 | 1天 | StorageService |
| IMPL-011 | SkillCenterService | service.skillcenter | 10 | 1天 | SkillService |
| IMPL-012 | RemoteDeploymentService | service.skillcenter | 6 | 0.5天 | SkillCenterService |
| IMPL-013 | CipherService | service.security.crypto | 6 | 0.5天 | 无 |

---

## 5. 第四阶段：属性补充与事件总线 (P1)

### 5.1 InstalledSkill 属性补充

**任务ID**: DTO-001  
**工作量**: 0.5天  
**优先级**: P1

**补充属性**:
```java
public class InstalledSkill {
    // 现有属性
    private String skillId;
    private String skillName;
    private String version;
    private String status;
    
    // 补充属性
    private String previousStatus;
    private long lastStateChangeTime;
    private int restartCount;
    private long totalRunTime;
    private long lastStartTime;
    private long lastStopTime;
    private String errorMessage;
    private Map<String, Object> runtimeState;
    private List<String> dependencies;
    private Map<String, String> config;
    private int priority;
    private boolean autoStart;
    private long healthCheckInterval;
    private long lastHealthCheckTime;
}
```

### 5.2 LinkInfo 属性补充

**任务ID**: DTO-002  
**工作量**: 0.5天  
**优先级**: P1

**补充属性**:
```java
public class LinkInfo {
    // 现有属性
    private String linkId;
    private String sourceId;
    private String targetId;
    private String linkType;
    
    // 补充属性
    private String status;
    private long establishedTime;
    private long lastActiveTime;
    private int reconnectCount;
    private long totalBytesSent;
    private long totalBytesReceived;
    private double avgLatency;
    private double packetLossRate;
    private String qualityLevel;
    private Map<String, Object> metadata;
}
```

### 5.3 SceneGroupInfo 属性补充

**任务ID**: DTO-003  
**工作量**: 0.5天  
**优先级**: P1

**补充属性**:
```java
public class SceneGroupInfo {
    // 现有属性
    private String groupId;
    private String sceneId;
    private String primaryId;
    private List<String> memberIds;
    
    // 补充属性
    private String status;
    private int maxMembers;
    private int heartbeatInterval;
    private int heartbeatTimeout;
    private boolean autoFailover;
    private String previousPrimaryId;
    private long primarySince;
    private int failoverCount;
    private Map<String, Object> sharedState;
    private long lastStateUpdate;
    private List<String> pendingInvitations;
}
```

### 5.4 EventBus 事件总线

**任务ID**: SVC-003  
**工作量**: 2天  
**优先级**: P1

**接口定义**:
```java
package net.ooder.sdk.api.event;

public interface EventBus {
    <T extends Event> void publish(T event);
    <T extends Event> void subscribe(Class<T> eventType, EventHandler<T> handler);
    <T extends Event> void unsubscribe(Class<T> eventType, EventHandler<T> handler);
    <T extends Event> void publishSync(T event);
    <T extends Event, R> CompletableFuture<R> publishAndWait(T event, Class<R> resultType);
}

public abstract class Event {
    private String eventId;
    private String eventType;
    private long timestamp;
    private String source;
    private Map<String, Object> metadata;
}
```

**标准事件**:
- `SkillInstalledEvent` - 技能安装完成
- `SkillUninstalledEvent` - 技能卸载完成
- `SkillStartedEvent` - 技能启动
- `SkillStoppedEvent` - 技能停止
- `PeerDiscoveredEvent` - 节点发现
- `PeerLostEvent` - 节点丢失
- `LinkEstablishedEvent` - 链路建立
- `LinkDisconnectedEvent` - 链路断开
- `SceneGroupCreatedEvent` - 场景组创建
- `MemberJoinedEvent` - 成员加入
- `MemberLeftEvent` - 成员离开
- `PrimaryChangedEvent` - 主节点变更
- `SecurityAlertEvent` - 安全告警
- `LoginEvent` - 登录事件
- `SyncStartedEvent` - 同步开始
- `SyncCompletedEvent` - 同步完成

---

## 6. 第五阶段：安全服务实现 (P1)

### 6.1 SecurityService 增强

**任务ID**: SVC-004  
**工作量**: 3天  
**优先级**: P1

**接口定义**:
```java
public interface SecurityService {
    // 用户管理
    CompletableFuture<List<UserInfo>> getUsers();
    CompletableFuture<UserInfo> addUser(UserData userData);
    CompletableFuture<UserInfo> editUser(String userId, UserData userData);
    CompletableFuture<Void> deleteUser(String userId);
    CompletableFuture<UserInfo> enableUser(String userId);
    CompletableFuture<UserInfo> disableUser(String userId);

    // 权限管理
    CompletableFuture<List<Permission>> getPermissions();
    CompletableFuture<Void> savePermissions(String roleId, List<Permission> permissions);
    CompletableFuture<Boolean> checkPermission(String userId, String resource, String action);

    // 安全日志
    CompletableFuture<List<SecurityLog>> getSecurityLogs(SecurityLogQuery query);
    CompletableFuture<Void> logSecurityEvent(SecurityEvent event);

    // 审计
    CompletableFuture<AuditReport> runAudit();
    CompletableFuture<List<AuditFinding>> getAuditFindings();

    // 告警
    void addSecurityAlertListener(SecurityAlertListener listener);
}
```

### 6.2 EncryptionService 加密服务

**任务ID**: SVC-005  
**工作量**: 1天  
**优先级**: P1

**接口定义**:
```java
public interface EncryptionService {
    CompletableFuture<SessionKey> generateSessionKey(String targetPeerId);
    CompletableFuture<byte[]> encryptWithSessionKey(String sessionId, byte[] data);
    CompletableFuture<byte[]> decryptWithSessionKey(String sessionId, byte[] data);
    CompletableFuture<Void> destroySessionKey(String sessionId);
    CompletableFuture<SessionKeyStatus> getSessionKeyStatus(String sessionId);
}
```

---

## 7. 第六阶段：测试用例补充

### 7.1 高优先级测试

| 任务ID | 测试目标 | 测试方法数 | 依赖 |
|--------|----------|------------|------|
| TEST-001 | OfflineServiceImpl | 12 | 无 |
| TEST-002 | PrivateResourceServiceImpl | 12 | 无 |
| TEST-003 | ObservationProtocolImpl | 14 | 无 |
| TEST-004 | CollaborationProtocolImpl | 12 | 无 |
| TEST-005 | SkillShareServiceImpl | 9 | SVC-002 |

### 7.2 中优先级测试

| 任务ID | 测试目标 | 测试方法数 | 依赖 |
|--------|----------|------------|------|
| TEST-006 | MetadataQueryServiceImpl | 6 | 无 |
| TEST-007 | ChangeLogServiceImpl | 4 | 无 |
| TEST-008 | SceneGroupManagerImpl | 8 | 无 |
| TEST-009 | SceneManagerImpl | 8 | 无 |
| TEST-010 | EventBusImpl | 5 | SVC-003 |

### 7.3 新增服务测试

| 任务ID | 测试目标 | 测试方法数 | 依赖 |
|--------|----------|------------|------|
| TEST-011 | StorageServiceImpl | 8 | IMPL-001 |
| TEST-012 | SecurityServiceImpl | 6 | IMPL-002 |
| TEST-013 | NetworkServiceImpl | 10 | IMPL-003 |
| TEST-014 | LlmServiceImpl | 8 | IMPL-004 |
| TEST-015 | HeartbeatServiceImpl | 4 | IMPL-005 |
| TEST-016 | DiscoveryServiceImpl | 6 | IMPL-006 |
| TEST-017 | AgentServiceImpl | 8 | IMPL-007 |
| TEST-018 | MetadataServiceImpl | 6 | IMPL-008 |
| TEST-019 | SceneServiceImpl | 8 | IMPL-009 |
| TEST-020 | SkillServiceImpl | 8 | IMPL-010 |
| TEST-021 | SkillCenterServiceImpl | 10 | IMPL-011 |
| TEST-022 | RemoteDeploymentServiceImpl | 6 | IMPL-012 |
| TEST-023 | CipherServiceImpl | 6 | IMPL-013 |
| TEST-024 | EncryptionServiceImpl | 5 | SVC-005 |

---

## 8. 任务依赖关系图

```
第一阶段：南向协议 (P0)
├── PROTO-001 DiscoveryProtocol增强
├── PROTO-002 LoginProtocol增强
└── PROTO-003 CollaborationProtocol增强

第二阶段：离线与分享 (P0)
├── SVC-001 OfflineService增强
└── SVC-002 SkillShareService新增

第三阶段：核心服务 (P0)
├── IMPL-001 StorageService
├── IMPL-003 NetworkService
├── IMPL-004 LlmService
│
├── IMPL-002 SecurityService (依赖 IMPL-001)
├── IMPL-005 HeartbeatService (依赖 IMPL-003)
├── IMPL-006 DiscoveryService (依赖 IMPL-003)
├── IMPL-008 MetadataService (依赖 IMPL-001)
│
├── IMPL-007 AgentService (依赖 IMPL-005)
├── IMPL-009 SceneService (依赖 IMPL-008)
├── IMPL-010 SkillService (依赖 IMPL-001)
│   └── IMPL-011 SkillCenterService
│       └── IMPL-012 RemoteDeploymentService
└── IMPL-013 CipherService

第四阶段：属性与事件 (P1)
├── DTO-001 InstalledSkill属性补充
├── DTO-002 LinkInfo属性补充
├── DTO-003 SceneGroupInfo属性补充
└── SVC-003 EventBus事件总线

第五阶段：安全服务 (P1)
├── SVC-004 SecurityService增强
└── SVC-005 EncryptionService新增

第六阶段：测试 (最后)
└── 所有测试任务 (依赖对应实现完成)
```

---

## 9. 进度跟踪

### 9.1 实现进度

| 阶段 | 任务数 | 已完成 | 进度 |
|------|--------|--------|------|
| 第一阶段 | 3 | 3 | **100%** |
| 第二阶段 | 2 | 2 | **100%** |
| 第三阶段 | 13 | 13 | **100%** |
| 第四阶段 | 4 | 4 | **100%** |
| 第五阶段 | 2 | 2 | **100%** |
| **总计** | **24** | **24** | **100%** |

### 9.2 测试进度

| 阶段 | 任务数 | 已完成 | 进度 |
|------|--------|--------|------|
| 高优先级 | 5 | 3 | 60% |
| 中优先级 | 5 | 1 | 20% |
| 新增服务 | 14 | 3 | 21% |
| **总计** | **24** | **7** | **29%** |

---

## 9.3 新增模块开发说明 (v2.1)

### 9.3.1 SkillCenter 协作适配模块

**完成日期**: 2026-02-18

为支持 SkillCenter 项目与 SDK 0.7.2 的集成，新增以下适配器模块：

#### 南向协议适配器

| 模块 | 文件 | 状态 |
|------|------|------|
| DiscoveryProtocolAdapter | DiscoveryProtocolAdapterSdkImpl.java | ✅ 完成 |
| LoginProtocolAdapter | LoginProtocolAdapterSdkImpl.java | ✅ 完成 |
| CollaborationProtocolAdapter | CollaborationProtocolAdapterSdkImpl.java | ✅ 完成 |

#### 核心服务适配器

| 模块 | 文件 | 状态 |
|------|------|------|
| NetworkSdkAdapter | NetworkSdkAdapterImpl.java | ✅ 完成 |
| SecuritySdkAdapter | SecuritySdkAdapterImpl.java | ✅ 完成 |
| SceneSdkAdapter | SceneSdkAdapterImpl.java | ✅ 完成 |
| SceneGroupSdkAdapter | SceneGroupSdkAdapterImpl.java | ✅ 完成 |

#### 功能增强模块

| 模块 | 文件 | 状态 |
|------|------|------|
| AgentSDKWrapper | AgentSDKWrapper.java | ✅ 完成 |
| P2PNodeManager | P2PNodeManager.java (修改) | ✅ 完成 |
| ObservationProtocolAdapter | ObservationProtocolAdapter.java | ✅ 完成 |
| DomainManagementProtocolAdapter | DomainManagementProtocolAdapter.java | ✅ 完成 |

### 9.3.2 DTO 属性补充

| 类 | 补充属性数 | 状态 |
|----|-----------|------|
| LinkInfo | 7 | ✅ 完成 |
| SceneGroupInfo | 10 | ✅ 完成 |
| InstalledSkill | 15 | ✅ 完成 |

### 9.3.3 Git 发现模块

SDK 0.7.2 已内置 GitHub/Gitee 技能发现功能：

| 模块 | 位置 | 状态 |
|------|------|------|
| GitHubDiscoverer | net.ooder.sdk.discovery.git | ✅ 已存在 |
| GiteeDiscoverer | net.ooder.sdk.discovery.git | ✅ 已存在 |
| GitRepositoryDiscoverer | net.ooder.sdk.discovery.git | ✅ 已存在 |
| GitDiscoveryConfig | net.ooder.sdk.discovery.git | ✅ 已存在 |

### 9.3.4 DiscoveryMethod 枚举

```java
public enum DiscoveryMethod {
    UDP_BROADCAST,
    DHT_KADEMLIA,
    MDNS_DNS_SD,
    SKILL_CENTER,
    LOCAL_FS,
    GITHUB,    // ✅ 已存在
    GITEE;     // ✅ 已存在
}
```

### 9.4 ooder-Nexus 协作模块 (v2.2)

**完成日期**: 2026-02-18

为支持 ooder-Nexus 项目协作需求，新增以下模块：

#### 9.4.1 安装进度跟踪增强

| 模块 | 文件 | 状态 |
|------|------|------|
| InstallProgress | SkillInstaller.java (增强) | ✅ 完成 |
| DependencyInfo | DependencyInfo.java | ✅ 完成 |
| DependencyItem | DependencyItem.java | ✅ 完成 |
| DependencyInfoImpl | DependencyInfoImpl.java | ✅ 完成 |

**新增属性**:
- `installId` - 安装实例ID
- `stage` - 安装阶段 (downloading, extracting, validating, installing)
- `status` - 状态 (running, completed, failed)
- `startTime` - 开始时间

#### 9.4.2 技能生命周期控制

| 模块 | 文件 | 状态 |
|------|------|------|
| SkillLifecycle | SkillLifecycle.java | ✅ 已存在 |
| SkillState | SkillState.java | ✅ 已存在 |
| SkillLifecycleImpl | SkillLifecycleImpl.java | ✅ 完成 |

**支持状态**:
- CREATED, INITIALIZING, INITIALIZED, STARTING, RUNNING
- PAUSED, STOPPING, STOPPED, DESTROYING, DESTROYED, ERROR, FAILED

#### 9.4.3 南向协议适配器

| 适配器 | 接口文件 | 实现文件 | 状态 |
|--------|----------|----------|------|
| DiscoveryProtocolAdapter | DiscoveryProtocolAdapter.java | DiscoveryProtocolAdapterImpl.java | ✅ 完成 |
| LoginProtocolAdapter | LoginProtocolAdapter.java | LoginProtocolAdapterImpl.java | ✅ 完成 |
| CollaborationProtocolAdapter | CollaborationProtocolAdapter.java | CollaborationProtocolAdapterImpl.java | ✅ 完成 |
| DomainManagementProtocolAdapter | DomainManagementProtocolAdapter.java | DomainManagementProtocolAdapterImpl.java | ✅ 完成 |
| ObservationProtocolAdapter | ObservationProtocolAdapter.java | ObservationProtocolAdapterImpl.java | ✅ 完成 |

#### 9.4.4 适配器模型类

| 模块 | 文件 | 用途 |
|------|------|------|
| DiscoveryConfig | DiscoveryConfig.java | 发现配置 |
| DiscoveredNode | DiscoveredNode.java | 发现节点信息 |
| LoginRequest | LoginRequest.java | 登录请求 |
| LoginResult | LoginResult.java | 登录结果 |
| SessionInfo | SessionInfo.java | 会话信息 |
| CollaborationMember | CollaborationMember.java | 协作成员 |
| CollaborationMessage | CollaborationMessage.java | 协作消息 |
| CollaborationListener | CollaborationListener.java | 协作监听器 |
| DomainConfig | DomainConfig.java | 域配置 |
| DomainInfo | DomainInfo.java | 域信息 |
| DomainMember | DomainMember.java | 域成员 |
| ObservationConfig | ObservationConfig.java | 观察配置 |
| ObservationData | ObservationData.java | 观察数据 |
| ObservationMetric | ObservationMetric.java | 观察指标 |
| ObservationListener | ObservationListener.java | 观察监听器 |

---

## 10. 预期覆盖率提升

| 指标 | 当前 | 第一阶段后 | 第二阶段后 | 第三阶段后 | 第四阶段后 | 第五阶段后 | 第六阶段后 |
|------|------|------------|------------|------------|------------|------------|------------|
| 实现覆盖率 | 48.5% | 57.6% | 63.6% | 93.9% | 97.0% | 100% | 100% |
| 测试覆盖率 | 24.2% | 24.2% | 27.3% | 27.3% | 30.3% | 30.3% | 87.9% |

---

## 11. 验收标准

### 11.1 实现验收

- [ ] 所有接口方法已实现
- [ ] 编译无错误
- [ ] 代码符合规范
- [ ] 有基本的错误处理
- [ ] 添加Javadoc注释

### 11.2 测试验收

- [ ] 所有公共方法有测试用例
- [ ] 测试覆盖率 > 80%
- [ ] 所有测试通过
- [ ] 有边界条件测试
- [ ] 有异常场景测试

### 11.3 文档验收

- [ ] API文档完整
- [ ] 更新README.md
- [ ] 更新架构文档

---

## 12. 附录

### 12.1 接口方法签名差异处理

| SDK接口 | SDK方法 | Nexus期望方法 | 处理方式 |
|---------|---------|---------------|----------|
| SkillPackageManager | install(InstallRequest) | install(skillId, version, config) | 保留SDK签名，Nexus适配 |
| SkillPackageManager | start(skillId) | startSkill(skillId) | 添加别名方法 |
| SkillPackageManager | 无 | pause(skillId) | 新增方法 |
| SkillPackageManager | 无 | resume(skillId) | 新增方法 |
| NetworkService | 无 | getLinkQuality(linkId) | 新增方法 |
| NetworkService | 无 | calculateOptimalPath(sourceId, targetId) | 新增方法 |
| SceneGroupManager | 无 | generateInviteCode(groupId) | 新增方法 |
| SceneGroupManager | 无 | sendHeartbeat(groupId) | 新增方法 |

### 12.2 错误码规范

```java
public enum ErrorCode {
    // 通用错误
    UNKNOWN("SDK_000", "Unknown error", 500),
    INVALID_PARAMETER("SDK_001", "Invalid parameter", 400),
    
    // 技能错误
    SKILL_NOT_FOUND("SKILL_001", "Skill not found", 404),
    SKILL_INSTALL_FAILED("SKILL_002", "Install failed", 500),
    SKILL_START_FAILED("SKILL_003", "Start failed", 500),
    
    // 网络错误
    PEER_NOT_FOUND("NET_001", "Peer not found", 404),
    LINK_ESTABLISH_FAILED("NET_002", "Link establish failed", 500),
    
    // 安全错误
    AUTH_FAILED("SEC_001", "Authentication failed", 401),
    PERMISSION_DENIED("SEC_002", "Permission denied", 403);
}
```

---

**版本**: 2.2  
**日期**: 2026-02-18  
**作者**: ooder Team
