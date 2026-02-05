/**
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
});