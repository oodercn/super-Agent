/**
 * OA办公组件使用示例
 * 演示如何在项目中使用OA办公组件
 */

// 确保DOM加载完成后再执行
document.addEventListener('DOMContentLoaded', function() {
    // 1. 通知公告组件使用示例
    function createNoticeComponent() {
        var notice = new (ood.Class.getInstance('ood.Mobile.OA.Notice'))({
            notices: [
                {
                    id: '1',
                    title: '系统维护通知',
                    time: '2025-09-15 14:30',
                    summary: '系统将于今晚00:00-02:00进行例行维护，期间服务可能中断，请提前做好数据保存工作。',
                    important: true,
                    read: false,
                    disabled: false
                },
                {
                    id: '2',
                    title: '新功能上线',
                    time: '2025-09-14 09:15',
                    summary: 'OA系统新增了在线会议功能，支持多人视频会议和屏幕共享。',
                    important: false,
                    read: true,
                    disabled: false
                }
            ],
            onNoticeClick: function(profile, index, notice) {
                console.log('点击了通知:', notice.title);
                // 标记为已读
                profile.boxing().markAsRead(index);
                alert('通知内容:\n' + notice.summary);
            }
        });
        
        // 渲染到指定容器
        notice.render(document.getElementById('notice-container'));
        return notice;
    }
    
    // 2. 待办事项组件使用示例
    function createTodoListComponent() {
        var todoList = new (ood.Class.getInstance('ood.Mobile.OA.TodoList'))({
            todos: [
                {
                    id: '1',
                    title: '完成季度报告',
                    dueDate: '2025-09-20',
                    priority: 'high',
                    completed: false,
                    disabled: false
                },
                {
                    id: '2',
                    title: '准备会议材料',
                    dueDate: '2025-09-18',
                    priority: 'medium',
                    completed: true,
                    disabled: false
                },
                {
                    id: '3',
                    title: '回复客户邮件',
                    dueDate: '2025-09-15',
                    priority: 'low',
                    completed: false,
                    disabled: false
                }
            ],
            onTodoClick: function(profile, index, todo) {
                console.log('点击了待办事项:', todo.title);
                alert('待办事项详情:\n' + todo.title + '\n截止日期: ' + todo.dueDate);
            },
            onTodoCompleteChange: function(profile, index, todo) {
                console.log('待办事项状态改变:', todo.title, '完成状态:', todo.completed);
            }
        });
        
        // 渲染到指定容器
        todoList.render(document.getElementById('todo-container'));
        return todoList;
    }
    
    // 3. 审批流程组件使用示例
    function createApprovalFlowComponent() {
        var approvalFlow = new (ood.Class.getInstance('ood.Mobile.OA.ApprovalFlow'))({
            approvals: [
                {
                    id: '1',
                    title: '请假申请',
                    applicant: '张三',
                    applyTime: '2025-09-14 09:30',
                    type: '请假',
                    status: 'pending',
                    urgent: false,
                    disabled: false
                },
                {
                    id: '2',
                    title: '出差申请',
                    applicant: '李四',
                    applyTime: '2025-09-13 14:20',
                    type: '出差',
                    status: 'approved',
                    urgent: true,
                    disabled: false
                }
            ],
            onApprovalClick: function(profile, index, approval) {
                console.log('点击了审批:', approval.title);
                alert('审批详情:\n' + approval.title + '\n申请人: ' + approval.applicant);
            },
            onActionClick: function(profile, index, approval, action) {
                console.log('执行了审批操作:', action, '审批:', approval.title);
                if (action === 'approve') {
                    alert('已同意审批: ' + approval.title);
                } else if (action === 'reject') {
                    alert('已拒绝审批: ' + approval.title);
                }
            }
        });
        
        // 渲染到指定容器
        approvalFlow.render(document.getElementById('approval-container'));
        return approvalFlow;
    }
    
    // 4. 日程安排组件使用示例
    function createScheduleComponent() {
        var schedule = new (ood.Class.getInstance('ood.Mobile.OA.Schedule'))({
            schedules: [
                {
                    id: '1',
                    date: '2025-09-15',
                    time: '09:00-10:00',
                    title: '部门周会',
                    location: '会议室A',
                    participants: ['张三', '李四', '王五'],
                    important: true,
                    disabled: false
                },
                {
                    id: '2',
                    date: '2025-09-15',
                    time: '14:00-15:30',
                    title: '项目评审',
                    location: '会议室B',
                    participants: ['赵六', '钱七'],
                    important: false,
                    disabled: false
                }
            ],
            onScheduleClick: function(profile, index, schedule) {
                console.log('点击了日程:', schedule.title);
                alert('日程详情:\n' + schedule.title + '\n时间: ' + schedule.time + '\n地点: ' + schedule.location);
            }
        });
        
        // 渲染到指定容器
        schedule.render(document.getElementById('schedule-container'));
        return schedule;
    }
    
    // 5. 通讯录组件使用示例
    function createContactListComponent() {
        var contactList = new (ood.Class.getInstance('ood.Mobile.OA.ContactList'))({
            contacts: [
                {
                    id: '1',
                    name: '张三',
                    position: '产品经理',
                    department: '产品部',
                    avatar: '',
                    phone: '13800138001',
                    email: 'zhangsan@company.com',
                    disabled: false
                },
                {
                    id: '2',
                    name: '李四',
                    position: '前端工程师',
                    department: '技术部',
                    avatar: '',
                    phone: '13800138002',
                    email: 'lisi@company.com',
                    disabled: false
                }
            ],
            onContactClick: function(profile, index, contact) {
                console.log('点击了联系人:', contact.name);
                alert('联系人信息:\n' + contact.name + '\n职位: ' + contact.position + '\n电话: ' + contact.phone);
            }
        });
        
        // 渲染到指定容器
        contactList.render(document.getElementById('contact-container'));
        return contactList;
    }

    // 6. 通知公告组件（设计器版本）使用示例
    function createNoticeDesignerComponent() {
        var noticeDesigner = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesigner'))({
            // 可以在这里设置组件属性
            width: '100%',
            height: 'auto'
        });
        
        // 渲染到指定容器
        noticeDesigner.render(document.getElementById('notice-designer-container'));
        return noticeDesigner;
    }

    // 7. 通知公告组件（高级版）使用示例
    function createNoticeDesignerAdvancedComponent() {
        var noticeDesignerAdvanced = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesignerAdvanced'))({
            // 可以在这里设置组件属性
            width: '100%',
            height: 'auto',
            actions: {
                onClick: [
                    {
                        "desc": "处理通知点击",
                        "type": "event",
                        "target": "onNoticeClick",
                        "args": ["{args[0]}", "{args[1]}"]
                    }
                ],
                onLoad: [
                    {
                        "desc": "数据加载完成处理",
                        "type": "event",
                        "target": "onDataLoad",
                        "args": ["{args[0]}"]
                    }
                ],
                onError: [
                    {
                        "desc": "数据加载错误处理",
                        "type": "event",
                        "target": "onDataError",
                        "args": ["{args[0]}"]
                    }
                ]
            }
        });
        
        // 渲染到指定容器
        noticeDesignerAdvanced.render(document.getElementById('notice-designer-advanced-container'));
        return noticeDesignerAdvanced;
    }

    // 8. 通知公告组件（使用示例版本）使用示例
    function createNoticeDesignerAdvancedUsageComponent() {
        var noticeDesignerAdvancedUsage = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesignerAdvancedUsage'))({
            // 可以在这里设置组件属性
            width: '100%',
            height: 'auto'
        });
        
        // 渲染到指定容器
        noticeDesignerAdvancedUsage.render(document.getElementById('notice-designer-advanced-usage-container'));
        return noticeDesignerAdvancedUsage;
    }

    // 初始化所有组件
    function initAllComponents() {
        // 创建各个组件实例
        createNoticeComponent();
        createTodoListComponent();
        createApprovalFlowComponent();
        createScheduleComponent();
        createContactListComponent();
        // 创建设计器版本组件实例
        createNoticeDesignerComponent();
        // 创建高级版组件实例
        createNoticeDesignerAdvancedComponent();
        // 创建使用示例版本组件实例
        createNoticeDesignerAdvancedUsageComponent();
    }

    // 在OOD框架准备就绪后初始化组件
    if (typeof ood !== 'undefined' && ood.isReady) {
        initAllComponents();
    } else {
        document.addEventListener('ood-ready', initAllComponents);
    }
});

