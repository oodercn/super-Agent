# List

高级列表组件，支持单选、多选、复选框选择、主题切换、响应式设计和可访问性增强。提供丰富的配置选项，适用于各种列表展示和选择场景。

## 类名
`ood.UI.List`

## 继承
`ood.UI`, `ood.absList`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/List.js"></script>

<!-- 创建列表容器 -->
<div id="list-container"></div>

<script>
var list = ood.UI.List({
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'settings', caption: '设置', imageClass: 'ri-settings-line'},
        {id: 'profile', caption: '个人资料', imageClass: 'ri-user-line', disabled: true},
        {id: 'help', caption: '帮助', imageClass: 'ri-question-line'}
    ],
    selMode: 'single',
    theme: 'light',
    responsive: true,
    width: 320,
    height: '15em',
    maxHeight: 420,
    borderType: 'flat',
    labelCaption: '功能列表',
    labelPos: 'left',
    labelSize: '4em'
}).appendTo('#list-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 预定义的4个示例项 | 列表项数组，每个项包含 id、caption、imageClass、disabled 等属性 |
| `theme` | String | `'light'` | 主题样式 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `selMode` | String | `'single'` | 选择模式 |
| `borderType` | String | `'flat'` | 边框类型 |
| `noCtrlKey` | Boolean | `true` | 多选时是否需要Ctrl键 |
| `width` | Number | `320` | 列表宽度 |
| `height` | String | `'15em'` | 列表高度 |
| `maxHeight` | Number | `420` | 最大高度限制 |
| `itemRow` | String | `''` | 列表项行类型 |
| `optBtn` | String | `""` | 选项按钮样式类 |
| `tagCmds` | Array | `[]` | 标签命令数组 |
| `tagCmdsAlign` | String | `"right"` | 标签命令对齐方式 |
| `labelSize` | String | `'4em'` | 标签尺寸 |
| `labelPos` | String | `"left"` | 标签位置 |
| `labelGap` | Number | `4` | 标签与列表的间距 |
| `labelCaption` | String | `'$RAD.widgets.optionList'` | 标签标题文本 |
| `labelHAlign` | String | `'right'` | 标签水平对齐 |
| `labelVAlign` | String | `'top'` | 标签垂直对齐 |
| `value` | String | `'a'` | 当前选中的列表项ID |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'light'` | 主题样式 | `'light'`, `'dark'`, `'high-contrast'` |
| `responsive` | Boolean | `true` | 是否启用响应式设计 | - |
| `expression` | String | `''` | 表达式 | - |
| `selMode` | String | `'single'` | 选择模式 | `'single'` (单选), `'none'` (不可选), `'multi'` (多选), `'multibycheckbox'` (复选框多选) |
| `borderType` | String | `'flat'` | 边框类型 | `'none'`, `'flat'`, `'inset'`, `'outset'` |
| `noCtrlKey` | Boolean | `true` | 多选时是否需要Ctrl键 | - |
| `width` | String | `'auto'` | 列表宽度 | 支持CSS单位，`$spaceunit: 1` |
| `height` | String | `'15em'` | 列表高度 | 支持CSS单位，`$spaceunit: 1` |
| `maxHeight` | Number | `420` | 最大高度限制 | - |
| `itemRow` | String | `''` | 列表项行类型 | `'row'`, `'cell'` |
| `optBtn` | String | `""` | 选项按钮样式类 | `'ood-uicmd-opt'`, `'ood-icon-singleright'` |
| `tagCmds` | Array | `[]` | 标签命令数组 | - |
| `tagCmdsAlign` | String | `"right"` | 标签命令对齐方式 | `'left'`, `'right'`, `'floatright'` |
| `labelSize` | String | `0` | 标签尺寸 | `$spaceunit: 2` |
| `labelPos` | String | `"left"` | 标签位置 | `'none'`, `'left'`, `'top'`, `'right'`, `'bottom'` |
| `labelGap` | Number | `4` | 标签与列表的间距 | `$spaceunit: 2` |
| `labelCaption` | String | `""` | 标签标题文本 | - |
| `labelHAlign` | String | `'right'` | 标签水平对齐 | `''`, `'left'`, `'center'`, `'right'` |
| `labelVAlign` | String | `'top'` | 标签垂直对齐 | `''`, `'top'`, `'middle'`, `'bottom'` |

## 实例方法

### `setTheme(theme)`
设置主题样式。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'`, `'dark'`, `'high-contrast'` |

**返回值**: `this`（支持链式调用）

### `getTheme()`
获取当前主题。

**返回值**: `String` - 当前主题

### `ListTrigger()`
初始化现代化功能：主题、响应式设计、可访问性增强。在组件渲染后自动调用。

### `toggleTheme()`
循环切换主题（light → dark → high-contrast → light）。

**返回值**: `this`（支持链式调用）

### `adjustLayout()`
响应式布局调整。根据屏幕宽度自动调整样式，添加/移除移动端类名。

**返回值**: `this`（支持链式调用）

### `enhanceAccessibility()`
增强可访问性支持。为容器、列表、标签和列表项添加ARIA属性。

**返回值**: `this`（支持链式调用）

### `_setCtrlValue(value)`
设置控件值（内部使用）。

| 参数 | 类型 | 描述 |
|------|------|------|
| `value` | Any | 控件值 |

**返回值**: `this`（支持链式调用）

### `_clearMouseOver()`
清除鼠标悬停状态（内部使用）。

### `adjustSize()`
调整列表尺寸，考虑最大高度限制。

**返回值**: `this`（支持链式调用）

### `activate()`
激活列表（继承自 `ood.absList`）。

### `setCaptionValue(value)`
设置标题值（继承自 `ood.absValue`）。

### `getCaptionValue()`
获取标题值（继承自 `ood.absValue`）。

### `getSelectedItem()`
获取选中的列表项（继承自 `ood.absList`）。

### `getShowValue(value)`
获取显示值。

| 参数 | 类型 | 描述 |
|------|------|------|
| `value` | String | 列表项ID |

**返回值**: `String` - 对应的标题文本

### `_setDirtyMark()`
设置脏数据标记（内部使用）。

## 静态方法

### `Templates`
组件模板定义：
- `tagName`: `'div'`
- `LABEL`: 标签区域
- `ITEMS`: 列表项容器
- `ITEM`: 单个列表项
- `CAPTION`: 列表项标题
- `ICON`: 列表项图标
- `EXTRA`: 列表项额外信息
- `MARK`: 选择标记
- `CMD`: 命令按钮

### `DataModel`
数据模型定义（见上方属性表）。

### `EventHandlers`
事件处理器定义：
- `onClick`: 点击列表项时触发
- `onCmd`: 点击命令按钮时触发
- `beforeClick`: 点击前触发
- `afterClick`: 点击后触发
- `onDblclick`: 双击列表项时触发
- `onShowOptions`: 显示选项时触发
- `onItemSelected`: 列表项被选中时触发
- `onLabelClick`: 点击标签时触发
- `onLabelDblClick`: 双击标签时触发
- `onLabelActive`: 标签激活时触发

### `_onStartDrag(profile, e, src, pos)`
开始拖拽处理（内部使用）。

### `_onDropTest(profile, e, src, key, data, item)`
拖拽放置测试（内部使用）。

### `_onDrop(profile, e, src, key, data, item)`
拖拽放置处理（内部使用）。

### `_prepareData(profile)`
预处理模板数据，处理边框类型、标签样式等。

### `_prepareItem(profile, item, oitem, pid, index, len)`
准备单个列表项数据，设置选择标记、行类型等。

### `RenderTrigger()`
渲染触发器，负责调整尺寸和初始化现代化功能。

### `_onresize(profile, width, height)`
大小改变时的处理函数。

## 事件

### `onClick`
点击列表项时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `item` | Object | 被点击的列表项 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

### `onCmd`
点击命令按钮时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `item` | Object | 对应的列表项 |
| `cmdkey` | String | 命令按钮键名 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

### `beforeClick`
点击列表项前触发，可返回 `false` 阻止点击。

### `afterClick`
点击列表项后触发。

### `onDblclick`
双击列表项时触发。

### `onShowOptions`
显示选项时触发。

### `onItemSelected`
列表项被选中时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `item` | Object | 被选中的列表项 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |
| `type` | String | 选择类型 |

### `onLabelClick`
点击标签时触发。

### `onLabelDblClick`
双击标签时触发。

### `onLabelActive`
标签激活时触发。

## 使用示例

### 单选列表
```javascript
var singleList = ood.UI.List({
    items: [
        {id: 'red', caption: '红色'},
        {id: 'green', caption: '绿色'},
        {id: 'blue', caption: '蓝色'}
    ],
    selMode: 'single',
    value: 'red',
    width: 200,
    height: '10em'
}).appendTo('#container');
```

### 多选列表（复选框）
```javascript
var multiList = ood.UI.List({
    items: [
        {id: 'java', caption: 'Java'},
        {id: 'javascript', caption: 'JavaScript'},
        {id: 'python', caption: 'Python'},
        {id: 'csharp', caption: 'C#'}
    ],
    selMode: 'multibycheckbox',
    value: 'java,javascript',
    borderType: 'inset',
    labelCaption: '编程语言',
    labelPos: 'top'
});
```

### 带图标的列表
```javascript
var iconList = ood.UI.List({
    items: [
        {id: 'file', caption: '文件', imageClass: 'ri-file-line'},
        {id: 'folder', caption: '文件夹', imageClass: 'ri-folder-line'},
        {id: 'image', caption: '图片', imageClass: 'ri-image-line'},
        {id: 'video', caption: '视频', imageClass: 'ri-video-line'}
    ],
    selMode: 'single',
    theme: 'dark',
    responsive: true
});
```

### 动态更新列表项
```javascript
// 添加新项
list.insertItems([
    {id: 'new', caption: '新项目', imageClass: 'ri-add-line'}
]);

// 更新现有项
list.updateItem('settings', {
    caption: '系统设置',
    imageClass: 'ri-settings-3-line'
});

// 删除项
list.removeItems(['help']);
```

## 现代化功能

### 主题系统
- 支持 `light`、`dark`、`high-contrast` 三种主题
- 主题设置自动保存到 `localStorage`
- 可通过 `setTheme()`、`getTheme()`、`toggleTheme()` 方法管理

### 响应式设计
当 `responsive` 属性为 `true` 时，组件会根据屏幕宽度自动调整：
- 屏幕宽度 `< 768px`：添加 `list-mobile` 类，调整内边距和字体大小
- 屏幕宽度 `< 480px`：添加 `list-tiny` 类，隐藏额外信息，简化图标

### 可访问性增强
组件自动添加ARIA属性：
- 容器：`role="application"`, `aria-orientation`（根据布局方向）
- 列表：`role="listbox"`, `aria-label`, `aria-multiselectable`
- 标签：`role="label"`, `id`（用于关联）
- 列表项：`role="option"`（单选）或 `role="checkbox"`（多选），`aria-selected`, `aria-disabled`, `tabindex`

### 键盘导航
- 选中项可通过 `Tab` 键聚焦
- 聚焦状态下按 `Enter` 或 `Space` 触发点击事件
- 支持方向键导航（需在应用层面实现）

## 选择模式详解

### `single`（单选）
- 只能选中一个列表项
- 选中项显示勾选标记
- 值格式：单个ID字符串

### `multi`（多选）
- 可选中多个列表项
- 需配合 `Ctrl` 键（除非 `noCtrlKey: false`）
- 值格式：ID字符串，用分隔符连接

### `multibycheckbox`（复选框多选）
- 每个列表项显示复选框
- 可独立选择/取消选择
- 值格式：ID字符串，用分隔符连接

### `none`（不可选）
- 列表项不可选中
- 仅用于展示

## 拖拽支持
列表组件支持拖拽功能：
- 可拖拽列表项到其他可放置区域
- 支持拖拽排序（需在应用层面实现）
- 提供完整的拖拽事件API

## 性能优化建议
1. **虚拟滚动**：对于大量列表项，建议在应用层面实现虚拟滚动
2. **批量更新**：多个操作建议使用 `batchUpdate()` 包装
3. **惰性渲染**：隐藏的列表项可延迟渲染
4. **内存管理**：及时销毁不再使用的列表实例