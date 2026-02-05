/**
 * 通用工具函数
 */

/**
 * 从URL中获取当前页面标识
 * @returns {string} 当前页面标识
 */
export function getCurrentPage() {
    const path = window.location.pathname;
    let activeSection = 'dashboard';

    if (path.includes('my-skill')) {
        activeSection = 'my-skills';
    } else if (path.includes('my-execution')) {
        activeSection = 'my-execution';
    } else if (path.includes('my-sharing')) {
        activeSection = 'my-sharing';
    } else if (path.includes('my-group')) {
        activeSection = 'my-groups';
    } else if (path.includes('my-identity')) {
        activeSection = 'my-identity';
    } else if (path.includes('my-help')) {
        activeSection = 'my-help';
    } else if (path.includes('admin/dashboard')) {
        activeSection = 'admin-dashboard';
    } else if (path.includes('admin/skill-management')) {
        activeSection = 'skill-management';
    } else if (path.includes('admin/market-management')) {
        activeSection = 'market-management';
    } else if (path.includes('admin/skill-authentication')) {
        activeSection = 'skill-authentication';
    } else if (path.includes('admin/group-management')) {
        activeSection = 'group-management';
    } else if (path.includes('admin/remote-hosting')) {
        activeSection = 'remote-hosting';
    } else if (path.includes('admin/storage-management')) {
        activeSection = 'storage-management';
    } else if (path.includes('admin/system-management')) {
        activeSection = 'system-management';
    }

    return activeSection;
}

/**
 * 异步获取JSON数据
 * @param {string} url - 请求URL
 * @returns {Promise<any>} 返回数据
 */
export async function fetchJson(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
}

/**
 * 生成随机ID
 * @returns {string} 随机ID
 */
export function generateId() {
    return Math.random().toString(36).substr(2, 9);
}

/**
 * 格式化日期
 * @param {Date} date - 日期对象
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date) {
    return new Date(date).toLocaleString();
}

/**
 * 防抖函数
 * @param {Function} func - 要执行的函数
 * @param {number} wait - 等待时间(ms)
 * @returns {Function} 防抖后的函数
 */
export function debounce(func, wait) {
    let timeout;
    return function() {
        const context = this;
        const args = arguments;
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(context, args), wait);
    };
}

/**
 * 节流函数
 * @param {Function} func - 要执行的函数
 * @param {number} limit - 时间限制(ms)
 * @returns {Function} 节流后的函数
 */
export function throttle(func, limit) {
    let inThrottle;
    return function() {
        const context = this;
        const args = arguments;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

/**
 * 检查是否为空值
 * @param {*} value - 要检查的值
 * @returns {boolean} 是否为空
 */
export function isEmpty(value) {
    return value === undefined || value === null || value === '' || 
           (Array.isArray(value) && value.length === 0) ||
           (typeof value === 'object' && Object.keys(value).length === 0);
}

/**
 * 深拷贝对象
 * @param {*} obj - 要拷贝的对象
 * @returns {*} 拷贝后的对象
 */
export function deepClone(obj) {
    if (obj === null || typeof obj !== 'object') return obj;
    if (obj instanceof Date) return new Date(obj.getTime());
    if (obj instanceof Array) return obj.map(item => deepClone(item));
    if (typeof obj === 'object') {
        const clonedObj = {};
        for (const key in obj) {
            if (obj.hasOwnProperty(key)) {
                clonedObj[key] = deepClone(obj[key]);
            }
        }
        return clonedObj;
    }
}

/**
 * 显示通知消息
 * @param {string} message - 消息内容
 * @param {string} type - 消息类型(success, error, warning, info)
 * @param {number} duration - 显示时长(ms)
 */
export function showNotification(message, type = 'info', duration = 3000) {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.padding = '12px 20px';
    notification.style.borderRadius = '4px';
    notification.style.color = '#fff';
    notification.style.fontWeight = '500';
    notification.style.zIndex = '10000';
    notification.style.transition = 'all 0.3s ease';
    notification.style.opacity = '0';
    notification.style.transform = 'translateX(100%)';

    // 设置不同类型的颜色
    const colors = {
        success: '#4CAF50',
        error: '#F44336',
        warning: '#FFC107',
        info: '#2196F3'
    };

    notification.style.backgroundColor = colors[type] || colors.info;

    document.body.appendChild(notification);

    // 显示通知
    setTimeout(() => {
        notification.style.opacity = '1';
        notification.style.transform = 'translateX(0)';
    }, 100);

    // 隐藏通知
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, duration);
}
