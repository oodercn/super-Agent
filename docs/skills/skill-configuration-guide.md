# SKILL加入Agent服务配置指南

## 1. 概述

本文档详细说明开发一个SKILL需要具备哪些配置信息才能成功加入到SuperAgent系统的Agent服务中。SKILL作为SuperAgent系统的功能模块，需要通过EndAgent注册并与其他组件进行交互。

## 2. 核心配置信息

### 2.1 基本标识配置

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| skillId | SKILL唯一标识 | `file-upload-skill-001` | 必填 |
| name | SKILL名称 | `文件上传技能` | 必填 |
| description | SKILL功能描述 | `提供文件上传和管理功能` | 必填 |
| version | SKILL版本号 | `1.0.0` | 必填 |
| ownerType | 所有者类型 | `PERSONAL`/`ORGANIZATION`/`EXTERNAL` | 必填 |
| ownerId | 所有者ID | `user-123456` | 必填 |
| categories | 功能分类列表 | `["file", "storage"]` | 必填 |

### 2.2 功能接口配置

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| interfaces | 对外提供的接口列表 | 见接口配置详情 | 必填 |
| endpoints | 通信端点配置列表 | 见端点配置详情 | 必填 |

#### 接口配置详情

```json
[
  {
    "interfaceId": "upload-file-001",
    "name": "上传文件",
    "description": "上传文件到指定位置",
    "method": "POST",
    "path": "/upload",
    "params": [
      {
        "name": "file",
        "type": "file",
        "required": true,
        "description": "要上传的文件"
      },
      {
        "name": "targetPath",
        "type": "string",
        "required": true,
        "description": "目标存储路径"
      }
    ],
    "returnType": "json",
    "categories": ["file"]
  }
]
```

#### 端点配置详情

```json
{
  "endpointId": "endpoint-001",
  "protocol": "HTTPS",
  "host": "localhost",
  "port": 8080,
  "path": "/api/v1",
  "description": "主接口端点",
  "security": {
    "tlsEnabled": true,
    "certificate": "-----BEGIN CERTIFICATE-----..."
  }
}
```

### 2.3 安全配置

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| publicKey | 公钥 | `-----BEGIN PUBLIC KEY-----...` | 必填 |
| isAiServerAuthenticated | 是否通过AIServer认证 | `true` | 必填 |
| accessPolicy | 访问控制策略 | 见访问策略详情 | 必填 |

#### 访问策略详情

```json
{
  "policyId": "access-policy-001",
  "permissions": [
    {
      "resource": "*",
      "actions": ["read", "write"],
      "effect": "allow",
      "conditions": {
        "agentType": ["EndAgent", "RouteAgent"]
      }
    }
  ],
  "defaultEffect": "deny"
}
```

### 2.4 部署配置

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| deploymentType | 部署方式 | `local`/`edge`/`cloud`/`hybrid` | 必填 |
| location | 部署位置信息 | 见位置配置详情 | 必填 |
| skillType | SKILL类型 | `FILE_ACCESS_SKILL`/`NETWORK_SKILL`等 | 必填 |
| techStack | 技术栈 | `Java, Spring Boot, MySQL` | 必填 |

#### 位置配置详情

```json
{
  "spaceId": "home-space",
  "spaceName": "home",
  "zoneId": "living-room-zone",
  "zoneName": "living-room",
  "physicalLocation": "北京市朝阳区xxx小区"
}
```

### 2.5 元数据配置

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| isStatic | 是否为静态定义状态 | `true` | 必填 |
| organizationId | 组织ID（如果是组织SKILL） | `org-789012` | 可选 |
| vfsFilePath | VFS文件地址（静态定义时指定） | `/skills/file-upload-skill.json` | 可选 |

## 3. 注册流程

### 3.1 SKILL向EndAgent注册流程

1. **准备注册信息**：SKILL准备包含上述所有配置信息的注册请求
2. **发送注册请求**：SKILL向EndAgent发送注册请求，包含基本信息和Capability列表
3. **验证与注册**：EndAgent验证SKILL的合法性，将SKILL信息注册到本地注册表
4. **通知RouteAgent**：EndAgent通知RouteAgent SKILL注册成功，包含SKILL基本信息和Capability列表

### 3.2 注册请求示例

