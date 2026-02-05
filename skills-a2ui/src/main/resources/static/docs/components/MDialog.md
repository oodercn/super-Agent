# MDialog

移动端对话框组件，专为触摸设备优化，支持模态/非模态显示、最大化/最小化、拖拽移动、调整大小、主题切换和可访问性增强。提供丰富的动画效果和交互功能，适用于移动端应用的各种弹窗场景。

## 类名
`ood.UI.MDialog`

## 继承
`ood.UI.Widget`

## 快速开始

```html
<!-- 引入组件 -->
<script type="text/javascript" src="ood/UI/MDialog.js"></script>

<!-- 创建对话框容器 -->
<div id="dialog-container"></div>

<script>
var dialog = ood.UI.MDialog({
    caption: '移动端对话框',
    width: '26.5em',
    height: '45.5em',
    theme: 'light',
    responsive: true,
    modal: false,
    movable: true,
    resizer: true,
    shadow: true,
    showEffects: 'Flip H',
    hideEffects: 'Flip H'
}).appendTo('#dialog-container');

// 显示对话框
dialog.show();
</script>
```

## 属性

### 初始化属性 (iniProp)

| 属性名 | 类型 | 默认值 | 描述 |
|--------|------|--------|------|
| `dock` | String | `'height'` | 停靠方向 |
| `showEffects` | String | `'Flip H'` | 显示动画效果 |
| `hideEffects` | String | `'Flip H'` | 隐藏动画效果 |
| `width` | String | `'26.515151515151512em'` | 对话框宽度 |
| `height` | String | `'45.45454545454545em'` | 对话框高度 |
| `caption` | String | `'弹出框'` | 对话框标题 |
| `minWidth` | String | `'350'` | 最小宽度 |
| `status` | String | `'right'` | 初始状态 |
| `panelBgClr` | String | `'#FFFFFF'` | 面板背景颜色 |

### 数据模型属性 (DataModel)

| 属性名 | 类型 | 默认值 | 描述 | 可选值/备注 |
|--------|------|--------|------|-------------|
| `rotate` | Any | `null` | 旋转设置 | - |
| `selectable` | Boolean | `true` | 是否可选择文本 | - |
| `tips` | String | `null` | 提示信息 | - |
| `border` | String | `null` | 边框样式 | - |
| `disabled` | Boolean | `null` | 是否禁用 | - |
| `dock` | String | `'none'` | 停靠方式 | - |
| `showEffects` | String | `'Classic'` | 显示动画效果 | - |
| `hideEffects` | String | `''` | 隐藏动画效果 | - |
| `initPos` | String | `'center'` | 初始位置 | `'auto'`, `'center'` |
| `iframeAutoLoad` | String | `''` | iframe自动加载URL | - |
| `ajaxAutoLoad` | String | `''` | Ajax自动加载URL | - |
| `html` | String | - | HTML内容 | `html: 1` |
| `caption` | String | `undefined` | 对话框标题 | - |
| `image` | Image | - | 标题图标 | 格式为 `image` |
| `imagePos` | String | - | 图标位置 | CSS `background-position` |
| `imageBgSize` | String | - | 图标背景大小 | CSS `background-size` |
| `imageClass` | String | `''` | 图标类名 | - |
| `iconFontCode` | String | - | 图标字体代码 | - |
| `shadow` | Boolean | `true` | 是否显示阴影 | - |
| `resizer` | Boolean | `true` | 是否可调整大小 | - |
| `movable` | Boolean | `true` | 是否可移动 | - |
| `minBtn` | Boolean | `true` | 是否显示最小化按钮 | - |
| `maxBtn` | Boolean | `true` | 是否显示最大化按钮 | - |
| `restoreBtn` | Boolean | `true` | 是否显示恢复按钮 | - |
| `infoBtn` | Boolean | `false` | 是否显示信息按钮 | - |
| `optBtn` | Boolean | `false` | 是否显示选项按钮 | - |
| `closeBtn` | Boolean | `true` | 是否显示关闭按钮 | - |
| `refreshBtn` | Boolean | `false` | 是否显示刷新按钮 | - |
| `pinBtn` | Boolean | `false` | 是否显示固定按钮 | - |
| `landBtn` | Boolean | `false` | 是否显示横屏按钮 | - |
| `displayBar` | Boolean | `false` | 是否显示标题栏 | - |
| `width` | String | `'25em'` | 对话框宽度 | `$spaceunit: 1` |
| `height` | String | `'25em'` | 对话框高度 | `$spaceunit: 1` |
| `minWidth` | Number | `200` | 最小宽度 | - |
| `minHeight` | Number | `100` | 最小高度 | - |
| `position` | String | `'absolute'` | 定位方式 | - |
| `fromRegion` | Object | `null` | 来源区域（用于动画） | - |
| `modal` | Boolean | `false` | 是否模态对话框 | - |
| `status` | String | `'normal'` | 对话框状态 | `'normal'`, `'min'`, `'max'`, `'left'`, `'right'` |
| `hAlign` | String | `'left'` | 标题水平对齐 | `'left'`, `'center'`, `'right'` |
| `tagCmds` | Array | `[]` | 标签命令数组 | - |

