# OODER 组件体系指南

## 1. 组件继承层次结构

```
ood.UI (核心基础类)
├── ood.UI.Widget (基础组件)
├── ood.UI.Link (链接组件)
├── ood.UI.Element (基础元素组件)
│   └── ood.UI.HTMLButton (HTML按钮组件)
│       └── ood.UI.Button (按钮组件) - 同时继承 ood.absValue
├── ood.UI.Icon (图标组件)
├── ood.UI.Span (文本容器组件)
│   └── ood.UI.CSSBox (CSS盒子组件)
└── ood.UI.Div (容器组件)
    └── ood.UI.MoudluePlaceHolder (模块占位符组件)
```

## 2. 组件分类与核心功能

### 2.1 基础核心组件

| 组件类名 | 继承关系 | 核心功能 | 主要用途 |
|---------|---------|---------|---------|
| ood.UI | - | 所有UI组件的基类 | 定义通用属性和方法 |
| ood.UI.Widget | ood.UI | 基础组件类 | 构建复杂组件的基础 |
| ood.UI.Element | ood.UI | 基础元素组件 | 封装HTML元素 |

### 2.2 容器组件

| 组件类名 | 继承关系 | 核心功能 | 主要用途 |
|---------|---------|---------|---------|
| ood.UI.Div | ood.UI | 通用容器组件 | 页面布局、内容容器 |
| ood.UI.Span | ood.UI | 文本容器组件 | 文本包装、行内元素 |
| ood.UI.CSSBox | ood.UI.Span | CSS盒子组件 | 样式化容器 |

### 2.3 交互组件

| 组件类名 | 继承关系 | 核心功能 | 主要用途 |
|---------|---------|---------|---------|
| ood.UI.Button | ood.UI.HTMLButton, ood.absValue | 按钮组件 | 触发操作、提交表单 |
| ood.UI.Link | ood.UI | 链接组件 | 页面跳转、导航 |
| ood.UI.Icon | ood.UI | 图标组件 | 显示图标、装饰元素 |

### 2.4 特殊组件

| 组件类名 | 继承关系 | 核心功能 | 主要用途 |
|---------|---------|---------|---------|
| ood.UI.MoudluePlaceHolder | ood.UI.Div | 模块占位符 | 动态加载模块 |

## 3. 组件查找规则

1. **基础组件查找**：
   - 基础组件（Button, Icon, Div, Span等）都在 `ood.js` 文件中定义
   - 查找顺序：先在 `ood.js` 中查找，再在 `js/ui/` 文件夹下查找

2. **高级组件查找**：
   - 高级组件（MenuBar, Gallery, TitleBlock等）在 `js/ui/` 文件夹下的对应文件中定义
   - 例如：`MenuBar` 在 `js/ui/MenuBar.js` 中定义

3. **组件命名规则**：
   - 组件类名格式：`ood.UI.ComponentName`
   - 文件名格式：`ComponentName.js`（首字母大写）

## 4. 核心组件属性与方法

### 4.1 通用属性（所有组件继承）

| 属性名 | 类型 | 说明 | 示例 |
|-------|------|------|------|
| position | 字符串 | 定位方式 | `.setPosition("absolute")` |
| left | 字符串/数值 | 左侧偏移量 | `.setLeft("10px")` |
| top | 字符串/数值 | 顶部偏移量 | `.setTop("20px")` |
| right | 字符串/数值 | 右侧偏移量 | `.setRight("10px")` |
| width | 字符串/数值 | 组件宽度 | `.setWidth("100px")` |
| height | 字符串/数值 | 组件高度 | `.setHeight("50px")` |
| zIndex | 数值 | 堆叠顺序 | `.setZIndex(10)` |
| visibility | 字符串 | 可见性 | `.setVisibility("visible")` |
| display | 字符串 | 显示方式 | `.setDisplay("block")` |
| tabindex | 数值 | 键盘导航顺序 | `.setTabindex(1)` |

### 4.2 通用方法（所有组件继承）

