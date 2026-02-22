/**
 * Nexus UI模块
 * 包含所有UI交互相关功能
 * @module nexusUI
 */

(function() {
    class NexusUI {
        constructor(base) {
            this.base = base;
        }

        /**
         * 显示加载状态
         * @param {string} elementId - 元素ID
         * @param {string} message - 加载消息
         */
        showLoading(elementId, message = '加载中...') {
            const element = document.getElementById(elementId);
            if (element) {
                this.base.loadingStates.set(elementId, element.innerHTML);
                element.innerHTML = `<div class="loading-indicator">${message}</div>`;
                element.style.opacity = '0.7';
            }
        }

        /**
         * 隐藏加载状态
         * @param {string} elementId - 元素ID
         */
        hideLoading(elementId) {
            const element = document.getElementById(elementId);
            if (element && this.base.loadingStates.has(elementId)) {
                element.innerHTML = this.base.loadingStates.get(elementId);
                this.base.loadingStates.delete(elementId);
                element.style.opacity = '1';
            }
        }

        /**
         * 显示消息提示
         * @param {string} message - 消息内容
         * @param {string} type - 消息类型：success, error, warning, info
         * @param {number} duration - 显示时长（毫秒）
         */
        showMessage(message, type = 'info', duration = 3000) {
            const toast = document.createElement('div');
            toast.className = `toast ${type}`;
            toast.textContent = message;
            document.body.appendChild(toast);

            setTimeout(() => {
                toast.style.animation = 'slideOut 0.3s ease-in';
                setTimeout(() => {
                    document.body.removeChild(toast);
                }, 300);
            }, duration);
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusUI;
    }

    if (typeof window !== 'undefined') {
        window.NexusUI = NexusUI;
    }
})();

