# Input 组件手册

## 1. 概述

Input 组件是 ooder-agent-rad 前端框架中用于文本输入的基础组件，支持各种输入类型、验证规则和自定义样式。它提供了直观的方式来接收用户输入，适用于各种表单场景。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.Input
```

### 2.2 继承关系

```
ood.UI.Input extends ood.UI
```

### 2.3 主要功能

- 支持多种输入类型（文本、密码、数字、邮箱等）
- 支持输入验证和错误提示
- 支持自动完成和提示
- 支持自定义样式和主题
- 支持响应式设计
- 支持无障碍访问

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Input 组件：

```javascript
var input = ood.create("ood.UI.Input")
    .setHost(host, "input")
    .setName("input")
    .setWidth("200px")
    .setHeight("30px")
    .setPlaceholder("请输入文本")
    .setValue("默认值");
```

### 3.2 输入类型设置

```javascript
// 文本输入
var textInput = ood.create("ood.UI.Input")
    .setHost(host, "textInput")
    .setName("textInput")
    .setType("text")
    .setPlaceholder("请输入文本");

// 密码输入
var passwordInput = ood.create("ood.UI.Input")
    .setHost(host, "passwordInput")
    .setName("passwordInput")
    .setType("password")
    .setPlaceholder("请输入密码");

// 数字输入
var numberInput = ood.create("ood.UI.Input")
    .setHost(host, "numberInput")
    .setName("numberInput")
    .setType("number")
    .setPlaceholder("请输入数字")
    .setMin(0)
    .setMax(100);
```

## 4. 核心属性

### 4.1 基础属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| name | String | "Input" | 组件名称 |
| type | String | "text" | 输入类型（text/password/number/email/tel/url等） |
| value | String | "" | 输入值 |
| width | String | "150px" | 组件宽度 |
| height | String | "25px" | 组件高度 |
| placeholder | String | "" | 占位符文本 |
| disabled | Boolean | false | 是否禁用 |
| readOnly | Boolean | false | 是否只读 |
| required | Boolean | false | 是否必填 |
| autoFocus | Boolean | false | 是否自动获取焦点 |

### 4.2 验证属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| validation | String | "" | 验证规则表达式 |
| validationMessage | String | "" | 验证失败提示信息 |
| min | Number | null | 最小值（仅适用于数字类型） |
| max | Number | null | 最大值（仅适用于数字类型） |
| minLength | Number | null | 最小长度 |
| maxLength | Number | null | 最大长度 |
| pattern | String | "" | 正则表达式验证 |

### 4.3 样式属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| className | String | "" | 自定义 CSS 类名 |
| style | Object | {} | 自定义样式 |
| theme | String | "light" | 主题（light/dark） |
| fontSize | String | "14px" | 字体大小 |
| fontWeight | String | "normal" | 字体粗细 |
| color | String | "#333333" | 文本颜色 |
| backgroundColor | String | "#ffffff" | 背景颜色 |

## 5. 方法详解

### 5.1 值管理

#### setValue(value)

设置输入值：

```javascript
input.setValue("新值");
```

#### getValue()

获取输入值：

```javascript
var value = input.getValue();
```

#### clearValue()

清空输入值：

```javascript
input.clearValue();
```

### 5.2 状态管理

#### setDisabled(disabled)

设置是否禁用：

```javascript
input.setDisabled(true); // 禁用输入框
input.setDisabled(false); // 启用输入框
```

#### setReadOnly(readOnly)

设置是否只读：

```javascript
input.setReadOnly(true); // 设置为只读
input.setReadOnly(false); // 取消只读
```

#### focus()

获取焦点：

```javascript
input.focus();
```

#### blur()

失去焦点：

```javascript
input.blur();
```

### 5.3 验证方法

#### validate()

验证输入值：

```javascript
var isValid = input.validate();
if (isValid) {
    console.log("输入有效");
} else {
    console.log("输入无效");
}
```

#### setValidation(validation, message)

设置验证规则和提示信息：

```javascript
// 使用内置验证规则
input.setValidation("required", "此项为必填项");
input.setValidation("email", "请输入有效的邮箱地址");

// 使用自定义正则表达式
input.setValidation("/^[0-9]{6}$/", "请输入6位数字");
```

### 5.4 主题管理

#### setTheme(theme)

设置组件主题：

```javascript
input.setTheme("dark"); // 设置为深色主题
```

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| onChange | 输入值改变时触发 |
| onBlur | 失去焦点时触发 |
| onFocus | 获取焦点时触发 |
| onKeyDown | 键盘按下时触发 |
| onKeyUp | 键盘释放时触发 |
| onKeyPress | 键盘按下并释放时触发 |
| onInput | 输入时触发 |
| onValidate | 验证时触发 |

### 6.2 事件设置

```javascript
// 监听输入变化事件
input.setOnChange(function(profile, value, oldValue) {
    console.log("输入值已改变:", value);
});

// 监听焦点事件
input.setOnFocus(function(profile) {
    console.log("输入框获取焦点");
});

// 监听失去焦点事件
input.setOnBlur(function(profile) {
    console.log("输入框失去焦点");
});

