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
}/**
 * 移动端输入框组件
 * 继承自 ood.UI（UI基类）和 ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持多种输入类型、验证、格式化和移动端优化
 */
ood.Class("ood.Mobile.Input", ["ood.UI", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['CONTAINER', 'LABEL', 'INPUT', 'CLEAR', 'HELP', 'ERROR']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            width: '18em',
            height: '2.5em',
            caption: '输入框',
            placeholder: '请输入内容',
            type: 'text',
            theme: 'light',
            responsive: true
        },

        Initialize: function() {
            // 调用父类初始化
         //   this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端输入框特性
            this.initMobileInputFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileInputFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端输入框CSS类
            profile.getRoot().addClass('ood-mobile-input ood-mobile-component');
            
            // 初始化输入框功能
            this.initInputFeatures();
            
            // 初始化触摸事件
            this.bindTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // 响应式设计初始化
        initResponsive: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var self = this;
            
            // 初始调整布局
            this.adjustLayout();
            
            // 监听窗口大小变化
            if (window.addEventListener) {
                var resizeHandler = ood.Mobile.utils.debounce(function() {
                    self.adjustLayout();
                }, 300);
                
                window.addEventListener('resize', resizeHandler);
                window.addEventListener('orientationchange', resizeHandler);
                
                // 在组件销毁时清理事件监听
                profile.$beforeDestroy = profile.$beforeDestroy || [];
                profile.$beforeDestroy.push(function() {
                    window.removeEventListener('resize', resizeHandler);
                    window.removeEventListener('orientationchange', resizeHandler);
                });
            }
        },
        
        // 可访问性初始化
        initAccessibility: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var input = profile.getSubNode('INPUT');
            var label = profile.getSubNode('LABEL');
            
            // 为输入框添加ARIA属性
            if (input && !input.isEmpty()) {
                var inputId = profile.getDomId() + '_input';
                input.attr({
                    'id': inputId,
                    'aria-describedby': profile.getDomId() + '_help',
                    'aria-invalid': 'false'
                });
                
                // 关联标签
                if (label && !label.isEmpty()) {
                    label.attr('for', inputId);
                }
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                
                // 清除旧的尺寸类
                root.removeClass('input-xs input-sm input-md input-lg input-xl');
                
                // 添加当前尺寸类
                root.addClass('input-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('input-tiny');
                } else {
                    root.removeClass('input-tiny');
                }
            });
        },
        // 重写 _setCtrlValue 方法 - 设置输入框的值
        _setCtrlValue: function(value) {
            return this.each(function(profile) {
                var inputNode = profile.getSubNode('INPUT');
                if (inputNode && inputNode.get(0)) {
                    inputNode.get(0).value = value || '';
                    
                    // 触发变化事件以更新UI状态
                    var changeEvent = document.createEvent('Event');
                    changeEvent.initEvent('input', true, true);
                    inputNode.get(0).dispatchEvent(changeEvent);
                }
            });
        },
        
        // 重写 _getCtrlValue 方法 - 获取输入框的值
        _getCtrlValue: function() {
            var profile = this.get(0);
            if (!profile) return '';
            
            var inputNode = profile.getSubNode('INPUT');
            if (inputNode && inputNode.get(0)) {
                return inputNode.get(0).value || '';
            }
            return '';
        },
        
        // 重写 _setDirtyMark 方法 - 设置输入框的脏标记
        _setDirtyMark: function(key) {
            return this.each(function(profile) {
                if (!profile.renderId) return;
                
                var properties = profile.properties;
                var flag = properties.value !== properties.$UIvalue;
                var container = profile.getRoot();
                var dirtyClass = 'ood-mobile-input-dirty';
                
                if (profile._dirtyFlag !== flag) {
                    if (properties.dirtyMark && properties.showDirtyMark) {
                        if (profile.beforeDirtyMark && false === profile.boxing().beforeDirtyMark(profile, flag)) {
                            // 不做处理
                        } else {
                            if (flag) {
                                container.addClass(dirtyClass);
                            } else {
                                container.removeClass(dirtyClass);
                            }
                        }
                    }
                    profile._dirtyFlag = flag;
                }
            });
        },
        
        // 激活输入框（聚焦）
        activate: function() {
            var profile = this.get(0);
            if (profile) {
                var inputNode = profile.getSubNode('INPUT');
                if (inputNode && inputNode.get(0)) {
                    inputNode.get(0).focus();
                }
            }
            return this;
        },
        
        // 组件初始化
        initInputFeatures: function() {
            // 原有的输入框特性初始化逻辑
            this.initInputState();
            this.initValidation();
            this.initFormatting();
        },
        
        // 绑定触摸事件 - 符合 ood 事件规范
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            
            // 注册 ood 事件处理器
            this.registerEventHandlers();
        },
        
        // 注册 ood 事件处理器
        registerEventHandlers: function() {
            var self = this;
            var profile = this.get(0);
            
            // 聚焦事件 - 符合 ood 三阶段事件机制
            profile.beforeFocus = function(profile, e, src) {
                if (profile.properties.disabled || profile.properties.readonly) {
                    return false;
                }
                return true;
            };
            
            profile.onFocus = function(profile, e, src) {
                var container = profile.getRoot();
                container.addClass('ood-mobile-input-focused');
                self.handleInputFocus(profile, e);
            };
            
            profile.afterFocus = function(profile, e, src) {
                // 聚焦后的处理
                self.afterInputFocus(profile, e);
            };
            
            // 失焦事件
            profile.beforeBlur = function(profile, e, src) {
                return true;
            };
            
            profile.onBlur = function(profile, e, src) {
                var container = profile.getRoot();
                container.removeClass('ood-mobile-input-focused');
                self.handleInputBlur(profile, e);
                self.validateInput();
            };
            
            profile.afterBlur = function(profile, e, src) {
                // 失焦后的处理
                self.afterInputBlur(profile, e);
            };
            
            // 输入变化事件
            profile.beforeChange = function(profile, e, src) {
                return true;
            };
            
            profile.onChange = function(profile, e, src) {
                self.handleInputChange(profile, e);
                self.formatInput();
                self.validateInput();
            };
            
            profile.afterChange = function(profile, e, src) {
                // 变化后的处理
                self.afterInputChange(profile, e);
            };
            
            // 键盘事件
            profile.beforeKeydown = function(profile, e, src) {
                return true;
            };
            
            profile.onKeydown = function(profile, e, src) {
                self.handleKeydown(profile, e);
            };
            
            profile.afterKeydown = function(profile, e, src) {
                // 键盘事件后的处理
            };
            
            // 点击事件（针对清除按钮等）
            profile.beforeClick = function(profile, e, src) {
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                // 处理点击事件，如清除按钮
                var target = e.target || e.srcElement;
                if (ood(target).hasClass('ood-mobile-input-clear') || ood(target).closest('.ood-mobile-input-clear').length > 0) {
                    e.preventDefault();
                    self.clearInput();
                }
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理
            };
        },
        
        // 初始化输入框状态
        initInputState: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 设置输入框类型
            this.setInputType(properties.type || 'text');
            
            // 设置初始值
            if (properties.value) {
                this.setValue(properties.value);
            }
            
            // 设置占位符
            if (properties.placeholder) {
                this.setPlaceholder(properties.placeholder);
            }
            
            // 设置禁用状态
            if (properties.disabled) {
                this.setDisabled(true);
            }
            
            // 设置只读状态
            if (properties.readonly) {
                this.setReadonly(true);
            }
        },
        
        // 初始化验证
        initValidation: function() {
            this._validators = [];
            this._isValid = true;
            this._errorMessage = '';
            
            // 添加内置验证规则
            this.addBuiltinValidators();
        },
        
        // 添加内置验证规则
        addBuiltinValidators: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 必填验证
            if (properties.required) {
                this.addValidator('required', function(value) {
                    return value && value.trim().length > 0;
                }, '此字段为必填项');
            }
            
            // 最小长度验证
            if (properties.minLength) {
                this.addValidator('minLength', function(value) {
                    return !value || value.length >= properties.minLength;
                }, '输入长度不能少于' + properties.minLength + '个字符');
            }
            
            // 最大长度验证
            if (properties.maxLength) {
                this.addValidator('maxLength', function(value) {
                    return !value || value.length <= properties.maxLength;
                }, '输入长度不能超过' + properties.maxLength + '个字符');
            }
            
            // 邮箱验证
            if (properties.type === 'email') {
                this.addValidator('email', function(value) {
                    if (!value) return true;
                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    return emailRegex.test(value);
                }, '请输入有效的邮箱地址');
            }
            
            // 手机号验证
            if (properties.type === 'tel') {
                this.addValidator('tel', function(value) {
                    if (!value) return true;
                    var telRegex = /^1[3-9]\d{9}$/;
                    return telRegex.test(value);
                }, '请输入有效的手机号码');
            }
            
            // 数字验证
            if (properties.type === 'number') {
                this.addValidator('number', function(value) {
                    if (!value) return true;
                    return !isNaN(value);
                }, '请输入有效的数字');
            }
        },
        
        // 添加验证器
        addValidator: function(name, validator, message) {
            this._validators.push({
                name: name,
                validator: validator,
                message: message
            });
        },
        
        // 移除验证器
        removeValidator: function(name) {
            this._validators = this._validators.filter(function(v) {
                return v.name !== name;
            });
        },
        
        // 验证输入
        validateInput: function() {
            var value = this.getValue();
            var isValid = true;
            var errorMessage = '';
            
            for (var i = 0; i < this._validators.length; i++) {
                var validator = this._validators[i];
                if (!validator.validator(value)) {
                    isValid = false;
                    errorMessage = validator.message;
                    break;
                }
            }
            
            this._isValid = isValid;
            this._errorMessage = errorMessage;
            
            // 更新UI状态
            this.updateValidationState();
            
            return isValid;
        },
        
        // 更新验证状态UI
        updateValidationState: function() {
            var profile = this.get(0);
            var container = profile.getRoot();
            var errorNode = profile.getSubNode('ERROR');
            
            if (this._isValid) {
                container.removeClass('ood-mobile-input-error');
                errorNode.css('display', 'none');
            } else {
                container.addClass('ood-mobile-input-error');
                errorNode.html(this._errorMessage);
                errorNode.css('display', 'block');
            }
        },
        
        // 初始化格式化
        initFormatting: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 根据类型设置格式化器
            if (properties.formatter) {
                this._formatter = properties.formatter;
            } else {
                this._formatter = this.getDefaultFormatter(properties.type);
            }
        },
        
        // 获取默认格式化器
        getDefaultFormatter: function(type) {
            switch(type) {
                case 'tel':
                    return this.formatPhoneNumber;
                case 'number':
                    return this.formatNumber;
                case 'currency':
                    return this.formatCurrency;
                default:
                    return null;
            }
        },
        
        // 格式化输入
        formatInput: function() {
            if (!this._formatter) return;
            
            var value = this.getValue();
            var formattedValue = this._formatter.call(this, value);
            
            if (formattedValue !== value) {
                this.setValue(formattedValue);
            }
        },
        
        // 手机号格式化
        formatPhoneNumber: function(value) {
            if (!value) return value;
            
            // 移除所有非数字字符
            var numbers = value.replace(/\D/g, '');
            
            // 限制长度
            if (numbers.length > 11) {
                numbers = numbers.substring(0, 11);
            }
            
            // 格式化为 xxx xxxx xxxx
            if (numbers.length > 7) {
                return numbers.substring(0, 3) + ' ' + numbers.substring(3, 7) + ' ' + numbers.substring(7);
            } else if (numbers.length > 3) {
                return numbers.substring(0, 3) + ' ' + numbers.substring(3);
            }
            
            return numbers;
        },
        
        // 数字格式化
        formatNumber: function(value) {
            if (!value) return value;
            
            // 移除非数字和小数点字符
            var numbers = value.replace(/[^\d.]/g, '');
            
            // 确保只有一个小数点
            var parts = numbers.split('.');
            if (parts.length > 2) {
                numbers = parts[0] + '.' + parts.slice(1).join('');
            }
            
            return numbers;
        },
        
        // 货币格式化
        formatCurrency: function(value) {
            if (!value) return value;
            
            var numbers = this.formatNumber(value);
            if (!numbers) return numbers;
            
            var num = parseFloat(numbers);
            if (isNaN(num)) return numbers;
            
            return num.toFixed(2);
        },
        
        // 设置输入框类型
        setInputType: function(type) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.attr('type', type);
            profile.properties.type = type;
            
            // 移动端优化
            this.setMobileAttributes(type);
        },
        
        // 设置移动端属性
        setMobileAttributes: function(type) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            switch(type) {
                case 'tel':
                    input.attr('inputmode', 'tel');
                    input.attr('pattern', '[0-9]*');
                    break;
                case 'number':
                    input.attr('inputmode', 'numeric');
                    input.attr('pattern', '[0-9]*');
                    break;
                case 'email':
                    input.attr('inputmode', 'email');
                    break;
                case 'url':
                    input.attr('inputmode', 'url');
                    break;
                case 'search':
                    input.attr('inputmode', 'search');
                    break;
            }
        },
        
        // 设置值
        setValue: function(value) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.val(value || '');
            profile.properties.value = value;
            
            // 更新清除按钮显示
            this.updateClearButton();
            
            // 触发change事件
            this.onInputChange();
        },
        
        // 获取值
        getValue: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            return input.val() || '';
        },
        
        // 设置占位符
        setPlaceholder: function(placeholder) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.attr('placeholder', placeholder);
            profile.properties.placeholder = placeholder;
        },
        
        // 设置禁用状态
        setDisabled: function(disabled) {
            return this.each(function(profile) {
                var container = profile.getRoot();
                var input = profile.getSubNode('INPUT');
                
                profile.properties.disabled = disabled;
                
                if (disabled) {
                    container.addClass('ood-mobile-input-disabled');
                    input.attr('disabled', true);
                } else {
                    container.removeClass('ood-mobile-input-disabled');
                    input.removeAttr('disabled');
                }
            });
        },
        
        // 设置只读状态
        setReadonly: function(readonly) {
            return this.each(function(profile) {
                var container = profile.getRoot();
                var input = profile.getSubNode('INPUT');
                
                profile.properties.readonly = readonly;
                
                if (readonly) {
                    container.addClass('ood-mobile-input-readonly');
                    input.attr('readonly', true);
                } else {
                    container.removeClass('ood-mobile-input-readonly');
                    input.removeAttr('readonly');
                }
            });
        },
        
        // 清除输入
        clearInput: function() {
            this.setValue('');
            this.focus();
        },
        
        // 聚焦
        focus: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            setTimeout(function() {
                input.get(0).focus();
            }, 0);
        },
        
        // 失焦
        blur: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.get(0).blur();
        },
        
        // 更新清除按钮显示
        updateClearButton: function() {
            var profile = this.get(0);
            var clearBtn = profile.getSubNode('CLEAR');
            var value = this.getValue();
            
            if (value && value.length > 0 && !profile.properties.disabled && !profile.properties.readonly) {
                clearBtn.css('display', 'block');
            } else {
                clearBtn.css('display', 'none');
            }
        },
        
        // 输入框聚焦事件处理 - 符合 ood 规范
        handleInputFocus: function(profile, e) {
            // 触发用户自定义事件
            if (profile.properties.onFocus && typeof profile.properties.onFocus === 'function') {
                profile.properties.onFocus.call(this, profile, e);
            }
        },
        
        // 聚焦后处理
        afterInputFocus: function(profile, e) {
            if (profile.properties.onAfterFocus && typeof profile.properties.onAfterFocus === 'function') {
                profile.properties.onAfterFocus.call(this, profile, e);
            }
        },
        
        // 输入框失焦事件处理 - 符合 ood 规范
        handleInputBlur: function(profile, e) {
            // 触发用户自定义事件
            if (profile.properties.onBlur && typeof profile.properties.onBlur === 'function') {
                profile.properties.onBlur.call(this, profile, e);
            }
        },
        
        // 失焦后处理
        afterInputBlur: function(profile, e) {
            if (profile.properties.onAfterBlur && typeof profile.properties.onAfterBlur === 'function') {
                profile.properties.onAfterBlur.call(this, profile, e);
            }
        },
        
        // 输入变化事件处理 - 符合 ood 规范 
        handleInputChange: function(profile, e) {
            var value = this.getValue();
            
            // 更新清除按钮显示
            this.updateClearButton();
            
            // 触发用户自定义事件
            if (profile.properties.onChange && typeof profile.properties.onChange === 'function') {
                profile.properties.onChange.call(this, profile, e, value);
            }
        },
        
        // 变化后处理
        afterInputChange: function(profile, e) {
            if (profile.properties.onAfterChange && typeof profile.properties.onAfterChange === 'function') {
                profile.properties.onAfterChange.call(this, profile, e);
            }
        },
        
        // 键盘事件处理
        handleKeydown: function(profile, e) {
            if (profile.properties.onKeydown && typeof profile.properties.onKeydown === 'function') {
                profile.properties.onKeydown.call(this, profile, e);
            }
        },
        
        // 检查是否为交互式组件
        isInteractive: function() {
            return true;
        },
        
        // 检查是否为动态组件
        isDynamic: function() {
            return true; // 输入框内容会变化
        },
        
        // 激活事件处理（键盘激活）
        onActivate: function(e) {
            if (!this.get(0).properties.disabled && !this.get(0).properties.readonly) {
                this.focus();
            }
        },
        
        // 主题变化事件处理
        onThemeChange: function(oldTheme, newTheme) {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 移除旧主题类
            root.removeClass('input-theme-' + oldTheme);
            
            // 添加新主题类
            if (newTheme && newTheme !== 'light') {
                root.addClass('input-theme-' + newTheme);
            }
        },
        
        // 移动端布局事件
        onMobileLayout: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            // 移动端增大输入框尺寸
            input.css('min-height', 'var(--mobile-touch-target-lg)');
            input.css('font-size', 'var(--mobile-font-lg)');
        },
        
        // 桌面端布局事件
        onDesktopLayout: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            // 恢复默认尺寸
            input.css('min-height', 'var(--mobile-touch-target)');
            input.css('font-size', 'var(--mobile-font-md)');
        },
        
        // 添加屏幕阅读器专用文本
        addScreenReaderText: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var properties = profile.properties;
            
            // 为必填字段添加说明
            if (properties.required) {
                var srRequired = ood('<span class="mobile-sr-only">必填字段</span>');
                root.append(srRequired);
            }
            
            // 为错误状态添加说明
            if (!this._isValid && this._errorMessage) {
                var srError = ood('<span class="mobile-sr-only">输入错误：' + this._errorMessage + '</span>');
                root.append(srError);
            }
        },
    },
    
    Static: {
        // 模板定义
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-input {_sizeClass} {_statusClass}',
            style: '{_style}',
            
            LABEL: {
                tagName: 'label',
                className: 'ood-mobile-input-label',
                text: '{label}',
                style: 'display: {_labelDisplay}'
            },
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-input-container',
                
                PREFIX: {
                    tagName: 'div',
                    className: 'ood-mobile-input-prefix',
                    style: 'display: {_prefixDisplay}',
                    text: '{prefix}'
                },
                
                INPUT: {
                    tagName: 'input',
                    className: 'ood-mobile-input-field',
                    type: '{type}',
                    value: '{value}',
                    placeholder: '{placeholder}',
                    maxlength: '{maxLength}',
                    minlength: '{minLength}'
                },
                
                SUFFIX: {
                    tagName: 'div',
                    className: 'ood-mobile-input-suffix',
                    style: 'display: {_suffixDisplay}',
                    text: '{suffix}'
                },
                
                CLEAR: {
                    tagName: 'button',
                    className: 'ood-mobile-input-clear',
                    type: 'button',
                    style: 'display: none',
                    
                    ICON: {
                        tagName: 'i',
                        className: 'ood-mobile-input-clear-icon'
                    }
                }
            },
            
            ERROR: {
                tagName: 'div',
                className: 'ood-mobile-input-error-message',
                style: 'display: none'
            },
            
            HINT: {
                tagName: 'div',
                className: 'ood-mobile-input-hint',
                text: '{hint}',
                style: 'display: {_hintDisplay}'
            }
        },
        
        // 外观样式定义
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'margin-bottom': 'var(--mobile-spacing-md)'
            },
            
            // 标签
            LABEL: {
                display: 'block',
                'margin-bottom': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-sm)',
                'font-weight': '500',
                color: 'var(--mobile-text-secondary)'
            },
            
            // 输入容器
            CONTAINER: {
                position: 'relative',
                display: 'flex',
                'align-items': 'center',
                'background-color': 'var(--mobile-bg-primary)',
                border: '1px solid var(--mobile-border-color)',
                'border-radius': 'var(--mobile-border-radius)',
                overflow: 'hidden',
                transition: 'all 0.2s ease-in-out'
            },
            
            // 前缀
            PREFIX: {
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)',
                'white-space': 'nowrap'
            },
            
            // 输入字段
            INPUT: {
                flex: '1',
                border: 'none',
                outline: 'none',
                'background-color': 'transparent',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4',
                'min-height': 'var(--mobile-touch-target)'
            },
            
            // 后缀
            SUFFIX: {
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)',
                'white-space': 'nowrap'
            },
            
            // 清除按钮
            CLEAR: {
                position: 'absolute',
                right: 'var(--mobile-spacing-sm)',
                width: '20px',
                height: '20px',
                border: 'none',
                'background-color': 'transparent',
                cursor: 'pointer',
                'border-radius': '50%',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            // 清除图标
            'CLEAR ICON': {
                'font-size': '14px',
                color: 'var(--mobile-text-tertiary)'
            },
            
            // 错误消息
            ERROR: {
                'margin-top': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-danger)',
                'line-height': '1.4'
            },
            
            // 提示信息
            HINT: {
                'margin-top': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-quaternary)',
                'line-height': '1.4'
            },
            
            // 聚焦状态
            'KEY.ood-mobile-input-focused CONTAINER': {
                'border-color': 'var(--mobile-primary)',
                'box-shadow': '0 0 0 2px rgba(0, 122, 255, 0.1)'
            },
            
            // 错误状态
            'KEY.ood-mobile-input-error CONTAINER': {
                'border-color': 'var(--mobile-danger)',
                'box-shadow': '0 0 0 2px rgba(255, 59, 48, 0.1)'
            },
            
            // 禁用状态
            'KEY.ood-mobile-input-disabled': {
                opacity: 'var(--mobile-disabled-opacity, 0.5)',
                'pointer-events': 'none'
            },
            
            'KEY.ood-mobile-input-disabled CONTAINER': {
                'background-color': 'var(--mobile-disabled-bg)'
            },
            
            // 只读状态
            'KEY.ood-mobile-input-readonly CONTAINER': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            // 主题支持 - 暗黑模式
            'KEY.input-theme-dark CONTAINER': {
                'background-color': 'var(--mobile-bg-tertiary)',
                'border-color': 'var(--mobile-border-color)'
            },
            
            'KEY.input-theme-dark INPUT': {
                color: 'var(--mobile-text-primary)'
            },
            
            // 主题支持 - 高对比度
            'KEY.input-theme-light-hc CONTAINER, KEY.input-theme-dark-hc CONTAINER': {
                'border-width': 'var(--mobile-border-width)',
                'border-style': 'solid'
            },
            
            'KEY.input-theme-light-hc.ood-mobile-input-focused CONTAINER, KEY.input-theme-dark-hc.ood-mobile-input-focused CONTAINER': {
                'box-shadow': '0 0 0 3px rgba(0, 122, 255, 0.3)'
            },
            
            // 聚焦状态
            'KEY INPUT:focus-visible': {
                outline: 'none'
            },
            
            'KEY:focus-within CONTAINER': {
                'border-color': 'var(--mobile-primary)',
                'box-shadow': '0 0 0 2px rgba(0, 122, 255, 0.1)'
            },
            
            // 高对比度聚焦状态
            'KEY.input-theme-light-hc:focus-within CONTAINER, KEY.input-theme-dark-hc:focus-within CONTAINER': {
                'box-shadow': '0 0 0 3px rgba(0, 122, 255, 0.3)'
            },
            
            // 移动端响应式样式
            'KEY.mobile-responsive INPUT': {
                'min-height': 'var(--mobile-touch-target-lg)',
                'font-size': 'var(--mobile-font-lg)',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)'
            },
            
            'KEY.mobile-tiny INPUT': {
                'font-size': 'var(--mobile-font-md)',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)'
            },
            
            // 尺寸样式
            'KEY.ood-mobile-input-sm INPUT': {
                'min-height': '32px',
                padding: 'var(--mobile-spacing-xs) var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)'
            },
            
            'KEY.ood-mobile-input-lg INPUT': {
                'min-height': '48px',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)',
                'font-size': 'var(--mobile-font-lg)'
            }
        },
        
        // 行为定义
        Behaviors: {
            HotKeyAllowed: true,
            DroppableKeys: []
        },
        
        // 数据模型
        DataModel: {
            // ===== 基础必需属性 =====
            // 标签文本（显示值）- 对应模板中的 {label} 或 {caption}
            caption: {
                caption: '输入框标签',
                ini: '输入框',
                action: function(value) {
                    var profile = this;
                    var labelNode = profile.getSubNode('LABEL');
                    if (labelNode && !labelNode.isEmpty()) {
                        labelNode.html(value || '');
                    }
                }
            },
            
            // 输入框宽度
            width: {
                caption: '输入框宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // 输入框高度
            height: {
                caption: '输入框高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 标签颜色（颜色选择器）
            labelColor: {
                caption: '标签颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var labelNode = this.getSubNode('LABEL');
                        if (labelNode && !labelNode.isEmpty()) {
                            labelNode.css('color', value);
                        }
                    }
                }
            },
            
            // 输入框边框色（颜色选择器）
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var inputNode = this.getSubNode('INPUT');
                        if (inputNode && !inputNode.isEmpty()) {
                            inputNode.css('border-color', value);
                        }
                    }
                }
            },
            
            // 前缀图标（图标选择器）
            prefixIcon: {
                caption: '前缀图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var prefixNode = profile.getSubNode('PREFIX_ICON');
                    
                    if (value) {
                        if (prefixNode && !prefixNode.isEmpty()) {
                            prefixNode.attr('class', 'ood-mobile-input-prefix-icon ' + value);
                            prefixNode.css('display', 'block');
                        }
                    } else {
                        if (prefixNode && !prefixNode.isEmpty()) {
                            prefixNode.css('display', 'none');
                        }
                    }
                }
            },
            
            // 后缀图标（图标选择器）
            suffixIcon: {
                caption: '后缀图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var suffixNode = profile.getSubNode('SUFFIX_ICON');
                    
                    if (value) {
                        if (suffixNode && !suffixNode.isEmpty()) {
                            suffixNode.attr('class', 'ood-mobile-input-suffix-icon ' + value);
                            suffixNode.css('display', 'block');
                        }
                    } else {
                        if (suffixNode && !suffixNode.isEmpty()) {
                            suffixNode.css('display', 'none');
                        }
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
                    this.getSubNode('INPUT').attr('aria-label', value);
                }
            },
            
            ariaDescribedBy: {
                caption: 'ARIA描述关联',
                ini: '',
                action: function(value) {
                    this.getSubNode('INPUT').attr('aria-describedby', value);
                }
            },
            
            // 输入值（继承自 ood.absValue）
            value: {
                caption: '输入值',
                ini: ''
                // set 方法由 ood.absValue 提供
            },
            
            // 继承 ood.absValue 的表单字段属性
            isFormField: {
                caption: '是否为表单字段',
                ini: true
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: true
            },
            
            showDirtyMark: {
                caption: '显示脏标记',
                ini: true
            },
            
            readonly: {
                caption: '只读状态',
                ini: false,
                action: function(value) {
                    this.boxing().setReadonly(value);
                }
            },
            
            // 输入类型
            type: {
                caption: '输入类型',
                ini: 'text',
                listbox: ['text', 'password', 'email', 'tel', 'number', 'url', 'search'],
                action: function(value) {
                    this.boxing().setInputType(value);
                }
            },
            
            // 占位符
            placeholder: {
                caption: '占位符文本',
                ini: '',
                action: function(value) {
                    this.boxing().setPlaceholder(value);
                }
            },
            
            // 标签
            label: {
                caption: '输入框标签',
                ini: ''
            },
            
            // 前缀
            prefix: {
                caption: '前缀内容',
                ini: ''
            },
            
            // 后缀
            suffix: {
                caption: '后缀内容',
                ini: ''
            },
            
            // 提示信息
            hint: {
                caption: '提示信息',
                ini: ''
            },
            
            // 尺寸
            size: {
                caption: '输入框尺寸',
                ini: 'md',
                listbox: ['sm', 'md', 'lg'],
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    
                    // 移除所有尺寸类
                    root.removeClass('ood-mobile-input-sm ood-mobile-input-lg');
                    
                    // 添加新尺寸类
                    if (value && value !== 'md') {
                        root.addClass('ood-mobile-input-' + value);
                    }
                }
            },
            
            // 是否必填
            required: {
                caption: '必填验证',
                ini: false,
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    var inputNode = profile.getSubNode('INPUT');
                    
                    if (value) {
                        root.addClass('ood-mobile-input-required');
                        inputNode.attr('required', 'required');
                    } else {
                        root.removeClass('ood-mobile-input-required');
                        inputNode.removeAttr('required');
                    }
                }
            },
            
            // 最小长度
            minLength: {
                caption: '最小输入长度',
                ini: null
            },
            
            // 最大长度
            maxLength: {
                caption: '最大输入长度',
                ini: null
            },
            
            // 是否禁用
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    this.boxing().setDisabled(value);
                }
            },
            
            // 是否只读
            readonly: {
                caption: '只读状态',
                ini: false,
                action: function(value) {
                    this.boxing().setReadonly(value);
                }
            },
            
            // 格式化器
            formatter: {
                caption: '内容格式化器',
                ini: null
            },
            
            // ood 规范事件处理器
            onFocus: {
                caption: '聚焦事件处理器',
                ini: null
            },
            
            onAfterFocus: {
                caption: '聚焦后事件处理器',
                ini: null
            },
            
            onBlur: {
                caption: '失焦事件处理器',
                ini: null
            },
            
            onAfterBlur: {
                caption: '失焦后事件处理器',
                ini: null
            },
            
            onChange: {
                caption: '变化事件处理器',
                ini: null
            },
            
            onAfterChange: {
                caption: '变化后事件处理器',
                ini: null
            },
            
            onKeydown: {
                caption: '键盘事件处理器',
                ini: null
            },
            
            // 宽度
            width: {
                caption: '输入框宽度',
                $spaceunit: 1,
                ini: '100%'
            }
        },
        
        // 表单字段检查
        _isFormField: function(profile) {
            return profile.properties.isFormField;
        },
        
        // 值确保方法
        _ensureValue: function(profile, value) {
            // 确保输入值为字符串
            return value === null || value === undefined ? '' : String(value);
        },
        
        // 校验方法
        _checkValid: function(profile, value) {
            var properties = profile.properties;
            
            // 必填校验
            if (properties.required && (!value || !value.trim())) {
                return false;
            }
            
            // 长度校验
            if (properties.minLength && value && value.length < properties.minLength) {
                return false;
            }
            
            if (properties.maxLength && value && value.length > properties.maxLength) {
                return false;
            }
            
            // 类型校验
            if (properties.type === 'email' && value) {
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(value);
            }
            
            if (properties.type === 'tel' && value) {
                var phoneRegex = /^[\d\s\-\+\(\)]+$/;
                return phoneRegex.test(value);
            }
            
            return true;
        },
        
        // 渲染触发器
        RenderTrigger: function() {
            var profile = this;
            
            // 初始化输入框
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },
        
        // 数据准备
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var properties = profile.properties;
            
            // 设置尺寸类
            data._sizeClass = properties.size && properties.size !== 'md' ? 'ood-mobile-input-' + properties.size : '';
            
            // 设置状态类
            data._statusClass = '';
            if (properties.disabled) data._statusClass += ' ood-mobile-input-disabled';
            if (properties.readonly) data._statusClass += ' ood-mobile-input-readonly';
            
            // 设置显示状态
            data._labelDisplay = properties.label ? 'block' : 'none';
            data._prefixDisplay = properties.prefix ? 'block' : 'none';
            data._suffixDisplay = properties.suffix ? 'block' : 'none';
            data._hintDisplay = properties.hint ? 'block' : 'none';
            
            // 设置输入框属性
            data.placeholder = properties.placeholder || '';
            data.type = properties.type || 'text';
            data.value = properties.value || '';
            
            // 设置图标
            data.prefixIcon = properties.prefixIcon || '';
            data.suffixIcon = properties.suffixIcon || '';
            
            // 设置主题类
            data._themeClass = properties.theme && properties.theme !== 'light' ? 'input-theme-' + properties.theme : '';
            
            // 设置响应式类
            data._responsiveClass = properties.responsive ? 'mobile-responsive' : '';
            
            // 设置ARIA属性
            data._ariaLabel = properties.ariaLabel || properties.label || '输入框';
            data._ariaDescribedBy = properties.ariaDescribedBy || '';
            
            // 设置必填状态
            data._required = properties.required ? 'required' : '';
            
            // 设置只读和禁用状态
            data._readonly = properties.readonly ? 'readonly' : '';
            data._disabled = properties.disabled ? 'disabled' : '';
            
            // 设置样式
            data._style = properties.style || '';
            
            return data;
        },
        
        // 事件处理器
        EventHandlers: {
            // 输入框事件
            onInputFocus: function(profile, event) {
                // 输入框聚焦事件处理器
            },
            
            onInputBlur: function(profile, event) {
                // 输入框失焦事件处理器
            },
            
            onInputChange: function(profile, event, value) {
                // 输入框变化事件处理器
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
            
            beforeUIValueSet: function(profile, oldValue, newValue, force, tag) {
                // UI值设置前的处理
            },
            
            afterUIValueSet: function(profile, oldValue, newValue, force, tag) {
                // UI值设置后的处理
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
            },
            
            beforeDirtyMark: function(profile, dirty) {
                // 脏标记前处理
            }
        }
    }
});

