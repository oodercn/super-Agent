ood.Class("ood.UI.Panel", "ood.UI.Div", {
    Instance: {
        // 添加 iniProp 对象来存储默认值

        iniProp: {
            caption: '普通面板',
            dock: 'none',
            width: '18em',
            height: '18em',
            toggleBtn: true,
            closeBtn: true,
            refreshBtn: true
        },


        activate: function () {
            var profile = this.get(0);
            profile.getSubNode('CAPTION').focus(true);
            return this;
        },

        getAllFormValues: function (isAll) {
            var a = this.getChildren(),
                elems = ood.absValue.pack(a),
                formValue = {},
                profile = this.get(0);
            ood.arr.each(profile.children, function (o) {
                var oo = o[0].boxing(), name = oo.getProperties().name || o[0].alias;
                if (oo.Class['ood.UI.Tabs']) {
                    ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
                    //formValue[name] = oo.getAllFormValues();
                } else if (oo.Class['ood.UI.Block'] || oo.Class["ood.UI.Panel"] || oo.Class["ood.UI.Layout"]) {
                    ood.merge(formValue, oo.getAllFormValues(isAll), 'all')
                } else if (oo.getFormValues) {
                    ood.merge(formValue, oo.getFormValues(isAll), 'all')
                } else if (oo.getUIValue) {
                    formValue[name] = oo.getUIValue();
                } else if (oo.getValue) {
                    formValue[name] = oo.getValue();
                }
            });

            return formValue;
        },

        resetPanelView: function (removeChildren, destroyChildren) {
            if (!ood.isSet(removeChildren)) removeChildren = true;
            if (!ood.isSet(destroyChildren)) destroyChildren = true;
            var ins;
            return this.each(function (profile) {
                if (profile.renderId) {
                    delete profile.$ini;
                    if (removeChildren) {
                        ins = profile.boxing();
                        ins.removeChildren(true, destroyChildren);
                    }
                    if (profile.properties.toggle)
                        ins.setToggle(false);
                }
            });
        },
        iniPanelView: function () {
            return this.each(function (profile) {
                if (!profile.$ini) {
                    profile.$ini = true;
                    var p = profile.properties;
                    if (profile.onIniPanelView) profile.boxing().onIniPanelView(profile);
                    if (p.iframeAutoLoad || p.ajaxAutoLoad)
                        ood.UI.Div._applyAutoLoad(profile);
                }
            });
        },

        // Set theme - 使用CSS类切换实现主题切换
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 移除所有主题类
                root.removeClass('panel-dark panel-hc');

                // 应用选定的主题
                if (theme === 'dark') {
                    root.addClass('panel-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('panel-hc');
                }

                // 检查响应式布局
                profile.boxing()._checkResponsiveLayout();

                // 保存主题设置到本地存储
                if (typeof localStorage !== 'undefined') {
                    localStorage.setItem('panel-theme', theme);
                }
            });
        },

        // Get current theme
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('panel-theme') || 'light';
        },


        // Modern initialization trigger - 优化初始化流程
        PanelTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties;
            var self = this;

            this._initPanel();
            return this;
        },

        // 私有初始化方法
        _initPanel: function () {
            var profile = this.get(0);
            var prop = profile.properties;
            var self = this;

            // 初始化主题
            var theme = prop.theme || localStorage.getItem('panel-theme') || 'light';
            this.setTheme(theme);

            // 初始化响应式设计
            if (prop.responsive !== false) {
                // 初始检查
                this._checkResponsiveLayout();

                // 添加窗口大小变化监听
                var resizeHandler = function () {
                    self._checkResponsiveLayout();
                };
                //
                // // 使用OOD框架的事件系统
                // var eventId = 'resize.' + profile.serialId;
                // ood.Event.off(window, eventId);
                // ood.Event.on(window, eventId, resizeHandler);
                //
                // // 存储handler引用以便后续移除
                // profile._resizeHandler = resizeHandler;
            }

            // 初始化无障碍功能
            this.enhanceAccessibility();
        },

        // Toggle dark mode
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
        },

        // 检查响应式布局 - 简化版本，使用CSS类实现响应式设计
        _checkResponsiveLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width;

                // 移除所有响应式类
                root.removeClass('panel-mobile panel-tiny');

                // 应用响应式类
                if (width < 400) {
                    root.addClass('panel-tiny');
                } else if (width < 600) {
                    root.addClass('panel-mobile');
                }
            });
        },

        // Responsive layout adjustment - 保持向后兼容
        adjustLayout: function () {
            return this._checkResponsiveLayout();
        },

        // Enhance accessibility support
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var caption = profile.getSubNode('CAPTION'),
                    panel = profile.getSubNode('PANEL'),
                    buttons = ['INFO', 'OPT', 'CLOSE', 'POP', 'REFRESH', 'TOGGLE'];

                // Add ARIA attributes to panel
                caption.attr({
                    'role': 'heading',
                    'aria-level': '2',
                    'tabindex': '0'
                });

                panel.attr({
                    'role': 'region',
                    'aria-labelledby': caption.id()
                });

                // Add ARIA labels and keyboard support for buttons
                ood.arr.each(buttons, function (btnName) {
                    var btn = profile.getSubNode(btnName);
                    if (btn && !btn.isEmpty()) {
                        var label = btnName.toLowerCase();
                        var ariaLabel = {
                            'info': ood.getRes('UI.panel.showInfo'),
                            'opt': ood.getRes('UI.panel.showOptions'),
                            'close': ood.getRes('UI.panel.close'),
                            'pop': ood.getRes('UI.panel.pop'),
                            'refresh': ood.getRes('UI.panel.refresh'),
                            'toggle': ood.getRes('UI.panel.toggle')
                        }[label] || label + ' button';

                        btn.attr({
                            'role': 'button',
                            'aria-label': ariaLabel,
                            'tabindex': '0'
                        });
                    }
                });


                caption.on('keydown', function (e) {
                    if (e.keyCode === 13 || e.keyCode === 32) { // Enter or Space
                        // Toggle panel expand/collapse state
                        if (profile.properties.toggleBtn) {
                            profile.getSubNode('TOGGLE').click();
                        }
                        e.preventDefault();
                    }
                });
            });
        }
    },
    Static: {
        Templates: {
            tagName: 'div',
            style: '{_style}',
            className: '{_className}',
            BORDER: {
                tagName: 'div',
                className: 'ood-uiborder-outset ood-uiborder-box ood-uiborder-radius-big',
                TBAR: {
                    tagName: 'div',
                    className: 'ood-uibar-top',
                    BARTDL: {
                        className: 'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-tl',
                        BARTDLT: {
                            className: 'ood-uibar-tdlt'
                        }
                    },
                    BARTDM: {
                        $order: 1,
                        className: 'ood-uibar-tdm ood-uibar',
                        BARTDMT: {
                            className: 'ood-uibar-tdmt'
                        }
                    },
                    BARTDR: {
                        $order: 2,
                        className: 'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-tr',
                        BARTDRT: {
                            className: 'ood-uibar-tdrt'
                        }
                    },
                    BARCMDL: {
                        $order: 3,
                        tagName: 'div',
                        className: 'ood-uibar-cmdl',
                        style: '{_align}',
                        LTAGCMDS: {
                            tagName: 'span',
                            className: 'ood-ltag-cmds',
                            style: '{_ltagDisplay}',
                            text: "{ltagCmds}"
                        },
                        TOGGLE: {
                            $order: 1,
                            className: 'oodfont',
                            $fonticon: '{_fi_toggleCls2}',
                            style: '{toggleDisplay}',
                            $order: 0
                        },
                        ICON: {
                            $order: 2,
                            className: 'oodcon {imageClass}  {picClass}',
                            style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                            text: '{iconFontCode}'
                        },
                        CAPTION: {
                            tabindex: '{tabindex}',
                            className: "ood-title-node",
                            text: '{caption}',
                            $order: 3
                        }
                    },
                    BARCMDR: {
                        $order: 4,
                        tagName: 'div',
                        className: 'ood-uibar-cmdr',
                        RTAGCMDS: {
                            $order: 0,
                            tagName: 'span',
                            className: 'ood-rtag-cmds',
                            style: '{_rtagDisplay}',
                            text: "{rtagCmds}"
                        },
                        INFO: {
                            className: 'oodcon',
                            $fonticon: 'ood-uicmd-info',
                            style: '{infoDisplay}',
                            $order: 1
                        },
                        OPT: {
                            className: 'oodcon',
                            $fonticon: 'ood-uicmd-opt',
                            style: '{optDisplay}',
                            $order: 2
                        },
                        POP: {
                            className: 'oodcon',
                            $fonticon: 'ood-uicmd-pop',
                            style: '{popDisplay}',
                            $order: 3
                        },
                        REFRESH: {
                            className: 'oodcon',
                            $fonticon: 'ood-uicmd-refresh',
                            style: '{refreshDisplay}',
                            $order: 4
                        },
                        CLOSE: {
                            className: 'oodcon',
                            $fonticon: 'ood-uicmd-close',
                            style: '{closeDisplay}',
                            $order: 5
                        }
                    }
                },
                MAIN: {
                    $order: 2,
                    tagName: 'div',
                    className: 'ood-uicon-main ood-uibar',
                    style: "{_leftp}",
                    MAINI: {
                        tagName: 'div',
                        className: 'ood-uicon-maini ood-uibar',
                        style: "{_rightp}",
                        PANEL: {
                            tagName: 'div',
                            className: 'ood-uibase ood-uicontainer {_bordertype}',
                            style: '{panelDisplay};{_panelstyle};{_overflow};',
                            text: '{html}' + ood.UI.$childTag
                        }
                    }
                },
                BBAR: {
                    $order: 3,
                    tagName: 'div',
                    className: 'ood-uibar-bottom-s',
                    style: "{_bbarDisplay}",
                    BBARTDL: {
                        className: 'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-bl'
                    },
                    BBARTDM: {
                        $order: 1,
                        className: 'ood-uibar-tdm ood-uibar'
                    },
                    BBARTDR: {
                        $order: 2,
                        className: 'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-br'
                    }
                }
            },
            $submap: ood.UI.$getTagCmdsTpl()
        },
        // 基础Appearances定义 - 详细样式已移至panel-themes.css
        Appearances: {
            KEY: {
                background: 'transparent'
            },
            'KEY BORDER': {
                zoom: ood.browser.ie6 ? 1 : null
            },
            'MIN:hover, MAX:hover,  INFO:hover, OPT:hover, REFRESH:hover, PIN:hover, LAND:hover': {
                'background-color': 'rgba(0,0,0,0.1)',
                'color': 'var(--ood-success)',
                'transform': 'scale(1.1)'
            },
            'CLOSE:hover': {
                'background-color': 'var(--ood-error)',
                'color': 'var(--ood-text-inverse)'
            },
            PANEL: {
                position: 'relative',
                left: 0,
                top: 0,
                overflow: 'auto',
                zoom: ood.browser.ie6 ? 1 : null
            },
            CAPTION: {
                display: 'inline',
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle'
            }
        },
        Behaviors: {
            DroppableKeys: ['PANEL'],
            PanelKeys: ['PANEL'],
            DraggableKeys: ['TBAR'],
            NavKeys: {CAPTION: 1},
            NoDraggableKeys: ['INFO', 'OPT', 'CLOSE', 'POP', 'REFRESH', 'TOGGLE', 'CMD', 'TOGGLE'],
            HoverEffected: {
                INFO: 'INFO',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                POP: 'POP',
                REFRESH: 'REFRESH',
                CMD: 'CMD',
                TOGGLE: 'TOGGLE',
                ICON: 'ICON'
            },
            ClickEffected: {
                INFO: 'INFO',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                POP: 'POP',
                REFRESH: 'REFRESH',
                CMD: 'CMD',
                TOGGLE: 'TOGGLE'
            },
            TOGGLE: {
                onClick: function (profile, e, src) {
                    if (profile.properties.toggleBtn) {
                        profile.box._toggle(profile, !profile.properties.toggle);
                        return false;
                    }
                }
            },
            CAPTION: {
                onClick: function (profile, e, src) {
                    if (!profile.onClickBar || false === profile.boxing().onClickBar(profile, src))
                        return ood.Event.getKey(e).shiftKey;
                }
            },
            PANEL: {
                onClick: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return false;
                    if (profile.onClickPanel)
                        return profile.boxing().onClickPanel(profile, e, src);
                }
            },

            INFO: {
                onClick: function (profile, e, src) {
                    profile.boxing().onShowInfo(profile, e, src);
                }
            },
            OPT: {
                onClick: function (profile, e, src) {
                    profile.boxing().onShowOptions(profile, e, src);
                }
            },
            REFRESH: {
                onClick: function (profile, e, src) {
                    profile.boxing().onRefresh(profile);
                }
            },
            CLOSE: {
                onClick: function (profile, e, src) {
                    var properties = profile.properties;
                    if (properties.disabled) return;
                    var instance = profile.boxing();

                    if (false === instance.beforeClose(profile)) return;

                    instance.destroy();
                }
            },
            POP: {
                onClick: function (profile, e, src) {
                    var properties = profile.properties;
                    if (properties.disabled) return;
                    var pos = profile.getRoot().offset(), size = profile.getRoot().cssSize(),
                        options = {
                            parent: null,
                            host: null,
                            properties: null,
                            events: null,
                            CS: null,
                            CC: null,
                            CB: null,
                            CF: null,
                            init: null
                        };

                    if (profile.beforePop && false == profile.boxing().beforePop(profile, options, e, src))
                        return false;

                    var pro = ood.copy(ood.UI.Dialog.$DataStruct),
                        events = {};
                    ood.merge(pro, properties, 'with');
                    ood.merge(pro, {
                        dock: 'none',
                        width: Math.max(size.width, 200),
                        height: Math.max(size.height, 100),
                        left: pos.left,
                        top: pos.top,
                        landBtn: true
                    }, 'all');
                    if (options.properties)
                        ood.merge(pro, options.properties, 'with');

                    if (options.events)
                        ood.merge(events, options.events, 'all');

                    var dialog = new ood.UI.Dialog(pro, events, options.host || profile.host, options.CS || null, options.CC || null, options.CB || null, options.CF || null);

                    if (ood.isFun(options.init) && false === options.init(dialog, profile, options)) {
                    } else {
                        dialog.show(options.parent || ood('body'));
                        var arr = [];
                        ood.arr.each(profile.children, function (o) {
                            arr.push(o[0]);
                        });
                        if (arr.length) {
                            dialog.append(ood.UI.pack(arr, false));
                        }
                        profile.boxing().removeChildren().destroy(true);
                    }
                }
            },
            CMD: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled) return false;
                    if (profile.onCmd)
                        profile.boxing().onCmd(profile, ood.use(src).id().split('_')[1], e, src);
                    return false;
                }
            }
        },
        DataModel: {
            // 主题属性
            theme: {
                ini: 'dark',
                listbox: ['light', 'dark', 'high-contrast'],
                action: function (v) {
                    this.boxing().setTheme(v);
                },
                caption: ood.getResText("DataModel.theme") || "主题"
            },
            // 响应式设计属性
            responsive: {
                ini: true,
                action: function (v) {
                    if (v) {
                        this.boxing().adjustLayout();
                        // 添加resize监听器
                        ood.Event.on(window, 'resize.' + this.get(0).serialId, this.boxing().adjustLayout.bind(this.boxing()));
                    } else {
                        // 移除resize监听器
                        ood.Event.off(window, 'resize.' + this.get(0).serialId);
                    }
                },
                caption: ood.getResText("DataModel.responsiveDesign") || "响应式设计"
            },
            rotate: {
                ini: null,
                caption: ood.getResText("DataModel.rotation") || "旋转"
            },
            selectable: {
                ini: true,
                caption: ood.getResText("DataModel.selectable") || "可选"
            },
            dock: {
                ini: 'fill',
                caption: ood.getResText("DataModel.dockMode") || "停靠方式"
            },
            caption: {
                ini: undefined,
                // ui update function when setCaption
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('CAPTION').html(ood.adjustRes(v, true));
                },
                caption: ood.getResText("DataModel.title") || "标题"
            },
            html: {
                action: function (v, ov, force) {
                    this.getSubNode('PANEL').html(ood.adjustRes(v, 0, 1), null, null, force);
                },
                caption: ood.getResText("DataModel.htmlContent") || "HTML内容"
            },
            toggle: {
                ini: true,
                action: function (v) {
                    this.box._toggle(this, v);
                },
                caption: ood.getResText("DataModel.toggle") || "切换"
            },
            image: {
                format: 'image',
                action: function (v) {
                    ood.UI.$iconAction(this);
                },
                caption: ood.getResText("DataModel.image") || "图像"
            },
            imagePos: {
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundPosition', value || 'center');
                },
                caption: ood.getResText("DataModel.imagePosition") || "图像位置"
            },
            imageBgSize: {
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundSize', value || '');
                },
                caption: ood.getResText("DataModel.imageBackgroundSize") || "图像背景大小"
            },
            imageClass: {
                ini: '',
                action: function (v, ov) {
                    ood.UI.$iconAction(this, 'ICON', ov);
                },
                caption: ood.getResText("DataModel.imageClass") || "图像类名"
            },
            iconFontCode: {
                action: function (v) {
                    ood.UI.$iconAction(this);
                },
                caption: ood.getResText("DataModel.iconFontCode") || "图标字体代码"
            },
            borderType: {
                ini: 'inset',
                listbox: ['none', 'flat', 'inset', 'outset'],
                action: function (v) {
                    var ns = this,
                        p = ns.properties,
                        node = ns.getSubNode('PANEL'),
                        reg = /^ood-uiborder-/,
                        pretag = 'ood-uiborder-',
                        root = ns.getRoot();
                    node.removeClass(reg);
                    node.addClass(pretag + v);

                    //force to resize
                    ns.adjustSize();
                },
                caption: ood.getResText("DataModel.borderType") || "边框类型"
            },
            noFrame: {
                ini: false,
                action: function (v) {
                    var ns = this, root = ns.getRoot();
                    ns.getSubNode('BBAR').css('display', v ? 'none' : '');
                    ns.getSubNode('MAIN').css('paddingLeft', v ? '0' : '');
                    ns.getSubNode('MAINI').css('paddingRight', v ? '0' : '').css('backgroundImage', v ? 'none' : '');
                    //force to resize
                    ns.adjustSize();
                },
                caption: ood.getResText("DataModel.noFrame") || "无框架"
            },
            hAlign: {
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode("BARCMDL").css('textAlign', v);
                },
                caption: ood.getResText("DataModel.horizontalAlign") || "水平对齐"
            },
            toggleIcon: {
                listbox: ['ood-uicmd-toggle', 'ood-uicmd-check'],
                ini: 'ood-uicmd-toggle',
                action: function (v, ov) {
                    this.getSubNode('TOGGLE').replaceClass(new RegExp("\\b" + ov, 'img'), v);
                },
                caption: ood.getResText("DataModel.toggleIcon") || "切换图标"
            },

            infoBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('INFO').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.infoButton") || "信息按钮"
            },
            optBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('OPT').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.optionButton") || "选项按钮"
            },
            toggleBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('TOGGLE').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.toggleButton") || "切换按钮"
            },
            closeBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('CLOSE').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.closeButton") || "关闭按钮"
            },
            refreshBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('REFRESH').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.refreshButton") || "刷新按钮"
            },
            popBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('POP').css('display', v ? '' : 'none');
                },
                caption: ood.getResText("DataModel.popButton") || "弹出按钮"
            },
            tagCmds: {
                ini: [],
                action: function () {
                    this.boxing().refresh();
                },
                caption: ood.getResText("DataModel.labelCommand") || "标签命令"
            }
        },
        RenderTrigger: function () {
            var self = this;

            //force to resize
            self.boxing().adjustSize();

            // 现代化功能初始化
            self.boxing().PanelTrigger();
        },
        LayoutTrigger: function () {
            var self = this, t = self.properties;
            if (!t.toggle) {
                self.box._toggle(self, false, true);
            } else {
                // for default expand container
                self.boxing().iniPanelView();
            }

            // 现代化功能初始化
            self.boxing().PanelTrigger();


        },

        EventHandlers: {
            onIniPanelView: function (profile) {
            },
            beforeFold: function (profile) {
            },
            beforeExpand: function (profile) {
            },
            afterFold: function (profile) {
            },
            afterExpand: function (profile) {
            },
            onClickBar: function (profile, src) {
            },
            onClickPanel: function (profile, e, src) {
            },

            beforePop: function (profile, options, e, src) {
            },
            beforeClose: function (profile) {
            },
            onShowInfo: function (profile, e, src) {
            },
            onShowOptions: function (profile, e, src) {
            },
            onRefresh: function (profile) {
            },
            onCmd: function (profile, cmdkey, e, src) {
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                nodisplay = 'display:none';

            data._bordertype = 'ood-uiborder-' + data.borderType;
            data._bbarDisplay = data.noFrame ? nodisplay : "";
            data._leftp = data.noFrame ? "padding-left:0;" : "";
            data._rightp = data.noFrame ? "padding-right:0;background-image:none;" : "";

            data.toggleDisplay = data.toggleBtn ? '' : nodisplay;
            data.panelDisplay = data.toggleBtn && !data.toggle ? nodisplay : '';
            data._fi_toggleCls2 = data.toggleIcon + (data.toggleBtn && data.toggle ? ' oodfont-checked ' + data.toggleIcon + '-checked' : '');
            profile._toggle = !!data.toggle;

            data.infoDisplay = data.infoBtn ? '' : nodisplay;
            data.optDisplay = data.optBtn ? '' : nodisplay;
            data.closeDisplay = data.closeBtn ? '' : nodisplay;
            data.popDisplay = data.popBtn ? '' : nodisplay;
            data.refreshDisplay = data.refreshBtn ? '' : nodisplay;

            data._align = 'text-align:' + data.hAlign + ';';

            if (!ood.isEmpty(data.tagCmds))
                this._prepareCmds(profile, data);

            return data;
        },
        _toggle: function (profile, value, ignoreEvent) {
            var p = profile.properties, ins = profile.boxing();

            //event
            if (value) {
                ins.iniPanelView();
            }
            if (ignoreEvent || profile._toggle !== !!value) {
                //set toggle mark
                profile._toggle = p.toggle = !!value;
                if (!ignoreEvent) {
                    if (value) {
                        if (ins.beforeExpand && false === ins.beforeExpand(profile)) return;
                    } else {
                        if (ins.beforeFold && false === ins.beforeFold(profile)) return;
                    }
                }
                //chang toggle button
                if (p.toggleBtn)
                    profile.getSubNode('TOGGLE').tagClass('-checked', !!value);

                // use onresize function
                profile.adjustSize(true);

                if (!ignoreEvent) {
                    if (value) {
                        if (ins.afterExpand)
                            ins.afterExpand(profile);
                    } else {
                        if (ins.afterFold)
                            ins.afterFold(profile);
                    }
                }
                // try redock
                if (p.dock && p.dock != 'none') {
                    ins.adjustDock(true);
                }
            }
        },
        _onresize: function (profile, width, height) {
            var prop = profile.properties,
                // compare with px
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                root = profile.getRoot();

            var isize = {},
                noFrame = prop.noFrame,
                border = profile.getSubNode('BORDER'),
                v1 = profile.getSubNode('TBAR'),
                panel = profile.getSubNode('PANEL'),
                v4 = profile.getSubNode('BBAR'),
                v5 = profile.getSubNode('MAIN'),
                v6 = profile.getSubNode('MAINI'),
                fzrate = profile.getEmSize() / root._getEmSize(),
                panelfz = panel._getEmSize(fzrate),

                cb1 = border.contentBox(),
                h0 = border._borderH(),
                cb2 = panel.contentBox(),
                bordersize = profile.properties.borderType != 'none' ? panel._borderW() : 0,
                h1, h4, t;

            // caculate by px
            if (width && width != 'auto') width = profile.$px(width, null, true);
            if (height && height != 'auto') height = profile.$px(height, null, true);

            if (height) {
                if (profile._toggle) {
                    panel.css('display', '');
                } else {
                    panel.css('display', 'none');
                }
                if (height == 'auto') {
                    root.height(isize.height = 'auto');
                } else {
                    if (profile._toggle) {
                        //force to get height
                        h1 = v1.offsetHeight(true);
                        h4 = noFrame ? 0 : v4.offsetHeight(true);
                        if ((t = height - h0 - h1 - h4) > 0)
                            isize.height = adjustunit(t - (cb2 ? bordersize : 0), panelfz);

                        border.height(adjustunit(height - (cb1 ? h0 : 0), border));
                        root.height(adjustunit(height));
                    } else {
                        border.height('auto');
                        root.height('auto');
                    }
                }
            }
            if (width) {
                isize.width = adjustunit(width
                    - (noFrame ? 0 : (Math.round(parseFloat(v6.css('paddingRight'))) || 0))
                    - (noFrame ? 0 : (Math.round(parseFloat(v5.css('paddingLeft'))) || 0))
                    - h0
                    - bordersize
                    - (v5._borderW())
                    - (v6._borderW())
                    , panelfz);
            }

            if (profile._toggle) {
                panel.cssSize(isize, true);
                if (width) {
                    ood.UI._adjustConW(profile, panel, isize.width);
                }
            }
        }
    }
});