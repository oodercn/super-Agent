# FoldingTabs

可折叠标签页组件，继承自Tabs组件，提供可折叠/展开的标签页功能，支持多种主题、响应式布局和可访问性增强。

## 类名
`ood.UI.FoldingTabs`

## 继承
`ood.UI.Tabs`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/FoldingTabs.js"></script>

<!-- 创建可折叠标签页 -->
<div id="foldingtabs-container"></div>

<script>
var foldingTabs = ood.UI.FoldingTabs({
    items: [
        {id: 'a', caption: '标签页1', message: "正常标签页"},
        {id: 'b', caption: '标签页2', message: "带图标", imageClass: "ri-image-line"},
        {id: 'c', caption: '标签页3', message: "自定义高度", height: 100},
        {id: 'd', caption: '可折叠标签页', message: "带命令按钮", closeBtn: true, optBtn: 'ood-uicmd-opt', popBtn: true}
    ],
    value: 'a',
    theme: 'light',
    responsive: true,
    collapsible: true,
    enhancedAccessibility: true
}).appendTo('#foldingtabs-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[...]` | 标签页项目数组，包含id、caption、message等属性 |
| `value` | String | `'a'` | 当前选中的标签页ID |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `iconColors` | Object | `null` | 图标颜色配置 |
| `itemColors` | Object | `null` | 项目颜色配置 |
| `fontColors` | Object | `null` | 字体颜色配置 |
| `autoFontColor` | Boolean | `false` | 自动字体颜色 |
| `autoIconColor` | Boolean | `true` | 自动图标颜色 |
| `autoItemColor` | Boolean | `false` | 自动项目颜色 |
| `$hborder` | Number | `0` | 水平边框宽度 |
| `$vborder` | Number | `0` | 垂直边框宽度 |
| `noPanel` | Boolean | `null` | 无面板模式 |
| `noHandler` | Boolean | `null` | 无处理器模式 |
| `HAlign` | String | `null` | 水平对齐方式 |
| `selMode` | String | `'single'` | 选择模式：'single'（单选），'multi'（多选） |
| `theme` | String | `'light'` | 主题：'light'（明亮），'dark'（暗黑），'high-contrast'（高对比度） |
| `responsive` | Boolean | `true` | 响应式布局 |
| `transitionDuration` | String | `'normal'` | 过渡动画持续时间：'fast'（快速），'normal'（正常），'slow'（慢速） |
| `collapsible` | Boolean | `true` | 是否可折叠 |
| `enhancedAccessibility` | Boolean | `true` | 可访问性增强 |

**注意：** FoldingTabs组件继承所有Tabs组件的属性。

## 方法

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

### `toggleTheme()`
切换主题（在light、dark、high-contrast之间循环）。

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

### `_updateUIValue(box, value, profile, item, event, source, checktype)`
更新UI值（内部方法）。

### `_setBackgroundInfo(item, properties)`
设置项目背景信息（内部方法）。

### `_setOverflowStyle(item, properties)`
设置溢出样式（内部方法）。

### `_setItemAccessibility(profile, item)`
设置项目可访问性属性（内部方法）。

### `_setContainerAccessibility(root)`
设置容器可访问性属性（内部方法）。

### `_initAnimationStyles()`
初始化动画样式（内部方法）。

### `_initTheme(properties)`
初始化主题（内部方法）。

### `_applyResponsiveClasses(root, viewportWidth)`
应用响应式布局类（内部方法）。

### `_prepareItems(profile, arr, pid)`
准备项目集合（内部方法）。

## 事件

继承Tabs组件的所有事件，并添加折叠/展开相关事件。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `foldingtabs-themed` | 主题化样式基础类 |
| `foldingtabs-dark` | 暗黑主题样式 |
| `foldingtabs-hc` | 高对比度主题样式 |

## 示例

### 基本可折叠标签页

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/FoldingTabs.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-foldingtabs"></div>
    
    <script>
    var myFoldingTabs = ood.UI.FoldingTabs({
        width: '800px',
        height: '400px',
        items: [
            {id: 'tab1', caption: '基本信息', message: "显示基本信息面板"},
            {id: 'tab2', caption: '详细设置', message: "显示详细设置选项"},
            {id: 'tab3', caption: '高级选项', message: "显示高级配置选项"}
        ],
        value: 'tab1',
        theme: 'light'
    }).appendTo('#my-foldingtabs');
    </script>
</body>
</html>
```

### 响应式可折叠标签页组

```html
<div class="foldingtabs-group">
    <div id="settings-tabs"></div>
    <div id="preview-tabs"></div>
</div>

<script>
// 创建多个可折叠标签页实例
var settingsTabs = ood.UI.FoldingTabs({
    width: '100%',
    height: '300px',
    responsive: true,
    theme: 'dark',
    collapsible: true,
    items: [
        {id: 'general', caption: '常规设置', message: "常规系统设置"},
        {id: 'security', caption: '安全设置', message: "安全与权限配置"},
        {id: 'appearance', caption: '外观设置', message: "界面主题与样式"}
    ]
}).appendTo('#settings-tabs');

var previewTabs = ood.UI.FoldingTabs({
    width: '100%',
    height: '300px',
    responsive: true,
    theme: 'dark',
    collapsible: true,
    items: [
        {id: 'desktop', caption: '桌面预览', message: "桌面端界面预览"},
        {id: 'mobile', caption: '移动端预览', message: "移动设备界面预览"},
        {id: 'tablet', caption: '平板预览', message: "平板设备界面预览"}
    ]
}).appendTo('#preview-tabs');

// 响应式调整
window.addEventListener('resize', function() {
    settingsTabs.adjustLayout();
    previewTabs.adjustLayout();
});
</script>
```

## 注意事项

1. **折叠功能**：`collapsible`属性控制是否启用折叠功能，默认为true。

2. **主题系统**：支持三种主题：light、dark、high-contrast，主题设置自动保存到localStorage。

3. **响应式设计**：根据屏幕宽度自动调整布局，优化不同设备的显示效果。

4. **可访问性**：自动添加ARIA属性，支持键盘导航和屏幕阅读器。

5. **动画效果**：折叠/展开时有平滑的过渡动画，持续时间可通过`transitionDuration`调整。

6. **颜色配置**：支持自动颜色配置（autoFontColor、autoIconColor、autoItemColor）。

7. **选择模式**：支持单选（single）和多选（multi）两种模式。

8. **继承功能**：完全继承Tabs组件的所有功能和特性。