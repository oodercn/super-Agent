# 移动端OA办公组件库

一套完整的移动端OA办公组件解决方案，包含通知公告、待办事项、审批流程、日程安排、通讯录、文件管理、会议管理和考勤打卡等常用办公功能组件。

## 组件列表

1. **Notice** - 通知公告组件
2. **TodoList** - 待办事项组件
3. **ApprovalFlow** - 审批流程组件
4. **Schedule** - 日程安排组件
5. **ContactList** - 通讯录组件
6. **FileManager** - 文件管理组件
7. **MeetingManager** - 会议管理组件
8. **Attendance** - 考勤打卡组件

## 使用方法

### 1. 引入组件库

在HTML文件中引入组件库：

```html
<!-- 依赖的基础框架 -->
<script src="../../ood.js"></script>
<script src="../../mobile/index.js"></script>

<!-- OA办公组件库 -->
<script src="./index.js"></script>
```

### 2. 创建组件实例

```javascript
// 创建通知公告组件
var notice = new (ood.Class.getInstance('ood.Mobile.OA.Notice'))({
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

### 3. 使用快捷创建方法

```javascript
// 使用快捷方法创建组件
var todoList = ood.Mobile.OA.Usage.createTodoList({
    todos: [
        {
            id: '1',
            title: '完成季度报告',
            dueDate: '2025-09-20',
            priority: 'high',
            completed: false,
            disabled: false
        }
    ]
});

todoList.render(document.getElementById('todo-container'));
```

### 4. 响应式支持

所有组件都支持响应式调整，通过[_onresize](file:///e:/ooder-gitee/ooder-pro/src/main/resources/static/ood/js/pc/Form/Editor.js#L267-L277)方法实现：

```javascript
// 组件会自动响应容器尺寸变化
var component = new (ood.Class.getInstance('ood.Mobile.OA.ComponentName'))(config);
component.render(container);

// 也可以手动触发尺寸调整
component.get(0).boxing()._onresize(component.get(0), '300px', '400px');
```

## 组件详细说明

### Notice 通知公告组件

用于显示企业内部的通知、公告等信息，支持重要性标记和已读状态管理。

**主要功能：**
- 通知列表展示
- 重要性标记
- 已读/未读状态
- 点击事件处理
- 响应式调整支持

### TodoList 待办事项组件

用于显示和管理用户的待办任务，支持优先级设置和完成状态切换。

**主要功能：**
- 待办任务列表
- 优先级标记（高/中/低）
- 完成状态切换
- 添加/删除任务
- 响应式调整支持

### ApprovalFlow 审批流程组件

用于显示和处理审批流程，支持多种审批状态和操作。

**主要功能：**
- 审批列表展示
- 审批状态管理（待审批/已同意/已拒绝/已撤销）
- 审批操作（同意/拒绝）
- 紧急程度标记
- 响应式调整支持

### Schedule 日程安排组件

用于显示和管理用户的日程安排，支持按日期分组显示。

**主要功能：**
- 日程列表展示
- 按日期分组
- 时间/地点/参与人信息
- 重要性标记
- 响应式调整支持

### ContactList 通讯录组件

用于显示和管理员工通讯录信息，支持搜索和字母索引。

**主要功能：**
- 联系人列表
- 搜索功能
- 字母索引导航
- 头像显示
- 响应式调整支持

### FileManager 文件管理组件

用于显示和管理企业文件，支持多种文件类型和操作。

**主要功能：**
- 文件列表展示
- 文件类型图标
- 文件操作（下载/分享/删除）
- 搜索功能
- 多选操作
- 响应式调整支持

### MeetingManager 会议管理组件

用于显示和管理企业会议安排，支持会议状态管理。

**主要功能：**
- 会议列表展示
- 会议状态管理（已安排/进行中/已完成/已取消）
- 会议操作（加入/取消）
- 视图切换（今天/本周/本月）
- 响应式调整支持

### Attendance 考勤打卡组件

用于员工上下班打卡和考勤记录查看。

**主要功能：**
- 上下班打卡
- 考勤统计
- 考勤记录查看
- 打卡状态显示
- 响应式调整支持

## 事件处理

所有组件都支持丰富的事件处理机制：

```javascript
var component = new (ood.Class.getInstance('ood.Mobile.OA.ComponentName'))({
    // 数据变更事件
    onDataChange: function(profile, data) {
        console.log('数据变更:', data);
    },
    
    // 点击事件
    onClick: function(profile, index, item) {
        console.log('点击了项:', item);
    },
    
    // 操作事件
    onAction: function(profile, index, item, action) {
        console.log('执行了操作:', action, '在项:', item);
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