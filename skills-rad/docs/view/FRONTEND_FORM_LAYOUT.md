# FormLayout 组件使用指南

## 1. 概述

FormLayout 组件是 ooder-agent-rad 前端框架中用于表单元素布局的核心组件，支持网格布局、响应式设计、主题切换和无障碍访问。它提供了直观的方式来组织和排列表单元素，适用于各种复杂的表单场景。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.FormLayout
```

### 2.2 继承关系

```
ood.UI.FormLayout extends ood.UI and ood.absList
```

### 2.3 主要功能

- 网格布局：支持行列配置和单元格合并
- 响应式设计：自动适应不同屏幕尺寸
- 主题支持：支持浅色/深色主题切换
- 无障碍访问：支持 ARIA 属性和键盘导航
- 设计模式：支持可视化设计和编辑
- 自定义样式：支持自定义单元格样式和边框

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 FormLayout 组件：

```javascript
var formLayout = ood.create("ood.UI.FormLayout")
    .setHost(host, "formLayout")
    .setName("formLayout")
    .setWidth("100%")
    .setHeight("100%")
    .setVisibility("visible")
    .setOverflow("visible");
```

### 3.2 布局数据配置

通过 `layoutData` 属性配置表单布局：

```javascript
var formLayout = ood.create("ood.UI.FormLayout")
    .setHost(host, "formLayout")
    .setName("formLayout")
    .setLayoutData({
        rows: 3,
        cols: 2,
        merged: [
            {
                row: 0,
                col: 0,
                rowspan: 1,
                colspan: 2,
                removed: false
            }
        ],
        rowSetting: {
            "1": { manualHeight: 56 },
            "2": { manualHeight: 35 },
            "3": { manualHeight: 142 }
        },
        colSetting: {
            "A": { manualWidth: 150 },
            "B": { manualWidth: 525 }
        },
        cells: {
            "A1": {
                value: "标题",
                style: { textAlign: "center", fontSize: "28px" }
            },
            "A2": {
                value: "名称",
                style: { textAlign: "center" }
            },
            "B2": {
                value: "张三",
                style: { textAlign: "left", paddingLeft: "10px" }
            },
            "A3": {
                value: "描述",
                style: { textAlign: "center" }
            },
            "B3": {
                value: "这是一个示例表单",
                style: { textAlign: "left", paddingLeft: "10px", fontSize: "14px", color: "#666" }
            }
        }
    });
