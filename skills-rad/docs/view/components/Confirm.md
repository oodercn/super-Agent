# Confirm 组件手册

## 1. 概述

Confirm 组件是 ooder-a2ui 前端框架中用于显示确认对话框的组件，通常用于需要用户明确确认的操作场景，如删除数据、执行重要操作等。它提供了灵活的配置方式，支持自定义标题、内容、按钮文本和回调函数等功能。

## 2. 核心概念

### 2.1 组件类名

```
ood.UI.Confirm
```

### 2.2 继承关系

```
ood.UI.Confirm extends ood.UI.Dialog
```

### 2.3 主要功能

- 支持自定义标题和内容
- 支持自定义按钮文本
- 支持回调函数处理用户选择
- 支持键盘导航（Esc键关闭）
- 支持主题切换
- 支持响应式设计
- 支持无障碍访问

## 3. 组件创建与初始化

### 3.1 基本创建

使用 `ood.create()` 方法创建 Confirm 组件：

```javascript
// 创建Confirm组件
var confirm = ood.create("ood.UI.Confirm")
    .setHost(host, "confirm")
    .setName("confirm")
    .setProperties({
        caption: "确认操作",
        message: "确定要删除这条数据吗？",
        btnYes: "确定",
        btnNo: "取消"
    });
```

### 3.2 使用静态方法快速创建

```javascript
// 使用静态方法快速创建确认对话框
ood.confirm(
    "确认删除",
    "确定要删除这条记录吗？此操作不可恢复。",
    function() {
        console.log("用户点击了确定");
        // 执行删除操作
    },
    function() {
        console.log("用户点击了取消");
        // 取消删除操作
    }
);
```

### 3.3 使用JSON配置创建

```json
{
  "id": "confirm",
  "type": "Confirm",
  "props": {
    "caption": "确认操作",
    "message": "确定要执行此操作吗？",
    "btnYes": "确定",
    "btnNo": "取消"
  }
}
```

## 4. 核心属性

| 属性名称 | 类型 | 默认值 | 描述 |
|---------|------|--------|------|
| `caption` | String | "确认" | 对话框标题 |
| `message` | String | "" | 确认消息内容 |
| `btnYes` | String | "确定" | 确认按钮文本 |
| `btnNo` | String | "取消" | 取消按钮文本 |
| `width` | String | "auto" | 组件宽度 |
| `height` | String | "auto" | 组件高度 |
| `theme` | String | "dark" | 主题，可选值："light"、"dark"、"system" |
| `responsive` | Boolean | true | 是否启用响应式设计 |
| `modal` | Boolean | true | 是否显示为模态对话框 |
| `overflow` | String | "hidden" | 内容溢出处理方式 |

## 5. 方法详解

### 5.1 显示与隐藏

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **show** | `show(parent, modal, left, top)` | 显示确认对话框 |
| **showModal** | `showModal(parent, left, top)` | 以模态方式显示确认对话框 |
| **hide** | `hide()` | 隐藏确认对话框 |
| **close** | `close()` | 关闭确认对话框 |

### 5.2 静态方法

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **confirm** | `confirm(title, caption, onYes, onNo, btnCapYes, btnCapNo, left, top, parent, subId, noCache)` | 静态方法，快速创建并显示确认对话框 |
| **alert** | `alert(title, content, btnCaption, left, top, parent, subId)` | 静态方法，快速创建并显示提示对话框 |
| **prompt** | `prompt(title, caption, value, onOk, onCancel, btnCapOk, btnCapCancel, left, top, parent, subId)` | 静态方法，快速创建并显示输入对话框 |

### 5.3 主题管理

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **setTheme** | `setTheme(theme)` | 设置组件主题 |
| **getTheme** | `getTheme()` | 获取当前主题 |
| **toggleTheme** | `toggleTheme()` | 切换主题 |
| **detectSystemTheme** | `detectSystemTheme()` | 检测系统主题偏好 |
| **initTheme** | `initTheme()` | 初始化主题 |

### 5.4 响应式设计

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **adjustLayout** | `adjustLayout()` | 调整响应式布局 |

### 5.5 无障碍访问

| 方法名 | 签名 | 说明 |
|--------|------|------|
| **enhanceAccessibility** | `enhanceAccessibility()` | 增强无障碍访问支持 |

## 6. 事件处理

### 6.1 内置事件

| 事件名称 | 描述 |
|---------|------|
| `onShow` | 对话框显示时触发 |
| `onHide` | 对话框隐藏时触发 |
| `onClose` | 对话框关闭时触发 |
| `onYes` | 用户点击确认按钮时触发 |
| `onNo` | 用户点击取消按钮时触发 |
| `onThemeChange` | 主题改变时触发 |
| `beforeClose` | 对话框关闭前触发 |
| `onHotKeydown` | 键盘按键按下时触发 |

