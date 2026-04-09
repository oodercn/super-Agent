/**
 * 主题管理器
 * 处理主题切换、保存和加载功能
 * 符合 Nexus UI 规范：使用 data-theme 属性
 */
class ThemeManager {
    constructor() {
        this.storageKey = 'nx-theme';
        this.currentTheme = 'dark';
        this.init();
    }

    init() {
        this.loadTheme();
        this.applyThemeImmediately();
        this.bindEvents();
    }

    applyThemeImmediately() {
        const theme = this.currentTheme;
        
        if (theme === 'light') {
            document.documentElement.setAttribute('data-theme', 'light');
        } else {
            document.documentElement.removeAttribute('data-theme');
        }
    }

    loadTheme() {
        const savedTheme = localStorage.getItem(this.storageKey);
        if (savedTheme && (savedTheme === 'dark' || savedTheme === 'light')) {
            this.currentTheme = savedTheme;
        } else {
            this.currentTheme = 'dark';
            localStorage.setItem(this.storageKey, this.currentTheme);
        }
    }

    applyTheme() {
        if (this.currentTheme === 'light') {
            document.documentElement.setAttribute('data-theme', 'light');
        } else {
            document.documentElement.removeAttribute('data-theme');
        }
        
        this.updateThemeButtons();
        
        window.dispatchEvent(new CustomEvent('themeChanged', {
            detail: { theme: this.currentTheme }
        }));
    }

    toggleTheme() {
        this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
        this.saveTheme();
        this.applyTheme();
    }

    setTheme(theme) {
        if (theme === 'dark' || theme === 'light') {
            this.currentTheme = theme;
            this.saveTheme();
            this.applyTheme();
        }
    }

    saveTheme() {
        localStorage.setItem(this.storageKey, this.currentTheme);
    }

    getCurrentTheme() {
        return this.currentTheme;
    }

    bindEvents() {
        document.addEventListener('click', (e) => {
            const themeBtn = e.target.closest('.theme-toggle-btn, #theme-toggle');
            if (themeBtn) {
                e.preventDefault();
                e.stopPropagation();
                this.toggleTheme();
            }
        });

        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
            const savedTheme = localStorage.getItem(this.storageKey);
            if (!savedTheme) {
                this.currentTheme = e.matches ? 'dark' : 'light';
                this.applyTheme();
            }
        });
    }

    updateThemeButtons() {
        const buttons = document.querySelectorAll('.theme-toggle-btn, #theme-toggle');
        buttons.forEach(button => {
            this.updateButtonContent(button);
        });
    }

    updateButtonContent(button) {
        const icon = button.querySelector('i');
        if (icon) {
            icon.className = this.currentTheme === 'dark' ? 'ri-moon-line' : 'ri-sun-line';
        }
        button.title = this.currentTheme === 'dark' ? '切换到浅色模式' : '切换到深色模式';
    }
}

if (typeof module !== 'undefined' && module.exports) {
    module.exports = ThemeManager;
} else {
    window.ThemeManager = ThemeManager;
}

if (!window.themeManager) {
    window.themeManager = new ThemeManager();
}

(function() {
    const savedTheme = localStorage.getItem('nx-theme');
    const theme = savedTheme || 'dark';
    
    if (theme === 'light') {
        document.documentElement.setAttribute('data-theme', 'light');
    } else {
        document.documentElement.removeAttribute('data-theme');
    }
})();

window.toggleTheme = function() {
    if (window.themeManager) {
        window.themeManager.toggleTheme();
    }
};

document.addEventListener('DOMContentLoaded', function() {
    if (window.themeManager) {
        window.themeManager.updateThemeButtons();
    }
});
