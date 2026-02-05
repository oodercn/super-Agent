/**
 * 个人功能 API 调用封装
 * 提供统一的 API 调用接口，处理个人中心的所有功能
 */

class PersonalAPI {
    constructor() {
        this.baseURL = '/api/personal';
        this.cache = new Map();
    }

    /**
     * 通用 API 调用方法
     */
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
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || '请求失败');
            }
            
            return data;
        } catch (error) {
            console.error('API 调用错误:', error);
            throw error;
        }
    }

    /**
     * 获取个人仪表盘统计数据
     */
    async getDashboardStats() {
        const cacheKey = 'dashboard-stats';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.request('/dashboard/stats');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 获取个人技能列表
     */
    async getPersonalSkills() {
        const cacheKey = 'personal-skills';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.request('/skills');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 发布个人技能
     */
    async publishSkill(skill) {
        const data = await this.request('/skills', {
            method: 'POST',
            body: skill
        });
        
        this.cache.delete('personal-skills');
        this.cache.delete('dashboard-stats');
        return data;
    }

    /**
     * 更新个人技能
     */
    async updateSkill(skillId, skill) {
        const data = await this.request(`/skills/${skillId}`, {
            method: 'PUT',
            body: skill
        });
        
        this.cache.delete('personal-skills');
        this.cache.delete('dashboard-stats');
        return data;
    }

    /**
     * 删除个人技能
     */
    async deleteSkill(skillId) {
        const data = await this.request(`/skills/${skillId}`, {
            method: 'DELETE'
        });
        
        this.cache.delete('personal-skills');
        this.cache.delete('dashboard-stats');
        return data;
    }

    /**
     * 执行个人技能
     */
    async executeSkill(skillId, parameters = {}) {
        const data = await this.request(`/execution/execute/${skillId}`, {
            method: 'POST',
            body: parameters
        });
        return data;
    }

    /**
     * 获取执行历史
     */
    async getExecutionHistory() {
        const data = await this.request('/execution/history');
        return data;
    }

    /**
     * 获取执行结果
     */
    async getExecutionResult(executionId) {
        const data = await this.request(`/execution/result/${executionId}`);
        return data;
    }

    /**
     * 获取分享的技能列表
     */
    async getSharedSkills() {
        const data = await this.request('/sharing/shared');
        return data;
    }

    /**
     * 获取收到的技能列表
     */
    async getReceivedSkills() {
        const data = await this.request('/sharing/received');
        return data;
    }

    /**
     * 分享技能
     */
    async shareSkill(skillId, target, targetType) {
        const data = await this.request('/sharing', {
            method: 'POST',
            body: {
                skillId,
                target,
                targetType
            }
        });
        return data;
    }

    /**
     * 获取我的群组列表
     */
    async getMyGroups() {
        const data = await this.request('/groups');
        return data;
    }

    /**
     * 获取群组技能列表
     */
    async getGroupSkills(groupId) {
        const data = await this.request(`/groups/${groupId}/skills`);
        return data;
    }

    /**
     * 获取个人身份信息
     */
    async getPersonalIdentity() {
        const cacheKey = 'personal-identity';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.request('/identity');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 更新个人身份信息
     */
    async updatePersonalIdentity(identity) {
        const data = await this.request('/identity', {
            method: 'PUT',
            body: identity
        });
        
        this.cache.delete('personal-identity');
        return data;
    }

    /**
     * 获取帮助文档
     */
    async getHelp() {
        const cacheKey = 'help-docs';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.request('/help');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 获取系统信息
     */
    async getSystemInfo() {
        const cacheKey = 'system-info';
        if (this.cache.has(cacheKey)) {
            return this.cache.get(cacheKey);
        }

        const data = await this.request('/system/info');
        this.cache.set(cacheKey, data);
        return data;
    }

    /**
     * 清除缓存
     */
    clearCache() {
        this.cache.clear();
    }

    /**
     * 清除特定缓存
     */
    clearCacheKey(key) {
        this.cache.delete(key);
    }
}

// 导出 PersonalAPI 类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = PersonalAPI;
} else {
    window.PersonalAPI = PersonalAPI;
}

// 创建全局实例
window.personalAPI = new PersonalAPI();
