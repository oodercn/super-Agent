/**
 * 公共模块入口
 * 导出所有工具类和函数
 */

// 导入模块
import { ApiClient, apiClient } from './api-client.js';
import ListManager from './list-manager.js';
import FormManager from './form-manager.js';
import utils from './utils.js';

// 导出模块
export {
    ApiClient,
    apiClient,
    ListManager,
    FormManager,
    utils
};

// 全局导出
if (typeof window !== 'undefined') {
    window.ApiClient = ApiClient;
    window.apiClient = apiClient;
    window.ListManager = ListManager;
    window.FormManager = FormManager;
    window.utils = utils;
}
