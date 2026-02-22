const ObservationAPI = {
    baseUrl: '/api/observation',

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

    async getTopology() {
        return await this.request('/topology', {});
    },

    async getNode(nodeId) {
        return await this.request('/node', { nodeId });
    },

    async getRoutes(query = {}) {
        return await this.request('/routes', query);
    },

    async getRoute(routeId) {
        return await this.request('/route', { routeId });
    },

    async getLogs(query = {}) {
        return await this.request('/logs', query);
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

    formatStatus(status) {
        const statusMap = {
            'ONLINE': { text: '在线', class: 'status-online' },
            'OFFLINE': { text: '离线', class: 'status-offline' },
            'WARNING': { text: '警告', class: 'status-warning' },
            'ACTIVE': { text: '活跃', class: 'status-active' },
            'INACTIVE': { text: '非活跃', class: 'status-inactive' }
        };
        return statusMap[status] || { text: status, class: '' };
    },

    formatLogLevel(level) {
        const levelMap = {
            'INFO': { text: '信息', class: 'log-info' },
            'WARN': { text: '警告', class: 'log-warn' },
            'ERROR': { text: '错误', class: 'log-error' },
            'DEBUG': { text: '调试', class: 'log-debug' }
        };
        return levelMap[level] || { text: level, class: '' };
    }
};

window.ObservationAPI = ObservationAPI;
