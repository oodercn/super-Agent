# OODER 前端开发指南

## 1. 项目概述

OODER 是一个基于 OOD JS 框架开发的低代码设计器，用于快速构建 Web 应用程序。设计器提供了可视化的界面，允许开发者通过拖拽组件、设置属性和编写事件处理逻辑来创建应用程序，无需手动编写大量代码。

### 1.1 核心功能

- 可视化组件拖拽和放置
- 实时属性编辑和预览
- 事件处理和动作配置
- 多设备视图支持（移动设备、平板、桌面）
- SVG 路径编辑和定制
- 代码生成和导出
- 撤销/重做功能
- 主题切换支持

### 1.2 技术栈

- 基于 OOD JS 框架
- JavaScript
- HTML5
- CSS3
- SVG

## 2. 项目结构

### 2.1 主要文件和目录

```
RAD/
├── api/              # API 配置相关组件
├── custom/           # 自定义组件
├── db/               # 数据库相关组件
├── expression/       # 表达式编辑器
├── img/              # 图片资源
├── org/              # 组织管理相关组件
├── project/          # 项目管理相关组件
├── resource/         # 资源管理相关组件
├── server/           # 服务器配置相关组件
├── OODDesigner.js    # 设计器入口文件
├── Designer.js       # 主设计器画布
├── conf.js           # 配置文件
├── conf_widgets.js   # 组件配置
└── index.js          # 应用入口

ood/                  # OOD JS 框架
├── Locale/           # 国际化资源
├── ThirdParty/       # 第三方库
├── appearance/       # 主题外观
├── css/              # CSS 样式
├── iconfont/         # 图标字体
├── img/              # 图片资源
└── js/               # 核心 JavaScript 文件
    ├── UI/           # UI 组件库
    ├── mobile/       # 移动组件
    ├── Module.js     # 模块系统
    ├── UI.js         # UI 核心
    └── ood.js        # 框架入口
```

### 2.2 核心文件说明

#### 2.2.1 OODDesigner.js

设计器的入口文件，定义了 `RAD.OODDesigner` 类，负责管理设计器的整体功能，包括：

- 项目管理
- 文件操作
- 组件管理
- 代码生成
- 事件处理

#### 2.2.2 Designer.js

主设计器画布，定义了 `RAD.Designer` 类，负责：

- 组件的拖拽和放置
- 属性编辑
- 组件选择和操作
- 多设备视图切换
- SVG 路径编辑
- 撤销/重做功能

#### 2.2.3 ood.js

OOD JS 框架的核心文件，提供了：

- 模块化系统
- UI 组件库
- 事件处理机制
- DOM 操作工具
- 动画效果

## 3. 核心概念

### 3.1 模块（Module）

OOD 框架的核心概念，所有组件都是模块的实例。模块提供了：

- 生命周期管理
- 属性和事件系统
- 组件嵌套和组合
- 继承机制

### 3.2 组件（Component）

可视化界面元素，如按钮、输入框、表格等。组件可以通过拖拽的方式添加到设计器画布中，并通过属性面板进行配置。

### 3.3 属性（Property）

组件的配置项，如尺寸、颜色、文本内容等。属性可以在设计器的属性面板中进行编辑，实时反映在画布上。

### 3.4 事件（Event）

组件的交互行为，如点击、鼠标移动、键盘输入等。事件可以绑定动作，如显示对话框、发送请求等。

### 3.5 动作（Action）

事件触发时执行的操作，如页面跳转、数据请求、组件操作等。

### 3.6 视图（View）

设计器支持多种设备视图，如移动设备、平板和桌面。每种视图都有不同的分辨率和交互方式。

## 4. 设计器架构

### 4.1 核心类关系

```
RAD.OODDesigner
└── RAD.JSEditor
    └── RAD.Designer
        ├── 画布（Canvas）
        ├── 属性面板（Property Grid）
        ├── 工具栏（Toolbar）
        ├── 组件库（Component Library）
        └── 事件编辑器（Event Editor）
```

### 4.2 设计器工作流程

1. **初始化**：加载设计器配置和组件库
2. **项目加载**：打开或创建项目
3. **组件添加**：从组件库拖拽组件到画布
4. **属性配置**：在属性面板中编辑组件属性
5. **事件绑定**：为组件添加事件和动作
6. **预览和测试**：在不同设备视图下预览应用
7. **代码生成**：生成可执行的应用代码
8. **项目保存**：保存项目配置和代码

## 5. 组件开发

### 5.1 组件类型

