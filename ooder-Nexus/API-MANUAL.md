# API 接口文档

## 1. API 概述

Nexus 是一个基于 ooderAgent 架构的 P2P AI 能力分发枢纽产品，采用 MIT 开源协议。当前版本是在 ooderAgent 核心 0.6.6 开发的预览版程序，0.6.6 以后会独立建立分支。部分程序未经严格测试，正式发布版本前仅供研究学习。

### SDK 0.6.6 与示例程序说明

SDK 0.6.6 实现了完整的 ooderAgent 协议，为 Nexus 提供了稳定的底层通信能力。Nexus 在 SDK 基础的通讯和能力管理上，针对常见的路由器安装、智能设备安装提供了网络和路由管理示例页面，同时针对家庭网关的特殊设备也提供了相应的开发示例。

开发者可以通过 skillsCenter 下载相应的 skills 插件，安装在自己的路由器或网关上，就可以有针对性地进行开发，使其支持跨生态的 AI 分发服务。

### 1.2 API 架构

Nexus 提供 RESTful API 接口，支持 HTTP/HTTPS 协议，采用 JSON 格式进行数据交换。API 接口按功能模块分为多个命名空间，便于管理和使用。

### 1.3 API 版本

当前 API 版本为 v1，所有 API 路径均以 `/api` 开头。

### 1.4 认证方式

Nexus API 支持以下认证方式：
- **基于 KEY 的认证**：通过请求头中的 `Authorization` 字段传递认证信息
- **会话认证**：通过 Cookie 保持会话状态
- **匿名访问**：部分公开 API 支持匿名访问

### 1.5 响应格式

所有 API 响应均采用统一的 JSON 格式：

```json
{
  "success": true,        // 操作是否成功
  "data": {},            // 响应数据
  "message": "Success",  // 响应消息
  "code": 200,           // 响应代码
  "timestamp": 1234567890 // 时间戳
}
```

### 1.6 错误处理

API 错误通过 HTTP 状态码和响应体中的错误信息表示：

| 状态码 | 描述 | 含义 |
|-------|------|------|
| 400 | Bad Request | 请求参数错误 |
| 401 | Unauthorized | 未授权访问 |
| 403 | Forbidden | 权限不足 |
| 404 | Not Found | 资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |
| 503 | Service Unavailable | 服务不可用 |

## 2. 个人中心 API (/api/personal)

### 2.1 仪表盘统计

#### 2.1.1 获取个人仪表盘统计

