# 场景组协议 (Scene Group Protocol)

> 本文档定义了Ooder Agent SDK v0.7.0的场景组协议，包括场景组创建、管理、故障切换和密钥管理。

## 文档信息

- 版本：v0.7.0
- 创建日期：2026-02-15
- 状态：定稿

---

## 一、概述

### 1.1 场景组定义

**场景组（SceneGroup）** 是共享KEY和VFS资源的Agent集合，实现场景的协作和故障切换。

### 1.2 场景组特点

| 特点 | 描述 |
|------|------|
| 共享KEY | 所有成员共享SceneGroupKey |
| 资源共享 | VFS资源访问权限绑定在KEY上 |
| 故障切换 | 支持PRIMARY/BACKUP角色切换 |
| 高可用 | 单点故障不影响服务 |

---

## 二、场景组成员角色

### 2.1 PRIMARY（主角色）

| 属性 | 值 |
|------|-----|
| 定义 | 场景组的主要RouteAgent |
| 职责 | 消息路由、KEY管理、链路维护、资源协调 |
| 状态 | ACTIVE |
| 数量 | 每个场景组1个 |

### 2.2 BACKUP（备角色）

| 属性 | 值 |
|------|-----|
| 定义 | 场景组的备用RouteAgent |
| 职责 | 状态监听、链路同步、准备接管 |
| 状态 | STANDBY |
| 数量 | 每个场景组0-N个 |

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

## 四、场景组创建流程

### 4.1 创建流程

```
1. Route Agent请求创建场景组
   │
   ├── 发送SCENE_GROUP_CREATE请求到MCP Agent
   │   {
   │       "type": "SCENE_GROUP_CREATE",
   │       "agentId": "route-agent-a",
   │       "sceneName": "messaging",
   │       "capabilities": ["msg-send", "msg-receive"],
   │       "vfsPaths": ["data/messages", "cache/sessions"]
   │   }
   │
   ▼
2. MCP Agent创建场景组
   │
   ├── 生成groupId
   ├── 生成masterKey和accessKey
   ├── 使用Shamir算法分割密钥
   ├── 创建SceneGroupKey
   └── 签名SceneGroupKey
   │
   ▼
3. MCP Agent返回SceneGroupKey
   │
   ├── 返回给请求者
   └── 包含keyShare[0]
   │
   ▼
4. Route Agent初始化场景组
   │
   ├── 保存SceneGroupKey
   ├── 启动心跳服务
   └── 广播场景组创建消息
```

### 4.2 创建消息格式

**请求：**
```json
{
    "type": "SCENE_GROUP_CREATE",
    "source": "route-agent-a",
    "target": "mcp-agent",
    "payload": {
        "sceneName": "messaging",
        "capabilities": ["msg-send", "msg-receive"],
        "vfsPaths": ["data/messages", "cache/sessions"],
        "initialMembers": ["route-agent-a"]
    },
    "timestamp": "2026-02-15T10:00:00Z",
    "signature": "..."
}
```

**响应：**
```json
{
    "type": "SCENE_GROUP_CREATE_ACK",
    "source": "mcp-agent",
    "target": "route-agent-a",
    "payload": {
        "groupId": "scene-messaging-001",
        "sceneGroupKey": {
            "groupId": "scene-messaging-001",
            "sceneName": "messaging",
            "version": 1,
            "accessKey": "...",
            "permissions": {...},
            "members": [
                {
                    "agentId": "route-agent-a",
                    "role": "PRIMARY",
                    "keyShare": "..."
                }
            ],
            "createdAt": "2026-02-15T10:00:00Z",
            "expiresAt": "2027-02-15T10:00:00Z",
            "signature": "..."
        }
    },
    "timestamp": "2026-02-15T10:00:01Z",
    "signature": "..."
}
```

---

## 五、场景组加入流程

### 5.1 加入流程

```
1. Route Agent请求加入场景组
   │
   ├── 发送SCENE_GROUP_JOIN请求到PRIMARY
   │   {
   │       "type": "SCENE_GROUP_JOIN",
   │       "agentId": "route-agent-b",
   │       "groupId": "scene-messaging-001",
   │       "role": "BACKUP",
   │       "publicKey": "..."
   │   }
   │
   ▼
2. PRIMARY验证请求
   │
   ├── 验证Agent身份
   ├── 验证权限
   └── 检查场景组容量
   │
   ▼
3. PRIMARY更新SceneGroupKey
   │
   ├── 添加新成员
   ├── 生成新的keyShare
   ├── 更新签名
   └── 广播更新
   │
   ▼
4. 新成员收到SceneGroupKey
   │
   ├── 保存SceneGroupKey
   ├── 开始心跳监听
   └── 同步链路表
```

### 5.2 加入消息格式

**请求：**
```json
{
    "type": "SCENE_GROUP_JOIN",
    "source": "route-agent-b",
    "target": "route-agent-a",
    "payload": {
        "groupId": "scene-messaging-001",
        "role": "BACKUP",
        "publicKey": "...",
        "capabilities": ["msg-send", "msg-receive"]
    },
    "timestamp": "2026-02-15T10:05:00Z",
    "signature": "..."
}
```

