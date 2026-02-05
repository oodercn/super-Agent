# Breadcrumb 组件

## 1. 组件概述
Breadcrumb 组件是 ooder-a2ui 前端框架中的一个导航组件，用于显示当前页面在网站层级结构中的位置，帮助用户了解自己的位置并方便返回上一级页面。

### 1.1 核心功能
- 支持多种分隔符样式
- 支持自定义分隔符
- 支持点击交互
- 支持响应式设计
- 支持主题定制
- 支持无障碍访问
- 支持动态生成

### 1.2 适用场景
- 网站导航层级展示
- 页面位置指示
- 多级目录导航
- 面包屑导航路径

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "breadcrumb1",
  "type": "Breadcrumb",
  "props": {
    "separator": ">",
    "items": [
      {
        "text": "首页",
        "href": "#",
        "onClick": "goHome"
      },
      {
        "text": "产品管理",
        "href": "#products",
        "onClick": "goProducts"
      },
      {
        "text": "产品详情",
        "href": "#product-detail",
        "onClick": "goProductDetail"
      }
    ]
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const breadcrumb = ood.create("Breadcrumb", {
  id: "breadcrumb1",
  separator: ">",
  items: [
    {
      text: "首页",
      href: "#",
      onClick: () => {
        console.log("跳转到首页");
      }
    },
    {
      text: "产品管理",
      href: "#products",
      onClick: () => {
        console.log("跳转到产品管理");
      }
    },
    {
      text: "产品详情",
      href: "#product-detail",
      onClick: () => {
        console.log("跳转到产品详情");
      }
    }
  ]
});

// 添加到页面
page.addChild(breadcrumb);
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **items** | array | [] | 面包屑项数组 |
| **separator** | string | ">" | 分隔符，可以是字符串或自定义组件 |
| **separatorType** | string | "default" | 分隔符类型："default"、"slash"、"arrow"、"dot" |
| **theme** | string | "light" | 主题："light" 或 "dark" |
| **className** | string | "" | 自定义CSS类名 |
| **style** | object | {} | 自定义样式 |

## 4. 面包屑项属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **text** | string | - | 面包屑项文本 |
| **href** | string | - | 面包屑项链接 |
| **onClick** | function | - | 面包屑项点击事件处理函数 |
| **disabled** | boolean | false | 是否禁用面包屑项 |
| **className** | string | - | 面包屑项自定义CSS类名 |
| **style** | object | {} | 面包屑项自定义样式 |

## 5. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setItems** | `setItems(items)` | 设置面包屑项 |
| **getItems** | `getItems()` | 获取面包屑项 |
| **addItem** | `addItem(item)` | 添加面包屑项 |
| **removeItem** | `removeItem(index)` | 移除指定索引的面包屑项 |
| **updateItem** | `updateItem(index, item)` | 更新指定索引的面包屑项 |
| **clearItems** | `clearItems()` | 清空面包屑项 |
| **setSeparator** | `setSeparator(separator)` | 设置分隔符 |
| **setSeparatorType** | `setSeparatorType(type)` | 设置分隔符类型 |

## 6. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onItemClick** | 面包屑项点击时触发 | `(item, index)` - 点击的面包屑项和索引 |

## 7. 示例代码

### 7.1 不同分隔符样式
```json
{
  "id": "breadcrumbGroup",
  "type": "Stacks",
  "props": {
    "direction": "vertical",
    "spacing": 20,
    "padding": 20
  },
  "children": [
    {
      "type": "Label",
      "props": {
        "text": "默认分隔符 (>)",
        "style": {
          "fontWeight": "bold"
        }
      }
    },
    {
      "type": "Breadcrumb",
      "props": {
        "id": "breadcrumbDefault",
        "separator": ">",
        "items": [
          { "text": "首页", "href": "#" },
          { "text": "产品管理", "href": "#products" },
          { "text": "产品详情", "href": "#product-detail" }
        ]
      }
    },
    {
      "type": "Label",
      "props": {
        "text": "斜杠分隔符 (/)",
        "style": {
          "fontWeight": "bold"
        }
      }
    },
    {
      "type": "Breadcrumb",
      "props": {
        "id": "breadcrumbSlash",
        "separator": "/",
        "items": [
          { "text": "首页", "href": "#" },
          { "text": "产品管理", "href": "#products" },
          { "text": "产品详情", "href": "#product-detail" }
        ]
      }
    },
    {
      "type": "Label",
      "props": {
        "text": "箭头分隔符 (→)",
        "style": {
          "fontWeight": "bold"
        }
      }
    },
    {
      "type": "Breadcrumb",
      "props": {
        "id": "breadcrumbArrow",
        "separator": "→",
        "items": [
          { "text": "首页", "href": "#" },
          { "text": "产品管理", "href": "#products" },
          { "text": "产品详情", "href": "#product-detail" }
        ]
      }
    },
    {
      "type": "Label",
      "props": {
        "text": "点分隔符 (·)",
        "style": {
          "fontWeight": "bold"
        }
      }
    },
    {
      "type": "Breadcrumb",
      "props": {
        "id": "breadcrumbDot",
        "separator": "·",
        "items": [
          { "text": "首页", "href": "#" },
          { "text": "产品管理", "href": "#products" },
          { "text": "产品详情", "href": "#product-detail" }
        ]
      }
    }
  ]
}
```

### 7.2 自定义分隔符
```javascript
const customBreadcrumb = ood.create("Breadcrumb", {
  id: "customBreadcrumb",
  separator: {
    type: "Icon",
    props: {
      name: "arrow-right",
      style: {
        margin: "0 8px",
        color: "#999"
      }
    }
  },
  items: [
    {
      text: "首页",
      href: "#",
      onClick: () => {
        console.log("跳转到首页");
      }
    },
    {
      text: "产品管理",
      href: "#products",
      onClick: () => {
        console.log("跳转到产品管理");
      }
    },
    {
      text: "产品详情",
      href: "#product-detail",
      onClick: () => {
        console.log("跳转到产品详情");
      }
    }
  ]
});

// 添加到页面
page.addChild(customBreadcrumb);
```

### 7.3 动态生成面包屑
```javascript
const dynamicBreadcrumb = ood.create("Breadcrumb", {
  id: "dynamicBreadcrumb",
  separator: ">"
});

// 动态生成面包屑项
function generateBreadcrumb(path) {
  const items = [];
  const pathArray = path.split("/");
  
  // 添加首页
  items.push({
    text: "首页",
    href: "#",
    onClick: () => {
      console.log("跳转到首页");
    }
  });
  
  // 添加中间路径
  let currentPath = "";
  for (let i = 0; i < pathArray.length; i++) {
    const segment = pathArray[i];
    if (segment) {
      currentPath += `/${segment}`;
      items.push({
        text: segment,
        href: `#${currentPath}`,
        onClick: () => {
          console.log(`跳转到 ${currentPath}`);
        }
      });
    }
  }
  
  // 更新面包屑项
  dynamicBreadcrumb.setItems(items);
}

