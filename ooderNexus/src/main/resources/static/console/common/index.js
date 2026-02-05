/**
 * 公共模块入口
 * 导出所有工具类和函数
 */

// 全局导出
if (typeof window !== 'undefined') {
    // 确保所有模块都已加载
    window.ApiClient = window.ApiClient || ApiClient;
    window.apiClient = window.apiClient || apiClient;
    window.ListManager = window.ListManager || ListManager;
    window.FormManager = window.FormManager || FormManager;
    window.utils = window.utils || utils;
}
