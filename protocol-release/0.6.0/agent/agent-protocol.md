# Agent 协议分册

## 1. 概述

Agent 是 Ooder 智能体系统中的核心组件，负责执行任务、管理资源和与其他组件通信。Agent 协议是**南下协议**的具体实现，用于 mcpAgent、routeAgent、endAgent 以及它们的程序载体 skills 之间的通信。

### 1.1 协议体系定位

- **北上协议**：以 mcpAgent 为中心，向云服务（包括公云、私有云、混合云）通信的协议统称，也称为 AI Bridge 协议
- **南下协议**：mcpAgent 向下与其他组件通信的协议统称
- **Agent 协议**：南下协议的具体实现，本手册详细定义此协议
- **AI Bridge 协议**：北上协议的具体实现，用于与云服务通信

### 1.2 Agent 分类

| Agent 类型 | 主要职责 | 部署位置 |
|------------|----------|----------|
| MCP Agent | 资源管理和调度 | 中央服务器 |
| Route Agent | 消息路由和转发 | 边缘节点 |
| End Agent | 设备交互和数据采集 | 终端设备 |

### 1.3 设计原则

- **职责分离**：每种 Agent 专注于特定职责
- **层次化架构**：形成从中心到边缘的层次结构
- **通信标准化**：使用统一的 Agent 协议（南下）和 AI Bridge 协议（北上）
- **高可用性**：支持故障转移和负载均衡

## 2. MCP Agent 规范

### 2.1 功能定位

MCP (Master Control Plane) Agent 是系统的主控组件，负责全局资源管理和任务调度。

### 2.2 核心功能

| 功能 | 描述 | 优先级 |
|------|------|--------|
| 资源管理 | 管理全局资源，包括 Space、Zone 和设备 | 高 |
| 任务调度 | 分配和调度任务到合适的 Agent | 高 |
| 状态监控 | 监控所有 Agent 的运行状态 | 高 |
| 配置管理 | 管理全局配置和策略 | 中 |
| 安全控制 | 执行全局安全策略 | 高 |

### 2.3 接口协议

#### 2.3.1 资源管理接口

| 接口名称 | 资源创建 |
|----------|----------|
| URL | `/api/v1/resources` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 管理员权限 |

**请求参数：**

```json
{
  "resource_id": "string",
  "type": "space|zone|device",
  "name": "string",
  "description": "string",
  "properties": {
    "key1": "value1",
    "key2": "value2"
  },
  "parent_id": "string",
  "metadata": {
    "key1": "value1"
  }
}
```

#### 2.3.2 任务调度接口

| 接口名称 | 任务创建 |
|----------|----------|
| URL | `/api/v1/tasks` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求参数：**

```json
{
  "task_id": "string",
  "name": "string",
  "description": "string",
  "type": "task_type",
  "parameters": {
    "param1": "value1",
    "param2": "value2"
  },
  "priority": "high|medium|low",
  "target": {
    "agent_type": "end_agent",
    "filters": {
      "zone_id": "zone-123"
    }
  },
  "callback_url": "string",
  "timeout": "number"
}
```

### 2.4 数据模型

#### 2.4.1 资源数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| resource_id | String(36) | PRIMARY KEY | 资源唯一标识符 |
| type | String(64) | NOT NULL | 资源类型 |
| name | String(255) | NOT NULL | 资源名称 |
| description | String(1024) | | 资源描述 |
| properties | JSON | | 资源属性 |
| parent_id | String(36) | | 父资源 ID |
| status | String(32) | NOT NULL | 资源状态 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |

#### 2.4.2 任务数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| task_id | String(36) | PRIMARY KEY | 任务唯一标识符 |
| name | String(255) | NOT NULL | 任务名称 |
| description | String(1024) | | 任务描述 |
| type | String(64) | NOT NULL | 任务类型 |
| parameters | JSON | | 任务参数 |
| priority | String(32) | NOT NULL | 任务优先级 |
| status | String(32) | NOT NULL | 任务状态 |
| target | JSON | NOT NULL | 目标 Agent |
| callback_url | String(512) | | 回调 URL |
| timeout | Integer | | 超时时间 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |
| completed_at | Timestamp | | 完成时间 |

## 3. Route Agent 规范

