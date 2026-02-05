/**
 * 全局变量和初始化模块
 */

// 全局变量
const COMMON = {
    // API基础URL
    API_BASE_URL: '/api/mcp',
    // 当前页面
    currentPage: 'dashboard',
    // 菜单配置
    menuConfig: null,
    // 当前用户场景
    currentScenario: null
};

// 页面加载完成后初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initCommon);
} else {
    initCommon();
}

/**
 * 初始化通用功能
 */
function initCommon() {
    if (typeof updateTimestamp === 'function') updateTimestamp();
    if (typeof loadMenuConfig === 'function') loadMenuConfig();
    if (typeof setupEventListeners === 'function') setupEventListeners();
    // 每分钟更新时间戳
    setInterval(() => {
        if (typeof updateTimestamp === 'function') updateTimestamp();
    }, 60000);
}

/**
 * 设置全局事件监听器
 */
function setupEventListeners() {
    // 禁止鼠标右键菜单（可选）
    document.addEventListener('contextmenu', function(e) {
        e.preventDefault();
    });
    
    // 禁止文本选择（可选）
    document.addEventListener('selectstart', function(e) {
        e.preventDefault();
    });
}

/**
 * 更新时间戳
 */
function updateTimestamp() {
    const timestampElement = document.getElementById('timestamp');
    if (timestampElement) {
        const now = new Date();
        timestampElement.textContent = now.toLocaleString('zh-CN');
    }
}

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        COMMON,
        initCommon,
        setupEventListeners,
        updateTimestamp
    };
}
