class SecurityAPI {
    constructor() {
        this.baseURL = '/api/security';
        this.cache = new Map();
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            method: options.method || 'GET',
            headers: {
                ...options.headers
            },
            ...options
        };

        if (config.method !== 'GET' && options.body) {
            config.headers['Content-Type'] = 'application/json';
            config.body = JSON.stringify(options.body);
        }

        try {
            const response = await fetch(url, config);
            
            const contentType = response.headers.get('content-type');
            let data;
            
            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            } else {
                const text = await response.text();
                data = {
                    code: 500,
                    message: `非JSON响应: ${text.substring(0, 100)}...`,
                    data: null
                };
            }
            
            if (!response.ok) {
                throw new Error(data.message || '请求失败');
            }
            
            return data;
        } catch (error) {
            console.error('API 调用错误:', error);
            return {
                code: 500,
                message: error.message,
                data: null
            };
        }
    }

    async getSecurityStatus() {
        return await this.request('/status');
    }

    async runSecurityScan() {
        return await this.request('/scan', {
            method: 'POST'
        });
    }

    async handleAlert(alertId) {
        return await this.request(`/alert/${alertId}/handle`, {
            method: 'POST'
        });
    }

    async getSecurityHistory() {
        return await this.request('/history');
    }

    async getSecurityRecommendations() {
        return await this.request('/recommendations');
    }

    async updateSecuritySettings(settings) {
        return await this.request('/settings', {
            method: 'PUT',
            body: settings
        });
    }

    async getSecurityLogs() {
        return await this.request('/logs');
    }

    async exportSecurityReport() {
        return await this.request('/report/export');
    }

    async importSecuritySettings(file) {
        const formData = new FormData();
        formData.append('file', file);
        
        return await this.request('/settings/import', {
            method: 'POST',
            headers: {},
            body: formData
        });
    }
}

if (typeof window !== 'undefined') {
    window.SecurityAPI = SecurityAPI;
}
