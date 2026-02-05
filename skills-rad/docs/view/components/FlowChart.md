# FlowChart 组件

## 1. 组件概述
FlowChart 是 ooder-a2ui 框架中的 SVG 流程图组件，用于创建和展示各种流程图、工作流图和关系图。它基于 Raphael.js 图形库，提供了丰富的节点类型和连接线样式，支持拖拽、缩放和交互操作。

## 2. 组件特性
- 支持多种节点类型：矩形、圆形、椭圆、文本、路径等
- 丰富的连接线样式和箭头类型
- 支持节点拖拽、缩放和旋转
- 支持连接线的自动布局和调整
- 支持节点和连接线的属性自定义
- 支持事件处理和回调函数
- 支持流程图的导入和导出

## 3. 组件创建

### 3.1 JSON格式创建
```json
{
  "type": "FlowChart",
  "id": "flowchart1",
  "properties": {
    "width": 800,
    "height": 600,
    "nodes": [
      {
        "id": "node1",
        "type": "rect",
        "x": 100,
        "y": 100,
        "width": 120,
        "height": 60,
        "text": "开始",
        "attr": {
          "fill": "#4CAF50",
          "stroke": "#2E7D32",
          "r": 5
        }
      },
      {
        "id": "node2",
        "type": "rect",
        "x": 300,
        "y": 100,
        "width": 120,
        "height": 60,
        "text": "处理",
        "attr": {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      }
    ],
    "connectors": [
      {
        "id": "conn1",
        "type": "arrow",
        "from": "node1",
        "to": "node2",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "block-wide-long"
        }
      }
    ]
  }
}
```

### 3.2 JavaScript格式创建
```javascript
var flowchart = ood.create({
  type: "FlowChart",
  id: "flowchart1",
  properties: {
    width: 800,
    height: 600,
    nodes: [
      {
        id: "node1",
        type: "rect",
        x: 100,
        y: 100,
        width: 120,
        height: 60,
        text: "开始",
        attr: {
          fill: "#4CAF50",
          stroke: "#2E7D32",
          r: 5
        }
      },
      {
        id: "node2",
        type: "rect",
        x: 300,
        y: 100,
        width: 120,
        height: 60,
        text: "处理",
        attr: {
          fill: "#2196F3",
          stroke: "#1565C0"
        }
      }
    ],
    connectors: [
      {
        id: "conn1",
        type: "arrow",
        from: "node1",
        to: "node2",
        attr: {
          stroke: "#666",
          stroke-width: 2,
          arrow-end: "block-wide-long"
        }
      }
    ]
  }
});
```

## 4. 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| width | Number | 800 | 流程图宽度 |
| height | Number | 600 | 流程图高度 |
| nodes | Array | [] | 节点数组 |
| connectors | Array | [] | 连接线数组 |
| gridSize | Number | 10 | 网格大小 |
| snapToGrid | Boolean | true | 是否吸附到网格 |
| showGrid | Boolean | false | 是否显示网格 |
| zoomable | Boolean | true | 是否可缩放 |
| draggable | Boolean | true | 是否可拖拽 |

### 4.1 节点属性

| 属性名 | 类型 | 说明 |
|--------|------|------|
| id | String | 节点唯一标识 |
| type | String | 节点类型：rect, circle, ellipse, text, path |
| x | Number | 节点x坐标 |
| y | Number | 节点y坐标 |
| width | Number | 节点宽度（矩形） |
| height | Number | 节点高度（矩形） |
| cx | Number | 圆心x坐标（圆形、椭圆） |
| cy | Number | 圆心y坐标（圆形、椭圆） |
| r | Number | 半径（圆形） |
| rx | Number | x轴半径（椭圆） |
| ry | Number | y轴半径（椭圆） |
| text | String | 节点文本内容 |
| attr | Object | Raphael.js属性对象，用于样式定义 |

### 4.2 连接线属性

| 属性名 | 类型 | 说明 |
|--------|------|------|
| id | String | 连接线唯一标识 |
| type | String | 连接线类型：arrow, line, curve |
| from | String | 起始节点id |
| to | String | 目标节点id |
| fromAnchor | String | 起始锚点：left, top, right, bottom |
| toAnchor | String | 目标锚点：left, top, right, bottom |
| attr | Object | Raphael.js属性对象，用于样式定义 |
| path | String | 自定义路径（可选） |

