# TreeView

TreeView是现代树形视图组件，继承自TreeBar，提供增强的主题切换、响应式设计、可访问性支持和键盘导航功能。适用于文件浏览器、目录结构、导航菜单等场景。

## 类名
`ood.UI.TreeView`

## 继承
`ood.UI.TreeBar` → `ood.UI` → `ood.absList` → `ood.absList`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/TreeView.js"></script>

<!-- 创建树形视图容器 -->
<div id="treeview-container"></div>

<script>
var treeview = ood.UI.TreeView({
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
}).appendTo('#treeview-container');
</script>
```

## 特性

### 1. 主题切换
支持三种主题模式：
- `light`: 亮色主题
- `dark`: 暗色主题（默认）
- `high-contrast`: 高对比度主题，适合无障碍访问

主题设置会自动保存到本地存储，下次访问时自动恢复。

### 2. 响应式设计
根据屏幕尺寸自动调整布局：
- 屏幕宽度 < 768px: 添加 `treeview-mobile` 类
- 屏幕宽度 < 480px: 添加 `treeview-tiny` 类

### 3. 可访问性增强
- 完整的ARIA属性支持（role、aria-label、aria-expanded等）
- 键盘导航支持（箭头键、Enter、Space、Home、End）
- 屏幕阅读器友好

### 4. 键盘导航
支持完整的键盘操作：
- `ArrowDown`: 移动到下一个节点
- `ArrowUp`: 移动到上一个节点
- `ArrowRight`: 展开节点或移动到第一个子节点
- `ArrowLeft`: 折叠节点或移动到父节点
- `Enter`/`Space`: 选择/激活节点
- `Home`: 移动到第一个节点
- `End`: 移动到最后一个节点

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `animCollapse` | Boolean | `true` | 是否启用折叠动画 |
| `items` | Array | 见示例 | 树形结构数据数组 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `theme` | String | `'dark'` | 主题样式 | `'light'`, `'dark'`, `'high-contrast'`，支持自动保存和恢复 |
| `responsive` | Boolean | `true` | 是否启用响应式设计 | - |
| `expression` | String | `''` | 表达式（保留属性） | - |
| `$subMargin` | Number | `1.8` | 子项边距 | 用于计算缩进间距 |
| `group` | Boolean | `null` | 是否分组模式 | - |
| `noIcon` | Boolean | `false` | 是否隐藏图标 | 设置为true时隐藏所有节点图标 |
| `iconColors` | String | `null` | 图标颜色列表 | CSS颜色值，支持逗号分隔 |
| `fontColors` | String | `null` | 字体颜色列表 | CSS颜色值，支持逗号分隔 |
| `itemColors` | String | `null` | 项目颜色列表 | CSS颜色值，支持逗号分隔 |
| `autoFontColor` | Boolean | `false` | 是否自动设置字体颜色 | 根据背景色自动调整字体颜色 |
| `autoIconColor` | Boolean | `false` | 是否自动设置图标颜色 | 根据背景色自动调整图标颜色 |
| `autoItemColor` | Boolean | `false` | 是否自动设置项目颜色 | 根据主题自动调整项目颜色 |
| `tabindex` | Number | `0` | Tab键索引 | 控制键盘Tab键的导航顺序 |

### 继承自TreeBar的属性
TreeView继承了TreeBar的所有属性，包括：
- `selMode`: 选择模式（single、none、multi、singlecheckbox、multibycheckbox）
- `dock`: 停靠位置（top、bottom、left、right、fill）
- `width`/`height`: 组件尺寸
- `initFold`: 初始折叠状态
- `singleOpen`: 是否只允许一个节点展开
- 等更多属性请参考[TreeBar文档](TreeBar.md)

## 实例方法

### `setTheme(theme)`
设置主题样式，并保存到本地存储。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'`, `'dark'`, `'high-contrast'` |

**返回：**
- (Object): 组件实例

**示例：**
```javascript
treeview.setTheme('light');
```

### `getTheme()`
获取当前主题。

**返回：**
- (String): 当前主题名称

**示例：**
```javascript
var currentTheme = treeview.getTheme();
```

### `toggleDarkMode()`
在light和dark主题之间切换。

**返回：**
- (Object): 组件实例

**示例：**
```javascript
treeview.toggleDarkMode();
```

### `TreeViewTrigger()`
初始化触发器，用于设置初始主题和响应式布局。通常在组件初始化后自动调用。

### `adjustLayout()`
根据屏幕尺寸调整响应式布局。当`responsive`属性为true时自动调用。

**返回：**
- (Object): 组件实例

### `enhanceAccessibility()`
增强可访问性支持，添加ARIA属性和键盘事件监听器。

**返回：**
- (Object): 组件实例

## 事件

TreeView继承了TreeBar的所有事件，并增加了现代化的事件处理。可以通过以下方法绑定事件：

