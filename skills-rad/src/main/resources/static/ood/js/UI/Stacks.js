ood.Class("ood.UI.Stacks", "ood.UI.Tabs", {
    Initialize: function () {// 检查模板是否存在，如果不存在则创建一个默认的模板对象
        var t = this.getTemplate(), keys = this.$Keys;
        // 如果t是undefined，创建一个默认的模板对象
        if (!t) {
            t = {};
            t.LIST = {};
            t.PNAELS = {};
        }
        // 确保LIST属性存在
        if (!t.LIST) t.LIST = {};
        // 确保PNAELS属性存在
        if (!t.PNAELS) t.PNAELS = {};
        t.BOX = {tagName: 'div', LIST: t.LIST, PNAELS: t.PNAELS};
        // 安全地删除LIST的属性
        if (t.LIST) {
            delete t.LIST.LEFT;
            delete t.LIST.RIGHT;
            delete t.LIST.DROP;
            delete t.LIST;
        }
        delete t.PNAELS;
        t.$submap.items.ITEM.className = 'ood-uibar ood-uiborder-t ood-uiborder-b';
        t.$submap.items.ITEM.ITEMI.ITEMC.HANDLE.IBWRAP.TOGGLE = {
            $order: 1,
            className: 'oodfont {_tlgchecked}',
            $fonticon: 'ood-uicmd-toggle'
        };
        this.setTemplate(t);
        delete keys.LEFT;
        delete keys.RIGHT;
        delete keys.DROP;
    },
    
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [{id: 'a', caption: 'tab1', message: "normal"}, {id: 'b', caption: 'tab2', message: "with image", imageClass: "ri-image-line"}, {id: 'c', caption: 'tab3', message: "height:100", height: 100}, {
                id: 'd',
                caption: '$RAD.widgets.collapsible',
                message: "with commands",
                closeBtn: true,
                optBtn: 'ood-uicmd-opt',
                popBtn: true
            }],
            autoItemColor: true,
            value: 'a',
            caption: '$RAD.widgets.folding'
        }
    },
    
    Static: {
        Behaviors: {
            DroppableKeys: ['PANEL', 'KEY', 'ITEM']
        },
        Appearances: {
            BOX: {
                position: 'absolute',
                overflow: 'hidden',
                left: 0,
                top: 0,
                'background-color': 'var(--ood-bg)',
                'border': '1px solid var(--ood-border)',
                'border-radius': 'var(--ood-radius-lg)'
            },
            LIST: {
                position: 'static'
            },
            ITEMS: {
                position: 'static'
            },
            ITEM: {
                $order: 0,
                display: 'block',
                position: 'absolute',
                cursor: 'pointer',
                width: '100%',
                left: 0,
                'background-color': 'var(--ood-bg)',
                'border-bottom': '1px solid var(--ood-border)',
                transition: 'background-color 0.2s ease'
            },
            'ITEM:hover': {
                'background-color': 'var(--ood-bg-hover)'
            },
            'ITEM-checked': {
                'background-color': 'var(--ood-primary-light)'
            },
            ITEMC: {
                display: 'block'
            },
            ITEMI: {
                display: 'block'
            },
            HANDLE: {
                cursor: 'pointer',
                display: 'block',
                'white-space': 'nowrap'
            },
            PANEL: {
                position: 'absolute',
                display: 'none',
                overflow: 'auto',
                'background-color': 'var(--ood-bg)',
                padding: 'var(--ood-spacing-md)'
            },
            CMDS: {
                position: 'absolute',
                top: 'var(--ood-spacing-sm)',
                right: 'var(--ood-spacing-md)',
                'text-align': 'right',
                'vertical-align': 'middle'
            }
        },
        DataModel: {
            expression: {
                ini: '', caption: ood.getResText("DataModel.expression") || "表达式", action: function () {
                }
            },
            noPanel: {ini: null, caption: ood.getResText("DataModel.noPanel") || "无面板"},
            noHandler: {ini: null, caption: ood.getResText("DataModel.noHandler") || "无处理器"},
            selMode: {ini: null, caption: ood.getResText("DataModel.Stacks.selMode") || "选择模式"},
            borderType: {
                ini: 'flat',
                caption: ood.getResText("DataModel.borderType") || "边框类型",
                listbox: ['none', 'flat', 'inset', 'outset'],
                action: function (v) {
                    var ns = this, p = ns.properties, n1 = ns.getSubNode('BOX'), reg = /^ood-uiborder-/,
                        flat = 'ood-uiborder-flat  ood-uiborder-radius',
                        ins = 'ood-uiborder-inset  ood-uiborder-radius',
                        outs = 'ood-uiborder-outset  ood-uiborder-radius', root = ns.getRoot();
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
                    ns.adjustSize();
                }
            }
        },
        LayoutTrigger: function () {
            var v = this.properties.borderType;
            if (v && v != 'none') this.boxing().setBorderType(v, true);
        },
        _onresize: function (profile, width, height, force, key) {
            var prop = profile.properties,
                noPanel = prop.noPanel,
                cb = ood.browser.contentBox,
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
                box = profile.getSubNode('BOX'),
                list = profile.getSubNode('LIST'),

                fzrate = profile.getEmSize() / root._getEmSize(),
                panelfz = panel._getEmSize(fzrate),
                listfz = list._getEmSize(fzrate),

                type = prop.borderType,
                // have to use borderLeftWidth( ff will result 0 with borderWidth)
                bw = !cb ? 0 : (type == 'flat' || type == 'inset' || type == 'outset') ? box._borderW() : 0,
                wc = null,
                hc = null,
                off,
                temp, t1, t2, obj, top;

            if (!panel || panel.isEmpty()) return;

            // caculate by px
            width = width ? profile.$px(width, null, true) : width;
            height = height ? profile.$px(height, null, true) : height;

            // change value
            if (height) {
                height -= bw;
                t2 = t1 = 0;
                ood.arr.each(prop.items, function (o) {
                    obj = profile.getSubNodeByItemId('ITEM', o.id);
                    obj.cssRegion({bottom: 'auto', top: adjustunit(t1, obj)});

                    // force to get offsetHeight
                    off = o.hidden ? 0 : obj.offsetHeight(true);
                    t1 += off
                    if (o.id == key) return false;
                });
                ood.arr.each(prop.items, function (o) {
                    if (o.id == key) return false;
                    obj = profile.getSubNodeByItemId('ITEM', o.id);
                    obj.cssRegion({top: 'auto', bottom: adjustunit(t2, obj)});

                    // offsetHeight maybe not set here
                    off = o.hidden ? 0 : obj.offsetHeight(true);
                    t2 += off
                }, null, true);

                temp = height - t1 - t2;
                if (temp > 0) {
                    top = t1;
                    hc = temp;
                }

                box.height(adjustunit(height));
            }
            if (width) {
                width -= bw;
                wc = width;
                box.width(adjustunit(width));
            }

            panel.cssRegion({
                width: wc ? adjustunit(wc, panelfz) : null,
                height: hc ? adjustunit(hc, panelfz) : null,
                top: adjustunit(top, panelfz),
                left: 0 + profile.$picku()
            }, true);
            if (wc) {
                list.width(wc = adjustunit(wc, listfz));
                ood.UI._adjustConW(profile, panel, wc);
            }
        },
        _adjustScroll: null
    }
});
