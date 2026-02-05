# Slider

滑块组件，用于选择数值范围或单个值，支持水平和垂直方向、主题切换、响应式设计和可访问性增强。支持单滑块和范围滑块两种模式。

## 类名
`ood.UI.Slider`

## 继承
`ood.UI` 和 `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Slider.js"></script>

<!-- 创建滑块 -->
<div id="slider-container"></div>

<script>
var slider = ood.UI.Slider({
    value: '30:70',
    width: '300px',
    height: '4em',
    theme: 'light',
    responsive: true,
    type: 'horizontal',
    isRange: true,
    showIncreaseHandle: true,
    showDecreaseHandle: true,
    labelPos: 'top',
    labelSize: '6em',
    labelCaption: '滑块值',
    labelHAlign: 'left'
}).appendTo('#slider-container');

// 获取当前值
var currentValue = slider.value();
// 设置新值
slider.value('40:80');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `labelSize` | String | `'6em'` | 标签区域大小 |
| `height` | String | `'4em'` | 组件高度 |
| `labelPos` | String | `'top'` | 标签位置：'none', 'left', 'top', 'right', 'bottom' |
| `caption` | String | `'$RAD.widgets.slider'` | 标题文本（资源键） |
| `labelHAlign` | String | `'left'` | 标签水平对齐：'', 'left', 'center', 'right' |
| `isRange` | Boolean | `false` | 是否为范围滑块（双滑块） |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `position` | String | `'absolute'` | 定位方式 |
| `expression` | String | `''` | 表达式（保留） |
| `width` | String | `'15em'` | 组件宽度（带空间单位） |
| `height` | String | `'4em'` | 组件高度（带空间单位） |
| `precision` | Number | `0` | 数值精度（小数点位数） |
| `numberTpl` | String | `'* - 1% ~ 2%'` | 数值显示模板：`*` 表示标签文本，`1` 表示起始值，`2` 表示结束值 |
| `steps` | Number | `0` | 步数（0表示连续，>0表示离散步长） |
| `value` | String | `'0:0'` | 当前值（单值：'30'，范围值：'30:70'） |
| `type` | String | `'horizontal'` | 滑块方向：'horizontal'（水平），'vertical'（垂直） |
| `isRange` | Boolean | `true` | 是否为范围滑块 |
| `showIncreaseHandle` | Boolean | `true` | 是否显示增加手柄 |
| `showDecreaseHandle` | Boolean | `true` | 是否显示减少手柄 |
| `labelSize` | String | `0` | 标签区域大小（带空间单位） |
| `labelPos` | String | `'left'` | 标签位置：'none', 'left', 'top', 'right', 'bottom' |
| `labelGap` | String | `4` | 标签与滑块之间的间距（带空间单位） |
| `labelCaption` | String | `''` | 标签文本 |
| `labelHAlign` | String | `'right'` | 标签水平对齐：'', 'left', 'center', 'right' |
| `labelVAlign` | String | `'top'` | 标签垂直对齐：'', 'top', 'middle', 'bottom' |

## 方法

### `_setCtrlValue(value)`
设置滑块值并更新显示。

**参数：**
- `value` (String|Array): 滑块值（单值：'30'，范围值：'30:70' 或数组 [30,70]）

**返回：**
- (Object): 组件实例，支持链式调用。

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
根据屏幕尺寸调整响应式布局。在小屏幕设备上优化显示。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

### `onLabelClick(profile, e, src)`
标签点击事件。

### `onLabelDblClick(profile, e, src)`
标签双击事件。

### `onLabelActive(profile, e, src)`
标签激活事件。

## CSS 变量 (Appearances)

组件支持以下CSS自定义属性，可在主题中覆盖：

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `--ood-slider-track-bg` | 滑块轨道背景色 | 主题定义 |
| `--ood-slider-border-radius` | 滑块边框圆角 | 主题定义 |
| `--ood-slider-track-border` | 滑块轨道边框 | 主题定义 |
| `--ood-slider-height` | 滑块轨道高度 | 主题定义 |
| `--ood-slider-fill-bg` | 滑块填充区域背景 | 主题定义 |
| `--ood-slider-thumb-shadow` | 滑块拇指阴影 | 主题定义 |
| `--ood-slider-thumb-bg` | 滑块拇指背景色 | 主题定义 |
| `--ood-slider-thumb-border` | 滑块拇指边框 | 主题定义 |
| `--ood-slider-thumb-size` | 滑块拇指尺寸 | 主题定义 |
| `--ood-slider-thumb-hover-shadow` | 滑块拇指悬停阴影 | 主题定义 |
| `--ood-slider-thumb-hover` | 滑块拇指悬停背景色 | 主题定义 |

## 示例

### 基本水平滑块

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Slider.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-slider"></div>
    
    <script>
    // 创建单值滑块
    var slider = ood.UI.Slider({
        value: '50',
        width: '400px',
        height: '4em',
        theme: 'light',
        type: 'horizontal',
        isRange: false,
        labelPos: 'top',
        labelSize: '6em',
        labelCaption: '音量控制'
    }).appendTo('#my-slider');
    
    // 监听值变化
    slider.on('change', function() {
        console.log('新值:', this.value());
    });
    </script>
</body>
</html>
```

