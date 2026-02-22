/**
 * Nexus工具模块
 * 包含各种工具函数
 * @module nexusUtils
 */

(function() {
    class NexusUtils {
        constructor(base) {
            this.base = base;
        }

        /**
         * 格式化数字（添加千位分隔符）
         * @param {number} num - 数字
         * @returns {string} - 格式化后的字符串
         */
        formatNumber(num) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.formatNumber) {
                return sdk.formatNumber(num);
            }
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        }

        /**
         * 格式化时间戳
         * @param {number} timestamp - 时间戳
         * @returns {string} - 格式化后的时间字符串
         */
        formatTimestamp(timestamp) {
            const sdk = this.base.getSDK();
            if (sdk && sdk.formatTimestamp) {
                return sdk.formatTimestamp(timestamp);
            }
            return new Date(timestamp).toLocaleString();
        }

        /**
         * 生成唯一ID
         * @returns {string} - 唯一ID
         */
        generateId() {
            return 'id_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
        }

        /**
         * 防抖函数
         * @param {Function} func - 要执行的函数
         * @param {number} wait - 等待时间（毫秒）
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
        }

        /**
         * 节流函数
         * @param {Function} func - 要执行的函数
         * @param {number} limit - 时间限制（毫秒）
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
        }

        /**
         * 深拷贝对象
         * @param {Object} obj - 要拷贝的对象
         * @returns {Object} - 拷贝后的对象
         */
        deepClone(obj) {
            return JSON.parse(JSON.stringify(obj));
        }

        /**
         * 检查对象是否为空
         * @param {Object} obj - 要检查的对象
         * @returns {boolean} - 是否为空
         */
        isEmpty(obj) {
            return Object.keys(obj).length === 0;
        }

        /**
         * 安全地获取对象属性
         * @param {Object} obj - 对象
         * @param {string} path - 属性路径
         * @param {*} defaultValue - 默认值
         * @returns {*} - 属性值或默认值
         */
        get(obj, path, defaultValue = undefined) {
            const travel = (regexp) =>
                String.prototype.split
                    .call(path, regexp)
                    .filter(Boolean)
                    .reduce(
                        (res, key) => (res !== null && res !== undefined ? res[key] : res),
                        obj
                    );
            const result = travel(/[,\[\]\.]+/);
            return result === undefined || result === null ? defaultValue : result;
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusUtils;
    }

    if (typeof window !== 'undefined') {
        window.NexusUtils = NexusUtils;
    }
})();

