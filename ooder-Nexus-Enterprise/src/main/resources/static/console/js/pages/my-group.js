(function(global) {
    'use strict';

    var currentGroupId = null;

    var MyGroup = {
        init: function() {
            window.onPageInit = function() {
                MyGroup.loadGroups();
            };
        },

        loadGroups: async function() {
            try {
                var response = await fetch('/api/personal/groups/list', { method: 'POST' });
                
                var contentType = response.headers.get('content-type');
                var rs;
                
                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MyGroup.renderGroupList(rs.data);
                } else {
                    MyGroup.showError('加载群组列表失败');
                }
            } catch (error) {
                console.error('加载群组列表错误:', error);
                MyGroup.showError('加载群组列表失败');
            }
        },

        renderGroupList: function(groups) {
            var groupList = document.getElementById('group-list');
            groupList.innerHTML = '';
            
            groups.forEach(function(group) {
                var groupItem = document.createElement('div');
                groupItem.className = 'group-item';
                groupItem.innerHTML = '\
                    <div class="group-info">\
                        <h3>' + group.name + '</h3>\
                        <p>' + (group.description || '暂无描述') + '</p>\
                        <span class="group-members">' + group.memberCount + ' 成员</span>\
                    </div>\
                    <div class="group-actions">\
                        <button class="btn btn-secondary" onclick="showGroupDetail(\'' + group.id + '\')">\
                            <i class="ri-eye-line"></i> 查看\
                        </button>\
                        <button class="btn btn-danger" onclick="deleteGroup(\'' + group.id + '\')">\
                            <i class="ri-delete-line"></i> 删除\
                        </button>\
                    </div>\
                ';
                groupList.appendChild(groupItem);
            });
        },

        showCreateGroupModal: function() {
            document.getElementById('create-group-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        createGroup: async function() {
            var name = document.getElementById('group-name').value;
            var description = document.getElementById('group-description').value;
            
            if (!name) {
                MyGroup.showError('请填写群组名称');
                return;
            }
            
            try {
                var response = await fetch('/api/personal/groups', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        name: name,
                        description: description
                    })
                });
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyGroup.closeModal('create-group-modal');
                    MyGroup.showSuccess('群组创建成功');
                    MyGroup.loadGroups();
                } else {
                    MyGroup.showError('群组创建失败');
                }
            } catch (error) {
                console.error('创建群组错误:', error);
                MyGroup.showError('群组创建失败');
            }
        },

        showGroupDetail: async function(groupId) {
            currentGroupId = groupId;
            
            try {
                var response = await fetch('/api/personal/groups/' + groupId);
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    var group = result.data;
                    var content = document.getElementById('group-detail-content');
                    content.innerHTML = '\
                        <div class="group-detail">\
                            <h4>群组信息</h4>\
                            <p><strong>名称:</strong> ' + group.name + '</p>\
                            <p><strong>描述:</strong> ' + (group.description || '暂无描述') + '</p>\
                            <h4>成员列表</h4>\
                            <ul class="member-list">\
                                ' + group.members.map(function(member) {
                                    return '<li>\
                                        <span>' + member.name + '</span>\
                                        <button class="btn btn-danger btn-sm" onclick="removeMember(\'' + member.id + '\')">移除</button>\
                                    </li>';
                                }).join('') + '\
                            </ul>\
                        </div>\
                    ';
                    document.getElementById('group-detail-modal').style.display = 'block';
                } else {
                    MyGroup.showError('加载群组详情失败');
                }
            } catch (error) {
                console.error('加载群组详情错误:', error);
                MyGroup.showError('加载群组详情失败');
            }
        },

        addMember: async function() {
            var memberId = document.getElementById('member-id').value;
            
            if (!memberId) {
                MyGroup.showError('请输入用户ID');
                return;
            }
            
            try {
                var response = await fetch('/api/personal/groups/' + currentGroupId + '/members', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userId: memberId })
                });
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyGroup.showSuccess('成员添加成功');
                    MyGroup.showGroupDetail(currentGroupId);
                } else {
                    MyGroup.showError('成员添加失败');
                }
            } catch (error) {
                console.error('添加成员错误:', error);
                MyGroup.showError('成员添加失败');
            }
        },

        removeMember: async function(memberId) {
            if (!confirm('确定要移除此成员吗？')) {
                return;
            }
            
            try {
                var response = await fetch('/api/personal/groups/' + currentGroupId + '/members/' + memberId, {
                    method: 'DELETE'
                });
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyGroup.showSuccess('成员移除成功');
                    MyGroup.showGroupDetail(currentGroupId);
                } else {
                    MyGroup.showError('成员移除失败');
                }
            } catch (error) {
                console.error('移除成员错误:', error);
                MyGroup.showError('成员移除失败');
            }
        },

        deleteGroup: async function(groupId) {
            if (!confirm('确定要删除此群组吗？')) {
                return;
            }
            
            try {
                var response = await fetch('/api/personal/groups/' + groupId, {
                    method: 'DELETE'
                });
                
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }
                
                if (result.status === 'success') {
                    MyGroup.showSuccess('群组删除成功');
                    MyGroup.loadGroups();
                } else {
                    MyGroup.showError('群组删除失败');
                }
            } catch (error) {
                console.error('删除群组错误:', error);
                MyGroup.showError('群组删除失败');
            }
        },

        showError: function(message) { MyGroup.showNotification(message, 'error'); },
        showSuccess: function(message) { MyGroup.showNotification(message, 'success'); },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MyGroup.init();

    global.showCreateGroupModal = MyGroup.showCreateGroupModal;
    global.closeModal = MyGroup.closeModal;
    global.createGroup = MyGroup.createGroup;
    global.showGroupDetail = MyGroup.showGroupDetail;
    global.addMember = MyGroup.addMember;
    global.removeMember = MyGroup.removeMember;
    global.deleteGroup = MyGroup.deleteGroup;
    global.MyGroup = MyGroup;

})(typeof window !== 'undefined' ? window : this);
