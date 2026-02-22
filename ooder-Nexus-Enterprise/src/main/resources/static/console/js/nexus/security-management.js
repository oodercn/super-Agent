/**
 * 安全管理页面 JavaScript
 */

// SDK存储集合名称
const COLLECTION_USERS = 'users';
const COLLECTION_SECURITY_LOGS = 'security_logs';

// 用户数据
let users = [];

// 安全日志数据
let securityLogs = [];

// 默认用户数据
const defaultUsers = [
    {
        id: 1,
        username: "admin",
        role: "enterprise",
        status: "active",
        lastLogin: new Date(Date.now() - 3600000).toISOString()
    },
    {
        id: 2,
        username: "user1",
        role: "personal",
        status: "active",
        lastLogin: new Date(Date.now() - 7200000).toISOString()
    },
    {
        id: 3,
        username: "user2",
        role: "home",
        status: "inactive",
        lastLogin: new Date(Date.now() - 86400000).toISOString()
    },
    {
        id: 4,
        username: "user3",
        role: "personal",
        status: "active",
        lastLogin: new Date(Date.now() - 172800000).toISOString()
    },
    {
        id: 5,
        username: "user4",
        role: "home",
        status: "active",
        lastLogin: new Date(Date.now() - 259200000).toISOString()
    }
];

// 默认安全日志
const defaultSecurityLogs = [
    {
        id: 'log-001',
        timestamp: new Date(Date.now() - 3600000).toISOString(),
        event: "登录成功",
        user: "admin",
        ip: "192.168.1.100"
    },
    {
        id: 'log-002',
        timestamp: new Date(Date.now() - 7200000).toISOString(),
        event: "登录成功",
        user: "user1",
        ip: "192.168.1.101"
    },
    {
        id: 'log-003',
        timestamp: new Date(Date.now() - 86400000).toISOString(),
        event: "密码修改",
        user: "admin",
        ip: "192.168.1.100"
    }
];

/**
 * 加载用户数据
 */
async function loadUserData() {
    try {
        // 初始化默认数据
        await SdkDataService.initCollection(COLLECTION_USERS, defaultUsers);

        // 从SDK存储获取用户列表
        users = await SdkDataService.getAll(COLLECTION_USERS);

        const tbody = document.getElementById('userTableBody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${user.username}</td>
                <td>${user.role === 'personal' ? '个人' : user.role === 'home' ? '家庭' : '企业'}</td>
                <td><span class="nexus-status-badge ${user.status === 'active' ? 'nexus-status-active' : 'nexus-status-inactive'}">${user.status === 'active' ? '活跃' : '禁用'}</span></td>
                <td>${nexusCommon.formatTimestamp(new Date(user.lastLogin).getTime())}</td>
                <td>
                    <button class="nexus-btn nexus-btn-sm nexus-btn-primary" onclick="editUser(${user.id})">编辑</button>
                    <button class="nexus-btn nexus-btn-sm nexus-btn-secondary" onclick="${user.status === 'active' ? 'disableUser' : 'enableUser'}(${user.id})">${user.status === 'active' ? '禁用' : '启用'}</button>
                    <button class="nexus-btn nexus-btn-sm nexus-btn-danger" onclick="deleteUser(${user.id})">删除</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('加载用户数据失败:', error);
        nexusCommon.showMessage('加载用户数据失败', 'error');
    }
}

/**
 * 加载安全日志
 */
async function loadSecurityLogs() {
    try {
        // 初始化默认数据
        await SdkDataService.initCollection(COLLECTION_SECURITY_LOGS, defaultSecurityLogs);

        // 从SDK存储获取日志列表
        securityLogs = await SdkDataService.getAll(COLLECTION_SECURITY_LOGS);

        const logContainer = document.getElementById('securityLogContainer');
        logContainer.innerHTML = '';

        securityLogs.forEach(log => {
            const logItem = document.createElement('div');
            logItem.className = 'nexus-log-item';
            logItem.innerHTML = `
                <div class="nexus-log-header">
                    <span class="nexus-log-timestamp">${nexusCommon.formatTimestamp(new Date(log.timestamp).getTime())}</span>
                    <span class="nexus-log-level">${log.event}</span>
                </div>
                <div class="nexus-log-message">用户: ${log.user} | IP: ${log.ip}</div>
            `;
            logContainer.appendChild(logItem);
        });
    } catch (error) {
        console.error('加载安全日志失败:', error);
        nexusCommon.showMessage('加载安全日志失败', 'error');
    }
}

/**
 * 刷新安全状态
 */
async function refreshSecurityStatus() {
    try {
        nexusCommon.showMessage('刷新安全状态...', 'info');
        // 模拟刷新
        setTimeout(() => {
            nexusCommon.showMessage('安全状态已刷新', 'success');
        }, 1000);
    } catch (error) {
        nexusCommon.handleApiError(error, '刷新安全状态失败');
    }
}

/**
 * 添加用户
 */
function addUser() {
    nexusCommon.showMessage('添加用户功能开发中', 'info');
}

/**
 * 编辑用户
 */
function editUser(userId) {
    nexusCommon.showMessage('编辑用户功能开发中', 'info');
}

/**
 * 启用用户
 */
function enableUser(userId) {
    const user = users.find(u => u.id === userId);
    if (user) {
        user.status = 'active';
        loadUserData();
        nexusCommon.showMessage('用户已启用', 'success');
    }
}

/**
 * 禁用用户
 */
function disableUser(userId) {
    const user = users.find(u => u.id === userId);
    if (user) {
        user.status = 'inactive';
        loadUserData();
        nexusCommon.showMessage('用户已禁用', 'success');
    }
}

/**
 * 删除用户
 */
function deleteUser(userId) {
    if (confirm('确定要删除该用户吗？')) {
        users = users.filter(u => u.id !== userId);
        loadUserData();
        nexusCommon.showMessage('用户已删除', 'success');
    }
}

/**
 * 保存权限设置
 */
function savePermissions() {
    nexusCommon.showMessage('权限设置已保存', 'success');
}

/**
 * 保存安全设置
 */
function saveSecuritySettings() {
    nexusCommon.showMessage('安全设置已保存', 'success');
}

/**
 * 初始化页面
 */
function initSecurityManagementPage() {
    // 初始化菜单
    if (typeof initMenu === 'function') {
        initMenu();
    }
    
    // 初始化页面
    if (typeof nexusCommon !== 'undefined') {
        nexusCommon.initPage('security-management', function() {
            loadUserData();
            loadSecurityLogs();
        });
    }
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', initSecurityManagementPage);
