/**
 * 主题管理器
 * 处理主题切换、保存和加载功能
 * 使用 Cookie 存储主题设置，确保跨页面一致性
 */
class ThemeManager {
    constructor() {
        // 使用统一的 cookie key
        this.cookieKey = 'nexus_theme';
        this.currentTheme = 'dark';
        this.init();
    }

    /**
     * 初始化主题管理器
     */
    init() {
        this.loadTheme();
        this.applyThemeImmediately();
        this.bindEvents();
    }

    /**
     * 立即应用主题（在DOM加载前）
     * 防止页面闪烁
     */
    applyThemeImmediately() {
        const theme = this.currentTheme;
        
        // 直接在 documentElement 上应用主题，不等待 body 加载
        if (theme === 'light') {
            document.documentElement.classList.add('light-theme');
        } else {
            document.documentElement.classList.remove('light-theme');
        }
        
        // 添加一个标记，表示主题已初始化
        document.documentElement.setAttribute('data-theme', theme);
    }

    /**
     * 从 Cookie 加载主题
     */
    loadTheme() {
        const savedTheme = this.getCookie(this.cookieKey);
        if (savedTheme && (savedTheme === 'dark' || savedTheme === 'light')) {
            this.currentTheme = savedTheme;
        } else {
            // 检测系统主题偏好
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            this.currentTheme = prefersDark ? 'dark' : 'light';
            // 保存到 cookie
            this.setCookie(this.cookieKey, this.currentTheme, 365);
        }
    }

    /**
     * 应用主题（完整应用，包括body和按钮更新）
     */
    applyTheme() {
        // 应用主题到 documentElement
        if (this.currentTheme === 'light') {
            document.documentElement.classList.add('light-theme');
            if (document.body) {
                document.body.classList.add('light-theme');
            }
        } else {
            document.documentElement.classList.remove('light-theme');
            if (document.body) {
                document.body.classList.remove('light-theme');
            }
        }
        
        // 更新主题标记
        document.documentElement.setAttribute('data-theme', this.currentTheme);

        // 更新所有主题切换按钮的状态
        this.updateThemeButtons();

        // 触发自定义事件，通知其他组件主题已更改
        window.dispatchEvent(new CustomEvent('themeChanged', {
            detail: { theme: this.currentTheme }
        }));
    }

    /**
     * 切换主题
     */
    toggleTheme() {
        this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
        this.saveTheme();
        this.applyTheme();
    }

    /**
     * 设置主题
     * @param {string} theme - 主题名称 ('dark' 或 'light')
     */
    setTheme(theme) {
        if (theme === 'dark' || theme === 'light') {
            this.currentTheme = theme;
            this.saveTheme();
            this.applyTheme();
        }
    }

    /**
     * 保存主题到 Cookie
     */
    saveTheme() {
        this.setCookie(this.cookieKey, this.currentTheme, 365);
    }

    /**
     * 获取当前主题
     * @returns {string} 当前主题名称
     */
    getCurrentTheme() {
        return this.currentTheme;
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
        // 设置 cookie，确保跨页面可用
        document.cookie = name + "=" + (value || "") + expires + "; path=/; SameSite=Lax";
    }

    /**
     * 删除 Cookie
     * @param {string} name - Cookie 名称
     */
    deleteCookie(name) {
        document.cookie = name + '=; Max-Age=-99999999; path=/';
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        // 监听主题切换按钮点击
        document.addEventListener('click', (e) => {
            const themeBtn = e.target.closest('.theme-toggle-btn');
            if (themeBtn) {
                e.preventDefault();
                e.stopPropagation();
                this.toggleTheme();
            }
        });

        // 监听系统主题变化
        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
            const savedTheme = this.getCookie(this.cookieKey);
            if (!savedTheme) {
                // 只有在没有保存主题偏好时才跟随系统变化
                this.currentTheme = e.matches ? 'dark' : 'light';
                this.applyTheme();
            }
        });
    }

    /**
     * 更新主题切换按钮的状态
     */
    updateThemeButtons() {
        const buttons = document.querySelectorAll('.theme-toggle-btn');
        buttons.forEach(button => {
            this.updateButtonContent(button);
        });
    }

    /**
     * 更新单个按钮的内容
     * @param {HTMLButtonElement} button - 按钮元素
     */
    updateButtonContent(button) {
        if (this.currentTheme === 'dark') {
            button.innerHTML = '<i class="ri-sun-line"></i> 浅色模式';
            button.title = '切换到浅色模式';
            button.dataset.theme = 'light';
        } else {
            button.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
            button.title = '切换到深色模式';
            button.dataset.theme = 'dark';
        }
    }

    /**
     * 创建主题切换按钮
     * @returns {HTMLButtonElement} 主题切换按钮
     */
    createThemeToggleButton() {
        const button = document.createElement('button');
        button.className = 'theme-toggle-btn btn btn-secondary';
        button.type = 'button';
        this.updateButtonContent(button);
        return button;
    }

    /**
     * 在所有页面上自动添加主题切换按钮到头部
     */
    autoAddThemeToggleToHeader() {
        // 查找头部区域
        const header = document.querySelector('.content-header, .page-header, header');
        if (header && !header.querySelector('.theme-toggle-btn')) {
            const themeBtn = this.createThemeToggleButton();
            themeBtn.classList.add('header-theme-toggle');
            header.appendChild(themeBtn);
        }
    }
}

// 导出ThemeManager类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ThemeManager;
} else {
    window.ThemeManager = ThemeManager;
}

// 全局主题管理器实例 - 确保只创建一个实例
if (!window.themeManager) {
    window.themeManager = new ThemeManager();
}

// 立即执行主题应用（防止页面闪烁）
// 这段代码在脚本加载时立即执行，不需要等待 DOMContentLoaded
(function() {
    function getCookie(name) {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }
    
    const savedTheme = getCookie('nexus_theme');
    const theme = savedTheme || 'dark';
    
    // 立即应用主题到 html 元素
    if (theme === 'light') {
        document.documentElement.classList.add('light-theme');
    } else {
        document.documentElement.classList.remove('light-theme');
    }
    document.documentElement.setAttribute('data-theme', theme);
})();

// 全局主题切换函数
window.toggleTheme = function() {
    if (window.themeManager) {
        window.themeManager.toggleTheme();
    }
};

// 页面加载完成后自动添加主题切换按钮
document.addEventListener('DOMContentLoaded', function() {
    if (window.themeManager) {
        window.themeManager.autoAddThemeToggleToHeader();
    }
});