### 6.2 事件设置

```javascript
// 监听确认事件
confirm.setOnYes(function() {
    console.log("用户点击了确认");
    // 执行确认操作
});

// 监听取消事件
confirm.setOnNo(function() {
    console.log("用户点击了取消");
    // 执行取消操作
});

// 监听对话框显示事件
confirm.setOnShow(function() {
    console.log("确认对话框已显示");
});

// 监听对话框关闭事件
confirm.setOnClose(function() {
    console.log("确认对话框已关闭");
});
```

## 7. 响应式设计

Confirm 组件支持响应式设计，可以根据屏幕尺寸自动调整布局：

```javascript
// 启用响应式设计
confirm.setProperties({ responsive: true });

// 自定义响应式调整
confirm.adjustLayout = function() {
    return this.each(function(profile) {
        var root = profile.getRoot();
        var width = ood(document.body).cssSize().width;
        
        // 小屏幕处理
        if (width < 768) {
            root.addClass('confirm-mobile');
            // 调整对话框的最大宽度为屏幕宽度的90%
            confirm.setWidth("90%");
        } else {
            root.removeClass('confirm-mobile');
            // 恢复原始宽度
            confirm.setWidth("auto");
        }
    });
};
```

## 8. 主题支持

### 8.1 主题切换

```javascript
// 设置主题
confirm.setTheme("light");

// 获取当前主题
var currentTheme = confirm.getTheme();

// 切换主题
confirm.toggleTheme();
```

### 8.2 主题样式

组件会根据主题自动调整样式：

- 浅色主题：白色背景，深色文字
- 深色主题：深色背景，浅色文字
- 系统主题：跟随系统主题偏好

## 9. 无障碍访问

### 9.1 ARIA属性

Confirm组件自动添加ARIA属性，提高无障碍访问性：

```html
<div class="ood-ui-confirm" role="dialog" aria-modal="true" aria-labelledby="confirm-caption">
    <div class="ood-ui-confirm-container">
        <div id="confirm-caption" class="ood-ui-confirm-caption" role="heading" aria-level="1">确认操作</div>
        <div class="ood-ui-confirm-message" role="document">确定要执行此操作吗？</div>
        <div class="ood-ui-confirm-buttons">
            <button class="ood-ui-confirm-btn-yes" role="button" aria-label="确定" tabindex="1">确定</button>
            <button class="ood-ui-confirm-btn-no" role="button" aria-label="取消" tabindex="1">取消</button>
        </div>
    </div>
</div>
```

### 9.2 键盘导航

支持键盘导航和焦点管理：

- Tab键：在确认和取消按钮间切换焦点
- Escape键：关闭确认对话框（等同于点击取消按钮）
- Enter/Space键：触发当前焦点按钮的点击事件

## 10. 示例代码

### 10.1 基本Confirm组件

```javascript
var basicConfirm = ood.create("ood.UI.Confirm")
    .setHost(host, "basicConfirm")
    .setName("basicConfirm")
    .setProperties({
        caption: "确认操作",
        message: "确定要执行此操作吗？",
        btnYes: "确定",
        btnNo: "取消"
    });

// 显示确认对话框
basicConfirm.show();
```

### 10.2 使用静态方法创建Confirm

```javascript
// 使用静态方法快速创建确认对话框
ood.confirm(
    "确认删除",
    "确定要删除这条记录吗？此操作不可恢复。",
    function() {
        console.log("用户点击了确定，执行删除操作");
        // 执行删除操作
        // deleteRecord(recordId);
    },
    function() {
        console.log("用户点击了取消，取消删除操作");
        // 取消删除操作
    }
);
```

### 10.3 自定义按钮文本

```javascript
// 自定义按钮文本的确认对话框
ood.confirm(
    "确认提交",
    "确定要提交表单吗？提交后将无法修改。",
    function() {
        console.log("用户点击了提交");
        // 提交表单
        // submitForm();
    },
    function() {
        console.log("用户点击了返回");
        // 返回编辑
    },
    "提交",
    "返回"
);
```

### 10.4 不同主题的Confirm

```javascript
// 浅色主题的确认对话框
var lightConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "确认操作",
        message: "确定要执行此操作吗？",
        theme: "light"
    });

// 深色主题的确认对话框
var darkConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "确认操作",
        message: "确定要执行此操作吗？",
        theme: "dark"
    });
```

### 10.5 带HTML内容的确认对话框

