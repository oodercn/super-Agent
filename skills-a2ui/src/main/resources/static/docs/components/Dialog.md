# Dialog

对话框组件，提供功能丰富的弹出窗口，支持模态/非模态显示、最小化/最大化/恢复、调整大小、移动、主题切换、响应式布局和丰富的工具栏按钮。常用于表单弹窗、信息提示、配置面板等场景。

## 类名
`ood.UI.Dialog`

## 继承
`ood.UI.Widget`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Dialog.js"></script>

<!-- 创建对话框 -->
<div id="dialog-container"></div>

<script>
var dialog = ood.UI.Dialog({
    caption: '弹出窗口',
    theme: 'light',
    responsive: true,
    width: '25em',
    height: '36em',
    modal: false,
    status: 'normal',
    displayBar: true,
    minBtn: true,
    maxBtn: true,
    closeBtn: true,
    resizer: true,
    movable: true
}).appendTo('#dialog-container');

// 显示对话框
dialog.show();
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'system'（系统自动检测） |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `rotate` | Number | `null` | 旋转角度（度） |
| `selectable` | Boolean | `true` | 是否允许选择文本 |
| `tips` | String | `null` | 提示文本（tooltip） |
| `border` | String | `null` | 边框样式 |
| `disabled` | Boolean | `null` | 是否禁用 |
| `dock` | String | `'none'` | 停靠位置：'none', 'left', 'right', 'top', 'bottom', 'fill' |
| `showEffects` | String | `'Classic'` | 显示动画效果 |
| `hideEffects` | String | `''` | 隐藏动画效果 |
| `initPos` | Object | `{ ini: 'center' }` | 初始位置：'auto', 'center' |
| `iframeAutoLoad` | String | `''` | iframe自动加载URL |
| `ajaxAutoLoad` | String | `''` | AJAX自动加载URL |
| `html` | String | `''` | HTML内容（设置html属性时会直接更新面板内容） |
| `caption` | String | `undefined` | 对话框标题 |
| `image` | String | `''` | 图标图片URL |
| `imagePos` | String | `''` | 图标背景位置 |
| `imageBgSize` | String | `''` | 图标背景大小 |
| `imageClass` | String | `''` | 图标CSS类名 |
| `iconFontCode` | String | `''` | 图标字体代码 |
| `shadow` | Boolean | `true` | 是否显示阴影 |
| `resizer` | Boolean | `true` | 是否允许调整大小 |
| `movable` | Boolean | `true` | 是否允许移动 |
| `displayBar` | Boolean | `true` | 是否显示标题栏 |
| `minBtn` | Boolean | `true` | 是否显示最小化按钮 |
| `maxBtn` | Boolean | `true` | 是否显示最大化按钮 |
| `restoreBtn` | Boolean | `true` | 是否显示恢复按钮 |
| `infoBtn` | Boolean | `false` | 是否显示信息按钮 |
| `optBtn` | Boolean | `false` | 是否显示选项按钮 |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |
| `refreshBtn` | Boolean | `false` | 是否显示刷新按钮 |
| `pinBtn` | Boolean | `false` | 是否显示置顶按钮 |
| `landBtn` | Boolean | `false` | 是否显示横屏按钮 |
| `width` | String | `'25em'` | 对话框宽度（CSS尺寸） |
| `height` | String | `'36em'` | 对话框高度（CSS尺寸） |
| `minWidth` | Number | `200` | 最小宽度（像素） |
| `minHeight` | Number | `100` | 最小高度（像素） |
| `position` | String | `'absolute'` | 定位方式 |
| `fromRegion` | Object | `{ hidden: true, ini: null }` | 来源区域（隐藏属性） |
| `modal` | Boolean | `false` | 是否为模态对话框 |
| `status` | String | `'normal'` | 窗口状态：'normal', 'min', 'max' |
| `hAlign` | String | `'left'` | 水平对齐：'left', 'center', 'right' |
| `tagCmds` | Array | `[]` | 标签命令数组 |

## 方法

### `show(parent, modal, left, top, callback, ignoreEffects)`
显示对话框。

**参数：**
- `parent` (Object): 父容器（默认body）
- `modal` (Boolean): 是否模态显示
- `left` (Number): 左侧位置（像素）
- `top` (Number): 顶部位置（像素）
- `callback` (Function): 显示完成后的回调函数
- `ignoreEffects` (Boolean): 是否忽略动画效果

**返回：**
- (Object): 组件实例，支持链式调用。

### `showModal(parent, left, top, callback, ignoreEffects)`
显示模态对话框（show的便捷方法）。

**参数：** 同show方法

**返回：**
- (Object): 组件实例，支持链式调用。

### `setTheme(theme)`
设置对话框主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'system'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称（优先从localStorage恢复）。

