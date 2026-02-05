# 能力定义（Capability）

## 1. 设计目标

- 定义SuperAgent系统中SKILL能力的标准格式和结构
- 支持1:n Capability关系，一个SKILL可以提供多个Capability
- 确保Capability的静态定义，开发完成后不可修改
- 提供清晰的Capability分类机制（CAT）
- 支持Capability的注册、管理和验证

## 2. Capability定义原则

1. **静态定义原则**：Capability由SKILL开发者在开发时定义，开发完成后不可修改
2. **1:n关系原则**：一个SKILL可以提供多个Capability，支持细粒度的功能管理
3. **分类管理原则**：通过CAT（Capability And Type）对Capability进行分类，便于组织和发现
4. **唯一标识原则**：每个Capability具有唯一的标识符，便于Agent发现和调用
5. **安全可控原则**：Capability的访问和使用受到严格的权限控制

## 3. Capability结构

### 3.1 核心结构

| 字段名称 | 类型 | 描述 |
|----------|------|------|
| capabilityId | String | Capability的唯一标识符 |
| name | String | Capability的名称 |
| description | String | Capability的详细描述 |
| CAT | String | Capability的分类标识 |
| version | String | Capability的版本号 |
| interfaces | List<Interface> | Capability提供的接口列表 |
| permissions | List<Permission> | Capability的权限要求 |
| isStatic | Boolean | 是否为静态定义（固定为true） |

### 3.2 Interface结构

| 字段名称 | 类型 | 描述 |
|----------|------|------|
| interfaceId | String | 接口的唯一标识符 |
| name | String | 接口的名称 |
| method | String | HTTP方法（GET/POST/PUT/DELETE等） |
| path | String | 接口路径 |
| params | List<Param> | 接口参数列表 |
| returnType | Type | 返回值类型 |

### 3.3 Param结构

| 字段名称 | 类型 | 描述 |
|----------|------|------|
| name | String | 参数名称 |
| type | String | 参数类型 |
| in | String | 参数位置（path/query/body/header） |
| required | Boolean | 是否为必填参数 |
| description | String | 参数描述 |
| defaultValue | Object | 默认值 |

## 4. SKILL开发者静态定义Capability

### 4.1 定义流程

```
1. SKILL开发者在开发SKILL时，定义所需的Capability
2. 为每个Capability分配唯一的capabilityId和CAT
3. 定义Capability的接口和参数
4. 设置Capability的权限要求
5. 将Capability定义打包到SKILL中
6. SKILL注册时，将Capability信息提交到SKILL注册中心
```

### 4.2 定义示例

```json
{
  "skillId": "file-handler-skill",
  "name": "文件处理SKILL",
  "description": "提供本地文件系统访问和操作功能",
  "version": "1.0.0",
  "capabilities": [
    {
      "capabilityId": "file-read",
      "name": "读取文件",
      "description": "读取本地文件内容",
      "CAT": "file",
      "version": "1.0.0",
      "interfaces": [
        {
          "interfaceId": "file-read-interface",
          "name": "读取文件接口",
          "method": "GET",
          "path": "/read/{file_path}",
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
              "defaultValue": "utf-8",
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
              }
            }
          }
        }
      ],
      "permissions": [
        {
          "role": "user",
          "action": "read"
        }
      ],
      "isStatic": true
    }
  ]
}
```

## 5. Capability注册与管理

### 5.1 注册流程

```
1. SKILL启动时，向SKILL注册中心发送注册请求
2. 注册请求中包含SKILL的所有Capability信息
3. SKILL注册中心验证Capability的合法性
4. 验证通过后，将Capability信息存储到数据库
5. SKILL注册中心通知相关Agent有新的Capability可用
```

### 5.2 管理功能

- **查询**：支持根据skillId、CAT、capabilityId等条件查询Capability
- **验证**：验证Capability的合法性和完整性
- **版本管理**：管理Capability的版本，确保兼容性
- **权限管理**：管理Capability的访问权限

## 6. Capability验证机制

### 6.1 静态验证

- 验证Capability的结构完整性
- 验证capabilityId的唯一性
- 验证CAT的合法性
- 验证接口定义的合法性

### 6.2 动态验证

- 验证Capability的权限要求
- 验证Capability的可用性
- 验证Capability的版本兼容性

## 7. 特殊AGENT的Capability处理

### 7.1 A2UIAGENT

- 内置SKILL和AGENT一体的特殊AGENT
- 由LLM直接管理，无需单独注册
- 提供特定的Capability，如界面绘制、用户交互等

### 7.2 系统AGENT

- 提供系统级别的Capability
- 如网络管理、安全管理、系统监控等
- 具有较高的权限级别

## 8. Capability版本管理

### 8.1 版本号格式

采用语义化版本号格式：`{major}.{minor}.{patch}`

### 8.2 版本兼容性

- **向后兼容**：新版本Capability兼容旧版本请求
- **版本路由**：支持根据请求的版本号路由到不同版本的Capability实现
- **废弃标记**：对于即将废弃的Capability，添加废弃标记

## 9. 实现要点

1. **静态定义保障**：在SKILL打包和注册时进行严格验证，确保Capability是静态定义
2. **1:n关系支持**：在数据模型中支持一个SKILL对应多个Capability
3. **CAT分类机制**：实现高效的CAT分类和查询机制
4. **权限控制**：实现基于Capability的细粒度权限控制
5. **发现机制**：实现高效的Capability发现机制