```javascript
// 带HTML内容的确认对话框
ood.confirm(
    "HTML内容示例",
    "<div style='text-align: center;'>"
    + "<h3>注意事项</h3>"
    + "<p>此操作将影响以下内容：</p>"
    + "<ul style='text-align: left; margin: 10px 20px;'>"
    + "<li>数据将被永久删除</li>"
    + "<li>无法恢复</li>"
    + "<li>影响其他关联数据</li>"
    + "</ul>"
    + "<p style='color: red; font-weight: bold;'>请谨慎操作！</p>"
    + "</div>",
    function() {
        console.log("用户确认操作");
    },
    function() {
        console.log("用户取消操作");
    }
);
```

### 10.6 带自定义样式的确认对话框

```javascript
// 带自定义样式的确认对话框
var styledConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "自定义样式确认",
        message: "这是一个带有自定义样式的确认对话框",
        btnYes: "确定",
        btnNo: "取消"
    });

// 显示对话框
styledConfirm.showModal();

// 添加自定义CSS
var css = ".ood-ui-confirm {
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}
.ood-ui-confirm-caption {
    background-color: #4CAF50;
    color: white;
    padding: 12px;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
}
.ood-ui-confirm-message {
    padding: 20px;
    font-size: 16px;
    line-height: 1.5;
}
.ood-ui-confirm-buttons {
    padding: 12px;
    background-color: #f5f5f5;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
    text-align: right;
}
.ood-ui-confirm-btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: 8px;
    font-weight: bold;
}
.ood-ui-confirm-btn-yes {
    background-color: #4CAF50;
    color: white;
}
.ood-ui-confirm-btn-no {
    background-color: #f44336;
    color: white;
}";

var styleSheet = document.createElement("style");
styleSheet.type = "text/css";
styleSheet.innerText = css;
document.head.appendChild(styleSheet);
```

### 10.7 带超时自动关闭功能的确认对话框

```javascript
// 带超时自动关闭功能的确认对话框
var timeoutConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "自动关闭确认",
        message: "此确认对话框将在5秒后自动关闭",
        btnYes: "确定",
        btnNo: "取消"
    });

// 显示对话框
var dialog = timeoutConfirm.showModal();

// 设置5秒后自动关闭
var countdown = 5;
var timer = setInterval(function() {
    countdown--;
    timeoutConfirm.setProperties({
        message: "此确认对话框将在" + countdown + "秒后自动关闭"
    });
    
    if (countdown <= 0) {
        clearInterval(timer);
        timeoutConfirm.close();
        console.log("确认对话框已自动关闭");
    }
}, 1000);

// 用户点击按钮时清除定时器
timeoutConfirm.setOnYes(function() {
    clearInterval(timer);
    console.log("用户点击了确定");
});

timeoutConfirm.setOnNo(function() {
    clearInterval(timer);
    console.log("用户点击了取消");
});
```

### 10.8 模态与非模态确认对话框

```javascript
// 模态确认对话框
var modalConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "模态确认",
        message: "这是一个模态确认对话框，需要用户操作才能关闭",
        btnYes: "确定",
        btnNo: "取消"
    });

// 以模态方式显示
modalConfirm.showModal();

// 非模态确认对话框
var nonModalConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "非模态确认",
        message: "这是一个非模态确认对话框，用户可以点击背景关闭",
        btnYes: "确定",
        btnNo: "取消"
    });

// 以非模态方式显示
nonModalConfirm.show();
```

### 10.9 带加载状态的确认对话框

```javascript
// 带加载状态的确认对话框
var loadingConfirm = ood.create("ood.UI.Confirm")
    .setProperties({
        caption: "处理中...",
        message: "请稍候，系统正在处理您的请求",
        btnYes: "确定",
        btnNo: "取消"
    });

// 显示对话框
loadingConfirm.showModal();

// 模拟处理过程
setTimeout(function() {
    // 更新对话框内容
    loadingConfirm.setProperties({
        caption: "处理完成",
        message: "请求处理成功！",
        btnYes: "确定",
        btnNo: "关闭"
    });
    
    // 添加成功图标
    var root = loadingConfirm.getRoot();
    var messageNode = root.querySelector(".ood-ui-confirm-message");
    messageNode.innerHTML = "<div style='display: flex; align-items: center; justify-content: center;'>" +
        "<div style='font-size: 48px; margin-right: 16px; color: #4CAF50;'>✓</div>" +
        "<div>请求处理成功！</div>" +
        "</div>";
}, 3000);
```

### 10.10 表单验证确认对话框

