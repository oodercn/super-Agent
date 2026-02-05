# SuperAgent Skillflow服务流运行机制设计

## 1. Skillflow基本概念

### 1.1 定义

Skillflow是由多个Agent协作完成的服务流程，定义了一系列步骤，每个步骤由特定类型的Agent执行，最终完成一个完整的业务功能。

### 1.2 核心要素

| 要素 | 描述 |
|-----|------|
| 步骤（Step） | Skillflow的基本执行单元，包含执行逻辑和条件判断 |
| 条件（Condition） | 用于判断流程走向的规则 |
| 动作（Action） | 步骤中要执行的具体操作 |
| 上下文（Context） | 流程执行过程中共享的数据 |
| 状态（Status） | Skillflow的执行状态 |
| 事件（Event） | 流程执行过程中产生的通知 |

## 2. Skillflow定义格式

### 2.1 基本结构

```json
{
  "skillflowId": "flow-001",
  "name": "文件上传并分析",
  "description": "上传文件到服务器并进行内容分析",
  "version": "1.0.0",
  "createTime": "2023-01-01T00:00:00Z",
  "updateTime": "2023-01-01T00:00:00Z",
  "status": "ACTIVE",
  "steps": [
    {
      "stepId": "step-001",
      "name": "接收文件",
      "description": "接收客户端上传的文件",
      "agentType": "mcpagent",
      "action": "receiveFile",
      "params": {
        "maxSize": "10MB"
      },
      "nextStep": "step-002",
      "onError": "step-error"
    },
    {
      "stepId": "step-002",
      "name": "文件存储",
      "description": "将文件存储到本地磁盘",
      "agentType": "routeagent",
      "action": "storeFile",
      "params": {
        "storagePath": "/data/uploads"
      },
      "nextStep": "step-003",
      "onError": "step-error"
    },
    {
      "stepId": "step-003",
      "name": "内容分析",
      "description": "分析文件内容",
      "agentType": "endagent",
      "action": "analyzeContent",
      "skill": "content-analyzer-skill",
      "params": {
        "analysisType": "text"
      },
      "nextStep": "step-success",
      "onError": "step-error"
    },
    {
      "stepId": "step-success",
      "name": "成功处理",
      "description": "处理成功，返回结果",
      "agentType": "mcpagent",
      "action": "returnSuccess"
    },
    {
      "stepId": "step-error",
      "name": "错误处理",
      "description": "处理错误，返回错误信息",
      "agentType": "mcpagent",
      "action": "returnError"
    }
  ],
  "variables": {
    "fileId": "",
    "fileName": "",
    "fileSize": 0,
    "analysisResult": {}
  },
  "metadata": {
    "author": "system",
    "tags": ["file", "analysis"]
  }
}
```

### 2.2 各部分说明

#### 2.2.1 基本信息

| 字段 | 类型 | 必须 | 描述 |
|-----|------|------|------|
| skillflowId | string | 是 | 服务流唯一标识 |
| name | string | 是 | 服务流名称 |
| description | string | 否 | 服务流描述 |
| version | string | 是 | 服务流版本 |
| createTime | string | 是 | 创建时间 |
| updateTime | string | 是 | 最后更新时间 |
| status | string | 是 | 服务流状态（ACTIVE, INACTIVE, DRAFT） |

#### 2.2.2 步骤定义

| 字段 | 类型 | 必须 | 描述 |
|-----|------|------|------|
| stepId | string | 是 | 步骤唯一标识 |
| name | string | 是 | 步骤名称 |
| description | string | 否 | 步骤描述 |
| agentType | string | 是 | 执行该步骤的Agent类型（mcpagent, routeagent, endagent） |
| action | string | 是 | 步骤要执行的动作 |
| skill | string | 否 | 执行动作所需的Skill ID（仅endagent需要） |
| params | object | 否 | 动作参数 |
| nextStep | string | 否 | 下一个步骤ID |
| conditions | array | 否 | 条件列表，用于条件分支 |
| onError | string | 否 | 错误处理步骤ID |
| timeout | integer | 否 | 步骤执行超时时间（毫秒） |
| retry | integer | 否 | 步骤执行失败后的重试次数 |
| retryInterval | integer | 否 | 重试间隔时间（毫秒） |

#### 2.2.3 条件定义

```json
{
  "conditionId": "cond-001",
  "expression": "${fileSize} > 1048576",
  "nextStep": "step-large-file",
  "onFalse": "step-small-file"
}
```

#### 2.2.4 变量定义

| 字段 | 类型 | 必须 | 描述 |
|-----|------|------|------|
| variables | object | 否 | 服务流执行过程中共享的变量 |

