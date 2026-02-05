# MenuBar

菜单栏组件，用于创建水平导航菜单，支持多级下拉菜单、主题切换、响应式设计和可访问性增强。常用于应用顶部导航。

## 类名
`ood.UI.MenuBar`

## 继承
`ood.UI` 和 `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MenuBar.js"></script>

<!-- 创建菜单栏 -->
<div id="menubar-container"></div>

<script>
var menuBar = ood.UI.MenuBar({
    items: [
        {
            id: 'file',
            caption: '文件',
            sub: [
                {id: 'new', caption: '新建'},
                {id: 'open', caption: '打开'},
                {type: 'split'},
                {id: 'save', caption: '保存'},
                {id: 'saveas', caption: '另存为'}
            ]
        },
        {
            id: 'edit',
            caption: '编辑',
            sub: [
                {id: 'undo', caption: '撤销'},
                {id: 'redo', caption: '重做'},
                {type: 'split'},
                {id: 'cut', caption: '剪切'},
                {id: 'copy', caption: '复制'},
                {id: 'paste', caption: '粘贴'}
            ]
        },
        {id: 'view', caption: '视图'},
        {id: 'help', caption: '帮助'}
    ],
    theme: 'light',
    responsive: true,
    dock: 'top',
    width: '100%'
}).appendTo('#menubar-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 预定义的菜单项数组 | 菜单项配置数组，包含id、caption、sub（子菜单）等 |
| `theme` | String | `'light'` | 主题：'light', 'dark', 'moonify', 'lightblue', 'darkblue' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `handler` | Boolean | `true` | 是否显示拖动句柄 |
| `hAlign` | String | `'left'` | 水平对齐：'left', 'center', 'right' |
| `vAlign` | String | `'middle'` | 垂直对齐：'top', 'middle', 'bottom' |
| `dock` | String | `'top'` | 停靠位置：'top', 'bottom', 'left', 'right' |
| `border` | String | `'outset'` | 边框样式：'none', 'flat', 'inset', 'outset', 'groove', 'ridge' |
| `autoShowTime` | Number | `200` | 鼠标悬停自动显示子菜单的延迟时间（毫秒） |
| `value` | String | `''` | 当前选中的菜单项ID |
| `autoItemColor` | Boolean | `false` | 是否自动设置菜单项颜色 |
| `autoIconColor` | Boolean | `false` | 是否自动设置图标颜色 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题，支持切换操作 |
| `responsive` | Boolean | `true` | 响应式设计开关 |
| `listKey` | String | `null` | 列表键 |
| `dragSortable` | String | `null` | 是否可拖拽排序 |
| `autoTips` | Boolean | `false` | 自动提示 |
| `expression` | String | `''` | 表达式 |
| `autoFontColor` | Boolean | `false` | 自动字体颜色 |
| `autoIconColor` | Boolean | `false` | 自动图标颜色 |
| `autoItemColor` | Boolean | `false` | 自动菜单项颜色 |
| `iconColors` | String | `null` | 图标颜色列表 |
| `itemColors` | String | `null` | 菜单项颜色列表 |
| `fontColors` | String | `null` | 字体颜色列表 |
| `value` | String | `''` | 当前值 |
| `height` | String | `'auto'` | 高度 |
| `width` | String | `'auto'` | 宽度 |
| `border` | String | `'outset'` | 边框样式 |
| `parentID` | String | `''` | 父容器ID |
| `hAlign` | String | `'left'` | 水平对齐 |
| `vAlign` | String | `'middle'` | 垂直对齐 |
| `$hborder` | Number | `1` | 水平边框宽度 |
| `$vborder` | Number | `1` | 垂直边框宽度 |
| `left` | Number | `0` | 左侧位置 |
| `top` | Number | `0` | 顶部位置 |
| `autoShowTime` | Number | `200` | 自动显示时间 |
| `handler` | Boolean | `true` | 拖动句柄 |
| `position` | String | `'absolute'` | 定位方式 |
| `dock` | String | `'top'` | 停靠位置 |

## 方法

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'moonify', 'lightblue', 'darkblue'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleTheme(nextTheme)`
切换主题。如果指定了nextTheme，则切换到指定主题；否则循环切换。

