let currentGroup = null;
let sceneGroupId = null;
let availableCapabilities = [];
let allUsers = [];
let selectedParticipant = null;
let selectedCapability = null;
let currentDetailBinding = null;

document.addEventListener('DOMContentLoaded', async function() {
    sceneGroupId = getUrlParam('id');
    
    initTabs();
    await loadAllUsers();
    await loadCapabilities();
    
    if (sceneGroupId) {
        loadSceneGroup(sceneGroupId);
    } else {
        loadMockSceneGroup();
    }
});

function getUrlParam(name) {
    const params = new URLSearchParams(window.location.search);
    return params.get(name);
}

function initTabs() {
    const tabs = document.querySelectorAll('.nx-tabs__tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            tabs.forEach(t => t.classList.remove('nx-tabs__tab--active'));
            this.classList.add('nx-tabs__tab--active');
            
            const tabId = this.getAttribute('data-tab');
            document.querySelectorAll('.tab-content').forEach(content => {
                content.style.display = 'none';
            });
            document.getElementById('tab-' + tabId).style.display = 'block';
            
            if (tabId === 'knowledge') {
                loadKnowledgeBindings();
            } else if (tabId === 'llm') {
                loadLlmConfig();
            }
        });
    });
}

async function loadAllUsers() {
    try {
        const response = await fetch('/api/v1/org/users');
        const result = await response.json();
        
        if (result.status === 'success') {
            allUsers = result.data || [];
            console.log('Loaded users:', allUsers.length);
        } else {
            allUsers = getDefaultUsers();
        }
    } catch (error) {
        console.error('Failed to load users:', error);
        allUsers = getDefaultUsers();
    }
}

function getDefaultUsers() {
    return [];
}

async function loadCapabilities() {
    try {
        const response = await fetch('/api/v1/capabilities');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            availableCapabilities = result.data.list || result.data || [];
        } else if (Array.isArray(result)) {
            availableCapabilities = result;
        } else {
            availableCapabilities = getDefaultCapabilities();
        }
    } catch (error) {
        console.error('Failed to load capabilities:', error);
        availableCapabilities = getDefaultCapabilities();
    }
}

function getDefaultCapabilities() {
    return [];
}

async function loadSceneGroup(id) {
    try {
        const response = await fetch('/api/v1/scene-groups/' + id);
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            currentGroup = result.data;
            
            const snapshotsResponse = await fetch('/api/v1/scene-groups/' + id + '/snapshots');
            const snapshotsResult = await snapshotsResponse.json();
            if (snapshotsResult.status === 'success') {
                currentGroup.snapshots = snapshotsResult.data || [];
            }
            
            renderSceneGroup();
        } else {
            loadMockSceneGroup();
        }
    } catch (error) {
        console.error('Failed to load scene group:', error);
        loadMockSceneGroup();
    }
}

async function loadLogs() {
    if (!sceneGroupId) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/event-log?limit=50');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            renderLogList(result.data);
        } else {
            renderMockLogs();
        }
    } catch (error) {
        console.error('Failed to load logs:', error);
        renderMockLogs();
    }
}

function renderLogList(logs) {
    const container = document.getElementById('logList');
    if (!logs || logs.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary">暂无日志</p>';
        return;
    }
    
    const eventTypeConfig = {
        'PARTICIPANT_JOIN': { icon: 'ri-user-add-line', color: '#4CAF50' },
        'PARTICIPANT_LEAVE': { icon: 'ri-user-unfollow-line', color: '#f44336' },
        'CAPABILITY_BIND': { icon: 'ri-link', color: '#2196F3' },
        'CAPABILITY_UNBIND': { icon: 'ri-link-unlink', color: '#FF9800' },
        'CAPABILITY_INVOKE': { icon: 'ri-flashlight-line', color: '#9C27B0' },
        'SCENE_CREATE': { icon: 'ri-add-circle-line', color: '#4CAF50' },
        'SCENE_START': { icon: 'ri-play-line', color: '#2196F3' },
        'SCENE_STOP': { icon: 'ri-stop-line', color: '#FF9800' },
        'SCENE_DELETE': { icon: 'ri-delete-bin-line', color: '#f44336' },
        'SNAPSHOT_CREATE': { icon: 'ri-camera-line', color: '#00BCD4' },
        'SNAPSHOT_RESTORE': { icon: 'ri-restore-line', color: '#9C27B0' },
        'STATUS_CHANGE': { icon: 'ri-exchange-line', color: '#FF9800' },
        'ROLE_CHANGE': { icon: 'ri-user-settings-line', color: '#3F51B5' }
    };
    
    let html = '<div class="nx-relative" style="padding-left: 24px;">' +
        '<div style="position: absolute; left: 8px; top: 0; bottom: 0; width: 2px; background: var(--nx-border-color);"></div>';
    
    logs.forEach((log, idx) => {
        const config = eventTypeConfig[log.eventType] || { icon: 'ri-information-line', color: '#666' };
        const statusColor = log.status === 'SUCCESS' ? '#4CAF50' : log.status === 'ERROR' ? '#f44336' : log.status === 'WARN' ? '#FF9800' : '#2196F3';
        
        html += '<div class="nx-relative nx-mb-3">' +
            '<div style="position: absolute; left: -20px; top: 4px; width: 16px; height: 16px; border-radius: 50%; background: ' + statusColor + '; display: flex; align-items: center; justify-content: center;">' +
            '<i class="' + config.icon + '" style="font-size: 10px; color: white;"></i></div>' +
            '<div class="nx-p-3 nx-bg-elevated nx-rounded" style="border-left: 3px solid ' + config.color + ';">' +
            '<div class="nx-flex nx-items-center nx-justify-between nx-mb-1">' +
            '<span class="nx-text-sm nx-font-medium">' + (log.eventType || log.action || '事件') + '</span>' +
            '<span class="nx-text-xs nx-text-secondary">' + formatTime(log.timestamp) + '</span>' +
            '</div>' +
            '<div class="nx-text-sm nx-text-secondary">' + (log.message || '-') + '</div>' +
            (log.participantId ? '<div class="nx-text-xs nx-text-secondary nx-mt-1"><i class="ri-user-line"></i> ' + log.participantId + '</div>' : '') +
            '</div></div>';
    });
    
    html += '</div>';
    container.innerHTML = html;
}

function renderMockLogs() {
    const container = document.getElementById('logList');
    container.innerHTML = '<p class="nx-text-secondary">暂无日志记录</p>';
}

function loadMockSceneGroup() {
    console.warn('[loadMockSceneGroup] No mock data - redirecting to list');
    alert('场景组不存在或已被删除');
    window.location.href = '/console/pages/scene-group-management.html';
}

function renderSceneGroup() {
    document.getElementById('pageTitle').textContent = currentGroup.name;
    document.getElementById('groupName').textContent = currentGroup.name;
    document.getElementById('sceneGroupId').textContent = currentGroup.sceneGroupId;
    document.getElementById('groupTemplate').textContent = currentGroup.templateId || '-';
    document.getElementById('groupDescription').textContent = currentGroup.description || '-';
    document.getElementById('groupCreator').textContent = currentGroup.creatorId || '-';
    document.getElementById('groupCreateTime').textContent = formatTime(currentGroup.createTime);
    
    const statusBadge = document.getElementById('groupStatus');
    statusBadge.textContent = getStatusText(currentGroup.status);
    statusBadge.className = 'nx-badge ' + getStatusBadgeClass(currentGroup.status);
    
    const toggleBtn = document.getElementById('toggleStatusBtn');
    if (currentGroup.status === 'ACTIVE') {
        toggleBtn.innerHTML = '<i class="ri-pause-line"></i> <span>暂停</span>';
    } else {
        toggleBtn.innerHTML = '<i class="ri-play-line"></i> <span>激活</span>';
    }
    
    document.getElementById('memberCount').textContent = currentGroup.participants?.length || 0;
    document.getElementById('bindingCount').textContent = currentGroup.capabilityBindings?.length || 0;
    
    renderParticipantOverview();
    renderParticipants();
    renderNetworkTopology();
    renderCapabilityBindings();
    renderSnapshots();
    renderLogs();
    renderCollaborationFlow();
    renderCollaborationMessages();
    renderCollaborationHistory();
}