#### 2.2.5 元数据

| 字段 | 类型 | 必须 | 描述 |
|-----|------|------|------|
| metadata | object | 否 | 服务流的元数据信息 |

## 3. Skillflow执行引擎设计

### 3.1 核心组件

| 组件 | 描述 |
|-----|------|
| 流程解析器 | 解析Skillflow定义，生成可执行的流程对象 |
| 流程调度器 | 负责流程的启动、暂停、恢复和终止 |
| 步骤执行器 | 执行具体的步骤逻辑 |
| 条件评估器 | 评估条件表达式，确定流程走向 |
| 上下文管理器 | 管理流程执行过程中的共享数据 |
| 状态管理器 | 管理流程的执行状态 |
| 异常处理器 | 处理流程执行过程中的异常 |
| 事件管理器 | 处理流程执行过程中的事件 |

### 3.2 执行引擎类图

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  流程解析器      │────▶│  流程调度器      │────▶│  步骤执行器      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                │                        │
                                ▼                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  条件评估器      │◀────│  上下文管理器      │◀────│  状态管理器      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                │                        │
                                ▼                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  异常处理器      │     │  事件管理器      │     │  监控日志管理器  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## 4. Skillflow执行流程

### 4.1 基本执行流程

```
1. 外部请求 → MCP Agent → 流程调度器
2. 流程调度器 → 流程解析器 → 解析Skillflow定义
3. 流程调度器 → 创建流程实例 → 初始化上下文
4. 流程调度器 → 步骤执行器 → 执行第一个步骤
5. 步骤执行器 → 根据agentType调用相应的Agent
6. Agent → 执行动作 → 返回结果
7. 步骤执行器 → 更新上下文 → 检查条件
8. 条件评估器 → 评估条件 → 确定下一个步骤
9. 流程调度器 → 执行下一个步骤 → 重复步骤5-8
10. 流程调度器 → 流程结束 → 返回结果
```

### 4.2 详细执行流程

#### 4.2.1 流程启动

```
1. 接收外部请求，包含skillflowId和请求参数
2. 验证请求合法性，包括权限验证和参数验证
3. 解析Skillflow定义，生成流程对象
4. 创建流程实例，分配唯一的instanceId
5. 初始化流程上下文，设置初始变量
6. 将流程状态设置为RUNNING
7. 发布流程启动事件
8. 调度执行第一个步骤
```

#### 4.2.2 步骤执行

```
1. 根据步骤定义，确定执行该步骤的Agent类型
2. 查找可用的Agent实例
3. 准备执行参数，包括步骤参数和上下文数据
4. 调用Agent的processRequest方法执行动作
5. 等待Agent返回执行结果
6. 处理执行结果：
   a. 如果执行成功，更新上下文，继续执行下一步
   b. 如果执行失败，根据重试配置决定是否重试
   c. 如果重试次数用尽或不需要重试，执行错误处理步骤
7. 发布步骤执行事件
8. 更新步骤状态和流程状态
```

#### 4.2.3 条件分支

```
1. 步骤执行成功后，检查是否存在条件分支
2. 如果存在条件分支，评估每个条件的表达式
3. 根据评估结果，确定下一个要执行的步骤
4. 如果有多个条件满足，根据优先级选择第一个
5. 如果没有条件满足，执行默认的nextStep
6. 发布条件评估事件
```

#### 4.2.4 流程结束

```
1. 执行到没有nextStep的步骤
2. 或执行到明确的结束步骤
3. 或发生不可恢复的错误
4. 更新流程状态为COMPLETED或FAILED
5. 收集执行结果，生成最终响应
6. 发布流程结束事件
7. 清理流程资源
8. 返回最终结果给请求方
```

## 5. Skillflow状态管理

### 5.1 流程状态

| 状态 | 描述 |
|-----|------|
| INIT | 流程已初始化，尚未开始执行 |
| RUNNING | 流程正在执行 |
| PAUSED | 流程已暂停 |
| COMPLETED | 流程执行完成，成功 |
| FAILED | 流程执行失败 |
| TERMINATED | 流程被终止 |

### 5.2 步骤状态

| 状态 | 描述 |
|-----|------|
| PENDING | 步骤等待执行 |
| RUNNING | 步骤正在执行 |
| COMPLETED | 步骤执行完成，成功 |
| FAILED | 步骤执行失败 |
| SKIPPED | 步骤被跳过 |

