# MTreeView

MTreeView移动端树形视图组件，专为移动设备优化的树形结构展示组件，支持折叠/展开、图标显示、多选和触摸交互。

## 类名
`ood.UI.MTreeView`

## 继承
`ood.UI.TreeBar`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MTreeView.js"></script>

<!-- 创建树形视图容器 -->
<div id="treeview-container"></div>

<script>
var treeview = ood.UI.MTreeView({
    width: '20em',
    height: '30em',
    items: [
        {
            id: 'folder1',
            caption: '文档',
            sub: [
                {
                    id: 'file1',
                    caption: '报告.pdf',
                    imageClass: 'ri-file-text-line'
                },
                {
                    id: 'file2',
                    caption: '合同.docx',
                    imageClass: 'ri-file-word-line'
                }
            ]
        },
        {
            id: 'folder2',
            caption: '图片',
            initFold: false,
            sub: [
                {
                    id: 'image1',
                    caption: '风景.jpg',
                    imageClass: 'ri-image-line'
                }
            ]
        }
    ],
    animCollapse: true,
    noIcon: false,
    selMode: 'single'
}).appendTo('#treeview-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `animCollapse` | Boolean | `true` | 是否启用折叠动画效果 |
| `items` | Array | 预定义的树节点数组 | 树节点配置数组，支持多级嵌套 |
| `selMode` | String | `'single'` | 选择模式：'single', 'multi', 'singlecheckbox', 'multibycheckbox', 'none' |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式 |
| `$subMargin` | Number | `1.8` | 子节点缩进边距（em单位） |
| `group` | String | `null` | 分组标识 |
| `noIcon` | Boolean | `true` | 是否隐藏图标，设置为false显示默认文件图标 |
| `selMode` | String | `'single'` | 选择模式，支持单选、多选、复选框等 |
| `togglePlaceholder` | Boolean | `false` | 是否显示折叠占位符 |

### 树节点配置 (items)

每个树节点支持以下属性：

| 属性名 | 类型 | 描述 |
|--------|------|------|
| `id` | String | 节点唯一标识符 |
| `caption` | String | 节点显示文本 |
| `sub` | Array | 子节点数组，用于创建多级树结构 |
| `imageClass` | String | 图标CSS类名，支持Remix Icon等图标库 |
| `image` | String | 图片URL，用于自定义节点图标 |
| `iconFontCode` | String | 字体图标代码 |
| `initFold` | Boolean | 初始是否折叠，默认true（折叠） |
| `disabled` | Boolean | 是否禁用节点 |
| `hidden` | Boolean | 是否隐藏节点 |
| `type` | String | 节点类型：'split'表示分隔线 |
| `showMark` | Boolean | 是否显示标记图标 |
| `fiCheck` | String | 自定义选中图标类名 |

## 方法

### `Initialize()`
组件初始化方法，设置默认模板和样式。

### `_prepareItem(profile, item, oitem, pid, index, len)`
准备树节点数据，设置节点的缩进、图标、样式等属性。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 节点数据对象
- `oitem` (Object): 原始节点对象
- `pid` (String): 父节点ID
- `index` (Number): 节点在父节点中的索引
- `len` (Number): 父节点中子节点总数

### `_tofold(profile, item, pid)`
折叠节点时更新节点样式。

**参数：**
- `profile` (Object): 组件配置对象
- `item` (Object): 节点数据对象
- `pid` (String): 父节点ID

### `_autoColor(item, index, properties)`
自动设置节点颜色（内部方法）。

## 外观样式 (Appearances)

组件提供以下CSS类名用于自定义样式：

| 类名 | 描述 |
|------|------|
| `ITEMS` | 树节点容器样式 |
| `ITEM` | 单个树节点样式 |
| `MARK` | 标记图标样式 |
| `BAR` | 节点背景条样式 |
| `SUB` | 子节点容器样式 |
| `BOX` | 树形视图主容器样式 |
| `IMAGE` | 节点图标样式 |

### 默认样式值

```css
.ood-uitembg {
    /* 节点背景样式 */
}

.ood-uiborder-radius {
    /* 圆角边框 */
}

.ood-showfocus {
    /* 聚焦状态样式 */
}

.ood-uimcmd-check {
    /* 选中图标默认样式 */
}

.ood-uimcmd-toggle {
    /* 折叠/展开切换图标样式 */
}

.ood-icon-file {
    /* 默认文件图标样式 */
}

.ood-icon-file-fold {
    /* 文件夹图标样式 */
}
```

## 行为交互 (Behaviors)

### `MARK` 标记图标行为
- `onClick`: 点击标记图标时触发，切换节点选中状态

### `ITEMICON` 节点图标行为
- `onClick`: 点击节点图标时触发，切换节点折叠/展开状态

## 示例

### 基本树形视图

```html
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="ood/ood.js"></script>
    <script type="text/javascript" src="ood/UI/MTreeView.js"></script>
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/remixicon@3.5.0/fonts/remixicon.css">
</head>
<body>
    <div id="mobile-treeview"></div>
    
    <script>
    var mobileTreeview = ood.UI.MTreeView({
        width: '300px',
        height: '400px',
        items: [
            {
                id: 'root1',
                caption: '我的设备',
                sub: [
                    {
                        id: 'internal',
                        caption: '内部存储',
                        sub: [
                            {id: 'downloads', caption: '下载', imageClass: 'ri-download-line'},
                            {id: 'documents', caption: '文档', imageClass: 'ri-folder-line'},
                            {id: 'pictures', caption: '图片', imageClass: 'ri-image-line'}
                        ]
                    },
                    {
                        id: 'sd-card',
                        caption: 'SD卡',
                        imageClass: 'ri-sd-card-line'
                    }
                ]
            },
            {
                id: 'root2',
                caption: '云存储',
                sub: [
                    {id: 'google-drive', caption: 'Google Drive', imageClass: 'ri-google-drive-line'},
                    {id: 'dropbox', caption: 'Dropbox', imageClass: 'ri-dropbox-line'}
                ]
            }
        ],
        animCollapse: true,
        noIcon: false,
        selMode: 'singlecheckbox',
        togglePlaceholder: true
    }).appendTo('#mobile-treeview');

    // 监听节点选择事件
    mobileTreeview.on('onItemSelected', function(profile, item, src) {
        console.log('节点被选择:', item.id, item.caption);
    });
    </script>
</body>
</html>
```

### 带图标和复选框的树形视图

```html
<div id="checkable-treeview"></div>

<script>
var checkableTreeview = ood.UI.MTreeView({
    width: '250px',
    height: '350px',
    items: [
        {
            id: 'category1',
            caption: '电子产品',
            initFold: false,
            sub: [
                {
                    id: 'laptops',
                    caption: '笔记本电脑',
                    type: 'checkbox',
                    sub: [
                        {id: 'dell', caption: '戴尔', type: 'checkbox'},
                        {id: 'hp', caption: '惠普', type: 'checkbox'},
                        {id: 'lenovo', caption: '联想', type: 'checkbox'}
                    ]
                },
                {
                    id: 'phones',
                    caption: '手机',
                    type: 'checkbox',
                    sub: [
                        {id: 'iphone', caption: 'iPhone', type: 'checkbox'},
                        {id: 'samsung', caption: '三星', type: 'checkbox'},
                        {id: 'xiaomi', caption: '小米', type: 'checkbox'}
                    ]
                }
            ]
        },
        {
            id: 'category2',
            caption: '服装',
            sub: [
                {id: 'shirts', caption: '衬衫', type: 'checkbox'},
                {id: 'pants', caption: '裤子', type: 'checkbox'},
                {id: 'shoes', caption: '鞋子', type: 'checkbox'}
            ]
        }
    ],
    noIcon: false,
    animCollapse: false,
    selMode: 'multibycheckbox'
}).appendTo('#checkable-treeview');

// 获取选中的节点
function getSelectedItems() {
    var selected = [];
    ood.each(checkableTreeview.properties.items, function(item) {
        if (item._checked) selected.push(item.id);
    });
    return selected;
}
</script>
```

### 动态加载树节点

```html
<div id="dynamic-treeview"></div>

<script>
var dynamicTreeview = ood.UI.MTreeView({
    width: '280px',
    height: '320px',
    items: [],
    noIcon: false,
    animCollapse: true
}).appendTo('#dynamic-treeview');

// 动态添加节点
function addTreeNode(parentId, nodeData) {
    var items = dynamicTreeview.properties.items;
    
    function findAndAdd(items, parentId, nodeData) {
        for (var i = 0; i < items.length; i++) {
            if (items[i].id === parentId) {
                if (!items[i].sub) items[i].sub = [];
                items[i].sub.push(nodeData);
                return true;
            }
            if (items[i].sub && findAndAdd(items[i].sub, parentId, nodeData)) {
                return true;
            }
        }
        return false;
    }
    
    if (!parentId) {
        items.push(nodeData);
    } else {
        findAndAdd(items, parentId, nodeData);
    }
    
    dynamicTreeview.refresh();
}

// 初始化根节点
addTreeNode(null, {
    id: 'root',
    caption: '根目录',
    sub: []
});

// 动态添加子节点
setTimeout(function() {
    addTreeNode('root', {
        id: 'folder1',
        caption: '新建文件夹',
        imageClass: 'ri-folder-add-line'
    });
}, 1000);

setTimeout(function() {
    addTreeNode('folder1', {
        id: 'file1',
        caption: '新建文件.txt',
        imageClass: 'ri-file-add-line'
    });
}, 2000);
</script>
```

## 注意事项

1. **移动端优化**：组件专为移动设备设计，支持触摸交互和响应式布局。

2. **图标支持**：
   - 默认使用Remix Icon图标库，确保引入相应CSS
   - 可通过`imageClass`属性指定图标类名
   - 设置`noIcon: false`显示默认文件/文件夹图标

3. **折叠动画**：`animCollapse`属性控制折叠/展开时的动画效果，建议在移动设备上启用以获得更好体验。

4. **选择模式**：支持多种选择模式：
   - `'single'`: 单选（默认）
   - `'multi'`: 多选
   - `'singlecheckbox'`: 带复选框的单选
   - `'multibycheckbox'`: 带复选框的多选
   - `'none'`: 不可选择

5. **节点分隔线**：通过设置`type: 'split'`创建节点分隔线，常用于菜单分组。

6. **样式自定义**：通过覆盖Appearances中定义的CSS类名来自定义组件外观。

7. **性能考虑**：对于大型树结构，建议使用虚拟滚动或分节点加载以保持界面流畅。

8. **可访问性**：组件支持键盘导航和屏幕阅读器，确保为业务场景添加适当的ARIA属性。

9. **浏览器兼容性**：基于现代移动浏览器标准，建议使用Chrome Mobile、Safari Mobile等主流移动浏览器。