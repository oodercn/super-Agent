ood.Class("ood.UI.FormLayout", ["ood.UI", "ood.absList"], {
    Initialize: function () {
        this.addTemplateKeys(['ITEM', 'TABLE', 'CBORDER', 'CBT', 'CBL', 'HOLDER', 'SPREADER']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {

            "name": "TestForm",
            "showEffects": "Classic",
            "width": "50em",
            "height": "20em",
            "visibility": "visible",
            "floatHandler": false,
            "defaultRowHeight": 35,
            "defaultColWidth": 150,
            "layoutData": {
                "rows": 3,
                "cols": 2,
                "merged": [
                    {
                        "row": 0,
                        "col": 0,
                        "rowspan": 1,
                        "colspan": 2,
                        "removed": false
                    }
                ],
                "rowSetting": {
                    "1": {
                        "manualHeight": 56
                    },
                    "2": {
                        "manualHeight": 35
                    },
                    "3": {
                        "manualHeight": 142
                    }
                },
                "colSetting": {
                    "A": {
                        "manualWidth": 150
                    },
                    "B": {
                        "manualWidth": 525
                    }
                },
                "cells": {
                    "A1": {
                        "value": "标题",
                        "style": {
                            "textAlign": "center",
                            "fontSize": "28px"
                        }
                    },
                    "A2": {
                        "value": "名称",
                        "style": {
                            "textAlign": "center"
                        }
                    },
                    "B2": {
                        "value": "张三",
                        "style": {
                            "textAlign": "left",
                            "paddingLeft": "10px"
                        }
                    },
                    "A3": {
                        "value": "描述",
                        "style": {
                            "textAlign": "center"
                        }
                    },
                    "B3": {
                        "value": "这是一个示例表单，用于展示表单布局的基本功能。",
                        "style": {
                            "textAlign": "left",
                            "paddingLeft": "10px",
                            "fontSize": "14px",
                            "color": "#666"
                        }
                    }
                }

            }
        },

        _isDesignMode: function () {
            return this.getMode() == "design";
        },
        getContainer: function (subId) {
            var prf = this.get(0);
            if (prf.ItemIdMapSubSerialId && prf.ItemIdMapSubSerialId[subId]) {
                return arguments.callee.upper.apply(this, [subId]);
            } else {
                return this.getSubNode("POOL");
            }
        },
    },


    setChildren: function (childrens, prf) {
        var host = this, items = [], ctx = host.getModule().getCtxComponents();
        this.removeChildren(true, true, true);
        ood.each(childrens, function (o) {
            if (o && o.alias) {
                if (o.alias == 'PAGECTX') {
                    ctx.boxing().removeChildren(true, true, true);
                    host.getModule().addCtxComponents(o.children);
                } else {
                    items.push(o);
                }
            }
        });
        ood.addChild({children: items}, host, host.getModule(), host.getModule());
    },

    // 现代化功能：设置主题
    setTheme: function (theme) {


        // 动态加载主题CSS
        if (!document.getElementById('input-theme-css')) {
            var link = document.createElement('link');
            link.id = 'input-theme-css';
            link.rel = 'stylesheet';
            link.href = 'ood/css/formlayout-themes.css';
            document.head.appendChild(link);
        }


        return this.each(function (profile) {
            // 保存主题到属性
            profile.properties.theme = theme;

            // 使用OOD框架的全局主题API
            if (window.ood && ood.setGlobalTheme) {
                ood.setGlobalTheme(theme);
            } else {
                // 备用方案：直接设置data-theme属性
                document.documentElement.setAttribute('data-theme', theme);
                localStorage.setItem('global-theme', theme);
            }
        });
    },

    // 现代化初始化触发器
    FormLayoutTrigger: function () {
        var profile = this.get(0);
        var prop = profile.properties,
            boxing = this;

        // 初始化主题
        if (prop.theme) {
            boxing.setTheme(prop.theme);
        } else {
            // 从本地存储恢复主题
            var savedTheme = localStorage.getItem('formlayout-theme');
            if (savedTheme) {
                boxing.setTheme(savedTheme);
            }
        }

        // 初始化响应式设计
        if (prop.responsive !== false) {
            boxing.adjustLayout();
            // 注意：窗口大小变化的监听需要在应用层面处理
            // OOD框架有自己的事件处理机制
        }

        // 初始化可访问性
        boxing.enhanceAccessibility();
    },


    // 获取当前主题
    getTheme: function () {
        var profile = this.get(0);
        return profile.properties.theme || localStorage.getItem('formlayout-theme') || 'light';
    },

    // 切换暗黑模式
    toggleDarkMode: function () {
        var currentTheme = this.getTheme();
        this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
    },

    // 切换高对比度模式
    toggleHighContrast: function () {
        var currentTheme = this.getTheme();
        this.setTheme(currentTheme === 'highcontrast' ? 'light' : 'highcontrast');
    },

    // 响应式布局调整
    adjustLayout: function () {
        return this.each(function (profile) {
            var root = profile.getRoot(),
                width = ood(document.body).cssSize().width,
                box = profile.getSubNode('BOX'),
                table = profile.getSubNode('TABLE'),
                items = profile.getSubNode('ITEM', true),
                prop = profile.properties;

            // 对于小屏幕，调整布局
            if (width < 768) {
                root.addClass('formlayout-mobile');

                // 平板模式调整
                if (table && !table.isEmpty()) {
                    table.css({
                        'font-size': '0.9em'
                    });
                }
                items.css({
                    'padding': '4px 6px',
                    'font-size': '0.9em'
                });

                // 调整默认行高和列宽
                if (profile.boxing()._isDesignMode()) {
                    if (prop.defaultRowHeight > 40) {
                        profile.getSubNode('BOX').css('--row-height', '40px');
                    }
                    if (prop.defaultColWidth > 100) {
                        profile.getSubNode('BOX').css('--col-width', '100px');
                    }
                }
            } else {
                root.removeClass('formlayout-mobile');

                // 恢复桌面布局
                if (table && !table.isEmpty()) {
                    table.css({
                        'font-size': ''
                    });
                }
                items.css({
                    'padding': '',
                    'font-size': ''
                });

                // 恢复默认尺寸
                if (profile.boxing()._isDesignMode()) {
                    profile.getSubNode('BOX').css({
                        '--row-height': '',
                        '--col-width': ''
                    });
                }
            }

            // 超小屏幕特殊处理
            if (width < 480) {
                root.addClass('formlayout-tiny');

                // 进一步压缩间距
                items.css({
                    'padding': '2px 4px',
                    'font-size': '0.8em'
                });
            } else {
                root.removeClass('formlayout-tiny');

                // 恢复正常间距
                if (width >= 768) {
                    items.css({
                        'padding': '',
                        'font-size': ''
                    });
                }
            }

            // 触发表格重新计算
            if (profile.renderId && profile.box._resizeTable) {
                var size = box.cssSize();
                profile.box._resizeTable(profile, size, false, true);
            }
        });
    },

    // 增强可访问性支持
    enhanceAccessibility: function () {
        return this.each(function (profile) {
            var root = profile.getRoot(),
                box = profile.getSubNode('BOX'),
                table = profile.getSubNode('TABLE'),
                items = profile.getSubNode('ITEM', true),
                holder = profile.getSubNode('HOLDER');

            // 为容器添加ARIA属性
            root.attr({
                'role': 'grid',
                'aria-label': '表单布局网格'
            });

            // 为表格添加ARIA属性
            if (table && !table.isEmpty()) {
                table.attr({
                    'role': 'table',
                    'aria-label': '表单布局表格'
                });
            }

            // 为持有者添加ARIA属性
            if (holder && !holder.isEmpty()) {
                holder.attr({
                    'role': 'region',
                    'aria-label': '表单内容区域'
                });
            }

            // 为网格单元格添加ARIA属性
            items.each(function (item) {
                var itemNode = ood(item);
                var coord = itemNode.attr('data-coord');
                if (coord) {
                    itemNode.attr({
                        'role': 'gridcell',
                        'aria-label': '单元格 ' + coord,
                        'tabindex': '0'
                    });
                }
            });

            // 键盘事件处理需要在应用层面实现
            // OOD框架的事件系统与标准DOM事件API不同
        });
    },

    Static: {
        HasHtmlTableNode: 1,
        _CONTAINERKEY: "ITEM",
        _ITEMCONTAINER: 1,
        _ACTIVEHANDLER: ["KEY", "HOLDER"],
        _NoProp: {"conLayoutColumns": 1},
        _objectProp: {layoutData: 1},
        Appearances: {
            KEY: {
                overflow: 'hidden',
                transition: 'all 0.3s ease'
            },
            BOX: {
                position: 'absolute',
                left: 0,
                top: 0,
                'z-index': 1,
                'border': '1px solid rgba(0,0,0,0.1)',
                'border-radius': '6px',
                'box-shadow': '0 2px 4px rgba(0,0,0,0.1)',
                transition: 'all 0.3s ease'
            },
            'BOX:hover': {
                'box-shadow': '0 3px 6px rgba(0,0,0,0.15)'
            },
            CBORDER: {
                position: 'absolute',
                left: 0,
                top: 0
            },
            'CBORDER div': {
                position: 'absolute',
                display: 'block'
            },
            POOL: {
                position: 'absolute',
                left: '-100%',
                top: '-100%',
                width: 0,
                height: 0
            },
            HOLDER: {
                overflow: 'auto',
                position: 'relative',
                'border-radius': '4px',
                transition: 'background-color 0.3s ease'
            },
            TABLE: {
                'overflow': 'hidden',
                'border-collapse': 'separate',
                'border-spacing': '0',
                margin: '0',
                'border-width': '0',
                'table-layout': 'fixed',
                width: '0',
                'outline-width': '0',
                cursor: 'default',
                'max-width': 'none',
                'max-height': 'none',
                transition: 'background-color 0.3s ease'
            },
            "ITEM:empty:after": {
                'text-align': 'center',
                color: '#cccccc',
                content: 'attr(data-coord)',
                position: 'absolute',
                top: '0',
                bottom: '0',
                left: '0',
                right: '0',
                margin: 'auto',
                height: '1.5em',
                '-moz-user-select': ood.browser.gek ? '-moz-none' : null,
                '-khtml-user-select': ood.browser.kde ? 'none' : null,
                '-webkit-user-select': ood.browser.kde ? 'none' : null,
                '-o-user-select': ood.browser.opr ? 'none' : null,
                '-ms-user-select': (ood.browser.ie || ood.browser.newie) ? 'none' : null,
                'user-select': 'none',
                'touch-action': 'none',
                transition: 'color 0.3s ease'
            },
            // {{ for read/write mode (layoutcell)
            "ITEM.layoutcell": {
                "border-right": "1px solid rgba(0,0,0,0.1)",
                "border-bottom": "1px solid rgba(0,0,0,0.1)",
                "border-left": "none",
                "border-top": "none",
                transition: 'all 0.2s ease',
                $order: 1
            },
            "ITEM.layoutcell:hover": {
                'background-color': '#f8f9fa',
                'box-shadow': '0 1px 3px rgba(0,0,0,0.1)'
            },
            "ITEM.layoutcell.firstrow": {
                "border-top": "1px solid rgba(0,0,0,0.1)",
                $order: 2
            },
            "ITEM.layoutcell.firstcol": {
                "border-left": "1px solid rgba(0,0,0,0.1)",
                $order: 2
            },
            // "BOX.solidgridline ITEM.layoutcell": {
            //     "border-right": "1px solid #007bff",
            //     "border-bottom": "1px solid #007bff",
            //     "border-left": "none",
            //     "border-top": "none",
            //     $order: 3
            // },
            // "BOX.solidgridline ITEM.layoutcell.firstrow": {
            //     "border-top": "1px solid #007bff",
            //     $order: 4
            // },
            // "BOX.solidgridline ITEM.layoutcell.firstcol": {
            //     "border-left": "1px solid #007bff",
            //     $order: 4
            // },
            // }}

            // {{ for design mode (handsontable)
            // reset 
            "KEY ITEM": {
                "position": "relative",
                background: "transparent",
                height: '22px',
                padding: '0 4px',
                overflow: 'hidden',
                'outline-width': '0',
                'white-space': 'pre-line',
                'empty-cells': 'show',
                'line-height': '1.22',
                'text-align': 'left',
                'vertical-align': 'middle',
                'background-clip': 'padding-box',
                transition: 'background-color 0.2s ease'
            },
            "KEY ITEM:hover": {
                'background-color': 'rgba(0,123,255,0.1)'
            },
            "KEY .handsontableInput": {
                'line-height': '1.22'
            },
            "KEY .handsontable tr": {
                background: "transparent"
            },
            // reset
            "BOX.handsontable tr:first-child td, BOX.handsontable tr:first-child th": {
                "border-top": "none"
            },
            "BOX.handsontable tr:first-child > td, BOX.handsontable tr:first-child > th": {
                "border-top": "1px solid #dee2e6"
            },
            // for handsontable solid grid lines
            ".handsontable.solidgridline td": {
                "border-right": "1px solid #007bff",
                "border-bottom": "1px solid #007bff"
            },
            ".handsontable.solidgridline tr:first-child > td": {
                "border-top": "1px solid #007bff"
            },
            ".handsontable.solidgridline th:nth-child(2), .handsontable.solidgridline td:first-of-type, .handsontable.solidgridline .htNoFrame + th, .handsontable.solidgridline .htNoFrame + td": {
                "border-left": "1px solid #007bff"
            },
            ".handsontable.nogridline td": {
                "border-right": "1px solid transparent",
                "border-bottom": "1px solid transparent"
            },
            ".handsontable.nogridline tr:first-child > td": {
                "border-top": "1px solid transparent"
            },
            ".handsontable.nogridline th:nth-child(2), .handsontable.nogridline td:first-of-type, .handsontable.nogridline .htNoFrame + th, .handsontable.nogridline .htNoFrame + td": {
                "border-left": "1px solid transparent"
            },
            // }}

            // 组件类名
            'BOX': {
                className: 'formlayout-box'
            },
            'HOLDER': {
                className: 'formlayout-holder'
            },
            'TABLE': {
                className: 'formlayout-table'
            },
            'ITEM.layoutcell': {
                className: 'formlayout-item'
            },

            // 移动端样式
            'formlayout-mobile TABLE': {
                'font-size': '0.9em'
            },
            'formlayout-mobile ITEM.layoutcell': {
                'padding': '4px 6px',
                'font-size': '0.9em'
            },

            // 小屏幕样式
            'formlayout-tiny ITEM.layoutcell': {
                'padding': '2px 4px',
                'font-size': '0.8em'
            }
        },
        Templates: {
            tagName: 'div',
            className: '{_className}',
            style: '{_style}',
            BOX: {
                tagName: 'div'
            },
            POOL: {
                tagName: 'div'
            }
        },
        Behaviors: {
            DroppableKeys: ['ITEM'],
            PanelKeys: ['ITEM'],
            HotKeyAllowed: false,
            ITEM: {}
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
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

            expression: {
                ini: '',
                action: function () {
                },
                caption: ood.getResText("DataModel.expression") || "表达式"
            },
            tabindex: null,
            defaultFocus: null,
            disableClickEffect: null,
            disableHoverEffect: null,
            disableTips: null,
            disabled: null,
            renderer: null,
            selectable: null,
            tips: null,
            autoTips: null,
            overflow: null,
            items: {
                hidden: true,
                ini: [],
                caption: ood.getResText("DataModel.item") || "项目"
            },
            listKey: null,
            dragSortable: null,
            mode: {
                ini: 'write',
                listbox: ['design', 'write', 'read'],
                get: function () {
                    return this.$inDesign ? 'design' : (this.properties.mode || 'read');
                },
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.mode") || "模式"
            },
            lineSpacing: {
                ini: 10,
                action: function (value) {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.rowGap") || "行间距"
            },
            width: {
                $spaceunit: 1,
                ini: '30em',
                caption: ood.getResText("DataModel.width") || "宽度"
            },
            height: {
                $spaceunit: 1,
                ini: '25em',
                caption: ood.getResText("DataModel.height") || "高度"
            },
            solidGridlines: {
                ini: true,
                action: function (value) {
                    var cls = value ? "solidgridline" : this.boxing()._isDesignMode() ? "" : "nogridline",
                        node = this.getSubNode('BOX');
                    node.removeClass("solidgridline nogridline");
                    if (cls) node.addClass(cls);
                },
                caption: ood.getResText("DataModel.solidGrid") || "实线网格"
            },
            stretchHeight: {
                ini: "none",
                listbox: ["none", "last", "all"],
                action: function () {
                    if (this.boxing()._isDesignMode()) {
                        this.box._updateSetting(this, 'stretchHeight');
                        var size = this.getRoot().cssSize();
                        this.box._resizeTable(this, this.getSubNode('BOX').cssSize(), false, true);
                    } else {
                        this.box._resizeTable(this, this.getSubNode('BOX').cssSize(), false, true);
                    }
                },
                caption: ood.getResText("DataModel.stretchHeight") || "拉伸高度"
            },
            stretchH: {
                ini: "all",
                listbox: ["all", "none", "last"],
                action: function () {
                    if (this.boxing()._isDesignMode()) {
                        this.box._updateSetting(this, 'stretchH');
                        this.box._resizeTable(this, this.getSubNode('BOX').cssSize(), false, true);
                    } else {
                        this.box._resizeTable(this, this.getSubNode('BOX').cssSize(), false, true);
                    }
                },
                caption: ood.getResText("DataModel.horizontalStretch") || "水平拉伸"
            },
            rowHeaderWidth: {
                ini: 25,
                action: function () {
                    if (this.boxing()._isDesignMode()) {
                        this.box._updateSetting(this, 'rowHeaderWidth');
                        var size = this.getRoot().cssSize();
                        ood.UI.$tryResize(this, size.width, size.height);
                    }
                },
                caption: ood.getResText("DataModel.rowTitleWidth") || "行标题宽度"
            },
            columnHeaderHeight: {
                ini: 25,
                action: function () {
                    if (this.boxing()._isDesignMode()) {
                        this.box._updateSetting(this, 'columnHeaderHeight');
                        var size = this.getRoot().cssSize();
                        ood.UI.$tryResize(this, size.width, size.height);
                    }
                },
                caption: ood.getResText("DataModel.colHeaderHeight") || "列标题高度"
            },
            floatHandler: {
                ini: true,
                action: function () {
                    if (this.boxing()._isDesignMode()) {
                        var size = this.getRoot().cssSize();
                        ood.UI.$tryResize(this, size.width, size.height, true);
                    }
                },
                caption: ood.getResText("DataModel.floatHandling") || "浮动处理"
            },
            defaultRowSize: {
                ini: 5,
                caption: ood.getResText("DataModel.defaultRows") || "默认行数"
            },
            defaultColumnSize: {
                ini: 5,
                caption: ood.getResText("DataModel.defaultCols") || "默认列数"
            },
            defaultRowHeight: {
                ini: 50,
                caption: ood.getResText("DataModel.defaultRowHeight") || "默认行高"
            },
            defaultColWidth: {
                ini: 120,
                caption: ood.getResText("DataModel.defaultColWidth") || "默认列宽"
            },

            // don't use handsometable's cell className - buggy (when moving row/column)
            // rows:5, cols:5, rowSetting:{'3':{}}, colSetting:{"B":{}}, cells:{A3:{type:"",value:"",,style:"",border:""}}, merged:[]
            layoutData: {
                ini: {},
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.layoutData") || "布局数据"
            },

            // if use handsontable 6.22 (MIT license) as renderer
            rendererCDNJS: {
                ini: "/plugins/formlayout/handsontable.full.min.js",
                caption: ood.getResText("DataModel.rendererJS") || "渲染器JS"
            },
            rendererCDNCSS: {
                ini: "/plugins/formlayout/handsontable.full.min.css",
                caption: ood.getResText("DataModel.rendererCSS") || "渲染器CSS"
            }
        },
        RenderTrigger: function () {
            var prf = this, prop = prf.properties, cls = prf.box;
            if (prf.boxing()._isDesignMode()) {
                if (window.Handsontable) cls._renderAsHandsontable(prf);
                else {
                    // prf.boxing().busy(false, "Loading table ...");
                    var cssId = "ood.UI.HTable:Handsontable";
                    ood.CSS.includeLink(prop.rendererCDNCSS, cssId);
                    ood.include("Handsontable", prop.rendererCDNJS, function () {
                        if (ood(cssId).get(0)) {
                            //prf.boxing().free();
                            cls._renderAsHandsontable(prf);
                            var size = prf.getRoot().cssSize();
                            ood.UI.$tryResize(prf, size.width, size.height);
                        } else {
                            ood.Thread.repeat(function () {
                                if (ood(cssId).get(0)) {
                                    //prf.boxing().free();
                                    cls._renderAsHandsontable(prf);
                                    var size = prf.getRoot().cssSize();
                                    ood.UI.$tryResize(prf, size.width, size.height);

                                    return false;
                                }
                            }, 200);
                        }
                    }, null, false, {cache: true});
                }
            } else {
                cls._renderAsH5Table(prf);
            }
            // will be called in refresh()
            prf.$handleCustomVars = function (d) {
                if (!d) {
                    ood.arr.each(prf.children, function (c) {
                        delete c[0]._attached2cell;
                        delete c[0]._autoexpand;
                    });
                }
            }
            prf.boxing().setSolidGridlines(prop.solidGridlines, true);

            // 现代化功能初始化
            var self = prf;
            // ood.asyRun(function(){
            //     self.boxing().FormLayoutTrigger();
            // });
        },


        EventHandlers: {
            onShowTips: null,
            onGetCellData: function (cellCoord, cellObj, cellChild) {
            }
        },
        _getHeaderOffset: function (prf) {
            var prop = prf.properties, offset = {left: 0, top: 0};
            if (prop.floatHandler && prf.boxing()._isDesignMode()) {
                offset.left = prop.rowHeaderWidth;
                offset.top = prop.columnHeaderHeight + 1;
            }
            return offset;
        },
        _layoutChanged: function (prf, force) {
            if (force || prf._$tableInited)
                ood.resetRun(prf.getUid("layoutchanged"), function () {
                    //console.log("onLayoutChanged");
                    if (prf.properties) {
                        var oData = prf.properties.layoutData;
                        prf.properties.layoutData = prf.box._getLayoutData(prf);
                        if (prf.$onLayoutChanged) prf.$onLayoutChanged(prf, oData, prf.properties.layoutData);
                    }

                });
        },
        _getLayoutData: function (prf) {
            var prop = prf.properties,
                cells = {}, borders, rowSetting = {}, colSetting = {}, merged = [],
                layoutData = {},
                data, rows, cols, t, p, s, tmp;

            // handsontable to ood
            if (t = prf.$htable) {
                // rows:5, cols:5, merged:[]
                layoutData.rows = t.countRows();
                layoutData.cols = t.countCols();
                merged = ood.copy(ood.get(t.getPlugin("mergeCells"), ["mergedCellsCollection", "mergedCells"]));
                if (!ood.isEmpty(merged)) layoutData.merged = merged;

                s = t.getSettings();
                // rowSetting:{'3':{}}
                p = t.getPlugin("ManualRowResize");
                for (var i = 0, l = layoutData.rows, h; i < l; i++) {
                    var row = t.toPhysicalRow(i);
                    if (p.manualRowHeights && p.manualRowHeights[row]) ood.set(rowSetting, [i + 1, 'manualHeight'], p.manualRowHeights[row]);
                    if (tmp = ood.isArr(s.rowHeights) ? s.rowHeights[row] : s.rowHeights) ood.set(rowSetting, [i + 1, 'height'], tmp);
                }
                if (!ood.isEmpty(rowSetting)) layoutData.rowSetting = rowSetting;

                // colSetting:{"B":{}}
                p = t.getPlugin("ManualColumnResize");
                for (var i = 0, l = layoutData.cols, w; i < l; i++) {
                    var col = t.toPhysicalColumn(i);
                    if (p.manualColumnWidths && p.manualColumnWidths[col]) ood.set(colSetting, [ood.ExcelFormula.toColumnChr(i + 1), 'manualWidth'], p.manualColumnWidths[col]);
//                    if(tmp = ood.isArr(s.colWidths)?s.colWidths[col]:s.colWidths) ood.set(colSetting, [ood.ExcelFormula.toColumnChr(i+1), 'width'], tmp);
                }
                if (!ood.isEmpty(colSetting)) layoutData.colSetting = colSetting;

                // cells:{A3:{type:"",value:"",style:"",border:""}
                data = t.getData();
                // cells:{A3:{value:"v"}
                for (var i = 0, l = data.length; i < l; i++) {
                    var row = data[i];
                    for (var m = 0, n = row.length; m < n; m++) {
                        // ignore null/undefined/""
                        if (ood.isSet(data[i][m]) && data[i][m] !== "") {
                            ood.set(cells, [ood.ExcelFormula.toCellId(m, i), "value"], data[i][m]);
                        }
                    }
                }
                // cells:{A3:{style:{}}
                for (var i = 0, l = layoutData.rows; i < l; i++) {
                    var row = t.toPhysicalRow(i);
                    var rowMetas = t.getCellMetaAtRow(row);
                    if (rowMetas && rowMetas.length) {
                        for (var m = 0, n = rowMetas.length; m < n; m++) {
                            var col = t.toPhysicalColumn(m);
                            // align settings
                            // don't use className - buggy
//                        if(rowMetas[col].className)
//                            ood.set(cells, [ood.ExcelFormula.toCellId(m,i), "className"], ood.str.trim(rowMetas[col].className));
                            // style: ignore empty {}
                            if (!ood.isEmpty(rowMetas[col].style))
                                ood.set(cells, [ood.ExcelFormula.toCellId(m, i), "style"], ood.copy(rowMetas[col].style));
                        }
                    }

                }
                if (!ood.isEmpty(cells)) layoutData.cells = cells;

                var cbPlugin = t.getPlugin('customBorders');
                if ((borders = cbPlugin.getBorders()).length) {
                    layoutData.customBorders = ood.clone(borders, function (h, i) {
                        return i != 'id' && i != 'border'
                    });
                }
            }
            return layoutData;
        },
        _renderAsH5Table: function (prf) {
            var boxNode = prf.getSubNode("BOX"),
                elem = boxNode.get(0),
                prop = prf.properties,
                layoutData = prop.layoutData,
                rowSize = layoutData.rows || prop.defaultRowSize,
                colSize = layoutData.cols || prop.defaultColumnSize,
                childrenMap = {}, t, merged = {}, merged2 = {},
                getCellData = function (childrenMap, itemId) {
                    var data = prf.onGetCellData && prf.boxing().onGetCellData(prf, itemId, ood.get(layoutData, ["cells", itemId]), childrenMap[itemId]);
                    if (!ood.isSet(data)) {
                        if (!childrenMap[itemId]) {
                            data = ood.get(layoutData, ["cells", itemId, "value"]);
                        } else {
                            var childPrf = childrenMap[itemId], ins = childPrf && childPrf.boxing();
                            if (childPrf.key == 'ood.UI.RichEditor' || childPrf.key == 'ood.UI.Image' || childPrf.key == 'ood.UI.Label') {
                                data = '';
                            } else if (childPrf.key == 'ood.UI.CheckBox' || childPrf.key == 'ood.UI.SCheckBox') {
                                data = '<input type="checkbox" disabled tabindex="-1"  onclick="javascript:return false;"'
                                    + (ins.getUIValue() ? "checked" : "")
                                    + '>';// + ins.getCaption();
                            } else {
                                data = ins.getShowValue ? ins.getShowValue() :
                                    ins.getValue ? ('$UIvalue' in childPrf.properties ? ins.getUIValue() : ins.getValue()) :
                                        ins.getCaption ? ins.getCaption() :
                                            ins.getHtml ? ins.getHtml() :
                                                ins.getLabel ? ins.getLabel() :
                                                    '';
                            }
                        }
                    }
                    return ood.isSet(data) ? data : "";
                },
                cellProp, subSerialId, item, itemId, domId, styles, tpl = [];

            ood.arr.each(prf.children, function (v) {
                childrenMap[v[1]] = v[0];
            });
            ood.arr.each(layoutData.merged, function (v) {
                merged[v.row + ":" + v.col] = (v.rowspan > 1 ? (" rowspan=" + v.rowspan) : "") + (v.colspan ? (" colspan = " + v.colspan) : "") + " ";
                for (var i = 0, l = v.rowspan; i < l; i++) {
                    for (var j = 0, k = v.colspan; j < k; j++) {
                        if (i === 0 && j === 0) continue;
                        merged2[(v.row + i) + ":" + (v.col + j)] = 1;
                    }
                }
            });

            tpl.push("<div id='" + prf.key + "-HOLDER:" + prf.serialId + ":" + "' class='" + prf.getClass("HOLDER")
                + "' style='width:" + prop.width + "; height:" + prop.height
                + "' >");
            tpl.push("<table id='" + prf.key + "-TABLE:" + prf.serialId + ":" + "' class='" + prf.getClass("TABLE") + "'"
                + (prop.stretchH != 'none' ? (" style='width:" + prop.width + ";'") : "")
                + ">");
            // colgroup
            var colWidths = prf.box._getColWidths(prf, prf.$px(prop.width));
            tpl.push("<colgroup><col style='width:0;border:0;margin:0;padding:0;'></col>");
            for (var col = 0, n = colSize; col < n; col++) {
                tpl.push("<col rowid=" + row + " style='width:" + colWidths[col] + ";'></col>");
            }
            tpl.push("</colgroup>");
            // tbody
            tpl.push("<tbody>");

            for (var row = 0, l = rowSize; row < l; row++) {
                tpl.push("<tr rowid=" + row + "><th style='width:0;border:0;margin:0;padding:0;");
                if (t = ood.get(layoutData, ['rowSetting', row + 1, 'height']) || ood.get(layoutData, ['rowSetting', row + 1, 'manualHeight']) || prop.defaultRowHeight) {
                    tpl.push("height:" + (t - (row === 0 ? 1/*2*/ : 1)) + "px;");
                    //tpl.push("height:" + rowHeights[row] + "px;");
                }
                tpl.push("'></th>");
                for (var col = 0, n = colSize; col < n; col++) {
                    subSerialId = prf.pickSubId('items');
                    itemId = ood.ExcelFormula.toCellId(col, row);
                    item = {
                        _serialId: subSerialId,
                        col: col,
                        row: row,
                        id: itemId,
                        value: getCellData(childrenMap, itemId),
                        style: ood.get(layoutData, ["cells", itemId, "style"]) || {}
                    };
                    prf.ItemIdMapSubSerialId[itemId] = subSerialId;
                    prf.SubSerialIdMapItem[subSerialId] = item;

                    domId = prf.key + "-ITEM:" + prf.serialId + ":" + subSerialId;
                    styles = [];
                    ood.each(item.style, function (v, k) {
                        styles.push(k.replace(/[A-Z]/g, function (a) {
                            return '-' + a.toLowerCase()
                        }) + ":" + v);
                    });
                    // layoutData.merged
                    if (!merged2[row + ":" + col]) {
                        tpl.push("<td rowid=" + row + " id='" + domId + "' class='layoutcell " + (row === 0 ? 'firstrow ' : '') + (col === 0 ? 'firstcol ' : '') + prf.getClass("ITEM") + "' style='" + styles.join(";") + "' " + (merged[row + ":" + col] || "") + ">");
                        tpl.push(item.value);
                        tpl.push("</td>");
                    }
                }
                tpl.push("</tr>");
            }
            tpl.push("</tbody></table>");
            tpl.push("<div id='" + prf.key + "-CBORDER:" + prf.serialId + ":" + "' class='" + prf.getClass("CBORDER") + "' ></div>");
            tpl.push("</div>");

            elem.innerHTML = tpl.join("");
            ood.UI.$addEventsHandler(prf, elem, false);

            // layoutData.customBorders
            prf.box._setCustomBorders(prf);

            // lazy append
            var arr = [];
            ood.each(prf.children, function (v) {
                arr.push(v);
            });
            prf.children = [];
            ood.arr.each(arr, function (v) {
                prf.boxing().append(v[0], v[1]);
            });
        },
        _renderAsHandsontable: function (prf) {
            if (!prf || !prf.box) return;
            var onLayoutChanged = function (prf, force) {
                prf.box._layoutChanged(prf, force);
            };
            var boxNode = prf.getSubNode("BOX"),
                elem = boxNode.get(0),
                htable,
                prop = prf.properties,
                layoutData = prop.layoutData,
                designMode = true,
                fixedSet = {
                    // "fix" some functions for handsontable
                    autoWrapRow: true,
                    renderAllRows: true,
                    persistentState: false,

                    // "readonly" handsontable
                    readOnly: !designMode,
                    readOnlyCellClassName: "no",
                    comments: !designMode,
                    disableVisualSelection: !designMode,
                    enterBeginsEditing: designMode,
                    manualRowMove: designMode,
                    manualColumnMove: designMode,
                    contextMenu: designMode,
                    copyable: designMode,
                    copyPaste: designMode,
                    beforeOnCellMouseDown: !designMode ? null : function (e, c) {
                        // fire event
                        if (c.row === -1 && c.col === -1) {
                            e.stopImmediatePropagation();
                        }
                    },
                    afterOnCellMouseUp: !designMode ? null : function (e, c) {
                        // fire event
                        if (c.row === -1 && c.col === -1) {
                            this.deselectCell();
                            prf.getRoot().onClick(true);
                            e.stopImmediatePropagation();
                        }
                    },

                    /* cell render*/
                    // for ood dom id & event handler
                    beforeRenderer: function (TD, row, col, vprop, value, cellprop) {
                        var subSerialId = prf.pickSubId('items'),
                            itemId = ood.ExcelFormula.toCellId(col, row);
                        // memory map
                        cellprop.oid = cellprop.id;
                        cellprop.id = itemId;
                        if (cellprop.oid && cellprop.oid != cellprop.id) {
                            prf.$cellIdChangedMap[cellprop.oid] = cellprop.id;
                        }
                        cellprop._serialId = subSerialId;
                        prf.ItemIdMapSubSerialId[itemId] = subSerialId;
                        prf.SubSerialIdMapItem[subSerialId] = cellprop;
                        // customized styles
                        // first time, set cellprop.style from prop.layoutData
                        if (!cellprop.style) {
                            cellprop.style = ood.get(prop.layoutData, ["cells", ood.ExcelFormula.toCellId(col, row), "style"]) || {};
                        }
                        // dom
                        TD.id = prf.key + "-ITEM:" + prf.serialId + ":" + subSerialId;
                        ood.UI.$addEventsHandler(prf, TD, true);
                        for (var i in cellprop.style) TD.style[i] = cellprop.style[i];
                        // align class
//                        if(!cellprop.className){
//                            cellprop.className = ood.get(prop.layoutData, ["cells", ood.ExcelFormula.toCellId(col,row),"className"]) || "";
//                        }
                        TD.className = (TD.className || "") + prf.getClass("ITEM");
                        if (designMode)
                            TD.setAttribute('data-coord', itemId);
                        if (cellprop._child_autoexpandH) {
                            TD.style.height = cellprop._child_autoexpandH + "px";
                        } else {
                            TD.style.height = "";
                        }
                    },
                    /* cell renderer
                    renderer : function(instance, TD, row, col, vprop, value, cellprop){
                    },
                    */
                    /* table render*/
                    beforeRender: function (flag) {
                        // **: updateSetting will re-render all table elements
                        // we have to reset memory map, and keep children here
                        if (prf._$tableInited) {
                            prf.$cellIdChangedMap = {};
                            //*** save children first
                            var pool = prf.getSubNode("POOL").get(0), arr = [];
                            ood.arr.each(prf.children, function (v) {
                                if (v[0] && v[0].rendered) {
                                    pool.appendChild(v[0].getRootNode());
                                    arr.push(v);
                                }
                            });
                            //keep children, prevent to be destroyed
                            if (arr.length) {
                                prf._pool_children = arr;
                            }

                            // reset memory map
                            prf.children = [];
                            for (var i in prf.SubSerialIdMapItem)
                                prf.reclaimSubId(i, "items");
                            prf.ItemIdMapSubSerialId = {};
                            prf.SubSerialIdMapItem = {};
                        }
                    },
                    afterInit: function () {
                        prf._$tableInited = 1;
                    },
                    afterRender: function (isForced) {
                        //console.log('afterRender');
                        ood.tryF(prf.$onrender, [], prf);

//                        onLayoutChanged(prf);

                        // Set id for important nodes, for getting profile from dom id
                        var node = prf.getSubNode("BOX");
                        node = node.first();
                        node.id(prf.key + "-MASTER:" + prf.serialId + ":");
                        node = node.first();
                        node.id(prf.key + "-HOLDER:" + prf.serialId + ":");
                        node = node.first();
                        node.id(prf.key + "-HIDER:" + prf.serialId + ":");
                        node = node.first();
                        node.id(prf.key + "-SPREADER:" + prf.serialId + ":");

                        if (prf._$tableInited) {
                            var map = prf.$cellIdChangedMap;

                            //*** restore children
                            if (prf._pool_children) {
                                ood.arr.each(prf._pool_children, function (v) {
                                    delete v[0].$dockParent;
                                    if (v[0].$domId) {
                                        prf.boxing().append(v[0], map[v[1]] || v[1]);
                                    }

                                });
                                delete prf._pool_children;
                            }
                        } else {
                            //
                            // lazy append
                            var arr = [];
                            ood.each(prf.children, function (v) {
                                if (v[0].$domId) {
                                    arr.push(v);
                                }
                            });
                            prf.children = [];

                            ood.arr.each(arr, function (v) {
                                if (v[0].$xid) {
                                    prf.boxing().append(v[0], v[1]);
                                }

                            });

                            prf.getRoot().onMouseup(function (p, e) {
                                prf.$lastMousePos = ood.Event.getPos(e);
                            });
                        }
                    },

                    afterSelection: function () {
                        if (prop.floatHandler)
                            prf.getRoot().css('overflow', 'visible');
                    },
                    outsideClickDeselects: function (node) {
                        //for lang span, or inner renderer
                        while ((
                                (!node.id)
                                || node.id == ood.$localeDomId
                                || node.tagName == 'tspan'
                            )
                            && node.parentNode !== document && node.parentNode !== window
                            ) node = node.parentNode;
                        var p = ood.UIProfile.getFromDom(node);
                        if (p && prf.$popmenu) {
                            var r = prf.$popmenu.get(0);
                            if (p == r || p.$parentPopMenu == r || p.$popGrp == r.$popGrp) {
                                return false;
                            }
                        }

                        prf.getRoot().css('overflow', '');
                        return true;
                    },
                    afterContextMenuShow: !designMode ? null : function (context) {
                        if (prf.$onPopMenu) prf.$popmenu = prf.$onPopMenu(prf, prf.$popmenu, context.menu);
                        // don't use dft menu
                        context.menu.container.style.display = 'none';
                    },

                    afterUpdateSettings: function () {
                        // only resize use this
                        onLayoutChanged(prf);
                    },
                    afterRedo: function () {
                        onLayoutChanged(prf);
                    },
                    afterUndo: function () {
                        onLayoutChanged(prf);
                    },
                    afterChange: function (change, source) {
                        if (source != 'loadData' && source != 'populateFromArray')
                            onLayoutChanged(prf);
                    },
                    beforeCellAlignment: function () {
                        onLayoutChanged(prf);
                    },
                    afterMergeCells: function (a, b, auto) {
                        if (!auto)
                            onLayoutChanged(prf);
                    },
                    afterUnmergeCells: function (a, b, auto) {
                        if (!auto)
                            onLayoutChanged(prf);
                    },

                    // reset autoexpand
                    afterRowResize: function (row, size, dblclick) {
                        onLayoutChanged(prf);
                        var cells = this.getCellMetaAtRow(row);
                        for (var i = 0, l = cells.length; i < l; i++) {
                            var target = prf.boxing().getChildren(cells[i].id);
                            if (target['ood.UI.Input']
                                && target.getMultiLines && target.getMultiLines()
                                && target.setAutoexpand
                            ) {
                                target.get(0)._autoexpand = (size - 1) + "px";
                            }
                        }
                    },

                    afterColumnResize: function () {
                        onLayoutChanged(prf);
                    },
                    afterColumnSort: function () {
                        onLayoutChanged(prf);
                    },
                    afterRowMove: function (rows, target) {
                        onLayoutChanged(prf);
                    },
                    afterColumnMove: function () {
                        onLayoutChanged(prf);
                    },
                    // for fix ManualColumnResize and ManualRowResize
                    afterCreateCol: function (index, amount) {
                        onLayoutChanged(prf);

                        // patch for ManualColumnResize
                        var p = this.getPlugin("ManualColumnResize");
                        // new cols
                        var arr = [];
                        for (var i = 0; i < amount; i++) arr.push(prop.defaultColWidth);
                        // ensure length
                        if (!p.manualColumnWidths) p.manualColumnWidths = [];
                        for (var i = 0; i < index; i++) p.manualColumnWidths[i] = p.manualColumnWidths[i] || (void 0);
                        // insert
                        p.manualColumnWidths.splice.apply(p.manualColumnWidths, [index, 0].concat(arr));

                        this.deselectCell();
                        var ns = this;
                        ood.asyRun(function () {
                            ns.selectColumns(index);
                        });
                    },
                    afterCreateRow: function (index, amount) {
                        onLayoutChanged(prf);
                        // patch for ManualRowResize
                        var p = this.getPlugin("ManualRowResize");
                        // new row
                        var arr = [];
                        for (var i = 0; i < amount; i++) arr.push(prop.defaultRowHeight);
                        // ensure length
                        if (!p.manualRowHeights) p.manualRowHeights = [];
                        for (var i = 0; i < index; i++) p.manualRowHeights[i] = p.manualRowHeights[i] || (void 0);
                        // insert
                        p.manualRowHeights.splice.apply(p.manualRowHeights, [index, 0].concat(arr));

                        this.deselectCell();
                        var ns = this;
                        ood.asyRun(function () {
                            ns.selectRows(index);
                        });
                    },
                    afterRemoveCol: function (index, amount) {
                        onLayoutChanged(prf);
                        var p = this.getPlugin("ManualColumnResize");
                        if (p.manualColumnWidths) {
                            p.manualColumnWidths.splice(index, amount);
                        }
                        this.deselectCell();
                    },
                    afterRemoveRow: function (index, amount) {
                        onLayoutChanged(prf);
                        var p = this.getPlugin("ManualRowResize");
                        if (p.manualRowHeights) {
                            p.manualRowHeights.splice(index, amount);
                        }
                        this.deselectCell();
                    }
                },
                settings = {}, t;

            var offset = prf.box._getHeaderOffset(prf);
            // size
            settings.height = prf.$px(prop.height) + offset.top;
            settings.width = prf.$px(prop.width) + offset.left;
            // stretch
            settings.stretchHeight = (t = prop.stretchHeight) == "last" ? "last" : t == "all" ? "all" : "none";
            settings.stretchH = (t = prop.stretchH) == "last" ? "last" : t == "all" ? "all" : "none";
            // dft widht/height
            settings.rowHeaderWidth = prop.rowHeaderWidth;
            settings.columnHeaderHeight = prop.columnHeaderHeight;
            settings.defaultColumnWidth = prop.defaultColWidth;
            // show header?
            settings.rowHeaders = designMode;
            settings.colHeaders = designMode;


            // merged info
            if (layoutData.merged) {
                settings.mergeCells = layoutData.merged;
            } else {
                settings.mergeCells = designMode;
            }

            // data, manualRowResize, minRowHeights, manualColumnResize, colWidths, cellMetas
            // if there's table data
            if (layoutData.cols) {
                // cell data
                var minRowHeights = [], colWidths = [], manualRowResize = [], manualColumnResize = [], data = [], row;
                // manualRowResize (start from "1")
                ood.each(layoutData.rowSetting, function (v, k) {
                    if (ood.isSet(v.manualHeight || v)) manualRowResize[parseInt(k, 10) - 1] = parseInt(v.manualHeight || v, 10);
                    if (ood.isSet(v.height || v)) minRowHeights[parseInt(k, 10) - 1] = parseInt(v.height || v, 10);
                });
                // manualColumnResize (start from "A"=>"1")
                ood.each(layoutData.colSetting, function (v, k) {
                    k = ood.ExcelFormula.toColumnNum(k);
                    if (ood.isSet(v.manualWidth || v)) manualColumnResize[k - 1] = parseInt(v.manualWidth || v, 10);
                    // if(ood.isSet(v.width||v))colWidths[k - 1]=parseInt(v.width||v,10);
                });
                // init data
                for (var i = 0, l = layoutData.rows || prop.defaultRowSize; i < l; i++) {
                    data.push(row = []);
                    for (var m = 0, n = layoutData.cols || prop.defaultColumnSize; m < n; m++) {
                        row.push(null);
                    }
                }
                // fill data
                ood.each(layoutData.cells, function (cell, id) {
                    var coord = ood.ExcelFormula.toCoordinate(id);
                    data[coord.row][coord.col] = ood.isSet(cell.value) ? cell.value : null;
                });

                // set manualRowResize, manualColumnResize and data
                for (var i = 0; i < layoutData.rows; i++) {
                    manualRowResize[i] = manualRowResize[i] || prop.defaultRowHeight;
                }
                settings.manualRowResize = manualRowResize;
                settings.manualColumnResize = !ood.isEmpty(manualColumnResize) ? manualColumnResize : designMode;

                if (!ood.isEmpty(minRowHeights)) settings.rowHeights = minRowHeights;
                // don't use colWidths
                //if(!ood.isEmpty(colWidths))settings.colWidths = colWidths;

                settings.data = data;

                if (layoutData.customBorders)
                    settings.customBorders = layoutData.customBorders;
            } else {
                settings.manualColumnResize = designMode;
                var manualRowResize = [];
                for (var i = 0; i < prop.defaultRowSize; i++) {
                    manualRowResize[i] = prop.defaultRowHeight;
                }
                settings.manualRowResize = manualRowResize;
            }
            prf.$htable = htable = new Handsontable(elem, ood.merge(settings, fixedSet, 'all'));

            if (!layoutData.cols) {
                // reset layoutData
                prf.properties.layoutData = prf.box._getLayoutData(prf);
            }

            // set before destroy function
            (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["destroyhtable"] = function () {
                var t;
                if (t = this.$htable) {
                    // must purge lazy-bound node here
                    var node = this.getSubNode("BOX").get(0);
                    if (node)
                        ood.$purgeChildren(node);

                    if (!t.isDestroyed) {
                        Handsontable.hooks.destroy(t);
                        t.destroy();
                    }
                    delete this.$htable;
                }
                if (t = this.$popmenu) {
                    t.destroy();
                }
            }
        },

        _getRowHeights: function (prf, tableHeight, ispx) {
            var prop = prf.properties, layoutData = prop.layoutData, t,
                rowSize = layoutData.rows || prop.defaultRowSize, rowsize = [],
                reCalculated = [], rowHeights = [], fix = 0, count = 0, per, off, rc = 0, bW = 0,
                stretchHeight = prop.stretchHeight || 'all',
                lastoff = prop.defaultRowHeight;

            for (var row = 0, n = rowSize; row < n; row++) {
                if (t = ood.get(layoutData, ['rowSetting', row + 1, 'manualHeight'])) {
                    if (row < rowSize - 1) {
                        fix += parseInt(t);
                    }
                }
            }
            lastoff = tableHeight - fix;
            if (stretchHeight == "all") {
                for (var row = 0, n = rowSize; row < n; row++) {
                    if (t = ood.get(layoutData, ['rowSetting', row + 1, 'manualHeight'])) {
                        fix += parseInt(t);
                        reCalculated.push(t);
                    } else {
                        count++;
                        reCalculated.push(null);
                    }
                }
                per = (tableHeight - fix) / count;
                off = per - Math.round(per);
            }
            for (var row = 0, n = rowSize; row < n; row++) {
                var aW, px = !ispx ? "px" : "";
                rc++;
                t = ood.get(layoutData, ['rowSetting', row + 1, 'manualHeight']) || (prop.defaultRowHeight);
                switch (stretchHeight) {
                    case 'all':
                        aW = reCalculated[row] === null ? Math.max(prop.defaultRowHeight, (Math.round(per))) : reCalculated[row];
                        bW += aW;
                        rowHeights.push((row == rowSize - 1 ? Math.round(tableHeight - bW + aW) : aW) + px);
                        break;
                    case 'last':
                        rowHeights.push(row == rowSize - 1 ? lastoff + px : (t + px));
                        break;
                    default:
                        rowHeights.push(t + px);
                }
            }
            return rowHeights;
        },
        _getColWidths: function (prf, tableWidth) {
            var prop = prf.properties, layoutData = prop.layoutData, t,
                colSize = layoutData.cols || prop.defaultColumnSize,
                stretchH = prop.stretchH || 'all',
                reCalculated = [], colWidths = [], fix = 0, count = 0, per, off, rc = 0, bW = 0;

            if (colSize.length < 3) {
                return colSize;
            }

            if (stretchH == "all") {
                for (var col = 0, n = colSize; col < n; col++) {
                    var chr = ood.ExcelFormula.toColumnChr(col + 1);
                    if (t = ood.get(layoutData, ['colSetting', chr, 'manualWidth'])) {
                        fix += parseInt(t);
                        reCalculated.push(t);
                    } else {
                        count++;
                        reCalculated.push(null);
                    }
                }
                per = (tableWidth - fix) / count;
                off = per - Math.round(per);
            }
            var allW = 0;
            for (var col = 0, n = colSize; col < n; col++) {
                var chr = ood.ExcelFormula.toColumnChr(col + 1), aW;
                rc++;
                t = ood.get(layoutData, ['colSetting', chr, 'manualWidth']) || (prop.defaultColWidth);
                width = t = parseInt(t);
                switch (stretchH) {
                    case 'all':
                        aW = reCalculated[col] === null ? Math.max(prop.defaultColWidth, (Math.round(per))) : reCalculated[col];
                        if (ood.isNumb(aW)) {
                            bW += aW;
                        } else {
                            bW += parseInt(aW);
                        }
                        width = (col == colSize - 1 ? Math.round(tableWidth - bW + aW) - 25 : aW);
                        break;
                    case 'last':
                        width = col == colSize - 1 ? tableWidth - allW - 5 : t;
                        width = width - 20;
                        break;
                    default:
                        width = t;
                }
                allW = allW + width;
                colWidths.push(width);

            }
            return colWidths;

        },
        _setCustomBorders: function (prf) {
            ood.resetRun(prf.getUid() + ":cborder", function () {
                if (!prf.renderId) return;
                var cborder = prf.getSubNode("CBORDER").get(0);
                ood.arr.each(prf.properties.layoutData.customBorders, function (conf) {
                    var itemId = ood.ExcelFormula.toCellId(conf.col, conf.row),
                        subSerialId = prf.ItemIdMapSubSerialId[itemId],
                        table = prf.getSubNode("TABLE").get(0),
                        td = prf.getSubNode("ITEM", subSerialId),
                        pos = td.offset(null, table),
                        id, div, style;
                    if (conf.top && conf.top.width) {
                        id = prf.key + "-CBT:" + prf.serialId + ":" + subSerialId;
                        div = ood.Dom.byId(id);
                        style = div && div.style;
                        if (!div) {
                            div = document.createElement("div");
                            div.id = id;
                            cborder.appendChild(div);
                        }
                        style = div.style;
                        style.backgroundColor = conf.top.color;
                        style.left = (pos.left - 1) + "px";
                        style.top = (pos.top - 1) + "px";
                        style.width = td.offsetWidth() + "px";
                        style.height = "1px";
                    }
                    if (conf.left && conf.left.width) {
                        id = prf.key + "-CBL:" + prf.serialId + ":" + subSerialId;
                        div = ood.Dom.byId(id);
                        if (!div) {
                            div = document.createElement("div");
                            div.id = id;
                            cborder.appendChild(div);
                        }
                        style = div.style;
                        style.backgroundColor = conf.left.color;
                        style.left = (pos.left - 1) + "px";
                        style.top = (pos.top - 1) + "px";
                        style.width = "1px";
                        style.height = td.offsetHeight() + "px";
                    }
                });
            });
        },
        _resizeTable: function (prf, size, force, reload) {

            if (prf.boxing()._isDesignMode()) {
                var t;
                if (t = prf.$htable) {
                    try {
                        var osize = prf.getSubNode("HOLDER").cssSize();
                        if (!force && (size.width != osize.width || size.height != osize.height) || reload) {
                            prf.getSubNode("HOLDER").cssSize(size);
                            size.manualRowResize = this._getRowHeights(prf, size.height, true);
                            size.manualColumnResize = this._getColWidths(prf, size.width);
                            size.mergeCells = ood.copy(ood.get(t.getPlugin("mergeCells"), ["mergedCellsCollection", "mergedCells"]));
                            t.updateSettings(size);
                        }

                    } catch (e) {

                    }

                }
            } else {
                prf.getSubNode("HOLDER").cssSize(size);
                var tb = prf.getSubNode("TABLE");
                var tr = prf.getSubNode("TR");

                if (tb.get(0)) {
                    if (prf.properties.stretchH && prf.properties.stretchH != 'none') {
                        var rw = size.width - (tb.offsetHeight() > size.height ? ood.Dom.getScrollBarSize() : 0);
                        tb.width(rw);
                    }

                    //if (force || !prf.properties.stretchH || prf.properties.stretchH == "all") {
                    var colWidths = this._getColWidths(prf, rw);
                    tb.first().children().each(function (node, i) {
                        // ignore the first one for th
                        if (i !== 0) {
                            var width = colWidths[i - 1];
                            if (!ood.str.endWith(width + '', 'px')) {
                                width = width + 'px';
                            }
                            node.style.width = width;
                        }
                    });
                    //   }
                    var cells = [], rowHeights = this._getRowHeights(prf, size.height);
                    if (prf.properties.stretchHeight != 'none') {
                        var rh = size.height - (tb.offsetHeight() > size.height ? ood.Dom.getScrollBarSize() : 0);
                        tb.height(rh);
                        prf.getSubNodes("ITEM", true).each(function (cell) {
                            if (cell.parentNode.attributes.rowid) {
                                var rowid = parseInt(cell.attributes.rowid.value);
                                var height = rowHeights[rowid];
                                var th = cell.parentNode.children[0];
                                th.style.height = height;
                                //cell.style.height = height;
                            }
                        });

                    }

                    // to trigger cells onsize

                    prf.getSubNodes("ITEM", true).each(function (cell) {
                        if (ood.Dom.$hasEventHandler(cell, 'onsize')) cells.push(cell);
                    });
                    if (cells.length) ood(cells).onSize(true);

                    // adjust custom borders
                    prf.box._setCustomBorders(prf);
                }
            }
        },
        _updateSetting: function (prf, opt) {
            var t = prf.$htable;
            if (typeof opt == "string") {
                var h = {};
                h[opt] = prf.properties[opt];
                opt = h;
            }
            // for merged cells
            opt.mergeCells = ood.copy(ood.get(t.getPlugin("mergeCells"), ["mergedCellsCollection", "mergedCells"]));
            if ((!opt["stretchHeight"] && !opt["stretchH"]) || (opt["stretchHeight"] == 'none' || opt["stretchH"] == 'none')) {
                t.updateSettings(opt);
            }


        },


        $beforeAppend: function (prf, target, subId) {
            if (!subId) return false;
            // only one allowed
            // if (target && target.size && target.size() == 1) {
            //     ood.arr.each(prf.children, function (v) {
            //         if (v[0] && subId == v[1] && v[0] !== target.get(0)) {
            //             v[0].boxing().destroy();
            //         }
            //     }, true);
            // }
        },
        $afterAppend: function (prf, target, subId) {
            if (!subId) return;
            // force dock for the only widget
            var lineSpacing = prf.properties.lineSpacing | 0;
            if (prf.renderId && target['ood.UI'] && target.size() == 1) {
                var item = prf.getItemByItemId(subId), inputPrf = target.get(0), iProp = inputPrf.properties;
                if (item) {
                    var cell = prf.getSubNode("ITEM", item._serialId),
                        isFormField = inputPrf.box._isFormField ? inputPrf.box._isFormField(inputPrf) : !!ood.get(inputPrf, ['properties', 'isFormField']),
                        mode = prf.boxing().getMode(),
                        show = mode != 'read' || target['ood.UI.RichEditor'] || target['RAD.expression.JavaEditor'];
                    if (isFormField && (!iProp.name || prf.ItemIdMapSubSerialId[iProp.name])) {
                        iProp.name = item.id;
                    }
                    // for form field only
                    // onsize for dom must here
                    if (cell && cell.get(0)) {
                        // if parent is re-rendered
                        if (inputPrf._cellresizeP != cell) {
                            var adjustSize = function () {
                                if (!cell.get(0)) return;
                                if (target['ood.UI.Image'] || target['ood.UI.ProgressBar'] || target['ood.UI.Button']) {
                                    return;
                                }

                                target.setPosition('absolute').setLeft(0).setTop(lineSpacing / 2).setBottom(lineSpacing / 2);
                                var height = cell.offsetHeight(), width = cell.offsetWidth();
                                // first row/col , 2 pix border

                                if (target.setWidth) target.setWidth(width - (item.col ? 1 : 2));
                                if (target.setHeight) target.setHeight(height - (item.row ? 1 : 2) - lineSpacing);
                            };
                            adjustSize();
                            cell.onSize(adjustSize);
                            inputPrf._cellresizeP = cell;
                        }
                    }

                    if (!inputPrf._attached2cell) {
                        //console.log('afterappend',subId);
                        inputPrf._attached2cell = 1;
                        // for form field only
                        // prop and autoexpand
                        if (isFormField) {
                            if (show) {
                                inputPrf.locked = 1;
                                inputPrf.boxing().setDisplay('');
                                //if (target.setLabelPos) target.setLabelPos('none').setLabelCaption('').setLabelSize('0');
                                if (target.setLabelPos) target.setLabelPos('none').setLabelSize('0');
                                if (target.setVAlign) target.setVAlign('middle');

                                if (target['ood.UI.Input']
                                    && target.getMultiLines && target.getMultiLines()
                                    && target.setAutoexpand
                                ) {
                                    // use the hidden one: _autoexpand
                                    // once: set minH from subId
                                    if (!parseFloat(inputPrf._autoexpand)) {
                                        // need set autoexpand in afterRowResize too
                                        inputPrf._autoexpand = (cell.offsetHeight() - 1) + "px";
                                        inputPrf.getSubNode("INPUT").addClass("autoexpand");
                                        inputPrf.$beforeAutoexpand = function (p, h) {
                                            h = target.getAutoexpandHeight();
                                            item._child_autoexpandH = h;
                                            if (prf.boxing()._isDesignMode()) {
                                                // ensure to trigger table render once
                                                ood.resetRun(prf.getUid("autoex"), function () {
                                                    if (prf.$htable) prf.$htable.render();
                                                });
                                            } else {
                                                cell.height(h);
                                            }
                                            // adjust custom borders
                                            if (!prf.boxing()._isDesignMode())
                                                prf.box._setCustomBorders(prf);
                                            return false;
                                        };
                                        // try to trigger aoutoexpand
                                        inputPrf.box._checkAutoexpand(inputPrf);
                                    }
                                }
                                if (mode == 'read') {
                                    if (target.setReadonly) target.setReadonly(true, true);
                                }
                            } else {
                                inputPrf.boxing().setDisplay('none');
                            }
                        }
                        inputPrf.$handleCustomVars = function (d) {
                            if (d) {
                                for (var i in d) if (d[i]) this[i] = d[i];
                            } else {
                                return {
                                    _attached2cell: this._attached2cell,
                                    _autoexpand: this._autoexpand,
                                    $beforeAutoexpand: this.$beforeAutoexpand
                                }
                            }
                        }
                    }
                }
            }
        },
        _IllegalDetect: function (pro, target, throwErr) {
            return null;
            // detect those with html table node
            var count = 0, detect = function (arr) {
                ood.arr.each(arr, function (c) {
                    c = c[0] || c;
                    if (c.box && c.box.HasHtmlTableNode) count++;
                    else detect(c.children);
                });
            };
            detect(target._nodes);
            if (count) {
                if (throwErr) throw new Error('Cant append control with HTML TABLE node into ' + pro.key);
                else return count;
            }
        },
        _onresize: function (prf, width, height) {
            var prop = prf.properties,
                // compare with px
                us = ood.$us(prf),
                adjustunit = function (v, emRate) {
                    return prf.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                root = prf.getRoot(),
                boxNode = prf.getSubNode('BOX'),
                offset = prf.box._getHeaderOffset(prf),
                // caculate by px
                ww = width ? prf.$px(width) : width,
                hh = height ? prf.$px(height) : height,
                t;
            if (width || height) {
                // reset here
                if (width) prop.width = adjustunit(ww);
                if (height) prop.height = adjustunit(hh);

                boxNode.css({
                    marginLeft: -offset.left + "px",
                    marginTop: -offset.top + "px",
                    width: width ? (ww + offset.left + 'px') : null,
                    height: height ? (hh + offset.top + 'px') : null
                });

                ood.resetRun(prf.getUid("resize"), function () {
                    if (prf && prf.box) prf.box._resizeTable(prf, prf.getSubNode('BOX').cssSize(), false);
                });
            }
        }
    }
});