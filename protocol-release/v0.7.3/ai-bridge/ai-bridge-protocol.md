# AI Bridge 协议文档 - v0.7.3

## 1. 协议概述

AI Bridge 协议是 SuperAgent 系统中的北向协议，用于实现 MCP Agent 与云服务（包括公云、私有云、混合云）之间的通信。v0.7.3 版本在 v0.7.0 基础上进行了升级，增强了离线支持和事件管理能力。

### 1.1 协议目标

- 实现 MCP Agent 与云服务之间的高效、安全通信
- 支持广域网环境下的 AI 能力调用
- 提供标准化的云服务接口和消息格式
- 确保通信的安全性和可靠性
- 支持云服务的动态发现和注册
- **v0.7.3 新增**：支持离线模式和自动同步

### 1.2 v0.7.3 升级内容

| 新增特性 | 说明 |
|---------|------|
| 离线支持 | 网络断开时自动切换离线模式 |
| 事件总线集成 | 统一事件管理和模块解耦 |
| 状态同步 | 离线期间变更的自动同步 |
| 缓存增强 | 离线场景下的数据缓存 |

### 1.3 协议适用范围

- 广域网内的 MCP Agent 与云服务通信
- 跨网络、跨平台的 AI 能力调用
- 企业级分布式 AI 系统
- 个人设备与云服务的安全通信
- 离线环境下的 AI 服务调用

## 2. 协议架构

### 2.1 分层架构

```
┌───────────────────────
│      云服务层        │
└───────────────────────
        │
┌───────────────────────
│      协议层          │
│  - Discovery         │
│  - Login             │
│  - Collaboration     │
│  - Offline           │
└───────────────────────
        │
┌───────────────────────
│      传输层          │
└───────────────────────
        │
┌───────────────────────
│      安全层          │
└───────────────────────
        │
┌───────────────────────
│      MCP Agent 层    │
└───────────────────────
```

## 3. 协议格式

### 3.1 消息格式

```json
{
  "version": "0.7.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "ai.bridge.invoke",
  "params": {
    "service_id": "service-123",
    "method": "generate",
    "data": {
      "prompt": "Write a poem about AI",
      "model": "gpt-4"
    }
  },
  "metadata": {
    "sender_id": "mcp-agent-123",
    "trace_id": "trace-456",
    "security_level": "high",
    "timeout": 30000,
    "offline_mode": false,
    "cache_enabled": true
  },
  "signature": "digital_signature",
  "token": "access_token"
}
```

### 3.2 响应格式

```json
{
  "version": "0.7.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "response",
  "command": "ai.bridge.invoke",
  "request_id": "request-uuid",
  "result": {
    "status": "success",
    "data": {
      "output": "AI, a mirror to our minds...",
      "usage": {
        "prompt_tokens": 10,
        "completion_tokens": 50,
        "total_tokens": 60
      }
    },
    "cached": false,
    "offline": false
  },
  "error": null
}
```

## 4. 命令系统

### 4.1 核心命令

| 命令类型 | 功能描述 | 目标服务 |
|----------|----------|----------|
| ai.bridge.invoke | 调用 AI 服务 | 云服务 |
| ai.bridge.status | 获取 AI 服务状态 | 云服务 |
| ai.bridge.services | 列出可用 AI 服务 | 云服务 |
| ai.bridge.service.register | 注册 AI 服务 | 云服务 |
| ai.bridge.security.authenticate | 身份认证 | 云服务 |

### 4.2 v0.7.3 新增命令

| 命令类型 | 功能描述 | 目标服务 |
|----------|----------|----------|
| ai.bridge.offline.enable | 启用离线模式 | 本地 |
| ai.bridge.offline.sync | 离线数据同步 | 云服务 |
| ai.bridge.offline.status | 离线状态查询 | 本地 |
| ai.bridge.cache.get | 获取缓存数据 | 本地 |
| ai.bridge.cache.set | 设置缓存数据 | 本地 |
| ai.bridge.event.subscribe | 事件订阅 | 本地 |

### 4.3 扩展命令

| 命令类型 | 功能描述 | 目标服务 |
|----------|----------|----------|
| ai.bridge.llm.complete | 完成文本 | LLM 服务 |
| ai.bridge.llm.chat | 聊天对话 | LLM 服务 |
| ai.bridge.vision.generate | 生成图像 | 视觉服务 |
| ai.bridge.audio.speech | 语音合成 | 音频服务 |
| ai.bridge.search.web | 网页搜索 | 搜索服务 |