## 实例方法

### `setChildren(childrens, prf)`
设置对话框内容子组件。

| 参数 | 类型 | 描述 |
|------|------|------|
| `childrens` | Array | 子组件数组 |
| `prf` | Object | 父组件实例（可选） |

### `showModal(parent, left, top, callback, ignoreEffects)`
以模态方式显示对话框。

| 参数 | 类型 | 描述 |
|------|------|------|
| `parent` | Object | 父容器，默认 `ood('body')` |
| `left` | Number | 左侧位置（像素） |
| `top` | Number | 顶部位置（像素） |
| `callback` | Function | 显示完成后的回调函数 |
| `ignoreEffects` | Boolean | 是否忽略动画效果 |

**返回值**: `this`（支持链式调用）

### `show(parent, modal, left, top, callback, ignoreEffects)`
显示对话框（可指定是否为模态）。

| 参数 | 类型 | 描述 |
|------|------|------|
| `parent` | Object | 父容器，默认 `ood('body')` |
| `modal` | Boolean | 是否模态对话框 |
| `left` | Number | 左侧位置（像素） |
| `top` | Number | 顶部位置（像素） |
| `callback` | Function | 显示完成后的回调函数 |
| `ignoreEffects` | Boolean | 是否忽略动画效果 |

**返回值**: `this`（支持链式调用）

### `hide(ignoreEffects)`
隐藏对话框。

| 参数 | 类型 | 描述 |
|------|------|------|
| `ignoreEffects` | Boolean | 是否忽略动画效果 |

**返回值**: `this`（支持链式调用）

### `close(triggerEvent, ignoreEffects)`
关闭对话框。

| 参数 | 类型 | 描述 |
|------|------|------|
| `triggerEvent` | Boolean | 是否触发关闭事件，默认 `true` |
| `ignoreEffects` | Boolean | 是否忽略动画效果 |

**返回值**: `this`（支持链式调用）

### `activate(flag)`
激活对话框（获取焦点）。

| 参数 | 类型 | 描述 |
|------|------|------|
| `flag` | Boolean | 激活标志，默认 `true` |

### `isPinned()`
检查对话框是否已固定。

**返回值**: `Boolean` - 如果固定返回 `true`

### `setTheme(theme)`
设置主题样式（从现有代码推断）。

| 参数 | 类型 | 描述 |
|------|------|------|
| `theme` | String | 主题名称：`'light'` 或 `'dark'` |

**返回值**: `this`（支持链式调用）

### `getTheme()`
获取当前主题。

**返回值**: `String` - 当前主题

### `adjustLayout()`
响应式布局调整（从现有代码推断）。

**返回值**: `this`（支持链式调用）

### `enhanceAccessibility()`
增强可访问性支持（从现有代码推断）。

**返回值**: `this`（支持链式调用）

## 静态方法

### `Initialize()`
初始化方法，设置模板和全局快捷方法。

### `Static.Appearances`
样式定义：
- `KEY`: 基础样式设置

### `Templates`
组件模板定义（复杂的多层结构）：
- `FRAME`: 对话框框架
- `BORDER`: 边框区域，包含标题栏和内容区
- `MAIN`: 主内容区域
- `PANEL`: 实际内容面板
- `TBAR`: 顶部标题栏
- `BBAR`: 底部工具栏

### `DataModel`
数据模型定义（见上方属性表）。

### `EventHandlers`
事件处理器定义：
- `onIniPanelView`: 面板视图初始化时触发
- `onShow`: 对话框显示时触发
- `onActivated`: 对话框激活时触发
- `beforePin`: 固定前触发
- `beforeStatusChanged`: 状态改变前触发
- `afterStatusChanged`: 状态改变后触发
- `onClickPanel`: 点击面板时触发
- `onLand`: 横屏模式时触发
- `beforeClose`: 关闭前触发
- `onShowInfo`: 显示信息时触发
- `onShowOptions`: 显示选项时触发
- `onRefresh`: 刷新时触发
- `onCmd`: 命令按钮点击时触发

### `_prepareData(profile)`
预处理模板数据，处理标题栏显示、对齐方式等。

### `_min(profile, status, effectcallback, ignoreEffects)`
最小化对话框（内部方法）。

### `_max(profile, status, effectcallback, ignoreEffects)`
最大化对话框（内部方法）。

### `_restore(profile, status)`
恢复对话框到正常状态（内部方法）。

### `_modal(profile)`
设置为模态对话框（内部方法）。

### `_unModal(profile)`
取消模态状态（内部方法）。

### `LayoutTrigger()`
布局初始化触发器，确保模态状态正确。

