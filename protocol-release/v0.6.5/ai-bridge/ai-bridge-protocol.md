# AI Bridge 协议分册

## 1. 协议概述

AI Bridge 协议是 Ooder 智能体系统中用于智能体、SKILL 和资源系统之间通信的核心协议。它定义了统一的消息格式、命令系统和安全机制，实现了不同组件之间的无缝交互。

### 1.1 设计原则

- **标准化**：定义统一的通信接口和数据格式
- **安全性**：提供完善的认证、授权和加密机制
- **高效性**：优化通信协议，减少延迟和带宽消耗
- **可扩展性**：支持自定义命令和参数扩展

## 2. 通信协议

### 2.1 传输层

| 协议 | 适用场景 | 特点 |
|------|----------|------|
| HTTP/HTTPS | 标准 API 调用 | 广泛支持、易于调试 |
| WebSocket/WSS | 实时通信 | 低延迟、全双工 |
| MQTT | IoT 设备通信 | 轻量级、低功耗 |

### 2.2 协议栈架构

```
┌───────────────────────
│ 应用层(业务逻辑)    │
├───────────────────────
│ AI Bridge 协议层   │
├───────────────────────
│ 传输层(HTTP/WS/MQTT) │
├───────────────────────
│ 安全层(TLS/SSL)     │
└───────────────────────
```

## 3. Skill-VFS 特殊协议规范

Skill-VFS 是一种特殊的 Skill 实现，提供基于文件的同步接口服务，作为 group 文件分发和共享的中心。

### 3.1 VFS 服务接口

#### 3.1.1 文件上传接口

| 方法 | URL | 请求参数 | 响应格式 |
|------|-----|----------|----------|
| POST | /vfs/upload | file、group_name、path | JSON {"success": true, "file_id": "..."} |

#### 3.1.2 文件下载接口

| 方法 | URL | 请求参数 | 响应格式 |
|------|-----|----------|----------|
| GET | /vfs/download | file_id、group_name | 文件流 |

#### 3.1.3 文件列表接口

| 方法 | URL | 请求参数 | 响应格式 |
|------|-----|----------|----------|
| GET | /vfs/list | group_name、path | JSON {"success": true, "files": [...]} |

#### 3.1.4 文件删除接口

| 方法 | URL | 请求参数 | 响应格式 |
|------|-----|----------|----------|
| DELETE | /vfs/delete | file_id、group_name | JSON {"success": true} |

### 3.2 存储切换机制

- **VFS 优先**：当 VFS 可用时，优先使用 VFS 作为核心存储
- **本地回退**：当无法找到 VFS 时，使用本地目录存储
- **自动同步**：当 VFS 从不可用变为可用时，自动进行文件同步

## 4. 消息格式

### 4.1 通用消息结构

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "skill.discover",
  "params": {
    "param1": "value1",
    "param2": "value2"
  },
  "metadata": {
    "sender_id": "agent-123",
    "trace_id": "trace-456",
    "priority": "high"
  }
}
```

### 4.2 请求消息

| 字段 | 类型 | 描述 | 必须 |
|------|------|------|------|
| version | String | 协议版本 | 是|
| id | String | 消息唯一标识 | 是|
| timestamp | Long | 消息时间戳 | 是|
| type | String | 消息类型：request | 是|
| command | String | 命令名称 | 是|
| params | Object | 命令参数 | 是|
| metadata | Object | 元数据 | 是|

### 4.3 响应消息

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000100,
  "type": "response",
  "status": "success",
  "result": {
    "key1": "value1",
    "key2": "value2"
  },
  "error": {
    "code": "1001",
    "message": "参数错误"
  },
  "metadata": {
    "sender_id": "skill-456",
    "trace_id": "trace-456"
  }
}
```

| 字段 | 类型 | 描述 | 必须 |
|------|------|------|------|
| version | String | 协议版本 | 是 |
| id | String | 消息唯一标识（与请求一致） | 是 |
| timestamp | Long | 消息时间戳 | 是 |
| type | String | 消息类型：response | 是 |
| status | String | 处理状态：success/failed | 是 |
| result | Object | 处理结果 | 是 |
| error | Object | 错误信息 | 是 |
| metadata | Object | 元数据 | 是 |

## 5. 命令系统

### 5.1 核心命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| skill.discover | 发现可用 SKILL | space_id, capability |
| skill.invoke | 调用 SKILL 能力 | skill_id, capability_id, params |
| agent.register | 注册智能体 | agent_id, type, endpoints |
| agent.unregister | 注销智能体 | agent_id |
| resource.list | 列出资源 | space_id, type |
| resource.get | 获取资源详情 | resource_id |
| llm.query | LLM 查询 | skill_id, query, parameters |
| llm.advanced-query | 高级 LLM 查询 | skill_id, query, parameters, options |
| llm.status | LLM 服务状态 | - |
| llm.models | 列出可用 LLM 模型 | - |

#### 5.1.1 skill.discover 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| space_id | String | 1-64 | 是 | 资源空间ID | "home_space_001" |
| capability | String | 1-128 | 是 | 能力名称 | "temperature_sensor" |

#### 5.1.2 skill.invoke 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "temperature_converter_001" |
| capability_id | String | 1-64 | 是 | 能力ID | "celsius_to_fahrenheit" |
| params | Object | - | 是 | 调用参数 | `{"celsius": 25}` |

