ood.Class("ood.UI.PopMenu", ["ood.UI.Widget", "ood.absList"], {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [{id: 'a', caption: 'item 1', imageClass: 'ri-number-1'}, {
                id: 'b',
                caption: 'item 2',
                imageClass: 'ri-number-2'
            }, {id: 'c', caption: 'item 3', imageClass: 'ri-number-3'}, {
                id: 'd',
                caption: 'item 4',
                imageClass: 'ri-number-4',
                disabled: true
            }]
        },

        adjustSize: function () {
            this.each(function (profile) {
                if (profile.renderId) {
                    var root = profile.getRoot();
                    var border = profile.getSubNode('BORDER'),
                        box = profile.getSubNode('BOX'),
                        bg = profile.getSubNode('BOXBGBAR'),
                        items = profile.getSubNode('ITEMS'),
                        nodes = profile.getSubNode('ITEM', true),
                        prop = profile.properties,
                        us = ood.$us(profile),
                        cb = border.contentBox(),
                        adjustunit = function (v, emRate) {
                            return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                        },
                        ww = 0, hh = 0;

                    items.cssSize({width: 'auto', height: 'auto'});

                    hh = items.height() + (cb ? 0 : border._borderH());
                    if (ood.browser.ie67 && hh % 2 == 1) hh += 1;
                    items.addClass(profile.getClass('ITEMS', '-inline'));
                    nodes.each(function (n) {
                        ww = Math.max(ww, n.offsetWidth);
                    });

                    if (ww % 2 == 1) ww += 1;
                    items.removeClass(profile.getClass('ITEMS', '-inline'));
                    ww = ww < 120 ? 120 : ww;
                    hh = hh + 10;
                    // for IE7
                    items.cssSize({
                        width: adjustunit(ww),
                        height: adjustunit(hh)
                    });
                    bg.height(adjustunit(hh));

                    var h = adjustunit(Math.min(prop._maxHeight, hh)),
                        w = adjustunit(Math.min(prop._maxWidth, ww)),
                        size = {
                            width: w,
                            height: h
                        };
                    prop.width = w;
                    prop.height = h;

                    root.cssSize(size);
                    border.cssSize(size);
                    box.cssSize({
                        width: adjustunit(Math.min(prop._maxWidth, ww) + ood.Dom.getScrollBarSize()),
                        height: size.height
                    });
                }
            });
            return this._setScroll();
        },
        _setScroll: function () {
            return this.each(function (profile) {
                if (profile.renderId) {
                    var o = profile.getSubNode('BOX'),
                        t = o.scrollTop(),
                        h = o.scrollHeight(),
                        b = profile.getRoot(),
                        hh = b.offsetHeight();
                    profile.getSubNode('TOP').css('display', t === 0 ? 'none' : 'block');
                    profile.getSubNode('BOTTOM').css('display', (hh >= h - t) ? 'none' : 'block');
                }
            })
        },
        _scrollToBottom: function () {
            var profile = this.get(0),
                o = profile.getSubNode('BOX'),
                border = profile.getSubNode('BORDER'),
                y = o.scrollTop(),
                b = false,
                offset,
                h = o.scrollHeight(),
                bh = border.height();
            if (bh < h - y) {
                y += (profile.$scrollStep = Math.max(5, (profile.$scrollStep || 1) * 1.005));
                if (bh >= h - y) {
                    y = h - bh;
                    b = true;
                }
                o.scrollTop(y);
                if (b) {
                    profile.getSubNode('BOTTOM').css('display', 'none');
                    profile.$scrollTobottom = false;
                    profile.$scrollStep = 1;
                } else {
                    profile.getSubNode('TOP').css('display', 'block');
                    if (profile.$scrollTobottom)
                        ood.asyRun(this._scrollToBottom, 0, [], this);
                }
            }
        },
        _scrollToTop: function () {
            var profile = this.get(0),
                o = profile.getSubNode('BOX'),
                y = o.scrollTop(),
                b = false;
            if (y > 0) {
                y -= (profile.$scrollStep = Math.max(5, (profile.$scrollStep || 1) * 1.005));
                if (y < 0) {
                    y = 0;
                    b = true;
                }
                o.scrollTop(y);
                if (b) {
                    profile.getSubNode('TOP').css('display', 'none');
                    profile.$scrollToTop = false;
                    profile.$scrollStep = 1;
                } else {
                    profile.getSubNode('BOTTOM').css('display', 'block');
                    if (profile.$scrollToTop)
                        ood.asyRun(this._scrollToTop, 0, [], this);
                }
            }
        },
        _initGrp: function () {
            var profile = this.get(0), root;
            if (!profile.$popGrp || !profile.$popGrp.length) {
                root = profile.getRoot();
                profile.$popGrp = [root._get(0)];
                //group blur trigger
                root.setBlurTrigger(profile.$xid, null);
                root.setBlurTrigger(profile.$xid, function () {
                    if (profile.box) {
                        profile.boxing().hide();
                        if (profile.$popGrp)
                            profile.$popGrp.length = 0;
                    }
                }, profile.$popGrp);
            }
        },
        setTagVar: function (tagVar) {
            var ns = this, profile = ns.get(0);
            if (!profile.tagVar) {
                profile.tagVar = tagVar
            } else {
                ood.merge(profile.tagVar, tagVar, 'all')
            }
        },
        pop: function (pos, type, parent, ignoreEffects) {
            var ns = this,
                profile = ns.get(0),
                p = profile.properties,
                pid = p.parentID || ood.ini.$rootContainer,
                sms = '$subPopMenuShowed',
                hl = '$highLight',
                cm = '$childPopMenu';
            //ensure rendered
            if (!profile.renderId) {
                //use empty idv for LayoutTrigger
                ood.Dom.getEmptyDiv().append(ns.render(true));
            }

            //clear highLight first
            if (profile.$highLight)
                ood([profile.$highLight]).tagClass('-hover', false);

            // set container
            profile._conainer = pid ? ood.get(profile, ["host", pid]) ? profile.host[pid].getContainer() : ood(pid) : parent || null;

            profile.getRoot().popToTop(pos, type, profile._conainer);

            ns._setScroll();
            ns.adjustSize();

            ns._initGrp();
            profile[cm] = profile[sms] = profile[hl] = null;
            return ns;
        },
        hide: function (triggerEvent, ignoreEffects, e) {
            var t,
                profile = this.get(0),
                p = profile.properties,
                root = profile.getRoot(),
                sms = '$subPopMenuShowed',
                hl = '$highLight',
                cm = '$childPopMenu',
                fun = function () {
                    if (false !== triggerEvent)
                        if (false === profile.boxing().beforeHide(profile, ignoreEffects, e))
                            return this;

                    if (!root || root.css('display') == 'none') return;

                    if (ood.get(profile, ['$hideMenuPool', '_nodes', 0]))
                        profile.$hideMenuPool.append(root);
                    else
                        root.css('display', 'none');

                    if (t = profile[hl])
                        ood([t]).tagClass('-hover', false);

                    //hide all parent pop
                    var p = profile[cm], q;
                    if (t = profile[sms]) t.hide(triggerEvent, ignoreEffects);
                    while (p) {
                        p.boxing().hide(triggerEvent, ignoreEffects);
                        p = (q = p)[cm];
                        q[cm] = q[sms] = q[hl] = null;
                    }
                    profile[cm] = profile[sms] = profile[hl] = null;
                    if (t = profile.$parentPopMenu) t[sms] = null;

                    if (profile.$popGrp)
                        ood.arr.removeValue(profile.$popGrp, root._get(0));

                    //remove trigger
                    if (!profile.$popGrp || !profile.$popGrp.length)
                        root.setBlurTrigger(profile.$xid, null);

                    if (false !== triggerEvent)
                        profile.boxing().onHide(profile);
                };
            root.hide(fun, null, ignoreEffects);
            return this;
        },
        _afterInsertItems: function (profile) {
            if (!profile.renderId) return;
            profile.boxing().adjustSize();
        },
        _afterRemoveItems: function (profile) {
            if (!profile.renderId) return;
            profile.boxing().adjustSize();
        },

        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    border = profile.getSubNode('BORDER'),
                    box = profile.getSubNode('BOX'),
                    items = profile.getSubNode('ITEM', true),
                    captions = profile.getSubNode('CAPTION', true),
                    icons = profile.getSubNode('ICON', true),
                    bg = profile.getSubNode('BOXBGBAR');

                if (theme === 'dark') {
                    // 暗黑模式样式
                    root.addClass('popmenu-dark');
                    border.css({
                        'background-color': 'var(--dark-bg-card)',
                        'border-color': 'var(--dark-border)',
                        'color': 'var(--dark-text)'
                    });
                    box.css({
                        'background-color': 'var(--dark-bg)',
                        'color': 'var(--dark-text)'
                    });
                    items.css({
                        'color': 'var(--dark-text)'
                    });
                    // // 悬停和选中状态
                    // items.filter('.ood-ui-item-hover, .ood-ui-item-checked').css({
                    //     'background-color': 'var(--dark-bg-hover)',
                    //     'color': 'var(--dark-text-heading)'
                    // });
                    captions.css({
                        'color': 'var(--dark-text)'
                    });
                    icons.css({
                        'color': 'var(--dark-text-muted)'
                    });
                    bg.css({
                        'background-color': 'var(--dark-bg-card)'
                    });
                } else {
                    // 浅色模式样式
                    root.removeClass('popmenu-dark');
                    border.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    box.css({
                        'background-color': '',
                        'color': ''
                    });
                    items.css({
                        'color': ''
                    });
                    captions.css({
                        'color': ''
                    });
                    icons.css({
                        'color': ''
                    });
                    bg.css({
                        'background-color': ''
                    });
                }

                // 保存主题设置
                localStorage.setItem('popmenu-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('popmenu-theme') || 'light';
        },

        PopMenuTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('popmenu-theme');
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
                    captions = profile.getSubNode('CAPTION', true),
                    icons = profile.getSubNode('ICON', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('popmenu-mobile');

                    // 移动端调整
                    items.css({
                        'padding': '0.5em 0.8em',
                        'min-height': '44px' // 更好的触摸体验
                    });
                    captions.css({
                        'font-size': '0.9em'
                    });
                    icons.css({
                        'font-size': '1.1em'
                    });

                    // 调整最大宽度以适应移动设备
                    prop._maxWidth = Math.min(prop._maxWidth || 460, width - 40);
                } else {
                    root.removeClass('popmenu-mobile');

                    // 恢复桌面样式
                    items.css({
                        'padding': '',
                        'min-height': ''
                    });
                    captions.css({
                        'font-size': ''
                    });
                    icons.css({
                        'font-size': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('popmenu-tiny');

                    // 优化小屏幕显示
                    captions.css({
                        'font-size': '0.8em'
                    });
                    // 在极小屏幕上隐藏图标以节省空间
                    if (width < 320) {
                        icons.css({
                            'display': 'none'
                        });
                    }
                } else {
                    root.removeClass('popmenu-tiny');
                }

                // 重新调整尺寸
                profile.boxing().adjustSize();
            });
        },

        // 增强可访问性支持
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    icons = profile.getSubNode('ICON', true),
                    captions = profile.getSubNode('CAPTION', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'menu',
                    'aria-label': '弹出菜单'
                });

                // 为每个菜单项添加ARIA属性
                items.each(function (item, index) {
                    var itemNode = ood(item);
                    var itemData = profile.getItemByDom(item);

                    // 基础ARIA属性
                    itemNode.attr({
                        'role': itemData.type === 'split' ? 'separator' : 'menuitem',
                        'tabindex': itemData.disabled ? '-1' : '0',
                        'aria-label': itemData.caption || '菜单项',
                        'aria-disabled': itemData.disabled ? 'true' : 'false'
                    });

                    // 对于有子菜单的项目
                    if (itemData.sub && itemData.sub.length > 0) {
                        itemNode.attr({
                            'aria-haspopup': 'true',
                            'aria-expanded': 'false'
                        });
                    }

                    // 对于复选框类型
                    if (itemData.type === 'checkbox') {
                        itemNode.attr({
                            'role': 'menuitemcheckbox',
                            'aria-checked': itemData.value ? 'true' : 'false'
                        });
                    }

                    // 对于单选框类型
                    if (itemData.type === 'radiobox') {
                        itemNode.attr({
                            'role': 'menuitemradio',
                            'aria-checked': itemData.value ? 'true' : 'false'
                        });
                    }
                });

                // 为图标添加ARIA属性
                icons.each(function (icon) {
                    ood(icon).attr({
                        'aria-hidden': 'true' // 隐藏装饰性图标
                    });
                });
            });
        }
    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();
        ood.merge(t.FRAME.BORDER, {
            className: "ood-uiborder-outset ood-uiborder-radius",
            TOP: {
                className: 'oodfont ood-uibar ood-uiborder-b',
                $fonticon: 'ood-icon-circleup'
            },
            BOTTOM: {
                className: 'oodfont ood-uibar ood-uiborder-t',
                $fonticon: 'ood-icon-circledown'
            },
            BOX: {
                tagName: 'div',
                className: "ood-uibase",
                BOXBGBAR: {
                    tabName: 'div',
                    className: 'ood-uibar',
                    style: '{_iconDisplay}'
                },
                ITEMS: {
                    tagName: 'div',
                    text: "{items}"
                }
            },
            POOL: {}
        }, 'all');
        t.$submap = {
            'items': function (profile, template, v, tag, result) {
                var t;
                tag = tag + '.' + v.type;
                //for ood.UI or ood.Template
                if (t = v.object) {
                    //[v] is for ood.Template
                    result[result.length] = t.build(v);
                } else {
                    if (template[tag])
                        ood.UI.$doTemplate(profile, template, v, tag, result);
                }
            },
            'items.split': {
                ITEMSPLIT: {
                    style: "{_itemDisplay}",
                    className: 'ood-uiborder-b'
                }
            },
            'items.button': {
                ITEM: {
                    tabindex: -1,
                    className: ' ood-uimenu {itemClass} {disabled}',
                    style: '{itemStyle}{_itemDisplay}{_itemColor}',
                    ICON: {
                        $order: 0,
                        className: ' {imageClass}  {picClass}',
                        style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{_iconDisplay}{iconStyle} {_iconColor}',
                        text: '{iconFontCode}'
                    },
                    CAPTION: {
                        text: '{caption}',
                        style: '{_fontColor}',
                        $order: 1
                    },
                    RULER: {
                        style: '{displayAdd}',
                        $order: 2
                    },
                    ADD: {
                        tagName: 'div',
                        style: '{displayAdd}',
                        text: '{add}',
                        $order: 2
                    },
                    SUB: {
                        className: 'oodfont',
                        $fonticon: 'ood-icon-singleright',
                        style: '{displaySub}'
                    }
                }
            },
            'items.checkbox': {
                ITEM: {
                    tabindex: -1,
                    className: '  ood-uimenu {itemClass} {disabled}',
                    style: '{itemStyle}{_itemDisplay}',
                    CHECKBOX: {
                        $order: 0,
                        className: 'oodfont',
                        $fonticon: '{_fi_checkboxCls1} {_fi_checkboxCls2}  {_iconDisplay}'
                    },
                    CAPTION: {
                        text: '{caption}',
                        $order: 1
                    },
                    RULER: {
                        style: '{displayAdd}',
                        $order: 2
                    },
                    ADD: {
                        tagName: 'div',
                        style: '{displayAdd}',
                        text: '{add}',
                        $order: 2
                    }
                }
            },
            'items.radiobox': {
                ITEM: {
                    tabindex: -1,
                    className: '  ood-uimenu {itemClass} {disabled}',
                    style: '{itemStyle}{_itemDisplay}',
                    RADIOBOX: {
                        $order: 0,
                        className: 'oodfont',
                        $fonticon: '{_fi_radioboxCls1} {_fi_radioboxCls2}  {_iconDisplay}'
                    },
                    CAPTION: {
                        text: '{caption}',
                        $order: 1
                    },
                    RULER: {
                        style: '{displayAdd}',
                        $order: 2
                    },
                    ADD: {
                        tagName: 'div',
                        style: '{displayAdd}',
                        text: '{add}',
                        $order: 2
                    }
                }
            }
        };
        this.setTemplate(t);

        this.prototype.popUp = this.prototype.pop;
    },
    Static: {
        $initRootHidden: true,
        Appearances: {
            KEY: {
                visibility: 'hidden'
            },
            POOL: {
                position: 'absolute',
                display: 'none'
            },
            BOX: {
                overflow: 'hidden',
                position: 'relative',
                overflow: 'hidden',
                'overflow-y': 'auto',
                'z-index': '3',
                'background-color': 'var(--bg-primary)',
                'border-radius': 'var(--radius-md)',
                'box-shadow': 'var(--shadow-lg)'
            },
            BORDER: {
                position: 'relative',
                overflow: 'hidden',
                'background-color': 'var(--bg-primary)',
                'border': '1px solid var(--border)'
            },
            ITEMS: {
                position: 'relative',
                top: 0,
                left: 0,
                overflow: 'hidden',
                'white-space': 'nowrap'
            },
            BOXBGBAR: {
                'z-index': -1,
                position: 'absolute',
                left: 0,
                top: 0,
                width: '2em',
                height: '100%',
                'background-color': 'rgba(0,0,0,0.15)'
            },
            'ITEMS-inline ITEM': {
                $order: 5,
                display: ood.$inlineBlock
            },
            ITEM: {
                display: 'block',
                position: 'relative',
                overflow: 'visible',
                'white-space': 'nowrap',
                cursor: 'pointer',
                padding: 'var(--spacing-xs) var(--spacing-lg) var(--spacing-xs) var(--spacing-sm)',
                outline: 0,
                'background-color': 'transparent',
                'transition': 'background-color var(--transition-fast)',
                'color': 'var(--text)'
            },
            'ITEM:hover': {
                'background-color': 'var(--bg-hover)',
                'color': 'var(--text-heading)'
            },
            ITEMSPLIT: {
                display: 'block',
                position: 'relative',
                overflow: 'visible',
                'white-space': 'nowrap',
                margin: 'var(--spacing-xs) var(--spacing-xs) var(--spacing-xs) var(--spacing-md)',
                'border-top': '1px solid var(--border)'
            },
            ICON: {
                margin: 0,
                'font-size': '1.25em !important',
                'color': 'var(--text-primary)'
            },
            TOP: {
                cursor: 'pointer',
                display: 'none',
                position: 'absolute',
                'z-index': '10',
                top: 0,
                'text-align': 'center',
                width: '100%',
                'background-color': 'var(--bg-header)'
            },
            BOTTOM: {
                cursor: 'pointer',
                display: 'none',
                position: 'absolute',
                bottom: 0,
                'z-index': '10',
                'text-align': 'center',
                width: '100%',
                'background-color': 'var(--bg-header)'
            },
            'RADIOBOX, CHECKBOX, RADIOBOX-checked, CHECKBOX-checked': {
                cursor: 'pointer',
                'vertical-align': 'middle'
            },
            CAPTION: {
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle',
                'padding-left': 'var(--spacing-sm)',
                'font-size': 'var(--font-size-md)',
                'color': 'var(--text)'
            },
            RULER: {
                width: '12em'
            },
            ADD: {
                position: 'absolute',
                top: 'var(--spacing-xs)',
                right: 0,
                width: '7em',
                'padding-right': 'var(--spacing-lg)',
                'text-align': 'right',
                'z-index': '10',
                zoom: ood.browser.ie ? 1 : null,
                'color': 'var(--text-muted)'
            },
            SUB: {
                position: 'absolute',
                top: '0.15em',
                right: '0.15em',
                'color': 'var(--text-muted)'
            },

            // 暗黑模式样式
            'popmenu-dark BOX': {
                'background-color': 'var(--dark-bg) !important',
                'box-shadow': 'var(--dark-shadow-lg) !important'
            },
            'popmenu-dark BORDER': {
                'background-color': 'var(--dark-bg-card) !important',
                'border-color': 'var(--dark-border) !important'
            },
            'popmenu-dark BOXBGBAR': {
                'background-color': 'var(--dark-bg-card) !important'
            },
            'popmenu-dark ITEM': {
                'color': 'var(--dark-text) !important'
            },
            'popmenu-dark ITEM:hover': {
                'background-color': 'var(--dark-bg-hover) !important',
                'color': 'var(--dark-text-heading) !important'
            },
            'popmenu-dark ITEMSPLIT': {
                'border-top-color': 'var(--dark-border) !important'
            },
            'popmenu-dark ICON': {
                'color': 'var(--dark-text-muted) !important'
            },
            'popmenu-dark CAPTION': {
                'color': 'var(--dark-text) !important'
            },
            'popmenu-dark ADD': {
                'color': 'var(--dark-text-muted) !important'
            },
            'popmenu-dark SUB': {
                'color': 'var(--dark-text-muted) !important'
            },

            // 移动端样式
            'popmenu-mobile ITEM': {
                'padding': 'var(--spacing-sm) var(--spacing-md)',
                'min-height': '44px'
            },
            'popmenu-mobile CAPTION': {
                'font-size': 'var(--font-size-sm)'
            },
            'popmenu-mobile ICON': {
                'font-size': '1.1em !important'
            },

            // 小屏幕样式
            'popmenu-tiny CAPTION': {
                'font-size': 'var(--font-size-xs)'
            },
            'popmenu-tiny ICON': {
                'display': 'none !important'
            }
        },
        Behaviors: {
            HoverEffected: {TOP: 'TOP', BOTTOM: 'BOTTOM'},
            BOX: {
                onScroll: function (profile, e, src) {
                    profile.boxing()._setScroll();
                }
            },
            ITEM: {
                onMouseover: function (profile, e, src) {
                    var sms = '$subPopMenuShowed',
                        all = '$allPops',
                        hl = '$highLight',
                        showp = '$showpops',
                        popgrp = '$popGrp';
                    //for stop second trigger by focus event
                    if (profile[hl] == src) return;
                    profile[all] = profile[all] || {};

                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        itemId = item.id,
                        Cancel = false,
                        pop, popp, t;
                    //if sub pop menu showed
                    if (t = profile[sms]) {
                        //if the showed menu is self
                        if (t == ood.get(profile, [all, itemId]))
                            Cancel = true;
                        else {
                            t.hide();
                            profile[sms] = null;
                        }
                    }
                    if (!Cancel) {
                        if (t = profile[hl])
                            ood([t]).tagClass('-hover', false);
                        profile[hl] = src;
                        ood.use(src).tagClass('-hover');
                        //don't fire events here
                        try {
                            ood.use(src).get(0).focus()
                        } catch (e) {
                        }
                    }

                    if (!Cancel && item.sub) {
                        // if no sub arrays
                        if (!(ood.isArr(item.sub) && item.sub.length)) {
                            if (profile.onShowSubMenu) {
                                var r = profile[all][itemId];
                                if (r && r['ood.UI'] && !r.isEmpty()) {
                                }
                                else
                                    r = profile.boxing().onShowSubMenu(profile, item, src);

                                // return UI control
                                if (r && r['ood.UI'] && !r.isEmpty()) {
                                    // keep it
                                    profile[all][itemId] = profile[sms] = r;

                                    r = r.reBoxing();
                                    r.onMouseout(function (p, e, src) {
                                        profile.box._mouseout(profile, e, src);
                                    }, null, -1);

                                    profile.boxing()._initGrp();
                                    profile[popgrp].push(r._get(0));

                                    r.popToTop(src, 2, profile._conainer);

                                    return;
                                }
                                // return items array
                                else if (r && ood.isArr(r) && r.length) {
                                    item.sub = r;
                                }
                            }
                        }

                        // show items
                        if (ood.isArr(item.sub) && item.sub.length) {
                            //no create
                            if (!(pop = profile[all][itemId])) {
                                var pro = profile.properties;
                                pop = (new ood.UI.PopMenu({
                                    position: 'absolute',
                                    items: item.sub,
                                    autoHide: pro.autoHide,
                                    showEffects: pro.showEffects,
                                    hideEffects: pro.hideEffects
                                })).render(true);
                                pop.onShowSubMenu(function (pro, item, src) {
                                    return profile.boxing().onShowSubMenu(profile, item, src);
                                });
                                pop.onMenuSelected(function (pro, item, src) {
                                    return profile.boxing().onMenuSelected(profile, item, src);
                                });
                                popp = pop.get(0);
                                //set pool to parent
                                popp.$hideMenuPool = ood.get(profile, ['$hideMenuPool', '_nodes', 0]) || profile.getSubNode('POOL');

                                profile[all][itemId] = pop;

                                //collect
                                profile[showp] = profile[showp] || [profile];
                                popp[showp] = profile[showp];
                                profile[showp].push(popp);
                            } else popp = pop.get(0);

                            //input a copy of root for group trigger
                            profile.boxing()._initGrp();
                            profile[popgrp].push(popp.getRoot()._get(0));
                            popp[popgrp] = profile[popgrp];

                            //set parent pop
                            popp.$parentPopMenu = profile;
                            profile.$childPopMenu = popp;

                            pop.pop(src, 2, profile._conainer);
                            profile[sms] = pop;
                        }
                    }
                },
                onMouseout: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        itemId = item.id,
                        action = true,
                        hl = '$highLight',
                        t;
                    if (profile[hl] == src) return;

                    //if cursor move to submenu, keep the hover face
                    if (t = profile.$subPopMenuShowed) {
                        var node = e.toElement || e.relatedTarget,
                            target = t.get(0).getRootNode();
                        try {
                            do {
                                if (node == target)
                                    return;
                            } while ((node && (node = node.parentNode)))
                        } catch (a) {
                        }
                    }
                    ood.use(src).tagClass('-hover', false);
                    profile[hl] = null;
                },
                onClick: function (profile, e, src) {
                    var prop = profile.properties,
                        item = profile.getItemByDom(src),
                        itemId = item.id;
                    if (prop.disabled || item.disabled) return false;

                    // give a change to click an item with sub popmenu
                    if (!item.group) {
                        if (item.type == 'checkbox')
                            profile.getSubNodeByItemId('CHECKBOX', item.id).tagClass('-checked', item.value = !item.value);
                        else if (item.type == 'radiobox') {
                            profile.getSubNode('RADIOBOX', true).tagClass('-checked', false);
                            ood.arr.each(prop.items, function (o) {
                                if (o.type == 'radiobox')
                                    o.value = false;
                            });
                            profile.getSubNodeByItemId('RADIOBOX', item.id).tagClass('-checked', item.value = true);
                        }

                        if (profile.onMenuSelected) profile.boxing().onMenuSelected(profile, item, src);

                        if (prop.hideAfterClick) {
                            ood.use(src).tagClass('-hover', false);
                            //hide all parent pop
                            ood.asyRun(function () {
                                var p = profile, q;
                                if (!p.renderId) return;
                                while (p) {
                                    p.boxing().hide();
                                    p = (q = p).$parentPopMenu;
                                    q.$parentPopMenu = q.$subPopMenuShowed = null;
                                }
                                //reset
                                profile.$subPopMenuShowed = null;
                                if (profile.$popGrp)
                                    profile.$popGrp.length = 0;
                            }, 100);
                        }
                    }
                },
                onFocus: function (profile, e, src) {
                    var box = profile.getSubNode('BOX'),
                        top = box.scrollTop(), h = box.scrollHeight(),
                        n = ood.use(src).offsetTop();

                    if (n < top || n > top + h)
                        ood.use(src).offsetTop(top);

                    ood.use(src).onMouseover();
                },
                onKeydown: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        items = profile.properties.items,
                        key = ood.Event.getKey(e).key,
                        itemId = item.id,
                        flag, r, tid, node, t;

                    switch (key) {
                        case 'enter':
                            ood(src).onClick();
                            break;
                        case 'up':
                            r = true;
                            flag = false;
                            ood.arr.each(items, function (o, i) {
                                if (o.type == 'split') return;
                                if (flag) {
                                    tid = o.id;
                                    return r = false;
                                }
                                if (o.id == itemId) flag = true;
                            }, null, true);
                            //last
                            if (r) tid = items[items.length - 1].id;
                            node = profile.getSubNodeByItemId('ITEM', tid).get(0);
                            break;
                        case 'down':
                            r = true;
                            flag = false;
                            ood.arr.each(items, function (o, i) {
                                if (o.type == 'split') return;
                                if (flag) {
                                    tid = o.id;
                                    return r = false;
                                }
                                if (o.id == itemId) flag = true;
                            });
                            //first
                            if (r) tid = items[0].id;
                            node = profile.getSubNodeByItemId('ITEM', tid).get(0);
                            break;
                        case 'left':
                            if (t = profile.$parentPopMenu) {
                                if (t = profile.$parentPopMenu.$highLight)
                                    node = t;
                            }
                            break;
                        case 'right':
                            if ((t = profile.$subPopMenuShowed) && t == profile.$allPops[itemId])
                                t.activate();
                            break;
                    }
                    if (node && node.tagName) try {
                        node.focus()
                    } catch (e) {
                    }
                }
            },
            TOP: {
                onMouseover: function (profile) {
                    profile.$scrollToTop = true;
                    profile.$scrollStep = 1;
                    profile.boxing()._scrollToTop();
                },
                onMouseout: function (profile) {
                    profile.$scrollToTop = false;
                    profile.$scrollStep = 1;
                },
                onClick: function (profile) {
                    profile.$scrollStep *= 2;
                }
            },
            BOTTOM: {
                onMouseover: function (profile) {
                    profile.$scrollTobottom = true;
                    profile.$scrollStep = 1;
                    profile.boxing()._scrollToBottom();
                },
                onMouseout: function (profile) {
                    profile.$scrollTobottom = false;
                    profile.$scrollStep = 1;
                },
                onClick: function (profile) {
                    profile.$scrollStep *= 2;
                }
            },
            ITEMS: {
                afterKeydown: function (profile, e) {
                    var key = ood.Event.getKey(e).key;
                    if (key == 'tab' || key == 'enter')
                        return true;
                    else if (key == 'esc') {
                        //top
                        do {
                            profile.boxing().hide();
                        } while (profile = profile.$parentPopMenu)

                        return false;
                    } else return false;
                }
            },
            BORDER: {
                onMouseout: function (profile, e, src) {
                    profile.box._mouseout(profile, e, src);
                }
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

            autoFontColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            iconColors: null,
            itemColors: null,
            fontColors: null,
            dock: null,
            tabindex: null,
            tips: null,
            border: null,
            resizer: null,
            dragSortable: null,
            showEffects: "Blur",
            hideEffects: "",
            autoTips: false,
            shadow: true,
            _maxHeight: 360,
            _maxWidth: 460,
            left: -10000,
            parentID: '',
            hideAfterClick: true,

            autoHide: false,

            height: {
                $spaceunit: 1,
                ini: 'auto'
            },
            //opera needs more space for initialize
            width: {
                $spaceunit: 1,
                ini: 'auto'
            },
            position: 'absolute',
            noIcon: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            $hborder: 0,
            $vborder: 0
        }),
        EventHandlers: {
            onShowSubMenu: function (profile, item, src) {
            },
            beforeHide: function (profile, e) {
            },
            onHide: function (profile) {
            },
            onMenuSelected: function (profile, item, src) {
            }
        },


        RenderTrigger: function () {
            var prf = this;
            prf.boxing().adjustSize();

            // 初始化现代化功能
            prf.boxing().PopMenuTrigger();

            (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["sub-pops"] = function (t) {
                ood.each(prf.$allPops, function (pop) {
                    if (pop && !pop.$noDestroyByParentMenu) {
                        if (pop['ood.UI'] && !pop.isEmpty() && !pop.isDestroyed()) {
                            pop.destroy();
                        } else if (pop['ood.UIProfile'] && !pop.destroyed) {
                            pop.__gc();
                        } else if (pop['ood.Dom'] && !pop.isEmpty()) {
                            pop.remove();
                        }
                    }
                });
            };
        },


        _beforeSerialized: function (profile) {
            var o = arguments.callee.upper.call(this, profile),
                op = o.properties;
            delete op.left;
            delete op.top;
            delete op.right;
            delete op.bottom;
            delete op.width;
            delete op.height;
            return o;
        },
        _mouseout: function (profile, e) {
            if (profile.properties.autoHide) {
                var p1 = ood.Event.getPos(e),
                    size, p2, b;
                ood.arr.each(profile.$popGrp, function (o) {
                    o = ood([o]);
                    p2 = o.offset();
                    size = o.cssSize();
                    if (p1.left >= p2.left && p1.top >= p2.top && p1.left <= p2.left + size.width && p1.top <= p2.top + size.height) {
                        b = 1;
                        return false;
                    }
                });
                if (!b) {
                    while (b = profile.$parentPopMenu) profile = b;
                    profile.boxing().hide(true, null, e);
                    if (profile.$popGrp)
                        profile.$popGrp.length = 0;
                }
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                NONE = 'display:none', ns = this,
                prop = profile.properties;
            if (prop.noIcon) data._iconDisplay = NONE;

            ood.arr.each(data.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(data.items, item);
                }
                profile.boxing()._autoColor(item, index, prop);
            })

            return data;
        },
        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties, NONE = 'display:none;',
                prop = profile.properties;

            item.add = item.add || '';
            item.displayAdd = item.add ? '' : NONE;
            item.displaySub = item.sub ? '' : NONE;
            item._itemDisplay = item.hidden ? NONE : '';
            profile.boxing()._autoColor(item, index, p);
            if (!item.image) {
                item.imageClass = item.imageClass ? item.imageClass : 'oodcon ood-icon-placeholder';
                if (ood.str.startWith("ood-")) {
                    item.imageClass = "oodcon " + item.imageClass;
                }
            } else {
                item.imageClass = "oodcon ood-icon-placeholder "
            }


            item._iconDisplay = prop.noIcon ? NONE : '';

            item.type = item.type || 'button';
            if (item.type == 'checkbox') {
                item._fi_checkboxCls1 = 'ood-uicmd-check';
                item._fi_checkboxCls2 = item.value ? 'oodcon-checked ood-uicmd-check-checked' : '';
            }
            else if (item.type == 'radiobox') {
                item._fi_radioboxCls1 = 'ood-uicmd-radio';
                item._fi_radioboxCls2 = item.value ? 'oodcon-checked ood-uicmd-radio-checked' : '';
            }
        },
        _onresize: null
    }
});