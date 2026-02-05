# 移动端OA办公组件库（规范版本）

一套遵循OOD开发规范的移动端OA办公组件解决方案，包含通知公告、待办事项、审批流程、日程安排、通讯录、文件管理、会议管理和考勤打卡等常用办公功能组件。

## 设计理念

本组件库完全遵循OOD开发规范，具有以下特点：

1. **组件结构规范**：严格按照OOD.Class定义组件结构
2. **事件处理规范**：使用标准的事件处理器定义方式
3. **数据绑定规范**：支持与APICaller集成的数据交互
4. **设计器兼容**：支持在可视化设计器中使用
5. **动作关联机制**：支持标准的动作关联和回调处理

## 组件列表

### OA办公组件
1. **Notice** - 通知公告组件
2. **TodoList** - 待办事项组件
3. **NoticeDesigner** - 通知公告组件（设计器版本）
4. **APICallerExample** - APICaller集成示例

### 基础组件 (Basic)
- **Button** - 按钮组件，支持多种类型、尺寸、状态和触摸反馈
- **Input** - 输入框组件，支持验证、格式化、移动端键盘优化
- **List** - 列表组件，支持虚拟滚动、下拉刷新、滑动操作
- **DatePicker** - 日期选择器组件，支持多种日期格式和移动端优化
- **TimePicker** - 时间选择器组件，支持24小时制和12小时制

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

## 使用方法

### 1. 引入组件库

在HTML文件中引入组件库：

```html
<!-- 依赖的基础框架 -->
<script src="../../ood.js"></script>
<script src="../../mobile/index.js"></script>

<!-- OA办公组件库（规范版本） -->
<script src="./index.js"></script>
```

### 2. 创建组件实例

```javascript
// 创建通知公告组件
var notice = new (ood.Class.getInstance('ood.Mobile.OA.new.Notice'))({
    notices: [
        {
            id: '1',
            title: '关于五一假期安排的通知',
            time: '2025-04-25',
            summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
            important: true,
            read: false,
            disabled: false
        }
    ],
    onNoticeClick: function(profile, index, notice) {
        console.log('点击了通知:', notice);
    }
});

// 渲染到页面
notice.render(document.getElementById('notice-container'));
```

### 3. 使用APICaller集成

```javascript
// 创建APICaller示例组件
var apiCallerExample = new (ood.Class.getInstance('ood.Mobile.OA.new.APICallerExample'))({
    apiConfig: {
        url: '/api/notices',
        method: 'GET',
        proxyType: 'AJAX',
        requestType: 'JSON',
        responseType: 'JSON'
    },
    onLoad: function(data) {
        console.log('数据加载完成:', data);
    },
    onError: function(error) {
        console.log('数据加载错误:', error);
    }
});

apiCallerExample.render(document.getElementById('api-caller-container'));
```

## 组件详细说明

### Notice 通知公告组件

用于显示企业内部的通知、公告等信息，支持重要性标记和已读状态管理。

**主要功能：**
- 通知列表展示
- 重要性标记
- 已读/未读状态
- 点击事件处理
- 与APICaller集成

### TodoList 待办事项组件

用于显示和管理用户的待办任务，支持优先级设置和完成状态切换。

**主要功能：**
- 待办任务列表
- 优先级标记（高/中/低）
- 完成状态切换
- 添加/删除任务
- 与APICaller集成

### NoticeDesigner 通知公告组件（设计器版本）

专为可视化设计器设计的版本，支持在设计器中配置和使用。

**主要功能：**
- 完全兼容可视化设计器
- 支持APICaller配置
- 支持动作关联机制
- 事件处理标准化

### APICallerExample APICaller集成示例

演示如何在业务组件中正确使用APICaller进行数据交互。

**主要功能：**
- APICaller实例化和配置
- 数据加载、创建、删除操作
- 事件回调处理
- 动作关联机制

## 事件处理

所有组件都支持丰富的事件处理机制：

```javascript
var component = new (ood.Class.getInstance('ood.Mobile.OA.new.ComponentName'))({
    // 数据变更事件
    onDataLoad: function(profile, data) {
        console.log('数据加载完成:', data);
    },
    
    // 数据错误事件
    onDataError: function(profile, error) {
        console.log('数据加载错误:', error);
    },
    
    // 点击事件
    onClick: function(profile, index, item) {
        console.log('点击了项:', item);
    },
    
    // 状态变更事件
    onStatusChange: function(profile, index, item, status) {
        console.log('状态变更:', status, '在项:', item);
    }
});
```

## 动作关联机制

所有组件都支持标准的动作关联机制：

```javascript
var component = new (ood.Class.getInstance('ood.Mobile.OA.new.ComponentName'))({
    actions: {
        onLoad: {
            actions: [
                {
                    "desc": "执行关联动作",
                    "type": "function",
                    "target": "someFunction",
                    "args": ["{data}"]
                }
            ]
        },
        onError: {
            actions: [
                {
                    "desc": "执行错误处理",
                    "type": "function",
                    "target": "errorHandler",
                    "args": ["{error}"]
                }
            ]
        }
    }
});
```

## 样式定制

所有组件都使用CSS变量进行样式定制，可以通过修改以下变量来改变组件外观：

```css
:root {
    --mobile-primary: #007AFF;
    --mobile-success: #4CD964;
    --mobile-warning: #FF9500;
    --mobile-danger: #FF3B30;
    --mobile-bg-primary: #FFFFFF;
    --mobile-bg-secondary: #F2F2F7;
    --mobile-text-primary: #000000;
    --mobile-text-secondary: #8E8E93;
    --mobile-border-color: #C6C6C8;
}
```

## 浏览器兼容性

- Chrome 50+
- Safari 10+
- Firefox 45+
- Edge 12+

## 许可证

MIT License