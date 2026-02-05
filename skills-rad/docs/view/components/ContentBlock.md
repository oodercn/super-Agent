# ContentBlock 组件

## 1. 组件概述
ContentBlock 组件是 ooder-a2ui 前端框架中的一个结构化内容容器，用于展示具有标题、正文和页脚的完整内容块，适合用于文章、文档、信息卡片等场景。

### 1.1 核心功能
- 支持标题、正文、页脚三部分结构
- 可配置边框、背景、阴影等样式
- 支持自定义标题图标
- 支持折叠/展开功能
- 支持响应式布局
- 支持内容滚动
- 可嵌套使用

### 1.2 适用场景
- 文章或文档的内容展示
- 信息卡片的结构化布局
- 仪表盘的数据展示
- 帮助文档的内容组织
- 产品介绍的内容呈现

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "contentBlock1",
  "type": "ContentBlock",
  "props": {
    "title": "内容块标题",
    "icon": "info",
    "collapsible": true,
    "collapsed": false,
    "showFooter": true
  },
  "children": {
    "header": [
      {
        "id": "headerBtn",
        "type": "Button",
        "props": {
          "text": "操作按钮",
          "size": "small"
        }
      }
    ],
    "body": [
      {
        "id": "contentText",
        "type": "Label",
        "props": {
          "text": "这是内容块的正文部分，可以包含各种组件。",
          "width": "100%"
        }
      }
    ],
    "footer": [
      {
        "id": "footerText",
        "type": "Label",
        "props": {
          "text": "这是内容块的页脚部分",
          "width": "100%"
        }
      }
    ]
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const contentBlock = ood.create("ContentBlock", {
  id: "contentBlock1",
  title: "动态创建的内容块",
  icon: "document",
  bordered: true,
  shadow: true,
  collapsible: true,
  height: 300,
  scrollable: true
});

// 添加头部内容
const headerBtn = ood.create("Button", { text: "头部按钮", size: "small" });
contentBlock.addToHeader(headerBtn);

// 添加正文内容
const bodyText = ood.create("Label", { text: "这是正文内容", width: "100%" });
contentBlock.addToBody(bodyText);

// 添加页脚内容
const footerText = ood.create("Label", { text: "这是页脚内容", width: "100%" });
contentBlock.addToFooter(footerText);

// 添加到父容器
parentComponent.addChild(contentBlock);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **title** | string | "" | 内容块标题 |
| **icon** | string | "" | 标题图标，支持内置图标名称 |
| **collapsible** | boolean | false | 是否可折叠 |
| **collapsed** | boolean | false | 初始是否折叠 |
| **bordered** | boolean | true | 是否显示边框 |
| **shadow** | boolean | false | 是否显示阴影 |
| **showFooter** | boolean | true | 是否显示页脚 |
| **scrollable** | boolean | false | 正文是否可滚动 |
| **height** | number | null | 内容块高度 |
| **maxHeight** | number | null | 内容块最大高度 |
| **backgroundColor** | string | "#ffffff" | 背景颜色 |
| **headerBackgroundColor** | string | "#fafafa" | 头部背景颜色 |
| **footerBackgroundColor** | string | "#fafafa" | 页脚背景颜色 |
| **titleColor** | string | "#333333" | 标题颜色 |
| **className** | string | "" | 自定义CSS类名 |
| **visible** | boolean | true | 是否可见 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **addToHeader** | `addToHeader(component)` | 向头部添加组件 |
| **addToBody** | `addToBody(component)` | 向正文添加组件 |
| **addToFooter** | `addToFooter(component)` | 向页脚添加组件 |
| **removeFromHeader** | `removeFromHeader(component)` | 从头部移除组件 |
| **removeFromBody** | `removeFromBody(component)` | 从正文移除组件 |
| **removeFromFooter** | `removeFromFooter(component)` | 从页脚移除组件 |
| **clearHeader** | `clearHeader()` | 清空头部内容 |
| **clearBody** | `clearBody()` | 清空正文内容 |
| **clearFooter** | `clearFooter()` | 清空页脚内容 |
| **toggleCollapse** | `toggleCollapse()` | 切换折叠/展开状态 |
| **collapse** | `collapse()` | 折叠内容块 |
| **expand** | `expand()` | 展开内容块 |
| **setTitle** | `setTitle(title)` | 设置标题 |
| **setIcon** | `setIcon(icon)` | 设置标题图标 |
| **setVisible** | `setVisible(visible)` | 设置组件可见性 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onCollapse** | 内容块折叠时触发 | `{ collapsed: boolean }` |
| **onExpand** | 内容块展开时触发 | `{ collapsed: boolean }` |
| **onTitleClick** | 标题被点击时触发 | `event` - 点击事件对象 |
| **onHeaderClick** | 头部被点击时触发 | `event` - 点击事件对象 |

## 6. 示例代码

### 6.1 基本内容块
```json
{
  "id": "basicContentBlock",
  "type": "ContentBlock",
  "props": {
    "title": "基本内容块",
    "icon": "info",
    "bordered": true
  },
  "children": {
    "body": [
      {
        "id": "contentLabel",
        "type": "Label",
        "props": {
          "text": "这是一个基本的ContentBlock组件示例，包含标题、图标和正文内容。",
          "width": "100%",
          "padding": 10
        }
      }
    ]
  }
}
```

### 6.2 可折叠内容块
```javascript
const collapsibleBlock = ood.create("ContentBlock", {
  id: "collapsibleBlock",
  title: "可折叠内容块",
  icon: "settings",
  collapsible: true,
  collapsed: false,
  height: 200,
  scrollable: true
});

// 添加多个内容项到正文
for (let i = 1; i <= 10; i++) {
  const item = ood.create("Label", {
    text: `这是第 ${i} 行内容，用于测试可折叠和滚动功能。`,
    width: "100%",
    padding: 5
  });
  collapsibleBlock.addToBody(item);
}

// 监听折叠事件
collapsibleBlock.on("onCollapse", (event) => {
  console.log("内容块折叠状态:", event.collapsed);
});

// 添加到页面
page.addChild(collapsibleBlock);
```

### 6.3 带页脚的内容块
```json
{
  "id": "footerContentBlock",
  "type": "ContentBlock",
  "props": {
    "title": "带页脚的内容块",
    "icon": "document",
    "showFooter": true,
    "shadow": true
  },
  "children": {
    "body": [
      {
        "id": "bodyContent",
        "type": "Label",
        "props": {
          "text": "这是内容块的正文部分，包含主要内容信息。",
          "width": "100%",
          "padding": 10
        }
      }
    ],
    "footer": [
      {
        "id": "footerInfo",
        "type": "Label",
        "props": {
          "text": "页脚信息：更新时间 2026-01-25",
          "width": "100%",
          "textAlign": "right",
          "padding": 5,
          "color": "#666666"
        }
      }
    ]
  }
}
```

### 6.4 嵌套内容块
```javascript
const parentBlock = ood.create("ContentBlock", {
  id: "parentBlock",
  title: "父内容块",
  icon: "folder",
  bordered: true
});

// 创建子内容块1
const childBlock1 = ood.create("ContentBlock", {
  id: "childBlock1",
  title: "子内容块1",
  icon: "file",
  height: 150,
  scrollable: true
});

// 创建子内容块2
const childBlock2 = ood.create("ContentBlock", {
  id: "childBlock2",
  title: "子内容块2",
  icon: "file",
  height: 150,
  scrollable: true
});

// 向子内容块添加内容
for (let i = 1; i <= 5; i++) {
  childBlock1.addToBody(ood.create("Label", {
    text: `子内容块1 - 第 ${i} 行内容`,
    padding: 5
  }));
  
  childBlock2.addToBody(ood.create("Label", {
    text: `子内容块2 - 第 ${i} 行内容`,
    padding: 5
  }));
}

// 创建Stacks布局来排列子内容块
const stacks = ood.create("Stacks", {
  direction: "vertical",
  spacing: 10
});

stacks.addChild(childBlock1);
stacks.addChild(childBlock2);

// 将Stacks添加到父内容块
parentBlock.addToBody(stacks);

// 添加到页面
page.addChild(parentBlock);
```

## 7. 最佳实践

### 7.1 内容组织
- 保持内容块的标题简洁明了，概括内容主题
- 正文内容应结构化，使用适当的组件展示不同类型的信息
- 页脚适合放置辅助信息，如更新时间、操作按钮等

### 7.2 样式设计
- 统一设置内容块的边框、阴影样式，保持视觉一致性
- 合理使用背景色区分不同层级的内容块
- 标题图标应与内容主题相关
- 避免过多使用阴影和边框，保持界面简洁

### 7.3 交互设计
- 对于内容较长的内容块，建议设置 `scrollable: true` 并指定合适的高度
- 对于包含大量内容的内容块，建议设置 `collapsible: true`，允许用户折叠
- 头部右侧适合放置操作按钮，如编辑、删除等

### 7.4 性能优化
- 避免在单个内容块中放置过多组件，影响渲染性能
- 对于可折叠的内容块，考虑延迟加载折叠部分的内容
- 合理使用 `maxHeight` 属性，避免内容块过高影响页面布局

## 8. 常见问题与解决方案

### 8.1 内容块高度自适应问题
**问题**：内容块高度无法自适应内容
**解决方案**：不设置固定高度，或使用 `maxHeight` 限制最大高度

### 8.2 折叠功能不生效
**问题**：设置了 `collapsible: true`，但无法折叠
**解决方案**：确保内容块有标题，折叠功能依赖标题点击

### 8.3 滚动条样式不一致
**问题**：不同浏览器下滚动条样式不一致
**解决方案**：使用自定义滚动条样式，或通过CSS统一滚动条外观

### 8.4 嵌套内容块样式冲突
**问题**：嵌套使用多个ContentBlock时样式冲突
**解决方案**：使用 `className` 自定义样式，或调整嵌套层级

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他组件 |

## 10. 相关组件

- **Panel**：简单的容器组件，适合基础布局
- **Block**：带侧边栏的高级容器
- **Layout**：用于复杂页面的整体布局
- **Stacks**：用于垂直或水平排列子组件
- **Card**：卡片式容器组件

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本内容块功能 | 2025-01-15 |
| 1.1.0 | 新增折叠/展开功能 | 2025-03-20 |
| 1.2.0 | 新增滚动功能 | 2025-05-10 |
| 1.3.0 | 增强样式定制能力 | 2025-07-05 |
| 1.4.0 | 优化响应式布局 | 2025-09-15 |

## 12. 扩展阅读

- [CSS 阴影效果指南](https://developer.mozilla.org/zh-CN/docs/Web/CSS/box-shadow)
- [响应式设计最佳实践](https://web.dev/responsive-design/)
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