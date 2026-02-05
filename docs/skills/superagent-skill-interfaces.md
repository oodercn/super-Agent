# SuperAgent Skill接口功能分类与Endpoint映射设计

## 1. Skill接口功能分类（Capability And Type）定义

### 1.1 核心分类

| 分类名称 | 分类标识（Capability And Type） | 描述 |
|---------|---------------|------|
| 联网管理 | network | 管理skill的网络连接状态 |
| 本地文件访问 | file | 访问和操作本地文件系统 |
| 网络访问 | http | 发起HTTP/HTTPS请求 |
| 系统管理 | system | 管理skill的系统资源 |
| 安全管理 | security | 处理skill的安全相关功能 |
| 消息通信 | message | 处理消息发送和接收 |
| 数据处理 | data | 处理和转换数据 |
| 定时任务 | schedule | 执行定时任务 |

### 1.2 扩展分类

| 分类名称 | 分类标识（Capability And Type） | 描述 |
|---------|---------------|------|
| 数据库访问 | database | 访问数据库 |
| 缓存操作 | cache | 操作缓存系统 |
| 事件处理 | event | 处理和发布事件 |
| 日志管理 | logging | 管理日志记录 |
| 监控告警 | monitor | 监控系统状态和产生告警 |
| 配置管理 | config | 管理配置信息 |

## 2. Endpoint映射规则

### 2.1 基本格式

```
{protocol}://{host}:{port}/{api_version}/{Capability_And_Type}/{skill_id}/{interface_path}
```

### 2.2 各部分说明

| 部分 | 说明 | 示例 |
|-----|------|------|
| protocol | 通信协议 | http, https, ws, wss等 |
| host | Skill主机地址 | localhost, 192.168.1.100等 |
| port | Skill端口 | 8080, 9090等 |
| api_version | API版本 | v1, v2等 |
| Capability_And_Type | 功能分类 | network, file, http等 |
| skill_id | Skill唯一标识 | skill-001, file-handler等 |
| interface_path | 接口路径 | /connect, /read, /get等 |

### 2.3 示例Endpoint

```
http://localhost:8080/v1/network/skill-001/connect
https://192.168.1.100:9090/v1/file/storage-skill/read
ws://localhost:8081/v1/message/chat-skill/subscribe
```

## 3. 接口参数与返回值规范

### 3.1 请求参数规范

#### 3.1.1 查询参数（Query Parameters）

| 参数名 | 类型 | 必须 | 描述 | 示例 |
|-------|------|------|------|------|
| timeout | integer | 否 | 请求超时时间（毫秒） | 5000 |
| retry | integer | 否 | 重试次数 | 3 |
| format | string | 否 | 返回数据格式 | json, xml |

#### 3.1.2 路径参数（Path Parameters）

根据接口定义，在interface_path中包含的参数。

示例：
```
http://localhost:8080/v1/file/storage-skill/read/{file_path}
```

#### 3.1.3 请求体（Request Body）

请求体采用JSON格式，具体字段根据接口定义。

### 3.2 返回值规范

#### 3.2.1 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 具体数据
  },
  "timestamp": 1620000000000,
  "requestId": "req-001"
}
```

#### 3.2.2 错误响应

```json
{
  "code": 400,
  "message": "Bad Request",
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "Parameter 'file_path' is required"
  },
  "timestamp": 1620000000000,
  "requestId": "req-001"
}
```

### 3.3 响应码定义

| 响应码 | 描述 | 分类 |
|-------|------|------|
| 200 | 成功 | 成功 |
| 400 | 坏请求 | 客户端错误 |
| 401 | 未授权 | 客户端错误 |
| 403 | 禁止访问 | 客户端错误 |
| 404 | 资源未找到 | 客户端错误 |
| 500 | 服务器内部错误 | 服务器错误 |
| 501 | 未实现 | 服务器错误 |
| 502 | 网关错误 | 服务器错误 |
| 503 | 服务不可用 | 服务器错误 |
| 504 | 网关超时 | 服务器错误 |

## 4. 各分类接口设计

### 4.1 联网管理（network）接口

#### 4.1.1 连接网络

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 连接网络 |
| 接口标识 | connect |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/network/{skill_id}/connect` |
| 请求体 | `{"networkType": "wifi", "ssid": "mywifi", "password": "mypassword"}` |
| 返回值 | `{"connected": true, "ip": "192.168.1.100", "mac": "00:11:22:33:44:55"}` |