### 3.1 功能定位

Route Agent 负责消息的路由和转发，是连接中心和边缘的桥梁。

### 3.2 核心功能

| 功能 | 描述 | 优先级 |
|------|------|--------|
| 消息路由 | 根据路由规则转发消息 | 高 |
| 负载均衡 | 在多个目标间分配负载 | 高 |
| 故障转移 | 自动切换到备用目标 | 高 |
| 本地缓存 | 缓存常用数据和消息 | 中 |
| 流量控制 | 控制消息流量，防止过载 | 中 |

### 3.3 接口协议

#### 3.3.1 路由规则管理接口

| 接口名称 | 路由规则创建 |
|----------|----------|
| URL | `/api/v1/routes` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 管理员权限 |

**请求参数：**

```json
{
  "route_id": "string",
  "name": "string",
  "description": "string",
  "source": {
    "agent_type": "end_agent",
    "filters": {
      "zone_id": "zone-123"
    }
  },
  "destination": {
    "agent_type": "mcp_agent",
    "endpoints": [
      {
        "url": "string",
        "weight": "number"
      }
    ]
  },
  "priority": "number",
  "enabled": "boolean"
}
```

#### 3.3.2 消息转发接口

| 接口名称 | 消息转发 |
|----------|----------|
| URL | `/api/v1/messages/forward` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求参数：**

```json
{
  "message_id": "string",
  "source_id": "string",
  "destination_id": "string",
  "payload": {
    "key1": "value1",
    "key2": "value2"
  },
  "metadata": {
    "priority": "high",
    "timeout": "number"
  }
}
```

### 3.4 数据模型

#### 3.4.1 路由规则数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| route_id | String(36) | PRIMARY KEY | 路由规则唯一标识符 |
| name | String(255) | NOT NULL | 路由规则名称 |
| description | String(1024) | | 路由规则描述 |
| source | JSON | NOT NULL | 消息源 |
| destination | JSON | NOT NULL | 消息目标 |
| priority | Integer | NOT NULL | 路由优先级 |
| enabled | Boolean | NOT NULL | 是否启用 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |

#### 3.4.2 消息队列数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| message_id | String(36) | PRIMARY KEY | 消息唯一标识符 |
| source_id | String(36) | NOT NULL | 消息源 ID |
| destination_id | String(36) | NOT NULL | 消息目标 ID |
| payload | JSON | NOT NULL | 消息内容 |
| status | String(32) | NOT NULL | 消息状态 |
| retry_count | Integer | NOT NULL | 重试次数 |
| max_retries | Integer | NOT NULL | 最大重试次数 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |
| processed_at | Timestamp | | 处理时间 |

## 4. End Agent 规范

### 4.1 功能定位

End Agent 是直接与终端设备和外部系统交互的组件，负责数据采集和命令执行。

### 4.2 核心功能

| 功能 | 描述 | 优先级 |
|------|------|--------|
| 数据采集 | 采集设备和传感器数据 | 高 |
| 命令执行 | 执行设备控制命令 | 高 |
| 本地处理 | 在边缘进行数据处理 | 中 |
| 状态上报 | 上报设备和自身状态 | 高 |
| 离线操作 | 支持离线模式和断点续传 | 中 |

### 4.3 接口协议

#### 4.3.1 数据采集接口

| 接口名称 | 数据上报 |
|----------|----------|
| URL | `/api/v1/data/report` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求参数：**

```json
{
  "data_id": "string",
  "device_id": "string",
  "sensor_id": "string",
  "timestamp": "timestamp",
  "value": "number|string|boolean|object",
  "unit": "string",
  "quality": "good|bad|unknown",
  "metadata": {
    "key1": "value1"
  }
}
```

#### 4.3.2 命令执行接口

| 接口名称 | 命令执行 |
|----------|----------|
| URL | `/api/v1/commands/execute` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求参数：**

```json
{
  "command_id": "string",
  "device_id": "string",
  "command": "string",
  "parameters": {
    "param1": "value1",
    "param2": "value2"
  },
  "timeout": "number",
  "callback_url": "string"
}
```

### 4.4 数据模型

