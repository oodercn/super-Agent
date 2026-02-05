# PageBar 组件

PageBar 是一个现代化的分页导航组件，支持多种主题、响应式布局和可访问性特性。

## 基本用法

```javascript
// 创建 PageBar 实例
var pageBar = ood('div').pageBar({
    caption: '页码：',
    pageCount: 10,
    value: '1:1:50'  // 格式：最小页码:当前页码:最大页码
});

// 添加到页面
pageBar.appendTo('body');
```

## 属性

### 核心属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `caption` | String | `' Page: '` | 分页标签文本 |
| `pageCount` | Number | `20` | 每页显示的记录数 |
| `value` | String | `'1:1:1'` | 分页值，格式为 `最小页码:当前页码:最大页码` |
| `uriTpl` | String | `'#*'` | 链接模板，`*` 会被替换为页码 |
| `textTpl` | String | `'*'` | 文本模板，`*` 会被替换为页码 |
| `prevMark` | String | `''` | 上一页按钮文本 |
| `nextMark` | String | `''` | 下一页按钮文本 |
| `showMoreBtns` | Boolean | `true` | 是否显示更多按钮（...） |

### 现代化属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `theme` | String | `'light'` | 主题样式，可选值：`'light'`, `'dark'` |
| `responsive` | Boolean | `true` | 是否启用响应式布局 |

### 状态属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `disabled` | Boolean | `false` | 是否禁用组件 |
| `readonly` | Boolean | `false` | 是否只读模式 |
| `hiddenBar` | Boolean | `false` | 是否隐藏分页栏 |

### 数据绑定属性

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `dataField` | String | `null` | 数据字段名 |
| `dataBinder` | Object | `null` | 数据绑定器 |
| `autoTips` | Boolean | `false` | 是否自动显示提示 |
| `dirtyMark` | Boolean | `false` | 是否显示脏数据标记 |
| `showDirtyMark` | Boolean | `false` | 是否显示脏数据标记 |
| `parentID` | String | `''` | 父组件ID |

## 方法

### 分页控制

```javascript
// 设置当前页码
pageBar.setPage(5);

// 获取当前页码
var currentPage = pageBar.getPage();

// 获取总页数
var totalPages = pageBar.getTotalPages();

// 设置总记录数
pageBar.setTotalCount(1000);
```

### 主题控制

```javascript
// 设置主题
pageBar.setTheme('dark');

// 获取当前主题
var theme = pageBar.getTheme();

// 切换主题（light → dark → high-contrast → light）
pageBar.toggleTheme();

// 切换暗黑模式
pageBar.toggleDarkMode();
```

### 布局和可访问性

```javascript
// 调整响应式布局
pageBar.adjustLayout();

// 增强可访问性
pageBar.enhanceAccessibility();
```

### API 绑定

```javascript
// 绑定外部 API
pageBar.buindAPI({
    getData: function(page) {
        // 获取指定页数据
    }
});
```

## 事件

### onClick
当分页按钮被点击时触发。

```javascript
pageBar.onClick = function(profile, page) {
    console.log('点击页码：', page);
};
```

### onPageSet
当页码被设置时触发。

```javascript
pageBar.onPageSet = function(profile, page, start, count, eventType, opage, ostart) {
    console.log('页码变化：', opage, '→', page);
    console.log('数据起始位置：', ostart, '→', start);
    console.log('事件类型：', eventType);
};
```

## 模板结构

PageBar 包含以下子节点：

| 节点 | 类型 | 描述 |
|------|------|------|
| `LABEL` | 标签 | 显示分页标签文本 |
| `FIRST` | 按钮 | 第一页按钮 |
| `PREM` | 按钮 | 前面更多页按钮（...） |
| `PREV` | 按钮 | 上一页按钮 |
| `CUR` | 输入框 | 当前页码输入框 |
| `NEXT` | 按钮 | 下一页按钮 |
| `NEXTM` | 按钮 | 后面更多页按钮（...） |
| `LAST` | 按钮 | 最后一页按钮 |
| `POOL` | 容器 | 弹出菜单容器 |

## 响应式设计

PageBar 自动适应不同屏幕尺寸：

- **桌面端**：完整显示所有按钮
- **平板端**（< 768px）：调整按钮内边距和字体大小
- **手机端**（< 480px）：进一步优化显示，简化按钮

## 可访问性特性

- 为容器添加 `role="navigation"` 和 `aria-label="分页导航"`
- 为所有按钮添加适当的 ARIA 标签
- 为输入框添加 `role="spinbutton"` 和相关属性
- 支持键盘导航和屏幕阅读器

## 主题样式

### Light 主题（默认）
浅色背景，适合大多数应用场景。

### Dark 主题
深色背景，减少视觉疲劳，适合夜间使用。

### High-Contrast 主题
高对比度配色，提高可读性，适合视力障碍用户。

## 示例

```javascript
// 完整示例
var pageBar = ood('div').pageBar({
    caption: '第 ',
    pageCount: 20,
    value: '1:1:100',
    theme: 'dark',
    responsive: true,
    showMoreBtns: true
});

// 事件处理
pageBar.onPageSet = function(profile, page, start, count, eventType) {
    // 加载对应页数据
    loadData(start, count);
};

// 添加到页面
pageBar.appendTo('#container');

// 设置总记录数（自动计算总页数）
pageBar.setTotalCount(2000);
```

## 注意事项

1. 分页值格式必须为 `最小页码:当前页码:最大页码`
2. 当 `disabled` 或 `readonly` 为 `true` 时，输入框将变为只读
3. 主题设置会自动保存到本地存储
4. 响应式布局在窗口大小变化时可能需要手动调用 `adjustLayout()`