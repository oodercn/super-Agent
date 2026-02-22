# ooderNexus Mock数据与Skills API对应关系文档

**文档版本**: 1.1  
**更新日期**: 2026-02-23  
**目标**: 梳理使用mock数据的页面，检查skills库中是否有对应API，并制定替代方案

---

## 1. Mock数据使用情况

### 1.1 主要使用mock数据的页面

| 页面路径 | 功能 | Mock数据内容 |
|---------|------|-------------|
| `msgcmd/msg-push.html` | 消息推送 | `mockUsers`、`mockGroups`、`mockTopics`、`mockHistory` |
| `msgcmd/topic-manager.html` | Topic管理 | `mockSubscribers`、`mockMessages`、`loadMockTopicTree()` |
| `msgcmd/cmd-monitor.html` | 命令监控 | `loadMockCommands()`、`generateMockLogs()` |
| `observation/topology-monitor.html` | 拓扑监控 | `loadMockData()` |
| `network/network-overview.html` | 网络概览 | `toggleMockMode()` |
| `datasource/datasource-manager.html` | 数据源管理 | 静态数据源类型定义、本地配置存储 |
| `system/cache-management.html` | 缓存管理 | `loadMockStats()`、`renderMockCacheList()` |
| `system/session-management.html` | 会话管理 | `loadMockSessions()` |

### 1.2 API返回空数据的情况

| 页面路径 | 功能 | 空数据处理 |
|---------|------|------------|
| `msgcmd/topic-manager.html` | Topic管理 | `!subscribers || subscribers.length === 0` |
| `msgcmd/topic-manager.html` | Topic管理 | `!messages || messages.length === 0` |
| `msgcmd/msg-push.html` | 消息推送 | `!history || history.length === 0` |
| `storage/file-browser.html` | 文件浏览 | `!files || files.length === 0` |
| `im/group-list.html` | 群组管理 | `empty-state` 状态显示 |
| `im/conversation-list.html` | 会话管理 | `empty-state` 状态显示 |
| `monitor/monitor-dashboard.html` | 监控面板 | 多处 `empty-state` 显示 |
| `datasource/datasource-manager.html` | 数据源管理 | `result.data && result.data.length > 0` 空数据提示 |

---

## 2. Skills库API对应关系

### 2.1 现有Skills服务

| Skill ID | 服务名称 | 对应功能 | 匹配度 |
|----------|----------|----------|--------|
| `skill-msg-service` | 消息服务 | 消息推送、P2P通信、Topic订阅 | **高** ✅ |
| `skill-cmd-service` | 命令服务 | 命令下发、任务调度、执行追踪 | **高** ✅ |
| `skill-res-service` | 资源服务 | 组织管理、场景策略、资源配置 | **高** ✅ |
| `skill-org-dingding` | 钉钉组织服务 | 钉钉组织集成、用户认证、消息发送 | **高** ✅ |
| `skill-org-feishu` | 飞书组织服务 | 飞书组织集成、用户认证、消息发送 | **高** ✅ |
| `skill-user-auth` | 用户认证服务 | 本地用户认证、会话管理、权限检查 | **高** ✅ |
| `skill-agent-service` | Agent服务 | Agent管理、消息路由 | **中** ⚠️ |
| `skill-data-service` | 数据服务 | 数据同步、存储管理 | **中** ⚠️ |
| `skill-jds-server` | 服务注册发现 | 服务注册、健康检查 | **低** ❌ |

### 2.2 API映射关系

| Mock功能 | 对应Skill | API路径 | 状态 |
|----------|-----------|---------|------|
| 消息推送 | `skill-msg-service` | `/api/msg/push` | **可用** ✅ |
| Topic管理 | `skill-msg-service` | `/api/msg/topic` | **可用** ✅ |
| 命令监控 | `skill-cmd-service` | `/api/north/command/*` | **可用** ✅ |
| 组织管理 | `skill-res-service` | `/api/org/*` | **可用** ✅ |
| 钉钉组织集成 | `skill-org-dingding` | `/api/org/tree` | **可用** ✅ |
| 钉钉消息发送 | `skill-org-dingding` | `/api/message/send` | **可用** ✅ |
| 飞书组织集成 | `skill-org-feishu` | `/api/org/tree` | **可用** ✅ |
| 飞书消息发送 | `skill-org-feishu` | `/api/message/send` | **可用** ✅ |
| 会话管理 | `skill-user-auth` | `/api/auth/*` | **可用** ✅ |
| 数据源管理 | 核心服务 | `/api/enexus/datasource/*` | **可用** ✅ |
| 网络拓扑 | `skill-agent-service` | 无直接对应 | **需要开发** ❌ |
| 缓存管理 | `skill-data-service` | 无直接对应 | **需要开发** ❌ |

