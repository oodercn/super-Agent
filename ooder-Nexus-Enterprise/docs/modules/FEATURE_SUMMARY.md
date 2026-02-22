# Nexus 功能总表

**文档版本**: 2.0  
**更新日期**: 2026-02-17  
**SDK 版本**: 0.7.1  

---

## 1. 功能架构总览

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Nexus 功能架构                                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │
│  │  AI 服务    │  │  技能中心   │  │  网络服务   │  │  安全中心   │    │
│  │  MOD-001    │  │  MOD-002    │  │  MOD-003    │  │  MOD-004    │    │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘    │
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────────┐    │
│  │  存储服务   │  │  托管部署   │  │       Nexus 核心服务         │    │
│  │  MOD-005    │  │  MOD-006    │  │  NexusService / OfflineService│    │
│  └─────────────┘  └─────────────┘  └─────────────────────────────┘    │
│                                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐  │
│  │                     南北向服务层                                  │  │
│  │  北向: UDP/P2P/域管理/观测  │  南向: HTTP/认证/协作/发现/角色    │  │
│  └─────────────────────────────────────────────────────────────────┘  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. 模块功能清单

### 2.1 AI 服务模块 (MOD-001)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-LLM-001 | 同步对话 | 发送消息获取完整响应 | `POST /api/llm/chat` | ✅ | ✅ |
| F-LLM-002 | 异步对话 | 异步方式发送消息 | `POST /api/llm/chat/async` | ✅ | ✅ |
| F-LLM-003 | 流式输出 | SSE 实时输出响应 | `GET /api/llm/stream` | ✅ | ✅ |
| F-LLM-004 | 多模型支持 | 切换不同 AI 模型 | `POST /api/llm/models` | ✅ | ✅ |
| F-LLM-005 | Token 统计 | 统计 API 使用量 | `POST /api/llm/tokens` | ✅ | ✅ |
| F-LLM-006 | 向量嵌入 | 文本向量化处理 | `POST /api/llm/embeddings` | ✅ | ✅ |
| F-LLM-007 | Function Calling | 函数调用能力 | `POST /api/llm/functions` | ✅ | ✅ |

**模块完成度**: 后端 100% | 前端 100%

---

### 2.2 技能中心模块 (MOD-002)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-SKILL-001 | 技能列表 | 列出已安装技能 | `POST /api/skillcenter/installed/list` | ✅ | ✅ |
| F-SKILL-002 | 技能安装 | 安装新技能 | `POST /api/skillcenter/installed/install` | ✅ | ✅ |
| F-SKILL-003 | 技能卸载 | 卸载已安装技能 | `POST /api/skillcenter/installed/uninstall` | ✅ | ✅ |
| F-SKILL-004 | 技能启动 | 启动技能 | `POST /api/skillcenter/installed/start` | ✅ | ✅ |
| F-SKILL-005 | 技能停止 | 停止技能 | `POST /api/skillcenter/installed/stop` | ✅ | ✅ |
| F-SKILL-006 | 技能配置 | 配置技能参数 | `POST /api/skillcenter/config/update` | ✅ | ✅ |
| F-SKILL-007 | 安装进度 | 查看安装进度 | `POST /api/skillcenter/installed/progress` | ⚠️ | ⚠️ |
| F-SKILL-008 | 依赖管理 | 管理技能依赖 | `POST /api/skillcenter/dependencies` | ⚠️ | ⚠️ |
| F-SKILL-009 | 生命周期控制 | 暂停/恢复技能 | `POST /api/skillcenter/installed/pause` | ⚠️ | ⚠️ |
| F-SKILL-010 | 状态可视化 | 技能状态图表 | - | ⚠️ | ⚠️ |
| F-SKILL-011 | 协作场景 | 多 Agent 协作 | `POST /api/skillcenter/collaboration` | ⚠️ | ⚠️ |

**模块完成度**: 后端 60% | 前端 58%

---

### 2.3 网络服务模块 (MOD-003)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-NET-001 | 链路创建 | 创建网络链路 | `POST /api/network/links/create` | ✅ | ✅ |
| F-NET-002 | 链路列表 | 列出所有链路 | `POST /api/network/links/list` | ✅ | ✅ |
| F-NET-003 | 链路查询 | 查询链路详情 | `POST /api/network/links/query` | ✅ | ✅ |
| F-NET-004 | 链路删除 | 删除链路 | `POST /api/network/links/remove` | ✅ | ✅ |
| F-NET-005 | 链路质量 | 查询链路质量 | `POST /api/network/links/quality` | ✅ | ✅ |
| F-NET-006 | 最优路径 | 计算最优路径 | `POST /api/network/route/optimal` | ✅ | ⚠️ |
| F-NET-007 | 质量监控 | 网络质量监控 | `POST /api/network/monitor` | ✅ | ✅ |
| F-NET-008 | 网络统计 | 网络统计数据 | `POST /api/network/stats` | ✅ | ✅ |

