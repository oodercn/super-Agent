/**
 * 统一的AJAX请求封装类
 * 提供统一的API请求方法，支持各种类型的请求和错误处理
 */
class ApiClient {
    constructor(baseURL = '/api') {
        this.baseURL = baseURL;
        this.defaultHeaders = {
            'Accept': 'application/json'
        };
        this.timeout = 30000; // 30秒超时
    }

    /**
     * 构建完整的URL
     * @param {string} endpoint - API端点
     * @returns {string} 完整的URL
     */
    buildUrl(endpoint) {
        if (!endpoint) return this.baseURL;
        
        // 如果是完整URL，直接返回
        if (endpoint.startsWith('http://') || endpoint.startsWith('https://')) {
            return endpoint;
        }
        
        // 如果已经是从根路径开始的相对路径，直接返回
        if (endpoint.startsWith('/')) {
            return this.baseURL + endpoint;
        }
        
        // 构建从根路径开始的完整路径
        return this.baseURL + '/' + endpoint;
    }

    /**
     * 构建请求配置
     * @param {string} method - 请求方法
     * @param {Object} options - 选项
     * @returns {Object} 请求配置
     */
    buildConfig(method, options = {}) {
        const config = {
            method: method,
            headers: {
                ...this.defaultHeaders,
                ...options.headers
            },
            ...options
        };

        // 只有当body不是FormData时才设置Content-Type为application/json
        if (!(options.body instanceof FormData)) {
            config.headers['Content-Type'] = 'application/json';
        } else {
            // 对于FormData，不设置Content-Type，让浏览器自动设置
            delete config.headers['Content-Type'];
        }

        // 序列化非FormData的请求体
        if (config.method !== 'GET' && options.body && !(options.body instanceof FormData)) {
            config.body = JSON.stringify(options.body);
        }

        return config;
    }

    /**
     * 处理响应
     * @param {Response} response - fetch响应
     * @returns {Promise<Object>} 处理后的响应数据
     */
    async handleResponse(response) {
        const contentType = response.headers.get('content-type');
        let data;
        
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            const text = await response.text();
            data = {
                code: 500,
                message: `非JSON响应: ${text.substring(0, 100)}...`,
                data: null
            };
        }
        
        if (!response.ok) {
            throw new Error(data.message || '请求失败');
        }
        
        return data;
    }

    /**
     * 通用请求方法
     * @param {string} endpoint - API端点
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async request(endpoint, options = {}) {
        const url = this.buildUrl(endpoint);
        const config = this.buildConfig(options.method || 'GET', options);
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), this.timeout);

        try {
            // 添加信号到配置
            config.signal = controller.signal;
            
            const response = await fetch(url, config);
            clearTimeout(timeoutId);
            
            const data = await this.handleResponse(response);
            return data;
        } catch (error) {
            clearTimeout(timeoutId);
            console.error('API 调用错误:', error);
            return {
                code: 500,
                message: error.message,
                data: null
            };
        }
    }

    /**
     * GET请求
     * @param {string} endpoint - API端点
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async get(endpoint, options = {}) {
        return await this.request(endpoint, {
            ...options,
            method: 'GET'
        });
    }

    /**
     * POST请求
     * @param {string} endpoint - API端点
     * @param {Object|FormData} body - 请求体
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async post(endpoint, body, options = {}) {
        return await this.request(endpoint, {
            ...options,
            method: 'POST',
            body: body
        });
    }

    /**
     * PUT请求
     * @param {string} endpoint - API端点
     * @param {Object|FormData} body - 请求体
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async put(endpoint, body, options = {}) {
        return await this.request(endpoint, {
            ...options,
            method: 'PUT',
            body: body
        });
    }

    /**
     * DELETE请求
     * @param {string} endpoint - API端点
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async delete(endpoint, options = {}) {
        return await this.request(endpoint, {
            ...options,
            method: 'DELETE'
        });
    }

    /**
     * 上传文件
     * @param {string} endpoint - API端点
     * @param {File|Blob} file - 文件
     * @param {Object} metadata - 元数据
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async uploadFile(endpoint, file, metadata = {}, options = {}) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('metadata', JSON.stringify(metadata));
        
        return await this.post(endpoint, formData, options);
    }

    /**
     * 批量上传文件
     * @param {string} endpoint - API端点
     * @param {Array<File|Blob>} files - 文件数组
     * @param {Object} metadata - 元数据
     * @param {Object} options - 选项
     * @returns {Promise<Object>} 响应数据
     */
    async uploadFiles(endpoint, files, metadata = {}, options = {}) {
        const formData = new FormData();
        
        // 添加多个文件
        files.forEach((file, index) => {
            formData.append(`file${index}`, file);
        });
        
        formData.append('metadata', JSON.stringify(metadata));
        
        return await this.post(endpoint, formData, options);
    }

    /**
     * 设置默认请求头
     * @param {Object} headers - 请求头
     */
    setDefaultHeaders(headers) {
        this.defaultHeaders = {
            ...this.defaultHeaders,
            ...headers
        };
    }

    /**
     * 添加请求头
     * @param {string} key - 键
     * @param {string} value - 值
     */
    addHeader(key, value) {
        this.defaultHeaders[key] = value;
    }

    /**
     * 移除请求头
     * @param {string} key - 键
     */
    removeHeader(key) {
        delete this.defaultHeaders[key];
    }

    /**
     * 清除所有请求头
     */
    clearHeaders() {
        this.defaultHeaders = {
            'Accept': 'application/json'
        };
    }

    /**
     * 批量获取数据
     * @param {Object} params - 请求参数
     * @returns {Promise<Object>} - 响应数据
     */
    async batchFetch(params) {
        return this.post('/batch/fetch', params);
    }

    /**
     * 批量创建数据
     * @param {Object} params - 请求参数
     * @returns {Promise<Object>} - 响应数据
     */
    async batchCreate(params) {
        return this.post('/batch/create', params);
    }

    /**
     * 批量更新数据
     * @param {Object} params - 请求参数
     * @returns {Promise<Object>} - 响应数据
     */
    async batchUpdate(params) {
        return this.post('/batch/update', params);
    }

    /**
     * 批量删除数据
     * @param {Object} params - 请求参数
     * @returns {Promise<Object>} - 响应数据
     */
    async batchDelete(params) {
        return this.post('/batch/delete', params);
    }

    /**
     * 批量执行操作
     * @param {Object} params - 请求参数
     * @returns {Promise<Object>} - 响应数据
     */
    async batchExecute(params) {
        return this.post('/batch/execute', params);
    }
}

// 导出ApiClient类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ApiClient;
} else {
    window.ApiClient = ApiClient;
}

// 导出单例实例
const apiClient = new ApiClient();

// 全局导出
if (typeof window !== 'undefined') {
    window.apiClient = apiClient;
}
