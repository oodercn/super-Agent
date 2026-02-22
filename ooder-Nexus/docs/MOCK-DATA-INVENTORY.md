# Mock 数据与空数据接口清单

## 文档信息

| 项目 | 内容 |
|------|------|
| 创建日期 | 2026-02-22 |
| 版本 | v1.0 |
| 状态 | 待确认 |

---

## 1. 使用 Mock 数据的页面

### 1.1 协议管理模块 (Protocol)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 协作协议 | `/api/protocol/collaboration/*` | SDK 未注入时返回 503 | CollaborationProtocol |
| 发现协议 | `/api/protocol/discovery/*` | SDK 未注入时返回 503 | DiscoveryProtocol |
| 登录协议 | `/api/protocol/login/*` | SDK 未注入时返回 503 | LoginProtocol |
| 观察协议 | `/api/protocol/observation/*` | SDK 未注入时返回 503 | ObservationProtocol |
| 域管理协议 | `/api/protocol/domain/*` | SDK 未注入时返回 503 | DomainManagementProtocol |

### 1.2 管理后台模块 (Admin)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 管理仪表盘 | `/api/admin/dashboard/*` | 依赖 AdminDashboardService | AdminDashboard |
| 技能审核 | `/api/admin/skills/*` | 依赖 SkillManager | SkillManagement |
| 用户管理 | `/api/admin/users/*` | 依赖 UserManager | UserManagement |
| 存储管理 | `/api/admin/storage/*` | 依赖 StorageManager | StorageManagement |
| 远程管理 | `/api/admin/remote/*` | 依赖 RemoteService | RemoteManagement |

### 1.3 安全管理模块 (Security)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 防火墙 | `/api/security/firewall/*` | 依赖 FirewallService | FirewallManagement |
| 访问控制 | `/api/security/access/*` | 依赖 AccessControlService | AccessControl |

### 1.4 个人中心模块 (Personal)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 身份管理 | `/api/personal/identity/*` | 依赖 IdentityService | IdentityManagement |
| 我的分享 | `/api/personal/sharing/*` | 依赖 ShareService | ShareManagement |
| 执行记录 | `/api/personal/execution/*` | 依赖 ExecutionService | ExecutionHistory |

### 1.5 即时通讯模块 (IM)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 消息中心 | `/api/im/*` | 依赖 IMService | InstantMessaging |
| 联系人 | `/api/im/contacts/*` | 依赖 ContactsService | ContactManagement |
| 群组管理 | `/api/im/groups/*` | 依赖 GroupService | GroupManagement |
| 文件管理 | `/api/im/files/*` | 依赖 FileService | FileManagement |

### 1.6 场景管理模块 (Scene)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 场景列表 | `/api/scene/*` | 依赖 SceneService | SceneManagement |
| 场景组 | `/api/scene/group/*` | 依赖 SceneGroupService | SceneGroupManagement |
| 能力约束 | `/api/scene/capability/*` | 依赖 CapabilityService | CapabilityManagement |

### 1.7 协作关系模块 (Collaboration)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 协作关系 | `/api/collaboration/*` | 依赖 CollaborationService | CollaborationManagement |

### 1.8 审计日志模块 (Audit)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 审计日志 | `/api/audit/*` | 依赖 AuditService | AuditLogging |

### 1.9 能力中心模块 (SkillCenter)

| 页面 | API 路径 | 数据状态 | 需要的 Skill |
|------|----------|----------|--------------|
| 技能分类 | `/api/skillcenter/*` | 依赖 SkillCenterService | SkillCenterSync |

---

## 2. Controller 依赖注入情况分析

### 2.1 使用 `@Autowired(required = false)` 的 Controller

这些 Controller 在 SDK 服务未注入时会返回空数据或 503 错误：

| Controller | 依赖服务 | 状态 |
|------------|----------|------|
| LoginProtocolController | LoginProtocol | SDK 未注入返回 503 |
| DiscoveryProtocolController | DiscoveryProtocol | SDK 未注入返回 503 |
| CollaborationProtocolController | CollaborationProtocol | SDK 未注入返回 503 |
| ObservationProtocolController | ObservationProtocol | SDK 未注入返回 503 |
| DomainManagementProtocolController | DomainManagementProtocol | SDK 未注入返回 503 |
| PersonalDashboardController | PersonalDashboardService | 需要实现 |
| AdminDashboardController | AdminDashboardService | 需要实现 |
| CollaborationController | CollaborationService | 需要实现 |
| SceneManagementController | SceneManagementService | 需要实现 |
| InstalledSkillController | InstalledSkillService | 需要实现 |
| SkillDiscoveryController | SkillDiscoveryService | 需要实现 |
| ConfigController | ConfigService | 需要实现 |
| StorageController | StorageService | 需要实现 |
| NetworkDeviceController | NetworkDeviceService | 需要实现 |
| NetworkMonitorController | NetworkMonitorService | 需要实现 |
| DeviceController | DeviceService | 需要实现 |
| OrganizationController | OrganizationService | 需要实现 |
| EndAgentController | EndAgentService | 需要实现 |
| McpAgentController | McpAgentService | 需要实现 |
| RouteController | RouteService | 需要实现 |
| SchedulerController | SchedulerService | 需要实现 |
| MsgController | MsgService | 需要实现 |

