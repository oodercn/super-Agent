# Carousel 轮播组件

## 1. 组件概述

Carousel 是一个功能强大的轮播图组件，用于在有限空间内展示多张图片或内容卡片，支持自动轮播、手动滑动、键盘导航和多种主题样式。

**核心功能：**
- 支持自动轮播和手动滑动切换
- 响应式设计，适配不同屏幕尺寸
- 支持多种主题（浅色、深色）
- 完善的可访问性支持
- 支持触摸手势和键盘导航
- 自定义过渡动画效果
- 图片加载状态管理

## 2. 核心概念

### 2.1 组件结构

Carousel 组件采用多层嵌套结构设计：

```
Carousel
├── Items (容器)
│   ├── Item (轮播项)
│   │   ├── ItemFrame (项框架)
│   │   │   ├── Content (内容区)
│   │   │   │   ├── Icon (图标)
│   │   │   │   └── Image (图片)
│   │   │   ├── Caption (标题)
│   │   │   └── Comment (描述)
│   │   └── Flag (标记)
```

### 2.2 数据模型

Carousel 使用数组形式的数据模型来定义轮播项：

```javascript
items: [
    {
        id: 'item1',           // 唯一标识
        caption: '图片1',       // 标题
        comment: '描述文字',     // 描述
        image: 'path/to/image.jpg',  // 图片路径
        imageClass: 'ri-image-line'  // 图标类名
    },
    // 更多轮播项...
]
```

## 3. 创建方式

### 3.1 静态创建（HTML）

```html
<div id="myCarousel" class="ood-ui-gallery" data-items="[
    {id: 'item1', caption: '图片1', image: 'image1.jpg'},
    {id: 'item2', caption: '图片2', image: 'image2.jpg'},
    {id: 'item3', caption: '图片3', image: 'image3.jpg'}
]"></div>
```

### 3.2 动态创建（JavaScript）

```javascript
var carousel = ood("#myCarousel").gallery({
    items: [
        {id: 'item1', caption: '图片1', image: 'image1.jpg'},
        {id: 'item2', caption: '图片2', image: 'image2.jpg'},
        {id: 'item3', caption: '图片3', image: 'image3.jpg'}
    ],
    width: '100%',
    height: '300px',
    responsive: true
});
```

### 3.3 工厂方法创建

```javascript
var carousel = ood.gallery({
    items: [/* 轮播项数据 */],
    width: '100%',
    height: '300px'
});

// 添加到页面
ood("#container").append(carousel);
```

## 4. 属性说明

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `items` | Array | [] | 轮播项数据数组 |
| `value` | String | '' | 当前选中的轮播项ID |
| `width` | String/Number | '32em' | 组件宽度 |
| `height` | String/Number | '20em' | 组件高度 |
| `theme` | String | 'light' | 主题样式：'light', 'dark' |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `autoColumns` | Boolean | false | 是否自动调整列数 |
| `columns` | Number | 0 | 显示列数，0表示自适应 |
| `rows` | Number | 0 | 显示行数，0表示自适应 |
| `itemMargin` | Number | 6 | 轮播项间距 |
| `itemPadding` | Number | 2 | 轮播项内边距 |
| `itemWidth` | Number | 32 | 轮播项宽度 |
| `itemHeight` | Number | 32 | 轮播项高度 |
| `autoItemSize` | Boolean | true | 是否自动调整轮播项大小 |
| `autoImgSize` | Boolean | false | 是否自动调整图片大小 |
| `iconOnly` | Boolean | false | 是否仅显示图标 |
| `backgroundColor` | String | '' | 背景颜色 |

## 5. 方法列表

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| `setStatus(id)` | id: String | String | 获取指定轮播项的状态 |
| `setTheme(theme)` | theme: String | Object | 设置主题样式 |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | Object | 切换暗黑模式 |
| `adjustLayout()` | 无 | Object | 调整响应式布局 |
| `enhanceAccessibility()` | 无 | Object | 增强可访问性支持 |
| `updateItemData(profile, item)` | profile: Object, item: Object | 无 | 更新轮播项数据 |

## 6. 事件处理

| 事件名 | 触发时机 | 参数说明 |
|--------|----------|----------|
| `onClick` | 点击轮播项时 | profile, item, e, src |
| `onItemHover` | 鼠标悬停在轮播项上时 | profile, item, e, src |
| `onItemLeave` | 鼠标离开轮播项时 | profile, item, e, src |
| `onItemFocus` | 轮播项获得焦点时 | profile, item, e, src |
| `onItemBlur` | 轮播项失去焦点时 | profile, item, e, src |
| `onItemDblClick` | 双击轮播项时 | profile, item, e, src |
| `onItemContextmenu` | 右键点击轮播项时 | profile, item, e, src |
| `onFlagClick` | 点击标记时 | profile, item, e, src |
| `swipe` | 滑动轮播项时 | profile, item, e, src |
| `swipeleft` | 向左滑动时 | profile, item, e, src |
| `swiperight` | 向右滑动时 | profile, item, e, src |
| `swipeup` | 向上滑动时 | profile, item, e, src |
| `swipedown` | 向下滑动时 | profile, item, e, src |

## 7. 响应式设计

