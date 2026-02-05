# FusionChartsXT

FusionChartsXT图表组件，封装FusionChartsXT图表库（www.FusionCharts.com），提供丰富的图表类型和数据可视化功能，支持JSON/XML数据格式、动态更新和交互事件。

**注意**：此组件是FusionChartsXT的EUSUI封装，**不是**EUSUI产品的一部分。如果在商业项目中使用此组件，请单独购买FusionChartsXT许可证。

## 类名
`ood.UI.FusionChartsXT`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/FusionChartsXT.js"></script>

<!-- 创建图表容器 -->
<div id="chart-container"></div>

<script>
var chart = ood.UI.FusionChartsXT({
    width: '30em',
    height: '25em',
    chartType: 'Column2D',
    JSONData: {
        "chart": {
            "caption": "月度销售报告",
            "subcaption": "2023年度",
            "xaxisname": "月份",
            "yaxisname": "销售额（万元）",
            "numberprefix": "¥",
            "theme": "fusion"
        },
        "data": [
            {"label": "1月", "value": "420"},
            {"label": "2月", "value": "380"},
            {"label": "3月", "value": "510"},
            {"label": "4月", "value": "480"},
            {"label": "5月", "value": "550"},
            {"label": "6月", "value": "610"}
        ]
    }
}).appendTo('#chart-container');

// 动态更新数据
chart.properties.plotData = [
    {"label": "1月", "value": "450"},
    {"label": "2月", "value": "410"},
    {"label": "3月", "value": "530"}
];
chart.refreshChart();
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `tabindex` | Number | `null` | Tab键顺序 |
| `expression` | String | `''` | 表达式 |
| `defaultFocus` | Boolean | `null` | 默认获取焦点 |
| `disableClickEffect` | Boolean | `null` | 禁用点击效果 |
| `disableHoverEffect` | Boolean | `null` | 禁用悬停效果 |
| `disableTips` | Boolean | `null` | 禁用提示 |
| `disabled` | Boolean | `null` | 禁用状态 |
| `renderer` | String | `null` | 渲染器类型 |
| `selectable` | Boolean | `null` | 是否可选择 |
| `tips` | String | `null` | 提示文本 |
| `width` | String | `'30em'` | 图表宽度（支持em、px单位） |
| `height` | String | `'25em'` | 图表高度（支持em、px单位） |
| `chartCDN` | String | `'/plugins/fusioncharts/fusioncharts.js'` | FusionCharts库CDN地址 |
| `chartType` | String | `'Column2D'` | 图表类型，支持以下选项： |
| `JSONData` | Object | `{}` | JSON格式的图表数据 |
| `XMLUrl` | String | `''` | XML数据文件的URL地址 |
| `XMLData` | String | `''` | XML格式的图表数据 |
| `JSONUrl` | String | `''` | JSON数据文件的URL地址 |
| `plotData` | Object | `{}` | 绘图数据，用于动态更新图表数据 |
| `feedData` | String | `''` | 实时数据馈送 |

### 图表类型 (chartType)

组件支持丰富的图表类型，包括：

#### 单系列图表
- `Column2D`, `Column3D` - 柱状图
- `Line` - 折线图
- `Area2D` - 面积图
- `Bar2D`, `Bar3D` - 条形图
- `Pie2D`, `Pie3D` - 饼图
- `Doughnut2D`, `Doughnut3D` - 环图
- `Pareto2D`, `Pareto3D` - 帕累托图

#### 多系列图表
- `MSColumn2D`, `MSColumn3D` - 多系列柱状图
- `MSLine` - 多系列折线图
- `MSBar2D`, `MSBar3D` - 多系列条形图
- `MSArea` - 多系列面积图
- `Marimekko` - 马赛克图
- `ZoomLine` - 缩放折线图

#### 堆叠图表
- `StackedColumn3D`, `StackedColumn2D` - 堆叠柱状图
- `StackedBar2D`, `StackedBar3D` - 堆叠条形图
- `StackedArea2D` - 堆叠面积图
- `MSStackedColumn2D` - 多系列堆叠柱状图

