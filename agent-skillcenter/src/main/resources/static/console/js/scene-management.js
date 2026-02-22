// 场景管理 JavaScript

var sceneTable = null;
var currentSceneId = null;

function initSceneManagementPage() {
    console.log('[SceneManagement] 初始化场景管理页面');

    if (typeof initMenu === 'function') {
        initMenu('scene-management');
    }

    initSceneTable();
    initModals();
}

function initModals() {
    const modalsHtml = `
        <div class="nx-modal" id="addCapabilityModal">
            <div class="nx-modal__backdrop" onclick="closeAddCapabilityModal()"></div>
            <div class="nx-modal__content">
                <div class="nx-modal__header">
                    <h3 class="nx-modal__title">添加能力</h3>
                    <button class="nx-modal__close" onclick="closeAddCapabilityModal()">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="nx-modal__body">
                    <div class="nx-form-group nx-mb-4">
                        <label class="nx-form-label">能力ID</label>
                        <input type="text" class="nx-input" id="addCapId" placeholder="输入能力ID">
                    </div>
                    <div class="nx-form-group nx-mb-4">
                        <label class="nx-form-label">能力名称</label>
                        <input type="text" class="nx-input" id="addCapName" placeholder="输入能力名称">
                    </div>
                    <div class="nx-form-group nx-mb-4">
                        <label class="nx-form-label">能力类型</label>
                        <select class="nx-input" id="addCapType">
                            <option value="core">核心能力</option>
                            <option value="extension">扩展能力</option>
                        </select>
                    </div>
                </div>
                <div class="nx-modal__footer">
                    <button class="nx-btn nx-btn--secondary" onclick="closeAddCapabilityModal()">取消</button>
                    <button class="nx-btn nx-btn--primary" onclick="submitAddCapability()">添加</button>
                </div>
            </div>
        </div>

        <div class="nx-modal" id="snapshotModal">
            <div class="nx-modal__backdrop" onclick="closeSnapshotModal()"></div>
            <div class="nx-modal__content" style="max-width: 700px;">
                <div class="nx-modal__header">
                    <h3 class="nx-modal__title">快照管理</h3>
                    <button class="nx-modal__close" onclick="closeSnapshotModal()">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="nx-modal__body">
                    <div class="nx-mb-4">
                        <button class="nx-btn nx-btn--primary" onclick="createSnapshot()">
                            <i class="ri-camera-line"></i> 创建快照
                        </button>
                    </div>
                    <div class="nx-table-container">
                        <table class="nx-table">
                            <thead>
                                <tr>
                                    <th>快照ID</th>
                                    <th>创建时间</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="snapshotTableBody">
                                <tr><td colspan="4" style="text-align: center;">加载中...</td></tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="nx-modal__footer">
                    <button class="nx-btn nx-btn--secondary" onclick="closeSnapshotModal()">关闭</button>
                </div>
            </div>
        </div>

        <div class="nx-modal" id="rolesModal">
            <div class="nx-modal__backdrop" onclick="closeRolesModal()"></div>
            <div class="nx-modal__content">
                <div class="nx-modal__header">
                    <h3 class="nx-modal__title">角色配置</h3>
                    <button class="nx-modal__close" onclick="closeRolesModal()">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="nx-modal__body">
                    <div class="nx-mb-4">
                        <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="openAddRoleForm()">
                            <i class="ri-add-line"></i> 添加角色
                        </button>
                    </div>
                    <div id="rolesList">
                        <div class="nx-py-4 nx-text-center nx-text-secondary">加载中...</div>
                    </div>
                    <div id="addRoleForm" style="display: none;" class="nx-border nx-p-4 nx-rounded nx-mt-4">
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">角色ID</label>
                            <input type="text" class="nx-input" id="addRoleId" placeholder="如: primary, backup, observer">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">角色名称</label>
                            <input type="text" class="nx-input" id="addRoleName" placeholder="如: 主节点, 备份节点">
                        </div>
                        <div class="nx-form-group nx-mb-3">
                            <label class="nx-form-label">
                                <input type="checkbox" id="addRoleRequired"> 必需角色
                            </label>
                        </div>
                        <div class="nx-flex nx-gap-2">
                            <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="cancelAddRole()">取消</button>
                            <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="submitAddRole()">添加</button>
                        </div>
                    </div>
                </div>
                <div class="nx-modal__footer">
                    <button class="nx-btn nx-btn--secondary" onclick="closeRolesModal()">关闭</button>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalsHtml);
}

function initSceneTable() {
    console.log('[SceneManagement] 开始初始化场景表格');

    if (!utils || !utils.DataTable) {
        console.error('[SceneManagement] DataTable 组件未加载');
        return;
    }

    const tableBody = document.getElementById('sceneTableBody');
    if (!tableBody) {
        console.error('[SceneManagement] 找不到表格体元素');
        return;
    }

    sceneTable = utils.DataTable({
        tableId: 'sceneTableBody',
        apiUrl: '/scenes/list',
        pageName: 'SceneManagement',
        columns: [
            { field: 'sceneId', title: '场景ID', width: '120px' },
            { field: 'name', title: '名称', width: '150px' },
            { 
                field: 'type', 
                title: '类型', 
                width: '100px',
                align: 'center',
                formatter: (value) => {
                    if (value === 'primary') return '<span class="nx-badge nx-badge--primary">主场景</span>';
                    return '<span class="nx-badge nx-badge--info">协作场景</span>';
                }
            },
            { 
                field: 'version', 
                title: '版本', 
                width: '80px',
                align: 'center'
            },
            { 
                field: 'status', 
                title: '状态', 
                width: '80px',
                align: 'center',
                formatter: (value, row) => {
                    const state = row.state || {};
                    return state.active ? 
                        '<span class="user-status status-active">活跃</span>' : 
                        '<span class="user-status status-inactive">停止</span>';
                }
            },
            { 
                field: 'capabilities', 
                title: '能力数', 
                width: '80px',
                align: 'center',
                formatter: (value) => value ? value.length : 0
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
                width: '350px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewDetail('${row.sceneId}')">
                            <i class="ri-eye-line"></i> 详情
                        </button>
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="openSnapshotModal('${row.sceneId}')">
                            <i class="ri-camera-line"></i> 快照
                        </button>
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="toggleScene('${row.sceneId}', ${row.state && row.state.active})">
                            <i class="ri-${row.state && row.state.active ? 'stop' : 'play'}-line"></i> ${row.state && row.state.active ? '停用' : '激活'}
                        </button>
                        <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteScene('${row.sceneId}')">
                            <i class="ri-delete-line"></i>
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[SceneManagement] 场景列表加载完成，共', data.length, '条');
        },
        onError: (error) => {
            console.error('[SceneManagement] 场景列表加载失败:', error);
        }
    });
}

window.openCreateModal = function() {
    console.log('[SceneManagement] 打开创建模态框');
    document.getElementById('createName').value = '';
    document.getElementById('createDescription').value = '';
    document.getElementById('createType').value = 'primary';
    document.getElementById('createPrefix').value = '';
    document.getElementById('createModal').style.display = 'flex';
};

window.closeCreateModal = function() {
    document.getElementById('createModal').style.display = 'none';
};

window.submitCreate = async function() {
    console.log('[SceneManagement] 提交创建场景');
    
    const name = document.getElementById('createName').value.trim();
    if (!name) {
        utils.msg.error('请输入场景名称');
        return;
    }

    const definition = {
        name: name,
        description: document.getElementById('createDescription').value.trim(),
        type: document.getElementById('createType').value,
        scenePrefix: document.getElementById('createPrefix').value.trim(),
        version: '1.0.0'
    };

    try {
        const result = await utils.api.post('/scenes/create', definition);
        if (result.code === 200 && result.data) {
            if (sceneTable) sceneTable.refresh();
            closeCreateModal();
            utils.msg.success('场景创建成功！');
        } else {
            utils.msg.error('创建失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 创建场景错误:', error);
        utils.msg.error('创建失败');
    }
};

window.viewDetail = async function(sceneId) {
    console.log('[SceneManagement] 查看详情:', sceneId);
    currentSceneId = sceneId;
    
    try {
        const result = await utils.api.post('/scenes/get', { sceneId: sceneId });
        
        if (result.code === 200 && result.data) {
            const scene = result.data;
            
            const basicInfo = document.getElementById('sceneBasicInfo');
            basicInfo.innerHTML = `
                <div><strong>场景ID:</strong> ${scene.sceneId}</div>
                <div><strong>名称:</strong> ${scene.name}</div>
                <div><strong>类型:</strong> ${scene.type === 'primary' ? '主场景' : '协作场景'}</div>
                <div><strong>版本:</strong> ${scene.version}</div>
                <div><strong>描述:</strong> ${scene.description || '-'}</div>
                <div><strong>前缀:</strong> ${scene.scenePrefix || '-'}</div>
            `;
            
            await loadCapabilities(sceneId);
            
            document.getElementById('detailModal').style.display = 'flex';
        } else {
            utils.msg.error('获取场景详情失败');
        }
    } catch (error) {
        console.error('[SceneManagement] 获取场景详情错误:', error);
        utils.msg.error('获取场景详情失败');
    }
};

async function loadCapabilities(sceneId) {
    const capResult = await utils.api.post('/scenes/capabilities/list', { 
        sceneId: sceneId,
        pageNum: 1,
        pageSize: 100
    });
    
    const capBody = document.getElementById('capabilityTableBody');
    if (capResult.code === 200 && capResult.data && capResult.data.items) {
        const capabilities = capResult.data.items;
        if (capabilities && capabilities.length > 0) {
            capBody.innerHTML = capabilities.map(cap => `
                <tr>
                    <td>${cap.capId || '-'}</td>
                    <td>${cap.name || '-'}</td>
                    <td>${cap.type || '-'}</td>
                    <td>${cap.status || '-'}</td>
                    <td>
                        <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="removeCapability('${sceneId}', '${cap.capId}')">
                            <i class="ri-delete-bin-line"></i>
                        </button>
                    </td>
                </tr>
            `).join('');
        } else {
            capBody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无能力</td></tr>';
        }
    } else {
        capBody.innerHTML = '<tr><td colspan="5" style="text-align: center;">暂无能力</td></tr>';
    }
}

window.closeDetailModal = function() {
    document.getElementById('detailModal').style.display = 'none';
    currentSceneId = null;
};

window.toggleScene = async function(sceneId, isActive) {
    const action = isActive ? '停用' : '激活';
    console.log('[SceneManagement] ' + action + '场景:', sceneId);
    
    try {
        const endpoint = isActive ? '/scenes/deactivate' : '/scenes/activate';
        const result = await utils.api.post(endpoint, { sceneId: sceneId });
        
        if (result.code === 200 && result.data) {
            if (sceneTable) sceneTable.refresh();
            utils.msg.success('场景' + action + '成功！');
        } else {
            utils.msg.error(action + '失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] ' + action + '场景错误:', error);
        utils.msg.error(action + '失败');
    }
};

window.deleteScene = async function(sceneId) {
    if (!utils.msg.confirm('确定要删除这个场景吗？此操作不可恢复！')) {
        return;
    }

    console.log('[SceneManagement] 删除场景:', sceneId);
    
    try {
        const result = await utils.api.post('/scenes/delete', { sceneId: sceneId });
        if (result.code === 200 && result.data) {
            if (sceneTable) sceneTable.refresh();
            utils.msg.success('场景删除成功！');
        } else {
            utils.msg.error('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 删除场景错误:', error);
        utils.msg.error('删除失败');
    }
};

window.openAddCapabilityModal = function() {
    if (!currentSceneId) {
        utils.msg.error('请先选择场景');
        return;
    }
    document.getElementById('addCapId').value = '';
    document.getElementById('addCapName').value = '';
    document.getElementById('addCapType').value = 'core';
    document.getElementById('addCapabilityModal').classList.add('active');
};

window.closeAddCapabilityModal = function() {
    document.getElementById('addCapabilityModal').classList.remove('active');
};

window.submitAddCapability = async function() {
    const capId = document.getElementById('addCapId').value.trim();
    const capName = document.getElementById('addCapName').value.trim();
    const capType = document.getElementById('addCapType').value;

    if (!capId) {
        utils.msg.error('请输入能力ID');
        return;
    }

    try {
        const result = await utils.api.post('/scenes/capabilities/add', {
            sceneId: currentSceneId,
            capabilityId: capId,
            name: capName,
            type: capType
        });

        if (result.code === 200) {
            utils.msg.success('能力添加成功');
            closeAddCapabilityModal();
            await loadCapabilities(currentSceneId);
        } else {
            utils.msg.error('添加失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 添加能力错误:', error);
        utils.msg.error('添加失败');
    }
};

window.removeCapability = async function(sceneId, capId) {
    if (!utils.msg.confirm('确定要移除该能力吗？')) {
        return;
    }

    try {
        const result = await utils.api.post('/scenes/capabilities/remove', {
            sceneId: sceneId,
            capId: capId
        });

        if (result.code === 200) {
            utils.msg.success('能力移除成功');
            await loadCapabilities(sceneId);
        } else {
            utils.msg.error('移除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 移除能力错误:', error);
        utils.msg.error('移除失败');
    }
};

window.openSnapshotModal = async function(sceneId) {
    currentSceneId = sceneId;
    document.getElementById('snapshotModal').classList.add('active');
    await loadSnapshots(sceneId);
};

window.closeSnapshotModal = function() {
    document.getElementById('snapshotModal').classList.remove('active');
};

async function loadSnapshots(sceneId) {
    const tbody = document.getElementById('snapshotTableBody');
    tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">加载中...</td></tr>';

    try {
        const result = await utils.api.post('/scenes/snapshot/list', { sceneId: sceneId });
        
        if (result.code === 200 && result.data) {
            const snapshots = result.data.items || result.data || [];
            if (snapshots.length > 0) {
                tbody.innerHTML = snapshots.map(snap => `
                    <tr>
                        <td>${snap.snapshotId || snap.id || '-'}</td>
                        <td>${snap.createTime ? utils.date.format(snap.createTime) : '-'}</td>
                        <td><span class="nx-badge nx-badge--${snap.status === 'valid' ? 'success' : 'secondary'}">${snap.status || '有效'}</span></td>
                        <td>
                            <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="restoreSnapshot('${sceneId}', '${snap.snapshotId || snap.id}')">
                                <i class="ri-refresh-line"></i> 恢复
                            </button>
                            <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteSnapshot('${sceneId}', '${snap.snapshotId || snap.id}')">
                                <i class="ri-delete-line"></i>
                            </button>
                        </td>
                    </tr>
                `).join('');
            } else {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">暂无快照</td></tr>';
            }
        } else {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">暂无快照</td></tr>';
        }
    } catch (error) {
        console.error('[SceneManagement] 加载快照错误:', error);
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">加载失败</td></tr>';
    }
}

window.createSnapshot = async function() {
    if (!currentSceneId) return;

    try {
        const result = await utils.api.post('/scenes/snapshot/create', { sceneId: currentSceneId });
        
        if (result.code === 200) {
            utils.msg.success('快照创建成功');
            await loadSnapshots(currentSceneId);
        } else {
            utils.msg.error('创建失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 创建快照错误:', error);
        utils.msg.error('创建失败');
    }
};

window.restoreSnapshot = async function(sceneId, snapshotId) {
    if (!utils.msg.confirm('确定要恢复到此快照吗？当前数据将被覆盖！')) {
        return;
    }

    try {
        const result = await utils.api.post('/scenes/snapshot/restore', {
            sceneId: sceneId,
            snapshotId: snapshotId
        });

        if (result.code === 200) {
            utils.msg.success('快照恢复成功');
            await loadSnapshots(sceneId);
        } else {
            utils.msg.error('恢复失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 恢复快照错误:', error);
        utils.msg.error('恢复失败');
    }
};

window.deleteSnapshot = async function(sceneId, snapshotId) {
    if (!utils.msg.confirm('确定要删除此快照吗？')) {
        return;
    }

    try {
        const result = await utils.api.post('/scenes/snapshot/delete', {
            sceneId: sceneId,
            snapshotId: snapshotId
        });

        if (result.code === 200) {
            utils.msg.success('快照删除成功');
            await loadSnapshots(sceneId);
        } else {
            utils.msg.error('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 删除快照错误:', error);
        utils.msg.error('删除失败');
    }
};

window.openRolesModal = async function(sceneId) {
    currentSceneId = sceneId;
    document.getElementById('rolesModal').classList.add('active');
    await loadRoles(sceneId);
};

window.closeRolesModal = function() {
    document.getElementById('rolesModal').classList.remove('active');
    document.getElementById('addRoleForm').style.display = 'none';
};

async function loadRoles(sceneId) {
    const container = document.getElementById('rolesList');
    container.innerHTML = '<div class="nx-py-4 nx-text-center nx-text-secondary">加载中...</div>';

    try {
        const result = await utils.api.post('/scenes/get', { sceneId: sceneId });
        
        if (result.code === 200 && result.data) {
            const roles = result.data.roles || [];
            if (roles.length > 0) {
                container.innerHTML = roles.map(role => `
                    <div class="nx-flex nx-items-center nx-justify-between nx-py-3 nx-border-b">
                        <div>
                            <span class="nx-font-semibold">${role.name || role.roleId}</span>
                            <span class="nx-badge nx-badge--${role.required ? 'primary' : 'secondary'} nx-ml-2">${role.required ? '必需' : '可选'}</span>
                        </div>
                        <div class="nx-text-secondary nx-text-sm">
                            ${role.capabilities ? role.capabilities.length : 0} 个能力
                        </div>
                    </div>
                `).join('');
            } else {
                container.innerHTML = '<div class="nx-py-4 nx-text-center nx-text-secondary">暂无角色配置</div>';
            }
        }
    } catch (error) {
        console.error('[SceneManagement] 加载角色错误:', error);
        container.innerHTML = '<div class="nx-py-4 nx-text-center nx-text-secondary">加载失败</div>';
    }
}

window.openAddRoleForm = function() {
    document.getElementById('addRoleForm').style.display = 'block';
    document.getElementById('addRoleId').value = '';
    document.getElementById('addRoleName').value = '';
    document.getElementById('addRoleRequired').checked = false;
};

window.cancelAddRole = function() {
    document.getElementById('addRoleForm').style.display = 'none';
};

window.submitAddRole = async function() {
    const roleId = document.getElementById('addRoleId').value.trim();
    const roleName = document.getElementById('addRoleName').value.trim();
    const required = document.getElementById('addRoleRequired').checked;

    if (!roleId) {
        utils.msg.error('请输入角色ID');
        return;
    }

    try {
        const result = await utils.api.post('/scenes/roles/add', {
            sceneId: currentSceneId,
            roleId: roleId,
            name: roleName,
            required: required
        });

        if (result.code === 200) {
            utils.msg.success('角色添加成功');
            cancelAddRole();
            await loadRoles(currentSceneId);
        } else {
            utils.msg.error('添加失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SceneManagement] 添加角色错误:', error);
        utils.msg.error('添加失败');
    }
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        initSceneManagementPage();
    });
} else {
    initSceneManagementPage();
}
