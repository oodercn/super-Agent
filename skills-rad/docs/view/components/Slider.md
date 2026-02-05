# Slider 组件

## 1. 组件概述
Slider 是 ooder-a2ui 框架中的滑块组件，用于提供直观的数值选择界面，支持单滑块和范围滑块两种模式。该组件支持水平和垂直方向，提供主题切换、响应式设计和增强的可访问性支持，适用于各种需要数值选择的场景。

## 2. 组件特性
- 支持单滑块和范围滑块两种模式
- 支持水平和垂直两种方向
- 支持主题切换（浅色/深色）
- 响应式设计，适配不同屏幕尺寸
- 增强的可访问性支持
- 支持步长设置
- 支持标签显示
- 支持增加/减少按钮
- 支持键盘导航
- 支持拖拽操作
- 支持自定义数值格式

## 3. 组件创建

### 3.1 JSON格式创建
```json
{
  "type": "Slider",
  "id": "slider1",
  "properties": {
    "width": "15em",
    "height": "4em",
    "type": "horizontal",
    "isRange": true,
    "value": "0:100",
    "steps": 0,
    "precision": 0,
    "theme": "light",
    "responsive": true
  }
}
```

### 3.2 JavaScript格式创建
```javascript
var slider = ood.create("ood.UI.Slider")
    .setId("slider1")
    .setWidth("15em")
    .setHeight("4em")
    .setType("horizontal")
    .setIsRange(true)
    .setValue("0:100")
    .setSteps(0)
    .setPrecision(0)
    .setTheme("light")
    .setResponsive(true);
```

## 4. 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| theme | String | "light" | 主题样式，可选值：light, dark |
| responsive | Boolean | true | 是否启用响应式布局 |
| width | String/Number | "15em" | 组件宽度 |
| height | String/Number | "4em" | 组件高度 |
| type | String | "horizontal" | 滑块方向，可选值：horizontal, vertical |
| isRange | Boolean | true | 是否为范围滑块 |
| value | String | "0:0" | 滑块值，格式为"min:max" |
| steps | Number | 0 | 步长，0表示无步长限制 |
| precision | Number | 0 | 数值精度 |
| numberTpl | String | "* - 1% ~ 2%" | 数值模板，*表示标题，1表示最小值，2表示最大值 |
| showIncreaseHandle | Boolean | true | 是否显示增加按钮 |
| showDecreaseHandle | Boolean | true | 是否显示减少按钮 |
| labelSize | String/Number | "0" | 标签大小 |
| labelPos | String | "left" | 标签位置，可选值：none, left, top, right, bottom |
| labelGap | String/Number | "4" | 标签间距 |
| labelCaption | String | "" | 标签标题 |
| labelHAlign | String | "right" | 标签水平对齐方式，可选值：left, center, right |
| labelVAlign | String | "top" | 标签垂直对齐方式，可选值：top, middle, bottom |

## 5. 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| setTheme(theme) | theme: String - 主题名称 | this | 设置组件主题 |
| getTheme() | 无 | String | 获取当前主题 |
| toggleDarkMode() | 无 | this | 切换深色/浅色模式 |
| adjustLayout() | 无 | this | 调整响应式布局 |
| enhanceAccessibility() | 无 | this | 增强可访问性支持 |
| SliderTrigger() | 无 | void | 滑块触发器，初始化主题和响应式设计 |

## 6. 事件处理

| 事件名 | 触发条件 | 回调参数 |
|--------|----------|----------|
| onLabelClick | 点击标签时 | profile: Object - 组件配置, e: Event - 事件对象, src: Object - 触发源 |
| onLabelDblClick | 双击标签时 | profile: Object - 组件配置, e: Event - 事件对象, src: Object - 触发源 |
| onLabelActive | 标签激活时 | profile: Object - 组件配置, e: Event - 事件对象, src: Object - 触发源 |

## 7. 组件示例

### 7.1 基本滑块
```json
{
  "type": "Slider",
  "id": "basicSlider",
  "properties": {
    "width": "20em",
    "height": "4em",
    "type": "horizontal",
    "isRange": false,
    "value": "50",
    "steps": 0,
    "precision": 0
  }
}
```

### 7.2 范围滑块
```javascript
var rangeSlider = ood.create("ood.UI.Slider")
    .setId("rangeSlider")
    .setWidth("20em")
    .setHeight("4em")
    .setType("horizontal")
    .setIsRange(true)
    .setValue("20:80")
    .setSteps(0)
    .setPrecision(0);
```

