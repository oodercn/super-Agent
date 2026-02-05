# Tensor

Tensor组件是一个iframe容器，用于嵌入和显示外部HTML内容，特别设计用于TensorFlow可视化、机器学习模型展示或其他需要iframe嵌入的复杂Web应用。

## 类名
`ood.UI.Tensor`

## 继承
`ood.UI`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Tensor.js"></script>

<!-- 创建Tensor容器 -->
<div id="tensor-container"></div>

<script>
var tensor = ood.UI.Tensor({
    src: '/plugins/dist/index.html',
    width: '40em',
    height: '30em',
    prepareFormData: true,
    uploadUrl: 'upload/',
    params: {
        modelId: '12345',
        version: '1.0'
    }
}).appendTo('#tensor-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `selectable` | Boolean | `true` | 是否可选择 |
| `width` | String | `'40em'` | 组件宽度（带空间单位） |
| `height` | String | `'30em'` | 组件高度（带空间单位） |
| `src` | String | `'/plugins/dist/index.html'` | iframe源URL，指定要加载的外部HTML页面 |
| `prepareFormData` | Boolean | `true` | 是否准备表单数据，启用后会自动收集页面上下文数据作为查询参数 |
| `uploadUrl` | String | `'upload/'` | 上传URL，用于文件上传操作 |
| `params` | Object | `{}` | 自定义参数对象，会作为查询参数附加到iframe的src URL中 |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `patterned` | Boolean | `false` | 是否启用图案背景 |
| `dock` | String | `'fill'` | 停靠方式：'fill'（填充）, 'left'（左停靠）, 'right'（右停靠）, 'top'（上停靠）, 'bottom'（下停靠） |

## 方法

### `setQueryData(data, path)`
设置查询数据，可以指定路径或替换整个params对象。

**参数：**
- `data` (Object): 要设置的数据对象
- `path` (String): 可选，点分隔的路径字符串，用于在params对象中设置嵌套属性

**示例：**
```javascript
// 设置整个params对象
tensor.setQueryData({modelId: '67890', dataset: 'mnist'});

// 设置嵌套属性
tensor.setQueryData('custom', 'modelId');
```

### `reload(profile)`
重新加载iframe内容，重新构建URL并刷新iframe。

**参数：**
- `profile` (Object): UI配置对象

**返回：**
- (String): 重新构建的src URL

**示例：**
```javascript
tensor.reload();
```

## 事件

### `uploadfile(profile, eventType, item, response)`
文件上传成功事件。

**参数：**
- `profile` (Object): UI配置对象
- `eventType` (String): 事件类型，固定为'uploadfile'
- `item` (Object): 上传的文件项
- `response` (Object): 服务器响应数据

### `uploadfail(profile, eventType, item, response)`
文件上传失败事件。

**参数：**
- `profile` (Object): UI配置对象
- `eventType` (String): 事件类型，固定为'uploadfail'
- `item` (Object): 上传的文件项
- `response` (Object): 服务器响应数据

### `uploadcomplete(profile, eventType, item, response)`
文件上传完成事件（无论成功或失败）。

**参数：**
- `profile` (Object): UI配置对象
- `eventType` (String): 事件类型，固定为'uploadcomplete'
- `item` (Object): 上传的文件项
- `response` (Object): 服务器响应数据

### `uploadprogress(profile, eventType, item, response)`
文件上传进度事件。

**参数：**
- `profile` (Object): UI配置对象
- `eventType` (String): 事件类型，固定为'uploadprogress'
- `item` (Object): 上传的文件项
- `response` (Object): 服务器响应数据，包含进度信息

## CSS 变量 (Appearances)

| 类名 | 描述 |
|------|------|
| `KEY` | 组件容器样式，包含边框、背景色和溢出隐藏 |
| `H5` | iframe元素样式，绝对定位，无边框，z-index为1 |
| `COVER` | 覆盖层样式，用于设计模式或特殊效果，绝对定位，可设置背景色 |

**详细CSS属性：**
- `KEY`: `overflow: hidden; border: 1px solid var(--ood-border); background-color: var(--ood-bg)`
- `H5`: `position: absolute; left: -1px; top: -1px; z-index: 1; border: none`
- `COVER`: `position: absolute; left: -1px; top: -1px; width: 0; height: 0; z-index: 4; background-color: var(--ood-overlay)`

## 示例

### 基本Tensor可视化

```html
<div id="tensor-viz"></div>

<script>
var tensorViz = ood.UI.Tensor({
    src: '/plugins/tensorflow/visualization.html',
    width: '800px',
    height: '600px',
    params: {
        model: 'neural-network',
        layers: 5,
        activation: 'relu'
    }
}).appendTo('#tensor-viz');

// 监听上传事件
tensorViz.on('uploadfile', function(profile, eventType, item, response) {
    console.log('文件上传成功:', item.name);
    console.log('服务器响应:', response);
});
</script>
```

### 动态更新Tensor参数

```html
<div id="dynamic-tensor"></div>
<button onclick="updateModel()">更新模型参数</button>

<script>
var dynamicTensor = ood.UI.Tensor({
    src: '/plugins/ml/model-viewer.html',
    width: '100%',
    height: '500px',
    prepareFormData: true,
    params: {
        modelType: 'regression',
        learningRate: 0.01
    }
}).appendTo('#dynamic-tensor');

function updateModel() {
    // 更新查询参数
    dynamicTensor.setQueryData({
        modelType: 'classification',
        learningRate: 0.001,
        epochs: 50
    });
    
    // 重新加载iframe
    dynamicTensor.reload();
}
</script>
```

### 文件上传集成

```html
<div id="upload-tensor"></div>

<script>
var uploadTensor = ood.UI.Tensor({
    src: '/plugins/upload/interface.html',
    width: '600px',
    height: '400px',
    uploadUrl: '/api/upload',
    prepareFormData: true,
    params: {
        maxFileSize: '10MB',
        allowedTypes: ['jpg', 'png', 'csv'],
        userId: 'user123'
    }
}).appendTo('#upload-tensor');

// 完整事件监听
uploadTensor.on('uploadprogress', function(profile, eventType, item, response) {
    var percent = response.progress || 0;
    console.log('上传进度:', percent + '%');
});

uploadTensor.on('uploadcomplete', function(profile, eventType, item, response) {
    console.log('上传完成，状态:', response.status);
});

uploadTensor.on('uploadfail', function(profile, eventType, item, response) {
    console.error('上传失败:', response.error);
});
</script>
```

### 响应式Tensor容器

```html
<div class="tensor-responsive">
    <h3>机器学习模型展示</h3>
    <div id="responsive-tensor"></div>
</div>

<style>
.tensor-responsive {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
}

@media (max-width: 768px) {
    .tensor-responsive {
        padding: 10px;
    }
}
</style>

<script>
var responsiveTensor = ood.UI.Tensor({
    src: '/plugins/responsive/model.html',
    width: '100%',
    height: '700px',
    params: {
        responsive: true,
        mobileOptimized: true
    }
}).appendTo('#responsive-tensor');

// 窗口大小变化时调整
window.addEventListener('resize', function() {
    responsiveTensor._onresize();
});
</script>
```

## 注意事项

1. **跨域限制**：iframe内容受浏览器同源策略限制，确保加载的内容与主页面同源或已正确配置CORS。

2. **URL构建**：当`prepareFormData`为true时，组件会自动收集页面上下文数据并作为查询参数附加到iframe URL中。

3. **上传功能**：`uploadUrl`属性指定文件上传的目标地址，需要配合后端上传接口使用。

4. **参数传递**：`params`对象中的所有属性都会作为查询参数传递到iframe中，支持复杂嵌套对象。

5. **安全性**：确保加载的外部内容来源可信，避免XSS攻击和其他安全风险。

6. **性能考虑**：iframe会创建独立的浏览器上下文，可能增加内存消耗，特别是在嵌入多个Tensor组件时。

7. **通信机制**：组件使用`postMessage` API与iframe内容进行跨域通信，确保iframe内容正确实现了消息处理。

8. **响应式设计**：组件支持响应式布局，但iframe内容的响应式行为取决于其内部实现。

9. **设计模式**：在编辑/设计模式下，会显示COVER覆盖层，防止与iframe内容交互。

10. **事件处理**：上传相关事件需要通过postMessage从iframe内部触发，确保iframe内容正确发送消息。

11. **浏览器兼容性**：基于HTML5 iframe和postMessage API，要求现代浏览器支持。旧版IE需要兼容性处理。

12. **移动端适配**：在移动设备上可能需要调整iframe尺寸和交互方式，确保良好的用户体验。