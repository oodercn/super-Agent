ood.Class("ood.UI.TitleBlock", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            "items": [
                {
                    "id": "item1",
                    "title": "信息报送",
                    "more": "&gt;&gt; 更多",
                    "msgnum": "1",
                    "flagClass": "ri-grid-line",
                    "caption": "item1"
                },
                {
                    "id": "item2",
                    "title": "日常审批",
                    "more": "&gt;&gt; 更多",
                    "msgnum": "21",
                    "flagClass": "bpmfont bpmVueFlyActivityOperation",
                    "caption": "item2"
                },
                {
                    "id": "item3",
                    "title": "会议通知",
                    "more": "&gt;&gt; 更多",
                    "msgnum": "7",
                    "flagClass": "bpmfont bpmhuiyitongzhi2",
                    "caption": "item3"
                },
                {
                    "id": "item4",
                    "title": "请销假",
                    "more": "&gt;&gt; 更多",
                    "msgnum": "0",
                    "flagClass": "ri ri-time-line",
                    "caption": "item4"
                }
            ],
            "dock": "fill",
            "selMode": "none",
            "borderType": "none",
            "columns": 2
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
                    style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize}{itemStyle};{_itemColor}',

                    ITEMFRAME: {
                        style: '{_inneritemSize};{_bgColor}; {_position}',
                        MSGNUM: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay}; {_fontColor}',
                            text: '{msgnum}',
                            $order: 0
                        },
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay};{_fontColor}',
                            text: '{_title}',
                            $order: 3,
                            IMAGE: {
                                tagName: 'img',
                                src: ood.ini.img_bg,
                                title: '{image}',
                                style: '{_innerimgSize};{imgStyle};{_iconColor}}'
                            }

                        },
                        MORE: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay};{_moreBgColor};',
                            text: '{more}',
                            $order: 4
                        }

                    },
                    FLAG: {
                        $order: 20,
                        className: 'ood-display-none {flagClass}',
                        style: '{_flagStyle};{flagStyle};font-size:{iconFontSize};{_fontColor}',
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
        ood.UI.IconList = ood.UI.TitleBlock;
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
                '-moz-box-flex': '1'
            },
            IBWRAP: {},
            IMAGE: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                visibility: 'hidden',
                'vertical-align': 'middle'
            },
            MSGNUM: {
                'text-align': 'right',
                overflow: 'hidden',
                "color": "var(--ood-title-text)",
                'white-space': 'nowrap',
                'font-weight': 'bold',
                "font-size": '2em',
                "margin-top": "var(--ood-spacing-xs)",
                "margin-right": "var(--ood-spacing-sm)",
                "height": "35px"
            },

            TITLE: {
                "color": "var(--ood-title-text)",
                display: 'block',
                'margin-right': 'var(--ood-spacing-xs)',
                'text-align': 'right',
                'font-size': '1em'
            },
            MORE: {
                "color": "var(--ood-title-text)",
                display: 'block',
                "background-color": "var(--ood-title-more-bg)",
                'margin-top': 'var(--ood-spacing-sm)',
                "text-align": "center",
                'font-size': '0.75em'
            },
            FLAG: {
                'left': "var(--ood-spacing-sm)",
                'top': "0.2em",
                'text-align': "left",
                'color': "var(--ood-title-text)",
                "opacity": 0.2,
                'position': 'absolute',
                'z-index': 10
            }
        },
        Behaviors: {
            ITEMS: {
                swipeleft: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swipeleft) {
                        box.swipeleft(profile, item, e, src);
                        return false;
                    }
                },
                swiperight: function (profile, item, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swiperight) {
                        box.swipeleft(profile, item, e, src);
                        return false;
                    }
                },
            },
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
            TITLE: {
                onClick: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.onTitleClick) {
                        box.onTitleClick(profile, item, e, src);
                        return false;
                    }
                }
            },
            MORE: {
                onClick: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.onMoreClick) {
                        box.onMoreClick(profile, item, e, src);
                        return false;
                    }
                }
            }
        },

        DataModel: {
            flagText: null,
            flagClass: null,
            flagStyle: null,

            iconColors:null,
            itemColors:null,
            fontColors: null,
            moreColors:null,

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
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoImgSize: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemSize: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },

            iconFontSize: {
                ini: '3.5em',
                action: function (v) {
                    this.getSubNode('FLAG', true).css('font-size', v);
                }
            },

            itemMargin: {
                ini: 0,
                action: function (v) {
                    this.getSubNode('ITEM', true).css('margin', v || 0);
                }
            },
            itemPadding: {
                ini: 0,
                action: function (v) {
                    this.getSubNode('ITEM', true).css('padding', v || 0);
                }
            },
            itemWidth: {
                $spaceunit: 1,
                ini: 32,
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).width(v || '');
                }
            },
            itemHeight: {
                $spaceunit: 1,
                ini: 32,
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).height(v || '');
                }
            },
            width: {
                $spaceunit: 1,
                ini: '16rem'
            },
            height: {
                $spaceunit: 1,
                ini: '16rem'
            },
            columns: {
                ini: 0,
                action: function () {
                    this.boxing().refresh();
                }
            },
            rows: {
                ini: 0,
                action: function () {
                    this.boxing().refresh();
                }
            }
        },
        EventHandlers: {
            onCmd: null,
            onMoreClick: function (profile, item, e, src) {
            },
            onTitleClick: function (profile, item, e, src) {
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
            },
            swipeleft: function (profile, item, e, src) {
            },
            swiperight: function (profile, item, e, src) {
            },
            swipeup: function (profile, item, e, src) {
            },
            swipedown: function (profile, item, e, src) {
            },


            press: function (profile, item, e, src) {
            },
            pressup: function (profile, item, e, src) {
            }
        },
        _prepareData: function (profile) {
            var d = arguments.callee.upper.call(this, profile), p = profile.properties, ns = this;
            if (p.cols) d._itemscls1 = profile.getClass('ITEMS', '-nowrap');

            ood.arr.each(d.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(d.items, item);
                }
                profile.boxing()._autoColor(item, index, p);
            })

            return d;
        },




        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties,
                cols = p.columns,
                rows = p.rows,
                auto1 = item.autoItemSize || p.autoItemSize,
                t;
            profile.boxing()._autoColor(item, index, p);
            ood.arr.each(ood.toArr('itemWidth,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,iconColor,fontColor,itemColor'), function (i) {
                item[i] = ood.isSet(item[i]) ? item[i] : p[i];
            });


            item.itemWidth = (!auto1 && (t = item.itemWidth)) ? profile.$forceu(t) : '';
            item.itemHeight = (!auto1 && (t = item.itemHeight)) ? profile.$forceu(t) : '';
            item.itemMargin = (t = item.itemMargin) ? profile.$forceu(t) : 0;
            item.itemPadding = (t = item.itemPadding) ? profile.$forceu(t) : 0;
            item._tabindex = p.tabindex;

            if (item.title) {
                item._title = item.title;
            } else if (item.caption) {
                item._title = item.caption;
            }

            if (p.flagClass && !item.flagClass) {
                item.flagClass = p.flagClass;
            }
            if (p.flagStyle && !item.flagStyle) {
                item.flagStyle = p.flagStyle;
            }
            if (p.flagText && !item.flagText) {
                item.flagText = p.flagText;
            }
            //      if (t = item.iconFontSize) item._fontSize = "font-size:" + t;

            item._imageClass = '';
            if (!item.iconFontCode && !item.imageClass) item._imageClass += 'ood-icon-loading';

            if (item.imageClass) {
                item._imageClass += ' ' + item.imageClass;
            } else {
                item._imageClass = ' icon1';
            }

            // if (item.fontColor) item._fontColor = "color:"+ item.fontColor ;
            // if (item.iconColor) item._iconColor = "color:" + item.iconColor ;
            // if (item.itemColor) item._iconColor = "color:" + item.itemColor ;

            // if (item.bgColor) item._bgColor = ' background-color:' + item.bgColor;
            // if (item.moreBgColor) item._moreBgColor = ' background-color:' + item.moreBgColor;
            //

            if (item.flagText || item.flagClass) item._flagStyle = 'display:block';
            if (!item.flagClass) item.flagClass = 'ood-uiflag-1';

            item._itemSize = '';
            if (cols)
                item._itemSize += 'width:' + (100 / cols + '%') + ';border:0;margin-left:0;margin-right:0;padding-left:0;padding-right:0;';
            if (rows)
                item._itemSize += 'height:' + (100 / rows + '%') + ';border:0;margin-top:0;margin-bottom:0;padding-top:0;padding-bottom:0;';

        }
        ,
        RenderTrigger: function () {
            this.boxing()._afterInsertItems(this);
        }
    }
});
