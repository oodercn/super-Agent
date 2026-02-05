# OrganizationChart 组件

## 1. 组件概述

OrganizationChart 是 ooder-a2ui 框架中的 SVG 组织结构图组件，用于可视化展示企业、机构或团队的层级结构关系。它基于 SvgBase 组件构建，提供了丰富的图形元素和连接方式，可用于创建清晰直观的组织结构图。

### 1.1 核心功能

- 多层级组织结构展示
- 灵活的节点样式（矩形、圆形、自定义形状）
- 智能连接线（支持直线、折线、曲线）
- 节点文本标签支持
- 交互支持（悬停、点击等事件）
- 可扩展性（支持自定义节点和连接样式）
- 响应式设计

### 1.2 应用场景

- 企业组织结构展示
- 团队层级关系图
- 项目管理中的责任分配图
- 教育机构的层级结构图
- 医疗机构的科室关系图

## 2. 创建方式

### 2.1 模块方式

```javascript
ood.Class("ooder.OrganizationChart", "ood.Module", {
    Instance: {
        initialize: function() {
            this.iniComponents();
        },
        
        iniComponents: function() {
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);
            
            // 创建 SVGPaper 组件
            var svgPaper = ood.create("ood.UI.SVGPaper")
                .setHost(host, "svgPaper")
                .setWidth("100%")
                .setHeight("100%")
                .setOverflow("visible");
            append(svgPaper);
            
            // 添加组织结构图节点和连接线
            // ...
            
            return children;
        }
    }
});
```

### 2.2 直接使用 SVG 组件组合

```json
{
  "key": "ood.UI.SVGPaper",
  "properties": {
    "width": "100%",
    "height": "600px",
    "overflow": "visible"
  },
  "children": [
    {
      "key": "ood.svg.rectComb",
      "properties": {
        "attr": {
          "KEY": {
            "x": 550,
            "y": 100,
            "width": 300,
            "height": 60,
            "fill": "#3498db",
            "stroke": "#2980b9",
            "stroke-width": 1.5
          },
          "TEXT": {
            "text": "董事会",
            "fill": "#fff",
            "font-size": "18px",
            "font-weight": "bold"
          }
        }
      }
    },
    {
      "key": "ood.svg.connector",
      "properties": {
        "attr": {
          "KEY": {
            "path": "M,700,160L,700,200",
            "fill": "none",
            "stroke": "#6c757d",
            "stroke-width": 2,
            "arrow-end": "classic-wide-long"
          }
        }
      }
    }
    // 更多节点和连接线...
  ]
}
```

## 3. 组件构成

OrganizationChart 组件主要由以下 SVG 元素构成：

### 3.1 容器

- **ood.UI.SVGPaper**：作为组织结构图的画布容器，负责管理所有 SVG 元素

### 3.2 节点元素

| 元素类型 | 说明 | 适用场景 |
|----------|------|----------|
| **ood.svg.rectComb** | 带文本的矩形节点 | 高层职位、部门节点 |
| **ood.svg.circleComb** | 带文本的圆形节点 | 普通员工、具体角色 |
| **ood.svg.pathComb** | 带文本的自定义路径节点 | 特殊职位、自定义节点 |
| **ood.svg.text** | 纯文本节点 | 标题、说明文字 |

### 3.3 连接线元素

- **ood.svg.connector**：用于连接不同层级的节点，支持多种路径样式和箭头类型

## 4. 属性配置

### 4.1 SVGPaper 属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `width` | String/Number | "100%" | 画布宽度 |
| `height` | String/Number | "100%" | 画布高度 |
| `overflow` | String | "visible" | 溢出处理方式 |
| `graphicZIndex` | Number | 2 | 图形层级 |
| `scaleChildren` | Boolean | false | 是否缩放子元素 |

### 4.2 节点属性

#### 4.2.1 矩形节点 (rectComb)

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `x` | Number | 0 | 节点左上角 X 坐标 |
| `y` | Number | 0 | 节点左上角 Y 坐标 |
| `width` | Number | 100 | 节点宽度 |
| `height` | Number | 50 | 节点高度 |
| `rx` | Number | 0 | 圆角 X 半径 |
| `ry` | Number | 0 | 圆角 Y 半径 |
| `fill` | String | "#ffffff" | 填充颜色 |
| `stroke` | String | "#000000" | 边框颜色 |
| `stroke-width` | Number | 1 | 边框宽度 |
| `text` | String | "" | 节点文本 |
| `font-size` | Number | 14 | 文本字体大小 |
| `font-weight` | String | "normal" | 文本字体粗细 |
| `fill` | String | "#000000" | 文本颜色 |

#### 4.2.2 圆形节点 (circleComb)

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `cx` | Number | 0 | 圆心 X 坐标 |
| `cy` | Number | 0 | 圆心 Y 坐标 |
| `r` | Number | 20 | 半径 |
| `fill` | String | "#ffffff" | 填充颜色 |
| `stroke` | String | "#000000" | 边框颜色 |
| `stroke-width` | Number | 1 | 边框宽度 |
| `text` | String | "" | 节点文本 |
| `font-size` | Number | 12 | 文本字体大小 |
| `font-weight` | String | "normal" | 文本字体粗细 |
| `fill` | String | "#000000" | 文本颜色 |