function getStatusText(status) {
    const statusMap = { 'ACTIVE': '运行中', 'SUSPENDED': '已暂停', 'CREATING': '创建中', 'DESTROYED': '已销毁' };
    return statusMap[status] || status;
}

function getStatusBadgeClass(status) {
    const classMap = { 'ACTIVE': 'nx-badge--success', 'SUSPENDED': 'nx-badge--warning', 'CREATING': 'nx-badge--info' };
    return classMap[status] || 'nx-badge--secondary';
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', {hour: '2-digit', minute: '2-digit'});
}

function renderParticipantOverview() {
    const container = document.getElementById('participantOverview');
    if (!currentGroup.participants?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无参与者</p>';
        return;
    }
    
    const byRole = {};
    currentGroup.participants.forEach(p => {
        if (!byRole[p.role]) byRole[p.role] = [];
        byRole[p.role].push(p);
    });
    
    const roleConfig = {
        'OWNER': { icon: 'ri-vip-crown-line', color: '#FFD700', bg: '#FFF8E1', label: '创建者' },
        'MANAGER': { icon: 'ri-shield-star-line', color: '#2196F3', bg: '#E3F2FD', label: '管理者' },
        'EMPLOYEE': { icon: 'ri-user-line', color: '#4CAF50', bg: '#E8F5E9', label: '员工' },
        'LLM_ASSISTANT': { icon: 'ri-robot-line', color: '#9C27B0', bg: '#F3E5F5', label: 'LLM助手' },
        'COORDINATOR': { icon: 'ri-git-merge-line', color: '#FF9800', bg: '#FFF3E0', label: '协调者' },
        'OBSERVER': { icon: 'ri-eye-line', color: '#9E9E9E', bg: '#FAFAFA', label: '观察者' }
    };
    
    let html = '';
    Object.entries(byRole).forEach(([role, participants]) => {
        const config = roleConfig[role] || { icon: 'ri-user-line', color: '#666', bg: '#f5f5f5', label: role };
        html += '<div class="nx-mb-4" style="border-left: 3px solid ' + config.color + '; padding-left: 12px;">' +
            '<div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">' +
            '<i class="' + config.icon + '" style="color: ' + config.color + ';"></i>' +
            '<span class="nx-text-sm nx-font-medium">' + config.label + '</span>' +
            '<span class="nx-badge nx-badge--secondary nx-text-xs">' + participants.length + '</span>' +
            '</div>' +
            '<div class="nx-flex nx-flex-wrap nx-gap-2">';
        
        participants.forEach(p => {
            const typeIcon = p.participantType === 'AGENT' ? 'ri-robot-line' : 'ri-user-line';
            const statusColor = p.status === 'ACTIVE' ? '#4CAF50' : p.status === 'SUSPENDED' ? '#f44336' : '#9E9E9E';
            html += '<div class="nx-flex nx-items-center nx-gap-1 nx-px-2 nx-py-1 nx-rounded" style="background: ' + config.bg + ';">' +
                '<span style="width: 6px; height: 6px; border-radius: 50%; background: ' + statusColor + ';"></span>' +
                '<i class="' + typeIcon + ' nx-text-xs"></i>' +
                '<span class="nx-text-sm">' + (p.name || p.participantId) + '</span>' +
                '</div>';
        });
        
        html += '</div></div>';
    });
    container.innerHTML = html;
}

function renderParticipants() {
    const container = document.getElementById('participantList');
    if (!currentGroup.participants?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无参与者</p>';
        return;
    }
    
    const users = allUsers.length > 0 ? allUsers : getDefaultUsers();
    const userMap = {};
    users.forEach(u => { userMap[u.userId] = u; });
    
    let html = '';
    currentGroup.participants.forEach(p => {
        const isAgent = p.participantType === 'AGENT' || p.participantType === 'SUPER_AGENT';
        const avatarIcon = isAgent ? 'ri-robot-line' : 'ri-user-line';
        const avatarBg = isAgent ? 'var(--nx-success-light)' : 'var(--nx-primary-light)';
        
        const userInfo = userMap[p.participantId];
        const displayName = p.userName || p.name || (userInfo ? userInfo.name : p.participantId);
        
        const agentLink = isAgent 
            ? '<a href="agent-detail.html?id=' + p.participantId + '" class="nx-btn nx-btn--ghost nx-btn--sm nx-mr-1" title="查看Agent详情"><i class="ri-external-link-line"></i></a>'
            : '';
        
        html += '<div class="nx-card nx-mb-3">' +
            '<div class="nx-card__body">' +
            '<div class="nx-flex nx-items-center nx-gap-3">' +
            '<div class="nx-flex nx-items-center nx-justify-center" style="width: 40px; height: 40px; background: ' + avatarBg + '; border-radius: 50%;">' +
            '<i class="' + avatarIcon + '"></i></div>' +
            '<div class="nx-flex-1">' +
            '<div class="nx-flex nx-items-center nx-gap-2">' +
            '<span class="nx-font-medium">' + displayName + '</span>' +
            '<span class="nx-badge nx-badge--secondary" id="role-badge-' + p.participantId + '">' + p.role + '</span>' +
            (isAgent ? '<span class="nx-badge nx-badge--info nx-text-xs">Agent</span>' : '') +
            '</div>' +
            '<div class="nx-text-sm nx-text-secondary">类型: ' + p.participantType + (p.participantId !== displayName ? ' | ID: ' + p.participantId : '') + '</div>' +
            '</div>' +
            agentLink +
            '<button class="nx-btn nx-btn--ghost nx-btn--sm nx-mr-1" onclick="showChangeRoleModal(\'' + p.participantId + '\', \'' + p.role + '\')" title="变更角色">' +
            '<i class="ri-user-settings-line"></i></button>' +
            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="removeParticipant(\'' + p.participantId + '\')" title="移除">' +
            '<i class="ri-user-unfollow-line"></i></button>' +
            '</div></div></div>';
    });
    container.innerHTML = html;
}

