# ButtonLayout 组件手册

## 1. 概述

ButtonLayout 组件是 ooder-agent-rad 前端框架中用于按钮布局的核心组件，支持网格布局、响应式设计、主题切换和无障碍访问。它提供了直观的方式来组织和排列按钮，适用于各种按钮组场景。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.ButtonLayout
```

### 2.2 继承关系

```
ood.UI.ButtonLayout extends ood.UI.List
```

### 2.3 主要功能

- 网格布局：支持行列配置，自动排列按钮
- 响应式设计：自动适应不同屏幕尺寸
- 主题支持：支持浅色/深色主题切换
- 无障碍访问：支持 ARIA 属性和键盘导航
- 图标支持：支持字体图标和图片图标
- 自定义样式：支持自定义按钮样式和颜色

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 ButtonLayout 组件：

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setName("buttonLayout")
    .setWidth("100%")
    .setHeight("100%")
    .setColumns(5)
    .setItems([
        {
            caption: "首页",
            id: "index",
            imageClass: "ri-link"
        },
        {
            caption: "待办",
            id: "waitedwork",
            imageClass: "ri-calendar-line"
        },
        {
            caption: "起草",
            id: "startWork",
            imageClass: "ri-flashlight-line"
        }
    ]);
```

### 3.2 配置默认属性

通过 `iniProp` 对象配置默认属性：

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setName("buttonLayout")
    .setIniProp({
        "value": "mywork",
        "showDirtyMark": false,
        "items": [
            {
                "caption": "首页",
                "id": "index",
                "imageClass": "ri-link"
            },
            {
                "caption": "待办",
                "id": "waitedwork",
                "imageClass": "ri-calendar-line"
            }
        ],
        "dock": "fill",
        "borderType": "none",
        "iconFontSize": "2em",
        "columns": 5
    });
```

## 4. 核心属性

### 4.1 基础属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| name | String | "ButtonLayout" | 组件名称 |
| width | String | "16rem" | 组件宽度 |
| height | String | "16rem" | 组件高度 |
| value | String | "mywork" | 当前选中项ID |
| columns | Number | 5 | 列数 |
| rows | Number | 0 | 行数 |
| dock | String | "fill" | 停靠方式 |
| borderType | String | "none" | 边框类型 |
| iconFontSize | String | "2em" | 图标字体大小 |

### 4.2 主题与样式属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| theme | String | "light" | 主题（light/dark/high-contrast） |
| responsive | Boolean | true | 是否启用响应式设计 |
| autoColumns | Boolean | false | 是否自动调整列数 |
| autoFontColor | Boolean | false | 是否自动字体颜色 |
| autoIconColor | Boolean | true | 是否自动图标颜色 |
| autoItemColor | Boolean | false | 是否自动项目颜色 |
| autoImgSize | Boolean | false | 是否自动图片大小 |
| autoItemSize | Boolean | true | 是否自动项目大小 |
| iconOnly | Boolean | false | 是否仅显示图标 |

### 4.3 间距与尺寸属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| itemMargin | Number | 6 | 项目边距 |
| itemPadding | Number | 2 | 项目内边距 |
| itemWidth | Number | 32 | 项目宽度 |
| itemHeight | Number | 32 | 项目高度 |
| imgWidth | Number | 16 | 图片宽度 |
| imgHeight | Number | 16 | 图片高度 |

## 5. 方法详解

### 5.1 主题管理

#### setTheme(theme)

设置组件主题：

```javascript
buttonLayout.setTheme("dark"); // 设置为深色主题
```

#### getTheme()

获取当前主题：

```javascript
var currentTheme = buttonLayout.getTheme();
```

#### toggleDarkMode()

切换暗黑模式：

```javascript
buttonLayout.toggleDarkMode();
```

### 5.2 响应式设计

#### adjustLayout()

调整布局以适应屏幕尺寸：

```javascript
buttonLayout.adjustLayout();
```

### 5.3 无障碍访问

#### enhanceAccessibility()

增强无障碍访问支持：

```javascript
buttonLayout.enhanceAccessibility();
```

### 5.4 项目管理

#### setItems(items)

设置按钮项：

```javascript
buttonLayout.setItems([
    {
        caption: "首页",
        id: "index",
        imageClass: "ri-link"
    },
    {
        caption: "待办",
        id: "waitedwork",
        imageClass: "ri-calendar-line"
    }
]);
```

#### updateItemData(profile, item)

更新项目数据：

```javascript
buttonLayout.updateItemData(profile, {
    id: "index",
    caption: "首页",
    imageClass: "ri-home-line"
});
```

#### updateItem(id, item)

更新指定项目：

```javascript
buttonLayout.updateItem("index", {
    caption: "首页",
    imageClass: "ri-home-line"
});
```

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| onCmd | 命令执行时触发 |
| onFlagClick | 标记点击时触发 |
| touchstart | 触摸开始时触发 |
| touchmove | 触摸移动时触发 |
| touchend | 触摸结束时触发 |
| touchcancel | 触摸取消时触发 |
| swipe | 滑动时触发 |
| swipeleft | 向左滑动时触发 |
| swiperight | 向右滑动时触发 |
| swipeup | 向上滑动时触发 |
| swipedown | 向下滑动时触发 |
| press | 长按开始时触发 |
| pressup | 长按结束时触发 |

### 6.2 事件设置

```javascript
// 监听命令执行事件
buttonLayout.setOnCmd(function(profile, item, e, src) {
    console.log("命令执行:", item.id);
});