// 监听验证事件
input.setOnValidate(function(profile, isValid, message) {
    if (!isValid) {
        console.log("验证失败:", message);
    }
});
```

## 7. 响应式设计

Input 组件支持响应式设计，自动适应不同屏幕尺寸。可以通过 CSS 媒体查询或 JavaScript 动态调整组件大小和样式：

```javascript
// 根据屏幕宽度调整输入框大小
function adjustInputSize() {
    var width = ood(document.body).cssSize().width;
    if (width < 768) {
        input.setWidth("100%");
    } else {
        input.setWidth("300px");
    }
}

// 初始化调整
adjustInputSize();

// 监听窗口大小变化
ood(window).onResize(adjustInputSize);
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
input.setTheme("dark");

// 获取当前主题
var currentTheme = input.getTheme();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA 属性

组件自动添加 ARIA 属性：

```html
<input type="text" role="textbox" aria-label="文本输入" aria-required="true" aria-invalid="false">
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab 键：在输入框之间导航
- Enter 键：提交表单
- Escape 键：取消输入

## 10. 示例代码

### 10.1 基本文本输入

```javascript
var textInput = ood.create("ood.UI.Input")
    .setHost(host, "textInput")
    .setName("textInput")
    .setWidth("300px")
    .setHeight("35px")
    .setPlaceholder("请输入姓名")
    .setValue("")
    .setRequired(true)
    .setValidation("required", "请输入姓名");
```

### 10.2 密码输入

```javascript
var passwordInput = ood.create("ood.UI.Input")
    .setHost(host, "passwordInput")
    .setName("passwordInput")
    .setType("password")
    .setWidth("300px")
    .setHeight("35px")
    .setPlaceholder("请输入密码")
    .setValidation("/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/", "密码至少包含8个字符，包括字母和数字");
```

### 10.3 邮箱输入

```javascript
var emailInput = ood.create("ood.UI.Input")
    .setHost(host, "emailInput")
    .setName("emailInput")
    .setType("email")
    .setWidth("300px")
    .setHeight("35px")
    .setPlaceholder("请输入邮箱")
    .setValidation("email", "请输入有效的邮箱地址");
```

### 10.4 带验证的数字输入

```javascript
var numberInput = ood.create("ood.UI.Input")
    .setHost(host, "numberInput")
    .setName("numberInput")
    .setType("number")
    .setWidth("300px")
    .setHeight("35px")
    .setPlaceholder("请输入年龄")
    .setMin(0)
    .setMax(120)
    .setValidation("number", "请输入有效的数字");
```

## 11. 最佳实践

### 11.1 输入验证

1. **使用内置验证规则**：优先使用组件内置的验证规则，如 `required`、`email`、`number` 等
2. **提供清晰的错误提示**：验证失败时显示明确的错误信息，帮助用户理解问题
3. **实时验证**：在用户输入过程中进行实时验证，及时反馈错误
4. **合理的验证时机**：在失去焦点或提交表单时进行最终验证

### 11.2 样式设计

1. **统一的样式**：保持输入框样式的一致性，包括大小、颜色、边框等
2. **合适的尺寸**：设置合适的宽度和高度，保证良好的可读性
3. **清晰的占位符**：使用简洁明了的占位符文本，指导用户输入
4. **视觉反馈**：提供清晰的焦点状态和错误状态样式

### 11.3 性能优化

1. **减少不必要的验证**：避免过于频繁的验证，影响性能
2. **合理使用自动完成**：根据需要启用或禁用自动完成功能
3. **优化事件处理**：避免在输入事件中执行复杂操作

### 11.4 无障碍访问

1. **添加 ARIA 属性**：确保输入框有适当的 ARIA 属性
2. **支持键盘导航**：确保可以通过键盘完全操作输入框
3. **清晰的错误提示**：错误提示应该对屏幕阅读器友好

## 12. 常见问题

### 12.1 输入值不更新

- **问题**：输入值改变后，通过 `getValue()` 方法获取的值没有更新
- **解决方案**：
  1. 确保已正确设置 `onChange` 事件
  2. 检查是否有其他代码阻止了事件冒泡
  3. 尝试使用 `onInput` 事件代替 `onChange` 事件

### 12.2 验证不生效

- **问题**：设置了验证规则，但验证不生效
- **解决方案**：
  1. 检查验证规则格式是否正确
  2. 确保已调用 `validate()` 方法
  3. 检查是否有其他代码覆盖了验证逻辑

### 12.3 样式不生效

- **问题**：设置了自定义样式，但样式不生效
- **解决方案**：
  1. 检查样式属性名是否正确
  2. 检查是否有 CSS 样式冲突
  3. 尝试使用 `!important` 优先级

### 12.4 焦点问题

- **问题**：输入框无法获取焦点或焦点丢失
- **解决方案**：
  1. 检查是否设置了 `disabled` 或 `readOnly` 属性
  2. 检查是否有其他元素覆盖了输入框
  3. 尝试调用 `focus()` 方法手动获取焦点

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础文本输入功能 |
| v1.1.0 | 支持多种输入类型和验证规则 |
| v1.2.0 | 主题支持和响应式设计 |
| v1.3.0 | 无障碍访问支持 |

## 14. 总结

Input 组件是一个功能强大的文本输入组件，支持多种输入类型、验证规则和自定义样式。它提供了直观的方式来接收用户输入，适用于各种表单场景。通过合理使用 Input 组件，可以提高表单的易用性和数据准确性。