### 5.3 状态转换图

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    INIT         │────▶│    RUNNING      │────▶│   COMPLETED     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
        │                        │                        ▲
        │                        ▼                        │
        │                  ┌─────────────────┐            │
        └──────────────────│    PAUSED       │────────────┘
                           └─────────────────┘
                                │                        
                                ▼                        
                           ┌─────────────────┐     ┌─────────────────┐
                           │    FAILED       │◀────│   TERMINATED    │
                           └─────────────────┘     └─────────────────┘
```

## 6. Skillflow上下文管理

### 6.1 上下文数据结构

```json
{
  "instanceId": "flow-inst-001",
  "skillflowId": "flow-001",
  "variables": {
    "fileId": "file-123",
    "fileName": "example.txt",
    "fileSize": 1024,
    "analysisResult": {
      "sentiment": "positive",
      "keywords": ["example", "text"]
    }
  },
  "executionInfo": {
    "startTime": "2023-01-01T00:00:00Z",
    "currentStep": "step-003",
    "currentAgent": "endagent-001",
    "stepCount": 3,
    "retryCount": 0
  },
  "requestInfo": {
    "requestId": "req-001",
    "userId": "user-001",
    "clientIp": "192.168.1.100",
    "requestTime": "2023-01-01T00:00:00Z"
  }
}
```

### 6.2 上下文传递机制

1. **同步传递**：在步骤执行过程中，上下文数据通过方法参数直接传递给Agent
2. **异步传递**：对于异步执行的步骤，上下文数据通过消息队列传递
3. **共享存储**：上下文数据存储在共享存储中，Agent通过instanceId访问

### 6.3 上下文更新机制

1. **直接更新**：Agent执行结果直接更新上下文变量
2. **表达式更新**：通过表达式更新上下文变量，如 `${fileSize} = ${fileSize} + 100`
3. **合并更新**：将Agent返回的结果与现有上下文合并

## 7. Skillflow异常处理

### 7.1 异常类型

| 异常类型 | 描述 | 处理方式 |
|---------|------|--------|
| 参数异常 | 请求参数不合法 | 返回错误响应，流程终止 |
| 权限异常 | 没有执行权限 | 返回错误响应，流程终止 |
| Agent不可用 | 找不到可用的Agent | 重试或执行错误处理步骤 |
| 执行超时 | 步骤执行超过超时时间 | 重试或执行错误处理步骤 |
| 执行失败 | Agent执行动作失败 | 重试或执行错误处理步骤 |
| 系统异常 | 系统内部错误 | 执行错误处理步骤，流程终止 |

### 7.2 异常处理策略

1. **重试机制**：
   - 配置重试次数和重试间隔
   - 支持指数退避重试
   - 支持特定异常类型的重试

2. **错误处理步骤**：
   - 每个步骤可以配置onError指定错误处理步骤
   - 错误处理步骤可以记录错误信息、通知相关人员等
   - 支持嵌套错误处理

3. **异常恢复**：
   - 支持从指定步骤恢复流程
   - 支持流程回滚
   - 支持补偿操作

4. **异常通知**：
   - 发布异常事件
   - 发送告警通知
   - 记录详细日志

### 7.3 错误处理流程

```
1. 步骤执行过程中发生异常
2. 捕获异常，记录异常信息
3. 检查是否需要重试：
   a. 如果需要重试，等待重试间隔后重新执行该步骤
   b. 如果重试次数用尽，执行错误处理步骤
4. 执行错误处理步骤：
   a. 更新上下文，记录错误信息
   b. 执行错误处理逻辑
   c. 确定流程走向
