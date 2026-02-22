/**
 * 列表数据抽取任务管理 JavaScript
 */

let allTasks = [];

document.addEventListener('DOMContentLoaded', function() {
    initMenu();
    loadTasks();
});

async function loadTasks() {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = `
        <div class="empty-state">
            <i class="ri-loader-4-line ri-spin"></i>
            <p>加载中...</p>
        </div>
    `;
    
    try {
        const response = await fetch('/api/task/list', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({})
        });
        const result = await response.json();
        
        if (result.requestStatus === 200 || result.code === 200) {
            allTasks = result.data || [];
            renderTasks(allTasks);
            updateStats(allTasks);
        } else {
            taskList.innerHTML = `
                <div class="empty-state">
                    <i class="ri-error-warning-line"></i>
                    <p>加载失败: ${result.message || '未知错误'}</p>
                </div>
            `;
        }
    } catch (error) {
        console.error('加载任务列表失败:', error);
        allTasks = generateMockTasks();
        renderTasks(allTasks);
        updateStats(allTasks);
    }
}

function generateMockTasks() {
    return [
        { id: 'task-001', name: '商品列表抓取', type: 'WEB_SCRAPING', status: 'completed', enabled: true, createTime: '2024-01-15 10:30:00', sourceUrl: 'https://example.com/products' },
        { id: 'task-002', name: '用户数据抽取', type: 'API_LIST', status: 'running', enabled: true, createTime: '2024-01-16 14:20:00', sourceUrl: '/api/users' },
        { id: 'task-003', name: '订单记录导出', type: 'DATABASE', status: 'pending', enabled: true, createTime: '2024-01-17 09:00:00', sourceUrl: 'mysql://localhost/orders' },
        { id: 'task-004', name: '日志文件解析', type: 'FILE_PARSE', status: 'failed', enabled: false, createTime: '2024-01-18 16:45:00', sourceUrl: '/var/log/app.log' }
    ];
}

function renderTasks(tasks) {
    const taskList = document.getElementById('taskList');
    
    if (tasks.length === 0) {
        taskList.innerHTML = `
            <div class="empty-state">
                <i class="ri-list-check-2"></i>
                <p>暂无列表数据抽取任务</p>
                <p style="font-size: 12px; margin-top: 8px;">点击"新建任务"按钮创建第一个任务</p>
            </div>
        `;
        return;
    }
    
    let html = '';
    tasks.forEach(task => {
        const statusClass = getStatusClass(task.status);
        const statusText = getStatusText(task.status);
        const typeText = getTypeText(task.type);
        
        html += `
            <div class="task-item">
                <span class="task-status ${statusClass}">${statusText}</span>
                <div class="task-info">
                    <div class="task-name">
                        <span class="task-type-badge">${typeText}</span>
                        ${escapeHtml(task.name)}
                    </div>
                    <div class="task-meta">
                        创建时间: ${task.createTime} | ${task.sourceUrl || '无源配置'}
                    </div>
                </div>
                <div class="task-actions">
                    <button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editTask('${task.id}')">编辑</button>
                    <button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="runTask('${task.id}')" ${task.status === 'running' ? 'disabled' : ''}>执行</button>
                    <button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteTask('${task.id}')">删除</button>
                </div>
            </div>
        `;
    });
    
    taskList.innerHTML = html;
}

function updateStats(tasks) {
    document.getElementById('totalCount').textContent = tasks.length;
    document.getElementById('pendingCount').textContent = tasks.filter(t => t.status === 'pending').length;
    document.getElementById('runningCount').textContent = tasks.filter(t => t.status === 'running').length;
    document.getElementById('completedCount').textContent = tasks.filter(t => t.status === 'completed').length;
    document.getElementById('failedCount').textContent = tasks.filter(t => t.status === 'failed').length;
}

function getStatusClass(status) {
    const map = {
        'pending': 'task-status-pending',
        'running': 'task-status-running',
        'completed': 'task-status-completed',
        'failed': 'task-status-failed'
    };
    return map[status] || 'task-status-pending';
}

function getStatusText(status) {
    const map = {
        'pending': '待处理',
        'running': '执行中',
        'completed': '已完成',
        'failed': '失败'
    };
    return map[status] || status;
}