```json
{
  "messageType": "SKILL_REGISTRATION_REQUEST",
  "timestamp": 1673846400000,
  "skillInfo": {
    "skillId": "file-upload-skill-001",
    "name": "文件上传技能",
    "description": "提供文件上传和管理功能",
    "version": "1.0.0",
    "ownerType": "PERSONAL",
    "ownerId": "user-123456",
    "categories": ["file", "storage"],
    "skillType": "FILE_ACCESS_SKILL",
    "techStack": "Java, Spring Boot, MySQL",
    "deploymentType": "local",
    "location": {
      "spaceId": "home-space",
      "spaceName": "home",
      "zoneId": "living-room-zone",
      "zoneName": "living-room"
    },
    "status": "REGISTERED",
    "isStatic": true,
    "isAiServerAuthenticated": true,
    "publicKey": "-----BEGIN PUBLIC KEY-----...",
    "endpoints": [
      {
        "endpointId": "endpoint-001",
        "protocol": "HTTPS",
        "host": "localhost",
        "port": 8080,
        "path": "/api/v1"
      }
    ],
    "interfaces": [
      {
        "interfaceId": "upload-file-001",
        "name": "上传文件",
        "description": "上传文件到指定位置",
        "method": "POST",
        "path": "/upload",
        "params": [
          {
            "name": "file",
            "type": "file",
            "required": true
          },
          {
            "name": "targetPath",
            "type": "string",
            "required": true
          }
        ],
        "returnType": "json",
        "categories": ["file"]
      }
    ],
    "accessPolicy": {
      "policyId": "access-policy-001",
      "permissions": [
        {
          "resource": "*",
          "actions": ["read", "write"],
          "effect": "allow",
          "conditions": {
            "agentType": ["EndAgent"]
          }
        }
      ]
    }
  }
}
```

## 4. 验证与测试

### 4.1 配置验证

在注册前，SKILL应该验证自身配置的完整性和正确性：

1. 确保所有必填配置项都已填写
2. 验证配置格式是否符合规范
3. 检查端点地址和端口是否正确
4. 验证安全配置是否有效

### 4.2 功能测试

注册成功后，SKILL应该进行功能测试：

1. 测试接口调用是否正常
2. 验证安全机制是否有效
3. 检查与EndAgent的通信是否稳定
4. 测试异常情况的处理

## 5. 常见问题与解决方案

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 注册失败：SKILL ID重复 | skillId已被其他SKILL使用 | 生成新的唯一skillId |
| 注册失败：端点不可访问 | EndAgent无法访问SKILL的端点 | 检查网络连接和端口配置 |
| 注册失败：安全认证失败 | 公钥无效或未通过AIServer认证 | 重新生成公钥并确保通过AIServer认证 |
| 调用失败：权限不足 | 访问策略配置不当 | 调整accessPolicy，确保调用方有足够权限 |
| 调用失败：接口不存在 | 接口配置与实际实现不一致 | 检查接口path和method配置是否正确 |

## 6. 最佳实践

1. **唯一性**：确保skillId全局唯一，建议使用UUID或包含时间戳的格式
2. **安全性**：始终使用HTTPS和有效的证书，保护通信安全
3. **最小权限**：访问策略遵循最小权限原则，仅授予必要的访问权限
4. **版本控制**：使用语义化版本号，便于版本管理和升级
5. **文档化**：提供详细的接口文档和使用说明
6. **监控与日志**：实现完善的监控和日志记录，便于问题排查

## 7. VFS集成配置

### 7.1 VFS概述

VFS（Virtual File System）是SuperAgent系统中的文件同步和共享中心，为所有SKILL提供集中存储服务。SKILL可以使用VFS作为核心存储，也可以在VFS不可用时自动切换到本地存储。

### 7.2 VFS配置信息

| 配置项 | 描述 | 示例值 | 必要性 |
|--------|------|--------|--------|
| vfs.serverUrl | VFS服务器地址 | `http://localhost:8080/vfs` | 必填 |
| vfs.groupName | VFS组名 | `skill-a-group` | 必填 |
| vfs.connectionTimeout | 连接超时时间（毫秒） | `5000` | 必填 |
| vfs.retryCount | 重试次数 | `3` | 必填 |
| vfs.enabled | 是否启用VFS | `true` | 必填 |

### 7.3 VFS集成示例

在application.yml中配置VFS：

```yaml
# VFS Configuration
vfs:
  serverUrl: http://localhost:8080/vfs
  groupName: skill-a-group
  connectionTimeout: 5000
  retryCount: 3
  enabled: true
```

### 7.4 VFS存储实现

SKILL需要实现自动切换VFS和本地存储的机制：

1. 当VFS可用时，使用VFS作为核心存储
2. 当VFS不可用时，自动切换到本地存储
3. 当VFS重新可用时，自动同步本地更改到VFS

### 7.5 VFS同步策略

| 同步类型 | 描述 | 触发条件 |
|----------|------|----------|
| 本地到VFS | 将本地文件同步到VFS | VFS重新可用时 |
| VFS到本地 | 将VFS文件同步到本地 | 首次连接VFS时 |
| 双向同步 | 同时同步本地和VFS的更改 | SKILL启动时 |

## 8. 参考文档

- [SuperAgent Skill开发规范](superagent-skill-dev-spec.md)
- [SKILL与EndAgent通讯设计](../../communication/superagent-agent-communication-design/02-skill-endagent-communication.md)
- [SuperAgent安全设计](../security/superagent-security-design.md)
- [VFS协议规范](../../protocol-release/0.6.2/skill/skill-vfs-protocol.md)