### 垂直范围滑块

```html
<div id="vertical-slider"></div>

<script>
var verticalSlider = ood.UI.Slider({
    value: '30:70',
    width: '4em',
    height: '200px',
    type: 'vertical',
    theme: 'dark',
    isRange: true,
    showIncreaseHandle: true,
    showDecreaseHandle: true,
    labelPos: 'right',
    labelSize: '6em',
    labelCaption: '温度范围'
}).appendTo('#vertical-slider');

// 设置步长（离散滑块）
verticalSlider.properties.steps = 10;
verticalSlider.refresh();
</script>
```

### 响应式滑块组

```html
<div class="slider-group">
    <div>亮度:</div>
    <div id="brightness-slider"></div>
    <div>对比度:</div>
    <div id="contrast-slider"></div>
    <div>饱和度:</div>
    <div id="saturation-slider"></div>
</div>

<script>
// 创建多个响应式滑块
var brightness = ood.UI.Slider({
    value: '75',
    width: '100%',
    height: '4em',
    responsive: true,
    theme: 'light',
    labelPos: 'left',
    labelSize: '5em',
    labelCaption: '亮度'
}).appendTo('#brightness-slider');

var contrast = ood.UI.Slider({
    value: '50',
    width: '100%',
    height: '4em',
    responsive: true,
    theme: 'light',
    labelPos: 'left',
    labelSize: '5em',
    labelCaption: '对比度'
}).appendTo('#contrast-slider');

var saturation = ood.UI.Slider({
    value: '30:80',
    width: '100%',
    height: '4em',
    responsive: true,
    theme: 'light',
    isRange: true,
    labelPos: 'left',
    labelSize: '5em',
    labelCaption: '饱和度范围'
}).appendTo('#saturation-slider');

// 同步更新示例
function updateAllSliders(value) {
    brightness.value(value);
    contrast.value(value);
    saturation.value(value + ':' + (parseInt(value) + 20));
}
</script>
```

## 注意事项

1. **值格式**：`value` 属性支持两种格式：
   - 单值：`'50'`（字符串或数字）
   - 范围值：`'30:70'`（字符串格式，用冒号分隔）

2. **步长设置**：`steps` 属性控制滑块精度：
   - `0`：连续滑块（默认）
   - `>0`：离散滑块，例如 `steps: 10` 表示分为10个步长

3. **方向切换**：`type` 属性支持 'horizontal'（水平）和 'vertical'（垂直）两种方向，切换时组件会自动调整布局。

4. **范围模式**：`isRange` 属性控制是否为双滑块范围选择器：
   - `false`：单滑块（选择单个值）
   - `true`：范围滑块（选择起始值和结束值）

5. **标签控制**：标签相关属性：
   - `labelPos: 'none'` 可隐藏标签
   - `labelSize` 控制标签区域大小
   - `labelCaption` 支持动态更新

6. **主题切换**：主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。

7. **响应式设计**：根据屏幕宽度自动调整布局：
   - 小于768px：移动端模式（增加触控区域）
   - 小于480px：超小屏幕模式（进一步优化）

8. **可访问性**：增强可访问性功能会自动添加 ARIA 属性：
   - `role="slider"`
   - `aria-orientation` 根据方向设置
   - `aria-valuemin`, `aria-valuemax`, `aria-valuenow`
   - `aria-label` 提供滑块描述

9. **过渡动画**：组件支持平滑过渡动画：
   - 滑块拇指悬停时有0.2秒的过渡效果
   - 主题切换时有平滑的颜色过渡

10. **浏览器兼容性**：组件使用现代CSS变量和Flexbox布局，建议在支持这些特性的浏览器中使用。