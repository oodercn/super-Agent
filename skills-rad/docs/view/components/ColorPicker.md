# ColorPicker 组件

## 1. 组件概述
ColorPicker 是 ooder-a2ui 框架中的颜色选择器组件，用于提供直观的颜色选择界面，支持多种颜色模式和高级颜色选择功能。该组件支持 RGB、HEX 和 HSV 三种颜色模式，提供预设颜色列表和高级颜色选择器，适用于各种需要颜色选择的场景。

## 2. 组件特性
- 支持 RGB、HEX、HSV 三种颜色模式
- 提供丰富的预设颜色列表
- 支持高级颜色选择器（HSV 色轮）
- 支持透明色选择
- 支持主题切换（浅色/深色）
- 响应式设计，适配不同屏幕尺寸
- 增强的可访问性支持
- 支持颜色名称显示
- 支持拖拽调整颜色值
- 支持事件处理

## 3. 组件创建

### 3.1 JSON格式创建
```json
{
  "type": "ColorPicker",
  "id": "colorpicker1",
  "properties": {
    "value": "FFFFFF",
    "theme": "light",
    "responsive": true,
    "advance": false
  }
}
```

### 3.2 JavaScript格式创建
```javascript
var colorPicker = ood.create("ood.UI.ColorPicker")
    .setId("colorpicker1")
    .setValue("FFFFFF")
    .setTheme("light")
    .setResponsive(true)
    .setAdvance(false);
```

## 4. 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| theme | String | "light" | 主题样式，可选值：light, dark |
| responsive | Boolean | true | 是否启用响应式布局 |
| value | String | "FFFFFF" | 初始颜色值，格式为十六进制字符串 |
| barDisplay | Boolean | true | 是否显示顶部工具栏 |
| closeBtn | Boolean | true | 是否显示关闭按钮 |
| advance | Boolean | false | 是否启用高级颜色选择器 |
| height | String | "auto" | 组件高度，只读属性 |
| width | String | "auto" | 组件宽度，只读属性 |

## 5. 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| activate() | 无 | this | 激活组件，设置焦点到切换按钮 |
| getColorName() | 无 | String | 获取当前颜色的名称 |
| setTheme(theme) | theme: String - 主题名称 | this | 设置组件主题 |
| getTheme() | 无 | String | 获取当前主题 |
| toggleDarkMode() | 无 | this | 切换深色/浅色模式 |
| adjustLayout() | 无 | this | 调整响应式布局 |
| enhanceAccessibility() | 无 | this | 增强可访问性支持 |
| ColorPickerTrigger() | 无 | void | 颜色选择器触发器，初始化主题和响应式设计 |

## 6. 事件处理

| 事件名 | 触发条件 | 回调参数 |
|--------|----------|----------|
| beforeClose | 关闭组件前 | profile: Object - 组件配置, src: Object - 触发源 |

## 7. 组件示例

### 7.1 基本颜色选择器
```json
{
  "type": "ColorPicker",
  "id": "basicColorPicker",
  "properties": {
    "value": "FF5733",
    "barDisplay": true,
    "closeBtn": true,
    "advance": false
  }
}
```

### 7.2 深色主题颜色选择器
```javascript
var darkColorPicker = ood.create("ood.UI.ColorPicker")
    .setId("darkColorPicker")
    .setValue("3498DB")
    .setTheme("dark")
    .setResponsive(true);
```

### 7.3 启用高级颜色选择器
```json
{
  "type": "ColorPicker",
  "id": "advancedColorPicker",
  "properties": {
    "value": "2ECC71",
    "advance": true,
    "barDisplay": true,
    "closeBtn": true
  }
}
```

### 7.4 监听关闭事件
```javascript
var eventColorPicker = ood.create("ood.UI.ColorPicker")
    .setId("eventColorPicker")
    .setValue("E74C3C");

// 添加自定义事件处理
ood.merge(eventColorPicker.properties.events, {
    beforeClose: function(profile, src) {
        console.log("颜色选择器即将关闭，当前选择的颜色是:", profile.properties.value);
        // 返回false可以阻止关闭
        return true;
    }
});
```

