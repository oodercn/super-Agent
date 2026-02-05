ood.Class("ood.UI.MenuBar", ["ood.UI", "ood.absList"], {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {
                    "id": "menu1",
                    "sub": [{"id": "normal", "caption": "normal"},
                        {"id": "disabled", "caption": "disabled", "disabled": true},
                        {"id": "image", "caption": "image", imageClass: "ri-image-line"},
                        {"type": "split"},
                        {"id": "checkbox 1", "caption": "checkbox 1", "type": "checkbox"},
                        {"id": "checkbox 2", "caption": "checkbox 2", "type": "checkbox"}],
                    "caption": "menu1"
                }
            ],
            theme: 'light',
            responsive: true,
            handler: true,
            hAlign: 'left',
            vAlign: 'middle',
            dock: 'top',
            border: 'outset',
            autoShowTime: 200,
            value: '',
            autoItemColor: false,
            autoIconColor: false,
            autoFontColor: false
        },

        // 现代化功能：设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                var root = profile.getRoot();
                
                // 保存主题到属性
                profile.properties.theme = theme;
                
                // 移除所有主题类
                root.removeClass('menubar-dark menubar-moonify menubar-lightblue menubar-darkblue menubar-hc');
                
                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('menubar-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('menubar-hc');
                } else if (theme === 'moonify') {
                    root.addClass('menubar-moonify');
                } else if (theme === 'lightblue') {
                    root.addClass('menubar-lightblue');
                } else if (theme === 'darkblue') {
                    root.addClass('menubar-darkblue');
                } else {
                    // 默认浅色主题
                    root.removeClass('menubar-dark menubar-moonify menubar-lightblue menubar-darkblue menubar-hc');
                }
                
                // 保存主题设置
                localStorage.setItem('menubar-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('menubar-theme') || 'light';
        },
        
        // 切换主题
        toggleTheme: function(nextTheme) {
            var currentTheme = this.getTheme();
            var themes = ['light', 'dark', 'moonify', 'lightblue', 'darkblue'];
            
            if (nextTheme && themes.indexOf(nextTheme) !== -1) {
                this.setTheme(nextTheme);
            } else {
                // 循环切换主题
                var currentIndex = themes.indexOf(currentTheme);
                var nextIndex = (currentIndex + 1) % themes.length;
                this.setTheme(themes[nextIndex]);
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    items = profile.getSubNode('ITEM', true),
                    captions = profile.getSubNode('CAPTION', true),
                    icons = profile.getSubNode('ICON', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('menubar-mobile');
                    
                    // 平板模式调整
                    items.css({
                        'margin': '0 0.125em',
                        'padding': '0.3em 0.6em'
                    });
                    captions.css({
                        'font-size': '0.9em'
                    });
                } else {
                    root.removeClass('menubar-mobile');
                    
                    // 恢复桌面布局
                    items.css({
                        'margin': '',
                        'padding': ''
                    });
                    captions.css({
                        'font-size': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('menubar-tiny');
                    // 隐藏图标，只显示文字
                    icons.css('display', 'none');
                    captions.css({
                        'font-size': '0.8em'
                    });
                } else {
                    root.removeClass('menubar-tiny');
                    
                    // 恢复图标显示
                    icons.css('display', '');
                    if (width >= 768) {
                        captions.css('font-size', '');
                    }
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    border = profile.getSubNode('BORDER'),
                    items = profile.getSubNode('ITEM', true);

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application'
                });
                
                border.attr({
                    'role': 'menubar',
                    'aria-label': '菜单栏'
                });
                
                // 为菜单项添加ARIA属性
                items.each(function(item) {
                    var itemNode = ood(item);
                    var itemId = itemNode.id();
                    if (itemId) {
                        var subId = itemId.split('_').pop();
                        var itemData = profile.getItemByDom(item);
                        
                        itemNode.attr({
                            'role': 'menuitem',
                            'aria-label': '菜单项: ' + (itemData ? itemData.caption : subId),
                            'tabindex': '0'
                        });
                        
                        // 如果有子菜单，添加相应属性
                        if (itemData && itemData.sub) {
                            itemNode.attr({
                                'aria-haspopup': 'true',
                                'aria-expanded': 'false'
                            });
                        }
                    }
                });

                // 键盘事件处理需要在应用层面实现
                // OOD框架的事件系统与标准DOM事件API不同
            });
        },
        updateItem: function (subId, options) {
            var self = this,
                profile = self.get(0),
                items = profile.properties.items;
            //the root
            if (ood.arr.subIndexOf(items, "id", subId) != -1)
                arguments.callee.upper.call(self, subId, options);
            //try each sub popmenu
            else {
                var ok = 0;
                ood.each(profile.$allPops, function (o) {
                    o.updateItem(subId, options);
                    ok = 1;
                });
                if (!ok)
                    arguments.callee.upper.call(self, subId, options);
            }
            return self;
        },
        _pop: function (item, src) {
            var self = this,
                profile = self.get(0);
            //hide first, ignoreEffects false,true
            if (profile.$curPop) self.hide();

            if (!item.sub) return;

            if (profile.beforePopMenu && false == profile.boxing().beforePopMenu(profile, item, src)) {
                return;
            } else {
                ood.use(src).tagClass('-active');
                var menu,
                    id = item.id,
                    pro = profile.properties,
                    pid = pro.parentID || ood.ini.$rootContainer,
                    all = '$allPops';

                profile.$curPop = id;
                profile.$curElem = src;
                profile.$menuPop = id;

                profile[all] = profile[all] || {};
                if (!profile[all][id]) {
                    var callback = function (sub) {
                        var hash = {
                            position: 'absolute',
                            items: sub,
                            autoHide: !!pro.autoShowTime,
                            minWidth: '200px',  // 增加最小宽度
                            padding: '0.5em 0', // 增加内边距
                            itemSpacing: '0.5em' // 增加菜单项间距
                        };
                        if (pro.showEffects) hash.showEffects = pro.showEffects;
                        if (pro.hideEffects) hash.hideEffects = pro.hideEffects;
                        var menu = ood.create('PopMenu', hash);
                        // 添加子菜单样式
                        menu.getRoot().css({
                            'min-width': '200px',
                            'padding': '0.5em 0',
                            'border-radius': '6px',
                            'box-shadow': '0 4px 12px rgba(0,0,0,0.15)'
                        });
                        // 设置菜单项样式
                        menu.getSubNode('ITEM').css({
                            'padding': '0.5em 1em',
                            'margin': '0.25em 0',
                            'line-height': '1.5'
                        });
                        profile.getSubNode('POOL').append(menu);
                        menu.onHide(function (pro) {
                            self.hide(false);
                        }).onMenuSelected(function (pro, item, src) {
                            return profile.boxing().onMenuSelected(profile, pro, item, src);
                        }).onShowSubMenu(function (pro, item, src) {
                            return profile.boxing().onShowSubMenu(profile, pro, item, src);
                        });
                        menu.get(0).$hideMenuPool = profile.getSubNode('POOL');
                        menu.get(0)[all] = profile[all];
                        profile[all][id] = menu;
                    }

                    if (ood.isArr(item.sub) && item.sub.length)
                        callback(item.sub);
                    else if (profile.onGetPopMenu) {
                        var r = profile.boxing().onGetPopMenu(profile, item, callback);
                        if (ood.isArr(r) && r.length)
                            callback(item.sub = r);
                    }
                }
                // popmenu
                if (profile[all][id])
                    profile[all][id].pop(ood(src), 1, pid ? ood.get(profile, ["host", pid]) ? profile.host[pid].getContainer() : ood(pid) : null);

                return false;
            }
        },
        _afterInsertItems: function () {
            this.clearPopCache();
        },
        hide: function (ignoreEffects) {
            var profile = this.get(0), menu,
                id = profile.$curPop,
                node = profile.$curElem;

            if (menu = profile.$allPops[id]) {
                //To avoid trigger recursive call
                if (false !== arguments[0])
                    menu.hide(false, ignoreEffects);
                // collect
                profile.getSubNode('POOL').append(menu.reBoxing());
                ood([node]).tagClass('-active', false);
            }
            profile.$menuPop = profile.$curPop = profile.$curElem = null;
        },
        clearPopCache: function () {
            var profile = this.get(0);
            if (profile.renderId) {
                profile.getSubNode('POOL').empty();
                profile.$allPops = profile.$curPop = profile.$curElem = null;
            }
        }
    },
    Initialize: function () {


    },
    Static: {
        _nocap2tip: true,
        Templates: {
            tagName: 'div',
            className: '{_className};{_vAlign}',
            style: '{_style}',
            POOL: {
                tagName: 'div'
            },
            BORDER: {
                // className: 'ood-uibar ood-uiborder-outset ood-uiborder-radius',
                className: 'ood-uibar ood-uiborder-radius',
                tagName: 'div',
                style: 'border:{borderStyle};{_hAlign}',
                LIST: {
                    tagName: 'div',
                    HANDLER: {
                        className: 'oodfont',
                        $fonticon: 'ood-icon-placeholder',
                        style: '{handler}'
                    },
                    ITEMS: {
                        $order: 1,
                        text: "{items}"
                    }
                }
            },
            $submap: {
                items: {
                    ITEM: {
                        style: '{itemStyle}{_itemDisplay}{_itemColor}',
                        className: 'ood-uimenu',
                        ITEMI: {
                            ITEMC: {
                                ITEMA: {
                                    tabindex: '{_tabindex}',
                                    className: ' {typeCls} {disabled}',
                                    ICON: {
                                        $order: 1,
                                        className: 'oodcon {imageClass}  {picClass}',
                                        style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle} {_iconColor}',
                                        text: '{iconFontCode}'
                                    },
                                    CAPTION: {
                                        $order: 2,
                                        text: '{caption}',
                                        style: '{captionDisplay}  {_fontColor}'
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
                left: 0,
                top: 0
            },
            POOL: {
                width: 0,
                height: 0,
                visibility: 'hidden',
                position: 'absolute',
                left: '-10000px',
                top: '-10000px'
            },

            BORDER: {
                left: 0,
                top: 0
            },
            HANDLER: {
                height: '100%',
                width: '0.5em',
                background: 'url(' + ood.ini.img_handler + ') repeat-y left top',
                cursor: 'move',
                'vertical-align': 'middle'
            },
            LIST: {
                padding: '.125em'
            },
            ITEMS: {
                'vertical-align': 'middle'
            },
            ITEM: {
                'white-space': 'nowrap',
                'vertical-align': 'top',
                overflow: 'hidden',
               // margin: '0 .25em .5em .25em',  /* 增加底部间距 */
                //padding: '0.25em 0.5em',       /* 增加内边距 */
               // 'line-height': '1.5'           /* 增加行高 */
            },
            'ITEM *': {
                cursor: 'pointer'
            },
            ITEMI: {
                'padding': '0 .5em',          /* 增加水平内边距 */
                'vertical-align': 'middle',   /* 更好的垂直对齐 */
                'line-height': '1.5'          /* 增加行高 */
            },
            ITEMC: {
                'padding': '.5em 0',         /* 增加垂直内边距 */
                'vertical-align': 'middle',  /* 更好的垂直对齐 */
                'min-height': '1.5em'        /* 确保最小高度 */
            },
            ITEMA: {
                display: ood.$inlineBlock
            },
            CAPTION: {
                'vertical-align': 'middle',
                'font-size': '1em',
                'font-weight': '500',
                'transition': 'all 0.2s ease',
                'padding': '0 0.5em',        /* 增加水平内边距 */
                'line-height': '1.5',        /* 增加行高 */
                'margin': '0.125em 0'        /* 增加垂直外边距 */
            },

            ICON: {
                'transition': 'all 0.2s ease',
                'margin-right': '0.25em'
            },



            

            

            
            // 移动端样式
            'menubar-mobile ITEM': {
                'margin': '0 0.125em',
                'padding': '0.3em 0.6em'
            },
            'menubar-mobile CAPTION': {
                'font-size': '0.9em'
            },
            
            // 小屏幕样式
            'menubar-tiny ICON': {
                'display': 'none'
            },
            'menubar-tiny CAPTION': {
                'font-size': '0.8em'
            }
        },
        Behaviors: {
            ITEM: {
                onMouseover: function (profile, e, src) {
                    var p = profile.properties, ns = src;
                    if (p.disabled) return;
                    var item = profile.getItemByDom(src),
                        itemId = item.id;
                    if (item.disabled) return;
                    ood.use(ns).tagClass('-hover');
                    if (profile.$menuPop) {
                        if (profile.$menuPop != itemId) {
                            //show current popmenu
                            profile.boxing()._pop(item, ns);
                        }
                    } else {
                        if (p.autoShowTime) {
                            ood.resetRun(profile.$xid + ':autoShowTime', function () {
                                profile.boxing()._pop(item, ns);
                            }, p.autoShowTime);
                        }
                    }
                },
                onMouseout: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return;
                    var item = profile.getItemByDom(src);
                    if (item.disabled) return;
                    if (item.id != p.value) {
                        ood.use(src).tagClass('-hover', false);
                        ood.use(src).tagClass('-active', false);
                    }


                    if (p.autoShowTime) {
                        var pop = profile.$allPops;
                        if (pop = pop && pop[profile.$curPop]) {
                            var node = pop.get(0).getRoot(),
                                p1 = ood.Event.getPos(e),
                                size = node.cssSize(),
                                add = 3,
                                p2 = node.offset();

                            if (p1.left > p2.left && p1.top > p2.top - add && p1.left < p2.left + size.width && p1.top < p2.top + size.height) {
                            } else
                                pop.hide();
                        }
                        ood.resetRun(profile.$xid + ':autoShowTime', null);
                    }
                },
                onMousedown: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return;
                    var item = profile.getItemByDom(src),
                        itemId = item.id;
                    if (item.disabled) return;
                    ood.use(src).tagClass('-active');
                    p.value = item.id;

                    // if poped, stop to trigger document.body's onmousedown event
                    return profile.boxing()._pop(item, src);
                },
                onMouseup: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    if (profile.$menuPop != item.id)
                        ood.use(src).tagClass('-active', false);
                },
                onKeydown: function (profile, e, src) {
                    var keys = ood.Event.getKey(e), key = keys.key, shift = keys.shiftKey,
                        cur = ood(src),
                        first = profile.getRoot().nextFocus(true, true, false),
                        last = profile.getRoot().nextFocus(false, true, false);

                    switch (ood.Event.getKey(e).key) {
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
                            cur.onMousedown();
                            break;
                    }
                },
                onClick: function (profile, e, src) {
                    var item = profile.getItemByDom(src), p = profile.properties;
                    ood.use(src).tagClass('-active');
                    var ns = profile.boxing();
                    ood.each(ns.getItems(), function (o) {
                        if (o.id != item.id) {
                            ns.getSubNodeByItemId('ITEM', o.id).tagClass('-hover', false);
                            ns.getSubNodeByItemId('ITEM', o.id).tagClass('-active', false);
                        }
                    });
                    p.value = item.id;
                    if (profile.$menuPop != item.id)
                        if (profile.onMenuBtnClick)
                            profile.boxing().onMenuBtnClick(profile, item, src);
                }
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'moonify', 'lightblue', 'darkblue'],
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
            
            listKey: null,
            dragSortable: null,
            autoTips: false,
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
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            iconColors: null,
            itemColors: null,
            fontColors: null,
            value: {
                ini: '',
                action: function () {
                }
            },
            // 允许设置高度
            height: {
                $spaceunit: 1,
                ini: 'auto',
                action: function(value) {
                    this.getSubNode('BORDER').css('height', value);
                    this.getSubNode('LIST').css('height', value);
                }
            },

            width: {
                $spaceunit: 1,
                ini: 'auto'
            },
            border: {
                ini: 'outset',
                listbox: ['none', 'flat', 'inset', 'outset', 'groove', 'ridge'],
                action: function (v) {
                    var ns = this,
                        p = ns.properties;
                    ns.box._borderType(ns, v, p.sideBarStatus, p.sideBarType.split('-'), true);
                }
            },

            parentID: '',

            hAlign: {
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode('BORDER').css('textAlign', v);
                }
            },
            vAlign: {
                ini: 'middle',
                listbox: ['top', 'middle', 'bottom'],
                action: function (v) {
                    this.getSubNode('POOL').css('vertical-align:', v);
                }
            },

            $hborder: 1,
            $vborder: 1,
            left: 0,
            top: 0,
            autoShowTime: 200,
            handler: {
                ini: true,
                action: function (v) {
                    this.getSubNode('HANDLER').css('display', v ? '' : 'none');
                }
            },
            position: 'absolute',
            dock: {
                ini: 'top',
                listbox: ['top', 'bottom', 'left', 'right']
            }
            // 移除了 items 属性定义，因为现在在 Instance 部分的 iniProp 对象中定义了默认值
        },
        LayoutTrigger: function () {
            var v = this.properties, nd = this.getSubNode("BORDER");
            v.$hborder = v.$vborder = nd._borderW('left');
            
            // 现代化功能初始化
            var boxing = this.boxing();
            
            // 初始化主题
            if (v.theme) {
                boxing.setTheme(v.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('menubar-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (v.responsive !== false) {
                boxing.adjustLayout();
                // 注意：窗口大小变化的监听需要在应用层面处理
                // OOD框架有自己的事件处理机制
            }
            
            // 初始化可访问性
            boxing.enhanceAccessibility();
        },
        EventHandlers: {
            onGetPopMenu: function (profile, item, callback) {
            },
            onMenuBtnClick: function (profile, item, src) {
            },
            beforePopMenu: function (profile, item, src) {
            },
            onShowSubMenu: function (profile, popProfile, item, src) {
            },
            onMenuSelected: function (profile, popProfile, item, src) {
            }
        },
        RenderTrigger: function () {
            if (this.properties.disabled) this.boxing().setDisabled(true, true);
        },
        _prepareData: function (profile) {
            var none = 'display:none;';
            var data = arguments.callee.upper.call(this, profile);
            var p = profile.properties, ns = this;
            data._hAlign = p.hAlign != 'left' ? ('text-align:' + p.hAlign + ';') : '';
            data._vAlign = p.vAlign != 'center' ? ('vertical-align:' + p.vAlign + ';') : '';
            data.handler = data.handler ? '' : none;

            ood.arr.each(data.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(data.items, item);
                }
                profile.boxing()._autoColor(item,index, p);
            })

            data._itemDisplay = data.hidden ? none : '';
            return data;
        }


    }
});