## 5. 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| addNode(node) | node: Object - 节点对象 | String - 节点id | 添加节点 |
| removeNode(nodeId) | nodeId: String - 节点id | Boolean - 是否成功 | 移除节点 |
| getNode(nodeId) | nodeId: String - 节点id | Object - 节点对象 | 获取节点 |
| addConnector(connector) | connector: Object - 连接线对象 | String - 连接线id | 添加连接线 |
| removeConnector(connId) | connId: String - 连接线id | Boolean - 是否成功 | 移除连接线 |
| getConnector(connId) | connId: String - 连接线id | Object - 连接线对象 | 获取连接线 |
| zoom(level) | level: Number - 缩放级别 | void | 缩放流程图 |
| pan(dx, dy) | dx: Number - x方向偏移, dy: Number - y方向偏移 | void | 平移流程图 |
| toSVG() | includeImage: Boolean - 是否包含图片 | String - SVG字符串 | 导出为SVG |
| fromSVG(svgString) | svgString: String - SVG字符串 | void | 从SVG导入 |

## 6. 事件处理

| 事件名 | 触发条件 | 回调参数 |
|--------|----------|----------|
| nodeClick | 点击节点时 | nodeId: String - 节点id, event: Event - 事件对象 |
| nodeDoubleClick | 双击节点时 | nodeId: String - 节点id, event: Event - 事件对象 |
| nodeDragStart | 开始拖拽节点时 | nodeId: String - 节点id, event: Event - 事件对象 |
| nodeDragEnd | 结束拖拽节点时 | nodeId: String - 节点id, event: Event - 事件对象 |
| connectorClick | 点击连接线时 | connId: String - 连接线id, event: Event - 事件对象 |
| connectorDoubleClick | 双击连接线时 | connId: String - 连接线id, event: Event - 事件对象 |
| zoom | 缩放流程图时 | level: Number - 缩放级别 |
| pan | 平移流程图时 | dx: Number - x偏移, dy: Number - y偏移 |

## 7. 样式属性

FlowChart 组件使用 Raphael.js 属性系统来定义节点和连接线的样式。以下是常用的样式属性：

### 7.1 基本样式

| 属性名 | 类型 | 说明 |
|--------|------|------|
| fill | String | 填充颜色 |
| stroke | String | 描边颜色 |
| stroke-width | Number | 描边宽度 |
| opacity | Number | 透明度（0-1） |
| fill-opacity | Number | 填充透明度（0-1） |
| stroke-opacity | Number | 描边透明度（0-1） |

### 7.2 文本样式

| 属性名 | 类型 | 说明 |
|--------|------|------|
| font | String | 字体样式，如："12px Arial" |
| font-family | String | 字体族 |
| font-size | Number | 字体大小 |
| font-weight | String/Number | 字体粗细 |
| text-anchor | String | 文本对齐方式：start, middle, end |

### 7.3 连接线样式

| 属性名 | 类型 | 说明 |
|--------|------|------|
| arrow-start | String | 起始箭头样式 |
| arrow-end | String | 结束箭头样式 |
| stroke-dasharray | String | 虚线样式，如："- ", ". ", "- ." |
| stroke-linecap | String | 线帽样式：butt, round, square |
| stroke-linejoin | String | 线连接样式：miter, round, bevel |

## 8. 节点类型

### 8.1 矩形节点（rect）
```json
{
  "id": "rectNode",
  "type": "rect",
  "x": 100,
  "y": 100,
  "width": 120,
  "height": 60,
  "text": "矩形节点",
  "attr": {
    "fill": "#2196F3",
    "stroke": "#1565C0",
    "stroke-width": 2,
    "r": 5
  }
}
```

### 8.2 圆形节点（circle）
```json
{
  "id": "circleNode",
  "type": "circle",
  "cx": 200,
  "cy": 200,
  "r": 40,
  "text": "圆形节点",
  "attr": {
    "fill": "#FF9800",
    "stroke": "#F57C00"
  }
}
```