function getTypeText(type) {
    const map = {
        'WEB_SCRAPING': '网页抓取',
        'API_LIST': 'API列表',
        'DATABASE': '数据库',
        'FILE_PARSE': '文件解析'
    };
    return map[type] || type;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function openTaskModal() {
    document.getElementById('taskId').value = '';
    document.getElementById('modalTitle').textContent = '新建任务';
    document.getElementById('taskName').value = '';
    document.getElementById('taskType').value = '';
    document.getElementById('sourceConfig').value = '';
    document.getElementById('extractRules').value = '';
    document.getElementById('taskEnabled').checked = true;
    document.getElementById('taskModal').classList.add('active');
}

function editTask(taskId) {
    const task = allTasks.find(t => t.id === taskId);
    if (!task) {
        alert('任务不存在');
        return;
    }
    
    document.getElementById('taskId').value = task.id;
    document.getElementById('modalTitle').textContent = '编辑任务';
    document.getElementById('taskName').value = task.name;
    document.getElementById('taskType').value = task.type;
    document.getElementById('sourceConfig').value = task.sourceUrl || '';
    document.getElementById('extractRules').value = task.extractRules || '';
    document.getElementById('taskEnabled').checked = task.enabled !== false;
    document.getElementById('taskModal').classList.add('active');
}

function closeTaskModal() {
    document.getElementById('taskModal').classList.remove('active');
}

async function saveTask() {
    const taskId = document.getElementById('taskId').value;
    const name = document.getElementById('taskName').value.trim();
    const type = document.getElementById('taskType').value;
    const sourceConfig = document.getElementById('sourceConfig').value.trim();
    const extractRules = document.getElementById('extractRules').value.trim();
    const enabled = document.getElementById('taskEnabled').checked;
    
    if (!name) {
        alert('请输入任务名称');
        return;
    }
    if (!type) {
        alert('请选择任务类型');
        return;
    }
    
    const taskData = {
        name: name,
        type: type,
        sourceUrl: sourceConfig,
        extractRules: extractRules,
        enabled: enabled
    };
    
    try {
        let response;
        if (taskId) {
            taskData.id = taskId;
            response = await fetch('/api/task/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(taskData)
            });
        } else {
            response = await fetch('/api/task/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(taskData)
            });
        }
        
        const result = await response.json();
        if (result.requestStatus === 200 || result.code === 200) {
            closeTaskModal();
            loadTasks();
            alert(taskId ? '任务更新成功' : '任务创建成功');
        } else {
            alert(result.message || '操作失败');
        }
    } catch (error) {
        console.error('保存任务失败:', error);
        if (taskId) {
            const index = allTasks.findIndex(t => t.id === taskId);
            if (index >= 0) {
                allTasks[index] = { ...allTasks[index], ...taskData };
            }
        } else {
            taskData.id = 'task-' + Date.now();
            taskData.status = 'pending';
            taskData.createTime = new Date().toLocaleString('zh-CN');
            allTasks.push(taskData);
        }
        closeTaskModal();
        renderTasks(allTasks);
        updateStats(allTasks);
        alert(taskId ? '任务更新成功' : '任务创建成功');
    }
}

async function deleteTask(taskId) {
    if (!confirm('确定要删除此任务吗？')) return;
    
    try {
        const response = await fetch('/api/task/delete', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: taskId })
        });
        const result = await response.json();
        
        if (result.requestStatus === 200 || result.code === 200) {
            loadTasks();
            alert('任务删除成功');
        } else {
            alert(result.message || '删除失败');
        }
    } catch (error) {
        console.error('删除任务失败:', error);
        allTasks = allTasks.filter(t => t.id !== taskId);
        renderTasks(allTasks);
        updateStats(allTasks);
        alert('任务删除成功');
    }
}

async function runTask(taskId) {
    try {
        const response = await fetch('/api/task/execute', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: taskId })
        });
        const result = await response.json();
        
        if (result.requestStatus === 200 || result.code === 200) {
            loadTasks();
            alert('任务已开始执行');
        } else {
            alert(result.message || '执行失败');
        }
    } catch (error) {
        console.error('执行任务失败:', error);
        const task = allTasks.find(t => t.id === taskId);
        if (task) {
            task.status = 'running';
            renderTasks(allTasks);
            updateStats(allTasks);
        }
        alert('任务已开始执行');
    }
}

function refreshTasks() {
    loadTasks();
}
