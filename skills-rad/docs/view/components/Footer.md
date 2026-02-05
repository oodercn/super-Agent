# Footer 组件手册

## 1. 概述

Footer 组件是 ooder-a2ui 前端框架中用于页面底部布局的组件，通常包含版权信息、网站链接、联系方式和社交媒体图标等元素。它提供了灵活的配置方式，可以根据不同需求组合各种子组件，实现复杂的页面底部布局。

## 2. 核心概念

### 2.1 组件类名

Footer 组件通常通过 `ood.UI.Layout` 组件结合底部面板配置实现，也可以使用自定义组合组件。

### 2.2 继承关系

```
ood.UI.Layout extends ood.UI
```

### 2.3 主要功能

- 支持底部导航栏布局
- 可集成版权信息、链接、联系方式等子组件
- 支持响应式设计
- 支持主题切换
- 支持折叠/展开功能
- 支持无障碍访问

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Footer 组件：

```javascript
// 创建Layout组件作为Footer容器
var footer = ood.create("ood.UI.Layout")
    .setHost(host, "footer")
    .setName("footer")
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
                id: 'bottom',
                pos: 'after',
                size: 60,
                locked: true,
                hidden: false,
                cmd: false,
                transparent: true
            }
        ]
    });
```

### 3.2 集成版权信息

```javascript
// 创建版权信息
var copyright = ood.create("ood.UI.Label")
    .setName("copyright")
    .setText("© 2026 Ooder-a2ui 框架. 保留所有权利.")
    .setFontSize("14px")
    .setLeft("20px")
    .setTop("20px")
    .setColor("#666666");

// 将版权信息添加到Footer中
footer.addChild(copyright, "bottom");
```

### 3.3 完整Footer示例

```javascript
// 创建完整的Footer组件
var completeFooter = ood.create("ood.UI.Layout")
    .setHost(host, "completeFooter")
    .setName("completeFooter")
    .setWidth("100%")
    .setHeight("120px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'left',
                pos: 'before',
                size: 300,
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
                size: 300,
                locked: true,
                cmd: false,
                transparent: true
            }
        ]
    });

// 添加品牌信息
var brandInfo = ood.create("ood.UI.Block")
    .setName("brandInfo")
    .setLeft("40px")
    .setTop("20px");

var brandTitle = ood.create("ood.UI.Label")
    .setName("brandTitle")
    .setText("Ooder-a2ui")
    .setFontSize("20px")
    .setFontWeight("bold")
    .setColor("#333333");
brandInfo.addChild(brandTitle);

var brandDesc = ood.create("ood.UI.Label")
    .setName("brandDesc")
    .setText("现代化的前端UI框架")
    .setFontSize("14px")
    .setColor("#666666")
    .setTop("30px");
brandInfo.addChild(brandDesc);

completeFooter.addChild(brandInfo, "left");

// 添加导航链接
var navLinks = ood.create("ood.UI.Block")
    .setName("navLinks")
    .setLeft("50%")
    .setTop("20px");

var linkList = ood.create("ood.UI.List")
    .setName("linkList")
    .setOrientation("horizontal")
    .setItems([
        { text: "首页", url: "/" },
        { text: "文档", url: "/docs" },
        { text: "示例", url: "/examples" },
        { text: "API", url: "/api" },
        { text: "关于", url: "/about" }
    ])
    .setItemStyle({ marginRight: "20px", fontSize: "14px" });
navLinks.addChild(linkList);

completeFooter.addChild(navLinks, "main");

// 添加联系方式
var contactInfo = ood.create("ood.UI.Block")
    .setName("contactInfo")
    .setRight("40px")
    .setTop("20px");

var email = ood.create("ood.UI.Label")
    .setName("email")
    .setText("email: contact@ooder-a2ui.com")
    .setFontSize("14px")
    .setColor("#666666");
contactInfo.addChild(email);

var phone = ood.create("ood.UI.Label")
    .setName("phone")
    .setText("phone: +86 123 4567 8901")
    .setFontSize("14px")
    .setColor("#666666")
    .setTop("25px");
contactInfo.addChild(phone);

completeFooter.addChild(contactInfo, "right");

// 添加版权信息
var copyright = ood.create("ood.UI.Label")
    .setName("copyright")
    .setText("© 2026 Ooder-a2ui. All rights reserved.")
    .setFontSize("12px")
    .setColor("#999999")
    .setLeft("50%")
    .setTop("80px");
completeFooter.addChild(copyright, "bottom");
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
| `pos` | String | "after" | 项目位置，可选值："before"（上/左）、"after"（下/右） |
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
footer.setOnClickPanel(function(profile, item, e, src) {
    console.log("Footer面板被点击", item);
});

// 监听主题改变事件
footer.getRoot().on("themeChange", function(e) {
    console.log("主题已改变为:", e.theme);
});
```

