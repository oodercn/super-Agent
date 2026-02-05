ood.Class("ood.UI.Label", "ood.UI", {
    iniProp: {
        caption:  "文本标签"
    },
    Initialize: function () {
        // compitable
        ood.UI.SLabel = ood.UI.Label;
        var key = "ood.UI.SLabel";
        ood.absBox.$type[key.replace("ood.UI.", "")] = ood.absBox.$type[key] = key;
    },
    Instance: {
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        },
        // calculate the formula, and apply to the control
        _applyExcelFormula: function (cellsMap) {
            var profile = this.get(0), prop = profile.properties, f, value;
            if (f = prop.excelCellFormula) {
                value = ood.ExcelFormula.calculate(f, cellsMap);
                if (ood.isSet(value)) {
                    if (profile.beforeApplyExcelFormula && false === profile.beforeApplyExcelFormula(profile, prop.excelCellFormula, value)) {
                    } else {
                        this.setCaption(value, true);
                        if (profile.afterApplyExcelFormula) profile.afterApplyExcelFormula(profile, prop.excelCellFormula, value);
                    }
                }
            }
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    caption = profile.getSubNode('CAPTION'),
                    icon = profile.getSubNode('ICON');

                if (theme === 'dark') {
                    // 暗黑模式样式
                    root.addClass('label-dark');
                    caption.css({
                        'color': 'var(--ood-text-muted)'
                    });
                    icon.css({
                        'color': '#cccccc'
                    });
                } else {
                    // 浅色模式样式
                    root.removeClass('label-dark');
                    caption.css({
                        'color': ''
                    });
                    icon.css({
                        'color': ''
                    });
                }
                
                // 保存主题设置
                localStorage.setItem('label-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('label-theme') || 'light';
        },

        LabelTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('label-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                this.adjustLayout();
            }

            // 初始化可访问性
            this.enhanceAccessibility();
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
                    caption = profile.getSubNode('CAPTION'),
                    icon = profile.getSubNode('ICON'),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('label-mobile');
                    
                    // 移动端调整
                    caption.css({
                        'font-size': '0.9em'
                    });
                    if (icon && !icon.isEmpty()) {
                        icon.css({
                            'font-size': '1.1em'
                        });
                    }
                } else {
                    root.removeClass('label-mobile');
                    
                    // 恢复桌面样式
                    caption.css({
                        'font-size': ''
                    });
                    if (icon && !icon.isEmpty()) {
                        icon.css({
                            'font-size': ''
                        });
                    }
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('label-tiny');
                    
                    // 优化小屏幕显示
                    caption.css({
                        'font-size': '0.8em'
                    });
                } else {
                    root.removeClass('label-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    caption = profile.getSubNode('CAPTION'),
                    icon = profile.getSubNode('ICON'),
                    properties = profile.properties;

                // 为标签添加ARIA属性
                root.attr({
                    'role': 'text',
                    'aria-label': properties.caption || '标签'
                });
                
                // 为图标添加ARIA属性
                if (icon && !icon.isEmpty()) {
                    icon.attr({
                        'aria-hidden': 'true' // 隐藏装饰性图标
                    });
                }

                // 如果是时钟模式，添加适当的标签
                if (properties.clock) {
                    root.attr({
                        'role': 'timer',
                        'aria-label': '时钟显示',
                        'aria-live': 'polite'
                    });
                }
            });
        }
    },
    Static: {
        Templates: {
            tagName: "label",
            className: '{_className}',
            style: '{_hAlign};{_style}',
            VALIGN: {
                $order: 0,
                style: '{_vAlign}'
            },
            ICON: {
                $order: 1,
                className: 'oodcon {imageClass} {picClass}',
                style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{_fc}{imageDisplay}{iconStyle}',
                text: '{iconFontCode}',
                'color': 'var(--ood-text-muted)'
            },
            CAPTION: {
                $order: 2,
                text: '{caption}',
                style: '{_fc}{_fw}{_fs}{_ff}',
                'font-size': 'var(--ood-font-size-md)',
                'color': 'var(--ood-text)'
            }
        },
        Appearances: {
            VALIGN: {
                'font-size': 0,
                width: 0,
                display: 'inline-block',
                height: '100%'
            },
            
            // 暗黑模式样式
            'label-dark ICON': {
                'color': 'var(--ood-dark-text-muted) !important'
            },
            'label-dark CAPTION': {
                'color': 'var(--ood-dark-text) !important'
            },
            
            // 移动端样式
            'label-mobile CAPTION': {
                'font-size': 'var(--ood-font-size-sm)'
            },
            'label-mobile ICON': {
                'font-size': '1.1em !important'
            },
            
            // 小屏幕样式
            'label-tiny CAPTION': {
                'font-size': 'var(--ood-font-size-xs)'
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                caption: ood.getResText("DataModel.theme") || "主题",
                listbox: ['light', 'dark'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                caption: ood.getResText("DataModel.responsive") || "响应式",
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            selectable: {
                ini: true,
                caption: ood.getResText("DataModel.selectable") || "可选择"
            },
            expression: {
                ini: '',
                caption: ood.getResText("DataModel.expression") || "表达式",
                action: function () {
                }
            },
            caption: {
                ini: null,
                caption: ood.getResText("DataModel.title") || "标题",
                action: function (v) {
                    var prf = this;
                    if (!prf.properties.clock) {
                        v = (ood.isSet(v) ? v : "") + "";
                        prf.getSubNode("CAPTION").html(ood.adjustRes(v, true));
                    }
                }
            },
            clock: {
                ini: '',
                caption: ood.getResText("DataModel.clock") || "时钟",
                combobox: ['hh : mm : ss', 'hh - mm : ss'],
                action: function (v) {
                    var prf = this, timer;
                    if (v && !prf._timer) {
                        timer = prf._timer = new ood.Timer();
                        var f = timer.get(0).$onTime = function () {
                            if (!prf.destroyed)
                                prf.getSubNode("CAPTION").html(ood.Date.format(new Date, prf.properties.clock));
                        };
                        f();
                    } else if (!v && prf._timer) {
                        prf._timer.destroy();
                        prf._timer = null;
                        prf.boxing().setCaption(prf.properties.caption, true);
                    }
                }
            },
            image: {
                format: 'image',
                caption: ood.getResText("DataModel.image") || "图像",
                action: function (v) {
                    ood.UI.$iconAction(this);
                }
            },
            imagePos: {
                ini: '',
                caption: ood.getResText("DataModel.imagePosition") || "图像位置",
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundPosition', value || 'center');
                }
            },
            imageBgSize: {
                ini: '',
                caption: ood.getResText("DataModel.imageBgSize") || "图像背景大小",
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundSize', value || '');
                }
            },
            imageClass: {
                ini: '',
                caption: ood.getResText("DataModel.imageClass") || "图像类",
                action: function (v, ov) {
                    ood.UI.$iconAction(this, 'ICON', ov);
                }
            },
            iconFontCode: {
                ini: '',
                caption: ood.getResText("DataModel.iconFontCode") || "图标字体代码",
                action: function (v) {
                    ood.UI.$iconAction(this);
                }
            },
            iconFontColor: {
                ini: '',
                type: "color",
                caption: ood.getResText("DataModel.iconFontColor") || "图标字体颜色",
                action: function (value) {
                    this.getRoot().css('color', value);
                }
            },
            hAlign: {
                ini: 'right',
                caption: ood.getResText("DataModel.hAlign") || "水平对齐",
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getRoot().css('textAlign', v || '');
                }
            },
            vAlign: {
                ini: 'top',
                caption: ood.getResText("DataModel.vAlign") || "垂直对齐",
                listbox: ['top', 'middle', 'bottom'],
                action: function (v) {
                    this.getSubNode('VALIGN').css('verticalAlign', v || '');
                }
            },
            fontColor: {
                caption: ood.getResText("DataModel.fontColor") || "字体颜色",
                value: ood.UI.Button.$DataModel.fontColor
            },
            fontSize: {
                caption: ood.getResText("DataModel.fontSize") || "字体大小",
                value: ood.UI.Button.$DataModel.fontSize
            },
            fontWeight: {
                caption: ood.getResText("DataModel.fontWeight") || "字体粗细",
                value: ood.UI.Button.$DataModel.fontWeight
            },
            fontFamily: {
                caption: ood.getResText("DataModel.fontFamily") || "字体",
                value: ood.UI.Button.$DataModel.fontFamily
            },
            excelCellFormula: {
                ini: "",
                caption: ood.getResText("DataModel.excelCellFormula") || "Excel单元格公式",
                action: function (v) {
                    var prf = this, m,
                        prop = prf.properties;
                    if (v && ood.ExcelFormula.validate(v)) {
                        if (prf.host && (m = prf.host['ood.Module'])) {
                            m.applyExcelFormula(prf);
                        }
                    }
                }
            }
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY', ICON: 'ICON'},
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            }
        },
        EventHandlers: {
            onClick: function (profile, e, src) {
            },
            beforeApplyExcelFormula: function (profile, excelCellFormula, value) {
            },
            afterApplyExcelFormula: function (profile, excelCellFormula, value) {
            }
        },
        RenderTrigger: function () {
            var prf = this, t;
            (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["timerClear"] = function () {
                if (prf._timer) {
                    prf._timer.destroy();
                    delete prf._timer;
                }
            };
            if (t = prf.properties.clock) {
                prf.boxing().setClock(t, true);
            }
            
            // 现代化功能初始化
            var self = this;
            ood.asyRun(function(){
                self.boxing().LabelTrigger();
            });
        },
        

        _prepareData: function (profile, data) {
            data = arguments.callee.upper.call(this, profile, data);
            var v;
            if (data.caption) data.caption = ood.adjustVar(data.caption);
            if (data.clock) data.caption = '';
            if (v = data.iconFontColor) data._ic = 'color:var(--' + v + ');';
            if (v = data.fontSize) data._fs = 'font-size:' + v + ';';
            if (v = data.fontWeight) data._fw = 'font-weight:' + v + ';';
            if (v = data.fontColor) data._fc = 'color:var(--' + v + ');';
            if (v = data.fontFamily) data._ff = 'font-family:' + v + ';';
            data._hAlign = 'text-align:' + (data.hAlign || '');
            data._vAlign = 'vertical-align:' + (data.vAlign || '');
            return data;
        }
    }
});
