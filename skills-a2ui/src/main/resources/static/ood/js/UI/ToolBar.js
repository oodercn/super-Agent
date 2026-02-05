ood.Class("ood.UI.ToolBar", ["ood.UI", "ood.absList"], {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {
                    "id": "common",
                    "sub": [
                        {
                            "id": "new",
                            "caption": "$RAD.widgets.esd.buttonnew",
                            "imageClass": "ri-calendar-event-line"
                        },
                        {
                            "id": "delete",
                            "caption": "$RAD.widgets.esd.buttondelete",
                            "imageClass": "ri-close-line"
                        },
                        {
                            "id": "reload",
                            "caption": "$RAD.widgets.esd.buttonreload",
                            "imageClass": "ri-refresh-line"
                        }]
                }
            ],
            theme: 'light',
            responsive: true,
            handler: true,
            hAlign: 'left',
            dock: 'top',
            autoIconColor: true,
            autoItemColor: false,
            autoFontColor: false
        },

        // Modern feature: Set theme

        setTheme: function (theme) {
            return this.each(function (profile) {
                var root = profile.getRoot();

                // Save theme to properties
                profile.properties.theme = theme;

                // Remove all theme classes first
                root.removeClass('toolbar-dark toolbar-hc');

                // Apply theme class
                if (theme === 'dark') {
                    root.addClass('toolbar-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('toolbar-hc');
                }

                // Save theme setting
                localStorage.setItem('toolbar-theme', theme);
            });
        },

        // Get current theme
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('toolbar-theme') || 'light';
        },

        // Toggle between all three themes
        toggleTheme: function () {
            var current = this.getTheme();
            var next = current === 'light' ? 'dark' :
                current === 'dark' ? 'high-contrast' : 'light';
            this.setTheme(next);
        },

        // Get current theme
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('toolbar-theme') || 'light';
        },

        // Modern initialization trigger
        ToolBarTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties
            // Initialize theme
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('toolbar-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }

            // Initialize responsive design
            if (prop.responsive !== false) {
                this.adjustLayout();
                // Note: Window resize listeners need to be handled at application level
                // OOD framework has its own event handling mechanism
            }

            // Initialize accessibility
            this.enhanceAccessibility();
        },

        // Toggle dark mode
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
        },

        // Responsive layout adjustment
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // 根据屏幕宽度添加或移除响应式类名
                // 所有样式都通过CSS类控制，不再使用内联样式
                if (width < 768) {
                    root.addClass('toolbar-mobile');
                } else {
                    root.removeClass('toolbar-mobile');
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('toolbar-tiny');
                } else {
                    root.removeClass('toolbar-tiny');
                }
            });
        },

        // 增强可访问性支持
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEMS'),
                    buttons = profile.getSubNode('BTN', true),
                    groups = profile.getSubNode('GROUP', true);

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-label': '工具栏容器'
                });

                items.attr({
                    'role': 'toolbar',
                    'aria-label': ood.getRes('UI.toolbar.label') || '工具栏',
                    'aria-orientation': 'horizontal'
                });

                // 为工具按钮添加ARIA属性
                buttons.each(function (button) {
                    var buttonNode = ood(button);
                    var buttonId = buttonNode.id();
                    if (buttonId) {
                        var subId = buttonId.split('_').pop();
                        var itemData = profile.getItemByDom(button);
                        var label = (itemData ? itemData.caption || itemData.label : subId) || '';

                        buttonNode.attr({
                            'role': 'button',
                            'aria-label': label,
                            'tabindex': '0',
                            'aria-disabled': buttonNode.hasClass('ood-ui-itemdisabled') ? 'true' : 'false'
                        });

                        // 如果是切换按钮，添加状态属性
                        if (itemData && (itemData.type === 'toggle' || itemData.value)) {
                            var isPressed = buttonNode.hasClass('ood-ui-item-checked');
                            buttonNode.attr({
                                'aria-pressed': isPressed ? 'true' : 'false'
                            });
                        }

                        // 如果有下拉菜单，添加相应属性
                        if (itemData && itemData.sub) {
                            buttonNode.attr({
                                'aria-haspopup': 'menu',
                                'aria-expanded': 'false',
                                'aria-controls': buttonId + '_menu'
                            });
                        }
                    }
                });

                // 为工具组添加ARIA属性
                groups.each(function (group) {
                    var groupNode = ood(group);
                    var groupId = groupNode.id();
                    if (groupId) {
                        var subId = groupId.split('_').pop();
                        var groupData = profile.getItemByItemId(subId);

                        groupNode.attr({
                            'role': 'group',
                            'aria-label': groupData ? groupData.caption : '工具栏分组',
                            'aria-orientation': 'horizontal'
                        });
                    }
                });

                // 键盘事件处理需要在应用层面实现
                // OOD框架的事件系统与标准DOM事件API不同
            });
        },
        updateItem: function (subId, options) {
            if (options.type) {
                return arguments.callee.upper.call(this, subId, options);
            } else {
                var self = this,
                    profile = self.get(0),
                    box = profile.box,
                    items = profile.properties.items,
                    rst = profile.queryItems(items, function (o) {
                        return typeof o == 'object' ? o.id === subId : o == subId
                    }, true, true, true),
                    nid, item, n1, n2, n3, n4, n5, t;
                if (ood.isStr(options)) options = {caption: options};

                if (rst.length) {
                    rst = rst[0];
                    if (item = rst[0]) {

                        // [[modify id
                        if (ood.isSet(options.id)) options.id += "";
                        if (options.id && subId !== options.id) {
                            nid = options.id;
                            var m2 = profile.ItemIdMapSubSerialId, v;
                            if (!m2[nid]) {
                                if (v = m2[subId]) {
                                    m2[nid] = v;
                                    delete m2[subId];
                                    profile.SubSerialIdMapItem[v].id = nid;
                                } else {
                                    item.id = nid;
                                }
                            }
                        }
                        delete options.id;
                        // modify id only
                        if (ood.isEmpty(options))
                            return self;
                        //]]

                        //in dom already?
                        n1 = profile.getSubNodeByItemId('ICON', nid || subId);
                        n2 = profile.getSubNodeByItemId('CAPTION', nid || subId);
                        n3 = profile.getSubNodeByItemId('ITEM', nid || subId);
                        n4 = profile.getSubNodeByItemId('LABEL', nid || subId);
                        n5 = profile.getSubNodeByItemId('BTN', nid || subId);

                        if ('value' in options && options.value !== item.value)
                            profile.getSubNodeByItemId('BTN', nid || subId).tagClass('-checked', !!options.value);

                        if ('caption' in options && options.caption !== item.caption) {
                            n2.html(options.caption);
                            if (options.caption && !item.caption)
                                n2.css('display', '');
                            if (!options.caption && item.caption)
                                n2.css('display', 'none');
                        }
                        if ('label' in options && options.label !== item.label) {
                            n4.html(options.label);
                            if (options.label && !item.label)
                                n4.css('display', '');
                            if (!options.label && item.label)
                                n4.css('display', 'none');
                        }
                        if ('disabled' in options && options.disabled !== item.disabled) {
                            if (options.disabled)
                                n3.addClass('ood-ui-itemdisabled');
                            else
                                n3.removeClass('ood-ui-itemdisabled');

                            n5.onMouseout(true, {$force: true})
                        }
                        if ('image' in options && options.image !== item.image)
                            n1.css('background-image', options.image);
                        if ('imagePos' in options && options.imagePos !== item.imagePos)
                            n1.css('background-position', options.imagePos);
                        if ('imageClass' in options && options.imageClass !== item.imageClass) {
                            if (item.imageClass)
                                n1.removeClass(item.imageClass);
                            if (options.imageClass)
                                n1.addClass(options.imageClass);
                        }
                        if ('hidden' in options) {
                            var b = !!options.hidden;
                            if (b) {
                                if (item.hidden !== true) {
                                    n3.css('display', 'none');
                                }
                            } else {
                                if (item.hidden === true) {
                                    n3.css('display', '');
                                }
                            }
                        }

                        //merge options
                        ood.merge(item, options, 'all');
                    }
                }
                return self;
            }
        },
        showItem: function (itemId, value) {
            return this.each(function (profile) {
                var item = profile.getItemByItemId(itemId);
                if (item) {
                    item.hidden = value === false;
                    profile.getSubNodeByItemId('ITEM', itemId).css('display', value === false ? 'none' : '');
                }
            });
        },
        showGroup: function (grpId, value) {
            return this.each(function (profile) {
                ood.arr.each(profile.properties.items, function (o) {
                    if (o.id == grpId) {
                        o.hidden = value === false;
                        return false;
                    }
                });
                var n = profile.getSubNodeByItemId('GROUP', grpId);
                n.css('display', value === false ? 'none' : '');

                ood.resetRun(profile.$xid + ':showgrp', function () {
                    if (profile.renderId && profile.getRootNode().offsetWidth) {
                        ood.UI.$dock(profile, true, true);
                    }
                });
            });
        }
    },
    Static: {
        _focusNodeKey: 'BTN',
        _ITEMKEY: 'GROUP',
        Templates: {
            tagName: 'div',
            className: '{_className}',
            style: '{_style}',
            ITEMS: {
                className: 'ood-uibar ood-uiborder-outset ood-uiborder-radius',
                tagName: 'div',
                style: '{mode}',
                text: '{items}'
            },
            $submap: {
                items: {
                    GROUP: {
                        className: '{groupClass}',
                        style: '{grpDisplay} {_groupStyle}',
                        HANDLER: {
                            className: 'oodfont',
                            $fonticon: 'ood-icon-placeholder',
                            style: '{mode2}'
                        },
                        LIST: {
                            $order: 1,
                            tagName: 'text',
                            text: '{sub}'
                        }
                    }
                },
                'items.sub': {
                    ITEM: {
                        style: '{_itemDisplay}',
                        className: " {disabled}",
                        //for firefox2 image in -moz-inline-box cant change height bug
                        IBWRAP: {
                            tagName: 'div',
                            SPLIT: {
                                style: '{splitDisplay}',
                                className: "ood-uiborder-l ood-uiborder-r",
                                // for auto height
                                text: '&nbsp;'
                            },
                            LABEL: {
                                style: '{labelDisplay}',
                                text: '{label}'
                            },

                            BTN: {
                                tagName: 'button',
                                className: 'ood-uiborder-hidden ood-uiborder-radius ood-showfocus {itemClass}{itemcls}',
                                style: '{itemStyle} {_boxDisplay}{_itemColor}',
                                tabindex: '{_tabindex}',
                                BOXWRAP: {
                                    tagName: 'div',
                                    RULER: {},
                                    ICON: {
                                        $order: 1,
                                        className: 'oodcon {imageClass} {picClass}',
                                        style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{_iconFontSize}{imageDisplay}{iconStyle}{_iconColor}',
                                        text: '{iconFontCode}'
                                    },
                                    CAPTION: {
                                        $order: 1,
                                        text: '{caption}',
                                        style: '{_fontSize}{captionDisplay}{_fontColor}'
                                    },
                                    DROP: {
                                        $order: 3,
                                        className: 'oodfont',
                                        $fonticon: 'ood-uicmd-arrowdrop',
                                        style: '{_dropDisplay}'
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        Appearances: {
            KEY: {
                position: 'absolute',
                overflow: 'hidden',
                left: 0,
                top: 0
            },
            RULER: {
                padding: '0',
                margin: '0',
                width: '0'
            },
            ICON: {
                margin: 0,
                'font-size': '1.25em',
                'color': 'var(--icon-color)',
                'transition': 'all 0.2s ease',
                'vertical-align': 'middle'
            },
            'BTN:hover ICON': {
                'color': 'var(--icon-hover)'
            },

            ITEMS: {
                display: 'flex',
                'flex-wrap': 'wrap',
                // 'padding': '0.5em',
                'background-color': 'var(--ood-toolbar-bg)',
                'border': 'var(--ood-toolbar-border)',
                'border-radius': '0.25rem',
                'gap': '0.25em',
                'box-shadow': '0 1px 3px rgba(0,0,0,0.1)'
            },
            HANDLER: {
                height: '100%',
                width: '.75em',
                background: 'url(' + ood.ini.img_handler + ') repeat-y left top',
                cursor: 'move',
                'vertical-align': 'middle'
            },
            GROUP: {
                // crack for: The IE 'non-disappearing content' bug
                position: 'static',
                padding: '.125em .25em 0 .125em',
                'vertical-align': 'middle'
            },
            ITEM: {
                'vertical-align': 'middle',
                padding: '0 .125em',
                margin: '0'
            },
            'SPLIT': {
                $order: 1,
                width: '0',
                'vertical-align': 'middle',
                margin: '0 .25em'
            },
            BTN: {
                'cursor': 'pointer',
                'border-radius': '0.25rem',
                'background-color': 'var(--ood-toolbar-bg)',
                'color': 'var(--ood-toolbar-text)',
                'border': '1px solid transparent',
                'transition': 'all 0.2s ease',
                'display': 'inline-flex',
                'align-items': 'center',
                'justify-content': 'center',
                'gap': '0.5em',
                'padding': '0.5em 0.25em',
                //'min-width': '3em',
                'font-weight': 'normal'
            },
            'BTN:hover': {
                'background-color': 'var(--ood-toolbar-item-hover)',
                'box-shadow': '0 2px 5px rgba(0,0,0,0.1)'
            },
            'BTN:active': {
                'background-color': 'var(--ood-toolbar-item-active)'
            },
            'BTN-disabled': {
                'opacity': '0.6',
                'cursor': 'not-allowed'
            },
            BOX: {
                height: 'auto'
            },
            'LABEL, CAPTION': {
                'vertical-align': 'middle',
                'margin-left': '.25em',
                'margin-right': '.25em',
                'font-size': '1em'
            },
            LABEL: {
                cursor: 'default',
                'padding': '.25em'
            },
            DROP: {
                'vertical-align': 'middle'
            },


            // 移动端样式
            'toolbar-mobile BTN': {
                'min-width': '2em',
                'padding': '0.2em 0.4em'
            },
            'toolbar-mobile CAPTION': {
                'font-size': '0.9em'
            },
            'toolbar-mobile GROUP': {
                'margin': '0 0.2em'
            },

            // 小屏幕样式
            'toolbar-tiny CAPTION': {
                'display': 'none'
            },
            'toolbar-tiny BTN': {
                'min-width': '1.8em',
                'padding': '0.2em'
            }
        },
        Behaviors: {
            NoTips: ["GROUP", "HANDLER"],
            HoverEffected: {BTN: ['BTN']},
            ClickEffected: {BTN: ['BTN']},
            DraggableKeys: ["HANDLER"],
            DroppableKeys: ["GROUP", "ITEMS"],
            BTN: {
                onClick: function (profile, e, src) {
                    if (profile.properties.disabled) return false;
                    var id2 = ood.use(src).parent(3).id(),
                        item2 = profile.getItemByDom(id2);
                    if (item2.disabled) return false;

                    var item = profile.getItemByDom(src);
                    if (item.disabled) return false;

                    ood.use(src).focus(true);
                    if (item.type == "statusButton")
                        ood.use(src).tagClass('-checked', item.value = !item.value);

                    profile.boxing().onClick(profile, item, item2, e, src, item.id);
                    return false;
                }
            }
        },
        DataModel: {
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
            listKey: null,
            iconColors: null,
            itemColors: null,
            fontColors: null,
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

            height: {
                ini: 'auto',
                readonly: true
            },
            iconFontSize: {
                ini: '',
                action: function (v) {
                    this.getSubNode('ICON', true).css('font-size', v);
                }
            },
            width: {
                $spaceunit: 1,
                ini: 'auto'
            },

            left: {
                $spaceunit: 1,
                ini: 0
            },
            top: {
                $spaceunit: 1,
                ini: 0
            },

            handler: {
                ini: true,
                action: function (v) {
                    this.getSubNode('HANDLER', true).css('display', v ? '' : 'none');
                }
            },
            position: 'absolute',
            hAlign: {
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode('ITEMS', true).css('textAlign', v);
                }
            },
            dock: {
                ini: 'top',
                listbox: ['top', 'bottom', 'left', 'right']
            }
            // 移除了 items 属性定义，因为现在在 Instance 部分的 iniProp 对象中定义了默认值
        },
        EventHandlers: {
            onClick: function (profile, item, group, e, src, itemid) {
            }
        },
        _adjustItems: function (arr) {
            if (!arr) arr = [ood.stamp() + ''];
            if (ood.isStr(arr)) arr = [arr];

            var a = ood.copy(arr), m;
            ood.arr.each(a, function (o, i) {
                if (ood.isArr(o)) {
                    o = {
                        id: ood.id(),
                        sub: o
                    };
                }
                if (ood.isHash(o)) {
                    //copy group
                    a[i] = ood.copy(o);
                    a[i].sub = [];
                    //copy sub(tool item)
                    if (o.sub)
                        ood.arr.each(o.sub, function (v) {
                            a[i].sub.push(ood.isHash(v) ? ood.copy(v) : {id: v + ""});
                        });
                }
            });
            return a;
        },


        _onDrop: function (profile, e, src, key, data, item) {
            var k = profile.getKey(ood.use(src).id()),
                po = data.profile,
                ps = data.domId,
                oitem,
                t = ood.absObj.$specialChars;

            //remove
            oitem = ood.clone(po.getItemByDom(ps), function (o, i) {
                return !t[(i + '').charAt(0)]
            });
            po.boxing().removeItems([oitem.id], 'GROUP', true);

            if (k == profile.keys.GROUP)
                profile.boxing().insertItems([oitem], item.id, true);
            else
                profile.boxing().insertItems([oitem]);

            data._new = oitem;
            return false;
        },
        _prepareData: function (profile) {
            var d = arguments.callee.upper.call(this, profile);
            var p = profile.properties, ns = this;
            d.mode = p.hAlign != 'left' ? ('text-align:' + p.hAlign + ';') : '';
            ood.arr.each(d.items, function (item) {
                if (item.sub) {
                    ood.arr.each(item.sub, function (subitem) {
                        var index = subitem.index;
                        if (!index) {
                            index = ood.arr.indexOf(item.sub, subitem);
                        }
                        profile.boxing()._autoColor(subitem, index, p);
                    })
                }

            })
            return d;
        },

        _prepareItem: function (profile, oitem, sitem, pid, index, len, mapCache, serialId) {
            var ns = this, p = profile.properties,
                dn = 'display:none',

                tabindex = profile.properties.tabindex,
                fun = function (profile, dataItem, item, pid, index, len, mapCache, serialId) {
                    var id = dataItem[ood.UI.$tag_subId] = typeof serialId == 'string' ? serialId : ('a_' + profile.pickSubId('aitem')),
                        t;
                    if (typeof item == 'string')
                        item = {caption: item};

                    if (false !== mapCache) {
                        profile.ItemIdMapSubSerialId[item.id] = id;
                        profile.SubSerialIdMapItem[id] = item;
                    }

                    if (item['object']) {
                        dataItem['object'] = ns._prepareInlineObj(profile, item, tabindex);
                    } else {

                        ood.arr.each(ood.toArr('itemWidth,bgimg,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,fontSize,autoItemSize,autoImgSize'), function (i) {
                            item[i] = ood.isSet(item[i]) ? item[i] : p[i];
                        });


                        // for compitable with older versions
                        if (item.statusButton) {
                            item.type = "statusButton";
                            delete item.statusButton;
                        }
                        else if (item.dropButton) {
                            item.type = "dropButton";
                            delete item.dropButton;
                        }
                        else if (item.split) {
                            item.type = "split";
                            delete item.split;
                        }

                        if (item.type !== "split" && !item.caption) {
                            item.caption = "";
                        }

                        ood.UI.adjustData(profile, item, dataItem);

                        if (item.type == "statusButton" && !!item.value)
                            dataItem.itemcls = " ood-uiborder-hidden-checked " + profile.getClass('BTN', '-checked', !!item.value);


                        dataItem._tabindex = tabindex;
                        dataItem.splitDisplay = dataItem.type == "split" ? '' : dn;
                        dataItem.labelDisplay = dataItem.label ? '' : dn;
                        dataItem._iconFontSize = dataItem.iconFontSize;
                        dataItem._fontSize = dataItem.fontSize;
                        dataItem._boxStyle = dataItem.boxStyle;
                        dataItem.captionDisplay = dataItem.caption ? '' : dn;
                        dataItem._dropDisplay = item.type == "dropButton" ? '' : dn;
                        dataItem._boxDisplay = (dataItem.type !== "split" && (dataItem.caption || dataItem.image || dataItem.imageClass)) ? '' : dn;
                    }
                    dataItem._itemDisplay = item.hidden ? dn : '';
                    item._pid = pid;
                };

            if (oitem.sub) {
                ood.arr.each(oitem.sub, function (subitem) {
                    var subindex = subitem.index;
                    if (!subindex) {
                        subindex = ood.arr.indexOf(oitem.sub, subitem);
                    }
                    profile.boxing()._autoColor(subitem, subindex, p);
                })
            }

            if (pid) {
                fun(profile, oitem, sitem, pid, index, len, mapCache, serialId);
            } else {
                var arr = [],
                    dataItem,
                    a = sitem.sub || [];

                pid = sitem.id;
                oitem.mode2 = ('handler' in sitem) ? (sitem.handler ? '' : dn) : (profile.properties.handler ? '' : dn);
                oitem.grpDisplay = sitem.hidden ? dn : '';
                oitem._groupStyle = sitem.groupStyle ? sitem.groupStyle : "";
                oitem.sub = arr;

                ood.arr.each(a, function (item) {
                    dataItem = {id: item.id, groupStyle: oitem.groupStyle};
                    fun(profile, dataItem, item, pid, index, len, mapCache, serialId);
                    arr.push(dataItem);
                });
            }
        },

        RenderTrigger: function () {
            var self = this, p = self.properties;
            if (p.value)
                self.boxing().setValue(p.value, null, null, 'render');

            // 现代化功能初始化
            ood.asyRun(function () {
                if (self.boxing() && self.boxing().ToolBarTrigger) {
                    self.boxing().ToolBarTrigger();
                }
            })
        }


    }
});