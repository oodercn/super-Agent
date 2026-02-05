# Dropdown 下拉菜单组件

## 组件概述
Dropdown（下拉菜单）组件用于从一个触发元素弹出的菜单列表，提供多个选项供用户选择。它支持多种菜单项类型（按钮、分隔线、复选框、单选框）、子菜单、主题切换和键盘导航。Dropdown 组件在 ooder-a2ui 中通过 `ood.UI.PopMenu` 实现，继承自 `ood.UI.Widget` 和 `ood.absList`，提供了灵活的配置选项和流畅的交互体验。

## 组件结构

Dropdown 组件由以下部分组成：

- **KEY**：下拉菜单的外层容器
- **BORDER**：菜单边框
- **BOX**：菜单内容容器，包含：
  - **BOXBGBAR**：背景条
  - **ITEMS**：菜单项容器，包含多个 **ITEM**（菜单项）
    - **ICON**：菜单项图标
    - **CAPTION**：菜单项文本
    - **CHECKBOX/RADIOBOX**：复选框/单选框类型的菜单项
    - **ADD**：附加信息
    - **SUB**：子菜单指示器
  - **TOP**：滚动到顶部按钮
  - **BOTTOM**：滚动到底部按钮
- **POOL**：菜单项池，用于动态生成菜单项

## 创建方式

### 1. JSON 配置方式

```json
{
  "type": "PopMenu",
  "caption": "下拉菜单",
  "items": [
    {"id": "item1", "caption": "菜单项1", "imageClass": "ri-home-line"},
    {"id": "item2", "caption": "菜单项2", "imageClass": "ri-settings-line"},
    {"id": "split", "type": "split"},
    {"id": "item3", "caption": "菜单项3", "imageClass": "ri-logout-box-line", "disabled": false}
  ],
  "theme": "light",
  "responsive": true
}
```

### 2. JavaScript 动态创建

```javascript
var dropdownComponent = ood.create({
  type: "PopMenu",
  caption: "下拉菜单",
  items: [
    {id: "item1", caption: "菜单项1", imageClass: "ri-home-line"},
    {id: "item2", caption: "菜单项2", imageClass: "ri-settings-line"},
    {id: "split", type: "split"},
    {id: "item3", caption: "菜单项3", imageClass: "ri-logout-box-line", disabled: false}
  ],
  theme: "light",
  responsive: true,
  onMenuSelected: function(profile, item, src) {
    console.log("选中菜单项：", item.id);
    // 执行相关业务逻辑
  }
});

// 弹出菜单
dropdownComponent.pop(element, "below-right");
```

## 属性说明

| 属性名 | 类型 | 说明 | 默认值 | 可选值 |
|--------|------|------|--------|--------|
| **theme** | String | 主题模式 | "light" | "light", "dark" |
| **responsive** | Boolean | 是否启用响应式设计 | true | true, false |
| **autoHide** | Boolean | 是否自动隐藏 | false | true, false |
| **hideAfterClick** | Boolean | 点击后是否隐藏 | true | true, false |
| **showEffects** | String | 显示效果 | "Blur" | 各种效果名称 |
| **hideEffects** | String | 隐藏效果 | "" | 各种效果名称 |
| **height** | String | 菜单高度 | "auto" | 带单位的尺寸值 |
| **width** | String | 菜单宽度 | "auto" | 带单位的尺寸值 |
| **noIcon** | Boolean | 是否不显示图标 | false | true, false |
| **items** | Array | 菜单项列表 | 示例数据 | 菜单项对象数组 |
| **parentID** | String | 父容器ID | ood.ini.$rootContainer | 容器ID |

## 菜单项属性

| 属性名 | 类型 | 说明 | 默认值 | 可选值 |
|--------|------|------|--------|--------|
| **id** | String | 菜单项ID | 无 | 唯一字符串 |
| **caption** | String | 菜单项文本 | 无 | 任意字符串 |
| **type** | String | 菜单项类型 | "button" | "button", "split", "checkbox", "radiobox" |
| **imageClass** | String | 图标类名 | "oodcon ood-icon-placeholder" | 图标类名 |
| **disabled** | Boolean | 是否禁用 | false | true, false |
| **value** | Boolean | 复选框/单选框值 | false | true, false |
| **sub** | Array | 子菜单 | 无 | 菜单项数组 |

