ood.Class("ood.UI.List", ["ood.UI", "ood.absList", "ood.absValue"], {
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
            }],
            theme: 'light',
            responsive: true,
            selMode: 'single',
            borderType: 'flat',
            noCtrlKey: true,
            width: 320,
            height: '15em',
            maxHeight: 420,
            itemRow: '',
            optBtn: "",
            tagCmds: [],
            tagCmdsAlign: "right",
            labelSize: '4em',
            labelPos: "left",
            labelGap: 4,
            labelCaption: '$RAD.widgets.optionList',
            labelHAlign: 'right',
            labelVAlign: 'top',
            value: 'a'
        },

        // 现代化功能：设置主题 (优化版)
        setTheme: function(theme) {
            return this.each(function(profile) {
                var p = profile.properties;
                p.theme = theme;
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEMS'),
                    itemNodes = profile.getSubNode('ITEM', true),
                    label = profile.getSubNode('LABEL');

                // 移除所有主题类
                root.removeClass('list-dark list-hc');
                
                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('list-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('list-hc');
                }
                
                // 应用CSS变量
                root.attr('data-theme', theme);
                
                // 保存主题设置
                localStorage.setItem('list-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('list-theme') || 'light';
        },


        // 现代化初始化触发器
        ListTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties,
                boxing = this;

            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('list-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                this.adjustLayout();
                // 注意：窗口大小变化的监听需要在应用层面处理
                // OOD框架有自己的事件处理机制
            }

            // 初始化可访问性
            this.enhanceAccessibility();
        },
        
        // 切换暗黑模式
        toggleTheme: function() {
            const themes = ['light', 'dark', 'high-contrast'];
            const currentTheme = this.getTheme();
            const nextIndex = (themes.indexOf(currentTheme) + 1) % themes.length;
            this.setTheme(themes[nextIndex]);
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    items = profile.getSubNode('ITEMS'),
                    itemNodes = profile.getSubNode('ITEM', true),
                    label = profile.getSubNode('LABEL'),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('list-mobile');
                    
                    // 调整列表项样式
                    itemNodes.css({
                        'padding': '0.6em',
                        'font-size': '0.9em',
                        'min-height': '2.8em'
                    });
                    
                    // 调整标签样式
                    if (label && prop.labelPos && prop.labelPos !== 'none') {
                        if (prop.labelPos === 'left' || prop.labelPos === 'right') {
                            // 在小屏幕上将左右标签改为顶部
                            label.css({
                                'font-size': '0.85em',
                                'margin-bottom': '0.3em'
                            });
                        }
                    }
                } else {
                    root.removeClass('list-mobile');
                    
                    // 恢复桌面布局
                    itemNodes.css({
                        'padding': '',
                        'font-size': '',
                        'min-height': ''
                    });
                    
                    if (label) {
                        label.css({
                            'font-size': '',
                            'margin-bottom': ''
                        });
                    }
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('list-tiny');
                    
                    // 隐藏额外信息显示区域
                    var extraNodes = profile.getSubNode('EXTRA', true);
                    extraNodes.css('display', 'none');
                    
                    // 简化图标显示
                    var iconNodes = profile.getSubNode('ICON', true);
                    iconNodes.css({
                        'width': '1.2em',
                        'height': '1.2em'
                    });
                } else {
                    root.removeClass('list-tiny');
                    
                    // 恢复额外信息显示
                    var extraNodes = profile.getSubNode('EXTRA', true);
                    extraNodes.css('display', '');
                    
                    // 恢复图标显示
                    var iconNodes = profile.getSubNode('ICON', true);
                    iconNodes.css({
                        'width': '',
                        'height': ''
                    });
                }
            });
        },
        
        // 增强可访问性支持 (优化版)
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEMS'),
                    itemNodes = profile.getSubNode('ITEM', true),
                    label = profile.getSubNode('LABEL'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-orientation': properties.vertical ? 'vertical' : 'horizontal'
                });
                
                items.attr({
                    'role': 'listbox',
                    'aria-label': properties.caption || '列表项',
                    'aria-multiselectable': (properties.selMode === 'multi' || properties.selMode === 'multibycheckbox') ? 'true' : 'false'
                });
                
                // 为标签添加ARIA属性
                if (label && properties.labelCaption) {
                    label.attr({
                        'id': 'list-label-' + profile.$xid,
                        'role': 'label',
                        'aria-hidden': properties.labelPos === 'none' ? 'true' : 'false'
                    });
                    items.attr('aria-labelledby', 'list-label-' + profile.$xid);
                }
                
                // 为列表项添加ARIA属性
                itemNodes.each(function(item) {
                    var itemNode = ood(item);
                    var itemId = itemNode.id();
                    if (itemId) {
                        var itemData = profile.getItemByDom(item);
                        var isSelected = itemNode.hasClass('ood-ui-item-checked');
                        var selMode = properties.selMode;
                        
                        itemNode.attr({
                            'role': selMode === 'single' ? 'option' : 'checkbox',
                            'aria-label': '列表项: ' + (itemData ? itemData.caption : itemId),
                            'aria-selected': isSelected ? 'true' : 'false',
                            'aria-disabled': itemData.disabled ? 'true' : 'false',
                            'tabindex': isSelected && selMode === 'single' ? '0' : '-1'
                        });
                        
                        // 添加键盘导航支持
                        itemNode.on('keydown', function(e) {
                            if (e.key === 'Enter' || e.key === ' ') {
                                itemNode.fireEvent('click');
                                e.preventDefault();
                            }
                        });
                    }
                });
            });
        },
        _setCtrlValue: function (value) {
            return this.each(function (profile) {
                if (!profile.renderId) return;

                var box = profile.box,
                    uiv = profile.boxing().getUIValue(),
                    p = profile.properties,
                    item = box._ITEMKEY || 'ITEM',
                    k = box._DIRTYKEY || 'ITEM',
                    mk = 'MARK',
                    getN = function (k, i) {
                        return profile.getSubNode(k, i)
                    },
                    getI = function (i) {
                        return profile.getSubIdByItemId(i)
                    };
                if (p.selMode == 'single') {
                    var itemId = getI(uiv);
                    if (uiv !== null && itemId) {
                        getN(item, itemId).tagClass('-checked', false).tagClass('-hover', false);
                        getN(mk, itemId).tagClass('-checked', false);
                    }

                    itemId = getI(value);
                    if (itemId) {
                        getN(item, itemId).tagClass('-checked');
                        getN(mk, itemId).tagClass('-checked');
                    }

                    //scroll
                    try {
                        if (itemId) {
                            var o = getN(item, itemId);
                            if (o) {
                                var items = profile.getSubNode('ITEMS'),
                                    offset = o.offset(null, items),
                                    top = offset ? offset.top : 0,
                                    height = o.offsetHeight(),
                                    sh = items.scrollHeight(),
                                    st = items.scrollTop(),
                                    hh = items.height();
                                if (sh > hh)
                                    if (top < st || (top + height) > (st + hh))
                                        items.scrollTop(top);
                            }
                        }
                    } catch (e) {
                        console.warn(e);
                    }

                } else if (p.selMode == 'multi' || p.selMode == 'multibycheckbox') {
                    uiv = uiv ? uiv.split(p.valueSeparator) : [];
                    value = value ? value.split(p.valueSeparator) : [];
                    //check all
                    ood.arr.each(uiv, function (o) {
                        getN(item, getI(o)).tagClass('-checked', false).tagClass('-hover', false);
                        getN(mk, getI(o)).tagClass('-checked', false);
                    });
                    ood.arr.each(value, function (o) {
                        getN(item, getI(o)).tagClass('-checked');
                        getN(mk, getI(o)).tagClass('-checked');
                    });
                }
            });
        },
        _clearMouseOver: function () {
            var box = this.constructor,
                item = box._ITEMKEY || 'ITEM';
            this.getSubNode(item, true).tagClass('-hover', false);
        },
        adjustSize: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEMS'),
                    pp = profile.properties,
                    mh = pp.maxHeight,
                    h_em = profile.$isEm(pp.height),
                    h, flag;

                if (profile.$isEm(mh)) mh = profile.$em2px(mh, items, true);

                if (root.css('display') == 'none') {
                    flag = 1;
                    root.css('visibility', 'hidden');
                }
                items.height('auto');
                if (profile.properties.height != 'auto') {
                    h = Math.min(mh, items.offsetHeight());
                    if (h_em) h = profile.$px2em(h, items) + 'em';
                    items.height(pp.height = h);
                } else {
                    h = items.offsetHeight();
                    if (h > mh) {
                        items.height(pp.maxHeight);
                        profile.getRoot().height(pp.maxHeight);
                    }
                }
                if (flag) {
                    root.css('visibility', '');
                    root.css('display', 'none');
                }
                profile.getRoot().height('auto');
            });
        },
        activate: function () {
            return ood.absList.prototype.activate.call(this);
        },

        setCaptionValue: function (value) {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;

        },

        getCaptionValue: function () {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return v;
        },

        getSelectedItem: function () {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return v;
        },

        getShowValue: function (value) {
            var profile = this.get(0),
                pro = profile.properties, v, t;
            if (!ood.isDefined(value))
                value = pro.$UIvalue;
            if ((v = ood.arr.subIndexOf(pro.items, 'id', value)) != -1) {
                v = pro.items[v].caption;
                v = v.charAt(0) == '$' ? ood.getRes(v.slice(1)) : v;
            } else
                v = '';
            return v;
        },
        _setDirtyMark: function () {
            return arguments.callee.upper.apply(this, ['ITEMS']);
        }
    },
    Static: {
        _DIRTYKEY: 'ITEM',
        Templates: {
            tagName: 'div',
            style: '{_style}',
            className: '{_className}',
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign};color:var(--ood-text-heading)',
                text: '{labelCaption}'
            },
            ITEMS: {
                $order: 10,
                tagName: 'div',
                className: 'ood-uibase {_cmdsalign} {_bordertype} {_itemscls1}',
                style: 'background-color:var(--ood-bg-card);border-radius:var(--ood-radius-md)',
                text: "{items}"
            },
            $submap: {
                items: {
                    ITEM: {
                        className: 'ood-uitembg ood-uiborder-radius ood-showfocus {_itemRow} {_split} {itemClass} {disabled} {readonly}',
                        style: '{itemStyle}{_itemDisplay};background-color:var(--ood-bg);color:var(--ood-text)',
                        tabindex: '{_tabindex}',
                        LTAGCMDS: {
                            $order: 2,
                            tagName: 'span',
                            style: '{_ltagDisplay}',
                            text: "{ltagCmds}"
                        },
                        MARK: {
                            $order: 5,
                            className: 'oodfont',
                            $fonticon: 'ood-uicmd-check',
                            style: "{_cbDisplay}"
                        },
                        ICON: {
                            $order: 10,
                            className: 'oodcon {imageClass}  {picClass}',
                            style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                            text: '{iconFontCode}'
                        },
                        CAPTION: {
                            style: '{_capDisplay}',
                            text: '{caption}',
                            $order: 20
                        },
                        EXTRA: {
                            style: '{_extraDisplay}',
                            text: '{ext}',
                            $order: 30
                        },
                        RTAGCMDS: {
                            $order: 40,
                            tagName: 'span',
                            style: '{_rtagDisplay}',
                            text: "{rtagCmds}"
                        },
                        OPT: {
                            $order: 50,
                            style: '{_optDisplay}',
                            className: 'oodfont',
                            $fonticon: '{_fi_optClass}'
                        }
                    }
                },

                'items.ltagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "items.tagCmds" + (map[v.type] || '.button'), result)
                },
                'items.rtagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "items.tagCmds" + (map[v.type] || '.button'), result)
                },
                'items.tagCmds.text': ood.UI.$getTagCmdsTpl('text'),
                'items.tagCmds.button': ood.UI.$getTagCmdsTpl('button'),
                'items.tagCmds.image': ood.UI.$getTagCmdsTpl('image')
            }
        },
        Appearances: {
            KEY: {},
            LABEL: {
                'z-index': 1,
                top: 0,
                left: 0,
                display: ood.browser.isWebKit ? '-webkit-flex' : 'flex',
                position: 'absolute',
                'padding-top': '.333em',
                'color': 'var(--ood-text-heading)'
            },
            EXTRA: {
                display: 'none',
                'color': 'var(--ood-text-muted)'
            },
            ITEMS: {
                position: 'relative',
                overflow: 'auto',
                'overflow-x': 'hidden',
                'background-color': 'var(--ood-bg-card)',
                'border-radius': 'var(--ood-radius-md)'
            },
            ITEM: {
                display: 'block',
                zoom: ood.browser.ie ? 1 : null,
                cursor: 'pointer',
                position: 'relative',
                'white-space': 'nowrap',
                'background-color': 'var(--ood-bg)',
                'color': 'var(--ood-text)',
                'transition': 'background-color var(--ood-transition-fast)'
            },
            MARK: {
                $order: 1,
                cursor: 'pointer',
                'vertical-align': 'middle'
            },
            CAPTION: {
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle',
                padding: '.167em',
                'font-size': '1em',
                'white-space': 'normal',
                'color': 'var(--ood-text)'
            },
            OPT: {
                $order: 10,
                position: 'absolute',
                left: 'auto',
                top: '50%',
                'margin-top': '-0.5em',
                right: '.167em',
                display: 'none'
            },
            'LTAGCMDS, RTAGCMDS': {
                padding: 0,
                margin: 0,
                'vertical-align': 'middle'
            },
            'ITEMS-tagcmdleft RTAGCMDS': {
                "padding-right": '.333em',
                "float": "left"
            },
            'ITEMS-tagcmdfloatright RTAGCMDS': {
                "padding-right": '.333em',
                "float": "right"
            }
        },
        Behaviors: {
            HoverEffected: {ITEM: 'ITEM', OPT: 'OPT', CMD: 'CMD', ICON: 'ICON'},
            ClickEffected: {ITEM: 'ITEM', OPT: 'OPT', CMD: 'CMD'},
            DraggableKeys: ["ITEM"],
            DroppableKeys: ["ITEM", "ITEMS"],
            ITEM: {
                onDblclick: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src);
                    profile.boxing().onDblclick(profile, item, e, src);
                },
                onClick: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        itemId = profile.getSubId(src),
                        box = profile.boxing(),
                        ks = ood.Event.getKey(e);

                    if (profile.beforeClick && false === box.beforeClick(profile, item, e, src)) return false;

                    if (properties.disabled || item.disabled || item.type == 'split') return false;

                    if (profile.onClick)
                        box.onClick(profile, item, e, src);

                    ood.use(src).focus(true);

                    switch (properties.selMode) {
                        case 'none':
                            box.onItemSelected(profile, item, e, src, 0);
                            break;
                        case 'multibycheckbox':
                            if (properties.readonly || item.readonly) return false;
                            var value = box.getUIValue(),
                                arr = value ? value.split(properties.valueSeparator) : [],
                                checktype = 1;
                            if (arr.length) {
                                if (ood.arr.indexOf(arr, item.id) != -1) {
                                    ood.arr.removeValue(arr, item.id);
                                    checktype = -1
                                } else {
                                    arr.push(item.id);
                                }
                                arr.sort();
                                value = arr.join(properties.valueSeparator);

                                //update string value only for setCtrlValue
                                if (box.getUIValue() !== value) {
                                    box.setUIValue(value, null, null, 'click');
                                    if (box.get(0) && box.getUIValue() == value)
                                        box.onItemSelected(profile, item, e, src, checktype);
                                }
                                break;
                            }
                        case 'multi':
                            if (properties.readonly || item.readonly) return false;
                            var value = box.getUIValue(),
                                arr = value ? value.split(properties.valueSeparator) : [],
                                checktype = 1;

                            if (arr.length && (ks.ctrlKey || ks.shiftKey || properties.noCtrlKey || properties.$checkbox)) {
                                //for select
                                if (ks.shiftKey) {
                                    var items = properties.items,
                                        i1 = ood.arr.subIndexOf(items, 'id', profile.$firstV.id),
                                        i2 = ood.arr.subIndexOf(items, 'id', item.id),
                                        i;
                                    arr.length = 0;
                                    for (i = Math.min(i1, i2); i <= Math.max(i1, i2); i++)
                                        arr.push(items[i].id);
                                } else {
                                    if (ood.arr.indexOf(arr, item.id) != -1) {
                                        ood.arr.removeValue(arr, item.id);
                                        checktype = -1
                                    } else
                                        arr.push(item.id);
                                }

                                arr.sort();
                                value = arr.join(properties.valueSeparator);

                                //update string value only for setCtrlValue
                                if (box.getUIValue() !== value) {
                                    box.setUIValue(value, null, null, 'click');
                                    if (box.get(0) && box.getUIValue() == value)
                                        box.onItemSelected(profile, item, e, src, checktype);
                                }
                                break;
                            }
                        case 'single':
                            if (properties.readonly || item.readonly) return false;
                            if (box.getUIValue() !== item.id) {
                                profile.$firstV = item;
                                box.setUIValue(item.id, null, null, 'click');
                                if (box.get(0) && box.getUIValue() == item.id)
                                    box.onItemSelected(profile, item, e, src, 1);
                            }

                            break;
                    }
                    if (profile.afterClick) box.afterClick(profile, item, e, src);
                },
                onKeydown: function (profile, e, src) {
                    var keys = ood.Event.getKey(e), key = keys[0], shift = keys[2],
                        cur = ood(src),
                        first = profile.getRoot().nextFocus(true, true, false),
                        last = profile.getRoot().nextFocus(false, true, false);

                    switch (ood.Event.getKey(e)[0]) {
                        case 'tab':
                            if (shift) {
                                if (cur.get(0) != first.get(0)) {
                                    first.focus(true);
                                    return false;
                                }
                            } else {
                                if (cur.get(0) != last.get(0)) {
                                    last.focus(true);
                                    return false;
                                }
                            }
                            break;
                        case 'left':
                        case 'up':
                            var next = cur.nextFocus(false, true, false);
                            if (cur.get(0) == first.get(0))
                                last.focus(true);
                            else
                                cur.nextFocus(false);
                            return false;
                            break;
                        case 'right':
                        case 'down':
                            var next = cur.nextFocus(true, false, false);
                            if (cur.get(0) == last.get(0))
                                first.focus(true);
                            else
                                cur.nextFocus();
                            return false;
                            break;
                        case 'enter':
                            cur.onClick(true);
                            break;
                    }
                },
                onContextmenu: function (profile, e, src) {
                    if (profile.onContextmenu)
                        return profile.boxing().onContextmenu(profile, e, src, profile.getItemByDom(src),ood.Event.getPos(e)) !== false;
                },
                onMouseover: function (profile, e, src) {
                    if (ood.browser.fakeTouch || ood.browser.deviceType == 'touchOnly') return;
                    var item = profile.getItemByDom(src);
                    if (!item) return;
                    if (!profile.properties.optBtn && !item.optBtn) return;
                    profile.getSubNode('OPT', profile.getSubId(src)).setInlineBlock();
                },
                onMouseout: function (profile, e, src) {
                    if (ood.browser.fakeTouch || ood.browser.deviceType == 'touchOnly') return;
                    var item = profile.getItemByDom(src);
                    if (!item) return;
                    if (!profile.properties.optBtn && !item.optBtn) return;
                    profile.getSubNode('OPT', profile.getSubId(src)).css('display', 'none');
                }
            },
            OPT: {
                onClick: function (profile, e, src) {
                    if (profile.onShowOptions) {
                        var item = profile.getItemByDom(src);
                        if (!item) return;
                        if (!profile.properties.optBtn && !item.optBtn) return;
                        profile.boxing().onShowOptions(profile, item, e, src);
                    }
                    return false;
                },
                onDblclick: function (profile, e, src) {
                    return false;
                }
            },
            CMD: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties,
                        item = profile.getItemByDom(ood.use(src).parent().get(0));
                    if (!item) return false;

                    if (prop.disabled || item.disabled || item.type == 'split') return false;
                    if (profile.onCmd)
                        profile.boxing().onCmd(profile, item, ood.use(src).id().split('_')[1], e, src);
                    return false;
                }
            },
            LABEL: {
                onClick: function (profile, e, src) {
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelClick)
                        profile.boxing().onLabelClick(profile, e, src);
                },
                onDblClick: function (profile, e, src) {
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelDblClick)
                        profile.boxing().onLabelDblClick(profile, e, src);
                },
                onMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != 'left') return;
                    if (profile.properties.disabled) return false;
                    if (profile.onLabelActive)
                        profile.boxing().onLabelActive(profile, e, src);
                }
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
                action: function(value) {
                    this.boxing().setTheme(value);
                },
                caption: ood.getResText("DataModel.theme") || "主题"
            },
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                },
                caption: ood.getResText("DataModel.responsive") || "响应式"
            },
            
            expression: {
                ini: '',
                action: function () {
                },
                caption: ood.getResText("DataModel.expression") || "表达式"
            },
            selMode: {
                ini: 'single',
                listbox: ['single', 'none', 'multi', 'multibycheckbox'],
                action: function (value) {
                    if (!this.box._ITEMMARKED)
                        this.getSubNode('MARK', true).css('display', (value == 'multi' || value == 'multibycheckbox') ? '' : 'none');
                },
                caption: ood.getResText("DataModel.selectionMode") || "选择模式"
            },
            borderType: {
                ini: 'flat',
                listbox: ['none', 'flat', 'inset', 'outset'],
                action: function (v) {
                    var ns = this,
                        p = ns.properties,
                        node = ns.getSubNode('ITEMS'),
                        reg = /^ood-uiborder-/, 
                        pretag = 'ood-uiborder-',
                        root = ns.getRoot();
                    node.removeClass(reg);
                    node.addClass(pretag + v);

                    //force to resize
                    ns.adjustSize();
                },
                caption: ood.getResText("DataModel.borderType") || "边框类型"
            },

            noCtrlKey: {
                ini: true,
                caption: ood.getResText("DataModel.noCtrlKey") || "无需Ctrl键"
            },
            width: {
                $spaceunit: 1,
                ini: 'auto',
                caption: ood.getResText("DataModel.width") || "宽度"
            },
            height: {
                $spaceunit: 1,
                ini: '15em',
                caption: ood.getResText("DataModel.height") || "高度"
            },
            maxHeight: {
                ini: 420,
                caption: ood.getResText("DataModel.maxHeight") || "最大高度"
            },
            itemRow: {
                ini: '',
                combobox: ["row", "cell"],
                action: function (v) {
                    var ns = this.getSubNode('ITEM', true);
                    ns.removeClass(/ood-item-[\w]+/);
                    if (v) ns.addClass('ood-item-' + (v || 'row'));
                },
                caption: ood.getResText("DataModel.itemRowType") || "项目行类型"
            },
            optBtn: {
                ini: "",
                combobox: ood.toArr("ood-uicmd-opt,ood-icon-singleright"),
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.optionButton") || "选项按钮"
            },
            tagCmds: {
                ini: [],
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.labelCommand") || "标签命令"
            },
            tagCmdsAlign: {
                ini: "right",
                listbox: ['left', 'right', 'floatright'],
                action: function (v) {
                    var profile = this, box = profile.getSubNode("ITEMS"), cls = profile.getClass('ITEMS', '-tagcmd');
                    box.removeClass(new RegExp(cls + '[\w]*')).addClass(profile.getClass('ITEMS', '-tagcmd' + v));
                },
                caption: ood.getResText("DataModel.labelCommandAlign") || "标签命令对齐"
            },
            // label
            labelSize: {
                $spaceunit: 2,
                ini: 0,
                action: function (v) {
                    this.getSubNode('LABEL').css({display: v ? '' : 'none'});
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                },
                caption: ood.getResText("DataModel.labelSize") || "标签尺寸"
            },
            labelPos: {
                ini: "left",
                listbox: ['none', 'left', 'top', 'right', 'bottom'],
                action: function (v) {
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                },
                caption: ood.getResText("DataModel.labelPosition") || "标签位置"
            },

            labelGap: {
                $spaceunit: 2,
                ini: 4,
                action: function (v) {
                    ood.UI.$doResize(this, this.properties.width, this.properties.height, true);
                },
                caption: ood.getResText("DataModel.labelGap") || "标签间距"
            },
            labelCaption: {
                ini: "",
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('LABEL').html(ood.adjustRes(v, true));
                },
                caption: ood.getResText("DataModel.labelCaption") || "标签标题"
            },
            labelHAlign: {
                ini: 'right',
                listbox: ['', 'left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode('LABEL').css({
                        'textAlign': v || '',
                        'justifyContent': v == 'right' ? 'flex-end' : v == 'center' ? 'center' : v == 'left' ? 'flex-start' : ''
                    });
                },
                caption: ood.getResText("DataModel.labelHAlign") || "标签水平对齐"
            },
            labelVAlign: {
                ini: 'top',
                listbox: ['', 'top', 'middle', 'bottom'],
                action: function (v) {
                    this.getSubNode('LABEL').css('align-items', v == 'bottom' ? 'flex-end' : v == 'middle' ? 'center' : v == 'top' ? 'flex-start' : '');
                },
                caption: ood.getResText("DataModel.labelVAlign") || "标签垂直对齐"
            }
        },
        EventHandlers: {
            onClick: function (profile, item, e, src) {
            },
            onCmd: function (profile, item, cmdkey, e, src) {
            },
            beforeClick: function (profile, item, e, src) {
            },
            afterClick: function (profile, item, e, src) {
            },
            onDblclick: function (profile, item, e, src) {
            },
            onShowOptions: function (profile, item, e, src) {
            },
            onItemSelected: function (profile, item, e, src, type) {
            },

            onLabelClick: function (profile, e, src) {
            },
            onLabelDblClick: function (profile, e, src) {
            },
            onLabelActive: function (profile, e, src) {
            }
        },
        _onStartDrag: function (profile, e, src, pos) {
            var pos = ood.Event.getPos(e);
            ood.use(src).startDrag(e, {
                dragSource: profile.$xid,
                dragType: 'icon',
                shadowFrom: src,
                targetLeft: pos.left + 12,
                targetTop: pos.top + 12,
                dragCursor: 'pointer',
                dragDefer: 2,
                dragKey: profile.box.getDragKey(profile, src),
                dragData: profile.box.getDragData(profile, e, src)
            });
            return false;
        },
        _onDropTest: function (profile, e, src, key, data, item) {
            var fid = data && data.domId, tid = ood.use(src).id();
            if (fid) {
                if (fid == tid) return false;
                if (ood.get(ood.use(src).get(0), ['previousSibling', 'id']) == fid) return false;
            }
        },
        _onDrop: function (profile, e, src, key, data, item) {
            var k = profile.getKey(ood.use(src).id()),
                po = data.profile,
                ps = data.domId,
                oitem,
                t = ood.absObj.$specialChars,
                uiv = profile.properties.$UIvalue;
            //remove
            oitem = ood.clone(po.getItemByDom(ps), function (o, i) {
                return !t[(i + '').charAt(0)]
            });
            po.boxing().removeItems([oitem.id]);

            if (k == profile.keys.ITEM)
                profile.boxing().insertItems([oitem], item.id, true);
            else
                profile.boxing().insertItems([oitem]);

            if (oitem.id == uiv)
                profile.boxing().setUIValue(oitem.id, true, null, 'drop');

            data._new = oitem;
            return false;
        },
        _prepareData: function (profile) {
            var p = profile.properties, d = arguments.callee.upper.call(this, profile), t, v;
            d._bordertype = 'ood-uiborder-' + d.borderType;
            d._labelHAlign = 'text-align:' + (v = d.labelHAlign || '') + ';justify-content:' + (v == 'right' ? 'flex-end' : v == 'center' ? 'center' : v == 'left' ? 'flex-start' : '');
            d._labelVAlign = 'align-items:' + ((v = d.labelVAlign) == 'bottom' ? 'flex-end' : v == 'middle' ? 'center' : v == 'top' ? 'flex-start' : '');
            d.labelShow = d.labelPos != 'none' && d.labelSize && d.labelSize != 'auto' ? "" : "display:none";
            d._labelSize = d.labelSize ? '' : 0 + profile.$picku();
            // adjustRes for labelCaption
            if (d.labelCaption)
                d.labelCaption = ood.adjustRes(d.labelCaption, true);
            d._cmdsalign = profile.getClass('ITEMS', '-tagcmd' + profile.properties.tagCmdsAlign);


            ood.arr.each(d.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(d.items, item);
                }
                profile.boxing()._autoColor(item, index, p);
            })


            return d;
        },
        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties, m = p.selMode, t;
            item._cbDisplay = (m == 'multi' || m == 'multibycheckbox') ? '' : 'display:none;';
            item._itemRow = (t = profile.properties.itemRow) ? ('ood-item-' + t) : '';

            profile.boxing()._autoColor(item, index, p);

            if (ood.browser.fakeTouch || ood.browser.deviceType !== 'mouseOnly') {
                item._optDisplay = p.optBtn ? 'display:block;' : '';
            }

            item._fi_optClass = p.optBtn;

            if (item.type == 'split') {
                item._split = 'ood-uitem-split';
                item._ltagDisplay = item._rtagDisplay = item.imageDisplay = item._cbDisplay = item._capDisplay = item._extraDisplay = item._optDisplay = 'display:none;';
            }
            this._prepareCmds(profile, item);
        },
        RenderTrigger: function () {
            if (this.key != "ood.UI.List") return;

            var p = this.properties;
            ood.UI.$doResize(this, p.width, p.height);
            
            // 现代化功能初始化
            var self = this;
            ood.asyRun(function(){
                self.boxing().ListTrigger();
            });
        },
        _onresize: function (profile, width, height) {
            var prop = profile.properties,
                // compare with px
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                root = profile.getRoot(),
                cb = ood.browser.contentBox,

                f = function (k) {
                    return profile.getSubNode(k)
                },
                items = f('ITEMS'),
                label = f('LABEL'),

                fzrate = profile.getEmSize() / root._getEmSize(),
                itemsfz = items._getEmSize(fzrate),
                labelfz = label._getEmSize(fzrate),

                border = !cb ? 0 : prop.borderType != 'none' ? items._borderW() : 0,
                dock = prop.dock,
                max = prop.maxHeight,

                labelPos = prop.labelPos,
                labelSize = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelSize, labelfz) || 0,
                labelGap = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelGap) || 0,
                ll, tt, ww, hh;

            // caculate by px
            if (width && width != 'auto') width = profile.$px(width);
            if (height && height != 'auto') height = profile.$px(height);

            items.cssRegion({
                left: adjustunit(ll = labelPos == 'left' ? labelSize : 0, itemsfz),
                top: adjustunit(tt = labelPos == 'top' ? labelSize : 0, itemsfz),
                width: adjustunit(ww = width === null ? null : width == 'auto' ? width : Math.max(0, (width - (!cb ? 0 : items._paddingW('both')) - ((labelPos == 'left' || labelPos == 'right') ? labelSize : 0) - border)), itemsfz),
                height: adjustunit(hh = height === null ? null : height == 'auto' ? height : Math.max(0, (height - (!cb ? 0 : items._paddingH('both')) - ((labelPos == 'top' || labelPos == 'bottom') ? labelSize : 0) - border)), itemsfz)
            });


            if (height == "auto") {
                if (dock != "fill" && dock != "cover" && dock != "height" && dock != "left" && dock != "right") {
                    if (items.height() > max) {
                        items.height(adjustunit(max, itemsfz));
                        root.height('auto');
                    }
                }
            }
            if (labelSize) {
                if (width == 'auto') ww = items.offsetWidth();
                if (height == 'auto') hh = items.offsetHeight();
                label.cssRegion({
                    left: adjustunit(width === null ? null : Math.max(0, labelPos == 'right' ? ((width == 'auto' ? ww : (width - labelSize)) + labelGap) : 0), labelfz),
                    top: adjustunit(height === null ? null : Math.max(0, labelPos == 'bottom' ? ((height == 'auto' ? hh : (height - labelSize)) + labelGap) : 0), labelfz),
                    width: adjustunit(width === null ? null : Math.max(0, ((labelPos == 'left' || labelPos == 'right') ? (labelSize - labelGap) : (width == 'auto' ? ww : width))), labelfz),
                    height: adjustunit(height === null ? null : Math.max(0, ((labelPos == 'top' || labelPos == 'bottom') ? (labelSize - labelGap) : (height == 'auto' ? hh : height))), labelfz)
                });
            }
        }
    }
});