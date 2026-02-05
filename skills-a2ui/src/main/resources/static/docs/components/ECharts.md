# ECharts

ECharts图表组件，封装百度ECharts图表库，提供丰富的图表类型和交互功能，支持动态数据绑定、主题切换和响应式设计。

## 类名
`ood.UI.ECharts`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ECharts.js"></script>

<!-- 创建图表容器 -->
<div id="echarts-container"></div>

<script>
var chart = ood.UI.ECharts({
    width: '30em',
    height: '25em',
    theme: 'light',
    responsive: true,
    chartOption: {
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'line'
        }]
    },
    chartRenderer: 'canvas',
    chartTheme: '',
    chartResizeSilent: false
}).appendTo('#echarts-container');
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
| `autoTips` | Boolean | `null` | 自动提示 |
| `width` | String | `'30em'` | 组件宽度（带空间单位） |
| `height` | String | `'25em'` | 组件高度（带空间单位） |
| `chartCDN` | String | `'/plugins/echarts/echarts.min.js'` | ECharts库CDN地址 |
| `chartCDNGL` | String | `'/plugins/echarts/echarts-gl.min.js'` | ECharts GL库CDN地址 |
| `chartTheme` | String | `''` | 图表主题名称 |
| `chartRenderer` | String | `'canvas'` | 渲染器：'', 'canvas', 'svg' |
| `chartDevicePixelRatio` | Number | `window.devicePixelRatio` | 设备像素比 |
| `chartResizeSilent` | Boolean | `false` | 调整大小时是否静默 |
| `chartOption` | Object | `{}` | ECharts配置选项 |
| `optionUpdater` | Object | `{}` | 选项更新器，用于动态更新图表数据 |
| `dataset` | Object | `{}` | 数据集配置 |
| `xAxisDateFormatter` | String | `'hh:mm:ss'` | X轴日期格式化字符串 |
| `realTimePoints` | Number | `5` | 实时数据点数量 |

### 动态数据属性

组件支持动态数据绑定，自动生成以下属性：

| 属性名 | 描述 |
|--------|------|
| `dataValue1` ~ `dataValue9` | 数据值属性，用于绑定图表数据 |
| `realTimeData1` ~ `realTimeData9` | 实时数据属性，用于实时数据更新 |

## 方法

### `getECharts()`
获取底层的ECharts实例。

**返回：**
- (Object): ECharts实例，可用于直接调用ECharts API。

### `optionAdapter(option)`
选项适配器，可在设置选项前修改配置。

**参数：**
- `option` (Object): ECharts配置选项

**返回：**
- (Object): 修改后的选项

### `echarts_call(funName, params)`
调用ECharts实例的方法。

**参数：**
- `funName` (String): 方法名
- `params` (Array): 参数数组

**返回：**
- (any): 方法返回值

### `echarts_dispatchAction(payload)`
派发ECharts动作。

### `echarts_showLoading(type, opts)`
显示加载动画。

### `echarts_hideLoading()`
隐藏加载动画。

### `echarts_getOption()`
获取当前图表选项。

### `echarts_setOption(option)`
设置图表选项。

### `echarts_getDataURL(opts)`
获取图表数据URL。

### `echarts_getConnectedDataURL(opts)`
获取连接图表的数据URL。

### `echarts_appendData(opts)`
追加数据。

### `echarts_clear()`
清除图表。

### `echarts_isDisposed()`
检查图表是否已销毁。

## 事件

### `onMouseEvent(profile, eventType, params)`
鼠标事件处理器，支持以下事件：
- `click`, `dblclick`
- `mousedown`, `mouseup`
- `mouseover`, `mouseout`
- `globalout`, `contextmenu`

### `onChartEvent(profile, eventType, params)`
图表事件处理器，支持ECharts所有内置事件：
- `legendselectchanged`, `legendunselected`
- `datazoom`, `datarangeselected`
- `timelinechanged`, `timelineplaychanged`
- `brush`, `brushselected`
- 等30多种事件

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 图表容器样式 |
| `BOX` | 图表区域样式 |
| `COVER` | 覆盖层样式 |

## 示例

### 基本折线图

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ECharts.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="line-chart"></div>
    
    <script>
    var lineChart = ood.UI.ECharts({
        width: '600px',
        height: '400px',
        chartOption: {
            title: {
                text: '销售趋势'
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                name: '销售额',
                data: [820, 932, 901, 934, 1290, 1330, 1320],
                type: 'line',
                smooth: true
            }]
        }
    }).appendTo('#line-chart');
    </script>
</body>
</html>
```

### 动态数据更新

```html
<div id="dynamic-chart"></div>

<script>
var dynamicChart = ood.UI.ECharts({
    width: '500px',
    height: '300px',
    chartOption: {
        xAxis: {
            type: 'category',
            data: ['A', 'B', 'C', 'D', 'E']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [0, 0, 0, 0, 0],
            type: 'bar'
        }]
    }
}).appendTo('#dynamic-chart');

// 动态更新数据
function updateChartData(values) {
    // 通过optionUpdater更新
    dynamicChart.properties.optionUpdater = {
        series: [{
            data: values
        }]
    };
    dynamicChart.refresh();
}

// 示例：每秒更新数据
setInterval(function() {
    var values = [];
    for (var i = 0; i < 5; i++) {
        values.push(Math.random() * 100);
    }
    updateChartData(values);
}, 1000);
</script>
```

### 多图表组合

```html
<div class="chart-grid">
    <div id="chart1"></div>
    <div id="chart2"></div>
    <div id="chart3"></div>
</div>

<script>
// 创建多个图表实例
var pieChart = ood.UI.ECharts({
    width: '300px',
    height: '300px',
    chartOption: {
        series: [{
            type: 'pie',
            data: [
                {value: 335, name: '直接访问'},
                {value: 310, name: '邮件营销'},
                {value: 234, name: '联盟广告'}
            ]
        }]
    }
}).appendTo('#chart1');

var barChart = ood.UI.ECharts({
    width: '300px',
    height: '300px',
    chartOption: {
        xAxis: {
            type: 'category',
            data: ['产品A', '产品B', '产品C', '产品D']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: [120, 200, 150, 80],
            type: 'bar'
        }]
    }
}).appendTo('#chart2');

var scatterChart = ood.UI.ECharts({
    width: '300px',
    height: '300px',
    chartOption: {
        xAxis: {},
        yAxis: {},
        series: [{
            symbolSize: 20,
            data: [
                [10, 8], [5, 12], [15, 5], [8, 9]
            ],
            type: 'scatter'
        }]
    }
}).appendTo('#chart3');
</script>
```

## 注意事项

1. **ECharts库依赖**：组件需要ECharts库支持，会自动从`chartCDN`地址加载。

2. **动态数据绑定**：通过`dataValue1`~`dataValue9`和`realTimeData1`~`realTimeData9`属性实现动态数据更新。

3. **图表选项**：`chartOption`属性直接对应ECharts配置选项，支持所有ECharts功能。

4. **性能优化**：大数据量时建议使用`dataset`属性和`optionUpdater`进行高效更新。

5. **响应式设计**：组件支持响应式布局，会自动调整图表尺寸。

6. **主题系统**：支持ECharts内置主题和自定义主题。

7. **事件系统**：支持完整的鼠标事件和图表事件，可实现丰富交互。

8. **渲染器选择**：支持canvas和svg两种渲染器，根据需求选择。

9. **浏览器兼容性**：基于ECharts库，要求现代浏览器支持。