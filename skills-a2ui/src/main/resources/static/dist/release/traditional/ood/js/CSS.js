ood.Class("ood.CSS", null, {
    Static: {
        _r: ood.browser.ie ? 'rules' : 'cssRules',
        _baseid: 'ood:css:base',
        _firstid: 'ood:css:first',
        _lastid: 'ood:css:last',
        _reg1: /\.(\w+)\[CLASS~="\1"\]/g,
        _reg2: /\[ID"([^"]+)"\]/g,
        _reg3: /\*([.#])/g,
        _reg4: /\s+/g,
        _reg5: /\*\|/g,
        _reg6: /(\s*,\s*)/g,
        _rep: function (str) {
            var ns = this;
            return str.replace(ns._reg1, '.$1')
                .replace(ns._reg2, '#$1')
                .replace(ns._reg3, '$1')
                .replace(ns._reg4, ' ')
                .replace(ns._reg5, '')
                .replace(ns._reg6, ',').toLowerCase();
        },
        _appendSS: function (container, txt, id, before, attr) {
            var fc = document.createElement('style');
            fc.type = "text/css";
            if (id) fc.id = id;
            if (ood.browser.ie && fc.styleSheet && "cssText" in fc.styleSheet)
                fc.styleSheet.cssText = txt || '';
            else
                try {
                    fc.appendChild(document.createTextNode(txt || ''))
                } catch (p) {
                    fc.styleSheet.cssText = txt || ''
                }
            if (attr) {
                ood(fc).attr(attr);
            }
            if (before) ood(container).prepend(fc);
            else ood(container).append(fc);
            return fc;
        },
        _createCss: function (id, txt, last) {
            var ns = this,
                head = this._getHead(),
                fid = ns._firstid,
                lid = ns._lastid,
                fc,
                c;
            fc = document.createElement('style');
            fc.type = "text/css";
            fc.id = id;
            if (txt) {

                if (ood.browser.ie && fc.styleSheet && "cssText" in fc.styleSheet)
                    fc.styleSheet.cssText = txt || '';
                else
                    try {
                        fc.appendChild(document.createTextNode(txt || ''))
                    } catch (p) {
                        fc.styleSheet.cssText = txt || ''
                    }
            }
            if (!last) {
                c = document.getElementById(fid) || head.firstChild;
                while ((c = c.nextSibling) && !/^(script|link|style)$/i.test('' + c.tagName)) ;
                if (c)
                    head.insertBefore(fc, c);
                else {
                    if (c = document.getElementById(lid))
                        head.insertBefore(fc, c);
                    else
                        head.appendChild(fc);
                }
            } else
                head.appendChild(fc);
            return fc;
        },
        _getCss: function (id, css, last) {
            return document.getElementById(id) || this._createCss(id, css, last);
        },
        _getBase: function () {
            return this._getCss(this._baseid);
        },
        _getFirst: function () {
            return this._getCss(this._firstid);
        },
        _getLast: function () {
            return this._getCss(this._lastid, null, true);
        },
        _getHead: function () {
            return this._head || (this._head = document.getElementsByTagName("head")[0] || document.documentElement);
        },
        _check: function () {
            if (!ood.browser.ie) return;
            var count = 0;
            for (var head = this._getHead(), i = 0, t = head.childNodes, l; l = t[i++];)
                if (l.type == "text/css")
                    count++
            return count > 20;
        },
        get: function (property, value) {
            for (var head = this._getHead(), i = 0, t = head.childNodes, l; l = t[i++];)
                if (l.type == "text/css" && property in l && l[property] == value)
                    return l;
        },
        //if backOf==true, add to head last node
        //else add to the before position of the base styleSheet
        addStyleSheet: function (txt, id, backOf, force) {
            var e, ns = this, head = ns._getHead(),
                add = function (txt, id, backOf) {
                    var e = document.createElement('style');
                    e.type = "text/css";
                    if (id) e.id = id;
                    //for ie
                    if (ood.browser.ie && e.styleSheet && "cssText" in e.styleSheet)
                        e.styleSheet.cssText = txt || '';
                    else
                        try {
                            e.appendChild(document.createTextNode(txt || ''))
                        } catch (p) {
                            e.styleSheet.cssText = txt || ''
                        }
                    if (backOf === -1) {
                        if (head.firstChild) head.insertBefore(e, head.firstChild);
                        else head.appendChild(e);
                    } else if (backOf === 1) {
                        head.appendChild(e);
                    } else {
                        head.insertBefore(e, backOf ? ns._getLast() : ns._getBase());
                    }
                    e.disabled = true;
                    e.disabled = false;
                    return e;
                }, merge = function (txt, backOf) {
                    var e = backOf ? ns._getLast() : ns._getBase();
                    e.styleSheet.cssText += txt;
                    return e;
                };
            if (id && (id = id.replace(/[^\w\-\_\.\:]/g, '_')) && (e = ns.get('id', id))) {
                if (force) {
                    e.disabled = true;
                    head.removeChild(e);
                }
                else return e;
            }

            if (ns._check()) {
                return merge(txt, backOf);
            } else
                return add(txt, id, backOf);
        },
        //if front==true, add to the before position of the base styleSheet
        //else add to the last postion
        includeLink: function (href, id, front, attr) {
            var e, ns = this, head = ns._getHead();
            if (href && (e = ns.get('href', href))) {
            } else {
                e = document.createElement('link');
                e.type = 'text/css';
                e.rel = 'stylesheet';
                e.href = href;
                if (id)
                    e.id = id;
                e.media = 'all';
                ood.each(attr, function (o, i) {
                    e.setAttribute(i, o);
                });
            }
            head.insertBefore(e, front ? ns._getBase() : ns._getLast());
            e.disabled = true;
            e.disabled = false;
            return e;
        },
        remove: function (property, value) {
            var head = this._getHead();
            if (value = this.get(property, value)) {
                value.disabled = true;
                head.removeChild(value);
            }
        },
        replaceLink: function (href, property, oValue, nValue) {
            var ns = this,
                head = ns._getHead(),
                attr = {}, e, v;
            attr[property] = nValue;
            e = ns.includeLink(href, null, false, attr);
            if (v = ns.get(property, oValue))
                head.replaceChild(e, v);
            e.disabled = true;
            e.disabled = false;
        },
        _build: function (selector, value, flag) {
            var t = '';
            ood.each(value, function (o, i) {
                t += i.replace(/([A-Z])/g, "-$1").toLowerCase() + ":" + o + ";";
            });
            return flag ? t : selector + "{" + t + "}";
        },
        //selector: single css exp without ','; not allow '.a, .b{}'
        //  for *** IE *** allow single css exp only
        setStyleRules: function (selector, value, force) {
            var ns = this,
                add = true,
                ds = document.styleSheets,
                target, target2, selectorText, bak, h, e, t, _t;
            selector = ood.str.trim(selector.replace(/\s+/g, ' '));
            if (!(value && force)) {
                bak = selector.toLowerCase();
                ood.arr.each(ood.toArr(ds), function (o) {
                    try {
                        o[ns._r]
                    } catch (e) {
                        return
                    }
                    ood.arr.each(ood.toArr(o[ns._r]), function (v, i) {
                        if (!v.selectorText) return;
                        if (v.disabled) return;
                        selectorText = ns._rep(v.selectorText);
                        /*Notice: in IE, no ',' in any selectorTExt*/
                        _t = selectorText.split(',');
                        //null=>remove
                        if (!value) {
                            add = false;
                            if (ood.arr.indexOf(_t, bak) != -1 && _t.length > 1) {
                                _t = ood.arr.removeFrom(_t, ood.arr.indexOf(_t, bak)).join(',');
                                t = v.cssText.slice(v.cssText.indexOf("{") + 1, v.cssText.lastIndexOf("}"));
                                if (o.insertRule)
                                    o.insertRule(_t + "{" + t + "}", o[ns._r].length);
                                else if (o.addRule)
                                    o.addRule(_t, t || "{}");
                                if (o.deleteRule)
                                    o.deleteRule(i);
                                else
                                    o.removeRule(i);
                                o.disabled = true;
                                o.disabled = false;
                            } else if (selectorText == bak) {
                                if (o.deleteRule)
                                    o.deleteRule(i);
                                else
                                    o.removeRule(i);
                                o.disabled = true;
                                o.disabled = false;
                            }
                            //modify the last one
                        } else {
                            //for single css exp, (all single css exp in IE)
                            if (selectorText == bak) {
                                target = v;
                                return false
                            }
                            //for multi css exps, not in IE
                            if (ood.arr.indexOf(_t, bak) != -1) {
                                target2 = v;
                                return false
                            }
                        }
                    }, null, true);
                    if (target) {
                        add = false;
                        try {
                            ood.each(value, function (o, i) {
                                i = i.replace(/(-[a-z])/gi, function (m, a) {
                                    return a.charAt(1).toUpperCase()
                                });
                                target.style[i] = typeof o == 'function' ? o(target.style[i]) : o;
                            })
                        } catch (e) {
                        }
                        o.disabled = true;
                        o.disabled = false;
                        return false;
                        //not in IE
                    } else if (target2) {
                        add = false;
                        o.insertRule(ns._build(selector, value), o[ns._r].length);
                        o.disabled = true;
                        o.disabled = false;
                        return false;
                    }
                }, null, true);
            }
            //need to add
            if (force || add)
                ns._addRules(selector, value);
            return ns;
        },
        $getCSSValue: function (selector, cssKey, cssValue, ownerNode) {
            var ns = this,
                k = ns._r, css,
                ds = document.styleSheets,
                l = ds.length, m, o, v, i, j,
                selectorText;
            selector = ood.str.trim(selector.replace(/\s+/g, ' ').toLowerCase());
            for (i = l - 1; i >= 0; i--) {
                try {
                    //firefox cracker
                    o = ds[i][k];
                } catch (e) {
                    continue;
                }
                if (!ds[i].disabled) {
                    o = ds[i][k];
                    if (o) {
                        m = o.length;
                        for (j = m - 1; j >= 0; j--) {
                            if ((v = o[j]).selectorText && !v.disabled) {
                                selectorText = ns._rep(v.selectorText);
                                if (ood.arr.indexOf(selectorText.split(/\s*,\s*/g), selector) != -1) {
                                    if (!cssKey) {
                                        (css = css || []).push(v);
                                    } else {
                                        if (!cssValue) {
                                            if (!ownerNode || (ownerNode == ds[i].ownerNode || ds[i].owningElement))
                                                if (v.style[cssKey] !== '')
                                                // return cssValue
                                                // replace is crack for opera
                                                    return (v.style[cssKey] || "").replace(/^\"|\"$/g, '');
                                        } else if (cssValue === v.style[cssKey]) {
                                            // return css dom node
                                            return ds[i].ownerNode || ds[i].owningElement;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // return all stylesheets named cssKey
            return css;
        },
        _addRules: function (selector, value) {
            var ns = this,
                target = ns._getLast(),
                changed = target.sheet || target.styleSheet;
            if (changed.insertRule)
                changed.insertRule(ns._build(selector, value), changed[ns._r].length);
            else if (changed.addRule)
                changed.addRule(selector, ns._build(selector, value, true) || "{}");
            target.disabled = true;
            target.disabled = false;
            return ns;
        },
        /*resetCSS:function(){
            var css = '';
            this.addStyleSheet(css,"ood.CSSreset");
        },*/
        adjustFont: function (fontSize, fontFamily, fontWeight, fontStyle) {
            if (fontSize) ood('html').css('font-size', fontSize);
            if (fontFamily) ood('html').css('font-family', fontFamily);
            if (fontWeight) ood('html').css('font-weight', fontWeight);
            if (fontStyle) ood('html').css('font-style', fontStyle);

            this._dftEmStr = '';
            this._getDftEmSize(true);
            this._dftRemStr = '';
            this._getDftRemSize(true);
            if (ood.UI)
                ood.UI.getAll().reLayout(true);
        },
        _dftEmStr: '',
        _dftEm: 0,
        _getDftEmSize: function (force) {
            var ns = this;
            if (force || !ns._dftEm) {
                var fz = ns.$getCSSValue('.ood-ui-ctrl', 'fontSize'), num;

                // only can be triggerred by modifing font-size of '.ood-ui-ctrl' itslef.
                if (!ns._dftEmStr || ns._dftEmStr != fz) {
                    num = parseFloat(fz);
                    if (num && ns.$isPx(fz)) {
                        ns._dftEm = num;

                        ns._dftEmStr = fz;
                    } else if (num && ns.$isRem(fz)) {
                        ns._dftEm = num * ns._getDftRemSize();

                        ns._dftEmStr = fz;
                    } else {
                        var div;
                        ood('body').append(div = ood.create('<div class="ood-ui-ctrl" style="height:1em;visibility:hidden;position:absolute;border:0;margin:0;padding:0;left:-10000px;"></div>'));
                        ns._dftEm = div.get(0).offsetHeight;
                        div.remove();

                        ns._dftEmStr = ns._dftEm + "px";
                    }
                }
            }
            return ns._dftEm;
        },
        $resetEm: function () {
            delete ood.CSS._dftEm;
        },
        _dftRemStr: '',
        _dftRem: 0,
        _getDftRemSize: function (force) {
            var ns = this;
            if (force || !ns._dftRem)
                ns._dftRem = parseFloat(ood('html').css('font-size')) || 16;
            return ns._dftRem;
        },
        $resetRem: function () {
            delete ood.CSS._dftRem;
        },
        $isEm: function (value) {
            return (!value || value == 'auto') ? ood.$us() == 1 : /^-?((\d\d*\.\d*)|(^\d\d*)|(^\.\d\d*))em$/i.test(ood.str.trim(value + ''));
        },
        $isRem: function (value) {
            return (!value || value == 'auto') ? ood.$us() == 1 : /^-?((\d\d*\.\d*)|(^\d\d*)|(^\.\d\d*))rem$/i.test(ood.str.trim(value + ''));
        },
        $isPx: function (value) {
            return (!value || value == 'auto') ? ood.$us() == 1 : /^-?((\d\d*\.\d*)|(^\d\d*)|(^\.\d\d*))px$/i.test(ood.str.trim(value + ''));
        },

        $em2px: function (value, node, roundPx) {
            value = (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isEm(value)) ? (parseFloat(value) || 0) * (node ? ood.isFun(node) ? node() : ood.isFinite(node) ? node : ood(node)._getEmSize() : this._getDftEmSize() || this._getDftEmSize()) : value;
            return roundPx ? Math.round(parseFloat(value) || 0) : value;
        },
        $px2em: function (value, node, roundPx) {
            return (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isPx(value)) ? (roundPx ? Math.round(parseFloat(value) || 0) : (parseFloat(value) || 0)) / (node ? ood.isFun(node) ? node() : ood.isFinite(node) ? node : ood(node)._getEmSize() : this._getDftEmSize() || this._getDftEmSize()) : value;
        },
        $rem2px: function (value, roundPx) {
            value = (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isRem(value)) ? (parseFloat(value) || 0) * this._getDftRemSize() : value;
            return roundPx ? Math.round(parseFloat(value) || 0) : value;
        },
        $px2rem: function (value, roundPx) {
            return (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isPx(value)) ? (roundPx ? Math.round(parseFloat(value) || 0) : (parseFloat(value) || 0)) / this._getDftRemSize() : value;
        },
        $em2rem: function (value, node) {
            return (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isEm(value)) ? (parseFloat(value) || 0) * (node ? ood.isFinite(node) ? node : ood(node)._getEmSize() : this._getDftEmSize() || this._getDftEmSize()) / this._getDftRemSize() : value;
        },
        $rem2em: function (value, node) {
            return (!value || value == 'auto') ? value : (ood.isFinite(value) || this.$isRem(value)) ? (parseFloat(value) || 0) * this._getDftRemSize() / (node ? ood.isFinite(node) ? node : ood(node)._getEmSize() : this._getDftEmSize() || this._getDftEmSize()) : value;
        },
        $px: function (value, node, roundPx) {
            value = ((!ood.isFinite(value) && this.$isRem(value)) ? this.$rem2px(value, roundPx) : this.$isEm(value) ? this.$em2px(value, node, roundPx) : (!value || value == 'auto') ? value : (parseFloat(value) || 0));
            return roundPx ? Math.round(parseFloat(value) || 0) : value;
        },
        $em: function (value, node, roundPx) {
            return ((ood.isFinite(value) || this.$isPx(value)) ? this.$px2em(value, node, roundPx) : this.$isRem(value) ? this.$rem2em(value, node) : (!value || value == 'auto') ? value : (parseFloat(value) || 0));
        },
        $rem: function (value, node, roundPx) {
            return ((ood.isFinite(value) || this.$isPx(value)) ? this.$px2rem(value, roundPx) : this.$isEm(value) ? this.$em2rem(value, node) : (!value || value == 'auto') ? value : (parseFloat(value) || 0));
        },
        $addpx: function (a, b, node) {
            if (a == 'auto') return a;
            if (this.$isRem(a)) {
                return this.$px2rem(Math.round(this.$rem2px(a) + (parseFloat(b) || 0))) + 'rem';
            } else if (this.$isEm(a)) {
                return this.$px2em(Math.round(this.$em2px(a, false, node) + (parseFloat(b) || 0))) + 'em';
            } else {
                return Math.round((parseFloat(a) || 0) + (parseFloat(b) || 0)) + 'px';
            }
        },
        $forceu: function (v, u, node, roundPx) {
            return (v === null || v === undefined || v === '' || v == 'auto') ? v :
                (u ? u == 'rem' : (ood.$us() === 0)) ? this.$rem(v, node, roundPx !== false) + 'rem' :
                    (u ? u == 'em' : (ood.$us() == 1)) ? this.$em(v, node, roundPx !== false) + 'em' :
                        Math.round(this.$px(v, node, roundPx !== false)) + 'px'
        },

        $picku: function (v) {
            return v && v != 'auto' && (v + '').replace(/[-\d\s.]*/g, '') || (ood.$us() == 1 ? 'em' : 'px')
        },
        $addu: function (v) {
            return v == 'auto' ? v : (ood.isFinite(v) || this.$isPx(v)) ? Math.round(parseFloat(v) || 0) + 'px' : v + ''
        },

        // 主题管理系统
        Theme: {
            themes: {},
            current: 'default',

            register: function(name, config) {
                this.themes[name] = config;
                return this;
            },

            set: function(name) {
                if (this.themes[name]) {
                    this.current = name;
                    this.apply();
                    return true;
                }
                return false;
            },

            apply: function() {
                var theme = this.themes[this.current];
                if (theme) {
                    // 应用颜色主题
                    if (theme.colors) {
                        this.applyColors(theme.colors);
                    }

                    // 应用字体主题
                    if (theme.fonts) {
                        this.applyFonts(theme.fonts);
                    }
                }
            },

            applyColors: function(colors) {
                // 动态更新 CSS 变量
                var cssVars = '';
                for (var key in colors) {
                    cssVars += '--' + key + ':' + colors[key] + ';';
                }
                ood('html').css(cssVars);
            },

            applyFonts: function(fonts) {
                ood.CSS.adjustFont(fonts.size, fonts.family, fonts.weight, fonts.style);
            }
        }

    },
    Initialize: function () {
        var b = ood.browser,
            inlineblock = (b.gek
                ? b.ver < 3
                    ? ((b.ver < 3 ? "-moz-outline-offset:-1px !important;" : "") + "display:-moz-inline-block;display:-moz-inline-box;display:inline-block;")
                    : "display:inline-block;"
                : b.ie6
                    ? "display:inline-box;display:inline;"
                    : "display:inline-block;") +
                (b.ie ? "zoom:1;" : ""),
            css = ".ood-node{margin:0;padding:0;line-height:1.22;-webkit-text-size-adjust:none;}" +
                ".ood-node-highlight{color:var(--text-color, #000);}" +
                ".ood-title-node{}" +
                ".oodfont-hover, .oodcon-hover{ color: var(--secondary-color, #686868); }" +
                (!ood.browser.fakeTouch && ood.browser.devType != 'touchOnly' ? ".oodfont-active, .oodcon-active{ color: var(--active-color, #3393D2); }" : "") +
                ".oodfont-checked, .oodcon-checked{ color: var(--checked-color, #3393D2); }" +
                ".ood-wrapper{color:var(--text-color, #000);font-family:arial,helvetica,clean,sans-serif;font-style:normal;font-weight:normal;vertical-align:middle;}" +
                ".ood-cover{cursor:wait;background:url(" + ood.ini.img_bg + ") transparent repeat;opacity:0.5;background-color:var(--background-color, #ccc)}" +
                ".ood-node-table{border-collapse:collapse;border-spacing:0;empty-cells:show;font-size:inherit;" + (b.ie ? "font:100%;" : "") + "}" +
                ".ood-node-fieldset,.ood-node-img{border:0;}" +
                ".ood-node-ol,.ood-node-ul,.ood-node-li{list-style:none;}" +
                ".ood-node-caption,.ood-node-th{text-align:left;}" +
                ".ood-node-th{font-weight:normal;}" +
                ".ood-node-q:before,.ood-node-q:after{content:'';}" +
                ".ood-node-abbr,.ood-node-acronym{border:0;font-variant:normal;}" +
                ".ood-node-sup{vertical-align:text-top;}" +
                ".ood-node-sub{vertical-align:text-bottom;}" +
                ".ood-node-input,.ood-node-textarea,.ood-node-select{cursor:text;font-family:inherit;font-size:inherit;font-weight:inherit;" + (b.ie ? "font-size:100%;" : "") + "}" +
                ".ood-node-del,.ood-node-ins{text-decoration:none;}" +
                ".ood-node-pre,.ood-node-code,.ood-node-kbd,.ood-node-samp,.ood-node-tt{font-family:monospace;" + (b.ie ? "font-size:108%;" : "") + "line-height:100%;}" +
                // dont use font(use font-size/font-family) in IE678
                ".ood-node-select,.ood-node-input,.ood-node-textarea{font-family:arial,helvetica,clean,sans-serif;border-width:1px;}" +
                ((b.ie && b.ver <= 8) ? ".ood-node-input{overflow:hidden;}" : "") +
                // base setting
                ".ood-node-a, .ood-node-a .ood-node{cursor:pointer;color:var(--primary-color, #0000ee);text-decoration:none;}" +
                ".ood-node-a:hover, .ood-node-a:hover .ood-node{color:var(--hover-color, red)}" +
                (b.gek ? (".ood-node-a:focus{outline-offset:-1px;" + (b.ver < 3 ? "-moz-outline-offset:-1px !important" : "") + "}") : "") +
                ".ood-node-span, .ood-node-div{border:0;}" +
                ((b.ie && b.ver <= 8) ? "" : ".ood-node-span:not(.ood-showfocus):focus, .ood-node-div:not(.ood-showfocus):focus{outline:0;}.ood-showfocus:focus{outline-width: 1px;outline-style: dashed;}") +
                ".ood-node-span, .ood-wrapper span" + ((b.ie && b.ver <= 7) ? "" : ", .ood-v-wrapper:before, .ood-v-wrapper > .ood-v-node") + "{outline-offset:-1px;" +
                inlineblock +
                "}" +
                ".ood-node-h1,.ood-node-h2,.ood-node-h3,.ood-node-h4,.ood-node-h5,.ood-node-h6{font-size:100%;font-weight:normal;}" +
                ".ood-node-h1{font-size:138.5%;}" +
                ".ood-node-h2{font-size:123.1%;}" +
                ".ood-node-h3{font-size:108%;}" +
                ".ood-node-h1,.ood-node-h2,.ood-node-h3{margin:1em 0;}" +
                ".ood-node-h1,.ood-node-h2,.ood-node-h3,.ood-node-h4,.ood-node-h5,.ood-node-h6,.ood-node-strong{font-weight:bold;}" +
                ".ood-node-em{font-style:italic;}" +
                ".ood-node-legend{color:var(--text-color, #000);}" +
                (b.ie6 ? ("#" + ood.$localeDomId + "{vertical-align:baseline;}") : "") +

                // some cross browser css solution
                ".ood-nofocus:focus{outline:0;}" +
                ".ood-cls-wordwrap{" +
                "white-space: pre-wrap;word-break: break-all;" + // css-3
                (b.gek ? "white-space: -moz-pre-wrap;" : "") +  // Mozilla, since 1999
                (b.opr ? "white-space: -pre-wrap;" : "") + // Opera 4-6
                (b.opr ? "white-space: -o-pre-wrap;" : "") + // Opera 7
                (b.ie ? "word-wrap: break-word;" : "") + // Internet Explorer 5.5+
                "}" +
                ((b.ie && b.ver <= 8) ? "" : (".ood-v-wrapper:before{content:'';height:100%;font-size:0;vertical-align:middle;}" +
                    ".ood-v-wrapper > .ood-v-node{vertical-align:middle;}" +
                    ".ood-v-top > .ood-v-wrapper:before{vertical-align:top;}" +
                    ".ood-v-top > .ood-v-wrapper > .ood-v-node{vertical-align:top;}" +
                    ".ood-v-bottom > .ood-v-wrapper:before{vertical-align:bottom;}" +
                    ".ood-v-bottom > .ood-v-wrapper > .ood-v-node{vertical-align:bottom;}")) +
                ".ood-node-tips{background-color:var(--background-light-color, #FDF8D2);}" +

                // must here for get correct base font size

                // ".oodfont, .oodcon{font-size:1.3333333333333333em;line-height:1em;}"+
                ".oodcon{margin: 0 .25em;" +
                inlineblock +
                "}" +
                ".oodcon:before{height:1em;width:1em;}" +
                ".ood-ui-ctrl, .ood-ui-reset{font-family:arial,helvetica,clean,sans-serif; font-style:normal; font-weight:normal; vertical-align:middle; color:var(--text-color, #000); }" +
                //ood-ui-ctrl must be after ood-ui-reset
                ".ood-ui-reset{font-size: inherit;}" +
                // html(default 10px) > .ood-ui-ctrl(rem) > inner nodes(em)
                ".ood-ui-ctrl{cursor:default;font-size:1rem;}" ;
        ;

        this.addStyleSheet(css, 'ood.CSS');

        /*
        ood.Thread.repeat(function(t){
            if((t=ood.CSS._dftEm) && (t!==ood.CSS._getDftEmSize(true)))ood.CSS.adjustFont();
        }, 10000);
        */
    },
    

});

// 添加主题管理API
ood.CSS.switchTheme = function(name) {
    return ood.CSS.Theme.set(name);
};

ood.CSS.getCurrentTheme = function() {
    return ood.CSS.Theme.current;
};

ood.CSS.getAvailableThemes = function() {
    return Object.keys(ood.CSS.Theme.themes);
};

// 注册默认主题
ood.CSS.Theme.register('default', {
    colors: {
        'primary-color': '#3393D2',
        'secondary-color': '#686868',
        'background-color': '#ffffff',
        'text-color': '#000000',
        'border-color': '#cccccc',
        'hover-color': '#ff0000',
        'active-color': '#3393D2',
        'checked-color': '#3393D2'
    },
    fonts: {
        size: '16px',
        family: 'arial, helvetica, clean, sans-serif',
        weight: 'normal',
        style: 'normal'
    }
});

// 注册暗色主题
ood.CSS.Theme.register('dark', {
    colors: {
        'primary-color': '#4a90e2',
        'secondary-color': '#a0a0a0',
        'background-color': '#1a1a1a',
        'text-color': '#ffffff',
        'border-color': '#444444',
        'hover-color': '#ff6b6b',
        'active-color': '#4a90e2',
        'checked-color': '#4a90e2'
    },
    fonts: {
        size: '16px',
        family: 'arial, helvetica, clean, sans-serif',
        weight: 'normal',
        style: 'normal'
    }
});