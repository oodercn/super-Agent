# Select 组件

## 1. 组件概述
Select 组件是 ooder-a2ui 前端框架中的一个下拉选择组件，用于从预设选项中选择一个或多个值，支持搜索、分组、自定义选项等功能。

### 1.1 核心功能
- 支持单选和多选模式
- 支持搜索过滤选项
- 支持选项分组
- 支持自定义选项模板
- 支持远程数据加载
- 支持虚拟滚动（大量选项时）
- 支持键盘导航
- 支持主题定制
- 支持国际化

### 1.2 适用场景
- 表单中的下拉选择输入
- 数据查询的筛选条件
- 分类选择器
- 标签选择器（多选模式）
- 国家/地区选择器
- 部门/人员选择器

## 2. 创建方法

### 2.1 JSON 方式创建
```json
{
  "id": "select1",
  "type": "Select",
  "props": {
    "value": "option2",
    "options": [
      { "label": "选项1", "value": "option1" },
      { "label": "选项2", "value": "option2" },
      { "label": "选项3", "value": "option3" }
    ],
    "placeholder": "请选择选项",
    "disabled": false,
    "showClearBtn": true
  }
}
```

### 2.2 JavaScript 方式创建
```javascript
const select = ood.create("Select", {
  id: "select1",
  type: "multiple",
  value: ["option1", "option3"],
  options: [
    { label: "选项1", "value": "option1" },
    { label: "选项2", "value": "option2" },
    { label: "选项3", "value": "option3" }
  ],
  placeholder: "请选择选项",
  showSearch: true,
  filterOption: (input, option) => {
    return option.label.toLowerCase().includes(input.toLowerCase());
  }
});

// 添加到表单
form.addChild(select);

// 监听选择变化事件
select.on("onChange", (value) => {
  console.log("选择的选项:", value);
});
```

## 3. 属性列表

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| **value** | string/array | null | 选中的选项值，单选为字符串，多选为数组 |
| **type** | string | "single" | 选择类型："single"（单选）或 "multiple"（多选） |
| **options** | array | [] | 选项列表，每个选项包含 label 和 value 属性 |
| **placeholder** | string | "请选择" | 占位文本 |
| **disabled** | boolean | false | 是否禁用组件 |
| **readonly** | boolean | false | 是否只读 |
| **showClearBtn** | boolean | true | 是否显示清除按钮 |
| **showSearch** | boolean | false | 是否显示搜索框 |
| **filterOption** | boolean/function | true | 搜索过滤选项的方式，true 表示默认过滤，false 表示关闭过滤，函数表示自定义过滤 |
| **allowCreate** | boolean | false | 是否允许创建新选项 |
| **maxTagCount** | number | 5 | 多选模式下最多显示的标签数量 |
| **maxTagPlaceholder** | function | null | 多选模式下标签数量超过限制时的占位符 |
| **loading** | boolean | false | 是否显示加载状态 |
| **remote** | boolean | false | 是否远程加载数据 |
| **remoteMethod** | function | null | 远程加载数据的方法 |
| **width** | number/string | 200 | 组件宽度 |
| **size** | string | "medium" | 组件尺寸："small"、"medium"、"large" |
| **borderRadius** | number | 4 | 边框圆角 |
| **className** | string | "" | 自定义CSS类名 |

## 4. 方法列表

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **getValue** | `getValue()` | 获取当前选中的选项值 |
| **setValue** | `setValue(value)` | 设置选中的选项值 |
| **clearValue** | `clearValue()` | 清除选中的选项值 |
| **getOptions** | `getOptions()` | 获取选项列表 |
| **setOptions** | `setOptions(options)` | 设置选项列表 |
| **addOption** | `addOption(option)` | 添加单个选项 |
| **removeOption** | `removeOption(value)` | 移除指定值的选项 |
| **show** | `show()` | 显示下拉面板 |
| **hide** | `hide()` | 隐藏下拉面板 |
| **toggle** | `toggle()` | 切换下拉面板的显示/隐藏状态 |
| **setDisabled** | `setDisabled(disabled)` | 设置组件是否禁用 |
| **setReadonly** | `setReadonly(readonly)` | 设置组件是否只读 |
| **setLoading** | `setLoading(loading)` | 设置加载状态 |
| **validate** | `validate()` | 验证选择是否有效 |

