/**
 * Nexus API模块
 * 包含所有API请求相关功能
 * @module nexusApi
 */

(function() {
    class NexusApi {
        constructor(base) {
            this.base = base;
        }

        /**
         * 统一的AJAX请求方法
         * @param {string} endpoint - API端点
         * @param {Object} options - 请求选项
         * @returns {Promise<Object>} - 响应数据
         */
        async ajax(endpoint, options = {}) {
            const url = `${this.base.apiBaseUrl}${endpoint}`;
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), this.base.ajaxTimeout);

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
                return this.validateApiResponse(data);
            } catch (error) {
                clearTimeout(timeoutId);
                console.error('API request error:', error);
                throw error;
            }
        }

        /**
         * 验证API返回格式
         * @param {Object} response - API响应数据
         * @returns {Object} - 验证后的响应数据
         * @throws {Error} - 格式验证失败时抛出错误
         */
        validateApiResponse(response) {
            if (!response) {
                throw new Error('Invalid API response: response is null or undefined');
            }

            if (typeof response !== 'object') {
                throw new Error('Invalid API response: response is not an object');
            }

            if (!response.hasOwnProperty('status')) {
                throw new Error('Invalid API response: missing status field');
            }

            if (!response.hasOwnProperty('message')) {
                throw new Error('Invalid API response: missing message field');
            }

            if (response.status === 'success' && !response.hasOwnProperty('data')) {
                console.warn('API response warning: missing data field in success response');
            }

            if (!response.hasOwnProperty('timestamp')) {
                console.warn('API response warning: missing timestamp field');
            }

            return response;
        }

        /**
         * GET请求
         * @param {string} endpoint - API端点
         * @param {Object} params - 查询参数
         * @returns {Promise<Object>} - 响应数据
         */
        async get(endpoint, params = {}) {
            const queryString = new URLSearchParams(params).toString();
            const url = queryString ? `${endpoint}?${queryString}` : endpoint;

            return this.ajax(url, {
                method: 'GET',
            });
        }

        /**
         * POST请求
         * @param {string} endpoint - API端点
         * @param {Object} data - 请求数据
         * @returns {Promise<Object>} - 响应数据
         */
        async post(endpoint, data = {}) {
            return this.ajax(endpoint, {
                method: 'POST',
                body: JSON.stringify(data),
            });
        }

        /**
         * PUT请求
         * @param {string} endpoint - API端点
         * @param {Object} data - 请求数据
         * @returns {Promise<Object>} - 响应数据
         */
        async put(endpoint, data = {}) {
            return this.ajax(endpoint, {
                method: 'PUT',
                body: JSON.stringify(data),
            });
        }

        /**
         * DELETE请求
         * @param {string} endpoint - API端点
         * @returns {Promise<Object>} - 响应数据
         */
        async delete(endpoint) {
            return this.ajax(endpoint, {
                method: 'DELETE',
            });
        }

        /**
         * 处理API错误
         * @param {Error} error - 错误对象
         * @param {string} defaultMessage - 默认错误消息
         */
        handleApiError(error, defaultMessage = '操作失败') {
            console.error('API Error:', error);
            let errorMessage = defaultMessage;

            if (error.message) {
                errorMessage = error.message;
            }

            if (this.base.showMessage) {
                this.base.showMessage(errorMessage, 'error');
            }
        }

        /**
         * 处理API成功响应
         * @param {Object} response - 响应对象
         * @param {string} successMessage - 成功消息
         */
        handleApiSuccess(response, successMessage = '操作成功') {
            if (this.base.showMessage) {
                if (response && response.message) {
                    this.base.showMessage(response.message, 'success');
                } else {
                    this.base.showMessage(successMessage, 'success');
                }
            }
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusApi;
    }

    if (typeof window !== 'undefined') {
        window.NexusApi = NexusApi;
    }
})();