**参数：**
- `nextTheme` (String, 可选): 要切换到的主题名称

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。在小屏幕设备上调整菜单项间距和字体大小。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateItem(subId, options)`
更新指定ID的菜单项配置。

**参数：**
- `subId` (String): 菜单项ID
- `options` (Object): 新的配置选项

**返回：**
- (Object): 组件实例，支持链式调用。

### `hide(ignoreEffects)`
隐藏当前显示的下拉菜单。

**参数：**
- `ignoreEffects` (Boolean, 可选): 是否忽略动画效果

**返回：**
- (Object): 组件实例，支持链式调用。

### `clearPopCache()`
清除下拉菜单缓存。

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

组件支持以下事件处理程序：

### `onGetPopMenu(profile, item, callback)`
获取下拉菜单数据时触发。可用于动态加载子菜单。

### `onMenuBtnClick(profile, item, src)`
菜单按钮点击时触发。

### `beforePopMenu(profile, item, src)`
显示下拉菜单前触发，返回false可阻止显示。

### `onShowSubMenu(profile, popProfile, item, src)`
显示子菜单时触发。

### `onMenuSelected(profile, popProfile, item, src)`
菜单项被选中时触发。

## 示例

### 基本菜单栏

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/MenuBar.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="main-menu"></div>
    
    <script>
    // 创建菜单栏
    var menuBar = ood.UI.MenuBar({
        items: [
            {
                id: 'file',
                caption: '文件',
                sub: [
                    {id: 'new', caption: '新建', imageClass: 'ri-file-add-line'},
                    {id: 'open', caption: '打开', imageClass: 'ri-folder-open-line'},
                    {type: 'split'},
                    {id: 'save', caption: '保存', imageClass: 'ri-save-line'},
                    {id: 'saveas', caption: '另存为', imageClass: 'ri-save-2-line'}
                ]
            },
            {
                id: 'edit',
                caption: '编辑',
                sub: [
                    {id: 'undo', caption: '撤销', imageClass: 'ri-arrow-go-back-line'},
                    {id: 'redo', caption: '重做', imageClass: 'ri-arrow-go-forward-line'}
                ]
            },
            {id: 'view', caption: '视图'},
            {id: 'help', caption: '帮助'}
        ],
        theme: 'dark',
        responsive: true,
        dock: 'top',
        width: '100%',
        height: '40px'
    }).appendTo('#main-menu');
    
    // 监听菜单选择
    menuBar.on('onMenuSelected', function(profile, popProfile, item, src) {
        console.log('菜单项被选择:', item.id, item.caption);
    });
    </script>
</body>
</html>
```

### 响应式菜单栏

```html
<div id="responsive-menu"></div>

<script>
var responsiveMenu = ood.UI.MenuBar({
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'products', caption: '产品', sub: [
            {id: 'web', caption: 'Web应用'},
            {id: 'mobile', caption: '移动应用'},
            {id: 'desktop', caption: '桌面应用'}
        ]},
        {id: 'services', caption: '服务'},
        {id: 'about', caption: '关于我们'},
        {id: 'contact', caption: '联系我们'}
    ],
    theme: 'light',
    responsive: true,
    autoShowTime: 300,
    hAlign: 'center',
    width: '100%'
}).appendTo('#responsive-menu');

// 窗口大小变化时调整布局
$(window).on('resize', function() {
    responsiveMenu.adjustLayout();
});
</script>
```

## 注意事项

1. 组件默认包含一个示例菜单项（menu1），包含普通项、禁用项、带图标项、分隔线和复选框等不同类型。
2. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
3. 响应式设计根据屏幕宽度自动调整布局：
   - 小于768px：移动端模式，调整间距和字体大小
   - 小于480px：超小屏幕模式，隐藏图标，进一步缩小字体
4. 可访问性增强功能会自动为菜单栏添加 ARIA 属性，支持键盘导航（Tab、方向键、Enter）。
5. 下拉菜单使用独立的 PopMenu 组件实现，支持最小宽度、圆角、阴影等现代化样式。
6. 菜单项支持多种类型：普通项、分隔线（type: 'split'）、复选框（type: 'checkbox'）。
7. 组件支持鼠标悬停自动显示子菜单（通过 autoShowTime 控制延迟时间）。
8. 图标使用 Remix Icon 字体库，确保在配置中使用正确的类名（如 'ri-image-line'）。