OODER 设计器支持多种类型的组件：

- **基础组件**：按钮、输入框、标签等
- **布局组件**：面板、表格、网格等
- **数据组件**：列表、表格、图表等
- **交互组件**：对话框、菜单、选项卡等
- **媒体组件**：图片、音频、视频等
- **SVG 组件**：矢量图形、路径等

### 5.2 组件注册

组件需要注册到设计器中才能在组件库中显示。注册方式如下：

```javascript
// 在 conf_widgets.js 中注册组件
CONF.widgets.push({
    key: 'ood.UI.Button',  // 组件类名
    caption: 'Button',     // 显示名称
    imageClass: 'ri-button-line',  // 图标
    category: '基础组件',  // 分类
    iniProp: {             // 初始属性
        caption: 'Button',
        width: 100,
        height: 30
    },
    iniEvents: {}          // 初始事件
});
```

### 5.3 组件开发流程

1. **定义组件类**：继承自 OOD 框架的基础组件类
2. **实现组件逻辑**：包括属性、事件和方法
3. **注册组件**：将组件添加到设计器配置中
4. **测试组件**：在设计器中测试组件的功能和外观
5. **优化组件**：根据测试结果进行调整和优化

## 6. 组件结构体系

OODER 框架采用层次分明的组件体系，所有组件均继承自 `ood.UI` 基类，分为基础组件和高级组件两大类。

### 6.1 组件体系概述

1. **基础组件**：直接封装 HTML 原生元素，提供基础 UI 交互功能，定义在 `ood/js/UI.js` 中
2. **高级组件**：基于基础组件构建的复合组件，提供复杂的业务功能，定义在 `ood/js/UI/` 目录下的独立文件中

### 6.2 组件查找规则

1. **基础组件**：直接在 `ood/js/UI.js` 中查找，包括 Div, Span, Button, Icon, Label, Image
2. **高级组件**：在 `ood/js/UI/` 目录下查找对应文件，如 `ood/js/UI/MenuBar.js` 对应 `ood.UI.MenuBar`

### 6.3 组件创建与使用

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

### 6.4 完整组件结构指南

详细的组件结构体系、分类、属性和使用方法，请参考独立文档：

- **《OODER组件结构指南.md》**：完整的组件结构体系文档，包括组件分类、属性、使用方法和最佳实践

### 6.5 组件继承关系

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

## 7. 事件处理

### 7.1 事件类型

OODER 设计器支持多种事件类型：

- **鼠标事件**：点击、双击、鼠标移动等
- **键盘事件**：按键按下、释放等
- **表单事件**：提交、改变、聚焦等
- **组件事件**：加载完成、数据更新等
- **自定义事件**：开发者定义的事件

### 6.2 事件绑定

在设计器中，可以通过事件编辑器为组件绑定事件和动作。事件处理逻辑可以是：

- 简单的 JavaScript 代码
- 调用组件方法
- 页面跳转
- 数据请求
- 显示/隐藏组件

### 6.3 事件处理示例

```javascript
// 按钮点击事件处理
function onButtonClick() {
    // 显示对话框
    ood.showDialog({
        title: '提示',
        content: '按钮被点击了！',
        buttons: [
            { caption: '确定', onClick: function() { this.hide(); } }
        ]
    });
}
```

## 8. 代码生成

### 7.1 代码结构

设计器生成的代码遵循 OOD 框架的模块化结构，包括：

- 组件初始化代码
- 属性配置代码
- 事件绑定代码
- 自定义方法代码

### 7.2 代码示例

```javascript
ood.Class('view.Index', 'ood.Module', {
    Instance: {
        iniComponents: function() {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function(child) {
                children.push(child.get(0))
            };

            append(new ood.UI.Button()
                .setHost(host, "button1")
                .setLeft(50)
                .setTop(50)
                .setWidth(100)
                .setHeight(30)
                .setCaption("Button")
                .onClick("button1_onclick")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        button1_onclick: function(profile, e, src) {
            ood.showDialog({
                title: '提示',
                content: '按钮被点击了！',
                buttons: [
                    { caption: '确定', onClick: function() { this.hide(); } }
                ]
            });
        }
    }
});
```

## 9. 多设备视图支持

### 8.1 视图类型

设计器支持多种设备视图：

- **移动设备**：iPhone SE、iPhone 12、iPhone 12 Pro Max、Samsung S21 等
- **平板设备**：iPad、iPad Air、iPad Pro 等
- **桌面设备**：HD 720p、WXGA、FHD 1080p、QHD 1440p、4K 2160p 等
- **经典分辨率**：SVGA、XGA、SXGA 等

