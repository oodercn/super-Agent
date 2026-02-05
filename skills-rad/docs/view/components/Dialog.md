# Dialog 组件

## 1. 组件概述
Dialog 组件是 ooder-a2ui 前端框架中的一个模态对话框组件，用于在页面中显示重要的信息或进行交互操作，支持多种样式和交互方式。

### 1.1 核心功能
- 支持多种对话框类型（普通、确认、提示等）
- 支持自定义标题和内容
- 支持自定义按钮
- 支持拖拽功能
- 支持缩放功能
- 支持全屏模式
- 支持模态和非模态两种模式
- 支持键盘操作（ESC 关闭、Enter 确认）
- 支持点击遮罩层关闭
- 支持主题定制
- 支持响应式设计
- 支持无障碍访问

### 1.2 适用场景
- 表单提交前的确认
- 错误信息的提示
- 成功操作的反馈
- 复杂表单的填写
- 数据的查看和编辑
- 系统设置和配置

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "dialog1",
  "type": "Dialog",
  "props": {
    "title": "普通对话框",
    "visible": false,
    "width": 500,
    "height": 300,
    "maskClosable": true,
    "footer": [
      {
        "type": "Button",
        "props": {
          "text": "取消",
          "type": "default",
          "onClick": "dialogCancel"
        }
      },
      {
        "type": "Button",
        "props": {
          "text": "确定",
          "type": "primary",
          "onClick": "dialogConfirm"
        }
      }
    ]
  },
  "children": [
    {
      "type": "Label",
      "props": {
        "text": "这是对话框的内容",
        "padding": 20
      }
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const dialog = ood.create("Dialog", {
  id: "dialog1",
  title: "普通对话框",
  visible: false,
  width: 500,
  height: 300,
  maskClosable: true,
  onOk: () => {
    console.log("对话框确认");
    dialog.close();
  },
  onCancel: () => {
    console.log("对话框取消");
    dialog.close();
  }
});

// 添加内容
const content = ood.create("Label", {
  text: "这是对话框的内容",
  padding: 20
});

dialog.addChild(content);

// 添加到页面
page.addChild(dialog);

// 显示对话框
function showDialog() {
  dialog.open();
}
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **title** | string/object | - | 对话框标题 |
| **visible** | boolean | false | 是否显示对话框 |
| **width** | number/string | 500 | 对话框宽度 |
| **height** | number/string | - | 对话框高度 |
| **minWidth** | number | 300 | 对话框最小宽度 |
| **minHeight** | number | 200 | 对话框最小高度 |
| **maxWidth** | number | - | 对话框最大宽度 |
| **maxHeight** | number | - | 对话框最大高度 |
| **mask** | boolean | true | 是否显示遮罩层 |
| **maskClosable** | boolean | true | 点击遮罩层是否可以关闭对话框 |
| **closable** | boolean | true | 是否显示关闭按钮 |
| **draggable** | boolean | true | 是否支持拖拽 |
| **resizable** | boolean | false | 是否支持缩放 |
| **fullscreen** | boolean | false | 是否全屏显示 |
| **footer** | array/boolean | - | 对话框底部按钮配置，false 表示不显示底部 |
| **bodyStyle** | object | {} | 对话框内容区域样式 |
| **headerStyle** | object | {} | 对话框头部样式 |
| **footerStyle** | object | {} | 对话框底部样式 |
| **maskStyle** | object | {} | 遮罩层样式 |
| **zIndex** | number | 1000 | 对话框层级 |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |
| **style** | object | {} | 自定义样式 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **open** | `open()` | 显示对话框 |
| **close** | `close()` | 关闭对话框 |
| **setTitle** | `setTitle(title)` | 设置对话框标题 |
| **getTitle** | `getTitle()` | 获取对话框标题 |
| **setVisible** | `setVisible(visible)` | 设置对话框是否可见 |
| **isVisible** | `isVisible()` | 获取对话框是否可见 |
| **setWidth** | `setWidth(width)` | 设置对话框宽度 |
| **setHeight** | `setHeight(height)` | 设置对话框高度 |
| **setMaskClosable** | `setMaskClosable(closable)` | 设置点击遮罩层是否可以关闭对话框 |
| **setDraggable** | `setDraggable(draggable)` | 设置是否支持拖拽 |
| **setResizable** | `setResizable(resizable)` | 设置是否支持缩放 |
| **setFullscreen** | `setFullscreen(fullscreen)` | 设置是否全屏显示 |
| **toggleFullscreen** | `toggleFullscreen()` | 切换全屏状态 |
| **addChild** | `addChild(child)` | 向对话框内容区域添加子组件 |
| **removeChild** | `removeChild(child)` | 从对话框内容区域移除子组件 |
| **clearChildren** | `clearChildren()` | 清空对话框内容区域的子组件 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onOk** | 点击确定按钮时触发 | - |
| **onCancel** | 点击取消按钮或关闭按钮或点击遮罩层或按 ESC 键时触发 | - |
| **onOpen** | 对话框打开时触发 | - |
| **onClose** | 对话框关闭时触发 | - |
| **onFullscreenChange** | 全屏状态改变时触发 | `fullscreen` - 是否全屏 |
| **onResize** | 对话框大小改变时触发 | `{ width, height }` - 新的宽度和高度 |
| **onDragStart** | 对话框开始拖拽时触发 | - |
| **onDragEnd** | 对话框拖拽结束时触发 | - |

## 6. 示例代码

### 6.1 普通对话框
```json
{
  "id": "normalDialog",
  "type": "Dialog",
  "props": {
    "title": "普通对话框",
    "visible": false,
    "width": 500,
    "maskClosable": true
  },
  "children": [
    {
      "type": "Label",
      "props": {
        "text": "这是一个普通的对话框，用于显示一些信息或进行简单的交互。",
        "padding": 20,
        "style": {
          "fontSize": 14,
          "lineHeight": 1.5
        }
      }
    }
  ]
}
```

### 6.2 确认对话框
```javascript
const confirmDialog = ood.create("Dialog", {
  id: "confirmDialog",
  title: "确认对话框",
  visible: false,
  width: 400,
  maskClosable: false,
  onOk: () => {
    console.log("确认操作");
    confirmDialog.close();
  },
  onCancel: () => {
    console.log("取消操作");
    confirmDialog.close();
  }
});

// 添加内容
const content = ood.create("Label", {
  text: "您确定要执行此操作吗？此操作不可恢复。",
  padding: 20,
  style: {
    fontSize: 14,
    lineHeight: 1.5
  }
});

confirmDialog.addChild(content);

// 添加到页面
page.addChild(confirmDialog);

// 显示确认对话框
function showConfirmDialog() {
  confirmDialog.open();
}
```

### 6.3 表单对话框
```json
{
  "id": "formDialog",
  "type": "Dialog",
  "props": {
    "title": "表单对话框",
    "visible": false,
    "width": 600,
    "height": 400,
    "maskClosable": false
  },
  "children": [
    {
      "type": "FormLayout",
      "props": {
        "columns": 2,
        "spacing": 20,
        "padding": 20
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "用户名",
            "width": 100
          }
        },
        {
          "type": "TextInput",
          "props": {
            "id": "username",
            "placeholder": "请输入用户名",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "密码",
            "width": 100
          }
        },
        {
          "type": "PasswordInput",
          "props": {
            "id": "password",
            "placeholder": "请输入密码",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "邮箱",
            "width": 100
          }
        },
        {
          "type": "TextInput",
          "props": {
            "id": "email",
            "placeholder": "请输入邮箱",
            "width": 200
          }
        },
        {
          "type": "Label",
          "props": {
            "text": "手机号",
            "width": 100
          }
        },
        {
          "type": "TextInput",
          "props": {
            "id": "phone",
            "placeholder": "请输入手机号",
            "width": 200
          }
        }
      ]
    }
  ]
}
```

### 6.4 自定义底部按钮
```javascript
const customFooterDialog = ood.create("Dialog", {
  id: "customFooterDialog",
  title: "自定义底部按钮",
  visible: false,
  width: 500,
  footer: [
    {
      "type": "Button",
      "props": {
        "text": "忽略",
        "type": "default",
        "onClick": () => {
          console.log("忽略操作");
          customFooterDialog.close();
        }
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "重试",
        "type": "secondary",
        "onClick": () => {
          console.log("重试操作");
        }
      }
    },
    {
      "type": "Button",
      "props": {
        "text": "确认",
        "type": "primary",
        "onClick": () => {
          console.log("确认操作");
          customFooterDialog.close();
        }
      }
    }
  ]
});

// 添加内容
const content = ood.create("Label", {
  text: "这是一个自定义底部按钮的对话框，您可以根据需要添加多个按钮。",
  padding: 20,
  style: {
    fontSize: 14,
    lineHeight: 1.5
  }
});

customFooterDialog.addChild(content);

// 添加到页面
page.addChild(customFooterDialog);
```

### 6.5 拖拽和缩放对话框
```json
{
  "id": "draggableResizableDialog",
  "type": "Dialog",
  "props": {
    "title": "拖拽和缩放对话框",
    "visible": false,
    "width": 500,
    "height": 300,
    "draggable": true,
    "resizable": true,
    "minWidth": 400,
    "minHeight": 200,
    "maxWidth": 800,
    "maxHeight": 600
  },
  "children": [
    {
      "type": "Label",
      "props": {
        "text": "这个对话框支持拖拽和缩放，您可以拖动标题栏移动对话框，拖动右下角调整对话框大小。",
        "padding": 20,
        "style": {
          "fontSize": 14,
          "lineHeight": 1.5
        }
      }
    }
  ]
}
```

## 7. 最佳实践

### 7.1 对话框设计
- 保持对话框标题的简洁性，避免过长的标题文本
- 根据内容合理设置对话框的大小
- 对于复杂的内容，考虑使用可滚动的内容区域
- 对于重要的操作，使用确认对话框
- 对于成功或失败的反馈，使用提示对话框

### 7.2 交互设计
- 提供清晰的关闭方式（关闭按钮、遮罩层点击、ESC 键）
- 为对话框提供合理的默认位置（居中显示）
- 对于长时间操作，提供加载状态提示
- 支持键盘操作，提升可访问性
- 提供明确的按钮文本，避免歧义

### 7.3 性能优化
- 避免在对话框中放置过多复杂组件
- 对于大型对话框，考虑使用懒加载
- 合理设置对话框的层级，避免层级冲突
- 关闭对话框时，释放不必要的资源

### 7.4 可访问性
- 确保对话框有明确的标题
- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

## 8. 常见问题与解决方案

### 8.1 对话框不显示
**问题**：设置了 `visible: true`，但对话框不显示
**解决方案**：检查对话框是否已添加到页面，或是否被其他元素遮挡，或是否有 CSS 样式冲突

### 8.2 对话框无法拖拽
**问题**：设置了 `draggable: true`，但对话框无法拖拽
**解决方案**：检查对话框是否有标题，或是否有自定义样式覆盖了拖拽功能

### 8.3 点击遮罩层无法关闭对话框
**问题**：设置了 `maskClosable: true`，但点击遮罩层无法关闭对话框
**解决方案**：检查是否有事件冒泡被阻止，或是否有其他元素拦截了点击事件

### 8.4 对话框大小无法调整
**问题**：设置了 `resizable: true`，但对话框大小无法调整
**解决方案**：检查是否有 CSS 样式限制了对话框的大小，或是否有其他元素拦截了鼠标事件

### 8.5 对话框层级问题
**问题**：对话框被其他元素遮挡
**解决方案**：调整对话框的 `zIndex` 属性，确保其层级高于其他元素

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 建议使用 Babel 转译 |

## 10. 相关组件

- **Button**：按钮组件，常用于对话框底部的操作按钮
- **FormLayout**：表单布局组件，常用于对话框中的表单设计
- **Message**：消息提示组件，用于简单的信息反馈
- **Loading**：加载组件，用于对话框中的加载状态

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本对话框功能 | 2025-01-15 |
| 1.1.0 | 新增拖拽和缩放功能 | 2025-03-20 |
| 1.2.0 | 新增全屏模式和响应式设计 | 2025-05-10 |
| 1.3.0 | 增强无障碍访问支持 | 2025-07-05 |
| 1.4.0 | 新增自定义底部按钮和多种对话框类型 | 2025-09-15 |

## 12. 扩展阅读

- [对话框设计最佳实践](https://developer.mozilla.org/zh-CN/docs/Web/Accessibility/ARIA/Roles/dialog_role)
- [无障碍对话框设计指南](https://www.w3.org/WAI/ARIA/apg/patterns/dialogmodal/)
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