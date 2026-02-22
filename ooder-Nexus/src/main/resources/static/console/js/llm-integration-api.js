class LLMIntegrationAPI {
    constructor() {
        this.baseURL = '/api/skillbridge';
        this.cache = new Map();
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            method: options.method || 'GET',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        if (config.method !== 'GET' && options.body) {
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

    async registerSkill(skillData) {
        return await this.request('/register', {
            method: 'POST',
            body: skillData
        });
    }

    async discoverSkills(category, keyword) {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        if (keyword) params.append('keyword', keyword);
        
        return await this.request(`/discover?${params.toString()}`);
    }

    async executeSkill(skillId, params) {
        return await this.request('/execute', {
            method: 'POST',
            body: {
                skillId: skillId,
                parameters: params
            }
        });
    }

    async getUserPreferences(userId) {
        return await this.request(`/preferences?userId=${userId}`);
    }

    async updateUserPreferences(preferences) {
        return await this.request('/preferences', {
            method: 'PUT',
            body: preferences
        });
    }

    async analyzeContext(contextData) {
        return await this.request('/context', {
            method: 'POST',
            body: contextData
        });
    }

    async getSkillDescription(skillId) {
        return await this.request(`/skill/${skillId}/description`);
    }

    async getRegisteredSkills(filter) {
        const params = new URLSearchParams();
        if (filter.category) params.append('category', filter.category);
        if (filter.keyword) params.append('keyword', filter.keyword);
        if (filter.status) params.append('status', filter.status);
        
        return await this.request(`/registered?${params.toString()}`);
    }

    async deleteSkill(skillId) {
        return await this.request(`/skill/${skillId}`, {
            method: 'DELETE'
        });
    }

    async updateSkill(skillId, skillData) {
        return await this.request(`/skill/${skillId}`, {
            method: 'PUT',
            body: skillData
        });
    }

    async testSkill(skillId, params) {
        return await this.request(`/test/${skillId}`, {
            method: 'POST',
            body: params
        });
    }

    async getExecutionHistory() {
        return await this.request('/execution/history');
    }

    async getExecutionResult(executionId) {
        return await this.request(`/execution/result/${executionId}`);
    }

    async getExecutionLog(executionId) {
        return await this.request(`/execution/log/${executionId}`);
    }

    clearCache() {
        this.cache.clear();
    }

    clearCacheKey(key) {
        this.cache.delete(key);
    }
}
