# ButtonLayout

按钮布局组件，用于创建网格状按钮容器，支持主题切换、响应式设计和可访问性增强。常用于导航菜单、功能入口等场景。

## 类名
`ood.UI.ButtonLayout`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ButtonLayout.js"></script>

<!-- 创建按钮布局 -->
<div id="buttonlayout-container"></div>

<script>
var buttonLayout = ood.UI.ButtonLayout({
    items: [
        {id: 'home', caption: '首页', comment: '返回首页', imageClass: 'ri-home-line'},
        {id: 'settings', caption: '设置', comment: '系统设置', imageClass: 'ri-settings-line'},
        {id: 'profile', caption: '个人资料', comment: '查看和编辑个人信息', imageClass: 'ri-user-line'}
    ],
    columns: 3,
    theme: 'light',
    responsive: true,
    width: '500px',
    height: '200px'
}).appendTo('#buttonlayout-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `value` | String | `'mywork'` | 当前选中的按钮ID |
| `showDirtyMark` | Boolean | `false` | 是否显示脏数据标记 |
| `items` | Array | 预定义的5个按钮 | 按钮项数组，每个项包含 id、caption、comment、imageClass 等 |
| `dock` | String | `'fill'` | 停靠方式 |
| `borderType` | String | `'none'` | 边框样式：'none', 'flat', 'inset', 'outset' |
| `iconFontSize` | String | `'2em'` | 图标字体大小 |
| `columns` | Number | `5` | 列数（0表示自动） |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `autoColumns` | Boolean | `false` | 是否自动调整列数 |
| `tagCmds` | String | `null` | 标签命令 |
| `expression` | String | `''` | 表达式 |
| `bgimg` | String | `null` | 背景图片 |
| `tagCmdsAlign` | String | `null` | 标签命令对齐方式 |
| `flagText` | String | `null` | 标志文本 |
| `flagClass` | String | `null` | 标志CSS类 |
| `flagStyle` | String | `null` | 标志样式 |
| `autoFontColor` | Boolean | `false` | 自动字体颜色 |
| `autoIconColor` | Boolean | `true` | 自动图标颜色 |
| `autoItemColor` | Boolean | `false` | 自动项目颜色 |
| `iconColors` | String | `null` | 图标颜色列表 |
| `itemColors` | String | `null` | 项目颜色列表 |
| `fontColors` | String | `null` | 字体颜色列表 |
| `autoImgSize` | Boolean | `false` | 自动图片大小 |
| `autoItemSize` | Boolean | `true` | 自动项目大小 |
| `iconOnly` | Boolean | `false` | 仅显示图标（隐藏标题和注释） |
| `iconFontSize` | String | `''` | 图标字体大小（覆盖默认） |
| `itemMargin` | Number | `6` | 项目外边距（像素） |
| `itemPadding` | Number | `2` | 项目内边距（像素） |
| `itemWidth` | Number | `32` | 项目宽度（像素） |
| `itemHeight` | Number | `32` | 项目高度（像素） |
| `imgWidth` | Number | `16` | 图片宽度（像素） |
| `imgHeight` | Number | `16` | 图片高度（像素） |
| `width` | String | `'16rem'` | 组件宽度 |
| `height` | String | `'16rem'` | 组件高度 |
| `columns` | Number | `0` | 列数（0表示自动） |
| `rows` | Number | `0` | 行数（0表示自动） |

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

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateItemData(profile, item)`
更新按钮项数据并刷新显示。

**参数：**
- `profile` (Object): 组件profile对象
- `item` (Object): 按钮项数据

### `_afterInsertItems(profile)`
在插入按钮项后执行的操作（内部方法）。

## 事件

组件支持以下事件：

### 触摸事件
- `touchstart` - 触摸开始
- `touchmove` - 触摸移动
- `touchend` - 触摸结束
- `touchcancel` - 触摸取消

### 滑动手势
- `swipe` - 滑动（任意方向）
- `swipeleft` - 向左滑动
- `swiperight` - 向右滑动
- `swipeup` - 向上滑动
- `swipedown` - 向下滑动

### 按压手势
- `press` - 长按开始
- `pressup` - 长按结束

### 自定义事件
- `onCmd` - 按钮命令触发时调用
- `onFlagClick` - 点击标志按钮时触发

## 示例

### 基本按钮布局

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ButtonLayout.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-buttons"></div>
    
    <script>
    // 创建按钮布局
    var buttons = ood.UI.ButtonLayout({
        items: [
            {id: 'dashboard', caption: '仪表盘', comment: '查看数据概览', imageClass: 'ri-dashboard-line'},
            {id: 'analytics', caption: '分析', comment: '数据分析和报表', imageClass: 'ri-bar-chart-line'},
            {id: 'reports', caption: '报告', comment: '生成和查看报告', imageClass: 'ri-file-text-line'},
            {id: 'settings', caption: '设置', comment: '系统配置', imageClass: 'ri-settings-line'}
        ],
        columns: 2,
        theme: 'dark',
        responsive: true,
        width: '600px',
        height: '300px'
    }).appendTo('#my-buttons');
    
    // 监听按钮点击
    buttons.on('click', function(buttonId) {
        console.log('按钮被点击:', buttonId);
    });
    
    // 切换主题
    buttons.toggleDarkMode();
    </script>
</body>
</html>
```

### 响应式按钮布局

```html
<div id="responsive-buttons"></div>

<script>
var responsiveButtons = ood.UI.ButtonLayout({
    items: [
        {id: 'mail', caption: '邮件', imageClass: 'ri-mail-line'},
        {id: 'calendar', caption: '日历', imageClass: 'ri-calendar-line'},
        {id: 'contacts', caption: '联系人', imageClass: 'ri-contacts-line'},
        {id: 'tasks', caption: '任务', imageClass: 'ri-task-line'},
        {id: 'notes', caption: '笔记', imageClass: 'ri-sticky-note-line'}
    ],
    autoColumns: true,
    responsive: true,
    width: '100%',
    height: '150px'
}).appendTo('#responsive-buttons');
</script>
```

## 注意事项

1. 组件默认包含5个预定义按钮（首页、待办、起草、我的、消息），可通过 `items` 属性完全自定义。
2. 启用 `autoColumns` 时，组件会根据屏幕宽度自动调整列数，最小为2列。
3. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
4. 响应式设计通过CSS媒体查询和JavaScript动态调整实现，确保在不同设备上的良好显示。
5. 可访问性增强功能会自动为按钮添加 ARIA 属性，支持键盘导航（Tab、Enter、Space）。
6. 图标使用 Remix Icon 字体库，确保在 `imageClass` 属性中使用正确的类名（如 'ri-home-line'）。
7. 组件支持触摸手势操作，包括滑动、长按等，适合移动端使用。