# StatusButtons 组件

StatusButtons（状态按钮）组件，继承自List组件，提供状态指示和选择的按钮组，支持主题切换、响应式设计和可访问性增强。常用于状态选择、过滤器等场景。

## 类名
`ood.UI.StatusButtons`

## 继承
`ood.UI.List`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/StatusButtons.js"></script>

<!-- 创建状态按钮组 -->
<div id="statusbuttons-container"></div>

<script>
var statusButtons = ood.UI.StatusButtons({
    items: [
        {id: 'a', caption: '状态1'},
        {id: 'b', caption: '状态2'},
        {id: 'c', caption: '状态3'},
        {id: 'd', caption: '状态4'}
    ],
    value: 'a',
    borderType: 'none',
    itemMargin: '2px 4px',
    itemWidth: '4em',
    width: '30em',
    theme: 'light',
    responsive: true,
    autoItemColor: true
}).appendTo('#statusbuttons-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[{id: 'a', caption: 'status 1'}, ...]` | 状态按钮项数组 |
| `borderType` | String | `'none'` | 边框类型 |
| `itemMargin` | String | `'2px 4px'` | 按钮项外边距 |
| `itemWidth` | String | `'4em'` | 按钮项宽度 |
| `width` | String | `'30em'` | 组件宽度 |
| `value` | String | `'a'` | 当前选中的按钮ID |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `theme` | String | `'light'` | 主题：'light'（明亮），'dark'（暗黑） |
| `responsive` | Boolean | `true` | 是否启用响应式设计 |
| `expression` | String | `''` | 表达式属性 |
| `menuType` | String | `'BOTTOMBAR'` | 菜单类型 |
| `iconColors` | Object | `null` | 图标颜色配置 |
| `itemColors` | Object | `null` | 按钮项颜色配置 |
| `fontColors` | Object | `{ini: '', type: "color", caption: "字体颜色"}` | 字体颜色配置 |
| `autoFontColor` | Boolean | `false` | 是否自动调整字体颜色 |
| `autoIconColor` | Boolean | `false` | 是否自动调整图标颜色 |
| `autoItemColor` | Boolean | `true` | 是否自动调整按钮项颜色 |
| `maxHeight` | Number | `null` | 最大高度 |
| `tagCmds` | String | `null` | 标签命令 |
| `height` | String | `'auto'` | 组件高度 |
| `align` | String | `'center'` | 对齐方式：'', 'left', 'center', 'right' |
| `itemMargin` | Object | `{ini: "", action: function(value){...}}` | 按钮项外边距配置 |
| `itemPadding` | Object | `{ini: "", action: function(v){...}}` | 按钮项内边距配置 |
| `itemWidth` | Object | `{$spaceunit: 1, ini: "auto", action: function(v){...}}` | 按钮项宽度配置 |
| `itemAlign` | Object | `{ini: "", listbox: ['', 'left', 'center', 'right'], action: function(value){...}}` | 按钮项对齐方式配置 |
| `itemType` | String | `'button'` | 按钮项类型：'text', 'button', 'dropButton' |
| `connected` | Boolean | `false` | 是否连接按钮 |

## 方法

### 主题控制方法

```javascript
// 设置主题
statusButtons.setTheme('dark');

// 获取当前主题
var currentTheme = statusButtons.getTheme();

// 切换所有主题（light → dark → high-contrast → light）
statusButtons.toggleTheme();

// 切换暗黑模式
statusButtons.toggleDarkMode();
```

### 布局调整方法

```javascript
// 调整响应式布局
statusButtons.adjustLayout();

// 增强可访问性
statusButtons.enhanceAccessibility();
```

### 继承自List的方法
StatusButtons组件继承了List组件的所有方法，包括：

- `getItem(id)` - 获取指定ID的按钮项
- `setValue(value)` - 设置当前选中的按钮
- `addItem(item)` - 添加新按钮项
- `removeItem(id)` - 移除按钮项
- `refresh()` - 刷新组件显示

## 事件

### 继承事件
继承自List组件的事件系统：

- `onItemClick` - 当按钮项被点击时触发
- `onValueChange` - 当选中值变化时触发
- `onCmd` - 当命令被执行时触发

### 自定义事件
```javascript
// 监听状态切换事件
statusButtons.on('valueChange', function(profile, newValue, oldValue) {
    console.log('状态切换:', oldValue, '->', newValue);
});
```

## 模板结构

StatusButtons组件基于List模板构建，主要包含以下子节点：

| 节点 | 描述 |
|------|------|
| `ITEMS` | 按钮项容器 |
| `ITEM` | 单个状态按钮 |
| `ICON` | 按钮图标 |
| `CAPTION` | 按钮文本 |
| `DROP` | 下拉箭头（用于dropButton类型） |
| `FLAG` | 标记标识 |

## 响应式设计

StatusButtons组件支持响应式布局，自动适应不同屏幕尺寸：

- **桌面端**：完整显示所有按钮项
- **平板端**（< 768px）：调整按钮大小、字体和间距
- **手机端**（< 480px）：简化显示，长文本自动隐藏

## 可访问性特性

- 为容器添加`role="toolbar"`和`aria-label="状态按钮工具栏"`
- 为按钮项添加适当的ARIA角色和属性
- 支持键盘导航（Tab、Enter、Space）
- 为屏幕阅读器提供语义化标签
- 高对比度主题支持

## 主题样式

### Light 主题（默认）
浅色背景，适合大多数应用场景。

### Dark 主题
深色背景，减少视觉疲劳，适合夜间使用。

### High-Contrast 主题
高对比度配色，提高可读性，适合视力障碍用户。

## 示例

### 基本状态选择
```javascript
var statusButtons = ood.UI.StatusButtons({
    items: [
        {id: 'active', caption: '活跃'},
        {id: 'inactive', caption: '非活跃'},
        {id: 'pending', caption: '待处理'},
        {id: 'completed', caption: '已完成'}
    ],
    value: 'active'
});
```

### 带图标的按钮
```javascript
var statusButtons = ood.UI.StatusButtons({
    items: [
        {id: 'success', caption: '成功', imageClass: 'ri-checkbox-circle-line'},
        {id: 'warning', caption: '警告', imageClass: 'ri-alert-line'},
        {id: 'error', caption: '错误', imageClass: 'ri-close-circle-line'},
        {id: 'info', caption: '信息', imageClass: 'ri-information-line'}
    ],
    autoItemColor: true
});
```

### 连接按钮组
```javascript
var statusButtons = ood.UI.StatusButtons({
    items: [
        {id: 'low', caption: '低'},
        {id: 'medium', caption: '中'},
        {id: 'high', caption: '高'}
    ],
    connected: true,
    borderType: 'flat'
});
```

## 注意事项

1. StatusButtons组件继承自List，共享大部分特性和方法
2. 自动颜色功能可根据按钮状态应用不同的背景色和文字颜色
3. 连接按钮组（`connected: true`）会移除按钮间的间距，形成连续的外观
4. 响应式设计需要容器支持大小变化事件
5. 按钮类型`dropButton`会显示下拉箭头，适合需要下拉菜单的场景