# ToolBar

工具栏组件，用于创建和管理工具按钮、分组和下拉菜单的集合。支持主题切换、响应式设计和可访问性增强，常用于应用程序的顶部或侧边工具栏。

## 类名
`ood.UI.ToolBar`

## 继承
`["ood.UI", "ood.absList"]`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/ToolBar.js"></script>

<!-- 创建工具栏容器 -->
<div id="toolbar-container"></div>

<script>
// 创建工具栏组件
var toolbar = ood.UI.ToolBar({
    items: [
        {
            id: 'common',
            sub: [
                {
                    id: 'new',
                    caption: '新建',
                    imageClass: 'ri-calendar-event-line'
                },
                {
                    id: 'delete',
                    caption: '删除',
                    imageClass: 'ri-close-line'
                },
                {
                    id: 'reload',
                    caption: '刷新',
                    imageClass: 'ri-refresh-line'
                }
            ]
        }
    ],
    theme: 'light',
    responsive: true,
    handler: true,
    hAlign: 'left',
    dock: 'top',
    width: '100%',
    height: 'auto'
}).appendTo('#toolbar-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light', 'dark', 'high-contrast'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `expression` | String | `''` | 表达式（保留属性） |
| `listKey` | String | `null` | 列表键（保留属性） |
| `iconColors` | String | `null` | 图标颜色设置 |
| `itemColors` | String | `null` | 项目颜色设置 |
| `fontColors` | String | `null` | 字体颜色设置 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |
| `autoIconColor` | Boolean | `true` | 是否自动设置图标颜色 |
| `autoItemColor` | Boolean | `false` | 是否自动设置项目颜色 |
| `height` | String | `'auto'` | 组件高度（只读属性） |
| `iconFontSize` | String | `''` | 图标字体大小 |
| `width` | String | `'auto'` | 组件宽度（带空间单位） |
| `left` | String | `0` | 左侧位置（带空间单位） |
| `top` | String | `0` | 顶部位置（带空间单位） |
| `handler` | Boolean | `true` | 是否显示拖动句柄 |
| `position` | String | `'absolute'` | 定位方式 |
| `hAlign` | String | `'left'` | 水平对齐方式：'left', 'center', 'right' |
| `dock` | String | `'top'` | 停靠位置：'top', 'bottom', 'left', 'right' |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 见示例 | 工具栏项目数组，支持分组结构 |
| `theme` | String | `'light'` | 初始主题：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `handler` | Boolean | `true` | 是否显示拖动句柄 |
| `hAlign` | String | `'left'` | 水平对齐方式 |
| `dock` | String | `'top'` | 停靠位置 |
| `autoIconColor` | Boolean | `true` | 是否自动设置图标颜色 |
| `autoItemColor` | Boolean | `false` | 是否自动设置项目颜色 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |

### 项目属性 (每个item可单独设置)

| 属性名 | 类型 | 描述 |
|--------|------|------|
| `id` | String | 项目唯一标识 |
| `caption` | String | 显示文本 |
| `imageClass` | String | 图标CSS类名（支持图标字体） |
| `type` | String | 按钮类型：默认按钮、'statusButton'（状态按钮）、'dropButton'（下拉按钮）、'split'（分割线） |
| `value` | Boolean | 状态按钮的当前值（true/false） |
| `sub` | Array | 子项目数组（用于分组或下拉菜单） |
| `hidden` | Boolean | 是否隐藏 |
| `disabled` | Boolean | 是否禁用 |
| `label` | String | 标签文本 |
| `image` | String | 图片URL |
| `imagePos` | String | 图片位置 |
| `groupStyle` | String | 分组样式 |
| `itemClass` | String | 项目CSS类名 |
| `itemStyle` | String | 项目内联样式 |

## 方法

### `setTheme(theme)`
设置工具栏主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `toggleTheme()`
在三个主题之间循环切换：light → dark → high-contrast → light。

**返回：**
- (Object): 组件实例，支持链式调用。

### `toggleDarkMode()`
在light和dark主题之间切换（旧版兼容方法）。

**返回：**
- (Object): 组件实例，支持链式调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。在小屏幕设备上优化显示。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航支持。

**返回：**
- (Object): 组件实例，支持链式调用。

### `updateItem(subId, options)`
更新指定工具项的属性。

**参数：**
- `subId` (String): 工具项ID
- `options` (Object): 更新选项，支持caption、value、disabled、hidden、imageClass等属性

**示例：**
```javascript
toolbar.updateItem('new', {
    caption: '新建文件',
    imageClass: 'ri-file-add-line',
    disabled: false
});
```

**返回：**
- (Object): 组件实例，支持链式调用。

### `showItem(itemId, value)`
显示或隐藏指定工具项。

**参数：**
- `itemId` (String): 工具项ID
- `value` (Boolean): true显示，false隐藏

**返回：**
- (Object): 组件实例，支持链式调用。

### `showGroup(grpId, value)`
显示或隐藏整个工具组。

**参数：**
- `grpId` (String): 工具组ID
- `value` (Boolean): true显示，false隐藏

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

### `onClick(profile, item, group, e, src, itemid)`
工具项点击事件处理器。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 被点击的工具项数据
- `group` (Object): 所在工具组数据
- `e` (Event): 事件对象
- `src` (Element): 触发事件的DOM元素
- `itemid` (String): 工具项ID

**示例：**
```javascript
toolbar.onClick = function(profile, item, group, e, src, itemid) {
    console.log('点击了工具项:', itemid, '标题:', item.caption);
    if (itemid === 'new') {
        // 执行新建操作
        createNewDocument();
    }
};
```

## CSS 变量 (Appearances)

组件支持以下CSS类名，可在主题中自定义样式：

| 选择器 | 描述 | 默认样式 |
|--------|------|----------|
| `KEY` | 键区域 | `position: absolute; overflow: hidden; left: 0; top: 0` |
| `RULER` | 标尺元素 | `padding: 0; margin: 0; width: 0` |
| `ICON` | 图标元素 | `margin: 0; font-size: 1.25em; color: var(--icon-color); transition: all 0.2s ease; vertical-align: middle` |
| `BTN:hover ICON` | 悬停时的图标 | `color: var(--icon-hover)` |
| `ITEMS` | 项目容器 | `display: flex; flex-wrap: wrap; background-color: var(--ood-toolbar-bg); border: var(--ood-toolbar-border); border-radius: 0.25rem; gap: 0.25em; box-shadow: 0 1px 3px rgba(0,0,0,0.1)` |
| `HANDLER` | 拖动句柄 | `height: 100%; width: .75em; background: url(img/handler.png) repeat-y left top; cursor: move; vertical-align: middle` |
| `GROUP` | 工具组容器 | `position: static; padding: .125em .25em 0 .125em; vertical-align: middle` |
| `ITEM` | 工具项容器 | `vertical-align: middle; padding: 0 .125em; margin: 0` |
| `SPLIT` | 分割线 | `width: 0; vertical-align: middle; margin: 0 .25em` |
| `BTN` | 按钮元素 | `cursor: pointer; border-radius: 0.25rem; background-color: var(--ood-toolbar-bg); color: var(--ood-toolbar-text); border: 1px solid transparent; transition: all 0.2s ease; display: inline-flex; align-items: center; justify-content: center; gap: 0.5em; padding: 0.5em 0.25em; font-weight: normal` |
| `BTN:hover` | 悬停按钮 | `background-color: var(--ood-toolbar-item-hover); box-shadow: 0 2px 5px rgba(0,0,0,0.1)` |
| `BTN:active` | 激活按钮 | `background-color: var(--ood-toolbar-item-active)` |
| `BTN-disabled` | 禁用按钮 | `opacity: 0.6; cursor: not-allowed` |
| `BOX` | 框容器 | `height: auto` |
| `LABEL, CAPTION` | 标签和标题 | `vertical-align: middle; margin-left: .25em; margin-right: .25em; font-size: 1em` |
| `LABEL` | 标签 | `cursor: default; padding: .25em` |
| `DROP` | 下拉箭头 | `vertical-align: middle` |
| `toolbar-mobile BTN` | 移动端按钮 | `min-width: 2em; padding: 0.2em 0.4em` |
| `toolbar-mobile CAPTION` | 移动端标题 | `font-size: 0.9em` |
| `toolbar-mobile GROUP` | 移动端分组 | `margin: 0 0.2em` |
| `toolbar-tiny CAPTION` | 小屏幕标题 | `display: none` |
| `toolbar-tiny BTN` | 小屏幕按钮 | `min-width: 1.8em; padding: 0.2em` |

### 主题类名
| 类名 | 描述 |
|------|------|
| `toolbar-dark` | 暗黑主题样式类 |
| `toolbar-hc` | 高对比度主题样式类 |
| `toolbar-mobile` | 移动端响应式样式类（宽度<768px时自动添加） |
| `toolbar-tiny` | 超小屏幕响应式样式类（宽度<480px时自动添加） |

## 示例

### 基本工具栏

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/ToolBar.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="main-toolbar"></div>
    
    <script>
    // 创建主工具栏
    var mainToolbar = ood.UI.ToolBar({
        items: [
            {
                id: 'file',
                caption: '文件',
                sub: [
                    {id: 'new', caption: '新建', imageClass: 'ri-file-add-line'},
                    {id: 'open', caption: '打开', imageClass: 'ri-folder-open-line'},
                    {id: 'save', caption: '保存', imageClass: 'ri-save-line'},
                    {type: 'split'},
                    {id: 'exit', caption: '退出', imageClass: 'ri-door-open-line'}
                ]
            },
            {
                id: 'edit',
                caption: '编辑',
                sub: [
                    {id: 'undo', caption: '撤销', imageClass: 'ri-arrow-go-back-line'},
                    {id: 'redo', caption: '重做', imageClass: 'ri-arrow-go-forward-line'},
                    {type: 'split'},
                    {id: 'cut', caption: '剪切', imageClass: 'ri-scissors-line'},
                    {id: 'copy', caption: '复制', imageClass: 'ri-file-copy-line'},
                    {id: 'paste', caption: '粘贴', imageClass: 'ri-clipboard-line'}
                ]
            },
            {
                id: 'view',
                caption: '视图',
                sub: [
                    {id: 'zoom-in', caption: '放大', imageClass: 'ri-zoom-in-line'},
                    {id: 'zoom-out', caption: '缩小', imageClass: 'ri-zoom-out-line'},
                    {id: 'actual-size', caption: '实际大小', imageClass: 'ri-zoom-reset-line'}
                ]
            }
        ],
        width: '100%',
        theme: 'light',
        responsive: true
    }).appendTo('#main-toolbar');
    
    // 添加点击事件处理
    mainToolbar.onClick = function(profile, item, group, e, src, itemid) {
        console.log('工具栏点击:', {
            item: itemid,
            caption: item.caption,
            group: group ? group.id : '无分组'
        });
        
        // 根据itemid执行不同操作
        switch(itemid) {
            case 'new':
                alert('执行新建操作');
                break;
            case 'save':
                alert('执行保存操作');
                break;
            // ... 其他操作
        }
    };
    </script>
</body>
</html>
```

### 带状态按钮的工具栏

```html
<div id="status-toolbar"></div>

<script>
var statusToolbar = ood.UI.ToolBar({
    items: [
        {
            id: 'format',
            sub: [
                {
                    id: 'bold',
                    caption: '加粗',
                    imageClass: 'ri-bold',
                    type: 'statusButton',
                    value: false
                },
                {
                    id: 'italic',
                    caption: '斜体',
                    imageClass: 'ri-italic',
                    type: 'statusButton',
                    value: true
                },
                {
                    id: 'underline',
                    caption: '下划线',
                    imageClass: 'ri-underline',
                    type: 'statusButton',
                    value: false
                }
            ]
        },
        {
            id: 'align',
            sub: [
                {
                    id: 'left',
                    caption: '左对齐',
                    imageClass: 'ri-align-left',
                    type: 'statusButton',
                    value: true
                },
                {
                    id: 'center',
                    caption: '居中对齐',
                    imageClass: 'ri-align-center',
                    type: 'statusButton',
                    value: false
                },
                {
                    id: 'right',
                    caption: '右对齐',
                    imageClass: 'ri-align-right',
                    type: 'statusButton',
                    value: false
                }
            ]
        }
    ],
    theme: 'light',
    width: 'auto'
}).appendTo('#status-toolbar');

// 监听状态变化
statusToolbar.onClick = function(profile, item, group, e, src, itemid) {
    if (item.type === 'statusButton') {
        console.log('状态按钮切换:', itemid, '新值:', item.value);
        // 更新UI或其他逻辑
        updateFormatting(itemid, item.value);
    }
};

function updateFormatting(style, enabled) {
    // 根据样式和启用状态更新文档格式
    console.log('更新格式:', style, enabled ? '启用' : '禁用');
}
</script>
```

### 响应式工具栏

```html
<div id="responsive-toolbar"></div>

<style>
/* 自定义响应式样式 */
.responsive-container {
    width: 100%;
    padding: 10px;
    background: #f5f5f5;
    border-radius: 8px;
    margin-bottom: 20px;
}

@media (max-width: 768px) {
    .responsive-container {
        padding: 5px;
    }
}
</style>

<script>
var responsiveToolbar = ood.UI.ToolBar({
    items: [
        {
            id: 'nav',
            sub: [
                {id: 'home', caption: '首页', imageClass: 'ri-home-line'},
                {id: 'back', caption: '返回', imageClass: 'ri-arrow-left-line'},
                {id: 'forward', caption: '前进', imageClass: 'ri-arrow-right-line'}
            ]
        },
        {
            id: 'actions',
            sub: [
                {id: 'print', caption: '打印', imageClass: 'ri-printer-line'},
                {id: 'share', caption: '分享', imageClass: 'ri-share-line'},
                {id: 'settings', caption: '设置', imageClass: 'ri-settings-line'}
            ]
        },
        {
            id: 'tools',
            sub: [
                {id: 'search', caption: '搜索', imageClass: 'ri-search-line'},
                {id: 'filter', caption: '筛选', imageClass: 'ri-filter-line'},
                {id: 'sort', caption: '排序', imageClass: 'ri-sort-asc'}
            ]
        }
    ],
    theme: 'light',
    responsive: true,
    width: '100%'
}).appendTo('#responsive-toolbar');

// 添加窗口大小变化监听
window.addEventListener('resize', function() {
    responsiveToolbar.adjustLayout();
});

// 初始调整布局
responsiveToolbar.adjustLayout();
</script>
```

### 暗黑主题工具栏

```html
<div id="dark-toolbar"></div>

<button onclick="toggleTheme()">切换主题</button>

<script>
var darkToolbar = ood.UI.ToolBar({
    items: [
        {
            id: 'media',
            sub: [
                {id: 'play', caption: '播放', imageClass: 'ri-play-line'},
                {id: 'pause', caption: '暂停', imageClass: 'ri-pause-line'},
                {id: 'stop', caption: '停止', imageClass: 'ri-stop-line'},
                {type: 'split'},
                {id: 'volume-up', caption: '音量+', imageClass: 'ri-volume-up-line'},
                {id: 'volume-down', caption: '音量-', imageClass: 'ri-volume-down-line'},
                {id: 'mute', caption: '静音', imageClass: 'ri-volume-mute-line'}
            ]
        }
    ],
    theme: 'dark', // 初始设置为暗黑主题
    width: 'auto',
    height: 'auto'
}).appendTo('#dark-toolbar');

// 主题切换函数
function toggleTheme() {
    darkToolbar.toggleTheme();
    console.log('当前主题:', darkToolbar.getTheme());
}

// 添加可访问性增强
darkToolbar.enhanceAccessibility();

// 添加键盘导航支持
document.addEventListener('keydown', function(e) {
    if (e.key === 'Tab' && document.activeElement.closest('.ood-ui-toolbar')) {
        // 工具栏内的Tab键导航
        console.log('工具栏键盘导航激活');
    }
});
</script>
```

### 动态更新工具栏

```html
<div id="dynamic-toolbar"></div>

<button onclick="addTool()">添加工具</button>
<button onclick="removeTool()">移除工具</button>
<button onclick="disableTool()">禁用/启用工具</button>

<script>
var dynamicToolbar = ood.UI.ToolBar({
    items: [
        {
            id: 'basic',
            sub: [
                {id: 'tool1', caption: '工具1', imageClass: 'ri-tools-line'},
                {id: 'tool2', caption: '工具2', imageClass: 'ri-wrench-line'}
            ]
        }
    ],
    theme: 'light',
    width: '300px'
}).appendTo('#dynamic-toolbar');

// 添加新工具
function addTool() {
    // 更新项目
    dynamicToolbar.updateItem('basic', {
        sub: [
            {id: 'tool1', caption: '工具1', imageClass: 'ri-tools-line'},
            {id: 'tool2', caption: '工具2', imageClass: 'ri-wrench-line'},
            {id: 'tool3', caption: '新增工具', imageClass: 'ri-add-circle-line'}
        ]
    });
    console.log('已添加新工具');
}

// 移除工具
function removeTool() {
    dynamicToolbar.showItem('tool2', false);
    console.log('已隐藏工具2');
}

// 禁用/启用工具
var toolDisabled = false;
function disableTool() {
    toolDisabled = !toolDisabled;
    dynamicToolbar.updateItem('tool1', {
        disabled: toolDisabled,
        caption: toolDisabled ? '工具1(禁用)' : '工具1'
    });
    console.log('工具1', toolDisabled ? '已禁用' : '已启用');
}
</script>
```

## 注意事项

1. **主题系统**：组件支持light、dark、high-contrast三种主题，可通过`setTheme()`方法切换。主题设置自动保存到localStorage。

2. **响应式设计**：
   - 宽度<768px：自动添加`toolbar-mobile`类，优化移动端显示
   - 宽度<480px：自动添加`toolbar-tiny`类，进一步简化界面
   - 可通过`adjustLayout()`方法手动触发布局调整

3. **可访问性增强**：
   - 自动添加ARIA属性：`role="toolbar"`, `aria-label`, `aria-orientation`等
   - 为工具按钮添加`role="button"`, `aria-label`, `tabindex`属性
   - 状态按钮支持`aria-pressed`属性
   - 下拉按钮支持`aria-haspopup`, `aria-expanded`, `aria-controls`属性

4. **工具项类型**：
   - 普通按钮：默认类型，点击触发操作
   - 状态按钮（`type: "statusButton"`）：类似复选框，有选中/未选中状态
   - 下拉按钮（`type: "dropButton"`）：带有下拉菜单的按钮
   - 分割线（`type: "split"`）：视觉分隔线，不响应点击

5. **分组结构**：工具栏支持多级分组，`items`数组中的每个对象代表一个工具组，`sub`属性包含该组的工具项。

6. **拖动支持**：通过`handler: true`启用拖动句柄，用户可重新排列工具组位置。

7. **颜色系统**：支持自动颜色计算（`autoIconColor`, `autoItemColor`, `autoFontColor`），也可通过CSS变量手动设置颜色。

8. **兼容性**：组件已包含对旧版IE（IE6-IE7）的兼容性处理，确保在现代化浏览器和旧版浏览器中都能正常工作。

9. **性能优化**：工具栏渲染使用虚拟DOM技术，大量工具项时仍能保持良好性能。

10. **事件系统**：与OOD框架事件系统深度集成，支持框架特有的`onClick`事件处理机制。