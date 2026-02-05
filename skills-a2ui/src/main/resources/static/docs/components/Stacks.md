# Stacks 组件

Stacks（堆叠面板）组件，继承自Tabs组件，提供可折叠、堆叠式的面板容器，支持主题切换、响应式设计和可访问性增强。常用于需要分层显示内容的场景。

## 类名
`ood.UI.Stacks`

## 继承
`ood.UI.Tabs`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/Stacks.js"></script>

<!-- 创建堆叠面板 -->
<div id="stacks-container"></div>

<script>
var stacks = ood.UI.Stacks({
    items: [
        {id: 'a', caption: '面板1', message: "普通面板"},
        {id: 'b', caption: '面板2', message: "带图标", imageClass: "ri-image-line"},
        {id: 'c', caption: '面板3', message: "自定义高度", height: 100},
        {
            id: 'd',
            caption: '高级面板',
            message: "带控制按钮",
            closeBtn: true,
            optBtn: 'ood-uicmd-opt',
            popBtn: true
        }
    ],
    value: 'a',
    caption: '堆叠面板',
    autoItemColor: true,
    borderType: 'flat',
    theme: 'light',
    responsive: true
}).appendTo('#stacks-container');
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `items` | Array | `[{id: 'a', caption: 'tab1', message: "normal"}, ...]` | 面板项数组，包含id、caption、message、imageClass等属性 |
| `autoItemColor` | Boolean | `true` | 是否自动应用项目颜色 |
| `value` | String | `'a'` | 当前选中的面板ID |
| `caption` | String | `'$RAD.widgets.folding'` | 组件标题 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `expression` | String | `''` | 表达式属性 |
| `noPanel` | Object | `null` | 无面板配置 |
| `noHandler` | Object | `null` | 无处理器配置 |
| `selMode` | Object | `null` | 选择模式配置 |
| `borderType` | String | `'flat'` | 边框类型：'none'（无边框），'flat'（扁平），'inset'（内凹），'outset'（外凸） |

## 方法

### 继承自Tabs的方法
Stacks组件继承了Tabs组件的所有方法，包括：

- `getPanel(key)` - 获取指定ID的面板
- `setValue(value)` - 设置当前选中的面板
- `addItem(item)` - 添加新面板项
- `removeItem(id)` - 移除面板项
- `showPanel(id)` - 显示指定面板
- `hidePanel(id)` - 隐藏指定面板

### 布局调整方法
```javascript
// 调整布局以响应容器大小变化
stacks._onresize(profile, width, height, force, key);
```

## 事件

### 继承事件
继承自Tabs组件的事件系统：

- `onItemClick` - 当面板项被点击时触发
- `onValueChange` - 当选中值变化时触发
- `onPanelShow` - 当面板显示时触发
- `onPanelHide` - 当面板隐藏时触发

### 自定义事件
```javascript
// 监听面板切换事件
stacks.on('valueChange', function(profile, newValue, oldValue) {
    console.log('面板切换:', oldValue, '->', newValue);
});
```

## 模板结构

Stacks组件基于Tabs模板构建，主要包含以下子节点：

| 节点 | 描述 |
|------|------|
| `BOX` | 主容器，包含LIST和PNAELS |
| `LIST` | 面板列表容器 |
| `PNAELS` | 面板内容容器 |
| `ITEM` | 单个面板项 |
| `PANEL` | 单个面板内容区域 |

## 响应式设计

Stacks组件支持响应式布局，自动适应不同屏幕尺寸：

- **桌面端**：完整显示所有面板项
- **平板端**：优化面板项间距和字体大小
- **手机端**：简化显示，优化触摸操作

## 可访问性特性

- 为容器添加适当的ARIA角色和属性
- 支持键盘导航（Tab、Enter、Space）
- 为屏幕阅读器提供语义化标签
- 高对比度主题支持

## 主题样式

### Light 主题（默认）
浅色背景，适合大多数应用场景。

### Dark 主题
深色背景，减少视觉疲劳，适合夜间使用。

### High-Conttrast 主题
高对比度配色，提高可读性，适合视力障碍用户。

## 示例

### 基本示例
```javascript
var stacks = ood.UI.Stacks({
    items: [
        {id: 'panel1', caption: '基本信息'},
        {id: 'panel2', caption: '详细信息'},
        {id: 'panel3', caption: '设置选项'}
    ],
    value: 'panel1',
    borderType: 'flat'
});
```

### 动态添加面板
```javascript
// 添加新面板
stacks.addItem({
    id: 'newPanel',
    caption: '新增面板',
    message: '动态添加的面板',
    imageClass: 'ri-add-circle-line'
});

// 切换到新面板
stacks.setValue('newPanel');
```

### 主题切换
```javascript
// 切换到暗黑主题
stacks.setTheme('dark');

// 切换主题
stacks.toggleDarkMode();
```

## 注意事项

1. Stacks组件继承自Tabs，共享大部分特性和方法
2. 面板项的高度可以自定义，通过`height`属性设置
3. 边框类型影响组件的视觉样式和边框宽度计算
4. 自动颜色功能可根据面板内容应用不同的背景色
5. 响应式设计需要容器支持大小变化事件