function renderNetworkTopology() {
    const container = document.getElementById('networkTopology');
    if (!container) return;
    
    const participants = currentGroup.participants || [];
    
    if (participants.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-py-12">暂无参与者</p>';
        return;
    }
    
    const agents = participants.filter(p => 
        p.participantType === 'AGENT' || p.participantType === 'SUPER_AGENT'
    );
    const users = participants.filter(p => p.participantType === 'USER');
    
    const centerX = 300;
    const centerY = 150;
    const radius = 120;
    
    const roleColors = {
        'OWNER': '#FFD700',
        'MANAGER': '#2196F3',
        'EMPLOYEE': '#4CAF50',
        'LLM_ASSISTANT': '#9C27B0',
        'COORDINATOR': '#FF9800',
        'OBSERVER': '#9E9E9E'
    };
    
    const roleLabels = {
        'OWNER': '创建者',
        'MANAGER': '管理者',
        'EMPLOYEE': '员工',
        'LLM_ASSISTANT': 'LLM助手',
        'COORDINATOR': '协调者',
        'OBSERVER': '观察者'
    };
    
    let svg = `<svg width="100%" height="300" viewBox="0 0 600 300" style="overflow: visible;">
        <defs>
            <marker id="arrowhead" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
                <polygon points="0 0, 10 3.5, 0 7" fill="#999"/>
            </marker>
            <filter id="shadow" x="-20%" y="-20%" width="140%" height="140%">
                <feDropShadow dx="1" dy="1" stdDeviation="2" flood-opacity="0.2"/>
            </filter>
        </defs>
        
        <circle cx="${centerX}" cy="${centerY}" r="45" fill="var(--nx-primary-light)" stroke="var(--nx-primary)" stroke-width="2" filter="url(#shadow)"/>
        <text x="${centerX}" y="${centerY - 5}" text-anchor="middle" font-size="11" font-weight="500" fill="var(--nx-text)">场景组</text>
        <text x="${centerX}" y="${centerY + 12}" text-anchor="middle" font-size="9" fill="var(--nx-text-secondary)">${currentGroup.name || sceneGroupId}</text>`;
    
    const allParticipants = [...users, ...agents];
    const totalNodes = allParticipants.length;
    
    allParticipants.forEach((p, idx) => {
        const angle = (2 * Math.PI * idx / totalNodes) - Math.PI / 2;
        const x = centerX + radius * Math.cos(angle);
        const y = centerY + radius * Math.sin(angle);
        
        const role = p.role || 'EMPLOYEE';
        const color = roleColors[role] || '#666';
        const roleLabel = roleLabels[role] || role;
        const isAgent = p.participantType === 'AGENT' || p.participantType === 'SUPER_AGENT';
        const icon = isAgent ? '🤖' : '👤';
        const name = p.name || p.userName || p.participantId;
        const shortName = name.length > 6 ? name.substring(0, 6) + '...' : name;
        
        svg += `<line x1="${centerX}" y1="${centerY}" x2="${x}" y2="${y}" stroke="#ddd" stroke-width="1.5" stroke-dasharray="4,2"/>`;
        
        svg += `<circle cx="${x}" cy="${y}" r="28" fill="white" stroke="${color}" stroke-width="2" filter="url(#shadow)"/>`;
        svg += `<text x="${x}" y="${y - 3}" text-anchor="middle" font-size="14">${icon}</text>`;
        svg += `<text x="${x}" y="${y + 12}" text-anchor="middle" font-size="8" fill="var(--nx-text)">${shortName}</text>`;
        
        svg += `<title>${name} (${roleLabel})</title>`;
    });
    
    agents.forEach((agent, idx) => {
        const otherAgents = agents.filter(a => a.participantId !== agent.participantId);
        otherAgents.forEach(other => {
            const agentIdx = allParticipants.findIndex(p => p.participantId === agent.participantId);
            const otherIdx = allParticipants.findIndex(p => p.participantId === other.participantId);
            
            if (agentIdx < otherIdx) {
                const angle1 = (2 * Math.PI * agentIdx / totalNodes) - Math.PI / 2;
                const angle2 = (2 * Math.PI * otherIdx / totalNodes) - Math.PI / 2;
                const x1 = centerX + radius * Math.cos(angle1);
                const y1 = centerY + radius * Math.sin(angle1);
                const x2 = centerX + radius * Math.cos(angle2);
                const y2 = centerY + radius * Math.sin(angle2);
                
                svg += `<path d="M ${x1} ${y1} Q ${centerX} ${centerY - 30} ${x2} ${y2}" 
                    fill="none" stroke="#9C27B0" stroke-width="1" stroke-opacity="0.4" stroke-dasharray="3,3"/>`;
            }
        });
    });
    
    svg += '</svg>';
    container.innerHTML = svg;
}

function renderCollaborationFlow() {
    const container = document.getElementById('collaborationFlow');
    if (!container) return;
    
    const participants = currentGroup.participants || [];
    const bindings = currentGroup.capabilityBindings || [];
    
    if (participants.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-py-12">暂无参与者，无法展示协作流程</p>';
        return;
    }
    
    const agents = participants.filter(p => p.participantType === 'AGENT' || p.participantType === 'SUPER_AGENT');
    const users = participants.filter(p => p.participantType === 'USER');
    
    let svg = `<svg width="100%" height="250" viewBox="0 0 600 250" style="overflow: visible;">
        <defs>
            <marker id="flowArrow" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
                <polygon points="0 0, 10 3.5, 0 7" fill="#666"/>
            </marker>
        </defs>`;
    
    const steps = [
        { x: 80, y: 125, label: '用户请求', icon: '👤', color: '#2196F3' },
        { x: 200, y: 125, label: '场景协调', icon: '🎯', color: '#FF9800' },
        { x: 350, y: 125, label: '能力调用', icon: '⚡', color: '#4CAF50' },
        { x: 500, y: 125, label: '结果返回', icon: '📤', color: '#9C27B0' }
    ];
    
    steps.forEach((step, idx) => {
        svg += `<rect x="${step.x - 50}" y="${step.y - 40}" width="100" height="80" rx="8" fill="white" stroke="${step.color}" stroke-width="2"/>`;
        svg += `<text x="${step.x}" y="${step.y - 10}" text-anchor="middle" font-size="20">${step.icon}</text>`;
        svg += `<text x="${step.x}" y="${step.y + 15}" text-anchor="middle" font-size="10" fill="var(--nx-text)">${step.label}</text>`;
        
        if (idx < steps.length - 1) {
            const nextStep = steps[idx + 1];
            svg += `<line x1="${step.x + 50}" y1="${step.y}" x2="${nextStep.x - 50}" y2="${nextStep.y}" stroke="#999" stroke-width="1.5" marker-end="url(#flowArrow)"/>`;
        }
    });
    
    if (agents.length > 0) {
        svg += `<text x="350" y="70" text-anchor="middle" font-size="9" fill="var(--nx-text-secondary)">参与Agent: ${agents.length}</text>`;
        agents.slice(0, 3).forEach((agent, idx) => {
            const name = (agent.name || agent.participantId).substring(0, 8);
            svg += `<text x="${300 + idx * 50}" y="85" text-anchor="middle" font-size="8" fill="#9C27B0">${name}</text>`;
        });
    }
    
    if (bindings.length > 0) {
        svg += `<text x="350" y="185" text-anchor="middle" font-size="9" fill="var(--nx-text-secondary)">绑定能力: ${bindings.length}</text>`;
    }
    
    svg += '</svg>';
    container.innerHTML = svg;
}

function renderCollaborationMessages() {
    const container = document.getElementById('collaborationMessages');
    if (!container) return;
    
    const logs = currentGroup.eventLogs || [];
    const collaborationLogs = logs.filter(log => 
        ['CAPABILITY_CALL', 'CAPABILITY_RESULT', 'PARTICIPANT_JOIN', 'PARTICIPANT_LEAVE', 'ROLE_CHANGE'].includes(log.eventType)
    ).slice(0, 20);
    
    if (collaborationLogs.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary">暂无协作消息</p>';
        return;
    }
    
    let html = '<div class="nx-space-y-2">';
    collaborationLogs.forEach(log => {
        const icon = getEventIcon(log.eventType);
        const time = formatTime(log.eventTime);
        html += `<div class="nx-flex nx-items-start nx-gap-2 nx-p-2 nx-rounded" style="background: var(--nx-bg-elevated);">
            <span class="nx-text-lg">${icon}</span>
            <div class="nx-flex-1">
                <div class="nx-flex nx-justify-between nx-items-center">
                    <span class="nx-text-sm nx-font-medium">${log.eventName || log.eventType}</span>
                    <span class="nx-text-xs nx-text-secondary">${time}</span>
                </div>
                <p class="nx-text-xs nx-text-secondary">${log.description || '-'}</p>
            </div>
        </div>`;
    });
    html += '</div>';
    container.innerHTML = html;
}

