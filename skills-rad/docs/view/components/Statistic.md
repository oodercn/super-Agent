# Statistic 统计组件

## 1. 组件概述

Statistic 是一个用于展示关键数据指标的统计组件，用于在页面中突出显示重要的数字、文本或状态信息，支持多种样式、主题和响应式设计。

**核心功能：**
- 支持展示数字、文本和状态信息
- 多种展示样式和布局选项
- 响应式设计，适配不同屏幕尺寸
- 支持多种主题（浅色、深色、高对比度）
- 完善的可访问性支持
- 支持图标和自定义样式

## 2. 核心概念

### 2.1 组件结构

Statistic 组件采用简洁的结构设计，主要包含标题、数值和描述三部分：

```
Statistic
├── Icon (可选，图标)
├── Value (核心数值)
├── Title (标题)
└── Description (描述)
```

### 2.2 数据模型

Statistic 使用对象形式的数据模型来定义统计项：

```javascript
items: [
    {
        id: 'stat1',           // 唯一标识
        title: '统计标题',       // 标题
        value: '12345',         // 数值
        description: '统计描述', // 描述
        iconClass: 'ri-arrow-up-line', // 图标类名
        color: '#4CAF50'        // 数值颜色
    },
    // 更多统计项...
]
```

## 3. 创建方式

### 3.1 静态创建（HTML）

```html
<div id="myStatistic" class="ood-ui-infoblock" data-items="[
    {id: 'stat1', title: '用户数量', value: '12,345', description: '本月新增', iconClass: 'ri-user-line'},
    {id: 'stat2', title: '销售额', value: '$98,765', description: '本月累计', iconClass: 'ri-money-line'},
    {id: 'stat3', title: '订单数量', value: '543', description: '今日新增', iconClass: 'ri-shopping-cart-line'}
]"></div>
```

### 3.2 动态创建（JavaScript）

```javascript
var statistic = ood("#myStatistic").infoblock({
    items: [
        {id: 'stat1', title: '用户数量', value: '12,345', description: '本月新增', iconClass: 'ri-user-line'},
        {id: 'stat2', title: '销售额', value: '$98,765', description: '本月累计', iconClass: 'ri-money-line'},
        {id: 'stat3', title: '订单数量', value: '543', description: '今日新增', iconClass: 'ri-shopping-cart-line'}
    ],
    width: '100%',
    columns: 3,
    theme: 'light'
});
```

### 3.3 工厂方法创建

```javascript
var statistic = ood.infoblock({
    items: [/* 统计项数据 */],
    width: '100%',
    columns: 3
});

// 添加到页面
ood("#container").append(statistic);
```

## 4. 属性说明

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `items` | Array | [] | 统计项数据数组 |
| `width` | String/Number | '16rem' | 组件宽度 |
| `height` | String/Number | '16rem' | 组件高度 |
| `columns` | Number | 0 | 显示列数，0表示自适应 |
| `rows` | Number | 0 | 显示行数，0表示自适应 |
| `itemMargin` | Number | 6 | 统计项间距 |
| `itemPadding` | Number | 2 | 统计项内边距 |
| `itemWidth` | Number | 32 | 统计项宽度 |
| `itemHeight` | Number | 32 | 统计项高度 |
| `autoItemSize` | Boolean | true | 是否自动调整统计项大小 |
| `autoImgSize` | Boolean | false | 是否自动调整图片大小 |
| `iconOnly` | Boolean | false | 是否仅显示图标 |
| `theme` | String | 'light' | 主题样式：'light', 'dark', 'highcontrast' |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `responsiveBreakpoint` | String | 'md' | 响应式断点：'sm', 'md', 'lg', 'xl' |

## 5. 方法列表

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| `setTheme(theme)` | theme: String | Object | 设置主题样式 |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | Object | 切换暗黑模式 |
| `adjustLayout()` | 无 | Object | 调整响应式布局 |
| `enhanceAccessibility()` | 无 | Object | 增强可访问性支持 |
| `updateItemData(profile, item)` | profile: Object, item: Object | 无 | 更新统计项数据 |

## 6. 事件处理

| 事件名 | 触发时机 | 参数说明 |
|--------|----------|----------|
| `onClick` | 点击统计项时 | profile, e, src |
| `onFlagClick` | 点击标记时 | profile, item, e, src |
| `touchstart` | 触摸开始时 | profile, item, e, src |
| `touchmove` | 触摸移动时 | profile, item, e, src |
| `touchend` | 触摸结束时 | profile, item, e, src |
| `touchcancel` | 触摸取消时 | profile, item, e, src |
| `swipe` | 滑动时 | profile, item, e, src |

## 7. 响应式设计

Statistic 组件提供了完善的响应式设计支持：

- 自动检测屏幕尺寸并调整布局
- 支持多种响应式断点：'sm', 'md', 'lg', 'xl'
- 小屏幕（<768px）自动调整样式和间距
- 超小屏幕（<480px）优化显示效果
- 支持触摸手势操作

## 8. 主题支持

Statistic 组件支持多种主题样式：

### 8.1 浅色主题（默认）