#### 组合图表
- `MSCombi3D`, `MSCombi2D` - 多系列组合图
- `MSColumnLine3D` - 柱线组合图
- `StackedColumn2DLine`, `StackedColumn3DLine` - 堆叠柱线图
- `MSCombiDY2D`, `MSColumn3DLineDY`, `StackedColumn3DLineDY`, `MSStackedColumn2DLineDY` - 双Y轴组合图

#### XY散点图
- `Scatter` - 散点图
- `Bubble` - 气泡图

#### 滚动图表
- `ScrollColumn2D`, `ScrollLine2D`, `ScrollArea2D` - 滚动图表
- `ScrollStackedColumn2D`, `ScrollCombi2D`, `ScrollCombiDY2D` - 滚动堆叠/组合图

#### 漏斗图
- `Funnel` - 漏斗图

#### 实时图表
- `RealTimeLine`, `RealTimeArea`, `RealTimeColumn` - 实时图表
- `RealTimeLineDY`, `RealTimeStackedArea`, `RealTimeStackedColumn` - 实时双Y轴/堆叠图

#### 仪表盘
- `HLinearGauge`, `Cylinder`, `HLED`, `VLED` - 线性仪表
- `Thermometer` - 温度计
- `AngularGauge` - 角度仪表

#### 其他图表
- `Pyramid` - 金字塔图
- `Radar` - 雷达图

## 方法

### `refreshChart(dataFormat)`
刷新图表，重新渲染当前数据。

**参数：**
- `dataFormat` (String, 可选): 数据格式，可选值：'XMLUrl', 'JSONUrl', 'XMLData', 'JSONData'。如果未指定，自动检测数据源。

**返回：**
- (Object): 组件实例，支持链式调用。

### `setTransparent(isTransparent)`
设置图表背景透明。

**参数：**
- `isTransparent` (Boolean): 是否透明

**返回：**
- (Object): 组件实例，支持链式调用。

### `getChartAttribute(key)`
获取图表属性。

**参数：**
- `key` (String, 可选): 属性键名。如果未指定，返回所有属性。

**返回：**
- (any): 属性值或属性对象。

### `setChartAttribute(key, value)`
设置图表属性。

**参数：**
- `key` (String|Object): 属性键名或属性对象
- `value` (any, 可选): 属性值（当key为String时）

**返回：**
- (Object): 组件实例，支持链式调用。

### `getFCObject()`
获取底层的FusionCharts实例。

**返回：**
- (Object): FusionCharts实例，可用于直接调用FusionCharts API。

### `getSVGString()`
获取图表的SVG字符串表示。

**返回：**
- (String): SVG字符串。

## 事件

### `onDataClick(profile, argsMap, sourceData)`
数据点点击事件。

**参数：**
- `profile`: 组件实例
- `argsMap`: 事件参数映射
- `sourceData`: 原始数据对象

### `onLabelClick(profile, argsMap)`
数据标签点击事件。

### `onAnnotationClick(profile, argsMap)`
图表注解点击事件。

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 图表容器样式 |
| `BOX` | 图表区域样式 |

## 示例

### 基本柱状图

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/FusionChartsXT.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="sales-chart"></div>
    
    <script>
    var salesChart = ood.UI.FusionChartsXT({
        width: '600px',
        height: '400px',
        chartType: 'Column2D',
        JSONData: {
            "chart": {
                "caption": "年度销售业绩",
                "subcaption": "2023年各部门",
                "xaxisname": "部门",
                "yaxisname": "销售额（万元）",
                "numberprefix": "¥",
                "theme": "fusion",
                "showvalues": "1",
                "plotgradientcolor": ""
            },
            "data": [
                {"label": "销售部", "value": "1280"},
                {"label": "市场部", "value": "980"},
                {"label": "技术部", "value": "1560"},
                {"label": "行政部", "value": "420"},
                {"label": "财务部", "value": "890"}
            ]
        }
    }).appendTo('#sales-chart');
    </script>
</body>
</html>
```

### 动态数据更新

```html
<div id="dynamic-chart"></div>

