# TreeBar

树形导航栏组件，用于显示层次化的树形结构数据，支持折叠/展开、单选/多选、拖放操作、主题切换等功能。TreeView和MTreeView组件都继承自TreeBar。

## 类名
`ood.UI.TreeBar`

## 继承
`["ood.UI", "ood.absList", "ood.absList"]`

## 子类
- `ood.UI.TreeView` - 树形视图组件
- `ood.UI.MTreeView` - 移动端树形视图组件

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/TreeBar.js"></script>

<!-- 创建树形导航栏容器 -->
<div id="treebar-container"></div>

<script>
// 创建树形导航栏组件
var treeBar = ood.UI.TreeBar({
    items: [
        {
            id: 'node1',
            caption: '根节点1',
            sub: [
                {
                    id: 'node12',
                    caption: '子节点1.2',
                    imageClass: "ri-image-line"
                }
            ]
        },
        {
            id: 'node2',
            caption: '根节点2',
            initFold: false
        }
    ],
    theme: 'dark',
    responsive: true,
    initFold: true,
    animCollapse: true,
    dock: 'fill',
    group: false,
    selMode: 'single',
    noCtrlKey: true,
    singleOpen: false,
    dynDestory: false,
    width: 'auto',
    height: 'auto'
}).appendTo('#treebar-container');
</script>
```

## 属性

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'dark'` | 主题：'light', 'dark', 'high-contrast'，支持切换操作 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `expression` | String | `''` | 表达式（保留属性） |
| `enumClass` | Object | `{}` | 枚举类（保留属性） |
| `listKey` | String | `null` | 列表键（保留属性） |
| `isFormField` | Boolean | `false` | 是否表单字段（隐藏属性） |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |
| `autoIconColor` | Boolean | `true` | 是否自动设置图标颜色 |
| `autoItemColor` | Boolean | `false` | 是否自动设置项目颜色 |
| `iconColors` | String | `null` | 图标颜色设置 |
| `itemColors` | String | `null` | 项目颜色设置 |
| `fontColors` | String | `null` | 字体颜色设置 |
| `width` | String | `'auto'` | 组件宽度（带空间单位） |
| `height` | String | `'auto'` | 组件高度（带空间单位） |
| `initFold` | Boolean | `true` | 初始是否折叠 |
| `animCollapse` | Boolean | `true` | 是否启用折叠动画 |
| `dock` | String | `'fill'` | 停靠位置：'top', 'bottom', 'left', 'right', 'fill' |
| `group` | Boolean | `false` | 是否分组模式 |
| `selMode` | String | `'single'` | 选择模式：'single', 'none', 'multi', 'singlecheckbox', 'multibycheckbox' |
| `noCtrlKey` | Boolean | `true` | 是否禁用Ctrl键多选 |
| `singleOpen` | Boolean | `false` | 是否只允许一个节点展开 |
| `dynDestory` | Boolean | `false` | 是否动态销毁折叠的子节点 |
| `position` | String | `'absolute'` | 定位方式 |
| `optBtn` | String | `''` | 选项按钮类名 |
| `togglePlaceholder` | Boolean | `false` | 是否显示切换占位符 |
| `tagCmds` | Array | `[]` | 标签命令数组 |
| `tagCmdsAlign` | String | `'right'` | 标签命令对齐方式：'left', 'right', 'floatright' |

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | 见示例 | 树形结构数据数组 |
| `theme` | String | `'dark'` | 初始主题 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `initFold` | Boolean | `true` | 初始是否折叠 |
| `animCollapse` | Boolean | `true` | 是否启用折叠动画 |
| `dock` | String | `'fill'` | 停靠位置 |
| `group` | Boolean | `false` | 是否分组模式 |
| `selMode` | String | `'single'` | 选择模式 |
| `noCtrlKey` | Boolean | `true` | 是否禁用Ctrl键多选 |
| `singleOpen` | Boolean | `false` | 是否只允许一个节点展开 |
| `dynDestory` | Boolean | `false` | 是否动态销毁折叠的子节点 |
| `optBtn` | String | `''` | 选项按钮类名 |
| `tagCmds` | Array | `[]` | 标签命令数组 |
| `tagCmdsAlign` | String | `'right'` | 标签命令对齐方式 |
| `autoItemColor` | Boolean | `false` | 是否自动设置项目颜色 |
| `autoIconColor` | Boolean | `true` | 是否自动设置图标颜色 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 |