function renderCollaborationHistory() {
    const container = document.getElementById('collaborationHistory');
    if (!container) return;
    
    const logs = currentGroup.eventLogs || [];
    const historyLogs = logs.filter(log => 
        ['SCENE_CREATE', 'SCENE_ACTIVATE', 'SCENE_DEACTIVATE', 'SNAPSHOT_CREATE', 'SNAPSHOT_RESTORE'].includes(log.eventType)
    );
    
    if (historyLogs.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary">暂无协作历史记录</p>';
        return;
    }
    
    let html = '<table class="nx-table"><thead><tr><th>时间</th><th>事件</th><th>操作者</th><th>描述</th></tr></thead><tbody>';
    historyLogs.forEach(log => {
        html += `<tr>
            <td>${formatTime(log.eventTime)}</td>
            <td><span class="nx-badge nx-badge--info">${log.eventName || log.eventType}</span></td>
            <td>${log.operatorName || log.operatorId || '-'}</td>
            <td>${log.description || '-'}</td>
        </tr>`;
    });
    html += '</tbody></table>';
    container.innerHTML = html;
}

function getEventIcon(eventType) {
    const icons = {
        'CAPABILITY_CALL': '⚡',
        'CAPABILITY_RESULT': '✅',
        'PARTICIPANT_JOIN': '👋',
        'PARTICIPANT_LEAVE': '👋',
        'ROLE_CHANGE': '🔄',
        'SCENE_CREATE': '🎬',
        'SCENE_ACTIVATE': '▶️',
        'SCENE_DEACTIVATE': '⏸️',
        'SNAPSHOT_CREATE': '📸',
        'SNAPSHOT_RESTORE': '🔙'
    };
    return icons[eventType] || '📌';
}

function renderCapabilityBindings() {
    const container = document.getElementById('bindingList');
    if (!currentGroup.capabilityBindings?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无能力绑定</p>';
        return;
    }
    
    const groupedBindings = {};
    currentGroup.capabilityBindings.forEach(b => {
        const key = b.capId;
        if (!groupedBindings[key]) {
            groupedBindings[key] = {
                capId: b.capId,
                capName: b.capName,
                capType: b.capType,
                providers: []
            };
        }
        groupedBindings[key].providers.push(b);
    });
    
    let html = '';
    Object.values(groupedBindings).forEach(group => {
        const mainProvider = group.providers.find(p => p.priority === 1) || group.providers[0];
        const hasMultipleProviders = group.providers.length > 1;
        const isSkill = group.capType === 'SKILL' || group.capId.startsWith('skill-');
        const skillLink = isSkill 
            ? '<a href="skill-detail.html?id=' + group.capId + '" class="nx-btn nx-btn--ghost nx-btn--sm" title="查看Skill详情"><i class="ri-external-link-line"></i></a>'
            : '';
        
        html += '<div class="nx-card nx-mb-3">' +
            '<div class="nx-card__body">' +
            '<div class="nx-flex nx-items-start nx-justify-between">' +
            '<div class="nx-flex-1">' +
            '<div class="nx-flex nx-items-center nx-gap-2 nx-mb-2">' +
            '<i class="ri-flashlight-line" style="color: var(--nx-primary);"></i>' +
            '<span class="nx-font-medium">' + (group.capName || group.capId) + '</span>' +
            (isSkill ? '<span class="nx-badge nx-badge--info nx-text-xs">Skill</span>' : '') +
            '<span class="nx-badge nx-badge--secondary">' + group.providers.length + '个提供者</span>' +
            '</div>' +
            '<div class="nx-text-sm nx-text-secondary nx-mb-2">能力ID: ' + group.capId + '</div>';
        
        html += '<div class="nx-flex nx-flex-col nx-gap-1">';
        group.providers.sort((a, b) => (a.priority || 1) - (b.priority || 1)).forEach((p, idx) => {
            const isPrimary = idx === 0;
            const statusClass = p.status === 'ACTIVE' ? 'nx-badge--success' : p.status === 'ERROR' ? 'nx-badge--danger' : 'nx-badge--secondary';
            const badge = isPrimary ? '<span class="nx-badge nx-badge--primary nx-text-xs">主</span>' : '<span class="nx-badge nx-badge--info nx-text-xs">备用</span>';
            const fallbackIcon = p.fallback ? '<i class="ri-refresh-line" title="故障转移: 已启用"></i>' : '';
            const providerIsAgent = p.providerType === 'AGENT';
            const providerLink = providerIsAgent 
                ? '<a href="agent-detail.html?id=' + p.providerId + '" class="nx-text-sm" style="color: var(--nx-primary);">' + (p.providerType || '-') + ': ' + (p.providerId || '-') + '</a>'
                : '<span class="nx-text-sm">' + (p.providerType || '-') + ': ' + (p.providerId || '-') + '</span>';
            
            html += '<div class="nx-flex nx-items-center nx-gap-2 nx-p-2 nx-bg-elevated nx-rounded">' +
                '<span class="nx-text-xs nx-text-secondary" style="min-width: 20px;">#' + (idx + 1) + '</span>' +
                badge +
                providerLink +
                '<span class="nx-badge ' + statusClass + ' nx-text-xs">' + (p.status || 'ACTIVE') + '</span>' +
                '<span class="nx-text-xs nx-text-secondary">P' + (p.priority || 1) + '</span>' +
                fallbackIcon +
                '</div>';
        });
        html += '</div>';
        
        html += '</div>' +
            '<div class="nx-flex nx-gap-1 nx-ml-2">' +
            skillLink +
            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="showCapabilityDetail(\'' + group.capId + '\')" title="查看详情">' +
            '<i class="ri-information-line"></i></button>' +
            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="unbindCapabilityGroup(\'' + group.capId + '\')" title="解绑所有">' +
            '<i class="ri-link-unlink"></i></button>' +
            '</div>' +
            '</div></div></div>';
    });
    container.innerHTML = html;
}

function unbindCapabilityGroup(capId) {
    if (!confirm('确定要解绑该能力的所有提供者吗？')) return;
    
    const bindings = currentGroup.capabilityBindings?.filter(b => b.capId === capId) || [];
    let removed = 0;
    
    bindings.forEach(async (b) => {
        try {
            const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/capabilities/' + b.bindingId, {
                method: 'DELETE'
            });
            const result = await response.json();
            if (result.status === 'success') {
                removed++;
            }
        } catch (e) {
            console.error('Failed to unbind:', e);
        }
    });
    
    setTimeout(() => {
        loadSceneGroup(sceneGroupId);
    }, 500);
}

function renderSnapshots() {
    const container = document.getElementById('snapshotList');
    if (!currentGroup.snapshots?.length) {
        container.innerHTML = '<p class="nx-text-secondary">暂无快照</p>';
        return;
    }
    
    let html = '';
    currentGroup.snapshots.forEach(s => {
        html += '<div class="nx-card nx-mb-3">' +
            '<div class="nx-card__body">' +
            '<div class="nx-flex nx-items-center nx-justify-between">' +
            '<div>' +
            '<div class="nx-flex nx-items-center nx-gap-2">' +
            '<i class="ri-camera-line" style="color: var(--nx-primary);"></i>' +
            '<span class="nx-font-medium">' + s.snapshotId + '</span>' +
            '</div>' +
            '<div class="nx-text-sm nx-text-secondary nx-mt-1">创建: ' + formatTime(s.createTime) + '</div>' +
            '</div>' +
            '<div class="nx-flex nx-gap-2">' +
            '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="restoreSnapshot(\'' + s.snapshotId + '\')">' +
            '<i class="ri-restore-line"></i> 恢复</button>' +
            '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="deleteSnapshot(\'' + s.snapshotId + '\')">' +
            '<i class="ri-delete-bin-line"></i></button>' +
            '</div></div></div></div>';
    });
    container.innerHTML = html;
}

function renderLogs() {
    loadLogs();
}

function inviteParticipant() {
    selectedParticipant = null;
    document.getElementById('participantForm').reset();
    document.getElementById('participantSelectorContainer').innerHTML = '<p class="nx-text-secondary nx-p-3">请先选择参与者类型</p>';
    document.getElementById('selectedParticipantDisplay').innerHTML = '<span class="nx-text-secondary">-</span>';
    document.getElementById('participantRole').innerHTML = '<option value="">请选择角色</option>';
    document.getElementById('participantModal').classList.add('nx-modal--open');
}

