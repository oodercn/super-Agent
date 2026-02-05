# PopMenu

弹出菜单组件，继承自Widget和absList，提供可定制的弹出式菜单，支持图标、禁用状态、分组和响应式设计。

## 类名
`ood.UI.PopMenu`

## 继承
`ood.UI.Widget` 和 `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/PopMenu.js"></script>

<!-- 创建弹出菜单 -->
<div id="popmenu-container"></div>

<script>
var popMenu = ood.UI.PopMenu({
    items: [
        {id: 'a', caption: '选项1', imageClass: 'ri-number-1'},
        {id: 'b', caption: '选项2', imageClass: 'ri-number-2'},
        {id: 'c', caption: '选项3', imageClass: 'ri-number-3'},
        {id: 'd', caption: '选项4', imageClass: 'ri-number-4', disabled: true}
    ],
    width: 'auto',
    height: 'auto',
    theme: 'light',
    responsive: true
}).appendTo('#popmenu-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[...]` | 菜单项数组，包含id、caption、imageClass、disabled等属性 |

### 数据模型属性 (DataModel)

从代码分析，PopMenu组件继承absList的数据模型属性，主要包括：

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[]` | 菜单项列表 |
| `value` | String/Array | `''` | 当前选中值 |
| `selectable` | Boolean | `true` | 是否可选择 |
| `multiple` | Boolean | `false` | 是否多选 |
| `disabled` | Boolean | `false` | 是否禁用 |
| `readonly` | Boolean | `false` | 是否只读 |
| `visible` | Boolean | `true` | 是否可见 |
| `width` | String | `'auto'` | 宽度 |
| `height` | String | `'auto'` | 高度 |
| `maxWidth` | Number | `null` | 最大宽度 |
| `maxHeight` | Number | `null` | 最大高度 |

## 方法

### `adjustSize()`
调整菜单大小，根据内容自动计算最佳尺寸。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_setScroll()`
设置滚动条，当内容超出显示区域时显示滚动指示器。

**返回：**
- (Object): 组件实例，支持链式调用。

### `_scrollToBottom()`
滚动到底部。

### `_scrollToTop()`
滚动到顶部。

### `_scrollStepUp()`
向上滚动一步。

### `_scrollStepDown()`
向下滚动一步。

### `_onKeyDown(profile, event, src)`
键盘事件处理，支持方向键、回车键等导航。

### `_onKeyUp(profile, event, src)`
键盘释放事件处理。

### `_onItemMouseEnter(profile, event, src)`
鼠标进入菜单项事件。

### `_onItemMouseLeave(profile, event, src)`
鼠标离开菜单项事件。

### `_onItemClick(profile, event, src)`
菜单项点击事件。

### `_onItemDblClick(profile, event, src)`
菜单项双击事件。

### `_onTopClick(profile, event, src)`
顶部滚动按钮点击事件。

### `_onBottomClick(profile, event, src)`
底部滚动按钮点击事件。

## 事件

### `onItemSelected(profile, item, event, source, checktype)`
菜单项选中事件。

### `onItemMouseEnter(profile, event, src)`
鼠标进入菜单项事件。

### `onItemMouseLeave(profile, event, src)`
鼠标离开菜单项事件。

### `onItemClick(profile, event, src)`
菜单项点击事件。

### `onItemDblClick(profile, event, src)`
菜单项双击事件。

## CSS 变量 (Appearances)

组件支持以下CSS类名：

| 类名 | 描述 |
|------|------|
| `ood-popmenu` | 弹出菜单容器样式 |
| `ood-popmenu-border` | 菜单边框样式 |
| `ood-popmenu-box` | 菜单内容区域样式 |
| `ood-popmenu-items` | 菜单项容器样式 |
| `ood-popmenu-item` | 单个菜单项样式 |
| `ood-popmenu-item-hover` | 菜单项悬停样式 |
| `ood-popmenu-item-selected` | 菜单项选中样式 |
| `ood-popmenu-item-disabled` | 菜单项禁用样式 |

## 示例

