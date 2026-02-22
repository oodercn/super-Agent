class SkillCategories {
    constructor() {
        this.syncAPI = new SkillCenterSyncAPI();
        this.categories = [];
    }

    async init() {
        await this.loadCategories();
        this.bindEvents();
    }

    async loadCategories() {
        const response = await this.syncAPI.getCategories();
        if (response.code === 200 || response.message === "success") {
            this.categories = response.data;
            this.renderCategories();
        }
    }

    renderCategories() {
        const categoryList = document.getElementById('category-list');
        let html = '';

        if (this.categories.length === 0) {
            categoryList.innerHTML = '<div class="empty-state">暂无分类</div>';
            return;
        }

        this.categories.forEach(category => {
            html += `<div class="category-item">`;
            html += `<div class="category-icon">`;
            html += `<i class="${category.icon || 'ri-folder-line'}"></i>`;
            html += `</div>`;
            html += `<div class="category-info">`;
            html += `<h3 class="category-name">${category.name}</h3>`;
            html += `<p class="category-description">${category.description || '无描述'}</p>`;
            html += `<div class="category-meta">`;
            html += `<span class="category-count">技能数量：${category.skillCount}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="category-actions">`;
            html += `<button class="btn btn-sm btn-secondary" onclick="skillCategories.editCategory('${category.id}')">`;
            html += `<i class="ri-edit-line"></i>`;
            html += `编辑</button>`;
            html += `<button class="btn btn-sm btn-danger" onclick="skillCategories.deleteCategory('${category.id}')">`;
            html += `<i class="ri-delete-bin-line"></i>`;
            html += `删除</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        categoryList.innerHTML = html;
    }

    bindEvents() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });
    }

    async createCategory() {
        const categoryData = {
            name: document.getElementById('category-name').value,
            description: document.getElementById('category-description').value,
            icon: document.getElementById('category-icon').value
        };

        const response = await this.syncAPI.createCategory(categoryData);
        if (response.code === 200 || response.message === "success") {
            this.showSuccess('分类创建成功');
            closeModal('create-category-modal');
            await this.loadCategories();
        } else {
            this.showError('分类创建失败');
        }
    }

    async editCategory(categoryId) {
        alert('编辑功能开发中...');
    }

    async deleteCategory(categoryId) {
        if (confirm('确定要删除该分类吗？')) {
            const response = await this.syncAPI.deleteCategory(categoryId);
            if (response.code === 200 || response.message === "success") {
                this.showSuccess('分类删除成功');
                await this.loadCategories();
            } else {
                this.showError('分类删除失败');
            }
        }
    }

    showSuccess(message) {
        this.showNotification(message, 'success');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    }

    loadPage(page) {
        window.location.href = `/console/pages/skillcenter-sync/${page}.html`;
    }
}

let skillCategories;
document.addEventListener('DOMContentLoaded', () => {
    skillCategories = new SkillCategories();
    skillCategories.init();
});

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showCreateCategoryModal() {
    document.getElementById('create-category-modal').style.display = 'flex';
}

async function refreshCategories() {
    await skillCategories.loadCategories();
}

async function createCategory() {
    await skillCategories.createCategory();
}

async function editCategory(categoryId) {
    await skillCategories.editCategory(categoryId);
}

async function deleteCategory(categoryId) {
    await skillCategories.deleteCategory(categoryId);
}
