# MFormLayout

移动表单布局组件，专为移动设备优化的表单布局解决方案，提供类似电子表格的网格布局，支持设计模式、写入模式和只读模式。内置现代化功能如主题切换、响应式设计和可访问性增强，适用于移动端复杂表单设计、数据录入和报表展示场景。

## 类名
`ood.UI.MFormLayout`

## 继承
`ood.UI`, `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MFormLayout.js"></script>

<!-- 创建移动表单布局 -->
<div id="mformlayout-container"></div>

<script>
var mFormLayout = ood.UI.MFormLayout({
    name: "MobileForm",
    width: "100%",
    height: "40em",
    theme: "light",
    responsive: true,
    mode: "write",
    defaultRowHeight: 45,
    defaultColWidth: 100,
    layoutData: {
        rows: 4,
        cols: 2,
        cells: {
            A1: {value: "移动端用户信息", style: {textAlign: "center", fontSize: "20px", fontWeight: "bold"}},
            A2: {value: "姓名"},
            B2: {value: ""},
            A3: {value: "手机号"},
            B3: {value: ""},
            A4: {value: "备注", rowspan: 1, colspan: 2}
        }
    }
}).appendTo('#mformlayout-container');

// 切换模式
mFormLayout.properties.mode = "read";
mFormLayout.refresh();
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `name` | String | `"TestForm"` | 表单名称 |
| `showEffects` | String | `"Classic"` | 显示效果：'Classic' |
| `width` | String | `"50em"` | 组件宽度 |
| `height` | String | `"20em"` | 组件高度 |
| `visibility` | String | `"visible"` | 可见性 |
| `floatHandler` | Boolean | `false` | 是否启用浮动处理 |
| `defaultRowHeight` | Number | `35` | 默认行高（像素） |
| `defaultColWidth` | Number | `150` | 默认列宽（像素） |
| `layoutData` | Object | 预定义3行2列表格 | 布局数据，包含行数、列数、单元格内容、合并设置等 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `tabindex` | Number | `null` | Tab键顺序 |
| `defaultFocus` | Boolean | `null` | 默认获取焦点 |
| `disableClickEffect` | Boolean | `null` | 禁用点击效果 |
| `disableHoverEffect` | Boolean | `null` | 禁用悬停效果 |
| `disableTips` | Boolean | `null` | 禁用提示 |
| `disabled` | Boolean | `null` | 禁用状态 |
| `renderer` | String | `null` | 渲染器类型 |
| `selectable` | Boolean | `null` | 是否可选择 |
| `tips` | String | `null` | 提示文本 |
| `autoTips` | Boolean | `null` | 自动提示 |
| `overflow` | String | `null` | 溢出处理 |
| `items` | Object | `{hidden: true}` | 项目列表（隐藏） |
| `listKey` | String | `null` | 列表键 |
| `dragSortable` | Boolean | `null` | 是否支持拖拽排序 |
| `mode` | String | `'write'` | 模式：'design'（设计）、'write'（写入）、'read'（只读） |
| `lineSpacing` | Number | `0` | 行间距（像素） |
| `width` | String | `'30em'` | 组件宽度（带空间单位） |
| `height` | String | `'25em'` | 组件高度（带空间单位） |
| `solidGridlines` | Boolean | `true` | 是否显示实线网格 |
| `stretchHeight` | String | `'none'` | 高度拉伸方式：'none'（不拉伸）、'last'（最后一行拉伸）、'all'（所有行拉伸） |
| `stretchH` | String | `'all'` | 水平拉伸方式：'all'（拉伸所有）、'none'（不拉伸）、'last'（最后一列拉伸） |
| `rowHeaderWidth` | Number | `25` | 行标题宽度（像素） |
| `columnHeaderHeight` | Number | `25` | 列标题高度（像素） |
| `floatHandler` | Boolean | `true` | 是否启用浮动处理器 |
| `defaultRowSize` | Number | `5` | 默认行数 |
| `defaultColumnSize` | Number | `5` | 默认列数 |
| `defaultRowHeight` | Number | `50` | 默认行高（像素） |
| `defaultColWidth` | Number | `120` | 默认列宽（像素） |
| `layoutData` | Object | `{}` | 布局数据，包含表格结构、单元格内容和样式 |
| `rendererCDNJS` | String | `'/plugins/formlayout/handsontable.full.min.js'` | 渲染器JavaScript库（Handsontable） |
| `rendererCDNCSS` | String | `'/plugins/formlayout/handsontable.full.min.css'` | 渲染器CSS样式（Handsontable） |

## 方法

### `_isDesignMode()`
检查当前是否处于设计模式。

**返回：**
- (Boolean): 如果当前模式为'design'则返回true。

### `getContainer(subId)`
获取指定子ID的容器节点。

**参数：**
- `subId` (String): 子项目ID

**返回：**
- (Object): DOM节点或组件实例。

### `refresh()`
刷新组件布局，重新渲染表格。

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateCategories(data, index)`
更新分类数据。

**参数：**
- `data` (Array|Object): 分类数据
- `index` (Number, 可选): 数据索引

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateLine(data, index)`
更新线条数据。

**参数：**
- `data` (Array|Object): 线条数据
- `index` (Number, 可选): 数据索引

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateData(data, index)`
更新图表数据。

