# Gallery

图片库组件，用于展示和管理图片集合，支持主题切换、响应式设计和可访问性增强。适用于相册、产品展示、媒体库等场景。

## 类名
`ood.UI.Gallery`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Gallery.js"></script>

<!-- 创建图片库 -->
<div id="gallery-container"></div>

<script>
var gallery = ood.UI.Gallery({
    items: [
        {id: 'photo1', caption: '风景照1', imageClass: 'ri-image-line', desc: '美丽的自然风光'},
        {id: 'photo2', caption: '风景照2', imageClass: 'ri-image-2-line', desc: '山水之间'},
        {id: 'photo3', caption: '人像照', imageClass: 'ri-user-line', desc: '专业人像摄影'},
        {id: 'photo4', caption: '建筑照', imageClass: 'ri-building-line', desc: '现代建筑设计'}
    ],
    theme: 'light',
    responsive: true,
    autoColumns: false,
    width: '800px',
    height: '500px'
}).appendTo('#gallery-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 4个预定义图片项 | 图片项数组，每个项包含 id、caption、imageClass、desc 等 |
| `value` | String | `'item1'` | 当前选中的项ID |
| `width` | String | `'32em'` | 组件宽度 |
| `height` | String | `'20em'` | 组件高度 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | Object | `{ini: 'light', listbox: ['light', 'dark']}` | 主题设置 |
| `responsive` | Object | `{ini: true}` | 响应式布局设置 |
| `autoColumns` | Object | `{ini: false}` | 自动调整列数 |
| `tagCmds` | Object | `{ini: null}` | 标签命令 |
| `expression` | Object | `{ini: ''}` | 表达式 |
| `bgimg` | Object | `{ini: null}` | 背景图片 |
| `iotStatus` | Object | `{ini: null}` | 物联网状态 |
| `tagCmdsAlign` | Object | `{ini: null}` | 标签命令对齐方式 |
| `flagText` | Object | `{ini: null}` | 标记文本 |
| `flagClass` | Object | `{ini: null}` | 标记样式类 |
| `flagStyle` | Object | `{ini: null}` | 标记样式 |
| `iconColors` | Object | `{ini: null}` | 图标颜色列表 |
| `itemColors` | Object | `{ini: null}` | 项目颜色列表 |
| `fontColors` | Object | `{ini: null}` | 字体颜色列表 |
| `autoFontColor` | Object | `{ini: false}` | 自动字体颜色 |
| `autoIconColor` | Object | `{ini: true}` | 自动图标颜色 |
| `autoItemColor` | Object | `{ini: false}` | 自动项目颜色 |
| `autoImgSize` | Object | `{ini: false}` | 自动图片大小 |
| `autoItemSize` | Object | `{ini: true}` | 自动项目大小 |
| `iconOnly` | Object | `{ini: false}` | 仅显示图标 |
| `iconFontSize` | Object | `{ini: ''}` | 图标字体大小 |
| `backgroundColor` | Object | `{ini: ''}` | 背景颜色 |
| `itemMargin` | Object | `{ini: 6}` | 项目外边距（像素） |
| `itemPadding` | Object | `{ini: 2}` | 项目内边距（像素） |
| `itemWidth` | Object | `{$spaceunit: 1, ini: 32}` | 项目宽度（像素） |
| `itemHeight` | Object | `{$spaceunit: 1, ini: 32}` | 项目高度（像素） |
| `imgWidth` | Object | `{ini: 16}` | 图片宽度（像素） |
| `imgHeight` | Object | `{ini: 16}` | 图片高度（像素） |
| `width` | Object | `{$spaceunit: 1, ini: '16rem'}` | 组件宽度 |
| `height` | Object | `{$spaceunit: 1, ini: '16rem'}` | 组件高度 |
| `columns` | Object | `{ini: 0}` | 列数（0表示自动） |
| `rows` | Object | `{ini: 0}` | 行数（0表示自动） |

## 方法

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleDarkMode()`
在亮色和暗色主题之间切换。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局，针对不同设备优化显示效果。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `GalleryTrigger()`
现代化功能初始化触发器，自动设置主题、响应式布局和可访问性。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getStatus(id)`
获取指定ID项的状态。

**参数：**
- `id` (String): 项ID

**返回：**
- (String): 状态值：'ini' 或其他自定义状态

### `updateItemData(profile, item)`
更新项数据并刷新显示。

**参数：**
- `profile` (Object): 组件profile对象
- `item` (Object): 项数据

**返回：**
- (Object): 组件实例，支持链式调用。

### `_afterInsertItems(profile)` (内部方法)
在插入项后执行的操作，修复Firefox浏览器bug。

### `_prepareData(profile)` (内部方法)
准备渲染数据。

### `_prepareItem(profile, item, oitem, pid, index, len)` (内部方法)
准备单个项数据。

## 事件

### 自定义事件处理器 (EventHandlers)

| 事件名 | 参数 | 描述 |
|--------|------|------|
| `onCmd` | - | 命令触发时调用 |
| `onFlagClick` | `profile, item, e, src` | 点击标记按钮时触发 |
| `touchstart` | `profile, item, e, src` | 触摸开始 |
| `touchmove` | `profile, item, e, src` | 触摸移动 |
| `touchend` | `profile, item, e, src` | 触摸结束 |
| `touchcancel` | `profile, item, e, src` | 触摸取消 |
| `swipe` | `profile, item, e, src` | 滑动（任意方向） |
| `swipeleft` | `profile, item, e, src` | 向左滑动 |
| `swiperight` | `profile, item, e, src` | 向右滑动 |
| `swipeup` | `profile, item, e, src` | 向上滑动 |
| `swipedown` | `profile, item, e, src` | 向下滑动 |
| `press` | `profile, item, e, src` | 长按开始 |
| `pressup` | `profile, item, e, src` | 长按结束 |

### 渲染触发器 (RenderTrigger)
组件渲染时自动调用的初始化函数，负责：
1. 设置背景颜色
2. 执行 `_afterInsertItems` 方法
3. 触发现代化功能初始化（`GalleryTrigger`）

## 示例

### 基本图片库

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Gallery.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-gallery"></div>
    
    <script>
    // 创建图片库
    var gallery = ood.UI.Gallery({
        items: [
            {id: 'img1', caption: '日出', imageClass: 'ri-sun-line', desc: '壮观的日出景象'},
            {id: 'img2', caption: '海滩', imageClass: 'ri-beach-line', desc: '宁静的海滩日落'},
            {id: 'img3', caption: '森林', imageClass: 'ri-tree-line', desc: '茂密的森林小径'},
            {id: 'img4', caption: '雪山', imageClass: 'ri-mountain-line', desc: '雄伟的雪山峰顶'}
        ],
        theme: 'dark',
        responsive: true,
        autoColumns: true,
        width: '900px',
        height: '600px'
    }).appendTo('#my-gallery');
    
    // 监听图片点击
    gallery.on('click', function(itemId) {
        console.log('图片被点击:', itemId);
    });
    
    // 切换主题
    gallery.toggleDarkMode();
    </script>
</body>
</html>
```

### 响应式图片库

```html
<div id="responsive-gallery"></div>

<script>
var responsiveGallery = ood.UI.Gallery({
    items: [
        {id: 'p1', caption: '产品1', imageClass: 'ri-product-hunt-line'},
        {id: 'p2', caption: '产品2', imageClass: 'ri-shopping-bag-line'},
        {id: 'p3', caption: '产品3', imageClass: 'ri-store-line'},
        {id: 'p4', caption: '产品4', imageClass: 'ri-gift-line'},
        {id: 'p5', caption: '产品5', imageClass: 'ri-price-tag-3-line'},
        {id: 'p6', caption: '产品6', imageClass: 'ri-trophy-line'}
    ],
    autoColumns: true,
    responsive: true,
    theme: 'light',
    width: '100%',
    height: 'auto',
    itemMargin: 10,
    itemPadding: 8
}).appendTo('#responsive-gallery');

// 窗口大小变化时重新调整布局
window.addEventListener('resize', function() {
    responsiveGallery.adjustLayout();
});
</script>
```

### 图标库模式

```html
<div id="icon-gallery"></div>

<script>
var iconGallery = ood.UI.Gallery({
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'settings', caption: '设置', imageClass: 'ri-settings-line'},
        {id: 'user', caption: '用户', imageClass: 'ri-user-line'},
        {id: 'mail', caption: '邮件', imageClass: 'ri-mail-line'},
        {id: 'calendar', caption: '日历', imageClass: 'ri-calendar-line'},
        {id: 'file', caption: '文件', imageClass: 'ri-file-text-line'},
        {id: 'image', caption: '图片', imageClass: 'ri-image-line'},
        {id: 'video', caption: '视频', imageClass: 'ri-video-line'}
    ],
    iconOnly: true,
    columns: 4,
    theme: 'light',
    width: '400px',
    height: '300px',
    iconFontSize: '24px',
    iconColors: '#007bff,#28a745,#dc3545,#ffc107,#17a2b8,#6c757d,#343a40'
}).appendTo('#icon-gallery');
</script>
```

## 注意事项

1. **主题系统**：
   - 支持亮色（light）和暗色（dark）主题
   - 主题设置自动保存到 localStorage（键名：'gallery-theme'）
   - 支持通过CSS变量和data-theme属性实现主题切换

2. **响应式设计**：
   - 支持自动调整列数（`autoColumns: true`）
   - 根据屏幕宽度动态调整项目大小和间距
   - 针对移动端、平板和桌面设备优化显示

3. **可访问性**：
   - 自动添加ARIA角色（grid、gridcell）
   - 为屏幕阅读器提供语义化标签
   - 支持键盘导航（Tab、方向键）
   - 装饰性图标自动添加 `aria-hidden="true"`

4. **颜色系统**：
   - 支持自动图标颜色（`autoIconColor: true`）
   - 支持自定义颜色列表（`iconColors`、`itemColors`、`fontColors`）
   - 背景颜色可通过 `backgroundColor` 属性设置

5. **显示模式**：
   - 正常模式：显示图标、标题和描述
   - 图标模式（`iconOnly: true`）：仅显示图标，隐藏标题和描述
   - 支持自定义图标字体类（`imageClass`）

6. **布局控制**：
   - 支持固定列数（`columns` 属性）
   - 支持固定行数（`rows` 属性）
   - 支持项目尺寸自定义（`itemWidth`、`itemHeight`、`imgWidth`、`imgHeight`）

7. **性能优化**：
   - 大量图片时建议使用懒加载
   - 图标字体性能优于图片图标
   - 响应式布局通过CSS媒体查询和JavaScript动态调整实现