// ==================== 行业实例组件使用示例 ====================

/**
 * 创建考勤管理系统实例
 */
function createAttendanceSystem() {
    var attendanceSystem = new (ood.Class.getInstance('ood.Mobile.OA.AttendanceSystem'))({
        userInfo: {
            id: 'EMP001',
            name: '张三',
            department: '技术部'
        },
        onPunch: function(profile, type, time, record) {
            console.log('打卡成功:', type === 'in' ? '上班' : '下班', '时间:', time);
            alert((type === 'in' ? '上班' : '下班') + '打卡成功: ' + time);
        }
    });
    
    // 渲染到指定容器
    attendanceSystem.render(document.getElementById('attendance-system-container'));
    return attendanceSystem;
}

/**
 * 创建待办事项管理系统实例
 */
function createTodoManagementSystem() {
    var todoSystem = new (ood.Class.getInstance('ood.Mobile.OA.TodoManagementSystem'))({
        todoItems: [
            {
                id: 'todo_1',
                title: '完成季度报告',
                dueDate: '2025-09-20',
                priority: 'high',
                completed: false,
                disabled: false
            },
            {
                id: 'todo_2',
                title: '准备会议材料',
                dueDate: '2025-09-18',
                priority: 'medium',
                completed: true,
                disabled: false
            }
        ],
        onTodoClick: function(profile, index, todo) {
            console.log('点击了待办事项:', todo.title);
            alert('待办事项详情:\n' + todo.title + '\n截止日期: ' + todo.dueDate);
        },
        onTodoCompleteChange: function(profile, index, todo) {
            console.log('待办事项状态改变:', todo.title, '完成状态:', todo.completed);
        }
    });
    
    // 渲染到指定容器
    todoSystem.render(document.getElementById('todo-system-container'));
    return todoSystem;
}

