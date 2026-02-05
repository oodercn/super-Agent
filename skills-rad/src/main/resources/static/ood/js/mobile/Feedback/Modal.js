/**
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
}