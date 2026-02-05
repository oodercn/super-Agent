# Notification 组件手册

## 1. 概述

Notification 组件是 ooder-a2ui 前端框架中用于显示临时消息提示的组件，通常用于操作结果反馈、状态变更通知、系统提示等场景。它提供了灵活的配置方式，支持多种类型提示、自动消失、位置定制和主题切换等功能。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.Notification
```

### 2.2 继承关系

```
ood.UI.Notification extends ood.UI.Widget
```

### 2.3 主要功能

- 支持多种类型提示（成功、错误、警告、信息、加载中）
- 支持自动消失和手动关闭
- 支持自定义位置（顶部、中部、底部）
- 支持主题切换
- 支持响应式设计
- 支持无障碍访问
- 支持键盘导航

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Notification 组件：

```javascript
// 创建Notification组件
var notification = ood.create("ood.UI.Notification")
    .setHost(host, "notification")
    .setName("notification")
    .setProperties({
        message: "操作成功",
        type: "success",
        position: "top",
        duration: 3000
    });
```

### 3.2 使用JSON配置创建

```json
{
  "id": "notification",
  "type": "Notification",
  "props": {
    "message": "数据加载完成",
    "type": "info",
    "position": "center",
    "duration": 2000
  }
}
```

### 3.3 快速创建（静态方法）

```javascript
// 快速显示成功消息
ood.UI.Notification.success("保存成功");

// 快速显示错误消息
ood.UI.Notification.error("操作失败，请重试");

// 快速显示警告消息
ood.UI.Notification.warning("数据格式不正确");

// 快速显示信息消息
ood.UI.Notification.info("新消息通知");

// 快速显示加载中消息
ood.UI.Notification.loading("数据加载中...");
```

## 4. 核心属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `message` | String | "" | 通知消息内容 |
| `type` | String | "info" | 通知类型，可选值："success"、"error"、"warning"、"info"、"loading" |
| `position` | String | "top" | 显示位置，可选值："top"、"center"、"bottom" |
| `duration` | Number | 3000 | 显示时长（毫秒），0表示不自动关闭 |
| `width` | String | "auto" | 组件宽度 |
| `height` | String | "auto" | 组件高度 |
| `theme` | String | "light" | 主题，可选值："light"、"dark" |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `icon` | Boolean | true | 是否显示图标 |
| `closable` | Boolean | false | 是否显示关闭按钮 |

## 5. 方法详解

### 5.1 消息显示与隐藏

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **show** | `show(message, type, duration)` | 显示通知消息 |
| **hide** | `hide()` | 隐藏通知消息 |
| **success** | `success(message, duration)` | 静态方法，快速显示成功消息 |
| **error** | `error(message, duration)` | 静态方法，快速显示错误消息 |
| **warning** | `warning(message, duration)` | 静态方法，快速显示警告消息 |
| **info** | `info(message, duration)` | 静态方法，快速显示信息消息 |
| **loading** | `loading(message)` | 静态方法，快速显示加载中消息 |

### 5.2 消息内容与类型

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setMessage** | `setMessage(message)` | 设置通知消息内容 |
| **getMessage** | `getMessage()` | 获取通知消息内容 |
| **setType** | `setType(type)` | 设置通知类型 |
| **getType** | `getType()` | 获取通知类型 |

### 5.3 配置管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setPosition** | `setPosition(position)` | 设置显示位置 |
| **getPosition** | `getPosition()` | 获取显示位置 |
| **setDuration** | `setDuration(duration)` | 设置显示时长 |
| **getDuration** | `getDuration()` | 获取显示时长 |
| **setIcon** | `setIcon(icon)` | 设置是否显示图标 |
| **getIcon** | `getIcon()` | 获取是否显示图标 |
| **setClosable** | `setClosable(closable)` | 设置是否显示关闭按钮 |
| **getClosable** | `getClosable()` | 获取是否显示关闭按钮 |

### 5.4 主题管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setTheme** | `setTheme(theme)` | 设置组件主题 |
| **getTheme** | `getTheme()` | 获取当前主题 |
| **toggleDarkMode** | `toggleDarkMode()` | 切换暗黑模式 |

### 5.5 响应式设计

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **adjustLayout** | `adjustLayout()` | 调整响应式布局 |

### 5.6 无障碍访问

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **enhanceAccessibility** | `enhanceAccessibility()` | 增强无障碍访问支持 |

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| `onShow` | 通知显示时触发 |
| `onHide` | 通知隐藏时触发 |
| `onClose` | 手动关闭通知时触发 |
| `onThemeChange` | 主题改变时触发 |

### 6.2 事件设置

```javascript
// 监听通知显示事件
notification.setOnShow(function() {
    console.log("通知已显示");
});

// 监听通知隐藏事件
notification.setOnHide(function() {
    console.log("通知已隐藏");
});

// 监听手动关闭事件
notification.setOnClose(function() {
    console.log("通知已手动关闭");
});

