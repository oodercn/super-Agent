var keys = [];

async function init() {
    await DictCache.init();
    loadKeyTypeOptions();
    loadKeys();
}

async function loadKeyTypeOptions() {
    var items = await DictCache.getDictItems('key_type');
    var typeSelect = document.getElementById('keyType');
    var typeFilter = document.getElementById('typeFilter');
    
    items.forEach(function(item) {
        typeSelect.innerHTML += '<option value="' + item.code + '">' + item.name + '</option>';
        typeFilter.innerHTML += '<option value="' + item.code + '">' + item.name + '</option>';
    });
}

async function loadKeys() {
    var type = document.getElementById('typeFilter').value;
    var status = document.getElementById('statusFilter').value;
    var search = document.getElementById('searchInput').value;
    
    var url = '/api/v1/keys?';
    if (type) url += 'type=' + type + '&';
    if (status) url += 'status=' + status;
    
    try {
        var result = await ApiClient.get(url);
        
        if (result && result.status === 'success') {
            keys = result.data || [];
        } else {
            keys = result || [];
        }
        
        if (search) {
            keys = keys.filter(function(k) {
                return k.keyName.toLowerCase().includes(search.toLowerCase());
            });
        }
        
        renderKeys();
        updateStats();
    } catch (e) {
        console.error('Load keys failed:', e);
        keys = [];
        renderKeys();
    }
}

function renderKeys() {
    var container = document.getElementById('keyList');
    
    if (keys.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="ri-key-line"></i><p>暂无密钥数据</p></div>';
        return;
    }
    
    var html = '';
    keys.forEach(function(key) {
        var iconClass = getKeyIconClass(key.keyType);
        var statusClass = getStatusClass(key.status);
        var statusName = getStatusName(key.status);
        
        html += '<div class="key-card">';
        html += '  <div class="key-info">';
        html += '    <div class="key-icon ' + iconClass + '"><i class="ri-key-line"></i></div>';
        html += '    <div class="key-details">';
        html += '      <h3>' + key.keyName + '</h3>';
        html += '      <p>' + (key.provider || '-') + '</p>';
        html += '      <div class="key-meta">';
        html += '        <span><i class="ri-time-line"></i> ' + formatDate(key.createdAt) + '</span>';
        html += '        <span><i class="ri-bar-chart-line"></i> ' + key.useCount + ' 次使用</span>';
        html += '      </div>';
        html += '    </div>';
        html += '  </div>';
        html += '  <div style="display: flex; align-items: center; gap: 16px;">';
        html += '    <span class="status-badge ' + statusClass + '">' + statusName + '</span>';
        html += '    <div class="key-actions">';
        html += '      <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="showDetail(\'' + key.keyId + '\')" title="详情"><i class="ri-eye-line"></i></button>';
        html += '      <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="rotateKey(\'' + key.keyId + '\')" title="轮换"><i class="ri-refresh-line"></i></button>';
        html += '      <button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="revokeKey(\'' + key.keyId + '\')" title="撤销"><i class="ri-forbid-line"></i></button>';
        html += '    </div>';
        html += '  </div>';
        html += '</div>';
    });
    
    container.innerHTML = html;
}

function updateStats() {
    document.getElementById('totalKeys').textContent = keys.length;
    document.getElementById('activeKeys').textContent = keys.filter(function(k) { return k.status === 'ACTIVE'; }).length;
    document.getElementById('todayUsage').textContent = keys.reduce(function(sum, k) { return sum + (k.useCount || 0); }, 0);
    
    var now = Date.now();
    var expiring = keys.filter(function(k) {
        return k.expiresAt > 0 && (k.expiresAt - now) < 7 * 24 * 60 * 60 * 1000;
    }).length;
    document.getElementById('expiringKeys').textContent = expiring;
}

function showCreateModal() {
    document.getElementById('createModal').classList.add('nx-modal--open');
    document.getElementById('createForm').reset();
}

function hideCreateModal() {
    document.getElementById('createModal').classList.remove('nx-modal--open');
}

