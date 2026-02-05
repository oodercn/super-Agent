/**
 * 移动端主题管理器
 * 统一管理所有移动端组件的主题切换、可访问性和响应式支持
 */

ood.Mobile = ood.Mobile || {};

ood.Mobile.ThemeManager = {
    // 当前主题
    currentTheme: 'light',
    
    // 支持的主题列表
    themes: ['light', 'dark', 'light-hc', 'dark-hc'],
    
    // 已注册的组件实例
    registeredComponents: [],
    
    // 初始化主题管理器
    init: function() {
        this.loadSavedTheme();
        this.watchSystemPreferences();
        this.applyGlobalTheme();
    },
    
    // 注册组件实例
    register: function(component) {
        if (component && typeof component.setTheme === 'function') {
            this.registeredComponents.push(component);
            // 立即应用当前主题
            component.setTheme(this.currentTheme, false);
        }
    },
    
    // 注销组件实例
    unregister: function(component) {
        var index = this.registeredComponents.indexOf(component);
        if (index > -1) {
            this.registeredComponents.splice(index, 1);
        }
    },
    
    // 设置全局主题
    setGlobalTheme: function(theme) {
        if (this.themes.indexOf(theme) === -1) {
            console.warn('Unsupported theme:', theme);
            return;
        }
        
        var oldTheme = this.currentTheme;
        this.currentTheme = theme;
        
        // 保存主题设置
        this.saveTheme(theme);
        
        // 应用到document
        this.applyGlobalTheme();
        
        // 更新所有注册的组件
        this.updateAllComponents();
        
        // 触发主题变化事件
        this.onThemeChange(oldTheme, theme);
    },
    
    // 获取当前主题
    getCurrentTheme: function() {
        return this.currentTheme;
    },
    
    // 切换暗黑模式
    toggleDarkMode: function() {
        var isCurrentlyDark = this.currentTheme.includes('dark');
        var isHighContrast = this.currentTheme.endsWith('-hc');
        
        var newTheme;
        if (isCurrentlyDark) {
            newTheme = isHighContrast ? 'light-hc' : 'light';
        } else {
            newTheme = isHighContrast ? 'dark-hc' : 'dark';
        }
        
        this.setGlobalTheme(newTheme);
    },
    
    // 切换高对比度模式
    toggleHighContrast: function() {
        var isHighContrast = this.currentTheme.endsWith('-hc');
        var baseTheme = this.currentTheme.replace('-hc', '');
        
        var newTheme = isHighContrast ? baseTheme : (baseTheme + '-hc');
        this.setGlobalTheme(newTheme);
    },
    
    // 检查是否为暗黑模式
    isDarkMode: function() {
        return this.currentTheme.includes('dark');
    },
    
    // 检查是否为高对比度模式
    isHighContrast: function() {
        return this.currentTheme.endsWith('-hc');
    },
    
    // 应用全局主题到document
    applyGlobalTheme: function() {
        var body = document.body;
        var html = document.documentElement;
        
        // 移除所有主题类
        this.themes.forEach(function(theme) {
            body.classList.remove('mobile-theme-' + theme);
            html.classList.remove('mobile-theme-' + theme);
        });
        
        // 移除data-theme属性
        body.removeAttribute('data-theme');
        html.removeAttribute('data-theme');
        
        // 应用当前主题
        if (this.currentTheme && this.currentTheme !== 'light') {
            body.classList.add('mobile-theme-' + this.currentTheme);
            html.classList.add('mobile-theme-' + this.currentTheme);
            body.setAttribute('data-theme', this.currentTheme);
            html.setAttribute('data-theme', this.currentTheme);
        }
    },
    
    // 更新所有注册的组件
    updateAllComponents: function() {
        var self = this;
        this.registeredComponents.forEach(function(component) {
            if (component && typeof component.setTheme === 'function') {
                try {
                    component.setTheme(self.currentTheme, false);
                } catch(e) {
                    console.error('Error updating component theme:', e);
                }
            }
        });
    },
    
    // 从localStorage加载保存的主题
    loadSavedTheme: function() {
        try {
            var savedTheme = localStorage.getItem('ood-mobile-global-theme');
            if (savedTheme && this.themes.indexOf(savedTheme) > -1) {
                this.currentTheme = savedTheme;
                return;
            }
        } catch(e) {
            // 忽略localStorage错误
        }
        
        // 检测系统主题偏好
        this.detectSystemTheme();
    },
    
    // 保存主题到localStorage
    saveTheme: function(theme) {
        try {
            localStorage.setItem('ood-mobile-global-theme', theme);
        } catch(e) {
            // 忽略localStorage错误
        }
    },
    
    // 检测系统主题偏好
    detectSystemTheme: function() {
        if (!window.matchMedia) {
            return;
        }
        
        var prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        var prefersHighContrast = window.matchMedia('(prefers-contrast: high)').matches;
        
        if (prefersDark && prefersHighContrast) {
            this.currentTheme = 'dark-hc';
        } else if (prefersDark) {
            this.currentTheme = 'dark';
        } else if (prefersHighContrast) {
            this.currentTheme = 'light-hc';
        } else {
            this.currentTheme = 'light';
        }
    },
    
    // 监听系统主题偏好变化
    watchSystemPreferences: function() {
        if (!window.matchMedia) {
            return;
        }
        
        var self = this;
        
        // 监听暗黑模式变化
        var darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
        var handleDarkModeChange = function(e) {
            // 只在没有手动设置主题时响应系统变化
            try {
                var savedTheme = localStorage.getItem('ood-mobile-global-theme');
                if (!savedTheme) {
                    var isHighContrast = self.currentTheme.endsWith('-hc');
                    var newTheme = e.matches ? 
                        (isHighContrast ? 'dark-hc' : 'dark') : 
                        (isHighContrast ? 'light-hc' : 'light');
                    self.setGlobalTheme(newTheme);
                }
            } catch(ex) {
                // 忽略错误
            }
        };
        
        // 监听高对比度变化
        var contrastQuery = window.matchMedia('(prefers-contrast: high)');
        var handleContrastChange = function(e) {
            try {
                var savedTheme = localStorage.getItem('ood-mobile-global-theme');
                if (!savedTheme) {
                    var isDark = self.currentTheme.includes('dark');
                    var baseTheme = isDark ? 'dark' : 'light';
                    var newTheme = e.matches ? (baseTheme + '-hc') : baseTheme;
                    self.setGlobalTheme(newTheme);
                }
            } catch(ex) {
                // 忽略错误
            }
        };
        
        // 绑定事件监听器
        if (darkModeQuery.addListener) {
            darkModeQuery.addListener(handleDarkModeChange);
        } else if (darkModeQuery.addEventListener) {
            darkModeQuery.addEventListener('change', handleDarkModeChange);
        }
        
        if (contrastQuery.addListener) {
            contrastQuery.addListener(handleContrastChange);
        } else if (contrastQuery.addEventListener) {
            contrastQuery.addEventListener('change', handleContrastChange);
        }
    },
    
    // 主题变化事件处理
    onThemeChange: function(oldTheme, newTheme) {
        // 触发自定义事件
        if (typeof CustomEvent !== 'undefined') {
            var event = new CustomEvent('oodMobileThemeChange', {
                detail: {
                    oldTheme: oldTheme,
                    newTheme: newTheme
                }
            });
            document.dispatchEvent(event);
        }
        
        // 兼容性回调
        if (typeof ood.Mobile.onThemeChange === 'function') {
            ood.Mobile.onThemeChange(oldTheme, newTheme);
        }
    },
    
    // 响应式管理
    ResponsiveManager: {
        // 当前屏幕尺寸
        currentSize: 'md',
        
        // 已注册的组件
        components: [],
        
        // 初始化响应式管理
        init: function() {
            this.updateCurrentSize();
            this.watchResize();
        },
        
        // 注册组件
        register: function(component) {
            if (component && typeof component.adjustLayout === 'function') {
                this.components.push(component);
                // 立即应用当前布局
                component.adjustLayout();
            }
        },
        
        // 注销组件
        unregister: function(component) {
            var index = this.components.indexOf(component);
            if (index > -1) {
                this.components.splice(index, 1);
            }
        },
        
        // 更新当前屏幕尺寸
        updateCurrentSize: function() {
            this.currentSize = ood.Mobile.utils.getScreenSize();
        },
        
        // 监听窗口大小变化
        watchResize: function() {
            var self = this;
            var resizeHandler = ood.Mobile.utils.throttle(function() {
                var oldSize = self.currentSize;
                self.updateCurrentSize();
                
                if (oldSize !== self.currentSize) {
                    self.updateAllComponents();
                    self.onSizeChange(oldSize, self.currentSize);
                }
            }, 250);
            
            ood(window).on('resize', resizeHandler);
        },
        
        // 更新所有组件布局
        updateAllComponents: function() {
            this.components.forEach(function(component) {
                if (component && typeof component.adjustLayout === 'function') {
                    try {
                        component.adjustLayout();
                    } catch(e) {
                        console.error('Error updating component layout:', e);
                    }
                }
            });
        },
        
        // 尺寸变化事件处理
        onSizeChange: function(oldSize, newSize) {
            // 触发自定义事件
            if (typeof CustomEvent !== 'undefined') {
                var event = new CustomEvent('oodMobileResponsiveChange', {
                    detail: {
                        oldSize: oldSize,
                        newSize: newSize
                    }
                });
                document.dispatchEvent(event);
            }
        }
    },
    
    // 可访问性管理
    AccessibilityManager: {
        // 初始化可访问性管理
        init: function() {
            this.setupGlobalKeyboardNavigation();
            this.setupFocusManagement();
            this.setupScreenReaderSupport();
        },
        
        // 设置全局键盘导航
        setupGlobalKeyboardNavigation: function() {
            // 为所有移动端组件添加键盘导航支持
            ood(document).on('keydown', function(e) {
                var target = ood(e.target);
                if (target.hasClass('ood-mobile-component')) {
                    // 处理全局键盘事件
                }
            });
        },
        
        // 设置焦点管理
        setupFocusManagement: function() {
            // 确保焦点在移动端组件间正确移动
        },
        
        // 设置屏幕阅读器支持
        setupScreenReaderSupport: function() {
            // 为动态内容添加live region
            if (!document.getElementById('ood-mobile-live-region')) {
                var liveRegion = document.createElement('div');
                liveRegion.id = 'ood-mobile-live-region';
                liveRegion.className = 'mobile-sr-only';
                liveRegion.setAttribute('aria-live', 'polite');
                liveRegion.setAttribute('aria-atomic', 'true');
                document.body.appendChild(liveRegion);
            }
        },
        
        // 向屏幕阅读器通知消息
        announce: function(message) {
            var liveRegion = document.getElementById('ood-mobile-live-region');
            if (liveRegion) {
                liveRegion.textContent = message;
                // 清空内容以便下次通知
                setTimeout(function() {
                    liveRegion.textContent = '';
                }, 1000);
            }
        }
    }
};

// 初始化主题管理器
ood.asyRun(function() {
    ood.Mobile.ThemeManager.init();
    ood.Mobile.ThemeManager.ResponsiveManager.init();
    ood.Mobile.ThemeManager.AccessibilityManager.init();
});

// 导出到全局
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ood.Mobile.ThemeManager;
}