### `toggleTheme()`
循环切换主题（light → dark → high-contrast → light）。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setChildren(childrens, prf)`
设置对话框子组件。

**参数：**
- `childrens` (Array): 子组件数组
- `prf` (Object): 配置对象

**返回：**
- (Object): 组件实例，支持链式调用。

### `detectSystemTheme()`
检测系统主题偏好（内部方法）。

**返回：**
- (String): 'light' 或 'dark'

### `DialogTrigger()`
初始化对话框触发器的内部方法，用于设置主题和响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_prepareData(profile)`
准备渲染数据的内部方法，处理标题栏、按钮显示等逻辑。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 渲染数据对象。

### `_min(profile, status, effectcallback, ignoreEffects)`
最小化对话框的内部方法。

**参数：**
- `profile` (Object): 组件配置对象
- `status` (String): 目标状态
- `effectcallback` (Function): 动画效果回调
- `ignoreEffects` (Boolean): 是否忽略动画效果

**返回：**
- (Object): 组件实例，支持链式调用。

### `_max(profile, status, effectcallback, ignoreEffects)`
最大化对话框的内部方法。

**参数：** 同_min方法

**返回：**
- (Object): 组件实例，支持链式调用。

### `_restore(profile, status)`
恢复对话框大小的内部方法。

**参数：**
- `profile` (Object): 组件配置对象
- `status` (String): 目标状态

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

组件支持以下事件处理器：

### 显示相关事件
- `onShow` - 对话框显示时触发
- `onActivated` - 对话框激活时触发
- `beforeClose` - 关闭前触发（可阻止关闭）

### 状态变化事件
- `beforeStatusChanged` - 状态改变前触发（可阻止改变）
- `afterStatusChanged` - 状态改变后触发

### 工具栏按钮事件
- `onShowInfo` - 点击信息按钮时触发
- `onShowOptions` - 点击选项按钮时触发
- `onRefresh` - 点击刷新按钮时触发
- `onCmd` - 点击自定义命令按钮时触发

### 交互事件
- `onClickPanel` - 点击面板内容时触发
- `onLand` - 点击横屏按钮时触发
- `beforePin` - 置顶前触发

## 示例

### 基本对话框

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Dialog.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-dialog"></div>
    
    <script>
    // 创建对话框
    var dlg = ood.UI.Dialog({
        caption: '欢迎使用A2UI',
        width: '400px',
        height: '300px',
        modal: true,
        theme: 'light'
    }).appendTo('#my-dialog');
    
    // 设置内容
    dlg.setChildren([
        ood.UI.Label({
            caption: '这是一个模态对话框示例',
            dock: 'fill'
        })
    ]);
    
    // 显示对话框
    dlg.showModal();
    
    // 监听关闭事件
    dlg.on('beforeClose', function(profile) {
        console.log('对话框即将关闭');
        return true; // 允许关闭
    });
    </script>
</body>
</html>
```

### 带工具栏的对话框

```html
<div id="toolbar-dialog"></div>

<script>
var toolbarDlg = ood.UI.Dialog({
    caption: '编辑器',
    width: '600px',
    height: '450px',
    minBtn: true,
    maxBtn: true,
    closeBtn: true,
    refreshBtn: true,
    pinBtn: true,
    resizer: true,
    movable: true,
    theme: 'dark'
}).appendTo('#toolbar-dialog');

// 显示并允许调整大小
toolbarDlg.show();
</script>
```

### 响应式对话框

```html
<div id="responsive-dialog"></div>

<script>
var responsiveDlg = ood.UI.Dialog({
    caption: '响应式对话框',
    width: '80%',
    height: '70%',
    responsive: true,
    initPos: 'center',
    displayBar: true,
    theme: 'system' // 自动跟随系统主题
}).appendTo('#responsive-dialog');

// 在窗口大小改变时自动调整
window.addEventListener('resize', function() {
    responsiveDlg.adjustLayout();
});

responsiveDlg.show();
</script>
```

## 注意事项

1. 对话框组件功能非常丰富，提供了完整的窗口管理功能，包括最小化、最大化、恢复、调整大小、移动等。
2. 主题系统支持三种模式：light（亮色）、dark（暗色）、system（跟随系统主题）。
3. 响应式设计会自动根据屏幕尺寸调整对话框大小和布局，确保在不同设备上的良好显示。
4. 工具栏按钮可以通过属性控制显示/隐藏，包括最小化、最大化、关闭、刷新、置顶、横屏等按钮。
5. 对话框支持模态和非模态两种模式，模态对话框会阻止背景交互。
6. 动画效果可以通过 `showEffects` 和 `hideEffects` 属性配置。
7. 内容可以通过 `html` 属性直接设置HTML，或通过 `setChildren` 方法添加子组件。
8. 对话框位置可以通过 `initPos` 属性设置为 'auto'（自定义位置）或 'center'（居中显示）。
9. 组件支持丰富的键盘快捷键操作，如 ESC 关闭对话框等。
10. ARIA 属性已内置，确保良好的可访问性支持。