# Pagination 分页组件

## 组件概述
Pagination（分页）组件用于在多页数据之间进行导航，提供直观的页码控制和页面跳转功能。它支持多种显示模式，包括显示/隐藏更多页码按钮、自定义页码模板、主题切换和响应式设计。Pagination组件继承自 `ood.UI` 和 `ood.absValue`，实现了四分离设计模式，提供了灵活的配置选项和流畅的交互体验。

## 组件结构

Pagination 组件由以下部分组成：

- **LABEL**：分页标签，显示页码提示文本
- **FIRST**：第一页按钮
- **PREM**：前面更多页按钮（显示为 "..."）
- **PREV**：上一页按钮
- **CUR**：当前页码输入框
- **NEXT**：下一页按钮
- **NEXTM**：后面更多页按钮（显示为 "..."）
- **LAST**：最后一页按钮
- **POOL**：页码池，用于存储动态生成的页码按钮
- **POP**：页码弹出层，用于显示更多页码选项

## 创建方式

### 1. JSON 配置方式

```json
{
  "type": "Pagination",
  "value": "1:1:10",
  "caption": "页码: ",
  "showMoreBtns": true,
  "pageCount": 20,
  "theme": "light",
  "responsive": true
}
```

### 2. JavaScript 动态创建

```javascript
var paginationComponent = ood.create({
  type: "Pagination",
  value: "1:1:10",
  caption: "页码: ",
  showMoreBtns: true,
  pageCount: 20,
  theme: "light",
  responsive: true,
  onPageSet: function(profile, pageNum, startIndex, pageSize, eventType, oldPage, oldStart) {
    console.log("页码切换：", pageNum);
    // 执行相关业务逻辑，如加载新页面数据
  }
});
```

## 属性说明

| 属性名 | 类型 | 说明 | 默认值 | 可选值 |
|--------|------|------|--------|--------|
| **theme** | String | 主题模式 | "light" | "light", "dark", "high-contrast" |
| **responsive** | Boolean | 是否启用响应式设计 | true | true, false |
| **hiddenBar** | Boolean | 是否隐藏分页栏 | false | true, false |
| **caption** | String | 分页标签文本 | " Page: " | 任意字符串 |
| **showMoreBtns** | Boolean | 是否显示更多页码按钮（"..."） | true | true, false |
| **pageCount** | Number | 每页显示的记录数 | 20 | 任意正整数 |
| **disabled** | Boolean | 是否禁用分页 | false | true, false |
| **readonly** | Boolean | 是否只读 | false | true, false |
| **value** | String | 分页值，格式为 "min:current:max" | "1:1:1" | 格式正确的字符串 |
| **uriTpl** | String | 页码链接模板，"*" 表示页码 | "#*" | 包含 "*" 的字符串 |
| **textTpl** | String | 页码文本模板，"*" 表示页码 | "*" | 包含 "*" 的字符串 |
| **prevMark** | String | 上一页标记 | 页码减一 | 任意字符串或数字 |
| **nextMark** | String | 下一页标记 | 页码加一 | 任意字符串或数字 |
| **parentID** | String | 父容器ID | "" | 任意有效的DOM元素ID |

## 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| **_setCtrlValue(value)** | 设置分页值 | value: String - 格式为 "min:current:max" 的分页值 | 组件实例 |
| **setPage(value, force, type)** | 设置当前页码 | value: Number - 页码<br>force: Boolean - 是否强制设置<br>type: String - 事件类型 | 组件实例 |
| **getPage(total)** | 获取当前页码或总页数 | total: Boolean - 是否获取总页数 | Number - 当前页码或总页数 |
| **buindAPI(api)** | 绑定API对象 | api: Object - API对象 | 绑定的API对象 |
| **getTotalPages()** | 获取总页数 | 无 | Number - 总页数 |
| **setTotalCount(count)** | 设置总记录数，自动计算总页数 | count: Number - 总记录数 | 组件实例 |
| **setTheme(theme)** | 设置主题 | theme: String - 主题模式 | 组件实例 |
| **getTheme()** | 获取当前主题 | 无 | String - 当前主题模式 |
| **toggleTheme()** | 切换主题 | 无 | 组件实例 |
| **toggleDarkMode()** | 切换暗黑模式 | 无 | 组件实例 |
| **adjustLayout()** | 调整布局以适应不同屏幕尺寸 | 无 | 组件实例 |
| **enhanceAccessibility()** | 增强无障碍支持 | 无 | 组件实例 |
| **PageBarTrigger()** | 初始化现代化功能 | 无 | 无 |

## 事件处理

### onPageSet

当页码改变时触发，返回新的页码、起始索引、每页记录数、事件类型等信息。

