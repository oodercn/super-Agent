# Skill 协议分册

## 1. 概述

Skill �?Ooder 智能体系统中提供特定功能的服务单元，通过 AI Bridge 协议与智能体和其他组件进行通信。本分册定义�?Skill 的需求规格、接口协议和数据模型�?
### 1.1 设计目标

- **功能模块�?*：将功能封装为独立的 Skill 单元
- **能力可组�?*：支持一�?Skill 提供多个 Capability
- **接口标准�?*：定义统一�?Skill 接口协议
- **部署灵活�?*：支持多种部署方式和运行环境

## 2. Skill-Capability 关系

### 2.1 关系模型

Skill �?Capability 之间是一对多的关系：

```
┌────────────────�?�? Skill 1       �?└────────────────�?        �?        ├─────────────�?        �?            �?┌────────────────�?┌────────────────�?�? Capability 1  �?�? Capability 2  �?└────────────────�?└────────────────�?```

### 2.2 关系特�?
- **独立�?*：Capability 可以独立定义和管�?- **组合�?*：多�?Capability 可以组合成一�?Skill
- **可扩展�?*：支持动态添加和移除 Capability
- **版本控制**：Skill �?Capability 都支持版本管�?
## 3. Skill 需求规�?
### 3.1 功能需�?
| 需求编�?| 需求描�?| 优先�?| 验收标准 |
|----------|----------|--------|----------|
| SKILL-REQ-001 | Skill 应支持注册到 Ooder 系统 | �?| Skill 能成功注册并被系统发�?|
| SKILL-REQ-002 | Skill 应支持从 Ooder 系统注销 | �?| Skill 能成功注销并从系统中移�?|
| SKILL-REQ-003 | Skill 应支持一�?Skill 对应多个 Capability | �?| 一�?Skill 能注册和管理多个 Capability |
| SKILL-REQ-004 | Skill 应支持状态查�?| �?| 能查�?Skill 的运行状态和健康信息 |
| SKILL-REQ-005 | Skill 应支持版本管�?| �?| 能标识和管理不同版本�?Skill |

### 3.2 非功能需�?
| 需求编�?| 需求描�?| 优先�?| 验收标准 |
|----------|----------|--------|----------|
| SKILL-NREQ-001 | Skill 应支持高并发请求 | �?| 能处�?1000 QPS 以上的请�?|
| SKILL-NREQ-002 | Skill 响应时间应小�?500ms | �?| 95% 的请求响应时间小�?500ms |
| SKILL-NREQ-003 | Skill 可用性应达到 99.9% | �?| 年度停机时间不超�?8.76 小时 |
| SKILL-NREQ-004 | Skill 应向后兼�?| �?| 新版�?Skill 能兼容旧版本的请求格�?|

## 4. Skill 接口定义

### 4.1 注册接口

| 接口名称 | Skill 注册 |
|----------|------------|
| URL | `/api/v1/skills/register` |
| 方法 | POST |
| 认证 | �?|
| 权限 | 注册权限 |

**请求参数�?*

```json
{
  "skill_id": "string",
  "name": "string",
  "description": "string",
  "version": "string",
  "endpoints": [
    {
      "url": "string",
      "protocol": "http|https|ws|wss",
      "weight": "number"
    }
  ],
  "capabilities": [
    {
      "capability_id": "string",
      "name": "string",
      "description": "string",
      "category": "string",
      "version": "string",
      "parameters": {
        "param1": {
          "type": "string",
          "required": "boolean",
          "description": "string"
        }
      }
    }
  ]
}
```

### 4.2 命令执行接口

| 接口名称 | 命令执行 |
|----------|------------|
| URL | `/api/v1/commands/execute` |
| 方法 | POST |
| 认证 | �?|
| 权限 | 执行权限 |

**请求参数�?*

```json
{
  "command_id": "string",
  "skill_id": "string",
  "capability_id": "string",
  "parameters": {
    "param1": "value1",
    "param2": "value2"
  },
  "callback_url": "string",
  "timeout": "number"
}
```

### 4.3 健康检查接�?
| 接口名称 | 健康检�?|
|----------|------------|
| URL | `/health` |
| 方法 | GET |
| 认证 | �?|
| 权限 | �?|

**返回参数�?*

```json
{
  "status": "healthy|unhealthy|warning",
  "version": "string",
  "uptime": "number",
  "timestamp": "timestamp"
}
```

## 5. Skill 数据模型

