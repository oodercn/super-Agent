(function() {
    'use strict';

    var RoleWorkspace = {
        currentUser: null,
        roleType: null,
        
        init: function(roleType) {
            this.roleType = roleType;
            this.checkLogin();
        },
        
        checkLogin: function() {
            var self = this;
            fetch('/api/v1/auth/session')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.currentUser = result.data;
                        document.getElementById('user-name').textContent = self.currentUser.name;
                        document.getElementById('user-avatar').textContent = (self.currentUser.name || 'U').charAt(0).toUpperCase();
                        self.loadStats();
                        self.loadData();
                    } else {
                        window.location.href = '/console/pages/login.html';
                    }
                })
                .catch(function(e) {
                    console.error('Session check failed:', e);
                    window.location.href = '/console/pages/login.html';
                });
        },
        
        loadStats: function() {
            var self = this;
            var statsConfig = this.getStatsConfig();
            
            var promises = statsConfig.map(function(stat) {
                return fetch(stat.url)
                    .then(function(response) { return response.json(); })
                    .then(function(result) {
                        if (result.status === 'success' && result.data) {
                            var data = Array.isArray(result.data) ? result.data : (result.data.capabilities || result.data.scenes || result.data.todos || result.data.users || []);
                            var value = stat.filter ? data.filter(stat.filter).length : data.length;
                            document.getElementById(stat.elementId).textContent = value;
                        }
                    })
                    .catch(function(e) {
                        console.error('Failed to load stat:', stat.elementId, e);
                    });
            });
            
            Promise.all(promises).catch(function(e) {
                console.error('Failed to load stats:', e);
            });
        },
        
        getStatsConfig: function() {
            var configs = {
                admin: [
                    { elementId: 'capability-count', url: '/api/v1/capabilities' },
                    { elementId: 'active-scene-count', url: '/api/v1/scene-groups', filter: function(s) { return s.status === 'active'; } },
                    { elementId: 'user-count', url: '/api/v1/org/users' },
                    { elementId: 'pending-count', url: '/api/v1/scene-groups', filter: function(s) { return s.status === 'pending' || s.status === 'draft'; } }
                ],
                user: [
                    { elementId: 'todo-count', url: '/api/v1/my/todos', filter: function(t) { return t.status === 'pending'; } },
                    { elementId: 'done-count', url: '/api/v1/my/history/scenes', filter: function(h) { return h.status === 'completed'; } },
                    { elementId: 'scene-count', url: '/api/v1/scene-groups/my/participated' },
                    { elementId: 'key-count', url: '/api/v1/keys/my' }
                ],
                leader: [
                    { elementId: 'scene-count', url: '/api/v1/scene-groups/my/led' },
                    { elementId: 'member-count', url: '/api/v1/scene-groups/my/led/members' },
                    { elementId: 'capability-count', url: '/api/v1/capabilities?installed=true' },
                    { elementId: 'pending-count', url: '/api/v1/my/todos', filter: function(t) { return t.status === 'pending'; } }
                ],
                collaborator: [
                    { elementId: 'scene-count', url: '/api/v1/scene-groups/my/participated' },
                    { elementId: 'task-count', url: '/api/v1/my/tasks' },
                    { elementId: 'capability-count', url: '/api/v1/capabilities?installed=true' },
                    { elementId: 'pending-count', url: '/api/v1/my/todos', filter: function(t) { return t.status === 'pending'; } }
                ],
                installer: [
                    { elementId: 'installed-count', url: '/api/v1/capabilities?installed=true' },
                    { elementId: 'available-count', url: '/api/v1/capabilities/discoverable' },
                    { elementId: 'scene-count', url: '/api/v1/scene-groups' },
                    { elementId: 'pending-count', url: '/api/v1/capabilities/updates' }
                ],
                developer: [
                    { elementId: 'project-count', url: '/api/v1/projects' },
                    { elementId: 'capability-count', url: '/api/v1/capabilities?installed=true' },
                    { elementId: 'scene-count', url: '/api/v1/scene-groups' },
                    { elementId: 'pending-count', url: '/api/v1/my/todos', filter: function(t) { return t.status === 'pending'; } }
                ]
            };
            
            return configs[this.roleType] || [];
        },
        
        loadData: function() {
            var loadFunctions = {
                admin: function() { this.loadAdminData(); },
                user: function() { this.loadUserData(); },
                leader: function() { this.loadLeaderData(); },
                collaborator: function() { this.loadCollaboratorData(); },
                installer: function() { this.loadInstallerData(); },
                developer: function() { this.loadDeveloperData(); }
            };
            
            var loadFn = loadFunctions[this.roleType];
            if (loadFn) {
                loadFn.call(this);
            }
        },
        
        loadAdminData: function() {
            this.loadList('capability-list', '/api/v1/capabilities?installed=true', function(cap) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (cap.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (cap.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge ' + (cap.skillForm === 'SCENE' ? 'nx-badge--primary' : 'nx-badge--secondary') + '">' + (cap.skillForm || 'STANDALONE') + '</span>' +
                    '<a href="/console/pages/capability-detail.html?id=' + cap.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>';
            }, 5);
            
            this.loadList('scene-list', '/api/v1/scene-groups', function(scene) {
                return scene.status === 'active' ? '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (scene.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (scene.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge nx-badge--success">active</span>' +
                    '<a href="/console/pages/scene-group-detail.html?id=' + scene.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>' : '';
            }, 5, function(data) { return data.filter(function(s) { return s.status === 'active'; }); });
        },
        
        loadUserData: function() {
            this.loadList('todo-list', '/api/v1/my/todos', function(todo) {
                return todo.status === 'pending' ? '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (todo.title || todo.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (todo.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge nx-badge--warning">' + (todo.priority || 'normal') + '</span>' +
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="RoleWorkspace.acceptTask(\'' + todo.id + '\')">接受</button>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="RoleWorkspace.rejectTask(\'' + todo.id + '\')">拒绝</button></div></div>' : '';
            }, 5, function(data) { return data.filter(function(t) { return t.status === 'pending'; }); });
            
            this.loadList('scene-list', '/api/v1/scene-groups/my/participated', function(scene) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (scene.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (scene.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge ' + (scene.status === 'active' ? 'nx-badge--success' : 'nx-badge--secondary') + '">' + (scene.status || 'pending') + '</span>' +
                    '<a href="/console/pages/scene-group-detail.html?id=' + scene.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>';
            }, 5);
        },
        
        loadLeaderData: function() {
            this.loadList('scene-list', '/api/v1/scene-groups/my/led', function(scene) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (scene.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (scene.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge ' + (scene.status === 'active' ? 'nx-badge--success' : 'nx-badge--secondary') + '">' + (scene.status || 'pending') + '</span>' +
                    '<a href="/console/pages/scene-group-detail.html?id=' + scene.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>';
            }, 5);
            
            this.loadList('member-list', '/api/v1/scene-groups/my/led/members', function(member) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (member.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (member.email || '-') + '</p></div>' +
                    '<span class="nx-badge nx-badge--secondary">' + (member.role || 'member') + '</span></div>';
            }, 5);
        },
        
        loadCollaboratorData: function() {
            this.loadList('task-list', '/api/v1/my/tasks', function(task) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (task.title || task.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (task.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge nx-badge--warning">' + (task.status || 'pending') + '</span>' +
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="RoleWorkspace.completeTask(\'' + task.id + '\')">完成</button></div></div>';
            }, 5);
        },
        
        loadInstallerData: function() {
            this.loadList('capability-list', '/api/v1/capabilities?installed=true', function(cap) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (cap.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (cap.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge nx-badge--success">已安装</span>' +
                    '<a href="/console/pages/capability-detail.html?id=' + cap.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>';
            }, 5);
        },
        
        loadDeveloperData: function() {
            this.loadList('project-list', '/api/v1/projects', function(project) {
                return '<div class="nx-flex nx-items-center nx-justify-between nx-p-4 nx-border-b nx-border-[var(--nx-border)]">' +
                    '<div><h4 class="nx-font-medium">' + (project.name || '-') + '</h4>' +
                    '<p class="nx-text-sm nx-text-secondary">' + (project.description || '暂无描述') + '</p></div>' +
                    '<div class="nx-flex nx-items-center nx-gap-2">' +
                    '<span class="nx-badge nx-badge--primary">' + (project.status || 'active') + '</span>' +
                    '<a href="/console/pages/project-detail.html?id=' + project.id + '" class="nx-btn nx-btn--secondary nx-btn--sm">详情</a></div></div>';
            }, 5);
        },
        
        loadList: function(containerId, url, renderFn, limit, filterFn) {
            var container = document.getElementById(containerId);
            if (!container) return;
            
            fetch(url)
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var data = Array.isArray(result.data) ? result.data : (result.data.capabilities || result.data.scenes || result.data.todos || result.data.members || result.data.tasks || result.data.projects || []);
                        
                        if (filterFn) {
                            data = filterFn(data);
                        }
                        
                        if (data.length === 0) {
                            container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无数据</p>';
                            return;
                        }
                        
                        var html = data.slice(0, limit || 5).map(renderFn).join('');
                        if (!html.trim()) {
                            container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无数据</p>';
                        } else {
                            container.innerHTML = html;
                        }
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load list:', containerId, e);
                    container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">加载失败</p>';
                });
        },
        
        acceptTask: function(taskId) {
            var self = this;
            fetch('/api/v1/my/todos/' + taskId + '/accept', { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('任务已接受');
                        self.loadStats();
                        self.loadData();
                    } else {
                        alert('接受失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(e) {
                    alert('接受失败: ' + e.message);
                });
        },
        
        rejectTask: function(taskId) {
            if (!confirm('确定要拒绝这个任务吗？')) return;
            
            var self = this;
            fetch('/api/v1/my/todos/' + taskId + '/reject', { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('任务已拒绝');
                        self.loadStats();
                        self.loadData();
                    } else {
                        alert('拒绝失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(e) {
                    alert('拒绝失败: ' + e.message);
                });
        },
        
        completeTask: function(taskId) {
            var self = this;
            fetch('/api/v1/my/tasks/' + taskId + '/complete', { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('任务已完成');
                        self.loadStats();
                        self.loadData();
                    } else {
                        alert('完成失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(e) {
                    alert('完成失败: ' + e.message);
                });
        }
    };

    window.RoleWorkspace = RoleWorkspace;
})();
