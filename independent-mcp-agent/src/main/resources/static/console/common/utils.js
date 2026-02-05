/**
 * 工具函数
 * 通用的工具函数集合
 */
const utils = {
    /**
     * 格式化时间戳
     * @param {number} timestamp - 时间戳
     * @param {string} format - 格式
     * @returns {string} - 格式化后的时间
     */
    formatTimestamp(timestamp, format = 'YYYY-MM-DD HH:mm:ss') {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return format
            .replace('YYYY', year)
            .replace('MM', month)
            .replace('DD', day)
            .replace('HH', hours)
            .replace('mm', minutes)
            .replace('ss', seconds);
    },

    /**
     * 深拷贝对象
     * @param {Object} obj - 要拷贝的对象
     * @returns {Object} - 拷贝后的对象
     */
    deepClone(obj) {
        if (obj === null || typeof obj !== 'object') {
            return obj;
        }
        if (obj instanceof Date) {
            return new Date(obj.getTime());
        }
        if (obj instanceof Array) {
            return obj.map(item => this.deepClone(item));
        }
        if (typeof obj === 'object') {
            const clonedObj = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    clonedObj[key] = this.deepClone(obj[key]);
                }
            }
            return clonedObj;
        }
    },

    /**
     * 防抖函数
     * @param {Function} func - 要执行的函数
     * @param {number} wait - 等待时间
     * @returns {Function} - 防抖后的函数
     */
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    /**
     * 节流函数
     * @param {Function} func - 要执行的函数
     * @param {number} limit - 限制时间
     * @returns {Function} - 节流后的函数
     */
    throttle(func, limit) {
        let inThrottle;
        return function executedFunction(...args) {
            if (!inThrottle) {
                func.apply(this, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    },

    /**
     * 生成UUID
     * @returns {string} - UUID
     */
    generateUUID() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            const r = Math.random() * 16 | 0;
            const v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    },

    /**
     * 格式化字节大小
     * @param {number} bytes - 字节数
     * @param {number} decimals - 小数位数
     * @returns {string} - 格式化后的大小
     */
    formatBytes(bytes, decimals = 2) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const dm = decimals < 0 ? 0 : decimals;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    },

    /**
     * 格式化数字
     * @param {number} num - 数字
     * @param {number} decimals - 小数位数
     * @param {string} thousandsSeparator - 千位分隔符
     * @returns {string} - 格式化后的数字
     */
    formatNumber(num, decimals = 0, thousandsSeparator = ',') {
        const n = Math.abs(num);
        const prec = !isFinite(+decimals) ? 0 : Math.min(decimals, 20);
        const s = (prec ? n.toFixed(prec) : Math.round(n)).toString();
        const sep = thousandsSeparator || ',';
        const parts = s.split('.');
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, sep);
        return (num < 0 ? '-' : '') + parts.join('.');
    },

    /**
     * 验证邮箱
     * @param {string} email - 邮箱地址
     * @returns {boolean} - 是否有效
     */
    validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    },

    /**
     * 验证URL
     * @param {string} url - URL地址
     * @returns {boolean} - 是否有效
     */
    validateURL(url) {
        try {
            new URL(url);
            return true;
        } catch (error) {
            return false;
        }
    },

    /**
     * 截断字符串
     * @param {string} str - 字符串
     * @param {number} length - 长度
     * @param {string} suffix - 后缀
     * @returns {string} - 截断后的字符串
     */
    truncateString(str, length, suffix = '...') {
        if (str.length <= length) {
            return str;
        }
        return str.substring(0, length - suffix.length) + suffix;
    },

    /**
     * 数组去重
     * @param {Array} arr - 数组
     * @param {string} key - 去重的键
     * @returns {Array} - 去重后的数组
     */
    uniqueArray(arr, key = null) {
        if (key) {
            return arr.filter((item, index, self) =>
                index === self.findIndex(t => t[key] === item[key])
            );
        }
        return [...new Set(arr)];
    },

    /**
     * 数组排序
     * @param {Array} arr - 数组
     * @param {string} key - 排序的键
     * @param {string} order - 排序顺序
     * @returns {Array} - 排序后的数组
     */
    sortArray(arr, key, order = 'asc') {
        return arr.sort((a, b) => {
            const aVal = a[key];
            const bVal = b[key];
            if (aVal < bVal) {
                return order === 'asc' ? -1 : 1;
            }
            if (aVal > bVal) {
                return order === 'asc' ? 1 : -1;
            }
            return 0;
        });
    },

    /**
     * 数组分页
     * @param {Array} arr - 数组
     * @param {number} page - 页码
     * @param {number} pageSize - 每页大小
     * @returns {Array} - 分页后的数组
     */
    paginateArray(arr, page, pageSize) {
        const startIndex = (page - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        return arr.slice(startIndex, endIndex);
    },

    /**
     * 数组过滤
     * @param {Array} arr - 数组
     * @param {Object} filters - 过滤条件
     * @returns {Array} - 过滤后的数组
     */
    filterArray(arr, filters) {
        return arr.filter(item => {
            for (const key in filters) {
                if (filters[key] !== undefined && filters[key] !== null && filters[key] !== '') {
                    if (typeof item[key] === 'string') {
                        if (!item[key].toLowerCase().includes(filters[key].toLowerCase())) {
                            return false;
                        }
                    } else {
                        if (item[key] !== filters[key]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        });
    },

    /**
     * 显示通知
     * @param {string} message - 消息
     * @param {string} type - 类型
     * @param {number} duration - 持续时间
     */
    showNotification(message, type = 'info', duration = 3000) {
        // 创建通知元素
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        
        // 添加样式
        notification.style.position = 'fixed';
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.padding = '16px';
        notification.style.borderRadius = '8px';
        notification.style.color = 'white';
        notification.style.fontSize = '14px';
        notification.style.fontWeight = '500';
        notification.style.zIndex = '9999';
        notification.style.boxShadow = '0 4px 12px rgba(0, 0, 0, 0.15)';
        notification.style.transition = 'all 0.3s ease';
        notification.style.transform = 'translateX(100%)';
        
        // 设置类型样式
        switch (type) {
            case 'success':
                notification.style.backgroundColor = '#22c55e';
                break;
            case 'error':
                notification.style.backgroundColor = '#ef4444';
                break;
            case 'warning':
                notification.style.backgroundColor = '#f59e0b';
                break;
            default:
                notification.style.backgroundColor = '#3b82f6';
        }
        
        // 添加到页面
        document.body.appendChild(notification);
        
        // 显示通知
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
        }, 100);
        
        // 隐藏通知
        setTimeout(() => {
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, duration);
    },

    /**
     * 显示加载动画
     * @param {string} message - 消息
     * @returns {Object} - 加载动画实例
     */
    showLoader(message = 'Loading...') {
        // 创建加载元素
        const loader = document.createElement('div');
        loader.className = 'loader';
        loader.innerHTML = `
            <div class="loader-overlay"></div>
            <div class="loader-content">
                <div class="loader-spinner"></div>
                <div class="loader-message">${message}</div>
            </div>
        `;
        
        // 添加样式
        loader.style.position = 'fixed';
        loader.style.top = '0';
        loader.style.left = '0';
        loader.style.width = '100%';
        loader.style.height = '100%';
        loader.style.zIndex = '9999';
        
        const overlay = loader.querySelector('.loader-overlay');
        overlay.style.position = 'absolute';
        overlay.style.top = '0';
        overlay.style.left = '0';
        overlay.style.width = '100%';
        overlay.style.height = '100%';
        overlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
        
        const content = loader.querySelector('.loader-content');
        content.style.position = 'absolute';
        content.style.top = '50%';
        content.style.left = '50%';
        content.style.transform = 'translate(-50%, -50%)';
        content.style.textAlign = 'center';
        content.style.color = 'white';
        
        const spinner = loader.querySelector('.loader-spinner');
        spinner.style.border = '4px solid rgba(255, 255, 255, 0.3)';
        spinner.style.borderRadius = '50%';
        spinner.style.borderTop = '4px solid #3b82f6';
        spinner.style.width = '40px';
        spinner.style.height = '40px';
        spinner.style.animation = 'spin 1s linear infinite';
        spinner.style.margin = '0 auto 16px';
        
        const messageElement = loader.querySelector('.loader-message');
        messageElement.style.fontSize = '14px';
        
        // 添加动画样式
        const style = document.createElement('style');
        style.textContent = `
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;
        document.head.appendChild(style);
        
        // 添加到页面
        document.body.appendChild(loader);
        
        // 返回实例
        return {
            hide: () => {
                document.body.removeChild(loader);
                document.head.removeChild(style);
            }
        };
    },

    /**
     * 生成随机颜色
     * @returns {string} - 随机颜色
     */
    generateRandomColor() {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    },

    /**
     * 深比较两个对象
     * @param {Object} obj1 - 对象1
     * @param {Object} obj2 - 对象2
     * @returns {boolean} - 是否相等
     */
    deepEqual(obj1, obj2) {
        if (obj1 === obj2) return true;
        if (obj1 == null || obj2 == null) return false;
        if (typeof obj1 !== typeof obj2) return false;
        if (typeof obj1 !== 'object') return false;
        
        const keys1 = Object.keys(obj1);
        const keys2 = Object.keys(obj2);
        if (keys1.length !== keys2.length) return false;
        
        for (const key of keys1) {
            if (!keys2.includes(key)) return false;
            if (!this.deepEqual(obj1[key], obj2[key])) return false;
        }
        
        return true;
    }
};

if (typeof module !== 'undefined' && module.exports) {
    module.exports = utils;
} else if (typeof window !== 'undefined') {
    window.utils = utils;
}
