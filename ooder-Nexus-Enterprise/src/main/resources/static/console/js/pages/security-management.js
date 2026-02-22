(function(global) {
    'use strict';

    var SecurityManagement = {
        users: [
            { username: 'admin', role: '管理员', status: 'active', lastLogin: '2026-02-13 10:00:00' },
            { username: 'user1', role: '普通用户', status: 'active', lastLogin: '2026-02-12 15:30:00' },
            { username: 'guest', role: '访客', status: 'inactive', lastLogin: '2026-02-10 09:00:00' }
        ],
        
        securityLogs: [
            { time: '2026-02-13 10:00:00', level: 'INFO', message: '用户 admin 登录成功' },
            { time: '2026-02-12 15:30:00', level: 'INFO', message: '用户 user1 登录成功' },
            { time: '2026-02-12 14:00:00', level: 'WARNING', message: '用户 guest 登录失败（密码错误）' }
        ],

        init: function() {
            window.onPageInit = function() {
                console.log('安全管理页面初始化完成');
                SecurityManagement.loadSecurityData();
            };
        },

        loadSecurityData: function() {
            document.getElementById('userCount').textContent = SecurityManagement.users.length;
            document.getElementById('activeSessions').textContent = SecurityManagement.users.filter(function(u) {
                return u.status === 'active';
            }).length;
            document.getElementById('securityEvents').textContent = SecurityManagement.securityLogs.filter(function(l) {
                return l.level === 'WARNING' || l.level === 'ERROR';
            }).length;
            SecurityManagement.renderUsers();
            SecurityManagement.renderSecurityLogs();
        },

        renderUsers: function() {
            var tbody = document.getElementById('userTableBody');
            tbody.innerHTML = '';
            SecurityManagement.users.forEach(function(user) {
                var row = document.createElement('tr');
                var statusClass = user.status === 'active' ? 'nx-text-success' : 'nx-text-danger';
                var statusText = user.status === 'active' ? '活跃' : '禁用';
                row.innerHTML = 
                    '<td>' + user.username + '</td>' +
                    '<td>' + user.role + '</td>' +
                    '<td><span class="' + statusClass + '">' + statusText + '</span></td>' +
                    '<td>' + user.lastLogin + '</td>' +
                    '<td>' +
                        '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editUser(\'' + user.username + '\')">编辑</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        renderSecurityLogs: function() {
            var container = document.getElementById('securityLogContainer');
            container.innerHTML = '';
            SecurityManagement.securityLogs.forEach(function(log) {
                var levelClass = log.level === 'ERROR' ? 'nx-text-danger' : log.level === 'WARNING' ? 'nx-text-warning' : 'nx-text-secondary';
                var item = document.createElement('div');
                item.className = 'nx-flex nx-items-center nx-gap-3 nx-py-3 nx-border-bottom';
                item.innerHTML = 
                    '<span class="nx-text-sm ' + levelClass + '" style="min-width: 60px;">' + log.level + '</span>' +
                    '<span class="nx-text-sm nx-text-secondary" style="min-width: 160px;">' + log.time + '</span>' +
                    '<span class="nx-text-sm">' + log.message + '</span>';
                container.appendChild(item);
            });
        },

        addUser: function() {
            alert('添加用户功能开发中...');
        },

        editUser: function(username) {
            alert('编辑用户: ' + username);
        },

        saveSecuritySettings: function() {
            alert('安全设置保存成功！');
        },

        resetSecuritySettings: function() {
            if (confirm('确定要重置安全设置吗？')) {
                alert('安全设置已重置为默认值');
            }
        }
    };

    SecurityManagement.init();

    global.addUser = SecurityManagement.addUser;
    global.editUser = SecurityManagement.editUser;
    global.saveSecuritySettings = SecurityManagement.saveSecuritySettings;
    global.resetSecuritySettings = SecurityManagement.resetSecuritySettings;

})(typeof window !== 'undefined' ? window : this);