**参数：**
- `data` (Array|Object): 图表数据
- `index` (Number, 可选): 数据索引

**返回：**
- (Object): 组件实例，支持链式调用。

### `fillData(data, index, isLineset)`
填充数据到图表。

**参数：**
- `data` (Array|Object): 要填充的数据
- `index` (Number, 可选): 数据索引
- `isLineset` (Boolean, 可选): 是否为线条数据集

**返回：**
- (Object): 组件实例，支持链式调用。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 主容器样式 |
| `BOX` | 内容区域样式 |
| `CBORDER` | 单元格边框样式 |
| `POOL` | 项目池样式（隐藏区域） |
| `HOLDER` | 滚动容器样式 |
| `TABLE` | 表格样式 |
| `ITEM` | 单元格项样式 |
| `ITEM.layoutcell` | 布局单元格样式 |
| `ITEM.layoutcell.firstrow` | 首行单元格样式 |
| `ITEM.layoutcell.firstcol` | 首列单元格样式 |

## 示例

### 移动端数据录入表单

```html
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/MFormLayout.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <style>
        body { padding: 10px; background-color: #f5f5f5; }
        .mobile-form-container { 
            border-radius: 12px; 
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            overflow: hidden;
            margin: 0 auto;
            max-width: 600px;
        }
    </style>
</head>
<body>
    <div class="mobile-form-container" id="data-entry-form"></div>
    
    <script>
    var mobileForm = ood.UI.MFormLayout({
        name: "DataEntry",
        width: "100%",
        height: "500px",
        theme: "light",
        responsive: true,
        mode: "write",
        defaultRowHeight: 55,
        defaultColWidth: 120,
        solidGridlines: true,
        stretchHeight: "last",
        layoutData: {
            rows: 6,
            cols: 2,
            merged: [
                {row: 0, col: 0, rowspan: 1, colspan: 2}
            ],
            rowSetting: {
                "1": {manualHeight: 65},
                "2": {manualHeight: 55},
                "3": {manualHeight: 55},
                "4": {manualHeight: 55},
                "5": {manualHeight: 55},
                "6": {manualHeight: 100}
            },
            colSetting: {
                "A": {manualWidth: 120},
                "B": {manualWidth: 300}
            },
            cells: {
                A1: {value: "移动端数据录入", style: {textAlign: "center", fontSize: "22px", fontWeight: "600", color: "#333", backgroundColor: "#e8f4ff"}},
                A2: {value: "产品名称", style: {textAlign: "right", paddingRight: "15px", fontWeight: "500"}},
                B2: {value: "", style: {paddingLeft: "10px"}},
                A3: {value: "产品编号", style: {textAlign: "right", paddingRight: "15px", fontWeight: "500"}},
                B3: {value: "", style: {paddingLeft: "10px"}},
                A4: {value: "数量", style: {textAlign: "right", paddingRight: "15px", fontWeight: "500"}},
                B4: {value: "1", style: {paddingLeft: "10px"}},
                A5: {value: "单价", style: {textAlign: "right", paddingRight: "15px", fontWeight: "500"}},
                B5: {value: "0.00", style: {paddingLeft: "10px", color: "#e44a00"}},
                A6: {value: "备注说明", style: {textAlign: "right", paddingRight: "15px", fontWeight: "500", verticalAlign: "top", paddingTop: "15px"}},
                B6: {value: "请在此输入产品的详细描述和特殊要求...", style: {padding: "10px", fontSize: "14px", color: "#666", minHeight: "80px"}}
            }
        }
    }).appendTo('#data-entry-form');
    
    // 响应式调整
    window.addEventListener('resize', function() {
        mobileForm.refresh();
    });
    </script>
</body>
</html>
```

### 只读模式报表展示

```html
<div id="report-viewer"></div>

<script>
var reportViewer = ood.UI.MFormLayout({
    name: "SalesReport",
    width: "100%",
    height: "400px",
    theme: "light",
    responsive: true,
    mode: "read",
    defaultRowHeight: 40,
    defaultColWidth: 100,
    solidGridlines: false,
    layoutData: {
        rows: 5,
        cols: 4,
        cells: {
            A1: {value: "季度", style: {textAlign: "center", fontWeight: "bold", backgroundColor: "#f0f0f0"}},
            B1: {value: "Q1", style: {textAlign: "center", fontWeight: "bold", backgroundColor: "#f0f0f0"}},
            C1: {value: "Q2", style: {textAlign: "center", fontWeight: "bold", backgroundColor: "#f0f0f0"}},
            D1: {value: "Q3", style: {textAlign: "center", fontWeight: "bold", backgroundColor: "#f0f0f0"}},
            A2: {value: "销售额", style: {textAlign: "right", paddingRight: "10px"}},
            B2: {value: "¥1,280K", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            C2: {value: "¥1,560K", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            D2: {value: "¥1,890K", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            A3: {value: "增长率", style: {textAlign: "right", paddingRight: "10px"}},
            B3: {value: "+8.5%", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            C3: {value: "+12.3%", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            D3: {value: "+15.7%", style: {textAlign: "right", paddingRight: "10px", color: "#2e7d32"}},
            A4: {value: "市场份额", style: {textAlign: "right", paddingRight: "10px"}},
            B4: {value: "18.2%", style: {textAlign: "right", paddingRight: "10px", color: "#1565c0"}},
            C4: {value: "19.5%", style: {textAlign: "right", paddingRight: "10px", color: "#1565c0"}},
            D4: {value: "21.3%", style: {textAlign: "right", paddingRight: "10px", color: "#1565c0"}},
            A5: {value: "客户数", style: {textAlign: "right", paddingRight: "10px"}},
            B5: {value: "2,450", style: {textAlign: "right", paddingRight: "10px", color: "#6a1b9a"}},
            C5: {value: "2,780", style: {textAlign: "right", paddingRight: "10px", color: "#6a1b9a"}},
            D5: {value: "3,120", style: {textAlign: "right", paddingRight: "10px", color: "#6a1b9a"}}
        }
    }
}).appendTo('#report-viewer');

// 添加响应式支持
reportViewer.properties.responsive = true;
reportViewer.refresh();
</script>
```

