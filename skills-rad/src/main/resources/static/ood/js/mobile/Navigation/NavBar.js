/**
 * 移动端导航栏组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持标题、左右操作按钮、状态栏适配等功能
 */
ood.Class("ood.Mobile.NavBar", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            //this.constructor.upper.prototype.Initialize.call(this);
            this.initNavBarFeatures();
        },
        
        initNavBarFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-navbar');
            this.applySafeArea();
            this.updateNavBarStyle();
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
            
            // 点击事件 - 符合 ood 三阶段事件机制
            profile.beforeClick = function(profile, e, src) {
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                var target = e.target || e.srcElement;
                var clickedElement = ood(target);
                
                // 判断点击的是哪个元素
                if (clickedElement.closest('.ood-mobile-navbar-left').length > 0) {
                    self.handleLeftButtonClick(profile, e);
                } else if (clickedElement.closest('.ood-mobile-navbar-right').length > 0) {
                    self.handleRightButtonClick(profile, e);
                } else if (clickedElement.closest('.ood-mobile-navbar-title').length > 0) {
                    self.handleTitleClick(profile, e);
                }
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理
                self.afterNavClick(profile, e);
            };
        },
        
        applySafeArea: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.safeArea) {
                profile.getRoot().addClass('ood-mobile-navbar-safe');
            }
        },
        
        updateNavBarStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 应用样式类
            root.removeClass('ood-mobile-navbar-transparent ood-mobile-navbar-bordered');
            
            if (props.transparent) root.addClass('ood-mobile-navbar-transparent');
            if (props.bordered) root.addClass('ood-mobile-navbar-bordered');
        },
        
        setTitle: function(title) {
            var profile = this.get(0);
            var titleNode = profile.getSubNode('TITLE');
            
            profile.properties.title = title;
            titleNode.html(title || '');
        },
        
        setLeftButton: function(config) {
            var profile = this.get(0);
            var leftBtn = profile.getSubNode('LEFT_BUTTON');
            var leftIcon = profile.getSubNode('LEFT_ICON');
            var leftText = profile.getSubNode('LEFT_TEXT');
            
            if (!config) {
                leftBtn.css('display', 'none');
                return;
            }
            
            leftBtn.css('display', 'flex');
            
            if (config.icon) {
                leftIcon.attr('class', 'ood-mobile-navbar-icon ' + config.icon);
                leftIcon.css('display', 'inline-block');
            } else {
                leftIcon.css('display', 'none');
            }
            
            if (config.text) {
                leftText.html(config.text);
                leftText.css('display', 'inline-block');
            } else {
                leftText.css('display', 'none');
            }
            
            profile.properties.leftButton = config;
        },
        
        setRightButton: function(config) {
            var profile = this.get(0);
            var rightBtn = profile.getSubNode('RIGHT_BUTTON');
            var rightIcon = profile.getSubNode('RIGHT_ICON');
            var rightText = profile.getSubNode('RIGHT_TEXT');
            
            if (!config) {
                rightBtn.css('display', 'none');
                return;
            }
            
            rightBtn.css('display', 'flex');
            
            if (config.icon) {
                rightIcon.attr('class', 'ood-mobile-navbar-icon ' + config.icon);
                rightIcon.css('display', 'inline-block');
            } else {
                rightIcon.css('display', 'none');
            }
            
            if (config.text) {
                rightText.html(config.text);
                rightText.css('display', 'inline-block');
            } else {
                rightText.css('display', 'none');
            }
            
            profile.properties.rightButton = config;
        },
        
        // 左侧按钮点击事件处理 - 符合 ood 规范
        handleLeftButtonClick: function(profile, e) {
            var config = profile.properties.leftButton;
            
            if (config && config.handler) {
                config.handler.call(this, e);
            }
            
            // 触发用户自定义事件
            if (profile.properties.onLeftButtonClick && typeof profile.properties.onLeftButtonClick === 'function') {
                profile.properties.onLeftButtonClick.call(this, profile, e);
            }
        },
        
        // 右侧按钮点击事件处理 - 符合 ood 规范
        handleRightButtonClick: function(profile, e) {
            var config = profile.properties.rightButton;
            
            if (config && config.handler) {
                config.handler.call(this, e);
            }
            
            // 触发用户自定义事件
            if (profile.properties.onRightButtonClick && typeof profile.properties.onRightButtonClick === 'function') {
                profile.properties.onRightButtonClick.call(this, profile, e);
            }
        },
        
        // 标题点击事件处理 - 符合 ood 规范
        handleTitleClick: function(profile, e) {
            // 触发用户自定义事件
            if (profile.properties.onTitleClick && typeof profile.properties.onTitleClick === 'function') {
                profile.properties.onTitleClick.call(this, profile, e);
            }
        },
        
        // 导航栏点击后处理
        afterNavClick: function(profile, e) {
            if (profile.properties.onAfterNavClick && typeof profile.properties.onAfterNavClick === 'function') {
                profile.properties.onAfterNavClick.call(this, profile, e);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            // NavBar不支持动态插入项，但为了符合接口规范提供空实现
            return this;
        },
        
        removeItems: function(indices) {
            // NavBar不支持动态删除项，但为了符合接口规范提供空实现
            return this;
        },
        
        clearItems: function() {
            // NavBar不支持清空项，但为了符合接口规范提供空实现
            return this;
        },
        
        getItems: function() {
            // NavBar不支持获取项列表，但为了符合接口规范返回空数组
            return [];
        },
        
        getSelectedItems: function() {
            // NavBar不支持选择项，但为了符合接口规范返回空数组
            return [];
        },
        
        selectItem: function(value) {
            // NavBar不支持选择项，但为了符合接口规范提供空实现
            return this;
        },
        
        unselectItem: function(value) {
            // NavBar不支持取消选择项，但为了符合接口规范提供空实现
            return this;
        },
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-navbar {_styleClasses}',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-navbar-container',
                
                LEFT_BUTTON: {
                    tagName: 'div',
                    className: 'ood-mobile-navbar-button ood-mobile-navbar-left',
                    style: 'display: {_leftButtonDisplay}',
                    
                    LEFT_ICON: {
                        tagName: 'i',
                        className: 'ood-mobile-navbar-icon',
                        style: 'display: none'
                    },
                    
                    LEFT_TEXT: {
                        tagName: 'span',
                        className: 'ood-mobile-navbar-text',
                        style: 'display: none'
                    }
                },
                
                TITLE: {
                    tagName: 'div',
                    className: 'ood-mobile-navbar-title',
                    text: '{title}'
                },
                
                RIGHT_BUTTON: {
                    tagName: 'div',
                    className: 'ood-mobile-navbar-button ood-mobile-navbar-right',
                    style: 'display: {_rightButtonDisplay}',
                    
                    RIGHT_ICON: {
                        tagName: 'i',
                        className: 'ood-mobile-navbar-icon',
                        style: 'display: none'
                    },
                    
                    RIGHT_TEXT: {
                        tagName: 'span',
                        className: 'ood-mobile-navbar-text',
                        style: 'display: none'
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'background-color': 'var(--mobile-bg-primary)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'z-index': 100
            },
            
            'KEY.ood-mobile-navbar-safe': {
                'padding-top': 'var(--mobile-safe-top)'
            },
            
            'KEY.ood-mobile-navbar-transparent': {
                'background-color': 'transparent',
                'border-bottom': 'none'
            },
            
            'KEY.ood-mobile-navbar-bordered': {
                'border-bottom': '2px solid var(--mobile-border-color)'
            },
            
            CONTAINER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                height: '44px',
                padding: '0 var(--mobile-spacing-md)',
                position: 'relative'
            },
            
            'LEFT_BUTTON, RIGHT_BUTTON': {
                display: 'flex',
                'align-items': 'center',
                gap: 'var(--mobile-spacing-xs)',
                'min-width': '44px',
                'min-height': '44px',
                cursor: 'pointer',
                'border-radius': 'var(--mobile-border-radius)',
                transition: 'background-color 0.2s ease'
            },
            
            'LEFT_BUTTON:hover, RIGHT_BUTTON:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            'LEFT_BUTTON': {
                'justify-content': 'flex-start'
            },
            
            'RIGHT_BUTTON': {
                'justify-content': 'flex-end'
            },
            
            TITLE: {
                position: 'absolute',
                left: '50%',
                top: '50%',
                transform: 'translate(-50%, -50%)',
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'white-space': 'nowrap',
                overflow: 'hidden',
                'text-overflow': 'ellipsis',
                'max-width': '60%',
                cursor: 'pointer'
            },
            
            '.ood-mobile-navbar-icon': {
                'font-size': '18px',
                color: 'var(--mobile-primary)'
            },
            
            '.ood-mobile-navbar-text': {
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-primary)',
                'white-space': 'nowrap'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '导航栏标题',
                ini: '导航栏',
                action: function(value) {
                    var profile = this;
                    // 同步更新title属性
                    profile.properties.title = value;
                    this.boxing().setTitle(value);
                    profile.getRoot().attr('aria-label', value || '导航栏');
                }
            },
            
            width: {
                caption: '导航栏宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '导航栏高度',
                $spaceunit: 1,
                ini: '44px'
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
            
            leftIcon: {
                caption: '左侧按钮图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    // 更新左侧按钮图标
                    var leftButton = this.properties.leftButton || {};
                    leftButton.icon = value;
                    this.boxing().setLeftButton(leftButton);
                }
            },
            
            rightIcon: {
                caption: '右侧按钮图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    // 更新右侧按钮图标
                    var rightButton = this.properties.rightButton || {};
                    rightButton.icon = value;
                    this.boxing().setRightButton(rightButton);
                }
            },
            
            // ===== 导航栏特有属性 =====
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
                caption: '导航标题',
                ini: '',
                action: function(value) {
                    this.boxing().setTitle(value);
                }
            },
            
            leftButton: {
                caption: '左侧按钮',
                ini: null,
                action: function(value) {
                    this.boxing().setLeftButton(value);
                }
            },
            
            rightButton: {
                caption: '右侧按钮',
                ini: null,
                action: function(value) {
                    this.boxing().setRightButton(value);
                }
            },
            
            transparent: {
                caption: '透明背景',
                ini: false,
                action: function(value) {
                    this.boxing().updateNavBarStyle();
                }
            },
            
            bordered: {
                caption: '显示下边框',
                ini: false,
                action: function(value) {
                    this.boxing().updateNavBarStyle();
                }
            },
            
            safeArea: {
                caption: '安全区域适配',
                ini: true
            },
            
            // ood 规范事件处理器
            onLeftButtonClick: {
                caption: '左侧按钮点击事件处理器',
                ini: null
            },
            
            onRightButtonClick: {
                caption: '右侧按钮点击事件处理器',
                ini: null
            },
            
            onTitleClick: {
                caption: '标题点击事件处理器',
                ini: null
            },
            
            onAfterNavClick: {
                caption: '导航栏点击后事件处理器',
                ini: null
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
            if (props.transparent) classes.push('ood-mobile-navbar-transparent');
            if (props.bordered) classes.push('ood-mobile-navbar-bordered');
            if (props.safeArea) classes.push('ood-mobile-navbar-safe');
            
            data._styleClasses = classes.join(' ');
            data._leftButtonDisplay = props.leftButton ? 'flex' : 'none';
            data._rightButtonDisplay = props.rightButton ? 'flex' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onLeftButtonClick: function(profile, event) {
                // 左侧按钮点击事件处理器
            },
            
            onRightButtonClick: function(profile, event) {
                // 右侧按钮点击事件处理器
            },
            
            onTitleClick: function(profile, event) {
                // 标题点击事件处理器
            }
        }
    }
});