## 7. 响应式设计

Footer 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
footer.setProperties({ responsive: true });

// 自定义响应式调整
footer.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('footer-mobile');
            // 隐藏部分元素或调整为垂直布局
        } else {
            root.removeClass('footer-mobile');
            // 恢复正常布局
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
footer.setTheme("dark");

// 获取当前主题
var currentTheme = footer.getTheme();

// 切换暗黑模式
footer.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA属性

Footer组件自动添加ARIA属性，提高无障碍访问性：

```html
<div role="contentinfo" aria-label="页面底部信息">
    <!-- Footer内容 -->
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：在Footer内部链接和按钮间导航
- Enter/Space键：激活当前聚焦的链接或按钮

## 10. 示例代码

### 10.1 基本Footer

```javascript
var basicFooter = ood.create("ood.UI.Layout")
    .setHost(host, "basicFooter")
    .setName("basicFooter")
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
                id: 'bottom',
                pos: 'after',
                size: 60,
                locked: true,
                cmd: false
            }
        ]
    });

// 添加版权信息
var copyright = ood.create("ood.UI.Label")
    .setName("copyright")
    .setText("© 2026 Ooder-a2ui. All rights reserved.")
    .setFontSize("14px")
    .setLeft("20px")
    .setTop("20px")
    .setColor("#666666");
basicFooter.addChild(copyright, "bottom");
```

### 10.2 带链接的Footer

```javascript
var linksFooter = ood.create("ood.UI.Layout")
    .setHost(host, "linksFooter")
    .setName("linksFooter")
    .setWidth("100%")
    .setHeight("80px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden'
            },
            {
                id: 'bottom',
                pos: 'after',
                size: 80,
                locked: true,
                cmd: false
            }
        ]
    });

// 创建链接容器
var linksContainer = ood.create("ood.UI.Block")
    .setName("linksContainer")
    .setLeft("50%")
    .setTop("20px");

// 创建链接列表
var links = [
    { text: "首页", url: "/" },
    { text: "文档", url: "/docs" },
    { text: "示例", url: "/examples" },
    { text: "API", url: "/api" },
    { text: "关于", url: "/about" },
    { text: "联系我们", url: "/contact" }
];

links.forEach(function(link, index) {
    var linkBtn = ood.create("ood.UI.Button")
        .setName("footerLink" + index)
        .setCaption(link.text)
        .setFontSize("14px")
        .setColor("#666666")
        .setLeft(index * 120 + "px")
        .setType("text")
        .setClick(function() {
            window.location.href = link.url;
        });
    linksContainer.addChild(linkBtn);
});

linksFooter.addChild(linksContainer, "bottom");
```

### 10.3 带社交媒体图标的Footer

```javascript
var socialFooter = ood.create("ood.UI.Layout")
    .setHost(host, "socialFooter")
    .setName("socialFooter")
    .setWidth("100%")
    .setHeight("100px")
    .setType("horizontal")
    .setProperties({
        items: [
            {
                id: 'main',
                pos: 'main',
                overflow: 'hidden'
            },
            {
                id: 'bottom',
                pos: 'after',
                size: 100,
                locked: true,
                cmd: false
            }
        ]
    });

