# Panel

面板组件，继承自Div组件，提供带标题、边框和控制按钮的容器面板，支持主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.Panel`

## 继承
`ood.UI.Div`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Panel.js"></script>

<!-- 创建面板 -->
<div id="panel-container"></div>

<script>
var panel = ood.UI.Panel({
    caption: '普通面板',
    width: '18em',
    height: '18em',
    dock: 'none',
    toggleBtn: true,
    closeBtn: true,
    refreshBtn: true,
    theme: 'dark',
    responsive: true,
    toggle: true,
    borderType: 'inset',
    noFrame: false,
    hAlign: 'left',
    infoBtn: false,
    optBtn: false
}).appendTo('#panel-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `caption` | String | `'普通面板'` | 面板标题 |
| `dock` | String | `'none'` | 停靠方式 |
| `width` | String | `'18em'` | 面板宽度 |
| `height` | String | `'18em'` | 面板高度 |
| `toggleBtn` | Boolean | `true` | 是否显示切换按钮 |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |
| `refreshBtn` | Boolean | `true` | 是否显示刷新按钮 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'dark'` | 主题：'light'（明亮），'dark'（暗黑），'high-contrast'（高对比度） |
| `responsive` | Boolean | `true` | 响应式设计 |
| `rotate` | Number | `null` | 旋转角度 |
| `selectable` | Boolean | `true` | 是否可选择 |
| `dock` | String | `'fill'` | 停靠方式 |
| `caption` | String | `undefined` | 标题文本 |
| `html` | String | `''` | HTML内容 |
| `toggle` | Boolean | `true` | 是否可切换 |
| `image` | String | `''` | 图像URL |
| `imagePos` | String | `''` | 图像位置 |
| `imageBgSize` | String | `''` | 图像背景大小 |
| `imageClass` | String | `''` | 图像类名 |
| `iconFontCode` | String | `''` | 图标字体代码 |
| `borderType` | String | `'inset'` | 边框类型：'none', 'flat', 'inset', 'outset' |
| `noFrame` | Boolean | `false` | 无框架模式 |
| `hAlign` | String | `'left'` | 水平对齐：'left', 'center', 'right' |
| `toggleIcon` | String | `'ood-uicmd-toggle'` | 切换图标：'ood-uicmd-toggle', 'ood-uicmd-check' |
| `infoBtn` | Boolean | `false` | 信息按钮 |
| `optBtn` | Boolean | `false` | 选项按钮 |

## 方法

### `activate()`
激活面板，使标题获得焦点。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getAllFormValues(isAll)`
获取面板内所有表单元素的值。

**参数：**
- `isAll` (Boolean): 是否包含所有值

**返回：**
- (Object): 表单值对象

### `resetPanelView(removeChildren, destroyChildren)`
重置面板视图。

**参数：**
- `removeChildren` (Boolean): 是否移除子元素，默认true
- `destroyChildren` (Boolean): 是否销毁子元素，默认true

**返回：**
- (Object): 组件实例，支持链式调用。

### `iniPanelView()`
初始化面板视图。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setTheme(theme)`
设置主题样式。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `PanelTrigger()`
面板触发器，初始化现代化功能。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

继承Div组件的所有事件。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `panel-dark` | 暗黑主题样式 |
| `panel-hc` | 高对比度主题样式 |

## 示例

### 基本面板

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Panel.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-panel"></div>
    
    <script>
    var myPanel = ood.UI.Panel({
        caption: '用户信息',
        width: '400px',
        height: '300px',
        theme: 'light',
        toggleBtn: true,
        closeBtn: true,
        borderType: 'inset'
    }).appendTo('#my-panel');
    
    // 设置面板内容
    myPanel.properties.html = '<div style="padding:20px">欢迎使用面板组件</div>';
    myPanel.refresh();
    </script>
</body>
</html>
```

### 响应式面板组

```html
<div class="panel-group">
    <div id="panel-left"></div>
    <div id="panel-right"></div>
</div>

<style>
.panel-group {
    display: flex;
    gap: 20px;
}
@media (max-width: 768px) {
    .panel-group {
        flex-direction: column;
    }
}
</style>

<script>
// 创建多个面板实例
var leftPanel = ood.UI.Panel({
    width: '100%',
    height: '350px',
    caption: '左侧面板',
    theme: 'dark',
    responsive: true
}).appendTo('#panel-left');

var rightPanel = ood.UI.Panel({
    width: '100%',
    height: '350px',
    caption: '右侧面板',
    theme: 'dark',
    responsive: true
}).appendTo('#panel-right');

// 响应式调整
window.addEventListener('resize', function() {
    leftPanel.adjustLayout();
    rightPanel.adjustLayout();
});
</script>
```

### 可折叠面板

```html
<div id="collapsible-panel"></div>

<script>
var collapsiblePanel = ood.UI.Panel({
    caption: '可折叠面板',
    width: '500px',
    height: 'auto',
    toggleBtn: true,
    toggle: true,
    theme: 'light'
}).appendTo('#collapsible-panel');

// 设置折叠状态
collapsiblePanel.properties.toggle = false;
collapsiblePanel.refresh();
</script>
```

## 注意事项

1. **停靠方式**：`dock`属性控制面板在容器中的停靠方式，支持'none'、'fill'等值。

2. **边框类型**：`borderType`属性支持四种边框样式：'none'（无边框）、'flat'（扁平边框）、'inset'（内凹边框）、'outset'（外凸边框）。

3. **按钮控制**：通过`toggleBtn`、`closeBtn`、`refreshBtn`、`infoBtn`、`optBtn`等属性控制面板上显示的按钮。

4. **主题系统**：支持三种主题：light、dark、high-contrast，主题设置自动保存到localStorage。

5. **响应式设计**：根据屏幕宽度自动调整布局，优化不同设备的显示效果。

6. **可访问性**：自动添加ARIA属性，支持键盘导航和屏幕阅读器。

7. **内容设置**：通过`html`属性设置面板内容，支持动态更新。

8. **折叠功能**：通过`toggle`属性控制面板的折叠/展开状态。

9. **图像支持**：支持通过`image`、`imageClass`等属性设置面板图标。

10. **继承功能**：完全继承Div组件的所有功能和特性。