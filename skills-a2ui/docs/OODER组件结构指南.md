# OODER 组件结构指南

## 1. 组件体系概述

OODER 前端框架采用组件化架构，所有组件均继承自 `ood.UI` 基类，形成了层次分明的组件体系。组件分为两大类：

### 1.1 基础组件

**定义位置**：`ood/js/UI.js`（核心基础组件）

**特点**：
- 直接封装HTML原生元素
- 提供基础UI交互功能
- 轻量、高效
- 所有组件均以 `ood.UI.` 为命名前缀

### 1.2 高级组件

**定义位置**：`ood/js/UI/` 目录下的独立文件

**特点**：
- 基于基础组件构建的复合组件
- 提供复杂的业务功能
- 具有完整的生命周期管理
- 支持丰富的配置选项

## 2. 基础组件列表

| 组件名称 | 继承关系 | 功能描述 | 核心方法/属性 |
|---------|---------|---------|-------------|
| **Div** | ood.UI | 容器组件，对应HTML的`<div>` | setHtml, setCustomStyle, append |
| **Span** | ood.UI | 文本容器，对应HTML的`<span>` | setCaption, setFontColor, setFontSize |
| **Button** | [ood.UI.HTMLButton, ood.absValue] | 按钮组件，对应HTML的`<button>` | setCaption, setImageClass, setClick |
| **Icon** | ood.UI | 图标组件 | setImageClass, setIconFontSize |
| **Label** | ood.UI | 文本标签 | setCaption, setFontColor, setFontSize |
| **Image** | ood.UI | 图片组件 | setSrc, setAlt, setWidth, setHeight |

## 3. 高级组件分类

### 3.1 布局组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Block** | 基础布局块 | dock, width, height, position |
| **Layout** | 复杂布局管理器 | layoutType, items |
| **Panel** | 面板容器 | title, collapsible, borderType |
| **ContentBlock** | 内容块组件 | content, title |
| **TitleBlock** | 带标题的块 | title, content |
| **ButtonLayout** | 按钮布局 | items, layoutType |
| **FormLayout** | 表单布局 | items, columns |
| **MFormLayout** | 移动端表单布局 | items, columns |

### 3.2 导航组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **MenuBar** | 菜单栏 | items, orientation |
| **ToolBar** | 工具栏 | items, orientation |
| **Tabs** | 标签页 | items, activeTab |
| **MTabs** | 移动端标签页 | items, activeTab |
| **FoldingTabs** | 折叠标签页 | items, activeTab |
| **PageBar** | 分页栏 | total, pageSize, currentPage |

### 3.3 数据展示组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Gallery** | 图片画廊 | items, columns, itemWidth |
| **List** | 列表组件 | items, template |
| **TreeView** | 树状视图 | items, expandLevel |
| **MTreeView** | 移动端树状视图 | items, expandLevel |
| **TreeGrid** | 树状表格 | columns, items |
| **MTreeGrid** | 移动端树状表格 | columns, items |
| **FoldingList** | 折叠列表 | items, expandLevel |
| **ProgressBar** | 进度条 | value, max, min |

### 3.4 输入组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Input** | 输入框 | value, placeholder, type |
| **ComboInput** | 组合输入框 | items, value, placeholder |
| **CheckBox** | 复选框 | checked, caption |
| **RadioBox** | 单选框 | checked, group, caption |
| **HiddenInput** | 隐藏输入框 | value |
| **RichEditor** | 富文本编辑器 | content, height |
| **FileUpload** | 文件上传 | accept, multiple |

### 3.5 选择器组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **DatePicker** | 日期选择器 | value, format |
| **TimePicker** | 时间选择器 | value, format |
| **ColorPicker** | 颜色选择器 | value, format |

### 3.6 对话框组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Dialog** | 对话框 | title, content, buttons |
| **MDialog** | 移动端对话框 | title, content, buttons |
| **PopMenu** | 弹出菜单 | items, position |