### 设计模式表单编辑器

```html
<div class="design-editor">
    <div class="toolbar">
        <button onclick="switchMode('design')">设计模式</button>
        <button onclick="switchMode('write')">预览模式</button>
        <button onclick="switchMode('read')">只读模式</button>
    </div>
    <div id="form-designer"></div>
</div>

<script>
var formDesigner = ood.UI.MFormLayout({
    name: "FormDesigner",
    width: "100%",
    height: "600px",
    theme: "light",
    responsive: true,
    mode: "design",
    defaultRowHeight: 50,
    defaultColWidth: 150,
    solidGridlines: true,
    floatHandler: true,
    layoutData: {
        rows: 8,
        cols: 4,
        cells: {
            A1: {value: "表单设计器", style: {textAlign: "center", fontSize: "24px", fontWeight: "bold", backgroundColor: "#e3f2fd", color: "#1565c0"}},
            A2: {value: "字段类型", style: {textAlign: "center", fontWeight: "500"}},
            B2: {value: "文本框", style: {textAlign: "center"}},
            C2: {value: "下拉框", style: {textAlign: "center"}},
            D2: {value: "日期选择", style: {textAlign: "center"}},
            A3: {value: "示例字段1", style: {textAlign: "right", paddingRight: "10px"}},
            B3: {value: "输入文本...", style: {paddingLeft: "8px", color: "#999"}},
            C3: {value: "选择选项", style: {paddingLeft: "8px", color: "#999"}},
            D3: {value: "选择日期", style: {paddingLeft: "8px", color: "#999"}},
            A4: {value: "示例字段2", style: {textAlign: "right", paddingRight: "10px"}},
            B4: {value: "", style: {paddingLeft: "8px"}},
            C4: {value: "", style: {paddingLeft: "8px"}},
            D4: {value: "", style: {paddingLeft: "8px"}},
            A5: {value: "必填字段", style: {textAlign: "right", paddingRight: "10px", color: "#d32f2f"}},
            B5: {value: "*必填", style: {paddingLeft: "8px", color: "#d32f2f"}},
            C5: {value: "*必填", style: {paddingLeft: "8px", color: "#d32f2f"}},
            D5: {value: "*必填", style: {paddingLeft: "8px", color: "#d32f2f"}},
            A6: {value: "说明区域", style: {textAlign: "right", paddingRight: "10px", verticalAlign: "top", paddingTop: "15px"}},
            B6: {value: "这是一个示例表单设计，您可以在设计模式下拖拽调整单元格大小和位置。", style: {padding: "10px", fontSize: "14px", color: "#666", colspan: 3}}
        }
    }
}).appendTo('#form-designer');

function switchMode(newMode) {
    formDesigner.properties.mode = newMode;
    formDesigner.refresh();
}
</script>
```

## 注意事项

1. **移动端优化**：组件专门针对移动设备进行优化，支持触摸操作和响应式布局。

2. **三种模式**：
   - **设计模式**：允许拖拽调整单元格大小、位置，编辑表单结构
   - **写入模式**：用户可输入数据的表单模式
   - **只读模式**：仅用于展示数据的报表模式

3. **Handsontable渲染器**：组件使用Handsontable作为底层渲染引擎，需确保相关资源正确加载。

4. **响应式设计**：组件自动适应不同屏幕尺寸，在移动端和小屏幕设备上有更好的表现。

5. **性能考虑**：大量单元格（超过1000个）可能会影响性能，建议合理设计表单规模。

6. **浏览器兼容**：支持现代浏览器和移动端浏览器，部分高级功能在老版本浏览器中可能受限。

7. **数据绑定**：支持动态数据绑定，可通过JavaScript API实时更新表单数据。

8. **可访问性**：组件内置ARIA属性支持，提升屏幕阅读器兼容性。

9. **主题定制**：支持主题切换，可适配不同视觉风格的应用场景。

10. **单元格合并**：支持跨行跨列的单元格合并，满足复杂表单布局需求。