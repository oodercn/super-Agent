# Stacks 组件

## 1. 组件概述
Stacks 组件是 ooder-a2ui 前端框架中的一个灵活的布局容器，用于垂直或水平排列子组件，并支持灵活的间距控制和对齐方式。

### 1.1 核心功能
- 支持垂直和水平两种排列方向
- 可配置子组件间距
- 支持灵活的对齐方式（水平和垂直）
- 支持自动换行（水平方向）
- 支持响应式布局
- 可嵌套使用，构建复杂布局

### 1.2 适用场景
- 表单字段的垂直排列
- 按钮组的水平排列
- 卡片列表的网格布局
- 导航菜单的水平/垂直排列
- 复杂仪表盘的布局构建

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "stacks1",
  "type": "Stacks",
  "props": {
    "direction": "vertical",
    "spacing": 10,
    "alignItems": "center",
    "justifyContent": "flex-start"
  },
  "children": [
    {
      "id": "btn1",
      "type": "Button",
      "props": {
        "text": "按钮1"
      }
    },
    {
      "id": "btn2",
      "type": "Button",
      "props": {
        "text": "按钮2"
      }
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const stacks = ood.create("Stacks", {
  id: "stacks1",
  direction: "horizontal",
  spacing: 15,
  wrap: true,
  alignItems: "flex-start",
  justifyContent: "space-between"
});

// 添加子组件
const btn1 = ood.create("Button", { text: "按钮1" });
const btn2 = ood.create("Button", { text: "按钮2" });
stacks.addChild(btn1);
stacks.addChild(btn2);

// 添加到父容器
parentComponent.addChild(stacks);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **direction** | string | "vertical" | 排列方向："vertical"（垂直）或 "horizontal"（水平） |
| **spacing** | number | 8 | 子组件之间的间距（像素） |
| **alignItems** | string | "stretch" | 交叉轴对齐方式："stretch"、"flex-start"、"flex-end"、"center"、"baseline" |
| **justifyContent** | string | "flex-start" | 主轴对齐方式："flex-start"、"flex-end"、"center"、"space-between"、"space-around"、"space-evenly" |
| **wrap** | boolean | false | 水平方向是否自动换行 |
| **gap** | number | 0 | 网格布局中的间隙（像素，优先级高于spacing） |
| **padding** | number | 0 | 内边距（像素） |
| **margin** | number | 0 | 外边距（像素） |
| **width** | number/string | "auto" | 宽度，支持像素值或百分比 |
| **height** | number/string | "auto" | 高度，支持像素值或百分比 |
| **backgroundColor** | string | "transparent" | 背景颜色 |
| **borderRadius** | number | 0 | 圆角半径（像素） |
| **border** | string | "none" | 边框样式，格式："宽度 样式 颜色" |
| **visible** | boolean | true | 是否可见 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **addChild** | `addChild(component)` | 添加子组件 |
| **removeChild** | `removeChild(component)` | 移除指定子组件 |
| **removeAllChildren** | `removeAllChildren()` | 移除所有子组件 |
| **insertChild** | `insertChild(index, component)` | 在指定位置插入子组件 |
| **setDirection** | `setDirection(direction)` | 设置排列方向 |
| **setSpacing** | `setSpacing(spacing)` | 设置子组件间距 |
| **setAlignItems** | `setAlignItems(align)` | 设置交叉轴对齐方式 |
| **setJustifyContent** | `setJustifyContent(justify)` | 设置主轴对齐方式 |
| **setWrap** | `setWrap(wrap)` | 设置是否自动换行 |
| **setVisible** | `setVisible(visible)` | 设置组件可见性 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onClick** | 组件被点击时触发 | `event` - 点击事件对象 |
| **onMouseEnter** | 鼠标进入组件时触发 | `event` - 鼠标事件对象 |
| **onMouseLeave** | 鼠标离开组件时触发 | `event` - 鼠标事件对象 |

## 6. 示例代码

### 6.1 垂直排列的表单字段
```json
{
  "id": "formStacks",
  "type": "Stacks",
  "props": {
    "direction": "vertical",
    "spacing": 12,
    "width": "100%"
  },
  "children": [
    {
      "id": "field1",
      "type": "Stacks",
      "props": {
        "direction": "horizontal",
        "alignItems": "center",
        "spacing": 10
      },
      "children": [
        {
          "id": "label1",
          "type": "Label",
          "props": {
            "text": "姓名：",
            "width": 80
          }
        },
        {
          "id": "input1",
          "type": "Input",
          "props": {
            "width": 200
          }
        }
      ]
    },
    {
      "id": "field2",
      "type": "Stacks",
      "props": {
        "direction": "horizontal",
        "alignItems": "center",
        "spacing": 10
      },
      "children": [
        {
          "id": "label2",
          "type": "Label",
          "props": {
            "text": "年龄：",
            "width": 80
          }
        },
        {
          "id": "input2",
          "type": "Input",
          "props": {
            "width": 200
          }
        }
      ]
    }
  ]
}
```

### 6.2 水平排列的按钮组
```javascript
const buttonGroup = ood.create("Stacks", {
  id: "buttonGroup",
  direction: "horizontal",
  spacing: 8,
  justifyContent: "flex-end",
  padding: 10,
  backgroundColor: "#f5f5f5",
  borderRadius: 4
});

// 添加按钮
const saveBtn = ood.create("Button", { text: "保存", type: "primary" });
const cancelBtn = ood.create("Button", { text: "取消" });
const resetBtn = ood.create("Button", { text: "重置" });

buttonGroup.addChild(saveBtn);
buttonGroup.addChild(cancelBtn);
buttonGroup.addChild(resetBtn);

// 添加到表单
form.addChild(buttonGroup);
```

### 6.3 响应式卡片网格
```json
{
  "id": "cardGrid",
  "type": "Stacks",
  "props": {
    "direction": "horizontal",
    "spacing": 16,
    "wrap": true,
    "justifyContent": "space-between",
    "width": "100%"
  },
  "children": [
    {
      "id": "card1",
      "type": "Card",
      "props": {
        "title": "卡片1",
        "width": 200,
        "height": 150
      }
    },
    {
      "id": "card2",
      "type": "Card",
      "props": {
        "title": "卡片2",
        "width": 200,
        "height": 150
      }
    },
    {
      "id": "card3",
      "type": "Card",
      "props": {
        "title": "卡片3",
        "width": 200,
        "height": 150
      }
    }
  ]
}
```

## 7. 最佳实践

### 7.1 间距设计
- 保持一致的间距风格，建议在主题中定义统一的间距变量
- 垂直排列的表单字段建议使用 10-16px 的间距
- 水平排列的按钮组建议使用 8-12px 的间距
- 卡片网格建议使用 16-24px 的间距

### 7.2 对齐策略
- 垂直方向的表单字段建议左对齐
- 按钮组建议右对齐或居中对齐
- 卡片网格建议使用 space-between 或 space-around 对齐
- 文本内容建议使用 flex-start 对齐

### 7.3 响应式设计
- 在水平方向使用 `wrap: true` 确保在小屏幕上自动换行
- 考虑在不同屏幕尺寸下切换方向（使用媒体查询或响应式属性）
- 为卡片等组件设置最大宽度，确保在大屏幕上不过度拉伸

### 7.4 性能优化
- 避免过深的嵌套层级，建议最多嵌套 3-4 层
- 大量子组件时考虑使用虚拟滚动
- 合理使用 `className` 进行样式定制，减少内联样式

## 8. 常见问题与解决方案

### 8.1 子组件间距不一致
**问题**：设置了 spacing 属性，但子组件间距不一致
**解决方案**：确保所有子组件的 margin 和 padding 为 0，或统一管理

### 8.2 水平排列时溢出容器
**问题**：水平排列的子组件溢出父容器
**解决方案**：设置 `wrap: true` 或调整子组件尺寸

### 8.3 对齐方式不生效
**问题**：设置的 alignItems 或 justifyContent 不生效
**解决方案**：检查父容器是否有固定高度/宽度，或子组件是否设置了固定尺寸

### 8.4 嵌套Stacks性能问题
**问题**：嵌套使用多个Stacks组件导致性能下降
**解决方案**：简化嵌套结构，或使用其他布局组件替代

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他布局方案 |

## 10. 相关组件

- **Layout**：用于复杂页面的整体布局
- **Panel**：带标题和边框的容器组件
- **Block**：带侧边栏的高级容器
- **ContentBlock**：用于展示结构化内容

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本布局功能 | 2025-01-15 |
| 1.1.0 | 新增自动换行功能 | 2025-03-20 |
| 1.2.0 | 增强响应式支持 | 2025-05-10 |
| 1.3.0 | 新增gap属性，优化间距控制 | 2025-07-05 |

## 12. 扩展阅读

- [CSS Flexbox布局指南](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Flexible_Box_Layout/Basic_Concepts_of_Flexbox)
- [ooder-a2ui布局系统详解](FRONTEND_FORM_LAYOUT.md)
- [响应式设计最佳实践](https://web.dev/responsive-design/)

## 13. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队