# 场景组协议 (Scene Group Protocol) - v0.7.3

## 文档信息

- 版本：v0.7.3
- 创建日期：2026-02-20
- 状态：定稿

---

## 一、概述

### 1.1 场景组定义

**场景组（SceneGroup）** 是共享KEY和VFS资源的Agent集合，实现场景的协作和故障切换。

### 1.2 v0.7.3 升级内容

| 新增特性 | 说明 |
|---------|------|
| CollaborationProtocol | 场景组协作协议，支持任务分配和状态同步 |
| OfflineService | 离线服务，支持网络断开时的场景组运行 |
| EventBus | 事件总线，统一场景组事件管理 |
| 任务协作 | 场景组成员间任务分配和结果提交 |
| 状态同步 | 场景组状态一致性保证 |

### 1.3 场景组特点

| 特点 | 描述 |
|------|------|
| 共享KEY | 所有成员共享SceneGroupKey |
| 资源共享 | VFS资源访问权限绑定在KEY上 |
| 故障切换 | 支持PRIMARY/BACKUP角色切换 |
| 高可用 | 单点故障不影响服务 |
| **v0.7.3 新增** 离线支持 | 网络断开时可继续运行 |
| **v0.7.3 新增** 任务协作 | 成员间任务分配和协作 |

---

## 二、场景组成员角色

### 2.1 PRIMARY（主角色）

| 属性 | 值 |
|------|-----|
| 定义 | 场景组的主要RouteAgent |
| 职责 | 消息路由、KEY管理、链路维护、资源协调、任务分配 |
| 状态 | ACTIVE |
| 数量 | 每个场景组1个 |

### 2.2 BACKUP（备角色）

| 属性 | 值 |
|------|-----|
| 定义 | 场景组的备用RouteAgent |
| 职责 | 状态监听、链路同步、准备接管、任务执行 |
| 状态 | STANDBY |
| 数量 | 每个场景组0-N个 |

### 2.3 v0.7.3 新增角色能力

| 角色 | 新增能力 |
|------|---------|
| PRIMARY | 任务分配、状态同步、离线协调 |
| BACKUP | 任务接收、结果提交、离线缓存 |

---

## 三、场景组KEY

### 3.1 SceneGroupKey结构

```yaml
groupId: string                    # 场景组唯一标识
sceneName: string                  # 场景名称
version: integer                   # KEY版本

masterKey: string                  # 主密钥（加密存储）
accessKey: string                  # 访问密钥

permissions:                       # 权限定义
  vfs:                             # VFS权限
    "path": ["read", "write", "delete"]
  capabilities:                    # 能力权限
    "capabilityId": true

members:                           # 成员列表
  - agentId: string                # Agent标识
    role: PRIMARY | BACKUP         # 角色
    publicKey: string              # 公钥
    keyShare: string               # 密钥分片

createdAt: string                  # 创建时间（ISO 8601）
expiresAt: string                  # 过期时间（ISO 8601）

signature: string                  # MCP Agent签名

# v0.7.3 新增
offlineConfig:                     # 离线配置
  enabled: true                    # 是否启用离线模式
  syncInterval: 60000              # 同步间隔（毫秒）
  conflictResolution: last-write   # 冲突解决策略
```

### 3.2 密钥分片机制

使用Shamir秘密共享算法分割主密钥：

| 参数 | 说明 | 建议值 |
|------|------|--------|
| N | 总分片数 | 场景组成员数 |
| K | 恢复阈值 | N/2 + 1 |

**恢复公式：**
```
masterKey = recover(keyShare[0], keyShare[1], ..., keyShare[K-1])
```

---

## 四、故障检测与切换

### 4.1 心跳机制

| 参数 | 值 | 说明 |
|------|-----|------|
| 心跳间隔 | 5秒 | PRIMARY发送心跳 |
| 状态报告间隔 | 10秒 | BACKUP发送状态报告 |
| 超时阈值 | 3次 | 连续超时次数 |
| 检测时间 | 15秒 | 故障判定时间 |

### 4.2 故障检测流程

