(function() {
    'use strict';
    
    var RoleAdmin = {
        currentUser: null,
        allRoles: [],
        allUsers: [],
        currentTab: 'roles',
        
        init: function() {
            this.checkLogin();
        },
        
        checkLogin: function() {
            var self = this;
            
            fetch('/api/v1/mvp-auth/my-session')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.currentUser = result.data;
                        document.getElementById('user-name').textContent = self.currentUser.name;
                        return Promise.all([
                            self.loadRoles(),
                            self.loadUsers()
                        ]);
                    } else {
                        window.location.href = '/console/pages/login.html';
                    }
                })
                .catch(function(e) {
                    console.error('Session check failed:', e);
                    window.location.href = '/console/pages/login.html';
                });
        },
        
        switchTab: function(evt, tab) {
            this.currentTab = tab;
            document.querySelectorAll('.tab-content').forEach(function(el) {
                el.classList.remove('active');
            });
            document.querySelectorAll('.nx-tabs__tab').forEach(function(el) {
                el.classList.remove('nx-tabs__tab--active');
            });
            
            document.getElementById('tab-' + tab).classList.add('active');
            evt.target.classList.add('nx-tabs__tab--active');
        },
        
        loadRoles: function() {
            var self = this;
            
            return fetch('/api/v1/role-management/roles')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allRoles = result.data;
                        self.renderRoleList();
                        self.renderRoleOptions();
                        self.updateStats();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load roles:', e);
                });
        },
        
        renderRoleList: function() {
            var container = document.getElementById('role-list');
            var self = this;
            
            if (this.allRoles.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无角色</p>';
                return;
            }
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>角色ID</th><th>角色名称</th><th>描述</th><th>图标</th><th>用户数</th><th>操作</th></tr></thead><tbody>';
            
            this.allRoles.forEach(function(role) {
                var userCount = self.allUsers.filter(function(u) {
                    return u.role === role.id;
                }).length;
                
                html += '<tr>' +
                    '<td><code>' + role.id + '</code></td>' +
                    '<td><span class="nx-badge nx-badge--primary">' + role.name + '</span></td>' +
                    '<td>' + (role.description || '-') + '</td>' +
                    '<td><i class="' + (role.icon || 'ri-user-line') + '"></i></td>' +
                    '<td><span class="nx-badge nx-badge--secondary">' + userCount + '</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="editRole(\'' + role.id + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteRole(\'' + role.id + '\')">删除</button>' +
                    '</td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        renderRoleOptions: function() {
            var selects = ['role-filter', 'user-role', 'bind-role-select'];
            var self = this;
            
            selects.forEach(function(id) {
                var select = document.getElementById(id);
                if (select) {
                    select.innerHTML = '<option value="">请选择角色</option>';
                    self.allRoles.forEach(function(role) {
                        var option = document.createElement('option');
                        option.value = role.id;
                        option.textContent = role.name;
                        select.appendChild(option);
                    });
                }
            });
        },
        
        loadUsers: function() {
            var self = this;
            
            return fetch('/api/v1/role-management/users')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allUsers = result.data;
                        self.renderUserList(self.allUsers);
                        self.updateStats();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load users:', e);
                });
        },
        
        renderUserList: function(users) {
            var container = document.getElementById('user-list');
            var self = this;
            
            if (users.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无用户</p>';
                return;
            }
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>用户</th><th>邮箱</th><th>角色</th><th>组织角色</th><th>操作</th></tr></thead><tbody>';
            
            users.forEach(function(user) {
                var roleInfo = self.allRoles.find(function(r) {
                    return r.id === user.role;
                });
                
                html += '<tr>' +
                    '<td><div class="nx-flex nx-items-center nx-gap-2">' +
                    '<div class="nx-w-8 nx-h-8 nx-rounded-full nx-bg-primary nx-flex nx-items-center nx-justify-center nx-text-white nx-text-sm">' + (user.name || 'U').charAt(0).toUpperCase() + '</div>' +
                    '<span class="nx-font-medium">' + (user.name || user.userId) + '</span></div></td>' +
                    '<td>' + (user.email || '-') + '</td>' +
                    '<td><span class="nx-badge nx-badge--primary">' + (roleInfo ? roleInfo.name : user.role || '-') + '</span></td>' +
                    '<td>' + (user.orgRole || '-') + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="showBindRoleModal(\'' + user.userId + '\')">绑定角色</button> ' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="deleteUser(\'' + user.userId + '\')">删除</button>' +
                    '</td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        filterUsersByRole: function() {
            var roleId = document.getElementById('role-filter').value;
            
            if (roleId) {
                var filtered = this.allUsers.filter(function(u) {
                    return u.role === roleId;
                });
                this.renderUserList(filtered);
            } else {
                this.renderUserList(this.allUsers);
            }
        },
        
        updateStats: function() {
            document.getElementById('role-count').textContent = this.allRoles.length;
            document.getElementById('user-count').textContent = this.allUsers.length;
            
            var totalMenus = 0;
            var totalPermissions = 0;
            
            this.allRoles.forEach(function(role) {
                if (role.menuIds) {
                    totalMenus += role.menuIds.length;
                }
                if (role.permissions) {
                    totalPermissions += role.permissions.length;
                }
            });
            
            document.getElementById('menu-count').textContent = totalMenus;
            document.getElementById('permission-count').textContent = totalPermissions;
            
            this.renderPermissionList();
        },
        
        renderPermissionList: function() {
            var container = document.getElementById('permission-list');
            var self = this;
            
            if (this.allRoles.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无权限配置</p>';
                return;
            }
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>角色</th><th>菜单权限</th><th>功能权限</th><th>操作</th></tr></thead><tbody>';
            
            this.allRoles.forEach(function(role) {
                var menuCount = role.menuIds ? role.menuIds.length : 0;
                var permCount = role.permissions ? role.permissions.length : 0;
                
                html += '<tr>' +
                    '<td><span class="nx-badge nx-badge--primary">' + role.name + '</span></td>' +
                    '<td>' + menuCount + ' 个菜单</td>' +
                    '<td>' + permCount + ' 项权限</td>' +
                    '<td><button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="window.location.href=\'menu-auth.html?role=' + role.id + '\'">配置菜单</button></td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        showRoleModal: function(roleId) {
            document.getElementById('role-modal-title').textContent = roleId ? '编辑角色' : '新增角色';
            document.getElementById('role-id').value = roleId || '';
            
            if (roleId) {
                var role = this.allRoles.find(function(r) {
                    return r.id === roleId;
                });
                if (role) {
                    document.getElementById('role-id-input').value = role.id;
                    document.getElementById('role-id-input').disabled = true;
                    document.getElementById('role-name').value = role.name || '';
                    document.getElementById('role-description').value = role.description || '';
                    document.getElementById('role-icon').value = role.icon || '';
                    document.getElementById('role-org-role').value = role.orgRole || '';
                    document.getElementById('role-default-username').value = role.defaultUsername || '';
                    document.getElementById('role-default-password').value = role.defaultPassword || '';
                }
            } else {
                document.getElementById('role-id-input').disabled = false;
                document.getElementById('role-form').reset();
            }
            
            document.getElementById('role-modal').classList.add('nx-modal--open');
        },
        
        closeRoleModal: function() {
            document.getElementById('role-modal').classList.remove('nx-modal--open');
        },
        
        saveRole: function() {
            var self = this;
            var roleId = document.getElementById('role-id').value;
            var id = document.getElementById('role-id-input').value;
            var name = document.getElementById('role-name').value;
            var description = document.getElementById('role-description').value;
            var icon = document.getElementById('role-icon').value;
            var orgRole = document.getElementById('role-org-role').value;
            var defaultUsername = document.getElementById('role-default-username').value;
            var defaultPassword = document.getElementById('role-default-password').value;
            
            if (!id || !name) {
                alert('请填写角色ID和名称');
                return;
            }
            
            var roleData = {
                id: id,
                name: name,
                description: description,
                icon: icon || 'ri-user-line',
                orgRole: orgRole,
                defaultUsername: defaultUsername,
                defaultPassword: defaultPassword
            };
            
            var url = roleId ? '/api/v1/role-management/roles/' + roleId : '/api/v1/role-management/roles';
            var method = roleId ? 'PUT' : 'POST';
            
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(roleData)
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.closeRoleModal();
                    return self.loadRoles();
                } else {
                    alert('保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('保存失败: ' + e.message);
            });
        },
        
        editRole: function(roleId) {
            this.showRoleModal(roleId);
        },
        
        deleteRole: function(roleId) {
            var self = this;
            
            if (!confirm('确定要删除此角色吗？')) return;
            
            fetch('/api/v1/role-management/roles/' + roleId, {
                method: 'DELETE'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    return self.loadRoles();
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('删除失败: ' + e.message);
            });
        },
        
        showUserModal: function(userId) {
            document.getElementById('user-modal-title').textContent = userId ? '编辑用户' : '新增用户';
            document.getElementById('user-id').value = userId || '';
            
            if (userId) {
                var user = this.allUsers.find(function(u) {
                    return u.userId === userId;
                });
                if (user) {
                    document.getElementById('user-name-input').value = user.name || '';
                    document.getElementById('user-email').value = user.email || '';
                    document.getElementById('user-role').value = user.role || '';
                    document.getElementById('user-org-role').value = user.orgRole || '';
                    document.getElementById('user-department').value = user.departmentId || '';
                }
            } else {
                document.getElementById('user-form').reset();
            }
            
            document.getElementById('user-modal').classList.add('nx-modal--open');
        },
        
        closeUserModal: function() {
            document.getElementById('user-modal').classList.remove('nx-modal--open');
        },
        
        saveUser: function() {
            var self = this;
            var userId = document.getElementById('user-id').value;
            var name = document.getElementById('user-name-input').value;
            var email = document.getElementById('user-email').value;
            var role = document.getElementById('user-role').value;
            var orgRole = document.getElementById('user-org-role').value;
            var departmentId = document.getElementById('user-department').value;
            var password = document.getElementById('user-password').value;
            
            if (!name || !role) {
                alert('请填写姓名和角色');
                return;
            }
            
            var userData = {
                name: name,
                email: email,
                orgRole: orgRole,
                departmentId: departmentId
            };
            
            if (password) {
                userData.password = password;
            }
            
            var url = '/api/v1/role-management/users';
            var method = 'POST';
            
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    var newUserId = result.data.userId;
                    return fetch('/api/v1/role-management/users/' + newUserId + '/bind-role/' + role, {
                        method: 'POST'
                    });
                } else {
                    throw new Error(result.message || '保存失败');
                }
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.closeUserModal();
                    return self.loadUsers();
                } else {
                    alert('绑定角色失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('保存失败: ' + e.message);
            });
        },
        
        deleteUser: function(userId) {
            var self = this;
            
            if (!confirm('确定要删除此用户吗？')) return;
            
            fetch('/api/v1/org/users/' + userId, {
                method: 'DELETE'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    return self.loadUsers();
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('删除失败: ' + e.message);
            });
        },
        
        showBindRoleModal: function(userId) {
            document.getElementById('bind-user-id').value = userId;
            document.getElementById('bind-role-modal').classList.add('nx-modal--open');
        },
        
        closeBindRoleModal: function() {
            document.getElementById('bind-role-modal').classList.remove('nx-modal--open');
        },
        
        confirmBindRole: function() {
            var self = this;
            var userId = document.getElementById('bind-user-id').value;
            var roleId = document.getElementById('bind-role-select').value;
            
            if (!roleId) {
                alert('请选择角色');
                return;
            }
            
            fetch('/api/v1/role-management/users/' + userId + '/bind-role/' + roleId, {
                method: 'POST'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.closeBindRoleModal();
                    return self.loadUsers();
                } else {
                    alert('绑定失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('绑定失败: ' + e.message);
            });
        }
    };
    
    window.switchTab = function(evt, tab) {
        RoleAdmin.switchTab(evt, tab);
    };
    
    window.showRoleModal = function(roleId) {
        RoleAdmin.showRoleModal(roleId);
    };
    
    window.closeRoleModal = function() {
        RoleAdmin.closeRoleModal();
    };
    
    window.saveRole = function() {
        RoleAdmin.saveRole();
    };
    
    window.editRole = function(roleId) {
        RoleAdmin.editRole(roleId);
    };
    
    window.deleteRole = function(roleId) {
        RoleAdmin.deleteRole(roleId);
    };
    
    window.showUserModal = function(userId) {
        RoleAdmin.showUserModal(userId);
    };
    
    window.closeUserModal = function() {
        RoleAdmin.closeUserModal();
    };
    
    window.saveUser = function() {
        RoleAdmin.saveUser();
    };
    
    window.deleteUser = function(userId) {
        RoleAdmin.deleteUser(userId);
    };
    
    window.filterUsersByRole = function() {
        RoleAdmin.filterUsersByRole();
    };
    
    window.showBindRoleModal = function(userId) {
        RoleAdmin.showBindRoleModal(userId);
    };
    
    window.closeBindRoleModal = function() {
        RoleAdmin.closeBindRoleModal();
    };
    
    window.confirmBindRole = function() {
        RoleAdmin.confirmBindRole();
    };
    
    window.RoleAdmin = RoleAdmin;
    
    document.addEventListener('DOMContentLoaded', RoleAdmin.init.bind(RoleAdmin));
})();
