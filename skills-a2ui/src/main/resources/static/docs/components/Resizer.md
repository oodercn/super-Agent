# Resizer

调整器组件，用于为其他UI组件添加调整大小、移动、旋转等功能，常用于设计工具和可视化编辑场景。

## 类名
`ood.UI.Resizer`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Resizer.js"></script>

<!-- 为目标元素添加调整器 -->
<div id="target-element" style="position:absolute; left:100px; top:100px; width:200px; height:150px; background:#f0f0f0;">
  可调整的元素
</div>

<script>
// 获取目标元素
var target = ood('#target-element');

// 添加调整器
var resizer = target.addResizer({
    minWidth: 50,
    minHeight: 30,
    maxWidth: 500,
    maxHeight: 400,
    rotatable: true,
    leftConfigBtn: true,
    rightConfigBtn: true
});

// 监听调整事件
resizer.onUpdate(function(profile, target, size, cssPos, rotate) {
    console.log('元素被调整:', size);
});
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `_attached` | Boolean | `false` | 是否已附加到目标元素 |
| `forceVisible` | Boolean | `false` | 强制可见，即使父元素隐藏 |
| `forceMovable` | String | `''` | 强制可移动方向 |
| `singleDir` | Boolean | `false` | 单方向调整 |
| `vertical` | Boolean | `true` | 允许垂直调整 |
| `horizontal` | Boolean | `true` | 允许水平调整 |
| `minHeight` | Number | `12` | 最小高度（像素） |
| `minWidth` | Number | `12` | 最小宽度（像素） |
| `maxHeight` | Number | `5000` | 最大高度（像素） |
| `maxWidth` | Number | `5000` | 最大宽度（像素） |
| `handlerSize` | Number | `8`或`12` | 操作柄大小（根据浏览器盒模型） |
| `handlerOffset` | Number | `1`或`0` | 操作柄偏移量（根据浏览器盒模型） |
| `readonly` | Boolean | `false` | 只读模式 |
| `disabled` | Boolean | `false` | 禁用状态 |
| `leftConfigBtn` | Boolean | `false` | 显示左侧配置按钮 |
| `rightConfigBtn` | Boolean | `false` | 显示右侧配置按钮 |
| `rotatable` | Boolean | `false` | 允许旋转 |
| `left` | Number | `100` | 左侧位置 |
| `top` | Number | `100` | 顶部位置 |
| `height` | Number | `100` | 高度 |
| `width` | Number | `100` | 宽度 |
| `position` | String | `'absolute'` | 定位方式 |
| `display` | String | `'block'` | 显示方式 |

## 方法

### `_attachTo(target, parent)`
将调整器附加到目标元素。

**参数：**
- `target` (Object): 目标元素（ood.Dom对象）
- `parent` (Object, 可选): 父容器，默认为body

**返回：**
- (Object): 调整器实例，支持链式调用。

### `show()`
显示调整器。

**返回：**
- (Object): 调整器实例，支持链式调用。

### `hide()`
隐藏调整器。

**返回：**
- (Object): 调整器实例，支持链式调用。

## OOD.Dom插件方法

### `addResizer(properties, onUpdate, onChange)`
为目标元素添加调整器（插件方法）。

**参数：**
- `properties` (Object, 可选): 调整器配置属性
- `onUpdate` (Function, 可选): 调整更新时的回调函数
- `onChange` (Function, 可选): 调整变化时的回调函数

**返回：**
- (Object): 调整器实例。

### `removeResizer()`
移除目标元素的调整器（插件方法）。

**返回：**
- (Object): 目标元素本身，支持链式调用。

### `$getResizer()`
获取目标元素的调整器实例（插件方法）。

**返回：**
- (Object): 调整器实例或undefined。

## OOD.UI.Widget插件方法

### `_resizer(key, args)`
为Widget组件添加调整器（内部方法）。

### `_unResizer()`
移除Widget组件的调整器（内部方法）。

## 事件

### `onDblclick(profile, e, src)`
双击调整器时触发。

### `onUpdate(profile, target, size, cssPos, rotate)`
调整器更新目标元素大小时触发。

