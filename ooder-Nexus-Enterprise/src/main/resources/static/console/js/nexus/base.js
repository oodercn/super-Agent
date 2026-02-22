/**
 * Nexus基础模块
 * 包含核心类和SDK管理功能
 * @module nexusBase
 */

(function() {
    class NexusBase {
        constructor() {
            this.apiBaseUrl = '/api';
            this.ajaxTimeout = 30000;
            this.loadingStates = new Map();
            this.sdk = null;
        }

        /**
         * 获取 SDK 实例
         * @returns {Object} SDK 实例
         */
        getSDK() {
            if (!this.sdk) {
                if (window.nexusSDKFactory) {
                    this.sdk = window.nexusSDKFactory.getCurrentSDK();
                } else {
                    console.warn('[nexusCommon] nexusSDKFactory not found, using legacy API');
                }
            }
            return this.sdk;
        }

        /**
         * 切换到 Mock SDK
         */
        switchToMockSDK() {
            if (window.nexusSDKFactory) {
                window.nexusSDKFactory.switchToMockSDK();
                this.sdk = window.nexusSDKFactory.getCurrentSDK();
                console.log('[nexusCommon] Switched to Mock SDK');
            }
        }

        /**
         * 切换到 Real SDK
         */
        switchToRealSDK() {
            if (window.nexusSDKFactory) {
                window.nexusSDKFactory.switchToRealSDK();
                this.sdk = window.nexusSDKFactory.getCurrentSDK();
                console.log('[nexusCommon] Switched to Real SDK');
            }
        }

        /**
         * 检查当前是否使用 Mock SDK
         * @returns {boolean} 是否使用 Mock SDK
         */
        isMockMode() {
            if (window.nexusSDKFactory) {
                return window.nexusSDKFactory.isMockMode();
            }
            return false;
        }

        /**
         * 检查当前是否使用 Real SDK
         * @returns {boolean} 是否使用 Real SDK
         */
        isRealMode() {
            if (window.nexusSDKFactory) {
                return window.nexusSDKFactory.isRealMode();
            }
            return false;
        }

        /**
         * 获取 SDK 信息
         * @returns {Object} SDK 信息
         */
        getSDKInfo() {
            if (window.nexusSDKFactory) {
                return window.nexusSDKFactory.getSDKInfo();
            }
            return null;
        }

        /**
         * 初始化Nexus页面
         * @param {string} pageId - 页面ID
         * @param {Function} callback - 回调函数
         */
        initPage(pageId, callback) {
            if (window.initMenu) {
                window.initMenu();
            }

            this.initThemeToggle();

            if (typeof callback === 'function') {
                callback();
            }

            console.log(`Nexus page ${pageId} initialized`);
        }

        /**
         * 初始化主题切换
         * 使用统一的 cookie 存储 (nexus_theme)
         */
        initThemeToggle() {
            const themeToggleBtn = document.querySelector('.theme-toggle-btn');
            if (themeToggleBtn) {
                themeToggleBtn.addEventListener('click', () => {
                    document.body.classList.toggle('light-theme');
                    document.documentElement.classList.toggle('light-theme');
                    const isLightTheme = document.body.classList.contains('light-theme');
                    themeToggleBtn.innerHTML = isLightTheme
                        ? '<i class="ri-moon-line"></i> 深色模式'
                        : '<i class="ri-sun-line"></i> 浅色模式';
                    // 使用统一的 cookie key
                    this.setCookie('nexus_theme', isLightTheme ? 'light' : 'dark', 365);
                });

                const savedTheme = this.getCookie('nexus_theme');
                if (savedTheme === 'light') {
                    document.body.classList.add('light-theme');
                    document.documentElement.classList.add('light-theme');
                    themeToggleBtn.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
                }
            }
        }

        /**
         * 获取 Cookie 值
         * @param {string} name - Cookie 名称
         * @returns {string|null} Cookie 值
         */
        getCookie(name) {
            const nameEQ = name + "=";
            const ca = document.cookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) === ' ') c = c.substring(1, c.length);
                if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
            }
            return null;
        }

        /**
         * 设置 Cookie
         * @param {string} name - Cookie 名称
         * @param {string} value - Cookie 值
         * @param {number} days - 过期天数
         */
        setCookie(name, value, days) {
            let expires = "";
            if (days) {
                const date = new Date();
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toUTCString();
            }
            document.cookie = name + "=" + (value || "") + expires + "; path=/; SameSite=Lax";
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusBase;
    }

    if (typeof window !== 'undefined') {
        window.NexusBase = NexusBase;
    }
})();