### 项目属性 (每个item可单独设置)

| 属性名 | 类型 | 描述 |
|--------|------|------|
| `id` | String | 节点唯一标识 |
| `caption` | String | 节点显示文本 |
| `sub` | Array | 子节点数组 |
| `imageClass` | String | 图标CSS类名 |
| `initFold` | Boolean | 节点初始是否折叠 |
| `group` | Boolean | 是否为分组节点 |
| `disabled` | Boolean | 是否禁用 |
| `readonly` | Boolean | 是否只读 |
| `hidden` | Boolean | 是否隐藏 |
| `image` | String | 图片URL |
| `icon` | String | 图标类名（与imageClass功能相同） |
| `iconFontCode` | String | 图标字体代码 |
| `tagCmds` | Array | 节点特定标签命令 |

## 方法

### `_setCtrlValue(value, flag)`
设置控件值，用于单选/多选操作。

**参数：**
- `value` (String|Array): 要设置的值
- `flag` (Boolean): 操作标志

**返回：**
- (Object): 组件实例，支持链式调用。

### `insertItems(arr, pid, base, before, toggle)`
插入树节点。

**参数：**
- `arr` (Array): 要插入的节点数组
- `pid` (String): 父节点ID，true表示当前选中节点的父节点
- `base` (String): 基准节点ID，true表示当前选中节点
- `before` (Boolean): 是否插入到基准节点之前
- `toggle` (Boolean): 是否自动展开

**返回：**
- (Object): 组件实例，支持链式调用。

### `_toggleNodes(items, expand, recursive, init)`
切换节点展开/折叠状态。

**参数：**
- `items` (Array): 节点数组
- `expand` (Boolean): true展开，false折叠
- `recursive` (Boolean): 是否递归操作子节点
- `init` (Boolean): 是否初始操作

**返回：**
- (Object): 组件实例，支持链式调用。

### `_removeAllkeys(arr, captionarr, item, profile)`
递归移除所有子节点键。

**参数：**
- `arr` (Array): ID数组
- `captionarr` (Array): 标题数组
- `item` (Object): 当前节点
- `profile` (Object): 组件配置

**返回：**
- (Array): 更新后的数组。

### `setTheme(theme)`
设置树形导航栏主题。

**参数：**
- `theme` (String): 主题名称：'light', 'dark', 'high-contrast'

**返回：**
- (Object): 组件实例，支持链式调用。

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。

**返回：**
- (Object): 组件实例，支持链式调用。

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘导航。

**返回：**
- (Object): 组件实例，支持链式调用。

### `exportData()`
导出树形结构数据。

**返回：**
- (Array): 完整的树形结构数据。

### `importData(data)`
导入树形结构数据。

**参数：**
- `data` (Array): 要导入的树形结构数据

**返回：**
- (Object): 组件实例，支持链式调用。

### `toggleNode(id, expand, recursive, stopanim, callback)`
切换节点的展开/折叠状态。

**参数：**
- `id` (String): 节点ID
- `expand` (Boolean): true展开，false折叠，undefined切换当前状态
- `recursive` (Boolean): 是否递归操作子节点
- `stopanim` (Boolean): 是否停止动画效果
- `callback` (Function): 操作完成后的回调函数

**返回：**
- (Object): 组件实例，支持链式调用。

### `reloadNode(id, expand, recursive, stopanim, callback)`
重新加载节点数据。

**参数：**
- `id` (String): 节点ID
- `expand` (Boolean): 重新加载后是否展开
- `recursive` (Boolean): 是否递归操作子节点
- `stopanim` (Boolean): 是否停止动画效果
- `callback` (Function): 操作完成后的回调函数

