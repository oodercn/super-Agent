(function() {
    'use strict';

    let groupsData = [];
    let currentGroupId = null;

    function postApi(url, data) {
        return fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data || {})
        }).then(r => r.json());
    }

    function loadGroups() {
        postApi('/api/scene-groups/list').then(res => {
            if (res.code === 0 && res.data) {
                groupsData = res.data.list || res.data || [];
                renderGroups();
            }
        }).catch(err => {
            console.error('加载群组失败:', err);
            renderEmptyState();
        });
    }

    function renderGroups() {
        const container = document.querySelector('.nx-grid');
        if (!container) return;

        if (groupsData.length === 0) {
            renderEmptyState();
            return;
        }

        container.innerHTML = groupsData.map(group => {
            const memberCount = group.memberCount || (group.members ? group.members.length : 0);
            const isAdmin = group.myRole === 'admin' || group.myRole === 'PRIMARY';
            
            return `
                <div class="nx-card" data-group-id="${group.sceneGroupId || group.groupId}">
                    <div class="nx-card__body">
                        <div class="nx-flex nx-items-center nx-gap-3 nx-mb-3">
                            <i class="ri-team-line nx-text-2xl nx-text-primary"></i>
                            <div>
                                <h4 class="nx-font-semibold">${group.name || group.sceneGroupName || '未命名群组'}</h4>
                                <span class="nx-text-secondary nx-text-sm">${memberCount} 成员</span>
                            </div>
                        </div>
                        <p class="nx-text-secondary nx-text-sm nx-mb-4">${group.description || '暂无描述'}</p>
                        <div class="nx-flex nx-gap-2">
                            <button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="viewGroupDetail('${group.sceneGroupId || group.groupId}')">
                                <i class="ri-eye-line"></i> 详情
                            </button>
                            ${isAdmin ? `
                                <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="openGroupSettings('${group.sceneGroupId || group.groupId}')">
                                    <i class="ri-settings-3-line"></i> 管理
                                </button>
                            ` : ''}
                            <button class="nx-btn nx-btn--ghost nx-btn--sm nx-text-danger" onclick="leaveGroup('${group.sceneGroupId || group.groupId}')">
                                <i class="ri-logout-box-line"></i> 退出
                            </button>
                        </div>
                    </div>
                </div>
            `;
        }).join('');
    }

    function renderEmptyState() {
        const container = document.querySelector('.nx-grid');
        if (!container) return;

        container.innerHTML = `
            <div class="nx-col-span-3 nx-text-center nx-py-12">
                <i class="ri-team-line nx-text-6xl nx-text-secondary nx-mb-4"></i>
                <h3 class="nx-text-lg nx-font-semibold nx-mb-2">暂无群组</h3>
                <p class="nx-text-secondary nx-mb-4">创建一个群组开始协作吧</p>
                <button class="nx-btn nx-btn--primary" onclick="openCreateGroupModal()">
                    <i class="ri-add-line"></i> 创建群组
                </button>
            </div>
        `;
    }

    window.openCreateGroupModal = function() {
        const modal = document.getElementById('create-group-modal');
        if (modal) {
            modal.classList.add('active');
            loadAvailableScenes();
        }
    };

    window.closeCreateGroupModal = function() {
        const modal = document.getElementById('create-group-modal');
        if (modal) {
            modal.classList.remove('active');
            document.getElementById('create-group-form').reset();
        }
    };

    function loadAvailableScenes() {
        postApi('/api/scenes/list').then(res => {
            if (res.code === 0 && res.data) {
                const scenes = res.data.list || res.data || [];
                const select = document.getElementById('create-scene-select');
                if (select) {
                    select.innerHTML = '<option value="">请选择场景</option>' +
                        scenes.map(s => `<option value="${s.sceneId}">${s.name}</option>`).join('');
                }
            }
        });
    }

    window.submitCreateGroup = function() {
        const form = document.getElementById('create-group-form');
        const formData = new FormData(form);
        
        const data = {
            name: formData.get('name'),
            sceneId: formData.get('sceneId'),
            description: formData.get('description'),
            maxMembers: parseInt(formData.get('maxMembers')) || 10
        };

        if (!data.name || !data.sceneId) {
            alert('请填写群组名称并选择场景');
            return;
        }

        postApi('/api/scene-groups/create', data).then(res => {
            if (res.code === 0) {
                alert('群组创建成功');
                closeCreateGroupModal();
                loadGroups();
            } else {
                alert('创建失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('创建失败: ' + err.message);
        });
    };

    window.viewGroupDetail = function(groupId) {
        currentGroupId = groupId;
        
        postApi('/api/scene-groups/get', { sceneGroupId: groupId }).then(res => {
            if (res.code === 0 && res.data) {
                showGroupDetailModal(res.data);
            }
        });
    };

    function showGroupDetailModal(group) {
        const modal = document.getElementById('group-detail-modal');
        if (!modal) return;

        const members = group.members || [];
        const sharedSkills = group.sharedSkills || [];

        document.getElementById('detail-group-name').textContent = group.name || group.sceneGroupName || '未命名';
        document.getElementById('detail-member-count').textContent = members.length + ' 成员';
        document.getElementById('detail-description').textContent = group.description || '暂无描述';

        const membersList = document.getElementById('detail-members-list');
        if (membersList) {
            membersList.innerHTML = members.map(m => `
                <div class="nx-flex nx-items-center nx-justify-between nx-py-2 nx-border-b">
                    <div class="nx-flex nx-items-center nx-gap-2">
                        <i class="ri-user-line"></i>
                        <span>${m.nodeName || m.userName || m.nodeId}</span>
                    </div>
                    <div>
                        <span class="nx-badge ${m.role === 'PRIMARY' ? 'nx-badge--primary' : m.role === 'BACKUP' ? 'nx-badge--info' : 'nx-badge--secondary'}">
                            ${m.role === 'PRIMARY' ? '主节点' : m.role === 'BACKUP' ? '备份节点' : '观察者'}
                        </span>
                        <span class="nx-text-secondary nx-text-sm nx-ml-2">${m.status === 'online' ? '在线' : '离线'}</span>
                    </div>
                </div>
            `).join('') || '<p class="nx-text-secondary">暂无成员</p>';
        }

        const skillsList = document.getElementById('detail-skills-list');
        if (skillsList) {
            skillsList.innerHTML = sharedSkills.map(s => `
                <div class="nx-flex nx-items-center nx-justify-between nx-py-2 nx-border-b">
                    <span>${s.skillName}</span>
                    <span class="nx-text-secondary nx-text-sm">${s.permission || '只读'}</span>
                </div>
            `).join('') || '<p class="nx-text-secondary">暂无共享技能</p>';
        }

        modal.classList.add('active');
    }

    window.closeGroupDetailModal = function() {
        const modal = document.getElementById('group-detail-modal');
        if (modal) {
            modal.classList.remove('active');
        }
        currentGroupId = null;
    };

    window.leaveGroup = function(groupId) {
        if (!confirm('确定要退出该群组吗？')) return;

        postApi('/api/scene-groups/leave', { sceneGroupId: groupId }).then(res => {
            if (res.code === 0) {
                alert('已退出群组');
                loadGroups();
            } else {
                alert('退出失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('退出失败: ' + err.message);
        });
    };

    window.openGroupSettings = function(groupId) {
        currentGroupId = groupId;
        
        postApi('/api/scene-groups/get', { sceneGroupId: groupId }).then(res => {
            if (res.code === 0 && res.data) {
                showGroupSettingsModal(res.data);
            }
        });
    };

    function showGroupSettingsModal(group) {
        const modal = document.getElementById('group-settings-modal');
        if (!modal) return;

        document.getElementById('settings-group-name').value = group.name || '';
        document.getElementById('settings-description').value = group.description || '';
        document.getElementById('settings-max-members').value = group.maxMembers || 10;

        modal.classList.add('active');
    }

    window.closeGroupSettingsModal = function() {
        const modal = document.getElementById('group-settings-modal');
        if (modal) {
            modal.classList.remove('active');
        }
    };

    window.saveGroupSettings = function() {
        const data = {
            sceneGroupId: currentGroupId,
            name: document.getElementById('settings-group-name').value,
            description: document.getElementById('settings-description').value,
            maxMembers: parseInt(document.getElementById('settings-max-members').value) || 10
        };

        postApi('/api/scene-groups/update', data).then(res => {
            if (res.code === 0) {
                alert('设置保存成功');
                closeGroupSettingsModal();
                loadGroups();
            } else {
                alert('保存失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('保存失败: ' + err.message);
        });
    };

    window.openInviteModal = function() {
        if (!currentGroupId) return;
        
        const modal = document.getElementById('invite-member-modal');
        if (modal) {
            modal.classList.add('active');
        }
    };

    window.closeInviteModal = function() {
        const modal = document.getElementById('invite-member-modal');
        if (modal) {
            modal.classList.remove('active');
            document.getElementById('invite-member-id').value = '';
        }
    };

    window.inviteMember = function() {
        const memberId = document.getElementById('invite-member-id').value;
        const role = document.getElementById('invite-member-role').value;

        if (!memberId) {
            alert('请输入成员ID');
            return;
        }

        postApi('/api/scene-groups/join', {
            sceneGroupId: currentGroupId,
            nodeId: memberId,
            role: role
        }).then(res => {
            if (res.code === 0) {
                alert('邀请成功');
                closeInviteModal();
                viewGroupDetail(currentGroupId);
            } else {
                alert('邀请失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('邀请失败: ' + err.message);
        });
    };

    window.destroyGroup = function() {
        if (!currentGroupId) return;
        
        if (!confirm('确定要解散该群组吗？此操作不可恢复！')) return;
        if (!confirm('再次确认：解散群组将移除所有成员，确定继续？')) return;

        postApi('/api/scene-groups/destroy', { sceneGroupId: currentGroupId }).then(res => {
            if (res.code === 0) {
                alert('群组已解散');
                closeGroupSettingsModal();
                loadGroups();
            } else {
                alert('解散失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('解散失败: ' + err.message);
        });
    };

    window.generateKey = function(groupId) {
        if (!confirm('确定要重新生成密钥吗？旧密钥将失效。')) return;

        postApi('/api/scene-groups/key/generate', { sceneGroupId: groupId }).then(res => {
            if (res.code === 0) {
                alert('密钥生成成功');
            } else {
                alert('密钥生成失败: ' + (res.message || '未知错误'));
            }
        }).catch(err => {
            alert('密钥生成失败: ' + err.message);
        });
    };

    function initCreateButton() {
        const createBtn = document.querySelector('.nx-btn--primary');
        if (createBtn && createBtn.textContent.includes('创建群组')) {
            createBtn.setAttribute('onclick', 'openCreateGroupModal()');
        }
    }

    function initModals() {
        const modalsHtml = `
            <div class="nx-modal" id="create-group-modal">
                <div class="nx-modal__backdrop" onclick="closeCreateGroupModal()"></div>
                <div class="nx-modal__content">
                    <div class="nx-modal__header">
                        <h3 class="nx-modal__title">创建群组</h3>
                        <button class="nx-modal__close" onclick="closeCreateGroupModal()">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                    <div class="nx-modal__body">
                        <form id="create-group-form">
                            <div class="nx-form-group nx-mb-4">
                                <label class="nx-form-label">群组名称</label>
                                <input type="text" class="nx-input" name="name" placeholder="输入群组名称" required>
                            </div>
                            <div class="nx-form-group nx-mb-4">
                                <label class="nx-form-label">关联场景</label>
                                <select class="nx-input" name="sceneId" id="create-scene-select" required>
                                    <option value="">请选择场景</option>
                                </select>
                            </div>
                            <div class="nx-form-group nx-mb-4">
                                <label class="nx-form-label">描述</label>
                                <textarea class="nx-input" name="description" rows="3" placeholder="群组描述（可选）"></textarea>
                            </div>
                            <div class="nx-form-group nx-mb-4">
                                <label class="nx-form-label">最大成员数</label>
                                <input type="number" class="nx-input" name="maxMembers" value="10" min="2" max="100">
                            </div>
                        </form>
                    </div>
                    <div class="nx-modal__footer">
                        <button class="nx-btn nx-btn--secondary" onclick="closeCreateGroupModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="submitCreateGroup()">创建</button>
                    </div>
                </div>
            </div>

            <div class="nx-modal" id="group-detail-modal">
                <div class="nx-modal__backdrop" onclick="closeGroupDetailModal()"></div>
                <div class="nx-modal__content" style="max-width: 600px;">
                    <div class="nx-modal__header">
                        <h3 class="nx-modal__title">群组详情</h3>
                        <button class="nx-modal__close" onclick="closeGroupDetailModal()">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                    <div class="nx-modal__body">
                        <div class="nx-mb-4">
                            <h4 class="nx-font-semibold" id="detail-group-name">群组名称</h4>
                            <p class="nx-text-secondary nx-text-sm" id="detail-member-count">0 成员</p>
                            <p class="nx-text-secondary nx-text-sm" id="detail-description">描述</p>
                        </div>
                        
                        <div class="nx-mb-4">
                            <h5 class="nx-font-semibold nx-mb-2">成员列表</h5>
                            <div id="detail-members-list" class="nx-max-h-48 nx-overflow-y-auto"></div>
                        </div>

                        <div class="nx-mb-4">
                            <h5 class="nx-font-semibold nx-mb-2">共享技能</h5>
                            <div id="detail-skills-list" class="nx-max-h-48 nx-overflow-y-auto"></div>
                        </div>
                    </div>
                    <div class="nx-modal__footer">
                        <button class="nx-btn nx-btn--secondary" onclick="openInviteModal()">
                            <i class="ri-user-add-line"></i> 邀请成员
                        </button>
                        <button class="nx-btn nx-btn--secondary" onclick="generateKey(currentGroupId)">
                            <i class="ri-key-line"></i> 生成密钥
                        </button>
                        <button class="nx-btn nx-btn--primary" onclick="closeGroupDetailModal()">关闭</button>
                    </div>
                </div>
            </div>

            <div class="nx-modal" id="invite-member-modal">
                <div class="nx-modal__backdrop" onclick="closeInviteModal()"></div>
                <div class="nx-modal__content">
                    <div class="nx-modal__header">
                        <h3 class="nx-modal__title">邀请成员</h3>
                        <button class="nx-modal__close" onclick="closeInviteModal()">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                    <div class="nx-modal__body">
                        <div class="nx-form-group nx-mb-4">
                            <label class="nx-form-label">成员ID</label>
                            <input type="text" class="nx-input" id="invite-member-id" placeholder="输入要邀请的成员ID">
                        </div>
                        <div class="nx-form-group nx-mb-4">
                            <label class="nx-form-label">角色</label>
                            <select class="nx-input" id="invite-member-role">
                                <option value="OBSERVER">观察者</option>
                                <option value="BACKUP">备份节点</option>
                                <option value="PRIMARY">主节点</option>
                            </select>
                        </div>
                    </div>
                    <div class="nx-modal__footer">
                        <button class="nx-btn nx-btn--secondary" onclick="closeInviteModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="inviteMember()">邀请</button>
                    </div>
                </div>
            </div>

            <div class="nx-modal" id="group-settings-modal">
                <div class="nx-modal__backdrop" onclick="closeGroupSettingsModal()"></div>
                <div class="nx-modal__content">
                    <div class="nx-modal__header">
                        <h3 class="nx-modal__title">群组设置</h3>
                        <button class="nx-modal__close" onclick="closeGroupSettingsModal()">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                    <div class="nx-modal__body">
                        <div class="nx-form-group nx-mb-4">
                            <label class="nx-form-label">群组名称</label>
                            <input type="text" class="nx-input" id="settings-group-name">
                        </div>
                        <div class="nx-form-group nx-mb-4">
                            <label class="nx-form-label">描述</label>
                            <textarea class="nx-input" id="settings-description" rows="3"></textarea>
                        </div>
                        <div class="nx-form-group nx-mb-4">
                            <label class="nx-form-label">最大成员数</label>
                            <input type="number" class="nx-input" id="settings-max-members" min="2" max="100">
                        </div>
                    </div>
                    <div class="nx-modal__footer">
                        <button class="nx-btn nx-btn--danger" onclick="destroyGroup()">
                            <i class="ri-delete-bin-line"></i> 解散群组
                        </button>
                        <button class="nx-btn nx-btn--secondary" onclick="closeGroupSettingsModal()">取消</button>
                        <button class="nx-btn nx-btn--primary" onclick="saveGroupSettings()">保存</button>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalsHtml);
    }

    window.initMyGroupPage = function() {
        initCreateButton();
        initModals();
        loadGroups();
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            if (typeof window.onPageInit === 'function') {
                const originalInit = window.onPageInit;
                window.onPageInit = function() {
                    originalInit();
                    window.initMyGroupPage();
                };
            } else {
                window.onPageInit = window.initMyGroupPage;
            }
        });
    } else {
        window.initMyGroupPage();
    }

})();
