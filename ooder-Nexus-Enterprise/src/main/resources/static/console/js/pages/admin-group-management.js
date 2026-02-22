(function(global) {
    'use strict';

    var currentGroupId = null;

    var GroupManagement = {
        init: function() {
            window.onPageInit = function() {
                GroupManagement.loadGroups();
            };
        },

        loadGroups: function() {
            var groupList = document.getElementById('group-list');
            groupList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/groups/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        GroupManagement.renderGroupList(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载群组列表错误:', error);
                    document.getElementById('group-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    GroupManagement.showError('加载群组列表失败');
                });
        },

        renderGroupList: function(groups) {
            var groupList = document.getElementById('group-list');
            groupList.innerHTML = '';

            if (groups.length === 0) {
                groupList.innerHTML = '<div class="empty-state">暂无群组</div>';
                return;
            }

            groups.forEach(function(group) {
                var groupItem = document.createElement('div');
                groupItem.className = 'group-item';
                groupItem.innerHTML = '<div class="group-info">' +
                    '<h3>' + group.name + '</h3>' +
                    '<p>' + (group.description || '暂无描述') + '</p>' +
                    '<span class="group-type">' + (group.type === 'public' ? '公开' : '私有') + '</span>' +
                    '<span class="group-members">' + (group.memberCount || 0) + ' 成员</span>' +
                    '</div>' +
                    '<div class="group-actions">' +
                    '<button class="btn btn-secondary" onclick="showGroupDetail(\'' + group.id + '\')"><i class="ri-eye-line"></i> 查看</button>' +
                    '<button class="btn btn-secondary" onclick="editGroup(\'' + group.id + '\')"><i class="ri-edit-line"></i> 编辑</button>' +
                    '<button class="btn btn-danger" onclick="deleteGroup(\'' + group.id + '\')"><i class="ri-delete-line"></i> 删除</button>' +
                    '</div>';
                groupList.appendChild(groupItem);
            });
        },

        showCreateGroupModal: function() {
            document.getElementById('create-group-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        createGroup: function() {
            var name = document.getElementById('group-name').value;
            var description = document.getElementById('group-description').value;
            var type = document.getElementById('group-type').value;

            if (!name) {
                GroupManagement.showError('请填写群组名称');
                return;
            }

            fetch('/api/admin/groups', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: name, description: description, type: type })
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        GroupManagement.closeModal('create-group-modal');
                        GroupManagement.showSuccess('群组创建成功');
                        GroupManagement.loadGroups();
                    } else {
                        GroupManagement.showError('群组创建失败');
                    }
                })
                .catch(function(error) {
                    console.error('创建群组错误:', error);
                    GroupManagement.showError('群组创建失败');
                });
        },

        editGroup: function(groupId) {
            fetch('/api/admin/groups/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: groupId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('edit-group-id').value = rs.data.id;
                        document.getElementById('edit-group-name').value = rs.data.name || '';
                        document.getElementById('edit-group-description').value = rs.data.description || '';
                        document.getElementById('edit-group-modal').style.display = 'flex';
                    } else {
                        GroupManagement.showError(rs.message || '获取群组信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取群组信息错误:', error);
                    GroupManagement.showError('获取群组信息失败');
                });
        },

        closeEditGroupModal: function() {
            document.getElementById('edit-group-modal').style.display = 'none';
        },

        submitEditGroup: function() {
            var id = document.getElementById('edit-group-id').value;
            var name = document.getElementById('edit-group-name').value;
            var description = document.getElementById('edit-group-description').value;

            if (!name) {
                GroupManagement.showError('请填写群组名称');
                return;
            }

            fetch('/api/admin/groups/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id, name: name, description: description })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        GroupManagement.closeEditGroupModal();
                        GroupManagement.showSuccess('群组更新成功');
                        GroupManagement.loadGroups();
                    } else {
                        GroupManagement.showError(rs.message || '群组更新失败');
                    }
                })
                .catch(function(error) {
                    console.error('更新群组错误:', error);
                    GroupManagement.showError('群组更新失败');
                });
        },

        showGroupDetail: function(groupId) {
            currentGroupId = groupId;
            var content = document.getElementById('group-detail-content');
            content.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/groups/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: groupId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        var group = rs.data;
                        content.innerHTML = '<div class="group-detail">' +
                            '<h4>群组信息</h4>' +
                            '<p><strong>名称:</strong> ' + group.name + '</p>' +
                            '<p><strong>描述:</strong> ' + (group.description || '暂无描述') + '</p>' +
                            '<p><strong>成员数:</strong> ' + (group.memberCount || 0) + '</p>' +
                            '<p><strong>状态:</strong> ' + (group.status === 'active' ? '活跃' : '禁用') + '</p>' +
                            '</div>';
                        document.getElementById('group-detail-modal').style.display = 'block';
                    } else {
                        GroupManagement.showError('加载群组详情失败');
                    }
                })
                .catch(function(error) {
                    console.error('加载群组详情错误:', error);
                    document.getElementById('group-detail-content').innerHTML = '<div class="empty-state">加载失败</div>';
                    GroupManagement.showError('加载群组详情失败');
                });
        },

        addMember: function() {
            var memberId = document.getElementById('member-id').value;
            var permissionLevel = document.getElementById('permission-level').value;

            if (!memberId) {
                GroupManagement.showError('请输入用户ID');
                return;
            }

            fetch('/api/admin/groups/' + currentGroupId + '/members', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId: memberId, role: permissionLevel })
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        GroupManagement.showSuccess('成员添加成功');
                        GroupManagement.showGroupDetail(currentGroupId);
                    } else {
                        GroupManagement.showError('成员添加失败');
                    }
                })
                .catch(function(error) {
                    console.error('添加成员错误:', error);
                    GroupManagement.showError('成员添加失败');
                });
        },

        removeMember: function(memberId) {
            if (!confirm('确定要移除此成员吗？')) return;

            fetch('/api/admin/groups/' + currentGroupId + '/members/' + memberId, {
                method: 'DELETE'
            })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        GroupManagement.showSuccess('成员移除成功');
                        GroupManagement.showGroupDetail(currentGroupId);
                    } else {
                        GroupManagement.showError('成员移除失败');
                    }
                })
                .catch(function(error) {
                    console.error('移除成员错误:', error);
                    GroupManagement.showError('成员移除失败');
                });
        },

        deleteGroup: function(groupId) {
            if (!confirm('确定要删除此群组吗？')) return;

            fetch('/api/admin/groups/' + groupId, { method: 'DELETE' })
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        GroupManagement.showSuccess('群组删除成功');
                        GroupManagement.loadGroups();
                    } else {
                        GroupManagement.showError('群组删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除群组错误:', error);
                    GroupManagement.showError('群组删除失败');
                });
        },

        showError: function(message) {
            GroupManagement.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            GroupManagement.showNotification(message, 'success');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    GroupManagement.init();

    global.showCreateGroupModal = GroupManagement.showCreateGroupModal;
    global.closeModal = GroupManagement.closeModal;
    global.createGroup = GroupManagement.createGroup;
    global.editGroup = GroupManagement.editGroup;
    global.closeEditGroupModal = GroupManagement.closeEditGroupModal;
    global.submitEditGroup = GroupManagement.submitEditGroup;
    global.showGroupDetail = GroupManagement.showGroupDetail;
    global.addMember = GroupManagement.addMember;
    global.removeMember = GroupManagement.removeMember;
    global.deleteGroup = GroupManagement.deleteGroup;

})(typeof window !== 'undefined' ? window : this);
