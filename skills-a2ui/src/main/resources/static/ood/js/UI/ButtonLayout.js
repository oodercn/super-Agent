ood.Class("ood.UI.ButtonLayout", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            "value": "mywork",
            "showDirtyMark": false,
            "items": [
                {
                    "caption": "",
                    "comment": ood.getRes('RAD.widgets.home') || "首页",
                    "id": "index",
                    "imageClass": "ri-link"
                },
                {
                    "caption": "",
                    "comment": ood.getRes('RAD.widgets.todo') || "待办",
                    "id": "waitedwork",
                    "imageClass": "ri-calendar-line"
                },
                {
                    "caption": "",
                    "comment": ood.getRes('RAD.widgets.draft') || "起草",
                    "id": "startWork",
                    "imageClass": "ri-flashlight-line"
                },
                {
                    "caption": "",
                    "comment": "我的",
                    "id": "mywork",
                    "imageClass": "ri-user-line"
                },
                {
                    "caption": "",
                    "comment": "消息",
                    "id": "msg",
                    "imageClass": "ri-message-line"
                }
            ],
            "dock": "fill",
            "borderType": "none",
            "iconFontSize": "2em",
            "columns": 5
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
            this.updateItem(item.id, item);
            this.get(0).boxing().refresh();
        },

        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();
                root.attr('data-theme', theme);

                // 保存主题设置
                localStorage.setItem('buttonlayout-theme', theme);
            });
        }
        ,

        ButtonLayoutTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties,
                boxing = this;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('buttonlayout-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                boxing.adjustLayout();
            }

            // 初始化可访问性
            boxing.enhanceAccessibility();
        },


        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('buttonlayout-theme') || 'light';
        },

        // 切换暗黑模式
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },

        // 响应式布局调整
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // 清除所有响应式类
                root.removeClass('buttonlayout-mobile buttonlayout-tiny buttonlayout-small');

                // 根据屏幕宽度应用响应式类
                if (width < 480) {
                    root.addClass('buttonlayout-tiny');
                } else if (width < 768) {
                    root.addClass('buttonlayout-mobile');
                } else if (width < 1024) {
                    root.addClass('buttonlayout-small');
                }

                // 调整列数（如果使用自动列数）
                if (prop.autoColumns) {
                    var newCols = Math.max(2, Math.floor(width / 80)); // 最小2列
                    if (newCols !== prop.columns) {
                        profile.boxing().updateItem('columns', newCols);
                    }
                }

                // 触发resize事件让CSS生效
                //ood(window).fireEvent('resize');
            });
        },

        // 增强可访问性支持
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'grid',
                    'aria-label': properties.caption || ood.getRes('UI.buttonlayout.label') || '按钮布局'
                });

                // 为每个按钮添加ARIA属性
                items.each(function (item, index) {
                    var itemNode = ood(item);
                    var itemId = itemNode.id();
                    if (itemId) {
                        var itemData = profile.getItemByDom(item);
                        if (itemData) {
                            itemNode.attr({
                                'role': 'gridcell',
                                'aria-label': itemData.caption || itemData.comment || itemData.id,
                                'tabindex': index === 0 ? '0' : '-1', // 仅第一个可焦点
                                'aria-describedby': itemData.comment ? itemId + '_comment' : null
                            });

                            // 为评论添加ID
                            if (itemData.comment) {
                                var commentNode = profile.getSubNodeByItemId('COMMENT', itemData.id);
                                if (commentNode && !commentNode.isEmpty()) {
                                    commentNode.attr('id', itemId + '_comment');
                                }
                            }
                        }
                    }
                });

                // 为图标添加ARIA属性
                var icons = profile.getSubNode('ICON', true);
                icons.each(function (icon) {
                    var iconNode = ood(icon);
                    iconNode.attr({
                        'aria-hidden': 'true' // 隐藏装饰性图标
                    });
                });
            });
        }
    },
    Initialize: function () {
        //modify default template fro shell
        var t = this.getTemplate();
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if (!t) {
            t = {};
        }
        t.$submap = {
            items: {
                ITEM: {
                    tabindex: '{_tabindex}',
                    className: '{itemClass} {disabled} {readonly}',
                    style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize};{itemStyle} {_itemColor}',
                    ITEMFRAME: {
                        style: '{_inneritemSize};{_bgimg}; {_itemposition}',
                        COMMENT: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            text: '{comment}',
                            style: '{commentDisplay};{_fontColor}',
                            $order: 2
                        },
                        CONTENT: {
                            tagName: 'div',
                            $order: 1,
                            className: '{contentClass}',
                            style: '{_loadbg}',
                            ICON: {
                                className: 'oodfont {_imageClass} ood-showfocus',
                                style: "{_fontSize};{_icon};{_position}{_iconColor}",
                                text: '{iconFontCode}'
                            },
                            IMAGE: {
                                tagName: 'img',
                                src: ood.ini.img_bg,
                                title: '{image}',
                                style: '{_innerimgSize};{imgStyle}'
                            }
                        },
                        CAPTION: {
                            tagName: 'div',
                            className: 'ood-ui-ellipsis',
                            style: '{capDisplay} {_iconColor}',
                            text: '{caption}',
                            $order: 0
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
        ood.UI.IconList = ood.UI.ButtonLayout;
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
                display: 'inline-flex',
                position: 'relative',
                cursor: 'pointer',
                'flex-direction': 'column',
                'align-items': 'center',
                'justify-content': 'center',
                margin: '0.25em',
                padding: '0.5em',
                'border-radius': 'var(--ood-border-radius)',
                'background-color': 'var(--ood-primary-color)',
                'color': 'var(--ood-text-on-primary)',
                'transition': 'all 0.2s ease',
                'box-shadow': '0 1px 2px rgba(0,0,0,0.1)',
                'border': '1px solid transparent'
            },
            'ITEM:hover': {
                'box-shadow': '0 2px 6px rgba(0,0,0,0.15)',
                'transform': 'translateY(-1px)'
            },
            'ITEM:focus': {
                'box-shadow': '0 0 0 2px rgba(0, 120, 212, 0.3)',
                'outline': 'none'
            },
            ITEMFRAME: {
                display: ood.browser.ie67 ? ood.$inlineBlock : 'block',
                zoom: ood.browser.ie67 ? 1 : null,
                position: 'relative',
                overflow: 'hidden',
                border: 0,
                padding: 0,
                margin: 0,
                '-moz-box-flex': '1',
                // 现代化样式
                'border-radius': '4px'
            },
            IBWRAP: {},
            IMAGE: {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie6 ? 1 : null,
                visibility: 'hidden',
                'vertical-align': 'middle',
                // 现代化样式
                'border-radius': '3px'
            },
            CAPTION: {
                'text-align': 'center',
                'font-weight': '500',
                "font-size": "1em",
                "margin": "0.5em 0 0 0",
                "line-height": "1.2",
                'white-space': 'normal',
                'max-width': '100%',
                'overflow': 'hidden',
                'text-overflow': 'ellipsis',
                'display': '-webkit-box',
                '-webkit-line-clamp': '2',
                '-webkit-box-orient': 'vertical'
            },
            CONTENT: {
                'text-align': 'center',
                'white-space': 'nowrap',
                'background-repeat': 'no-repeat',
                'font-size': '1em',
                // 现代化样式
                transition: 'background-color 0.3s ease'
            },
            COMMENT: {
                display: 'block',
                margin: '0.25em 0 0 0',
                'text-align': 'center',
                'font-size': '0.75em',
                'color': 'var(--ood-text-secondary)',
                'line-height': '1.3',
                'max-width': '100%',
                'overflow': 'hidden',
                'text-overflow': 'ellipsis'
            },
            FLAG: {
                'right': "30%",
                'top': "0.125em",
                'font-size': "8px",
                'color': "var(--ood-warning)",
                'position': 'absolute',
                'z-index': 10,
                // 现代化样式
                'border-radius': '50%',
                'box-shadow': '0 1px 2px rgba(0,0,0,0.2)'
            },

            // 暗黑模式样式
            'buttonlayout-dark ITEM': {
                'background-color': '#2d2d30',
                'border-color': '#3c3c3c',
                'color': '#cccccc',
                'box-shadow': '0 1px 3px rgba(0,0,0,0.3)'
            },
            'buttonlayout-dark ITEM:hover': {
                'background-color': '#3c3c3c',
                'box-shadow': '0 2px 6px rgba(0,0,0,0.4)'
            },
            'buttonlayout-dark CAPTION': {
                'color': '#d4d4d4'
            },
            'buttonlayout-dark COMMENT': {
                'color': '#cccccc'
            },
            'buttonlayout-dark CONTENT': {
                'background-color': 'transparent'
            },

            // 移动端样式
            'buttonlayout-mobile ITEM': {
                'padding': '0.6em',
                'margin': '0.3em'
            },
            'buttonlayout-mobile CAPTION': {
                'font-size': '16px',
                'margin-top': '10px',
                'height': '30px'
            },
            'buttonlayout-mobile COMMENT': {
                'font-size': '0.7em'
            },
            'buttonlayout-mobile ICON': {
                'font-size': '1.2em'
            },

            // 小屏幕样式
            'buttonlayout-tiny CAPTION': {
                'font-size': '14px',
                'margin-top': '8px',
                'height': '25px'
            },
            'buttonlayout-tiny COMMENT': {
                'font-size': '0.6em'
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
                        box.swiperight(profile, item, e, src);
                        return false;
                    }
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
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
                action: function (value) {
                    this.boxing().setTheme(value);
                },
                caption: ood.getResText("DataModel.theme") || "主题"
            },
            responsive: {
                ini: true,
                action: function (value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                },
                caption: ood.getResText("DataModel.responsive") || "响应式"
            },
            autoColumns: {
                ini: false,
                action: function (value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                },
                caption: ood.getResText("DataModel.autoColumns") || "自动列数"
            },

            tagCmds: null,
            expression: {
                ini: '',
                action: function () {
                },
                caption: ood.getResText("DataModel.expression") || "表达式"
            },
            bgimg: null,
            tagCmdsAlign: null,
            flagText: null,
            flagClass: null,
            flagStyle: null,


            autoFontColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.autoFontColor") || "自动字体颜色"
            },
            autoIconColor: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.autoIconColor") || "自动图标颜色"
            },
            autoItemColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.autoItemColor") || "自动项目颜色"
            },
            iconColors: null,
            itemColors: null,
            fontColors: null,


            autoImgSize: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.autoImgSize") || "自动图片大小"
            },
            autoItemSize: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.autoItemSize") || "自动项目大小"
            },
            iconOnly: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.iconOnly") || "仅显示图标"
            },
            iconFontSize: {
                ini: '',
                action: function (v) {
                    this.getSubNode('ICON', true).css('font-size', v);
                }
            },
            itemMargin: {
                ini: 6,
                action: function (v) {
                    this.getSubNode('ITEM', true).css('margin', v || 0);
                },
                caption: ood.getResText("DataModel.itemMargin") || "项目边距"
            },
            itemPadding: {
                ini: 2,
                action: function (v) {
                    this.getSubNode('ITEM', true).css('padding', v || 0);
                },
                caption: ood.getResText("DataModel.itemPadding") || "项目内边距"
            },
            itemWidth: {
                $spaceunit: 1,
                ini: 32,
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).width(v || '');
                },
                caption: ood.getResText("DataModel.itemWidth") || "项目宽度"
            },
            itemHeight: {
                $spaceunit: 1,
                ini: 32,
                action: function (v) {
                    this.getSubNode('ITEMFRAME', true).height(v || '');
                },
                caption: ood.getResText("DataModel.itemHeight") || "项目高度"
            },
            imgWidth: {
                ini: 16,
                action: function (v) {
                    this.getSubNode('IMAGE', true).width(v || '');
                },
                caption: ood.getResText("DataModel.imgWidth") || "图片宽度"
            },
            imgHeight: {
                ini: 16,
                action: function (v) {
                    this.getSubNode('IMAGE', true).height(v || '');
                },
                caption: ood.getResText("DataModel.imgHeight") || "图片高度"
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
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.columns") || "列数"
            },

            rows: {
                ini: 0,
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.rows") || "行数"
            }
        },
        EventHandlers: {
            onCmd: null,

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
                auto2 = item.autoImgSize || p.autoImgSize,
                t;
            profile.boxing()._autoColor(item, index, p);
            ood.arr.each(ood.toArr('itemWidth,bgimg,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,autoItemSize,autoImgSize'), function (i) {
                item[i] = ood.isSet(item[i]) ? item[i] : p[i];
            });

            if (p.flagClass && !item.flagClass) {
                item.flagClass = p.flagClass;
            }
            if (!item.flagStyle) {
                item.flagStyle = p.flagStyle;
            }
            if (!item.flagText) {
                item.flagText = p.flagText;
            }

            item.itemWidth = (!auto1 && (t = item.itemWidth)) ? profile.$forceu(t) : '';
            item.itemHeight = (!auto1 && (t = item.itemHeight)) ? profile.$forceu(t) : '';
            item.itemMargin = (t = item.itemMargin) ? profile.$forceu(t) : 0;
            item.itemPadding = (t = item.itemPadding) ? profile.$forceu(t) : 0;
            item.imgWidth = (!auto2 && (t = item.imgWidth)) ? profile.$forceu(t) : '';
            item.imgHeight = (!auto2 && (t = item.imgHeight)) ? profile.$forceu(t) : '';
            item._tabindex = p.tabindex;

            if (item.icon) item._icon = "background-image: url(" + item.icon + ")";
            if (item.position) item._position = "background-position:" + item.position;

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

            // 现代化功能初始化
            var self = this;
            ood.asyRun(function () {
                if (self.boxing() && ood.isFun(self.boxing().ButtonLayoutTrigger)) {
                    self.boxing().ButtonLayoutTrigger();
                }

            });
        }
    }
});
