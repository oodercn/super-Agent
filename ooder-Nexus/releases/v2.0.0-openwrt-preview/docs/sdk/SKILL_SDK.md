# ooderNexus 技能开�?SDK 文档

## 1. 概述

### 1.1 什么是技能？

�?ooderNexus 中，**技能（Skill�?*是一种可共享、可执行�?AI 能力单元。它可以是：

- 🤖 AI 模型推理服务
- 📊 数据处理和分�?- 🔧 系统管理工具
- 🌐 第三�?API 调用
- 📁 文件处理操作

### 1.2 技能特�?
- **去中心化**: 技能分布在网络各节点，无需中心服务�?- **即插即用**: 安装即可使用，无需复杂配置
- **版本管理**: 支持技能版本控制和自动更新
- **权限控制**: 细粒度的执行权限管理
- **跨平�?*: 支持 Windows/Linux/macOS/OpenWrt

---

## 2. 快速入�?
### 2.1 创建第一个技�?
**步骤 1: 定义技能元数据**

```json
{
  "id": "example.hello",
  "name": "Hello World",
  "version": "1.0.0",
  "description": "简单的问候技�?,
  "author": "Your Name",
  "type": "java",
  "entry": "HelloSkillHandler",
  "params": [
    {
      "name": "name",
      "type": "string",
      "description": "要问候的名字",
      "required": false,
      "default": "World"
    }
  ],
  "outputs": [
    {
      "name": "message",
      "type": "string",
      "description": "问候消�?
    }
  ]
}
```

**步骤 2: 实现技能处理器**

```java
package com.example.skills;

import net.ooder.nexus.core.skill.annotation.SkillHandler;
import net.ooder.nexus.core.skill.model.SkillRequest;
import net.ooder.nexus.core.skill.model.SkillResponse;

/**
 * Hello World 技能处理器
 */
@SkillHandler("example.hello")
public class HelloSkillHandler {

    /**
     * 执行技�?     *
     * @param request 技能请求参�?     * @return 技能执行结�?     */
    public SkillResponse execute(SkillRequest request) {
        // 获取参数
        String name = request.getParam("name", "World");
        
        // 执行业务逻辑
        String message = "Hello, " + name + "!";
        
        // 返回结果
        return SkillResponse.success()
            .withData("message", message)
            .withData("timestamp", System.currentTimeMillis());
    }
}
```

**步骤 3: 打包并发�?*

```bash
# 打包�?JAR
mvn clean package

# 发布到技能市�?# 通过 Web 控制台或 API 发布
```

---

## 3. 技能开发详�?
### 3.1 技能元数据

**完整字段说明:**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | string | �?| 技能唯一标识，格�? `作�?技能名` |
| `name` | string | �?| 技能显示名�?|
| `version` | string | �?| 语义化版本号 |
| `description` | string | �?| 技能描�?|
| `author` | string | �?| 作者信�?|
| `type` | string | �?| 技能类�? `java`/`python`/`shell` |
| `entry` | string | �?| 入口�?文件 |
| `icon` | string | �?| 技能图�?URL |
| `category` | string | �?| 分类标签 |
| `tags` | array | �?| 标签列表 |
| `params` | array | �?| 输入参数定义 |
| `outputs` | array | �?| 输出结果定义 |
| `dependencies` | array | �?| 依赖的其他技�?|
| `permissions` | array | �?| 所需权限 |

### 3.2 参数定义

```json
{
  "params": [
    {
      "name": "input",
      "type": "string",
      "description": "输入文本",
      "required": true
    },
    {
      "name": "count",
      "type": "integer",
      "description": "处理数量",
      "required": false,
      "default": 10,
      "min": 1,
      "max": 100
    },
    {
      "name": "options",
      "type": "object",
      "description": "高级选项",
      "properties": {
        "timeout": {
          "type": "integer",
          "default": 30
        },
        "retry": {
          "type": "boolean",
          "default": true
        }
      }
    }
  ]
}
```

**支持的参数类�?**

| 类型 | 说明 | 示例 |
|------|------|------|
| `string` | 字符�?| `"hello"` |
| `integer` | 整数 | `42` |
| `number` | 浮点�?| `3.14` |
| `boolean` | 布尔�?| `true`/`false` |
| `array` | 数组 | `[1, 2, 3]` |
| `object` | 对象 | `{"key": "value"}` |
| `file` | 文件 | 文件路径�?URL |

### 3.3 技能处理器

**生命周期方法:**

