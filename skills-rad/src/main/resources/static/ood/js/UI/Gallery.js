ood.Class("ood.UI.Gallery", "ood.UI.List", {
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {id: 'item1', caption: '图片顷1', imageClass: 'ri ri-image-line', desc: '这是第一张图片'},
                {id: 'item2', caption: '图片顷2', imageClass: 'ri ri-image-2-line', desc: '这是第二张图片'},
                {id: 'item3', caption: '图片顷3', imageClass: 'ri ri-image-add-line', desc: '这是第三张图片'},
                {id: 'item4', caption: '图片顷4', imageClass: 'ri ri-gallery-line', desc: '这是第四张图片'}
            ],
            value: 'item1',
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
            this.get(0).box._prepareItem(this.get(0), item);
            this.get(0).boxing().refresh();
        },

        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                var p = profile.properties;
                p.theme = theme;

                var root = profile.getRoot();
                // 移除所有主题类
                root.removeClass('gallery-light gallery-dark gallery-highcontrast');
                // 添加当前主题类
                root.addClass('gallery-' + theme);

                // 更新data-theme属性以支持CSS变量
                root.attr('data-theme', theme);

                // 保存主题设置
                localStorage.setItem('gallery-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('gallery-theme') || 'light';
        },

        GalleryTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('gallery-theme');
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
                    items = profile.getSubNode('ITEM', true),
                    captions = profile.getSubNode('CAPTION', true),
                    comments = profile.getSubNode('COMMENT', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('gallery-mobile');
                } else {
                    root.removeClass('gallery-mobile');
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('gallery-tiny');

                    // 调整列数（如果使用自动列数）
                    if (prop.autoColumns) {
                        var newCols = Math.max(2, Math.floor(width / 120)); // 最小2列
                        if (newCols !== prop.columns) {
                            profile.boxing().updateItem('columns', newCols);
                        }
                    }
                } else {
                    root.removeClass('gallery-tiny');
                }
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
                    'aria-label': properties.caption || '图片库'
                });

                // 为每个图片项目添加ARIA属性
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

        t.$submap = {
            items: {
                ITEM: {
                    tabindex: '{_tabindex}',
                    className: 'ood-uitembg ood-uiborder-radius ood-showfocus {itemClass} {disabled} {readonly}',
                    style: 'padding:{itemPadding};margin:{itemMargin};{_itemSize};{itemStyle}{_itemColor}',
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
                                className: 'oodfont {_imageClass}',
                                style: "{_fontSize};{_iconColor};{_icon};{_position};}",
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
                position: 'relative',
                cursor: 'pointer',
                'vertical-align': 'top',
                margin: 'var(--ood-spacing-xs)',
                'background-color': 'var(--ood-bg-card)',
                'border-radius': 'var(--ood-radius-md)',
                'box-shadow': 'var(--ood-shadow-sm)',
                transition: 'all 0.2s ease'
            },
            'ITEM:hover': {
                'box-shadow': 'var(--ood-shadow-md)',
                'transform': 'translateY(-2px)'
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
                "font-size": "var(--ood-font-size-lg)",
                "margin-top": "var(--ood-spacing-md)",
                "height": "auto",
                "color": "var(--ood-text-heading)"
            },
            CONTENT: {
                'text-align': 'center',
                'white-space': 'nowrap',
                'background-repeat': 'no-repeat',
                'font-size': 'var(--ood-font-size-base)',
                'color': 'var(--ood-text-body)'
            },
            COMMENT: {
                display: 'block',
                margin: 'var(--ood-spacing-xs)',
                'text-align': 'center',
                'font-size': 'var(--ood-font-size-sm)',
                'color': 'var(--ood-text-muted)'
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
                // 添加PC版对应的键盘事件支持
                onKeyDown: function (profile, e, src) {
                    var keyCode = e.keyCode || e.which;
                    var items = profile.getSubNode('ITEM', true);
                    var currentFocus = document.activeElement;
                    var currentIndex = -1;

                    // 找到当前焦点项的索引
                    items.each(function (item, index) {
                        if (item === currentFocus) {
                            currentIndex = index;
                            return false;
                        }
                    });

                    // 处理方向键导航
                    switch (keyCode) {
                        case 37: // 左箭头
                            if (currentIndex > 0) {
                                items.get(currentIndex - 1).focus();
                            }
                            e.preventDefault();
                            break;
                        case 39: // 右箭头
                            if (currentIndex < items.length - 1) {
                                items.get(currentIndex + 1).focus();
                            }
                            e.preventDefault();
                            break;
                        case 13: // 回车键
                        case 32: // 空格键
                            if (currentFocus) {
                                // 触发点击事件
                                var clickEvent = new MouseEvent('click', {
                                    view: window,
                                    bubbles: true,
                                    cancelable: true
                                });
                                currentFocus.dispatchEvent(clickEvent);
                            }
                            e.preventDefault();
                            break;
                    }
                },
                // 添加PC版鼠标事件支持
                onMouseOver: function (profile, e, src) {
                    // 为容器添加悬停状态
                    var container = ood.use(src);
                    container.addClass('ood-items-hover');
                },
                onMouseOut: function (profile, e, src) {
                    // 移除容器悬停状态
                    var container = ood.use(src);
                    container.removeClass('ood-items-hover');
                }
            },

            ITEM: {
                // 添加PC版对应的鼠标事件支持
                onMouseOver: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var itemNode = ood.use(src);

                    // 添加悬停效果
                    itemNode.addClass('ood-uitem-hover');

                    // 触发自定义悬停事件
                    if (profile.onItemHover) {
                        profile.boxing().onItemHover(profile, item, e, src);
                    }
                },

                onMouseOut: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var itemNode = ood.use(src);

                    // 移除悬停效果
                    itemNode.removeClass('ood-uitem-hover');

                    // 触发自定义离开事件
                    if (profile.onItemLeave) {
                        profile.boxing().onItemLeave(profile, item, e, src);
                    }
                },

                swipeleft: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swipeleft) {
                        box.swipeleft(profile, item, e, src);
                        return false;
                    }
                },

                swiperight: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swiperight) {
                        box.swiperight(profile, item, e, src);
                        return false;
                    }
                },
                swipedown: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swipedown) {
                        box.swipedown(profile, item, e, src);
                        return false;
                    }
                },
                swipeup: function (profile, e, src) {
                    var item = profile.getItemByDom(src),
                        box = profile.boxing();

                    if (profile.swipeup) {
                        box.swipeup(profile, item, e, src);
                        return false;
                    }
                },

                // 添加PC版对应的键盘事件支持
                onKeyDown: function (profile, e, src) {
                    var keyCode = e.keyCode || e.which;
                    var item = profile.getItemByDom(src);

                    // 处理回车键和空格键触发点击
                    if (keyCode === 13 || keyCode === 32) {
                        // 触发点击事件
                        var clickEvent = new MouseEvent('click', {
                            view: window,
                            bubbles: true,
                            cancelable: true
                        });
                        src.dispatchEvent(clickEvent);
                        e.preventDefault();
                    }

                    // 处理方向键导航
                    switch (keyCode) {
                        case 37: // 左箭头
                        case 38: // 上箭头
                            // 导航到前一个项目
                            var items = profile.getSubNode('ITEM', true);
                            var currentIndex = -1;
                            items.each(function (item, index) {
                                if (item === src) {
                                    currentIndex = index;
                                    return false;
                                }
                            });

                            if (currentIndex > 0) {
                                items.get(currentIndex - 1).focus();
                            }
                            e.preventDefault();
                            break;

                        case 39: // 右箭头
                        case 40: // 下箭头
                            // 导航到后一个项目
                            var items = profile.getSubNode('ITEM', true);
                            var currentIndex = -1;
                            items.each(function (item, index) {
                                if (item === src) {
                                    currentIndex = index;
                                    return false;
                                }
                            });

                            if (currentIndex < items.length - 1) {
                                items.get(currentIndex + 1).focus();
                            }
                            e.preventDefault();
                            break;
                    }
                },

                // 添加PC版对应的焦点事件支持
                onFocus: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var itemNode = ood.use(src);

                    // 添加焦点样式
                    itemNode.addClass('ood-uitem-focus');

                    // 触发自定义焦点事件
                    if (profile.onItemFocus) {
                        profile.boxing().onItemFocus(profile, item, e, src);
                    }
                },

                onBlur: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var itemNode = ood.use(src);

                    // 移除焦点样式
                    itemNode.removeClass('ood-uitem-focus');

                    // 触发自定义失焦事件
                    if (profile.onItemBlur) {
                        profile.boxing().onItemBlur(profile, item, e, src);
                    }
                },

                // 添加PC版双击事件支持
                onDblclick: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var box = profile.boxing();

                    // 触发自定义双击事件
                    if (profile.onItemDblClick) {
                        box.onItemDblClick(profile, item, e, src);
                    }
                },

                // 添加PC版右键菜单事件支持
                onContextmenu: function (profile, e, src) {
                    var item = profile.getItemByDom(src);
                    var box = profile.boxing();

                    // 触发自定义右键菜单事件
                    if (profile.onItemContextmenu) {
                        return box.onItemContextmenu(profile, item, e, src) !== false;
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
                },
                // 添加PC版鼠标事件支持
                onMouseOver: function (profile, e, src) {
                    var flag = ood.use(src);
                    flag.addClass('ood-flag-hover');
                },
                onMouseOut: function (profile, e, src) {
                    var flag = ood.use(src);
                    flag.removeClass('ood-flag-hover');
                }
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
                caption: ood.getResText("DataModel.theme") || "主题",
                action: function (value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                caption: ood.getResText("DataModel.responsive") || "响应式",
                action: function (value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            autoColumns: {
                ini: false,
                caption: ood.getResText("DataModel.autoColumns") || "自动列数",
                action: function (value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
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
                caption: ood.getResText("DataModel.iotStatus") || "物联网状态"
            },
            tagCmdsAlign: {
                ini: null,
                caption: ood.getResText("DataModel.tagCommandsAlign") || "标签命令对齐方式"
            },
            flagText: {
                ini: null,
                caption: ood.getResText("DataModel.flagText") || "标记文本"
            },
            flagClass: {
                ini: null,
                caption: ood.getResText("DataModel.flagClass") || "标记样式类"
            },
            flagStyle: {
                ini: null,
                caption: ood.getResText("DataModel.markStyle") || "标记样式"
            },

            iconColors: {
                ini: null,
                caption: ood.getResText("DataModel.iconColors") || "图标颜色列表"
            },
            itemColors: {
                ini: null,
                caption: ood.getResText("DataModel.itemColors") || "项目颜色列表"
            },
            fontColors: {
                ini: null,
                caption: ood.getResText("DataModel.fontColors") || "字体颜色列表"
            },

            autoFontColor: {
                ini: false,
                caption: ood.getResText("DataModel.autoFontColor") || "自动填充颜色",
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                ini: true,
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

            backgroundColor: {
                ini: '',
                type: "color",
                caption: ood.getResText("DataModel.bgColor") || "背景颜色",
                action: function (value) {
                    this.getSubNode('ITEMS', true).css('background-color', value);
                }
            },

            itemMargin: {
                ini: 6,
                caption: ood.getResText("DataModel.itemSpacing") || "项目间距",
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
                caption: ood.getResText("DataModel.imgWidth") || "图片宽度",
                action: function (v) {
                    this.getSubNode('IMAGE', true).width(v || '');
                }
            },
            imgHeight: {
                ini: 16,
                caption: ood.getResText("DataModel.imgHeight") || "图片高度",
                action: function (v) {
                    this.getSubNode('IMAGE', true).height(v || '');
                }
            },
            width: {
                $spaceunit: 1,
                //ini: '16rem',
                caption: ood.getResText("DataModel.width") || "宽度"
            },
            height: {
                $spaceunit: 1,
             //   ini: '16rem',
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

            ood.arr.each(ood.toArr('itemWidth,backgroundColor,bgimg,iconColor,fontColor,itemColor,iotStatus,position,itemHeight,imgWidth,imgHeight,itemPadding,itemMargin,iconFontSize,autoItemSize,autoImgSize'), function (i) {
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

            if (item.fontColor) item._fontColor = "color:" + item.fontColor;
            if (item.iconColor) item._iconColor = "color:" + item.iconColor;
            if (item.itemColor) item._iconColor = "color:" + item.itemColor;
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
            var profile = this,
                p = profile.properties;
            if (p.backgroundColor) {
                if (!p.autoItemColor) {
                    profile.getSubNode('ITEMS', true).css('background-color', p.backgroundColor);
                }
            }

            this.boxing()._afterInsertItems(this);

            // 现代化功能初始化
            var self = this;
            ood.asyRun(function () {
                if (self.boxing() && self.boxing().GalleryTrigger()) {
                    self.boxing().GalleryTrigger();
                }
            });
        }


    }
});
