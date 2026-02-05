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
         */
        initThemeToggle() {
            const themeToggleBtn = document.querySelector('.theme-toggle-btn');
            if (themeToggleBtn) {
                themeToggleBtn.addEventListener('click', () => {
                    document.body.classList.toggle('light-theme');
                    const isLightTheme = document.body.classList.contains('light-theme');
                    themeToggleBtn.innerHTML = isLightTheme 
                        ? '<i class="ri-moon-line"></i> 深色模式' 
                        : '<i class="ri-sun-line"></i> 浅色模式';
                    localStorage.setItem('theme', isLightTheme ? 'light' : 'dark');
                });

                const savedTheme = localStorage.getItem('theme');
                if (savedTheme === 'light') {
                    document.body.classList.add('light-theme');
                    themeToggleBtn.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
                }
            }
        }
    }

    if (typeof module !== 'undefined' && module.exports) {
        module.exports = NexusBase;
    }

    if (typeof window !== 'undefined') {
        window.NexusBase = NexusBase;
    }
})();