#### 4.1.2 断开网络

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 断开网络 |
| 接口标识 | disconnect |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/network/{skill_id}/disconnect` |
| 请求体 | `{"networkType": "wifi"}` |
| 返回值 | `{"disconnected": true}` |

#### 4.1.3 获取网络状态

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 获取网络状态 |
| 接口标识 | status |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/network/{skill_id}/status` |
| 返回值 | `{"connected": true, "networkType": "wifi", "ssid": "mywifi", "ip": "192.168.1.100", "mac": "00:11:22:33:44:55", "signalStrength": 85}` |

### 4.2 本地文件访问（file）接口

#### 4.2.1 读取文件

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 读取文件 |
| 接口标识 | read |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/file/{skill_id}/read/{file_path}` |
| Query参数 | `encoding`: 文件编码（默认：utf-8） |
| 返回值 | `{"content": "file content", "size": 1024, "modifiedTime": 1620000000000}` |

#### 4.2.2 写入文件

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 写入文件 |
| 接口标识 | write |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/file/{skill_id}/write/{file_path}` |
| Request Body | `{"content": "new content", "overwrite": true, "encoding": "utf-8"}` |
| 返回值 | `{"success": true, "size": 128, "modifiedTime": 1620000000000}` |

#### 4.2.3 删除文件

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 删除文件 |
| 接口标识 | delete |
| HTTP方法 | DELETE |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/file/{skill_id}/delete/{file_path}` |
| Query参数 | `recursive`: 是否递归删除目录（默认：false） |
| 返回值 | `{"success": true}` |

#### 4.2.4 列出目录

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 列出目录 |
| 接口标识 | list |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/file/{skill_id}/list/{directory_path}` |
| Query参数 | `recursive`: 是否递归列出（默认：false） |
| 返回值 | `{"files": [{"name": "file1.txt", "type": "file", "size": 1024, "modifiedTime": 1620000000000}, {"name": "dir1", "type": "directory", "size": 0, "modifiedTime": 1620000000000}]}` |

### 4.3 网络访问（http）接口

#### 4.3.1 发起GET请求

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 发起GET请求 |
| 接口标识 | get |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/http/{skill_id}/get` |
| Request Body | `{"url": "https://api.example.com/data", "headers": {"Authorization": "Bearer token"}, "params": {"param1": "value1"}, "timeout": 5000}` |
| 返回值 | `{"status": 200, "headers": {"Content-Type": "application/json"}, "body": {"data": "response data"}, "duration": 120}` |

#### 4.3.2 发起POST请求

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 发起POST请求 |
| 接口标识 | post |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/http/{skill_id}/post` |
| Request Body | `{"url": "https://api.example.com/data", "headers": {"Content-Type": "application/json"}, "body": {"key": "value"}, "timeout": 5000}` |
| 返回值 | `{"status": 201, "headers": {"Content-Type": "application/json"}, "body": {"id": "123"}, "duration": 250}` |

#### 4.3.3 发起PUT请求

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 发起PUT请求 |
| 接口标识 | put |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/http/{skill_id}/put` |
| Request Body | `{"url": "https://api.example.com/data/123", "headers": {"Content-Type": "application/json"}, "body": {"key": "new-value"}, "timeout": 5000}` |
| 返回值 | `{"status": 200, "headers": {"Content-Type": "application/json"}, "body": {"success": true}, "duration": 180}` |

#### 4.3.4 发起DELETE请求

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 发起DELETE请求 |
| 接口标识 | delete |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/http/{skill_id}/delete` |
| Request Body | `{"url": "https://api.example.com/data/123", "headers": {"Authorization": "Bearer token"}, "timeout": 5000}` |
| 返回值 | `{"status": 204, "headers": {}, "body": null, "duration": 100}` |

### 4.4 系统管理（system）接口