**返回：**
- (Object): 组件实例，支持链式调用。

### `disableNode(ids, deep)`
禁用指定节点。

**参数：**
- `ids` (String|Array): 节点ID或ID数组
- `deep` (Boolean): 是否递归禁用于节点

**返回：**
- (Object): 组件实例，支持链式调用。

### `getCallBackValue()`
获取回调值，用于表单提交等场景。

**返回：**
- (Object): 回调值对象，包含字段值和标题值。

### `openToNode(id, triggerEvent)`
展开到指定节点，并可选触发点击事件。

**参数：**
- `id` (String): 目标节点ID
- `triggerEvent` (Boolean): 是否触发点击事件

**返回：**
- (Object): 组件实例，支持链式调用。

## 事件

### `onShowOptions(profile, item, e, src)`
显示选项事件处理器。

### `beforeClick(profile, item, e, src)`
点击前事件处理器。

### `onClick(profile, item, e, src)`
点击事件处理器。

### `afterClick(profile, item, e, src)`
点击后事件处理器。

### `onCmd(profile, item, cmdkey, e, src)`
命令事件处理器。

### `onDblclick(profile, item, e, src)`
双击事件处理器。

### `onGetContent(profile, item, callback)`
获取内容事件处理器，用于动态加载节点内容。

### `onItemSelected(profile, item, e, src, type)`
节点选中事件处理器。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 被选中的节点数据
- `e` (Event): 事件对象
- `src` (Element): 触发事件的DOM元素
- `type` (Number): 选择类型：1选中，-1取消选中

### `beforeFold(profile, item)`
折叠前事件处理器。

### `beforeExpand(profile, item)`
展开前事件处理器。

### `afterFold(profile, item)`
折叠后事件处理器。

### `afterExpand(profile, item)`
展开后事件处理器。

## CSS 变量 (Appearances)

| 选择器 | 描述 | 默认样式 |
|--------|------|----------|
| `KEY` | 键区域 | `border: 0` |
| `EXTRA` | 额外内容区域 | `display: none` |
| `BOX` | 框容器 | `left: 0em; overflow: auto; overflow-x: hidden; position: relative; clear: both; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); transition: all 0.3s ease` |
| `ITEMS` | 项目容器 | `overflow: hidden; transition: all 0.3s ease; padding: .25em` |
| `ITEM` | 单个项目 | `white-space: nowrap; position: relative; line-height: 1.5; overflow: hidden; transition: all 0.2s ease` |
| `BAR` | 节点条 | `zoom: 1 (IE); position: relative; height: 2em; display: block; outline-offset: -1px; background: var(--bg-card, var(--bg-secondary, #ffffff)); border-radius: var(--radius-md, 4px); margin: 1px 0; transition: all var(--ood-transition-fast); cursor: pointer` |
| `BAR:hover` | 悬停节点条 | `background: var(--bg-hover, #f0f0f0); color: var(--text-secondary, #666666); box-shadow: var(--shadow-md, 0 2px 4px rgba(0,0,0,0.1))` |
| `BAR-checked:hover, BAR-active:hover` | 选中/激活节点条悬停 | `background: var(--primary-active, #0078d4); box-shadow: var(--shadow-lg, 0 4px 8px rgba(0,0,0,0.15))` |
| `BAR-disabled` | 禁用节点条 | `opacity: 0.6; cursor: not-allowed; transform: none !important; box-shadow: none !important; filter: grayscale(0.5)` |
| `SUB` | 子节点容器 | `zoom: 1 (IE); height: 0; font-size: 1px (IE6-8); line-height: 1px (IE6-8); position: relative; overflow: hidden; margin-left: 0.75em; transition: height var(--transition-normal)` |
| `MARK` | 标记区域 | `cursor: pointer; vertical-align: middle; color: var(--mark-color, #1890ff)` |
| `BAR-group` | 分组节点条 | `border: none; font-weight: 500; background: var(--group-bg, rgba(0,0,0,0.02))` |
| `ITEMCAPTION` | 节点标题 | `vertical-align: middle; padding: .25em; font-size: 0.95em; transition: color 0.2s ease` |
| `OPT` | 选项按钮 | `position: absolute; left: auto; top: 50%; margin-top: -0.5em; right: .5em; display: none; opacity: 0.7; transition: opacity 0.2s ease` |
| `BAR:hover OPT` | 悬停时的选项按钮 | `display: inline-block; opacity: 1` |
| `LTAGCMDS, RTAGCMDS` | 左右标签命令 | `padding: 0; margin: 0; vertical-align: middle` |
| `ITEMS-tagcmdleft RTAGCMDS` | 左对齐标签命令 | `padding-right: .333em; float: left` |
| `ITEMS-tagcmdfloatright RTAGCMDS` | 右浮动标签命令 | `padding-right: .333em; float: right` |
| `TOGGLE` | 切换按钮 | `padding: 0 .334em 0 0; transition: transform 0.2s ease` |
| `TOGGLE:hover` | 悬停切换按钮 | `transform: scale(1.1)` |
| `ITEMICON` | 节点图标 | `transition: all 0.2s ease; padding: .25em; vertical-align: middle` |
| `treebar-mobile BAR` | 移动端节点条 | `height: 2.2em; font-size: 0.9em; padding: .3em .6em` |
| `treebar-mobile ITEMCAPTION` | 移动端节点标题 | `font-size: 0.9em` |
| `treebar-tiny ITEMICON` | 小屏幕节点图标 | `display: none` |