### `RenderTrigger()`
渲染触发器，负责销毁时的清理。

## 事件

### `onIniPanelView`
面板视图初始化完成时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |

### `onShow`
对话框显示完成时触发。

### `onActivated`
对话框激活（获取焦点）时触发。

### `beforePin`
固定对话框前触发，可返回 `false` 阻止固定。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `value` | Boolean | 固定状态值 |

### `beforeStatusChanged`
对话框状态改变前触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `oldStatus` | String | 旧状态 |
| `newStatus` | String | 新状态 |

### `afterStatusChanged`
对话框状态改变后触发。

### `onClickPanel`
点击对话框面板时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

### `onLand`
切换到横屏模式时触发。

### `beforeClose`
对话框关闭前触发，可返回 `false` 阻止关闭。

### `onShowInfo`
显示信息按钮点击时触发。

### `onShowOptions`
显示选项按钮点击时触发。

### `onRefresh`
刷新按钮点击时触发。

### `onCmd`
命令按钮点击时触发。

| 参数 | 类型 | 描述 |
|------|------|------|
| `profile` | Object | 组件实例 |
| `cmdkey` | String | 命令按钮键名 |
| `e` | Event | 原生事件对象 |
| `src` | Object | 事件源 |

## 使用示例

### 基本模态对话框
```javascript
var modalDialog = ood.UI.MDialog({
    caption: '确认删除',
    width: '20em',
    height: '15em',
    modal: true,
    html: '<div style="padding:1em">确定要删除这条记录吗？</div>'
}).showModal();
```

### 非模态对话框（可拖拽）
```javascript
var movableDialog = ood.UI.MDialog({
    caption: '移动端设置',
    width: '22em',
    height: '30em',
    modal: false,
    movable: true,
    resizer: true,
    theme: 'dark',
    responsive: true
}).show();
```

### 带自动加载的对话框
```javascript
var autoLoadDialog = ood.UI.MDialog({
    caption: '远程内容',
    width: '28em',
    height: '35em',
    iframeAutoLoad: 'https://example.com/content',
    modal: true
}).showModal();
```

### 状态切换示例
```javascript
var dialog = ood.UI.MDialog({
    caption: '多功能对话框',
    width: '25em',
    height: '25em'
}).show();

// 最大化
dialog.setStatus('max');

// 最小化
dialog.setStatus('min');

// 恢复正常
dialog.setStatus('normal');
```

### 动态更新内容
```javascript
// 更新标题
dialog.setCaption('新标题');

// 更新内容
dialog.setHtml('<div>新内容</div>');

// 更新图标
dialog.setImage('path/to/icon.png');
```

## 动画效果

### 内置动画类型
1. **Classic**: 经典淡入淡出
2. **Flip H**: 水平翻转
3. **Flip V**: 垂直翻转
4. **Slide Up**: 向上滑动
5. **Slide Down**: 向下滑动
6. **Zoom In**: 缩放进入
7. **Zoom Out**: 缩放退出

### 自定义动画
通过 `showEffects` 和 `hideEffects` 属性指定动画效果，支持组合多个动画。

## 现代化功能

### 主题系统
- 支持 `light` 和 `dark` 两种主题
- 主题设置自动保存到 `localStorage`
- 可通过 `setTheme()` 和 `getTheme()` 方法管理

### 响应式设计
当 `responsive` 属性为 `true` 时，组件会根据屏幕宽度自动调整：
- 小屏幕优化触摸区域
- 适配不同设备方向
- 智能调整按钮大小和间距

### 可访问性增强
- 自动添加ARIA属性
- 键盘导航支持
- 屏幕阅读器优化
- 焦点管理

## 对话框状态

### `normal`（正常）
常规显示状态，可移动和调整大小。

### `min`（最小化）
缩放到最小状态，通常只显示标题栏。

### `max`（最大化）
占据整个可用空间，隐藏边框和标题栏装饰。

### `left`（靠左）
停靠在左侧，通常用于侧边栏场景。

### `right`（靠右）
停靠在右侧，通常用于侧边栏场景。

## 移动端优化特性

### 触摸友好
- 大触摸目标（按钮、手柄）
- 触摸反馈效果
- 手势支持（拖拽、缩放）

### 性能优化
- 硬件加速动画
- 惰性渲染
- 内存高效管理

### 设备适配
- 自动适应屏幕方向
- 状态栏间距
- 虚拟键盘交互

## 注意事项

1. **模态对话框**：模态对话框会阻止与其他元素的交互，确保在适当的时候关闭。
2. **内存管理**：长时间不使用的对话框应及时销毁（`destroy()`）以避免内存泄漏。
3. **动画性能**：复杂的动画可能影响低端设备的性能，建议进行测试和优化。
4. **嵌套使用**：避免过多的对话框嵌套，可能导致用户体验问题和性能下降。
5. **移动端兼容性**：在不同移动浏览器中测试以确保一致的行为和外观。