#### 4.4.1 获取系统信息

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 获取系统信息 |
| 接口标识 | info |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/system/{skill_id}/info` |
| 返回值 | `{"os": "Linux", "version": "5.10.0", "cpu": {"cores": 4, "usage": 25}, "memory": {"total": 8589934592, "used": 2147483648, "free": 6442450944}, "disk": {"total": 107374182400, "used": 21474836480, "free": 85899345920}}` |

#### 4.4.2 获取进程信息

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 获取进程信息 |
| 接口标识 | processes |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/system/{skill_id}/processes` |
| Query参数 | `pid`: 进程ID（可选，不提供则返回所有进程） |
| 返回值 | `{"processes": [{"pid": 1234, "name": "skill-process", "cpu": 10, "memory": 104857600, "status": "running"}]}` |

#### 4.4.3 获取网络连接

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 获取网络连接 |
| 接口标识 | connections |
| HTTP方法 | GET |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/system/{skill_id}/connections` |
| Query参数 | `protocol`: 协议（可选，如：tcp, udp） |
| 返回值 | `{"connections": [{"protocol": "tcp", "localAddress": "192.168.1.100:8080", "remoteAddress": "10.0.0.1:12345", "status": "ESTABLISHED"}]}` |

### 4.5 安全管理（security）接口

#### 4.5.1 生成加密密钥

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 生成加密密钥 |
| 接口标识 | generate-key |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/security/{skill_id}/generate-key` |
| Request Body | `{"algorithm": "RSA", "keySize": 2048}` |
| 返回值 | `{"privateKey": "-----BEGIN PRIVATE KEY-----...", "publicKey": "-----BEGIN PUBLIC KEY-----..."}` |

#### 4.5.2 加密数据

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 加密数据 |
| 接口标识 | encrypt |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/security/{skill_id}/encrypt` |
| Request Body | `{"algorithm": "AES", "key": "secret-key", "data": "plain text", "mode": "CBC"}` |
| 返回值 | `{"encryptedData": "encrypted-base64-string", "iv": "initialization-vector"}` |

#### 4.5.3 解密数据

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 解密数据 |
| 接口标识 | decrypt |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/security/{skill_id}/decrypt` |
| Request Body | `{"algorithm": "AES", "key": "secret-key", "data": "encrypted-base64-string", "mode": "CBC", "iv": "initialization-vector"}` |
| 返回值 | `{"decryptedData": "plain text"}` |

#### 4.5.4 生成数字签名

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 生成数字签名 |
| 接口标识 | sign |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/security/{skill_id}/sign` |
| Request Body | `{"algorithm": "SHA256withRSA", "privateKey": "private-key", "data": "data to sign"}` |
| 返回值 | `{"signature": "base64-encoded-signature"}` |

### 4.6 消息通信（message）接口

#### 4.6.1 发送消息

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 发送消息 |
| 接口标识 | send |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/message/{skill_id}/send` |
| Request Body | `{"topic": "topic-name", "message": "message content", "qos": 1, "retain": false}` |
| 返回值 | `{"messageId": "msg-001", "sent": true}` |

#### 4.6.2 订阅主题

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 订阅主题 |
| 接口标识 | subscribe |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/message/{skill_id}/subscribe` |
| Request Body | `{"topics": [{"topic": "topic1", "qos": 1}, {"topic": "topic2", "qos": 0}], "callbackUrl": "http://callback.example.com/messages"}` |
| 返回值 | `{"subscribed": true, "subscriptionId": "sub-001"}` |

#### 4.6.3 取消订阅

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 取消订阅 |
| 接口标识 | unsubscribe |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/message/{skill_id}/unsubscribe` |
| Request Body | `{"subscriptionId": "sub-001"}` |
| 返回值 | `{"unsubscribed": true}` |

### 4.7 数据处理（data）接口

#### 4.7.1 JSON解析

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | JSON解析 |
| 接口标识 | json-parse |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/data/{skill_id}/json-parse` |
| Request Body | `{"jsonString": '{"key": "value"}'}` |
| 返回值 | `{"parsed": {"key": "value"}}` |

#### 4.7.2 JSON序列化

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | JSON序列化 |
| 接口标识 | json-stringify |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/data/{skill_id}/json-stringify` |
| Request Body | `{"data": {"key": "value"}, "pretty": true}` |
| 返回值 | `{"stringified": "{\n  \"key\": \"value\"\n}"}` |

