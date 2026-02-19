# Nexus（个人）角色功能需求规格手册

## 术语表

| 术语 | 英文 | 定义 | 示例 |
|------|------|------|------|
| **Nexus** | Nexus | 用户安装的Agent节点实例 | 用户本地部署的Agent服务 |
| **个人域** | Personal Domain | 用户私有的资源域 | 用户私有技能、私有存储 |
| **雷达模式** | Radar Mode | 无域策略时扫描公共资源的模式 | 公共技能检索、资源发现 |
| **私有资源** | Private Resource | 用户私有的技能、存储、网络资源 | 个人开发的技能、本地存储 |
| **离线运行** | Offline Operation | 无网络连接时的本地运行能力 | 本地技能执行、本地数据访问 |
| **技能** | Skill | Agent可执行的功能模块 | 数据分析技能、文件处理技能 |
| **场景** | Scene | Agent执行任务的上下文环境 | 数据处理场景、协作办公场景 |

## 1. 角色概述

### 1.1 角色定义

Nexus是用户安装的Agent节点实例，代表个人用户在Agent网络中的存在。Nexus角色关注个人资源管理、自主运行和灵活接入。

```
Nexus角色定位
│
├── 个人用户视角
│   ├── 私有资源管理
│   ├── 个人技能使用
│   └── 自主运行决策
│
├── 网络参与者视角
│   ├── 灵活接入组织域
│   ├── 按需加入协作
│   └── 隐私保护优先
│
└── 资源消费者视角
    ├── 公共资源获取
    ├── 技能安装使用
    └── 数据同步存储
```

### 1.2 核心能力

| 能力领域 | 能力描述 | 优先级 |
|----------|----------|--------|
| **自主运行** | 无网络时本地运行，有网络时智能接入 | 高 |
| **资源管理** | 管理私有技能、存储、网络资源 | 高 |
| **灵活接入** | 按需加入组织域或公共域 | 高 |
| **隐私保护** | 私有资源隔离，数据本地优先 | 高 |
| **技能使用** | 安装、使用、分享技能 | 中 |

### 1.3 角色边界

```
┌─────────────────────────────────────────────────────────────────┐
│                    Nexus角色边界                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Nexus负责（职责内）：                                            │
│  ├── 本地资源管理和使用                                          │
│  ├── 个人技能的安装和执行                                        │
│  ├── 本地数据的存储和同步                                        │
│  ├── 网络发现和角色决策                                          │
│  └── 离线模式的自主运行                                          │
│                                                                 │
│  Nexus不负责（职责外）：                                          │
│  ├── 组织域的策略制定（由管理角色负责）                            │
│  ├── 全局网络拓扑管理（由管理角色负责）                            │
│  ├── 其他Nexus的资源分配（由管理角色负责）                         │
│  └── 跨域协作的协调调度（由管理角色负责）                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1.4 能力中心引用

Nexus角色涉及的能力管理功能请参考能力中心规格手册：

| 能力功能 | 规格手册 | 主要职责 |
|----------|----------|----------|
| **能力规范** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 技能/场景的标准化定义和元数据管理 |
| **能力分发** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力从中心分发到目标节点 |
| **能力管理** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力的生命周期管理 |
| **能力监测** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 能力运行状态的实时监测 |
| **能力协同** | [CAPABILITY_CENTER_SPECIFICATION.md](CAPABILITY_CENTER_SPECIFICATION.md) | 多能力之间的协作编排 |

> **注意**：Nexus的技能安装、执行、分享等功能通过能力中心进行能力管理和分发。

## 2. 功能需求

### 2.1 网络发现与接入

#### 2.1.1 功能描述

Nexus启动时自动发现网络环境，根据发现结果决定自身角色和接入策略。

#### 2.1.2 场景闭环

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 | 数据流向 | 观测角度 | 参与者 |
|--------|----------|----------|----------|----------|----------|----------|--------|
| NEX-US-001 | 首次启动无网络 | 新安装Nexus | 启动服务 | 进入离线模式 | 本地配置→本地存储 | 用户观测启动状态 | 用户 |
| NEX-US-002 | 局域网有MCP | 局域网存在MCP | 启动发现 | 注册为RouteAgent | UDP广播→MCP响应 | 用户观测连接状态 | 用户、MCP |
| NEX-US-003 | 局域网无MCP | 局域网无MCP | 启动发现 | 升级为McpAgent | 本地决策→本地初始化 | 用户观测角色状态 | 用户 |
| NEX-US-004 | 组织域接入 | 有组织域邀请 | 接受邀请 | 加入组织域 | 邀请链接→域认证→域策略 | 用户观测域状态 | 用户、组织域 |
| NEX-US-005 | 公共域访问 | 无组织域 | 访问公共域 | 获取公共资源 | 公共域API→本地缓存 | 用户观测资源列表 | 用户、公共域 |

#### 2.1.3 数据流向

```
网络发现数据流
    │
    ├─→ 配置读取
    │   ├── 本地配置文件 → 发现参数
    │   └── 历史连接记录 → 已知节点列表
    │
    ├─→ 网络探测
    │   ├── UDP广播 → 局域网节点响应
    │   ├── HTTP查询 → 组织域节点列表
    │   └── 配置文件 → 预配置节点列表
    │
    ├─→ 角色决策
    │   ├── 发现结果 → 角色决策算法 → 角色类型
    │   └── 角色类型 → 初始化流程
    │
    └─→ 状态存储
        ├── 角色状态 → 本地持久化
        └── 连接信息 → 本地缓存