async function createKey(e) {
    e.preventDefault();
    
    var data = {
        keyName: document.getElementById('keyName').value,
        keyType: document.getElementById('keyType').value,
        provider: document.getElementById('provider').value,
        rawValue: document.getElementById('rawValue').value,
        maxUseCount: parseInt(document.getElementById('maxUseCount').value) || -1,
        allowedUsers: [],
        allowedRoles: [],
        allowedScenes: []
    };
    
    var expiresAt = document.getElementById('expiresAt').value;
    if (expiresAt) {
        data.expiresAt = new Date(expiresAt).getTime();
    }
    
    try {
        var result = await ApiClient.post('/api/v1/keys', data);
        
        if (result) {
            hideCreateModal();
            loadKeys();
            alert('创建成功');
        } else {
            var errorMsg = result.message || result.error || '创建失败';
            alert('创建失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Create key failed:', e);
        alert('创建失败: ' + e.message);
    }
}

function showDetail(keyId) {
    var key = keys.find(function(k) { return k.keyId === keyId; });
    if (!key) return;
    
    var html = '<div class="form-group"><label>密钥ID</label><input type="text" value="' + key.keyId + '" readonly></div>';
    html += '<div class="form-row"><div class="form-group"><label>类型</label><input type="text" value="' + key.keyType + '" readonly></div>';
    html += '<div class="form-group"><label>状态</label><input type="text" value="' + key.status + '" readonly></div></div>';
    html += '<div class="form-group"><label>创建时间</label><input type="text" value="' + formatDateTime(key.createdAt) + '" readonly></div>';
    html += '<div class="form-row"><div class="form-group"><label>使用次数</label><input type="text" value="' + key.useCount + '" readonly></div>';
    html += '<div class="form-group"><label>最大次数</label><input type="text" value="' + (key.maxUseCount > 0 ? key.maxUseCount : '无限制') + '" readonly></div></div>';
    
    if (key.allowedUsers && key.allowedUsers.length > 0) {
        html += '<div class="form-group"><label>授权用户</label><input type="text" value="' + key.allowedUsers.join(', ') + '" readonly></div>';
    }
    if (key.allowedScenes && key.allowedScenes.length > 0) {
        html += '<div class="form-group"><label>授权场景</label><input type="text" value="' + key.allowedScenes.join(', ') + '" readonly></div>';
    }
    
    document.getElementById('detailContent').innerHTML = html;
    document.getElementById('detailModal').classList.add('nx-modal--open');
}

function hideDetailModal() {
    document.getElementById('detailModal').classList.remove('nx-modal--open');
}

async function rotateKey(keyId) {
    if (!confirm('确定要轮换此密钥吗？')) return;
    
    var newValue = prompt('请输入新的密钥值:');
    if (!newValue) return;
    
    try {
        var result = await ApiClient.post('/api/v1/keys/' + keyId + '/rotate', newValue);
        
        if (result) {
            loadKeys();
            alert('轮换成功');
        } else {
            var errorMsg = result.message || result.error || '轮换失败';
            alert('轮换失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Rotate key failed:', e);
        alert('轮换失败: ' + e.message);
    }
}

async function revokeKey(keyId) {
    if (!confirm('确定要撤销此密钥吗？此操作不可恢复！')) return;
    
    try {
        var result = await ApiClient.post('/api/v1/keys/' + keyId + '/revoke');
        
        if (result) {
            loadKeys();
            alert('撤销成功');
        } else {
            var errorMsg = result.message || result.error || '撤销失败';
            alert('撤销失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Revoke key failed:', e);
        alert('撤销失败: ' + e.message);
    }
}

function getKeyIconClass(keyType) {
    var typeMap = {
        'LLM_API_KEY': 'llm',
        'CLOUD_API_KEY': 'cloud',
        'DATABASE_KEY': 'database',
        'SERVICE_TOKEN': 'service',
        'AGENT_KEY': 'agent',
        'ENCRYPTION_KEY': 'encryption'
    };
    return typeMap[keyType] || 'llm';
}

function getStatusClass(status) {
    var classMap = {
        'ACTIVE': 'status-active',
        'INACTIVE': 'status-inactive',
        'EXPIRED': 'status-expired',
        'REVOKED': 'status-revoked'
    };
    return classMap[status] || 'status-inactive';
}

function getStatusName(status) {
    var nameMap = {
        'ACTIVE': '激活',
        'INACTIVE': '未激活',
        'EXPIRED': '已过期',
        'REVOKED': '已撤销'
    };
    return nameMap[status] || status;
}

function formatDate(timestamp) {
    if (!timestamp) return '-';
    var d = new Date(timestamp);
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
}

function formatDateTime(timestamp) {
    if (!timestamp) return '-';
    var d = new Date(timestamp);
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + 
           pad(d.getHours()) + ':' + pad(d.getMinutes());
}

function pad(n) {
    return n < 10 ? '0' + n : n;
}

init();