```java
@SkillHandler("example.advanced")
public class AdvancedSkillHandler {

    /**
     * 初始�?- 技能加载时调用
     */
    @PostConstruct
    public void init() {
        // 加载配置、初始化资源
    }

    /**
     * 执行技�?     */
    public SkillResponse execute(SkillRequest request) {
        try {
            // 1. 验证参数
            validateParams(request);
            
            // 2. 执行业务逻辑
            Object result = doWork(request);
            
            // 3. 返回成功结果
            return SkillResponse.success()
                .withData("result", result);
                
        } catch (ValidationException e) {
            // 参数验证失败
            return SkillResponse.error(ErrorCode.INVALID_PARAM, e.getMessage());
        } catch (Exception e) {
            // 执行异常
            return SkillResponse.error(ErrorCode.EXECUTION_FAILED, e.getMessage());
        }
    }

    /**
     * 销�?- 技能卸载时调用
     */
    @PreDestroy
    public void destroy() {
        // 释放资源、清理缓�?    }

    private void validateParams(SkillRequest request) {
        // 参数验证逻辑
    }

    private Object doWork(SkillRequest request) {
        // 业务逻辑
        return null;
    }
}
```

---

## 4. API 参�?
### 4.1 SkillRequest

```java
public class SkillRequest {
    
    /**
     * 获取参数（带默认值）
     */
    public <T> T getParam(String name, T defaultValue);
    
    /**
     * 获取参数（必填）
     */
    public <T> T getRequiredParam(String name) throws ValidationException;
    
    /**
     * 获取所有参�?     */
    public Map<String, Object> getParams();
    
    /**
     * 获取文件参数
     */
    public File getFileParam(String name);
    
    /**
     * 获取请求上下�?     */
    public SkillContext getContext();
}
```

### 4.2 SkillResponse

```java
public class SkillResponse {
    
    /**
     * 创建成功响应
     */
    public static SkillResponse success();
    
    /**
     * 创建错误响应
     */
    public static SkillResponse error(ErrorCode code, String message);
    
    /**
     * 添加数据
     */
    public SkillResponse withData(String key, Object value);
    
    /**
     * 添加文件
     */
    public SkillResponse withFile(String key, File file);
    
    /**
     * 设置执行时间
     */
    public SkillResponse withExecutionTime(long millis);
}
```

### 4.3 SkillContext

```java
public class SkillContext {
    
    /**
     * 获取调用者信�?     */
    public AgentInfo getCaller();
    
    /**
     * 获取执行节点信息
     */
    public AgentInfo getExecutor();
    
    /**
     * 获取技能实�?     */
    public SkillInstance getSkillInstance();
    
    /**
     * 访问存储服务
     */
    public StorageService getStorage();
    
    /**
     * 访问网络服务
     */
    public NetworkService getNetwork();
}
```

---

## 5. 示例技�?
### 5.1 文件处理技�?
```java
@SkillHandler("example.file-processor")
public class FileProcessorSkill {

    public SkillResponse execute(SkillRequest request) {
        // 获取输入文件
        File inputFile = request.getFileParam("input");
        String operation = request.getParam("operation", "convert");
        
        // 执行处理
        File outputFile = processFile(inputFile, operation);
        
        // 返回结果文件
        return SkillResponse.success()
            .withFile("output", outputFile)
            .withData("size", outputFile.length());
    }
    
    private File processFile(File input, String operation) {
        // 文件处理逻辑
        return input;
    }
}
```

### 5.2 API 调用技�?
```java
@SkillHandler("example.api-caller")
public class ApiCallerSkill {

    @Autowired
    private RestTemplate restTemplate;

    public SkillResponse execute(SkillRequest request) {
        String url = request.getRequiredParam("url");
        String method = request.getParam("method", "GET");
        Map<String, Object> headers = request.getParam("headers", new HashMap<>());
        Object body = request.getParam("body");
        
        // 构建请求
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach((k, v) -> httpHeaders.add(k, String.valueOf(v)));
        
        HttpEntity<Object> entity = new HttpEntity<>(body, httpHeaders);
        
        // 发送请�?        ResponseEntity<String> response = restTemplate.exchange(
            url, 
            HttpMethod.valueOf(method), 
            entity, 
            String.class
        );
        
        // 返回结果
        return SkillResponse.success()
            .withData("status", response.getStatusCodeValue())
            .withData("headers", response.getHeaders())
            .withData("body", response.getBody());
    }
}
```

### 5.3 AI 模型推理技�?
```java
@SkillHandler("ai.text-generator")
public class TextGeneratorSkill {

    @Autowired
    private ModelInferenceService modelService;

    public SkillResponse execute(SkillRequest request) {
        String prompt = request.getRequiredParam("prompt");
        int maxTokens = request.getParam("maxTokens", 100);
        double temperature = request.getParam("temperature", 0.7);
        
        // 构建推理参数
        InferenceParams params = InferenceParams.builder()
            .prompt(prompt)
            .maxTokens(maxTokens)
            .temperature(temperature)
            .build();
        
        // 执行推理
        InferenceResult result = modelService.infer(params);
        
        // 返回结果
        return SkillResponse.success()
            .withData("text", result.getGeneratedText())
            .withData("tokens", result.getTokenCount())
            .withData("time", result.getInferenceTime());
    }
}
```

