# ooder-pro Code First模式注解测试说明

## 概述

本文档介绍了在 ooder-pro 环境下如何实现 Code First 模式的注解驱动开发。通过创建符合 ooder 注解规范的 Java 类和对应的前端组件，验证注解驱动在实际项目中的正确使用。

## 项目结构

```
src/main/java/net/ooder/test/
├── AnnotationTestView.java     # 注解驱动的视图类 (Code First)
├── AnnotationTestService.java  # 注解驱动的服务类 (Code First)

src/main/resources/static/ood/js/mobile/OA/
├── AnnotationTestPage.js       # 前端测试页面
├── README_CodeFirstTest.md     # 本说明文档
```

## Code First 模式实现

### 1. 视图类设计 (AnnotationTestView.java)

遵循 ooder 注解规范，使用注解定义视图结构：

```java
@FormAnnotation(col = 1, bottombarMenu = {CustomFormMenu.SAVE, CustomFormMenu.SEARCH})
public class AnnotationTestView {
    @CustomAnnotation(hidden = true, uid = true)
    String id;
    
    @CustomAnnotation(caption = "名称")
    @TextEditorAnnotation()
    @FieldAnnotation(required = true)
    String name;
    
    // 其他字段...
}
```

### 2. 服务类设计 (AnnotationTestService.java)

使用 Spring 注解和 ooder 视图注解结合：

```java
@Controller
@RequestMapping(path = "/test/annotation")
public class AnnotationTestService {
    
    @RequestMapping(method = RequestMethod.POST, value = "getForm")
    @FormViewAnnotation()  // ooder 视图注解
    @ResponseBody
    public ResultModel<AnnotationTestView> getForm(@RequestParam(required = false) String id) {
        // 实现逻辑
    }
}
```

### 3. 前端集成 (AnnotationTestPage.js)

前端通过 APICaller 与后端注解服务进行交互：

```javascript
// 通过APICaller调用后端服务
var apiCaller = ood.create("ood.APICaller");
apiCaller.setRequestDataSource({
    url: "/test/annotation/getForm",
    method: "POST",
    params: {
        id: "1"
    }
});
apiCaller.execute();
```

## 注解规范遵循

### 强关联设计原理
- 通过 `@FormViewAnnotation()` 和 `@GridViewAnnotation()` 等注解建立视图绑定
- 使用泛型参数确保类型安全：`ResultModel<AnnotationTestView>`
- 编译期验证注解配置的正确性

### 生命周期管理
1. **编译期**：Java 编译器处理注解，生成元数据
2. **启动期**：Spring 扫描并注册带注解的组件
3. **运行时**：ooder 引擎解析注解并生成对应的 UI 组件
4. **销毁期**：Spring 容器管理组件生命周期

### 参数设计规范
1. **简单参数**：用于获取、删除等操作
   ```java
   public ResultModel<AnnotationTestView> getForm(@RequestParam(required = false) String id)
   ```

2. **复杂视图对象参数**：用于保存、更新等操作
   ```java
   public ResultModel<Boolean> save(@RequestBody AnnotationTestView view)
   ```

3. **查询参数**：用于列表查询操作
   ```java
   public ListResultModel<List<AnnotationTestView>> query(
       @RequestParam(required = false) String keyword,
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "10") int size)
   ```

## 测试验证

### 前端测试
1. 打开 AnnotationTestPage.js 页面
2. 点击不同的测试按钮验证各种功能
3. 查看浏览器控制台输出的测试结果

### 后端测试
1. 启动 Spring Boot 应用
2. 通过 REST API 测试各服务接口
3. 验证注解驱动的功能正确性

## ood 与 ooder annotation 的隔离设计

在 ooder-pro 环境中，我们确保了 ood 前端框架与 ooder 后端注解的良好隔离：

1. **后端 (ooder annotation)**：
   - 使用 Java 注解定义视图和服务
   - 通过 Spring Boot 提供 REST API
   - 实现 Code First 模式

2. **前端 (ood)**：
   - 使用 JavaScript/TypeScript 实现 UI 组件
   - 通过 APICaller 与后端交互
   - 不直接依赖后端注解实现

3. **集成层**：
   - 通过 REST API 实现前后端通信
   - ooder 引擎解析后端注解并生成前端组件映射
   - 确保两端的独立性和可维护性

## 相互校验机制

在测试和验证环节，ood 和 ooder annotation 可以相互校验：

1. **功能校验**：前端功能与后端注解定义的一致性
2. **数据校验**：前后端数据传输的正确性
3. **类型校验**：视图对象在前后端的类型匹配
4. **行为校验**：注解定义的行为在前端的正确体现

## 最佳实践

### 1. 注解使用规范
- 优先使用 ooder 视图注解而非外部 URL 配置
- 遵循强关联设计原理，通过泛型建立类型绑定
- 合理使用三级注解体系（强注解、分支注解、叶子注解）

### 2. 参数设计规范
- 根据业务场景选择合适的参数类型
- 遵循参数最小化原则
- 正确使用默认值机制

### 3. 前后端隔离
- 确保前端不直接依赖后端注解实现
- 通过标准接口进行前后端通信
- 保持两端的独立开发和测试能力

## 总结

通过本次测试用例的实现，我们验证了在 ooder-pro 环境下 Code First 模式的正确性，展示了如何：

1. 使用 ooder 注解规范创建后端视图和服务类
2. 实现前后端的良好隔离设计
3. 通过标准接口实现两端的集成和相互校验
4. 遵循 ooder 框架的核心规范和最佳实践

这为在实际项目中应用 ooder 注解驱动开发提供了参考和指导。