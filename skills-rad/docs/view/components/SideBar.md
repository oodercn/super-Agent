# SideBar 组件手册

## 1. 概述

SideBar 组件是 ooder-a2ui 前端框架中用于页面侧边布局的组件，通常包含导航菜单、功能列表和快捷操作等元素。它提供了灵活的配置方式，可以根据不同需求组合各种子组件，实现复杂的页面侧边导航布局。

## 2. 核心概念

### 2.1 组件类名

SideBar 组件通常通过 `ood.UI.Layout` 组件结合侧边面板配置实现，也可以使用自定义组合组件。

### 2.2 继承关系

```
ood.UI.Layout extends ood.UI
```

### 2.3 主要功能

- 支持侧边导航栏布局
- 可集成菜单、树形结构、工具栏等子组件
- 支持折叠/展开功能
- 支持响应式设计
- 支持主题切换
- 支持嵌套导航结构
- 支持无障碍访问

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 SideBar 组件：

```javascript
// 创建Layout组件作为SideBar容器
var sidebar = ood.create("ood.UI.Layout")
    .setHost(host, "sidebar")
    .setName("sidebar")
    .setWidth("240px")
    .setHeight("100%")
    .setType("vertical")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'auto',
                transparent: true
            },
            {
                id: 'sidebar',
                pos: 'before',
                size: 240,
                locked: false,
                hidden: false,
                cmd: true,
                transparent: true
            }
        ]
    });
```

### 3.2 集成导航菜单

```javascript
// 创建导航菜单
var navMenu = ood.create("ood.UI.MenuBar")
    .setName("sidebarMenu")
    .setItems([
        {
            caption: "首页",
            id: "home",
            imageClass: "ri-home-line"
        },
        {
            caption: "产品管理",
            id: "products",
            imageClass: "ri-box-line",
            sub: [
                {
                    caption: "产品列表",
                    id: "productList"
                },
                {
                    caption: "添加产品",
                    id: "addProduct"
                },
                {
                    caption: "产品分类",
                    id: "productCategories"
                }
            ]
        },
        {
            caption: "订单管理",
            id: "orders",
            imageClass: "ri-file-list-line"
        },
        {
            caption: "用户管理",
            id: "users",
            imageClass: "ri-user-line"
        },
        {
            caption: "系统设置",
            id: "settings",
            imageClass: "ri-settings-line"
        }
    ]);

// 将导航菜单添加到SideBar中
sidebar.addChild(navMenu, "sidebar");
```

### 3.3 完整SideBar示例

```javascript
// 创建完整的SideBar组件
var completeSidebar = ood.create("ood.UI.Layout")
    .setHost(host, "completeSidebar")
    .setName("completeSidebar")
    .setWidth("260px")
    .setHeight("100%")
    .setType("vertical")
    .setProperties({
        items: [
            {
                id: 'sidebar',
                pos: 'before',
                size: 260,
                locked: false,
                cmd: true,
                transparent: true
            },
            {
                id: 'main',
                pos: 'main',
                overflow: 'auto',
                transparent: true
            }
        ]
    });

// 创建SideBar容器
var sidebarContainer = ood.create("ood.UI.Block")
    .setName("sidebarContainer")
    .setWidth("100%")
    .setHeight("100%");

// 添加品牌信息
var brandInfo = ood.create("ood.UI.Block")
    .setName("brandInfo")
    .setHeight("60px")
    .setWidth("100%")
    .setBorder("bottom", "1px solid #e0e0e0");

var brandTitle = ood.create("ood.UI.Label")
    .setName("brandTitle")
    .setText("Ooder-a2ui")
    .setFontSize("20px")
    .setFontWeight("bold")
    .setLeft("20px")
    .setTop("15px")
    .setColor("#333333");
brandInfo.addChild(brandTitle);

sidebarContainer.addChild(brandInfo);

// 创建导航菜单
var navMenu = ood.create("ood.UI.MenuBar")
    .setName("mainNavMenu")
    .setWidth("100%")
    .setHeight("calc(100% - 120px)")
    .setTop("60px")
    .setItems([
        {
            caption: "首页",
            id: "home",
            imageClass: "ri-home-line"
        },
        {
            caption: "产品管理",
            id: "products",
            imageClass: "ri-box-line",
            sub: [
                {
                    caption: "产品列表",
                    id: "productList"
                },
                {
                    caption: "添加产品",
                    id: "addProduct"
                },
                {
                    caption: "产品分类",
                    id: "productCategories"
                }
            ]
        },
        {
            caption: "订单管理",
            id: "orders",
            imageClass: "ri-file-list-line"
        },
        {
            caption: "用户管理",
            id: "users",
            imageClass: "ri-user-line"
        },
        {
            caption: "统计分析",
            id: "analytics",
            imageClass: "ri-bar-chart-line"
        },
        {
            caption: "系统设置",
            id: "settings",
            imageClass: "ri-settings-line"
        }
    ]);

sidebarContainer.addChild(navMenu);

// 添加用户信息
var userInfo = ood.create("ood.UI.Block")
    .setName("userInfo")
    .setHeight("60px")
    .setWidth("100%")
    .setBottom("0px")
    .setBorder("top", "1px solid #e0e0e0");

var userAvatar = ood.create("ood.UI.Avatar")
    .setName("userAvatar")
    .setSize("40px")
    .setText("管理员")
    .setLeft("15px")
    .setTop("10px");
userInfo.addChild(userAvatar);

var userName = ood.create("ood.UI.Label")
    .setName("userName")
    .setText("管理员")
    .setFontSize("14px")
    .setFontWeight("bold")
    .setLeft("65px")
    .setTop("15px")
    .setColor("#333333");
userInfo.addChild(userName);

var userRole = ood.create("ood.UI.Label")
    .setName("userRole")
    .setText("系统管理员")
    .setFontSize("12px")
    .setLeft("65px")
    .setTop("35px")
    .setColor("#666666");
userInfo.addChild(userRole);

sidebarContainer.addChild(userInfo);

// 将SideBar容器添加到布局中
completeSidebar.addChild(sidebarContainer, "sidebar");
```