---

## 3. 替代方案

### 3.1 优先使用现有Skills

| 页面 | Mock功能 | 替代方案 |
|------|----------|----------|
| `msgcmd/msg-push.html` | 消息推送 | 使用 `skill-msg-service` 的 `/api/msg/push` |
| `msgcmd/topic-manager.html` | Topic管理 | 使用 `skill-msg-service` 的 `/api/msg/topic` |
| `msgcmd/cmd-monitor.html` | 命令监控 | 使用 `skill-cmd-service` 的 `/api/north/command/*` |
| `im/org-structure.html` | 组织管理 | 使用 `skill-res-service` 的 `/api/org/*` |
| `im/org-structure.html` | 钉钉组织集成 | 使用 `skill-org-dingding` 的 `/api/org/tree` |
| `im/org-structure.html` | 飞书组织集成 | 使用 `skill-org-feishu` 的 `/api/org/tree` |
| `system/session-management.html` | 会话管理 | 使用 `skill-user-auth` 的 `/api/auth/*` |
| `datasource/datasource-manager.html` | 数据源管理 | 使用核心服务的 `/api/enexus/datasource/*` |

### 3.2 需要开发的API

| 功能 | 建议 | 优先级 |
|------|------|--------|
| 网络拓扑数据 | 扩展 `skill-agent-service` | **高** |
| 缓存管理API | 扩展 `skill-data-service` | **中** |
| 系统监控API | 新建 `skill-monitor-service` | **低** |

---

## 4. 迁移计划

### 4.1 短期（1-2周）

1. **消息命令模块**：替换 `msgcmd/*` 页面的mock数据
2. **组织管理模块**：替换 `im/org-*` 页面的mock数据
3. **命令监控模块**：替换 `msgcmd/cmd-monitor.html` 的mock数据
4. **认证会话模块**：替换 `system/session-management.html` 的mock数据
5. **数据源管理模块**：集成核心服务的数据源API
6. **第三方组织集成**：集成 `skill-org-dingding` 和 `skill-org-feishu` 服务

### 4.2 中期（2-4周）

1. **网络管理模块**：开发并集成网络拓扑API
2. **存储管理模块**：替换 `storage/*` 页面的空数据处理
3. **监控模块**：开发系统监控API

### 4.3 长期（4-8周）

1. **完整迁移**：替换所有mock数据
2. **API优化**：完善所有Skills API
3. **文档完善**：更新API文档和使用指南

---

## 5. 技术风险

| 风险 | 影响 | 缓解措施 |
|------|------|----------|
| API性能问题 | 页面加载缓慢 | 实现缓存机制、分页加载 |
| 数据一致性 | 数据不同步 | 实现数据同步机制 |
| 兼容性问题 | 现有页面不兼容 | 渐进式迁移，保持向后兼容 |
| 开发资源 | 开发工作量大 | 优先迁移核心功能 |

---

## 6. 结论

### 6.1 可用的Skills API

✅ **消息服务** (`skill-msg-service`)：完全覆盖消息推送、Topic管理功能  
✅ **命令服务** (`skill-cmd-service`)：完全覆盖命令监控功能  
✅ **资源服务** (`skill-res-service`)：完全覆盖组织管理功能  
✅ **钉钉组织服务** (`skill-org-dingding`)：完全覆盖钉钉组织集成、用户认证、消息发送功能  
✅ **飞书组织服务** (`skill-org-feishu`)：完全覆盖飞书组织集成、用户认证、消息发送功能  
✅ **用户认证服务** (`skill-user-auth`)：完全覆盖本地用户认证、会话管理、权限检查功能  

### 6.2 需要开发的功能

❌ **网络拓扑**：需要扩展 `skill-agent-service`  
❌ **缓存管理**：需要扩展 `skill-data-service`  

### 6.3 建议行动

1. **立即开始**：使用现有Skills API替换消息命令模块的mock数据
2. **认证集成**：集成 `skill-user-auth` 服务，替换会话管理的mock数据
3. **第三方集成**：集成钉钉和飞书组织服务，丰富组织管理功能
4. **规划开发**：扩展现有Skills服务，添加缺失的API
5. **持续迁移**：按优先级逐步替换所有mock数据
6. **监控效果**：迁移后监控页面性能和用户体验

---

**文档维护**：
- 随着Skills API的扩展，需要及时更新此文档
- 迁移过程中的问题和解决方案应记录在文档中
- 定期审查mock数据使用情况，确保逐步减少mock数据的使用