// 添加清除按钮图标样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .ood-mobile-input-clear-icon:before {
            content: "×";
            font-weight: bold;
        }
    `;
    document.head.appendChild(style);
}

// 添加Getter/Setter方法
(function() {
    var proto = ood.Mobile.Input.prototype;
    
    // caption 属性的 getter/setter
    proto.getCaption = function() {
        var profile = this.get(0);
        return profile ? profile.properties.caption : '';
    };
    
    proto.setCaption = function(value) {
        return this.each(function(profile) {
            profile.properties.caption = value;
            if (profile.boxing().caption && typeof profile.boxing().caption.action === 'function') {
                profile.boxing().caption.action.call(profile, value);
            }
        });
    };
    
    // width 属性的 getter/setter
    proto.getWidth = function() {
        var profile = this.get(0);
        return profile ? profile.properties.width : '';
    };
    
    proto.setWidth = function(value) {
        return this.each(function(profile) {
            profile.properties.width = value;
            if (profile.boxing().width && typeof profile.boxing().width.action === 'function') {
                profile.boxing().width.action.call(profile, value);
            }
        });
    };
    
    // height 属性的 getter/setter
    proto.getHeight = function() {
        var profile = this.get(0);
        return profile ? profile.properties.height : '';
    };
    
    proto.setHeight = function(value) {
        return this.each(function(profile) {
            profile.properties.height = value;
            if (profile.boxing().height && typeof profile.boxing().height.action === 'function') {
                profile.boxing().height.action.call(profile, value);
            }
        });
    };
    
    // labelColor 属性的 getter/setter
    proto.getLabelColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.labelColor : '';
    };
    
    proto.setLabelColor = function(value) {
        return this.each(function(profile) {
            profile.properties.labelColor = value;
            if (profile.boxing().labelColor && typeof profile.boxing().labelColor.action === 'function') {
                profile.boxing().labelColor.action.call(profile, value);
            }
        });
    };
    
    // borderColor 属性的 getter/setter
    proto.getBorderColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.borderColor : '';
    };
    
    proto.setBorderColor = function(value) {
        return this.each(function(profile) {
            profile.properties.borderColor = value;
            if (profile.boxing().borderColor && typeof profile.boxing().borderColor.action === 'function') {
                profile.boxing().borderColor.action.call(profile, value);
            }
        });
    };
    
    // prefixIcon 属性的 getter/setter
    proto.getPrefixIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.prefixIcon : '';
    };
    
    proto.setPrefixIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.prefixIcon = value;
            if (profile.boxing().prefixIcon && typeof profile.boxing().prefixIcon.action === 'function') {
                profile.boxing().prefixIcon.action.call(profile, value);
            }
        });
    };
    
    // suffixIcon 属性的 getter/setter
    proto.getSuffixIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.suffixIcon : '';
    };
    
    proto.setSuffixIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.suffixIcon = value;
            if (profile.boxing().suffixIcon && typeof profile.boxing().suffixIcon.action === 'function') {
                profile.boxing().suffixIcon.action.call(profile, value);
            }
        });
    };
    
    // theme 属性的 getter/setter
    proto.getTheme = function() {
        var profile = this.get(0);
        return profile ? profile.properties.theme : 'light';
    };
    
    proto.setTheme = function(value) {
        return this.each(function(profile) {
            profile.properties.theme = value;
            if (profile.boxing().theme && typeof profile.boxing().theme.action === 'function') {
                profile.boxing().theme.action.call(profile, value);
            }
        });
    };
    
    // responsive 属性的 getter/setter
    proto.getResponsive = function() {
        var profile = this.get(0);
        return profile ? profile.properties.responsive : true;
    };
    
    proto.setResponsive = function(value) {
        return this.each(function(profile) {
            profile.properties.responsive = value;
            if (profile.boxing().responsive && typeof profile.boxing().responsive.action === 'function') {
                profile.boxing().responsive.action.call(profile, value);
            }
        });
    };
    
    // ariaLabel 属性的 getter/setter
    proto.getAriaLabel = function() {
        var profile = this.get(0);
        return profile ? profile.properties.ariaLabel : '';
    };
    
    proto.setAriaLabel = function(value) {
        return this.each(function(profile) {
            profile.properties.ariaLabel = value;
            if (profile.boxing().ariaLabel && typeof profile.boxing().ariaLabel.action === 'function') {
                profile.boxing().ariaLabel.action.call(profile, value);
            }
        });
    };
    
    // ariaDescribedBy 属性的 getter/setter
    proto.getAriaDescribedBy = function() {
        var profile = this.get(0);
        return profile ? profile.properties.ariaDescribedBy : '';
    };
    
    proto.setAriaDescribedBy = function(value) {
        return this.each(function(profile) {
            profile.properties.ariaDescribedBy = value;
            if (profile.boxing().ariaDescribedBy && typeof profile.boxing().ariaDescribedBy.action === 'function') {
                profile.boxing().ariaDescribedBy.action.call(profile, value);
            }
        });
    };
    
    // value 属性的 getter/setter
    proto.getValue = function() {
        var profile = this.get(0);
        return profile ? profile.properties.value : '';
    };
    
    proto.setValue = function(value) {
        return this.each(function(profile) {
            profile.properties.value = value;
            if (profile.boxing().value && typeof profile.boxing().value.action === 'function') {
                profile.boxing().value.action.call(profile, value);
            }
        });
    };
    
    // isFormField 属性的 getter/setter
    proto.getIsFormField = function() {
        var profile = this.get(0);
        return profile ? profile.properties.isFormField : true;
    };
    
    proto.setIsFormField = function(value) {
        return this.each(function(profile) {
            profile.properties.isFormField = value;
            if (profile.boxing().isFormField && typeof profile.boxing().isFormField.action === 'function') {
                profile.boxing().isFormField.action.call(profile, value);
            }
        });
    };
    
    // dirtyMark 属性的 getter/setter
    proto.getDirtyMark = function() {
        var profile = this.get(0);
        return profile ? profile.properties.dirtyMark : true;
    };
    
    proto.setDirtyMark = function(value) {
        return this.each(function(profile) {
            profile.properties.dirtyMark = value;
            if (profile.boxing().dirtyMark && typeof profile.boxing().dirtyMark.action === 'function') {
                profile.boxing().dirtyMark.action.call(profile, value);
            }
        });
    };
    
    // showDirtyMark 属性的 getter/setter
    proto.getShowDirtyMark = function() {
        var profile = this.get(0);
        return profile ? profile.properties.showDirtyMark : true;
    };
    
    proto.setShowDirtyMark = function(value) {
        return this.each(function(profile) {
            profile.properties.showDirtyMark = value;
            if (profile.boxing().showDirtyMark && typeof profile.boxing().showDirtyMark.action === 'function') {
                profile.boxing().showDirtyMark.action.call(profile, value);
            }
        });
    };
    
    // readonly 属性的 getter/setter
    proto.getReadonly = function() {
        var profile = this.get(0);
        return profile ? profile.properties.readonly : false;
    };
    
    proto.setReadonly = function(value) {
        return this.each(function(profile) {
            profile.properties.readonly = value;
            if (profile.boxing().readonly && typeof profile.boxing().readonly.action === 'function') {
                profile.boxing().readonly.action.call(profile, value);
            }
        });
    };
    
    // type 属性的 getter/setter
    proto.getType = function() {
        var profile = this.get(0);
        return profile ? profile.properties.type : 'text';
    };
    
    proto.setType = function(value) {
        return this.each(function(profile) {
            profile.properties.type = value;
            if (profile.boxing().type && typeof profile.boxing().type.action === 'function') {
                profile.boxing().type.action.call(profile, value);
            }
        });
    };
    
    // placeholder 属性的 getter/setter
    proto.getPlaceholder = function() {
        var profile = this.get(0);
        return profile ? profile.properties.placeholder : '';
    };
    
    proto.setPlaceholder = function(value) {
        return this.each(function(profile) {
            profile.properties.placeholder = value;
            if (profile.boxing().placeholder && typeof profile.boxing().placeholder.action === 'function') {
                profile.boxing().placeholder.action.call(profile, value);
            }
        });
    };
    
    // label 属性的 getter/setter
    proto.getLabel = function() {
        var profile = this.get(0);
        return profile ? profile.properties.label : '';
    };
    
    proto.setLabel = function(value) {
        return this.each(function(profile) {
            profile.properties.label = value;
            if (profile.boxing().label && typeof profile.boxing().label.action === 'function') {
                profile.boxing().label.action.call(profile, value);
            }
        });
    };
    
    // prefix 属性的 getter/setter
    proto.getPrefix = function() {
        var profile = this.get(0);
        return profile ? profile.properties.prefix : '';
    };
    
    proto.setPrefix = function(value) {
        return this.each(function(profile) {
            profile.properties.prefix = value;
            if (profile.boxing().prefix && typeof profile.boxing().prefix.action === 'function') {
                profile.boxing().prefix.action.call(profile, value);
            }
        });
    };
    
    // suffix 属性的 getter/setter
    proto.getSuffix = function() {
        var profile = this.get(0);
        return profile ? profile.properties.suffix : '';
    };
    
    proto.setSuffix = function(value) {
        return this.each(function(profile) {
            profile.properties.suffix = value;
            if (profile.boxing().suffix && typeof profile.boxing().suffix.action === 'function') {
                profile.boxing().suffix.action.call(profile, value);
            }
        });
    };
    
    // hint 属性的 getter/setter
    proto.getHint = function() {
        var profile = this.get(0);
        return profile ? profile.properties.hint : '';
    };
    
    proto.setHint = function(value) {
        return this.each(function(profile) {
            profile.properties.hint = value;
            if (profile.boxing().hint && typeof profile.boxing().hint.action === 'function') {
                profile.boxing().hint.action.call(profile, value);
            }
        });
    };
    
    // size 属性的 getter/setter
    proto.getSize = function() {
        var profile = this.get(0);
        return profile ? profile.properties.size : 'md';
    };
    
    proto.setSize = function(value) {
        return this.each(function(profile) {
            profile.properties.size = value;
            if (profile.boxing().size && typeof profile.boxing().size.action === 'function') {
                profile.boxing().size.action.call(profile, value);
            }
        });
    };
    
    // required 属性的 getter/setter
    proto.getRequired = function() {
        var profile = this.get(0);
        return profile ? profile.properties.required : false;
    };
    
    proto.setRequired = function(value) {
        return this.each(function(profile) {
            profile.properties.required = value;
            if (profile.boxing().required && typeof profile.boxing().required.action === 'function') {
                profile.boxing().required.action.call(profile, value);
            }
        });
    };
    
    // minLength 属性的 getter/setter
    proto.getMinLength = function() {
        var profile = this.get(0);
        return profile ? profile.properties.minLength : null;
    };
    
    proto.setMinLength = function(value) {
        return this.each(function(profile) {
            profile.properties.minLength = value;
            if (profile.boxing().minLength && typeof profile.boxing().minLength.action === 'function') {
                profile.boxing().minLength.action.call(profile, value);
            }
        });
    };
    
    // maxLength 属性的 getter/setter
    proto.getMaxLength = function() {
        var profile = this.get(0);
        return profile ? profile.properties.maxLength : null;
    };
    
    proto.setMaxLength = function(value) {
        return this.each(function(profile) {
            profile.properties.maxLength = value;
            if (profile.boxing().maxLength && typeof profile.boxing().maxLength.action === 'function') {
                profile.boxing().maxLength.action.call(profile, value);
            }
        });
    };
    
    // disabled 属性的 getter/setter
    proto.getDisabled = function() {
        var profile = this.get(0);
        return profile ? profile.properties.disabled : false;
    };
    
    proto.setDisabled = function(value) {
        return this.each(function(profile) {
            profile.properties.disabled = value;
            if (profile.boxing().disabled && typeof profile.boxing().disabled.action === 'function') {
                profile.boxing().disabled.action.call(profile, value);
            }
        });
    };
    
    // formatter 属性的 getter/setter
    proto.getFormatter = function() {
        var profile = this.get(0);
        return profile ? profile.properties.formatter : null;
    };
    
    proto.setFormatter = function(value) {
        return this.each(function(profile) {
            profile.properties.formatter = value;
            if (profile.boxing().formatter && typeof profile.boxing().formatter.action === 'function') {
                profile.boxing().formatter.action.call(profile, value);
            }
        });
    };
    
    // onFocus 属性的 getter/setter
    proto.getOnFocus = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onFocus : null;
    };
    
    proto.setOnFocus = function(value) {
        return this.each(function(profile) {
            profile.properties.onFocus = value;
            if (profile.boxing().onFocus && typeof profile.boxing().onFocus.action === 'function') {
                profile.boxing().onFocus.action.call(profile, value);
            }
        });
    };
    
    // onAfterFocus 属性的 getter/setter
    proto.getOnAfterFocus = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterFocus : null;
    };
    
    proto.setOnAfterFocus = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterFocus = value;
            if (profile.boxing().onAfterFocus && typeof profile.boxing().onAfterFocus.action === 'function') {
                profile.boxing().onAfterFocus.action.call(profile, value);
            }
        });
    };
    
    // onBlur 属性的 getter/setter
    proto.getOnBlur = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onBlur : null;
    };
    
    proto.setOnBlur = function(value) {
        return this.each(function(profile) {
            profile.properties.onBlur = value;
            if (profile.boxing().onBlur && typeof profile.boxing().onBlur.action === 'function') {
                profile.boxing().onBlur.action.call(profile, value);
            }
        });
    };
    
    // onAfterBlur 属性的 getter/setter
    proto.getOnAfterBlur = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterBlur : null;
    };
    
    proto.setOnAfterBlur = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterBlur = value;
            if (profile.boxing().onAfterBlur && typeof profile.boxing().onAfterBlur.action === 'function') {
                profile.boxing().onAfterBlur.action.call(profile, value);
            }
        });
    };
    
    // onChange 属性的 getter/setter
    proto.getOnChange = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onChange : null;
    };
    
    proto.setOnChange = function(value) {
        return this.each(function(profile) {
            profile.properties.onChange = value;
            if (profile.boxing().onChange && typeof profile.boxing().onChange.action === 'function') {
                profile.boxing().onChange.action.call(profile, value);
            }
        });
    };
    
    // onAfterChange 属性的 getter/setter
    proto.getOnAfterChange = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterChange : null;
    };
    
    proto.setOnAfterChange = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterChange = value;
            if (profile.boxing().onAfterChange && typeof profile.boxing().onAfterChange.action === 'function') {
                profile.boxing().onAfterChange.action.call(profile, value);
            }
        });
    };
    
    // onKeydown 属性的 getter/setter
    proto.getOnKeydown = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onKeydown : null;
    };
    
    proto.setOnKeydown = function(value) {
        return this.each(function(profile) {
            profile.properties.onKeydown = value;
            if (profile.boxing().onKeydown && typeof profile.boxing().onKeydown.action === 'function') {
                profile.boxing().onKeydown.action.call(profile, value);
            }
        });
    };
})();/**
 * 移动端列表组件
 * 继承自 ood.UI（UI基类）、ood.absList（列表功能）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 */
ood.Class("ood.Mobile.List", ["ood.UI", "ood.absList", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['CONTAINER', 'HEADER', 'ITEMS', 'ITEM', 'FOOTER', 'LOADING']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {
                    id: 'item1',
                    title: '列表项1',
                    subtitle: '子标题1',
                    avatar: 'ri ri-user-line',
                    status: 'active'
                },
                {
                    id: 'item2',
                    title: '列表项2',
                    subtitle: '子标题2',
                    avatar: 'ri ri-user-2-line',
                    status: 'inactive'
                },
                {
                    id: 'item3',
                    title: '列表项3',
                    subtitle: '子标题3',
                    avatar: 'ri ri-user-3-line',
                    status: 'pending'
                },
                {
                    id: 'item4',
                    title: '列表项4',
                    subtitle: '子标题4',
                    avatar: 'ri ri-user-4-line',
                    status: 'active',
                    badge: 5
                },
                {
                    id: 'item5',
                    title: '列表项5',
                    subtitle: '子标题5',
                    avatar: 'ri ri-user-5-line',
                    status: 'disabled'
                }
            ],
            theme: 'light',
            responsive: true,
            showAvatar: true,
            showBadge: true
        },

        Initialize: function() {
            // 调用父类初始化
             // 初始化移动端列表特性
            this.initMobileListFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileListFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端列表CSS类
            profile.getRoot().addClass('ood-mobile-list ood-mobile-component');
            
            // 初始化列表功能
            this.initListFeatures();
            
            // 初始化触摸事件
            this.bindTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // ood.absValue 必需方法 - 控制值管理
        _setCtrlValue: function(value) {
            this.each(function(profile) {
                // 设置列表的选中值
                if (value) {
                    var items = profile.getSubNode('ITEMS').find('.ood-mobile-list-item');
                    items.removeClass('ood-mobile-list-item-selected');
                    
                    var targetIndex = parseInt(value);
                    if (!isNaN(targetIndex) && targetIndex >= 0 && targetIndex < items.length) {
                        ood(items[targetIndex]).addClass('ood-mobile-list-item-selected');
                    }
                }
            });
        },
        
        _getCtrlValue: function() {
            var profile = this.get(0);
            if (!profile) return null;
            
            var selectedItem = profile.getSubNode('ITEMS').find('.ood-mobile-list-item-selected');
            if (selectedItem.length > 0) {
                return selectedItem.attr('data-index');
            }
            return null;
        },
        
        activate: function() {
            return this.each(function(profile) {
                // 激活列表（聚焦到第一个项）
                var firstItem = profile.getSubNode('ITEMS').find('.ood-mobile-list-item').eq(0);
                if (firstItem.length > 0) {
                    firstItem.focus();
                }
            });
        },
        
        resetValue: function(value) {
            // 重写基类的 resetValue 方法
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
            });
            return this;
        },

        // 表单字段支持
        _isFormField: function() {
            var profile = this.get(0);
            return profile && profile.properties.isFormField;
        },
        
        // 主题支持方法
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme || 'light';
                var root = profile.getRoot();
                
                // 清除现有主题类
                root.removeClass('ood-mobile-list-dark ood-mobile-list-hc');
                
                // 应用新主题类
                switch (theme) {
                    case 'dark':
                        root.addClass('ood-mobile-list-dark');
                        break;
                    case 'light-hc':
                    case 'dark-hc':
                        root.addClass('ood-mobile-list-hc');
                        break;
                }
                
                // 持久化主题偏好
                try {
                    if (typeof localStorage !== 'undefined') {
                        localStorage.setItem('mobile-list-theme', theme);
                    }
                } catch (e) {
                    console.warn('主题偏好保存失败:', e);
                }
            });
        },
        
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme || 'light' : 'light';
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var width = ood(document.body).cssSize().width;
                // 移除所有响应式类
                root.removeClass('ood-mobile-list-sm ood-mobile-list-xs');
                // 应用响应式类
                if (width < 480) {
                    root.addClass('ood-mobile-list-xs');
                } else if (width < 768) {
                    root.addClass('ood-mobile-list-sm');
                }
            });
        },
        
        initListFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-list');
          //  this.initData();
            this.initPullRefresh();
            
            // 初始化主题
            if (profile.properties.theme) {
                this.setTheme(profile.properties.theme);
            }
            
            // 初始化响应式布局
            if (profile.properties.responsive !== false) {
                this.adjustLayout();
            }
        },
        
        // 绑定触摸事件 - 符合 ood 事件规范
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            
            // 注册 ood 事件处理器
            this.registerEventHandlers();
        },
        
        // 注册 ood 事件处理器
        registerEventHandlers: function() {
            var self = this;
            var profile = this.get(0);
            
            // 列表项点击事件 - 符合 ood 三阶段事件机制
            profile.beforeClick = function(profile, e, src) {
                var target = e.target || e.srcElement;
                var item = ood(target).closest('.ood-mobile-list-item');
                
                if (item.length === 0) return false;
                if (item.hasClass('ood-mobile-list-item-disabled')) return false;
                
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                var target = e.target || e.srcElement;
                var item = ood(target).closest('.ood-mobile-list-item');
                
                if (item.length > 0) {
                    var index = parseInt(item.attr('data-index'));
                    var data = self.getItemData(index);
                    self.handleItemClick(profile, index, data, e);
                }
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理
                self.afterItemClick(profile, e);
            };
        },

        initPullRefresh: function() {
            var profile = this.get(0);
            if (!profile || !profile.properties.pullRefresh) return;
            
            this._pullRefresh = {
                enabled: true,
                threshold: 80,
                isRefreshing: false
            };
        },

        getItemData: function(index) {
            return this._data[index];
        },

        // 列表项点击事件处理 - 符合 ood 规范
        handleItemClick: function(profile, index, data, event) {
            // 触发用户自定义事件
            if (profile.properties.onItemClick && typeof profile.properties.onItemClick === 'function') {
                profile.properties.onItemClick.call(this, profile, index, data, event);
            }
        },
        
        // 点击后处理
        afterItemClick: function(profile, event) {
            if (profile.properties.onAfterItemClick && typeof profile.properties.onAfterItemClick === 'function') {
                profile.properties.onAfterItemClick.call(this, profile, event);
            }
        }
    },
    
    Static: {
        _DIRTYKEY: 'ITEM',
        Templates: {
            tagName: 'div',
            className: '{_className} ood-mobile-list',
            style: '{_style}',
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign}',
                text: '{labelCaption}'
            },
            ITEMS: {
                $order: 10,
                tagName: 'div',
                className: 'ood-mobile-list-items ood-uibase',
                style: '{_itemsStyle}',
                text: "{items}"
            },
            $submap: {
                items: {
                    ITEM: {

                        className: 'ood-mobile-list-item {itemClass} {disabled} {readonly} {_itemState}',
                        style: '{itemStyle}{_itemDisplay}',
                        tabindex: '{_tabindex}',
                        'data-index': '{_itemIndex}',
                        'data-id': '{id}',

                        ICON: {
                            $order: 5,
                            className: 'ood-mobile-list-item-icon {imageClass}',
                            style: '{iconStyle}{imageDisplay}',
                            text: '{iconFontCode}'
                        },
                        ITEMC: {
                            $order: 10,
                            className: 'ood-mobile-list-item-content',
                            TITLE: {
                                $order: 1,
                                className: 'ood-mobile-list-item-title',
                                text: '{title}'
                            },
                            SUBTITLE: {
                                $order: 2,
                                className: 'ood-mobile-list-item-subtitle',
                                style: '{_subtitleDisplay}',
                                text: '{subtitle}'
                            }
                        },
                        RIGHT: {
                            $order: 20,
                            className: 'ood-mobile-list-item-right',
                            style: '{_rightDisplay}',
                            text: '{rightText}'
                        },
                        BADGE: {
                            $order: 30,
                            className: 'ood-mobile-list-item-badge',
                            style: '{_badgeDisplay}',
                            text: '{badge}'
                        }
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: 'auto',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            LABEL: {
                'z-index': 1,
                top: 0,
                left: 0,
                display: 'flex',
                position: 'absolute',
                'padding-top': '.333em',
                'color': 'var(--mobile-text-heading)'
            },
            
            ITEMS: {
                position: 'relative',
                width: '100%',
                'overflow-y': 'auto',
                '-webkit-overflow-scrolling': 'touch',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            ITEM: {
                position: 'relative',
                display: 'flex',
                'align-items': 'center',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'background-color': 'var(--mobile-bg-primary)',
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                'min-height': 'var(--mobile-touch-target)',
                'color': 'var(--mobile-text-primary)'
            },
            
            'ITEM:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            'ITEM-selected': {
                'background-color': 'var(--mobile-primary-light)',
                'border-left': '4px solid var(--mobile-primary)',
                color: 'var(--mobile-primary-dark)'
            },
            
            'ITEM-disabled': {
                opacity: '0.5',
                cursor: 'not-allowed'
            },
            
            'KEY-readonly ITEM': {
                cursor: 'default'
            },
            
            ICON: {
                $order: 1,
                'margin-right': 'var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-lg)',
                color: 'var(--mobile-primary)'
            },
            
            ITEMC: {
                flex: 1,
                'min-width': 0
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4'
            },
            
            SUBTITLE: {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'line-height': '1.4',
                'margin-top': '2px'
            },
            
            RIGHT: {
                'margin-left': 'var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)'
            },
            
            BADGE: {
                'margin-left': 'var(--mobile-spacing-xs)',
                'background-color': 'var(--mobile-danger)',
                color: 'white',
                'border-radius': '50%',
                'min-width': '1.5em',
                'min-height': '1.5em',
                'font-size': 'var(--mobile-font-xs)',
                'text-align': 'center',
                'line-height': '1.5em'
            },
            
            // 主题支持
            'KEY-dark': {
                'background-color': 'var(--mobile-bg-dark)',
                color: 'var(--mobile-text-dark)'
            },
            
            'KEY-dark ITEM': {
                'background-color': 'var(--mobile-bg-dark)',
                'border-bottom-color': 'var(--mobile-border-dark)',
                color: 'var(--mobile-text-dark)'
            },
            
            'KEY-dark ITEM:hover': {
                'background-color': 'var(--mobile-bg-dark-hover)'
            },
            
            'KEY-hc': {
                'background-color': 'var(--mobile-hc-bg)',
                'border': '2px solid var(--mobile-hc-border)',
                color: 'var(--mobile-hc-text)'
            },
            
            'KEY-hc ITEM': {
                'background-color': 'var(--mobile-hc-bg)',
                'border-bottom': '2px solid var(--mobile-hc-border)',
                color: 'var(--mobile-hc-text)',
                'font-weight': 'bold'
            },
            
            'KEY-hc ITEM-selected': {
                'background-color': 'var(--mobile-hc-primary)',
                color: 'var(--mobile-hc-bg)',
                'border': '2px solid var(--mobile-hc-border)'
            },
            
            // 响应式支持
            'KEY-sm ITEM': {
                padding: 'var(--mobile-spacing-sm)',
                'font-size': '0.9em'
            },
            
            'KEY-xs ITEM': {
                padding: 'var(--mobile-spacing-xs)',
                'font-size': '0.85em',
                'min-height': '2.5em'
            },
            
            'KEY-xs RIGHT': {
                display: 'none'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            // 列表标题（显示值）
            caption: {
                caption: '列表标题',
                ini: '列表',
                action: function(value) {
                    var profile = this;
                    var titleNode = profile.getSubNode('TITLE');
                    if (titleNode && !titleNode.isEmpty()) {
                        titleNode.html(value || '');
                    }
                }
            },
            
            // 列表宽度
            width: {
                caption: '列表宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // 列表高度
            height: {
                caption: '列表高度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 列表背景色（颜色选择器）
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            // 列表项文字色（颜色选择器）
            textColor: {
                caption: '文字颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var items = this.getSubNode('ITEM', true);
                        if (items && !items.isEmpty()) {
                            items.css('color', value);
                        }
                    }
                }
            },
            
            // 列表项图标（图标选择器）
            itemIcon: {
                caption: '列表项图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var iconNodes = profile.getSubNode('ICON', true);
                    
                    if (value && iconNodes && !iconNodes.isEmpty()) {
                        iconNodes.each(function(icon) {
                            var iconNode = ood(icon);
                            iconNode.attr('class', 'ood-mobile-list-item-icon ' + value);
                            iconNode.css('display', 'inline-block');
                        });
                    } else if (iconNodes && !iconNodes.isEmpty()) {
                        iconNodes.css('display', 'none');
                    }
                }
            },
            
            // ===== 继承 ood.UI 的主题和可访问性属性 =====
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
            
            // ood.absValue 必需属性
            value: {
                caption: '选中值',
                ini: null,
                action: function(value) {
                    this.boxing()._setCtrlValue(value);
                }
            },
            
            isFormField: {
                caption: '表单字段',
                ini: false
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: false
            },
            
            showDirtyMark: {
                caption: '显示脏标记',
                ini: true
            },
            
            readonly: {
                caption: '只读',
                ini: false,
                action: function(value) {
                    this.getRoot().toggleClass('ood-mobile-list-readonly', value);
                }
            },
            
            // ood.absList 必需属性  
            selMode: {
                caption: '选择模式',
                ini: 'single',
                listbox: ['single', 'multi', 'none']
            },
            
            valueSeparator: {
                caption: '值分隔符',
                ini: ','
            },
            
            // 列表特定属性
            data: {
                caption: '列表数据',
                ini: [],
                action: function(value) {
                    this.boxing().setData(value);
                }
            },
            
            pullRefresh: {
                caption: '下拉刷新',
                ini: false
            },
            
            height: {
                caption: '列表高度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // ood 规范事件处理器
            onItemClick: {
                caption: '列表项点击事件处理器',
                ini: null
            },
            
            onAfterItemClick: {
                caption: '列表项点击后事件处理器',
                ini: null
            },
            
            // ood.absValue 事件处理器
            onChanged: {
                caption: '值改变事件处理器',
                ini: null
            },
            
            onChecked: {
                caption: '选中事件处理器', 
                ini: null
            },
            
            // ood.absList 事件处理器
            onItemSelected: {
                caption: '列表项选中事件处理器',
                ini: null
            }
        },

        // ===== GET/SET 方法 - 便于编辑器读写属性 =====
        // 基础属性的getter/setter
        getCaption: function() {
            var profile = this.get(0);
            return profile ? profile.properties.caption : '列表';
        },
        
        setCaption: function(value) {
            return this.each(function(profile) {
                profile.properties.caption = value || '列表';
                var titleNode = profile.getSubNode('TITLE');
                if (titleNode && !titleNode.isEmpty()) {
                    titleNode.html(value || '');
                }
            });
        },
        
        getWidth: function() {
            var profile = this.get(0);
            return profile ? profile.properties.width : '100%';
        },
        
        setWidth: function(value) {
            return this.each(function(profile) {
                profile.properties.width = value || '100%';
                if (profile.getRoot) {
                    profile.getRoot().css('width', value || '100%');
                }
            });
        },
        
        getHeight: function() {
            var profile = this.get(0);
            return profile ? profile.properties.height : '100%';
        },
        
        setHeight: function(value) {
            return this.each(function(profile) {
                profile.properties.height = value || '100%';
                if (profile.getRoot) {
                    profile.getRoot().css('height', value || '100%');
                }
            });
        },
        
        // 颜色属性的getter/setter
        getBackgroundColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.backgroundColor : '';
        },
        
        setBackgroundColor: function(value) {
            return this.each(function(profile) {
                profile.properties.backgroundColor = value || '';
                if (value && profile.getRoot) {
                    profile.getRoot().css('background-color', value);
                }
            });
        },
        
        getTextColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.textColor : '';
        },
        
        setTextColor: function(value) {
            return this.each(function(profile) {
                profile.properties.textColor = value || '';
                if (value) {
                    var items = profile.getSubNode('ITEM', true);
                    if (items && !items.isEmpty()) {
                        items.css('color', value);
                    }
                }
            });
        },
        
        getItemIcon: function() {
            var profile = this.get(0);
            return profile ? profile.properties.itemIcon : '';
        },
        
        setItemIcon: function(value) {
            return this.each(function(profile) {
                profile.properties.itemIcon = value || '';
                var iconNodes = profile.getSubNode('ICON', true);
                
                if (value && iconNodes && !iconNodes.isEmpty()) {
                    iconNodes.each(function(icon) {
                        var iconNode = ood(icon);
                        iconNode.attr('class', 'ood-mobile-list-item-icon ' + value);
                        iconNode.css('display', 'inline-block');
                    });
                } else if (iconNodes && !iconNodes.isEmpty()) {
                    iconNodes.css('display', 'none');
                }
            });
        },
        
        // 主题和功能属性的getter/setter
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme : 'light';
        },
        
        setTheme: function(value) {
            return this.each(function(profile) {
                profile.properties.theme = value || 'light';
                profile.boxing().setTheme(value || 'light');
            });
        },
        
        getResponsive: function() {
            var profile = this.get(0);
            return profile ? profile.properties.responsive : true;
        },
        
        setResponsive: function(value) {
            return this.each(function(profile) {
                profile.properties.responsive = value !== false;
                if (value) {
                    profile.boxing().adjustLayout();
                }
            });
        },
        
        // 值管理属性的getter/setter (继承自absValue)
        getValue: function() {
            var profile = this.get(0);
            return profile ? profile.properties.value : null;
        },
        
        setValue: function(value) {
            return this.each(function(profile) {
                profile.properties.value = value;
                profile.boxing()._setCtrlValue(value);
            });
        },
        
        getIsFormField: function() {
            var profile = this.get(0);
            return profile ? profile.properties.isFormField : false;
        },
        
        setIsFormField: function(value) {
            return this.each(function(profile) {
                profile.properties.isFormField = !!value;
            });
        },
        
        getDirtyMark: function() {
            var profile = this.get(0);
            return profile ? profile.properties.dirtyMark : false;
        },
        
        setDirtyMark: function(value) {
            return this.each(function(profile) {
                profile.properties.dirtyMark = !!value;
            });
        },
        
        getShowDirtyMark: function() {
            var profile = this.get(0);
            return profile ? profile.properties.showDirtyMark : true;
        },
        
        setShowDirtyMark: function(value) {
            return this.each(function(profile) {
                profile.properties.showDirtyMark = value !== false;
            });
        },
        
        getReadonly: function() {
            var profile = this.get(0);
            return profile ? profile.properties.readonly : false;
        },
        
        setReadonly: function(value) {
            return this.each(function(profile) {
                profile.properties.readonly = !!value;
                profile.getRoot().toggleClass('ood-mobile-list-readonly', !!value);
            });
        },
        
        // 列表管理属性的getter/setter (继承自absList)
        getSelMode: function() {
            var profile = this.get(0);
            return profile ? profile.properties.selMode : 'single';
        },
        
        setSelMode: function(value) {
            return this.each(function(profile) {
                profile.properties.selMode = value || 'single';
            });
        },
        
        getValueSeparator: function() {
            var profile = this.get(0);
            return profile ? profile.properties.valueSeparator : ',';
        },
        
        setValueSeparator: function(value) {
            return this.each(function(profile) {
                profile.properties.valueSeparator = value || ',';
            });
        },

        getPullRefresh: function() {
            var profile = this.get(0);
            return profile ? profile.properties.pullRefresh : false;
        },
        
        setPullRefresh: function(value) {
            return this.each(function(profile) {
                profile.properties.pullRefresh = !!value;
            });
        },
        
        // 事件处理器的getter/setter
        getOnItemClick: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onItemClick : null;
        },
        
        setOnItemClick: function(value) {
            return this.each(function(profile) {
                profile.properties.onItemClick = value;
            });
        },
        
        getOnAfterItemClick: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onAfterItemClick : null;
        },
        
        setOnAfterItemClick: function(value) {
            return this.each(function(profile) {
                profile.properties.onAfterItemClick = value;
            });
        },
        
        getOnChanged: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onChanged : null;
        },
        
        setOnChanged: function(value) {
            return this.each(function(profile) {
                profile.properties.onChanged = value;
            });
        },
        
        getOnChecked: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onChecked : null;
        },
        
        setOnChecked: function(value) {
            return this.each(function(profile) {
                profile.properties.onChecked = value;
            });
        },
        
        getOnItemSelected: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onItemSelected : null;
        },
        
        setOnItemSelected: function(value) {
            return this.each(function(profile) {
                profile.properties.onItemSelected = value;
            });
        },
        
        // 数据准备方法（模板渲染前调用）
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 处理根级别样式类
            var classes = ['ood-mobile-list', 'ood-mobile-component'];
            if (props.theme === 'dark') classes.push('ood-mobile-list-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classes.push('ood-mobile-list-hc');
            if (props.readonly) classes.push('ood-mobile-list-readonly');
            
            data._styleClasses = classes.join(' ');
            data._itemsStyle = this._buildItemsStyle(props);
            
            // 设置响应式类
            var screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            var isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            data._screenSize = screenSize;
            data._isMobile = isMobile;
            
            // 合并className
            var classNames = ['ood-mobile-list', 'ood-mobile-component'];
            if (props.theme === 'dark') classNames.push('ood-mobile-list-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classNames.push('ood-mobile-list-hc');
            if (props.readonly) classNames.push('ood-mobile-list-readonly');
            if (isMobile) classNames.push('mobile-device');
            classNames.push('list-' + screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            // 设置样式
            data._style = props.style || '';
            
            // 设置标签显示控制
            data._labelShow = props.label ? 'block' : 'none';
            
            // 设置标签尺寸
            data._labelSize = props.labelSize || 'auto';
            
            // 设置标签对齐方式
            data._labelHAlign = props.labelHAlign || '';
            data._labelVAlign = props.labelVAlign || '';
            
            // 设置必填标记
            data._required = props.required ? 'required' : '';
            
            // 设置标签标题
            data.labelCaption = props.labelCaption || props.label || '';
            return data;
        },
        
        // 构建容器样式
        _buildItemsStyle: function(props) {
            var styles = [];
            if (props.backgroundColor) {
                styles.push('background-color: ' + props.backgroundColor);
            }
            if (props.height && props.height !== '100%') {
                styles.push('height: ' + props.height);
            }
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 准备单个列表项数据 - 处理所有模板变量
        _prepareItem: function(profile, dataItem, item, pid, i, l, mapCache, serialId) {
            // 调用父类方法
            if (arguments.callee.upper) {
                arguments.callee.upper.call(this, profile, dataItem, item, pid, i, l, mapCache, serialId);
            }
            
            var props = profile.properties;
            var isSelected = item.selected || false;
            var isDisabled = item.disabled || false;
            var isReadonly = item.readonly || props.readonly || false;
            
            // 基础数据属性
            dataItem.id = item.id || 'list-item-' + i;
            dataItem.title = item.title || item.text || item.caption || '';
            dataItem.subtitle = item.subtitle || item.description || '';
            dataItem.rightText = item.rightText || item.value || '';
            dataItem.badge = item.badge;
            dataItem.disabled = isDisabled ? 'disabled' : '';
            dataItem.readonly = isReadonly ? 'readonly' : '';
            
            // 索引和状态
            dataItem._itemIndex = i;
            dataItem._tabindex = isDisabled ? '-1' : '0';
            
            // 状态类和样式
            dataItem._itemState = this._buildItemStateClass(isSelected, isDisabled, isReadonly);
            dataItem._itemDisplay = 'display: flex;';
            
            // 图标相关变量
            dataItem.imageClass = item.imageClass || item.icon || '';
            dataItem.iconFontCode = this._getIconFontCode(item.icon || item.imageClass);
            dataItem.iconStyle = this._buildIconStyle(item, props);
            dataItem.imageDisplay = (item.imageClass || item.icon) ? 'display: inline-block;' : 'display: none;';
            
            // 显示控制变量
            dataItem._subtitleDisplay = dataItem.subtitle ? 'display: block;' : 'display: none;';
            dataItem._rightDisplay = dataItem.rightText ? 'display: block;' : 'display: none;';
            dataItem._badgeDisplay = (item.badge !== null && item.badge !== undefined) ? 'display: inline-block;' : 'display: none;';
            
            // 自定义样式
            dataItem.itemClass = item.itemClass || item.className || '';
            dataItem.itemStyle = this._buildItemStyle(item, isSelected, props);
            
            return dataItem;
        },
        
        // 构建项目状态类
        _buildItemStateClass: function(isSelected, isDisabled, isReadonly) {
            var classes = [];
            if (isSelected) classes.push('selected');
            if (isDisabled) classes.push('disabled');
            if (isReadonly) classes.push('readonly');
            return classes.join(' ');
        },
        
        // 获取图标字体代码
        _getIconFontCode: function(icon) {
            if (!icon) return '';
            // 处理不同的图标字体格式
            if (icon.startsWith('ri-')) {
                return ''; // Remix Icon 使用CSS类
            } else if (icon.startsWith('fa-')) {
                return ''; // Font Awesome 使用CSS类
            } else if (/^\\[a-fA-F0-9]{4}$/.test(icon)) {
                return icon; // Unicode 字符
            }
            return '';
        },
        
        // 构建图标样式
        _buildIconStyle: function(item, props) {
            var styles = [];
            
            // 基础图标样式
            if (item.iconStyle) {
                styles.push(item.iconStyle);
            }
            
            // 主题颜色
            if (props.textColor) {
                styles.push('color: ' + props.textColor);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 构建项目样式
        _buildItemStyle: function(item, isSelected, props) {
            var styles = [];
            
            // 基础项目样式
            if (item.itemStyle) {
                styles.push(item.itemStyle);
            }
            
            // 选中状态背景
            if (isSelected && props.selectedBackgroundColor) {
                styles.push('background-color: ' + props.selectedBackgroundColor);
            }
            
            // 自定义宽度
            if (item.width) {
                styles.push('width: ' + item.width);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // ood.absValue 静态方法
        _isFormField: function() {
            return true; // 列表组件可以作为表单字段
        },
        
        _ensureValue: function(value) {
            // 确保值的类型正确
            if (value === null || value === undefined) return null;
            if (typeof value === 'string' || typeof value === 'number') {
                return value.toString();
            }
            return null;
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                // 使用 ood 规范的初始化
                profile.boxing().initListFeatures();
                
                // 设置默认值
                var value = profile.properties.value;
                if (value !== null && value !== undefined) {
                    profile.boxing()._setCtrlValue(value);
                }
                
                // 触发准备就绪事件
                if (profile.onReady) {
                    profile.boxing().onReady(profile);
                }
            });
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // List组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                containerNode = profile.getSubNode('CONTAINER'),
                itemsNode = profile.getSubNode('ITEMS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整列表容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    containerNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整列表容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    containerNode.css('height', '100%');
                    
                    // 调整列表项容器的高度以适应容器
                    if (itemsNode && !itemsNode.isEmpty()) {
                        // 计算可用高度（减去头部和底部的高度）
                        var headerHeight = profile.getSubNode('HEADER').offsetHeight(true) || 0;
                        var footerHeight = profile.getSubNode('FOOTER').offsetHeight(true) || 0;
                        var availableHeight = pxHeight - headerHeight - footerHeight;
                        
                        itemsNode.css('height', adjustunit(availableHeight));
                    }
                }
            }

            // 调整内部布局以适应新尺寸
            this.boxing().adjustLayout();
        },

        EventHandlers: {
            // ood.UI 生命周期事件处理器
            onReady: function(profile) {
                // 组件就绪时的初始化
                profile.boxing().initListFeatures();
            },
            
            onCreated: function(profile) {
                // 组件创建完成时的处理
            },
            
            onDestroy: function(profile) {
                // 组件销毁时的清理
            },
            
            // ood.absValue 事件处理器
            onChanged: function(profile, e, src, value) {
                // 值改变事件
            },
            
            onChecked: function(profile, e, src, value) {
                // 选中事件
            },
            
            onValueSet: function(profile, oldValue, newValue) {
                // 值设置事件
            },
            
            // ood.absList 事件处理器
            onItemSelected: function(profile, item, e, src, type) {
                // 列表项选中事件
            },
            
            onItemAdded: function(profile, items, index) {
                // 列表项添加事件
            },
            
            onItemRemoved: function(profile, items, indices) {
                // 列表项删除事件
            },
            
            // 列表特定事件处理器
            onItemClick: function(profile, index, data, event) {
                // 列表项点击事件处理器
            },
            
            onAfterItemClick: function(profile, event) {
                // 列表项点击后事件处理器
            }
        }
    }
});/**
 * 移动端徽章组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持数字徽章、点徽章、状态徽章等多种类型
 */
ood.Class("ood.Mobile.Badge", ["ood.UI"], {
    Instance: {
        Initialize: function() {
          //  this.constructor.upper.prototype.Initialize.call(this);
            this.initBadgeFeatures();
        },
        
        initBadgeFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-badge');
            this.updateBadgeDisplay();
        },
        
        updateBadgeDisplay: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var badgeNode = profile.getSubNode('BADGE');
            
            // 设置徽章内容和样式
            this.setBadgeContent(props.count, props.text);
            this.setBadgeType(props.type);
            this.setBadgePosition(props.position);
            
            // 显示/隐藏徽章
            if (this.shouldShowBadge()) {
                badgeNode.css('display', 'block');
            } else {
                badgeNode.css('display', 'none');
            }
        },
        
        setBadgeContent: function(count, text) {
            var profile = this.get(0);
            var badgeNode = profile.getSubNode('BADGE');
            var props = profile.properties;
            
            // 移除所有类型类
            badgeNode.removeClass('ood-mobile-badge-dot ood-mobile-badge-text ood-mobile-badge-count');
            
            if (text) {
                // 文本徽章
                badgeNode.addClass('ood-mobile-badge-text');
                badgeNode.html(text);
            } else if (typeof count === 'number') {
                if (count > 0) {
                    // 数字徽章
                    badgeNode.addClass('ood-mobile-badge-count');
                    
                    var displayCount = count;
                    if (count > props.maxCount) {
                        displayCount = props.maxCount + '+';
                    }
                    
                    badgeNode.html(displayCount);
                } else {
                    // 点徽章
                    badgeNode.addClass('ood-mobile-badge-dot');
                    badgeNode.html('');
                }
            } else {
                // 点徽章
                badgeNode.addClass('ood-mobile-badge-dot');
                badgeNode.html('');
            }
        },
        
        setBadgeType: function(type) {
            var profile = this.get(0);
            var badgeNode = profile.getSubNode('BADGE');
            
            // 移除所有状态类
            badgeNode.removeClass('ood-mobile-badge-primary ood-mobile-badge-success ood-mobile-badge-warning ood-mobile-badge-danger ood-mobile-badge-info');
            
            // 添加新状态类
            if (type) {
                badgeNode.addClass('ood-mobile-badge-' + type);
            } else {
                badgeNode.addClass('ood-mobile-badge-danger'); // 默认红色
            }
        },
        
        setBadgePosition: function(position) {
            var profile = this.get(0);
            var badgeNode = profile.getSubNode('BADGE');
            
            // 移除所有位置类
            badgeNode.removeClass('ood-mobile-badge-top-right ood-mobile-badge-top-left ood-mobile-badge-bottom-right ood-mobile-badge-bottom-left');
            
            // 添加新位置类
            if (position) {
                badgeNode.addClass('ood-mobile-badge-' + position);
            } else {
                badgeNode.addClass('ood-mobile-badge-top-right'); // 默认右上角
            }
        },
        
        shouldShowBadge: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            // 如果showZero为false且count为0，不显示
            if (!props.showZero && props.count === 0) {
                return false;
            }
            
            // 如果有文本内容，显示
            if (props.text) {
                return true;
            }
            
            // 如果count大于0，显示
            if (typeof props.count === 'number' && props.count > 0) {
                return true;
            }
            
            // 如果dot为true，显示点徽章
            if (props.dot) {
                return true;
            }
            
            return false;
        },
        
        setCount: function(count) {
            var profile = this.get(0);
            profile.properties.count = count;
            this.updateBadgeDisplay();
        },
        
        getCount: function() {
            var profile = this.get(0);
            return profile.properties.count || 0;
        },
        
        setText: function(text) {
            var profile = this.get(0);
            profile.properties.text = text;
            this.updateBadgeDisplay();
        },
        
        getText: function() {
            var profile = this.get(0);
            return profile.properties.text || '';
        },
        
        setType: function(type) {
            var profile = this.get(0);
            profile.properties.type = type;
            this.setBadgeType(type);
        },
        
        show: function() {
            var profile = this.get(0);
            var badgeNode = profile.getSubNode('BADGE');
            badgeNode.css('display', 'block');
        },
        
        hide: function() {
            var profile = this.get(0);
            var badgeNode = profile.getSubNode('BADGE');
            badgeNode.css('display', 'none');
        },
        
        setContent: function(content) {
            var profile = this.get(0);
            var contentNode = profile.getSubNode('CONTENT');
            
            if (typeof content === 'string') {
                contentNode.html(content);
            } else {
                contentNode.empty().append(content);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-badge-wrapper',
            style: '{_style}',
            
            CONTENT: {
                tagName: 'div',
                className: 'ood-mobile-badge-content',
                text: '{content}'
            },
            
            BADGE: {
                tagName: 'div',
                className: 'ood-mobile-badge ood-mobile-badge-{type} ood-mobile-badge-{position}',
                style: 'display: {_badgeDisplay}'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                display: 'inline-block'
            },
            
            CONTENT: {
                position: 'relative',
                'z-index': 1
            },
            
            BADGE: {
                position: 'absolute',
                'z-index': 2,
                'font-size': 'var(--mobile-font-xs)',
                'font-weight': '600',
                'line-height': '1',
                'white-space': 'nowrap',
                'text-align': 'center',
                color: '#FFFFFF',
                'border-radius': '10px',
                'box-shadow': '0 0 0 1px var(--mobile-bg-primary)'
            },
            
            // 徽章类型样式
            'BADGE.ood-mobile-badge-primary': {
                'background-color': 'var(--mobile-primary)'
            },
            
            'BADGE.ood-mobile-badge-success': {
                'background-color': 'var(--mobile-success)'
            },
            
            'BADGE.ood-mobile-badge-warning': {
                'background-color': 'var(--mobile-warning)'
            },
            
            'BADGE.ood-mobile-badge-danger': {
                'background-color': 'var(--mobile-danger)'
            },
            
            'BADGE.ood-mobile-badge-info': {
                'background-color': 'var(--mobile-info)'
            },
            
            // 点徽章样式
            'BADGE.ood-mobile-badge-dot': {
                width: '8px',
                height: '8px',
                'border-radius': '50%',
                padding: 0,
                'min-width': 'auto'
            },
            
            // 数字徽章样式
            'BADGE.ood-mobile-badge-count': {
                'min-width': '16px',
                height: '16px',
                padding: '0 4px',
                'font-size': '10px',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            // 文本徽章样式
            'BADGE.ood-mobile-badge-text': {
                height: '16px',
                padding: '0 6px',
                'font-size': '10px',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            // 位置样式
            'BADGE.ood-mobile-badge-top-right': {
                top: 0,
                right: 0,
                transform: 'translate(50%, -50%)'
            },
            
            'BADGE.ood-mobile-badge-top-left': {
                top: 0,
                left: 0,
                transform: 'translate(-50%, -50%)'
            },
            
            'BADGE.ood-mobile-badge-bottom-right': {
                bottom: 0,
                right: 0,
                transform: 'translate(50%, 50%)'
            },
            
            'BADGE.ood-mobile-badge-bottom-left': {
                bottom: 0,
                left: 0,
                transform: 'translate(-50%, 50%)'
            },
            
            // 点徽章位置调整
            'BADGE.ood-mobile-badge-dot.ood-mobile-badge-top-right': {
                top: '2px',
                right: '2px',
                transform: 'none'
            },
            
            'BADGE.ood-mobile-badge-dot.ood-mobile-badge-top-left': {
                top: '2px',
                left: '2px',
                transform: 'none'
            },
            
            'BADGE.ood-mobile-badge-dot.ood-mobile-badge-bottom-right': {
                bottom: '2px',
                right: '2px',
                transform: 'none'
            },
            
            'BADGE.ood-mobile-badge-dot.ood-mobile-badge-bottom-left': {
                bottom: '2px',
                left: '2px',
                transform: 'none'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '徽章标题',
                ini: '徽章',
                action: function(value) {
                    var profile = this;
                    // 更新内容显示
                    profile.properties.content = value;
                    profile.getRoot().attr('aria-label', value || '徽章');
                }
            },
            
            width: {
                caption: '徽章宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            height: {
                caption: '徽章高度',
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
                    var badgeNode = this.getSubNode('BADGE');
                    if (value && badgeNode && !badgeNode.isEmpty()) {
                        badgeNode.css('background-color', value);
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
                    var badgeNode = this.getSubNode('BADGE');
                    if (value && badgeNode && !badgeNode.isEmpty()) {
                        badgeNode.css('color', value);
                    }
                }
            },
            
            // ===== 徽章特有属性 =====
            count: {
                caption: '数字徽章',
                ini: 0,
                action: function(value) {
                    this.boxing().setCount(value);
                }
            },
            
            text: {
                caption: '文字徽章',
                ini: '',
                action: function(value) {
                    this.boxing().setText(value);
                }
            },
            
            type: {
                ini: 'danger',
                listbox: ['primary', 'success', 'warning', 'danger', 'info'],
                action: function(value) {
                    this.boxing().setType(value);
                }
            },
            
            position: {
                ini: 'top-right',
                listbox: ['top-right', 'top-left', 'bottom-right', 'bottom-left']
            },
            
            maxCount: {
                ini: 99
            },
            
            showZero: {
                ini: false
            },
            
            dot: {
                ini: false
            },
            
            content: {
                ini: ''
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Badge组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                badgeNode = profile.getSubNode('BADGE'),
                contentNode = profile.getSubNode('CONTENT'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整徽章容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    contentNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整徽章容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    contentNode.css('height', '100%');
                }
            }

            // 根据新的尺寸调整徽章位置
            if (width || height) {
                // 重新计算徽章的定位，确保在容器内的正确位置
                var position = prop.position || 'top-right';
                var badgeSize = badgeNode.cssSize();
                
                // 根据位置调整徽章的偏移量
                switch(position) {
                    case 'top-right':
                        badgeNode.css({
                            'top': '0',
                            'right': '0'
                        });
                        break;
                    case 'top-left':
                        badgeNode.css({
                            'top': '0',
                            'left': '0'
                        });
                        break;
                    case 'bottom-right':
                        badgeNode.css({
                            'bottom': '0',
                            'right': '0'
                        });
                        break;
                    case 'bottom-left':
                        badgeNode.css({
                            'bottom': '0',
                            'left': '0'
                        });
                        break;
                }
            }
        },

        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            data._badgeDisplay = profile.boxing().shouldShowBadge() ? 'block' : 'none';
            
            return data;
        }
    }
});/**
 * 移动端卡片组件
 * 继承自 ood.UI（UI基类），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持标题、内容、操作按钮、图片等多种布局
 */
ood.Class("ood.Mobile.Card", "ood.UI", {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['COVER', 'HEADER', 'TITLE', 'EXTRA', 'CONTENT', 'FOOTER', 'ACTIONS']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
           // this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端卡片特性
            this.initMobileCardFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileCardFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端卡片CSS类
            profile.getRoot().addClass('ood-mobile-card ood-mobile-component');
            
            // 初始化卡片功能
            this.initCardFeatures();
            
            // 绑定事件
            this.bindTouchEvents();
        },
        
        initCardFeatures: function() {
            // 原有的卡片特性初始化逻辑
            this.updateCardStyle();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var root = profile.getRoot();
            var actionsContainer = profile.getSubNode('ACTIONS');
            
            // 卡片点击事件
            root.on('click', function(e) {
                // 如果点击的是操作按钮区域，不触发卡片点击事件
                if (ood(e.target).closest('.ood-mobile-card-actions').length === 0) {
                    self.onCardClick(e);
                }
            });
            
            // 操作按钮点击事件
            actionsContainer.on('click', '.ood-mobile-card-action', function(e) {
                e.stopPropagation();
                var btn = ood(this);
                var index = parseInt(btn.attr('data-index'));
                var action = self._actions[index];
                
                if (action && action.handler) {
                    action.handler.call(self, action, index, e);
                }
                
                self.onActionClick(action, index, e);
            });
        },
        
        updateCardStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 应用样式类
            root.removeClass('ood-mobile-card-bordered ood-mobile-card-shadow ood-mobile-card-hoverable');
            
            if (props.bordered) root.addClass('ood-mobile-card-bordered');
            if (props.shadow) root.addClass('ood-mobile-card-shadow');
            if (props.hoverable) root.addClass('ood-mobile-card-hoverable');
        },
        
        setTitle: function(title) {
            var profile = this.get(0);
            var titleNode = profile.getSubNode('TITLE');
            var header = profile.getSubNode('HEADER');
            
            profile.properties.title = title;
            
            if (title) {
                titleNode.html(title);
                header.css('display', 'block');
            } else {
                // 如果没有标题且没有额外内容，隐藏头部
                var extra = profile.getSubNode('EXTRA');
                if (!extra.html().trim()) {
                    header.css('display', 'none');
                }
            }
        },
        
        setExtra: function(extra) {
            var profile = this.get(0);
            var extraNode = profile.getSubNode('EXTRA');
            var header = profile.getSubNode('HEADER');
            
            if (typeof extra === 'string') {
                extraNode.html(extra);
            } else {
                extraNode.empty().append(extra);
            }
            
            // 如果有内容，显示头部
            if (extra) {
                header.css('display', 'block');
            }
        },
        
        setContent: function(content) {
            var profile = this.get(0);
            var contentNode = profile.getSubNode('CONTENT');
            
            if (typeof content === 'string') {
                contentNode.html(content);
            } else {
                contentNode.empty().append(content);
            }
        },
        
        setCover: function(src) {
            var profile = this.get(0);
            var cover = profile.getSubNode('COVER');
            var coverImg = profile.getSubNode('COVER_IMG');
            
            profile.properties.cover = src;
            
            if (src) {
                coverImg.attr('src', src);
                cover.css('display', 'block');
            } else {
                cover.css('display', 'none');
            }
        },
        
        setActions: function(actions) {
            this._actions = actions || [];
            this.renderActions();
        },
        
        renderActions: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('ACTIONS');
            var footer = profile.getSubNode('FOOTER');
            
            if (!this._actions || this._actions.length === 0) {
                footer.css('display', 'none');
                return;
            }
            
            footer.css('display', 'block');
            container.html('');
            
            for (var i = 0; i < this._actions.length; i++) {
                var action = this._actions[i];
                var actionElement = this.createActionElement(action, i);
                container.append(actionElement);
            }
        },
        
        createActionElement: function(action, index) {
            var btnClass = 'ood-mobile-card-action';
            if (action.type) {
                btnClass += ' ood-mobile-card-action-' + action.type;
            }
            
            var btn = ood('<button class="' + btnClass + '" data-index="' + index + '"></button>');
            
            // 图标
            if (action.icon) {
                var icon = ood('<i class="ood-mobile-card-action-icon ' + action.icon + '"></i>');
                btn.append(icon);
            }
            
            // 文本
            if (action.text) {
                var text = ood('<span class="ood-mobile-card-action-text">' + action.text + '</span>');
                btn.append(text);
            }
            
            // 禁用状态
            if (action.disabled) {
                btn.addClass('ood-mobile-card-action-disabled');
                btn.attr('disabled', true);
            }
            
            return btn;
        },
        
        onCardClick: function(e) {
            var profile = this.get(0);
            
            if (profile.onCardClick) {
                profile.boxing().onCardClick(profile, e);
            }
        },
        
        onActionClick: function(action, index, e) {
            var profile = this.get(0);
            
            if (profile.onActionClick) {
                profile.boxing().onActionClick(profile, action, index, e);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-card {_styleClasses}',
            style: '{_style}',
            
            COVER: {
                tagName: 'div',
                className: 'ood-mobile-card-cover',
                style: 'display: {_coverDisplay}',
                
                COVER_IMG: {
                    tagName: 'img',
                    className: 'ood-mobile-card-cover-img',
                    src: '{cover}',
                    alt: ''
                }
            },
            
            HEADER: {
                tagName: 'div',
                className: 'ood-mobile-card-header',
                style: 'display: {_headerDisplay}',
                
                TITLE: {
                    tagName: 'div',
                    className: 'ood-mobile-card-title',
                    text: '{title}'
                },
                
                EXTRA: {
                    tagName: 'div',
                    className: 'ood-mobile-card-extra'
                }
            },
            
            CONTENT: {
                tagName: 'div',
                className: 'ood-mobile-card-content',
                text: '{content}'
            },
            
            FOOTER: {
                tagName: 'div',
                className: 'ood-mobile-card-footer',
                style: 'display: {_footerDisplay}',
                
                ACTIONS: {
                    tagName: 'div',
                    className: 'ood-mobile-card-actions'
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                'background-color': 'var(--mobile-bg-primary)',
                'border-radius': 'var(--mobile-border-radius)',
                overflow: 'hidden',
                transition: 'all 0.2s ease'
            },
            
            'KEY.ood-mobile-card-bordered': {
                border: '1px solid var(--mobile-border-color)'
            },
            
            'KEY.ood-mobile-card-shadow': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            'KEY.ood-mobile-card-hoverable': {
                cursor: 'pointer'
            },
            
            'KEY.ood-mobile-card-hoverable:hover': {
                'box-shadow': 'var(--mobile-shadow-medium)',
                transform: 'translateY(-2px)'
            },
            
            COVER: {
                position: 'relative',
                width: '100%',
                'padding-bottom': '56.25%', // 16:9 aspect ratio
                overflow: 'hidden'
            },
            
            COVER_IMG: {
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                'object-fit': 'cover'
            },
            
            HEADER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-md) 0'
            },
            
            TITLE: {
                flex: 1,
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4',
                'margin-right': 'var(--mobile-spacing-sm)'
            },
            
            EXTRA: {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'flex-shrink': 0
            },
            
            CONTENT: {
                padding: 'var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-md)',
                'line-height': '1.6',
                color: 'var(--mobile-text-primary)'
            },
            
            FOOTER: {
                padding: '0 var(--mobile-spacing-md) var(--mobile-spacing-md)',
                'border-top': '1px solid var(--mobile-border-color)',
                'margin-top': 'var(--mobile-spacing-md)'
            },
            
            ACTIONS: {
                display: 'flex',
                gap: 'var(--mobile-spacing-sm)',
                'flex-wrap': 'wrap'
            },
            
            '.ood-mobile-card-action': {
                display: 'flex',
                'align-items': 'center',
                gap: 'var(--mobile-spacing-xs)',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)',
                border: '1px solid var(--mobile-border-color)',
                'background-color': 'transparent',
                'border-radius': 'var(--mobile-border-radius)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-primary)',
                cursor: 'pointer',
                transition: 'all 0.2s ease'
            },
            
            '.ood-mobile-card-action:hover': {
                'background-color': 'var(--mobile-bg-secondary)',
                'border-color': 'var(--mobile-primary)'
            },
            
            '.ood-mobile-card-action-primary': {
                'background-color': 'var(--mobile-primary)',
                'border-color': 'var(--mobile-primary)',
                color: '#FFFFFF'
            },
            
            '.ood-mobile-card-action-primary:hover': {
                opacity: 0.9
            },
            
            '.ood-mobile-card-action-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-card-action-icon': {
                'font-size': '14px'
            },
            
            '.ood-mobile-card-action-text': {
                'white-space': 'nowrap'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            // 卡片标题（显示值）- 对应模板中的 {title}
            title: {
                caption: '卡片标题',
                ini: '卡片标题',
                action: function(value) {
                    this.boxing().setTitle(value);
                }
            },
            
            // 卡片宽度
            width: {
                caption: '卡片宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // 卡片高度
            height: {
                caption: '卡片高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 卡片背景色（颜色选择器）
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            // 卡片标题色（颜色选择器）
            titleColor: {
                caption: '标题颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var titleNode = this.getSubNode('TITLE');
                        if (titleNode && !titleNode.isEmpty()) {
                            titleNode.css('color', value);
                        }
                    }
                }
            },
            
            // 卡片图标（图标选择器）
            icon: {
                caption: '卡片图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var iconNode = profile.getSubNode('ICON');
                    
                    if (value && iconNode && !iconNode.isEmpty()) {
                        iconNode.attr('class', 'ood-mobile-card-icon ' + value);
                        iconNode.css('display', 'inline-block');
                    } else if (iconNode && !iconNode.isEmpty()) {
                        iconNode.css('display', 'none');
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
            
            content: {
                caption: '卡片内容',
                ini: '',
                action: function(value) {
                    this.boxing().setContent(value);
                }
            },
            
            cover: {
                caption: '封面图片',
                ini: '',
                action: function(value) {
                    this.boxing().setCover(value);
                }
            },
            
            actions: {
                caption: '操作按钮组',
                ini: [],
                action: function(value) {
                    this.boxing().setActions(value);
                }
            },
            
            bordered: {
                caption: '显示边框',
                ini: true,
                action: function(value) {
                    this.boxing().updateCardStyle();
                }
            },
            
            shadow: {
                caption: '显示阴影',
                ini: true,
                action: function(value) {
                    this.boxing().updateCardStyle();
                }
            },
            
            hoverable: {
                caption: '悬停效果',
                ini: false,
                action: function(value) {
                    this.boxing().updateCardStyle();
                }
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Card组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                coverNode = profile.getSubNode('COVER'),
                headerNode = profile.getSubNode('HEADER'),
                contentNode = profile.getSubNode('CONTENT'),
                footerNode = profile.getSubNode('FOOTER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整卡片容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    
                    // 调整内部元素宽度
                    coverNode.css('width', '100%');
                    headerNode.css('width', '100%');
                    contentNode.css('width', '100%');
                    footerNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整卡片容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    
                    // 计算内容区域可用高度
                    var headerHeight = headerNode.offsetHeight(true) || 0;
                    var footerHeight = footerNode.offsetHeight(true) || 0;
                    var coverHeight = coverNode.offsetHeight(true) || 0;
                    var availableHeight = pxHeight - headerHeight - footerHeight - coverHeight;
                    
                    // 调整内容区域高度
                    contentNode.css('height', adjustunit(availableHeight));
                }
            }

            // 根据新的尺寸调整卡片样式
            if (width || height) {
                // 如果宽度较小，调整内部布局
                if (width && profile.$px(width) < 300) {
                    root.addClass('ood-mobile-card-compact');
                } else {
                    root.removeClass('ood-mobile-card-compact');
                }
            }
        },

        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            var classes = [];
            if (props.bordered) classes.push('ood-mobile-card-bordered');
            if (props.shadow) classes.push('ood-mobile-card-shadow');
            if (props.hoverable) classes.push('ood-mobile-card-hoverable');
            
            data._styleClasses = classes.join(' ');
            data._coverDisplay = props.cover ? 'block' : 'none';
            data._headerDisplay = (props.title || props.extra) ? 'flex' : 'none';
            data._footerDisplay = (props.actions && props.actions.length > 0) ? 'block' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onCardClick: function(profile, event) {
                // 卡片点击事件处理器
            },
            
            onActionClick: function(profile, action, index, event) {
                // 操作按钮点击事件处理器
            }
        }
    }
});/**
 * 移动端操作面板组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持底部弹出、操作列表、取消按钮等功能
 */
ood.Class("ood.Mobile.ActionSheet", ["ood.UI.Widget", "ood.absList"], {
    Instance: {
        Initialize: function() {
           // this.constructor.upper.prototype.Initialize.call(this);
            this.initActionSheetFeatures();
        },
        
        initActionSheetFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-actionsheet');
            this.initActionSheetState();
            this.initActions();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var cancelBtn = profile.getSubNode('CANCEL');
            var actionsContainer = profile.getSubNode('ACTIONS');
            
            // 遮罩点击关闭
            overlay.on('click', function(e) {
                if (e.target === overlay.get(0)) {
                    self.close();
                }
            });
            
            // 取消按钮
            cancelBtn.on('click', function(e) {
                self.onCancel();
            });
            
            // 操作项点击
            actionsContainer.on('click', '.ood-mobile-actionsheet-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var action = self._actions[index];
                
                if (action && !action.disabled) {
                    self.onActionClick(action, index);
                }
            });
        },
        
        initActionSheetState: function() {
            this._isVisible = false;
        },
        
        initActions: function() {
            this._actions = [];
            
            var profile = this.get(0);
            if (profile.properties.actions) {
                this.setActions(profile.properties.actions);
            }
        },
        
        show: function() {
            if (this._isVisible) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var sheet = profile.getSubNode('SHEET');
            
            this._isVisible = true;
            
            // 显示遮罩
            overlay.css('display', 'flex');
            
            // 添加显示动画
            setTimeout(function() {
                overlay.addClass('ood-mobile-actionsheet-overlay-show');
                sheet.addClass('ood-mobile-actionsheet-show');
            }, 10);
            
            // 阻止页面滚动
            ood('body').addClass('ood-mobile-actionsheet-open');
            
            this.onShow();
        },
        
        close: function() {
            if (!this._isVisible) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var sheet = profile.getSubNode('SHEET');
            
            this._isVisible = false;
            
            // 移除显示动画
            overlay.removeClass('ood-mobile-actionsheet-overlay-show');
            sheet.removeClass('ood-mobile-actionsheet-show');
            
            // 隐藏遮罩
            var self = this;
            setTimeout(function() {
                overlay.css('display', 'none');
                self.onClose();
            }, 300);
            
            // 恢复页面滚动
            ood('body').removeClass('ood-mobile-actionsheet-open');
        },
        
        setActions: function(actions) {
            this._actions = actions || [];
            this.renderActions();
        },
        
        getActions: function() {
            return this._actions;
        },
        
        renderActions: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('ACTIONS');
            
            container.html('');
            
            for (var i = 0; i < this._actions.length; i++) {
                var action = this._actions[i];
                var actionElement = this.createActionElement(action, i);
                container.append(actionElement);
            }
        },
        
        createActionElement: function(action, index) {
            var itemClass = 'ood-mobile-actionsheet-item';
            
            if (action.type) {
                itemClass += ' ood-mobile-actionsheet-item-' + action.type;
            }
            
            if (action.disabled) {
                itemClass += ' ood-mobile-actionsheet-item-disabled';
            }
            
            var item = ood('<div class="' + itemClass + '" data-index="' + index + '"></div>');
            
            // 图标
            if (action.icon) {
                var icon = ood('<i class="ood-mobile-actionsheet-icon ' + action.icon + '"></i>');
                item.append(icon);
            }
            
            // 文本
            var text = ood('<span class="ood-mobile-actionsheet-text">' + (action.text || action.label) + '</span>');
            item.append(text);
            
            // 描述
            if (action.description) {
                var desc = ood('<span class="ood-mobile-actionsheet-description">' + action.description + '</span>');
                item.append(desc);
            }
            
            return item;
        },
        
        setTitle: function(title) {
            var profile = this.get(0);
            var titleNode = profile.getSubNode('TITLE');
            var header = profile.getSubNode('HEADER');
            
            profile.properties.title = title;
            
            if (title) {
                titleNode.html(title);
                header.css('display', 'block');
            } else {
                header.css('display', 'none');
            }
        },
        
        setDescription: function(description) {
            var profile = this.get(0);
            var descNode = profile.getSubNode('DESCRIPTION');
            var header = profile.getSubNode('HEADER');
            
            profile.properties.description = description;
            
            if (description) {
                descNode.html(description);
                descNode.css('display', 'block');
                header.css('display', 'block');
            } else {
                descNode.css('display', 'none');
            }
        },
        
        isVisible: function() {
            return this._isVisible;
        },
        
        onShow: function() {
            var profile = this.get(0);
            
            if (profile.onShow) {
                profile.boxing().onShow(profile);
            }
        },
        
        onClose: function() {
            var profile = this.get(0);
            
            if (profile.onClose) {
                profile.boxing().onClose(profile);
            }
        },
        
        onActionClick: function(action, index) {
            var profile = this.get(0);
            
            // 执行操作的处理函数
            if (action.handler) {
                action.handler.call(this, action, index);
            }
            
            // 触发全局事件
            if (profile.onActionClick) {
                profile.boxing().onActionClick(profile, action, index);
            }
            
            // 默认关闭操作面板
            if (action.autoClose !== false) {
                this.close();
            }
        },
        
        onCancel: function() {
            var profile = this.get(0);
            
            if (profile.onCancel) {
                profile.boxing().onCancel(profile);
            }
            
            this.close();
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-actionsheet',
            style: '{_style}',
            
            OVERLAY: {
                tagName: 'div',
                className: 'ood-mobile-actionsheet-overlay',
                style: 'display: none',
                
                SHEET: {
                    tagName: 'div',
                    className: 'ood-mobile-actionsheet-container',
                    
                    HEADER: {
                        tagName: 'div',
                        className: 'ood-mobile-actionsheet-header',
                        style: 'display: {_headerDisplay}',
                        
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-mobile-actionsheet-title',
                            text: '{title}'
                        },
                        
                        DESCRIPTION: {
                            tagName: 'div',
                            className: 'ood-mobile-actionsheet-description',
                            text: '{description}',
                            style: 'display: {_descriptionDisplay}'
                        }
                    },
                    
                    ACTIONS: {
                        tagName: 'div',
                        className: 'ood-mobile-actionsheet-actions'
                    },
                    
                    CANCEL: {
                        tagName: 'div',
                        className: 'ood-mobile-actionsheet-cancel',
                        style: 'display: {_cancelDisplay}',
                        text: '{cancelText}'
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative'
            },
            
            OVERLAY: {
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                'background-color': 'rgba(0,0,0,0.5)',
                'z-index': 1200,
                display: 'flex',
                'align-items': 'flex-end',
                'justify-content': 'center',
                opacity: 0,
                transition: 'opacity 0.3s ease'
            },
            
            'OVERLAY.ood-mobile-actionsheet-overlay-show': {
                opacity: 1
            },
            
            SHEET: {
                'background-color': 'var(--mobile-bg-primary)',
                'border-radius': 'var(--mobile-border-radius) var(--mobile-border-radius) 0 0',
                'max-height': '80vh',
                width: '100%',
                'max-width': '100%',
                transform: 'translateY(100%)',
                transition: 'transform 0.3s ease',
                'padding-bottom': 'var(--mobile-safe-bottom)',
                overflow: 'hidden'
            },
            
            'SHEET.ood-mobile-actionsheet-show': {
                transform: 'translateY(0)'
            },
            
            HEADER: {
                padding: 'var(--mobile-spacing-lg)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'text-align': 'center'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4'
            },
            
            'HEADER DESCRIPTION': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'line-height': '1.4',
                'margin-top': 'var(--mobile-spacing-xs)'
            },
            
            ACTIONS: {
                'max-height': '50vh',
                'overflow-y': 'auto'
            },
            
            '.ood-mobile-actionsheet-item': {
                display: 'flex',
                'align-items': 'center',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                'min-height': 'var(--mobile-touch-target)'
            },
            
            '.ood-mobile-actionsheet-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            '.ood-mobile-actionsheet-item:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-actionsheet-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-actionsheet-item-destructive': {
                color: 'var(--mobile-danger)'
            },
            
            '.ood-mobile-actionsheet-icon': {
                'font-size': '20px',
                'margin-right': 'var(--mobile-spacing-md)',
                'flex-shrink': 0
            },
            
            '.ood-mobile-actionsheet-text': {
                flex: 1,
                'font-size': 'var(--mobile-font-md)',
                'line-height': '1.4'
            },
            
            '.ood-mobile-actionsheet-item .ood-mobile-actionsheet-description': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)',
                'margin-left': 'auto',
                'flex-shrink': 0
            },
            
            CANCEL: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                padding: 'var(--mobile-spacing-lg)',
                'background-color': 'var(--mobile-bg-secondary)',
                'border-top': '8px solid var(--mobile-bg-secondary)',
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                'min-height': 'var(--mobile-touch-target)'
            },
            
            'CANCEL:hover': {
                'background-color': 'var(--mobile-border-color)'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '操作面板标题',
                ini: '操作面板',
                action: function(value) {
                    var profile = this;
                    // 同步更新title属性
                    profile.properties.title = value;
                    var titleNode = profile.getSubNode('TITLE');
                    if (titleNode && !titleNode.isEmpty()) {
                        titleNode.html(value || '');
                    }
                    profile.getRoot().attr('aria-label', value || '操作面板');
                }
            },
            
            width: {
                caption: '操作面板宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '操作面板高度',
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
                    var actionsheetNode = this.getSubNode('ACTIONSHEET');
                    if (value && actionsheetNode && !actionsheetNode.isEmpty()) {
                        actionsheetNode.css('background-color', value);
                    }
                }
            },
            
            titleColor: {
                caption: '标题颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var titleNode = this.getSubNode('TITLE');
                    if (value && titleNode && !titleNode.isEmpty()) {
                        titleNode.css('color', value);
                    }
                }
            },
            
            // ===== 操作面板特有属性 =====
            title: {
                caption: '操作面板标题',
                ini: ''
            },
            
            description: {
                ini: ''
            },
            
            actions: {
                ini: [],
                action: function(value) {
                    this.boxing().setActions(value);
                }
            },
            
            showCancel: {
                ini: true
            },
            
            cancelText: {
                ini: '取消'
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
            
            data._headerDisplay = (props.title || props.description) ? 'block' : 'none';
            data._descriptionDisplay = props.description ? 'block' : 'none';
            data._cancelDisplay = props.showCancel ? 'block' : 'none';
            
            return data;
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // ActionSheet组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                overlay = profile.getSubNode('OVERLAY'),
                sheet = profile.getSubNode('SHEET'),
                header = profile.getSubNode('HEADER'),
                actionsContainer = profile.getSubNode('ACTIONS'),
                cancelBtn = profile.getSubNode('CANCEL'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整操作面板容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    // 限制最大宽度为屏幕宽度的90%
                    var maxWidth = Math.min(pxWidth, window.innerWidth * 0.9);
                    sheet.css('max-width', adjustunit(maxWidth));
                }
            }

            // 如果提供了高度，调整操作面板容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    // 限制最大高度为屏幕高度的80%
                    var maxHeight = Math.min(pxHeight, window.innerHeight * 0.8);
                    sheet.css('max-height', adjustunit(maxHeight));
                    
                    // 调整操作项容器的高度以适应面板
                    var headerHeight = header.offsetHeight(true) || 0;
                    var cancelHeight = cancelBtn.offsetHeight(true) || 0;
                    var availableHeight = maxHeight - headerHeight - cancelHeight;
                    
                    actionsContainer.css('max-height', adjustunit(availableHeight));
                }
            }

            // 根据屏幕尺寸调整布局
            if (width || height) {
                var screenWidth = window.innerWidth;
                var screenHeight = window.innerHeight;
                
                // 在大屏幕上居中显示
                if (screenWidth > 768) {
                    sheet.addClass('ood-mobile-actionsheet-centered');
                } else {
                    sheet.removeClass('ood-mobile-actionsheet-centered');
                }
            }
        },

        EventHandlers: {
            onShow: function(profile) {
                // 操作面板显示事件处理器
            },
            
            onClose: function(profile) {
                // 操作面板关闭事件处理器
            },
            
            onActionClick: function(profile, action, index) {
                // 操作项点击事件处理器
            },
            
            onCancel: function(profile) {
                // 取消按钮点击事件处理器
            }
        },
        
        // 静态方法：快速显示操作面板
        show: function(options) {
            var actionSheet = new ood.Mobile.ActionSheet({
                title: options.title,
                description: options.description,
                actions: options.actions || [],
                showCancel: options.showCancel !== false,
                cancelText: options.cancelText || '取消',
                onActionClick: options.onActionClick,
                onCancel: options.onCancel
            });
            
            actionSheet.render(document.body);
            actionSheet.show();
            
            return actionSheet;
        }
    }
});

// 添加全局样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .ood-mobile-actionsheet-open {
            overflow: hidden !important;
        }
    `;
    document.head.appendChild(style);
}/**
 * 移动端模态框组件
 * 继承自 ood.UI（UI基类），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持自定义内容、操作按钮、动画效果等功能
 */