## 4. 核心属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 组件唯一标识 |
| `type` | String | "vertical" | 布局类型，可选值："horizontal"、"vertical" |
| `width` | String | "240px" | 组件宽度 |
| `height` | String | "100%" | 组件高度 |
| `items` | Array | [] | 布局项目配置 |
| `theme` | String | "light" | 主题，可选值："light"、"dark" |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `transparent` | Boolean | false | 是否透明背景 |

### 4.1 items配置项

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 项目唯一标识 |
| `pos` | String | "before" | 项目位置，可选值："before"（上/左）、"after"（下/右） |
| `size` | Number | 240 | 项目大小（宽度或高度） |
| `locked` | Boolean | false | 是否锁定大小 |
| `hidden` | Boolean | false | 是否隐藏 |
| `cmd` | Boolean | true | 是否显示命令按钮 |
| `folded` | Boolean | false | 是否折叠 |
| `min` | Number | 50 | 最小大小 |
| `max` | Number | 400 | 最大大小 |
| `transparent` | Boolean | false | 是否透明背景 |

## 5. 方法详解

### 5.1 布局管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **addChild** | `addChild(child, subId)` | 向指定子面板添加组件 |
| **removeChild** | `removeChild(child)` | 移除子组件 |
| **updateItem** | `updateItem(subId, options)` | 更新布局项目配置 |
| **fireCmdClickEvent** | `fireCmdClickEvent(subId)` | 触发命令按钮点击事件 |
| **adjustLayout** | `adjustLayout()` | 调整响应式布局 |
| **toggleFold** | `toggleFold(subId)` | 切换侧边栏折叠/展开状态 |
| **fold** | `fold(subId)` | 折叠侧边栏 |
| **expand** | `expand(subId)` | 展开侧边栏 |

### 5.2 主题管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setTheme** | `setTheme(theme)` | 设置组件主题 |
| **getTheme** | `getTheme()` | 获取当前主题 |
| **toggleDarkMode** | `toggleDarkMode()` | 切换暗黑模式 |

### 5.3 事件处理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setOnClickPanel** | `setOnClickPanel(callback)` | 设置面板点击事件回调 |
| **setOnFoldChange** | `setOnFoldChange(callback)` | 设置折叠状态改变事件回调 |

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| `onClickPanel` | 点击面板时触发 |
| `onThemeChange` | 主题改变时触发 |
| `onResize` | 窗口大小改变时触发 |
| `onFoldChange` | 折叠状态改变时触发 |

### 6.2 事件设置