// 添加Getter/Setter方法
(function() {
    var proto = ood.Mobile.NavBar.prototype;
    
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
    
    // backgroundColor 属性的 getter/setter
    proto.getBackgroundColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.backgroundColor : '';
    };
    
    proto.setBackgroundColor = function(value) {
        return this.each(function(profile) {
            profile.properties.backgroundColor = value;
            if (profile.boxing().backgroundColor && typeof profile.boxing().backgroundColor.action === 'function') {
                profile.boxing().backgroundColor.action.call(profile, value);
            }
        });
    };
    
    // titleColor 属性的 getter/setter
    proto.getTitleColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.titleColor : '';
    };
    
    proto.setTitleColor = function(value) {
        return this.each(function(profile) {
            profile.properties.titleColor = value;
            if (profile.boxing().titleColor && typeof profile.boxing().titleColor.action === 'function') {
                profile.boxing().titleColor.action.call(profile, value);
            }
        });
    };
    
    // leftIcon 属性的 getter/setter
    proto.getLeftIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.leftIcon : '';
    };
    
    proto.setLeftIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.leftIcon = value;
            if (profile.boxing().leftIcon && typeof profile.boxing().leftIcon.action === 'function') {
                profile.boxing().leftIcon.action.call(profile, value);
            }
        });
    };
    
    // rightIcon 属性的 getter/setter
    proto.getRightIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.rightIcon : '';
    };
    
    proto.setRightIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.rightIcon = value;
            if (profile.boxing().rightIcon && typeof profile.boxing().rightIcon.action === 'function') {
                profile.boxing().rightIcon.action.call(profile, value);
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
    
    // title 属性的 getter/setter
    proto.getTitle = function() {
        var profile = this.get(0);
        return profile ? profile.properties.title : '';
    };
    
    proto.setTitle = function(value) {
        return this.each(function(profile) {
            profile.properties.title = value;
            if (profile.boxing().title && typeof profile.boxing().title.action === 'function') {
                profile.boxing().title.action.call(profile, value);
            }
        });
    };
    
    // leftButton 属性的 getter/setter
    proto.getLeftButton = function() {
        var profile = this.get(0);
        return profile ? profile.properties.leftButton : null;
    };
    
    proto.setLeftButton = function(value) {
        return this.each(function(profile) {
            profile.properties.leftButton = value;
            if (profile.boxing().leftButton && typeof profile.boxing().leftButton.action === 'function') {
                profile.boxing().leftButton.action.call(profile, value);
            }
        });
    };
    
    // rightButton 属性的 getter/setter
    proto.getRightButton = function() {
        var profile = this.get(0);
        return profile ? profile.properties.rightButton : null;
    };
    
    proto.setRightButton = function(value) {
        return this.each(function(profile) {
            profile.properties.rightButton = value;
            if (profile.boxing().rightButton && typeof profile.boxing().rightButton.action === 'function') {
                profile.boxing().rightButton.action.call(profile, value);
            }
        });
    };
    
    // transparent 属性的 getter/setter
    proto.getTransparent = function() {
        var profile = this.get(0);
        return profile ? profile.properties.transparent : false;
    };
    
    proto.setTransparent = function(value) {
        return this.each(function(profile) {
            profile.properties.transparent = value;
            if (profile.boxing().transparent && typeof profile.boxing().transparent.action === 'function') {
                profile.boxing().transparent.action.call(profile, value);
            }
        });
    };
    
    // bordered 属性的 getter/setter
    proto.getBordered = function() {
        var profile = this.get(0);
        return profile ? profile.properties.bordered : false;
    };
    
    proto.setBordered = function(value) {
        return this.each(function(profile) {
            profile.properties.bordered = value;
            if (profile.boxing().bordered && typeof profile.boxing().bordered.action === 'function') {
                profile.boxing().bordered.action.call(profile, value);
            }
        });
    };
    
    // safeArea 属性的 getter/setter
    proto.getSafeArea = function() {
        var profile = this.get(0);
        return profile ? profile.properties.safeArea : true;
    };
    
    proto.setSafeArea = function(value) {
        return this.each(function(profile) {
            profile.properties.safeArea = value;
            if (profile.boxing().safeArea && typeof profile.boxing().safeArea.action === 'function') {
                profile.boxing().safeArea.action.call(profile, value);
            }
        });
    };
    
    // onLeftButtonClick 属性的 getter/setter
    proto.getOnLeftButtonClick = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onLeftButtonClick : null;
    };
    
    proto.setOnLeftButtonClick = function(value) {
        return this.each(function(profile) {
            profile.properties.onLeftButtonClick = value;
            if (profile.boxing().onLeftButtonClick && typeof profile.boxing().onLeftButtonClick.action === 'function') {
                profile.boxing().onLeftButtonClick.action.call(profile, value);
            }
        });
    };
    
    // onRightButtonClick 属性的 getter/setter
    proto.getOnRightButtonClick = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onRightButtonClick : null;
    };
    
    proto.setOnRightButtonClick = function(value) {
        return this.each(function(profile) {
            profile.properties.onRightButtonClick = value;
            if (profile.boxing().onRightButtonClick && typeof profile.boxing().onRightButtonClick.action === 'function') {
                profile.boxing().onRightButtonClick.action.call(profile, value);
            }
        });
    };
    
    // onTitleClick 属性的 getter/setter
    proto.getOnTitleClick = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onTitleClick : null;
    };
    
    proto.setOnTitleClick = function(value) {
        return this.each(function(profile) {
            profile.properties.onTitleClick = value;
            if (profile.boxing().onTitleClick && typeof profile.boxing().onTitleClick.action === 'function') {
                profile.boxing().onTitleClick.action.call(profile, value);
            }
        });
    };
    
    // onAfterNavClick 属性的 getter/setter
    proto.getOnAfterNavClick = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterNavClick : null;
    };
    
    proto.setOnAfterNavClick = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterNavClick = value;
            if (profile.boxing().onAfterNavClick && typeof profile.boxing().onAfterNavClick.action === 'function') {
                profile.boxing().onAfterNavClick.action.call(profile, value);
            }
        });
    };
})();
