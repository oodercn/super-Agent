let historyData = [];
let statistics = {};
let pageNum = 1;
let pageSize = 20;
let hasMore = true;

document.addEventListener('DOMContentLoaded', async function() {
    await initDicts();
    await refreshHistory();
});

async function initDicts() {
    if (typeof DictCache !== 'undefined') {
        await DictCache.init();
        
        const sceneTypeSelect = document.getElementById('sceneType');
        if (sceneTypeSelect) {
            const items = await DictCache.getDictItems(DictCache.DICT_CODES.TEMPLATE_CATEGORY);
            if (items && items.length) {
                items.forEach(item => {
                    const option = document.createElement('option');
                    option.value = item.code;
                    option.textContent = item.name;
                    sceneTypeSelect.appendChild(option);
                });
            }
        }
    }
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'});
}

function formatDuration(startTime, endTime) {
    if (!startTime || !endTime) return '-';
    const diff = endTime - startTime;
    const minutes = Math.floor(diff / 60000);
    if (minutes < 60) {
        return minutes + '分钟';
    }
    const hours = Math.floor(minutes / 60);
    const remainMinutes = minutes % 60;
    return hours + '小时' + (remainMinutes > 0 ? remainMinutes + '分钟' : '');
}

async function refreshHistory() {
    pageNum = 1;
    hasMore = true;
    
    var timeRange = document.getElementById('timeRange').value;
    var sceneType = document.getElementById('sceneType').value;
    var execStatus = document.getElementById('execStatus').value;
    var keyword = document.getElementById('searchKeyword').value;
    
    try {
        var url = '/api/v1/my/history/scenes?pageNum=' + pageNum + '&pageSize=' + pageSize + '&days=' + timeRange;
        if (sceneType) url += '&category=' + sceneType;
        if (execStatus) url += '&status=' + execStatus;
        if (keyword) url += '&keyword=' + encodeURIComponent(keyword);
        
        var result = await ApiClient.get(url);
        
        if (result.status === 'success' && result.data) {
            historyData = result.data.list || [];
            hasMore = historyData.length === pageSize;
        } else {
            historyData = [];
        }
    } catch (error) {
        console.error('Failed to load history:', error);
        historyData = [];
    }
    
    await loadStatistics();
    renderHistory();
}

async function loadStatistics() {
    try {
        var result = await ApiClient.get('/api/v1/my/history/statistics');
        
        if (result.status === 'success' && result.data) {
            statistics = result.data;
        } else {
            statistics = { totalCount: 0, successCount: 0, partialCount: 0, failedCount: 0 };
        }
    } catch (error) {
        console.error('Failed to load statistics:', error);
        statistics = { totalCount: 0, successCount: 0, partialCount: 0, failedCount: 0 };
    }
    
    updateStatsDisplay();
}

function loadMockHistory() {
    historyData = [];
}

function calculateMockStatistics() {
    return { totalCount: 0, successCount: 0, partialCount: 0, failedCount: 0 };
}

function updateStatsDisplay() {
    document.getElementById('totalScenes').textContent = statistics.totalScenes || 0;
    document.getElementById('participateCount').textContent = statistics.participateCount || 0;
    document.getElementById('successRate').textContent = (statistics.successRate || 0) + '%';
    document.getElementById('avgDuration').textContent = (statistics.avgDuration || 0) + '分钟';
}

