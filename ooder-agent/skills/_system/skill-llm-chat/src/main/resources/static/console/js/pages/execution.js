let executionId = null;
let executionData = null;

document.addEventListener('DOMContentLoaded', async function() {
    executionId = getUrlParam('id');
    if (!executionId) {
        alert('未指定执行ID');
        goBack();
        return;
    }
    
    await loadExecutionDetail();
});

function getUrlParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
}

async function loadExecutionDetail() {
    try {
        const response = await fetch('/api/v1/my/history/' + executionId);
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            executionData = result.data;
            renderExecutionDetail();
        } else {
            document.getElementById('executionLogs').innerHTML = '<p class="nx-text-danger">加载失败: ' + (result.message || '未知错误') + '</p>';
        }
    } catch (error) {
        console.error('Failed to load execution detail:', error);
        document.getElementById('executionLogs').innerHTML = '<p class="nx-text-danger">加载失败: ' + error.message + '</p>';
    }
}

function renderExecutionDetail() {
    if (!executionData) return;
    
    document.getElementById('pageTitle').textContent = executionData.sceneName || '执行详情';
    document.getElementById('executionId').textContent = executionData.executionId || '-';
    document.getElementById('sceneName').textContent = executionData.sceneName || '-';
    
    const statusBadge = getStatusBadge(executionData.status);
    document.getElementById('executionStatus').innerHTML = statusBadge;
    
    document.getElementById('startTime').textContent = formatTime(executionData.startTime);
    document.getElementById('endTime').textContent = formatTime(executionData.endTime);
    
    if (executionData.startTime && executionData.endTime) {
        const duration = executionData.endTime - executionData.startTime;
        document.getElementById('duration').textContent = formatDuration(duration);
    } else {
        document.getElementById('duration').textContent = '-';
    }
    
    renderLogs(executionData.logs);
    renderResult(executionData.result);
}

function getStatusBadge(status) {
    const badges = {
        'SUCCESS': '<span class="nx-badge nx-badge--success">成功</span>',
        'FAILED': '<span class="nx-badge nx-badge--danger">失败</span>',
        'RUNNING': '<span class="nx-badge nx-badge--info">执行中</span>',
        'PENDING': '<span class="nx-badge nx-badge--warning">等待中</span>',
        'CANCELLED': '<span class="nx-badge nx-badge--secondary">已取消</span>'
    };
    return badges[status] || '<span class="nx-badge nx-badge--secondary">' + status + '</span>';
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN');
}

function formatDuration(ms) {
    if (!ms || ms < 0) return '-';
    const seconds = Math.floor(ms / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    
    if (hours > 0) {
        return hours + '小时' + (minutes % 60) + '分钟';
    } else if (minutes > 0) {
        return minutes + '分钟' + (seconds % 60) + '秒';
    } else {
        return seconds + '秒';
    }
}

function renderLogs(logs) {
    const container = document.getElementById('executionLogs');
    
    if (!logs || logs.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary">暂无日志</p>';
        return;
    }
    
    let html = '';
    logs.forEach(log => {
        const time = formatTime(log.timestamp || log.time);
        const level = log.level || 'INFO';
        const levelClass = level === 'ERROR' ? 'nx-text-danger' : level === 'WARN' ? 'nx-text-warning' : 'nx-text-secondary';
        const message = log.message || log.msg || log;
        
        html += '<div class="nx-mb-2"><span class="nx-text-xs ' + levelClass + '">[' + time + '] [' + level + ']</span> <span>' + message + '</span></div>';
    });
    
    container.innerHTML = html;
}

function renderResult(result) {
    const container = document.getElementById('executionResult');
    
    if (!result) {
        container.innerHTML = '<p class="nx-text-secondary">暂无结果</p>';
        return;
    }
    
    if (typeof result === 'string') {
        container.innerHTML = '<pre class="nx-bg-gray-50 nx-p-4 nx-rounded nx-overflow-x-auto">' + result + '</pre>';
    } else {
        container.innerHTML = '<pre class="nx-bg-gray-50 nx-p-4 nx-rounded nx-overflow-x-auto">' + JSON.stringify(result, null, 2) + '</pre>';
    }
}

function goBack() {
    if (document.referrer) {
        window.history.back();
    } else {
        window.location.href = '/console/pages/my-history.html';
    }
}

async function rerunExecution() {
    if (!confirm('确定要重新执行此场景吗？')) return;
    
    try {
        const response = await fetch('/api/v1/my/history/' + executionId + '/rerun', {
            method: 'POST'
        });
        const result = await response.json();
        
        if (result.status === 'success') {
            alert('场景已开始执行');
            window.location.href = '/console/pages/my-scenes.html';
        } else {
            alert('执行失败: ' + result.message);
        }
    } catch (error) {
        console.error('Failed to rerun:', error);
        alert('执行失败: ' + error.message);
    }
}
