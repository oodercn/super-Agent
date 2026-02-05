/**
 * API工具类 - 封装API调用功能
 */

class ApiClient {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 发送GET请求
     * @param {string} endpoint - API端点
     * @param {Object} params - 查询参数
     * @returns {Promise<any>} - 返回Promise对象
     */
    async get(endpoint, params = {}) {
        const url = this.buildUrl(endpoint, params);
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return this.handleResponse(response);
        } catch (error) {
            throw this.handleError(error);
        }
    }

    /**
     * 发送POST请求
     * @param {string} endpoint - API端点
     * @param {Object} data - 请求数据
     * @returns {Promise<any>} - 返回Promise对象
     */
    async post(endpoint, data = {}) {
        const url = this.buildUrl(endpoint);
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            return this.handleResponse(response);
        } catch (error) {
            throw this.handleError(error);
        }
    }

    /**
     * 发送PUT请求
     * @param {string} endpoint - API端点
     * @param {Object} data - 请求数据
     * @returns {Promise<any>} - 返回Promise对象
     */
    async put(endpoint, data = {}) {
        const url = this.buildUrl(endpoint);
        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            return this.handleResponse(response);
        } catch (error) {
            throw this.handleError(error);
        }
    }

    /**
     * 发送DELETE请求
     * @param {string} endpoint - API端点
     * @returns {Promise<any>} - 返回Promise对象
     */
    async delete(endpoint) {
        const url = this.buildUrl(endpoint);
        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return this.handleResponse(response);
        } catch (error) {
            throw this.handleError(error);
        }
    }

    /**
     * 构建完整的URL
     * @param {string} endpoint - API端点
     * @param {Object} params - 查询参数
     * @returns {string} - 完整的URL
     */
    buildUrl(endpoint, params = {}) {
        let url = `${this.baseUrl}${endpoint}`;
        const queryParams = new URLSearchParams();
        
        for (const [key, value] of Object.entries(params)) {
            if (value !== undefined && value !== null) {
                queryParams.append(key, value);
            }
        }
        
        const queryString = queryParams.toString();
        if (queryString) {
            url += `?${queryString}`;
        }
        
        return url;
    }

    /**
     * 处理响应
     * @param {Response} response - 响应对象
     * @returns {Promise<any>} - 返回处理后的响应数据
     */
    async handleResponse(response) {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        try {
            return await response.json();
        } catch (error) {
            // 处理非JSON响应
            return await response.text();
        }
    }

    /**
     * 处理错误
     * @param {Error} error - 错误对象
     * @returns {Error} - 处理后的错误对象
     */
    handleError(error) {
        console.error('API error:', error);
        return error;
    }
}

// 创建默认的API客户端实例
const API_BASE_URL = '/skillcenter/api';
const apiClient = new ApiClient(API_BASE_URL);

// 导出API客户端
export { ApiClient, apiClient, API_BASE_URL };
