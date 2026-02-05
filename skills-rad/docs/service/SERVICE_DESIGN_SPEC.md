# 服务设计规范

## 1. 总览

ooder-agent-rad 采用注解驱动的服务开发方式，通过简洁的注解定义服务的行为和属性。本文档将详细介绍 ooder-agent-rad 的服务设计规范，包括服务类型、设计原则、实现方式等。

## 2. 服务类型

ooder-agent-rad 定义了多种服务类型，包括：

| 服务类型 | 用途 | 示例实现 |
|---------|------|--------|
| 核心服务 | 系统的核心服务，提供基础功能 | [RADEditor.java](../../src/main/java/net/ooder/editor/service/RADEditor.java) |
| 项目管理服务 | 管理项目的创建、克隆、查询等 | [ProjectManager.java](../../src/main/java/net/ooder/editor/service/ProjectManager.java) |
| 文件系统服务 | 处理文件和文件夹的操作 | [VFSService.java](../../src/main/java/net/ooder/editor/service/VFSService.java) |
| 插件服务 | 管理插件的加载和初始化 | [EditorService.java](../../src/main/java/net/ooder/editor/plugins/EditorService.java) |
| API服务 | 提供API接口，供外部系统调用 | [APIService.java](../../src/main/java/net/ooder/editor/service/APIService.java) |

## 3. 服务设计原则

### 3.1 单一职责原则

每个服务只负责一个功能领域，避免服务功能过于复杂。

**示例**：
```java
// 良好实践：RADEditor服务只负责RAD编辑器相关功能
@Controller
@RequestMapping("/RAD/")
public class RADEditor {
    // 只包含RAD编辑器相关功能
}

// 不良实践：一个服务负责多个不相关的功能
@Controller
@RequestMapping("/api/")
public class MiscService {
    // 包含用户管理、订单管理、产品管理等不相关功能
}
```

### 3.2 接口设计原则

服务接口设计应遵循以下原则：

1. **清晰的命名**：接口名称应清晰反映其功能
2. **合理的参数**：参数数量应尽量少，避免过多的参数
3. **明确的返回值**：返回值类型应明确，避免返回过于复杂的数据结构
4. **统一的响应格式**：使用ResultModel或ListResultModel作为统一响应格式

**示例**：
```java
// 良好实践：清晰的接口设计
@RequestMapping(value = "exportProject", method = RequestMethod.POST)
@ResponseBody
public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
    // 实现代码
}

// 不良实践：接口设计不清晰
@RequestMapping(value = "doSomething", method = RequestMethod.POST)
@ResponseBody
public Object doSomething(Map<String, Object> params) {
    // 实现代码
}
```

### 3.3 异常处理原则

服务应合理处理异常，返回友好的错误信息：

1. **捕获具体异常**：避免捕获所有异常
2. **记录异常日志**：使用SLF4J记录异常信息
3. **返回统一错误格式**：使用ErrorResultModel返回错误信息
4. **提供有意义的错误消息**：错误消息应清晰反映错误原因

**示例**：
```java
// 良好实践：合理的异常处理
@RequestMapping(value = "exportProject", method = RequestMethod.POST)
@ResponseBody
public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
    ResultModel<Boolean> result = new ResultModel<>();
    try {
        // 实现代码
        result.setData(true);
    } catch (IOException e) {
        logger.error("导出项目失败：{}", e.getMessage(), e);
        result = new ErrorResultModel();
        ((ErrorResultModel) result).setErrcode(JDSException.IOERROR);
        ((ErrorResultModel) result).setErrdes("导出项目失败：" + e.getMessage());
    } catch (Exception e) {
        logger.error("导出项目失败：{}", e.getMessage(), e);
        result = new ErrorResultModel();
        ((ErrorResultModel) result).setErrcode(JDSException.SYSTEMERROR);
        ((ErrorResultModel) result).setErrdes("系统错误：" + e.getMessage());
    }
    return result;
}
```

### 3.4 日志记录原则

服务应使用SLF4J记录日志，遵循以下原则：

1. **使用合适的日志级别**：
   - ERROR：记录错误信息
   - WARN：记录警告信息
   - INFO：记录重要信息
   - DEBUG：记录调试信息
   - TRACE：记录详细的跟踪信息
