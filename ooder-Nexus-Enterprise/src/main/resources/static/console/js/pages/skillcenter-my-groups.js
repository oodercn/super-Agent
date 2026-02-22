(function(global) {
    'use strict';

    var currentGroupId = null;
    var allGroups = [];

    var MyGroups = {
        init: function() {
            window.onPageInit = function() {
                MyGroups.loadGroups();
            };

            window.onclick = function(event) {
                if (event.target.classList.contains('modal')) {
                    event.target.classList.remove('active');
                }
            };
        },

        loadGroups: function() {
            fetch('/api/skillcenter/groups/list', { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.requestStatus === 200 && data.data) {
                        allGroups = data.data;
                        MyGroups.renderGroups(allGroups);
                    }
                })
                .catch(function(error) {
                    console.error('加载群组失败:', error);
                    MyGroups.showNotification('加载群组失败', 'error');
                });
        },

        renderGroups: function(groups) {
            var container = document.getElementById('groups-grid');

            if (groups.length === 0) {
                container.innerHTML = '<div class="empty-state" style="grid-column: 1 / -1;">' +
                    '<i class="ri-team-line"></i>' +
                    '<h3>暂无群组</h3>' +
                    '<p>点击"创建群组"按钮创建您的第一个群组</p>' +
                    '</div>';
                return;
            }

            var html = '';
            groups.forEach(function(group) {
                var roleText = group.role === 'owner' ? '群主' : '成员';
                var actionButtons = '';
                
                if (group.role === 'owner') {
                    actionButtons = '<button class="btn btn-danger" onclick="deleteGroup(\'' + group.id + '\')">' +
                        '<i class="ri-delete-bin-line"></i> 删除' +
                        '</button>';
                } else {
                    actionButtons = '<button class="btn btn-secondary" onclick="leaveGroup(\'' + group.id + '\')">' +
                        '<i class="ri-logout-box-line"></i> 退出' +
                        '</button>';
                }

                html += '<div class="group-card">' +
                    '<div class="group-header">' +
                    '<div class="group-icon"><i class="ri-team-line"></i></div>' +
                    '<div class="group-info">' +
                    '<div class="group-name">' + group.name + '</div>' +
                    '<span class="group-role">' + roleText + '</span>' +
                    '</div>' +
                    '</div>' +
                    '<div class="group-description">' + (group.description || '暂无描述') + '</div>' +
                    '<div class="group-meta">' +
                    '<span><i class="ri-user-line"></i> ' + group.memberCount + ' 成员</span>' +
                    '<span><i class="ri-calendar-line"></i> ' + new Date(group.createdAt).toLocaleDateString() + '</span>' +
                    '</div>' +
                    '<div class="group-actions">' +
                    '<button class="btn btn-secondary" onclick="showMembers(\'' + group.id + '\')">' +
                    '<i class="ri-user-line"></i> 成员' +
                    '</button>' +
                    actionButtons +
                    '</div>' +
                    '</div>';
            });
            container.innerHTML = html;
        },

        searchGroups: function() {
            var keyword = document.getElementById('search-input').value.toLowerCase();
            var filtered = allGroups.filter(function(g) {
                return g.name.toLowerCase().indexOf(keyword) !== -1 ||
                    (g.description && g.description.toLowerCase().indexOf(keyword) !== -1);
            });
            MyGroups.renderGroups(filtered);
        },

        showCreateModal: function() {
            document.getElementById('create-modal').classList.add('active');
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).classList.remove('active');
        },

        submitCreate: function() {
            var name = document.getElementById('create-group-name').value.trim();
            var description = document.getElementById('create-group-desc').value.trim();

            if (!name) {
                MyGroups.showNotification('请输入群组名称', 'error');
                return;
            }

            fetch('/api/skillcenter/groups/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: name, description: description })
            })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.requestStatus === 200 || data.code === 200) {
                        MyGroups.showNotification('群组创建成功！', 'success');
                        MyGroups.closeModal('create-modal');
                        document.getElementById('create-group-name').value = '';
                        document.getElementById('create-group-desc').value = '';
                        MyGroups.loadGroups();
                    } else {
                        MyGroups.showNotification(data.message || '创建失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('创建群组失败:', error);
                    MyGroups.showNotification('创建失败，请检查网络连接', 'error');
                });
        },

        showMembers: function(groupId) {
            currentGroupId = groupId;
            fetch('/api/skillcenter/groups/' + groupId + '/members')
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.code === 200 && data.data) {
                        var container = document.getElementById('member-list');
                        var html = '';
                        data.data.forEach(function(member) {
                            var roleText = member.role === 'admin' ? '管理员' : '成员';
                            var removeButton = '';
                            if (member.role !== 'admin') {
                                removeButton = '<button class="btn btn-sm btn-danger" onclick="removeMember(\'' + member.id + '\')">' +
                                    '<i class="ri-delete-bin-line"></i>' +
                                    '</button>';
                            }
                            html += '<div class="member-item">' +
                                '<div class="member-avatar">' + member.username.charAt(0).toUpperCase() + '</div>' +
                                '<div class="member-info">' +
                                '<div class="member-name">' + member.username + '</div>' +
                                '<div class="member-role">' + roleText + '</div>' +
                                '</div>' +
                                removeButton +
                                '</div>';
                        });
                        container.innerHTML = html;
                        document.getElementById('members-modal').classList.add('active');
                    }
                })
                .catch(function(error) {
                    console.error('加载成员失败:', error);
                    MyGroups.showNotification('加载成员失败', 'error');
                });
        },

        addMember: function() {
            var username = document.getElementById('new-member-name').value.trim();
            if (!username) {
                MyGroups.showNotification('请输入用户名', 'error');
                return;
            }

            fetch('/api/skillcenter/groups/' + currentGroupId + '/members', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userId: 'user-' + Date.now(),
                    username: username,
                    role: 'member'
                })
            })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.code === 200) {
                        MyGroups.showNotification('成员添加成功！', 'success');
                        document.getElementById('new-member-name').value = '';
                        MyGroups.showMembers(currentGroupId);
                        MyGroups.loadGroups();
                    } else {
                        MyGroups.showNotification(data.message || '添加失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('添加成员失败:', error);
                    MyGroups.showNotification('添加失败，请检查网络连接', 'error');
                });
        },

        removeMember: function(memberId) {
            if (!confirm('确定要移除该成员吗？')) return;

            fetch('/api/skillcenter/groups/' + currentGroupId + '/members/' + memberId, {
                method: 'DELETE'
            })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.code === 200) {
                        MyGroups.showNotification('成员已移除', 'success');
                        MyGroups.showMembers(currentGroupId);
                        MyGroups.loadGroups();
                    } else {
                        MyGroups.showNotification(data.message || '操作失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('移除成员失败:', error);
                    MyGroups.showNotification('操作失败，请检查网络连接', 'error');
                });
        },

        deleteGroup: function(groupId) {
            if (!confirm('确定要删除该群组吗？此操作不可恢复。')) return;

            fetch('/api/skillcenter/groups/' + groupId, {
                method: 'DELETE'
            })
                .then(function(response) { return response.json(); })
                .then(function(data) {
                    if (data.code === 200) {
                        MyGroups.showNotification('群组已删除', 'success');
                        MyGroups.loadGroups();
                    } else {
                        MyGroups.showNotification(data.message || '删除失败', 'error');
                    }
                })
                .catch(function(error) {
                    console.error('删除群组失败:', error);
                    MyGroups.showNotification('删除失败，请检查网络连接', 'error');
                });
        },

        leaveGroup: function(groupId) {
            if (!confirm('确定要退出该群组吗？')) return;
            MyGroups.showNotification('功能开发中...', 'success');
        },

        showNotification: function(message, type) {
            var existing = document.querySelector('.notification');
            if (existing) existing.remove();

            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            var icon = type === 'success' ? 'ri-check-line' : 'ri-error-warning-line';
            notification.innerHTML = '<i class="' + icon + '"></i> ' + message;
            document.body.appendChild(notification);

            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MyGroups.init();

    global.searchGroups = MyGroups.searchGroups;
    global.showCreateModal = MyGroups.showCreateModal;
    global.closeModal = MyGroups.closeModal;
    global.submitCreate = MyGroups.submitCreate;
    global.showMembers = MyGroups.showMembers;
    global.addMember = MyGroups.addMember;
    global.removeMember = MyGroups.removeMember;
    global.deleteGroup = MyGroups.deleteGroup;
    global.leaveGroup = MyGroups.leaveGroup;

})(typeof window !== 'undefined' ? window : this);