// 监听主题改变事件
notification.getRoot().on("themeChange", function(e) {
    console.log("主题已改变为:", e.theme);
});
```

## 7. 响应式设计

Notification 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
notification.setProperties({ responsive: true });

// 自定义响应式调整
notification.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('notification-mobile');
            // 调整通知的最大宽度为屏幕宽度的90%
            notification.setWidth("90%");
        } else {
            root.removeClass('notification-mobile');
            // 恢复原始宽度
            notification.setWidth("auto");
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
notification.setTheme("dark");

// 获取当前主题
var currentTheme = notification.getTheme();

// 切换暗黑模式
notification.toggleDarkMode();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字

## 9. 无障碍访问

### 9.1 ARIA属性

Notification组件自动添加ARIA属性，提高无障碍访问性：

```html
<div class="ood-ui-notification" role="alert" aria-live="assertive" aria-atomic="true">
    <div class="ood-ui-notification-container ood-ui-notification-info">
        <div class="ood-ui-notification-icon ood-ui-notification-icon-info">i</div>
        <div class="ood-ui-notification-message">信息提示内容</div>
    </div>
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：可以聚焦到通知组件
- Escape键：关闭当前通知（如果可关闭）
- Enter/Space键：关闭当前通知（如果可关闭）

## 10. 示例代码

### 10.1 基本Notification组件

```javascript
var basicNotification = ood.create("ood.UI.Notification")
    .setHost(host, "basicNotification")
    .setName("basicNotification")
    .setProperties({
        message: "这是一条基本通知",
        type: "info",
        position: "top",
        duration: 3000
    });

// 显示通知
basicNotification.show();
```

### 10.2 不同类型的Notification

```javascript
// 成功通知
ood.UI.Notification.success("操作成功");

// 错误通知
ood.UI.Notification.error("操作失败，请重试");

// 警告通知
ood.UI.Notification.warning("数据格式不正确");

// 信息通知
ood.UI.Notification.info("新消息通知");

// 加载中通知
ood.UI.Notification.loading("数据加载中...");
```

### 10.3 自定义位置和时长

```javascript
// 底部显示，5秒后自动消失
ood.UI.Notification.show(
    "底部显示的通知，5秒后自动消失",
    "info",
    5000,
    "bottom"
);

// 居中显示，不自动消失
var persistentNotification = ood.create("ood.UI.Notification")
    .setProperties({
        message: "居中显示的持久通知，需要手动关闭",
        type: "warning",
        position: "center",
        duration: 0,
        closable: true
    });

persistentNotification.show();
```

### 10.4 带关闭按钮的Notification

```javascript
var closableNotification = ood.create("ood.UI.Notification")
    .setProperties({
        message: "带关闭按钮的通知",
        type: "info",
        closable: true
    });

closableNotification.show();
```

## 11. 最佳实践

### 11.1 数据设计

1. **合理选择通知类型**：根据消息内容选择合适的通知类型，保持视觉一致性
2. **简洁明了的消息**：通知内容应简洁明了，避免过长文本
3. **适当的显示时长**：根据消息重要性设置合适的显示时长，重要消息可设置较长时长或不自动关闭
4. **合理的位置选择**：根据使用场景选择合适的显示位置，如操作反馈通常显示在顶部

### 11.2 性能优化

1. **避免频繁显示**：避免在短时间内显示大量通知，影响用户体验
2. **合理使用自动消失**：对于不重要的通知，建议使用自动消失功能，减少用户干扰
3. **及时清理资源**：通知隐藏后，及时清理相关资源
4. **优化动画效果**：使用平滑的过渡动画，避免过度动画影响性能

### 11.3 交互设计

1. **清晰的视觉反馈**：提供明确的通知类型和状态指示
2. **适当的动画效果**：使用平滑的显示和隐藏动画，提高用户体验
3. **支持键盘导航**：确保可以通过键盘完全操作通知
4. **合理的默认设置**：根据业务需求设置合适的默认参数

### 11.4 无障碍访问

1. **添加ARIA属性**：确保Notification及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作Notification
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈
4. **语义化HTML**：使用语义化的HTML结构

## 12. 常见问题

### 12.1 通知不显示

- **问题**：调用show()方法后，通知不显示
- **解决方案**：
  1. 检查message属性是否正确设置
  2. 确保组件已经渲染到DOM中
  3. 检查position属性是否设置正确
  4. 检查z-index是否足够高，避免被其他元素遮挡

### 12.2 通知不自动消失

- **问题**：设置了duration属性，但通知不自动消失
- **解决方案**：
  1. 检查duration属性是否大于0
  2. 确保组件已经渲染到DOM中
  3. 检查是否有事件处理函数阻止了自动消失
  4. 尝试手动调用hide()方法

### 12.3 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：notification.setTheme(notification.getTheme());

### 12.4 关闭按钮不显示

- **问题**：设置了closable属性为true，但关闭按钮不显示
- **解决方案**：
  1. 检查closable属性是否设置为true
  2. 确保组件已经渲染到DOM中
  3. 检查CSS样式是否隐藏了关闭按钮
  4. 尝试重新渲染组件

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础Notification功能 |
| v1.1.0 | 支持多种通知类型和自动消失 |
| v1.2.0 | 支持主题切换和响应式设计 |
| v1.3.0 | 增强无障碍访问支持 |
| v1.4.0 | 支持静态方法快速创建通知 |

## 14. 总结

Notification 组件是 ooder-a2ui 框架中用于显示临时消息提示的核心组件，通过灵活的配置和组合，可以实现各种复杂的消息提示效果。它支持多种类型提示、自动消失、位置定制、主题切换和响应式设计，能够满足不同场景下的需求。

在实际应用中，Notification 组件通常用于操作结果反馈、状态变更通知、系统提示等场景。通过合理的配置和优化，可以创建出既美观又实用的消息提示效果，提升用户体验。