**参数：**
- `profile` (Object): 调整器profile对象
- `target` (Object): 目标元素
- `size` (Object): 大小信息 {width, height}
- `cssPos` (Object): 位置信息 {left, top}
- `rotate` (Number): 旋转角度

### `onChange(profile, proxy)`
调整器状态变化时触发。

## 示例

### 为自定义元素添加调整功能

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Resizer.js"></script>
    <style>
        .resizable-box {
            position: absolute;
            left: 150px;
            top: 150px;
            width: 300px;
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            cursor: move;
        }
    </style>
</head>
<body>
    <div id="my-box" class="resizable-box">
        <h3>可调整的元素</h3>
        <p>尝试拖动边框或角落进行调整</p>
    </div>
    
    <script>
    // 为自定义元素添加调整器
    var myBox = ood('#my-box');
    var resizer = myBox.addResizer({
        minWidth: 100,
        minHeight: 80,
        maxWidth: 800,
        maxHeight: 600,
        rotatable: true,
        leftConfigBtn: true,
        rightConfigBtn: true,
        handlerSize: 10,
        vertical: true,
        horizontal: true
    });
    
    // 监听调整事件
    resizer.onUpdate(function(profile, target, size, cssPos) {
        console.log('新尺寸:', size);
        console.log('新位置:', cssPos);
    });
    
    // 显示调整器
    resizer.show();
    </script>
</body>
</html>
```

### 集成到UI组件中

```html
<!-- 创建带调整功能的按钮组件 -->
<div id="custom-button-container"></div>

<script>
// 创建按钮组件
var customButton = ood.UI.ButtonLayout({
    items: [{id: 'btn1', caption: '可调整按钮', imageClass: 'ri-cursor-line'}],
    width: '200px',
    height: '60px',
    theme: 'light',
    // 启用调整功能
    resizer: true,
    resizerProp: {
        minWidth: 80,
        minHeight: 40,
        rotatable: false,
        leftConfigBtn: false,
        rightConfigBtn: false
    }
}).appendTo('#custom-button-container');

// 调整器会自动附加到按钮的BORDER节点
</script>
```

### 响应式设计支持

```html
<div id="design-element" style="position:absolute; left:50px; top:50px; width:250px; height:180px; background:#e0f7fa;">
  设计元素
</div>

<script>
var designElement = ood('#design-element');
var designResizer = designElement.addResizer({
    minWidth: 60,
    minHeight: 40,
    maxWidth: 600,
    maxHeight: 450,
    // 响应式限制
    responsiveLimits: {
        mobile: { maxWidth: 300, maxHeight: 200 },
        tablet: { maxWidth: 500, maxHeight: 350 }
    }
});

// 根据屏幕尺寸动态调整限制
function updateResizerLimits() {
    var width = window.innerWidth;
    if (width < 768) {
        designResizer.properties.maxWidth = 300;
        designResizer.properties.maxHeight = 200;
    } else if (width < 1024) {
        designResizer.properties.maxWidth = 500;
        designResizer.properties.maxHeight = 350;
    }
}

window.addEventListener('resize', updateResizerLimits);
</script>
```

## 注意事项

1. 调整器主要作为其他UI组件的附加功能使用，通常不单独实例化。
2. `addResizer` 是 `ood.Dom` 的插件方法，可直接在元素上调用。
3. `resizer` 和 `resizerProp` 是 `ood.UI.Widget` 的数据模型属性，用于在Widget组件中集成调整功能。
4. 调整器包含多个操作柄：四边（L,R,T,B）、四角（LT,RT,LB,RB）、移动（MOVE）、旋转（ROTATE）、配置按钮（CONF1,CONF2）。
5. 调整器支持最小/最大尺寸限制，防止元素过小或过大。
6. 只读（`readonly`）和禁用（`disabled`）状态会影响调整器的交互行为。
7. 旋转功能（`rotatable`）在IE8及以下浏览器中不可用。
8. 操作柄大小（`handlerSize`）和偏移量（`handlerOffset`）根据浏览器盒模型自动调整。
9. 调整器使用CSS变量定义样式，支持主题定制。
10. 调整事件（`onUpdate`）提供详细的调整信息，包括尺寸、位置和旋转角度。