### 8.2 视图切换

在设计器中，可以通过工具栏的视图切换按钮或右键菜单来切换不同的设备视图。切换视图后，画布会调整为对应设备的分辨率和交互方式。

### 8.3 响应式设计

设计器支持响应式设计，组件可以根据不同设备的屏幕尺寸自动调整布局和大小。开发者可以为不同的设备视图设置不同的属性值。

## 10. SVG 路径编辑

### 9.1 SVG 组件支持

设计器支持 SVG 组件的编辑，包括：

- 基本形状（矩形、圆形、椭圆等）
- 路径（Path）
- 文本
- 组（Group）

### 9.2 SVG 路径编辑功能

- **路径绘制**：通过拖拽绘制 SVG 路径
- **路径编辑**：调整路径的控制点和形状
- **路径变换**：平移、旋转、缩放、镜像等
- **路径样式**：设置填充颜色、描边颜色、线宽等

### 9.3 SVG 编辑示例

```javascript
// SVG 路径组件
append(new ood.svg.Path()
    .setHost(host, "path1")
    .setLeft(50)
    .setTop(50)
    .setWidth(200)
    .setHeight(200)
    .setPath("M 50 50 L 150 50 L 150 150 L 50 150 Z")
    .setFillColor("#ff0000")
    .setStrokeColor("#000000")
    .setStrokeWidth(2)
);
```

## 11. 主题切换

### 10.1 主题类型

设计器支持多种主题：

- **蓝色主题**（blue）
- **深色主题**（dark）
- **绿色主题**（green）
- **高对比度主题**（high-contrast）
- **浅色主题**（light）
- **紫色主题**（purple）

### 10.2 主题切换方法

```javascript
// 切换主题
ood.theme.setTheme('dark');
```

### 10.3 自定义主题

开发者可以创建自定义主题，包括：

- 定义主题颜色变量
- 创建主题 CSS 文件
- 注册主题到设计器

## 12. 最佳实践

### 11.1 核心开发规则

1. **iniComponents 方法规范**：
   - `iniComponents` 是 OODER 基础的组件组织规范，必须严格遵守
   - 所有组件初始化代码必须放在此方法中
   - 方法应返回组件数组，用于组件管理和生命周期控制

2. **组件属性和方法检查**：
   - **属性名称验证**：不同组件可能使用不同的属性名称（如 MenuBar 使用 `border` 而非 `borderType`），必须检查组件的 DataModel 定义
   - **方法存在性检查**：在使用组件方法前，必须确认该方法存在于组件中
   - **DataModel 参考**：组件的所有可配置属性都在其 DataModel 中定义，使用前必须查看对应组件的 DataModel
   - **只读属性处理**：某些属性是只读的（如 MenuBar 的 `height` 属性），必须通过 `setCustomStyle` 等方式设置
   - **set* 方法匹配**：set* 方法必须与组件 DataModel 中的属性名称匹配
   - **常见属性对照表**：
     | 组件类型 | 正确属性名 | 错误属性名 |
     |---------|-----------|-----------|
     | MenuBar | border    | borderType |
     | MenuBar | width     | 无（只读，通过 setCustomStyle 设置） |
     | MenuBar | height    | 无（只读，通过 setCustomStyle 设置） |
   - **检查方法**：
     1. 查看组件文件中的 DataModel 定义
     2. 检查组件的 Instance.iniProp 对象
     3. 避免假设所有组件都有相同的属性和方法
     4. 使用浏览器开发者工具检查组件实例的属性
   - **避免调用不存在的方法**：调用不存在的方法会导致运行时错误，必须确保方法存在
   - **优先使用标准组件**：标准组件的属性和方法有更完善的文档和支持

3. **虚拟 DOM 和模板理解**：
   - 预先了解组件的虚拟 DOM 结构和模板
   - 将样式修改设定到指定的虚拟节点下
   - 样式嵌套关系必须与组件 Templates 属性匹配
   - 正确使用虚拟节点名称（如 CAPTION、KEY、BORDER）设置样式
   - 对于组件不支持的属性，使用 `setCustomStyle` 方法设置

