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
┌────────────────────┐
│ 应用层 (业务逻辑)    │
├────────────────────┤
│ AI Bridge 协议层    │
├────────────────────┤
│ 传输层 (HTTP/WS/MQTT) │
├────────────────────┤
│ 安全层 (TLS/SSL)     │
└────────────────────┘
```

## 3. 消息格式

### 3.1 通用消息结构

```json
{
  "version": "0.6.0",
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

### 3.2 请求消息

| 字段 | 类型 | 描述 | 必须 |
|------|------|------|------|
| version | String | 协议版本 | 是 |
| id | String | 消息唯一标识 | 是 |
| timestamp | Long | 消息时间戳 | 是 |
| type | String | 消息类型：request | 是 |
| command | String | 命令名称 | 是 |
| params | Object | 命令参数 | 否 |
| metadata | Object | 元数据 | 否 |

### 3.3 响应消息

```json
{
  "version": "0.6.0",
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
| result | Object | 处理结果 | 否 |
| error | Object | 错误信息 | 否 |
| metadata | Object | 元数据 | 否 |

## 4. 命令系统

### 4.1 核心命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| skill.discover | 发现可用 SKILL | space_id, capability |
| skill.invoke | 调用 SKILL 能力 | skill_id, capability_id, params |
| agent.register | 注册智能体 | agent_id, type, endpoints |
| agent.unregister | 注销智能体 | agent_id |
| resource.list | 列出资源 | space_id, type |
| resource.get | 获取资源详情 | resource_id |

#### 4.1.1 skill.discover 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| space_id | String | 1-64 | 否 | 资源空间ID | "home_space_001" |
| capability | String | 1-128 | 否 | 能力名称 | "temperature_sensor" |

#### 4.1.2 skill.invoke 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "temperature_converter_001" |
| capability_id | String | 1-64 | 是 | 能力ID | "celsius_to_fahrenheit" |
| params | Object | - | 否 | 调用参数 | `{"celsius": 25}` |

#### 4.1.3 agent.register 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| agent_id | String | 1-64 | 是 | 智能体唯一标识 | "route_agent_001" |
| type | String | 1-32 | 是 | 智能体类型 | "route_agent" |
| endpoints | Array[String] | 1-10 | 是 | 智能体访问端点列表 | `["http://192.168.1.100:9010", "ws://192.168.1.100:9011"]` |

#### 4.1.4 agent.unregister 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| agent_id | String | 1-64 | 是 | 智能体唯一标识 | "route_agent_001" |

#### 4.1.5 resource.list 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| space_id | String | 1-64 | 否 | 资源空间ID | "home_space_001" |
| type | String | 1-32 | 否 | 资源类型 | "device" |

#### 4.1.6 resource.get 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| resource_id | String | 1-64 | 是 | 资源唯一标识 | "device_001" |

### 4.2 场景管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| scene.declare | 声明场景 | scene_type, skill_id, skill_role, is_owner_declaration, cap_id, cert_sn |
| scene.declare.cancel | 取消场景声明 | scene_type, skill_id, cap_id |
| scene.query | 查询场景信息 | scene_type |

#### 4.2.1 scene.declare 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 是 | 场景类型 | "SMART_HOME" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "smart_home_controller_001" |
| skill_role | String | 1-32 | 是 | SKILL角色 | "agent_route" |
| is_owner_declaration | Boolean | - | 是 | 是否为所有者声明 | false |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |
| cert_sn | String | 16 | 是 | 证书序列号（仅当声明为RouteAgent时必填） | "8a1b2c3d4e5f6a7b" |

#### 4.2.2 scene.declare.cancel 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 是 | 场景类型 | "SMART_HOME" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "smart_home_controller_001" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.2.3 scene.query 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| scene_type | String | 1-32 | 否 | 场景类型 | "SMART_HOME" |

### 4.3 Cap管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| cap.declare | 声明Cap | cap_id, cap_name, cap_desc, skill_id, cap_type, supported_scenes, max_members, data_storage_type, link_type, disk_path, group_file_path, links_file_path, sync_enabled, sync_interval |
| cap.update | 更新Cap | cap_id, cap_info |
| cap.query | 查询Cap | skill_id, cap_id |
| cap.remove | 移除Cap | cap_id |

#### 4.3.1 cap.declare 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |
| cap_name | String | 1-128 | 是 | Cap名称 | "设备控制能力" |
| cap_desc | String | 1-512 | 否 | Cap描述 | "用于控制智能家居设备的能力" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "smart_home_controller_001" |
| cap_type | String | 1-32 | 是 | Cap类型 | "device_control" |
| supported_scenes | Array[String] | 1-10 | 否 | 支持的场景类型 | `["SMART_HOME", "OFFICE_AUTOMATION"]` |
| max_members | Integer | 1-1000 | 是 | 最大成员数 | 100 |
| data_storage_type | String | 1-32 | 是 | 数据存储类型 | "json" |
| link_type | String | 1-32 | 是 | 链接类型 | "bidirectional" |
| disk_path | String | 1-256 | 是 | 磁盘路径 | "/var/cap_data/device_control" |
| group_file_path | String | 1-256 | 是 | 群组文件路径 | "/var/cap_data/device_control/group.json" |
| links_file_path | String | 1-256 | 是 | 链接文件路径 | "/var/cap_data/device_control/links.json" |
| sync_enabled | Boolean | - | 是 | 是否启用同步 | true |
| sync_interval | Integer | 1-3600 | 是 | 同步间隔（秒） | 60 |

#### 4.3.2 cap.update 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |
| cap_info | Object | - | 是 | Cap更新信息 | `{"cap_name": "更新的设备控制能力", "sync_interval": 30}` |

#### 4.3.3 cap.query 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| skill_id | String | 1-64 | 否 | SKILL唯一标识 | "smart_home_controller_001" |
| cap_id | String | 1-64 | 否 | Cap唯一标识 | "device_control_cap_001" |

#### 4.3.4 cap.remove 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |

### 4.4 频道数据管理命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| group.member.add | 添加频道成员 | group_id, skill_id, role, cap_id |
| group.member.remove | 移除频道成员 | group_id, skill_id, cap_id |
| group.link.add | 添加链路关系 | group_id, source_skill_id, target_skill_id, link_type, cap_id |
| group.link.remove | 移除链路关系 | group_id, source_skill_id, target_skill_id, cap_id |
| group.data.set | 设置频道数据 | group_id, data_key, data_value, data_type, cap_id |
| group.data.get | 获取频道数据 | group_id, data_key, cap_id |

#### 4.4.1 group.member.add 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "temperature_sensor_001" |
| role | String | 1-32 | 是 | 成员角色 | "sensor" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.4.2 group.member.remove 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| skill_id | String | 1-64 | 是 | SKILL唯一标识 | "temperature_sensor_001" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.4.3 group.link.add 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| source_skill_id | String | 1-64 | 是 | 源SKILL唯一标识 | "temperature_sensor_001" |
| target_skill_id | String | 1-64 | 是 | 目标SKILL唯一标识 | "air_conditioner_001" |
| link_type | String | 1-32 | 是 | 链路类型 | "data_flow" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.4.4 group.link.remove 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| source_skill_id | String | 1-64 | 是 | 源SKILL唯一标识 | "temperature_sensor_001" |
| target_skill_id | String | 1-64 | 是 | 目标SKILL唯一标识 | "air_conditioner_001" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.4.5 group.data.set 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| data_key | String | 1-64 | 是 | 数据键名 | "temperature" |
| data_value | Any | - | 是 | 数据值 | 25 |
| data_type | String | 1-32 | 是 | 数据类型 | "number" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

#### 4.4.6 group.data.get 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| group_id | String | 1-64 | 是 | 群组ID | "smart_home_group_001" |
| data_key | String | 1-64 | 是 | 数据键名 | "temperature" |
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "smart_home_channel_001" |

### 4.5 VFS同步命令

| 命令 | 功能描述 | 参数 |
|------|----------|------|
| cap.vfs.sync | 同步Cap VFS数据 | cap_id, vfs_path |
| cap.vfs.sync.status | 查询同步状态 | cap_id |
| cap.vfs.recover | 恢复Cap VFS数据 | cap_id |

#### 4.5.1 cap.vfs.sync 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |
| vfs_path | String | 1-256 | 是 | VFS路径 | "/vfs/cap/device_control_cap_001/group.json" |

#### 4.5.2 cap.vfs.sync.status 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |

#### 4.5.3 cap.vfs.recover 命令参数约束

| 参数名 | 数据类型 | 长度限制 | 必填性 | 描述 | 示例值 |
|--------|----------|----------|--------|------|--------|
| cap_id | String | 1-64 | 是 | Cap唯一标识 | "device_control_cap_001" |

### 4.6 命令执行流程

```
┌───────────┐     ┌─────────────┐     ┌─────────────┐     ┌───────────┐
│ 发送请求   │────▶│ 命令解析     │────▶│ 权限验证     │────▶│ 执行命令   │
└───────────┘     └─────────────┘     └─────────────┘     └───────────┘
        ▲                                                               ▲
        │                                                               │
        └───────────────────────────────────────────────────────────────┘
                                  返回结果
```

## 5. 安全机制

### 5.1 认证机制

- **证书认证**：声明为RouteAgent或声明场景的SKILL必须使用唯一证书，证书SN为16位十六进制字符串，通过以下三种方式之一签发：
  - 运营商授权签发
  - 应用商店授权签发
  - 企业手工授权签发
- **JWT 认证**：使用 JSON Web Token 进行身份验证
- **API Key**：使用 API 密钥进行简单认证
- **OAuth 2.0**：支持第三方认证

### 5.2 授权机制

- **基于角色的访问控制 (RBAC)**：根据用户角色分配权限
- **基于资源的访问控制 (RBAC)**：根据资源类型和属性分配权限
- **ABAC**：基于属性的访问控制，支持更灵活的权限策略
- **RouteAgent 特殊授权**：
  - 声明为RouteAgent的SKILL必须经过运营商、应用商店或企业手工授权
  - 授权的RouteAgent可被killflow发现和调度
  - 未授权的SKILL无法声明为RouteAgent
- **场景声明授权**：
  - 声明场景的SKILL必须使用授权证书
  - 证书SN必须为16位十六进制字符串
  - 未授权SKILL无法声明场景

### 5.3 加密机制

- **传输加密**：使用 TLS/SSL 加密传输数据
- **数据加密**：对敏感数据进行端到端加密
- **密钥管理**：安全的密钥生成、分发和轮换机制
- **证书管理**：
  - 证书有效期管理
  - 证书吊销机制
  - 证书更新流程

### 5.4 安全管控流程

1. **SKILL证书申请**：
   - SKILL开发者向运营商、应用商店或企业申请证书
   - 提供SKILL基本信息和用途
   - 审核通过后获取唯一证书（SN为16位十六进制字符串）

2. **RouteAgent声明**：
   - SKILL使用授权证书声明为RouteAgent
   - AI Bridge验证证书有效性和授权范围
   - 验证通过后，RouteAgent可被killflow发现和调度

3. **场景声明**：
   - RouteAgent使用授权证书声明场景
   - AI Bridge验证证书有效性和RouteAgent权限
   - 验证通过后创建场景并允许其他SKILL加入

## 6. 数据模型

### 6.1 消息数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值 |
|------|------|------|------|-----------|--------|
| Message | id | String | 消息唯一标识 | UUID v4 | "123e4567-e89b-12d3-a456-426614174000" |
| Message | version | String | 协议版本 | 主版本号.次版本号 | "0.6.0" |
| Message | timestamp | Long | 消息时间戳 | Unix时间戳（毫秒） | 1737000000000 |
| Message | type | String | 消息类型 | "request" 或 "response" | "request" |
| Message | command | String | 命令名称 | 点分隔的命令路径 | "skill.discover" |
| Message | params | Object | 命令参数 | JSON对象 | `{"space_id": "home_space_001"}` |
| Message | metadata | Object | 元数据 | JSON对象 | `{"sender_id": "agent_123"}` |

### 6.2 错误数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值 |
|------|------|------|------|-----------|--------|
| Error | code | String | 错误码 | 4位数字 | "1001" |
| Error | message | String | 错误消息 | 简洁的错误描述 | "参数错误" |
| Error | details | Object | 错误详情 | 详细的错误信息 | `{"param_name": "space_id", "reason": "格式不正确"}` |

### 6.3 元数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值 |
|------|------|------|------|-----------|--------|
| Metadata | sender_id | String | 发送者标识 | 1-64位字符串 | "agent_123" |
| Metadata | trace_id | String | 跟踪标识 | UUID v4 | "456e7890-e12b-34d5-a678-901234567890" |
| Metadata | priority | String | 消息优先级 | "low"、"medium" 或 "high" | "high" |
| Metadata | timeout | Long | 超时时间 | 毫秒数 | 30000 |

### 6.4 SKILL数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值 |
|------|------|------|------|-----------|--------|
| SKILL | skill_id | String | SKILL唯一标识 | 1-64位字符串 | "smart_home_controller_001" |
| SKILL | name | String | SKILL名称 | 1-128位字符串 | "智能家居控制器" |
| SKILL | description | String | SKILL描述 | 1-512位字符串 | "用于控制智能家居设备的SKILL" |
| SKILL | version | String | SKILL版本 | 主版本号.次版本号.修订版本号 | "1.0.0" |
| SKILL | type | String | SKILL类型 | "system" 或 "custom" | "custom" |
| SKILL | endpoints | Array[String] | 访问端点列表 | 1-10个URL | `["http://192.168.1.100:9010"]` |
| SKILL | capabilities | Array[String] | 能力列表 | 1-100个能力ID | `["temperature_sensor", "device_control"]` |

### 6.5 Cap数据模型

| 实体 | 属性 | 类型 | 描述 | 格式/规则 | 示例值 |
|------|------|------|------|-----------|--------|
| Cap | cap_id | String | Cap唯一标识 | 1-64位字符串 | "device_control_cap_001" |
| Cap | name | String | Cap名称 | 1-128位字符串 | "设备控制能力" |
| Cap | description | String | Cap描述 | 1-512位字符串 | "用于控制智能家居设备的能力" |
| Cap | skill_id | String | 所属SKILL ID | 1-64位字符串 | "smart_home_controller_001" |
| Cap | type | String | Cap类型 | 1-32位字符串 | "device_control" |
| Cap | status | String | 状态 | "active" 或 "inactive" | "active" |
| Cap | supported_scenes | Array[String] | 支持的场景类型 | 1-10个场景类型 | `["SMART_HOME", "OFFICE_AUTOMATION"]` |

## 7. 扩展机制

### 7.1 自定义命令

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

### 7.2 扩展点

| 扩展点 | 描述 | 配置方式 |
|--------|------|----------|
| 命令解析器 | 自定义命令解析逻辑 | 插件配置 |
| 权限验证器 | 自定义权限验证逻辑 | 插件配置 |
| 消息处理器 | 自定义消息处理逻辑 | 插件配置 |
| 错误处理器 | 自定义错误处理逻辑 | 插件配置 |

## 8. 性能优化

### 8.1 批量操作

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

### 8.2 缓存策略

| 缓存类型 | 适用场景 | 过期策略 |
|----------|----------|----------|
| SKILL 元数据 | 频繁的 SKILL 发现请求 | TTL 30 秒 |
| 权限信息 | 频繁的权限验证 | TTL 10 分钟 |
| 资源信息 | 频繁的资源查询 | TTL 1 分钟 |

## 9. Cap频道管理

### 9.1 Cap声明流程

1. SKILL初始化时，声明其支持的Cap列表
2. SKILL向AI Bridge发送cap.declare命令
3. AI Bridge验证Cap信息并注册
4. AI Bridge返回注册结果

### 9.2 Cap与Scene关联

SKILL声明Scene时，需要指定一个Cap作为频道：

```json
{
  "version": "0.6.0",
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

## 10. VFS同步协议

### 10.1 同步机制

所有Cap相关数据通过VFS实现云端同步，包括：

| 数据类型 | 本地存储 | 云端存储 | 同步方式 |
|----------|----------|----------|----------|
| Cap配置 | cap.json | /vfs/cap/{cap_id}/cap.json | 实时同步 |
| disk数据 | Cap专属VFS目录 | /vfs/cap/{cap_id}/disk/ | 实时/定期同步 |
| group数据 | group.json | /vfs/cap/{cap_id}/group.json | 实时同步 |
| links数据 | links.json | /vfs/cap/{cap_id}/links.json | 实时同步 |

### 10.2 同步命令格式

```json
{
  "version": "0.6.0",
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

## 11. 版本管理

### 11.1 版本升级策略

- 主版本号：重大功能变更，可能不兼容旧版本
- 次版本号：新增功能，兼容旧版本
- 修订版本号：bug修复，兼容旧版本

### 11.2 向后兼容性

- 支持版本协商机制
- 旧版本消息自动转换
- 废弃功能标记和警告

## 12. 测试规范

### 12.1 协议测试方法

- 单元测试：测试协议各组件的基本功能
- 集成测试：测试组件间的交互
- 性能测试：测试协议的性能和吞吐量
- 安全测试：测试协议的安全性

### 12.2 测试用例

```json
{
  "test_case_id": "tc_001",
  "description": "测试cap.declare命令",
  "request": {
    "version": "0.6.0",
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

## 13. 部署指南

### 13.1 协议部署配置

- 端口配置：默认9010-9020
- 安全配置：TLS/SSL证书配置
- 日志配置：日志级别和格式

### 13.2 最佳实践

- 建议使用WebSocket协议进行实时通信
- 定期备份Cap数据
- 启用VFS同步功能确保数据一致性

## 14. 错误处理

### 14.1 错误码体系

| 错误码范围 | 错误类型 |
|------------|----------|
| 1000-1999 | 参数错误 |
| 2000-2999 | 认证授权错误 |
| 3000-3999 | 资源错误 |
| 4000-4999 | 服务错误 |
| 5000-5999 | 系统错误 |

### 14.2 重试机制

| 错误类型 | 重试策略 |
|----------|----------|
| 网络错误 | 指数退避重试 |
| 服务繁忙 | 延迟重试 |
| 参数错误 | 不重试 |
| 权限错误 | 不重试 |