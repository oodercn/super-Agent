ood.Class("ood.UI.MTreeView", "ood.UI.TreeBar", {
    Initialize: function () {
        this.addTemplateKeys(['IMAGE']);
        var t = this.getTemplate();
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if(!t){
            t = {};
        }
        // 确保$submap和所有必要的嵌套属性存在
        if(!t.$submap) t.$submap = {};
        if(!t.$submap.items) t.$submap.items = {};
        if(!t.$submap.items.ITEM) t.$submap.items.ITEM = {};
        if(!t.$submap.items.ITEM.BAR) t.$submap.items.ITEM.BAR = {};
        if(!t.$submap.items.ITEM.BAR.ITEMICON) t.$submap.items.ITEM.BAR.ITEMICON = {};
        
        t.$submap.items.ITEM.BAR.className = 'ood-uitembg ood-uiborder-radius ood-showfocus {cls_group} {cls_fold} {_split} {disabled} {readonly}';
        var n = t.$submap.items.ITEM.BAR.ITEMICON;
        n.$fonticon = '{_fi_cls_file}';
        this.setTemplate(t);
    },
    
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            animCollapse: true,
            items: [
                {
                    id: 'node1', sub: [
                        {id: 'node12', imageClass: "ri-image-line"}
                    ]
                }, {
                    id: 'node2',
                    initFold: false
                }]
        }
    },
    
    Static: {

        Appearances: {
            ITEMS: {
                //overflow: 'visible'
                'padding': '.0em'
            },
            ITEM: {
                'white-space': 'nowrap',
                position: 'relative',
                'line-height': 1.22,
                //  padding: '0.25em 0 0.25em 0',
                'border-width': '1px',
                'border-bottom-style': 'solid',
                'border-color': 'var(--ood-border-light)',
                overflow: 'hidden'
            },
            MARK: {
                position: 'absolute',
                left: 'auto',
                top: '50%',
                'margin-top': '-0.5em',
                right: '.167em',
                $order: 12,
                color: "var(--ood-success)",
                cursor: 'pointer',
                'vertical-align': 'middle'
            },
            BAR: {
                zoom: ood.browser.ie ? 1 : null,
                position: 'relative',
                height: "2em",
                'border-width': '1px',
                'border-top-style': 'solid',
                'border-color': '#DDDDDD',
                display: 'block',
                'outline-offset': '-1px',
                '-moz-outline-offset': (ood.browser.gek && ood.browser.ver < 3) ? '-1px !important' : null
            },
            SUB: {
                zoom: ood.browser.ie ? 1 : null,
                height: 0,
                'font-size': ood.browser.ie68 ? '1px' : null,
                //1px for ie8
                'line-height': ood.browser.ie68 ? '1px' : null,
                position: 'relative',
                overflow: 'hidden'
            },
            BOX: {
                left: '0em',
                overflow: 'auto',
                position: 'relative'
            }
        },
        Behaviors: {
            MARK: {
                onClick: function (profile, e, src) {
                    profile.box._onclickbar(profile, e, ood.use(src).parent().xid());
                    return false;
                }
            },
            ITEMICON: {
                onClick: function (profile, e, src) {
                    profile.box._onclickbar(profile, e, ood.use(src).parent().xid());
                    return false;
                }
            }
        },
        DataModel: {
            expression: {
                ini: '',
                action: function () {
                }
            },
            $subMargin: 1.8,
            group: null,
            noIcon: {
                ini: true,
                action: function (v) {
                    this.getSubNode("ITEMICON", true).css('display', v ? 'none' : '');
                }
            }
        },


        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties,
                map1 = profile.ItemIdMapSubSerialId,
                map2 = profile.SubSerialIdMapItem,
                pitem;

            profile.boxing()._autoColor(item, index, p);

            if (item.fiCheck) {
                item._fi_check = item.fiCheck;
            } else {
                item._fi_check = 'ood-uimcmd-check';
            }

            if (pid) {
                oitem._pid = pid;
                if (pitem = map2[map1[pid]]) {
                    oitem._deep = pitem._deep + 1;
                    item.rulerStyle = 'width:' + (oitem._deep * p.$subMargin) + 'em;';
                    // for the last one
                    item._fi_togglemark = item.sub ? ('ood-uimcmd-toggle' + (item._checked ? '-checked' : '')) : (p.togglePlaceholder ? 'ood-uicmd-empty' : 'ood-uicmd-none');
                }
            } else {
                oitem._deep = 0;
                item.rulerStyle = '';
                item.innerIcons = '';
                item._fi_togglemark = item.sub ? ('ood-uimcmd-toggle' + (item._checked ? '-checked' : '')) : (p.togglePlaceholder ? 'ood-uicmd-empty' : 'ood-uicmd-none');
            }
            // show image
            item.imageDisplay = (item.noIcon || p.noIcon) ? "display:none;" : "";
            //
            item.cls_fold = item.sub ? profile.getClass('BAR', '-fold') : '';

            if (!(item.imageClass || item.image || item.iconFontCode))
                item._fi_cls_file = 'ood-icon-file' + (item.sub ? ' ood-icon-file-fold' : '');

            item._fi_optClass = p.optBtn;

            item.disabled = item.disabled ? 'ood-ui-disabled' : '';
            item._itemDisplay = item.hidden ? 'display:none;' : '';

            item.mark2Display = ('showMark' in item) ? (item.showMark ? '' : 'display:none;') : ((p.selMode == 'singlecheckbox' && !item.sub) || p.selMode == 'multibycheckbox') ? '' : 'display:none;';
            //          item.mark2Display = ('showMark' in item)?(item.showMark?'':'display:none;'):(p.selMode=='multi'||p.selMode == 'single'||p.selMode=='multibycheckbox')?'':'display:none;';

            item._tabindex = p.tabindex;
            this._prepareCmds(profile, item);

            if (item.type == 'split') {
                item._split = 'ood-uitem-split';
                item._splitstyle = 'margin-left:' + (oitem._deep * p.$subMargin) + 'em;';
                item._ruleDisplay = item._ltagDisplay = item._tglDisplay = item._rtagDisplay = item.imageDisplay = item.mark2Display = item._capDisplay = item._extraDisplay = item._optDisplay = 'display:none;';
            }
        },
        _tofold: function (profile, item, pid) {
            var cls = profile.getClass('IMAGE');
            profile.getSubNodeByItemId('BAR', pid).addClass(profile.getClass('BAR', '-fold'));
            profile.getSubNodeByItemId('TOGGLE', pid).replaceClass(new RegExp("\\b" + cls + "-path([-\\w]+)\\b"), cls + '-fold$1');
        }
    }
});
