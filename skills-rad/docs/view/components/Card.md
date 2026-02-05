# Card 组件

## 1. 组件概述

Card 组件是 ooder-a2ui 框架中的数据展示组件，用于以卡片形式展示结构化信息。它提供了灵活的布局和样式选项，支持多种卡片类型，是构建现代化UI界面的重要组件之一。

### 1.1 核心功能

- 多种卡片样式（基础卡片、带图卡片、带操作卡片等）
- 灵活的内容布局（标题、副标题、内容、操作区等）
- 支持卡片阴影和边框自定义
- 支持卡片点击和悬停效果
- 支持响应式设计
- 支持卡片组和网格布局
- 支持自定义卡片模板

### 1.2 应用场景

- 商品展示卡片
- 内容卡片
- 个人信息卡片
- 统计数据卡片
- 新闻卡片
- 应用卡片
- 卡片式菜单

## 2. 创建方式

### 2.1 JSON 方式

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "卡片标题",
    "subtitle": "卡片副标题",
    "content": "卡片内容描述",
    "image": {
      "src": "https://example.com/image.jpg",
      "width": "100%",
      "height": 200
    },
    "actions": [
      {"text": "操作1", "onClick": "onAction1"},
      {"text": "操作2", "onClick": "onAction2"}
    ],
    "onClick": "onCardClick"
  }
}
```

### 2.2 JavaScript 方式

```javascript
// 创建 Card 组件
var card = ood.create("ood.UI.Card")
    .setProperties({
        title: "卡片标题",
        subtitle: "卡片副标题",
        content: "卡片内容描述",
        image: {
            src: "https://example.com/image.jpg",
            width: "100%",
            height: 200
        },
        actions: [
            {
                text: "操作1",
                onClick: function() {
                    console.log("点击了操作1");
                }
            },
            {
                text: "操作2",
                onClick: function() {
                    console.log("点击了操作2");
                }
            }
        ],
        onClick: function(profile) {
            console.log("点击了卡片");
        }
    });
```

## 3. 属性配置

### 3.1 基础属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `key` | String | "ood.UI.Card" | 组件类型 |
| `title` | String | "" | 卡片标题 |
| `subtitle` | String | "" | 卡片副标题 |
| `content` | String | "" | 卡片内容 |
| `image` | Object | null | 卡片图片配置 |
| `actions` | Array | [] | 卡片操作按钮配置 |
| `footer` | String/Object | null | 卡片底部内容 |
| `className` | String | "" | 自定义类名 |

### 3.2 样式属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `width` | String/Number | "auto" | 卡片宽度 |
| `height` | String/Number | "auto" | 卡片高度 |
| `background` | String | "#ffffff" | 卡片背景色 |
| `border` | String | "none" | 卡片边框 |
| `borderRadius` | Number | 8 | 卡片圆角 |
| `boxShadow` | String | "0 2px 8px rgba(0, 0, 0, 0.1)" | 卡片阴影 |
| `padding` | Number/String | 16 | 卡片内边距 |
| `margin` | Number/String | 0 | 卡片外边距 |
| `hoverEffect` | Boolean | true | 是否启用悬停效果 |
| `transition` | String | "all 0.3s ease" | 过渡动画 |

### 3.3 图片属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `src` | String | "" | 图片 URL |
| `width` | String/Number | "100%" | 图片宽度 |
| `height` | Number | 150 | 图片高度 |
| `objectFit` | String | "cover" | 图片适应方式 |
| `radius` | Number | 8 | 图片圆角 |
| `position` | String | "top" | 图片位置：top, bottom |

### 3.4 操作按钮属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `text` | String | "" | 按钮文本 |
| `icon` | String | "" | 按钮图标 |
| `type` | String | "default" | 按钮类型：primary, default, danger, success |
| `size` | String | "medium" | 按钮大小：small, medium, large |
| `onClick` | Function/String | null | 点击事件处理函数 |

## 4. 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `setTitle(title)` | 设置卡片标题 | title: 标题文本 | 当前实例 |
| `setSubtitle(subtitle)` | 设置卡片副标题 | subtitle: 副标题文本 | 当前实例 |
| `setContent(content)` | 设置卡片内容 | content: 内容文本 | 当前实例 |
| `setImage(image)` | 设置卡片图片 | image: 图片配置对象 | 当前实例 |
| `setActions(actions)` | 设置卡片操作按钮 | actions: 操作按钮数组 | 当前实例 |
| `setFooter(footer)` | 设置卡片底部内容 | footer: 底部内容 | 当前实例 |
| `setLoading(loading)` | 设置卡片加载状态 | loading: 是否显示加载状态 | 当前实例 |
| `refresh()` | 刷新卡片 | 无 | 当前实例 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| `onClick` | 卡片点击事件 | profile: 组件配置 |
| `onHover` | 卡片悬停事件 | profile: 组件配置<br>isHover: 是否悬停 |
| `onActionClick` | 卡片操作按钮点击事件 | profile: 组件配置<br>action: 点击的操作配置<br>index: 操作按钮索引 |

## 6. 卡片类型

### 6.1 基础卡片

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "基础卡片",
    "content": "这是一个基础卡片示例，用于展示简单的信息内容。"
  }
}
```

### 6.2 带图卡片

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "带图卡片",
    "subtitle": "带有图片的卡片示例",
    "image": {
      "src": "https://example.com/image.jpg",
      "height": 200
    },
    "content": "这是一个带有图片的卡片示例，图片位于卡片顶部。"
  }
}
```

### 6.3 带操作卡片

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "带操作卡片",
    "content": "这是一个带有操作按钮的卡片示例，操作按钮位于卡片底部。",
    "actions": [
      {"text": "查看详情", "type": "primary", "onClick": "onViewDetail"},
      {"text": "删除", "type": "danger", "onClick": "onDelete"}
    ]
  }
}
```

