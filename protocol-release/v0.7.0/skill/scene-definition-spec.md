# 场景定义规范 (Scene Definition Specification)

> 本文档定义了Ooder Agent SDK v0.7.0的场景定义规范，包括场景类型、场景配置和场景依赖关系。

## 文档信息

- 版本：v0.7.0
- 创建日期：2026-02-15
- 状态：定稿

---

## 一、概述

### 1.1 场景定义

**场景（Scene）** 是能力需求的抽象定义，描述"做什么"，是技能的契约和接口。

### 1.2 场景特点

| 特点 | 描述 |
|------|------|
| 抽象性 | 场景定义能力需求，不涉及具体实现 |
| 共识性 | 场景是行业或领域内的共识定义 |
| 稳定性 | 场景定义相对稳定，不频繁变更 |
| 可扩展 | 场景可以通过技能实现进行扩展 |

---

## 二、场景类型

### 2.1 主场景（Primary Scene）

**定义：** 技能的核心功能场景，技能是场景的主要提供者。

**特点：**
- 技能必须提供
- 场景生命周期与技能绑定
- 一个技能只能有一个主场景

**示例：**
```
skill-msg-feishu 的主场景: messaging
skill-org-feishu 的主场景: auth
```

### 2.2 协作场景（Collaborative Scene）

**定义：** 技能依赖的其他场景，技能是场景的消费者。

**特点：**
- 技能依赖但不提供
- 需要加入已存在的场景
- 或触发新技能安装
- 一个技能可以有多个协作场景

**示例：**
```
skill-msg-feishu 的协作场景:
- auth (组织认证)
- vfs (文件传输)
```

---

## 三、场景定义格式

### 3.1 scene.yaml 结构

```yaml
apiVersion: skill.ooder.net/v1
kind: SceneDefinition

metadata:
  name: string                    # 场景名称（必需）
  version: string                 # 场景协议版本（必需）
  description: string             # 场景描述（必需）

spec:
  type: primary | collaborative   # 场景类型（必需）
  
  capabilities:                   # 能力列表（必需）
    - id: string                  # 能力ID
      name: string                # 能力名称
      description: string         # 能力描述
      category: string            # 能力分类
      version: string             # 能力版本
      parameters:                 # 输入参数
        - name: string
          type: string
          required: boolean
          description: string
      returns:                    # 返回值
        type: string
        description: string
      errors:                     # 错误定义
        - code: string
          message: string
      permissions:                # 所需权限
        - string
      rate:                       # 速率限制
        max: integer
        window: integer
  
  roles:                          # 角色定义
    - name: string                # 角色名称
      capabilities:               # 角色能力
        - string
  
  protocols:                      # 通信协议
    transport: http | websocket | grpc
    format: json | protobuf | msgpack
    encryption: tls | mtls | none
  
  security:                       # 安全策略
    authRequired: boolean
    allowedRoles:
      - string
```

### 3.2 字段定义

#### metadata 字段

| 字段 | 类型 | 必需 | 默认值 | 验证规则 | 描述 |
|------|------|------|--------|----------|------|
| name | string | 是 | - | `^[a-z][a-z0-9-]*$` | 场景名称 |
| version | string | 是 | - | 语义化版本 | 场景协议版本 |
| description | string | 是 | - | 最大500字符 | 场景描述 |

#### spec 字段

| 字段 | 类型 | 必需 | 默认值 | 验证规则 | 描述 |
|------|------|------|--------|----------|------|
| type | string | 是 | - | 枚举: primary, collaborative | 场景类型 |
| capabilities | array | 是 | - | 至少1个元素 | 能力列表 |
| roles | array | 否 | [] | - | 角色定义 |
| protocols | object | 否 | {} | - | 通信协议 |
| security | object | 否 | {} | - | 安全策略 |

---

## 四、场景依赖配置

### 4.1 技能场景配置

在skill.yaml中配置场景依赖：

```yaml
spec:
  scenes:
    primary:                      # 主场景
      name: messaging
      role: provider              # 提供者角色
      capabilities:               # 提供的能力
        - msg-send
        - msg-receive
        - msg-history
    
    collaborative:                # 协作场景
      - name: auth                # 场景名称
        role: consumer            # 消费者角色
        required: true            # 必须依赖
        capabilities:             # 需要的能力
          - org-data-read
          - user-auth
        fallback:                 # 降级策略
          action: install         # 不存在时安装
          prefer: skill-org-feishu # 优先安装
          message: "需要组织认证场景"
      
      - name: vfs
        role: consumer
        required: false           # 可选依赖
        capabilities:
          - file-upload
          - file-download
        fallback:
          action: skip            # 不存在时跳过
          message: "文件传输功能不可用"
```

