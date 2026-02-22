/**
 * 数据抽取任务管理页面 JavaScript
 */

let tasks = [];

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    initMenu();
    loadTasks();
    loadStats();
});

/**
 * 加载任务列表
 */
async function loadTasks() {
    try {
        const response = await fetch('/api/tasks');
        const result = await response.json();
        
        if (result.code === 200) {
            tasks = result.data || [];
            renderTasks();
        } else {
            nexusCommon.showMessage('加载任务列表失败: ' + result.message, 'error');
        }
    } catch (error) {
        console.error('加载任务列表失败:', error);
        nexusCommon.showMessage('加载任务列表失败', 'error');
    }
}

/**
 * 加载统计信息
 */
async function loadStats() {
    try {
        const response = await fetch('/api/tasks/stats');
        const result = await response.json();
        
        if (result.code === 200) {
            const stats = result.data;
            document.getElementById('totalCount').textContent = stats.totalCount || 0;
            document.getElementById('enabledCount').textContent = stats.enabledCount || 0;
            document.getElementById('runningCount').textContent = stats.runningCount || 0;
            document.getElementById('completedCount').textContent = stats.completedCount || 0;
            document.getElementById('failedCount').textContent = stats.failedCount || 0;
        }
    } catch (error) {
        console.error('加载统计信息失败:', error);
    }
}

/**
 * 渲染任务列表
 */
function renderTasks() {
    const container = document.getElementById('taskList');
    
    if (tasks.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="ri-database-2-line"></i>
                <p>暂无数据抽取任务</p>
                <p style="font-size: 12px; margin-top: 8px;">点击"新建任务"按钮创建第一个任务</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = tasks.map(task => `
        <div class="task-item">
            <div class="task-info">
                <div class="task-name">
                    <span class="task-type-badge">${getTaskTypeLabel(task.type)}</span>
                    ${task.name}
                </div>
                <div class="task-meta">
                    ${task.description || '无描述'} | 
                    创建时间: ${nexusCommon.formatTimestamp(new Date(task.createTime).getTime())} |
                    执行次数: ${task.executeCount || 0} |
                    成功: ${task.successCount || 0} | 
                    失败: ${task.failCount || 0}
                </div>
            </div>
            <div class="task-status task-status-${(task.status || 'pending').toLowerCase()}">
                ${getTaskStatusLabel(task.status)}
            </div>
            <div class="task-actions">
                <button class="nexus-btn nexus-btn-sm nexus-btn-primary" onclick="executeTask('${task.id}')" 
                        ${task.status === 'RUNNING' ? 'disabled' : ''}>
                    <i class="ri-play-line"></i>
                </button>
                <button class="nexus-btn nexus-btn-sm nexus-btn-secondary" onclick="editTask('${task.id}')">
                    <i class="ri-edit-line"></i>
                </button>
                <button class="nexus-btn nexus-btn-sm ${task.enabled ? 'nexus-btn-warning' : 'nexus-btn-success'}" 
                        onclick="toggleTask('${task.id}', ${!task.enabled})">
                    <i class="ri-${task.enabled ? 'pause' : 'play'}-line"></i>
                </button>
                <button class="nexus-btn nexus-btn-sm nexus-btn-danger" onclick="deleteTask('${task.id}')">
                    <i class="ri-delete-bin-line"></i>
                </button>
            </div>
        </div>
    `).join('');
}

/**
 * 获取任务类型标签
 */
function getTaskTypeLabel(type) {
    const labels = {
        'DATABASE': '数据库',
        'API': 'API',
        'FILE': '文件',
        'LOG': '日志',
        'CUSTOM': '自定义'
    };
    return labels[type] || type;
}

/**
 * 获取任务状态标签
 */
function getTaskStatusLabel(status) {
    const labels = {
        'PENDING': '待执行',
        'RUNNING': '执行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'CANCELLED': '已取消'
    };
    return labels[status] || status;
}

/**
 * 刷新任务列表
 */
function refreshTasks() {
    loadTasks();
    loadStats();
    nexusCommon.showMessage('任务列表已刷新', 'info');
}

/**
 * 打开任务模态框
 */
function openTaskModal(taskId = null) {
    const modal = document.getElementById('taskModal');
    const title = document.getElementById('modalTitle');
    const form = document.getElementById('taskForm');
    
    if (taskId) {
        const task = tasks.find(t => t.id === taskId);
        if (task) {
            title.textContent = '编辑任务';
            document.getElementById('taskId').value = task.id;
            document.getElementById('taskName').value = task.name;
            document.getElementById('taskDescription').value = task.description || '';
            document.getElementById('taskType').value = task.type;
            document.getElementById('sourceConfig').value = task.sourceConfig || '';
            document.getElementById('targetConfig').value = task.targetConfig || '';
            document.getElementById('cronExpression').value = task.cronExpression || '';
            document.getElementById('taskEnabled').checked = task.enabled;
        }
    } else {
        title.textContent = '新建任务';
        form.reset();
        document.getElementById('taskId').value = '';
    }
    
    modal.classList.add('active');
}

/**
 * 关闭任务模态框
 */
function closeTaskModal() {
    document.getElementById('taskModal').classList.remove('active');
}

/**
 * 保存任务
 */
async function saveTask() {
    const taskId = document.getElementById('taskId').value;
    const task = {
        name: document.getElementById('taskName').value,
        description: document.getElementById('taskDescription').value,
        type: document.getElementById('taskType').value,
        sourceConfig: document.getElementById('sourceConfig').value,
        targetConfig: document.getElementById('targetConfig').value,
        cronExpression: document.getElementById('cronExpression').value,
        enabled: document.getElementById('taskEnabled').checked
    };
    
    if (!task.name || !task.type) {
        nexusCommon.showMessage('请填写必填项', 'warning');
        return;
    }
    
    try {
        const url = taskId ? `/api/tasks/${taskId}` : '/api/tasks';
        const method = taskId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(task)
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            nexusCommon.showMessage(taskId ? '任务更新成功' : '任务创建成功', 'success');
            closeTaskModal();
            loadTasks();
            loadStats();
        } else {
            nexusCommon.showMessage(result.message || '操作失败', 'error');
        }
    } catch (error) {
        console.error('保存任务失败:', error);
        nexusCommon.showMessage('保存任务失败', 'error');
    }
}

