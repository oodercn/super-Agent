# TextArea 组件

## 1. 组件概述
TextArea 组件是 ooder-a2ui 前端框架中的一个多行文本输入组件，用于接收用户输入的长文本内容，支持自动高度调整、字数统计、 placeholder 等功能。

### 1.1 核心功能
- 支持多行文本输入
- 支持自动高度调整
- 支持字数统计
- 支持最大长度限制
- 支持 placeholder 文本
- 支持禁用和只读状态
- 支持自定义样式
- 支持键盘导航
- 支持主题定制

### 1.2 适用场景
- 表单中的备注或描述输入
- 评论或反馈输入
- 文章或内容编辑
- 消息发送输入框
- 配置项的文本输入

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "textarea1",
  "type": "TextArea",
  "props": {
    "value": "这是一个多行文本输入框示例",
    "placeholder": "请输入文本内容",
    "disabled": false,
    "readonly": false,
    "rows": 4,
    "maxLength": 1000,
    "showCount": true
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const textarea = ood.create("TextArea", {
  id: "textarea1",
  value: "",
  placeholder: "请输入详细描述",
  rows: 3,
  autoSize: true,
  maxLength: 500,
  showCount: true,
  width: 400,
  height: 120,
  border-radius: 4
});

// 添加到表单
form.addChild(textarea);

// 监听文本变化事件
textarea.on("onChange", (value) => {
  console.log("输入的文本:", value);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **value** | string | "" | 文本内容 |
| **placeholder** | string | "" | 占位文本 |
| **disabled** | boolean | false | 是否禁用组件 |
| **readonly** | boolean | false | 是否只读 |
| **rows** | number | 3 | 初始行数 |
| **cols** | number | 20 | 初始列数 |
| **maxLength** | number | null | 最大输入长度 |
| **showCount** | boolean | false | 是否显示字数统计 |
| **autoSize** | boolean/object | false | 是否自动调整高度，对象类型可配置 minRows 和 maxRows |
| **width** | number/string | "100%" | 组件宽度 |
| **height** | number/string | null | 组件高度，设置后 autoSize 失效 |
| **resize** | string | "both" | 调整大小方式："none"、"both"、"horizontal"、"vertical" |
| **borderRadius** | number | 4 | 边框圆角 |
| **backgroundColor** | string | "#ffffff" | 背景颜色 |
| **color** | string | "#333333" | 文本颜色 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **getValue** | `getValue()` | 获取当前文本内容 |
| **setValue** | `setValue(value)` | 设置文本内容 |
| **clearValue** | `clearValue()` | 清除文本内容 |
| **focus** | `focus()` | 获取焦点 |
| **blur** | `blur()` | 失去焦点 |
| **select** | `select()` | 选中所有文本 |
| **setDisabled** | `setDisabled(disabled)` | 设置组件是否禁用 |
| **setReadonly** | `setReadonly(readonly)` | 设置组件是否只读 |
| **getLength** | `getLength()` | 获取当前文本长度 |
| **validate** | `validate()` | 验证输入是否有效 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 文本内容变化时触发 | `value` - 当前文本内容 |
| **onInput** | 输入时触发（实时） | `value` - 当前文本内容 |
| **onFocus** | 获得焦点时触发 | `event` - 焦点事件对象 |
| **onBlur** | 失去焦点时触发 | `event` - 失焦事件对象 |
| **onKeyDown** | 键盘按下时触发 | `event` - 键盘事件对象 |
| **onKeyUp** | 键盘抬起时触发 | `event` - 键盘事件对象 |
| **onPaste** | 粘贴时触发 | `event` - 粘贴事件对象 |
| **onResize** | 调整大小时触发 | `{ width, height }` - 调整后的尺寸 |

## 6. 示例代码

### 6.1 基本文本域
```json
{
  "id": "basicTextArea",
  "type": "TextArea",
  "props": {
    "value": "",
    "placeholder": "请输入文本内容",
    "rows": 4,
    "cols": 50,
    "width": 400
  }
}
```

### 6.2 带字数统计的文本域
```javascript
const countTextArea = ood.create("TextArea", {
  id: "countTextArea",
  value: "",
  placeholder: "请输入评论内容（最多1000字）",
  rows: 5,
  maxLength: 1000,
  showCount: true,
  width: 500,
  height: 150
});

// 监听输入事件
countTextArea.on("onInput", (value) => {
  const length = value.length;
  console.log("当前字数:", length);
});

// 添加到页面
page.addChild(countTextArea);
```

### 6.3 自动高度调整的文本域
```json
{
  "id": "autoSizeTextArea",
  "type": "TextArea",
  "props": {
    "value": "",
    "placeholder": "请输入详细描述，高度会自动调整",
    "autoSize": {
      "minRows": 2,
      "maxRows": 8
    },
    "width": 400,
    "resize": "none"
  }
}
```

### 6.4 禁用和只读文本域
```javascript
const disabledTextArea = ood.create("TextArea", {
  id: "disabledTextArea",
  value: "这是一个禁用的文本域",
  disabled: true,
  rows: 3,
  width: 400
});

const readonlyTextArea = ood.create("TextArea", {
  id: "readonlyTextArea",
  value: "这是一个只读的文本域",
  readonly: true,
  rows: 3,
  width: 400
});

// 创建Stacks布局来排列文本域
const stacks = ood.create("Stacks", {
  direction: "vertical",
  spacing: 10
});

stacks.addChild(disabledTextArea);
stacks.addChild(readonlyTextArea);

// 添加到页面
page.addChild(stacks);
```

### 6.5 自定义样式的文本域
```javascript
const customTextArea = ood.create("TextArea", {
  id: "customTextArea",
  value: "",
  placeholder: "请输入自定义样式的文本",
  rows: 4,
  width: 450,
  backgroundColor: "#f5f5f5",
  color: "#333333",
  borderRadius: 6,
  resize: "vertical"
});

// 添加到页面
page.addChild(customTextArea);
```

## 7. 最佳实践

### 7.1 尺寸设计
- 根据输入内容的预期长度设置合适的初始 rows 和 cols
- 对于简短输入（如备注），建议设置 2-4 行
- 对于较长输入（如评论），建议设置 5-8 行
- 考虑使用 autoSize 自动调整高度，提升用户体验

### 7.2 字数限制
- 合理设置 maxLength，避免用户输入过多内容
- 对于需要大量文本的场景，考虑不设置 maxLength
- 启用 showCount 显示字数统计，给用户明确的反馈
- 接近最大长度时，考虑添加视觉提示

### 7.3 样式设计
- 保持与其他表单组件的样式一致性
- 考虑使用浅色背景区分文本域和普通输入框
- 合理设置边框样式，提供清晰的视觉边界
- 根据需要设置 resize 属性，控制是否允许用户调整大小

### 7.4 性能优化
- 对于长文本输入，考虑使用防抖处理 onChange 事件
- 避免在 onInput 事件中执行复杂计算
- 合理使用 autoSize，避免频繁的 DOM 操作
- 对于非常长的文本，考虑使用虚拟滚动技术

### 7.5 可访问性
- 确保组件支持键盘导航
- 提供清晰的 placeholder 文本
- 支持屏幕阅读器
- 提供足够的颜色对比度
- 为必填字段添加适当的标识

## 8. 常见问题与解决方案

### 8.1 自动高度调整不生效
**问题**：设置了 autoSize: true，但高度没有自动调整
**解决方案**：确保没有设置固定 height 属性，autoSize 和 fixed height 不能同时使用

### 8.2 字数统计不准确
**问题**：显示的字数与实际输入的字数不一致
**解决方案**：检查是否包含空格、换行符等特殊字符，或自定义字数统计逻辑

### 8.3 文本域无法调整大小
**问题**：设置了 resize: "both"，但无法调整大小
**解决方案**：确保没有设置 fixed height 属性，或检查是否有 CSS 样式覆盖

### 8.4 性能问题
**问题**：输入长文本时，组件卡顿
**解决方案**：使用防抖处理 onChange 事件，或减少实时计算

### 8.5 移动端适配问题
**问题**：在移动端，文本域输入体验不佳
**解决方案**：优化 autoSize 配置，调整 touch 事件处理，或提供更大的输入区域

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他文本输入方案 |

## 10. 相关组件

- **Input**：基础单行输入框组件
- **ComboInput**：组合输入框组件
- **Label**：标签组件，用于文本域的标题
- **FormLayout**：表单布局组件，用于组织表单控件

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本多行文本输入 | 2025-01-15 |
| 1.1.0 | 新增自动高度调整功能 | 2025-03-20 |
| 1.2.0 | 新增字数统计功能 | 2025-05-10 |
| 1.3.0 | 增强样式定制能力 | 2025-07-05 |
| 1.4.0 | 优化移动端适配 | 2025-09-15 |
| 1.5.0 | 优化性能和可访问性 | 2025-11-20 |

## 12. 扩展阅读

- [HTML Textarea 元素](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/textarea)
- [ooder-a2ui 表单组件最佳实践](FRONTEND_COMPONENTS.md#表单组件最佳实践)
- [响应式设计在表单中的应用](https://web.dev/responsive-design/)

## 13. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队