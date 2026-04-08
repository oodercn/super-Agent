/**
 * NexusMenu - 统一菜单系统
 * 封装 MenuLoader 提供标准接口
 */

(function() {
    'use strict';

    const NexusMenu = {
        initialized: false,
        menuLoader: null,

        async init() {
            if (this.initialized) {
                console.log('[NexusMenu] 已经初始化，跳过');
                return;
            }

            console.log('[NexusMenu] 开始初始化...');

            // 确保 MenuLoader 已加载
            if (typeof MenuLoader === 'undefined') {
                console.log('[NexusMenu] MenuLoader 未加载，尝试动态加载...');
                await this.loadMenuLoader();
            }

            if (typeof MenuLoader === 'undefined') {
                console.error('[NexusMenu] MenuLoader 加载失败');
                return;
            }

            this.menuLoader = new MenuLoader();
            await this.menuLoader.init();
            this.initialized = true;

            console.log('[NexusMenu] 初始化完成');
        },

        async loadMenuLoader() {
            return new Promise((resolve, reject) => {
                // 检查是否已经在加载中
                if (document.querySelector('script[src*="menu-loader.js"]')) {
                    console.log('[NexusMenu] menu-loader.js 已存在，等待加载完成');
                    // 等待加载完成
                    const checkInterval = setInterval(() => {
                        if (typeof MenuLoader !== 'undefined') {
                            clearInterval(checkInterval);
                            resolve();
                        }
                    }, 50);
                    // 超时处理
                    setTimeout(() => {
                        clearInterval(checkInterval);
                        reject(new Error('MenuLoader load timeout'));
                    }, 5000);
                    return;
                }

                const script = document.createElement('script');
                script.src = '/console/js/menu-loader.js';
                script.onload = () => {
                    console.log('[NexusMenu] menu-loader.js 加载成功');
                    resolve();
                };
                script.onerror = () => {
                    console.error('[NexusMenu] menu-loader.js 加载失败');
                    reject(new Error('Failed to load menu-loader.js'));
                };
                document.head.appendChild(script);
            });
        },

        async refresh() {
            if (this.menuLoader) {
                await this.menuLoader.init();
            }
        },

        setRole(role) {
            if (this.menuLoader) {
                localStorage.setItem('currentRole', role);
                this.menuLoader.currentRole = role;
                this.menuLoader.renderMenu();
            }
        },

        getRole() {
            return localStorage.getItem('currentRole') || 'personal';
        }
    };

    window.NexusMenu = NexusMenu;

})();
