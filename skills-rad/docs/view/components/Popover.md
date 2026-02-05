# Popover 组件

## 1. 组件概述
Popover 组件是 ooder-a2ui 前端框架中的一个气泡卡片组件，用于在触发元素周围显示额外的信息或操作，支持多种触发方式和位置，提供丰富的交互体验。

### 1.1 核心功能
- 支持多种触发方式（点击、悬停、聚焦）
- 支持多种位置（上、下、左、右、左上、右上、左下、右下）
- 支持自定义内容
- 支持主题定制
- 支持响应式设计
- 支持无障碍访问
- 支持关闭按钮
- 支持箭头显示控制
- 支持动画效果
- 支持自定义触发元素

### 1.2 适用场景
- 鼠标悬停显示详细信息
- 点击按钮显示操作菜单
- 表单字段的辅助说明
- 数据卡片的额外信息展示
- 复杂操作的快捷入口

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "popover1",
  "type": "Popover",
  "props": {
    "trigger": "click",
    "placement": "top",
    "content": {
      "type": "Panel",
      "props": {
        "title": "气泡内容",
        "padding": 10,
        "children": [
          {
            "type": "Label",
            "props": {
              "text": "这是气泡卡片的内容"
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
        "text": "点击显示气泡"
      }
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const popover = ood.create("Popover", {
  id: "popover1",
  trigger: "click",
  placement: "top",
  content: {
    type: "Panel",
    props: {
      title: "气泡内容",
      padding: 10,
      children: [
        {
          type: "Label",
          props: {
            text: "这是气泡卡片的内容"
          }
        }
      ]
    }
  }
});

// 添加触发元素
const button = ood.create("Button", {
  text: "点击显示气泡"
});

popover.addChild(button);

// 添加到页面
page.addChild(popover);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **trigger** | string | "hover" | 触发方式："hover"、"click"、"focus"、"manual" |
| **placement** | string | "top" | 气泡位置："top"、"bottom"、"left"、"right"、"topLeft"、"topRight"、"bottomLeft"、"bottomRight"、"leftTop"、"leftBottom"、"rightTop"、"rightBottom" |
| **content** | string/object | - | 气泡内容，可以是字符串或组件配置 |
| **title** | string | - | 气泡标题 |
| **visible** | boolean | false | 气泡是否可见（仅在 trigger 为 manual 时生效） |
| **arrow** | boolean | true | 是否显示箭头 |
| **closable** | boolean | false | 是否显示关闭按钮 |
| **mask** | boolean | false | 是否显示遮罩层 |
| **maskClosable** | boolean | true | 点击遮罩层是否可以关闭气泡 |
| **animation** | boolean | true | 是否启用动画效果 |
| **delay** | number | 0 | 触发延迟时间（毫秒） |
| **zIndex** | number | 1050 | 气泡层级 |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |
| **style** | object | {} | 自定义样式 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **show** | `show()` | 显示气泡 |
| **hide** | `hide()` | 隐藏气泡 |
| **toggle** | `toggle()` | 切换气泡显示/隐藏状态 |
| **setContent** | `setContent(content)` | 设置气泡内容 |
| **setTitle** | `setTitle(title)` | 设置气泡标题 |
| **setVisible** | `setVisible(visible)` | 设置气泡是否可见 |
| **isVisible** | `isVisible()` | 获取气泡是否可见 |
| **setPlacement** | `setPlacement(placement)` | 设置气泡位置 |
| **setTrigger** | `setTrigger(trigger)` | 设置触发方式 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onVisibleChange** | 气泡显示/隐藏状态变化时触发 | `visible` - 是否可见 |
| **onShow** | 气泡显示时触发 | - |
| **onHide** | 气泡隐藏时触发 | - |
| **onClick** | 点击气泡内容时触发 | `event` - 点击事件对象 |

## 6. 示例代码

### 6.1 不同触发方式
```json
{
  "id": "popoverTriggerGroup",
  "type": "Stacks",
  "props": {
    "direction": "horizontal",
    "spacing": 20,
    "padding": 20,
    "alignItems": "center"
  },
  "children": [
    {
      "type": "Popover",
      "props": {
        "id": "popoverHover",
        "trigger": "hover",
        "placement": "top",
        "content": "悬停显示的气泡内容"
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
      "type": "Popover",
      "props": {
        "id": "popoverClick",
        "trigger": "click",
        "placement": "top",
        "content": "点击显示的气泡内容"
      },
      "children": [
        {
          "type": "Button",
          "props": {
            "text": "点击触发"
          }
        }
      ]
    },
    {
      "type": "Popover",
      "props": {
        "id": "popoverFocus",
        "trigger": "focus",
        "placement": "top",
        "content": "聚焦显示的气泡内容"
      },
      "children": [
        {
          "type": "TextInput",
          "props": {
            "placeholder": "聚焦显示气泡"
          }
        }
      ]
    }
  ]
}
```

### 6.2 不同位置
```javascript
const popoverPlacementGroup = ood.create("Stacks", {
  id: "popoverPlacementGroup",
  direction: "vertical",
  spacing: 20,
  padding: 20
});

// 创建不同位置的气泡
const placements = ["top", "bottom", "left", "right", "topLeft", "topRight", "bottomLeft", "bottomRight"];

placements.forEach(placement => {
  const popover = ood.create("Popover", {
    trigger: "click",
    placement: placement,
    content: `${placement} 位置的气泡内容`,
    children: [
      ood.create("Button", {
        text: placement
      })
    ]
  });
  
  popoverPlacementGroup.addChild(popover);
});

// 添加到页面
page.addChild(popoverPlacementGroup);
```

### 6.3 自定义内容
```json
{
  "id": "customContentPopover",
  "type": "Popover",
  "props": {
    "trigger": "click",
    "placement": "right",
    "content": {
      "type": "Panel",
      "props": {
        "title": "用户信息",
        "padding": 15,
        "children": [
          {
            "type": "FormLayout",
            "props": {
              "columns": 2,
              "spacing": 10
            },
            "children": [
              {
                "type": "Label",
                "props": {
                  "text": "姓名:"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "张三"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "职位:"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "产品经理"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "部门:"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "产品部"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "邮箱:"
                }
              },
              {
                "type": "Label",
                "props": {
                  "text": "zhangsan@example.com"
                }
              }
            ]
          },
          {
            "type": "Stacks",
            "props": {
              "direction": "horizontal",
              "justifyContent": "flex-end",
              "spacing": 10,
              "marginTop": 10
            },
            "children": [
              {
                "type": "Button",
                "props": {
                  "text": "编辑",
                  "type": "primary",
                  "size": "small"
                }
              },
              {
                "type": "Button",
                "props": {
                  "text": "删除",
                  "type": "danger",
                  "size": "small"
                }
              }
            ]
          }
        ]
      }
    }
  },
  "children": [
    {
      "type": "Button",
      "props": {
        "text": "查看用户信息"
      }
    }
  ]
}
```

### 6.4 手动控制显示/隐藏
```javascript
const manualPopover = ood.create("Popover", {
  id: "manualPopover",
  trigger: "manual",
  placement: "top",
  content: "手动控制显示/隐藏的气泡内容"
});

// 添加触发元素
const showButton = ood.create("Button", {
  text: "显示气泡",
  onClick: () => {
    manualPopover.show();
  }
});

const hideButton = ood.create("Button", {
  text: "隐藏气泡",
  onClick: () => {
    manualPopover.hide();
  }
});

const toggleButton = ood.create("Button", {
  text: "切换气泡",
  onClick: () => {
    manualPopover.toggle();
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

manualPopover.addChild(buttonGroup);

// 添加到页面
page.addChild(manualPopover);
```

## 7. 最佳实践

### 7.1 触发方式选择
- 对于简单的信息展示，使用 `hover` 触发方式
- 对于需要交互的内容，使用 `click` 触发方式
- 对于表单元素，使用 `focus` 触发方式
- 对于需要程序控制的场景，使用 `manual` 触发方式

### 7.2 位置选择
- 根据触发元素的位置和页面布局选择合适的气泡位置
- 避免气泡超出视口范围
- 考虑响应式设计，在小屏幕上自动调整位置

### 7.3 内容设计
- 保持气泡内容的简洁性，避免内容过多
- 对于复杂内容，考虑使用 `Panel` 或其他容器组件进行组织
- 提供清晰的操作按钮
- 考虑添加关闭按钮，方便用户手动关闭

### 7.4 交互设计
- 提供清晰的视觉反馈
- 支持键盘操作，提升可访问性
- 合理设置动画效果，提升用户体验
- 避免在气泡中嵌套过多交互元素

### 7.5 可访问性
- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度
- 为关闭按钮提供清晰的文本描述

## 8. 常见问题与解决方案

### 8.1 气泡不显示
**问题**：设置了触发条件，但气泡不显示
**解决方案**：检查触发方式是否正确，或是否有 CSS 样式冲突，或是否正确添加了触发元素

### 8.2 气泡位置异常
**问题**：气泡位置显示不正确
**解决方案**：检查 placement 属性是否正确，或是否有 CSS 样式影响了位置计算

### 8.3 气泡内容超出视口
**问题**：气泡内容超出视口范围
**解决方案**：调整气泡位置，或简化气泡内容，或设置 maxWidth 属性限制气泡宽度

### 8.4 触发方式不生效
**问题**：设置了 trigger 属性，但触发方式不生效
**解决方案**：检查 trigger 属性值是否正确，或是否有其他事件阻止了触发

### 8.5 气泡关闭不正常
**问题**：气泡无法正常关闭
**解决方案**：检查 maskClosable 属性是否设置正确，或是否有事件冒泡被阻止

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 建议使用 Babel 转译 |

## 10. 相关组件

- **Tooltip**：提示组件，用于显示简短的提示信息
- **Dialog**：对话框组件，用于显示模态内容
- **Menu**：菜单组件，用于显示操作菜单
- **Button**：按钮组件，常用于作为触发元素

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本气泡功能 | 2025-01-15 |
| 1.1.0 | 新增多种触发方式和位置 | 2025-03-20 |
| 1.2.0 | 支持自定义内容和主题 | 2025-05-10 |
| 1.3.0 | 增强无障碍访问支持 | 2025-07-05 |
| 1.4.0 | 支持响应式设计和动画效果 | 2025-09-15 |

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队