### 4.3 连接线属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `path` | String | "" | SVG 路径字符串 |
| `stroke` | String | "#6c757d" | 线条颜色 |
| `stroke-width` | Number | 1.5 | 线条宽度 |
| `arrow-start` | String | "" | 起点箭头样式 |
| `arrow-end` | String | "classic-wide-long" | 终点箭头样式 |
| `stroke-dasharray` | String | "" | 虚线样式 |

## 5. 组织结构图设计模式

### 5.1 典型层级结构

```
董事会
    │
    ▼
    CEO
    │
    ├── CTO
    │   ├── 技术部经理
    │   └── 研发部经理
    │
    ├── CFO
    │   └── 财务部经理
    │
    ├── COO
    │   └── 运营部经理
    │
    └── CMO
        ├── 市场部经理
        └── 销售部经理
```

### 5.2 节点布局策略

1. **水平布局**：同一层级的节点水平排列
2. **垂直间距**：不同层级之间保持适当的垂直间距
3. **中心对齐**：子节点相对于父节点居中对齐
4. **宽度适配**：根据节点文本长度自动调整宽度
5. **连接线优化**：使用折线或曲线连接不同层级，避免交叉

## 6. 使用示例

### 6.1 基础组织结构图

```javascript
// 创建 SVGPaper
var svgPaper = ood.create("ood.UI.SVGPaper")
    .setWidth("100%")
    .setHeight("600px")
    .setOverflow("visible");

// 添加标题
svgPaper.append(ood.create("ood.svg.text")
    .setAttr({"KEY": {x: 700, y: 60, 'font-size': '24px', 'font-weight': 'bold', fill: '#2c3e50', text: '公司组织结构图'}}));

// 添加顶层节点 - 董事会
svgPaper.append(ood.create("ood.svg.rectComb")
    .setAttr({"KEY": {x: 550, y: 100, width: 300, height: 60, rx: 5, ry: 5, fill: '#3498db', stroke: '#2980b9', 'stroke-width': 1.5}, "TEXT": {text: '董事会', fill: '#fff', 'font-size': '18px', 'font-weight': 'bold'}}));

// 添加 CEO 节点
svgPaper.append(ood.create("ood.svg.rectComb")
    .setAttr({"KEY": {x: 625, y: 200, width: 150, height: 50, rx: 5, ry: 5, fill: '#2ecc71', stroke: '#27ae60', 'stroke-width': 1.5}, "TEXT": {text: 'CEO', fill: '#fff', 'font-size': '16px', 'font-weight': 'bold'}}));

// 连接董事会和 CEO
svgPaper.append(ood.create("ood.svg.connector")
    .setAttr({"KEY": {path: "M,700,160L,700,200", fill: "none", stroke: '#6c757d', 'stroke-width': 2, 'arrow-end': 'classic-wide-long'}}));
```

### 6.2 完整公司组织结构图

请参考示例文件：`SvgExamples/organization/OrganizationChart.js`

## 7. 最佳实践

### 7.1 设计原则

1. **清晰的层级关系**：使用不同颜色和大小区分不同层级的节点
2. **一致的视觉风格**：保持节点和连接线的样式一致性
3. **适当的间距**：确保节点之间有足够的间距，避免视觉拥挤
4. **简洁的文本**：节点文本应简洁明了，避免过长
5. **合理的布局**：根据组织结构的复杂程度选择合适的布局方式

### 7.2 性能优化

1. **避免过多节点**：单个组织结构图节点数量建议不超过 100 个
2. **合理使用分组**：对于复杂的组织结构，考虑使用分组或折叠功能
3. **优化连接线**：避免过多的交叉连接线
4. **使用缓存**：对于静态组织结构图，考虑使用缓存机制

### 7.3 交互设计

1. **悬停效果**：为节点添加悬停效果，显示更多信息
2. **点击事件**：支持点击节点查看详细信息
3. **缩放和平移**：对于大型组织结构图，支持缩放和平移功能
4. **搜索功能**：支持搜索节点

## 8. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分功能可能受限 |

## 9. 注意事项

1. **坐标系统**：SVG 使用的是基于画布左上角的坐标系统，设计时需注意
2. **文本定位**：文本位置需要根据节点大小和样式进行调整
3. **连接线路径**：复杂组织结构图的连接线路径需要仔细设计，避免交叉
4. **性能考虑**：大型组织结构图可能会影响页面性能，需合理设计
5. **响应式设计**：考虑在不同屏幕尺寸下的显示效果

## 10. 扩展阅读

- [SvgBase 组件文档](SvgBase.md)
- [SVG 路径教程](https://developer.mozilla.org/zh-CN/docs/Web/SVG/Tutorial/Paths)
- [组织结构图设计最佳实践](https://www.nngroup.com/articles/organizational-charts/)
- [ooder-a2ui 高级 SVG 应用指南](../ADVANCED_SVG.md)

OrganizationChart 组件为 ooder-a2ui 框架提供了强大的组织结构图绘制能力，通过合理使用该组件，可以创建出清晰、直观、交互性强的组织结构图，帮助用户更好地理解和管理复杂的层级关系。