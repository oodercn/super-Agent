ood.Class("ood.UI.ContentBlock", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            "items": [
                {
                    "id": "item1",
                    "title": "上报信息",
                    "datetime": "2024-9-9 12:00",
                    "flagClass": "ri-arrow-left-right-line",
                    "flagText": "管理员",
                    "caption": "item1"
                },
                {
                    "id": "item2",
                    "title": "信息报送",
                    "datetime": "2024-9-9 12:00",
                    "flagClass": "ri ri-drag-move-line",
                    "flagText": "管理员",
                    "caption": "item2"
                }
            ],
            "dock": "fill",
            "selMode": "none",
            "borderType": "none",
            "columns": 1
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
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    itemframes = profile.getSubNode('ITEMFRAME', true),
                    datetimes = profile.getSubNode('DATETIME', true),
                    titles = profile.getSubNode('TITLE', true),
                    mores = profile.getSubNode('MORE', true),
                    flags = profile.getSubNode('FLAG', true),
                    images = profile.getSubNode('IMAGE', true);

                // 清除所有主题类
                root.removeClass('contentblock-dark contentblock-light contentblock-hc');
                
                // 应用主题类
                switch(theme) {
                    case 'dark':
                        root.addClass('contentblock-dark');
                        break;
                    case 'high-contrast':
                        root.addClass('contentblock-hc');
                        break;
                    default:
                        root.addClass('contentblock-light');
                }

                // 清除内联样式
                items.css({
                    'background-color': '',
                    'border-color': '',
                    'color': ''
                });
                itemframes.css({
                    'background-color': ''
                });
                datetimes.css({
                    'color': ''
                });
                titles.css({
                    'color': ''
                });
                mores.css({
                    'background-color': '',
                    'color': ''
                });
                flags.css({
                    'color': ''
                });
                images.css({
                    'filter': ''
                });
                
                // 保存主题设置
                localStorage.setItem('contentblock-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('contentblock-theme') || 'light';
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    items = profile.getSubNode('ITEM', true),
                    itemframes = profile.getSubNode('ITEMFRAME', true),
                    datetimes = profile.getSubNode('DATETIME', true),
                    titles = profile.getSubNode('TITLE', true),
                    mores = profile.getSubNode('MORE', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('contentblock-mobile');
                    
                    // 移动端调整
                    itemframes.css({
                        'padding': '1em'
                    });
                    datetimes.css({
                        'font-size': '20px',
                        'line-height': '28px'
                    });
                    titles.css({
                        'font-size': '22px',
                        'line-height': '32px'
                    });
                    mores.css({
                        'font-size': '0.8em',
                        'padding': '0.5em'
                    });
                } else {
                    root.removeClass('contentblock-mobile');
                    
                    // 恢复桌面样式
                    itemframes.css({
                        'padding': ''
                    });
                    datetimes.css({
                        'font-size': '',
                        'line-height': ''
                    });
                    titles.css({
                        'font-size': '',
                        'line-height': ''
                    });
                    mores.css({
                        'font-size': '',
                        'padding': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('contentblock-tiny');
                    
                    // 优化小屏幕显示
                    datetimes.css({
                        'font-size': '18px',
                        'line-height': '24px'
                    });
                    titles.css({
                        'font-size': '20px',
                        'line-height': '28px'
                    });
                    mores.css({
                        'font-size': '0.7em'
                    });
                } else {
                    root.removeClass('contentblock-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    images = profile.getSubNode('IMAGE', true),
                    mores = profile.getSubNode('MORE', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'list',
                    'aria-label': properties.caption || '内容块列表'
                });
                
                // 为每个内容项添加ARIA属性
                items.each(function(item, index) {
                    var itemNode = ood(item);
                    var itemData = profile.getItemByDom(item);
                    
                    // 基础ARIA属性
                    itemNode.attr({
                        'role': 'listitem',
                        'tabindex': itemData.disabled ? '-1' : '0',
                        'aria-label': itemData.title || itemData.datetime || '内容项',
                        'aria-disabled': itemData.disabled ? 'true' : 'false'
                    });
                    
                    // 如果有日期时间信息
                    if (itemData.datetime) {
                        itemNode.attr('aria-describedby', profile.serialId + '_datetime_' + index);
                        var datetimeNode = itemNode.find('.ood-ui-ellipsis').filter(function() {
                            return ood(this).text() === itemData.datetime;
                        });
                        if (datetimeNode.length) {
                            datetimeNode.attr('id', profile.serialId + '_datetime_' + index);
                        }
                    }
                });
                
                // 为图片添加ARIA属性
                images.each(function(img) {
                    var imgNode = ood(img);
                    var itemData = profile.getItemByDom(img);
                    imgNode.attr({
                        'alt': itemData.title || itemData.datetime || '图片',
                        'role': 'img'
                    });
                });
                
                // 为更多按钮添加ARIA属性
                mores.each(function(more) {
                    ood(more).attr({
                        'role': 'button',
                        'tabindex': '0',
                        'aria-label': '查看更多'
                    });
                });
            });
        },
        
        ContentBlockTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('contentblock-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                this.adjustLayout();
            }

            // 初始化可访问性
            this.enhanceAccessibility();
        }

    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if(!t){
            t = {};
        }
        t.$submap = {
            items: {
                ITEM: {
                    tabindex: '{_tabindex}',
                    className: 'ood-uitembg ood-uiborder-radius ood-showfocus {itemClass} {disabled} {readonly}',
                    style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize};{itemStyle}{_itemColor}',

                    ITEMFRAME: {
                        style: '{_inneritemSize};{_bgColor}; {_position}',
                        DATETIME: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay} ;{_fontColor}',
                            text: '{datetime}',
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
                                style: '{_innerimgSize};{imgStyle};{_iconColor}'
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
                        style: '{_flagStyle};{flagStyle};font-size:{iconFontSize}{_fontColor}',
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
                padding: "32px 0px 20px 0px",
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
                padding:  "10px 32px 10px 32px",
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
            DATETIME: {
                'text-align': 'left',
                overflow: 'hidden',
                "color": "var(--ood-text-secondary)",
                'white-space': 'nowrap',
                'font-weight': 'Regular',
                "font-size": '26px',
                "left":"0.5em",
                "line-height":"36px",
                "margin-top": "5px",
                "margin-right": "0.5em",
                "height": "36px"
            },

            TITLE: {
                "color": "var(--ood-text-heading)",
                display: 'block',
                "font-size": "28px",
                "line-height":"40px",
                "font-weight":"Medium",
                'margin-right': '0.25em',
                'text-align': 'left'
            },
            MORE: {
                "color": "var(--ood-text-inverse)",
                display: 'block',
                "background-color": "var(--ood-primary)",
                'margin-top': '0.5em',
                "text-align": "center",
                'font-size': '0.75em'
            },
            FLAG: {
                "color": "var(--ood-text-heading)",
                'right': "0.5em",
                'top': "0.2em",
                'text-align': "left",

                "font-size": '26px',
                "line-height":"36px",
                "opacity": 1,
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
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
                caption: ood.getResText("DataModel.theme") || "主题",
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                caption: ood.getResText("DataModel.responsive") || "响应式",
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            flagText: {
                ini: null,
                caption: ood.getResText("DataModel.markText") || "标志文本"
            },
            flagClass: {
                ini: null,
                caption: ood.getResText("DataModel.markStyleClass") || "标志样式类"
            },
            flagStyle: {
                ini: null,
                caption: ood.getResText("DataModel.markStyle") || "标志样式"
            },

            iconColors: {
                ini: null,
                caption: ood.getResText("DataModel.iconColor") || "图标颜色"
            },
            itemColors: {
                ini: null,
                caption: ood.getResText("DataModel.itemColor") || "项目颜色"
            },
            fontColors: {
                ini: null,
                caption: ood.getResText("DataModel.fontColor") || "字体颜色"
            },
            moreColors: {
                ini: null,
                caption: ood.getResText("DataModel.moreColors") || "更多颜色"
            },

            autoFontColor: {
                ini: false,
                caption: ood.getResText("DataModel.autoFontColor") || "自动字体颜色",
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                ini: false,
                caption: ood.getResText("DataModel.autoIconColor") || "自动图标颜色",
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemColor: {
                ini: false,
                caption: ood.getResText("DataModel.autoItemColor") || "自动项目颜色",
                action: function () {
                    this.boxing().refresh();
                }
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

            iconFontSize: {
                ini: '1.5em',
                caption: ood.getResText("DataModel.iconFontSize") || "图标字体大小",
                action: function (v) {
                    this.getSubNode('FLAG', true).css('font-size', v);
                }
            },

            itemMargin: {
                ini: 0,
                caption: ood.getResText("DataModel.itemSpacing") || "项目间距",
                action: function (v) {
                    this.getSubNode('ITEM', true).css('margin', v || 0);
                }
            },
            itemPadding: {
                ini: 0,
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
