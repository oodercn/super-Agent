# 服务注解详解

## 1. 总览

ooder-agent-rad 采用注解驱动的服务开发方式，通过简洁的注解定义服务的行为和属性。服务注解是 ooder-agent-rad 注解体系的重要组成部分，用于定义服务的路由、参数、返回值等。

## 2. 服务注解体系

ooder-agent-rad 设计了一套完整的服务注解体系，包括：

| 注解类型 | 用途 | 核心注解 |
|---------|------|---------|
| 控制器注解 | 标记控制器类 | @Controller |
| 请求映射注解 | 映射请求路径 | @RequestMapping, @GetMapping, @PostMapping |
| 响应体注解 | 标记方法返回结果直接作为响应体 | @ResponseBody |
| 方法名称注解 | 为方法提供中文名称 | @MethodChinaName |
| 模块注解 | 标记模块组件 | @ModuleAnnotation |
| 动作注解 | 定义动作行为 | @ActionAnnotation |

## 3. 核心服务注解详解

### 3.1 @Controller

**用途**：标记类为控制器，用于处理HTTP请求

**核心属性**：
- 无特殊属性，主要用于标记类

**示例**：
```java
@Controller
@RequestMapping("/rad/")
public class Main {
    // 控制器实现
}
```

### 3.2 @RequestMapping

**用途**：映射HTTP请求到控制器方法

**核心属性**：
- `value`：请求路径
- `method`：HTTP方法（GET/POST/PUT/DELETE等）
- `produces`：响应内容类型
- `consumes`：请求内容类型

**示例**：
```java
@RequestMapping(value = {"/RAD/"}, method = RequestMethod.POST)
public class RADEditor {
    // 服务实现
}
```

### 3.3 @ResponseBody

**用途**：标记方法返回结果直接作为HTTP响应体

**核心属性**：
- 无特殊属性，主要用于标记方法

**示例**：
```java
@RequestMapping("Main")
@LayoutViewAnnotation
@ModuleAnnotation
@ResponseBody
public ResultModel<FramePanel> getMain() {
    // 实现代码
}
```

### 3.4 @MethodChinaName

**用途**：为方法提供中文名称，便于生成文档和界面显示

**核心属性**：
- `cname`：方法中文名称

**示例**：
```java
@Controller
@RequestMapping(value = {"/RAD/"})
@MethodChinaName(cname = "OOD编辑器")
public class RADEditor {
    // 服务实现
}
```

### 3.5 @ActionAnnotation

**用途**：定义动作行为，用于菜单和工具栏

**核心属性**：
- `name`：动作名称
- `caption`：动作标题
- `icon`：动作图标
- `roles`：允许执行该动作的角色
- `enabled`：动作是否启用

**示例**：
```java
@ActionAnnotation(name = "saveDesign", caption = "保存设计", icon = "ri-save-line")
@RequestMapping(method = RequestMethod.POST, value = "save")
@ResponseBody
public ResultModel<Boolean> saveDesign(@RequestBody DesignData data) {
    // 保存设计逻辑
    return ResultModel.success(true);
}
```

### 3.6 @ModuleAnnotation

**用途**：标记方法返回模块组件

**核心属性**：
- `caption`：模块标题
- `panelType`：面板类型

**示例**：
```java
@RequestMapping(method = RequestMethod.POST, value = "embed")
@ModuleAnnotation(caption = "OOD设计器", panelType = PanelType.panel)
@PanelViewAnnotation()
@ResponseBody
public ModuleComponent embedDesigner() {
    // 实现代码
}
```

## 4. 服务注解使用示例

### 4.1 完整服务控制器示例

```java
package net.ooder.editor.service;

/**
 * RAD编辑器服务类
 * 负责项目导出、打开项目、获取项目配置等功能
 */
@Controller
@RequestMapping(value = {"/RAD/"})
@MethodChinaName(cname = "OOD编辑器")
public class RADEditor {
    
    /**
     * 导出项目
     * 将设计好的项目导出为可部署的代码
     * 
     * @param projectName 项目名称
     * @param type 导出类型
     * @param exportPath 导出路径
     * @return 导出结果
     */
    @RequestMapping(value = {"exportProject"}, method = {RequestMethod.POST})
    @MethodChinaName(cname = "导出工程")
    @ResponseBody
    public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
        ResultModel<Boolean> result = new ResultModel<>();
        // 实现代码
        return result;
    }
    
    /**
     * 打开项目
     * 打开已存在的项目
     * 
     * @param projectName 项目名称
     * @return 项目配置
     */
    @RequestMapping(value = {"openProject"}, method = {RequestMethod.POST})
    @MethodChinaName(cname = "打开工程")
    @ResponseBody
    public ResultModel<ProjectConfig> openProject(String projectName) {
        ResultModel<ProjectConfig> result = new ResultModel<>();
        // 实现代码
        return result;
    }
}
```

### 4.2 工程管理服务示例

