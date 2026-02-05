# DatePicker

日期选择器组件，提供直观的日历界面，支持日期选择、时间输入、主题切换、响应式布局和国际化格式。常用于表单输入、日期筛选、日程安排等场景。

## 类名
`ood.UI.DatePicker`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/DatePicker.js"></script>

<!-- 创建日期选择器 -->
<div id="datepicker-container"></div>

<script>
var datePicker = ood.UI.DatePicker({
    value: new Date(),
    theme: 'light',
    responsive: true,
    timeInput: false,
    closeBtn: true,
    firstDayOfWeek: 0,
    offDays: '60',
    hideWeekLabels: false,
    dateInputFormat: "yyyy-mm-dd"
}).appendTo('#datepicker-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `timeInput` | Boolean | `false` | 是否启用时间输入（支持选择具体时间） |
| `height` | String | `'auto'` | 组件高度（只读，CSS尺寸） |
| `width` | String | `'auto'` | 组件宽度（只读，CSS尺寸） |
| `value` | Date | `new Date()` | 当前选择的日期（Date对象） |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |
| `firstDayOfWeek` | Number | `0` | 每周的第一天：0=周日，1=周一 |
| `offDays` | String | `'60'` | 禁用日期设置（字符串格式） |
| `hideWeekLabels` | Boolean | `false` | 是否隐藏星期标签 |
| `dateInputFormat` | String | `"yyyy-mm-dd"` | 日期输入格式：'yyyy-mm-dd', 'mm-dd-yyyy', 'dd-mm-yyyy' |

## 方法

### `activate()`
激活日期选择器，将焦点设置到切换按钮。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setTheme(theme)`
设置组件主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称（优先从localStorage恢复）。

### `toggleDarkMode()`
切换暗黑模式（在light和dark之间切换）。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `getDateFrom()`
获取当前选择的日期（起始日期）。

**返回：**
- (Date): 当前选择的日期对象。

### `_setCtrlValue(value)`
内部方法，设置控件值并更新显示。

**参数：**
- `value` (Date): 要设置的日期值

**返回：**
- (Object): 组件实例，支持链式调用。

### `_prepareData(profile)`
准备渲染数据的内部方法，处理关闭按钮、时间输入等显示逻辑。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 渲染数据对象。

### `_ensureValue(profile, value)`
确保日期值有效的内部方法，处理日期格式转换。

**参数：**
- `profile` (Object): 组件配置对象
- `value` (any): 输入的日期值（Date对象、时间戳或字符串）

**返回：**
- (Date): 处理后的有效日期对象。

### `DatePickerTrigger()`
初始化日期选择器触发器的内部方法，用于设置主题和响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

组件支持以下事件：

### 自定义事件
- `beforeClose` - 关闭前触发，可用于阻止关闭或执行清理操作

### 内置事件
组件继承自 `ood.UI` 和 `ood.absValue`，支持以下内置事件：
- `change` - 日期值改变时触发
- `focus` - 获得焦点时触发
- `blur` - 失去焦点时触发

## 示例

### 基本日期选择器

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/DatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-datepicker"></div>
    
    <script>
    // 创建日期选择器
    var picker = ood.UI.DatePicker({
        value: new Date(),
        theme: 'light',
        responsive: true,
        closeBtn: true
    }).appendTo('#my-datepicker');
    
    // 监听日期变化
    picker.on('change', function(dateValue) {
        console.log('日期已改变:', dateValue);
    });
    
    // 切换主题
    picker.toggleDarkMode();
    </script>
</body>
</html>
```

### 带时间输入的日期选择器

```html
<div id="datetime-picker"></div>

<script>
var datetimePicker = ood.UI.DatePicker({
    value: new Date(),
    theme: 'light',
    responsive: true,
    timeInput: true, // 启用时间输入
    dateInputFormat: "yyyy-mm-dd"
}).appendTo('#datetime-picker');
</script>
```

### 自定义工作日设置

```html
<div id="custom-datepicker"></div>

<script>
var customPicker = ood.UI.DatePicker({
    value: new Date(),
    theme: 'dark',
    responsive: true,
    firstDayOfWeek: 1, // 周一开始
    offDays: '60', // 禁用日期设置
    hideWeekLabels: false
}).appendTo('#custom-datepicker');
</script>
```

## 注意事项

1. 组件依赖 `ood.Date` 库，确保在引入DatePicker.js之前已加载ood.js核心库。
2. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
3. 启用 `timeInput` 属性时，组件会显示时间输入控件，支持选择具体的时间点。
4. `firstDayOfWeek` 属性影响日历的显示方式，0表示周日为一周的第一天，1表示周一。
5. `offDays` 属性用于设置禁用日期，具体格式请参考相关文档。
6. `dateInputFormat` 属性控制日期输入框的显示格式，不影响内部Date对象的值。
7. 响应式设计通过CSS媒体查询和JavaScript动态调整实现，确保在不同设备上的良好显示。
8. 组件支持触摸手势操作，适合移动端使用。