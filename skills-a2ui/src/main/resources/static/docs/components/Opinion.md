# Opinion

Opinion意见反馈组件，用于展示和管理用户意见、评论或反馈条目，支持多列布局、图标显示和状态标记。

## 类名
`ood.UI.Opinion`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Opinion.js"></script>

<!-- 创建意见列表容器 -->
<div id="opinion-container"></div>

<script>
var opinionList = ood.UI.Opinion({
    width: '41.833em',
    columns: 1,
    items: [
        {
            id: 'feedback1',
            caption: '界面设计需要改进',
            comment: '建议增加更多的视觉层次和色彩对比',
            creatorName: '张三',
            department: '设计部',
            createDateStr: '2024-03-01',
            imageClass: 'ri-feedback-line'
        },
        {
            id: 'feedback2',
            caption: '性能优化建议',
            comment: '页面加载速度有待提升，建议压缩资源',
            creatorName: '李四',
            department: '技术部',
            createDateStr: '2024-03-02',
            image: 'avatar.png'
        }
    ],
    autoItemSize: true,
    autoImgSize: false
}).appendTo('#opinion-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `columns` | Number | `1` | 列数，用于多列布局 |
| `width` | String | `'41.833333333333336em'` | 组件宽度 |
| `items` | Array | 预定义的意见项目数组 | 意见条目配置数组，包含id、caption、comment、creatorName等 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `tagCmds` | Array | `null` | 标签命令数组 |
| `bgimg` | String | `null` | 背景图片URL |
| `iotStatus` | String | `null` | IoT状态标识 |
| `flagText` | String | `null` | 标志文本 |
| `flagClass` | String | `null` | 标志样式类名 |
| `flagStyle` | String | `null` | 标志内联样式 |
| `tagCmdsAlign` | String | `null` | 标签命令对齐方式 |
| `autoImgSize` | Boolean | `false` | 是否自动调整图片大小 |
| `autoItemSize` | Boolean | `true` | 是否自动调整项目大小 |
| `iconOnly` | Boolean | `false` | 是否仅显示图标 |
| `iconFontSize` | String | `''` | 图标字体大小 |
| `itemMargin` | Number | `6` | 项目外边距（像素） |
| `itemPadding` | Number | `2` | 项目内边距（像素） |
| `itemWidth` | String | `'32em'` | 项目宽度（带空间单位） |
| `itemHeight` | String | `'32em'` | 项目高度（带空间单位） |
| `itemClass` | String | `''` | 项目自定义CSS类名 |
| `itemStyle` | String | `''` | 项目内联样式 |

### 意见项目配置 (items)

每个意见项目支持以下属性：

| 属性名 | 类型 | 描述 |
|--------|------|------|
| `id` | String | 项目唯一标识符 |
| `caption` | String | 标题文本 |
| `comment` | String | 详细评论内容 |
| `creatorName` | String | 创建者姓名 |
| `department` | String | 所属部门 |
| `createDateStr` | String | 创建日期字符串 |
| `level` | String | 级别标识，如'经理级'、'员工级'等 |
| `imageClass` | String | 图标CSS类名 |
| `image` | String | 图片URL |
| `iconFontCode` | String | 字体图标代码 |
| `hidden` | Boolean | 是否隐藏项目 |
| `itemClass` | String | 项目自定义CSS类名 |
| `itemStyle` | String | 项目内联样式 |
| `imgWidth` | Number | 图片宽度 |
| `imgHeight` | Number | 图片高度 |
| `autoImgSize` | Boolean | 是否自动调整图片大小 |
| `icon` | String | 图标标识（兼容性属性） |

## 方法

### `getStatus(id)`
获取指定ID项目的状态。

**参数：**
- `id` (String): 项目ID

**返回：**
- (String): 项目状态，可能的值：'ini'（初始）、'loaded'（已加载）、'error'（错误）

### `_afterInsertItems(profile)`
插入项目后的处理函数，主要用于图片加载和错误处理。

**参数：**
- `profile` (Object): 组件配置对象

### `updateItemData(profile, item)`
更新项目数据并刷新显示。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 更新后的项目数据

## 外观样式 (Appearances)

组件提供以下CSS类名用于自定义样式：

| 类名 | 描述 |
|------|------|
| `KEY` | 组件主容器样式 |
| `ITEMS` | 项目容器样式 |
| `ITEM` | 单个项目样式 |
| `ITEMFRAME` | 项目框架样式 |
| `IMAGE` | 图片样式 |
| `CAPTION` | 标题样式 |
| `CONTENT` | 内容区域样式 |
| `COMMENT` | 评论内容样式 |
| `PERSON` | 创建者信息样式 |
| `PERFORTIME` | 时间信息样式 |
| `FLAG` | 标志样式 |
| `EXTRA` | 额外信息样式 |
| `IBWRAP` | 图片包裹器样式 |

### 默认样式值

```css
.ood-uitembg {
    /* 项目背景样式 */
}

.ood-uiborder-radius {
    /* 圆角边框样式 */
}

.ood-showfocus {
    /* 聚焦状态样式 */
}

.ood-ui-ellipsis {
    /* 文本溢出省略号样式 */
}

.ood-icon-loading {
    /* 加载中图标样式 */
}

.ood-load-error {
    /* 加载错误图标样式 */
}

.ood-display-none {
    /* 隐藏元素样式 */
}
```

## 行为交互 (Behaviors)

### `IMAGE` 图片行为
- `onLoad`: 图片加载完成时触发，自动调整图片大小和显示状态
- `onError`: 图片加载失败时触发，显示错误状态

### `FLAG` 标志行为
- `onClick`: 点击标志时触发，可绑定自定义处理函数

## 事件

### `onFlagClick(profile, item, e, src)`
点击标志时触发的事件，需要在组件实例上绑定。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 被点击的项目数据
- `e` (Event): 原始事件对象
- `src` (HTMLElement): 触发事件的DOM元素

## 示例

### 基本意见列表

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/Opinion.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/remixicon@3.5.0/fonts/remixicon.css">
</head>
<body>
    <div id="feedback-list"></div>
    
    <script>
    var feedbackList = ood.UI.Opinion({
        width: '600px',
        columns: 2,
        items: [
            {
                id: 'op1',
                caption: '用户界面改进建议',
                comment: '当前界面布局较为紧凑，建议增加更多的间距和视觉分隔',
                creatorName: '王五',
                department: '用户体验部',
                createDateStr: '2024-03-10',
                imageClass: 'ri-user-voice-line',
                itemClass: 'feedback-item-important'
            },
            {
                id: 'op2',
                caption: '功能扩展需求',
                comment: '希望增加批量处理功能，提高工作效率',
                creatorName: '赵六',
                department: '运营部',
                createDateStr: '2024-03-11',
                imageClass: 'ri-function-line',
                flagText: 'NEW'
            },
            {
                id: 'op3',
                caption: '数据导出优化',
                comment: '当前数据导出格式有限，建议支持更多格式（CSV、Excel、PDF）',
                creatorName: '孙七',
                department: '数据分析部',
                createDateStr: '2024-03-12',
                imageClass: 'ri-file-download-line',
                flagText: 'URGENT'
            }
        ],
        autoItemSize: true,
        autoImgSize: true,
        itemMargin: 8,
        itemPadding: 4
    }).appendTo('#feedback-list');

    // 绑定标志点击事件
    feedbackList.on('onFlagClick', function(profile, item, e, src) {
        alert('点击了项目: ' + item.caption + '\n标志: ' + item.flagText);
    });
    </script>
</body>
</html>
```

### 多列瀑布流布局

```html
<div id="waterfall-opinions"></div>

<style>
.opinion-waterfall {
    column-count: 3;
    column-gap: 20px;
}
.opinion-item {
    break-inside: avoid;
    margin-bottom: 20px;
}
</style>

<script>
var waterfallOpinions = ood.UI.Opinion({
    width: '900px',
    columns: 3,
    items: [
        // 多个意见项目数据...
        {
            id: 'w1',
            caption: '项目进度管理改进',
            comment: '建议增加甘特图视图，更直观展示项目时间线',
            creatorName: '项目经理',
            department: '项目管理部',
            createDateStr: '2024-03-15',
            imageClass: 'ri-calendar-line'
        },
        {
            id: 'w2',
            caption: '团队协作工具集成',
            comment: '建议集成Slack、Teams等协作工具，提高沟通效率',
            creatorName: '技术主管',
            department: '技术研发部',
            createDateStr: '2024-03-16',
            imageClass: 'ri-team-line'
        },
        {
            id: 'w3',
            caption: '移动端适配优化',
            comment: '当前移动端体验有待提升，建议优化触摸交互',
            creatorName: '移动端开发',
            department: '移动开发部',
            createDateStr: '2024-03-17',
            imageClass: 'ri-smartphone-line'
        }
        // ... 更多项目
    ],
    itemClass: 'opinion-item'
}).appendTo('#waterfall-opinions');
</script>
```

### 动态加载意见项目

```html
<div id="dynamic-opinions"></div>

<script>
var dynamicOpinions = ood.UI.Opinion({
    width: '500px',
    columns: 1,
    items: [],
    autoItemSize: true
}).appendTo('#dynamic-opinions');

// 模拟API加载数据
function loadOpinionsFromAPI() {
    // 假设从API获取数据
    var apiData = [
        {
            id: 'api1',
            caption: 'API响应时间优化',
            comment: '部分API接口响应时间超过2秒，建议进行性能优化',
            creatorName: '后端开发',
            department: '后端研发部',
            createDateStr: new Date().toLocaleDateString(),
            imageClass: 'ri-api-line'
        },
        {
            id: 'api2',
            caption: '错误处理机制改进',
            comment: '当前错误信息不够友好，建议提供更详细的错误说明',
            creatorName: '前端开发',
            department: '前端研发部',
            createDateStr: new Date().toLocaleDateString(),
            imageClass: 'ri-bug-line'
        }
    ];
    
    // 更新项目数据
    dynamicOpinions.properties.items = apiData;
    dynamicOpinions.refresh();
}

// 定时加载数据
setInterval(loadOpinionsFromAPI, 30000);

// 初始加载
loadOpinionsFromAPI();
</script>
```

## 注意事项

1. **多列布局**：通过`columns`属性控制列数，适用于瀑布流式意见展示。

2. **图片加载**：组件内置图片加载和错误处理机制，支持自动大小调整。

3. **状态管理**：每个项目有独立的状态标识（初始、已加载、错误），便于跟踪项目状态。

4. **标志系统**：通过`flagText`、`flagClass`、`flagStyle`属性自定义项目标志，用于突出显示重要或紧急意见。

5. **响应式设计**：组件支持响应式布局，可根据容器宽度自动调整列数和项目大小。

6. **性能优化**：对于大量意见项目，建议使用虚拟滚动或分页加载以提高性能。

7. **自定义样式**：通过覆盖Appearances中定义的CSS类名和项目级别的`itemClass`、`itemStyle`属性来自定义外观。

8. **数据绑定**：支持动态更新项目数据，通过修改`items`数组并调用`refresh()`方法更新显示。

9. **事件处理**：提供`onFlagClick`事件处理函数，可绑定自定义业务逻辑。

10. **浏览器兼容性**：基于现代浏览器标准，建议使用Chrome、Firefox、Edge等主流浏览器。