# Header 组件手册

## 1. 概述

Header 组件是 ooder-a2ui 前端框架中用于页面顶部布局的组件，通常包含页面标题、导航菜单、工具栏和用户信息等元素。它提供了灵活的配置方式，可以根据不同需求组合各种子组件，实现复杂的页面顶部布局。

## 2. 核心概念

### 2.1 组件类名

Header 组件通常通过 `ood.UI.Layout` 组件结合顶部面板配置实现，也可以使用自定义组合组件。

### 2.2 继承关系

```
ood.UI.Layout extends ood.UI
```

### 2.3 主要功能

- 支持顶部导航栏布局
- 可集成菜单、工具栏、用户信息等子组件
- 支持响应式设计
- 支持主题切换
- 支持折叠/展开功能
- 支持无障碍访问

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Header 组件：

```javascript
// 创建Layout组件作为Header容器
var header = ood.create("ood.UI.Layout")
    .setHost(host, "header")
    .setName("header")
    .setWidth("100%")
    .setHeight("60px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden',
                transparent: true
            },
            {
                id: 'top',
                pos: 'before',
                size: 60,
                locked: true,
                hidden: false,
                cmd: false,
                transparent: true
            }
        ]
    });
```

### 3.2 集成MenuBar

```javascript
// 创建MenuBar组件
var menuBar = ood.create("ood.UI.MenuBar")
    .setName("menuBar")
    .setItems([
        {
            caption: "首页",
            id: "home",
            imageClass: "ri-home-line"
        },
        {
            caption: "产品",
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
                }
            ]
        },
        {
            caption: "关于我们",
            id: "about",
            imageClass: "ri-information-line"
        }
    ]);

// 将MenuBar添加到Header中
header.addChild(menuBar, "top");
```

### 3.3 完整Header示例

```javascript
// 创建完整的Header组件
var completeHeader = ood.create("ood.UI.Layout")
    .setHost(host, "completeHeader")
    .setName("completeHeader")
    .setWidth("100%")
    .setHeight("60px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'left',
                pos: 'before',
                size: 200,
                locked: true,
                cmd: false,
                transparent: true
            },
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden',
                transparent: true
            },
            {
                id: 'right',
                pos: 'after',
                size: 200,
                locked: true,
                cmd: false,
                transparent: true
            }
        ]
    });

// 添加Logo
var logo = ood.create("ood.UI.Image")
    .setName("logo")
    .setSrc("/img/logo.png")
    .setWidth("40px")
    .setHeight("40px")
    .setLeft("10px")
    .setTop("10px");
completeHeader.addChild(logo, "left");

// 添加标题
var title = ood.create("ood.UI.Label")
    .setName("title")
    .setText("Ooder-a2ui 管理系统")
    .setFontSize("18px")
    .setFontWeight("bold")
    .setLeft("60px")
    .setTop("15px");
completeHeader.addChild(title, "left");

// 添加导航菜单
var navMenu = ood.create("ood.UI.MenuBar")
    .setName("navMenu")
    .setItems([
        { caption: "首页", id: "home" },
        { caption: "数据管理", id: "data" },
        { caption: "系统设置", id: "settings" }
    ]);
completeHeader.addChild(navMenu, "main");

// 添加用户信息
var userInfo = ood.create("ood.UI.Block")
    .setName("userInfo")
    .setRight("20px")
    .setTop("10px");

var avatar = ood.create("ood.UI.Avatar")
    .setName("userAvatar")
    .setSize("40px")
    .setText("管理员")
    .setRight("0px");
userInfo.addChild(avatar);

completeHeader.addChild(userInfo, "right");
```

## 4. 核心属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 组件唯一标识 |
| `type` | String | "horizontal" | 布局类型，可选值："horizontal"、"vertical" |
| `width` | String | "100%" | 组件宽度 |
| `height` | String | "60px" | 组件高度 |
| `items` | Array | [] | 布局项目配置 |
| `theme` | String | "light" | 主题，可选值："light"、"dark" |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `transparent` | Boolean | false | 是否透明背景 |

### 4.1 items配置项

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `id` | String | 自动生成 | 项目唯一标识 |
| `pos` | String | "before" | 项目位置，可选值："before"（上/左）、"after"（下/右） |
| `size` | Number | 80 | 项目大小（宽度或高度） |
| `locked` | Boolean | false | 是否锁定大小 |
| `hidden` | Boolean | false | 是否隐藏 |
| `cmd` | Boolean | true | 是否显示命令按钮 |
| `folded` | Boolean | false | 是否折叠 |
| `min` | Number | 10 | 最小大小 |
| `max` | Number | null | 最大大小 |
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

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| `onClickPanel` | 点击面板时触发 |
| `onThemeChange` | 主题改变时触发 |
| `onResize` | 窗口大小改变时触发 |

### 6.2 事件设置

```javascript
// 设置面板点击事件
header.setOnClickPanel(function(profile, item, e, src) {
    console.log("Header面板被点击", item);
});

// 监听主题改变事件
header.getRoot().on("themeChange", function(e) {
    console.log("主题已改变为:", e.theme);
});
```

