# Skill 数据模型

## 1. 文档概述

本文档详细定义了 Ooder AI Bridge 协议 v0\.6\.2 �?Skill 服务的数据模型，包括实体、关系、属性和约束等内容�?
## 2. 实体关系�?
```
┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? Skill         │────▶│  Endpoint      �?    �? Capability    �?└────────────────�?    └────────────────�?    └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?                      �?┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? SkillMetadata �?    �? EndpointParam �?    �? CapabilityParam�?└────────────────�?    └────────────────�?    └────────────────�?        �?                      �?                      �?        �?                      �?                      �?        �?                      �?                      �?┌────────────────�?    ┌────────────────�?    ┌────────────────�?�? SkillStatus   �?    �? Channel       �?    �? ChannelContext�?└────────────────�?    └────────────────�?    └────────────────�?```

## 3. 实体定义

### 3.1 Skill 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| skill_id | String(36) | PRIMARY KEY | Skill 唯一标识符，使用 UUID |
| name | String(255) | NOT NULL | Skill 名称 |
| description | String(1024) | | Skill 描述 |
| version | String(32) | NOT NULL | Skill 版本，遵循语义化版本规范 |
| status | Enum | NOT NULL | Skill 状态：active, inactive, maintenance |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |
| registered_at | Timestamp | | 注册时间，ISO 8601 格式 |
| last_heartbeat | Timestamp | | 最后心跳时间，ISO 8601 格式 |

### 3.2 Endpoint 实体

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

### 3.3 Capability 实体

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

### 3.4 SkillMetadata 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| metadata_id | String(36) | PRIMARY KEY | Metadata 唯一标识符，使用 UUID |
| skill_id | String(36) | FOREIGN KEY | 所�?Skill ID |
| key | String(64) | NOT NULL | Metadata �?|
| value | String(512) | NOT NULL | Metadata �?|
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 3.5 EndpointParam 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| param_id | String(36) | PRIMARY KEY | 参数唯一标识符，使用 UUID |
| endpoint_id | String(36) | FOREIGN KEY | 所�?Endpoint ID |
| key | String(64) | NOT NULL | 参数�?|
| value | String(512) | NOT NULL | 参数�?|
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 3.6 CapabilityParam 实体

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

### 3.7 SkillStatus 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| status_id | String(36) | PRIMARY KEY | 状态唯一标识符，使用 UUID |
| skill_id | String(36) | FOREIGN KEY | 所�?Skill ID |
| health | Enum | NOT NULL | 健康状态：healthy, unhealthy, warning |
| uptime | Integer | NOT NULL DEFAULT 0 | 运行时间（秒�?|
| request_count | Integer | NOT NULL DEFAULT 0 | 请求计数 |
| error_count | Integer | NOT NULL DEFAULT 0 | 错误计数 |
| cpu_usage | Double | NOT NULL DEFAULT 0 | CPU 使用率（%�?|
| memory_usage | Double | NOT NULL DEFAULT 0 | 内存使用率（%�?|
| last_check | Timestamp | NOT NULL | 最后检查时间，ISO 8601 格式 |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

### 3.8 Channel 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| channel_id | String(36) | PRIMARY KEY | Channel 唯一标识符，使用 UUID |
| skill_id | String(36) | FOREIGN KEY | 所�?Skill ID |
| capability_id | String(36) | FOREIGN KEY | 所�?Capability ID |
| status | Enum | NOT NULL | 状态：open, closed, error |
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |
| expiry_time | Timestamp | | 过期时间，ISO 8601 格式 |

### 3.9 ChannelContext 实体

| 属性名�?| 数据类型 | 约束 | 描述 |
|----------|----------|------|------|
| context_id | String(36) | PRIMARY KEY | 上下文唯一标识符，使用 UUID |
| channel_id | String(36) | FOREIGN KEY | 所�?Channel ID |
| key | String(64) | NOT NULL | 上下文键 |
| value | String(1024) | NOT NULL | 上下文�?|
| created_at | Timestamp | NOT NULL | 创建时间，ISO 8601 格式 |
| updated_at | Timestamp | NOT NULL | 更新时间，ISO 8601 格式 |

## 4. 关系定义

### 4.1 Skill - Endpoint 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Skill 可以有多�?Endpoint |

