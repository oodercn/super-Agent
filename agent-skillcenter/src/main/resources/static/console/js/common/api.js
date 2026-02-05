/**
 * API 调用模块
 */

import { fetchJson, showNotification } from './utils.js';

/**
 * API 基础配置
 */
const API_CONFIG = {
    baseUrl: '/skillcenter',
    timeout: 30000
};

/**
 * 通用 API 响应处理
 * @param {Response} response - 响应对象
 * @returns {Promise<any>} 处理后的响应数据
 */
async function handleResponse(response) {
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `API error: ${response.status}`);
    }
    return response.json();
}

/**
 * 技能管理 API
 */
export const skillApi = {
    /**
     * 获取技能列表
     * @param {Object} params - 查询参数
     * @returns {Promise<any>} 技能列表
     */
    async getSkills(params = {}) {
        try {
            const queryString = new URLSearchParams(params).toString();
            const url = `${API_CONFIG.baseUrl}/api/skills${queryString ? `?${queryString}` : ''}`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get skills:', error);
            showNotification('获取技能列表失败', 'error');
            throw error;
        }
    },

    /**
     * 获取技能详情
     * @param {string} skillId - 技能ID
     * @returns {Promise<any>} 技能详情
     */
    async getSkillDetail(skillId) {
        try {
            const url = `${API_CONFIG.baseUrl}/api/skills/${skillId}`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get skill detail:', error);
            showNotification('获取技能详情失败', 'error');
            throw error;
        }
    },

    /**
     * 创建技能
     * @param {Object} skillData - 技能数据
     * @returns {Promise<any>} 创建结果
     */
    async createSkill(skillData) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/skills`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(skillData)
            });
            const result = await handleResponse(response);
            showNotification('技能创建成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to create skill:', error);
            showNotification('技能创建失败', 'error');
            throw error;
        }
    },

    /**
     * 更新技能
     * @param {string} skillId - 技能ID
     * @param {Object} skillData - 技能数据
     * @returns {Promise<any>} 更新结果
     */
    async updateSkill(skillId, skillData) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/skills/${skillId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(skillData)
            });
            const result = await handleResponse(response);
            showNotification('技能更新成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to update skill:', error);
            showNotification('技能更新失败', 'error');
            throw error;
        }
    },

    /**
     * 删除技能
     * @param {string} skillId - 技能ID
     * @returns {Promise<any>} 删除结果
     */
    async deleteSkill(skillId) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/skills/${skillId}`, {
                method: 'DELETE'
            });
            const result = await handleResponse(response);
            showNotification('技能删除成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to delete skill:', error);
            showNotification('技能删除失败', 'error');
            throw error;
        }
    },

    /**
     * 执行技能
     * @param {string} skillId - 技能ID
     * @param {Object} params - 执行参数
     * @returns {Promise<any>} 执行结果
     */
    async executeSkill(skillId, params = {}) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/skills/${skillId}/execute`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(params)
            });
            const result = await handleResponse(response);
            showNotification('技能执行成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to execute skill:', error);
            showNotification('技能执行失败', 'error');
            throw error;
        }
    }
};

/**
 * 执行管理 API
 */
export const executionApi = {
    /**
     * 获取执行列表
     * @param {Object} params - 查询参数
     * @returns {Promise<any>} 执行列表
     */
    async getExecutions(params = {}) {
        try {
            const queryString = new URLSearchParams(params).toString();
            const url = `${API_CONFIG.baseUrl}/api/executions${queryString ? `?${queryString}` : ''}`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get executions:', error);
            showNotification('获取执行列表失败', 'error');
            throw error;
        }
    },

    /**
     * 获取执行详情
     * @param {string} executionId - 执行ID
     * @returns {Promise<any>} 执行详情
     */
    async getExecutionDetail(executionId) {
        try {
            const url = `${API_CONFIG.baseUrl}/api/executions/${executionId}`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get execution detail:', error);
            showNotification('获取执行详情失败', 'error');
            throw error;
        }
    },

    /**
     * 取消执行
     * @param {string} executionId - 执行ID
     * @returns {Promise<any>} 取消结果
     */
    async cancelExecution(executionId) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/executions/${executionId}/cancel`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('执行取消成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to cancel execution:', error);
            showNotification('执行取消失败', 'error');
            throw error;
        }
    }
};

/**
 * 市场管理 API
 */