// 创建社交媒体图标容器
var socialContainer = ood.create("ood.UI.Block")
    .setName("socialContainer")
    .setLeft("50%")
    .setTop("20px");

// 社交媒体配置
var socialIcons = [
    { name: "github", icon: "ri-github-line", url: "https://github.com" },
    { name: "twitter", icon: "ri-twitter-line", url: "https://twitter.com" },
    { name: "linkedin", icon: "ri-linkedin-line", url: "https://linkedin.com" },
    { name: "facebook", icon: "ri-facebook-line", url: "https://facebook.com" },
    { name: "instagram", icon: "ri-instagram-line", url: "https://instagram.com" }
];

// 创建社交媒体图标按钮
socialIcons.forEach(function(social, index) {
    var socialBtn = ood.create("ood.UI.Button")
        .setName("social" + social.name)
        .setImageClass(social.icon)
        .setFontSize("24px")
        .setWidth("40px")
        .setHeight("40px")
        .setLeft(index * 60 + "px")
        .setType("text")
        .setClick(function() {
            window.open(social.url, "_blank");
        });
    socialContainer.addChild(socialBtn);
});

socialFooter.addChild(socialContainer, "bottom");

// 添加版权信息
var copyright = ood.create("ood.UI.Label")
    .setName("copyright")
    .setText("© 2026 Ooder-a2ui. All rights reserved.")
    .setFontSize("14px")
    .setLeft("50%")
    .setTop("70px")
    .setColor("#666666");
socialFooter.addChild(copyright, "bottom");
```

## 11. 最佳实践

### 11.1 布局设计

1. **保持简洁**：Footer 应保持简洁，避免过多元素导致视觉混乱
2. **明确层次**：合理组织版权信息、链接、社交媒体等元素的层次关系
3. **响应式设计**：确保在不同屏幕尺寸下都能良好显示
4. **主题一致性**：保持与整个应用的主题风格一致

### 11.2 内容组织

1. **版权信息**：始终包含版权声明和年份
2. **重要链接**：只包含最重要的导航链接，避免过多链接
3. **联系方式**：根据需要添加邮箱、电话或地址
4. **社交媒体**：只添加常用的社交媒体图标

### 11.3 性能优化

1. **减少DOM元素**：避免在Footer中添加过多不必要的DOM元素
2. **优化图片**：如果使用图片，确保图片经过优化
3. **延迟加载**：对于非关键功能，可以考虑延迟加载

### 11.4 无障碍访问

1. **添加ARIA属性**：确保Footer及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作Footer
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈

## 12. 常见问题

### 12.1 布局错位

- **问题**：Footer组件在某些情况下布局错位
- **解决方案**：
  1. 确保正确设置了width和height属性
  2. 检查items配置中的size和position是否正确
  3. 避免在Footer中使用固定定位的子组件

### 12.2 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：footer.setTheme(footer.getTheme());

### 12.3 响应式调整不触发

- **问题**：窗口大小改变时，Footer没有自动调整
- **解决方案**：
  1. 确保responsive属性设置为true
  2. 检查是否正确实现了adjustLayout方法
  3. 手动添加窗口resize事件监听器

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础Footer布局功能 |
| v1.1.0 | 支持主题切换和响应式设计 |
| v1.2.0 | 增强无障碍访问支持 |
| v1.3.0 | 优化移动端适配 |

## 14. 总结

Footer 组件是 ooder-a2ui 框架中用于页面底部布局的核心组件，通过灵活的配置和组合，可以实现各种复杂的底部布局设计。它支持响应式布局、主题切换和无障碍访问，能够满足不同场景下的需求。

在实际应用中，Footer 组件通常包含版权信息、网站链接、联系方式和社交媒体图标等元素，为用户提供额外的导航和信息。通过合理的配置和优化，可以创建出既美观又实用的Footer设计。