/**
 * API客户端
 * 统一处理与后端API的通信
 */
function ApiClient(baseUrl = '/api') {
    this.baseUrl = baseUrl;
    this.timeout = 30000; // 30秒超时
}

/**
 * 通用请求方法
 * @param {string} endpoint - API端点
 * @param {Object} options - 请求选项
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.request = async function(endpoint, options = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), this.timeout);

    try {
        const response = await fetch(url, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
            signal: controller.signal,
        });

        clearTimeout(timeoutId);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        clearTimeout(timeoutId);
        console.error('API request error:', error);
        throw error;
    }
};

/**
 * GET请求
 * @param {string} endpoint - API端点
 * @param {Object} params - 查询参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.get = async function(endpoint, params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const url = queryString ? `${endpoint}?${queryString}` : endpoint;

    return this.request(url, {
        method: 'GET',
    });
};

/**
 * POST请求
 * @param {string} endpoint - API端点
 * @param {Object} data - 请求数据
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.post = async function(endpoint, data = {}) {
    return this.request(endpoint, {
        method: 'POST',
        body: JSON.stringify(data),
    });
};

/**
 * PUT请求
 * @param {string} endpoint - API端点
 * @param {Object} data - 请求数据
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.put = async function(endpoint, data = {}) {
    return this.request(endpoint, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
};

/**
 * DELETE请求
 * @param {string} endpoint - API端点
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.delete = async function(endpoint) {
    return this.request(endpoint, {
        method: 'DELETE',
    });
};

/**
 * 批量获取数据
 * @param {Object} params - 请求参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.batchFetch = async function(params) {
    return this.post('/batch/fetch', params);
};

/**
 * 批量创建数据
 * @param {Object} params - 请求参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.batchCreate = async function(params) {
    return this.post('/batch/create', params);
};

/**
 * 批量更新数据
 * @param {Object} params - 请求参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.batchUpdate = async function(params) {
    return this.post('/batch/update', params);
};

/**
 * 批量删除数据
 * @param {Object} params - 请求参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.batchDelete = async function(params) {
    return this.post('/batch/delete', params);
};

/**
 * 批量执行操作
 * @param {Object} params - 请求参数
 * @returns {Promise<any>} - 响应数据
 */
ApiClient.prototype.batchExecute = async function(params) {
    return this.post('/batch/execute', params);
};

// 导出单例实例
const apiClient = new ApiClient();

// 全局导出
if (typeof window !== 'undefined') {
    window.ApiClient = ApiClient;
    window.apiClient = apiClient;
}
