(function(global) {
    'use strict';

    var syncAPI = null;
    var skillList = [];
    var filter = { category: '', keyword: '' };

    var SkillDownload = {
        init: function() {
            window.onPageInit = function() {
                console.log('技能下载页面初始化完成');
                syncAPI = new SkillCenterSyncAPI();
                SkillDownload.loadSkillList();
                SkillDownload.bindEvents();
            };
        },

        bindEvents: function() {
            document.getElementById('search-input').addEventListener('input', function(e) {
                filter.keyword = e.target.value;
                SkillDownload.loadSkillList();
            });

            document.getElementById('category-filter').addEventListener('change', function(e) {
                filter.category = e.target.value;
                SkillDownload.loadSkillList();
            });
        },

        loadSkillList: function() {
            syncAPI.getSkillList(filter.category, 0, 50)
                .then(function(response) {
                    if (response.success) {
                        skillList = response.data;
                        SkillDownload.renderSkillList();
                    } else {
                        SkillDownload.showError('加载技能列表失败');
                    }
                })
                .catch(function(error) {
                    console.error('加载技能列表错误:', error);
                    SkillDownload.showError('加载技能列表失败');
                });
        },

        renderSkillList: function() {
            var listEl = document.getElementById('skill-list');
            if (skillList.length === 0) {
                listEl.innerHTML = '<div class="empty-state">暂无技能</div>';
                return;
            }

            var html = '';
            skillList.forEach(function(skill) {
                html += '<div class="skill-item">' +
                    '<div class="skill-icon">' +
                    '<i class="' + (skill.icon || 'ri-file-line') + '"></i>' +
                    '</div>' +
                    '<div class="skill-info">' +
                    '<h3 class="skill-name">' + skill.skillName + '</h3>' +
                    '<p class="skill-description">' + skill.description + '</p>' +
                    '<div class="skill-meta">' +
                    '<span>版本：' + skill.version + '</span>' +
                    '<span>作者：' + (skill.author || '未知') + '</span>' +
                    '<span>评分：' + skill.rating + '</span>' +
                    '</div>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                    '<button class="nx-btn nx-btn--primary" onclick="downloadSkill(\'' + skill.skillId + '\')">' +
                    '<i class="ri-download-line"></i> 下载' +
                    '</button>' +
                    '</div>' +
                    '</div>';
            });
            listEl.innerHTML = html;
        },

        downloadSkill: function(skillId) {
            syncAPI.downloadSkill(skillId)
                .then(function(blob) {
                    var url = URL.createObjectURL(blob);
                    var a = document.createElement('a');
                    a.href = url;
                    a.download = 'skill-' + skillId + '.zip';
                    a.click();
                    URL.revokeObjectURL(url);
                    SkillDownload.showSuccess('技能下载成功');
                })
                .catch(function(error) {
                    console.error('技能下载错误:', error);
                    SkillDownload.showError('技能下载失败');
                });
        },

        refreshSkillList: function() {
            SkillDownload.loadSkillList();
        },

        showSuccess: function(message) {
            SkillDownload.showNotification(message, 'success');
        },

        showError: function(message) {
            SkillDownload.showNotification(message, 'error');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification ' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.classList.add('show'); }, 10);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    SkillDownload.init();

    global.downloadSkill = SkillDownload.downloadSkill;
    global.refreshSkillList = SkillDownload.refreshSkillList;

})(typeof window !== 'undefined' ? window : this);