## 5. 安全机制

### 5.1 身份认证

- **OAuth 2.0**：使用标准的 OAuth 2.0 协议进行身份认证
- **API Key**：使用 API Key 进行简单认证
- **JWT**：使用 JSON Web Token 进行无状态认证
- **Mutual TLS**：使用双向 TLS 进行强安全认证
- **v0.7.3 新增**：本地认证缓存，支持离线认证

### 5.2 数据加密

- **传输加密**：使用 TLS 1.3 加密所有通信
- **端到端加密**：对敏感数据使用端到端加密
- **数据脱敏**：对敏感数据进行脱敏处理

### 5.3 访问控制

- **基于角色的访问控制**：根据用户角色设置不同的访问权限
- **基于策略的访问控制**：根据预定义策略控制资源访问
- **权限审计**：记录所有权限相关的操作

## 6. 离线支持（v0.7.3 新增）

### 6.1 离线模式

```java
public interface OfflineService {
    
    /**
     * 启用离线模式
     */
    void enableOfflineMode();
    
    /**
     * 禁用离线模式
     */
    void disableOfflineMode();
    
    /**
     * 检查是否为离线模式
     */
    boolean isOfflineMode();
    
    /**
     * 立即同步
     */
    CompletableFuture<SyncResult> syncNow();
    
    /**
     * 获取待同步变更
     */
    List<PendingChange> getPendingChanges();
}
```

### 6.2 离线流程

```
网络断开检测
    │
    ▼
启用离线模式
    │
    ├── 使用本地缓存响应请求
    ├── 暂存写操作
    └── 发布 OfflineModeEnabledEvent
    │
    ▼
网络恢复检测
    │
    ▼
自动同步
    │
    ├── 冲突检测
    ├── 冲突解决
    ├── 状态合并
    └── 发布 SyncCompletedEvent
```

### 6.3 缓存策略

| 缓存类型 | 说明 | 过期策略 |
|---------|------|---------|
| 响应缓存 | 缓存 AI 服务响应 | TTL 过期 |
| 模型缓存 | 缓存常用模型 | LRU 淘汰 |
| 配置缓存 | 缓存服务配置 | 永不过期 |

## 7. 事件管理（v0.7.3 新增）

### 7.1 事件类型

| 事件类型 | 说明 |
|---------|------|
| ServiceConnectedEvent | 服务连接成功 |
| ServiceDisconnectedEvent | 服务断开连接 |
| OfflineModeEnabledEvent | 离线模式启用 |
| SyncCompletedEvent | 同步完成 |
| CacheHitEvent | 缓存命中 |
| CacheMissEvent | 缓存未命中 |

### 7.2 事件订阅

```java
@PostConstruct
public void init() {
    eventBus.subscribe(ServiceConnectedEvent.class, this::onServiceConnected);
    eventBus.subscribe(OfflineModeEnabledEvent.class, this::onOfflineModeEnabled);
    eventBus.subscribe(SyncCompletedEvent.class, this::onSyncCompleted);
}
```

## 8. 服务发现

### 8.1 服务注册

1. **服务注册**：云服务向服务注册表注册自身信息
2. **服务发现**：MCP Agent 从服务注册表发现可用服务
3. **服务健康检查**：定期检查服务健康状态
4. **服务更新**：当服务状态变更时，更新服务注册表

### 8.2 服务发现机制

- **DNS 服务发现**：使用 DNS SRV 记录
- **Consul**：使用 Consul 进行服务发现
- **Etcd**：使用 Etcd 进行服务发现

## 9. 错误处理

### 9.1 错误码

| 错误码 | 错误描述 | HTTP状态码 |
|--------|----------|------------|
| 1000 | 参数错误 | 400 |
| 1001 | 认证失败 | 401 |
| 1002 | 权限不足 | 403 |
| 1003 | 资源不存在 | 404 |
| 1004 | 请求超时 | 408 |
| 2000 | 安全验证失败 | 401 |
| 2001 | 令牌过期 | 401 |
| 2003 | 广域网连接失败 | 504 |
| 3001 | 离线模式限制 | 409 |
| 3002 | 同步冲突 | 409 |
| 3003 | 缓存未命中 | 404 |

## 10. 参考资料

- [SuperAgent 核心协议文档](../main/protocol-main.md)
- [Agent 协议文档](../agent/agent-protocol.md)
- [P2P 协议文档](../p2p/p2p-protocol.md)
