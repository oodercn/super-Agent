ood.Class("ood.UI.StatusButtons", ["ood.UI.List"], {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [{id: 'a', caption: 'status 1'}, {id: 'b', caption: 'status 2'}, {
                id: 'c',
                caption: 'status 3'
            }, {id: 'd', caption: 'status 4'}],
            borderType: 'none',
            itemMargin: '2px 4px',
            itemWidth: '4em',
            width: '30em',
            value: 'a'
        },

        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 移除所有主题类
                root.removeClass('statusbuttons-dark statusbuttons-hc');

                // 应用主题类名
                if (theme === 'dark') {
                    root.addClass('statusbuttons-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('statusbuttons-hc');
                } else {
                    root.removeClass('statusbuttons-dark statusbuttons-hc');
                }

                // 保存主题设置
                localStorage.setItem('statusbuttons-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('statusbuttons-theme') || 'light';
        },

        // 切换所有主题
        toggleTheme: function () {
            var current = this.getTheme();
            var next = current === 'light' ? 'dark' :
                current === 'dark' ? 'high-contrast' : 'light';
            this.setTheme(next);
            return this;
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('statusbuttons-theme') || 'light';
        },


        StatusButtonsTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('statusbuttons-theme');
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
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },

        // 响应式布局调整
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    items = profile.getSubNode('ITEM', true),
                    icons = profile.getSubNode('ICON', true),
                    captions = profile.getSubNode('CAPTION', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('statusbuttons-mobile');

                    // 移动端调整按钮大小
                    items.css({
                        'min-height': '2.2em',
                        'font-size': '0.9em',
                        'padding': '0.4em 0.6em'
                    });

                    // 移动端调整图标大小
                    icons.css({
                        'font-size': '1.1em'
                    });

                    // 移动端调整文字大小
                    captions.css({
                        'font-size': '0.9em'
                    });
                } else {
                    root.removeClass('statusbuttons-mobile');

                    // 恢复桌面样式
                    items.css({
                        'min-height': '',
                        'font-size': '',
                        'padding': ''
                    });

                    icons.css({
                        'font-size': ''
                    });

                    captions.css({
                        'font-size': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('statusbuttons-tiny');

                    // 超小屏幕隐藏部分文字，只显示图标
                    captions.each(function (caption) {
                        var captionElement = ood(caption);
                        var text = captionElement.text();
                        if (text && text.length > 4) {
                            captionElement.css('display', 'none');
                        }
                    });
                } else {
                    root.removeClass('statusbuttons-tiny');

                    // 恢复文字显示
                    captions.css('display', '');
                }
            });
        },

        // 增强可访问性支持
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEMS', true),
                    icons = profile.getSubNode('ICON', true),
                    captions = profile.getSubNode('CAPTION', true),
                    flags = profile.getSubNode('FLAG', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'toolbar',
                    'aria-label': '状态按钮工具栏',
                    'aria-orientation': 'horizontal'
                });

                // 为按钮项添加ARIA属性
                items.each(function (item) {
                    var itemElement = ood(item),
                        itemIndex = ood.arr.indexOf(items, item),
                        itemData = profile.properties.items && profile.properties.items[itemIndex],
                        itemText = itemData && itemData.caption || '按钮';

                    if (itemElement.hasClass('ood-ui-btn')) {
                        itemElement.attr({
                            'role': 'button',
                            'aria-label': itemText,
                            'aria-pressed': itemElement.hasClass('ITEM-checked') ? 'true' : 'false',
                            'tabindex': properties.disabled ? '-1' : '0'
                        });
                    } else {
                        itemElement.attr({
                            'role': 'menuitemradio',
                            'aria-label': itemText,
                            'aria-checked': itemElement.hasClass('ITEM-checked') ? 'true' : 'false',
                            'tabindex': properties.disabled ? '-1' : '0'
                        });
                    }
                });

                // 为图标添加ARIA属性
                icons.each(function (icon) {
                    var iconElement = ood(icon);
                    iconElement.attr({
                        'aria-hidden': 'true'
                    });
                });

                // 为标记添加ARIA属性
                flags.each(function (flag) {
                    var flagElement = ood(flag),
                        flagText = flagElement.text();

                    if (flagText) {
                        flagElement.attr({
                            'role': 'status',
                            'aria-label': '标记: ' + flagText
                        });
                    }
                });
            });
        }
    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();

        t.className = '{_className}';
        t.ITEMS.className = '{_bordertype }';
        t.ITEMS.style = '{_align}';
        t.$submap = {
            items: {
                ITEM: {
                    className: '{_itemClass} {itemClass} {disabled} {readonly} ',
                    style: '{itemPadding};{itemMargin};{itemWidth};{itemAlign};{itemStyle};{_display} {_itemColor}',
                    tabindex: '{_tabindex}',
                    ICON: {
                        $order: 10,
                        className: 'oodcon {imageClass} {picClass}',
                        style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}{_iconColor}',
                        text: '{iconFontCode}'
                    },
                    CAPTION: {
                        $order: 11,
                        style: '{_fontColor}',
                        text: '{caption}'
                    },
                    DROP: {
                        $order: 12,
                        className: 'oodfont',
                        $fonticon: 'ood-uicmd-arrowdrop',
                        style: '{_dropDisplay}'
                    },
                    FLAG: {
                        $order: 13,
                        className: 'ood-display-none {flagClass}',
                        style: '{_flagStyle};{flagStyle}',
                        text: '{flagText}'
                    }
                }
            }
        };
        this.setTemplate(t);
    },
    Static: {
        Appearances: {
            ITEMS: {
                position: 'relative',
                overflow: 'visible'
                // ,
                // 'text-align': 'center'
            },
            ITEM: {
                'vertical-align': 'middle',
                position: 'relative',
                'padding': 'var(--ood-statusbtn-padding)',
                'margin': '.166667em',
                'cursor': 'pointer',
                'white-space': 'nowrap',
                'border-radius': 'var(--ood-statusbtn-border-radius)',
                'background': 'var(--ood-statusbtn-default-bg)',
                'border': 'var(--ood-statusbtn-default-border)',
                'color': 'var(--ood-statusbtn-default-text)',
                'transition': 'all 0.2s ease'
            },
            'ITEM-hover': {
                'background': 'var(--ood-statusbtn-hover-bg)'
            },
            'ITEM-active, ITEM-checked': {
                'background': 'var(--ood-statusbtn-active-bg)',
                'color': 'var(--ood-statusbtn-active-text)'
            },
            CAPTION: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                'vertical-align': 'middle',
                margin: "0 0.8em 0 0.3em",
                'font-size': '1.2em'
            },
            DROP: {
                'vertical-align': 'middle'
            },
            FLAG: {
                top: '-.5em',
                right: '-.5em',
                position: 'absolute',
                'z-index': 10
            }
        },
        DataModel: ({
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
                action: function (value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                action: function (value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },

            expression: {
                ini: '',
                action: function () {
                }
            },
            menuType: {
                ini: 'BOTTOMBAR'
            },
            iconColors: null,
            itemColors: null,
            fontColors: {
                ini: '',
                type: "color",
                caption: "字体颜色",
                action: function (value) {
                    this.getRoot().css('color', value);
                }
            },

            expression: {
                ini: '',
                action: function () {
                }
            },
            autoFontColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemColor: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },
            maxHeight: null,
            tagCmds: null,
            height: 'auto',
            align: {
                ini: "center",
                listbox: ['', 'left', 'center', 'right'],
                action: function (value) {
                    this.getSubNode('ITEMS', true).css('text-align', value);
                }
            },

            itemMargin: {
                ini: "",
                action: function (value) {
                    this.getSubNode('ITEM', true).css('margin', v);
                }
            },
            itemPadding: {
                ini: "",
                action: function (v) {
                    this.getSubNode('ITEM', true).css('padding', v);
                }
            },
            itemWidth: {
                $spaceunit: 1,
                ini: "auto",
                action: function (v) {
                    this.getSubNode('ITEM', true).width(v || 'auto');
                }
            },
            itemAlign: {
                ini: "",
                listbox: ['', 'left', 'center', 'right'],
                action: function (value) {
                    this.getSubNode('ITEM', true).css('text-align', value);
                }
            },

            itemType: {
                ini: "button",
                listbox: ['text', 'button', 'dropButton'],
                action: function (value) {
                    this.boxing().refresh();
                }
            },
            connected: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            }
        }),
        Behaviors: {
            DroppableKeys: ["ITEMS"]
        },
        EventHandlers: {
            onCmd: null
        },

        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile), t, v;
            var p = profile.properties, ns = this;
            if (p.align) {
                data._align = "text-align:" + p.align;
            }
            ood.arr.each(data.items, function (item) {
                if (!item.index) {
                    item.index = ood.arr.indexOf(data.items, item);
                }
                profile.boxing()._autoColor(item, item.index, p);
            })
            return data;
        },


        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties, t,
                type = item.type || p.itemType;
            item._tabindex = p.tabindex;

            profile.boxing()._autoColor(item, index, p);

            if (p.connected) item.itemMargin = "margin:" + (index === 0 ? "0" : "0 0 0 -1px");
            else if (t = item.itemMargin || p.itemMargin) item.itemMargin = "margin:" + t;

            if (t = item.itemPadding || p.itemPadding) item.itemPadding = "padding:" + t;

            if (t = item.itemWidth || p.itemWidth) item.itemWidth = "width:" + profile.$forceu(t || 'auto');
            if (t = item.itemAlign || p.itemAlign) item.itemAlign = "text-align:" + t;

            if (item.flagText || item.flagClass) item._flagStyle = 'display:block';
            if (!item.flagClass) item.flagClass = 'ood-uiflag-1';
            if (!item.iconFontSize) item.iconFontSize = 'iconFontSize:1.2em';

            item._itemClass = type == "text" ? "ood-node-a"
                : ("ood-ui-btn ood-uibar ood-uigradient " + (p.connected ? (index == 0 ? "ood-uiborder-radius-tl ood-uiborder-radius-bl ood-uiborder-noradius-r"
                    : index === len - 1 ? "ood-uiborder-radius-tr ood-uiborder-radius-br ood-uiborder-noradius-l"
                        : "ood-uiborder-noradius")
                    : "ood-uiborder-radius"));

            item._dropDisplay = type == "dropButton" ? '' : 'display:none';
            item.hidden ? item._display = 'display:none' : '';


        },

        RenderTrigger: function () {
            // 现代化功能初始化
            var self = this;
            ood.asyRun(function () {
                self.boxing().StatusButtonsTrigger();
            });
        }
    }
});

