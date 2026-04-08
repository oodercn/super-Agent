var config = {};
var policies = [];

async function init() {
    await loadConfig();
    await loadPolicies();
    await loadStats();
}

async function loadConfig() {
    try {
        var result = await ApiClient.get('/api/v1/security/config');
        
        var configData;
        if (result && result.status === 'success') {
            configData = result.data || {};
        } else {
            configData = result || {};
        }
        config = configData;
        
        document.getElementById('enableAuth').checked = config.enableAuth !== false;
        document.getElementById('enableEncryption').checked = config.enableEncryption !== false;
        document.getElementById('enableAudit').checked = config.enableAudit !== false;
        document.getElementById('sessionTimeout').value = config.sessionTimeout || 30;
        document.getElementById('maxLoginAttempts').value = config.maxLoginAttempts || 5;
        document.getElementById('keyRotationDays').value = config.keyRotationDays || 90;
        document.getElementById('enableFirewall').checked = config.enableFirewall === true;
        document.getElementById('firewallMode').value = config.firewallMode || 'active';
        document.getElementById('enableAgentAuth').checked = config.enableAgentAuth !== false;
        document.getElementById('enableAgentEncryption').checked = config.enableAgentEncryption !== false;
        document.getElementById('enableAgentIsolation').checked = config.enableAgentIsolation !== false;
        document.getElementById('llmRateLimit').value = config.llmRateLimit || 60;
        document.getElementById('costAlertThreshold').value = config.costAlertThreshold || 100;
        
    } catch (e) {
        console.error('Load config failed:', e);
    }
}

async function loadPolicies() {
    try {
        var result = await ApiClient.get('/api/v1/security/policies');
        
        if (result && result.status === 'success') {
            policies = result.data || [];
        } else {
            policies = result || [];
        }
        renderPolicies();
    } catch (e) {
        console.error('Load policies failed:', e);
        policies = [
            { policyId: 'p1', name: '默认访问控制', type: 'ACCESS_CONTROL', enabled: true, rules: ['用户认证', '权限检查'] },
            { policyId: 'p2', name: '数据加密策略', type: 'DATA_PROTECTION', enabled: true, rules: ['传输加密', '存储加密'] }
        ];
        renderPolicies();
    }
}

async function loadStats() {
    try {
        var result = await ApiClient.get('/api/v1/keys');
        
        var keys;
        if (result && result.status === 'success') {
            keys = result.data || [];
        } else {
            keys = result || [];
        }
        document.getElementById('activeKeys').textContent = keys ? keys.filter(function(k) { return k.status === 'ACTIVE'; }).length : 0;
    } catch (e) {
        document.getElementById('activeKeys').textContent = '0';
    }
    
    try {
        var result = await ApiClient.get('/api/v1/security/stats');
        
        var stats;
        if (result && result.status === 'success') {
            stats = result.data || {};
        } else {
            stats = result || {};
        }
        document.getElementById('threatCount').textContent = stats.threatCount || 0;
        document.getElementById('policyCount').textContent = policies.length;
    } catch (e) {
        document.getElementById('threatCount').textContent = '0';
        document.getElementById('policyCount').textContent = policies.length;
    }
}

function renderPolicies() {
    var container = document.getElementById('policyList');
    
    if (policies.length === 0) {
        container.innerHTML = '<p style="color: var(--ns-secondary); text-align: center; padding: 20px;">暂无安全策略</p>';
        return;
    }
    
    var html = '';
    policies.forEach(function(policy) {
        var iconClass = policy.enabled ? 'active' : 'inactive';
        var rulesHtml = policy.rules ? policy.rules.map(function(r) { return '<span>' + r + '</span>'; }).join('') : '';
        
        html += '<div class="policy-card">';
        html += '  <div class="policy-header">';
        html += '    <div class="policy-name">';
        html += '      <span class="status-dot ' + iconClass + '"></span>';
        html += '      ' + policy.name;
        html += '    </div>';
        html += '    <div>';
        html += '      <button class="nx-btn nx-btn--ghost" onclick="togglePolicy(\'' + policy.policyId + '\')">' + (policy.enabled ? '禁用' : '启用') + '</button>';
        html += '      <button class="nx-btn nx-btn--ghost" style="color: var(--ns-danger);" onclick="deletePolicy(\'' + policy.policyId + '\')">删除</button>';
        html += '    </div>';
        html += '  </div>';
        html += '  <div class="policy-rules">' + rulesHtml + '</div>';
        html += '</div>';
    });
    
    container.innerHTML = html;
}

