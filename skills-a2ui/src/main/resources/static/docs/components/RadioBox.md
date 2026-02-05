# RadioBox

单选按钮组组件，用于创建单选或复选框选项组，支持主题切换、响应式设计和可访问性增强。

## 类名
`ood.UI.RadioBox`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/RadioBox.js"></script>

<!-- 创建单选按钮组 -->
<div id="radiobox-container"></div>

<script>
var radioBox = ood.UI.RadioBox({
    items: [
        {id: 'option1', caption: '选项1', value: '1'},
        {id: 'option2', caption: '选项2', value: '2'},
        {id: 'option3', caption: '选项3', value: '3'},
        {id: 'option4', caption: '选项4', value: '4', disabled: true}
    ],
    value: 'option1',
    checkBox: false,
    selMode: 'multibycheckbox',
    caption: '选择',
    theme: 'light',
    responsive: true
}).appendTo('#radiobox-container');

// 监听选项变化
radioBox.on('change', function(value) {
    console.log('选中选项:', value);
});
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 4个预定义选项 | 选项配置数组，每个项包含 id、caption、value、disabled 等 |
| `value` | String | `'option1'` | 当前选中的选项ID |
| `checkBox` | Boolean | `true` | 是否显示为复选框样式（false为单选按钮样式） |
| `selMode` | String | `'multibycheckbox'` | 选择模式 |
| `caption` | String | `'选择'` | 组件标题 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `expression` | String | `''` | 表达式 |
| `tagCmds` | String | `null` | 标签命令 |
| `borderType` | String | `'none'` | 边框样式 |
| `checkBox` | Boolean | `false` | 是否为复选框模式 |

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

### `RadioBoxTrigger()`
单选按钮组触发器，初始化现代化功能。

**返回：**
- (Object): 组件实例，支持链式调用。

## 内部方法

### `_prepareItem(profile, item)`
准备选项项数据（内部方法）。

## 事件

### `onCmd`
命令触发时调用。

## 示例

### 基本单选按钮组

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/RadioBox.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="gender-selection"></div>
    
    <script>
    // 创建性别选择单选按钮组
    var genderRadio = ood.UI.RadioBox({
        items: [
            {id: 'male', caption: '男', value: 'M'},
            {id: 'female', caption: '女', value: 'F'},
            {id: 'other', caption: '其他', value: 'O'}
        ],
        value: 'male',
        checkBox: false,
        caption: '性别选择',
        theme: 'light',
        responsive: true
    }).appendTo('#gender-selection');
    
    // 获取当前选中的值
    console.log('当前选中:', genderRadio.value());
    </script>
</body>
</html>
```

### 复选框模式

```html
<div id="hobbies-selection"></div>

<script>
var hobbiesCheckbox = ood.UI.RadioBox({
    items: [
        {id: 'reading', caption: '阅读', value: 'read'},
        {id: 'sports', caption: '运动', value: 'sport'},
        {id: 'music', caption: '音乐', value: 'music'},
        {id: 'travel', caption: '旅行', value: 'travel'}
    ],
    value: '', // 复选框模式初始可为空
    checkBox: true,
    selMode: 'multibycheckbox',
    caption: '兴趣爱好',
    theme: 'dark',
    responsive: true
}).appendTo('#hobbies-selection');

// 复选框模式支持多选，值处理方式不同
</script>
```

### 响应式选项组

```html
<div class="form-group">
    <label>订阅选项:</label>
    <div id="subscription-options"></div>
</div>

<script>
var subscriptionRadio = ood.UI.RadioBox({
    items: [
        {id: 'monthly', caption: '月度订阅', value: 'month'},
        {id: 'quarterly', caption: '季度订阅', value: 'quarter'},
        {id: 'yearly', caption: '年度订阅', value: 'year', disabled: false}
    ],
    value: 'monthly',
    checkBox: false,
    caption: '订阅周期',
    theme: 'light',
    responsive: true,
    width: '100%'
}).appendTo('#subscription-options');

// 响应式设计根据屏幕宽度自动调整布局：
// - 小于480px：tiny模式
// - 小于768px：mobile模式
// - 小于1024px：small模式
</script>
```

## 注意事项

1. 组件默认包含4个预定义选项（选项1-4），其中选项4为禁用状态。
2. `checkBox` 属性控制显示样式：
   - `true`: 显示为复选框样式，支持多选（需配合相应选择模式）
   - `false`: 显示为传统单选按钮样式
3. `selMode` 属性定义选择行为模式，'multibycheckbox' 表示通过复选框进行多选。
4. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
5. 响应式设计根据屏幕宽度自动应用CSS类：
   - `radiobox-tiny`: 宽度 < 480px
   - `radiobox-mobile`: 480px ≤ 宽度 < 768px
   - `radiobox-small`: 768px ≤ 宽度 < 1024px
6. 可访问性增强功能自动添加ARIA属性：
   - 容器：`role="radiogroup"`，`aria-label` 使用caption属性
   - 每个选项：`role="radio"`，`aria-checked` 反映选中状态
   - 支持键盘导航（Tab键切换焦点，Enter/Space键选择）
7. 选项项支持 `disabled` 属性，禁用状态下无法选择和交互。
8. 组件使用Remix Icon字体库，选项图标可通过 `imageClass` 属性配置。