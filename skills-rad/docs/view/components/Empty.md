# Empty 空状态组件

## 1. 组件概述

Empty 是空状态占位组件，用于在数据为空或操作无结果时，向用户提供友好的提示信息和引导操作。

**核心功能：**
- 支持自定义图标、标题和描述文本
- 提供多种预设空状态类型
- 支持添加操作按钮
- 响应式设计，适配不同屏幕尺寸
- 支持多种主题样式
- 完善的可访问性支持

## 2. 核心概念

### 2.1 组件结构

Empty 组件采用简洁的结构设计：

```
Empty
├── Icon (可选，空状态图标)
├── Title (标题文本)
├── Description (描述文本)
└── Action (可选，操作按钮)
```

### 2.2 数据模型

Empty 使用对象形式的数据模型来定义空状态：

```javascript
emptyConfig: {
    type: 'default',         // 空状态类型
    title: '暂无数据',       // 标题
    description: '数据加载中或暂无数据', // 描述
    iconClass: 'ri-file-search-line', // 图标类名
    actions: [              // 操作按钮
        {
            text: '刷新',
            onClick: function() { /* 刷新操作 */ }
        }
    ]
}
```

## 3. 创建方式

### 3.1 静态创建（HTML）

```html
<div id="myEmpty" class="ood-ui-empty" data-config="{
    'type': 'default',
    'title': '暂无数据',
    'description': '数据加载中或暂无数据',
    'iconClass': 'ri-file-search-line',
    'actions': [{
        'text': '刷新',
        'onClick': 'refreshData()'
    }]
}"></div>
```

### 3.2 动态创建（JavaScript）

```javascript
var empty = ood("#myEmpty").empty({
    type: 'default',
    title: '暂无数据',
    description: '数据加载中或暂无数据',
    iconClass: 'ri-file-search-line',
    actions: [
        {
            text: '刷新',
            onClick: function() {
                console.log('刷新数据');
                // 执行刷新操作
            }
        }
    ]
});
```

### 3.3 工厂方法创建

```javascript
var empty = ood.empty({
    type: 'search',
    title: '暂无搜索结果',
    description: '未找到相关内容，请尝试其他关键词',
    iconClass: 'ri-search-line'
});

// 添加到页面
ood("#container").append(empty);
```

## 4. 属性说明

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `type` | String | 'default' | 空状态类型：'default', 'search', 'data', 'network', 'permission', '404', '403', '500' |
| `title` | String | '暂无数据' | 标题文本 |
| `description` | String | '' | 描述文本 |
| `iconClass` | String | '' | 图标类名 |
| `iconSize` | String | '64px' | 图标大小 |
| `image` | String | '' | 自定义图片路径 |
| `actions` | Array | [] | 操作按钮列表 |
| `theme` | String | 'light' | 主题样式：'light', 'dark' |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `width` | String/Number | 'auto' | 组件宽度 |
| `height` | String/Number | 'auto' | 组件高度 |

## 5. 方法列表

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| `setType(type)` | type: String | Object | 设置空状态类型 |
| `setTitle(title)` | title: String | Object | 设置标题文本 |
| `setDescription(description)` | description: String | Object | 设置描述文本 |
| `setIcon(iconClass)` | iconClass: String | Object | 设置图标 |
| `setImage(image)` | image: String | Object | 设置自定义图片 |
| `addAction(action)` | action: Object | Object | 添加操作按钮 |
| `removeAction(index)` | index: Number | Object | 移除操作按钮 |
| `setTheme(theme)` | theme: String | Object | 设置主题样式 |
| `getTheme()` | 无 | String | 获取当前主题 |
| `toggleDarkMode()` | 无 | Object | 切换暗黑模式 |
| `adjustLayout()` | 无 | Object | 调整响应式布局 |
| `enhanceAccessibility()` | 无 | Object | 增强可访问性支持 |

## 6. 事件处理

| 事件名 | 触发时机 | 参数说明 |
|--------|----------|----------|
| `onActionClick` | 点击操作按钮时 | action: Object, index: Number, e: Event |
| `onThemeChange` | 主题变化时 | theme: String |

## 7. 响应式设计

Empty 组件提供了完善的响应式设计支持：

- 自动检测屏幕尺寸并调整布局
- 小屏幕（<768px）自动调整图标大小和文本位置
- 超小屏幕（<480px）优化显示效果
- 支持触摸手势操作

## 8. 主题支持

Empty 组件支持多种主题样式：

### 8.1 浅色主题（默认）

```javascript
empty.setTheme('light');
```

### 8.2 深色主题

```javascript
empty.setTheme('dark');
```

### 8.3 主题切换

```javascript
// 切换暗黑模式
empty.toggleDarkMode();

// 获取当前主题
var currentTheme = empty.getTheme();
```

## 9. 可访问性支持

Empty 组件实现了丰富的可访问性支持：

- 为容器添加 `role="status"` 和 `aria-label` 属性
- 为图标添加 `aria-hidden="true"` 属性
- 支持键盘导航
- 为焦点元素添加视觉反馈
- 为图片添加 `alt` 属性

