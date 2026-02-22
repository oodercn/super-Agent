/**
 * Nexus API 客户端 v3.0
 * 统一封装所有 AJAX 请求，遵循 ResultModel 2.0 规范
 * 
 * 功能特性：
 * 1. 支持 GET/POST/PUT/DELETE 请求
 * 2. 支持文件上传
 * 3. 支持批量操作
 * 4. 统一错误处理
 * 5. ResultModel 格式验证
 */

(function() {
    'use strict';

    const config = {
        baseURL: '',
        timeout: 30000,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    };

    const ApiClient = {
        version: '3.0.0',

        setConfig(options) {
            Object.assign(config, options);
        },

        async get(url, params = {}, options = {}) {
            const queryString = this._buildQueryString(params);
            const fullUrl = this._buildUrl(url) + (queryString ? '?' + queryString : '');
            return this._request(fullUrl, { method: 'GET', ...options });
        },

        async post(url, data = {}, options = {}) {
            const fullUrl = this._buildUrl(url);
            const requestOptions = {
                method: 'POST',
                headers: { ...config.headers, ...options.headers },
                ...options
            };
            if (data && Object.keys(data).length > 0) {
                requestOptions.body = JSON.stringify(data);
            }
            return this._request(fullUrl, requestOptions);
        },

        async put(url, data = {}, options = {}) {
            const fullUrl = this._buildUrl(url);
            return this._request(fullUrl, {
                method: 'PUT',
                headers: { ...config.headers, ...options.headers },
                body: JSON.stringify(data),
                ...options
            });
        },

        async delete(url, options = {}) {
            const fullUrl = this._buildUrl(url);
            return this._request(fullUrl, {
                method: 'DELETE',
                headers: { ...config.headers, ...options.headers },
                ...options
            });
        },

        async upload(url, file, metadata = {}, options = {}) {
            const fullUrl = this._buildUrl(url);
            const formData = new FormData();
            formData.append('file', file);
            if (Object.keys(metadata).length > 0) {
                formData.append('metadata', JSON.stringify(metadata));
            }
            return this._request(fullUrl, {
                method: 'POST',
                body: formData,
                ...options
            });
        },

        async uploadMultiple(url, files, metadata = {}, options = {}) {
            const fullUrl = this._buildUrl(url);
            const formData = new FormData();
            files.forEach((file, index) => {
                formData.append('file' + index, file);
            });
            if (Object.keys(metadata).length > 0) {
                formData.append('metadata', JSON.stringify(metadata));
            }
            return this._request(fullUrl, {
                method: 'POST',
                body: formData,
                ...options
            });
        },

        async _request(url, options) {
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), config.timeout);

            try {
                const response = await fetch(url, {
                    ...options,
                    signal: controller.signal
                });

                clearTimeout(timeoutId);

                if (!response.ok) {
                    throw new Error('HTTP ' + response.status + ': ' + response.statusText);
                }

                const result = await response.json();

                if (!this._isValidResultModel(result)) {
                    return result;
                }

                if (result.code !== 200 && result.code !== 0) {
                    throw new Error(result.message || '业务错误: ' + result.code);
                }

                return result;

            } catch (error) {
                clearTimeout(timeoutId);
                if (error.name === 'AbortError') {
                    throw new Error('请求超时，请稍后重试');
                }
                throw error;
            }
        },

        _buildUrl(url) {
            if (url.startsWith('http://') || url.startsWith('https://')) {
                return url;
            }
            if (url.startsWith('/')) {
                return url;
            }
            return config.baseURL + '/' + url;
        },

        _buildQueryString(params) {
            if (!params || Object.keys(params).length === 0) {
                return '';
            }
            return Object.entries(params)
                .map(function(entry) {
                    return encodeURIComponent(entry[0]) + '=' + encodeURIComponent(entry[1]);
                })
                .join('&');
        },

        _isValidResultModel(result) {
            return result &&
                typeof result === 'object' &&
                'code' in result &&
                'message' in result;
        },

        showLoading: function(message) {
            if (typeof NX !== 'undefined' && NX.loading) {
                NX.loading.show(message || '加载中...');
            }
        },

        hideLoading: function() {
            if (typeof NX !== 'undefined' && NX.loading) {
                NX.loading.hide();
            }
        },

        showError: function(message) {
            if (typeof NX !== 'undefined' && NX.toast) {
                NX.toast.error(message);
            } else {
                console.error('[ApiClient]', message);
            }
        },

        showSuccess: function(message) {
            if (typeof NX !== 'undefined' && NX.toast) {
                NX.toast.success(message);
            }
        }
    };

    if (typeof window !== 'undefined') {
        window.ApiClient = ApiClient;
        window.apiClient = ApiClient;
    }

})();
