# ooderNexus 功能完善开发任务

## 1. 菜单重复功能去重

### 1.1 已完成去重的功能项

| 功能名称 | 重复项1 | 重复项2 | 处理方式 | 状态 |
|---------|---------|---------|---------|------|
| 文档管理 | 我的能力 > 我的文档 | 协同协作 > 文件共享 | 合并为"文件管理" | ✅ 已完成 |
| 网络管理 | 我的能力 > 我的网络 | 网络管理 > 网络配置 | 改为"网络概览" | ✅ 已完成 |
| 安全管理 | 我的能力 > 安全设置 | Nexus管理 > 安全管理 | 保留Nexus管理下 | ✅ 已完成 |
| 设备管理 | 我的能力 > 我的设备 | 网络管理 > 设备管理 | 保留网络管理下 | ✅ 已完成 |
| 群组管理 | 我的能力 > 我的群组 | 协同协作 > 消息中心 | 移动到协同协作下 | ✅ 已完成 |

### 1.2 当前菜单结构

```
我的能力（个人视角）
├── 我的能力包
└── 网络概览

协同协作
├── 消息中心
├── 联系人
├── 协同场景
├── 群组管理
└── 文件管理

系统配置
├── 能力约束
├── 协作关系
├── 健康检查
└── 审计日志

资源管理
├── 能力中心
├── LLM配置
└── 存储管理

网络管理
├── IP管理
├── 流量监控
├── 网络配置
├── 设备管理
└── 远程终端

Nexus管理
├── 仪表盘
├── 系统状态
├── P2P管理
├── 安全管理
├── 协议管理
└── 终端代理

任务管理
├── 数据抽取
└── 列表抽取
```

## 2. 后端已实现但前端页面缺失

### 2.1 有API无页面的功能

| API路径 | 功能描述 | 对应控制器 | 开发优先级 |
|---------|---------|-----------|-----------|
| /api/admin/* | 管理后台功能 | Admin*Controller | 高 |
| /api/personal/* | 个人中心功能 | Personal*Controller | 高 |
| /api/protocol/* | 协议管理功能 | Protocol*Controller | 中 |
| /api/tools | 工具管理 | ToolManagementController | 中 |
| /api/firewall | 防火墙管理 | FirewallController | 高 |
| /api/mcp/* | MCP代理管理 | McpAgentController | 中 |
| /api/skills/database | 数据库连接 | DatabaseConnectionController | 低 |
| /api/skills/auth | 技能认证 | SkillAuthController | 低 |

### 2.2 需要新增的前端页面

1. **管理后台模块**（高优先级）
   - 管理仪表盘：/console/pages/admin/dashboard.html
   - 技能审核：/console/pages/admin/skills.html
   - 用户管理：/console/pages/admin/users.html
   - 存储管理：/console/pages/admin/storage.html
   - 远程管理：/console/pages/admin/remote.html

2. **个人中心模块**（高优先级）
   - 个人仪表盘：/console/pages/personal/dashboard.html（已有，需完善）
   - 我的分享：/console/pages/personal/sharing.html
   - 身份管理：/console/pages/personal/identity.html
   - 执行记录：/console/pages/personal/execution.html
   - 帮助中心：/console/pages/personal/help.html

3. **协议管理模块**（中优先级）
   - 协作协议：/console/pages/protocol/collaboration.html
   - 发现协议：/console/pages/protocol/discovery.html
   - 登录协议：/console/pages/protocol/login.html
   - 观察协议：/console/pages/protocol/observation.html
   - 域管理协议：/console/pages/protocol/domain.html

4. **防火墙管理**（高优先级）
   - 防火墙规则：/console/pages/security/firewall.html
   - 访问控制：/console/pages/security/access-control.html

## 3. 前端已有但后端未完善的功能

### 3.1 有页面无API的功能

| 页面路径 | 功能描述 | 需要开发的API | 开发优先级 | 状态 |
|---------|---------|--------------|-----------|------|
| ~~enterprise/*~~ | ~~企业管理~~ | - | - | ❌ 已移除 |
| /console/pages/lan/* | 局域网管理 | /api/lan/* | 中 |
| /console/pages/openwrt/* | OpenWrt管理 | /api/openwrt/* | 高 |
| /console/pages/monitor/* | 监控管理 | /api/monitor/* | 中 |
| /console/pages/mine/* | 我的模块 | /api/mine/* | 低 |

### 3.2 需要新增的后端API

1. **OpenWrt管理模块**（高优先级）
   - OpenWrtService：已存在，需完善API
   - 路由器仪表盘：GET /api/openwrt/dashboard
   - 网络设置：PUT /api/openwrt/network
   - 系统状态：GET /api/openwrt/system
   - 命令执行：POST /api/openwrt/command
   - 配置文件：GET/PUT /api/openwrt/config

2. **局域网管理模块**（中优先级）
   - LanDashboardService：需新建
   - 局域网仪表盘：GET /api/lan/dashboard
   - 网络设备：GET /api/lan/devices
   - 网络状态：GET /api/lan/status
   - 带宽监控：GET /api/lan/bandwidth

## 4. 功能完善度评估

### 4.1 完整度评分（满分100）

| 模块 | 后端完整度 | 前端完整度 | 总体评分 |
|------|-----------|-----------|---------|
| 我的能力 | 80% | 70% | 75 |
| 协同协作 | 85% | 80% | 82 |
| 系统配置 | 90% | 85% | 87 |
| 资源管理 | 75% | 70% | 72 |
| 网络管理 | 85% | 60% | 72 |
| Nexus管理 | 90% | 70% | 80 |
| 任务管理 | 95% | 90% | 92 |
| 管理后台 | 70% | 10% | 40 |
| 个人中心 | 60% | 30% | 45 |
| 协议管理 | 80% | 20% | 50 |
| OpenWrt | 50% | 80% | 65 |

### 4.2 总体完整度

- **后端API完整度**：75%
- **前端页面完整度**：55%
- **总体完整度**：65%

## 5. 开发任务优先级排序

### 5.1 高优先级任务（P0）

1. ~~菜单去重和结构优化~~ ✅ 已完成
2. 管理后台前端页面开发
3. OpenWrt管理API完善
4. 防火墙管理页面开发

### 5.2 中优先级任务（P1）

1. 个人中心前端页面完善
2. 协议管理前端页面开发
3. 局域网管理API开发
4. 网络管理前端页面完善

### 5.3 低优先级任务（P2）

1. ~~企业管理模块开发~~ ❌ 已移除
2. 监控管理模块开发
3. 其他功能优化

## 6. 下一步行动计划

### 6.1 立即执行

1. ~~更新 menu-config.json，去除重复功能~~ ✅ 已完成
2. 创建管理后台前端页面框架
3. 完善 OpenWrt 管理相关API

### 6.2 本周计划

1. 完成管理后台核心页面开发
2. 完成个人中心页面开发
3. 完成防火墙管理页面开发

### 6.3 本月计划

1. 完成所有高优先级任务
2. 完成中优先级任务的50%
3. 系统整体完整度提升至80%

---

**文档版本**：v1.1  
**创建日期**：2026-02-22  
**更新日期**：2026-02-22  
**负责人**：开发团队