## 7. 响应式设计

Header 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
header.setProperties({ responsive: true });

// 自定义响应式调整
header.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('header-mobile');
            // 隐藏部分元素或调整布局
        } else {
            root.removeClass('header-mobile');
            // 恢复正常布局
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
header.setTheme("dark");

// 获取当前主题
var currentTheme = header.getTheme();

// 切换暗黑模式
header.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA属性

Header组件自动添加ARIA属性，提高无障碍访问性：

```html
<div role="banner" aria-label="页面顶部导航">
    <!-- Header内容 -->
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：在Header内部组件间导航
- Enter/Space键：激活当前聚焦的按钮或菜单项
- Escape键：关闭打开的菜单

## 10. 示例代码

### 10.1 基本Header

```javascript
var basicHeader = ood.create("ood.UI.Layout")
    .setHost(host, "basicHeader")
    .setName("basicHeader")
    .setWidth("100%")
    .setHeight("60px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden'
            },
            {
                id: 'top',
                pos: 'before',
                size: 60,
                locked: true,
                cmd: false
            }
        ]
    });

// 添加标题
var headerTitle = ood.create("ood.UI.Label")
    .setName("headerTitle")
    .setText("基本Header示例")
    .setFontSize("20px")
    .setFontWeight("bold")
    .setLeft("20px")
    .setTop("15px");
basicHeader.addChild(headerTitle, "top");
```

### 10.2 带导航菜单的Header

```javascript
var navHeader = ood.create("ood.UI.Layout")
    .setHost(host, "navHeader")
    .setName("navHeader")
    .setWidth("100%")
    .setHeight("60px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden'
            },
            {
                id: 'top',
                pos: 'before',
                size: 60,
                locked: true,
                cmd: false
            }
        ]
    });

// 创建导航菜单
var navMenu = ood.create("ood.UI.MenuBar")
    .setName("navMenu")
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

navHeader.addChild(navMenu, "top");
```

### 10.3 带工具栏的Header

```javascript
var toolbarHeader = ood.create("ood.UI.Layout")
    .setHost(host, "toolbarHeader")
    .setName("toolbarHeader")
    .setWidth("100%")
    .setHeight("60px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden'
            },
            {
                id: 'top',
                pos: 'before',
                size: 60,
                locked: true,
                cmd: false
            }
        ]
    });

// 创建工具栏
var toolbar = ood.create("ood.UI.ToolBar")
    .setName("headerToolbar")
    .setItems([
        {
            caption: "新建",
            id: "new",
            imageClass: "ri-add-line"
        },
        {
            caption: "保存",
            id: "save",
            imageClass: "ri-save-line"
        },
        {
            caption: "删除",
            id: "delete",
            imageClass: "ri-delete-line"
        },
        {
            caption: "搜索",
            id: "search",
            imageClass: "ri-search-line"
        }
    ]);

toolbarHeader.addChild(toolbar, "top");
```

## 11. 最佳实践

### 11.1 布局设计

1. **保持简洁**：Header 应保持简洁，避免过多元素导致视觉混乱
2. **明确层次**：合理组织标题、导航、工具栏等元素的层次关系
3. **响应式设计**：确保在不同屏幕尺寸下都能良好显示
4. **主题一致性**：保持与整个应用的主题风格一致

### 11.2 性能优化

1. **减少DOM元素**：避免在Header中添加过多不必要的DOM元素
2. **延迟加载**：对于非关键功能，可以考虑延迟加载
3. **优化事件处理**：避免在Header中添加过多复杂的事件监听器

### 11.3 无障碍访问

1. **添加ARIA属性**：确保Header及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作Header
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈

## 12. 常见问题

### 12.1 布局错位

- **问题**：Header组件在某些情况下布局错位
- **解决方案**：
  1. 确保正确设置了width和height属性
  2. 检查items配置中的size和position是否正确
  3. 避免在Header中使用固定定位的子组件

### 12.2 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：header.setTheme(header.getTheme());

### 12.3 响应式调整不触发

- **问题**：窗口大小改变时，Header没有自动调整
- **解决方案**：
  1. 确保responsive属性设置为true
  2. 检查是否正确实现了adjustLayout方法
  3. 手动添加窗口resize事件监听器

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础Header布局功能 |
| v1.1.0 | 支持主题切换和响应式设计 |
| v1.2.0 | 增强无障碍访问支持 |
| v1.3.0 | 优化移动端适配 |

## 14. 总结

Header 组件是 ooder-a2ui 框架中用于页面顶部布局的核心组件，通过灵活的配置和组合，可以实现各种复杂的顶部导航栏设计。它支持响应式布局、主题切换和无障碍访问，能够满足不同场景下的需求。

在实际应用中，Header 组件通常与 MenuBar、ToolBar、Avatar 等组件结合使用，构建完整的页面顶部导航系统。通过合理的配置和优化，可以创建出既美观又实用的Header设计。