#### 4.7.3 数据转换

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 数据转换 |
| 接口标识 | transform |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/data/{skill_id}/transform` |
| Request Body | `{"data": {"name": "John", "age": 30}, "template": "Name: {{name}}, Age: {{age}}"}` |
| 返回值 | `{"transformed": "Name: John, Age: 30"}` |

### 4.8 定时任务（schedule）接口

#### 4.8.1 创建定时任务

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 创建定时任务 |
| 接口标识 | create |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/schedule/{skill_id}/create` |
| Request Body | `{"name": "task-name", "cron": "0 * * * *", "action": "http.get", "params": {"url": "https://api.example.com/cron"}, "enabled": true}` |
| 返回值 | `{"taskId": "task-001", "created": true}` |

#### 4.8.2 执行定时任务

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 执行定时任务 |
| 接口标识 | execute |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/schedule/{skill_id}/execute/{task_id}` |
| 返回值 | `{"executed": true, "executionId": "exec-001"}` |

#### 4.8.3 停止定时任务

| 接口信息 | 详情 |
|---------|------|
| 接口名称 | 停止定时任务 |
| 接口标识 | stop |
| HTTP方法 | POST |
| Endpoint | `{protocol}://{host}:{port}/{api_version}/schedule/{skill_id}/stop/{task_id}` |
| 返回值 | `{"stopped": true}` |

## 5. Endpoint映射与路由规则

### 5.1 路由规则

1. **静态路由**：直接映射到具体的接口实现
2. **动态路由**：包含路径参数的路由，如`/file/{skill_id}/read/{file_path}`
3. **通配符路由**：支持通配符匹配，如`/http/{skill_id}/*`匹配所有HTTP相关接口
4. **优先级规则**：更具体的路由优先匹配

### 5.2 Endpoint注册与发现

```
1. Skill注册时，将所有接口信息提交到Skill注册中心
2. Skill注册中心根据接口capability和path生成规范的endpoint
3. Agent通过Skill注册中心查询skill的endpoint信息
4. Agent使用生成的endpoint调用skill接口
```

## 6. 接口版本管理

### 6.1 版本号格式

采用语义化版本号格式：`v{major}`，如`v1`、`v2`等。

### 6.2 版本兼容策略

1. **向后兼容**：新版本接口兼容旧版本请求
2. **版本路由**：通过URL路径中的版本号路由到不同版本的接口实现
3. **废弃标记**：对于即将废弃的接口，在响应头中添加`X-Deprecated: true`标记
4. **迁移指南**：提供详细的版本迁移指南

## 7. 接口文档生成

### 7.1 自动生成

1. **基于代码注释**：从接口实现的注释中提取接口信息
2. **基于接口定义**：从SkillInterface对象中提取接口信息
3. **基于运行时信息**：从运行中的skill获取实际接口信息

### 7.2 文档格式

支持生成多种格式的接口文档：
- Swagger/OpenAPI 3.0
- Markdown
- HTML
- PDF

### 7.3 文档访问

```
{protocol}://{host}:{port}/{api_version}/docs/{skill_id}
{protocol}://{host}:{port}/{api_version}/swagger/{skill_id}
```

## 8. 接口安全策略

### 8.1 认证机制

1. **API密钥认证**：在请求头中添加`X-API-Key`字段
2. **JWT认证**：使用JSON Web Token进行认证
3. **OAuth 2.0**：支持OAuth 2.0授权码流程
4. **签名认证**：对请求进行签名验证

### 8.2 授权机制

1. **基于角色的访问控制（RBAC）**：根据角色控制接口访问权限
2. **基于策略的访问控制（PBAC）**：根据策略动态控制访问权限
3. **接口级权限控制**：对每个接口单独设置访问权限

### 8.3 传输安全

1. **HTTPS加密**：所有接口推荐使用HTTPS传输
2. **WebSocket加密**：WebSocket接口使用WSS协议
3. **数据压缩**：支持gzip压缩减少传输数据量

