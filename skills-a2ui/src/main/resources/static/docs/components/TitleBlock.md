# TitleBlock

标题块组件，继承自List组件，用于显示带有图标、标题、数字和"更多"链接的块状项目。常用于仪表板、信息卡片等场景。

## 类名
`ood.UI.TitleBlock`

## 继承
`ood.UI.List`

## 别名
`ood.UI.IconList` (为保持兼容性而保留)

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/TitleBlock.js"></script>

<!-- 创建标题块容器 -->
<div id="titleblock-container"></div>

<script>
// 创建标题块组件
var titleBlock = ood.UI.TitleBlock({
    items: [
        {
            id: 'item1',
            title: '信息报送',
            more: '>> 更多',
            msgnum: '1',
            flagClass: 'ri-grid-line',
            caption: 'item1'
        },
        {
            id: 'item2',
            title: '日常审批',
            more: '>> 更多',
            msgnum: '21',
            flagClass: 'bpmfont bpmVueFlyActivityOperation',
            caption: 'item2'
        }
    ],
    width: '16rem',
    height: '16rem',
    columns: 2,
    theme: 'light',
    responsive: true
}).appendTo('#titleblock-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `flagText` | String | `null` | 标志文本 |
| `flagClass` | String | `null` | 标志CSS类名（用于显示图标） |
| `flagStyle` | String | `null` | 标志样式 |
| `iconColors` | String | `null` | 图标颜色 |
| `itemColors` | String | `null` | 项目颜色 |
| `fontColors` | String | `null` | 字体颜色 |
| `moreColors` | String | `null` | "更多"链接颜色 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |
| `autoIconColor` | Boolean | `false` | 是否自动设置图标颜色 |
| `autoItemColor` | Boolean | `true` | 是否自动设置项目颜色 |
| `autoImgSize` | Boolean | `false` | 是否自动设置图片尺寸 |
| `autoItemSize` | Boolean | `true` | 是否自动设置项目尺寸 |
| `iconFontSize` | String | `'3.5em'` | 图标字体大小 |
| `itemMargin` | String | `0` | 项目外边距 |
| `itemPadding` | String | `0` | 项目内边距 |
| `itemWidth` | String | `32` | 项目宽度（带空间单位） |
| `itemHeight` | String | `32` | 项目高度（带空间单位） |
| `width` | String | `'16rem'` | 组件宽度 |
| `height` | String | `'16rem'` | 组件高度 |
| `columns` | Number | `0` | 列数（0表示自动） |
| `rows` | Number | `0` | 行数（0表示自动） |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 见示例 | 项目数组，每个项目包含：id, title, more, msgnum, flagClass, caption |
| `dock` | String | `'fill'` | 布局位置：'left', 'right', 'top', 'bottom', 'fill' |
| `selMode` | String | `'none'` | 选择模式：'none', 'single', 'multi' |
| `borderType` | String | `'none'` | 边框类型：'none', 'solid', 'dashed'等 |
| `columns` | Number | `2` | 网格列数 |

### 项目属性 (每个item可单独设置)

| 属性名 | 类型 | 描述 |
|--------|------|------|
| `id` | String | 项目唯一标识 |
| `title` | String | 显示标题 |
| `more` | String | "更多"链接文本 |
| `msgnum` | String | 数字显示（如消息数量） |
| `flagClass` | String | 图标CSS类名 |
| `caption` | String | 项目说明 |
| `itemClass` | String | 项目CSS类名 |
| `disabled` | Boolean | 是否禁用 |
| `readonly` | Boolean | 是否只读 |
| `itemPadding` | String | 项目内边距 |
| `itemMargin` | String | 项目外边距 |
| `itemStyle` | String | 项目内联样式 |
| `image` | String | 图片URL |
| `imageClass` | String | 图片CSS类名 |
| `iconFontCode` | String | 图标字体代码 |
| `imgWidth` | String | 图片宽度 |
| `imgHeight` | String | 图片高度 |
| `iconColor` | String | 图标颜色 |
| `fontColor` | String | 字体颜色 |
| `itemColor` | String | 项目颜色 |
| `bgColor` | String | 背景颜色 |
| `moreBgColor` | String | "更多"背景颜色 |

## 方法

### `_afterInsertItems(profile)`
项目插入后的回调方法，用于处理图片加载等。

**参数：**
- `profile` (Object): 组件配置对象

### `updateItemData(profile, item)`
更新项目数据并刷新显示。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 要更新的项目数据

**示例：**
```javascript
titleBlock.updateItemData(titleBlock.get(0), {
    id: 'item1',
    msgnum: '5',
    title: '更新后的标题'
});
```

### `_prepareData(profile)`
准备渲染数据，自动处理项目颜色等。

**参数：**
- `profile` (Object): 组件配置对象

**返回：**
- (Object): 处理后的渲染数据

### `_prepareItem(profile, item, oitem, pid, index, len)`
准备单个项目的渲染数据。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 项目数据
- `oitem` (Object): 原始项目数据
- `pid` (String): 父ID
- `index` (Number): 索引
- `len` (Number): 总长度

## 事件

### `onCmd(profile, item, e, src)`
命令事件处理器。

### `onMoreClick(profile, item, e, src)`
"更多"链接点击事件。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 被点击的项目
- `e` (Event): 事件对象
- `src` (Element): 触发事件的DOM元素

### `onTitleClick(profile, item, e, src)`
标题点击事件。

### `touchstart`, `touchmove`, `touchend`, `touchcancel`
触摸事件。

### `swipe`, `swipeleft`, `swiperight`, `swipeup`, `swipedown`
滑动事件。

### `press`, `pressup`
长按事件。

## CSS 变量 (Appearances)

组件支持以下CSS类名，可在主题中自定义样式：

| 类名 | 描述 |
|------|------|
| `ood-uitembg` | 项目背景样式 |
| `ood-uiborder-radius` | 圆角边框样式 |
| `ood-showfocus` | 焦点显示样式 |
| `ood-ui-ellipsis` | 文本溢出省略样式 |
| `ood-display-none` | 隐藏元素 |
| `ood-icon-loading` | 图标加载中样式 |
| `ood-load-error` | 加载错误样式 |
| `ood-uiflag-1` | 默认标志样式 |

### 外观样式 (Appearances)

| 选择器 | 描述 | 默认样式 |
|--------|------|----------|
| `EXTRA` | 额外内容区域 | `display: none` |
| `KEY` | 键区域 | `overflow: visible` |
| `ITEMS` | 项目容器 | `position: relative; overflow: auto; overflow-x: hidden; zoom: 1 (IE6)` |
| `ITEMS-nowrap` | 不换行模式 | `white-space: nowrap` |
| `ITEM` | 单个项目 | `display: inline-block; position: relative; cursor: pointer; vertical-align: top; margin: 0` |
| `ITEMFRAME` | 项目框架 | `display: block; position: relative; overflow: hidden; border: 0; padding: 0; margin: 0; -moz-box-flex: 1` |
| `IMAGE` | 图片元素 | `display: inline-block; visibility: hidden; vertical-align: middle` |
| `MSGNUM` | 数字显示区域 | `text-align: right; overflow: hidden; color: var(--ood-title-text); white-space: nowrap; font-weight: bold; font-size: 2em; margin-top: var(--ood-spacing-xs); margin-right: var(--ood-spacing-sm); height: 35px` |
| `TITLE` | 标题区域 | `color: var(--ood-title-text); display: block; margin-right: var(--ood-spacing-xs); text-align: right; font-size: 1em` |
| `MORE` | "更多"链接区域 | `color: var(--ood-title-text); display: block; background-color: var(--ood-title-more-bg); margin-top: var(--ood-spacing-sm); text-align: center; font-size: 0.75em` |
| `FLAG` | 标志区域 | `left: var(--ood-spacing-sm); top: 0.2em; text-align: left; color: var(--ood-title-text); opacity: 0.2; position: absolute; z-index: 10` |

## 示例

### 基本标题块

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/TitleBlock.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="dashboard-cards"></div>
    
    <script>
    // 创建仪表板卡片
    var dashboard = ood.UI.TitleBlock({
        items: [
            {
                id: 'messages',
                title: '未读消息',
                more: '查看详情 >>',
                msgnum: '12',
                flagClass: 'ri-message-line',
                caption: 'messages'
            },
            {
                id: 'tasks',
                title: '待办任务',
                more: '处理任务 >>',
                msgnum: '5',
                flagClass: 'ri-task-line',
                caption: 'tasks'
            },
            {
                id: 'notifications',
                title: '系统通知',
                more: '查看全部 >>',
                msgnum: '3',
                flagClass: 'ri-notification-line',
                caption: 'notifications'
            },
            {
                id: 'alerts',
                title: '预警信息',
                more: '立即处理 >>',
                msgnum: '7',
                flagClass: 'ri-alert-line',
                caption: 'alerts'
            }
        ],
        width: '100%',
        columns: 2,
        theme: 'light',
        responsive: true
    }).appendTo('#dashboard-cards');
    
    // 添加事件监听
    dashboard.onMoreClick = function(profile, item, e, src) {
        console.log('点击了更多链接:', item.id);
        // 这里可以添加跳转逻辑
    };
    
    dashboard.onTitleClick = function(profile, item, e, src) {
        console.log('点击了标题:', item.title);
        // 这里可以添加标题点击逻辑
    };
    </script>
</body>
</html>
```

### 带自定义样式的标题块

```html
<div id="custom-titleblock"></div>

<style>
/* 自定义标题块样式 */
.ood-uitembg.custom-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 15px;
    box-shadow: 0 10px 20px rgba(0,0,0,0.1);
    transition: transform 0.3s ease;
}

