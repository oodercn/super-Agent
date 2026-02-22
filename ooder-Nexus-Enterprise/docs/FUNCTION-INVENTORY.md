# Nexus 功能摸底文档

**文档版本**: 1.0  
**更新日期**: 2026-02-22  
**检查范围**: 菜单配置、页面文件、后端API

---

## 1. 概述

本文档对 Nexus 控制台的三层架构进行摸底检查：
- **菜单配置**: `menu-config.json`
- **页面文件**: `src/main/resources/static/console/pages/`
- **后端API**: `src/main/java/` 中的 Controller

---

## 2. 菜单配置与页面匹配情况

### 2.1 菜单配置中的页面（55项）- 全部存在 ✓

| 菜单ID | 菜单名称 | 页面路径 | 状态 |
|--------|---------|---------|------|
| dashboard | 工作台 | /console/pages/admin/dashboard.html | ✓ 存在 |
| nexus-instances | Nexus节点 | /console/pages/enexus/nexus-instances.html | ✓ 存在 |
| mcp-agents | MCP Agent | /console/pages/enexus/mcp-agents.html | ✓ 存在 |
| capability-registry | 能力服务 | /console/pages/enexus/capability-registry.html | ✓ 存在 |
| network-nodes | 网络节点 | /console/pages/nexus/network-nodes.html | ✓ 存在 |
| link-management | 链路管理 | /console/pages/nexus/link-management.html | ✓ 存在 |
| storage-types | 存储类型 | /console/pages/storage/storage-types.html | ✓ 存在 |
| file-browser | 文件浏览 | /console/pages/storage/file-browser.html | ✓ 存在 |
| capability-monitor | 能力监控 | /console/pages/storage/capability-monitor.html | ✓ 存在 |
| cleanup-policy | 清理策略 | /console/pages/storage/cleanup-policy.html | ✓ 存在 |
| user-keys | 用户密钥 | /console/pages/enexus/security/user-keys.html | ✓ 存在 |
| domain-keys | 域密钥 | /console/pages/enexus/security/domain-keys.html | ✓ 存在 |
| scene-group-keys | 场景组密钥 | /console/pages/enexus/security/scene-group-keys.html | ✓ 存在 |
| certificates | 数字证书 | /console/pages/enexus/security/certificates.html | ✓ 存在 |
| tokens | 访问令牌 | /console/pages/enexus/security/tokens.html | ✓ 存在 |
| relay-commands | 命令转发 | /console/pages/enexus/communication/relay-commands.html | ✓ 存在 |
| p2p-approval | P2P审批 | /console/pages/enexus/communication/p2p-approval.html | ✓ 存在 |
| p2p-monitor | P2P监控 | /console/pages/enexus/communication/p2p-monitor.html | ✓ 存在 |
| permission-rules | 权限规则 | /console/pages/enexus/communication/permission-rules.html | ✓ 存在 |
| msg-push | 消息推送 | /console/pages/msgcmd/msg-push.html | ✓ 存在 |
| msg-manager | 消息管理 | /console/pages/msgcmd/msg-manager.html | ✓ 存在 |
| topic-manager | Topic管理 | /console/pages/msgcmd/topic-manager.html | ✓ 存在 |
| cmd-manager | 命令发送 | /console/pages/msgcmd/cmd-manager.html | ✓ 存在 |
| cmd-monitor | 执行监控 | /console/pages/msgcmd/cmd-monitor.html | ✓ 存在 |
| im-conversation | 会话列表 | /console/pages/im/conversation-list.html | ✓ 存在 |
| im-contact | 联系人 | /console/pages/im/contact-list.html | ✓ 存在 |
| im-group | 群组管理 | /console/pages/im/group-list.html | ✓ 存在 |
| business-category | 业务分类 | /console/pages/im/business-category.html | ✓ 存在 |
| business-scene | 业务场景 | /console/pages/im/business-scene.html | ✓ 存在 |
| org-structure | 组织架构 | /console/pages/im/org-structure.html | ✓ 存在 |
| monitor-dashboard | 监控仪表盘 | /console/pages/monitor/monitor-dashboard.html | ✓ 存在 |
| topology-monitor | 网络拓扑 | /console/pages/observation/topology-monitor.html | ✓ 存在 |
| routing-observation | 路由观测 | /console/pages/observation/routing-observation.html | ✓ 存在 |
| resource-monitor | 资源监控 | /console/pages/nexus/system-status.html | ✓ 存在 |
| health-check | 健康检查 | /console/pages/nexus/health-check.html | ✓ 存在 |
| history-logs | 历史日志 | /console/pages/observation/history-logs.html | ✓ 存在 |
| scene-list | 场景列表 | /console/pages/scene/list.html | ✓ 存在 |
| scene-dashboard | 运行监控 | /console/pages/scene/dashboard.html | ✓ 存在 |
| scene-flow | 启动流程 | /console/pages/scene/flow-tracker.html | ✓ 存在 |
| scene-config | 配置编辑 | /console/pages/scene/config-editor.html | ✓ 存在 |
| scene-history | 配置历史 | /console/pages/scene/config-history.html | ✓ 存在 |
| datasource-manager | 数据源配置 | /console/pages/datasource/datasource-manager.html | ✓ 存在 |
| datasource-sync | 同步管理 | /console/pages/datasource/sync-manager.html | ✓ 存在 |
| collab-requests | 协作请求 | /console/pages/collaboration/collab-requests.html | ✓ 存在 |
| task-distribution | 任务分配 | /console/pages/collaboration/task-distribution.html | ✓ 存在 |
| status-tracking | 状态跟踪 | /console/pages/collaboration/status-tracking.html | ✓ 存在 |
| anomaly-detection | 异常检测 | /console/pages/anomaly/anomaly-detection.html | ✓ 存在 |
| auto-correction | 自动纠正 | /console/pages/anomaly/auto-correction.html | ✓ 存在 |
| manual-intervention | 手动干预 | /console/pages/anomaly/manual-intervention.html | ✓ 存在 |
| org-management | 组织架构 | /console/pages/enterprise/org-management.html | ✓ 存在 |
| session-management | 会话管理 | /console/pages/system/session-management.html | ✓ 存在 |
| cache-management | 缓存管理 | /console/pages/system/cache-management.html | ✓ 存在 |
| init-package | 初始化配置 | /console/pages/enexus/init-package.html | ✓ 存在 |
| policy-config | 策略配置 | /console/pages/nexus/config-management.html | ✓ 存在 |
| app-config | 应用设置 | /console/pages/config/app-config.html | ✓ 存在 |

