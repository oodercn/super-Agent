# Upload 组件

## 1. 组件概述
Upload 是 ooder-a2ui 框架中的文件上传组件，用于实现文件的上传、管理和预览功能。该组件基于 iframe 实现，集成了现代化的文件上传界面，支持多文件上传、拖放上传、进度显示等功能。

## 2. 组件特性
- 支持多文件上传
- 支持拖放上传
- 显示上传进度
- 支持文件预览
- 支持主题切换（浅色、深色、高对比度）
- 响应式设计，适配不同屏幕尺寸
- 增强的可访问性支持
- 支持自定义上传参数
- 支持上传事件回调

## 3. 组件创建

### 3.1 JSON格式创建
```json
{
  "type": "Upload",
  "id": "upload1",
  "properties": {
    "width": "40em",
    "height": "30em",
    "uploadUrl": "upload/",
    "theme": "light",
    "responsive": true
  }
}
```

### 3.2 JavaScript格式创建
```javascript
var upload = ood.create("ood.UI.FileUpload")
    .setId("upload1")
    .setWidth("40em")
    .setHeight("30em")
    .setUploadUrl("upload/")
    .setTheme("light")
    .setResponsive(true);
```

## 4. 组件属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| theme | String | "light" | 主题样式，可选值：light, dark, high-contrast |
| responsive | Boolean | true | 是否启用响应式布局 |
| selectable | Boolean | true | 是否可选择 |
| width | String/Number | "40em" | 组件宽度 |
| height | String/Number | "30em" | 组件高度 |
| src | String | "/plugins/fileupload/uploadgrid.html" | 上传界面URL |
| prepareFormData | Boolean | true | 是否准备表单数据 |
| uploadUrl | String | "upload/" | 文件上传URL |
| params | Object | {} | 上传参数 |

## 5. 组件方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| setQueryData(data, path) | data: Object - 查询数据, path: String - 路径 | void | 设置查询数据 |
| reload(profile) | profile: Object - 组件配置文件 | String | 重新加载上传组件 |
| setTheme(theme) | theme: String - 主题名称 | this | 设置组件主题 |
| getTheme() | 无 | String | 获取当前主题 |
| toggleTheme() | 无 | this | 切换主题 |
| adjustLayout() | 无 | this | 调整响应式布局 |
| enhanceAccessibility() | 无 | this | 增强可访问性支持 |
| FileUploadTrigger() | 无 | void | 文件上传触发器，初始化主题和响应式设计 |

## 6. 事件处理

| 事件名 | 触发条件 | 回调参数 |
|--------|----------|----------|
| uploadfile | 文件开始上传时 | profile: Object - 组件配置, eventType: String - 事件类型, item: Object - 文件项, response: Object - 响应数据 |
| uploadfail | 文件上传失败时 | profile: Object - 组件配置, eventType: String - 事件类型, item: Object - 文件项, response: Object - 响应数据 |
| uploadcomplete | 文件上传完成时 | profile: Object - 组件配置, eventType: String - 事件类型, item: Object - 文件项, response: Object - 响应数据 |
| uploadprogress | 文件上传进度更新时 | profile: Object - 组件配置, eventType: String - 事件类型, item: Object - 文件项, response: Object - 响应数据 |

## 7. 组件示例

### 7.1 基本文件上传
```json
{
  "type": "Upload",
  "id": "basicUpload",
  "properties": {
    "width": "50em",
    "height": "40em",
    "uploadUrl": "/api/upload",
    "theme": "light"
  }
}
```

### 7.2 深色主题上传
```javascript
var darkUpload = ood.create("ood.UI.FileUpload")
    .setId("darkUpload")
    .setWidth("45em")
    .setHeight("35em")
    .setUploadUrl("/files/upload")
    .setTheme("dark")
    .setResponsive(true);
```

### 7.3 自定义上传参数
```json
{
  "type": "Upload",
  "id": "customUpload",
  "properties": {
    "width": "40em",
    "height": "30em",
    "uploadUrl": "/upload",
    "params": {
      "folder": "documents",
      "allowedTypes": "pdf,doc,docx",
      "maxSize": "5MB"
    }
  }
}
```