4. **ood.merge 方法使用规则**：
   - **正确用法**：用于合并属性对象，创建新的属性集合
   - **错误用法**：直接修改组件的 properties 对象
   - **标准模板**：
     ```javascript
     var host = this, children = [], properties = {}, append = function(child) {
         children.push(child.get(0))
     };
     ood.merge(properties, host.properties);
     ```
   - **使用场景**：
     - 在 `iniComponents` 方法中合并组件属性
     - 创建新的属性集合用于组件初始化
     - 避免直接修改组件的原始 properties 对象
   - **注意事项**：
     - 第一个参数是目标对象，第二个参数是源对象
     - 不会修改源对象，而是将源对象的属性合并到目标对象
     - 用于创建组件初始化所需的属性集合

5. **组件复用原则**：
   - 尽可能使用已有组件完成界面绘制
   - 避免重复开发相似功能的组件
   - 优先使用 OOD 框架提供的标准组件
   - 合理组合现有组件实现复杂功能

6. **组件定位和分层规则**：
   - **核心定位属性**：
     | 属性名 | 类型 | 说明 | 取值范围 | 示例 |
     |--------|------|------|----------|------|
     | dock | 字符串 | 组件停靠方式 | top, bottom, left, right, center, fill | `.setDock("top")` |
     | position | 字符串 | 定位模式 | static, absolute, relative, fixed | `.setPosition("static")` |
     | top | 字符串/数值 | 顶部偏移量 | 带单位字符串（em, px）或数值 | `.setTop("0.75em")` |
     | left | 字符串/数值 | 左侧偏移量 | 带单位字符串（em, px）或数值 | `.setLeft("10.6em")` |
     | right | 字符串/数值 | 右侧偏移量 | 带单位字符串（em, px）或数值 | `.setRight("0em")` |
     | width | 字符串/数值 | 组件宽度 | 带单位字符串（em, px）、百分比或数值 | `.setWidth("16em")` |
     | height | 字符串/数值 | 组件高度 | 带单位字符串（em, px）、百分比或数值 | `.setHeight("5em")` |
     | zIndex | 数值 | 组件堆叠顺序 | 整数 | `.setZIndex(10)` |
   - **辅助定位属性**：
     | 属性名 | 类型 | 说明 | 示例 |
     |--------|------|------|------|
     | visibility | 字符串 | 组件可见性 | `.setVisibility("visible")` |
     | borderType | 字符串 | 组件边框类型 | `.setBorderType("none")` |
     | overflow | 字符串 | 内容溢出处理方式 | `.setOverflow("hidden")` |
     | panelBgClr | 字符串 | 面板背景颜色 | `.setPanelBgClr("#3498DB")` |
     | hAlign | 字符串 | 水平对齐方式 | `.setHAlign("center")` |
   - **定位模式详解**：
     - **Dock定位**：用于页面级布局，如顶部导航栏、侧边栏等，组件会自动填充父容器对应边缘
     - **绝对定位**：用于容器内精确定位，如图标、按钮等，需要手动设置top、left、width、height等
     - **静态定位**：用于容器内流式布局，如文本、内联元素等，按照文档流顺序排列
   - **组件层次结构设计**：
     - 使用 `ood.UI.Block` 作为容器，统一管理子组件
     - 顶层组件使用 `dock` 属性进行页面级布局
     - 容器内的子组件使用 `top`、`left`、`right` 等属性进行精确定位
     - 相关组件组织在同一个容器内，便于管理和维护
     - 重要组件设置较高 `zIndex`，确保正确显示层级
   - **定位最佳实践**：
     - 优先使用 `dock` 定位进行页面级布局
     - 容器内组件使用绝对定位或静态定位
     - 合理设置组件的 `zIndex`，确保正确的显示层级
     - 组件命名规范，便于后续维护
     - 统一使用合适的尺寸单位（优先使用em）
     - 遵循组件创建流程，确保代码结构清晰
   - **组件创建流程**：
     1. **创建组件**：使用 `ood.create("ood.UI.ComponentType")`
     2. **设置宿主和名称**：`.setHost(host, "componentName").setName("componentName")`
     3. **设置定位属性**：根据需求设置 `dock`、`position`、`top`、`left` 等
     4. **设置样式属性**：设置 `background`、`borderType`、`visibility` 等
     5. **添加到父容器**：使用 `parentComponent.append(component)`
   - **常见定位问题及解决方案**：
     | 问题 | 原因 | 解决方案 |
     |------|------|----------|
     | 组件显示在错误位置 | 定位属性设置错误 | 检查 `top`、`left`、`dock` 等属性值 |
     | 组件被其他组件遮挡 | `zIndex` 过低 | 提高组件的 `zIndex` 值 |
     | 组件大小不符合预期 | 未设置或设置错误的 `width`/`height` | 明确设置组件的宽度和高度 |
     | 组件位置随页面滚动变化 | 定位模式选择错误 | 根据需求选择 `dock` 或 `fixed` 定位 |
     | 子组件超出父容器 | 父容器 `overflow` 设置错误 | 设置父容器 `.setOverflow("hidden")` 或调整子组件位置 |

