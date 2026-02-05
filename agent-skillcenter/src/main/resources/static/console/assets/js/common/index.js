/**
 * 公共组件入口文件
 */

// 导入API客户端
import { ApiClient, apiClient, API_BASE_URL } from './api.js';

// 导入列表组件
import { ListComponent } from './list.js';

// 导入表单组件
import { FormComponent } from './form.js';

// 导入模板管理模块
import { initTemplateSystem, createTemplate, renderTemplate, destroyTemplate, getPredefinedTemplate, renderWithTemplate, cacheTemplate, getCachedTemplate, clearTemplateCache, predefinedTemplates } from './template.js';

// 导出所有组件
export {
    ApiClient,
    apiClient,
    API_BASE_URL,
    ListComponent,
    FormComponent,
    initTemplateSystem,
    createTemplate,
    renderTemplate,
    destroyTemplate,
    getPredefinedTemplate,
    renderWithTemplate,
    cacheTemplate,
    getCachedTemplate,
    clearTemplateCache,
    predefinedTemplates
};

// 导出默认对象
export default {
    ApiClient,
    apiClient,
    API_BASE_URL,
    ListComponent,
    FormComponent,
    initTemplateSystem,
    createTemplate,
    renderTemplate,
    destroyTemplate,
    getPredefinedTemplate,
    renderWithTemplate,
    cacheTemplate,
    getCachedTemplate,
    clearTemplateCache,
    predefinedTemplates
};
