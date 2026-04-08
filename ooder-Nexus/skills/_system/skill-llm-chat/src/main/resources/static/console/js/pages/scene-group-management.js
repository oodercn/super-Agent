let sceneGroups = [];
let templates = [];
let filteredGroups = [];

async function initPage() {
    await loadTemplates();
    await refreshSceneGroups();
}

async function loadTemplates() {
    try {
        const response = await fetch('/api/v1/templates?pageNum=1&pageSize=100');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            templates = result.data.list || result.data || [];
            populateTemplateSelects();
        }
    } catch (e) {
        console.error('Failed to load templates:', e);
        templates = [];
    }
}

function populateTemplateSelects() {
    const filterSelect = document.getElementById('template-filter');
    const formSelect = document.getElementById('group-template');
    
    filterSelect.innerHTML = '<option value="">全部模板</option>';
    formSelect.innerHTML = '<option value="">请选择模板</option>';
    
    templates.forEach(t => {
        const opt1 = document.createElement('option');
        opt1.value = t.templateId || t.id;
        opt1.textContent = t.name;
        filterSelect.appendChild(opt1);
        
        const opt2 = document.createElement('option');
        opt2.value = t.templateId || t.id;
        opt2.textContent = t.name;
        formSelect.appendChild(opt2);
    });
}

async function refreshSceneGroups() {
    const container = document.getElementById('scene-group-list');
    container.innerHTML = '<div class="nx-flex nx-items-center nx-justify-center nx-p-8"><i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i><span class="nx-ml-2">加载中...</span></div>';
    
    try {
        const response = await fetch('/api/v1/scene-groups');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            sceneGroups = result.data.list || result.data || [];
            filteredGroups = sceneGroups;
            renderSceneGroups();
            updateStats();
        } else {
            container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">加载失败: ' + (result.message || '未知错误') + '</p>';
        }
    } catch (e) {
        console.error('Failed to load scene groups:', e);
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">加载失败: ' + e.message + '</p>';
    }
}

function renderSceneGroups() {
    const container = document.getElementById('scene-group-list');
    
    if (filteredGroups.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无场景组数据</p>';
        return;
    }
    
    let html = '<div class="nx-table-container"><table class="nx-table">';
    html += '<thead><tr><th>场景组名称</th><th>模板</th><th>状态</th><th>成员数</th><th>创建时间</th><th>操作</th></tr></thead><tbody>';
    
    filteredGroups.forEach(group => {
        const statusBadge = getStatusBadge(group.status);
        const template = templates.find(t => (t.templateId || t.id) === (group.templateId || group.template));
        
        html += '<tr>';
        html += '<td><div class="nx-flex nx-items-center nx-gap-2"><i class="ri-folder-line" style="color: var(--nx-primary);"></i><span class="nx-font-medium">' + (group.name || group.sceneGroupId) + '</span></div></td>';
        html += '<td><span class="nx-badge nx-badge--secondary">' + (template ? template.name : group.templateId || '-') + '</span></td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>' + (group.memberCount || 0) + '</td>';
        html += '<td>' + formatTime(group.createTime || group.createdAt) + '</td>';
        html += '<td>';
        html += '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="viewSceneGroup(\'' + (group.sceneGroupId || group.id) + '\')" title="查看"><i class="ri-eye-line"></i></button> ';
        html += '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="editSceneGroup(\'' + (group.sceneGroupId || group.id) + '\')" title="编辑"><i class="ri-edit-line"></i></button> ';
        html += '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="toggleStatus(\'' + (group.sceneGroupId || group.id) + '\', \'' + group.status + '\')" title="' + (group.status === 'ACTIVE' ? '停用' : '激活') + '"><i class="' + (group.status === 'ACTIVE' ? 'ri-pause-line' : 'ri-play-line') + '"></i></button> ';
        html += '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteSceneGroup(\'' + (group.sceneGroupId || group.id) + '\')" title="删除"><i class="ri-delete-bin-line"></i></button>';
        html += '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table></div>';
    container.innerHTML = html;
}

function getStatusBadge(status) {
    const badges = {
        'ACTIVE': '<span class="nx-badge nx-badge--success">活跃</span>',
        'SUSPENDED': '<span class="nx-badge nx-badge--warning">已暂停</span>',
        'CREATING': '<span class="nx-badge nx-badge--info">创建中</span>',
        'DESTROYED': '<span class="nx-badge nx-badge--danger">已销毁</span>'
    };
    return badges[status] || '<span class="nx-badge nx-badge--secondary">' + (status || '未知') + '</span>';
}

function updateStats() {
    document.getElementById('total-groups').textContent = sceneGroups.length;
    document.getElementById('active-groups').textContent = sceneGroups.filter(g => g.status === 'ACTIVE').length;
    document.getElementById('total-members').textContent = sceneGroups.reduce((sum, g) => sum + (g.memberCount || 0), 0);
    document.getElementById('total-bindings').textContent = sceneGroups.length * 3;
}

function formatTime(timestamp) {
    if (!timestamp) return '-';
    const date = new Date(timestamp);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
}

function createSceneGroup() {
    document.getElementById('modal-title').textContent = '创建场景组';
    document.getElementById('scene-group-form').reset();
    document.getElementById('group-id').value = '';
    document.getElementById('scene-group-modal').classList.add('nx-modal--open');
}

let editingSceneGroupId = null;

async function editSceneGroup(sceneGroupId) {
    editingSceneGroupId = sceneGroupId;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId);
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            const group = result.data;
            document.getElementById('modal-title').textContent = '编辑场景组';
            document.getElementById('group-id').value = group.sceneGroupId;
            document.getElementById('group-name').value = group.name || '';
            document.getElementById('group-template').value = group.templateId || '';
            document.getElementById('group-description').value = group.description || '';
            document.getElementById('min-members').value = group.config?.minMembers || 1;
            document.getElementById('max-members').value = group.config?.maxMembers || 100;
            document.getElementById('scene-group-modal').classList.add('nx-modal--open');
        } else {
            alert('获取场景组信息失败: ' + (result.message || '未知错误'));
        }
    } catch (e) {
        console.error('Failed to load scene group:', e);
        alert('获取场景组信息失败: ' + e.message);
    }
}

