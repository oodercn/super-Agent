ood.Class("ood.UI.Dialog", "ood.UI.Widget", {
    Instance: {
        iniProp: {caption: '弹出窗口'},
        // 设置主题
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 应用主题属性
                root.attr('data-theme', theme);

                // 保存主题设置
                localStorage.setItem('dialog-theme', theme);
            });
        },

        // Get current theme
        getTheme: function () {
            var profile = this.get(0);
            // 从本地存储获取主题
            var savedTheme = null;
            try {
                savedTheme = window.localStorage.getItem('dialog-theme');
            } catch(e) {
                console.warn('Failed to read theme preference:', e);
            }
            return profile.properties.theme || savedTheme || 'dark';
        },
        
        // 循环切换主题
        toggleTheme: function() {
            const themes = ['light', 'dark', 'high-contrast'];
            const currentTheme = this.getTheme();
            const nextIndex = (themes.indexOf(currentTheme) + 1) % themes.length;
            this.setTheme(themes[nextIndex]);
            return this;
        },
        setChildren: function (childrens, prf) {
            var host = this;
            this.removeChildren(true, true, true);
            ood.addChild({children: childrens}, host, host.getModule(), host.getModule());
        },


        showModal: function (parent, left, top, callback, ignoreEffects) {
            this.show(parent, true, left, top, callback, ignoreEffects);
        },
        show: function (parent, modal, left, top, callback, ignoreEffects) {
            parent = parent || ood('body');
            return this.each(function (profile) {
                if (profile.inShowing) return;
                
                // ARIA attributes
                var root = profile.getRoot();
                root.attr({
                    'role': 'dialog',
                    'aria-modal': 'true',
                    'aria-labelledby': profile.getId('CAPTION')
                });
                
                // Focus management
                profile.getSubNode('CONTENT').attr('tabindex', '0');
                var t,
                    p = profile.properties,
                    us = ood.$us(profile),
                    ins = profile.boxing();
                // default to center dlg
                switch (p.initPos) {
                    case 'auto':
                        // all in px
                        if (ood.isHash(left)) {
                            top = left.top;
                            left = left.left;
                        } else {
                            top = (top || top === 0) ? top : profile.$px(p.top);
                            left = (left || left === 0) ? left : profile.$px(p.left);
                        }
                        break;
                    case 'center':
                        if (ood.isHash(left)) {
                            top = left.top + (left.height - profile.$px(p.height)) / 2;
                            left = left.left + (left.width - profile.$px(p.width)) / 2;
                        } else {
                            var pr = parent.get(0) == ood('body').get(0) ? ood.win : (parent['ood.UI'] ? parent.getRoot() : parent),
                                scale = pr == ood.win && ood.ini.$zoomScale || 1;
                            // here, have to use global em
                            top = (top || top === 0) ? top : Math.max(0, (pr.height() / scale - profile.$px(p.height)) / 2 + pr.scrollTop() / scale);
                            left = (left || left === 0) ? left : Math.max(0, (pr.width() / scale - profile.$px(p.width)) / 2 + pr.scrollLeft() / scale);
                        }
                        break;
                }
                if (left < 0) left = 0;
                if (top < 0) top = 0;

                if (p.status == 'max') {
                    left = top = 0;
                }

                var f1 = function () {
                    parent.append(ins);
                    var box = profile.box,
                        root = profile.getRoot(),
                        adjustunit = function (v, emRate) {
                            return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                        };

                    if (p.iframeAutoLoad || p.ajaxAutoLoad)
                        ood.UI.Div._applyAutoLoad(profile);

                    if ((modal || p.modal) && !profile.$inModal)
                        box._modal(profile);

                    ins.activate();
                    var tt = profile._$rs_args, fun = function () {
                        if (profile.onShow) profile.boxing().onShow(profile);
                        delete profile.inShowing;
                        delete profile.$inThread;
                        ood.tryF(callback);

                        // attention animation
                        if (p && p.activeAnim) {
                            ood.asyRun(function () {
                                if (profile && !profile.destroyed)
                                    ins.setActiveAnim(p.activeAnim, true);
                            });
                        }

                    };
                    if (p.status == 'min')
                        box._min(profile, 'normal', fun, true);
                    else if (p.status == 'max')
                        box._max(profile, 'normal', fun, ignoreEffects);
                    else {
                        // resize immidiately here, maybe max here
                        ood.UI.$doResize(profile, (tt && tt[1]) || p.width, (tt && tt[2]) || p.height);
                        root.show(left || left === 0 ? adjustunit(left) : null, top || top === 0 ? adjustunit(top) : null, fun, null, ignoreEffects);
                        box._refreshRegion(profile);
                    }
                };

                profile.inShowing = 1;
                if (t = p.fromRegion) {
                    root.css({
                        'left': t.left + 'px',
                        'top': t.top + 'px',
                        'width': t.width + 'px',
                        'height': t.height + 'px'
                    }).addClass('dialog-animate-open');
                    setTimeout(f1, 300);
                }
                else
                    f1();
            });
        },
        hide: function (ignoreEffects) {
            this.each(function (profile) {
                var pro = profile.properties,
                    box = profile.box,
                    us = ood.$us(profile),
                    root = profile.getRoot();

                var fun = function () {
                    if (profile.inHiding) return;
                    profile.inHiding = 1;
                    if (profile.$inModal)
                        box._unModal(profile);
                    //max has dock prop
                    if (pro.status == 'max' || pro.status == 'min') {
                        var os = pro.status;
                        box._restore(profile);
                        pro.status = os;
                    }

                    var t = pro.fromRegion, f1 = function () {
                        delete profile.inHiding;
                        delete profile.$inThread;
                    };
                    if (t)
                        profile.$inThread = ood.Dom.animate({
                            border: 'solid 1px var(--border-color)',
                            background: 'var(--bg-secondary)',
                            opacity: .1
                        }, {
                            left: [profile.$px(pro.left), t.left],
                            top: [profile.$px(pro.top), t.top],
                            width: [profile.$px(pro.width), t.width],
                            height: [profile.$px(pro.height), t.height]
                        }, null, f1, 300, 0, 'expoOut').start();
                    else
                        f1();
                };
                root.hide(fun, null, ignoreEffects);
            });
            return this;
        },
        close: function (triggerEvent, ignoreEffects) {
            return this.each(function (profile) {
                if (false !== triggerEvent && profile.beforeClose && false === profile.boxing().beforeClose(profile))
                    return;
                if (profile.inClosing) return;
                profile.inClosing = 1;
                var pro = profile.properties, t = pro.fromRegion, fun = function () {
                    profile.boxing().destroy(ignoreEffects);
                    delete profile.inClosing;
                    delete profile.$inThread;
                };

                if (t)
                    profile.$inThread = ood.Dom.animate({
                        border: 'solid 1px var(--border-color)',
                        background: 'var(--bg-secondary)',
                        opacity: .1
                    }, {
                        left: [pro.left, t.left],
                        top: [pro.top, t.top],
                        width: [pro.width, t.width],
                        height: [pro.height, t.height]
                    }, null, fun, 300, 0, 'expoOut').start();
                else
                    fun();
            });
        },
        activate: function (flag) {
            var self = this, profile = this.get(0), ifocus;
            profile.box._active(profile, flag);
            this.getChildren(null, true).each(function (o) {
                if (ood.get(o, ['properties', 'defaultFocus'])) {
                    try {
                        ood.asyRun(function () {
                            o.boxing().activate()
                        })
                    } catch (e) {
                    }
                    ifocus = 1;
                    return false;
                }
            });
            ood.asyRun(function () {
                if (flag !== false && !ifocus) {
                    try {
                        profile.getSubNode('CAPTION').focus(true);
                    } catch (e) {
                    }
                }
                if (self.onActivated) self.onActivated(profile);
            });
        },
        isPinned: function () {
            return !!ood.get(this.get(0), ['properties', 'pinned']);
        },


        // Method to initialize theme
        initTheme: function () {
            // Prefer theme saved in localStorage
            var savedTheme = null;
            try {
                savedTheme = localStorage.getItem('dialog-theme');
            } catch (e) {
            }

            // If no saved theme, detect system theme
            var theme = savedTheme || this.detectSystemTheme();

            // Apply theme
            return this.setTheme(theme);
        },

        // Add method to auto-detect system theme
        detectSystemTheme: function () {
            // 检测系统主题偏好
            var darkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            var hcMode = window.matchMedia && window.matchMedia('(prefers-contrast: more)').matches;
            
            if (hcMode) return 'high-contrast';
            return darkMode ? 'dark' : 'light';
        },

        // Modern initialization trigger
        DialogTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties

            // Initialize theme
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('dialog-theme');
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

        // Responsive layout adjustment
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    screenWidth = ood.win.width(),
                    screenHeight = ood.win.height(),
                    dialogWidth = profile.$px(profile.properties.width),
                    dialogHeight = profile.$px(profile.properties.height),
                    border = profile.getSubNode('BORDER'),
                    tbar = profile.getSubNode('TBAR'),
                    caption = profile.getSubNode('CAPTION'),
                    barcmdl = profile.getSubNode('BARCMDL'),
                    barcmdr = profile.getSubNode('BARCMDR');

                // Adjust dialog size and position for small screens
                if (width < 768) {
                    root.addClass('dialog-mobile');

                    // Full screen or near full screen display on mobile
                    var mobileWidth = Math.min(screenWidth - 20, dialogWidth);
                    var mobileHeight = Math.min(screenHeight - 40, dialogHeight);

                    profile.properties.width = mobileWidth + 'px';
                    profile.properties.height = mobileHeight + 'px';
                    profile.properties.left = 10;
                    profile.properties.top = 20;

                    // Title bar layout adjustment
                    barcmdl.css({
                        'flex-direction': 'column',
                        'align-items': 'flex-start'
                    });

                    // Button group adjustment
                    barcmdr.css({
                        'flex-wrap': 'wrap',
                        'gap': '4px'
                    });

                    // Font size adjustment
                    caption.css('font-size', '0.9em');

                    // Force reposition to screen center
                    ood.asyRun(function () {
                        if (profile.renderId) {
                            root.css({
                                'left': '10px',
                                'top': '20px',
                                'width': mobileWidth + 'px',
                                'height': mobileHeight + 'px'
                            });
                        }
                    });
                } else {
                    root.removeClass('dialog-mobile');

                    // Restore desktop layout
                    barcmdl.css({
                        'flex-direction': '',
                        'align-items': ''
                    });

                    barcmdr.css({
                        'flex-wrap': '',
                        'gap': ''
                    });

                    caption.css('font-size', '');
                }

                // Mobile adaptation
                if (width < 768) {
                    root.addClass('dialog-mobile');

                    // Hide some buttons
                    var buttons = ['INFO', 'OPT', 'REFRESH', 'PIN'];
                    ood.arr.each(buttons, function (btn) {
                        profile.getSubNode(btn).css('display', 'none');
                    });
                } else {
                    root.removeClass('dialog-tiny');

                    // Restore button display
                    var prop = profile.properties;
                    profile.getSubNode('INFO').css('display', prop.infoBtn ? '' : 'none');
                    profile.getSubNode('OPT').css('display', prop.optBtn ? '' : 'none');
                    profile.getSubNode('REFRESH').css('display', prop.refreshBtn ? '' : 'none');
                    profile.getSubNode('PIN').css('display', prop.pinBtn ? '' : 'none');
                }
            });
        },

        // Enhance accessibility support
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var border = profile.getSubNode('BORDER'),
                    caption = profile.getSubNode('CAPTION'),
                    panel = profile.getSubNode('PANEL'),
                    buttons = ['MIN', 'MAX', 'CLOSE', 'INFO', 'OPT', 'REFRESH', 'PIN', 'LAND'];

                // Add ARIA attributes to dialog
                border.attr({
                    'role': 'dialog',
                    'aria-modal': 'true',
                    'aria-labelledby': caption.id()
                });

                caption.attr({
                    'role': 'heading',
                    'aria-level': '1',
                    'tabindex': '0'
                });

                panel.attr({
                    'role': 'document',
                    'aria-labelledby': caption.id()
                });

                // Add ARIA labels and keyboard support for buttons
                ood.arr.each(buttons, function (btnName) {
                    var btn = profile.getSubNode(btnName);
                    if (btn && !btn.isEmpty()) {
                        var label = btnName.toLowerCase();
                        var ariaLabel = {
                            'min': ood.getRes('UI.dialog.minimize'),
                            'max': ood.getRes('UI.dialog.maximize'),
                            'close': ood.getRes('UI.dialog.close'),
                            'info': ood.getRes('UI.dialog.showInfo'),
                            'opt': ood.getRes('UI.dialog.showOptions'),
                            'refresh': ood.getRes('UI.dialog.refresh'),
                            'pin': ood.getRes('UI.dialog.pin'),
                            'land': ood.getRes('UI.dialog.move')
                        }[label] || label + ' button';

                        btn.attr({
                            'role': 'button',
                            'aria-label': ariaLabel,
                            'tabindex': '0'
                        });

                    }
                });

            });
        }
    },
    Initialize: function () {
        var ns = this, t = ns.getTemplate();
        // 检查模板是否存在，如果不存在则创建一个默认的模板对象
        if (!t) {
            t = {};
        }
        // 确保FRAME和BORDER属性存在
        if (!t.FRAME) t.FRAME = {};
        if (!t.FRAME.BORDER) t.FRAME.BORDER = {};

        ood.merge(t.FRAME.BORDER, {
            tabindex: '{tabindex}',
            className: 'ood-uiborder-outset ood-uiborder-box ood-uiborder-radius-big',
            TABSTOP1: {$order: -1},
            TBAR: {
                tagName: 'div',
                style: '{_displayBar}',
                className: 'ood-uibar-top',
                TBARTDL: {
                    className: 'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-tl',
                    TBARTDLT: {
                        className: 'ood-uibar-tdlt'
                    }
                },
                TBARTDM: {
                    $order: 1,
                    className: 'ood-uibar-tdm ood-uibar',
                    TBARTDMT: {
                        className: 'ood-uibar-tdmt'
                    }
                },
                TBARTDR: {
                    $order: 2,
                    className: 'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-tr',
                    TBARTDRT: {
                        className: 'ood-uibar-tdrt'
                    }
                },
                BARCMDL: {
                    $order: 3,
                    tagName: 'div',
                    className: 'ood-uibar-cmdl',
                    style: '{_align}',
                    RULER: {
                        className: 'ood-ui-ruler'
                    },
                    LTAGCMDS: {
                        tagName: 'span',
                        className: 'ood-ltag-cmds',
                        style: '{_ltagDisplay}',
                        text: "{ltagCmds}"
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
                        className: 'oodcon dialog-btn dialog-btn-info',
                        $fonticon: 'ood-uicmd-info',
                        $order: 2
                    },
                    OPT: {
                        className: 'oodcon dialog-btn dialog-btn-opt',
                        $fonticon: 'ood-uicmd-opt',
                        $order: 3
                    },
                    PIN: {
                        $order: 4,
                        className: 'oodcon dialog-btn dialog-btn-pin',
                        $fonticon: 'ood-uicmd-pin'
                    },
                    LAND: {
                        $order: 5,
                        className: 'oodcon dialog-btn dialog-btn-land',
                        $fonticon: 'ood-uicmd-land'
                    },
                    REFRESH: {
                        className: 'oodcon dialog-btn dialog-btn-refresh',
                        $fonticon: 'ood-uicmd-refresh',
                        $order: 6
                    },
                    MIN: {
                        $order: 7,
                        className: 'oodcon',
                        $fonticon: 'ood-uicmd-min',
                        style: '{minDisplay}'
                    },
                    RESTORE: {
                        $order: 8,
                        className: 'oodcon',
                        $fonticon: 'ood-uicmd-restore',
                        style: 'display:none;'
                    },
                    MAX: {
                        $order: 9,
                        className: 'oodcon',
                        $fonticon: 'ood-uicmd-max',
                        style: '{maxDisplay}'
                    },
                    CLOSE: {
                        $order: 10,
                        className: 'oodcon',
                        $fonticon: 'ood-uicmd-close',
                        style: '{closeDisplay}'
                    }
                },
                TBARTDB: {
                    $order: 5,
                    tagName: 'div',
                    className: 'ood-uibar-tdb ood-uiborder-inset ood-uiborder-radius'
                }
            },
            MAIN: {
                $order: 2,
                tagName: 'div',
                className: 'ood-uicon-main ood-uibar',
                MAINI: {
                    tagName: 'div',
                    className: 'ood-uicon-maini ood-uibar',
                    PANEL: {
                        tagName: 'div',
                        style: "{_panelstyle};{_overflow};",
                        className: 'ood-uibar ood-uicontainer',
                        text: '{html}' + ood.UI.$childTag
                    }
                }
            },
            BBAR: {
                $order: 3,
                tagName: 'div',
                className: 'ood-uibar-bottom',
                BBARTDL: {
                    $order: 1,
                    className: 'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-bl'
                },
                BBARTDM: {
                    $order: 2,
                    className: 'ood-uibar-tdm ood-uibar'
                },
                BBARTDR: {
                    $order: 3,
                    className: 'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-br'
                }
            },
            TABSTOP2: {$order: 9}
        }, 'all');
        t.$submap = ood.UI.$getTagCmdsTpl();

        ns.setTemplate(t);

        ood.alert = ns.alert;
        ood.confirm = ns.confirm;
        ood.pop = ns.pop;
        ood.prompt = ns.prompt;
    },
    Static: {
        Appearances: {
            KEY: {
                overflow: 'visible',
                transition: 'all 0.3s ease'
            },
            'BORDER': {
                'box-shadow': '0 8px 32px rgba(0,0,0,0.3)',
                'border-radius': '12px',
                'backdrop-filter': 'blur(10px)',
                transition: 'box-shadow 0.3s ease, transform 0.3s ease',
                outline: 0
            },
            'BORDER:hover': {
                'box-shadow': '0 12px 48px rgba(0,0,0,0.4)'
                //  'transform': 'translateY(-2px)'
            },
            'TBAR': {
                //   'background': 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
                height:'2.5em',
                'border-radius': '12px 12px 0 0',
                'padding': '8px 8px',
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'border-bottom': '1px solid rgba(0,0,0,0.1)'
            },
            'PANEL': {
                position: 'relative',
                overflow: 'auto',
                'border-radius': '0 0 12px 12px',
                'background-color': 'var(--light-bg)',
                transition: 'background-color 0.3s ease'
            },
            'CAPTION': {
                display: 'inline',
                'font-weight': '600',
                'font-size': '1.1em',
                'color': 'var(--light-text)',
                'vertical-align': ood.browser.ie6 ? 'baseline' : 'middle'
            },
            'BARCMDL, BARCMDR': {
                'display': 'flex',
                'align-items': 'center',
                'gap': '8px'
            },
            // Button style enhancements
            'MIN, MAX, CLOSE, INFO, OPT, REFRESH, PIN, LAND': {
                'border-radius': '6px',
             //   'padding': '6px 8px',
                'transition': 'all 0.2s ease',
                'cursor': 'pointer',
                'font-size': '1.1em'
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
            'LTAGCMDS, RTAGCMDS': {
                padding: 0,
                margin: 0,
                'vertical-align': 'middle'
            },
            "TABSTOP1,TABSTOP2": {
                height: 0,
                width: "16px",
                display: 'inline',
                position: 'absolute'
            },
            'TBART, BBART': {
                'border-spacing': 0,
                'border-collapse': 'separate'
            },
            MAINI: {
                'padding-top': '.16667em'
            },
            // Responsive design styles
            '.dialog-mobile TBAR': {
                'flex-direction': 'column',
                'align-items': 'flex-start',
                'gap': '8px',
                'padding': '8px 12px'
            },
            '.dialog-mobile BARCMDR': {
                'flex-wrap': 'wrap'
            },
            '.dialog-mobile BORDER': {
                'border-radius': '8px',
                'margin': '10px'
            },
            '.dialog-tiny CAPTION': {
                'font-size': '0.9em'
            }
          
        },
        Behaviors: {
            DroppableKeys: ['PANEL'],
            PanelKeys: ['PANEL'],
            DraggableKeys: ['LAND'],
            NoDraggableKeys: ['LAND', 'MIN', 'MAX', 'RESTORE', 'PIN', 'INFO', 'OPT', 'CLOSE', 'REFRESH', 'CMD'],
            HoverEffected: {
                LAND: 'LAND',
                MIN: 'MIN',
                MAX: 'MAX',
                RESTORE: 'RESTORE',
                PIN: 'PIN',
                INFO: 'INFO',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                REFRESH: 'REFRESH',
                CMD: 'CMD',
                ICON: 'ICON'
            },
            ClickEffected: {
                LAND: 'LAND',
                MIN: 'MIN',
                MAX: 'MAX',
                RESTORE: 'RESTORE',
                PIN: 'PIN',
                INFO: 'INFO',
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                REFRESH: 'REFRESH',
                CMD: 'CMD',
                ICON: 'ICON'
            },
            onMousedown: function (profile, e) {
                if (!profile.$inModal)
                    profile.box._active(profile);
            },
            afterKeydown: function (profile, e) {
                var keys = ood.Event.getKey(e);
                if ((e.$key || e.keyCode || e.charCode) == 9) {
                    // hack for ie tab event
                    if (ood.browser.ie) {
                        var id = "ood::_specialforietab";
                        if (!ood.Dom.byId(id))
                            ood('body').append("<div style='display:none;position:absolute;' id=" + id + "></div>");
                        ood.Dom.byId(id).innerHTML = ood.stamp() + "";
                    }
                    var n1 = profile.getSubNode("TABSTOP1").get(0),
                        n2 = profile.getSubNode("TABSTOP2").get(0),
                        m = ood.Event.getSrc(e), t;
                    if (keys.shiftKey) {
                        if (m !== n1)
                            n1.tabIndex = m.tabIndex;
                        n2.removeAttribute("tabIndex");
                    } else {
                        if (m !== n2)
                            n2.tabIndex = m.tabIndex;
                        n1.removeAttribute("tabIndex");
                    }
                    n1 = n2 = m = null;
                }
            },
            onDragstop: function (profile) {
                var p = profile.properties,
                    us = ood.$us(profile),
                    root = profile.getRoot(),
                    pos = root.cssPos(),
                    l = null, t = null;

                if (profile.$px(p.left) !== pos.left)
                    p.left = l = profile.$forceu(pos.left, null);
                if (profile.$px(p.top) !== pos.top)
                    p.top = t = profile.$forceu(pos.top, null);

                root.cssPos({left: l, top: t});

                if (profile.onMove && (l !== null || t !== null))
                    profile.boxing().onMove(profile, l, t, null, null);
            },
            TABSTOP1: {
                onFocus: function (profile, e, src) {
                    var tabindex = parseInt(ood.use(src).get(0).tabIndex || 1 + "", 10) - 1;
                    var children = profile.getRoot().get(0).getElementsByTagName('*'), t, n;
                    for (var i = 0, l = children.length, o; o = children[i]; i++) {
                        if (o.nodeType == 1) {
                            //cant set tabIndex to zero
                            if (o.tabIndex && o.tabIndex <= tabindex) {
                                if (!t) t = (n = o).tabIndex;
                                if (o.tabIndex > t) t = (n = o).tabIndex;
                                if (t === tabindex) break;
                            }
                        }
                    }
                    if (o) {
                        ood(o).focus(true);
                        ood.use(src).get(0).tabIndex = o.tabIndex;
                    }
                    else {
                        o = profile.getRoot().nextFocus(false, true, false);
                        ood(o).focus(true);
                        ood.use(src).get(0).tabIndex = o.get(0).tabIndex;
                    }
                    children = o = null;
                }
            },
            TABSTOP2: {
                onFocus: function (profile, e, src) {
                    var tabindex = parseInt(ood.use(src).get(0).tabIndex || 1 + "") + 1;
                    var children = profile.getRoot().get(0).getElementsByTagName('*'), t, n;
                    for (var i = 0, l = children.length, o; o = children[i]; i++) {
                        if (o.nodeType == 1) {
                            //cant set tabIndex to zero
                            if (o.tabIndex && o.tabIndex >= tabindex) {
                                if (!t) t = (n = o).tabIndex;
                                if (o.tabIndex < t) t = (n = o).tabIndex;
                                if (t === tabindex) break;
                            }
                        }
                    }
                    if (o) {
                        ood(o).focus(true);
                        ood.use(src).get(0).tabIndex = o.tabIndex;
                    }
                    else {
                        o = profile.getRoot().nextFocus(true, true, false);
                        ood(o).focus(true);
                        ood.use(src).get(0).tabIndex = o.get(0).tabIndex;
                    }
                    children = o = null;
                }
            },
            TBAR: {
                beforeMousedown: function (profile, e, src) {
                    if (profile.$inDesign) return;

                    if (ood.Event.getBtn(e) != "left") return;
                    if (profile.getKey(ood.Event.getSrc(e).parentNode.id) == profile.keys.BARCMDR) return;
                    if (profile.properties.status == "max") return false;

                    if (profile.properties.movable && !profile._locked) {
                        profile.box._active(profile);
                        var root = profile.getRoot(),
                            region = root.cssRegion(),
                            pregion = root.parent().cssRegion(),
                            dist = profile.getEmSize();
                        root.startDrag(e, {
                            dragDefer: 2,
                            maxLeftOffset: region.left,
                            maxRightOffset: pregion.width - region.left - dist,
                            maxTopOffset: region.top,
                            maxBottomOffset: pregion.height - region.top - dist,
                            magneticDistance: dist,
                            xMagneticLines: [0, pregion.width - region.width],
                            yMagneticLines: [0, pregion.height - region.height],
                            targetOffsetParent: root.parent()
                        });
                    }
                },
                onDblclick: function (profile, e, src) {
                    if (profile.getKey(ood.Event.getSrc(e).parentNode.id) == profile.keys.BARCMDR) return;
                    if (!profile.properties.maxBtn) return;
                    if (profile.properties.status == 'max')
                        profile.box._restore(profile);
                    else
                        profile.box._max(profile);
                }
            },
            PIN: {
                onClick: function (profile, e, src) {
                    var key = profile.keys.PIN, t = profile.properties, ins = profile.boxing();
                    if (profile.beforePin && false === profile.boxing().beforePin(profile, t.pinned))
                        return;

                    //set pinned status
                    t.pinned = !t.pinned;
                    //set appea
                    profile.getSubNode('PIN').tagClass('-checked', t.pinned);
                    //set lock flag for not movable
                    profile._locked = t.pinned;

                    // add/remove resize
                    if (t.resizer) {
                        if (!t.pinned) {
                            // if not in min mode
                            if (t.status != 'min')
                                ins._resizer();
                        } else if (profile.$resizer)
                        //profile.boxing().setResizer(false);
                            ins._unResizer();
                    }
                }
            },
            MIN: {
                onClick: function (profile, e, src) {
                    profile.box._min(profile, null, null, true);
                }
            },
            MAX: {
                onClick: function (profile, e, src) {
                    profile.box._max(profile, null, null, true);
                }
            },
            RESTORE: {
                onClick: function (profile, e, src) {
                    profile.box._restore(profile);
                }
            },
            LAND: {
                onClick: function (profile, e, src) {
                    profile.boxing().onLand(profile, e, src);
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
                role: 'button',
                'aria-label': 'Information',
                onClick: function (profile, e, src) {
                    profile.boxing().onShowInfo(profile, e, src);
                }
            },
            OPT: {
                role: 'button',
                'aria-label': 'Options',
                onClick: function (profile, e, src) {
                    profile.boxing().onShowOptions(profile, e, src);
                }
            },
            REFRESH: {
                role: 'button',
                'aria-label': 'Refresh',
                onClick: function (profile, e, src) {
                    profile.boxing().onRefresh(profile);
                }
            },
            CLOSE: {
                role: 'button',
                'aria-label': 'Close dialog',
                onClick: function (profile, e, src) {
                    profile.boxing().close();
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
            // Theme properties
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'system'],
                action: function (v) {
                    // If system theme selected, detect system preference
                    if (v === 'system') {
                        v = this.boxing().detectSystemTheme();
                    }
                    this.boxing().setTheme(v);
                }
            },
            // Responsive design properties
            responsive: {
                ini: true,
                action: function (v) {
                    if (v) {
                        this.boxing().adjustLayout();
                        // Add resize listener
                        ood.Event.on(window, 'resize.' + this.get(0).serialId, this.boxing().adjustLayout.bind(this.boxing()));
                    } else {
                        // Remove resize listener
                        ood.Event.off(window, 'resize.' + this.get(0).serialId);
                    }
                }
            },
            rotate: null,
            selectable: true,
            tips: null,
            border: null,
            disabled: null,
            dock: 'none',
            showEffects: "Classic",
            hideEffects: "",
            initPos: {
                ini: 'center',
                listbox: ['auto', 'center']
            },
            iframeAutoLoad: {
                ini: "",
                action: function () {
                    ood.UI.Div._applyAutoLoad(this);
                }
            },
            ajaxAutoLoad: {
                ini: "",
                action: function () {
                    ood.UI.Div._applyAutoLoad(this);
                }
            },
            html: {
                html: 1,
                action: function (v, ov, force) {
                    this.getSubNode('PANEL').html(ood.adjustRes(v, 0, 1), null, null, force);
                }
            },

            // setCaption and getCaption
            caption: {
                ini: undefined,
                // ui update function when setCaption
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('CAPTION').html(ood.adjustRes(v, true));
                }
            },
            image: {
                format: 'image',
                action: function (v) {
                    ood.UI.$iconAction(this);
                }
            },
            imagePos: {
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundPosition', value || 'center');
                }
            },
            imageBgSize: {
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundSize', value || '');
                }
            },
            imageClass: {
                ini: '',
                action: function (v, ov) {
                    ood.UI.$iconAction(this, 'ICON', ov);
                }
            },
            iconFontCode: {
                action: function (v) {
                    ood.UI.$iconAction(this);
                }
            },
            // setCaption and getCaption
            shadow: true,
            resizer: true,
            movable: true,

            displayBar: {
                ini: true,
                action: function (v) {
                    var o = this.getSubNode('TBAR');
                    if (v)
                        o.css('display', '');
                    else
                        o.css('display', 'none');
                }
            },

            minBtn: {
                ini: true,
                action: function (v) {
                    var o = this.getSubNode('MIN');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            maxBtn: {
                ini: true,
                action: function (v) {
                    var o = this.getSubNode('MAX');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            restoreBtn: {
                ini: true,
                action: function (v) {
                    var o = this.getSubNode('RESTORE');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            infoBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('INFO').css('display', v ? '' : 'none');
                }
            },
            optBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('OPT').css('display', v ? '' : 'none');
                }
            },
            closeBtn: {
                ini: true,
                action: function (v) {
                    var o = this.getSubNode('CLOSE');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            refreshBtn: {
                ini: false,
                action: function (v) {
                    this.getSubNode('REFRESH').css('display', v ? '' : 'none');
                }
            },
            pinBtn: {
                ini: false,
                action: function (v) {
                    var o = this.getSubNode('PIN');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            landBtn: {
                ini: false,
                action: function (v) {
                    var o = this.getSubNode('LAND');
                    if (v)
                        o.setInlineBlock();
                    else
                        o.css('display', 'none');
                }
            },
            width: {
                $spaceunit: 1,
                ini: '25em'
            },
            height: {
                $spaceunit: 1,
                ini: '36em'
            },
            minWidth: 200,
            minHeight: 100,

            position: 'absolute',
            fromRegion: {
                hidden: true,
                ini: null
            },
            modal: {
                ini: false,
                action: function (v) {
                    if (this.box) {
                        if (v) this.box._modal(this);
                        else this.box._unModal(this);
                    }
                }
            },
            status: {
                ini: 'normal',
                listbox: ['normal', 'min', 'max'],
                action: function (v, o) {
                    var self = this, b = self.box;
                    if (v == 'min') b._min(self, o, null, true);
                    else if (v == 'max') b._max(self, o, null, true);
                    else b._restore(self, o);
                }
            },
            hAlign: {
                ini: 'left',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getSubNode("BARCMDL").css('textAlign', v);
                }
            },
            tagCmds: {
                ini: [],
                action: function () {
                    this.boxing().refresh();
                }
            },
            //hide props( with px)
            $hborder: 1,
            $vborder: 1
        },
        EventHandlers: {
            onIniPanelView: function (profile) {
            },
            onShow: function (profile) {
            },
            onActivated: function (profile) {
            },
            beforePin: function (profile, value) {
            },
            beforeStatusChanged: function (profile, oldStatus, newStatus) {
            },
            afterStatusChanged: function (profile, oldStatus, newStatus) {
            },
            onClickPanel: function (profile, e, src) {
            },

            onLand: function (profile, e, src) {
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
        RenderTrigger: function () {
            var ns = this;
            ns.destroyTrigger = function () {
                var s = this;
                if (s.$inModal) s.box._unModal(s);
            };
        },


        LayoutTrigger: function () {
            var self = this, t = self.properties;
            // ensure modal
            if (t.modal) {
                var p = self.$modalDiv && self.$modalDiv.parent(), b = self.box;
                if (p && p.get(0) && p.get(0) !== self.getRootNode()) {
                    b._unModal(self);
                }
                b._modal(self);

                // Enhance modal background styles
                if (self.$modalDiv) {
                    self.$modalDiv.addClass('dialog-modal-backdrop');
                }
            }

            // Modern feature initialization
            self.boxing().DialogTrigger();

            // Remove calls to non-existent methods
            self.boxing().initTheme();

            // Set responsive layout
            self.boxing().adjustLayout();

            // Add system theme change listener
            if (window.matchMedia) {
                window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function (e) {
                    if (self.properties.theme === 'system' || !self.properties.theme) {
                        self.boxing().setTheme(e.matches ? 'dark' : 'light');
                    }
                });
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                nodisplay = 'display:none';

            data._displayBar = data.displayBar ? '' : nodisplay;
            data.minDisplay = data.minBtn ? '' : nodisplay;
            data.maxDisplay = data.maxBtn ? '' : nodisplay;
            data.infoDisplay = data.infoBtn ? '' : nodisplay;
            data.optDisplay = data.optBtn ? '' : nodisplay;
            data.closeDisplay = data.closeBtn ? '' : nodisplay;
            data.pinDisplay = data.pinBtn ? '' : nodisplay;
            data.landDisplay = data.landBtn ? '' : nodisplay;
            data.refreshDisplay = data.refreshBtn ? '' : nodisplay;
            data._align = 'text-align:' + data.hAlign + ';';

            var status = profile.properties.status;
            if (status == 'min' || status == 'max')
                profile.$noR = 1;
            if (ood.isStr(data.overflow))
                data._overflow = data.overflow.indexOf(':') != -1 ? (data.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");

            if (!ood.isEmpty(data.tagCmds))
                this._prepareCmds(profile, data);

            return data;
        },

        //ov from design mode
        _min: function (profile, status, effectcallback, ignoreEffects) {
            var o = profile.getRoot(),
                box = profile.box,
                p = o.parent(),
                ins = profile.boxing(),
                t = profile.properties,
                a = ood.Dom._getEffects(t.showEffects, 1);
            if (profile.$inThread) profile.$inThread.abort();
            if (!status) status = t.status;
            if (profile.beforeStatusChanged && false === profile.boxing().beforeStatusChanged(profile, 'min', status))
                return;

            // unMax
            if (status == 'max')
                box._unMax(profile);
            // keep restore values
            else
                box._refreshRegion(profile);

            // hide those
            profile.getSubNodes(['PANEL', 'BBAR']).css('display', 'none');

            if (t.minBtn) {
                // show restore button
                if (t.restoreBtn)
                    profile.getSubNode('RESTORE').setInlineBlock();
                // hide min button
                profile.getSubNode('MIN').css('display', 'none');
            }

            // lockResize function
            if (t.resizer && profile.$resizer)
                ins._unResizer();

            //set it before resize
            t.status = 'min';

            var h1 = o.height(),
                h2 = profile.getSubNode('BORDER').height(),
                h = profile.getSubNode('TBAR').height();
            // resize
            o.cssSize({width: t.minWidth, height: h + h1 - h2}, true);
            if (profile.afterStatusChanged) profile.boxing().afterStatusChanged(profile, 'min', status);

            if (a && ood.browser.ie678)
                ood.filter(a.params, function (o, i) {
                    return !!ood.Dom._cssfake[i];
                });
            o.show(null, null, effectcallback, null, ignoreEffects);
        },
        _max: function (profile, status, effectcallback, ignoreEffects) {
            var o = profile.getRoot(),
                box = profile.box,
                ins = profile.boxing(),
                p = o.parent(),
                t = profile.properties,
                a = ood.Dom._getEffects(t.showEffects, 1);
            if (!status) status = t.status;
            if (profile.$inThread) profile.$inThread.abort();

            if (profile.beforeStatusChanged && false === profile.boxing().beforeStatusChanged(profile, 'max', status))
                return;

            // if from normal status
            if (status == 'min')
            //unset min
                box._unMin(profile);
            else
                box._refreshRegion(profile);

            // hide pin button
            if (t.pinBtn)
                profile.getSubNode('PIN').css('display', 'none');
            if (t.maxBtn) {
                // hide max button
                profile.getSubNode('MAX').css('display', 'none');
                // show restore button
                if (t.restoreBtn)
                    profile.getSubNode('RESTORE').setInlineBlock();
            }

            if (t.resizer && profile.$resizer)
                ins._unResizer();

            t.status = 'max';

            ins.setDock('cover', true);
            if (profile.afterStatusChanged) profile.boxing().afterStatusChanged(profile, 'max', status);
            if (a && ood.browser.ie678)
                ood.filter(a.params, function (o, i) {
                    return !!ood.Dom._cssfake[i];
                });
            o.show(null, null, effectcallback, null, ignoreEffects);
        },
        _restore: function (profile, status) {
            var o = profile.getRoot(),
                box = profile.box,
                t = profile.properties;
            if (!status) status = t.status;
            t.status = 'normal';

            if (profile.beforeStatusChanged && false === profile.boxing().beforeStatusChanged(profile, 'normal', status))
                return;

            // if from max
            if (status == 'max') box._unMax(profile);
            if (status == 'min') box._unMin(profile);

            profile.getSubNode('BORDER').ieRemedy();

            // hide restore button
            profile.getSubNode('RESTORE').css('display', 'none');
        },
        _unMax: function (profile) {
            var t = profile.properties,
                ins = profile.boxing();
            profile.getSubNode('MAX').setInlineBlock();
            if (t.pinBtn)
                profile.getSubNode('PIN').setInlineBlock();

            if (t.resizer && !t.pinned) {
                ins._resizer();
            }

            ins.setDock('none');

            // resize
            profile.adjustSize(true);
            if (profile.afterStatusChanged) profile.boxing().afterStatusChanged(profile, 'normal', status);
        },
        _unMin: function (profile) {
            var t = profile.properties,
                ins = profile.boxing();
            profile.getSubNodes(['PANEL', 'BBAR']).css('display', 'block');
            profile.getSubNode('MIN').setInlineBlock();

            if (t.resizer && !t.pinned) {
                ins._resizer();
            }

            profile.getRoot().cssSize({width: t.width, height: t.height});
            // resize
            profile.adjustSize(true);
        },
        _active: function (profile, flag) {
            var self = this;
            if (profile.$inDesign) return;

            if (flag !== false && ood.$cache.unique.activeWndId == profile.$xid) return;

            self._deActive();
            if (flag !== false) {
                var o = ood(profile.domId),
                    //in ie, .children can't get the same thread added node(modal div,  here)
                    t1 = o.topZindex(),
                    t2 = parseInt(o.css('zIndex'), 0);
                o.css('zIndex', t1 > t2 ? t1 : t2);

                profile.getSubNode('TBAR').tagClass('-focus');
                ood.$cache.unique.activeWndId = profile.$xid;
            }
        },
        _deActive: function () {
            var profile;
            if (profile = ood.UI._cache['$' + ood.$cache.unique.activeWndId])
                profile.getSubNode('TBAR').tagClass('-focus', false);
            delete ood.$cache.unique.activeWndId;
        },
        _modal: function (profile) {
            var s = profile.getRoot(), temp, p = s.parent(), cover;
            if (!p.isEmpty()) {
                if (!profile.$inModal) {
                    cover = profile.$modalDiv;
                    if (!cover || !cover.get(0) || !cover.get(0).parentNode) {
                        cover = profile.$modalDiv = ood.create("<div class='ood-cover ood-custom ood-cover-modal' style='left:0;top:0;position:absolute;overflow:hidden;display:block;z-index:0;'></div>");
                        cover.setSelectable(false);
                    }
                    p.append(cover);

                    // attach onresize event
                    if (p.get(0) === document.body || p.get(0) === document || p.get(0) === window) p = ood.win;

                    cover.css({
                        display: 'block',
                        width: Math.max(p.width(), p.scrollWidth()) + 'px',
                        height: Math.max(p.height(), p.scrollHeight()) + 'px'
                    })
                        .onMousedown(function () {
                            return profile.$inDesign ? null : false
                        })
                        .topZindex(true);

                    if (profile.$inDesign) cover.onClick(function () {
                        s.onClick(true)
                    });

                    p.onSize(function (node) {
                        node = ood(node);


                        var w = node.width() + "px", h = node.height() + "px";
                        // set widht/height first
                        cover.css({width: w, height: h});
                        ood.asyRun(function () {
                            var w = Math.max(node.width(), node.scrollWidth()) + "px",
                                h = Math.max(node.height(), node.scrollHeight()) + "px";
                            cover.css({width: w, height: h});
                        });
                    }, "dialog:" + profile.serialId);

                    var i = (parseInt(cover.css('zIndex'), 10) || 0) + 1;
                    s.css('zIndex', i);

                    if (i >= ood.Dom.TOP_ZINDEX)
                        ood.Dom.TOP_ZINDEX = i + 1;


                    profile.$inModal = true;
                    // avoid triggering the previously set trigger
                    p.setBlurTrigger(profile.$xid + "_anti", true, ood([cover.get(0), profile.getRootNode()]));
                }
            }
        },
        _unModal: function (profile) {
            if (profile.$inModal) {
                // detach onresize event
                var p = profile.$modalDiv.parent();
                if (p.get(0) === document.body || p.get(0) === document || p.get(0) === window)
                    p = ood.win;

                p.onSize(null, "dialog:" + profile.serialId);

                profile.getRoot().css('zIndex', 0);

                profile.$modalDiv.css('display', 'none');
                var node = profile.getSubNode('BORDER');
                if (!node.isEmpty())
                    node.append(profile.$modalDiv);

                profile.$inModal = false;
                p.setBlurTrigger(profile.$xid + "_anti");
            }
        },
        _refreshRegion: function (profile) {
            if (!profile.renderId) return;
            var prop = profile.properties,
                root = profile.getRoot(),
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                nr = root.cssRegion();

            nr.left = adjustunit(nr.left);
            nr.top = adjustunit(nr.top);
            nr.width = adjustunit(nr.width);
            nr.height = adjustunit(nr.height);

            return ood.merge(prop, nr, function (o, i) {
                return prop[i] != 'auto'
            });
        },

        _adjust: function (dialog, caption, content, dftTilte, left, top) {
            caption = ood.adjustRes(caption || '');
            if (!ood.isSet(content) || content === "") {
                content = caption;
                caption = dftTilte || "";
            }

            var node = dialog.$div.reBoxing(),
                ID = 'ood:temp:dialog',
                me = arguments.callee,
                w, h;

            if (!ood.Dom.byId(ID)) {
                n2 = me._cache = node.clone(false);
                ood('body').append(n2);
                n2.css({
                    overflow: 'visible',
                    position: 'absolute',
                    visibility: 'visible',
                    left: ood.Dom.HIDE_VALUE,
                    top: ood.Dom.HIDE_VALUE
                })
                    .id(ID, true);
            }
            var n2 = me._cache;
            n2.width('auto').height('auto');
            n2.html(content, false);
            var size = n2.cssSize();
            size.width += 10;
            size.height += 10;

            node.html(content);

            if (size.width > 500) {
                size.width = 500;
                n2.width(500);
                size.height = n2.offsetHeight() + 10;
                n2.width('auto');
            }
            n2.html("", false);
            size.height += 10;
            if (size.height > 400) size.height = 400;
            if (size.width < 150) size.width = 150;
            if (size.height < 30) size.height = 30;

            node.cssSize(size).css('overflow', 'auto').show();

            var fs = dialog.getRoot()._getEmSize();
            w = size.width + fs * 2;
            h = size.height + fs * 7.5;
            dialog.setCaption(caption).setWidth(w).setHeight(h);
            return {width: w, height: h};
        },
        alert: function (title, content, onClose, btnCap, left, top, parent, subId, noCache) {
            var me = arguments.callee, dialog;
            if (noCache || !(dialog = me.dialog) || !dialog.get(0) || (!dialog.get(0).renderId)) {
                dialog = new ood.UI.Dialog({
                    overflow: 'hidden',
                    minBtn: false,
                    maxBtn: false,
                    pinBtn: false,
                    resizer: false
                }, {
                    beforeClose: function () {
                        ood.tryF(dialog._$onClose);
                        dialog._$onClose = null;
                        if (!noCache) {
                            dialog.hide();
                            return false;
                        }
                    },
                    onHotKeydown: function (p, k) {
                        if (k.key == 'esc')
                            dialog.close();
                    }
                });

                var cmd = dialog.$cmd = new ood.UI.Div({
                        //  height:'2.5em',
                        dock: 'bottom',
                        zIndex: 10
                    }, null, null, null, {KEY: "text-align:center;padding-top:.5em"}),

                    btn = dialog.$btn = new ood.UI.SButton({
                            position: 'relative',
                            tabindex: 1
                        },
                        {
                            onClick: function () {
                                dialog.close();
                            },
                            onHotKeydown: function (p, k) {
                                if (k.key == 'enter')
                                    dialog.close();
                            }
                        }, null, null, {KEY: 'margin:0 .5em'});
                cmd.append(btn);

                var div = dialog.$div = new ood.UI.Div({
                    left: 10,
                    top: 10
                });
                dialog.append(cmd).append(div).render();

                if (!noCache)
                    me.dialog = dialog;
            }
            dialog._$onClose = onClose;

            dialog.$btn.setCaption("&nbsp;&nbsp;" + (btnCap || ood.wrapRes('$inline.ok')) + "&nbsp;&nbsp;");

            var size = ood.UI.Dialog._adjust(dialog, title, content, "Alert");

            if (parent && parent["ood.UI"]) parent = parent.getContainer(subId);
            if (!ood.isSet(parent)) parent = ood('body');

            dialog.show(parent, true, left, top);
            ood.resetRun("dlg_focus:" + dialog.get(0).$xid, function () {
                dialog.$btn.activate();
            });
            return dialog;
        },
        confirm: function (title, caption, onYes, onNo, btnCapYes, btnCapNo, left, top, parent, subId, noCache) {
            var me = arguments.callee, dialog;

            if (noCache || !(dialog = me.dialog) || !dialog.get(0) || (!dialog.get(0).renderId)) {
                dialog = new ood.UI.Dialog({
                    overflow: 'hidden',
                    minBtn: false,
                    maxBtn: false,
                    pinBtn: false,
                    resizer: false
                }, {
                    beforeClose: function () {
                        if (!dialog._$_clicked)
                            ood.tryF(dialog._$onNo, ['close']);
                        else
                            delete dialog._$_clicked;
                        dialog._$onYes = dialog._$onNo = null;
                        if (!noCache) {
                            dialog.hide();
                            return false;
                        }
                    },
                    onHotKeydown: function (p, k) {
                        if (k.key == 'esc')
                            dialog.close();
                    }
                });

                var cmd = dialog.$cmd = new ood.UI.Div({
                        height: '3.5em',
                        dock: 'bottom',
                        zIndex: 10
                    }, null, null, null, {KEY: "text-align:center;padding-top:.5em"}),
                    btn = dialog.$btn1 = new ood.UI.SButton({
                            tabindex: 1,
                            position: 'relative'
                        },
                        {
                            onClick: function () {
                                ood.tryF(dialog._$onYes, ['yes']);
                                dialog._$_clicked = 1;
                                dialog.close();
                            }
                        }, null, null, {KEY: 'margin:0 .5em'});
                cmd.append(btn);

                btn = dialog.$btn2 = new ood.UI.SButton({
                        tabindex: 1,
                        position: 'relative'
                    },
                    {
                        onClick: function () {
                            ood.tryF(dialog._$onNo, ['no']);
                            dialog._$_clicked = 1;
                            dialog.close();
                        }
                    }, null, null, {KEY: 'margin:0 .5em'});
                cmd.append(btn);

                var div = dialog.$div = new ood.UI.Div({
                    left: 10,
                    top: 10
                });
                dialog.append(cmd).append(div).render();

                if (!noCache)
                    me.dialog = dialog;
            }
            dialog._$onYes = onYes;
            dialog._$onNo = onNo;
            delete dialog._$_clicked;
            dialog.$btn1.setCaption("&nbsp;&nbsp;" + (btnCapYes || ood.wrapRes('$inline.yes')) + "&nbsp;&nbsp;");
            dialog.$btn2.setCaption("&nbsp;&nbsp;" + (btnCapNo || ood.wrapRes('$inline.no')) + "&nbsp;&nbsp;");
            var size = ood.UI.Dialog._adjust(dialog, title, caption, "Confirm");

            if (parent && parent["ood.UI"]) parent = parent.getContainer(subId);
            if (!ood.isSet(parent)) parent = ood('body');

            dialog.show(parent, true, left, top);
            ood.resetRun("dlg_focus:" + dialog.get(0).$xid, function () {
                dialog.$btn2.activate();
            });
            return dialog;
        },
        pop: function (title, content, btnCap, left, top, parent, subId) {
            var dialog = new ood.UI.Dialog({
                    overflow: 'hidden',
                    minBtn: false,
                    maxBtn: false,
                    pinBtn: false,
                    resizer: false
                }, {
                    onHotKeydown: function (p, k) {
                        if (k.key == 'esc')
                            dialog.close();
                    }
                }),

                cmd = dialog.$cmd = new ood.UI.Div({
                    height: '2.5em',
                    dock: 'bottom',
                    zIndex: 10
                }, null, null, null, {KEY: "text-align:center;padding-top:.5em"})
                    .append(dialog.$btn = new ood.UI.SButton({
                            caption: "&nbsp;&nbsp;" + (btnCap || '$inline.ok') + "&nbsp;&nbsp;",
                            tabindex: 1,
                            position: 'relative'
                        },
                        {
                            onClick: function () {
                                dialog.destroy();
                            },
                            onHotKeydown: function (p, k) {
                                if (k.key == 'enter')
                                    dialog.close();
                            }
                        }, null, null, {KEY: 'margin:0 .5em'})),

                div = dialog.$div = new ood.UI.Div({
                    left: 10,
                    top: 10,
                    width: '7em'
                }).setCustomStyle({
                    KEY: 'overflow:visible'
                });

            dialog.append(cmd).append(div).render();

            var size = ood.UI.Dialog._adjust(dialog, title, content, "Message");

            if (parent && parent["ood.UI"]) parent = parent.getContainer(subId);
            if (!ood.isSet(parent)) parent = ood('body');

            dialog.show(parent, false, left, top);

            ood.resetRun("dlg_focus:" + dialog.get(0).$xid, function () {
                dialog.$btn.activate();
            });
            return dialog;
        },
        prompt: function (title, caption, content, onYes, onNo, btnCapYes, btnCapNo, left, top, parent, subId, noCache) {
            var dialog,
                me = arguments.callee;
            if (noCache || !(dialog = me.dialog) || !dialog.get(0) || (!dialog.get(0).renderId)) {
                dialog = new ood.UI.Dialog({
                    overflow: 'hidden',
                    minBtn: false,
                    maxBtn: false,
                    pinBtn: false,
                    left: left || 200,
                    top: top || 200,
                    width: '25em',
                    height: '11em',
                    conDockPadding: {left: '.5em', right: '.5em', top: 0, bottom: 0}
                }, {
                    beforeClose: function () {
                        if (!dialog._$_clickYes)
                            ood.tryF(dialog._$onNo, ["no"]);
                        else
                            delete dialog._$_clickYes;

                        dialog._$input.setValue('', false, 'prompt');
                        dialog._$onYes = dialog._$onNo = null;
                        if (!noCache) {
                            dialog.hide();
                            return false;
                        }
                    }
                });
                var con = dialog._$caption = new ood.UI.Div({
                        height: '1.5em',
                        dock: 'top'
                    }),
                    cmd = new ood.UI.Div({
                        height: '2.5em',
                        dock: 'bottom',
                        zIndex: 10
                    }, null, null, null, {KEY: "text-align:center;padding-top:.5em"})
                        .append(dialog.$btn1 = new ood.UI.SButton({
                                position: 'relative',
                                tabindex: 1
                            },
                            {
                                onClick: function () {
                                    if (false !== ood.tryF(dialog._$onYes, [dialog._$input.getUIValue()])) {
                                        dialog._$_clickYes = 1;
                                        dialog.close();
                                    }
                                }
                            }, null, null, {KEY: 'margin:0 .5em'}));

                cmd.append(dialog.$btn2 = new ood.UI.SButton({
                        tabindex: 1,
                        position: 'relative'
                    },
                    {
                        onClick: function () {
                            dialog.close();
                        }
                    }, null, null, {KEY: 'margin:0 .5em'}));
                var inp = dialog._$input = new ood.UI.Input({
                    dock: 'fill',
                    multiLines: true
                })
                dialog.append(con).append(cmd).append(inp).render();
                if (!noCache)
                    me.dialog = dialog;
            }
            dialog.setCaption(title || 'Prompt');
            dialog._$caption.setHtml(caption || "");
            dialog._$input.setValue(content || "", true, 'prompt');
            dialog._$onYes = onYes;
            dialog._$onNo = onNo;
            delete dialog._$_clickYes;
            dialog.$btn1.setCaption("&nbsp;&nbsp;" + (btnCapYes || ood.wrapRes('$inline.ok')) + "&nbsp;&nbsp;");
            dialog.$btn2.setCaption("&nbsp;&nbsp;" + (btnCapNo || ood.wrapRes('$inline.cancel')) + "&nbsp;&nbsp;");

            if (parent && parent["ood.UI"]) parent = parent.getContainer(subId);
            if (!ood.isSet(parent)) parent = ood('body');

            dialog.show(parent, true, left, top);
            ood.resetRun("dlg_focus:" + dialog.get(0).$xid, function () {
                dialog._$input.activate();
            });
            return dialog;
        },
        //
        _onresize: function (profile, width, height, force) {
            if (width && profile.properties.status == 'min')
                width = profile.properties.minWidth;

            var prop = profile.properties,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                size = arguments.callee.upper.apply(this, arguments),
                isize = {},
                v0 = profile.getSubNode('BORDER'),
                v1 = profile.getSubNode('TBAR'),
                v2 = profile.getSubNode('PANEL'),
                v4 = profile.getSubNode('BBAR'),
                v5 = profile.getSubNode('MAIN'),
                v6 = profile.getSubNode('MAINI'),
                cb1 = v0.contentBox(),
                cb2 = v2.contentBox(),
                h1, h4, t;
            // caculate with px
            if (width) width = profile.$px(width);
            if (height) height = profile.$px(height);
            if (size.left) size.left = profile.$px(size.left);
            if (size.top) size.top = profile.$px(size.top);
            if (size.width) size.width = profile.$px(size.width);
            if (size.height) size.height = profile.$px(size.height);

            if (height) {
                if (height == 'auto') {
                    isize.height = height;
                } else {
                    //force to get height
                    h1 = v1.offsetHeight(true);
                    h4 = v4.offsetHeight(true);
                    if ((t = size.height - h1 - h4) > 0)
                        isize.height = t;
                }
            }
            if (height)
                isize.height = isize.height - v6._paddingH() - (cb1 ? 0 : v0._borderH()) - (cb2 ? 0 : v2._borderH());

            if (width)
                isize.width = size.width
                    - (parseFloat(v6.css('paddingRight')) || 0) - (parseFloat(v6.css('borderRightWidth')) || 0)
                    - (parseFloat(v5.css('paddingLeft')) || 0) - (parseFloat(v5.css('borderLeftWidth')) || 0);
            -(cb1 ? 0 : v0._borderW()) - (cb2 ? 0 : v2._borderW())

            if (width && us > 0) isize.width = adjustunit(isize.width);
            if (height && us > 0) isize.height = adjustunit(isize.height);

            v2.cssSize(isize, true);
            if (width) {
                ood.UI._adjustConW(profile, v2, isize.width);
            }
        }
    }
});