#### 4.4.1 设备数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| device_id | String(36) | PRIMARY KEY | 设备唯一标识符 |
| name | String(255) | NOT NULL | 设备名称 |
| description | String(1024) | | 设备描述 |
| type | String(64) | NOT NULL | 设备类型 |
| status | String(32) | NOT NULL | 设备状态 |
| properties | JSON | | 设备属性 |
| zone_id | String(36) | NOT NULL | 所属区域 ID |
| last_seen | Timestamp | | 最后在线时间 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |

#### 4.4.2 传感器数据模型

| 属性名称 | 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| sensor_id | String(36) | PRIMARY KEY | 传感器唯一标识符 |
| device_id | String(36) | NOT NULL | 所属设备 ID |
| name | String(255) | NOT NULL | 传感器名称 |
| type | String(64) | NOT NULL | 传感器类型 |
| unit | String(32) | | 数据单位 |
| range_min | Number | | 测量范围最小值 |
| range_max | Number | | 测量范围最大值 |
| precision | Number | | 测量精度 |
| status | String(32) | NOT NULL | 传感器状态 |
| created_at | Timestamp | NOT NULL | 创建时间 |
| updated_at | Timestamp | NOT NULL | 更新时间 |

## 5. Agent 通信机制

### 5.1 通信模式

| 模式 | 描述 | 适用场景 |
|------|------|----------|
| 请求-响应 | 发送请求并等待响应 | 命令执行、状态查询 |
| 发布-订阅 | 发布消息到主题，订阅者接收 | 事件通知、数据广播 |
| 推送-拉取 | 主动推送或定期拉取数据 | 数据同步、状态更新 |
| 双向流 | 建立持久连接，双向通信 | 实时监控、远程控制 |

### 5.2 通信协议栈

```
┌────────────────┐
│ 应用层 (业务逻辑)    │
├────────────────┤
│ AI Bridge 协议层    │
├────────────────┤
│ 传输层 (HTTP/WS/MQTT) │
├────────────────┤
│ 安全层 (TLS/SSL)     │
└────────────────┘
```

## 6. 错误处理

### 6.1 Agent 通用错误码

| 错误码 | 描述 | HTTP状态码 |
|--------|------|------------|
| 3001 | Agent 不存在 | 404 |
| 3002 | Agent 不可用 | 503 |
| 3003 | 权限不足 | 403 |
| 3004 | 参数错误 | 400 |
| 3005 | 执行超时 | 504 |
| 3006 | 资源冲突 | 409 |

### 6.2 错误响应格式

```json
{
  "code": "3001",
  "message": "Agent not found",
  "details": {
    "agent_id": "agent-123",
    "agent_type": "end_agent"
  },
  "timestamp": "2026-01-18T00:00:00Z"
}
```

## 7. 部署与运行

### 7.1 部署架构

```
┌────────────────┐
│  MCP Agent     │
└────────────────┘
        │
        ├────────────────┐
        │                │
┌────────────────┐ ┌────────────────┐
│  Route Agent 1 │ │  Route Agent 2 │
└────────────────┘ └────────────────┘
        │                │
        ├────────────────┤
        │
┌────────────────┐
│  End Agent     │
└────────────────┘
```

### 7.2 运行环境要求

| Agent 类型 | CPU | 内存 | 存储 | 网络 |
|------------|-----|------|------|------|
| MCP Agent | 4核+ | 8GB+ | 100GB+ | 千兆网卡 |
| Route Agent | 2核+ | 4GB+ | 50GB+ | 千兆网卡 |
| End Agent | 1核+ | 1GB+ | 10GB+ | 百兆网卡 |

## 8. 监控与维护

### 8.1 监控指标

| Agent 类型 | 关键指标 |
|------------|----------|
| MCP Agent | CPU 使用率、内存使用率、任务处理速率、资源利用率 |
| Route Agent | CPU 使用率、内存使用率、消息转发速率、路由命中率 |
| End Agent | CPU 使用率、内存使用率、数据采集速率、命令执行成功率 |

### 8.2 日志管理

| Agent 类型 | 日志类型 |
|------------|----------|
| MCP Agent | 资源日志、任务日志、安全日志、系统日志 |
| Route Agent | 路由日志、消息日志、系统日志 |
| End Agent | 设备日志、数据日志、命令日志、系统日志 |

---

**Ooder Technology Co., Ltd.**