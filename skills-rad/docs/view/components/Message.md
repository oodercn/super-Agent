# Message 组件

## 1. 组件概述

Message 组件是 ooder-a2ui 框架中的消息处理组件，提供了两种主要功能：消息服务（MessageService）和消息提示（Toast）。它支持发布-订阅模式的消息传递机制，以及各种类型的消息提示展示，是构建交互式应用的重要组件。

### 1.1 核心功能

- **消息服务（MessageService）**：
  - 基于发布-订阅模式的消息传递
  - 支持多种消息类型和接收者类型
  - 异步消息处理
  - 消息回执机制
  - 批量消息订阅和发布

- **消息提示（Toast）**：
  - 多种消息类型（成功、错误、警告、信息、加载）
  - 自动消失和手动关闭
  - 可配置的显示位置
  - 支持自定义内容和样式
  - 支持动画效果

### 1.2 应用场景

- **消息服务**：
  - 组件间通信
  - 模块间数据传递
  - 事件驱动架构
  - 实时消息推送
  - 跨页面消息传递

- **消息提示**：
  - 操作结果反馈
  - 加载状态提示
  - 错误信息展示
  - 成功操作确认
  - 系统通知

## 2. 消息服务（MessageService）

### 2.1 创建方式

```javascript
// 创建 MessageService 实例
var messageService = ood.create("ood.MessageService")
    .setProperties({
        recipientType: "user.notification,system.alert",
        asynReceive: false
    });
```

### 2.2 属性配置

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `recipientType` | String | "" | 接收者类型，支持多个类型用逗号分隔 |
| `msgType` | String | "" | 消息类型（兼容旧版本，建议使用 recipientType） |
| `asynReceive` | Boolean | false | 是否异步接收消息 |

### 2.3 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `broadcast(recipientType, msg1, msg2, msg3, msg4, msg5, readReceipt)` | 广播消息到指定接收者类型 | recipientType: 接收者类型<br>msg1-msg5: 消息内容<br>readReceipt: 消息回执回调 | 当前组件实例 |
| `setRecipientType(type, init)` | 设置接收者类型 | type: 接收者类型<br>init: 是否初始化 | 当前组件实例 |
| `destroy()` | 销毁消息服务实例 | 无 | 无 |

### 2.4 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| `onMessageReceived` | 收到消息时触发 | profile: 组件配置<br>msg1-msg5: 消息内容<br>readReceipt: 消息回执函数 |
| `onReceipt` | 消息回执时触发 | profile: 组件配置<br>recipientType: 接收者类型<br>args: 回执参数 |

### 2.5 使用示例

#### 2.5.1 订阅和发布消息

```javascript
// 创建消息服务实例
var messageService = ood.create("ood.MessageService")
    .setProperties({
        recipientType: "user.notification",
        onMessageReceived: function(profile, msg1, msg2, msg3, readReceipt) {
            console.log("收到消息:", msg1, msg2, msg3);
            // 调用回执函数
            if (readReceipt) {
                readReceipt("已收到消息");
            }
        },
        onReceipt: function(profile, recipientType, args) {
            console.log("消息已被接收:", recipientType, args);
        }
    });

// 发布消息
messageService.broadcast("user.notification", "操作成功", {code: 200}, "详细信息");
```

#### 2.5.2 多个消息类型

```javascript
// 创建消息服务实例，订阅多个消息类型
var messageService = ood.create("ood.MessageService")
    .setProperties({
        recipientType: "user.notification,system.alert,data.update",
        onMessageReceived: function(profile, msg1, msg2, msg3) {
            console.log("收到消息:", msg1, msg2, msg3);
        }
    });

// 发布到特定消息类型
messageService.broadcast("system.alert", "系统警告", "服务器负载过高");
messageService.broadcast("data.update", "数据更新", {table: "users", id: 123});
```

## 3. 消息提示（Toast）

### 3.1 创建方式

#### 3.1.1 快速创建（静态方法）

```javascript
// 成功消息
ood.Mobile.Toast.success('操作成功！');

// 错误消息
ood.Mobile.Toast.error('网络请求失败');

// 警告消息
ood.Mobile.Toast.warning('请检查输入内容');

// 信息消息
ood.Mobile.Toast.info('数据正在加载中');

// 加载消息
ood.Mobile.Toast.loading('加载中...', 3000);
```

#### 3.1.2 实例化创建

```javascript
// 创建 Toast 实例
var toast = new ood.Mobile.Toast({
    type: 'success',
    message: '操作成功！',
    duration: 2000,
    position: 'top',
    onClose: function() {
        console.log('Toast 已关闭');
    }
});

// 显示 Toast
toast.show();

// 手动隐藏 Toast
setTimeout(function() {
    toast.hide();
}, 1000);
```

### 3.2 属性配置

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `type` | String | "info" | 消息类型：success, error, warning, info, loading |
| `message` | String | "" | 消息内容 |
| `duration` | Number | 2000 | 显示时长（毫秒），0 表示不自动关闭 |
| `position` | String | "center" | 显示位置：top, middle, bottom, center |
| `showClose` | Boolean | false | 是否显示关闭按钮 |
| `closeOnClick` | Boolean | true | 是否点击关闭 |
| `mask` | Boolean | false | 是否显示遮罩层 |
| `zIndex` | Number | 9999 | 层级 |
| `animation` | Boolean | true | 是否显示动画 |
| `className` | String | "" | 自定义类名 |

