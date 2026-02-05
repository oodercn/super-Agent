# TimePicker 组件

## 1. 组件概述
TimePicker 组件是 ooder-a2ui 前端框架中的一个时间选择器，用于方便用户选择时间，支持多种时间格式、时间范围选择和自定义时间间隔。

### 1.1 核心功能
- 支持单时间点选择
- 支持时间范围选择
- 支持多种时间格式（HH:mm, HH:mm:ss, 12小时制等）
- 支持自定义时间间隔（分钟、秒）
- 支持快捷时间选择
- 支持禁用特定时间段
- 支持键盘导航
- 支持主题定制
- 支持国际化

### 1.2 适用场景
- 表单中的时间输入
- 日程安排的时间选择
- 会议预约的时间段选择
- 考勤系统的打卡时间
- 数据查询的时间筛选

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "timePicker1",
  "type": "TimePicker",
  "props": {
    "value": "14:30",
    "format": "HH:mm",
    "placeholder": "请选择时间",
    "disabled": false,
    "showClearBtn": true
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const timePicker = ood.create("TimePicker", {
  id: "timePicker1",
  type: "range",
  value: ["09:00", "18:00"],
  format: "HH:mm:ss",
  placeholder: ["开始时间", "结束时间"],
  step: 300, // 5分钟间隔
  disabledTime: (time) => {
    // 禁用凌晨2点到6点的时间
    const hour = time.getHours();
    return hour >= 2 && hour < 6;
  }
});

// 添加到表单
form.addChild(timePicker);

// 监听时间变化事件
timePicker.on("onChange", (value) => {
  console.log("选择的时间范围:", value);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **value** | string/array | null | 选中的时间值，单时间点为字符串，范围选择为数组 [startTime, endTime] |
| **type** | string | "single" | 选择类型："single"（单时间点）或 "range"（时间范围） |
| **format** | string | "HH:mm" | 时间格式，支持 HH:mm、HH:mm:ss、h:mm a（12小时制）等 |
| **placeholder** | string/array | "请选择时间" | 占位文本，范围选择为数组 [startPlaceholder, endPlaceholder] |
| **disabled** | boolean | false | 是否禁用组件 |
| **readonly** | boolean | false | 是否只读 |
| **showClearBtn** | boolean | true | 是否显示清除按钮 |
| **showQuickSelect** | boolean | false | 是否显示快捷选择面板 |
| **quickSelectOptions** | array | [] | 自定义快捷选择选项 |
| **disabledTime** | function | null | 自定义禁用时间的函数，返回 true 表示禁用该时间 |
| **step** | number | 60 | 时间间隔（秒），默认1分钟 |
| **minTime** | string/Date | null | 最小可选时间 |
| **maxTime** | string/Date | null | 最大可选时间 |
| **width** | number/string | 200 | 组件宽度 |
| **size** | string | "medium" | 组件尺寸："small"、"medium"、"large" |
| **borderRadius** | number | 4 | 边框圆角 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **getValue** | `getValue()` | 获取当前选中的时间值 |
| **setValue** | `setValue(value)` | 设置选中的时间值 |
| **clearValue** | `clearValue()` | 清除选中的时间值 |
| **show** | `show()` | 显示时间选择面板 |
| **hide** | `hide()` | 隐藏时间选择面板 |
| **toggle** | `toggle()` | 切换时间选择面板的显示/隐藏状态 |
| **setDisabled** | `setDisabled(disabled)` | 设置组件是否禁用 |
| **setReadonly** | `setReadonly(readonly)` | 设置组件是否只读 |
| **validate** | `validate()` | 验证时间格式是否正确 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 时间选择变化时触发 | `value` - 选中的时间值 |
| **onSelect** | 选择时间时触发（包括面板选择） | `time` - 选中的时间对象 |
| **onShow** | 时间选择面板显示时触发 | 无 |
| **onHide** | 时间选择面板隐藏时触发 | 无 |
| **onClear** | 清除时间时触发 | 无 |

## 6. 示例代码

### 6.1 基本时间选择器
```json
{
  "id": "basicTimePicker",
  "type": "TimePicker",
  "props": {
    "value": "14:30",
    "format": "HH:mm",
    "placeholder": "请选择时间",
    "showClearBtn": true,
    "width": 200
  }
}
```

### 6.2 时间范围选择器
```javascript
const rangePicker = ood.create("TimePicker", {
  id: "rangePicker",
  type: "range",
  value: ["09:00", "18:00"],
  format: "HH:mm",
  placeholder: ["开始时间", "结束时间"],
  showQuickSelect: true,
  quickSelectOptions: [
    { label: "上午", value: ["09:00", "12:00"] },
    { label: "下午", value: ["13:00", "18:00"] },
    { label: "全天", value: ["00:00", "23:59"] }
  ]
});

// 监听时间范围变化
rangePicker.on("onChange", (value) => {
  console.log("开始时间:", value[0]);
  console.log("结束时间:", value[1]);
});

// 添加到查询表单
queryForm.addChild(rangePicker);
```

### 6.3 自定义时间间隔
```json
{
  "id": "customStepPicker",
  "type": "TimePicker",
  "props": {
    "value": "14:30",
    "format": "HH:mm",
    "placeholder": "请选择时间",
    "step": 300, // 5分钟间隔
    "width": 200
  }
}
```

### 6.4 12小时制时间选择器
```javascript
const twelveHourPicker = ood.create("TimePicker", {
  id: "twelveHourPicker",
  value: "02:30 PM",
  format: "h:mm a",
  placeholder: "请选择时间 (12小时制)",
  size: "large",
  borderRadius: 6,
  width: 250
});

// 获取格式化后的时间
const formattedTime = twelveHourPicker.getValue();
console.log("12小时制时间:", formattedTime);

// 添加到页面
page.addChild(twelveHourPicker);
```

### 6.5 带秒的时间选择器
```json
{
  "id": "secondTimePicker",
  "type": "TimePicker",
  "props": {
    "value": "14:30:45",
    "format": "HH:mm:ss",
    "placeholder": "请选择时间（带秒）",
    "step": 1, // 1秒间隔
    "width": 250
  }
}
```

## 7. 最佳实践

### 7.1 时间格式选择
- 建议使用24小时制格式 "HH:mm"，确保清晰无歧义
- 根据业务需求选择是否显示秒，如考勤系统可能需要秒级精度
- 12小时制适合面向普通用户的应用，如预约系统

### 7.2 时间间隔设计
- 合理设置时间间隔，避免过多选项影响用户体验
- 会议预约建议使用15分钟或30分钟间隔
- 精确计时场景建议使用1分钟或更短间隔
- 避免设置过长的时间间隔，导致可选时间过少

### 7.3 范围选择设计
- 对于时间范围选择，建议提供合理的快捷选项，如上午、下午、全天等
- 清晰区分开始时间和结束时间的输入框
- 考虑添加时间范围的最大限制，如最多选择12小时

### 7.4 禁用时间策略
- 合理设置禁用时间，避免用户选择无效时间
- 对于非工作时间，根据业务需求决定是否禁用
- 禁用时间时，提供清晰的视觉反馈

### 7.5 性能优化
- 对于短时间间隔的时间选择器，考虑限制可选时间范围
- 避免在 disabledTime 函数中执行复杂计算
- 对于频繁更新的时间选择器，考虑使用防抖处理

## 8. 常见问题与解决方案

### 8.1 时间格式转换问题
**问题**：选择的时间格式与后端要求不一致
**解决方案**：使用 format 属性设置正确的时间格式，或在 onChange 事件中进行格式转换

### 8.2 禁用时间不生效
**问题**：设置了 disabledTime 函数，但禁用时间不生效
**解决方案**：检查 disabledTime 函数的返回值，确保返回布尔值，且时间对象的处理正确

### 8.3 时间范围选择顺序问题
**问题**：用户可以选择结束时间早于开始时间的范围
**解决方案**：框架内置了范围选择的顺序验证，自动调整选择顺序

### 8.4 快捷选择选项不显示
**问题**：设置了 quickSelectOptions，但快捷选择面板不显示
**解决方案**：确保同时设置了 showQuickSelect: true

### 8.5 国际化支持问题
**问题**：时间选择器的AM/PM显示为英文，需要中文显示
**解决方案**：设置框架的国际化配置，或自定义时间格式

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他时间选择方案 |

## 10. 相关组件

- **DatePicker**：日期选择器，用于选择日期
- **Input**：基础输入框组件
- **ComboInput**：组合输入框组件
- **Select**：下拉选择组件

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本时间选择功能 | 2025-01-15 |
| 1.1.0 | 新增时间范围选择功能 | 2025-03-20 |
| 1.2.0 | 新增自定义时间间隔功能 | 2025-05-10 |
| 1.3.0 | 增强禁用时间功能，支持函数式配置 | 2025-07-05 |
| 1.4.0 | 支持12小时制时间格式 | 2025-09-15 |
| 1.5.0 | 优化键盘导航支持 | 2025-11-20 |

## 12. 扩展阅读

- [JavaScript Date 对象](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Date)
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