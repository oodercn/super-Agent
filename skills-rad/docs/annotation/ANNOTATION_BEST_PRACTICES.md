# 注解最佳实践

## 1. 总览

ooder-agent-rad 采用注解驱动开发，注解是连接代码和配置的桥梁。使用注解时，遵循最佳实践可以提高代码的可读性、可维护性和性能。本文档将介绍 ooder-agent-rad 注解的最佳实践。

## 2. 注解设计原则

### 2.1 单一职责原则

每个注解应该只负责一个功能，避免创建过于复杂的注解。例如，@Controller 只负责标记控制器类，@RequestMapping 只负责映射请求路径。

**良好实践**：
```java
@Controller
@RequestMapping("/rad/")
public class Main {
    // 控制器实现
}
```

**不良实践**：
```java
@ControllerWithMapping("/rad/") // 同时负责标记控制器和映射路径
public class Main {
    // 控制器实现
}
```

### 2.2 简洁明了原则

注解的属性应该简洁明了，避免过多的属性。对于可选属性，应该提供合理的默认值，减少使用时的配置工作量。

**良好实践**：
```java
@PanelViewAnnotation(caption = "主面板") // 只配置必要属性
public class MainPanel {
    // 面板实现
}
```

**不良实践**：
```java
@PanelViewAnnotation(
    caption = "主面板",
    icon = "ri-home-line",
    collapsible = false,
    expanded = true,
    border = true,
    padding = "10px",
    margin = "5px",
    backgroundColor = "#ffffff",
    textColor = "#333333"
) // 配置了过多不必要的属性
public class MainPanel {
    // 面板实现
}
```

### 2.3 一致性原则

在整个项目中，应该保持注解使用的一致性。例如，对于相同类型的组件，应该使用相同的注解组合。

**良好实践**：
```java
// 控制器类都使用 @Controller + @RequestMapping 注解
@Controller
@RequestMapping("/rad/")
public class Main {
    // 控制器实现
}

@Controller
@RequestMapping("/admin/")
public class AdminController {
    // 控制器实现
}
```

**不良实践**：
```java
// 不同控制器类使用不同的注解组合
@Controller
@RequestMapping("/rad/")
public class Main {
    // 控制器实现
}

@RestController // 使用了 @RestController 而不是 @Controller + @ResponseBody
@RequestMapping("/admin/")
public class AdminController {
    // 控制器实现
}
```

### 2.4 文档化原则

对于复杂的注解，应该添加详细的文档注释，说明注解的用途、属性和使用示例。

**良好实践**：
```java
/**
 * 表单视图注解
 * 用于定义表单视图的属性和行为
 * 
 * @param expression 表单表达式
 * @param saveUrl 保存URL
 * @param dataUrl 数据URL
 * @param caption 表单标题
 * @param autoSave 是否自动保存
 */
@FormViewAnnotation(
    expression = "userForm",
    saveUrl = "/api/user/save",
    dataUrl = "/api/user/data",
    caption = "用户管理表单",
    autoSave = true
)
public void getUserForm() {
    // 表单实现
}
```

**不良实践**：
```java
@FormViewAnnotation(expression = "userForm", saveUrl = "/api/user/save")
public void getUserForm() {
    // 表单实现
}
```

## 3. 注解使用最佳实践

### 3.1 控制器注解

**最佳实践**：
- 使用 @Controller 标记控制器类
- 使用 @RequestMapping 映射请求路径，指定明确的 HTTP 方法
- 对于返回 JSON 数据的方法，使用 @ResponseBody 注解
- 为方法提供中文名称，便于生成文档和界面显示

**示例**：
```java
@Controller
@RequestMapping("/rad/")
public class Main {
    
    @RequestMapping(value = "Main", method = RequestMethod.GET)
    @LayoutViewAnnotation
    @ModuleAnnotation
    @MethodChinaName(cname = "获取主面板")
    @ResponseBody
    public ResultModel<FramePanel> getMain() {
        // 实现代码
    }
}
```