5. 发布异常事件
6. 更新流程状态
```

## 8. Skillflow监控与日志

### 8.1 监控指标

| 指标名称 | 描述 |
|---------|------|
| 流程执行次数 | 流程被执行的总次数 |
| 流程成功次数 | 流程执行成功的次数 |
| 流程失败次数 | 流程执行失败的次数 |
| 平均执行时间 | 流程的平均执行时间 |
| 最大执行时间 | 流程的最大执行时间 |
| 最小执行时间 | 流程的最小执行时间 |
| 失败率 | 流程执行的失败率 |
| 步骤执行次数 | 各步骤被执行的次数 |
| 步骤平均执行时间 | 各步骤的平均执行时间 |
| 步骤失败率 | 各步骤的失败率 |

### 8.2 日志记录

| 日志类型 | 描述 | 记录时机 |
|---------|------|--------|
| 流程日志 | 记录流程的整体执行情况 | 流程启动、结束、状态变化时 |
| 步骤日志 | 记录每个步骤的执行情况 | 步骤开始、结束、状态变化时 |
| 异常日志 | 记录执行过程中的异常 | 发生异常时 |
| 上下文日志 | 记录上下文的变化 | 上下文更新时 |
| 事件日志 | 记录产生的事件 | 发布事件时 |

### 8.3 日志格式

```json
{
  "timestamp": "2023-01-01T00:00:00Z",
  "logLevel": "INFO",
  "logType": "FLOW_EXECUTION",
  "instanceId": "flow-inst-001",
  "skillflowId": "flow-001",
  "stepId": "step-001",
  "stepName": "接收文件",
  "agentType": "mcpagent",
  "agentId": "mcp-001",
  "eventType": "STEP_STARTED",
  "message": "步骤执行开始",
  "context": {
    "fileId": "file-123",
    "fileName": "example.txt"
  },
  "executionTime": 100,
  "status": "RUNNING",
  "error": null
}
```

## 9. Skillflow事件系统

### 9.1 事件类型

| 事件类型 | 描述 |
|---------|------|
| FLOW_STARTED | 流程启动 |
| FLOW_COMPLETED | 流程完成 |
| FLOW_FAILED | 流程失败 |
| FLOW_PAUSED | 流程暂停 |
| FLOW_RESUMED | 流程恢复 |
| FLOW_TERMINATED | 流程终止 |
| STEP_STARTED | 步骤开始执行 |
| STEP_COMPLETED | 步骤执行完成 |
| STEP_FAILED | 步骤执行失败 |
| STEP_SKIPPED | 步骤被跳过 |
| CONDITION_EVALUATED | 条件评估完成 |
| EXCEPTION_THROWN | 发生异常 |
| CONTEXT_UPDATED | 上下文更新 |

### 9.2 事件发布与订阅

1. **事件发布**：
   - 流程执行过程中，在关键节点发布事件
   - 事件包含事件类型、流程信息、步骤信息、上下文数据等
   - 支持同步和异步发布

2. **事件订阅**：
   - 支持基于事件类型的订阅
   - 支持基于skillflowId的订阅
   - 支持基于instanceId的订阅
   - 支持持久化订阅和非持久化订阅

3. **事件处理**：
   - 订阅者接收到事件后，执行相应的处理逻辑
   - 支持事件过滤和转换
   - 支持事件链处理

## 10. Skillflow实例管理

### 10.1 实例创建

```
1. 接收外部请求，包含skillflowId和请求参数
2. 验证请求合法性
3. 创建流程实例，分配唯一的instanceId
4. 初始化流程上下文
5. 设置流程状态为RUNNING
6. 发布FLOW_STARTED事件
```

### 10.2 实例查询

```
1. 支持按skillflowId查询实例列表
2. 支持按状态查询实例列表
3. 支持按时间范围查询实例列表
4. 支持查询单个实例的详细信息
5. 支持查询实例的执行历史
```

### 10.3 实例操作

| 操作 | 描述 | 条件 |
|-----|------|------|
| 启动 | 启动一个新的流程实例 | 流程状态为INIT |
| 暂停 | 暂停正在执行的流程 | 流程状态为RUNNING |
| 恢复 | 恢复暂停的流程 | 流程状态为PAUSED |
| 终止 | 终止正在执行的流程 | 流程状态为RUNNING或PAUSED |
| 重试 | 重试失败的流程 | 流程状态为FAILED |
| 回滚 | 回滚流程到指定步骤 | 流程状态为RUNNING或FAILED |
| 删除 | 删除流程实例 | 流程状态为COMPLETED或FAILED |

### 10.4 实例存储

1. **内存存储**：
   - 活跃的流程实例存储在内存中，提高执行效率
   - 支持设置最大内存实例数，超过则持久化到磁盘

2. **持久化存储**：
   - 所有流程实例都持久化到数据库中，确保数据安全
   - 支持定期清理过期的实例数据
   - 支持实例数据的备份和恢复

## 11. Skillflow示例

### 11.1 示例：文件上传并分析

#### 11.1.1 Skillflow定义

```json
{
  "skillflowId": "file-upload-analyze-flow",
  "name": "文件上传并分析",
  "description": "上传文件到服务器并进行内容分析",
  "version": "1.0.0",
  "steps": [
    {
      "stepId": "receive-file",
      "name": "接收文件",
      "description": "接收客户端上传的文件",
      "agentType": "mcpagent",
      "action": "receiveFile",
      "params": {
        "maxSize": "10MB"
      },
      "nextStep": "store-file",
      "onError": "handle-error"
    },
    {
      "stepId": "store-file",
      "name": "文件存储",
      "description": "将文件存储到本地磁盘",
      "agentType": "routeagent",
      "action": "storeFile",
      "params": {
        "storagePath": "/data/uploads"
      },
      "nextStep": "analyze-content",
      "onError": "handle-error"
    },
    {
      "stepId": "analyze-content",
      "name": "内容分析",
      "description": "分析文件内容",
      "agentType": "endagent",
      "action": "analyzeContent",
      "skill": "content-analyzer-skill",
      "params": {
        "analysisType": "text"
      },
      "conditions": [
        {
          "conditionId": "large-file",
          "expression": "${fileSize} > 1048576",
          "nextStep": "notify-large-file",
          "priority": 1
        }
      ],
      "nextStep": "return-success",
      "onError": "handle-error"
    },
    {
      "stepId": "notify-large-file",
      "name": "通知大文件",
      "description": "通知管理员有大文件上传",
      "agentType": "mcpagent",
      "action": "sendNotification",
      "params": {
        "recipient": "admin@example.com",
        "subject": "大文件上传通知",
        "content": "有一个大文件上传：${fileName}，大小：${fileSize}字节"
      },
      "nextStep": "return-success"
    },
    {
      "stepId": "return-success",
      "name": "成功返回",
      "description": "返回成功结果",
      "agentType": "mcpagent",
      "action": "returnSuccess",
      "params": {
        "resultPath": "analysisResult"
      }
    },
    {
      "stepId": "handle-error",
      "name": "错误处理",
      "description": "处理执行过程中的错误",
      "agentType": "mcpagent",
      "action": "returnError",
      "params": {
        "errorPath": "errorMessage"
      }
    }
  ],
  "variables": {
    "fileId": "",
    "fileName": "",
    "fileSize": 0,
    "filePath": "",
    "analysisResult": {},
    "errorMessage": ""
  }
}
```

#### 11.1.2 执行流程

```
1. 外部请求上传文件 → MCP Agent
2. MCP Agent → 流程调度器 → 启动流程实例
3. 流程调度器 → 执行第一个步骤 "receive-file"
4. MCP Agent → 接收文件 → 返回文件信息
5. 流程调度器 → 更新上下文 → fileId, fileName, fileSize
6. 流程调度器 → 执行第二个步骤 "store-file"
7. Route Agent → 存储文件 → 返回存储路径
8. 流程调度器 → 更新上下文 → filePath
9. 流程调度器 → 执行第三个步骤 "analyze-content"
10. End Agent → 调用 "content-analyzer-skill" → 分析文件内容
11. End Agent → 返回分析结果
12. 流程调度器 → 更新上下文 → analysisResult
13. 条件评估器 → 评估条件 "large-file"
14. 如果fileSize > 1MB → 执行 "notify-large-file" 步骤
15. MCP Agent → 发送通知 → 返回成功
16. 流程调度器 → 执行 "return-success" 步骤
17. MCP Agent → 返回最终结果 → 外部请求方
18. 流程调度器 → 设置流程状态为COMPLETED
19. 发布FLOW_COMPLETED事件
```

## 12. 性能优化

### 12.1 并行执行

- 支持步骤的并行执行
- 支持设置并行度
- 支持并行结果的合并

### 12.2 异步执行

- 支持步骤的异步执行
- 支持异步结果的回调
- 支持异步执行的超时控制

### 12.3 缓存机制

- 缓存频繁访问的数据
- 缓存Agent执行结果
- 支持缓存失效策略

### 12.4 负载均衡

- 支持Agent的负载均衡
- 支持基于权重的负载均衡
- 支持基于性能的负载均衡

### 12.5 资源管理

- 限制每个流程实例的资源使用
- 支持资源隔离
- 支持资源动态调整

## 13. 总结

SuperAgent Skillflow服务流运行机制设计采用了模块化、可扩展的架构，支持复杂的业务流程编排和执行。主要特点包括：

1. **灵活的流程定义**：支持可视化定义和JSON定义，便于开发和维护
2. **强大的执行引擎**：支持同步和异步执行，支持条件分支和循环
3. **完善的状态管理**：实时跟踪流程和步骤状态，支持流程暂停、恢复和终止
4. **健壮的异常处理**：支持重试机制和错误处理步骤，提高流程的可靠性
5. **全面的监控与日志**：详细记录流程执行过程，便于问题定位和性能分析
6. **丰富的事件系统**：支持事件发布和订阅，便于扩展和集成
7. **高效的实例管理**：支持实例的创建、查询和操作，便于管理和监控
8. **良好的性能优化**：支持并行执行、异步执行和缓存机制，提高系统性能

该设计为SuperAgent系统提供了强大的服务流编排和执行能力，能够满足各种复杂业务场景的需求，为构建灵活、可靠、高效的应用奠定了基础。