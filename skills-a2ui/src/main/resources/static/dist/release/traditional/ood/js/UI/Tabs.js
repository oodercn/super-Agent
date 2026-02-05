ood.Class("ood.UI.Tabs", ["ood.UI", "ood.absList", "ood.absValue"], {
    Instance: {

        iniProp: {
            items: [
                {id: 'a', caption: 'page1', imageClass: "ri-image-line"},
                {id: 'b', caption: 'page2'},
                {id: 'c', caption: 'page3'},
                {id: 'd', caption: 'page4', closeBtn: true, optBtn: 'ood-uicmd-opt', popBtn: true}
            ],
            autoFontColor: true,
            value: 'a',
            caption: 'TABS'
        },

        // 主题切换方法
        /**
         * 设置组件主题
         * @param {string} theme - 主题名称 (light/dark/high-contrast)
         * @returns {Object} 组件实例
         */
        setTheme: function (theme) {
            return this.each(function (profile) {
                // 验证并设置主题
                var root = profile.getRoot();
                // 移除所有主题类
                root.removeClass('tabs-light tabs-dark tabs-highcontrast');
                // 添加当前主题类
                root.addClass('gallery-' + theme);

                // 更新data-theme属性以支持CSS变量
                root.attr('data-theme', theme);

                // 更新响应式布局
                try {
                    profile.boxing()._checkResponsiveLayout();
                } catch (e) {
                    console.warn('Responsive layout check failed:', e);
                }

                // 持久化主题偏好
                try {
                    if (typeof localStorage !== 'undefined') {
                        localStorage.setItem('tabs-theme', theme);
                    }
                } catch (e) {
                    console.warn('Theme preference save failed:', e);
                }
            });
        },

        // 检查响应式布局
        _checkResponsiveLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width;

                // 移除所有响应式类
                root.removeClass('tabs-mobile tabs-tiny');

                // 应用响应式类
                if (width < 400) {
                    root.addClass('tabs-tiny');
                } else if (width < 600) {
                    root.addClass('tabs-mobile');
                }
            });
        },


        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('tabs-theme') || 'light';
        },

        // 切换暗黑模式
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },

        // 响应式布局调整
        adjustLayout: function (profile) {
            if (profile) {
                this.each(function (pf) {
                    if (pf.serialId === profile.serialId) {
                        this._onresize(pf, null, null, true);
                    }
                });
            } else {
                this.each(function (pf) {
                    if (pf.boxing()._onresize) {
                        pf.boxing()._onresize(pf, null, null, true);
                    }

                });
            }
            return this;
        },
        _setCtrlValue: function (value) {
            this.each(function (profile) {
                var id = profile.domId,
                    box = profile.boxing(),
                    uiv = box.getUIValue(),
                    prop = profile.properties,
                    dm = profile.box.$DataModel,
                    mcap = profile.getSubNode('MENUCAPTION'),
                    mcls = profile.getSubNode('MENUCLOSE'),
                    fold = function (itemId, arr) {
                        var subId = profile.getSubIdByItemId(itemId),
                            item = profile.getItemByItemId(itemId);
                        if (subId) {
                            arr.push(subId);
                            if (!dm.hasOwnProperty("noPanel") || !prop.noPanel) {
                                // hide pane
                                //box.getPanel(itemId).hide();
                                var pn = box.getPanel(itemId).get(0);
                                if (pn && (item._scrollTop = pn.scrollTop || 0))
                                    pn.scrollTop = 0;

                                box.getPanel(itemId).css('display', 'none');
                            }
                        }
                    },
                    expand = function (itemId, arr) {
                        var subId = profile.getSubIdByItemId(itemId),
                            item = profile.getItemByItemId(itemId);
                        if (subId) {
                            arr.push(subId);
                            mcap.html(item.caption);
                            mcls.css('display', item.closeBtn ? '' : 'none');
                            profile._menuId = item.id;
                            if (!dm.hasOwnProperty("noPanel") || !prop.noPanel) {
                                // show pane
                                //box.getPanel(value).css('position','relative').show('auto','auto');
                                box.getPanel(itemId).css('display', 'block');
                                if (item._scrollTop)
                                    box.getPanel(itemId).get(0).scrollTop = item._scrollTop;

                                profile.adjustSize(false, false, value);

                                profile.box._forLazyAppend(profile, item, value);
                                profile.box._forIniPanelView(profile, item);
                            }
                        }
                    };
                var arr1 = [], arr2 = [];
                if (dm.hasOwnProperty("selMode") &&
                    dm.hasOwnProperty("noPanel") &&
                    prop.noPanel &&
                    prop.selMode == "multi") {

                    uiv = uiv ? uiv.split(prop.valueSeparator) : [];
                    ood.arr.each(uiv, function (key) {
                        fold(key, arr1);
                    });
                    value = value ? value.split(prop.valueSeparator) : [];
                    var lastV = "";
                    ood.arr.each(value, function (key) {
                        var l = arr2.length;
                        expand(key, arr2);
                        // the last one
                        if (l < arr2.length)
                            lastV = key;
                    });
                } else {
                    fold(uiv, arr1);
                    expand(value, arr2);
                }

                if (arr1.length) {
                    profile.getSubNodes(['ITEM', 'TOGGLE'], arr1).tagClass('-checked', false);
                    profile.getSubNodes('ITEM', arr1).tagClass('-checked', false);
                }
                if (arr2.length) {
                    profile.getSubNodes(['ITEM', 'TOGGLE'], arr2).tagClass('-checked');
                    profile.getSubNodes('ITEM', arr2).tagClass('-checked');
                }

            });
        },
        append: function (target, subId, pre, base) {
            var p = this.get(0).properties;
            if (subId = subId || p.$UIvalue || p.value)
                arguments.callee.upper.call(this, target, subId + '', pre, base);
            return this;
        },
        getCurPanel: function () {
            var profile = this.get(0),
                dm = profile.box.$DataModel,
                v = profile.properties.$UIvalue;
            if (dm.hasOwnProperty("noPanel") && dm.hasOwnProperty("selMode") && profile.properties.selMode == 'multi') {
                v = v.split(prop.valueSeparator);
                v = v[0] || null;
            }
            return v ? this.getPanel(v) : null;
        },
        autoSave: function () {
            var module = this.getActiveModule();
            if (module) {
                module.autoSave();
            }
        },

        getActiveModule: function () {
            if (this.getSelectedItem()) {
                var cls = this.getSelectedItem().euClassName;
                return this.getModule().getChildModule(cls);
            }
            return null;
        },


        // get pane in page views
        getPanel: function (subId) {
            var profile = this.get(0);
            return profile.getSubNodeByItemId('PANEL', subId + '');
        },
        ////
        addPanel: function (paras, children, item) {
            var ns = this,
                i = {}, arr = [],
                id = item && item.id,
                items = ns.getItems(),
                id2 = paras.id || paras.tag;
            if (items.length) {
                if (-1 != ood.arr.subIndexOf(items, 'id', id2))
                    return false;
            }

            ood.merge(i, {
                caption: paras.caption,
                image: paras.image,
                closeBtn: paras.closeBtn || false,
                popBtn: paras.popBtn || false,
                optBtn: paras.optBtn || false,
                imagePos: paras.imagePos,
                imageBgSize: paras.imageBgSize,
                dragKey: paras.dragKey,
                dropKeys: paras.dropKeys,
                id: paras.id || paras.tag || ood.id()
            });

            if (id) ns.insertItems([i], id, true);
            else ns.insertItems([i]);

            ood.arr.each(children, function (o) {
                arr.push(o[0]);
            });
            ns.append(ood.UI.pack(arr, false), i.id);

            return ns;
        },
        removePanel: function (domId) {
            var self = this,
                item = self.getItemByDom(domId);
            return self.removeItems([item.id]);
        },
        getPanelPara: function (domId) {
            var profile = this.get(0),
                pp = profile.properties,
                item = profile.getItemByDom(domId),
                paras = ood.clone(item, false);
            if (!paras.dragKey) paras.dragKey = pp.dragKey;
            if (!paras.dropKeys) paras.dropKeys = pp.dropKeys;
            return paras;
        },
        getAllFormValues: function (isAll) {
            var a = this.getChildren(),
                elems = ood.absValue.pack(a),
                tabValue = {},
                formValue = {},
                profile = this.get(0);
            ood.arr.each(profile.children, function (o) {
                var oo = o[0].boxing(), name = oo.getProperties().name || o[0].alias;
                if (ood.isArr(o) && o.length > 1 && ood.isStr(o[1])) {
                    name = o[1];
                }
                if (oo.Class['ood.UI.Tabs']) {
                    //  formValue[name] = oo.getAllFormValues();
                    ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
                } else if (oo.Class['ood.UI.Block'] || oo.Class["ood.UI.Panel"] || oo.Class["ood.UI.Layout"]) {
                    if (isAll && !ood.str.endWith(name, 'Main')) {
                        if (!formValue[name]) {
                            formValue[name] = oo.getAllFormValues(isAll);
                        } else {
                            ood.merge(formValue[name], oo.getAllFormValues(isAll), 'all')
                        }
                    } else {
                        ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
                    }

                } else if (oo.getFormValues) {
                    formValue[name] = oo.getFormValues();
                } else if (oo.getUIValue) {
                    formValue[name] = oo.getUIValue();
                } else if (oo.getValue) {
                    formValue[name] = oo.getValue();
                }
            });
            tabValue[profile.properties.name || profile.alias] = formValue;

            return tabValue;
        },

        getPanelChildren: function (domId) {
            var profile = this.get(0),
                id = profile.getItemIdByDom(domId),
                arr = [];
            if (id)
                ood.arr.each(profile.children, function (o) {
                    if (o[1] == id) arr.push(o);
                });
            return arr;
        },

        resetPanelView: function (subId, removeChildren, destroyChildren) {
            if (!ood.isSet(removeChildren)) removeChildren = true;
            if (!ood.isSet(destroyChildren)) destroyChildren = true;
            var ins, item;
            return this.each(function (profile) {
                if (profile.renderId) {
                    ood.arr.each(profile.properties.items, function (o) {
                        if (subId === true || (subId + '') === o.id)
                            delete o._$ini;
                    });
                    if (removeChildren)
                        profile.boxing().removeChildren(subId, destroyChildren)
                }
            });
        },

        iniPanelView: function (subId) {
            return this.each(function (profile) {
                if (subId) {
                    if (subId = profile.getItemByItemId(subId + '')) {
                        profile.box._forIniPanelView(profile, subId);
                    }
                } else {
                    ood.arr.each(profile.properties.items, function (item) {
                        profile.box._forIniPanelView(profile, item);
                    });
                }
            });
        },

        ////
        fireItemClickEvent: function (subId) {
            var node = this.getSubNodeByItemId('ITEM', subId + ''), ev = ood.Event;
            node.onClick(true);

            //if(ev.__realtouch)ev.__simulatedMousedown=1;
            //node.onMousedown(true).onMouseup(true);
            //if(ev.__realtouch)ev.__simulatedMousedown=0;
            return this;
        },
        /* insert some views to pageView widgets
            arr: hash(view properties) or array of hash
            before: views will insert before it, string
        */
        _afterInsertItems: function (profile, data) {
            if (!profile.renderId) return;
            var box = profile.box, obj, v, pp = profile.properties;
            if (obj = profile.getSubNode(profile.keys.BOX || profile.keys.KEY)) {
                // add panels anyway
                obj.append(profile._buildItems('panels', data));
                // for stacks only
                if (!profile.box.$DataModel.hasOwnProperty("noPanel")) {
                    if (!(v = this.getUIValue()))
                        this.fireItemClickEvent((v = pp.items[0]) && v.id);
                }
                profile.adjustSize();
            }
        },
        /*  remove some views from pageView
            arr: array for id
        */


        removeItems: function (arr/*default is the current*/, purgeNow) {
            var self = this,
                p, obj, serialId;
            self.each(function (profile) {
                var p = profile.properties;
                arr = ood.isSet(arr) ? ood.isArr(arr) ? arr : (arr + "").split(p.valueSeparator) : null;
                if (!arr) arr = ((p.$UIvalue || p.value) + "").split(p.valueSeparator);
                if (!profile.box.$DataModel.hasOwnProperty("noPanel") || !profile.properties.noPanel)
                    ood.arr.each(arr, function (o) {
                        // get ui serial id
                        serialId = profile.getSubIdByItemId(o + "");
                        if (serialId && !(obj = profile.getSubNode('PANEL', serialId)).isEmpty()) {
                            // remove ui
                            obj.remove(true, purgeNow);
                        }
                    });
            });
            arguments.callee.upper.apply(self, arguments);

            self.each(function (profile) {
                if (!profile.boxing().getUIValue()) {
                    ood.asyRun(function () {
                        if (!profile || !profile.renderId || !profile.properties || !profile.properties.items.length) return;
                        var i;
                        profile.boxing().fireItemClickEvent((i = profile.properties.items[0]) && i.id);
                    });
                }

                profile.adjustSize();
            });

            return self;
        },
        clearItems: function (purgeNow) {
            var self = this;
            self.each(function (profile) {
                if (!profile.box.$DataModel.hasOwnProperty("noPanel") || !profile.properties.noPanel)
                    profile.getSubNode('PANEL', true).remove(true, purgeNow);
            });
            self.setValue(null, true, 'clear');
            arguments.callee.upper.apply(self, arguments);
            return self;
        },
        markItemCaption: function (subId, mark, force, tag, cls) {
            var profile = this.get(0);
            subId = profile.getItemByItemId(subId + '');

            if ((subId._dirty != mark) || force) {
                var id = subId.id,
                    item = profile.getItemByItemId(id),
                    caption = item.caption,
                    node = profile.getSubNodeByItemId('CAPTION', id);
                if (tag) {
                    if (ood.isFun(tag)) {
                        item.caption = tag(profile, item, mark);
                        node.html(item.caption);
                    } else
                        node.html(item.caption = mark ? tag + caption : caption.replace(new RegExp("^" + tag), ''));
                } else
                    node.html(item.caption = mark ? '*' + caption : caption.replace(/^\*/, ''));
                if (cls) {
                    if (mark) node.addCalss(cls);
                    else node.removeCalss(cls);
                } else
                    node.css({'font-weight': mark ? 'bold' : '', 'font-style': mark ? 'italic' : ''});

                subId._dirty = mark;
            }
            return this;
        }
    },
    Static: {
        Templates: {
            tagName: 'div',
            style: '{_style};',
            className: '{_className}',
            LIST: {
                $order: 1,
                tagName: 'div',
                style: '{_liststyle}',
                LISTBG: {
                    $order: 0,
                    className: 'ood-uiborder-t ood-uiborder-b ood-uiborder-dark ood-uibar-checked'
                },
                MENU: {
                    className: 'ood-ui-unselectable ood-uiborder-hidden ood-uiborder-radius',
                    MENUICON: {
                        className: 'oodfont',
                        $fonticon: 'ri-menu-3-line'
                    },
                    MENUCAPTION: {},
                    MENUCLOSE: {
                        className: 'oodfont',
                        $fonticon: 'ri-close-line',
                        $order: 2
                    }
                },
                MENU2: {
                    tagName: 'div',
                    className: 'ood-ui-unselectable',
                    MENUICON2: {
                        className: 'ood-uiborder-hidden ood-uiborder-radius oodcon {_iconChecked}',
                        $fonticon: 'ri-menu-3-line'
                    }
                },
                ITEMS: {
                    tagName: 'div',
                    className: 'ood-ui-unselectable {_specialIconCls}',
                    text: "{items}",
                    style: '{HAlign}'
                }
            },
            PNAELS: {
                $order: 2,
                tagName: 'text',
                text: '{panels}'
            },
            $submap: {
                items: {
                    ITEM: {
                        className: 'ood-uiborder-flat ood-uiborder-nob ood-uiborder-box ood-uiborder-radius-big-tl ood-uiborder-radius-big-tr ood-uibar {itemClass} {disabled} {readonly}',
                        style: '{_itemDisplay} {itemStyle}{_itemColor}',
                        ITEMI: {
                            ITEMC: {
                                HANDLE: {
                                    tabindex: '{_tabindex}',
                                    className: 'ood-showfocus',
                                    IBWRAP: {
                                        tagName: 'div',
                                        style: "white-space:nowrap;",
                                        RULER: {},
                                        LTAGCMDS: {
                                            $order: 1,
                                            tagName: 'span',
                                            className: 'ood-ltag-cmds',
                                            style: '{_ltagDisplay}',
                                            text: "{ltagCmds}"
                                        },
                                        ICON: {
                                            $order: 2,
                                            className: ' {imageClass}  {picClass}',
                                            style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}{_iconColor}',
                                            text: '{iconFontCode}'
                                        },
                                        CAPTION: {
                                            $order: 3,
                                            text: '{caption}',
                                            className: "ood-title-node",
                                            style: '{itemWidth};{itemAlign}{_fontColor}'
                                        },
                                        CMDS: {
                                            $order: 4,
                                            RTAGCMDS: {
                                                $order: 0,
                                                tagName: 'span',
                                                className: 'ood-rtag-cmds',
                                                style: '{_rtagDisplay}',
                                                text: "{rtagCmds}"
                                            },
                                            OPT: {
                                                $order: 1,
                                                className: 'oodfont',
                                                $fonticon: 'ood-uicmd-opt',
                                                style: '{_opt}'
                                            },
                                            POP: {
                                                className: 'oodfont',
                                                $fonticon: 'ood-uicmd-pop',
                                                style: '{popDisplay}',
                                                $order: 1
                                            },
                                            CLOSE: {
                                                className: 'oodfont',
                                                $fonticon: 'ood-uicmd-close',
                                                style: '{closeDisplay}',
                                                $order: 2
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                panels: {
                    PANEL: {
                        tagName: 'div',
                        className: 'ood-uibase ood-uicontainer',
                        style: "{_overflow};{_bginfo}",
                        text: '{html}' + ood.UI.$childTag
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
            KEY: {
                position: 'absolute',
                overflow: 'hidden'
            },
            LIST: {
                position: 'relative',
                overflow: 'hidden',
                left: 0,
                width: '100%',
                // padding: '.25em .25em 0 .25em ',
                'white-space': 'nowrap'
            },
            LISTBG: {
                position: 'absolute',
                overflow: 'hidden',
                left: 0,
                bottom: 0,
                height: '3px',
                width: '100%'
            },
            MENU: {
                display: 'none',
                margin: '.25em',
                padding: '.16667em',
                cursor: 'pointer'
            },
            MENU2: {
                display: 'none'
            },
            MENUCAPTION: {
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle',
                margin: '0 4px',
                'font-size': '1em'
            },
            ITEMS: {
                padding: ood.browser.contentBox ? '0 0 4px 0' : '0 0 2px 0',
                position: 'relative',
                left: 0,
                top: 0,
                'white-space': 'nowrap'
            },
            'ITEMS-icon CAPTION, ITEMS-icon OPT, ITEMS-icon POP, ITEMS-icon2 CAPTION, ITEMS-icon2 OPT, ITEMS-icon2 POP': {
                display: 'none'
            },

            'ITEMS-menu ITEM': {
                display: 'none'
            },
            ITEM: {
                $order: 0,
                cursor: 'pointer',
                //  padding: '0.75rem 1rem',
                'vertical-align': 'top',
                height: 'auto',
                'border-bottom': '2px solid transparent',
                transition: 'all 0.2s ease',
                color: 'var(--text)'
            },
            'ITEM-checked': {
                'border-bottom-color': 'var(--primary)',
                color: 'var(--primary-active) !important',
                fontWeight: '100'
            },
            'ITEM-checked:hover': {
                'border-bottom-color': 'var(--primary)',
                color: 'var(--primary-active) !important',
                fontWeight: '100'
            },
            ITEMI: {
                $order: 0,
                'padding-left': '.5em',
                //keep this same with ITEM
                'vertical-align': 'top'
            },
            ITEMC: {
                $order: 0,
                padding: '0.75em 0 0 0',
                //keep this same with ITEM
                'vertical-align': 'top',
                'text-align': 'center'
            },
            HANDLE: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                cursor: 'pointer',
                'vertical-align': 'middle',
                padding: 0
            },
            'ITEM-checked HANDLE': {
                'padding-bottom': '1px'
            },
            RULER: {
                height: '1.5em',
                width: '0',
                'vertical-align': 'middle'
            },
            PANEL: {
                position: 'relative',
                display: 'none',
                width: '100%',
                overflow: 'auto',
                background: 'var(--bg)',
                padding: '1rem',
                border: '1px solid var(--border)',
                'border-radius': '0 0 4px 4px'
            },
            CAPTION: {
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle',
                margin: '6px 6px 0 6px',
                overflow: 'hidden'
            },
            CMDS: {
                'vertical-align': 'middle',
                'padding-right': '0.25em'
            },
            'LTAGCMDS, RTAGCMDS': {
                padding: 0,
                margin: 0,
                'vertical-align': 'middle'
            },
            CMD: {
                padding: 0,
                margin: 0
            }
        },
        Behaviors: {
            NOTIPS: ["GROUP", "HANDLER"],
            DroppableKeys: ['PANEL', 'LIST', 'ITEM'],
            PanelKeys: ['PANEL'],
            DraggableKeys: ['ITEM'],
            HoverEffected: {
                ITEM: 'ITEM',
                MENU: 'MENU',
                MENU2: 'MENU2',
                MENUICON2: 'MENUICON2',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                MENUCLOSE: 'MENUCLOSE',
                POP: 'POP',
                ICON: 'ICON',
                CMD: 'CMD'
            },
            ClickEffected: {
                ITEM: 'ITEM',
                MENU: 'MENU',
                MENU2: 'MENU2',
                MENUICON2: 'MENUICON2',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                MENUCLOSE: 'MENUCLOSE',
                POP: 'POP',
                CMD: 'CMD'
            },
            CAPTION: {
                onMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != 'left') return;
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (properties.disabled || item.disabled) return false;
                    if (properties.readonly || item.readonly) return false;
                    if (box.getUIValue() == item.id) {
                        if (profile.onCaptionActive)
                            profile.boxing().onCaptionActive(profile, profile.getItemByDom(src), e, src);
                    }
                }
            },
            ITEM: {
                onClick: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != 'left') return false;
                    var t;
                    if ((t = ood.Event.getSrc(e).parentNode) && t.id && (profile.getKey(t.id)) == profile.keys.CMDS) return false;

                    var prop = profile.properties,
                        dm = profile.box.$DataModel,
                        itemId = profile.getSubId(src),
                        item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (prop.disabled || item.disabled) return false;
                    if (prop.readonly || item.readonly) return false;

                    //for some input onblur event
                    //profile.getSubNode('HANDLE', itemId).focus(true);

                    if (dm.hasOwnProperty("selMode") &&
                        dm.hasOwnProperty("noPanel") &&
                        prop.noPanel &&
                        prop.selMode == "multi") {

                        var value = box.getUIValue(),
                            arr = value ? value.split(prop.valueSeparator) : [],
                            checktype = 1,
                            rt = false,
                            rt2 = false;
                        // for multi selection
                        if (arr.length) {
                            //for select
                            if (ood.arr.indexOf(arr, item.id) != -1) {
                                ood.arr.removeValue(arr, item.id);
                                checktype = -1
                            } else
                                arr.push(item.id);

                            arr.sort();
                            value = arr.join(prop.valueSeparator);

                            //update string value only for setCtrlValue
                            if (box.getUIValue() == value)
                                rt = false;
                            else {
                                box.setUIValue(value, null, null, 'md');
                                if (box.get(0) && box.getUIValue() == value)
                                    rt = box.onItemSelected(profile, item, e, src, checktype) || rt2;
                            }
                            return rt;
                        }

                    }
                    // for single selection
                    if (box.getUIValue() != item.id) {
                        box.setUIValue(item.id, null, null, 'md');
                        //if success
                        if (box.getUIValue() == item.id) {
                            rt = box.onItemSelected(profile, item, e, src) || rt2;
                            return rt;
                        }
                    }
                }
            },
            HANDLE: {
                onKeydown: function (profile, e, src) {
                    var keys = ood.Event.getKey(e), key = keys.key, shift = keys.shiftKey;
                    if (key == ' ' || key == 'enter') {
                        profile.getSubNode('ITEM', profile.getSubId(src)).onClick();
                        return false;
                    }

                    var cur = ood(src),
                        target = profile.getSubNode('ITEMS'),
                        first = target.nextFocus(true, true, false),
                        last = target.nextFocus(false, true, false);

                    switch (key) {
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
                    }
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
            OPT: {
                onClick: function (profile, e, src) {
                    profile.boxing().onShowOptions(profile, profile.getItemByDom(src), e, src);
                    return false;
                }
            },
            CLOSE: {
                onClick: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        uiv = properties.$UIvalue,
                        bak;

                    if (properties.disabled || item.disabled) return;
                    if (properties.readonly || item.readonly) return false;
                    var instance = profile.boxing();

                    if (false === instance.beforePageClose(profile, item, src)) return;

                    bak = ood.copy(item);

                    // if the current item is selected, select the next or the pre one item
                    if (uiv && uiv == item.id) {
                        var items = properties.items,
                            index = ood.arr.subIndexOf(items, "id", item.id),
                            t,
                            nuiv = (t = items[index + 1]) ? t.id : (t = items[index - 1]) ? t.id : (t = items[0]) ? t.id : null;
                        if (nuiv && nuiv != uiv) {
                            profile.boxing().fireItemClickEvent(nuiv);
                        }
                    }

                    instance.removeItems(item.id);

                    instance.afterPageClose(profile, bak);

                    profile.adjustSize();

                    return false;
                }
            },
            MENUCLOSE: {
                onClick: function (profile, e, src) {
                    profile.getSubNodeByItemId("CLOSE", profile._menuId).onClick(true);
                }
            },
            POP: {
                onClick: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        options = {
                            parent: null,
                            host: null,
                            properties: null,
                            events: null,
                            CS: null,
                            CC: null,
                            CB: null,
                            CF: null,
                            init: null
                        },
                        id = item.id;
                    if (properties.disabled || item.disabled) return false;
                    if (properties.readonly || item.readonly) return false;

                    if (profile.beforePagePop && false == profile.boxing().beforePagePop(profile, item, options, e, src))
                        return false;

                    var panel = profile.boxing().getPanel(id),
                        pos = profile.getRoot().offset(),
                        size = profile.getRoot().cssSize(),
                        pro = ood.copy(ood.UI.Dialog.$DataStruct),
                        events = {};

                    ood.merge(pro, item, 'with');
                    ood.merge(pro, {
                        dragKey: item.dragkey || properties.dragKey,
                        dock: 'none',
                        tag: item.tag || item.id,
                        width: Math.max(size.width, 200),
                        height: Math.max(size.height, 100),
                        left: pos.left,
                        top: pos.top,
                        landBtn: true
                    }, 'all');
                    if (options.properties)
                        ood.merge(pro, options.properties, 'with');

                    if (options.events)
                        ood.merge(events, options.events, 'all');

                    var dialog = new ood.UI.Dialog(pro, events, options.host || profile.host, options.CS || null, options.CC || null, options.CB || null, options.CF || null);

                    if (ood.isFun(options.init) && false === options.init(dialog, profile, options)) {
                    } else {
                        dialog.show(options.parent || ood('body'));
                        var arr = [];
                        ood.arr.each(profile.children, function (o) {
                            if (o[1] == id) {
                                arr.push(o[0]);
                            }
                        });
                        if (arr.length) {
                            dialog.append(ood.UI.pack(arr, false));
                        }
                        profile.boxing().removeChildren(id).removeItems(id);
                    }
                    return false;
                }
            },
            MENU: {
                onMouseover: function (profile, e, src) {
                    var menu = profile._droppopmenu;
                    if (menu) return;

                    var ins = profile.boxing(),
                        items = profile.properties.items,
                        nitems = [],
                        l = items.length,
                        ll;
                    if (items.length > 10) {
                        ll = Math.ceil(l / 10);
                        for (var i = 0; i < ll; i++)
                            nitems.push({caption: (i * 10 + 1) + " - " + Math.min(l, ((i + 1) * 10 + 1)), sub: []});
                        ood.arr.each(items, function (item, i) {
                            nitems[parseInt(i / 10)].sub.push(ood.clone(item, false, 1));
                        });
                    } else {
                        nitems = ood.clone(items, false, 2);
                    }
                    //POPMENU
                    menu = profile._droppopmenu = new ood.UI.PopMenu({
                        items: nitems,
                        autoHide: true
                    }, {
                        onMenuSelected: function (profile, item) {
                            ins.fireItemClickEvent(item.id);
                        },
                        beforeHide: function (p, e) {
                            if (e) {
                                var node = ood(src),
                                    p1 = ood.Event.getPos(e),
                                    size = node.cssSize(),
                                    add = 3,
                                    p2 = node.offset();

                                if (p1.left > p2.left && p1.top > p2.top - add && p1.left < p2.left + size.width && p1.top < p2.top + size.height) {
                                    return false;
                                }
                            }
                        }, onHide: function () {
                            profile._droppopmenu.destroy(true);
                            delete profile._droppopmenu;
                        }
                    });
                    menu.pop(src);
                },
                onMouseout: function (profile, e, src) {
                    var pop;
                    if (pop = profile._droppopmenu) {
                        var node = pop.get(0).getRoot(),
                            p1 = ood.Event.getPos(e),
                            size = node.cssSize(),
                            add = 3,
                            p2 = node.offset();

                        if (p1.left > p2.left && p1.top > p2.top - add && p1.left < p2.left + size.width && p1.top < p2.top + size.height) {
                        } else
                            pop.hide();
                    }
                },
                onClick: function (p, e, src) {
                    ood(src).onMouseover(true);
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

            rotate: null,
            iconColors: null,
            itemColors: null,
            fontColors: null,

            autoFontColor: {
                caption: ood.getResText("DataModel.autoFontColor") || "自动字体颜色",
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                caption: ood.getResText("DataModel.autoIconColor") || "自动图标颜色",
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemColor: {
                caption: ood.getResText("DataModel.autoItemColor") || "自动项颜色",
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },

            expression: {
                caption: ood.getResText("DataModel.expression") || "表达式",
                ini: '',
                action: function () {
                }
            },
            selectable: {
                caption: ood.getResText("DataModel.selectable") || "可选择",
                ini: true
            },
            dirtyMark: {
                caption: ood.getResText("DataModel.dirtyMark") || "脏标记",
                ini: false
            },

            lazyAppend: {
                caption: ood.getResText("DataModel.delayAppend") || "延迟追加",
                ini: true
            },
            isFormField: {
                caption: ood.getResText("DataModel.formField") || "表单字段",
                hidden: true,
                ini: false
            },


            dock: {
                caption: ood.getResText("DataModel.dockMode") || "停靠方式",
                ini: 'fill'
            },
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
            position: {
                caption: ood.getResText("DataModel.position") || "位置",
                ini: 'absolute'
            },
            itemWidth: {
                caption: ood.getResText("DataModel.itemWidth") || "项宽度",
                ini: 0,
                action: function (value) {
                    this.getSubNode('CAPTION', true).width(value);
                }
            },
            itemAlign: {
                caption: ood.getResText("DataModel.itemAlign") || "项对齐",
                ini: "",
                listbox: ['', 'left', 'center', 'right'],
                action: function (value) {
                    this.getSubNode('CAPTION', true).css('text-align', value);
                }
            },
            HAlign: {
                caption: ood.getResText("DataModel.hAlign") || "水平对齐",
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (value) {
                    this.getSubNode('ITEMS').css('textAlign', value);
                }
            },
            dropKeysPanel: {
                caption: ood.getResText("DataModel.dropKeysPanel") || "面板拖放键",
                ini: ''
            },
            value: {
                caption: ood.getResText("DataModel.value") || "值",
                ini: ''
            },
            selMode: {
                caption: ood.getResText("DataModel.selMode") || "选择模式",
                ini: 'single',
                listbox: ['single', 'multi']
            },
            noPanel: {
                caption: ood.getResText("DataModel.noPanel") || "无面板",
                ini: false,
                action: function (value) {
                    this.getSubNode('PANEL', true).css('display', value ? 'none' : 'block');
                    this.adjustSize(null, true);
                }
            },
            noHandler: {
                caption: ood.getResText("DataModel.noHandler") || "无句柄",
                ini: false,
                action: function (value) {
                    this.getSubNode('LIST').css('display', value ? 'none' : 'block');
                    this.adjustSize(null, true);
                }
            },
            tagCmds: {
                caption: ood.getResText("DataModel.tagCmds") || "标签命令",
                ini: [],
                action: function () {
                    this.boxing().refresh();
                }
            }
        },
        EventHandlers: {
            onCmd: function (profile, item, cmdkey, e, src) {
            },
            onIniPanelView: function (profile, item) {
            },
            beforePagePop: function (profile, item, options, e, src) {
            },
            beforePageClose: function (profile, item, src) {
            },
            afterPageClose: function (profile, item) {
            },
            onShowOptions: function (profile, item, e, src) {
            },
            onItemSelected: function (profile, item, e, src, type) {
            },
            onCaptionActive: function (profile, item, e, src) {
            },
            onClickPanel: function (profile, item, e, src) {
            }
        },
        RenderTrigger: function () {
            var self = this, v, i, ins;
            // set default value
            if (v = self.properties.value) {
                (ins = self.boxing()).setUIValue(v, null, null, 'render');
                if (i = self.getItemByItemId(v))
                    ins.onItemSelected(self, i);
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile);
            data.panels = data.items, p = profile.properties, ns = this;
            if (data.HAlign)
                data.HAlign = 'text-align:' + data.HAlign + ';';
            data._liststyle = data.noHandler ? 'display:none' : '';
            if (data.sideBarStatus == 'fold') {
                data._specialIconCls = profile.getClass('ITEMS') + '-icon2';
                data._iconChecked = ' ood-uiborder-hidden-checked ood-icon-menu-checked ';
            }


            ood.arr.each(data.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(data.items, item);
                }

                profile.boxing()._autoColor(item, index, p);
            })

            return data;
        },

        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var dpn = 'display:none', p = profile.properties, t;
            profile.boxing()._autoColor(item, index, p);

            if (!item.closeBtn) {
                item.closeBtn = p.closeBtn;
            }
            if (!item.autoSave) {
                item.autoSave = p.autoSave;
            }

            if (!item.optBtn) {
                item.optBtn = p.optBtn;
            }

            item.closeDisplay = item.closeBtn ? '' : dpn;
            item.popDisplay = item.popBtn ? '' : dpn;
            item._opt = item.optBtn ? '' : dpn;
            item._itemDisplay = item.hidden ? dpn : '';
            if (t = item.itemWidth || p.itemWidth)
                item.itemWidth = "width:" + t + (ood.isFinite(t) ? "px" : "");
            if (t = item.itemAlign || p.itemAlign)
                item.itemAlign = "text-align:" + t;

            item._bginfo = "";
            if (t = item.panelBgClr || p.panelBgClr)
                item._bginfo += "background-color:" + t + ";";
            if (t = item.panelBgImg || p.panelBgImg)
                item._bginfo += "background-image:url(" + ood.adjustRes(t) + ");";
            if (t = item.panelBgImgPos || p.panelBgImgPos)
                item._bginfo += "background-position:" + t + ";";
            if (t = item.panelBgImgRepeat || p.panelBgImgRepeat)
                item._bginfo += "background-repeat:" + t + ";";
            if (t = item.panelBgImgAttachment || p.panelBgImgAttachment)
                item._bginfo += "background-attachment:" + t + ";";

            if (ood.isStr(item.overflow))
                item._overflow = item.overflow.indexOf(':') != -1 ? (item.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");
            else if (ood.isStr(p.overflow))
                item._overflow = p.overflow.indexOf(':') != -1 ? (p.overflow) : (p.overflow ? ("overflow:" + p.overflow) : "");

            this._prepareCmds(profile, item);
        },
        getDropKeys: function (profile, node) {
            var prop = profile.properties, item = profile.getItemByDom(node);
            return profile.getKey(ood.use(node).id()) == profile.keys.PANEL
                ? ((item && item.dropKeysPanel) || prop.dropKeysPanel)
                : ((item && item.dropKeys) || prop.dropKeys);
        },
        _forLazyAppend: function (profile, item, value) {
            var prop = profile.properties, box = profile.boxing(),
                moduleHash = {},
                zz = profile.moduleClass + "[" + profile.moduleXid + "]";
            //dynamic render
            if (prop.lazyAppend) {
                var arr = profile.children, a = [];
                ood.arr.each(arr, function (o) {
                    if (o[1] == value &&
                        // not rendered, or node not in
                        (!o[0].renderId || ood.UIProfile.getFromDom(ood(o[0].renderId).parent().id()) != profile)
                    ) {
                        a.push(o[0]);
                    }
                });
                if (a.length)
                    ood.arr.each(a, function (o, y, z) {
                        if (o.moduleClass && o.moduleXid && (y = ood.SC.get(o.moduleClass)) && (y = y.getInstance(o.moduleXid)) && y["ood.Module"]) {
                            z = o.moduleClass + "[" + o.moduleXid + "]";
                            if (zz != z && !moduleHash[z]) {
                                moduleHash[z] = y;
                            }
                        }
                        box.append(ood(o), value);
                    });

                // $attached is dynamic
                if (profile.$attached) {
                    for (var i = 0, v; v = profile.$attached[i++];)
                        if (v._render)
                            v._render(true);
                    delete profile.$attached;
                }

                arr = profile.exchildren;
                if (arr && arr.length) {
                    a = [];
                    ood.filter(arr, function (o) {
                        if (o[1] == value) {
                            a.push(o[0]);
                            return false;
                        }
                    });
                    if (a.length)
                        ood.arr.each(a, function (o) {
                            if (o.moduleClass && o.moduleXid && (y = ood.SC.get(o.moduleClass)) && (y = y.getInstance(o.moduleXid)) && y["ood.Module"]) {
                                z = o.moduleClass + "[" + o.moduleXid + "]";
                                if (zz != z && !moduleHash[z]) {
                                    moduleHash[z] = y;
                                }
                            }
                            box.append(ood(o), value);
                        });
                }

                arr = profile.excoms;
                if (arr && arr.length) {
                    a = [];
                    ood.filter(arr, function (o) {
                        if (o[1] == value) {
                            a.push(o[0]);
                            return false;
                        }
                    });
                    if (a.length)
                        ood.arr.each(a, function (o) {
                            o.show(null, box, value, false);
                        });
                }

                ood.each(moduleHash, function (o) {
                    o.render();
                });
            }
        },
        _forIniPanelView: function (profile, item) {
            if (!item) return;
            var prop = profile.properties, box = profile.boxing();
            if (!item._$ini) {
                item._$ini = true;
                if (profile.onIniPanelView) box.onIniPanelView(profile, item);
                if (item.iframeAutoLoad) {
                    box.getPanel(item.id).css('overflow', 'hidden');
                    var _if = typeof item.iframeAutoLoad == 'string' ? {url: item.iframeAutoLoad} : ood.clone(item.iframeAutoLoad, true),
                        id = "diframe_" + ood.rand(),
                        e = ood.browser.ie && ood.browser.ver < 9,
                        ifr = document.createElement(e ? "<iframe name='" + id + "'>" : "iframe");

                    _if.url = ood.adjustRes(_if.url, false, true);

                    ifr.id = ifr.name = id;
                    if (ood.isHash(item.iframeAutoLoad)) item.iframeAutoLoad.frameName = id;
                    item._frameName = id;

                    if (!_if.query) _if.query = {};
                    _if.query._rand = ood.rand();
                    ifr.frameBorder = '0';
                    ifr.marginWidth = '0';
                    ifr.marginHeight = '0';
                    ifr.vspace = '0';
                    ifr.hspace = '0';
                    ifr.allowTransparency = 'true';
                    ifr.width = '100%';
                    ifr.height = '100%';
                    box.getPanel(item.id).html("").append(ifr);

                    if ((_if.method || "").toLowerCase() == "post")
                        ood.Dom.submit(_if.url, _if.query, "post", id, _if.enctype);
                    else
                        ifr.src = _if.url;
                } else if (item.ajaxAutoLoad) {
                    var _ajax = typeof item.ajaxAutoLoad == 'string' ? {url: item.ajaxAutoLoad} : ood.clone(item.ajaxAutoLoad, true),
                        options = {rspType: "text"};
                    ood.merge(options, _ajax.options);
                    if (!_ajax.query) _ajax.query = {};
                    _ajax.query._rand = ood.rand();
                    box.busy(false, null, "PANEL", profile.getSubIdByItemId(item.id));
                    var node = box.getPanel(item.id);
                    ood.Ajax(ood.adjustRes(_ajax.url, false, true), _ajax.query, function (rsp) {
                        node.html(rsp, true, true);
                        box.free();
                    }, function (err) {
                        node.html("<div>" + err + "</div>", true, false);
                        box.free();
                    }, null, options).start();
                }
            }
        },
        _showTips: function (profile, node, pos) {
            if (profile.properties.disableTips) return;
            if (profile.onShowTips)
                return profile.boxing().onShowTips(profile, node, pos);
            if (!ood.Tips) return;
            var id = node.id, pid, ppid, ks = profile.keys;
            pid = ood.get(node, ["parentNode", "id"]) || "";
            ppid = ood.get(node, ["parentNode", "parentNode", "id"]) || "";
            if (id.indexOf(ks.ITEM) === 0 || pid.indexOf(ks.ITEM) === 0 || ppid.indexOf(ks.ITEM) === 0 ||
                id.indexOf(ks.HANDLE) === 0 || pid.indexOf(ks.HANDLE) === 0 || ppid.indexOf(ks.HANDLE) === 0 ||
                id.indexOf(ks.CMDS) === 0 || pid.indexOf(ks.CMDS) === 0 || ppid.indexOf(ks.CMDS) === 0) {
                var upper = arguments.callee.upper,
                    rtn = upper.apply(this, ood.toArr(arguments));
                upper = null;
                return rtn;
            }
        },
        // drop item
        _onDrop: ood.UI.List._onDrop,
        //for tabs only
        _onresize: function (profile, width, height, force, key) {
            var prop = profile.properties,
                item = profile.getItemByItemId(key);

            if (!item) {
                key = prop.$UIvalue || prop.value;
                item = profile.getItemByItemId(key);
            }
            if (!item) {
                item = prop.items[0];
                key = item && item.id;
            }
            if (!item) return;
            var panel = profile.boxing().getPanel(key),
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                root = profile.getRoot(),
                list = profile.getSubNode('LIST'),

                fzrate = profile.getEmSize() / root._getEmSize(),
                panelfz = panel._getEmSize(fzrate),
                listfz = list._getEmSize(fzrate),

                wc = null,
                hc = null,
                listH;

            // caculate by px
            if (width && width != 'auto') width = profile.$px(width, null, true);
            if (height && height != 'auto') height = profile.$px(height, null, true);

            if (!panel || panel.isEmpty()) return;

            if (!prop.noHandler) {
                //force to get offsetHeight
                listH = list.offsetHeight(true);
                if (profile._listH != listH) {
                    profile._listH = listH;
                    force = true;
                }
            }

            if (force) item._w = item._h = null;
            if (height && item._h != height) {
                item._h = height;
                if (height == 'auto') {
                    hc = 'auto';
                } else {
                    if (!prop.noHandler) {
                        height = height - listH;
                    }
                    if (height > 0) hc = height;
                }
                ;
            } else hc = height;

            if (width && item._w != width) {
                list.width(wc = adjustunit(item._w = width, listfz));
                if (!prop.noHandler) {
                    this._adjustHScroll(profile);
                }
            }

            if (!prop.noPanel && (hc || wc)) panel.height(adjustunit(hc, panelfz)).onSize();

            if (wc) {
                ood.UI._adjustConW(profile, panel, wc);
            }
        },
        _adjustHScroll: function (profile) {
            // SCROLL
            var items = profile.getSubNode('ITEMS'),
                innerW = items.width(),
                list = profile.getSubNode('LIST'),
                menu = profile.getSubNode('MENU'),
                caps = profile.getSubNode('CAPTION', true),
                itemsW = 0,
                getItemsW = function () {
                    var w = 0;
                    items.children().each(function (item) {
                        if (item.offsetWidth == 0) return;
                        if (!w) {
                            w = item.offsetLeft + item.offsetWidth;
                            return false;
                        }
                    }, true);
                    return w;
                },
                getCapsW = function () {
                    var w = 0;
                    caps.each(function (item) {
                        if (item.clientWidth == 0) return;
                        w += item.clientWidth;
                    });
                    return w;
                },
                ignoreCap;

            // init
            items.tagClass('-icon', false);
            items.tagClass('-icon2', false);
            items.tagClass('-menu', false);
            menu.css('display', 'none');
            caps.css('width', '');

            profile._mode = 'normal';
            // try 1: minus caption width
            itemsW = getItemsW();
            if (itemsW > innerW) {
                var capw = getCapsW();
                if ((itemsW - innerW) < capw * .75) {
                    var percent = 1 - (itemsW - innerW) / capw;
                    caps.each(function (cap) {
                        ood(cap).width(Math.floor(cap.clientWidth * percent) + 'px');
                    });
                    profile._mode = 'narrow';
                } else {
                    ignoreCap = 1;
                }

                // try 2: icon mode
                if (ignoreCap || getItemsW() > innerW) {
                    items.tagClass('-icon', true);
                    profile._mode = 'icon';
                    // try 3: menu mode
                    if (getItemsW() > innerW) {
                        items.tagClass('-menu', true);
                        menu.setInlineBlock();
                        profile._mode = 'menu';
                    }
                }
            }
        }
    }
});