## 5. 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| **onChange** | 选择变化时触发 | `value` - 选中的选项值 |
| **onSelect** | 选中某个选项时触发 | `option` - 选中的选项对象 |
| **onDeselect** | 取消选中某个选项时触发（多选模式） | `option` - 取消选中的选项对象 |
| **onSearch** | 搜索输入时触发 | `keyword` - 搜索关键词 |
| **onFocus** | 组件获得焦点时触发 | `event` - 焦点事件对象 |
| **onBlur** | 组件失去焦点时触发 | `event` - 失焦事件对象 |
| **onClear** | 点击清除按钮时触发 | 无 |
| **onCreate** | 创建新选项时触发（allowCreate 为 true 时） | `value` - 新选项的值 |

## 6. 示例代码

### 6.1 基本选择器
```json
{
  "id": "basicSelect",
  "type": "Select",
  "props": {
    "value": "option2",
    "options": [
      { "label": "选项1", "value": "option1" },
      { "label": "选项2", "value": "option2" },
      { "label": "选项3", "value": "option3" }
    ],
    "placeholder": "请选择选项",
    "showClearBtn": true,
    "width": 200
  }
}
```

### 6.2 多选选择器
```javascript
const multipleSelect = ood.create("Select", {
  id: "multipleSelect",
  type: "multiple",
  value: ["option1", "option3"],
  options: [
    { "label": "选项1", "value": "option1" },
    { "label": "选项2", "value": "option2" },
    { "label": "选项3", "value": "option3" },
    { "label": "选项4", "value": "option4" },
    { "label": "选项5", "value": "option5" }
  ],
  placeholder: "请选择选项",
  maxTagCount: 3,
  maxTagPlaceholder: (count) => `+${count} 个选项`,
  width: 300
});

// 监听选择变化
multipleSelect.on("onChange", (value) => {
  console.log("选择的选项数量:", value.length);
  console.log("选择的选项:", value);
});

// 添加到页面
page.addChild(multipleSelect);
```

### 6.3 带搜索功能的选择器
```json
{
  "id": "searchSelect",
  "type": "Select",
  "props": {
    "value": null,
    "options": [
      { "label": "中国", "value": "CN" },
      { "label": "美国", "value": "US" },
      { "label": "日本", "value": "JP" },
      { "label": "英国", "value": "GB" },
      { "label": "法国", "value": "FR" },
      { "label": "德国", "value": "DE" }
    ],
    "placeholder": "请选择国家",
    "showSearch": true,
    "filterOption": true,
    "width": 250
  }
}
```

### 6.4 分组选择器
```javascript
const groupSelect = ood.create("Select", {
  id: "groupSelect",
  value: "apple",
  options: [
    {
      label: "水果",
      options: [
        { "label": "苹果", "value": "apple" },
        { "label": "香蕉", "value": "banana" },
        { "label": "橙子", "value": "orange" }
      ]
    },
    {
      label: "蔬菜",
      options: [
        { "label": "白菜", "value": "cabbage" },
        { "label": "萝卜", "value": "radish" },
        { "label": "西红柿", "value": "tomato" }
      ]
    }
  ],
  placeholder: "请选择食材",
  showSearch: true,
  width: 250
});

// 添加到表单
form.addChild(groupSelect);
```

### 6.5 远程搜索选择器
```javascript
const remoteSelect = ood.create("Select", {
  id: "remoteSelect",
  value: null,
  placeholder: "请输入关键词搜索",
  showSearch: true,
  remote: true,
  loading: false,
  filterOption: false,
  remoteMethod: (keyword) => {
    // 显示加载状态
    remoteSelect.setLoading(true);
    
    // 模拟远程请求
    setTimeout(() => {
      // 模拟搜索结果
      const mockResults = [
        { "label": `搜索结果1: ${keyword}`, "value": "result1" },
        { "label": `搜索结果2: ${keyword}`, "value": "result2" },
        { "label": `搜索结果3: ${keyword}`, "value": "result3" }
      ];
      
      // 设置选项
      remoteSelect.setOptions(mockResults);
      
      // 隐藏加载状态
      remoteSelect.setLoading(false);
    }, 1000);
  },
  width: 300
});

// 添加到页面
page.addChild(remoteSelect);
```