```

## 4. 核心属性

### 4.1 基础属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| name | String | "TestForm" | 表单名称 |
| width | String | "50em" | 表单宽度 |
| height | String | "20em" | 表单高度 |
| visibility | String | "visible" | 可见性 |
| floatHandler | Boolean | false | 是否启用浮动处理 |
| defaultRowHeight | Number | 35 | 默认行高 |
| defaultColWidth | Number | 150 | 默认列宽 |

### 4.2 布局属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| layoutData | Object | {} | 布局数据，包含行列配置和单元格信息 |
| solidGridlines | Boolean | true | 是否显示实线网格 |
| stretchHeight | String | "none" | 高度拉伸方式（none/last/all） |
| stretchH | String | "all" | 水平拉伸方式（all/none/last） |
| rowHeaderWidth | Number | 25 | 行标题宽度 |
| columnHeaderHeight | Number | 25 | 列标题高度 |

### 4.3 现代化属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| theme | String | "light" | 主题（light/dark） |
| responsive | Boolean | true | 是否启用响应式设计 |

## 5. 方法详解

### 5.1 主题管理

#### setTheme(theme)

设置表单主题：

```javascript
formLayout.setTheme("dark"); // 设置为深色主题
```

#### getTheme()

获取当前主题：

```javascript
var currentTheme = formLayout.getTheme();
```

#### toggleDarkMode()

切换暗黑模式：

```javascript
formLayout.toggleDarkMode();
```

#### toggleHighContrast()

切换高对比度模式：

```javascript
formLayout.toggleHighContrast();
```

### 5.2 响应式设计

#### adjustLayout()

调整布局以适应屏幕尺寸：

```javascript
formLayout.adjustLayout();
```

### 5.3 无障碍访问

#### enhanceAccessibility()

增强无障碍访问支持：

```javascript
formLayout.enhanceAccessibility();
```

### 5.4 布局管理

#### setLayoutData(layoutData)

设置布局数据：

```javascript
formLayout.setLayoutData({
    rows: 2,
    cols: 2,
    cells: {
        "A1": { value: "标签1", style: { textAlign: "right" } },
        "B1": { value: "值1" },
        "A2": { value: "标签2", style: { textAlign: "right" } },
        "B2": { value: "值2" }
    }
});
```

#### setSolidGridlines(solid, force)

设置是否显示实线网格：

```javascript
formLayout.setSolidGridlines(true, true);
```

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| onShowTips | 显示提示时触发 |
| onGetCellData | 获取单元格数据时触发 |
| onLayoutChanged | 布局改变时触发 |

### 6.2 事件设置

```javascript
// 监听布局改变事件
formLayout.onLayoutChanged = function(profile, oldData, newData) {
    console.log("布局已改变:", oldData, newData);
};
```

## 7. 响应式设计

### 7.1 断点设计

FormLayout 组件根据屏幕宽度自动调整布局：

| 屏幕宽度 | 布局调整 |
|---------|----------|
| < 480px | 小屏幕模式，进一步压缩间距 |
| < 768px | 移动端模式，调整字体大小和内边距 |
| ≥ 768px | 桌面模式，正常布局 |

### 7.2 响应式样式

组件会自动添加相应的 CSS 类名：

- `formlayout-mobile`：移动端样式
- `formlayout-tiny`：小屏幕样式

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
formLayout.setTheme("dark");

// 切换主题
formLayout.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整颜色、背景和边框样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA 属性

组件自动添加 ARIA 属性：

```html
<div role="grid" aria-label="表单布局网格">
    <table role="table" aria-label="表单布局表格">
        <td role="gridcell" aria-label="单元格 A1" tabindex="0"></td>
    </table>
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab 键：在单元格之间导航
- 方向键：在单元格之间移动

## 10. 设计模式

### 10.1 设计模式切换

```javascript
formLayout.setMode("design"); // 设计模式
formLayout.setMode("write"); // 写入模式
formLayout.setMode("read"); // 只读模式
```

### 10.2 可视化编辑

在设计模式下，FormLayout 组件使用 Handsontable 库提供可视化编辑功能：

- 拖拽调整行列大小
- 合并/拆分单元格
- 调整单元格样式
- 配置单元格边框

## 11. 示例代码

### 11.1 基本表单布局

```javascript
var host = this;
var formLayout = ood.create("ood.UI.FormLayout")
    .setHost(host, "formLayout")
    .setName("formLayout")
    .setWidth("100%")
    .setHeight("300px")
    .setVisibility("visible")
    .setOverflow("visible")
    .setLayoutData({
        rows: 4,
        cols: 2,
        rowSetting: {
            "1": { manualHeight: 40 },
            "2": { manualHeight: 40 },
            "3": { manualHeight: 40 },
            "4": { manualHeight: 40 }
        },
        colSetting: {
            "A": { manualWidth: 120 },
            "B": { manualWidth: 300 }
        },
        cells: {
            "A1": { value: "姓名", style: { textAlign: "right", paddingRight: "10px", fontWeight: "bold" } },
            "B1": { value: "" },
            "A2": { value: "年龄", style: { textAlign: "right", paddingRight: "10px", fontWeight: "bold" } },
            "B2": { value: "" },
            "A3": { value: "性别", style: { textAlign: "right", paddingRight: "10px", fontWeight: "bold" } },
            "B3": { value: "" },
            "A4": { value: "邮箱", style: { textAlign: "right", paddingRight: "10px", fontWeight: "bold" } },
            "B4": { value: "" }
        }
    });

// 添加到父组件
parentComponent.append(formLayout);
```

### 11.2 复杂表单布局