### 基本弹出菜单

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/PopMenu.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <button onclick="showMenu()">显示菜单</button>
    <div id="menu-container" style="display:none;"></div>
    
    <script>
    var menu = ood.UI.PopMenu({
        items: [
            {id: 'new', caption: '新建文件', imageClass: 'ri-file-add-line'},
            {id: 'open', caption: '打开文件', imageClass: 'ri-folder-open-line'},
            {id: 'save', caption: '保存文件', imageClass: 'ri-save-line'},
            {id: 'divider', caption: '-'},
            {id: 'exit', caption: '退出', imageClass: 'ri-logout-box-line'}
        ]
    }).appendTo('#menu-container');
    
    function showMenu() {
        var container = document.getElementById('menu-container');
        container.style.display = 'block';
        container.style.position = 'absolute';
        container.style.top = '40px';
        container.style.left = '10px';
        menu.adjustSize();
    }
    
    // 监听菜单选择
    menu.onItemSelected = function(profile, item, event, source, checktype) {
        console.log('选择了:', item.caption);
        document.getElementById('menu-container').style.display = 'none';
    };
    </script>
</body>
</html>
```

### 带图标和状态的菜单

```html
<div id="advanced-menu"></div>

<script>
var advancedMenu = ood.UI.PopMenu({
    width: '200px',
    items: [
        {id: 'edit', caption: '编辑', imageClass: 'ri-edit-line', shortcut: 'Ctrl+E'},
        {id: 'copy', caption: '复制', imageClass: 'ri-file-copy-line', shortcut: 'Ctrl+C'},
        {id: 'paste', caption: '粘贴', imageClass: 'ri-clipboard-line', shortcut: 'Ctrl+V', disabled: true},
        {id: 'div1', caption: '-'},
        {id: 'find', caption: '查找', imageClass: 'ri-search-line', shortcut: 'Ctrl+F'},
        {id: 'replace', caption: '替换', imageClass: 'ri-exchange-line', shortcut: 'Ctrl+H'},
        {id: 'div2', caption: '-'},
        {id: 'tools', caption: '工具', imageClass: 'ri-tools-line', subItems: [
            {id: 'options', caption: '选项', imageClass: 'ri-settings-3-line'},
            {id: 'preferences', caption: '首选项', imageClass: 'ri-user-settings-line'}
        ]}
    ]
}).appendTo('#advanced-menu');
</script>
```

### 响应式弹出菜单

```html
<div class="menu-wrapper">
    <div id="responsive-menu"></div>
</div>

<style>
.menu-wrapper {
    position: relative;
    width: 100%;
    max-width: 600px;
    margin: 0 auto;
}
@media (max-width: 768px) {
    .ood-popmenu {
        max-width: 100%;
        font-size: 14px;
    }
}
@media (max-width: 480px) {
    .ood-popmenu-item {
        padding: 8px 12px;
    }
}
</style>

<script>
var responsiveMenu = ood.UI.PopMenu({
    items: [
        {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
        {id: 'profile', caption: '个人资料', imageClass: 'ri-user-line'},
        {id: 'messages', caption: '消息', imageClass: 'ri-message-line'},
        {id: 'settings', caption: '设置', imageGroup: 'ri-settings-line'}
    ],
    responsive: true
}).appendTo('#responsive-menu');

// 窗口大小变化时调整菜单
window.addEventListener('resize', function() {
    responsiveMenu.adjustSize();
});
</script>
```

## 注意事项

1. **菜单项格式**：每个菜单项支持id、caption、imageClass、disabled、shortcut、subItems等属性。

2. **分隔符**：使用`{caption: '-'}`创建菜单分隔线。

3. **子菜单**：通过`subItems`属性创建嵌套子菜单。

4. **键盘导航**：支持方向键、回车键、ESC键等键盘操作。

5. **响应式设计**：根据屏幕尺寸自动调整菜单布局和样式。

6. **滚动支持**：内容超出时显示滚动条和导航按钮。

7. **事件处理**：提供完整的鼠标和键盘事件支持。

8. **继承功能**：继承absList的数据绑定和选择功能。

9. **主题支持**：可通过CSS类名自定义菜单主题样式。

10. **性能优化**：大量菜单项时建议使用虚拟滚动优化性能。