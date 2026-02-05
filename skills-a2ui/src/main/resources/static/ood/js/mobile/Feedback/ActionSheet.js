/**
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
}