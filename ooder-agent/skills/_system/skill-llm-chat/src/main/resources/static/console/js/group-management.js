// 初始化菜单
initMenu('group-management');

// 群组表格实例（使用 var 避免重复声明错误）
var groupTable = null;

// 页面加载完成后初始化
function initGroupManagementPage() {
    console.log('[GroupManagement] 初始化群组管理页面');

    // 初始化表格组件
    initGroupTable();

    // 绑定表单提交事件
    initGroupForm();

    // 绑定搜索事件
    initSearchEvent();
}

// 初始化群组表格
function initGroupTable() {
    if (!utils.DataTable) {
        console.error('[GroupManagement] DataTable 组件未加载');
        return;
    }

    groupTable = utils.DataTable({
        tableId: 'groupTableBody',
        apiUrl: '/api/admin/groups',
        pageName: 'GroupManagement',
        columns: [
            { field: 'id', title: '群组ID', width: '120px' },
            { field: 'name', title: '群组名称', width: '150px' },
            { field: 'description', title: '描述' },
            { field: 'memberCount', title: '成员数量', width: '100px', align: 'center' },
            {
                field: 'createdAt',
                title: '创建时间',
                width: '180px',
                formatter: (value) => formatDateTime(value)
            },
            {
                field: 'actions',
                title: '操作',
                width: '200px',
                align: 'center',
                formatter: (value, row) => `
                    <div class="action-buttons">
                        <button class="btn btn-secondary" onclick="viewGroupDetails('${escapeHtml(row.id)}')">
                            <i class="ri-eye-line"></i> 查看
                        </button>
                        <button class="btn btn-primary" onclick="editGroup('${escapeHtml(row.id)}')">
                            <i class="ri-edit-line"></i> 编辑
                        </button>
                        <button class="btn btn-danger" onclick="deleteGroup('${escapeHtml(row.id)}')">
                            <i class="ri-delete-line"></i> 删除
                        </button>
                    </div>
                `
            }
        ],
        onLoad: (data) => {
            console.log('[GroupManagement] 表格数据加载完成，共', data.length, '条记录');
        },
        onError: (error) => {
            console.error('[GroupManagement] 表格数据加载失败:', error);
        }
    });
}

// 初始化群组表单
function initGroupForm() {
    // 表单提交通过 onclick 处理，不需要绑定 submit 事件
    console.log('[GroupManagement] 表单事件初始化完成（使用 onclick 方式）');
}

// 初始化搜索事件
function initSearchEvent() {
    const groupSearchInput = document.getElementById('groupSearch');
    if (groupSearchInput) {
        // 使用防抖，避免频繁请求
        let debounceTimer;
        groupSearchInput.addEventListener('input', function () {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                searchGroups();
            }, 300);
        });
    }
}

// 保存群组
window.saveGroup = async function() {
    const groupId = document.getElementById('groupId').value;
    const groupName = document.getElementById('groupName').value;
    const groupDescription = document.getElementById('groupDescription').value;

    const groupData = {
        name: groupName,
        description: groupDescription
    };

    let url = `${utils.API_BASE_URL}/admin/groups`;
    let method = 'POST';

    if (groupId) {
        // 编辑现有群组
        url = `${utils.API_BASE_URL}/admin/groups/${groupId}`;
        method = 'PUT';
    }

    // 显示保存中状态
    const submitBtn = document.querySelector('#groupModal .btn-primary');
    const originalText = submitBtn ? submitBtn.textContent : '保存';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 保存中...';
    }

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(groupData)
        });

        const data = await response.json();

        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }

        if (data.success) {
            // 刷新表格数据
            if (groupTable) {
                groupTable.refresh();
            }
            closeGroupModal();
            showNotification('群组保存成功！', 'success');
        } else {
            showNotification('群组保存失败：' + (data.message || '未知错误'), 'error');
        }
    } catch (error) {
        // 恢复按钮状态
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
        console.error('Error saving group:', error);
        showNotification('群组保存失败', 'error');
    }
}