## 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| **adjustSize()** | 调整菜单尺寸 | 无 | 组件实例 |
| **_setScroll()** | 设置滚动条显示状态 | 无 | 组件实例 |
| **_scrollToBottom()** | 滚动到底部 | 无 | 无 |
| **_scrollToTop()** | 滚动到顶部 | 无 | 无 |
| **_initGrp()** | 初始化菜单组 | 无 | 无 |
| **setTagVar(tagVar)** | 设置标签变量 | tagVar: Object - 标签变量 | 无 |
| **pop(pos, type, parent, ignoreEffects)** | 弹出菜单 | pos: Object - 位置<br>type: String - 弹出类型<br>parent: Object - 父容器<br>ignoreEffects: Boolean - 是否忽略效果 | 组件实例 |
| **hide(triggerEvent, ignoreEffects, e)** | 隐藏菜单 | triggerEvent: Boolean - 是否触发事件<br>ignoreEffects: Boolean - 是否忽略效果<br>e: Event - 事件对象 | 组件实例 |
| **setTheme(theme)** | 设置主题 | theme: String - 主题模式 | 组件实例 |
| **getTheme()** | 获取当前主题 | 无 | String - 当前主题模式 |
| **toggleDarkMode()** | 切换暗黑模式 | 无 | 组件实例 |
| **adjustLayout()** | 调整布局以适应不同屏幕尺寸 | 无 | 组件实例 |
| **enhanceAccessibility()** | 增强无障碍支持 | 无 | 组件实例 |
| **PopMenuTrigger()** | 初始化现代化功能 | 无 | 无 |

## 事件处理

### onMenuSelected

当选择菜单项时触发，返回选中的菜单项信息。

```javascript
var dropdownComponent = ood.create({
  type: "PopMenu",
  items: [...],
  onMenuSelected: function(profile, item, src) {
    console.log("选中菜单项：", item.id, item.caption);
    // 执行相关业务逻辑
  }
});
```

### onShowSubMenu

当显示子菜单时触发。

```javascript
var dropdownComponent = ood.create({
  type: "PopMenu",
  items: [...],
  onShowSubMenu: function(profile, item, src) {
    console.log("显示子菜单：", item.id);
    // 可以返回动态生成的子菜单项
  }
});
```

### onHide

当菜单隐藏时触发。

```javascript
var dropdownComponent = ood.create({
  type: "PopMenu",
  items: [...],
  onHide: function(profile) {
    console.log("菜单隐藏");
    // 执行相关业务逻辑
  }
});
```

## 主题支持

Dropdown 组件支持多种主题模式，包括：

- **light**：浅色主题
- **dark**：深色主题

可以通过 `theme` 属性或 `setTheme()` 方法设置主题：

```javascript
// 通过属性设置
var dropdownComponent = ood.create({
  type: "PopMenu",
  theme: "dark"
});

// 通过方法设置
dropdownComponent.setTheme("light");
```

组件还支持主题的本地存储，会自动记住用户的主题偏好。

## 响应式设计

Dropdown 组件支持响应式设计，通过 `responsive` 属性可以控制其响应式行为。当启用响应式设计时，组件会根据屏幕尺寸自动调整布局：

- 屏幕宽度 < 768px：添加 `popmenu-mobile` 类，调整菜单项 padding 和字体大小
- 屏幕宽度 < 480px：添加 `popmenu-tiny` 类，进一步调整字体大小
- 屏幕宽度 < 320px：隐藏图标，节省空间

这些类可以用于自定义不同屏幕尺寸下的样式。

## 无障碍支持

Dropdown 组件实现了无障碍支持，包括：

- 使用 `role="menu"` 和 `aria-label="弹出菜单"` 标识为菜单控件
- 为每个菜单项添加 `role="menuitem"` 或 `role="menuitemcheckbox"`/`role="menuitemradio"` 属性
- 为菜单项添加 `tabindex` 属性，支持键盘导航
- 为禁用的菜单项设置 `aria-disabled="true"`
- 为子菜单设置 `aria-haspopup="true"` 和 `aria-expanded` 属性
- 支持键盘导航（上下箭头选择菜单项，回车键选择，左右箭头切换子菜单）

## 应用场景

### 1. 基本下拉菜单