#### 5.1.3 agent.register 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| agent_id | String | 1-64 | 是 | 智能体唯一标识 | "route_agent_001" |
| type | String | 1-32 | 是 | 智能体类型 | "route_agent" |
| endpoints | Array[String] | 1-10 | 是 | 智能体访问端点列表 | `["http://192.168.1.100:9010", "ws://192.168.1.100:9011"]` |

#### 5.1.4 agent.unregister 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| agent_id | String | 1-64 | 是| 智能体唯一标识 | "route_agent_001" |

#### 5.1.5 resource.list 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| space_id | String | 1-64 | 是 | 资源空间ID | "home_space_001" |
| type | String | 1-32 | 是 | 资源类型 | "device" |

#### 5.1.6 resource.get 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| resource_id | String | 1-64 | 是 | 资源唯一标识 | "device_001" |

### 5.2 场景管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| scene.declare | 声明场景 | scene_type, skill_id, skill_role, is_owner_declaration, cap_id, cert_sn |
| scene.declare.cancel | 取消场景声明 | scene_type, skill_id, cap_id |
| scene.query | 查询场景信息 | scene_type |

#### 5.2.1 scene.declare 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 是 | 场景类型 | "SMART_HOME" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "smart_home_controller_001" |
| skill_role | String | 1-32 | 是 | SKILL角色 | "agent_route" |
| is_owner_declaration | Boolean | - | 是 | 是否为所有者声明 | false |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |
| cert_sn | String | 16 | 是 | 证书序列号（仅当声明为RouteAgent时必填） | "8a1b2c3d4e5f6a7b" |

#### 5.2.2 scene.declare.cancel 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 是| 场景类型 | "SMART_HOME" |
| skill_id | String | 1-64 | 是| SKILL唯一标识 | "smart_home_controller_001" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.2.3 scene.query 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 是| 场景类型 | "SMART_HOME" |

### 5.3 Cap管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| cap.declare | 声明Cap | cap_id, cap_name, cap_desc, skill_id, cap_type, supported_scenes, max_members, data_storage_type, link_type, disk_path, group_file_path, links_file_path, sync_enabled, sync_interval |
| cap.update | 更新Cap | cap_id, cap_info |
| cap.query | 查询Cap | skill_id, cap_id |
| cap.remove | 移除Cap | cap_id |

#### 5.3.1 cap.declare 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |
| cap_name | String | 1-128 | 是 | Cap名称 | "设备控制能力" |
| cap_desc | String | 1-512 | 是 | Cap描述 | "用于控制智能家居设备的能力" |
| skill_id | String | 1-64 | 是| SKILL唯一标识 | "smart_home_controller_001" |
| cap_type | String | 1-32 | 是| Cap类型 | "device_control" |
| supported_scenes | Array[String] | 1-10 | 是 | 支持的场景类型 | `["SMART_HOME", "OFFICE_AUTOMATION"]` |
| max_members | Integer | 1-1000 | 是| 最大成员数 | 100 |
| data_storage_type | String | 1-32 | 是| 数据存储类型 | "json" |
| link_type | String | 1-32 | 是| 链接类型 | "bidirectional" |
| disk_path | String | 1-256 | 是| 磁盘路径 | "/var/cap_data/device_control" |
| group_file_path | String | 1-256 | 是| 群组文件路径 | "/var/cap_data/device_control/group.json" |
| links_file_path | String | 1-256 | 是| 链接文件路径 | "/var/cap_data/device_control/links.json" |
| sync_enabled | Boolean | - | 是| 是否启用同步 | true |
| sync_interval | Integer | 1-3600 | 是 | 同步间隔（秒） | 60 |

#### 5.3.2 cap.update 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |
| cap_info | Object | - | 是 | Cap更新信息 | `{"cap_name": "更新的设备控制能力", "sync_interval": 30}` |

#### 5.3.3 cap.query 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| skill_id | String | 1-64 | 是| SKILL唯一标识 | "smart_home_controller_001" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |

#### 5.3.4 cap.remove 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |

### 5.4 频道数据管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| group.member.add | 添加频道成员 | group_id, skill_id, role, cap_id |
| group.member.remove | 移除频道成员 | group_id, skill_id, cap_id |
| group.link.add | 添加链路关系 | group_id, source_skill_id, target_skill_id, link_type, cap_id |
| group.link.remove | 移除链路关系 | group_id, source_skill_id, target_skill_id, cap_id |
| group.data.set | 设置频道数据 | group_id, data_key, data_value, data_type, cap_id |
| group.data.get | 获取频道数据 | group_id, data_key, cap_id |

#### 5.4.1 group.member.add 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| skill_id | String | 1-64 | 是| SKILL唯一标识 | "temperature_sensor_001" |
| role | String | 1-32 | 是 | 成员角色 | "sensor" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.4.2 group.member.remove 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是| 群组ID | "smart_home_group_001" |
| skill_id | String | 1-64 | 是| SKILL唯一标识 | "temperature_sensor_001" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.4.3 group.link.add 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是| 群组ID | "smart_home_group_001" |
| source_skill_id | String | 1-64 | 是| 源SKILL唯一标识 | "temperature_sensor_001" |
| target_skill_id | String | 1-64 | 是| 目标SKILL唯一标识 | "air_conditioner_001" |
| link_type | String | 1-32 | 是| 链路类型 | "data_flow" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.4.4 group.link.remove 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是| 群组ID | "smart_home_group_001" |
| source_skill_id | String | 1-64 | 是| 源SKILL唯一标识 | "temperature_sensor_001" |
| target_skill_id | String | 1-64 | 是| 目标SKILL唯一标识 | "air_conditioner_001" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.4.5 group.data.set 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是| 群组ID | "smart_home_group_001" |
| data_key | String | 1-64 | 是| 数据键名 | "temperature" |
| data_value | Any | - | 是| 数值| 25 |
| data_type | String | 1-32 | 是| 数据类型 | "number" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