## 8. 颜色模式

ColorPicker 组件支持三种颜色模式，用户可以通过界面上的输入框调整不同模式下的颜色值：

### 8.1 RGB模式
- R: 红色通道值（0-255）
- G: 绿色通道值（0-255）
- B: 蓝色通道值（0-255）

### 8.2 HEX模式
- 十六进制颜色值，格式为六位字符串（如：FFFFFF）
- 支持拖拽调整每个十六进制位

### 8.3 HSV模式
- H: 色相（0-360°）
- S: 饱和度（0-100%）
- V: 明度（0-100%）

## 9. 主题设置

ColorPicker 组件支持两种主题：

### 9.1 浅色主题（light）
默认主题，适合大多数场景使用。

### 9.2 深色主题（dark）
适合在深色背景下使用，减少视觉疲劳。

## 10. 响应式设计

ColorPicker 组件支持响应式设计，会根据屏幕尺寸自动调整布局：

- **大屏幕（≥768px）**：完整显示颜色选择器界面
- **小屏幕（<768px）**：优化移动端布局，调整输入框和颜色块大小
- **超小屏幕（<480px）**：进一步调整布局，减小字体大小

## 11. 可访问性支持

ColorPicker 组件增强了可访问性支持：

- 添加了适当的 ARIA 属性
- 支持键盘导航
- 提供清晰的视觉反馈
- 支持屏幕阅读器
- 为颜色块和输入框添加了描述性标签

## 12. 最佳实践

1. **初始颜色设置**：
   - 根据应用场景设置合适的初始颜色
   - 考虑使用品牌色或常用颜色作为默认值

2. **主题选择**：
   - 根据应用的整体风格选择合适的主题
   - 考虑用户的使用环境和偏好
   - 支持用户自定义主题

3. **高级功能使用**：
   - 对于专业用户，考虑启用高级颜色选择器
   - 对于普通用户，默认关闭高级功能，简化界面

4. **事件处理**：
   - 合理使用beforeClose事件，提供良好的用户体验
   - 处理关闭事件，保存用户选择的颜色值

5. **响应式设计**：
   - 确保在不同屏幕尺寸下都有良好的用户体验
   - 考虑移动设备的使用场景

6. **性能优化**：
   - 避免频繁创建和销毁颜色选择器组件
   - 合理使用事件处理，避免不必要的性能开销

## 13. 浏览器兼容性

ColorPicker 组件支持所有现代浏览器：

- Chrome 20+
- Firefox 15+
- Safari 6+
- Internet Explorer 10+
- Edge 12+

## 14. 常见问题

### 14.1 颜色值不生效
- 检查颜色值格式是否正确（六位十六进制字符串）
- 确保颜色值不包含#符号
- 检查组件是否正确初始化

### 14.2 主题切换不生效
- 确保主题名称正确（light, dark）
- 检查组件是否正确初始化
- 尝试手动调用setTheme方法

### 14.3 高级颜色选择器不显示
- 确保advance属性设置为true
- 检查组件是否正确初始化
- 查看浏览器控制台是否有错误信息

### 14.4 响应式布局不生效
- 确保responsive属性设置为true
- 检查浏览器窗口大小是否符合响应式断点
- 尝试手动调用adjustLayout方法

## 15. 总结

ColorPicker 组件是一个功能强大的颜色选择器组件，提供了丰富的颜色选择功能和现代化的用户界面。它支持多种颜色模式、主题切换、响应式设计和增强的可访问性支持，适合各种颜色选择场景。

通过合理配置和使用 ColorPicker 组件，可以为用户提供良好的颜色选择体验，同时简化开发工作。组件的主题切换和响应式设计使其能够适应不同的使用环境和设备，增强的可访问性支持提高了组件的可用性。

ColorPicker 组件的应用范围包括但不限于：
- 网页设计工具
- 图形编辑软件
- 主题定制工具
- 表单颜色选择
- 数据可视化工具
- 富文本编辑器

作为 ooder-a2ui 框架的重要组成部分，ColorPicker 组件为 Web 应用提供了可靠的颜色选择解决方案。