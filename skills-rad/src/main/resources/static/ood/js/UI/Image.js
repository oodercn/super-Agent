ood.Class("ood.UI.Image", "ood.UI", {

    Initialize: function () {
        var ns = this;
        ns._adjustItems = ood.absList._adjustItems;
        ns.prototype._prepareItems = function (a) {
            return a;
        };
    },
    Instance: {
        iniProp: {
            src:  "ri-image-line",
            IconFontSize:'3em'
        },
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        },
        getRate: function () {
            return parseFloat(this.get(0)._rate) || 1;
        },

        getUIValue: function () {

            return this.getSrc();
        },
        setUIValue: function (src) {
            this.setSrc(src);
        },
        getValue: function () {

            return this.getSrc();
        },
        setValue: function (src) {
            this.setSrc(src);
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 移除所有主题类
                root.removeClass('image-dark image-highcontrast');
                
                // 应用主题类
                if (theme === 'dark') {
                    root.addClass('image-dark');
                } else if (theme === 'highcontrast') {
                    root.addClass('image-highcontrast');
                }
                
                // 确保基础样式类存在
                root.addClass('ood-image');
                
                // 保存主题设置
                localStorage.setItem('image-theme', theme);
                
                // 触发主题变化事件
                if (profile.onThemeChange) {
                    profile.onThemeChange(theme);
                }
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('image-theme') || 'light';
        },
        
        // 响应式断点配置
        setResponsiveBreakpoint: function(breakpoint) {
            return this.each(function(profile) {
                profile.properties.responsiveBreakpoint = breakpoint;
                profile.boxing().adjustLayout();
            });
        },
        
        // 启用键盘导航
        enableKeyboardNavigation: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                root.attr('tabindex', '0');
                
                // 添加键盘事件监听
                root.on('keydown', function(e) {
                    if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault();
                        profile.boxing().fireClickEvent();
                    }
                });
            });
        },
        
        // 设置屏幕阅读器标签
        setScreenReaderLabel: function(label) {
            return this.each(function(profile) {
                profile.properties.ariaLabel = label;
                profile.getRoot().attr('aria-label', label);
            });
        },

        ImageTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 保存原始值供响应式调整使用
            if (!profile.properties._originalMaxWidth) {
                profile.properties._originalMaxWidth = prop.maxWidth;
            }
            if (!profile.properties._originalMaxHeight) {
                profile.properties._originalMaxHeight = prop.maxHeight;
            }

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('image-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                boxing.adjustLayout();
            }

            // 初始化可访问性
            boxing.enhanceAccessibility();
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('image-mobile');
                    
                    // 移动端调整最大宽度
                    var currentMaxWidth = prop.maxWidth || 800;
                    var newMaxWidth = Math.min(currentMaxWidth, width - 40);
                    
                    if (newMaxWidth !== prop.maxWidth) {
                        profile.properties.maxWidth = newMaxWidth;
                        profile.box._adjust(profile, prop.width, prop.height);
                    }
                } else {
                    root.removeClass('image-mobile');
                    
                    // 恢复桌面样式
                    if (profile.properties._originalMaxWidth) {
                        profile.properties.maxWidth = profile.properties._originalMaxWidth;
                        profile.box._adjust(profile, prop.width, prop.height);
                    }
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('image-tiny');
                    
                    // 限制最大高度
                    var maxMobileHeight = Math.min(prop.maxHeight || 600, width * 0.75);
                    if (maxMobileHeight !== prop.maxHeight) {
                        profile.properties.maxHeight = maxMobileHeight;
                        profile.box._adjust(profile, prop.width, prop.height);
                    }
                } else {
                    root.removeClass('image-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    properties = profile.properties;

                // 为图片添加ARIA属性
                root.attr({
                    'role': 'img',
                    'alt': properties.alt || '图片',
                    'aria-label': properties.alt || properties.tips || '图片'
                });
                
                // 如果没有alt属性，添加默认值
                if (!properties.alt || properties.alt === '') {
                    root.attr('alt', '图片');
                }
                
                // 支持键盘导航
                if (!properties.disabled) {
                    root.attr({
                        'tabindex': '0',
                        'aria-describedby': properties.tips ? profile.serialId + '_tips' : null
                    });
                }
            });
        }
    },
    Static: {
        IMGNODE: 1,
        Templates: {
            tagName: 'img',
            style: 'cursor:{cursor};{_style}',
            className: '{_className}',
            border: "0",
            src: ood.ini.img_bg,
            alt: "{alt}"
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY'},
            ClickEffected: {KEY: 'KEY'},
            DraggableKeys: ["KEY"],
            onError: function (profile, e, src) {
                profile.boxing().onError(profile);
            },
            onLoad: function (profile, e, src) {
                var img = ood.use(src).get(0), path;
                // for IE8 bug
                try {
                    path = img.src;
                } catch (e) {
                    return;
                }
                if (path != ood.ini.img_bg) {
                    var i = new Image();
                    i.onload = function () {
                        if (!profile || profile.destroyed) return;
                        var prop = profile.properties,
                            size = profile.box._adjust(profile, (prop.width === "" || prop.width == "auto") ? i.width : prop.width, (prop.height === "" || prop.height == "auto") ? i.height : prop.height);
                        if (profile.$afterLoad) profile.$afterLoad.apply(profile.host, [profile, path, size[0], size[1]]);
                        profile.boxing().afterLoad(profile, path, size[0], size[1]);
                        if (prop.dock != 'none')
                            profile.boxing().adjustDock();
                        i.onload = null;
                    }
                    // must after onload for IE<8 fix
                    i.src = path;
                    ood.Dom.fixPng(img);
                }
            },
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            },
            onDblclick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onDblclick)
                    profile.boxing().onDblclick(profile, e, src);
            }
        },
        RenderTrigger: function () {
            var self = this, pro = self.properties,
                v = pro.src, v2 = pro.activeItem;
            if (v2 && -1 != ood.arr.subIndexOf(pro.items, "id", v2)) {
                self.boxing().setActiveItem(v2, true);
            } else if (v) self.boxing().setSrc(v, v != ood.ini.img_bg);
            
            // 现代化功能初始化
            ood.asyRun(function(){
                self.boxing().ImageTrigger();
            });
        },
        

        EventHandlers: {
            onClick: function (profile, e, src) {
            },
            onDblclick: function (profile, e, src) {
            },
            onError: function (profile) {
            },
            beforeLoad: function (profile) {
            },
            afterLoad: function (profile, path, width, height) {
            }
        },
        _adjust: function (profile, width, height) {
            var prop = profile.properties,
                src = profile.getRootNode(),
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                };

            width = width ? profile.$px(width, null, true) : width;
            height = height ? profile.$px(height, null, true) : height;

            src.style.width = src.style.height = '';
            if (width > 0 && height > 0) {
                var r1 = prop.maxWidth / width,
                    r2 = prop.maxHeight / height,
                    r = r1 < r2 ? r1 : r2;
                if (r >= 1) r = 1;
                profile._rate = r;
                return [src.width = width * r, src.height = height * r];
            }
            return [0, 0];
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'highcontrast'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            responsiveBreakpoint: {
                ini: 'md',
                listbox: ['sm', 'md', 'lg', 'xl'],
                action: function(value) {
                    this.boxing().adjustLayout();
                }
            },
            ariaLabel: {
                ini: '',
                action: function(value) {
                    this.getRoot().attr('aria-label', value);
                }
            },
            tabIndex: {
                ini: '0',
                action: function(value) {
                    this.getRoot().attr('tabindex', value);
                }
            },
            
            expression: {
                ini: '',
                action: function () {
                }
            },
            maxWidth: {
                ini: 800,
                action: function (v) {
                    var src = this.getRootNode(), prop = this.properties;
                    this.box._adjust(this, (prop.width === "" || prop.width == "auto") ? src.width : prop.width, (prop.height === "" || prop.height == "auto") ? src.height : prop.height);
                }
            },
            maxHeight: {
                ini: 600,
                action: function (v) {
                    var src = this.getRootNode(), prop = this.properties;
                    this.box._adjust(this, (prop.width === "" || prop.width == "auto") ? src.width : prop.width, (prop.height === "" || prop.height == "auto") ? src.height : prop.height);
                }
            },
            width: {
                ini: 'auto',
                action: function (v) {
                    var src = this.getRootNode(),
                        prop = this.properties,
                        i = new Image();
                    i.src = src.src;
                    this.box._adjust(this, (v === "" || v == "auto") ? i.width : v, (prop.height === "" || prop.height == "auto") ? i.height : prop.height);
                }
            },
            height: {
                ini: 'auto',
                action: function (v) {
                    var src = this.getRootNode(),
                        prop = this.properties,
                        i = new Image();
                    i.src = src.src;
                    this.box._adjust(this, (prop.width === "" || prop.width == "auto") ? i.width : prop.width, (v === "" || v == "auto") ? i.height : v);
                }
            },
            src: {
                format: 'image',
                ini: ood.ini.img_bg,
                linkage: ["activeItem"],
                //use asyn mode
                action: function (v) {
                    var self = this;
                    if (false !== self.boxing().beforeLoad(this))
                        ood.asyRun(function () {
                            var p = self.properties, r = self.getRoot(), src = ood.adjustRes(v);
                            if (p) {
                                if (r.attr('src') !== src) r.attr('src', src);
                            }

                        });
                    if (!self.$inner)
                        self.properties.activeItem = "";
                }
            },
            alt: {
                ini: "",
                action: function (v) {
                    this.getRoot().attr('alt', v);
                }
            },
            items: {
                ini: []
            },
            activeItem: {
                ini: "",
                linkage: ["src", "alt", "tips"],
                action: function (v) {
                    var items = this.properties.items,
                        i = ood.arr.subIndexOf(items, "id", "" + v),
                        item, ins = this.boxing(),
                        src = ood.ini.img_bg, alt, tips;
                    if ((i != -1) && (item = items[i])) {
                        src = item.image;
                        alt = item.alt || "";
                        tips = item.tips || "";
                    }
                    this.$inner = 1;
                    ins.setSrc(src, true);
                    delete this.$inner;
                    ins.setAlt(alt || "");
                    ins.setTips(tips || "");
                }
            },
            cursor: {
                ini: "auto",
                combobox: ["", "default", "text", "pointer", "move", "crosshair", "wait", "help", "e-resize", "ne-resize", "nw-resize", "n-resize", "se-resize", "sw-resize", "s-resize", "w-resize"],
                action: function (v) {
                    this.getRoot().css('cursor', v);
                }
            }
        }
    }
});