## 7. 最佳实践

### 7.1 选项设计
- 选项的 label 应清晰易懂，value 应唯一且稳定
- 对于大量选项，建议使用搜索功能或分页加载
- 对于相关的选项，建议使用分组功能
- 避免选项过多导致性能问题，建议使用虚拟滚动

### 7.2 多选设计
- 合理设置 maxTagCount，避免标签过多影响布局
- 提供清晰的视觉反馈，显示已选数量
- 对于重要的多选场景，考虑提供全选/取消全选功能

### 7.3 搜索功能
- 对于选项数量超过 20 个的情况，建议启用搜索功能
- 自定义搜索过滤逻辑时，保持搜索响应速度
- 远程搜索时，添加适当的延迟和防抖处理

### 7.4 性能优化
- 对于大量选项（超过 1000 个），建议使用虚拟滚动
- 远程加载数据时，实现合理的缓存机制
- 避免在 filterOption 函数中执行复杂计算
- 合理使用 loading 状态，提供良好的用户体验

### 7.5 可访问性
- 确保组件支持键盘导航
- 提供清晰的 ARIA 属性
- 支持屏幕阅读器
- 提供足够的颜色对比度

## 8. 常见问题与解决方案

### 8.1 选项不显示
**问题**：设置了 options，但选项不显示
**解决方案**：检查 options 格式是否正确，每个选项必须包含 label 和 value 属性

### 8.2 搜索功能不生效
**问题**：设置了 showSearch: true，但搜索功能不生效
**解决方案**：确保 filterOption 为 true 或自定义过滤函数，且 options 不为空

### 8.3 多选模式下无法取消选择
**问题**：在多选模式下，选中的选项无法取消选择
**解决方案**：确保没有禁用组件，且 readonly 为 false

### 8.4 远程加载数据不显示
**问题**：调用 remoteMethod 加载数据后，选项不显示
**解决方案**：确保在 remoteMethod 中正确调用 setOptions 方法设置选项

### 8.5 性能问题
**问题**：选项数量过多，导致组件卡顿
**解决方案**：启用虚拟滚动，或使用分页加载，或减少选项数量

## 9. 浏览器兼容性

| 浏览器 | 支持版本 | 注意事项 |
|--------|----------|----------|
| Chrome | 60+ | 完全支持 |
| Firefox | 55+ | 完全支持 |
| Safari | 12+ | 完全支持 |
| Edge | 79+ | 完全支持 |
| IE11 | 不支持 | 建议使用其他组件 |

## 10. 相关组件

- **ComboInput**：组合输入框组件
- **Input**：基础输入框组件
- **CheckBox**：复选框组件
- **RadioBox**：单选框组件

## 11. 升级与更新历史

| 版本 | 更新内容 | 日期 |
|------|----------|------|
| 1.0.0 | 初始版本，支持基本选择功能 | 2025-01-15 |
| 1.1.0 | 新增多选模式和搜索功能 | 2025-03-20 |
| 1.2.0 | 新增选项分组功能 | 2025-05-10 |
| 1.3.0 | 新增远程数据加载功能 | 2025-07-05 |
| 1.4.0 | 新增虚拟滚动支持 | 2025-09-15 |
| 1.5.0 | 优化键盘导航和可访问性 | 2025-11-20 |

## 12. 扩展阅读

- [ooder-a2ui 表单组件最佳实践](FRONTEND_COMPONENTS.md#表单组件最佳实践)
- [响应式设计在表单中的应用](https://web.dev/responsive-design/)
- [虚拟滚动技术原理](https://developer.mozilla.org/zh-CN/docs/Web/Performance/Virtual_scrolling)

## 13. 贡献与反馈

如有任何问题或建议，请通过以下方式反馈：
- 提交Issue：[GitHub Issues](https://github.com/ooder/ooder-pro/issues)
- 邮件反馈：support@ooder.com
- 社区论坛：[ooder社区](https://community.ooder.com)

---

**最后更新时间**：2026-01-25  
**文档版本**：1.0  
**作者**：ooder-a2ui 开发团队