| 方法名 | 说明 | 示例 |
|-------|------|------|
| setHost(host, alias) | 设置宿主和别名 | `.setHost(host, "myComponent")` |
| setName(name) | 设置组件名称 | `.setName("myComponent")` |
| setCustomStyle(key, value) | 设置自定义样式 | `.setCustomStyle({"KEY": "color:red;"})` |
| setCustomClass(key, value) | 设置自定义类 | `.setCustomClass("KEY", "my-class")` |
| setCustomAttr(key, value) | 设置自定义属性 | `.setCustomAttr("KEY", {"data-id": "123"})` |
| getRoot() | 获取根节点 | `component.getRoot()` |
| getRootNode() | 获取根DOM节点 | `component.getRootNode()` |
| getSubNode(key, subId) | 获取子节点 | `component.getSubNode("CAPTION")` |
| getSubNodes(key, subId) | 获取多个子节点 | `component.getSubNodes("ITEM")` |
| append(component) | 添加子组件 | `parent.append(child)` |
| removeChildren(subId, destroy, purgeNow) | 移除子组件 | `component.removeChildren()` |
| adjustSize() | 调整大小 | `component.adjustSize()` |
| destroy(ignoreEffects, purgeNow) | 销毁组件 | `component.destroy()` |

### 4.3 按钮组件（ood.UI.Button）

**核心属性**：
- caption: 按钮文本
- imageClass: 图标类名
- type: 按钮类型（normal, status, drop）
- value: 按钮值
- fontColor: 字体颜色
- fontSize: 字体大小
- fontWeight: 字体粗细
- fontFamily: 字体

**核心方法**：
- setCaption(caption) | 设置按钮文本
- setImageClass(className) | 设置图标类
- setType(type) | 设置按钮类型
- setValue(value) | 设置按钮值
- activate() | 激活按钮（获取焦点）

**使用示例**：
```javascript
ood.create("ood.UI.Button")
    .setHost(host, "myButton")
    .setName("myButton")
    .setCaption("点击我")
    .setImageClass("ood-icon-check")
    .setWidth("100px")
    .setHeight("40px")
    .onClick("myButton_onclick")
```

### 4.4 容器组件（ood.UI.Div）

**核心属性**：
- html: HTML内容
- overflow: 溢出处理
- iframeAutoLoad: 自动加载iframe
- ajaxAutoLoad: 自动加载AJAX内容

**核心方法**：
- setHtml(html) | 设置HTML内容
- setOverflow(overflow) | 设置溢出处理
- fireClickEvent() | 触发点击事件

**使用示例**：
```javascript
ood.create("ood.UI.Div")
    .setHost(host, "myDiv")
    .setName("myDiv")
    .setPosition("relative")
    .setLeft("0")
    .setTop("0")
    .setWidth("100%")
    .setHeight("200px")
    .setHtml("<p>这是一个Div容器</p>")
    .setOverflow("auto")
```

### 4.5 图标组件（ood.UI.Icon）

**核心属性**：
- imageClass: 图标类名
- iconFontCode: 图标字体代码
- iconFontSize: 图标字体大小
- imageDisplay: 图标显示方式

**核心方法**：
- setImageClass(className) | 设置图标类
- setIconFontCode(code) | 设置图标字体代码
- setIconFontSize(size) | 设置图标字体大小

## 5. 组件创建与使用流程

### 5.1 组件创建

**使用 ood.create() 方法**：
```javascript
var component = ood.create("ood.UI.ComponentName", properties, events);
```

**示例**：
```javascript
var button = ood.create("ood.UI.Button", {
    caption: "提交",
    width: "100px",
    height: "40px"
}, {
    onClick: "onButtonClick"
});
```

### 5.2 组件添加到容器

**使用 append() 方法**：
```javascript
parentComponent.append(component);
```

**示例**：
```javascript
host.pageContainer.append(button);
```

### 5.3 组件事件绑定

**方法一：创建时绑定**：
```javascript
var button = ood.create("ood.UI.Button", {
    caption: "点击我"
}, {
    onClick: "button_onclick"
});
```

**方法二：创建后绑定**：
```javascript
button.onClick("button_onclick");
```

## 6. 组件定位规则

### 6.1 定位属性

| 属性名 | 说明 | 取值范围 | 示例 |
|-------|------|----------|------|
| position | 定位模式 | static, relative, absolute | `.setPosition("absolute")` |
| dock | 停靠方式 | none, top, bottom, left, right, center, fill | `.setDock("top")` |
| left | 左侧偏移 | 带单位字符串或数值 | `.setLeft("10px")` |
| top | 顶部偏移 | 带单位字符串或数值 | `.setTop("20px")` |
| right | 右侧偏移 | 带单位字符串或数值 | `.setRight("10px")` |
| bottom | 底部偏移 | 带单位字符串或数值 | `.setBottom("10px")` |
| width | 宽度 | 带单位字符串或数值 | `.setWidth("100px")` |
| height | 高度 | 带单位字符串或数值 | `.setHeight("50px")` |
| zIndex | 堆叠顺序 | 整数 | `.setZIndex(10)` |

### 6.2 定位模式

1. **static（静态定位）**：
   - 默认定位方式
   - 元素按照文档流顺序排列
   - 不支持 top, left, right, bottom 属性

