/**
 * 移动端按钮组件
 * 继承自 ood.UI（UI基类）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持多种按钮类型、尺寸、状态和触摸交互
 */
ood.Class("ood.Mobile.Button", ["ood.UI", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['CONTENT', 'ICON', 'TEXT', 'LOADER', 'RIPPLE']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            width: '4em',
            height: '2em',
            caption: '按钮',
            text: '按钮',
            type: 'primary',
            theme: 'light',
            responsive: true
        },

        // 重写 _setCtrlValue 方法 - 控制按钮的视觉状态
        _setCtrlValue: function(value) {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var properties = profile.properties;
                
                // 根据按钮类型处理值
                if (properties.type === 'status' || properties.type === 'toggle') {
                    // 状态按钮：设置选中状态
                    if (value) {
                        root.addClass('ood-mobile-button-active');
                        root.attr('aria-pressed', 'true');
                    } else {
                        root.removeClass('ood-mobile-button-active');
                        root.attr('aria-pressed', 'false');
                    }
                }
                
                // 更新按钮文本（如果有文本映射）
                if (properties.textMap) {
                    var textNode = profile.getSubNode('TEXT');
                    var newText = value ? properties.textMap.active : properties.textMap.inactive;
                    if (newText) {
                        textNode.html(newText);
                    }
                }
            });
        },
        
        // 重写 _getCtrlValue 方法 - 获取按钮当前状态值
        _getCtrlValue: function() {
            var profile = this.get(0);
            if (!profile) return null;
            
            var properties = profile.properties;
            if (properties.type === 'status' || properties.type === 'toggle') {
                var root = profile.getRoot();
                return root.hasClass('ood-mobile-button-active');
            }
            
            return properties.$UIvalue;
        },
        
        // 激活按钮（聚焦）
        activate: function() {
            this.getRoot().focus(true);
            return this;
        },
        
        initButtonFeatures: function() {
            // 原有的按钮特性初始化逻辑
            this.initRippleEffect();
            this.initHapticFeedback();
        },
        
        // 组件初始化
        Initialize: function() {
            // 调用父类初始化
          //  this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端按钮特性
            this.initMobileButtonFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileButtonFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端按钮CSS类
            profile.getRoot().addClass('ood-mobile-button ood-mobile-component');
            
            // 初始化按钮功能
            this.initButtonFeatures();
            
            // 初始化触摸事件
            this.bindTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        initResponsive: function() {
            // 响应式布局初始化
            var profile = this.get(0);
            if (!profile) return;
            
            this.adjustLayout();
        },
        
        initAccessibility: function() {
            // 可访问性初始化
            var profile = this.get(0);
            if (!profile) return;
            
            var root = profile.getRoot();
            
            // 基础ARIA属性
            root.attr({
                'role': 'button',
                'tabindex': '0'
            });
            
            // 状态按钮的ARIA属性
            if (profile.properties.type === 'toggle' || profile.properties.type === 'status') {
                root.attr('aria-pressed', profile.properties.checked ? 'true' : 'false');
            }
        },
        
        adjustLayout: function() {
            // 响应式布局调整
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
                
                // 清除旧的尺寸类
                root.removeClass('button-xs button-sm button-md button-lg button-xl');
                
                // 添加当前尺寸类
                root.addClass('button-' + screenSize);
            });
        },
        
        // 绑定触摸事件 - 符合 ood 事件规范
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 注册 ood 事件处理器
            this.registerEventHandlers();
        },
        
        // 注册 ood 事件处理器
        registerEventHandlers: function() {
            var self = this;
            var profile = this.get(0);
            
            // 触摸开始事件 - 符合 ood 三阶段事件机制
            profile.beforeTouchstart = function(profile, e, src) {
                if (profile.properties.disabled) return false;
                return true;
            };
            
            profile.onTouchstart = function(profile, e, src) {
                var root = profile.getRoot();
                root.addClass('ood-mobile-button-pressed');
                self.showRipple(e);
              //  self.triggerHapticFeedback('light');
            };
            
            profile.afterTouchstart = function(profile, e, src) {
                // 触摸开始后的处理
            };
            
            // 触摸结束事件
            profile.beforeTouchend = function(profile, e, src) {
                if (profile.properties.disabled) return false;
                return true;
            };
            
            profile.onTouchend = function(profile, e, src) {
                var root = profile.getRoot();
                root.removeClass('ood-mobile-button-pressed');
                
                // 延迟移除波纹效果
                setTimeout(function() {
                    self.hideRipple();
                }, 300);
            };
            
            profile.afterTouchend = function(profile, e, src) {
                // 触摸结束后的处理
            };
            
            // 点击事件 - 符合 ood 三阶段事件机制
            profile.beforeClick = function(profile, e, src) {
                if (profile.properties.disabled) {
                    return false; // 阻止事件继续
                }
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                // 主要点击处理逻辑
                self.handleButtonClick(profile, e);
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理，如统计、日志等
                self.afterButtonClick(profile, e);
            };
        },
        
        // 检查是否为交互式组件
        isInteractive: function() {
            return true;
        },
        
        // 激活事件处理（键盘激活）
        onActivate: function(e) {
            if (!this.get(0).properties.disabled) {
                this.onButtonClick(e);
            }
        },
        
        // 主题变化事件处理
        onThemeChange: function(oldTheme, newTheme) {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 移除旧主题类
            if (oldTheme) {
                root.removeClass('button-theme-' + oldTheme);
            }
            
            // 添加新主题类
            if (newTheme && newTheme !== 'light') {
                root.addClass('button-theme-' + newTheme);
            }
        },
        
        // 移动端布局事件
        onMobileLayout: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 移动端增大触摸目标
            root.css('min-height', 'var(--mobile-touch-target-lg)');
            root.css('font-size', 'var(--mobile-font-lg)');
        },
        
        // 桌面端布局事件
        onDesktopLayout: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 恢复默认尺寸
            root.css('min-height', 'var(--mobile-touch-target)');
            root.css('font-size', 'var(--mobile-font-md)');
        },
        
        // 添加屏幕阅读器专用文本
        addScreenReaderText: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var properties = profile.properties;
            
            // 为加载状态添加屏幕阅读器文本
            if (properties.loading) {
                var srText = ood('<span class="mobile-sr-only">加载中...</span>');
                root.append(srText);
            }
            
            // 为禁用状态添加说明
            if (properties.disabled) {
                var srDisabled = ood('<span class="mobile-sr-only">按钮已禁用</span>');
                root.append(srDisabled);
            }
        },
        
        // 初始化波纹效果
        initRippleEffect: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            if (profile.properties.ripple !== false) {
                root.addClass('ood-mobile-button-ripple');
            }
        },
        
        // 显示波纹效果
        showRipple: function(e) {
            var profile = this.get(0);
            if (!profile.properties.ripple) return;
            
            var root = profile.getRoot();
            var rect = root.get(0).getBoundingClientRect();
            var size = Math.max(rect.width, rect.height);
            var x = (e.touches ? e.touches[0].clientX : e.clientX) - rect.left - size / 2;
            var y = (e.touches ? e.touches[0].clientY : e.clientY) - rect.top - size / 2;
            
            var ripple = ood('<div class="ood-mobile-button-ripple-effect"></div>');
            ripple.css({
                width: size + 'px',
                height: size + 'px',
                left: x + 'px',
                top: y + 'px'
            });
            
            root.append(ripple);
            profile._currentRipple = ripple;
        },
        
        // 隐藏波纹效果
        hideRipple: function() {
            var profile = this.get(0);
            if (profile._currentRipple) {
                profile._currentRipple.remove();
                delete profile._currentRipple;
            }
        },
        
        // 初始化触觉反馈
        initHapticFeedback: function() {
            // 检查浏览器是否支持触觉反馈
            this._supportsHaptic = 'vibrate' in navigator;
        },
        
        // 触发触觉反馈
        triggerHapticFeedback: function(type) {
            if (!this._supportsHaptic) return;
            
            var pattern;
            switch(type) {
                case 'light':
                    pattern = [10];
                    break;
                case 'medium':
                    pattern = [20];
                    break;
                case 'heavy':
                    pattern = [30];
                    break;
                default:
                    pattern = [10];
            }
            
            navigator.vibrate(pattern);
        },
        
        // 按钮点击主要处理逻辑 - 符合 ood 规范
        handleButtonClick: function(profile, e) {
            // 执行点击动作
            if (profile.properties.action && typeof profile.properties.action === 'function') {
                profile.properties.action.call(this, e);
            }
            
            // 触发用户自定义事件
            if (profile.properties.onClick && typeof profile.properties.onClick === 'function') {
                profile.properties.onClick.call(this, profile, e);
            }
        },
        
        // 点击后处理 - 符合 ood 规范
        afterButtonClick: function(profile, e) {
            // 点击后的统计、日志等处理
            if (profile.properties.onAfterClick && typeof profile.properties.onAfterClick === 'function') {
                profile.properties.onAfterClick.call(this, profile, e);
            }
        },
        
        // 触发 ood 事件
        fireOodEvent: function(eventName, eventData) {
            var profile = this.get(0);
            if (profile && profile.host) {
                // 使用 ood 的事件系统触发事件
                ood.Event.fireEvent(profile.host, eventName, eventData);
            }
        },
        
        // 设置按钮状态
        setDisabled: function(disabled) {
            return this.each(function(profile) {
                profile.properties.disabled = disabled;
                var root = profile.getRoot();
                
                if (disabled) {
                    root.addClass('ood-mobile-button-disabled');
                    root.attr('aria-disabled', 'true');
                } else {
                    root.removeClass('ood-mobile-button-disabled');
                    root.attr('aria-disabled', 'false');
                }
            });
        },
        
        // 设置加载状态
        setLoading: function(loading) {
            return this.each(function(profile) {
                profile.properties.loading = loading;
                var root = profile.getRoot();
                var content = profile.getSubNode('CONTENT');
                var loader = profile.getSubNode('LOADER');
                
                if (loading) {
                    root.addClass('ood-mobile-button-loading');
                    content.css('opacity', '0');
                    loader.css('display', 'block');
                } else {
                    root.removeClass('ood-mobile-button-loading');
                    content.css('opacity', '1');
                    loader.css('display', 'none');
                }
            });
        },
        
        // 获取按钮文本
        getText: function() {
            var profile = this.get(0);
            return profile.properties.text || '';
        },
        
        // 重写 resetValue 方法
        resetValue: function(value) {
            // 调用父类方法
            var upper = arguments.callee.upper;
            var rtn = upper.apply(this, arguments);
            
            // 移动端特定的重置逻辑
            this.each(function(profile) {
                var root = profile.getRoot();
                root.removeClass('ood-mobile-button-active ood-mobile-button-pressed');
            });
            
            return rtn;
        },
        
        // 重写 resetValue 方法
        resetValue: function(value) {
            // 调用父类方法
            var upper = arguments.callee.upper;
            var rtn = upper.apply(this, arguments);
            
            // 移动端特定的重置逻辑
            this.each(function(profile) {
                var root = profile.getRoot();
                root.removeClass('ood-mobile-button-active ood-mobile-button-pressed');
            });
            
            return rtn;
        }
    },
    
    Static: {
        // 模板定义
        Templates: {
            tagName: 'button',
            className: 'ood-mobile-button {_typeClass} {_sizeClass} {_shapeClass}',
            style: '{_style}',
            type: 'button',
            'aria-label': '{text}',
            role: 'button',
            
            CONTENT: {
                tagName: 'div',
                className: 'ood-mobile-button-content',
                
                ICON: {
                    tagName: 'i',
                    className: 'ood-mobile-button-icon {iconClass}',
                    style: 'display: {_iconDisplay}'
                },
                
                TEXT: {
                    tagName: 'span',
                    className: 'ood-mobile-button-text',
                    text: '{text}'
                }
            },
            
            LOADER: {
                tagName: 'div',
                className: 'ood-mobile-button-loader',
                style: 'display: none',
                
                SPINNER: {
                    tagName: 'div',
                    className: 'ood-mobile-button-spinner'
                }
            }
        },
        
        // 外观样式定义
        Appearances: {
            KEY: {
                position: 'relative',
                display: 'inline-flex',
                'align-items': 'center',
                'justify-content': 'center',
                'font-family': 'inherit',
                'font-weight': '500',
                'text-align': 'center',
                'text-decoration': 'none',
                'vertical-align': 'middle',
                'user-select': 'none',
                border: 'none',
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                outline: 'none',
                overflow: 'hidden',
                
                // 默认样式
                'background-color': 'var(--mobile-primary)',
                color: '#FFFFFF',
                'border-radius': 'var(--mobile-border-radius)',
                'min-height': 'var(--mobile-touch-target)',
                padding: '0 var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-md)',
                'line-height': '1.4'
            },
            
            // 按钮内容
            CONTENT: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                gap: 'var(--mobile-spacing-xs)',
                transition: 'opacity 0.2s ease-in-out'
            },
            
            // 图标
            ICON: {
                'font-size': '1em',
                'line-height': '1'
            },
            
            // 文本
            TEXT: {
                'white-space': 'nowrap'
            },
            
            // 加载器
            LOADER: {
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)'
            },
            
            // 加载动画
            SPINNER: {
                width: '16px',
                height: '16px',
                border: '2px solid rgba(255,255,255,0.3)',
                'border-top-color': '#FFFFFF',
                'border-radius': '50%',
                animation: 'ood-mobile-button-spin 1s linear infinite'
            },
            
            // 按钮类型样式
            'KEY.ood-mobile-button-primary': {
                'background-color': 'var(--mobile-primary)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'KEY.ood-mobile-button-secondary': {
                'background-color': 'var(--mobile-secondary)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'KEY.ood-mobile-button-success': {
                'background-color': 'var(--mobile-success)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'KEY.ood-mobile-button-warning': {
                'background-color': 'var(--mobile-warning)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'KEY.ood-mobile-button-danger': {
                'background-color': 'var(--mobile-danger)',
                color: 'var(--mobile-text-inverse)'
            },
            
            'KEY.ood-mobile-button-ghost': {
                'background-color': 'transparent',
                color: 'var(--mobile-primary)',
                border: '1px solid var(--mobile-primary)'
            },
            
            'KEY.ood-mobile-button-link': {
                'background-color': 'transparent',
                color: 'var(--mobile-primary)',
                border: 'none',
                'text-decoration': 'underline'
            },
            
            // 主题支持 - 暗黑模式
            'KEY.button-theme-dark.ood-mobile-button-ghost': {
                color: 'var(--mobile-text-primary)',
                'border-color': 'var(--mobile-text-primary)'
            },
            
            'KEY.button-theme-dark.ood-mobile-button-link': {
                color: 'var(--mobile-text-primary)'
            },
            
            // 主题支持 - 高对比度
            'KEY.button-theme-light-hc, KEY.button-theme-dark-hc': {
                'border-width': 'var(--mobile-border-width)',
                'font-weight': 'bold'
            },
            
            'KEY.button-theme-light-hc.ood-mobile-button-ghost, KEY.button-theme-dark-hc.ood-mobile-button-ghost': {
                'border-width': '3px'
            },
            
            // 按钮尺寸样式
            'KEY.ood-mobile-button-xs': {
                'min-height': '28px',
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-xs)'
            },
            
            'KEY.ood-mobile-button-sm': {
                'min-height': '32px',
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)'
            },
            
            'KEY.ood-mobile-button-lg': {
                'min-height': '48px',
                padding: '0 var(--mobile-spacing-lg)',
                'font-size': 'var(--mobile-font-lg)'
            },
            
            'KEY.ood-mobile-button-xl': {
                'min-height': '52px',
                padding: '0 var(--mobile-spacing-xl)',
                'font-size': 'var(--mobile-font-xl)'
            },
            
            // 按钮形状样式
            'KEY.ood-mobile-button-round': {
                'border-radius': '999px'
            },
            
            'KEY.ood-mobile-button-square': {
                'border-radius': '0'
            },
            
            'KEY.ood-mobile-button-circle': {
                'border-radius': '50%',
                'aspect-ratio': '1',
                padding: '0'
            },
            
            // 禁用状态
            'KEY.ood-mobile-button-disabled': {
                opacity: 'var(--mobile-disabled-opacity, 0.5)',
                cursor: 'not-allowed',
                'pointer-events': 'none',
                'background-color': 'var(--mobile-disabled-bg)',
                color: 'var(--mobile-disabled-text)'
            },
            
            // 按下状态
            'KEY.ood-mobile-button-pressed': {
                transform: 'scale(0.98)',
                opacity: '0.8'
            },
            
            // 激活状态（状态按钮）
            'KEY.ood-mobile-button-active': {
                'background-color': 'var(--mobile-primary-dark)',
                transform: 'none'
            },
            
            // 按钮悬停状态
            'KEY:hover:not(.ood-mobile-button-disabled)': {
                'background-color': 'var(--mobile-hover-bg)',
                transform: 'translateY(-1px)',
                'box-shadow': 'var(--mobile-shadow-medium)'
            },
            
            // 聚焦状态
            'KEY:focus-visible': {
                outline: '2px solid var(--mobile-primary)',
                'outline-offset': '2px'
            },
            
            // 高对比度聚焦状态
            'KEY.button-theme-light-hc:focus-visible, KEY.button-theme-dark-hc:focus-visible': {
                outline: '3px solid var(--mobile-primary)',
                'outline-offset': '3px'
            },
            
            // 移动端响应式样式
            'KEY.mobile-responsive': {
                'min-height': 'var(--mobile-touch-target-lg)',
                'font-size': 'var(--mobile-font-lg)',
                padding: '0 var(--mobile-spacing-lg)'
            },
            
            'KEY.mobile-tiny': {
                'font-size': 'var(--mobile-font-md)',
                padding: '0 var(--mobile-spacing-md)'
            },
            
            // 波纹效果
            'KEY.ood-mobile-button-ripple': {
                overflow: 'hidden'
            },
            
            '.ood-mobile-button-ripple-effect': {
                position: 'absolute',
                'background-color': 'rgba(255,255,255,0.3)',
                'border-radius': '50%',
                transform: 'scale(0)',
                animation: 'ood-mobile-button-ripple 0.6s linear',
                'pointer-events': 'none'
            }
        },
        
        // 行为定义
        Behaviors: {
            HotKeyAllowed: true,
            DroppableKeys: [],
            NavKeys: {KEY: 1},
            onClick: function(profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                
                var b = profile.boxing();
                
                // 状态按钮处理
                if (p.type === 'status' || p.type === 'toggle') {
                    if (p.readonly) return false;
                    var newValue = !p.$UIvalue;
                    b.setUIValue(newValue, null, null, 'click');
                    
                    // 触发状态改变事件
                    if (profile.onChecked) {
                        b.onChecked(profile, e, newValue);
                    }
                }
                
                // 普通点击事件
                if (profile.onClick) {
                    return b.onClick(profile, e, src, p.$UIvalue);
                }
            },
            
            // 键盘事件支持
            onKeydown: function(profile, e, src) {
                var keys = ood.Event.getKey(e), key = keys.key;
                if (key == ' ' || key == 'enter') {
                    profile.getSubNode('KEY').afterMousedown();
                    profile.__fakeclick = 1;
                }
            },
            
            onKeyup: function(profile, e, src) {
                var keys = ood.Event.getKey(e), key = keys.key;
                if (key == ' ' || key == 'enter') {
                    profile.getSubNode('KEY').afterMouseup();
                    if (profile.__fakeclick) {
                        ood.use(src).onClick();
                    }
                }
                delete profile.__fakeclick;
            }
        },
        
        // 数据模型
        DataModel: {
            // ===== 基础必需属性 =====
            // 按钮文本（显示值）- 对应模板中的 {text}
            text: {
                caption: '按钮文本',
                ini: '按钮',
                action: function(value) {
                    var profile = this;
                    var textNode = profile.getSubNode('TEXT');
                    if (textNode && !textNode.isEmpty()) {
                        textNode.html(value || '');
                    }
                    // 更新 aria-label
                    profile.getRoot().attr('aria-label', value || '');
                }
            },
            
            // 按钮宽度
            width: {
                caption: '按钮宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // 按钮高度  
            height: {
                caption: '按钮高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 按钮图标（图标选择器）
            icon: {
                caption: '按钮图标',
                ini: '',
                combobox: function() {
                    // 返回图标选择器
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var iconNode = profile.getSubNode('ICON');
                    
                    if (value) {
                        iconNode.attr('class', 'ood-mobile-button-icon ' + value);
                        iconNode.css('display', 'inline-block');
                    } else {
                        iconNode.css('display', 'none');
                    }
                }
            },
            
            // 按钮背景色（颜色选择器）
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    // 返回颜色选择器
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            // 按钮文字色（颜色选择器）
            textColor: {
                caption: '文字颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('color', value);
                    }
                }
            },
            
            // ===== 继承基类的主题和可访问性属性 =====
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
            
            ariaLabel: {
                caption: 'ARIA标签',
                ini: '',
                action: function(value) {
                    this.getRoot().attr('aria-label', value || this.properties.text);
                }
            },
            
            // 继承 ood.absValue 的 value 属性
            value: {
                caption: '按钮值',
                ini: false
                // set 方法由 ood.absValue 提供
            },
            
            // 文本映射（状态按钮）
            textMap: {
                caption: '状态文本映射',
                ini: null
            },
            
            // 按钮类型
            type: {
                caption: '按钮类型',
                ini: 'primary',
                listbox: ['primary', 'secondary', 'success', 'warning', 'danger', 'ghost', 'link', 'status', 'toggle'],
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    
                    // 移除所有类型类
                    root.removeClass('ood-mobile-button-primary ood-mobile-button-secondary ood-mobile-button-success ood-mobile-button-warning ood-mobile-button-danger ood-mobile-button-ghost ood-mobile-button-link ood-mobile-button-status ood-mobile-button-toggle');
                    
                    // 添加新类型类
                    if (value) {
                        root.addClass('ood-mobile-button-' + value);
                    }
                    
                    // 状态按钮需要特殊处理
                    if (value === 'status' || value === 'toggle') {
                        root.attr('aria-pressed', profile.properties.$UIvalue ? 'true' : 'false');
                    } else {
                        root.removeAttr('aria-pressed');
                    }
                }
            },
            
            // 按钮尺寸
            size: {
                caption: '按钮尺寸',
                ini: 'md',
                listbox: ['xs', 'sm', 'md', 'lg', 'xl'],
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    
                    // 移除所有尺寸类
                    root.removeClass('ood-mobile-button-xs ood-mobile-button-sm ood-mobile-button-lg ood-mobile-button-xl');
                    
                    // 添加新尺寸类
                    if (value && value !== 'md') {
                        root.addClass('ood-mobile-button-' + value);
                    }
                }
            },
            
            // 按钮形状
            shape: {
                caption: '按钮形状',
                ini: 'default',
                listbox: ['default', 'round', 'square', 'circle'],
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    
                    // 移除所有形状类
                    root.removeClass('ood-mobile-button-round ood-mobile-button-square ood-mobile-button-circle');
                    
                    // 添加新形状类
                    if (value && value !== 'default') {
                        root.addClass('ood-mobile-button-' + value);
                    }
                }
            },
            
            // 是否禁用
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    this.boxing().setDisabled(value);
                }
            },
            
            // 继承 ood.absValue 的表单字段属性
            isFormField: {
                caption: '是否为表单字段',
                ini: true
            },
            
            readonly: {
                caption: '只读状态',
                ini: false
            },
            
            // 是否加载中
            loading: {
                caption: '加载状态',
                ini: false,
                action: function(value) {
                    this.boxing().setLoading(value);
                }
            },
            
            // 是否启用波纹效果
            ripple: {
                caption: '波纹效果',
                ini: true
            },
            
            // 点击动作
            action: {
                caption: '点击动作',
                ini: null
            },
            
            onAfterClick: {
                caption: '点击后事件处理器', 
                ini: null
            }
        },
        
        // 表单字段检查
        _isFormField: function(profile) {
            return (profile.properties.type === 'status' || profile.properties.type === 'toggle') && 
                   profile.properties.isFormField;
        },
        
        // 值确保方法
        _ensureValue: function(profile, value) {
            if (profile.properties.type === 'status' || profile.properties.type === 'toggle') {
                return !!value; // 状态按钮确保布尔值
            }
            return value;
        },
        
        // 渲染触发器
        RenderTrigger: function() {
            var profile = this;
            
            // 初始化按钮状态
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },
        
        // 数据准备
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var properties = profile.properties;
            
            // 设置类型类
            data._typeClass = properties.type ? 'ood-mobile-button-' + properties.type : 'ood-mobile-button-primary';
            
            // 设置尺寸类
            data._sizeClass = properties.size && properties.size !== 'md' ? 'ood-mobile-button-' + properties.size : '';
            
            // 设置形状类
            data._shapeClass = properties.shape && properties.shape !== 'default' ? 'ood-mobile-button-' + properties.shape : '';
            
            // 设置图标显示
            data._iconDisplay = properties.icon ? 'inline-block' : 'none';
            
            // 设置文本
            data.text = properties.text || properties.caption || '';
            
            // 设置图标类
            data.iconClass = properties.icon ? properties.icon : '';
            
            // 设置加载状态
            data._loadingDisplay = properties.loading ? 'block' : 'none';
            
            // 设置禁用状态
            data._disabled = properties.disabled ? 'disabled' : '';
            
            // 设置只读状态
            data._readonly = properties.readonly ? 'readonly' : '';
            
            // 设置主题类
            data._themeClass = properties.theme && properties.theme !== 'light' ? 'button-theme-' + properties.theme : '';
            
            // 设置响应式类
            data._responsiveClass = properties.responsive ? 'mobile-responsive' : '';
            
            // 设置ARIA标签
            data._ariaLabel = properties.ariaLabel || properties.text || properties.caption || '按钮';
            
            // 设置样式
            data._style = properties.style || '';
            
            return data;
        },
        
        // 事件处理器
        EventHandlers: {
            // 点击事件处理
            onClick: function(profile, e, src, value) {
                // 点击事件处理
            },
            
            onChecked: function(profile, e, value) {
                // 状态改变事件处理
            },
            
            // 继承 ood.absValue 的事件处理器
            beforeValueSet: function(profile, oldValue, newValue, force, tag) {
                // 值设置前的处理
            },
            
            afterValueSet: function(profile, oldValue, newValue, force, tag) {
                // 值设置后的处理
            },
            
            onChange: function(profile, oldValue, newValue, force, tag) {
                // 值改变处理
            },
            
            // 继承 ood.UI 的事件处理器
            beforeRender: function(profile) {
                // 渲染前处理
            },
            
            onRender: function(profile) {
                // 渲染后处理
            },
            
            beforeDestroy: function(profile) {
                // 销毁前处理
            },
            
            afterDestroy: function(profile) {
                // 销毁后处理
            }
        }
    }
});

// 添加Getter/Setter方法
(function() {
    var proto = ood.Mobile.Button.prototype;

    // 响应式调整大小事件处理
    proto._onresize = function(profile, width, height) {
        // Button组件通常不需要复杂的尺寸调整逻辑
        // 但为了与PC端组件保持一致性，实现一个基本的_onresize方法

        var prop = profile.properties,
            root = profile.getRoot(),
            // 获取单位转换函数
            us = ood.$us(profile),
            adjustunit = function(v, emRate) {
                return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
            };

        // 如果提供了宽度，调整按钮宽度
        if (width && width !== 'auto') {
            // 转换为像素值进行计算
            var pxWidth = profile.$px(width, null, true);
            if (pxWidth) {
                root.css('width', adjustunit(pxWidth));
            }
        }

        // 如果提供了高度，调整按钮高度
        if (height && height !== 'auto') {
            var pxHeight = profile.$px(height, null, true);
            if (pxHeight) {
                root.css('height', adjustunit(pxHeight));
            }
        }

        // 调整内部内容以适应新尺寸
        this.adjustLayout();
    };
})();
// 添加CSS动画关键帧
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        @keyframes ood-mobile-button-spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        @keyframes ood-mobile-button-ripple {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);
}