class SkillExecution {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.executionHistory = [];
    }

    async init() {
        try {
            await this.loadExecutionHistory();
        } catch (error) {
            console.error('初始化执行历史失败:', error);
            // 使用默认值
            this.executionHistory = [];
            this.renderExecutionHistory();
        }
        this.bindEvents();
    }

    async loadExecutionHistory() {
        try {
            const executionHistory = document.getElementById('execution-history');
            executionHistory.innerHTML = '<div class="loading">加载中...</div>';
            
            const response = await this.llmAPI.getExecutionHistory();
            if (response && (response.code === 200 || response.message === "success")) {
                this.executionHistory = response.data || [];
                this.renderExecutionHistory();
            } else {
                this.executionHistory = [];
                this.renderExecutionHistory();
                this.showError('加载执行历史失败');
            }
        } catch (error) {
            console.error('加载执行历史失败:', error);
            this.executionHistory = [];
            this.renderExecutionHistory();
            this.showError('加载执行历史失败');
        }
    }

    renderExecutionHistory() {
        const executionHistory = document.getElementById('execution-history');
        let html = '';

        if (this.executionHistory.length === 0) {
            executionHistory.innerHTML = '<div class="empty-state">暂无执行历史</div>';
            return;
        }

        this.executionHistory.forEach(execution => {
            html += `<div class="execution-item" data-execution-id="${execution.executionId}">`;
            html += `<div class="execution-header">`;
            html += `<div class="execution-skill">`;
            html += `<i class="ri-play-circle-line"></i>`;
            html += `<span class="execution-skill-name">${execution.skillName}</span>`;
            html += `</div>`;
            html += `<div class="execution-info">`;
            html += `<span class="execution-time">${this.formatTime(execution.executeTime)}</span>`;
            html += `<span class="execution-status">${this.getStatusText(execution.status)}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="execution-result">`;
            html += `<h4>执行结果</h4>`;
            html += `<pre>${execution.result || '无结果'}</pre>`;
            html += `</div>`;
            html += `<div class="execution-actions">`;
            html += `<button class="btn btn-sm btn-secondary" onclick="skillExecution.viewExecutionLog('${execution.executionId}')">`;
            html += `<i class="ri-file-list-line"></i>`;
            html += `查看日志</button>`;
            html += `<button class="btn btn-sm btn-secondary" onclick="skillExecution.downloadExecutionResult('${execution.executionId}')">`;
            html += `<i class="ri-download-line"></i>`;
            html += `下载结果</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        executionHistory.innerHTML = html;
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

    formatTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleString('zh-CN');
    }

    getStatusText(status) {
        const statusMap = {
            'RUNNING': '运行中',
            'COMPLETED': '已完成',
            'FAILED': '失败',
            'CANCELLED': '已取消'
        };
        return statusMap[status] || status;
    }

    async viewExecutionLog(executionId) {
        try {
            const response = await this.llmAPI.getExecutionLog(executionId);
            if (response.code === 200 || response.message === "success") {
                alert(response.data);
            } else {
                this.showError('获取日志失败');
            }
        } catch (error) {
            console.error('获取执行日志失败:', error);
            this.showError('获取日志失败，请检查网络连接');
        }
    }

    async downloadExecutionResult(executionId) {
        try {
            const response = await this.llmAPI.getExecutionResult(executionId);
            if (response.code === 200 || response.message === "success") {
                const resultText = JSON.stringify(response.data, null, 2);
                const blob = new Blob([resultText], { type: 'application/json' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `execution-result-${executionId}.json`;
                a.click();
                URL.revokeObjectURL(url);
                
                this.showSuccess('下载成功');
            } else {
                this.showError('下载失败');
            }
        } catch (error) {
            console.error('下载执行结果失败:', error);
            this.showError('下载失败，请检查网络连接');
        }
    }

    async exportExecutionHistory() {
        try {
            if (this.executionHistory.length === 0) {
                this.showError('暂无执行历史可导出');
                return;
            }
            
            const historyText = this.executionHistory.map(exec => 
                `[${new Date(exec.executeTime).toLocaleString()}] [${exec.status}] ${exec.skillName}: ${exec.result || '无结果'}`
            ).join('\n');
            
            const blob = new Blob([historyText], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `execution-history-${Date.now()}.txt`;
            a.click();
            URL.revokeObjectURL(url);
            
            this.showSuccess('导出成功');
        } catch (error) {
            console.error('导出执行历史失败:', error);
            this.showError('导出失败，请检查网络连接');
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

let skillExecution;
document.addEventListener('DOMContentLoaded', () => {
    skillExecution = new SkillExecution();
    skillExecution.init();
});

async function refreshExecutionHistory() {
    if (skillExecution) {
        await skillExecution.loadExecutionHistory();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}

async function exportExecutionHistory() {
    if (skillExecution) {
        await skillExecution.exportExecutionHistory();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}
