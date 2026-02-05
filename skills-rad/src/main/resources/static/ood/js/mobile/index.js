/**
 * ood.js Mobile UI Components Library
 * 基于ood.UI的移动端组件库
 * 符合四分离设计模式（样式、模板、行为、数据分离）
 * @version 1.0.0
 * @author ood Development Team
 */

// 移动端组件库命名空间
ood.Mobile = ood.Mobile || {};

// 移动端基础配置
ood.Mobile.config = {
    // 移动端默认主题
    defaultTheme: 'mobile-light',
    
    // 触摸事件配置
    touch: {
        enabled: true,
        swipeThreshold: 50,
        longPressDelay: 500
    },
    
    // 响应式断点
    breakpoints: {
        xs: 0,      // 超小屏幕
        sm: 576,    // 小屏幕
        md: 768,    // 中等屏幕
        lg: 992,    // 大屏幕
        xl: 1200    // 超大屏幕
    },
    
    // 组件默认尺寸
    sizes: {
        xs: 'xs',
        sm: 'sm', 
        md: 'md',
        lg: 'lg',
        xl: 'xl'
    }
};

// 移动端工具函数
ood.Mobile.utils = {
    // 检测是否为移动设备
    isMobile: function() {
        return ood.browser.isTouch || window.innerWidth < 768;
    },
    
    // 获取屏幕尺寸分类
    getScreenSize: function() {
        var width = window.innerWidth;
        var breakpoints = ood.Mobile.config.breakpoints;
        
        if (width < breakpoints.sm) return 'xs';
        if (width < breakpoints.md) return 'sm';
        if (width < breakpoints.lg) return 'md';
        if (width < breakpoints.xl) return 'lg';
        return 'xl';
    },
    
    // 移动端安全区域处理
    getSafeArea: function() {
        return {
            top: parseInt(getComputedStyle(document.documentElement).getPropertyValue('--safe-area-inset-top') || '0'),
            right: parseInt(getComputedStyle(document.documentElement).getPropertyValue('--safe-area-inset-right') || '0'),
            bottom: parseInt(getComputedStyle(document.documentElement).getPropertyValue('--safe-area-inset-bottom') || '0'),
            left: parseInt(getComputedStyle(document.documentElement).getPropertyValue('--safe-area-inset-left') || '0')
        };
    },
    
    // 防抖函数
    debounce: function(func, wait) {
        var timeout;
        return function() {
            var context = this, args = arguments;
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                func.apply(context, args);
            }, wait);
        };
    },
    
    // 节流函数
    throttle: function(func, limit) {
        var inThrottle;
        return function() {
            var args = arguments;
            var context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(function() { inThrottle = false; }, limit);
            }
        };
    }
};

