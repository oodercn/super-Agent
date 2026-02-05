# FormLayout

表单布局组件，提供类似电子表格的网格布局，支持设计模式、写入模式和只读模式。内置现代化功能如主题切换、响应式设计和可访问性增强，适用于复杂表单设计、数据录入和报表展示场景。

## 类名
`ood.UI.FormLayout`

## 继承
`ood.UI`, `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/FormLayout.js"></script>

<!-- 创建表单布局 -->
<div id="formlayout-container"></div>

<script>
var formLayout = ood.UI.FormLayout({
    name: "MyForm",
    width: "60em",
    height: "30em",
    theme: "light",
    responsive: true,
    mode: "write",
    defaultRowHeight: 50,
    defaultColWidth: 120,
    layoutData: {
        rows: 4,
        cols: 3,
        cells: {
            A1: {value: "用户信息表单", style: {textAlign: "center", fontSize: "24px"}},
            A2: {value: "姓名"},
            B2: {value: ""},
            A3: {value: "邮箱"},
            B3: {value: ""},
            A4: {value: "备注"},
            B4: {value: "", colspan: 2}
        }
    }
}).appendTo('#formlayout-container');
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
| `theme` | Object | `{ini: 'light', listbox: ['light', 'dark']}` | 主题设置 |
| `responsive` | Object | `{ini: true}` | 响应式布局设置 |
| `expression` | Object | `{ini: ''}` | 表达式 |
| `tabindex` | Number | `null` | Tab索引 |
| `defaultFocus` | Object | `null` | 默认焦点 |
| `disableClickEffect` | Boolean | `null` | 禁用点击效果 |
| `disableHoverEffect` | Boolean | `null` | 禁用悬停效果 |
| `disableTips` | Boolean | `null` | 禁用提示 |
| `disabled` | Boolean | `null` | 禁用状态 |
| `renderer` | Object | `null` | 渲染器 |
| `selectable` | Boolean | `null` | 是否可选择 |
| `tips` | String | `null` | 提示文本 |
| `autoTips` | Boolean | `null` | 自动提示 |
| `overflow` | String | `null` | 溢出处理 |
| `items` | Object | `{hidden: true, ini: []}` | 项目列表（隐藏） |
| `listKey` | String | `null` | 列表键 |
| `dragSortable` | Boolean | `null` | 拖拽排序 |
| `mode` | Object | `{ini: 'write', listbox: ['design', 'write', 'read']}` | 模式：设计、写入、只读 |
| `lineSpacing` | Object | `{ini: 10}` | 行间距 |
| `width` | Object | `{$spaceunit: 1, ini: '30em'}` | 组件宽度 |
| `height` | Object | `{$spaceunit: 1, ini: '25em'}` | 组件高度 |
| `solidGridlines` | Object | `{ini: true}` | 实线网格 |
| `stretchHeight` | Object | `{ini: 'none', listbox: ['none', 'last', 'all']}` | 高度拉伸方式 |
| `stretchH` | Object | `{ini: 'all', listbox: ['all', 'none', 'last']}` | 水平拉伸方式 |
| `rowHeaderWidth` | Object | `{ini: 25}` | 行标题宽度 |
| `columnHeaderHeight` | Object | `{ini: 25}` | 列标题高度 |
| `floatHandler` | Object | `{ini: true}` | 浮动处理 |
| `defaultRowSize` | Object | `{ini: 5}` | 默认行数 |
| `defaultColumnSize` | Object | `{ini: 5}` | 默认列数 |
| `defaultRowHeight` | Object | `{ini: 50}` | 默认行高 |
| `defaultColWidth` | Object | `{ini: 120}` | 默认列宽 |
| `layoutData` | Object | `{ini: {}}` | 布局数据 |
| `rendererCDNJS` | Object | `{ini: '/plugins/formlayout/handsontable.full.min.js'}` | 渲染器JS（Handsontable） |
| `rendererCDNCSS` | Object | `{ini: '/plugins/formlayout/handsontable.full.min.css'}` | 渲染器CSS（Handsontable） |

## 方法

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleDarkMode()`
在亮色和暗色主题之间切换。

**返回：**
- (Object): 组件实例，支持链式调用。

### `toggleHighContrast()`
切换高对比度模式。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。针对移动端、平板和桌面设备优化显示。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `FormLayoutTrigger()`
现代化功能初始化触发器，自动设置主题、响应式布局和可访问性。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setChildren(childrens, prf)` (内部方法)
设置子组件。

### `_isDesignMode()` (内部方法)
检查是否处于设计模式。

### `getContainer(subId)` (内部方法)
获取容器节点。

### `_getHeaderOffset(prf)` (内部方法)
获取标题偏移量。

### `_layoutChanged(prf, force)` (内部方法)
布局变化处理。

### `_getLayoutData(prf)` (内部方法)
获取布局数据。

## 事件

### 自定义事件处理器 (EventHandlers)

| 事件名 | 参数 | 描述 |
|--------|------|------|
| `onShowTips` | - | 显示提示时触发 |
| `onGetCellData` | `cellCoord, cellObj, cellChild` | 获取单元格数据时触发 |

### 渲染触发器 (RenderTrigger)
组件渲染时自动调用的初始化函数，负责：
1. 根据模式（design/write/read）选择合适的渲染器
2. 初始化主题设置
3. 设置网格线样式
4. 触发现代化功能初始化

## 示例

### 设计模式表单

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/FormLayout.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="design-form"></div>
    
    <script>
    // 创建设计模式表单
    var designForm = ood.UI.FormLayout({
        mode: "design",
        width: "800px",
        height: "600px",
        theme: "light",
        solidGridlines: true,
        layoutData: {
            rows: 10,
            cols: 6,
            rowSetting: {
                "3": {manualHeight: 80},
                "7": {manualHeight: 120}
            },
            colSetting: {
                "B": {manualWidth: 200},
                "E": {manualWidth: 150}
            },
            cells: {
                A1: {value: "设计表单", style: {textAlign: "center", fontSize: "28px", colspan: 6}}
            }
        }
    }).appendTo('#design-form');
    
    // 切换到暗色主题
    designForm.toggleDarkMode();
    </script>
</body>
</html>
```