```javascript
var paginationComponent = ood.create({
  type: "Pagination",
  value: "1:1:10",
  onPageSet: function(profile, pageNum, startIndex, pageSize, eventType, oldPage, oldStart) {
    console.log("页码切换：", pageNum);
    console.log("起始索引：", startIndex);
    console.log("每页记录数：", pageSize);
    console.log("事件类型：", eventType);
    // 执行相关业务逻辑，如加载新页面数据
  }
});
```

### onClick

当点击分页按钮时触发，返回点击的页码。

```javascript
var paginationComponent = ood.create({
  type: "Pagination",
  value: "1:1:10",
  onClick: function(profile, pageNum) {
    console.log("点击页码：", pageNum);
    // 执行相关业务逻辑
  }
});
```

## 主题支持

Pagination 组件支持多种主题模式，包括：

- **light**：浅色主题
- **dark**：深色主题
- **high-contrast**：高对比度主题

可以通过 `theme` 属性或 `setTheme()` 方法设置主题：

```javascript
// 通过属性设置
var paginationComponent = ood.create({
  type: "Pagination",
  theme: "dark"
});

// 通过方法设置
paginationComponent.setTheme("light");
```

组件还支持主题的本地存储，会自动记住用户的主题偏好。

## 响应式设计

Pagination 组件支持响应式设计，通过 `responsive` 属性可以控制其响应式行为。当启用响应式设计时，组件会根据屏幕尺寸自动调整布局：

- 屏幕宽度 < 768px：添加 `pagebar-mobile` 类，调整按钮大小和字体
- 屏幕宽度 < 480px：添加 `pagebar-tiny` 类，进一步简化显示

这些类可以用于自定义不同屏幕尺寸下的样式。

## 无障碍支持

Pagination 组件实现了无障碍支持，包括：

- 使用 `role="navigation"` 和 `aria-label="分页导航"` 标识为导航控件
- 为每个按钮添加 `role="button"` 和 `aria-label` 属性
- 为页码输入框添加 `role="spinbutton"` 和相关 ARIA 属性
- 支持键盘导航和焦点管理
- 为标签和输入框添加正确的关联

## 应用场景

### 1. 数据列表分页

用于数据列表的分页导航，允许用户在不同页面之间切换。

```json
{
  "type": "Pagination",
  "value": "1:1:20",
  "caption": "页码: ",
  "pageCount": 10,
  "onPageSet": "loadData"
}
```

### 2. 图片画廊分页

用于图片画廊的分页导航，允许用户浏览不同页面的图片。

```json
{
  "type": "Pagination",
  "value": "1:1:15",
  "caption": "页: ",
  "pageCount": 9,
  "theme": "dark",
  "onPageSet": "loadGalleryPage"
}
```

### 3. 搜索结果分页

用于搜索结果的分页导航，允许用户查看不同页面的搜索结果。

```json
{
  "type": "Pagination",
  "value": "1:1:100",
  "caption": "结果页码: ",
  "pageCount": 20,
  "showMoreBtns": true,
  "onPageSet": "loadSearchResults"
}
```

## 代码示例

### 示例 1：基本分页

```json
{
  "type": "Pagination",
  "value": "1:1:10",
  "caption": "页码: "
}
```

### 示例 2：隐藏更多按钮

```json
{
  "type": "Pagination",
  "value": "1:1:10",
  "caption": "页码: ",
  "showMoreBtns": false
}
```

### 示例 3：深色主题

```json
{
  "type": "Pagination",
  "value": "1:1:10",
  "caption": "页码: ",
  "theme": "dark"
}
```

### 示例 4：自定义每页记录数

```json
{
  "type": "Pagination",
  "value": "1:1:20",
  "caption": "页码: ",
  "pageCount": 50
}
```

### 示例 5：动态更新总页数

```javascript
// 创建分页组件
var pagination = ood.create({
  type: "Pagination",
  value: "1:1:10",
  caption: "页码: ",
  pageCount: 20
});

// 动态更新总记录数，自动计算总页数
function updateTotalCount(total) {
  pagination.setTotalCount(total);
}

// 调用示例：更新为100条记录
updateTotalCount(100);
```

## 总结

Pagination 组件是一个功能丰富、配置灵活的分页导航组件，具有以下特点：

- 支持多种主题模式：light、dark、high-contrast
- 响应式设计，自动适应不同屏幕尺寸
- 支持显示/隐藏更多页码按钮
- 自定义每页记录数
- 支持页码直接输入跳转
- 完整的事件处理机制
- 良好的无障碍支持
- 主题偏好本地存储
- 流畅的交互体验

Pagination 组件适用于各种需要分页导航的场景，如数据列表、图片画廊、搜索结果等，能够为用户提供直观的分页控制和良好的交互体验。