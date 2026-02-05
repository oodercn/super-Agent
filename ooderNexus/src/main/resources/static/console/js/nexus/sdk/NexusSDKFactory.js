/**
 * Nexus SDK 工厂和配置
 * 
 * 本文件提供了 SDK 的工厂类和配置管理，支持在 Mock SDK 和 Real SDK 之间切换。
 * 
 * 配置说明：
 * - SDK 实现方式通过配置文件或环境变量控制
 * - 默认使用 Mock SDK，方便开发和测试
 * - 生产环境建议使用 Real SDK，确保数据准确性
 * - 支持动态切换 SDK 实现方式，无需重启应用
 * 
 * 使用方式：
 * 1. 在 HTML 中引入 SDK 配置文件：
 *    <script src="../../js/nexus/sdk/NexusSDKConfig.js"></script>
 * 2. 在页面初始化时创建 SDK 实例：
 *    const sdk = NexusSDKFactory.createSDK();
 * 3. 调用 SDK 方法：
 *    const result = await sdk.getDashboardData();
 * 
 * @module NexusSDK
 * @version 1.0.0
 */

/**
 * SDK 配置类
 * 管理 SDK 的配置参数
 */
class NexusSDKConfig {
    /**
     * SDK 实现方式：'mock' | 'real'
     * - mock: 使用模拟数据，用于开发和测试
     * - real: 调用真实 API，用于生产环境
     */
    static SDK_TYPE = {
        MOCK: 'mock',
        REAL: 'real'
    };

    /**
     * 默认配置
     */
    static DEFAULT_CONFIG = {
        sdkType: NexusSDKConfig.SDK_TYPE.MOCK,
        baseUrl: '/api',
        timeout: 30000,
        enableLogging: true,
        enableErrorHandling: true,
        mockDelay: 500,
        mockErrorRate: 0
    };

    /**
     * 获取配置
     * @returns {Object} 配置对象
     */
    static getConfig() {
        const config = { ...NexusSDKConfig.DEFAULT_CONFIG };

        try {
            const savedConfig = localStorage.getItem('nexusSDKConfig');
            if (savedConfig) {
                const parsedConfig = JSON.parse(savedConfig);
                Object.assign(config, parsedConfig);
            }
        } catch (error) {
            console.warn('[NexusSDKConfig] Failed to load saved config:', error);
        }

        if (NexusSDKConfig.SDK_TYPE.REAL === config.sdkType) {
            console.log('[NexusSDKConfig] Using Real SDK');
        } else {
            console.log('[NexusSDKConfig] Using Mock SDK');
        }

        return config;
    }

    /**
     * 保存配置
     * @param {Object} config - 配置对象
     */
    static saveConfig(config) {
        try {
            localStorage.setItem('nexusSDKConfig', JSON.stringify(config));
            console.log('[NexusSDKConfig] Config saved:', config);
        } catch (error) {
            console.error('[NexusSDKConfig] Failed to save config:', error);
        }
    }

    /**
     * 重置配置为默认值
     */
    static resetConfig() {
        NexusSDKConfig.saveConfig(NexusSDKConfig.DEFAULT_CONFIG);
        console.log('[NexusSDKConfig] Config reset to default');
    }

    /**
     * 切换 SDK 类型
     * @param {string} sdkType - SDK 类型：'mock' | 'real'
     */
    static switchSDKType(sdkType) {
        const config = NexusSDKConfig.getConfig();
        config.sdkType = sdkType;
        NexusSDKConfig.saveConfig(config);
        console.log(`[NexusSDKConfig] SDK type switched to: ${sdkType}`);
    }

    /**
     * 设置基础 URL
     * @param {string} baseUrl - 基础 URL
     */
    static setBaseUrl(baseUrl) {
        const config = NexusSDKConfig.getConfig();
        config.baseUrl = baseUrl;
        NexusSDKConfig.saveConfig(config);
    }

    /**
     * 设置超时时间
     * @param {number} timeout - 超时时间（毫秒）
     */
    static setTimeout(timeout) {
        const config = NexusSDKConfig.getConfig();
        config.timeout = timeout;
        NexusSDKConfig.saveConfig(config);
    }

    /**
     * 启用/禁用日志
     * @param {boolean} enabled - 是否启用日志
     */
    static setLoggingEnabled(enabled) {
        const config = NexusSDKConfig.getConfig();
        config.enableLogging = enabled;
        NexusSDKConfig.saveConfig(config);
    }

    /**
     * 设置 Mock 延迟时间
     * @param {number} delay - 延迟时间（毫秒）
     */
    static setMockDelay(delay) {
        const config = NexusSDKConfig.getConfig();
        config.mockDelay = delay;
        NexusSDKConfig.saveConfig(config);
    }

    /**
     * 设置 Mock 错误率
     * @param {number} rate - 错误率（0-1）
     */
    static setMockErrorRate(rate) {
        const config = NexusSDKConfig.getConfig();
        config.mockErrorRate = rate;
        NexusSDKConfig.saveConfig(config);
    }

    /**
     * 获取当前 SDK 类型
     * @returns {string} SDK 类型
     */
    static getCurrentSDKType() {
        const config = NexusSDKConfig.getConfig();
        return config.sdkType;
    }

    /**
     * 检查是否使用 Mock SDK
     * @returns {boolean} 是否使用 Mock SDK
     */
    static isMockMode() {
        return NexusSDKConfig.getCurrentSDKType() === NexusSDKConfig.SDK_TYPE.MOCK;
    }

