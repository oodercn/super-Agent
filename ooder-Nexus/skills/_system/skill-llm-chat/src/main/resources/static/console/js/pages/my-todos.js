let todos = [];
let currentTab = 'pending';

const TODO_TYPES = {
    INVITATION: 'invitation',
    DELEGATION: 'delegation',
    REMINDER: 'reminder',
    APPROVAL: 'approval',
    ACTIVATION: 'activation',
    SCENE_NOTIFICATION: 'scene-notification'
};

document.addEventListener('DOMContentLoaded', async function() {
    await initDicts();
    refreshAll();
});

async function initDicts() {
    if (typeof DictCache !== 'undefined') {
        await DictCache.init();
    }
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'});
}

function formatDeadline(timestamp) {
    if (!timestamp) return '';
    const now = Date.now();
    const diff = timestamp - now;
    const hours = Math.floor(diff / 3600000);
    
    if (diff < 0) {
        return '<span class="deadline-urgent">已过期</span>';
    } else if (hours < 2) {
        return '<span class="deadline-urgent">剩余 ' + hours + ' 小时</span>';
    } else if (hours < 24) {
        return '<span class="deadline-soon">剩余 ' + hours + ' 小时</span>';
    } else {
        const days = Math.floor(hours / 24);
        return '剩余 ' + days + ' 天';
    }
}

async function refreshAll() {
    try {
        var result = await ApiClient.get('/api/v1/my/todos?pageNum=1&pageSize=50');
        
        if (result.status === 'success' && result.data) {
            todos = result.data.list || result.data || [];
            renderTodos();
            updateStats();
        } else {
            console.error('API returned error:', result.message);
            todos = [];
            renderTodos();
            updateStats();
        }
    } catch (error) {
        console.error('Failed to load todos:', error);
        todos = [];
        renderTodos();
        updateStats();
    }
}



function switchTab(tab) {
    currentTab = tab;
    
    document.querySelectorAll('.todo-tab').forEach(t => t.classList.remove('active'));
    event.target.closest('.todo-tab').classList.add('active');
    
    renderTodos();
}

function renderTodos() {
    let filteredTodos = todos;
    
    if (currentTab === 'pending') {
        filteredTodos = todos.filter(t => t.status === 'pending');
    } else if (currentTab === 'completed') {
        filteredTodos = todos.filter(t => t.status === 'completed');
    }
    
    const invitations = filteredTodos.filter(t => t.type === TODO_TYPES.INVITATION);
    const delegations = filteredTodos.filter(t => t.type === TODO_TYPES.DELEGATION);
    const reminders = filteredTodos.filter(t => t.type === TODO_TYPES.REMINDER);
    const approvals = filteredTodos.filter(t => t.type === TODO_TYPES.APPROVAL);
    const activations = filteredTodos.filter(t => t.type === TODO_TYPES.ACTIVATION);
    const sceneNotifications = filteredTodos.filter(t => t.type === TODO_TYPES.SCENE_NOTIFICATION);
    
    renderTodoList('invitationList', invitations, 'invitation');
    renderTodoList('delegationList', delegations, 'delegation');
    renderTodoList('reminderList', reminders, 'reminder');
    renderTodoList('approvalList', approvals, 'approval');
    renderTodoList('activationList', activations, 'activation');
    renderTodoList('sceneNotificationList', sceneNotifications, 'scene-notification');
    
    document.getElementById('invitationSection').style.display = invitations.length ? 'block' : 'none';
    document.getElementById('delegationSection').style.display = delegations.length ? 'block' : 'none';
    document.getElementById('reminderSection').style.display = reminders.length ? 'block' : 'none';
    document.getElementById('approvalSection').style.display = approvals.length ? 'block' : 'none';
    document.getElementById('activationSection').style.display = activations.length ? 'block' : 'none';
    document.getElementById('sceneNotificationSection').style.display = sceneNotifications.length ? 'block' : 'none';
}