### 主题类名
| 类名 | 描述 |
|------|------|
| `treebar-dark` | 暗黑主题样式类 |
| `treebar-hc` | 高对比度主题样式类 |
| `treebar-mobile` | 移动端响应式样式类（宽度<768px时自动添加） |
| `treebar-tiny` | 超小屏幕响应式样式类（宽度<480px时自动添加） |

## 示例

### 基本树形导航栏

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/TreeBar.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
</head>
<body>
    <div id="file-tree"></div>
    
    <script>
    // 创建文件树
    var fileTree = ood.UI.TreeBar({
        items: [
            {
                id: 'project',
                caption: '项目文件',
                sub: [
                    {
                        id: 'src',
                        caption: '源代码',
                        sub: [
                            {id: 'main.js', caption: '主程序'},
                            {id: 'utils.js', caption: '工具函数'},
                            {id: 'config.js', caption: '配置文件'}
                        ]
                    },
                    {
                        id: 'docs',
                        caption: '文档',
                        sub: [
                            {id: 'api.md', caption: 'API文档'},
                            {id: 'guide.md', caption: '使用指南'}
                        ]
                    },
                    {id: 'package.json', caption: '项目配置'},
                    {id: 'README.md', caption: '说明文档'}
                ]
            },
            {
                id: 'assets',
                caption: '资源文件',
                sub: [
                    {id: 'images', caption: '图片资源'},
                    {id: 'fonts', caption: '字体文件'},
                    {id: 'styles', caption: '样式文件'}
                ]
            }
        ],
        theme: 'dark',
        responsive: true,
        width: '250px',
        height: '400px'
    }).appendTo('#file-tree');
    
    // 添加点击事件处理
    fileTree.onClick = function(profile, item, e, src) {
        console.log('选中节点:', item.caption, 'ID:', item.id);
        // 可以根据节点类型执行不同操作
        if (item.id.endsWith('.js')) {
            console.log('打开JavaScript文件');
        } else if (item.id.endsWith('.md')) {
            console.log('打开Markdown文档');
        }
    };
    
    // 监听展开/折叠事件
    fileTree.afterExpand = function(profile, item) {
        console.log('节点展开:', item.caption);
    };
    
    fileTree.afterFold = function(profile, item) {
        console.log('节点折叠:', item.caption);
    };
    </script>
</body>
</html>
```

### 带复选框的树形导航栏

```html
<div id="multi-select-tree"></div>