// 移动端组件基础类
ood.Class("ood.Mobile.Base", "ood.UI", {
    Instance: {
        // 移动端组件初始化
        Initialize: function() {
            this.initMobileFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile.ThemeManager !== 'undefined') {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        // 初始化移动端特性
        initMobileFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端CSS类
            profile.getRoot().addClass('ood-mobile-component');
            
            // 初始化触摸事件
            this.initTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // 初始化触摸事件
        initTouchEvents: function() {
            if (!ood.Mobile.config.touch.enabled) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 添加触摸支持类
            root.addClass('ood-touch-enabled');
            
            // 绑定触摸事件
            this.bindTouchEvents();
        },
        
        // 绑定触摸事件（子类重写）
        bindTouchEvents: function() {
            // 子类实现具体的触摸事件绑定
        },
        
        // ===== 主题管理 =====
        
        // 设置主题
        setTheme: function(theme, persist) {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var oldTheme = profile.properties.theme || 'light';
                
                // 更新属性
                profile.properties.theme = theme;
                
                // 移除旧主题类
                root.removeClass('mobile-theme-' + oldTheme);
                root.removeAttr('data-theme');
                
                // 应用新主题
                if (theme && theme !== 'light') {
                    root.addClass('mobile-theme-' + theme);
                    root.attr('data-theme', theme);
                }
                
                // 持久化主题设置
                if (persist !== false) {
                    try {
                        localStorage.setItem('ood-mobile-theme', theme);
                    } catch(e) {
                        // 忽略localStorage错误
                    }
                }
                
                // 触发主题变化事件
                profile.boxing().onThemeChange(oldTheme, theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            if (profile && profile.properties.theme) {
                return profile.properties.theme;
            }
            
            // 从localStorage获取
            try {
                var savedTheme = localStorage.getItem('ood-mobile-theme');
                if (savedTheme) return savedTheme;
            } catch(e) {
                // 忽略localStorage错误
            }
            
            // 检测系统主题
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                return 'dark';
            }
            
            return 'light';
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            var newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            return this.setTheme(newTheme);
        },
        
        // 设置高对比度模式
        setHighContrast: function(enabled) {
            var currentTheme = this.getTheme();
            var baseTheme = currentTheme.replace('-hc', '');
            var newTheme = enabled ? (baseTheme + '-hc') : baseTheme;
            return this.setTheme(newTheme);
        },
        
        // 检查是否为高对比度模式
        isHighContrast: function() {
            return this.getTheme().endsWith('-hc');
        },
        
        // 监听系统主题变化
        watchSystemTheme: function() {
            var self = this;
            
            if (window.matchMedia) {
                var darkModeQuery = window.matchMedia('(prefers-color-scheme: dark)');
                var contrastQuery = window.matchMedia('(prefers-contrast: high)');
                
                // 监听暗黑模式变化
                var handleDarkModeChange = function(e) {
                    var currentTheme = self.getTheme();
                    var isCurrentlyHC = currentTheme.endsWith('-hc');
                    var baseTheme = e.matches ? 'dark' : 'light';
                    var newTheme = isCurrentlyHC ? (baseTheme + '-hc') : baseTheme;
                    
                    self.setTheme(newTheme, false); // 不持久化系统主题
                };
                
                // 监听高对比度变化
                var handleContrastChange = function(e) {
                    var currentTheme = self.getTheme();
                    var baseTheme = currentTheme.replace('-hc', '');
                    var newTheme = e.matches ? (baseTheme + '-hc') : baseTheme;
                    
                    self.setTheme(newTheme, false); // 不持久化系统主题
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
                
                // 保存清理函数
                var profile = this.get(0);
                if (profile) {
                    profile._themeCleanup = function() {
                        if (darkModeQuery.removeListener) {
                            darkModeQuery.removeListener(handleDarkModeChange);
                        } else if (darkModeQuery.removeEventListener) {
                            darkModeQuery.removeEventListener('change', handleDarkModeChange);
                        }
                        
                        if (contrastQuery.removeListener) {
                            contrastQuery.removeListener(handleContrastChange);
                        } else if (contrastQuery.removeEventListener) {
                            contrastQuery.removeEventListener('change', handleContrastChange);
                        }
                    };
                }
            }
            
            return this;
        },
        
        // 主题变化事件处理
        onThemeChange: function(oldTheme, newTheme) {
            // 子类可以重写此方法处理主题变化
        },
        
        // 初始化响应式
        initResponsive: function() {
            var self = this;
            var profile = this.get(0);
            
            // 添加当前屏幕尺寸类
            this.updateResponsiveClass();
            
            // 监听窗口大小变化
            ood(window).on('resize', ood.Mobile.utils.throttle(function() {
                self.updateResponsiveClass();
            }, 250));
        },
        
        // 更新响应式CSS类
        updateResponsiveClass: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var size = ood.Mobile.utils.getScreenSize();
            
            // 移除所有尺寸类
            root.removeClass('ood-xs ood-sm ood-md ood-lg ood-xl');
            
            // 添加当前尺寸类
            root.addClass('ood-' + size);
        },
        
        // 初始化可访问性
        initAccessibility: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 确保所有交互元素都有适当的ARIA属性
            if (!root.attr('role')) {
                root.attr('role', 'widget');
            }
            
            // 添加键盘导航支持
            this.initKeyboardNavigation();
            
            // 添加ARIA标签
            this.addAriaLabels();
            
            // 初始化屏幕阅读器支持
            this.initScreenReaderSupport();
        },
        
        // 初始化键盘导航
        initKeyboardNavigation: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 为可交互元素添加tabindex
            if (this.isInteractive()) {
                if (!root.attr('tabindex')) {
                    root.attr('tabindex', '0');
                }
            }
            
            // 绑定键盘事件
            this.bindKeyboardEvents();
        },
        
        // 绑定键盘事件
        bindKeyboardEvents: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 基本键盘事件处理
            root.on('keydown', function(e) {
                var keyCode = e.keyCode || e.which;
                
                switch(keyCode) {
                    case 13: // Enter
                    case 32: // Space
                        if (profile.boxing().isInteractive()) {
                            e.preventDefault();
                            profile.boxing().onActivate(e);
                        }
                        break;
                    case 27: // Escape
                        profile.boxing().onEscape(e);
                        break;
                    case 9: // Tab
                        profile.boxing().onTab(e);
                        break;
                }
            });
        },
        
        // 添加ARIA标签
        addAriaLabels: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var properties = profile.properties;
            
            // 添加基本ARIA属性
            if (properties.ariaLabel) {
                root.attr('aria-label', properties.ariaLabel);
            }
            
            if (properties.ariaLabelledBy) {
                root.attr('aria-labelledby', properties.ariaLabelledBy);
            }
            
            if (properties.ariaDescribedBy) {
                root.attr('aria-describedby', properties.ariaDescribedBy);
            }
            
            // 状态相关ARIA属性
            if (properties.disabled) {
                root.attr('aria-disabled', 'true');
            }
            
            if (properties.hidden) {
                root.attr('aria-hidden', 'true');
            }
        },
        
        // 初始化屏幕阅读器支持
        initScreenReaderSupport: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 为动态内容添加live region
            if (this.isDynamic()) {
                root.attr('aria-live', 'polite');
            }
            
            // 添加屏幕阅读器专用文本
            this.addScreenReaderText();
        },
        
        // 添加屏幕阅读器专用文本
        addScreenReaderText: function() {
            // 子类可以重写此方法添加专用文本
        },
        
        // 检查是否为交互式组件
        isInteractive: function() {
            // 子类重写此方法
            return false;
        },
        
        // 检查是否为动态组件
        isDynamic: function() {
            // 子类重写此方法
            return false;
        },
        
        // 激活事件处理
        onActivate: function(e) {
            // 子类重写此方法
        },
        
        // Escape键事件处理
        onEscape: function(e) {
            // 子类重写此方法
        },
        
        // Tab键事件处理
        onTab: function(e) {
            // 子类重写此方法
        },
        
        // ===== 响应式支持 =====
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                
                // 移除所有响应式类
                root.removeClass('mobile-responsive-xs mobile-responsive-sm mobile-responsive-md mobile-responsive-lg mobile-responsive-xl');
                
                // 添加当前屏幕尺寸类
                root.addClass('mobile-responsive-' + screenSize);
                
                // 移动端特殊处理
                if (width < 768) {
                    root.addClass('mobile-responsive');
                    profile.boxing().onMobileLayout();
                } else {
                    root.removeClass('mobile-responsive');
                    profile.boxing().onDesktopLayout();
                }
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('mobile-tiny');
                    profile.boxing().onTinyLayout();
                } else {
                    root.removeClass('mobile-tiny');
                }
            });
        },
        
        // 移动端布局事件
        onMobileLayout: function() {
            // 子类重写此方法
        },
        
        // 桌面端布局事件
        onDesktopLayout: function() {
            // 子类重写此方法
        },
        
        // 超小屏幕布局事件
        onTinyLayout: function() {
            // 子类重写此方法
        },
        
        // 设置移动端主题（即将废弃，使用ThemeManager替代）
        setMobileTheme: function(theme) {
            // 如果主题管理器存在，优先使用全局主题管理
            if (typeof ood.Mobile.ThemeManager !== 'undefined') {
                ood.Mobile.ThemeManager.setGlobalTheme(theme);
                return this;
            }
            
            // 退回到本地主题设置
            return this.each(function(profile) {
                var root = profile.getRoot();
                
                // 移除所有主题类
                root.removeClass('mobile-light mobile-dark mobile-hc');
                
                // 添加新主题类
                if (theme) {
                    root.addClass('mobile-' + theme);
                }
                
                // 保存主题设置
                profile.properties.mobileTheme = theme;
            });
        },
        
        // 获取当前移动端主题（即将废弃，使用ThemeManager替代）
        getMobileTheme: function() {
            // 如果主题管理器存在，优先使用全局主题管理
            if (typeof ood.Mobile.ThemeManager !== 'undefined') {
                return ood.Mobile.ThemeManager.getCurrentTheme();
            }
            
            var profile = this.get(0);
            return profile.properties.mobileTheme || 'light';
        },
        
        // 组件销毁时的清理工作
        destroy: function() {
            // 从主题管理器中注销
            if (typeof ood.Mobile.ThemeManager !== 'undefined') {
                ood.Mobile.ThemeManager.unregister(this);
                ood.Mobile.ThemeManager.ResponsiveManager.unregister(this);
            }
            
            // 清理主题监听器
            var profile = this.get(0);
            if (profile && profile._themeCleanup) {
                profile._themeCleanup();
                delete profile._themeCleanup;
            }
            
            return this;
        }
    },
    
    Static: {
        // 数据模型
        DataModel: {
            // 主题设置
            theme: {
                caption: '主题模式',
                ini: 'light',
                listbox: ['light', 'dark', 'light-hc', 'dark-hc'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // 响应式设置
            responsive: {
                caption: '响应式布局',
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            // 可访问性设置
            accessibility: {
                caption: '可访问性支持',
                ini: true
            },
            
            // ARIA标签
            ariaLabel: {
                caption: 'ARIA标签',
                ini: ''
            },
            
            ariaLabelledBy: {
                caption: 'ARIA标签关联',
                ini: ''
            },
            
            ariaDescribedBy: {
                caption: 'ARIA描述关联',
                ini: ''
            },
            
            // 禁用状态
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    var root = this.getRoot();
                    if (value) {
                        root.attr('aria-disabled', 'true');
                        root.addClass('mobile-disabled');
                    } else {
                        root.removeAttr('aria-disabled');
                        root.removeClass('mobile-disabled');
                    }
                }
            },
            
            // 隐藏状态
            hidden: {
                caption: '隐藏状态',
                ini: false,
                action: function(value) {
                    var root = this.getRoot();
                    if (value) {
                        root.attr('aria-hidden', 'true');
                        root.css('display', 'none');
                    } else {
                        root.removeAttr('aria-hidden');
                        root.css('display', '');
                    }
                }
            }
        },
        
        // 渲染触发器
        RenderTrigger: function() {
            var profile = this;
            
            // 初始化移动端特性
            ood.asyRun(function() {
                profile.boxing().Initialize();
                
                // 初始化主题
                var savedTheme = profile.boxing().getTheme();
                if (savedTheme) {
                    profile.boxing().setTheme(savedTheme, false);
                }
                
                // 初始化响应式设计
                if (profile.properties.responsive !== false) {
                    profile.boxing().adjustLayout();
                }
                
                // 监听系统主题变化
                profile.boxing().watchSystemTheme();
            });
        },
        
        // 事件处理器
        EventHandlers: {
            onThemeChange: function(profile, oldTheme, newTheme) {
                // 主题变化事件处理器
            },
            
            onResponsiveChange: function(profile, screenSize) {
                // 响应式变化事件处理器
            }
        },
        
        // 移动端组件通用样式变量
        CSSVariables: {
            // 颜色主题
            '--mobile-primary': '#007AFF',
            '--mobile-secondary': '#5856D6',
            '--mobile-success': '#34C759',
            '--mobile-warning': '#FF9500',
            '--mobile-danger': '#FF3B30',
            '--mobile-info': '#007AFF',
            '--mobile-light': '#F2F2F7',
            '--mobile-dark': '#1C1C1E',
            
            // 文字颜色
            '--mobile-text-primary': '#000000',
            '--mobile-text-secondary': '#3C3C43',
            '--mobile-text-tertiary': '#6A6A6A',
            '--mobile-text-quaternary': '#99999F',
            
            // 背景颜色
            '--mobile-bg-primary': '#FFFFFF',
            '--mobile-bg-secondary': '#F2F2F7',
            '--mobile-bg-tertiary': '#FFFFFF',
            
            // 边框
            '--mobile-border-color': '#C6C6C8',
            '--mobile-border-width': '0.5px',
            '--mobile-border-radius': '8px',
            
            // 阴影
            '--mobile-shadow-light': '0 1px 3px rgba(0,0,0,0.1)',
            '--mobile-shadow-medium': '0 4px 6px rgba(0,0,0,0.1)',
            '--mobile-shadow-heavy': '0 10px 25px rgba(0,0,0,0.15)',
            
            // 间距
            '--mobile-spacing-xs': '4px',
            '--mobile-spacing-sm': '8px',
            '--mobile-spacing-md': '16px',
            '--mobile-spacing-lg': '24px',
            '--mobile-spacing-xl': '32px',
            
            // 字体大小
            '--mobile-font-xs': '10px',
            '--mobile-font-sm': '12px',
            '--mobile-font-md': '14px',
            '--mobile-font-lg': '16px',
            '--mobile-font-xl': '18px',
            '--mobile-font-xxl': '20px',
            
            // 触摸目标最小尺寸
            '--mobile-touch-target': '44px',
            
            // 安全区域
            '--mobile-safe-top': 'env(safe-area-inset-top)',
            '--mobile-safe-right': 'env(safe-area-inset-right)',
            '--mobile-safe-bottom': 'env(safe-area-inset-bottom)',
            '--mobile-safe-left': 'env(safe-area-inset-left)'
        }
    }
});

// 导出移动端组件命名空间
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ood.Mobile;
}