# Tooltip 组件

## 1. 组件概述
Tooltip 组件是 ooder-a2ui 前端框架中的一个提示组件，用于在用户悬停或聚焦时显示简短的提示信息，帮助用户理解界面元素的功能和含义。

### 1.1 核心功能
- 支持多种触发方式（悬停、聚焦）
- 支持多种位置（上、下、左、右、左上、右上、左下、右下）
- 支持自定义内容
- 支持主题定制
- 支持响应式设计
- 支持无障碍访问
- 支持箭头显示控制
- 支持动画效果
- 支持自定义延迟时间

### 1.2 适用场景
- 表单字段的辅助说明
- 图标按钮的功能提示
- 复杂操作的简短说明
- 数据项的额外信息
- 界面元素的解释说明

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "tooltip1",
  "type": "Tooltip",
  "props": {
    "trigger": "hover",
    "placement": "top",
    "content": "这是提示信息"
  },
  "children": [
    {
      "type": "Button",
      "props": {
        "text": "悬停显示提示"
      }
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const tooltip = ood.create("Tooltip", {
  id: "tooltip1",
  trigger: "hover",
  placement: "top",
  content: "这是提示信息"
});

// 添加触发元素
const button = ood.create("Button", {
  text: "悬停显示提示"
});

tooltip.addChild(button);

// 添加到页面
page.addChild(tooltip);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **trigger** | string | "hover" | 触发方式："hover"、"focus"、"manual" |
| **placement** | string | "top" | 提示位置："top"、"bottom"、"left"、"right"、"topLeft"、"topRight"、"bottomLeft"、"bottomRight"、"leftTop"、"leftBottom"、"rightTop"、"rightBottom" |
| **content** | string/object | - | 提示内容，可以是字符串或组件配置 |
| **visible** | boolean | false | 提示是否可见（仅在 trigger 为 manual 时生效） |
| **arrow** | boolean | true | 是否显示箭头 |
| **animation** | boolean | true | 是否启用动画效果 |
| **delay** | number | 0 | 触发延迟时间（毫秒） |
| **zIndex** | number | 1050 | 提示层级 |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |
| **style** | object | {} | 自定义样式 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **show** | `show()` | 显示提示 |
| **hide** | `hide()` | 隐藏提示 |
| **toggle** | `toggle()` | 切换提示显示/隐藏状态 |
| **setContent** | `setContent(content)` | 设置提示内容 |
| **setVisible** | `setVisible(visible)` | 设置提示是否可见 |
| **isVisible** | `isVisible()` | 获取提示是否可见 |
| **setPlacement** | `setPlacement(placement)` | 设置提示位置 |
| **setTrigger** | `setTrigger(trigger)` | 设置触发方式 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|----------|
| **onVisibleChange** | 提示显示/隐藏状态变化时触发 | `visible` - 是否可见 |
| **onShow** | 提示显示时触发 | - |
| **onHide** | 提示隐藏时触发 | - |

## 6. 示例代码

### 6.1 不同触发方式
```json
{
  "id": "tooltipTriggerGroup",
  "type": "Stacks",
  "props": {
    "direction": "horizontal",
    "spacing": 20,
    "padding": 20,
    "alignItems": "center"
  },
  "children": [
    {
      "type": "Tooltip",
      "props": {
        "id": "tooltipHover",
        "trigger": "hover",
        "placement": "top",
        "content": "悬停显示的提示信息"
      },
      "children": [
        {
          "type": "Button",
          "props": {
            "text": "悬停触发"
          }
        }
      ]
    },
    {
      "type": "Tooltip",
      "props": {
        "id": "tooltipFocus",
        "trigger": "focus",
        "placement": "top",
        "content": "聚焦显示的提示信息"
      },
      "children": [
        {
          "type": "TextInput",
          "props": {
            "placeholder": "聚焦显示提示"
          }
        }
      ]
    }
  ]
}
```

### 6.2 不同位置
```javascript
const tooltipPlacementGroup = ood.create("Stacks", {
  id: "tooltipPlacementGroup",
  direction: "vertical",
  spacing: 20,
  padding: 20
});

// 创建不同位置的提示
const placements = ["top", "bottom", "left", "right", "topLeft", "topRight", "bottomLeft", "bottomRight"];

placements.forEach(placement => {
  const tooltip = ood.create("Tooltip", {
    trigger: "hover",
    placement: placement,
    content: `${placement} 位置的提示信息`,
    children: [
      ood.create("Button", {
        text: placement
      })
    ]
  });
  
  tooltipPlacementGroup.addChild(tooltip);
});

// 添加到页面
page.addChild(tooltipPlacementGroup);
```

### 6.3 自定义内容
```json
{
  "id": "customContentTooltip",
  "type": "Tooltip",
  "props": {
    "trigger": "hover",
    "placement": "right",
    "content": {
      "type": "Panel",
      "props": {
        "padding": 10,
        "children": [
          {
            "type": "Label",
            "props": {
              "text": "自定义提示内容",
              "style": {
                "fontWeight": "bold"
              }
            }
          },
          {
            "type": "Label",
            "props": {
              "text": "这是自定义的提示内容，可以包含多个组件",
              "style": {
                "marginTop": 5
              }
            }
          }
        ]
      }
    }
  },
  "children": [
    {
      "type": "Button",
      "props": {
        "text": "自定义内容提示"
      }
    }
  ]
}
```

### 6.4 手动控制显示/隐藏
```javascript
const manualTooltip = ood.create("Tooltip", {
  id: "manualTooltip",
  trigger: "manual",
  placement: "top",
  content: "手动控制显示/隐藏的提示信息"
});

// 添加触发元素
const showButton = ood.create("Button", {
  text: "显示提示",
  onClick: () => {
    manualTooltip.show();
  }
});

const hideButton = ood.create("Button", {
  text: "隐藏提示",
  onClick: () => {
    manualTooltip.hide();
  }
});

const toggleButton = ood.create("Button", {
  text: "切换提示",
  onClick: () => {
    manualTooltip.toggle();
  }
});

// 组装触发元素
const buttonGroup = ood.create("Stacks", {
  direction: "horizontal",
  spacing: 10,
  alignItems: "center"
});

buttonGroup.addChild(showButton);
buttonGroup.addChild(hideButton);
buttonGroup.addChild(toggleButton);

manualTooltip.addChild(buttonGroup);

// 添加到页面
page.addChild(manualTooltip);
```

## 7. 最佳实践

### 7.1 内容设计
- 保持提示内容的简洁性，避免过长的文本
- 对于复杂内容，考虑使用 Popover 组件
- 提供清晰、准确的提示信息
- 避免在提示中包含过多交互元素

### 7.2 位置选择
- 根据触发元素的位置和页面布局选择合适的提示位置
- 避免提示超出视口范围
- 考虑响应式设计，在小屏幕上自动调整位置

### 7.3 交互设计
- 提供清晰的触发反馈
- 合理设置延迟时间，避免频繁触发
- 支持键盘导航
- 提供适当的焦点样式

### 7.4 可访问性
- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

### 7.5 性能优化
- 避免在提示中包含复杂组件
- 合理设置延迟时间，避免不必要的渲染
- 对于大量元素，考虑使用虚拟滚动或按需渲染

## 8. 常见问题与解决方案

### 8.1 提示不显示
**问题**：设置了触发条件，但提示不显示
**解决方案**：检查触发方式是否正确，或是否有 CSS 样式冲突，或是否正确添加了触发元素

### 8.2 提示位置异常
**问题**：提示位置显示不正确
**解决方案**：检查 placement 属性是否正确，或是否有 CSS 样式影响了位置计算

### 8.3 提示内容超出视口
**问题**：提示内容超出视口范围
**解决方案**：调整提示位置，或简化提示内容，或设置 maxWidth 属性限制提示宽度

### 8.4 触发方式不生效
**问题**：设置了 trigger 属性，但触发方式不生效
**解决方案**：检查 trigger 属性值是否正确，或是否有其他事件阻止了触发

### 8.5 提示闪烁
**问题**：提示频繁闪烁
**解决方案**：调整延迟时间，或检查是否有事件冲突，或避免在提示内容中包含触发元素

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 建议使用 Babel 转译 |

## 10. 相关组件

- **Popover**：气泡卡片组件，用于显示更复杂的内容
- **Dialog**：对话框组件，用于显示模态内容
- **Message**：消息提示组件，用于显示操作结果
- **Button**：按钮组件，常用于作为触发元素

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本提示功能 | 2025-01-15 |
| 1.1.0 | 新增多种触发方式和位置 | 2025-03-20 |
| 1.2.0 | 支持自定义内容和主题 | 2025-05-10 |
| 1.3.0 | 增强无障碍访问支持 | 2025-07-05 |
| 1.4.0 | 支持响应式设计和动画效果 | 2025-09-15 |

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队