```

#### 2.1.4 观测角度

| 观测点 | 观测内容 | 观测方式 |
|--------|----------|----------|
| 启动日志 | 发现过程、角色决策 | 本地日志文件 |
| 网络状态 | 连接状态、延迟 | 状态面板 |
| 节点列表 | 已发现节点信息 | 节点管理界面 |

#### 2.1.5 参与者

| 参与者 | 角色 | 职责 |
|--------|------|------|
| 用户 | 操作者 | 启动Nexus、配置参数 |
| MCP节点 | 网络节点 | 响应发现请求、接受注册 |
| 组织域 | 网络域 | 提供域策略、管理域成员 |

### 2.2 登录与域策略响应

#### 2.2.1 功能描述

Nexus支持用户登录，根据用户所属域自动响应域策略配置。

#### 2.2.2 场景闭环

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 | 数据流向 | 观测角度 | 参与者 |
|--------|----------|----------|----------|----------|----------|----------|--------|
| NEX-LO-001 | 用户主动登录 | 有账号 | 输入凭证登录 | 获取用户信息和域策略 | 凭证→认证服务→令牌+策略 | 用户观测登录状态 | 用户、认证服务 |
| NEX-LO-002 | 自动登录 | 配置保存凭证 | 启动时自动登录 | 自动获取令牌和策略 | 配置→认证服务→令牌+策略 | 用户观测自动登录 | 用户 |
| NEX-LO-003 | 有域策略响应 | 登录成功有域 | 自动应用策略 | 配置存储、技能中心 | 策略→本地配置→服务连接 | 用户观测配置进度 | 用户、域服务 |
| NEX-LO-004 | 无域策略雷达 | 登录成功无域 | 开启雷达模式 | 扫描公共资源 | 公共域API→资源列表 | 用户观测资源发现 | 用户、公共域 |
| NEX-LO-005 | 登出清理 | 已登录 | 用户登出 | 清理敏感数据 | 本地清理→状态重置 | 用户观测登出状态 | 用户 |

#### 2.2.3 数据流向

```
登录与策略响应数据流
    │
    ├─→ 登录认证
    │   ├── 用户凭证 → 认证服务 → 用户令牌
    │   └── 用户令牌 → 用户信息 + 域列表
    │
    ├─→ 域策略获取
    │   ├── 有域 → 域策略请求 → 策略详情
    │   └── 无域 → 雷达模式启动
    │
    ├─→ 策略应用
    │   ├── 存储策略 → 存储配置 → 存储连接
    │   ├── 技能策略 → 技能列表 → 技能安装
    │   └── 安全策略 → 安全配置 → 权限设置
    │
    └─→ 状态持久化
        ├── 用户会话 → 本地存储
        └── 策略状态 → 本地缓存