// 调用生成函数
generateBreadcrumb("product-management/product-detail");

// 添加到页面
page.addChild(dynamicBreadcrumb);
```

### 7.4 禁用状态的面包屑项
```json
{
  "id": "disabledBreadcrumb",
  "type": "Breadcrumb",
  "props": {
    "separator": ">",
    "items": [
      {
        "text": "首页",
        "href": "#",
        "onClick": "goHome"
      },
      {
        "text": "产品管理",
        "href": "#products",
        "disabled": true
      },
      {
        "text": "产品详情",
        "href": "#product-detail",
        "onClick": "goProductDetail"
      }
    ]
  }
}
```

## 8. 最佳实践

### 8.1 导航设计
- 保持面包屑路径的简洁性，避免过长
- 合理设置分隔符，保持视觉清晰
- 最后一项通常为当前页面，不添加链接
- 面包屑项数量建议不超过 5 个

### 8.2 交互设计
- 提供清晰的点击反馈
- 禁用状态下提供明确的视觉提示
- 支持键盘导航
- 提供适当的焦点样式

### 8.3 响应式设计
- 在小屏幕上，考虑折叠过长的面包屑
- 合理设置面包屑的宽度和溢出处理
- 对于移动端，考虑简化面包屑路径

### 8.4 可访问性
- 支持键盘导航
- 提供适当的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

## 9. 常见问题与解决方案

### 9.1 面包屑项不显示
**问题**：设置了 `items` 属性，但面包屑项不显示
**解决方案**：检查 `items` 数组格式是否正确，或是否有 CSS 样式冲突

### 9.2 分隔符不显示
**问题**：设置了 `separator` 属性，但分隔符不显示
**解决方案**：检查 `separator` 值是否正确，或是否有 CSS 样式覆盖

### 9.3 点击事件不触发
**问题**：设置了 `onClick` 事件，但点击时不触发
**解决方案**：检查事件绑定是否正确，或是否有元素阻止了事件冒泡

### 9.4 面包屑过长
**问题**：面包屑项数量过多，导致面包屑过长
**解决方案**：考虑折叠过长的面包屑，或简化面包屑路径

### 9.5 样式异常
**问题**：面包屑样式显示异常
**解决方案**：检查 CSS 样式是否冲突，或是否正确设置了主题

## 10. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 建议使用 Babel 转译 |

## 11. 相关组件

- **Menu**：菜单组件，用于网站导航
- **Tab**：标签页组件，用于内容切换
- **Layout**：布局组件，用于页面布局

## 12. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本面包屑功能 | 2025-01-15 |
| 1.1.0 | 新增自定义分隔符和动态生成功能 | 2025-03-20 |
| 1.2.0 | 增强无障碍访问支持 | 2025-05-10 |
| 1.3.0 | 支持响应式设计和主题定制 | 2025-07-05 |

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队