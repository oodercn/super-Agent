ood.Class("ood.UI.Layout", ["ood.UI", "ood.absList"], {
    Instance: {

        iniProp: {
            caption: ood.getRes('RAD.widgets.menuNav') || '布局',
            items: [{
                id: 'main',
                "overflow": "auto",
                "transparent": true,
                pos: 'main'
            }, {
                id: 'before',
                "size": 220,
                "cmd": true,
                "locked": false,
                "transparent": true,
                pos: 'before',
                lock: true,
                cmd: false
            }]
        },

        setChildren: function (childrens, prf) {
            var host = this;
            this.removeChildren(true, true, true);
            ood.addChild({children: childrens}, host, host.getModule(), host.getModule());
        },

        getAllFormValues: function (isAll) {
            var a = this.getChildren(),
                elems = ood.absValue.pack(a),
                formValue = {},
                profile = this.get(0);
            ood.arr.each(profile.children, function (o) {
                var oo = o[0].boxing(), name = oo.getProperties().name || o[0].alias;
                if (oo.Class['ood.UI.Tabs']) {
                    formValue[name] = oo.getAllFormValues(isAll);
                } else if (oo.Class['ood.UI.Block'] || oo.Class["ood.UI.Panel"] || oo.Class["ood.UI.Layout"]) {
                    ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
                } else if (oo.getFormValues) {
                    ood.merge(formValue, oo.getFormValues(), 'all')
                } else if (oo.getUIValue) {
                    formValue[name] = oo.getUIValue();
                } else if (oo.getValue) {
                    formValue[name] = oo.getValue();
                }
            });
            return formValue;
        },
        getPanel: function (subId) {
            return this.get(0).getSubNodeByItemId('PANEL', subId);
        },
        append: function (target, subId, pre, base) {
            var pro = this.get(0);
            return arguments.callee.upper.call(this, target, subId || 'main', pre, base);
        },
        insertItems: function (arr, base, before, all) {
            return this._insertItems(arr, base, before, all);
        },
        _insertItems: function (arr, base, before, all) {
            var node, arr2,
                items, index, r,
                data, box,
                pos = "before",
                b = this._afterInsertItems;
            return this.each(function (profile) {
                box = profile.box;
                items = profile.properties.items;
                if (!all) {
                    index = ood.arr.subIndexOf(items, 'id', base);
                    if (index == -1) {
                        pos = before ? 'before' : 'after';
                    } else {
                        if (items[index].id == 'main')
                            pos = before ? 'before' : 'after';
                        else
                            pos = items[index].pos || 'after';
                    }
                    arr2 = box._adjustItems2(arr, pos);
                } else {
                    arr2 = box._adjustItems(arr);
                }

                //must be here
                if (index == -1)
                    ood.arr.insertAny(items, arr2, before ? 0 : -1);
                else
                    ood.arr.insertAny(items, arr2, before ? index : index + 1);

                //if in dom, create it now
                if (profile.renderId) {
                    data = box._prepareItems(profile, arr2, base);
                    r = profile._buildItems('items', data);
                    // try to render inner ood.UI
                    if (profile.$attached) {
                        for (var i = 0, v; v = profile.$attached[i++];) {
                            if (v._render) v._render(true);
                        }
                        delete profile.$attached;
                    }
                    profile.getRoot().prepend(r);
                    profile.adjustSize();
                    t = null;
                }

                if (b)
                    profile.boxing()._afterInsertItems(profile, data, base, before);
            });
        },
        _afterRemoveItems: function (profile) {
            if (profile.renderId)
                profile.adjustSize();
        },
        updateItem: function (subId, options) {
            var self = this,
                profile = self.get(0),
                vertical = profile.properties.type == 'vertical',
                getN = function (key, subId) {
                    return profile.getSubNodeByItemId(key, subId)
                },
                box = profile.box,
                items = profile.properties.items,
                rst = profile.queryItems(items, function (o) {
                    return typeof o == 'object' ? o.id === subId : o == subId
                }, true, true, true),
                nid, item, serialId, node, sub, t;
            if (typeof options != 'object') return;

            if (rst.length) {
                rst = rst[0];
                if (typeof rst[0] != 'object')
                    item = rst[2][rst[1]] = {id: rst[0]};
                else
                    item = rst[0];

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

                var bResize = false;
                //in dom already?
                node = getN('ITEM', subId);
                if (!node.isEmpty()) {
                    if (options.hasOwnProperty('size')) {
                        options.size = Math.round(parseFloat('' + options.size));
                        if (options.size != item._size) {
                            item._size = options.size;
                            if (vertical)
                                node.height(options.size);
                            else
                                node.width(options.size);
                            bResize = true;
                        }
                    }
                    if (options.hasOwnProperty('hidden')) {
                        options.hidden = !!options.hidden;
                        if (options.hidden !== item.hidden) {
                            getN('ITEM', subId).css('display', options.hidden ? 'none' : '');
                            bResize = true;
                        }
                    }
                    if (options.hasOwnProperty('locked')) {
                        options.locked = !!options.locked;
                        if (options.locked !== item.locked) {
                            getN('MOVE', subId).css({
                                display: options.locked ? 'none' : '',
                                cursor: options.locked ? 'default' : vertical ? 'n-resize' : 'w-resize'
                            });
                            getN('CMD', subId).css('display', (('cmd' in options) ? options.cmd : item.cmd) && !options.locked ? '' : 'none');
                            bResize = true;
                        }
                    }
                    if (options.hasOwnProperty('folded')) {
                        options.folded = !!options.folded;
                        if (options.folded !== item.folded)
                            profile.boxing().fireCmdClickEvent(subId);
                    }
                    if (options.hasOwnProperty('cmd')) {
                        options.cmd = !!options.cmd;
                        if (options.cmd !== item.cmd)
                            getN('CMD', subId).css('display', options.cmd && !(('locked' in options) ? options.locked : item.locked) ? '' : 'none');
                    }

                    var hash = {};
                    if (options.hasOwnProperty('panelBgClr')) hash["background-color"] = options.panelBgClr;
                    if (options.hasOwnProperty('panelBgImg')) {
                        hash["background-image"] = options.panelBgImg ? ("url(" + ood.adjustRes(options.panelBgImg) + ")") : "";
                    }
                    if (options.hasOwnProperty('panelBgImgPos')) hash["position-color"] = options.panelBgImgPos;
                    if (options.hasOwnProperty('panelBgImgRepeat')) hash["background-repeat"] = options.panelBgImgRepeat;
                    if (options.hasOwnProperty('panelBgImgAttachment')) hash["background-attachment"] = options.panelBgImgAttachment;
                    if (options.hasOwnProperty('overflow')) {
                        var v = options.overflow;
                        if (v) {
                            if (v.indexOf(':') != -1) {
                                ood.arr.each(v.split(/\s*;\s*/g), function (s) {
                                    var a = s.split(/\s*:\s*/g);
                                    if (a.length > 1) {
                                        hash[ood.str.trim(a[0])] = ood.str.trim(a[1] || '');
                                    }
                                });
                            }
                        }
                        hash.overflow = v || "";
                    }
                    if (!ood.isEmpty(hash)) {
                        getN('PANEL', subId).css(hash);
                    }
                }

                //merge options
                ood.merge(item, options, 'all');

                if (bResize)
                    profile.adjustSize();
            }
            return self;
        },
        fireCmdClickEvent: function (subId) {
            this.getSubNodeByItemId('CMD', subId).onMousedown();
            return this;
        },

        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 移除所有主题类
                root.removeClass('layout-dark');

                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('layout-dark');
                }

                // 保存主题设置
                localStorage.setItem('layout-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('layout-theme') || 'light';
        },

        // 切换暗黑模式
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
        },

        // 响应式布局调整
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    items = profile.getSubNode('ITEM', true),
                    moves = profile.getSubNode('MOVE', true),
                    cmds = profile.getSubNode('CMD', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('layout-mobile');

                    // 移动端调整
                    if (prop.type === 'horizontal') {
                        // moves.css({
                        //     'height': '8px'
                        // });
                        cmds.css({
                            'font-size': '0.9em'
                        });
                    } else {
                        // moves.css({
                        //     'width': '8px'
                        // });
                        cmds.css({
                            'font-size': '0.9em'
                        });
                    }
                } else {
                    root.removeClass('layout-mobile');

                    // 恢复桌面布局
                    moves.css({
                        'height': '',
                        'width': ''
                    });
                    cmds.css('font-size', '');
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('layout-tiny');

                    // 隐藏部分控制按钮
                    cmds.css('display', 'none');
                } else {
                    root.removeClass('layout-tiny');

                    // 恢复按钮显示
                    var currentItems = profile.properties.items;
                    ood.arr.each(currentItems, function (item) {
                        if (item.cmd !== false && !item.locked) {
                            profile.getSubNodeByItemId('CMD', item.id).css('display', '');
                        }
                    });
                }

                // 触发布局重新计算
                if (profile.renderId) {
                    profile.adjustSize();
                }
            });
        },

        // 增强可访问性支持
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    panels = profile.getSubNode('PANEL', true),
                    moves = profile.getSubNode('MOVE', true),
                    cmds = profile.getSubNode('CMD', true);

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'main',
                    'aria-label': '布局容器'
                });

                // 为面板添加ARIA属性
                panels.each(function (panel) {
                    var panelNode = ood(panel);
                    var panelId = panelNode.id();
                    if (panelId) {
                        var itemId = panelId.split('_').pop(); // 从完整ID中提取子ID
                        panelNode.attr({
                            'role': 'region',
                            'aria-label': '布局面板 ' + itemId
                        });
                    }
                });

                // 为移动手柄添加ARIA属性和键盘支持
                moves.each(function (move) {
                    var moveNode = ood(move);
                    var moveId = moveNode.id();
                    if (moveId) {
                        var itemId = moveId.split('_').pop(); // 从完整ID中提取子ID
                        moveNode.attr({
                            'role': 'separator',
                            'aria-label': '调整 ' + itemId + ' 大小',
                            'tabindex': '0',
                            'aria-orientation': profile.properties.type === 'vertical' ? 'horizontal' : 'vertical'
                        });

                        // 添加键盘支持属性（实际的键盘事件处理需要在应用层面实现）
                        // OOD框架的事件系统与标准DOM事件API不同
                    }
                });

                // 为命令按钮添加ARIA属性
                cmds.each(function (cmd) {
                    var cmdNode = ood(cmd);
                    var cmdId = cmdNode.id();
                    if (cmdId) {
                        var itemId = cmdId.split('_').pop(); // 从完整ID中提取子ID
                        cmdNode.attr({
                            'role': 'button',
                            'aria-label': '折叠/展开 ' + itemId,
                            'tabindex': '0'
                        });

                        // 添加键盘支持属性（实际的键盘事件处理需要在应用层面实现）
                        // OOD框架的事件系统与标准DOM事件API不同
                    }
                });
            });
        }
    },
    Static: {
        Templates: {
            tagName: 'div',
            style: '{_style}',
            className: '{_className} {_trans}',
            text: "{items}",
            $submap: {
                items: {
                    ITEM: {
                        tagName: 'div',
                        className: '{cls1} {itemClass}',
                        style: '{itemStyle};{display}',
                        MOVE: {
                            $order: 0,
                            tagName: 'div',
                            // give icon font for em size
                            className: 'ood-ui-unselectable ood-uibar {clsmovebg} {cls2} ',
                            style: '{moveDisplay};cursor:{_cursor}'
                        },
                        CMD: {
                            $order: 1,
                            tagName: 'span',
                            style: '{cmdDisplay}',
                            className: 'ood-node ood-ui-unselectable oodfont {cls3}',
                            $fonticon: '{_fi_cls3} '
                        },
                        PANEL: {
                            tagName: 'div',
                            className: 'ood-uibase ood-uicontainer',
                            style: 'position:absolute;{_bginfo};{_overflow};',
                            text: ood.UI.$childTag
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
                top: 0,
                transition: 'all 0.3s ease'
            },
            "KEY-trans, KEY-trans > ITEM, KEY-trans > ITEM > PANEL, KEY-trans > ITEM > MOVE, KEY-trans > ITEM > CMD": {
                $order: 100,
                "background-color": "transparent",
                "border": "none"
            },
            MOVE: {
                $order: 0,
                position: 'absolute',
                'z-index': '10',
                'background': 'var(--border)',
                transition: 'all 0.2s ease'
            },
            'MOVE:hover': {
                'background': 'var(--primary)',
                'transform': 'scale(1.02)'
            },
            CMD: {
                position: 'absolute',
                cursor: 'pointer',
                'z-index': '20',
                'background': 'var(--primary)',
                'color': 'var(--text-inverse)',
                'border': '1px solid var(--primary-dark)',
                'border-radius': '4px',
                transition: 'all 0.2s ease',
                'box-shadow': 'var(--shadow-sm)'
            },
            'CMD:hover': {
                'background': 'var(--primary-dark)',
                'transform': 'scale(1.1)',
                'box-shadow': 'var(--shadow-md)'
            },

            ITEM: {
                position: 'absolute',
                "z-index": 1,
                overflow: 'hidden',
                'border-width': ood.browser.opr ? '0' : null,
                'background-color': 'var(--bg)',
                //  'border': '1px solid var(--border)',
                'border-radius': '4px',
                'box-shadow': 'var(--shadow-sm)',
                transition: 'box-shadow 0.3s ease'
            },
            'ITEM:hover': {
                'box-shadow': 'var(--shadow-md)'
            },
            PANEL: {
                position: 'absolute',
                overflow: 'auto',
                left: 0,
                top: 0,
                'border-width': ood.browser.opr ? '0' : null,
                'background-color': 'var(--bg)',
                transition: 'background-color 0.3s ease'
            },
            'ITEM-MAIN': {
                left: 0,
                right: 0,
                top: 0,
                bottom: 0
            },
            'ITEM-TOP, ITEM-BOTTOM': {
                left: 0,
                right: 0
            },
            'ITEM-LEFT, ITEM-RIGHT': {
                top: 0,
                bottom: 0
            },
            'MOVE-TOP, MOVE-BOTTOM': {
                width: '100%',
                height: ood.browser.contentBox ? '.2em' : '.22em',
                cursor: 'n-resize'
            },
            'MOVE-LEFT, MOVE-RIGHT': {
                height: '100%',
                width: ood.browser.contentBox ? '.2em' : '.22em',
                cursor: 'w-resize'
            },
            'MOVE-TOP': {
                bottom: 0
            },
            'MOVE-BOTTOM': {
                top: 0
            },
            'MOVE-LEFT': {
                right: '0'
            },
            'MOVE-RIGHT': {
                left: 0
            },
            'CMD-TOP, CMD-BOTTOM, CMD-LEFT, CMD-RIGHT': {
                padding: 0,
                'border-radius': '4px'
            },
            'CMD-TOP': {
                $order: 1,
                left: '50%',
                'margin-left': '-1em',
                bottom: 0,
                width: '2em',
                height: ood.browser.contentBox ? '.36667em' : '.45em',
                'text-align': 'center'
            },
            'CMD-BOTTOM': {
                $order: 1,
                left: '50%',
                'margin-left': '-1em',
                top: 0,
                width: '2em',
                height: ood.browser.contentBox ? '.46667em' : '.45em',
                'text-align': 'center'
            },
            'CMD-LEFT': {
                $order: 1,
                top: '50%',
                'margin-top': '-1em',
                right: 0,
                height: '2em',
                width: ood.browser.contentBox ? '.36667em' : '.45em',
                'line-height': '2em'
            },
            'CMD-RIGHT': {
                $order: 1,
                top: '50%',
                'margin-top': '-1em',
                left: 0,
                height: '2em',
                width: ood.browser.contentBox ? '.36667em' : '.45em',
                'line-height': '2em'
            },
            'MOVE-MAIN': {
                $order: 5,
                display: 'none'
            },
            'CMD-MAIN': {
                $order: 5,
                display: 'none'
            },
            // 响应式设计样式
            '.layout-mobile MOVE': {
                //   'background': 'linear-gradient(135deg, #dee2e6 0%, #adb5bd 100%) !important'
            },
            '.layout-mobile CMD': {
                'font-size': '0.9em !important'
            },
            '.layout-tiny CMD': {
                'display': 'none !important'
            },

        },
        Behaviors: {
            DroppableKeys: ['PANEL'],
            PanelKeys: ['PANEL'],
            HoverEffected: {MOVE: 'MOVE', CMD: ['MOVE', 'CMD']},
            ClickEffected: {CMD: 'CMD'},
            MOVE: {
                beforeMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != "left") return;
                    var itemId = profile.getSubId(src),
                        item = profile.getItemByDom(src);
                    if (item.folded) return;
                    if (item.locked) return;

                    var main = profile.getItemByItemId('main'),
                        o = profile.getSubNode('ITEM', itemId),
                        m = profile.getSubNodeByItemId('ITEM', 'main'),
                        cursor = ood.use(src).css('cursor'),
                        t = profile.properties,
                        h, w, mh, mw, offset1, offset2;

                    profile.pos = item.pos;

                    if (t.type == 'vertical') {
                        h = profile._cur = o.height();
                        mh = m.height();
                        if (item.pos == 'before') {
                            offset1 = h - item.min;
                            offset2 = Math.round(item.max ? Math.min(parseFloat(item.max) - h, (mh - main.min)) : (mh - main.min));
                        } else {
                            offset1 = Math.round(item.max ? Math.min(parseFloat(item.max) - h, (mh - main.min)) : (mh - main.min));
                            offset2 = h - item.min;
                        }

                        ood.use(src).startDrag(e, {
                            dragType: 'copy',
                            targetReposition: false,
                            verticalOnly: true,
                            maxTopOffset: offset1,
                            maxBottomOffset: offset2,
                            dragCursor: cursor,
                            // IE8 bug
                            targetWidth: ood.browser.ie ? ood.use(src).offsetWidth() : null,
                            targetHeight: ood.browser.ie ? ood.use(src).offsetHeight() : null,
                            targetCallback: ood.browser.ie ? function (n) {
                                n.tagClass('-(top|bottom)', false)
                            } : null
                        });
                    } else {
                        w = profile._cur = o.width();
                        mw = m.width();
                        if (item.pos == 'before') {
                            offset1 = w - item.min;
                            offset2 = Math.round(item.max ? Math.min(parseFloat(item.max) - w, (mw - main.min)) : (mw - main.min));
                        } else {
                            offset1 = Math.round(item.max ? Math.min(parseFloat(item.max) - w, (mw - main.min)) : (mw - main.min));
                            offset2 = w - item.min;
                        }

                        ood.use(src).startDrag(e, {
                            dragType: 'copy',
                            targetReposition: false,
                            horizontalOnly: true,
                            maxLeftOffset: offset1,
                            maxRightOffset: offset2,
                            dragCursor: cursor,
                            // IE8 bug
                            targetWidth: ood.browser.ie ? ood.use(src).offsetWidth() : null,
                            targetHeight: ood.browser.ie ? ood.use(src).offsetHeight() : null,
                            targetCallback: ood.browser.ie ? function (n) {
                                n.tagClass('-(left|right)', false)
                            } : null
                        });
                    }

                    profile._limited = 0;
                },
                onDrag: function (profile, e, src) {
                    var t = profile.properties,
                        d = ood.DragDrop,
                        p = ood.DragDrop._profile,
                        b = 0;
                    if (t.type == 'vertical') {
                        if ((p.y <= p.restrictedTop) || (p.y >= p.restrictedBottom)) b = true;
                    } else {
                        if (p.x <= p.restrictedLeft || p.x >= p.restrictedRight) b = true;
                    }

                    if (b) {
                        if (!profile._limited) {
                            p.proxyNode.addClass('ood-alert');
                            profile._limited = true;
                        }
                    } else {
                        if (profile._limited) {
                            p.proxyNode.removeClass('ood-alert');
                            profile._limited = 0;
                        }
                    }

                },
                onDragstop: function (profile, e, src) {
                    var t = profile.properties,
                        height = t.height,
                        width = t.width,
                        o = ood.use(src).parent(),
                        r = profile.getRoot(),
                        item = profile.getItemByDom(src),
                        sum = 0, cur,
                        innerW = null, innerH = null, mainItem;
                    for (var i = 0, l = t.items.length; i < l; i++) {
                        if (!t.items[i].hidden) sum += t.items[i].size || 80;
                        if (t.items[i].id == "main") mainItem = t.items[i];
                    }
                    //add offset and refresh
                    if (t.type == 'vertical') {
                        innerH = r.height();
                        //use size to ignore onresize event once
                        item._size = profile._cur + (profile.pos == 'before' ? 1 : -1) * ood.DragDrop.getProfile().offset.y
                        o.height(profile.$isEm(height) ? profile.$px2em(item._size, o) + 'em' : item._size);
                        cur = sum * item._size / innerH;
                    } else {
                        innerW = r.width();
                        item._size = profile._cur + (profile.pos == 'before' ? 1 : -1) * ood.DragDrop.getProfile().offset.x
                        o.width(profile.$isEm(width) ? profile.$px2em(item._size, o) + 'em' : item._size);
                        cur = sum * item._size / innerW;
                    }
                    // always - main
                    mainItem.size -= cur - item.size;
                    item.size = cur;
                    //use size to ignore onresize event once
                    // use px here,  _onresize handle em things
                    ood.UI.$tryResize(profile, innerW, innerH, true);
                    profile._limited = 0;
                }
            },
            CMD: {
                onMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != "left") return;
                    var t = profile.properties,
                        itemId = profile.getSubId(src),
                        item = profile.getItemByDom(src),
                        r = profile.getRoot(),
                        main = profile.getItemByItemId('main'),
                        m = profile.getSubNodeByItemId('ITEM', 'main'),
                        o = profile.getSubNode('ITEM', itemId),
                        panel = profile.getSubNode('PANEL', itemId),
                        move = profile.getSubNode('MOVE', itemId),
                        _handlerSize = (item.locked ? 0 : t.type == 'vertical' ? move.offsetHeight() : move.offsetWidth());

                    if (t.type == 'vertical') {
                        // restore resize mode
                        if (item.folded) {
                            // if(item._size <= m.height() - main.min + _handlerSize){
                            //restore h
                            o.height(item._size);
                            panel.show();

                            item.folded = false;
                            //set appearance
                            if (item.pos == 'before')
                                ood.use(src).replaceClass(/bottom/g, 'top');
                            else
                                ood.use(src).replaceClass(/top/g, 'bottom');

                            //hidden 'move'
                            if (!item.locked) move.css('cursor', 'n-resize');
                            profile.getSubNode('MOVE').tagClass('-checked', false);
                            // }else
                            //    ood.message('no enough space!');
                            // to min and fix mode
                        } else {
                            o.height(_handlerSize);
                            panel.hide();

                            item.folded = true;
                            if (item.pos == 'before')
                                ood.use(src).replaceClass(/top/g, 'bottom');
                            else
                                ood.use(src).replaceClass(/bottom/g, 'top');

                            if (!item.locked)
                                move.css('cursor', 'default');
                            profile.getSubNode('MOVE').tagClass('-checked');
                        }
                        ood.UI.$tryResize(profile, null, r.height(), true);
                    } else {
                        if (item.folded) {
                            // if(item._size <= m.width()-main.min + _handlerSize){
                            o.width(item._size);
                            panel.show();
                            item.folded = false;
                            if (item.pos == 'before')
                                ood.use(src).replaceClass(/right/g, 'left');
                            else
                                ood.use(src).replaceClass(/left/g, 'right');

                            if (!item.locked) move.css('cursor', 'w-resize');
                            profile.getSubNode('MOVE').tagClass('-checked', false);
                            //}else
                            //    ood.message('no enough space!');
                        } else {
                            o.width(_handlerSize);
                            panel.hide();
                            item.folded = true;
                            if (item.pos == 'before')
                                ood.use(src).replaceClass(/left/g, 'right');
                            else
                                ood.use(src).replaceClass(/right/g, 'left');


                            if (!item.locked)
                                move.css('cursor', 'default');
                            profile.getSubNode('MOVE').tagClass('-checked');
                        }
                        ood.UI.$tryResize(profile, r.width(), null, true);
                    }

                    return false;
                }
            },
            PANEL: {
                onClick: function (profile, e, src) {
                    var p = profile.properties,
                        item = profile.getItemByDom(src);
                    if (p.disabled || item.disabled) return false;
                    if (profile.onClickPanel)
                        return profile.boxing().onClickPanel(profile, item, e, src);
                }
            }
        },
        DataModel: {
            // 主题属性
            theme: {
                caption: ood.getResText("DataModel.theme") || "主题",
                ini: 'light',
                listbox: ['light', 'dark'],
                action: function (v) {
                    this.boxing().setTheme(v);
                }
            },
            // 响应式设计属性
            responsive: {
                caption: ood.getResText("DataModel.responsive") || "响应式",
                ini: true,
                action: function (v) {
                    if (v) {
                        this.boxing().adjustLayout();
                        // 添加resize监听器
                        ood.Event.on(window, 'resize.' + this.get(0).serialId, this.boxing().adjustLayout.bind(this.boxing()));
                    } else {
                        // 移除resize监听器
                        ood.Event.off(window, 'resize.' + this.get(0).serialId);
                    }
                }
            },

            rotate: {
                ini: null,
                caption: ood.getResText("DataModel.rotate") || "旋转"
            },
            selectable: {
                caption: ood.getResText("DataModel.selectable") || "可选择",
                ini: true
            },
            navComboType: {
                caption: ood.getResText("DataModel.navComboType") || "导航组合类型",
                listbox: ['custom', 'galleryNav', 'menuBarNav', 'treeNav', 'foldingNav'],
                ini: 'custom'
            },
            disabled: {
                ini: null,
                caption: ood.getResText("DataModel.disabled") || "禁用"
            },
            position: {
                caption: ood.getResText("DataModel.position") || "位置",
                ini: 'absolute'
            },
            type: {
                caption: ood.getResText("DataModel.type") || "类型",
                listbox: ['vertical', 'horizontal'],
                ini: 'horizontal',
                action: function (value, ovalue) {
                    if (value != ovalue) {
                        var self = this, auto = 'auto',
                            nodes2 = self.getSubNode('ITEM', true),
                            nodes1 = self.getSubNode('MOVE', true),
                            nodes3 = self.getSubNode('CMD', true);
                        nodes1.merge(nodes2).merge(nodes3);

                        if (value == 'vertical') {
                            nodes1.replaceClass(/(-left)(\b)/ig, '-top$2');
                            nodes1.replaceClass(/(-right)(\b)/ig, '-bottom$2');
                            nodes2.each(function (o) {
                                ood(o).height(ood(o).width());
                            })
                                .css({left: 0, top: auto, right: auto, bottom: auto})
                            ;
                        } else {
                            nodes1.replaceClass(/(-top)(\b)/ig, '-left$2');
                            nodes1.replaceClass(/(-bottom)(\b)/ig, '-right$2');
                            nodes2.each(function (o) {
                                ood(o).width(ood(o).height());
                            })
                                .css({left: auto, top: 0, right: auto, bottom: auto})
                            ;

                        }

                        self.adjustSize();
                    }
                }
            },
            dock: {
                caption: ood.getResText("DataModel.dock") || "停靠",
                ini: 'fill'
            },
            listKey: null,
            width: {
                caption: ood.getResText("DataModel.width") || "宽度",
                $spaceunit: 1,
                ini: '18em'
            },
            height: {
                caption: ood.getResText("DataModel.height") || "高度",
                $spaceunit: 1,
                ini: '18em'
            },
            dragSortable: {
                ini: null,
                caption: ood.getResText("DataModel.dragSortable") || "拖拽排序"
            },

            flexSize: {
                caption: ood.getResText("DataModel.flexSize") || "弹性大小",
                ini: false,
                action: function () {
                    this.adjustSize();
                }
            },


            transparent: {
                caption: ood.getResText("DataModel.transparent") || "透明",
                ini: false,
                action: function (v) {
                    this.getRoot().tagClass('-trans', !!v);
                }
            },
            items: {
                caption: ood.getResText("DataModel.items") || "项目",
                ini: []
            }
        },
        EventHandlers: {
            onClickPanel: function (profile, item, e, src) {
            }
        },
        _adjustItems2: function (items, pos) {
            var arr = [];
            //arrage items
            ood.arr.each(items, function (o) {
                if (o.id != 'main') {
                    arr.push(o = ood.isHash(o) ? o : {id: '' + o});
                    o.pos = pos;
                }
            });

            //set the items to default value
            ood.arr.each(arr, function (o) {
                o.id = ood.isStr(o.id) ? o.id : ood.id();
                o.min = o.min || 10;
                o._size = o.size = Math.round(parseFloat(o.size)) || 80;
                o.locked = typeof o.locked == 'boolean' ? o.locked : false;
                o.folded = typeof o.folded == 'boolean' ? o.folded : false;
                o.hidden = typeof o.hidden == 'boolean' ? o.hidden : false;
                o.cmd = typeof o.cmd == 'boolean' ? o.cmd : true;
            });
            return arr;
        },
        _adjustItems: function (items) {
            var main, before = [], after = [], watershed = 0;

            //arrage items
            ood.arr.each(items, function (o, i) {
                o = ood.copy(o);
                if (o.id == 'main') {
                    main = o;
                    watershed = i;
                } else {
                    if (o.pos == 'before') {
                        before.push(o);
                    } else if (o.pos == 'after') {
                        after.push(o);
                    } else if (watershed) {
                        o.pos = 'after';
                        after.push(o);
                    } else {
                        o.pos = 'after';
                        before.push(o);
                    }
                }
            });

            main = main || {};
            main.id = 'main';
            main.min = main.min || 10;
            // no _size in main
            main.size = Math.round(parseFloat(main.size)) || 80;

            //reset items
            items.length = 0;
            ood.arr.insertAny(items, this._adjustItems2(before, 'before'));
            ood.arr.insertAny(items, main);
            ood.arr.insertAny(items, this._adjustItems2(after, 'after'));

            return items;
        },
        _prepareData: function (profile) {
            var prop = profile.properties;
            if (prop.layoutType) {
                prop.type = prop.layoutType;
            }
            if (!prop.items || !ood.isArr(prop.items))
                prop.items = ood.clone([
                    {id: 'before', pos: 'before', locked: false, size: 120, min: 150, max: 600},
                    {id: 'after', pos: 'after', locked: false, size: 120, min: 150, max: 600}
                ]);
            prop.items = this._adjustItems(prop.items);
            var data = arguments.callee.upper.call(this, profile);
            data._trans = data.transparent ? profile.getClass("KEY", "-trans") : "";
            return data;
        },
        _prepareItems: function (profile, items) {
            var data = arguments.callee.upper.apply(this, arguments);
            var p = profile.properties;
            if (p.layoutType) {
                p.type = p.layoutType;
            }
            ood.arr.each(items, function (o) {
                delete o.caption;
            });
            return data;
        },
        _prepareItem: function (profile, data, item) {
            var p = profile.properties,
                width = p.width, height = p.height, t;

            if (data.id == 'main') {
                data.cls1 = profile.getClass('ITEM', '-main');
                data.cls2 = profile.getClass('MOVE', '-main');
                data.cls3 = profile.getClass('CMD', '-main');
            } else {

                var pos;
                if (p.type == 'vertical') {
                    data.clsmovebg = "ood-uiborder-t ood-uiborder-b";
                    if (data.pos == 'before')
                        pos = 'top';
                    else
                        pos = 'bottom';
                } else {
                    data.clsmovebg = "ood-uiborder-l ood-uiborder-r";
                    if (data.pos == 'before')
                        pos = 'left';
                    else
                        pos = 'right';
                }

                data.cls1 = profile.getClass('ITEM', '-' + pos);
                data.cls2 = profile.getClass('MOVE', '-' + pos);
                data.cls3 = profile.getClass('CMD', '-' + pos);
                data._fi_cls3 = "ood-icon-arrow" + pos;

                data.display = data.hidden ? 'display:none' : '';
                data._cursor = data.locked ? 'default' : (p.type == 'vertical') ? 'n-resize' : 'w-resize';
                data.cmdDisplay = (data.cmd && !data.locked) ? '' : 'display:none';
                data.moveDisplay = !data.locked ? '' : 'display:none';
            }
            data._bginfo = "";
            if (t = data.panelBgClr || p.panelBgClr)
                data._bginfo += "background-color:" + t + ";";
            if (t = data.panelBgImg || p.panelBgImg)
                data._bginfo += "background-image:url(" + ood.adjustRes(t) + ");";
            if (t = data.panelBgImgPos || p.panelBgImgPos)
                data._bginfo += "background-position:" + t + ";";
            if (t = data.panelBgImgRepeat || p.panelBgImgRepeat)
                data._bginfo += "background-repeat:" + t + ";";
            if (t = data.panelBgImgAttachment || p.panelBgImgAttachment)
                data._bginfo += "background-attachment:" + t + ";";
            if (ood.isStr(data.overflow))
                data._overflow = data.overflow.indexOf(':') != -1 ? (data.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");
            else if (ood.isStr(p.overflow))
                data._overflow = p.overflow.indexOf(':') != -1 ? (p.overflow) : (p.overflow ? ("overflow:" + p.overflow) : "");
        },
        RenderTrigger: function () {
            var t, profile = this;
            ood.arr.each(profile.properties.items, function (item) {
                if (item.id != 'main') {
                    if (item.folded && (t = profile.getSubIdByItemId(item.id))) {
                        item.folded = false;
                        profile.getSubNode('CMD', t).onMousedown();
                    }
                }
            });
        },
        _onresize: function (profile, width, height) {
            var t = profile.properties, itemId,
                key = profile.keys.ITEM,
                panel = profile.keys.PANEL,
                main = profile.getItemByItemId('main'),
                mainmin = main.min || 10,
                pct = t.flexSize,
                sum = 0,

                move, _handlerSize,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                fzrate = profile.getEmSize() / profile.getRoot()._getEmSize();

            if (width) width = profile.$px(width);
            if (height) height = profile.$px(height);

            var obj = {}, obj2 = {};
            // **keep the original size
            //,obj3={};
            ood.arr.each(t.items, function (o) {
                itemId = profile.getSubIdByItemId(o.id);
                obj[itemId] = {};
                obj2[itemId] = {};
//                obj3[itemId] = o;
                if (pct && !o.hidden) sum += o.size || 80;
            });

            var fun = function (prop, w, width, left, right, offset, forceoffset) {
                var _t, m, m1, itemId, temp1 = 0, temp2 = 0, temp = 0, blocknumb = 0, offsetbak = offset;

                ood.arr.each(prop.items, function (o) {
                    if (o.id == 'main') return;
                    if (o.pos == 'before') {
                        itemId = profile.getSubIdByItemId(o.id);
                        move = profile.getSubNode('MOVE', itemId),
                            _handlerSize = (o.locked ? 0 : t.type == 'vertical' ? move.offsetHeight() : move.offsetWidth())
                        if (o.hidden) {
                            m = 0;
                            obj2[itemId][width] = Math.round(pct ? parseFloat(w * Math.min(1, (o.size / sum))) : o._size);
                        } else if (o.folded) {
                            m = obj2[itemId][width] = _handlerSize;
                        } else {
                            blocknumb++;
                            m = m1 = Math.round(pct ? parseFloat(w * Math.min(1, (o.size / sum))) : o._size);
                            if (m > offset + o.min) {
                                m -= offset;
                            } else {
                                offset = m - o.min;
                                m = o.min;
                            }
                            m -= forceoffset;
                            m = Math.max(m, _handlerSize);
                        }
                        obj2[itemId][left] = temp1;
                        temp1 += m;
                        obj[itemId][left] = 0;
                        obj[itemId][width] = m - _handlerSize;
                        obj2[itemId][right] = obj[itemId][right] = 'auto';
                        obj2[itemId][width] = m;
                        mainmin += _handlerSize;
                    }
                });
                ood.arr.each(prop.items, function (o) {
                    if (o.id == 'main') return;
                    if (o.pos == 'after') {
                        itemId = profile.getSubIdByItemId(o.id);
                        move = profile.getSubNode('MOVE', itemId),
                            _handlerSize = (o.locked ? 0 : t.type == 'vertical' ? move.offsetHeight() : move.offsetWidth())
                        if (o.hidden) {
                            m = 0;
                            obj2[itemId][width] = Math.round(pct ? parseFloat(w * Math.min(1, (o.size / sum))) : o._size);
                        } else if (o.folded) {
                            m = obj2[itemId][width] = _handlerSize;
                        } else {
                            blocknumb++;
                            m = m1 = Math.round(pct ? parseFloat(w * Math.min(1, (o.size / sum))) : o._size);
                            if (m > offset + o.min) {
                                m -= offset;
                            } else {
                                offset = m - o.min;
                                m = o.min;
                            }
                            m -= forceoffset;
                            m = Math.max(m, _handlerSize);
                        }
                        obj2[itemId][right] = temp2;
                        temp2 += m;
                        obj[itemId][right] = 0;
                        obj[itemId][width] = m - _handlerSize;
                        obj2[itemId][left] = obj[itemId][left] = 'auto';
                        obj2[itemId][width] = m;
                        mainmin += _handlerSize;
                    }
                }, null, true);
                temp = temp1 + temp2;

                //set main
                if (w - temp >= mainmin || forceoffset) {
                    _t = profile.getSubIdByItemId('main');
                    obj2[_t][width] = obj[_t][width] = w - temp;
                    obj2[_t][left] = temp1;
                } else {
                    var args = ood.toArr(arguments);
                    // second time only
                    if (!offsetbak) {
                        args[args.length - 2] = (mainmin - (w - temp)) / blocknumb;
                    }
                    // third time only
                    else {
                        args[args.length - 2] = offsetbak;
                        args[args.length - 1] = (mainmin - (w - temp)) / blocknumb;
                    }
                    //second time
                    fun.apply(null, args);
                }
            };

            if (t.type != 'vertical') {
                if (!ood.isNull(width)) {
                    //get left
                    fun(t, width, 'width', 'left', 'right', 0, 0);
//                    ood.each(obj2,function(o,i){
//                       if(o.width && !obj3[i].folded)obj3[i]._size=o.width;
//                    });
                }
                if (!ood.isNull(height)) {
                    ood.each(obj, function (o, id) {
                        obj2[id].height = o.height = height;
                    });
                }
            } else {
                if (!ood.isNull(height)) {
                    //get left
                    fun(t, height, 'height', 'top', 'bottom', 0, 0);
//                    ood.each(obj2,function(o,i){
//                        if(o.height  && !obj3[i].folded)obj3[i]._size=o.height;
//                    });
                }
                if (!ood.isNull(width)) {
                    ood.each(obj, function (o, id) {
                        obj2[id].width = o.width = width;
                    });
                }
            }
            var ff_w = function (o, emRate) {
                ood.arr.each("left,width,right".split(','), function (t) {
                    if (t in o) o[t] = profile.$forceu(o[t], 'em', emRate);
                });
            }, ff_h = function (o, emRate) {
                ood.arr.each("top,height,bottom".split(','), function (t) {
                    if (t in o) o[t] = profile.$forceu(o[t], 'em', emRate);
                });
            };
            //collect width/height in size
            ood.each(obj2, function (o, id) {
                var p = profile.getSubNode('PANEL', id),
                    i = profile.getSubNode('ITEM', id);

                if (us > 0) {
                    var pfz = us > 0 ? p._getEmSize(fzrate) : null,
                        ifz = us > 0 ? i._getEmSize(fzrate) : null;
                    ff_w(obj[id], ifz);
                    ff_w(obj2[id], ifz);
                    ff_h(obj[id], pfz);
                    ff_h(obj2[id], pfz);
                }
                p.cssRegion(obj[id], true);

                if (obj[id].width) {
                    ood.UI._adjustConW(profile, p, obj[id].width);
                }

                i.cssRegion(obj2[id]);
            });
        },
        LayoutTrigger: function () {
            var prop = this.properties,
                boxing = this.boxing();

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('layout-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                boxing.adjustLayout();
                // 注意：窗口大小变化的监听需要在应用层面处理
                // OOD框架有自己的事件处理机制
            }

            // 初始化可访问性
            boxing.enhanceAccessibility();
        }
    }
});
