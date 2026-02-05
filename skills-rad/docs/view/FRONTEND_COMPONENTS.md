# 前端组件文档

## 1. 概述

ooder-agent-rad 前端组件库基于 ooder 全栈框架构建，提供了丰富的 UI 组件，用于快速构建现代化的企业级应用界面。这些组件包括基础组件、布局组件、数据组件和高级组件，支持响应式设计、主题切换和无障碍访问。

## 2. 组件分类

### 2.1 基础组件

基础组件是构建 UI 界面的基本元素，包括：

| 组件名称 | 类名 | 用途 |
|---------|------|------|
| 按钮 | ood.UI.Button | 触发操作 |
| 输入框 | ood.UI.Input | 文本输入 |
| 文本框 | ood.UI.TextArea | 多行文本输入 |
| 标签 | ood.UI.Label | 文本标签 |
| 复选框 | ood.UI.CheckBox | 多选选择 |
| 单选框 | ood.UI.RadioBox | 单选选择 |
| 下拉框 | ood.UI.ComboInput | 下拉选择 |
| 日期选择器 | ood.UI.DatePicker | 日期选择 |
| 时间选择器 | ood.UI.TimePicker | 时间选择 |

### 2.2 布局组件

布局组件用于组织和排列界面元素，包括：

| 组件名称 | 类名 | 用途 |
|---------|------|------|
| 表单布局 | ood.UI.FormLayout | 表单元素布局 |
| 按钮布局 | ood.UI.ButtonLayout | 按钮排列 |
| 面板 | ood.UI.Panel | 内容容器 |
| 标签页 | ood.UI.Tabs | 标签页切换 |
| 折叠列表 | ood.UI.FoldingList | 可折叠列表 |
| 堆叠面板 | ood.UI.Stacks | 堆叠布局 |
| 布局管理器 | ood.UI.Layout | 整体布局管理 |

### 2.3 数据组件

数据组件用于展示和管理数据，包括：

| 组件名称 | 类名 | 用途 |
|---------|------|------|
| 表格 | ood.UI.Table | 表格数据展示 |
| 树形表格 | ood.UI.TreeGrid | 树形结构表格 |
| 树形视图 | ood.UI.TreeView | 树形结构展示 |
| 列表 | ood.UI.List | 列表数据展示 |
| 分页条 | ood.UI.PageBar | 分页导航 |
| 进度条 | ood.UI.ProgressBar | 进度展示 |

### 2.4 高级组件

高级组件提供更复杂的功能，包括：

| 组件名称 | 类名 | 用途 |
|---------|------|------|
| SVG画布 | ood.UI.SVGPaper | SVG图形绘制 |
| 富文本编辑器 | ood.UI.RichEditor | 富文本编辑 |
| 文件上传 | ood.UI.FileUpload | 文件上传 |
| 图表 | ood.UI.ECharts | 数据可视化图表 |
| 对话框 | ood.UI.Dialog | 模态对话框 |
| 颜色选择器 | ood.UI.ColorPicker | 颜色选择 |
| 滑块 | ood.UI.Slider | 数值滑块 |

## 3. 核心组件详解

### 3.1 FormLayout 组件

FormLayout 组件是用于表单元素布局的核心组件，支持网格布局、响应式设计和主题切换。详细说明请参考 [FormLayout 组件使用指南](FRONTEND_FORM_LAYOUT.md)。

### 3.2 SVGPaper 组件

SVGPaper 组件用于绘制 SVG 图形，支持各种预定义形状和自定义路径，用于创建流程图、组织结构图等可视化图表。

### 3.3 Table 组件

Table 组件用于展示表格数据，支持排序、过滤、分页和编辑等功能。

## 4. 组件使用规范

### 4.1 组件创建

使用 ood.create() 方法创建组件：

```javascript
var button = ood.create("ood.UI.Button")
    .setHost(host, "button")
    .setName("button")
    .setText("点击按钮")
    .setOnClick("handleClick");
```

### 4.2 属性设置

使用链式调用设置组件属性：