function renderTodoList(containerId, items, type) {
    const container = document.getElementById(containerId);
    
    if (!items.length) {
        container.innerHTML = '<div class="empty-state"><i class="ri-inbox-line"></i><p>暂无待办</p></div>';
        return;
    }
    
    let html = '';
    items.forEach(item => {
        const typeIcon = getTypeIcon(type);
        const typeText = getTypeText(type);
        
        html += `
            <div class="todo-card ${type}">
                <div class="todo-card-header">
                    <div class="todo-card-type">
                        ${typeIcon}
                        ${typeText}
                    </div>
                    <span class="nx-text-sm nx-text-secondary">${formatTime(item.createTime)}</span>
                </div>
                <div class="todo-card-title">${item.title}</div>
                <div class="todo-card-meta">
                    ${item.fromUser ? `<span><i class="ri-user-line"></i> 来源: ${item.fromUser}</span>` : ''}
                    ${item.sceneGroupName ? `<span><i class="ri-artboard-line"></i> ${item.sceneGroupName}</span>` : ''}
                    ${item.deadline ? `<span><i class="ri-time-line"></i> ${formatDeadline(item.deadline)}</span>` : ''}
                    ${item.role ? `<span><i class="ri-user-star-line"></i> 角色: ${getRoleName(item.role)}</span>` : ''}
                </div>
                ${item.status === 'pending' ? renderPendingActions(item) : renderCompletedActions(item)}
            </div>
        `;
    });
    
    container.innerHTML = html;
}

function getTypeIcon(type) {
    const icons = {
        [TODO_TYPES.INVITATION]: '<i class="ri-user-add-line" style="color: var(--nx-info);"></i>',
        [TODO_TYPES.DELEGATION]: '<i class="ri-user-star-line" style="color: var(--nx-warning);"></i>',
        [TODO_TYPES.REMINDER]: '<i class="ri-alarm-line" style="color: var(--nx-success);"></i>',
        [TODO_TYPES.APPROVAL]: '<i class="ri-checkbox-circle-line" style="color: var(--nx-error);"></i>',
        [TODO_TYPES.ACTIVATION]: '<i class="ri-key-2-line" style="color: #8b5cf6;"></i>',
        [TODO_TYPES.SCENE_NOTIFICATION]: '<i class="ri-notification-3-line" style="color: #06b6d4;"></i>'
    };
    return icons[type] || '<i class="ri-notification-3-line"></i>';
}

function getTypeText(type) {
    const texts = {
        [TODO_TYPES.INVITATION]: '协作邀请',
        [TODO_TYPES.DELEGATION]: '领导委派',
        [TODO_TYPES.REMINDER]: '待办提醒',
        [TODO_TYPES.APPROVAL]: '审批请求',
        [TODO_TYPES.ACTIVATION]: '待激活能力',
        [TODO_TYPES.SCENE_NOTIFICATION]: '场景通知'
    };
    return texts[type] || '待办';
}

function getRoleName(role) {
    const roles = {
        'manager': '管理者',
        'employee': '员工',
        'hr': 'HR',
        'llm-assistant': 'LLM助手',
        'coordinator': '协调Agent',
        'super-agent': '超级Agent'
    };
    return roles[role] || role;
}

function renderPendingActions(item) {
    switch (item.type) {
        case TODO_TYPES.INVITATION:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="acceptTodo('${item.id}')">
                        <i class="ri-check-line"></i> 接受
                    </button>
                    <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="rejectTodo('${item.id}')">
                        <i class="ri-close-line"></i> 拒绝
                    </button>
                </div>
            `;
        case TODO_TYPES.DELEGATION:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="startProcess('${item.id}', '${item.sceneGroupId}')">
                        <i class="ri-play-line"></i> 开始处理
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewScene('${item.sceneGroupId}')">
                        <i class="ri-eye-line"></i> 查看详情
                    </button>
                </div>
            `;
        case TODO_TYPES.REMINDER:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="completeTodo('${item.id}')">
                        <i class="ri-check-line"></i> 立即处理
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="snoozeTodo('${item.id}')">
                        <i class="ri-time-line"></i> 稍后提醒
                    </button>
                </div>
            `;
        case TODO_TYPES.APPROVAL:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--success nx-btn--sm" onclick="approveTodo('${item.id}')">
                        <i class="ri-check-line"></i> 批准
                    </button>
                    <button class="nx-btn nx-btn--error nx-btn--sm" onclick="rejectTodo('${item.id}')">
                        <i class="ri-close-line"></i> 驳回
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewTodo('${item.id}')">
                        <i class="ri-eye-line"></i> 查看
                    </button>
                </div>
            `;
        case TODO_TYPES.ACTIVATION:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="goToActivation('${item.installId}')">
                        <i class="ri-key-2-line"></i> 前往激活
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewCapability('${item.capabilityId}')">
                        <i class="ri-eye-line"></i> 查看能力
                    </button>
                </div>
            `;
        case TODO_TYPES.SCENE_NOTIFICATION:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="viewScene('${item.sceneGroupId}')">
                        <i class="ri-eye-line"></i> 查看场景
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="completeTodo('${item.id}')">
                        <i class="ri-check-line"></i> 已知晓
                    </button>
                </div>
            `;
        default:
            return `
                <div class="todo-card-actions">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="completeTodo('${item.id}')">
                        <i class="ri-check-line"></i> 完成
                    </button>
                </div>
            `;
    }
}