### 7.3 垂直滑块
```json
{
  "type": "Slider",
  "id": "verticalSlider",
  "properties": {
    "width": "4em",
    "height": "15em",
    "type": "vertical",
    "isRange": false,
    "value": "75",
    "steps": 0,
    "precision": 0
  }
}
```

### 7.4 带步长的滑块
```javascript
var stepSlider = ood.create("ood.UI.Slider")
    .setId("stepSlider")
    .setWidth("20em")
    .setHeight("4em")
    .setType("horizontal")
    .setIsRange(true)
    .setValue("0:100")
    .setSteps(10)
    .setPrecision(0);
```

### 7.5 深色主题滑块
```json
{
  "type": "Slider",
  "id": "darkSlider",
  "properties": {
    "width": "20em",
    "height": "4em",
    "type": "horizontal",
    "isRange": true,
    "value": "30:70",
    "steps": 0,
    "precision": 0,
    "theme": "dark"
  }
}
```

## 8. 主题设置

Slider 组件支持两种主题：

### 8.1 浅色主题（light）
默认主题，适合大多数场景使用。

### 8.2 深色主题（dark）
适合在深色背景下使用，减少视觉疲劳。

## 9. 响应式设计

Slider 组件支持响应式设计，会根据屏幕尺寸自动调整布局：

- **大屏幕（≥768px）**：完整显示滑块界面
- **小屏幕（<768px）**：优化移动端布局，增大滑块手柄尺寸
- **超小屏幕（<480px）**：进一步调整布局，增大触摸区域

## 10. 可访问性支持

Slider 组件增强了可访问性支持：

- 添加了适当的 ARIA 属性
- 支持键盘导航
- 提供清晰的视觉反馈
- 支持屏幕阅读器
- 为滑块和手柄添加了描述性标签

## 11. 最佳实践

1. **滑块类型选择**：
   - 对于单一数值选择，使用单滑块模式
   - 对于数值范围选择，使用范围滑块模式
   - 根据界面布局选择水平或垂直方向

2. **步长设置**：
   - 对于连续数值，步长设置为0
   - 对于离散数值，设置合适的步长
   - 步长不宜过小或过大，确保良好的用户体验

3. **主题选择**：
   - 根据应用的整体风格选择合适的主题
   - 考虑用户的使用环境和偏好
   - 支持用户自定义主题

4. **标签设置**：
   - 合理设置标签位置和大小
   - 使用清晰的标签标题
   - 考虑在移动设备上的显示效果

5. **事件处理**：
   - 合理使用标签事件，提供良好的用户体验
   - 处理滑块值变化事件，及时更新相关数据

6. **响应式设计**：
   - 确保在不同屏幕尺寸下都有良好的用户体验
   - 考虑移动设备的使用场景

7. **性能优化**：
   - 避免频繁创建和销毁滑块组件
   - 合理使用事件处理，避免不必要的性能开销

## 12. 浏览器兼容性

Slider 组件支持所有现代浏览器：

- Chrome 20+
- Firefox 15+
- Safari 6+
- Internet Explorer 10+
- Edge 12+

## 13. 常见问题

### 13.1 滑块值不生效
- 检查值的格式是否正确（单滑块为数值，范围滑块为"min:max"格式）
- 确保值在最小值和最大值范围内
- 检查组件是否正确初始化

### 13.2 主题切换不生效
- 确保主题名称正确（light, dark）
- 检查组件是否正确初始化
- 尝试手动调用setTheme方法

### 13.3 响应式布局不生效
- 确保responsive属性设置为true
- 检查浏览器窗口大小是否符合响应式断点
- 尝试手动调用adjustLayout方法

### 13.4 步长设置不生效
- 确保steps属性设置为大于0的数值
- 检查组件是否正确初始化
- 查看浏览器控制台是否有错误信息

## 14. 总结

Slider 组件是一个功能强大的滑块组件，提供了丰富的数值选择功能和现代化的用户界面。它支持单滑块和范围滑块两种模式，水平和垂直两种方向，主题切换，响应式设计和增强的可访问性支持，适合各种数值选择场景。

通过合理配置和使用 Slider 组件，可以为用户提供良好的数值选择体验，同时简化开发工作。组件的主题切换和响应式设计使其能够适应不同的使用环境和设备，增强的可访问性支持提高了组件的可用性。

Slider 组件的应用范围包括但不限于：
- 音量控制
- 亮度调节
- 价格范围选择
- 时间范围选择
- 进度显示
- 数值参数调整

作为 ooder-a2ui 框架的重要组成部分，Slider 组件为 Web 应用提供了可靠的数值选择解决方案。