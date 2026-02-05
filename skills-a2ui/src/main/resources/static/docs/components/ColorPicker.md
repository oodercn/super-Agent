# ColorPicker

颜色选择器组件，提供直观的颜色选择界面，支持简单色板、RGB/HSV/HEX 数值调整，以及高级调色板功能。常用于图形编辑、主题配置等场景。

## 类名
`ood.UI.ColorPicker`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ColorPicker.js"></script>

<!-- 创建颜色选择器 -->
<div id="colorpicker-container"></div>

<script>
var colorPicker = ood.UI.ColorPicker({
    value: 'FF0000',
    theme: 'light',
    responsive: true,
    barDisplay: true,
    closeBtn: true,
    advance: false,
    width: '400px',
    height: '300px'
}).appendTo('#colorpicker-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `height` | String | `'auto'` | 组件高度（只读） |
| `width` | String | `'auto'` | 组件宽度（只读） |
| `value` | String | `'FFFFFF'` | 当前颜色值（HEX格式，不带#） |
| `barDisplay` | Boolean | `true` | 是否显示顶部工具栏 |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |
| `advance` | Boolean | `false` | 是否显示高级调色板 |

## 方法

### `activate()`
激活颜色选择器，将焦点设置到切换按钮。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getColorName()`
获取当前颜色的名称（如果有）。

**返回：**
- (String): 颜色名称，如果没有则返回空字符串。

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

### `ColorPickerTrigger()`
初始化颜色选择器触发器（内部方法），用于设置初始主题和响应式布局。

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

### 基本颜色选择器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ColorPicker.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-colorpicker"></div>
    
    <script>
    // 创建颜色选择器
    var picker = ood.UI.ColorPicker({
        value: '00FF00',
        theme: 'light',
        responsive: true,
        barDisplay: true,
        closeBtn: true,
        advance: false,
        width: '400px',
        height: '300px'
    }).appendTo('#my-colorpicker');
    
    // 监听颜色变化
    picker.on('change', function(colorValue) {
        console.log('颜色已改变:', colorValue);
    });
    
    // 切换主题
    picker.toggleDarkMode();
    </script>
</body>
</html>
```

### 带高级调色板的颜色选择器

```html
<div id="advanced-colorpicker"></div>

<script>
var advancedPicker = ood.UI.ColorPicker({
    value: 'FF8800',
    theme: 'dark',
    responsive: true,
    barDisplay: true,
    closeBtn: true,
    advance: true,
    width: '600px',
    height: '400px'
}).appendTo('#advanced-colorpicker');
</script>
```

## 注意事项

1. 组件默认显示简单色板，包含常用颜色预定义值。
2. 启用 `advance` 属性时，显示高级调色板，支持更精细的颜色调整。
3. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
4. 响应式设计通过CSS媒体查询和JavaScript动态调整实现，确保在不同设备上的良好显示。
5. 可访问性增强功能会自动为交互元素添加 ARIA 属性，支持键盘导航。
6. 颜色值使用 HEX 格式（6个字符，不带#），例如 'FF0000' 表示红色。
7. 组件支持触摸手势操作，适合移动端使用。