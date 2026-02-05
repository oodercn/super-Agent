# Avatar 头像组件

## 组件概述
Avatar 是一个移动端头像组件，用于显示用户头像、状态或图标。它支持多种类型的头像（图片头像、文字头像、图标头像），并可自定义尺寸、形状和在线状态。Avatar 组件继承自 `ood.UI`，实现了四分离设计模式，提供了灵活的配置选项和流畅的交互体验。

## 组件结构

Avatar 组件由以下部分组成：

- **KEY**：头像的外层容器
- **CONTAINER**：头像的内部容器，包含：
  - **IMAGE**：图片头像元素
  - **ICON**：图标头像元素
  - **TEXT**：文字头像元素
- **STATUS**：在线状态指示器

## 创建方式

### 1. JSON 配置方式

```json
{
  "type": "Avatar",
  "src": "https://example.com/avatar.jpg",
  "alt": "用户头像",
  "size": "md",
  "shape": "circle",
  "online": true
}
```

### 2. JavaScript 动态创建

```javascript
var avatarComponent = ood.create({
  type: "Avatar",
  text: "张三",
  size: "lg",
  shape: "square",
  online: true,
  onAvatarClick: function(profile, event) {
    console.log("头像被点击");
  }
});
```

## 属性说明

| 属性名 | 类型 | 说明 | 默认值 | 可选值 |
|--------|------|------|--------|--------|
| **caption** | String | 头像标题 | "头像" | 任意字符串 |
| **width** | String | 头像宽度 | "40px" | 带单位的尺寸值 |
| **height** | String | 头像高度 | "40px" | 带单位的尺寸值 |
| **backgroundColor** | String | 背景颜色 | 主题色 | 颜色值（如 #f0f0f0） |
| **textColor** | String | 文字颜色 | 主题文字色 | 颜色值（如 #333333） |
| **theme** | String | 主题模式 | "light" | "light", "dark", "light-hc", "dark-hc" |
| **responsive** | Boolean | 是否启用响应式设计 | true | true, false |
| **src** | String | 头像图片地址 | "" | 图片URL |
| **text** | String | 文字头像内容 | "" | 任意字符串 |
| **icon** | String | 图标头像类名 | "" | 图标类名 |
| **alt** | String | 图片替代文字 | "avatar" | 任意字符串 |
| **size** | String | 头像尺寸 | "md" | "xs", "sm", "md", "lg", "xl" |
| **shape** | String | 头像形状 | "circle" | "circle", "square" |
| **online** | Boolean | 在线状态 | null | true, false, null |

## 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| **initAvatarFeatures()** | 初始化头像特性 | 无 | 无 |
| **bindTouchEvents()** | 绑定触摸事件 | 无 | 无 |
| **updateAvatarDisplay()** | 更新头像显示 | 无 | 无 |
| **showImage()** | 显示图片头像 | 无 | 无 |
| **showIcon()** | 显示图标头像 | 无 | 无 |
| **showText()** | 显示文字头像 | 无 | 无 |
| **showDefault()** | 显示默认头像 | 无 | 无 |
| **getDisplayText(text)** | 获取显示文本 | text: String - 原始文本 | String - 处理后的显示文本 |
| **updateOnlineStatus()** | 更新在线状态 | 无 | 无 |
| **updateAvatarSize()** | 更新头像尺寸 | 无 | 无 |
| **updateAvatarShape()** | 更新头像形状 | 无 | 无 |
| **setSrc(src)** | 设置图片地址 | src: String - 图片URL | 无 |
| **setText(text)** | 设置文字内容 | text: String - 文字内容 | 无 |
| **setIcon(icon)** | 设置图标类名 | icon: String - 图标类名 | 无 |
| **setOnline(online)** | 设置在线状态 | online: Boolean - 在线状态 | 无 |
| **setSize(size)** | 设置头像尺寸 | size: String - 尺寸 | 无 |
| **setShape(shape)** | 设置头像形状 | shape: String - 形状 | 无 |
| **onImageError(e)** | 图片加载错误处理 | e: Event - 错误事件 | 无 |
| **onImageLoad(e)** | 图片加载完成处理 | e: Event - 加载事件 | 无 |
| **onAvatarClick(e)** | 头像点击事件处理 | e: Event - 点击事件 | 无 |

## 事件处理

### onAvatarClick

当头像被点击时触发。

```javascript
var avatarComponent = ood.create({
  type: "Avatar",
  src: "https://example.com/avatar.jpg",
  onAvatarClick: function(profile, event) {
    console.log("头像被点击");
    // 执行相关业务逻辑，如打开用户资料
  }
});
```

### onImageError

当图片加载失败时触发。