### 3.7 多媒体组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Audio** | 音频播放器 | src, controls |
| **Video** | 视频播放器 | src, controls, autoplay |
| **Flash** | Flash播放器 | src, width, height |
| **Camera** | 摄像头组件 | width, height |

### 3.8 图表组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **ECharts** | ECharts图表 | option, height |
| **FusionChartsXT** | FusionCharts图表 | type, data, width, height |
| **Tensor** | 张量可视化 | data, width, height |

### 3.9 其他组件

| 组件名称 | 功能描述 | 核心属性 |
|---------|---------|---------|
| **Resizer** | 可调整大小组件 | direction, minWidth, minHeight |
| **StatusButtons** | 状态按钮组 | items, activeItem |
| **Group** | 组件组 | items |
| **Opinion** | 意见反馈组件 | items, value |
| **IOTGallery** | IoT设备画廊 | items, columns |
| **InfoBlock** | 信息展示块 | title, content, type |
| **SVGPaper** | SVG绘图容器 | width, height |
| **Slider** | 滑块组件 | value, min, max |
| **Stacks** | 堆叠组件 | items, activeItem |
| **TreeBar** | 树状导航栏 | items, expandLevel |

## 4. 组件查找规则

1. **基础组件**：直接在 `ood/js/UI.js` 中查找
   - 包括：Div, Span, Button, Icon, Label, Image
   - 使用方法：`ood.create("ood.UI.Button")`

2. **高级组件**：在 `ood/js/UI/` 目录下查找对应文件
   - 例如：`ood/js/UI/MenuBar.js` 对应 `ood.UI.MenuBar`
   - 使用方法：`ood.create("ood.UI.MenuBar")`

## 5. 组件创建与使用流程

### 5.1 创建组件

**推荐方式**：使用 `ood.create()` 方法（替代 `new` 关键字）

```javascript
// 创建基础组件
var button = ood.create("ood.UI.Button")
    .setCaption("点击我")
    .setImageClass("fa fa-check");

// 创建高级组件
var menuBar = ood.create("ood.UI.MenuBar")
    .setItems([
        { caption: "首页", id: "home", imageClass: "fa fa-home" },
        { caption: "关于", id: "about", imageClass: "fa fa-info" }
    ]);
```

### 5.2 添加到容器

**推荐方式**：使用 `host.pageContainer.append()` 或 `parent.append()`

```javascript
// 添加到页面容器
host.pageContainer.append(button);

// 添加到父组件
host.topBar.append(menuBar);
```

### 5.3 设置属性

**规则**：使用组件提供的 `set*` 方法，避免直接修改属性

```javascript
// 正确方式
button.setCaption("新标题");
button.setFontColor("#FF0000");

// 错误方式
button.properties.caption = "新标题"; // 可能导致渲染问题
```

## 6. 组件定位系统

### 6.1 核心定位属性

| 属性名称 | 类型 | 取值范围 | 描述 |
|---------|------|---------|------|
| **position** | string | absolute, relative, static | 定位方式 |
| **dock** | string | top, bottom, left, right, none | 停靠方式 |
| **top** | string/number | 如 "10px", "2em", 100 | 距离顶部位置 |
| **left** | string/number | 如 "10px", "2em", 100 | 距离左侧位置 |
| **right** | string/number | 如 "10px", "2em", 100 | 距离右侧位置 |
| **bottom** | string/number | 如 "10px", "2em", 100 | 距离底部位置 |
| **width** | string/number | 如 "100px", "50%", 200 | 宽度 |
| **height** | string/number | 如 "100px", "50%", 200 | 高度 |
| **zIndex** | number | 整数 | 层级顺序 |

### 6.2 定位示例

```javascript
// 绝对定位
ood.create("ood.UI.Block")
    .setPosition("absolute")
    .setTop("10px")
    .setLeft("20px")
    .setWidth("200px")
    .setHeight("100px");

// 顶部停靠
ood.create("ood.UI.Block")
    .setDock("top")
    .setHeight("50px")
    .setWidth("100%");
```

## 7. 组件生命周期