- **方法**：GET
- **路径**：`/api/personal/dashboard/stats`
- **描述**：获取个人统计数据、活动记录、执行统计
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "skillCount": 10,
    "executionCount": 100,
    "sharedCount": 5,
    "receivedCount": 3,
    "groupCount": 2,
    "recentActivities": [
      {
        "type": "skill_executed",
        "message": "Executed skill 'Text Processing'",
        "timestamp": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

### 2.2 技能管理

#### 2.2.1 获取个人技能列表

- **方法**：GET
- **路径**：`/api/personal/skills`
- **描述**：获取个人技能列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
  - `category` (可选)：技能分类
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 10,
    "page": 1,
    "size": 10,
    "skills": [
      {
        "skillId": "com.example.skill.TextProcessing",
        "name": "Text Processing",
        "version": "1.0.0",
        "description": "A simple text processing skill",
        "category": "Utilities",
        "author": "Developer",
        "createdAt": 1234567890,
        "updatedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.2.2 发布个人技能

- **方法**：POST
- **路径**：`/api/personal/skills`
- **描述**：发布个人技能
- **参数**：
  - `skillFile` (必填)：技能 JAR 文件
  - `skillJson` (必填)：技能描述 JSON 文件
- **响应**：

```json
{
  "success": true,
  "data": {
    "skillId": "com.example.skill.TextProcessing",
    "name": "Text Processing",
    "version": "1.0.0",
    "status": "published"
  },
  "message": "Skill published successfully",
  "code": 200
}
```

#### 2.2.3 更新个人技能

- **方法**：PUT
- **路径**：`/api/personal/skills/{skillId}`
- **描述**：更新个人技能
- **参数**：
  - `skillId` (必填)：技能 ID
  - `skillFile` (可选)：更新的技能 JAR 文件
  - `skillJson` (可选)：更新的技能描述 JSON 文件
- **响应**：

```json
{
  "success": true,
  "data": {
    "skillId": "com.example.skill.TextProcessing",
    "name": "Text Processing",
    "version": "1.1.0",
    "status": "updated"
  },
  "message": "Skill updated successfully",
  "code": 200
}
```

#### 2.2.4 删除个人技能

- **方法**：DELETE
- **路径**：`/api/personal/skills/{skillId}`
- **描述**：删除个人技能
- **参数**：
  - `skillId` (必填)：技能 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "skillId": "com.example.skill.TextProcessing",
    "status": "deleted"
  },
  "message": "Skill deleted successfully",
  "code": 200
}
```

### 2.3 执行管理

#### 2.3.1 执行个人技能

- **方法**：POST
- **路径**：`/api/personal/execution/execute/{skillId}`
- **描述**：执行个人技能
- **参数**：
  - `skillId` (必填)：技能 ID
  - `parameters` (必填)：执行参数
- **请求示例**：

```json
{
  "parameters": {
    "input": "Hello, Nexus!"
  }
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "executionId": "exec-123456",
    "skillId": "com.example.skill.TextProcessing",
    "result": "Processed: Hello, Nexus!",
    "executionTime": 123,
    "status": "completed"
  },
  "message": "Skill executed successfully",
  "code": 200
}
```

#### 2.3.2 获取执行历史

- **方法**：GET
- **路径**：`/api/personal/execution/history`
- **描述**：获取执行历史
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
  - `skillId` (可选)：技能 ID
  - `status` (可选)：执行状态
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "executions": [
      {
        "executionId": "exec-123456",
        "skillId": "com.example.skill.TextProcessing",
        "skillName": "Text Processing",
        "parameters": {
          "input": "Hello, Nexus!"
        },
        "result": "Processed: Hello, Nexus!",
        "executionTime": 123,
        "status": "completed",
        "createdAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.3.3 获取执行结果

- **方法**：GET
- **路径**：`/api/personal/execution/result/{executionId}`
- **描述**：获取执行结果
- **参数**：
  - `executionId` (必填)：执行 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "executionId": "exec-123456",
    "skillId": "com.example.skill.TextProcessing",
    "skillName": "Text Processing",
    "result": "Processed: Hello, Nexus!",
    "executionTime": 123,
    "status": "completed",
    "createdAt": 1234567890
  },
  "message": "Success",
  "code": 200
}
```

### 2.4 分享管理

#### 2.4.1 获取分享的技能列表

- **方法**：GET
- **路径**：`/api/personal/sharing/shared`
- **描述**：获取分享的技能列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 5,
    "page": 1,
    "size": 10,
    "sharings": [
      {
        "sharingId": "share-123456",
        "skillId": "com.example.skill.TextProcessing",
        "skillName": "Text Processing",
        "targetType": "user",
        "targetId": "user123",
        "sharedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.4.2 获取收到的技能列表

- **方法**：GET
- **路径**：`/api/personal/sharing/received`
- **描述**：获取收到的技能列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 3,
    "page": 1,
    "size": 10,
    "receivings": [
      {
        "sharingId": "share-654321",
        "skillId": "com.example.skill.MathSkill",
        "skillName": "Math Skill",
        "fromType": "user",
        "fromId": "user456",
        "receivedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.4.3 分享技能

- **方法**：POST
- **路径**：`/api/personal/sharing`
- **描述**：分享技能
- **参数**：
  - `skillId` (必填)：技能 ID
  - `targetType` (必填)：目标类型 (user/group)
  - `targetId` (必填)：目标 ID
- **请求示例**：

```json
{
  "skillId": "com.example.skill.TextProcessing",
  "targetType": "user",
  "targetId": "user123"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "sharingId": "share-123456",
    "skillId": "com.example.skill.TextProcessing",
    "targetType": "user",
    "targetId": "user123",
    "status": "shared"
  },
  "message": "Skill shared successfully",
  "code": 200
}
```

### 2.5 群组管理

#### 2.5.1 获取我的群组列表

- **方法**：GET
- **路径**：`/api/personal/groups`
- **描述**：获取我的群组列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 2,
    "page": 1,
    "size": 10,
    "groups": [
      {
        "groupId": "group-123456",
        "name": "Development Team",
        "description": "Development team group",
        "memberCount": 5,
        "createdAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.5.2 获取群组技能列表

- **方法**：GET
- **路径**：`/api/personal/groups/{groupId}/skills`
- **描述**：获取群组技能列表
- **参数**：
  - `groupId` (必填)：群组 ID
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 10,
    "page": 1,
    "size": 10,
    "skills": [
      {
        "skillId": "com.example.skill.TextProcessing",
        "name": "Text Processing",
        "version": "1.0.0",
        "description": "A simple text processing skill",
        "addedBy": "user123",
        "addedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

### 2.6 身份管理

#### 2.6.1 获取个人身份信息

- **方法**：GET
- **路径**：`/api/personal/identity`
- **描述**：获取个人身份信息
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "userId": "user-123456",
    "username": "developer",
    "displayName": "Developer",
    "email": "developer@example.com",
    "avatar": "avatar.png",
    "createdAt": 1234567890
  },
  "message": "Success",
  "code": 200
}
```

#### 2.6.2 更新个人身份信息

- **方法**：PUT
- **路径**：`/api/personal/identity`
- **描述**：更新个人身份信息
- **参数**：
  - `displayName` (可选)：显示名称
  - `email` (可选)：邮箱
  - `avatar` (可选)：头像
- **请求示例**：

```json
{
  "displayName": "Senior Developer",
  "email": "senior.developer@example.com"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "userId": "user-123456",
    "username": "developer",
    "displayName": "Senior Developer",
    "email": "senior.developer@example.com",
    "avatar": "avatar.png",
    "updatedAt": 1234567890
  },
  "message": "Identity updated successfully",
  "code": 200
}
```

### 2.7 帮助与支持

#### 2.7.1 获取帮助文档

- **方法**：GET
- **路径**：`/api/personal/help`
- **描述**：获取帮助文档
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "documents": [
      {
        "title": "User Guide",
        "url": "/docs/user-guide",
        "description": "User guide for Nexus"
      },
      {
        "title": "API Manual",
        "url": "/docs/api-manual",
        "description": "API manual for Nexus"
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 2.7.2 获取系统信息

- **方法**：GET
- **路径**：`/api/personal/system/info`
- **描述**：获取系统信息
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "version": "1.0.0",
    "buildTime": "2025-11-04T12:00:00Z",
    "os": "Windows 10",
    "javaVersion": "1.8.0_301",
    "memory": {
      "total": "4096MB",
      "used": "2048MB",
      "free": "2048MB"
    }
  },
  "message": "Success",
  "code": 200
}
```

## 3. P2P 网络 API (/api/network)

### 3.1 网络发现

#### 3.1.1 发现网络中的其他节点

- **方法**：GET
- **路径**：`/api/network/discovery`
- **描述**：发现网络中的其他节点
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "nodes": [
      {
        "nodeId": "node-123456",
        "name": "Nexus Node 1",
        "type": "nexus",
        "ip": "192.168.1.100",
        "port": 9876,
        "status": "online",
        "lastSeen": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 3.1.2 加入 P2P 网络

- **方法**：POST
- **路径**：`/api/network/join`
- **描述**：加入 P2P 网络
- **参数**：
  - `seedNodes` (可选)：种子节点列表
- **请求示例**：

```json
{
  "seedNodes": [
    {
      "ip": "192.168.1.100",
      "port": 9876
    }
  ]
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "status": "joined",
    "nodeId": "node-789012",
    "peers": 5
  },
  "message": "Joined network successfully",
  "code": 200
}
```

### 3.2 节点管理

#### 3.2.1 获取网络节点列表

- **方法**：GET
- **路径**：`/api/network/nodes`
- **描述**：获取网络节点列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
  - `status` (可选)：节点状态
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 10,
    "page": 1,
    "size": 10,
    "nodes": [
      {
        "nodeId": "node-123456",
        "name": "Nexus Node 1",
        "type": "nexus",
        "ip": "192.168.1.100",
        "port": 9876,
        "status": "online",
        "lastSeen": 1234567890,
        "skills": 5
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 3.2.2 获取节点详情

- **方法**：GET
- **路径**：`/api/network/nodes/{nodeId}`
- **描述**：获取节点详情
- **参数**：
  - `nodeId` (必填)：节点 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "nodeId": "node-123456",
    "name": "Nexus Node 1",
    "type": "nexus",
    "ip": "192.168.1.100",
    "port": 9876,
    "status": "online",
    "lastSeen": 1234567890,
    "skills": 5,
    "resources": {
      "cpu": "20%",
      "memory": "50%",
      "disk": "30%"
    }
  },
  "message": "Success",
  "code": 200
}
```

#### 3.2.3 移除节点

- **方法**：DELETE
- **路径**：`/api/network/nodes/{nodeId}`
- **描述**：移除节点
- **参数**：
  - `nodeId` (必填)：节点 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "nodeId": "node-123456",
    "status": "removed"
  },
  "message": "Node removed successfully",
  "code": 200
}
```

### 3.3 技能分发

#### 3.3.1 发布技能到网络

- **方法**：POST
- **路径**：`/api/network/skill/publish`
- **描述**：发布技能到网络
- **参数**：
  - `skillId` (必填)：技能 ID
  - `visibility` (可选)：可见性 (public/private)
- **请求示例**：

```json
{
  "skillId": "com.example.skill.TextProcessing",
  "visibility": "public"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "skillId": "com.example.skill.TextProcessing",
    "status": "published",
    "nodes": 5
  },
  "message": "Skill published to network successfully",
  "code": 200
}
```

#### 3.3.2 订阅网络中的技能

- **方法**：POST
- **路径**：`/api/network/skill/subscribe`
- **描述**：订阅网络中的技能
- **参数**：
  - `skillId` (必填)：技能 ID
  - `nodeId` (必填)：发布节点 ID
- **请求示例**：

```json
{
  "skillId": "com.example.skill.MathSkill",
  "nodeId": "node-123456"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "skillId": "com.example.skill.MathSkill",
    "nodeId": "node-123456",
    "status": "subscribed"
  },
  "message": "Skill subscribed successfully",
  "code": 200
}
```

#### 3.3.3 获取技能市场列表

- **方法**：GET
- **路径**：`/api/network/skill/market`
- **描述**：获取技能市场列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
  - `category` (可选)：技能分类
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 20,
    "page": 1,
    "size": 10,
    "skills": [
      {
        "skillId": "com.example.skill.TextProcessing",
        "name": "Text Processing",
        "version": "1.0.0",
        "description": "A simple text processing skill",
        "category": "Utilities",
        "author": "Developer",
        "publisherId": "node-123456",
        "publisherName": "Nexus Node 1",
        "publishedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

### 3.4 网络拓扑

#### 3.4.1 获取网络拓扑

- **方法**：GET
- **路径**：`/api/network/topology`
- **描述**：获取网络拓扑
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "nodes": [
      {
        "id": "node-123456",
        "name": "Nexus Node 1",
        "type": "nexus",
        "status": "online"
      }
    ],
    "links": [
      {
        "source": "node-123456",
        "target": "node-789012",
        "strength": 0.8
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

## 4. 监控 API (/api/monitor)

### 4.1 系统指标

#### 4.1.1 获取系统指标

- **方法**：GET
- **路径**：`/api/monitor/metrics`
- **描述**：获取系统指标
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "cpu": {
      "usage": "20%",
      "cores": 4
    },
    "memory": {
      "total": "4096MB",
      "used": "2048MB",
      "free": "2048MB"
    },
    "disk": {
      "total": "50GB",
      "used": "15GB",
      "free": "35GB"
    },
    "network": {
      "in": "1MB/s",
      "out": "500KB/s"
    }
  },
  "message": "Success",
  "code": 200
}
```

#### 4.1.2 获取历史指标数据

- **方法**：GET
- **路径**：`/api/monitor/metrics/history`
- **描述**：获取历史指标数据
- **参数**：
  - `metric` (必填)：指标类型 (cpu/memory/disk/network)
  - `duration` (可选)：时间范围 (1h/6h/24h/7d)
- **响应**：

```json
{
  "success": true,
  "data": {
    "metric": "cpu",
    "duration": "1h",
    "data": [
      {
        "timestamp": 1234567890,
        "value": 20
      },
      {
        "timestamp": 1234567900,
        "value": 25
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

### 4.2 告警管理

#### 4.2.1 获取告警列表

- **方法**：GET
- **路径**：`/api/monitor/alerts`
- **描述**：获取告警列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
  - `status` (可选)：告警状态 (active/resolved)
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 5,
    "page": 1,
    "size": 10,
    "alerts": [
      {
        "alertId": "alert-123456",
        "type": "cpu",
        "message": "CPU usage exceeds 80%",
        "severity": "warning",
        "status": "active",
        "createdAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 4.2.2 清除告警

- **方法**：POST
- **路径**：`/api/monitor/alerts/clear`
- **描述**：清除告警
- **参数**：
  - `alertId` (必填)：告警 ID
- **请求示例**：

```json
{
  "alertId": "alert-123456"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "alertId": "alert-123456",
    "status": "resolved"
  },
  "message": "Alert cleared successfully",
  "code": 200
}
```

### 4.3 健康检查

#### 4.3.1 获取系统健康状态

- **方法**：GET
- **路径**：`/api/monitor/health`
- **描述**：获取系统健康状态
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "status": "healthy",
    "components": {
      "system": "healthy",
      "network": "healthy",
      "storage": "healthy",
      "skills": "healthy"
    }
  },
  "message": "Success",
  "code": 200
}
```

## 5. 存储管理 API (/api/storage)

### 5.1 存储空间

#### 5.1.1 获取存储空间信息

- **方法**：GET
- **路径**：`/api/storage/space`
- **描述**：获取存储空间信息
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 10737418240,  // 总空间（字节）
    "used": 2147483648,   // 已用空间（字节）
    "free": 8589934592,   // 可用空间（字节）
    "usage": 20           // 使用率（百分比）
  },
  "message": "Success",
  "code": 200
}
```

### 5.2 文件夹管理

#### 5.2.1 获取文件夹内容

- **方法**：GET
- **路径**：`/api/storage/folder/{folderId}/children`
- **描述**：获取文件夹内容
- **参数**：
  - `folderId` (必填)：文件夹 ID，root 表示根目录
- **响应**：

```json
{
  "success": true,
  "data": {
    "folderId": "root",
    "name": "Root",
    "children": [
      {
        "id": "folder-123456",
        "name": "Documents",
        "type": "folder",
        "createdAt": 1234567890
      },
      {
        "id": "file-654321",
        "name": "example.txt",
        "type": "file",
        "size": 1024,
        "createdAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 5.2.2 创建文件夹

- **方法**：POST
- **路径**：`/api/storage/folder`
- **描述**：创建文件夹
- **参数**：
  - `name` (必填)：文件夹名称
  - `parentId` (必填)：父文件夹 ID
- **请求示例**：

```json
{
  "name": "New Folder",
  "parentId": "root"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "folderId": "folder-123456",
    "name": "New Folder",
    "parentId": "root",
    "status": "created"
  },
  "message": "Folder created successfully",
  "code": 200
}
```

#### 5.2.3 删除文件夹

- **方法**：DELETE
- **路径**：`/api/storage/folder/{folderId}`
- **描述**：删除文件夹
- **参数**：
  - `folderId` (必填)：文件夹 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "folderId": "folder-123456",
    "status": "deleted"
  },
  "message": "Folder deleted successfully",
  "code": 200
}
```

### 5.3 文件管理

#### 5.3.1 上传文件

- **方法**：POST
- **路径**：`/api/storage/upload`
- **描述**：上传文件
- **参数**：
  - `file` (必填)：文件内容
  - `folderId` (必填)：目标文件夹 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "fileId": "file-123456",
    "name": "example.txt",
    "size": 1024,
    "folderId": "root",
    "status": "uploaded"
  },
  "message": "File uploaded successfully",
  "code": 200
}
```

#### 5.3.2 下载文件

- **方法**：GET
- **路径**：`/api/storage/download/{fileId}`
- **描述**：下载文件
- **参数**：
  - `fileId` (必填)：文件 ID
- **响应**：
  - 文件内容（二进制）

#### 5.3.3 删除文件

- **方法**：DELETE
- **路径**：`/api/storage/file/{fileId}`
- **描述**：删除文件
- **参数**：
  - `fileId` (必填)：文件 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "fileId": "file-123456",
    "status": "deleted"
  },
  "message": "File deleted successfully",
  "code": 200
}
```

#### 5.3.4 更新文件信息

- **方法**：PUT
- **路径**：`/api/storage/file/{fileId}`
- **描述**：更新文件信息
- **参数**：
  - `fileId` (必填)：文件 ID
  - `name` (可选)：新文件名
- **请求示例**：

```json
{
  "name": "updated-example.txt"
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "fileId": "file-123456",
    "name": "updated-example.txt",
    "status": "updated"
  },
  "message": "File updated successfully",
  "code": 200
}
```

### 5.4 版本管理

#### 5.4.1 获取文件版本列表

- **方法**：GET
- **路径**：`/api/storage/file/{fileId}/versions`
- **描述**：获取文件版本列表
- **参数**：
  - `fileId` (必填)：文件 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "fileId": "file-123456",
    "versions": [
      {
        "versionId": "version-123456",
        "version": 1,
        "size": 1024,
        "createdAt": 1234567890,
        "createdBy": "user-123456"
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 5.4.2 恢复文件版本

- **方法**：POST
- **路径**：`/api/storage/file/{fileId}/restore/{versionId}`
- **描述**：恢复文件版本
- **参数**：
  - `fileId` (必填)：文件 ID
  - `versionId` (必填)：版本 ID
- **响应**：

```json
{
  "success": true,
  "data": {
    "fileId": "file-123456",
    "versionId": "version-123456",
    "status": "restored"
  },
  "message": "File version restored successfully",
  "code": 200
}
```

### 5.5 存储清理

#### 5.5.1 清理存储缓存

- **方法**：POST
- **路径**：`/api/storage/cleanup`
- **描述**：清理存储缓存
- **参数**：无
- **响应**：

```json
{
  "success": true,
  "data": {
    "freedSpace": 104857600,  // 释放的空间（字节）
    "status": "completed"
  },
  "message": "Storage cleanup completed successfully",
  "code": 200
}
```

### 5.6 文件分享

#### 5.6.1 获取分享的文件列表

- **方法**：GET
- **路径**：`/api/storage/shared`
- **描述**：获取分享的文件列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 5,
    "page": 1,
    "size": 10,
    "sharings": [
      {
        "sharingId": "share-123456",
        "fileId": "file-123456",
        "fileName": "example.txt",
        "targetType": "user",
        "targetId": "user123",
        "sharedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

#### 5.6.2 分享文件

- **方法**：POST
- **路径**：`/api/storage/share`
- **描述**：分享文件
- **参数**：
  - `fileId` (必填)：文件 ID
  - `targets` (必填)：目标列表
- **请求示例**：

```json
{
  "fileId": "file-123456",
  "targets": [
    {
      "type": "user",
      "id": "user123"
    }
  ]
}
```

- **响应**：

```json
{
  "success": true,
  "data": {
    "sharingId": "share-123456",
    "fileId": "file-123456",
    "status": "shared"
  },
  "message": "File shared successfully",
  "code": 200
}
```

#### 5.6.3 获取收到的文件列表

- **方法**：GET
- **路径**：`/api/storage/received`
- **描述**：获取收到的文件列表
- **参数**：
  - `page` (可选)：页码，默认 1
  - `size` (可选)：每页大小，默认 10
- **响应**：

```json
{
  "success": true,
  "data": {
    "total": 3,
    "page": 1,
    "size": 10,
    "receivings": [
      {
        "sharingId": "share-654321",
        "fileId": "file-654321",
        "fileName": "document.pdf",
        "fromType": "user",
        "fromId": "user456",
        "receivedAt": 1234567890
      }
    ]
  },
  "message": "Success",
  "code": 200
}
```

## 6. API 版本控制

### 6.1 版本策略

Nexus API 采用语义化版本控制，版本号格式为 `major.minor.patch`：

- **Major**：不兼容的 API 变更
- **Minor**：向后兼容的功能添加
- **Patch**：向后兼容的 bug 修复

### 6.2 版本管理

当前 API 版本为 v1，所有 API 路径均以 `/api` 开头。未来版本可能会通过路径前缀（如 `/api/v2`）进行区分。

## 7. API 最佳实践

### 7.1 调用频率限制

为了保护系统稳定性，Nexus API 对调用频率进行限制：
- **普通 API**：60 次/分钟
- **资源密集型 API**：10 次/分钟

### 7.2 错误处理

调用 API 时应妥善处理错误：
- **检查 HTTP 状态码**：根据状态码判断操作是否成功
- **解析错误响应**：从响应体中获取详细错误信息
- **实现重试机制**：对于临时性错误，可实现指数退避重试

### 7.3 性能优化

- **批量操作**：使用批量 API 减少网络开销
- **缓存策略**：合理缓存 API 响应
- **异步操作**：对于耗时操作，使用异步 API
- **分页查询**：使用分页参数避免一次性获取大量数据

### 7.4 安全性

- **使用 HTTPS**：在生产环境中使用 HTTPS 保护数据传输
- **认证信息**：妥善保管认证信息，避免明文传输
- **参数验证**：在客户端验证输入参数，减少无效请求
- **权限检查**：确保只调用有权限的 API

## 8. 总结

Nexus API 提供了丰富的接口，支持个人中心、P2P 网络、监控和存储管理等功能。通过遵循本手册中的 API 规范和最佳实践，您可以充分利用 Nexus 的能力，构建基于 Nexus 的应用和集成。

相关应用推荐配合 ooder-skillsCenter 使用，以获得更好的整体体验。

**注意**：本手册适用于 Nexus 预览版程序，部分 API 可能在正式版本中有所变更。

---

**最后更新时间**：2026-02-02
**版本**：v1.0.0
**发布平台**：gitee 独家发布