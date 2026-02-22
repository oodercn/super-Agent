class UserPreferences {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.preferences = {};
        this.userId = 'default-user';
    }

    async init() {
        try {
            await this.loadPreferences();
        } catch (error) {
            console.error('初始化偏好设置失败:', error);
            // 使用默认值
            this.preferences = {
                userId: this.userId,
                apiToken: '',
                defaultCategory: '',
                skillSort: 'name',
                theme: 'light',
                language: 'zh-CN'
            };
            this.renderPreferences();
        }
        this.bindEvents();
    }

    async loadPreferences() {
        try {
            const response = await this.llmAPI.getUserPreferences(this.userId);
            if (response && (response.code === 200 || response.message === "success")) {
                this.preferences = response.data || {};
                this.renderPreferences();
            } else {
                this.preferences = {};
                this.renderPreferences();
                this.showError('加载偏好设置失败');
            }
        } catch (error) {
            console.error('加载偏好设置失败:', error);
            this.preferences = {};
            this.renderPreferences();
            this.showError('加载偏好设置失败');
        }
    }

    renderPreferences() {
        document.getElementById('user-id').value = this.preferences.userId || this.userId;
        document.getElementById('api-token').value = this.preferences.apiToken || '';
        document.getElementById('default-category').value = this.preferences.defaultCategory || '';
        document.getElementById('skill-sort').value = this.preferences.skillSort || 'name';
        document.getElementById('theme').value = this.preferences.theme || 'light';
        document.getElementById('language').value = this.preferences.language || 'zh-CN';
    }

    bindEvents() {
        // 绑定菜单事件
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });
    }

    async savePreferences() {
        try {
            const preferences = {
                userId: document.getElementById('user-id').value,
                apiToken: document.getElementById('api-token').value,
                defaultCategory: document.getElementById('default-category').value,
                skillSort: document.getElementById('skill-sort').value,
                theme: document.getElementById('theme').value,
                language: document.getElementById('language').value
            };

            const response = await this.llmAPI.updateUserPreferences(preferences);
            if (response && (response.code === 200 || response.message === "success")) {
                this.showSuccess('偏好保存成功');
            } else {
                this.showError('偏好保存失败');
            }
        } catch (error) {
            console.error('保存偏好设置失败:', error);
            this.showError('保存失败，请检查网络连接');
        }
    }

    async resetPreferences() {
        if (confirm('确定要重置为默认设置吗？')) {
            try {
                const preferences = {
                    userId: this.userId,
                    apiToken: '',
                    defaultCategory: '',
                    skillSort: 'name',
                    theme: 'light',
                    language: 'zh-CN'
                };

                const response = await this.llmAPI.updateUserPreferences(preferences);
                if (response && (response.code === 200 || response.message === "success")) {
                    this.showSuccess('偏好重置成功');
                    this.preferences = preferences;
                    this.renderPreferences();
                } else {
                    this.showError('偏好重置失败');
                }
            } catch (error) {
                console.error('重置偏好设置失败:', error);
                this.showError('重置失败，请检查网络连接');
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
        window.location.href = `/console/pages/llm-integration/${page}.html`;
    }
}

let userPreferences;
document.addEventListener('DOMContentLoaded', () => {
    userPreferences = new UserPreferences();
    userPreferences.init();
});

async function savePreferences() {
    if (userPreferences) {
        await userPreferences.savePreferences();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}

async function resetPreferences() {
    if (userPreferences) {
        await userPreferences.resetPreferences();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}