**模块完成度**: 后端 100% | 前端 94%

---

### 2.4 安全中心模块 (MOD-004)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-SEC-001 | 密钥生成 | 生成密钥对 | `POST /api/security/keypair/generate` | ✅ | ✅ |
| F-SEC-002 | 数据加密 | 加密数据 | `POST /api/security/encrypt` | ✅ | ✅ |
| F-SEC-003 | 数据解密 | 解密数据 | `POST /api/security/decrypt` | ✅ | ✅ |
| F-SEC-004 | 数据签名 | 签名数据 | `POST /api/security/sign` | ✅ | ✅ |
| F-SEC-005 | 签名验证 | 验证签名 | `POST /api/security/verify` | ✅ | ✅ |
| F-SEC-006 | Token 生成 | 生成访问令牌 | `POST /api/security/token/generate` | ✅ | ✅ |
| F-SEC-007 | Token 验证 | 验证令牌 | `POST /api/security/token/validate` | ✅ | ✅ |
| F-SEC-008 | Token 撤销 | 撤销令牌 | `POST /api/security/token/revoke` | ✅ | ⚠️ |
| F-SEC-009 | 场景密钥 | 生成场景密钥 | `POST /api/security/scene/key/generate` | ✅ | ✅ |
| F-SEC-010 | 对等端加密 | 端到端加密 | `POST /api/security/peer/encrypt` | ✅ | ✅ |

**模块完成度**: 后端 100% | 前端 95%

---

### 2.5 存储服务模块 (MOD-005)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-STO-001 | 数据存储 | 存储数据 | `POST /api/storage/save` | ✅ | N/A |
| F-STO-002 | 数据读取 | 读取数据 | `POST /api/storage/load` | ✅ | N/A |
| F-STO-003 | 数据删除 | 删除数据 | `POST /api/storage/delete` | ✅ | N/A |
| F-STO-004 | 列表查询 | 查询数据列表 | `POST /api/storage/list` | ✅ | N/A |
| F-STO-005 | 缓存管理 | 缓存操作 | `POST /api/storage/cache` | ✅ | N/A |

**模块完成度**: 后端 100% | 前端 N/A (内部服务)

---

### 2.6 托管部署模块 (MOD-006)

| 功能ID | 功能名称 | 功能描述 | API 端点 | 后端状态 | 前端状态 |
|--------|---------|---------|---------|---------|---------|
| F-HOST-001 | 远程技能列表 | 列出远程技能 | `POST /api/admin/remote/skills/list` | ✅ | ✅ |
| F-HOST-002 | 技能同步 | 同步远程技能 | `POST /api/admin/remote/skills/{id}/sync` | ✅ | ✅ |
| F-HOST-003 | 技能移除 | 移除远程技能 | `DELETE /api/admin/remote/skills/{id}` | ✅ | ✅ |
| F-HOST-004 | 托管列表 | 列出托管项 | `POST /api/admin/remote/hosting/list` | ✅ | ✅ |
| F-HOST-005 | 托管切换 | 切换托管状态 | `POST /api/admin/remote/hosting/{id}/toggle` | ✅ | ✅ |
| F-HOST-006 | 托管移除 | 移除托管 | `DELETE /api/admin/remote/hosting/{id}` | ✅ | ✅ |
| F-HOST-007 | 监控列表 | 列出监控项 | `POST /api/admin/remote/monitoring/list` | ✅ | ✅ |
| F-HOST-008 | 监控检查 | 检查监控状态 | `POST /api/admin/remote/monitoring/{id}/check` | ✅ | ✅ |
| F-HOST-009 | 监控移除 | 移除监控 | `DELETE /api/admin/remote/monitoring/{id}` | ✅ | ✅ |

**模块完成度**: 后端 100% | 前端 79%

---

### 2.7 Nexus 核心服务 (SDK 0.7.2 新增)

