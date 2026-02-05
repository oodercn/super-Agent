# 视图设计规范

## 1. 总览

ooder-agent-rad 采用注解驱动的视图开发方式，通过简洁的注解定义复杂的视图结构。本文档将详细介绍 ooder-agent-rad 的视图设计规范，包括视图类型、布局设计、组件设计等。

## 2. 视图类型

ooder-agent-rad 支持多种视图类型，包括：

| 视图类型 | 用途 | 核心注解 |
|---------|------|---------|
| 布局视图 | 定义页面的整体布局 | @LayoutAnnotation, @LayoutViewAnnotation |
| 面板视图 | 定义面板组件 | @PanelViewAnnotation |
| 表单视图 | 定义表单组件 | @FormViewAnnotation |
| 网格视图 | 定义网格组件 | @GridViewAnnotation |
| 树形视图 | 定义树形结构 | @NavTreeViewAnnotation |
| 图表视图 | 定义图表组件 | @EChartViewAnnotation |

## 3. 视图布局设计

### 3.1 布局类型

ooder-agent-rad 支持两种主要的布局类型：

- **水平布局**（horizontal）：组件水平排列
- **垂直布局**（vertical）：组件垂直排列

### 3.2 布局注解

使用 @LayoutAnnotation 注解定义视图的整体布局：

```java
@LayoutAnnotation(transparent = false, layoutType = LayoutType.vertical)
public class RADMainView {
    // 视图实现
}
```

### 3.3 布局项位置

使用 @LayoutItemAnnotation 注解定义组件在布局中的位置：

| 位置类型 | 用途 |
|---------|------|
| PosType.top | 顶部区域 |
| PosType.bottom | 底部区域 |
| PosType.left | 左侧区域 |
| PosType.right | 右侧区域 |
| PosType.main | 主内容区域 |

**示例**：
```java
@RequestMapping(method = RequestMethod.POST, value = "main")
@LayoutItemAnnotation(pos = PosType.main)
@ModuleAnnotation
@PanelViewAnnotation()
@ResponseBody
public ModuleComponent getMainView() {
    // 实现代码
}
```

## 4. 面板组件设计

### 4.1 面板属性

面板组件是 ooder-agent-rad 中最常用的组件之一，用于组织和展示内容。面板组件的核心属性包括：

- `caption`：面板标题
- `icon`：面板图标
- `collapsible`：是否可折叠
- `expanded`：初始是否展开
- `border`：是否显示边框
- `padding`：内边距
- `margin`：外边距

### 4.2 面板注解

使用 @PanelViewAnnotation 注解定义面板组件：

```java
@PanelViewAnnotation(
    caption = "属性面板",
    icon = "ri-settings-line",
    collapsible = true,
    expanded = false
)
public class PropertyPanel {
    // 面板实现
}
```

## 5. 表单视图设计

### 5.1 表单属性

表单视图用于数据录入和展示，核心属性包括：

- `expression`：表单表达式，用于绑定数据
- `saveUrl`：保存URL，用于提交表单数据
- `dataUrl`：数据URL，用于加载表单数据
- `caption`：表单标题
- `autoSave`：是否自动保存
- `validation`：验证规则

### 5.2 表单注解

使用 @FormViewAnnotation 注解定义表单视图：

```java
@FormViewAnnotation(
    expression = "userForm",
    saveUrl = "/api/user/save",
    dataUrl = "/api/user/data",
    caption = "用户表单",
    autoSave = false
)
public class UserForm {
    // 表单实现
}
```

## 6. 网格视图设计

### 6.1 网格属性

网格视图用于展示列表数据，核心属性包括：

- `expression`：网格表达式，用于绑定数据
- `dataUrl`：数据URL，用于加载网格数据
- `editorPath`：编辑器路径，用于编辑网格数据
- `pageSize`：分页大小
- `sortable`：是否支持排序
- `filterable`：是否支持过滤
- `selectable`：是否支持选择

### 6.2 网格注解

使用 @GridViewAnnotation 注解定义网格视图：

```java
@GridViewAnnotation(
    expression = "userGrid",
    dataUrl = "/api/user/list",
    editorPath = "/api/user/edit",
    pageSize = 20,
    sortable = true,
    filterable = true,
    selectable = true
)
public class UserGrid {
    // 网格实现
}
```

## 7. 树形视图设计

### 7.1 树形属性

树形视图用于展示层级结构数据，核心属性包括：

- `expression`：树表达式，用于绑定数据
- `dataUrl`：数据URL，用于加载树数据
- `expandLevel`：初始展开级别
- `checkable`：是否支持勾选
- `draggable`：是否支持拖拽