### 7.4 监听上传事件
```javascript
var eventUpload = ood.create("ood.UI.FileUpload")
    .setId("eventUpload")
    .setWidth("40em")
    .setHeight("30em")
    .setUploadUrl("/api/upload");

// 添加自定义事件处理
ood.merge(eventUpload.properties.events, {
    uploadfile: function(profile, eventType, item, response) {
        console.log("文件开始上传:", item.name);
    },
    uploadprogress: function(profile, eventType, item, response) {
        console.log("上传进度:", item.name, response.progress + "%");
    },
    uploadcomplete: function(profile, eventType, item, response) {
        console.log("文件上传完成:", item.name, response.url);
    },
    uploadfail: function(profile, eventType, item, response) {
        console.error("文件上传失败:", item.name, response.error);
    }
});
```

## 8. 主题设置

Upload 组件支持三种主题：

### 8.1 浅色主题（light）
默认主题，适合大多数场景使用。

### 8.2 深色主题（dark）
适合在深色背景下使用，减少视觉疲劳。

### 8.3 高对比度主题（high-contrast）
适合视力障碍用户，提高文本和元素的对比度。

## 9. 响应式设计

Upload 组件支持响应式设计，会根据屏幕尺寸自动调整布局：

- **大屏幕（≥768px）**：完整显示上传界面
- **小屏幕（<768px）**：优化移动端布局，调整按钮大小和间距
- **超小屏幕（<480px）**：进一步调整布局，确保在小屏幕设备上可用

## 10. 可访问性支持

Upload 组件增强了可访问性支持：

- 添加了适当的 ARIA 属性
- 支持键盘导航
- 提供清晰的视觉反馈
- 支持屏幕阅读器

## 11. 最佳实践

1. **上传URL配置**：
   - 确保上传URL正确配置，指向服务器端的上传处理接口
   - 考虑跨域问题，确保服务器端支持跨域请求

2. **文件类型限制**：
   - 在服务器端设置允许上传的文件类型
   - 在客户端提供清晰的文件类型指引

3. **文件大小限制**：
   - 合理设置文件大小限制，避免过大文件占用服务器资源
   - 在客户端提供文件大小限制提示

4. **主题选择**：
   - 根据应用的整体风格选择合适的主题
   - 考虑用户的使用环境和偏好

5. **事件处理**：
   - 合理使用上传事件，提供良好的用户反馈
   - 处理上传失败情况，提供清晰的错误信息

6. **响应式设计**：
   - 确保在不同屏幕尺寸下都有良好的用户体验
   - 考虑移动设备的使用场景

7. **性能优化**：
   - 避免同时上传过多文件
   - 考虑分片上传大文件
   - 优化服务器端上传处理

## 12. 浏览器兼容性

Upload 组件支持所有现代浏览器：

- Chrome 20+
- Firefox 15+
- Safari 6+
- Internet Explorer 10+
- Edge 12+

## 13. 常见问题

### 13.1 上传失败
- 检查上传URL是否正确
- 检查服务器端是否支持跨域请求
- 检查文件大小和类型是否符合服务器限制
- 查看浏览器控制台的错误信息

### 13.2 主题切换不生效
- 确保主题名称正确（light, dark, high-contrast）
- 检查组件是否正确初始化
- 尝试手动调用 setTheme 方法

### 13.3 响应式布局不生效
- 确保 responsive 属性设置为 true
- 检查浏览器窗口大小是否符合响应式断点
- 尝试手动调用 adjustLayout 方法

### 13.4 事件不触发
- 确保事件名称正确
- 检查事件处理函数是否正确绑定
- 查看浏览器控制台是否有错误信息

## 14. 总结

Upload 组件是一个功能强大的文件上传组件，提供了现代化的上传界面和丰富的功能。它支持多文件上传、拖放上传、进度显示、主题切换和响应式设计等特性，适合各种文件上传场景。

通过合理配置和使用 Upload 组件，可以为用户提供良好的文件上传体验，同时简化开发工作。组件的主题切换和响应式设计使其能够适应不同的使用环境和设备，增强的可访问性支持提高了组件的可用性。

Upload 组件的应用范围包括但不限于：
- 文档管理系统
- 媒体库管理
- 表单文件上传
- 云存储服务
- 内容管理系统

作为 ooder-a2ui 框架的重要组成部分，Upload 组件为 Web 应用提供了可靠的文件上传解决方案。