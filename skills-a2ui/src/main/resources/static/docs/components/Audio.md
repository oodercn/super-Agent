# Audio

音频播放器组件，封装HTML5 Audio元素，提供现代化的音频播放控制界面，支持主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.Audio`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Audio.js"></script>

<!-- 创建音频播放器 -->
<div id="audio-container"></div>

<script>
var audio = ood.UI.Audio({
    src: 'audio/music.mp3',
    width: '18em',
    height: '5em',
    theme: 'light',
    responsive: true,
    controls: true,
    preload: 'metadata',
    loop: false,
    muted: false,
    volume: 0.8,
    autoplay: false
}).appendTo('#audio-container');

// 控制播放
audio.play();
audio.pause();
audio.load();
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `selectable` | Boolean | `true` | 是否可选择 |
| `width` | String | `'18em'` | 组件宽度（带空间单位） |
| `height` | String | `'5em'` | 组件高度（带空间单位） |
| `src` | String | `''` | 音频源文件URL（支持相对路径和绝对路径） |
| `cover` | Boolean | `false` | 是否显示封面图 |
| `controls` | Boolean | `true` | 是否显示播放控制控件 |
| `preload` | String | `'none'` | 预加载策略：'none'（不预加载），'metadata'（仅加载元数据），'auto'（自动加载） |
| `loop` | Boolean | `false` | 是否循环播放 |
| `muted` | Boolean | `false` | 是否静音 |
| `volume` | Number | `1` | 音量（0.0到1.0） |
| `autoplay` | Boolean | `false` | 是否自动播放 |

## 方法

### `play()`
开始播放音频。

**返回：**
- (Object): 组件实例，支持链式调用。

### `pause()`
暂停播放音频。

**返回：**
- (Object): 组件实例，支持链式调用。

### `load()`
重新加载音频源。

**返回：**
- (Object): 组件实例，支持链式调用。

### `canPlayType(type)`
检查浏览器是否支持指定的音频格式。

**参数：**
- `type` (String): MIME类型，如 'audio/mp3', 'audio/ogg', 'audio/wav'

**返回：**
- (String): 浏览器支持程度：'probably', 'maybe', 或空字符串（不支持）

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

### `onMediaEvent(profile, eventType, params)`
音频媒体事件处理器，监听HTML5 Audio元素的各种事件。

**支持的事件类型：**
- `loadstart`: 开始加载音频
- `progress`: 加载进度更新
- `durationchange`: 音频时长变化
- `seeked`: 搜索完成
- `seeking`: 正在搜索
- `timeupdate`: 播放时间更新
- `playing`: 开始播放
- `canplay`: 可以播放
- `canplaythrough`: 可以完整播放
- `volumechange`: 音量变化
- `ratechange`: 播放速率变化
- `loadedmetadata`: 元数据加载完成
- `loadeddata`: 音频数据加载完成
- `play`: 播放开始
- `pause`: 播放暂停
- `ended`: 播放结束

## CSS 变量 (Appearances)

组件支持以下CSS类名，可在主题中自定义样式：

| 类名 | 描述 |
|------|------|
| `ood-audio` | 音频播放器容器样式 |
| `ood-audio-controls` | 音频控制元素样式 |
| `ood-audio-cover` | 音频封面图样式 |
| `ood-audio:hover .ood-audio-cover` | 悬停时的封面图样式 |
| `audio-dark` | 暗黑主题样式类 |
| `audio-hc` | 高对比度主题样式类 |
| `audio-mobile` | 移动端响应式样式类 |
| `audio-tiny` | 超小屏幕响应式样式类 |

## 示例

### 基本音频播放器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Audio.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-audio"></div>
    
    <script>
    // 创建音频播放器
    var audioPlayer = ood.UI.Audio({
        src: 'sounds/background.mp3',
        width: '300px',
        height: '60px',
        theme: 'light',
        controls: true,
        preload: 'auto',
        volume: 0.7
    }).appendTo('#my-audio');
    
    // 添加事件监听
    audioPlayer.onMediaEvent = function(profile, eventType, params) {
        console.log('音频事件:', eventType);
        if (eventType === 'ended') {
            console.log('播放结束');
        }
    };
    </script>
</body>
</html>
```