function closeParticipantModal() {
    document.getElementById('participantModal').classList.remove('nx-modal--open');
}

function onParticipantTypeChange() {
    const type = document.getElementById('participantType').value;
    const container = document.getElementById('participantSelectorContainer');
    const roleSelect = document.getElementById('participantRole');
    
    selectedParticipant = null;
    document.getElementById('selectedParticipantDisplay').innerHTML = '<span class="nx-text-secondary">-</span>';
    roleSelect.innerHTML = '<option value="">请选择角色</option>';
    
    if (type === 'USER') {
        renderUserSelector(container);
        roleSelect.innerHTML = '<option value="manager">管理者</option><option value="employee">员工</option><option value="hr">HR</option>';
    } else if (type === 'AGENT') {
        renderAgentSelector(container);
        roleSelect.innerHTML = '<option value="llm-assistant">LLM助手</option><option value="coordinator">协调Agent</option>';
    } else {
        container.innerHTML = '<p class="nx-text-secondary nx-p-3">请先选择参与者类型</p>';
    }
}

function renderUserSelector(container) {
    const users = allUsers.length > 0 ? allUsers : getDefaultUsers();
    
    let html = '<select class="nx-input" id="userSelect" onchange="onUserSelect()"><option value="">请选择用户</option>';
    users.forEach(user => {
        const exists = currentGroup.participants?.some(p => p.participantId === user.userId);
        if (!exists) {
            html += '<option value="' + user.userId + '" data-name="' + (user.name || user.userId) + '">' + (user.name || user.userId) + ' (' + user.userId + ')</option>';
        }
    });
    html += '</select>';
    container.innerHTML = html;
}

function onUserSelect() {
    const select = document.getElementById('userSelect');
    const userId = select.value;
    const userName = select.options[select.selectedIndex].getAttribute('data-name');
    
    if (userId) {
        selectedParticipant = { id: userId, name: userName, type: 'USER' };
        document.getElementById('selectedParticipantDisplay').innerHTML = 
            '<span class="nx-font-medium">' + userName + '</span> <span class="nx-text-secondary">(' + userId + ')</span>';
    } else {
        selectedParticipant = null;
        document.getElementById('selectedParticipantDisplay').innerHTML = '<span class="nx-text-secondary">-</span>';
    }
}

function renderAgentSelector(container) {
    container.innerHTML = '<p class="nx-text-secondary nx-p-3">加载中...</p>';
    
    fetch('/api/agent/list')
        .then(response => response.json())
        .then(result => {
            let agents = [];
            if (result.status === 'success' && result.data) {
                agents = result.data;
            }
            
            if (agents.length === 0) {
                agents = getDefaultAgents();
            }
            
            let html = '<select class="nx-input" id="agentSelect" onchange="onAgentSelect()"><option value="">请选择Agent</option>';
            agents.forEach(agent => {
                const exists = currentGroup.participants?.some(p => p.participantId === agent.agentId);
                if (!exists) {
                    html += '<option value="' + agent.agentId + '" data-name="' + (agent.name || agent.agentId) + '">' + (agent.name || agent.agentId) + ' (' + agent.agentId + ')</option>';
                }
            });
            html += '</select>';
            container.innerHTML = html;
        })
        .catch(error => {
            console.error('Failed to load agents:', error);
            const agents = getDefaultAgents();
            let html = '<select class="nx-input" id="agentSelect" onchange="onAgentSelect()"><option value="">请选择Agent</option>';
            agents.forEach(agent => {
                const exists = currentGroup.participants?.some(p => p.participantId === agent.id);
                if (!exists) {
                    html += '<option value="' + agent.id + '" data-name="' + agent.name + '">' + agent.name + ' (' + agent.id + ')</option>';
                }
            });
            html += '</select>';
            container.innerHTML = html;
        });
}

function getDefaultAgents() {
    return [
        { id: 'agent-llm-001', name: 'LLM分析助手' },
        { id: 'agent-coordinator-001', name: '协调Agent' }
    ];
}

function onAgentSelect() {
    const select = document.getElementById('agentSelect');
    const agentId = select.value;
    const agentName = select.options[select.selectedIndex].getAttribute('data-name');
    
    if (agentId) {
        selectedParticipant = { id: agentId, name: agentName, type: 'AGENT' };
        document.getElementById('selectedParticipantDisplay').innerHTML = 
            '<span class="nx-font-medium">' + agentName + '</span> <span class="nx-text-secondary">(' + agentId + ')</span>';
    } else {
        selectedParticipant = null;
        document.getElementById('selectedParticipantDisplay').innerHTML = '<span class="nx-text-secondary">-</span>';
    }
}

async function saveParticipant() {
    if (!selectedParticipant) {
        alert('请选择参与者');
        return;
    }
    
    const role = document.getElementById('participantRole').value;
    if (!role) {
        alert('请选择角色');
        return;
    }
    
    const participant = {
        participantId: selectedParticipant.id,
        participantType: selectedParticipant.type,
        role: role,
        name: selectedParticipant.name
    };
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/participants', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(participant)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            closeParticipantModal();
            loadSceneGroup(sceneGroupId);
        } else {
            alert('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to save participant:', error);
        alert('移除失败: ' + error.message);
    }
}

let currentEditingParticipantId = null;
let currentEditingParticipantName = null;
let currentEditingRole = null;

const ROLE_CONFIG = {
    'OWNER': { icon: 'ri-vip-crown-line', color: '#FFD700', label: '创建者' },
    'MANAGER': { icon: 'ri-shield-star-line', color: '#2196F3', label: '管理者' },
    'EMPLOYEE': { icon: 'ri-user-line', color: '#4CAF50', label: '员工' },
    'LLM_ASSISTANT': { icon: 'ri-robot-line', color: '#9C27B0', label: 'LLM助手' },
    'COORDINATOR': { icon: 'ri-git-merge-line', color: '#FF9800', label: '协调者' },
    'OBSERVER': { icon: 'ri-eye-line', color: '#9E9E9E', label: '观察者' }
};

function getRoleDisplayName(role) {
    return ROLE_CONFIG[role]?.label || role;
}

function showChangeRoleModal(participantId, currentRole) {
    currentEditingParticipantId = participantId;
    currentEditingRole = currentRole;
    
    const participant = currentGroup.participants?.find(p => p.participantId === participantId);
    currentEditingParticipantName = participant?.name || participant?.userName || participantId;
    
    document.getElementById('roleChangeParticipantName').textContent = currentEditingParticipantName;
    document.getElementById('roleChangeCurrentRole').innerHTML = 
        '<span class="nx-badge" style="background: ' + (ROLE_CONFIG[currentRole]?.color || '#666') + '20; color: ' + (ROLE_CONFIG[currentRole]?.color || '#666') + ';">' + 
        '<i class="' + (ROLE_CONFIG[currentRole]?.icon || 'ri-user-line') + '"></i> ' + 
        getRoleDisplayName(currentRole) + '</span>';
    document.getElementById('newRoleSelect').value = currentRole;
    
    document.getElementById('roleChangeModal').classList.add('nx-modal--open');
}

function closeRoleChangeModal() {
    document.getElementById('roleChangeModal').classList.remove('nx-modal--open');
    currentEditingParticipantId = null;
    currentEditingParticipantName = null;
    currentEditingRole = null;
}

async function confirmRoleChange() {
    const newRole = document.getElementById('newRoleSelect').value;
    
    if (!newRole) {
        alert('请选择新角色');
        return;
    }
    
    if (newRole === currentEditingRole) {
        closeRoleChangeModal();
        return;
    }
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/participants/' + currentEditingParticipantId + '/role', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newRole: newRole })
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            closeRoleChangeModal();
            loadSceneGroup(sceneGroupId);
        } else {
            alert('角色变更失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to change role:', error);
        alert('角色变更失败: ' + error.message);
    }
}