#### 5.4.6 group.data.get 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是| 群组ID | "smart_home_group_001" |
| data_key | String | 1-64 | 是| 数据键名 | "temperature" |
| cap_id | String | 1-64 | 是| Cap唯一标识 | "smart_home_channel_001" |

### 5.5 VFS同步命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| cap.vfs.sync | 同步Cap VFS数据 | cap_id, vfs_path |
| cap.vfs.sync.status | 查询同步状态| cap_id |
| cap.vfs.recover | 恢复Cap VFS数据 | cap_id |

#### 5.5.1 cap.vfs.sync 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |
| vfs_path | String | 1-256 | 是| VFS路径 | "/vfs/cap/device_control_cap_001/group.json" |

#### 5.5.2 cap.vfs.sync.status 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |

#### 5.5.3 cap.vfs.recover 命令参数约束

| 参数名| 数据类型 | 长度限制 | 必填| 描述 | 示例值|
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是| Cap唯一标识 | "device_control_cap_001" |

### 5.6 命令执行流程

```
┌───────────┐    ┌─────────────┐    ┌─────────────┐    ┌───────────┐
│ 发送请求  │────▶│ 命令解析     │────▶│ 权限验证     │────▶│ 执行命令   │
└───────────┘    └─────────────┘    └─────────────┘    └───────────┘
        │                                                              │
        │                                                              │
        │                                                              │
        └───────────────────────────────────────────────────────────────┘
                                  返回结果
```

## 6. 安全机制

### 6.1 认证机制

- **证书认证**：声明为RouteAgent或声明场景的SKILL必须使用唯一证书，证书SN为6位十六进制字符串，通过以下三种方式之一签发：  - 运营商授权签发  - 应用商店授权签发
  - 企业手工授权签发
- **JWT 认证**：使用JSON Web Token 进行身份验证
- **API Key**：使用API 密钥进行简单认证
- **OAuth 2.0**：支持第三方认证

### 6.2 授权机制

- **基于角色的访问控制(RBAC)**：根据用户角色分配权限
- **基于资源的访问控制(RBAC)**：根据资源类型和属性分配权限
- **ABAC**：基于属性的访问控制，支持更灵活的权限策略
- **RouteAgent 特殊授权**：  - 声明为RouteAgent的SKILL必须经过运营商、应用商店或企业手工授权
  - 授权的RouteAgent可被killflow发现和调用  - 未授权的SKILL无法声明为RouteAgent
- **场景声明授权**：  - 声明场景的SKILL必须使用授权证书
  - 证书SN必须为6位十六进制字符串
  - 未授权SKILL无法声明场景

### 6.3 加密机制

- **传输加密**：使用TLS/SSL 加密传输数据
- **数据加密**：对敏感数据进行端到端加密
- **密钥管理**：安全的密钥生成、分发和轮换机制
- **证书管理**：  - 证书有效期管理  - 证书吊销机制
  - 证书更新流程

### 6.4 安全管控流程

1. **SKILL证书申请**：   - SKILL开发者向运营商、应用商店或企业申请证书
   - 提供SKILL基本信息和用途   - 审核通过后获取唯一证书（SN为6位十六进制字符串）
2. **RouteAgent声明**：   - SKILL使用授权证书声明为RouteAgent
   - AI Bridge验证证书有效性和授权范围
   - 验证通过后，RouteAgent可被killflow发现和调用
3. **场景声明**：   - RouteAgent使用授权证书声明场景
   - AI Bridge验证证书有效性和RouteAgent权限
   - 验证通过后创建场景并允许其他SKILL加入

## 7. 数据模型

### 7.1 消息数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值|
|------|------|------|------|-----------|--------|
| Message | id | String | 消息唯一标识 | UUID v4 | "123e4567-e89b-12d3-a456-426614174000" |
| Message | version | String | 协议版本 | 主版本号.次版本号 | "0.6.3" |
| Message | timestamp | Long | 消息时间戳 | Unix时间戳（毫秒） | 1737000000000 |
| Message | type | String | 消息类型 | "request" 和"response" | "request" |
| Message | command | String | 命令名称 | 点分隔的命令路径 | "skill.discover" |
| Message | params | Object | 命令参数 | JSON对象 | `{"space_id": "home_space_001"}` |
| Message | metadata | Object | 元数据 | JSON对象 | `{"sender_id": "agent_123"}` |

### 7.2 错误数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值|
|------|------|------|------|-----------|--------|
| Error | code | String | 错误码 | 4位数 | "1001" |
| Error | message | String | 错误消息 | 简洁的错误描述 | "参数错误" |
| Error | details | Object | 错误详情 | 详细的错误信息 | `{"param_name": "space_id", "reason": "格式不正确"}` |

### 7.3 元数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值|
|------|------|------|------|-----------|--------|
| Metadata | sender_id | String | 发送者标识| 1-64位字符串 | "agent_123" |
| Metadata | trace_id | String | 跟踪标识 | UUID v4 | "456e7890-e12b-34d5-a678-901234567890" |
| Metadata | priority | String | 消息优先级 | "low"、"medium"、"high" | "high" |
| Metadata | timeout | Long | 超时时间 | 毫秒 | 30000 |