<script>
var multiTree = ood.UI.TreeBar({
    items: [
        {
            id: 'department1',
            caption: '技术部',
            sub: [
                {
                    id: 'team1',
                    caption: '前端组',
                    sub: [
                        {id: 'user1', caption: '张三'},
                        {id: 'user2', caption: '李四'},
                        {id: 'user3', caption: '王五'}
                    ]
                },
                {
                    id: 'team2',
                    caption: '后端组',
                    sub: [
                        {id: 'user4', caption: '赵六'},
                        {id: 'user5', caption: '钱七'}
                    ]
                }
            ]
        },
        {
            id: 'department2',
            caption: '市场部',
            sub: [
                {id: 'user6', caption: '孙八'},
                {id: 'user7', caption: '周九'}
            ]
        }
    ],
    selMode: 'multibycheckbox', // 启用多选复选框模式
    theme: 'light',
    width: '300px',
    height: '350px'
}).appendTo('#multi-select-tree');

// 获取选中的节点
function getSelectedNodes() {
    var value = multiTree.getUIValue();
    console.log('选中的节点ID:', value);
    // 可以根据ID获取对应的节点数据
    return value ? value.split(',') : [];
}

// 设置选中节点
function setSelectedNodes(nodeIds) {
    multiTree.setUIValue(nodeIds.join(','));
}

// 监听选中变化
multiTree.onItemSelected = function(profile, item, e, src, type) {
    console.log('选择变化:', item.caption, type > 0 ? '选中' : '取消选中');
    // 更新UI或其他逻辑
    updateSelectionSummary();
};

function updateSelectionSummary() {
    var selected = getSelectedNodes();
    console.log('当前选中', selected.length, '个节点');
}
</script>
```

### 响应式树形导航栏

```html
<div id="responsive-tree"></div>