用于提供多个选项供用户选择，如工具栏按钮的下拉菜单。

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "new", "caption": "新建", "imageClass": "ri-add-line"},
    {"id": "open", "caption": "打开", "imageClass": "ri-folder-open-line"},
    {"id": "save", "caption": "保存", "imageClass": "ri-save-line"},
    {"id": "split1", "type": "split"},
    {"id": "exit", "caption": "退出", "imageClass": "ri-logout-box-line"}
  ]
}
```

### 2. 带复选框的下拉菜单

用于提供多个可选择的选项，如设置菜单。

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "option1", "caption": "选项1", "type": "checkbox", "value": true, "imageClass": "ri-check-line"},
    {"id": "option2", "caption": "选项2", "type": "checkbox", "value": false, "imageClass": "ri-check-line"},
    {"id": "option3", "caption": "选项3", "type": "checkbox", "value": true, "imageClass": "ri-check-line"}
  ]
}
```

### 3. 带单选框的下拉菜单

用于从多个选项中选择一个，如视图切换菜单。

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "view1", "caption": "视图1", "type": "radiobox", "value": true, "imageClass": "ri-view-grid-line"},
    {"id": "view2", "caption": "视图2", "type": "radiobox", "value": false, "imageClass": "ri-list-unordered"},
    {"id": "view3", "caption": "视图3", "type": "radiobox", "value": false, "imageClass": "ri-column-width"}
  ]
}
```

### 4. 带子菜单的下拉菜单

用于组织多层级的菜单结构，如复杂应用的导航菜单。

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "file", "caption": "文件", "imageClass": "ri-file-line", "sub": [
      {"id": "new", "caption": "新建", "imageClass": "ri-add-line"},
      {"id": "open", "caption": "打开", "imageClass": "ri-folder-open-line"}
    ]},
    {"id": "edit", "caption": "编辑", "imageClass": "ri-edit-line", "sub": [
      {"id": "copy", "caption": "复制", "imageClass": "ri-file-copy-line"},
      {"id": "paste", "caption": "粘贴", "imageClass": "ri-clipboard-line"}
    ]}
  ]
}
```

## 代码示例

### 示例 1：基本下拉菜单

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "item1", "caption": "菜单项1", "imageClass": "ri-home-line"},
    {"id": "item2", "caption": "菜单项2", "imageClass": "ri-settings-line"},
    {"id": "split", "type": "split"},
    {"id": "item3", "caption": "菜单项3", "imageClass": "ri-logout-box-line"}
  ]
}
```

### 示例 2：带复选框的下拉菜单

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "option1", "caption": "选项1", "type": "checkbox", "value": true, "imageClass": "ri-check-line"},
    {"id": "option2", "caption": "选项2", "type": "checkbox", "value": false, "imageClass": "ri-check-line"}
  ]
}
```

### 示例 3：带单选框的下拉菜单

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "radio1", "caption": "选项1", "type": "radiobox", "value": true, "imageClass": "ri-circle-line"},
    {"id": "radio2", "caption": "选项2", "type": "radiobox", "value": false, "imageClass": "ri-circle-line"}
  ]
}
```

### 示例 4：带子菜单的下拉菜单

```json
{
  "type": "PopMenu",
  "items": [
    {"id": "parent", "caption": "父菜单项", "sub": [
      {"id": "child1", "caption": "子菜单项1"},
      {"id": "child2", "caption": "子菜单项2"}
    ]}
  ]
}
```

### 示例 5：动态弹出菜单

```javascript
// 创建菜单组件
var menu = ood.create({
  type: "PopMenu",
  items: [
    {"id": "item1", "caption": "菜单项1"},
    {"id": "item2", "caption": "菜单项2"}
  ]
});

// 绑定到按钮点击事件
var button = ood("#myButton");
button.on("click", function(e) {
  var pos = ood(this).offset();
  menu.pop(pos, "below-right");
});
```

## 总结

Dropdown 组件是一个功能丰富、配置灵活的下拉菜单组件，具有以下特点：

- 支持多种菜单项类型：按钮、分隔线、复选框、单选框
- 支持子菜单，可无限嵌套
- 支持主题切换（浅色/深色）
- 支持响应式设计，适配不同屏幕尺寸
- 支持键盘导航和无障碍访问
- 支持滚动条和滚动控制按钮
- 支持菜单项的动态生成和管理
- 提供完整的事件处理机制
- 流畅的动画效果和交互体验

Dropdown 组件适用于各种需要下拉菜单的场景，如工具栏按钮、上下文菜单、设置菜单等，能够为用户提供直观的菜单选择体验。