```javascript
// 设置面板点击事件
sidebar.setOnClickPanel(function(profile, item, e, src) {
    console.log("SideBar面板被点击", item);
});

// 监听折叠状态改变事件
sidebar.setOnFoldChange(function(profile, itemId, folded) {
    console.log("SideBar折叠状态改变", itemId, folded);
});

// 监听主题改变事件
sidebar.getRoot().on("themeChange", function(e) {
    console.log("主题已改变为:", e.theme);
});
```

## 7. 响应式设计

SideBar 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
sidebar.setProperties({ responsive: true });

// 自定义响应式调整
sidebar.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('sidebar-mobile');
            // 自动折叠侧边栏或调整布局
            sidebar.fold('sidebar');
        } else {
            root.removeClass('sidebar-mobile');
            // 恢复侧边栏展开状态
            sidebar.expand('sidebar');
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
sidebar.setTheme("dark");

// 获取当前主题
var currentTheme = sidebar.getTheme();

// 切换暗黑模式
sidebar.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA属性

SideBar组件自动添加ARIA属性，提高无障碍访问性：

```html
<div role="navigation" aria-label="侧边导航">
    <!-- SideBar内容 -->
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：在SideBar内部组件间导航
- Enter/Space键：激活当前聚焦的菜单项
- Escape键：关闭打开的子菜单
- Arrow键：在菜单中导航

## 10. 示例代码

### 10.1 基本SideBar

```javascript
var basicSidebar = ood.create("ood.UI.Layout")
    .setHost(host, "basicSidebar")
    .setName("basicSidebar")
    .setWidth("200px")
    .setHeight("100%")
    .setType("vertical")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'auto'
            },
            {
                id: 'sidebar',
                pos: 'before',
                size: 200,
                locked: false,
                cmd: true
            }
        ]
    });

// 创建简单菜单
var simpleMenu = ood.create("ood.UI.MenuBar")
    .setName("simpleMenu")
    .setItems([
        { caption: "首页", id: "home", imageClass: "ri-home-line" },
        { caption: "产品管理", id: "products", imageClass: "ri-box-line" },
        { caption: "订单管理", id: "orders", imageClass: "ri-file-list-line" },
        { caption: "用户管理", id: "users", imageClass: "ri-user-line" },
        { caption: "系统设置", id: "settings", imageClass: "ri-settings-line" }
    ]);

basicSidebar.addChild(simpleMenu, "sidebar");
```

### 10.2 带树形菜单的SideBar

```javascript
var treeSidebar = ood.create("ood.UI.Layout")
    .setHost(host, "treeSidebar")
    .setName("treeSidebar")
    .setWidth("240px")
    .setHeight("100%")
    .setType("vertical")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'auto'
            },
            {
                id: 'sidebar',
                pos: 'before',
                size: 240,
                locked: false,
                cmd: true
            }
        ]
    });

// 创建树形菜单数据
var treeData = [
    {
        id: "1",
        text: "产品管理",
        icon: "ri-box-line",
        children: [
            { id: "1-1", text: "产品列表", icon: "ri-list-line" },
            { id: "1-2", text: "添加产品", icon: "ri-add-circle-line" },
            { id: "1-3", text: "产品分类", icon: "ri-folder-line" }
        ]
    },
    {
        id: "2",
        text: "订单管理",
        icon: "ri-file-list-line",
        children: [
            { id: "2-1", text: "订单列表", icon: "ri-list-line" },
            { id: "2-2", text: "添加订单", icon: "ri-add-circle-line" },
            { id: "2-3", text: "订单统计", icon: "ri-bar-chart-line" }
        ]
    },
    {
        id: "3",
        text: "用户管理",
        icon: "ri-user-line",
        children: [
            { id: "3-1", text: "用户列表", icon: "ri-list-line" },
            { id: "3-2", text: "添加用户", icon: "ri-add-circle-line" },
            { id: "3-3", text: "角色管理", icon: "ri-shield-line" }
        ]
    }
];

// 创建树形菜单
var treeMenu = ood.create("ood.UI.TreeView")
    .setName("treeMenu")
    .setData(treeData)
    .setWidth("100%")
    .setHeight("100%")
    .setOnNodeClick(function(node) {
        console.log("点击了节点:", node.id, node.text);
    });