function renderCompletedActions(item) {
    return `
        <div class="todo-card-actions">
            <span class="nx-text-sm nx-text-secondary">
                <i class="ri-checkbox-circle-line" style="color: var(--nx-success);"></i>
                已于 ${formatTime(item.completedTime)} 处理
            </span>
        </div>
    `;
}

function updateStats() {
    const pending = todos.filter(t => t.status === 'pending');
    const completed = todos.filter(t => t.status === 'completed');
    
    document.getElementById('pendingCount').textContent = pending.length;
    document.getElementById('completedCount').textContent = completed.length;
    document.getElementById('invitationCount').textContent = pending.filter(t => t.type === TODO_TYPES.INVITATION).length;
    document.getElementById('delegationCount').textContent = pending.filter(t => t.type === TODO_TYPES.DELEGATION).length;
    
    document.getElementById('pendingTabCount').textContent = pending.length;
    document.getElementById('completedTabCount').textContent = completed.length;
    document.getElementById('allTabCount').textContent = todos.length;
}

async function acceptTodo(todoId) {
    try {
        var result = await ApiClient.post('/api/v1/my/todos/' + todoId + '/accept');
        
        if (result.status === 'success') {
            var todo = todos.find(t => t.id === todoId);
            if (todo) {
                todo.status = 'completed';
                todo.completedTime = Date.now();
            }
            renderTodos();
            updateStats();
        } else {
            alert('操作失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to accept todo:', error);
        alert('操作失败: ' + error.message);
    }
}

async function rejectTodo(todoId) {
    if (!confirm('确定要拒绝吗？')) return;
    
    try {
        var result = await ApiClient.post('/api/v1/my/todos/' + todoId + '/reject');
        
        if (result.status === 'success') {
            var todo = todos.find(t => t.id === todoId);
            if (todo) {
                todo.status = 'completed';
                todo.completedTime = Date.now();
            }
            renderTodos();
            updateStats();
        } else {
            alert('操作失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to reject todo:', error);
        alert('操作失败: ' + error.message);
    }
}

async function completeTodo(todoId) {
    try {
        var result = await ApiClient.post('/api/v1/my/todos/' + todoId + '/complete');
        
        if (result.status === 'success') {
            var todo = todos.find(t => t.id === todoId);
            if (todo) {
                todo.status = 'completed';
                todo.completedTime = Date.now();
            }
            renderTodos();
            updateStats();
        } else {
            alert('操作失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to complete todo:', error);
        alert('操作失败: ' + error.message);
    }
}

async function approveTodo(todoId) {
    try {
        var result = await ApiClient.post('/api/v1/my/todos/' + todoId + '/approve');
        
        if (result.status === 'success') {
            var todo = todos.find(t => t.id === todoId);
            if (todo) {
                todo.status = 'completed';
                todo.completedTime = Date.now();
            }
            renderTodos();
            updateStats();
        } else {
            alert('操作失败: ' + result.message);
        }
    } catch (error) {
        console.error('Failed to approve todo:', error);
        var todo = todos.find(t => t.id === todoId);
        if (todo) {
            todo.status = 'completed';
            todo.completedTime = Date.now();
        }
        renderTodos();
        updateStats();
    }
}

function snoozeTodo(todoId) {
    alert('将在1小时后再次提醒');
}

function startProcess(todoId, sceneGroupId) {
    window.location.href = '/console/pages/scene-group-detail.html?id=' + sceneGroupId;
}

function viewScene(sceneGroupId) {
    window.location.href = '/console/pages/scene-group-detail.html?id=' + sceneGroupId;
}

function viewTodo(todoId) {
    const todo = todos.find(t => t.id === todoId);
    if (todo && todo.sceneGroupId) {
        viewScene(todo.sceneGroupId);
    }
}

function goToActivation(installId) {
    window.location.href = '/console/pages/capability-activation.html?installId=' + installId;
}

function viewCapability(capabilityId) {
    window.location.href = '/console/pages/my-capabilities.html?capabilityId=' + capabilityId;
}
