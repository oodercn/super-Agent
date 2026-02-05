/**
 * 通用JavaScript功能模块
 * 作为入口文件，引入所有拆分出的模块
 */

// 引入拆分出的模块
// 注意：在浏览器环境中，我们使用脚本标签顺序加载，而不是使用require
// 这里只是为了保持模块化结构和向后兼容性

// 全局变量和初始化模块
// 已拆分到 globals.js

// 菜单管理模块
// 已拆分到 menu.js

// UI交互模块
// 已拆分到 ui.js

// 工具函数模块
// 已拆分到 utils.js

// API请求模块
// 已拆分到 api.js

// 场景管理模块
// 已拆分到 scenario.js

/**
 * 导出通用模块（保持向后兼容性）
 */
if (typeof module !== 'undefined' && module.exports) {
    // 在CommonJS环境中，我们可以使用require引入模块
    const globals = require('./globals');
    const menu = require('./menu');
    const ui = require('./ui');
    const utils = require('./utils');
    const api = require('./api');
    const scenario = require('./scenario');
    
    module.exports = {
        // 全局变量
        COMMON: globals.COMMON,
        
        // 初始化和全局功能
        initCommon: globals.initCommon,
        setupEventListeners: globals.setupEventListeners,
        updateTimestamp: globals.updateTimestamp,
        
        // 菜单管理
        loadMenuConfig: menu.loadMenuConfig,
        renderMenu: menu.renderMenu,
        createMenuItem: menu.createMenuItem,
        renderDefaultMenu: menu.renderDefaultMenu,
        setupNavigation: menu.setupNavigation,
        
        // UI交互
        showLoading: ui.showLoading,
        hideLoading: ui.hideLoading,
        showErrorToast: ui.showErrorToast,
        hideErrorToast: ui.hideErrorToast,
        showSuccessToast: ui.showSuccessToast,
        hideSuccessToast: ui.hideSuccessToast,
        showToast: ui.showToast,
        
        // 工具函数
        formatBytes: utils.formatBytes,
        formatTime: utils.formatTime,
        getRelativeTime: utils.getRelativeTime,
        
        // API请求
        fetchData: api.fetchData,
        
        // 场景管理
        enterScenario: scenario.enterScenario,
        loadScenarioFeatures: scenario.loadScenarioFeatures
    };
}
