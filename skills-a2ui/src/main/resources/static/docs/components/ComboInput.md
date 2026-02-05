# ComboInput

组合输入框组件，结合文本框和下拉列表的功能，支持多种输入类型（文本、数字、日期、文件等）和现代化特性。常用于表单输入、数据选择等场景。

## 类名
`ood.UI.ComboInput`

## 继承
`ood.UI.Input`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ComboInput.js"></script>

<!-- 创建组合输入框 -->
<div id="comboinput-container"></div>

<script>
var comboInput = ood.UI.ComboInput({
    width: '20em',
    labelSize: '6em',
    caption: '请选择',
    items: [
        {id: 'option1', caption: '选项1', value: '1'},
        {id: 'option2', caption: '选项2', value: '2'},
        {id: 'option3', caption: '选项3', value: '3'},
        {id: 'option4', caption: '选项4', value: '4', disabled: true}
    ],
    theme: 'light',
    responsive: true,
    type: 'combobox'
}).appendTo('#comboinput-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `width` | String | `'18em'` | 组件宽度 |
| `labelSize` | String | `'6em'` | 标签宽度 |
| `caption` | String | `'$RAD.widgets.inputField'` | 显示标题 |
| `items` | Array | 4个预定义选项 | 下拉选项数组，每个项包含 id、caption、value、disabled 等 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `cachePopWnd` | Boolean | `true` | 是否缓存弹出窗口 |
| `bindClass` | String | `''` | 绑定样式类 |
| `expression` | String | `''` | 表达式 |
| `filter` | String | `''` | 过滤器 |
| `itemsExpression` | String | `''` | 项目表达式 |
| `dateEditorTpl` | String | `''` | 日期编辑器模板 |
| `groupingSeparator` | String | `','` | 分组分隔符 |
| `decimalSeparator` | String | `'.'` | 小数分隔符 |
| `forceFillZero` | Boolean | `true` | 是否强制补零 |
| `trimTailZero` | Boolean | `false` | 是否修剪尾部零 |
| `parentID` | String | `''` | 父级ID |
| `enumClass` | String | `''` | 枚举类 |
| `popCtrlProp` | Object | `{}` | 弹出控件属性 |
| `popCtrlEvents` | Object | `{}` | 弹出控件事件 |
| `image` | Object | `{format:'image'}` | 图像属性 |
| `imagePos` | String | `''` | 图像位置 |
| `imageBgSize` | String | `''` | 图像背景大小 |
| `imageClass` | String | `''` | 图像样式类 |
| `iconFontCode` | String | `''` | 图标字体代码 |
| `dropImageClass` | String | `''` | 下拉图标样式类 |
| `unit` | String | `''` | 单位 |
| `units` | String | `''` | 单位列表 |
| `numberTpl` | String | `''` | 数字模板 |
| `currencyTpl` | String | `'$ *'` | 货币模板 |
| `listKey` | String | `''` | 列表键 |
| `items` | Array | `[]` | 项目列表 |
| `type` | String | `'combobox'` | 输入类型：'none','input','password','combobox','listbox','file','getter','helpinput','button','dropbutton','cmdbox','popbox','date','time','datetime','color','spin','counter','currency','number' |
| `showMode` | String | `'normal'` | 显示模式：'','normal','compact','transparent' |
| `precision` | Number | `2` | 数字精度（用于数字/货币类型） |

## 方法

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

### `_adjustV(value)`
调整输入值（内部方法）。

**参数：**
- `value` (Any): 原始值

**返回：**
- (Any): 调整后的值

### `getValue()`
获取当前值。

**返回：**
- (Any): 当前值

### `getAllSelectedValues(key)`
获取所有选中项的值。

**参数：**
- `key` (String, optional): 指定返回值的属性键

**返回：**
- (Array): 选中值的数组

### `getSelectedItem(returnArr)`
获取选中的项。

**参数：**
- `returnArr` (Boolean, optional): 是否返回数组

**返回：**
- (Object|Array): 选中项或数组

### `getUICationValue(returnArr)`
获取UI表示值（内部方法）。

### `getUIValue()`
获取UI值（内部方法）。

### `setCaptionValue(value)`
设置标题值（内部方法）。

### `getCaptionValue()`
获取标题值（内部方法）。

### `_getCtrlValue()`
获取控件值（内部方法）。

### `_setCtrlValue(value)`
设置控件值（内部方法）。

### `_compareValue(v1, v2)`
比较两个值（内部方法）。

### `getShowValue(value)`
获取显示值（内部方法）。

### `_toEditor(value)`
转换为编辑器格式（内部方法）。

### `_fromEditor(value)`
从编辑器格式转换（内部方法）。

### `setPopWnd(drop)`
设置弹出窗口。

### `_cache(type, focus)`
缓存弹出窗口（内部方法）。

### `clearPopCache()`
清除弹出窗口缓存。

### `setUploadObj(input)`
设置上传对象（用于文件类型）。

### `getUploadObj()`
获取上传对象（用于文件类型）。

### `popFileSelector(accept, multiple)`
弹出文件选择器（用于文件类型）。

### `ComboInputTrigger()`
初始化触发器（内部方法）。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

## 事件

组件支持以下事件：

### 触摸事件
- `touchstart` - 触摸开始
- `touchmove` - 触摸移动
- `touchend` - 触摸结束
- `touchcancel` - 触摸取消

### 滑动手势
- `swipe` - 滑动（任意方向）
- `swipeleft` - 向左滑动
- `swiperight` - 向右滑动
- `swipeup` - 向上滑动
- `swipedown` - 向下滑动

### 按压手势
- `press` - 长按开始
- `pressup` - 长按结束

### 自定义事件
- `onCmd` - 按钮命令触发时调用
- `onFlagClick` - 点击标志按钮时触发

## 示例

### 基本组合输入框

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ComboInput.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-comboinput"></div>
    
    <script>
    // 创建组合输入框
    var combo = ood.UI.ComboInput({
        width: '20em',
        labelSize: '6em',
        caption: '请选择一个选项',
        items: [
            {id: 'opt1', caption: '第一选项', value: 'value1'},
            {id: 'opt2', caption: '第二选项', value: 'value2'},
            {id: 'opt3', caption: '第三选项', value: 'value3'},
            {id: 'opt4', caption: '第四选项', value: 'value4', disabled: true}
        ],
        theme: 'light',
        responsive: true,
        type: 'combobox'
    }).appendTo('#my-comboinput');
    
    // 监听值变化
    combo.on('change', function(newValue) {
        console.log('新值:', newValue);
    });
    </script>
</body>
</html>
```

### 数字输入框

```html
<div id="number-input"></div>

<script>
var numberInput = ood.UI.ComboInput({
    width: '15em',
    caption: '输入金额',
    type: 'number',
    precision: 2,
    unit: '元',
    theme: 'dark',
    responsive: true
}).appendTo('#number-input');
</script>
```

### 文件上传输入框

```html
<div id="file-upload"></div>

<script>
var fileInput = ood.UI.ComboInput({
    width: '25em',
    caption: '选择文件',
    type: 'file',
    theme: 'light',
    responsive: true
}).appendTo('#file-upload');
</script>
```

## 注意事项

1. 组件支持多种输入类型，通过 `type` 属性切换。
2. 启用 `responsive` 属性时，组件会根据屏幕尺寸自动调整布局。
3. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
4. 可访问性增强功能会自动为交互元素添加 ARIA 属性，支持键盘导航。
5. 对于文件类型，组件提供了上传对象管理方法。
6. 对于数字和货币类型，支持精度控制和单位显示。
7. 组件支持触摸手势操作，适合移动端使用。