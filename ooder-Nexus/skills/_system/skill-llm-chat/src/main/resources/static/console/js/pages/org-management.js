(function() {
    'use strict';
    
    var OrgManagement = {
        currentUser: null,
        allUsers: [],
        allDepartments: [],
        allRoles: [],
        currentTab: 'tree',
        
        init: function() {
            this.checkLogin();
        },
        
        checkLogin: function() {
            var self = this;
            
            fetch('/api/v1/auth/session')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.currentUser = result.data;
                        document.getElementById('user-name').textContent = self.currentUser.name;
                        return Promise.all([
                            self.loadRoles(),
                            self.loadDepartments(),
                            self.loadUsers(),
                            self.loadOrgTree()
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
            
            if (tab === 'tree') {
                this.loadOrgTree();
            }
        },
        
        loadRoles: function() {
            var self = this;
            
            return fetch('/api/v1/org/roles')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allRoles = result.data;
                        self.renderRoleList();
                        self.renderRoleOptions();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load roles:', e);
                });
        },
        
        renderRoleList: function() {
            var container = document.getElementById('role-list');
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>角色ID</th><th>角色名称</th><th>描述</th><th>操作</th></tr></thead><tbody>';
            
            this.allRoles.forEach(function(role) {
                html += '<tr>' +
                    '<td><code>' + role.id + '</code></td>' +
                    '<td><span class="nx-badge nx-badge--primary">' + role.name + '</span></td>' +
                    '<td>' + (role.description || '-') + '</td>' +
                    '<td><button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="OrgManagement.showMenuAuth(\'' + role.id + '\')">菜单授权</button></td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        renderRoleOptions: function() {
            var selects = ['role-filter', 'user-role'];
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
        
        loadDepartments: function() {
            var self = this;
            
            return fetch('/api/v1/org/departments')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allDepartments = result.data;
                        document.getElementById('dept-count').textContent = self.allDepartments.length;
                        self.renderDepartments();
                        self.renderDeptOptions();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load departments:', e);
                });
        },
        
        renderDepartments: function() {
            var container = document.getElementById('department-list');
            var self = this;
            
            if (this.allDepartments.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无部门</p>';
                return;
            }
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>部门名称</th><th>描述</th><th>负责人</th><th>成员数</th><th>操作</th></tr></thead><tbody>';
            
            this.allDepartments.forEach(function(dept) {
                var manager = self.allUsers.find(function(u) {
                    return u.userId === dept.managerId;
                });
                var memberCount = dept.memberIds ? dept.memberIds.length : 0;
                
                html += '<tr>' +
                    '<td><div class="nx-flex nx-items-center nx-gap-2"><i class="ri-building-line" style="color: var(--nx-primary);"></i><span class="nx-font-medium">' + dept.name + '</span></div></td>' +
                    '<td>' + (dept.description || '-') + '</td>' +
                    '<td>' + (manager ? manager.name : '-') + '</td>' +
                    '<td><span class="nx-badge nx-badge--secondary">' + memberCount + '人</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="OrgManagement.editDept(\'' + dept.departmentId + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="OrgManagement.deleteDept(\'' + dept.departmentId + '\')">删除</button>' +
                    '</td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        renderDeptOptions: function() {
            var selects = ['dept-filter', 'user-department', 'dept-parent'];
            var self = this;
            
            selects.forEach(function(id) {
                var select = document.getElementById(id);
                if (select) {
                    select.innerHTML = '<option value="">请选择部门</option>';
                    self.allDepartments.forEach(function(dept) {
                        var option = document.createElement('option');
                        option.value = dept.departmentId;
                        option.textContent = dept.name;
                        select.appendChild(option);
                    });
                }
            });
        },
        
        loadUsers: function() {
            var self = this;
            
            return fetch('/api/v1/org/users')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allUsers = result.data;
                        self.updateStats();
                        self.renderUsers(self.allUsers);
                        self.renderManagerOptions();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load users:', e);
                });
        },
        
        updateStats: function() {
            document.getElementById('user-count').textContent = this.allUsers.length;
            
            var managers = this.allUsers.filter(function(u) {
                return u.role === 'manager' || u.role === 'admin';
            });
            document.getElementById('manager-count').textContent = managers.length;
            
            var agents = this.allUsers.filter(function(u) {
                return u.role === 'llm-assistant' || u.role === 'coordinator';
            });
            document.getElementById('agent-count').textContent = agents.length;
        },
        
        renderUsers: function(users) {
            var container = document.getElementById('user-list');
            var self = this;
            
            if (users.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无用户</p>';
                return;
            }
            
            var html = '<div class="nx-table-container"><table class="nx-table">';
            html += '<thead><tr><th>用户</th><th>部门</th><th>角色</th><th>邮箱</th><th>操作</th></tr></thead><tbody>';
            
            users.forEach(function(user) {
                var roleInfo = self.allRoles.find(function(r) {
                    return r.id === user.role;
                });
                var deptInfo = self.allDepartments.find(function(d) {
                    return d.departmentId === user.departmentId;
                });
                var roleClass = self.getRoleClass(user.role);
                
                html += '<tr>' +
                    '<td><div class="nx-flex nx-items-center nx-gap-2">' +
                    '<div class="nx-w-8 nx-h-8 nx-rounded-full nx-bg-primary nx-flex nx-items-center nx-justify-center nx-text-white nx-text-sm">' + (user.name || 'U').charAt(0).toUpperCase() + '</div>' +
                    '<div><div class="nx-font-medium">' + (user.name || user.userId) + '</div>' +
                    '<div class="nx-text-sm nx-text-secondary">' + (user.title || '') + '</div></div></div></td>' +
                    '<td>' + (deptInfo ? deptInfo.name : '-') + '</td>' +
                    '<td><span class="nx-badge ' + roleClass + '">' + (roleInfo ? roleInfo.name : user.role) + '</span></td>' +
                    '<td>' + (user.email || '-') + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="OrgManagement.editUser(\'' + user.userId + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="OrgManagement.deleteUser(\'' + user.userId + '\')">删除</button>' +
                    '</td>' +
                    '</tr>';
            });
            
            html += '</tbody></table></div>';
            container.innerHTML = html;
        },
        
        getRoleClass: function(role) {
            var classes = {
                'admin': 'nx-badge--danger',
                'manager': 'nx-badge--primary',
                'hr': 'nx-badge--info',
                'employee': 'nx-badge--secondary',
                'llm-assistant': 'nx-badge--warning',
                'coordinator': 'nx-badge--warning',
                'installer': 'nx-badge--success',
                'leader': 'nx-badge--primary',
                'collaborator': 'nx-badge--secondary'
            };
            return classes[role] || 'nx-badge--secondary';
        },
        
        renderManagerOptions: function() {
            var select = document.getElementById('dept-manager');
            if (select) {
                select.innerHTML = '<option value="">请选择负责人</option>';
                this.allUsers.forEach(function(user) {
                    var option = document.createElement('option');
                    option.value = user.userId;
                    option.textContent = user.name + ' (' + (user.title || user.role) + ')';
                    select.appendChild(option);
                });
            }
        },
        
        filterUsers: function() {
            var deptId = document.getElementById('dept-filter').value;
            var roleId = document.getElementById('role-filter').value;
            
            var filtered = this.allUsers;
            
            if (deptId) {
                filtered = filtered.filter(function(u) {
                    return u.departmentId === deptId;
                });
            }
            
            if (roleId) {
                filtered = filtered.filter(function(u) {
                    return u.role === roleId;
                });
            }
            
            this.renderUsers(filtered);
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
                    document.getElementById('user-department').value = user.departmentId || '';
                    document.getElementById('user-role').value = user.role || '';
                    document.getElementById('user-title').value = user.title || '';
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
            var departmentId = document.getElementById('user-department').value;
            var role = document.getElementById('user-role').value;
            var title = document.getElementById('user-title').value;
            
            if (!name || !role) {
                alert('请填写必填项');
                return;
            }
            
            var userData = {
                name: name,
                email: email,
                departmentId: departmentId,
                role: role,
                title: title
            };
            
            var url = userId ? '/api/v1/org/users/' + userId : '/api/v1/org/users';
            var method = userId ? 'PUT' : 'POST';
            
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
                    self.closeUserModal();
                    return Promise.all([self.loadUsers(), self.loadDepartments()]);
                } else {
                    alert('保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('保存失败: ' + e.message);
            });
        },
        
        editUser: function(userId) {
            this.showUserModal(userId);
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
                    return Promise.all([self.loadUsers(), self.loadDepartments()]);
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('删除失败: ' + e.message);
            });
        },
        
        showDeptModal: function(deptId) {
            document.getElementById('dept-modal-title').textContent = deptId ? '编辑部门' : '新增部门';
            document.getElementById('dept-id').value = deptId || '';
            
            if (deptId) {
                var dept = this.allDepartments.find(function(d) {
                    return d.departmentId === deptId;
                });
                if (dept) {
                    document.getElementById('dept-name').value = dept.name || '';
                    document.getElementById('dept-desc').value = dept.description || '';
                    document.getElementById('dept-parent').value = dept.parentId || '';
                    document.getElementById('dept-manager').value = dept.managerId || '';
                }
            } else {
                document.getElementById('dept-form').reset();
            }
            
            document.getElementById('dept-modal').classList.add('nx-modal--open');
        },
        
        closeDeptModal: function() {
            document.getElementById('dept-modal').classList.remove('nx-modal--open');
        },
        
        saveDept: function() {
            var self = this;
            var deptId = document.getElementById('dept-id').value;
            var name = document.getElementById('dept-name').value;
            var description = document.getElementById('dept-desc').value;
            var parentId = document.getElementById('dept-parent').value;
            var managerId = document.getElementById('dept-manager').value;
            
            if (!name) {
                alert('请填写部门名称');
                return;
            }
            
            var deptData = {
                name: name,
                description: description,
                parentId: parentId || null,
                managerId: managerId || null
            };
            
            var url = deptId ? '/api/v1/org/departments/' + deptId : '/api/v1/org/departments';
            var method = deptId ? 'PUT' : 'POST';
            
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(deptData)
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.closeDeptModal();
                    return self.loadDepartments();
                } else {
                    alert('保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('保存失败: ' + e.message);
            });
        },
        
        editDept: function(deptId) {
            this.showDeptModal(deptId);
        },
        
        deleteDept: function(deptId) {
            var self = this;
            
            if (!confirm('确定要删除此部门吗？')) return;
            
            fetch('/api/v1/org/departments/' + deptId, {
                method: 'DELETE'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    return Promise.all([self.loadDepartments(), self.loadUsers()]);
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('删除失败: ' + e.message);
            });
        },
        
        loadOrgTree: function() {
            var self = this;
            
            return fetch('/api/v1/org/tree')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.renderOrgTree(result.data);
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load org tree:', e);
                });
        },
        
        renderOrgTree: function(tree) {
            var container = document.getElementById('org-tree');
            
            if (!tree || tree.length === 0) {
                container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无组织架构</p>';
                return;
            }
            
            var html = '<div class="org-tree">';
            tree.forEach(function(node) {
                html += this.renderTreeNode(node, 0);
            }.bind(this));
            html += '</div>';
            
            container.innerHTML = html;
        },
        
        renderTreeNode: function(node, level) {
            var indent = level * 24;
            var icon = node.type === 'department' ? 'ri-building-line' : 'ri-user-line';
            var color = node.type === 'department' ? 'var(--nx-primary)' : 'var(--nx-text-secondary)';
            
            var html = '<div class="nx-flex nx-items-center nx-gap-2 nx-py-2 nx-px-3 nx-rounded nx-hover:bg-[var(--nx-bg-hover)]" style="padding-left: ' + indent + 'px;">' +
                '<i class="' + icon + '" style="color: ' + color + ';"></i>' +
                '<span class="nx-font-medium">' + node.name + '</span>' +
                (node.role ? '<span class="nx-badge nx-badge--secondary nx-text-xs">' + node.role + '</span>' : '') +
                (node.title ? '<span class="nx-text-sm nx-text-secondary nx-ml-2">' + node.title + '</span>' : '') +
                '</div>';
            
            if (node.children && node.children.length > 0) {
                node.children.forEach(function(child) {
                    html += this.renderTreeNode(child, level + 1);
                }.bind(this));
            }
            
            return html;
        },
        
        showMenuAuth: function(roleId) {
            window.location.href = '/console/pages/menu-auth.html?role=' + roleId;
        }
    };
    
    window.switchTab = function(evt, tab) {
        OrgManagement.switchTab(evt, tab);
    };
    
    window.filterUsers = function() {
        OrgManagement.filterUsers();
    };
    
    window.showUserModal = function(userId) {
        OrgManagement.showUserModal(userId);
    };
    
    window.closeUserModal = function() {
        OrgManagement.closeUserModal();
    };
    
    window.saveUser = function() {
        OrgManagement.saveUser();
    };
    
    window.editUser = function(userId) {
        OrgManagement.editUser(userId);
    };
    
    window.deleteUser = function(userId) {
        OrgManagement.deleteUser(userId);
    };
    
    window.showDeptModal = function(deptId) {
        OrgManagement.showDeptModal(deptId);
    };
    
    window.closeDeptModal = function() {
        OrgManagement.closeDeptModal();
    };
    
    window.saveDept = function() {
        OrgManagement.saveDept();
    };
    
    window.editDept = function(deptId) {
        OrgManagement.editDept(deptId);
    };
    
    window.deleteDept = function(deptId) {
        OrgManagement.deleteDept(deptId);
    };
    
    window.OrgManagement = OrgManagement;
    
    document.addEventListener('DOMContentLoaded', OrgManagement.init.bind(OrgManagement));
})();
