ood.Class("ood.UI.Opinion", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            "columns": 1,
            width: "41.833333333333336em",
            items: [
                {
                    "caption": "同意请执行！",
                    "createDateStr": "2024-01-15",
                    "creatorName": "架构师",
                    "department": "技术部",
                    "id": "opinion1"
                },
                {
                    "caption": "建议增加更多细节说明",
                    "hidden": false,
                    "createDateStr": "2024-01-16",
                    "creatorName": "王五",
                    "department": "产品部",
                    "id": "opinion2"
                },
                {
                    "caption": "经过审核，同意此方案",
                    "hidden": false,
                    "createDateStr": "2024-01-17",
                    "creatorName": "赵六",
                    "department": "管理部",
                    "level": "经理级",
                    "id": "opinion3"
                }
            ]
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
            this.get(0).box._prepareItem(this.get(0), item);
            this.get(0).boxing().refresh();
        }

    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();
        t.$submap = {
            items: {
                ITEM: {
                    tabindex: '{_tabindex}',
                    className: 'ood-uitembg ood-uiborder-radius ood-showfocus {itemClass} {disabled} {readonly}',
                    style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize};{itemStyle})',

                    ITEMFRAME: {
                        style: '{_inneritemSize};{_bgimg}; {_position}}',
                        COMMENT: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            text: '{comment}',
                            style: '{commentDisplay};{_color}',
                            $order: 2
                        },
                        CONTENT: {
                            tagName: 'div',
                            $order: 1,
                            className: '{contentClass}',
                            style: '{_loadbg}',

                            ICON: {
                                className: 'oodfont {_imageClass}',
                                style: "{_fontSize};{_color};{_icon};{_position};}",
                                text: '{iconFontCode}'
                            }
                        },
                        CAPTION: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay}',
                            text: '{caption}',
                            $order: 0
                        },


                        PERSON: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay}',
                            text: '{creatorName}',
                            $order: 3,
                            IMAGE: {
                                tagName: 'img',
                                src: ood.ini.img_bg,
                                title: '{image}',
                                style: '{_innerimgSize};{imgStyle}'
                            }

                        },
                        PERFORTIME: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay}',
                            text: '{createDateStr}',
                            $order: 4
                        }

                    },
                    FLAG: {
                        $order: 20,
                        className: 'ood-display-none {flagClass}',
                        style: '{_flagStyle};{flagStyle}',
                        text: '{flagText}'
                    },
                    EXTRA: {
                        text: '{ext}',
                        $order: 30
                    }
                }
            }
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
                'text-align': 'left',
                overflow: 'hidden',
                'right': "var(--ood-spacing-md)",
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
            TIEM: {
                display: 'block',
                margin: '.25em',
                'text-align': 'center',
                'font-size': '1em'
            },
            PERSON: {
                display: 'block',
                'margin-top': 'var(--ood-spacing-sm)',
                'margin-right': 'var(--ood-spacing-lg)',
                'text-align': 'right',
                'font-size': 'var(--ood-font-base)',
                "color": "var(--ood-text-secondary)"
            },
            PERFORTIME: {
                display: 'block',
                'margin-top': 'var(--ood-spacing-xs)',
                'margin-right': 'var(--ood-spacing-md)',
                "text-align": "right",
                'font-size': 'var(--ood-font-base)',
                "color": "var(--ood-text-tertiary)"
            },


            FLAG: {
                'width': "2em",
                "height": "2em",
                'right': "1.5em",
                'top': "1em",
                'text-align': "left",
                'font-size': "16px",
                'color': "var(--ood-warning)",
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
            expression: {
                ini: '',
                caption: ood.getResText("DataModel.expression") || "表达式",
                action: function () {
                }
            },
            tagCmds: {
                ini: null,
                caption: ood.getResText("DataModel.tagCmds") || "标签命令"
            },
            bgimg: {
                ini: null,
                caption: ood.getResText("DataModel.backgroundImage") || "背景图片"
            },
            iotStatus: {
                ini: null,
                caption: ood.getResText("DataModel.iotStatus") || "IoT状态"
            },
            flagText: {
                ini: null,
                caption: ood.getResText("DataModel.flagText") || "标志文本"
            },
            flagClass: {
                ini: null,
                caption: ood.getResText("DataModel.flagStyleClass") || "标志样式类"

            },
            flagStyle: {
                ini: null,
                caption: ood.getResText("DataModel.flagStyle") || "标志样式"
            },
            tagCmdsAlign: {
                ini: null,
                caption: ood.getResText("DataModel.tagCmdsAlign") || "标签命令对齐"
            },
            autoImgSize: {
                ini: false,
                caption: ood.getResText("DataModel.autoImgSize") || "自动图片大小",
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
                caption:ood.getResText("DataModel.itemMargin") || "项目间距",
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
                caption: ood.getResText("DataModel.imageWidth") || "图片宽度",
                action: function (v) {
                    this.getSubNode('IMAGE', true).width(v || '');
                }
            },
            imgHeight: {
                ini: 16,
                caption: ood.getResText("DataModel.imageHeight") || "图片高度",
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
                caption: ood.getResText("DataModel.cols") || "列数",
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

            if (p.flagClass && !item.flagClass) {
                item.flagClass = p.flagClass;
            }
            if (p.flagStyle && !item.flagStyle) {
                item.flagStyle = p.flagStyle;
            }
            if (p.flagText && !item.flagText) {
                item.flagText = p.flagText;
            }

            if (item.icon) item._icon = "background-image: url(" + item.icon + ")";

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

            if (item.content) {
                item.caption = item.content;
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
