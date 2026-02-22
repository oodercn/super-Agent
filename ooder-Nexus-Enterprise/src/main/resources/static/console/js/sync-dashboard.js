class SyncDashboard {
    constructor() {
        this.syncAPI = new SkillCenterSyncAPI();
        this.syncStatus = null;
    }

    async init() {
        await this.loadSyncStatus();
        this.bindEvents();
    }

    async loadSyncStatus() {
        try {
            const response = await this.syncAPI.getSyncStatus();
            if (response.code === 200 || response.message === "success") {
                this.syncStatus = response.data;
                this.renderSyncStatus();
            } else {
                this.showError('加载同步状态失败');
            }
        } catch (error) {
            console.error('加载同步状态错误:', error);
            this.showError('加载同步状态失败');
        }
    }

    renderSyncStatus() {
        if (this.syncStatus) {
            document.getElementById('synced-count').textContent = this.syncStatus.completedSkills || 0;
            document.getElementById('sync-failed-count').textContent = this.syncStatus.failedSkills || 0;
            
            if (this.syncStatus.endTime) {
                const lastSyncTime = new Date(this.syncStatus.endTime).toLocaleString('zh-CN');
                document.getElementById('last-sync-time').textContent = lastSyncTime;
            }
        }
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

let syncDashboard;
document.addEventListener('DOMContentLoaded', () => {
    syncDashboard = new SyncDashboard();
    syncDashboard.init();
});

async function refreshSyncStatus() {
    await syncDashboard.loadSyncStatus();
}

function showBatchSyncModal() {
    alert('批量同步功能开发中...');
}