```javascript
var avatarComponent = ood.create({
  type: "Avatar",
  src: "https://example.com/invalid.jpg",
  onImageError: function(profile, event) {
    console.log("图片加载失败");
    // 执行相关处理，如显示默认头像
  }
});
```

### onImageLoad

当图片加载完成时触发。

```javascript
var avatarComponent = ood.create({
  type: "Avatar",
  src: "https://example.com/avatar.jpg",
  onImageLoad: function(profile, event) {
    console.log("图片加载完成");
    // 执行相关处理，如显示加载动画
  }
});
```

## 主题支持

Avatar 组件支持多种主题模式，包括：

- **light**：浅色主题
- **dark**：深色主题
- **light-hc**：浅色高对比度主题
- **dark-hc**：深色高对比度主题

可以通过 `theme` 属性设置主题：

```json
{
  "type": "Avatar",
  "text": "李四",
  "theme": "dark",
  "size": "md"
}
```

## 响应式设计

Avatar 组件支持响应式设计，通过 `responsive` 属性可以控制其响应式行为。当启用响应式设计时，组件会根据屏幕尺寸自动调整布局。

组件实现了 `_onresize` 方法，用于处理窗口大小变化时的布局调整，确保头像始终显示在正确的位置和大小。

## 无障碍支持

Avatar 组件实现了基本的无障碍支持，包括：

- 使用 `aria-label` 属性提供可访问名称
- 为图片头像提供 `alt` 属性
- 支持键盘导航和焦点管理
- 确保在线状态指示器可访问

## 应用场景

### 1. 用户头像

用于显示用户头像，支持图片、文字和图标三种类型。

```json
{
  "type": "Avatar",
  "src": "https://example.com/user.jpg",
  "alt": "用户头像",
  "size": "lg"
}
```

### 2. 联系人列表

在联系人列表中显示联系人头像和在线状态。

```json
{
  "type": "Avatar",
  "text": "王五",
  "size": "sm",
  "online": true
}
```

### 3. 群聊成员

在群聊中显示成员头像，支持不同尺寸和形状。

```json
{
  "type": "Avatar",
  "text": "赵六",
  "size": "md",
  "shape": "square"
}
```

### 4. 状态指示

用于显示用户的在线状态，如在线、离线等。

```json
{
  "type": "Avatar",
  "icon": "user-icon",
  "size": "xs",
  "online": false
}
```

## 代码示例

### 示例 1：图片头像

```json
{
  "type": "Avatar",
  "src": "https://example.com/avatar.jpg",
  "alt": "用户头像",
  "size": "md"
}
```

### 示例 2：文字头像

```json
{
  "type": "Avatar",
  "text": "张三",
  "size": "lg",
  "shape": "square"
}
```

### 示例 3：图标头像

```json
{
  "type": "Avatar",
  "icon": "user-icon",
  "size": "md"
}
```

### 示例 4：不同尺寸

```json
[
  {
    "type": "Avatar",
    "text": "XS",
    "size": "xs"
  },
  {
    "type": "Avatar",
    "text": "SM",
    "size": "sm"
  },
  {
    "type": "Avatar",
    "text": "MD",
    "size": "md"
  },
  {
    "type": "Avatar",
    "text": "LG",
    "size": "lg"
  },
  {
    "type": "Avatar",
    "text": "XL",
    "size": "xl"
  }
]
```

### 示例 5：在线状态

```json
[
  {
    "type": "Avatar",
    "text": "在线",
    "size": "md",
    "online": true
  },
  {
    "type": "Avatar",
    "text": "离线",
    "size": "md",
    "online": false
  },
  {
    "type": "Avatar",
    "text": "无状态",
    "size": "md"
  }
]
```

### 示例 6：动态更新头像

```javascript
// 创建头像组件
var avatar = ood.create({
  type: "Avatar",
  text: "初始用户",
  size: "md"
});

// 动态更新为图片头像
setTimeout(function() {
  avatar.set("src", "https://example.com/avatar.jpg");
  avatar.set("alt", "更新后的头像");
}, 2000);
```

## 总结

Avatar 组件是一个功能丰富、配置灵活的移动端头像组件，具有以下特点：

- 支持多种类型的头像：图片头像、文字头像、图标头像
- 提供多种尺寸：xs、sm、md、lg、xl
- 支持两种形状：圆形、方形
- 支持在线状态显示
- 内置图片加载错误处理
- 支持点击事件
- 支持主题切换
- 支持响应式设计
- 基本的无障碍支持

Avatar 组件适用于各种需要显示用户头像或状态的场景，如联系人列表、聊天界面、用户资料等，能够为用户提供直观的视觉反馈和良好的交互体验。