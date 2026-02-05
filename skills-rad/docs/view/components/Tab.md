# Tab 组件

## 1. 组件概述
Tab 组件是 ooder-a2ui 前端框架中的一个标签页组件，用于在同一容器内展示多个内容面板，通过切换标签来显示不同的内容，支持多种样式和交互方式。

### 1.1 核心功能
- 支持水平和垂直两种标签方向
- 支持多种标签样式（线条、卡片、胶囊等）
- 支持动态添加和删除标签
- 支持禁用标签
- 支持自定义标签内容
- 支持标签滚动（标签过多时）
- 支持标签动画效果
- 支持主题定制
- 支持响应式设计

### 1.2 适用场景
- 页面内容的分类展示
- 表单的分步填写
- 数据的多维度展示
- 配置项的分类管理
- 产品功能的分类导航

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "tab1",
  "type": "Tab",
  "props": {
    "activeKey": "tab1",
    "type": "line",
    "tabPosition": "top"
  },
  "children": [
    {
      "id": "tabPane1",
      "type": "TabPane",
      "props": {
        "key": "tab1",
        "tab": "标签1"
      },
      "children": [
        {
          "id": "content1",
          "type": "Label",
          "props": {
            "text": "这是标签1的内容",
            "padding": 20
          }
        }
      ]
    },
    {
      "id": "tabPane2",
      "type": "TabPane",
      "props": {
        "key": "tab2",
        "tab": "标签2"
      },
      "children": [
        {
          "id": "content2",
          "type": "Label",
          "props": {
            "text": "这是标签2的内容",
            "padding": 20
          }
        }
      ]
    },
    {
      "id": "tabPane3",
      "type": "TabPane",
      "props": {
        "key": "tab3",
        "tab": "标签3",
        "disabled": true
      },
      "children": [
        {
          "id": "content3",
          "type": "Label",
          "props": {
            "text": "这是标签3的内容",
            "padding": 20
          }
        }
      ]
    }
  ]
}
```

### 2.2 JavaScript 方式创建
```javascript
const tab = ood.create("Tab", {
  id: "tab1",
  activeKey: "tab1",
  type: "card",
  tabPosition: "top",
  onChange: (activeKey) => {
    console.log("当前激活的标签:", activeKey);
  }
});

// 创建标签面板
const tabPane1 = ood.create("TabPane", {
  key: "tab1",
  tab: {
    type: "Stacks",
    props: {
      direction: "horizontal",
      alignItems: "center",
      spacing: 5
    },
    children: [
      {
        type: "Icon",
        props: {
          name: "home"
        }
      },
      {
        type: "Label",
        props: {
          text: "首页"
        }
      }
    ]
  }
});

const tabPane2 = ood.create("TabPane", {
  key: "tab2",
  tab: "关于我们"
});

const tabPane3 = ood.create("TabPane", {
  key: "tab3",
  tab: "联系我们"
});

// 添加内容到标签面板
const content1 = ood.create("Label", {
  text: "这是首页的内容",
  padding: 20
});

tabPane1.addChild(content1);

// 添加标签面板到Tab组件
tab.addChild(tabPane1);
tab.addChild(tabPane2);
tab.addChild(tabPane3);

// 添加到页面
page.addChild(tab);

// 动态添加标签
const addTab = () => {
  const newKey = `tab${tab.getChildren().length + 1}`;
  const newTab = ood.create("TabPane", {
    key: newKey,
    tab: `新标签${tab.getChildren().length + 1}`
  });
  
  const newContent = ood.create("Label", {
    text: `这是新标签${tab.getChildren().length + 1}的内容`,
    padding: 20
  });
  
  newTab.addChild(newContent);
  tab.addChild(newTab);
  tab.setActiveKey(newKey);
};
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **activeKey** | string | - | 当前激活的标签页 key |
| **defaultActiveKey** | string | - | 默认激活的标签页 key |
| **type** | string | "line" | 标签类型："line"、"card"、"capsule" |
| **tabPosition** | string | "top" | 标签位置："top"、"bottom"、"left"、"right" |
| **size** | string | "medium" | 标签尺寸："small"、"medium"、"large" |
| **animated** | boolean | true | 是否启用标签切换动画 |
| **swipeable** | boolean | false | 是否支持滑动切换标签 |
| **hideAdd** | boolean | true | 是否隐藏添加标签按钮 |
| **closable** | boolean | false | 是否支持关闭标签 |
| **centered** | boolean | false | 标签是否居中显示 |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |

