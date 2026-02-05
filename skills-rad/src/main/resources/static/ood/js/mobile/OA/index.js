/**
 * 移动端OA办公组件库入口文件
 * 导出所有OA办公相关组件
 */

// 确保ood.Mobile.OA命名空间存在
if (typeof ood !== 'undefined' && ood.Mobile) {
    if (!ood.Mobile.OA) {
        ood.Mobile.OA = {};
    }
} else {
    console.warn('ood framework not ready, OA components may not work properly');
}

// 导入所有OA组件
// 通知公告组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
        // 这里只需要确保文件被加载
    }
} catch (e) {
    console.warn('Failed to load Notice component:', e);
}

// 待办事项组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load TodoList component:', e);
}

// 审批流程组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load ApprovalFlow component:', e);
}

// 日程安排组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load Schedule component:', e);
}

// 通讯录组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load ContactList component:', e);
}

// 文件管理组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load FileManager component:', e);
}

// 会议管理组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load MeetingManager component:', e);
}

// 考勤打卡组件
try {
    if (typeof ood !== 'undefined' && typeof ood.Mobile !== 'undefined') {
        // 组件会在各自文件中自动注册到ood.Class系统
    }
} catch (e) {
    console.warn('Failed to load Attendance component:', e);
}

// 导出组件列表
if (typeof ood !== 'undefined' && ood.Mobile && ood.Mobile.OA) {
    ood.Mobile.OA.Components = {
        Notice: 'ood.Mobile.OA.Notice',
        TodoList: 'ood.Mobile.OA.TodoList',
        ApprovalFlow: 'ood.Mobile.OA.ApprovalFlow',
        Schedule: 'ood.Mobile.OA.Schedule',
        ContactList: 'ood.Mobile.OA.ContactList',
        FileManager: 'ood.Mobile.OA.FileManager',
        MeetingManager: 'ood.Mobile.OA.MeetingManager',
        Attendance: 'ood.Mobile.OA.Attendance',
        // 设计器版本组件
        NoticeDesigner: 'ood.Mobile.OA.NoticeDesigner',
        NoticeDesignerAdvanced: 'ood.Mobile.OA.NoticeDesignerAdvanced',
        NoticeDesignerAdvancedUsage: 'ood.Mobile.OA.NoticeDesignerAdvancedUsage',
        NoticeDesignerStandard: 'ood.Mobile.OA.NoticeDesignerStandard',
        NoticeDesignerFinal: 'ood.Mobile.OA.NoticeDesignerFinal',
        NoticeDesignerAPICaller: 'ood.Mobile.OA.NoticeDesignerAPICaller',
        // 行业实例组件
        AttendanceSystem: 'ood.Mobile.OA.AttendanceSystem',
        TodoManagementSystem: 'ood.Mobile.OA.TodoManagementSystem',
        NoticeManagementSystem: 'ood.Mobile.OA.NoticeManagementSystem',
        OfficePlatform: 'ood.Mobile.OA.OfficePlatform'
    };
    
    // 组件库信息
    ood.Mobile.OA.Info = {
        name: '移动端OA办公组件库',
        version: '1.0.0',
        description: '一套完整的移动端OA办公组件解决方案',
        author: 'OOD Team',
        created: '2025-09-15'
    };
    
    // 组件使用说明
    ood.Mobile.OA.Usage = {
        /**
         * 创建通知公告组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.Notice} 通知公告组件实例
         */
        createNotice: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.Notice'))(config);
        },
        
        /**
         * 创建待办事项组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.TodoList} 待办事项组件实例
         */
        createTodoList: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.TodoList'))(config);
        },
        
        /**
         * 创建审批流程组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.ApprovalFlow} 审批流程组件实例
         */
        createApprovalFlow: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.ApprovalFlow'))(config);
        },
        
        /**
         * 创建日程安排组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.Schedule} 日程安排组件实例
         */
        createSchedule: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.Schedule'))(config);
        },
        
        /**
         * 创建通讯录组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.ContactList} 通讯录组件实例
         */
        createContactList: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.ContactList'))(config);
        },
        
        /**
         * 创建文件管理组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.FileManager} 文件管理组件实例
         */
        createFileManager: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.FileManager'))(config);
        },
        
        /**
         * 创建会议管理组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.MeetingManager} 会议管理组件实例
         */
        createMeetingManager: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.MeetingManager'))(config);
        },
        
        /**
         * 创建考勤打卡组件实例
         * @param {Object} config - 配置参数
         * @returns {ood.Mobile.OA.Attendance} 考勤打卡组件实例
         */
        createAttendance: function(config) {
            return new (ood.Class.getInstance('ood.Mobile.OA.Attendance'))(config);
        }
    };
}

// 组件库初始化完成事件
if (typeof ood !== 'undefined' && typeof CustomEvent !== 'undefined') {
    try {
        var event = new CustomEvent('ood-mobile-oa-ready', {
            detail: {
                components: typeof ood.Mobile.OA !== 'undefined' ? ood.Mobile.OA.Components : {},
                info: typeof ood.Mobile.OA !== 'undefined' ? ood.Mobile.OA.Info : {}
            }
        });
        if (typeof document !== 'undefined') {
            document.dispatchEvent(event);
        }
    } catch (e) {
        console.warn('Failed to dispatch ood-mobile-oa-ready event:', e);
    }
}

console.log('移动端OA办公组件库加载完成');