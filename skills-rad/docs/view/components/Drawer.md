# Drawer 抽屉组件

## 组件概述
Drawer 是一个移动端抽屉组件，用于在屏幕边缘滑出的侧边面板，提供额外的导航或内容展示空间。它支持左右滑出、遮罩层、手势控制等功能，继承自 `ood.UI.Widget` 和 `ood.absList`，实现了四分离设计模式，提供了灵活的配置选项和流畅的交互体验。

## 组件结构

Drawer 组件由以下部分组成：

- **KEY**：抽屉的外层容器
- **OVERLAY**：抽屉的遮罩层，点击可关闭抽屉
- **DRAWER**：抽屉面板，包含：
  - **HEADER**：抽屉头部，包含：
    - **TITLE**：抽屉标题
    - **CLOSE**：关闭按钮
  - **CONTENT**：抽屉内容区域

## 创建方式

### 1. JSON 配置方式

```json
{
  "type": "Drawer",
  "caption": "侧边导航",
  "title": "导航菜单",
  "placement": "left",
  "width": "280px",
  "swipeToOpen": true,
  "maskClosable": true
}
```

### 2. JavaScript 动态创建

```javascript
var drawerComponent = ood.create({
  type: "Drawer",
  caption: "侧边导航",
  title: "导航菜单",
  placement: "left",
  width: "280px",
  swipeToOpen: true,
  maskClosable: true,
  onOpen: function(profile) {
    console.log("抽屉打开");
  },
  onClose: function(profile) {
    console.log("抽屉关闭");
  }
});
```

## 属性说明

| 属性名 | 类型 | 说明 | 默认值 | 可选值 |
|--------|------|------|--------|--------|
| **caption** | String | 抽屉标题 | "抽屉" | 任意字符串 |
| **width** | String | 抽屉宽度 | "280px" | 带单位的尺寸值 |
| **height** | String | 抽屉高度 | "100%" | 带单位的尺寸值 |
| **backgroundColor** | String | 背景颜色 | 主题色 | 颜色值（如 #ffffff） |
| **titleColor** | String | 标题颜色 | 主题文字色 | 颜色值（如 #333333） |
| **placement** | String | 抽屉位置 | "left" | "left", "right" |
| **title** | String | 抽屉标题 | "" | 任意字符串 |
| **swipeToOpen** | Boolean | 是否支持滑动打开 | true | true, false |
| **maskClosable** | Boolean | 点击遮罩是否可关闭 | true | true, false |

## 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| **initDrawerFeatures()** | 初始化抽屉特性 | 无 | 无 |
| **bindTouchEvents()** | 绑定触摸事件 | 无 | 无 |
| **initDrawerState()** | 初始化抽屉状态 | 无 | 无 |
| **initSwipeGesture()** | 初始化滑动手势 | 无 | 无 |
| **open()** | 打开抽屉 | 无 | 无 |
| **close()** | 关闭抽屉 | 无 | 无 |
| **toggle()** | 切换抽屉状态 | 无 | 无 |
| **isOpen()** | 检查抽屉是否打开 | 无 | Boolean - 是否打开 |
| **setContent(content)** | 设置抽屉内容 | content: String/Element - 内容 | 无 |
| **onOpen()** | 抽屉打开事件处理 | 无 | 无 |
| **onClose()** | 抽屉关闭事件处理 | 无 | 无 |
| **getCaption()** | 获取抽屉标题 | 无 | String - 抽屉标题 |
| **setCaption(value)** | 设置抽屉标题 | value: String - 抽屉标题 | 组件实例 |
| **getWidth()** | 获取抽屉宽度 | 无 | String - 抽屉宽度 |
| **setWidth(value)** | 设置抽屉宽度 | value: String - 抽屉宽度 | 组件实例 |
| **getHeight()** | 获取抽屉高度 | 无 | String - 抽屉高度 |
| **setHeight(value)** | 设置抽屉高度 | value: String - 抽屉高度 | 组件实例 |
| **getBackgroundColor()** | 获取背景颜色 | 无 | String - 背景颜色 |
| **setBackgroundColor(value)** | 设置背景颜色 | value: String - 背景颜色 | 组件实例 |
| **getTitleColor()** | 获取标题颜色 | 无 | String - 标题颜色 |
| **setTitleColor(value)** | 设置标题颜色 | value: String - 标题颜色 | 组件实例 |
| **getPlacement()** | 获取抽屉位置 | 无 | String - 抽屉位置 |
| **setPlacement(value)** | 设置抽屉位置 | value: String - 抽屉位置 | 组件实例 |
| **getTitle()** | 获取抽屉标题 | 无 | String - 抽屉标题 |
| **setTitle(value)** | 设置抽屉标题 | value: String - 抽屉标题 | 组件实例 |
| **getSwipeToOpen()** | 获取是否支持滑动打开 | 无 | Boolean - 是否支持滑动打开 |
| **setSwipeToOpen(value)** | 设置是否支持滑动打开 | value: Boolean - 是否支持滑动打开 | 组件实例 |
| **getMaskClosable()** | 获取点击遮罩是否可关闭 | 无 | Boolean - 点击遮罩是否可关闭 |
| **setMaskClosable(value)** | 设置点击遮罩是否可关闭 | value: Boolean - 点击遮罩是否可关闭 | 组件实例 |

