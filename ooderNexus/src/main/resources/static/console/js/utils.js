/**
 * 工具函数模块
 */

/**
 * 格式化字节数
 * @param {number} bytes 字节数
 * @returns {string} 格式化后的字符串
 */
function formatBytes(bytes) {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

/**
 * 格式化时间
 * @param {number} timestamp 时间戳
 * @returns {string} 格式化后的时间字符串
 */
function formatTime(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString('zh-CN');
}

/**
 * 计算相对时间
 * @param {number} timestamp 时间戳
 * @returns {string} 相对时间字符串
 */
function getRelativeTime(timestamp) {
    const now = Date.now();
    const diff = Math.floor((now - timestamp) / 1000);
    
    if (diff < 60) {
        return `${diff}s ago`;
    } else if (diff < 3600) {
        return `${Math.floor(diff / 60)}m ago`;
    } else if (diff < 86400) {
        return `${Math.floor(diff / 3600)}h ago`;
    } else {
        return `${Math.floor(diff / 86400)}d ago`;
    }
}

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        formatBytes,
        formatTime,
        getRelativeTime
    };
}