### 8.3 椭圆节点（ellipse）
```json
{
  "id": "ellipseNode",
  "type": "ellipse",
  "cx": 300,
  "cy": 300,
  "rx": 60,
  "ry": 40,
  "text": "椭圆节点",
  "attr": {
    "fill": "#4CAF50",
    "stroke": "#388E3C"
  }
}
```

### 8.4 文本节点（text）
```json
{
  "id": "textNode",
  "type": "text",
  "x": 400,
  "y": 400,
  "text": "文本节点",
  "attr": {
    "font-size": 16,
    "font-weight": "bold",
    "fill": "#333"
  }
}
```

## 9. 连接线类型

### 9.1 直线连接
```json
{
  "id": "lineConn",
  "type": "line",
  "from": "node1",
  "to": "node2",
  "attr": {
    "stroke": "#666",
    "stroke-width": 2
  }
}
```

### 9.2 箭头连接
```json
{
  "id": "arrowConn",
  "type": "arrow",
  "from": "node1",
  "to": "node2",
  "attr": {
    "stroke": "#666",
    "stroke-width": 2,
    "arrow-end": "classic-wide-long"
  }
}
```

### 9.3 曲线连接
```json
{
  "id": "curveConn",
  "type": "curve",
  "from": "node1",
  "to": "node2",
  "attr": {
    "stroke": "#666",
    "stroke-width": 2,
    "arrow-end": "block-wide-long"
  }
}
```

## 10. 使用示例

### 10.1 基本流程图
```json
{
  "type": "FlowChart",
  "id": "basicFlowchart",
  "properties": {
    "width": 600,
    "height": 400,
    "nodes": [
      {
        "id": "start",
        "type": "circle",
        "cx": 100,
        "cy": 200,
        "r": 30,
        "text": "开始",
        "attr": {
          "fill": "#4CAF50",
          "stroke": "#388E3C"
        }
      },
      {
        "id": "process1",
        "type": "rect",
        "x": 200,
        "y": 170,
        "width": 120,
        "height": 60,
        "text": "处理1",
        "attr": {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      {
        "id": "decision",
        "type": "rect",
        "x": 380,
        "y": 170,
        "width": 120,
        "height": 60,
        "text": "判断",
        "attr": {
          "fill": "#FFC107",
          "stroke": "#FFA000"
        }
      },
      {
        "id": "process2",
        "type": "rect",
        "x": 200,
        "y": 280,
        "width": 120,
        "height": 60,
        "text": "处理2",
        "attr": {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      {
        "id": "end",
        "type": "circle",
        "cx": 440,
        "cy": 310,
        "r": 30,
        "text": "结束",
        "attr": {
          "fill": "#F44336",
          "stroke": "#D32F2F"
        }
      }
    ],
    "connectors": [
      {
        "id": "conn1",
        "type": "arrow",
        "from": "start",
        "to": "process1",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn2",
        "type": "arrow",
        "from": "process1",
        "to": "decision",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn3",
        "type": "arrow",
        "from": "decision",
        "to": "process2",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn4",
        "type": "arrow",
        "from": "process2",
        "to": "end",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn5",
        "type": "arrow",
        "from": "decision",
        "to": "process1",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "stroke-dasharray": "- ",
          "arrow-end": "classic-wide-long"
        }
      }
    ]
  }
}
```

