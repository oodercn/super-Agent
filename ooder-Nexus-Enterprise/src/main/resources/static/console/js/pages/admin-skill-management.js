(function(global) {
    'use strict';

    var SkillManagement = {
        init: function() {
            window.onPageInit = function() {
                SkillManagement.loadSkills();
                SkillManagement.loadCategories();
                SkillManagement.loadApprovals();
            };
        },

        loadSkills: function() {
            var skillList = document.getElementById('skill-list');
            skillList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/skills/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        SkillManagement.renderSkillList(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载技能列表错误:', error);
                    document.getElementById('skill-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillManagement.showError('加载技能列表失败');
                });
        },

        renderSkillList: function(skills) {
            var skillList = document.getElementById('skill-list');
            skillList.innerHTML = '';

            if (skills.length === 0) {
                skillList.innerHTML = '<div class="empty-state">暂无技能</div>';
                return;
            }

            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = '<div class="skill-info">' +
                    '<h3>' + skill.name + '</h3>' +
                    '<p>' + (skill.description || '暂无描述') + '</p>' +
                    '<span class="skill-category">' + SkillManagement.getCategoryName(skill.category) + '</span>' +
                    '<span class="skill-author">作者: ' + skill.author + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                    '<button class="btn btn-secondary" onclick="viewSkill(\'' + skill.id + '\')"><i class="ri-eye-line"></i> 查看</button>' +
                    '<button class="btn btn-danger" onclick="deleteSkill(\'' + skill.id + '\')"><i class="ri-delete-line"></i> 删除</button>' +
                    '</div>';
                skillList.appendChild(skillItem);
            });
        },

        loadCategories: function() {
            var categoryList = document.getElementById('category-list');
            categoryList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/categories/list', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        SkillManagement.renderCategoryList(rs.data);
                    } else {
                        throw new Error(rs.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载分类列表错误:', error);
                    document.getElementById('category-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillManagement.showError('加载分类列表失败');
                });
        },

        renderCategoryList: function(categories) {
            var categoryList = document.getElementById('category-list');
            categoryList.innerHTML = '';

            if (categories.length === 0) {
                categoryList.innerHTML = '<div class="empty-state">暂无分类</div>';
                return;
            }

            categories.forEach(function(category) {
                var categoryItem = document.createElement('div');
                categoryItem.className = 'category-item';
                categoryItem.innerHTML = '<div class="category-info">' +
                    '<h3>' + category.name + '</h3>' +
                    '<p>' + (category.description || '暂无描述') + '</p>' +
                    '<span class="category-count">' + (category.skillCount || 0) + ' 个技能</span>' +
                    '</div>' +
                    '<div class="category-actions">' +
                    '<button class="btn btn-secondary" onclick="editCategory(\'' + category.id + '\')"><i class="ri-edit-line"></i> 编辑</button>' +
                    '<button class="btn btn-danger" onclick="deleteCategory(\'' + category.id + '\')"><i class="ri-delete-line"></i> 删除</button>' +
                    '</div>';
                categoryList.appendChild(categoryItem);
            });
        },

        loadApprovals: function() {
            var approvalList = document.getElementById('approval-list');
            approvalList.innerHTML = '<div class="loading">加载中...</div>';

            fetch('/api/admin/skills/pending')
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
                    if (result && result.code === 200) {
                        SkillManagement.renderApprovalList(result.data || []);
                    } else {
                        throw new Error(result.message || '响应数据格式错误');
                    }
                })
                .catch(function(error) {
                    console.error('加载审批列表错误:', error);
                    document.getElementById('approval-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    SkillManagement.showError('加载审批列表失败');
                });
        },

        renderApprovalList: function(skills) {
            var approvalList = document.getElementById('approval-list');
            approvalList.innerHTML = '';

            if (skills.length === 0) {
                approvalList.innerHTML = '<div class="empty-state">暂无待审批技能</div>';
                return;
            }

            skills.forEach(function(skill) {
                var skillItem = document.createElement('div');
                skillItem.className = 'skill-item';
                skillItem.innerHTML = '<div class="skill-info">' +
                    '<h3>' + skill.name + '</h3>' +
                    '<p>' + (skill.description || '暂无描述') + '</p>' +
                    '<span class="skill-author">作者: ' + skill.author + '</span>' +
                    '<span class="skill-time">提交时间: ' + SkillManagement.formatTime(skill.submittedAt) + '</span>' +
                    '</div>' +
                    '<div class="skill-actions">' +
                    '<button class="btn btn-primary" onclick="approveSkill(\'' + skill.id + '\')"><i class="ri-check-line"></i> 通过</button>' +
                    '<button class="btn btn-danger" onclick="rejectSkill(\'' + skill.id + '\')"><i class="ri-close-line"></i> 拒绝</button>' +
                    '</div>';
                approvalList.appendChild(skillItem);
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

        showAddCategoryModal: function() {
            document.getElementById('add-category-modal').style.display = 'block';
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).style.display = 'none';
        },

        addCategory: function() {
            var name = document.getElementById('category-name').value;
            var description = document.getElementById('category-description').value;

            if (!name) {
                SkillManagement.showError('请填写分类名称');
                return;
            }

            fetch('/api/admin/categories/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: name, description: description })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        SkillManagement.closeModal('add-category-modal');
                        SkillManagement.showSuccess('分类添加成功');
                        SkillManagement.loadCategories();
                    } else {
                        SkillManagement.showError(rs.message || '分类添加失败');
                    }
                })
                .catch(function(error) {
                    console.error('添加分类错误:', error);
                    SkillManagement.showError('分类添加失败');
                });
        },

        editCategory: function(categoryId) {
            fetch('/api/admin/categories/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: categoryId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('edit-category-id').value = rs.data.id;
                        document.getElementById('edit-category-name').value = rs.data.name || '';
                        document.getElementById('edit-category-description').value = rs.data.description || '';
                        document.getElementById('edit-category-icon').value = rs.data.icon || 'ri-folder-line';
                        document.getElementById('edit-category-modal').style.display = 'flex';
                    } else {
                        SkillManagement.showError(rs.message || '获取分类信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取分类信息错误:', error);
                    SkillManagement.showError('获取分类信息失败');
                });
        },

        closeEditCategoryModal: function() {
            document.getElementById('edit-category-modal').style.display = 'none';
        },

        submitEditCategory: function() {
            var id = document.getElementById('edit-category-id').value;
            var name = document.getElementById('edit-category-name').value;
            var description = document.getElementById('edit-category-description').value;
            var icon = document.getElementById('edit-category-icon').value;

            if (!name) {
                SkillManagement.showError('请填写分类名称');
                return;
            }

            fetch('/api/admin/categories/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id, name: name, description: description, icon: icon })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        SkillManagement.closeEditCategoryModal();
                        SkillManagement.showSuccess('分类更新成功');
                        SkillManagement.loadCategories();
                    } else {
                        SkillManagement.showError(rs.message || '分类更新失败');
                    }
                })
                .catch(function(error) {
                    console.error('更新分类错误:', error);
                    SkillManagement.showError('分类更新失败');
                });
        },

        deleteCategory: function(categoryId) {
            if (!confirm('确定要删除此分类吗？')) return;

            fetch('/api/admin/categories/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: categoryId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        SkillManagement.showSuccess('分类删除成功');
                        SkillManagement.loadCategories();
                    } else {
                        SkillManagement.showError(rs.message || '分类删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除分类错误:', error);
                    SkillManagement.showError('分类删除失败');
                });
        },

        viewSkill: function(skillId) {
            fetch('/api/admin/skills/get', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: skillId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        document.getElementById('view-skill-name').textContent = rs.data.name || '';
                        document.getElementById('view-skill-description').textContent = rs.data.description || '';
                        document.getElementById('view-skill-category').textContent = rs.data.category || '';
                        document.getElementById('view-skill-status').textContent = rs.data.status || '';
                        document.getElementById('view-skill-modal').style.display = 'flex';
                    } else {
                        SkillManagement.showError(rs.message || '获取技能信息失败');
                    }
                })
                .catch(function(error) {
                    console.error('获取技能信息错误:', error);
                    SkillManagement.showError('获取技能信息失败');
                });
        },

        closeViewSkillModal: function() {
            document.getElementById('view-skill-modal').style.display = 'none';
        },

        deleteSkill: function(skillId) {
            if (!confirm('确定要删除此技能吗？')) return;

            fetch('/api/admin/skills/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: skillId })
            })
                .then(function(response) { return response.json(); })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        SkillManagement.showSuccess('技能删除成功');
                        SkillManagement.loadSkills();
                    } else {
                        SkillManagement.showError(rs.message || '技能删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除技能错误:', error);
                    SkillManagement.showError('技能删除失败');
                });
        },

        approveSkill: function(skillId) {
            fetch('/api/admin/skills/' + skillId + '/approve', { method: 'POST' })
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
                    if (result && result.code === 200) {
                        SkillManagement.showSuccess('技能已通过审批');
                        SkillManagement.loadApprovals();
                    } else {
                        SkillManagement.showError(result.message || '审批失败');
                    }
                })
                .catch(function(error) {
                    console.error('审批技能错误:', error);
                    SkillManagement.showError('审批失败');
                });
        },

        rejectSkill: function(skillId) {
            if (!confirm('确定要拒绝此技能吗？')) return;

            fetch('/api/admin/skills/' + skillId + '/reject', { method: 'POST' })
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
                    if (result && result.code === 200) {
                        SkillManagement.showSuccess('技能已拒绝');
                        SkillManagement.loadApprovals();
                    } else {
                        SkillManagement.showError(result.message || '拒绝失败');
                    }
                })
                .catch(function(error) {
                    console.error('拒绝技能错误:', error);
                    SkillManagement.showError('拒绝失败');
                });
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '未知';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },

        showError: function(message) {
            SkillManagement.showNotification(message, 'error');
        },

        showSuccess: function(message) {
            SkillManagement.showNotification(message, 'success');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    SkillManagement.init();

    global.switchTab = SkillManagement.switchTab;
    global.showAddCategoryModal = SkillManagement.showAddCategoryModal;
    global.closeModal = SkillManagement.closeModal;
    global.addCategory = SkillManagement.addCategory;
    global.editCategory = SkillManagement.editCategory;
    global.closeEditCategoryModal = SkillManagement.closeEditCategoryModal;
    global.submitEditCategory = SkillManagement.submitEditCategory;
    global.deleteCategory = SkillManagement.deleteCategory;
    global.viewSkill = SkillManagement.viewSkill;
    global.closeViewSkillModal = SkillManagement.closeViewSkillModal;
    global.deleteSkill = SkillManagement.deleteSkill;
    global.approveSkill = SkillManagement.approveSkill;
    global.rejectSkill = SkillManagement.rejectSkill;

})(typeof window !== 'undefined' ? window : this);