| 功能ID | 功能名称 | 功能描述 | 接口 | 状态 |
|--------|---------|---------|------|------|
| F-NEXUS-001 | 启动/停止 | Nexus 生命周期管理 | `NexusService.start/stop` | ✅ |
| F-NEXUS-002 | 登录/登出 | 用户认证管理 | `NexusService.login/logout` | ✅ |
| F-NEXUS-003 | 会话管理 | 获取当前会话 | `NexusService.getCurrentSession` | ✅ |
| F-NEXUS-004 | 对等发现 | 发现网络节点 | `NexusService.discoverPeers` | ✅ |
| F-NEXUS-005 | 角色决策 | 决定节点角色 | `NexusService.getCurrentRole` | ✅ |
| F-NEXUS-006 | 场景组管理 | 加入/离开场景组 | `NexusService.joinSceneGroup` | ✅ |
| F-NEXUS-007 | 私有资源 | 技能安装/存储 | `PrivateResourceService` | ✅ |
| F-NEXUS-008 | 离线运行 | 离线模式支持 | `OfflineService` | ✅ |

---

### 2.8 南北向服务层 (SDK 0.7.2 新增)

#### 北向服务

| 功能ID | 功能名称 | 功能描述 | 接口 | 状态 |
|--------|---------|---------|------|------|
| F-NORTH-001 | UDP 消息 | UDP 通信 | `NorthNetworkService.sendUdpMessage` | ✅ |
| F-NORTH-002 | P2P 消息 | P2P 通信 | `NorthNetworkService.sendP2pMessage` | ✅ |
| F-NORTH-003 | 广播消息 | 网络广播 | `NorthNetworkService.broadcast` | ✅ |
| F-NORTH-004 | 域管理 | 域创建/成员管理 | `DomainManagementProtocol` | ✅ |
| F-NORTH-005 | 观测服务 | 指标/日志/追踪 | `ObservationProtocol` | ✅ |

#### 南向服务

| 功能ID | 功能名称 | 功能描述 | 接口 | 状态 |
|--------|---------|---------|------|------|
| F-SOUTH-001 | HTTP 请求 | HTTP 通信 | `SouthNetworkService.sendHttpRequest` | ✅ |
| F-SOUTH-002 | 认证服务 | 用户认证 | `SouthNetworkService.authenticate` | ✅ |
| F-SOUTH-003 | 权限检查 | 权限验证 | `SouthNetworkService.checkPermission` | ✅ |
| F-SOUTH-004 | 协作协议 | 场景组协作 | `CollaborationProtocol` | ✅ |
| F-SOUTH-005 | 发现协议 | 网络发现 | `DiscoveryProtocol` | ✅ |
| F-SOUTH-006 | 登录协议 | 登录认证 | `LoginProtocol` | ✅ |
| F-SOUTH-007 | 角色协议 | 角色决策 | `RoleProtocol` | ✅ |

---

### 2.9 增强协议中心 (SDK 0.7.2 新增)

| 功能ID | 功能名称 | 功能描述 | 接口 | 状态 |
|--------|---------|---------|------|------|
| F-PROTO-001 | 命令处理 | 处理增强命令 | `EnhancedProtocolHub.handleEnhancedCommand` | ✅ |
| F-PROTO-002 | 命令追踪 | 追踪命令执行 | `EnhancedProtocolHub.traceCommand` | ✅ |
| F-PROTO-003 | 命令重试 | 重试失败命令 | `EnhancedProtocolHub.retryCommand` | ✅ |
| F-PROTO-004 | 处理器注册 | 注册协议处理器 | `EnhancedProtocolHub.registerProtocolHandler` | ✅ |

---

## 3. 功能统计

### 3.1 按模块统计

| 模块 | 总功能数 | 后端完成 | 前端完成 | 后端完成率 | 前端完成率 |
|------|---------|---------|---------|-----------|-----------|
| AI 服务 | 7 | 7 | 7 | 100% | 100% |
| 技能中心 | 11 | 6 | 5 | 55% | 45% |
| 网络服务 | 8 | 8 | 7 | 100% | 88% |
| 安全中心 | 10 | 10 | 9 | 100% | 90% |
| 存储服务 | 5 | 5 | - | 100% | N/A |
| 托管部署 | 9 | 9 | 9 | 100% | 100% |
| Nexus 核心 | 8 | 8 | - | 100% | N/A |
| 北向服务 | 5 | 5 | - | 100% | N/A |
| 南向服务 | 7 | 7 | - | 100% | N/A |
| 增强协议 | 4 | 4 | - | 100% | N/A |
| **总计** | **74** | **69** | **37** | **93%** | **88%** |

### 3.2 按优先级统计

