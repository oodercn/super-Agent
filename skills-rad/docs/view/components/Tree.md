# Tree 组件手册

## 1. 概述

Tree 组件是 ooder-a2ui 前端框架中用于展示层级数据结构的组件，通常用于文件系统导航、分类菜单、组织架构展示等场景。它提供了灵活的配置方式，支持折叠/展开、多选、主题切换和响应式设计等功能。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.TreeView
```

### 2.2 继承关系

```
ood.UI.TreeView extends ood.UI.TreeBar
```

### 2.3 主要功能

- 支持层级数据展示
- 支持折叠/展开功能
- 支持单选和多选模式
- 支持主题切换
- 支持响应式设计
- 支持键盘导航
- 支持无障碍访问
- 支持自定义图标
- 支持拖拽功能

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Tree 组件：

```javascript
// 创建Tree组件
var tree = ood.create("ood.UI.TreeView")
    .setHost(host, "tree")
    .setName("tree")
    .setWidth("240px")
    .setHeight("300px")
    .setItems([
        {
            id: "node1",
            caption: "产品管理",
            imageClass: "ri-box-line",
            sub: [
                { id: "node1-1", caption: "产品列表", imageClass: "ri-list-line" },
                { id: "node1-2", caption: "添加产品", imageClass: "ri-add-circle-line" },
                { id: "node1-3", caption: "产品分类", imageClass: "ri-folder-line" }
            ]
        },
        {
            id: "node2",
            caption: "订单管理",
            imageClass: "ri-file-list-line",
            sub: [
                { id: "node2-1", caption: "订单列表", imageClass: "ri-list-line" },
                { id: "node2-2", caption: "添加订单", imageClass: "ri-add-circle-line" }
            ]
        },
        {
            id: "node3",
            caption: "用户管理",
            imageClass: "ri-user-line",
            sub: [
                { id: "node3-1", caption: "用户列表", imageClass: "ri-list-line" },
                { id: "node3-2", caption: "角色管理", imageClass: "ri-shield-line" }
            ]
        }
    ]);
```

### 3.2 使用JSON配置创建

```json
{
  "id": "tree",
  "type": "TreeView",
  "props": {
    "width": "240px",
    "height": "300px",
    "items": [
      {
        "id": "node1",
        "caption": "产品管理",
        "imageClass": "ri-box-line",
        "sub": [
          { "id": "node1-1", "caption": "产品列表", "imageClass": "ri-list-line" },
          { "id": "node1-2", "caption": "添加产品", "imageClass": "ri-add-circle-line" }
        ]
      },
      {
        "id": "node2",
        "caption": "订单管理",
        "imageClass": "ri-file-list-line"
      }
    ]
  }
}
```

### 3.3 动态加载数据

```javascript
// 创建空Tree组件
var dynamicTree = ood.create("ood.UI.TreeView")
    .setHost(host, "dynamicTree")
    .setName("dynamicTree")
    .setWidth("240px")
    .setHeight("300px")
    .setOnNodeExpand(function(node) {
        // 动态加载子节点数据
        console.log("展开节点:", node.id);
        // 模拟异步加载数据
        setTimeout(function() {
            var childNodes = [
                { id: node.id + "-1", caption: "子节点1", imageClass: "ri-list-line" },
                { id: node.id + "-2", caption: "子节点2", imageClass: "ri-list-line" }
            ];
            dynamicTree.addSubItems(node.id, childNodes);
        }, 500);
    });