### 3.3 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `show()` | 显示 Toast | 无 | 当前实例 |
| `hide()` | 隐藏 Toast | 无 | 当前实例 |
| `destroy()` | 销毁 Toast | 无 | 无 |

### 3.4 静态方法

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `success(message, duration, options)` | 显示成功消息 | message: 消息内容<br>duration: 显示时长<br>options: 配置项 | Toast 实例 |
| `error(message, duration, options)` | 显示错误消息 | message: 消息内容<br>duration: 显示时长<br>options: 配置项 | Toast 实例 |
| `warning(message, duration, options)` | 显示警告消息 | message: 消息内容<br>duration: 显示时长<br>options: 配置项 | Toast 实例 |
| `info(message, duration, options)` | 显示信息消息 | message: 消息内容<br>duration: 显示时长<br>options: 配置项 | Toast 实例 |
| `loading(message, duration, options)` | 显示加载消息 | message: 消息内容<br>duration: 显示时长<br>options: 配置项 | Toast 实例 |

### 3.5 事件处理

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| `onOpen` | Toast 打开时触发 | 无 |
| `onClose` | Toast 关闭时触发 | 无 |
| `onClick` | Toast 点击时触发 | 无 |

### 3.6 使用示例

#### 3.6.1 不同类型的 Toast

```javascript
// 成功消息
ood.Mobile.Toast.success('保存成功！', 2000, {
    position: 'top'
});

// 错误消息
ood.Mobile.Toast.error('操作失败，请重试', 3000, {
    showClose: true
});

// 警告消息
ood.Mobile.Toast.warning('请注意，此操作不可恢复', 2500);

// 信息消息
ood.Mobile.Toast.info('您有新的消息', 2000);

// 加载消息（不自动关闭）
var loadingToast = ood.Mobile.Toast.loading('数据加载中...', 0, {
    position: 'center',
    mask: true
});

// 模拟数据加载完成后隐藏
setTimeout(function() {
    loadingToast.hide();
    ood.Mobile.Toast.success('数据加载完成');
}, 3000);
```

#### 3.6.2 自定义 Toast

```javascript
// 自定义 Toast 内容和样式
var customToast = new ood.Mobile.Toast({
    message: '<div style="display:flex;align-items:center;"><span style="color:#4CAF50;margin-right:10px;">✓</span> 自定义内容</div>',
    duration: 3000,
    position: 'bottom',
    className: 'custom-toast',
    closeOnClick: true,
    onClose: function() {
        console.log('自定义 Toast 已关闭');
    }
});

customToast.show();
```

## 4. 最佳实践

### 4.1 消息服务最佳实践

1. **合理设计消息类型**：
   - 使用清晰的命名规范（如：模块名.功能名）
   - 避免过于宽泛的消息类型
   - 为不同业务场景设计独立的消息类型

2. **消息内容设计**：
   - 消息内容应包含足够的上下文信息
   - 建议使用对象格式传递复杂消息
   - 包含必要的元数据（如：时间戳、发送者、优先级）

3. **性能优化**：
   - 避免频繁发送大量消息
   - 合理使用异步接收
   - 及时销毁不再使用的消息服务实例

4. **错误处理**：
   - 实现消息接收失败的重试机制
   - 添加消息日志记录
   - 处理消息格式错误

### 4.2 消息提示最佳实践

1. **消息类型选择**：
   - 成功操作使用 success 类型
   - 错误信息使用 error 类型
   - 警告信息使用 warning 类型
   - 普通信息使用 info 类型
   - 加载状态使用 loading 类型

2. **显示时长控制**：
   - 简短提示使用 1500-2000ms
   - 重要信息使用 2500-3000ms
   - 加载状态使用 0（手动关闭）
   - 复杂内容使用更长时长

3. **位置选择**：
   - 顶部：系统通知、全局提示
   - 中部：重要操作结果
   - 底部：普通提示、辅助信息
   - 居中：加载状态、确认信息

4. **用户体验**：
   - 避免同时显示多个 Toast
   - 提供关闭按钮（特别是长时长消息）
   - 使用清晰的图标和文字
   - 保持样式一致性

## 5. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分功能可能受限 |

## 6. 注意事项

1. **消息服务注意事项**：
   - 消息服务是基于发布-订阅模式，不保证消息的可靠传递
   - 消息内容不宜过大，建议不超过 1MB
   - 避免循环消息导致性能问题
   - 注意消息类型的命名冲突

2. **消息提示注意事项**：
   - 避免过度使用 Toast，影响用户体验
   - 重要信息建议使用 Dialog 组件
   - 确保 Toast 内容清晰可读
   - 考虑移动端和桌面端的显示差异
   - 注意 accessibility 支持

3. **性能考虑**：
   - 频繁创建和销毁 Toast 可能影响性能
   - 建议使用 Toast 池或复用实例
   - 避免在 Toast 中使用复杂的 DOM 结构
   - 注意动画效果对性能的影响

## 7. 扩展阅读

- [发布-订阅模式详解](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern)
- [组件间通信最佳实践](https://reactjs.org/docs/faq-communication.html)
- [消息队列与事件驱动架构](https://martinfowler.com/articles/201701-event-driven.html)
- [Toast 组件设计指南](https://material.io/components/snackbars)
- [交互反馈设计原则](https://www.nngroup.com/articles/feedback-principles/)

Message 组件为 ooder-a2ui 框架提供了完整的消息处理解决方案，无论是组件间通信还是用户反馈，都能提供高效、灵活的实现方式。通过合理使用 Message 组件，可以构建出更加交互式、响应式的应用程序。