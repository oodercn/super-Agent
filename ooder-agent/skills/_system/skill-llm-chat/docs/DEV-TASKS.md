# skill-llm-chat 协作技能开发任务

> **技能类型**: 协作技能  
> **工程路径**: skills/_system/skill-llm-chat/  
> **优先级**: P0  
> **开发周期**: 2周

---

## 一、技能定位

skill-llm-chat 是协作场景技能，提供LLM对话能力。它不是核心功能，而是通过场景机制与 skill-scene 协作的增强功能。

### 1.1 职责边界

| 由本技能负责 | 由 skill-scene 负责 |
|-------------|-------------------|
| LLM对话界面 | 能力注册管理 |
| 流式输出处理 | 能力调用路由 |
| 对话历史管理 | 审计日志记录 |
| 模型切换 | 权限控制 |

### 1.2 依赖关系

```yaml
dependencies:
  - skillId: skill-scene
    capability: capability-register
    usage: 注册LLM对话能力
    required: true
  
  - skillId: skill-scene
    capability: capability-invoke
    usage: 调用场景内其他能力
    required: true
  
  - skillId: skill-scene
    capability: scene-context
    usage: 获取场景上下文
    required: true
```

---

## 二、能力定义

### 2.1 skill.yaml

```yaml
apiVersion: skill.ooder.net/v1
kind: Skill

metadata:
  id: skill-llm-chat
  name: LLM对话技能
  version: 1.0.0
  description: 提供LLM对话能力，支持多Provider、流式输出、Function Calling
  author: ooder
  category: intelligence
  tags:
    - llm
    - chat
    - ai

spec:
  form: PROVIDER
  visibility: PUBLIC
  sceneTypes:
    - all
  
  capabilities:
    - id: llm-chat
      name: LLM对话
      description: 与LLM进行对话交互
      category: intelligence
      connectorType: HTTP
      parameters:
        - name: prompt
          type: string
          required: true
          description: 用户输入
        - name: systemPrompt
          type: string
          required: false
          description: 系统提示词
        - name: model
          type: string
          required: false
          description: 指定模型
        - name: stream
          type: boolean
          required: false
          defaultValue: true
          description: 是否流式输出
      returns:
        type: ChatResponse
        properties:
          - name: response
            type: string
          - name: tokenUsage
            type: object
          - name: model
            type: string

    - id: llm-chat-stream
      name: LLM流式对话
      description: SSE流式对话
      category: intelligence
      connectorType: WEBSOCKET

    - id: llm-translate
      name: 文本翻译
      description: 翻译文本
      category: intelligence
      connectorType: HTTP

    - id: llm-summarize
      name: 文本摘要
      description: 生成文本摘要
      category: intelligence
      connectorType: HTTP

    - id: llm-models
      name: 模型列表
      description: 获取可用模型列表
      category: management
      connectorType: INTERNAL

    - id: llm-set-model
      name: 切换模型
      description: 切换当前使用的模型
      category: management
      connectorType: INTERNAL

  ui:
    pages:
      - path: /llm-chat/main.html
        title: LLM对话
        icon: ri-robot-line
    menu:
      - title: LLM对话
        icon: ri-robot-line
        path: /llm-chat/main.html
        order: 100
```

---

## 三、开发任务

### Phase 1: 核心功能（Week 1）

| 任务ID | 任务名称 | 工作量 | 优先级 |
|--------|----------|--------|--------|
| LLM-001 | 技能骨架创建 | 1d | P0 |
| LLM-002 | 对话能力实现 | 2d | P0 |
| LLM-003 | 流式输出实现 | 2d | P0 |

#### LLM-001: 技能骨架创建

**产出物**:
```
skill-llm-chat/
├── pom.xml
├── skill.yaml
├── skill-index-entry.yaml
└── src/main/
    ├── java/net/ooder/skill/llm/chat/
    │   ├── LlmChatSkill.java
    │   ├── controller/
    │   │   └── LlmChatController.java
    │   ├── service/
    │   │   ├── LlmChatService.java
    │   │   └── impl/
    │   │       └── LlmChatServiceImpl.java
    │   └── model/
    │       ├── ChatRequest.java
    │       └── ChatResponse.java
    └── resources/
        └── static/console/
            └── pages/llm-chat/
                ├── main.html
                └── main.js
```

**验收标准**:
- [ ] 技能可安装
- [ ] 能力可注册到场景
- [ ] 基础API可调用

#### LLM-002: 对话能力实现

**产出物**:
```
src/main/java/net/ooder/skill/llm/chat/
├── provider/
│   ├── LlmProvider.java
│   ├── DeepSeekProvider.java
│   └── QianfanProvider.java
└── service/
    └── impl/
        └── LlmChatServiceImpl.java
```

**验收标准**:
- [ ] 支持DeepSeek Provider
- [ ] 支持百度千帆 Provider
- [ ] 支持模型切换
- [ ] 支持系统提示词

#### LLM-003: 流式输出实现

**产出物**:
```
src/main/java/net/ooder/skill/llm/chat/
├── controller/
│   └── LlmStreamController.java
└── service/
    └── impl/
        └── LlmStreamServiceImpl.java
```

**验收标准**:
- [ ] 支持SSE流式输出
- [ ] 支持中断输出
- [ ] 流式状态管理

### Phase 2: UI开发（Week 2）

| 任务ID | 任务名称 | 工作量 | 优先级 |
|--------|----------|--------|--------|
| LLM-004 | 对话界面开发 | 3d | P0 |
| LLM-005 | 设置界面开发 | 1d | P1 |
| LLM-006 | 历史记录功能 | 1d | P1 |

#### LLM-004: 对话界面开发

**参考设计**: `temp/ooder-Nexus/src/main/resources/static/console/pages/llm/llm-chat.html`

**产出物**:
```
src/main/resources/static/console/pages/llm-chat/
├── main.html
├── main.js
└── main.css
```

**功能要求**:
- [ ] 对话消息列表
- [ ] 输入框和发送按钮
- [ ] 模型选择器
- [ ] Token统计显示
- [ ] 快捷操作按钮
- [ ] 流式输出显示

---

## 四、API端点

| 方法 | 端点 | 说明 |
|------|------|------|
| POST | /api/llm/chat | 同步对话 |
| POST | /api/llm/chat/stream | 流式对话(SSE) |
| POST | /api/llm/translate | 翻译 |
| POST | /api/llm/summarize | 摘要 |
| POST | /api/llm/models | 获取模型列表 |
| PUT | /api/llm/model | 切换模型 |
| GET | /api/llm/providers | 获取Provider列表 |
| GET | /api/llm/history | 获取对话历史 |
| DELETE | /api/llm/history | 清空对话历史 |

---

## 五、与 skill-scene 集成

### 5.1 能力注册

```java
@Component
public class LlmChatSkillLifecycle implements SceneSkillLifecycle {
    
    @Override
    public void onInstall(SceneContext context) {
        context.registerCapability(
            Capability.builder()
                .id("llm-chat")
                .name("LLM对话")
                .type(CapabilityType.AI)
                .connectorType(ConnectorType.HTTP)
                .build()
        );
    }
}
```

### 5.2 能力调用

```java
@Service
public class LlmChatServiceImpl implements LlmChatService {
    
    @Autowired
    private SceneContext sceneContext;
    
    @Override
    public ChatResponse chat(ChatRequest request) {
        Object result = sceneContext.invokeCapability("llm-chat", request.toMap());
        return ChatResponse.from(result);
    }
}
```

---

*文档生成时间: 2026-03-15*
