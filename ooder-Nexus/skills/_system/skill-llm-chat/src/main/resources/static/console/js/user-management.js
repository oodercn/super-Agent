// 用户管理 JavaScript

var userTable = null;

function initUserManagementPage() {
    console.log('[UserManagement] 初始化用户管理页面');

    if (typeof initMenu === 'function') {
        initMenu('admin-user-management');
    }

    initUserTable();
    initSearchEvent();
}

function initUserTable() {
    console.log('[UserManagement] 开始初始化用户表格');

    if (!utils || !utils.DataTable) {
        console.error('[UserManagement] DataTable 组件未加载');
        return;
    }

    const tableBody = document.getElementById('userTableBody');
    if (!tableBody) {
        console.error('[UserManagement] 找不到表格体元素');
        return;
    }

    userTable = utils.DataTable({
        tableId: 'userTableBody',
        apiUrl: '/api/admin/users',
        pageName: 'UserManagement',
        columns: [
            { field: 'id', title: '用户ID', width: '120px' },
            { field: 'username', title: '用户名', width: '150px' },
            { field: 'email', title: '邮箱', width: '200px' },
            { field: 'role', title: '角色', width: '100px' },
            { 
                field: 'status', 
                title: '状态', 
                width: '100px',
                align: 'center',
                formatter: (value) => {
                    const statusClass = value === 'active' ? 'status-active' : 'status-inactive';
                    const statusText = value === 'active' ? '活跃' : '禁用';
                    return `<span class="user-status ${statusClass}">${statusText}</span>`;
                }
            },
            { 
                field: 'createdAt', 
                title: '创建时间', 
                width: '180px',
                formatter: (value) => value ? utils.date.format(value) : '-'
            },
            {
                field: 'actions',
                title: '操作',
                width: '200px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="editUser('${row.id}')">
                            <i class="ri-edit-line"></i> 编辑
                        </button>
                        <button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteUser('${row.id}')">
                            <i class="ri-delete-line"></i> 删除
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[UserManagement] 用户列表加载完成，共', data.length, '条');
        },
        onError: (error) => {
            console.error('[UserManagement] 用户列表加载失败:', error);
        }
    });
}

function initSearchEvent() {
    const btnSearch = document.getElementById('btnSearchUser');
    if (btnSearch) {
        btnSearch.addEventListener('click', function(e) {
            e.preventDefault();
            searchUsers();
        });
    }

    const keywordInput = document.getElementById('userKeyword');
    if (keywordInput) {
        keywordInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchUsers();
            }
        });
    }
}

window.searchUsers = async function() {
    const keyword = document.getElementById('userKeyword')?.value?.trim() || '';

    if (keyword) {
        console.log('[UserManagement] 搜索用户:', keyword);
        try {
            const result = await utils.api.post('/admin/users/search', { 
                keyword: keyword 
            });
            if (result.code === 200 && result.data) {
                if (userTable && typeof userTable.setData === 'function') {
                    userTable.setData(result.data.items || result.data);
                }
            } else {
                utils.msg.error('搜索失败: ' + (result.message || '未知错误'));
            }
        } catch (error) {
            console.error('[UserManagement] 搜索用户错误:', error);
            utils.msg.error('搜索失败');
        }
    } else {
        if (userTable && typeof userTable.refresh === 'function') {
            userTable.refresh();
        }
    }
};

window.openAddUserModal = function() {
    console.log('[UserManagement] 打开添加用户模态框');
    
    const modalTitle = document.getElementById('modalTitle');
    const userId = document.getElementById('userId');
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const role = document.getElementById('role');
    const status = document.getElementById('status');
    const userModal = document.getElementById('userModal');

    if (modalTitle) modalTitle.textContent = '添加用户';
    if (userId) userId.value = '';
    if (username) username.value = '';
    if (email) email.value = '';
    if (role) role.value = 'user';
    if (status) status.value = 'active';
    if (userModal) userModal.style.display = 'flex';
};

window.editUser = async function(userId) {
    console.log('[UserManagement] 编辑用户:', userId);
    
    try {
        const result = await utils.api.post('/admin/users/search', { keyword: userId });
        
        if (result.code === 200 && result.data) {
            const users = result.data.items || result.data;
            const user = Array.isArray(users) ? users.find(u => u.id === userId) : users;
            
            if (user) {
                const modalTitle = document.getElementById('modalTitle');
                const userIdInput = document.getElementById('userId');
                const username = document.getElementById('username');
                const email = document.getElementById('email');
                const role = document.getElementById('role');
                const status = document.getElementById('status');
                const userModal = document.getElementById('userModal');

                if (modalTitle) modalTitle.textContent = '编辑用户';
                if (userIdInput) userIdInput.value = user.id || '';
                if (username) username.value = user.username || '';
                if (email) email.value = user.email || '';
                if (role) role.value = user.role || 'user';
                if (status) status.value = user.status || 'active';
                if (userModal) userModal.style.display = 'block';
            } else {
                utils.msg.error('用户不存在');
            }
        } else {
            utils.msg.error('获取用户详情失败');
        }
    } catch (error) {
        console.error('[UserManagement] 获取用户详情错误:', error);
        utils.msg.error('获取用户详情失败');
    }
};

window.closeUserModal = function() {
    const userModal = document.getElementById('userModal');
    if (userModal) userModal.style.display = 'none';
};

window.deleteUser = async function(userId) {
    if (!utils.msg.confirm('确定要删除这个用户吗？')) {
        return;
    }

    console.log('[UserManagement] 删除用户:', userId);
    try {
        const result = await utils.api.post(`/api/admin/users/${userId}/delete`, {});
        
        if (result.code === 200 && result.data) {
            if (userTable) userTable.refresh();
            utils.msg.success('用户删除成功！');
        } else {
            utils.msg.error('删除失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[UserManagement] 删除用户错误:', error);
        utils.msg.error('删除失败');
    }
};

window.submitUserForm = async function() {
    console.log('[UserManagement] 提交用户表单');

    const userId = document.getElementById('userId')?.value || '';
    const username = document.getElementById('username')?.value?.trim() || '';
    const email = document.getElementById('email')?.value?.trim() || '';
    const role = document.getElementById('role')?.value || 'user';
    const status = document.getElementById('status')?.value || 'active';

    if (!username || username.length < 2) {
        utils.msg.error('用户名至少2个字符');
        return;
    }
    if (!email || !email.includes('@')) {
        utils.msg.error('请输入有效的邮箱地址');
        return;
    }

    const userData = {
        username: username,
        email: email,
        role: role,
        status: status
    };

    const submitBtn = document.querySelector('#userModal .nx-btn--primary');
    const originalText = submitBtn ? submitBtn.textContent : '保存';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 保存中...';
    }

    try {
        let result;
        if (userId) {
            console.log('[UserManagement] 更新用户:', userId);
            result = await utils.api.post(`/api/admin/users/${userId}/update`, userData);
        } else {
            console.log('[UserManagement] 创建用户');
            result = await utils.api.post('/admin/users/add', userData);
        }

        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (result.code === 200) {
            if (userTable) userTable.refresh();
            closeUserModal();
            utils.msg.success('用户保存成功！');
        } else {
            utils.msg.error('保存失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('[UserManagement] 保存用户错误:', error);
        utils.msg.error('保存失败');
    }
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        initUserManagementPage();
    });
} else {
    initUserManagementPage();
}