### 7.4 SKILL数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值|
|------|------|------|------|-----------|--------|
| SKILL | skill_id | String | SKILL唯一标识 | 1-64位字符串 | "smart_home_controller_001" |
| SKILL | name | String | SKILL名称 | 1-128位字符串 | "智能家居控制器" |
| SKILL | description | String | SKILL描述 | 1-512位字符串 | "用于控制智能家居设备的SKILL" |
| SKILL | version | String | SKILL版本 | 主版本号.次版本号.修订版本号 | "1.0.0" |
| SKILL | type | String | SKILL类型 | "system" 和"custom" | "custom" |
| SKILL | endpoints | Array[String] | 访问端点列表 | 1-10个URL | `["http://192.168.1.100:9010"]` |
| SKILL | capabilities | Array[String] | 能力列表 | 1-100个能力ID | `["temperature_sensor", "device_control"]` |

### 7.5 Cap数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值|
|------|------|------|------|-----------|--------|
| Cap | cap_id | String | Cap唯一标识 | 1-64位字符串 | "device_control_cap_001" |
| Cap | name | String | Cap名称 | 1-128位字符串 | "设备控制能力" |
| Cap | description | String | Cap描述 | 1-512位字符串 | "用于控制智能家居设备的能力" |
| Cap | skill_id | String | 所属SKILL ID | 1-64位字符串 | "smart_home_controller_001" |
| Cap | type | String | Cap类型 | 1-32位字符串 | "device_control" |
| Cap | status | String | 状态| "active" 和"inactive" | "active" |
| Cap | supported_scenes | Array[String] | 支持的场景类型| 1-10个场景类型| `["SMART_HOME", "OFFICE_AUTOMATION"]` |

## 8. 扩展机制

### 8.1 自定义命令

```json
{
  "command": "custom.command",
  "params": {
    "custom_param1": "value1",
    "custom_param2": "value2"
  },
  "extension": {
    "vendor": "company-x",
    "schema_version": "1.0"
  }
}
```

### 8.2 扩展点

| 扩展点| 描述 | 配置方式 |
|--------|------|----------|
| 命令解析点| 自定义命令解析逻辑 | 插件配置 |
| 权限验证点| 自定义权限验证逻辑 | 插件配置 |
| 消息处理点| 自定义消息处理逻辑 | 插件配置 |
| 错误处理点| 自定义错误处理逻辑 | 插件配置 |

## 9. 性能优化

### 9.1 批量操作

支持批量命令执行，减少网络往返次数：

```json
{
  "command": "batch.execute",
  "params": {
    "commands": [
      {"command": "skill.invoke", "params": {"skill_id": "skill1", "capability_id": "cap1"}},
      {"command": "skill.invoke", "params": {"skill_id": "skill2", "capability_id": "cap2"}}
    ]
  }
}
```

### 9.2 缓存策略

| 缓存类型 | 适用场景 | 过期策略 |
|----------|----------|----------|
| SKILL 元数据| 频繁的SKILL 发现请求 | TTL 30 分钟|
| 权限信息 | 频繁的权限验证| TTL 10 分钟 |
| 资源信息 | 频繁的资源查询| TTL 1 分钟 |

## 10. Cap频道管理

### 10.1 Cap声明流程

1. SKILL初始化时，声明其支持的Cap列表
2. SKILL向AI Bridge发送cap.declare命令
3. AI Bridge验证Cap信息并注册
4. AI Bridge返回注册结果

### 10.2 Cap与Scene关联

SKILL声明Scene时，需要指定一个Cap作为频道

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "scene.declare",
  "params": {
    "scene_type": "SMART_HOME",
    "skill_id": "skill_001",
    "skill_role": "agent_route",
    "is_owner_declaration": false,
    "cap_id": "skill_001_device_control"
  },
  "metadata": {
    "sender_id": "skill-001",
    "trace_id": "trace-456"
  }
}
```

## 11. VFS同步协议

### 11.1 同步机制

所有Cap相关数据通过VFS实现云端同步，包括：

| 数据类型 | 本地存储 | 云端存储 | 同步方式 |
|----------|----------|----------|----------|
| Cap配置 | cap.json | /vfs/cap/{cap_id}/cap.json | 实时同步 |
| disk数据 | Cap专属VFS目录 | /vfs/cap/{cap_id}/disk/ | 实时/定期同步 |
| group数据 | group.json | /vfs/cap/{cap_id}/group.json | 实时同步 |
| links数据 | links.json | /vfs/cap/{cap_id}/links.json | 实时同步 |

### 11.2 同步命令格式

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "cap.vfs.sync",
  "params": {
    "cap_id": "skill_001_device_control",
    "vfs_path": "/vfs/cap/skill_001_device_control/group.json",
    "sync_type": "REAL_TIME",
    "content": "{\"group_id\": \"group_smart_home_device\", ...}"
  },
  "metadata": {
    "sender_id": "skill-001",
    "trace_id": "trace-456"
  }
}
```

## 12. 版本管理

### 12.1 版本升级策略

- 主版本号：重大功能变更，可能不兼容旧版本
- 次版本号：新增功能，兼容旧版本
- 修订版本号：bug修复，兼容旧版本

### 12.2 向后兼容性

- 支持版本协商机制
- 旧版本消息自动转换
- 废弃功能标记和警告

## 13. 测试规范

### 13.1 协议测试方法

- 单元测试：测试协议各组件的基本功能
- 集成测试：测试组件间的交互
- 性能测试：测试协议的性能和吞吐量
- 安全测试：测试协议的安全性

### 13.2 测试用例

