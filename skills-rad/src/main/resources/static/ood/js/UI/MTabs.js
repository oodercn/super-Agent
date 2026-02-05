ood.Class("ood.UI.MTabs", "ood.UI.Tabs", {

    Initialize: function () {
        var t = this.getTemplate(), keys = this.$Keys;
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if(!t){
            t = {};
            t.LIST = {};
            t.$submap = { items: { ITEM: {} } };
        }
        // 确保LIST属性存在
        if(!t.LIST) t.LIST = {};
        // 确保$submap和相关属性存在
        if(!t.$submap) t.$submap = {};
        if(!t.$submap.items) t.$submap.items = {};
        if(!t.$submap.items.ITEM) t.$submap.items.ITEM = {};
        
        t.LIST.className = 'ood-uibar';
        this.setTemplate(t);
        t.$submap.items.ITEM.className = 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius {itemClass} {disabled} {readonly} {itemPosCls}';
        delete keys.LEFT;
        delete keys.RIGHT;
        delete keys.DROP;


    },
    
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            "barLocation": "left",
            "barSize": "12em",
            "sideBarStatus": "fold",
            items: [{
                "caption": "item 1",
                "id": "a",
                "imageClass": "ri-number-1",
                "name": "item 1"
            },
                {
                    "caption": "item 2",
                    "id": "b",
                    "imageClass": "ri-number-2",
                    "name": "item 2"
                },
                {
                    "caption": "item 3",
                    "id": "c",
                    "imageClass": "ri-number-3",
                    "name": "item 3"
                },
                {
                    "caption": "item 4",
                    "id": "d",
                    "imageClass": "ri-number-4",
                    "name": "item 4"
                }],
            value: 'a'
        }
    },

    Static: {
        Appearances: {
            LIST: {
                'z-index': '2',
                position: 'absolute',
                'white-space': 'nowrap',
                overflow: 'hidden'
            },
            // for auto height
            'LIST-attop, LIST-atbottom': {
                $order: 2,
                position: 'relative'
            },
            LISTBG: {
                display: 'none'
            },
            MENU: {
                display: 'none',
                margin: '.25em',
                padding: '.25em',
                cursor: 'pointer'
            },
            MENU2: {
                display: 'none',
                'text-align': 'center'
            },
            MENUICON2: {
                position: 'relative',
                margin: '.09375em',
                padding: '.1875em',
                cursor: 'pointer'
            },
            ITEMS: {
                'z-index': '2',
                position: 'relative',
                left: 0,
                top: 0,
                width: '100%',
                height: '100%',
                'white-space': 'nowrap',
                'overflow-x': 'hidden',
                'overflow-y': 'scroll'
            },
            'ITEMS-left, ITEMS-left ITEMC': {
                $order: 1,
                height: 'auto',
                'text-align': 'left'
            },
            'ITEMS-center, ITEMS-center ITEMC': {
                $order: 1,
                height: 'auto',
                'text-align': 'center'
            },
            'ITEMS-right, ITEMS-right ITEMC': {
                $order: 1,
                height: 'auto',
                'text-align': 'right'
            },
            'ITEMS-left HANDLE, ITEMS-right HANDLE': {
                $order: 2,
                display: 'block'
            },
            ITEM: {
                $order: 0,
                margin: '.166667em',
                position: 'relative',
                cursor: 'pointer',
                'padding': '0 .125em 0 0',
                'vertical-align': 'top'
            },
            ITEMI: {
                $order: 0,
                'padding-left': '.125em',
                //keep this same with ITEM
                'vertical-align': 'top'
            },
            'ITEMS-block ITEM, ITEMS-block ITEMI, ITEMS-block ITEMC': {
                $order: 2,
                display: 'block'
            },
            ITEMC: {
                $order: 0,
                padding: '.15em 0 .125em 0',
                //keep this same with ITEM
                'vertical-align': 'top',
                'text-align': 'center'
            },
            HANDLE: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                cursor: 'pointer',
                'vertical-align': 'middle',
                margin: '.125em'
            },
            'ITEM-checked HANDLE': {}
        },
        DataModel: {
            HAlign: null,
            barLocation: {
                ini: 'top',
                listbox: ['top', 'bottom', 'left', 'right'],
                action: function (v) {
                    var self = this,
                        hs = self.getSubNode('LIST'),
                        h = self.getSubNode('ITEMS'),
                        unit = 0 + self.$picku();
                    switch (v) {
                        case 'left':
                            hs.cssRegion({left: unit, top: unit, right: 'auto', bottom: unit});
                            break;
                        case 'top':
                            hs.cssRegion({left: unit, top: unit, right: unit, bottom: 'auto'});
                            break;
                        case 'right':
                            hs.cssRegion({left: 'auto', top: unit, right: unit, bottom: unit});
                            break;
                        case 'bottom':
                            hs.cssRegion({left: unit, top: 'auto', right: unit, bottom: unit});
                            break;
                    }
                    switch (v) {
                        case 'left':
                        case 'right':
                            h.tagClass('-block', true);
                            break;
                        case 'top':
                        case 'bottom':
                            h.tagClass('-block', false);
                            hs.height('auto');
                            break;
                    }
                    // add 'at' to be distinguished from ood-uibar-bottom
                    hs.tagClass('(-attop|-atbottom|-atleft|-atright)', false).tagClass('-at' + v, true);
                    this.adjustSize();
                }
            },
            barHAlign: {
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    var hl = this.getSubNode('ITEMS');
                    hl.tagClass('(-left|-right|-center)', false).tagClass('-' + v, true);
                }
            },
            barVAlign: {
                ini: 'top',
                listbox: ['top', 'bottom'],
                action: function (v) {
                    var hl = this.getSubNode('ITEMS'),
                        unit = 0 + this.$picku();
                    if (v == 'top')
                        hl.cssRegion({top: unit, bottom: 'auto'});
                    else
                        hl.cssRegion({bottom: unit, top: 'auto'});
                    this.adjustSize();
                }
            },
            barSize: {
                $spaceunit: 1,
                ini: '2.5em',
                action: function (v) {
                    this.adjustSize();
                }
            },
            borderType: {
                ini: 'none',
                listbox: ['none', 'flat', 'inset', 'outset'],
                action: function (v) {
                    var ns = this,
                        p = ns.properties,
                        n1 = ns.getSubNode('LIST'),
                        reg = /^ood-uiborder-/,
                        flat = 'ood-uiborder-flat ood-uiborder-radius',
                        ins = 'ood-uiborder-inset ood-uiborder-radius',
                        outs = 'ood-uiborder-outset ood-uiborder-radius',
                        root = ns.getRoot();
                    n1.removeClass(reg);
                    switch (v) {
                        case 'flat':
                            n1.addClass(flat);
                            break;
                        case 'inset':
                            n1.addClass(ins);
                            break;
                        case 'outset':
                            n1.addClass(outs);
                            break;
                    }

                    //force to resize
                    this.adjustSize();
                }
            },
            noFoldBar: {
                ini: false,
                action: function (value) {
                    this.getSubNode('MENU2', true).css('display', value ? 'none' : 'block');
                }
            },
            sideBarStatus: {
                ini: 'expand',
                listbox: ['expand', 'fold'],
                action: function (v) {
                    var self = this,
                        t = self.properties,
                        us = ood.$us(self),
                        adjustunit = function (v, emRate) {
                            return self.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                        },
                        hl = self.getSubNode('ITEMS'),
                        menu2 = self.getSubNode('MENUICON2');

                    if (t.sideBarStatus == 'fold') {
                        hl.tagClass('-icon2', true);
                        menu2.tagClass('-checked', true);
                    } else {
                        hl.tagClass('-icon2', false);
                        menu2.tagClass('-checked', false);
                    }

                    this.adjustSize();
                }
            },
            sideBarSize: {
                ini: '3em',
                action: function (v) {
                    this.adjustSize();
                }
            }
        },
        Behaviors: {
            MENU2: {
                onClick: function (profile, e, src) {
                    profile.boxing().setSideBarStatus(profile.properties.sideBarStatus == 'fold' ? 'expand' : 'fold', true);
                }
            }
        },

        LayoutTrigger: function () {
            var pro = this.properties;
            this.boxing().setBarLocation(pro.barLocation, true)
                .setBarHAlign(pro.barHAlign, true)
                .setBarVAlign(pro.barVAlign, true);

            if (pro.barLocation == 'top' || pro.barLocation == 'bottom') {
                this.getSubNode('ITEMS').addClass('ood-css-noscroll');
            }
            if (pro.borderType && pro.borderType != 'none') this.boxing().setBorderType(pro.borderType, true);
        },
        _onresize: function (profile, width, height, force, key) {
            var prop = profile.properties,
                noPanel = prop.noPanel,
                noHandler = prop.noHandler,
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
                // caculate by px
                ww = width ? profile.$px(width, null, true) : width,
                hh = (height && height != 'auto') ? profile.$px(height, null, true) : height,
                root = profile.getRootNode(),
                hs = profile.getSubNode('LIST'),
                hl = profile.getSubNode('ITEMS'),
                menu2 = profile.getSubNode('MENU2'),

                fzrate = profile.getEmSize() / profile.getRoot()._getEmSize(),
                panelfz = panel._getEmSize(fzrate),
                hsfz = hs._getEmSize(fzrate),
                hlfz = hl._getEmSize(fzrate),
                cb = ood(root).contentBox(),
                type = prop.borderType,
                bw = !cb ? 0 : (type == 'flat' || type == 'inset' || type == 'outset') ? hs._borderW() : 0,
                wc = null,
                hc = null,
                top, left, itmsH;

            // side bar
            menu2.css('display', 'none');

            if (noHandler) {
                wc = ww;
                hc = hh;
            } else {
                if (prop.barLocation == 'top' || prop.barLocation == 'bottom') {
                    if (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB)) {
                    } else {
                        hl.css('overflow-y', 'hidden');
                    }
                    itmsH = hs.height(prop.barSize || 'auto').offsetHeight(true);
                    hl.css('position', 'relative');

                    if (width) {
                        hs.width(adjustunit(ww - bw, hsfz));
                        hl.width(adjustunit(ww - bw, hlfz));

                        // for nopanel:
                        if (noPanel && height != 'auto') {
                            hs.height(adjustunit(hh - bw, hsfz));
                            hl.height(adjustunit(hh - bw, hsfz));
                        }

                        if (!noHandler)
                            profile.box._adjustHScroll(profile);

                        left = 0;
                        wc = ww;
                    }
                    if (hh != 'auto') {
                        // caculate by px
                        if (hh - itmsH > 0) hc = hh - itmsH - bw;
                        //hs.height(adjustunit(itmsH, hsfz));
                    } else {
                        hc = hh;
                    }
                    var t;
                    if (prop.barLocation == 'top') {
                        // ensure it's the last
                        if ((t = root.firstElementChild || root.firstChild) != hs.get(0)) {
                            if (t) root.insertBefore(hs.get(0), t);
                            else root.appendChild(hs.get(0));
                        }
                    } else if (prop.barLocation == 'bottom') {
                        // ensure it's the last
                        if ((root.lastElementChild || root.lastChild) != hs.get(0)) {
                            root.appendChild(hs.get(0));
                        }
                    }
                } else {
                    //reset to default
                    if (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB)) {
                    } else {
                        // bug: in mobile android, it will make hs._nodes.length=0
                        hl.css('overflow-y', 'scroll');
                    }
                    // side bar

                    menu2.css('display', (prop.sideBarSize && prop.noFoldBar) ? 'block' : 'none');

                    if (!noHandler)
                        profile.getSubNode('CAPTION', true).css('width', '');

                    // dont support auto height for 'top' or 'bottom' barLocation
                    if (hh == 'auto') {
                        hh = 300;
                    }

                    if (height) {
                        // for nopanel:
                        if (noPanel) {
                            if (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB)) {
                            } else {
                                hl.css('overflow-y', 'hidden');
                            }
                            hs.width(adjustunit(ww - bw, hsfz));
                            hl.width(adjustunit(ww - bw, hsfz));
                        }

                        // for nopanel:
                        hs.height(adjustunit(hh - bw, hsfz));
                        hl.height('auto');
                        // for scroll by mouse wheel
                        if (hl.height() >= hs.height()) {
                            hl.height(adjustunit(hh - bw - (prop.sideBarSize ? menu2.offsetHeight() : 0), hlfz));
                        }
                        hl.css('position', prop.barVAlign == 'bottom' ? 'absolute' : 'relative');
                        hc = hh;
                    }
                    if (height || width) {
                        var v = profile.$px(prop.sideBarStatus == 'fold' ? prop.sideBarSize : prop.barSize, hlfz, true);
                        var vv = !cb ? 0 : (hl._paddingW() + hl._marginW());

                        //caculate by px
                        left = prop.barLocation == 'left' ? bw + profile.$px(v + vv, hsfz, true) : 0;
                        wc = ww - profile.$px(v + vv, hsfz, true) - bw;

                        hl.width('auto');

                        if (!noPanel) {
                            hs.width(adjustunit(v + vv, hsfz));
                            hl.width(adjustunit(v + ood.Dom.getScrollBarSize(), hlfz));
                        }
                    }
                }
            }

            if (!noPanel) {
                panel.cssRegion({
                    width: (wc = wc ? adjustunit(wc, panelfz) : null),
                    height: hc ? adjustunit(hc, panelfz) : null,
                    left: noHandler ? 0 : adjustunit(left, panelfz)
                }, true);
                if (wc) {
                    ood.UI._adjustConW(profile, panel, wc);
                }
            }
        }
    }
});