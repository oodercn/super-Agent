# TreeGrid

TreeGrid树形表格组件，提供树状结构的数据展示和编辑功能，支持多级折叠/展开、行列操作、数据绑定、主题切换和Excel公式计算。

## 类名
`ood.UI.TreeGrid`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/TreeGrid.js"></script>

<!-- 创建表格容器 -->
<div id="treegrid-container"></div>

<script>
var treegrid = ood.UI.TreeGrid({
    width: '40em',
    height: '30em',
    rowHandler: true,
    rowNumbered: true,
    header: [
        {caption: 'ID', id: 'id', width: '5em'},
        {caption: '名称', id: 'name', width: '12em'},
        {caption: '状态', id: 'status', width: '8em'},
        {caption: '创建时间', id: 'createTime', width: '14em'}
    ],
    rows: [
        {
            id: '1',
            cells: [
                {value: '1'},
                {value: '测试数据1'},
                {value: '启用'},
                {value: '2024-01-01'}
            ]
        },
        {
            id: '2',
            cells: [
                {value: '2'},
                {value: '测试数据2'},
                {value: '禁用'},
                {value: '2024-01-02'}
            ]
        },
        {
            id: '3',
            cells: [
                {value: '3'},
                {value: '测试数据3'},
                {value: '启用'},
                {value: '2024-01-03'}
            ]
        }
    ],
    selMode: 'single',
    editMode: 'focus',
    treeMode: 'inhandler',
    altRowsBg: true,
    theme: 'light'
}).appendTo('#treegrid-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `rowHandler` | Boolean | `true` | 是否显示行处理器 |
| `rowNumbered` | Boolean | `true` | 是否显示行编号 |
| `header` | Array | 预定义的4列 | 表头配置数组 |
| `rows` | Array | 3行示例数据 | 行数据数组 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'light'` | 主题样式 | `'light'`, `'dark'`, `'high-contrast'` |
| `selMode` | String | `'none'` | 选择模式 | `'single'`, `'none'`, `'multi'`, `'multibycheckbox'` |
| `editMode` | String | `'focus'` | 编辑模式 | `'focus'`, `'sharp'`, `'hover'`, `'hoversharp'`, `'inline'` |
| `dock` | String | `'fill'` | 停靠位置 | |
| `togglePlaceholder` | Boolean | `false` | 切换占位符 | |
| `isFormField` | Boolean | `false` | 是否表单字段 | |
| `altRowsBg` | Boolean | `false` | 交替行背景色 | |
| `rowNumbered` | Boolean | `false` | 行编号显示 | |
| `editable` | Boolean | `false` | 是否可编辑 | |
| `firstCellEditable` | Boolean | `false` | 首单元格是否可编辑 | |
| `$subMargin` | String | `'1.375em'` | 子项边距 | |
| `initFold` | Boolean | `true` | 初始折叠 | |
| `animCollapse` | Boolean | `false` | 动画折叠 | |
| `position` | String | `'absolute'` | 定位方式 | |
| `width` | String | `'25em'` | 组件宽度（带空间单位） | |
| `height` | String | `'18em'` | 组件高度（带空间单位） | |
| `_minColW` | String | `'.5em'` | 最小列宽 | |
| `_maxColW` | String | `'25em'` | 最大列宽 | |
| `_minRowH` | String | `'1.83333em'` | 最小行高 | |
| `gridHandlerCaption` | String | `""` | 表格标题 | |
| `headerTail` | String | `""` | 表尾内容 | |
| `rowHandlerWidth` | String | `'5em'` | 行处理器宽度 | |
| `showHeader` | Boolean | `true` | 是否显示表头 | |
| `headerHeight` | String | `'2em'` | 表头高度 | |
| `rowHeight` | String | `'2em'` | 行高 | |
| `_colDfWidth` | String | `'8em'` | 默认列宽 | |
| `rowHandler` | Boolean | `true` | 是否显示行处理器 | |
| `rowResizer` | Boolean | `false` | 是否允许行调整大小 | |
| `colHidable` | Boolean | `false` | 列是否可隐藏 | |
| `colResizer` | Boolean | `true` | 是否允许列调整大小 | |
| `colSortable` | Boolean | `true` | 列是否可排序 | |
| `colMovable` | Boolean | `false` | 列是否可移动 | |
| `uidColumn` | String | `''` | 唯一标识列 | |
| `grpCols` | Array | `[]` | 分组列配置 | |
| `rawData` | Array | `[]` | 原始数据数组 | |
| `dataset` | Object | `{}` | 数据集配置 | |
| `tagCmds` | Array | `[]` | 标签命令数组 | |
| `activeMode` | String | `'row'` | 活动模式 | `'row'`, `'cell'`, `'none'` |
| `rowOptions` | Object | `{}` | 行选项配置 | |
| `colOptions` | Object | `{}` | 列选项配置 | |
| `treeMode` | String | `'inhandler'` | 树模式 | `'none'`, `'inhandler'`, `'infirstcell'` |
| `freezedColumn` | Number | `0` | 冻结列数 | |
| `freezedRow` | Number | `0` | 冻结行数 | |
| `hotRowMode` | String | `'none'` | 热行模式 | `'none'`, `'show'`, `'hide'`, `'auto'` |
| `excelCellId` | String | `""` | Excel单元格ID | |
| `gridValueFormula` | String | `""` | 表格值公式 | |
| `hotRowNumber` | String | `'[*]'` | 热行编号 | |
| `hotRowCellCap` | String | `'(*)'` | 热行单元格标题 | |
| `hotRowRequired` | String | `''` | 热行必填标记 | |
| `noCtrlKey` | Boolean | `true` | 禁用Ctrl键 | |

### 表头配置 (header)

每个表头项包含以下属性：
- `caption` (String): 列标题文本
- `id` (String): 列唯一标识符
- `width` (String): 列宽度（带单位）
- 其他可选属性：`type`, `colRenderer`, `sortable`, `resizable`, `movable`, `hidden`, `flexSize`, `editorCacheKey`等

### 行数据配置 (rows)

每行数据包含以下属性：
- `id` (String): 行唯一标识符
- `cells` (Array): 单元格数据数组，每个单元格包含`value`属性
- `children` (Array): 子行数据数组（用于树形结构）
- 其他可选属性：`rowRenderer`, `initFold`, `editable`, `selected`, `caption`, `_type`等

## 实例方法

### `setTheme(theme)`
设置主题样式。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'`, `'dark'`, `'high-contrast'` |

**返回：**
- (Object): 组件实例

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称

### `toggleTheme()`
切换主题（在light、dark、high-contrast之间循环切换）。

**返回：**
- (Object): 组件实例

### `activate()`
激活表格，使第一个可聚焦单元格获得焦点。

**返回：**
- (Object): 组件实例

### `calculateGridValue()`
计算表格的网格值（支持Excel公式）。

**返回：**
- (any): 计算得到的值

### `getExcelCellValue()`
获取表格的Excel单元格值。

**返回：**
- (any): Excel单元格值，如果没有设置`excelCellId`则返回null

### `applyCellFormula(cellTo, dirtyMark, triggerEvent)`
应用单元格公式计算并更新单元格值。

| 参数 | 类型 | 描述 |
|------|------|------|
| `cellTo` | Object | 目标单元格对象 |
| `dirtyMark` | Boolean | 是否标记为脏数据 |
| `triggerEvent` | Boolean | 是否触发事件 |

**返回：**
- (Object): 组件实例

### `triggerFormulas(cellFrom, dirtyMark, triggerEvent)`
触发所有单元格（或从指定单元格开始）的公式计算。

| 参数 | 类型 | 描述 |
|------|------|------|
| `cellFrom` | Object | 起始单元格对象（可选） |
| `dirtyMark` | Boolean | 是否标记为脏数据 |
| `triggerEvent` | Boolean | 是否触发事件 |

**返回：**
- (Object): 组件实例

### `notifyExcel()`
通知Excel模块表格已修改（用于模块中的模拟Excel）。

### `_setCtrlValue(value)`
设置控件值（内部方法）。

## 事件

TreeGrid支持多种事件，可以通过相应的方法进行绑定：

- `afterUIValueSet`: UI值设置后触发
- `afterRowActive`: 行激活后触发
- `onClickRow`: 点击行时触发
- `onDblclickRow`: 双击行时触发
- `beforeRowActive`: 行激活前触发
- `beforeCellUpdated`: 单元格更新前触发
- `beforeIniEditor`: 初始化编辑器前触发
- `onBeginEdit`: 开始编辑时触发
- `beforeEditApply`: 编辑应用前触发
- `onCmd`: 执行命令时触发

## 使用示例

### 示例1：在模块中使用
```javascript
append(
    ood.create("ood.UI.TreeGrid")
    .setHost(host,"grid")
    .setShowDirtyMark(false)
    .setSelMode("multibycheckbox")
    .setRowHandlerWidth("2.3333333333333335em")
    .setColHidable(true)
    .setColMovable(true)
    .setTreeMode(false)
    .setValue("")
    .afterUIValueSet("_grid_afteruivalueset")
    .afterRowActive("_grid_afterrowactive")
    .onClickRow("_grid_onclickrow")
    .onDblclickRow("_grid_ondblclickrow")
);
```

### 示例2：JSON编辑器
```javascript
append(
    ood.create("ood.UI.TreeGrid")
    .setHost(host,"tg")
    .setTogglePlaceholder(true)
    .setEditable(true)
    .setInitFold(false)
    .setRowHandler(false)
    .setColSortable(false)
    .setHeader([
        {
            "id": "key",
            "width": 100,
            "type": "input",
            "caption": "key",
            "editorCacheKey": "input",
            "colResizer":true,
            "flexSize": true
        },{
            "id": "value",
            "width": 200,
            "type": "textarea",
            "caption": "value",
            "editorCacheKey": "textarea",
            "flexSize": true
        }
    ])
    .setTreeMode("infirstcell")
);
```

## 相关组件

- [MTreeGrid](MTreeGrid.md): 增强版树形表格组件，提供更多高级功能
- [List](List.md): 列表组件，适用于简单列表展示
- [Table](Table.md): 普通表格组件

## 注意事项

1. TreeGrid支持树形结构展示，通过`treeMode`属性控制树形展示方式
2. 支持Excel公式计算，通过`gridValueFormula`属性设置公式
3. 支持主题切换，提供light、dark、high-contrast三种主题
4. 行选择支持单选、多选、复选框多选等多种模式
5. 单元格编辑支持多种模式，包括焦点编辑、悬浮编辑等