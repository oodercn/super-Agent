# 组件手册

## 1. 概述

ooder-agent-rad 组件库是一个功能丰富的前端组件集合，基于 ooder 全栈框架构建，用于快速开发企业级应用界面。本手册提供了所有组件的详细使用说明、API 参考和示例代码。

## 2. 组件分类

### 2.1 基础组件

| 组件名称 | 文档链接 | 用途 |
|---------|----------|------|
| Button | [Button.md](Button.md) | 触发操作 |
| Input | [Input.md](Input.md) | 文本输入 |
| CheckBox | [CheckBox.md](CheckBox.md) | 多选选择 |
| RadioBox | [RadioBox.md](RadioBox.md) | 单选选择 |
| Label | [Label.md](Label.md) | 文本标签 |
| ComboInput | [ComboInput.md](ComboInput.md) | 下拉选择 |

### 2.2 布局组件

| 组件名称 | 文档链接 | 用途 |
|---------|----------|------|
| FormLayout | [FormLayout.md](FormLayout.md) | 表单布局 |
| ButtonLayout | [ButtonLayout.md](ButtonLayout.md) | 按钮布局 |
| Panel | [Panel.md](Panel.md) | 内容容器 |
| Layout | [Layout.md](Layout.md) | 整体布局 |
| Tabs | [Tabs.md](Tabs.md) | 标签页切换 |
| Stacks | [Stacks.md](Stacks.md) | 堆叠布局 |
| FoldingTabs | [FoldingTabs.md](FoldingTabs.md) | 可折叠标签页 |

### 2.3 数据组件

| 组件名称 | 文档链接 | 用途 |
|---------|----------|------|
| Table | [Table.md](Table.md) | 表格数据展示 |
| TreeGrid | [TreeGrid.md](TreeGrid.md) | 树形表格 |
| TreeView | [TreeView.md](TreeView.md) | 树形结构展示 |
| List | [List.md](List.md) | 列表数据展示 |
| PageBar | [PageBar.md](PageBar.md) | 分页导航 |
| ComboInput | [ComboInput.md](ComboInput.md) | 下拉选择 |

### 2.4 高级组件

| 组件名称 | 文档链接 | 用途 |
|---------|----------|------|
| SVGPaper | [SVGPaper.md](SVGPaper.md) | SVG图形绘制 |
| RichEditor | [RichEditor.md](RichEditor.md) | 富文本编辑 |
| FileUpload | [FileUpload.md](FileUpload.md) | 文件上传 |
| ECharts | [ECharts.md](ECharts.md) | 数据可视化图表 |
| Dialog | [Dialog.md](Dialog.md) | 模态对话框 |
| ColorPicker | [ColorPicker.md](ColorPicker.md) | 颜色选择 |
| Slider | [Slider.md](Slider.md) | 数值滑块 |

## 3. 组件使用规范

### 3.1 组件创建

使用 `ood.create()` 方法创建组件：

```javascript
var button = ood.create("ood.UI.Button")
    .setHost(host, "button")
    .setName("button")
    .setText("点击按钮")
    .setOnClick("handleClick");
```

### 3.2 属性设置

使用链式调用设置组件属性：

```javascript
var input = ood.create("ood.UI.Input")
    .setWidth("200px")
    .setHeight("30px")
    .setPlaceholder("请输入文本")
    .setValue("默认值");
```

### 3.3 事件处理

使用 setOnXXX() 方法设置事件处理函数：

```javascript
var button = ood.create("ood.UI.Button")
    .setOnClick(function() {
        alert("按钮被点击了！");
    });
```

## 4. 主题与样式

### 4.1 主题切换

支持浅色主题和深色主题切换：

```javascript
// 设置主题
var component = ood.create("ood.UI.Component")
    .setTheme("dark");

// 切换主题
component.toggleDarkMode();
```

### 4.2 自定义样式

通过 CSS 类名和样式属性自定义组件样式：

```javascript
var panel = ood.create("ood.UI.Panel")
    .setClassName("custom-panel")
    .setStyle({ backgroundColor: "#f0f0f0", borderRadius: "4px" });
```

## 5. 无障碍访问

组件支持无障碍访问，包括 ARIA 属性、键盘导航和屏幕阅读器支持：

```javascript
var component = ood.create("ood.UI.Component")
    .enhanceAccessibility();
```

## 6. 性能优化

### 6.1 懒加载

对于复杂组件，使用懒加载提高页面加载速度：

```javascript
// 延迟加载组件
ood.include("ComponentName", "/path/to/component.js", function() {
    var component = ood.create("ComponentName");
    // 使用组件
});
```

### 6.2 缓存

合理使用缓存，减少重复渲染：

```javascript
// 设置缓存
ood.include("ComponentName", "/path/to/component.js", function() {
    // 使用组件
}, null, false, { cache: true });
```

## 7. 最佳实践

1. **组件复用**：设计可复用的组件，减少重复代码
2. **模块化设计**：将复杂界面拆分为多个模块
3. **响应式设计**：确保在不同设备上都有良好的显示效果
4. **性能优化**：合理使用懒加载和缓存
5. **无障碍访问**：确保组件支持键盘导航和屏幕阅读器
6. **主题一致性**：保持应用主题的一致性

## 8. 常见问题

### 8.1 组件加载失败

- 检查组件路径是否正确
- 检查组件类名是否正确
- 检查依赖是否已加载

### 8.2 组件样式异常

- 检查 CSS 类名是否冲突
- 检查样式属性是否正确
- 检查主题设置是否正确

### 8.3 组件事件不触发

- 检查事件名称是否正确
- 检查事件处理函数是否存在
- 检查组件是否已正确添加到 DOM

## 9. 版本更新

### 9.1 版本历史

- v1.0.0：初始版本，包含基础组件
- v1.1.0：添加高级组件和主题支持
- v1.2.0：增强响应式设计和无障碍访问

### 9.2 升级指南

- 查看版本更新日志
- 检查 API 变更
- 更新组件引用和配置

## 10. 参考资源

- [组件 API 文档](https://docs.example.com/components)
- [组件示例代码](https://github.com/example/components-examples)
- [设计系统指南](https://design.example.com)