```java
package net.ooder.editor.service;

/**
 * 工程管理控制器类
 * 负责工程的创建、克隆、查询、版本管理等核心功能
 */
@Controller
@RequestMapping(value = {"/admin/"})
@MethodChinaName(cname = "RAD工程管理")
public class ProjectManager {
    
    /**
     * 创建工程
     * 支持从模板克隆或全新创建工程
     * 
     * @param newProjectName 新工程名称
     * @param projectName 原始工程名称
     * @param desc 工程描述
     * @param projectType 工程访问类型
     * @param tempName 模板名称
     * @param url 工程公共服务器URL
     * @return 创建结果
     */
    @RequestMapping(value = {"createProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    @MethodChinaName(cname = "创建工程")
    public @ResponseBody
    ResultModel<OODProject> createProject(String newProjectName, String projectName, String desc, ProjectDefAccess projectType, String tempName, String url) {
        ResultModel<OODProject> result = new ResultModel<>();
        // 实现代码
        return result;
    }
    
    /**
     * 获取工程列表
     * 
     * @param pattern 搜索模式
     * @return 项目列表
     */
    @RequestMapping(value = {"getProjectList"}, method = {RequestMethod.GET, RequestMethod.POST})
    @MethodChinaName(cname = "获取工程列表")
    public @ResponseBody
    ListResultModel<List<OODProject>> getProjectList(String pattern) {
        ListResultModel<List<OODProject>> result = new ListResultModel<>();
        // 实现代码
        return result;
    }
}
```

## 5. 服务注解与RESTful API设计

ooder-agent-rad 服务注解支持 RESTful API 设计，通过 @RequestMapping 注解的 method 属性可以指定 HTTP 方法，实现 RESTful 风格的 API。

### 5.1 RESTful API 示例

```java
@Controller
@RequestMapping("/api/users")
public class UserController {
    
    // GET /api/users - 获取用户列表
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers() {
        // 实现代码
    }
    
    // GET /api/users/{id} - 获取单个用户
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        // 实现代码
    }
    
    // POST /api/users - 创建用户
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user) {
        // 实现代码
    }
    
    // PUT /api/users/{id} - 更新用户
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        // 实现代码
    }
    
    // DELETE /api/users/{id} - 删除用户
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUser(@PathVariable Long id) {
        // 实现代码
    }
}
```

### 5.2 RESTful API 最佳实践

1. **使用合适的 HTTP 方法**：
   - GET：获取资源
   - POST：创建资源
   - PUT：更新资源
   - DELETE：删除资源
   - PATCH：部分更新资源

2. **使用有意义的 URI**：
   - 使用名词而不是动词
   - 使用复数形式表示资源集合
   - 使用层次结构表示资源关系

3. **使用标准的状态码**：
   - 200 OK：成功
   - 201 Created：资源创建成功
   - 204 No Content：资源删除成功
   - 400 Bad Request：请求参数错误
   - 401 Unauthorized：未授权
   - 403 Forbidden：禁止访问
   - 404 Not Found：资源不存在
   - 500 Internal Server Error：服务器内部错误

## 6. 服务注解的处理机制

ooder-agent-rad 的服务注解处理机制基于 Spring Boot 的注解处理框架，主要包括以下步骤：

1. **编译时处理**：Java 编译器在编译时处理注解，生成注解元数据
2. **运行时扫描**：Spring Boot 在启动时扫描指定包下的服务注解
3. **注解解析**：解析服务注解元数据，生成相应的 HandlerMapping
4. **请求处理**：当 HTTP 请求到达时，根据 HandlerMapping 找到对应的处理方法
5. **方法调用**：调用处理方法，处理请求
6. **响应生成**：将方法返回结果转换为 HTTP 响应

## 7. 服务注解最佳实践

### 7.1 注解使用原则

1. **单一职责**：每个服务只负责一个功能领域
2. **清晰的 URI 设计**：使用 RESTful 风格的 URI
3. **合理的请求方法**：根据操作类型选择合适的 HTTP 方法
4. **统一的响应格式**：使用 ResultModel 或 ListResultModel 作为统一响应格式
5. **适当的注解组合**：根据需求合理组合不同的注解

### 7.2 常见使用场景

| 场景 | 推荐注解组合 |
|------|------------|
| 控制器类 | @Controller + @RequestMapping |
| GET 请求 | @RequestMapping(method = RequestMethod.GET) + @ResponseBody |
| POST 请求 | @RequestMapping(method = RequestMethod.POST) + @ResponseBody |
| RESTful API | @RequestMapping(method = RequestMethod.*) + @ResponseBody |
| 带中文名称的方法 | @MethodChinaName + @RequestMapping + @ResponseBody |

### 7.3 性能考虑

- 服务注解主要在启动时处理，运行时性能影响较小
- 避免在服务方法中使用过于复杂的注解组合
- 合理使用缓存，减少重复处理请求
- 对于频繁调用的服务，考虑使用异步处理

## 8. 服务注解与传统服务开发的对比

| 特性 | 传统服务开发 | 注解驱动服务开发 |
|------|-------------|----------------|
| 配置方式 | XML配置或硬编码 | 简洁的注解配置 |
| 可读性 | 较差，配置与代码分离 | 高，配置与代码紧密结合 |
| 维护成本 | 高，需要维护大量配置文件 | 低，注解驱动开发 |
| 开发效率 | 低，需要编写大量配置 | 高，注解简洁明了 |
| 扩展性 | 差，需要修改大量配置 | 好，注解支持继承和扩展 |
| RESTful 支持 | 复杂，需要手动配置 | 简单，通过注解即可实现 |

## 9. 总结

ooder-agent-rad 的服务注解体系为开发者提供了一种简洁、高效的服务开发方式。通过服务注解，开发者可以快速定义 RESTful API，提高开发效率，降低维护成本。

服务注解的核心优势在于：

1. **简洁明了**：使用简洁的注解替代繁琐的配置
2. **代码与配置结合**：配置与代码紧密结合，提高可读性
3. **易于扩展**：支持注解的继承和组合，便于扩展
4. **RESTful 支持**：内置支持 RESTful API 设计
5. **高效开发**：减少开发时间，提高开发效率
6. **低维护成本**：注解驱动开发，降低维护成本

随着 ooder-agent-rad 的不断发展，服务注解体系也将不断完善，为开发者提供更加先进、高效的服务开发体验。