export const marketApi = {
    /**
     * 获取市场技能列表
     * @param {Object} params - 查询参数
     * @returns {Promise<any>} 技能列表
     */
    async getMarketSkills(params = {}) {
        try {
            const queryString = new URLSearchParams(params).toString();
            const url = `${API_CONFIG.baseUrl}/api/market/skills${queryString ? `?${queryString}` : ''}`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get market skills:', error);
            showNotification('获取市场技能列表失败', 'error');
            throw error;
        }
    },

    /**
     * 下载技能
     * @param {string} skillId - 技能ID
     * @returns {Promise<any>} 下载结果
     */
    async downloadSkill(skillId) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/market/skills/${skillId}/download`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('技能下载成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to download skill:', error);
            showNotification('技能下载失败', 'error');
            throw error;
        }
    },

    /**
     * 评分技能
     * @param {string} skillId - 技能ID
     * @param {number} rating - 评分(1-5)
     * @param {string} review - 评价内容
     * @returns {Promise<any>} 评分结果
     */
    async rateSkill(skillId, rating, review) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/market/skills/${skillId}/rate`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ rating, review })
            });
            const result = await handleResponse(response);
            showNotification('技能评分成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to rate skill:', error);
            showNotification('技能评分失败', 'error');
            throw error;
        }
    }
};

/**
 * 存储管理 API
 */
export const storageApi = {
    /**
     * 获取存储状态
     * @returns {Promise<any>} 存储状态
     */
    async getStorageStatus() {
        try {
            const url = `${API_CONFIG.baseUrl}/api/storage/status`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get storage status:', error);
            showNotification('获取存储状态失败', 'error');
            throw error;
        }
    },

    /**
     * 备份存储
     * @returns {Promise<any>} 备份结果
     */
    async backupStorage() {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/storage/backup`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('存储备份成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to backup storage:', error);
            showNotification('存储备份失败', 'error');
            throw error;
        }
    },

    /**
     * 恢复存储
     * @param {string} backupId - 备份ID
     * @returns {Promise<any>} 恢复结果
     */
    async restoreStorage(backupId) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/storage/restore/${backupId}`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('存储恢复成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to restore storage:', error);
            showNotification('存储恢复失败', 'error');
            throw error;
        }
    },

    /**
     * 清理存储
     * @returns {Promise<any>} 清理结果
     */
    async cleanStorage() {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/storage/clean`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('存储清理成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to clean storage:', error);
            showNotification('存储清理失败', 'error');
            throw error;
        }
    }
};

/**
 * 系统管理 API
 */
export const systemApi = {
    /**
     * 获取系统信息
     * @returns {Promise<any>} 系统信息
     */
    async getSystemInfo() {
        try {
            const url = `${API_CONFIG.baseUrl}/api/system/info`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get system info:', error);
            showNotification('获取系统信息失败', 'error');
            throw error;
        }
    },

    /**
     * 获取系统状态
     * @returns {Promise<any>} 系统状态
     */
    async getSystemStatus() {
        try {
            const url = `${API_CONFIG.baseUrl}/api/system/status`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get system status:', error);
            showNotification('获取系统状态失败', 'error');
            throw error;
        }
    },

    /**
     * 重启系统
     * @returns {Promise<any>} 重启结果
     */
    async restartSystem() {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/system/restart`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('系统重启成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to restart system:', error);
            showNotification('系统重启失败', 'error');
            throw error;
        }
    },

    /**
     * 关闭系统
     * @returns {Promise<any>} 关闭结果
     */
    async shutdownSystem() {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/system/shutdown`, {
                method: 'POST'
            });
            const result = await handleResponse(response);
            showNotification('系统关闭成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to shutdown system:', error);
            showNotification('系统关闭失败', 'error');
            throw error;
        }
    }
};

/**
 * 个人中心 API
 */
export const personalApi = {
    /**
     * 获取个人信息
     * @returns {Promise<any>} 个人信息
     */
    async getProfile() {
        try {
            const url = `${API_CONFIG.baseUrl}/api/personal/profile`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get profile:', error);
            showNotification('获取个人信息失败', 'error');
            throw error;
        }
    },

    /**
     * 更新个人信息
     * @param {Object} profileData - 个人信息
     * @returns {Promise<any>} 更新结果
     */
    async updateProfile(profileData) {
        try {
            const response = await fetch(`${API_CONFIG.baseUrl}/api/personal/profile`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(profileData)
            });
            const result = await handleResponse(response);
            showNotification('个人信息更新成功', 'success');
            return result;
        } catch (error) {
            console.error('Failed to update profile:', error);
            showNotification('个人信息更新失败', 'error');
            throw error;
        }
    },

    /**
     * 获取个人技能列表
     * @returns {Promise<any>} 技能列表
     */
    async getPersonalSkills() {
        try {
            const url = `${API_CONFIG.baseUrl}/api/personal/skills`;
            return await fetchJson(url);
        } catch (error) {
            console.error('Failed to get personal skills:', error);
            showNotification('获取个人技能列表失败', 'error');
            throw error;
        }
    }
};