    /**
     * 检查是否使用 Real SDK
     * @returns {boolean} 是否使用 Real SDK
     */
    static isRealMode() {
        return NexusSDKConfig.getCurrentSDKType() === NexusSDKConfig.SDK_TYPE.REAL;
    }
}

/**
 * SDK 工厂类
 * 负责创建和管理 SDK 实例
 */
class NexusSDKFactory {
    /**
     * 当前 SDK 实例
     */
    static _currentSDK = null;

    /**
     * 创建 SDK 实例
     * @param {Object} [config] - 可选的配置参数
     * @returns {Object} SDK 实例
     */
    static createSDK(config = {}) {
        const sdkConfig = { ...NexusSDKConfig.getConfig(), ...config };
        const sdkType = sdkConfig.sdkType;

        if (NexusSDKConfig.SDK_TYPE.MOCK === sdkType) {
            return NexusSDKFactory._createMockSDK(sdkConfig);
        } else if (NexusSDKConfig.SDK_TYPE.REAL === sdkType) {
            return NexusSDKFactory._createRealSDK(sdkConfig);
        } else {
            console.warn(`[NexusSDKFactory] Unknown SDK type: ${sdkType}, using Mock SDK as fallback`);
            return NexusSDKFactory._createMockSDK(sdkConfig);
        }
    }

    /**
     * 创建 Mock SDK 实例
     * @param {Object} config - 配置参数
     * @returns {Object} Mock SDK 实例
     */
    static _createMockSDK(config) {
        console.log('[NexusSDKFactory] Creating Mock SDK with config:', config);
        return new NexusMockSDK({
            delay: config.mockDelay,
            errorRate: config.mockErrorRate
        });
    }

    /**
     * 创建 Real SDK 实例
     * @param {Object} config - 配置参数
     * @returns {Object} Real SDK 实例
     */
    static _createRealSDK(config) {
        console.log('[NexusSDKFactory] Creating Real SDK with config:', config);
        return new NexusRealSDK({
            baseUrl: config.baseUrl,
            timeout: config.timeout,
            onRequest: config.enableLogging ? (method, endpoint, data) => {
                console.log(`[SDK Request] ${method} ${endpoint}`, data);
            } : null,
            onResponse: config.enableLogging ? (method, endpoint, result) => {
                console.log(`[SDK Response] ${method} ${endpoint}`, result);
            } : null,
            onError: config.enableErrorHandling ? (error, response) => {
                console.error(`[SDK Error] ${error}`, response);
            } : null
        });
    }

    /**
     * 获取当前 SDK 实例
     * @returns {Object} 当前 SDK 实例
     */
    static getCurrentSDK() {
        if (!NexusSDKFactory._currentSDK) {
            NexusSDKFactory._currentSDK = NexusSDKFactory.createSDK();
        }
        return NexusSDKFactory._currentSDK;
    }

    /**
     * 重置 SDK 实例
     * 用于在配置更改后重新创建 SDK 实例
     */
    static resetSDK() {
        NexusSDKFactory._currentSDK = null;
        console.log('[NexusSDKFactory] SDK instance reset');
    }

    /**
     * 切换到 Mock SDK
     */
    static switchToMockSDK() {
        NexusSDKConfig.switchSDKType(NexusSDKConfig.SDK_TYPE.MOCK);
        NexusSDKFactory.resetSDK();
        console.log('[NexusSDKFactory] Switched to Mock SDK');
    }

    /**
     * 切换到 Real SDK
     */
    static switchToRealSDK() {
        NexusSDKConfig.switchSDKType(NexusSDKConfig.SDK_TYPE.REAL);
        NexusSDKFactory.resetSDK();
        console.log('[NexusSDKFactory] Switched to Real SDK');
    }

    /**
     * 切换 SDK 类型
     * @param {string} sdkType - SDK 类型：'mock' | 'real'
     */
    static switchSDKType(sdkType) {
        NexusSDKConfig.switchSDKType(sdkType);
        NexusSDKFactory.resetSDK();
        console.log(`[NexusSDKFactory] Switched SDK type to: ${sdkType}`);
    }

    /**
     * 检查当前是否使用 Mock SDK
     * @returns {boolean} 是否使用 Mock SDK
     */
    static isMockMode() {
        return NexusSDKConfig.isMockMode();
    }

    /**
     * 检查当前是否使用 Real SDK
     * @returns {boolean} 是否使用 Real SDK
     */
    static isRealMode() {
        return NexusSDKConfig.isRealMode();
    }

    /**
     * 获取 SDK 配置信息
     * @returns {Object} SDK 配置信息
     */
    static getSDKInfo() {
        const sdkType = NexusSDKConfig.getCurrentSDKType();
        const config = NexusSDKConfig.getConfig();
        
        return {
            sdkType: sdkType,
            sdkTypeName: sdkType === NexusSDKConfig.SDK_TYPE.MOCK ? 'Mock SDK' : 'Real SDK',
            baseUrl: config.baseUrl,
            timeout: config.timeout,
            enableLogging: config.enableLogging,
            mockDelay: config.mockDelay,
            mockErrorRate: config.mockErrorRate
        };
    }

    /**
     * 初始化 SDK
     * @param {Object} [config] - 可选的配置参数
     * @returns {Promise<void>}
     */
    static async initialize(config = {}) {
        const sdk = NexusSDKFactory.createSDK(config);
        await sdk.initialize(config);
        NexusSDKFactory._currentSDK = sdk;
        console.log('[NexusSDKFactory] SDK initialized');
    }
}

/**
 * 导出 SDK 配置和工厂
 */
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        NexusSDKConfig,
        NexusSDKFactory
    };
}

