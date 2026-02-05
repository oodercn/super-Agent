# FoldingList

可折叠列表组件，用于创建可展开/折叠的内容区域，支持主题切换、响应式设计和可访问性增强。常用于FAQ、设置面板、文档目录等场景。

## 类名
`ood.UI.FoldingList`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/FoldingList.js"></script>

<!-- 创建可折叠列表 -->
<div id="foldinglist-container"></div>

<script>
var foldingList = ood.UI.FoldingList({
    items: [
        {id: 'item1', caption: '常见问题1', title: '如何开始使用？', text: '请参考入门指南...'},
        {id: 'item2', caption: '常见问题2', title: '如何配置设置？', text: '在设置面板中...'},
        {id: 'item3', caption: '常见问题3', title: '技术支持', text: '请联系客服...'}
    ],
    theme: 'light',
    responsive: true,
    collapsible: true,
    activeLast: true,
    width: '600px',
    height: '400px'
}).appendTo('#foldinglist-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 预定义的5个标签项 | 列表项数组，每个项包含 id、caption、title、text 等 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `value` | Object | `null` | 当前值 |
| `borderType` | String | `null` | 边框类型 |
| `activeLast` | Boolean | `true` | 是否自动激活最后一个项 |
| `transitionDuration` | String | `'normal'` | 动画过渡时长：'fast', 'normal', 'slow' |
| `collapsible` | Boolean | `true` | 是否可折叠 |
| `theme` | Object | `{ini: 'light', listbox: ['light', 'dark', 'high-contrast']}` | 主题设置 |
| `responsive` | Object | `{ini: true}` | 响应式布局设置 |

## 方法

### `setTheme(theme)`
设置主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleTheme()`
循环切换主题（light → dark → high-contrast → light）。

**返回：**
- (Object): 组件实例，支持链式调用。

### `fillContent(id, obj)`
为指定ID的列表项填充内容。

**参数：**
- `id` (String): 列表项ID
- `obj` (Object/String): 要填充的内容对象或HTML字符串

**返回：**
- (Object): 组件实例，支持链式调用。

### `toggle(id)`
切换指定ID的列表项的展开/折叠状态。

**参数：**
- `id` (String): 列表项ID

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_prepareItems(profile, arr, pid)` (内部方法)
准备列表项数据。

### `_prepareItem(profile, item)` (内部方法)
准备单个列表项。

### `_buildBody(profile, item)` (内部方法)
构建列表项内容体。

## 事件

### 自定义事件处理器 (EventHandlers)

| 事件名 | 参数 | 描述 |
|--------|------|------|
| `onGetContent` | `profile, item, onEnd` | 获取列表项内容时触发，可用于异步加载内容 |
| `onShowOptions` | `profile, item, e, src` | 显示选项时触发 |

### 触摸和手势事件
组件支持所有标准触摸事件：
- `touchstart` - 触摸开始
- `touchmove` - 触摸移动
- `touchend` - 触摸结束
- `touchcancel` - 触摸取消

## 示例

### 基本可折叠列表

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/FoldingList.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="my-foldinglist"></div>
    
    <script>
    // 创建可折叠列表
    var foldingList = ood.UI.FoldingList({
        items: [
            {id: 'intro', caption: '介绍', title: '欢迎使用', text: '这是一个可折叠列表组件示例...'},
            {id: 'features', caption: '功能', title: '主要功能', text: '1. 可折叠/展开\n2. 主题切换\n3. 响应式设计...'},
            {id: 'usage', caption: '用法', title: '如何使用', text: '参考快速开始部分...'}
        ],
        theme: 'dark',
        collapsible: true,
        activeLast: false,
        width: '800px',
        height: '500px'
    }).appendTo('#my-foldinglist');
    
    // 监听项切换
    foldingList.on('toggle', function(itemId, isExpanded) {
        console.log('项', itemId, isExpanded ? '展开' : '折叠');
    });
    </script>
</body>
</html>
```

### 异步加载内容的可折叠列表

```html
<div id="async-foldinglist"></div>

<script>
var asyncList = ood.UI.FoldingList({
    items: [
        {id: 'user', caption: '用户信息', title: '加载用户数据...'},
        {id: 'stats', caption: '统计', title: '加载统计数据...'},
        {id: 'history', caption: '历史记录', title: '加载历史记录...'}
    ],
    theme: 'light',
    collapsible: true
}).appendTo('#async-foldinglist');

// 自定义内容加载处理器
asyncList.on('onGetContent', function(profile, item, callback) {
    // 模拟异步加载
    setTimeout(function() {
        var content = '<div class="content">' + 
                      '<h3>' + item.title + '</h3>' +
                      '<p>这是异步加载的内容，ID: ' + item.id + '</p>' +
                      '</div>';
        callback(content);
    }, 1000);
});
</script>
```

### 响应式可折叠列表

```html
<div id="responsive-foldinglist"></div>

<script>
var responsiveList = ood.UI.FoldingList({
    items: [
        {id: 'faq1', caption: 'FAQ1', title: '常见问题1', text: '回答内容1...'},
        {id: 'faq2', caption: 'FAQ2', title: '常见问题2', text: '回答内容2...'},
        {id: 'faq3', caption: 'FAQ3', title: '常见问题3', text: '回答内容3...'},
        {id: 'faq4', caption: 'FAQ4', title: '常见问题4', text: '回答内容4...'},
        {id: 'faq5', caption: 'FAQ5', title: '常见问题5', text: '回答内容5...'}
    ],
    responsive: true,
    theme: 'light',
    width: '100%',
    height: 'auto'
}).appendTo('#responsive-foldinglist');
</script>
```

## 注意事项

1. 组件默认包含5个预定义列表项（tab1-tab5），可通过 `items` 属性完全自定义。
2. 设置 `activeLast: true` 时，最后一个列表项会自动展开并加载内容。
3. 主题切换功能会自动保存到 localStorage，页面刷新后保持相同主题。
4. 响应式设计通过CSS媒体查询和JavaScript动态调整实现，确保在不同设备上的良好显示。
5. 可访问性增强功能会自动为列表项添加 ARIA 属性，支持键盘导航（Tab、Enter、Space、方向键）。
6. 使用 `onGetContent` 事件处理器可以实现异步内容加载，适合加载大量或远程数据。
7. 组件支持触摸手势操作，适合移动端使用。
8. 动画过渡效果通过CSS关键帧实现，可通过 `transitionDuration` 属性调整速度。