## 4. TabPane 属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **key** | string | - | 标签页唯一标识 |
| **tab** | string/object | - | 标签标题 |
| **disabled** | boolean | false | 是否禁用标签 |
| **closable** | boolean | null | 是否可关闭，继承 Tab 组件的 closable 属性 |
| **forceRender** | boolean | false | 是否强制渲染，即使标签未激活 |
| **className** | string | - | 自定义CSS类名 |

## 5. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setActiveKey** | `setActiveKey(key)` | 设置当前激活的标签页 |
| **getActiveKey** | `getActiveKey()` | 获取当前激活的标签页 key |
| **addChild** | `addChild(tabPane)` | 添加标签页 |
| **removeChild** | `removeChild(key)` | 移除指定标签页 |
| **updateChild** | `updateChild(key, props)` | 更新指定标签页属性 |
| **getChildren** | `getChildren()` | 获取所有标签页 |
| **setTabPosition** | `setTabPosition(position)` | 设置标签位置 |
| **setType** | `setType(type)` | 设置标签类型 |

## 6. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 标签切换时触发 | `activeKey` - 当前激活的标签页 key |
| **onTabClick** | 点击标签时触发 | `key` - 点击的标签页 key |
| **onTabScroll** | 标签滚动时触发 | `{ direction, currentOffset, scrollOffset }` |
| **onTabAdd** | 点击添加标签按钮时触发 | 无 |
| **onTabRemove** | 点击删除标签按钮时触发 | `key` - 要删除的标签页 key |
| **onTabDisable** | 点击禁用标签时触发 | `key` - 点击的禁用标签页 key |

## 7. 示例代码

### 7.1 线条样式标签
```json
{
  "id": "lineTab",
  "type": "Tab",
  "props": {
    "activeKey": "tab1",
    "type": "line",
    "tabPosition": "top"
  },
  "children": [
    {
      "type": "TabPane",
      "props": {
        "key": "tab1",
        "tab": "标签1"
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "这是线条样式标签1的内容",
            "padding": 20
          }
        }
      ]
    },
    {
      "type": "TabPane",
      "props": {
        "key": "tab2",
        "tab": "标签2"
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "这是线条样式标签2的内容",
            "padding": 20
          }
        }
      ]
    }
  ]
}
```

### 7.2 卡片样式标签
```javascript
const cardTab = ood.create("Tab", {
  id: "cardTab",
  activeKey: "tab1",
  type: "card",
  tabPosition: "top",
  size: "large"
});

// 创建标签面板
const tabPane1 = ood.create("TabPane", {
  key: "tab1",
  tab: "卡片标签1"
});

const tabPane2 = ood.create("TabPane", {
  key: "tab2",
  tab: "卡片标签2"
});

// 添加内容
const content1 = ood.create("Card", {
  title: "卡片内容1",
  children: [
    {
      type: "Label",
      props: {
        text: "这是卡片样式标签1的内容",
        padding: 10
      }
    }
  ]
});

tabPane1.addChild(content1);

// 添加到Tab组件
cardTab.addChild(tabPane1);
cardTab.addChild(tabPane2);

// 添加到页面
page.addChild(cardTab);
```

### 7.3 垂直标签
```json
{
  "id": "verticalTab",
  "type": "Tab",
  "props": {
    "activeKey": "tab1",
    "type": "line",
    "tabPosition": "left",
    "style": {
      "height": 300
    }
  },
  "children": [
    {
      "type": "TabPane",
      "props": {
        "key": "tab1",
        "tab": "垂直标签1"
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "这是垂直标签1的内容",
            "padding": 20
          }
        }
      ]
    },
    {
      "type": "TabPane",
      "props": {
        "key": "tab2",
        "tab": "垂直标签2"
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "这是垂直标签2的内容",
            "padding": 20
          }
        }
      ]
    },
    {
      "type": "TabPane",
      "props": {
        "key": "tab3",
        "tab": "垂直标签3"
      },
      "children": [
        {
          "type": "Label",
          "props": {
            "text": "这是垂直标签3的内容",
            "padding": 20
          }
        }
      ]
    }
  ]
}
```

