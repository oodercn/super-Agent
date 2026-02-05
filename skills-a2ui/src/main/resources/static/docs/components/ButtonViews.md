# ButtonViews

按钮视图组件，继承自Tabs，提供按钮样式的标签页导航，支持侧边栏布局、主题切换和响应式设计。

## 类名
`ood.UI.ButtonViews`

## 继承
`ood.UI.Tabs`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ButtonViews.js"></script>

<!-- 创建按钮视图 -->
<div id="buttonviews-container"></div>

<script>
var buttonViews = ood.UI.ButtonViews({
    items: [
        {id: 'tab1', caption: '标签页1', imageClass: 'ri-file-line'},
        {id: 'tab2', caption: '标签页2', imageClass: 'ri-folder-line'},
        {id: 'tab3', caption: '标签页3', imageClass: 'ri-settings-line'}
    ],
    barLocation: 'left',
    barSize: '220px',
    value: 'tab1',
    theme: 'light',
    width: '800px',
    height: '400px'
}).appendTo('#buttonviews-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `barLocation` | String | `'left'` | 标签栏位置：'left', 'right', 'top', 'bottom' |
| `items` | Array | 4个示例项 | 标签项数组，每个项包含 id、caption、imageClass 等 |
| `autoItemColor` | Boolean | `true` | 自动项目颜色 |
| `barSize` | Number | `220` | 标签栏大小（像素） |
| `value` | String | `'a'` | 当前选中的标签ID |
| `caption` | String | `'$RAD.widgets.navBar'` | 组件标题 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `HAlign` | String | `null` | 水平对齐方式（兼容性属性） |
| `barLocation` | String | `'top'` | 标签栏位置：'top', 'bottom', 'left', 'right' |
| `barHAlign` | String | `'left'` | 标签栏水平对齐：'left', 'center', 'right' |
| `barVAlign` | String | `'top'` | 标签栏垂直对齐：'top', 'bottom' |
| `barSize` | Number | `'2.5em'` | 标签栏大小（支持单位） |
| `borderType` | String | `'none'` | 边框样式：'none', 'flat', 'inset', 'outset' |
| `noFoldBar` | Boolean | `false` | 是否隐藏折叠栏 |
| `sideBarStatus` | String | `'expand'` | 侧边栏状态：'expand'（展开）, 'fold'（折叠） |
| `sideBarSize` | String | `'3.25em'` | 侧边栏大小 |

## 方法

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleDarkMode()`
切换暗黑模式（在light和dark之间切换）。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustResponsive()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，为按钮添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

组件支持以下事件：

### 标签切换事件
- `change` - 当前标签改变时触发
- `beforechange` - 标签切换前触发，可取消

### 按钮事件
- `click` - 按钮点击时触发

### 自定义事件
- 可通过 `onCmd` 等自定义事件处理器

## 示例

### 左侧标签栏布局

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ButtonViews.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="nav-container"></div>
    
    <script>
    // 创建按钮视图
    var nav = ood.UI.ButtonViews({
        items: [
            {id: 'overview', caption: '概览', imageClass: 'ri-dashboard-line'},
            {id: 'projects', caption: '项目', image: 'projects.png'},
            {id: 'team', caption: '团队', imageClass: 'ri-team-line'},
            {id: 'documents', caption: '文档', closeBtn: true}
        ],
        barLocation: 'left',
        barSize: 250,
        value: 'overview',
        theme: 'dark',
        width: '1000px',
        height: '600px'
    }).appendTo('#nav-container');
    
    // 监听标签切换
    nav.on('change', function(tabId) {
        console.log('切换到标签:', tabId);
    });
    </script>
</body>
</html>
```

### 响应式顶部标签栏

```html
<div id="responsive-tabs"></div>

<script>
var responsiveTabs = ood.UI.ButtonViews({
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'news', caption: '新闻', imageClass: 'ri-newspaper-line'},
        {id: 'about', caption: '关于', imageClass: 'ri-information-line'},
        {id: 'contact', caption: '联系', imageClass: 'ri-contacts-line'}
    ],
    barLocation: 'top',
    responsive: true,
    width: '100%',
    height: '500px'
}).appendTo('#responsive-tabs');
</script>
```

## 注意事项

1. 组件继承自Tabs，因此具有Tabs的所有功能和事件。
2. 标签栏位置 (`barLocation`) 支持四个方向：top、bottom、left、right。
3. 当标签栏在左侧或右侧时，会自动启用垂直布局（块状显示）。
4. 主题切换功能会自动保存到 localStorage，确保用户偏好持久化。
5. 可访问性增强功能会自动为按钮添加 `role="button"`、`aria-label` 等属性，并支持键盘操作（空格键、回车键）。
6. 响应式设计通过CSS类（`ood-mobile`、`ood-tiny`）实现，可根据屏幕宽度自动调整样式。
7. 标签项支持多种图标方式：`imageClass`（字体图标）、`image`（图片URL）、`icon`（背景图片）。
8. 可通过 `closeBtn`、`optBtn`、`popBtn` 等属性为标签项添加操作按钮。