# TimePicker

TimePicker时间选择器组件，提供直观的时间选择界面，支持小时和分钟选择，具有现代化的主题切换、响应式设计和可访问性增强功能。

## 类名
`ood.UI.TimePicker`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/TimePicker.js"></script>

<!-- 创建时间选择器容器 -->
<div id="timepicker-container"></div>

<script>
var timepicker = ood.UI.TimePicker({
    width: '20em',
    height: '25em',
    theme: 'light',
    responsive: true,
    value: '14:30',
    closeBtn: true
}).appendTo('#timepicker-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题样式：'light'（亮色）, 'dark'（暗色） |
| `responsive` | Boolean | `true` | 是否启用响应式设计，自动适配不同屏幕尺寸 |
| `expression` | String | `''` | 表达式 |
| `height` | String | `'auto'` | 组件高度（只读属性） |
| `width` | String | `'auto'` | 组件宽度（只读属性） |
| `value` | String | `'00:00'` | 当前选中的时间值，格式为"HH:MM" |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `patterned` | Boolean | `false` | 是否启用图案背景 |
| `dock` | String | `'fill'` | 停靠方式：'fill'（填充）, 'left'（左停靠）, 'right'（右停靠）, 'top'（上停靠）, 'bottom'（下停靠） |

## 方法

### `activate()`
激活时间选择器，将焦点设置到输入框。

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
timepicker.activate();
```

### `setTheme(theme)`
设置时间选择器主题。

**参数：**
- `theme` (String): 主题名称，支持'light'（亮色）, 'dark'（暗色）

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
timepicker.setTheme('dark');
```

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称

**示例：**
```javascript
var currentTheme = timepicker.getTheme();
console.log('当前主题:', currentTheme);
```

### `toggleDarkMode()`
切换暗黑模式（在light和dark之间切换）。

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
timepicker.toggleDarkMode();
```

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。在小屏幕设备上优化显示。

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
timepicker.adjustLayout();
```

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用

**示例：**
```javascript
timepicker.enhanceAccessibility();
```

### `_setCtrlValue(value)`
内部方法：设置控件值。

**参数：**
- `value` (String): 时间值，格式为"HH:MM"

**返回：**
- (Object): 组件实例

### `_ensureValue(profile, value)`
内部方法：确保时间值有效。

**参数：**
- `profile` (Object): UI配置对象
- `value` (String|Array): 时间值

**返回：**
- (String): 格式化的有效时间值

### `formatValue(value)`
格式化时间值。

**参数：**
- `value` (Array): 时间数组 [小时, 分钟]

**返回：**
- (String): 格式化的时间字符串

### `_v2a(v)`
内部方法：将时间字符串转换为数组。

**参数：**
- `v` (String|Array): 时间值

**返回：**
- (Array): 时间数组 [小时, 分钟]

### `_showV(profile, a)`
内部方法：显示时间值。

**参数：**
- `profile` (Object): UI配置对象
- `a` (Array): 时间数组

**返回：**
- (String): 显示的字符串

## 事件

### `beforeClose(profile, src)`
关闭前事件，在时间选择器关闭前触发。

**参数：**
- `profile` (Object): UI配置对象
- `src` (Object): 事件源DOM元素

**返回：**
- (Boolean): 返回false可阻止关闭

**示例：**
```javascript
timepicker.beforeClose = function(profile, src) {
    if (confirm('确定要关闭时间选择器吗？')) {
        return true;
    } else {
        return false;
    }
};
```

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 组件容器样式 |
| `TBART, BBART` | 顶部和底部工具栏表格样式 |
| `MAINI` | 主区域内部容器样式 |
| `CONH` | 小时选择区域样式 |
| `CONM` | 分钟选择区域样式 |
| `BARCMDL` | 中间命令栏样式 |
| `PRE, PRE2, NEXT, NEXT2` | 导航按钮样式（减小时、增加小时、减少分钟、增加分钟） |
| `HOUR, MINUTE` | 小时和分钟输入框样式 |
| `SET` | 设置按钮样式 |
| `TAILI` | 底部区域内部容器样式 |
| `CAPTION` | 标题样式 |
| `.oodex-timepicker` | 时间选择项基础样式 |
| `.oodex-timepicker3` | 小时选择项特殊样式 |

**详细CSS属性：**
- `TBART, BBART`: `border-spacing: 0; border-collapse: separate`
- `MAINI`: `padding: .5em .3333em .3333em 0`
- `CONH`: `white-space: nowrap; text-align: center`
- `CONM`: `margin-top: .5em; white-space: nowrap; text-align: center`
- `BARCMDL`: `top: .125em`
- `PRE, PRE2, NEXT, NEXT2`: `position: relative; margin: 0 .25em; vertical-align: middle; cursor: default`
- `HOUR, MINUTE`: `font-weight: bold; vertical-align: middle; cursor: e-resize; margin: 0 .25em; padding: 0 .25em`
- `SET`: `position: absolute; display: none; top: .125em; right: .5em`
- `TAILI`: `position: relative; padding: .5em 0 0 0; text-align: center`
- `CAPTION`: `vertical-align: baseline/middle; font-size: 1em`
- `.oodex-timepicker`: `width: 2em; text-align: center; padding: .25em 0; margin: 0`
- `.oodex-timepicker3`: `width: 1.6666em; text-align: center; font-weight: bold; padding: .25em 0; margin: 0`

## 示例

### 基本时间选择器

```html
<div id="basic-timepicker"></div>

<script>
var basicTimepicker = ood.UI.TimePicker({
    width: '300px',
    height: '350px',
    theme: 'light',
    responsive: true,
    value: '09:00',
    closeBtn: true
}).appendTo('#basic-timepicker');

// 监听时间变化
basicTimepicker.on('change', function() {
    console.log('选择的时间:', basicTimepicker.getUIValue());
});
</script>
```

### 暗黑主题时间选择器

```html
<div id="dark-timepicker"></div>
<button onclick="toggleDarkMode()">切换主题</button>

<script>
var darkTimepicker = ood.UI.TimePicker({
    width: '280px',
    height: '320px',
    theme: 'dark',
    responsive: true,
    value: '18:45',
    closeBtn: true
}).appendTo('#dark-timepicker');

function toggleDarkMode() {
    darkTimepicker.toggleDarkMode();
}
</script>
```

### 响应式时间选择器

```html
<div class="timepicker-responsive">
    <h3>预约时间选择</h3>
    <div id="responsive-timepicker"></div>
</div>

<style>
.timepicker-responsive {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

@media (max-width: 768px) {
    .timepicker-responsive {
        padding: 10px;
    }
}
</style>

<script>
var responsiveTimepicker = ood.UI.TimePicker({
    src: '/plugins/timepicker/interface.html',
    width: '100%',
    height: '400px',
    responsive: true,
    params: {
        timeSlots: ['09:00', '10:00', '11:00', '14:00', '15:00', '16:00'],
        timezone: 'Asia/Shanghai'
    }
}).appendTo('#responsive-timepicker');

// 窗口大小变化时调整布局
window.addEventListener('resize', function() {
    responsiveTimepicker.adjustLayout();
});
</script>
```

### 自定义时间范围

```html
<div id="custom-range-timepicker"></div>
<button onclick="setMorning()">上午时段</button>
<button onclick="setAfternoon()">下午时段</button>

<script>
var customTimepicker = ood.UI.TimePicker({
    width: '320px',
    height: '360px',
    theme: 'light',
    responsive: true,
    value: '08:30'
}).appendTo('#custom-range-timepicker');

function setMorning() {
    // 设置上午时段：08:00 - 12:00
    customTimepicker.setTheme('light');
    customTimepicker.setUIValue('08:00');
}

function setAfternoon() {
    // 设置下午时段：13:00 - 17:00
    customTimepicker.setTheme('dark');
    customTimepicker.setUIValue('13:00');
}
</script>
```

### 表单集成

```html
<form id="appointment-form">
    <div class="form-group">
        <label for="appointment-time">预约时间：</label>
        <div id="appointment-time"></div>
    </div>
    <button type="submit">提交预约</button>
</form>

<script>
var appointmentTimepicker = ood.UI.TimePicker({
    width: '250px',
    height: '300px',
    theme: 'light',
    responsive: true,
    value: '10:00',
    closeBtn: false
}).appendTo('#appointment-time');

// 表单提交时获取时间值
document.getElementById('appointment-form').addEventListener('submit', function(e) {
    e.preventDefault();
    var selectedTime = appointmentTimepicker.getUIValue();
    alert('您预约的时间是：' + selectedTime);
    // 实际应用中，这里可以将数据发送到服务器
});
</script>
```

## 注意事项

1. **主题系统**：支持light和dark两种主题，可通过`setTheme()`方法动态切换，主题偏好会自动保存到localStorage。

2. **响应式设计**：组件自动适配不同屏幕尺寸，在移动端会自动调整布局和元素大小。

3. **可访问性**：组件支持ARIA属性，增强屏幕阅读器兼容性，提供完整的键盘导航支持。

4. **时间格式**：时间值使用24小时制，格式为"HH:MM"，小时范围0-23，分钟范围0-59。

5. **值验证**：输入的时间值会自动验证和格式化，确保始终为有效时间。

6. **交互方式**：
   - 可通过点击小时/分钟数字直接选择
   - 可通过导航按钮（← →）调整时间
   - 支持鼠标拖动调整小时和分钟

7. **键盘导航**：
   - Tab键：在组件元素间切换焦点
   - 方向键：调整选中时间
   - 空格键/回车键：确认选择

8. **事件处理**：提供完整的事件系统，可监听时间变化、关闭等操作。

9. **CSS定制**：通过Appearances定义的CSS类可自定义组件样式，支持主题化设计。

10. **浏览器兼容性**：基于现代Web标准，要求现代浏览器支持。旧版IE需要兼容性处理。

11. **性能优化**：组件内部使用事件代理，减少内存占用，提高大型应用的性能。

12. **移动端适配**：在移动设备上提供优化交互体验，支持触摸操作。

13. **国际化**：支持不同地区的时间格式，可通过CSS和JavaScript定制显示格式。

14. **错误处理**：输入无效时间时会自动纠正为最近的有效时间，提供友好的用户体验。