async function removeParticipant(participantId) {
    if (!confirm('确定要移除此参与者吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/participants/' + participantId, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
        } else {
            alert('移除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to remove participant:', error);
        alert('移除失败: ' + error.message);
    }
}

function bindCapability() {
    selectedCapability = null;
    document.getElementById('capabilityForm').reset();
    
    const capSelect = document.getElementById('capabilitySelect');
    capSelect.innerHTML = '<option value="">请选择能力</option>';
    
    const caps = availableCapabilities.length > 0 ? availableCapabilities : getDefaultCapabilities();
    caps.forEach(cap => {
        const exists = currentGroup.capabilityBindings?.some(b => b.capId === cap.id);
        if (!exists) {
            capSelect.innerHTML += '<option value="' + cap.id + '" data-name="' + cap.name + '">' + cap.name + ' (' + cap.type + ')</option>';
        }
    });
    
    const providerSelect = document.getElementById('providerSelect');
    providerSelect.innerHTML = '<option value="">请选择提供者</option>';
    
    const providerType = document.getElementById('providerType').value;
    fetch('/api/v1/selectors/providers?type=' + providerType)
        .then(response => response.json())
        .then(result => {
            if (result.status === 'success' && result.data) {
                result.data.forEach(provider => {
                    providerSelect.innerHTML += '<option value="' + provider.id + '">' + provider.name + ' (' + provider.type + ')</option>';
                });
            }
        })
        .catch(error => {
            console.error('Failed to load providers:', error);
            providerSelect.innerHTML += '<option value="skill-daily-report">日报Skill</option>';
            providerSelect.innerHTML += '<option value="agent-llm">LLM Agent</option>';
        });
    
    document.getElementById('capabilityModal').classList.add('nx-modal--open');
}

function closeCapabilityModal() {
    document.getElementById('capabilityModal').classList.remove('nx-modal--open');
}

function onCapabilitySelect() {
    const select = document.getElementById('capabilitySelect');
    const capId = select.value;
    const capName = select.options[select.selectedIndex].getAttribute('data-name');
    
    if (capId) {
        selectedCapability = { id: capId, name: capName };
    } else {
        selectedCapability = null;
    }
}

async function saveCapabilityBinding() {
    if (!selectedCapability) {
        alert('请选择能力');
        return;
    }
    
    const providerId = document.getElementById('providerSelect').value;
    if (!providerId) {
        alert('请选择提供者');
        return;
    }
    
    const priority = parseInt(document.getElementById('bindingPriority').value) || 1;
    const fallback = document.getElementById('bindingFallback').checked;
    
    const binding = {
        capId: selectedCapability.id,
        capName: selectedCapability.name,
        providerType: document.getElementById('providerType').value,
        providerId: providerId,
        priority: priority,
        fallback: fallback
    };
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/capabilities', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(binding)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            closeCapabilityModal();
            loadSceneGroup(sceneGroupId);
        } else {
            alert('绑定失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to bind capability:', error);
        alert('绑定失败: ' + error.message);
    }
}

async function unbindCapability(bindingId) {
    if (!confirm('确定要解绑此能力吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/capabilities/' + bindingId, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
        } else {
            alert('解绑失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to unbind capability:', error);
        alert('解绑失败: ' + error.message);
    }
}

let currentEditingBindingId = null;

function editCapabilityBinding(bindingId) {
    const binding = currentGroup.capabilityBindings?.find(b => b.bindingId === bindingId);
    if (!binding) {
        alert('找不到能力绑定');
        return;
    }
    
    currentEditingBindingId = bindingId;
    
    document.getElementById('bindingPriority').value = binding.priority || 1;
    document.getElementById('bindingFallback').checked = binding.fallback !== false;
    
    const newPriority = prompt('请输入新优先级 (1-100):', binding.priority || 1);
    if (newPriority !== null) {
        const priority = parseInt(newPriority) || 1;
        updateCapabilityBindingConfig(bindingId, priority, binding.fallback !== false);
    }
}

async function updateCapabilityBindingConfig(bindingId, priority, fallback) {
    try {
        const binding = currentGroup.capabilityBindings?.find(b => b.bindingId === bindingId);
        if (!binding) return;
        
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/capabilities/' + bindingId, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                priority: priority,
                fallback: fallback
            })
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
        } else {
            alert('更新失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to update capability binding:', error);
        alert('更新失败: ' + error.message);
    }
}

async function startWorkflow() {
    if (!confirm('确定要启动工作流吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/workflow/start', {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            alert('工作流已启动: ' + (result.data?.message || '成功'));
            loadSceneGroup(sceneGroupId);
        } else {
            alert('启动失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to start workflow:', error);
        alert('启动失败: ' + error.message);
    }
}

async function createSnapshot() {
    const description = prompt('请输入快照描述（可选）:');
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/snapshots', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ description: description || '手动创建' })
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
        } else {
            alert('创建快照失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to create snapshot:', error);
        alert('创建快照失败: ' + error.message);
    }
}

async function restoreSnapshot(snapshotId) {
    if (!confirm('确定要恢复到此快照吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/snapshots/' + snapshotId + '/restore', {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
            alert('快照已恢复');
        } else {
            alert('恢复失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to restore snapshot:', error);
        alert('恢复失败: ' + error.message);
    }
}

async function deleteSnapshot(snapshotId) {
    if (!confirm('确定要删除此快照吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/snapshots/' + snapshotId, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadSceneGroup(sceneGroupId);
        } else {
            alert('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to delete snapshot:', error);
        alert('删除失败: ' + error.message);
    }
}

async function toggleStatus() {
    const action = currentGroup.status === 'ACTIVE' ? 'deactivate' : 'activate';
    const confirmMsg = currentGroup.status === 'ACTIVE' ? '确定要暂停此场景组吗？' : '确定要激活此场景组吗？';
    
    if (!confirm(confirmMsg)) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/' + action, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            currentGroup.status = action === 'activate' ? 'ACTIVE' : 'SUSPENDED';
            renderSceneGroup();
        } else {
            alert('操作失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to toggle status:', error);
        alert('操作失败: ' + error.message);
    }
}

async function showCapabilityDetail(capId) {
    const bindings = currentGroup.capabilityBindings?.filter(b => b.capId === capId) || [];
    if (bindings.length === 0) {
        alert('找不到能力绑定');
        return;
    }
    
    const group = {
        capId: capId,
        capName: bindings[0].capName,
        providers: bindings
    };
    
    currentDetailBinding = group;
    
    document.getElementById('capabilityDetailTitle').textContent = group.capName || capId;
    
    document.getElementById('detailCapId').textContent = capId;
    document.getElementById('detailCapName').textContent = group.capName || '-';
    document.getElementById('detailCapType').textContent = '-';
    document.getElementById('detailCapCategory').textContent = '-';
    document.getElementById('detailCapDescription').textContent = '加载中...';
    
    let providerHtml = '<table class="nx-table"><thead><tr><th>#</th><th>角色</th><th>提供者类型</th><th>提供者ID</th><th>优先级</th><th>故障转移</th><th>状态</th></tr></thead><tbody>';
    group.providers.sort((a, b) => (a.priority || 1) - (b.priority || 1)).forEach((p, idx) => {
        const isPrimary = idx === 0;
        const role = isPrimary ? '<span class="nx-badge nx-badge--primary">主</span>' : '<span class="nx-badge nx-badge--info">备用</span>';
        const statusClass = p.status === 'ACTIVE' ? 'nx-badge--success' : 'nx-badge--secondary';
        const fallback = p.fallback ? '<i class="ri-check-line" style="color: var(--nx-success)"></i>' : '<i class="ri-close-line" style="color: var(--nx-text-secondary)"></i>';
        
        providerHtml += '<tr>' +
            '<td>' + (idx + 1) + '</td>' +
            '<td>' + role + '</td>' +
            '<td>' + (p.providerType || '-') + '</td>' +
            '<td>' + (p.providerId || '-') + '</td>' +
            '<td>P' + (p.priority || 1) + '</td>' +
            '<td>' + fallback + '</td>' +
            '<td><span class="nx-badge ' + statusClass + '">' + (p.status || 'ACTIVE') + '</span></td>' +
            '</tr>';
    });
    providerHtml += '</tbody></table>';
    document.getElementById('detailParameters').innerHTML = providerHtml;
    
    try {
        const response = await fetch('/api/v1/scene/capabilities/' + capId);
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            const cap = result.data;
            document.getElementById('detailCapType').textContent = cap.typeName || cap.type || '-';
            document.getElementById('detailCapCategory').textContent = cap.categoryName || cap.category || '-';
            document.getElementById('detailCapDescription').textContent = cap.description || '暂无描述';
        } else {
            document.getElementById('detailCapDescription').textContent = '暂无描述';
        }
    } catch (e) {
        console.error('Failed to load capability details:', e);
        document.getElementById('detailCapDescription').textContent = '加载失败';
    }
    
    loadCapabilityLogs(capId);
    
    initDetailTabs();
    
    document.getElementById('capabilityDetailModal').classList.add('nx-modal--open');
}

function initDetailTabs() {
    const tabs = document.querySelectorAll('[data-detail-tab]');
    tabs.forEach(tab => {
        tab.removeEventListener('click', handleDetailTabClick);
        tab.addEventListener('click', handleDetailTabClick);
    });
    
    tabs.forEach(t => t.classList.remove('nx-tabs__tab--active'));
    tabs[0]?.classList.add('nx-tabs__tab--active');
    
    document.querySelectorAll('.detail-tab-content').forEach(content => {
        content.style.display = 'none';
    });
    const firstTabId = tabs[0]?.getAttribute('data-detail-tab');
    if (firstTabId) {
        const firstContent = document.getElementById('detail-tab-' + firstTabId);
        if (firstContent) firstContent.style.display = 'block';
    }
}

function handleDetailTabClick(event) {
    const clickedTab = event.currentTarget;
    const tabId = clickedTab.getAttribute('data-detail-tab');
    
    document.querySelectorAll('[data-detail-tab]').forEach(t => t.classList.remove('nx-tabs__tab--active'));
    clickedTab.classList.add('nx-tabs__tab--active');
    
    document.querySelectorAll('.detail-tab-content').forEach(content => {
        content.style.display = 'none';
    });
    
    const targetContent = document.getElementById('detail-tab-' + tabId);
    if (targetContent) {
        targetContent.style.display = 'block';
    }
}

function closeCapabilityDetailModal() {
    document.getElementById('capabilityDetailModal').classList.remove('nx-modal--open');
}

async function loadCapabilityLogs(capId) {
    if (!sceneGroupId) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/event-log?limit=20');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            const logs = result.data.filter(log => log.action && log.action.includes(capId));
            renderCapabilityLogs(logs);
        } else {
            document.getElementById('detailCapabilityLogs').innerHTML = '<p class="nx-text-secondary">暂无调用日志</p>';
        }
    } catch (error) {
        console.error('Failed to load capability logs:', error);
        document.getElementById('detailCapabilityLogs').innerHTML = '<p class="nx-text-secondary">暂无调用日志</p>';
    }
}