Carousel 组件提供了完善的响应式设计支持：

- 自动检测屏幕尺寸并调整布局
- 小屏幕（<768px）添加 `gallery-mobile` 类
- 超小屏幕（<480px）添加 `gallery-tiny` 类并调整列数
- 支持触摸手势操作

## 8. 主题支持

Carousel 组件支持多种主题样式：

### 8.1 浅色主题（默认）

```javascript
carousel.setTheme('light');
```

### 8.2 深色主题

```javascript
carousel.setTheme('dark');
```

### 8.3 主题切换示例

```javascript
// 切换暗黑模式
carousel.toggleDarkMode();

// 获取当前主题
var currentTheme = carousel.getTheme();
```

## 9. 可访问性支持

Carousel 组件实现了丰富的可访问性支持：

- 为容器添加 `role="grid"` 和 `aria-label` 属性
- 为轮播项添加 `role="gridcell"` 和 `aria-label` 属性
- 支持键盘导航（左右箭头键切换，回车键/空格键激活）
- 为焦点项添加视觉反馈
- 为图标添加 `aria-hidden="true"` 属性

## 10. 使用示例

### 10.1 基础轮播图

```javascript
var basicCarousel = ood.gallery({
    items: [
        {id: 'img1', caption: '图片1', image: 'images/img1.jpg'},
        {id: 'img2', caption: '图片2', image: 'images/img2.jpg'},
        {id: 'img3', caption: '图片3', image: 'images/img3.jpg'}
    ],
    width: '100%',
    height: '400px',
    autoItemSize: true
});
```

### 10.2 带图标的轮播图

```javascript
var iconCarousel = ood.gallery({
    items: [
        {id: 'icon1', caption: '图标1', imageClass: 'ri-image-line'},
        {id: 'icon2', caption: '图标2', imageClass: 'ri-camera-line'},
        {id: 'icon3', caption: '图标3', imageClass: 'ri-video-line'}
    ],
    iconOnly: true,
    itemWidth: 64,
    itemHeight: 64
});
```

### 10.3 响应式轮播图

```javascript
var responsiveCarousel = ood.gallery({
    items: [/* 轮播项数据 */],
    responsive: true,
    autoColumns: true,
    width: '100%'
});
```

### 10.4 监听轮播项点击事件

```javascript
var interactiveCarousel = ood.gallery({
    items: [/* 轮播项数据 */],
    onClick: function(profile, item, e, src) {
        console.log('点击了轮播项:', item.id);
        // 执行自定义操作
    }
});
```

## 11. 高级配置

### 11.1 自定义样式

```javascript
var styledCarousel = ood.gallery({
    items: [/* 轮播项数据 */],
    itemMargin: 10,
    itemPadding: 5,
    backgroundColor: '#f5f5f5'
});
```

### 11.2 触摸手势支持

```javascript
var gestureCarousel = ood.gallery({
    items: [/* 轮播项数据 */],
    swipeleft: function(profile, item, e, src) {
        console.log('向左滑动');
        // 执行滑动操作
    },
    swiperight: function(profile, item, e, src) {
        console.log('向右滑动');
        // 执行滑动操作
    }
});
```

## 12. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 需特殊处理图片加载 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 部分支持 | 需使用兼容模式 |

## 13. 性能优化

- 图片懒加载：仅在需要时加载图片
- 事件委托：减少事件监听器数量
- 响应式图片：根据屏幕尺寸加载合适大小的图片
- CSS硬件加速：使用transform和opacity属性实现平滑过渡

## 14. 最佳实践

1. **合理设置轮播项数量**：建议不超过10个，避免性能问题
2. **优化图片大小**：确保图片经过压缩，减少加载时间
3. **提供清晰的视觉反馈**：为当前活动轮播项添加明显的样式
4. **支持多种交互方式**：同时支持触摸滑动、键盘导航和鼠标点击
5. **考虑可访问性**：确保键盘用户和屏幕阅读器用户能够正常使用
6. **测试不同设备**：在各种屏幕尺寸和浏览器上测试轮播图效果

## 15. 常见问题

### 15.1 轮播图不显示图片

**问题**：轮播图显示空白，图片不加载

**解决方案**：
- 检查图片路径是否正确
- 确保图片服务器允许跨域访问
- 检查浏览器控制台是否有错误信息
- 尝试设置 `autoImgSize: true`

### 15.2 轮播图滑动不流畅

**问题**：触摸滑动或键盘导航时不流畅

**解决方案**：
- 减少轮播项数量
- 优化图片大小和质量
- 避免在轮播图上添加过多动画效果
- 确保使用最新版本的浏览器

### 15.3 响应式布局不正常

**问题**：在小屏幕设备上布局错乱

**解决方案**：
- 确保设置了 `responsive: true`
- 检查CSS样式是否冲突
- 尝试调整 `itemWidth` 和 `itemHeight` 属性

## 16. 结语

Carousel 组件是一个功能强大、易于使用的轮播图解决方案，适用于各种网站和应用场景。它提供了丰富的配置选项和事件处理，支持响应式设计和多种主题样式，同时注重可访问性和性能优化。

通过合理配置和使用Carousel组件，可以创建出美观、交互友好的轮播图效果，提升用户体验和网站吸引力。