**响应：**
```json
{
    "type": "SCENE_GROUP_JOIN_ACK",
    "source": "route-agent-a",
    "target": "route-agent-b",
    "payload": {
        "groupId": "scene-messaging-001",
        "role": "BACKUP",
        "keyShare": "...",
        "routingTable": {...},
        "vfsAccess": {...}
    },
    "timestamp": "2026-02-15T10:05:01Z",
    "signature": "..."
}
```

---

## 六、故障检测与切换

### 6.1 心跳机制

| 参数 | 值 | 说明 |
|------|-----|------|
| 心跳间隔 | 5秒 | PRIMARY发送心跳 |
| 状态报告间隔 | 10秒 | BACKUP发送状态报告 |
| 超时阈值 | 3次 | 连续超时次数 |
| 检测时间 | 15秒 | 故障判定时间 |

### 6.2 故障检测流程

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

### 6.3 故障切换流程

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
```

### 6.4 切换消息格式

**故障检测：**
```json
{
    "type": "FAULT_DETECT",
    "source": "route-agent-b",
    "target": "mcp-agent",
    "payload": {
        "faultyAgent": "route-agent-a",
        "groupId": "scene-messaging-001",
        "detectionTime": "2026-02-15T10:00:15Z",
        "lastHeartbeat": "2026-02-15T09:59:45Z"
    },
    "timestamp": "2026-02-15T10:00:16Z",
    "signature": "..."
}
```

**切换授权：**
```json
{
    "type": "FAILOVER_APPROVE",
    "source": "mcp-agent",
    "target": "route-agent-b",
    "payload": {
        "groupId": "scene-messaging-001",
        "newPrimary": "route-agent-b",
        "keyShares": ["...", "..."],
        "validUntil": "2026-02-15T11:00:00Z"
    },
    "timestamp": "2026-02-15T10:00:17Z",
    "signature": "..."
}
```

**路由更新：**
```json
{
    "type": "ROUTE_UPDATE",
    "source": "route-agent-b",
    "target": "broadcast",
    "payload": {
        "groupId": "scene-messaging-001",
        "newPrimary": "route-agent-b",
        "routingTable": {
            "end-agent-1": {"endpoint": "192.168.1.200:8081"},
            "end-agent-2": {"endpoint": "192.168.1.201:8081"}
        }
    },
    "timestamp": "2026-02-15T10:00:25Z",
    "signature": "..."
}
```

---

## 七、原链路恢复

### 7.1 恢复流程

```
1. 原PRIMARY重新上线
   │
   ├── 发送RECOVERY_NOTIFY
   │
   ▼
2. 当前PRIMARY确认
   │
   ├── 验证原PRIMARY身份
   └── 评估是否切换
   │
   ▼
3. 切换评估
   │
   ├── 资源状态（30%）
   ├── 网络质量（25%）
   ├── 业务连续性（25%）
   └── 历史稳定性（20%）
   │
   ▼
4. 决策执行
   │
   ├── 切换：原PRIMARY接管，当前PRIMARY降级
   └── 不切换：原PRIMARY以BACKUP角色加入
```

### 7.2 切换评估算法

```python
def evaluate_failback(recovered_agent, current_primary):
    score = 0
    
    # 资源状态（30%）
    if recovered_agent.cpu_available > current_primary.cpu_available:
        score += 15
    if recovered_agent.memory_available > current_primary.memory_available:
        score += 15
    
    # 网络质量（25%）
    if recovered_agent.network_latency < current_primary.network_latency:
        score += 25
    
    # 业务连续性（25%）
    if not current_primary.has_active_tasks:
        score += 25
    
    # 历史稳定性（20%）
    if recovered_agent.uptime_ratio > 0.99:
        score += 20
    
    return score >= 70  # 70分以上切换
```

### 7.3 恢复消息格式

```json
{
    "type": "RECOVERY_NOTIFY",
    "source": "route-agent-a",
    "target": "route-agent-b",
    "payload": {
        "groupId": "scene-messaging-001",
        "recoveredAgent": "route-agent-a",
        "requestRole": "BACKUP",
        "syncRequired": true
    },
    "timestamp": "2026-02-15T11:00:00Z",
    "signature": "..."
}
```

---

## 八、VFS资源访问

### 8.1 访问令牌格式

```json
{
    "agentId": "route-agent-b",
    "groupId": "scene-messaging-001",
    "resourceId": "data/messages",
    "permission": "read",
    "timestamp": "2026-02-15T10:00:30Z",
    "expiresAt": "2026-02-15T10:05:30Z",
    "signature": "..."
}
```

### 8.2 权限验证流程

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
   └── 检查资源路径
   │
   ▼
3. 返回结果
   │
   ├── 验证通过：返回资源或执行操作
   └── 验证失败：返回错误
```

---

## 九、性能指标

| 指标 | 要求 | 说明 |
|------|------|------|
| 故障检测时间 | < 15秒 | 心跳超时检测 |
| 切换时间 | < 30秒 | 从检测到服务恢复 |
| VFS访问延迟 | < 100ms | 令牌验证+资源访问 |
| 心跳间隔 | 5秒 | PRIMARY发送心跳 |

---

## 十、版本历史

| 版本 | 日期 | 变更说明 |
|------|------|----------|
| v0.7.0 | 2026-02-15 | 初始版本，定义场景组协议 |
