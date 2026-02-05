ood.Class("ood.UI.InfoBlock",   "ood.UI.List", {
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
            var root = profile.getRoot();

            // 移除所有主题类
            root.removeClass('infoblock-dark infoblock-highcontrast');
            
            // 应用主题类
            if (theme === 'dark') {
                root.addClass('infoblock-dark');
            } else if (theme === 'highcontrast') {
                root.addClass('infoblock-highcontrast');
            }
            
            // 确保基础样式类存在
            root.addClass('ood-infoblock');
            
            // 为子元素添加基础类
            profile.getSubNode('ITEM', true).addClass('ood-infoblock-item');
            profile.getSubNode('CAPTION', true).addClass('ood-infoblock-caption');
            profile.getSubNode('COMMENT', true).addClass('ood-infoblock-comment');
            profile.getSubNode('IMAGE', true).addClass('ood-infoblock-image');
            
            // 保存主题设置
            localStorage.setItem('infoblock-theme', theme);
            
            // 触发主题变化事件
            if (profile.onThemeChange) {
                profile.onThemeChange(theme);
            }
        });
    },
    
    // 获取当前主题
    getTheme: function() {
        var profile = this.get(0);
        return profile.properties.theme || localStorage.getItem('infoblock-theme') || 'light';
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
                captions = profile.getSubNode('CAPTION', true),
                comments = profile.getSubNode('COMMENT', true),
                prop = profile.properties;

            // 对于小屏幕，调整布局
            if (width < 768) {
                root.addClass('infoblock-mobile');
                
                // 移动端调整
                items.css({
                    'padding': '0.5em',
                    'margin': '0.25em'
                });
                captions.css({
                    'font-size': '16px',
                    'margin-top': '10px',
                    'height': '30px'
                });
                comments.css({
                    'font-size': '0.9em'
                });
            } else {
                root.removeClass('infoblock-mobile');
                
                // 恢复桌面样式
                items.css({
                    'padding': '',
                    'margin': ''
                });
                captions.css({
                    'font-size': '',
                    'margin-top': '',
                    'height': ''
                });
                comments.css({
                    'font-size': ''
                });
            }

            // 超小屏幕特殊处理
            if (width < 480) {
                root.addClass('infoblock-tiny');
                
                // 优化小屏幕显示
                captions.css({
                    'font-size': '14px',
                    'margin-top': '8px',
                    'height': '25px'
                });
                comments.css({
                    'font-size': '0.8em'
                });
                
                // 调整列数（如果使用自动列数）
                if (prop.autoColumns) {
                    var newCols = Math.max(2, Math.floor(width / 120)); // 最小2列
                    if (newCols !== prop.columns) {
                        profile.boxing().updateItem('columns', newCols);
                    }
                }
            } else {
                root.removeClass('infoblock-tiny');
            }
        });
    },
    
    // 增强可访问性支持
    enhanceAccessibility: function() {
        return this.each(function(profile) {
            var root = profile.getRoot(),
                items = profile.getSubNode('ITEM', true),
                images = profile.getSubNode('IMAGE', true),
                icons = profile.getSubNode('ICON', true),
                properties = profile.properties;

            // 为容器添加ARIA属性
            root.attr({
                'role': 'grid',
                'aria-label': properties.caption || '信息块列表'
            });
            
            // 为每个信息项添加ARIA属性
            items.each(function(item, index) {
                var itemNode = ood(item);
                var itemData = profile.getItemByDom(item);
                
                // 基础ARIA属性
                itemNode.attr({
                    'role': 'gridcell',
                    'tabindex': itemData.disabled ? '-1' : '0',
                    'aria-label': itemData.caption || itemData.comment || '信息项',
                    'aria-disabled': itemData.disabled ? 'true' : 'false'
                });
                
                // 如果有描述信息
                if (itemData.comment) {
                    itemNode.attr('aria-describedby', profile.serialId + '_comment_' + index);
                    var commentNode = itemNode.find('.ood-ui-ellipsis').filter(function() {
                        return ood(this).text() === itemData.comment;
                    });
                    if (commentNode.length) {
                        commentNode.attr('id', profile.serialId + '_comment_' + index);
                    }
                }
            });
            
            // 为图片添加ARIA属性
            images.each(function(img) {
                var imgNode = ood(img);
                var itemData = profile.getItemByDom(img);
                imgNode.attr({
                    'alt': itemData.caption || itemData.comment || '图片',
                    'role': 'img'
                });
            });
            
            // 为图标添加ARIA属性
            icons.each(function(icon) {
                ood(icon).attr({
                    'aria-hidden': 'true' // 隐藏装饰性图标
                });
            });
        });
    },
    
    InfoBlockTrigger: function() {
        var profile = this.get(0);
        var prop = profile.properties;

        // 初始化现代化功能
        // 初始化主题
        if (prop.theme) {
            this.setTheme(prop.theme);
        } else {
            // 从本地存储恢复主题
            var savedTheme = localStorage.getItem('infoblock-theme');
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
    t.$submap = {
        items: {
            ITEM: {
                tabindex: '{_tabindex}',
                className: 'ood-uitembg ood-uiborder-radius ood-showfocus {itemClass} {disabled} {readonly}',
                style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize};{itemStyle})',
                ITEMFRAME: {
                    style: '{_inneritemSize};{_bgimg}; {_itemposition}',
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
                        style: '{capDisplay}',
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
    ood.UI.IconList = ood.UI.InfoBlock;
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
                "font-size": "20px",
                "margin-top": "15px",
                "height": "35px"
        },
        CONTENT: {
            'text-align': 'center',
                'white-space': 'nowrap',
                'background-repeat': 'no-repeat',
                //  'background-position': 'center center',
                'font-size': '1em'
        },
        COMMENT: {
            display: 'block',
                margin: '.25em',
                'text-align': 'center',
                'font-size': '1em'
        },
        FLAG: {
            'width': "2em",
                "height": "2em",
                'right': "5em",
                'top': "1em",
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
        // 现代化属性
        theme: {
            ini: 'light',
            listbox: ['light', 'dark', 'highcontrast'],
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
        responsiveBreakpoint: {
            ini: 'md',
            listbox: ['sm', 'md', 'lg', 'xl'],
            caption: ood.getResText("DataModel.responsiveBreakpoint") || "响应式断点",
            action: function(value) {
                this.boxing().adjustLayout();
            }
        },
        ariaLabel: {
            ini: '',
            caption: ood.getResText("DataModel.ariaLabel") || "ARIA标签",
            action: function(value) {
                this.getRoot().attr('aria-label', value);
            }
        },
        tabIndex: {
            ini: '0',
            caption: ood.getResText("DataModel.tabIndex") || "Tab索引",
            action: function(value) {
                this.getSubNode('ITEM', true).attr('tabindex', value);
            }
        },
        
        tagCmds: {
            ini: null,
            caption: ood.getResText("DataModel.tagCommands") || "标签命令"
        },
            expression: {
            ini: '',
            caption: ood.getResText("DataModel.expression") || "表达式",
                action: function () {
            }
        },
        bgimg: {
            ini: null,
            caption: ood.getResText("DataModel.backgroundImage") || "背景图片"
        },
            iotStatus: {
            ini: null,
            caption: ood.getResText("DataModel.iotStatus") || "IoT状态"
        },
            tagCmdsAlign: {
            ini: null,
            caption: ood.getResText("DataModel.tagCommandsAlign") || "标签命令对齐"
        },
            flagText: {
            ini: null,
            caption: ood.getResText("DataModel.flagText") || "标志文本"
        },
            flagClass: {
            ini: null,
            caption: ood.getResText("DataModel.flagClass") || "标志样式类"
        },
            flagStyle: {
            ini: null,
            caption: ood.getResText("DataModel.flagStyle") || "标志样式"
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
            caption: ood.getResText("DataModel.itemWidth") || "项目宽度",
                ini: 32,
                action: function (v) {
                this.getSubNode('ITEMFRAME', true).width(v || '');
            }
        },
        itemHeight: {
            $spaceunit: 1,
            caption: ood.getResText("DataModel.itemHeight") || "项目高度",
                ini: 32,
                action: function (v) {
                this.getSubNode('ITEMFRAME', true).height(v || '');
            }
        },
        imgWidth: {
            caption: ood.getResText("DataModel.imgWidth") || "图片宽度",
            ini: 16,
                action: function (v) {
                this.getSubNode('IMAGE', true).width(v || '');
            }
        },
        imgHeight: {
            caption: ood.getResText("DataModel.imgHeight") || "图片高度",
            ini: 16,
                action: function (v) {
                this.getSubNode('IMAGE', true).height(v || '');
            }
        },
        width: {
            $spaceunit: 1,
            caption: ood.getResText("DataModel.width") || "宽度",
                ini: '16rem'
        },
        height: {
            $spaceunit: 1,
            caption: ood.getResText("DataModel.height") || "高度",
                ini: '16rem'
        },
        columns: {
            caption: ood.getResText("DataModel.columns") || "列数",
            ini: 0,
                action: function () {
                this.boxing().refresh();
            }
        },

        rows: {
            caption: ood.getResText("DataModel.rows") || "行数",
            ini: 0,
                action: function () {
                this.boxing().refresh();
            }
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
        var d = arguments.callee.upper.call(this, profile), p = profile.properties,ns=this;
        if (p.cols) d._itemscls1 = profile.getClass('ITEMS', '-nowrap');
        ood.arr.each(d.items, function (item) {
            var index=item.index;
            if (!index) {
                index = ood.arr.indexOf(d.items, item);
                while (index && index > (p.fontColors.length-1)) {
                    index = index - p.fontColors.length;
                }
                item.index = index;
            }
            profile.boxing()._autoColor(item, p);
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
        ood.arr.each(ood.toArr('itemWidth,bgimg,iotStatus,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,autoItemSize,autoImgSize'), function (i) {
            item[i] = ood.isSet(item[i]) ? item[i] : p[i];
        });

        if (p.flagClass && !item.flagClass) {
            item.flagClass = p.flagClass;
        }
        if (p.flagStyle && !item.flagStyle) {
            item.flagStyle = p.flagStyle;
        }
        if (p.flagText && !item.flagText) {
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
        // 初始化现代化功能
        this.boxing().InfoBlockTrigger();
    }
}
});
