# Label

文本标签组件，支持图标、时钟、Excel公式、主题切换、响应式设计和可访问性增强。常用于表单标签、状态显示、动态时钟等场景。

## 类名
`ood.UI.Label`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Label.js"></script>

<!-- 创建标签 -->
<div id="label-container"></div>

<script>
var label = ood.UI.Label({
    caption: '用户名',
    theme: 'light',
    responsive: true,
    hAlign: 'left',
    vAlign: 'middle',
    iconFontCode: 'ri-user-line'
}).appendTo('#label-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `caption` | String | `'文本标签'` | 标签显示的文本内容 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'light'` | 主题样式 | `'light'`, `'dark'` |
| `responsive` | Boolean | `true` | 是否启用响应式设计 | - |
| `selectable` | Boolean | `true` | 是否可选择文本 | - |
| `expression` | String | `''` | 表达式 | - |
| `caption` | String | `null` | 标题文本 | - |
| `clock` | String | `''` | 时钟格式 | `'hh : mm : ss'`, `'hh - mm : ss'` |
| `image` | Image | - | 图像 | 格式为 `image` |
| `imagePos` | String | `''` | 图像位置 | CSS `background-position` 值 |
| `imageBgSize` | String | `''` | 图像背景大小 | CSS `background-size` 值 |
| `imageClass` | String | `''` | 图像类名 | 用于自定义图标样式 |
| `iconFontCode` | String | `''` | 图标字体代码 | 字体图标代码 |
| `iconFontColor` | String | `''` | 图标字体颜色 | 颜色值 |
| `hAlign` | String | `'right'` | 水平对齐方式 | `'left'`, `'center'`, `'right'` |
| `vAlign` | String | `'top'` | 垂直对齐方式 | `'top'`, `'middle'`, `'bottom'` |
| `fontColor` | Color | - | 字体颜色 | 继承自 `ood.UI.Button` |
| `fontSize` | String | - | 字体大小 | 继承自 `ood.UI.Button` |
| `fontWeight` | String | - | 字体粗细 | 继承自 `ood.UI.Button` |
| `fontFamily` | String | - | 字体 | 继承自 `ood.UI.Button` |
| `excelCellFormula` | String | `''` | Excel单元格公式 | - |

## 实例方法

### `fireClickEvent()`
触发点击事件，调用绑定的 `onClick` 事件处理器。

**返回值**: `this`（支持链式调用）

### `_applyExcelFormula(cellsMap)`
计算Excel公式并应用到标签文本。

| 参数 | 类型 | 描述 |
|------|------|------|
| `cellsMap` | Object | Excel单元格映射表 |

### `setTheme(theme)`
设置主题样式。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'` 或 `'dark'` |

**返回值**: `this`（支持链式调用）

### `getTheme()`
获取当前主题。

**返回值**: `String` - 当前主题（`'light'` 或 `'dark'`）

### `LabelTrigger()`
初始化现代化功能：主题、响应式设计、可访问性增强。在组件渲染后自动调用。

### `toggleDarkMode()`
切换暗黑模式（在 `light` 和 `dark` 之间切换）。

**返回值**: `this`（支持链式调用）

### `adjustLayout()`
响应式布局调整。根据屏幕宽度自动调整样式，添加/移除移动端类名。

**返回值**: `this`（支持链式调用）

### `enhanceAccessibility()`
增强可访问性支持。添加ARIA属性，为时钟模式设置适当的角色。

**返回值**: `this`（支持链式调用）

## 静态方法

### `Templates`
组件模板定义：
- `tagName`: `"label"`
- `VALIGN`: 垂直对齐占位符
- `ICON`: 图标区域，支持字体图标和背景图片
- `CAPTION`: 文本内容区域

### `Appearances`
样式定义：
- `VALIGN`: 垂直对齐样式
- `label-dark ICON/CAPTION`: 暗黑模式样式
- `label-mobile ICON/CAPTION`: 移动端样式
- `label-tiny CAPTION`: 小屏幕样式

### `DataModel`
数据模型定义（见上方属性表）。

### `Behaviors`
行为定义：
- `HoverEffected`: 悬停效果
- `onClick`: 点击事件处理

### `EventHandlers`
事件处理器：
- `onClick`: 点击事件回调
- `beforeApplyExcelFormula`: 应用Excel公式前回调
- `afterApplyExcelFormula`: 应用Excel公式后回调

### `RenderTrigger()`
渲染触发器，负责初始化时钟和现代化功能。

### `_prepareData(profile, data)`
预处理模板数据，处理对齐、字体等样式。

## 事件

### `onClick`
点击标签时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

### `beforeApplyExcelFormula`
应用Excel公式前触发，可返回 `false` 阻止应用。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `excelCellFormula` | String | Excel公式 |
| `value` | Any | 计算后的值 |

### `afterApplyExcelFormula`
应用Excel公式后触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `excelCellFormula` | String | Excel公式 |
| `value` | Any | 计算后的值 |

## 使用示例

### 基础标签
```javascript
var label = ood.UI.Label({
    caption: '欢迎使用OOD',
    hAlign: 'center',
    fontSize: '18px'
}).appendTo('#container');
```

### 带图标的标签
```javascript
var label = ood.UI.Label({
    caption: '消息通知',
    iconFontCode: 'ri-notification-line',
    iconFontColor: '#1890ff'
}).appendTo('#container');
```

### 时钟标签
```javascript
var label = ood.UI.Label({
    caption: '当前时间',
    clock: 'hh : mm : ss'
}).appendTo('#container');
```

### 响应式标签（自动适应屏幕）
```javascript
var label = ood.UI.Label({
    caption: '响应式标签',
    responsive: true,
    theme: 'dark'
}).appendTo('#container');
```

### Excel公式标签
```javascript
var label = ood.UI.Label({
    caption: '计算结果',
    excelCellFormula: 'SUM(A1:A10)'
}).appendTo('#container');
```

## 现代化功能

### 主题切换
组件支持 `light` 和 `dark` 两种主题，可通过 `setTheme()` 方法动态切换，主题设置会保存到 `localStorage`。

### 响应式设计
当 `responsive` 属性为 `true` 时，组件会自动根据屏幕宽度调整样式：
- 屏幕宽度 `< 768px`：添加 `label-mobile` 类，缩小字体
- 屏幕宽度 `< 480px`：添加 `label-tiny` 类，进一步缩小字体

### 可访问性
组件自动添加ARIA属性：
- 默认角色：`role="text"`
- 时钟模式：`role="timer"`, `aria-live="polite"`
- 图标隐藏：装饰性图标添加 `aria-hidden="true"`

## 兼容性说明
- `ood.UI.SLabel` 是 `ood.UI.Label` 的别名，确保向后兼容。
- 支持Excel公式计算，需确保已加载 `ood.ExcelFormula` 模块。