```javascript
statistic.setTheme('light');
```

### 8.2 深色主题

```javascript
statistic.setTheme('dark');
```

### 8.3 高对比度主题

```javascript
statistic.setTheme('highcontrast');
```

### 8.4 主题切换

```javascript
// 切换暗黑模式
statistic.toggleDarkMode();

// 获取当前主题
var currentTheme = statistic.getTheme();
```

## 9. 可访问性支持

Statistic 组件实现了丰富的可访问性支持：

- 为容器添加 `role="grid"` 和 `aria-label` 属性
- 为统计项添加 `role="gridcell"` 属性
- 支持键盘导航
- 为焦点元素添加视觉反馈
- 为图片添加 `alt` 属性
- 为图标添加 `aria-hidden="true"` 属性

## 10. 使用示例

### 10.1 基础统计卡片

```javascript
var basicStatistic = ood.infoblock({
    items: [
        {id: 'stat1', title: '用户数量', value: '12,345', description: '本月新增'},
        {id: 'stat2', title: '销售额', value: '$98,765', description: '本月累计'},
        {id: 'stat3', title: '订单数量', value: '543', description: '今日新增'}
    ],
    columns: 3,
    width: '100%'
});
```

### 10.2 带图标的统计卡片

```javascript
var iconStatistic = ood.infoblock({
    items: [
        {id: 'stat1', title: '用户数量', value: '12,345', description: '本月新增', iconClass: 'ri-user-line'},
        {id: 'stat2', title: '销售额', value: '$98,765', description: '本月累计', iconClass: 'ri-money-line'},
        {id: 'stat3', title: '订单数量', value: '543', description: '今日新增', iconClass: 'ri-shopping-cart-line'}
    ],
    columns: 3
});
```

### 10.3 自定义主题

```javascript
var themedStatistic = ood.infoblock({
    items: [/* 统计项数据 */],
    theme: 'dark'
});
```

### 10.4 响应式统计卡片

```javascript
var responsiveStatistic = ood.infoblock({
    items: [/* 统计项数据 */],
    responsive: true,
    responsiveBreakpoint: 'sm',
    columns: 4
});
```

## 11. 高级配置

### 11.1 自定义样式

```javascript
var styledStatistic = ood.infoblock({
    items: [
        {
            id: 'stat1',
            title: '用户数量',
            value: '12,345',
            description: '本月新增',
            iconClass: 'ri-user-line',
            itemStyle: 'background-color: #f0f8ff; border-radius: 8px;',
            valueStyle: 'color: #4CAF50; font-size: 24px;'
        }
    ]
});
```

### 11.2 动态更新数据

```javascript
var dynamicStatistic = ood.infoblock({
    items: [/* 初始统计项数据 */]
});

// 动态更新数据
function updateStatistics() {
    var statistic = ood("#myStatistic").get(0);
    var newData = {
        id: 'stat1',
        title: '用户数量',
        value: '12,345',
        description: '本月新增',
        iconClass: 'ri-user-line'
    };
    statistic.boxing().updateItemData(statistic, newData);
}

// 定期更新数据
setInterval(updateStatistics, 5000);
```

## 12. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 需使用兼容模式 |

## 13. 性能优化

- 减少统计项数量：建议每页不超过12个统计项
- 优化图标和图片：使用轻量级图标库，压缩图片
- 避免复杂样式：减少CSS动画和过渡效果
- 延迟加载：对于大量统计数据，使用异步加载

## 14. 最佳实践

1. **保持简洁**：统计卡片应突出核心数据，避免过多装饰
2. **使用一致的样式**：保持统计卡片的尺寸、颜色和字体一致
3. **添加适当的图标**：使用直观的图标增强信息传达
4. **使用对比色**：为关键数据使用对比色，提高可读性
5. **考虑响应式设计**：确保在不同屏幕尺寸下正常显示
6. **提供清晰的上下文**：使用标题和描述提供数据上下文

## 15. 常见问题

### 15.1 统计卡片布局错乱

**问题**：统计卡片在某些屏幕尺寸下布局错乱

**解决方案**：
- 确保设置了 `responsive: true`
- 调整 `columns` 属性，减少列数
- 使用 `itemMargin` 和 `itemPadding` 调整间距

### 15.2 图标不显示

**问题**：统计卡片中的图标不显示

**解决方案**：
- 确保图标库正确加载
- 检查图标类名是否正确
- 确保设置了正确的 `iconClass` 属性

### 15.3 主题切换不生效

**问题**：调用setTheme方法后，主题没有变化

**解决方案**：
- 确保主题名称正确：'light', 'dark', 'highcontrast'
- 检查CSS样式是否正确加载

## 16. 结语

Statistic 组件是一个功能强大、易于使用的统计数据展示解决方案，适用于各种仪表盘和数据分析场景。它提供了丰富的配置选项和事件处理，支持响应式设计和多种主题样式，同时注重可访问性和性能优化。

通过合理配置和使用Statistic组件，可以创建出美观、直观的统计数据展示效果，帮助用户快速理解和分析关键业务指标。