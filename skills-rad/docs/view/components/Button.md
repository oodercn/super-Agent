# Button 组件

## 1. 组件概述
Button 组件是 ooder-a2ui 前端框架中的一个基础交互组件，用于触发用户操作，支持多种样式和交互方式。

### 1.1 核心功能
- 支持多种按钮类型（主要、次要、危险、链接等）
- 支持多种按钮尺寸（小、中、大）
- 支持图标按钮
- 支持禁用状态
- 支持加载状态
- 支持圆角和方角样式
- 支持主题定制
- 支持响应式设计
- 支持键盘导航
- 支持无障碍访问

### 1.2 适用场景
- 表单提交
- 页面跳转
- 数据操作（新增、编辑、删除等）
- 模态框控制
- 菜单触发
- 状态切换

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "primaryBtn",
  "type": "Button",
  "props": {
    "text": "主要按钮",
    "type": "primary",
    "size": "medium"
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const button = ood.create("Button", {
  id: "primaryBtn",
  text: "主要按钮",
  type: "primary",
  size: "medium",
  onClick: () => {
    console.log("按钮被点击");
  }
});

// 添加到页面
page.addChild(button);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **text** | string | - | 按钮文本内容 |
| **type** | string | "default" | 按钮类型："primary"、"default"、"secondary"、"danger"、"success"、"warning"、"link" |
| **size** | string | "medium" | 按钮尺寸："small"、"medium"、"large" |
| **shape** | string | "rounded" | 按钮形状："rounded"、"circle"、"square" |
| **disabled** | boolean | false | 是否禁用按钮 |
| **loading** | boolean | false | 是否显示加载状态 |
| **icon** | string | - | 按钮图标名称 |
| **iconPosition** | string | "left" | 图标位置："left"、"right" |
| **block** | boolean | false | 是否为块级按钮（占满父容器宽度） |
| **bordered** | boolean | true | 是否显示边框（链接类型按钮默认不显示） |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |
| **style** | object | {} | 自定义样式 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setText** | `setText(text)` | 设置按钮文本 |
| **getText** | `getText()` | 获取按钮文本 |
| **setType** | `setType(type)` | 设置按钮类型 |
| **setSize** | `setSize(size)` | 设置按钮尺寸 |
| **setDisabled** | `setDisabled(disabled)` | 设置按钮是否禁用 |
| **isDisabled** | `isDisabled()` | 获取按钮是否禁用 |
| **setLoading** | `setLoading(loading)` | 设置按钮是否显示加载状态 |
| **isLoading** | `isLoading()` | 获取按钮是否显示加载状态 |
| **setIcon** | `setIcon(icon)` | 设置按钮图标 |
| **setIconPosition** | `setIconPosition(position)` | 设置图标位置 |
| **focus** | `focus()` | 使按钮获得焦点 |
| **blur** | `blur()` | 使按钮失去焦点 |
| **click** | `click()` | 模拟点击按钮 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onClick** | 按钮点击时触发 | `event` - 点击事件对象 |
| **onMouseEnter** | 鼠标进入按钮时触发 | `event` - 鼠标事件对象 |
| **onMouseLeave** | 鼠标离开按钮时触发 | `event` - 鼠标事件对象 |
| **onFocus** | 按钮获得焦点时触发 | `event` - 焦点事件对象 |
| **onBlur** | 按钮失去焦点时触发 | `event` - 焦点事件对象 |
| **onKeyDown** | 按钮获得焦点且按键按下时触发 | `event` - 键盘事件对象 |
| **onKeyUp** | 按钮获得焦点且按键释放时触发 | `event` - 键盘事件对象 |

## 6. 示例代码

### 6.1 不同类型的按钮
```json
{
  "id": "buttonGroup",
  "type": "Stacks",
  "props": {
    "direction": "horizontal",
    "spacing": 10,
    "alignItems": "center"
  },
  "children": [
    {
      "type": "Button",
      "props": {
        "text": "主要按钮",
        "type": "primary"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "默认按钮",
        "type": "default"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "次要按钮",
        "type": "secondary"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "危险按钮",
        "type": "danger"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "成功按钮",
        "type": "success"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "警告按钮",
        "type": "warning"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "链接按钮",
        "type": "link"
      }
    }
  ]
}
```

### 6.2 不同尺寸的按钮
```javascript
const buttonGroup = ood.create("Stacks", {
  direction: "horizontal",
  spacing: 10,
  alignItems: "center"
});

// 小尺寸按钮
const smallBtn = ood.create("Button", {
  text: "小尺寸",
  size: "small",
  type: "primary"
});

// 中尺寸按钮
const mediumBtn = ood.create("Button", {
  text: "中尺寸",
  size: "medium",
  type: "primary"
});

// 大尺寸按钮
const largeBtn = ood.create("Button", {
  text: "大尺寸",
  size: "large",
  type: "primary"
});

// 添加到按钮组
buttonGroup.addChild(smallBtn);
buttonGroup.addChild(mediumBtn);
buttonGroup.addChild(largeBtn);

// 添加到页面
page.addChild(buttonGroup);
```

### 6.3 带图标的按钮
```json
{
  "id": "iconBtnGroup",
  "type": "Stacks",
  "props": {
    "direction": "horizontal",
    "spacing": 10,
    "alignItems": "center"
  },
  "children": [
    {
      "type": "Button",
      "props": {
        "text": "左侧图标",
        "type": "primary",
        "icon": "search",
        "iconPosition": "left"
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "右侧图标",
        "type": "primary",
        "icon": "arrow-right",
        "iconPosition": "right"
      }
    },
    {
      "type": "Button",
      "props": {
        "type": "primary",
        "icon": "search",
        "shape": "circle"
      }
    }
  ]
}
```

### 6.4 禁用和加载状态的按钮
```javascript
const stateBtnGroup = ood.create("Stacks", {
  direction: "horizontal",
  spacing: 10,
  alignItems: "center"
});

// 禁用状态的按钮
const disabledBtn = ood.create("Button", {
  text: "禁用按钮",
  type: "primary",
  disabled: true
});

// 加载状态的按钮
const loadingBtn = ood.create("Button", {
  text: "加载中...",
  type: "primary",
  loading: true
});

// 添加到按钮组
stateBtnGroup.addChild(disabledBtn);
stateBtnGroup.addChild(loadingBtn);

// 添加到页面
page.addChild(stateBtnGroup);
```

### 6.5 块级按钮
```json
{
  "id": "blockBtn",
  "type": "Button",
  "props": {
    "text": "块级按钮",
    "type": "primary",
    "block": true
  }
}
```

## 7. 最佳实践

### 7.1 按钮类型选择
- 主要操作使用 `primary` 类型按钮
- 次要操作使用 `default` 或 `secondary` 类型按钮
- 危险操作（如删除）使用 `danger` 类型按钮
- 成功状态反馈使用 `success` 类型按钮
- 警告信息使用 `warning` 类型按钮
- 链接跳转使用 `link` 类型按钮

### 7.2 按钮尺寸选择
- 表单中使用 `medium` 尺寸按钮
- 工具栏中使用 `small` 尺寸按钮
- 独立操作区使用 `large` 尺寸按钮
- 移动端优先使用 `medium` 或 `large` 尺寸按钮

### 7.3 按钮布局
- 按钮组中使用 `Stacks` 组件进行布局
- 主要按钮放在左侧，次要按钮放在右侧
- 危险按钮单独放置或放在最右侧
- 保持按钮间距一致（建议 8-16px）

### 7.4 交互设计
- 提供清晰的点击反馈（颜色变化、阴影效果）
- 禁用状态下提供明确的视觉提示
- 加载状态下显示加载动画
- 支持键盘回车和空格触发
- 提供适当的焦点样式

### 7.5 可访问性
- 确保按钮有明确的文本描述
- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

## 8. 常见问题与解决方案

### 8.1 按钮点击事件不触发
**问题**：点击按钮后，`onClick` 事件不触发
**解决方案**：检查按钮是否被禁用，或是否被其他元素遮挡，或事件绑定是否正确

### 8.2 按钮样式异常
**问题**：按钮样式显示异常
**解决方案**：检查按钮类型和尺寸是否正确，或是否有自定义样式冲突

### 8.3 图标不显示
**问题**：设置了 `icon` 属性，但图标不显示
**解决方案**：检查图标名称是否正确，或图标资源是否已加载

### 8.4 按钮文字溢出
**问题**：按钮文本过长导致溢出
**解决方案**：缩短按钮文本，或调整按钮尺寸，或设置 `block` 属性为 `true`

### 8.5 加载状态不显示
**问题**：设置了 `loading` 属性为 `true`，但加载状态不显示
**解决方案**：检查按钮类型是否支持加载状态，或是否有自定义样式覆盖

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 建议使用 Babel 转译 |

## 10. 相关组件

- **Dialog**：对话框组件，常用于按钮触发的模态交互
- **Menu**：菜单组件，常用于按钮触发的下拉菜单
- **Popover**：气泡卡片组件，常用于按钮触发的信息展示
- **Tooltip**：提示组件，常用于按钮的悬停提示

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本按钮功能 | 2025-01-15 |
| 1.1.0 | 新增多种按钮类型和尺寸 | 2025-03-20 |
| 1.2.0 | 支持图标按钮和加载状态 | 2025-05-10 |
| 1.3.0 | 支持主题定制和响应式设计 | 2025-07-05 |
| 1.4.0 | 增强无障碍访问支持 | 2025-09-15 |

## 12. 扩展阅读

- [按钮设计最佳实践](https://developer.mozilla.org/zh-CN/docs/Web/Accessibility/ARIA/Attributes/aria-label)
- [无障碍按钮设计指南](https://www.w3.org/WAI/tutorials/forms/controls/)
- [ooder-a2ui 组件库介绍](FRONTEND_COMPONENTS.md)

## 13. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队