.ood-uitembg.custom-card:hover {
    transform: translateY(-5px);
}

.ood-ui-ellipsis.custom-title {
    font-size: 1.2em;
    font-weight: 600;
    color: white;
}

.ood-ui-ellipsis.custom-msgnum {
    font-size: 2.5em;
    font-weight: bold;
    color: #ffd700;
}

.custom-more {
    background-color: rgba(255,255,255,0.2) !important;
    color: white !important;
    border-radius: 20px;
    padding: 5px 15px;
}

.ood-uiflag-1.custom-icon {
    font-size: 4em;
    opacity: 0.3;
    color: white;
}
</style>

<script>
var customBlock = ood.UI.TitleBlock({
    items: [
        {
            id: 'card1',
            title: '项目进度',
            more: '查看详情',
            msgnum: '85%',
            flagClass: 'ri-progress-line custom-icon',
            itemClass: 'custom-card',
            caption: 'card1'
        }
    ],
    width: '18rem',
    height: '12rem',
    theme: 'light'
}).appendTo('#custom-titleblock');

// 添加自定义CSS类
customBlock.getSubNode('ITEM', true).addClass('custom-card');
customBlock.getSubNode('TITLE', true).addClass('custom-title');
customBlock.getSubNode('MSGNUM', true).addClass('custom-msgnum');
customBlock.getSubNode('MORE', true).addClass('custom-more');
</script>
```

### 响应式标题块网格

```html
<div id="responsive-grid"></div>

