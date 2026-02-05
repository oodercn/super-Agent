# ood.js 移动端组件库

基于ood.js框架的移动端UI组件库，参考阿里易搭低代码移动组件库设计，采用四分离设计模式（样式、模板、行为、数据分离），为移动端应用提供丰富的UI组件支持。

## 🚀 特性

- **继承规范**: 所有组件都继承自 `ood.UI`，符合ood框架规范
- **四分离设计**: 样式(Appearances)、模板(Templates)、行为(Behaviors)、数据(DataModel)分离
- **移动端优化**: 支持触摸交互、手势操作、响应式布局
- **现代化设计**: 采用CSS变量、Flexbox、Grid等现代CSS特性
- **可访问性**: 支持ARIA属性、键盘导航、屏幕阅读器
- **主题支持**: 内置亮色/暗色主题，支持自定义主题
- **TypeScript友好**: 完整的类型定义支持

## 📦 组件列表

### 基础组件 (Basic)
- **Button** - 按钮组件，支持多种类型、尺寸、状态和触摸反馈
- **Input** - 输入框组件，支持验证、格式化、移动端键盘优化
- **List** - 列表组件，支持虚拟滚动、下拉刷新、滑动操作

### 布局组件 (Layout)
- **Grid** - 网格布局组件，支持响应式布局和间距控制
- **Layout** - 布局组件，支持Flex布局和安全区域适配
- **Panel** - 面板组件，支持标题、边框、阴影配置

### 表单组件 (Form)
- **Form** - 表单组件，支持表单验证和数据收集
- **Switch** - 开关组件，支持触摸交互和动画效果
- **Picker** - 选择器组件，支持单选、多选、级联选择

### 导航组件 (Navigation)
- **NavBar** - 导航栏组件，支持标题、左右操作按钮、状态栏适配
- **TabBar** - 标签栏组件，支持多标签切换、徽章显示、自定义图标
- **Drawer** - 抽屉组件，支持左右滑出、遮罩层、手势控制

### 反馈组件 (Feedback)
- **Toast** - 消息提示组件，支持多种类型提示、自动消失、位置配置
- **Modal** - 模态框组件，支持自定义内容、操作按钮、动画效果
- **ActionSheet** - 操作面板组件，支持底部弹出、操作列表、取消按钮

### 数据展示组件 (Display)
- **Card** - 卡片组件，支持标题、内容、操作按钮、图片等多种布局
- **Badge** - 徽章组件，支持数字徽章、点徽章、状态徽章等多种类型
- **Avatar** - 头像组件，支持图片头像、文字头像、图标头像、在线状态

## 🏗️ 架构设计

### 组件继承关系
```
ood.UI (基础UI类)
├── ood.Mobile.Base (移动端基础类)
    ├── ood.Mobile.Button
    ├── ood.Mobile.Input
    ├── ood.Mobile.List
    ├── ood.Mobile.Grid
    ├── ood.Mobile.Layout
    ├── ood.Mobile.Panel
    ├── ood.Mobile.Form
    ├── ood.Mobile.Switch
    └── ood.Mobile.Picker
```

### 核心方法实现

每个组件都包含以下核心方法：

#### Instance 方法
- `Initialize()` - 组件初始化
- `initMobileFeatures()` - 移动端特性初始化
- `bindTouchEvents()` - 触摸事件绑定
- `initAccessibility()` - 可访问性初始化

#### Static 配置
- `Templates` - 模板定义
- `Appearances` - 样式定义
- `Behaviors` - 行为定义
- `DataModel` - 数据模型
- `RenderTrigger` - 渲染触发器
- `EventHandlers` - 事件处理器

## 📱 移动端特性

### 触摸交互
- 触摸反馈效果
- 长按手势支持
- 滑动操作
- 触觉反馈

### 响应式设计
- 屏幕尺寸自适应
- 安全区域适配
- 横竖屏切换

### 性能优化
- 虚拟滚动
- 防抖节流
- 懒加载
- 动画优化

## 🎨 主题系统

### CSS变量定义
```css
:root {
  /* 颜色主题 */
  --mobile-primary: #007AFF;
  --mobile-secondary: #5856D6;
  --mobile-success: #34C759;
  --mobile-warning: #FF9500;
  --mobile-danger: #FF3B30;
  
  /* 间距系统 */
  --mobile-spacing-xs: 4px;
  --mobile-spacing-sm: 8px;
  --mobile-spacing-md: 16px;
  --mobile-spacing-lg: 24px;
  --mobile-spacing-xl: 32px;
  
  /* 字体大小 */
  --mobile-font-xs: 10px;
  --mobile-font-sm: 12px;
  --mobile-font-md: 14px;
  --mobile-font-lg: 16px;
  --mobile-font-xl: 18px;
}
```

### 主题切换
```javascript
// 设置主题
component.setMobileTheme('dark');

// 获取主题
var theme = component.getMobileTheme();
```

## 💻 使用示例

### 创建按钮
```javascript
var button = new ood.Mobile.Button({
    text: '点击我',
    type: 'primary',
    size: 'lg',
    ripple: true,
    onButtonClick: function(profile, event) {
        console.log('按钮被点击');
    }
});
```

### 创建输入框
```javascript
var input = new ood.Mobile.Input({
    type: 'email',
    placeholder: '请输入邮箱',
    required: true,
    onInputChange: function(profile, event, value) {
        console.log('输入值：', value);
    }
});
```

### 创建列表
```javascript
var list = new ood.Mobile.List({
    data: [
        { title: '项目1', subtitle: '描述1' },
        { title: '项目2', subtitle: '描述2' }
    ],
    pullRefresh: true,
    onItemClick: function(profile, index, data, event) {
        console.log('点击了项目：', data);
    }
});
```

## 🔧 开发指南

### 组件开发规范

1. **继承关系**: 所有移动端组件必须继承自 `ood.Mobile.Base`
2. **命名规范**: 组件类名格式为 `ood.Mobile.ComponentName`
3. **方法实现**: 必须实现 `Initialize()` 和相关生命周期方法
4. **事件绑定**: 在 `bindTouchEvents()` 中处理触摸事件
5. **样式定义**: 在 `Appearances` 中定义组件样式
6. **数据模型**: 在 `DataModel` 中定义属性和行为

### 自定义组件示例
```javascript
ood.Class("ood.Mobile.CustomComponent", "ood.Mobile.Base", {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initCustomFeatures();
        },
        
        initCustomFeatures: function() {
            // 自定义功能初始化
        },
        
        bindTouchEvents: function() {
            // 触摸事件处理
        }
    },
    
    Static: {
        Templates: {
            // 模板定义
        },
        
        Appearances: {
            // 样式定义
        },
        
        DataModel: {
            // 数据模型
        }
    }
});
```

## 🌐 浏览器支持

- iOS Safari 10+
- Android Chrome 60+
- Android WebView 60+
- 微信小程序
- 支付宝小程序

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来完善组件库。

## 📞 联系我们

如有问题或建议，请联系开发团队。