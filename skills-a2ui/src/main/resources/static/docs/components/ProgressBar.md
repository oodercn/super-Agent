# ProgressBar

进度条组件，用于显示任务进度或加载状态，支持水平和垂直方向、主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.ProgressBar`

## 继承
`ood.UI.Widget` 和 `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ProgressBar.js"></script>

<!-- 创建进度条 -->
<div id="progressbar-container"></div>

<script>
var progressBar = ood.UI.ProgressBar({
    value: 50,
    width: '300px',
    height: '20px',
    theme: 'light',
    responsive: true,
    captionTpl: '* %',
    type: 'horizontal'
}).appendTo('#progressbar-container');

// 更新进度值
progressBar.value(75);
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `value` | Number | `0` | 进度值（0-100） |
| `width` | String | `'25em'` | 组件宽度 |
| `height` | String | `'1.5em'` | 组件高度 |
| `captionTpl` | String | `'* %'` | 标题模板，`*` 或 `{value}` 会被替换为当前值 |
| `type` | String | `'horizontal'` | 进度条方向：'horizontal'（水平），'vertical'（垂直） |
| `fillBG` | String | `''` | 填充区域背景色（CSS颜色值） |
| `$hborder` | Number | `1` | 水平边框宽度 |
| `$vborder` | Number | `1` | 垂直边框宽度 |

## 方法

### `_setCtrlValue(value)`
设置进度值并更新显示。

**参数：**
- `value` (Number): 进度值（0-100）

**返回：**
- (Object): 组件实例，支持链式调用。

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
根据屏幕尺寸调整响应式布局。在小屏幕设备上优化显示。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

## 内部方法

### `_prepareData(profile)`
准备渲染数据（内部方法）。

### `_ensureValue(profile, value)`
确保进度值在有效范围内（0-100）（内部方法）。

### `_onresize(profile, width, height)`
处理组件大小变化（内部方法）。

### `ProgressBarTrigger()`
进度条触发器，初始化现代化功能（内部方法）。

## 示例

### 基本水平进度条

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ProgressBar.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-progress"></div>
    
    <script>
    // 创建进度条
    var progress = ood.UI.ProgressBar({
        value: 30,
        width: '400px',
        height: '25px',
        theme: 'dark',
        captionTpl: '已完成: *%'
    }).appendTo('#my-progress');
    
    // 模拟进度更新
    var interval = setInterval(function() {
        var current = progress.value();
        if (current >= 100) {
            clearInterval(interval);
            return;
        }
        progress.value(current + 5);
    }, 500);
    </script>
</body>
</html>
```

### 垂直进度条

```html
<div id="vertical-progress"></div>

<script>
var verticalProgress = ood.UI.ProgressBar({
    value: 60,
    width: '30px',
    height: '200px',
    type: 'vertical',
    theme: 'light',
    captionTpl: '*%'
}).appendTo('#vertical-progress');

// 控制进度
function updateProgress(value) {
    verticalProgress.value(value);
}
</script>
```

### 响应式进度条组

```html
<div class="progress-group">
    <div>文件上传:</div>
    <div id="upload-progress"></div>
    <div>处理进度:</div>
    <div id="process-progress"></div>
</div>

<script>
// 创建多个响应式进度条
var uploadProgress = ood.UI.ProgressBar({
    value: 0,
    width: '100%',
    height: '15px',
    responsive: true,
    theme: 'light'
}).appendTo('#upload-progress');

var processProgress = ood.UI.ProgressBar({
    value: 0,
    width: '100%',
    height: '15px',
    responsive: true,
    theme: 'light'
}).appendTo('#process-progress');

// 模拟上传过程
function simulateUpload() {
    var uploadValue = 0;
    var uploadInterval = setInterval(function() {
        uploadValue += 5;
        uploadProgress.value(uploadValue);
        
        if (uploadValue >= 100) {
            clearInterval(uploadInterval);
            // 上传完成后开始处理
            simulateProcessing();
        }
    }, 300);
}

// 模拟处理过程
function simulateProcessing() {
    var processValue = 0;
    var processInterval = setInterval(function() {
        processValue += 10;
        processProgress.value(processValue);
        
        if (processValue >= 100) {
            clearInterval(processInterval);
        }
    }, 400);
}

// 开始上传
simulateUpload();
</script>
```

## 注意事项

1. 进度值 `value` 的范围为 0-100，组件会自动确保值在此范围内。
2. `type` 属性支持 'horizontal'（水平）和 'vertical'（垂直）两种方向，切换时会自动交换 width 和 height 值。
3. `captionTpl` 模板中可以使用 `*` 或 `{value}` 作为占位符，会被替换为当前进度值。
4. `fillBG` 属性可以自定义填充区域的颜色，值为有效的 CSS 颜色字符串。
5. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
6. 响应式设计根据屏幕宽度自动调整布局：
   - 小于768px：移动端模式
   - 小于480px：超小屏幕模式
7. 可访问性增强功能会自动添加 ARIA 属性：
   - `role="progressbar"`
   - `aria-valuemin="0"`, `aria-valuemax="100"`
   - `aria-valuenow` 设置为当前进度值
   - `aria-label` 提供进度条描述
8. 组件支持平滑过渡动画，进度变化时填充区域会有0.3秒的过渡效果。