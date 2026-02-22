/**
 * Nexus SDK 统一入口文件
 * 
 * 本文件是 Nexus SDK 的统一入口，导出所有 SDK 相关的模块。
 * 
 * 使用方式：
 * 1. 在 HTML 中引入本文件：
 *    <script src="../../js/nexus/sdk/NexusSDK.js"></script>
 * 2. 在页面中使用 SDK：
 *    const sdk = NexusSDKFactory.createSDK();
 *    const result = await sdk.getDashboardData();
 * 
 * 配置说明：
 * - SDK 实现方式通过配置文件或环境变量控制
 * - 默认使用 Mock SDK，方便开发和测试
 * - 生产环境建议使用 Real SDK，确保数据准确性
 * - 支持动态切换 SDK 实现方式，无需重启应用
 * 
 * SDK 切换方式：
 * 1. 使用配置类切换：
 *    NexusSDKConfig.switchSDKType('real');
 *    NexusSDKFactory.resetSDK();
 * 2. 使用工厂类切换：
 *    NexusSDKFactory.switchToRealSDK();
 * 
 * @module NexusSDK
 * @version 1.0.0
 */

/**
 * 导出 SDK 配置和工厂
 * 供外部使用
 */
const NexusSDKConfig = window.NexusSDKConfig || class NexusSDKConfig {
    static SDK_TYPE = {
        MOCK: 'mock',
        REAL: 'real'
    };

    static DEFAULT_CONFIG = {
        sdkType: NexusSDKConfig.SDK_TYPE.MOCK,
        baseUrl: '/api',
        timeout: 30000,
        enableLogging: true,
        enableErrorHandling: true,
        mockDelay: 500,
        mockErrorRate: 0
    };

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

    static saveConfig(config) {
        try {
            localStorage.setItem('nexusSDKConfig', JSON.stringify(config));
            console.log('[NexusSDKConfig] Config saved:', config);
        } catch (error) {
            console.error('[NexusSDKConfig] Failed to save config:', error);
        }
    }

    static resetConfig() {
        NexusSDKConfig.saveConfig(NexusSDKConfig.DEFAULT_CONFIG);
        console.log('[NexusSDKConfig] Config reset to default');
    }

    static switchSDKType(sdkType) {
        const config = NexusSDKConfig.getConfig();
        config.sdkType = sdkType;
        NexusSDKConfig.saveConfig(config);
        console.log(`[NexusSDKConfig] SDK type switched to: ${sdkType}`);
    }

    static setBaseUrl(baseUrl) {
        const config = NexusSDKConfig.getConfig();
        config.baseUrl = baseUrl;
        NexusSDKConfig.saveConfig(config);
    }

    static setTimeout(timeout) {
        const config = NexusSDKConfig.getConfig();
        config.timeout = timeout;
        NexusSDKConfig.saveConfig(config);
    }

    static setLoggingEnabled(enabled) {
        const config = NexusSDKConfig.getConfig();
        config.enableLogging = enabled;
        NexusSDKConfig.saveConfig(config);
    }

    static setMockDelay(delay) {
        const config = NexusSDKConfig.getConfig();
        config.mockDelay = delay;
        NexusSDKConfig.saveConfig(config);
    }

    static setMockErrorRate(rate) {
        const config = NexusSDKConfig.getConfig();
        config.mockErrorRate = rate;
        NexusSDKConfig.saveConfig(config);
    }

    static getCurrentSDKType() {
        const config = NexusSDKConfig.getConfig();
        return config.sdkType;
    }

    static isMockMode() {
        return NexusSDKConfig.getCurrentSDKType() === NexusSDKConfig.SDK_TYPE.MOCK;
    }

    static isRealMode() {
        return NexusSDKConfig.getCurrentSDKType() === NexusSDKConfig.SDK_TYPE.REAL;
    }
};

const NexusSDKFactory = window.NexusSDKFactory || class NexusSDKFactory {
    static _currentSDK = null;

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

    static _createMockSDK(config) {
        console.log('[NexusSDKFactory] Creating Mock SDK with config:', config);
        return new NexusMockSDK({
            delay: config.mockDelay,
            errorRate: config.mockErrorRate
        });
    }

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

    static getCurrentSDK() {
        if (!NexusSDKFactory._currentSDK) {
            NexusSDKFactory._currentSDK = NexusSDKFactory.createSDK();
        }
        return NexusSDKFactory._currentSDK;
    }

    static resetSDK() {
        NexusSDKFactory._currentSDK = null;
        console.log('[NexusSDKFactory] SDK instance reset');
    }

    static switchToMockSDK() {
        NexusSDKConfig.switchSDKType(NexusSDKConfig.SDK_TYPE.MOCK);
        NexusSDKFactory.resetSDK();
        console.log('[NexusSDKFactory] Switched to Mock SDK');
    }

    static switchToRealSDK() {
        NexusSDKConfig.switchSDKType(NexusSDKConfig.SDK_TYPE.REAL);
        NexusSDKFactory.resetSDK();
        console.log('[NexusSDKFactory] Switched to Real SDK');
    }

    static switchSDKType(sdkType) {
        NexusSDKConfig.switchSDKType(sdkType);
        NexusSDKFactory.resetSDK();
        console.log(`[NexusSDKFactory] Switched SDK type to: ${sdkType}`);
    }

    static isMockMode() {
        return NexusSDKConfig.isMockMode();
    }

    static isRealMode() {
        return NexusSDKConfig.isRealMode();
    }

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

    static async initialize(config = {}) {
        const sdk = NexusSDKFactory.createSDK(config);
        await sdk.initialize(config);
        NexusSDKFactory._currentSDK = sdk;
        console.log('[NexusSDKFactory] SDK initialized');
    }
};

/**
 * 导出 SDK 模块
 */
window.NexusSDKConfig = NexusSDKConfig;
window.NexusSDKFactory = NexusSDKFactory;

/**
 * 创建全局 SDK 实例
 * 供页面直接使用
 */
window.nexusSDK = NexusSDKFactory.getCurrentSDK();

/**
 * 导出 SDK 信息
 */
window.NexusSDKInfo = {
    version: '1.0.0',
    name: 'Nexus SDK',
    description: 'Nexus SDK for ooderAgent 0.6.6',
    config: NexusSDKFactory.getSDKInfo()
};

console.log('[NexusSDK] SDK loaded:', window.NexusSDKInfo);