### 4.2 场景依赖字段

#### primary 字段

| 字段 | 类型 | 必需 | 默认值 | 验证规则 | 描述 |
|------|------|------|--------|----------|------|
| name | string | 是 | - | 场景名称格式 | 场景名称 |
| role | string | 是 | - | 枚举: provider | 角色 |
| capabilities | array | 否 | [] | 字符串数组 | 提供的能力 |

#### collaborative[] 元素

| 字段 | 类型 | 必需 | 默认值 | 验证规则 | 描述 |
|------|------|------|--------|----------|------|
| name | string | 是 | - | 场景名称格式 | 场景名称 |
| role | string | 是 | - | 枚举: consumer | 角色 |
| required | boolean | 否 | true | - | 是否必需 |
| capabilities | array | 是 | - | 字符串数组，至少1个 | 需要的能力 |
| fallback | object | 否 | {} | - | 降级策略 |

#### fallback 字段

| 字段 | 类型 | 必需 | 默认值 | 验证规则 | 描述 |
|------|------|------|--------|----------|------|
| action | string | 否 | "fail" | 枚举: install, skip, fail | 降级动作 |
| prefer | string | 否 | "" | 技能ID格式 | 优先安装的技能 |
| message | string | 否 | "" | 最大256字符 | 提示信息 |

---

## 五、场景示例

### 5.1 认证场景

```yaml
apiVersion: skill.ooder.net/v1
kind: SceneDefinition

metadata:
  name: auth
  version: "1.0"
  description: 组织认证场景，提供用户认证和组织数据访问能力

spec:
  type: primary
  
  capabilities:
    - id: user-auth
      name: User Authentication
      description: 用户认证
      category: authentication
      parameters:
        - name: credentials
          type: object
          required: true
          description: 认证凭证
      returns:
        type: AuthResult
        description: 认证结果
      errors:
        - code: AUTH_FAILED
          message: 认证失败
        - code: CREDENTIALS_EXPIRED
          message: 凭证过期
      permissions:
        - auth:execute
    
    - id: org-data-read
      name: Organization Data Read
      description: 读取组织数据
      category: data-access
      parameters:
        - name: dataType
          type: string
          required: true
          description: 数据类型
        - name: filter
          type: object
          required: false
          description: 过滤条件
      returns:
        type: object
        description: 组织数据
      permissions:
        - org:read
  
  roles:
    - name: admin
      capabilities:
        - user-auth
        - org-data-read
    - name: user
      capabilities:
        - user-auth
  
  protocols:
    transport: http
    format: json
    encryption: tls
  
  security:
    authRequired: true
    allowedRoles:
      - admin
      - user
```

### 5.2 消息场景

```yaml
apiVersion: skill.ooder.net/v1
kind: SceneDefinition

metadata:
  name: messaging
  version: "1.0"
  description: 即时消息场景，提供消息发送、接收和历史查询能力

spec:
  type: primary
  
  capabilities:
    - id: msg-send
      name: Send Message
      description: 发送消息
      category: communication
      parameters:
        - name: to
          type: string
          required: true
          description: 接收者ID
        - name: content
          type: object
          required: true
          description: 消息内容
      returns:
        type: MessageResult
        description: 发送结果
      errors:
        - code: USER_NOT_FOUND
          message: 用户不存在
        - code: PERMISSION_DENIED
          message: 无发送权限
      permissions:
        - msg:send
      rate:
        max: 100
        window: 60
    
    - id: msg-receive
      name: Receive Message
      description: 接收消息
      category: communication
      returns:
        type: Message
        description: 消息内容
      permissions:
        - msg:receive
    
    - id: msg-history
      name: Message History
      description: 查询消息历史
      category: data-access
      parameters:
        - name: userId
          type: string
          required: true
          description: 用户ID
        - name: limit
          type: integer
          required: false
          description: 返回数量
      returns:
        type: array
        description: 消息历史列表
      permissions:
        - msg:history
  
  roles:
    - name: sender
      capabilities:
        - msg-send
    - name: receiver
      capabilities:
        - msg-receive
        - msg-history
    - name: admin
      capabilities:
        - msg-send
        - msg-receive
        - msg-history
  
  protocols:
    transport: websocket
    format: json
    encryption: tls
  
  security:
    authRequired: true
    allowedRoles:
      - sender
      - receiver
      - admin
```

---

## 六、版本历史

| 版本 | 日期 | 变更说明 |
|------|------|----------|
| v0.7.0 | 2026-02-15 | 初始版本，定义场景规范 |
