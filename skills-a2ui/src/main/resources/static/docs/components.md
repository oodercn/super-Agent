# A2UI 组件库详细文档

本文档详细介绍 A2UI 框架中的所有 UI 组件，包括使用方法、API 参考和示例代码。

## 组件索引

- [Audio](#audio) - 音频播放器组件
- [Block](#block) - 通用块容器组件
- [ButtonLayout](#buttonlayout) - 按钮布局组件
- [ButtonViews](#buttonviews) - 按钮视图组件
- [CheckBox](#checkbox) - 复选框组件
- [ColorPicker](#colorpicker) - 颜色选择器组件
- [ComboInput](#comboinput) - 组合输入框组件
- [ContentBlock](#contentblock) - 内容块组件
- [DatePicker](#datepicker) - 日期选择器组件
- [Dialog](#dialog) - 对话框组件
- [ECharts](#echarts) - ECharts 图表组件
- [FileUpload](#fileupload) - 文件上传组件
- [Flash](#flash) - Flash 播放器组件
- [FoldingList](#foldinglist) - 折叠列表组件
- [FoldingTabs](#foldingtabs) - 折叠标签页组件
- [FormLayout](#formlayout) - 表单布局组件
- [FusionChartsXT](#fusionchartsxt) - FusionCharts 图表组件
- [Gallery](#gallery) - 图片画廊组件
- [Group](#group) - 分组容器组件
- [HiddenInput](#hiddeninput) - 隐藏输入框组件
- [Image](#image) - 图片显示组件
- [InfoBlock](#infoblock) - 信息块组件
- [Input](#input) - 输入框组件
- [IOTGallery](#iotgallery) - IoT 图片画廊组件
- [Label](#label) - 标签组件
- [Layout](#layout) - 布局组件
- [List](#list) - 列表组件
- [MDialog](#mdialog) - 移动端对话框组件
- [MenuBar](#menubar) - 菜单栏组件
- [MFormLayout](#mformlayout) - 移动端表单布局组件
- [MTabs](#mtabs) - 移动端标签页组件
- [MTreeGrid](#mtreegrid) - 移动端树形表格组件
- [MTreeView](#mtreeview) - 移动端树形视图组件
- [Opinion](#opinion) - 意见反馈组件
- [PageBar](#pagebar) - 分页栏组件
- [Panel](#panel) - 面板组件
- [PopMenu](#popmenu) - 弹出菜单组件
- [ProgressBar](#progressbar) - 进度条组件
- [RadioBox](#radiobox) - 单选框组件
- [Resizer](#resizer) - 尺寸调整组件
- [RichEditor](#richeditor) - 富文本编辑器组件
- [Slider](#slider) - 滑块组件
- [Stacks](#stacks) - 堆栈组件
- [StatusButtons](#statusbuttons) - 状态按钮组件
- [SVGPaper](#svgpaper) - SVG 画布组件
- [Tabs](#tabs) - 标签页组件
- [Tensor](#tensor) - 张量显示组件
- [TimePicker](#timepicker) - 时间选择器组件
- [TitleBlock](#titleblock) - 标题块组件
- [ToolBar](#toolbar) - 工具栏组件
- [TreeBar](#treebar) - 树形导航栏组件
- [TreeGrid](#treegrid) - 树形表格组件
- [TreeView](#treeview) - 树形视图组件
- [Video](#video) - 视频播放器组件

---

## Audio

音频播放器组件，基于 HTML5 audio 元素，支持主题切换、响应式设计和可访问性增强。

### 类名
`ood.UI.Audio`

### 继承
`ood.UI`

### 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Audio.js"></script>

<!-- 创建音频播放器 -->
<div id="audio-container"></div>

<script>
var audio = ood.UI.Audio({
    src: 'music.mp3',
    controls: true,
    autoplay: false,
    theme: 'dark',
    responsive: true
}).appendTo('#audio-container');
</script>
```

### 属性

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `src` | String | `''` | 音频文件地址 |
| `controls` | Boolean | `true` | 是否显示控制条 |
| `autoplay` | Boolean | `false` | 是否自动播放 |
| `loop` | Boolean | `false` | 是否循环播放 |
| `muted` | Boolean | `false` | 是否静音 |
| `preload` | String | `'none'` | 预加载策略：'none', 'metadata', 'auto' |
| `volume` | Number | `1` | 音量（0.0-1.0） |
| `theme` | String | `'light'` | 主题：'light', 'dark' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `selectable` | Boolean | `true` | 是否可选 |
| `width` | String | `'18em'` | 宽度 |
| `height` | String | `'5em'` | 高度 |
| `cover` | Boolean | `false` | 是否显示封面 |

### 方法

#### `play()`
播放音频。

#### `pause()`
暂停音频。

#### `load()`
重新加载音频。

#### `canPlayType(type)`
检查浏览器是否支持指定的音频类型。

**参数：**
- `type` (String): 音频 MIME 类型，如 'audio/mp3'

**返回：**
- (String): 支持程度：'probably', 'maybe', 或空字符串

#### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light' 或 'dark'

#### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称

#### `toggleDarkMode()`
切换暗黑模式。

#### `adjustLayout()`
根据屏幕尺寸调整布局。

#### `enhanceAccessibility()`
增强可访问性支持。

### 事件

组件支持所有 HTML5 audio 事件：
- `loadstart`, `progress`, `durationchange`, `seeked`, `seeking`
- `timeupdate`, `playing`, `canplay`, `canplaythrough`
- `volumechange`, `ratechange`, `loadedmetadata`, `loadeddata`
- `play`, `pause`, `ended`

### 示例

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Audio.js"></script>
</head>
<body>
    <div id="player"></div>
    
    <script>
    // 创建音频播放器
    var myAudio = ood.UI.Audio({
        src: 'https://example.com/audio.mp3',
        controls: true,
        theme: 'dark',
        width: '300px',
        height: '80px'
    }).appendTo('#player');
    
    // 播放音频
    myAudio.play();
    
    // 监听播放事件
    myAudio.on('play', function() {
        console.log('音频开始播放');
    });
    </script>
</body>
</html>
```

### 注意事项
1. 音频文件路径需要符合同源策略或支持 CORS
2. 移动端浏览器可能限制自动播放
3. 不同浏览器支持的音频格式可能不同

---