// 查询群组（点击查询按钮时调用）
window.searchGroups = function() {
    console.log('[GroupManagement] 查询按钮被点击');
    console.log('[GroupManagement] groupTable 状态:', groupTable);

    const keyword = document.getElementById('groupSearch')?.value?.trim() || '';
    console.log('[GroupManagement] 筛选值:', { keyword });

    if (groupTable && typeof groupTable.load === 'function') {
        const params = {};
        if (keyword) params.keyword = keyword;
        console.log('[GroupManagement] 查询参数:', params);
        groupTable.load(params);
    } else {
        console.error('[GroupManagement] groupTable 未初始化或 load 方法不可用');
        utils.msg.error('表格未初始化，请刷新页面重试');
    }
};

// 重置筛选条件
window.resetFilters = function() {
    console.log('[GroupManagement] 重置筛选条件');

    const keywordInput = document.getElementById('groupSearch');
    if (keywordInput) keywordInput.value = '';

    // 重新加载全部数据
    if (groupTable) {
        groupTable.refresh();
    }
};

// 打开创建群组模态框
window.openAddGroupModal = function() {
    console.log('[GroupManagement] 打开创建群组模态框');
    const modalTitle = document.getElementById('modalTitle');
    const groupId = document.getElementById('groupId');
    const groupName = document.getElementById('groupName');
    const groupDescription = document.getElementById('groupDescription');
    const groupModal = document.getElementById('groupModal');

    if (!modalTitle || !groupId || !groupName || !groupDescription || !groupModal) {
        console.error('[GroupManagement] 找不到模态框元素');
        return;
    }

    modalTitle.textContent = '创建群组';
    groupId.value = '';
    groupName.value = '';
    groupDescription.value = '';
    groupModal.style.display = 'flex';
}

// 打开编辑群组模态框
window.editGroup = function(groupId) {
    const modalTitle = document.getElementById('modalTitle');
    const groupIdInput = document.getElementById('groupId');
    const groupName = document.getElementById('groupName');
    const groupDescription = document.getElementById('groupDescription');
    const groupModal = document.getElementById('groupModal');

    if (!modalTitle || !groupIdInput || !groupName || !groupDescription || !groupModal) {
        console.error('[GroupManagement] 找不到模态框元素');
        return;
    }

    // 调用API获取群组详情
    fetch(`${utils.API_BASE_URL}/admin/groups/${groupId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.data) {
                const group = data.data;
                modalTitle.textContent = '编辑群组';
                groupIdInput.value = group.id;
                groupName.value = group.name;
                groupDescription.value = group.description;
                groupModal.style.display = 'block';
            } else {
                showNotification('获取群组详情失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error fetching group details:', error);
            showNotification('获取群组详情失败', 'error');
        });
}

// 关闭群组模态框
window.closeGroupModal = function() {
    const groupModal = document.getElementById('groupModal');
    if (groupModal) {
        groupModal.style.display = 'none';
    }
}

// 查看群组详情
window.viewGroupDetails = function(groupId) {
    // 同时获取群组详情和成员详情
    Promise.all([
        fetch(`${utils.API_BASE_URL}/admin/groups/${groupId}`).then(r => r.json()),
        fetch(`${utils.API_BASE_URL}/admin/groups/${groupId}/members`).then(r => r.json())
    ])
        .then(([groupData, membersData]) => {
            if (groupData.success && groupData.data) {
                const group = groupData.data;
                document.getElementById('basicInfo').innerHTML = `
                    <p><strong>群组ID:</strong> ${escapeHtml(group.id)}</p>
                    <p><strong>群组名称:</strong> ${escapeHtml(group.name)}</p>
                    <p><strong>描述:</strong> ${escapeHtml(group.description)}</p>
                    <p><strong>成员数量:</strong> ${group.memberCount || 0}</p>
                    <p><strong>创建时间:</strong> ${formatDateTime(group.createdAt)}</p>
                `;

                // 显示成员详情（从新的API获取）
                const memberList = document.getElementById('memberList');
                memberList.innerHTML = '';

                if (membersData.success && membersData.data && membersData.data.length > 0) {
                    membersData.data.forEach(user => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <div style="display: flex; align-items: center; gap: 10px; padding: 8px; border-bottom: 1px solid #eee;">
                                <i class="ri-user-line" style="color: #1890ff;"></i>
                                <div>
                                    <div style="font-weight: bold;">${escapeHtml(user.name)}</div>
                                    <div style="font-size: 12px; color: #666;">
                                        ${escapeHtml(user.id)} | ${escapeHtml(user.email)} | 角色: ${escapeHtml(user.role)}
                                    </div>
                                </div>
                                <span class="status-badge ${user.status === 'active' ? 'status-active' : 'status-inactive'}" 
                                      style="margin-left: auto; font-size: 11px; padding: 2px 8px; border-radius: 10px;">
                                    ${user.status === 'active' ? '活跃' : '停用'}
                                </span>
                            </div>
                        `;
                        memberList.appendChild(li);
                    });
                } else {
                    memberList.innerHTML = '<li style="padding: 20px; text-align: center; color: #999;">暂无成员</li>';
                }

                // 显示技能列表
                const groupSkills = document.getElementById('groupSkills');
                groupSkills.innerHTML = '';
                if (group.skills && group.skills.length > 0) {
                    group.skills.forEach(skill => {
                        const li = document.createElement('li');
                        li.innerHTML = `<i class="ri-code-box-line" style="color: #52c41a; margin-right: 8px;"></i>${escapeHtml(skill)}`;
                        li.style.padding = '8px';
                        li.style.borderBottom = '1px solid #eee';
                        groupSkills.appendChild(li);
                    });
                } else {
                    groupSkills.innerHTML = '<li style="padding: 20px; text-align: center; color: #999;">暂无技能</li>';
                }

                document.getElementById('groupDetails').style.display = 'block';
            } else {
                showNotification('获取群组详情失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error fetching group details:', error);
            showNotification('获取群组详情失败', 'error');
        });
}

