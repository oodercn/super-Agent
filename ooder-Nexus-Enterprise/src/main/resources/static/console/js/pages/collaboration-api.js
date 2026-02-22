const CollaborationAPI = {
    baseUrl: '/api/collaboration',

    async request(endpoint, body = {}) {
        try {
            const response = await fetch(this.baseUrl + endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(body)
            });
            const rs = await response.json();
            if (rs.requestStatus === 200 || rs.code === 200 || rs.status === 'success') {
                return rs;
            } else {
                throw new Error(rs.message || '请求失败');
            }
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    async getRequests(query = {}) {
        return await this.request('/requests', query);
    },

    async getRequest(requestId) {
        return await this.request('/request', { requestId });
    },

    async approveRequest(requestId) {
        return await this.request('/approve', { requestId });
    },

    async rejectRequest(requestId, reason) {
        return await this.request('/reject', { requestId, reason });
    },

    async getTasks(query = {}) {
        return await this.request('/tasks', query);
    },

    async getTask(taskId) {
        return await this.request('/task', { taskId });
    },

    async assignTask(taskId, nodeId) {
        return await this.request('/assign', { taskId, nodeId });
    },

    async completeTask(taskId) {
        return await this.request('/complete', { taskId });
    },

    async getStats() {
        return await this.request('/stats', {});
    },

    formatTime(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    },

    formatRequestStatus(status) {
        const statusMap = {
            'PENDING': { text: '待处理', class: 'status-pending' },
            'APPROVED': { text: '已批准', class: 'status-approved' },
            'REJECTED': { text: '已拒绝', class: 'status-rejected' },
            'COMPLETED': { text: '已完成', class: 'status-completed' }
        };
        return statusMap[status] || { text: status, class: '' };
    },

    formatTaskStatus(status) {
        const statusMap = {
            'PENDING': { text: '待执行', class: 'status-pending' },
            'RUNNING': { text: '执行中', class: 'status-running' },
            'COMPLETED': { text: '已完成', class: 'status-completed' },
            'FAILED': { text: '失败', class: 'status-failed' }
        };
        return statusMap[status] || { text: status, class: '' };
    },

    formatPriority(priority) {
        const priorityMap = {
            'HIGH': { text: '高', class: 'priority-high' },
            'MEDIUM': { text: '中', class: 'priority-medium' },
            'LOW': { text: '低', class: 'priority-low' }
        };
        return priorityMap[priority] || { text: priority, class: '' };
    }
};

window.CollaborationAPI = CollaborationAPI;
