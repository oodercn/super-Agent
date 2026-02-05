# Video

视频播放器组件，继承自Audio组件，提供HTML5 Video元素的现代化封装，支持全屏播放、画布渲染、海报图像、主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.Video`

## 继承
`ood.UI.Audio` → `ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Video.js"></script>

<!-- 创建视频播放器容器 -->
<div id="video-container"></div>

<script>
var video = ood.UI.Video({
    src: 'video/sample.mp4',
    poster: 'video/poster.jpg',
    width: '34em',
    height: '25em',
    theme: 'dark',
    responsive: true,
    controls: true,
    preload: 'metadata',
    loop: false,
    muted: false,
    volume: 0.8,
    autoplay: false
}).appendTo('#video-container');

// 控制播放
video.play();
video.pause();
video.enterFullscreen();
</script>
```

## 特性

### 1. 继承Audio组件所有功能
Video组件完全继承了Audio组件的所有特性，包括：
- 完整的播放控制（播放、暂停、停止、音量调节等）
- 主题切换支持（light、dark、high-contrast）
- 响应式设计适配不同屏幕尺寸
- 可访问性增强（ARIA属性、键盘导航）

### 2. 视频特有功能
- **全屏播放**: 支持进入/退出全屏模式
- **海报图像**: 视频加载前显示预览图像
- **画布渲染**: 支持Canvas元素用于视频处理
- **响应式视频**: 根据屏幕尺寸自适应视频尺寸

### 3. 响应式设计
根据屏幕尺寸自动调整布局：
- 屏幕宽度 < 768px: 添加 `video-mobile` 类，视频宽度100%
- 屏幕宽度 < 480px: 添加 `video-tiny` 类，限制最大高度

### 4. 可访问性支持
- 完整的ARIA属性支持
- 键盘导航支持
- 屏幕阅读器友好

## 属性

### 数据模型属性 (DataModel)

Video组件继承了Audio的所有DataModel属性，并增加了以下视频特有属性：

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'dark'` | 主题样式 | `'light'`, `'dark'`, `'high-contrast'` |
| `responsive` | Boolean | `true` | 是否启用响应式设计 | - |
| `width` | String | `'34em'` | 组件宽度（带空间单位） | `$spaceunit: 1` |
| `height` | String | `'25em'` | 组件高度（带空间单位） | `$spaceunit: 1` |
| `poster` | String | `''` | 视频海报图像URL | 支持相对路径和绝对路径 |

### 继承自Audio的属性
Video继承了Audio的所有属性，包括：
- `src`: 视频源文件URL
- `controls`: 是否显示播放控制控件
- `preload`: 预加载策略
- `loop`: 是否循环播放
- `muted`: 是否静音
- `volume`: 音量（0.0到1.0）
- `autoplay`: 是否自动播放
- 等更多属性请参考[Audio文档](Audio.md)

## 实例方法

### 视频特有方法

#### `enterFullscreen()`
进入全屏播放模式。

**返回：**
- (Object): 组件实例，支持链式调用。

**示例：**
```javascript
video.enterFullscreen();
```

#### `exitFullscreen()`
退出全屏播放模式。

**返回：**
- (Object): 组件实例，支持链式调用。

**示例：**
```javascript
video.exitFullscreen();
```

#### `VideoTrigger()`
视频初始化触发器，用于设置初始主题、响应式布局和可访问性增强。通常在组件初始化后自动调用。

#### `enhanceAccessibility()`
增强视频播放器的可访问性支持，添加ARIA属性和键盘事件监听器。

**返回：**
- (Object): 组件实例，支持链式调用。

#### `adjustLayout()`
根据屏幕尺寸调整响应式布局。当`responsive`属性为true时自动调用。

**返回：**
- (Object): 组件实例，支持链式调用。

### 继承自Audio的方法
Video继承了Audio的所有方法，包括：
- `play()`: 开始播放
- `pause()`: 暂停播放
- `load()`: 重新加载视频源
- `canPlayType(type)`: 检查浏览器支持的视频格式
- `setTheme(theme)`: 设置主题
- `getTheme()`: 获取当前主题
- 等更多方法请参考[Audio文档](Audio.md)

## 使用示例

### 示例1：基础视频播放器
```html
<div id="my-video"></div>
<script>
var videoPlayer = ood.UI.Video({
    src: 'assets/videos/intro.mp4',
    poster: 'assets/images/video-poster.jpg',
    width: '640px',
    height: '360px',
    theme: 'light',
    controls: true,
    autoplay: false,
    loop: false
}).appendTo('#my-video');
</script>
```

### 示例2：全屏播放控制
```javascript
// 创建视频播放器
var video = ood.UI.Video({
    src: 'presentation.mp4',
    width: '800px',
    height: '450px'
}).appendTo('#container');

// 添加全屏控制按钮
document.getElementById('fullscreen-btn').addEventListener('click', function() {
    video.enterFullscreen();
});

// 监听全屏变化
document.addEventListener('fullscreenchange', function() {
    if (!document.fullscreenElement) {
        console.log('已退出全屏模式');
    }
});
```