2. **relative（相对定位）**：
   - 相对于元素正常位置进行偏移
   - 保留元素在文档流中的位置
   - 支持 top, left, right, bottom 属性

3. **absolute（绝对定位）**：
   - 相对于最近的定位祖先元素进行定位
   - 脱离文档流
   - 支持 top, left, right, bottom 属性
   - 常用 dock 属性进行快速定位

### 6.3 定位示例

**顶部导航栏**：
```javascript
ood.create("ood.UI.Block")
    .setHost(host, "topBar")
    .setName("topBar")
    .setDock("top")
    .setHeight("5em")
    .setZIndex(10)
    .setVisibility("visible")
    .setBorderType("none")
    .setOverflow("hidden")
    .setPanelBgClr("#3498DB")
```

**绝对定位元素**：
```javascript
ood.create("ood.UI.Icon")
    .setHost(host, "userIcon")
    .setName("userIcon")
    .setLeft("10.6em")
    .setTop("0.75em")
    .setWidth("3em")
    .setHeight("2.2em")
    .setVisibility("visible")
    .setImageClass("spafont spa-icon-login")
    .setIconFontSize("2em")
```

## 7. 组件样式设置

### 7.1 使用 setCustomStyle

**设置单个样式**：
```javascript
component.setCustomStyle("KEY", "color:red;");
```

**设置多个样式**：
```javascript
component.setCustomStyle({
    "KEY": "color:red; font-size:16px;",
    "CAPTION": "font-weight:bold;"
});
```

### 7.2 使用虚拟节点

**组件模板中的虚拟节点**：
- KEY: 组件根节点
- CAPTION: 标题/文本节点
- ICON: 图标节点
- BORDER: 边框节点
- PANEL: 面板节点

**示例**：
```javascript
component.setCustomStyle({
    "KEY": "background-color:#f0f0f0;",
    "CAPTION": "color:#333;",
    "ICON": "font-size:20px;"
});
```

## 8. 组件最佳实践

1. **组件选择**：
   - 根据功能选择合适的组件
   - 优先使用基础组件组合实现复杂功能
   - 避免过度嵌套组件

2. **属性设置**：
   - 只设置组件支持的属性
   - 检查组件的 DataModel 定义
   - 对于只读属性，使用 setCustomStyle 等方式设置
   - set* 方法必须与组件属性匹配

3. **事件处理**：
   - 事件处理函数必须存在
   - 避免在事件处理中执行耗时操作
   - 使用事件委托优化性能

4. **性能优化**：
   - 减少不必要的组件创建和销毁
   - 合理使用缓存
   - 避免频繁修改定位属性
   - 对于静态内容，使用静态定位

5. **代码规范**：
   - 组件命名规范，使用有意义的名称
   - 遵循 iniComponents 方法规范
   - 使用 ood.create() 替代 new 关键字
   - 使用 host.pageContainer.append() 添加组件

## 9. 组件查找与调试

### 9.1 查找组件定义

1. **基础组件**：查看 `ood.js` 文件
2. **高级组件**：查看 `js/ui/` 文件夹下对应的文件
3. **组件属性**：查看组件的 DataModel 定义
4. **组件方法**：查看组件的 Instance 定义

### 9.2 调试技巧

1. **查看组件结构**：
   ```javascript
   console.log(component.getRoot()); // 查看根节点
   console.log(component.get(0)); // 查看组件配置文件
   ```

2. **检查组件属性**：
   ```javascript
   console.log(component.get(0).properties); // 查看组件属性
   ```

3. **检查组件方法**：
   ```javascript
   console.log(Object.keys(component)); // 查看组件方法
   ```

4. **查看组件DOM结构**：
   ```javascript
   console.log(component.getRootNode()); // 查看DOM节点
   ```

## 10. 总结

OODER 组件体系基于 OOD JS 框架构建，提供了丰富的组件类型和灵活的使用方式。通过理解组件的继承关系、核心属性和方法，开发者可以高效地构建复杂的 Web 应用界面。

**核心要点**：
1. 组件继承自 `ood.UI` 基础类
2. 组件分为基础组件、容器组件、交互组件和特殊组件
3. 使用 `ood.create()` 创建组件
4. 使用 `append()` 方法添加子组件
5. 组件定位通过 position、dock、top、left 等属性控制
6. 样式设置通过 `setCustomStyle` 方法和虚拟节点实现
7. 事件绑定通过事件处理器实现

通过遵循组件体系的规则和最佳实践，可以编写出结构清晰、性能优异、易于维护的 OODER 应用代码。