1. **创建阶段**：`ood.create()` 调用时
2. **初始化阶段**：组件属性设置
3. **渲染阶段**：`render()` 方法调用
4. **挂载阶段**：添加到DOM树
5. **更新阶段**：属性变更时
6. **销毁阶段**：`destroy()` 方法调用

## 8. 最佳实践

### 8.1 组件选择原则

1. **优先使用现有组件**：避免重复造轮子
2. **基础组件优先**：简单场景使用基础组件，复杂场景使用高级组件
3. **组件粒度适中**：根据功能划分组件，避免过大或过小
4. **高内聚低耦合**：组件内部功能完整，组件间依赖最小化

### 8.2 性能优化

1. **合理使用缓存**：避免频繁创建和销毁组件
2. **懒加载**：对于大型组件，使用懒加载机制
3. **事件委托**：减少事件监听器数量
4. **避免过度渲染**：合理使用 `refresh()` 方法

### 8.3 代码规范

1. **组件命名**：使用 PascalCase，如 `ood.UI.MenuBar`
2. **方法命名**：使用 camelCase，如 `setCaption()`
3. **属性命名**：使用 camelCase，如 `fontSize`
4. **代码结构**：遵循 `initComponents` 方法规范
5. **注释**：为复杂组件添加必要注释

## 9. 组件继承关系图

```
ood.absObj
    ↓
ood.UI
    ↓
├── 基础组件
│   ├── Div
│   ├── Span
│   ├── Button (多重继承: ood.UI.HTMLButton, ood.absValue)
│   ├── Icon
│   ├── Label
│   └── Image
    ↓
├── 高级组件
│   ├── 布局组件: Block, Layout, Panel, ContentBlock
│   ├── 导航组件: MenuBar, ToolBar, Tabs
│   ├── 数据展示: Gallery, List, TreeView, TreeGrid
│   ├── 输入组件: Input, ComboInput, CheckBox, RadioBox
│   ├── 选择器: DatePicker, TimePicker, ColorPicker
│   ├── 对话框: Dialog, MDialog, PopMenu
│   ├── 多媒体: Audio, Video, Flash
│   └── 其他: Resizer, StatusButtons, Group
```

## 10. 组件学习资源

1. **源码阅读**：
   - 基础组件：`ood/js/UI.js`
   - 高级组件：`ood/js/UI/` 目录下对应文件

2. **示例代码**：
   - 首页组件：`static/js/OODERHomePage.js`
   - 设计器组件：`static/RAD/ooddesigner.js`

3. **开发指南**：
   - 《OODER前端开发指南.md》
   - 《OODER组件定位指南.md》

## 11. 常见问题与解决方案

### 11.1 组件方法未定义

**问题**：调用组件方法时提示 "方法未定义"

**解决方案**：
1. 检查组件是否正确继承自 `ood.UI`
2. 确认方法名拼写正确
3. 查看组件源码，确认该方法是否存在
4. 检查组件版本，确认方法是否被废弃

### 11.2 组件样式不生效

**问题**：设置组件样式后不生效

**解决方案**：
1. 确认使用了正确的 `setCustomStyle` 方法
2. 检查样式选择器是否正确（使用虚拟节点 KEY, CAPTION, BORDER 等）
3. 确认样式属性名称正确
4. 检查是否有样式冲突

### 11.3 组件定位异常

**问题**：组件位置不符合预期

**解决方案**：
1. 检查 `position` 和 `dock` 属性是否冲突
2. 确认定位属性值的单位正确
3. 检查父容器的定位设置
4. 调整 `zIndex` 属性解决层级问题

## 12. 总结

OODER 组件体系设计清晰，层次分明，提供了从基础到高级的完整组件库。通过遵循本指南的组件查找规则和使用最佳实践，可以高效地开发出高质量的OODER前端应用。

组件化开发的核心思想是 "高内聚、低耦合"，合理使用OODER组件体系，可以大幅提高开发效率，降低维护成本，实现代码的复用和扩展。