```

#### 2.2.4 观测角度

| 观测点 | 观测内容 | 观测方式 |
|--------|----------|----------|
| 登录状态 | 登录进度、结果 | 状态面板 |
| 策略应用 | 配置进度、成功/失败 | 配置日志 |
| 资源发现 | 雷达扫描结果 | 资源列表界面 |

#### 2.2.5 参与者

| 参与者 | 角色 | 职责 |
|--------|------|------|
| 用户 | 操作者 | 提供凭证、确认配置 |
| 认证服务 | 服务提供者 | 验证凭证、颁发令牌 |
| 域服务 | 策略提供者 | 提供域策略、管理域资源 |

### 2.3 私有资源管理

#### 2.3.1 功能描述

Nexus管理用户的私有资源，包括私有技能、私有存储、私有网络配置。

#### 2.3.2 场景闭环

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 | 数据流向 | 观测角度 | 参与者 |
|--------|----------|----------|----------|----------|----------|----------|--------|
| NEX-PR-001 | 私有技能安装 | 有技能包 | 安装技能 | 技能可用 | 技能包→本地安装→技能注册 | 用户观测安装进度 | 用户 |
| NEX-PR-002 | 私有技能执行 | 技能已安装 | 执行技能 | 执行结果 | 执行请求→技能引擎→结果 | 用户观测执行状态 | 用户 |
| NEX-PR-003 | 私有存储配置 | 有存储资源 | 配置存储 | 存储可用 | 存储配置→连接测试→可用状态 | 用户观测存储状态 | 用户 |
| NEX-PR-004 | 私有数据存储 | 存储已配置 | 存储数据 | 数据持久化 | 数据→存储服务→存储确认 | 用户观测存储结果 | 用户 |
| NEX-PR-005 | 私有资源分享 | 有私有资源 | 分享资源 | 生成分享链接 | 资源→分享配置→分享链接 | 用户观测分享状态 | 用户、目标用户 |

#### 2.3.3 数据流向

```
私有资源管理数据流
    │
    ├─→ 技能管理
    │   ├── 技能包 → 解析验证 → 本地安装
    │   ├── 技能注册 → 技能列表 → 技能可用
    │   └── 执行请求 → 技能引擎 → 执行结果
    │
    ├─→ 存储管理
    │   ├── 存储配置 → 连接验证 → 存储可用
    │   ├── 数据写入 → 存储服务 → 写入确认
    │   └── 数据读取 → 存储服务 → 数据返回
    │
    └─→ 分享管理
        ├── 资源选择 → 分享配置 → 分享链接
        └── 访问控制 → 权限验证 → 访问结果
```

#### 2.3.4 观测角度

| 观测点 | 观测内容 | 观测方式 |
|--------|----------|----------|
| 技能状态 | 安装、执行、错误 | 技能管理界面 |
| 存储状态 | 连接、容量、IO | 存储管理界面 |
| 分享状态 | 分享列表、访问记录 | 分享管理界面 |

#### 2.3.5 参与者

| 参与者 | 角色 | 职责 |
|--------|------|------|
| 用户 | 资源所有者 | 安装、配置、使用资源 |
| 技能引擎 | 执行环境 | 执行技能、返回结果 |
| 存储服务 | 存储提供者 | 存储数据、返回数据 |

### 2.4 离线运行

#### 2.4.1 功能描述

Nexus在无网络连接时能够继续运行，使用本地资源执行任务。

#### 2.4.2 场景闭环

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 | 数据流向 | 观测角度 | 参与者 |
|--------|----------|----------|----------|----------|----------|----------|--------|
| NEX-OFF-001 | 网络断开检测 | 正在运行 | 网络断开 | 进入离线模式 | 网络检测→状态切换→离线模式 | 用户观测网络状态 | 用户 |
| NEX-OFF-002 | 离线技能执行 | 离线模式 | 执行本地技能 | 正常执行 | 执行请求→本地引擎→结果 | 用户观测执行状态 | 用户 |
| NEX-OFF-003 | 离线数据访问 | 离线模式 | 访问本地数据 | 正常访问 | 数据请求→本地缓存→数据返回 | 用户观测数据状态 | 用户 |
| NEX-OFF-004 | 网络恢复检测 | 离线模式 | 网络恢复 | 退出离线模式 | 网络检测→状态切换→在线模式 | 用户观测网络状态 | 用户 |
| NEX-OFF-005 | 数据同步恢复 | 网络恢复 | 自动同步 | 数据一致性 | 本地变更→同步服务→同步确认 | 用户观测同步状态 | 用户、同步服务 |

#### 2.4.3 数据流向

```
离线运行数据流
    │
    ├─→ 网络状态监测
    │   ├── 心跳检测 → 网络状态
    │   └── 状态变化 → 模式切换
    │
    ├─→ 离线模式运行
    │   ├── 本地技能 → 本地执行 → 结果
    │   ├── 本地数据 → 本地访问 → 数据
    │   └── 本地变更 → 变更日志 → 待同步队列
    │
    └─→ 恢复同步
        ├── 网络恢复 → 同步触发
        ├── 待同步队列 → 同步服务 → 同步结果
        └── 同步结果 → 本地状态更新