function renderCapabilityLogs(logs) {
    const container = document.getElementById('detailCapabilityLogs');
    if (!logs || logs.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary">暂无调用日志</p>';
        return;
    }
    
    let html = '<div class="nx-flex nx-flex-col nx-gap-2" style="max-height: 300px; overflow-y: auto;">';
    logs.forEach(log => {
        const levelClass = log.status === 'ERROR' ? 'nx-badge--danger' : 
                          log.status === 'WARN' ? 'nx-badge--warning' : 
                          log.status === 'SUCCESS' ? 'nx-badge--success' : 'nx-badge--info';
        html += '<div class="nx-flex nx-items-start nx-gap-3 nx-p-2 nx-bg-elevated nx-rounded">' +
            '<span class="nx-text-sm nx-text-secondary" style="min-width: 80px;">' + formatTime(log.timestamp) + '</span>' +
            '<span class="nx-badge ' + levelClass + '">' + (log.status || 'INFO') + '</span>' +
            '<span class="nx-text-sm">' + (log.message || '-') + '</span>' +
            '</div>';
    });
    html += '</div>';
    container.innerHTML = html;
}

function refreshCapabilityLogs() {
    if (currentDetailBinding) {
        loadCapabilityLogs(currentDetailBinding.capId);
    }
}

let availableKnowledgeBases = [];

async function bindKnowledge() {
    document.getElementById('knowledgeBindModal').classList.add('nx-modal--open');
    
    const knowledgeSelect = document.getElementById('knowledgeSelect');
    knowledgeSelect.innerHTML = '<option value="">加载中...</option>';
    
    try {
        const response = await fetch('/api/v1/knowledge-bases');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            availableKnowledgeBases = result.data;
            
            if (availableKnowledgeBases.length === 0) {
                knowledgeSelect.innerHTML = '<option value="">暂无可用知识库</option>';
                return;
            }
            
            knowledgeSelect.innerHTML = '<option value="">请选择知识库</option>';
            availableKnowledgeBases.forEach(kb => {
                const bound = currentGroup.knowledgeBases?.some(b => b.kbId === kb.kbId);
                if (!bound) {
                    knowledgeSelect.innerHTML += '<option value="' + kb.kbId + '" data-name="' + kb.name + '">' + 
                        kb.name + ' (' + kb.kbId + ')</option>';
                }
            });
            
            if (knowledgeSelect.options.length === 1) {
                knowledgeSelect.innerHTML = '<option value="">所有知识库已绑定</option>';
            }
        } else {
            knowledgeSelect.innerHTML = '<option value="">加载失败</option>';
        }
    } catch (error) {
        console.error('Failed to load knowledge bases:', error);
        knowledgeSelect.innerHTML = '<option value="">加载失败: ' + error.message + '</option>';
    }
}

function closeKnowledgeBindModal() {
    document.getElementById('knowledgeBindModal').classList.remove('nx-modal--open');
}

async function confirmKnowledgeBind() {
    const kbId = document.getElementById('knowledgeSelect').value;
    const layer = document.getElementById('knowledgeLayer').value;
    const priority = parseInt(document.getElementById('knowledgePriority').value) || 0;
    
    if (!kbId) {
        alert('请选择知识库');
        return;
    }
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/knowledge', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ kbId: kbId, layer: layer, priority: priority })
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            closeKnowledgeBindModal();
            loadKnowledgeBindings();
        } else {
            alert('绑定失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to bind knowledge:', error);
        alert('绑定失败: ' + error.message);
    }
}

async function loadKnowledgeBindings() {
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/knowledge');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            renderKnowledgeBindings(result.data);
        }
    } catch (error) {
        console.error('Failed to load knowledge bindings:', error);
    }
}

function renderKnowledgeBindings(bindings) {
    const generalList = document.getElementById('generalKnowledgeList');
    const professionalList = document.getElementById('professionalKnowledgeList');
    const sceneList = document.getElementById('sceneKnowledgeList');
    
    const byLayer = { GENERAL: [], PROFESSIONAL: [], SCENE: [] };
    (bindings || []).forEach(b => {
        if (byLayer[b.layer]) {
            byLayer[b.layer].push(b);
        }
    });
    
    generalList.innerHTML = renderKnowledgeList(byLayer.GENERAL, 'GENERAL');
    professionalList.innerHTML = renderKnowledgeList(byLayer.PROFESSIONAL, 'PROFESSIONAL');
    sceneList.innerHTML = renderKnowledgeList(byLayer.SCENE, 'SCENE');
}

