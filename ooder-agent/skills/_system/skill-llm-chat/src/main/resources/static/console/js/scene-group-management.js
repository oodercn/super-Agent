// 场景组管理 JavaScript

var sceneGroupTable = null;
var currentSceneGroupId = null;

function initSceneGroupManagementPage() {
    console.log('[SceneGroupManagement] 初始化场景组管理页面');

    if (typeof initMenu === 'function') {
        initMenu('admin-scene-group-management');
    }

    initSceneGroupTable();
}

function initSceneGroupTable() {
    console.log('[SceneGroupManagement] 开始初始化场景组表格');

    if (!utils || !utils.DataTable) {
        console.error('[SceneGroupManagement] DataTable 组件未加载');
        return;
    }

    const tableBody = document.getElementById('sceneGroupTableBody');
    if (!tableBody) {
        console.error('[SceneGroupManagement] 找不到表格体元素');
        return;
    }

    sceneGroupTable = utils.DataTable({
        tableId: 'sceneGroupTableBody',
        apiUrl: '/scene-groups/list',
        pageName: 'SceneGroupManagement',
        columns: [
            { field: 'sceneGroupId', title: '场景组ID', width: '120px' },
            { field: 'sceneId', title: '场景ID', width: '120px' },
            { field: 'name', title: '名称', width: '150px' },
            { 
                field: 'status', 
                title: '状态', 
                width: '100px',
                align: 'center',
                formatter: (value) => {
                    const statusClass = value === 'active' ? 'status-active' : 'status-inactive';
                    const statusText = value === 'active' ? '活跃' : '停止';
                    return `<span class="user-status ${statusClass}">${statusText}</span>`;
                }
            },
            { 
                field: 'memberCount', 
                title: '成员数', 
                width: '80px',
                align: 'center'
            },
            { 
                field: 'primaryAgentId', 
                title: '主节点', 
                width: '120px',
                formatter: (value) => value || '-'
            },
            { 
                field: 'createTime', 
                title: '创建时间', 
                width: '180px',
                formatter: (value) => value ? utils.date.format(value) : '-'
            },
            {
                field: 'actions',
                title: '操作',
                width: '250px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewMembers('${row.sceneGroupId}')">
                            <i class="ri-team-line"></i> 成员
                        </button>
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="generateKey('${row.sceneGroupId}')">
                            <i class="ri-key-line"></i> 密钥
                        </button>
                        <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="destroyGroup('${row.sceneGroupId}')">
                            <i class="ri-delete-line"></i> 销毁
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[SceneGroupManagement] 场景组列表加载完成，共', data.length, '条');
        },
        onError: (error) => {
            console.error('[SceneGroupManagement] 场景组列表加载失败:', error);
        }
    });
}

window.openCreateModal = function() {
    console.log('[SceneGroupManagement] 打开创建模态框');
    document.getElementById('createSceneId').value = '';
    document.getElementById('createMinMembers').value = '1';
    document.getElementById('createMaxMembers').value = '5';
    document.getElementById('createHeartbeatInterval').value = '5000';
    document.getElementById('createHeartbeatTimeout').value = '15000';
    document.getElementById('createModal').style.display = 'flex';
};

window.closeCreateModal = function() {
    document.getElementById('createModal').style.display = 'none';
};

window.submitCreate = async function() {
    console.log('[SceneGroupManagement] 提交创建场景组');
    
    const sceneId = document.getElementById('createSceneId').value.trim();
    if (!sceneId) {
        utils.msg.error('请输入场景ID');
        return;
    }

    const config = {
        sceneId: sceneId,
        minMembers: parseInt(document.getElementById('createMinMembers').value) || 1,
        maxMembers: parseInt(document.getElementById('createMaxMembers').value) || 5,
        heartbeatInterval: parseInt(document.getElementById('createHeartbeatInterval').value) || 5000,
        heartbeatTimeout: parseInt(document.getElementById('createHeartbeatTimeout').value) || 15000,
        keyThreshold: 2
    };

    try {
        const result = await utils.api.post('/scene-groups/create', { sceneId: sceneId, config: config });
        if (result.code === 200 && result.data) {
            if (sceneGroupTable) sceneGroupTable.refresh();
            closeCreateModal();
            utils.msg.success('场景组创建成功！');
        } else {
            utils.msg.error('创建失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneGroupManagement] 创建场景组错误:', error);
        utils.msg.error('创建失败');
    }
};

window.viewMembers = async function(sceneGroupId) {
    console.log('[SceneGroupManagement] 查看成员:', sceneGroupId);
    
    try {
        const result = await utils.api.post('/scene-groups/members', { 
            sceneGroupId: sceneGroupId,
            pageNum: 1,
            pageSize: 100
        });
        
        if (result.code === 200 && result.data) {
            const members = result.data.items || result.data;
            const tbody = document.getElementById('memberTableBody');
            tbody.innerHTML = '';
            
            if (members && members.length > 0) {
                members.forEach(member => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${member.agentId || '-'}</td>
                        <td>${member.agentName || '-'}</td>
                        <td>${member.role || '-'}</td>
                        <td>${member.status || '-'}</td>
                        <td>${member.joinTime ? utils.date.format(member.joinTime) : '-'}</td>
                    `;
                    tbody.appendChild(row);
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无成员</td></tr>';
            }
            
            document.getElementById('memberModal').style.display = 'flex';
        } else {
            utils.msg.error('获取成员列表失败');
        }
    } catch (error) {
        console.error('[SceneGroupManagement] 获取成员列表错误:', error);
        utils.msg.error('获取成员列表失败');
    }
};

window.closeMemberModal = function() {
    document.getElementById('memberModal').style.display = 'none';
};

window.generateKey = async function(sceneGroupId) {
    console.log('[SceneGroupManagement] 生成密钥:', sceneGroupId);
    
    try {
        const result = await utils.api.post('/scene-groups/key/generate', { sceneGroupId: sceneGroupId });
        if (result.code === 200 && result.data) {
            utils.msg.success('密钥生成成功！');
            console.log('[SceneGroupManagement] 密钥:', result.data);
        } else {
            utils.msg.error('生成密钥失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneGroupManagement] 生成密钥错误:', error);
        utils.msg.error('生成密钥失败');
    }
};

window.destroyGroup = async function(sceneGroupId) {
    if (!utils.msg.confirm('确定要销毁这个场景组吗？此操作不可恢复！')) {
        return;
    }

    console.log('[SceneGroupManagement] 销毁场景组:', sceneGroupId);
    
    try {
        const result = await utils.api.post('/scene-groups/destroy', { sceneGroupId: sceneGroupId });
        if (result.code === 200 && result.data) {
            if (sceneGroupTable) sceneGroupTable.refresh();
            utils.msg.success('场景组销毁成功！');
        } else {
            utils.msg.error('销毁失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneGroupManagement] 销毁场景组错误:', error);
        utils.msg.error('销毁失败');
    }
};

window.openJoinModal = function() {
    console.log('[SceneGroupManagement] 打开加入场景组模态框');
    document.getElementById('joinGroupId').value = '';
    document.getElementById('joinAgentId').value = '';
    document.getElementById('joinModal').style.display = 'block';
};

window.closeJoinModal = function() {
    document.getElementById('joinModal').style.display = 'none';
};

window.submitJoin = async function() {
    const groupId = document.getElementById('joinGroupId').value.trim();
    const agentId = document.getElementById('joinAgentId').value.trim();
    
    if (!groupId || !agentId) {
        utils.msg.error('请填写场景组ID和代理ID');
        return;
    }
    
    try {
        const result = await utils.api.post('/scene-groups/join', {
            sceneGroupId: groupId,
            agentId: agentId
        });
        
        if (result.code === 200) {
            closeJoinModal();
            if (sceneGroupTable) sceneGroupTable.refresh();
            utils.msg.success('加入场景组成功！');
        } else {
            utils.msg.error('加入失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneGroupManagement] 加入场景组错误:', error);
        utils.msg.error('加入失败');
    }
};

window.openKeyManagementModal = function(sceneGroupId) {
    currentSceneGroupId = sceneGroupId;
    console.log('[SceneGroupManagement] 打开密钥管理模态框:', sceneGroupId);
    document.getElementById('keyManagementModal').style.display = 'flex';
};

window.closeKeyManagementModal = function() {
    document.getElementById('keyManagementModal').style.display = 'none';
    currentSceneGroupId = null;
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        initSceneGroupManagementPage();
    });
} else {
    initSceneGroupManagementPage();
}