```javascript
treeview.onClick(function(profile, item, e, src) {
    console.log('节点被点击:', item.id);
});

treeview.onDblclick(function(profile, item, e, src) {
    console.log('节点被双击:', item.id);
});

treeview.afterUIValueSet(function(profile, oldValue, newValue) {
    console.log('选择值变化:', oldValue, '→', newValue);
});
```

## 数据格式

### 节点数据结构
每个树节点可以包含以下属性：

```javascript
{
    id: 'unique-id',           // 必填，节点唯一标识
    caption: '显示文本',        // 节点显示文本
    imageClass: 'ri-folder-line', // 图标CSS类
    sub: [ /* 子节点数组 */ ], // 子节点数组
    initFold: true,            // 初始是否折叠
    disabled: false,           // 是否禁用
    selected: false,           // 初始是否选中
    editable: true,            // 是否可编辑
    // 其他自定义属性...
}
```

### 示例数据
```javascript
var treeData = {
    items: [
        {
            id: 'root1',
            caption: '文档',
            imageClass: 'ri-folder-line',
            sub: [
                {
                    id: 'doc1',
                    caption: '报告.docx',
                    imageClass: 'ri-file-word-line'
                },
                {
                    id: 'doc2',
                    caption: '演示稿.pptx',
                    imageClass: 'ri-file-ppt-line'
                }
            ]
        },
        {
            id: 'root2',
            caption: '图片',
            imageClass: 'ri-image-line',
            initFold: false,
            sub: [
                {
                    id: 'img1',
                    caption: '风景.jpg',
                    imageClass: 'ri-image-2-line'
                }
            ]
        }
    ]
};
```

## 使用示例

### 示例1：动态切换主题
```javascript
// 创建TreeView实例
var treeview = ood.UI.TreeView({
    items: treeData.items,
    theme: 'light'
}).appendTo('#container');

// 添加主题切换按钮
document.getElementById('theme-toggle').addEventListener('click', function() {
    var currentTheme = treeview.getTheme();
    var newTheme = currentTheme === 'light' ? 'dark' : 'light';
    treeview.setTheme(newTheme);
});
```

### 示例2：响应式布局
```javascript
// 创建响应式TreeView
var treeview = ood.UI.TreeView({
    items: treeData.items,
    responsive: true,
    width: '100%',
    height: '400px'
}).appendTo('#container');

// 监听窗口大小变化
window.addEventListener('resize', function() {
    treeview.adjustLayout();
});
```

### 示例3：增强可访问性
```javascript
// 创建支持完整可访问性的TreeView
var treeview = ood.UI.TreeView({
    items: treeData.items,
    theme: 'high-contrast', // 高对比度主题适合视障用户
    responsive: true
}).appendTo('#container');

// 初始化可访问性增强
treeview.enhanceAccessibility();
```

## CSS主题

TreeView提供三种CSS主题，通过`data-theme`属性应用：

### 暗色主题 (dark)
```css
[data-theme="dark"] .ood-ui-treeview {
    --ood-treeview-bg: #2d3748;
    --ood-treeview-text: #e2e8f0;
    --ood-treeview-border: 1px solid #4a5568;
    --ood-treeview-item-hover: #4a5568;
    --ood-treeview-item-selected: #2c5282;
}
```

### 亮色主题 (light)
```css
[data-theme="light"] .ood-ui-treeview {
    --ood-treeview-bg: #ffffff;
    --ood-treeview-text: #2d3748;
    --ood-treeview-border: 1px solid #e2e8f0;
    --ood-treeview-item-hover: #f7fafc;
    --ood-treeview-item-selected: #ebf8ff;
}
```

### 高对比度主题 (high-contrast)
```css
[data-theme="high-contrast"] .ood-ui-treeview {
    --ood-treeview-bg: #000000;
    --ood-treeview-text: #ffffff;
    --ood-treeview-border: 2px solid #ffffff;
    --ood-treeview-item-hover: #333333;
    --ood-treeview-item-selected: #0066cc;
}
```

## 相关组件

- [TreeBar](TreeBar.md): TreeView的父类，基础树形导航组件
- [MTreeView](MTreeView.md): 移动端优化的树形视图组件
- [List](List.md): 平面列表组件
- [TreeGrid](TreeGrid.md): 树形表格组件，支持更复杂的数据展示

## 最佳实践

1. **主题一致性**: 在整个应用中使用统一的主题设置
2. **响应式设计**: 始终启用`responsive`属性以适应不同设备
3. **可访问性**: 为视障用户考虑启用高对比度主题
4. **键盘导航**: 确保用户可以通过键盘完全操作树形视图
5. **性能优化**: 对于大型树结构，考虑启用`dynDestory`属性以动态销毁折叠的节点

## 注意事项

1. TreeView的主题设置会自动保存到`localStorage`，键名为`treeview-theme`
2. 响应式设计需要监听窗口大小变化事件，建议在应用层面统一处理
3. 键盘导航功能默认启用，无需额外配置
4. ARIA属性在组件初始化时自动添加，无需手动设置