### 3.2 视图注解

**最佳实践**：
- 使用 @LayoutAnnotation 定义视图的整体布局
- 使用 @PanelViewAnnotation 定义面板组件
- 使用 @FormViewAnnotation 定义表单视图
- 使用 @GridViewAnnotation 定义网格视图
- 使用 @NavTreeViewAnnotation 定义树形视图

**示例**：
```java
@LayoutAnnotation(transparent = false, layoutType = LayoutType.vertical)
public class RADMainView {
    
    @RequestMapping(method = RequestMethod.POST, value = "main")
    @LayoutItemAnnotation(pos = PosType.main)
    @ModuleAnnotation
    @PanelViewAnnotation(caption = "主面板")
    @ResponseBody
    public ModuleComponent getMainView() {
        // 实现代码
    }
}
```

### 3.3 服务注解

**最佳实践**：
- 使用 @Controller 标记服务类
- 使用 @RequestMapping 映射请求路径
- 使用 @ResponseBody 标记返回结果直接作为响应体
- 使用 @MethodChinaName 为方法提供中文名称
- 遵循 RESTful API 设计原则

**示例**：
```java
@Controller
@RequestMapping(value = {"/RAD/"})
@MethodChinaName(cname = "OOD编辑器")
public class RADEditor {
    
    @RequestMapping(value = {"exportProject"}, method = {RequestMethod.POST})
    @MethodChinaName(cname = "导出工程")
    @ResponseBody
    public ResultModel<Boolean> exportProject(String projectName, String type, String exportPath) {
        // 实现代码
    }
}
```

### 3.4 组件注解

**最佳实践**：
- 使用 @ModuleAnnotation 标记模块组件
- 使用 @PanelViewAnnotation 定义面板组件
- 使用 @ButtonAnnotation 定义按钮组件
- 使用 @InputAnnotation 定义输入组件
- 使用 @FormAnnotation 定义表单组件
- 使用 @GridAnnotation 定义网格组件

**示例**：
```java
@ModuleAnnotation(caption = "用户信息表单")
@PanelViewAnnotation(caption = "用户信息", icon = "ri-user-line")
@FormAnnotation(name = "userForm", action = "/api/user/save")
public class UserInfoForm {
    // 表单实现
}
```

## 4. RESTful API 设计最佳实践

### 4.1 使用合适的 HTTP 方法

根据操作类型选择合适的 HTTP 方法：
- GET：获取资源
- POST：创建资源
- PUT：更新资源
- DELETE：删除资源
- PATCH：部分更新资源