/**
 * 编辑任务
 */
function editTask(taskId) {
    openTaskModal(taskId);
}

/**
 * 执行任务
 */
async function executeTask(taskId) {
    try {
        const response = await fetch(`/api/tasks/${taskId}/execute`, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            nexusCommon.showMessage('任务开始执行', 'success');
            loadTasks();
            loadStats();
        } else {
            nexusCommon.showMessage(result.message || '执行失败', 'error');
        }
    } catch (error) {
        console.error('执行任务失败:', error);
        nexusCommon.showMessage('执行任务失败', 'error');
    }
}

/**
 * 切换任务状态
 */
async function toggleTask(taskId, enable) {
    try {
        const url = enable ? `/api/tasks/${taskId}/enable` : `/api/tasks/${taskId}/disable`;
        const response = await fetch(url, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            nexusCommon.showMessage(enable ? '任务已启用' : '任务已禁用', 'success');
            loadTasks();
            loadStats();
        } else {
            nexusCommon.showMessage(result.message || '操作失败', 'error');
        }
    } catch (error) {
        console.error('切换任务状态失败:', error);
        nexusCommon.showMessage('切换任务状态失败', 'error');
    }
}

/**
 * 删除任务
 */
async function deleteTask(taskId) {
    if (!confirm('确定要删除这个任务吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/tasks/${taskId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.code === 200) {
            nexusCommon.showMessage('任务删除成功', 'success');
            loadTasks();
            loadStats();
        } else {
            nexusCommon.showMessage(result.message || '删除失败', 'error');
        }
    } catch (error) {
        console.error('删除任务失败:', error);
        nexusCommon.showMessage('删除任务失败', 'error');
    }
}

// 点击模态框外部关闭
document.addEventListener('DOMContentLoaded', function() {
    const taskModal = document.getElementById('taskModal');
    if (taskModal) {
        taskModal.addEventListener('click', function(e) {
            if (e.target === this) {
                closeTaskModal();
            }
        });
    }
});
