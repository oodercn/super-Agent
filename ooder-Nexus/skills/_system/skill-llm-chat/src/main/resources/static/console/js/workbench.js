/**
 * 工作台核心逻辑
 * 管理场景-待办聚合展示
 */

class Workbench {
    constructor() {
        this.userId = null;
        this.userInfo = null;
        this.workbenchData = null;
        this.currentFilter = '';
        this.selectedTodo = null;
        
        this.init();
    }

    async init() {
        console.log('[Workbench] 初始化工作台...');
        
        try {
            await this.waitForPageInit();
            
            this.userInfo = window.NexusMenu ? NexusMenu.getCurrentUser() : null;
            this.userId = this.userInfo?.userId || 'default-user';
            
            console.log('[Workbench] 用户ID:', this.userId);
            
            this.updateUserInfo();
            this.bindEvents();
            await this.loadWorkbenchData();
            
            console.log('[Workbench] 工作台初始化完成');
        } catch (error) {
            console.error('[Workbench] 初始化失败:', error);
            this.loadMockData();
        }
    }

    waitForPageInit() {
        return new Promise((resolve) => {
            if (window.NexusMenu && NexusMenu.initialized) {
                resolve();
            } else {
                const checkInterval = setInterval(() => {
                    if (window.NexusMenu && NexusMenu.initialized) {
                        clearInterval(checkInterval);
                        resolve();
                    }
                }, 100);
                
                // 超时处理
                setTimeout(() => {
                    clearInterval(checkInterval);
                    resolve();
                }, 5000);
            }
        });
    }

    updateUserInfo() {
        const userInfoEl = document.getElementById('userInfo');
        if (userInfoEl && this.userInfo) {
            userInfoEl.querySelector('.user-name').textContent = this.userInfo.name || this.userInfo.userId;
        }
    }

    bindEvents() {
        // 刷新按钮
        document.getElementById('refreshBtn')?.addEventListener('click', () => {
            this.loadWorkbenchData();
        });

        // 场景状态过滤
        document.getElementById('sceneStatusFilter')?.addEventListener('change', (e) => {
            this.currentFilter = e.target.value;
            this.loadSceneTodoGroups();
        });

        // 新建场景按钮
        document.getElementById('createSceneBtn')?.addEventListener('click', () => {
            window.location.href = '/console/pages/scene-group-management.html?action=create';
        });

        // 通知按钮
        document.getElementById('notificationBtn')?.addEventListener('click', () => {
            window.location.href = '/console/pages/message-center.html';
        });

        // 弹窗关闭
        document.getElementById('modalClose')?.addEventListener('click', () => {
            this.closeTodoModal();
        });

        // 待办操作按钮
        document.getElementById('todoAcceptBtn')?.addEventListener('click', () => {
            this.handleTodoAction('accept');
        });

        document.getElementById('todoRejectBtn')?.addEventListener('click', () => {
            this.handleTodoAction('reject');
        });

        // 点击弹窗外部关闭
        document.getElementById('todoModal')?.addEventListener('click', (e) => {
            if (e.target.id === 'todoModal') {
                this.closeTodoModal();
            }
        });
    }

    async loadWorkbenchData() {
        try {
            this.showLoading();
            
            // 获取工作台数据
            this.workbenchData = await apiService.getWorkbenchData(this.userId);
            
            // 更新统计卡片
            this.updateStatistics();
            
            // 渲染场景-待办分组
            this.renderSceneTodoGroups();
            
            // 渲染快捷操作
            this.renderQuickActions();
            
            // 渲染全局待办
            this.renderGlobalTodos();
            
            // 渲染最近动态
            this.renderRecentActivity();
            
            // 更新通知角标
            this.updateNotificationBadge();
            
        } catch (error) {
            console.error('[Workbench] 加载数据失败:', error);
            this.showError('加载工作台数据失败');
        } finally {
            this.hideLoading();
        }
    }