```
T+0s:   PRIMARY停止响应
        │
        ▼
T+5s:   BACKUP未收到心跳，heartbeatMissed = 1
        │
        ▼
T+10s:  BACKUP未收到心跳，heartbeatMissed = 2
        │
        ├── 日志警告
        └── 准备故障检测
        │
        ▼
T+15s:  BACKUP未收到心跳，heartbeatMissed = 3
        │
        ├── 触发故障检测
        └── 向MCP Agent确认PRIMARY状态
        │
        ▼
T+16s:  MCP Agent确认PRIMARY故障
        │
        └── 发送FAILOVER_APPROVE
```

### 4.3 故障切换流程

```
1. 收到FAILOVER_APPROVE
   │
   ▼
2. 恢复主密钥
   │
   ├── 收集keyShare（本地 + 其他节点 + MCP Agent）
   ├── 使用Shamir算法恢复masterKey
   └── 验证masterKey签名
   │
   ▼
3. 继承场景组KEY
   │
   ├── 激活本地SceneGroupKey副本
   ├── 验证权限
   └── 更新角色为PRIMARY
   │
   ▼
4. 恢复链路表
   │
   ├── 从本地副本恢复
   ├── 重建与End Agent的连接
   └── 通知MCP Agent更新路由
   │
   ▼
5. 启动技能服务
   │
   ├── 检查本地技能
   ├── 启动必需技能
   └── 验证健康状态
   │
   ▼
6. 广播路由更新
   │
   └── 发送ROUTE_UPDATE到所有节点
   │
   ▼
7. 发布事件 (v0.7.3 新增)
   │
   └── EventBus.publish(PrimaryChangedEvent)
```

---

## 五、任务协作（v0.7.3 新增）

### 5.1 任务分配接口

```java
public interface CollaborationProtocol {
    
    CompletableFuture<TaskInfo> receiveTask(String groupId);
    
    CompletableFuture<Void> submitTaskResult(String groupId, String taskId, TaskResult result);
    
    CompletableFuture<TaskStatus> getTaskStatus(String groupId, String taskId);
}
```

### 5.2 任务数据结构

```java
public class TaskInfo {
    
    private String taskId;
    private String groupId;
    private String assignerId;
    private String assigneeId;
    private TaskType type;
    private int priority;
    private Map<String, Object> params;
    private long createTime;
    private long deadline;
}

public enum TaskType {
    DATA_PROCESSING,
    SKILL_INVOCATION,
    RESOURCE_SYNC,
    HEALTH_CHECK,
    CUSTOM
}
```

### 5.3 任务协作流程

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

---

## 六、状态同步（v0.7.3 新增）

### 6.1 同步接口

```java
public interface CollaborationProtocol {
    
    CompletableFuture<Void> syncState(String groupId, SceneGroupState state);
    
    CompletableFuture<SceneGroupState> getState(String groupId);
}
```

### 6.2 状态数据结构

```java
public class SceneGroupState {
    
    private String groupId;
    private long version;
    private long timestamp;
    
    private Map<String, MemberState> members;
    private Map<String, Object> sharedData;
    private List<String> pendingTasks;
    
    private OfflineState offlineState;
}

public class MemberState {
    
    private String agentId;
    private String role;
    private MemberStatus status;
    private long lastHeartbeat;
    private List<String> assignedTasks;
}

public class OfflineState {
    
    private boolean offlineMode;
    private long lastSyncTime;
    private int pendingChanges;
    private String conflictResolution;
}
```

### 6.3 同步策略

| 同步类型 | 触发条件 | 数据范围 |
|---------|---------|---------|
| 全量同步 | 成员加入 | 完整状态 |
| 增量同步 | 状态变更 | 变更部分 |
| 心跳同步 | 定时 | 状态摘要 |
| 离线同步 | 网络恢复 | 离线期间变更 |

---

## 七、离线支持（v0.7.3 新增）

### 7.1 离线模式切换

```java
public class SceneGroupOfflineHandler {
    
    @EventHandler
    public void onNetworkDisconnected(NetworkDisconnectedEvent event) {
        if (!isSceneGroupMember()) {
            return;
        }
        
        offlineService.enableOfflineMode();
        sceneStateManager.enableOfflineBuffering();
        eventBus.publish(new OfflineModeEnabledEvent(groupId));
    }
    
    @EventHandler
    public void onNetworkConnected(NetworkConnectedEvent event) {
        offlineService.syncNow()
            .thenAccept(result -> {
                sceneStateManager.disableOfflineBuffering();
                eventBus.publish(new SyncCompletedEvent(groupId, result));
            });
    }
}
```

