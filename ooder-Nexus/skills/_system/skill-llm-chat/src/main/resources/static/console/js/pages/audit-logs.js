var logs = [];
var currentPage = 1;
var pageSize = 20;
var total = 0;

async function init() {
    await DictCache.init();
    loadEventTypeOptions();
    initFiltersFromUrl();
    loadLogs();
    loadStats();
}

function initFiltersFromUrl() {
    var urlParams = new URLSearchParams(window.location.search);
    var resourceId = urlParams.get('resourceId');
    var userId = urlParams.get('userId');
    var eventType = urlParams.get('eventType');
    var result = urlParams.get('result');
    
    if (resourceId) {
        document.getElementById('resourceFilter').value = resourceId;
    }
    if (userId) {
        document.getElementById('userFilter').value = userId;
    }
    if (eventType) {
        document.getElementById('eventTypeFilter').value = eventType;
    }
    if (result) {
        document.getElementById('resultFilter').value = result;
    }
}

async function loadEventTypeOptions() {
    var items = await DictCache.getDictItems(DictCache.DICT_CODES.AUDIT_EVENT_TYPE);
    var select = document.getElementById('eventTypeFilter');
    
    items.forEach(function(item) {
        select.innerHTML += '<option value="' + item.code + '">' + item.name + '</option>';
    });
}

async function loadLogs() {
    var params = new URLSearchParams();
    params.append('pageNum', currentPage);
    params.append('pageSize', pageSize);
    
    var eventType = document.getElementById('eventTypeFilter').value;
    var result = document.getElementById('resultFilter').value;
    var userId = document.getElementById('userFilter').value;
    var resourceId = document.getElementById('resourceFilter').value;
    var startTime = document.getElementById('startTime').value;
    var endTime = document.getElementById('endTime').value;
    
    if (eventType) params.append('eventType', eventType);
    if (result) params.append('result', result);
    if (userId) params.append('userId', userId);
    if (resourceId) params.append('resourceId', resourceId);
    if (startTime) params.append('startTime', new Date(startTime).getTime());
    if (endTime) params.append('endTime', new Date(endTime).getTime());
    
    try {
        var res = await ApiClient.get('/api/v1/audit/logs?' + params.toString());
        
        if (res && res.code === 200 && res.data) {
            logs = res.data.list || [];
            total = res.data.total || logs.length;
        } else {
            logs = [];
        }
        renderLogs();
    } catch (e) {
        console.error('Load logs failed:', e);
        logs = [];
        renderLogs();
    }
}

async function loadStats() {
    try {
        var result = await ApiClient.get('/api/v1/audit/stats');
        
        var stats;
        if (result && result.code === 200 && result.data) {
            stats = result.data || {};
        } else {
            stats = {};
        }
        
        document.getElementById('totalEvents').textContent = stats.totalEvents || 0;
        document.getElementById('successCount').textContent = stats.successCount || 0;
        document.getElementById('failureCount').textContent = stats.failureCount || 0;
        document.getElementById('deniedCount').textContent = stats.deniedCount || 0;
        document.getElementById('todayCount').textContent = stats.todayCount || 0;
        document.getElementById('weekCount').textContent = stats.weekCount || 0;
        document.getElementById('monthCount').textContent = stats.monthCount || 0;
    } catch (e) {
        console.error('Load stats failed:', e);
    }
}

function renderLogs() {
    var tbody = document.getElementById('logList');
    
    if (logs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 40px; color: var(--ns-secondary);">暂无审计日志</td></tr>';
        return;
    }
    
    var html = '';
    logs.forEach(function(log) {
        var eventIconClass = getEventIconClass(log.eventType);
        var resultClass = getResultClass(log.result);
        var resultName = getResultName(log.result);
        var eventName = getEventName(log.eventType);
        
        html += '<tr>';
        html += '<td>' + formatDateTime(log.timestamp) + '</td>';
        html += '<td><div class="event-type"><div class="event-icon ' + eventIconClass + '"><i class="ri-shield-line"></i></div><span>' + eventName + '</span></div></td>';
        html += '<td>' + (log.userId || '-') + '</td>';
        html += '<td>' + (log.resourceId || '-') + '</td>';
        html += '<td>' + (log.action || '-') + '</td>';
        html += '<td><span class="result-badge ' + resultClass + '">' + resultName + '</span></td>';
        html += '<td><button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="showDetail(\'' + log.recordId + '\')"><i class="ri-eye-line"></i></button></td>';
        html += '</tr>';
    });
    
    tbody.innerHTML = html;
}

