/**
 * 主题管理器
 * 处理主题切换、保存和加载功能
 * 使用 Cookie 存储主题设置，确保跨页面一致性
 */
class ThemeManager {
    constructor() {
        this.cookieKey = 'nexus_theme';
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
            document.documentElement.classList.add('light-theme');
        } else {
            document.documentElement.classList.remove('light-theme');
        }
        
        document.documentElement.setAttribute('data-theme', theme);
    }

    loadTheme() {
        const savedTheme = this.getCookie(this.cookieKey);
        if (savedTheme && (savedTheme === 'dark' || savedTheme === 'light')) {
            this.currentTheme = savedTheme;
        } else {
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            this.currentTheme = prefersDark ? 'dark' : 'light';
            this.setCookie(this.cookieKey, this.currentTheme, 365);
        }
    }

    applyTheme() {
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
        
        document.documentElement.setAttribute('data-theme', this.currentTheme);
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
        this.setCookie(this.cookieKey, this.currentTheme, 365);
    }

    getCurrentTheme() {
        return this.currentTheme;
    }

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

    setCookie(name, value, days) {
        let expires = "";
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/; SameSite=Lax";
    }

    deleteCookie(name) {
        document.cookie = name + '=; Max-Age=-99999999; path=/';
    }

    bindEvents() {
        document.addEventListener('click', (e) => {
            const themeBtn = e.target.closest('.theme-toggle-btn');
            if (themeBtn) {
                e.preventDefault();
                e.stopPropagation();
                this.toggleTheme();
            }
        });

        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
            const savedTheme = this.getCookie(this.cookieKey);
            if (!savedTheme) {
                this.currentTheme = e.matches ? 'dark' : 'light';
                this.applyTheme();
            }
        });
    }

    updateThemeButtons() {
        const buttons = document.querySelectorAll('.theme-toggle-btn');
        buttons.forEach(button => {
            this.updateButtonContent(button);
        });
    }

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

    createThemeToggleButton() {
        const button = document.createElement('button');
        button.className = 'theme-toggle-btn btn btn-secondary';
        button.type = 'button';
        this.updateButtonContent(button);
        return button;
    }

    autoAddThemeToggleToHeader() {
        const header = document.querySelector('.content-header, .page-header, header');
        if (header && !header.querySelector('.theme-toggle-btn')) {
            const themeBtn = this.createThemeToggleButton();
            themeBtn.classList.add('header-theme-toggle');
            header.appendChild(themeBtn);
        }
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
    
    if (theme === 'light') {
        document.documentElement.classList.add('light-theme');
    } else {
        document.documentElement.classList.remove('light-theme');
    }
    document.documentElement.setAttribute('data-theme', theme);
})();

window.toggleTheme = function() {
    if (window.themeManager) {
        window.themeManager.toggleTheme();
    }
};

document.addEventListener('DOMContentLoaded', function() {
    if (window.themeManager) {
        window.themeManager.autoAddThemeToggleToHeader();
    }
});