### 4.2 Skill - Capability 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Skill 可以有多�?Capability |

### 4.3 Skill - SkillMetadata 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Skill 可以有多�?Metadata |

### 4.4 Endpoint - EndpointParam 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Endpoint 可以有多个参�?|

### 4.5 Capability - CapabilityParam 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Capability 可以有多个参�?|

### 4.6 Skill - SkillStatus 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对一 | 1:1 | 一�?Skill 对应一个状态记�?|

### 4.7 Channel - ChannelContext 关系

| 关系类型 | 基数 | 描述 |
|----------|------|------|
| 一对多 | 1:N | 一�?Channel 可以有多个上下文信息 |

## 5. 索引定义

### 5.1 Skill 索引

| 索引名称 | 索引类型 | 索引字段 | 描述 |
|----------|----------|----------|------|
| idx_skill_name | UNIQUE | name, version | 唯一确定一�?Skill 版本 |
| idx_skill_status | NORMAL | status | 按状态查�?Skill |
| idx_skill_created | NORMAL | created_at | 按创建时间排�?|

### 5.2 Endpoint 索引

| 索引名称 | 索引类型 | 索引字段 | 描述 |
|----------|----------|----------|------|
| idx_endpoint_skill | NORMAL | skill_id | �?Skill ID 查询 Endpoint |
| idx_endpoint_status | NORMAL | status | 按状态查�?Endpoint |

### 5.3 Capability 索引

| 索引名称 | 索引类型 | 索引字段 | 描述 |
|----------|----------|----------|------|
| idx_capability_skill | NORMAL | skill_id | �?Skill ID 查询 Capability |
| idx_capability_category | NORMAL | category | 按分类查�?Capability |
| idx_capability_status | NORMAL | status | 按状态查�?Capability |

### 5.4 Channel 索引

| 索引名称 | 索引类型 | 索引字段 | 描述 |
|----------|----------|----------|------|
| idx_channel_skill | NORMAL | skill_id | �?Skill ID 查询 Channel |
| idx_channel_expiry | NORMAL | expiry_time | 按过期时间查�?Channel |
| idx_channel_status | NORMAL | status | 按状态查�?Channel |

## 6. 数据验证规则

### 6.1 Skill 验证规则

1. Skill 名称必须唯一
2. Skill 版本必须遵循语义化版本规范（MAJOR.MINOR.PATCH�?3. Skill 状态只能是 active, inactive �?maintenance 之一

### 6.2 Endpoint 验证规则

1. Endpoint URL 必须是有效的 URL 格式
2. Endpoint 协议只能�?http, https, ws �?wss 之一
3. Endpoint 权重必须是正整数

### 6.3 Capability 验证规则

1. Capability 名称在同一�?Skill 内必须唯一
2. Capability 版本必须遵循语义化版本规�?3. Capability 状态只能是 enabled �?disabled 之一

## 7. 数据一致性约�?
### 7.1 外键约束

1. Endpoint.skill_id 必须存在�?Skill.skill_id
2. Capability.skill_id 必须存在�?Skill.skill_id
3. SkillMetadata.skill_id 必须存在�?Skill.skill_id
4. EndpointParam.endpoint_id 必须存在�?Endpoint.endpoint_id
5. CapabilityParam.capability_id 必须存在�?Capability.capability_id
6. SkillStatus.skill_id 必须存在�?Skill.skill_id
7. Channel.skill_id 必须存在�?Skill.skill_id
8. Channel.capability_id 必须存在�?Capability.capability_id
9. ChannelContext.channel_id 必须存在�?Channel.channel_id

### 7.2 事务约束

1. Skill 注册/注销操作必须是原子的
2. Endpoint 增删改操作必须与 Skill 状态一�?3. Capability 增删改操作必须与 Skill 状态一�?4. Channel 创建/销毁操作必须是原子�?
## 8. 数据迁移策略

### 8.1 版本升级

1. 数据模型变更必须向后兼容
2. 升级前必须进行数据备�?3. 升级过程中必须保持服务可用�?4. 升级后必须进行数据验�?
### 8.2 数据清理

1. 定期清理过期�?Channel �?ChannelContext
2. 定期清理历史状态记�?3. 清理操作必须在低峰期进行
4. 清理前必须进行数据备�?
---

**Ooder Technology Co., Ltd.**