function closeModal() {
    document.getElementById('scene-group-modal').classList.remove('nx-modal--open');
}

async function saveSceneGroup() {
    const name = document.getElementById('group-name').value;
    const templateId = document.getElementById('group-template').value;
    const sceneGroupId = document.getElementById('group-id').value;
    
    if (!name) {
        alert('请填写场景组名称');
        return;
    }
    
    if (!sceneGroupId && !templateId) {
        alert('请选择模板');
        return;
    }
    
    const currentUser = NX.userMenu.getCurrentUser ? NX.userMenu.getCurrentUser() : null;
    const userId = currentUser ? currentUser.userId : 'default-user';
    
    const config = {
        name: name,
        description: document.getElementById('group-description').value,
        minMembers: parseInt(document.getElementById('min-members').value) || 1,
        maxMembers: parseInt(document.getElementById('max-members').value) || 100,
        creatorId: userId,
        creatorType: 'USER'
    };
    
    try {
        let response;
        
        if (sceneGroupId) {
            response = await fetch('/api/v1/scene-groups/' + sceneGroupId, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ config: config })
            });
        } else {
            const request = {
                templateId: templateId,
                config: config
            };
            response = await fetch('/api/v1/scene-groups', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(request)
            });
        }
        
        const result = await response.json();
        
        if (result.status === 'success') {
            closeModal();
            refreshSceneGroups();
        } else {
            alert('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (e) {
        console.error('Failed to save scene group:', e);
        alert('保存失败: ' + e.message);
    }
}

function viewSceneGroup(sceneGroupId) {
    window.location.href = 'scene-group-detail.html?id=' + sceneGroupId;
}

async function toggleStatus(sceneGroupId, currentStatus) {
    const action = currentStatus === 'ACTIVE' ? 'deactivate' : 'activate';
    const confirmMsg = currentStatus === 'ACTIVE' ? '确定要停用此场景组吗？' : '确定要激活此场景组吗？';
    
    if (!confirm(confirmMsg)) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId + '/' + action, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            refreshSceneGroups();
        } else {
            alert('操作失败: ' + (result.message || '未知错误'));
        }
    } catch (e) {
        console.error('Failed to toggle status:', e);
        alert('操作失败: ' + e.message);
    }
}

async function deleteSceneGroup(sceneGroupId) {
    if (!confirm('确定要删除此场景组吗？此操作不可恢复。')) return;
    
    try {
        const response = await fetch('/api/v1/scene-groups/' + sceneGroupId, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            refreshSceneGroups();
        } else {
            alert('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (e) {
        console.error('Failed to delete scene group:', e);
        alert('删除失败: ' + e.message);
    }
}

function filterByTemplate() {
    const templateId = document.getElementById('template-filter').value;
    const statusFilter = document.getElementById('status-filter').value;
    
    filteredGroups = sceneGroups.filter(g => {
        const matchTemplate = !templateId || g.templateId === templateId;
        const matchStatus = !statusFilter || g.status === statusFilter;
        return matchTemplate && matchStatus;
    });
    
    renderSceneGroups();
}

function filterByStatus() {
    filterByTemplate();
}

document.addEventListener('DOMContentLoaded', initPage);
