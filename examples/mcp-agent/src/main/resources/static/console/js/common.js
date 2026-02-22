/**
 * Nexus Console 公共脚本
 * 统一处理主题、菜单、页面初始化
 */

// ========== 页面初始化 ==========
(function() {
    'use strict';

    // 等待 DOM 加载完成
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initPage);
    } else {
        initPage();
    }

    function initPage() {
        console.log('[Common] Initializing page...');

        // 1. 初始化主题（如果 theme-manager.js 已加载）
        if (window.themeManager) {
            window.themeManager.applyTheme();
            console.log('[Common] Theme applied:', window.themeManager.getCurrentTheme());
        }

        // 2. 初始化菜单（如果 menu-loader.js 已加载）
        if (window.initMenu) {
            window.initMenu();
            console.log('[Common] Menu initialized');
        }

        // 3. 确保内容头部有主题切换按钮
        ensureThemeToggleButton();

        // 4. 触发页面自定义初始化
        if (window.onPageInit) {
            try {
                window.onPageInit();
                console.log('[Common] Page init callback executed');
            } catch (e) {
                console.error('[Common] Error in onPageInit:', e);
            }
        }
    }

    /**
     * 确保页面有主题切换按钮
     */
    function ensureThemeToggleButton() {
        // 查找内容头部（支持 .content-header 和 .header）
        const contentHeader = document.querySelector('.content-header, .header');
        if (!contentHeader) {
            // 如果没有头部，尝试创建
            createContentHeader();
            return;
        }

        // 检查是否已有主题切换按钮
        if (!contentHeader.querySelector('.theme-toggle-btn')) {
            const themeBtn = createThemeToggleButton();
            const contentActions = contentHeader.querySelector('.content-actions, .header-actions');
            if (contentActions) {
                contentActions.appendChild(themeBtn);
            } else {
                themeBtn.classList.add('header-theme-toggle');
                contentHeader.appendChild(themeBtn);
            }
        }
    }

    /**
     * 创建内容头部（如果不存在）
     */
    function createContentHeader() {
        const mainContent = document.querySelector('.main-content');
        if (!mainContent) return;

        // 检查是否已有 header
        let header = mainContent.querySelector('.content-header, .header');
        if (!header) {
            // 获取页面标题
            const pageTitle = document.title.split(' - ')[0] || '页面';

            header = document.createElement('div');
            header.className = 'content-header';
            header.innerHTML = `
                <h1><i class="ri-dashboard-line"></i> ${pageTitle}</h1>
                <div class="content-actions">
                    <button class="theme-toggle-btn btn btn-secondary">
                        <i class="ri-sun-line"></i> 切换主题
                    </button>
                </div>
            `;

            // 插入到 main-content 的开头
            mainContent.insertBefore(header, mainContent.firstChild);
        }
    }

    /**
     * 创建主题切换按钮
     */
    function createThemeToggleButton() {
        const button = document.createElement('button');
        button.className = 'theme-toggle-btn btn btn-secondary';
        button.type = 'button';

        // 根据当前主题设置按钮内容
        if (window.themeManager) {
            const currentTheme = window.themeManager.getCurrentTheme();
            if (currentTheme === 'dark') {
                button.innerHTML = '<i class="ri-sun-line"></i> 浅色模式';
            } else {
                button.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
            }
        } else {
            button.innerHTML = '<i class="ri-sun-line"></i> 切换主题';
        }

        // 绑定点击事件
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            if (window.themeManager) {
                window.themeManager.toggleTheme();
            } else if (window.toggleTheme) {
                window.toggleTheme();
            }
        });

        return button;
    }
})();

// ========== 工具函数 ==========

/**
 * 格式化日期时间
 */
function formatDateTime(date) {
    const d = new Date(date);
    return d.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
}

/**
 * 显示加载状态
 */
function showLoading(element, text = '加载中...') {
    if (typeof element === 'string') {
        element = document.getElementById(element);
    }
    if (element) {
        element.innerHTML = `<div class="loading"><i class="ri-loader-4-line ri-spin"></i> ${text}</div>`;
    }
}

/**
 * 显示错误信息
 */
function showError(element, message) {
    if (typeof element === 'string') {
        element = document.getElementById(element);
    }
    if (element) {
        element.innerHTML = `<div class="error-message"><i class="ri-error-warning-line"></i> ${message}</div>`;
    }
}

/**
 * 显示成功信息
 */
function showSuccess(element, message) {
    if (typeof element === 'string') {
        element = document.getElementById(element);
    }
    if (element) {
        element.innerHTML = `<div class="success-message"><i class="ri-check-line"></i> ${message}</div>`;
    }
}

/**
 * 发送 API 请求
 */
async function apiRequest(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const mergedOptions = { ...defaultOptions, ...options };
    if (options.headers) {
        mergedOptions.headers = { ...defaultOptions.headers, ...options.headers };
    }

    try {
        const response = await fetch(url, mergedOptions);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}

/**
 * 防抖函数
 */
function debounce(func, wait) {
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
 */
function throttle(func, limit) {
    let inThrottle;
    return function executedFunction(...args) {
        if (!inThrottle) {
            func(...args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// ========== 兼容性处理 ==========

// 确保 window.initMenu 存在（如果 menu-loader.js 未加载）
if (!window.initMenu) {
    window.initMenu = function() {
        console.warn('menu-loader.js not loaded, using default menu');
        const navMenu = document.getElementById('nav-menu');
        if (navMenu) {
            navMenu.innerHTML = `
                <li><a href="/console/pages/dashboard.html"><i class="ri-dashboard-line"></i> 仪表盘</a></li>
                <li><a href="/console/pages/lan/lan-dashboard.html"><i class="ri-wifi-line"></i> 局域网</a></li>
            `;
        }
    };
}

// 确保 window.toggleTheme 存在（如果 theme-manager.js 未加载）
if (!window.toggleTheme) {
    window.toggleTheme = function() {
        console.warn('theme-manager.js not loaded, using basic toggle');
        document.documentElement.classList.toggle('light-theme');
        document.body.classList.toggle('light-theme');
    };
}

// ========== 监听主题变化并更新按钮 ==========
window.addEventListener('themeChanged', function(e) {
    const theme = e.detail ? e.detail.theme : 'dark';
    const buttons = document.querySelectorAll('.theme-toggle-btn');
    buttons.forEach(btn => {
        if (theme === 'dark') {
            btn.innerHTML = '<i class="ri-sun-line"></i> 浅色模式';
            btn.title = '切换到浅色模式';
        } else {
            btn.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
            btn.title = '切换到深色模式';
        }
    });
});
