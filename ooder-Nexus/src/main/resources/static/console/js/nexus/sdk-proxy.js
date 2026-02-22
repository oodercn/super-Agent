/**
 * Nexus SDK代理模块
 * 包含所有SDK方法的代理封装
 * @module nexusSdkProxy
 */

(function() {
    class NexusSdkProxy {
        constructor(base) {
            this.base = base;
        }

        /**
         * 获取仪表盘数据
         * @returns {Promise<Object>} 仪表盘数据
         */
        async getDashboardData() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getDashboardData) {
                return await sdk.getDashboardData();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取系统状态
         * @returns {Promise<Object>} 系统状态
         */
        async getSystemStatus() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getSystemStatus) {
                return await sdk.getSystemStatus();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取安全状态
         * @returns {Promise<Object>} 安全状态
         */
        async getSecurityStatus() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getSecurityStatus) {
                return await sdk.getSecurityStatus();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取健康检查数据
         * @returns {Promise<Object>} 健康检查数据
         */
        async getHealthData() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getHealthData) {
                return await sdk.getHealthData();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取终端列表
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 终端列表
         */
        async getEndAgents(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getEndAgents) {
                return await sdk.getEndAgents(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取协议处理器列表
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 协议处理器列表
         */
        async getProtocolHandlers(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getProtocolHandlers) {
                return await sdk.getProtocolHandlers(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取日志列表
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 日志列表
         */
        async getLogs(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getLogs) {
                return await sdk.getLogs(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取配置列表
         * @returns {Promise<Object>} 配置列表
         */
        async getConfigs() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getConfigs) {
                return await sdk.getConfigs();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取系统配置
         * @returns {Promise<Object>} 系统配置
         */
        async getSystemConfig() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getSystemConfig) {
                return await sdk.getSystemConfig();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取网络配置
         * @returns {Promise<Object>} 网络配置
         */
        async getNetworkConfig() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getNetworkConfig) {
                return await sdk.getNetworkConfig();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取终端配置
         * @returns {Promise<Object>} 终端配置
         */
        async getTerminalConfig() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getTerminalConfig) {
                return await sdk.getTerminalConfig();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取服务配置
         * @returns {Promise<Object>} 服务配置
         */
        async getServiceConfig() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getServiceConfig) {
                return await sdk.getServiceConfig();
            }
            throw new Error('SDK not available');
        }

        /**
         * 保存配置
         * @param {Object} configData - 配置数据
         * @returns {Promise<Object>} 保存结果
         */
        async saveConfig(configData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.saveConfig) {
                return await sdk.saveConfig(configData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 导出配置
         * @param {Object} params - 导出参数
         * @returns {Promise<Object>} 导出结果
         */
        async exportConfig(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.exportConfig) {
                return await sdk.exportConfig(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 导入配置
         * @param {Object} params - 导入参数
         * @returns {Promise<Object>} 导入结果
         */
        async importConfig(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.importConfig) {
                return await sdk.importConfig(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 重置配置
         * @returns {Promise<Object>} 重置结果
         */
        async resetConfig() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.resetConfig) {
                return await sdk.resetConfig();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取配置历史
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 配置历史
         */
        async getConfigHistory(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getConfigHistory) {
                return await sdk.getConfigHistory(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 应用配置历史
         * @param {string} historyId - 历史 ID
         * @returns {Promise<Object>} 应用结果
         */
        async applyConfigHistory(historyId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.applyConfigHistory) {
                return await sdk.applyConfigHistory(historyId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 添加用户
         * @param {Object} userData - 用户数据
         * @returns {Promise<Object>} 添加结果
         */
        async addUser(userData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.addUser) {
                return await sdk.addUser(userData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 编辑用户
         * @param {string} userId - 用户 ID
         * @param {Object} userData - 用户数据
         * @returns {Promise<Object>} 编辑结果
         */
        async editUser(userId, userData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.editUser) {
                return await sdk.editUser(userId, userData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 删除用户
         * @param {string} userId - 用户 ID
         * @returns {Promise<Object>} 删除结果
         */
        async deleteUser(userId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.deleteUser) {
                return await sdk.deleteUser(userId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 启用用户
         * @param {string} userId - 用户 ID
         * @returns {Promise<Object>} 启用结果
         */
        async enableUser(userId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.enableUser) {
                return await sdk.enableUser(userId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 禁用用户
         * @param {string} userId - 用户 ID
         * @returns {Promise<Object>} 禁用结果
         */
        async disableUser(userId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.disableUser) {
                return await sdk.disableUser(userId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取用户列表
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 用户列表
         */
        async getUsers(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getUsers) {
                return await sdk.getUsers(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取权限列表
         * @returns {Promise<Object>} 权限列表
         */
        async getPermissions() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getPermissions) {
                return await sdk.getPermissions();
            }
            throw new Error('SDK not available');
        }

        /**
         * 保存权限设置
         * @param {Object} permissions - 权限设置
         * @returns {Promise<Object>} 保存结果
         */
        async savePermissions(permissions) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.savePermissions) {
                return await sdk.savePermissions(permissions);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取安全设置
         * @returns {Promise<Object>} 安全设置
         */
        async getSecuritySettings() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getSecuritySettings) {
                return await sdk.getSecuritySettings();
            }
            throw new Error('SDK not available');
        }

        /**
         * 保存安全设置
         * @param {Object} settings - 安全设置
         * @returns {Promise<Object>} 保存结果
         */
        async saveSecuritySettings(settings) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.saveSecuritySettings) {
                return await sdk.saveSecuritySettings(settings);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取安全日志
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} 安全日志
         */
        async getSecurityLogs(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getSecurityLogs) {
                return await sdk.getSecurityLogs(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 运行健康检查
         * @param {Object} params - 检查参数
         * @returns {Promise<Object>} 检查结果
         */
        async runHealthCheck(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.runHealthCheck) {
                return await sdk.runHealthCheck(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 导出健康报告
         * @returns {Promise<Object>} 导出结果
         */
        async exportHealthReport() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.exportHealthReport) {
                return await sdk.exportHealthReport();
            }
            throw new Error('SDK not available');
        }

        /**
         * 设置定时健康检查
         * @param {Object} params - 定时参数
         * @returns {Promise<Object>} 设置结果
         */
        async scheduleHealthCheck(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.scheduleHealthCheck) {
                return await sdk.scheduleHealthCheck(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 检查服务状态
         * @param {string} serviceName - 服务名称
         * @returns {Promise<Object>} 服务状态
         */
        async checkService(serviceName) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.checkService) {
                return await sdk.checkService(serviceName);
            }
            throw new Error('SDK not available');
        }

        /**
         * 添加终端
         * @param {Object} agentData - 终端数据
         * @returns {Promise<Object>} 添加结果
         */
        async addEndAgent(agentData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.addEndAgent) {
                return await sdk.addEndAgent(agentData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 编辑终端
         * @param {string} agentId - 终端 ID
         * @param {Object} agentData - 终端数据
         * @returns {Promise<Object>} 编辑结果
         */
        async editEndAgent(agentId, agentData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.editEndAgent) {
                return await sdk.editEndAgent(agentId, agentData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 删除终端
         * @param {string} agentId - 终端 ID
         * @returns {Promise<Object>} 删除结果
         */
        async deleteEndAgent(agentId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.deleteEndAgent) {
                return await sdk.deleteEndAgent(agentId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取终端详情
         * @param {string} agentId - 终端 ID
         * @returns {Promise<Object>} 终端详情
         */
        async getEndAgentDetails(agentId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getEndAgentDetails) {
                return await sdk.getEndAgentDetails(agentId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新终端列表
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshEndAgents() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshEndAgents) {
                return await sdk.refreshEndAgents();
            }
            throw new Error('SDK not available');
        }

        /**
         * 搜索终端
         * @param {Object} params - 搜索参数
         * @returns {Promise<Object>} 搜索结果
         */
        async searchEndAgents(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.searchEndAgents) {
                return await sdk.searchEndAgents(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 注册协议处理器
         * @param {Object} handlerData - 处理器数据
         * @returns {Promise<Object>} 注册结果
         */
        async registerProtocolHandler(handlerData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.registerProtocolHandler) {
                return await sdk.registerProtocolHandler(handlerData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 移除协议处理器
         * @param {string} commandType - 命令类型
         * @returns {Promise<Object>} 移除结果
         */
        async removeProtocolHandler(commandType) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.removeProtocolHandler) {
                return await sdk.removeProtocolHandler(commandType);
            }
            throw new Error('SDK not available');
        }

        /**
         * 处理协议命令
         * @param {Object} commandData - 命令数据
         * @returns {Promise<Object>} 处理结果
         */
        async handleProtocolCommand(commandData) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.handleProtocolCommand) {
                return await sdk.handleProtocolCommand(commandData);
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新协议处理器列表
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshProtocolHandlers() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshProtocolHandlers) {
                return await sdk.refreshProtocolHandlers();
            }
            throw new Error('SDK not available');
        }

        /**
         * 搜索协议处理器
         * @param {Object} params - 搜索参数
         * @returns {Promise<Object>} 搜索结果
         */
        async searchProtocolHandlers(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.searchProtocolHandlers) {
                return await sdk.searchProtocolHandlers(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新日志
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshLogs() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshLogs) {
                return await sdk.refreshLogs();
            }
            throw new Error('SDK not available');
        }

        /**
         * 导出日志
         * @param {Object} params - 导出参数
         * @returns {Promise<Object>} 导出结果
         */
        async exportLogs(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.exportLogs) {
                return await sdk.exportLogs(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 清空日志
         * @returns {Promise<Object>} 清空结果
         */
        async clearLogs() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.clearLogs) {
                return await sdk.clearLogs();
            }
            throw new Error('SDK not available');
        }

        /**
         * 过滤日志
         * @param {Object} filters - 过滤条件
         * @returns {Promise<Object>} 过滤结果
         */
        async filterLogs(filters = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.filterLogs) {
                return await sdk.filterLogs(filters);
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取日志详情
         * @param {string} logId - 日志 ID
         * @returns {Promise<Object>} 日志详情
         */
        async getLogDetails(logId) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getLogDetails) {
                return await sdk.getLogDetails(logId);
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新系统状态
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshSystemStatus() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshSystemStatus) {
                return await sdk.refreshSystemStatus();
            }
            throw new Error('SDK not available');
        }

        /**
         * 重启系统
         * @param {Object} params - 重启参数
         * @returns {Promise<Object>} 重启结果
         */
        async restartSystem(params = {}) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.restartSystem) {
                return await sdk.restartSystem(params);
            }
            throw new Error('SDK not available');
        }

        /**
         * 导出系统报告
         * @returns {Promise<Object>} 导出结果
         */
        async exportSystemReport() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.exportSystemReport) {
                return await sdk.exportSystemReport();
            }
            throw new Error('SDK not available');
        }

        /**
         * 重启服务
         * @param {string} serviceName - 服务名称
         * @returns {Promise<Object>} 重启结果
         */
        async restartService(serviceName) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.restartService) {
                return await sdk.restartService(serviceName);
            }
            throw new Error('SDK not available');
        }

        /**
         * 停止服务
         * @param {string} serviceName - 服务名称
         * @returns {Promise<Object>} 停止结果
         */
        async stopService(serviceName) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.stopService) {
                return await sdk.stopService(serviceName);
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新网络状态
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshNetworkStatus() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshNetworkStatus) {
                return await sdk.refreshNetworkStatus();
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新命令统计
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshCommandStats() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshCommandStats) {
                return await sdk.refreshCommandStats();
            }
            throw new Error('SDK not available');
        }

        /**
         * 刷新安全状态
         * @returns {Promise<Object>} 刷新结果
         */
        async refreshSecurityStatus() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.refreshSecurityStatus) {
                return await sdk.refreshSecurityStatus();
            }
            throw new Error('SDK not available');
        }

        /**
         * 获取 SDK 版本信息
         * @returns {Promise<Object>} 版本信息
         */
        async getVersion() {
            const sdk = this.base.getSDK();
            if (sdk && sdk.getVersion) {
                return await sdk.getVersion();
            }
            throw new Error('SDK not available');
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusSdkProxy;
    }

    if (typeof window !== 'undefined') {
        window.NexusSdkProxy = NexusSdkProxy;
    }
})();

