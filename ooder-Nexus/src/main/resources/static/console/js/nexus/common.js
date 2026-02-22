/**
 * Nexus公共JS模块
 * 提供Nexus相关页面的通用功能
 * 
 * 本模块集成了 SDK 切换机制，支持在 Mock SDK 和 Real SDK 之间切换。
 * 
 * 配置说明：
 * - SDK 实现方式通过配置文件或环境变量控制
 * - 默认使用 Mock SDK，方便开发和测试
 * - 生产环境建议使用 Real SDK，确保数据准确性
 * - 支持动态切换 SDK 实现方式，无需重启应用
 * 
 * 使用方式：
 * 1. 在 HTML 中引入本文件：
 *    <script src="../../js/nexus/common.js"></script>
 * 2. 在页面中使用 SDK：
 *    const result = await nexusCommon.getDashboardData();
 * 3. 切换 SDK 类型：
 *    nexusCommon.switchToRealSDK();
 * 
 * @module NexusCommon
 * @version 1.0.0
 */

// 导入各模块
let NexusBase = null;
let NexusApi = null;
let NexusUI = null;
let NexusUtils = null;
let NexusSdkProxy = null;

// 动态导入模块
if (typeof window !== 'undefined') {
    // 浏览器环境，使用同步加载
    // 检查全局变量是否存在
    if (window.NexusBase) {
        NexusBase = window.NexusBase;
    } else {
        console.error('NexusBase not found');
    }
    if (window.NexusApi) {
        NexusApi = window.NexusApi;
    } else {
        console.error('NexusApi not found');
    }
    if (window.NexusUI) {
        NexusUI = window.NexusUI;
    } else {
        console.error('NexusUI not found');
    }
    if (window.NexusUtils) {
        NexusUtils = window.NexusUtils;
    } else {
        console.error('NexusUtils not found');
    }
    if (window.NexusSdkProxy) {
        NexusSdkProxy = window.NexusSdkProxy;
    } else {
        console.error('NexusSdkProxy not found');
    }
} else if (typeof module !== 'undefined' && module.exports) {
    // Node.js环境，使用require加载
    try {
        NexusBase = require('./base');
        NexusApi = require('./api');
        NexusUI = require('./ui');
        NexusUtils = require('./utils');
        NexusSdkProxy = require('./sdk-proxy');
    } catch (e) {
        console.warn('Failed to load modules:', e);
    }
}

class NexusCommon extends NexusBase {
    constructor() {
        super();
        
        // 初始化各模块实例
        this.api = new NexusApi(this);
        this.ui = new NexusUI(this);
        this.utils = new NexusUtils(this);
        this.sdkProxy = new NexusSdkProxy(this);
        
        // 代理API方法到当前实例
        this._proxyMethods();
    }
    
    /**
     * 代理各模块的方法到当前实例
     * 保持向后兼容性
     */
    _proxyMethods() {
        // 代理API方法
        const apiMethods = ['ajax', 'validateApiResponse', 'get', 'post', 'put', 'delete', 'handleApiError', 'handleApiSuccess'];
        apiMethods.forEach(method => {
            if (this.api[method]) {
                this[method] = (...args) => this.api[method](...args);
            }
        });
        
        // 代理UI方法
        const uiMethods = ['showLoading', 'hideLoading', 'showMessage'];
        uiMethods.forEach(method => {
            if (this.ui[method]) {
                this[method] = (...args) => this.ui[method](...args);
            }
        });
        
        // 代理工具方法
        const utilsMethods = ['formatNumber', 'formatTimestamp', 'generateId', 'debounce', 'throttle', 'deepClone', 'isEmpty', 'get'];
        utilsMethods.forEach(method => {
            if (this.utils[method]) {
                this[method] = (...args) => this.utils[method](...args);
            }
        });
        
        // 代理SDK方法
        const sdkMethods = [
            'getDashboardData', 'getSystemStatus', 'getSecurityStatus', 'getHealthData',
            'getEndAgents', 'getProtocolHandlers', 'getLogs', 'getConfigs',
            'getSystemConfig', 'getNetworkConfig', 'getTerminalConfig', 'getServiceConfig',
            'saveConfig', 'exportConfig', 'importConfig', 'resetConfig',
            'getConfigHistory', 'applyConfigHistory', 'addUser', 'editUser',
            'deleteUser', 'enableUser', 'disableUser', 'getUsers',
            'getPermissions', 'savePermissions', 'getSecuritySettings', 'saveSecuritySettings',
            'getSecurityLogs', 'runHealthCheck', 'exportHealthReport', 'scheduleHealthCheck',
            'checkService', 'addEndAgent', 'editEndAgent', 'deleteEndAgent',
            'getEndAgentDetails', 'refreshEndAgents', 'searchEndAgents', 'registerProtocolHandler',
            'removeProtocolHandler', 'handleProtocolCommand', 'refreshProtocolHandlers', 'searchProtocolHandlers',
            'refreshLogs', 'exportLogs', 'clearLogs', 'filterLogs',
            'getLogDetails', 'refreshSystemStatus', 'restartSystem', 'exportSystemReport',
            'restartService', 'stopService', 'refreshNetworkStatus', 'refreshCommandStats',
            'refreshSecurityStatus', 'getVersion'
        ];
        sdkMethods.forEach(method => {
            if (this.sdkProxy[method]) {
                this[method] = (...args) => this.sdkProxy[method](...args);
            }
        });
    }
}

const nexusCommon = new NexusCommon();

if (typeof window !== 'undefined') {
    window.NexusCommon = NexusCommon;
    window.nexusCommon = nexusCommon;
    // 不需要重新导出各模块，它们已经在各自的文件中导出到全局了
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = nexusCommon;
    module.exports.NexusCommon = NexusCommon;
    module.exports.NexusBase = NexusBase;
    module.exports.NexusApi = NexusApi;
    module.exports.NexusUI = NexusUI;
    module.exports.NexusUtils = NexusUtils;
    module.exports.NexusSdkProxy = NexusSdkProxy;
}