function renderKnowledgeList(items, layer) {
    if (!items || items.length === 0) {
        return '<p class="nx-text-secondary nx-text-sm">暂无绑定</p>';
    }
    
    return items.map(item => 
        '<div class="nx-flex nx-items-center nx-justify-between nx-p-2 nx-bg nx-rounded nx-mb-2">' +
        '<div>' +
        '<span class="nx-font-medium">' + (item.kbName || item.kbId) + '</span>' +
        '<span class="nx-text-secondary nx-text-sm nx-ml-2">' + item.kbId + '</span>' +
        '</div>' +
        '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="unbindKnowledge(\'' + item.kbId + '\', \'' + layer + '\')">' +
        '<i class="ri-delete-bin-line"></i>' +
        '</button>' +
        '</div>'
    ).join('');
}

async function unbindKnowledge(kbId, layer) {
    if (!confirm('确定要解绑此知识库吗？')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/knowledge/' + kbId + '?layer=' + layer, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadKnowledgeBindings();
        } else {
            alert('解绑失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to unbind knowledge:', error);
        alert('解绑失败: ' + error.message);
    }
}

async function saveKnowledgeConfig() {
    const config = {
        topK: parseInt(document.getElementById('knowledgeTopK').value) || 5,
        threshold: parseFloat(document.getElementById('knowledgeThreshold').value) || 0.7,
        crossLayerSearch: document.getElementById('crossLayerSearch').value === 'true'
    };
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/knowledge/config', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(config)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            alert('配置已保存');
        } else {
            alert('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to save knowledge config:', error);
        alert('保存失败: ' + error.message);
    }
}

async function loadLlmConfig() {
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/llm/config');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            const config = result.data;
            
            const isSystemDefault = config.isSystemDefault === true;
            
            const statusIndicator = document.getElementById('llmConfigStatus');
            if (statusIndicator) {
                if (isSystemDefault) {
                    statusIndicator.innerHTML = '<span class="nx-badge nx-badge--info"><i class="ri-settings-3-line"></i> 使用系统默认配置</span>';
                } else {
                    statusIndicator.innerHTML = '<span class="nx-badge nx-badge--success"><i class="ri-check-line"></i> 自定义配置</span>';
                }
            }
            
            if (config.provider) document.getElementById('llmProvider').value = config.provider;
            if (config.model) document.getElementById('llmModel').value = config.model;
            if (config.decisionMode) document.getElementById('decisionMode').value = config.decisionMode;
            if (config.decisionTimeout) document.getElementById('decisionTimeout').value = config.decisionTimeout;
            if (config.decisionCache !== undefined) document.getElementById('decisionCache').value = config.decisionCache.toString();
            if (config.cacheTtl) document.getElementById('cacheTtl').value = config.cacheTtl;
            if (config.functionCalling !== undefined) document.getElementById('functionCalling').value = config.functionCalling.toString();
            if (config.maxIterations) document.getElementById('maxIterations').value = config.maxIterations;
            if (config.llmTimeout) document.getElementById('llmTimeout').value = config.llmTimeout;
            if (config.dailyTokenLimit) document.getElementById('dailyTokenLimit').value = config.dailyTokenLimit;
            if (config.usedTokens !== undefined) document.getElementById('usedTokens').value = config.usedTokens;
            if (config.remainingTokens !== undefined) document.getElementById('remainingTokens').value = config.remainingTokens;
            
            loadLlmProviders(config.provider);
        }
    } catch (error) {
        console.error('Failed to load LLM config:', error);
    }
}

async function loadLlmProviders(currentProvider) {
    try {
        const response = await fetch('/api/v1/llm-providers/providers?configuredOnly=true');
        const result = await response.json();
        
        const providerSelect = document.getElementById('llmProvider');
        providerSelect.innerHTML = '<option value="">请选择提供者</option>';
        
        if (result.status === 'success' && result.data) {
            if (result.data.length === 0) {
                providerSelect.innerHTML = '<option value="">暂无可用Provider，请先配置API Key</option>';
                return;
            }
            
            result.data.forEach(p => {
                const option = document.createElement('option');
                option.value = p.providerId;
                option.textContent = p.name;
                if (p.providerId === currentProvider) option.selected = true;
                providerSelect.appendChild(option);
            });
        }
        
        if (currentProvider) {
            loadLlmModels(currentProvider);
        }
    } catch (error) {
        console.error('Failed to load LLM providers:', error);
    }
}

async function loadLlmModels(providerId) {
    const modelSelect = document.getElementById('llmModel');
    modelSelect.innerHTML = '<option value="">加载中...</option>';
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/llm/providers/' + providerId + '/models');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            modelSelect.innerHTML = result.data.map(m => 
                '<option value="' + m.id + '">' + m.name + '</option>'
            ).join('');
        } else {
            modelSelect.innerHTML = getDefaultModelsForProvider(providerId).map(m => 
                '<option value="' + m.value + '">' + m.label + '</option>'
            ).join('');
        }
    } catch (error) {
        console.error('Failed to load models:', error);
        modelSelect.innerHTML = getDefaultModelsForProvider(providerId).map(m => 
            '<option value="' + m.value + '">' + m.label + '</option>'
        ).join('');
    }
}

function onLlmProviderChange() {
    const provider = document.getElementById('llmProvider').value;
    loadLlmModels(provider);
}

function getDefaultModelsForProvider(provider) {
    const models = {
        deepseek: [
            { value: 'deepseek-chat', label: 'DeepSeek Chat' },
            { value: 'deepseek-coder', label: 'DeepSeek Coder' }
        ],
        baidu: [
            { value: 'ernie-bot-4', label: 'ERNIE Bot 4.0' },
            { value: 'ernie-bot-turbo', label: 'ERNIE Bot Turbo' }
        ],
        openai: [
            { value: 'gpt-4-turbo', label: 'GPT-4 Turbo' },
            { value: 'gpt-3.5-turbo', label: 'GPT-3.5 Turbo' }
        ],
        qianwen: [
            { value: 'qwen-turbo', label: '通义千问 Turbo' },
            { value: 'qwen-plus', label: '通义千问 Plus' }
        ]
    };
    
    return models[provider] || [];
}

async function saveLlmConfig() {
    const config = {
        provider: document.getElementById('llmProvider').value,
        model: document.getElementById('llmModel').value,
        decisionMode: document.getElementById('decisionMode').value,
        decisionTimeout: parseInt(document.getElementById('decisionTimeout').value) || 30000,
        decisionCache: document.getElementById('decisionCache').value === 'true',
        cacheTtl: parseInt(document.getElementById('cacheTtl').value) || 300000,
        functionCalling: document.getElementById('functionCalling').value === 'true',
        maxIterations: parseInt(document.getElementById('maxIterations').value) || 5,
        llmTimeout: parseInt(document.getElementById('llmTimeout').value) || 60000,
        dailyTokenLimit: parseInt(document.getElementById('dailyTokenLimit').value) || 100000
    };
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/llm/config', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(config)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            alert('配置已保存');
        } else {
            alert('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to save LLM config:', error);
        alert('保存失败: ' + error.message);
    }
}

async function testLlmConnection() {
    const provider = document.getElementById('llmProvider').value;
    const model = document.getElementById('llmModel').value;
    
    if (!provider) {
        alert('请先选择提供者');
        return;
    }
    
    try {
        const response = await fetch('/api/v1/llm-providers/providers/' + provider + '/test', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ model: model })
        });
        
        const result = await response.json();
        
        if (result.status === 'success' && result.data && result.data.success) {
            alert('连接测试成功: ' + (result.data.message || 'OK'));
        } else {
            alert('连接测试失败: ' + (result.data?.message || result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to test LLM connection:', error);
        alert('连接测试失败: ' + error.message);
    }
}

async function resetLlmConfig() {
    if (!confirm('确定要重置为系统默认配置吗？当前的自定义配置将被清除。')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/llm/reset', {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            loadLlmConfig();
            alert('已重置为系统默认配置');
        } else {
            alert('重置失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('Failed to reset LLM config:', error);
        alert('重置失败: ' + error.message);
    }
}
