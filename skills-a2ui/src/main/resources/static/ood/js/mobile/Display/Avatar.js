/**
 * 移动端头像组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持图片头像、文字头像、图标头像、在线状态等功能
 */
ood.Class("ood.Mobile.Avatar", ["ood.UI"], {
    Instance: {
        Initialize: function() {
          //  this.constructor.upper.prototype.Initialize.call(this);
            this.initAvatarFeatures();
        },
        
        initAvatarFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-avatar');
            this.updateAvatarDisplay();
            this.updateAvatarSize();
            this.updateAvatarShape();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 头像点击事件
            root.on('click', function(e) {
                self.onAvatarClick(e);
            });
            
            // 图片加载错误处理
            var img = profile.getSubNode('IMAGE');
            img.on('error', function(e) {
                self.onImageError(e);
            });
            
            img.on('load', function(e) {
                self.onImageLoad(e);
            });
        },
        
        updateAvatarDisplay: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            // 根据优先级显示内容：图片 > 图标 > 文字
            if (props.src) {
                this.showImage();
            } else if (props.icon) {
                this.showIcon();
            } else if (props.text) {
                this.showText();
            } else {
                this.showDefault();
            }
            
            // 更新在线状态
            this.updateOnlineStatus();
        },
        
        showImage: function() {
            var profile = this.get(0);
            var imageNode = profile.getSubNode('IMAGE');
            var iconNode = profile.getSubNode('ICON');
            var textNode = profile.getSubNode('TEXT');
            
            imageNode.css('display', 'block');
            iconNode.css('display', 'none');
            textNode.css('display', 'none');
            
            imageNode.attr('src', profile.properties.src);
        },
        
        showIcon: function() {
            var profile = this.get(0);
            var imageNode = profile.getSubNode('IMAGE');
            var iconNode = profile.getSubNode('ICON');
            var textNode = profile.getSubNode('TEXT');
            
            imageNode.css('display', 'none');
            iconNode.css('display', 'flex');
            textNode.css('display', 'none');
            
            iconNode.attr('class', 'ood-mobile-avatar-icon ' + profile.properties.icon);
        },
        
        showText: function() {
            var profile = this.get(0);
            var imageNode = profile.getSubNode('IMAGE');
            var iconNode = profile.getSubNode('ICON');
            var textNode = profile.getSubNode('TEXT');
            
            imageNode.css('display', 'none');
            iconNode.css('display', 'none');
            textNode.css('display', 'flex');
            
            // 显示文字（通常取姓名的首字符）
            var text = profile.properties.text;
            var displayText = this.getDisplayText(text);
            textNode.html(displayText);
        },
        
        showDefault: function() {
            var profile = this.get(0);
            var imageNode = profile.getSubNode('IMAGE');
            var iconNode = profile.getSubNode('ICON');
            var textNode = profile.getSubNode('TEXT');
            
            imageNode.css('display', 'none');
            iconNode.css('display', 'flex');
            textNode.css('display', 'none');
            
            // 显示默认图标
            iconNode.attr('class', 'ood-mobile-avatar-icon ood-mobile-avatar-default');
            iconNode.html('👤');
        },
        
        getDisplayText: function(text) {
            if (!text) return '';
            
            // 如果是中文名，取最后一个字符
            if (/[\u4e00-\u9fa5]/.test(text)) {
                return text.charAt(text.length - 1);
            }
            
            // 如果是英文名，取首字母
            var words = text.split(' ');
            if (words.length >= 2) {
                return (words[0].charAt(0) + words[1].charAt(0)).toUpperCase();
            } else {
                return text.charAt(0).toUpperCase();
            }
        },
        
        updateOnlineStatus: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var statusNode = profile.getSubNode('STATUS');
            
            if (props.online !== null && props.online !== undefined) {
                statusNode.css('display', 'block');
                
                if (props.online) {
                    statusNode.removeClass('ood-mobile-avatar-status-offline');
                    statusNode.addClass('ood-mobile-avatar-status-online');
                } else {
                    statusNode.removeClass('ood-mobile-avatar-status-online');
                    statusNode.addClass('ood-mobile-avatar-status-offline');
                }
            } else {
                statusNode.css('display', 'none');
            }
        },
        
        updateAvatarSize: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 移除所有尺寸类
            root.removeClass('ood-mobile-avatar-xs ood-mobile-avatar-sm ood-mobile-avatar-lg ood-mobile-avatar-xl');
            
            // 添加新尺寸类
            if (props.size && props.size !== 'md') {
                root.addClass('ood-mobile-avatar-' + props.size);
            }
        },
        
        updateAvatarShape: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 移除所有形状类
            root.removeClass('ood-mobile-avatar-square');
            
            // 添加新形状类
            if (props.shape === 'square') {
                root.addClass('ood-mobile-avatar-square');
            }
        },
        
        setSrc: function(src) {
            var profile = this.get(0);
            profile.properties.src = src;
            this.updateAvatarDisplay();
        },
        
        setText: function(text) {
            var profile = this.get(0);
            profile.properties.text = text;
            this.updateAvatarDisplay();
        },
        
        setIcon: function(icon) {
            var profile = this.get(0);
            profile.properties.icon = icon;
            this.updateAvatarDisplay();
        },
        
        setOnline: function(online) {
            var profile = this.get(0);
            profile.properties.online = online;
            this.updateOnlineStatus();
        },
        
        setSize: function(size) {
            var profile = this.get(0);
            profile.properties.size = size;
            this.updateAvatarSize();
        },
        
        setShape: function(shape) {
            var profile = this.get(0);
            profile.properties.shape = shape;
            this.updateAvatarShape();
        },
        
        onImageError: function(e) {
            // 图片加载失败，显示文字或图标
            this.showText();
            
            var profile = this.get(0);
            if (profile.onImageError) {
                profile.boxing().onImageError(profile, e);
            }
        },
        
        onImageLoad: function(e) {
            var profile = this.get(0);
            
            if (profile.onImageLoad) {
                profile.boxing().onImageLoad(profile, e);
            }
        },
        
        onAvatarClick: function(e) {
            var profile = this.get(0);
            
            if (profile.onAvatarClick) {
                profile.boxing().onAvatarClick(profile, e);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-avatar ood-mobile-avatar-{size} {_shapeClass}',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-avatar-container',
                
                IMAGE: {
                    tagName: 'img',
                    className: 'ood-mobile-avatar-image',
                    src: '{src}',
                    alt: '{alt}',
                    style: 'display: {_imageDisplay}'
                },
                
                ICON: {
                    tagName: 'i',
                    className: 'ood-mobile-avatar-icon {icon}',
                    style: 'display: {_iconDisplay}'
                },
                
                TEXT: {
                    tagName: 'div',
                    className: 'ood-mobile-avatar-text',
                    style: 'display: {_textDisplay}'
                }
            },
            
            STATUS: {
                tagName: 'div',
                className: 'ood-mobile-avatar-status',
                style: 'display: {_statusDisplay}'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                display: 'inline-block',
                'background-color': 'var(--mobile-bg-secondary)',
                'border-radius': '50%',
                overflow: 'hidden',
                'user-select': 'none',
                cursor: 'pointer',
                transition: 'all 0.2s ease'
            },
            
            'KEY:hover': {
                transform: 'scale(1.05)'
            },
            
            'KEY.ood-mobile-avatar-square': {
                'border-radius': 'var(--mobile-border-radius)'
            },
            
            // 尺寸样式
            'KEY, KEY.ood-mobile-avatar-md': {
                width: '40px',
                height: '40px'
            },
            
            'KEY.ood-mobile-avatar-xs': {
                width: '24px',
                height: '24px'
            },
            
            'KEY.ood-mobile-avatar-sm': {
                width: '32px',
                height: '32px'
            },
            
            'KEY.ood-mobile-avatar-lg': {
                width: '56px',
                height: '56px'
            },
            
            'KEY.ood-mobile-avatar-xl': {
                width: '80px',
                height: '80px'
            },
            
            CONTAINER: {
                position: 'relative',
                width: '100%',
                height: '100%',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            IMAGE: {
                width: '100%',
                height: '100%',
                'object-fit': 'cover'
            },
            
            ICON: {
                'font-size': '50%',
                color: 'var(--mobile-text-tertiary)',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            'ICON.ood-mobile-avatar-default': {
                'font-size': '60%'
            },
            
            TEXT: {
                'font-size': '50%',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            // 不同尺寸的字体大小调整
            'KEY.ood-mobile-avatar-xs ICON, KEY.ood-mobile-avatar-xs TEXT': {
                'font-size': '40%'
            },
            
            'KEY.ood-mobile-avatar-sm ICON, KEY.ood-mobile-avatar-sm TEXT': {
                'font-size': '45%'
            },
            
            'KEY.ood-mobile-avatar-lg ICON, KEY.ood-mobile-avatar-lg TEXT': {
                'font-size': '55%'
            },
            
            'KEY.ood-mobile-avatar-xl ICON, KEY.ood-mobile-avatar-xl TEXT': {
                'font-size': '60%'
            },
            
            STATUS: {
                position: 'absolute',
                bottom: 0,
                right: 0,
                width: '12px',
                height: '12px',
                'border-radius': '50%',
                border: '2px solid var(--mobile-bg-primary)',
                'box-sizing': 'border-box'
            },
            
            'STATUS.ood-mobile-avatar-status-online': {
                'background-color': 'var(--mobile-success)'
            },
            
            'STATUS.ood-mobile-avatar-status-offline': {
                'background-color': 'var(--mobile-text-quaternary)'
            },
            
            // 不同尺寸的状态点大小调整
            'KEY.ood-mobile-avatar-xs STATUS': {
                width: '8px',
                height: '8px',
                'border-width': '1px'
            },
            
            'KEY.ood-mobile-avatar-sm STATUS': {
                width: '10px',
                height: '10px',
                'border-width': '1px'
            },
            
            'KEY.ood-mobile-avatar-lg STATUS': {
                width: '14px',
                height: '14px',
                'border-width': '2px'
            },
            
            'KEY.ood-mobile-avatar-xl STATUS': {
                width: '16px',
                height: '16px',
                'border-width': '2px'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '头像标题',
                ini: '头像',
                action: function(value) {
                    var profile = this;
                    // 更新alt属性保持同步
                    profile.properties.alt = value;
                    profile.getRoot().attr('aria-label', value || '头像');
                    var imageNode = profile.getSubNode('IMAGE');
                    if (imageNode && !imageNode.isEmpty()) {
                        imageNode.attr('alt', value || '头像');
                    }
                }
            },
            
            width: {
                caption: '头像宽度',
                $spaceunit: 1,
                ini: '40px'
            },
            
            height: {
                caption: '头像高度',
                $spaceunit: 1,
                ini: '40px'
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
            
            textColor: {
                caption: '文字颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var textNode = this.getSubNode('TEXT');
                    if (value && textNode && !textNode.isEmpty()) {
                        textNode.css('color', value);
                    }
                }
            },
            
            // ===== 头像特有属性 =====
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
            
            src: {
                caption: '头像图片地址',
                ini: '',
                action: function(value) {
                    this.boxing().setSrc(value);
                }
            },
            
            text: {
                caption: '文字头像',
                ini: '',
                action: function(value) {
                    this.boxing().setText(value);
                }
            },
            
            icon: {
                caption: '图标头像',
                ini: '',
                action: function(value) {
                    this.boxing().setIcon(value);
                }
            },
            
            alt: {
                caption: '图片替代文字',
                ini: 'avatar'
            },
            
            size: {
                caption: '头像尺寸',
                ini: 'md',
                listbox: ['xs', 'sm', 'md', 'lg', 'xl'],
                action: function(value) {
                    this.boxing().setSize(value);
                }
            },
            
            shape: {
                caption: '头像形状',
                ini: 'circle',
                listbox: ['circle', 'square'],
                action: function(value) {
                    this.boxing().setShape(value);
                }
            },
            
            online: {
                caption: '在线状态',
                ini: null,
                action: function(value) {
                    this.boxing().setOnline(value);
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
            // Avatar组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                imageNode = profile.getSubNode('IMAGE'),
                iconNode = profile.getSubNode('ICON'),
                textNode = profile.getSubNode('TEXT'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整头像容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    
                    // 同时调整内部元素的大小
                    imageNode.css('width', '100%');
                    iconNode.css('width', '100%');
                    textNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整头像容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    
                    // 同时调整内部元素的大小
                    imageNode.css('height', '100%');
                    iconNode.css('height', '100%');
                    textNode.css('height', '100%');
                }
            }

            // 根据新的尺寸更新头像大小类
            if (width || height) {
                this.boxing().updateAvatarSize();
            }
        },

        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            data._shapeClass = props.shape === 'square' ? 'ood-mobile-avatar-square' : '';
            
            // 根据优先级设置显示状态
            if (props.src) {
                data._imageDisplay = 'block';
                data._iconDisplay = 'none';
                data._textDisplay = 'none';
            } else if (props.icon) {
                data._imageDisplay = 'none';
                data._iconDisplay = 'flex';
                data._textDisplay = 'none';
            } else if (props.text) {
                data._imageDisplay = 'none';
                data._iconDisplay = 'none';
                data._textDisplay = 'flex';
            } else {
                data._imageDisplay = 'none';
                data._iconDisplay = 'flex';
                data._textDisplay = 'none';
            }
            
            data._statusDisplay = (props.online !== null && props.online !== undefined) ? 'block' : 'none';
            
            return data;
        },
        
        EventHandlers: {
            onAvatarClick: function(profile, event) {
                // 头像点击事件处理器
            },
            
            onImageError: function(profile, event) {
                // 图片加载错误事件处理器
            },
            
            onImageLoad: function(profile, event) {
                // 图片加载完成事件处理器
            }
        }
    }
});