    async loadSceneTodoGroups() {
        try {
            const groups = await apiService.getSceneTodoGroups(this.userId, this.currentFilter);
            this.workbenchData.sceneTodoGroups = groups;
            this.renderSceneTodoGroups();
        } catch (error) {
            console.error('[Workbench] 加载场景待办分组失败:', error);
        }
    }

    updateStatistics() {
        const stats = this.workbenchData?.statistics;
        if (!stats) return;

        document.getElementById('activeSceneCount').textContent = stats.activeSceneCount || 0;
        document.getElementById('pendingTodoCount').textContent = stats.pendingTodoCount || 0;
        document.getElementById('highPriorityCount').textContent = stats.highPriorityTodoCount || 0;
        document.getElementById('dueTodayCount').textContent = stats.dueTodayCount || 0;
    }

    renderSceneTodoGroups() {
        const container = document.getElementById('sceneTodoList');
        if (!container) return;

        const groups = this.workbenchData?.sceneTodoGroups || [];

        if (groups.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="ri-folders-line"></i>
                    <p>暂无场景</p>
                    <span>点击右上角"新建场景"创建第一个场景</span>
                </div>
            `;
            return;
        }

        container.innerHTML = groups.map(group => this.renderSceneTodoGroup(group)).join('');

        // 绑定待办操作事件
        container.querySelectorAll('.todo-action-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                const todoId = btn.dataset.todoId;
                const action = btn.dataset.action;
                this.handleTodoAction(action, todoId);
            });
        });

        // 绑定场景点击事件
        container.querySelectorAll('.scene-header').forEach(header => {
            header.addEventListener('click', () => {
                const sceneId = header.dataset.sceneId;
                window.location.href = `/console/pages/scene-detail.html?id=${sceneId}`;
            });
        });
    }

    renderSceneTodoGroup(group) {
        const statusClass = this.getStatusClass(group.sceneStatus);
        const statusText = this.getStatusText(group.sceneStatus);

        return `
            <div class="scene-todo-group">
                <div class="scene-header" data-scene-id="${group.sceneGroupId}">
                    <div class="scene-info">
                        <div class="scene-icon">
                            <i class="ri-folder-line"></i>
                        </div>
                        <div class="scene-details">
                            <h4 class="scene-name">${this.escapeHtml(group.sceneName)}</h4>
                            <div class="scene-meta">
                                <span class="scene-status ${statusClass}">${statusText}</span>
                                <span class="scene-role">${group.myRole || '成员'}</span>
                                ${group.memberCount ? `<span class="scene-members"><i class="ri-team-line"></i> ${group.memberCount}</span>` : ''}
                            </div>
                        </div>
                    </div>
                    <div class="scene-stats">
                        ${group.pendingCount > 0 ? `<span class="stat-badge warning">${group.pendingCount} 待办</span>` : ''}
                        ${group.highPriorityCount > 0 ? `<span class="stat-badge danger">${group.highPriorityCount} 高优</span>` : ''}
                    </div>
                    <i class="ri-arrow-right-s-line scene-arrow"></i>
                </div>
                ${group.todos && group.todos.length > 0 ? `
                    <div class="scene-todos">
                        ${group.todos.slice(0, 3).map(todo => this.renderTodoItem(todo, group.sceneGroupId)).join('')}
                        ${group.todos.length > 3 ? `
                            <div class="more-todos">
                                <a href="/console/pages/scene-detail.html?id=${group.sceneGroupId}&tab=todos">
                                    还有 ${group.todos.length - 3} 个待办...
                                </a>
                            </div>
                        ` : ''}
                    </div>
                ` : ''}
            </div>
        `;
    }

    renderTodoItem(todo, sceneGroupId) {
        const priorityClass = (todo.priority || 'normal').toLowerCase();
        const isPending = todo.status === 'pending';

        return `
            <div class="todo-item ${!isPending ? 'completed' : ''}" data-todo-id="${todo.id}">
                <div class="todo-checkbox ${!isPending ? 'checked' : ''}" data-todo-id="${todo.id}">
                    <i class="ri-check-line"></i>
                </div>
                <div class="todo-content">
                    <div class="todo-title">${this.escapeHtml(todo.title)}</div>
                    ${todo.description ? `<div class="todo-desc">${this.escapeHtml(todo.description)}</div>` : ''}
                    <div class="todo-meta">
                        <span class="todo-priority ${priorityClass}">${this.getPriorityLabel(todo.priority)}</span>
                        ${todo.dueTime ? `<span class="todo-due ${this.isOverdue(todo.dueTime) ? 'overdue' : ''}">
                            <i class="ri-time-line"></i> ${this.formatDueTime(todo.dueTime)}
                        </span>` : ''}
                    </div>
                </div>
                ${isPending ? `
                    <div class="todo-actions">
                        <button class="todo-action-btn" data-todo-id="${todo.id}" data-action="complete" title="完成">
                            <i class="ri-check-line"></i>
                        </button>
                    </div>
                ` : ''}
            </div>
        `;
    }

    renderQuickActions() {
        const container = document.getElementById('quickActions');
        if (!container) return;

        const actions = this.workbenchData?.quickActions || [];

        if (actions.length === 0) {
            container.innerHTML = '<p class="no-actions">暂无快捷操作</p>';
            return;
        }

        container.innerHTML = actions.map(action => `
            <a href="${action.url}" class="quick-action-item ${action.priority === 1 ? 'urgent' : ''}">
                <div class="action-icon">
                    <i class="${action.icon || 'ri-arrow-right-circle-line'}"></i>
                </div>
                <div class="action-info">
                    <span class="action-name">${this.escapeHtml(action.title || action.name)}</span>
                    ${action.sceneGroupId ? '<span class="action-type">场景</span>' : ''}
                </div>
            </a>
        `).join('');
    }

    renderGlobalTodos() {
        const container = document.getElementById('globalTodoList');
        if (!container) return;

        const todos = this.workbenchData?.globalTodos || this.workbenchData?.pendingTodos || [];

        if (todos.length === 0) {
            container.innerHTML = `
                <div class="empty-state small">
                    <i class="ri-task-line"></i>
                    <p>暂无待办</p>
                </div>
            `;
            return;
        }

        const displayTodos = todos.slice(0, 5);

        container.innerHTML = displayTodos.map(todo => `
            <div class="global-todo-item" data-todo-id="${todo.id}">
                <div class="todo-dot ${(todo.priority || 'normal').toLowerCase()}"></div>
                <div class="todo-info">
                    <div class="todo-title">${this.escapeHtml(todo.title)}</div>
                    <div class="todo-scene">${this.escapeHtml(todo.sceneName || todo.sceneGroupName || '未分类')}</div>
                </div>
                <button class="todo-quick-complete" data-todo-id="${todo.id}" title="完成">
                    <i class="ri-check-line"></i>
                </button>
            </div>
        `).join('');

        container.querySelectorAll('.todo-quick-complete').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                const todoId = btn.dataset.todoId;
                this.handleTodoAction('complete', todoId);
            });
        });
    }

    renderRecentActivity() {
        const container = document.getElementById('activityList');
        if (!container) return;

        const activities = this.workbenchData?.recentActivity || [
            { type: 'todo', text: '张三创建了待办 "审批候选人简历"', time: '10分钟前' },
            { type: 'scene', text: '李四加入了 "招聘场景"', time: '30分钟前' },
            { type: 'system', text: '系统提醒 "面试安排待确认"', time: '1小时前' }
        ];

        container.innerHTML = activities.map(activity => `
            <div class="activity-item ${activity.type}">
                <div class="activity-icon">
                    <i class="ri-${activity.type === 'todo' ? 'task' : activity.type === 'scene' ? 'folder' : 'information'}-line"></i>
                </div>
                <div class="activity-content">
                    <div class="activity-message">${this.escapeHtml(activity.text || activity.message)}</div>
                    <div class="activity-time">${activity.time}</div>
                </div>
            </div>
        `).join('');
    }

    updateNotificationBadge() {
        const badge = document.getElementById('notificationBadge');
        if (!badge) return;

        const total = (this.workbenchData?.statistics?.pendingTodoCount || 0) + 
                      (this.workbenchData?.statistics?.pendingApprovalCount || 0);

        if (total > 0) {
            badge.textContent = total > 99 ? '99+' : total;
            badge.style.display = 'flex';
        } else {
            badge.style.display = 'none';
        }
    }

    async handleTodoAction(action, todoId) {
        if (!todoId && this.selectedTodo) {
            todoId = this.selectedTodo.id;
        }

        if (!todoId) {
            console.error('[Workbench] 未指定待办ID');
            return;
        }

        try {
            const result = await apiService.processTodo(this.userId, todoId, action);
            
            if (result) {
                this.showSuccess(`待办${action === 'accept' ? '已接受' : action === 'reject' ? '已拒绝' : '已完成'}`);
                this.closeTodoModal();
                // 刷新数据
                await this.loadWorkbenchData();
            } else {
                this.showError('操作失败');
            }
        } catch (error) {
            console.error('[Workbench] 处理待办失败:', error);
            this.showError('操作失败: ' + error.message);
        }
    }

    openTodoModal(todo) {
        this.selectedTodo = todo;
        const modal = document.getElementById('todoModal');
        const detail = document.getElementById('todoDetail');

        if (!modal || !detail) return;

        detail.innerHTML = `
            <div class="todo-detail-item">
                <label>标题</label>
                <div class="detail-value">${this.escapeHtml(todo.title)}</div>
            </div>
            ${todo.description ? `
                <div class="todo-detail-item">
                    <label>描述</label>
                    <div class="detail-value">${this.escapeHtml(todo.description)}</div>
                </div>
            ` : ''}
            <div class="todo-detail-item">
                <label>场景</label>
                <div class="detail-value">${this.escapeHtml(todo.sceneGroupName || '未分类')}</div>
            </div>
            <div class="todo-detail-item">
                <label>优先级</label>
                <div class="detail-value">${this.getPriorityLabel(todo.priority)}</div>
            </div>
            ${todo.dueTime ? `
                <div class="todo-detail-item">
                    <label>截止时间</label>
                    <div class="detail-value ${this.isOverdue(todo.dueTime) ? 'overdue' : ''}">
                        ${new Date(todo.dueTime).toLocaleString('zh-CN')}
                    </div>
                </div>
            ` : ''}
        `;

        modal.style.display = 'flex';
    }

    closeTodoModal() {
        const modal = document.getElementById('todoModal');
        if (modal) {
            modal.style.display = 'none';
        }
        this.selectedTodo = null;
    }

    // ==================== 工具方法 ====================

    getStatusClass(status) {
        const statusMap = {
            'ACTIVE': 'active',
            'RUNNING': 'active',
            'PENDING': 'pending',
            'DEACTIVATED': 'inactive',
            'ARCHIVED': 'archived'
        };
        return statusMap[status] || 'default';
    }

    getStatusText(status) {
        const statusMap = {
            'ACTIVE': '进行中',
            'RUNNING': '运行中',
            'PENDING': '待激活',
            'DEACTIVATED': '已停用',
            'ARCHIVED': '已归档'
        };
        return statusMap[status] || status;
    }

    getPriorityLabel(priority) {
        const labels = {
            'HIGH': '高',
            'MEDIUM': '中',
            'LOW': '低',
            'high': '高',
            'medium': '中',
            'low': '低'
        };
        return labels[priority] || '普通';
    }

    isOverdue(dueTime) {
        return dueTime && new Date(dueTime) < new Date();
    }

    formatDueTime(dueTime) {
        if (!dueTime) return '';
        const date = new Date(dueTime);
        const now = new Date();
        const diff = date - now;

        if (diff < 0) {
            return '已过期';
        } else if (diff < 3600000) {
            return Math.floor(diff / 60000) + '分钟后';
        } else if (diff < 86400000) {
            return Math.floor(diff / 3600000) + '小时后';
        } else {
            return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' });
        }
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    showLoading() {
        // 可以添加全局loading效果
    }

    hideLoading() {
        // 隐藏全局loading效果
    }

    showError(message) {
        // 可以使用toast或其他方式显示错误
        console.error('[Workbench Error]', message);
        alert(message);
    }

    showSuccess(message) {
        console.log('[Workbench Success]', message);
    }

    loadMockData() {
        console.log('[Workbench] 加载模拟数据...');
        
        this.workbenchData = {
            statistics: {
                activeSceneCount: 3,
                pendingTodoCount: 5,
                highPriorityTodoCount: 2,
                dueTodayCount: 1
            },
            sceneTodoGroups: [
                {
                    sceneGroupId: 'sg-001',
                    sceneName: '招聘流程',
                    sceneStatus: 'ACTIVE',
                    myRole: '管理员',
                    memberCount: 4,
                    pendingCount: 3,
                    highPriorityCount: 1,
                    todos: [
                        { id: 't1', title: '审批候选人简历', priority: 'HIGH', status: 'pending', dueTime: new Date(Date.now() + 3600000).toISOString() },
                        { id: 't2', title: '安排面试时间', priority: 'MEDIUM', status: 'pending', dueTime: new Date(Date.now() + 86400000).toISOString() },
                        { id: 't3', title: '发送面试邀请', priority: 'LOW', status: 'pending' }
                    ]
                },
                {
                    sceneGroupId: 'sg-002',
                    sceneName: '项目协作',
                    sceneStatus: 'ACTIVE',
                    myRole: '成员',
                    memberCount: 6,
                    pendingCount: 2,
                    highPriorityCount: 1,
                    todos: [
                        { id: 't4', title: '提交周报', priority: 'HIGH', status: 'pending', dueTime: new Date(Date.now() + 7200000).toISOString() },
                        { id: 't5', title: '代码评审', priority: 'MEDIUM', status: 'pending' }
                    ]
                }
            ],
            quickActions: [
                { id: 'qa1', title: '新建场景', url: './scene-group-management.html?action=create', icon: 'ri-add-circle-line' },
                { id: 'qa2', title: '我的待办', url: './my-todos.html', icon: 'ri-task-line' },
                { id: 'qa3', title: '消息中心', url: './message-center.html', icon: 'ri-message-3-line' },
                { id: 'qa4', title: '技能市场', url: './market.html', icon: 'ri-store-2-line' }
            ],
            globalTodos: [
                { id: 'gt1', title: '审批候选人简历', sceneName: '招聘流程', priority: 'HIGH' },
                { id: 'gt2', title: '提交周报', sceneName: '项目协作', priority: 'HIGH' },
                { id: 'gt3', title: '安排面试时间', sceneName: '招聘流程', priority: 'MEDIUM' }
            ],
            recentActivity: [
                { id: 'ra1', text: '场景"招聘流程"已激活', time: '10分钟前', type: 'scene' },
                { id: 'ra2', text: '待办"代码评审"已完成', time: '1小时前', type: 'todo' },
                { id: 'ra3', text: '新成员加入场景', time: '2小时前', type: 'member' }
            ]
        };
        
        this.updateStatistics();
        this.renderSceneTodoGroups();
        this.renderQuickActions();
        this.renderGlobalTodos();
        this.renderRecentActivity();
    }
}

// 初始化工作台
document.addEventListener('DOMContentLoaded', () => {
    if (document.querySelector('.workbench-container')) {
        window.workbench = new Workbench();
    }
});