ood.Class("ood.Mobile.Modal", "ood.UI.Div",{
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['OVERLAY', 'MODAL', 'HEADER', 'TITLE', 'CLOSE', 'CONTENT', 'FOOTER', 'ACTIONS']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
       //     this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端模态框特性
            this.initMobileModalFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileModalFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端模态框CSS类
            profile.getRoot().addClass('ood-mobile-modal ood-mobile-component');
            
            // 初始化模态框功能
            this.initModalFeatures();
            
            // 绑定事件
            this.bindTouchEvents();
        },
        
        initModalFeatures: function() {
            // 原有的模态框特性初始化逻辑
            this.initModalState();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var closeBtn = profile.getSubNode('CLOSE');
            var cancelBtn = profile.getSubNode('CANCEL');
            var confirmBtn = profile.getSubNode('CONFIRM');
            
            // 遮罩点击关闭
            overlay.on('click', function(e) {
                if (e.target === overlay.get(0) && profile.properties.maskClosable) {
                    self.close();
                }
            });
            
            // 关闭按钮
            if (closeBtn && !closeBtn.isEmpty()) {
                closeBtn.on('click', function(e) {
                    self.close();
                });
            }
            
            // 取消按钮
            if (cancelBtn && !cancelBtn.isEmpty()) {
                cancelBtn.on('click', function(e) {
                    self.onCancel();
                });
            }
            
            // 确认按钮
            if (confirmBtn && !confirmBtn.isEmpty()) {
                confirmBtn.on('click', function(e) {
                    self.onConfirm();
                });
            }
        },
        
        initModalState: function() {
            this._isVisible = false;
        },
        
        show: function() {
            if (this._isVisible) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var modal = profile.getSubNode('MODAL');
            
            this._isVisible = true;
            
            // 显示遮罩
            overlay.css('display', 'flex');
            
            // 添加显示动画
            setTimeout(function() {
                overlay.addClass('ood-mobile-modal-overlay-show');
                modal.addClass('ood-mobile-modal-show');
            }, 10);
            
            // 阻止页面滚动
            ood('body').addClass('ood-mobile-modal-open');
            
            this.onShow();
        },
        
        close: function() {
            if (!this._isVisible) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var modal = profile.getSubNode('MODAL');
            
            this._isVisible = false;
            
            // 移除显示动画
            overlay.removeClass('ood-mobile-modal-overlay-show');
            modal.removeClass('ood-mobile-modal-show');
            
            // 隐藏遮罩
            var self = this;
            setTimeout(function() {
                overlay.css('display', 'none');
                self.onClose();
            }, 300);
            
            // 恢复页面滚动
            ood('body').removeClass('ood-mobile-modal-open');
        },
        
        setTitle: function(title) {
            var profile = this.get(0);
            var titleNode = profile.getSubNode('TITLE');
            var header = profile.getSubNode('HEADER');
            
            profile.properties.title = title;
            
            if (title) {
                titleNode.html(title);
                header.css('display', 'block');
            } else {
                header.css('display', 'none');
            }
        },
        
        setContent: function(content) {
            var profile = this.get(0);
            var contentNode = profile.getSubNode('CONTENT');
            
            if (typeof content === 'string') {
                contentNode.html(content);
            } else {
                contentNode.empty().append(content);
            }
        },
        
        setActions: function(actions) {
            var profile = this.get(0);
            var footer = profile.getSubNode('FOOTER');
            var actionsNode = profile.getSubNode('ACTIONS');
            
            if (!actions || actions.length === 0) {
                footer.css('display', 'none');
                return;
            }
            
            footer.css('display', 'block');
            actionsNode.html('');
            
            for (var i = 0; i < actions.length; i++) {
                var action = actions[i];
                var btn = this.createActionButton(action);
                actionsNode.append(btn);
            }
        },
        
        createActionButton: function(action) {
            var btnClass = 'ood-mobile-modal-action';
            if (action.type) {
                btnClass += ' ood-mobile-modal-action-' + action.type;
            }
            
            var btn = ood('<button class="' + btnClass + '">' + action.text + '</button>');
            
            if (action.handler) {
                btn.on('click', action.handler);
            }
            
            return btn;
        },
        
        isVisible: function() {
            return this._isVisible;
        },
        
        onShow: function() {
            var profile = this.get(0);
            
            if (profile.onShow) {
                profile.boxing().onShow(profile);
            }
        },
        
        onClose: function() {
            var profile = this.get(0);
            
            if (profile.onClose) {
                profile.boxing().onClose(profile);
            }
        },
        
        onConfirm: function() {
            var profile = this.get(0);
            
            if (profile.onConfirm) {
                var result = profile.boxing().onConfirm(profile);
                if (result !== false) {
                    this.close();
                }
            } else {
                this.close();
            }
        },
        
        onCancel: function() {
            var profile = this.get(0);
            
            if (profile.onCancel) {
                profile.boxing().onCancel(profile);
            }
            
            this.close();
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-modal',
            style: '{_style}',
            
            OVERLAY: {
                tagName: 'div',
                className: 'ood-mobile-modal-overlay',
              //  style: 'display: none',
                
                MODAL: {
                    tagName: 'div',
                    className: 'ood-mobile-modal-container',
                    
                    HEADER: {
                        tagName: 'div',
                        className: 'ood-mobile-modal-header',
                        style: 'display: {_headerDisplay}',
                        
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-mobile-modal-title',
                            text: '{title}'
                        },
                        
                        CLOSE: {
                            tagName: 'button',
                            className: 'ood-mobile-modal-close',
                            type: 'button',
                            style: 'display: {_closeDisplay}',
                            text: '×'
                        }
                    },
                    
                    CONTENT: {
                        tagName: 'div',
                        className: 'ood-mobile-modal-content',
                        text: '{content}'
                    },
                    
                    FOOTER: {
                        tagName: 'div',
                        className: 'ood-mobile-modal-footer',
                        style: 'display: {_footerDisplay}',
                        
                        ACTIONS: {
                            tagName: 'div',
                            className: 'ood-mobile-modal-actions',
                            
                            CANCEL: {
                                tagName: 'button',
                                className: 'ood-mobile-modal-action ood-mobile-modal-action-cancel',
                                type: 'button',
                                style: 'display: {_cancelDisplay}',
                                text: '{cancelText}'
                            },
                            
                            CONFIRM: {
                                tagName: 'button',
                                className: 'ood-mobile-modal-action ood-mobile-modal-action-confirm',
                                type: 'button',
                                style: 'display: {_confirmDisplay}',
                                text: '{confirmText}'
                            }
                        }
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative'
            },
            
            OVERLAY: {
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                'background-color': 'rgba(0,0,0,0.5)',
                'z-index': 1500,
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                padding: 'var(--mobile-spacing-lg)',
             //   opacity: 0,
                transition: 'opacity 0.3s ease'
            },
            
            'OVERLAY.ood-mobile-modal-overlay-show': {
                opacity: 1
            },
            
            MODAL: {
                'background-color': 'var(--mobile-bg-primary)',
                'border-radius': 'var(--mobile-border-radius)',
                'box-shadow': 'var(--mobile-shadow-heavy)',
                'max-width': '90%',
                'max-height': '80%',
               // width: '100%',
                transform: 'scale(0.8)',
                transition: 'transform 0.3s ease',
                overflow: 'hidden'
            },
            
            'MODAL.ood-mobile-modal-show': {
                transform: 'scale(1)'
            },
            
            HEADER: {
                position: 'relative',
                padding: 'var(--mobile-spacing-lg)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'text-align': 'center'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4',
                'margin-right': '32px'
            },
            
            CLOSE: {
                position: 'absolute',
                top: '50%',
                right: 'var(--mobile-spacing-md)',
                transform: 'translateY(-50%)',
                width: '32px',
                height: '32px',
                border: 'none',
                'background-color': 'transparent',
                'font-size': '24px',
                color: 'var(--mobile-text-secondary)',
                cursor: 'pointer',
                'border-radius': '50%',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                transition: 'background-color 0.2s ease'
            },
            
            'CLOSE:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            CONTENT: {
                padding: 'var(--mobile-spacing-lg)',
                'max-height': '60vh',
                'overflow-y': 'auto',
                'font-size': 'var(--mobile-font-md)',
                'line-height': '1.6',
                color: 'var(--mobile-text-primary)'
            },
            
            FOOTER: {
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)',
                'border-top': '1px solid var(--mobile-border-color)'
            },
            
            ACTIONS: {
                display: 'flex',
                gap: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-modal-action': {
                flex: 1,
                height: '44px',
                border: 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                cursor: 'pointer',
                transition: 'all 0.2s ease'
            },
            
            '.ood-mobile-modal-action-cancel': {
                'background-color': 'var(--mobile-bg-secondary)',
                color: 'var(--mobile-text-primary)'
            },
            
            '.ood-mobile-modal-action-cancel:hover': {
                'background-color': 'var(--mobile-border-color)'
            },
            
            '.ood-mobile-modal-action-confirm': {
                'background-color': 'var(--mobile-primary)',
                color: '#FFFFFF'
            },
            
            '.ood-mobile-modal-action-confirm:hover': {
                opacity: 0.9
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            // 模态框标题（显示值）- 对应模板中的 {title}
            title: {
                caption: '模态框标题',
                ini: '提示',
                action: function(value) {
                    this.boxing().setTitle(value);
                }
            },
            
            // 模态框宽度
            width: {
                caption: '模态框宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // 模态框高度
            height: {
                caption: '模态框高度',
                ini: '20em'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 模态框背景色（颜色选择器）
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var modal = this.getSubNode('MODAL');
                        if (modal && !modal.isEmpty()) {
                            modal.css('background-color', value);
                        }
                    }
                }
            },
            
            // 模态框标题色（颜色选择器）
            titleColor: {
                caption: '标题颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var titleNode = this.getSubNode('TITLE');
                        if (titleNode && !titleNode.isEmpty()) {
                            titleNode.css('color', value);
                        }
                    }
                }
            },
            
            // 模态框图标（图标选择器）
            icon: {
                caption: '模态框图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var iconNode = profile.getSubNode('ICON');
                    
                    if (value && iconNode && !iconNode.isEmpty()) {
                        iconNode.attr('class', 'ood-mobile-modal-icon ' + value);
                        iconNode.css('display', 'inline-block');
                    } else if (iconNode && !iconNode.isEmpty()) {
                        iconNode.css('display', 'none');
                    }
                }
            },
            
            // ===== 模态框特定属性 =====
            content: {
                caption: '模态框内容',
                ini: ''
            },
            
            showClose: {
                ini: true
            },

            showCancel: {
                ini: true
            },
            
            showConfirm: {
                ini: true
            },
            
            cancelText: {
                ini: '取消'
            },
            
            confirmText: {
                ini: '确定'
            },
            
            maskClosable: {
                ini: true
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },

        _onresize: function (profile, width, height) {
            var size = arguments.callee.upper.apply(this, arguments),
                root = profile.getRoot(),
                border = profile.getSubNode('BORDER'),
                panel = profile.getSubNode('PANEL'),
                sidebar = profile.getSubNode('SIDEBAR'),
                sbcap = profile.getSubNode('SBCAP'),
                prop = profile.properties,
                sbs = prop.sideBarStatus,
                sbtype = prop.sideBarType.split('-'),
                cb1 = border.contentBox(),
                bv = (prop.$vborder || 0) * 2,
                bh = (prop.$hborder || 0) * 2,

                cb2 = panel.contentBox(),
                b2 = (prop.$iborder || 0) * 1,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },

                fzrate = profile.getEmSize() / root._getEmSize(),
                panelfz = panel._getEmSize(fzrate),

                // caculate by px
                ww = width ? profile.$px(size.width) : size.width,
                hh = height ? profile.$px(size.height) : size.height,
                sbsize = profile.$px(prop.sideBarSize),
                sbsize2 = adjustunit(sbsize);

            sbtype = sbs == 'expand' ? sbtype[0] : (sbtype[1] || sbtype[0]);

            size.left = size.top = 0;
            if (sbtype && sbtype != 'none') {
                sbcap.css('line-height', adjustunit(sbsize - (!cb1 ? 0 : bh)));
                if (sbtype == 'left' || sbtype == 'right') {
                    sidebar.width(sbsize2);
                    if (height && 'auto' !== height)
                        sidebar.height(adjustunit(hh - (cb1 ? 0 : bv)));
                } else {
                    sidebar.height(sbsize2);
                    sidebar.width(adjustunit(ww - (cb1 ? 0 : bh)));
                }

                if (sbs == 'fold') {
                    if (sbtype == 'left' || sbtype == 'right') {
                        root.width(adjustunit(sbsize + bh));
                        border.width(adjustunit(sbsize + (cb1 ? 0 : bh)));
                    } else {
                        root.height(adjustunit(sbsize + bv));
                        border.height(adjustunit(sbsize + (cb1 ? 0 : bv)));
                    }
                    return;
                } else {
                    if (sbtype == 'left' || sbtype == 'right') {
                        root.width(adjustunit(width));
                        border.width(adjustunit(ww));
                    } else {
                        root.height(adjustunit(height));
                        border.height(adjustunit(hh));
                    }
                    switch (sbtype) {
                        case 'left':
                            ww -= sbsize;
                            size.left = sbsize;
                            break;
                        case 'right':
                            ww -= sbsize;
                            break;
                        case 'top':
                            hh -= sbsize;
                            size.top = sbsize;
                            break;
                        case 'bottom':
                            hh -= sbsize;
                            break;
                    }
                }
            }
            if (size.width) size.width = adjustunit(ww - (cb1 ? 0 : bh) - (!cb2 ? 0 : b2), panelfz);
            if (size.height && 'auto' !== size.height)
                size.height = adjustunit(hh - (cb1 ? 0 : bv) - (!cb2 ? 0 : b2), panelfz);
            panel.cssRegion(size, true);

            if (size.width) {
                ood.UI._adjustConW(profile, panel, size.width);
            }
        },
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            data._headerDisplay = props.title ? 'block' : 'none';
            data._footerDisplay = (props.showCancel || props.showConfirm) ? 'block' : 'none';
            data._closeDisplay = props.showClose ? 'block' : 'none';
            data._cancelDisplay = props.showCancel ? 'block' : 'none';
            data._confirmDisplay = props.showConfirm ? 'block' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onShow: function(profile) {
                // 模态框显示事件处理器
            },
            
            onClose: function(profile) {
                // 模态框关闭事件处理器
            },
            
            onConfirm: function(profile) {
                // 确认按钮点击事件处理器
                return true; // 返回false阻止关闭
            },
            
            onCancel: function(profile) {
                // 取消按钮点击事件处理器
            }
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Modal组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                overlay = profile.getSubNode('OVERLAY'),
                modal = profile.getSubNode('MODAL'),
                header = profile.getSubNode('HEADER'),
                content = profile.getSubNode('CONTENT'),
                footer = profile.getSubNode('FOOTER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整模态框容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    // 限制最大宽度为屏幕宽度的90%
                    var maxWidth = Math.min(pxWidth, window.innerWidth * 0.9);
                    modal.css('max-width', adjustunit(maxWidth));
                }
            }

            // 如果提供了高度，调整模态框容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    // 限制最大高度为屏幕高度的80%
                    var maxHeight = Math.min(pxHeight, window.innerHeight * 0.8);
                    modal.css('max-height', adjustunit(maxHeight));
                    
                    // 调整内容区域的高度以适应模态框
                    var headerHeight = header.offsetHeight(true) || 0;
                    var footerHeight = footer.offsetHeight(true) || 0;
                    var availableHeight = maxHeight - headerHeight - footerHeight;
                    
                    content.css('max-height', adjustunit(availableHeight));
                }
            }

            // 根据屏幕尺寸调整布局
            if (width || height) {
                var screenWidth = window.innerWidth;
                var screenHeight = window.innerHeight;
                
                // 在大屏幕上居中显示
                if (screenWidth > 768) {
                    modal.addClass('ood-mobile-modal-centered');
                } else {
                    modal.removeClass('ood-mobile-modal-centered');
                }
            }
        },

        // 静态方法：快速显示确认对话框
        confirm: function(options) {
            var modal = new ood.Mobile.Modal({
                title: options.title || '确认',
                content: options.content || '',
                showClose: false,
                showCancel: true,
                showConfirm: true,
                cancelText: options.cancelText || '取消',
                confirmText: options.confirmText || '确定',
                onConfirm: options.onConfirm,
                onCancel: options.onCancel
            });
            
            modal.render(document.body);
            modal.show();
            
            return modal;
        },
        
        // 静态方法：快速显示警告对话框
        alert: function(options) {
            var modal = new ood.Mobile.Modal({
                title: options.title || '提示',
                content: options.content || '',
                showClose: false,
                showCancel: false,
                showConfirm: true,
                confirmText: options.confirmText || '确定',
                onConfirm: options.onConfirm
            });
            
            modal.render(document.body);
            modal.show();
            
            return modal;
        }
    }
});