// 隐藏群组详情
window.hideGroupDetails = function() {
    document.getElementById('groupDetails').style.display = 'none';
}

// 页面加载时初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
        console.log('[GroupManagement] DOM加载完成（通过事件）');
        initGroupManagementPage();
    });
} else {
    console.log('[GroupManagement] DOM已加载，直接初始化');
    initGroupManagementPage();
}

// 删除群组
window.deleteGroup = function(groupId) {
    if (confirm('确定要删除这个群组吗？')) {
        // 调用API删除群组
        fetch(`${utils.API_BASE_URL}/admin/groups/${groupId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 刷新表格数据
                    if (groupTable) {
                        groupTable.refresh();
                    }
                    hideGroupDetails();
                    showNotification('群组删除成功！', 'success');
                } else {
                    showNotification('群组删除失败：' + (data.message || '未知错误'), 'error');
                }
            })
            .catch(error => {
                console.error('Error deleting group:', error);
                showNotification('群组删除失败', 'error');
            });
    }
}

// HTML转义函数，防止XSS攻击
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 格式化日期时间
function formatDateTime(dateStr) {
    if (!dateStr) return '';
    try {
        const date = new Date(dateStr);
        return date.toLocaleString('zh-CN');
    } catch (e) {
        return dateStr;
    }
}

// 显示通知消息（替代alert）
function showNotification(message, type = 'info') {
    // 检查是否已存在通知容器
    let notificationContainer = document.getElementById('notification-container');
    if (!notificationContainer) {
        notificationContainer = document.createElement('div');
        notificationContainer.id = 'notification-container';
        notificationContainer.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            display: flex;
            flex-direction: column;
            gap: 10px;
        `;
        document.body.appendChild(notificationContainer);
    }

    // 创建通知元素
    const notification = document.createElement('div');
    const colors = {
        success: '#52c41a',
        error: '#ff4d4f',
        warning: '#faad14',
        info: '#1890ff'
    };
    const icons = {
        success: 'ri-check-circle-line',
        error: 'ri-error-warning-line',
        warning: 'ri-alert-line',
        info: 'ri-information-line'
    };

    notification.style.cssText = `
        background: ${colors[type] || colors.info};
        color: white;
        padding: 12px 20px;
        border-radius: 4px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.15);
        display: flex;
        align-items: center;
        gap: 8px;
        min-width: 200px;
        animation: slideIn 0.3s ease;
    `;
    notification.innerHTML = `
        <i class="${icons[type] || icons.info}"></i>
        <span>${message}</span>
    `;

    // 添加动画样式
    if (!document.getElementById('notification-styles')) {
        const style = document.createElement('style');
        style.id = 'notification-styles';
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }

    notificationContainer.appendChild(notification);

    // 3秒后自动移除
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', initGroupManagementPage);