```json
{
  "test_case_id": "tc_001",
  "description": "测试cap.declare命令",
  "request": {
    "version": "0.6.3",
    "command": "cap.declare",
    "params": {
      "cap_id": "test_cap",
      "cap_name": "Test Cap",
      "skill_id": "test_skill"
    }
  },
  "expected_response": {
    "status": "success"
  }
}
```

## 14. 部署指南

### 14.1 协议部署配置

- 端口配置：默认9010-9020
- 安全配置：TLS/SSL证书配置
- 日志配置：日志级别和格式

### 14.2 最佳实践

- 建议使用WebSocket协议进行实时通信
- 定期备份Cap数据
- 启用VFS同步功能确保数据一致

## 15. 错误处理

### 15.1 错误码体系

| 错误码范围 | 错误类型 |
|------------|----------|
| 1000-1999 | 参数错误 |
| 2000-2999 | 认证授权错误 |
| 3000-3999 | 资源错误 |
| 4000-4999 | 服务错误 |
| 5000-5999 | 系统错误 |

### 15.2 重试机制

| 错误类型 | 重试策略 |
|----------|----------|
| 网络错误 | 指数退避重试 |
| 服务繁忙 | 延迟重试 |
| 参数错误 | 不重试 |
| 权限错误 | 不重试 |

## 16. Skill-VFS 特殊协议

### 16.1 概述

Skill-VFS (Skill Virtual File System) 是 Ooder 智能体系统提供的基于文件的同步接口服务，作为 group 文件分发和共享的中心，支持多种文件同步接口方式，为各类 Skill 提供统一的存储方式接口。

### 16.2 设计原则

- **统一读写存储**：使用统一的读写存储模式，确保数据一致性
- **自动切换**：Skill 在无法访问 VFS 时使用本地目录存储，恢复后自动切换到 VFS 作为主要存储
- **文件结构支持**：Skill 可以创建、读取和写入文件结构，支持 VFS 存储接口
- **高效同步**：提供高效的同步接口支持，确保数据有效同步

### 16.3 通信接口

#### 16.3.1 VFS 健康检查

| 接口名称 | VFS 健康检查 |
|----------|------------|
| URL | `/vfs/health` |
| 方法 | GET |
| 认证 | 是 |
| 权限 | 执行权限 |

**响应格式**

```json
{
  "status": "healthy",
  "version": "0.6.3",
  "timestamp": 1737000000000,
  "details": {
    "storage_used": "2.5GB",
    "storage_total": "10GB",
    "connections": 5
  }
}
```

#### 16.3.2 文件同步到 VFS

| 接口名称 | 文件同步到 VFS |
|----------|------------|
| URL | `/vfs/sync` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求格式**

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "vfs.sync.to",
  "params": {
    "group_name": "skill-a-group",
    "files": [
      {
        "path": "test-data-1.json",
        "content": "{\"name\":\"Skill A\",\"version\":\"1.0.0\"}"
      },
      {
        "path": "test-data-2.txt",
        "content": "Sample text file"
      }
    ]
  },
  "metadata": {
    "sender_id": "skill-a-001",
    "trace_id": "trace-456"
  }
}
```

#### 16.3.3 从 VFS 同步文件

| 接口名称 | 从 VFS 同步文件 |
|----------|------------|
| URL | `/vfs/sync` |
| 方法 | POST |
| 认证 | 是 |
| 权限 | 执行权限 |

**请求格式**

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "vfs.sync.from",
  "params": {
    "group_name": "skill-a-group",
    "files": [
      "test-data-1.json",
      "test-data-2.txt"
    ]
  },
  "metadata": {
    "sender_id": "skill-a-001",
    "trace_id": "trace-456"
  }
}
```

### 16.4 消息格式

#### 16.4.1 VFS 同步消息

```json
{
  "version": "0.6.3",
  "id": "uuid-1234-5678-90ab-cdef",
  "timestamp": 1737000000000,
  "type": "request",
  "command": "vfs.sync",
  "params": {
    "action": "sync-all",
    "group_name": "skill-a-group",
    "sync_direction": "both"
  },
  "metadata": {
    "sender_id": "route-agent-001",
    "trace_id": "trace-456"
  }
}
```

### 16.5 同步策略

```
          
  Route Agent   ?  Skill-VFS     ?  Storage       
          
                                                      
                                                      
                                                      
                                     
                         Sync Engine                 
                                     
                                                      
                                                      
                                                      
          
  Skill A              Skill B              Skill C       
          
```

### 16.6 错误码

| 错误码 | 描述 | HTTP状态码 |
|--------|------|------------|
| 4001 | VFS 不可用 | 503 |
| 4002 | 同步失败 | 400 |
| 4003 | 文件不存在 | 404 |
| 4004 | 权限不足 | 403 |
| 4005 | 存储空间不足 | 507 |

## 17. LLM 协议分册

### 17.1 分册概述

LLM 协议分册是 Ooder AI Bridge 协议 v0.6.3 的核心组成部分，专注于大语言模型（LLM）相关的功能和交互规范，旨在实现 ooderAgent 与 LLM 之间的高效交互，以及 LLM 能力的标准化调用。本手册整合了南向协议中的 LLM 相关内容，提供完整的 LLM 集成解决方案。

#### 17.1.1 设计目标

- **统一接口**：提供标准化的 LLM 查询接口
- **性能优化**：优化 LLM 调用性能，减少 Token 消耗
- **可靠性**：实现错误处理和重试机制，确保 LLM 请求的可靠性
- **灵活性**：支持多种 LLM 模型的集成
- **可监控**：提供 LLM 服务状态监控和管理功能