function showDetail(recordId) {
    var log = logs.find(function(l) { return l.recordId === recordId; });
    if (!log) return;
    
    var html = '';
    html += '<div class="detail-row"><div class="detail-label">记录ID</div><div class="detail-value">' + log.recordId + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">事件类型</div><div class="detail-value">' + getEventName(log.eventType) + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">时间</div><div class="detail-value">' + formatDateTime(log.timestamp) + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">用户</div><div class="detail-value">' + (log.userId || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">Agent</div><div class="detail-value">' + (log.agentId || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">资源类型</div><div class="detail-value">' + (log.resourceType || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">资源ID</div><div class="detail-value">' + (log.resourceId || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">操作</div><div class="detail-value">' + (log.action || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">结果</div><div class="detail-value">' + getResultName(log.result) + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">详情</div><div class="detail-value">' + (log.detail || '-') + '</div></div>';
    html += '<div class="detail-row"><div class="detail-label">IP地址</div><div class="detail-value">' + (log.ipAddress || '-') + '</div></div>';
    
    if (log.metadata && Object.keys(log.metadata).length > 0) {
        html += '<div class="detail-row"><div class="detail-label">元数据</div><div class="detail-value"><pre>' + JSON.stringify(log.metadata, null, 2) + '</pre></div></div>';
    }
    
    document.getElementById('detailContent').innerHTML = html;
    document.getElementById('detailModal').classList.add('nx-modal--open');
}

function hideDetailModal() {
    document.getElementById('detailModal').classList.remove('nx-modal--open');
}

function exportLogs() {
    var params = new URLSearchParams();
    
    var eventType = document.getElementById('eventTypeFilter').value;
    var result = document.getElementById('resultFilter').value;
    var userId = document.getElementById('userFilter').value;
    var resourceId = document.getElementById('resourceFilter').value;
    var startTime = document.getElementById('startTime').value;
    var endTime = document.getElementById('endTime').value;
    
    if (eventType) params.append('eventType', eventType);
    if (result) params.append('result', result);
    if (userId) params.append('userId', userId);
    if (resourceId) params.append('resourceId', resourceId);
    if (startTime) params.append('startTime', new Date(startTime).getTime());
    if (endTime) params.append('endTime', new Date(endTime).getTime());
    
    var url = '/api/v1/audit/export?' + params.toString();
    
    var link = document.createElement('a');
    link.href = url;
    link.download = 'audit_logs.csv';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function getEventIconClass(eventType) {
    if (!eventType) return 'key';
    var code = eventType.code || eventType;
    if (code.startsWith('KEY')) return 'key';
    if (code.startsWith('PERMISSION')) return 'permission';
    if (code.startsWith('AGENT')) return 'agent';
    if (code.startsWith('SCENE')) return 'scene';
    if (code.startsWith('LLM')) return 'llm';
    return 'key';
}

function getEventName(eventType) {
    if (!eventType) return '-';
    return eventType.name || eventType.code || eventType;
}

function getResultClass(result) {
    if (!result) return 'result-success';
    var code = result.code || result;
    var classMap = {
        'SUCCESS': 'result-success',
        'FAILURE': 'result-failure',
        'DENIED': 'result-denied'
    };
    return classMap[code] || 'result-success';
}

function getResultName(result) {
    if (!result) return '-';
    return result.name || result.code || result;
}

function formatDateTime(timestamp) {
    if (!timestamp) return '-';
    var d = new Date(timestamp);
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + 
           pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
}

function pad(n) {
    return n < 10 ? '0' + n : n;
}

init();