/**
 * 创建通知公告管理系统实例
 */
function createNoticeManagementSystem() {
    var noticeSystem = new (ood.Class.getInstance('ood.Mobile.OA.NoticeManagementSystem'))({
        notices: [
            {
                id: '1',
                title: '关于五一假期安排的通知',
                time: '2025-04-25 09:30',
                summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                important: true,
                read: false,
                disabled: false
            },
            {
                id: '2',
                title: '新员工入职培训安排',
                time: '2025-04-20 14:15',
                summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                important: false,
                read: true,
                disabled: false
            }
        ],
        currentUser: {
            id: 'USER001',
            name: '李四',
            role: '管理员'
        },
        onNoticeClick: function(profile, index, notice) {
            console.log('点击了通知:', notice.title);
            alert('通知内容:\n' + notice.summary);
        }
    });
    
    // 渲染到指定容器
    noticeSystem.render(document.getElementById('notice-system-container'));
    return noticeSystem;
}

/**
 * 创建综合办公平台实例
 */
function createOfficePlatform() {
    var officePlatform = new (ood.Class.getInstance('ood.Mobile.OA.OfficePlatform'))({
        currentUser: {
            id: 'EMP001',
            name: '张三',
            department: '技术部',
            role: '员工'
        }
    });
    
    // 渲染到指定容器
    officePlatform.render(document.getElementById('office-platform-container'));
    return officePlatform;
}