### 7.2 离线数据暂存

```java
public class OfflineBuffer {
    
    private Queue<StateChange> pendingChanges;
    private int maxSize;
    
    public void addChange(StateChange change) {
        if (pendingChanges.size() >= maxSize) {
            applyEvictionPolicy();
        }
        pendingChanges.offer(change);
    }
    
    public List<StateChange> getPendingChanges() {
        return new ArrayList<>(pendingChanges);
    }
    
    public void clear() {
        pendingChanges.clear();
    }
}
```

### 7.3 冲突解决

| 策略 | 说明 | 适用场景 |
|------|------|---------|
| last-write | 最后写入胜出 | 简单场景 |
| merge | 自动合并 | 可合并数据 |
| manual | 手动解决 | 复杂冲突 |

---

## 八、事件管理（v0.7.3 新增）

### 8.1 场景组事件类型

| 事件类型 | 说明 | 发布时机 |
|---------|------|---------|
| SceneGroupCreatedEvent | 场景组创建 | 创建成功后 |
| MemberJoinedEvent | 成员加入 | 加入成功后 |
| MemberLeftEvent | 成员离开 | 离开后 |
| PrimaryChangedEvent | 主节点变更 | 故障切换后 |
| TaskAssignedEvent | 任务分配 | 任务入队后 |
| TaskCompletedEvent | 任务完成 | 结果提交后 |
| StateSyncedEvent | 状态同步 | 同步完成后 |
| OfflineModeEnabledEvent | 离线模式启用 | 网络断开后 |
| SyncCompletedEvent | 同步完成 | 网络恢复后 |

### 8.2 事件订阅示例

```java
@PostConstruct
public void init() {
    eventBus.subscribe(SceneGroupCreatedEvent.class, this::onSceneGroupCreated);
    eventBus.subscribe(MemberJoinedEvent.class, this::onMemberJoined);
    eventBus.subscribe(PrimaryChangedEvent.class, this::onPrimaryChanged);
    eventBus.subscribe(TaskAssignedEvent.class, this::onTaskAssigned);
    eventBus.subscribe(OfflineModeEnabledEvent.class, this::onOfflineModeEnabled);
}
```

---

## 九、VFS资源访问

### 9.1 访问令牌格式

```json
{
    "agentId": "route-agent-b",
    "groupId": "scene-messaging-001",
    "resourceId": "data/messages",
    "permission": "read",
    "timestamp": "2026-02-20T10:00:30Z",
    "expiresAt": "2026-02-20T10:05:30Z",
    "offlineMode": false,
    "signature": "..."
}
```

### 9.2 权限验证流程

```
1. Agent请求访问VFS资源
   │
   ├── 生成访问令牌
   └── 使用accessKey签名
   │
   ▼
2. VFS验证令牌
   │
   ├── 验证签名（使用场景组公钥）
   ├── 检查权限列表
   ├── 检查令牌有效期
   ├── 检查资源路径
   └── 检查离线模式状态 (v0.7.3 新增)
   │
   ▼
3. 返回结果
   │
   ├── 验证通过：返回资源或执行操作
   └── 验证失败：返回错误
```

---

## 十、性能指标

| 指标 | 要求 | 说明 |
|------|------|------|
| 故障检测时间 | < 15秒 | 心跳超时检测 |
| 切换时间 | < 30秒 | 从检测到服务恢复 |
| VFS访问延迟 | < 100ms | 令牌验证+资源访问 |
| 心跳间隔 | 5秒 | PRIMARY发送心跳 |
| 任务分配延迟 | < 50ms | v0.7.3 新增 |
| 状态同步延迟 | < 200ms | v0.7.3 新增 |
| 离线同步吞吐量 | > 100条/秒 | v0.7.3 新增 |

---

## 十一、版本历史

| 版本 | 日期 | 变更说明 |
|------|------|----------|
| v0.7.0 | 2026-02-11 | 初始版本，定义场景组协议 |
| v0.7.2 | 2026-02-15 | 增强密钥生成和心跳机制 |
| v0.7.3 | 2026-02-20 | 新增任务协作、状态同步、离线支持、事件管理 |