### 5.1 Skill 数据模型

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| skill_id | String(36) | PRIMARY KEY | Skill 唯一标识符，使用 UUID |
| name | String(255) | NOT NULL | Skill 名称 |
| description | String(1024) | | Skill 描述 |
| version | String(32) | NOT NULL | Skill 版本，遵循语义化版本规范 |
| status | Enum | NOT NULL | Skill 状态：active, inactive, maintenance |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 5.2 Endpoint 数据模型

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| endpoint_id | String(36) | PRIMARY KEY | Endpoint 唯一标识符，使用 UUID |
| skill_id | String(36) | FOREIGN KEY | 所�?Skill ID |
| url | String(512) | NOT NULL | Endpoint URL |
| protocol | Enum | NOT NULL | 协议类型：http, https, ws, wss |
| weight | Integer | NOT NULL DEFAULT 1 | 权重，用于负载均�?|
| status | Enum | NOT NULL | 状态：active, inactive |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 5.3 Capability 数据模型

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| capability_id | String(36) | PRIMARY KEY | Capability 唯一标识符，使用 UUID |
| skill_id | String(36) | FOREIGN KEY | 所�?Skill ID |
| name | String(255) | NOT NULL | Capability 名称 |
| description | String(1024) | | Capability 描述 |
| category | String(64) | NOT NULL | Capability 分类 |
| version | String(32) | NOT NULL | Capability 版本，遵循语义化版本规范 |
| status | Enum | NOT NULL | 状态：enabled, disabled |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 5.4 CapabilityParam 数据模型

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| param_id | String(36) | PRIMARY KEY | 参数唯一标识符，使用 UUID |
| capability_id | String(36) | FOREIGN KEY | 所�?Capability ID |
| name | String(64) | NOT NULL | 参数名称 |
| type | String(32) | NOT NULL | 参数类型：string, number, boolean, object, array |
| required | Boolean | NOT NULL DEFAULT false | 是否必填 |
| default_value | String(512) | | 默认�?|
| description | String(255) | | 参数描述 |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

## 6. Skill 协议交互流程

### 6.1 Skill 注册流程

```
┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? Skill 服务    │────▶│  注册中心      │────▶│  数据�?       �?└────────────────�?    └────────────────�?    └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        └───────────────────────┴───────────────────────�?                                  返回结果
```

### 6.2 Skill 调用流程

```
┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? Agent         │────▶│  路由服务      │────▶│  Skill 服务    �?└────────────────�?    └────────────────�?    └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        └───────────────────────┴───────────────────────�?                                  返回结果
```

### 6.3 MCP Agent 内部 UDP 广播与服务发现流�?
**重要说明**：Skill 系统通过 TCP/HTTP 协议�?AI Bridge 通信。UDP 广播仅在 MCP Agent 内部用于服务发现，需严格遵循安全认证流程�?
#### 6.3.1 UDP 通信技术规�?
| 配置�?| 取�?| 描述 |
|--------|------|------|
| UDP 端口 | 5000 | MCP Agent 内部服务发现�?UDP 广播端口 |
| 广播地址 | 255.255.255.255 | UDP 广播地址（仅在本地网络使用） |
| 缓冲区大�?| 1024 字节 | UDP 数据包缓冲区大小 |
| 超时时间 | 5 �?| 广播响应超时时间 |
| 广播频率 | 30 �?| 服务发现广播间隔 |

#### 6.3.2 安全广播消息格式

**发现请求消息**（带安全认证）：
```
DISCOVER:SKILL:{agentId};{agentName};{agentType};{agentEndpoint};{timestamp};{signature}
```

- `agentId`: 智能体唯一标识�?- `agentName`: 智能体名�?- `agentType`: 智能体类型（如：skill-a, skill-b�?- `agentEndpoint`: 智能体服务端�?URL
- `timestamp`: 消息发送时间戳（毫秒）
- `signature`: 使用预共享密钥生成的 HMAC-SHA256 签名

**加入响应消息**（带安全认证）：
```
JOIN_RESPONSE:SKILL:{agentId};{agentType};{sceneId};{status};{timestamp};{signature}
```

- `agentId`: 智能体唯一标识�?- `agentType`: 智能体类�?- `sceneId`: 场景 ID
- `status`: 加入状态（SUCCESS/FAILED�?- `timestamp`: 消息发送时间戳（毫秒）
- `signature`: 使用预共享密钥生成的 HMAC-SHA256 签名

#### 6.3.3 安全认证流程

