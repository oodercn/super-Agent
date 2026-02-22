/**
 * API请求模块
 */

/**
 * 异步获取数据
 * @param {string} url 请求URL
 * @param {Object} options 请求选项
 * @returns {Promise<any>} 响应数据
 */
async function fetchData(url, options = {}) {
    try {
        if (typeof showLoading === 'function') showLoading('加载数据中...');
        
        const defaultOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        
        const response = await fetch(url, {
            ...defaultOptions,
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        if (typeof hideLoading === 'function') hideLoading();
        return data;
    } catch (error) {
        if (typeof hideLoading === 'function') hideLoading();
        if (typeof showErrorToast === 'function') showErrorToast(`数据加载失败: ${error.message}`);
        console.error('Fetch error:', error);
        throw error;
    }
}

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        fetchData
    };
}