<script>
// 创建响应式标题块
var responsiveGrid = ood.UI.TitleBlock({
    items: [
        {id: 'stat1', title: '用户总数', more: '用户管理 >>', msgnum: '1,234', flagClass: 'ri-user-line', caption: 'stat1'},
        {id: 'stat2', title: '订单数量', more: '订单管理 >>', msgnum: '567', flagClass: 'ri-shopping-cart-line', caption: 'stat2'},
        {id: 'stat3', title: '营收总额', more: '财务分析 >>', msgnum: '¥89,012', flagClass: 'ri-money-dollar-circle-line', caption: 'stat3'},
        {id: 'stat4', title: '活跃用户', more: '用户分析 >>', msgnum: '789', flagClass: 'ri-user-heart-line', caption: 'stat4'},
        {id: 'stat5', title: '投诉建议', more: '客服中心 >>', msgnum: '23', flagClass: 'ri-customer-service-line', caption: 'stat5'},
        {id: 'stat6', title: '系统日志', more: '日志查询 >>', msgnum: '1,456', flagClass: 'ri-file-text-line', caption: 'stat6'}
    ],
    width: '100%',
    columns: 3,
    responsive: true,
    theme: 'light'
}).appendTo('#responsive-grid');

// 响应式调整（自动处理）
// 在小屏幕上自动调整列数
window.addEventListener('resize', function() {
    responsiveGrid.adjustLayout();
});
</script>
```

### 动态更新标题块数据

```html
<div id="dynamic-titleblock"></div>

