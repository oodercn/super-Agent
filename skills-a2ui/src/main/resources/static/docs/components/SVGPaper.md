# SVGPaper 组件

SVGPaper（SVG画布）组件，继承自Div组件，提供SVG图形绘制和编辑功能，支持多种预定义形状、文本、图像等SVG元素的创建和管理。常用于图表绘制、图形编辑、可视化设计等场景。

## 类名
`ood.UI.SVGPaper`

## 继承
`ood.UI.Div`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/SVGPaper.js"></script>

<!-- 创建SVG画布 -->
<div id="svgpaper-container"></div>

<script>
var svgPaper = ood.UI.SVGPaper({
    width: '32em',
    height: '25em',
    scaleChildren: false,
    overflow: undefined,
    graphicZIndex: 0
}).appendTo('#svgpaper-container');

// 添加SVG形状
svgPaper.append({
    key: 'ood.svg.circle',
    properties: {
        svgTag: 'Shapes:Circle',
        attr: {
            stroke: "#004A7F",
            fill: "#ffffff",
            "stroke-width": 2,
            r: 20,
            cx: 50,
            cy: 50
        }
    }
});
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式属性 |
| `iframeAutoLoad` | Object | `null` | iframe自动加载配置 |
| `html` | Object | `null` | HTML内容配置 |
| `width` | Object | `{$spaceunit: 1, ini: '32em'}` | 画布宽度 |
| `height` | Object | `{$spaceunit: 1, ini: '25em'}` | 画布高度 |
| `scaleChildren` | Object | `{ini: false}` | 是否缩放子元素 |
| `overflow` | Object | `{ini: undefined}` | 溢出处理方式 |
| `graphicZIndex` | Object | `{ini: 0, action: function(v){...}}` | 图形层级索引 |

## 方法

### 图形操作方法

```javascript
// 设置子元素（替换现有子元素）
svgPaper.setChildren([
    {key: 'ood.svg.circle', properties: {...}},
    {key: 'ood.svg.rect', properties: {...}}
]);

// 添加SVG元素
svgPaper.append({key: 'ood.svg.text', properties: {...}});

// 获取Paper对象（底层SVG库实例）
var paper = svgPaper.getPaper();

// 获取SVG字符串
var svgString = svgPaper.getSVGString();
```

### 继承自Div的方法
SVGPaper组件继承了Div组件的所有方法，包括：

- `append(target)` - 添加元素
- `removeChildren()` - 移除所有子元素
- `css(property, value)` - 设置CSS样式
- `attr(name, value)` - 设置HTML属性

## 预定义SVG形状

SVGPaper组件提供了一系列预定义的SVG形状，可以直接使用：

### 基础形状
| 形状ID | 名称 | 描述 |
|--------|------|------|
| `ood.svg.circle` | 圆形 | 可设置半径(r)、圆心(cx,cy)、描边、填充等属性 |
| `ood.svg.ellipse` | 椭圆 | 可设置水平半径(rx)、垂直半径(ry)、中心点(cx,cy) |
| `ood.svg.rect` | 矩形 | 可设置位置(x,y)、宽度(width)、高度(height) |
| `ood.svg.path1` | 三角形 | 使用路径(path)定义的三角形 |
| `ood.svg.path2` | 五角星 | 使用路径(path)定义的五角星 |
| `ood.svg.path3` | 直线 | 使用路径(path)定义的直线 |
| `ood.svg.path4` | 贝塞尔曲线 | 使用路径(path)定义的贝塞尔曲线 |

### 文本和图像
| 形状ID | 名称 | 描述 |
|--------|------|------|
| `ood.svg.text` | 文本 | SVG文本元素，可设置内容、位置、字体等属性 |
| `ood.svg.image` | 图像 | SVG图像元素，可设置源(src)、位置、尺寸等属性 |