<style>
.tree-container {
    width: 100%;
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

@media (max-width: 768px) {
    .tree-container {
        padding: 10px;
    }
}

@media (max-width: 480px) {
    .tree-container {
        padding: 5px;
    }
}
</style>

<script>
var responsiveTree = ood.UI.TreeBar({
    items: [
        {
            id: 'root1',
            caption: '产品目录',
            sub: [
                {
                    id: 'cat1',
                    caption: '电子产品',
                    sub: [
                        {id: 'prod1', caption: '智能手机'},
                        {id: 'prod2', caption: '笔记本电脑'},
                        {id: 'prod3', caption: '平板电脑'}
                    ]
                },
                {
                    id: 'cat2',
                    caption: '家居用品',
                    sub: [
                        {id: 'prod4', caption: '厨具'},
                        {id: 'prod5', caption: '家具'},
                        {id: 'prod6', caption: '装饰品'}
                    ]
                }
            ]
        },
        {
            id: 'root2',
            caption: '服务项目',
            sub: [
                {id: 'serv1', caption: '技术支持'},
                {id: 'serv2', caption: '售后服务'},
                {id: 'serv3', caption: '咨询服务'}
            ]
        }
    ],
    theme: 'light',
    responsive: true,
    animCollapse: true,
    width: '100%',
    height: '450px'
}).appendTo('#responsive-tree');

// 响应式调整
window.addEventListener('resize', function() {
    responsiveTree.adjustLayout();
});

// 初始调整
responsiveTree.adjustLayout();
</script>
```

### 动态加载树节点

```html
<div id="dynamic-tree"></div>

<button onclick="loadChildNodes()">动态加载子节点</button>

<script>
var dynamicTree = ood.UI.TreeBar({
    items: [
        {
            id: 'root',
            caption: '根节点',
            sub: true // 标记为需要动态加载
        }
    ],
    theme: 'dark',
    width: '250px',
    height: '300px'
}).appendTo('#dynamic-tree');

// 动态加载子节点的函数
function loadChildNodes() {
    // 模拟从服务器获取数据
    var mockData = [
        {id: 'child1', caption: '子节点1'},
        {id: 'child2', caption: '子节点2', sub: true}, // 这个节点也可以动态加载
        {id: 'child3', caption: '子节点3'}
    ];
    
    // 插入子节点
    dynamicTree.insertItems(mockData, 'root');
    
    console.log('已动态加载子节点');
}

// 监听节点展开事件，实现懒加载
dynamicTree.beforeExpand = function(profile, item, e, src) {
    if (item.sub === true) { // 需要动态加载
        console.log('准备动态加载节点:', item.caption);
        // 这里可以发起异步请求获取子节点数据
        return false; // 阻止默认展开，等待数据加载
    }
    return true;
};

// 监听获取内容事件
dynamicTree.onGetContent = function(profile, item, callback) {
    console.log('动态获取节点内容:', item.caption);
    
    // 模拟异步加载数据
    setTimeout(function() {
        var childNodes = [
            {id: 'dynamic1', caption: '动态节点1'},
            {id: 'dynamic2', caption: '动态节点2'}
        ];
        callback(childNodes);
    }, 500);
    
    return false; // 返回false表示等待异步回调
};
</script>
```

### 带拖放功能的树形导航栏

```html
<div id="drag-drop-tree"></div>

<div id="drop-area" style="width:300px;height:200px;border:2px dashed #ccc;padding:10px;">
    拖放到这里
</div>

<script>
var dragDropTree = ood.UI.TreeBar({
    items: [
        {
            id: 'folder1',
            caption: '文件夹1',
            sub: [
                {id: 'file1', caption: '文件1'},
                {id: 'file2', caption: '文件2'}
            ]
        },
        {
            id: 'folder2',
            caption: '文件夹2',
            sub: [
                {id: 'file3', caption: '文件3'},
                {id: 'file4', caption: '文件4'}
            ]
        }
    ],
    theme: 'light',
    width: '250px',
    height: '350px'
}).appendTo('#drag-drop-tree');

// 设置拖放目标
var dropArea = ood('#drop-area');

// 监听拖放事件
dropArea.on('dragover', function(e) {
    e.preventDefault();
    dropArea.css('border-color', '#0078d4');
});

dropArea.on('dragleave', function(e) {
    dropArea.css('border-color', '#ccc');
});

dropArea.on('drop', function(e) {
    e.preventDefault();
    dropArea.css('border-color', '#28a745');
    
    // 获取拖放数据
    var data = e.dataTransfer || e.originalEvent.dataTransfer;
    if (data) {
        console.log('拖放数据:', data);
        // 处理拖放逻辑
    }
    
    setTimeout(function() {
        dropArea.css('border-color', '#ccc');
    }, 1000);
});

// 启用拖放功能
dragDropTree.getSubNode('ITEMS', true).attr('draggable', 'true');
</script>
```

## 注意事项

1. **性能优化**：对于大型树形结构，建议使用动态加载（懒加载）功能，避免一次性渲染大量节点影响性能。

2. **选择模式**：根据需求选择合适的`selMode`：
   - `'single'`：单选模式
   - `'multi'`：多选模式（需要Ctrl/Shift键配合）
   - `'singlecheckbox'`：带复选框的单选模式
   - `'multibycheckbox'`：带复选框的多选模式
   - `'none'`：无选择功能

3. **动态加载**：通过`onGetContent`事件可以实现节点的动态加载，适用于数据量大的场景。

4. **拖放支持**：组件内置拖放功能，支持节点在树内或树间拖放。

5. **响应式设计**：组件支持响应式布局，可根据屏幕尺寸自动调整显示效果。

6. **主题系统**：支持light、dark、high-contrast三种主题，可通过`setTheme()`方法切换。

7. **可访问性**：组件增强可访问性支持，自动添加ARIA属性，支持键盘导航。

8. **动画效果**：折叠/展开操作支持动画效果，可通过`animCollapse`属性控制。

9. **动态销毁**：通过`dynDestory`属性可以控制折叠时是否销毁子节点DOM，节省内存。

10. **兼容性**：组件包含对旧版IE的兼容性处理，确保在现代化浏览器和旧版浏览器中都能正常工作。