```javascript
var formLayout = ood.create("ood.UI.FormLayout")
    .setHost(host, "complexForm")
    .setName("complexForm")
    .setWidth("100%")
    .setHeight("500px")
    .setLayoutData({
        rows: 5,
        cols: 3,
        merged: [
            {
                row: 0,
                col: 0,
                rowspan: 1,
                colspan: 3,
                removed: false
            },
            {
                row: 4,
                col: 1,
                rowspan: 1,
                colspan: 2,
                removed: false
            }
        ],
        cells: {
            "A1": { 
                value: "复杂表单示例", 
                style: { 
                    textAlign: "center", 
                    fontSize: "24px", 
                    fontWeight: "bold",
                    color: "#2c3e50",
                    padding: "10px 0"
                } 
            },
            "A2": { value: "基本信息", style: { fontWeight: "bold", backgroundColor: "#f0f0f0" } },
            "B2": { value: "", style: { backgroundColor: "#f0f0f0" } },
            "C2": { value: "", style: { backgroundColor: "#f0f0f0" } },
            "A3": { value: "姓名", style: { textAlign: "right", paddingRight: "10px" } },
            "B3": { value: "" },
            "C3": { value: "年龄", style: { textAlign: "right", paddingRight: "10px" } },
            "A4": { value: "性别", style: { textAlign: "right", paddingRight: "10px" } },
            "B4": { value: "" },
            "C4": { value: "邮箱", style: { textAlign: "right", paddingRight: "10px" } },
            "A5": { value: "地址", style: { textAlign: "right", paddingRight: "10px" } },
            "B5": { value: "" }
        }
    });
```

## 10. 最佳实践

### 10.1 布局设计

1. **合理规划行列**：根据表单复杂度规划合适的行列数
2. **使用单元格合并**：对于标题和说明文本，使用单元格合并
3. **统一对齐方式**：标签列右对齐，值列左对齐
4. **适当的行高和列宽**：设置合适的行高和列宽，保证良好的可读性

### 10.2 性能优化

1. **减少不必要的单元格**：只创建需要的单元格
2. **合理使用样式**：避免过度使用复杂样式
3. **启用响应式设计**：适应不同设备
4. **使用缓存**：对于复杂表单，考虑使用缓存

### 10.3 可维护性

1. **模块化设计**：将复杂表单拆分为多个模块
2. **清晰的命名**：使用清晰的名称和注释
3. **统一的风格**：保持表单风格的一致性
4. **文档化**：为复杂表单添加文档说明

## 11. 常见问题

### 11.1 布局显示异常

- **问题**：表单布局显示不正确
- **解决方案**：
  1. 检查 `layoutData` 配置是否正确
  2. 确保已设置正确的宽度和高度
  3. 检查是否有样式冲突

### 11.2 主题切换无效

- **问题**：主题切换后样式没有变化
- **解决方案**：
  1. 确保已调用 `setTheme()` 方法
  2. 检查主题 CSS 文件是否已加载
  3. 检查浏览器缓存

### 11.3 响应式设计不生效

- **问题**：在不同设备上显示效果相同
- **解决方案**：
  1. 确保已启用响应式设计（`responsive: true`）
  2. 检查 `adjustLayout()` 方法是否被调用
  3. 检查 CSS 媒体查询是否正确

### 11.4 无障碍访问不支持

- **问题**：屏幕阅读器无法正确读取表单
- **解决方案**：
  1. 调用 `enhanceAccessibility()` 方法
  2. 检查 ARIA 属性是否已正确添加
  3. 测试键盘导航功能

## 12. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础表单布局功能 |
| v1.1.0 | 响应式设计支持 |
| v1.2.0 | 主题切换和无障碍访问支持 |
| v1.3.0 | 设计模式和可视化编辑支持 |

## 13. 总结

FormLayout 组件是一个功能强大的表单布局组件，支持网格布局、响应式设计、主题切换和无障碍访问。它提供了直观的方式来组织和排列表单元素，适用于各种复杂的表单场景。通过合理使用 FormLayout 组件，可以提高表单的可读性和易用性，同时减少开发工作量。

## 14. 参考资源

- [FormLayout 组件 API 文档](https://docs.example.com/components/formlayout)
- [FormLayout 组件示例代码](https://github.com/example/formlayout-examples)
- [表单设计最佳实践](https://design.example.com/forms)