<script>
var dynamicChart = ood.UI.FusionChartsXT({
    width: '500px',
    height: '300px',
    chartType: 'Line',
    JSONData: {
        "chart": {
            "caption": "实时温度监控",
            "subcaption": "实验室1号",
            "xaxisname": "时间",
            "yaxisname": "温度（℃）",
            "theme": "fusion"
        },
        "data": [
            {"label": "10:00", "value": "22.5"},
            {"label": "11:00", "value": "23.1"},
            {"label": "12:00", "value": "24.3"}
        ]
    }
}).appendTo('#dynamic-chart');

// 动态添加数据点
function addDataPoint(label, value) {
    var data = dynamicChart.properties.JSONData.data;
    data.push({"label": label, "value": value.toString()});
    
    // 保持最多10个数据点
    if (data.length > 10) {
        data.shift();
    }
    
    dynamicChart.refreshChart();
}

// 模拟实时数据
setInterval(function() {
    var now = new Date();
    var label = now.getHours() + ':' + now.getMinutes();
    var value = 20 + Math.random() * 10;
    addDataPoint(label, value.toFixed(1));
}, 5000);
</script>
```

### 多图表仪表板

```html
<div class="dashboard">
    <div class="chart-row">
        <div id="gauge-chart"></div>
        <div id="pie-chart"></div>
    </div>
    <div class="chart-row">
        <div id="bar-chart"></div>
        <div id="line-chart"></div>
    </div>
</div>

<script>
// 仪表盘图表
var gaugeChart = ood.UI.FusionChartsXT({
    width: '300px',
    height: '250px',
    chartType: 'AngularGauge',
    JSONData: {
        "chart": {
            "caption": "服务器负载",
            "lowerlimit": "0",
            "upperlimit": "100",
            "lowerlimitdisplay": "空闲",
            "upperlimitdisplay": "满载",
            "palette": "1",
            "numbersuffix": "%",
            "tickvaluedistance": "10",
            "showvalue": "1",
            "gaugeinnerradius": "0",
            "bgcolor": "FFFFFF",
            "pivotfillcolor": "333333"
        },
        "colorrange": {
            "color": [
                {"minvalue": "0", "maxvalue": "45", "code": "e44a00"},
                {"minvalue": "45", "maxvalue": "75", "code": "f8bd19"},
                {"minvalue": "75", "maxvalue": "100", "code": "6baa01"}
            ]
        },
        "dials": {
            "dial": [{"value": "72"}]
        }
    }
}).appendTo('#gauge-chart');

// 饼图
var pieChart = ood.UI.FusionChartsXT({
    width: '300px',
    height: '250px',
    chartType: 'Pie3D',
    JSONData: {
        "chart": {
            "caption": "市场份额分布",
            "subcaption": "2023年Q4",
            "showpercentvalues": "1",
            "defaultcenterlabel": "市场份额",
            "aligncaptionwithcanvas": "0",
            "captionpadding": "0",
            "decimals": "1",
            "plottooltext": "<b>$percentValue</b> 的份额来自 $label",
            "theme": "fusion"
        },
        "data": [
            {"label": "产品A", "value": "38"},
            {"label": "产品B", "value": "25"},
            {"label": "产品C", "value": "18"},
            {"label": "产品D", "value": "12"},
            {"label": "其他", "value": "7"}
        ]
    }
}).appendTo('#pie-chart');

// 更多图表...
</script>
```

## 注意事项

1. **许可证要求**：FusionChartsXT是商业图表库，在商业项目中使用必须购买许可证。

2. **库依赖**：组件需要FusionCharts库支持，会自动从`chartCDN`地址加载。

3. **数据格式**：支持JSON和XML两种数据格式，可通过`JSONData`、`XMLData`属性直接设置，或通过`JSONUrl`、`XMLUrl`加载远程数据。

4. **动态更新**：通过`plotData`属性可动态更新图表数据，组件会自动处理动画过渡。

5. **实时数据**：`feedData`属性支持实时数据馈送，适用于监控仪表板。

6. **事件交互**：组件提供完整的事件系统，可响应数据点点击、标签点击等交互。

7. **浏览器兼容**：基于FusionCharts库，支持IE7+及所有现代浏览器。

8. **性能优化**：大数据量时建议使用服务器端分页和增量更新。

9. **主题定制**：支持FusionCharts内置主题和自定义主题配置。

10. **响应式设计**：图表容器尺寸变化时，图表会自动调整大小。