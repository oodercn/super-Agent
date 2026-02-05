# Layout

高级布局容器组件，支持可调整大小的面板、主题切换、响应式设计和可访问性增强。提供水平和垂直两种布局类型，常用于创建复杂的界面布局，如侧边栏、主内容区、底部栏等。

## 类名
`ood.UI.Layout`

## 继承
`ood.UI`, `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Layout.js"></script>

<!-- 创建布局容器 -->
<div id="layout-container"></div>

<script>
var layout = ood.UI.Layout({
    type: 'horizontal', // 水平布局
    theme: 'light',
    responsive: true,
    width: '800px',
    height: '500px',
    items: [
        {id: 'before', pos: 'before', size: 200, min: 150, locked: false, cmd: true},
        {id: 'main', pos: 'main', size: 400},
        {id: 'after', pos: 'after', size: 200, min: 150, locked: false, cmd: true}
    ]
}).appendTo('#layout-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `caption` | String | `'布局'` | 布局标题 |
| `items` | Array | 预定义的 `main` 和 `before` 面板 | 布局面板项数组，每个项包含 id、pos、size、min、locked、folded、hidden、cmd 等属性 |

**items 数组结构示例**:
```javascript
items: [
    {id: 'before', pos: 'before', size: 220, locked: false, cmd: true},
    {id: 'main', pos: 'main', overflow: 'auto', transparent: true},
    {id: 'after', pos: 'after', size: 120, locked: false, cmd: false}
]
```

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'light'` | 主题样式 | `'light'`, `'dark'` |
| `responsive` | Boolean | `true` | 是否启用响应式设计 | - |
| `rotate` | Any | `null` | 旋转设置 | - |
| `selectable` | Boolean | `true` | 是否可选择文本 | - |
| `navComboType` | String | `'custom'` | 导航组合类型 | `'custom'`, `'galleryNav'`, `'menuBarNav'`, `'treeNav'`, `'foldingNav'` |
| `disabled` | Boolean | `null` | 是否禁用布局 | - |
| `position` | String | `'absolute'` | 定位方式 | - |
| `type` | String | `'horizontal'` | 布局类型 | `'vertical'` (垂直), `'horizontal'` (水平) |
| `dock` | String | `'fill'` | 停靠方式 | - |
| `listKey` | String | `null` | 列表键名 | - |
| `width` | String | `'18em'` | 布局宽度 | 支持CSS单位 |
| `height` | String | `'18em'` | 布局高度 | 支持CSS单位 |
| `dragSortable` | Boolean | `null` | 是否支持拖拽排序 | - |
| `flexSize` | Boolean | `false` | 是否启用弹性大小 | - |
| `transparent` | Boolean | `false` | 是否透明背景 | - |
| `items` | Array | `[]` | 布局面板项 | 同初始化属性中的 items |

## 实例方法

### `setChildren(childrens, prf)`
设置子组件。

| 参数 | 类型 | 描述 |
|------|------|------|
| `childrens` | Array | 子组件数组 |
| `prf` | Object | 父组件实例（可选） |

### `getAllFormValues(isAll)`
获取布局内所有表单组件的值。

| 参数 | 类型 | 描述 |
|------|------|------|
| `isAll` | Boolean | 是否获取所有值（包括空值） |

**返回值**: `Object` - 表单值对象

### `getPanel(subId)`
根据子ID获取对应的面板节点。

| 参数 | 类型 | 描述 |
|------|------|------|
| `subId` | String | 子组件ID |

**返回值**: `Object` - 面板节点

### `append(target, subId, pre, base)`
向指定面板追加内容。

| 参数 | 类型 | 描述 |
|------|------|------|
| `target` | Object | 要追加的内容 |
| `subId` | String | 目标面板ID，默认 `'main'` |
| `pre` | Boolean | 是否插入到前面 |
| `base` | Any | 基准项 |

**返回值**: `this`（支持链式调用）

### `insertItems(arr, base, before, all)`
插入新的面板项。

| 参数 | 类型 | 描述 |
|------|------|------|
| `arr` | Array | 要插入的面板项数组 |
| `base` | String | 基准面板ID |
| `before` | Boolean | 是否插入到基准前面 |
| `all` | Boolean | 是否批量插入 |

**返回值**: `this`（支持链式调用）

### `_insertItems(arr, base, before, all)`
内部方法，实际执行面板项插入。

### `_afterRemoveItems(profile)`
面板项移除后的回调方法。

### `updateItem(subId, options)`
更新指定面板项的属性。

| 参数 | 类型 | 描述 |
|------|------|------|
| `subId` | String | 面板项ID |
| `options` | Object | 要更新的属性对象 |

**支持的属性**:
- `id`: 面板项ID（如果修改，会自动更新映射）
- `size`: 面板大小（自动调整布局）
- `hidden`: 是否隐藏面板
- `locked`: 是否锁定面板（禁用调整大小）
- `folded`: 是否折叠面板
- `cmd`: 是否显示命令按钮
- `panelBgClr`: 面板背景颜色
- `panelBgImg`: 面板背景图片
- `panelBgImgPos`: 背景图片位置
- `panelBgImgRepeat`: 背景图片重复方式
- `panelBgImgAttachment`: 背景图片附着方式
- `overflow`: 溢出处理方式

**返回值**: `this`（支持链式调用）

### `fireCmdClickEvent(subId)`
触发指定面板的命令按钮点击事件（用于折叠/展开）。

| 参数 | 类型 | 描述 |
|------|------|------|
| `subId` | String | 面板项ID |

**返回值**: `this`（支持链式调用）

### `setTheme(theme)`
设置主题样式。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'` 或 `'dark'` |

