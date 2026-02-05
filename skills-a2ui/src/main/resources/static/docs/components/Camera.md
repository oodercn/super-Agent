# Camera

摄像头组件，继承自音频播放器组件，专门用于摄像头视频流的捕获和显示，支持视频预览、截图和基本控制。

## 类名
`ood.UI.Camera`

## 继承
`ood.UI.Audio`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Camera.js"></script>

<!-- 创建摄像头 -->
<div id="camera-container"></div>

<script>
var camera = ood.UI.Camera({
    src: '', // 摄像头设备ID或视频流URL
    width: '34em',
    height: '25em',
    theme: 'light',
    responsive: true,
    controls: true,
    patterned: false,
    dock: 'fill',
    poster: '' // 预览图URL
}).appendTo('#camera-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `patterned` | Boolean | `false` | 是否启用图案背景 |
| `dock` | String | `'fill'` | 布局停靠位置：'fill'（填充） |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `width` | String | `'34em'` | 组件宽度（带空间单位） |
| `height` | String | `'25em'` | 组件高度（带空间单位） |
| `poster` | String | `''` | 预览图像URL，视频加载前显示 |

**注意：** Camera组件继承所有Audio组件的属性，包括`theme`、`responsive`、`src`、`controls`、`preload`、`loop`、`muted`、`volume`、`autoplay`等。

## 方法

Camera组件继承所有Audio组件的方法，包括：
- `play()`, `pause()`, `load()`, `canPlayType()`
- `setTheme()`, `getTheme()`, `toggleDarkMode()`
- `adjustLayout()`, `enhanceAccessibility()`

## 事件

继承Audio组件的`onMediaEvent`事件处理器，支持所有HTML5媒体事件。

## CSS 变量 (Appearances)

组件支持以下CSS类名：

| 类名 | 描述 |
|------|------|
| `ood-camera-container` | 摄像头容器样式 |
| 继承所有Audio组件的CSS类 | 包括主题类、响应式类等 |

## 示例

### 基本摄像头预览

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Camera.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-camera"></div>
    
    <script>
    // 创建摄像头组件
    var camera = ood.UI.Camera({
        width: '640px',
        height: '480px',
        theme: 'dark',
        controls: true,
        poster: 'images/camera-preview.jpg'
    }).appendTo('#my-camera');
    
    // 可选的摄像头流设置
    function startCameraStream(deviceId) {
        // 实际应用中需要调用getUserMedia API
        // camera.src(deviceId);
    }
    </script>
</body>
</html>
```

### 响应式摄像头布局

```html
<div class="camera-grid">
    <div class="camera-item">
        <h4>主摄像头</h4>
        <div id="primary-camera"></div>
    </div>
    <div class="camera-item">
        <h4>辅助摄像头</h4>
        <div id="secondary-camera"></div>
    </div>
</div>

<script>
// 创建多个摄像头实例
var primaryCamera = ood.UI.Camera({
    width: '100%',
    height: '300px',
    responsive: true,
    theme: 'light',
    controls: true,
    dock: 'fill'
}).appendTo('#primary-camera');

var secondaryCamera = ood.UI.Camera({
    width: '100%',
    height: '300px',
    responsive: true,
    theme: 'light',
    controls: true,
    dock: 'fill'
}).appendTo('#secondary-camera');

// 响应式调整
window.addEventListener('resize', function() {
    primaryCamera.adjustLayout();
    secondaryCamera.adjustLayout();
});
</script>
```

## 注意事项

1. **摄像头访问**：实际摄像头访问需要配合`getUserMedia` API，组件本身不处理设备权限和流捕获。

2. **视频源格式**：`src`属性通常用于指定视频流URL或设备ID，具体实现取决于后端服务。

3. **预览图**：`poster`属性用于设置视频加载前的预览图像，支持图片URL。

4. **布局控制**：`dock`属性控制组件在容器中的布局方式，当前主要支持'fill'（填充容器）。

5. **图案背景**：`patterned`属性控制是否显示图案背景，可用于视觉区分。

6. **继承功能**：组件完全继承Audio组件的所有功能，包括主题系统、响应式设计、可访问性增强等。

7. **浏览器支持**：视频捕获功能需要浏览器支持相应的API（如getUserMedia），不同浏览器支持程度不同。

8. **移动端适配**：摄像头组件在移动端会自动启用响应式样式，优化触控体验。