```javascript
var input = ood.create("ood.UI.Input")
    .setWidth("200px")
    .setHeight("30px")
    .setPlaceholder("请输入文本")
    .setValue("默认值");
```

### 4.3 事件处理

使用 setOnXXX() 方法设置事件处理函数：

```javascript
var button = ood.create("ood.UI.Button")
    .setOnClick(function() {
        alert("按钮被点击了！");
    });
```

## 5. 主题与样式

### 5.1 主题切换

支持浅色主题和深色主题切换：

```javascript
// 设置主题
var formLayout = ood.create("ood.UI.FormLayout")
    .setTheme("dark");

// 切换主题
formLayout.toggleDarkMode();
```

### 5.2 响应式设计

组件自动适应不同屏幕尺寸：

```javascript
var formLayout = ood.create("ood.UI.FormLayout")
    .setResponsive(true);

// 手动调整布局
formLayout.adjustLayout();
```

### 5.3 自定义样式

通过 CSS 类名和样式属性自定义组件样式：

```javascript
var panel = ood.create("ood.UI.Panel")
    .setClassName("custom-panel")
    .setStyle({ backgroundColor: "#f0f0f0", borderRadius: "4px" });
```

## 6. 无障碍访问

组件支持无障碍访问，包括 ARIA 属性、键盘导航和屏幕阅读器支持：

```javascript
var formLayout = ood.create("ood.UI.FormLayout")
    .enhanceAccessibility();
```

## 7. 性能优化

### 7.1 懒加载

对于复杂组件，使用懒加载提高页面加载速度：

```javascript
// 延迟加载组件
ood.include("ComponentName", "/path/to/component.js", function() {
    var component = ood.create("ComponentName");
    // 使用组件
});
```

### 7.2 缓存

合理使用缓存，减少重复渲染：

```javascript
// 设置缓存
ood.include("ComponentName", "/path/to/component.js", function() {
    // 使用组件
}, null, false, { cache: true });
```

## 8. 最佳实践

1. **组件复用**：设计可复用的组件，减少重复代码
2. **模块化设计**：将复杂界面拆分为多个模块
3. **响应式设计**：确保在不同设备上都有良好的显示效果
4. **性能优化**：合理使用懒加载和缓存
5. **无障碍访问**：确保组件支持键盘导航和屏幕阅读器
6. **主题一致性**：保持应用主题的一致性

## 9. 组件开发指南

### 9.1 组件命名规范

- 组件类名使用 PascalCase 命名法
- 组件文件使用相同的名称
- 组件命名应清晰反映其功能

### 9.2 组件结构

```javascript
ood.Class("ood.UI.ComponentName", ["ood.UI", "ood.absComponent"], {
    Initialize: function () {
        // 初始化代码
    },
    Instance: {
        // 实例方法和属性
        iniComponents: function() {
            // 初始化组件
        },
        // 其他实例方法
    },
    Static: {
        // 静态属性和方法
        DataModel: {
            // 数据模型定义
        },
        Templates: {
            // 模板定义
        },
        Appearances: {
            // 外观样式定义
        },
        // 其他静态属性和方法
    }
});
```

## 10. 常见问题

### 10.1 组件加载失败

- 检查组件路径是否正确
- 检查组件类名是否正确
- 检查依赖是否已加载

### 10.2 组件样式异常

- 检查 CSS 类名是否冲突
- 检查样式属性是否正确
- 检查主题设置是否正确

### 10.3 组件事件不触发

- 检查事件名称是否正确
- 检查事件处理函数是否存在
- 检查组件是否已正确添加到 DOM

## 11. 版本更新

### 11.1 版本历史

- v1.0.0：初始版本，包含基础组件
- v1.1.0：添加高级组件和主题支持
- v1.2.0：增强响应式设计和无障碍访问

### 11.2 升级指南

- 查看版本更新日志
- 检查 API 变更
- 更新组件引用和配置

## 12. 参考资源

- [组件 API 文档](https://docs.example.com/components)
- [组件示例代码](https://github.com/example/components-examples)
- [设计系统指南](https://design.example.com)