treeSidebar.addChild(treeMenu, "sidebar");
```

### 10.3 可折叠的SideBar

```javascript
var collapsibleSidebar = ood.create("ood.UI.Layout")
    .setHost(host, "collapsibleSidebar")
    .setName("collapsibleSidebar")
    .setWidth("240px")
    .setHeight("100%")
    .setType("vertical")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'auto'
            },
            {
                id: 'sidebar',
                pos: 'before',
                size: 240,
                locked: false,
                cmd: true,
                folded: false
            }
        ]
    });

// 创建带折叠功能的菜单
var collapsibleMenu = ood.create("ood.UI.MenuBar")
    .setName("collapsibleMenu")
    .setItems([
        { caption: "首页", id: "home", imageClass: "ri-home-line" },
        {
            caption: "产品管理",
            id: "products",
            imageClass: "ri-box-line",
            sub: [
                { caption: "产品列表", id: "productList" },
                { caption: "添加产品", id: "addProduct" }
            ]
        },
        { caption: "订单管理", id: "orders", imageClass: "ri-file-list-line" },
        { caption: "用户管理", id: "users", imageClass: "ri-user-line" }
    ]);

collapsibleSidebar.addChild(collapsibleMenu, "sidebar");

// 添加折叠按钮事件处理
collapsibleSidebar.setOnFoldChange(function(profile, itemId, folded) {
    if (folded) {
        console.log("SideBar已折叠");
        // 可以添加自定义折叠逻辑
    } else {
        console.log("SideBar已展开");
        // 可以添加自定义展开逻辑
    }
});
```

## 11. 最佳实践

### 11.1 布局设计

1. **合理宽度**：SideBar宽度建议在200-300px之间，过宽会影响主内容区域
2. **清晰层次**：合理组织菜单层级，避免过深的嵌套
3. **折叠功能**：建议添加折叠功能，节省屏幕空间
4. **响应式设计**：确保在不同屏幕尺寸下都能良好显示
5. **主题一致性**：保持与整个应用的主题风格一致

### 11.2 菜单设计

1. **图标使用**：为菜单项添加图标，提高可读性
2. **分组管理**：将相关菜单项分组，使用分隔线或标题区分
3. **高亮当前项**：当前激活的菜单项应有明显的视觉标识
4. **快捷键支持**：为常用菜单项添加快捷键
5. **搜索功能**：对于大型应用，添加菜单搜索功能

### 11.3 性能优化

1. **懒加载**：对于大型菜单，考虑使用懒加载技术
2. **减少DOM元素**：避免在SideBar中添加过多不必要的DOM元素
3. **优化事件处理**：避免在SideBar中添加过多复杂的事件监听器
4. **缓存数据**：对于静态菜单，缓存数据以提高加载速度

### 11.4 无障碍访问

1. **添加ARIA属性**：确保SideBar及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作SideBar
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈
4. **语义化HTML**：使用语义化的HTML结构

## 12. 常见问题

### 12.1 布局错位

- **问题**：SideBar组件在某些情况下布局错位
- **解决方案**：
  1. 确保正确设置了width和height属性
  2. 检查items配置中的size和position是否正确
  3. 避免在SideBar中使用固定定位的子组件

### 12.2 折叠/展开不生效

- **问题**：调用fold()或expand()方法后，SideBar没有折叠/展开
- **解决方案**：
  1. 确保正确指定了itemId参数
  2. 检查cmd属性是否设置为true
  3. 确保组件已经渲染到DOM中

### 12.3 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：sidebar.setTheme(sidebar.getTheme());

### 12.4 响应式调整不触发

- **问题**：窗口大小改变时，SideBar没有自动调整
- **解决方案**：
  1. 确保responsive属性设置为true
  2. 检查是否正确实现了adjustLayout方法
  3. 手动添加窗口resize事件监听器

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础SideBar布局功能 |
| v1.1.0 | 支持折叠/展开功能和主题切换 |
| v1.2.0 | 增强响应式设计和无障碍访问支持 |
| v1.3.0 | 优化移动端适配和性能 |

## 14. 总结

SideBar 组件是 ooder-a2ui 框架中用于页面侧边布局的核心组件，通过灵活的配置和组合，可以实现各种复杂的侧边导航设计。它支持折叠/展开功能、响应式布局、主题切换和无障碍访问，能够满足不同场景下的需求。

在实际应用中，SideBar 组件通常与 MenuBar、TreeView、Avatar 等组件结合使用，构建完整的侧边导航系统。通过合理的配置和优化，可以创建出既美观又实用的SideBar设计。