### 6.4 统计卡片

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "统计卡片",
    "subtitle": "用户数量",
    "content": "12,345",
    "footer": "较上月增长 15%",
    "className": "statistic-card"
  }
}
```

### 6.5 带徽章卡片

```json
{
  "key": "ood.UI.Card",
  "properties": {
    "title": "带徽章卡片",
    "subtitle": "带有徽章的卡片示例",
    "badge": {
      "text": "热门",
      "type": "success"
    },
    "content": "这是一个带有徽章的卡片示例，徽章位于卡片右上角。"
  }
}
```

## 7. 使用示例

### 7.1 基础卡片

```javascript
// 创建基础卡片
var basicCard = ood.create("ood.UI.Card")
    .setProperties({
        title: "基础卡片",
        content: "这是一个基础卡片示例，用于展示简单的信息内容。可以包含标题和正文，适用于各种信息展示场景。",
        padding: 20
    });
```

### 7.2 商品卡片

```javascript
// 创建商品卡片
var productCard = ood.create("ood.UI.Card")
    .setProperties({
        title: "商品名称",
        subtitle: "商品分类",
        image: {
            src: "https://example.com/product.jpg",
            height: 200
        },
        content: "这是一个商品卡片示例，展示商品图片、名称、价格等信息。",
        footer: {
            price: "¥99.00",
            originalPrice: "¥199.00"
        },
        actions: [
            {
                text: "加入购物车",
                type: "primary",
                onClick: function() {
                    ood.Mobile.Toast.success("已加入购物车");
                }
            },
            {
                text: "立即购买",
                onClick: function() {
                    ood.Mobile.Toast.success("跳转至结算页面");
                }
            }
        ],
        onClick: function() {
            ood.Mobile.Toast.success("查看商品详情");
        }
    });
```

### 7.3 统计卡片

```javascript
// 创建统计卡片
var statisticCard = ood.create("ood.UI.Card")
    .setProperties({
        title: "用户统计",
        subtitle: "总用户数",
        content: "12,345",
        footer: "较上月增长 15%",
        className: "statistic-card",
        style: {
            textAlign: "center",
            padding: "30px 20px"
        }
    });
```

### 7.4 卡片组

```javascript
// 创建卡片组
var cardGroup = ood.create("ood.UI.CardGroup")
    .setProperties({
        layout: "grid",
        columns: 3,
        gap: 20,
        data: [
            {
                title: "卡片 1",
                content: "卡片 1 内容",
                image: { src: "https://example.com/image1.jpg" }
            },
            {
                title: "卡片 2",
                content: "卡片 2 内容",
                image: { src: "https://example.com/image2.jpg" }
            },
            {
                title: "卡片 3",
                content: "卡片 3 内容",
                image: { src: "https://example.com/image3.jpg" }
            }
        ]
    });
```

### 7.5 自定义卡片

```javascript
// 创建自定义卡片
var customCard = ood.create("ood.UI.Card")
    .setProperties({
        title: "自定义卡片",
        content: "这是一个自定义样式的卡片示例，通过 className 属性添加自定义 CSS 类。",
        className: "custom-card",
        style: {
            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            color: "#fff",
            boxShadow: "0 8px 32px rgba(0, 0, 0, 0.2)"
        }
    });
```

## 8. 最佳实践

### 8.1 卡片设计原则

1. **保持简洁**：卡片内容应简洁明了，避免过多信息
2. **视觉层次**：使用清晰的标题、副标题和内容层次
3. **适当留白**：保持合适的内边距和外边距，提高可读性
4. **一致的样式**：在整个应用中使用统一的卡片样式
5. **响应式设计**：确保卡片在不同屏幕尺寸下都能正常显示
6. **交互反馈**：提供清晰的点击和悬停反馈

### 8.2 卡片布局

1. **网格布局**：对于多个卡片，使用网格布局保持整齐排列
2. **合适的卡片大小**：卡片大小应适中，不宜过大或过小
3. **一致的间距**：卡片之间保持一致的间距
4. **响应式调整**：在小屏幕上自动调整卡片数量和大小

### 8.3 性能优化

1. **图片优化**：卡片中的图片应进行压缩和懒加载
2. **避免复杂动画**：卡片动画应简洁，避免过度复杂的效果
3. **合理使用阴影**：阴影效果应适度，避免影响性能
4. **虚拟滚动**：对于大量卡片，使用虚拟滚动减少DOM节点

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分功能可能受限 |

## 10. 注意事项

1. **内容长度控制**：卡片内容不宜过长，避免卡片过高
2. **图片尺寸**：确保卡片图片尺寸合适，避免拉伸或变形
3. **操作按钮数量**：卡片操作按钮数量不宜过多，建议不超过3个
4. **性能考虑**：对于大量卡片，考虑使用虚拟滚动或分页
5. **可访问性**：添加适当的ARIA属性，确保键盘可访问
6. **测试不同屏幕尺寸**：确保卡片在各种屏幕尺寸下都能正常显示

## 11. 扩展阅读

- [List 组件文档](List.md)
- [Table 组件文档](Table.md)
- [响应式设计指南](../RESPONSIVE_DESIGN.md)
- [UI设计最佳实践](../UI_DESIGN_BEST_PRACTICES.md)

Card 组件是构建现代化UI界面的重要组件，通过合理配置和扩展，可以创建出各种风格和功能的卡片，满足不同场景的需求。结合卡片组和网格布局，可以创建出丰富多样的数据展示界面。