## 事件处理

### onOpen

当抽屉打开时触发。

```javascript
var drawerComponent = ood.create({
  type: "Drawer",
  onOpen: function(profile) {
    console.log("抽屉打开");
    // 执行相关业务逻辑
  }
});
```

### onClose

当抽屉关闭时触发。

```javascript
var drawerComponent = ood.create({
  type: "Drawer",
  onClose: function(profile) {
    console.log("抽屉关闭");
    // 执行相关业务逻辑
  }
});
```

## 主题支持

Drawer 组件支持主题切换，会自动继承系统主题。可以通过 CSS 变量自定义主题样式，主要涉及以下变量：

- `--mobile-bg-primary`：背景颜色
- `--mobile-border-color`：边框颜色
- `--mobile-text-primary`：文字颜色
- `--mobile-text-secondary`：次要文字颜色
- `--mobile-spacing-md`：间距
- `--mobile-shadow-heavy`：阴影效果

## 响应式设计

Drawer 组件是为移动端设计的，具有响应式特性：

- 自适应屏幕高度，默认占满整个屏幕高度
- 在小屏幕设备上自动调整宽度，最大宽度为屏幕宽度的 80%
- 最小宽度为 250px，确保在各种设备上都有良好的显示效果
- 支持滑动手势，在屏幕边缘滑动可打开抽屉

## 无障碍支持

Drawer 组件实现了基本的无障碍支持，包括：

- 使用 `aria-label` 属性提供可访问名称
- 支持键盘导航和焦点管理
- 点击遮罩可关闭抽屉，提供直观的关闭方式
- 关闭按钮带有清晰的视觉提示

## 应用场景

### 1. 侧边导航菜单

用于移动端应用的侧边导航菜单，提供主要导航选项。

```json
{
  "type": "Drawer",
  "title": "导航菜单",
  "placement": "left",
  "width": "280px",
  "content": "<ul><li>首页</li><li>分类</li><li>购物车</li><li>个人中心</li></ul>"
}
```

### 2. 右侧筛选面板

用于商品列表或搜索结果的筛选面板，从右侧滑出。

```json
{
  "type": "Drawer",
  "title": "筛选条件",
  "placement": "right",
  "width": "280px",
  "content": "<div>筛选条件内容</div>"
}
```

### 3. 个人中心菜单

用于显示用户个人信息和相关操作选项。

```json
{
  "type": "Drawer",
  "title": "个人中心",
  "placement": "left",
  "width": "280px",
  "content": "<div>个人信息和操作选项</div>"
}
```

## 代码示例

### 示例 1：左侧抽屉

```json
{
  "type": "Drawer",
  "title": "左侧抽屉",
  "placement": "left",
  "width": "280px"
}
```

### 示例 2：右侧抽屉

```json
{
  "type": "Drawer",
  "title": "右侧抽屉",
  "placement": "right",
  "width": "280px"
}
```

### 示例 3：不带标题

```json
{
  "type": "Drawer",
  "placement": "left",
  "width": "280px",
  "content": "<div>不带标题的抽屉内容</div>"
}
```

### 示例 4：禁用滑动打开

```json
{
  "type": "Drawer",
  "title": "禁用滑动",
  "placement": "left",
  "width": "280px",
  "swipeToOpen": false
}
```

### 示例 5：动态控制抽屉

```javascript
// 创建抽屉组件
var drawerComponent = ood.create({
  type: "Drawer",
  title: "动态控制",
  placement: "left",
  width: "280px"
});

// 打开抽屉
drawerComponent.open();

// 关闭抽屉
drawerComponent.close();

// 切换抽屉状态
drawerComponent.toggle();

// 检查抽屉是否打开
var isOpen = drawerComponent.isOpen();
console.log("抽屉是否打开：", isOpen);

// 设置抽屉内容
drawerComponent.setContent("<div>动态更新的内容</div>");
```

## 总结

Drawer 组件是一个功能丰富、配置灵活的移动端抽屉组件，具有以下特点：

- 支持左右滑出方向
- 支持遮罩层和点击关闭
- 支持手势滑动打开
- 流畅的动画过渡效果
- 可自定义宽度和高度
- 支持标题和关闭按钮
- 完整的事件处理机制
- 响应式设计，适配不同屏幕尺寸
- 基本的无障碍支持

Drawer 组件适用于各种需要侧边面板的场景，如导航菜单、筛选面板、个人中心等，能够为移动端应用提供良好的用户体验和交互方式。