| 优先级 | 功能数 | 完成数 | 完成率 |
|--------|--------|--------|--------|
| P0 | 28 | 28 | 100% |
| P1 | 26 | 22 | 85% |
| P2 | 20 | 19 | 95% |
| **总计** | **74** | **69** | **93%** |

---

## 4. API 端点汇总

### 4.1 按模块分类

| 模块 | API 数量 | GET | POST | PUT | DELETE |
|------|---------|-----|------|-----|--------|
| AI 服务 | 7 | 1 | 6 | 0 | 0 |
| 技能中心 | 11 | 0 | 11 | 0 | 0 |
| 网络服务 | 8 | 0 | 8 | 0 | 0 |
| 安全中心 | 10 | 0 | 10 | 0 | 0 |
| 存储服务 | 5 | 0 | 5 | 0 | 0 |
| 托管部署 | 9 | 0 | 3 | 0 | 6 |
| **总计** | **50** | **1** | **43** | **0** | **6** |

### 4.2 API 命名规范

```
/api/{module}/{resource}/{action}
/api/{module}/{resource}/{id}/{action}
```

示例:
- `/api/llm/chat` - AI 服务对话
- `/api/network/links/create` - 网络服务创建链路
- `/api/admin/remote/skills/{id}/sync` - 托管部署同步技能

---

## 5. 前端页面汇总

| 模块 | 页面路径 | 页面名称 | 状态 |
|------|---------|---------|------|
| AI 服务 | `pages/llm/llm-chat.html` | AI 对话 | ✅ |
| 技能中心 | `pages/skillcenter/installed-skills.html` | 已安装技能 | ✅ |
| 技能中心 | `pages/skillcenter/skill-market.html` | 技能市场 | ✅ |
| 网络服务 | `pages/nexus/link-management.html` | 链路管理 | ✅ |
| 安全中心 | `pages/security/security-center.html` | 安全中心 | ✅ |
| 托管部署 | `pages/skillcenter/admin/remote-hosting.html` | 远程托管 | ✅ |

---

## 6. 待开发功能

### 6.1 P1 优先级

| 功能ID | 功能名称 | 模块 | 工作量 | 状态 |
|--------|---------|------|--------|------|
| F-SKILL-007 | 安装进度 | 技能中心 | 0.5 天 | 待处理 |
| F-SKILL-008 | 依赖管理 | 技能中心 | 0.5 天 | 待处理 |
| F-SKILL-009 | 生命周期控制 | 技能中心 | 0.5 天 | 待处理 |

### 6.2 P2 优先级

| 功能ID | 功能名称 | 模块 | 工作量 | 状态 |
|--------|---------|------|--------|------|
| F-SKILL-011 | 协作场景 | 技能中心 | 1 天 | 待处理 |
| F-NET-006 | 最优路径可视化 | 网络服务 | 0.5 天 | 待处理 |
| F-SEC-008 | Token 撤销界面 | 安全中心 | 0.5 天 | 待处理 |

---

## 7. 版本历史

| 版本 | 日期 | 作者 | 变更说明 |
|------|------|------|---------|
| 1.0 | 2026-02-17 | ooder Team | 初始版本 |
| 2.0 | 2026-02-17 | ooder Team | 新增 Nexus 核心服务、南北向服务层、增强协议中心功能 |

---

## 8. 相关文档

| 文档 | 路径 | 说明 |
|------|------|------|
| 模块总览 | [README.md](README.md) | 模块实现状态总览 |
| SDK 升级方案 | [SDK_0.7.2_UPGRADE_PLAN.md](../SDK_0.7.2_UPGRADE_PLAN.md) | SDK 0.7.2 升级方案 |
| AI 服务分册 | [MOD-001_AI_SERVICE.md](MOD-001_AI_SERVICE.md) | AI 服务模块详情 |
| 技能中心分册 | [MOD-002_SKILL_CENTER.md](MOD-002_SKILL_CENTER.md) | 技能中心模块详情 |
| 网络服务分册 | [MOD-003_NETWORK_SERVICE.md](MOD-003_NETWORK_SERVICE.md) | 网络服务模块详情 |
| 安全中心分册 | [MOD-004_SECURITY_CENTER.md](MOD-004_SECURITY_CENTER.md) | 安全中心模块详情 |
| 存储服务分册 | [MOD-005_STORAGE_SERVICE.md](MOD-005_STORAGE_SERVICE.md) | 存储服务模块详情 |
| 托管部署分册 | [MOD-006_HOSTING_DEPLOYMENT.md](MOD-006_HOSTING_DEPLOYMENT.md) | 托管部署模块详情 |
