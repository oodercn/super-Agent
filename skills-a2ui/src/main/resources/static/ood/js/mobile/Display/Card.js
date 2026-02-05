/**
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
});