### 数据录入表单

```html
<div id="data-entry-form"></div>

<script>
var entryForm = ood.UI.FormLayout({
    mode: "write",
    name: "UserRegistration",
    width: "700px",
    height: "500px",
    theme: "light",
    responsive: true,
    layoutData: {
        rows: 8,
        cols: 4,
        merged: [
            {row: 0, col: 0, rowspan: 1, colspan: 4}
        ],
        cells: {
            A1: {value: "用户注册表单", style: {textAlign: "center", fontSize: "24px", backgroundColor: "#f0f0f0"}},
            A2: {value: "用户名*"},
            B2: {value: "", style: {border: "1px solid #ccc"}},
            A3: {value: "密码*"},
            B3: {value: "", style: {border: "1px solid #ccc", type: "password"}},
            A4: {value: "确认密码*"},
            B4: {value: "", style: {border: "1px solid #ccc", type: "password"}},
            A5: {value: "电子邮箱"},
            B5: {value: "", style: {border: "1px solid #ccc"}},
            A6: {value: "手机号码"},
            B6: {value: "", style: {border: "1px solid #ccc"}},
            A7: {value: "备注"},
            B7: {value: "", style: {border: "1px solid #ccc", rowspan: 2}},
            A8: {value: "提交", style: {textAlign: "center", colspan: 2, backgroundColor: "#007bff", color: "white"}}
        }
    }
}).appendTo('#data-entry-form');
</script>
```

### 响应式报表表单

```html
<div id="report-form"></div>

<script>
var reportForm = ood.UI.FormLayout({
    mode: "read",
    name: "SalesReport",
    width: "100%",
    height: "auto",
    theme: "dark",
    responsive: true,
    stretchHeight: "all",
    layoutData: {
        rows: 12,
        cols: 8,
        rowSetting: {
            "1": {manualHeight: 60},
            "12": {manualHeight: 80}
        },
        colSetting: {
            "A": {manualWidth: 100},
            "H": {manualWidth: 150}
        },
        cells: {
            A1: {value: "销售报表", style: {textAlign: "center", fontSize: "32px", colspan: 8, backgroundColor: "#333", color: "white"}},
            A2: {value: "月份", style: {fontWeight: "bold", backgroundColor: "#555", color: "white"}},
            B2: {value: "产品A", style: {fontWeight: "bold", backgroundColor: "#555", color: "white"}},
            C2: {value: "产品B", style: {fontWeight: "bold", backgroundColor: "#555", color: "white"}},
            D2: {value: "产品C", style: {fontWeight: "bold", backgroundColor: "#555", color: "white"}},
            E2: {value: "总计", style: {fontWeight: "bold", backgroundColor: "#555", color: "white"}},
            // ... 更多单元格数据
            A12: {value: "年度总计", style: {fontWeight: "bold", backgroundColor: "#444", color: "white"}},
            E12: {value: "=SUM(E3:E11)", style: {fontWeight: "bold", fontSize: "18px", backgroundColor: "#222", color: "#4CAF50"}}
        }
    }
}).appendTo('#report-form');

// 启用响应式布局
reportForm.adjustLayout();
// 窗口大小变化时重新调整
window.addEventListener('resize', function() {
    reportForm.adjustLayout();
});
</script>
```

## 注意事项

1. **模式说明**：
   - `design` 模式：可视化设计模式，支持拖拽调整行列尺寸、合并单元格
   - `write` 模式：数据录入模式，用户可在单元格中输入数据
   - `read` 模式：只读模式，适合报表展示，支持公式计算

2. **渲染器选择**：
   - 设计模式默认使用 Handsontable 渲染器（需加载对应JS/CSS）
   - 写入和只读模式使用 HTML5 table 渲染器
   - 可通过 `rendererCDNJS` 和 `rendererCDNCSS` 自定义渲染器路径

3. **布局数据格式**：
   - `layoutData` 使用类似Excel的坐标系统（A1, B2等）
   - 支持单元格合并（`colspan`, `rowspan`）
   - 支持行列自定义尺寸（`rowSetting`, `colSetting`）

4. **响应式设计**：
   - 屏幕宽度 < 768px 时启用移动端样式
   - 屏幕宽度 < 480px 时启用超小屏幕优化
   - 支持通过CSS变量动态调整行高列宽

5. **主题系统**：
   - 支持亮色（light）、暗色（dark）主题
   - 主题设置自动保存到 localStorage
   - 可与OOD框架全局主题系统集成

6. **可访问性**：
   - 自动添加ARIA角色和属性
   - 支持键盘导航（Tab、方向键）
   - 为屏幕阅读器提供语义化标记

7. **性能优化**：
   - 大数据量时建议使用虚拟滚动
   - 设计模式功能较复杂，适合中小型表单
   - 复杂公式计算可能影响性能，建议服务端处理