---

## 6. 最佳实�?
### 6.1 错误处理

```java
public SkillResponse execute(SkillRequest request) {
    try {
        // 业务逻辑
        return SkillResponse.success().withData("result", result);
    } catch (ValidationException e) {
        // 参数错误 - 客户端问�?        return SkillResponse.error(ErrorCode.INVALID_PARAM, e.getMessage());
    } catch (ResourceNotFoundException e) {
        // 资源不存�?        return SkillResponse.error(ErrorCode.RESOURCE_NOT_FOUND, e.getMessage());
    } catch (ExecutionException e) {
        // 执行失败 - 服务端问�?        return SkillResponse.error(ErrorCode.EXECUTION_FAILED, e.getMessage());
    } catch (Exception e) {
        // 未知错误
        return SkillResponse.error(ErrorCode.UNKNOWN_ERROR, "执行失败: " + e.getMessage());
    }
}
```

### 6.2 性能优化

```java
@SkillHandler("example.optimized")
public class OptimizedSkill {

    // 缓存配置
    private final Cache<String, Object> cache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build();

    public SkillResponse execute(SkillRequest request) {
        String cacheKey = buildCacheKey(request);
        
        // 尝试从缓存获�?        Object cached = cache.getIfPresent(cacheKey);
        if (cached != null) {
            return SkillResponse.success()
                .withData("result", cached)
                .withData("cached", true);
        }
        
        // 执行计算
        Object result = expensiveComputation(request);
        
        // 存入缓存
        cache.put(cacheKey, result);
        
        return SkillResponse.success()
            .withData("result", result)
            .withData("cached", false);
    }
}
```

### 6.3 安全�?
```java
public SkillResponse execute(SkillRequest request) {
    SkillContext context = request.getContext();
    
    // 1. 验证调用者权�?    if (!hasPermission(context.getCaller(), "skill:execute")) {
        return SkillResponse.error(ErrorCode.PERMISSION_DENIED, "无权执行此技�?);
    }
    
    // 2. 验证参数安全�?    String userInput = request.getParam("input");
    if (!isSafeInput(userInput)) {
        return SkillResponse.error(ErrorCode.INVALID_PARAM, "输入包含非法字符");
    }
    
    // 3. 执行业务逻辑
    // ...
}
```

---

## 7. 调试与测�?
### 7.1 本地测试

```java
@Test
public void testSkill() {
    // 创建技能实�?    HelloSkillHandler skill = new HelloSkillHandler();
    
    // 构建请求
    SkillRequest request = SkillRequest.builder()
        .param("name", "Test")
        .build();
    
    // 执行技�?    SkillResponse response = skill.execute(request);
    
    // 验证结果
    assertTrue(response.isSuccess());
    assertEquals("Hello, Test!", response.getData("message"));
}
```

### 7.2 使用调试工具

1. 启动 ooderNexus
2. 访问 http://localhost:8081/console/index.html
3. 进入「技能中心」→「调试工具�?4. 选择要调试的技�?5. 输入参数并执�?6. 查看执行结果和日�?
---

## 8. 发布技�?
### 8.1 打包技�?
```bash
# 1. 编译
mvn clean compile

# 2. 打包
mvn package -DskipTests

# 3. 生成技能包
# 包含: skill.json + JAR/classes
```

### 8.2 发布到技能市�?
**方式 1: Web 控制�?*
1. 登录 ooderNexus 控制�?2. 进入「技能中心」→「发布技能�?3. 上传技能包
4. 填写技能信�?5. 点击发布

**方式 2: API 调用**

```bash
curl -X POST http://localhost:8081/api/skills \
  -H "Content-Type: multipart/form-data" \
  -F "file=@skill-package.zip" \
  -F "metadata={\"version\":\"1.0.0\"};type=application/json"
```

---

## 9. 常见问题

### Q1: 技能执行超时怎么办？

**解决:**
- 优化技能代码，减少执行时间
- 使用异步执行模式
- 增加超时时间配置

### Q2: 如何访问其他节点的技能？

**解决:**
```java
// 通过 NetworkService 调用远程技�?NetworkService network = context.getNetwork();
SkillResponse response = network.callSkill(
    "target-agent-id",
    "skill.id",
    params
);
```

### Q3: 技能如何持久化数据�?
**解决:**
```java
// 使用 StorageService
StorageService storage = context.getStorage();
storage.save("key", data);
Object data = storage.load("key");
```

---

## 10. 参考资�?
- [开发指南](../development/DEVELOPMENT_GUIDE.md)
- [调试工具](../tools/DEBUGGING_TOOLS.md)
- [API 文档](http://localhost:8081/swagger-ui.html)
- [示例技能仓库](https://github.com/oodercn/nexus-skills)

---

**开始开发你的第一个技能吧�?🚀**