## 10. 使用示例

### 10.1 基础空状态

```javascript
var basicEmpty = ood.empty({
    type: 'default',
    title: '暂无数据',
    description: '数据加载中或暂无数据'
});
```

### 10.2 搜索空状态

```javascript
var searchEmpty = ood.empty({
    type: 'search',
    title: '暂无搜索结果',
    description: '未找到相关内容，请尝试其他关键词',
    iconClass: 'ri-search-line',
    actions: [
        {
            text: '重新搜索',
            onClick: function() {
                console.log('重新搜索');
                // 执行重新搜索操作
            }
        }
    ]
});
```

### 10.3 网络错误空状态

```javascript
var networkEmpty = ood.empty({
    type: 'network',
    title: '网络连接失败',
    description: '请检查您的网络连接后重试',
    iconClass: 'ri-wifi-off-line',
    actions: [
        {
            text: '重试',
            onClick: function() {
                console.log('重试网络连接');
                // 执行重试操作
            }
        }
    ]
});
```

### 10.4 权限错误空状态

```javascript
var permissionEmpty = ood.empty({
    type: 'permission',
    title: '无权限访问',
    description: '您没有权限访问此内容，请联系管理员',
    iconClass: 'ri-lock-line',
    actions: [
        {
            text: '联系管理员',
            onClick: function() {
                console.log('联系管理员');
                // 执行联系管理员操作
            }
        }
    ]
});
```

## 11. 高级配置

### 11.1 自定义样式

```javascript
var styledEmpty = ood.empty({
    type: 'default',
    title: '暂无数据',
    description: '数据加载中或暂无数据',
    iconClass: 'ri-file-search-line',
    style: {
        backgroundColor: '#f0f8ff',
        borderRadius: '8px',
        padding: '20px'
    },
    titleStyle: {
        color: '#333',
        fontSize: '18px',
        fontWeight: 'bold'
    },
    descriptionStyle: {
        color: '#666',
        fontSize: '14px'
    }
});
```

### 11.2 动态更新配置

```javascript
var dynamicEmpty = ood.empty({
    type: 'default',
    title: '暂无数据',
    description: '数据加载中或暂无数据'
});

// 动态更新配置
function updateEmptyConfig() {
    var empty = ood("#myEmpty").get(0);
    empty.boxing().setType('search');
    empty.boxing().setTitle('暂无搜索结果');
    empty.boxing().setDescription('未找到相关内容，请尝试其他关键词');
    empty.boxing().setIcon('ri-search-line');
}

// 执行动态更新
updateEmptyConfig();
```

### 11.3 多种空状态类型

```javascript
// 数据为空
var dataEmpty = ood.empty({
    type: 'data',
    title: '暂无数据',
    description: '暂无相关数据，敬请期待'
});

// 404错误
var notFoundEmpty = ood.empty({
    type: '404',
    title: '页面不存在',
    description: '您访问的页面不存在或已被删除',
    actions: [
        {
            text: '返回首页',
            onClick: function() {
                window.location.href = '/';
            }
        }
    ]
});

// 500错误
var serverErrorEmpty = ood.empty({
    type: '500',
    title: '服务器错误',
    description: '服务器内部错误，请稍后重试',
    actions: [
        {
            text: '重试',
            onClick: function() {
                location.reload();
            }
        }
    ]
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

- 延迟加载：仅在需要时渲染空状态
- 缓存空状态配置：避免重复创建
- 简化DOM结构：减少不必要的嵌套
- 优化图片加载：使用适当大小的图标和图片

## 14. 最佳实践

1. **提供清晰的提示信息**：使用简洁明了的标题和描述，让用户了解当前状态
2. **提供有用的操作按钮**：根据空状态类型提供相关的操作按钮
3. **使用合适的图标**：选择与空状态类型匹配的图标，增强视觉传达
4. **保持一致的设计风格**：与整个应用的设计风格保持一致
5. **考虑响应式设计**：确保在不同屏幕尺寸下正常显示
6. **优化可访问性**：确保键盘用户和屏幕阅读器用户能够正常使用

## 15. 常见问题

### 15.1 空状态不显示

**问题**：创建空状态后，页面上没有显示

**解决方案**：
- 检查是否正确初始化了组件
- 确保容器元素存在且可见
- 检查CSS样式是否冲突

### 15.2 操作按钮不响应点击

**问题**：点击操作按钮，没有执行对应的函数

**解决方案**：
- 检查onClick函数是否正确定义
- 确保函数在正确的作用域内
- 检查事件绑定是否成功

### 15.3 主题切换不生效

**问题**：调用setTheme方法后，主题没有变化

**解决方案**：
- 确保主题名称正确：'light', 'dark'
- 检查CSS样式是否正确加载

## 16. 结语

Empty 组件是一个功能强大、易于使用的空状态解决方案，适用于各种数据为空或操作无结果的场景。它提供了丰富的配置选项和事件处理，支持响应式设计和多种主题样式，同时注重可访问性和性能优化。

通过合理配置和使用Empty组件，可以创建出美观、友好的空状态提示，提升用户体验和应用的整体品质。