<button onclick="updateData()">更新数据</button>

<script>
// 创建动态标题块
var dynamicBlock = ood.UI.TitleBlock({
    items: [
        {id: 'dyn1', title: '实时温度', more: '刷新数据', msgnum: '25°C', flagClass: 'ri-temp-hot-line', caption: 'dyn1'},
        {id: 'dyn2', title: 'CPU使用率', more: '性能监控', msgnum: '65%', flagClass: 'ri-cpu-line', caption: 'dyn2'}
    ],
    width: '20rem',
    columns: 1
}).appendTo('#dynamic-titleblock');

// 更新数据的函数
function updateData() {
    // 更新第一个项目的数据
    var item1 = {
        id: 'dyn1',
        title: '实时温度',
        more: '刷新数据',
        msgnum: (Math.random() * 10 + 20).toFixed(1) + '°C', // 随机温度
        flagClass: 'ri-temp-hot-line',
        caption: 'dyn1'
    };
    
    // 更新第二个项目的数据
    var item2 = {
        id: 'dyn2',
        title: 'CPU使用率',
        more: '性能监控',
        msgnum: (Math.random() * 50 + 30).toFixed(0) + '%', // 随机使用率
        flagClass: 'ri-cpu-line',
        caption: 'dyn2'
    };
    
    // 更新组件数据
    dynamicBlock.updateItemData(dynamicBlock.get(0), item1);
    
    // 如果要更新所有项目，可以重新设置items
    // dynamicBlock.set('items', [item1, item2]);
}
</script>
```

## 注意事项

1. **图标支持**：组件支持CSS类名设置图标，建议使用Remix Icon、Font Awesome等图标库。默认使用`ood-uiflag-1`类。

2. **响应式设计**：通过`columns`属性控制网格列数，配合`responsive: true`可在不同屏幕尺寸下自动调整布局。

3. **颜色系统**：支持自动颜色计算（`autoItemColor`等），也可通过`itemColors`、`fontColors`等属性手动设置颜色。

4. **触摸支持**：组件原生支持触摸事件和滑动操作，适合移动端使用。

5. **图片加载**：组件会处理图片的加载和错误状态，自动显示加载中或错误图标。

6. **兼容性**：为保持兼容性，`ood.UI.IconList`作为`ood.UI.TitleBlock`的别名存在，两者功能相同。

7. **性能优化**：当项目数量较多时，建议合理设置`columns`和`rows`属性，避免过度渲染影响性能。

8. **主题支持**：组件支持主题切换，通过`theme`属性设置'light'或'dark'主题。

9. **键盘导航**：项目支持键盘焦点和Tab键导航，增强可访问性。

10. **浏览器支持**：基于现代浏览器标准，IE需要兼容性处理（组件已包含部分IE6-IE7兼容代码）。