2. **日志内容应包含关键信息**：如方法名、参数值、返回值等
3. **使用参数化日志**：避免字符串拼接，提高性能

**示例**：
```java
// 良好实践：合理的日志记录
private static final Logger logger = LoggerFactory.getLogger(RADEditor.class);

@RequestMapping(value = "exportProject", method = RequestMethod.POST)
@ResponseBody
public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
    logger.info("开始导出项目：{}, 类型：{}, 路径：{}", projectName, type, exportPath);
    // 实现代码
    logger.info("项目导出成功：{}", projectName);
    return ResultModel.success(true);
}
```

## 4. 服务实现规范

### 4.1 服务类命名规范

服务类命名应遵循以下规范：

1. **类名应反映服务的功能**：如RADEditor、ProjectManager等
2. **使用大写字母开头**：遵循Java类命名规范
3. **避免使用缩写**：除非是广为人知的缩写，如API、VFS等

### 4.2 服务方法命名规范

服务方法命名应遵循以下规范：

1. **方法名应反映其功能**：如exportProject、openProject等
2. **使用动词开头**：如get、set、save、delete等
3. **使用驼峰命名法**：如getUserList、saveProject等

### 4.3 服务注解规范

服务应使用以下注解规范：

1. **使用@Controller标记控制器类**
2. **使用@RequestMapping映射请求路径**
3. **使用@ResponseBody标记返回结果直接作为响应体**
4. **使用@MethodChinaName为方法提供中文名称**
5. **使用@ModuleAnnotation标记模块组件**

**示例**：
```java
@Controller
@RequestMapping("/RAD/")
@MethodChinaName(cname = "OOD编辑器")
public class RADEditor {
    
    @RequestMapping(value = "exportProject", method = RequestMethod.POST)
    @MethodChinaName(cname = "导出工程")
    @ResponseBody
    public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
        // 实现代码
    }
}
```

## 5. RESTful API设计规范

### 5.1 资源命名规范

RESTful API的资源命名应遵循以下规范：

1. **使用名词而不是动词**：如/users而不是/getUsers
2. **使用复数形式表示资源集合**：如/users表示用户资源集合
3. **使用层次结构表示资源关系**：如/users/{id}/roles表示用户的角色资源

**示例**：
```
// 良好实践：RESTful API设计
GET /api/users - 获取用户列表
GET /api/users/{id} - 获取单个用户
POST /api/users - 创建用户
PUT /api/users/{id} - 更新用户
DELETE /api/users/{id} - 删除用户

// 不良实践：非RESTful API设计
GET /api/getUsers - 获取用户列表
GET /api/getUser/{id} - 获取单个用户
POST /api/addUser - 创建用户
PUT /api/updateUser/{id} - 更新用户
DELETE /api/deleteUser/{id} - 删除用户
```

### 5.2 HTTP方法使用规范

根据操作类型选择合适的HTTP方法：

| HTTP方法 | 用途 |
|---------|------|
| GET | 获取资源 |
| POST | 创建资源 |
| PUT | 更新资源 |
| DELETE | 删除资源 |
| PATCH | 部分更新资源 |

### 5.3 状态码使用规范

返回标准的HTTP状态码：

| 状态码 | 用途 |
|-------|------|
| 200 OK | 成功 |
| 201 Created | 资源创建成功 |
| 204 No Content | 资源删除成功 |
| 400 Bad Request | 请求参数错误 |
| 401 Unauthorized | 未授权 |
| 403 Forbidden | 禁止访问 |
| 404 Not Found | 资源不存在 |
| 500 Internal Server Error | 服务器内部错误 |

## 6. 服务扩展机制

### 6.1 插件化扩展

ooder-agent-rad 支持插件化扩展，插件可以动态添加或修改服务：

```java
// 插件服务示例
public class MyPluginService implements Plugin {
    
    @Override
    public void initialize(PluginContext context) {
        // 注册新的服务
        context.registerService("myService", new MyService());
        
        // 修改现有服务
        context.modifyService("existingService", this::enhanceExistingService);
    }
    
    /**
     * 增强现有服务
     * @param service 现有服务
     * @return 增强后的服务
     */
    private Object enhanceExistingService(Object service) {
        // 增强服务逻辑
        return service;
    }
}
```

### 6.2 服务拦截器

