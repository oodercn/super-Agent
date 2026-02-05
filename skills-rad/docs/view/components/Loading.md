# Loading 组件

## 1. 组件概述

Loading 组件是 ooder-a2ui 框架中的加载状态指示器，用于在数据加载、页面渲染、操作处理等场景中向用户提供视觉反馈。它支持多种加载状态展示方式，包括全局加载提示、局部加载提示和组件级加载状态。

### 1.1 核心功能

- **多种加载样式**：支持圆形旋转、脉冲动画、进度条等多种加载效果
- **不同层级的加载状态**：
  - 全局加载：覆盖整个页面
  - 局部加载：覆盖特定区域
  - 组件级加载：嵌入到组件内部
- **自定义文本和样式**：支持自定义加载文本、颜色、大小等
- **自动关闭和手动控制**：根据场景需求灵活控制加载状态
- **响应式设计**：适配不同屏幕尺寸

### 1.2 应用场景

- 页面初始化加载
- 异步数据请求
- 表单提交处理
- 文件上传下载
- 复杂操作处理
- 组件渲染延迟

## 2. 加载状态类型

### 2.1 基于 Toast 的加载提示

这是框架中最常用的加载提示方式，通过 Toast 组件的 loading 类型实现。

#### 2.1.1 创建方式

```javascript
// 快速创建加载提示
var loadingToast = ood.Mobile.Toast.loading('加载中...');

// 自定义加载提示
var customLoading = ood.Mobile.Toast.show('正在处理...', 'loading', 0, {
    position: 'center',
    mask: true,
    className: 'custom-loading'
});
```

#### 2.1.2 属性配置

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `message` | String | "加载中..." | 加载提示文本 |
| `type` | String | "loading" | 消息类型，必须为 "loading" |
| `duration` | Number | 0 | 显示时长，0 表示不自动关闭 |
| `position` | String | "center" | 显示位置：top, middle, bottom, center |
| `mask` | Boolean | false | 是否显示遮罩层 |
| `zIndex` | Number | 9999 | 层级 |
| `animation` | Boolean | true | 是否显示动画 |
| `className` | String | "" | 自定义类名 |

#### 2.1.3 方法列表

| 方法名 | 说明 | 参数 | 返回值 |
|--------|------|------|--------|
| `hide()` | 隐藏加载提示 | 无 | 当前实例 |
| `destroy()` | 销毁加载提示 | 无 | 无 |

### 2.2 页面级加载状态

页面级加载状态通常用于整个页面的初始化加载或重要操作处理。

#### 2.2.1 创建方式

```javascript
// 显示页面加载
ood.showLoading();

// 显示带文本的页面加载
ood.showLoading('页面加载中...');

// 隐藏页面加载
ood.hideLoading();
```

#### 2.2.2 配置选项

| 选项名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `text` | String | "加载中..." | 加载提示文本 |
| `background` | String | "rgba(255, 255, 255, 0.8)" | 背景颜色 |
| `color` | String | "#4CAF50" | 加载图标颜色 |
| `size` | Number | 40 | 加载图标大小（像素） |

### 2.3 组件级加载状态

组件级加载状态嵌入到特定组件内部，用于组件自身的数据加载或操作处理。

#### 2.3.1 创建方式

```javascript
// 在指定元素上显示加载状态
ood.showComponentLoading(element, {
    text: '数据加载中...',
    type: 'spinner'
});

// 隐藏组件加载状态
ood.hideComponentLoading(element);
```

#### 2.3.2 配置选项

| 选项名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `text` | String | "加载中..." | 加载提示文本 |
| `type` | String | "spinner" | 加载类型：spinner, pulse, bars |
| `position` | String | "center" | 加载图标位置：top, middle, bottom, center |
| `background` | String | "rgba(255, 255, 255, 0.9)" | 背景颜色 |
| `color` | String | "#2196F3" | 加载图标颜色 |
| `size` | Number | 30 | 加载图标大小（像素） |
| `overlay` | Boolean | true | 是否显示覆盖层 |

## 3. 使用示例

### 3.1 基本加载提示

```javascript
// 显示加载提示
var loading = ood.Mobile.Toast.loading('正在加载数据...');

// 模拟数据加载
setTimeout(function() {
    // 隐藏加载提示
    loading.hide();
    
    // 显示加载成功消息
    ood.Mobile.Toast.success('数据加载完成！');
}, 3000);
```

### 3.2 带遮罩的加载提示

```javascript
// 显示带遮罩的加载提示
var loading = ood.Mobile.Toast.show('正在处理请求...', 'loading', 0, {
    position: 'center',
    mask: true,
    className: 'mask-loading'
});

// 模拟请求处理
setTimeout(function() {
    loading.hide();
    ood.Mobile.Toast.success('请求处理成功！');
}, 2500);
```

### 3.3 页面级加载

```javascript
// 页面加载时显示
window.addEventListener('load', function() {
    ood.hideLoading();
});

// 页面开始加载时显示
ood.showLoading('页面初始化中...');
```

### 3.4 组件级加载

