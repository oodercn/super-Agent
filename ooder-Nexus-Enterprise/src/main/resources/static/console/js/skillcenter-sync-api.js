class SkillCenterSyncAPI {
    constructor() {
        this.apiClient = new ApiClient('/api/skillcenter/sync');
        this.cache = new Map();
    }

    async uploadSkill(file, metadata) {
        return await this.apiClient.uploadFile('/upload', file, metadata);
    }

    async downloadSkill(skillId) {
        try {
            const response = await fetch(`${this.apiClient.baseURL}/download/${skillId}`);
            
            if (!response.ok) {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    const errorData = await response.json();
                    throw new Error(errorData.message || '下载失败');
                } else {
                    const errorText = await response.text();
                    throw new Error(`下载失败: ${errorText.substring(0, 100)}...`);
                }
            }
            
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                const errorData = await response.json();
                throw new Error(errorData.message || '下载失败');
            }
            
            return await response.blob();
        } catch (error) {
            console.error('下载技能错误:', error);
            throw error;
        }
    }

    async getSkillList(category, page, size) {
        const params = {};
        if (category) params.category = category;
        if (page) params.page = page;
        if (size) params.size = size;
        
        return await this.apiClient.get('/list', params);
    }

    async updateSkillVersion(skillId, versionData) {
        return await this.apiClient.put(`/version/${skillId}`, versionData);
    }

    async deleteSkill(skillId) {
        return await this.apiClient.delete(`/${skillId}`);
    }

    async searchSkills(keyword, category) {
        const params = {};
        if (keyword) params.keyword = keyword;
        if (category) params.category = category;
        
        return await this.apiClient.get('/search', params);
    }

    async getCategories() {
        return await this.apiClient.get('/categories');
    }

    async createCategory(categoryData) {
        return await this.apiClient.post('/category', categoryData);
    }

    async updateCategory(categoryId, categoryData) {
        return await this.apiClient.put(`/category/${categoryId}`, categoryData);
    }

    async deleteCategory(categoryId) {
        return await this.apiClient.delete(`/category/${categoryId}`);
    }

    async batchSyncSkills(skillList) {
        return await this.apiClient.post('/batch', { skills: skillList });
    }

    async getSyncStatus() {
        return await this.apiClient.get('/status');
    }

    async cancelSync(batchId) {
        return await this.apiClient.post('/cancel', { batchId });
    }

    clearCache() {
        this.cache.clear();
    }

    clearCacheKey(key) {
        this.cache.delete(key);
    }
}
