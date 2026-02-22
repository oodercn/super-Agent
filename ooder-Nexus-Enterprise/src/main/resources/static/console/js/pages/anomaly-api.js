const AnomalyAPI = {
    baseUrl: '/api/anomaly',

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

    async list(query = {}) {
        return await this.request('/list', query);
    },

    async get(anomalyId) {
        return await this.request('/get', { anomalyId });
    },

    async autoCorrect(anomalyId) {
        return await this.request('/autocorrect', { anomalyId });
    },

    async intervene(intervention) {
        return await this.request('/intervene', intervention);
    },

    async history(anomalyId) {
        return await this.request('/history', { anomalyId });
    },

    async stats() {
        return await this.request('/stats', {});
    },

    async acknowledge(anomalyId) {
        return await this.request('/acknowledge', { anomalyId });
    },

    async escalate(anomalyId, reason) {
        return await this.request('/escalate', { anomalyId, reason });
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

    formatSeverity(severity) {
        const severityMap = {
            'CRITICAL': { text: '严重', class: 'severity-critical' },
            'HIGH': { text: '高', class: 'severity-high' },
            'MEDIUM': { text: '中', class: 'severity-medium' },
            'LOW': { text: '低', class: 'severity-low' }
        };
        return severityMap[severity] || { text: severity, class: 'severity-low' };
    },

    formatStatus(status) {
        const statusMap = {
            'ACTIVE': { text: '活跃', class: 'status-active' },
            'RESOLVED': { text: '已解决', class: 'status-resolved' },
            'ACKNOWLEDGED': { text: '已确认', class: 'status-acknowledged' },
            'ESCALATED': { text: '已升级', class: 'status-escalated' }
        };
        return statusMap[status] || { text: status, class: '' };
    }
};

window.AnomalyAPI = AnomalyAPI;