### 11.2 组件设计原则

- **单一职责**：每个组件只负责一个功能
- **可复用性**：组件应该可以在不同场景下复用
- **可配置性**：组件应该提供丰富的配置选项
- **性能优化**：组件应该高效运行，避免不必要的渲染
- **可测试性**：组件应该易于测试和调试

### 11.3 代码组织

- 使用模块化设计，将相关功能组织到同一个模块中
- 遵循命名规范，使用清晰的变量和方法名
- 添加必要的注释，提高代码可读性
- 避免重复代码，使用函数和工具类封装通用逻辑

### 11.4 性能优化

- 减少组件数量，避免过度复杂化界面
- 合理使用缓存，避免重复计算
- 优化事件处理，避免频繁触发重绘
- 使用懒加载，只在需要时加载组件和资源

### 11.5 测试和调试

- 在不同设备视图下测试应用
- 使用浏览器开发者工具调试代码
- 测试组件的边界情况和异常情况
- 进行性能测试，优化应用响应速度

## 13. 开发环境搭建

### 12.1 环境要求

- 现代浏览器（Chrome、Firefox、Safari、Edge）
- Web 服务器（如 Apache、Nginx 或 Node.js）
- 文本编辑器或 IDE（如 VS Code、Sublime Text、WebStorm）

### 12.2 部署步骤

1. 将项目文件部署到 Web 服务器
2. 确保 OOD JS 框架文件正确引用
3. 在浏览器中访问设计器入口页面
4. 开始使用设计器创建应用

## 14. 常见问题和解决方案

### 13.1 组件拖拽问题

**问题**：组件无法拖拽到画布上

**解决方案**：
- 检查浏览器是否支持 HTML5 拖拽功能
- 确保设计器初始化完成
- 检查组件库配置是否正确

### 13.2 属性不生效

**问题**：修改组件属性后，画布上没有变化

**解决方案**：
- 检查属性名称是否正确
- 确保属性值符合要求
- 检查组件是否支持该属性
- 尝试刷新设计器

### 13.3 事件不触发

**问题**：绑定的事件没有触发

**解决方案**：
- 检查事件名称是否正确
- 确保事件绑定代码正确
- 检查组件是否支持该事件
- 检查浏览器控制台是否有错误信息

### 13.4 性能问题

**问题**：设计器运行缓慢

**解决方案**：
- 减少画布上的组件数量
- 关闭不必要的视图和面板
- 优化组件代码，减少重绘和回流
- 升级浏览器到最新版本

## 15. 总结

OODER 低代码设计器是一个功能强大的工具，可以帮助开发者快速构建 Web 应用程序。通过可视化的界面和丰富的组件库，开发者可以专注于应用程序的业务逻辑，而无需手动编写大量代码。

本指南介绍了 OODER 设计器的核心概念、架构和开发流程，希望能帮助开发者更好地理解和使用设计器。随着技术的不断发展，OODER 设计器也将不断更新和完善，提供更多强大的功能和更好的用户体验。

## 16. 附录

### 15.1 常用 API 参考

#### 15.1.1 ood.Class

用于定义类的方法

```javascript
ood.Class(className, superClassName, classDefinition);
```

#### 15.1.2 ood.create

用于创建组件实例的方法

```javascript
ood.create(componentClass, properties, events);
```

#### 15.1.3 ood.showDialog

用于显示对话框的方法

```javascript
ood.showDialog(options);
```

#### 15.1.4 ood.message

用于显示消息提示的方法

```javascript
ood.message(content, type);
```

### 15.2 快捷键参考

| 快捷键 | 功能 |
|--------|------|
| Ctrl + C | 复制组件 |
| Ctrl + V | 粘贴组件 |
| Ctrl + X | 剪切组件 |
| Delete | 删除组件 |
| Ctrl + Z | 撤销操作 |
| Ctrl + Y | 重做操作 |
| Ctrl + A | 全选组件 |
| Esc | 取消选择 |

### 15.3 资源链接

- OOD JS 框架文档
- OODER 设计器官方网站
- 组件库参考文档
- 示例项目和模板

---

**OODER 前端开发指南**

*版本：1.0*
*日期：2025-11-27*
*作者：OODER 开发团队*