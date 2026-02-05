# OODER 组件定位指南

## 1. 定位属性概述

OODER 框架中，组件定位涉及多个属性，用于控制组件在页面中的位置、大小和层级。以下是核心定位属性的详细说明：

## 2. 核心定位属性

| 属性名 | 类型 | 作用 | 示例 | 适用场景 |
|--------|------|------|------|----------|
| **dock** | 字符串 | 将组件固定到父容器边缘 | `setDock("top")` | 页面框架组件（顶部栏、侧边栏） |
| **position** | 字符串 | 定位方式 | `setPosition("absolute")` | 精确定位的子组件 |
| **top** | 字符串/数值 | 组件顶部位置 | `setTop("0.75em")` | 绝对定位组件 |
| **left** | 字符串/数值 | 组件左侧位置 | `setLeft("10.6em")` | 绝对定位组件 |
| **right** | 字符串/数值 | 组件右侧位置 | `setRight("0em")` | 绝对定位组件 |
| **width** | 字符串/数值 | 组件宽度 | `setWidth("3em")` | 所有组件 |
| **height** | 字符串/数值 | 组件高度 | `setHeight("2.2em")` | 所有组件 |
| **zIndex** | 数值 | 组件层级 | `setZIndex(10)` | 有层级关系的组件 |

## 3. 辅助定位属性

| 属性名 | 作用 | 示例 |
|--------|------|------|
| **visibility** | 控制组件可见性 | `setVisibility("visible")` |
| **borderType** | 设置边框类型 | `setBorderType("none")` |
| **overflow** | 处理内容溢出 | `setOverflow("hidden")` |
| **panelBgClr** | 设置面板背景色 | `setPanelBgClr("#3498DB")` |
| **hAlign** | 设置水平对齐方式 | `setHAlign("center")` |

## 4. 定位模式

### 4.1 固定定位 (dock)

将组件固定到父容器的边缘，自动适应父容器的宽度或高度。

**适用场景**：
- 页面顶部栏
- 侧边栏
- 底部导航栏

**使用示例**：
```javascript
ood.create("ood.UI.Block")
    .setHost(host, "topBar")
    .setName("topBar")
    .setDock("top")
    .setHeight("5em")
    .setZIndex(10)
    .setVisibility("visible")
    .setBorderType("none")
    .setOverflow("hidden")
    .setPanelBgClr("#3498DB")
```

### 4.2 绝对定位 (position: absolute)

相对于父容器精确定位组件，需要配合 `top/left/right` 属性使用。

**适用场景**：
- 父容器内的子组件
- 需要精确定位的元素

**使用示例**：
```javascript
host.topBar.append(ood.create("ood.UI.Icon")
    .setHost(host, "userIcon")
    .setName("userIcon")
    .setLeft("10.6em")
    .setTop("0.75em")
    .setWidth("3em")
    .setHeight("2.2em")
    .setVisibility("visible")
    .setImageClass("spafont spa-icon-login")
    .setIconFontSize("2em")
);
```

### 4.3 相对定位 (position: relative)

相对于组件自身的原始位置定位，用于微调组件位置。

**使用示例**：
```javascript
ood.create("ood.UI.Label")
    .setPosition("relative")
    .setLeft("10px")
    .setTop("5px")
```

## 5. 组件层次结构

### 5.1 嵌套关系

组件采用嵌套结构，父组件的定位会影响子组件的定位：

```
topBar (Block, dock: top)
├── userIcon (Icon, absolute)
├── userConfig (Div, absolute)
│   ├── toggleIcon (Label, static)
│   └── userName (Label, static)
├── logoName (Label, absolute)
├── logo (Image, absolute)
└── galleryMenu (Gallery, absolute)
```

### 5.2 层级管理

- 使用 `zIndex` 属性控制组件层级
- 父组件层级通常高于子组件
- 交互组件（如菜单）层级应较高
- 背景组件层级较低

## 6. 最佳实践

### 6.1 组件初始化顺序

1. 创建父容器组件
2. 设置父容器定位属性（dock, width, height）
3. 创建子组件
4. 设置子组件定位属性（left, top, width, height）
5. 将子组件添加到父容器

### 6.2 单位使用

- **em**：相对单位，适应字体大小变化
- **px**：绝对单位，精确控制
- **%**：相对父容器百分比

### 6.3 定位策略

- 页面框架使用 `dock` 定位
- 子组件使用 `absolute` 定位
- 内部元素使用 `static` 定位

### 6.4 响应式设计

- 使用相对单位（em, %）
- 避免固定像素值
- 考虑不同屏幕尺寸的适配

## 7. 常见问题与解决方案

### 7.1 组件定位不准确

**问题**：组件位置与预期不符

**解决方案**：
- 检查父组件定位属性
- 确认子组件使用的定位方式
- 验证 `left/top` 值的单位

### 7.2 组件层级冲突

**问题**：组件被其他组件遮挡

**解决方案**：
- 调整 `zIndex` 值
- 检查父组件层级
- 重新组织组件嵌套关系

### 7.3 响应式布局问题

**问题**：在不同屏幕尺寸下布局混乱

**解决方案**：
- 使用相对单位
- 考虑使用 `dock` 定位
- 实现响应式调整逻辑

## 8. 示例代码

### 8.1 完整的顶部栏实现

```javascript
// 创建顶部栏容器
append(ood.create("ood.UI.Block")
    .setHost(host, "topBar")
    .setName("topBar")
    .setDock("top")
    .setHeight("5em")
    .setZIndex(10)
    .setVisibility("visible")
    .setBorderType("none")
    .setOverflow("hidden")
    .setPanelBgClr("#3498DB")
);

// 添加用户图标
host.topBar.append(ood.create("ood.UI.Icon")
    .setHost(host, "userIcon")
    .setName("userIcon")
    .setLeft("10.6em")
    .setTop("0.75em")
    .setWidth("3em")
    .setHeight("2.2em")
    .setVisibility("visible")
    .setImageClass("spafont spa-icon-login")
    .setIconFontSize("2em")
);

// 添加用户配置
host.topBar.append(ood.create("ood.UI.Div")
    .setHost(host, "userConfig")
    .setName("userConfig")
    .setHoverPop("dynSvg")
    .setLeft("13.5em")
    .setTop("0.75em")
    .setWidth("8.75em")
    .setHeight("2.5em")
    .setVisibility("visible")
    .setPanelBgClr("transparent")
);

// 添加菜单
host.topBar.append(ood.create("ood.UI.Gallery")
    .setHost(host, "galleryMenu")
    .setName("galleryMenu")
    .setItems([
        {id: "a", caption: "item1", imageClass: "fa fa-lg fa-close"},
        {id: "b", caption: "item2", imageClass: "ood-icon-number1"},
        {id: "c", caption: "item3", imageClass: "fa-solid fa-hashtag"}
    ])
    .setTop("0.68em")
    .setWidth("16em")
    .setHeight("5em")
    .setRight("0em")
    .setVisibility("visible")
    .setSelMode("none")
    .setBorderType("none")
    .setIconOnly(true)
);
```

## 9. 总结

组件定位是 OODER 框架中构建页面布局的核心，正确使用定位属性可以创建出结构清晰、响应式的页面。在实际开发中，应根据组件的功能和位置选择合适的定位方式，并遵循最佳实践，确保页面在不同设备上都能正常显示。

通过本指南，开发者可以了解 OODER 框架中组件定位的基本概念、属性和使用方法，从而更高效地构建 OODER 应用程序。