// 监听标记点击事件
buttonLayout.setOnFlagClick(function(profile, item, e, src) {
    console.log("标记点击:", item.id);
});
```

## 7. 响应式设计

### 7.1 断点设计

ButtonLayout 组件根据屏幕宽度自动调整布局：

| 屏幕宽度 | 布局调整 | CSS类名 |
|---------|----------|--------|
| < 480px | 小屏幕模式，进一步压缩间距 | buttonlayout-tiny |
| < 768px | 移动端模式，调整字体大小和内边距 | buttonlayout-mobile |
| < 1024px | 小屏桌面模式，调整列数 | buttonlayout-small |

### 7.2 自动列数调整

当 `autoColumns` 属性为 `true` 时，组件会根据屏幕宽度自动调整列数：

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setAutoColumns(true);
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
buttonLayout.setTheme("dark");

// 切换主题
buttonLayout.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字
- 高对比度主题：高对比度颜色方案

## 9. 无障碍访问

### 9.1 ARIA 属性

组件自动添加 ARIA 属性：

```html
<div role="grid" aria-label="按钮布局">
    <div role="gridcell" aria-label="首页" tabindex="0">
        <div class="oodfont ri-home-line"></div>
        <div>首页</div>
    </div>
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab 键：在按钮之间导航
- 方向键：在按钮之间移动
- Enter/Space 键：触发按钮点击

## 10. 示例代码

### 10.1 基本按钮布局

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setName("buttonLayout")
    .setWidth("100%")
    .setHeight("100%")
    .setColumns(4)
    .setItems([
        {
            caption: "新建",
            id: "new",
            imageClass: "ri-add-line"
        },
        {
            caption: "编辑",
            id: "edit",
            imageClass: "ri-edit-line"
        },
        {
            caption: "删除",
            id: "delete",
            imageClass: "ri-delete-line"
        },
        {
            caption: "保存",
            id: "save",
            imageClass: "ri-save-line"
        }
    ]);
```

### 10.2 响应式按钮布局

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setName("buttonLayout")
    .setWidth("100%")
    .setHeight("100%")
    .setResponsive(true)
    .setAutoColumns(true)
    .setItems([
        {
            caption: "首页",
            id: "index",
            imageClass: "ri-home-line"
        },
        {
            caption: "待办",
            id: "waitedwork",
            imageClass: "ri-calendar-line"
        },
        {
            caption: "起草",
            id: "startWork",
            imageClass: "ri-flashlight-line"
        },
        {
            caption: "我的",
            id: "mywork",
            imageClass: "ri-user-line"
        },
        {
            caption: "消息",
            id: "msg",
            imageClass: "ri-message-line"
        }
    ]);
```

### 10.3 带图标和文字的按钮布局

```javascript
var buttonLayout = ood.create("ood.UI.ButtonLayout")
    .setHost(host, "buttonLayout")
    .setName("buttonLayout")
    .setWidth("100%")
    .setHeight("100%")
    .setColumns(3)
    .setIconFontSize("1.5em")
    .setItems([
        {
            caption: "用户管理",
            comment: "管理系统用户",
            id: "user",
            imageClass: "ri-user-settings-line"
        },
        {
            caption: "角色管理",
            comment: "管理用户角色",
            id: "role",
            imageClass: "ri-shield-keyhole-line"
        },
        {
            caption: "权限管理",
            comment: "管理系统权限",
            id: "permission",
            imageClass: "ri-key-2-line"
        }
    ]);
```

## 11. 最佳实践

### 11.1 布局设计

1. **合理规划行列**：根据按钮数量和容器大小规划合适的行列数
2. **使用响应式设计**：确保在不同设备上都有良好的显示效果
3. **统一图标风格**：使用相同风格的图标（如都是字体图标或都是图片图标）
4. **适当的间距**：设置合适的内边距和外边距，保证良好的可读性

### 11.2 性能优化

1. **减少不必要的按钮**：只显示必要的按钮
2. **合理使用自动调整**：根据需要启用自动列数和自动大小调整
3. **优化图标加载**：使用字体图标或优化图片图标大小

### 11.3 可维护性

1. **模块化设计**：将复杂按钮组拆分为多个模块
2. **清晰的命名**：使用清晰的按钮ID和名称
3. **统一的风格**：保持按钮风格的一致性
4. **文档化**：为复杂按钮组添加文档说明

## 12. 常见问题

### 12.1 布局显示异常

- **问题**：按钮布局显示不正确
- **解决方案**：
  1. 检查 `columns` 和 `rows` 配置是否正确
  2. 确保已设置正确的宽度和高度
  3. 检查是否有样式冲突

### 12.2 主题切换无效

- **问题**：主题切换后样式没有变化
- **解决方案**：
  1. 确保已调用 `setTheme()` 方法
  2. 检查主题 CSS 类是否已正确添加
  3. 检查浏览器缓存

### 12.3 响应式设计不生效

- **问题**：在不同设备上显示效果相同
- **解决方案**：
  1. 确保已启用响应式设计（`responsive: true`）
  2. 检查 `adjustLayout()` 方法是否被调用
  3. 检查 CSS 媒体查询是否正确

### 12.4 图标显示异常

- **问题**：图标显示不正确或不显示
- **解决方案**：
  1. 检查图标字体是否已正确加载
  2. 检查 `imageClass` 属性是否正确
  3. 检查图标字体大小设置是否合适

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础按钮布局功能 |
| v1.1.0 | 响应式设计支持 |
| v1.2.0 | 主题切换和无障碍访问支持 |
| v1.3.0 | 自动列数调整和现代化样式 |

## 14. 总结

ButtonLayout 组件是一个功能强大的按钮布局组件，支持网格布局、响应式设计、主题切换和无障碍访问。它提供了直观的方式来组织和排列按钮，适用于各种按钮组场景。通过合理使用 ButtonLayout 组件，可以提高界面的可用性和美观度，同时减少开发工作量。