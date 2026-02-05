/**
 * 主题管理器
 * 处理主题切换、保存和加载功能
 */
class ThemeManager {
    constructor() {
        this.currentTheme = 'dark';
        this.init();
    }

    /**
     * 初始化主题管理器
     */
    init() {
        this.loadTheme();
        this.applyTheme();
        this.bindEvents();
    }

    /**
     * 从本地存储加载主题
     */
    loadTheme() {
        const savedTheme = localStorage.getItem('nexus_theme');
        if (savedTheme) {
            this.currentTheme = savedTheme;
        } else {
            // 检测系统主题偏好
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            this.currentTheme = prefersDark ? 'dark' : 'light';
        }
    }

    /**
     * 应用主题
     */
    applyTheme() {
        if (this.currentTheme === 'light') {
            document.documentElement.classList.add('light-theme');
        } else {
            document.documentElement.classList.remove('light-theme');
        }

        // 更新所有主题切换按钮的状态
        this.updateThemeButtons();
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
     * 保存主题到本地存储
     */
    saveTheme() {
        localStorage.setItem('nexus_theme', this.currentTheme);
    }

    /**
     * 获取当前主题
     * @returns {string} 当前主题名称
     */
    getCurrentTheme() {
        return this.currentTheme;
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        // 监听主题切换按钮点击
        document.addEventListener('click', (e) => {
            if (e.target.closest('.theme-toggle-btn')) {
                this.toggleTheme();
            }
        });

        // 监听系统主题变化
        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
            const savedTheme = localStorage.getItem('nexus_theme');
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
            if (this.currentTheme === 'dark') {
                button.innerHTML = '<i class="ri-sun-line"></i> 浅色模式';
                button.dataset.theme = 'light';
            } else {
                button.innerHTML = '<i class="ri-moon-line"></i> 深色模式';
                button.dataset.theme = 'dark';
            }
        });
    }

    /**
     * 创建主题切换按钮
     * @returns {HTMLButtonElement} 主题切换按钮
     */
    createThemeToggleButton() {
        const button = document.createElement('button');
        button.className = 'theme-toggle-btn btn btn-secondary';
        button.title = '切换主题';
        this.updateThemeButtons();
        return button;
    }
}

// 导出ThemeManager类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ThemeManager;
} else {
    window.ThemeManager = ThemeManager;
}

// 全局主题管理器实例
window.themeManager = new ThemeManager();

// 全局主题切换函数
window.toggleTheme = function() {
    if (window.themeManager) {
        window.themeManager.toggleTheme();
    }
};