```

#### 2.4.4 观测角度

| 观测点 | 观测内容 | 观测方式 |
|--------|----------|----------|
| 网络状态 | 在线/离线、延迟 | 状态面板 |
| 离线能力 | 可用技能、可用数据 | 离线模式界面 |
| 同步状态 | 待同步项、同步进度 | 同步管理界面 |

#### 2.4.5 参与者

| 参与者 | 角色 | 职责 |
|--------|------|------|
| 用户 | 操作者 | 使用离线功能、确认同步 |
| 本地引擎 | 执行环境 | 离线执行技能 |
| 同步服务 | 同步提供者 | 网络恢复后同步数据 |

### 2.5 协作参与

#### 2.5.1 功能描述

Nexus作为参与者加入组织域或场景组，参与协作任务。

#### 2.5.2 场景闭环

| 场景ID | 场景名称 | 前置条件 | 触发动作 | 预期结果 | 数据流向 | 观测角度 | 参与者 |
|--------|----------|----------|----------|----------|----------|----------|--------|
| NEX-CO-001 | 加入场景组 | 有邀请 | 接受邀请 | 加入成功 | 邀请→确认→组注册 | 用户观测组状态 | 用户、场景组 |
| NEX-CO-002 | 接收协作任务 | 已加入组 | 接收任务 | 任务执行 | 任务→本地执行→结果 | 用户观测任务状态 | 用户、任务调度 |
| NEX-CO-003 | 提交协作结果 | 任务完成 | 提交结果 | 结果确认 | 结果→组服务→确认 | 用户观测提交状态 | 用户、组服务 |
| NEX-CO-004 | 退出场景组 | 已加入组 | 退出请求 | 退出成功 | 退出请求→组注销→确认 | 用户观测组状态 | 用户、场景组 |
| NEX-CO-005 | 协作状态同步 | 协作中 | 定期同步 | 状态一致 | 本地状态→组服务→状态更新 | 用户观测同步状态 | 用户、组服务 |

#### 2.5.3 数据流向

```
协作参与数据流
    │
    ├─→ 组加入
    │   ├── 邀请链接 → 验证 → 加入确认
    │   └── 组信息 → 本地缓存 → 组可用
    │
    ├─→ 任务执行
    │   ├── 任务接收 → 本地解析 → 执行计划
    │   ├── 执行计划 → 本地执行 → 执行结果
    │   └── 执行结果 → 结果提交 → 提交确认
    │
    └─→ 状态同步
        ├── 本地状态 → 组服务 → 状态更新
        └── 组状态 → 本地更新 → 状态一致
