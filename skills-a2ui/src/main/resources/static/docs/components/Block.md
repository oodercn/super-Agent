# Block

通用块容器组件，支持侧边栏、边框样式、主题切换和响应式布局。可用于创建可折叠的面板、带标题的内容区域和复杂的布局容器。

## 类名
`ood.UI.Block`

## 继承
`ood.UI.Widget`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Block.js"></script>

<!-- 创建块容器 -->
<div id="block-container"></div>

<script>
var block = ood.UI.Block({
    sideBarCaption: '侧边栏标题',
    sideBarType: 'left',
    sideBarStatus: 'expand',
    borderType: 'outset',
    width: '300px',
    height: '200px',
    background: '#ffffff'
}).appendTo('#block-container');
</script>
```

## 属性

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `dock` | String | `'width'` | 停靠方式：'width', 'height', 'none' |
| `position` | String | `'relative'` | CSS 定位方式 |
| `left` | String | `'auto'` | 左侧位置 |
| `top` | String | `'auto'` | 顶部位置 |
| `width` | String | `'auto'` | 宽度值 |
| `height` | String | `'18em'` | 高度值 |
| `toggleBtn` | Boolean | `true` | 是否显示切换按钮 |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |
| `refreshBtn` | Boolean | `true` | 是否显示刷新按钮 |
| `disabled` | Boolean | `false` | 是否禁用组件 |
| `tips` | String | `null` | 提示文本 |
| `comboType` | String | `'Block'` | 组合类型标识 |
| `rotate` | String | `null` | 旋转角度 |
| `iframeAutoLoad` | String | `''` | 自动加载的 iframe URL |
| `ajaxAutoLoad` | String | `''` | 自动加载的 AJAX URL |
| `selectable` | Boolean | `true` | 是否允许选择内容 |
| `html` | String | `''` | 内部 HTML 内容 |
| `borderType` | String | `'outset'` | 边框样式：'none', 'flat', 'inset', 'outset', 'groove', 'ridge' |
| `sideBarCaption` | String | `''` | 侧边栏标题文本 |
| `sideBarType` | String | `'none'` | 侧边栏位置：'none', 'left', 'right', 'top', 'bottom', 'left-top', 'left-bottom', 'right-top', 'right-bottom', 'top-left', 'top-right', 'bottom-left', 'bottom-right' |
| `sideBarStatus` | String | `'expand'` | 侧边栏状态：'expand'（展开）, 'fold'（折叠） |
| `sideBarSize` | String | `'2em'` | 侧边栏尺寸 |
| `background` | String | `''` | 背景颜色或图片 |

## 方法

### `toggleTheme(theme)`
切换主题。

**参数：**
- `theme` (String, 可选): 指定要切换的主题 ('light', 'dark', 'high-contrast')。如果不提供，则在默认主题间循环切换。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setHighContrastColor(color)`
设置高对比度主题颜色。

**参数：**
- `color` (String): 颜色值，如 '#ff0000'。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setChildren(childrens, prf)`
设置子组件。

**参数：**
- `childrens` (Array): 子组件配置数组。
- `prf` (Object, 可选): 父组件引用。

### `getAllFormValues(isAll)`
获取容器内所有表单组件的值。

**参数：**
- `isAll` (Boolean, 可选): 是否包含所有子组件的值，包括嵌套容器。

**返回：**
- (Object): 键值对形式的所有表单值。

## 事件

组件支持丰富的触摸和手势事件：

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

### 平移手势
- `pan` - 平移
- `panstart` - 平移开始
- `panmove` - 平移移动
- `panend` - 平移结束
- `pancancel` - 平移取消
- `panleft`, `panright`, `panup`, `pandown` - 特定方向平移

### 捏合手势
- `pinch` - 捏合
- `pinchstart` - 捏合开始
- `pinchmove` - 捏合移动
- `pinchend` - 捏合结束
- `pinchcancel` - 捏合取消
- `pinchin`, `pinchout` - 捏合缩放方向

### 旋转手势
- `rotate` - 旋转
- `rotatestart` - 旋转开始
- `rotatemove` - 旋转移动
- `rotateend` - 旋转结束
- `rotatecancel` - 旋转取消

### 自定义事件
- `onClickPanel` - 点击面板区域时触发
- `onFlagClick` - 点击标志按钮时触发
- `onShowTips` - 显示提示时触发

## 示例

### 创建带侧边栏的块容器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Block.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-block"></div>
    
    <script>
    // 创建块容器
    var myBlock = ood.UI.Block({
        sideBarCaption: '导航菜单',
        sideBarType: 'left',
        sideBarStatus: 'expand',
        borderType: 'groove',
        width: '400px',
        height: '300px',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        html: '<h3>主要内容区域</h3><p>这里可以放置任何内容。</p>'
    }).appendTo('#my-block');
    
    // 监听侧边栏状态变化
    myBlock.on('sideBarStatus', function(status) {
        console.log('侧边栏状态:', status);
    });
    
    // 切换主题
    myBlock.setTheme('dark');
    </script>
</body>
</html>
```

### 自动加载内容的块容器

```html
<div id="ajax-block"></div>

<script>
var ajaxBlock = ood.UI.Block({
    ajaxAutoLoad: 'data/content.html',
    width: '500px',
    height: '400px',
    borderType: 'flat'
}).appendTo('#ajax-block');
</script>
```

## 注意事项

1. 侧边栏位置类型 (`sideBarType`) 支持复合值如 'left-top'，表示侧边栏在左侧且标题在上方。
2. 边框样式 (`borderType`) 会影响内部面板的布局计算，特别是使用 'groove' 和 'ridge' 时会有额外的内边框。
3. 自动加载功能 (`iframeAutoLoad`, `ajaxAutoLoad`) 需要确保目标URL符合同源策略或支持 CORS。
4. 触摸和手势事件依赖于 Hammer.js 库，需确保已正确引入。
5. 组件支持响应式设计，但需要在移动端设备上测试布局适应性。
6. 主题切换功能依赖于 CSS 变量定义，确保主题样式表已正确加载。