#### 17.1.2 适用范围

- **Skills 与 LLM 交互**：通过 Skills 调用 LLM 能力
- **mcpAgent 与 LLM 交互**：通过 mcpAgent 统一管理 LLM 请求
- **routeAgent 与 LLM 交互**：通过 routeAgent 分发 LLM 相关命令
- **endAgent 与 LLM 交互**：通过 endAgent 执行 LLM 相关任务

### 17.2 核心功能

#### 17.2.1 LLM 查询接口

##### 17.2.1.1 基本查询

- **接口路径**：`/api/mcp-agent/llm/query`
- **请求方法**：POST
- **请求格式**：南向协议 v0.6.3 格式
- **请求参数**：
  ```json
  {
    "skill_id": "string",
    "query": "string",
    "parameters": "object"
  }
  ```
- **响应格式**：
  ```json
  {
    "result": "string",
    "tokens_used": "number"
  }
  ```

##### 17.2.1.2 LLM 查询交互流程图

```
+-----------------+     +-----------------+     +-----------------+     +-----------------+
|                 |     |                 |     |                 |     |                 |
|  Skills/用户     |---->|  mcpAgent       |---->|  routeAgent     |---->|  endAgent       |
|                 |     | (AIBridge)      |     | (Scene Core)    |     | (Executor)      |
|                 |     |                 |     |                 |     |                 |
+-----------------+     +-----------------+     +-----------------+     +-----------------
          ^                       |                       |                       |
          |                       |                       |                       |
          |                       v                       v                       v
          |                 +-----------------+     +-----------------+     +-----------------
          |                 |                 |     |                 |     |                 |
          +-----------------|  LLM 服务        |<----|  命令执行        |<----|  执行 LLM 查询   |
                            |                 |     |                 |     |                 |
                            +-----------------+     +-----------------+     +-----------------
```

##### 17.2.1.3 高级查询

- **接口路径**：`/api/mcp-agent/llm/advanced-query`
- **请求方法**：POST
- **请求格式**：南向协议 v0.6.3 格式
- **请求参数**：
  ```json
  {
    "skill_id": "string",
    "query": "string",
    "parameters": "object",
    "options": {
      "model": "string",
      "temperature": "number",
      "max_tokens": "number"
    }
  }
  ```
- **响应格式**：
  ```json
  {
    "result": "string",
    "tokens_used": "number",
    "model": "string",
    "execution_time": "number"
  }
  ```

#### 17.2.2 LLM 服务管理

##### 17.2.2.1 状态监控

- **接口路径**：`/api/mcp-agent/llm/status`
- **请求方法**：GET
- **请求格式**：南向协议 v0.6.3 格式
- **响应格式**：
  ```json
  {
    "status": "string",
    "uptime": "number",
    "active_requests": "number"
  }
  ```

##### 17.2.2.2 模型管理

- **接口路径**：`/api/mcp-agent/llm/models`
- **请求方法**：GET
- **请求格式**：南向协议 v0.6.3 格式
- **响应格式**：
  ```json
  {
    "models": [
      {
        "id": "string",
        "name": "string",
        "version": "string",
        "status": "string"
      }
    ]
  }
  ```

### 17.3 南向协议集成

#### 17.3.1 协议格式

LLM 分册使用与南向协议一致的基本格式：

```json
{
  "protocol_version": "0.6.3",
  "command_id": "uuid",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "string",
    "id": "string"
  },
  "destination": {
    "component": "string",
    "id": "string"
  },
  "operation": "string",
  "payload": {
    // 操作参数
  },
  "metadata": {
    "priority": "high|medium|low",
    "timeout": "number",
    "retry_count": "number"
  }
}
```

#### 17.3.2 操作类型

| 操作类型 | 功能描述 | 目标组件 | 参数 |
|----------|----------|----------|------|
| execute-llm-query | 执行 LLM 查询 | mcpAgent | `{"skill_id": "string", "query": "string", "parameters": "object"}` |
| get-llm-status | 获取 LLM 服务状态 | mcpAgent | N/A |
| list-llm-models | 列出可用 LLM 模型 | mcpAgent | N/A |
| execute-llm-advanced-query | 执行高级 LLM 查询 | mcpAgent | `{"skill_id": "string", "query": "string", "parameters": "object", "options": "object"}` |

#### 17.3.3 错误处理

| 错误码 | 错误描述 | 处理策略 |
|--------|----------|----------|
| 1000 | 参数错误 | 直接返回错误 |
| 1001 | 认证失败 | 直接返回错误 |
| 1002 | 权限不足 | 直接返回错误 |
| 1003 | 资源不存在 | 直接返回错误 |
| 1004 | 请求超时 | 指数退避重试 |
| 1005 | 网络错误 | 指数退避重试 |
| 1006 | 服务繁忙 | 指数退避重试 |
| 1007 | 内部错误 | 指数退避重试 |
| 1008 | 数据格式错误 | 直接返回错误 |
| 1009 | LLM 服务不可用 | 指数退避重试 |

### 17.4 协议示例

#### 17.4.1 执行 LLM 查询

```json
{
  "protocol_version": "0.6.3",
  "command_id": "cmd-123",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "Skills",
    "id": "a2ui"
  },
  "destination": {
    "component": "mcpAgent",
    "id": "mcp-agent-1"
  },
  "operation": "execute-llm-query",
  "payload": {
    "skill_id": "a2ui",
    "query": "如何优化表单组件",
    "parameters": {
      "context": "A2UI 四分离设计原则"
    }
  },
  "metadata": {
    "priority": "medium",
    "timeout": 60,
    "retry_count": 3
  }
}
```