```javascript
// 获取目标元素
var container = document.getElementById('data-container');

// 显示组件加载状态
ood.showComponentLoading(container, {
    text: '加载中，请稍候...',
    type: 'pulse',
    color: '#FF9800'
});

// 模拟数据加载
fetch('/api/data')
    .then(response => response.json())
    .then(data => {
        // 处理数据
        renderData(data);
        // 隐藏加载状态
        ood.hideComponentLoading(container);
    })
    .catch(error => {
        ood.hideComponentLoading(container);
        ood.Mobile.Toast.error('数据加载失败：' + error.message);
    });
```

### 3.5 表单提交加载

```javascript
// 表单提交事件处理
document.getElementById('submit-form').addEventListener('submit', function(e) {
    e.preventDefault();
    
    // 显示加载提示
    var loading = ood.Mobile.Toast.loading('提交中...');
    
    // 模拟表单提交
    setTimeout(function() {
        loading.hide();
        ood.Mobile.Toast.success('表单提交成功！');
    }, 2000);
});
```

## 4. 自定义加载样式

### 4.1 自定义 CSS

```css
/* 自定义加载动画 */
.custom-loading .ood-toast-content {
    background-color: rgba(0, 0, 0, 0.8);
    color: #fff;
    border-radius: 8px;
    padding: 15px 20px;
}

.custom-loading .ood-toast-icon {
    border: 3px solid rgba(255, 255, 255, 0.3);
    border-top-color: #fff;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    to { transform: rotate(360deg); }
}
```

### 4.2 使用自定义加载图标

```javascript
// 使用自定义图标和文本
var customLoading = ood.Mobile.Toast.show(
    '<div style="display:flex;align-items:center;">' +
    '<div style="width:24px;height:24px;border:2px solid #4CAF50;border-top-color:transparent;border-radius:50%;animation:spin 1s linear infinite;margin-right:10px;"></div>' +
    '<span>自定义加载...</span>' +
    '</div>',
    'info', 0, {
        position: 'center',
        className: 'custom-icon-loading'
    }
);
```

## 5. 最佳实践

### 5.1 加载状态选择

1. **根据加载时长选择**：
   - 短时间加载（< 1秒）：可以不显示加载提示
   - 中等时间加载（1-3秒）：显示轻量级加载提示
   - 长时间加载（> 3秒）：显示详细的加载提示，包含取消选项

2. **根据加载范围选择**：
   - 全局操作：使用页面级加载或带遮罩的加载提示
   - 局部操作：使用组件级加载或局部加载提示
   - 异步请求：使用 Toast 加载提示

3. **根据用户体验选择**：
   - 关键操作：显示明确的加载状态
   - 次要操作：可以使用更 subtle 的加载提示
   - 后台操作：可以不显示加载提示，完成后显示结果通知

### 5.2 加载提示设计原则

1. **提供反馈**：让用户知道系统正在处理请求
2. **保持简洁**：加载提示文本应简洁明了
3. **提供取消选项**：对于长时间加载，应提供取消按钮
4. **避免过度使用**：不要在每个小操作上都显示加载提示
5. **保持一致性**：在整个应用中使用统一的加载样式
6. **考虑可访问性**：确保加载提示对辅助技术友好

### 5.3 性能优化

1. **延迟显示**：对于快速完成的操作，可以延迟 500ms 显示加载提示，避免闪烁
2. **合理使用遮罩**：只在必要时使用遮罩，避免影响用户体验
3. **优化加载速度**：从根本上优化代码和资源，减少加载时间
4. **缓存机制**：对频繁加载的数据使用缓存，减少重复请求

## 6. 浏览器兼容性

| 浏览器 | 支持版本 | 备注 |
|--------|----------|------|
| Chrome | 最新版 | 完全支持 |
| Firefox | 最新版 | 完全支持 |
| Safari | 最新版 | 完全支持 |
| Edge | 最新版 | 完全支持 |
| IE | 11+ | 部分动画效果可能受限 |

## 7. 注意事项

1. **避免嵌套加载**：不要在已经显示加载提示的情况下再显示另一个加载提示
2. **及时隐藏加载状态**：确保在操作完成后立即隐藏加载提示，避免无限加载
3. **处理错误情况**：在请求失败时，确保隐藏加载提示并显示错误信息
4. **考虑移动设备**：在移动设备上，加载提示应简洁，避免遮挡过多内容
5. **测试不同网络条件**：在慢速网络条件下测试加载提示的显示效果
6. **避免阻塞用户操作**：对于非关键操作，考虑使用非阻塞的加载提示

## 8. 扩展阅读

- [Toast 组件文档](Message.md#3-消息提示toast)
- [用户体验设计：加载状态](https://www.nngroup.com/articles/progress-indicators/)
- [性能优化：减少加载时间](https://developers.google.com/web/fundamentals/performance/)
- [可访问性：加载状态](https://www.w3.org/WAI/WCAG21/Understanding/status-messages.html)

Loading 组件是提升用户体验的重要组成部分，通过合理使用不同类型的加载状态，可以让用户清晰了解系统的工作状态，减少等待焦虑，提高应用的可用性和用户满意度。