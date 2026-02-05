ood.Class("ood.UI.TreeBar", ["ood.UI", "ood.absList", "ood.absValue"], {

    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {
                    id: 'node1', sub: [
                        {id: 'node12', imageClass: "ri-image-line"}
                    ]
                }, {
                    id: 'node2',
                    initFold: false
                }],
            theme: 'dark',
            responsive: true,
            initFold: true,
            animCollapse: true,
            dock: 'fill',
            group: false,
            selMode: 'single',
            noCtrlKey: true,
            singleOpen: false,
            dynDestory: false,
            optBtn: "",
            togglePlaceholder: false,
            tagCmds: [],
            tagCmdsAlign: "right",
            autoItemColor: false,
            autoIconColor: true,
            autoFontColor: false,
            position: 'absolute'
        },

        _setCtrlValue: function (value, flag) {
            return this.each(function (profile) {
                if (!profile.renderId) return;

                var box = profile.boxing(),
                    uiv = box.getUIValue(),
                    properties = profile.properties,
                    fun = function (key, o, b) {
                        profile.getSubNodeByItemId(key, o).tagClass('-checked', b);
                    },
                    selmode = properties.selMode
                ;
                if (selmode == 'single' || selmode == 'singlecheckbox') {
                    var itemId = profile.getSubIdByItemId(uiv);
                    if (uiv && itemId) {
                        fun('BAR', uiv, false);
                        if (selmode == 'singlecheckbox') {
                            fun('MARK', uiv, false);
                        }
                    }


                    itemId = profile.getSubIdByItemId(value);
                    if (itemId) {
                        fun('BAR', value, true);
                        if (selmode == 'singlecheckbox') {
                            fun('MARK', value, true);
                        }
                        //scroll
                        if (!profile._noScroll) {
                            var o = profile.getSubNode('ITEM', itemId);
                            if (o) {
                                var items = profile.getSubNode('BOX'),
                                    offset = o.offset(null, items),
                                    top = offset ? offset.top : 0,
                                    height = o.offsetHeight(),
                                    sh = items.scrollHeight(),
                                    st = items.scrollTop(),
                                    hh = items.height();
                                if (sh > hh)
                                    if (top < st || (top + height) > (st + hh))
                                        items.scrollTop(top);
                            }
                        }
                    }
                } else if (selmode == 'multi' || selmode == 'multibycheckbox') {
                    uiv = uiv ? uiv.split(properties.valueSeparator) : [];
                    value = value ? value.split(properties.valueSeparator) : [];
                    if (flag) {
                        ood.arr.each(value, function (o) {
                            fun('BAR', o);
                            if (selmode == 'multibycheckbox') {
                                fun('MARK', o);
                            }

                        });
                    } else {
                        //check all
                        ood.arr.each(uiv, function (o) {
                            fun('BAR', o, false);
                            if (selmode == 'multibycheckbox') {
                                fun('MARK', o, false);
                            }
                        });
                        ood.arr.each(value, function (o) {
                            fun('BAR', o);
                            if (selmode == 'multibycheckbox') {
                                fun('MARK', o);
                            }
                        });
                    }
                }
            });
        },
        insertItems: function (arr, pid/*true: the current item*/, base/*true: the current item*/, before, toggle) {
            var node, data,
                b = this._afterInsertItems;

            return this.each(function (profile) {
                // prepare properties format
                var tar, r, k, newsub,
                    prop = profile.properties;

                data = profile.box._adjustItems(arr);

                // current
                if (pid === true) {
                    v = prop.$UIvalue || prop.value;
                    if (v) v = (v + '').split(prop.valueSeparator);
                    k = profile.getItemByItemId(v[0]);
                    pid = k ? k.id : null;
                }

                if (pid) {
                    k = profile.getItemByItemId(pid);
                    tar = ood.isArr(k.sub) ? k.sub : (newsub = true, k.sub = []);
                } else {
                    k = prop;
                    tar = k.items || (k.items = [])
                }
                //1
                if (profile.renderId) {
                    if (base === true) {
                        v = prop.$UIvalue || prop.value;
                        if (v) v = (v + '').split(prop.valueSeparator);
                        k = profile.getItemByItemId(v[0]);
                        base = k ? k.id : null;
                    }
                    if (base) {
                        node = profile.getSubNodeByItemId('ITEM', base);
                        if (node) {
                            r = profile._buildItems('items', profile.box._prepareItems(profile, data, pid));
                            if (before)
                                node.addPrev(r);
                            else
                                node.addNext(r);
                        }
                    } else {
                        if (!pid)
                            node = profile.getSubNode('ITEMS');
                        else if (pid) {
                            if (newsub) {
                                profile.getSubNodeByItemId('TOGGLE', pid)
                                    .removeClass('ood-icon-placeholder ood-uicmd-none')
                                    .addClass('ood-uicmd-toggle');
                            }
                            if (k._inited) {
                                node = profile.getSubNodeByItemId('SUB', pid);
                            }
                        }
                        if (node) {
                            r = profile._buildItems('items', profile.box._prepareItems(profile, data, pid));
                            if (before)
                                node.prepend(r);
                            else
                                node.append(r);
                        }
                    }
                }
                //2
                //must be here
                if (!base)
                    ood.arr.insertAny(tar, data, before ? 0 : -1);
                else {
                    var index = ood.arr.subIndexOf(tar, 'id', base);
                    ood.arr.insertAny(tar, data, before ? index : (index + 1));
                }
                //3
                if (profile.renderId && toggle !== false) {
                    // try to open root subs
                    if (!pid) {
                        profile.boxing()._toggleNodes(data, true, true, true);
                    }
                    // try to open parent node
                    else if (profile.getItemByItemId(pid)._inited) {
                        if (!(('initFold' in k) ? k.initFold : profile.properties.initFold))
                            profile.boxing()._toggleNodes(data, true, true, true);
                    }
                }

                if (b && profile.renderId)
                    profile.boxing()._afterInsertItems(profile, data, pid, base, before);

                if (profile.renderId && pid) {
                    profile.box._tofold(profile, k, pid);
                }

            });
        },
        // 增强版节点切换方法
        _toggleNodes: function (items, expand, recursive, init) {
            var self = this, prf = self.get(0), pro = prf.properties;

            // 添加ARIA属性更新
            var updateAria = function (nodeId, expanded) {
                var node = prf.getSubNodeByItemId('TOGGLE', nodeId);
                if (node) node.attr('aria-expanded', expanded ? 'true' : 'false');
            };
            f = function (items, expand, recursive, init) {
                if (ood.isArr(items)) {
                    ood.arr.each(items, function (o) {
                        if (init && (ood.isBool(o.initFold) ? o.initFold : pro.initFold)) return;
                        self.toggleNode(o.id, expand, false, recursive);
                        if (recursive && o.sub && ood.isArr(o.sub) && o.sub.length)
                            f(o.sub, expand, true, init);
                    });
                }
            };
            f(items, expand, recursive, init);
            return self;
        },

        /**
         * Recursively remove all child node keys
         * @param {Array} arr - ID array
         * @param {Array} captionarr - Caption array
         * @param {Object} item - 当前项
         * @param {Object} profile - 组件配置
         * @returns {Array} 更新后的数组
         */
        _removeAllkeys: function (arr, captionarr, item, profile) {
            ood.each(item.sub, function (subitem) {
                ood.arr.removeValue(arr, subitem.id);
                ood.arr.removeValue(captionarr, subitem.caption);
                if (subitem.sub && subitem.sub.length > 0) {
                    arr = profile.boxing()._removeAllkeys(arr, captionarr, subitem, profile);
                }
            });
            return arr;
        },


        // 新增主题设置方法
        setTheme: function (theme) {
            return this.each(function (profile) {
                var root = profile.getRoot();

                // 动态加载主题CSS
                if (!document.getElementById('treebar-theme-css')) {
                    var link = document.createElement('link');
                    link.id = 'treebar-theme-css';
                    link.rel = 'stylesheet';
                    link.href = 'ood/css/treebar-themes.css';
                    document.head.appendChild(link);
                }

                // 应用主题类
                root.removeClass('treebar-dark treebar-hc');
                if (theme === 'dark') root.addClass('treebar-dark');
                else if (theme === 'high-contrast') root.addClass('treebar-hc');

                // 保存主题设置
                localStorage.setItem('treebar-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('treebar-theme') || 'light';
        },


        /**
         * Recursively add all child node keys
         * @param {Array} arr - ID数组
         * @param {Array} captionarr - 标题数组
         * @param {Object} item - 当前项
         * @param {Object} profile - 组件配置
         * @returns {Array} 更新后的数组
         */
        _addAllkeys: function (arr, captionarr, item, profile) {
            ood.each(item.sub, function (subitem) {
                arr.push(subitem.id);
                captionarr.push(subitem.caption);
                if (subitem.sub && subitem.sub.length > 0) {
                    arr = profile.boxing()._addAllkeys(arr, captionarr, subitem, profile);
                }
            });
            return arr;
        },
        /**
         * Toggle node expand/collapse state
         * @param {String} id - 节点ID
         * @param {Boolean} expand - true:expand, false:collapse, undefined:toggle current state
         * @param {Boolean} recursive - Whether to recursively operate on child nodes
         * @param {Boolean} stopanim - Whether to stop animation effects
         * @param {Function} callback - 操作完成后的回调函数
         * @returns {Object} 当前实例
         */
        toggleNode: function (id, expand, recursive, stopanim, callback) {
            var profile = this.get(0),
                ns = this,
                self = arguments.callee;

            if (id) {
                var o = profile.getItemByItemId(id);

                if (o && o.sub && (expand == true || !ood.isSet(expand) || !!expand !== !!o._checked)) {
                    profile.box._setSub(profile, o, ((ood.isSet(expand) ? !!expand : !o._checked)), recursive, stopanim || recursive, callback);
                }
            } else {
                ood.arr.each(profile.properties.items, function (item) {
                    if (item.sub) self.call(ns, item.id, expand, recursive);
                });
            }
            return this;
        },

        /**
         * Reload node data
         * @param {String} id - 节点ID
         * @param {Boolean} expand - Whether to expand after reloading
         * @param {Boolean} recursive - 是否递归操作子节点
         * @param {Boolean} stopanim - 是否停止动画效果
         * @param {Function} callback - 操作完成后的回调函数
         * @returns {Object} 当前实例
         */
        reloadNode: function (id, expand, recursive, stopanim, callback) {
            var profile = this.get(0),
                ns = this,
                self = arguments.callee;

            if (id) {
                var o = profile.getItemByItemId(id);
                if (o && o.sub) {
                    var item = profile.getItemByItemId(id);
                    ood.arr.each(ood.clone(item.sub), function (oo) {
                        ns.removeItems([oo.id]);
                    });

                    o.sub = true;
                    ns.toggleNode(id, false);
                    ood.Thread.abort(profile.key + profile.id);
                    o._inited = false;
                    o._check = true;
                    ns.toggleNode(id, true);
                }
            }
            return this;
        },

        /**
         * Disable specified node(s)
         * @param {String|Array} ids - 节点ID或ID数组
         * @param {Boolean} deep - Whether to recursively disable child nodes
         * @returns {Object} 当前实例
         */
        disableNode: function (ids, deep) {
            var profile = this.get(0),
                ns = this,
                self = arguments.callee;

            if (ood.isArr(ids)) {
                ood.each(ids, function (id) {
                    ns._disableSingleNode(id, deep);
                });
            } else {
                ns._disableSingleNode(ids, deep);
            }
            return this;
        },


        /**
         * Disable single node
         * @private
         * @param {String} id - 节点ID
         * @param {Boolean} deep - 是否递归禁用子节点
         */
        _disableSingleNode: function (id, deep) {
            var profile = this.get(0),
                ns = this;

            var o = profile.getItemByItemId(id);
            if (o) {
                o.disabled = true;
                ns.updateItem(o, o.caption);

                // Recursively disable child nodes
                if (deep && o.sub && ood.isArr(o.sub)) {
                    ood.arr.each(o.sub, function (subItem) {
                        ns._disableSingleNode(subItem.id, deep);
                    });
                }
            }
        },

        /**
         * Get callback value
         * @returns {Object} 回调值对象
         */
        getCallBackValue: function () {
            var profile = this.get(0),
                ns = this,
                data = profile.getModule().getData(),
                callValue = {},
                item = this.getItemByItemId(this.getUIValue()),
                fieldName = data['fieldName'],
                fieldCaption = data['fieldCaption'];

            // Set field value
            if (fieldName && fieldName.value && item) {
                callValue[fieldName.value] = {
                    value: item.id,
                    captionValue: item.caption,
                    items: [ood.clone(item)]
                };
            }

            // Set caption value
            if (fieldCaption && fieldCaption.value &&
                fieldCaption.value != (fieldName && fieldName.value)) {
                callValue[fieldCaption.value] = this.getUICationValue();
            }

            return callValue;
        },
        /**
         * Expand to specified node
         * @param {String} id - 目标节点ID
         * @param {Boolean} triggerEvent - Whether to trigger click event
         * @returns {Object} 当前实例
         */
        openToNode: function (id, triggerEvent) {
            return this.each(function (profile) {
                var res = false,
                    nodePath = [],
                    // Recursively find node path
                    findNodePath = function (arr, targetId, layer) {
                        layer = layer || 0;
                        var self = findNodePath;

                        for (var i = 0; i < arr.length; i++) {
                            var node = arr[i];

                            // Found target node
                            if (node.id == targetId) {
                                nodePath.push(node);
                                res = true;
                                return true;
                            }

                            // Recursively find child nodes
                            if (node.sub && ood.isArr(node.sub)) {
                                if (self(node.sub, targetId, layer + 1)) {
                                    nodePath.push(node);
                                    return true;
                                }
                            }
                        }

                        return false;
                    };

                // Find node path
                findNodePath(profile.properties.items, id);

                // If node found, expand all nodes on path
                if (res) {
                    nodePath.reverse();
                    ood.arr.each(nodePath, function (node, index) {
                        var isLastNode = (index == nodePath.length - 1);
                        var isGroupNode = node.hasOwnProperty('group') ? node.group : profile.properties.group;

                        if (node.sub) {
                            // Expand node
                            profile.boxing().toggleNode(node.id, true);

                            // For last node, trigger click event
                            if (triggerEvent !== false && isLastNode && !isGroupNode) {
                                profile.boxing().fireItemClickEvent(node.id);
                            }
                        } else if (triggerEvent !== false) {
                            profile.boxing().fireItemClickEvent(node.id);
                        }
                    });
                }
            });
        },


        /**
         * Adjust layout based on screen size and current theme
         * @returns {Object} 当前实例
         */
        adjustLayout: function () {
            var profile = this.get(0);
            if (!profile) return this;

            var box = profile.getRoot(),
                width = box.width(),
                currentTheme = this.getTheme();

            // Remove responsive classes
            box.removeClass('ood-treebar-mobile ood-treebar-tiny');

            // 根据宽度添加响应式类
            if (width < 320) {
                box.addClass('ood-treebar-mobile ood-treebar-tiny');
            } else if (width < 480) {
                box.addClass('ood-treebar-mobile');
            }

            // 根据主题和设备类型应用不同的CSS变量
            var cssVars = {};

            // 基于主题和屏幕尺寸设置变量
            if (width < 480) { // 移动设备
                switch (currentTheme) {
                    case 'dark':
                        cssVars = {
                            '--item-height': '2.2em',
                            '--font-size': '0.9em',
                            '--icon-size': '1.2em',
                            '--padding': '0.5em'
                        };
                        break;
                    case 'high-contrast':
                        cssVars = {
                            '--item-height': '2.4em',
                            '--font-size': '1em',
                            '--icon-size': '1.4em',
                            '--padding': '0.6em'
                        };
                        break;
                    default: // light
                        cssVars = {
                            '--item-height': '2.2em',
                            '--font-size': '0.9em',
                            '--icon-size': '1.2em',
                            '--padding': '0.5em'
                        };
                }
            } else { // 桌面设备
                switch (currentTheme) {
                    case 'dark':
                        cssVars = {
                            '--item-height': '2em',
                            '--font-size': '1em',
                            '--icon-size': '1.4em',
                            '--padding': '0.4em'
                        };
                        break;
                    case 'high-contrast':
                        cssVars = {
                            '--item-height': '2.2em',
                            '--font-size': '1.1em',
                            '--icon-size': '1.6em',
                            '--padding': '0.5em'
                        };
                        break;
                    default: // light
                        cssVars = {
                            '--item-height': '2em',
                            '--font-size': '1em',
                            '--icon-size': '1.4em',
                            '--padding': '0.4em'
                        };
                }
            }

            // 应用CSS变量到根元素
            for (var prop in cssVars) {
                if (cssVars.hasOwnProperty(prop)) {
                    box.css(prop, cssVars[prop]);
                }
            }

            return this;
        },

        /**
         * Enhance accessibility
         * @returns {Object} 当前实例
         */
        enhanceAccessibility: function () {
            var profile = this.get(0);
            if (!profile) return this;

            // Add ARIA attributes to all nodes
            profile.boxing().getSubNode('ITEMS', true).each(function (item) {
                var itemNode = ood(item);
                if (itemNode) {
                    // Set role
                    itemNode.attr({'role': 'treeitem'});

                    // If has child nodes, add expanded state
                    if (item.children) {
                        var isExpanded = !itemNode.hasClass('ood-ui-item-fold');
                        itemNode.attr({
                            'aria-expanded': isExpanded ? 'true' : 'false',
                            'aria-level': (item._deep || 0) + 1
                        });
                    }
                }
            });

            // 为切换按钮添加ARIA属性
            profile.boxing().getSubNode('TOGGLE', true).each(function (toggle) {
                var toggleNode = ood(toggle);
                toggleNode.attr({
                    'role': 'button',
                    'aria-label': 'Expand/collapse node',
                    'tabindex': '0'
                });
            });

            // 为条目添加键盘导航属性
            profile.boxing().getSubNode('BAR', true).each(function (bar) {
                var barNode = ood(bar);
                barNode.attr({
                    'tabindex': '0',
                    'role': 'treeitem'
                });
            });

            return this;
        },

        /**
         * Export tree structure data
         * @returns {Array} 树结构数据
         */
        exportData: function () {
            var profile = this.get(0);
            if (!profile) return [];

            return ood.clone(profile.properties.items);
        },

        /**
         * Import tree structure data
         * @param {Array} data - 要导入的树结构数据
         * @returns {Object} 当前实例
         */
        importData: function (data) {
            if (!ood.isArr(data)) return this;

            this.clearItems();
            this.insertItems(data);
            return this;
        }
    },
    Static: {
        _focusNodeKey: 'BAR',
        // Cache all instances for global operations
        _cache: {},
        Templates: {
            tagName: 'div',
            style: '{_style}',
            className: '{_className} ood-treebar',
            BORDER: {
                tagName: 'div',
                className: "ood-uibase",
                BOX: {
                    tagName: 'div',
                    ITEMS: {
                        tagName: 'div',
                        className: '{_cmdsalign}',
                        text: "{items}"
                    }
                }
            },
            $submap: {
                items: {
                    ITEM: {
                        className: 'ood-tree-item',
                        style: '{_itemDisplay}',
                        tagName: 'div',
                        BAR: {
                            $order: 0,
                            tabindex: '{_tabindex}',

                            className: 'ood-uitembg   ood-uiborder-radius ood-uiborder-radius ood-showfocus {itemClass} {cls_group} {cls_fold} {_split} {disabled} {readonly}',
                            style: '{itemStyle};{_splitstyle} {_itemColor}',
                            RULER: {
                                $order: 2,
                                style: '{_ruleDisplay};{rulerStyle}',
                                text: '{innerIcons}'
                            },
                            TOGGLE: {
                                $order: 3,
                                style: '{_tglDisplay}',
                                className: 'oodfont',
                                $fonticon: '{_fi_togglemark}'
                            },
                            LTAGCMDS: {
                                $order: 4,
                                tagName: 'span',
                                style: '{_ltagDisplay}',
                                text: "{ltagCmds}"
                            },
                            MARK: {
                                $order: 5,
                                className: 'oodfont',
                                $fonticon: '{_fi_check}',
                                style: '{mark2Display}'
                            },
                            ITEMICON: {
                                $order: 6,
                                className: 'ood-icon {imageClass} {picClass}',
                                style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle} {_iconColor}',
                                text: '{iconFontCode}'
                            },
                            ITEMCAPTION: {
                                text: '{caption}',
                                style: '{_capDisplay} {_fontColor}',
                                className: "ood-item-caption {disabled} {readonly}",
                                $order: 7
                            },
                            EXTRA: {
                                style: '{_extraDisplay}',
                                text: '{ext}',
                                className: "ood-item-extra",
                                $order: 8
                            },
                            RTAGCMDS: {
                                $order: 9,
                                tagName: 'span',
                                style: '{_rtagDisplay}',
                                text: "{rtagCmds}"
                            },
                            OPT: {
                                $order: 10,
                                style: '{_optDisplay}',
                                className: 'oodfont',
                                $fonticon: '{_fi_optClass}'
                            }
                        },
                        SUB: {
                            $order: 1,
                            tagName: 'div',
                            text: ood.UI.$childTag
                        }
                    }
                },
                'items.ltagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    var buttonType = v.buttonType || v.type;
                    ood.UI.$doTemplate(profile, template, v, 'items.tagCmds' + (map[buttonType] || '.button'), result)
                },
                'items.rtagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    var buttonType = v.buttonType || v.type;
                    ood.UI.$doTemplate(profile, template, v, 'items.tagCmds' + (map[buttonType] || '.button'), result)
                },
                'items.tagCmds.text': ood.UI.$getTagCmdsTpl('text'),
                'items.tagCmds.button': ood.UI.$getTagCmdsTpl('button'),
                'items.tagCmds.image': ood.UI.$getTagCmdsTpl('image')
            }
        },
        Appearances: {
            KEY: {
                'border': 0
            },
            EXTRA: {
                display: 'none'
            },
            BOX: {
                left: '0em',
                overflow: 'auto',
                'overflow-x': 'hidden',
                position: 'relative',
                clear: "both",
                'border-radius': '8px',
                'box-shadow': '0 2px 8px rgba(0,0,0,0.08)',
                transition: 'all 0.3s ease'
            },

            ITEMS: {
                overflow: 'hidden',
                transition: 'all 0.3s ease',
                'padding': '.25em'
            },
            ITEM: {
                'white-space': 'nowrap',
                position: 'relative',
                'line-height': 1.5,
                overflow: 'hidden',
                transition: 'all 0.2s ease'
            },
            BAR: {
                zoom: ood.browser.ie ? 1 : null,
                position: 'relative',
                height: "2em",
                display: 'block',
                'outline-offset': '-1px',
                '-moz-outline-offset': (ood.browser.gek && ood.browser.ver < 3) ? '-1px !important' : null,
                'background': 'var(--bg-card, var(--bg-secondary, #ffffff))',
                'border-radius': 'var(--radius-md, 4px)',
                'margin': '1px 0',
                transition: 'all var(--ood-transition-fast)',
                cursor: 'pointer'
            },
            'BAR:hover': {
                'background': 'var(--bg-hover, #f0f0f0)',
                'color': 'var(--text-secondary, #666666)',
                'box-shadow': 'var(--shadow-md, 0 2px 4px rgba(0,0,0,0.1))'
            },

            'BAR-checked:hover, BAR-active:hover': {
                'background': 'var(--primary-active, #0078d4)',
                'box-shadow': 'var(--shadow-lg, 0 4px 8px rgba(0,0,0,0.15))'
            },
            'BAR-disabled': {
                'opacity': '0.6',
                'cursor': 'not-allowed',
                'transform': 'none !important',
                'box-shadow': 'none !important',
                'filter': 'grayscale(0.5)'
            },
            SUB: {
                zoom: ood.browser.ie ? 1 : null,
                height: 0,
                'font-size': ood.browser.ie68 ? '1px' : null,
                'line-height': ood.browser.ie68 ? '1px' : null,
                position: 'relative',
                overflow: 'hidden',
                'margin-left': '0.75em',
                transition: 'height var(--transition-normal)'
            },
            MARK: {
                cursor: 'pointer',
                'vertical-align': 'middle',
                'color': 'var(--mark-color, #1890ff)'
            },
            'BAR-group': {
                $order: 4,
                border: 'none',
                'font-weight': '500',
                'background': 'var(--group-bg, rgba(0,0,0,0.02))'
            },
            ITEMCAPTION: {
                'vertical-align': 'middle',
                padding: '.25em',
                //  'white-space': 'normal',
                'font-size': '0.95em',
                transition: 'color 0.2s ease'
            },
            OPT: {
                $order: 10,
                position: 'absolute',
                left: 'auto',
                top: '50%',
                'margin-top': '-0.5em',
                right: '.5em',
                display: 'none',
                opacity: '0.7',
                transition: 'opacity 0.2s ease'
            },
            'BAR:hover OPT': {
                display: 'inline-block',
                opacity: '1'
            },
            'LTAGCMDS, RTAGCMDS': {
                padding: 0,
                margin: 0,
                'vertical-align': 'middle'
            },
            'ITEMS-tagcmdleft RTAGCMDS': {
                "padding-right": '.333em',
                "float": "left"
            },
            'ITEMS-tagcmdfloatright RTAGCMDS': {
                "padding-right": '.333em',
                "float": "right"
            },
            TOGGLE: {
                padding: '0 .334em 0 0',
                transition: 'transform 0.2s ease'
            },
            'TOGGLE:hover': {
                'transform': 'scale(1.1)'
            },
            ITEMICON: {
                transition: 'all 0.2s ease',
                "padding": '.25em',
                'vertical-align': 'middle'
            },


            // Mobile styles
            'treebar-mobile BAR': {
                'height': '2.2em',
                'font-size': '0.9em',
                'padding': '.3em .6em'
            },
            'treebar-mobile ITEMCAPTION': {
                'font-size': '0.9em'
            },
            // Small screen styles
            'treebar-tiny ITEMICON': {
                'display': 'none'
            }
        },
        Behaviors: {
            HoverEffected: {TOGGLE: 'TOGGLE', BAR: 'BAR', OPT: 'OPT', CMD: 'CMD'},
            ClickEffected: {TOGGLE: 'TOGGLE', BAR: 'BAR', OPT: 'OPT', CMD: 'CMD'},
            DraggableKeys: ["BAR"],
            NoDraggableKeys: ['TOGGLE'],
            DroppableKeys: ["BAR", "TOGGLE", "BOX"],
            TOGGLE: {
                onClick: function (profile, e, src) {
                    var properties = profile.properties,
                        domId = ood.use(src).id(),
                        item = profile.getItemByDom(domId);

                    if (properties.disabled || item.disabled) return false;
                    if (!('sub' in item)) return false;
                    profile.box._setSub(profile, item, !item._checked);

                    // not to fire BAR's onclick event;
                    return false;
                }
            },
            BAR: {
                onDblclick: function (profile, e, src) {
                    var properties = profile.properties,
                        item = profile.getItemByDom(src),
                        rtn = profile.onDblclick && profile.boxing().onDblclick(profile, item, e, src);
                    if (item.sub && rtn !== false) {
                        profile.getSubNode('TOGGLE', profile.getSubId(src)).onClick();
                    }
                },
                onClick: function (profile, e, src) {
                    return profile.box._onclickbar(profile, e, src);
                },
                onKeydown: function (profile, e, src) {
                    return profile.box._onkeydownbar(profile, e, src);
                },
                onContextmenu: function (profile, e, src) {
                    if (profile.onContextmenu)
                        return profile.boxing().onContextmenu(profile, e, src, profile.getItemByDom(src), ood.Event.getPos(e)) !== false;
                },
                onMouseover: function (profile, e, src) {
                    if (ood.browser.fakeTouch || ood.browser.deviceType == 'touchOnly') return;
                    var item = profile.getItemByDom(src);
                    if (!item) return;
                    if (!profile.properties.optBtn && !item.optBtn) return;
                    profile.getSubNode('OPT', profile.getSubId(src)).setInlineBlock();
                },
                onMouseout: function (profile, e, src) {
                    if (ood.browser.fakeTouch || ood.browser.deviceType == 'touchOnly') return;
                    var item = profile.getItemByDom(src);
                    if (!item) return;
                    if (!profile.properties.optBtn && !item.optBtn) return;
                    profile.getSubNode('OPT', profile.getSubId(src)).css('display', 'none');
                }
            },
            OPT: {
                onClick: function (profile, e, src) {
                    if (profile.onShowOptions) {
                        var item = profile.getItemByDom(src);
                        if (!item) return;
                        if (!profile.properties.optBtn && !item.optBtn) return;
                        profile.boxing().onShowOptions(profile, item, e, src);
                    }
                    return false;
                },
                onDblclick: function (profile, e, src) {
                    return false;
                }
            },
            CMD: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties,
                        item = profile.getItemByDom(ood.use(src).parent().get(0));
                    if (!item) return false;

                    if (prop.disabled || item.disabled || item.type == 'split') return false;
                    if (profile.onCmd)
                        profile.boxing().onCmd(profile, item, ood.use(src).id().split('_')[1], e, src);
                    return false;
                }
            },
            BOX: {
                onScroll: function (profile, e, src) {
                    //for ie 'href focus' will scroll view
                    if ((e = ood.use(src)).scrollLeft() !== 0)
                        e.scrollLeft(0);
                }
            }
        },
        EventHandlers: {
            onShowOptions: function (profile, item, e, src) {
            },
            beforeClick: function (profile, item, e, src) {
            },
            onClick: function (profile, item, e, src) {
            },
            afterClick: function (profile, item, e, src) {
            },
            onCmd: function (profile, item, cmdkey, e, src) {
            },
            onDblclick: function (profile, item, e, src) {
            },

            onGetContent: function (profile, item, callback) {
            },
            onItemSelected: function (profile, item, e, src, type) {
            },

            beforeFold: function (profile, item) {
            },
            beforeExpand: function (profile, item) {
            },
            afterFold: function (profile, item) {
            },
            afterExpand: function (profile, item) {
            }
        },
        DataModel: {

            // Modern properties
            theme: {
                ini: 'dark',
                listbox: ['light', 'dark', 'high-contrast'],
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
            expression: {
                ini: '',
                caption: ood.getResText("DataModel.expression") || "表达式",
                action: function () {
                }
            },
            enumClass: {},
            listKey: null,
            isFormField: {
                hidden: true,
                ini: false
            },

            autoFontColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoIconColor: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },
            autoItemColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            iconColors: null,
            itemColors: null,
            fontColors: null,

            width: {
                $spaceunit: 1,
                ini: 'auto'
            },
            height: {
                $spaceunit: 1,
                ini: 'auto'
            },
            initFold: true,
            animCollapse: true,
            dock: 'fill',
            group: {
                ini: false,
                action: function (v) {
                    var self = this,
                        items = self.properties.items,
                        results = self.queryItems(items, function (o) {
                            return o.sub && o.group === undefined
                        }),
                        nodes = ood();
                    ood.arr.each(results, function (o) {
                        nodes.merge(self.getSubNodeByItemId('BAR', o.id));
                    });
                    if (v)
                        nodes.addClass('ood-uigradient ood-uibar ' + self.getClass('BAR', '-group'));
                    else
                        nodes.removeClass('ood-uigradient ood-uibar ' + self.getClass('BAR', '-group'));
                }
            },
            selMode: {
                ini: 'single',
                listbox: ['single', 'none', 'multi', 'singlecheckbox', 'multibycheckbox'],
                action: function (value) {
                    var ns = this, p = this.properties, sels = [];
                    ood.each(this.SubSerialIdMapItem, function (o) {
                        if (!(o.sub && (o.hasOwnProperty('group') ? o.group : p.group)))
                            sels.push(ns.getSubNodeByItemId('MARK', o.id).get(0));
                    });
                    ood(sels).css('display', (value == 'singlecheckbox' || value == 'multibycheckbox') ? '' : 'none');
                }
            },
            noCtrlKey: true,
            singleOpen: false,
            dynDestory: false,
            position: 'absolute',
            optBtn: {
                ini: "",
                combobox: ood.toArr("ood-uicmd-opt,ood-icon-singleright"),
                action: function () {
                    this.boxing().refresh();
                }
            },
            togglePlaceholder: false,
            tagCmds: {
                ini: [],
                action: function () {
                    this.boxing().refresh();
                }
            },
            tagCmdsAlign: {
                ini: "right",
                listbox: ['left', 'right', 'floatright'],
                action: function (v) {
                    var profile = this, box = profile.getSubNode("ITEMS"),
                        cls = profile.getClass('ITEMS', '-tagcmd');
                    box.removeClass(new RegExp(cls + '[\w]*')).addClass(profile.getClass('ITEMS', '-tagcmd' + v));
                }
            }
        },
        RenderTrigger: function () {
            this.boxing()._toggleNodes(this.properties.items, true, true, true);
        },


        _onclickbar: function (profile, e, src) {
            var properties = profile.properties,
                domId = ood.use(src).id(),
                item = profile.getItemByDom(domId),
                itemId = profile.getSubId(domId),
                box = profile.boxing(),
                ks = ood.Event.getKey(e),
                sk = profile.getKey(ood.Event.getSrc(e).id || ""),
                ignoreClick = sk == profile.keys.TOGGLE || sk == profile.keys.MARK;

            if (!ignoreClick && profile.beforeClick && false === box.beforeClick(profile, item, e, src)) return false;

            if (properties.disabled || item.disabled || item.type == 'split') return false;

            if (!ignoreClick && profile.onClick)
                box.onClick(profile, item, e, src);

            //group not fire event
            if (item.sub && (item.hasOwnProperty('group') ? item.group : properties.group)) {
                profile.getSubNode('TOGGLE', itemId).onClick();
                return;
            }

            profile.getSubNode(profile.box._focusNodeKey, itemId).focus(true);

            switch (properties.selMode) {
                case 'none':
                    box.onItemSelected(profile, item, e, src, 0);
                    break;
                case 'multibycheckbox':
                    if (properties.readonly || item.readonly) return false;
                    var captionvalue = box.getCaptionValue();
                    var value = box.getUIValue(),
                        cationarr = captionvalue ? captionvalue.split(properties.valueSeparator) : [],
                        arr = value ? value.split(properties.valueSeparator) : [],
                        checktype = 1;
                    var items = properties.items;
                    if (ks.shiftKey) {
                        if (profile.$firstV._pid != item._pid) return;
                        var items = properties.items;
                        if (item._pid) {
                            var pitem = profile.getItemByItemId(item._pid);
                            if (pitem) items = pitem.sub;
                        }
                        var i1 = ood.arr.subIndexOf(items, 'id', profile.$firstV.id),
                            i2 = ood.arr.subIndexOf(items, 'id', item.id),
                            i;
                        arr.length = 0;
                        for (i = Math.min(i1, i2); i <= Math.max(i1, i2); i++)
                            arr.push(items[i].id);
                    } else {

                        if (ood.arr.indexOf(arr, item.id) != -1) {
                            ood.arr.removeValue(arr, item.id);
                            ood.arr.removeValue(cationarr, item.caption);
                            profile.boxing()._removeAllkeys(arr, cationarr, item, profile);
                            checktype = -1;
                        } else {
                            arr.push(item.id);
                            cationarr.push(item.caption);
                            profile.boxing()._addAllkeys(arr, cationarr, item, profile);

                        }

                    }
                    arr.sort();
                    cationarr.sort();
                    value = arr.join(properties.valueSeparator);
                    captionvalue = cationarr.join(properties.valueSeparator);

                    //update string value only for _setCtrlValue
                    if (box.getUIValue() != captionvalue) {
                        profile._noScroll = 1;
                        box.setUIValue(value, null, null, 'click');
                        box.setCaptionValue(captionvalue);
                        delete profile._noScroll;
                        if (box.get(0) && box.getUIValue() == value)
                            box.onItemSelected(profile, item, e, src, checktype);
                    }
                    break;

                case 'multi':
                    if (properties.readonly || item.readonly) return false;
                    var value = box.getUIValue(),
                        arr = value ? value.split(properties.valueSeparator) : [],
                        checktype = 1;
                    if (arr.length && (ks.ctrlKey || ks.shiftKey || properties.noCtrlKey)) {
                        if (ks.shiftKey) {
                            if (profile.$firstV._pid != item._pid) return;
                            var items = properties.items;
                            if (item._pid) {
                                var pitem = profile.getItemByItemId(item._pid);
                                if (pitem) items = pitem.sub;
                            }
                            var i1 = ood.arr.subIndexOf(items, 'id', profile.$firstV.id),
                                i2 = ood.arr.subIndexOf(items, 'id', item.id),
                                i;
                            arr.length = 0;
                            for (i = Math.min(i1, i2); i <= Math.max(i1, i2); i++)
                                arr.push(items[i].id);
                        } else {
                            if (ood.arr.indexOf(arr, item.id) != -1) {
                                ood.arr.removeValue(arr, item.id);
                                checktype = -1;
                            } else
                                arr.push(item.id);
                        }
                        arr.sort();
                        value = arr.join(properties.valueSeparator);

                        //update string value only for _setCtrlValue
                        if (box.getUIValue() != value) {
                            profile._noScroll = 1;
                            box.setUIValue(value, null, null, 'click');
                            delete profile._noScroll;
                            if (box.get(0) && box.getUIValue() == value)
                                box.onItemSelected(profile, item, e, src, checktype);
                        }
                        break;
                    }
                case 'single':
                    if (box.getUIValue() != item.id) {
                        profile.$firstV = item;
                        profile._noScroll = 1;
                        box.setUIValue(item.id, null, null, 'click');
                        delete profile._noScroll;
                        if (box.get(0) && box.getUIValue() == item.id)
                            box.onItemSelected(profile, item, e, src, 1);
                    }
                    break;

                case 'singlecheckbox':
                    if (box.getUIValue() != item.id) {
                        profile.$firstV = item;
                        profile._noScroll = 1;
                        box.setUIValue(item.id, null, null, 'click');
                        delete profile._noScroll;
                        if (box.get(0) && box.getUIValue() == item.id)
                            box.onItemSelected(profile, item, e, src, 1);
                    }
                    break;


            }
            if (!ignoreClick && profile.afterClick) box.afterClick(profile, item, e, src);
            return !ignoreClick;
        },
        _onkeydownbar: function (profile, e, src) {
            var keys = ood.Event.getKey(e), key = keys.key, shift = keys.shiftKey, ctrl = keys.ctrlKey,
                cur = profile.getSubNode(profile.box._focusNodeKey, profile.getSubId(src)),
                root = profile.getRoot(),
                first = root.nextFocus(true, true, false),
                last = root.nextFocus(false, true, false);

            switch (key) {
                case 'enter':
                    cur.onClick();
                    break;
                case 'tab':
                    if (shift) {
                        if (cur.get(0) != first.get(0)) {
                            first.focus(true);
                            return false;
                        }
                    } else {
                        if (cur.get(0) != last.get(0)) {
                            last.focus(true);
                            return false;
                        }
                    }
                    break;
                case 'up':
                    if (ctrl) {
                        profile.getSubNode('TOGGLE', profile.getSubId(src)).onClick();
                        return false;
                    }
                    if (cur.get(0) == first.get(0))
                        last.focus(true);
                    else
                        cur.nextFocus(false, true, false).focus(true);
                    return false;
                    break;
                case 'down':
                    if (ctrl) {
                        profile.getSubNode('TOGGLE', profile.getSubId(src)).onClick();
                        return false;
                    }
                    if (cur.get(0) == last.get(0))
                        first.focus(true);
                    else
                        cur.nextFocus(true, false, false).focus(true);
                    return false;
                    break;
                case 'right':
                case 'left':
                    profile.getSubNode('TOGGLE', profile.getSubId(src)).onClick();
                    return false;
            }
        },
        _onStartDrag: function (profile, e, src, pos) {
            var pos = ood.Event.getPos(e);
            ood.use(src).startDrag(e, {
                dragSource: profile.$xid,
                dragType: 'icon',
                shadowFrom: src,
                targetLeft: pos.left + 12,
                targetTop: pos.top + 12,
                dragCursor: 'pointer',
                dragDefer: 2,
                dragKey: profile.box.getDragKey(profile, src),
                dragData: profile.box.getDragData(profile, e, src)
            });
            return false;
        },
        _onDropTest: function (profile, e, src, key, data, item) {
            var fid = data && data.domId;
            if (fid) {
                var tid = ood.use(src).id();
                if (fid == tid) return false;

                if (ood.get(ood.use(src).get(0), ['parentNode', 'previousSibling', 'firstChild', 'id']) == fid) return false;

                var oitem = profile.getItemByDom(fid);

                // stop self
                if (oitem && item && oitem._pid == item.id) return false;

                var p = ood.use(src).get(0),
                    rn = profile.getRootNode();
                // stop children
                while ((p = p.parentNode)) {
                    if (profile.getSubId(p.id) == profile.getSubId(fid)) {
                        return false;
                    }
                    if (p.id == ood.get(rn, ["parentNode", "id"])) {
                        break;
                    }
                }
            }
        },
        _onDrop: function (profile, e, src, key, data, item) {
            var k = profile.getKey(ood.use(src).id()),
                po = data.profile,
                ps = data.domId,
                oitem,
                ks = profile.keys,
                t = ood.absObj.$specialChars,
                b = profile.boxing(),
                arr = ood.copy(b.getUIValue(true));
            //remove
            oitem = ood.clone(po.getItemByDom(ps), function (o, i) {
                return !t[(i + '').charAt(0)]
            });
            po.boxing().removeItems([oitem.id]);

            //add
            if (k == ks.BOX)
                b.insertItems([oitem], null, null, false);
            else if (k == ks.BAR)
                b.insertItems([oitem], item._pid, item.id, true);
            else if (k == ks.TOGGLE)
                b.insertItems([oitem], item.id, null, false);

            if (arr && arr.length) {
                if (ood.arr.indexOf(arr, oitem.id) != -1) {
                    //set checked items
                    profile._noScroll = 1;
                    b.setUIValue(arr, true, null, 'drop');
                    delete profile._noScroll;
                }
            }
            data._new = oitem;
            return false;
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                ns = this,
                p = profile.properties;

            data._cmdsalign = profile.getClass('ITEMS', '-tagcmd' + profile.properties.tagCmdsAlign);
            ood.arr.each(data.items, function (item) {
                var index = item.index;
                if (!index) {
                    index = ood.arr.indexOf(data.items, item);
                }
                profile.boxing()._autoColor(item, index, p);
            })

            // Enhance accessibility
            // setTimeout(function () {
            //     profile.boxing().enhanceAccessibility();
            // }, 0);

            return data;
        },

        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties,
                map1 = profile.ItemIdMapSubSerialId,
                map2 = profile.SubSerialIdMapItem,
                pitem;

            item._fi_check = item.ficheck ? item.ficheck : 'ood-uicmd-check';
            if (pid)
                oitem._pid = pid;

            profile.boxing()._autoColor(item, index, p);


            // set 'visible' will show when parent call .height()
            item._fi_togglemark = item.sub ? ('ood-uicmd-toggle' + (item._checked ? " oodfont-checked ood-uicmd-toggle-checked" : "")) : (p.togglePlaceholder ? 'ood-icon-placeholder' : 'ood-uicmd-none');
            item.disabled = item.disabled ? 'ood-ui-disabled' : '';
            item.mark2Display = ('showMark' in item) ? (item.showMark ? '' : 'display:none;') : (p.selMode == 'multi' || p.selMode == 'multibycheckbox') ? '' : 'display:none;';
            item._tabindex = p.tabindex;
            item._fi_optClass = p.optBtn;

            //change css class
            if (item.sub && (item.hasOwnProperty('group') ? item.group : p.group)) {
                item.cls_group = "ood-uigradient ood-uibar " + profile.getClass('BAR', '-group');
                item.mark2Display = 'display:none';
            }
            this._prepareCmds(profile, item);

            if (ood.browser.fakeTouch || ood.browser.deviceType == 'mouseOnly') {
                item._optDisplay = p.optBtn ? 'display:block;' : '';
            }

            item.imageClass = item.icon ? item.icon : item.imageClass;

            if (item.tglDisplay && item.sub) {
                item._tglDisplay = 'display:none;';
                item._optDisplay = 'display:block;'
            }

            if (item.type == 'split') {
                item._split = 'ood-uitem-split';
                item._ruleDisplay = item._ltagDisplay = item._tglDisplay = item._rtagDisplay = item.imageDisplay = item.mark2Display = item._capDisplay = item._extraDisplay = item._optDisplay = 'display:none;';
            }
        },
        _setSub: function (profile, item, flag, recursive, stopanim, cb) {
            var id = profile.domId,
                ins = profile.boxing(),
                prop = profile.properties,
                itemId = profile.getSubIdByItemId(item.id),
                markNode = profile.getSubNode('TOGGLE', itemId),
                subNs = profile.getSubNode('SUB', itemId),

                barNode = profile.getSubNode('BAR', itemId),
                icon = profile.getSubNode('ITEMICON', itemId);

            if (ood.Thread.isAlive(profile.key + profile.id)) return;
            //close
            if (!flag) {
                if (item._checked) {
                    if (ins.beforeFold && false === ins.beforeFold(profile, item)) {
                        return;
                    }
                    var onend = function () {
                        subNs.css({display: 'none', height: 0});
                        markNode.tagClass('-checked', false);
                        barNode.tagClass('-expand', false).tagClass('-fold');
                        icon.tagClass('-expand', false).tagClass('-fold');
                        item._checked = false;
                        if (prop.dynDestory || item.dynDestory) {
                            var s = item.sub, arr = [];
                            for (var i = 0, l = s.length; i < l; i++)
                                arr.push(s[i].id);
                            profile.boxing().removeItems(arr);
                            item.sub = true;
                            delete item._inited;
                        }
                        if (ins.afterFold)
                            ins.afterFold(profile, item);
                        ood.resetRun(id, function (cb) {
                            if (cb) ood.tryF(cb, [profile, item], ins);
                        }, 0, [cb]);
                    };
                    if (!stopanim) {
                        if (prop.animCollapse) {
                            subNs.animate({'height': [subNs.height(), 0]}, null, onend, 200, null, 'expoOut', profile.key + profile.id).start();
                        } else onend();
                    } else onend();
                }
                if (recursive && item.sub && !prop.dynDestory && !item.dynDestory) {
                    ood.arr.each(item.sub, function (o) {
                        if (o.sub && o.sub.length)
                            profile.box._setSub(profile, o, flag, recursive, true, cb);
                    });
                }
            } else {
                //open
                if (!item._checked) {
                    if (ins.beforeExpand && false === ins.beforeExpand(profile, item)) {
                        return;
                    }
                    var onend = function (empty) {
                            subNs.css({display: '', height: 'auto'});
                            //markNode.css('background','');
                            // compitable with IE<8
                            if (ood.browser.ie && ood.browser.ver <= 8) {
                                markNode.css({
                                    backgroundImage: '',
                                    backgroundRepeat: '',
                                    backgroundPositionX: '',
                                    backgroundPositionY: '',
                                    backgroundColor: '',
                                    backgroundAttachment: ''
                                });
                            } else {
                                markNode.removeClass('ood-icon-loading');
                            }
                            if (!empty) {
                                item._checked = true;
                                if (ins.afterExpand)
                                    ins.afterExpand(profile, item);
                            }
                            ood.resetRun(id, function (cb) {
                                if (cb) ood.tryF(cb, [profile, item], ins);
                            }, 0, [cb]);
                        },
                        openSub = function (profile, item, id, markNode, subNs, barNode, icon, sub) {
                            var b = profile.boxing(),
                                p = profile.properties,
                                empty = sub === false;
                            //created
                            if (!empty && !item._inited) {
                                delete item.sub;
                                //before insertRows
                                item._inited = true;
                                if (sub) {
                                    if (typeof sub == 'string')
                                        subNs.html(item.sub = sub, false);
                                    else if (sub['ood.Template'] || sub['ood.UI']) {
                                        subNs.append(item.sub = sub.render(true));
                                    } else if (ood.isArr(sub)) {
                                        b.insertItems(sub, item.id);
                                        // for []
                                        if (!item.sub) item.sub = sub;
                                    }
                                    var s = 0, arr = b.getUIValue(true);
                                    if (arr && arr.length) {
                                        ood.arr.each(sub, function (o) {
                                            if (ood.arr.indexOf(arr, o.id || o) != -1) {
                                                s = 1;
                                                return false;
                                            }
                                        });
                                        if (s) {
                                            //set checked items
                                            profile._noScroll = 1;
                                            b._setCtrlValue(b.getUIValue());
                                            delete profile._noScroll;
                                        }
                                    }
                                }
                            }

                            if (p.singleOpen)
                                b._toggleNodes(item._pid ? profile.getItemByItemId(item._pid).sub : p.items, false)

                            if (!empty) {
                                markNode.tagClass('-checked');
                                barNode.tagClass('-expand').tagClass('-fold', false);
                                icon.tagClass('-fold', false).tagClass('-expand');
                            }

                            if (!stopanim) {
                                subNs.css("height", "0px").css("display", '');

                                if (p.animCollapse) {
                                    var h = 0;
                                    subNs.children().each(function (o) {
                                        h += o.offsetHeight;
                                    });
                                    subNs.animate({'height': [0, h]}, null, function () {
                                        onend(empty);
                                    }, 200, null, 'expoIn', profile.key + profile.id).start();
                                } else onend(empty);
                            } else onend(empty);
                        },
                        sub = item.sub,
                        callback = function (sub) {
                            var ns = this, nns = profile.boxing(), rep = ns._response, ids = rep ? rep.ids : [];
                            if (sub && sub.data) {
                                sub = sub.data;
                            }
                            openSub(profile, item, id, markNode, subNs, barNode, icon, sub);

                            if (ids && ood.isArr(ids)) {
                                ood.arr.each(ids, function (id) {
                                    // nns.fireItemClickEvent(id)
                                })
                            }

                        }, t;

                    if ((t = typeof sub) == 'string' || (t == 'object' && ((ood.isArr(sub) && sub.length > 0))))
                        callback(sub);
                    else if (profile.onGetContent) {
                        if (ood.browser.ie && ood.browser.ver <= 8) {
                            markNode.css('background', 'url(' + ood.ini.img_busy + ') no-repeat');
                        } else {
                            markNode.addClass('ood-icon-loading');
                        }
                        var r = profile.boxing().onGetContent(profile, item, callback);
                        if (r || r === false) {
                            //return true: toggle icon will be checked
                            if (r === true)
                                item._inited = true;
                            callback(r);
                        }
                    }
                }
                if (recursive && item.sub) {
                    ood.arr.each(item.sub, function (o) {
                        if (o.sub && o.sub.length && !o._checked)
                            profile.box._setSub(profile, o, flag, recursive, true, cb);
                    });
                }
            }
        },
        _tofold: function (profile, item, pid) {
            profile.getSubNodeByItemId('BAR', pid).addClass(profile.getClass('BAR', '-fold'));
            profile.getSubNodeByItemId('TOGGLE', pid).replaceClass(new RegExp("\\buicmd-(none|empty)\\b"), "ood-uicmd-toggle");
        },
        _onresize: function (profile, width, height) {
            profile.getSubNode('BORDER').cssSize({width: width ? width : null, height: height ? height : null});
            profile.getSubNode('BOX').cssSize({width: width ? width : null, height: height ? height : null});
        },
    }

});