**返回值**: `this`（支持链式调用）

### `getTheme()`
获取当前主题。

**返回值**: `String` - 当前主题（`'light'` 或 `'dark'`）

### `toggleDarkMode()`
切换暗黑模式（在 `light` 和 `dark` 之间切换）。

### `adjustLayout()`
响应式布局调整。根据屏幕宽度自动调整样式，添加/移除移动端类名。

**返回值**: `this`（支持链式调用）

### `enhanceAccessibility()`
增强可访问性支持。为容器、面板、移动手柄和命令按钮添加ARIA属性。

**返回值**: `this`（支持链式调用）

### `LayoutTrigger()`
初始化现代化功能：主题、响应式设计、可访问性增强。在组件渲染后自动调用。

## 静态方法

### `_adjustItems2(items, pos)`
调整面板项数组，为每个项设置默认值和位置。

| 参数 | 类型 | 描述 |
|------|------|------|
| `items` | Array | 原始面板项数组 |
| `pos` | String | 位置：`'before'` 或 `'after'` |

**返回值**: `Array` - 调整后的面板项数组

### `_adjustItems(items)`
调整整个面板项数组，分离主面板和前/后面板。

| 参数 | 类型 | 描述 |
|------|------|------|
| `items` | Array | 原始面板项数组 |

**返回值**: `Array` - 调整后的完整面板项数组

### `_prepareData(profile)`
预处理模板数据，处理布局类型、面板项等。

### `_prepareItems(profile, items)`
准备面板项数据，删除不必要的属性。

### `_prepareItem(profile, data, item)`
准备单个面板项数据，设置CSS类名、样式等。

### `RenderTrigger()`
渲染触发器，负责初始化折叠状态。

### `_onresize(profile, width, height)`
布局大小改变时的处理函数，重新计算各面板尺寸。

### `EventHandlers`
事件处理器定义：
- `onClickPanel`: 点击面板时触发

### `Behaviors`
行为定义：
- `HoverEffected`: 悬停效果

### `Appearances`
样式定义：
- `VALIGN`: 垂直对齐样式
- 其他布局相关样式类

## 事件

### `onClickPanel`
点击布局面板时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `item` | Object | 被点击的面板项 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

## 使用示例

### 基本水平布局
```javascript
var layout = ood.UI.Layout({
    type: 'horizontal',
    width: '100%',
    height: '400px',
    items: [
        {id: 'sidebar', pos: 'before', size: 200, min: 150, cmd: true},
        {id: 'content', pos: 'main', overflow: 'auto'},
        {id: 'toolbar', pos: 'after', size: 80, locked: true}
    ]
}).appendTo('#app');
```

### 垂直布局（侧边栏在顶部/底部）
```javascript
var layout = ood.UI.Layout({
    type: 'vertical',
    width: '300px',
    height: '600px',
    items: [
        {id: 'header', pos: 'before', size: 60, locked: true},
        {id: 'main', pos: 'main', overflow: 'auto'},
        {id: 'footer', pos: 'after', size: 40, locked: true}
    ]
});
```

### 动态添加面板
```javascript
// 在 'sidebar' 面板后插入新面板
layout.insertItems([
    {id: 'newPanel', pos: 'after', size: 150, min: 100, cmd: true}
], 'sidebar', false);
```

### 更新面板属性
```javascript
// 隐藏面板
layout.updateItem('sidebar', {hidden: true});

// 锁定面板（禁止调整大小）
layout.updateItem('toolbar', {locked: true});

// 更改面板大小
layout.updateItem('content', {size: 500});
```

### 主题切换
```javascript
// 切换到暗黑模式
layout.setTheme('dark');

// 切换主题
layout.toggleDarkMode();
```

## 现代化功能

### 主题系统
- 支持 `light` 和 `dark` 两种主题
- 主题设置自动保存到 `localStorage`
- 可通过 `setTheme()`、`getTheme()`、`toggleDarkMode()` 方法管理

### 响应式设计
当 `responsive` 属性为 `true` 时，组件会根据屏幕宽度自动调整：
- 屏幕宽度 `< 768px`：添加 `layout-mobile` 类，调整命令按钮字体大小
- 屏幕宽度 `< 480px`：添加 `layout-tiny` 类，隐藏命令按钮

### 可访问性增强
组件自动添加ARIA属性：
- 容器：`role="main"`, `aria-label="布局容器"`
- 面板：`role="region"`, `aria-label="布局面板 {id}"`
- 移动手柄：`role="separator"`, `aria-label="调整 {id} 大小"`, `tabindex="0"`
- 命令按钮：`role="button"`, `aria-label="折叠/展开 {id}"`, `tabindex="0"`

## 布局算法

### 面板大小计算
布局组件使用智能算法计算各面板实际大小：
1. 优先保证主面板 (`main`) 的最小尺寸
2. 根据 `flexSize` 属性决定是否按比例分配剩余空间
3. 考虑面板的 `hidden`、`folded`、`locked` 状态
4. 确保各面板不超出最小/最大限制

### 方向支持
- **水平布局** (`type: 'horizontal'`)：面板从左到右排列，移动手柄垂直方向
- **垂直布局** (`type: 'vertical'`)：面板从上到下排列，移动手柄水平方向

## 注意事项

1. **主面板必需**：布局必须包含一个 `id` 为 `'main'` 的面板项
2. **尺寸单位**：建议使用 `em` 或 `px` 作为尺寸单位，确保在不同字体大小下表现一致
3. **响应式监听**：窗口大小变化的监听需要在应用层面处理，OOD框架有自己的事件处理机制
4. **性能优化**：避免频繁调用 `updateItem()` 和 `adjustLayout()`，可批量更新后调用一次