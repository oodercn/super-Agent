# Collapse 折叠面板组件

## 1. 组件概述

Collapse 是一个可折叠的面板组件，用于在有限空间内展示和隐藏内容，支持手风琴效果、多级嵌套、主题切换和响应式设计。

**核心功能：**
- 支持单个或多个面板同时展开
- 手风琴模式，同一时间只展开一个面板
- 平滑过渡动画效果
- 支持多级嵌套使用
- 响应式设计，适配不同屏幕尺寸
- 支持多种主题样式（浅色、深色、高对比度）
- 完善的可访问性支持

## 2. 核心概念

### 2.1 组件结构

Collapse 组件采用层级化结构设计：

```
Collapse
├── Item (折叠项)
│   ├── Head (头部，可点击)
│   │   ├── Toggle (折叠图标)
│   │   └── Title (标题文本)
│   └── Body (内容区域，可折叠)
│       └── BodyI (内容容器)
```

### 2.2 数据模型

Collapse 使用数组形式的数据模型来定义折叠项：

```javascript
items: [
    {
        id: 'item1',           // 唯一标识
        caption: '标题1',       // 副标题
        title: '主标题1',       // 主标题
        text: '折叠内容1'       // 折叠内容
    },
    // 更多折叠项...
]
```

## 3. 创建方式

### 3.1 静态创建（HTML）

```html
<div id="myCollapse" class="ood-ui-foldinglist" data-items="[
    {id: 'a', caption: '折叠项1', title: '主标题1', text: '折叠内容1'},
    {id: 'b', caption: '折叠项2', title: '主标题2', text: '折叠内容2'},
    {id: 'c', caption: '折叠项3', title: '主标题3', text: '折叠内容3'}
]"></div>
```

### 3.2 动态创建（JavaScript）

```javascript
var collapse = ood("#myCollapse").foldinglist({
    items: [
        {id: 'a', caption: '折叠项1', title: '主标题1', text: '折叠内容1'},
        {id: 'b', caption: '折叠项2', title: '主标题2', text: '折叠内容2'},
        {id: 'c', caption: '折叠项3', title: '主标题3', text: '折叠内容3'}
    ],
    activeLast: true,
    theme: 'light'
});
```

### 3.3 工厂方法创建

```javascript
var collapse = ood.foldinglist({
    items: [/* 折叠项数据 */],
    width: '100%'
});

// 添加到页面
ood("#container").append(collapse);
```

## 4. 属性说明

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `items` | Array | [] | 折叠项数据数组 |
| `value` | String | null | 当前选中的折叠项ID |
| `borderType` | String | null | 边框类型 |
| `activeLast` | Boolean | true | 是否默认展开最后一个折叠项 |
| `transitionDuration` | String | 'normal' | 过渡动画时长：'fast', 'normal', 'slow' |
| `collapsible` | Boolean | true | 是否可折叠 |
| `theme` | String | 'light' | 主题样式：'light', 'dark', 'high-contrast' |
| `responsive` | Boolean | true | 是否启用响应式设计 |

## 5. 方法列表

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| `setTheme(theme)` | theme: String | Object | 设置主题样式 |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleTheme()` | 无 | Object | 切换主题 |
| `fillContent(id, obj)` | id: String, obj: String/Object | Object | 填充指定折叠项的内容 |
| `toggle(id)` | id: String | Object | 切换指定折叠项的展开/折叠状态 |

## 6. 事件处理

| 事件名 | 触发时机 | 参数说明 |
|--------|----------|----------|
| `onClick` | 点击折叠项时 | profile, e, src |
| `onKeydown` | 按下键盘时 | profile, e, src |
| `onGetContent` | 获取折叠项内容时 | profile, item, callback |
| `onShowOptions` | 显示选项时 | profile, item, e, src |

## 7. 响应式设计

Collapse 组件提供了完善的响应式设计支持：

- 自动检测屏幕尺寸并调整布局
- 针对不同设备优化交互体验
- 支持触摸手势操作
- 适配移动端和桌面端

## 8. 主题支持

Collapse 组件支持多种主题样式：

### 8.1 浅色主题（默认）

```javascript
collapse.setTheme('light');
```

### 8.2 深色主题

```javascript
collapse.setTheme('dark');
```

### 8.3 高对比度主题

```javascript
collapse.setTheme('high-contrast');
```

### 8.4 主题切换

```javascript
// 切换到下一个主题
collapse.toggleTheme();