// 设置初始数据
dynamicTree.setItems([
    { id: "dynamic1", caption: "动态节点1", expandable: true },
    { id: "dynamic2", caption: "动态节点2", expandable: true }
]);
```

## 4. 核心属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 组件唯一标识 |
| `items` | Array | [] | 树节点数据 |
| `width` | String | "240px" | 组件宽度 |
| `height` | String | "300px" | 组件高度 |
| `selMode` | String | "single" | 选择模式，可选值："single"、"multi" |
| `animCollapse` | Boolean | true | 是否启用折叠/展开动画 |
| `initFold` | Boolean | true | 初始是否折叠 |
| `autoItemColor` | Boolean | false | 是否自动设置项目颜色 |
| `theme` | String | "light" | 主题，可选值："light"、"dark" |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `draggable` | Boolean | false | 是否支持拖拽 |

### 4.1 节点配置项

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 节点唯一标识 |
| `caption` | String | "" | 节点显示文本 |
| `imageClass` | String | "" | 节点图标类名 |
| `sub` | Array | [] | 子节点列表 |
| `initFold` | Boolean | true | 节点初始是否折叠 |
| `expandable` | Boolean | false | 节点是否可展开（用于动态加载） |
| `disabled` | Boolean | false | 节点是否禁用 |
| `checked` | Boolean | false | 节点是否选中 |
| `imageUrl` | String | "" | 节点图标URL |

## 5. 方法详解

### 5.1 数据管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setItems** | `setItems(items)` | 设置树节点数据 |
| **getItems** | `getItems()` | 获取树节点数据 |
| **addItem** | `addItem(item)` | 添加单个节点 |
| **addSubItems** | `addSubItems(parentId, items)` | 为指定节点添加子节点 |
| **removeItem** | `removeItem(nodeId)` | 删除指定节点 |
| **updateItem** | `updateItem(nodeId, properties)` | 更新指定节点属性 |
| **getItem** | `getItem(nodeId)` | 获取指定节点数据 |

### 5.2 节点操作

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **expandNode** | `expandNode(nodeId)` | 展开指定节点 |
| **collapseNode** | `collapseNode(nodeId)` | 折叠指定节点 |
| **toggleNode** | `toggleNode(nodeId)` | 切换节点折叠/展开状态 |
| **expandAll** | `expandAll()` | 展开所有节点 |
| **collapseAll** | `collapseAll()` | 折叠所有节点 |
| **selectNode** | `selectNode(nodeId)` | 选中指定节点 |
| **deselectNode** | `deselectNode(nodeId)` | 取消选中指定节点 |
| **getSelectedNodes** | `getSelectedNodes()` | 获取所有选中节点 |

### 5.3 主题管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setTheme** | `setTheme(theme)` | 设置组件主题 |
| **getTheme** | `getTheme()` | 获取当前主题 |
| **toggleDarkMode** | `toggleDarkMode()` | 切换暗黑模式 |

### 5.4 响应式设计

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **adjustLayout** | `adjustLayout()` | 调整响应式布局 |

### 5.5 无障碍访问

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **enhanceAccessibility** | `enhanceAccessibility()` | 增强无障碍访问支持 |

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| `onNodeClick` | 点击节点时触发 |
| `onNodeExpand` | 展开节点时触发 |
| `onNodeCollapse` | 折叠节点时触发 |
| `onNodeSelect` | 选中节点时触发 |
| `onNodeDeselect` | 取消选中节点时触发 |
| `onThemeChange` | 主题改变时触发 |

### 6.2 事件设置

```javascript
// 监听节点点击事件
tree.setOnNodeClick(function(node) {
    console.log("点击了节点:", node.id, node.caption);
});

// 监听节点展开事件
tree.setOnNodeExpand(function(node) {
    console.log("展开了节点:", node.id);
});

// 监听节点折叠事件
tree.setOnNodeCollapse(function(node) {
    console.log("折叠了节点:", node.id);
});

// 监听节点选中事件
tree.setOnNodeSelect(function(nodes) {
    console.log("选中了节点:", nodes.map(n => n.id));
});

// 监听主题改变事件
tree.getRoot().on("themeChange", function(e) {
    console.log("主题已改变为:", e.theme);
});
```

## 7. 响应式设计

Tree 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
tree.setProperties({ responsive: true });

// 自定义响应式调整
tree.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('tree-mobile');
            // 调整树的宽度为100%
            tree.setWidth("100%");
        } else {
            root.removeClass('tree-mobile');
            // 恢复原始宽度
            tree.setWidth("240px");
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
tree.setTheme("dark");

// 获取当前主题
var currentTheme = tree.getTheme();

// 切换暗黑模式
tree.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA属性

Tree组件自动添加ARIA属性，提高无障碍访问性：

```html
<div role="tree" aria-label="树状视图" aria-multiselectable="false">
    <div role="treeitem" aria-label="节点1" aria-selected="false" aria-disabled="false">
        <div role="button" aria-label="展开" aria-controls="node1" tabindex="0" aria-expanded="false"></div>
        <span role="treeitem" tabindex="0" aria-label="节点1" aria-selected="false"></span>
    </div>
    <!-- 子节点 -->
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：在树节点间导航
- Enter/Space键：激活当前节点（折叠/展开或选中）
- ArrowUp键：导航到上一个节点
- ArrowDown键：导航到下一个节点
- ArrowLeft键：折叠当前节点
- ArrowRight键：展开当前节点

## 10. 示例代码

### 10.1 基本Tree组件

```javascript
var basicTree = ood.create("ood.UI.TreeView")
    .setHost(host, "basicTree")
    .setName("basicTree")
    .setWidth("240px")
    .setHeight("300px")
    .setItems([
        {
            id: "basic1",
            caption: "节点1",
            imageClass: "ri-folder-line",
            sub: [
                { id: "basic1-1", caption: "子节点1-1", imageClass: "ri-file-line" },
                { id: "basic1-2", caption: "子节点1-2", imageClass: "ri-file-line" }
            ]
        },
        {
            id: "basic2",
            caption: "节点2",
            imageClass: "ri-folder-line",
            sub: [
                { id: "basic2-1", caption: "子节点2-1", imageClass: "ri-file-line" }
            ]
        }
    ]);
```

### 10.2 多选Tree组件