### 提示框
| 形状ID | 名称 | 描述 |
|--------|------|------|
| `ood.svg.Tips0` | 简单提示框 | 矩形框加文本的提示框组合 |

### 箭头
| 形状ID | 名称 | 描述 |
|--------|------|------|
| `ood.svg.arrow1` | 箭头1 | 自定义路径箭头形状 |
| `ood.svg.arrow2` | 箭头2 | 自定义路径箭头形状 |
| `ood.svg.arrow3` | 箭头3 | 自定义路径箭头形状 |
| `ood.svg.arrow4` | 箭头4 | 自定义路径箭头形状 |

## 模板结构

SVGPaper组件基于Div模板构建，主要包含以下子节点：

| 节点 | 描述 |
|------|------|
| `SVG` | SVG画布容器 |
| 各种预定义形状 | 通过key指定形状类型 |

## SVG属性配置

每个SVG形状可以配置以下通用属性：

```javascript
{
    svgTag: 'Shapes:Circle', // 形状类型标识
    attr: {
        stroke: "#004A7F",      // 描边颜色
        fill: "#ffffff",        // 填充颜色
        "stroke-width": 2,      // 描边宽度
        r: 20,                  // 半径（圆形）
        cx: 50,                 // 圆心X坐标
        cy: 50                  // 圆心Y坐标
    }
}
```

## 事件

### 继承事件
继承自Div组件的事件系统：

- `onClick` - 当画布被点击时触发
- `onMouseMove` - 当鼠标在画布上移动时触发
- `onMouseDown` - 当鼠标按键在画布上按下时触发
- `onMouseUp` - 当鼠标按键在画布上释放时触发

### 自定义事件
```javascript
// 监听图形添加事件
svgPaper.on('shapeAdded', function(profile, shapeData) {
    console.log('添加了形状:', shapeData.svgTag);
});
```

## 示例

### 创建基本图形
```javascript
var svgPaper = ood.UI.SVGPaper({
    width: '500px',
    height: '300px'
});

// 添加圆形
svgPaper.append({
    key: 'ood.svg.circle',
    properties: {
        svgTag: 'Shapes:Circle',
        attr: {
            stroke: "#FF0000",
            fill: "#FFCCCC",
            "stroke-width": 3,
            r: 30,
            cx: 100,
            cy: 100
        }
    }
});

// 添加矩形
svgPaper.append({
    key: 'ood.svg.rect',
    properties: {
        svgTag: 'Shapes:Rect',
        attr: {
            stroke: "#0000FF",
            fill: "#CCCCFF",
            "stroke-width": 2,
            x: 200,
            y: 80,
            width: 60,
            height: 40
        }
    }
});
```

### 添加文本和图像
```javascript
// 添加文本
svgPaper.append({
    key: 'ood.svg.text',
    properties: {
        svgTag: 'Shapes:Text',
        attr: {
            x: 50,
            y: 200,
            'font-size': '16px',
            'font-weight': 'bold',
            fill: '#333333',
            text: "SVG文本示例"
        }
    }
});

// 添加图像
svgPaper.append({
    key: 'ood.svg.image',
    properties: {
        attr: {
            src: 'path/to/image.png',
            x: 300,
            y: 150,
            width: 80,
            height: 60
        }
    }
});
```

### 导出SVG内容
```javascript
// 获取SVG字符串
var svgString = svgPaper.getSVGString();
console.log('SVG内容:', svgString);

// 可以将SVG字符串保存为文件
// 或嵌入到HTML中
```

## 注意事项

1. SVGPaper组件依赖于底层的SVG库（ood.svg）
2. 所有SVG形状通过预定义的key进行创建
3. 图形属性通过attr对象配置，遵循SVG标准属性名
4. 支持自定义路径形状，通过path属性定义
5. 图形层级通过graphicZIndex控制，数值越大显示越靠前
6. 画布尺寸可以通过width和height属性设置，支持各种CSS单位
7. 添加的形状可以通过后续操作进行修改和删除