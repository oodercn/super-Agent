# DatePicker 组件

## 1. 组件概述
DatePicker 组件是 ooder-a2ui 前端框架中的一个日期选择器，用于方便用户选择日期，支持多种日期格式、日期范围选择和快捷选择功能。

### 1.1 核心功能
- 支持单日期选择
- 支持日期范围选择
- 支持多种日期格式（YYYY-MM-DD, YYYY/MM/DD 等）
- 支持快捷日期选择（今天、昨天、近7天等）
- 支持自定义日期范围限制
- 支持月份和年份快速切换
- 支持键盘导航
- 支持主题定制
- 支持国际化

### 1.2 适用场景
- 表单中的日期输入
- 数据查询的日期筛选
- 日程安排的日期选择
- 订单管理的日期范围查询
- 报表统计的日期条件

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "datePicker1",
  "type": "DatePicker",
  "props": {
    "value": "2026-01-25",
    "format": "YYYY-MM-DD",
    "placeholder": "请选择日期",
    "disabled": false,
    "showTodayBtn": true,
    "showClearBtn": true
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const datePicker = ood.create("DatePicker", {
  id: "datePicker1",
  type: "range",
  value: ["2026-01-01", "2026-01-25"],
  format: "YYYY/MM/DD",
  placeholder: ["开始日期", "结束日期"],
  disabledDate: (date) => {
    // 禁用今天之后的日期
    return date > new Date();
  }
});

// 添加到表单
form.addChild(datePicker);

// 监听日期变化事件
datePicker.on("onChange", (value) => {
  console.log("选择的日期范围:", value);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **value** | string/array | null | 选中的日期值，单日期为字符串，范围选择为数组 [startDate, endDate] |
| **type** | string | "single" | 选择类型："single"（单日期）或 "range"（日期范围） |
| **format** | string | "YYYY-MM-DD" | 日期格式，支持 YYYY-MM-DD、YYYY/MM/DD、MM-DD-YYYY 等 |
| **placeholder** | string/array | "请选择日期" | 占位文本，范围选择为数组 [startPlaceholder, endPlaceholder] |
| **disabled** | boolean | false | 是否禁用组件 |
| **readonly** | boolean | false | 是否只读 |
| **showTodayBtn** | boolean | true | 是否显示"今天"按钮 |
| **showClearBtn** | boolean | true | 是否显示清除按钮 |
| **showQuickSelect** | boolean | false | 是否显示快捷选择面板 |
| **quickSelectOptions** | array | [] | 自定义快捷选择选项 |
| **disabledDate** | function | null | 自定义禁用日期的函数，返回 true 表示禁用该日期 |
| **minDate** | string/Date | null | 最小可选日期 |
| **maxDate** | string/Date | null | 最大可选日期 |
| **width** | number/string | 200 | 组件宽度 |
| **size** | string | "medium" | 组件尺寸："small"、"medium"、"large" |
| **borderRadius** | number | 4 | 边框圆角 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **getValue** | `getValue()` | 获取当前选中的日期值 |
| **setValue** | `setValue(value)` | 设置选中的日期值 |
| **clearValue** | `clearValue()` | 清除选中的日期值 |
| **show** | `show()` | 显示日期选择面板 |
| **hide** | `hide()` | 隐藏日期选择面板 |
| **toggle** | `toggle()` | 切换日期选择面板的显示/隐藏状态 |
| **setDisabled** | `setDisabled(disabled)` | 设置组件是否禁用 |
| **setReadonly** | `setReadonly(readonly)` | 设置组件是否只读 |
| **validate** | `validate()` | 验证日期格式是否正确 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 日期选择变化时触发 | `value` - 选中的日期值 |
| **onSelect** | 选择日期时触发（包括面板选择） | `date` - 选中的日期对象 |
| **onShow** | 日期选择面板显示时触发 | 无 |
| **onHide** | 日期选择面板隐藏时触发 | 无 |
| **onClear** | 清除日期时触发 | 无 |
| **onToday** | 点击"今天"按钮时触发 | 无 |

## 6. 示例代码

### 6.1 基本日期选择器
```json
{
  "id": "basicDatePicker",
  "type": "DatePicker",
  "props": {
    "value": "2026-01-25",
    "format": "YYYY-MM-DD",
    "placeholder": "请选择日期",
    "showTodayBtn": true,
    "showClearBtn": true,
    "width": 200
  }
}
```

### 6.2 日期范围选择器
```javascript
const rangePicker = ood.create("DatePicker", {
  id: "rangePicker",
  type: "range",
  value: ["2026-01-01", "2026-01-25"],
  format: "YYYY-MM-DD",
  placeholder: ["开始日期", "结束日期"],
  showQuickSelect: true,
  quickSelectOptions: [
    { label: "近7天", value: ["2026-01-19", "2026-01-25"] },
    { label: "近30天", value: ["2026-12-27", "2026-01-25"] },
    { label: "本月", value: ["2026-01-01", "2026-01-31"] },
    { label: "上月", value: ["2025-12-01", "2025-12-31"] }
  ]
});

// 监听日期范围变化
rangePicker.on("onChange", (value) => {
  console.log("开始日期:", value[0]);
  console.log("结束日期:", value[1]);
});

// 添加到查询表单
queryForm.addChild(rangePicker);
```

### 6.3 带禁用日期的选择器
```json
{
  "id": "disabledDatePicker",
  "type": "DatePicker",
  "props": {
    "value": "2026-01-25",
    "format": "YYYY-MM-DD",
    "placeholder": "请选择日期",
    "disabledDate": "(date) => { return date < new Date('2026-01-01') || date > new Date('2026-12-31'); }",
    "width": 200
  }
}
```

### 6.4 自定义日期格式
```javascript
const customFormatPicker = ood.create("DatePicker", {
  id: "customFormatPicker",
  value: "2026/01/25",
  format: "YYYY/MM/DD",
  placeholder: "请选择日期 (YYYY/MM/DD)",
  size: "large",
  borderRadius: 6,
  width: 250
});

// 获取格式化后的日期
const formattedDate = customFormatPicker.getValue();
console.log("格式化的日期:", formattedDate);

// 添加到页面
page.addChild(customFormatPicker);
```

## 7. 最佳实践

### 7.1 日期格式选择
- 建议使用国际标准格式 "YYYY-MM-DD"，确保跨系统兼容性
- 根据业务需求选择合适的日期格式，如财务系统可能需要 "YYYY/MM/DD"
- 保持整个应用中日期格式的一致性

### 7.2 范围选择设计
- 对于日期范围选择，建议提供合理的快捷选项，如近7天、近30天、本月等
- 清晰区分开始日期和结束日期的输入框
- 考虑添加日期范围的最大限制，如最多选择90天

### 7.3 禁用日期策略
- 合理设置禁用日期，避免用户选择无效日期
- 对于过去的日期或未来的日期，根据业务需求决定是否禁用
- 禁用日期时，提供清晰的视觉反馈

### 7.4 性能优化
- 对于大范围的日期选择，考虑限制可选日期范围
- 避免在 disabledDate 函数中执行复杂计算
- 对于频繁更新的日期选择器，考虑使用防抖处理

### 7.5 可访问性
- 确保日期选择器支持键盘导航
- 提供清晰的ARIA属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

## 8. 常见问题与解决方案

### 8.1 日期格式转换问题
**问题**：选择的日期格式与后端要求不一致
**解决方案**：使用 format 属性设置正确的日期格式，或在 onChange 事件中进行格式转换

### 8.2 禁用日期不生效
**问题**：设置了 disabledDate 函数，但禁用日期不生效
**解决方案**：检查 disabledDate 函数的返回值，确保返回布尔值，且日期对象的比较正确

### 8.3 日期范围选择顺序问题
**问题**：用户可以选择结束日期早于开始日期的范围
**解决方案**：框架内置了范围选择的顺序验证，自动调整选择顺序

### 8.4 快捷选择选项不显示
**问题**：设置了 quickSelectOptions，但快捷选择面板不显示
**解决方案**：确保同时设置了 showQuickSelect: true

### 8.5 国际化支持问题
**问题**：日期选择器的月份和星期显示为英文，需要中文显示
**解决方案**：设置框架的国际化配置，或自定义月份和星期的显示文本

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他日期选择方案 |

## 10. 相关组件

- **TimePicker**：时间选择器，用于选择具体时间
- **Input**：基础输入框组件
- **ComboInput**：组合输入框组件
- **Select**：下拉选择组件

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本日期选择功能 | 2025-01-15 |
| 1.1.0 | 新增日期范围选择功能 | 2025-03-20 |
| 1.2.0 | 新增快捷选择面板 | 2025-05-10 |
| 1.3.0 | 增强禁用日期功能，支持函数式配置 | 2025-07-05 |
| 1.4.0 | 优化键盘导航支持 | 2025-09-15 |
| 1.5.0 | 增强国际化支持 | 2025-11-20 |

## 12. 扩展阅读

- [日期和时间格式指南](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Date/format)
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