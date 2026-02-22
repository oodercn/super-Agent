(function(global) {
    'use strict';

    var MySharing = {
        init: function() {
            window.onPageInit = function() {
                MySharing.loadSharedSkills();
                MySharing.loadReceivedSkills();
                MySharing.loadSkillOptions();
            };
        },

        loadSharedSkills: async function() {
            try {
                var response = await fetch('/api/skillcenter/personal/sharing/shared', { method: 'POST' });

                var contentType = response.headers.get('content-type');
                var rs;

                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }

                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MySharing.renderSharedList(rs.data);
                } else {
                    MySharing.showError('加载已分享技能列表失败');
                }
            } catch (error) {
                console.error('加载已分享技能列表错误:', error);
                MySharing.showError('加载已分享技能列表失败');
            }
        },

        loadReceivedSkills: async function() {
            try {
                var response = await fetch('/api/skillcenter/personal/sharing/received', { method: 'POST' });

                var contentType = response.headers.get('content-type');
                var rs;

                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }

                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MySharing.renderReceivedList(rs.data);
                } else {
                    MySharing.showError('加载已接收技能列表失败');
                }
            } catch (error) {
                console.error('加载已接收技能列表错误:', error);
                MySharing.showError('加载已接收技能列表失败');
            }
        },

        renderSharedList: function(skills) {
            var sharedList = document.getElementById('shared-list');
            sharedList.innerHTML = '';
            
            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'share-item';
                skillItem.innerHTML = '\
                    <div class="share-info">\
                        <h3>' + (skill.skillName || '未知技能') + '</h3>\
                        <p>分享给: ' + (skill.target || '未知目标') + '</p>\
                        <span class="share-time">' + MySharing.formatTime(skill.sharedAt) + '</span>\
                    </div>\
                    <div class="share-actions">\
                        <button class="btn btn-secondary" onclick="cancelShare(\'' + skill.id + '\')">\
                            <i class="ri-close-line"></i> 取消分享\
                        </button>\
                    </div>\
                ';
                sharedList.appendChild(skillItem);
            });
        },

        renderReceivedList: function(skills) {
            var receivedList = document.getElementById('received-list');
            receivedList.innerHTML = '';
            
            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'share-item';
                skillItem.innerHTML = '\
                    <div class="share-info">\
                        <h3>' + skill.skillName + '</h3>\
                        <p>来自: ' + skill.sharedBy + '</p>\
                        <span class="share-time">' + MySharing.formatTime(skill.receivedAt) + '</span>\
                    </div>\
                    <div class="share-actions">\
                        <button class="btn btn-primary" onclick="acceptSkill(\'' + skill.id + '\')">\
                            <i class="ri-check-line"></i> 接受\
                        </button>\
                    </div>\
                ';
                receivedList.appendChild(skillItem);
            });
        },

        loadSkillOptions: async function() {
            try {
                var response = await fetch('/api/skillcenter/personal/skills/list', { method: 'POST' });

                var contentType = response.headers.get('content-type');
                var rs;

                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                }

                if (rs.requestStatus === 200 || rs.status === 'success') {
                    var skillSelect = document.getElementById('skill-select');
                    skillSelect.innerHTML = '<option value="">请选择技能</option>';
                    rs.data.forEach(function(skill) {
                        var option = document.createElement('option');
                        option.value = skill.id;
                        option.textContent = skill.name;
                        skillSelect.appendChild(option);
                    });
                }
            } catch (error) {
                console.error('加载技能列表错误:', error);
            }
        },

        switchTab: function(tab) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) { btn.classList.remove('active'); });
            document.querySelectorAll('.tab-content').forEach(function(content) { content.style.display = 'none'; });
            
            event.target.classList.add('active');
            document.getElementById(tab + '-tab').style.display = 'block';
        },

        showShareModal: function() {
            document.getElementById('share-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        shareSkill: async function() {
            var skillId = document.getElementById('skill-select').value;
            var targetType = document.getElementById('target-type').value;
            var targetId = document.getElementById('target-id').value;

            if (!skillId || !targetId) {
                MySharing.showError('请填写完整信息');
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/personal/sharing', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        skillId: skillId,
                        target: targetId,
                        targetType: targetType
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
                    MySharing.closeModal('share-modal');
                    MySharing.showSuccess('技能分享成功');
                    MySharing.loadSharedSkills();
                } else {
                    MySharing.showError('技能分享失败');
                }
            } catch (error) {
                console.error('分享技能错误:', error);
                MySharing.showError('技能分享失败');
            }
        },

        cancelShare: async function(shareId) {
            if (!confirm('确定要取消分享此技能吗？')) {
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/personal/sharing/' + shareId, {
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
                    MySharing.showSuccess('取消分享成功');
                    MySharing.loadSharedSkills();
                } else {
                    MySharing.showError('取消分享失败');
                }
            } catch (error) {
                console.error('取消分享错误:', error);
                MySharing.showError('取消分享失败');
            }
        },

        acceptSkill: async function(shareId) {
            try {
                var response = await fetch('/api/skillcenter/personal/sharing/' + shareId, {
                    method: 'POST'
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
                    MySharing.showSuccess('技能已添加到我的技能');
                    MySharing.loadReceivedSkills();
                } else {
                    MySharing.showError('接受技能失败');
                }
            } catch (error) {
                console.error('接受技能错误:', error);
                MySharing.showError('接受技能失败');
            }
        },

        formatTime: function(timestamp) {
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) { MySharing.showNotification(message, 'error'); },
        showSuccess: function(message) { MySharing.showNotification(message, 'success'); },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MySharing.init();

    global.switchTab = MySharing.switchTab;
    global.showShareModal = MySharing.showShareModal;
    global.closeModal = MySharing.closeModal;
    global.shareSkill = MySharing.shareSkill;
    global.cancelShare = MySharing.cancelShare;
    global.acceptSkill = MySharing.acceptSkill;
    global.MySharing = MySharing;

})(typeof window !== 'undefined' ? window : this);
