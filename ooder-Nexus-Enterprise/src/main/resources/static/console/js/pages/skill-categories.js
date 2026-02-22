(function(global) {
    'use strict';

    var syncAPI = null;
    var categories = [];

    var SkillCategories = {
        init: function() {
            window.onPageInit = function() {
                console.log('技能分类页面初始化完成');
                syncAPI = new SkillCenterSyncAPI();
                SkillCategories.loadCategories();
            };
        },

        loadCategories: function() {
            syncAPI.getCategories()
                .then(function(response) {
                    if (response.success) {
                        categories = response.data;
                        SkillCategories.renderCategories();
                    } else {
                        SkillCategories.showError(response.message || '加载分类失败');
                        document.getElementById('category-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    }
                })
                .catch(function(error) {
                    console.error('加载分类错误:', error);
                    SkillCategories.showError('加载分类失败');
                    document.getElementById('category-list').innerHTML = '<div class="empty-state">加载失败</div>';
                });
        },

        renderCategories: function() {
            var categoryList = document.getElementById('category-list');
            if (categories.length === 0) {
                categoryList.innerHTML = '<div class="empty-state">暂无分类</div>';
                return;
            }

            var html = '';
            categories.forEach(function(category) {
                html += '<div class="category-item">' +
                    '<div class="category-icon"><i class="' + (category.icon || 'ri-folder-line') + '"></i></div>' +
                    '<div class="category-info">' +
                    '<h3 class="category-name">' + category.name + '</h3>' +
                    '<p class="category-description">' + (category.description || '无描述') + '</p>' +
                    '<div class="category-meta">技能数量：' + category.skillCount + '</div>' +
                    '</div>' +
                    '<div class="category-actions">' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editCategory(\'' + category.id + '\')"><i class="ri-edit-line"></i> 编辑</button>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteCategory(\'' + category.id + '\')"><i class="ri-delete-bin-line"></i> 删除</button>' +
                    '</div>' +
                    '</div>';
            });
            categoryList.innerHTML = html;
        },

        createCategory: function() {
            var categoryData = {
                name: document.getElementById('category-name').value,
                description: document.getElementById('category-description').value,
                icon: document.getElementById('category-icon').value
            };

            if (!categoryData.name) {
                SkillCategories.showError('分类名称不能为空');
                return;
            }

            syncAPI.createCategory(categoryData)
                .then(function(response) {
                    if (response.success) {
                        SkillCategories.showSuccess('分类创建成功');
                        SkillCategories.closeModal('create-category-modal');
                        document.getElementById('create-category-form').reset();
                        SkillCategories.loadCategories();
                    } else {
                        SkillCategories.showError(response.message || '分类创建失败');
                    }
                })
                .catch(function(error) {
                    console.error('创建分类错误:', error);
                    SkillCategories.showError('分类创建失败');
                });
        },

        editCategory: function(categoryId) {
            alert('编辑功能开发中...');
        },

        deleteCategory: function(categoryId) {
            if (confirm('确定要删除该分类吗？')) {
                syncAPI.deleteCategory(categoryId)
                    .then(function(response) {
                        if (response.success) {
                            SkillCategories.showSuccess('分类删除成功');
                            SkillCategories.loadCategories();
                        } else {
                            SkillCategories.showError(response.message || '分类删除失败');
                        }
                    })
                    .catch(function(error) {
                        console.error('删除分类错误:', error);
                        SkillCategories.showError('分类删除失败');
                    });
            }
        },

        closeModal: function(modalId) {
            document.getElementById(modalId).classList.remove('active');
        },

        showCreateCategoryModal: function() {
            document.getElementById('create-category-modal').classList.add('active');
        },

        refreshCategories: function() {
            document.getElementById('category-list').innerHTML = '<div class="loading">加载中...</div>';
            SkillCategories.loadCategories();
        },

        showSuccess: function(message) {
            SkillCategories.showNotification(message, 'success');
        },

        showError: function(message) {
            SkillCategories.showNotification(message, 'error');
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

    SkillCategories.init();

    global.createCategory = SkillCategories.createCategory;
    global.editCategory = SkillCategories.editCategory;
    global.deleteCategory = SkillCategories.deleteCategory;
    global.closeModal = SkillCategories.closeModal;
    global.showCreateCategoryModal = SkillCategories.showCreateCategoryModal;
    global.refreshCategories = SkillCategories.refreshCategories;

})(typeof window !== 'undefined' ? window : this);