function renderHistory() {
    const container = document.getElementById('historyList');
    
    if (!historyData.length) {
        container.innerHTML = '<div class="empty-state"><i class="ri-history-line"></i><p>暂无历史记录</p></div>';
        document.getElementById('pagination').style.display = 'none';
        return;
    }
    
    let html = '';
    historyData.forEach(item => {
        const statusClass = getStatusClass(item.status);
        const statusText = getStatusText(item.status);
        const categoryText = getCategoryText(item.category);
        
        html += `
            <div class="history-card">
                <div class="history-card-header">
                    <div class="history-card-title">
                        <i class="ri-artboard-line" style="color: var(--nx-primary);"></i>
                        ${item.sceneGroupName}
                        <span class="nx-badge ${statusClass}">${statusText}</span>
                    </div>
                    <span class="nx-text-sm nx-text-secondary">${formatTime(item.startTime)}</span>
                </div>
                <div class="history-card-meta">
                    <span><i class="ri-folder-line"></i> ${categoryText}</span>
                    <span><i class="ri-user-line"></i> ${item.participantCount || 0} 人参与</span>
                    <span><i class="ri-time-line"></i> 耗时 ${formatDuration(item.startTime, item.endTime)}</span>
                    <span><i class="ri-flashlight-line"></i> ${item.triggerType === 'schedule' ? '定时触发' : '手动触发'}</span>
                </div>
                ${item.errorMessage ? `<div class="nx-text-sm status-failed nx-mb-2"><i class="ri-error-warning-line"></i> ${item.errorMessage}</div>` : ''}
                <div class="history-card-actions">
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewDetail('${item.executionId}')">
                        <i class="ri-eye-line"></i> 查看详情
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="rerunScene('${item.sceneGroupId}')">
                        <i class="ri-refresh-line"></i> 重新执行
                    </button>
                </div>
            </div>
        `;
    });
    
    container.innerHTML = html;
    document.getElementById('pagination').style.display = hasMore ? 'flex' : 'none';
}

function getStatusClass(status) {
    const classes = {
        'success': 'nx-badge--success',
        'failed': 'nx-badge--error',
        'partial': 'nx-badge--warning'
    };
    return classes[status] || 'nx-badge--secondary';
}

function getStatusText(status) {
    const texts = {
        'success': '成功',
        'failed': '失败',
        'partial': '部分成功'
    };
    return texts[status] || status;
}

function getCategoryText(category) {
    const categories = {
        'business': '业务场景',
        'iot': '物联网场景',
        'collaboration': '协作场景',
        'governance': '治理场景',
        'notification': '通知推送',
        'data-input': '数据输入',
        'data-processing': '数据处理',
        'intelligence': '智能分析',
        'ui': '界面展示',
        'actuation': '设备控制',
        'sensing': '传感器读取'
    };
    return categories[category] || category;
}

function handleSearch(event) {
    if (event.key === 'Enter') {
        refreshHistory();
    }
}

async function loadMore() {
    if (!hasMore) return;
    
    pageNum++;
    
    try {
        var timeRange = document.getElementById('timeRange').value;
        var result = await ApiClient.get('/api/v1/my/history/scenes?pageNum=' + pageNum + '&pageSize=' + pageSize + '&days=' + timeRange);
        
        if (result.status === 'success' && result.data) {
            var newData = result.data.list || [];
            historyData = historyData.concat(newData);
            hasMore = newData.length === pageSize;
            renderHistory();
        }
    } catch (error) {
        console.error('Failed to load more:', error);
        hasMore = false;
    }
}

function viewDetail(executionId) {
    window.location.href = '/console/pages/execution.html?id=' + executionId;
}

async function rerunScene(sceneGroupId) {
    if (!confirm('确定要重新执行此场景吗？')) return;
    
    try {
        var result = await ApiClient.post('/api/v1/my/history/' + sceneGroupId + '/rerun');
        
        if (result.status === 'success') {
            alert('场景已开始执行');
            window.location.href = '/console/pages/my-scenes.html';
        } else {
            alert('执行失败: ' + result.message);
        }
    } catch (error) {
        console.error('Failed to rerun scene:', error);
        alert('执行失败');
    }
}

async function exportHistory() {
    var timeRange = document.getElementById('timeRange').value;
    var sceneType = document.getElementById('sceneType').value;
    var execStatus = document.getElementById('execStatus').value;
    
    try {
        var url = '/api/v1/my/history/export?days=' + timeRange;
        if (sceneType) url += '&category=' + sceneType;
        if (execStatus) url += '&status=' + execStatus;
        
        var response = await fetch(url);
        
        if (response.ok) {
            var blob = await response.blob();
            var downloadUrl = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = downloadUrl;
            a.download = '场景历史记录_' + new Date().toISOString().split('T')[0] + '.csv';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(downloadUrl);
        } else {
            alert('导出失败');
        }
    } catch (error) {
        console.error('Failed to export:', error);
        alert('导出失败');
    }
}