async function saveConfig() {
    var configData = {
        enableAuth: document.getElementById('enableAuth').checked,
        enableEncryption: document.getElementById('enableEncryption').checked,
        enableAudit: document.getElementById('enableAudit').checked,
        sessionTimeout: parseInt(document.getElementById('sessionTimeout').value),
        maxLoginAttempts: parseInt(document.getElementById('maxLoginAttempts').value),
        keyRotationDays: parseInt(document.getElementById('keyRotationDays').value),
        enableFirewall: document.getElementById('enableFirewall').checked,
        firewallMode: document.getElementById('firewallMode').value,
        enableAgentAuth: document.getElementById('enableAgentAuth').checked,
        enableAgentEncryption: document.getElementById('enableAgentEncryption').checked,
        enableAgentIsolation: document.getElementById('enableAgentIsolation').checked,
        llmRateLimit: parseInt(document.getElementById('llmRateLimit').value),
        costAlertThreshold: parseInt(document.getElementById('costAlertThreshold').value)
    };
    
    try {
        var result = await ApiClient.put('/api/v1/security/config', configData);
        
        if (result && (result.status === 'success' || result.code === undefined)) {
            alert('配置保存成功');
        } else {
            var errorMsg = result.message || result.error || '配置保存失败';
            alert('配置保存失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Save config failed:', e);
        alert('配置保存失败: ' + e.message);
    }
}

function showCreatePolicyModal() {
    document.getElementById('createPolicyModal').classList.add('nx-modal--open');
    document.getElementById('policyForm').reset();
}

function hideCreatePolicyModal() {
    document.getElementById('createPolicyModal').classList.remove('nx-modal--open');
}

async function createPolicy(e) {
    e.preventDefault();
    
    var policy = {
        name: document.getElementById('policyName').value,
        type: document.getElementById('policyType').value,
        description: document.getElementById('policyDescription').value,
        enabled: true,
        rules: []
    };
    
    try {
        var result = await ApiClient.post('/api/v1/security/policies', policy);
        
        if (result && (result.status === 'success' || result.code === undefined)) {
            hideCreatePolicyModal();
            loadPolicies();
            alert('策略创建成功');
        } else {
            var errorMsg = result.message || result.error || '创建失败';
            alert('创建失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Create policy failed:', e);
        alert('创建失败: ' + e.message);
    }
}

async function togglePolicy(policyId) {
    var policy = policies.find(function(p) { return p.policyId === policyId; });
    if (!policy) return;
    
    var action = policy.enabled ? '禁用' : '启用';
    if (!confirm('确定要' + action + '此策略吗？')) return;
    
    try {
        var url = '/api/v1/security/policies/' + policyId;
        var updates = { enabled: !policy.enabled };
        var result = await ApiClient.put(url, updates);
        
        if (result && (result === true || result.status === 'success')) {
            loadPolicies();
            alert('策略已' + action);
        } else {
            var errorMsg = result.message || result.error || action + '失败';
            alert(action + '失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Toggle policy failed:', e);
        alert(action + '失败: ' + e.message);
    }
}

async function deletePolicy(policyId) {
    if (!confirm('确定要删除此策略吗？')) return;
    
    try {
        var result = await ApiClient.delete('/api/v1/security/policies/' + policyId);
        
        if (result && (result.status === 'success' || result.code === undefined)) {
            loadPolicies();
            alert('策略删除成功');
        } else {
            var errorMsg = result.message || result.error || '删除失败';
            alert('删除失败: ' + errorMsg);
        }
    } catch (e) {
        console.error('Delete policy failed:', e);
        alert('删除失败: ' + e.message);
    }
}

init();
