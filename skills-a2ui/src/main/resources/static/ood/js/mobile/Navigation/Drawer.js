/**
 * 移动端抽屉组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持左右滑出、遮罩层、手势控制等功能
 */
ood.Class("ood.Mobile.Drawer", ["ood.UI.Widget", "ood.absList"], {
    Instance: {
        Initialize: function() {
           // this.constructor.upper.prototype.Initialize.call(this);
            this.initDrawerFeatures();
        },
        
        initDrawerFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-drawer');
            this.initDrawerState();
            this.initSwipeGesture();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var closeBtn = profile.getSubNode('CLOSE');
            
            // 遮罩点击关闭
            overlay.on('click', function(e) {
                if (e.target === overlay.get(0)) {
                    self.close();
                }
            });
            
            // 关闭按钮
            if (closeBtn && !closeBtn.isEmpty()) {
                closeBtn.on('click', function(e) {
                    self.close();
                });
            }
        },
        
        initDrawerState: function() {
            this._isOpen = false;
            this._isAnimating = false;
        },
        
        initSwipeGesture: function() {
            var self = this;
            var profile = this.get(0);
            var props = profile.properties;
            
            if (!props.swipeToOpen) return;
            
            var startX, startY, currentX;
            var isSwipping = false;
            var threshold = 50;
            
            // 监听整个文档的触摸事件
            ood(document).on('touchstart', function(e) {
                var touch = e.touches[0];
                startX = touch.clientX;
                startY = touch.clientY;
                isSwipping = false;
                
                // 判断是否在边缘区域开始滑动
                var edgeThreshold = 20;
                if (props.placement === 'left' && startX < edgeThreshold) {
                    isSwipping = true;
                } else if (props.placement === 'right' && startX > window.innerWidth - edgeThreshold) {
                    isSwipping = true;
                }
            });
            
            ood(document).on('touchmove', function(e) {
                if (!isSwipping) return;
                
                var touch = e.touches[0];
                currentX = touch.clientX;
                var deltaX = currentX - startX;
                var deltaY = touch.clientY - startY;
                
                // 判断是否为水平滑动
                if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 10) {
                    e.preventDefault();
                    
                    // 根据滑动方向判断是否应该打开抽屉
                    if ((props.placement === 'left' && deltaX > threshold) ||
                        (props.placement === 'right' && deltaX < -threshold)) {
                        self.open();
                        isSwipping = false;
                    }
                }
            });
            
            ood(document).on('touchend', function(e) {
                isSwipping = false;
            });
        },
        
        open: function() {
            if (this._isOpen || this._isAnimating) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var drawer = profile.getSubNode('DRAWER');
            
            this._isAnimating = true;
            
            // 显示遮罩
            overlay.css('display', 'flex');
            
            // 添加打开的类
            setTimeout(function() {
                overlay.addClass('ood-mobile-drawer-overlay-show');
                drawer.addClass('ood-mobile-drawer-show');
            }, 10);
            
            // 动画完成后更新状态
            var self = this;
            setTimeout(function() {
                self._isOpen = true;
                self._isAnimating = false;
                self.onOpen();
            }, 300);
            
            // 阻止页面滚动
            ood('body').addClass('ood-mobile-drawer-open');
        },
        
        close: function() {
            if (!this._isOpen || this._isAnimating) return;
            
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var drawer = profile.getSubNode('DRAWER');
            
            this._isAnimating = true;
            
            // 移除打开的类
            overlay.removeClass('ood-mobile-drawer-overlay-show');
            drawer.removeClass('ood-mobile-drawer-show');
            
            // 动画完成后隐藏
            var self = this;
            setTimeout(function() {
                overlay.css('display', 'none');
                self._isOpen = false;
                self._isAnimating = false;
                self.onClose();
            }, 300);
            
            // 恢复页面滚动
            ood('body').removeClass('ood-mobile-drawer-open');
        },
        
        toggle: function() {
            if (this._isOpen) {
                this.close();
            } else {
                this.open();
            }
        },
        
        isOpen: function() {
            return this._isOpen;
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
        
        onOpen: function() {
            var profile = this.get(0);
            
            if (profile.onOpen) {
                profile.boxing().onOpen(profile);
            }
        },
        
        onClose: function() {
            var profile = this.get(0);
            
            if (profile.onClose) {
                profile.boxing().onClose(profile);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-drawer',
            style: '{_style}',
            
            OVERLAY: {
                tagName: 'div',
                className: 'ood-mobile-drawer-overlay',
                style: 'display: none',
                
                DRAWER: {
                    tagName: 'div',
                    className: 'ood-mobile-drawer-panel ood-mobile-drawer-{placement}',
                    
                    HEADER: {
                        tagName: 'div',
                        className: 'ood-mobile-drawer-header',
                        style: 'display: {_headerDisplay}',
                        
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-mobile-drawer-title',
                            text: '{title}'
                        },
                        
                        CLOSE: {
                            tagName: 'button',
                            className: 'ood-mobile-drawer-close',
                            type: 'button',
                            text: '×'
                        }
                    },
                    
                    CONTENT: {
                        tagName: 'div',
                        className: 'ood-mobile-drawer-content'
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
                'z-index': 1000,
                opacity: 0,
                transition: 'opacity 0.3s ease'
            },
            
            'OVERLAY.ood-mobile-drawer-overlay-show': {
                opacity: 1
            },
            
            DRAWER: {
                position: 'absolute',
                top: 0,
                height: '100%',
                'background-color': 'var(--mobile-bg-primary)',
                'box-shadow': 'var(--mobile-shadow-heavy)',
                transition: 'transform 0.3s ease',
                'max-width': '80%',
                'min-width': '250px'
            },
            
            'DRAWER.ood-mobile-drawer-left': {
                left: 0,
                transform: 'translateX(-100%)'
            },
            
            'DRAWER.ood-mobile-drawer-right': {
                right: 0,
                transform: 'translateX(100%)'
            },
            
            'DRAWER.ood-mobile-drawer-left.ood-mobile-drawer-show': {
                transform: 'translateX(0)'
            },
            
            'DRAWER.ood-mobile-drawer-right.ood-mobile-drawer-show': {
                transform: 'translateX(0)'
            },
            
            HEADER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'min-height': '60px'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)'
            },
            
            CLOSE: {
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
                padding: 'var(--mobile-spacing-md)',
                height: 'calc(100% - 60px)',
                'overflow-y': 'auto'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '抽屉标题',
                ini: '抽屉',
                action: function(value) {
                    var profile = this;
                    // 同步更新title属性
                    profile.properties.title = value;
                    var titleNode = profile.getSubNode('TITLE');
                    if (titleNode && !titleNode.isEmpty()) {
                        titleNode.html(value || '');
                    }
                    profile.getRoot().attr('aria-label', value || '抽屉');
                }
            },
            
            width: {
                caption: '抽屉宽度',
                $spaceunit: 1,
                ini: '280px'
            },
            
            height: {
                caption: '抽屉高度',
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
                    var drawerNode = this.getSubNode('DRAWER');
                    if (value && drawerNode && !drawerNode.isEmpty()) {
                        drawerNode.css('background-color', value);
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
            
            // ===== 抽屉特有属性 =====
            placement: {
                caption: '抽屉位置',
                ini: 'left',
                listbox: ['left', 'right']
            },
            
            title: {
                caption: '抽屉标题',
                ini: ''
            },
            
            swipeToOpen: {
                ini: true
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
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            data.placement = props.placement || 'left';
            data._headerDisplay = props.title ? 'flex' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onOpen: function(profile) {
                // 抽屉打开事件处理器
            },
            
            onClose: function(profile) {
                // 抽屉关闭事件处理器
            }
        }
    }
});

// 添加全局样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .ood-mobile-drawer-open {
            overflow: hidden !important;
        }
    `;
    document.head.appendChild(style);
}

// 添加Getter/Setter方法
(function() {
    var proto = ood.Mobile.Drawer.prototype;
    
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
    
    // placement 属性的 getter/setter
    proto.getPlacement = function() {
        var profile = this.get(0);
        return profile ? profile.properties.placement : 'left';
    };
    
    proto.setPlacement = function(value) {
        return this.each(function(profile) {
            profile.properties.placement = value;
            if (profile.boxing().placement && typeof profile.boxing().placement.action === 'function') {
                profile.boxing().placement.action.call(profile, value);
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
    
    // swipeToOpen 属性的 getter/setter
    proto.getSwipeToOpen = function() {
        var profile = this.get(0);
        return profile ? profile.properties.swipeToOpen : true;
    };
    
    proto.setSwipeToOpen = function(value) {
        return this.each(function(profile) {
            profile.properties.swipeToOpen = value;
            if (profile.boxing().swipeToOpen && typeof profile.boxing().swipeToOpen.action === 'function') {
                profile.boxing().swipeToOpen.action.call(profile, value);
            }
        });
    };
    
    // maskClosable 属性的 getter/setter
    proto.getMaskClosable = function() {
        var profile = this.get(0);
        return profile ? profile.properties.maskClosable : true;
    };
    
    proto.setMaskClosable = function(value) {
        return this.each(function(profile) {
            profile.properties.maskClosable = value;
            if (profile.boxing().maskClosable && typeof profile.boxing().maskClosable.action === 'function') {
                profile.boxing().maskClosable.action.call(profile, value);
            }
        });
    };
})();
