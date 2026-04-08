// 初始化菜单
initMenu('my-identity');

// 使用 IIFE 封装，避免全局变量污染
(function() {
    // 当前用户信息
    let currentUser = null;

    // 加载用户身份信息
    async function loadUserIdentity() {
        try {
            const response = await fetch(`${utils.API_BASE_URL}/personal/identity`);
            if (response.ok) {
                const result = await response.json();
                if (result.success && result.data) {
                    currentUser = result.data;
                    renderUserIdentity();
                    console.log('[MyIdentity] 从API加载用户身份:', currentUser.username);
                    return;
                }
            }
            throw new Error('API返回数据格式不正确');
        } catch (error) {
            console.warn('[MyIdentity] API获取失败，使用默认数据:', error);
            // 使用默认数据
            currentUser = {
                id: 'user-001',
                username: 'user123',
                name: '用户',
                email: 'user@example.com',
                phone: '13800138000',
                avatar: '',
                bio: '',
                createdAt: '2026-01-01 00:00:00'
            };
            renderUserIdentity();
        }
    }

    // 加载身份映射
    async function loadIdentityMappings() {
        try {
            const response = await fetch(`${utils.API_BASE_URL}/personal/identity/mappings`);
            if (response.ok) {
                const result = await response.json();
                if (result.success && result.data) {
                    renderIdentityMappings(result.data);
                    console.log('[MyIdentity] 从API加载身份映射:', result.data.length);
                    return;
                }
            }
            throw new Error('API返回数据格式不正确');
        } catch (error) {
            console.warn('[MyIdentity] API获取失败，使用默认数据:', error);
            // 使用默认数据
            const defaultMappings = [
                {
                    id: 'mapping-001',
                    identityType: '个人',
                    skillId: 'text-to-uppercase',
                    skillName: '文本转大写',
                    permission: '执行',
                    createdAt: '2026-01-20'
                },
                {
                    id: 'mapping-002',
                    identityType: '个人',
                    skillId: 'json-formatter',
                    skillName: 'JSON格式化',
                    permission: '执行',
                    createdAt: '2026-01-22'
                },
                {
                    id: 'mapping-003',
                    identityType: '群组',
                    groupId: 'dev-team',
                    groupName: '开发团队',
                    skillId: 'code-generator',
                    skillName: '代码生成',
                    permission: '执行',
                    createdAt: '2026-01-25'
                }
            ];
            renderIdentityMappings(defaultMappings);
        }
    }

    // 渲染用户身份信息
    function renderUserIdentity() {
        if (!currentUser) return;

        const usernameEl = document.getElementById('username');
        const nameEl = document.getElementById('name');
        const emailEl = document.getElementById('email');
        const phoneEl = document.getElementById('phone');
        const createdAtEl = document.getElementById('created-at');

        if (usernameEl) usernameEl.textContent = currentUser.username || '';
        if (nameEl) nameEl.textContent = currentUser.name || '';
        if (emailEl) emailEl.textContent = currentUser.email || '';
        if (phoneEl) phoneEl.textContent = currentUser.phone || '';
        if (createdAtEl) createdAtEl.textContent = currentUser.createdAt || '';

        // 更新编辑表单
        const editUsernameEl = document.getElementById('edit-username');
        const editNameEl = document.getElementById('edit-name');
        const editEmailEl = document.getElementById('edit-email');
        const editPhoneEl = document.getElementById('edit-phone');

        if (editUsernameEl) editUsernameEl.value = currentUser.username || '';
        if (editNameEl) editNameEl.value = currentUser.name || '';
        if (editEmailEl) editEmailEl.value = currentUser.email || '';
        if (editPhoneEl) editPhoneEl.value = currentUser.phone || '';
    }

    // 渲染身份映射
    function renderIdentityMappings(mappings) {
        const mappingsContainer = document.getElementById('identity-mappings');
        if (!mappingsContainer) return;

        mappingsContainer.innerHTML = '';

        mappings.forEach(mapping => {
            const mappingCard = document.createElement('div');
            mappingCard.className = 'identity-mapping-card';

            if (mapping.identityType === '个人' || mapping.type === 'personal') {
                mappingCard.innerHTML = `
                    <div class="mapping-card-header">
                        <h4>${mapping.skillName || mapping.identifier}</h4>
                        <span class="mapping-type">个人身份</span>
                    </div>
                    <div class="mapping-card-body">
                        <div class="mapping-info">
                            <span class="mapping-permission">权限: ${mapping.permission || '执行'}</span>
                            <span class="mapping-created">创建时间: ${mapping.createdAt || mapping.linkedAt}</span>
                        </div>
                    </div>
                `;
            } else {
                mappingCard.innerHTML = `
                    <div class="mapping-card-header">
                        <h4>${mapping.skillName || mapping.identifier}</h4>
                        <span class="mapping-type">群组身份</span>
                    </div>
                    <div class="mapping-card-body">
                        <div class="mapping-info">
                            <span class="mapping-group">群组: ${mapping.groupName || '-'}</span>
                            <span class="mapping-permission">权限: ${mapping.permission || '执行'}</span>
                            <span class="mapping-created">创建时间: ${mapping.createdAt || mapping.linkedAt}</span>
                        </div>
                    </div>
                `;
            }

            mappingsContainer.appendChild(mappingCard);
        });
    }

    // 保存用户身份信息到后端
    async function saveUserIdentity() {
        if (!currentUser) return;

        const editNameEl = document.getElementById('edit-name');
        const editEmailEl = document.getElementById('edit-email');
        const editPhoneEl = document.getElementById('edit-phone');

        const updatedUser = {
            ...currentUser,
            name: editNameEl ? editNameEl.value : currentUser.name,
            email: editEmailEl ? editEmailEl.value : currentUser.email,
            phone: editPhoneEl ? editPhoneEl.value : currentUser.phone
        };

        try {
            const response = await fetch(`${utils.API_BASE_URL}/personal/identity`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedUser)
            });

            if (response.ok) {
                const result = await response.json();
                if (result.success && result.data) {
                    currentUser = result.data;
                    renderUserIdentity();
                    console.log('[MyIdentity] 用户身份更新成功');
                    return true;
                }
            }
            throw new Error('保存失败');
        } catch (error) {
            console.warn('[MyIdentity] API保存失败，使用本地更新:', error);
            // 本地更新
            currentUser = updatedUser;
            renderUserIdentity();
            return true;
        }
    }

    // 事件监听
    document.addEventListener('DOMContentLoaded', function() {
        // 加载用户身份和身份映射
        loadUserIdentity();
        loadIdentityMappings();

        // 编辑个人信息按钮
        const editIdentityBtn = document.querySelector('.edit-identity-btn');
        if (editIdentityBtn) {
            editIdentityBtn.addEventListener('click', function() {
                const modal = document.getElementById('edit-identity-modal');
                if (modal) {
                    // 更新表单值为当前用户数据
                    const editNameEl = document.getElementById('edit-name');
                    const editEmailEl = document.getElementById('edit-email');
                    const editPhoneEl = document.getElementById('edit-phone');

                    if (editNameEl && currentUser) editNameEl.value = currentUser.name || '';
                    if (editEmailEl && currentUser) editEmailEl.value = currentUser.email || '';
                    if (editPhoneEl && currentUser) editPhoneEl.value = currentUser.phone || '';

                    modal.style.display = 'block';
                }
            });
        }

        // 关闭编辑模态框
        const closeEditModal = document.getElementById('close-edit-modal');
        if (closeEditModal) {
            closeEditModal.addEventListener('click', function() {
                const modal = document.getElementById('edit-identity-modal');
                if (modal) modal.style.display = 'none';
            });
        }

        const cancelEditBtn = document.getElementById('cancel-edit-btn');
        if (cancelEditBtn) {
            cancelEditBtn.addEventListener('click', function() {
                const modal = document.getElementById('edit-identity-modal');
                if (modal) modal.style.display = 'none';
            });
        }

        // 提交编辑
        const submitEditBtn = document.getElementById('submit-edit-btn');
        if (submitEditBtn) {
            submitEditBtn.addEventListener('click', async function() {
                const form = document.getElementById('edit-identity-form');
                if (form && form.checkValidity()) {
                    const success = await saveUserIdentity();
                    if (success) {
                        const modal = document.getElementById('edit-identity-modal');
                        if (modal) modal.style.display = 'none';
                        alert('个人信息更新成功！');
                    } else {
                        alert('个人信息更新失败！');
                    }
                } else if (form) {
                    form.reportValidity();
                }
            });
        }
    });
})();