```

#### 2.5.4 观测角度

| 观测点 | 观测内容 | 观测方式 |
|--------|----------|----------|
| 组状态 | 成员列表、任务列表 | 组管理界面 |
| 任务状态 | 进度、结果、错误 | 任务管理界面 |
| 协作日志 | 操作记录、通信记录 | 协作日志界面 |

#### 2.5.5 参与者

| 参与者 | 角色 | 职责 |
|--------|------|------|
| 用户 | 协作参与者 | 执行任务、提交结果 |
| 场景组 | 协作环境 | 分配任务、收集结果 |
| 组服务 | 协调服务 | 管理组状态、协调成员 |

## 3. 接口规格

### 3.1 Nexus主接口

```java
package net.ooder.sdk.nexus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NexusService {
    
    CompletableFuture<NexusStatus> start(NexusConfig config);
    
    CompletableFuture<Void> stop();
    
    CompletableFuture<NexusStatus> getStatus();
    
    CompletableFuture<Void> login(LoginRequest request);
    
    CompletableFuture<Void> logout();
    
    CompletableFuture<UserSession> getCurrentSession();
    
    CompletableFuture<List<PeerInfo>> discoverPeers();
    
    CompletableFuture<RoleDecision> getCurrentRole();
    
    CompletableFuture<Void> joinSceneGroup(String groupId);
    
    CompletableFuture<Void> leaveSceneGroup(String groupId);
    
    CompletableFuture<List<SceneGroup>> listSceneGroups();
}
```

### 3.2 私有资源接口

```java
package net.ooder.sdk.nexus.resource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PrivateResourceService {
    
    CompletableFuture<Void> installSkill(SkillPackage skillPackage);
    
    CompletableFuture<Void> uninstallSkill(String skillId);
    
    CompletableFuture<List<SkillInfo>> listInstalledSkills();
    
    CompletableFuture<SkillResult> executeSkill(String skillId, Map<String, Object> params);
    
    CompletableFuture<Void> configureStorage(StorageConfig config);
    
    CompletableFuture<StorageStatus> getStorageStatus();
    
    CompletableFuture<Void> storeData(String key, byte[] data);
    
    CompletableFuture<byte[]> retrieveData(String key);
    
    CompletableFuture<ShareLink> createShareLink(String resourceId, ShareConfig config);
    
    CompletableFuture<Void> revokeShareLink(String shareId);
}
```

### 3.3 离线运行接口

```java
package net.ooder.sdk.nexus.offline;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OfflineService {
    
    CompletableFuture<Void> enableOfflineMode();
    
    CompletableFuture<Void> disableOfflineMode();
    
    boolean isOfflineMode();
    
    CompletableFuture<List<OfflineCapability>> getOfflineCapabilities();
    
    CompletableFuture<List<PendingSync>> getPendingSyncItems();
    
    CompletableFuture<SyncResult> syncNow();
    
    void addNetworkListener(NetworkStateListener listener);
    
    void addSyncListener(SyncStateListener listener);
}

public class OfflineCapability {
    
    private String capabilityType;
    private String name;
    private boolean available;
    private String description;
    
    public enum CapabilityType {
        SKILL,      // 可离线执行的技能
        DATA,       // 可离线访问的数据
        STORAGE     // 可离线使用的存储
    }
}

public class PendingSync {
    
    private String syncId;
    private SyncType type;
    private String resourceKey;
    private long createdTime;
    private int retryCount;
    
    public enum SyncType {
        DATA_UPLOAD,    // 数据上传
        DATA_DOWNLOAD,  // 数据下载
        STATE_SYNC      // 状态同步
    }
}
```

## 4. 配置规格

### 4.1 Nexus主配置

```properties
ooder.nexus.node-id=nexus-001
ooder.nexus.node-name=MyNexus
ooder.nexus.auto-start=true
ooder.nexus.auto-login=true
ooder.nexus.offline-enabled=true
ooder.nexus.discovery-timeout=5000
```

### 4.2 资源配置

```properties
ooder.nexus.skill.install-path=./skills
ooder.nexus.skill.max-installed=100
ooder.nexus.storage.local-path=./storage
ooder.nexus.storage.max-size=10GB
ooder.nexus.share.expiration=86400
```

### 4.3 网络配置

```properties
ooder.nexus.network.discovery.udp-enabled=true
ooder.nexus.network.discovery.udp-port=9001
ooder.nexus.network.discovery.http-endpoints=
ooder.nexus.network.offline-check-interval=5000
ooder.nexus.network.sync-interval=30000
```

## 5. 闭环验证

### 5.1 验证场景汇总

| 场景组 | 场景数量 | 验证重点 |
|--------|----------|----------|
| 网络发现与接入 | 5 | 发现机制、角色决策、接入流程 |
| 登录与策略响应 | 5 | 认证流程、策略应用、雷达模式 |
| 私有资源管理 | 5 | 技能管理、存储管理、分享管理 |
| 离线运行 | 5 | 离线检测、离线执行、同步恢复 |
| 协作参与 | 5 | 组加入、任务执行、状态同步 |

### 5.2 验证实现

```java
package net.ooder.sdk.nexus.validation;

public class NexusClosedLoopValidation {
    
    private NexusService nexusService;
    private PrivateResourceService resourceService;
    private OfflineService offlineService;
    
