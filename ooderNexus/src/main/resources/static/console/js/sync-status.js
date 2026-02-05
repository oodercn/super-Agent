class SyncStatusPage {
    constructor() {
        this.syncAPI = new SkillCenterSyncAPI();
        this.syncStatus = null;
    }

    async init() {
        await this.loadSyncStatus();
        this.bindEvents();
    }

    async loadSyncStatus() {
        const response = await this.syncAPI.getSyncStatus();
        if (response.code === 200 || response.message === "success") {
            this.syncStatus = response.data;
            this.renderSyncStatus();
        }
    }

    renderSyncStatus() {
        const syncStatusList = document.getElementById('sync-status-list');
        let html = '';

        if (!this.syncStatus || !this.syncStatus.tasks || this.syncStatus.tasks.length === 0) {
            syncStatusList.innerHTML = '<div class="empty-state">暂无同步任务</div>';
            return;
        }

        html += `<div class="sync-summary">`;
        html += `<div class="summary-item">`;
        html += `<span class="summary-label">总任务数：</span>`;
        html += `<span class="summary-value">${this.syncStatus.totalSkills}</span>`;
        html += `</div>`;
        html += `<div class="summary-item">`;
        html += `<span class="summary-label">已完成：</span>`;
        html += `<span class="summary-value success">${this.syncStatus.completedSkills}</span>`;
        html += `</div>`;
        html += `<div class="summary-item">`;
        html += `<span class="summary-label">失败：</span>`;
        html += `<span class="summary-value error">${this.syncStatus.failedSkills}</span>`;
        html += `</div>`;
        html += `<div class="summary-item">`;
        html += `<span class="summary-label">状态：</span>`;
        html += `<span class="summary-value ${this.getStateClass(this.syncStatus.state)}">${this.getStateText(this.syncStatus.state)}</span>`;
        html += `</div>`;
        html += `</div>`;

        html += `<h3>同步任务详情</h3>`;
        this.syncStatus.tasks.forEach(task => {
            html += `<div class="sync-task-item">`;
            html += `<div class="task-header">`;
            html += `<span class="task-name">${task.skillName || task.skillId}</span>`;
            html += `<span class="task-status ${this.getTaskStateClass(task.state)}">${this.getTaskStateText(task.state)}</span>`;
            html += `</div>`;
            if (task.errorMessage) {
                html += `<div class="task-error">`;
                html += `<strong>错误信息：</strong>${task.errorMessage}`;
                html += `</div>`;
            }
            html += `<div class="task-time">`;
            html += `<span>开始时间：${new Date(task.startTime).toLocaleString('zh-CN')}</span>`;
            if (task.endTime) {
                html += `<span>结束时间：${new Date(task.endTime).toLocaleString('zh-CN')}</span>`;
            }
            html += `</div>`;
            html += `</div>`;
        });

        syncStatusList.innerHTML = html;
    }

    getStateText(state) {
        const stateMap = {
            'PENDING': '等待中',
            'IN_PROGRESS': '进行中',
            'COMPLETED': '已完成',
            'FAILED': '失败',
            'CANCELLED': '已取消'
        };
        return stateMap[state] || state;
    }

    getStateClass(state) {
        const classMap = {
            'PENDING': 'pending',
            'IN_PROGRESS': 'in-progress',
            'COMPLETED': 'completed',
            'FAILED': 'failed',
            'CANCELLED': 'cancelled'
        };
        return classMap[state] || '';
    }

    getTaskStateText(state) {
        const stateMap = {
            'PENDING': '等待中',
            'IN_PROGRESS': '进行中',
            'COMPLETED': '已完成',
            'FAILED': '失败',
            'CANCELLED': '已取消'
        };
        return stateMap[state] || state;
    }

    getTaskStateClass(state) {
        const classMap = {
            'PENDING': 'pending',
            'IN_PROGRESS': 'in-progress',
            'COMPLETED': 'completed',
            'FAILED': 'failed',
            'CANCELLED': 'cancelled'
        };
        return classMap[classMap] || '';
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

let syncStatusPage;
document.addEventListener('DOMContentLoaded', () => {
    syncStatusPage = new SyncStatusPage();
    syncStatusPage.init();
});

async function refreshSyncStatus() {
    await syncStatusPage.loadSyncStatus();
}

function cancelAllSync() {
    alert('取消全部同步功能开发中...');
}