**示例**：
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
    
    // POST /api/users - 创建用户
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody User user) {
        // 实现代码
    }
}
```

### 4.2 使用有意义的 URI

URI 应该使用名词而不是动词，使用复数形式表示资源集合，使用层次结构表示资源关系。

**良好实践**：
- /api/users：用户资源集合
- /api/users/{id}：单个用户资源
- /api/users/{id}/roles：用户的角色资源

**不良实践**：
- /api/getUsers：使用动词
- /api/user：使用单数形式表示资源集合
- /api/userRoles/{userId}：没有使用层次结构

### 4.3 使用标准的状态码

返回标准的 HTTP 状态码，便于客户端处理响应：
- 200 OK：成功
- 201 Created：资源创建成功
- 204 No Content：资源删除成功
- 400 Bad Request：请求参数错误
- 401 Unauthorized：未授权
- 403 Forbidden：禁止访问
- 404 Not Found：资源不存在
- 500 Internal Server Error：服务器内部错误

## 5. 性能优化最佳实践

### 5.1 合理使用注解的保留策略

Java 注解有三种保留策略：
- RetentionPolicy.SOURCE：只在源代码中保留，编译时丢弃
- RetentionPolicy.CLASS：在编译时保留，运行时丢弃
- RetentionPolicy.RUNTIME：在运行时保留，可通过反射获取

**最佳实践**：
- 对于只在编译时使用的注解，使用 RetentionPolicy.CLASS
- 对于需要在运行时通过反射获取的注解，使用 RetentionPolicy.RUNTIME
- 避免不必要地使用 RetentionPolicy.RUNTIME，影响性能

### 5.2 避免过度使用注解

虽然注解可以简化配置，但过度使用注解会导致代码难以理解和维护。**最佳实践**：
- 只在必要时使用注解
- 避免在单个元素上使用过多注解
- 合理组合注解，减少重复配置

### 5.3 合理使用缓存

对于频繁调用的注解处理逻辑，可以使用缓存来提高性能。例如，对于视图注解，可以缓存生成的视图组件，避免重复生成。

## 6. 注解与传统配置的结合

在某些情况下，传统的配置方式可能比注解更合适。例如，对于复杂的配置，使用配置文件可能比使用大量注解更清晰。

**最佳实践**：
- 对于简单的配置，使用注解
- 对于复杂的配置，使用配置文件
- 合理结合注解和配置文件，发挥各自的优势

## 7. 注解文档化最佳实践

### 7.1 为注解添加文档注释

为自定义注解添加详细的文档注释，说明注解的用途、属性和使用示例。

**示例**：
```java
/**
 * 模块注解
 * 用于标记模块组件
 * 
 * @author ooderTeam
 * @version 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {
    
    /**
     * 模块标题
     * @return 模块标题
     */
    String caption() default "";
    
    /**
     * 面板类型
     * @return 面板类型
     */
    PanelType panelType() default PanelType.panel;
    
    /**
     * 模块图标
     * @return 模块图标
     */
    String icon() default "";
}
```

### 7.2 为使用注解的代码添加文档注释

为使用注解的代码添加文档注释，说明注解的作用和配置的含义。

**示例**：
```java
/**
 * RAD主控制器类
 * 提供应用程序的主入口视图
 */
@Controller
@RequestMapping("/rad/")
public class Main {
    
    /**
     * 获取主面板
     * 返回应用程序的主框架面板，包含菜单栏、工具栏和工作区
     * 
     * @return 主面板结果模型
     */
    @RequestMapping("Main")
    @LayoutViewAnnotation
    @ModuleAnnotation
    @MethodChinaName(cname = "获取主面板")
    @ResponseBody
    public ResultModel<FramePanel> getMain() {
        // 实现代码
    }
}
```

## 8. 常见注解使用场景

| 场景 | 推荐注解组合 |
|------|------------|
| 主控制器 | @Controller + @RequestMapping |
| GET 请求处理方法 | @RequestMapping(method = RequestMethod.GET) + @ResponseBody |
| POST 请求处理方法 | @RequestMapping(method = RequestMethod.POST) + @ResponseBody |
| 表单视图 | @FormViewAnnotation + @ModuleAnnotation + @ResponseBody |
| 网格视图 | @GridViewAnnotation + @ModuleAnnotation + @ResponseBody |
| 面板组件 | @PanelViewAnnotation + @ModuleAnnotation |
| 树形视图 | @NavTreeViewAnnotation + @ModuleAnnotation + @ResponseBody |
| RESTful API | @RequestMapping(method = RequestMethod.*) + @ResponseBody |

## 9. 总结

ooder-agent-rad 的注解体系为开发者提供了一种简洁、高效的开发方式。遵循注解最佳实践，可以提高代码的可读性、可维护性和性能。

**核心要点**：
1. 遵循单一职责原则，每个注解只负责一个功能
2. 遵循简洁明了原则，减少不必要的属性配置
3. 遵循一致性原则，保持注解使用的一致性
4. 遵循文档化原则，为注解和使用注解的代码添加文档注释
5. 合理使用注解的保留策略，避免不必要的性能开销
6. 避免过度使用注解，保持代码的简洁性
7. 合理结合注解和传统配置，发挥各自的优势

通过遵循这些最佳实践，开发者可以充分利用 ooder-agent-rad 的注解体系，提高开发效率，降低维护成本，开发出高质量的应用程序。