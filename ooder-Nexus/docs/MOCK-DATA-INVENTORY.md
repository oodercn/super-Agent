# Mock 数据与空数据接口清单

## 文档信息

| 项目 | 内容 |
|------|------|
| 创建日期 | 2026-02-22 |
| 更新日期 | 2026-02-22 |
| 版本 | v2.0 |
| 状态 | 已更新 |

---

## 1. 服务实现状态总览

### 1.1 已完成实现的服务 ✅

| 服务 | 实现类 | API 路径 | 状态 |
|------|--------|----------|------|
| 审计日志 | AuditLogServiceImpl | `/api/audit/*` | ✅ 完整实现 |
| 访问控制 | AccessControlServiceImpl | `/api/security/access/*` | ✅ 完整实现 |
| 防火墙管理 | FirewallController | `/api/firewall/*` | ✅ 完整实现 |
| 登录协议 | LoginProtocolAdapterImpl | `/api/protocol/login/*` | ✅ 完整实现 |
| 发现协议 | DiscoveryProtocolAdapterImpl | `/api/protocol/discovery/*` | ✅ 完整实现 |
| 协作协议 | CollaborationProtocolAdapterImpl | `/api/protocol/collaboration/*` | ✅ 完整实现 |
| 观察协议 | ObservationProtocol | `/api/protocol/observation/*` | ✅ 完整实现 |
| 域管理协议 | DomainManagementProtocol | `/api/protocol/domain/*` | ✅ 完整实现 |
| 管理仪表盘 | AdminDashboardServiceImpl | `/api/admin/dashboard/*` | ✅ 完整实现 |

### 1.2 Skills 库 SDK 实现 ✅

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

---

## 2. 待完善的服务

### 2.1 需要新增 SDK 实现的服务

| 功能模块 | 需要的 Skill | 优先级 | 说明 | Skills库匹配 |
|----------|--------------|--------|------|--------------|
| 远程管理 | RemoteService | 中 | 远程终端管理 | ❌ 无匹配 |
| 防火墙SDK | FirewallServiceSdk | 中 | 防火墙规则SDK | ❌ 无匹配 |
| 访问控制SDK | AccessControlServiceSdk | 高 | 权限访问控制SDK | ❌ 无匹配 |
| 审计日志SDK | AuditServiceSdk | 高 | 操作审计SDK | ❌ 无匹配 |

### 2.2 Controller 依赖注入待完善

| Controller | 依赖服务 | 当前状态 | 建议 |
|------------|----------|----------|------|
| PersonalDashboardController | PersonalDashboardService | 需要实现 | 使用 PersonalService |
| CollaborationController | CollaborationService | 需要实现 | 已有 CollaborationProtocol |
| SceneManagementController | SceneManagementService | 需要实现 | 使用 SceneService |
| InstalledSkillController | InstalledSkillService | 需要实现 | 使用 SkillService |
| SkillDiscoveryController | SkillDiscoveryService | 需要实现 | 使用 MarketService |
| ConfigController | ConfigService | 需要实现 | 使用 SystemService |
| StorageController | StorageService | 需要实现 | ✅ 已有 SDK |
| NetworkDeviceController | NetworkDeviceService | 需要实现 | 使用 NetworkService |
| NetworkMonitorController | NetworkMonitorService | 需要实现 | 使用 NetworkService |
| DeviceController | DeviceService | 需要实现 | 新增实现 |
| OrganizationController | OrganizationService | 需要实现 | 使用 GroupService |
| EndAgentController | EndAgentService | 需要实现 | 新增实现 |
| McpAgentController | McpAgentService | 需要实现 | 新增实现 |
| RouteController | RouteService | 需要实现 | 新增实现 |
| SchedulerController | SchedulerService | 需要实现 | 新增实现 |
| MsgController | MsgService | 需要实现 | 新增实现 |

---

## 3. 前端页面数据状态

### 3.1 已有真实数据支持 ✅

| 页面 | API 路径 | 数据来源 |
|------|----------|----------|
| 审计日志 | `/api/audit/*` | AuditLogServiceImpl |
| 访问控制 | `/api/security/access/*` | AccessControlServiceImpl |
| 防火墙 | `/api/firewall/*` | FirewallController |
| 登录协议 | `/api/protocol/login/*` | LoginProtocolAdapterImpl |
| 发现协议 | `/api/protocol/discovery/*` | DiscoveryProtocolAdapterImpl |
| 协作协议 | `/api/protocol/collaboration/*` | CollaborationProtocolAdapterImpl |
| 观察协议 | `/api/protocol/observation/*` | ObservationProtocol |
| 域管理协议 | `/api/protocol/domain/*` | DomainManagementProtocol |
| 管理仪表盘 | `/api/admin/dashboard/*` | AdminDashboardServiceImpl |

### 3.2 使用 Mock 数据或空数据的页面