1. **预共享密钥分�?*：MCP Agent 内部组件在部署时配置共享安全密钥
2. **消息签名**：发送方使用密钥和消息内容生�?HMAC-SHA256 签名
3. **签名验证**：接收方使用相同密钥验证消息完整性和真实�?4. **时间戳检�?*：拒绝超�?5 秒的过时消息，防止重放攻�?5. **权限验证**：检查发送方类型和权限，拒绝未授权组�?6. **加密会话**：广播后建立 TCP 连接进行关键信息的加密通信

#### 6.3.4 自主场景加入流程（带安全认证�?
```
┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? MCP 内部组件  │────▶│  UDP 广播      │────▶│  MCP 内部组件  �?└────────────────�?    └────────────────�?    └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?              ┌────────────────�?        �?                      �?              �? 安全认证      �?        �?                      �?              └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?              ┌────────────────�?        �?                      �?              �? Scene 管理    �?        �?                      �?              └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?                      �?        �?              ┌────────────────�?             �?        �?              �? UDP 响应      │◀─────────────�?        └──────────────▶└────────────────�?```

1. **广播发现请求**：MCP 内部组件发送带签名�?UDP 广播
2. **安全验证**：接收方验证签名和时间戳
3. **权限检�?*：验证发送方身份和权�?4. **场景管理**：通过认证后创建或获取场景
5. **响应确认**：发送带签名�?UDP 响应，确认加入结�?
#### 6.3.5 协议实现安全要求

1. **仅限内部使用**：UDP 广播仅在 MCP Agent 内部使用，禁止跨 MCP 网络
2. **本地网络限制**：仅在本地子网广播，不跨网段
3. **强制签名**：所�?UDP 消息必须包含 HMAC-SHA256 签名
4. **防重放机�?*：基于时间戳的重放攻击防�?5. **最小权限原�?*：组件仅能访问必要资�?6. **密钥管理**：定期轮换预共享密钥，确保安全�?7. **日志审计**：记录所�?UDP 通信，便于安全审�?8. **错误处理**：签名验证失败时丢弃消息并记录安全日�?
## 7. 错误处理

### 7.1 Skill 错误�?
| 错误�?| 描述 | HTTP状态码 |
|--------|------|------------|
| 2001 | Skill 不存�?| 404 |
| 2002 | Capability 不存�?| 404 |
| 2003 | Skill 不可�?| 503 |
| 2004 | Capability 不可�?| 503 |
| 2005 | 参数错误 | 400 |
| 2006 | 执行超时 | 504 |

### 7.2 错误响应格式

```json
{
  "code": "2001",
  "message": "Skill not found",
  "details": {
    "skill_id": "skill-123"
  },
  "timestamp": "2026-01-18T00:00:00Z"
}
```

## 8. 部署与运�?
### 8.1 部署方式

| 部署方式 | 特点 | 适用场景 |
|----------|------|----------|
| 容器化部�?| 轻量级、易扩展 | 云平台、微服务架构 |
| 裸机部署 | 高性能、低开销 | 专用服务器、边缘设�?|
| Serverless 部署 | 按需计费、自动扩�?| 流量波动大的场景 |

### 8.2 运行环境要求

| 环境 | 要求 |
|------|------|
| Python | 3.9+ |
| Go | 1.18+ |
| Node.js | 16+ |
| Java | 11+ |
| 内存 | 1GB+ |
| CPU | 1�? |

## 9. 监控与维�?
### 9.1 监控指标

| 指标 | 描述 | 监控频率 |
|------|------|----------|
| 请求�?| 每秒处理的请求数�?| 1�?|
| 响应时间 | 请求响应的平均时�?| 1�?|
| 错误�?| 错误请求的百分比 | 1�?|
| 资源使用�?| CPU和内存的使用�?| 5�?|
| 连接�?| 活跃连接的数�?| 5�?|

### 9.2 日志管理

| 日志类型 | 内容 | 存储周期 |
|----------|------|----------|
| 访问日志 | 请求和响应信�?| 7�?|
| 错误日志 | 错误和异常信�?| 30�?|
| 审计日志 | 关键操作记录 | 90�?|
| 性能日志 | 性能指标数据 | 14�?|

## 10. 兼容�?
### 10.1 向后兼容

- 支持旧版本的 API 接口
- 支持旧版本的数据格式
- 支持旧版本的命令系统

### 10.2 向前兼容

- 支持新功能的渐进式引�?- 支持新参数的可选使�?- 支持新数据格式的逐步过渡
