(function(global) {
    'use strict';

    var MarketManagement = {
        init: function() {
            window.onPageInit = function() {
                MarketManagement.loadMarketSkills();
                MarketManagement.loadEnterpriseAPIs();
            };
        },

        loadMarketSkills: function() {
            var marketSkills = document.getElementById('market-skills');
            marketSkills.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/market/skills/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        MarketManagement.renderMarketSkills(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载市场技能列表错误:', error);
                    document.getElementById('market-skills').innerHTML = '<div class="empty-state">加载失败</div>';
                    MarketManagement.showError('加载市场技能列表失败');
                });
        },

        renderMarketSkills: function(skills) {
            var skillList = document.getElementById('market-skills');
            skillList.innerHTML = '';

            if (skills.length === 0) {
                skillList.innerHTML = '<div class="empty-state">暂无市场技能</div>';
                return;
            }

            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = '<div class="skill-info">' +
                    '<h3>' + skill.name + '</h3>' +
                    '<p>' + (skill.description || '暂无描述') + '</p>' +
                    '<span class="skill-category">' + MarketManagement.getCategoryName(skill.category) + '</span>' +
                    '<span class="skill-author">作者: ' + skill.author + '</span>' +
                    '<span class="skill-downloads">下载: ' + (skill.downloadCount || 0) + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                    '<button class="btn btn-secondary" onclick="viewSkill(\'' + skill.id + '\')"><i class="ri-eye-line"></i> 查看</button>' +
                    '<button class="btn btn-danger" onclick="removeSkill(\'' + skill.id + '\')"><i class="ri-delete-line"></i> 移除</button>' +
                    '</div>';
                skillList.appendChild(skillItem);
            });
        },

        loadEnterpriseAPIs: function() {
            var enterpriseAPIs = document.getElementById('enterprise-apis');
            enterpriseAPIs.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/market/enterprise/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        MarketManagement.renderEnterpriseAPIs(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载企业级接口列表错误:', error);
                    document.getElementById('enterprise-apis').innerHTML = '<div class="empty-state">加载失败</div>';
                    MarketManagement.showError('加载企业级接口列表失败');
                });
        },

        renderEnterpriseAPIs: function(apis) {
            var apiList = document.getElementById('enterprise-apis');
            apiList.innerHTML = '';

            if (apis.length === 0) {
                apiList.innerHTML = '<div class="empty-state">暂无企业级接口</div>';
                return;
            }

            apis.forEach(function(api) {
                var apiItem = document.createElement('div');
                apiItem.className = 'skill-item';
                apiItem.innerHTML = '<div class="skill-info">' +
                    '<h3>' + api.name + '</h3>' +
                    '<p>' + (api.description || '暂无描述') + '</p>' +
                    '<span class="api-url">' + api.url + '</span>' +
                    '<span class="api-status ' + (api.status === 'active' ? 'success' : 'warning') + '">' + (api.status === 'active' ? '活跃' : '停用') + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                    '<button class="btn btn-secondary" onclick="viewAPI(\'' + api.id + '\')"><i class="ri-eye-line"></i> 查看</button>' +
                    '<button class="btn btn-primary" onclick="editAPI(\'' + api.id + '\')"><i class="ri-edit-line"></i> 编辑</button>' +
                    '<button class="btn btn-danger" onclick="deleteAPI(\'' + api.id + '\')"><i class="ri-delete-line"></i> 删除</button>' +
                    '</div>';
                apiList.appendChild(apiItem);
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

        switchTab: function(tab) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) { btn.classList.remove('active'); });
            document.querySelectorAll('.tab-content').forEach(function(content) { content.style.display = 'none'; });

            event.target.classList.add('active');
            document.getElementById(tab + '-tab').style.display = 'block';
        },

        showImportModal: function() {
            document.getElementById('import-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        importAPI: function() {
            var name = document.getElementById('api-name').value;
            var description = document.getElementById('api-description').value;
            var url = document.getElementById('api-url').value;
            var doc = document.getElementById('api-doc').value;

            if (!name || !url) {
                MarketManagement.showError('请填写API名称和地址');
                return;
            }

            fetch('/api/admin/market/enterprise', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: name, description: description, url: url, doc: doc })
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
                        MarketManagement.closeModal('import-modal');
                        MarketManagement.showSuccess('API导入成功');
                        MarketManagement.loadEnterpriseAPIs();
                    } else {
                        MarketManagement.showError('API导入失败');
                    }
                })
                .catch(function(error) {
                    console.error('导入API错误:', error);
                    MarketManagement.showError('API导入失败');
                });
        },

        viewSkill: function(skillId) {
            MarketManagement.showError('查看技能功能开发中...');
        },

        removeSkill: function(skillId) {
            if (!confirm('确定要移除此技能吗？')) return;

            fetch('/api/admin/market/skills/' + skillId, { method: 'DELETE' })
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
                        MarketManagement.showSuccess('技能移除成功');
                        MarketManagement.loadMarketSkills();
                    } else {
                        MarketManagement.showError('技能移除失败');
                    }
                })
                .catch(function(error) {
                    console.error('移除技能错误:', error);
                    MarketManagement.showError('技能移除失败');
                });
        },

        viewAPI: function(apiId) {
            fetch('/api/admin/market/enterprise/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: apiId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('view-api-name').textContent = rs.data.name || '-';
                        document.getElementById('view-api-type').textContent = rs.data.type || '-';
                        document.getElementById('view-api-status').textContent = rs.data.status || '-';
                        document.getElementById('view-api-calls').textContent = rs.data.dailyCalls || 0;
                        document.getElementById('view-api-modal').style.display = 'flex';
                    } else {
                        MarketManagement.showError(rs.message || '获取API信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取API信息错误:', error);
                    MarketManagement.showError('获取API信息失败');
                });
        },

        closeViewAPIModal: function() {
            document.getElementById('view-api-modal').style.display = 'none';
        },

        editAPI: function(apiId) {
            fetch('/api/admin/market/enterprise/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: apiId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('edit-api-id').value = rs.data.id;
                        document.getElementById('edit-api-name').value = rs.data.name || '';
                        document.getElementById('edit-api-type').value = rs.data.type || 'sync';
                        document.getElementById('edit-api-status').value = rs.data.status || 'active';
                        document.getElementById('edit-api-modal').style.display = 'flex';
                    } else {
                        MarketManagement.showError(rs.message || '获取API信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取API信息错误:', error);
                    MarketManagement.showError('获取API信息失败');
                });
        },

        closeEditAPIModal: function() {
            document.getElementById('edit-api-modal').style.display = 'none';
        },

        submitEditAPI: function() {
            var id = document.getElementById('edit-api-id').value;
            var name = document.getElementById('edit-api-name').value;
            var type = document.getElementById('edit-api-type').value;
            var status = document.getElementById('edit-api-status').value;

            if (!name) {
                MarketManagement.showError('请填写API名称');
                return;
            }

            fetch('/api/admin/market/enterprise/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id, name: name, type: type, status: status })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        MarketManagement.closeEditAPIModal();
                        MarketManagement.showSuccess('API更新成功');
                        MarketManagement.loadEnterpriseAPIs();
                    } else {
                        MarketManagement.showError(rs.message || 'API更新失败');
                    }
                })
                .catch(function(error) {
                    console.error('更新API错误:', error);
                    MarketManagement.showError('API更新失败');
                });
        },

        deleteAPI: function(apiId) {
            if (!confirm('确定要删除此API吗？')) return;
            MarketManagement.showError('删除API功能暂未实现');
        },

        showError: function(message) {
            MarketManagement.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            MarketManagement.showNotification(message, 'success');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    MarketManagement.init();

    global.switchTab = MarketManagement.switchTab;
    global.showImportModal = MarketManagement.showImportModal;
    global.closeModal = MarketManagement.closeModal;
    global.importAPI = MarketManagement.importAPI;
    global.viewSkill = MarketManagement.viewSkill;
    global.removeSkill = MarketManagement.removeSkill;
    global.viewAPI = MarketManagement.viewAPI;
    global.closeViewAPIModal = MarketManagement.closeViewAPIModal;
    global.editAPI = MarketManagement.editAPI;
    global.closeEditAPIModal = MarketManagement.closeEditAPIModal;
    global.submitEditAPI = MarketManagement.submitEditAPI;
    global.deleteAPI = MarketManagement.deleteAPI;

})(typeof window !== 'undefined' ? window : this);
