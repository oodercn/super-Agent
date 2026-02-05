# RichEditor

富文本编辑器组件，提供类似Word的文本编辑功能，支持格式设置、主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.RichEditor`

## 继承
`ood.UI` 和 `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/RichEditor.js"></script>

<!-- 创建富文本编辑器 -->
<div id="richeditor-container"></div>

<script>
var richEditor = ood.UI.RichEditor({
    labelSize: '2em',
    height: '26em',
    labelPos: 'top',
    caption: '$RAD.widgets.richText',
    labelHAlign: 'left',
    theme: 'light',
    responsive: true,
    value: '<h1>欢迎使用富文本编辑器</h1><p>这是一个示例文本。</p>',
    width: '40em'
}).appendTo('#richeditor-container');

// 获取编辑器内容
var content = richEditor.value();
console.log('编辑器内容:', content);
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `labelSize` | String | `'2em'` | 标签大小 |
| `height` | String | `'26em'` | 编辑器高度 |
| `labelPos` | String | `'top'` | 标签位置：'top', 'left', 'right', 'bottom' |
| `caption` | String | `'$RAD.widgets.richText'` | 标题文本，可使用资源标识符 |
| `labelHAlign` | String | `'left'` | 标签水平对齐方式 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `expression` | String | `''` | 表达式 |
| `selectable` | Boolean | `true` | 是否可选择文本 |
| `value` | String | `''` | 编辑器内容，支持HTML格式 |
| `width` | String | `'32em'` | 编辑器宽度 |
| `height` | String | `'25em'` | 编辑器高度 |
| `frameTemplate` | String | 预定义iframe模板 | iframe编辑器框架的HTML模板 |
| `frameStyle` | String | `''` | 框架样式 |
| `cmdList` | String | `'font1;font2;align;list;font4;font3;insert;clear;html'` | 工具栏命令列表 |
| `$useOriginalText` | Boolean | `false` | 是否使用原始文本（不进行资源调整） |
| `borderType` | String | `'flat'` | 边框样式 |
| `editorMode` | Boolean | `false` | 编辑器模式开关 |
| `html` | Boolean | `true` | 是否支持HTML格式 |
| `textType` | String | `'html'` | 文本类型：'html'或'text' |
| `scrollable` | Boolean | `true` | 是否可滚动 |

## 方法

### `activate()`
激活编辑器，使编辑器获得焦点。

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `getEditorWin()`
获取编辑器窗口对象（iframe的contentWindow）。

**返回：**
- (Object): 编辑器窗口对象。

### `getEditorDoc()`
获取编辑器文档对象（iframe的contentDocument）。

**返回：**
- (Object): 编辑器文档对象。

### `getEditorBody()`
获取编辑器内容主体（body或documentElement）。

**返回：**
- (Object): 编辑器内容主体。

### `_setCtrlValue(value)`
设置编辑器内容值。

**参数：**
- `value` (String): 要设置的文本内容，支持HTML

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `_getCtrlValue()`
获取编辑器内容值。

**返回：**
- (String): 编辑器当前内容。

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark'

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleDarkMode()`
切换暗黑模式（在light和dark之间切换）。

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 编辑器实例，支持链式调用。

### `RichEditorTrigger()`
富文本编辑器触发器，初始化现代化功能。

**返回：**
- (Object): 编辑器实例，支持链式调用。

## 事件

富文本编辑器支持标准UI组件事件，以及以下特定事件：

### `onCmd`
工具栏命令触发时调用。

### `onTextChange`
编辑器内容变化时触发。

## 示例

### 基本富文本编辑器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/RichEditor.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="text-editor"></div>
    
    <script>
    // 创建富文本编辑器
    var editor = ood.UI.RichEditor({
        caption: '文章编辑器',
        width: '800px',
        height: '400px',
        theme: 'light',
        responsive: true,
        value: '<h1>文章标题</h1><p>这里是文章内容...</p>',
        labelPos: 'top',
        labelSize: '3em'
    }).appendTo('#text-editor');
    
    // 监听内容变化
    editor.on('change', function() {
        var content = editor.value();
        console.log('内容已更新:', content.length, '字符');
    });
    
    // 激活编辑器
    editor.activate();
    </script>
</body>
</html>
```

