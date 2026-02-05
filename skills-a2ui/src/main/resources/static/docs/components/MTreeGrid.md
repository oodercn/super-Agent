# MTreeGrid

MTreeGrid树形表格组件，提供树状结构的数据展示和编辑功能，支持多级折叠/展开、行列操作、数据绑定和Excel公式计算。

## 类名
`ood.UI.MTreeGrid`

## 继承
`ood.UI`, `ood.absValue`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MTreeGrid.js"></script>

<!-- 创建表格容器 -->
<div id="treegrid-container"></div>

<script>
var treegrid = ood.UI.MTreeGrid({
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
                {value: '根节点1'},
                {value: '启用'},
                {value: '2024-01-01'}
            ],
            children: [
                {
                    id: '1-1',
                    cells: [
                        {value: '1-1'},
                        {value: '子节点1-1'},
                        {value: '启用'},
                        {value: '2024-01-02'}
                    ]
                }
            ]
        },
        {
            id: '2',
            cells: [
                {value: '2'},
                {value: '根节点2'},
                {value: '禁用'},
                {value: '2024-01-03'}
            ]
        }
    ],
    selMode: 'single',
    editMode: 'focus',
    treeMode: 'inhandler',
    altRowsBg: true
}).appendTo('#treegrid-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `directInput` | Boolean | `true` | 是否直接输入 |
| `listKey` | String | `null` | 列表键 |
| `currencyTpl` | String | `"$ *"` | 货币模板 |
| `numberTpl` | String | `""` | 数字模板 |
| `valueSeparator` | String | `";"` | 值分隔符 |
| `activeRow` | Object | `{hidden: true, ini: null}` | 活动行 |
| `activeCell` | Object | `{hidden: true, ini: null}` | 活动单元格 |
| `rowMap` | Object | `{hidden: true, ini: null}` | 行映射 |
| `selMode` | String | `'none'` | 选择模式：'single', 'none', 'multi', 'multibycheckbox' |
| `editMode` | String | `'focus'` | 编辑模式：'focus', 'sharp', 'hover', 'hoversharp', 'inline' |
| `dock` | String | `'fill'` | 停靠位置 |
| `togglePlaceholder` | Boolean | `false` | 切换占位符 |
| `isFormField` | Boolean | `false` | 是否表单字段 |
| `altRowsBg` | Boolean | `false` | 交替行背景色 |
| `rowNumbered` | Boolean | `false` | 行编号显示 |
| `editable` | Boolean | `false` | 是否可编辑 |
| `firstCellEditable` | Boolean | `false` | 首单元格是否可编辑 |
| `$subMargin` | String | `'1.375em'` | 子项边距 |
| `initFold` | Boolean | `true` | 初始折叠 |
| `animCollapse` | Boolean | `false` | 动画折叠 |
| `position` | String | `'absolute'` | 定位方式 |
| `width` | String | `'25em'` | 组件宽度（带空间单位） |
| `height` | String | `'18em'` | 组件高度（带空间单位） |
| `_minColW` | String | `'.5em'` | 最小列宽 |
| `_maxColW` | String | `'25em'` | 最大列宽 |
| `_minRowH` | String | `'1.83333em'` | 最小行高 |
| `gridHandlerCaption` | String | `""` | 表格标题 |
| `headerTail` | String | `""` | 表尾内容 |
| `rowHandlerWidth` | String | `'5em'` | 行处理器宽度 |
| `showHeader` | Boolean | `true` | 是否显示表头 |
| `headerHeight` | String | `'2em'` | 表头高度 |
| `rowHeight` | String | `'2em'` | 行高 |
| `_colDfWidth` | String | `'8em'` | 默认列宽 |
| `rowHandler` | Boolean | `true` | 是否显示行处理器 |
| `rowResizer` | Boolean | `false` | 是否允许行调整大小 |
| `colHidable` | Boolean | `false` | 列是否可隐藏 |
| `colResizer` | Boolean | `true` | 是否允许列调整大小 |
| `colSortable` | Boolean | `true` | 列是否可排序 |
| `colMovable` | Boolean | `false` | 列是否可移动 |
| `header` | Array | `[]` | 表头配置数组 |
| `uidColumn` | String | `''` | 唯一标识列 |
| `grpCols` | Array | `[]` | 分组列配置 |
| `rows` | Array | `[]` | 行数据数组 |
| `rawData` | Array | `[]` | 原始数据数组 |
| `dataset` | Object | `{}` | 数据集配置 |
| `tagCmds` | Array | `[]` | 标签命令数组 |
| `activeMode` | String | `'row'` | 活动模式：'row', 'cell', 'none' |
| `rowOptions` | Object | `{}` | 行选项配置 |
| `colOptions` | Object | `{}` | 列选项配置 |
| `treeMode` | String | `'inhandler'` | 树模式：'none', 'inhandler', 'infirstcell' |
| `freezedColumn` | Number | `0` | 冻结列数 |
| `freezedRow` | Number | `0` | 冻结行数 |
| `hotRowMode` | String | `'none'` | 热行模式：'none', 'show', 'hide', 'auto' |
| `excelCellId` | String | `""` | Excel单元格ID |
| `gridValueFormula` | String | `""` | 表格值公式 |
| `hotRowNumber` | String | `'[*]'` | 热行编号 |
| `hotRowCellCap` | String | `'(*)'` | 热行单元格标题 |
| `hotRowRequired` | String | `''` | 热行必填标记 |
| `noCtrlKey` | Boolean | `true` | 禁用Ctrl键 |

### 表头配置 (header)
每个表头项包含以下属性：
- `caption` (String): 列标题文本
- `id` (String): 列唯一标识符
- `width` (String): 列宽度（带单位）
- 其他可选属性：`colRenderer`, `sortable`, `resizable`, `movable`, `hidden`等

### 行数据配置 (rows)
每行数据包含以下属性：
- `id` (String): 行唯一标识符
- `cells` (Array): 单元格数据数组，每个单元格包含`value`属性
- `children` (Array): 子行数据数组（用于树形结构）
- 其他可选属性：`rowRenderer`, `initFold`, `editable`, `selected`等

## 方法

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

**参数：**
- `cellTo` (Object): 目标单元格对象
- `dirtyMark` (Boolean): 是否标记为脏数据
- `triggerEvent` (Boolean): 是否触发事件

**返回：**
- (Object): 组件实例

### `triggerFormulas(cellFrom, dirtyMark, triggerEvent)`
触发所有单元格（或从指定单元格开始）的公式计算。

**参数：**
- `cellFrom` (Object): 起始单元格对象（可选）
- `dirtyMark` (Boolean): 是否标记为脏数据
- `triggerEvent` (Boolean): 是否触发事件

**返回：**
- (Object): 组件实例

### `notifyExcel()`
通知Excel模块表格已修改（用于模块中的模拟Excel）。

### `_setCtrlValue(value)`
设置控件值（内部方法）。

**参数：**
- `value` (any): 要设置的值

**返回：**
- (Object): 组件实例

## 事件

### `onBodyLayout(profile, trigger)`
表格布局事件，在表格布局完成后触发。

### `beforeApplyDataset(profile, dataset)`
应用数据集前事件。

### `onScroll(profile, colId)`
滚动事件。

### `beforeCellKeydown(profile, cell, keys)`
单元格按键前事件。

### `afterCellFocused(profile, cell, row)`
单元格聚焦后事件。

### `beforeInitHotRow(profile)`
初始化热行前事件。

### `onInitHotRow(profile, row)`
初始化热行事件。

### `beforeHotRowAdded(profile, cellMap, row, leaveGrid)`
添加热行前事件。

### `afterHotRowAdded(profile, row)`
添加热行后事件。

### `onGetContent(profile, row, callback)`
获取内容事件。

### `onRowSelected(profile, row, e, src, type)`
行选中事件。

### `onCmd(profile, row, cmdkey, e, src)`
命令事件。

### `beforeFold(profile, item)`
折叠前事件。

### `beforeExpand(profile, item)`
展开前事件。

### `afterFold(profile, item)`
折叠后事件。

### `afterExpand(profile, item)`
展开后事件。

### `beforeColDrag(profile, colId)`
列拖拽前事件。

### `beforeColMoved(profile, colId, toId)`
列移动前事件。

### `afterColMoved(profile, colId, toId)`
列移动后事件。

### `beforeColSorted(profile, col)`
列排序前事件。

### `afterColSorted(profile, col)`
列排序后事件。

### `beforeColShowHide(profile, colId, flag)`
列显示/隐藏前事件。

### `afterColShowHide(profile, colId, flag)`
列显示/隐藏后事件。

### `beforeColResized(profile, colId, width)`
列调整大小前事件。

### `afterColResized(profile, colId, width)`
列调整大小后事件。

### `beforeRowResized(profile, rowId, height)`
行调整大小前事件。

### `afterRowResized(profile, rowId, height)`
行调整大小后事件。

### `swiperight(profile, row, e, src)`
向右滑动事件。

### `swipeup(profile, item, e, src)`
向上滑动事件。

### `swipedown(profile, item, e, src)`
向下滑动事件。

### `beforePrepareRow(profile, row, pid)`
准备行前事件。

### `beforePrepareCol(profile, col)`
准备列前事件。

### `beforeRowActive(profile, row)`
行激活前事件。

### `afterRowActive(profile, row)`
行激活后事件。

### `beforeCellActive(profile, cell)`
单元格激活前事件。

### `afterCellActive(profile, cell)`
单元格激活后事件。

### `beforeCellUpdated(profile, cell, options, isHotRow)`
单元格更新前事件。

### `afterCellUpdated(profile, cell, options, isHotRow)`
单元格更新后事件。

### `beforeRowUpdated(profile, obj, options, isHotRow)`
行更新前事件。

### `afterRowUpdated(profile, obj, options, isHotRow)`
行更新后事件。

### `onRowDirtied(profile, row)`
行标记为脏数据事件。

### `onRowHover(profile, row, hover, e, src)`
行悬停事件。

### `onClickHeader(profile, col, e, src)`
点击表头事件。

### `onClickRow(profile, row, e, src)`
点击行事件。

### `onClickRowHandler(profile, row, e, src)`
点击行处理器事件。

### `onDblclickRow(profile, row, e, src)`
双击行事件。

### `onClickCell(profile, cell, e, src)`
点击单元格事件。

### `onDblclickCell(profile, cell, e, src)`
双击单元格事件。

### `onClickGridHandler(profile, e, src)`
点击表格处理器事件。

### `beforeIniEditor(profile, cell, cellNode, pNode, type)`
初始化编辑器前事件。

### `onBeginEdit(profile, cell, editor, type)`
开始编辑事件。

### `beforeEditApply(profile, cell, options, editor, tag, type)`
应用编辑前事件。

### `onEndEdit(profile, cell, editor, type)`
结束编辑事件。

### `onFileDlgOpen(profile, cell, proEditor, src)`
文件对话框打开事件。

### `beforeComboPop(profile, cell, proEditor, pos, e, src)`
组合框弹出前事件。

### `beforePopShow(profile, cell, proEditor, popCtl, items)`
弹出窗口显示前事件。

### `afterPopShow(profile, cell, proEditor, popCtl)`
弹出窗口显示后事件。

### `onCommand(profile, cell, proEditor, src, type)`
命令执行事件。

### `onEditorClick(profile, cell, proEditor, type, src)`
编辑器点击事件。

### `beforeUnitUpdated(profile, cell, proEditor, type)`
单元更新前事件。

### `beforeApplyFormula(profile, cell, value, formula)`
应用公式前事件。

### `afterApplyFormulas(profile, dataArrs)`
应用公式后事件。

### `beforeGridValueCalculated(profile)`
计算表格值前事件。

### `afterGridValueCalculated(profile, value)`
计算表格值后事件。

### `onGetExcelCellValue(profile, excelCellId, dftValue)`
获取Excel单元格值事件。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 表格容器样式 |
| `BOX` | 表格区域样式 |
| `HEADER1`, `HEADER2` | 表头样式 |
| `CELLS1`, `CELLS2` | 单元格样式 |
| `ROW`, `ROWNUM` | 行和行号样式 |
| `FHCELL`, `HFMARK` | 行处理器样式 |
| `SCROLL` | 滚动区域样式 |

## 示例

### 基本树形表格

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/MTreeGrid.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="basic-treegrid"></div>
    
    <script>
    var treegrid = ood.UI.MTreeGrid({
        width: '800px',
        height: '500px',
        rowHandler: true,
        rowNumbered: true,
        altRowsBg: true,
        header: [
            {caption: '部门', id: 'dept', width: '200px'},
            {caption: '负责人', id: 'manager', width: '150px'},
            {caption: '预算', id: 'budget', width: '120px'},
            {caption: '实际支出', id: 'actual', width: '120px'},
            {caption: '状态', id: 'status', width: '100px'}
        ],
        rows: [
            {
                id: 'dept1',
                cells: [
                    {value: '研发部'},
                    {value: '张三'},
                    {value: '500000'},
                    {value: '450000'},
                    {value: '正常'}
                ],
                children: [
                    {
                        id: 'dept1-1',
                        cells: [
                            {value: '前端组'},
                            {value: '李四'},
                            {value: '200000'},
                            {value: '180000'},
                            {value: '正常'}
                        ]
                    },
                    {
                        id: 'dept1-2',
                        cells: [
                            {value: '后端组'},
                            {value: '王五'},
                            {value: '300000'},
                            {value: '270000'},
                            {value: '正常'}
                        ]
                    }
                ]
            },
            {
                id: 'dept2',
                cells: [
                    {value: '市场部'},
                    {value: '赵六'},
                    {value: '300000'},
                    {value: '320000'},
                    {value: '超支'}
                ]
            }
        ],
        selMode: 'single',
        editMode: 'focus',
        treeMode: 'inhandler'
    }).appendTo('#basic-treegrid');
    </script>
</body>
</html>
```

### 可编辑表格

```html
<div id="editable-treegrid"></div>

<script>
var editableGrid = ood.UI.MTreeGrid({
    width: '600px',
    height: '400px',
    editable: true,
    firstCellEditable: false,
    editMode: 'inline',
    header: [
        {caption: '任务', id: 'task', width: '200px'},
        {caption: '负责人', id: 'assignee', width: '150px'},
        {caption: '截止日期', id: 'dueDate', width: '120px'},
        {caption: '进度', id: 'progress', width: '100px'}
    ],
    rows: [
        {
            id: 'task1',
            cells: [
                {value: '需求分析', type: 'label'},
                {value: '张三', type: 'combobox', items: ['张三', '李四', '王五']},
                {value: '2024-06-01', type: 'date'},
                {value: 80, type: 'progress'}
            ]
        },
        {
            id: 'task2',
            cells: [
                {value: 'UI设计', type: 'label'},
                {value: '李四', type: 'combobox', items: ['张三', '李四', '王五']},
                {value: '2024-06-15', type: 'date'},
                {value: 60, type: 'progress'}
            ]
        }
    ]
}).appendTo('#editable-treegrid');
</script>
```

### 数据绑定示例

```html
<div id="data-bound-treegrid"></div>

<script>
// 创建表格实例
var dataGrid = ood.UI.MTreeGrid({
    width: '700px',
    height: '450px',
    rowHandler: true,
    header: [
        {caption: '产品', id: 'product', width: '180px'},
        {caption: '季度', id: 'quarter', width: '100px'},
        {caption: '销售额', id: 'sales', width: '120px'},
        {caption: '增长率', id: 'growth', width: '100px'}
    ],
    rows: []
}).appendTo('#data-bound-treegrid');

// 动态加载数据
function loadData(data) {
    dataGrid.properties.rows = data;
    dataGrid.refresh();
}

// 示例数据
var sampleData = [
    {
        id: 'q1',
        cells: [
            {value: '第一季度'},
            {value: 'Q1'},
            {value: '1500000'},
            {value: '+15%'}
        ],
        children: [
            {
                id: 'q1-product1',
                cells: [
                    {value: '产品A'},
                    {value: 'Q1'},
                    {value: '800000'},
                    {value: '+20%'}
                ]
            },
            {
                id: 'q1-product2',
                cells: [
                    {value: '产品B'},
                    {value: 'Q1'},
                    {value: '700000'},
                    {value: '+10%'}
                ]
            }
        ]
    },
    {
        id: 'q2',
        cells: [
            {value: '第二季度'},
            {value: 'Q2'},
            {value: '1800000'},
            {value: '+20%'}
        ]
    }
];

// 加载数据
loadData(sampleData);
</script>
```

## 注意事项

1. **树形结构**：通过`children`属性实现多级嵌套，支持折叠/展开功能。

2. **单元格类型**：支持多种单元格类型：`label`, `input`, `textarea`, `combobox`, `listbox`, `file`, `getter`, `helpinput`, `button`, `dropbutton`, `cmdbox`, `popbox`, `date`, `time`, `datetime`, `color`, `spin`, `counter`, `currency`, `number`, `checkbox`, `progress`。

3. **数据绑定**：支持通过`rows`属性直接设置数据，或通过`rawData`、`dataset`属性进行更复杂的数据绑定。

4. **Excel集成**：支持Excel公式计算，通过`gridValueFormula`和`excelCellId`属性实现与Excel模块的集成。

5. **性能优化**：大数据量时建议使用虚拟滚动或分页加载，避免一次性渲染过多行。

6. **编辑功能**：通过`editable`、`editMode`等属性控制编辑行为，支持多种编辑模式。

7. **选择模式**：支持单选、多选、复选框选择等多种选择方式。

8. **响应式设计**：组件支持响应式布局，会自动调整表格尺寸。

9. **事件系统**：提供丰富的事件系统，支持行列操作、编辑状态变更等多种事件。

10. **浏览器兼容性**：基于现代浏览器标准开发，建议使用Chrome、Firefox、Edge等现代浏览器。