# HiddenInput 组件

## 概述

HiddenInput 是一个隐藏输入框组件，用于在页面中存储不可见的数据。它生成一个标准的 HTML `<input type="hidden">` 元素，适用于存储表单状态、临时数据、安全令牌等场景。该组件继承了所有 Widget 的布局和交互能力，但默认在页面上不可见。

## 类名

- **完整类名**: `ood.UI.HiddenInput`
- **继承自**: `ood.UI.Widget`, `ood.absValue`
- **模板标签**: `input` (type="hidden")

## 快速开始

```javascript
// 创建隐藏输入框
var hiddenInput = ood.UI.HiddenInput({
    name: 'csrf_token',
    value: 'abc123xyz'
}).appendTo('#form-container');

// 获取和设置值
hiddenInput.setUIValue('new_token');
var currentValue = hiddenInput.getUIValue();

// 通过数据绑定使用
var dataBinder = ood.DataBinder({
    model: { userId: 1001 }
});
var hiddenUserId = ood.UI.HiddenInput({
    dataBinder: dataBinder,
    dataField: 'userId'
}).appendTo('body');
```

## API 参考

### DataModel 属性

HiddenInput 继承所有 Widget 的通用属性，以下是主要属性说明：

| 属性 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| `value` | String | `null` | 输入框的值（通过 `setUIValue`/`getUIValue` 访问） |
| `name` | String | `null` | 输入框的 name 属性，用于表单提交 |
| `expression` | String | `null` | 值表达式，支持动态计算 |
| `locked` | Boolean | `null` | 是否锁定，锁定后用户不能修改值 |
| `required` | Boolean | `null` | 是否为必填字段 |
| `dataBinder` | Object | `null` | 绑定的数据模型实例 |
| `dataField` | String | `null` | 绑定的数据字段名 |
| `display` | String | `null` | 显示模式，hidden 组件默认 `'none'` |
| `visibility` | String | `null` | 可见性控制 |
| `position` | String | `null` | 定位方式 |
| `left`, `top`, `right`, `bottom` | String | `null` | 定位坐标 |
| `width`, `height` | String | `null` | 尺寸设置 |
| `rotate` | Number | `null` | 旋转角度 |
| `showEffects`, `hideEffects` | Object | `null` | 显示/隐藏动画效果 |
| `activeAnim` | Object | `null` | 激活时的动画 |
| `tabindex` | Number | `null` | Tab 键顺序 |
| `zIndex` | Number | `null` | 层叠顺序 |
| `defaultFocus` | Boolean | `null` | 是否默认获取焦点 |
| `hoverPop`, `hoverPopType` | String | `null` | 鼠标悬停弹出内容 |
| `disabled`, `readonly` | Boolean | `null` | 禁用/只读状态 |
| `disableClickEffect`, `disableHoverEffect` | Boolean | `null` | 禁用点击/悬停效果 |
| `dock`, `dockOrder`, `dockMargin` | String | `null` | 停靠布局相关 |
| `dockMinW`, `dockMinH`, `dockMaxW`, `dockMaxH` | String | `null` | 停靠最小/最大尺寸 |
| `dockFloat`, `dockIgnore` | Boolean | `null` | 停靠浮动和忽略设置 |
| `dirtyMark`, `showDirtyMark` | Boolean | `null` | 脏标记显示 |
| `selectable` | Boolean | `null` | 是否可选 |
| `autoTips`, `tips`, `disableTips` | String/Boolean | `null` | 提示文本设置 |
| `renderer` | Function | `null` | 自定义渲染器 |
| `className` | String | `null` | 自定义 CSS 类名 |

### 静态方法

#### `setUIValue(value, force, triggerEventOnly, tag)`
设置输入框的值。

**参数**:
- `value` (String): 要设置的值
- `force` (Boolean, 可选): 是否强制更新
- `triggerEventOnly` (Boolean, 可选): 是否仅触发事件
- `tag` (String, 可选): 操作标签

**返回**: 无

#### `getUIValue()`
获取输入框的当前值。

**返回**: String - 输入框的值

#### `_ensureValue(profile, value)`
确保返回字符串格式的值，内部使用。

**参数**:
- `profile` (Object): 配置对象
- `value` (any): 原始值

**返回**: String - 转换后的字符串值

### 实例方法

#### `activate()`
激活组件，返回组件实例。

**返回**: `this` - 组件实例

### 事件

HiddenInput 支持所有 Widget 的标准事件：

| 事件 | 描述 |
|------|------|
| `beforeDirtyMark` | 脏标记前触发 |
| `onContextmenu` | 右键菜单时触发 |
| `onDock` | 停靠时触发 |
| `onLayout` | 布局时触发 |
| `onMove` | 移动时触发 |
| `onRender` | 渲染时触发 |
| `onResize` | 调整大小时触发 |
| `onShowTips` | 显示提示时触发 |
| `beforeAppend`, `afterAppend` | 附加前后触发 |
| `beforeRender`, `afterRender` | 渲染前后触发 |
| `beforeRemove`, `afterRemove` | 移除前后触发 |
| `onHotKeydown`, `onHotKeypress`, `onHotKeyup` | 快捷键事件 |

## 使用示例

### 示例 1：存储会话令牌
```javascript
// 创建隐藏的 CSRF 令牌字段
var csrfToken = ood.UI.HiddenInput({
    id: 'csrf_token',
    name: '_csrf',
    value: '{{csrf_token}}', // 服务器端渲染的值
    className: 'security-field'
}).appendTo(document.body);
```

### 示例 2：与数据绑定集成
```javascript
var binder = ood.DataBinder({
    model: {
        preferences: {
            theme: 'dark',
            fontSize: 14
        }
    }
});

// 绑定用户偏好设置
var themeField = ood.UI.HiddenInput({
    dataBinder: binder,
    dataField: 'preferences.theme'
}).appendTo('#settings-form');

var fontSizeField = ood.UI.HiddenInput({
    dataBinder: binder,
    dataField: 'preferences.fontSize'
}).appendTo('#settings-form');
```

### 示例 3：动态表达式
```javascript
// 使用表达式计算值
var calculatedField = ood.UI.HiddenInput({
    expression: 'model.total * model.taxRate',
    dataBinder: orderBinder
}).appendTo('#order-form');
```

## 注意事项

1. **可见性**: HiddenInput 默认使用 `display:none` 样式，不会在页面上显示。不要依赖 CSS 覆盖来显示它，应使用其他可见的输入组件。

2. **表单提交**: 当 HiddenInput 放置在 `<form>` 元素内时，其值会随表单一起提交。确保 `name` 属性设置正确。

3. **安全性**: 虽然值对用户不可见，但通过浏览器开发者工具仍然可以查看和修改。敏感数据（如密码、密钥）不应存储在 HiddenInput 中。

4. **数据绑定**: HiddenInput 完全支持 A2UI 的数据绑定系统，可以实时响应模型变化。

5. **性能**: 大量 HiddenInput 实例可能影响页面性能，建议在需要时动态创建，不需要时及时销毁。

6. **辅助功能**: 由于不可见，HiddenInput 对屏幕阅读器用户可能不可访问。如果需要存储对辅助技术重要的数据，考虑使用 `aria-live` 区域或其他可见元素。

7. **默认样式**: 组件默认添加 `ood-display-none` CSS 类，确保在样式表中定义了相应的隐藏样式。