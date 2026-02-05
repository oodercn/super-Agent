/* Ooder系统全局主题切换器 */
ood.Class("ood.ThemeSwitcher", {}, {
    Instance: {
        // 初始化主题切换器
        init: function() {
            // 检查本地存储中的主题设置
            var savedTheme = localStorage.getItem('ood-theme');
            
            // 如果没有保存的主题，检查系统偏好
            if (!savedTheme) {
                savedTheme = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
            }
            
            // 应用保存的主题
            this.applyTheme(savedTheme);
            
            return this;
        },
        
        // 应用主题
        applyTheme: function(theme) {
            var body = ood.Dom.getBody();
            
            // 移除旧的主题类
            if (theme === 'dark') {
                body.removeClass('light-theme');
                body.addClass('dark-theme');
            } else {
                body.removeClass('dark-theme');
                body.addClass('light-theme');
            }
            
            // 保存主题设置到本地存储
            localStorage.setItem('ood-theme', theme);
            
            // 通知所有已初始化的UI组件切换主题
            this.notifyComponents(theme);
            
            return this;
        },
        
        // 切换主题
        toggleTheme: function() {
            var currentTheme = localStorage.getItem('ood-theme') || 'dark';
            var newTheme = currentTheme === 'light' ? 'dark' : 'light';
            this.applyTheme(newTheme);
            return newTheme;
        },
        
        // 获取当前主题
        getCurrentTheme: function() {
            return localStorage.getItem('ood-theme') || 'dark';
        },
        
        // 通知所有组件切换主题
        notifyComponents: function(theme) {
            // 遍历所有可能的UI组件，应用主题
            var componentTypes = [
                'ood.UI.Panel',
                'ood.UI.Input',
                'ood.UI.ComboInput',
                'ood.UI.ButtonLayout',
                'ood.UI.CheckBox',
                'ood.UI.RadioBox',
                'ood.UI.Label',
                'ood.UI.DatePicker',
                'ood.UI.TimePicker',
                'ood.UI.Dialog',
                'ood.UI.FormLayout',
                'ood.UI.Tabs',
                'ood.UI.ToolBar'
            ];
            
            // 应用主题到所有已知组件
            componentTypes.forEach(function(type) {
                if (window[type] && window[type].setTheme) {
                    window[type].setTheme(theme);
                } else if (window[type] && window[type].each) {
                    window[type].each(function(profile) {
                        if (profile.setTheme) {
                            profile.setTheme(theme);
                        }
                    });
                }
            });
        },
        
        // 创建主题切换按钮
        createToggleButton: function(options) {
            var self = this;
            var opts = ood.merge({
                container: document.body,
                position: 'fixed',
                top: '20px',
                right: '20px',
                zIndex: 1000,
                labelLight: '切换到暗黑模式',
                labelDark: '切换到亮色模式'
            }, options || {});
            
            var button = ood.Dom.create('button', {
                'class': 'ood-theme-toggle',
                'style': {
                    'position': opts.position,
                    'top': opts.top,
                    'right': opts.right,
                    'zIndex': opts.zIndex,
                    'padding': '8px 16px',
                    'backgroundColor': 'var(--ood-primary)',
                    'color': 'white',
                    'border': 'none',
                    'borderRadius': '4px',
                    'cursor': 'pointer',
                    'fontSize': '14px',
                    'transition': 'background-color 0.3s'
                },
                'text': this.getCurrentTheme() === 'dark' ? opts.labelDark : opts.labelLight
            });
            
            ood.Dom.bind(button, 'click', function() {
                var newTheme = self.toggleTheme();
                button.textContent = newTheme === 'dark' ? opts.labelDark : opts.labelLight;
            });
            
            // 添加暗黑模式下的样式
            ood.Dom.styleSheet.addRule('.dark-theme .ood-theme-toggle', {
                'backgroundColor': 'var(--dark-primary)'
            });
            
            ood.Dom.append(opts.container, button);
            
            return button;
        }
    },
    
    Static: {
        // 单例实例
        instance: null,
        
        // 获取单例
        getInstance: function() {
            if (!this.instance) {
                this.instance = new this();
                this.instance.init();
            }
            return this.instance;
        },
        
        // 全局主题切换函数
        setTheme: function(theme) {
            return this.getInstance().applyTheme(theme);
        },
        
        // 全局主题切换
        toggleTheme: function() {
            return this.getInstance().toggleTheme();
        },
        
        // 获取当前主题
        getCurrentTheme: function() {
            return this.getInstance().getCurrentTheme();
        },
        
        // 创建主题切换按钮
        createToggleButton: function(options) {
            return this.getInstance().createToggleButton(options);
        }
    }
});

// 页面加载完成后初始化主题切换器
ood.Dom.ready(function() {
    // 初始化主题切换器
    ood.ThemeSwitcher.getInstance();
    
    // 监听系统主题偏好变化
    if (window.matchMedia) {
        var darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
        darkModeMediaQuery.addEventListener('change', function(e) {
            // 只有在用户没有明确设置过主题时，才根据系统偏好自动切换
            if (!localStorage.getItem('ood-theme')) {
                ood.ThemeSwitcher.setTheme(e.matches ? 'dark' : 'light');
            }
        });
    }
});