    public CompletableFuture<ValidationReport> validate() {
        return CompletableFuture.supplyAsync(() -> {
            ValidationReport report = new ValidationReport("Nexus Role Validation");
            
            report.addResult(validateScenario_NEX_US_001());
            report.addResult(validateScenario_NEX_US_002());
            report.addResult(validateScenario_NEX_LO_001());
            report.addResult(validateScenario_NEX_PR_001());
            report.addResult(validateScenario_NEX_OFF_001());
            report.addResult(validateScenario_NEX_CO_001());
            
            return report;
        });
    }
    
    private ValidationResult validateScenario_NEX_US_001() {
        String scenarioId = "NEX-US-001";
        String scenarioName = "首次启动无网络";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("新安装Nexus，无网络连接")
            .when("启动Nexus服务")
            .then("应进入离线模式，本地资源可用")
            .validate(() -> {
                NexusConfig config = new NexusConfig();
                config.setNodeId("nexus-test-001");
                
                NexusStatus status = nexusService.start(config).join();
                
                if (status.getMode() != NexusMode.OFFLINE) {
                    return ValidationFailure.of(scenarioId, 
                        "未进入离线模式，当前模式: " + status.getMode());
                }
                
                List<OfflineCapability> capabilities = 
                    offlineService.getOfflineCapabilities().join();
                
                if (capabilities.isEmpty()) {
                    return ValidationFailure.of(scenarioId, "无可用离线能力");
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "成功进入离线模式，可用能力: " + capabilities.size());
            });
    }
    
    private ValidationResult validateScenario_NEX_PR_001() {
        String scenarioId = "NEX-PR-001";
        String scenarioName = "私有技能安装";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("有有效的技能包")
            .when("安装技能")
            .then("技能应成功安装并可执行")
            .validate(() -> {
                SkillPackage skillPackage = createTestSkillPackage();
                
                resourceService.installSkill(skillPackage).join();
                
                List<SkillInfo> skills = resourceService.listInstalledSkills().join();
                
                if (skills.stream().noneMatch(s -> s.getSkillId().equals(skillPackage.getSkillId()))) {
                    return ValidationFailure.of(scenarioId, "技能未出现在已安装列表中");
                }
                
                SkillResult result = resourceService.executeSkill(
                    skillPackage.getSkillId(), 
                    Map.of("test", "data")
                ).join();
                
                if (!result.isSuccess()) {
                    return ValidationFailure.of(scenarioId, 
                        "技能执行失败: " + result.getErrorMessage());
                }
                
                return ValidationSuccess.of(scenarioId, 
                    "技能安装并执行成功，结果: " + result.getData());
            });
    }
    
    private ValidationResult validateScenario_NEX_OFF_001() {
        String scenarioId = "NEX-OFF-001";
        String scenarioName = "网络断开检测";
        
        return ValidationResult.scenario(scenarioId, scenarioName)
            .given("Nexus正在运行")
            .when("网络断开")
            .then("应检测到网络断开并进入离线模式")
            .validate(() -> {
                boolean offlineDetected = false;
                CountDownLatch latch = new CountDownLatch(1);
                
                offlineService.addNetworkListener((oldState, newState) -> {
                    if (newState == NetworkState.OFFLINE) {
                        offlineDetected = true;
                        latch.countDown();
                    }
                });
                
                simulateNetworkDisconnect();
                
                boolean awaited = latch.await(10, TimeUnit.SECONDS);
                
                if (!awaited) {
                    return ValidationFailure.of(scenarioId, "未在超时时间内检测到离线");
                }
                
                if (!offlineDetected) {
                    return ValidationFailure.of(scenarioId, "未检测到离线状态");
                }
                
                if (!offlineService.isOfflineMode()) {
                    return ValidationFailure.of(scenarioId, "未进入离线模式");
                }
                
                return ValidationSuccess.of(scenarioId, "成功检测到网络断开并进入离线模式");
            });
    }
}
```

## 6. 总结

Nexus（个人）角色功能需求规格定义了个人用户Agent的核心能力：

1. **自主运行**：无网络时本地运行，有网络时智能接入
2. **资源管理**：管理私有技能、存储、网络资源
3. **灵活接入**：按需加入组织域或公共域
4. **隐私保护**：私有资源隔离，数据本地优先
5. **协作参与**：作为参与者加入协作任务

---

**Ooder Agent SDK 0.7.2** - 构建智能、协作、安全的Agent生态系统！
