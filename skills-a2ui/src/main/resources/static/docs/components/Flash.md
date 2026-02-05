# Flash

Flash播放器组件，用于嵌入和播放Adobe Flash内容，支持参数配置、版本检测和内存管理。常用于Flash游戏、动画、教育内容等场景。

## 类名
`ood.UI.Flash`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Flash.js"></script>

<!-- 创建Flash播放器 -->
<div id="flash-container"></div>

<script>
var flashPlayer = ood.UI.Flash({
    selectable: true,
    width: '30em',
    height: '25em',
    cover: false,
    src: 'animation.swf',
    parameters: {},
    flashvars: {}
}).appendTo('#flash-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `selectable` | Boolean | `true` | 是否允许选择文本 |
| `width` | String | `'30em'` | 播放器宽度（CSS尺寸） |
| `height` | String | `'25em'` | 播放器高度（CSS尺寸） |
| `cover` | Boolean | `false` | 是否覆盖模式（全屏适配） |
| `src` | String | `''` | Flash文件源路径（.swf文件） |
| `parameters` | Object | `{}` | Flash参数配置对象 |
| `flashvars` | Object | `{}` | Flash变量传递对象 |

## 方法

### `refreshFlash()`
刷新Flash播放器，重新加载SWF文件。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getFlashVersion()`
获取当前浏览器中安装的Flash Player版本。

**返回：**
- (String): 版本号，格式为"主版本,次版本,修订号"，例如"32,0,0"。

### `_getSWF(profile)`
获取Flash DOM元素的内部方法。

**参数：**
- `profile` (Object/String): 组件配置对象或元素ID

**返回：**
- (Object): Flash DOM元素。

### `_clearMemory(profile)`
清除Flash内存的内部方法，用于解决内存泄漏问题。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 组件实例，支持链式调用。

### `_drawSWF(profile)`
绘制SWF元素的内部方法，生成Flash嵌入代码。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

组件继承自 `ood.UI`，支持标准UI事件：
- `load` - Flash加载完成时触发
- `error` - Flash加载失败时触发
- `resize` - 播放器大小改变时触发

## 示例

### 基本Flash播放器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Flash.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-flash"></div>
    
    <script>
    // 创建Flash播放器
    var flash = ood.UI.Flash({
        src: 'game.swf',
        width: '800px',
        height: '600px',
        parameters: {
            quality: 'high',
            bgcolor: '#ffffff'
        },
        flashvars: {
            level: '1',
            difficulty: 'medium'
        }
    }).appendTo('#my-flash');
    
    // 获取Flash版本
    var version = flash.getFlashVersion();
    console.log('Flash版本:', version);
    </script>
</body>
</html>
```

### 带参数的Flash播放器

```html
<div id="parameterized-flash"></div>

<script>
var paramFlash = ood.UI.Flash({
    src: 'presentation.swf',
    width: '100%',
    height: '500px',
    cover: true,
    parameters: {
        wmode: 'transparent',
        allowFullScreen: 'true',
        allowScriptAccess: 'always'
    },
    flashvars: {
        autoPlay: 'true',
        loop: 'false',
        volume: '80'
    }
}).appendTo('#parameterized-flash');
</script>
```

### 响应式Flash播放器

```html
<div id="responsive-flash"></div>

<script>
var responsiveFlash = ood.UI.Flash({
    src: 'video.swf',
    width: '90%',
    height: '400px',
    cover: false,
    parameters: {
        scale: 'showall',
        align: 'center'
    }
}).appendTo('#responsive-flash');

// 在窗口大小改变时刷新
window.addEventListener('resize', function() {
    responsiveFlash.refreshFlash();
});
</script>
```

## 注意事项

1. Flash技术已逐渐被现代Web标准（HTML5）取代，建议在新项目中优先考虑使用HTML5替代方案。
2. 组件需要浏览器安装Flash Player插件才能正常工作。
3. `parameters` 属性用于设置Flash插件的参数，如wmode、quality等。
4. `flashvars` 属性用于向Flash应用程序传递变量参数。
5. `cover` 属性启用时，Flash内容会按比例缩放以覆盖整个容器。
6. 版本检测功能 (`getFlashVersion`) 支持IE和现代浏览器。
7. 内存管理功能 (`_clearMemory`) 有助于解决Flash内存泄漏问题。
8. 组件会自动处理Flash元素的创建和销毁。
9. 对于移动设备，Flash支持有限，建议提供备用内容。
10. 由于安全考虑，现代浏览器可能默认禁用Flash或需要用户手动授权。