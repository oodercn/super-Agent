/**
 * Nexus Mock SDK 实现
 * 
 * 本文件实现了 Nexus SDK 的 Mock 版本，使用模拟数据，用于开发和测试。
 * 
 * 配置说明：
 * - Mock SDK 使用 nexusMockData 类中的模拟数据
 * - 所有操作都返回成功的响应，不进行实际的 API 调用
 * - 适用于开发、测试和演示环境
 * 
 * @module nexusSDK
 * @version 1.0.0
 */

/**
 * Mock SDK 实现类
 * 实现 InexusSDK 接口，使用模拟数据
 */
class nexusMockSDK {
    constructor(config = {}) {
        this.config = {
            delay: config.delay || 500,
            errorRate: config.errorRate || 0
        };
        this.initialized = false;
    }

    /**
     * 初始化 SDK
     * @param {Object} config - SDK 配置
     * @returns {Promise<void>}
     */
    async initialize(config = {}) {
        this.config = { ...this.config, ...config };
        this.initialized = true;
        console.log('[nexusMockSDK] SDK initialized with config:', this.config);
    }

    /**
     * 模拟延迟
     * @returns {Promise<void>}
     */
    async _delay() {
        return new Promise(resolve => setTimeout(resolve, this.config.delay));
    }

    /**
     * 模拟错误
     * @returns {boolean} 是否返回错误
     */
    _shouldError() {
        return Math.random() < this.config.errorRate;
    }

    /**
     * 创建成功响应
     * @param {*} data - 响应数据
     * @param {string} message - 响应消息
     * @returns {Object} API 响应
     */
    _createSuccessResponse(data, message = '操作成功') {
        return {
            status: 'success',
            message: message,
            data: data,
            timestamp: Date.now()
        };
    }

    /**
     * 创建错误响应
     * @param {string} message - 错误消息
     * @returns {Object} API 响应
     */
    _createErrorResponse(message = '操作失败') {
        return {
            status: 'error',
            message: message,
            timestamp: Date.now()
        };
    }

    // ==================== Dashboard 模块 ====================

