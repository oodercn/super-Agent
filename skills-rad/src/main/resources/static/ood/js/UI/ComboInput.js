ood.Class("ood.UI.ComboInput", "ood.UI.Input", {

    /*Instance*/
    Instance: {
        iniProp: {width: '18em', labelSize: '6em', caption: '$RAD.widgets.inputField', items: [
                {id: 'option1', caption: '选项1', value: '1'},
                {id: 'option2', caption: '选项2', value: '2'},
                {id: 'option3', caption: '选项3', value: '3'},
                {id: 'option4', caption: '选项4', value: '4', disabled: true}
            ]},
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    frame = profile.getSubNode('FRAME'),
                    border = profile.getSubNode('BORDER'),
                    box = profile.getSubNode('BOX'),
                    wrap = profile.getSubNode('WRAP'),
                    input = profile.getSubNode('INPUT'),
                    label = profile.getSubNode('LABEL'),
                    arrow = profile.getSubNode('ARROW'),
                    error = profile.getSubNode('ERROR');

                // 添加基础类名
                root.addClass('combo-input-themed');
                
                // 添加组件特定类名
                frame.addClass('combo-input-frame');
                border.addClass('combo-input-border');
                box.addClass('combo-input-box');
                wrap.addClass('combo-input-wrap');
                input.addClass('combo-input-field');
                label.addClass('combo-input-label');
                arrow.addClass('combo-input-arrow');
                error.addClass('combo-input-error');

                // 应用主题属性
                root.attr('data-theme', theme);
                
                // 保存主题设置
                localStorage.setItem('combo-input-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            return this.properties.theme;
        },

        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('combo-input-theme') || 'light';
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        _adjustV: function (v) {
            var profile = this.get(0), p = profile.properties;
            if (profile.$isNumber) {
                v = ('' + v).replace(/[^\d.-]/g, '');
                v = ood.isNumb(parseFloat(v)) ? ood.toFixedNumber(v, p.precision) : null;
            } else if (profile.properties.type == 'date' || profile.properties.type == 'datetime') {
                v = ood.isDate(v) ? v : ood.isFinite(v) ? new Date(parseInt(v, 10)) : null;


                if (v && ood.isDate(v)) {
                    v = v.getTime();
                }

            } else if (typeof v == "string" && v.indexOf("\r") != -1) {
                v = v.replace(/(\r\n|\r)/g, "\n");
            }
            return v;
        },
        getValue: function () {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return this._adjustV(v);
        },

        getAllSelectedValues: function (key) {
            var items = getSelectedItem(true);
            var selectValues = [];
            if (key) {
                ood.each(items, function (item) {
                        selectValues.push(item[k])
                    }
                )
            } else {
                selectValues = items;
            }
            return selectValues;
        },

        getSelectedItem: function (returnArr) {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return this._adjustV(v);
        },

        getUICationValue: function (returnArr) {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return this._adjustV(v);
        },

        getUIValue: function () {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return this._adjustV(v);
        },

        setCaptionValue: function (value) {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;

        },

        getCaptionValue: function () {
            var upper = arguments.callee.upper,
                v = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return v;
        },

        _getCtrlValue: function () {
            return this.get(0).properties.$UIvalue;
            //return this._fromEditor(this.getSubNode('INPUT').attr('value'));
        },
        _setCtrlValue: function (value) {
            var ns = this, me = arguments.callee, r1 = me._r1 || (me._r1 = /\</),
                r2 = me._r2 || (me._r2 = /\<\/?[^>]+\>/g);
            return this.each(function (profile) {
                if (!profile.$typeOK) profile.box._iniType(profile);
                var o = profile.getSubNode('INPUT'), type = profile.properties.type;

                value = profile.$_onedit
                    // for enter/esc key, show editMode value
                    ? ns._toEditor(value)
                    : ns.getShowValue(value);

                if (type !== 'none' && type !== 'input' && type !== 'password' && !profile.properties.multiLines && typeof value == 'string' && r1.test(value)) value = value.replace(r2, '');

                if (profile.$Mask && !value) {
                    value = profile.$Mask;
                }
                profile.$_inner = 1;
                o.attr('value', value || '');
                delete profile.$_inner;
                if (type == 'color') {
                    var clr = ood.UI.ColorPicker.getTextColor(value);
                    o.css({backgroundColor: value, color: clr});

                    if (profile.properties.showMode == 'compact')
                        profile.getRoot().query('button').css('color', clr);
                }
            })
        },
        _compareValue: function (v1, v2) {
            var profile = this.get(0), t;
            if (t = profile.CF.compareValue || profile.$compareValue)
                return t(profile, v1, v2);

            return v1 === v2;
        },
        getShowValue: function (value) {
            var profile = this.get(0),
                pro = profile.properties, v, t;
            if (!ood.isDefined(value))
                value = pro.$UIvalue;

            if (!profile.$typeOK) profile.box._iniType(profile);

            // try to give default caption
            if (t = profile.CF.getShowValue || profile.$getShowValue)
                v = t(profile, value);
            else {
                //get from items
                if ('listbox' == pro.type) {
                    var list = (pro.listKey) ? ood.UI.getCachedData(pro.listKey) : pro.items;
                    if (list && (t = ood.arr.subIndexOf(list, 'id', value)) != -1) {
                        v = list[t].caption + "";
                        if (v && v.length > 0) v = ood.adjustRes(v);
                    } else
                        v = null;
                } else
                    v = profile.$showValue;
            }
            if (!ood.isSet(v) && (profile.$inputReadonly || pro.inputReadonly))
                v = ood.isSet(pro.caption) ? pro.caption : null;
            return "" + (ood.isSet(v) ? v : ood.isSet(value) ? value : "");
        },
        _toEditor: function (value) {
            var profile = this.get(0),
                pro = profile.properties, t;
            if (t = profile.CF.toEditor || profile.$toEditor)
                return t(profile, value);
            return value;
        },
        _fromEditor: function (value) {
            var profile = this.get(0),
                pro = profile.properties, t;

            if (t = profile.CF.fromEditor || profile.$fromEditor)
                return t(profile, value);
            return value;
        },
        setPopWnd: function (drop) {
            if (this.isDestroyed()) return;
            var profile = this.get(0);
            profile.$poplink = drop ? drop['ood.Module'] ? drop.getRoot(true) : drop['ood.UI'] ? drop.get(0) : drop : null;
            (profile.$poplink.$beforeDestroy = (profile.$poplink.$beforeDestroy || {}))["$poplink_to"] = function () {
                delete profile.$poplink;
            };
            (profile.$beforeDestroy = (profile.$beforeDestroy || {}))["$poplink"] = function () {
                if (profile.$poplink) {
                    ood.filter(profile.box.$drop, function (o) {
                        return o !== profile.$poplink;
                    });
                    profile.$poplink.boxing().destroy();
                    profile.$poplink = null;
                }
            };
        },
        _cache: function (type, focus) {
            if (this.isDestroyed()) return;
            var profile = this.get(0);

            var drop = profile.$poplink, cached = profile.properties.cachePopWnd;
            if (drop) {
                if (!cached) {
                    if (!drop.destroyed)
                        drop.boxing().destroy(true);
                    delete profile.$poplink;
                    if (focus)
                        profile.boxing().activate();
                } else {
                    if (!profile.__tryToHide) {
                        profile.__tryToHide = ood.asyRun(function () {
                            // destroyed
                            if (!profile.box) return;
                            delete profile.__tryToHide;

                            if (!drop.destroyed) {
                                if (ood.browser.opr)
                                    drop.getRoot().css('display', 'none');
                                if (drop.boxing()._clearMouseOver) drop.boxing()._clearMouseOver();
                                profile.getSubNode('POOL').append(drop.getRoot());
                            }
                            if (focus)
                                profile.boxing().activate();
                        });
                    }
                }
            }
            delete profile.$poplink;

            if (profile.afterPopHide)
                this.afterPopHide(profile, drop, type);
            return cached;
        },
        clearPopCache: function () {
            var profile = this.get(0);
            if (profile.renderId)
                profile.getSubNode('POOL').empty();
            delete profile.$poplink;
            return this;
        },
        setUploadObj: function (input) {
            var profile = this.get(0),
                prop = profile.properties,
                c = ood(input).get(0);
            if (c.tagName && c.tagName.toLowerCase() == 'input' && c.type == 'file') {
                if (profile.renderId && prop.type == 'file') {
                    var o = profile.getSubNode('FILE').get(0);

                    ood.setNodeData(c.$xid = o.$xid, 'element', c);
                    c.id = o.id;
                    c.onclick = o.onclick;
                    c.onchange = o.onchange;
                    o.$xid = null;
                    o.id = o.onclick = o.onchange = null;
                    //a special node, must delete if from cache here:
                    delete profile.$_domid[profile.keys['FILE']];
                    ood([o]).addPrev(c).remove(false);
                    this.setUIValue(c.value || "", null, null, 'setfile');
                }
            }
            return this;
        },
        //for upload ,special must get the original node
        getUploadObj: function () {
            var profile = this.get(0), prop = profile.properties;
            if (profile.renderId && prop.type == 'file') {
                var o = profile.getSubNode('FILE').get(0);
                if (!o.value)
                    return null;

                var c = o.cloneNode(false);
                c.value = "";
                //inner replace
                ood.setNodeData(c.$xid = o.$xid, 'element', c);
                c.onclick = o.onclick;
                c.onchange = o.onchange;

                //remove those
                //if(ood.browser.ie)
                //    o.removeAttribute('$xid');
                //else
                //    delete o.$xid;
                //**: "removeAttribute" doesn't work in IE9+
                o.$xid = null;

                o.id = o.onclick = o.onchange = null;

                //a special node, must delete if from cache here:
                delete profile.$_domid[profile.keys['FILE']];
                ood([o]).addPrev(c).remove(false);
                c = null;

                this.setUIValue(this.getValue(), null, null, 'getfile');

                return o;
            }
        },
        popFileSelector: function (accept, multiple) {
            var profile = this.get(0), prop = profile.properties;
            if (profile.renderId && prop.type == 'file') {
                var input = profile.getSubNode('FILE'), fileInput = input.get(0);
                input.attr("accept", accept || prop.fileAccept || null);
                input.attr("multiple", multiple || prop.fileMultiple ? "multiple" : null);

                // for IE11
                if (ood.browser.ie11) {
                    var label = document.createElement("div");
                    fileInput.appendChild(label);
                    label.click();
                    fileInput.removeChild(label);
                } else {
                    fileInput.click();
                }
            }
        },

        ComboInputTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                this.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('comboinput-theme');
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

        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    input = profile.getSubNode('INPUT'),
                    button = profile.getSubNode('BUTTON'),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('comboinput-mobile');
                    
                    // 移动端调整
                    input.css({
                        'padding': '0.6em 0.5em',
                        'font-size': '16px' // 防止iOS缩放
                    });
                    if (button && !button.isEmpty()) {
                        button.css({
                            'min-width': '2.5em',
                            'padding': '0.6em'
                        });
                    }
                } else {
                    root.removeClass('comboinput-mobile');
                    
                    // 恢复桌面样式
                    input.css({
                        'padding': '',
                        'font-size': ''
                    });
                    if (button && !button.isEmpty()) {
                        button.css({
                            'min-width': '',
                            'padding': ''
                        });
                    }
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('comboinput-tiny');
                    
                    // 调整下拉列表最大高度
                    var dropList = profile.getSubNode('DROPLIST');
                    if (dropList && !dropList.isEmpty()) {
                        dropList.css({
                            'max-height': '60vh'
                        });
                    }
                } else {
                    root.removeClass('comboinput-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var input = profile.getSubNode('INPUT'),
                    button = profile.getSubNode('BUTTON'),
                    dropList = profile.getSubNode('DROPLIST'),
                    properties = profile.properties;

                // 为输入框添加ARIA属性
                if (input && !input.isEmpty()) {
                    input.attr({
                        'role': properties.type === 'combobox' ? 'combobox' : 'textbox',
                        'aria-label': properties.tips || properties.labelText || '输入框',
                        'aria-describedby': properties.tips ? profile.serialId + '_tips' : null,
                        'aria-expanded': 'false'
                    });
                    
                    if (properties.type === 'combobox' || properties.type === 'listbox') {
                        input.attr({
                            'aria-autocomplete': 'list',
                            'aria-haspopup': 'listbox'
                        });
                    }
                }
                
                // 为下拉按钮添加ARIA属性
                if (button && !button.isEmpty()) {
                    button.attr({
                        'role': 'button',
                        'aria-label': '打开下拉列表',
                        'tabindex': '0',
                        'aria-expanded': 'false'
                    });
                }

                // 为下拉列表添加ARIA属性
                if (dropList && !dropList.isEmpty()) {
                    dropList.attr({
                        'role': 'listbox',
                        'aria-label': '选项列表'
                    });
                }
            });
        },
        resetValue: function (value) {
            this.each(function (p) {
                if (p.properties.type == 'file')
                    p.getSubNode('FILE').attr('value', '');
            });
            var upper = arguments.callee.upper,
                rtn = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return rtn;
        },
        _drop: function (e, src, baseNode, ignoreEvent) {
            return this.each(function (profile) {
                var pro = profile.properties, type = pro.type, cacheDrop = pro.cachePopWnd;
                if (pro.disabled || pro.readonly) return;

                //open already
                if (profile.$poplink) return;
                var o, v, drop,
                    box = profile.boxing(),
                    main = profile.getSubNode('BOX'),
                    btn = profile.getSubNode('RBTN'),
                    pos = main.offset();
                pos.top += main.offsetHeight();

                //special cmd type: getter, 'cmdbox' and 'popbox'
                if ((!ignoreEvent && profile.beforeComboPop && false === box.beforeComboPop(profile, pos, e, src)))
                    return;

                // for standard drop
                if (type == 'combobox' || type == 'listbox' || type == 'helpinput'
                    || type == 'date'
                    || type == 'time'
                    || type == 'datetime'
                    || type == 'color') {

                    if (profile.__tryToHide) {
                        ood.clearTimeout(profile.__tryToHide);
                        delete profile.__tryToHide;
                    }

                    //get cache key
                    var cachekey;
                    if (cacheDrop) {
                        switch (type) {
                            case 'time':
                            case 'date':
                            case 'datetime':
                            case 'color':
                                cachekey = type;
                                break;
                            default:
                                if (pro.listKey)
                                //function no cache
                                    if (typeof ood.get(ood.$cache, ['UIDATA', pro.listKey]) == 'function')
                                        drop = cachekey = null;
                                    else
                                        cachekey = "!" + pro.listKey;
                                else
                                    cachekey = "$" + profile.$xid;
                        }
                        //get from global cache
                        if (cachekey) {
                            //filter first
                            ood.filter(profile.box.$drop, function (o) {
                                return !!o.renderId;
                            });
                            drop = profile.box.$drop[cachekey];
                        }
                    }

                    //cache pop
                    if (!drop) {
                        switch (type) {
                            case 'combobox':
                            case 'listbox':
                            case 'helpinput':
                                o = ood.create('List');
                                o.setHost(profile).setDirtyMark(false).setItems(ood.copy(pro.items)).setListKey(pro.listKey || '');
                                if (pro.dropListWidth) o.setWidth(pro.dropListWidth);
                                else o.setWidth(profile.$forceu(main.offsetWidth() + btn.offsetWidth()));

                                o.setHeight(pro.dropListHeight || 'auto');

                                o.afterClick(function () {
                                    if (!this.destroyed)
                                        this.boxing()._cache('', true);
                                    else
                                        o.destroy(true);
                                    return false;
                                });
                                o.beforeUIValueSet(function (p, ovalue, value) {
                                    var b2 = this.boxing();
                                    if (type == 'combobox') {
                                        var item = p.queryItems(p.properties.items, function (o) {
                                            return o.id == value
                                        }, false, true);
                                        if (item.length)
                                            value = item[0].caption;
                                    }
                                    //update value
                                    b2.setUIValue(value, null, null, 'pick');

                                    //cache pop
                                    return b2._cache('', true);
                                });
                                break;
                            case 'time':
                                o = ood.create('TimePicker');
                                o.setHost(profile);
                                o.beforeClose(function () {
                                    if (!this.destroyed)
                                        this.boxing()._cache('', true);
                                    return false
                                });
                                o.beforeUIValueSet(function (p, o, v) {
                                    var b2 = this.boxing();
                                    //update value
                                    b2.setUIValue(v, null, null, 'pick');
                                    return b2._cache('', true);
                                });
                                break;
                            case 'date':
                            case 'datetime':
                                o = ood.create('DatePicker');

                                if (type == 'datetime')
                                    o.setTimeInput(true);

                                o.setHost(profile);
                                o.beforeClose(function () {
                                    if (!this.destroyed)
                                        this.boxing()._cache('', true);
                                    else
                                        o.destroy(true);
                                    return false
                                });
                                o.beforeUIValueSet(function (p, o, v) {
                                    var b2 = this.boxing();
                                    //update value
                                    b2.setUIValue(String(v.getTime()), null, null, 'pick');
                                    return b2._cache('', true);
                                });

                                break;
                            case 'color':
                                o = ood.create('ColorPicker');
                                o.setHost(profile);
                                o.beforeClose(function () {
                                    if (!this.destroyed)
                                        this.boxing()._cache('', true);
                                    else
                                        o.destroy(true);
                                    return false
                                });
                                o.beforeUIValueSet(function (p, o, v) {
                                    var b2 = this.boxing();
                                    //update value
                                    b2.setUIValue((v == 'transparent' ? '' : '#') + v, null, null, 'pick');
                                    return b2._cache('', true);
                                });
                                break;
                        }
                        if (ood.isHash(pro.popCtrlProp) && !ood.isEmpty(pro.popCtrlProp))
                            o.setProperties(pro.popCtrlProp);
                        if (ood.isHash(pro.popCtrlEvents) && !ood.isEmpty(pro.popCtrlEvents))
                            o.setEvents(pro.popCtrlEvents);

                        drop = o.get(0);

                        //set to global cache
                        if (cachekey)
                            profile.box.$drop[cachekey] = drop;

                        o.render();
                    }

                    o = drop.boxing();
                    o.setHost(profile);

                    //set pop
                    switch (type) {
                        case 'combobox':
                        case 'listbox':
                        case 'helpinput':
                        case 'time':
                            o.setValue(profile.properties.$UIvalue, true, 'pop');
                            break;
                        case 'date':
                        case 'datetime':
                            var t = drop.properties;
                            if (t = profile.properties.$UIvalue)
                                o.setValue(new Date(parseInt(t, 10)), true, 'pop');
                            break;
                        case 'color':
                            o.setValue(profile.properties.$UIvalue.replace('#', ''), true, 'pop');
                            break;
                    }

                    profile.boxing().setPopWnd(o);

                    if (!ignoreEvent && profile.beforePopShow && false === box.beforePopShow(profile, drop, profile.properties.items))
                        return;
                    //pop
                    var node = o.reBoxing(), pid = pro.parentID || ood.ini.$rootContainer;
                    node.popToTop(baseNode || profile.getSubNode('BOX'), null,
                        pid ? ood.get(profile, ["host", pid]) ? profile.host[pid].getContainer() : ood(pid) : null);

                    ood.tryF(o.activate, [], o);

                    //for on blur disappear
                    var sid = profile.key + ":" + profile.$xid;
                    node.setBlurTrigger(sid, function () {
                        box._cache('blur');
                        ood.Event.keyboardHook('esc', 0, 0, 0, sid);
                    });

                    //for esc
                    ood.Event.keyboardHook('esc', 0, 0, 0, function () {
                        profile.$escclosedrop = 1;
                        ood.asyRun(function () {
                            delete profile.$escclosedrop;
                        });

                        box.activate();
                        box._cache('esc', true);

                        //unhook
                        ood.Event.keyboardHook('esc', 0, 0, 0, sid);
                        return false;
                    }, sid, null, null, profile.domId);
                } else if (type == 'file') {
                    profile.boxing().popFileSelector();
                }

                if (!ignoreEvent && profile.afterPopShow)
                    box.afterPopShow(profile, drop);
            });
        },
        expand: function (node, ignoreEvent, e) {
            var profile = this.get(0);
            if (profile.renderId)
                profile.boxing()._drop(e, node, node, ignoreEvent);
        },
        collapse: function () {
            var profile = this.get(0);
            if (profile.renderId && profile.$poplink)
                profile.boxing()._cache('call');
        },
        getPopWnd: function () {
            var profile = this.get(0);
            if (profile && profile.$poplink)
                return profile.$poplink.boxing();
        }
    },
    /*Initialize*/
    Initialize: function () {
        var ns = this;
        ns.addTemplateKeys(['ICONB', 'ICON', 'UNIT', 'FILE', 'LMID', 'RMID', 'LBTN', 'RBTN', 'SPINBTN', 'R1', 'R1B', 'R2', 'R2B']);
        //modify default template for shell
        var t = ns.getTemplate();
        ood.merge(t.FRAME.BORDER, {
            LBTN: {},
            RBTN: {},
            SPINBTN: {R1: {}, R2: {}},
            CMD: {
                $order: 50,
                tagName: 'button',
                className: 'ood-ui-unselectable ood-uiborder-radius-tr ood-uiborder-radius-br ood-uiborder-noradius-l ood-nofocus ood-ui-btn ood-uibar ood-uigradient',
                style: "{_cmdDisplay}",
                SMID: {
                    className: "oodfont",
                    $fonticon: '{_fi_commandCls}'
                }
            }
        }, 'all');
        var box = t.FRAME.BORDER.BOX;
        box.className = 'ood-ui-input ood-ui-shadow-input ood-uiborder-flat ood-uibase {_radius_input} ';
        box.ICONB = {
            tagName: 'button',
            className: 'ood-ui-unselectable ood-nofocus ood-ui-clear',
            tabindex: '-1',
            ICON: {
                className: 'oodfont {imageClass}  {picClass}',
                //for cover oodcon
                style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                text: '{iconFontCode}'
            }
        };
        box.UNIT = {
            tagName: 'button',
            tabindex: '-1',
            className: 'ood-ui-unselectable ood-nofocus',
            text: '{unit}'
        };

        t.FRAME.POOL = {};
        t.className += ' {typecls}';

        ns.setTemplate(t);
        ns._adjustItems = ood.absList._adjustItems;

        var a = ns.prototype, b = ood.absList.prototype;
        a.getItems = b.getItems;
        a.getItemByItemId = b.getItemByItemId;
    },
    Static: {
        _beforeResetValue: function (profile) {
            profile.properties.caption = undefined;
        },
        _iniType: function (profile) {
            var pro = profile.properties, type = pro.type, c = profile.box;
            delete profile.$beforeKeypress;
            delete profile.$inputReadonly;
            delete profile.$isNumber;
            delete profile.$compareValue;
            delete profile.$getShowValue;
            delete profile.$toEditor;
            delete profile.$fromEditor;
            delete profile.$typeOK;

            if (type == 'listbox' || type == 'file' || type == 'cmdbox' || type == 'button' || type == 'dropbutton')
                profile.$inputReadonly = true;

            if (type == 'file')
                profile.$oodFileCtrl = true;

            if (type != 'listbox' && type != 'combobox' && type != 'helpinput')
                pro.items = [];

            if (type == 'time') {
                var keymap = {a: 1, c: 1, v: 1, x: 1};
                ood.merge(profile, {
                    $beforeKeypress: function (p, c, k) {
                        return k.key.length != 1 || /[-0-9:]/.test(k.key) || (k.ctrlKey && !!keymap[k.key]);
                    },
                    $getShowValue: function (p, v) {
                        return v ? ood.UI.TimePicker._ensureValue(p, v) : '';
                    },
                    $fromEditor: function (p, v) {
                        if (v) {
                            v = ood.UI.TimePicker._ensureValue(p, v);
                            if (v == '00:00') v = p.properties.$UIvalue;
                        }
                        return v;
                    }
                }, 'all');
            } else if (type == 'date' || type == 'datetime') {
                var date = ood.Date;
                var keymap = {a: 1, c: 1, v: 1, x: 1};
                ood.merge(profile, {
                    $beforeKeypress: function (p, c, k) {
                        return k.key.length != 1 || /[0-9:/\-_ ]/.test(k.key) || (k.ctrlKey && !!keymap[k.key]);
                    },
                    $compareValue: function (p, a, b) {
                        return (!a && !b) || (String(a) == String(b))
                    },
                    $getShowValue: function (p, v) {
                        if (p.properties.dateEditorTpl)
                            return v ? date.format(v, p.properties.dateEditorTpl) : '';
                        else
                            return v ? date.getText(new Date(parseInt(v, 10)), p.properties.type == 'datetime' ? 'ymdhn' : 'ymd') : '';
                    },
                    $toEditor: function (p, v) {
                        if (!v) return "";

                        v = new Date(parseInt(v, 10) || 0);
                        if (p.properties.dateEditorTpl)
                            return date.format(v, p.properties.dateEditorTpl);
                        else {
                            var m = (date.get(v, 'm') + 1) + '', d = date.get(v, 'd') + '', h = date.get(v, 'h') + '',
                                n = date.get(v, 'n') + '';
                            return date.get(v, 'y') + '-' + (m.length == 1 ? '0' : '') + m + '-' + (d.length == 1 ? '0' : '') + d

                                + (p.properties.type == 'datetime' ? (" " + (h.length == 1 ? '0' : '') + h + ":" + (n.length == 1 ? '0' : '') + n) : "");
                        }
                    },
                    $fromEditor: function (p, v) {
                        if (v) {
                            if (p.properties.dateEditorTpl)
                                v = date.parse(v, p.properties.dateEditorTpl);
                            else
                                v = ood.Date.parse(v);
                            // set to old UIvalue
                            if (!v) {
                                v = p.properties.$UIvalue;
                                if (ood.isFinite(v)) v = new Date(parseInt(v, 10));
                            }
                            if (v) {
                                if (p.properties.type != 'datetime')
                                    v = date.getTimSpanStart(v, 'd', 1);
                                // min/max year
                                if (v.getFullYear() < p.properties.min)
                                    v.setTime(p.properties.min);
                                if (v.getFullYear() > p.properties.max)
                                    v.setTime(p.properties.max);
                            }
                        }
                        return v ? String(v.getTime()) : '';
                    }
                }, 'all');
            } else if (type == 'currency') {
                profile.$isNumber = 1;
                var keymap = {a: 1, c: 1, v: 1, x: 1};
                ood.merge(profile, {
                    $beforeKeypress: function (p, c, k) {
                        return k.key.length != 1 || /[-0-9,. ]/.test(k.key) || (k.ctrlKey && !!keymap[k.key]);
                    },
                    $compareValue: function (p, a, b) {
                        return ((a === '' && b !== '') || (b === '' && a !== '')) ? false : p.box._number(p, a) == p.box._number(p, b)
                    },
                    $getShowValue: function (p, v) {
                        var pp = p.properties;
                        if (ood.isSet(v) && v !== "") {
                            v = ood.formatNumeric(p.box._number(p, v), pp.precision, pp.groupingSeparator, pp.decimalSeparator, pp.forceFillZero, pp.trimTailZero);
                            if (p.properties.currencyTpl)
                                v = p.properties.currencyTpl.replace("*", v);
                        } else
                            v = "";
                        return v;
                    },
                    $toEditor: function (p, v) {
                        var pp = p.properties;
                        return (ood.isSet(v) && v !== "") ? ood.formatNumeric(p.box._number(p, v), pp.precision, pp.groupingSeparator, pp.decimalSeparator, pp.forceFillZero, pp.trimTailZero) : "";
                    },
                    $fromEditor: function (p, v) {
                        return (ood.isSet(v) && v !== "") ? p.box._number(p, v) : "";
                    }
                }, 'all');
            } else if (type == 'number' || type == 'spin' || type == 'counter') {
                profile.$isNumber = 1;
                var keymap = {a: 1, c: 1, v: 1, x: 1};
                ood.merge(profile, {
                    $beforeKeypress: function (p, c, k) {
                        return k.key.length != 1 || /[-0-9. ]/.test(k.key) || (k.ctrlKey && !!keymap[k.key]);
                    },
                    $compareValue: function (p, a, b) {
                        return ((a === '' && b !== '') || (b === '' && a !== '')) ? false : p.box._number(p, a) == p.box._number(p, b)
                    },
                    $getShowValue: function (p, v) {
                        var pp = p.properties;
                        v = (ood.isSet(v) && v !== "") ? ood.formatNumeric(p.box._number(p, v), pp.precision, pp.groupingSeparator, pp.decimalSeparator, pp.forceFillZero, pp.trimTailZero) : "";
                        if (v != "" && p.properties.numberTpl)
                            v = p.properties.numberTpl.replace("*", v);
                        return v;
                    },
                    $toEditor: function (p, v) {
                        var pp = p.properties;
                        return (ood.isSet(v) && v !== "") ? ood.formatNumeric(p.box._number(p, v), pp.precision, pp.groupingSeparator, pp.decimalSeparator, pp.forceFillZero, pp.trimTailZero) : "";
                    },
                    $fromEditor: function (p, v) {
                        return (ood.isSet(v) && v !== "") ? p.box._number(p, v) : "";
                    }
                }, 'all');
            }

            if (pro.value)
                pro.$UIvalue = pro.value = c._ensureValue(profile, pro.value);

            profile.$typeOK = true;
        },
        $drop: {},
        Appearances: {
            POOL: {
                position: 'absolute',
                left: 0,
                top: 0,
                width: 0,
                height: 0,
                display: 'none',
                visibility: 'hidden'
            },
            FILE: {
                visibility: 'hidden',
                'z-index': 30,
                border: 0,
                width: '100%',
                height: '100%',
                position: 'absolute',
                padding: 0,
                top: 0,
                right: 0,
                cursor: 'pointer',
                overflow: 'hidden'
            },
            'KEY-type-number INPUT, KEY-type-spin INPUT, KEY-type-counter INPUT, KEY-type-currency INPUT': {
                $order: 4,
                'text-align': 'right'
            },
            'KEY-type-counter INPUT': {
                $order: 4,
                'text-align': 'center'
            },
            'KEY-type-file INPUT, KEY-type-button INPUT, KEY-type-dropbutton INPUT, KEY-type-cmdbox INPUT, KEY-type-listbox INPUT': {
                $order: 4,
                cursor: 'pointer',
                'text-align': 'left',
                'white-space': 'normal',
                overflow: 'hidden'
            },
            'KEY-type-button INPUT, KEY-type-dropbutton INPUT': {
                $order: 5,
                'text-align': 'center'
            },
            'LBTN,RBTN,SPINBTN,CMD': {
                display: 'block',
                'z-index': 20,
                cursor: 'pointer',
                padding: 0,
                position: 'absolute',
                width: '1.5em',

                // for IE8
                overflow: 'visible'
            },
            'ICONB, UNIT': {
                'z-index': 20,
                cursor: 'pointer',
                position: 'absolute',
                padding: 0,
                margin: 0,
                border: 0,
                background: 'none',
                height: '100%',
                padding: '0 2px',

                // for IE67
                display: ood.$inlineBlock,
                zoom: ood.browser.ie67 ? 1 : null,
                width: (ood.browser.ie && ood.browser.ver <= 7) ? 'auto' : null,
                'overflow': (ood.browser.ie && ood.browser.ver <= 7) ? 'visible' : null
            },
            ICONB: {
                left: 0,
                top: 0
            },
            ICON: {
                // for right size in onresize
                'color': 'var(--text-input)  !important',
                width: '1em'
            },
            UNIT: {
                top: 0,
                right: 0
            },
            CMD: {
                $order: 2,
                'z-index': 22,
                padding: 0
            },
            'R1,R2': {
                $order: 1,
                display: 'block',
                cursor: 'pointer',
                padding: 0,
                position: 'absolute',
                height: '50%',
                width: '1.5em',

                // for IE8
                overflow: 'visible'
            },

            INPUT: {
                $order: 100,
                'padding-left': '1em',
                'background-color': 'var(--bg-input) !important',
                'color': 'var(--text-input)  !important'
            },

            R1: {
                top: 0
            },
            R2: {
                bottom: 0
            },
            'R1B,R2B': {
                cursor: 'pointer',
                position: 'absolute',
                left: 0,
                top: '50%',
                height: '6px',
                'margin-top': '-2px',
                padding: 0,
                'z-index': 2
            },
            'SMID,LMID,RMID': {
                $order: 2,
                cursor: 'pointer',
                padding: 0,
                left: 0
            }
        },

        _objectProp: {popCtrlProp: 1, popCtrlEvents: 1},
        Behaviors: {
            HoverEffected: {BOX: 'BOX', ICON: 'ICON', ICONB: 'ICONB'},
            ClickEffected: {BOX: 'BOX'},
            ICONB: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    if (profile.onClickIcon) profile.boxing().onClickIcon(profile, src);
                }
            },
            UNIT: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    if (!prop.units) return;
                    var o = ood.create('List', {
                        dirtyMark: false,
                        items: prop.units.split(/[,;\:]/),
                        width: 'auto',
                        height: 'auto',
                        value: prop.unit
                    });
                    o.afterClick(function () {
                        o.destroy(true);
                        return false;
                    });
                    o.beforeUIValueSet(function (p, o, v) {
                        profile.boxing().setUnit(ood.str.trim(v));
                    });
                    o.render();
                    //pop
                    var node = o.reBoxing(), pid = prop.parentID || ood.ini.$rootContainer;
                    node.popToTop(src, null,
                        pid ? ood.get(profile, ["host", pid]) ? profile.host[pid].getContainer() : ood(pid) : null);
                    ood.tryF(o.activate, [], o);
                    var sid = profile.key + ":unit:" + profile.$xid;
                    node.setBlurTrigger(sid, function () {
                        o.destroy();
                        ood.Event.keyboardHook('esc', 0, 0, 0, sid);
                    });
                    ood.Event.keyboardHook('esc', 0, 0, 0, function () {
                        o.destroy();
                        ood.Event.keyboardHook('esc', 0, 0, 0, sid);
                        return false;
                    }, sid, null, null, profile.domId);
                }
            },
            FILE: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    if (profile.onFileDlgOpen) profile.boxing().onFileDlgOpen(profile, src);
                },
                onChange: function (profile, e, src) {
                    var prop = profile.properties,
                        input = ood.use(src).get(0),
                        value = input.value + '';
                    if (prop.type == 'file') {
                        var arr = [];
                        for (var i = 0, f = input.files, l = f.length; i < l; i++) arr.push(f[i].name);
                        value = arr.length ? '"' + arr.join('", "') + '"' : '';
                    }
                    profile.boxing().setUIValue(value, null, null, 'onchange');
                }
            },
            LBTN: {
                onMousedown: function (profile) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    profile.box._spin(profile, false);
                },
                onMouseout: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                },
                onMouseup: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                }
            },
            RBTN: {
                onMousedown: function (profile) {
                    var prop = profile.properties, type = prop.type;
                    if (type != 'counter') return;

                    if (prop.disabled || prop.readonly) return;
                    profile.box._spin(profile, true);
                },
                onMouseout: function (profile) {
                    if (profile.properties.type != 'counter') return;
                    ood.Thread.abort(profile.$xid + ':spin');
                },
                onMouseup: function (profile) {
                    if (profile.properties.type != 'counter') return;
                    ood.Thread.abort(profile.$xid + ':spin');
                },
                onClick: function (profile, e, src) {
                    var prop = profile.properties, type = prop.type;
                    if (type == 'counter') return;

                    if (type == 'popbox' || type == 'cmdbox' || type == 'listbox' || type == 'getter' || type == 'dropbutton') {
                        if (profile.onClick && false === profile.boxing().onClick(profile, e, src, 'right', prop.$UIvalue))
                            return;
                    }
                    if (type == 'file') {
                        profile.boxing().popFileSelector();
                        return;
                    }

                    if (prop.disabled || prop.readonly) return;
                    profile.boxing()._drop(e, src);
                    return false;
                }
            },
            CMD: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    if (profile.onCommand && false === profile.boxing().onCommand(profile, src, prop.commandBtn))
                        return;
                    if (prop.commandBtn == 'delete' || prop.commandBtn == 'remove')
                        profile.boxing().setUIValue('', true, null, 'cmd');
                }
            },
            BOX: {
                onClick: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.type == 'cmdbox' || prop.type == 'button' || prop.type == 'dropbutton') {
                        if (profile.onClick)
                            profile.boxing().onClick(profile, e, src, 'left', prop.$UIvalue);
                        //DOM node's readOnly
                    } else if (prop.inputReadonly || profile.$inputReadonly) {
                        if (prop.disabled || prop.readonly) return;
                        profile.boxing()._drop(e, src);
                    }
                }
            },
            INPUT: {
                onClick: function (profile, e, src) {
                    // for grid cell editor 'enter' bug: trigger list pop again
                    if (profile.onClick) {
                        var prop = profile.properties;
                        profile.boxing().onClick(profile, e, src, 'left', prop.$UIvalue);
                    }

                    if (e.$cell) {
                        e = ood.Event.getPos(e);
                        if (e.left === 0 && e.top === 0) return false;
                    }
                },
                onChange: function (profile, e, src) {
                    if (profile.$_onedit || profile.$_inner || profile.destroyed || !profile.box) return;
                    var o = profile._inValid,
                        p = profile.properties, b = profile.box,
                        instance = profile.boxing(),
                        v = instance._fromEditor(ood.use(src).get(0).value),
                        uiv = p.$UIvalue;
                    if (!instance._compareValue(uiv, v)) {
                        //give a invalid value in edit mode
                        if (v === null)
                            instance._setCtrlValue(uiv);
                        else {
                            // trigger events
                            instance.setUIValue(v, null, null, 'onchange');
                            // input/textarea is special, ctrl value will be set before the $UIvalue
                            if (p.$UIvalue !== v) instance._setCtrlValue(p.$UIvalue);
                            if (o !== profile._inValid) if (profile.renderId) instance._setDirtyMark();
                        }
                    }
                    b._asyCheck(profile);
                },
                onKeyup: function (profile, e, src) {
                    var p = profile.properties, b = profile.box,
                        key = ood.Event.getKey(e);
                    if (p.disabled || p.readonly) return false;
                    if (profile.$inputReadonly || p.inputReadonly) return;

                    // must be key up event
                    if (key.key == 'esc') {
                        if (profile.$escclosedrop) {
                            return;
                        }

                        profile.boxing()._setCtrlValue(p.$UIvalue);
                        if (profile.onCancel)
                            profile.boxing().onCancel(profile);
                    }

                    if (p.dynCheck) {
                        var value = ood.use(src).get(0).value;
                        profile.box._checkValid(profile, value);
                        profile.boxing()._setDirtyMark();
                    }
                    b._asyCheck(profile);

                    if (key.key == 'down' || key.key == 'up') {
                        if (p.type == 'spin' || p.type == 'counter') {
                            ood.Thread.abort(profile.$xid + ':spin');
                            return false;
                        }
                    }
                },
                onMousedown: function (profile, e, src) {
                    profile._mousedownmark = 1;
                    ood.asyRun(function () {
                        if (profile) delete profile._mousedownmark;
                    });
                },
                onMouseup: function (profile, e, src) {
                    if (profile.properties.selectOnFocus && profile._stopmouseupcaret) {
                        var node = ood.use(src).get(0);
                        if (!node.readOnly && node.select) {
                            profile.$mouseupDelayFun = ood.asyRun(function () {
                                delete profile.$mouseupDelayFun;
                                if (node.tagName.toLowerCase() == "input" || !/[\n\r]/.test(node.value)) node.select();
                            })
                        }
                        delete profile._stopmouseupcaret;
                    }
                },
                onFocus: function (profile, e, src) {
                    if (profile.$ignoreFocus) return false;
                    if (profile.beforeFocus && false === profile.boxing().beforeFocus(profile)) {
                        profile.$ignoreBlur = 1;
                        ood(src).blur();
                        delete profile.$ignoreBlur;
                        return false;
                    }
                    var p = profile.properties, b = profile.box;
                    if (p.disabled || p.readonly) return false;
                    if (profile.onFocus) profile.boxing().onFocus(profile);
                    if (profile.$inputReadonly || p.inputReadonly) return;
                    profile.getSubNode('BORDER').tagClass('-focus');

                    var instance = profile.boxing(),
                        uiv = p.$UIvalue,
                        v = instance._toEditor(uiv),
                        node = ood.use(src).get(0),
                        nodev = node.value;

                    // if _toEditor adjust value, ensure node value
                    if (uiv !== v && nodev !== v) {
                        profile.$_onedit = true;
                        node.value = v;
                        delete profile.$_onedit;
                    }

                    //if no value, add mask
                    if (p.mask) {
                        var value = node.value;
                        if (!value) {
                            profile.$focusDelayFun = ood.asyRun(function () {
                                // destroyed
                                if (!profile.box) return;
                                delete profile.$focusDelayFun;
                                profile.$_onedit = true;
                                profile.boxing()._setCtrlValue(value = profile.$Mask);
                                delete profile.$_onedit;
                                b._setCaret(profile, node);
                            });
                        }
                    }
                    if (p.selectOnFocus && !node.readOnly && node.select) {
                        if (ood.browser.kde) {
                            profile.$focusDelayFun2 = ood.asyRun(function () {
                                delete profile.$focusDelayFun2;
                                if (node.tagName.toLowerCase() == "input" || !/[\n\r]/.test(node.value)) node.select();
                            });
                        } else {
                            if (node.tagName.toLowerCase() == "input" || !/[\n\r]/.test(node.value)) node.select();
                        }
                        // if focus was triggerred by mousedown, try to stop mouseup's caret
                        if (profile._mousedownmark) profile._stopmouseupcaret = 1;
                    }
                    //show tips color
                    profile.boxing()._setTB(3);

                    b._asyCheck(profile);
                },
                onBlur: function (profile, e, src) {
                    if (profile.$ignoreBlur) return false;
                    ood.resetRun(profile.$xid + ":asycheck");
                    if (profile.$focusDelayFun) ood.clearTimeout(profile.$focusDelayFun);
                    if (profile.$focusDelayFun2) ood.clearTimeout(profile.$focusDelayFun2);
                    if (profile.$focusDelayFun2) ood.clearTimeout(profile.$mouseupDelayFun);

                    var p = profile.properties;
                    if (p.disabled || p.readonly) return false;
                    if (profile.onBlur) profile.boxing().onBlur(profile);
                    if (profile.$inputReadonly || p.inputReadonly) return;

                    var b = profile.box,
                        instance = profile.boxing(),
                        uiv = p.$UIvalue,
                        v = ood.use(src).get(0).value;

                    if (profile.$Mask && profile.$Mask == v) {
                        v = "";
                        uiv = profile.$Mask;
                    }
                    v = instance._fromEditor(v);

                    profile.getSubNode('BORDER').tagClass('-focus', false);

                    //onblur check it
                    if (instance._compareValue(p.$UIvalue, v)) {
                        profile.box._checkValid(profile, v);
                        instance._setCtrlValue(uiv);
                    }
                    instance._setDirtyMark();
                    b._asyCheck(profile, false);
                },
                onKeydown: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled || p.readonly) return;
                    var b = profile.box,
                        m = p.multiLines,
                        evt = ood.Event,
                        k = evt.getKey(e);

                    //fire onchange first
                    if (k.key == 'enter' && (!m || k.altKey) && !p.inputReadonly && !profile.$inputReadonly) {
                        profile.$_onedit = true;
                        profile.boxing().setUIValue(profile.boxing()._fromEditor(ood.use(src).get(0).value), true, null, 'enter');
                        profile.$_onedit = false;
                    }

                    b._asyCheck(profile);

                    if (p.mask) {
                        if (k.key.length > 1) profile.$ignore = true;
                        else delete profile.$ignore;
                        switch (k.key) {
                            case 'backspace':
                                b._changeMask(profile, ood.use(src).get(0), '', false);
                                return false;
                            case 'delete':
                                b._changeMask(profile, ood.use(src).get(0), '');
                                return false;
                        }
                    }

                    if (k.key == 'down' || k.key == 'up') {
                        if (p.type == 'spin' || p.type == 'counter') {
                            if (!k.ctrlKey) {
                                profile.box._spin(profile, k.key == 'up');
                                return false;
                            }
                        } else if (k.ctrlKey && p.type != 'none' && p.type != 'input' && p.type != 'password') {
                            profile.boxing()._drop(e, src);
                            return false;
                        }
                    }
                },
                onDblclick: function (profile, e, src) {
                    profile.getSubNode('RBTN').onClick(true);
                }
            },
            R1: {
                onMousedown: function (profile) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    profile.box._spin(profile, true);
                },
                onMouseout: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                },
                onMouseup: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                }
            },
            R2: {
                onMousedown: function (profile) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;
                    profile.box._spin(profile, false);
                },
                onMouseout: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                },
                onMouseup: function (profile) {
                    ood.Thread.abort(profile.$xid + ':spin');
                }
            }
        },
        EventHandlers: {
            onFileDlgOpen: function (profile, src) {
            },
            onCommand: function (profile, src, type) {
            },
            beforeComboPop: function (profile, pos, e, src) {
            },
            beforePopShow: function (profile, popCtl, items) {
            },
            afterPopShow: function (profile, popCtl) {
            },
            afterPopHide: function (profile, popCtl, type) {
            },
            onClick: function (profile, e, src, btn, value) {
            },
            onClickIcon: function (profile, src) {
            },
            beforeUnitUpdated: function (prfole, unit) {
            },
            afterUnitUpdated: function (prfole, unit) {
            }
        },
        DataModel: {
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
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
            
            cachePopWnd: {
                ini: true,
                caption: ood.getResText("DataModel.cachePopWnd") || "缓存弹出窗口"
            },
            bindClass: {
                ini: "",
                caption: ood.getResText("DataModel.bindClass") || "绑定样式类"
            },
            expression: {
                ini: '',
                caption: ood.getResText("DataModel.expression") || "表达式",
                action: function () {
                }
            },

            // allowed: yyyy,mm,dd,y,m,d
            // yyyy-mm-dd
            // yyyy/mm/dd

            filter: {
                ini: "",
                caption: ood.getResText("DataModel.filter") || "过滤器"
            },
            itemsExpression: {
                ini: "",
                caption: ood.getResText("DataModel.itemsExpression") || "项目表达式"
            },
            dateEditorTpl: {
                ini: "",
                caption: ood.getResText("DataModel.dateEditorTpl") || "日期编辑器模板"
            },
            groupingSeparator: {
                ini: ",",
                caption: ood.getResText("DataModel.groupingSeparator") || "分组分隔符"
            },
            decimalSeparator: {
                ini: ".",
                caption: ood.getResText("DataModel.decimalSeparator") || "小数分隔符"
            },
            forceFillZero: {
                ini: true,
                caption: ood.getResText("DataModel.forceFillZero") || "强制补零"
            },
            trimTailZero: {
                ini: false,
                caption: ood.getResText("DataModel.trimTailZero") || "修剪尾部零"
            },
            parentID: {
                ini: "",
                caption: ood.getResText("DataModel.parentID") || "父级ID"
            },
            enumClass: {
                ini: "",
                caption: ood.getResText("DataModel.enumClass") || "枚举类"
            },
            popCtrlProp: {
                ini: {},
                caption: ood.getResText("DataModel.popCtrlProp") || "弹出控件属性"
            },
            popCtrlEvents: {
                ini: {},
                caption: ood.getResText("DataModel.popCtrlEvents") || "弹出控件事件"
            },
            image: {
                format: 'image',
                caption: ood.getResText("DataModel.image") || "图像",
                action: function () {
                    ood.UI.$iconAction(this);
                    this.boxing().reLayout(true);
                }
            },
            imagePos: {
                caption: ood.getResText("DataModel.imagePos") || "图像位置",
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundPosition', value || 'center');
                }
            },
            imageBgSize: {
                caption: ood.getResText("DataModel.imageBgSize") || "图像背景大小",
                action: function (value) {
                    this.getSubNode('ICON').css('backgroundSize', value || '');
                }
            },
            imageClass: {
                ini: '',
                caption: ood.getResText("DataModel.imageClass") || "图像样式类",
                action: function (v, ov) {
                    ood.UI.$iconAction(this, 'ICON', ov);
                    this.boxing().reLayout(true);
                }
            },
            iconFontCode: {
                caption: ood.getResText("DataModel.iconFontCode") || "图标字体代码",
                action: function (v) {
                    ood.UI.$iconAction(this);
                    this.boxing().reLayout(true);
                }
            },
            dropImageClass: {
                caption: ood.getResText("DataModel.dropImageClass") || "下拉图标样式类",
                action: function () {
                    this.boxing().refresh();
                }
            },
            unit: {
                ini: "",
                caption: ood.getResText("DataModel.unit") || "单位",
                set: function (v) {
                    var ns = this;
                    if (ns.beforeUnitUpdated && false === ns.boxing().beforeUnitUpdated(ns, v))
                        return;
                    ns.properties.unit = v;
                    if (ns.renderId) {
                        ns.getSubNode('UNIT').html(v);
                        ns.boxing().reLayout(true);
                    }
                    if (ns.afterUnitUpdated) ns.boxing().afterUnitUpdated(ns, v);
                }
            },
            units: {
                ini: '',
                caption: ood.getResText("DataModel.units") || "单位列表"
            },
            numberTpl: {
                ini: "",
                caption: ood.getResText("DataModel.numberTpl") || "数字模板",
                action: function () {
                    this.boxing().setUIValue(this.properties.$UIvalue, true, null, 'tpl');
                }
            },
            currencyTpl: {
                ini: "$ *",
                caption: ood.getResText("DataModel.currencyTpl") || "货币模板",
                action: function () {
                    this.boxing().setUIValue(this.properties.$UIvalue, true, null, 'tpl');
                }
            },
            listKey: {
                caption: ood.getResText("DataModel.listKey") || "列表键",
                set: function (value) {
                    var t = ood.UI.getCachedData(value),
                        o = this;
                    o.boxing().setItems(t ? ood.clone(t) : o.properties.items);
                    o.properties.listKey = value;
                }
            },
            dropListWidth: {
                ini: 0,
                caption: ood.getResText("DataModel.dropWidth") || "下拉列表宽度"
            },
            dropListHeight: {
                ini: 0,
                caption: ood.getResText("DataModel.dropHeight") || "下拉列表高度"
            },
            items: {
                ini: [],
                caption: ood.getResText("DataModel.items") || "项目列表",
                set: function (value) {
                    var o = this;
                    value = o.properties.items = o.box._adjustItems(value);
                    if (o.renderId) {
                        //clear those
                        o.SubSerialIdMapItem = {};
                        o.ItemIdMapSubSerialId = {};
                        o.box._prepareItems(o, value);

                        // if popped
                        if (o.$poplink && o.$poplink.box)
                            o.$poplink.boxing().setItems(value);
                        else
                            o.boxing().clearPopCache();
                    }
                }
            },
            type: {
                ini: 'combobox',
                listbox: ood.toArr('none,input,password,combobox,listbox,file,getter,helpinput,button,dropbutton,cmdbox,popbox,date,time,datetime,color,spin,counter,currency,number'),
                caption: ood.getResText("DataModel.type") || "类型",
                set: function (value) {
                    var pro = this;
                    pro.properties.type = value;
                    if (pro.renderId)
                        pro.boxing().refresh(true);
                }
            },
            showMode: {
                ini: 'normal',
                listbox: ['', 'normal', 'compact', 'transparent'],
                caption: ood.getResText("DataModel.displayMode") || "显示模式",
                action: function () {
                    this.boxing().refresh()
                }
            },
            // for number&currency
            precision: {
                ini: 2,
                caption: ood.getResText("DataModel.precision") || "精度"
            },
            increment: {
                ini: 0.01,
                caption: ood.getResText("DataModel.increment") || "增量值"
            },
            min: {
                ini: -Math.pow(10, 15),
                caption: ood.getResText("DataModel.minValue") || "最小值"
            },
            // big number for date
            max: {
                ini: Math.pow(10, 15),
                caption: ood.getResText("DataModel.maxValue") || "最大值"
            },
            commandBtn: {
                ini: "none",
                combobox: ood.toArr("none,save,delete,add,remove,pop,select,search,function"),
                caption: ood.getResText("DataModel.cmdButtons") || "命令按钮",
                action: function () {
                    this.boxing().refresh();
                }
            },
            disabled: {
                ini: false,
                caption: ood.getResText("DataModel.disabled") || "禁用",
                action: function (v) {
                    this.box._handleInput(this, "ood-ui-disabled", v);
                }
            },
            inputReadonly: {
                ini: false,
                caption: ood.getResText("DataModel.inputReadonly") || "输入只读",
                action: function (v) {
                    this.box._handleInput(this, "ood-ui-inputreadonly", v);
                }
            },
            readonly: {
                ini: false,
                caption: ood.getResText("DataModel.readonly") || "只读",
                action: function (v) {
                    this.box._handleInput(this, "ood-ui-readonly", v);
                }
            },
            // caption is for readonly comboinput(listbox/cmdbox are readonly)
            caption: {
                ini: null,
                caption: ood.getResText("DataModel.title") || "标题",
                set: function (v) {
                    var p = this.properties;
                    p.caption = v;

                    if (ood.isSet(v)) {
                        v = v + "";
                        p.caption = ood.adjustRes(v, false);
                    }
                    if (this.renderId) {
                        if (this.$inputReadonly || p.inputReadonly) {
                            this.getSubNode('INPUT').attr("value", this.boxing().getShowValue());
                        }
                    }
                },
                get: function () {
                    return this.boxing().getShowValue();
                }
            },
            fileAccept: {
                ini: "",
                caption: ood.getResText("DataModel.accept") || "文件接受类型"
            },
            fileMultiple: {
                ini: false,
                caption: ood.getResText("DataModel.multiple") || "允许多文件"
        }
        },
        RenderTrigger: function () {
            var self = this,
                instance = self.boxing(),
                p = self.properties;
            self.box._iniType(self);

            if (p.readonly)
                instance.setReadonly(true, true);
            else if (p.inputReadonly){
                instance.setInputReadonly(true, true);
            }

                
            // 现代化功能初始化
            ood.asyRun(function(){
                if (self.boxing() && self.boxing().ComboInputTrigger){
                    self.boxing().ComboInputTrigger();
                }
            });

        },
        

        _spin: function (profile, flag) {
            if (profile.$inDesign) return;

            var id = profile.$xid + ':spin';
            if (ood.Thread.isAlive(id)) return;
            var prop = profile.properties,
                increment = Math.max(prop.increment, Math.pow(10, -prop.precision)),
                off = increment * (flag ? 1 : -1),
                task = {delay: 300},
                fun = function () {
                    if (profile.destroyed) return false;
                    var v = ((+prop.$UIvalue) || 0) + off;
                    v = (ood.isSet(v) && v !== "") ? ood.formatNumeric(profile.box._number(profile, v), prop.precision, prop.groupingSeparator, prop.decimalSeparator, prop.forceFillZero, prop.trimTailZero) : "";
                    profile.boxing().setUIValue(v, null, null, 'spin');
                    task.delay *= 0.9;
                };
            task.task = fun;
            ood.Thread(id, [task], 500, null, fun, null, true).start();
        },
        _dynamicTemplate: function (profile) {
            var properties = profile.properties,
                type = properties.type,
                multiLines = properties.multiLines,
                fileAccept = properties.fileAccept,
                fileMultiple = properties.fileMultiple,
                showMode = properties.showMode,
                hash = profile._exhash = "$" +
                    'multiLines:' + multiLines + ';' +
                    'type:' + type + ';' +
                    'mode:' + showMode + ';',
                template = profile.box.getTemplate(hash),
                adj = function (s) {
                    return (!showMode || showMode == 'normal') ? s : 'ood-ui-clear ' + s.replace(/\b(ood-ui-btn|ood-uibar|ood-uigradient|ood-uibase)\b/g, '');
                }

            properties.$UIvalue = properties.value;

            // set template dynamic
            if (!template) {
                template = ood.clone(profile.box.getTemplate());
                var t = template.FRAME.BORDER,
                    ip = t.BOX.WRAP.INPUT;

                delete t.LBTN;
                delete t.RBTN;
                delete t.SPINBTN;

                ip.tagName = 'input';
                ip.type = 'text';
                switch (type) {
                    case "none":
                    case "input":
                    case "number":
                    case "currency":
                        break;
                    case 'button':
                        ip.type = 'button';
                        break;
                    case 'password':
                        ip.type = 'password';
                        break;
                    // spin has spin buttons
                    case 'spin':
                        t.SPINBTN = {
                            $order: 20,
                            className: 'ood-ui-unselectable',
                            style: "{rDisplay}",
                            R1: {
                                tagName: 'button',
                                className: adj('ood-ui-btn ood-uibar ood-uigradient ood-nofocus {_radius_dropt}'),
                                R1B: {
                                    className: 'oodfont',
                                    $fonticon: 'ood-icon-smallup'
                                }
                            },
                            R2: {
                                tagName: 'button',
                                className: adj('ood-ui-btn ood-uibar ood-uigradient ood-nofocus {_radius_dropb}'),
                                R2B: {
                                    className: 'oodfont',
                                    $fonticon: 'ood-icon-smalldown'
                                }
                            }
                        };
                        break;
                    // following have RBTN button
                    case 'counter':
                        t.LBTN = {
                            $order: 1,
                            tagName: 'button',
                            className: adj('ood-ui-unselectable ood-ui-btn ood-uibar ood-uigradient ood-nofocus {_radius_dropl}'),
                            style: "{_btnlDisplay}",
                            LMID: {
                                className: 'oodfont',
                                $fonticon: '{_fi_btnlClass}',
                                style: '{_btnlStyle}'
                            }
                        };
                        break;
                    case 'file':
                        t.FILE = {
                            $order: 20,
                            className: 'ood-ui-unselectable  {_radius_dropr}',
                            tagName: 'input',
                            type: 'file',
                            accept: fileAccept || null,
                            multiple: fileMultiple ? "multiple" : null,
                            hidefocus: ood.browser.ie ? "hidefocus" : null,
                            size: '1'
                        };
                    case 'listbox':
                    case 'cmdbox':
                    case 'dropbutton':
                        t.className += ' ood-ui-noshadow';
                        ip.type = 'button';
                }
                if (type != 'none' && type != 'input' && type != 'password' && type != 'button' && type != 'spin' && type != 'currency' && type != 'number') {
                    t.RBTN = {
                        $order: 20,
                        tagName: 'button',
                        className: adj('ood-ui-unselectable ood-ui-btn ood-uibar ood-uigradient ood-nofocus {_radius_dropr}'),
                        style: "{_btnrDisplay}",
                        RMID: {
                            className: 'oodfont',
                            $fonticon: '{_fi_btnrClass}'
                        }
                    };
                }
                if (type == 'button' || type == 'dropbutton') {
                    t.BOX.className += ' ood-uigradient';
                }

                if (multiLines) {
                    switch (type) {
                        case 'none':
                        case 'input':
                        case 'getter':
                        case 'helpinput':
                        case 'popbox':
                        case 'number':
                        case 'combobox':
                            ip.tagName = 'textarea';
                            ip.className = '';
                            delete ip.type;
                    }
                }
                if (showMode && showMode != 'normal') {
                    if (showMode == 'transparent') t.BOX.className = '{_radius_input} ';
                    t.CMD.className = adj(t.CMD.className);
                }
                // set template
                profile.box.setTemplate(template, hash);
            }
            profile.template = template;
        },
        _handleInput: function (prf, cls, v) {
            var i = prf.getSubNode('INPUT');
            if (("" + i.get(0).type).toLowerCase() != 'button') {
                if (!v && (prf.properties.disabled || prf.properties.readonly || prf.$inputReadonly))
                    v = true;
                prf.getRoot()[v ? 'addClass' : 'removeClass'](cls);
                i.attr('readonly', v);
            }
        },
        _prepareData: function (profile) {
            var data = {},
                NONE = 'display:none',
                prop = profile.properties,
                type = prop.type,
                showMode = prop.showMode,
                arr = profile.box.$DataModel.commandBtn.combobox;
            data = arguments.callee.upper.call(this, profile, data);

            var tt = type, a, b, c = tt == 'counter';
            tt = (tt == 'combobox' || tt == 'listbox' || tt == 'dropbutton') ? 'arrowdrop' : tt;

            data._fi_btnlClass = "ood-icon-singleleft";
            data._fi_btnrClass = tt == 'counter' ? 'ood-icon-singleright' : (data.dropImageClass || ('ood-uicmd-' + tt));

            data._type = "text";

            data._cmdDisplay = (a = (!data.commandBtn) || data.commandBtn == 'none') ? NONE : '';
            data._fi_commandCls = (ood.arr.indexOf(arr, data.commandBtn) != -1 ? "ood-uicmd-" : "") + data.commandBtn;

            data._btnrDisplay = (b = type == 'none' || type == 'input' || type == 'password' || type == 'currency' || type == 'number' || type == 'button') ? NONE : '';
            data.typecls = profile.getClass('KEY', '-type-' + data.type);
            if (!showMode || showMode == 'normal') {
                data._radius_dropl = 'ood-uiborder-radius-tl ood-uiborder-radius-bl ood-uiborder-noradius-r';
                // lbtn + rbtn + cmd ?
                data._radius_input = (a && b) ? 'ood-uiborder-radius' : c ? 'ood-uiborder-noradius' : 'ood-uiborder-radius-tl ood-uiborder-radius-bl ood-uiborder-noradius-r';
                // rtbn?
                data._radius_dropr = a ? 'ood-uiborder-radius-tr ood-uiborder-radius-br ood-uiborder-noradius-l' : 'ood-uiborder-noradius';

                data._radius_dropt = a ? 'ood-uiborder-radius-tr ood-uiborder-noradius-l ood-uiborder-noradius-b' : 'ood-uiborder-noradius';
                data._radius_dropb = a ? 'ood-uiborder-radius-br ood-uiborder-noradius-l ood-uiborder-noradius-t' : 'ood-uiborder-noradius';
            } else if (showMode == 'compact') {
                data._radius_input = 'ood-uiborder-radius';
            }
            return data;
        },
        _ensureValue: function (profile, value) {
            var me = arguments.callee, prop = profile.properties;
            //if value is empty
            if (!ood.isSet(value) || value === '') return '';
            if (profile.$Mask && profile.$Mask == value) {
                value = '';
            }
            switch (profile.properties.type) {
                case 'date':
                case 'datetime':
                    var d;
                    if (value) {
                        if (ood.isDate(value))
                            d = value;
                        else if (ood.isFinite(value))
                            d = new Date(parseInt(value, 10));
                        else
                            d = ood.Date.parse(value + "");
                    }
                    return d ? String(profile.properties.type == 'datetime' ? d.getTime() : ood.Date.getTimSpanStart(d, 'd', 1).getTime()) : "";
                case 'color':
                    var c = ood.UI.ColorPicker._ensureValue(null, value);
                    return (c == "transparent" ? '' : '#') + c;
                case 'time':
                    return ood.UI.TimePicker._ensureValue(null, value);
                case 'currency':
                case 'number':
                case 'spin':
                case 'counter':
                    return this._number(profile, value);
                default:
                    return typeof value == 'string' ? value : (value || value === 0) ? String(value) : '';
            }
        },
        _number: function (profile, value) {
            var prop = profile.properties;
            if (/^\s*\=/.test(value || "")) {
                value = ood.ExcelFormula.calculate(value || "") || "";
            }
            value = ood.toNumeric(value, prop.precision, prop.groupingSeparator, prop.decimalSeparator, prop.forceFillZero, prop.trimTailZero);
            if (ood.isSet(prop.max))
                value = value > prop.max ? prop.max : value;
            if (ood.isSet(prop.min))
                value = value < prop.min ? prop.min : value;
            return value;
        },
        _onresize: function (profile, width, height) {
            if (profile._$ignoreonsize) return;

            var prop = profile.properties,
                type = prop.type,
                cmp = prop.showMode == 'compact',
                // if any node use other font-size which does not equal to ood-node, use 'px'
                f = function (k) {
                    if (!k) return null;
                    k = profile.getSubNode(k);
                    return k;
                },
                root = f('KEY'),
                v1 = f('INPUT'),
                icb = f('ICONB'),
                ut = f('UNIT'),
                box = f('BOX'),
                label = f('LABEL'),
                cmdbtn = f(prop.commandBtn != 'none' ? 'CMD' : null),
                lbtn = f(type == 'counter' ? 'LBTN' : null),
                rbtn = f(type == 'spin' ? 'SPINBTN' : (type == 'none' || type == 'input' || type == 'password' || type == 'currency' || type == 'number' || type == 'button') ? null : 'RBTN'),
                // determine em
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },

                fzrate = profile.getEmSize() / root._getEmSize(),
                v1fz = v1._getEmSize(fzrate),
                labelfz = label._getEmSize(fzrate),

                isB = v1.get(0).type.toLowerCase() == 'button',
                $hborder, $vborder,

                clsname = 'ood-node ood-input-input',
                cb = ood.browser.contentBox,
                paddingH = !cb ? 0 : isB ? 0 : Math.round(v1._paddingH() / 2) * 2,
                paddingH2 = !cb ? 0 : Math.round(v1._paddingH() / 2) * 2,
                paddingW = 0,

                autoH, icbw, utw, btnw,
                pl = 0, pr = 0,
                boxB = box._borderW(),
                offset = boxB / 2;

            $hborder = $vborder = !cb ? 0 : offset;
            btnw = parseInt(profile.getEmSize() * 1.5, 10);

            // calculate by px
            if (height) height = (autoH = height == 'auto') ? profile.$em2px(!cb ? 1.6666667 : 1, null, true) + paddingH2 + boxB : profile.$isEm(height) ? profile.$em2px(height, null, true) : height;
            if (width) width = profile.$isEm(width) ? profile.$em2px(width, null, true) : width;

            // for auto height
            if (autoH) {
                profile._$ignoreonsize = 1;
                root.height(adjustunit(height));
                delete profile._$ignoreonsize;
            }

            var labelPos = prop.labelPos || 'left',
                // make it round to Integer
                labelSize = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelSize, labelfz, true) || 0,
                labelGap = (labelPos == 'none' || !labelPos) ? 0 : profile.$px(prop.labelGap, null, true) || 0,

                ww = width,
                hh = height,
                bwcmd = 0,
                lbw = 0,
                rbw = 0,
                left = Math.max(0, (prop.$b_lw || 0) - $hborder),
                top = Math.max(0, (prop.$b_tw || 0) - $vborder);
            if (null !== ww) {
                ww -= Math.max($hborder * 2, (prop.$b_lw || 0) + (prop.$b_rw || 0));
                lbw = lbtn ? btnw : 0;
                rbw = rbtn ? btnw : 0;
                bwcmd = cmdbtn ? btnw : 0;
//                bwcmd=(cmdbtn?cmdbtn.offsetWidth:0);
//                rbw=(rbtn?rbtn.offsetWidth:0);
                /*for ie6 bug*/
                /*for example, if single number, 100% width will add 1*/
                /*for example, if single number, attached shadow will overlap*/
                if (ood.browser.ie6) ww = (Math.round(ww / 2)) * 2;
            }
            if (null !== hh) {
                hh -= Math.max($vborder * 2, (prop.$b_lw || 0) + (prop.$b_rw || 0));

                if (ood.browser.ie6) hh = (Math.round(hh / 2)) * 2;
                /*for ie6 bug*/
                if (ood.browser.ie6 && null === width) box.ieRemedy();
            }
            var iL = ww === null ? null : left + (labelPos == 'left' ? labelSize : 0),
                iT = hh === null ? null : top + (labelPos == 'top' ? labelSize : 0),
                iW = ww === null ? null : Math.max(0, ww - ((labelPos == 'left' || labelPos == 'right') ? labelSize : 0)),
                iH = hh === null ? null : Math.max(0, hh - ((labelPos == 'top' || labelPos == 'bottom') ? labelSize : 0)),
                iH2 = hh === null ? null : Math.max(0, height - ((labelPos == 'top' || labelPos == 'bottom') ? labelSize : 0)),
                iR = labelPos == 'right' ? labelSize : 0;

            // label
            if (labelSize) {
                label.css('display', '');
                label.cssRegion({
                    left: adjustunit(ww === null ? null : labelPos == 'right' ? (ww - labelSize + labelGap + $hborder * 2) : 0, labelfz),
                    top: adjustunit(height === null ? null : labelPos == 'bottom' ? (height - labelSize + labelGap) : 0, labelfz),
                    width: adjustunit(ww === null ? null : Math.max(0, ((labelPos == 'left' || labelPos == 'right') ? (labelSize - labelGap) : ww)), labelfz),
                    height: adjustunit(height === null ? null : Math.max(0, ((labelPos == 'top' || labelPos == 'bottom') ? (labelSize - labelGap) : height) - paddingH), labelfz)
                });
            } else {
                label.css('display', 'none');
            }
            if (iW !== null) {
                if (cmp) {
                    pl += lbw;
                    pr += bwcmd + rbw;
                } else {
                    iW -= bwcmd + rbw + lbw;
                }
            }
            // left 1
            if (lbtn) {
                if (iH2 !== null)
                    lbtn.height(adjustunit(Math.max(0, iH2)));
                if (iW !== null)
                    lbtn.left(adjustunit(iL));
                lbtn.top(adjustunit(iT));
                if (!cmp) {
                    iL += lbw;
                }
                // for left offset 1px
                if (iW !== null) {
                    iL -= offset;
                    iW += offset;
                }
            }
            //left 2
            if (prop.image || prop.imageClass) {
                icb.setInlineBlock();
                if (icbw = icb.offsetWidth(true))
                    pl += icbw;
            }
            if (!icbw) icb.css('display', 'none');
            else if (cmp && lbw)
                icb.left(adjustunit(lbw, icb));

            // right 1
            if (bwcmd) {
                cmdbtn.top(adjustunit(iT));
                if (iH2 !== null)
                    cmdbtn.height(adjustunit(Math.max(0, iH2)));
                if (iW !== null) {
                    cmdbtn.css('right', adjustunit(iR));
                    iR += bwcmd - $hborder;

                    // for left offset 1px
                    iW += offset;
                }
            }

            // right 2
            if (rbw) {
                rbtn.top(adjustunit(iT));
                if (iH2 !== null)
                    rbtn.height(adjustunit(Math.max(0, iH2)));
                if (iW !== null) {
                    rbtn.css('right', adjustunit(iR));
                    // for left offset 1px
                    iW += offset;
                }
                if (iH2 !== null && prop.type == 'spin') {
                    if (iH2 / 2 - $vborder * 2 > 0) {
                        f('R1').height(adjustunit(iH2 / 2));
                        f('R2').height(adjustunit(iH2 / 2 + (Math.round(iH2) - Math.round(iH2 / 2) * 2)));
                    }
                }
            }
            // right 3
            if (prop.unit) {
                ut.setInlineBlock();
                if (utw = ut.offsetWidth(true))
                    pr += utw;
            }
            if (!utw) ut.css('display', 'none');
            else if (cmp && (rbw || bwcmd))
                ut.css('right', adjustunit(rbw + bwcmd, ut));

            // box
            box.cssRegion({
                left: iW ? adjustunit(iL) : null,
                top: iH ? adjustunit(iT) : null,
                width: iW ? adjustunit(iW) : null,
                height: iH ? adjustunit(iH) : null
            });

            // input last
            if (pl) v1.css('paddingLeft', adjustunit(pl, icb));
            if (pr) v1.css('paddingRight', adjustunit(pr, ut));

            // must recalculate here
            paddingW = !cb ? 0 : isB ? 0 : v1._paddingW();
            if (null !== iW && iW - paddingW > 0)
                v1.width(adjustunit(Math.max(0, iW - paddingW), v1fz));
            if (null !== iH && iH - paddingH > 0)
                v1.height(adjustunit(Math.max(0, iH - paddingH), v1fz));

            /*for ie6 bug*/
            if ((profile.$resizer) && ood.browser.ie) {
                box.ieRemedy();
            }
        }
    }
});