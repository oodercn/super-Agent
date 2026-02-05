/**
 * 移动端消息提示组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持多种类型提示、自动消失、位置配置等功能
 */
ood.Class("ood.Mobile.Toast", "ood.UI.Widget", {
    Instance: {
        Initialize: function() {
            this.initToastFeatures();
        },
        
        initToastFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-toast');
            this.initToastState();
        },
        
        initToastState: function() {
            this._isVisible = false;
            this._timer = null;
        },
        
        show: function(message, type, duration) {
            var profile = this.get(0);
            
            // 设置消息内容和类型
            if (message) this.setMessage(message);
            if (type) this.setType(type);
            
            // 设置持续时间
            var showDuration = duration || profile.properties.duration || 3000;
            
            // 显示Toast
            this.showToast();
            
            // 设置自动隐藏
            if (showDuration > 0) {
                this.setAutoHide(showDuration);
            }
        },
        
        hide: function() {
            this.hideToast();
        },
        
        showToast: function() {
            if (this._isVisible) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            this._isVisible = true;
            
            // 显示容器
            root.css('display', 'flex');
            
            // 添加显示动画
            setTimeout(function() {
                root.addClass('ood-mobile-toast-show');
            }, 10);
        },
        
        hideToast: function() {
            if (!this._isVisible) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            this._isVisible = false;
            
            // 清除定时器
            if (this._timer) {
                clearTimeout(this._timer);
                this._timer = null;
            }
            
            // 移除显示动画
            root.removeClass('ood-mobile-toast-show');
            
            // 隐藏容器
            var self = this;
            setTimeout(function() {
                root.css('display', 'none');
                self.onHide();
            }, 300);
        },
        
        setAutoHide: function(duration) {
            var self = this;
            
            // 清除现有定时器
            if (this._timer) {
                clearTimeout(this._timer);
            }
            
            // 设置新定时器
            this._timer = setTimeout(function() {
                self.hide();
            }, duration);
        },
        
        // setMessage: function(message) {
        //     var profile = this.get(0);
        //     var messageNode = profile.getSubNode('MESSAGE');
        //
        //     profile.properties.message = message;
        //     messageNode.html(message || '');
        // },
        
        setType: function(type) {
            var profile = this.get(0);
            var root = profile.getRoot();
            var container = profile.getSubNode('CONTAINER');
            var icon = profile.getSubNode('ICON');
            
            // 移除所有类型类
            container.removeClass('ood-mobile-toast-success ood-mobile-toast-error ood-mobile-toast-warning ood-mobile-toast-info ood-mobile-toast-loading');
            
            // 添加新类型类
            if (type) {
                container.addClass('ood-mobile-toast-' + type);
            }
            
            // 设置图标
            this.setIcon(type);
            
          //  profile.properties.type = type;
        },
        
        setIcon: function(type) {
            var profile = this.get(0);
            var icon = profile.getSubNode('ICON');
            
            // 清除现有图标类
            icon.attr('class', 'ood-mobile-toast-icon');
            
            // 根据类型设置图标
            switch(type) {
                case 'success':
                    icon.addClass('ood-mobile-toast-icon-success');
                    icon.html('✓');
                    break;
                case 'error':
                    icon.addClass('ood-mobile-toast-icon-error');
                    icon.html('✕');
                    break;
                case 'warning':
                    icon.addClass('ood-mobile-toast-icon-warning');
                    icon.html('!');
                    break;
                case 'info':
                    icon.addClass('ood-mobile-toast-icon-info');
                    icon.html('i');
                    break;
                case 'loading':
                    icon.addClass('ood-mobile-toast-icon-loading');
                    icon.html('');
                    break;
                default:
                    icon.html('');
                    break;
            }
        },
        
        // 检查是否为动态组件
        isDynamic: function() {
            return true; // Toast内容会动态显示和隐藏
        },
        
        // 主题变化事件处理
        onThemeChange: function(oldTheme, newTheme) {
            var profile = this.get(0);
            var root = profile.getRoot();
            var container = profile.getSubNode('CONTAINER');
            
            // 移除旧主题类
            root.removeClass('toast-theme-' + oldTheme);
            container.removeClass('toast-theme-' + oldTheme);
            
            // 添加新主题类
            if (newTheme && newTheme !== 'light') {
                root.addClass('toast-theme-' + newTheme);
                container.addClass('toast-theme-' + newTheme);
            }
        },
        
        // 添加屏幕阅读器专用文本
        addScreenReaderText: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var properties = profile.properties;
            
            // 为不同类型的Toast添加语义化说明
            var typeText = '';
            switch(properties.type) {
                case 'success':
                    typeText = '成功消息：';
                    break;
                case 'error':
                    typeText = '错误消息：';
                    break;
                case 'warning':
                    typeText = '警告消息：';
                    break;
                case 'info':
                    typeText = '信息提示：';
                    break;
                case 'loading':
                    typeText = '加载状态：';
                    break;
            }
            
            if (typeText) {
                var srText = ood('<span class="mobile-sr-only">' + typeText + '</span>');
                root.prepend(srText);
            }
        },
        
        // 移动端布局事件
        onMobileLayout: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 移动端调整Toast大小
            container.css('max-width', '90vw');
            container.css('padding', 'var(--mobile-spacing-md) var(--mobile-spacing-lg)');
        },
        
        // 超小屏幕布局事件
        onTinyLayout: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 超小屏幕进一步减小内边距
            container.css('max-width', '95vw');
            container.css('padding', 'var(--mobile-spacing-sm) var(--mobile-spacing-md)');
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Toast组件通常不需要复杂的尺寸调整逻辑
            // 但为了与PC端组件保持一致性，实现一个基本的_onresize方法
            
            var prop = profile.properties,
                root = profile.getRoot(),
                container = profile.getSubNode('CONTAINER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };
            
            // 如果提供了宽度，调整容器的最大宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    // 限制最大宽度为视口宽度的90%
                    var maxWidth = Math.min(pxWidth, window.innerWidth * 0.9);
                    container.css('max-width', adjustunit(maxWidth));
                }
            }
            
            // 如果提供了高度，可以相应地调整容器
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    container.css('height', adjustunit(pxHeight));
                }
            }
        },
        
        onHide: function() {
            var profile = this.get(0);
            
            if (profile.onHide) {
                profile.boxing().onHide(profile);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-toast ood-mobile-toast-{position}',
            //style: 'display: none; {_style}',
            style: ' {_style}',
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-toast-container ood-mobile-toast-{type}',
                
                ICON: {
                    tagName: 'div',
                    className: 'ood-mobile-toast-icon',
                    style: 'display: {_iconDisplay}'
                },
                
                MESSAGE: {
                    tagName: 'div',
                    className: 'ood-mobile-toast-message',
                    text: '{message}'
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'fixed',
                'z-index': 2000,
                'pointer-events': 'none',
                opacity: 0,
                transform: 'translateY(-20px)',
                transition: 'all 0.3s ease'
            },
            
            'KEY.ood-mobile-toast-top': {
                top: 'var(--mobile-safe-top, 20px)',
                left: '50%',
                transform: 'translateX(-50%) translateY(-20px)'
            },
            
            'KEY.ood-mobile-toast-center': {
                top: '50%',
                left: '50%',
                transform: 'translateX(-50%) translateY(-50%)'
            },
            
            'KEY.ood-mobile-toast-bottom': {
                bottom: 'calc(var(--mobile-safe-bottom, 20px) + 20px)',
                left: '50%',
                transform: 'translateX(-50%) translateY(20px)'
            },
            
            'KEY.ood-mobile-toast-show': {
                opacity: 1,
                transform: 'translateX(-50%) translateY(0)'
            },
            
            'KEY.ood-mobile-toast-center.ood-mobile-toast-show': {
                transform: 'translateX(-50%) translateY(-50%)'
            },
            
            CONTAINER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                gap: 'var(--mobile-spacing-sm)',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)',
                'background-color': 'rgba(0,0,0,0.8)',
                color: 'var(--mobile-text-inverse)',
                'border-radius': 'var(--mobile-border-radius)',
                'max-width': '80vw',
                'min-width': '120px',
                'box-shadow': 'var(--mobile-shadow-medium)',
                'pointer-events': 'auto',
                role: 'alert',
                'aria-live': 'assertive'
            },
            
            'CONTAINER.ood-mobile-toast-success': {
                'background-color': 'var(--mobile-success)'
            },
            
            'CONTAINER.ood-mobile-toast-error': {
                'background-color': 'var(--mobile-danger)'
            },
            
            'CONTAINER.ood-mobile-toast-warning': {
                'background-color': 'var(--mobile-warning)'
            },
            
            'CONTAINER.ood-mobile-toast-info': {
                'background-color': 'var(--mobile-info)'
            },
            
            'CONTAINER.ood-mobile-toast-loading': {
                'background-color': 'rgba(0,0,0,0.8)'
            },
            
            // 主题支持 - 暗黑模式
            'CONTAINER.toast-theme-dark': {
                'background-color': 'var(--mobile-bg-tertiary)',
                color: 'var(--mobile-text-primary)',
                'border': '1px solid var(--mobile-border-color)'
            },
            
            'CONTAINER.toast-theme-dark.ood-mobile-toast-success': {
                'background-color': 'var(--mobile-success)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'CONTAINER.toast-theme-dark.ood-mobile-toast-error': {
                'background-color': 'var(--mobile-danger)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'CONTAINER.toast-theme-dark.ood-mobile-toast-warning': {
                'background-color': 'var(--mobile-warning)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'CONTAINER.toast-theme-dark.ood-mobile-toast-info': {
                'background-color': 'var(--mobile-info)',
                color: 'var(--mobile-text-inverse)'
            },
            
            // 主题支持 - 高对比度
            'CONTAINER.toast-theme-light-hc, CONTAINER.toast-theme-dark-hc': {
                'border-width': 'var(--mobile-border-width)',
                'border-style': 'solid',
                'font-weight': 'bold'
            },
            
            // 移动端响应式样式
            'KEY.mobile-responsive CONTAINER': {
                'max-width': '90vw',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)'
            },
            
            'KEY.mobile-tiny CONTAINER': {
                'max-width': '95vw',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-sm)'
            },
            
            ICON: {
                'font-size': '16px',
                'font-weight': 'bold',
                'line-height': '1',
                'flex-shrink': 0
            },
            
            'ICON.ood-mobile-toast-icon-loading': {
                width: '16px',
                height: '16px',
                border: '2px solid rgba(255,255,255,0.3)',
                'border-top-color': '#FFFFFF',
                'border-radius': '50%',
                animation: 'ood-mobile-toast-spin 1s linear infinite'
            },
            
            MESSAGE: {
                'font-size': 'var(--mobile-font-md)',
                'line-height': '1.4',
                'white-space': 'pre-wrap',
                'word-break': 'break-word'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '提示框标题',
                ini: '提示框',
                action: function(value) {
                    var profile = this;
                    // 同步更新message属性作为显示内容
                    if (!profile.properties.message) {
                        profile.properties.message = value;
                        this.boxing().setMessage(value);
                    }
                    profile.getRoot().attr('aria-label', value || '提示框');
                }
            },
            
            width: {
                caption: '提示框宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            height: {
                caption: '提示框高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var toastNode = this.getSubNode('TOAST');
                    if (value && toastNode && !toastNode.isEmpty()) {
                        toastNode.css('background-color', value);
                    }
                }
            },
            
            textColor: {
                caption: '文字颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var messageNode = this.getSubNode('MESSAGE');
                    if (value && messageNode && !messageNode.isEmpty()) {
                        messageNode.css('color', value);
                    }
                }
            },
            
            iconColor: {
                caption: '图标颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var iconNode = this.getSubNode('ICON');
                    if (value && iconNode && !iconNode.isEmpty()) {
                        iconNode.css('color', value);
                    }
                }
            },
            
            // ===== 提示框特有属性 =====
            // 继承基类的主题和可访问性属性
            theme: {
                caption: '主题模式',
                ini: 'light',
                listbox: ['light', 'dark', 'light-hc', 'dark-hc'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            responsive: {
                caption: '响应式布局',
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            message: {
                caption: '提示消息',
                ini: '',
                action: function(value) {
                    this.boxing().setMessage(value);
                }
            },
            
            type: {
                caption: '消息类型',
                ini: 'info',
                listbox: ['success', 'error', 'warning', 'info', 'loading'],
                action: function(value) {
                    this.boxing().setType(value);
                }
            },
            
            position: {
                caption: '显示位置',
                ini: 'center',
                listbox: ['top', 'center', 'bottom']
            },
            
            duration: {
                caption: '显示时长（毫秒）',
                ini: 3000
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            data.message = props.message;
            data.caption=props.caption;
            data.position=props.position;
            data.type=props.type;
            data._iconDisplay = props.type ? 'block' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onHide: function(profile) {
                // Toast隐藏事件处理器
            }
        },
        
        // 静态方法：快速显示Toast
        show: function(message, type, duration) {
            var toast = new ood.Mobile.Toast({
                message: message,
                type: type || 'info',
                duration: duration || 3000
            });
            
            // 添加到body
            toast.render(document.body);
            toast.show();
            
            return toast;
        },
        
        success: function(message, duration) {
            return this.show(message, 'success', duration);
        },
        
        error: function(message, duration) {
            return this.show(message, 'error', duration);
        },
        
        warning: function(message, duration) {
            return this.show(message, 'warning', duration);
        },
        
        info: function(message, duration) {
            return this.show(message, 'info', duration);
        },
        
        loading: function(message) {
            return this.show(message || '加载中...', 'loading', 0);
        }
    }
});

// 添加CSS动画
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        @keyframes ood-mobile-toast-spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    `;
    document.head.appendChild(style);
}