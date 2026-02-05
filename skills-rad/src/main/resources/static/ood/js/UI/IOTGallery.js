ood.Class("ood.UI.Gallery", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [{id: 'a', caption: 'item 1', imageClass: 'ri-number-1'}, {id: 'b', caption: 'item 2', imageClass: 'ri-number-2'}, {id: 'c', caption: 'item 3', imageClass: 'ri-number-3'}, {id: 'd', caption: 'item 4', imageClass: 'ri-number-4', disabled: true}],
            value: 'a',
            width: '32em',
            height: '20em'
        },

        getStatus: function (id) {
            var item = this.get(0).getItemByItemId(id);
            return (item && item._status) || 'ini';
        },
      _afterInsertItems: function (profile) {
            profile.getSubNodes("IMAGE", true).each(function (o) {
                if (o.src == ood.ini.img_bg) {
                    // bug fix for firefox
                    if (ood.browser.isFF) o.src = '';
                    o.src = o.title;
                    o.title = '';
                }
            });
        },
        updateItemData: function (profile, item) {
            this.get(0).box._prepareItem( this.get(0), item);
            this.get(0).boxing().refresh();
        }

    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();
        t.$submap = {

        };
        this.setTemplate(t);

        // compitable
        ood.UI.IconList = ood.UI.Gallery;
        var key = "ood.UI.IconList";
        ood.absBox.$type[key.replace("ood.UI.", "")] = ood.absBox.$type[key] = key;
    },
    Static: {
        IMGNODE: 1,
        Appearances: {
            EXTRA: {
                display: 'none'
            },
            KEY: {
                overflow: 'visible'
            },
            ITEMS: {
                position: 'relative',
                overflow: 'auto',
                'overflow-x': 'hidden',
                zoom: ood.browser.ie6 ? 1 : null
            },
            'ITEMS-nowrap': {
                'white-space': 'nowrap'
            },
            ITEM: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie67 ? 1 : null,
                //   'background-image': 'url({_bgimg})',
                position: 'relative',
                cursor: 'pointer',
                'vertical-align': 'top',
                margin: 0
            },
            ITEMFRAME: {
                display: ood.browser.ie67 ? ood.$inlineBlock : 'block',
                zoom: ood.browser.ie67 ? 1 : null,
                position: 'relative',
                overflow: 'hidden',
                border: 0,
                padding: 0,
                margin: 0,
//                width:'100%',
//                height:'100%',
                '-moz-box-flex': '1'
            },
            IBWRAP: {},
            IMAGE: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                visibility: 'hidden',
                'vertical-align': 'middle'
            },
            CAPTION: {
                'text-align': 'center',
                overflow: 'hidden',
                'white-space': 'nowrap',
                'font-weight': 'bold',
                "font-size": "var(--ood-font-lg)",
                "margin-top": "var(--ood-spacing-md)",
                "height": "35px",
                "color": "var(--ood-text-primary)"
            },
            CONTENT: {
                'text-align': 'center',
                'white-space': 'nowrap',
                'background-repeat': 'no-repeat',
                //  'background-position': 'center center',
                'font-size': 'var(--ood-font-base)',
                "color": "var(--ood-text-secondary)"
            },
            COMMENT: {
                display: 'block',
                margin: 'var(--ood-spacing-xs)',
                'text-align': 'center',
                'font-size': 'var(--ood-font-sm)',
                "color": "var(--ood-text-tertiary)"
            },
            FLAG: {
                'width': "2em",
                "height": "2em",
                'right': "var(--ood-spacing-lg)",
                'top': "var(--ood-spacing-sm)",
                'font-size': "var(--ood-font-md)",
                'color': "var(--ood-accent)",
                'position': 'absolute',
                'z-index': 10
            }
        },
        Behaviors: {
            IMAGE: {
                onLoad: function (profile, e, src) {
                    var img = ood.use(src).get(0), path = img.src;
                    if (path != ood.ini.img_bg) {
                        var p = profile.properties,
                            nn = ood.use(src),
                            node = nn.get(0),
                            item = profile.getItemByDom(src);
                        if (!item) return;
                        var icon = profile.getSubNodeByItemId('ICON', item.id);
                        if (item.autoImgSize || p.autoImgSize) {
                            nn.attr('width', '');
                            nn.attr('height', '');
                        } else {
                            nn.attr('width', item.imgWidth);
                            nn.attr('height', item.imgWidth);
                        }

                        icon.removeClass('ood-icon-loading');
                        // hide
                        if (!item.iconFontCode && !item.imageClass && !item.icon) {
                            icon.addClass("ood-display-none");
                        }
                        nn.onLoad(null).onError(null).$removeEventHandler('load').$removeEventHandler('error');

                        item._status = 'loaded';
                        // don't show img_blank
                        if (ood.ini.img_blank == path) {
                            node.style.visibility = "hidden";
                            node.style.display = "none";
                        } else {
                            node.style.visibility = "visible";
                            node.style.display = "";
                        }
                    }
                },
                onError: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    if (item._status == 'error') return;

                    var p = profile.properties,
                        nn = ood.use(src),
                        node = nn.get(0),
                        icon = profile.getSubNodeByItemId('ICON', item.id);

                    icon.removeClass('ood-icon-loading ood-display-none').addClass('ood-load-error');
                    nn.onLoad(null).onError(null).$removeEventHandler('load').$removeEventHandler('error');
                    node.style.visibility = "hidden";
                    node.style.display = "none";
                    item._status = 'error';
                }
            },
            FLAG: {
                onClick: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.onFlagClick) {
                        box.onFlagClick(profile, item, e, src);
                        return false;
                    }
                }
            }
        },
        DataModel: {
            tagCmds: null,
            expression:{
                ini:'',
                caption: ood.getResText("DataModel.expression") || "表达式",
                action:function () {
                }
            },
            bgimg: null,
            iotStatus: null,
            tagCmdsAlign: null,
            autoImgSize: {
                ini: false,
                caption: ood.getResText("DataModel.autoImgSize") || "自动图像大小",
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemSize: {
                ini: true,
                caption: ood.getResText("DataModel.autoItemSize") || "自动项目大小",
                action: function () {
                    this.boxing().refresh();
                }
            },
            iconOnly: {
                ini: false,
                caption: ood.getResText("DataModel.iconOnly") || "仅显示图标",
                action: function () {
                    this.boxing().refresh();
                }
            },
            iconFontSize: {
                ini: '',
                caption: ood.getResText("DataModel.iconFontSize") || "图标字体大小",
                action: function (v) {
                    this.getSubNode('ICON', true).css('font-size', v);
                }
            },
            itemMargin: {
                ini: 6,
                caption: ood.getResText("DataModel.itemMargin") || "项目边距",
                action: function (v) {
                    this.getSubNode('ITEM', true).css('margin', v || 0);
                }
            },
            itemPadding: {
                ini: 2,
                caption: ood.getResText("DataModel.itemPadding") || "项目内边距",
                action: function (v) {
                    this.getSubNode('ITEM', true).css('padding', v || 0);
                }
            },
            itemWidth: {
                $spaceunit: 1,
                ini: 32,
                caption: ood.getResText("DataModel.itemWidth") || "项目宽度",
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).width(v || '');
                }
            },
            itemHeight: {
                $spaceunit: 1,
                ini: 32,
                caption: ood.getResText("DataModel.itemHeight") || "项目高度",
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).height(v || '');
                }
            },
            imgWidth: {
                ini: 16,
                caption: ood.getResText("DataModel.imgWidth") || "图像宽度",
                action: function (v) {
                    this.getSubNode('IMAGE', true).width(v || '');
                }
            },
            imgHeight: {
                ini: 16,
                caption: ood.getResText("DataModel.imgHeight") || "图像高度",
                action: function (v) {
                    this.getSubNode('IMAGE', true).height(v || '');
                }
            },
            width: {
                $spaceunit: 1,
                ini: '16rem',
                caption: ood.getResText("DataModel.width") || "宽度"
            },
            height: {
                $spaceunit: 1,
                ini: '16rem',
                caption: ood.getResText("DataModel.height") || "高度"
            },
            columns: {
                ini: 0,
                caption: ood.getResText("DataModel.columns") || "列数",
                action: function () {
                    this.boxing().refresh();
                }
            },
            rows: {
                ini: 0,
                caption: ood.getResText("DataModel.rows") || "行数",
                action: function () {
                    this.boxing().refresh();
                }
            }
        },
        EventHandlers: {
            onCmd: null,
            onFlagClick: function (profile, item, e, src) {
            }
        },
        _prepareData: function (profile) {
            var d = arguments.callee.upper.call(this, profile), p = profile.properties;
            if (p.cols) d._itemscls1 = profile.getClass('ITEMS', '-nowrap');
            return d;
        },


        _prepareItem: function (profile, item) {
            var p = profile.properties,
                cols = p.columns,
                rows = p.rows,
                auto1 = item.autoItemSize || p.autoItemSize,
                auto2 = item.autoImgSize || p.autoImgSize,
                t;

            ood.arr.each(ood.toArr('itemWidth,bgimg,iotStatus,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,autoItemSize,autoImgSize'), function (i) {
                item[i] = ood.isSet(item[i]) ? item[i] : p[i];
            });
            item.itemWidth = (!auto1 && (t = item.itemWidth)) ? profile.$forceu(t) : '';
            item.itemHeight = (!auto1 && (t = item.itemHeight)) ? profile.$forceu(t) : '';
            item.itemMargin = (t = item.itemMargin) ? profile.$forceu(t) : 0;
            item.itemPadding = (t = item.itemPadding) ? profile.$forceu(t) : 0;
            item.imgWidth = (!auto2 && (t = item.imgWidth)) ? profile.$forceu(t) : '';
            item.imgHeight = (!auto2 && (t = item.imgHeight)) ? profile.$forceu(t) : '';
            item._tabindex = p.tabindex;

            if (item.icon) item._icon = "background-image: url(" + item.icon + ")";

            switch (item.iotStatus) {
                case 'on':
                    item._position = 'background-position: right center';
                    item._color = ' color:var(--ood-status-on)';
                    break;
                case  'off' :
                    item._position = 'background-position: left center';
                    item._color = ' color:var(--ood-status-off)';
                    break;
                case  'none' :
                    item._position = 'background-position: center center';
                    item._color = ' color:var(--ood-status-neutral)';
                    break;
                case  'alarm' :
                    item._position = 'background-position: center center';
                    item._color = ' color:var(--ood-status-alarm)';
                    break;
                default :
                    item._position = ' ';
                    item._color = ' ';
            }
            ;


            if (item.bgimg) item._bgimg = "background-image: url(/" + item.bgimg + ")";

            if (t = item.iconFontSize) item._fontSize = "font-size:" + t;

            item._imageClass = '';
            if (!item.iconFontCode && !item.imageClass) item._imageClass += 'ood-icon-loading';

            if (item.imageClass) {
                item._imageClass += ' ' + item.imageClass;
            } else {
                item._imageClass = ' icon1';
            }

            if (item.flagText || item.flagClass) item._flagStyle = 'display:block';
            if (!item.flagClass) item.flagClass = 'ood-uiflag-1';

            if (p.iconOnly) {
                delete item.caption;
                delete item.comment;
            }

            if ((item.caption = item.caption || '') === '') item.capDisplay = 'display:none;';
            if ((item.comment = item.comment || '') === '') item.commentDisplay = 'display:none;';
            item._itemSize = '';
            if (cols)
                item._itemSize += 'width:' + (100 / cols + '%') + ';border:0;margin-left:0;margin-right:0;padding-left:0;padding-right:0;';
            if (rows)
                item._itemSize += 'height:' + (100 / rows + '%') + ';border:0;margin-top:0;margin-bottom:0;padding-top:0;padding-bottom:0;';

            if (!auto1) item._inneritemSize = (!cols && item.itemWidth ? ('width:' + item.itemWidth + ';') : '') +
                (!rows && item.itemHeight ? ('height:' + item.itemHeight) : '');
            if (!auto2)
                item._innerimgSize = (item.imgWidth ? ('width:' + item.imgWidth + ';') : '') + (!rows && item.imgHeight ? ('height:' + item.imgHeight) : '');
        }
        ,
        RenderTrigger: function () {
            this.boxing()._afterInsertItems(this);
        }
    }
});