### 响应式音频播放器组

```html
<div class="audio-playlist">
    <h3>音乐播放列表</h3>
    <div id="track1"></div>
    <div id="track2"></div>
    <div id="track3"></div>
</div>

<script>
// 创建多个响应式音频播放器
var track1 = ood.UI.Audio({
    src: 'music/track1.mp3',
    width: '100%',
    height: '50px',
    responsive: true,
    theme: 'dark',
    controls: true,
    preload: 'metadata'
}).appendTo('#track1');

var track2 = ood.UI.Audio({
    src: 'music/track2.mp3',
    width: '100%',
    height: '50px',
    responsive: true,
    theme: 'dark',
    controls: true,
    preload: 'metadata'
}).appendTo('#track2');

var track3 = ood.UI.Audio({
    src: 'music/track3.mp3',
    width: '100%',
    height: '50px',
    responsive: true,
    theme: 'dark',
    controls: true,
    preload: 'metadata'
}).appendTo('#track3');

// 控制播放逻辑（一次只播放一个）
function playOnly(track) {
    [track1, track2, track3].forEach(function(player) {
        if (player !== track) {
            player.pause();
        }
    });
}

track1.on('play', function() { playOnly(track1); });
track2.on('play', function() { playOnly(track2); });
track3.on('play', function() { playOnly(track3); });
</script>
```

### 自定义主题音频播放器

```html
<div id="custom-audio"></div>

<style>
/* 自定义音频样式 */
.ood-audio.custom-theme {
    border: 2px solid #4CAF50;
    border-radius: 10px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 10px;
}

.ood-audio.custom-theme .ood-audio-controls {
    filter: invert(1); /* 反色控制按钮 */
}

.audio-dark.custom-theme {
    background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
}
</style>

<script>
var customAudio = ood.UI.Audio({
    src: 'audio/custom.mp3',
    width: '350px',
    height: '70px',
    theme: 'light',
    responsive: true
}).appendTo('#custom-audio');

// 添加自定义主题类
customAudio.getRoot().addClass('custom-theme');

// 主题切换时保留自定义类
customAudio.setTheme('dark');
customAudio.getRoot().addClass('custom-theme');
</script>
```

## 注意事项

1. **音频格式兼容性**：不同浏览器支持的音频格式不同，建议提供多种格式（MP3、OGG、WAV）以确保兼容性。

2. **自动播放策略**：现代浏览器对自动播放有限制，通常需要用户交互后才能自动播放。`autoplay`属性可能不会立即生效。

3. **预加载策略**：
   - `none`: 不预加载，节省带宽
   - `metadata`: 仅加载元数据（时长、尺寸等）
   - `auto`: 自动加载音频文件

4. **响应式设计**：
   - 小于768px：移动端模式（添加`audio-mobile`类）
   - 小于480px：超小屏幕模式（添加`audio-tiny`类）

5. **可访问性增强**：
   - 自动添加ARIA属性：`role="application"`, `aria-label`
   - 支持键盘导航：空格键/回车键控制播放/暂停
   - 静音状态有特殊ARIA标签

6. **主题系统**：
   - 支持light、dark主题
   - 主题设置自动保存到localStorage
   - 可通过CSS类名自定义主题样式

7. **事件监听**：组件监听HTML5 Audio的所有标准事件，可通过`onMediaEvent`方法处理。

8. **封面图功能**：`cover`属性控制是否显示封面图，需要配合CSS样式实现。

9. **尺寸控制**：`width`和`height`属性支持CSS单位（px、em、%等），支持响应式调整。

10. **浏览器支持**：基于HTML5 Audio元素，要求现代浏览器支持。旧版IE需要兼容性处理。