---

## 3. 未纳入菜单的页面（待处理）

### 3.1 技能中心相关（12个页面）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| skillcenter/remote-install.html | 远程安装 | 可纳入菜单 |
| skillcenter/installed-skills.html | 已安装技能 | 可纳入菜单 |
| skillcenter/my-sharing.html | 我的分享 | 可纳入菜单 |
| skillcenter/my-groups.html | 我的群组 | 可纳入菜单 |
| skillcenter/admin/* (7个) | 技能中心管理 | 管理员菜单 |
| skillcenter/personal/* (5个) | 个人技能中心 | 个人菜单 |
| skillcenter-sync/* (5个) | 技能同步 | 可纳入菜单 |

### 3.2 LLM 相关（6个页面）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| llm/llm-chat.html | LLM对话 | 可纳入菜单 |
| llm/llm-functions.html | LLM函数 | 可纳入菜单 |
| llm/llm-embed.html | LLM嵌入 | 可纳入菜单 |
| llm-integration/* (5个) | LLM集成 | 可纳入菜单 |
| nexus/llm-management.html | LLM管理 | 可纳入菜单 |

### 3.3 网络管理相关（14个页面）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| network/traffic-monitor.html | 流量监控 | 可纳入菜单 |
| network/remote-terminal.html | 远程终端 | 可纳入菜单 |
| network/network-overview.html | 网络概览 | 可纳入菜单 |
| network/network-config.html | 网络配置 | 可纳入菜单 |
| network/ip-management.html | IP管理 | 可纳入菜单 |
| network/device-monitor.html | 设备监控 | 可纳入菜单 |
| network/device-assets.html | 设备资产 | 可纳入菜单 |
| network/config-management.html | 配置管理 | 可纳入菜单 |
| network/access-control.html | 访问控制 | 可纳入菜单 |
| nexus/route-management.html | 路由管理 | 可纳入菜单 |
| nexus/protocol-management.html | 协议管理 | 可纳入菜单 |
| nexus/p2p-visualization.html | P2P可视化 | 可纳入菜单 |
| nexus/p2p-management.html | P2P管理 | 可纳入菜单 |
| nexus/network-topology.html | 网络拓扑 | 可纳入菜单 |

### 3.4 局域网/家庭相关（9个页面）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| lan/lan-dashboard.html | 局域网仪表盘 | 局域网模式 |
| lan/network-status.html | 网络状态 | 局域网模式 |
| lan/network-settings.html | 网络设置 | 局域网模式 |
| lan/network-devices.html | 网络设备 | 局域网模式 |
| lan/ip-management.html | IP管理 | 局域网模式 |
| lan/device-details.html | 设备详情 | 局域网模式 |
| lan/bandwidth-monitor.html | 带宽监控 | 局域网模式 |
| home/security-status.html | 家庭安全 | 家庭模式 |
| dashboard.html | 旧仪表盘 | 可废弃 |

### 3.5 OpenWrt 相关（9个页面）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| openwrt/system-status.html | 系统状态 | OpenWrt设备 |
| openwrt/router-dashboard.html | 路由仪表盘 | OpenWrt设备 |
| openwrt/network-settings.html | 网络设置 | OpenWrt设备 |
| openwrt/network-management.html | 网络管理 | OpenWrt设备 |
| openwrt/ip-management.html | IP管理 | OpenWrt设备 |
| openwrt/config-files.html | 配置文件 | OpenWrt设备 |
| openwrt/command.html | 命令执行 | OpenWrt设备 |
| openwrt/blacklist.html | 黑名单 | OpenWrt设备 |
| openwrt/metrics.html | 指标监控 | OpenWrt设备 |

### 3.6 其他页面（10个）

| 页面路径 | 说明 | 建议 |
|---------|------|------|
| storage/storage-config.html | 存储配置 | 可合并 |
| storage/storage-management.html | 存储管理 | 可合并 |
| storage/shared-files.html | 共享文件 | 可纳入菜单 |
| storage/received-files.html | 接收文件 | 可纳入菜单 |
| nexus/security-management.html | 安全管理 | 可纳入菜单 |
| nexus/log-management.html | 日志管理 | 可纳入菜单 |
| nexus/endroute.html | 端路由 | 可纳入菜单 |
| nexus/endagent-management.html | 端代理管理 | 可纳入菜单 |
| nexus/capability-management.html | 能力管理 | 可纳入菜单 |
| nexus/dashboard.html | Nexus仪表盘 | 可纳入菜单 |
| enterprise/vfs-management.html | VFS管理 | 可纳入菜单 |
| task/data-extract-tasks.html | 数据提取任务 | 可纳入菜单 |
| security/security-center.html | 安全中心 | 可纳入菜单 |
| monitoring/health-check.html | 健康检查 | 重复页面 |

---

## 4. 后端 Controller 清单（103个）

### 4.1 核心业务 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| NexusInstanceController | /api/nexus/instances | Nexus节点管理 |
| EnexusMcpAgentController | /api/mcp/agents | MCP Agent管理 |
| CapabilityRegistryController | /api/capability | 能力注册 |
| SecurityKeyController | /api/security/keys | 密钥管理 |
| P2PApprovalController | /api/p2p/approval | P2P审批 |
| P2PMonitorController | /api/p2p/monitor | P2P监控 |
| PermissionRuleController | /api/permission/rules | 权限规则 |
| CommunicationController | /api/communication | 通信管理 |

### 4.2 消息命令 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| MsgController | /api/msg | 消息管理 |
| CmdController | /api/cmd | 命令管理 |
| TopicController | /api/topic | Topic管理 |
| ConversationController | /api/conversation | 会话管理 |
| ContactController | /api/contact | 联系人管理 |

### 4.3 场景管理 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| SceneSdk73Controller | /api/scene | 场景管理(SDK 0.7.3) |
| SceneVisualizationController | /api/scene/vis | 场景可视化 |
| SceneConfigController | /api/scene/config | 场景配置 |
| SceneRuntimeController | /api/scene/runtime | 场景运行时 |
| SceneFlowController | /api/scene/flow | 场景流程 |

### 4.4 存储管理 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| VfsStorageController | /api/storage/vfs | VFS存储 |
| StorageController | /api/storage | 存储管理 |
| StorageVisualizationController | /api/storage/vis | 存储可视化 |

### 4.5 监控运维 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| MonitorController | /api/monitor | 监控管理 |
| MonitorVisualizationController | /api/monitor/vis | 监控可视化 |
| HealthCheckController | /api/health | 健康检查 |
| ObservationController | /api/observation | 观测管理 |
| SystemMonitorController | /api/system/monitor | 系统监控 |
| SystemStatusController | /api/system/status | 系统状态 |

### 4.6 协作协调 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| CollaborationController | /api/collaboration | 协作管理 |
| AnomalyController | /api/anomaly | 异常处理 |

### 4.7 技能中心 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| SkillPackageController | /api/skill/package | 技能包管理 |
| SkillDiscoveryController | /api/skill/discovery | 技能发现 |
| InstalledSkillController | /api/skill/installed | 已安装技能 |
| SkillConfigController | /api/skill/config | 技能配置 |
| SkillCenterSyncController | /api/skill/sync | 技能同步 |
| RemoteSkillInstallController | /api/skill/remote | 远程安装 |
| GroupController | /api/group | 群组管理 |
| ShareController | /api/share | 分享管理 |

### 4.8 网络 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| NetworkController | /api/network | 网络管理 |
| NetworkLinkController | /api/network/link | 链路管理 |
| NetworkConfigController | /api/network/config | 网络配置 |
| NetworkDeviceController | /api/network/device | 网络设备 |
| NetworkMonitorController | /api/network/monitor | 网络监控 |
| IpManagementController | /api/network/ip | IP管理 |
| TrafficStatsController | /api/network/traffic | 流量统计 |
| FirewallController | /api/network/firewall | 防火墙 |
| PathController | /api/network/path | 路径管理 |
| LinkQualityController | /api/network/quality | 链路质量 |

### 4.9 路由/端代理 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| RouteController | /api/route | 路由管理 |
| EndAgentController | /api/end/agent | 端代理管理 |
| McpAgentController | /api/mcp | MCP代理 |
| McpAgentManagementController | /api/mcp/mgmt | MCP管理 |

### 4.10 个人中心 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| PersonalDashboardController | /api/personal/dashboard | 个人仪表盘 |
| PersonalSkillController | /api/personal/skill | 个人技能 |
| PersonalGroupController | /api/personal/group | 个人群组 |
| PersonalSharingController | /api/personal/sharing | 个人分享 |
| PersonalExecutionController | /api/personal/execution | 个人执行 |
| PersonalIdentityController | /api/personal/identity | 个人身份 |
| PersonalHelpController | /api/personal/help | 个人帮助 |

### 4.11 管理员 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| AdminDashboardController | /api/admin/dashboard | 管理仪表盘 |
| AdminSkillController | /api/admin/skill | 技能管理 |
| AdminStorageController | /api/admin/storage | 存储管理 |
| AdminMarketController | /api/admin/market | 市场管理 |
| AdminGroupController | /api/admin/group | 群组管理 |
| AdminCertificationController | /api/admin/cert | 认证管理 |
| AdminCategoryController | /api/admin/category | 分类管理 |
| AdminRemoteController | /api/admin/remote | 远程管理 |

### 4.12 OpenWrt Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| OpenWrtController | /api/openwrt | OpenWrt管理 |
| OpenWrtNetworkController | /api/openwrt/network | OpenWrt网络 |
| OpenWrtMetricsController | /api/openwrt/metrics | OpenWrt指标 |
| OpenWrtP2PController | /api/openwrt/p2p | OpenWrt P2P |

### 4.13 其他 Controller

| Controller | 路径 | 功能 |
|-----------|------|------|
| ConfigController | /api/config | 配置管理 |
| AppConfigController | /api/app/config | 应用配置 |
| SystemConfigController | /api/system/config | 系统配置 |
| CacheController | /api/cache | 缓存管理 |
| SessionController | /api/session | 会话管理 |
| SecurityController | /api/security | 安全管理 |
| OrganizationController | /api/org | 组织管理 |
| ScenarioController | /api/scenario | 场景管理 |
| ServiceController | /api/service | 服务管理 |
| CommandController | /api/command | 命令管理 |
| DeviceController | /api/device | 设备管理 |
| ToolManagementController | /api/tool | 工具管理 |
| MenuController | /api/console/menu | 菜单管理 |
| ConsoleController | /api/console | 控制台 |
| DebugConsoleController | /api/debug | 调试控制台 |
| HomeController | /api/home | 首页 |
| P2PNetworkController | /api/p2p | P2P网络 |
| CometController | /api/comet | Comet |
| SkillGatewayController | /api/skill/gateway | 技能网关 |
| MqttController | /api/mqtt | MQTT |
| DataSourceSyncController | /api/datasource | 数据源同步 |
| InitConfigPackageController | /api/init | 初始化配置 |
| NorthboundProtocolController | /api/north | 北向协议 |
| CommandDispatchController | /api/dispatch | 命令分发 |

---

## 5. 问题汇总

### 5.1 菜单与页面不匹配

**无** - 所有菜单配置中的页面文件都存在。

### 5.2 页面无菜单入口（共70+个）

主要分布在：
- 技能中心模块（12个）
- LLM模块（6个）
- 网络管理模块（14个）
- 局域网/家庭模块（9个）
- OpenWrt模块（9个）
- 其他模块（10+个）

### 5.3 API与页面匹配情况

大部分页面都有对应的 Controller 支持，但存在以下情况：
- 部分页面调用多个 Controller 的 API
- 部分 Controller 没有对应的前端页面（纯后端服务）

---

## 6. 建议处理方案

### 6.1 短期处理

1. **补充菜单配置**：将技能中心、LLM模块纳入菜单
2. **清理冗余页面**：删除重复或废弃的页面
3. **统一命名规范**：页面路径与菜单ID保持一致

### 6.2 长期规划

1. **模块化菜单**：根据用户角色动态加载菜单
2. **页面归类**：将散落的页面归入对应模块
3. **API文档化**：为所有 Controller 生成 API 文档

---

## 7. 统计数据

| 项目 | 数量 |
|------|------|
| 菜单项总数 | 55 |
| 菜单页面匹配率 | 100% |
| 未纳入菜单页面 | 70+ |
| Controller 总数 | 103 |
| 一级菜单 | 13 |
| 二级菜单 | 42 |

---

**文档结束**
