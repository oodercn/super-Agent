# FileUpload

文件上传组件，提供现代化的文件上传界面，支持多文件选择、上传进度显示、主题切换和响应式设计。常用于表单文件上传、文档管理系统、图片上传等场景。

## 类名
`ood.UI.FileUpload`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/FileUpload.js"></script>

<!-- 创建文件上传组件 -->
<div id="fileupload-container"></div>

<script>
var fileUpload = ood.UI.FileUpload({
    dock: 'fill',
    theme: 'light',
    responsive: true,
    width: '40em',
    height: '30em',
    src: '/plugins/fileupload/uploadgrid.html',
    prepareFormData: true,
    uploadUrl: 'upload/',
    params: {}
}).appendTo('#fileupload-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `selectable` | Boolean | `true` | 是否允许选择文本 |
| `width` | String | `'40em'` | 组件宽度（CSS尺寸） |
| `height` | String | `'30em'` | 组件高度（CSS尺寸） |
| `src` | String | `'/plugins/fileupload/uploadgrid.html'` | 上传页面URL（iframe源） |
| `prepareFormData` | Boolean | `true` | 是否准备表单数据（从宿主组件获取） |
| `uploadUrl` | String | `'upload/'` | 上传处理URL |
| `params` | Object | `{}` | 额外参数对象（会合并到上传请求中） |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `dock` | String | `'fill'` | 布局位置：'fill', 'left', 'right', 'top', 'bottom' |

## 方法

### `setQueryData(data, path)`
设置查询参数数据。

**参数：**
- `data` (Object): 要设置的参数数据
- `path` (String): 参数路径（点分隔符），可选

**返回：**
- (Object): 组件实例，支持链式调用。

### `reload(profile)`
重新加载上传组件，更新iframe源URL。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (String): 更新后的URL。

### `setTheme(theme)`
设置组件主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `FileUploadTrigger()`
初始化文件上传触发器的内部方法，用于设置主题和响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_prepareData(profile)`
准备渲染数据的内部方法，处理参数合并和URL生成。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 渲染数据对象。

## 事件

组件通过iframe的postMessage机制支持以下事件：

### 上传事件
- `uploadfile` - 文件上传开始时触发
- `uploadprogress` - 上传进度更新时触发
- `uploadcomplete` - 单个文件上传完成时触发
- `uploadfail` - 文件上传失败时触发

### 事件数据结构
```javascript
{
    eventType: 'uploadfile', // 事件类型
    item: { /* 文件信息 */ },
    response: { /* 服务器响应 */ }
}
```

## 示例

### 基本文件上传

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/FileUpload.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-fileupload"></div>
    
    <script>
    // 创建文件上传组件
    var uploader = ood.UI.FileUpload({
        dock: 'fill',
        theme: 'light',
        responsive: true,
        width: '600px',
        height: '400px',
        uploadUrl: '/api/upload'
    }).appendTo('#my-fileupload');
    
    // 监听上传事件
    uploader.on('uploadcomplete', function(profile, eventType, item, response) {
        console.log('文件上传完成:', item.name, response);
    });
    
    uploader.on('uploadprogress', function(profile, eventType, item, response) {
        console.log('上传进度:', item.progress + '%');
    });
    </script>
</body>
</html>
```

### 自定义参数的文件上传

```html
<div id="custom-fileupload"></div>

<script>
var customUpload = ood.UI.FileUpload({
    dock: 'fill',
    theme: 'dark',
    responsive: true,
    uploadUrl: '/api/upload',
    params: {
        category: 'documents',
        maxSize: '10MB',
        allowedTypes: 'pdf,doc,docx'
    }
}).appendTo('#custom-fileupload');

// 动态更新参数
customUpload.setQueryData({
    userId: 12345,
    folder: 'uploads/2024'
});
</script>
```

### 响应式文件上传

```html
<div id="responsive-fileupload"></div>

<script>
var responsiveUpload = ood.UI.FileUpload({
    dock: 'fill',
    theme: 'high-contrast',
    responsive: true,
    width: '90%',
    height: '80%',
    src: '/plugins/fileupload/mobile-upload.html'
}).appendTo('#responsive-fileupload');

// 在窗口大小改变时自动调整
window.addEventListener('resize', function() {
    responsiveUpload.adjustLayout();
});
</script>
```

## 注意事项

1. 组件使用iframe加载上传页面（默认 `/plugins/fileupload/uploadgrid.html`），确保该页面存在且可访问。
2. `prepareFormData` 属性启用时，组件会从宿主组件（host）获取表单数据并合并到上传参数中。
3. 上传事件通过HTML5的 `postMessage` 机制在iframe和父页面之间通信，需要现代浏览器支持。
4. 主题系统支持三种模式：light（亮色）、dark（暗色）、high-contrast（高对比度）。
5. 响应式设计会根据屏幕尺寸自动调整组件大小和布局。
6. 上传URL (`uploadUrl`) 是服务器端处理文件上传的接口地址，需要确保后端支持。
7. 额外参数 (`params`) 会通过查询字符串或表单数据的方式发送到上传接口。
8. 组件默认停靠位置为 `fill`（填充父容器），可根据需要调整。
9. 文件上传进度事件 (`uploadprogress`) 提供实时进度更新，便于实现进度条显示。
10. 组件内置了良好的错误处理和用户反馈机制。