```javascript
var multiTree = ood.create("ood.UI.TreeView")
    .setHost(host, "multiTree")
    .setName("multiTree")
    .setWidth("240px")
    .setHeight("300px")
    .setSelMode("multi")
    .setItems([
        {
            id: "multi1",
            caption: "可多选节点1",
            checked: true,
            imageClass: "ri-folder-line",
            sub: [
                { id: "multi1-1", caption: "子节点1-1", checked: true, imageClass: "ri-file-line" },
                { id: "multi1-2", caption: "子节点1-2", imageClass: "ri-file-line" }
            ]
        },
        {
            id: "multi2",
            caption: "可多选节点2",
            imageClass: "ri-folder-line",
            sub: [
                { id: "multi2-1", caption: "子节点2-1", imageClass: "ri-file-line" }
            ]
        }
    ])
    .setOnNodeSelect(function(nodes) {
        console.log("选中的节点:", nodes.map(n => n.id));
    });
```

### 10.3 带拖拽功能的Tree组件

```javascript
var dragTree = ood.create("ood.UI.TreeView")
    .setHost(host, "dragTree")
    .setName("dragTree")
    .setWidth("240px")
    .setHeight("300px")
    .setProperties({ draggable: true })
    .setItems([
        {
            id: "drag1",
            caption: "可拖拽节点1",
            imageClass: "ri-folder-line",
            sub: [
                { id: "drag1-1", caption: "子节点1-1", imageClass: "ri-file-line" }
            ]
        },
        {
            id: "drag2",
            caption: "可拖拽节点2",
            imageClass: "ri-folder-line",
            sub: [
                { id: "drag2-1", caption: "子节点2-1", imageClass: "ri-file-line" }
            ]
        }
    ])
    .setOnNodeDragStart(function(node) {
        console.log("开始拖拽节点:", node.id);
    })
    .setOnNodeDragEnd(function(node, targetNode) {
        console.log("拖拽结束，将节点", node.id, "拖拽到", targetNode.id, "下");
    });
```

## 11. 最佳实践

### 11.1 数据设计

1. **合理层级**：避免过深的层级结构，建议不超过5层
2. **唯一ID**：确保每个节点都有唯一的ID
3. **适当图标**：为不同类型的节点设置合适的图标，提高可读性
4. **动态加载**：对于大量数据，采用动态加载子节点的方式，提高性能

### 11.2 性能优化

1. **懒加载**：对于大型树结构，使用懒加载技术，只加载可见节点
2. **减少DOM元素**：避免在节点中添加过多不必要的DOM元素
3. **优化事件处理**：避免在节点事件中执行复杂操作
4. **缓存数据**：对于静态数据，缓存数据以提高加载速度

### 11.3 交互设计

1. **清晰的视觉反馈**：提供明确的节点展开/折叠状态和选中状态
2. **适当的动画**：使用平滑的折叠/展开动画，提高用户体验
3. **支持键盘导航**：确保可以通过键盘完全操作树组件
4. **合理的默认状态**：根据业务需求设置合适的初始折叠状态

### 11.4 无障碍访问

1. **添加ARIA属性**：确保Tree及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作Tree
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈
4. **语义化HTML**：使用语义化的HTML结构

## 12. 常见问题

### 12.1 节点不显示

- **问题**：设置了节点数据，但节点不显示
- **解决方案**：
  1. 检查items属性是否正确设置
  2. 确保节点配置中包含caption属性
  3. 检查组件是否已经渲染到DOM中

### 12.2 折叠/展开不生效

- **问题**：点击节点的折叠/展开按钮，节点没有反应
- **解决方案**：
  1. 确保animCollapse属性设置为true
  2. 检查节点是否有sub属性（子节点列表）
  3. 检查是否有事件处理函数阻止了默认行为

### 12.3 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：tree.setTheme(tree.getTheme());

### 12.4 拖拽功能不工作

- **问题**：设置了draggable属性为true，但拖拽功能不工作
- **解决方案**：
  1. 确保draggable属性设置为true
  2. 检查是否实现了必要的拖拽事件处理函数
  3. 检查浏览器是否支持HTML5拖拽API

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础TreeView功能 |
| v1.1.0 | 支持多选和折叠/展开动画 |
| v1.2.0 | 支持主题切换和响应式设计 |
| v1.3.0 | 增强无障碍访问支持 |
| v1.4.0 | 支持拖拽功能 |

## 14. 总结

Tree 组件是 ooder-a2ui 框架中用于展示层级数据结构的核心组件，通过灵活的配置和组合，可以实现各种复杂的树状结构展示。它支持折叠/展开、多选、主题切换、响应式设计和无障碍访问，能够满足不同场景下的需求。

在实际应用中，Tree 组件通常用于文件系统导航、分类菜单、组织架构展示等场景。通过合理的配置和优化，可以创建出既美观又实用的树状视图。