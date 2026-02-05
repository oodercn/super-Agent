ood.Class("ood.UI.TreeView", "ood.UI.TreeBar", {
    Initialize: function () {
        this.addTemplateKeys(['IMAGE']);
        var t = this.getTemplate();
        var n = t.$submap.items.ITEM.BAR.ITEMICON;
        n.$fonticon = '{_fi_cls_file}';
        this.setTemplate(t);
    },

    Instance: {

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
        },

        // Modern feature: Set theme
        setTheme: function (theme) {
            return this.each(function (profile) {
                var p = profile.properties;
                p.theme = theme;
                if (!p.autoItemColor) {

                    var root = profile.getRoot();
                    // Save theme to properties
                    profile.properties.theme = theme;

                    root.attr('data-theme', theme);

                }

                // Save theme settings
                localStorage.setItem('treeview-theme', theme);
            });
        },

        // Get current theme
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('treeview-theme') || 'light';
        },


        // Modern initialization trigger
        TreeViewTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties


            // Initialize theme
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('treeview-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }

            // Initialize responsive design
            if (prop.responsive !== false) {
                this.adjustLayout();
                // Note: Window resize listeners need to be handled at application level
                // OOD framework has its own event handling mechanism
            }

            // Initialize accessibility
            this.enhanceAccessibility();
        },
        // Toggle dark mode
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
        },

        // Responsive layout adjustment with theme awareness
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // Remove responsive classes
                root.removeClass('treeview-mobile treeview-tiny');

                // Apply responsive classes based on screen size
                if (width < 768) {
                    root.addClass('treeview-mobile');
                }
                if (width < 480) {
                    root.addClass('treeview-tiny');
                }

                // Special handling for high contrast mode in tiny screens
                if (width < 480 && profile.boxing().getTheme() === 'high-contrast') {
                    profile.getSubNode('BOX').css('--sub-margin', '1em');
                }
            });
        },

        // Enhanced accessibility support with keyboard navigation
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    bars = profile.getSubNode('BAR', true),
                    toggles = profile.getSubNode('TOGGLE', true);

                // Add ARIA attributes to container
                root.attr({
                    'role': 'tree',
                    'aria-label': ood.getRes('UI.treeview'),
                    'aria-multiselectable': profile.properties.selMode === 'multi' ? 'true' : 'false'
                });

                // Add ARIA attributes to tree nodes
                items.each(function (item) {
                    var itemNode = ood(item);
                    var itemId = itemNode.id();
                    if (itemId) {
                        var itemData = profile.getItemByDom(item);
                        var isSelected = itemNode.hasClass('ood-ui-item-checked');

                        itemNode.attr({
                            'role': 'treeitem',
                            'aria-label': ood.getRes('UI.treeview.node') + ': ' + (itemData ? itemData.caption : itemId),
                            'aria-selected': isSelected ? 'true' : 'false',
                            'aria-disabled': itemData && itemData.disabled ? 'true' : 'false'
                        });

                        // If has child nodes, add expanded state
                        if (itemData && itemData.sub && itemData.sub.map) {
                            var isExpanded = !itemNode.hasClass('ood-ui-item-fold');
                            itemNode.attr({
                                'aria-expanded': isExpanded ? 'true' : 'false',
                                'aria-level': (itemData._deep || 0) + 1,
                                'aria-owns': itemData.sub.map(sub => sub.id).join(' ')
                            });
                        }
                    }
                });

                // Enhanced ARIA attributes for toggle buttons
                toggles.each(function (toggle) {
                    var toggleNode = ood(toggle);
                    var parentItem = toggleNode.parent().parent();
                    var isExpanded = !parentItem.hasClass('ood-ui-item-fold');

                    toggleNode.attr({
                        'role': 'button',
                        'aria-label': isExpanded ?
                            ood.getRes('UI.treeview.collapse') :
                            ood.getRes('UI.treeview.expand'),
                        'aria-controls': parentItem.id(),
                        'tabindex': '0',
                        'aria-expanded': isExpanded ? 'true' : 'false'
                    });
                });

                // Enhanced keyboard navigation for bars
                bars.each(function (bar) {
                    var barNode = ood(bar);
                    var itemId = barNode.parent().id();
                    var itemData = profile.getItemByDom(barNode.parent().get(0));

                    barNode.attr({
                        'tabindex': '0',
                        'role': 'treeitem',
                        'aria-label': itemData ? itemData.caption : itemId,
                        'aria-selected': barNode.hasClass('ood-ui-item-checked') ? 'true' : 'false'
                    });

                    // Add keyboard event handlers
                    barNode.on('keydown', function (e) {
                        var key = e.key;
                        var currentItem = ood(this).parent();
                        var nextItem, prevItem, parentItem, firstChild;

                        switch (key) {
                            case 'ArrowDown':
                                nextItem = currentItem.next();
                                if (nextItem.length) {
                                    nextItem.find('[role="treeitem"]').focus();
                                }
                                break;
                            case 'ArrowUp':
                                prevItem = currentItem.prev();
                                if (prevItem.length) {
                                    prevItem.find('[role="treeitem"]').focus();
                                }
                                break;
                            case 'ArrowRight':
                                if (currentItem.hasClass('ood-ui-item-fold') && itemData.sub) {
                                    // Expand if collapsed
                                    profile.box._onclickbar(profile, e, itemId);
                                } else if (itemData.sub) {
                                    // Move to first child
                                    firstChild = currentItem.children('.ood-ui-submap').children().first();
                                    if (firstChild.length) {
                                        firstChild.find('[role="treeitem"]').focus();
                                    }
                                }
                                break;
                            case 'ArrowLeft':
                                if (!currentItem.hasClass('ood-ui-item-fold') && itemData.sub) {
                                    // Collapse if expanded
                                    profile.box._onclickbar(profile, e, itemId);
                                } else {
                                    // Move to parent
                                    parentItem = profile.getItemByItemId(itemData._pid);
                                    if (parentItem) {
                                        ood('#' + parentItem.id).find('[role="treeitem"]').focus();
                                    }
                                }
                                break;
                            case 'Enter':
                            case 'Space':
                                profile.box._onclickbar(profile, e, itemId);
                                break;
                            case 'Home':
                                root.find('[role="treeitem"]').first().focus();
                                break;
                            case 'End':
                                root.find('[role="treeitem"]').last().focus();
                                break;
                        }
                    });
                });
            });
        }
    },

    Static: {
        Appearances: {
            ITEMS: {
                'padding': '0',
                transition: 'all var(--transition-normal)'
            },
            ITEM: {
                'white-space': 'nowrap',
                position: 'relative',
                'line-height': 1.22,
                overflow: 'hidden',
                transition: 'all 0.2s ease',
                'color': 'var(--ood-treeview-text)',
                'padding-left': 'var(--ood-treeview-indent)'
            },


            BOX: {
                left: '0em',
                overflow: 'auto',
                position: 'relative',
                'background-color': 'var(--ood-treeview-bg)',
                'border': 'var(--ood-treeview-border)',
                'border-radius': '0.25rem',
                transition: 'all 0.3s ease'
            },


            // Mobile styles
            'treeview-mobile BAR': {
                'height': '2.2em',
                'font-size': '0.9em',
                'background': 'var(--ood-treeview-item-hover)'
            },
            'treeview-mobile CAPTION': {
                'font-size': '0.9em',
                'color': 'var(--ood-treeview-text)'
            },

            // Small screen styles
            'treeview-tiny ITEMICON': {
                'display': 'none'
            },

            // Hover and selected states
            'ITEM-hover': {
                'background-color': 'var(--ood-treeview-item-hover)'
            },
            'ITEM-checked': {
                'background-color': 'var(--ood-treeview-item-selected)'
            },
            'treeview-tiny BAR': {
                'padding': '0.5em',
                'background': 'var(--ood-treeview-item-selected)'
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
            // Modern properties
            theme: {
                ini: 'dark',
                caption: ood.getResText("DataModel.theme") || "主题",
                listbox: ['light', 'dark', 'high-contrast'],
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
            $subMargin: {
                ini: 1.8,
                caption: ood.getResText("DataModel.subMargin") || "子项边距"
            },
            group: {
                ini: null,
                caption: ood.getResText("DataModel.group") || "分组"
            },
            noIcon: {
                ini: false,
                caption: ood.getResText("DataModel.noIcon") || "无图标",
                action: function (v) {
                    this.getSubNode("ITEMICON", true).css('display', v ? 'none' : '');
                }
            },
            iconColors: {
                ini: null,
                caption: ood.getResText("DataModel.iconColors") || "图标颜色列表"
            },
            fontColors: {
                ini: null,
                caption: ood.getResText("DataModel.fontColors") || "字体颜色列表"
            },
            itemColors: {
                ini: null,
                caption: ood.getResText("DataModel.itemColors") || "项目颜色列表"
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
            tabindex: {
                ini: 0,
                caption: ood.getResText("DataModel.tabindex") || "Tab索引"
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile), ns = this, p = profile.properties;
            return data;
        },

        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var p = profile.properties,
                map1 = profile.ItemIdMapSubSerialId,
                map2 = profile.SubSerialIdMapItem,
                pnode = profile.getItemByItemId(pid),
                prop = {},
                pitem;
            if (item.fiCheck) {
                item._fi_check = item.fiCheck;
            } else {
                item._fi_check = 'ood-uicmd-check';
            }

            if (pnode) {
                prop.autoIconColor = pnode.autoIconColor ? pnode.autoIconColor : p.autoIconColor;
                prop.autoFontColor = pnode.autoFontColor ? pnode.autoFontColor : p.autoFontColor;
                prop.autoItemColor = pnode.autoItemColor ? pnode.autoItemColor : p.autoItemColor;
            }
            profile.boxing()._autoColor(item, index, prop);
            if (pid) {
                oitem._pid = pid;
                if (pitem = map2[map1[pid]]) {
                    oitem._deep = pitem._deep + 1;
                    item.rulerStyle = 'width:' + (oitem._deep * p.$subMargin) + 'em;';
                    // for the last one
                    item._fi_togglemark = item.sub ? ('ood-uicmd-toggle' + (item._checked ? '-checked' : '')) : (p.togglePlaceholder ? 'ood-uicmd-empty' : 'ood-uicmd-none');
                }
            } else {
                oitem._deep = 0;
                item.rulerStyle = '';
                item.innerIcons = '';
                item._fi_togglemark = item.sub ? ('ood-uicmd-toggle' + (item._checked ? '-checked' : '')) : (p.togglePlaceholder ? 'ood-uicmd-empty' : 'ood-uicmd-none');
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
        },


        RenderTrigger: function () {
            // Modern feature initialization
            var self = this;
            ood.asyRun(function () {
                if (self.boxing() && self.boxing().TreeViewTrigger) {
                    self.boxing().TreeViewTrigger();
                }

            });
        }

    }
});