// 获取当前主题
var currentTheme = collapse.getTheme();
```

## 9. 可访问性支持

Collapse 组件实现了丰富的可访问性支持：

- 为容器添加 `role="tree"` 和 `aria-label` 属性
- 为折叠项添加 `role="treeitem"` 和 `aria-expanded` 属性
- 支持键盘导航
- 为焦点元素添加视觉反馈
- 适配屏幕阅读器

## 10. 使用示例

### 10.1 基础折叠面板

```javascript
var basicCollapse = ood.foldinglist({
    items: [
        {id: 'item1', title: '折叠项1', text: '这是第一个折叠项的内容'},
        {id: 'item2', title: '折叠项2', text: '这是第二个折叠项的内容'},
        {id: 'item3', title: '折叠项3', text: '这是第三个折叠项的内容'}
    ],
    width: '100%'
});
```

### 10.2 手风琴模式

```javascript
var accordionCollapse = ood.foldinglist({
    items: [/* 折叠项数据 */],
    activeLast: true
});
```

### 10.3 自定义主题

```javascript
var themedCollapse = ood.foldinglist({
    items: [/* 折叠项数据 */],
    theme: 'dark'
});
```

### 10.4 动态填充内容

```javascript
var dynamicCollapse = ood.foldinglist({
    items: [
        {id: 'item1', title: '动态内容', text: '点击展开加载动态内容'}
    ],
    onGetContent: function(profile, item, callback) {
        // 模拟异步加载
        setTimeout(function() {
            callback('<div class="dynamic-content">这是异步加载的动态内容</div>');
        }, 1000);
    }
});
```

## 11. 高级配置

### 11.1 多级嵌套

```javascript
var nestedCollapse = ood.foldinglist({
    items: [
        {id: 'parent1', title: '父折叠项1', text: '<div id="childCollapse1"></div>'},
        {id: 'parent2', title: '父折叠项2', text: '<div id="childCollapse2"></div>'}
    ]
});

// 初始化子折叠面板
ood("#childCollapse1").foldinglist({
    items: [
        {id: 'child1-1', title: '子折叠项1-1', text: '子内容1-1'},
        {id: 'child1-2', title: '子折叠项1-2', text: '子内容1-2'}
    ]
});
```

### 11.2 自定义过渡动画

```javascript
var animatedCollapse = ood.foldinglist({
    items: [/* 折叠项数据 */],
    transitionDuration: 'slow'
});
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

- 延迟加载：只在需要时加载折叠内容
- 事件委托：减少事件监听器数量
- 虚拟滚动：支持大量折叠项时的性能优化
- CSS硬件加速：使用transform和opacity属性实现平滑过渡

## 14. 最佳实践

1. **合理设置折叠项数量**：建议不超过10个，避免页面过长
2. **优化内容加载**：对于大量内容，使用异步加载
3. **提供清晰的视觉反馈**：为当前展开的折叠项添加明显的样式
4. **支持键盘导航**：确保键盘用户能够正常使用
5. **考虑可访问性**：确保屏幕阅读器用户能够理解折叠面板的结构
6. **测试不同设备**：在各种屏幕尺寸和浏览器上测试折叠面板效果

## 15. 常见问题

### 15.1 折叠面板无法正常展开/折叠

**问题**：点击折叠项头部，内容区域没有变化

**解决方案**：
- 检查是否正确初始化了组件
- 确保items数组中的id唯一
- 检查CSS样式是否冲突

### 15.2 主题切换不生效

**问题**：调用setTheme方法后，主题没有变化

**解决方案**：
- 确保主题名称正确：'light', 'dark', 'high-contrast'
- 检查CSS样式是否正确加载

### 15.3 响应式设计不正常

**问题**：在小屏幕设备上布局错乱

**解决方案**：
- 确保设置了 `responsive: true`
- 检查CSS样式是否冲突
- 避免使用固定宽度，尽量使用百分比

## 16. 结语

Collapse 组件是一个功能强大、易于使用的折叠面板解决方案，适用于各种网站和应用场景。它提供了丰富的配置选项和事件处理，支持响应式设计和多种主题样式，同时注重可访问性和性能优化。

通过合理配置和使用Collapse组件，可以创建出美观、交互友好的折叠面板效果，提升用户体验和网站吸引力。