### 7.2 树形注解

使用 @NavTreeViewAnnotation 注解定义树形视图：

```java
@NavTreeViewAnnotation(
    expression = "menuTree",
    dataUrl = "/api/menu/list",
    expandLevel = 1,
    checkable = false,
    draggable = false
)
public class MenuTree {
    // 树形实现
}
```

## 8. 视图组件设计

### 8.1 组件类型

ooder-agent-rad 支持多种组件类型，包括：

- **基础组件**：按钮、输入框、文本框、标签等
- **布局组件**：面板、网格、表单布局等
- **数据组件**：表格、树形控件、下拉列表等
- **高级组件**：图表、富文本编辑器、文件上传等

### 8.2 组件注解

使用相应的组件注解定义组件：

- @ButtonAnnotation：定义按钮组件
- @InputAnnotation：定义输入组件
- @FormAnnotation：定义表单组件
- @GridAnnotation：定义网格组件
- @APIEventAnnotation：定义组件事件

**示例**：
```java
@ButtonAnnotation(
    text = "保存",
    icon = "ri-save-line",
    onClick = "saveForm()",
    type = ButtonType.primary
)
private Button saveButton;
```

## 9. 视图事件设计

### 9.1 事件类型

ooder-agent-rad 支持多种事件类型，包括：

- **点击事件**：按钮点击、链接点击等
- **输入事件**：输入框输入、下拉框选择等
- **表单事件**：表单提交、表单验证等
- **数据事件**：数据加载、数据更新等
- **组件事件**：组件显示、组件隐藏等

### 9.2 事件注解

使用 @APIEventAnnotation 注解定义组件事件：

```java
@APIEventAnnotation(
    name = "saveForm",
    caption = "保存表单",
    url = "/api/user/save",
    method = RequestMethod.POST,
    params = {"data", "userId"}
)
private Event saveEvent;
```

## 10. 视图样式设计

### 10.1 样式属性

ooder-agent-rad 支持多种样式属性，包括：

- **颜色属性**：backgroundColor, textColor, borderColor等
- **布局属性**：width, height, padding, margin等
- **边框属性**：border, borderWidth, borderRadius等
- **字体属性**：fontFamily, fontSize, fontWeight等

### 10.2 样式注解

使用样式注解定义组件样式：

```java
@StyleAnnotation(
    backgroundColor = "#ffffff",
    textColor = "#333333",
    padding = "10px",
    margin = "5px",
    border = "1px solid #e0e0e0",
    borderRadius = "4px"
)
private Panel mainPanel;
```

## 11. 视图响应式设计

### 11.1 响应式断点

ooder-agent-rad 支持响应式设计，根据屏幕尺寸自动调整布局：

| 断点类型 | 屏幕宽度 |
|---------|----------|
| xs | < 576px |
| sm | ≥ 576px |
| md | ≥ 768px |
| lg | ≥ 992px |
| xl | ≥ 1200px |
| xxl | ≥ 1600px |

### 11.2 响应式注解

使用 @ResponsiveAnnotation 注解定义响应式布局：

```java
@ResponsiveAnnotation(
    xs = @LayoutConfig(cols = 1),
    sm = @LayoutConfig(cols = 2),
    md = @LayoutConfig(cols = 3),
    lg = @LayoutConfig(cols = 4)
)
private GridLayout mainGrid;
```

## 12. 视图设计最佳实践

### 12.1 视图组织

1. **模块化设计**：将复杂的视图拆分为多个模块
2. **分层设计**：遵循MVC架构，分离视图、模型和控制器
3. **组件复用**：设计可复用的组件，减少重复代码

### 12.2 布局设计

1. **简洁明了**：布局结构清晰，层次分明
2. **响应式设计**：支持不同屏幕尺寸
3. **用户体验优先**：考虑用户的使用习惯和操作流程

### 12.3 组件设计

1. **单一职责**：每个组件只负责一个功能
2. **可配置性**：组件属性可配置，支持不同场景
3. **可扩展性**：组件支持扩展，便于定制

### 12.4 性能优化

1. **懒加载**：对于复杂组件，考虑使用懒加载
2. **缓存**：合理使用缓存，减少重复渲染
3. **异步加载**：异步加载数据，提高页面响应速度

## 13. 总结

ooder-agent-rad 的视图设计规范基于注解驱动开发，通过简洁的注解定义复杂的视图结构。遵循这些规范可以提高视图开发的效率和质量，确保视图的一致性和可维护性。

通过采用模块化设计、分层设计和组件复用等最佳实践，可以进一步提高视图开发的效率和质量，为用户提供良好的使用体验。