### 10.2 复杂流程图
```javascript
var complexFlowchart = ood.create({
  type: "FlowChart",
  id: "complexFlowchart",
  properties: {
    width: 1000,
    height: 600,
    nodes: [
      // 开始节点
      {
        id: "start",
        type: "circle",
        cx: 100,
        cy: 100,
        r: 30,
        text: "开始",
        attr: {
          "fill": "#4CAF50",
          "stroke": "#388E3C"
        }
      },
      // 多个处理节点
      {
        id: "process1",
        type: "rect",
        x: 200,
        y: 70,
        width: 120,
        height: 60,
        text: "处理1",
        attr: {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      {
        id: "process2",
        type: "rect",
        x: 380,
        y: 70,
        width: 120,
        height: 60,
        text: "处理2",
        attr: {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      {
        id: "process3",
        type: "rect",
        x: 200,
        y: 200,
        width: 120,
        height: 60,
        text: "处理3",
        attr: {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      {
        id: "process4",
        type: "rect",
        x: 380,
        y: 200,
        width: 120,
        height: 60,
        text: "处理4",
        attr: {
          "fill": "#2196F3",
          "stroke": "#1565C0"
        }
      },
      // 判断节点
      {
        id: "decision1",
        type: "rect",
        x: 290,
        y: 140,
        width: 120,
        height: 60,
        text: "判断1",
        attr: {
          "fill": "#FFC107",
          "stroke": "#FFA000"
        }
      },
      {
        id: "decision2",
        type: "rect",
        x: 560,
        y: 135,
        width: 120,
        height: 60,
        text: "判断2",
        attr: {
          "fill": "#FFC107",
          "stroke": "#FFA000"
        }
      },
      // 结束节点
      {
        id: "end",
        type: "circle",
        cx: 750,
        cy: 160,
        r: 30,
        text: "结束",
        attr: {
          "fill": "#F44336",
          "stroke": "#D32F2F"
        }
      }
    ],
    connectors: [
      // 连接线
      {
        "id": "conn1",
        "type": "arrow",
        "from": "start",
        "to": "process1",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn2",
        "type": "arrow",
        "from": "process1",
        "to": "decision1",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn3",
        "type": "arrow",
        "from": "decision1",
        "to": "process2",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn4",
        "type": "arrow",
        "from": "decision1",
        "to": "process3",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn5",
        "type": "arrow",
        "from": "process3",
        "to": "process4",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn6",
        "type": "arrow",
        "from": "process2",
        "to": "decision2",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn7",
        "type": "arrow",
        "from": "process4",
        "to": "decision2",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      },
      {
        "id": "conn8",
        "type": "arrow",
        "from": "decision2",
        "to": "end",
        "attr": {
          "stroke": "#666",
          "stroke-width": 2,
          "arrow-end": "classic-wide-long"
        }
      }
    ]
  }
});
```

## 11. 最佳实践

1. **节点设计**：
   - 使用不同颜色区分不同类型的节点
   - 保持节点大小适中，便于阅读和操作
   - 为节点添加清晰的文本标签

2. **连接线设计**：
   - 使用箭头明确指示流程方向
   - 避免连接线交叉过多，保持流程图清晰
   - 对于复杂流程图，考虑使用不同样式的连接线区分不同类型的关系

3. **布局规划**：
   - 从左到右或从上到下组织流程
   - 使用网格对齐节点，保持流程图整洁
   - 为未来扩展预留空间

4. **交互设计**：
   - 启用拖拽和缩放功能，方便用户查看和编辑
   - 添加适当的事件处理，增强用户体验
   - 提供清晰的操作反馈

5. **性能优化**：
   - 对于大型流程图，考虑使用虚拟滚动或分页加载
   - 避免过多的复杂样式和动画
   - 合理使用缓存机制

## 12. 浏览器兼容性

FlowChart 组件基于 Raphael.js，支持以下浏览器：

- Chrome 4+
- Firefox 3+
- Safari 3.1+
- Internet Explorer 6+
- Opera 9.5+

在不支持 SVG 的浏览器中，Raphael.js 会自动降级使用 VML。

## 13. 常见问题

### 13.1 流程图显示不完整
- 检查容器大小是否足够
- 调整流程图的 width 和 height 属性
- 使用 zoom 方法缩小视图

### 13.2 节点拖拽不流畅
- 减少节点数量或复杂度
- 关闭 showGrid 选项
- 增大 gridSize 值

### 13.3 连接线样式不生效
- 确保使用了正确的 Raphael.js 属性名
- 检查箭头样式是否受支持
- 确保 attr 对象格式正确

## 14. 总结

FlowChart 组件是一个功能强大的 SVG 流程图库，提供了丰富的节点类型和连接线样式，支持拖拽、缩放和交互操作。它适用于创建各种流程图、工作流图和关系图，广泛应用于业务流程设计、系统架构图、数据分析可视化等领域。

通过合理的节点设计、连接线设计和布局规划，可以创建出清晰、美观、易于理解的流程图。结合组件提供的交互功能，可以增强用户体验，提高工作效率。