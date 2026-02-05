# ContentBlock

内容块组件，用于显示具有标题、日期、标志和图片的内容项目列表。支持多列布局、响应式设计、主题切换和自动颜色调整。常用于新闻列表、消息中心、内容展示等场景。

## 类名
`ood.UI.ContentBlock`

## 继承
`ood.UI.List`, `ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ContentBlock.js"></script>

<!-- 创建内容块 -->
<div id="contentblock-container"></div>

<script>
var contentBlock = ood.UI.ContentBlock({
    items: [
        {
            id: "item1",
            title: "上报信息",
            datetime: "2024-9-9 12:00",
            flagClass: "ri-arrow-left-right-line",
            flagText: "管理员",
            caption: "item1"
        },
        {
            id: "item2",
            title: "信息报送",
            datetime: "2024-9-9 12:00",
            flagClass: "ri ri-drag-move-line",
            flagText: "管理员",
            caption: "item2"
        }
    ],
    dock: "fill",
    selMode: "none",
    borderType: "none",
    columns: 1,
    theme: "light",
    responsive: true
}).appendTo('#contentblock-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `flagText` | String | `null` | 标志文本（可为每个项目单独设置） |
| `flagClass` | String | `null` | 标志样式类（CSS类名） |
| `flagStyle` | String | `null` | 标志样式（内联样式） |
| `iconColors` | String | `null` | 图标颜色（CSS颜色值） |
| `itemColors` | String | `null` | 项目背景颜色（CSS颜色值） |
| `fontColors` | String | `null` | 字体颜色（CSS颜色值） |
| `moreColors` | String | `null` | “更多”按钮颜色（CSS颜色值） |
| `autoFontColor` | Boolean | `false` | 是否自动调整字体颜色（根据背景色） |
| `autoIconColor` | Boolean | `false` | 是否自动调整图标颜色 |
| `autoItemColor` | Boolean | `false` | 是否自动调整项目颜色 |
| `autoImgSize` | Boolean | `false` | 是否自动调整图片大小 |
| `autoItemSize` | Boolean | `true` | 是否自动调整项目大小 |
| `iconFontSize` | String | `'1.5em'` | 图标字体大小（CSS尺寸） |
| `itemMargin` | Number | `0` | 项目外边距（CSS margin值） |
| `itemPadding` | Number | `0` | 项目内边距（CSS padding值） |
| `itemWidth` | Number | `32` | 项目宽度（单位：像素） |
| `itemHeight` | Number | `32` | 项目高度（单位：像素） |
| `width` | String | `'16rem'` | 组件宽度（CSS尺寸） |
| `height` | String | `'16rem'` | 组件高度（CSS尺寸） |
| `columns` | Number | `0` | 列数（0表示自动） |
| `rows` | Number | `0` | 行数（0表示自动） |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[...]` | 内容项目数组，每个项目包含 id, title, datetime, flagClass, flagText, caption 等字段 |
| `dock` | String | `'fill'` | 布局位置：'fill', 'left', 'right', 'top', 'bottom' |
| `selMode` | String | `'none'` | 选择模式：'none', 'single', 'multiple' |
| `borderType` | String | `'none'` | 边框类型：'none', 'solid', 'dashed' |
| `columns` | Number | `1` | 列数（初始化默认值） |

## 方法

### `setTheme(theme)`
设置组件主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateItemData(profile, item)`
更新单个项目的数据并刷新显示。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 项目数据对象

**返回：**
- (Object): 组件实例，支持链式调用。

### `_afterInsertItems(profile)`
插入项目后触发的内部方法，用于处理图片背景等特殊逻辑。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 组件实例，支持链式调用。

### `_prepareData(profile)`
准备渲染数据的内部方法，处理自动颜色调整等。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 渲染数据对象。

### `_prepareItem(profile, item, oitem, pid, index, len)`
准备单个项目渲染数据的内部方法。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 项目数据对象
- `oitem` (Object): 原始项目数据
- `pid` (String): 父项目ID
- `index` (Number): 项目索引
- `len` (Number): 项目总数

**返回：**
- (Object): 处理后的项目数据。

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
- `onMoreClick` - 点击“更多”按钮时触发
- `onTitleClick` - 点击标题时触发

## 示例

### 基本内容块

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ContentBlock.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-contentblock"></div>
    
    <script>
    // 创建内容块
    var block = ood.UI.ContentBlock({
        items: [
            {
                id: "news1",
                title: "重要通知",
                datetime: "2024-10-01 09:00",
                flagClass: "ri-notification-line",
                flagText: "新",
                caption: "系统升级通知"
            },
            {
                id: "news2",
                title: "系统更新",
                datetime: "2024-10-02 14:30",
                flagClass: "ri-update-line",
                flagText: "更新",
                caption: "版本1.2.0发布"
            }
        ],
        theme: "light",
        responsive: true,
        columns: 2
    }).appendTo('#my-contentblock');
    
    // 监听更多按钮点击
    block.on('onMoreClick', function(profile, item) {
        console.log('更多按钮点击:', item);
    });
    </script>
</body>
</html>
```

### 暗色主题内容块

```html
<div id="dark-contentblock"></div>

<script>
var darkBlock = ood.UI.ContentBlock({
    items: [
        {
            id: "alert1",
            title: "安全警报",
            datetime: "2024-10-03 16:45",
            flagClass: "ri-alert-line",
            flagText: "紧急",
            caption: "检测到异常登录"
        }
    ],
    theme: "dark",
    responsive: true,
    autoFontColor: true,
    autoItemColor: true
}).appendTo('#dark-contentblock');
</script>
```

### 响应式多列布局

```html
<div id="responsive-contentblock"></div>

<script>
var responsiveBlock = ood.UI.ContentBlock({
    items: [
        // 多个项目...
    ],
    theme: "light",
    responsive: true,
    columns: 3,
    itemMargin: "10px",
    itemPadding: "15px"
}).appendTo('#responsive-contentblock');
</script>
```

## 注意事项

1. 组件继承自 `ood.UI.List`，因此支持列表组件的所有基本功能，包括项目选择、滚动等。
2. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
3. 启用 `responsive` 属性时，组件会根据屏幕宽度自动调整列数和布局。
4. 自动颜色调整功能（autoFontColor、autoIconColor、autoItemColor）可以根据背景色智能调整文本和图标颜色，确保可读性。
5. 项目数据中的 `flagClass` 支持 Remix Icon 等图标字体类名，用于显示项目标志图标。
6. `columns` 和 `rows` 属性可以控制网格布局，设置为 0 表示自动调整。
7. 组件支持触摸手势操作，适合移动端使用。
8. 图片背景处理包含对 Firefox 浏览器的特殊兼容性修复。