| 页面 | API 路径 | 当前状态 | 建议 Skill |
|------|----------|----------|------------|
| 协作关系 | `/api/collaboration/*` | 空数据 | CollaborationService |
| 场景列表 | `/api/scene/*` | 空数据 | SceneService |
| 场景组 | `/api/scene/group/*` | 空数据 | SceneGroupService |
| 能力约束 | `/api/scene/capability/*` | 空数据 | CapabilityService |
| 消息中心 | `/api/im/*` | Mock 数据 | IMService |
| 联系人 | `/api/im/contacts/*` | Mock 数据 | ContactsService |
| 群组管理 | `/api/im/groups/*` | Mock 数据 | GroupService |
| 文件管理 | `/api/im/files/*` | Mock 数据 | FileService |
| 身份管理 | `/api/personal/identity/*` | Mock 数据 | IdentityService |
| 我的分享 | `/api/personal/sharing/*` | Mock 数据 | ShareService |
| 执行记录 | `/api/personal/execution/*` | Mock 数据 | ExecutionService |
| 技能审核 | `/api/admin/skills/*` | Mock 数据 | SkillService |
| 用户管理 | `/api/admin/users/*` | Mock 数据 | UserService |
| 存储管理 | `/api/admin/storage/*` | Mock 数据 | StorageService |
| 远程管理 | `/api/admin/remote/*` | Mock 数据 | RemoteService |

---

## 4. Skills 库 API 检索结果

### 4.1 已匹配的 Skills

| Nexus 服务 | Skills 库 API | 匹配状态 |
|------------|---------------|----------|
| 用户管理 | UserService | ✅ 已有 SDK |
| 技能管理 | SkillService | ✅ 已有 SDK |
| 存储管理 | StorageService | ✅ 已有 SDK |
| 群组管理 | GroupService | ✅ 已有 SDK |
| 网络管理 | NetworkService | ✅ 已有 SDK |
| 安全管理 | SecurityService | ✅ 已有 SDK |
| 场景管理 | SceneService | ✅ 已有 SDK |
| 分享管理 | ShareService | ✅ 已有 SDK |
| 执行管理 | ExecutionService | ✅ 已有 SDK |
| 个人管理 | PersonalService | ✅ 已有 SDK |
| 系统管理 | SystemService | ✅ 已有 SDK |
| 市场管理 | MarketService | ✅ 已有 SDK |
| 托管管理 | HostingService | ✅ 已有 SDK |
| 认证管理 | AuthenticationService | ✅ 已有 SDK |
| 管理后台 | AdminService | ✅ 已有 SDK |

### 4.2 无匹配的 Skills（需要开发）

| 功能 | 建议 Skill 名称 | 优先级 | 说明 |
|------|-----------------|--------|------|
| 远程终端 | remote-terminal-skill | 高 | SSH 远程连接管理 |
| 防火墙SDK | firewall-skill | 中 | 防火墙规则 SDK 封装 |
| 访问控制SDK | access-control-skill | 高 | 权限控制 SDK 封装 |
| 审计日志SDK | audit-skill | 高 | 审计日志 SDK 封装 |
| 即时通讯 | im-skill | 中 | 消息、联系人、群组管理 |
| 设备管理 | device-skill | 低 | 设备资产管理 |
| 调度任务 | scheduler-skill | 低 | 定时任务管理 |
| 路由管理 | route-skill | 低 | 路由配置管理 |

---

## 5. 协作请求

### 5.1 需要开发的 Skills

根据检索结果，以下 Skills 在 Skills 库中没有匹配，需要开发：

#### 高优先级

1. **remote-terminal-skill**
   - 功能：SSH 远程连接管理
   - 依赖：JSch 或 Apache SSHD
   - API：连接、断开、执行命令、文件传输

2. **access-control-skill**
   - 功能：权限访问控制 SDK
   - 依赖：认证服务
   - API：权限检查、角色管理、ACL

3. **audit-skill**
   - 功能：审计日志 SDK
   - 依赖：存储服务
   - API：日志记录、查询、导出

#### 中优先级

4. **firewall-skill**
   - 功能：防火墙规则 SDK
   - 依赖：iptables/nftables
   - API：规则管理、状态查询

5. **im-skill**
   - 功能：即时通讯
   - 依赖：消息队列
   - API：消息收发、联系人、群组

### 5.2 协作文档

已创建协作文档，请 Skills 库团队确认：

1. 是否可以提供上述 Skills？
2. 预计完成时间？
3. 是否需要 Nexus 团队协助开发？

---

## 6. 行动计划

### 6.1 已完成 ✅

- [x] 实现审计日志服务
- [x] 实现访问控制服务
- [x] 完善防火墙控制器
- [x] 验证协议模块实现
- [x] 验证管理仪表盘实现

### 6.2 进行中

- [ ] 完善 Controller 与 SDK 服务的对接
- [ ] 移除前端 Mock 数据提示

### 6.3 待开始

- [ ] 开发 remote-terminal-skill
- [ ] 开发 access-control-skill
- [ ] 开发 audit-skill
- [ ] 开发 firewall-skill
- [ ] 开发 im-skill

---

## 附录：相关文件路径

- 前端页面：`src/main/resources/static/console/pages/`
- Controller：`src/main/java/net/ooder/nexus/adapter/inbound/controller/`
- Service 接口：`src/main/java/net/ooder/nexus/service/`
- Service 实现：`src/main/java/net/ooder/nexus/service/impl/`
- SDK 实现：`agent-skillcenter/src/main/java/net/ooder/skillcenter/service/impl/`