### 7.4 可关闭的标签
```javascript
const closableTab = ood.create("Tab", {
  id: "closableTab",
  activeKey: "tab1",
  type: "card",
  closable: true,
  hideAdd: false
});

// 创建标签面板
const tabPane1 = ood.create("TabPane", {
  key: "tab1",
  tab: "可关闭标签1"
});

const tabPane2 = ood.create("TabPane", {
  key: "tab2",
  tab: "可关闭标签2"
});

// 添加内容
const content1 = ood.create("Label", {
  text: "这是可关闭标签1的内容",
  padding: 20
});

tabPane1.addChild(content1);

// 添加到Tab组件
closableTab.addChild(tabPane1);
closableTab.addChild(tabPane2);

// 监听标签关闭事件
closableTab.on("onTabRemove", (key) => {
  console.log("要关闭的标签:", key);
  // 可以在这里添加自定义的关闭逻辑
});

// 监听添加标签事件
closableTab.on("onTabAdd", () => {
  console.log("添加新标签");
  // 可以在这里添加自定义的添加标签逻辑
  addNewTab(closableTab);
});

// 添加到页面
page.addChild(closableTab);
```

## 8. 最佳实践

### 8.1 标签设计
- 保持标签标题的简洁性，避免过长的标签文本
- 合理使用标签类型，线条类型适合简洁界面，卡片类型适合突出标签
- 对于重要的标签，放在前面位置
- 合理设置标签位置，垂直标签适合分类较多的场景

### 8.2 交互设计
- 提供清晰的激活状态反馈
- 合理使用动画效果，提升用户体验
- 对于可关闭的标签，提供明确的关闭按钮
- 对于标签过多的情况，支持滚动或分页

### 8.3 性能优化
- 对于复杂的标签内容，考虑使用懒加载
- 合理使用 forceRender 属性，避免不必要的渲染
- 避免在标签切换时执行复杂计算
- 对于大量标签，考虑使用虚拟滚动

### 8.4 响应式设计
- 在小屏幕上，考虑调整标签位置或类型
- 合理设置标签容器的高度和宽度
- 对于移动端，考虑支持滑动切换

### 8.5 可访问性
- 确保标签支持键盘导航
- 提供清晰的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度
- 为标签添加适当的焦点样式

## 9. 常见问题与解决方案

### 9.1 标签内容不显示
**问题**：切换标签后，标签内容不显示
**解决方案**：确保 TabPane 正确配置了 key 属性，且内容正确添加到 TabPane 中

### 9.2 标签切换动画不生效
**问题**：设置了 animated: true，但标签切换没有动画效果
**解决方案**：检查浏览器是否支持 CSS 动画，或尝试更换动画类型

### 9.3 标签过多导致溢出
**问题**：标签数量过多，导致标签栏溢出
**解决方案**：使用默认的标签滚动功能，或减少标签数量，或调整标签位置为垂直

### 9.4 动态添加标签不生效
**问题**：调用 addChild 方法添加标签后，标签不显示
**解决方案**：确保新添加的 TabPane 配置了正确的 key 和 tab 属性

### 9.5 垂直标签样式异常
**问题**：垂直标签的样式显示异常
**解决方案**：确保正确设置了 Tab 组件的高度，或调整标签样式

## 10. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他标签方案 |

## 11. 相关组件

- **Menu**：菜单组件，用于导航
- **Breadcrumb**：面包屑组件，用于显示当前位置
- **Card**：卡片组件，用于内容展示
- **Layout**：布局组件，用于页面布局

## 12. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本标签功能 | 2025-01-15 |
| 1.1.0 | 新增多种标签类型和位置 | 2025-03-20 |
| 1.2.0 | 支持动态添加和删除标签 | 2025-05-10 |
| 1.3.0 | 新增标签动画效果 | 2025-07-05 |
| 1.4.0 | 支持垂直标签和响应式设计 | 2025-09-15 |
| 1.5.0 | 支持标签滚动和虚拟滚动 | 2025-11-20 |

## 13. 扩展阅读

- [CSS 标签设计指南](https://developer.mozilla.org/zh-CN/docs/Web/CSS/Pseudo-elements)
- [响应式标签设计最佳实践](https://web.dev/responsive-design/)
- [ooder-a2ui 组件库介绍](FRONTEND_COMPONENTS.md)

## 14. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队