## 9. 接口监控与日志

### 9.1 监控指标

| 指标名称 | 描述 |
|---------|------|
| 请求次数 | 接口被调用的总次数 |
| 成功次数 | 接口调用成功的次数 |
| 失败次数 | 接口调用失败的次数 |
| 平均响应时间 | 接口的平均响应时间 |
| 最大响应时间 | 接口的最大响应时间 |
| 最小响应时间 | 接口的最小响应时间 |
| 错误率 | 接口调用的错误率 |

### 9.2 日志记录

1. **请求日志**：记录请求的详细信息，包括请求方法、URL、参数、请求体等
2. **响应日志**：记录响应的详细信息，包括状态码、响应头、响应体等
3. **错误日志**：记录接口调用过程中的错误信息
4. **性能日志**：记录接口的性能指标

## 10. 示例：完整的Skill接口定义

### 10.1 Skill接口列表

```json
{
  "skillId": "file-handler-skill",
  "name": "文件处理Skill",
  "description": "提供本地文件系统访问和操作功能",
  "version": "1.0.0",
  "category": "file",
  "interfaces": [
    {
      "interfaceId": "file-read",
      "name": "读取文件",
      "description": "读取本地文件内容",
      "method": "GET",
      "path": "/read/{file_path}",
      "capability": "file",
      "params": [
        {
          "name": "file_path",
          "type": "string",
          "in": "path",
          "required": true,
          "description": "文件路径"
        },
        {
          "name": "encoding",
          "type": "string",
          "in": "query",
          "required": false,
          "default": "utf-8",
          "description": "文件编码"
        }
      ],
      "returnType": {
        "type": "object",
        "properties": {
          "content": {
            "type": "string",
            "description": "文件内容"
          },
          "size": {
            "type": "integer",
            "description": "文件大小（字节）"
          },
          "modifiedTime": {
            "type": "integer",
            "description": "最后修改时间（时间戳）"
          }
        }
      }
    },
    {
      "interfaceId": "file-write",
      "name": "写入文件",
      "description": "向本地文件写入内容",
      "method": "POST",
      "path": "/write/{file_path}",
      "capability": "file",
      "params": [
        {
          "name": "file_path",
          "type": "string",
          "in": "path",
          "required": true,
          "description": "文件路径"
        },
        {
          "name": "content",
          "type": "string",
          "in": "body",
          "required": true,
          "description": "文件内容"
        },
        {
          "name": "overwrite",
          "type": "boolean",
          "in": "body",
          "required": false,
          "default": true,
          "description": "是否覆盖现有文件"
        },
        {
          "name": "encoding",
          "type": "string",
          "in": "body",
          "required": false,
          "default": "utf-8",
          "description": "文件编码"
        }
      ],
      "returnType": {
        "type": "object",
        "properties": {
          "success": {
            "type": "boolean",
            "description": "是否写入成功"
          },
          "size": {
            "type": "integer",
            "description": "写入的字节数"
          },
          "modifiedTime": {
            "type": "integer",
            "description": "最后修改时间（时间戳）"
          }
        }
      }
    }
  ]
}
```

### 10.2 生成的Endpoint

```
http://localhost:8080/v1/file/file-handler-skill/read/{file_path}
http://localhost:8080/v1/file/file-handler-skill/write/{file_path}
```

## 11. 总结

SuperAgent Skill接口功能分类与Endpoint映射设计遵循了以下原则：

1. **清晰的分类体系**：将skill接口功能按capability分类，便于管理和使用
2. **规范的Endpoint格式**：统一的Endpoint格式，便于agent调用
3. **标准化的接口设计**：统一的接口参数和返回值规范，便于开发和集成
4. **完整的安全策略**：提供多种认证和授权机制，确保接口安全
5. **完善的监控和日志**：便于监控接口性能和排查问题
6. **良好的扩展性**：支持新增分类和接口，便于扩展系统功能

该设计为SuperAgent系统中的skill接口提供了清晰的定义和访问方式，便于agent调用和skill开发，确保了系统的灵活性和可扩展性。