可以使用服务拦截器对服务进行增强：

```java
// 服务拦截器示例
@Component
public class ServiceInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求处理前的逻辑
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理后的逻辑
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后的逻辑
    }
}
```

## 7. 服务监控与日志

### 7.1 日志配置

ooder-agent-rad 使用SLF4J日志框架，支持多种日志实现，如Logback、Log4j等。

**日志配置示例**：
```xml
<!-- logback.xml -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>logs/ooder-agent-rad.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
    
    <logger name="net.ooder" level="DEBUG" />
</configuration>
```

### 7.2 服务监控

可以使用Spring Boot Actuator进行服务监控：

1. **添加依赖**：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. **配置监控端点**：
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
```

## 8. 服务测试规范

### 8.1 单元测试

服务应编写单元测试，测试服务的核心功能：

**示例**：
```java
@SpringBootTest
public class RADEditorTest {
    
    @Autowired
    private RADEditor radEditor;
    
    @Test
    public void testExportProject() {
        // 测试exportProject方法
        ResultModel<Boolean> result = radEditor.exportProject("testProject", "html", "/tmp");
        Assert.assertTrue(result.isSuccess());
        Assert.assertTrue(result.getData());
    }
    
    @Test
    public void testOpenProject() {
        // 测试openProject方法
        ResultModel<ProjectConfig> result = radEditor.openProject("testProject");
        Assert.assertTrue(result.isSuccess());
        Assert.assertNotNull(result.getData());
    }
}
```

### 8.2 集成测试

服务应编写集成测试，测试服务之间的协作：

**示例**：
```java
@SpringBootTest
public class ProjectIntegrationTest {
    
    @Autowired
    private RADEditor radEditor;
    
    @Autowired
    private ProjectManager projectManager;
    
    @Test
    public void testProjectWorkflow() {
        // 测试完整的项目工作流
        
        // 1. 创建项目
        ResultModel<OODProject> createResult = projectManager.createProject("testProject", null, "测试项目", ProjectDefAccess.PUBLIC, null, null);
        Assert.assertTrue(createResult.isSuccess());
        
        // 2. 打开项目
        ResultModel<ProjectConfig> openResult = radEditor.openProject("testProject");
        Assert.assertTrue(openResult.isSuccess());
        
        // 3. 导出项目
        ResultModel<Boolean> exportResult = radEditor.exportProject("testProject", "html", "/tmp");
        Assert.assertTrue(exportResult.isSuccess());
        
        // 4. 删除项目
        ResultModel<Boolean> deleteResult = projectManager.delProject("testProject");
        Assert.assertTrue(deleteResult.isSuccess());
    }
}
```

## 9. 最佳实践

### 9.1 服务开发最佳实践

1. **遵循RESTful API设计规范**：使用合适的HTTP方法和URI设计
2. **使用统一的响应格式**：使用ResultModel或ListResultModel作为统一响应格式
3. **合理处理异常**：捕获具体异常，返回友好的错误信息
4. **记录详细的日志**：使用SLF4J记录服务的运行状态和错误信息
5. **编写单元测试和集成测试**：确保服务的质量和稳定性
6. **使用缓存优化性能**：对于频繁调用的服务，考虑使用缓存
7. **异步处理耗时操作**：对于耗时的操作，使用异步处理，提高响应速度
8. **使用连接池管理数据库连接**：避免频繁创建和关闭数据库连接

### 9.2 服务部署最佳实践

1. **使用容器化部署**：使用Docker容器部署服务，提高部署效率和一致性
2. **使用负载均衡**：对于高并发服务，使用负载均衡提高系统的并发能力
3. **使用配置中心管理配置**：如Spring Cloud Config、Nacos等
4. **使用服务注册与发现**：如Eureka、Consul、Nacos等
5. **使用API网关统一管理API**：如Spring Cloud Gateway、Zuul等

## 10. 总结

ooder-agent-rad 的服务设计规范基于注解驱动开发，通过简洁的注解定义服务的行为和属性。遵循这些规范可以提高服务开发的效率和质量，确保服务的一致性和可维护性。

通过采用单一职责原则、清晰的接口设计、合理的异常处理、详细的日志记录等最佳实践，可以进一步提高服务的质量和稳定性，为用户提供高质量的服务。