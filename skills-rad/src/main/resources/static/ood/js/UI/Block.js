ood.Class("ood.UI.Block", "ood.UI.Widget", {

    Instance: {

        iniProp: {
            dock: 'width',
            position: 'relative',
            left: 'auto',
            top: 'auto',
            width: 'auto',
            height: '18em',
            toggleBtn: true,
            closeBtn: true,
            refreshBtn: true

        },

        /**
         * 切换主题
         * @param {string} [theme] 可选，指定要切换的主题('light'或'dark')
         */
        toggleTheme: function (theme) {
            var root = this.getRoot();
            var oldTheme = root.attr('data-theme') || 'light';
            var newTheme;

            if (theme) {
                newTheme = theme;
                root.attr('data-theme', newTheme);
            } else {
                newTheme = oldTheme === 'dark' ? 'high-contrast' :
                    oldTheme === 'high-contrast' ? 'light' : 'dark';
                root.attr('data-theme', newTheme);
            }

            // 触发主题变更事件
            this.emit('themechange', {
                oldTheme: oldTheme,
                newTheme: newTheme
            });
            return this;
        },

        /**
         * 设置主题
         * @param {string} theme 要设置的主题('light'或'dark')
         */
        setTheme: function (theme) {
            return this.toggleTheme(theme);
        },

        /**
         * 设置高对比度主题颜色
         * @param {string} color 高对比度颜色值
         */
        setHighContrastColor: function (color) {
            this.getRoot().css('--ood-hc-color', color);
            return this;
        },


        setChildren: function (childrens, prf) {
            var host = this, items = [], ctx = host.getModule().getCtxComponents();
            this.removeChildren(true, true, true);
            ood.each(childrens, function (o) {
                if (o && o.alias) {
                    if (o.alias == 'PAGECTX') {
                        ctx.boxing().removeChildren(true, true, true);
                        host.getModule().addCtxComponents(o.children);
                    } else {
                        items.push(o);
                    }
                }
            });
            ood.addChild({children: items}, host, host.getModule(), host.getModule());
        },
        getAllFormValues: function (isAll) {
            var a = this.getChildren(),
                elems = ood.absValue.pack(a),
                formValue = {},
                profile = this.get(0);
            ood.arr.each(profile.children, function (o) {
                var oo = o[0].boxing(), name = oo.getProperties().name || o[0].alias;
                if (oo.Class['ood.UI.Tabs']) {
                    // formValue[name] = oo.getAllFormValues();
                    ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
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
        }
    },


    Initialize: function () {
        var self = this,
            t = self.getTemplate();
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if (!t) {
            t = {};
        }
        // 确保className、FRAME和BORDER属性存在
        if (!t.className) t.className = '';
        if (!t.FRAME) t.FRAME = {};
        if (!t.FRAME.BORDER) t.FRAME.BORDER = {};

        //modify
        t.className += ' {_sidebarStatus}';
        ood.merge(t.FRAME.BORDER, {
            className: 'ood-uiw-border {clsBorderType1}',
            SIDEBAR: {
                tagName: 'div',
                className: 'ood-uisb ood-uibar {_sidebar}',
                SBCAP: {
                    className: 'ood-uisbcap ood-title-node',
                    text: '{sideBarCaption}'
                },
                SBBTN: {
                    $order: 1,
                    className: 'ood-uisbbtn oodfont',
                    $fonticon: '{_fi_btn}'
                }
            },
            PANEL: {
                tagName: 'div',
                className: 'ood-uibar ood-uicontainer {clsBorderType2}',
                style: '{_panelstyle};{background};{_overflow};',
                text: '{html}' + ood.UI.$childTag
            }
        }, 'all');


        //set back
        self.setTemplate(t);

        //get default Appearance
        t = self.getAppearance();
        //modify
        ood.merge(t, {
            PANEL: {
                position: 'relative',
                overflow: 'auto',
                'background-color': 'var(--ood-bg-primary)',
                'border-radius': 'var(--ood-border-radius-lg)',
                'box-shadow': '0 1px 3px rgba(0,0,0,0.1)'
              //  'padding': '1em'
            },
            SBCAP: {
                'text-overflow': 'ellipsis',
                'white-space': 'nowrap',
                overflow: 'hidden',
                'font-family': 'var(--ood-font-family)',
                'font-size': '1em',
                'font-weight': '500',
                'color': 'var(--ood-text-primary)'
            },

            SBBTN: {
                'z-index': 2,
                margin: '0.5em',
                'font-size': '1.25em',
                'color': 'var(--ood-icon-color)',
                'transition': 'all 0.2s ease',
                'cursor': 'pointer',
                'role': 'button',
                'aria-label': '{sideBarCaption}',
                'aria-expanded': '{sideBarStatus === "expand" ? "true" : "false"}'
            },
            'SBBTN:hover': {
                'color': 'var(--ood-icon-hover)'
            },
            'SBBTN:focus': {
                'outline': '2px solid var(--ood-icon-hover)',
                'outline-offset': '2px'
            }
        });
        //set back
        self.setAppearance(t);
    },
    Static: {
        Behaviors: {
            HoverEffected: {SBBTN: 'SBBTN', SBCAP: null},
            ClickEffected: {SBBTN: 'SBBTN'},
            DroppableKeys: ['PANEL'],
            PanelKeys: ['PANEL'],
            PANEL: {
                onClick: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return false;
                    if (profile.onClickPanel)
                        return profile.boxing().onClickPanel(profile, e, src);
                }
            },
            SBBTN: {
                onClick: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return false;
                    profile.boxing().setSideBarStatus(p.sideBarStatus == 'fold' ? 'expand' : 'fold');
                }
            },
            SIDEBAR: {
                onClick: function (profile, e, src) {
                    var p = profile.properties,
                        btn = profile.getSubNode('SBBTN');
                    if (p.disabled) return false;
                    if (ood.Event.getSrc(e).$xid != btn.xid()) {
                        if (p.sideBarStatus == 'fold') {
                            btn.onClick(true);
                        }
                    }
                }
            }
        },
        EventHandlers: {
            onClickPanel: function (profile, e, src) {
            },

            onFlagClick: function (profile, item, e, src) {
            },
            touchstart: function (profile, item, e, src) {
            },
            touchmove: function (profile, item, e, src) {
            },
            touchend: function (profile, item, e, src) {
            },
            touchcancel: function (profile, item, e, src) {
            },

            swipe: function (profile, item, e, src) {
                // 处理滑动事件
                var distance = ood.Event.getDistance(e);
                if (distance > 50) {
                    profile.boxing().toggleTheme();
                }
            },
            swipeleft: function (profile, item, e, src) {
                if (profile.properties.sideBarType === 'right') {
                    profile.boxing().setSideBarStatus('fold');
                }
            },
            swiperight: function (profile, item, e, src) {
                if (profile.properties.sideBarType === 'left') {
                    profile.boxing().setSideBarStatus('fold');
                }
            },
            swipeup: function (profile, item, e, src) {
                if (profile.properties.sideBarType === 'bottom') {
                    profile.boxing().setSideBarStatus('fold');
                }
            },
            swipedown: function (profile, item, e, src) {
                if (profile.properties.sideBarType === 'top') {
                    profile.boxing().setSideBarStatus('fold');
                }
            },


            press: function (profile, item, e, src) {
            },
            pressup: function (profile, item, e, src) {
            },


            pan: function (profile, item, e, src) {
            },
            panstart: function (profile, item, e, src) {
            },
            panmove: function (profile, item, e, src) {
            },
            panend: function (profile, item, e, src) {
            },
            pancancel: function (profile, item, e, src) {
            },

            panleft: function (profile, item, e, src) {
            },
            panright: function (profile, item, e, src) {
            },
            panup: function (profile, item, e, src) {
            },
            pandown: function (profile, item, e, src) {
            },


            pinch: function (profile, item, e, src) {
            },
            pinchstart: function (profile, item, e, src) {
            },
            pinchmove: function (profile, item, e, src) {
            },
            pinchend: function (profile, item, e, src) {
            },
            pinchcancel: function (profile, item, e, src) {
            },

            pinchin: function (profile, item, e, src) {
            },
            pinchout: function (profile, item, e, src) {
            },


            rotate: function (profile, item, e, src) {
            },
            rotatestart: function (profile, item, e, src) {
            },
            rotatemove: function (profile, item, e, src) {
            },
            rotateend: function (profile, item, e, src) {
            },
            rotatecancel: function (profile, item, e, src) {
            }
        },
        DataModel: {
            //delete those properties
            disabled: null,
            tips: null,

            comboType: {
                ini: "Block",
            },
            rotate: null,
            iframeAutoLoad: {
                ini: "",
                action: function () {
                    ood.UI.Div._applyAutoLoad(this);
                }
            },
            ajaxAutoLoad: {
                ini: "",
                action: function () {
                    ood.UI.Div._applyAutoLoad(this);
                }
            },
            selectable: true,
            html: {
                html: 1,
                action: function (v, ov, force) {
                    this.getSubNode('PANEL').html(ood.adjustRes(v, 0, 1), null, null, force);
                }
            },
            borderType: {
                ini: 'outset',
                listbox: ['none', 'flat', 'inset', 'outset', 'groove', 'ridge'],
                action: function (v) {
                    var ns = this,
                        p = ns.properties;
                    ns.box._borderType(ns, v, p.sideBarStatus, p.sideBarType.split('-'), true);
                }
            },

            // for side bar
            sideBarCaption: {
                ini: '',
                action: function (v) {
                    this.getSubNode("SBCAP").html(v);
                }
            },
            sideBarType: {
                ini: 'none',
                listbox: ['none', 'left', 'right', 'top', 'bottom', 'left-top', 'left-bottom', 'right-top', 'right-bottom', 'top-left', 'top-right', 'bottom-left', 'bottom-right'],
                action: function (v) {
                    var ns = this,
                        prop = ns.properties;
                    ns.box._adjustSideBar(ns, prop.sideBarStatus, v);

                    if (prop.dock != 'none') ns.boxing().adjustDock(true);
                    else ns.adjustSize();
                }
            },
            sideBarStatus: {
                ini: 'expand',
                listbox: ['expand', 'fold'],
                action: function (v) {
                    var ns = this, prop = ns.properties;
                    ns.getRoot().tagClass('-fold', v != 'expand');

                    ns.box._adjustSideBar(ns, v, prop.sideBarType);

                    // use sync way
                    ood.UI.$doResize(ns, prop.width, prop.height, true);
                    ns.boxing().adjustDock(true);
                }
            },
            sideBarSize: {
                ini: '2em',
                action: function (v) {
                    var ns = this,
                        prop = ns.properties;
                    if (prop.dock == 'none')
                        ns.adjustSize();
                    else
                        ns.boxing().adjustDock(true);
                }
            },

            background: {
                format: 'color',
                ini: '',
                action: function (v) {
                    this.getSubNode('PANEL').css('background', v);
                }
            },
            width: {
                $spaceunit: 1,
                ini: 'auto'
            },
            height: {
                $spaceunit: 1,
                ini: 'auto'
            }
        },
        Appearances: {
            KEY: {
                'line-height': 'normal'
            },
            'KEY-fold PANEL': {
                display: 'none'
            },
            'KEY-fold SIDEBAR': {
                cursor: 'pointer',
                'background-color': 'var(--ood-bg-disabled)'
            }
        },
        RenderTrigger: function () {
            // only div
            var ns = this;
            if (ns.box.KEY == "ood.UI.Block")
                if (ns.properties.iframeAutoLoad || ns.properties.ajaxAutoLoad)
                    ood.UI.Div._applyAutoLoad(this);
        },
        _sbicon: function (profile, sideBarStatus, type, ui) {
            var target = sideBarStatus == 'fold'
                ? type == 'left' ? 'left' : type == 'right' ? 'right' : type == 'top' ? 'up' : 'down'
                : type == 'left' ? 'right' : type == 'right' ? 'left' : type == 'top' ? 'down' : 'up';

            return ui ? profile.getSubNode('SBBTN').replaceClass(/(ood-icon-double)[\w]+/g, '$1' + target) : 'ood-icon-double' + target;
        },
        _borderType: function (profile, borderType, sideBarStatus, type, adjust) {
            type = sideBarStatus == 'expand' ? type[0] : (type[1] || type[0]);
            var ns = profile,
                v = borderType,
                n1 = ns.getSubNode('BORDER'), n2 = ns.getSubNode('PANEL'),
                reg = /^ood-uiborder-/,
                b = 'ood-uiborder-',
                r = b + 'radius',
                i = b + 'inset',
                o = b + 'outset',
                f = b + 'flat',
                ibr = type == 'left' ? r + '-tr ' + r + '-br' : type == 'top' ? r + '-bl ' + r + '-br' : type == 'right' ? r + '-tl ' + r + '-bl' : type == 'bottom' ? r + '-tl ' + r + '-tr' : r,
                flat = f + ' ' + r,
                ins = i + ' ' + r,
                outs = o + ' ' + r,
                ins2 = i + ' ' + ibr,
                outs2 = o + ' ' + ibr,
                root = ns.getRoot();
            n1.removeClass(reg);
            n2.removeClass(reg);
            switch (v) {
                case 'flat':
                    n1.addClass(flat);
                    n2.addClass(ibr);
                    break;
                case 'inset':
                    n1.addClass(ins);
                    n2.addClass(ibr);
                    break;
                case 'outset':
                    n1.addClass(outs);
                    n2.addClass(ibr);
                    break;
                case 'groove':
                    n1.addClass(ins);
                    n2.addClass(outs2);
                    break;
                case 'ridge':
                    n1.addClass(outs);
                    n2.addClass(ins2);
                    break;
            }

            //force to resize
            ns.box._setB(ns);

            if (adjust)
                ns.adjustSize();
        },
        _adjustSideBar: function (prf, sideBarStatus, sideBarType) {
            var ns = prf,
                prop = ns.properties,
                reg = /^ood-uisb-/,
                arr = sideBarType.split('-'),
                node = ns.getSubNode('SIDEBAR');
            node.removeClass(reg).addClass('ood-uisb-' + (sideBarStatus == 'expand' ? arr[0] : (arr[1] || arr[0])));
            ns.box._sbicon(ns, sideBarStatus, arr[1] || arr[0], true);
            ns.box._borderType(ns, prop.borderType, sideBarStatus, arr, false);
        },
        _setB: function (profile) {
            var p = profile.properties,
                type = p.borderType,
                nd = profile.getSubNode("BORDER"),
                w = nd._borderW('left');
            p.$hborder = p.$vborder = p.$iborder = 0;

            if (type == 'flat' || type == 'inset' || type == 'outset') {
                p.$hborder = p.$vborder = w;
                p.$iborder = 0;
            }
            else if (type == 'groove' || type == 'ridge') {
                p.$hborder = p.$vborder = p.$iborder = w;
            }
        },
        LayoutTrigger: function () {
            var prop = this.properties,
                m = prop.sideBarStatus,
                v = prop.borderType;
            if (v != 'none') this.boxing().setBorderType(v, true);
            if (m == 'fold') this.boxing().setSideBarStatus('fold', true);
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                a = data.sideBarType.split('-'),
                b = data.sideBarStatus;
            data.background = data.background ? 'background:' + data.background : '';
            if (ood.isStr(data.overflow))
                data._overflow = data.overflow.indexOf(':') != -1 ? (data.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");

            data._sidebar = 'ood-uisb-' + (b == 'expand' ? a[0] : (a[1] || a[0]));
            data._sidebarStatus = b == 'fold' ? profile.getClass('KEY', '-fold') : '';
            data._fi_btn = profile.box._sbicon(profile, b, a[1] || a[0]);

            return data;
        },

        _onresize: function (profile, width, height) {
            var size = arguments.callee.upper.apply(this, arguments),
                root = profile.getRoot(),
                border = profile.getSubNode('BORDER'),
                panel = profile.getSubNode('PANEL'),
                sidebar = profile.getSubNode('SIDEBAR'),
                sbcap = profile.getSubNode('SBCAP'),
                prop = profile.properties,
                sbs = prop.sideBarStatus,
                sbtype = prop.sideBarType.split('-'),
                cb1 = border.contentBox(),
                bv = (prop.$vborder || 0) * 2,
                bh = (prop.$hborder || 0) * 2,

                cb2 = panel.contentBox(),
                b2 = (prop.$iborder || 0) * 2,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },

                fzrate = profile.getEmSize() / root._getEmSize(),
                panelfz = panel._getEmSize(fzrate),

                // caculate by px
                ww = width ? profile.$px(size.width) : size.width,
                hh = height ? profile.$px(size.height) : size.height,
                sbsize = profile.$px(prop.sideBarSize),
                sbsize2 = adjustunit(sbsize);

            sbtype = sbs == 'expand' ? sbtype[0] : (sbtype[1] || sbtype[0]);

            size.left = size.top = 0;
            if (sbtype && sbtype != 'none') {
                sbcap.css('line-height', adjustunit(sbsize - (!cb1 ? 0 : bh)));
                if (sbtype == 'left' || sbtype == 'right') {
                    sidebar.width(sbsize2);
                    if (height && 'auto' !== height)
                        sidebar.height(adjustunit(hh - (cb1 ? 0 : bv)));
                } else {
                    sidebar.height(sbsize2);
                    sidebar.width(adjustunit(ww - (cb1 ? 0 : bh)));
                }

                if (sbs == 'fold') {
                    if (sbtype == 'left' || sbtype == 'right') {
                        root.width(adjustunit(sbsize + bh));
                        border.width(adjustunit(sbsize + (cb1 ? 0 : bh)));
                    } else {
                        root.height(adjustunit(sbsize + bv));
                        border.height(adjustunit(sbsize + (cb1 ? 0 : bv)));
                    }
                    return;
                } else {
                    if (sbtype == 'left' || sbtype == 'right') {
                        root.width(adjustunit(width));
                        border.width(adjustunit(ww));
                    } else {
                        root.height(adjustunit(height));
                        border.height(adjustunit(hh));
                    }
                    switch (sbtype) {
                        case 'left':
                            ww -= sbsize;
                            size.left = sbsize;
                            break;
                        case 'right':
                            ww -= sbsize;
                            break;
                        case 'top':
                            hh -= sbsize;
                            size.top = sbsize;
                            break;
                        case 'bottom':
                            hh -= sbsize;
                            break;
                    }
                }
            }
            if (size.width) size.width = adjustunit(ww - (cb1 ? 0 : bh) - (!cb2 ? 0 : b2), panelfz);
            if (size.height && 'auto' !== size.height)
                size.height = adjustunit(hh - (cb1 ? 0 : bv) - (!cb2 ? 0 : b2), panelfz);
            panel.cssRegion(size, true);

            // if (size.width) {
            //     ood.UI._adjustConW(profile, panel, size.width);
            // }
        },


        // _onresize: function (profile, width, height) {
        //     var size = arguments.callee.upper.apply(this, arguments),
        //         root = profile.getRoot(),
        //         border = profile.getSubNode('BORDER'),
        //         panel = profile.getSubNode('PANEL'),
        //         sidebar = profile.getSubNode('SIDEBAR'),
        //         sbcap = profile.getSubNode('SBCAP'),
        //         prop = profile.properties,
        //         sbs = prop.sideBarStatus,
        //         sbtype = prop.sideBarType.split('-'),
        //         cb1 = border.contentBox(),
        //         bv = (prop.$vborder || 0) * 2,
        //         bh = (prop.$hborder || 0) * 2,
        //
        //         cb2 = panel.contentBox(),
        //         b2 = (prop.$iborder || 0) * 1,
        //         us = ood.$us(profile),
        //         adjustunit = function (v, emRate) {
        //             return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
        //         },
        //
        //         fzrate = profile.getEmSize() / root._getEmSize(),
        //         panelfz = panel._getEmSize(fzrate),
        //
        //         // caculate by px
        //         ww = width ? profile.$px(size.width) : size.width,
        //         hh = height ? profile.$px(size.height) : size.height,
        //         sbsize = profile.$px(prop.sideBarSize),
        //         sbsize2 = adjustunit(sbsize);
        //
        //     sbtype = sbs == 'expand' ? sbtype[0] : (sbtype[1] || sbtype[0]);
        //
        //     size.left = size.top = 0;
        //     if (sbtype && sbtype != 'none') {
        //         sbcap.css('line-height', adjustunit(sbsize - (!cb1 ? 0 : bh)));
        //         if (sbtype == 'left' || sbtype == 'right') {
        //             sidebar.width(sbsize2);
        //             if (height && 'auto' !== height)
        //                 sidebar.height(adjustunit(hh - (cb1 ? 0 : bv)));
        //         } else {
        //             sidebar.height(sbsize2);
        //             sidebar.width(adjustunit(ww - (cb1 ? 0 : bh)));
        //         }
        //
        //         if (sbs == 'fold') {
        //             if (sbtype == 'left' || sbtype == 'right') {
        //                 root.width(adjustunit(sbsize + bh));
        //                 border.width(adjustunit(sbsize + (cb1 ? 0 : bh)));
        //             } else {
        //                 root.height(adjustunit(sbsize + bv));
        //                 border.height(adjustunit(sbsize + (cb1 ? 0 : bv)));
        //             }
        //             return;
        //         } else {
        //             if (sbtype == 'left' || sbtype == 'right') {
        //                 root.width(adjustunit(width));
        //                 border.width(adjustunit(ww));
        //             } else {
        //                 root.height(adjustunit(height));
        //                 border.height(adjustunit(hh));
        //             }
        //             switch (sbtype) {
        //                 case 'left':
        //                     ww -= sbsize;
        //                     size.left = sbsize;
        //                     break;
        //                 case 'right':
        //                     ww -= sbsize;
        //                     break;
        //                 case 'top':
        //                     hh -= sbsize;
        //                     size.top = sbsize;
        //                     break;
        //                 case 'bottom':
        //                     hh -= sbsize;
        //                     break;
        //             }
        //         }
        //     }
        //
        //     if (size.width && 'auto' !=size.width) size.width = adjustunit(ww - (cb1 ? 0 : bh) - (!cb2 ? 0 : b2), panelfz);
        //     if (size.height && 'auto' !== size.height)
        //         size.height = adjustunit(hh - (cb1 ? 0 : bv) - (!cb2 ? 0 : b2), panelfz);
        //     panel.cssRegion(size, true);
        //
        //     if (size.width) {
        //         ood.UI._adjustConW(profile, panel, size.width);
        //     }
        // },
        _showTips: function (profile, node, pos) {
            var p = profile.properties;
            if (p.disableTips) return;
            if (profile.onShowTips)
                return profile.boxing().onShowTips(profile, node, pos);

            if (!ood.Tips) return;
            if (p.sideBarType == 'none') return;

            var id = node.id, ks = profile.keys;
            if (p.sideBarStatus == "fold" && (id.indexOf(ks.SBCAP) === 0 || id.indexOf(ks.SBBTN) === 0)) {
                ood.Tips.show(pos, {tips: ood.wrapRes('$inline.Expand')});
                return false;
            } else if (p.sideBarStatus == "expand" && id.indexOf(ks.SBBTN) === 0) {
                ood.Tips.show(pos, {tips: ood.wrapRes('$inline.Fold')});
                return false;
            }
        }
    }
});

