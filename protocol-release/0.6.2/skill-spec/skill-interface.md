# Skill 接口定义

## 1. 文档概述

本文档详细定义了 Ooder AI Bridge 协议 v0\.6\.2 �?Skill 服务�?API 接口，包括接口名称、URL、请求方法、参数、返回值和错误处理等内容�?
## 2. 接口分类

Skill 接口主要分为以下几类�?
1. **注册与发现接�?*：用�?Skill 的注册、注销和发�?2. **Skill 管理接口**：用�?Skill 的生命周期管�?3. **Capability 管理接口**：用�?Capability 的管�?4. **通信接口**：用于智能体�?Skill 之间的通信
5. **监控与维护接�?*：用�?Skill 的监控和维护

## 3. 接口定义

### 3.1 注册与发现接�?
#### 3.1.1 Skill 注册

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
  ],
  "metadata": {
    "key1": "value1"
  }
}
```

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "skill_id": "string",
    "status": "registered",
    "registered_at": "timestamp"
  }
}
```

#### 3.1.2 Skill 注销

| 接口名称 | Skill 注销 |
|----------|------------|
| URL | `/api/v1/skills/{skill_id}/unregister` |
| 方法 | POST |
| 认证 | �?|
| 权限 | 注销权限 |

**请求参数�?*

```json
{
  "skill_id": "string"
}
```

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "skill_id": "string",
    "status": "unregistered",
    "unregistered_at": "timestamp"
  }
}
```

#### 3.1.3 Skill 发现

| 接口名称 | Skill 发现 |
|----------|------------|
| URL | `/api/v1/skills/discover` |
| 方法 | GET |
| 认证 | �?|
| 权限 | 查询权限 |

**请求参数�?*

| 参数名称 | 类型 | 必�?| 描述 |
|----------|------|------|------|
| capability | string | �?| �?Capability 名称过滤 |
| category | string | �?| �?Capability 分类过滤 |
| status | string | �?| �?Skill 状态过�?|
| page | number | �?| 页码，默�?1 |
| page_size | number | �?| 每页数量，默�?10 |

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "total": "number",
    "page": "number",
    "page_size": "number",
    "skills": [
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
            "version": "string"
          }
        ],
        "status": "string",
        "registered_at": "timestamp"
      }
    ]
  }
}
```

### 3.2 Skill 管理接口

#### 3.2.1 Skill 状态查�?
| 接口名称 | Skill 状态查�?|
|----------|------------|
| URL | `/api/v1/skills/{skill_id}/status` |
| 方法 | GET |
| 认证 | �?|
| 权限 | 查询权限 |

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "skill_id": "string",
    "status": "active|inactive|maintenance",
    "health": "healthy|unhealthy|warning",
    "uptime": "number",
    "request_count": "number",
    "error_count": "number"
  }
}
```

#### 3.2.2 Skill 状态更�?
| 接口名称 | Skill 状态更�?|
|----------|------------|
| URL | `/api/v1/skills/{skill_id}/status` |
| 方法 | PUT |
| 认证 | �?|
| 权限 | 管理权限 |

**请求参数�?*

```json
{
  "status": "active|inactive|maintenance"
}
```

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "skill_id": "string",
    "status": "active|inactive|maintenance",
    "updated_at": "timestamp"
  }
}
```

### 3.3 Capability 管理接口

#### 3.3.1 Capability 查询

| 接口名称 | Capability 查询 |
|----------|------------|
| URL | `/api/v1/capabilities` |
| 方法 | GET |
| 认证 | �?|
| 权限 | 查询权限 |

**请求参数�?*

| 参数名称 | 类型 | 必�?| 描述 |
|----------|------|------|------|
| name | string | �?| �?Capability 名称过滤 |
| category | string | �?| �?Capability 分类过滤 |
| skill_id | string | �?| �?Skill ID 过滤 |
| page | number | �?| 页码，默�?1 |
| page_size | number | �?| 每页数量，默�?10 |

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "total": "number",
    "page": "number",
    "page_size": "number",
    "capabilities": [
      {
        "capability_id": "string",
        "name": "string",
        "description": "string",
        "category": "string",
        "version": "string",
        "skill_id": "string",
        "parameters": {
          "param1": {
            "type": "string",
            "required": "boolean",
            "description": "string"
          }
        },
        "status": "enabled|disabled"
      }
    ]
  }
}
```

### 3.4 通信接口

#### 3.4.1 命令执行

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
  "metadata": {
    "key1": "value1"
  },
  "callback_url": "string",
  "timeout": "number"
}
```

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "execution_id": "string",
    "status": "pending|running|completed|failed",
    "result": {
      "key1": "value1"
    },
    "output": "string",
    "error": "string",
    "started_at": "timestamp",
    "completed_at": "timestamp"
  }
}
```

#### 3.4.2 Channel 创建

| 接口名称 | Channel 创建 |
|----------|------------|
| URL | `/api/v1/channels/create` |
| 方法 | POST |
| 认证 | �?|
| 权限 | 创建权限 |

**请求参数�?*

```json
{
  "skill_id": "string",
  "capability_id": "string",
  "metadata": {
    "key1": "value1"
  },
  "expiry_time": "timestamp"
}
```

**返回参数�?*

```json
{
  "code": "number",
  "message": "string",
  "data": {
    "channel_id": "string",
    "skill_id": "string",
    "capability_id": "string",
    "status": "open",
    "created_at": "timestamp",
    "expiry_time": "timestamp"
  }
}
```

## 4. 错误码定�?
| 错误�?| 描述 | HTTP状态码 |
|--------|------|------------|
| 1001 | 参数错误 | 400 |
| 1002 | 认证失败 | 401 |
| 1003 | 权限不足 | 403 |
| 1004 | Skill 不存�?| 404 |
| 1005 | Capability 不存�?| 404 |
| 1006 | Skill 已注�?| 409 |
| 1007 | 内部错误 | 500 |
| 1008 | 服务不可�?| 503 |

## 5. 接口版本管理

| 版本�?| 发布日期 | 主要变化 |
|--------|----------|----------|
| v1.0 | 2026-01-18 | 初始版本 |
| v1.1 | 2026-03-01 | 增加批量操作支持 |
| v2.0 | 2026-06-01 | 重构接口结构，优化性能 |

## 6. 安全考虑

- 所有接口都需要认证和授权
- 敏感数据需要加密传�?- 接口调用需要进行频率限�?- 错误信息不应包含敏感内容
- 需要记录接口调用日志用于审�?
---

**Ooder Technology Co., Ltd.**