#### 17.4.2 获取 LLM 状态

```json
{
  "protocol_version": "0.6.3",
  "command_id": "cmd-456",
  "timestamp": "2026-01-23T12:00:00Z",
  "source": {
    "component": "Skills",
    "id": "a2ui"
  },
  "destination": {
    "component": "mcpAgent",
    "id": "mcp-agent-1"
  },
  "operation": "get-llm-status",
  "payload": {},
  "metadata": {
    "priority": "low",
    "timeout": 30,
    "retry_count": 1
  }
}
```

### 17.5 性能优化

#### 17.5.1 查询优化

- **批处理**：批量处理相似的 LLM 查询
- **并行查询**：并行执行多个 LLM 查询
- **查询缓存**：缓存频繁使用的 LLM 响应
- **查询压缩**：压缩查询内容，减少网络传输

#### 17.5.2 Token 优化

- **上下文管理**：优化上下文窗口使用，减少重复 Token
- **提示词优化**：优化提示词，减少不必要的 Token
- **响应截断**：根据需要截断 LLM 响应，减少 Token 消耗
- **流式响应**：使用流式响应，减少等待时间

#### 17.5.3 资源管理

- **连接池**：使用连接池管理 LLM 服务连接
- **超时管理**：合理设置 LLM 查询超时时间
- **负载均衡**：在多个 LLM 服务之间实现负载均衡
- **资源限制**：设置合理的资源限制，防止资源耗尽

### 17.6 调度与管理

#### 17.6.1 MCP 架构下的调度原理

##### 17.6.1.1 核心组件

| 组件 | 角色 | 功能描述 |
|------|------|----------|
| mcpAgent | AIBridge | 作为 aibridge 统一处理 LLM 需求，汇总 routeAgent 的请求，统一与 LLM 完成交互 |
| routeAgent | Scene Core | 作为场景核心，通过内部建组的方式与 endAgent 完成交互，管理命令的广播和执行结果的收集 |
| endAgent | Executor | 执行具体的业务逻辑，响应 routeAgent 广播的命令 |
| Skills | Bridge | 作为技能桥接，连接用户需求和 ooderAgent 功能 |

##### 17.6.1.2 MCP 架构交互流程图

```
+-----------------+     +-----------------+     +-----------------+     +-----------------+
|                 |     |                 |     |                 |     |                 |
|  用户/IDE        |---->|  Skills         |---->|  mcpAgent       |---->|  LLM 服务        |
|                 |     | (Bridge)        |     | (AIBridge)      |     |                 |
|                 |     |                 |     |                 |     |                 |
+-----------------+     +-----------------+     +-----------------+     +-----------------+
          ^                       |                       |                       |
          |                       |                       |                       |
          |                       v                       v                       |
          |                 +-----------------+     +-----------------+           |
          |                 |                 |     |                 |           |
          +-----------------|  endAgent       |<----|  routeAgent     |<----------+
                            | (Executor)      |     | (Scene Core)    |
                            |                 |     |                 |
                            +-----------------+     +-----------------+
```

##### 17.6.1.3 调度流程

1. **请求发起**：用户通过 Skills 发起 LLM 请求
2. **请求处理**：mcpAgent 接收并处理 LLM 请求，汇总需求
3. **命令分发**：routeAgent 根据场景将请求转换为命令，广播给 endAgent
4. **命令执行**：endAgent 执行命令并返回结果
5. **结果汇总**：routeAgent 汇总执行结果
6. **结果返回**：mcpAgent 将结果返回给 Skills 和用户

#### 17.6.2 标准 Skills 调度原理

##### 17.6.2.1 技能注册与发现

1. **技能注册**：当技能被添加到 `skills` 目录后，trae-solo IDE 会自动发现并加载这些技能
2. **技能发现**：
   - **IDE 发现**：trae-solo IDE 会扫描 `skills` 目录，读取每个技能的 `skill.yaml` 文件
   - **mcpAgent 发现**：mcpAgent 会通过 routeAgent 管理已注册的 endAgent

##### 17.6.2.2 技能调用流程

```
+-----------------+     +-----------------+     +-----------------+     +-----------------+
|                 |     |                 |     |                 |     |                 |
|  用户/IDE        |---->|  mcpAgent       |---->|  routeAgent     |---->|  目标技能       |
|                 |     | (AIBridge)      |     | (Scene Core)    |     | (endAgent)      |
|                 |     |                 |     |                 |     |                 |
+-----------------+     +-----------------+     +-----------------+     +-----------------+
```

##### 17.6.2.3 调用步骤

1. **查询 mcpAgent 状态**：确保 mcpAgent 服务正常运行
2. **根据技能查找 mcpAgent id**：确定要使用的技能对应的 agent
3. **向指定 endAgent 发送执行命令及参数**：通过 mcpAgent 发送命令
4. **等待返回或异步查询结果**：支持同步等待和异步查询

#### 17.6.3 分级加载机制

##### 17.6.3.1 加载级别

采用 3 级加载机制，实现按需加载，节省 Token：

1. **1 级**：元数据（skill.yaml），轻量级加载
2. **2 级**：说明文档（SKILL.md），按需加载
3. **3 级及以上**：脚本和资源文件，按需加载执行

##### 17.6.3.2 加载流程

1. **初始化加载**：只加载 1 级元数据，快速完成技能初始化
2. **按需加载**：根据用户需求，逐步加载 2 级和 3 级内容
3. **缓存机制**：加载的内容会被缓存，减少重复加载