### 暗黑模式编辑器

```html
<div id="dark-editor"></div>

<script>
var darkEditor = ood.UI.RichEditor({
    caption: '暗黑模式编辑器',
    width: '100%',
    height: '300px',
    theme: 'dark',
    responsive: true,
    borderType: 'inset',
    value: '<h2 style="color: #64b5f6;">暗黑模式标题</h2><p style="color: #e0e0e0;">暗黑模式下的文本内容...</p>'
}).appendTo('#dark-editor');

// 切换主题示例
function toggleEditorTheme() {
    darkEditor.toggleDarkMode();
}
</script>
```

### 响应式编辑器集成

```html
<div class="container">
    <h3>博客编辑器</h3>
    <div id="blog-editor"></div>
    <div class="editor-tools">
        <button onclick="saveContent()">保存</button>
        <button onclick="previewContent()">预览</button>
    </div>
</div>

<script>
var blogEditor = ood.UI.RichEditor({
    caption: '博客内容',
    width: '100%',
    height: '350px',
    theme: 'light',
    responsive: true,
    editorMode: true,
    scrollable: true,
    cmdList: 'font1;font2;align;list;font4;insert;html',
    value: '<div style="font-family: Arial, sans-serif;">'
}).appendTo('#blog-editor');

// 保存内容
function saveContent() {
    var content = blogEditor.value();
    localStorage.setItem('blog-draft', content);
    alert('内容已保存到本地存储');
}

// 预览内容
function previewContent() {
    var content = blogEditor.value();
    var previewWindow = window.open();
    previewWindow.document.write(content);
    previewWindow.document.close();
}

// 响应式调整
window.addEventListener('resize', function() {
    blogEditor.adjustLayout();
});
</script>
```

## 工具栏命令说明

编辑器工具栏支持以下命令组：

| 命令组 | 功能 | 包含的工具 |
|--------|------|------------|
| `font1` | 字体格式 | 加粗、斜体、下划线、删除线 |
| `font2` | 字体样式 | 字体名称、字体大小、字体颜色、背景色 |
| `align` | 对齐方式 | 左对齐、居中、右对齐、两端对齐 |
| `list` | 列表格式 | 有序列表、无序列表 |
| `font4` | 特殊格式 | 上标、下标、清除格式 |
| `font3` | 段落格式 | 缩进、减少缩进、行高 |
| `insert` | 插入功能 | 链接、图片、表格、水平线 |
| `clear` | 清除功能 | 清除格式、清除内容 |
| `html` | HTML视图 | HTML代码视图、源代码编辑 |

## 注意事项

1. 编辑器支持两种模式：
   - 普通模式：直接显示编辑内容
   - iframe模式：使用iframe隔离编辑环境，适合复杂格式
2. 主题切换支持浅色和暗黑两种模式，自动保存用户偏好。
3. 响应式设计根据屏幕宽度自动调整：
   - 小于768px：移动端模式，调整工具栏按钮大小
   - 小于480px：超小屏幕模式，隐藏部分不常用按钮
4. 可访问性增强：
   - 容器：`role="application"`, `aria-label="富文本编辑器"`
   - 工具栏：`role="toolbar"`, `aria-label="编辑工具栏"`
   - 编辑区：`role="textbox"`, `aria-multiline="true"`
5. 内容值（`value`）支持HTML格式，但需要注意安全过滤。
6. 工具栏命令列表（`cmdList`）可通过配置自定义显示哪些工具组。
7. 编辑器支持通过 `frameTemplate` 自定义iframe框架的HTML结构。
8. 对于移动端设备，编辑器会自动启用触摸友好的界面和手势支持。