/**
 * API 调用模块
 * 使用全局 ApiClient 对象进行统一的 API 调用
 */

/**
 * 技能管理 API
 */
export const skillApi = {
    async getSkills(params = {}) {
        try {
            ApiClient.showLoading('获取技能列表...');
            const result = await ApiClient.post('/api/skills/list', {
                category: params.category,
                status: params.status,
                keyword: params.keyword,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取技能列表成功');
            return result;
        } catch (error) {
            console.error('Failed to get skills:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取技能列表失败: ' + error.message);
            throw error;
        }
    },

    async getSkillDetail(skillId) {
        try {
            ApiClient.showLoading('获取技能详情...');
            const result = await ApiClient.post('/api/skills/get', { skillId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取技能详情成功');
            return result;
        } catch (error) {
            console.error('Failed to get skill detail:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取技能详情失败: ' + error.message);
            throw error;
        }
    },

    async createSkill(skillData) {
        try {
            ApiClient.showLoading('创建技能...');
            const result = await ApiClient.post('/api/skills/add', skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能创建成功');
            return result;
        } catch (error) {
            console.error('Failed to create skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能创建失败: ' + error.message);
            throw error;
        }
    },

    async updateSkill(skillData) {
        try {
            ApiClient.showLoading('更新技能...');
            const result = await ApiClient.post('/api/skills/update', skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能更新成功');
            return result;
        } catch (error) {
            console.error('Failed to update skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能更新失败: ' + error.message);
            throw error;
        }
    },

    async deleteSkill(skillId) {
        try {
            ApiClient.showLoading('删除技能...');
            const result = await ApiClient.post('/api/skills/delete', { skillId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能删除成功');
            return result;
        } catch (error) {
            console.error('Failed to delete skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能删除失败: ' + error.message);
            throw error;
        }
    },

    async approveSkill(skillId) {
        try {
            ApiClient.showLoading('审核技能...');
            const result = await ApiClient.post(`/api/skills/${skillId}/approve`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能审核成功');
            return result;
        } catch (error) {
            console.error('Failed to approve skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能审核失败: ' + error.message);
            throw error;
        }
    },

    async rejectSkill(skillId) {
        try {
            ApiClient.showLoading('拒绝技能...');
            const result = await ApiClient.post(`/api/skills/${skillId}/reject`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能拒绝成功');
            return result;
        } catch (error) {
            console.error('Failed to reject skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能拒绝失败: ' + error.message);
            throw error;
        }
    },

    async executeSkill(skillId, params = {}) {
        try {
            ApiClient.showLoading('执行技能...');
            const result = await ApiClient.post(`/api/skills/${skillId}/execute`, {
                skillId: skillId,
                parameters: params
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能执行成功');
            return result;
        } catch (error) {
            console.error('Failed to execute skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能执行失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 市场管理 API
 */
export const marketApi = {
    async getMarketSkills(params = {}) {
        try {
            ApiClient.showLoading('获取市场技能列表...');
            const result = await ApiClient.post('/api/market/skills', {
                category: params.category,
                keyword: params.keyword,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取市场技能列表成功');
            return result;
        } catch (error) {
            console.error('Failed to get market skills:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取市场技能列表失败: ' + error.message);
            throw error;
        }
    },

    async getSkillDetail(skillId) {
        try {
            ApiClient.showLoading('获取技能详情...');
            const result = await ApiClient.post(`/api/market/skills/${skillId}`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取技能详情成功');
            return result;
        } catch (error) {
            console.error('Failed to get skill detail:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取技能详情失败: ' + error.message);
            throw error;
        }
    },

    async addMarketSkill(skillData) {
        try {
            ApiClient.showLoading('添加市场技能...');
            const result = await ApiClient.post('/api/market/skills/add', skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('添加市场技能成功');
            return result;
        } catch (error) {
            console.error('Failed to add market skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('添加市场技能失败: ' + error.message);
            throw error;
        }
    },

    async updateMarketSkill(skillId, skillData) {
        try {
            ApiClient.showLoading('更新市场技能...');
            const result = await ApiClient.post(`/api/market/skills/${skillId}/update`, skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新市场技能成功');
            return result;
        } catch (error) {
            console.error('Failed to update market skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新市场技能失败: ' + error.message);
            throw error;
        }
    },

    async deleteMarketSkill(skillId) {
        try {
            ApiClient.showLoading('删除市场技能...');
            const result = await ApiClient.post(`/api/market/skills/${skillId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除市场技能成功');
            return result;
        } catch (error) {
            console.error('Failed to delete market skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除市场技能失败: ' + error.message);
            throw error;
        }
    },

    async getSkillsByCategory(category) {
        try {
            ApiClient.showLoading('获取分类技能...');
            const result = await ApiClient.post(`/api/market/skills/category/${category}`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取分类技能成功');
            return result;
        } catch (error) {
            console.error('Failed to get skills by category:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取分类技能失败: ' + error.message);
            throw error;
        }
    },

    async getPopularSkills(pageSize = 10) {
        try {
            ApiClient.showLoading('获取热门技能...');
            const result = await ApiClient.post('/api/market/skills/popular', { pageNum: 1, pageSize });
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取热门技能成功');
            return result;
        } catch (error) {
            console.error('Failed to get popular skills:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取热门技能失败: ' + error.message);
            throw error;
        }
    },

    async getLatestSkills(pageSize = 10) {
        try {
            ApiClient.showLoading('获取最新技能...');
            const result = await ApiClient.post('/api/market/skills/latest', { pageNum: 1, pageSize });
            ApiClient.hideLoading();
            ApiClient.showSuccess('获取最新技能成功');
            return result;
        } catch (error) {
            console.error('Failed to get latest skills:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取最新技能失败: ' + error.message);
            throw error;
        }
    },

    async downloadSkill(skillId) {
        try {
            ApiClient.showLoading('下载技能...');
            const result = await ApiClient.post('/api/market/skills/download', { id: skillId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能下载成功');
            return result;
        } catch (error) {
            console.error('Failed to download skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能下载失败: ' + error.message);
            throw error;
        }
    },

    async rateSkill(skillId, rating, comment, userId) {
        try {
            ApiClient.showLoading('评分技能...');
            const result = await ApiClient.post('/api/market/skills/rate', { id: skillId, rating, comment, userId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能评分成功');
            return result;
        } catch (error) {
            console.error('Failed to rate skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能评分失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * P2P网络管理 API
 */
export const p2pApi = {
    async getStatus() {
        try {
            ApiClient.showLoading('获取P2P状态...');
            const result = await ApiClient.post('/api/p2p/status', {});
            ApiClient.hideLoading();
            return result;
        } catch (error) {
            console.error('Failed to get P2P status:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取P2P状态失败: ' + error.message);
            throw error;
        }
    },

    async getNodes() {
        try {
            ApiClient.showLoading('获取节点列表...');
            const result = await ApiClient.post('/api/p2p/nodes', {});
            ApiClient.hideLoading();
            return result;
        } catch (error) {
            console.error('Failed to get nodes:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取节点列表失败: ' + error.message);
            throw error;
        }
    },

    async getNodeDetail(nodeId) {
        try {
            ApiClient.showLoading('获取节点详情...');
            const result = await ApiClient.post(`/api/p2p/nodes/${nodeId}`, {});
            ApiClient.hideLoading();
            return result;
        } catch (error) {
            console.error('Failed to get node detail:', error);
            ApiClient.hideLoading();
            ApiClient.showError('获取节点详情失败: ' + error.message);
            throw error;
        }
    },

    async startNetwork() {
        try {
            ApiClient.showLoading('启动P2P网络...');
            const result = await ApiClient.post('/api/p2p/start', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('P2P网络启动成功');
            return result;
        } catch (error) {
            console.error('Failed to start P2P network:', error);
            ApiClient.hideLoading();
            ApiClient.showError('启动P2P网络失败: ' + error.message);
            throw error;
        }
    },

    async stopNetwork() {
        try {
            ApiClient.showLoading('停止P2P网络...');
            const result = await ApiClient.post('/api/p2p/stop', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('P2P网络停止成功');
            return result;
        } catch (error) {
            console.error('Failed to stop P2P network:', error);
            ApiClient.hideLoading();
            ApiClient.showError('停止P2P网络失败: ' + error.message);
            throw error;
        }
    },

    async discoverNodes(params = {}) {
        try {
            ApiClient.showLoading('发现节点...');
            const result = await ApiClient.post('/api/p2p/discover', params);
            ApiClient.hideLoading();
            ApiClient.showSuccess('节点发现成功');
            return result;
        } catch (error) {
            console.error('Failed to discover nodes:', error);
            ApiClient.hideLoading();
            ApiClient.showError('节点发现失败: ' + error.message);
            throw error;
        }
    },

    async connectToNode(address, port) {
        try {
            ApiClient.showLoading('连接节点...');
            const result = await ApiClient.post('/api/p2p/connect', { address, port });
            ApiClient.hideLoading();
            ApiClient.showSuccess('连接节点成功');
            return result;
        } catch (error) {
            console.error('Failed to connect to node:', error);
            ApiClient.hideLoading();
            ApiClient.showError('连接节点失败: ' + error.message);
            throw error;
        }
    },

    async disconnectFromNode(nodeId) {
        try {
            ApiClient.showLoading('断开节点...');
            const result = await ApiClient.post(`/api/p2p/disconnect/${nodeId}`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('断开节点成功');
            return result;
        } catch (error) {
            console.error('Failed to disconnect from node:', error);
            ApiClient.hideLoading();
            ApiClient.showError('断开节点失败: ' + error.message);
            throw error;
        }
    },

    async getStats() {
        try {
            const result = await ApiClient.post('/api/p2p/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get P2P stats:', error);
            throw error;
        }
    },

    async refreshNetwork() {
        try {
            ApiClient.showLoading('刷新网络...');
            const result = await ApiClient.post('/api/p2p/refresh', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('网络刷新成功');
            return result;
        } catch (error) {
            console.error('Failed to refresh network:', error);
            ApiClient.hideLoading();
            ApiClient.showError('网络刷新失败: ' + error.message);
            throw error;
        }
    },

    async getEvents(params = {}) {
        try {
            const result = await ApiClient.post('/api/p2p/events', params);
            return result;
        } catch (error) {
            console.error('Failed to get P2P events:', error);
            throw error;
        }
    }
};

/**
 * 个人中心 API
 */
export const personalApi = {
    async getDashboardStats() {
        try {
            const result = await ApiClient.post('/api/personal/dashboard/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get dashboard stats:', error);
            throw error;
        }
    },

    async getMySkills(params = {}) {
        try {
            const result = await ApiClient.post('/api/personal/skills/list', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get my skills:', error);
            throw error;
        }
    },

    async createSkill(skillData) {
        try {
            ApiClient.showLoading('创建技能...');
            const result = await ApiClient.post('/api/personal/skills/add', skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('创建技能成功');
            return result;
        } catch (error) {
            console.error('Failed to create skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('创建技能失败: ' + error.message);
            throw error;
        }
    },

    async getMyGroups(params = {}) {
        try {
            const result = await ApiClient.post('/api/personal/groups/list', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get my groups:', error);
            throw error;
        }
    },

    async getGroupDetail(groupId) {
        try {
            const result = await ApiClient.post(`/api/personal/groups/${groupId}/get`, {});
            return result;
        } catch (error) {
            console.error('Failed to get group detail:', error);
            throw error;
        }
    },

    async createGroup(groupData) {
        try {
            ApiClient.showLoading('创建群组...');
            const result = await ApiClient.post('/api/personal/groups/add', groupData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('创建群组成功');
            return result;
        } catch (error) {
            console.error('Failed to create group:', error);
            ApiClient.hideLoading();
            ApiClient.showError('创建群组失败: ' + error.message);
            throw error;
        }
    },

    async updateGroup(groupId, groupData) {
        try {
            ApiClient.showLoading('更新群组...');
            const result = await ApiClient.post(`/api/personal/groups/${groupId}/update`, groupData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新群组成功');
            return result;
        } catch (error) {
            console.error('Failed to update group:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新群组失败: ' + error.message);
            throw error;
        }
    },

    async deleteGroup(groupId) {
        try {
            ApiClient.showLoading('删除群组...');
            const result = await ApiClient.post(`/api/personal/groups/${groupId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除群组成功');
            return result;
        } catch (error) {
            console.error('Failed to delete group:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除群组失败: ' + error.message);
            throw error;
        }
    },

    async getGroupSkills(params = {}) {
        try {
            const result = await ApiClient.post('/api/personal/groups/skills/list', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get group skills:', error);
            throw error;
        }
    },

    async addGroupSkill(skillData) {
        try {
            ApiClient.showLoading('添加群组技能...');
            const result = await ApiClient.post('/api/personal/groups/skills/add', skillData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('添加群组技能成功');
            return result;
        } catch (error) {
            console.error('Failed to add group skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('添加群组技能失败: ' + error.message);
            throw error;
        }
    },

    async getIdentity() {
        try {
            const result = await ApiClient.post('/api/personal/identity/get', {});
            return result;
        } catch (error) {
            console.error('Failed to get identity:', error);
            throw error;
        }
    },

    async updateIdentity(identityData) {
        try {
            ApiClient.showLoading('更新身份...');
            const result = await ApiClient.post('/api/personal/identity/update', identityData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新身份成功');
            return result;
        } catch (error) {
            console.error('Failed to update identity:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新身份失败: ' + error.message);
            throw error;
        }
    },

    async getIdentityMappings() {
        try {
            const result = await ApiClient.post('/api/personal/identity/mappings/list', {});
            return result;
        } catch (error) {
            console.error('Failed to get identity mappings:', error);
            throw error;
        }
    },

    async getHelp() {
        try {
            const result = await ApiClient.post('/api/personal/help/get', {});
            return result;
        } catch (error) {
            console.error('Failed to get help:', error);
            throw error;
        }
    },

    async getSettings() {
        try {
            const result = await ApiClient.post('/api/personal/settings/get', {});
            return result;
        } catch (error) {
            console.error('Failed to get settings:', error);
            throw error;
        }
    },

    async updateSettings(settingsData) {
        try {
            ApiClient.showLoading('更新设置...');
            const result = await ApiClient.post('/api/personal/settings/update', settingsData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新设置成功');
            return result;
        } catch (error) {
            console.error('Failed to update settings:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新设置失败: ' + error.message);
            throw error;
        }
    },

    async getFeatures() {
        try {
            const result = await ApiClient.post('/api/personal/features/get', {});
            return result;
        } catch (error) {
            console.error('Failed to get features:', error);
            throw error;
        }
    }
};

/**
 * 分享管理 API
 */
export const shareApi = {
    async shareSkill(skillId, groupId, message) {
        try {
            ApiClient.showLoading('分享技能...');
            const result = await ApiClient.post('/api/share', { skillId, groupId, message });
            ApiClient.hideLoading();
            ApiClient.showSuccess('分享技能成功');
            return result;
        } catch (error) {
            console.error('Failed to share skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('分享技能失败: ' + error.message);
            throw error;
        }
    },

    async getSharedSkills() {
        try {
            const result = await ApiClient.post('/api/share/shared', {});
            return result;
        } catch (error) {
            console.error('Failed to get shared skills:', error);
            throw error;
        }
    },

    async getReceivedSkills() {
        try {
            const result = await ApiClient.post('/api/share/received', {});
            return result;
        } catch (error) {
            console.error('Failed to get received skills:', error);
            throw error;
        }
    },

    async unshareSkill(shareId) {
        try {
            ApiClient.showLoading('取消分享...');
            const result = await ApiClient.post(`/api/share/${shareId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('取消分享成功');
            return result;
        } catch (error) {
            console.error('Failed to unshare skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('取消分享失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 存储管理 API
 */
export const storageApi = {
    async getStatus() {
        try {
            const result = await ApiClient.post('/api/storage/status', {});
            return result;
        } catch (error) {
            console.error('Failed to get storage status:', error);
            throw error;
        }
    },

    async getStats() {
        try {
            const result = await ApiClient.post('/api/storage/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get storage stats:', error);
            throw error;
        }
    },

    async backup() {
        try {
            ApiClient.showLoading('备份存储...');
            const result = await ApiClient.post('/api/storage/backup', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('备份成功');
            return result;
        } catch (error) {
            console.error('Failed to backup:', error);
            ApiClient.hideLoading();
            ApiClient.showError('备份失败: ' + error.message);
            throw error;
        }
    },

    async getBackups(params = {}) {
        try {
            const result = await ApiClient.post('/api/storage/backups', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get backups:', error);
            throw error;
        }
    },

    async restore(backupName) {
        try {
            ApiClient.showLoading('恢复存储...');
            const result = await ApiClient.post(`/api/storage/restore/${backupName}`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('恢复成功');
            return result;
        } catch (error) {
            console.error('Failed to restore:', error);
            ApiClient.hideLoading();
            ApiClient.showError('恢复失败: ' + error.message);
            throw error;
        }
    },

    async clean() {
        try {
            ApiClient.showLoading('清理存储...');
            const result = await ApiClient.post('/api/storage/clean', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('清理成功');
            return result;
        } catch (error) {
            console.error('Failed to clean:', error);
            ApiClient.hideLoading();
            ApiClient.showError('清理失败: ' + error.message);
            throw error;
        }
    },

    async deleteBackup(backupName) {
        try {
            ApiClient.showLoading('删除备份...');
            const result = await ApiClient.post(`/api/storage/backups/${backupName}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除备份成功');
            return result;
        } catch (error) {
            console.error('Failed to delete backup:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除备份失败: ' + error.message);
            throw error;
        }
    },

    async cleanBackups() {
        try {
            ApiClient.showLoading('清理备份...');
            const result = await ApiClient.post('/api/storage/clean/backups', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('清理备份成功');
            return result;
        } catch (error) {
            console.error('Failed to clean backups:', error);
            ApiClient.hideLoading();
            ApiClient.showError('清理备份失败: ' + error.message);
            throw error;
        }
    },

    async getSettings() {
        try {
            const result = await ApiClient.post('/api/storage/settings', {});
            return result;
        } catch (error) {
            console.error('Failed to get storage settings:', error);
            throw error;
        }
    },

    async updateSettings(settingsData) {
        try {
            ApiClient.showLoading('更新存储设置...');
            const result = await ApiClient.post('/api/storage/settings/update', settingsData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新存储设置成功');
            return result;
        } catch (error) {
            console.error('Failed to update storage settings:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新存储设置失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 管理员 API
 */
export const adminApi = {
    async getDashboardStats() {
        try {
            const result = await ApiClient.post('/api/admin/dashboard/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get admin dashboard stats:', error);
            throw error;
        }
    },

    async getSkills(params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/skills', {
                category: params.category,
                status: params.status,
                keyword: params.keyword,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get admin skills:', error);
            throw error;
        }
    },

    async deleteSkill(skillId) {
        try {
            ApiClient.showLoading('删除技能...');
            const result = await ApiClient.post(`/api/admin/skills/${skillId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除技能成功');
            return result;
        } catch (error) {
            console.error('Failed to delete skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除技能失败: ' + error.message);
            throw error;
        }
    },

    async getUsers(params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/users', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get users:', error);
            throw error;
        }
    },

    async searchUsers(keyword, params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/users/search', {
                keyword: keyword,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to search users:', error);
            throw error;
        }
    },

    async deleteUser(userId) {
        try {
            ApiClient.showLoading('删除用户...');
            const result = await ApiClient.post(`/api/admin/users/${userId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除用户成功');
            return result;
        } catch (error) {
            console.error('Failed to delete user:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除用户失败: ' + error.message);
            throw error;
        }
    },

    async getGroups(params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/groups', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get groups:', error);
            throw error;
        }
    },

    async searchGroups(keyword, params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/groups/search', {
                keyword: keyword,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to search groups:', error);
            throw error;
        }
    },

    async deleteGroup(groupId) {
        try {
            ApiClient.showLoading('删除群组...');
            const result = await ApiClient.post(`/api/admin/groups/${groupId}/delete`, {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除群组成功');
            return result;
        } catch (error) {
            console.error('Failed to delete group:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除群组失败: ' + error.message);
            throw error;
        }
    },

    async getMarketSkills(params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/market/skills', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get market skills:', error);
            throw error;
        }
    },

    async getPopularMarketSkills(pageSize = 10) {
        try {
            const result = await ApiClient.post('/api/admin/market/skills/popular', { pageNum: 1, pageSize });
            return result;
        } catch (error) {
            console.error('Failed to get popular market skills:', error);
            throw error;
        }
    },

    async getLatestMarketSkills(pageSize = 10) {
        try {
            const result = await ApiClient.post('/api/admin/market/skills/latest', { pageNum: 1, pageSize });
            return result;
        } catch (error) {
            console.error('Failed to get latest market skills:', error);
            throw error;
        }
    },

    async getAuthenticationRequests(params = {}) {
        try {
            const result = await ApiClient.post('/api/admin/authentication/requests', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get authentication requests:', error);
            throw error;
        }
    },

    async updateAuthenticationRequestStatus(id, status, comments) {
        try {
            ApiClient.showLoading('更新认证请求状态...');
            const result = await ApiClient.post(`/api/admin/authentication/requests/${id}/status`, { 
                status, 
                comments 
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新认证请求状态成功');
            return result;
        } catch (error) {
            console.error('Failed to update authentication request status:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新认证请求状态失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 仪表盘 API
 */
export const dashboardApi = {
    async getDashboardStats() {
        try {
            const result = await ApiClient.post('/api/dashboard', {});
            return result;
        } catch (error) {
            console.error('Failed to get dashboard stats:', error);
            throw error;
        }
    },

    async getExecutionStats() {
        try {
            const result = await ApiClient.post('/api/dashboard/execution-stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get execution stats:', error);
            throw error;
        }
    },

    async getMarketStats() {
        try {
            const result = await ApiClient.post('/api/dashboard/market-stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get market stats:', error);
            throw error;
        }
    },

    async getSystemStats() {
        try {
            const result = await ApiClient.post('/api/dashboard/system-stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get system stats:', error);
            throw error;
        }
    }
};

/**
 * 系统管理 API
 */
export const systemApi = {
    async getStatus() {
        try {
            const result = await ApiClient.post('/api/system/status', {});
            return result;
        } catch (error) {
            console.error('Failed to get system status:', error);
            throw error;
        }
    },

    async getConfig() {
        try {
            const result = await ApiClient.post('/api/system/config', {});
            return result;
        } catch (error) {
            console.error('Failed to get system config:', error);
            throw error;
        }
    },

    async getVersion() {
        try {
            const result = await ApiClient.post('/api/system/version', {});
            return result;
        } catch (error) {
            console.error('Failed to get system version:', error);
            throw error;
        }
    },

    async getResources() {
        try {
            const result = await ApiClient.post('/api/system/resources', {});
            return result;
        } catch (error) {
            console.error('Failed to get system resources:', error);
            throw error;
        }
    },

    async updateConfig(configData) {
        try {
            ApiClient.showLoading('更新配置...');
            const result = await ApiClient.post('/api/system/config/update', configData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新配置成功');
            return result;
        } catch (error) {
            console.error('Failed to update config:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新配置失败: ' + error.message);
            throw error;
        }
    },

    async restart() {
        try {
            ApiClient.showLoading('重启系统...');
            const result = await ApiClient.post('/api/system/restart', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('系统重启命令已发出');
            return result;
        } catch (error) {
            console.error('Failed to restart system:', error);
            ApiClient.hideLoading();
            ApiClient.showError('重启系统失败: ' + error.message);
            throw error;
        }
    },

    async shutdown() {
        try {
            ApiClient.showLoading('关闭系统...');
            const result = await ApiClient.post('/api/system/shutdown', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('系统关闭命令已发出');
            return result;
        } catch (error) {
            console.error('Failed to shutdown system:', error);
            ApiClient.hideLoading();
            ApiClient.showError('关闭系统失败: ' + error.message);
            throw error;
        }
    },

    async getHealth() {
        try {
            const result = await ApiClient.post('/api/system/health', {});
            return result;
        } catch (error) {
            console.error('Failed to get system health:', error);
            throw error;
        }
    },

    async clearCache() {
        try {
            ApiClient.showLoading('清理缓存...');
            const result = await ApiClient.post('/api/system/cache/clear', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('清理缓存成功');
            return result;
        } catch (error) {
            console.error('Failed to clear cache:', error);
            ApiClient.hideLoading();
            ApiClient.showError('清理缓存失败: ' + error.message);
            throw error;
        }
    },

    async getOperations() {
        try {
            const result = await ApiClient.post('/api/system/operations', {});
            return result;
        } catch (error) {
            console.error('Failed to get system operations:', error);
            throw error;
        }
    },

    async getLogs(level = '') {
        try {
            const result = await ApiClient.post('/api/system/logs', { level });
            return result;
        } catch (error) {
            console.error('Failed to get system logs:', error);
            throw error;
        }
    },

    async clearLogs() {
        try {
            ApiClient.showLoading('清空日志...');
            const result = await ApiClient.post('/api/system/logs/clear', {});
            ApiClient.hideLoading();
            ApiClient.showSuccess('清空日志成功');
            return result;
        } catch (error) {
            console.error('Failed to clear logs:', error);
            ApiClient.hideLoading();
            ApiClient.showError('清空日志失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 监控管理 API
 */
export const monitorApi = {
    async getMonitorList() {
        try {
            const result = await ApiClient.post('/api/monitor/list', {});
            return result;
        } catch (error) {
            console.error('Failed to get monitor list:', error);
            throw error;
        }
    },

    async getRealtimeMonitor(skillId) {
        try {
            const result = await ApiClient.post(`/api/monitor/realtime/${skillId}`, {});
            return result;
        } catch (error) {
            console.error('Failed to get realtime monitor:', error);
            throw error;
        }
    },

    async getMetricsHistory(skillId, params = {}) {
        try {
            const result = await ApiClient.post(`/api/monitor/metrics/${skillId}`, {
                startTime: params.startTime,
                endTime: params.endTime,
                resolution: params.resolution || '1m'
            });
            return result;
        } catch (error) {
            console.error('Failed to get metrics history:', error);
            throw error;
        }
    },

    async getAlerts(params = {}) {
        try {
            const result = await ApiClient.post('/api/monitor/alerts', {
                skillId: params.skillId,
                severity: params.severity,
                status: params.status,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get alerts:', error);
            throw error;
        }
    },

    async getLogs(skillId, params = {}) {
        try {
            const result = await ApiClient.post(`/api/monitor/logs/${skillId}`, {
                level: params.level,
                keyword: params.keyword,
                startTime: params.startTime,
                endTime: params.endTime,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 20
            });
            return result;
        } catch (error) {
            console.error('Failed to get logs:', error);
            throw error;
        }
    },

    async getMonitorStats() {
        try {
            const result = await ApiClient.post('/api/monitor/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get monitor stats:', error);
            throw error;
        }
    }
};

/**
 * 编排管理 API
 */
export const orchestrationApi = {
    async getTemplates(params = {}) {
        try {
            const result = await ApiClient.post('/api/orchestration/templates', {
                category: params.category,
                status: params.status,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get templates:', error);
            throw error;
        }
    },

    async getTemplate(templateId) {
        try {
            const result = await ApiClient.post('/api/orchestration/templates/get', { templateId });
            return result;
        } catch (error) {
            console.error('Failed to get template:', error);
            throw error;
        }
    },

    async createTemplate(templateData) {
        try {
            ApiClient.showLoading('创建模板...');
            const result = await ApiClient.post('/api/orchestration/templates/create', templateData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('创建模板成功');
            return result;
        } catch (error) {
            console.error('Failed to create template:', error);
            ApiClient.hideLoading();
            ApiClient.showError('创建模板失败: ' + error.message);
            throw error;
        }
    },

    async updateTemplate(templateId, templateData) {
        try {
            ApiClient.showLoading('更新模板...');
            const result = await ApiClient.post('/api/orchestration/templates/update', {
                templateId,
                ...templateData
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('更新模板成功');
            return result;
        } catch (error) {
            console.error('Failed to update template:', error);
            ApiClient.hideLoading();
            ApiClient.showError('更新模板失败: ' + error.message);
            throw error;
        }
    },

    async deleteTemplate(templateId) {
        try {
            ApiClient.showLoading('删除模板...');
            const result = await ApiClient.post('/api/orchestration/templates/delete', { templateId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除模板成功');
            return result;
        } catch (error) {
            console.error('Failed to delete template:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除模板失败: ' + error.message);
            throw error;
        }
    },

    async executeTemplate(templateId, params = {}) {
        try {
            ApiClient.showLoading('执行编排...');
            const result = await ApiClient.post('/api/orchestration/execute', {
                templateId,
                parameters: params
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('执行编排成功');
            return result;
        } catch (error) {
            console.error('Failed to execute template:', error);
            ApiClient.hideLoading();
            ApiClient.showError('执行编排失败: ' + error.message);
            throw error;
        }
    },

    async getExecutionStatus(executionId) {
        try {
            const result = await ApiClient.post(`/api/orchestration/status/${executionId}`, {});
            return result;
        } catch (error) {
            console.error('Failed to get execution status:', error);
            throw error;
        }
    },

    async getExecutions(params = {}) {
        try {
            const result = await ApiClient.post('/api/orchestration/executions', {
                templateId: params.templateId,
                status: params.status,
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get executions:', error);
            throw error;
        }
    },

    async getSchedules(params = {}) {
        try {
            const result = await ApiClient.post('/api/orchestration/schedules', {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 10
            });
            return result;
        } catch (error) {
            console.error('Failed to get schedules:', error);
            throw error;
        }
    },

    async createSchedule(scheduleData) {
        try {
            ApiClient.showLoading('创建计划...');
            const result = await ApiClient.post('/api/orchestration/schedules/create', scheduleData);
            ApiClient.hideLoading();
            ApiClient.showSuccess('创建计划成功');
            return result;
        } catch (error) {
            console.error('Failed to create schedule:', error);
            ApiClient.hideLoading();
            ApiClient.showError('创建计划失败: ' + error.message);
            throw error;
        }
    },

    async toggleSchedule(scheduleId) {
        try {
            const result = await ApiClient.post('/api/orchestration/schedules/toggle', { scheduleId });
            return result;
        } catch (error) {
            console.error('Failed to toggle schedule:', error);
            throw error;
        }
    },

    async deleteSchedule(scheduleId) {
        try {
            ApiClient.showLoading('删除计划...');
            const result = await ApiClient.post('/api/orchestration/schedules/delete', { scheduleId });
            ApiClient.hideLoading();
            ApiClient.showSuccess('删除计划成功');
            return result;
        } catch (error) {
            console.error('Failed to delete schedule:', error);
            ApiClient.hideLoading();
            ApiClient.showError('删除计划失败: ' + error.message);
            throw error;
        }
    }
};

/**
 * 执行管理 API
 */
export const executionApi = {
    async getStats() {
        try {
            const result = await ApiClient.post('/api/execution/stats', {});
            return result;
        } catch (error) {
            console.error('Failed to get execution stats:', error);
            throw error;
        }
    },

    async execute(skillId, params = {}) {
        try {
            ApiClient.showLoading('执行技能...');
            const result = await ApiClient.post(`/api/execution/execute/${skillId}`, {
                parameters: params
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('技能执行成功');
            return result;
        } catch (error) {
            console.error('Failed to execute skill:', error);
            ApiClient.hideLoading();
            ApiClient.showError('技能执行失败: ' + error.message);
            throw error;
        }
    },

    async executeAsync(skillId, params = {}) {
        try {
            ApiClient.showLoading('提交执行任务...');
            const result = await ApiClient.post(`/api/execution/execute-async/${skillId}`, {
                parameters: params
            });
            ApiClient.hideLoading();
            ApiClient.showSuccess('执行任务已提交');
            return result;
        } catch (error) {
            console.error('Failed to execute skill async:', error);
            ApiClient.hideLoading();
            ApiClient.showError('提交执行任务失败: ' + error.message);
            throw error;
        }
    },

    async getResult(executionId) {
        try {
            const result = await ApiClient.post(`/api/execution/result/${executionId}`, {});
            return result;
        } catch (error) {
            console.error('Failed to get execution result:', error);
            throw error;
        }
    },

    async getStatus(executionId) {
        try {
            const result = await ApiClient.post(`/api/execution/status/${executionId}`, {});
            return result;
        } catch (error) {
            console.error('Failed to get execution status:', error);
            throw error;
        }
    },

    async clearResult(executionId) {
        try {
            const result = await ApiClient.post(`/api/execution/clear/${executionId}`, {});
            return result;
        } catch (error) {
            console.error('Failed to clear execution result:', error);
            throw error;
        }
    },

    async getHistory() {
        try {
            const result = await ApiClient.post('/execution/history', {});
            return result;
        } catch (error) {
            console.error('Failed to get execution history:', error);
            throw error;
        }
    }
};
