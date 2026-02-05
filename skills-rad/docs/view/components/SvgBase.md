# SvgBase 组件

## 1. 组件概述

SvgBase 是 ooder-a2ui 框架中的核心 SVG 组件，提供了基于 Raphael.js 库的 SVG 图形绘制和操作功能。它是所有 SVG 图形组件的基础类，负责初始化 SVG 环境、处理 SVG 属性、提供图形操作方法等。

### 1.1 核心功能

- SVG 环境初始化与管理
- 多种 SVG 元素支持（圆形、椭圆、矩形、文本、图像、路径等）
- 图形属性设置与获取
- 图形层级管理（前置、后置）
- 图形变换与动画支持
- 连接点与路径处理
- 事件处理与交互支持

### 1.2 应用场景

- 数据可视化图表
- 流程图与组织结构图
- 工业状态监控图
- 技术架构图
- 交互式图形编辑

## 2. 创建方式

### 2.1 JSON 方式

```json
{
  "key": "ood.svg.circle",
  "properties": {
    "attr": {
      "stroke": "#004A7F",
      "fill": "#ffffff",
      "stroke-width": 2,
      "r": 20,
      "cx": 0,
      "cy": 0
    }
  }
}
```

### 2.2 JavaScript 方式

```javascript
ood.create("ood.svg.circle")
  .setProperties({
    attr: {
      stroke: "#004A7F",
      fill: "#ffffff",
      "stroke-width": 2,
      "r": 20,
      "cx": 0,
      "cy": 0
    }
  });
```

## 3. 属性列表

### 3.1 基础属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `attr` | Object | {} | SVG 元素的属性集合，包含位置、大小、样式等信息 |
| `svgTag` | String | "" | SVG 元素的标签类型，用于设计器识别 |
| `shadow` | Boolean | false | 是否显示阴影效果 |

### 3.2 通用 SVG 属性

SvgBase 支持以下通用 SVG 属性，这些属性可以在 `attr` 对象中设置：

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `stroke` | String | 边框颜色 |
| `stroke-width` | Number | 边框宽度 |
| `stroke-dasharray` | String | 虚线样式 |
| `fill` | String | 填充颜色 |
| `fill-opacity` | Number | 填充透明度 |
| `opacity` | Number | 元素透明度 |
| `transform` | String | 变换矩阵 |
| `cursor` | String | 鼠标指针样式 |
| `title` | String | 提示文本 |

### 3.3 元素特定属性

不同类型的 SVG 元素支持不同的特定属性：

#### 圆形 (circle)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `cx` | Number | 圆心 X 坐标 |
| `cy` | Number | 圆心 Y 坐标 |
| `r` | Number | 半径 |

#### 椭圆 (ellipse)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `cx` | Number | 圆心 X 坐标 |
| `cy` | Number | 圆心 Y 坐标 |
| `rx` | Number | X 轴半径 |
| `ry` | Number | Y 轴半径 |

#### 矩形 (rect)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `x` | Number | 左上角 X 坐标 |
| `y` | Number | 左上角 Y 坐标 |
| `width` | Number | 宽度 |
| `height` | Number | 高度 |
| `r` | Number | 圆角半径 |

#### 文本 (text)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `x` | Number | 文本 X 坐标 |
| `y` | Number | 文本 Y 坐标 |
| `text` | String | 文本内容 |
| `font` | String | 字体设置 |
| `font-size` | Number | 字体大小 |
| `font-family` | String | 字体系列 |
| `font-weight` | String | 字体粗细 |
| `text-anchor` | String | 文本对齐方式 |

#### 图像 (image)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `src` | String | 图像 URL |
| `x` | Number | 左上角 X 坐标 |
| `y` | Number | 左上角 Y 坐标 |
| `width` | Number | 宽度 |
| `height` | Number | 高度 |

#### 路径 (path)

| 属性名 | 类型 | 说明 |
|--------|------|------|
| `path` | String | SVG 路径字符串 |

## 4. 方法列表

### 4.1 属性操作

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `getAttr(key)` | 获取 SVG 元素属性 | key: 属性名（可选） | 属性值或属性对象 |
| `setAttr(key, attr, reset, notify)` | 设置 SVG 元素属性 | key: 属性名或属性对象<br>attr: 属性值（当 key 为字符串时）<br>reset: 是否重置默认属性<br>notify: 是否触发通知 | 当前组件实例 |

### 4.2 层级管理

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `toFront()` | 将图形置于顶层 | 无 | 当前组件实例 |
| `toBack()` | 将图形置于底层 | 无 | 当前组件实例 |