// 组件库使用工具函数
var OAOUtils = {
    /**
     * 创建通知公告组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createNotice: function(config, container) {
        var notice = new (ood.Class.getInstance('ood.Mobile.OA.Notice'))(config);
        if (container) {
            notice.render(container);
        }
        return notice;
    },
    
    /**
     * 创建待办事项组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createTodoList: function(config, container) {
        var todoList = new (ood.Class.getInstance('ood.Mobile.OA.TodoList'))(config);
        if (container) {
            todoList.render(container);
        }
        return todoList;
    },
    
    /**
     * 创建审批流程组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createApprovalFlow: function(config, container) {
        var approvalFlow = new (ood.Class.getInstance('ood.Mobile.OA.ApprovalFlow'))(config);
        if (container) {
            approvalFlow.render(container);
        }
        return approvalFlow;
    },
    
    /**
     * 创建日程安排组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createSchedule: function(config, container) {
        var schedule = new (ood.Class.getInstance('ood.Mobile.OA.Schedule'))(config);
        if (container) {
            schedule.render(container);
        }
        return schedule;
    },
    
    /**
     * 创建通讯录组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createContactList: function(config, container) {
        var contactList = new (ood.Class.getInstance('ood.Mobile.OA.ContactList'))(config);
        if (container) {
            contactList.render(container);
        }
        return contactList;
    },
    
    /**
     * 创建文件管理组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createFileManager: function(config, container) {
        var fileManager = new (ood.Class.getInstance('ood.Mobile.OA.FileManager'))(config);
        if (container) {
            fileManager.render(container);
        }
        return fileManager;
    },
    
    /**
     * 创建会议管理组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createMeetingManager: function(config, container) {
        var meetingManager = new (ood.Class.getInstance('ood.Mobile.OA.MeetingManager'))(config);
        if (container) {
            meetingManager.render(container);
        }
        return meetingManager;
    },
    
    /**
     * 创建考勤打卡组件
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createAttendance: function(config, container) {
        var attendance = new (ood.Class.getInstance('ood.Mobile.OA.Attendance'))(config);
        if (container) {
            attendance.render(container);
        }
        return attendance;
    },
    
    /**
     * 创建通知公告组件（设计器版本）
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createNoticeDesigner: function(config, container) {
        var noticeDesigner = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesigner'))(config);
        if (container) {
            noticeDesigner.render(container);
        }
        return noticeDesigner;
    },
    
    /**
     * 创建通知公告组件（高级版）
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createNoticeDesignerAdvanced: function(config, container) {
        var noticeDesignerAdvanced = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesignerAdvanced'))(config);
        if (container) {
            noticeDesignerAdvanced.render(container);
        }
        return noticeDesignerAdvanced;
    },
    
    /**
     * 创建通知公告组件（使用示例版本）
     * @param {Object} config - 配置参数
     * @param {Element} container - 容器元素
     * @returns {Object} 组件实例
     */
    createNoticeDesignerAdvancedUsage: function(config, container) {
        var noticeDesignerAdvancedUsage = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesignerAdvancedUsage'))(config);
        if (container) {
            noticeDesignerAdvancedUsage.render(container);
        }
        return noticeDesignerAdvancedUsage;
    }
};

/**
 * 创建通知公告组件（使用示例版本）
 * @param {Object} config - 配置参数
 * @param {Element} container - 容器元素
 * @returns {Object} 组件实例
 */
function createNoticeDesignerAdvancedUsage(config, container) {
    var noticeDesignerAdvancedUsage = new (ood.Class.getInstance('ood.Mobile.OA.NoticeDesignerAdvancedUsage'))(config);
    if (container) {
        noticeDesignerAdvancedUsage.render(container);
    }
    return noticeDesignerAdvancedUsage;
}
