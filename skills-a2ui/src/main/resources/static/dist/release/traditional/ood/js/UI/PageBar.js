ood.Class("ood.UI.PageBar", ["ood.UI", "ood.absValue"], {

    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            caption: '$RAD.widgets.pagination'
        },

        _setCtrlValue: function (value) {
            return this.each(function (profile) {
                if (!profile.renderId) return;
                var t,
                    prop = profile.properties,
                    hidemore = !prop.showMoreBtns,
                    arr = profile.box._v2a(value),
                    min = arr[0],
                    cur = arr[1],
                    max = arr[2],
                    keys = profile.keys,
                    fun = function (p, k) {
                        return p.getSubNode(k)
                    },

                    first = fun(profile, 'FIRST'),
                    prev = fun(profile, 'PREV'),
                    prem = fun(profile, 'PREM'),
                    current = fun(profile, 'CUR'),
                    next = fun(profile, 'NEXT'),
                    nextm = fun(profile, 'NEXTM'),
                    last = fun(profile, 'LAST'),

                    change = function (n, i, j, k, t) {
                        if (i) {
                            n.attr('href', prop.uriTpl.replace('*', i));
                            n.attr('title', i);
                        } else if (t) {
                            n.attr('title', t);
                        }
                        if (ood.isSet(j))
                            n.html(prop.textTpl.replace('*', j), false);

                        if (ood.isSet(k))
                            n.get(0)._real_page = k;
                    },
                    display = function (n, f) {
                        n.css('display', f ? '' : 'none')
                    }
                ;
                //change href and text
                change(first, min, min);
                change(prem, '', '..' + ood.str.repeat('.', String(cur - 1 - min).length), 1, (min + 1) + "~" + (cur - 2));
                change(prev, cur - 1, prop.prevMark || (cur - 1));
                current.get(0).value = cur + "";
                change(next, cur + 1, prop.nextMark || (cur + 1));
                change(nextm, '', '..' + ood.str.repeat('.', String(max - cur - 1).length), 1, (cur + 2) + "~" + (max - 1));
                change(last, max, max);

                //show or hide
                if ((t = cur - min) <= 0) {
                    display(first, 0);
                    display(prem, 0);
                    display(prev, 0);
                } else if (t == 1) {
                    display(first, 1);
                    display(prem, 0);
                    display(prev, 0);
                } else if (t == 2) {
                    display(first, 1);
                    display(prem, 0);
                    display(prev, 1);
                    change(prev, cur - 1, cur - 1);
                } else {
                    display(first, 1);
                    display(prem, hidemore ? 0 : 1);
                    display(prev, 1);
                    if (t == 3) {
                        change(prev, cur - 1, cur - 1);
                        change(prem, cur - 2, cur - 2, 0);
                    }
                }
                if ((t = max - cur) <= 0) {
                    display(last, 0);
                    display(nextm, 0);
                    display(next, 0);
                } else if (t == 1) {
                    display(last, 1);
                    display(nextm, 0);
                    display(next, 0);
                } else if (t == 2) {
                    display(last, 1);
                    display(nextm, 0);
                    display(next, 1);
                    change(next, cur + 1, cur + 1);
                } else {
                    display(last, 1);
                    display(nextm, hidemore ? 0 : 1);
                    display(next, 1);
                    if (t == 3) {
                        change(next, cur + 1, cur + 1);
                        change(nextm, cur + 2, cur + 2, 0);
                    }
                }
            });
        },
        setPage: function (value, force, type) {
            return this.each(function (o) {
                if (!/^[1-9]\d*$/.test(value + "")) return;

                var p = o.properties,
                    pc = p.pageCount,
                    v = (p.$UIvalue || p.value || "") + "",
                    a = v.split(':'),
                    b = parseInt(a[1], 10);

                if (value > parseInt(a[2], 10)) return;
                a[1] = parseInt(value, 10) || b;

                if (force || a[1] !== b) {
                    o.boxing().setUIValue(a.join(':'), false, false, 'page');
                    if (o.onPageSet) o.boxing().onPageSet(o, a[1], (a[1] - 1) * pc, pc, type || "code", b, (b - 1) * pc);
                }
            });
        },
        getPage: function (total) {
            var o = this.get(0),
                p = o.properties,
                v = (p.$UIvalue || p.value || "") + "",
                a = v.split(':');
            return a[total ? 2 : 1];
        },

        buindAPI: function (api) {
            this.api = api;
            return api;
        },

        getTotalPages: function () {
            return this.getPage(true);
        },
        setTotalCount: function (count) {
            if (!/^[1-9]\d*$/.test(count + "")) return this;
            count = parseInt(count, 10);
            return this.each(function (o) {
                var p = o.properties,
                    pc = parseInt(p.pageCount, 10),
                    max = parseInt((count + pc - 1) / pc, 10),
                    v = (p.$UIvalue || p.value || "") + "",
                    a = v.split(':');

                a[2] = max;
                if (parseInt(a[1], 10) > max) a[1] = 1;

                o.boxing().setUIValue(a.join(':'), false, false, 'settotal');
            });
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    first = profile.getSubNode('FIRST'),
                    prev = profile.getSubNode('PREV'),
                    prem = profile.getSubNode('PREM'),
                    current = profile.getSubNode('CUR'),
                    next = profile.getSubNode('NEXT'),
                    nextm = profile.getSubNode('NEXTM'),
                    last = profile.getSubNode('LAST'),
                    input = profile.getSubNode('CUR'),
                    label = profile.getSubNode('LABEL');
                // 按钮样式
                var buttons = [first, prev, prem, next, nextm, last];
                // 移除所有主题类
                root.removeClass('pagebar-dark pagebar-hc');

                if (theme === 'dark') {
                    // 暗黑模式样式
                    root.addClass('pagebar-dark');

                    // 输入框样式
                    input.css({
                        'background-color': 'var(--dark-bg-input)',
                        'border-color': 'var(--dark-border)',
                        'color': 'var(--dark-text)'
                    });

                    // 标签样式
                    if (label && !label.isEmpty()) {
                        label.css({
                            'color': 'var(--dark-text)'
                        });
                    }


                    buttons.forEach(function(btn) {
                        if (btn && !btn.isEmpty()) {
                            btn.css({
                                'background-color': 'var(--dark-bg-btn)',
                                'border-color': 'var(--dark-border)',
                                'color': 'var(--dark-text)'
                            });
                        }
                    });
                } else if (theme === 'high-contrast') {
                    // 高对比度模式样式
                    root.addClass('pagebar-hc');

                    // 输入框样式
                    input.css({
                        'background-color': 'var(--hc-bg-input)',
                        'border': '2px solid var(--hc-border)',
                        'color': 'var(--hc-text)',
                        'font-weight': 'bold'
                    });

                    // 标签样式
                    if (label && !label.isEmpty()) {
                        label.css({
                            'color': 'var(--hc-text)',
                            'font-weight': 'bold'
                        });
                    }

                    // 按钮样式
                    buttons.forEach(function(btn) {
                        if (btn && !btn.isEmpty()) {
                            btn.css({
                                'background-color': 'var(--hc-bg-btn)',
                                'border': '2px solid var(--hc-border)',
                                'color': 'var(--hc-text)',
                                'font-weight': 'bold'
                            });
                        }
                    });
                } else {
                    // 浅色模式样式 (默认)
                    // 输入框样式
                    input.css({
                        'background-color': 'var(--light-bg-input)',
                        'border-color': 'var(--light-border)',
                        'color': 'var(--light-text)'
                    });

                    // 标签样式
                    if (label && !label.isEmpty()) {
                        label.css({
                            'color': 'var(--light-text)'
                        });
                    }

                    // 按钮样式
                    buttons.forEach(function(btn) {
                        if (btn && !btn.isEmpty()) {
                            btn.css({
                                'background-color': 'var(--light-bg-btn)',
                                'border-color': 'var(--light-border)',
                                'color': 'var(--light-text)'
                            });
                        }
                    });
                }
                
                // 保存主题设置
                window.localStorage.setItem('pagebar-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('pagebar-theme') || 'light';
        },

        // 切换所有主题
        toggleTheme: function() {
            var current = this.getTheme();
            var next = current === 'light' ? 'dark' : 
                      current === 'dark' ? 'high-contrast' : 'light';
            this.setTheme(next);
            return this;
        },


        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('pagebar-theme') || 'light';
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
                    buttons = root.find('a.ood-ui-btn'),
                    input = profile.getSubNode('CUR'),
                    label = profile.getSubNode('LABEL'),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('pagebar-mobile');
                    
                    // 移动端调整
                    buttons.css({
                        'padding': '0.4em 0.6em',
                        'font-size': '0.9em',
                        'min-height': '36px'
                    });
                    
                    input.css({
                        'width': '3em',
                        'height': '36px',
                        'font-size': '0.9em'
                    });
                    
                    label.css({
                        'font-size': '0.9em'
                    });
                    
                    // 在小屏幕上隐藏更多按钮文本
                    if (width < 480) {
                        profile.getSubNode('PREM').css('font-size', '0.8em');
                        profile.getSubNode('NEXTM').css('font-size', '0.8em');
                    }
                } else {
                    root.removeClass('pagebar-mobile');

                    // 恢复桌面样式
                    buttons.css({
                        'padding': '',
                        'font-size': '',
                        'min-height': ''
                    });
                    
                    input.css({
                        'width': '',
                        'height': '',
                        'font-size': ''
                    });
                    
                    label.css({
                        'font-size': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('pagebar-tiny');
                    
                    // 优化小屏幕显示
                    label.css({
                        'font-size': '0.8em'
                    });
                    
                    input.css({
                        'width': '2.5em',
                        'font-size': '0.8em'
                    });
                    
                    // 简化按钮显示
                    if (!prop.showMoreBtns) {
                        profile.getSubNode('PREM').css('display', 'none');
                        profile.getSubNode('NEXTM').css('display', 'none');
                    }
                } else {
                    root.removeClass('pagebar-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    buttons = root.find('a.ood-ui-btn'),
                    input = profile.getSubNode('CUR'),
                    label = profile.getSubNode('LABEL'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'navigation',
                    'aria-label': '分页导航'
                });
                
                // 为按钮添加ARIA属性
                profile.getSubNode('FIRST').attr({
                    'role': 'button',
                    'aria-label': '第一页',
                    'tabindex': '0'
                });
                
                profile.getSubNode('PREV').attr({
                    'role': 'button',
                    'aria-label': '上一页',
                    'tabindex': '0'
                });
                
                profile.getSubNode('NEXT').attr({
                    'role': 'button',
                    'aria-label': '下一页',
                    'tabindex': '0'
                });
                
                profile.getSubNode('LAST').attr({
                    'role': 'button',
                    'aria-label': '最后一页',
                    'tabindex': '0'
                });
                
                profile.getSubNode('PREM').attr({
                    'role': 'button',
                    'aria-label': '前面更多页',
                    'tabindex': '0'
                });
                
                profile.getSubNode('NEXTM').attr({
                    'role': 'button',
                    'aria-label': '后面更多页',
                    'tabindex': '0'
                });
                
                // 为输入框添加ARIA属性
                input.attr({
                    'role': 'spinbutton',
                    'aria-label': '当前页码',
                    'aria-valuemin': '1',
                    'aria-valuemax': profile.boxing().getPage(true) || '1',
                    'aria-valuenow': profile.boxing().getPage() || '1'
                });
                
                // 为标签添加关联
                if (label && !label.isEmpty()) {
                    var labelId = profile.serialId + '_label';
                    label.attr('id', labelId);
                    input.attr('aria-labelledby', labelId);
                }
            });
        },
        
        PageBarTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('pagebar-theme');
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
    Static: {
        Templates: {
            style: '{_style} {_hiddenBar}',
            className: '{_className}',
            POOL: {
                style: 'position:absolute;display:none;',
                POP: {
                    tagName: 'div',
                    className: 'ood-uibase ood-ui-reset ood-ui-ctrl'
                }
            },
            LABEL: {
                text: '{caption}'
            },
            FIRST: {
                $order: 1,
                tagName: "a",
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                href: '#',
                tabindex: '{tabindex}'
            },
            PREM: {
                $order: 2,
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                style: '{_css2}',
                tagName: 'a',
                href: '#',
                tabindex: '{tabindex}'
            },
            PREV: {
                $order: 3,
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                tagName: 'a',
                href: '#',
                tabindex: '{tabindex}',
                text: '{prevMark}'
            },
            CUR: {
                $order: 4,
                className: 'ood-ui-input ood-ui-shadow-input ood-uiborder-flat ood-uiborder-radius ood-uibase',
                tagName: 'input',
                tabindex: '{tabindex}',
                style: '{_css}'
            },
            NEXT: {
                $order: 5,
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                tagName: 'a',
                href: '#',
                tabindex: '{tabindex}',
                text: '{nextMark}'
            },
            NEXTM: {
                $order: 6,
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                style: '{_css2}',
                tagName: 'a',
                href: '#',
                tabindex: '{tabindex}'
            },
            LAST: {
                $order: 7,
                className: 'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                tagName: 'a',
                href: '#',
                tabindex: '{tabindex}'
            }
        },
        Appearances: {
            LABEL: {
                padding: 'var(--spacing-xs) var(--spacing-sm) 0 var(--spacing-sm)',
                'vertical-align': 'top',
                'white-space': 'nowrap',
                color: 'var(--text)'
            },
            KEY: {
                display: 'inline',
                overflow: 'visible'
            },
            'KEY a:focus, POP a:focus': {
                'outline': '2px solid var(--rimary)',
                'outline-offset': '2px'
            },
            'KEY .ood-ui-btn, KEY .ood-ui-input, POP .ood-ui-btn': {
                'margin-right': 'var(--spacing-xs)',
                transition: 'all 0.2s ease'
            },
            CUR: {
                'font-weight': 'bold',
                'text-align': 'center',
                padding: 'var(--spacing-xs)',
                'width': '3em',
                'margin-top': '-1px',
                border: '1px solid var(--border)',
                'border-radius': 'var(--radius-sm)',
                background: 'var(--bg)',
                color: 'var(--text)'
            },
            POP: {
                border: '1px solid var(--border)',
                position: 'absolute',
                padding: 'var(--spacing-xs)',
                'line-height': '2.25em',
                background: 'var(--bg)',
                'border-radius': 'var(--radius-md)',
                'box-shadow': 'var(--shadow-md)'
            },
            // 暗黑模式样式
            '.pagebar-dark': {
                '--bg': 'var(--dark-bg)',
                '--text': 'var(--dark-text)',
                '--border': 'var(--dark-border)'
            },
            // 移动端样式
            '.pagebar-mobile': {
                'font-size': '0.9em',
                '--spacing-xs': '0.4em',
                '--spacing-sm': '0.6em'
            },
            '.pagebar-tiny': {
                'font-size': '0.8em',
                '--spacing-xs': '0.3em',
                '--spacing-sm': '0.5em'
            }
        },
        Behaviors: {
            HoverEffected: {
                FIRST: 'FIRST',
                PREM: 'PREM',
                PREV: 'PREV',
                NEXT: 'NEXT',
                NEXTM: 'NEXTM',
                LAST: 'LAST',
                POPI: 'POPI',
                CUR: 'CUR'
            },
            ClickEffected: {
                FIRST: 'FIRST',
                PREM: 'PREM',
                PREV: 'PREV',
                NEXT: 'NEXT',
                NEXTM: 'NEXTM',
                LAST: 'LAST',
                POPI: 'POPI'
            },
            POP: {
                onClick: function (profile, e, src) {
                    var o = ood(src),
                        r = ood.Event.getSrc(e)
                    ;
                    o.setBlurTrigger(profile.key + ":" + profile.$xid, null);
                    profile.getSubNode('POOL').append(o);
                    if (r.tagName.toLowerCase() == 'a' || ((r = r.firstChild) && (r.tagName.toLowerCase() == 'a')) || ((r = r.firstChild) && (r.tagName.toLowerCase() == 'a')) || ((r = r.firstChild) && (r.tagName.toLowerCase() == 'a')))
                        return profile.box._click(profile, r);
                }
            },
            FIRST: {
                onClick: function (profile, e, src) {
                    return profile.box._click(profile, src);
                }
            },
            PREM: {
                onClick: function (profile, e, src) {
                    if (ood.use(src).get(0)._real_page) {
                        profile.box._show(profile, e, src, 0);
                        return false;
                    } else {
                        return profile.box._click(profile, src);
                    }
                }
            },
            PREV: {
                onClick: function (profile, e, src) {
                    return profile.box._click(profile, src);
                }
            },
            CUR: {
                onKeypress: function (profile, e, src) {
                    var k = ood.Event.getKey(e),
                        caret = ood.use(src).caret();
                    // if not positive integer, set back
                    if (!/^\d$/.test(k.key)) {
                        return false;
                    }
                    if (k.key === '0' && caret[0] === 0) {
                        return false;
                    }
                },
                onChange: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled || p.readonly) return;

                    var v = (p.$UIvalue || p.value || "") + "",
                        a = v.split(':'),
                        cur = parseInt(a[1] || "", 10),
                        max = parseInt(a[2] || "", 10),
                        value = ood.use(src).get(0).value || "";

                    // if not positive integer, set back
                    if (!/^[1-9]\d*$/.test(value)) {
                        ood(src).attr('value', cur + "");
                        return;
                    }

                    value = parseInt(value, 10);
                    if (cur !== value) {
                        value = value > max ? max : value;
                        ood(src).attr('value', value + "");
                        profile.boxing().setPage(value, false, 'input');
                    }
                },
                onKeydown: function (profile, e, src) {
                    var p = profile.properties, b = profile.box,
                        evt = ood.Event,
                        k = evt.getKey(e);
                    if (p.disabled || p.readonly) return;

                    //fire onchange
                    if (k.key == 'enter')
                        ood.use(src).onChange();
                }
            },
            NEXT: {
                onClick: function (profile, e, src) {
                    return profile.box._click(profile, src);
                }
            },
            NEXTM: {
                onClick: function (profile, e, src) {
                    if (ood.use(src).get(0)._real_page) {
                        profile.box._show(profile, e, src, 1);
                        return false;
                    } else {
                        return profile.box._click(profile, src);
                    }
                }
            },
            LAST: {
                onClick: function (profile, e, src) {
                    return profile.box._click(profile, src);
                }
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            dataField: null,
            dataBinder: null,
            autoTips: false,
            dirtyMark: false,
            showDirtyMark: false,
            parentID: '',

            hiddenBar: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },
            isFormField: {
                hidden: true,
                ini: false
            },
            caption: {
                ini: ' Page: ',
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('LABEL').html(ood.adjustRes(v, true));
                }
            },
            showMoreBtns: {
                ini: true,
                action: function (v) {
                    this.getSubNodes(['PREM', 'NEXTM']).css('display', v ? '' : 'none');
                }
            },
            pageCount: 20,
            disabled: {
                ini: false,
                action: function (v) {
                    var i = this.getSubNode('CUR'),
                        cls = "ood-ui-disabled";

                    if (v) this.getRoot().addClass(cls);
                    else this.getRoot().removeClass(cls);

                    if (!v && this.properties.readonly)
                        v = true;
                    // use 'readonly'(not 'disabled') for selection
                    i.attr('readonly', v);
                }
            },
            readonly: {
                ini: false,
                action: function (v) {
                    var i = this.getSubNode('CUR'),
                        cls = "ood-ui-readonly";

                    if (v) this.getRoot().addClass(cls);
                    else this.getRoot().removeClass(cls);

                    if (!v && this.properties.disabled)
                        v = true;
                    // use 'readonly'(not 'disabled') for selection
                    i.attr('readonly', v);
                }
            },
            value: "1:1:1",
            uriTpl: "#*",
            textTpl: "*",
            prevMark: '',
            api: null,
            nextMark: '',
            _moreStep: 30
        },
        EventHandlers: {
            onClick: function (profile, page) {
            },
            onPageSet: function (profile, page, start, count, eventType, opage, ostart) {
            }
        },
        RenderTrigger: function () {
            var ns = this, p = ns.properties, a = ((p.value || "") + "").split(':');
            if (p.readonly)
                ns.boxing().setReadonly(true, true);
            if (!ns.$inDesign)
                ns.boxing().setPage(a[1] || a[0], true, 'inited');
            
            // 初始化现代化功能
            ns.boxing().PageBarTrigger();
        },
        _ensureValue: function (profile, value) {
            value = value + '';
            var a = value.split(':'),
                p = profile.properties,
                b = [],
                fun = function (a) {
                    return parseInt(a, 10) || 1
                };
            if (a.length < 3) {
                b = ((p.$UIvalue || p.value || '') + '').split(':');
                a[1] = a[0];
                a[0] = b[0];
                a[2] = b[2];
            }
            b[0] = fun(a[0]);
            b[1] = fun(a[1]);
            b[2] = fun(a[2]);

            b[0] = Math.max(b[0], 1);
            b[0] = Math.min(b[0], b[1]);
            b[2] = Math.max(b[1], b[2]);

            return b.join(':');
        },
        _v2a: function (v) {
            v = typeof v == 'string' ? v.split(':') : v;
            v[0] = parseInt(v[0], 10);
            v[1] = parseInt(v[1], 10);
            v[2] = parseInt(v[2], 10);
            return v;
        },
        _click: function (profile, src) {
            var p = profile.properties;
            if (p.disabled || p.readonly) return false;
            var b = profile.boxing(),
                a = (p.$UIvalue || p.value || "").split(':'),
                nv = parseInt(ood(src).attr('href').split('#')[1], 10) || a[1] || a[0];

            var r = b.onClick(nv);

            // if didn't call setPage  in onclick event, setPage here
            if (!a.length || (nv + "") !== (a[1] + "")) {
                b.setPage(nv, false, 'click');
            }

            return typeof r == "boolean" ? r : false;
        },
        _show: function (profile, e, src, flag) {
            var prop = profile.properties;
            if (prop.disabled || prop.readonly) return false;

            var pid = prop.parentID || ood.ini.$rootContainer,
                arr = profile.box._v2a(prop.value),
                min = arr[0],
                cur = arr[1],
                max = arr[2],

                keys = profile.keys,
                fun = function (p, k) {
                    return p.getSubNode(k)
                },
                pool = fun(profile, 'POOL'),
                pop = fun(profile, 'POP'),
                ceil = function (n) {
                    return Math.ceil((n + 1) / 10) * 10
                },
                a = [],
                t, m, n, i, l
            ;

            if (flag) {
                if ((t = max - 1 - cur) <= 0) return;
                n = cur + 1;
                l = max;
            } else {
                if ((t = cur - 1 - min) <= 0) return;
                n = 1;
                l = cur - 1;
            }
            m = Math.ceil(t / prop._moreStep);
            if (m > 10) {
                n = ceil(n);
                l = ceil(l) - 1;
                m = ceil(m);
            } else
                n = n + m;
            //
            var _id = profile.keys.POPI + ':' + profile.serialId + ':';
            while (n < l) {
                //margin-top for ie6
                a.push('<a style="margin-top:.25em;" id="' + _id + n + '" class="ood-node ood-node-span ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius" href="' + prop.uriTpl.replace('*', n) + '">' + prop.textTpl.replace('*', n) + '</a>')
                n = n + m;
            }
            pop.width('auto');
            pop.html(a.join(' '));
            ood('body').append(pop);
            if (pop.width() > 300) pop.width(300);
            pop.popToTop(src, null, pid ? ood.get(profile, ["host", pid]) ? profile.host[pid].getContainer() : ood(pid) : null);
            pop.setBlurTrigger(profile.key + ":" + profile.$xid, function () {
                pool.append(pop);
            });
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile),
                prop = profile.properties;
            data._hiddenBar = prop.hiddenBar ? ";display:none" : "";
            data._css = ood.browser.kde ? 'resize:none;' : '';
            data._css2 = data.showMoreBtns ? '' : 'display:none;';
            return data;
        }
    },
    Initialize: function () {
        this.addTemplateKeys(['POPI']);
    }
});