### 4.3 位置与尺寸

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `getLeft()` | 获取图形左边界坐标 | 无 | 数值 |
| `setLeft(value)` | 设置图形左边界坐标 | value: 坐标值 | 当前组件实例 |
| `getTop()` | 获取图形上边界坐标 | 无 | 数值 |
| `setTop(value)` | 设置图形上边界坐标 | value: 坐标值 | 当前组件实例 |
| `getWidth()` | 获取图形宽度 | 无 | 数值 |
| `setWidth(value)` | 设置图形宽度 | value: 宽度值 | 当前组件实例 |
| `getHeight()` | 获取图形高度 | 无 | 数值 |
| `setHeight(value)` | 设置图形高度 | value: 高度值 | 当前组件实例 |
| `_getBBox(key, withTransform)` | 获取图形边界框 | key: 属性名（可选）<br>withTransform: 是否包含变换 | 边界框对象或属性值 |

### 4.4 连接点管理

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `_getConnectAnchors()` | 获取图形连接点 | 无 | 连接点对象 |
| `_getConnectPath()` | 获取图形连接路径 | 无 | 路径字符串 |
| `_getConnectPoint(anchor)` | 获取指定连接点坐标 | anchor: 连接点名称或坐标 | 连接点坐标对象 |

### 4.5 其他方法

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `getAllNodes()` | 获取所有 SVG 节点 | 无 | 节点集合 |
| `getPaper()` | 获取 Raphael 纸张对象 | 无 | Raphael 纸张对象 |
| `elemsAnimate(endpoints, ms, easing, callback)` | 执行元素动画 | endpoints: 目标属性<br>ms: 动画时长<br>easing: 缓动函数<br>callback: 回调函数 | 当前组件实例 |

## 5. 组件继承关系

SvgBase 是所有 SVG 组件的基础类，其他 SVG 组件如：

- `ood.svg.circle`（圆形）
- `ood.svg.ellipse`（椭圆）
- `ood.svg.rect`（矩形）
- `ood.svg.text`（文本）
- `ood.svg.image`（图像）
- `ood.svg.path`（路径）
- `ood.svg.rectComb`（组合矩形）
- `ood.svg.pathComb`（组合路径）

都继承自 SvgBase 组件，并在此基础上扩展了特定的功能和属性。

## 6. 使用示例

### 6.1 基本圆形示例

```json
{
  "key": "ood.svg.circle",
  "properties": {
    "attr": {
      "stroke": "#004A7F",
      "fill": "#ffffff",
      "stroke-width": 2,
      "r": 30,
      "cx": 50,
      "cy": 50
    }
  }
}
```

### 6.2 矩形带圆角示例

```json
{
  "key": "ood.svg.rect",
  "properties": {
    "attr": {
      "stroke": "#004A7F",
      "fill": "#5198D3",
      "stroke-width": 2,
      "x": 20,
      "y": 20,
      "width": 100,
      "height": 60,
      "r": 10
    }
  }
}
```

### 6.3 文本示例

```json
{
  "key": "ood.svg.text",
  "properties": {
    "attr": {
      "x": 50,
      "y": 50,
      "text": "Hello SVG",
      "font-size": "16px",
      "font-weight": "bold",
      "fill": "#004A7F"
    }
  }
}
```

### 6.4 路径示例

```json
{
  "key": "ood.svg.path",
  "properties": {
    "attr": {
      "stroke": "#004A7F",
      "stroke-width": 2,
      "fill": "#A1C8F6",
      "path": "M0,0L100,0L100,100L0,100Z"
    }
  }
}
```

## 7. 最佳实践

### 7.1 性能优化

- 避免频繁更新大量 SVG 元素
- 使用批量操作减少 DOM 重绘
- 合理使用缓存机制
- 对于复杂图形，考虑使用组合组件

### 7.2 最佳使用场景

- 数据可视化图表
- 交互式流程图
- 组织结构图
- 工业监控状态图
- 技术架构图

## 8. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分功能可能受限 |

## 9. 注意事项

1. SvgBase 组件依赖 Raphael.js 库，确保在使用前已正确加载
2. SVG 元素的坐标系统基于其父容器，使用前需了解坐标映射关系
3. 对于复杂 SVG 图形，建议使用组合组件或自定义组件
4. 避免在 SVG 中使用过多复杂滤镜和渐变，可能影响性能
5. 注意 SVG 元素的层级关系，避免不必要的重绘

## 10. 扩展阅读

- [Raphael.js 官方文档](http://raphaeljs.com/)
- [SVG 官方规范](https://www.w3.org/TR/SVG/)
- [ooder-a2ui 高级 SVG 应用指南](..//ADVANCED_SVG.md)

SvgBase 组件为 ooder-a2ui 框架提供了强大的 SVG 图形处理能力，是构建复杂数据可视化和交互式图形应用的基础。通过合理使用 SvgBase 及其扩展组件，可以创建出丰富多样的 SVG 图形应用。