    /**
     * 获取仪表盘数据
     * @returns {Promise<ApiResponse>}
     */
    async getDashboardData() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取仪表盘数据失败');
        }
        return this._createSuccessResponse(nexusMockData.getDashboardData());
    }

    /**
     * 刷新网络状态
     * @returns {Promise<ApiResponse>}
     */
    async refreshNetworkStatus() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新网络状态失败');
        }
        return this._createSuccessResponse(nexusMockData.getDashboardData());
    }

    /**
     * 刷新命令统计
     * @returns {Promise<ApiResponse>}
     */
    async refreshCommandStats() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新命令统计失败');
        }
        return this._createSuccessResponse(nexusMockData.getDashboardData());
    }

    // ==================== System Status 模块 ====================

    /**
     * 获取系统状态
     * @returns {Promise<ApiResponse>}
     */
    async getSystemStatus() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取系统状态失败');
        }
        return this._createSuccessResponse(nexusMockData.getSystemStatus());
    }

    /**
     * 刷新系统状态
     * @returns {Promise<ApiResponse>}
     */
    async refreshSystemStatus() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新系统状态失败');
        }
        return this._createSuccessResponse(nexusMockData.getSystemStatus());
    }

    /**
     * 重启系统
     * @param {Object} params - 重启参数
     * @returns {Promise<ApiResponse>}
     */
    async restartSystem(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('重启系统失败');
        }
        return this._createSuccessResponse({ reason: params.reason || '用户手动重启' }, '系统重启成功');
    }

    /**
     * 导出系统报告
     * @returns {Promise<ApiResponse>}
     */
    async exportSystemReport() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('导出系统报告失败');
        }
        return this._createSuccessResponse(nexusMockData.getSystemStatus(), '系统报告导出成功');
    }

    /**
     * 重启服务
     * @param {string} serviceName - 服务名称
     * @returns {Promise<ApiResponse>}
     */
    async restartService(serviceName) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse(`重启服务 ${serviceName} 失败`);
        }
        return this._createSuccessResponse({ serviceName }, `服务 ${serviceName} 重启成功`);
    }

    /**
     * 停止服务
     * @param {string} serviceName - 服务名称
     * @returns {Promise<ApiResponse>}
     */
    async stopService(serviceName) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse(`停止服务 ${serviceName} 失败`);
        }
        return this._createSuccessResponse({ serviceName }, `服务 ${serviceName} 停止成功`);
    }

    // ==================== Security Management 模块 ====================

    /**
     * 获取安全状态
     * @returns {Promise<ApiResponse>}
     */
    async getSecurityStatus() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取安全状态失败');
        }
        return this._createSuccessResponse(nexusMockData.getSecurityStatus());
    }

    /**
     * 刷新安全状态
     * @returns {Promise<ApiResponse>}
     */
    async refreshSecurityStatus() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新安全状态失败');
        }
        return this._createSuccessResponse(nexusMockData.getSecurityStatus());
    }

    /**
     * 获取用户列表
     * @param {SearchParams} [params] - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async getUsers(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取用户列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getUsers());
    }

    /**
     * 添加用户
     * @param {Object} userData - 用户数据
     * @returns {Promise<ApiResponse>}
     */
    async addUser(userData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('添加用户失败');
        }
        const newUser = {
            id: Date.now(),
            ...userData,
            status: 'active',
            lastLogin: new Date().toISOString()
        };
        return this._createSuccessResponse(newUser, '用户添加成功');
    }

    /**
     * 编辑用户
     * @param {string} userId - 用户 ID
     * @param {Object} userData - 用户数据
     * @returns {Promise<ApiResponse>}
     */
    async editUser(userId, userData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('编辑用户失败');
        }
        return this._createSuccessResponse({ id: userId, ...userData }, '用户编辑成功');
    }

    /**
     * 删除用户
     * @param {string} userId - 用户 ID
     * @returns {Promise<ApiResponse>}
     */
    async deleteUser(userId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('删除用户失败');
        }
        return this._createSuccessResponse({ id: userId }, '用户删除成功');
    }

    /**
     * 启用用户
     * @param {string} userId - 用户 ID
     * @returns {Promise<ApiResponse>}
     */
    async enableUser(userId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('启用用户失败');
        }
        return this._createSuccessResponse({ id: userId }, '用户启用成功');
    }

    /**
     * 禁用用户
     * @param {string} userId - 用户 ID
     * @returns {Promise<ApiResponse>}
     */
    async disableUser(userId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('禁用用户失败');
        }
        return this._createSuccessResponse({ id: userId }, '用户禁用成功');
    }

    /**
     * 获取权限列表
     * @returns {Promise<ApiResponse>}
     */
    async getPermissions() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取权限列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getPermissions());
    }

    /**
     * 保存权限设置
     * @param {Object} permissions - 权限设置
     * @returns {Promise<ApiResponse>}
     */
    async savePermissions(permissions) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('保存权限设置失败');
        }
        return this._createSuccessResponse(permissions, '权限设置保存成功');
    }

    /**
     * 获取安全设置
     * @returns {Promise<ApiResponse>}
     */
    async getSecuritySettings() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取安全设置失败');
        }
        return this._createSuccessResponse({
            passwordPolicy: 'medium',
            sessionTimeout: 30,
            maxLoginAttempts: 5,
            blockDuration: 15,
            enableTwoFactor: true,
            enableIpRestriction: false
        });
    }

    /**
     * 保存安全设置
     * @param {Object} settings - 安全设置
     * @returns {Promise<ApiResponse>}
     */
    async saveSecuritySettings(settings) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('保存安全设置失败');
        }
        return this._createSuccessResponse(settings, '安全设置保存成功');
    }

    /**
     * 获取安全日志
     * @param {SearchParams} [params] - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async getSecurityLogs(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取安全日志失败');
        }
        return this._createSuccessResponse(nexusMockData.getSecurityLogs());
    }

    // ==================== Health Check 模块 ====================

    /**
     * 获取健康检查数据
     * @returns {Promise<ApiResponse>}
     */
    async getHealthData() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取健康检查数据失败');
        }
        return this._createSuccessResponse(nexusMockData.getHealthData());
    }

    /**
     * 运行健康检查
     * @param {Object} params - 检查参数
     * @returns {Promise<ApiResponse>}
     */
    async runHealthCheck(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('运行健康检查失败');
        }
        return this._createSuccessResponse(nexusMockData.getHealthData(), '健康检查完成');
    }

    /**
     * 导出健康报告
     * @returns {Promise<ApiResponse>}
     */
    async exportHealthReport() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('导出健康报告失败');
        }
        return this._createSuccessResponse(nexusMockData.getHealthData(), '健康报告导出成功');
    }

    /**
     * 设置定时健康检查
     * @param {Object} params - 定时参数
     * @returns {Promise<ApiResponse>}
     */
    async scheduleHealthCheck(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('设置定时健康检查失败');
        }
        return this._createSuccessResponse(params, '定时健康检查设置成功');
    }

    /**
     * 检查服务状态
     * @param {string} serviceName - 服务名称
     * @returns {Promise<ApiResponse>}
     */
    async checkService(serviceName) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse(`检查服务 ${serviceName} 失败`);
        }
        return this._createSuccessResponse({
            serviceName,
            status: 'running',
            responseTime: Math.floor(Math.random() * 50) + 10
        }, `服务 ${serviceName} 状态正常`);
    }

    // ==================== End Agent Management 模块 ====================

    /**
     * 获取终端列表
     * @param {SearchParams} [params] - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async getEndAgents(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取终端列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getEndAgents());
    }

    /**
     * 添加终端
     * @param {Object} agentData - 终端数据
     * @returns {Promise<ApiResponse>}
     */
    async addEndAgent(agentData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('添加终端失败');
        }
        const newAgent = {
            id: 'endagent-' + Date.now(),
            ...agentData,
            status: 'active',
            lastUpdate: new Date().toISOString()
        };
        return this._createSuccessResponse(newAgent, '终端添加成功');
    }

    /**
     * 编辑终端
     * @param {string} agentId - 终端 ID
     * @param {Object} agentData - 终端数据
     * @returns {Promise<ApiResponse>}
     */
    async editEndAgent(agentId, agentData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('编辑终端失败');
        }
        return this._createSuccessResponse({ id: agentId, ...agentData }, '终端编辑成功');
    }

    /**
     * 删除终端
     * @param {string} agentId - 终端 ID
     * @returns {Promise<ApiResponse>}
     */
    async deleteEndAgent(agentId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('删除终端失败');
        }
        return this._createSuccessResponse({ id: agentId }, '终端删除成功');
    }

    /**
     * 获取终端详情
     * @param {string} agentId - 终端 ID
     * @returns {Promise<ApiResponse>}
     */
    async getEndAgentDetails(agentId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取终端详情失败');
        }
        const agents = nexusMockData.getEndAgents();
        const agent = agents.find(a => a.id === agentId);
        if (!agent) {
            return this._createErrorResponse('终端不存在');
        }
        return this._createSuccessResponse(agent, '获取终端详情成功');
    }

    /**
     * 刷新终端列表
     * @returns {Promise<ApiResponse>}
     */
    async refreshEndAgents() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新终端列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getEndAgents());
    }

    /**
     * 搜索终端
     * @param {SearchParams} params - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async searchEndAgents(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('搜索终端失败');
        }
        const agents = nexusMockData.getEndAgents();
        const keyword = (params.keyword || '').toLowerCase();
        const filteredAgents = agents.filter(agent => 
            agent.id.toLowerCase().includes(keyword) ||
            agent.name.toLowerCase().includes(keyword) ||
            agent.ip.includes(keyword)
        );
        return this._createSuccessResponse(filteredAgents, '搜索终端成功');
    }

    // ==================== Protocol Management 模块 ====================

    /**
     * 获取协议处理器列表
     * @param {SearchParams} [params] - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async getProtocolHandlers(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取协议处理器列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getProtocolHandlers());
    }

    /**
     * 注册协议处理器
     * @param {Object} handlerData - 处理器数据
     * @returns {Promise<ApiResponse>}
     */
    async registerProtocolHandler(handlerData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('注册协议处理器失败');
        }
        const newHandler = {
            commandType: handlerData.commandType,
            name: handlerData.name || handlerData.commandType,
            description: handlerData.description || '无描述',
            registeredTime: new Date().toISOString(),
            status: 'active'
        };
        return this._createSuccessResponse(newHandler, '协议处理器注册成功');
    }

    /**
     * 移除协议处理器
     * @param {string} commandType - 命令类型
     * @returns {Promise<ApiResponse>}
     */
    async removeProtocolHandler(commandType) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('移除协议处理器失败');
        }
        return this._createSuccessResponse({ commandType }, '协议处理器移除成功');
    }

    /**
     * 处理协议命令
     * @param {Object} commandData - 命令数据
     * @returns {Promise<ApiResponse>}
     */
    async handleProtocolCommand(commandData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('处理协议命令失败');
        }
        return this._createSuccessResponse({
            commandType: commandData.commandType,
            handlerName: commandData.commandType,
            commandData: commandData.data,
            result: `命令 ${commandData.commandType} 处理成功`,
            timestamp: new Date().toISOString()
        }, '协议命令处理成功');
    }

    /**
     * 刷新协议处理器列表
     * @returns {Promise<ApiResponse>}
     */
    async refreshProtocolHandlers() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新协议处理器列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getProtocolHandlers());
    }

    /**
     * 搜索协议处理器
     * @param {SearchParams} params - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async searchProtocolHandlers(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('搜索协议处理器失败');
        }
        const handlers = nexusMockData.getProtocolHandlers();
        const keyword = (params.keyword || '').toLowerCase();
        const filteredHandlers = handlers.filter(handler => 
            handler.commandType.toLowerCase().includes(keyword) ||
            handler.name.toLowerCase().includes(keyword)
        );
        return this._createSuccessResponse(filteredHandlers, '搜索协议处理器成功');
    }

    // ==================== Log Management 模块 ====================

    /**
     * 获取日志列表
     * @param {SearchParams} [params] - 搜索参数
     * @returns {Promise<ApiResponse>}
     */
    async getLogs(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取日志列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getLogs());
    }

    /**
     * 刷新日志
     * @returns {Promise<ApiResponse>}
     */
    async refreshLogs() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('刷新日志失败');
        }
        return this._createSuccessResponse(nexusMockData.getLogs());
    }

    /**
     * 导出日志
     * @param {Object} params - 导出参数
     * @returns {Promise<ApiResponse>}
     */
    async exportLogs(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('导出日志失败');
        }
        return this._createSuccessResponse(nexusMockData.getLogs(), '日志导出成功');
    }

    /**
     * 清空日志
     * @returns {Promise<ApiResponse>}
     */
    async clearLogs() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('清空日志失败');
        }
        return this._createSuccessResponse([], '日志清空成功');
    }

    /**
     * 过滤日志
     * @param {Object} filters - 过滤条件
     * @returns {Promise<ApiResponse>}
     */
    async filterLogs(filters = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('过滤日志失败');
        }
        const logs = nexusMockData.getLogs();
        const filteredLogs = logs.filter(log => {
            const matchesLevel = !filters.level || log.level === filters.level;
            const matchesSource = !filters.source || log.source === filters.source;
            const matchesKeyword = !filters.keyword || log.message.toLowerCase().includes(filters.keyword.toLowerCase());
            return matchesLevel && matchesSource && matchesKeyword;
        });
        return this._createSuccessResponse(filteredLogs, '日志过滤成功');
    }

    /**
     * 获取日志详情
     * @param {string} logId - 日志 ID
     * @returns {Promise<ApiResponse>}
     */
    async getLogDetails(logId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取日志详情失败');
        }
        const logs = nexusMockData.getLogs();
        const log = logs.find(l => l.id == logId);
        if (!log) {
            return this._createErrorResponse('日志不存在');
        }
        return this._createSuccessResponse(log, '获取日志详情成功');
    }

    // ==================== Config Management 模块 ====================

    /**
     * 获取配置列表
     * @returns {Promise<ApiResponse>}
     */
    async getConfigs() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取配置列表失败');
        }
        return this._createSuccessResponse(nexusMockData.getConfigs());
    }

    /**
     * 获取系统配置
     * @returns {Promise<ApiResponse>}
     */
    async getSystemConfig() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取系统配置失败');
        }
        const configs = nexusMockData.getConfigs();
        return this._createSuccessResponse(configs.system, '获取系统配置成功');
    }

    /**
     * 获取网络配置
     * @returns {Promise<ApiResponse>}
     */
    async getNetworkConfig() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取网络配置失败');
        }
        const configs = nexusMockData.getConfigs();
        return this._createSuccessResponse(configs.network, '获取网络配置成功');
    }

    /**
     * 获取终端配置
     * @returns {Promise<ApiResponse>}
     */
    async getTerminalConfig() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取终端配置失败');
        }
        const configs = nexusMockData.getConfigs();
        return this._createSuccessResponse(configs.terminal, '获取终端配置成功');
    }

    /**
     * 获取服务配置
     * @returns {Promise<ApiResponse>}
     */
    async getServiceConfig() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取服务配置失败');
        }
        const configs = nexusMockData.getConfigs();
        return this._createSuccessResponse(configs.service, '获取服务配置成功');
    }

    /**
     * 保存配置
     * @param {Object} configData - 配置数据
     * @returns {Promise<ApiResponse>}
     */
    async saveConfig(configData) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('保存配置失败');
        }
        return this._createSuccessResponse(configData, '配置保存成功');
    }

    /**
     * 导出配置
     * @param {Object} params - 导出参数
     * @returns {Promise<ApiResponse>}
     */
    async exportConfig(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('导出配置失败');
        }
        return this._createSuccessResponse(nexusMockData.getConfigs(), '配置导出成功');
    }

    /**
     * 导入配置
     * @param {Object} params - 导入参数
     * @returns {Promise<ApiResponse>}
     */
    async importConfig(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('导入配置失败');
        }
        return this._createSuccessResponse(params, '配置导入成功');
    }

    /**
     * 重置配置
     * @returns {Promise<ApiResponse>}
     */
    async resetConfig() {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('重置配置失败');
        }
        return this._createSuccessResponse(nexusMockData.getConfigs(), '配置重置成功');
    }

    /**
     * 获取配置历史
     * @param {PaginationParams} [params] - 分页参数
     * @returns {Promise<ApiResponse>}
     */
    async getConfigHistory(params = {}) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('获取配置历史失败');
        }
        return this._createSuccessResponse(nexusMockData.getConfigHistory());
    }

    /**
     * 应用配置历史
     * @param {string} historyId - 历史 ID
     * @returns {Promise<ApiResponse>}
     */
    async applyConfigHistory(historyId) {
        await this._delay();
        if (this._shouldError()) {
            return this._createErrorResponse('应用配置历史失败');
        }
        return this._createSuccessResponse({ historyId }, '配置历史应用成功');
    }

    // ==================== 通用方法 ====================

    /**
     * 格式化时间戳
     * @param {number} timestamp - 时间戳
     * @returns {string} 格式化后的时间字符串
     */
    formatTimestamp(timestamp) {
        return nexusMockData.formatTimestamp(timestamp);
    }

    /**
     * 格式化数字
     * @param {number} number - 数字
     * @returns {string} 格式化后的数字字符串
     */
    formatNumber(number) {
        return nexusMockData.formatNumber(number);
    }

    /**
     * 验证 API 响应
     * @param {ApiResponse} response - API 响应
     * @returns {ApiResponse} 验证后的响应
     */
    validateApiResponse(response) {
        if (!response) {
            return this._createErrorResponse('响应为空');
        }

        if (response.status === 'error' || response.status === 'warning') {
            return response;
        }

        if (!response.data && response.status !== 'success') {
            return this._createErrorResponse('响应格式错误');
        }

        return response;
    }
}

/**
 * 导出 Mock SDK
 */
if (typeof module !== 'undefined' && module.exports) {
    module.exports = nexusMockSDK;
}