#### 17.6.4 命令执行流程

##### 17.6.4.1 命令构建

```bash
# 通过 mcp-agent.sh 执行技能功能
bash scripts/mcp-agent.sh <action> <skill_id> [<parameters>]

# 直接执行技能脚本
bash <skill_directory>/scripts/<function_name>.sh [<parameters>]
```

##### 17.6.4.2 执行步骤

1. **命令构建**：根据用户需求构建命令
2. **查询在线成员**：获取组内在线的 endAgent
3. **广播命令**：向在线成员广播命令
4. **处理响应**：处理 endAgent 的响应（接受或拒绝）
5. **执行命令**：endAgent 执行命令
6. **返回结果**：endAgent 异步返回执行结果
7. **汇总结果**：routeAgent 汇总执行结果
8. **返回结果**：将结果返回给用户

### 17.7 部署与集成

#### 17.7.1 部署架构

##### 17.7.1.1 本地部署

```
+---------------------+
|     用户/IDE        |
+---------------------+
          |
+---------------------+
|     Skills          |
+---------------------+
          |
+---------------------+
|  ooderAgent 服务    |
| +-----------------+ |
| |   mcpAgent      | |
| |   routeAgent    | |
| |   endAgent      | |
| +-----------------+ |
+---------------------+
          |
+---------------------+
|     LLM 服务        |
+---------------------+
```

##### 17.7.1.2 分布式部署

```
+---------------------+
|     用户/IDE        |
+---------------------+
          |
+---------------------+
|     Skills          |
+---------------------+
          |
+---------------------+
|  Load Balancer      |
+---------------------+
          |
+---------------------+
|  ooderAgent Cluster |
| +-----------------+ |
| |   mcpAgent      | |
| |   routeAgent    | |
| |   endAgent      | |
| +-----------------+ |
+---------------------+
          |
+---------------------+
|  LLM 服务集群       |
+---------------------+
```

#### 17.7.2 集成步骤

1. **配置 LLM 服务**：设置 LLM 服务的连接信息和参数
2. **部署 ooderAgent**：部署 ooderAgent 服务，包括 mcpAgent、routeAgent 和 endAgent
3. **注册技能**：将技能注册到 ooderAgent 中
4. **配置路由规则**：配置 LLM 相关命令的路由规则
5. **测试集成**：测试 LLM 查询和服务管理功能

### 17.8 安全机制

#### 17.8.1 认证与授权

- **认证机制**：基于 Token 的认证，确保只有授权的组件能访问 LLM 服务
- **授权机制**：基于角色的访问控制，限制不同组件的操作权限
- **密钥管理**：安全存储和管理认证密钥

#### 17.8.2 数据加密

- **传输加密**：使用 TLS/SSL 加密传输数据
- **存储加密**：加密存储敏感的 LLM 查询和响应
- **数据脱敏**：对敏感数据进行脱敏处理

#### 17.8.3 安全审计

- **日志记录**：记录所有 LLM 相关操作的日志
- **审计跟踪**：跟踪和分析操作记录，检测异常行为
- **合规性检查**：确保 LLM 使用符合相关法规和政策

### 17.9 故障处理

#### 17.9.1 故障检测

- **健康检查**：定期检查 LLM 服务的健康状态
- **异常检测**：检测异常的 LLM 查询和响应
- **性能监控**：监控 LLM 服务的性能指标

#### 17.9.2 故障恢复

- **自动重试**：对临时性故障进行自动重试
- **故障转移**：当主 LLM 服务不可用时，切换到备用服务
- **服务降级**：在 LLM 服务负载过高时，实施服务降级策略

#### 17.9.3 容灾备份

- **数据备份**：定期备份 LLM 相关配置和数据
- **配置版本控制**：对 LLM 服务配置进行版本控制
- **灾难恢复计划**：制定详细的灾难恢复计划

### 17.10 监控与维护

#### 17.10.1 监控指标

- **查询成功率**：LLM 查询的成功比例
- **响应时间**：LLM 查询的平均响应时间
- **Token 消耗**：LLM 查询的平均 Token 消耗
- **服务可用性**：LLM 服务的可用时间比例
- **错误率**：LLM 查询的错误比例

#### 17.10.2 监控工具

- **Prometheus**：监控 LLM 服务的性能指标
- **Grafana**：可视化 LLM 服务的监控数据
- **ELK Stack**：收集和分析 LLM 服务的日志

#### 17.10.3 维护计划

- **定期检查**：定期检查 LLM 服务的状态和配置
- **版本更新**：及时更新 LLM 服务和 ooderAgent 的版本
- **性能调优**：根据监控数据进行性能调优
- **安全补丁**：及时应用安全补丁

### 17.11 术语定义

| 术语 | 解释 |
|------|------|
| LLM | Large Language Model，大语言模型 |
| mcpAgent | 模型控制平面代理，统一处理 LLM 需求 |
| routeAgent | 路由代理，负责命令的路由和分发 |
| endAgent | 执行代理，负责执行具体的业务逻辑 |
| Skills | 技能，提供特定功能的服务单元 |
| Token | LLM 处理的基本单位，用于计算费用和限制 |
| VFS | Virtual File System，虚拟文件系统 |
| A2UI | AI to UI，AI 生成 UI 代码的技术 |
| MCP | Model Control Plane，模型控制平面 |
| Capability | 能力，Skill 提供的具体功能 |
| Scene | 场景，智能体协同工作的上下文环境 |
| Cap | 通道，智能体间通信的逻辑通道 |