---

## 3. Skills 库 API 匹配情况

### 3.1 已有 SDK 实现的服务

| 服务接口 | 实现类 | 状态 |
|----------|--------|------|
| AdminService | AdminServiceSdkImpl | ✅ 已实现 |
| AuthenticationService | AuthenticationServiceSdkImpl | ✅ 已实现 |
| ExecutionService | ExecutionServiceSdkImpl | ✅ 已实现 |
| GroupService | GroupServiceSdkImpl | ✅ 已实现 |
| HostingService | HostingServiceSdkImpl | ✅ 已实现 |
| MarketService | MarketServiceSdk071Impl | ✅ 已实现 |
| NetworkService | NetworkServiceSdkImpl | ✅ 已实现 |
| PersonalService | PersonalServiceSdkImpl | ✅ 已实现 |
| SceneService | SceneServiceSdkImpl | ✅ 已实现 |
| SecurityService | SecurityServiceSdkImpl | ✅ 已实现 |
| ShareService | ShareServiceSdkImpl | ✅ 已实现 |
| SkillService | SkillServiceSdkImpl | ✅ 已实现 |
| StorageService | StorageServiceSdkImpl | ✅ 已实现 |
| SystemService | SystemServiceSdkImpl | ✅ 已实现 |
| UserService | UserServiceSdkImpl | ✅ 已实现 |

### 3.2 需要新增的 Skill/API

| 功能模块 | 需要的 Skill | 优先级 | 说明 |
|----------|--------------|--------|------|
| 登录协议 | LoginProtocol | 高 | 用户认证与授权 |
| 发现协议 | DiscoveryProtocol | 高 | 节点发现与注册 |
| 协作协议 | CollaborationProtocol | 高 | 节点间协作通信 |
| 观察协议 | ObservationProtocol | 中 | 节点状态监控 |
| 域管理协议 | DomainManagementProtocol | 中 | 域策略管理 |
| 审计日志 | AuditService | 高 | 操作审计记录 |
| 防火墙管理 | FirewallService | 中 | 防火墙规则管理 |
| 访问控制 | AccessControlService | 高 | 权限访问控制 |
| 远程管理 | RemoteService | 中 | 远程终端管理 |

---

## 4. 前端 Mock 数据页面清单

以下页面使用前端 Mock 数据或 API 返回空数据：

### 4.1 高优先级（核心功能）

1. **协议管理**
   - `protocol/collaboration.html` - 协作协议
   - `protocol/discovery.html` - 发现协议
   - `protocol/login.html` - 登录协议
   - `protocol/observation.html` - 观察协议
   - `protocol/domain.html` - 域管理协议

2. **管理后台**
   - `admin/dashboard.html` - 管理仪表盘
   - `admin/skills.html` - 技能审核
   - `admin/users.html` - 用户管理

3. **安全管理**
   - `security/firewall.html` - 防火墙
   - `security/access-control.html` - 访问控制

### 4.2 中优先级

1. **个人中心**
   - `personal/identity.html` - 身份管理
   - `personal/sharing.html` - 我的分享
   - `personal/execution.html` - 执行记录

2. **即时通讯**
   - `im/im-main.html` - 消息中心
   - `im/im-contacts.html` - 联系人
   - `im/im-groups.html` - 群组管理
   - `im/im-files.html` - 文件管理

3. **场景管理**
   - `scene/scene-list.html` - 场景列表
   - `scene/scene-group.html` - 场景组
   - `scene/capability-list.html` - 能力约束

### 4.3 低优先级

1. **其他模块**
   - `collaboration/collaboration-relations.html` - 协作关系
   - `audit/audit-logs.html` - 审计日志
   - `config/app-config.html` - 应用配置
   - `monitor/monitor-main.html` - 监控主页

---

## 5. 行动计划

### 5.1 短期目标

1. 完善协议管理模块的 SDK 实现
2. 实现审计日志服务
3. 实现访问控制服务

### 5.2 中期目标

1. 完善管理后台模块服务
2. 实现防火墙管理服务
3. 完善即时通讯模块服务

### 5.3 长期目标

1. 完善所有模块的真实数据支持
2. 移除前端 Mock 数据
3. 统一使用 SDK 服务

---

## 6. 协作需求

### 6.1 需要确认的问题

1. **协议实现优先级**：哪些协议需要优先实现？
2. **数据来源**：真实数据从哪里获取？
3. **Skill 开发**：是否需要开发新的 Skill？

### 6.2 建议的 Skill 开发清单

| Skill 名称 | 功能描述 | 依赖 |
|------------|----------|------|
| audit-skill | 审计日志记录与查询 | 存储服务 |
| firewall-skill | 防火墙规则管理 | 网络服务 |
| access-control-skill | 访问权限控制 | 认证服务 |
| remote-terminal-skill | 远程终端管理 | SSH 服务 |

---

## 附录：相关文件路径

- 前端页面：`src/main/resources/static/console/pages/`
- Controller：`src/main/java/net/ooder/nexus/adapter/inbound/controller/`
- Service 接口：`src/main/java/net/ooder/nexus/service/`
- SDK 实现：`agent-skillcenter/src/main/java/net/ooder/skillcenter/service/impl/`