// 添加全局样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .ood-mobile-modal-open {
            overflow: hidden !important;
        }
    `;
    document.head.appendChild(style);
}/**
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
}/**
 * 移动端表单组件
 * 继承自ood.UI（面板基类）和ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持表单验证、数据收集、提交等功能
 */
ood.Class("ood.Mobile.Form", ["ood.UI", "ood.absContainer", "ood.absList"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['FORM', 'FIELDS', 'ACTIONS', 'SUBMIT', 'RESET']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
            //this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端Form特性
            this.initMobileFormFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileFormFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端表单CSS类
            profile.getRoot().addClass('ood-mobile-form ood-mobile-component');
            
            // 初始化表单功能
            this.initFormFeatures();
            
            // 初始化触摸事件
            this.initTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        initFormFeatures: function() {
            // 原有的表单特性初始化逻辑
            this.initFields();
            this.initValidation();
        },
        
        // 移动端触摸事件初始化
        initTouchEvents: function() {
            if (!ood.Mobile || !ood.Mobile.config.touch.enabled) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 添加触摸支持类
            root.addClass('ood-touch-enabled');
            
            // 绑定触摸事件
            this.bindTouchEvents();
        },
        
        // 响应式设计初始化
        initResponsive: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var self = this;
            
            // 初始调整布局
            this.adjustLayout();
            
            // 监听窗口大小变化（使用ood框架的事件机制）
            if (window.addEventListener) {
                var resizeHandler = ood.Mobile.utils.debounce(function() {
                    self.adjustLayout();
                }, 300);
                
                window.addEventListener('resize', resizeHandler);
                window.addEventListener('orientationchange', resizeHandler);
                
                // 在组件销毁时清理事件监听
                profile.$beforeDestroy = profile.$beforeDestroy || [];
                profile.$beforeDestroy.push(function() {
                    window.removeEventListener('resize', resizeHandler);
                    window.removeEventListener('orientationchange', resizeHandler);
                });
            }
        },
        
        // 可访问性初始化
        initAccessibility: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var root = profile.getRoot();
            var form = profile.getSubNode('FORM');
            
            // 为表单添加ARIA属性
            root.attr({
                'role': 'form',
                'aria-label': '移动端表单'
            });
            
            if (form && !form.isEmpty()) {
                form.attr({
                    'novalidate': 'novalidate', // 禁用浏览器默认验证
                    'autocomplete': 'on'
                });
            }
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var form = profile.getSubNode('FORM');
            
            if (form && !form.isEmpty()) {
                // 表单提交
                form.on('submit', function(e) {
                    e.preventDefault();
                    self.submit();
                });
                
                // 重置按钮
                form.on('click', '.ood-mobile-form-reset', function(e) {
                    e.preventDefault();
                    self.reset();
                });
                
                // 移动端特有事件：长按重置（可选）
                if (ood.Mobile.config.touch.longPressDelay) {
                    var longPressTimer;
                    form.on('touchstart', '.ood-mobile-form-field', function(e) {
                        var field = e.currentTarget;
                        longPressTimer = setTimeout(function() {
                            self.showFieldOptions(field);
                        }, ood.Mobile.config.touch.longPressDelay);
                    });
                    
                    form.on('touchend touchcancel', '.ood-mobile-form-field', function() {
                        if (longPressTimer) {
                            clearTimeout(longPressTimer);
                            longPressTimer = null;
                        }
                    });
                }
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                var fieldsContainer = profile.getSubNode('FIELDS');
                var actionsContainer = profile.getSubNode('ACTIONS');
                
                // 清除旧的尺寸类
                root.removeClass('form-xs form-sm form-md form-lg form-xl');
                
                // 添加当前尺寸类
                root.addClass('form-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('form-tiny');
                    
                    // 调整字体大小
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'font-size': '0.9em',
                            'gap': 'var(--mobile-spacing-sm)'
                        });
                    }
                    
                    // 调整按钮布局为垂直排列
                    if (actionsContainer && !actionsContainer.isEmpty()) {
                        actionsContainer.css({
                            'flex-direction': 'column',
                            'gap': 'var(--mobile-spacing-sm)'
                        });
                    }
                } else {
                    root.removeClass('form-tiny');
                    
                    // 恢复正常尺寸
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'font-size': '',
                            'gap': ''
                        });
                    }
                    
                    if (actionsContainer && !actionsContainer.isEmpty()) {
                        actionsContainer.css({
                            'flex-direction': '',
                            'gap': ''
                        });
                    }
                }
                
                // 平板模式调整
                if (width >= 768 && width < 1024) {
                    root.addClass('form-tablet');
                    
                    // 两列布局优化
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'display': 'grid',
                            'grid-template-columns': 'repeat(auto-fit, minmax(250px, 1fr))',
                            'gap': 'var(--mobile-spacing-md)'
                        });
                    }
                } else {
                    root.removeClass('form-tablet');
                    
                    if (fieldsContainer && !fieldsContainer.isEmpty() && width < 768) {
                        fieldsContainer.css({
                            'display': '',
                            'grid-template-columns': '',
                            'gap': ''
                        });
                    }
                }
            });
        },
        
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
                        localStorage.setItem('ood-mobile-form-theme', theme);
                    } catch(e) {
                        // 忽略localStorage错误
                    }
                }
                
                // 触发主题变化事件
                if (profile.onThemeChange) {
                    profile.boxing().onThemeChange(oldTheme, theme);
                }
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            if (profile && profile.properties.theme) {
                return profile.properties.theme;
            }
            
            // 从 localStorage 获取
            try {
                var savedTheme = localStorage.getItem('ood-mobile-form-theme');
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
        
        // 显示字段选项（长按触发）
        showFieldOptions: function(field) {
            // 可以在这里实现字段的上下文菜单
            // 例如：清空、复制、粘贴等功能
            console.log('长按字段：', field);
        },
        
        initFields: function() {
            this._fields = {};
            this._fieldValidators = {};
        },
        
        initValidation: function() {
            this._isValid = true;
            this._errors = {};
        },
        
        addField: function(name, element) {
            this._fields[name] = element;
        },
        
        removeField: function(name) {
            delete this._fields[name];
            delete this._fieldValidators[name];
            delete this._errors[name];
        },
        
        getField: function(name) {
            return this._fields[name];
        },
        
        setFieldValue: function(name, value) {
            var field = this._fields[name];
            if (field && field.setValue) {
                field.setValue(value);
            }
        },
        
        getFieldValue: function(name) {
            var field = this._fields[name];
            if (field && field.getValue) {
                return field.getValue();
            }
            return null;
        },
        
        getFormData: function() {
            var data = {};
            for (var name in this._fields) {
                data[name] = this.getFieldValue(name);
            }
            return data;
        },
        
        setFormData: function(data) {
            for (var name in data) {
                this.setFieldValue(name, data[name]);
            }
        },
        
        validate: function() {
            this._isValid = true;
            this._errors = {};
            
            for (var name in this._fields) {
                var field = this._fields[name];
                var validators = this._fieldValidators[name];
                
                if (validators) {
                    var value = this.getFieldValue(name);
                    var fieldValid = this.validateField(name, value, validators);
                    
                    if (!fieldValid) {
                        this._isValid = false;
                    }
                }
            }
            
            this.updateValidationUI();
            return this._isValid;
        },
        
        validateField: function(name, value, validators) {
            var isValid = true;
            
            for (var i = 0; i < validators.length; i++) {
                var validator = validators[i];
                if (!validator.validate(value)) {
                    this._errors[name] = validator.message;
                    isValid = false;
                    break;
                }
            }
            
            return isValid;
        },
        
        updateValidationUI: function() {
            var profile = this.get(0);
            var form = profile.getSubNode('FORM');
            
            // 清除所有错误状态
            form.find('.ood-mobile-form-field-error').removeClass('ood-mobile-form-field-error');
            form.find('.ood-mobile-form-error-message').remove();
            
            // 显示错误
            for (var name in this._errors) {
                var field = this._fields[name];
                if (field && field.getRoot) {
                    var fieldRoot = field.getRoot();
                    fieldRoot.addClass('ood-mobile-form-field-error');
                    
                    var errorMsg = ood('<div class="ood-mobile-form-error-message">' + this._errors[name] + '</div>');
                    fieldRoot.after(errorMsg);
                }
            }
        },
        
        submit: function() {
            var profile = this.get(0);
            
            if (this.validate()) {
                var formData = this.getFormData();
                this.onSubmit(formData);
            }
        },
        
        reset: function() {
            for (var name in this._fields) {
                var field = this._fields[name];
                if (field && field.setValue) {
                    field.setValue('');
                }
            }
            
            this._errors = {};
            this.updateValidationUI();
            this.onReset();
        },
        
        onReset: function() {
            var profile = this.get(0);
            
            if (profile.onReset) {
                profile.boxing().onReset(profile);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            // Form组件通过addField方法管理字段，这里提供适配实现
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                // 对于表单组件，我们将items视为字段定义
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.name) {
                        // 注意：这里需要实际创建字段组件，简化实现只记录字段信息
                        self.addField(item.name, item);
                    }
                }
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                // 获取当前所有字段名
                var fieldNames = Object.keys(self._fields);
                
                // 从后往前删除，保证索引不变
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var index = parseInt(indices[i]);
                    if (index >= 0 && index < fieldNames.length) {
                        var fieldName = fieldNames[index];
                        self.removeField(fieldName);
                    }
                }
            });
        },
        
        clearItems: function() {
            var self = this;
            return this.each(function(profile) {
                // 清空所有字段
                var fieldNames = Object.keys(self._fields);
                for (var i = 0; i < fieldNames.length; i++) {
                    self.removeField(fieldNames[i]);
                }
            });
        },
        
        getItems: function() {
            var profile = this.get(0);
            var items = [];
            
            // 将字段信息转换为items格式返回
            for (var name in this._fields) {
                items.push({
                    name: name,
                    field: this._fields[name]
                });
            }
            
            return items;
        },
        
        getSelectedItems: function() {
            // Form组件不支持选择项，但为了符合接口规范返回空数组
            return [];
        },
        
        selectItem: function(value) {
            // Form组件不支持选择项，但为了符合接口规范提供空实现
            return this;
        },
        
        unselectItem: function(value) {
            // Form组件不支持取消选择项，但为了符合接口规范提供空实现
            return this;
        }
    },
    
    Static: {
        // 四分离架构 - 模板定义（Template）
        Templates: {
            tagName: 'div',
            className: '{_className} ood-mobile-form',
            style: '{_style}',
            tabindex: '{tabindex}',
            
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign}',
                text: '{labelCaption}'
            },
            
            FORM: {
                tagName: 'form',
                className: 'ood-mobile-form-element',
                novalidate: 'novalidate',
                autocomplete: 'on',
                
                ITEMS: {
                    $order: 10,
                    tagName: 'div',
                    className: 'ood-mobile-form-fields ood-uibase',
                    style: '{_itemsStyle}',
                    text: "{items}"
                },
                
                ACTIONS: {
                    $order: 20,
                    tagName: 'div',
                    className: 'ood-mobile-form-actions',
                    role: 'group',
                    'aria-label': '表单操作按钮',
                    
                    SUBMIT: {
                        tagName: 'button',
                        className: 'ood-mobile-form-submit ood-mobile-button ood-mobile-button-primary',
                        type: 'submit',
                        text: '{_submitText}',
                        'aria-describedby': 'submit-help'
                    },
                    
                    RESET: {
                        tagName: 'button',
                        className: 'ood-mobile-form-reset ood-mobile-button ood-mobile-button-ghost',
                        type: 'button',
                        text: '{_resetText}',
                        style: 'display: {_resetDisplay}',
                        'aria-describedby': 'reset-help'
                    }
                }
            },
            
            $submap: {
                items: {
                    ITEM: {
                        tagName: 'div',
                        className: 'ood-mobile-form-field {itemClass} {disabled} {readonly} {_itemState}',
                        style: '{itemStyle}{_itemDisplay}',
                        'data-field': '{fieldName}',
                        'data-id': '{id}',

                        /* 实例数据示例:
                         * {
                         *   id: 'field-1',
                         *   fieldName: 'username',
                         *   label: '用户名',
                         *   fieldType: 'text',
                         *   fieldId: 'username-field',
                         *   placeholder: '请输入用户名',
                         *   required: true,
                         *   readonly: false,
                         *   disabled: false,
                         *   errorMessage: '用户名不能为空',
                         *   itemClass: 'custom-form-field',
                         *   itemStyle: 'margin-bottom: 12px;',
                         *   _itemState: 'error',
                         *   _itemDisplay: 'display: flex;',
                         *   _textareaDisplay: 'display: none;',
                         *   _selectDisplay: 'display: none;',
                         *   _errorDisplay: 'display: block;'
                         * }
                         */

                        FIELDLABEL: {
                            $order: 5,
                            tagName: 'label',
                            className: 'ood-mobile-form-field-label',
                            text: '{label}',
                            'for': '{fieldId}'
                        },
                        FIELDINPUT: {
                            $order: 10,
                            tagName: 'input',
                            className: 'ood-mobile-form-field-input',
                            type: '{fieldType}',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            placeholder: '{placeholder}',
                            required: '{required}',
                            readonly: '{readonly}',
                            disabled: '{disabled}'
                        },
                        FIELDTEXTAREA: {
                            $order: 10,
                            tagName: 'textarea',
                            className: 'ood-mobile-form-field-textarea',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            placeholder: '{placeholder}',
                            required: '{required}',
                            readonly: '{readonly}',
                            disabled: '{disabled}',
                            style: '{_textareaDisplay}'
                        },
                        FIELDSELECT: {
                            $order: 10,
                            tagName: 'select',
                            className: 'ood-mobile-form-field-select',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            required: '{required}',
                            disabled: '{disabled}',
                            style: '{_selectDisplay}'
                        },
                        FIELDERROR: {
                            $order: 20,
                            className: 'ood-mobile-form-error-message',
                            style: '{_errorDisplay}',
                            text: '{errorMessage}'
                        }
                    }
                }
            }
        },
        
        // 四分离架构 - 外观定义（Appearances）
        Appearances: {
            KEY: {
                'width': '100%',
                'max-width': '100%',
                'box-sizing': 'border-box',
                'font-family': 'var(--mobile-font-family, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif)',
                'line-height': 'var(--mobile-line-height, 1.5)'
            },
            
            LABEL: {
                'z-index': 1,
                top: 0,
                left: 0,
                display: 'flex',
                position: 'absolute',
                'padding-top': '.333em',
                'color': 'var(--mobile-text-heading)'
            },
            
            FORM: {
                'width': '100%',
                'display': 'flex',
                'flex-direction': 'column',
                'gap': 'var(--mobile-spacing-lg, 16px)'
            },
            
            ITEMS: {
                'display': 'flex',
                'flex-direction': 'column',
                'gap': 'var(--mobile-spacing-md, 12px)',
                'margin-bottom': 'var(--mobile-spacing-lg, 16px)'
            },
            
            ITEM: {
                'display': 'flex',
                'flex-direction': 'column',
                'margin-bottom': 'var(--mobile-spacing-md, 12px)'
            },
            
            FIELDLABEL: {
                'font-size': 'var(--mobile-font-sm, 14px)',
                'font-weight': '600',
                'color': 'var(--mobile-text-primary, #000000)',
                'margin-bottom': 'var(--mobile-spacing-xs, 4px)',
                'line-height': '1.4'
            },
            
            'FIELDLABEL::after': {
                'content': '" *"',
                'color': 'var(--mobile-danger, #FF3B30)',
                'display': 'var(--field-required-display, none)'
            },
            
            'FIELDINPUT, FIELDTEXTAREA, FIELDSELECT': {
                'width': '100%',
                'padding': 'var(--mobile-spacing-md, 12px)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'background': 'var(--mobile-bg-primary, #FFFFFF)',
                'color': 'var(--mobile-text-primary, #000000)',
                'transition': 'all 0.2s ease',
                'box-sizing': 'border-box'
            },
            
            'FIELDINPUT:focus, FIELDTEXTAREA:focus, FIELDSELECT:focus': {
                'outline': 'none',
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'FIELDINPUT:disabled, FIELDTEXTAREA:disabled, FIELDSELECT:disabled': {
                'background': 'var(--mobile-disabled-bg, #F2F2F7)',
                'color': 'var(--mobile-disabled-text, #8E8E93)',
                'cursor': 'not-allowed'
            },
            
            'FIELDINPUT:readonly, FIELDTEXTAREA:readonly': {
                'background': 'var(--mobile-readonly-bg, #F9F9F9)',
                'cursor': 'default'
            },
            
            FIELDTEXTAREA: {
                'min-height': '6em',
                'resize': 'vertical'
            },
            
            FIELDSELECT: {
                'appearance': 'none',
                'background-image': 'url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 16 16\'%3E%3Cpath fill=\'%23999\' d=\'M8 12L3 7h10z\'/%3E%3C/svg%3E")',
                'background-repeat': 'no-repeat',
                'background-position': 'right 12px center',
                'background-size': '12px',
                'padding-right': '36px'
            },
            
            FIELDERROR: {
                'font-size': 'var(--mobile-font-xs, 12px)',
                'color': 'var(--mobile-danger, #FF3B30)',
                'margin-top': 'var(--mobile-spacing-xs, 4px)',
                'line-height': '1.4',
                'display': 'flex',
                'align-items': 'center',
                'gap': 'var(--mobile-spacing-xs, 4px)'
            },
            
            'FIELDERROR::before': {
                'content': '"\u26A0"',
                'font-size': '1em',
                'flex-shrink': '0'
            },
            
            ACTIONS: {
                'display': 'flex',
                'gap': 'var(--mobile-spacing-md, 12px)',
                'margin-top': 'var(--mobile-spacing-lg, 16px)',
                'justify-content': 'space-between',
                'flex-wrap': 'wrap'
            },
            
            SUBMIT: {
                'flex': '1',
                'min-width': '120px',
                'background': 'var(--mobile-primary, #007AFF)',
                'color': 'var(--mobile-primary-text, #FFFFFF)',
                'border': 'none',
                'border-radius': 'var(--mobile-radius, 8px)',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '600',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                'touch-action': 'manipulation'
            },
            
            'SUBMIT:hover': {
                'background': 'var(--mobile-primary-hover, #0056CC)',
                'transform': 'translateY(-1px)',
                'box-shadow': '0 4px 8px rgba(0, 122, 255, 0.3)'
            },
            
            'SUBMIT:active': {
                'transform': 'translateY(0)',
                'box-shadow': '0 2px 4px rgba(0, 122, 255, 0.3)'
            },
            
            'SUBMIT:disabled': {
                'background': 'var(--mobile-disabled, #C7C7CC)',
                'color': 'var(--mobile-disabled-text, #8E8E93)',
                'cursor': 'not-allowed',
                'transform': 'none',
                'box-shadow': 'none'
            },
            
            RESET: {
                'flex': '1',
                'min-width': '120px',
                'background': 'transparent',
                'color': 'var(--mobile-text-secondary, #8E8E93)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '500',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                'touch-action': 'manipulation'
            },
            
            'RESET:hover': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'color': 'var(--mobile-primary, #007AFF)',
                'background': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'RESET:active': {
                'background': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.2))'
            },
            
            // 错误状态样式
            'ITEM-error FIELDINPUT, ITEM-error FIELDTEXTAREA, ITEM-error FIELDSELECT': {
                'border-color': 'var(--mobile-danger, #FF3B30) !important',
                'box-shadow': '0 0 0 2px var(--mobile-danger-light, rgba(255, 59, 48, 0.1)) !important'
            },
            
            // 响应式断点样式
            '@media (max-width: 479px)': {
                'ACTIONS': {
                    'flex-direction': 'column',
                    'gap': 'var(--mobile-spacing-sm, 8px)'
                },
                'SUBMIT, RESET': {
                    'width': '100%',
                    'flex': 'none'
                }
            },
            
            '@media (min-width: 768px) and (max-width: 1023px)': {
                'ITEMS': {
                    'display': 'grid',
                    'grid-template-columns': 'repeat(auto-fit, minmax(250px, 1fr))',
                    'gap': 'var(--mobile-spacing-md, 12px)'
                }
            },
            
            // 主题支持
            '[data-theme="dark"]': {
                '--mobile-primary': '#0A84FF',
                '--mobile-primary-hover': '#0056CC',
                '--mobile-primary-light': 'rgba(10, 132, 255, 0.1)',
                '--mobile-text-secondary': '#8E8E93',
                '--mobile-border': '#38383A',
                '--mobile-danger': '#FF453A',
                '--mobile-danger-light': 'rgba(255, 69, 58, 0.1)'
            },
            
            '[data-theme="highcontrast"]': {
                '--mobile-primary': '#0000FF',
                '--mobile-primary-hover': '#000080',
                '--mobile-text-secondary': '#000000',
                '--mobile-border': '#000000',
                '--mobile-danger': '#FF0000'
            }
        },
        
        // 四分离架构 - 数据模型（DataModel）
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '表单标题',
                ini: '表单',
                action: function(value) {
                    var profile = this;
                    // 更新formLabel属性保持同步
                    profile.properties.formLabel = value;
                    profile.getRoot().attr('aria-label', value || '表单');
                }
            },
            
            // 继承自UI基类的基本属性
            width: {
                caption: '表单宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            height: {
                caption: '表单高度',
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
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            // ===== 表单特有属性 =====
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    var profile = this;
                    var submit = profile.getSubNode('SUBMIT');
                    var reset = profile.getSubNode('RESET');
                    
                    if (submit && !submit.isEmpty()) {
                        submit.prop('disabled', value);
                    }
                    if (reset && !reset.isEmpty()) {
                        reset.prop('disabled', value);
                    }
                }
            },
            
            // 表单特有属性
            showReset: {
                ini: true,
                action: function(value) {
                    this.getRoot().removeClass('form-hide-reset');
                    if (!value) {
                        this.getRoot().addClass('form-hide-reset');
                    }
                }
            },
            
            submitText: {
                ini: '提交',
                action: function(value) {
                    var submit = this.getSubNode('SUBMIT');
                    if (submit && !submit.isEmpty()) {
                        submit.text(value);
                    }
                }
            },
            
            resetText: {
                ini: '重置',
                action: function(value) {
                    var reset = this.getSubNode('RESET');
                    if (reset && !reset.isEmpty()) {
                        reset.text(value);
                    }
                }
            },
            
            // 主题支持
            theme: {
                ini: '',
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // 响应式支持
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            // 验证相关
            autoValidate: {
                ini: true
            },
            
            validateOnSubmit: {
                ini: true
            },
            
            showValidationSummary: {
                ini: false
            },
            
            // 可访问性支持
            formLabel: {
                ini: '表单',
                action: function(value) {
                    this.getRoot().attr('aria-label', value);
                }
            },
            
            // 触摸事件支持
            enableLongPress: {
                ini: false
            },
            
            longPressDelay: {
                ini: 500
            },
            
            // 动画效果
            enableAnimations: {
                ini: true
            },
            
            // 继承自UI基类的其他属性
            tabindex: {
                ini: -1
            },
            
            // 支持容器功能（继承自absContainer）
            childrenAlign: {
                ini: 'stretch'
            },
            
            overflow: {
                ini: 'visible'
            }
        },


        
        // 渲染触发器（组件初始化后调用）
        RenderTrigger: function() {
            var profile = this;
            
            // 异步执行初始化，避免循环依赖
            ood.asyRun(function() {
                // 触发Boxing的Initialize方法
                if (profile.boxing && profile.boxing().Initialize) {
                    profile.boxing().Initialize();
                }
                
                // 初始化主题
                var theme = profile.properties.theme || 
                           localStorage.getItem('ood-mobile-form-theme') || 
                           (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
                           
                if (theme !== 'light') {
                    profile.boxing().setTheme(theme, false);
                }
                
                // 初始化响应式布局
                if (profile.properties.responsive !== false) {
                    profile.boxing().adjustLayout();
                }
                
                // 触发自定义渲染事件
                if (profile.onRender) {
                    profile.boxing().onRender(profile);
                }
            });
        },
        
        // 数据准备方法（模板渲染前调用）
        _prepareData: function(profile) {
            // 调用父类的数据准备方法
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 准备表单特定数据
            data._resetDisplay = props.showReset ? 'inline-flex' : 'none';
            data._submitText = props.submitText || '提交';
            data._resetText = props.resetText || '重置';
            
            // 主题相关数据
            data._theme = props.theme || 'light';
            data._themeClass = props.theme && props.theme !== 'light' ? ('mobile-theme-' + props.theme) : '';
            
            // 响应式数据
            data._screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            data._isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            
            // 可访问性数据
            data._formLabel = props.formLabel || '表单';
            
            // 合并className
            var classNames = ['ood-mobile-form', 'ood-mobile-component'];
            if (data._themeClass) classNames.push(data._themeClass);
            if (data._isMobile) classNames.push('mobile-device');
            classNames.push('form-' + data._screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            return data;
        },
        
        // 准备单个表单项数据 - 处理所有模板变量
        _prepareItem: function(profile, dataItem, item, pid, i, l, mapCache, serialId) {
            // 调用父类方法
            if (arguments.callee.upper) {
                arguments.callee.upper.call(this, profile, dataItem, item, pid, i, l, mapCache, serialId);
            }
            
            var props = profile.properties;
            var hasError = item.hasError || false;
            var isDisabled = item.disabled || props.disabled || false;
            var isReadonly = item.readonly || false;
            
            // 基础数据属性
            dataItem.id = item.id || 'form-field-' + i;
            dataItem.fieldName = item.fieldName || item.name || '';
            dataItem.label = item.label || item.caption || '';
            dataItem.fieldType = item.fieldType || 'text';
            dataItem.fieldId = item.fieldId || (dataItem.fieldName + '-field');
            dataItem.placeholder = item.placeholder || '';
            dataItem.required = item.required ? 'required' : '';
            dataItem.readonly = isReadonly ? 'readonly' : '';
            dataItem.disabled = isDisabled ? 'disabled' : '';
            dataItem.errorMessage = item.errorMessage || '';
            
            // 状态类和样式
            dataItem._itemState = this._buildItemStateClass(hasError, isDisabled, isReadonly);
            dataItem._itemDisplay = 'display: flex;';
            
            // 显示控制变量
            var isTextarea = dataItem.fieldType === 'textarea';
            var isSelect = dataItem.fieldType === 'select';
            
            dataItem._textareaDisplay = isTextarea ? 'display: block;' : 'display: none;';
            dataItem._selectDisplay = isSelect ? 'display: block;' : 'display: none;';
            dataItem._errorDisplay = dataItem.errorMessage ? 'display: block;' : 'display: none;';
            
            // 自定义样式
            dataItem.itemClass = item.itemClass || item.className || '';
            dataItem.itemStyle = this._buildItemStyle(item, props);
            
            return dataItem;
        },
        
        // 构建项目状态类
        _buildItemStateClass: function(hasError, isDisabled, isReadonly) {
            var classes = [];
            if (hasError) classes.push('error');
            if (isDisabled) classes.push('disabled');
            if (isReadonly) classes.push('readonly');
            return classes.join(' ');
        },
        
        // 构建项目样式
        _buildItemStyle: function(item, props) {
            var styles = [];
            
            // 基础项目样式
            if (item.itemStyle) {
                styles.push(item.itemStyle);
            }
            
            // 自定义宽度
            if (item.width) {
                styles.push('width: ' + item.width);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 四分离架构 - 行为定义（Behaviors）
        Behaviors: {
            // 鼠标悬停效果支持
            HoverEffected: {
                KEY: 'SUBMIT,RESET'
            },
            
            // 支持面板操作（继承自UI）
            PanelKeys: ['KEY'],
            
            // 支持拖放操作（可选）
            DroppableKeys: ['KEY'],
            
            // 表单提交行为
            onSubmit: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 验证表单
                if (props.validateOnSubmit && !profile.boxing().validate()) {
                    return false;
                }
                
                // 触发提交事件
                if (profile.onSubmit) {
                    var formData = profile.boxing().getFormData();
                    return profile.boxing().onSubmit(profile, formData, e);
                }
                
                return true;
            },
            
            // 表单重置行为
            onReset: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 触发重置事件
                if (profile.onReset) {
                    return profile.boxing().onReset(profile, e);
                }
                
                return true;
            },
            
            // 点击事件处理
            onClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 根据点击目标处理不同逻辑
                var target = ood(e.target);
                
                if (target.hasClass('ood-mobile-form-submit')) {
                    return this.onSubmit(profile, e, src);
                } else if (target.hasClass('ood-mobile-form-reset')) {
                    return this.onReset(profile, e, src);
                }
                
                // 触发通用点击事件
                if (profile.onClick) {
                    return profile.boxing().onClick(profile, e, src);
                }
                
                return true;
            }
        },
        
        // 四分离架构 - 事件处理器（EventHandlers）
        EventHandlers: {
            // 表单提交事件
            onSubmit: function(profile, event) {
                // 子类可以重写此方法来处理表单提交
                console.log('表单提交');
                return true;
            },
            
            // 表单重置事件
            onReset: function(profile, event) {
                // 子类可以重写此方法来处理表单重置
                console.log('表单重置');
                return true;
            },
            
            // 表单验证事件
            onValidate: function(profile, isValid, errors) {
                // 验证结果处理
                return true;
            },
            
            // 字段值变化事件
            onFieldChange: function(profile, fieldName, newValue, oldValue) {
                // 字段值变化处理
                return true;
            },
            
            // 主题变化事件
            onThemeChange: function(profile, oldTheme, newTheme) {
                // 主题切换处理
                console.log('主题切换:', oldTheme, '->', newTheme);
                return true;
            },
            
            // 继承自UI基类的事件处理器
            onClick: function(profile, event, src) {
                // 通用点击事件处理
                return true;
            },
            
            onRender: function(profile) {
                // 渲染完成事件
                return true;
            },
            
            onResize: function(profile, width, height) {
                // 尺寸变化事件
                profile.boxing().adjustLayout();
                return true;
            },
            
            // 错误处理
            onError: function(profile, error, context) {
                console.error('表单错误:', error, context);
                return true;
            }
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Form组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                formNode = profile.getSubNode('FORM'),
                fieldsContainer = profile.getSubNode('FIELDS'),
                actionsContainer = profile.getSubNode('ACTIONS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整表单容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    formNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整表单容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    formNode.css('height', '100%');
                    
                    // 调整字段容器的高度以适应表单
                    var actionsHeight = actionsContainer.offsetHeight(true) || 0;
                    var availableHeight = pxHeight - actionsHeight;
                    
                    fieldsContainer.css('height', adjustunit(availableHeight));
                }
            }

            // 根据新的尺寸调整布局
            this.boxing().adjustLayout();
        }
    }
});/**
 * 移动端选择器组件
 * 继承自 ood.UI（UI基类）、ood.absList（列表功能）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持单选、多选、级联选择等功能
 */
ood.Class("ood.Mobile.Picker", ["ood.UI", "ood.absList", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['TRIGGER', 'TRIGGER_TEXT', 'ARROW', 'OVERLAY', 'POPUP', 'HEADER', 'OPTIONS']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化 - 修复不符合ood架构规范的调用方式
            //this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端选择器特性
            this.initMobilePickerFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobilePickerFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端选择器CSS类
            profile.getRoot().addClass('ood-mobile-picker ood-mobile-component');
            
            // 初始化选择器功能
            this.initPickerFeatures();
            
            // 初始化触摸事件
            this.initTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // 移动端触摸事件初始化
        initTouchEvents: function() {
            if (!ood.Mobile || !ood.Mobile.config.touch.enabled) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 添加触摸支持类
            root.addClass('ood-touch-enabled');
            
            // 绑定触摸事件
            this.bindTouchEvents();
        },
        
        // 响应式设计初始化
        initResponsive: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var self = this;
            
            // 初始调整布局
            this.adjustLayout();
            
            // 监听窗口大小变化
            if (window.addEventListener) {
                var resizeHandler = ood.Mobile.utils.debounce(function() {
                    self.adjustLayout();
                }, 300);
                
                window.addEventListener('resize', resizeHandler);
                window.addEventListener('orientationchange', resizeHandler);
                
                // 在组件销毁时清理事件监听
                profile.$beforeDestroy = profile.$beforeDestroy || [];
                profile.$beforeDestroy.push(function() {
                    window.removeEventListener('resize', resizeHandler);
                    window.removeEventListener('orientationchange', resizeHandler);
                });
            }
        },
        
        // 可访问性初始化
        initAccessibility: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var root = profile.getRoot();
            var trigger = profile.getSubNode('TRIGGER');
            var overlay = profile.getSubNode('OVERLAY');
            
            // 为触发器添加ARIA属性
            if (trigger && !trigger.isEmpty()) {
                trigger.attr({
                    'role': 'button',
                    'aria-haspopup': 'listbox',
                    'aria-expanded': 'false',
                    'aria-label': profile.properties.placeholder || '选择器'
                });
            }
            
            // 为弹窗添加ARIA属性
            if (overlay && !overlay.isEmpty()) {
                overlay.attr({
                    'role': 'dialog',
                    'aria-modal': 'true',
                    'aria-label': profile.properties.title || '选择器'
                });
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                var popup = profile.getSubNode('POPUP');
                
                // 清除旧的尺寸类
                root.removeClass('picker-xs picker-sm picker-md picker-lg picker-xl');
                
                // 添加当前尺寸类
                root.addClass('picker-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('picker-tiny');
                    
                    // 调整弹窗高度
                    if (popup && !popup.isEmpty()) {
                        popup.css({
                            'max-height': '70vh'
                        });
                    }
                } else {
                    root.removeClass('picker-tiny');
                    
                    if (popup && !popup.isEmpty()) {
                        popup.css({
                            'max-height': ''
                        });
                    }
                }
            });
        },
        
        // ood.absValue 必需方法
        _setCtrlValue: function(value) {
            this.setValue(value);
        },
        
        _getCtrlValue: function() {
            return this.getValue();
        },
        
        activate: function() {
            return this.each(function(profile) {
                var trigger = profile.getSubNode('TRIGGER');
                if (trigger) {
                    trigger.focus();
                }
            });
        },
        
        resetValue: function(value) {
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
            });
            return this;
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentOptions = self.getOptions();
                if (typeof index === 'undefined') {
                    currentOptions = currentOptions.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentOptions.splice.apply(currentOptions, [insertIndex, 0].concat(items));
                }
                
                self.setOptions(currentOptions);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentOptions = self.getOptions();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var index = parseInt(indices[i]);
                    if (index >= 0 && index < currentOptions.length) {
                        currentOptions.splice(index, 1);
                    }
                }
                
                self.setOptions(currentOptions);
            });
        },
        
        clearItems: function() {
            return this.setOptions([]);
        },
        
        getItems: function() {
            return this.getOptions();
        },
        
        getSelectedItems: function() {
            return this.getSelectedOptions();
        },
        
        selectItem: function(value) {
            this.selectOption(value);
            return this;
        },
        
        unselectItem: function(value) {
            var index = this._selectedValues.indexOf(value);
            if (index > -1) {
                this._selectedValues.splice(index, 1);
                this.updateSelection();
            }
            return this;
        },
        
        initPickerFeatures: function() {
            // 原有的选择器特性初始化逻辑
            this.initData();
            this.initSelection();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var trigger = profile.getSubNode('TRIGGER');
            var overlay = profile.getSubNode('OVERLAY');
            var closeBtn = profile.getSubNode('CLOSE');
            var confirmBtn = profile.getSubNode('CONFIRM');
            
            // 触发器点击
            trigger.on('click', function(e) {
                if (profile.properties.disabled) return;
                self.show();
            });
            
            // 遮罩点击关闭
            overlay.on('click', function(e) {
                if (e.target === overlay.get(0)) {
                    self.hide();
                }
            });
            
            // 关闭按钮
            closeBtn.on('click', function(e) {
                self.hide();
            });
            
            // 确认按钮
            confirmBtn.on('click', function(e) {
                self.confirm();
            });
            
            // 选项点击
            overlay.on('click', '.ood-mobile-picker-option', function(e) {
                var option = ood(this);
                var value = option.attr('data-value');
                self.selectOption(value);
            });
        },
        
        initData: function() {
            this._options = [];
            this._selectedValues = [];
            
            var profile = this.get(0);
            if (profile.properties.options) {
                this.setOptions(profile.properties.options);
            }
        },
        
        initSelection: function() {
            var profile = this.get(0);
            if (profile.properties.value) {
                this.setValue(profile.properties.value);
            }
        },
        
        setOptions: function(options) {
            this._options = options || [];
            this.renderOptions();
        },
        
        getOptions: function() {
            return this._options;
        },
        
        renderOptions: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('OPTIONS');
            
            container.html('');
            
            for (var i = 0; i < this._options.length; i++) {
                var option = this._options[i];
                var optionEl = this.createOptionElement(option);
                container.append(optionEl);
            }
        },
        
        createOptionElement: function(option) {
            // 修正：使用正确的DOM创建方法
            var optionEl = document.createElement('div');
            optionEl.className = 'ood-mobile-picker-option';
            optionEl.setAttribute('data-value', option.value);
            optionEl.innerHTML = option.label || option.text || option.value;
            
            // 返回ood包装的元素（使用ood的正确调用方式）
            var oodEl = ood([optionEl], false);
            
            if (option.disabled) {
                oodEl.addClass('ood-mobile-picker-option-disabled');
            }
            
            return oodEl;
        },
        
        selectOption: function(value) {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.multiple) {
                this.selectMultiple(value);
            } else {
                this.selectSingle(value);
            }
            
            this.updateSelection();
        },
        
        selectSingle: function(value) {
            this._selectedValues = [value];
        },
        
        selectMultiple: function(value) {
            var index = this._selectedValues.indexOf(value);
            if (index > -1) {
                this._selectedValues.splice(index, 1);
            } else {
                this._selectedValues.push(value);
            }
        },
        
        updateSelection: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('OPTIONS');
            var options = container.find('.ood-mobile-picker-option');
            
            var self = this;
            options.each(function() {
                var option = ood(this);
                var value = option.attr('data-value');
                
                if (self._selectedValues.indexOf(value) > -1) {
                    option.addClass('ood-mobile-picker-option-selected');
                } else {
                    option.removeClass('ood-mobile-picker-option-selected');
                }
            });
            
            this.updateTriggerText();
        },
        
        updateTriggerText: function() {
            var profile = this.get(0);
            var triggerText = profile.getSubNode('TRIGGER_TEXT');
            var text = this.getSelectedText();
            
            if (text) {
                triggerText.html(text);
                triggerText.removeClass('ood-mobile-picker-placeholder');
            } else {
                triggerText.html(profile.properties.placeholder || '请选择');
                triggerText.addClass('ood-mobile-picker-placeholder');
            }
        },
        
        getSelectedText: function() {
            var selectedOptions = this.getSelectedOptions();
            return selectedOptions.map(function(option) {
                return option.label || option.text || option.value;
            }).join(', ');
        },
        
        getSelectedOptions: function() {
            var self = this;
            return this._options.filter(function(option) {
                return self._selectedValues.indexOf(option.value) > -1;
            });
        },
        
        setValue: function(value) {
            var profile = this.get(0);
            
            if (profile.properties.multiple) {
                this._selectedValues = Array.isArray(value) ? value : [value];
            } else {
                this._selectedValues = value ? [value] : [];
            }
            
            this.updateSelection();
        },
        
        getValue: function() {
            var profile = this.get(0);
            
            if (profile.properties.multiple) {
                return this._selectedValues.slice();
            } else {
                return this._selectedValues[0] || null;
            }
        },
        
        show: function() {
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var popup = profile.getSubNode('POPUP');
            
            overlay.css('display', 'flex');
            
            // 动画显示
            setTimeout(function() {
                overlay.addClass('ood-mobile-picker-overlay-show');
                popup.addClass('ood-mobile-picker-popup-show');
            }, 10);
        },
        
        hide: function() {
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var popup = profile.getSubNode('POPUP');
            
            overlay.removeClass('ood-mobile-picker-overlay-show');
            popup.removeClass('ood-mobile-picker-popup-show');
            
            setTimeout(function() {
                overlay.css('display', 'none');
            }, 300);
        },
        
        confirm: function() {
            var profile = this.get(0);
            var value = this.getValue();
            
            profile.properties.value = value;
            this.onChange(value);
            this.hide();
        },
        
        onChange: function(value) {
            var profile = this.get(0);
            
            if (profile.onChange) {
                profile.boxing().onChange(profile, value);
            }
        }
    },
    
    Static: {
        // 四分离架构 - 模板定义（Template）
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-picker {_className}',
            style: '{_style}',
            tabindex: '{tabindex}',
            'aria-label': '{_ariaLabel}',
            
            TRIGGER: {
                tagName: 'div',
                className: 'ood-mobile-picker-trigger',
                role: 'button',
                'aria-haspopup': 'listbox',
                'aria-expanded': 'false',
                tabindex: '0',
                
                TRIGGER_TEXT: {
                    tagName: 'span',
                    className: 'ood-mobile-picker-trigger-text {_placeholderClass}',
                    text: '{_triggerText}'
                },
                
                ARROW: {
                    tagName: 'i',
                    className: 'ood-mobile-picker-arrow',
                    'aria-hidden': 'true'
                }
            },
            
            OVERLAY: {
                tagName: 'div',
                className: 'ood-mobile-picker-overlay',
                style: 'display: none',
                role: 'dialog',
                'aria-modal': 'true',
                'aria-label': '{title}',
                
                POPUP: {
                    tagName: 'div',
                    className: 'ood-mobile-picker-popup',
                    
                    HEADER: {
                        tagName: 'div',
                        className: 'ood-mobile-picker-header',
                        
                        CLOSE: {
                            tagName: 'button',
                            className: 'ood-mobile-picker-close',
                            type: 'button',
                            'aria-label': '取消',
                            text: '取消'
                        },
                        
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-mobile-picker-title',
                            text: '{title}'
                        },
                        
                        CONFIRM: {
                            tagName: 'button',
                            className: 'ood-mobile-picker-confirm',
                            type: 'button',
                            'aria-label': '确定',
                            text: '确定'
                        }
                    },
                    
                    OPTIONS: {
                        tagName: 'div',
                        className: 'ood-mobile-picker-options',
                        role: 'listbox',
                        'aria-multiselectable': '{_ariaMulti}'
                    }
                }
            }
        },
        
        // 四分离架构 - 外观定义（Appearances）
        Appearances: {
            KEY: {
                'position': 'relative',
                'width': '100%',
                'box-sizing': 'border-box',
                'font-family': 'var(--mobile-font-family, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif)'
            },
            
            TRIGGER: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'padding': 'var(--mobile-spacing-sm, 8px) var(--mobile-spacing-md, 12px)',
                'background-color': 'var(--mobile-bg-primary, #FFFFFF)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'cursor': 'pointer',
                'min-height': 'var(--mobile-touch-target, 44px)',
                'transition': 'all 0.2s ease',
                'outline': 'none'
            },
            
            'TRIGGER:hover': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'TRIGGER:focus': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.2))'
            },
            
            'TRIGGER:disabled': {
                'background-color': 'var(--mobile-disabled-bg, #F7F7F7)',
                'border-color': 'var(--mobile-disabled-border, #E0E0E0)',
                'cursor': 'not-allowed',
                'opacity': '0.6'
            },
            
            TRIGGER_TEXT: {
                'flex': '1',
                'font-size': 'var(--mobile-font-md, 16px)',
                'color': 'var(--mobile-text-primary, #000000)',
                'white-space': 'nowrap',
                'overflow': 'hidden',
                'text-overflow': 'ellipsis',
                'text-align': 'left'
            },
            
            'TRIGGER_TEXT.ood-mobile-picker-placeholder': {
                'color': 'var(--mobile-text-tertiary, #C7C7CC)'
            },
            
            ARROW: {
                'width': '0',
                'height': '0',
                'border-left': '4px solid transparent',
                'border-right': '4px solid transparent',
                'border-top': '4px solid var(--mobile-text-tertiary, #C7C7CC)',
                'margin-left': 'var(--mobile-spacing-sm, 8px)',
                'transition': 'transform 0.2s ease'
            },
            
            'TRIGGER[aria-expanded="true"] ARROW': {
                'transform': 'rotate(180deg)'
            },
            
            OVERLAY: {
                'position': 'fixed',
                'top': '0',
                'left': '0',
                'width': '100%',
                'height': '100%',
                'background-color': 'rgba(0, 0, 0, 0.5)',
                'z-index': '1000',
                'display': 'flex',
                'align-items': 'flex-end',
                'justify-content': 'center',
                'opacity': '0',
                'transition': 'opacity 0.3s ease'
            },
            
            'OVERLAY.ood-mobile-picker-overlay-show': {
                'opacity': '1'
            },
            
            POPUP: {
                'background-color': 'var(--mobile-bg-primary, #FFFFFF)',
                'border-radius': 'var(--mobile-radius-lg, 12px) var(--mobile-radius-lg, 12px) 0 0',
                'max-height': '60vh',
                'width': '100%',
                'max-width': '500px',
                'box-shadow': '0 -4px 20px rgba(0, 0, 0, 0.1)',
                'transform': 'translateY(100%)',
                'transition': 'transform 0.3s ease'
            },
            
            'POPUP.ood-mobile-picker-popup-show': {
                'transform': 'translateY(0)'
            },
            
            HEADER: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'border-bottom': '1px solid var(--mobile-border, #C7C7CC)',
                'min-height': '56px'
            },
            
            CLOSE: {
                'background-color': 'transparent',
                'border': 'none',
                'color': 'var(--mobile-text-secondary, #8E8E93)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'cursor': 'pointer',
                'padding': 'var(--mobile-spacing-xs, 4px) var(--mobile-spacing-sm, 8px)',
                'border-radius': 'var(--mobile-radius-sm, 4px)',
                'transition': 'background-color 0.2s ease'
            },
            
            'CLOSE:hover': {
                'background-color': 'var(--mobile-bg-secondary, #F2F2F7)'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg, 18px)',
                'font-weight': '600',
                'color': 'var(--mobile-text-primary, #000000)',
                'text-align': 'center',
                'flex': '1'
            },
            
            CONFIRM: {
                'background-color': 'transparent',
                'border': 'none',
                'color': 'var(--mobile-primary, #007AFF)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '600',
                'cursor': 'pointer',
                'padding': 'var(--mobile-spacing-xs, 4px) var(--mobile-spacing-sm, 8px)',
                'border-radius': 'var(--mobile-radius-sm, 4px)',
                'transition': 'background-color 0.2s ease'
            },
            
            'CONFIRM:hover': {
                'background-color': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'CONFIRM:disabled': {
                'color': 'var(--mobile-disabled, #C7C7CC)',
                'cursor': 'not-allowed'
            },
            
            OPTIONS: {
                'max-height': '40vh',
                'overflow-y': 'auto',
                'padding': 'var(--mobile-spacing-xs, 4px) 0'
            },
            
            '.ood-mobile-picker-option': {
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'border-bottom': '1px solid var(--mobile-border-light, #F2F2F7)',
                'cursor': 'pointer',
                'font-size': 'var(--mobile-font-md, 16px)',
                'color': 'var(--mobile-text-primary, #000000)',
                'transition': 'background-color 0.2s ease',
                'position': 'relative',
                'min-height': '44px',
                'display': 'flex',
                'align-items': 'center'
            },
            
            '.ood-mobile-picker-option:hover': {
                'background-color': 'var(--mobile-bg-secondary, #F2F2F7)'
            },
            
            '.ood-mobile-picker-option:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-picker-option-selected': {
                'background-color': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))',
                'color': 'var(--mobile-primary, #007AFF)',
                'font-weight': '600'
            },
            
            '.ood-mobile-picker-option-selected::after': {
                'content': '"\u2713"',
                'position': 'absolute',
                'right': 'var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-lg, 18px)',
                'color': 'var(--mobile-primary, #007AFF)'
            },
            
            '.ood-mobile-picker-option-disabled': {
                'opacity': '0.5',
                'cursor': 'not-allowed',
                'color': 'var(--mobile-disabled, #C7C7CC)'
            },
            
            // 响应式断点样式
            '@media (max-width: 479px)': {
                'POPUP': {
                    'max-height': '70vh'
                },
                'OPTIONS': {
                    'max-height': '50vh'
                },
                'HEADER': {
                    'padding': 'var(--mobile-spacing-sm, 8px) var(--mobile-spacing-md, 12px)'
                }
            },
            
            // 主题支持
            '[data-theme="dark"]': {
                '--mobile-bg-primary': '#1C1C1E',
                '--mobile-bg-secondary': '#2C2C2E',
                '--mobile-border': '#38383A',
                '--mobile-border-light': '#2C2C2E',
                '--mobile-text-primary': '#FFFFFF',
                '--mobile-text-secondary': '#8E8E93',
                '--mobile-text-tertiary': '#6D6D70',
                '--mobile-primary': '#0A84FF',
                '--mobile-primary-light': 'rgba(10, 132, 255, 0.1)'
            },
            
            '[data-theme="highcontrast"]': {
                '--mobile-primary': '#0000FF',
                '--mobile-text-primary': '#000000',
                '--mobile-border': '#000000',
                '--mobile-bg-primary': '#FFFFFF'
            }
        },
        
        // 四分离架构 - 数据模型（DataModel）
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '选择器标题',
                ini: '选择器',
                action: function(value) {
                    var profile = this;
                    // 更新ariaLabel属性保持同步
                    profile.properties.ariaLabel = value;
                    profile.getRoot().attr('aria-label', value || '选择器');
                }
            },
            
            // 继承自UI基类的基本属性
            width: {
                caption: '选择器宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            height: {
                caption: '选择器高度',
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
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            triggerIcon: {
                caption: '触发器图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var iconNode = this.getSubNode('ICON');
                    if (iconNode && !iconNode.isEmpty()) {
                        iconNode.html(value || '');
                    }
                }
            },
            
            // ===== 选择器特有属性 =====
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    var trigger = this.getSubNode('TRIGGER');
                    var confirm = this.getSubNode('CONFIRM');
                    
                    if (trigger && !trigger.isEmpty()) {
                        trigger.prop('disabled', value);
                        trigger.attr('aria-disabled', value.toString());
                    }
                    if (confirm && !confirm.isEmpty()) {
                        confirm.prop('disabled', value);
                    }
                }
            },
            
            // 选择器特有属性
            options: {
                ini: [],
                action: function(value) {
                    this.boxing().setOptions(value);
                }
            },
            
            value: {
                ini: null,
                action: function(value) {
                    this.boxing().setValue(value);
                }
            },
            
            placeholder: {
                ini: '请选择',
                action: function(value) {
                    this.boxing().updateTriggerText();
                }
            },
            
            title: {
                ini: '请选择'
            },
            
            multiple: {
                ini: false,
                action: function(value) {
                    var options = this.getSubNode('OPTIONS');
                    if (options && !options.isEmpty()) {
                        options.attr('aria-multiselectable', value.toString());
                    }
                }
            },
            
            // 主题支持
            theme: {
                ini: '',
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // 响应式支持
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            // 选项配置
            searchable: {
                ini: false
            },
            
            clearable: {
                ini: false
            },
            
            maxSelected: {
                ini: null
            },
            
            // 动画效果
            enableAnimations: {
                ini: true
            },
            
            // 可访问性支持
            ariaLabel: {
                ini: '选择器',
                action: function(value) {
                    this.getRoot().attr('aria-label', value);
                }
            },
            
            // 继承自UI基类的其他属性
            tabindex: {
                ini: -1
            }
        },
        

        // 渲染触发器（组件初始化后调用）
        RenderTrigger: function() {
            var profile = this;
            
            // 异步执行初始化，避免循环依赖
            ood.asyRun(function() {
                // 触发Boxing的Initialize方法
                if (profile.boxing && profile.boxing().Initialize) {
                    profile.boxing().Initialize();
                }
                
                // 初始化主题
                var theme = profile.properties.theme || 
                           localStorage.getItem('ood-mobile-picker-theme') || 
                           (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
                           
                if (theme !== 'light') {
                    profile.boxing().setTheme(theme, false);
                }
                
                // 初始化响应式布局
                if (profile.properties.responsive !== false) {
                    profile.boxing().adjustLayout();
                }
                
                // 触发自定义渲染事件
                if (profile.onRender) {
                    profile.boxing().onRender(profile);
                }
            });
        },
        
        // 数据准备方法（模板渲染前调用）
        _prepareData: function(profile) {
            // 调用父类的数据准备方法
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 准备选择器特定数据
            data._triggerText = props.placeholder || '请选择';
            data._placeholderClass = 'ood-mobile-picker-placeholder';
            data._ariaLabel = props.ariaLabel || '选择器';
            data._ariaMulti = props.multiple ? 'true' : 'false';
            
            // 主题相关数据
            data._theme = props.theme || 'light';
            data._themeClass = props.theme && props.theme !== 'light' ? ('mobile-theme-' + props.theme) : '';
            
            // 响应式数据
            data._screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            data._isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            
            // 合并className
            var classNames = ['ood-mobile-picker', 'ood-mobile-component'];
            if (data._themeClass) classNames.push(data._themeClass);
            if (data._isMobile) classNames.push('mobile-device');
            classNames.push('picker-' + data._screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            // 设置选择器属性
            data.title = props.title || '请选择';
            data.tabindex = props.tabindex || -1;
            
            // 设置禁用状态
            data._disabled = props.disabled ? 'disabled' : '';
            
            // 设置表单字段属性
            data._isFormField = props.isFormField ? 'form-field' : '';
            
            // 设置搜索和清除属性
            data._searchable = props.searchable ? 'searchable' : '';
            data._clearable = props.clearable ? 'clearable' : '';
            
            // 设置动画属性
            data._enableAnimations = props.enableAnimations !== false ? 'animations-enabled' : '';
            
            // 设置样式
            data._style = props.style || '';
            
            return data;
        },
        
        // 四分离架构 - 行为定义（Behaviors）
        Behaviors: {
            // 鼠标悬停效果支持
            HoverEffected: {
                KEY: 'TRIGGER,CLOSE,CONFIRM'
            },
            
            // 支持面板操作（继承自UI）
            PanelKeys: ['KEY'],
            
            // 触发器点击行为
            onTriggerClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 显示选择器
                if (profile.onShow) {
                    var result = profile.boxing().onShow(profile, e);
                    if (result !== false) {
                        profile.boxing().show();
                    }
                    return result;
                } else {
                    profile.boxing().show();
                    return true;
                }
            },
            
            // 选项点击行为
            onOptionClick: function(profile, value, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 选中选项
                profile.boxing().selectOption(value);
                
                // 触发选项点击事件
                if (profile.onOptionClick) {
                    return profile.boxing().onOptionClick(profile, value, e);
                }
                
                return true;
            },
            
            // 确认行为
            onConfirm: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                var value = profile.boxing().getValue();
                
                // 触发确认事件
                if (profile.onConfirm) {
                    var result = profile.boxing().onConfirm(profile, value, e);
                    if (result !== false) {
                        profile.boxing().confirm();
                    }
                    return result;
                } else {
                    profile.boxing().confirm();
                    return true;
                }
            },
            
            // 取消行为
            onCancel: function(profile, e, src) {
                var props = profile.properties;
                
                // 触发取消事件
                if (profile.onCancel) {
                    var result = profile.boxing().onCancel(profile, e);
                    if (result !== false) {
                        profile.boxing().hide();
                    }
                    return result;
                } else {
                    profile.boxing().hide();
                    return true;
                }
            },
            
            // 通用点击事件处理
            onClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 根据点击目标处理不同逻辑
                var target = ood(e.target);
                
                if (target.hasClass('ood-mobile-picker-trigger') || target.closest('.ood-mobile-picker-trigger').length) {
                    return this.onTriggerClick(profile, e, src);
                } else if (target.hasClass('ood-mobile-picker-option')) {
                    var value = target.attr('data-value');
                    return this.onOptionClick(profile, value, e, src);
                } else if (target.hasClass('ood-mobile-picker-confirm')) {
                    return this.onConfirm(profile, e, src);
                } else if (target.hasClass('ood-mobile-picker-close')) {
                    return this.onCancel(profile, e, src);
                }
                
                // 触发通用点击事件
                if (profile.onClick) {
                    return profile.boxing().onClick(profile, e, src);
                }
                
                return true;
            }
        },
        
        // 四分离架构 - 事件处理器（EventHandlers）
        EventHandlers: {
            // 触发器点击事件
            onTriggerClick: function(profile, event) {
                // 触发器点击处理
                return true;
            },
            
            // 弹窗显示事件
            onPopupShow: function(profile) {
                // 弹窗显示处理
                return true;
            },
            
            // 弹窗隐藏事件
            onPopupHide: function(profile) {
                // 弹窗隐藏处理
                return true;
            },
            
            // 选项点击事件
            onOptionClick: function(profile, value, event) {
                // 选项点击处理
                return true;
            },
            
            // 确认事件
            onConfirm: function(profile, value, event) {
                // 确认选择处理
                return true;
            },
            
            // 取消事件
            onCancel: function(profile, event) {
                // 取消选择处理
                return true;
            },
            
            // 主题变化事件
            onThemeChange: function(profile, oldTheme, newTheme) {
                // 主题切换处理
                console.log('选择器主题切换:', oldTheme, '->', newTheme);
                return true;
            },
            
            // 继承自UI基类的事件处理器
            onClick: function(profile, event, src) {
                // 通用点击事件处理
                return true;
            },
            
            onRender: function(profile) {
                // 渲染完成事件
                return true;
            },
            
            onResize: function(profile, width, height) {
                // 尺寸变化事件
                profile.boxing().adjustLayout();
                return true;
            },
            
            // 错误处理
            onError: function(profile, error, context) {
                console.error('选择器错误:', error, context);
                return true;
            }
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Picker组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                trigger = profile.getSubNode('TRIGGER'),
                popup = profile.getSubNode('POPUP'),
                optionsContainer = profile.getSubNode('OPTIONS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整选择器容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    trigger.css('width', '100%');
                    
                    // 调整弹窗的最大宽度
                    if (popup && !popup.isEmpty()) {
                        // 限制弹窗最大宽度为屏幕宽度的90%
                        var popupMaxWidth = Math.min(pxWidth, window.innerWidth * 0.9);
                        popup.css('max-width', adjustunit(popupMaxWidth));
                    }
                }
            }

            // 如果提供了高度，调整选择器容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    trigger.css('height', '100%');
                    
                    // 调整选项容器的高度以适应弹窗
                    if (optionsContainer && !optionsContainer.isEmpty() && popup && !popup.isEmpty()) {
                        var headerHeight = profile.getSubNode('HEADER').offsetHeight(true) || 0;
                        var popupHeight = popup.cssSize().height;
                        var availableHeight = popupHeight - headerHeight;
                        
                        optionsContainer.css('max-height', adjustunit(availableHeight));
                    }
                }
            }

            // 根据新的尺寸调整布局
            this.boxing().adjustLayout();
        }
    }
});/**
 * 移动端开关组件
 * 继承自 ood.UI（UI基类）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持触摸交互、动画效果、禁用状态等
 */
ood.Class("ood.Mobile.Switch", ["ood.UI", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['LABEL', 'SWITCH', 'TRACK', 'THUMB', 'INPUT']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            width: '3em',
            height: '1.5em',
            caption: '开关控件',
            value: false,
            theme: 'light',
            responsive: true
        },

        Initialize: function() {
            // 调用父类的初始化方法
         //   this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化开关特性
            this.initSwitchFeatures();
            
            // 绑定事件
            this.bindTouchEvents();
        },
        
        // ood.absValue 必需方法 - 控制值管理
        _setCtrlValue: function(value) {
            // Switch 的值为布尔值
            var checked = !!value;
            this.setChecked(checked);
        },
        
        _getCtrlValue: function() {
            // 返回当前的开关状态
            return this.getChecked();
        },
        
        activate: function() {
            return this.each(function(profile) {
                // 激活开关（聚焦）
                var switchEl = profile.getSubNode('SWITCH');
                if (switchEl) {
                    switchEl.focus();
                }
            });
        },
        
        resetValue: function(value) {
            // 重写基类的 resetValue 方法
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
                profile.properties.checked = !!value;
            });
            return this;
        },
        
        // 表单字段支持
        _isFormField: function() {
            var profile = this.get(0);
            return profile && profile.properties.isFormField;
        },
        
        // 主题支持方法
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme || 'light';
                var root = profile.getRoot();
                
                // 清除现有主题类
                root.removeClass('ood-mobile-switch-dark ood-mobile-switch-hc');
                
                // 应用新主题类
                switch (theme) {
                    case 'dark':
                        root.addClass('ood-mobile-switch-dark');
                        break;
                    case 'light-hc':
                    case 'dark-hc':
                        root.addClass('ood-mobile-switch-hc');
                        break;
                }
            });
        },
        
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme || 'light' : 'light';
        },
        
        initSwitchFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-switch');
            this.updateSwitchState();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var switchEl = profile.getSubNode('SWITCH');
            
            switchEl.on('click', function(e) {
                if (profile.properties.disabled) return;
                
                e.preventDefault();
                self.toggle();
            });
            
            // 触摸反馈
            switchEl.on('touchstart', function(e) {
                if (profile.properties.disabled) return;
                switchEl.addClass('ood-mobile-switch-pressed');
            });
            
            switchEl.on('touchend touchcancel', function(e) {
                switchEl.removeClass('ood-mobile-switch-pressed');
            });
        },
        
        updateSwitchState: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            var switchEl = profile.getSubNode('SWITCH');
            
            // 更新状态类
            if (props.checked) {
                root.addClass('ood-mobile-switch-checked');
                switchEl.attr('aria-checked', 'true');
            } else {
                root.removeClass('ood-mobile-switch-checked');
                switchEl.attr('aria-checked', 'false');
            }
            
            // 更新禁用状态
            if (props.disabled) {
                root.addClass('ood-mobile-switch-disabled');
                switchEl.attr('aria-disabled', 'true');
            } else {
                root.removeClass('ood-mobile-switch-disabled');
                switchEl.attr('aria-disabled', 'false');
            }
        },
        
        toggle: function() {
            var profile = this.get(0);
            if (profile.properties.disabled) return;
            
            this.setChecked(!profile.properties.checked);
        },
        
        setChecked: function(checked) {
            var profile = this.get(0);
            var oldValue = profile.properties.checked;
            
            profile.properties.checked = checked;
            this.updateSwitchState();
            
            // 触发change事件
            if (oldValue !== checked) {
                this.onChange(checked);
            }
        },
        
        getChecked: function() {
            var profile = this.get(0);
            return profile.properties.checked;
        },
        
        setDisabled: function(disabled) {
            var profile = this.get(0);
            profile.properties.disabled = disabled;
            this.updateSwitchState();
        },
        
        onChange: function(checked) {
            var profile = this.get(0);
            
            if (profile.onChange) {
                profile.boxing().onChange(profile, checked);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-switch',
            style: '{_style}',
            
            LABEL: {
                tagName: 'label',
                className: 'ood-mobile-switch-label',
                text: '{label}',
                style: 'display: {_labelDisplay}'
            },
            
            SWITCH: {
                tagName: 'div',
                className: 'ood-mobile-switch-element',
                role: 'switch',
                tabindex: '0',
                'aria-checked': '{_checked}',
                
                TRACK: {
                    tagName: 'div',
                    className: 'ood-mobile-switch-track'
                },
                
                THUMB: {
                    tagName: 'div',
                    className: 'ood-mobile-switch-thumb'
                }
            }
        },
        
        Appearances: {
            KEY: {
                display: 'inline-flex',
                'align-items': 'center',
                gap: 'var(--mobile-spacing-sm)',
                cursor: 'pointer'
            },
            
            LABEL: {
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-text-primary)',
                'user-select': 'none'
            },
            
            SWITCH: {
                position: 'relative',
                width: '48px',
                height: '28px',
                outline: 'none',
                cursor: 'pointer'
            },
            
            TRACK: {
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                'background-color': '#E5E5E7',
                'border-radius': '14px',
                transition: 'background-color 0.3s ease'
            },
            
            THUMB: {
                position: 'absolute',
                top: '2px',
                left: '2px',
                width: '24px',
                height: '24px',
                'background-color': '#FFFFFF',
                'border-radius': '50%',
                'box-shadow': '0 2px 4px rgba(0,0,0,0.2)',
                transition: 'transform 0.3s ease'
            },
            
            // 选中状态
            'KEY.ood-mobile-switch-checked TRACK': {
                'background-color': 'var(--mobile-primary)'
            },
            
            'KEY.ood-mobile-switch-checked THUMB': {
                transform: 'translateX(20px)'
            },
            
            // 禁用状态
            'KEY.ood-mobile-switch-disabled': {
                opacity: '0.5',
                cursor: 'not-allowed'
            },
            
            // 按下状态
            'KEY:not(.ood-mobile-switch-disabled) SWITCH.ood-mobile-switch-pressed THUMB': {
                transform: 'scale(1.1)'
            },
            
            'KEY:not(.ood-mobile-switch-disabled).ood-mobile-switch-checked SWITCH.ood-mobile-switch-pressed THUMB': {
                transform: 'translateX(20px) scale(1.1)'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: true
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '开关标题',
                ini: '开关控件',
                action: function(value) {
                    var profile = this;
                    // 同步更新label属性
                    profile.properties.label = value;
                    profile.getRoot().attr('aria-label', value || '开关控件');
                }
            },
            
            width: {
                caption: '开关宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            height: {
                caption: '开关高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            trackColor: {
                caption: '轨道颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var trackNode = this.getSubNode('TRACK');
                    if (value && trackNode && !trackNode.isEmpty()) {
                        trackNode.css('background-color', value);
                    }
                }
            },
            
            thumbColor: {
                caption: '拇指颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var thumbNode = this.getSubNode('THUMB');
                    if (value && thumbNode && !thumbNode.isEmpty()) {
                        thumbNode.css('background-color', value);
                    }
                }
            },
            
            // ===== 开关特有属性 =====
            // ood.UI 主题属性
            theme: {
                caption: '主题模式',
                ini: 'light',
                listbox: ['light', 'dark', 'light-hc', 'dark-hc'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // ood.absValue 必需属性
            value: {
                caption: '开关值',
                ini: false,
                action: function(value) {
                    this.boxing()._setCtrlValue(value);
                }
            },
            
            isFormField: {
                caption: '表单字段',
                ini: true
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: false
            },
            
            readonly: {
                caption: '只读',
                ini: false
            },
            
            // Switch 特定属性
            checked: {
                caption: '选中状态',
                ini: false,
                action: function(value) {
                    this.boxing().setChecked(value);
                }
            },
            
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    this.boxing().setDisabled(value);
                }
            },
            
            label: {
                caption: '开关标签',
                ini: ''
            },
            
            size: {
                caption: '开关尺寸',
                ini: 'md',
                listbox: ['sm', 'md', 'lg']
            },
            
            // 事件处理器
            onChange: {
                caption: '状态改变事件处理器',
                ini: null
            },
            
            onChanged: {
                caption: '值改变事件处理器',
                ini: null
            }
        },
        
        // ood.absValue 静态方法
        _isFormField: function() {
            return true; // Switch 组件可以作为表单字段
        },
        
        _ensureValue: function(value) {
            // 确保值为布尔类型
            return !!value;
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().initSwitchFeatures();
                
                // 设置默认值
                var value = profile.properties.value !== undefined ? profile.properties.value : profile.properties.checked;
                if (value !== null && value !== undefined) {
                    profile.boxing()._setCtrlValue(value);
                }
                
                // 触发准备就绪事件
                if (profile.onReady) {
                    profile.boxing().onReady(profile);
                }
            });
        },
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            data._checked = props.checked ? 'true' : 'false';
            data._labelDisplay = props.label ? 'block' : 'none';
            
            // 设置开关属性
            data.label = props.label || props.caption || '';
            
            // 设置禁用和只读状态
            data._disabled = props.disabled ? 'disabled' : '';
            data._readonly = props.readonly ? 'readonly' : '';
            
            // 设置尺寸类
            data._sizeClass = props.size && props.size !== 'md' ? 'ood-mobile-switch-' + props.size : '';
            
            // 设置主题类
            data._themeClass = props.theme && props.theme !== 'light' ? 'switch-theme-' + props.theme : '';
            
            // 设置ARIA标签
            data._ariaLabel = props.ariaLabel || props.label || props.caption || '开关';
            
            // 设置表单字段属性
            data._isFormField = props.isFormField ? 'form-field' : '';
            
            // 设置样式
            data._style = props.style || '';
            
            return data;
        },
        
        EventHandlers: {
            // ood.UI 生命周期事件处理器
            onReady: function(profile) {
                // 组件就绪时的初始化
                profile.boxing().initSwitchFeatures();
            },
            
            onCreated: function(profile) {
                // 组件创建完成时的处理
            },
            
            onDestroy: function(profile) {
                // 组件销毁时的清理
            },
            
            // ood.absValue 事件处理器
            onChanged: function(profile, e, src, value) {
                // 值改变事件
            },
            
            onChecked: function(profile, e, src, value) {
                // 选中事件
            },
            
            onValueSet: function(profile, oldValue, newValue) {
                // 值设置事件
            },
            
            // Switch 特定事件处理器
            onChange: function(profile, checked) {
                // 开关状态变化事件处理器
            }
        }
    }
});
/**
 * 移动端网格布局组件
 * 继承自 ood.UI（UI基类）和 ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持响应式布局、间距控制、对齐方式等
 */
ood.Class("ood.Mobile.Grid", ["ood.UI", "ood.absContainer"], {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['CONTAINER']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
          //  this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端网格特性
            this.initMobileGridFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileGridFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端网格CSS类
            profile.getRoot().addClass('ood-mobile-grid ood-mobile-component');
            
            // 初始化网格功能
            this.initGridFeatures();
        },
        
        initGridFeatures: function() {
            // 原有的网格特性初始化逻辑
            this.updateGridLayout();
        },
        
        updateGridLayout: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var container = profile.getSubNode('CONTAINER');
            
            // 设置网格布局
            container.css({
                'grid-template-columns': this.getGridColumns(props.columns),
                'gap': this.getGridGap(props.gap),
                'justify-content': props.justifyContent || 'start',
                'align-content': props.alignContent || 'start'
            });
        },
        
        getGridColumns: function(columns) {
            if (typeof columns === 'number') {
                return 'repeat(' + columns + ', 1fr)';
            } else if (typeof columns === 'string') {
                return columns;
            }
            return 'repeat(auto-fit, minmax(250px, 1fr))';
        },
        
        getGridGap: function(gap) {
            if (typeof gap === 'number') {
                return gap + 'px';
            } else if (typeof gap === 'string') {
                return gap;
            }
            return 'var(--mobile-spacing-md)';
        },
        
        addGridItem: function(content, gridArea) {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 修正：使用正确的DOM创建方法
            var item = document.createElement('div');
            item.className = 'ood-mobile-grid-item';
            
            if (gridArea) {
                item.style.gridArea = gridArea;
            }
            
            if (typeof content === 'string') {
                item.innerHTML = content;
            } else if (content && content.nodeType) {
                // 如果是DOM节点
                item.appendChild(content);
            } else if (content && content.get && content.get(0)) {
                // 如果是ood对象
                item.appendChild(content.get(0));
            }
            
            // 使用ood框架的DOM操作方法
            var containerNode = container.get(0);
            if (containerNode) {
                containerNode.appendChild(item);
            }
            
            // 返回ood包装的元素（使用ood的正确调用方式）
            return ood([item], false);
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-grid',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-grid-container'
            }
        },
        
        Appearances: {
            KEY: {
                width: '100%'
            },
            
            CONTAINER: {
                display: 'grid',
                width: '100%'
            },
            
            '.ood-mobile-grid-item': {
                'min-width': 0,
                'min-height': 0
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '网格标题',
                ini: '网格布局',
                action: function(value) {
                    var profile = this;
                    // 可以添加标题显示节点的逻辑
                    profile.getRoot().attr('aria-label', value || '网格布局');
                }
            },
            
            width: {
                caption: '网格宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '网格高度',
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
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            // ===== 网格特有属性 =====
            columns: {
                caption: '网格列数',
                ini: 'auto'
            },
            
            gap: {
                caption: '网格间距',
                ini: 'var(--mobile-spacing-md)'
            },
            
            justifyContent: {
                caption: '水平对齐',
                ini: 'start',
                listbox: ['start', 'end', 'center', 'stretch', 'space-around', 'space-between', 'space-evenly']
            },
            
            alignContent: {
                caption: '垂直对齐',
                ini: 'start',
                listbox: ['start', 'end', 'center', 'stretch', 'space-around', 'space-between', 'space-evenly']
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        }
    }
});/**
 * 移动端布局组件
 * 继承自 ood.UI（UI基类）和 ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持flex布局、安全区域适配、响应式布局
 */
ood.Class("ood.Mobile.Layout",  ["ood.UI", "ood.absList"], {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['CONTAINER']);
    },
    Instance: {
        Initialize: function() {
              // 初始化移动端布局特性
            this.initMobileLayoutFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileLayoutFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端布局CSS类
            profile.getRoot().addClass('ood-mobile-layout ood-mobile-component');
            
            // 初始化布局功能
            this.initLayoutFeatures();
        },
        
        initLayoutFeatures: function() {
            // 原有的布局特性初始化逻辑
            this.updateLayoutStyle();
            this.applySafeArea();
        },
        
        updateLayoutStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var container = profile.getSubNode('CONTAINER');
            
            container.css({
                'flex-direction': props.direction || 'column',
                'justify-content': props.justifyContent || 'flex-start',
                'align-items': props.alignItems || 'stretch',
                'gap': props.gap || 'var(--mobile-spacing-md)',
                'padding': props.padding || '0'
            });
        },
        
        applySafeArea: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.safeArea) {
                profile.getRoot().addClass('ood-mobile-layout-safe');
            }
        },
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-layout {_directionClass} {_safeAreaClass}',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-layout-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: '100%'
            },
            
            CONTAINER: {
                display: 'flex',
                width: '100%',
                height: '100%'
            },
            
            'KEY-safe': {
                'padding-top': 'var(--mobile-safe-top)',
                'padding-right': 'var(--mobile-safe-right)',
                'padding-bottom': 'var(--mobile-safe-bottom)',
                'padding-left': 'var(--mobile-safe-left)'
            },
            
            'KEY-row CONTAINER': {
                'flex-direction': 'row'
            },
            
            'KEY-column CONTAINER': {
                'flex-direction': 'column'
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '布局标题',
                ini: '布局容器',
                action: function(value) {
                    var profile = this;
                    // 为布局容器添加无障碍标签
                    profile.getRoot().attr('aria-label', value || '布局容器');
                }
            },
            
            width: {
                caption: '布局宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '布局高度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // ===== 设计器特殊类型属性 =====
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            // ===== 布局特有属性 =====
            direction: {
                caption: '布局方向',
                ini: 'column',
                listbox: ['row', 'column'],
                action: function(value) {
                    this.boxing().setDirection(value);
                }
            },
            
            justifyContent: {
                caption: '主轴对齐',
                ini: 'flex-start',
                listbox: ['flex-start', 'flex-end', 'center', 'space-between', 'space-around', 'space-evenly'],
                action: function(value) {
                    this.boxing().setJustifyContent(value);
                }
            },
            
            alignItems: {
                caption: '交叉轴对齐',
                ini: 'stretch',
                listbox: ['flex-start', 'flex-end', 'center', 'stretch', 'baseline'],
                action: function(value) {
                    this.boxing().setAlignItems(value);
                }
            },
            
            gap: {
                caption: '内容间距',
                ini: 'var(--mobile-spacing-md)'
            },
            
            padding: {
                caption: '内边距',
                ini: '0'
            },
            
            safeArea: {
                caption: '安全区域适配',
                ini: false
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
            data._style=props.style;
            data.gap=props.gap;
            data._directionClass = props.direction ? 'ood-mobile-layout-' + props.direction : '';
            data._safeAreaClass = props.safeArea ? 'ood-mobile-layout-safe' : '';
            
            return data;
        }
    }
});/**
 * 移动端面板组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持标题、边框、阴影、圆角等样式配置
 */
ood.Class("ood.Mobile.Panel", "ood.UI.Div",  {
    Instance: {
        Initialize: function() {
           // this.constructor.upper.prototype.Initialize.call(this);
            this.initPanelFeatures();
        },
        
        initPanelFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-panel');
            this.updatePanelStyle();
        },
        
        updatePanelStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 应用样式类
            root.removeClass('ood-mobile-panel-bordered ood-mobile-panel-shadow ood-mobile-panel-rounded');
            
            if (props.bordered) root.addClass('ood-mobile-panel-bordered');
            if (props.shadow) root.addClass('ood-mobile-panel-shadow');
            if (props.rounded) root.addClass('ood-mobile-panel-rounded');
        },

    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-panel {_styleClasses}',
            style: '{_style}',
            
            HEADER: {
                tagName: 'div',
                className: 'ood-mobile-panel-header',
                style: 'display: {_headerDisplay}',
                
                TITLE: {
                    tagName: 'div',
                    className: 'ood-mobile-panel-title',
                    text: '{title}'
                },
                
                EXTRA: {
                    tagName: 'div',
                    className: 'ood-mobile-panel-extra',
                    style: 'display: {_extraDisplay}'
                }
            },

            MAIN: {
                $order: 2,
                tagName: 'div',
                className: 'ood-mobile-panel-content',
                style: "{_leftp}",
                MAINI: {
                    tagName: 'div',
                    className: 'ood-uicon-maini ood-uibar',
                    style: "{_rightp}",
                    PANEL: {
                        tagName: 'div',
                        className: 'ood-uibase ood-uicontainer {_bordertype}',
                        style: '{panelDisplay};{_panelstyle};{_overflow};',
                        text: '{html}' + ood.UI.$childTag
                    }
                }
            }
            //
            // CONTENT: {
            //     tagName: 'div',
            //     className: 'ood-mobile-panel-content'
            // }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                'background-color': 'var(--mobile-bg-primary)',
                width: '100%'
            },
            
            'KEY.ood-mobile-panel-bordered': {
                border: '1px solid var(--mobile-border-color)'
            },
            
            'KEY.ood-mobile-panel-shadow': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            'KEY.ood-mobile-panel-rounded': {
                'border-radius': 'var(--mobile-border-radius)'
            },
            
            HEADER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4'
            },
            
            EXTRA: {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)'
            },
            
            MAIN: {
                padding: 'var(--mobile-spacing-md)'
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '面板标题',
                ini: '面板组件',
                action: function(value) {
                    var profile = this.boxing().get(0);
                    // 同步更新title属性
                    profile.properties.title = value;
                    profile.getRoot().attr('aria-label', value || '面板组件');
                }
            },
            
            width: {
                caption: '面板宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '面板高度',
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
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            titleColor: {
                caption: '标题颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var titleNode = this.getSubNode('TITLE');
                    if (value && titleNode && !titleNode.isEmpty()) {
                        titleNode.css('color', value);
                    }
                }
            },
            
            // ===== 面板特有属性 =====
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
            
            title: {
                caption: '面板标题',
                ini: '',
                action: function(value) {
                    var profile = this.get(0);
                    var titleNode = profile.getSubNode('TITLE');
                    var headerNode = profile.getSubNode('HEADER');
                    profile.properties.title = title;
                    if (title) {
                        titleNode.toHtml(title)
                        headerNode.css('display', 'block');
                    } else {
                        headerNode.css('display', 'none');
                    }

                }
            },
            
            bordered: {
                caption: '显示边框',
                ini: true,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
            },
            
            shadow: {
                caption: '显示阴影',
                ini: false,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
            },
            
            rounded: {
                caption: '圆角样式',
                ini: true,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
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
            
            var classes = [];
            if (props.bordered) classes.push('ood-mobile-panel-bordered');
            if (props.shadow) classes.push('ood-mobile-panel-shadow');
            if (props.rounded) classes.push('ood-mobile-panel-rounded');
            
            data._styleClasses = classes.join(' ');
            data._headerDisplay = props.title ? 'flex' : 'none';
            data._extraDisplay = 'none'; // 默认隐藏额外内容
            
            return data;
        }
    }
});