# CheckBox

复选框组件，支持主题切换、响应式设计和可访问性增强，适用于表单中的布尔值选择。

## 类名
`ood.UI.CheckBox`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/CheckBox.js"></script>

<!-- 创建复选框 -->
<div id="checkbox-container"></div>

<script>
var checkbox = ood.UI.CheckBox({
    caption: '同意用户协议',
    value: false,
    theme: 'light',
    responsive: true,
    hAlign: 'left',
    vAlign: 'middle',
    width: '200px',
    height: '30px'
}).appendTo('#checkbox-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `caption` | String | `'ri ri-checkbox-line'` | 复选框标题，支持图标类名 |
| `value` | Boolean | `true` | 复选框的初始值 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `value` | Boolean | `false` | 复选框当前值 |
| `expression` | String | `''` | 表达式 |
| `hAlign` | String | `'left'` | 水平对齐：'left', 'center', 'right' |
| `vAlign` | String | `'top'` | 垂直对齐：'top', 'middle', 'bottom' |
| `iconPos` | String | `'left'` | 图标位置：'left', 'right' |
| `image` | String | `null` | 图片URL（格式：image） |
| `imagePos` | String | `null` | 图片背景位置 |
| `imageBgSize` | String | `null` | 图片背景尺寸 |
| `imageClass` | String | `''` | 图标CSS类名 |
| `iconFontCode` | String | `null` | 图标字体代码 |
| `caption` | String | `undefined` | 标题文本（继承自iniProp） |
| `excelCellId` | String | `''` | Excel单元格ID，用于与Excel集成 |

## 方法

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
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `fireClickEvent()`
触发点击事件，模拟用户点击。

**返回：**
- (Object): 组件实例，支持链式调用。

### `activate()`
激活复选框，使其获得焦点。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_setCtrlValue(value)`
内部方法，设置控件显示值。

**参数：**
- `value` (Boolean): 是否选中

**返回：**
- (Object): 组件实例，支持链式调用。

### `_setDirtyMark()`
内部方法，设置脏数据标记。

### `notifyExcel()`
通知Excel单元格值变化。

### `getExcelCellValue()`
获取Excel单元格值。

## 事件

### `onChecked(profile, e, value)`
复选框状态改变时触发。

**参数：**
- `profile` (Object): 组件profile对象
- `e` (Event): 事件对象
- `value` (Boolean): 新的选中状态

### `onGetExcelCellValue(profile, excelCellId, dftValue)`
获取Excel单元格值时触发。

## 示例

### 基本复选框

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/CheckBox.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-checkbox"></div>
    
    <script>
    // 创建复选框
    var myCheckbox = ood.UI.CheckBox({
        caption: '启用通知',
        value: true,
        theme: 'dark',
        responsive: true,
        hAlign: 'left',
        iconPos: 'left'
    }).appendTo('#my-checkbox');
    
    // 监听状态变化
    myCheckbox.on('onChecked', function(profile, e, value) {
        console.log('复选框状态:', value ? '选中' : '未选中');
    });
    </script>
</body>
</html>
```

### 表单中的复选框组

```html
<form id="settings-form">
    <div id="checkbox-group"></div>
</form>

<script>
// 创建多个复选框
var notifications = ood.UI.CheckBox({
    caption: '消息通知',
    value: true,
    id: 'notifications'
}).appendTo('#checkbox-group');

var emailUpdates = ood.UI.CheckBox({
    caption: '邮件更新',
    value: false,
    id: 'emailUpdates'
}).appendTo('#checkbox-group');

var smsAlerts = ood.UI.CheckBox({
    caption: '短信提醒',
    value: true,
    id: 'smsAlerts'
}).appendTo('#checkbox-group');
</script>
```

### 响应式复选框

```html
<div id="responsive-checkbox"></div>

<script>
var responsiveCheckbox = ood.UI.CheckBox({
    caption: '响应式复选框示例',
    value: false,
    responsive: true,
    width: '100%',
    height: '40px'
}).appendTo('#responsive-checkbox');
</script>
```

## 注意事项

1. 复选框的 `caption` 属性支持图标类名（如 'ri ri-checkbox-line'）和文本混合。
2. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
3. 响应式设计通过添加 CSS 类（`checkbox-mobile`, `checkbox-tiny`）实现，根据屏幕宽度自动调整样式。
4. 可访问性增强功能会自动为复选框添加 ARIA 属性（`role="checkbox"`, `aria-checked`, `aria-label` 等）。
5. 支持键盘操作：空格键切换选中状态，Tab 键在表单元素间导航。
6. 图标位置 (`iconPos`) 控制图标相对于标题的位置（左侧或右侧）。
7. 支持多种图标显示方式：字体图标 (`imageClass`)、图片 URL (`image`)、背景图片 (`icon`)。
8. Excel 集成功能 (`excelCellId`) 允许复选框与 Excel 单元格双向同步数据。
9. 垂直对齐 (`vAlign`) 和水平对齐 (`hAlign`) 属性控制组件内部内容的对齐方式。