### 示例3：响应式视频播放器
```javascript
// 创建响应式视频播放器
var responsiveVideo = ood.UI.Video({
    src: 'responsive-video.mp4',
    poster: 'poster-mobile.jpg',
    responsive: true,
    theme: 'dark'
}).appendTo('#video-container');

// 监听窗口大小变化，重新调整布局
window.addEventListener('resize', function() {
    responsiveVideo.adjustLayout();
});
```

### 示例4：多主题切换
```javascript
var video = ood.UI.Video({
    src: 'video.mp4',
    theme: 'light'  // 初始主题
}).appendTo('#container');

// 主题切换函数
function toggleVideoTheme() {
    var currentTheme = video.getTheme();
    var newTheme = currentTheme === 'light' ? 'dark' : 
                   currentTheme === 'dark' ? 'high-contrast' : 'light';
    video.setTheme(newTheme);
}

// 绑定主题切换按钮
document.getElementById('theme-toggle').addEventListener('click', toggleVideoTheme);
```

## 事件处理

Video组件支持HTML5 Video元素的所有事件，并可以通过OOD框架的事件系统进行监听：

### 常用视频事件
```javascript
video.on('play', function(profile, event) {
    console.log('视频开始播放');
});

video.on('pause', function(profile, event) {
    console.log('视频暂停');
});

video.on('ended', function(profile, event) {
    console.log('视频播放结束');
});

video.on('timeupdate', function(profile, event) {
    var currentTime = profile.getSubNode('H5').get(0).currentTime;
    console.log('当前播放时间:', currentTime);
});

video.on('fullscreenchange', function(profile, event) {
    if (document.fullscreenElement) {
        console.log('进入全屏模式');
    } else {
        console.log('退出全屏模式');
    }
});
```

### 错误处理
```javascript
video.on('error', function(profile, event) {
    console.error('视频加载或播放错误:', profile.getSubNode('H5').get(0).error);
});

video.on('stalled', function(profile, event) {
    console.warn('视频数据加载停滞');
});
```

## CSS主题

Video组件支持三种CSS主题，通过`data-theme`属性应用：

### 暗色主题 (dark) - 默认
```css
[data-theme="dark"] .ood-video {
    --ood-video-bg: #1a202c;
    --ood-video-controls-bg: rgba(0, 0, 0, 0.8);
    --ood-video-text: #e2e8f0;
}
```

### 亮色主题 (light)
```css
[data-theme="light"] .ood-video {
    --ood-video-bg: #ffffff;
    --ood-video-controls-bg: rgba(255, 255, 255, 0.9);
    --ood-video-text: #2d3748;
}
```

### 高对比度主题 (high-contrast)
```css
[data-theme="high-contrast"] .ood-video {
    --ood-video-bg: #000000;
    --ood-video-controls-bg: #ffffff;
    --ood-video-text: #ffffff;
    --ood-video-border: 2px solid #ffffff;
}
```

## 响应式设计

Video组件自动根据屏幕尺寸调整布局：

### 桌面端 (≥768px)
- 固定宽度：默认34em
- 标准控制栏
- 完整功能集

### 移动端 (<768px)
- 添加 `video-mobile` 类
- 宽度100%，自适应容器
- 简化控制栏
- 触摸优化

### 小屏幕 (<480px)
- 添加 `video-tiny` 类
- 限制最大高度：40vh
- 最小化控制元素
- 全屏按钮优先

## 可访问性

Video组件提供完整的可访问性支持：

### ARIA属性
- `role="application"`: 标识为应用程序区域
- `aria-label`: 描述视频播放器功能
- `aria-controls`: 标识控制的元素
- `aria-describedby`: 关联描述信息

### 键盘导航
- `Space`/`Enter`: 播放/暂停
- `Arrow Left/Right`: 快退/快进
- `Arrow Up/Down`: 音量增减
- `F`: 进入/退出全屏
- `M`: 静音切换

### 屏幕阅读器支持
- 提供视频状态描述
- 支持播放进度播报
- 键盘焦点管理

## 相关组件

- [Audio](Audio.md): Video的父类，音频播放器组件
- [Camera](Camera.md): 摄像头组件，用于视频采集
- [SVGPaper](SVGPaper.md): 矢量图形绘制组件
- [Canvas](Canvas.md): 画布组件，与Video协同工作

## 最佳实践

1. **视频格式**: 提供多种格式（MP4、WebM）以确保浏览器兼容性
2. **海报图像**: 总是提供高质量的海报图像，提升用户体验
3. **响应式设计**: 始终启用`responsive`属性以适应不同设备
4. **可访问性**: 确保视频内容有字幕或文本描述
5. **性能优化**: 使用适当的预加载策略，平衡性能与用户体验

## 注意事项

1. **自动播放策略**: 现代浏览器限制自动播放，可能需要用户交互
2. **全屏API**: 全屏功能需要用户手势触发，不能自动调用
3. **跨域资源**: 使用跨域视频源时可能需要CORS配置
4. **移动端限制**: 移动设备可能有电池和流量优化，影响播放行为
5. **主题持久化**: 主题设置会自动保存到`localStorage`，键名为`video-theme`