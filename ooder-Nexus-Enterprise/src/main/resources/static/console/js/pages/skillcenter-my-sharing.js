(function(global) {
    'use strict';

    var SkillSharing = {
        init: function() {
            window.onPageInit = function() {
                SkillSharing.loadGroups();
            };
        },

        switchTab: function(tabName) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('active');
            });
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('active');
            });

            document.querySelector('[data-tab="' + tabName + '"]').classList.add('active');
            document.getElementById(tabName + '-tab').classList.add('active');

            if (tabName === 'shared') {
                this.loadSharedSkills();
            } else if (tabName === 'received') {
                this.loadReceivedSkills();
            }
        },

        loadGroups: async function() {
            try {
                var response = await fetch('/api/skillcenter/groups');
                var data = await response.json();

                if (data.code === 200 && data.data) {
                    var select = document.getElementById('share-group-select');
                    select.innerHTML = '<option value="">请选择群组</option>';

                    data.data.forEach(function(group) {
                        var option = document.createElement('option');
                        option.value = group.id;
                        option.textContent = group.name;
                        select.appendChild(option);
                    });
                }
            } catch (error) {
                console.error('加载群组失败:', error);
            }
        },

        submitShare: async function() {
            var skillId = document.getElementById('share-skill-select').value;
            var groupId = document.getElementById('share-group-select').value;
            var message = document.getElementById('share-message').value;

            if (!skillId) {
                this.showNotification('请选择要分享的技能', 'error');
                return;
            }
            if (!groupId) {
                this.showNotification('请选择群组', 'error');
                return;
            }

            try {
                var response = await fetch('/api/skillcenter/share', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ skillId: skillId, groupId: groupId, message: message })
                });

                var data = await response.json();

                if (data.code === 200) {
                    this.showNotification('技能分享成功！', 'success');
                    document.getElementById('share-skill-select').value = '';
                    document.getElementById('share-group-select').value = '';
                    document.getElementById('share-message').value = '';
                } else {
                    this.showNotification(data.message || '分享失败', 'error');
                }
            } catch (error) {
                console.error('分享失败:', error);
                this.showNotification('分享失败，请检查网络连接', 'error');
            }
        },

        loadSharedSkills: async function() {
            var self = this;
            try {
                var response = await fetch('/api/skillcenter/share/shared');
                var data = await response.json();

                var container = document.getElementById('shared-skills-list');

                if (data.code === 200 && data.data && data.data.length > 0) {
                    container.innerHTML = data.data.map(function(share) {
                        return '<div class="skill-item">' +
                            '<div class="skill-icon"><i class="ri-code-line"></i></div>' +
                            '<div class="skill-info">' +
                            '<div class="skill-name">' + (share.skillName || share.skillId) + '</div>' +
                            '<div class="skill-meta">分享到: ' + (share.groupName || share.groupId) + ' | ' + new Date(share.sharedAt).toLocaleString() + '</div>' +
                            '</div>' +
                            '<div class="skill-actions">' +
                            '<button class="btn btn-sm btn-danger" onclick="unshareSkill(\'' + share.id + '\')"><i class="ri-delete-bin-line"></i> 取消分享</button>' +
                            '</div></div>';
                    }).join('');
                } else {
                    container.innerHTML = '<div class="empty-state"><i class="ri-upload-cloud-line"></i><h3>暂无已分享的技能</h3><p>您分享的技能将显示在这里</p></div>';
                }
            } catch (error) {
                console.error('加载已分享技能失败:', error);
            }
        },

        loadReceivedSkills: async function() {
            try {
                var response = await fetch('/api/skillcenter/share/received');
                var data = await response.json();

                var container = document.getElementById('received-skills-list');

                if (data.code === 200 && data.data && data.data.length > 0) {
                    container.innerHTML = data.data.map(function(skill) {
                        return '<div class="skill-item">' +
                            '<div class="skill-icon"><i class="ri-code-line"></i></div>' +
                            '<div class="skill-info">' +
                            '<div class="skill-name">' + skill.skillName + '</div>' +
                            '<div class="skill-meta">分享者: ' + skill.sharerName + ' | 群组: ' + skill.groupName + ' | ' + new Date(skill.receivedAt).toLocaleString() + '</div>' +
                            (skill.message ? '<div style="margin-top: 8px; color: var(--ns-secondary);">' + skill.message + '</div>' : '') +
                            '</div>' +
                            '<div class="skill-actions">' +
                            '<button class="btn btn-sm btn-primary"><i class="ri-download-line"></i> 安装</button>' +
                            '</div></div>';
                    }).join('');
                } else {
                    container.innerHTML = '<div class="empty-state"><i class="ri-download-cloud-line"></i><h3>暂无收到的技能</h3><p>群组成员分享的技能将显示在这里</p></div>';
                }
            } catch (error) {
                console.error('加载收到的技能失败:', error);
            }
        },

        unshareSkill: async function(shareId) {
            var self = this;
            if (!confirm('确定要取消分享吗？')) return;

            try {
                var response = await fetch('/api/skillcenter/share/' + shareId, {
                    method: 'DELETE'
                });

                var data = await response.json();

                if (data.code === 200) {
                    self.showNotification('已取消分享', 'success');
                    self.loadSharedSkills();
                } else {
                    self.showNotification(data.message || '操作失败', 'error');
                }
            } catch (error) {
                console.error('取消分享失败:', error);
                self.showNotification('操作失败，请检查网络连接', 'error');
            }
        },

        showNotification: function(message, type) {
            var existing = document.querySelector('.notification');
            if (existing) existing.remove();

            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.innerHTML = '<i class="ri-' + (type === 'success' ? 'check-line' : 'error-warning-line') + '"></i> ' + message;
            document.body.appendChild(notification);

            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    SkillSharing.init();

    global.switchTab = function(tabName) { SkillSharing.switchTab(tabName); };
    global.loadGroups = function() { SkillSharing.loadGroups(); };
    global.submitShare = function() { SkillSharing.submitShare(); };
    global.loadSharedSkills = function() { SkillSharing.loadSharedSkills(); };
    global.loadReceivedSkills = function() { SkillSharing.loadReceivedSkills(); };
    global.unshareSkill = function(shareId) { SkillSharing.unshareSkill(shareId); };
    global.showNotification = function(message, type) { SkillSharing.showNotification(message, type); };

})(typeof window !== 'undefined' ? window : this);
