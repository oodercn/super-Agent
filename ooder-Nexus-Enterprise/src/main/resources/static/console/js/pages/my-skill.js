(function(global) {
    'use strict';

    var MySkill = {
        init: function() {
            window.onPageInit = function() {
                console.log('我的技能页面初始化完成');
                MySkill.loadSkills();
            };
        },

        loadSkills: async function() {
            try {
                var response = await fetch('/api/personal/skills/list', { method: 'POST' });
                var contentType = response.headers.get('content-type');
                var result;
                
                if (contentType && contentType.includes('application/json')) {
                    result = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100));
                }
                
                if (result.requestStatus === 200) {
                    MySkill.renderSkillList(result.data);
                } else {
                    MySkill.showError('加载技能列表失败');
                }
            } catch (error) {
                console.error('加载技能列表错误:', error);
                MySkill.showError('加载技能列表失败');
            }
        },

        renderSkillList: function(skills) {
            var skillList = document.getElementById('skill-list');
            skillList.innerHTML = '';
            
            if (!skills || skills.length === 0) {
                skillList.innerHTML = '<div class="nx-text-center nx-py-8 nx-text-secondary">暂无技能</div>';
                return;
            }
            
            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = '\
                    <div class="skill-info">\
                        <h3>' + skill.name + '</h3>\
                        <p>' + (skill.description || '暂无描述') + '</p>\
                        <span class="skill-category">' + MySkill.getCategoryName(skill.category) + '</span>\
                    </div>\
                    <div class="skill-actions">\
                        <button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editSkill(\'' + skill.id + '\')">\
                            <i class="ri-edit-line"></i> 编辑\
                        </button>\
                        <button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteSkill(\'' + skill.id + '\')">\
                            <i class="ri-delete-line"></i> 删除\
                        </button>\
                    </div>\
                ';
                skillList.appendChild(skillItem);
            });
        },

        getCategoryName: function(category) {
            var categoryMap = {
                'text-processing': '文本处理',
                'development': '开发工具',
                'deployment': '部署工具',
                'media': '媒体处理',
                'storage': '存储管理'
            };
            return categoryMap[category] || category;
        },

        showPublishModal: function() {
            document.getElementById('publish-modal').style.display = 'flex';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        publishSkill: async function() {
            var name = document.getElementById('skill-name').value;
            var description = document.getElementById('skill-description').value;
            var category = document.getElementById('skill-category').value;
            var code = document.getElementById('skill-code').value;
            
            if (!name || !code) {
                MySkill.showError('请填写技能名称和代码');
                return;
            }
            
            try {
                var response = await fetch('/api/personal/skills/create', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ name: name, description: description, category: category, code: code })
                });
                
                var contentType = response.headers.get('content-type');
                var rs;
                
                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100));
                }
                
                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MySkill.closeModal('publish-modal');
                    MySkill.showSuccess('技能发布成功');
                    MySkill.loadSkills();
                } else {
                    MySkill.showError(rs.message || '技能发布失败');
                }
            } catch (error) {
                console.error('发布技能错误:', error);
                MySkill.showError('技能发布失败');
            }
        },

        editSkill: async function(skillId) {
            try {
                var response = await fetch('/api/personal/skills/get', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId })
                });
                var rs = await response.json();
                
                if (rs.requestStatus === 200 && rs.data) {
                    document.getElementById('edit-skill-id').value = rs.data.id;
                    document.getElementById('edit-skill-name').value = rs.data.name || '';
                    document.getElementById('edit-skill-description').value = rs.data.description || '';
                    document.getElementById('edit-skill-category').value = rs.data.category || '';
                    document.getElementById('edit-skill-code').value = rs.data.code || '';
                    document.getElementById('edit-modal').style.display = 'flex';
                } else {
                    MySkill.showError(rs.message || '获取技能信息失败');
                }
            } catch (error) {
                console.error('获取技能信息错误:', error);
                MySkill.showError('获取技能信息失败');
            }
        },

        closeEditModal: function() {
            document.getElementById('edit-modal').style.display = 'none';
        },

        submitEditSkill: async function() {
            var id = document.getElementById('edit-skill-id').value;
            var name = document.getElementById('edit-skill-name').value;
            var description = document.getElementById('edit-skill-description').value;
            var category = document.getElementById('edit-skill-category').value;
            var code = document.getElementById('edit-skill-code').value;
            
            if (!name) {
                MySkill.showError('请填写技能名称');
                return;
            }
            
            try {
                var response = await fetch('/api/personal/skills/update', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: id, name: name, description: description, category: category, code: code })
                });
                var rs = await response.json();
                
                if (rs.requestStatus === 200) {
                    MySkill.closeEditModal();
                    MySkill.showSuccess('技能更新成功');
                    MySkill.loadSkills();
                } else {
                    MySkill.showError(rs.message || '技能更新失败');
                }
            } catch (error) {
                console.error('更新技能错误:', error);
                MySkill.showError('技能更新失败');
            }
        },

        deleteSkill: async function(skillId) {
            if (!confirm('确定要删除此技能吗？')) return;
            
            try {
                var response = await fetch('/api/personal/skills/delete', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id: skillId })
                });
                var contentType = response.headers.get('content-type');
                var rs;
                
                if (contentType && contentType.includes('application/json')) {
                    rs = await response.json();
                } else {
                    var text = await response.text();
                    throw new Error('非JSON响应: ' + text.substring(0, 100));
                }
                
                if (rs.requestStatus === 200 || rs.status === 'success') {
                    MySkill.showSuccess('技能删除成功');
                    MySkill.loadSkills();
                } else {
                    MySkill.showError(rs.message || '技能删除失败');
                }
            } catch (error) {
                console.error('删除技能错误:', error);
                MySkill.showError('技能删除失败');
            }
        },

        showError: function(message) { MySkill.showNotification(message, 'error'); },
        showSuccess: function(message) { MySkill.showNotification(message, 'success'); },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MySkill.init();

    global.showPublishModal = MySkill.showPublishModal;
    global.closeModal = MySkill.closeModal;
    global.publishSkill = MySkill.publishSkill;
    global.editSkill = MySkill.editSkill;
    global.closeEditModal = MySkill.closeEditModal;
    global.submitEditSkill = MySkill.submitEditSkill;
    global.deleteSkill = MySkill.deleteSkill;
    global.MySkill = MySkill;

})(typeof window !== 'undefined' ? window : this);