```javascript
// 表单验证确认对话框
function validateAndConfirm() {
    // 模拟表单验证
    var formValid = true;
    var errorMessage = "";
    
    // 假设表单验证失败
    formValid = false;
    errorMessage = "请填写所有必填字段：<br> - 用户名<br> - 密码<br> - 邮箱";
    
    if (!formValid) {
        // 显示验证错误确认对话框
        ood.confirm(
            "表单验证失败",
            errorMessage,
            function() {
                console.log("用户选择继续编辑表单");
                // 聚焦到第一个错误字段
                document.getElementById("username").focus();
            },
            function() {
                console.log("用户选择取消表单提交");
                // 重置表单
                document.getElementById("myForm").reset();
            },
            "继续编辑",
            "取消提交"
        );
    } else {
        // 表单验证通过，显示提交确认
        ood.confirm(
            "确认提交",
            "表单验证通过，确定要提交表单吗？",
            function() {
                console.log("用户确认提交表单");
                // 提交表单
                document.getElementById("myForm").submit();
            },
            function() {
                console.log("用户取消提交表单");
            }
        );
    }
}

// 调用示例
// validateAndConfirm();
```

## 11. 最佳实践

### 11.1 数据设计

1. **明确的消息内容**：确认消息应清晰明确，说明操作的后果
2. **适当的标题**：标题应简洁明了，反映确认的操作类型
3. **合理的按钮文本**：按钮文本应明确表示操作结果
4. **适当的默认焦点**：通常将焦点设置在取消按钮上，避免用户误操作

### 11.2 性能优化

1. **避免频繁显示**：避免在短时间内显示多个确认对话框，影响用户体验
2. **合理使用缓存**：对于重复使用的确认对话框，可以使用缓存机制，减少创建成本
3. **及时清理资源**：确认对话框关闭后，及时清理相关资源
4. **优化动画效果**：使用平滑的过渡动画，避免过度动画影响性能

### 11.3 交互设计

1. **清晰的视觉反馈**：提供明确的按钮状态和焦点指示
2. **适当的动画效果**：使用平滑的显示和隐藏动画，提高用户体验
3. **支持键盘导航**：确保可以通过键盘完全操作确认对话框
4. **合理的默认设置**：根据业务需求设置合适的默认参数

### 11.4 无障碍访问

1. **添加ARIA属性**：确保Confirm及其子组件具有适当的ARIA属性
2. **支持键盘导航**：确保可以通过键盘完全操作Confirm
3. **清晰的视觉反馈**：提供明确的焦点状态和交互反馈
4. **语义化HTML**：使用语义化的HTML结构

## 12. 常见问题

### 12.1 确认对话框不显示

- **问题**：调用show()方法后，确认对话框不显示
- **解决方案**：
  1. 检查message属性是否正确设置
  2. 确保组件已经渲染到DOM中
  3. 检查modal属性是否设置为true
  4. 检查z-index是否足够高，避免被其他元素遮挡

### 12.2 确认对话框无法关闭

- **问题**：点击按钮或按Esc键，确认对话框无法关闭
- **解决方案**：
  1. 检查是否有事件处理函数阻止了默认行为
  2. 确保对话框的close方法正常工作
  3. 检查对话框的beforeClose事件处理函数是否返回false

### 12.3 主题切换不生效

- **问题**：调用setTheme方法后主题没有切换
- **解决方案**：
  1. 确保组件已经渲染到DOM中
  2. 检查主题样式是否正确加载
  3. 尝试手动触发主题更新：confirm.setTheme(confirm.getTheme());

### 12.4 回调函数不执行

- **问题**：点击确认或取消按钮，回调函数不执行
- **解决方案**：
  1. 检查回调函数是否正确传递
  2. 检查按钮的onClick事件处理函数是否正常工作
  3. 检查对话框的beforeClose事件处理函数是否正确调用回调函数

## 13. 版本兼容性

| 版本 | 新增功能 |
|------|----------|
| v1.0.0 | 基础Confirm功能 |
| v1.1.0 | 支持自定义按钮文本和回调函数 |
| v1.2.0 | 支持主题切换和响应式设计 |
| v1.3.0 | 增强无障碍访问支持 |
| v1.4.0 | 支持系统主题检测和键盘导航 |

## 14. 总结

Confirm 组件是 ooder-a2ui 框架中用于显示确认对话框的核心组件，通过灵活的配置和组合，可以实现各种复杂的确认对话框效果。它支持自定义标题、内容、按钮文本、主题切换和响应式设计，能够满足不同场景下的需求。

在实际应用中，Confirm 组件通常用于需要用户明确确认的操作场景，如删除数据、执行重要操作等。通过合理的配置和优化，可以创建出既美观又实用的确认对话框，提升用户体验。