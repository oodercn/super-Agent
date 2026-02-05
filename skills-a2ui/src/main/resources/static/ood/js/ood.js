/*!
* EUSUI(ood) JavaScript Library v2.1
* http://itjds.net
* 
* Copyright ( 2004 ~ present) itjds.net
* Released under the MIT license
*
*/
// speed up references
var undefined, window = this, document = window.document;
if (!document) throw new Error("EUSUI requires a window with a document");

// global : ood
// we have to keep ood for gloable var
var ood = window.ood = function (nodes, flag) {
    return ood.Dom.pack(nodes, flag)
};

// Class & Namespace
ood.Class = function (key, pkey, obj) {
    var _Static, _parent = [], self = ood.Class, w = window, env = self._fun, reg = self._reg, parent0, _this, i, t, _t,
        _c = self._all,
        _funadj = function (str) {
            return (str + "").replace(/(\s*\/\*[^*]*\*+([^\/][^*]*\*+)*\/)|(\s*\/\/[^\n]*)|(\)[\s\S]*)/g, function (a) {
                return a.charAt(0) != ")" ? "" : a
            });
        }
    obj = obj || {};
    //exists?
    if (!self._ignoreNSCache && (t = ood.get(w, key.split('.'))) && typeof(t) == 'function' && t.$oodclass$) return self._last = t;
    //clear SC
    if (t = ood.get(w, ['ood', '$cache', 'SC'])) delete t[key];

    //multi parents mode
    pkey = (!pkey ? [] : typeof pkey == 'string' ? [pkey] : pkey);
    for (i = 0; t = pkey[i]; i++)
        if (!(_parent[i] = (ood.get(w, t.split('.')) || (ood && ood.SC && ood.SC(t)))))
            throw 'errNoParent--' + t;
    if (obj.Dependencies) {
        if (typeof obj.Dependencies == "string") obj.Dependencies = [obj.Dependencies];
        for (i = 0; t = obj.Dependencies[i]; i++)
            if (!(ood.get(w, t.split('.')) || (ood && ood.SC && ood.SC(t))))
                throw 'errNoDependency--' + t;
    }
    parent0 = _parent[0];

    // Give a change to modify the original object
    var $Start = obj.$Start || (parent0 && parent0.$Start);
    ood.tryF($Start, [], obj);

    // collect items
    _Static = obj.Static || {};
    t = {};
    for (i in _Static)
        if (reg[i]) t[i] = 1;
    for (i in t)
        delete _Static[i];

    //before and after will pass to children
    _Static.Before = obj.Before || (parent0 && parent0.Before);
    _Static.After = obj.After || (parent0 && parent0.After);
    _Static.$Start = $Start;
    _Static.$End = obj.$End || (parent0 && parent0.$End);
    _Static.__gc = obj.__gc || _Static.__gc || (parent0 && parent0.__gc) || function () {
        ood.Class.__gc(this.$key)
    };

    /*set constructor first and create _this
    upper is the first parent Class
    */
    var cf = function () {
        if (ood.Class.$instanceCreated) ood.Class.$instanceCreated(this);
        if (typeof this.initialize == 'function') this.initialize()
    };
    if (typeof obj.Constructor == 'function') {
        _this = env(obj.Constructor, 'Constructor', key, parent0 || cf, 'constructor');
        _this.Constructor = _funadj(obj.Constructor);
    } else {
        if (parent0) {
            // Constructor is for opera, in opear fun.toString can't get arguments sometime
            var f = cf, str = parent0.Constructor;
            if (str) f = new Function(str.slice(str.indexOf("(") + 1, str.indexOf(")")).split(','), str.slice(str.indexOf("{") + 1, str.lastIndexOf("}")));
            _this = env(f, 'Constructor', key, parent0.upper, 'constructor');
            _this.Constructor = _funadj(str);
        } else
            _this = cf;
    }

    //collect parent items, keep the last one
    _t = ood.fun();
    for (i = _parent.length - 1; t = _parent[i--];) {
        ood.merge(_t, t);
        ood.merge(_t.prototype, t.prototype);
    }
    //set keys
    _this.KEY = _this.$key = _this.prototype.KEY = _this.prototype.$key = key;
    //envelop
    //  from Static
    self._wrap(_this, _Static, 0, _t, 'static');
    //  from Instance
    if (t = obj.Instance)
        self._wrap(_this.prototype, t, 1, _t.prototype, 'instance');
    //inherite from parents
    self._inherit(_this, _t);
    self._inherit(_this.prototype, _t.prototype);
    _t = null;

    //exe before functoin
    if (ood.tryF(_this.Before, arguments, _this) === false)
        return false;

    //add child key to parents
    for (i = 0; t = _parent[i]; i++) {
        t = (t.$children || (t.$children = []));
        for (var j = 0, k = t.length, b; j < k; j++)
            if (t[k] == key) {
                b = true;
                break;
            }
        if (!b) t[t.length] = key;
    }

    //set symbol
    _this.$ood$ = _this.$oodclass$ = 1;
    _this.$children = [];
    _this.$parent = _parent;

    //set constructor
    _this.prototype.constructor = _this;
    _this.prototype.$ood$ = 1;
    //set key
    _this[key] = _this.prototype[key] = true;

    //allow load App.Sub first
    _t = t = ood.get(w, key.split('.'));
    ood.set(w, key.split('.'), _this);
    if (Object.prototype.toString.call(_t) == '[object Object]')
        for (i in _t) _this[i] = _t[i];

    //exe after function
    ood.tryF(_this.After, [], _this);
    //exe ini function
    ood.tryF(obj.Initialize, [], _this);
    ood.tryF(_this.$End, [], _this);

    ood.breakO([obj.Static, obj.Instance, obj], 2);

    if (!(key in _c)) {
        _c[key] = _c.length;
        _c.push(key);
    }

    //return Class
    return self._last = _this;
};
ood.Namespace = function (key) {
    var a = key.split('.'), w = window;
    return ood.get(w, a) || ((ood.Namespace._all[a[0]] = 1) && ood.set(w, a, {}));
};
ood.Namespace._all = {};

//window.onerror will be redefined in ood.Debugger
//window.onerror=function(){return true};

/*merge hash from source to target
  target:hash
  source:hash
  type:'all', 'with', 'without'[default], or function <return true will trigger merge>
  return: merged target
*/
ood.merge = function (target, source, type, force) {
    var i, f;
    if (typeof type == "function") {
        f = type;
        type = 'fun';
    }
    switch (type) {
        case 'fun':
            for (i in source) if ((force || source.hasOwnProperty(i)) && true === f(source[i], i)) target[i] = source[i];
            break;
        case 'all':
            for (i in source) if ((force || source.hasOwnProperty(i))) target[i] = source[i];
            break;
        case 'with':
            for (i in source) if ((force || source.hasOwnProperty(i)) && target.hasOwnProperty(i)) target[i] = source[i];
            break;
        default:
            for (i in source) if ((force || source.hasOwnProperty(i)) && !target.hasOwnProperty(i)) target[i] = source[i];
    }
    return target;
};

new function () {
    var lastTime = 0, vendors = ['ms', 'moz', 'webkit', 'o'], w = window, i = 0, l = vendors.length, tag;
    for (; i < l && !w.requestAnimationFrame && (tag = vendors[i++]);) {
        w.requestAnimationFrame = w[tag + 'RequestAnimationFrame'];
        w.cancelAnimationFrame = w[tag + 'CancelAnimationFrame'] || w[tag + 'CancelRequestAnimationFrame'];
    }
    w.requestAnimationFrame = w.requestAnimationFrame || function (callback, element) {
        var currTime = (new Date()).getTime(),
            timeToCall = Math.max(0, 1000 / 60 - (currTime - lastTime)),
            id = setTimeout(function () {
                callback(currTime + timeToCall)
            }, timeToCall);
        lastTime = currTime + timeToCall;
        return id;
    };
    w.cancelAnimationFrame = w.cancelAnimationFrame || function (id) {
        clearTimeout(id)
    };
    w.requestIdleCallback = w.requestIdleCallback || function (cb) {
        return setTimeout(function () {
            var start = Date.now();
            cb({
                didTimeout: false,
                timeRemaining: function () {
                    return Math.max(0, 50 - (Date.now() - start));
                }
            });
        }, 1);
    };
    w.cancelIdleCallback = w.cancelIdleCallback || function (id) {
        clearTimeout(id)
    };
};

new function () {
    var _to = Object.prototype.toString;
    ood.merge(ood, {
        stamp: function () {
            return +new Date()
        },
        rand: function () {
            return parseInt(ood.stamp() * Math.random(), 10).toString(36);
        },
        setTimeout: function (callback, delay) {
            return (delay === false || (delay || 0) > 1000 / 60) ? (setTimeout(callback, delay || 0) * -1) : requestAnimationFrame(callback);
        },
        clearTimeout: function (id) {
            if (id >= 0) cancelAnimationFrame(id);
            else clearTimeout(Math.abs(id));
        },
        fun: function () {
            return function () {
            }
        },
        exec: function (script, id, closure) {
            var me = this,
                d = document,
                h = d.getElementsByTagName("head")[0] || d.documentElement,
                s = d.createElement("script"), n;
            if (closure) script = "!function(){" + script + "}(window)";
            s.type = "text/javascript";
            if (id) {
                if ((n = d.getElementById(id)) && n.parentNode == h) {
                    h.removeChild(n);
                }
                s.id = id;
            }
            if (ood.browser.ie)
                s.text = script;
            else
                s.appendChild(d.createTextNode(script));
            h.appendChild(s);
            s.disalbed = true;
            s.disabled = false;
            if (!id) {
                h.removeChild(s);
            }
            return s;
        },
        /*
        get something from deep hash
        hash:target hash
        arr:path array,
        example:
        ood.get({a:{b:{c:1}}},['a','b']) => {c:1};
            ood.get({a:{b:{c:1}}},['a','b','c']) => 1;
            ood.get({a:{b:{c:1}}},['a','b','c','d']) => undefined;
        */
        get: function (hash, path) {
            if (!path) return hash;
            if (!ood.isSet(hash)) return undefined;
            else if (typeof path == 'string') return hash[path];
            else {
                for (var i = 0, l = path.length, t; i < l;) {
                    if (!(t = path[i++] + '')) continue;
                    if (!hash || (hash = t != (t = t.replace("()", "")) ? (typeof(hash[t]) == "function" && 0 !== t.indexOf("set")) ? hash[t]() : undefined : hash[t]) === undefined) return;
                }
                return hash;
            }
        },
        /*
        set/unset a value to deep hash
        example:
            ood.set({a:{b:{c:1}}},['a','b','c'],2) => {a:{b:{c:2}}}
            ood.set({a:{b:{c:1}}},['a','b','c']) => {a:{b:{}}}
        */
        set: function (hash, path, value) {
            if (!hash) return;
            if (typeof path != 'string') {
                var v, i = 0, m, last = path.length - 1;
                for (; i < last;) {
                    v = path[i++];
                    if (hash[v] && ((m = typeof hash[v]) == 'object' || m == 'function')) hash = hash[v];
                    else hash = hash[v] = {};
                }
                path = path[last];
            }
            // the last one can be a [set] function
            if (path != (path = (path + "").replace("()", ""))) {
                if (typeof(hash[path]) == "function") {
                    hash[path](value);
                    return value;
                }
            } else {
                if (value === undefined) {
                    if (hash.hasOwnProperty && hash.hasOwnProperty(path))
                        delete hash[path];
                    else hash[path] = undefined;
                } else {
                    return hash[path] = value;
                }
            }
        },
        /* try to excute a function
        fun:target function
        args:arguments for fun
        scope:[this] pointer for fun
        df:default return vale
        */
        tryF: function (fun, args, scope, df) {
            return (fun && typeof fun == 'function') ? fun.apply(scope || {}, args || []) : df
        },
        /*asynchronous run function
        fun:target function
        defer: setTimeout defer time
        args: arguments for fun
        scope: [this] pointer for fun
        */
        asyRun: function (fun, defer, args, scope) {
            //defer must set in opera
            return ood.setTimeout(typeof fun == 'string' ? function () {
                ood.exec(fun)
            } : function () {
                fun.apply(scope, args || []);
                fun = args = null;
            }, defer);
        },
        idleRun: function (fun, args, scope) {
            return window.requestIdleCallback(typeof fun == 'string' ? function () {
                ood.exec(fun)
            } : function () {
                fun.apply(scope, args || []);
                fun = args = null;
            });
        },
        asyHTML: function (content, callback, defer, size) {
            var div = document.createElement('div'),
                fragment = document.createDocumentFragment(),
                f = function () {
                    var i = size || 10;
                    while (--i && div.firstChild)
                        fragment.appendChild(div.firstChild);
                    if (div.firstChild)
                        ood.setTimeout(f, defer);
                    else
                        callback(fragment);
                };
            div.innerHTML = content;
            f();
        },
        isEmpty: function (hash) {
            if (hash == null) return true;
            if (ood.isNumb(hash)) return false;
            if (ood.isArr(hash) || ood.isStr(hash) || ood.isArguments(hash)) return hash.length === 0;
            for (var i in hash) if (Object.prototype.hasOwnProperty.call(hash, i)) return false;
            return true;
        },

        /*
        this will always run newer function
        key: for identify
        fun: to run
        defer: setTimeout defer time
        args: arguments for fun
        scope: 'this' for fun
        */
        resetRun: function (key, fun, defer, args, scope) {
            var me = ood.resetRun, k = key, cache = me.$cache || ((me.exists = function (k) {
                return this.$cache[k]
            }) && (me.$cache = {}));
            if (cache[k]) {
                ood.clearTimeout(cache[k])
            }
            if (typeof fun == 'function')
                cache[k] = ood.setTimeout(function () {
                    delete cache[k];
                    fun.apply(scope || null, args || [])
                }, defer);
            else delete cache[k];
        },
        //Dependencies: ood.Dom ood.Thread
        observableRun: function (tasks, onEnd, threadid, busyMsg) {
            ood.Thread.observableRun(tasks, onEnd, threadid, busyMsg);
        },

        /*break object memory link
        target: target object
        n: depth, default 1
        */
        breakO: function (target, depth, _layer) {
            var n = depth || 1, l = 1 + (_layer || 0), self = ood.breakO, _t = '___gc_', i;
            if (target && (typeof target == 'object' || typeof target == 'function') && target !== window && target !== document && target.nodeType !== 1) {
                try {
                    if (target.hasOwnProperty(_t)) return; else target[_t] = null
                } catch (e) {
                    return
                }
                try {
                    for (i in target) {
                        if (target.hasOwnProperty(i) && target[i]) {
                            if (typeof target[i] == 'object' || typeof target[i] == 'function')
                                if (l < n)
                                    self(target[i], n, l);
                            try {
                                target[i] = null
                            } catch (e) {
                            }
                        }
                    }
                } catch (e) {
                    return
                }
                if (target.length) target.length = 0;
                delete target[_t];
            }
        },

        /*each function for hash
        fun: fun to exec, if return false, stop the $iterator
        scope: 'this' pointer;
        */
        each: function (hash, fun, scope) {
            scope = scope || hash;
            for (var i in hash)
                if (false === fun.call(scope, hash[i], i, hash))
                    break;
            return hash;
        },

        /**
         * 动态获取类引用的方法
         * 用于解决组件包名硬编码问题
         * @param {string} className - 类名，如 'ood.UI.Image'
         * @returns {Function} - 类构造函数
         */
        getClass: function (className) {
            if (typeof className !== 'string') return className;

            var parts = className.split('.');
            var obj = window;

            for (var i = 0; i < parts.length; i++) {
                obj = obj[parts[i]];
                if (!obj) {
                    throw new Error('Class not found: ' + className);
                }
            }

            return obj;
        },

        /**
         * 检查类是否存在
         * @param {string} className - 类名
         * @returns {boolean} - 是否存在
         */
        hasClass: function (className) {
            try {
                ood.getClass(className);
                return true;
            } catch (e) {
                return false;
            }
        },
        compareVar: function (x, y, MAXL, MAXS) {
            if (x === y) return true;

            if (ood.isObj(x) || ood.isObj(y)) {
                if ((ood.isDate(x) && ood.isDate(y)) || (ood.isReg(x) && ood.isReg(y)))
                    return x + '' === y + '';
                else if ((ood.isHash(x) && ood.isHash(y)) || (ood.isArr(x) && ood.isArr(y)) || (ood.isArguments(x) && ood.isArguments(y))) {
                    x = ood.serialize(x, 0, 0, MAXL || 5, MAXS || 300);
                    y = ood.serialize(y, 0, 0, MAXL || 5, MAXS || 300);
                    return x.indexOf(ood.$_outofmilimted) == -1 && y.indexOf(ood.$_outofmilimted) == -1 && x === y;
                } else
                    return false;
            }
        },
        compareNumber: function (a, b, digits) {
            return ood.toFixedNumber(a, digits) === ood.toFixedNumber(b, digits);
        },
        toFixedNumber: function (number, digits) {
            if (!ood.isSet(digits)) digits = 2;
            var m = Math.abs(number),
                s = '' + Math.round(m * Math.pow(10, digits)),
                v, t, start, end;
            if (/\D/.test(s)) {
                v = "" + m;
            } else {
                while (s.length < 1 + digits) s = '0' + s;
                start = s.substring(0, t = (s.length - digits));
                end = s.substring(t);
                if (end) end = "." + end;
                v = start + end;
            }
            return parseFloat((number < 0 ? "-" : "") + v);
        },
        toNumeric: function (value, precision, groupingSeparator, decimalSeparator) {
            if (!ood.isNumb(value))
                value = parseFloat((value + "").replace(/\s*(e\+|[^0-9])/g, function (a, b, c) {
                    return b == 'e+' || b == 'E+' || (c == 0 && b == '-') ? b : b == decimalSeparator ? '.' : ''
                })) || 0;
            if (ood.isSet(precision) && precision >= 0)
                value = ood.toFixedNumber(value, precision);
            return value;
        },
        formatNumeric: function (value, precision, groupingSeparator, decimalSeparator, forceFillZero, trimTailZero) {
            if (ood.isSet(precision)) precision = parseInt(precision, 10);
            precision = (precision || precision === 0) ? precision : 0;
            groupingSeparator = ood.isSet(groupingSeparator) ? groupingSeparator : ",";
            decimalSeparator = decimalSeparator || ".";
            value = "" + parseFloat(value);
            if (value.indexOf('e') == -1) {
                value = ood.toFixedNumber(value, precision) + "";
                value = value.split(".");
                if (forceFillZero !== false) {
                    if ((value[1] ? value[1].length : 0) < precision) value[1] = (value[1] || "") + ood.str.repeat('0', precision - (value[1] ? value[1].length : 0));
                }
                value[0] = value[0].split("").reverse().join("").replace(/(\d{3})(?=\d)/g, "$1" + groupingSeparator).split("").reverse().join("");
                value = value.join(decimalSeparator);
            }
            return trimTailZero && value.indexOf(decimalSeparator) != -1 ? value.replace(new RegExp('[' + decimalSeparator + ']?0+$'), '') : value;
        },
        /***
         A wrapper for lots regExp string.replace to only once iterator replace
         You can use it, when
         1.replace >10
         2.need protect some regexp
         3.every long string to replac

         str: will be replace
         reg, array: [string, string] or [regex, string] or [[],[]]
         replace: to replace
         ignore_case: bool, for regexp symble 'i'
         return : replaced string

         For example:
         ood.replace("aAa","a","*",true)
         will return "*A*"
         ood.replace("aAa","a","*",false)
         will return "***"
         ood.replace("aAa","a","*")
         ood.replace("aAa",/a/,"*")         : "/a/" is OK, but not "/a/g"
         ood.replace("aAa",["a","*"])
         ood.replace("aAa",[["a","*"]])
         will return "***"
         ood.replace("aAa",[["a","*"],[/A/,"-"]])
         will return "*-*"
         Notice: there is a '$0' symbol here, for protect
         ood.replace("aba",[["ab","$0"],["a","*"]])
         will return "ab*"
         here, "ab" will be first matched and be protected to replace by express "a"
         ***/
        replace: function (str, reg, replace, ignore_case) {
            if (!str) return "";
            var i, len, _t, m, n, flag, a1 = [], a2 = [],
                me = arguments.callee,
                reg1 = me.reg1 || (me.reg1 = /\\./g),
                reg2 = me.reg2 || (me.reg2 = /\(/g),
                reg3 = me.reg3 || (me.reg3 = /\$\d/),
                reg4 = me.reg4 || (me.reg4 = /^\$\d+$/),
                reg5 = me.reg5 || (me.reg5 = /'/),
                reg6 = me.reg6 || (me.reg6 = /\\./g),
                reg11 = me.reg11 || (me.reg11 = /(['"])\1\+(.*)\+\1\1$/)
            ;

            if (!ood.isArr(reg)) {
                reg = [reg, replace]
            } else {
                ignore_case = replace
            }
            if (!ood.isArr(reg[0])) {
                reg = [reg]
            }
            ;
            ood.arr.each(reg, function (o) {
                m = typeof o[0] == 'string' ? o[0] : o[0].source;
                n = o[1] || "";
                len = ((m).replace(reg1, "").match(reg2) || "").length;
                if (typeof n != 'function') {
                    if (reg3.test(n)) {
                        //if only one paras and valid
                        if (reg4.test(n)) {
                            _t = parseInt(n.slice(1), 10);
                            if (_t <= len) n = _t;
                        } else {
                            flag = reg5.test(n.replace(reg6, "")) ? '"' : "'";
                            i = len;
                            while (i + 1)
                                n = n.split("$" + i).join(flag + "+a[o+" + i-- + "]+" + flag);

                            n = new Function("a,o", "return" + flag + n.replace(reg11, "$1") + flag);
                        }
                    }
                }
                a1.push(m || "^$");
                a2.push([n, len, typeof n]);
            });


            return str.replace(new RegExp("(" + a1.join(")|(") + ")", ignore_case ? "gim" : "gm"), function () {
                var i = 1, j = 0, args = arguments, p, t;
                if (!args[0]) return "";
                while (p = a2[j++]) {
                    if (t = args[i]) {
                        switch (p[2]) {
                            case 'function':
                                //arguments:
                                //1: array, all arguments;
                                //2: the data position index,  args[i] is $0;
                                //3: the regexp index
                                return p[0](args, i, j - 1);
                            case 'number':
                                return args[p[0] + i];
                            default:
                                return p[0];
                        }
                    } else {
                        i += p[1] + 1;
                    }
                }
            });
        },
        /*shadow copy for hash/array
        * var a=[]; a.b='b'; a.b will not be copied
        */
        copy: function (hash, filter) {
            return ood.clone(hash, filter, 1);
        },
        /*deep copy for hash/array, and hash/array only
        * var a=[]; a.b='b'; a.b will not be cloned
        *be careful for dead lock
        */
        clone: function (hash, filter, deep, _layer) {
            _layer = _layer || 0;
            if (hash && (ood.isHash(hash) || ood.isArr(hash))) {
                if (ood.isObj(hash)) {
                    var me = ood.clone,
                        isArr = ood.isArr(hash),
                        h = isArr ? [] : {},
                        i = 0, v, l;

                    if (!ood.isSet(deep)) deep = 100; else if (deep <= 0) return hash;

                    if (isArr) {
                        l = hash.length;
                        for (; i < l; i++) {
                            if (typeof filter == 'function' && false === filter.call(hash, hash[i], i, _layer + 1, h)) continue;
                            h[h.length] = ((v = hash[i]) && deep && (ood.isHash(v) || ood.isArr(v))) ? me(v, filter, deep - 1, _layer + 1) : v;
                        }
                    } else {
                        for (i in hash) {
                            if (filter === true ? i.charAt(0) == '_' :
                                filter === false ? (i.charAt(0) == '_' || i.charAt(0) == '$') :
                                    typeof filter == 'function' ? false === filter.call(hash, hash[i], i, _layer + 1, h) : 0)
                                continue;
                            h[i] = ((v = hash[i]) && deep && (ood.isHash(v) || ood.isArr(v))) ? me(v, filter, deep - 1, _layer + 1) : v;
                        }
                    }
                    return h;
                } else return hash;
            } else return hash;
        },
        /*filter hash/array
        filter: filter function(will delete "return false")
        */
        filter: function (obj, filter, force) {
            if (!force && obj && ood.isArr(obj)) {
                var i, l, v, a = [], o;
                for (i = 0, l = obj.length; i < l; i++) a[a.length] = obj[i];
                obj.length = 0;
                for (i = 0, l = a.length; i < l; i++)
                    if (typeof filter == 'function' ? false !== filter.call(a, a[i], i) : 1)
                        obj[obj.length] = a[i];
            } else {
                var i, bak = {};
                for (i in obj)
                    if (filter === true ? i.charAt(0) == '_' :
                        filter === false ? (i.charAt(0) == '_' || i.charAt(0) == '$') :
                            typeof filter == 'function' ? false === filter.call(obj, obj[i], i) : 0)
                        bak[i] = 1;

                for (i in bak)
                    delete obj[i];
            }
            return obj;
        },
        /*convert iterator to Array
        value: something can be iteratorred
        ood.toArr({a:1},true) => [a];
        ood.toArr({a:1},false) => [1];
        ood.toArr('a,b') => ['a','b'];
        ood.toArr('a;b',';') => ['a','b'];
        */
        toArr: function (value, flag) {
            if (!value) return [];
            var arr = [];
            //hash
            if (typeof flag == 'boolean')
                for (var i in value)
                    arr[arr.length] = flag ? i : value[i];
            //other like arguments
            else {
                if (ood.isHash(value)) {
                    for (var i in value) {
                        arr.push({key: i, value: value[i]});
                    }
                } else if (typeof value == 'string')
                    arr = value.split(flag || ',');
                else
                    for (var i = 0, l = value.length; i < l; ++i)
                        arr[i] = value[i];
            }
            return arr;
        },
        toUTF8: function (str) {
            return str.replace(/[^\x00-\xff]/g, function (a, b) {
                return '\\u' + ((b = a.charCodeAt()) < 16 ? '000' : b < 256 ? '00' : b < 4096 ? '0' : '') + b.toString(16)
            })
        },
        fromUTF8: function (str) {
            return str.replace(/\\u([0-9a-f]{3})([0-9a-f])/g, function (a, b, c) {
                return String.fromCharCode((parseInt(b, 16) * 16 + parseInt(c, 16)))
            })
        },
        urlEncode: function (hash) {
            var a = [], b = [], i, c, o;
            for (i in hash) {
                a[c = a.length] = b[b.length] = encodeURIComponent(i);
                if ((o = hash[i]) || o === 0) a[c] += '=' + encodeURIComponent(typeof o == 'string' ? o : ood.serialize(o));
            }
            a = ood.arr.stableSort(a, function (x, y, i, j) {
                return b[i] > b[j] ? 1 : b[i] == b[j] ? 0 : -1
            });
            return a.join('&');
        },
        urlDecode: function (str, key) {
            if (!str) return key ? '' : {};
            var arr, hash = {}, a = str.split('&'), o;
            for (var i = 0, l = a.length; i < l; i++) {
                o = a[i];
                arr = o.split('=');
                try {
                    hash[decodeURIComponent(arr[0])] = decodeURIComponent(arr[1] || '');
                } catch (e) {
                    hash[arr[0]] = arr[1] || '';
                }
            }
            return key ? hash[key] : hash;
        },
        getUrlParams: function (url) {
            return ood.urlDecode((url || location.href).replace(/^[^?]*[?!]+|^[^?]*$/, ''));
            // return ood.urlDecode((url || location.href).replace(/^[^#]*[#!]+|^[^#]*$/, ''));
        },
        preLoadImage: function (src, onSuccess, onFail) {
            if (ood.isArr(src)) {
                for (var i = 0, l = arr.length; i < l; i++)
                    ood.preLoadImage(src[i], onSuccess, onFail);
                return l;
            }
            var img = document.createElement("img");
            img.style.cssText = "position:absolute;left:-999px;top:-999px";
            img.width = img.height = 2;
            img.onload = function () {
                if (typeof onSuccess == 'function') onSuccess.call(this);
                this.onload = this.onerror = null;
                document.body.removeChild(this);
            };
            img.onerror = function () {
                if (typeof onFail == 'function') onFail.call(this);
                this.onload = this.onerror = null;
                document.body.removeChild(this);
            };
            document.body.appendChild(img);
            img.src = src;
            return 1;
        },
        // type detection
        isDefined: function (target) {
            return target !== undefined
        },
        isNull: function (target) {
            return target === null
        },
        isSet: function (target) {
            return target !== undefined && target !== null && target !== NaN
        },
        // including : object array function
        isObj: function (target) {
            return !!target && (typeof target == 'object' || typeof target == 'function')
        },
        isHash: function (target) {
            return !!target && _to.call(target) == '[object Object]' && target.constructor && /^\s*function\s+Object\(\s*\)/.test(target.constructor.toString()) && !Object.prototype.hasOwnProperty.call(target, "callee")
        },
        isBool: function (target) {
            return typeof target == 'boolean'
        },
        isNumb: function (target) {
            return typeof target == 'number' && isFinite(target)
        },
        isFinite: function (target) {
            return (target || target === 0) && isFinite(target) && !isNaN(parseFloat(target))
        },
        isDate: function (target) {
            return _to.call(target) === '[object Date]' && isFinite(+target)
        },
        isFun: function (target) {
            return _to.call(target) === '[object Function]'
        },
        isArr: function (target) {
            return _to.call(target) === '[object Array]'
        },
        isReg: function (target) {
            return _to.call(target) === '[object RegExp]'
        },
        isStr: function (target) {
            return _to.call(target) === '[object String]'
        },
        isArguments: function (target) {
            return target && (_to.call(target) === '[object Arguments]' || Object.prototype.hasOwnProperty.call(target, "callee"))
        },
        isEvent: function (target) {
            return target && ((/^(\[object (Keyboard|Mouse|Focus|Wheel|Composition|Storage)Event\])|(\[object Event\])$/.test(_to.call(target))) || (ood.isHash(target) && !!(target.$oodevent || target.$oodeventpara)))
        },
        isElem: function (target) {
            return !!(target && target.nodeType === 1)
        },
        isNaN: function (target) {
            return typeof target == 'number' && target != +target;
        },
        //for handling String
        str: {
            startWith: function (str, sStr) {
                return str.indexOf(sStr) === 0;
            },
            endWith: function (str, eStr) {
                var l = str.length - eStr.length;
                return l >= 0 && str.lastIndexOf(eStr) === l;
            },
            repeat: function (str, times) {
                return new Array(times + 1).join(str);
            },
            initial: function (str) {
                return str.charAt(0).toUpperCase() + str.substring(1);
            },
            trim: function (str) {
                return str ? str.replace(/^(\s|\uFEFF|\xA0)+|(\s|\uFEFF|\xA0)+$/g, '') : str;
            },
            ltrim: function (str) {
                return str ? str.replace(/^(\s|\uFEFF|\xA0)+/, '') : str;
            },
            rtrim: function (str) {
                return str ? str.replace(/(\s|\uFEFF|\xA0)+$/, '') : str;
            },
            /*
            blen : function(s){
                var _t=s.match(/[^\x00-\xff]/ig);
                return s.length+(null===_t?0:_t.length);
            },
            */
            //Dependencies: ood.Dom
            toDom: function (str) {
                var p = ood.$getGhostDiv(), r = [];
                p.innerHTML = str;
                for (var i = 0, t = p.childNodes, l = t.length; i < l; i++) r[r.length] = t[i];
                p = null;
                return ood(r);
            }
        },
        //for handling Array
        arr: {
            fastSortObject: function (arr, getKey) {
                if (!arr || arr.length < 2) return arr;

                var ll = arr.length,
                    zero = [],
                    len = (ll + "").length,
                    p = Object.prototype,
                    o, s, c, t;
                for (var i = 0; i < len; i++) zero[i] = new Array(len - i).join("0");
                for (var j = 0; j < ll; j++) {
                    s = j + '';
                    c = arr[j];
                    if (typeof c == "object") c._ood_$s$ = (ood.isSet(t = getKey.call(c, j)) ? t : '') + zero[s.length - 1] + s;
                }
                try {
                    o = p.toString;
                    p.toString = function () {
                        return this.hasOwnProperty('_ood_$s$') ? (this._ood_$s$) : (o.call(this));
                    };
                    arr.sort();
                } finally {
                    p.toString = o;
                    for (var j = 0; j < ll; j++) if (typeof arr[j] == "object") delete arr[j]._ood_$s$;
                }
                return arr;
            },
            stableSort: function (arr, sortby) {
                if (arr && arr.length > 1) {
                    for (var i = 0, l = arr.length, a = [], b = []; i < l; i++) b[i] = arr[a[i] = i];
                    if (ood.isFun(sortby))
                        a.sort(function (x, y) {
                            return sortby.call(arr, arr[x], arr[y], x, y) || (x > y ? 1 : -1);
                        });
                    else
                        a.sort(function (x, y) {
                            return arr[x] > arr[y] ? 1 : arr[x] < arr[y] ? -1 : x > y ? 1 : -1;
                        });
                    for (i = 0; i < l; i++) arr[i] = b[a[i]];
                    a.length = b.length = 0;
                }
                return arr;
            },
            subIndexOf: function (arr, key, value) {
                if (value === undefined) return -1;
                for (var i = 0, l = arr.length; i < l; i++)
                    if (arr[i] && arr[i][key] === value)
                        return i;
                return -1;
            },
            removeFrom: function (arr, index, length) {
                arr.splice(index, length || 1);
                return arr;
            },
            removeValue: function (arr, value) {
                for (var l = arr.length, i = l - 1; i >= 0; i--)
                    if (arr[i] === value)
                        arr.splice(i, 1);
                return arr;
            },
            intersection: function (a, b) {
                var ai = 0, bi = 0, result = [];
                while (ai < a.length && bi < b.length) {
                    if (a[ai] < b[bi]) ai++;
                    else if (a[ai] > b[bi]) bi++;
                    else {
                        result.push(a[ai]);
                        ai++;
                        bi++;
                    }
                }
                return result;
            },
            /*
             insert something to array
             arr: any
             index:default is length-1
             flag: is add array

             For example:
             insertAny([1,2],3)
                will return [1,2,3]
             insertAny([1,2],3,0)
                will return [3,1,2]
             insertAny([1,2],[3,4])
                will return [1,2,3,4]
             insertAny([1,2],[3,4],3,true)
                will return [1,2,[3,4]]
            */
            insertAny: function (arr, target, index, flag) {
                var l = arr.length;
                flag = (!ood.isArr(target)) || flag;
                if (index === 0) {
                    if (flag)
                        arr.unshift(target);
                    else
                        arr.unshift.apply(arr, target);
                } else {
                    var a;
                    if (!index || index < 0 || index > l) index = l;
                    if (index != l)
                        a = arr.splice(index, l - index);
                    if (flag)
                        arr[arr.length] = target;
                    else
                        arr.push.apply(arr, target);
                    if (a)
                        arr.push.apply(arr, a);
                }
                return index;
            },
            indexOf: function (arr, value) {
                for (var i = 0, l = arr.length; i < l; i++)
                    if (arr[i] === value)
                        return i;
                return -1;
            },

            /*
            fun: fun to apply
            desc: true - max to min , or min to max
            atarget: for this
            */
            each: function (arr, fun, scope, desc) {
                var i, l, a = arr;
                if (!a) return a;
                if (!ood.isArr(a)) {
                    if (!ood.isArr(a._nodes))
                        return a;
                    a = a._nodes;
                    if (desc === undefined)
                        desc = 1;
                }
                l = a.length;
                scope = scope || arr;
                if (!desc) {
                    for (i = 0; i < l; i++)
                        if (fun.call(scope, a[i], i, a) === false)
                            break;
                } else
                    for (i = l - 1; i >= 0; i--)
                        if (fun.call(scope, a[i], i, a) === false)
                            break;
                return arr;
            },
            removeDuplicate: function (arr, subKey) {
                var l = arr.length, a = arr.concat();
                arr.length = 0;
                for (var i = l - 1; i >= 0; i--) {
                    if (subKey ? this.subIndexOf(a, subKey, a[i][subKey]) === i : this.indexOf(a, a[i]) === i)
                        arr.push(a[i]);
                }
                return arr.reverse();
            }
        },
        _scope_set: function (dataMap) {
            if (window.get) ood._scope_bak = get;
            ood._scope_datamap = dataMap;
            window.get = function (key) {
                if (key) {
                    var t, i = (key = "" + key).indexOf("."), scope = i == -1 ? key : key.substr(0, i),
                        name = i == -1 ? null : key.substr(i + 1, key.length);
                    return (t = ood._scope_datamap) && (t = t[scope]) && (name ? t[name] : t);
                }
            };
        },
        _scope_clear: function (bak) {
            if (bak = ood._scope_bak) {
                window.get = bak;
                delete ood._scope_bak;
                delete ood._scope_datamap;
            }
        }
    });
};

ood.merge(ood.fun, {
    body: function (fun) {
        var s = "" + fun;
        s = s.replace(/(\s*\/\*[^*]*\*+([^\/][^*]*\*+)*\/)|(\s*\/\/[^\n]*)|(\)[\s\S]*)/g, function (a) {
            return a.charAt(0) != ")" ? "" : a
        });
        return s.slice(s.indexOf("{") + 1, s.lastIndexOf("}"));
    },
    args: function (fun) {
        var s = "" + fun;
        s = s.replace(/(\s*\/\*[^*]*\*+([^\/][^*]*\*+)*\/)|(\s*\/\/[^\n]*)|(\)[\s\S]*)/g, function (a) {
            return a.charAt(0) != ")" ? "" : a
        });
        s = s.slice(s.indexOf("(") + 1, s.indexOf(")")).split(/\s*,\s*/);
        return s[0] ? s : [];
    },
    clone: function (fun) {
        return new Function(ood.fun.args(fun), ood.fun.body(fun));
    }
});

ood.merge(ood.Class, {
    _reg: {$key: 1, $parent: 1, $children: 1, KEY: 1, Static: 1, Instance: 1, Constructor: 1, Initialize: 1},
    // give nodeType to avoid breakO
    _reg2: {
        'nodeType': 1,
        'constructor': 1,
        'prototype': 1,
        'toString': 1,
        'valueOf': 1,
        'hasOwnProperty': 1,
        'isPrototypeOf': 1,
        'propertyIsEnumerable': 1,
        'toLocaleString': 1
    },
    _all: [],
    /*envelop a function by some keys
    */
    _fun: function (fun, name, original, upper, type) {
        fun.$name$ = name;
        fun.$original$ = original;
        if (type) fun.$type$ = type;
        if (upper && fun !== upper) fun.upper = upper;
        return fun;
    },
    _other: ["toString", "valueOf"],
    /*envelop object's item from an object
    target: target object
    src: from object
     i: key in hash
    limit: envelop values in a hash
    */
    _o: {},
    //inherit from parents
    _inherit: function (target, src, instance) {
        var i, o, r = this._reg;
        for (i in src) {
            if (i in target || (!instance && r[i]) || i.charAt(0) == '$') continue;
            o = src[i];
            if (o && o.$ood$) continue;
            target[i] = o;
        }
    },
    //wrap
    _wrap: function (target, src, instance, parent, prtt) {
        var self = this, i, j, o, k = target.KEY, r = self._reg, r2 = self._reg2, f = self._fun, oo = self._other;
        for (i in src) {
            if (r2[i] || (!instance && r[i])) continue;
            o = src[i];
            target[i] = (typeof o != 'function') ? o : f(o, i, k, typeof parent[i] == 'function' && parent[i], prtt);
        }
        for (j = 0; i = oo[j++];) {
            o = src[i];
            if (o && (o == self._o[i])) continue;
            target[i] = (typeof o != 'function') ? o : f(o, i, k, typeof parent[i] == 'function' && parent[i], prtt);
        }
    },
    __gc: function (key) {
        var _c = ood.Class._all;
        if (!key) {
            for (var i = _c.length - 1; i > 0; i--)
                ood.Class.__gc(_c[i]);
            return;
        }
        if (typeof key == 'object') key = key.KEY || "";
        var t = ood.get(window, key.split('.')), s, i, j;
        if (t) {
            //remove from SC cache
            if (s = ood.get(window, ['ood', '$cache', 'SC'])) delete s[key];

            //remove parent link
            if (t.$parent)
                t.$parent.length = 0;

            //remove chidlren link
            //gc children
            if (s = t.$children) {
                //destroy children
                for (var i = 0, o; o = s[i]; i++)
                    if (o = ood.get(window, o.split('.')))
                        if (o.__gc)
                            o.__gc();
                s.length = 0;
            }

            //break function links
            for (i in t)
                if (i != 'upper' && typeof t[i] == 'function')
                    for (j in t[i])
                        if (t[i].hasOwnProperty(j))
                            delete t[i][j];
            ood.breakO(t);

            t = t.prototype;
            for (i in t)
                if (i != 'upper' && typeof t[i] == 'function')
                    for (j in t[i])
                        if (t[i].hasOwnProperty(j))
                            delete t[i][j];
            ood.breakO(t);

            //remove it out of window
            ood.set(window, key.split('.'));
        }

        _c.splice(_c[key], 1);
        delete _c[key];
    },
    destroy: function (key) {
        ood.Class.__gc(key)
    }
});

//function Dependencies: ood.Dom ood.Thread
ood.merge(ood, {
    version: 2.14,
    $DEFAULTHREF: 'javascript:;',
    $IEUNSELECTABLE: function () {
        return ood.browser.ie ? ' onselectstart="return false;" ' : ''
    },
    SERIALIZEMAXLAYER: 99,
    SERIALIZEMAXSIZE: 9999,

    $localeKey: 'cn',
    $localeDomId: 'xlid',
    $dateFormat: '',
    $rand: "_rnd_",
    _rnd: function () {
        return ood.debugMode ? ood.$rand + "=" + ood.rand() : null;
    },
    _debugPre: function (arr) {
        arr = ood.toArr(arr);
        arr[0] = "%c [" + arr[0] + "@ood]";
        ood.arr.insertAny(arr, 'color:#0000ff; font-style: italic;', 1);
        return arr;
    },
    _debugInfo: function () {
        if (ood.debugMode && ood.isDefined(window.console) && typeof(console.log) == 'function') {
            console.log.apply(console, ood._debugPre(arguments));
        }
    },
    _debugGroup: function () {
        if (ood.debugMode && ood.isDefined(window.console) && typeof(console.group) == 'function') {
            console.group.apply(console, ood._debugPre(arguments));
        } else ood._debugInfo.apply(ood, arguments);
    },
    _debugGroupEnd: function () {
        if (ood.debugMode && ood.isDefined(window.console) && typeof(console.groupEnd) == 'function') {
            console.groupEnd();
        } else ood._debugInfo.apply(ood, arguments);
    },
    SpaceUnit: 'em',
    $us: function (p) {
        // ie67 always px
        return (ood.browser.ie6 || ood.browser.ie7) ? p ? -2 : -1 :
            (p = p ? (p._spaceUnit || (p.properties && p.properties.spaceUnit)) : '') == 'px' ? -2 : p == 'em' ? 2 :
                ood.SpaceUnit == 'px' ? -1 : ood.SpaceUnit == 'em' ? 1 : 0;
    },
    // for show ood.echo
    debugMode: true,

    Locale: {},
    constant: {},
    $cache: {
        thread: {},
        SC: {},
        clsByURI: {},
        fetching: {},
        hookKey: {},
        hookKeyUp: {},
        snipScript: {},

        subscribes: {},

        //ghost divs
        ghostDiv: [],
        data: {},
        callback: {},
        functions: {},
        //cache purge map for dom element
        domPurgeData: {},
        //cache DomProfile or UIProfile
        profileMap: {},
        //cache the reclaim serial id for UIProfile
        reclaimId: {},
        //cache built template for UIProfile
        template: {},
        //cache [key]=>[event handler] map for UIProfile
        UIKeyMapEvents: {},
        droppable: {},
        unique: {}
    },
    subscribe: function (topic, subscriber, receiver, asy) {
        if (topic === null || topic === undefined || subscriber === null || subscriber === undefined || typeof receiver != 'function') return;
        var c = ood.$cache.subscribes, i;
        c[topic] = c[topic] || [];
        i = ood.arr.subIndexOf(c[topic], "id", subscriber);
        if (i != -1) ood.arr.removeFrom(c[topic], i);
        return c[topic].push({id: subscriber, receiver: receiver, asy: !!asy});
    },
    unsubscribe: function (topic, subscriber) {
        var c = ood.$cache.subscribes, i;
        if (!subscriber) {
            if (topic === null || topic === undefined)
                c = {};
            else
                delete c[topic];
        } else if (c[topic]) {
            i = ood.arr.subIndexOf(c[topic], "id", subscriber);
            if (i != -1) ood.arr.removeFrom(c[topic], i);
        }
    },
    publish: function (topic, args, subscribers, scope) {
        var c = ood.$cache.subscribes;
        if (topic === null || topic === undefined) {
            for (var topic in c) {
                ood.arr.each(c[topic], function (o) {
                    if (!subscribers || subscribers === o.id || (ood.isArr(subscribers) && ood.arr.indexOf(subscribers, o.id) != -1)) {
                        if (o.asy)
                            ood.asyRun(o.receiver, 0, args, scope);
                        else
                            return ood.tryF(o.receiver, args, scope, true);
                    }
                });
            }
        } else if (c[topic]) {
            ood.arr.each(c[topic], function (o) {
                if (!subscribers || subscribers === o.id || (ood.isArr(subscribers) && ood.arr.indexOf(subscribers, o.id) != -1)) {
                    if (o.asy)
                        ood.asyRun(o.receiver, 0, args, scope);
                    else
                        return ood.tryF(o.receiver, args, scope, true);
                }
            });
        }
    },
    getSubscribers: function (topic) {
        return (topic === null || topic === undefined) ? ood.$cache.subscribes : ood.$cache.subscribes[topic];
    },

    setDateFormat: function (format) {
        ood.$dateFormat = format
    },
    getDateFormat: function (format) {
        return format || ood.$dateFormat || ood.$cache.data.$DATE_FORMAT
    },

    setAppLangKey: function (key) {
        ood.$appLangKey = key
    },
    getAppLangKey: function (key) {
        return ood.$appLangKey
    },
    getLang: function () {
        return ood.$localeKey
    },
    setLang: function (key, onOK, callback) {
        var g = ood.getRes, t, v, i, j, f, m, z, a = [], l;
        ood.$localeKey = key;
        v = document.getElementsByTagName ? document.getElementsByTagName('span') : document.all && document.all.tags ? document.all.tags('span') : null;
        if (!v) return;
        for (i = 0; t = v[i]; i++) if (t.id == ood.$localeDomId) a[a.length] = t;
        l = a.length;
        f = function () {
            var ff = function () {
                j = a.splice(0, 100);
                for (i = 0; t = j[i]; i++)
                    if (t.className && typeof(v = g(t.className)) == 'string')
                        t.innerHTML = v;
                if (a.length)
                    ood.setTimeout(ff, 0);
                ood.tryF(callback, [a.length, l]);
                if (!a.length)
                    ood.tryF(onOK, [0, l]);
            };
            ff();
        },


            z = 'ood.Locale.' + key,
            m = function () {
                var k = ood.$appLangKey;
                if (k) ood.include(z + '.' + k, ood.getPath('ood.Locale.' + key, '.js'), f, f);
                else f();
            };
        // use special key to invoid other lang setting was loaded first
        ood.include(z + '.inline.$_$', ood.getPath(z, '.js'), m, m);
    },
    getTheme: function (a) {
        try {
            a = ood.CSS.$getCSSValue('.setting-uikey', 'fontFamily');
        } catch (e) {
        } finally {
            return a || "default";
        }
    },
    setTheme: function (key, refresh, onSucess, onFail, tag) {
        key = key || 'default';
        var okey = ood.getTheme();
        if (key != okey) {
            var onend = function (onSucess) {
                if (okey != 'default') {
                    var style;
                    while (style = ood.CSS.$getCSSValue('.setting-uikey', 'fontFamily', okey)) {
                        style.disabled = true;
                        style.parentNode.removeChild(style);
                        style = null;
                    }
                }
                if (refresh !== false)
                    ood.CSS.adjustFont();
                ood.tryF(onSucess);
            };
            if (key == 'default') {
                onend(onSucess);
            } else {
                try {
                    var tkey = ood.CSS.$getCSSValue('.setting-uikey', 'fontFamily');
                } catch (e) {
                    console.log(e);
                } finally {
                    if (tkey == key) {
                        ood.tryF(onSucess);
                        return;
                    } else {
                        var id = 'theme:' + key,
                            path = ood.getPath('ood.appearance.' + key, '');
                        if (tag) {
                            ood.getFileAsync(path + 'theme.css', function (rsp) {
                                rsp = ood.replace(rsp, [
                                    [/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/, '$0'],
                                    [/\{[^}]*\}/, '$0'],
                                    [/([^\/{},]+)/, function (a) {
                                        // protect '.setting-uikey'
                                        return ood.str.endWith(a[0], '.setting-uikey') ? a[0] : a[0].replace(/([^\s>]+)/, "$1" + tag)
                                    }]
                                ]);
                                rsp = rsp.replace(/url\(([^)]+)\)/g, "url(" + path + "$1)");
                                ood.CSS._appendSS(ood('head'), rsp, id, false);
                            });
                        } else
                            ood.CSS.includeLink(path + 'theme.css', id);
                    }

                    var count = 0, fun = function () {
                        // timeout: 21 seconds
                        if (count++ > 5) {
                            fun = count = null;
                            if (false !== ood.tryF(onFail))
                                throw 'errLoadTheme:' + key;
                            return;
                        }
                        //test
                        try {
                            var tkey = ood.CSS.$getCSSValue('.setting-uikey', 'fontFamily');
                        } catch (e) {
                        } finally {
                            if (tkey == key) {
                                onend(onSucess);
                                fun = count = null;
                            } else {
                                ood.asyRun(fun, 100 * count);
                            }
                        }
                    };
                    fun();
                }
            }
        } else {
            ood.tryF(onSucess);
        }
    },
    reLayout: function () {
        if (ood.UI) ood.UI.getAll().reLayout(true);
    },
    _langParamReg: /\x24(\d+)/g,
    _langscMark: /[$@{][\S]+/,
    // locale  pattern  :  $*  $a  $a.b.c  $(a.b.c- d)
    // variable pattern: @a.b.c@  @a@  {!}  {a.b.c}
    _langReg: /((\$)([^\w\(]))|((\$)([\w][\w\.-]*[\w]+))|((\$)\(([\w][\w\.]*[^)\n\r]+))\)|((\$)([^\s]))|((\@)([\w][\w\.]*[\w]+)(\@?))|((\@)([^\s])(\@?))|((\{)([~!@#$%^&*+-\/?.|:][\w\[\]]*|[\w\[\]]+(\(\))?(\.[\w\[\]]+(\(\))?)*)(\}))/g,
    _escapeMap: {
        "$": "\x01",
        ".": "\x02",
        "-": "\x03",
        ")": "\x04",
        "@": "\x05"
    },
    _unescapeMap: {
        "\x01": "$",
        "\x02": ".",
        "\x03": "-",
        "\x04": ")",
        "\x05": "@"
    },

    getResText: function (path) {
        var resPath = this.getRes(path);

        if (ood.str.endWith(path, "." + resPath)) {
            return undefined;
        } else {
            return resPath;
        }
    },
    //test1: ood.getRes("start.a.b.c $0 $1 ($- $. $$) end-1-2")  => "c 1 2 (- . $) end"
    //tset2: ood.getRes( ["a","b","c $0 $1 ($- $. $$) end"],1,2) => "c 1 2 (- . $) end"
    getRes: function (path) {
        var arr, conf, tmp, params = arguments, rtn;
        if (ood.isStr(path)) {
            path = path.replace(/\$([$.-])/g, function (a, b) {
                return ood._escapeMap[b] || a;
            });
            if (path.charAt(0) == '$') path = path.slice(1);
            if (path.indexOf('-') != -1) {
                tmp = path.split('-');
                path = tmp[0];
                params = tmp;
            } else if (ood.isArr(params[1])) {
                params = params[1];
                params.unshift(path);
            }
            arr = path.split(".");
            arr[arr.length - 1] = arr[arr.length - 1].replace(/([\x01\x02\x03\x04])/g, function (a) {
                return ood._unescapeMap[a];
            });
        } else if (ood.isArr(path)) {
            arr = path;
        } else {
            return path;
        }
        conf = ood.get(ood.Locale[ood.$localeKey], arr);
        if ((tmp = typeof conf) == 'function') {
            return conf.apply(null, params);
        } else if (tmp == 'object') {
            return conf;
        } else {
            conf = tmp == 'string' ? conf.replace(/\$([$.-])/g, function (a, b) {
                return ood._escapeMap[b] || a;
            }) : arr[arr.length - 1];
            rtn = params.length > 1 ? conf.replace(ood._langParamReg, function (z, id, k) {
                k = params[1 + +id];
                return (k === null || k === undefined) ? z : k
            }) : conf;
            return rtn.replace(/([\x01\x02\x03])/g, function (a) {
                return ood._unescapeMap[a];
            });
        }
    },
    wrapRes: function (id) {
        if (!ood.isStr(id)) return id;
        var i = id, s, r;
        if (i.charAt(0) == '$') arguments[0] = i.substr(1, i.length - 1);
        s = id;
        r = ood.getRes.apply(null, arguments);
        if (s == r) r = i;
        return '<span id="' + ood.$localeDomId + '" class="' + s.replace(/([\x01\x02\x03\x04])/g, function (a) {
            return '$' + ood._unescapeMap[a];
        }) + '" ' + ood.$IEUNSELECTABLE() + '>' + r + '</span>';
    },
    //test1: ood.adjustRes("$(start.a.b.c $0 $1 ($- $. $$$) end-1-2)"); => "c 1 2 (- . $) end"
    adjustRes: function (str, wrap, onlyBraces, onlyVars, params, scope1, scope2) {
        if (!ood.isStr(str)) return str;
        wrap = wrap ? ood.wrapRes : ood.getRes;
        str = str.replace(/\$([\$\.\-\)])/g, function (a, b) {
            return ood._escapeMap[b] || a;
        });
        str = ood._langscMark.test(str) ? str.replace(ood._langReg, function (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) {
            // protect $@{
            return c == '$' ? onlyVars ? a : d :
                // $a.b.c-1-3
                f == '$' ? onlyVars ? a : wrap(g, params) :
                    // $(a.b.c-d)
                    i == '$' ? onlyVars ? a : wrap(j, params) :
                        // $a
                        l == '$' ? onlyVars ? a : wrap(m, params) :
                            // variable: @a@ @a.b.c@ {a.b.c}
                            ((onlyBraces ? 0 : (o == '@' || s == '@')) || w == "{") ? ((z = ood.SC.get(o == "@" ? p : s == "@" ? t : x, scope1, scope2)) || (ood.isSet(z) ? z : ""))
                                : a;
        }) : str;
        return str.replace(/([\x01\x02\x03\x04])/g, function (a) {
            return ood._unescapeMap[a];
        });
    },
    adjustVar: function (obj, scope1, scope2) {
        var t;
        return typeof(obj) == "string" ?
            obj == "{[]}" ? [] :
                obj == "{{}}" ? {} :
                    obj == "{}" ? "" :
                        obj == "{true}" ? true :
                            obj == "{false}" ? false :
                                obj == "{NaN}" ? NaN :
                                    obj == "{null}" ? null :
                                        obj == "{undefined}" ? undefined :
                                            obj == "{now}" ? new Date() :
                                                (t = /^\s*\{((-?\d\d*\.\d*)|(-?\d\d*)|(-?\.\d\d*))\}\s*$/.exec(obj)) ? parseFloat(t[1]) :
                                                    // {a.b(3,"a")}
                                                    // scope allows hash only
                                                    (t = /^\s*\{([\w\.]+\([^)]*\))\s*\}\s*$/.exec(obj)) && (scope1 || scope2) && ood.isHash(scope1 || scope2) ? (new Function("try{return this." + t[1] + "}catch(e){}")).call(scope1 || scope2) :
                                                        //{a.b.c} or {prf.boxing().getValue()}
                                                        (t = /^\s*\{([^}]+)\}\s*$/.exec(obj)) ?
                                                            ood.SC.get(t[1], scope1, scope2)
                                                            : ood.adjustRes(obj, false, true, true, null, scope1, scope2)
            : obj;
    },
    _getrpc: function (uri, query, options) {
        var t = (options && options.proxyType) ? options.proxyType.toLowerCase() : "";

        return (t == "sajax" || t == "jsonp") ? ood.JSONP
            : (t == "iajax" || t == "xdmi") ? ood.XDMI
                : (t == "ajax") ? ood.Ajax
                    // include a file => XDMI
                    : (typeof query == 'object' && ((function (d) {
                        if (!ood.isHash(d)) return 0;
                        for (var i in d) if ((d[i] && d[i].nodeType == 1 && d[i].nodeName == "INPUT") || (d[i] && d[i].$oodFileCtrl)) return 1
                    })(query))) ? ood.XDMI
                        // post: crossdomain => XDMI, else Ajax
                        : (options && options.method && options.method.toLowerCase() == 'post') ? ood.absIO.isCrossDomain(uri) ? ood.XDMI : ood.Ajax
                            // get : crossdomain => JSONP, else Ajax
                            : ood.absIO.isCrossDomain(uri) ? ood.JSONP : ood.Ajax;
    },
    request: function (uri, query, onSuccess, onFail, threadid, options) {
        return ood._getrpc(uri, query, options).apply(null, arguments).start();
    },
    ajax: function (uri, query, onSuccess, onFail, threadid, options) {
        return ood.Ajax.apply(null, arguments).start();
    },
    jsonp: function (uri, query, onSuccess, onFail, threadid, options) {
        return ood.JSONP.apply(null, arguments).start();
    },
    xdmi: function (uri, query, onSuccess, onFail, threadid, options) {
        return ood.XDMI.apply(null, arguments).start();
    },
    restGet: function (uri, query, onSuccess, onFail, threadid, options) {
        if (!options) options = {};
        options.method = "get";
        return ood.Ajax(uri, query, onSuccess, onFail, threadid, options).start();
    },
    restPost: function (uri, query, onSuccess, onFail, threadid, options) {
        if (!options) options = {};
        options.method = "post";
        return ood.Ajax(uri, query, onSuccess, onFail, threadid, options).start();
    },
    restPut: function (uri, query, onSuccess, onFail, threadid, options) {
        if (!options) options = {};
        options.method = "put";
        return ood.Ajax(uri, query, onSuccess, onFail, threadid, options).start();
    },
    restDelete: function (uri, query, onSuccess, onFail, threadid, options) {
        if (!options) options = {};
        options.method = "delete";
        return ood.Ajax(uri, query, onSuccess, onFail, threadid, options).start();
    },
    getFileSync: function (uri, onSuccess, onFail, options) {
        return ood.Ajax(uri, options && options.force ? ood._rnd() : null, onSuccess, onFail, null, ood.merge({
            asy: false,
            rspType: options && options.rspType || "text"
        }, options, 'without')).start() || null;
    },
    getFileAsync: function (uri, onSuccess, onFail, threadid, options) {
        ood.Ajax(uri, options && options.force ? ood._rnd() : null, onSuccess, onFail, threadid, ood.merge({
            asy: true,
            rspType: options && options.rspType || "text"
        }, options, 'without')).start();
    },
    include: function (id, path, onSuccess, onFail, sync, options) {
        if (id && ood.SC.get(id))
            ood.tryF(onSuccess);
        else {
            options = options || {};
            var rnd = options.force ? ood._rnd() : null;
            options.rspType = 'script';
            if (!sync) {
                options.checkKey = id;
                ood.JSONP(path, rnd, onSuccess, onFail, 0, options).start()
            } else {
                options.asy = !sync;
                ood.Ajax(path, rnd, function (rsp) {
                    try {
                        ood.exec(rsp, id)
                    }
                    catch (e) {
                        ood.tryF(onFail, [e.name + ": " + e.message])
                    }
                    ood.tryF(onSuccess);
                }, onFail, 0, options).start();
            }
        }
    },
    mailTo: function (email, subject, body, cc, bcc) {
        if (ood.isHash(subject)) {
            bcc = subject.bcc || "";
            cc = subject.cc || "";
            body = subject.body || "";
            subject = subject.subject || "";
        }
        var url = 'mailto:' + email +
            '?subject=' + encodeURIComponent(ood.adjustRes(subject || ""))
            + '&body= ' + encodeURIComponent(ood.adjustRes(body || ""))
            + '&cc= ' + (cc || "")
            + '&bcc= ' + (bcc || "");
        ood.XDMI(url).start();
    },
    fetchClass: function (uri, onSuccess, onFail, onAlert, force, threadid, options) {
        options = options || {};
        var isPath = options.uri || /\//.test(uri) || /\.js$/i.test(uri),
            c = ood.$cache.clsByURI,
            onFetching = ood.$cache.fetching,
            clearFetching = function () {
                for (var i in onFetching[uri][3]) ood.Thread.abort(onFetching[uri][3][i]);
                if (onFetching[uri]) {
                    onFetching[uri][0].length = 0;
                    onFetching[uri][1].length = 0;
                    onFetching[uri][2].length = 0;
                    onFetching[uri][3].length = 0;
                    onFetching[uri].length = 0;
                    delete onFetching[uri];
                }
                onFetching = null;
            },
            rnd = options.force ? ood._rnd() : null,
            cls, obj;

        if (isPath) {
            cls = ood.getClassName(uri);
            if (cls && ood.SC.get(cls))
                isPath = false;
        } else {
            // special path( dont use any dynamic
            if (!options.hasOwnProperty('appPath') && window["/"]) options.appPath = window["/"];
            cls = uri;
            if (uri.indexOf(".cls") > 0) {
                uri = ood.getPath(uri, '.cls', '', options);
            } else if (uri.indexOf(".dyn") > 0) {
                uri = ood.getPath(uri, '.dyn', '', options);
            } else {
                uri = ood.getPath(uri, '.jsx', '', options);
            }
        }
        if (!force && (isPath ? ((obj = c[uri]) && obj.$ood$) : (obj = ood.SC.get(cls))))
            ood.tryF(onSuccess, [uri, cls], obj);
        else {
            // For fetching one class multiple times
            if (!onFetching[uri]) {
                onFetching[uri] = [onSuccess = onSuccess ? [onSuccess] : [], onFail = onFail ? [onFail] : [], onAlert = onAlert ? [onAlert] : [], []];
                if (!cls || options || ood.absIO.isCrossDomain(uri)) {
                    if (cls) {
                        ood.Class._ignoreNSCache = 1;
                        ood.Class._last = null;
                        if (rnd) {
                            rnd = rnd + "&tag=" + cls;
                        } else {
                            rnd = "?tag=" + cls;
                        }

                    }
                    ood.JSONP(uri, rnd, function () {
                        if (cls) {
                            if (ood.Class._last) obj = c[uri] = ood.Class._last;
                            ood.Class._ignoreNSCache = ood.Class._last = null;
                            if (obj) {
                                for (var i = 0, l = onSuccess.length; i < l; i++) ood.tryF(onSuccess[i], [uri, obj.KEY], obj);
                            }
                            else {
                                for (var i = 0, l = onFail.length; i < l; i++) ood.tryF(onFail[i], ood.toArr(arguments));
                            }
                            var s = ood.getClassName(uri, options.appPath);
                        } else {
                            for (var i = 0, l = onSuccess.length; i < l; i++) ood.tryF(onSuccess[i], [uri]);
                        }
                        clearFetching();
                    }, function () {
                        if (cls) {
                            ood.Class._ignoreNSCache = 1;
                            ood.Class._last = null;
                        }
                        for (var i = 0, l = onFail.length; i < l; i++) ood.tryF(onFail[i], ood.toArr(arguments));
                        // for Thread.group in fetchClasses
                        clearFetching();
                    }, threadid, {rspType: 'script', tag: cls}).start();
                } else {
                    ood.Ajax(uri, rnd, function (rsp) {
                        ood.Class._ignoreNSCache = 1;
                        ood.Class._last = null;
                        var scriptnode;
                        var s = ood.getClassName(uri, options.appPath);
                        try {
                            scriptnode = ood.exec(rsp, s)
                        } catch (e) {
                            for (var i = 0, l = onFail.length; i < l; i++) ood.tryF(onFail[i], [e.name + ": " + e.message]);
                            ood.Class._last = null;
                        }
                        if (ood.Class._last) obj = c[uri] = ood.Class._last;
                        ood.Class._ignoreNSCache = ood.Class._last = null;
                        if (obj) {
                            for (var i = 0, l = onSuccess.length; i < l; i++) ood.tryF(onSuccess[i], [uri, obj.KEY], obj);
                        }
                        else {
                            for (var i = 0, l = onFail.length; i < l; i++) ood.tryF(onFail[i], ood.toArr(arguments));
                        }
                        // if(obj&&obj.KEY!=s){
                        //     var msg="[ood] > The last class name in '"+uri+"' should be '"+s+"', but it's '"+obj.KEY+"'!";
                        //     for(var i=0,l=onAlert.length;i<l;i++)ood.tryF(onAlert[i], [msg, uri, s,  obj.KEY]);
                        //     ood.log( msg );
                        // }
                        // for Thread.group in fetchClasses
                        clearFetching();
                    }, function () {
                        ood.Class._ignoreNSCache = ood.Class._last = null;
                        for (var i = 0, l = onFail.length; i < l; i++) ood.tryF(onFail[i], ood.toArr(arguments));
                        // for Thread.group in fetchClasses
                        clearFetching();
                    }, threadid, {rspType: 'text', asy: true, tag: cls}).start();
                }
            } else {
                if (onSuccess) onFetching[uri][0].push(onSuccess);
                if (onFail) onFetching[uri][1].push(onFail);
                if (onAlert) onFetching[uri][2].push(onAlert);
                if (threadid) {
                    onFetching[uri][3].push(threadid);
                    ood.Thread.suspend(threadid);
                }
            }
        }
    },
    fetchClasses: function (uris, onEnd, onSuccess, onFail, onAlert, force, threadid, options) {
        var hash = {}, f = function (uri, i, hash) {
            hash[i] = ood.Thread(null, [function (tid) {
                if (uri) {
                    ood.fetchClass(uri, onSuccess, onFail, onAlert, force, tid, options);
                }

            }]);
        };
        for (var i = 0, l = uris.length; i < l; i++) f(uris[i], i, hash);
        return ood.Thread.group(null, hash, null, function () {
            ood.Thread.suspend(threadid);
        }, function () {
            ood.tryF(onEnd, arguments, this);
            ood.Thread.resume(threadid);
        }).start();
    },
    // Recursive require
    require: function (clsArr, onEnd, onSuccess, onFail, onAlert, force, threadid, options) {
        if (ood.isStr(clsArr)) clsArr = [clsArr];
        var fun = function (paths, tid) {
            ood.fetchClasses(paths, function () {
                var a2 = [], obj, r;
                for (var i = 0, l = paths.length; i < l; i++) {
                    obj = ood.SC.get(paths[i]);
                    //collect  required class
                    if (obj && (r = obj.Required) && r.length) {
                        for (var j = 0, m = r.length; j < m; j++) {
                            if (!ood.SC.get(r[j])) a2.push(r[j]);
                        }
                    }
                    // if it's module, collect required class in iniComponents
                    if (obj && obj['ood.Module'] && (obj = obj.prototype && obj.prototype.iniComponents)) {
                        ood.fun.body(obj).replace(/\bood.create\s*\(\s*['"]([\w.]+)['"]\s*[,)]/g, function (a, b) {
                            if (!(a = ood.SC.get(b))) {
                                a2.push(b);
                                a = null;
                            }
                            // if(force && a && a['ood.Module']){
                            //     a2.push(b);
                            // }
                        });
                    }
                }
                if (a2.length) {
                    fun(a2, null);
                } else {
                    var arr = [];
                    for (var i = 0, l = clsArr.length; i < l; i++) {
                        arr.push(ood.SC.get(clsArr[i]));
                    }
                    if (onEnd) onEnd.apply(null, arr);
                }
            }, onSuccess, onFail, onAlert, force, tid, options);
        };
        fun(clsArr, threadid);
    },
    /*
    set application main function
    example:
        ood.main(function(){
            ...
        });
    */
    _m: [],
    main: function (fun) {
        if (ood.arr.indexOf(ood._m, fun) == -1)
            ood._m.push(fun);
        // run it now
        if (ood.isDomReady) {
            ood._domReadyFuns();
        }
    },
    /*
    key: ood.UI.xxx
    tag: file tag
    add: appearance or bahavior
    example:
        ood.getPath('ood.UI.Button','','appearance') => ood.ini.path + /appearance/UI/Button/
        ood.getPath('ood.UI.Button','.gif','appearance') => ood.ini.path + /appearance/UI/Button.gif
        ood.getPath('a.b','','appearance') => ood.ini.appPath + /a/appearance/b/"
        ood.getPath('a.b','.gif','appearance') => ood.ini.appPath + /a/appearance/b.gif"
    */
    getPath: function (key, tag, folder, options) {
        if (!key) {
            return tag;
        }

        if (tag == '.cls') {
            key = key.split('.');
            key = key.splice(0, key.length - 1);
            return key.join('\/') + '.cls';
        }
        if (tag == '.dyn') {
            key = key.split('.');
            key = key.splice(0, key.length - 1);
            return key.join('\/') + '.dyn';
        }

        if (tag == '.jsx') {
            key = key.split('.');
            return "/" + key.join('\/') + '.jsx';

        }

        if (key.indexOf('ood.appearance.') == 0) {
            key = key.split('.');
            return "/" + key.join('\/') + "/";
        }

        if (key.indexOf('ood.UI.') == 0) {
            key = key.split('.');
            return "/" + key.join('\/') + ".js";
        }

        if (key.indexOf('ood.Locale.') == 0) {
            key = key.split('.');
            return "/" + key.join('\/') + ".js";
        }

        if (key.indexOf('root.') == 0) {
            key = key.split('.');
            return "/" + key.join('\/') + '.js';
        }

        if (key.indexOf('custom.') == 0) {
            key = key.split('.');
            return "" + key.join('\/') + '.cls';
        }

        if (ood.str.startWith(key, 'RAD.')) {
            key = key.split('.');
            return "/RAD/" + key.join('\/') + '.js';
        }

        if (ood.str.startWith(key, '/RAD/')) {
            return key + tag;
        }

        key = key.split('.');
        if (folder) {
            var a = [key[0], folder];
            for (var i = 1, l = key.length; i < l; i++)
                a.push(key[i]);
            key.length = 0;
            key = a;
        }

        var pre, ini = ood.ini, t,
            ensureTag = function (s) {
                return s && s.slice(-1) != "/" ? s + "/" : s;
            };
        if (key[0] == 'ood') {
            key.shift();
            if (key.length == (folder ? 1 : 0)) key.push('ood');

            pre = ensureTag((options && options.oodPath) || ini.path);
        } else {
            if (key.length == ((folder ? 1 : 0) + 1) && tag == '.js') key.push('index');

            if (pre = ensureTag(options && options.appPath)) {
                if (t = (options && options.verPath)) pre += ensureTag(t);
                if (t = (options && options.ver)) pre += ensureTag(t);
            } else if (pre = ensureTag(ini.appPath)) {
                if (t = ini.verPath) pre += ensureTag(t);
                if (t = ini.ver) pre += ensureTag(t);
            }
        }
        return key.join('\/') + (tag || '\/');

        // return pre + key.join('\/') + (tag || '\/');
    },

    getClassName: function (uri, appPath) {
        if (uri && ood.isStr(uri)) {
            if (appPath) {
                uri = uri.replace(appPath + "/", "")
            }
            return uri.replace(/\//g, ".").replace(/\.js$/i, "").replace(/\.jsx$/i, "").replace(/\.vv$/i, "").replace(/\.cls$/i, "").replace();
        }
    },
    log: ood.fun(),
    echo: ood.fun(),
    message: ood.fun(),

    //profile object cache
    _pool: [],
    getObject: function (id) {
        return ood._pool['$' + id]
    },
    getObjectByAlias: function (alias) {
        var o, a = [], l = 0;
        for (var i in ood._pool) {
            o = ood._pool[i];
            if (('alias' in o) && o.alias === alias) {
                a.push(o);
                l++;
            }
        }
        return l === 0 ? null : l === 1 ? a[0] : a;
    },
    _ghostDivId: "ood.ghost::",
    $getGhostDiv: function () {
        var pool = ood.$cache.ghostDiv,
            i = 0, l = pool.length, p;
        do {
            p = pool[i++]
        } while (i < l && (p && p.firstChild))
        if (!p || p.firstChild) {
            p = document.createElement('div');
            p.id = ood._ghostDivId;
            pool.push(p);
        }
        return p;
    },
    //for handling dom element
    $xid: 0,
    $registerNode: function (o) {
        //get id from cache or id
        var id, v, purge = ood.$cache.domPurgeData;
        if (!(o.$xid && (v = purge[o.$xid]) && v.element == o)) {
            id = '!' + ood.$xid++;
            v = purge[id] || (purge[id] = {});
            v.element = o;
            o.$xid = v.$xid = id;
        }
        o = null;
        return v;
    },
    getId: function (node) {
        if (typeof node == 'string') node = document.getElementById(node);
        return node ? window === node ? "!window" : document === node ? "!document" : (node.$xid || '') : '';
    },
    getNode: function (xid) {
        return ood.use(xid).get(0);
    },
    getNodeData: function (node, path) {
        if (!node) return;
        return ood.get(ood.$cache.domPurgeData[typeof node == 'string' ? node : ood.getId(node)], path);
    },
    setData: function (path, value) {
        return ood.set(ood.$cache.data, path, value);
    },
    getData: function (path) {
        return ood.get(ood.$cache.data, path);
    },
    setNodeData: function (node, path, value) {
        if (!node) return;
        return ood.set(ood.$cache.domPurgeData[typeof node == 'string' ? node : ood.getId(node)], path, value);
    },
    $purgeChildren: function (node) {
        var cache = ood.$cache,
            proMap = cache.profileMap,
            ch = cache.UIKeyMapEvents,
            pdata = cache.domPurgeData,
            event = ood.Event,
            handler = event.$eventhandler,
            handler3 = event.$eventhandler3,
            // ie<=10
            children = (ood.browser.ie && node.all) ? node.all : node.getElementsByTagName('*'),
            l = children.length,
            bak = [],
            i, j, o, t, v, w, id;
        for (i = 0; i < l; i++) {
            if (!(v = children[i])) continue;
            if (t = v.$xid) {
                if (o = pdata[t]) {

                    //clear event handler
                    if (w = o.eHandlers) {
                        if (ood.browser.isTouch && w['onoodtouchdown'])
                            event._removeEventListener(v, "oodtouchdown", handler);
                        for (j in w) {
                            event._removeEventListener(v, j, handler);
                            event._removeEventListener(v, j, handler3);
                        }
                    }
                    for (j in o)
                        o[j] = null;

                    delete pdata[t];
                }

                //remove the only var in dom element
                if (ood.browser.ie)
                    v.removeAttribute('$xid');
                else
                    delete v.$xid;
            }

            if (id = v.id) {
                //clear dom cache
                //trigger object __gc
                if (id in proMap) {
                    o = proMap[id];
                    if (!o) continue;
                    t = o.renderId;
                    if ('!window' === t || '!document' === t) continue;

                    //don't trigger any innerHTML or removeChild in __gc()
                    o.__gc(true, true);
                    //clear the cache
                    bak[bak.length] = id;
                    //clear the cache shadow
                    if (o.$domId && o.$domId != o.domId)
                        bak[bak.length] = o.$domId;
                }
            }
        }
        //clear dom cache
        for (i = 0; i < bak.length;)
            delete proMap[bak[i++]];
        //clear dom content
        //1)while(node.firstChild)
        //   node.removeChild(node.firstChild);
        //2) node.innerHTML='';
        //3) the best one: remove first level by removeChild desc
        for (i = node.childNodes.length - 1; i >= 0; i--)
            node.removeChild(node.childNodes[i]);
    },

    //create:function(tag, properties, events, host){
    create: function (tag, bak) {
        var arr, o, t, r1 = /</;
        if (ood.isArr(tag)) {
            arr = [];
            for (var i = 0, l = tag.length; i < l; i++) Array.prototype.push.apply(arr, ood.create(tag[i]).get());
            return ood(arr);
        } else if (typeof tag == 'string') {
            //Any class inherited from ood.absBox
            if (t = ood.absBox.$type[tag]) {
                arr = [];
                //shift will crash in opera
                for (var i = 1, l = arguments.length; i < l; i++)
                    arr[i - 1] = arguments[i];
                o = new (ood.SC(t))(false);
                if (o._ini) o._ini.apply(o, arr);
            } else if (((t = ood.SC.get(tag)) && t["ood.Module"]) || bak == "ood.Module") {
                if (t) {
                    o = new t();
                    // use place holder to lazy bind
                } else {
                    o = new ood.UI.MoudluePlaceHolder();
                    ood.require(tag, function (module) {
                        if (module && module["ood.Module"]) {
                            var t = o.get(0);
                            if (t) {
                                if (t.renderId) {
                                    var m = new module();
                                    m.create(function () {
                                        o.replaceWithModule(m);
                                    });
                                } else {
                                    t._module = new module();
                                }
                            }
                        }
                    });
                }
                //from HTML element tagName
            } else if (/^[\w-]+$/.test(tag)) {
                o = document.createElement(tag);
                o.id = typeof id == 'string' ? id : ood.id();
                o = ood(o);
                //from HTML string
            } else {
                if (r1.test(tag))
                    o = ood.str.toDom(tag);
                if (!(o && o.n0))
                    o = ood.str.toDom("<ood>" + tag + "</ood>");
            }
            //Any class inherited from ood.absBox
        } else {
            if (tag['ood.Module']) {
                if ((t = ood.SC.get(tag.key)) && t["ood.Module"]) {
                    o = new t(tag);
                    // use place holder to lazy bind
                } else {
                    o = new ood.UI.MoudluePlaceHolder();
                    if (t = tag.events) o.setEvents(t);
                    if (t = tag.properties) o.setProperties(t);

                    if (tag.moduleClass && tag.moduleXid) {
                        o.get(0).moduleClass = tag.moduleClass;
                        o.get(0).moduleXid = tag.moduleXid;
                    }
                    ood.require(tag.key, function (module) {
                        if (module && module["ood.Module"]) {
                            var m = new module(tag);
                            m.create(function () {
                                o.replaceWithModule(m);
                            });
                        }
                    });
                }
            } else {
                o = new (ood.SC(tag.key))(tag);
            }
        }
        if (o['ood.absObj'] && (t = o.n0) && (t.host && t.host != t) && t.alias) o.setHost(t.host, t.alias);
        return o;
    },
    query: function () {
        return ood.doc.query.apply(ood.doc, arguments);
    },
    querySelector: function () {
        return ood.doc.querySelector.apply(ood.doc, arguments);
    },
    querySelectorAll: function () {
        return ood.doc.querySelectorAll.apply(ood.doc, arguments);
    },
    use: function (xid) {
        var c = ood._tempBox || (ood._tempBox = ood()), n = c._nodes;
        n[0] = xid;
        if (n.length != 1) n.length = 1;
        return c;
    }
});

/* ood.ini ood.browser dom ready
*/
new function () {
    var ini = ood.ini = {
        rootModuleName: '_ood_root',
        // 全局主题设置
        theme: null, // 初始为null，将在下面初始化
        // 主题相关路径
        path: 'ood/',
        themesPath: 'ood/appearance/',
        // 默认主题
        defaultTheme: 'light'
    };
    //special var
    if (window.ood_ini)
        ood.merge(ini, window.ood_ini, 'all');

    // 初始化全局主题设置
    (function () {
        // 尝试从本地存储恢复主题设置
        var savedTheme = localStorage.getItem('global-theme');

        // 如果本地存储没有主题设置，检查系统主题偏好
        if (!savedTheme && window.matchMedia) {
            var darkModeMedia = window.matchMedia('(prefers-color-scheme: dark)');
            savedTheme = darkModeMedia.matches ? 'dark' : 'light';
        }

        // 应用主题设置
        ood.ini.theme = savedTheme || ood.ini.defaultTheme;

        // 为document添加主题类
        document.documentElement.setAttribute('data-theme', ood.ini.theme);

        // 监听系统主题变化
        if (window.matchMedia) {
            window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function (e) {
                // 只有当用户没有明确设置主题时，才跟随系统主题变化
                if (!localStorage.getItem('global-theme')) {
                    var newTheme = e.matches ? 'dark' : 'light';
                    ood.setGlobalTheme(newTheme);
                }
            });
        }
    })();

    // 全局主题切换函数
    ood.setGlobalTheme = function (theme) {
        if (!theme || (theme !== 'light' && theme !== 'dark')) {
            theme = ood.ini.defaultTheme;
        }

        // 更新全局主题设置
        ood.ini.theme = theme;

        // 保存到本地存储
        localStorage.setItem('global-theme', theme);

        // 更新document的主题属性
        document.documentElement.setAttribute('data-theme', theme);

        // 触发全局主题变化事件
        ood.Event.fire('global.theme.change', {theme: theme});

        // 通知所有UI组件更新主题
        ood.UI.refreshAllThemes(theme);

        return ood.ini.theme;
    };

    // 获取当前全局主题
    ood.getGlobalTheme = function () {

        return ood.ini.theme;
    };

    // 切换暗黑模式
    ood.toggleDarkMode = function () {
        var currentTheme = ood.getGlobalTheme();
        var newTheme = currentTheme === 'light' ? 'dark' : 'light';
        ood.setGlobalTheme(newTheme);
        return newTheme;
    };

    //browser sniffer
    var w = window, u = navigator.userAgent.toLowerCase(), d = document, dm = d.documentMode, b = ood.browser = {
        kde: /webkit/.test(u),
        applewebkit: /applewebkit/.test(u),
        opr: /opera/.test(u),
        ie: (/msie/.test(u) && !/opera/.test(u)),
        newie: /trident\/.* rv:([0-9]{1,}[.0-9]{0,})/.test(u),
        gek: /mozilla/.test(u) && !/(compatible|webkit)/.test(u),

        isStrict: d.compatMode == "CSS1Compat",
        isWebKit: /webkit/.test(u),
        isFF: /firefox/.test(u),
        isChrome: /chrome/.test(u),
        isSafari: (!/chrome/.test(u)) && /safari/.test(u),

        isWin: /(windows|win32)/.test(u),
        isMac: /(macintosh|mac os x)/.test(u),
        isAir: /adobeair/.test(u),
        isLinux: /linux/.test(u),
        isSecure: location.href.toLowerCase().indexOf("https") == 0,
        // detect touch for browser
        isTouch: !!(navigator.userAgent.match(/AppleWebkit.*Mobile.*/)
            || (("ontouchend" in d) && !(/hp-tablet/).test(u))
            || (w.DocumentTouch && d instanceof DocumentTouch)
            || w.PointerEvent
            || w.MSPointerEvent),
        isIOS: /iphone|ipad|ipod/.test(u),
        isAndroid: /android/.test(u),
        isBB: /blackberry/.test(u) || /BB[\d]+;.+\sMobile\s/.test(navigator.userAgent)
    }, v = function (k, s) {
        s = u.split(s)[1].split('.');
        return k + (b.ver = parseFloat((s.length > 0 && isFinite(s[1])) ? (s[0] + '.' + s[1]) : s[0]))
    };
    // for new device
    if (w.matchMedia && typeof w.matchMedia == 'function') {
        // detect touch for device
        //  b.isTouch = w.matchMedia('(any-pointer: coarse)').matches;
        b.deviceType = b.isTouch
            ? (
                (w.matchMedia('(any-hover: hover)').matches || w.matchMedia('(any-pointer: fine)').matches)
                    ? 'hybrid'
                    : 'touchOnly'
            )
            : 'mouseOnly';
    } else {
        b.deviceType = b.isTouch ? 'touchOnly' : 'mouseOnly';
    }
    // fake touch
    if (ood.ini.fakeTouch && b.deviceType == 'mouseOnly') {
        b.fakeTouch = true;
    }
    ood.$secureUrl = b.isSecure && b.ie ? 'javascript:""' : 'about:blank';

    ood.filter(b, function (o) {
        return !!o
    });
    if (b.newie) {
        b["newie" + (b.ver = dm)] = true;
        b.cssTag1 = "-ms-";
        b.cssTag2 = "ms";
    } else if (b.ie) {
        // IE 8+
        if (ood.isNumb(dm))
            b["ie" + (b.ver = dm)] = true;
        else
            b[v('ie', 'msie ')] = true;
        if (b.ie6) {
            //ex funs for ie6
            try {
                document.execCommand('BackgroundImageCache', false, true)
            } catch (e) {
            }
            w.XMLHttpRequest = function () {
                return new ActiveXObject("Msxml2.XMLHTTP")
            };
        }
        if (b.ie6 || b.ie7) b.ie67 = 1;
        if (b.ie6 || b.ie7 || b.ie8) b.ie678 = 1;
        b.cssTag1 = "-ms-";
        b.cssTag2 = "ms";
    } else if (b.gek) {
        b[v('gek', /.+\//)] = true;
        b.cssTag1 = "-moz-";
        b.cssTag2 = "Moz";
    } else if (b.opr) {
        b[v('opr', 'opera/')] = true;
        b.cssTag1 = "-o-";
        b.cssTag2 = "O";
    } else if (b.kde) {
        b[v('kde', 'webkit/')] = true;
        if (b.isSafari) {
            if (/applewebkit\/4/.test(u))
                b["safari" + (b.ver = 2)] = true;
            else if (/version/.test(u))
                b[v('safari', 'version/')] = true;
        } else if (b.isChrome)
            b[v('chrome', 'chrome/')] = true;

        if (b.isWebKit) {
            b.cssTag1 = "-webkit-";
            b.cssTag2 = "Webkit";
        } else {
            b.cssTag1 = "-khtml-";
            b.cssTag2 = "Khtml";
        }
    }
    // BB 6/7 is AppleWebKit
    if (b.isBB && !b.ver) {
        // BB 4.2 to 5.0
        b.ver = parseFloat(ua.split("/")[1].substring(0, 3));
        b["bb" + b.ver] = true;
    }

    if (!ini.path) {
        var s, arr = document.getElementsByTagName('script'), reg = /js\/ood(-[\w]+)?\.js$/, l = arr.length;
        while (--l >= 0) {
            s = arr[l].src;
            if (s.match(reg)) {
                ini.path = s.replace(reg, '').replace(/\(/g, "%28").replace(/\)/g, "%29");
                break;
            }
        }
    }
    ood.merge(ini, {
        appPath: location.href.split('?')[0].replace(/[^\\\/]+$/, ''),
        dummy_tag: '$_dummy_$'
    }, 'without');
    if (!ini.path) ini.path = ini.appPath + 'ood/';
    if (!ini.basePath) ini.basePath = ini.path.replace(/ood\/$/, "").replace(/runtime\/$/, "");
    ini.releasePath = ini.appPath;
    if (ini.verPath) ini.releasePath += (ini.verPath ? (ini.verPath + "/") : "") + (ini.ver ? (ini.ver + "/") : "");

    var data = new Image();
    data.onload = data.onerror = function () {
        var path = ood.ini.path + "appearance/_oldbrowser/";
        if (this.width != 1 || this.height != 1) {
            document.documentElement.className += " ood-nodatauri";
            ood.merge(ood.ini, {
                img_dd: path + 'ondrag.gif',
                img_busy: path + 'busy.gif',
                img_pic: path + 'picture.png',
                img_handler: path + 'handler.gif',
                img_bg: path + 'bg.gif',
                img_blank: path + 'bg.gif',
                img_touchcur: path + 'touchcur.png'
            }, 'without');
        }
        data.onload = data.onerror = null;
    };
    data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
    ood.merge(ood.ini, {
        img_dd: "data:image/gif;base64,R0lGODlhEABAAPcAAAAAAAEBAQICAgMDAwUFBAUFBQcHBwgICAkJCQwMDA8PDx4eHiQkJDAwMD8/P0JCQl1dXf///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAP8ALAAAAAAQAEAAAAj/AP8JHPigQQMHAxMOTAAAgIABAgAkUPgPQsMABAoQCNAQAISEFw0cMMCxI4CFAAIY+Eey4b+LCgSmXCnzpECVBf49AEBgJEWVBwg8WCCgQMuEQEkuYDBgo8uBIlsyeCCgJMWWAB68fPpP5cCOAhm6TLpV4teQI0tyrTgzrcmPChNoVIuAYsKCB+3qtQsgQt+/fgPb/Ce4MOCvEfb6HbhYb1/Eigc35ptYZmW7kw1rRgy48+S9oEOLHk26tGnRnlMzTl0Yst7Mgyk+tnzWpO3JfSPo3q274eXcfjv21rxbeHDBwE32VphbeHPmvYEvBzmc9+eXxa3Hxm69OHPbtk2zNO68WvNh2q8lb6fuuix434Ola5/Nvbt53tLhH8/unXpw49vlhx90/E2HmH3XyTcgSO91FBAAOw==",
        img_busy: "data:image/gif;base64,R0lGODlhEAAQAOMAAAQCBHx+fLy+vOTm5ERCRMTGxISGhAQGBISChMTCxOzq7FRSVMzKzP7+/gAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh+QQJCQANACwAAAAAEAAQAAAESrDJSau9OOvNe1VFonCFIBRcIiQJp7BjNySDxRjMNAQBUk+FAwCAaiR6gURhsUgYhgCEZIDgDRYEwqIALTZmNay2UTB4KwKmwBIBACH5BAkJAA8ALAAAAAAQABAAgwQCBHx+fLy+vERCRKSmpOTi5BQWFNTW1KyurIyKjMTCxERGRPTy9BwaHLSytP7+/gRE8MlJq7046827n47RIJwBAEZ5phsikg9TMNZBHBMj7PR0DEDco7ATFA6JhA04IEh0AgUjEQgomcLY7EG1PmzZClJpiQAAIfkECQkADQAsAAAAABAAEAAABEewyUmrtcywWxn4BTcZH4CIUlGGaFMYbOsuSywuBLHIuC4LNEFrkBjIBoEAwmhRFBKKRDKQuBQEgsIAoWRWEoJEleitRKGWCAAh+QQJCQAPACwAAAAAEAAQAIMEAgR8fny8vrxEQkSkpqTk4uQUFhTU1tSsrqyMiozEwsRERkT08vQcGhy0srT+/v4ERfDJ6UxDM2sDgHkHcWgT5x1DOpKIhRDpQJAaqtK1iJNHkqy7RyIQSAQlw+IR5APiGAXGkiGoSoOFqqBwpAoU1yA0vJxEAAAh+QQJCQANACwAAAAAEAAQAAAES7DJWdYqMzdmWFsEsTRDMkwMoFbhMgQBcjaGCiCCJQhwkEgFG2YyQMRmjYJhmCkhNVBFoqCAQgu7nzWTECS0W4k0UQ2bz+i0en2OAAAh+QQJCQAPACwAAAAAEAAQAIMEAgR8fny8vrxEQkSkpqTk4uQUFhTU1tSsrqyMiozEwsRERkT08vQcGhy0srT+/v4ERfDJeVI6M79DcApB8jAFQy3DUIEJI7zmQ6RDZx3FKxTSQWMTl0AR23Q0o5LEYWggkEgDAGCAaqRUawbRfGq/4LB4TC5DIwAh+QQJCQANACwAAAAAEAAQAAAER7DJqUpSM7eRRkuCUGjSgAQIJyQJ+QXwxWLkAKeuxnm5VCyLVk+yIBAWQ6IRmRQABclJwcCIMg4AwGhoyAIQyYJ3O5ySo9EIACH5BAkJAA8ALAAAAAAQABAAgwQCBHx+fLy+vERCRKSmpOTi5BQWFNTW1KyurIyKjMTCxERGRPTy9BwaHLSytP7+/gRG8MlJ62SFWcuE19tUeEIRXp4Cng+2hkeSHKyUBEFSP3e+x7Od5ECg1Q6LwcB4IigHBETD4NgcngcDAGCAFR9a7g5hMCAsEQA7",
        img_pic: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAJOgAACToB8GSSSgAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAA92SURBVHiczZt7fFTlmce/ZzKT66S5cQsChUC4yKVsFaEUYlAWi4ClfqTq6tYVrfVSy1W01roudPWj4H6ga6ygu3SpZdeu0iKIgLFoIZREBAIGSDDhHkLIhMncJzNz3v3jPScZkklyzkzYT5/5vJ9J5pzznvf3O8/7vM/zvM9RhBAkIoqiWAALYNNaMpCkHY4AIaBV+1aFEGpCN+xlURIhQAOfBKQCGUAmkI4kQUEC9wEewAv4gfDfEgnWeC9UFEVBPvk04BtAXyAXSUKqdloQCf4q4ABaAK+iKKG/FRLiJgAJPgUJuL/W+gHpAyBTBdEon74PqR2p2v0UJAmtZkjQCL9GRKLzlzgJiHr6KUAWkJMPgyfB8BkwdDjkW4DTcGU31B6D+jNgsUJSWF6nAJ6eSFAURcFJAWl8Cz9ZOKliPcd5iSAgFEVRkTzETURcNkCb+8lIlc8fDeN/CjO/D7f2gb5WsGoGIOSCli1QthEOfgE1yKlwCWhCTo+Y00HxKzeQyjsofO+aAxHqqWIR32I30rCGgEjcJAghTDek4csAhoyHO7fCe5ehQYCI1a6C869w8LvwPHA3UAwUAnlILbJc07+fIgTNiG4+VfwKyEfaH1vHPow2S1ysSVFSIHk2DJoKk/tJGxBTsiFrLIx6FeYXwY0a8IFIDbIDNk2rUAJKEal8BOR0e/fRrOBFpml9pANJsexET5IIAaIAlHtgUhbc0NPJmWCfCDe+Bt+fBqOJRYJTmaGBtxsYuY0fsRgYgLRDKYDFLAnxrgIqEJ4MDIZh1nbHp1vJgIwbYczLoD4P7IOTbQcrmEo2mzECXpc+FCI1L4D0MVq1sRmWRDQgMhWUTKmqhg1QJmRMhLFr4AdFuia8zmxu4j3MgJed5ZJEDlID0mlfZg1LXARoFlf9IzS0QoUwyXomZIyD0f8Kdw1/jNn8jNewkGZ6IE04iJBKu/dpSBOjJW5HSAihblCUsApVQi5pA8xcnwEZzmLGX1jLzVjj1MRTnEc+xCTa/YvrrwG6PCaEbyNsjMABs9d+VgT3bSctmBbnGAIEeJrdyIArrH2rmJiOkCABABUQcMEqAYeNXrPnVpi7A7wZcd9WsJZtHMaBNH5eZNxhPr4w6Pjorm90U9A8yR2Qch5GhOFAV86Q3v58KyLD062L09NH5U22ASuAx4F5wASgD9IOWLoab0xs3QH3+xkaibAyHOajcJjyQIC3Gxq4H2l0UpA2xAIo68HWBLcI+Or/AfxzwJPAAmAKMBiw4/PdQzi8nnC4nFBoB17vy3z++RhkIKYbScUQAeEwjwqBK9Zht5udK1cyFumGpugdb4P0Bhgfhr2dwBf3Avg3+Aj4OfAUcC8wDRjOW28VEgp9GBNIJOKhpmY50vHKoIPb3BX4BT3NDL+fc88+yzRkHqBtDV4ASZdgrArHosGnexME/zp/BlYCi4D7gOnACDZtGkckUtvjPC4tXappSpY+VWIS4HKRJwSXjcQSXi8XnniCmcigxK6TsB/SHDAlDOW9An4VB4H1wEvAo8BtwCg2bhxvCLwQgkDAye23/z0wlPYASon19P/BTEDl8XDxkUf4HjIeaCNhPdiKt/Ngqp9IvNAVFfHtxTj7wi7gbeBfgPuBSaxZc5Nh8Hr77W//DbhFG2s6YOlk7UMhXjdDgGYT6hcuZPY1JPi5HUHcz15REXOfQPwSgj+Guhvgv4EXgAd46KE5tLaeNjdKIfjss13AbGAM0oW3dvIDhCCl4289id1O/rp1/MfChUwAsjjGHVj5EEG62b4AFAFznoLv/gb6Q/IUyF8KE8dCDsXFfSkp2YDNNjSOrlOR6m9HWxU6usKKz8eRrCzzPesk1Exl3b5s/pkAaaSirQ/G+4kGn4lc3AdCagZ8c1BGxm0PvP/+4HBGRp75EQJHjtQjV60UtNWgkwYcPkypquKJp3+7nfxt97JsSCM+WpBBagTDzmkX4BkADIW0GV7viIfWrPEY7zFKgsEgGzee6vR7RxsApNTVscysHYhuzS6uDDlIMxcQuBGEEKjG5vwrIN4A8T8g/gKiBoQDRABECMQVcP9oxYpzCKGaGtWvf/1HYAnSiN6i8ZoSiwAbkFNfz5ZESHC00DSkAqcREoyAj2gtAKIWPD945pkLhknYt+8Q8CzwE2AuMBZpBG2dzkXO2vTsbL555gzbEyHhylWah5TT0h0JRsGrWguBaAFRCd55y5df6pGEsrJDWK3PA08jvcepSIcoHUiKRYCuBdmZmYyqrWXHdSRBvW0RLiPgda8yAiIIohnEIfDOWb68vksSysoOa+AXAQ/Qno3OpitHSCNB3/ToY7dz49dfs/M6kKAm/Ts7RsO6lVBrBHw0CQHtvEPgmRNLE/bu/Qqr9UVgMfAgcDsyBdeHqFR8l6PWSEgF+trtjO1lElTeYCvwnAWevgWW7oQ6I+CF9ns0CV+Cb240CXv21GC1rkFGjf8EzESm4/tqmNqCoS53hqK2v2xApt1O/8pKXi8oYFb3603X0uTk6k3VJJ3bSynPsB+ZzHDkgWMp5D4MP8+BiTbaA/quRGgthMyGnAHfi8uXt5TOmhUMzp69i0jEC9QDtfIwl5Cbs/o2fdtTjn0DeYKqXeD2eLg8+k/8/vOzVMVLQJ9scg6PJPSdLRxBbos5gAYHnF29cmX1iOZmb/WoUcf0/FZ3LoSe/LMhY9yhkL5qzZqsV2fN8g2JRNKQGaIQ0hvxaf9HosHrQLtt2n2SqGEml/FYqojsqeN4ItPh6lUai4tZBMwCxrJ69TgikWqEECk+X8P+wsJTzSC8mtXvaip0nA7NII6C9zdQWQjrkEmTecB4pPp33oYzMmLc3EoYN34EjQhLFZHSU9QkQkJLCw3z53M/jz46ndbWU9FHrR5P45ZRo47Vg3CDaDVIQhCEC0QdeLbCl38no8cFQBFQgIwDrERlhXoGLzcq3agIwog2Er5C3X2S2kRIONgw6ARe75lYR60uV9N7I0acO6+BMkpCSCPCAe49cGCKdIDmAZOQnnU6PWWE2g6GmK4tXEJLTXQm4QR18YCvduW3pLQ0NnZ3ltXpbN40cmS1URJ0IvTWAq59sL8IltG+DGYDth4J6AS+Iwm++Ek42ZLvSm6+5DBydpLL1fT7wsJaMyRENze4y2DvZHgYmT3ur9kCpUsC8DAAgaPL0CUBEk625LuSHfXNZvTF5nQ2by4sPK2T0JNhjFGfcOV92PRtmUobiKxriu0IAQoqW3rM2cQg4ZMeSIgHfLQmbBo58sR5zTCaJaERau6UgdAgjYDYGoCbvj2C75qESNkRjvU2eL31+fjj0r9C3SUQPs3oGSUgCIG9sGqcDITalsPOjpCNm7r1Zjp6I3q1kB3U3WwtnsTblZVURJ9W7cp3Twh/GW7Nze++6qM7KS+v8N111x9Ww6uX4ZBeCGA0M5IESf0hq0j+23bpNQQoiqKgkm1qYDoJ/8V2lrI/FCI4ZQolx49TBlDjzndNiBwKJQS+rOww06Z94AuF1K1wvgReCMFxM2khBUKZQKHcSA3rv3fWgEYqTQ/wbT7iCcqQvr0zEODS5Mm8uK1q2M4JHA205gzINd2nPvD9ZUcoLv6AcDgI+CLgKYW6Y/AzAUdMdBWKwL4T0CI0gVgEzKSOCLWGu32HHfyEfYCb9hK4i56HnvLfVVA1OpjZp5+JQV4jeeU7zj6/s2jQ4PxwCjKQcQCO0+BYCxUn4F4VvjDQlVBlUHR+vRC+a490XAHAxiFmIwxsaLzDDmTI+VNktqUIGMnq1eMIhWoSMXh55TvO/OplS2TtWsRbb3F54kQeQ26EDkGmtZNeAqsXJgm5IRvqwgCqETjph0diLvkxlsEkIJOj/JIwgZjAAwRYwwdIN/Mp5F5dEVBISckYPbDpDfC/+x2itBRRUUH9vHnMR25tZaFldE5BihOGqfC+KmsVVc0jjKgQUOFkCJa9BNZYt+uUD4iqAs1mCZN5jEX0YwzZ9OUKTZziPMsppZwmZCjuABqABkpKrDz++J+wWEYaU/LOklfx8dkln84dbE9XLXl5kJ8vW9++ANQvXszCzZupApxoiXchhLisKBm5cE8STFJk4OMX8JkKR6xC7O3qfrEIUDQtSEPW8A0A+pNEjlaQlISMq/3aIK4AjZSUJCcM/uDOs0s+mRMTfGYm2Gzg83HhySdZ+O67HIf23QchhEBRrC2QaYPUdAhfhdYcIVq6u2fMjJBGghUZOWXBNaVoOgFebQBOXnnFyooVH19v8BYLKAq43Vz44Q/5x507OYV8CEHirBfuLiWmV1+lILUhTfvbgiRAfxkiQCj0n1itC+IBDpB3cNe5JZ/cOcierlpyc2HgwK7B61JXx1+GD2cRcBn5IIJ0zPYYkO5SYirSYfBrN2hCLnH1yDnfBLjweot7E3z0k7fbY4MHKCigqKSEecgsbwZxFElCD1ViGpt6XlAvR/Vp3wEgRHLyDLM3bZMDByoXbLzTEQ1+4MB28MnJscHrMmEC30Gmutpqhc0OoccLRLuoQohIVJMlaYoy3uxNASgvr2T69P99Z4O62e3mC7PgAQYMYCjSPn0DLd1tdhiJ1gkqqGqz6avKyyuZNu0PhMPecBjHqlWsDoX43Ax4gJYWAkj1z6C9WMuUJFwoSSh0zNT5UeCBZqC+tZVz993Hc01NfGoUPEB1NQ20v65nshJBSuIE7Nu3GVV1Gjq3vPxoR/DAReCiy8WFqVNZdvYsnxoB7/fjf+01vkSGtaZLZHVJnIA77rhEdfULPZ5XXn6UoqJo8PqKcgW5ljudThpuvpklp0/zaU/drVvH9spKmpHGWN8EuT6lsl012jdnctm162n8/s6JzmAwyJtvfojF8hxyi/pB2vfq+iH9C6vW0oB+ycmM3bWLdaEQ/o63dblw/eIXvAs8A/wYmIPc788lKttrtCX05ii0OUypQC7Tp4/i4YdnM2zYOBQlnaNH69m06WsOHtSLmp1AI+1+hJtr3/LQ45BMoM/8+Yy7+25mFBQwyuuFQ4eo37CB6tOncdO+tXYRqUnNQECYfCGzNwjQ3eY05JLUD+mc6GW0oO0vaoNs0r69aOB17y1qQzYZadlztb70N1JtWn9BwKX11Yh8M1V/LdcUoETeHAWkn6Aoih4cgfQePciYPZoAD5IEN9KZCtPBddX6UmnXighyjrdo/UUT4EGS4Nbu3buxgOmO2qPIZK3p1eQKEmyrNvBWtI3frgas9RXdX4r2bUVa+3BUX60k8OJkrxEAnQau1+mDfJptzehgo6ZEdIvur1sijcj/AcgCrHU5y1W7AAAAAElFTkSuQmCC",
        img_handler: "data:image/gif;base64,R0lGODlhBAAEAPcAAAAAAGSMtP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAP8ALAAAAAAEAAQAAAgPAP8JFCDwX4B/BAUe/BcQADs=",
        img_touchcur: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAATCAMAAACqTK3AAAAAnFBMVEVKS0sAAAD6+vr39/fj4+OsrKxQUVHk5OTY2NjV1dVgYWHm5ube3t7c3NzV1dXT09PPz8/q6ur19fXNzc2/v7/v7+/u7u6QkJB1dnbt7e1SU1Pd3d3b29vW1tbU1NTT09Ps7Ozl5eXe39/e3t709PS7u7u5ublmZ2fp6enn5+fj4+Ph4eHg4ODc3Nzc3NzZ2dnZ2dnW1tbU1NTT09Pxrtv7AAAANHRSTlPmAP78+fHnyPf26NGiizAcBfr49fPu7e3q6Oedh08oDPr5+Pj38vLp3NfBuLGUkXRrSzgYlzzxjwAAAKFJREFUGNNl0NUOwzAMQFE7sDLzSmPm/f+/LY2atVXv45FlyQac1ZPnaCfLdgdqr4TF/KATq+nJS1gOXWZE35L8wlhAHw+bjuxAiCqyOgozGDKJC1hTGKeXgM52QnsN0DYmxM9iik0oFlMuWY6JVYCY8JHk5CvoSc2/LNY3edAlOCox0laSr1Eu92WrwlPPeaWE6Ru6u/soSfapyketXjjrB1sXCDrwyo6RAAAAAElFTkSuQmCC',
        // transparent 1*1 gif and png
        img_bg: data.src,
        img_blank: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVQYV2NgYAAAAAMAAWgmWQ0AAAAASUVORK5CYII="
    }, 'without');

    //for dom ready
    var f = ood._domReadyFuns = function () {
        if (!ood.isDomReady) {
            if (d.addEventListener) {
                d.removeEventListener("DOMContentLoaded", f, false);
                w.removeEventListener("load", f, false);
            } else if (d.detachEvent) {
                d.detachEvent("onreadystatechange", f);
                w.detachEvent("onload", f);
            }

            // adjust touchonly again
            if (ood.browser.deviceType != 'touchOnly' && !ood.Dom.getScrollBarSize()) {
                // in Mac, the element barsize is 0 without mouse device plugged
                // and if you plug the mouse, barsize will be 15

                //ood.browser.deviceType = 'touchOnly';
                if (ood.UI) {
                    var f2 = function (c) {
                        ood.arr.each(c, function (key) {
                            if (key = ood.SC.get(key)) {
                                if (key.$DataModel.overflow) {
                                    key.$DataModel.overflow.ini = 'auto';
                                    key.$DataStruct.overflow = 'auto';
                                }
                                if (key.$children && key.$children.length) f2(key.$children);
                            }
                        });
                    };
                    f2(ood.UI.$children);
                }
            }
        }

        try {
            if (ood.ini.customStyle && !ood.isEmpty(ood.ini.customStyle)) {
                var arr = [], style = ood.ini.customStyle, txt;
                ood.each(style, function (v, k) {
                    arr.push(k + " : " + v + ";")
                });
                txt = ".ood-custom{\r\n" + arr.join("\r\n") + "\r\n}";
                ood.CSS.addStyleSheet(txt, "ood:css:custom", 1);
            }
            ;
            for (var i = 0, l = ood._m.length; i < l; i++)
                ood.tryF(ood._m[i])
            ood._m.length = 0;
            ood.isDomReady = true;
        } catch (e) {
            ood.asyRun(function () {
                throw e
            })
        }
    };

    if (d.addEventListener) {
        d.addEventListener("DOMContentLoaded", f, false);
        w.addEventListener("load", f, false);
    }
    //IE<=10
    else {
        d.attachEvent("onreadystatechange", f);
        w.attachEvent("onload", f);

        var ff = function () {
            if (ood.isDomReady) return;
            try {
                //for ie7 iframe(doScroll is always ok)
                d.activeElement.id;
                d.documentElement.doScroll('left');
                f()
            } catch (e) {
                ood.setTimeout(ff, 9)
            }
        };
        ff();
    }

    // to ensure
    var fe = function () {
        ((!ood.isDomReady) && ((!d.readyState) || /in/.test(d.readyState))) ? ood.setTimeout(fe, 9) : f()
    };
    fe();
};

// for loction url info
new function () {
    ood._uriReg = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/;
    ood._localReg = /^(?:about|app|app\-storage|.+\-extension|file|widget):$/;
    ood._curHref = (function (a) {
        try {
            return location.href;
        } catch (e) {
            a = document.createElement("a");
            a.href = "";
            return a.href;
        }
    })(),
        ood._localParts = ood._uriReg.exec(ood._curHref.toLowerCase()) || [];
};

new function () {
    ood.pseudocode = {
        getScope: function (eventArgs, module, temp) {
            var parentModule;
            try {
                parentModule = module.getParentModule();
            } catch (e) {

            }

            return {
                temp: temp,
                page: module,
                parentModule: parentModule,
                args: eventArgs,
                functions: ood.$cache.functions,
                'constant': ood.constant,
                'global': ood.$cache.data,
                // special functions
                getCookies: ood.Cookies.get,
                getFI: function (key) {
                    var h = ood.getUrlParams();
                    return h && h[key]
                }
            };
        },
        exec: function (_ns, conf, resumeFun, level) {
            var ns = this, t, tt, m, n, p, k, arr, type = conf.type || "other",
                comparevars = function (x, y, s) {
                    switch (ood.str.trim(s)) {
                        case '=':
                        case 'is':
                            return x === y;
                        case '<>':
                        case 'is-not':
                        case '!=':
                            return x !== y;
                        case 'exists':
                        case 'defined':
                            return ood.isDefined(x);
                        case 'not-exists':
                        case 'undefined':
                            return !ood.isDefined(x);
                        case 'empty':
                            return ood.isEmpty(x);
                        case 'non-empty':
                            return !ood.isEmpty(x);
                        case '>':
                            return parseFloat(x) > parseFloat(y);
                        case '<':
                            return parseFloat(x) < parseFloat(y);
                        case '>=':
                            return parseFloat(x) >= parseFloat(y);
                        case '<=':
                            return parseFloat(x) <= parseFloat(y);
                        case 'include':
                            return (x + "").indexOf(y + "") != -1;
                        case 'exclude':
                            return (x + "").indexOf(y + "") == -1;
                        case 'begin':
                            return (x + "").indexOf(y + "") === 0;
                        case 'end':
                            return (x + "").indexOf(y + "") === (x + "").length - (y + "").length;
                        case "objhaskey":
                            return typeof(x) == "object" ? (y in x) : false;
                        case "objnokey":
                            return typeof(x) == "object" ? !(y in x) : false;
                        case "arrhasvalue":
                            return ood.isArr(x) ? ood.arr.indexOf(x, y) !== -1 : false;
                        case "arrnovalue":
                            return ood.isArr(x) ? ood.arr.indexOf(x, y) == -1 : false;
                        case "objarrhaskey":
                            return ood.isArr(x) ? ood.arr.subIndexOf(x, 'id', y) !== -1 : false;
                        case "objarrnokey":
                            return ood.isArr(x) ? ood.arr.subIndexOf(x, 'id', y) == -1 : false;
                        default:
                            return false;
                    }
                },
                adjustparam = function (o) {
                    if (typeof(o) == "string") {
                        var jsondata, oo;
                        if (ood.str.startWith(o, "[data]")) {
                            o = o.replace("[data]", "");
                            jsondata = 1;
                        }
                        o = ood.adjustVar(oo = o, _ns);
                        if (!ood.isDefined(o)) o = ood.adjustVar(oo);
                        // for file
                        if (jsondata && typeof(o) == "string")
                            o = ood.unserialize(ood.getFileSync(o));
                    } else if (ood.isHash(o)) {
                        // one layer
                        for (var i in o) o[i] = adjustparam(o[i]);
                    } else if (ood.isArr(o)) {
                        // one layer
                        for (var i = 0, l = o.length; i < l; i++) o[i] = adjustparam(o[i]);
                    }
                    return o;
                },
                redirection = conf.redirection,
                target = conf.target,
                method = conf.method,
                // className=conf.className,
                // conf.args > conf.params
                iparams = ood.clone(conf.args || conf.params) || [],
                conditions = conf.conditions || [],
                adjust = adjustparam(conf.adjust) || null,
                iconditions = [], t1, t2,
                timeout = ood.isSet(conf.timeout) ? parseInt(conf.timeout, 10) : null;

            var _debug = '"' + conf.desc + '"',
                _var = {type: type, target: target, method: method, args: iparams, pseudo: conf};

            // cover with inline params
            if (method.indexOf("-") != -1) {
                t = method.split("-");
                method = t[0];
                for (var i = 1, l = t.length; i < l; i++)
                    if (t[i]) iparams[i - 1] = t[i];
            }
            // handle conditions
            // currently, support and only
            // TODO: complex conditions
            for (var i = 0, l = conditions.length, con; i < l; i++) {
                con = conditions[i];
                if (!comparevars(
                    !ood.isDefined(t1 = ood.adjustVar(con.left, _ns)) ? ood.adjustVar(con.left) : t1,
                    !ood.isDefined(t2 = ood.adjustVar(con.right, _ns)) ? ood.adjustVar(con.right) : t2,
                    con.symbol)) {
                    if (typeof resumeFun == "function") {
                        ood._debugInfo.apply(ood, ["pseudo", ood.str.repeat('  ', level || 1), "//", _debug, _var]);
                        return resumeFun();
                    }
                    ood._debugInfo.apply(ood, ["pseudo", ood.str.repeat('  ', level || 1), "//", _debug, _var]);
                    return;
                }
            }
            if (redirection && (arr = redirection.split(":")) && ood.isArr(arr)) {
                if (arr[0]) type = arr[0];
                if (arr[1]) target = arr[1];
                if (arr[2]) method = arr[2];
            }
            if (target && method && target != "none" && method != "none") {
                //adjust params
                for (var i = redirection ? 3 : (type == "other" && target == "callback") ? method == "call" ? 1 : method == "set" ? 2 : 0 : 0, l = iparams.length; i < l; i++)
                    iparams[i] = adjustparam(iparams[i]);

                if (redirection && !(type == "other" && target == "callback" && method == "call")) {
                    iparams = iparams.slice(3);
                }

                var fun = function () {
                    ood._debugInfo.apply(ood, ["pseudo", ood.str.repeat('  ', level || 1), _debug, _var]);
                    if (false === ood.tryF(ns._Handlers, [type, method, iparams, adjust, target, conf])) return;
                    switch (type) {
                        case 'page':
                            // handle switch
                            if (method == "switch") {
                                if (!ood.History._callbackTag) {
                                    ood.History._callbackTag = function (fi, init) {
                                        if (init) return;
                                        var ar = ood.urlDecode(fi || "");
                                        if (!ar.cls) {
                                            ar.cls = "App";
                                            ar.cache = true;
                                        }
                                        // get root only
                                        ood('body').children().each(function (xid) {
                                            var module = ood.Module.getFromDom(xid);
                                            if (module && module._showed) {
                                                if (ar.cache) module.hide(); else module.destroy();
                                            }
                                        });
                                        ood.showModule(ar.cls);
                                        return false;
                                    };
                                }
                                var hash = {
                                    cls: target,
                                    cache: !!iparams[0]
                                };
                                if (iparams[1] && !ood.isEmpty(iparams[1])) {
                                    hash = ood.merge(hash, iparams[1]);
                                }
                                ood.History.setFI(hash, true);
                                return;
                            } else if (method == "open") {
                                var hash = {
                                    cls: target
                                };
                                if (iparams[0] && !ood.isEmpty(iparams[0])) {
                                    hash = ood.merge(hash, iparams[0]);
                                }
                                window.open('#!' + ood.urlEncode(hash));
                                return;
                            }
                            // try to get module
                            var cls = ood.get(window, target.split(".")), ins;
                            // TODO: now, only valid for the first one
                            if (cls) for (var i in cls._cache) {
                                ins = cls._cache[i];
                                break;
                            }

                            if (method == "destroy") {
                                if (ins) if (ood.isFun(t = ood.get(ins, [method]))) t.apply(ins, iparams);
                                return;
                            } else if (method == "show") {
                                // special for ood.Module.show
                                iparams.unshift(function (err, module) {
                                    if (err) {
                                        ood.message(err);
                                    }
                                });
                            }
                            if (ins) {
                                if (ood.isFun(t = ood.get(ins, [method]))) t.apply(ins, iparams);
                            }
                            // make sure call getModule once
                            else {
                                var t1 = _ns.temp._module_funs_ = _ns.temp._module_funs_ || {},
                                    t2 = t1[target] = t1[target] || [];
                                // collect funs
                                t2.push(function (ins, t) {
                                    if (ood.isFun(t = ood.get(ins, [method]))) t.apply(ins, iparams);
                                });
                                // Calling asyn call, but only for the first time
                                if (t2.length === 1) {
                                    ood.getModule(target, function (err, ins) {
                                        if (err) return;
                                        if (ins)
                                            for (var i = 0, l = t2.length; i < l; i++)
                                                t2[i].call(null, ins);
                                        t2.length = 0;
                                        t1 = t2 = null;
                                        delete _ns.temp._module_funs_[target];
                                    });
                                }
                            }
                            break;
                        case 'control':
                        case 'module':
                            if (method == "disable" || method == "enable") {
                                if (ood.isFun(t = ood.get(_ns.page, [target, "setDisabled"]))) t.apply(_ns.page[target], [method == "disable", true]);
                            } else {
                                if (method == "setProperties") {
                                    // [0] is native var, [1] is expression var
                                    var params = ood.merge(iparams[0], iparams[1], 'all');
                                    iparams[1] = null;
                                    if (m = params) {
                                        if (m.CA) {
                                            if (ood.isFun(t = ood.get(_ns.page, [target, "setCustomAttr"]))) t.apply(_ns.page[target], [m.CA]);
                                            delete m.CA;
                                        }
                                        if (m.CC) {
                                            if (ood.isFun(t = ood.get(_ns.page, [target, "setCustomClass"]))) t.apply(_ns.page[target], [m.CC]);
                                            delete m.CC;
                                        }
                                        if (m.CS) {
                                            if (ood.isFun(t = ood.get(_ns.page, [target, "setCustomStyle"]))) t.apply(_ns.page[target], [m.CS]);
                                            delete m.CS;
                                        }
                                    }
                                }
                                if (ood.isFun(t = ood.get(_ns.page, [target, method]))) t.apply(_ns.page[target], iparams);
                            }
                            break;
                        case 'otherModuleCall':
                            var moduleTarget = target, com;
                            if (target && (arr = target.split("##")) && ood.isArr(arr)) {
                                if (arr[0]) moduleTarget = arr[0];
                                if (arr[1]) com = arr[1];
                            }
                            var otherModule = ood.getModule(moduleTarget) || ood.newModule(moduleTarget);
                            if (com) {
                                if (ood.isFun(t = ood.get(otherModule, [com, method]))) t.apply(otherModule[com], iparams);
                            } else {
                                if (ood.isFun(t = ood.get(otherModule, [method]))) t.apply(otherModule, iparams);
                            }

                            break;
                        case 'other':
                            switch (target) {
                                case 'url':
                                    switch (method) {
                                        case "close":
                                            window.close();
                                            break;
                                        case "open":
                                            ood.Dom.submit(iparams[0], iparams[1], iparams[2], iparams[3]);
                                            break;
                                        case "mailTo":
                                            ood.mailTo.apply(ood, iparams);
                                            break;
                                        case "selectFile":
                                            ood.Dom.selectFile.apply(ood.Dom, iparams);
                                            break;
                                        case "readText":
                                            ood.getFileAsync.apply(ood, iparams);
                                            break;
                                        case "readJSON":
                                            iparams[4] = {rspType: 'json'};
                                            ood.getFileAsync.apply(ood, iparams);
                                            break;
                                    }
                                    break;
                                case 'msg':
                                    if (method == "busy" || method == "free") {
                                        if (ood.isFun(t = ood.get(ood.Dom, [method]))) t.apply(ood.Dom, iparams);
                                    } else if (method == "console" && ood.isDefined(window.console) && (typeof console.log == "function")) {
                                        console.log.apply(console, iparams);
                                    } else if (_ns.args[0].key == 'ood.APICaller') {
                                        var ajax = _ns.args[0].boxing();
                                        var onOk = function () {
                                            ajax.confirm = true;
                                            ajax.invoke();
                                            ajax.confirm = false;
                                        };
                                        switch (method) {
                                            case "confirm":
                                                iparams[2] = onOk;
                                                break
                                            case  "alert":
                                                iparams[3] = onOk;
                                                break
                                            case  "prompt":
                                                iparams[3] = onOk;
                                                break;
                                            case  "pop":
                                                iparams[2] = onOk;
                                                break;
                                            default:
                                                break;
                                        }

                                        if (ood.isFun(t = ood.get(ood, [method]))){
                                            t.apply(ood, iparams)
                                        }

                                    } else if (ood.isFun(t = ood.get(ood, [method]))) {
                                        t.apply(ood, iparams)
                                    }
                                    ;
                                    break;
                                case "var":
                                    if (iparams[0].length) {
                                        var v = iparams[1];
                                        if (iparams[2])
                                            v = ood.get(v, iparams[2].split(/\s*\.\s*/));
                                        if (adjust) {
                                            switch (adjust) {
                                                case "serialize":
                                                    v = ood.serialize(v);
                                                    break;
                                                case "unserialize":
                                                    v = ood.unserialize(v);
                                                    break;
                                                case "stringify":
                                                    v = ood.stringify(v);
                                                    break;
                                                default:
                                                    if (typeof(adjust = ood.get(adjust)) == "function")
                                                        v = adjust(v);
                                                    break;
                                            }
                                        }
                                        ood.set(_ns, (method + "." + ood.str.trim(iparams[0])).split(/\s*\.\s*/), v);
                                    }
                                    break;
                                case "callback":
                                    switch (method) {
                                        case "setCookies":
                                            if (iparams[0] && !ood.isEmpty(iparams[0])) ood.Cookies.set(iparams[0]);
                                            break;
                                        case "setFI":
                                            if (iparams[0] && !ood.isEmpty(iparams[0])) ood.History.setFI(iparams[0], true, true);
                                            break;
                                        case "set":
                                            t = iparams[1];
                                            if (ood.isStr(t) && /[\w\.\s*]+[^\.]\s*(\()?\s*(\))?\s*\}$/.test(t)) {
                                                // args[0] => args.0
                                                t = t.replace(/\[(\d+)\]/, ".$1");
                                                t = t.split(/\s*\.\s*/);
                                                if (t.length > 1) {
                                                    m = t.pop().replace(/[()}\s]/g, '');
                                                } else {
                                                    m = t[0].replace(/[{()}\s]/g, '');
                                                    t = ["{window"];
                                                }
                                                t = t.join(".") + "}";
                                                t = ood.adjustVar(tt = t, _ns);
                                                if (!ood.isDefined(t)) t = ood.adjustVar(tt);
                                                if (t && ood.isFun(t[m]))
                                                    ood.$cache.callback[iparams[0]] = [t, m];
                                            }
                                            break;
                                        case "call":
                                            var args = iparams.slice(3), doit, doit2, y;
                                            t = iparams[0];
                                            if (ood.isStr(t) && /[\w\.\s*]+[^\.]\s*(\()?\s*(\))?\s*\}$/.test(t)) {
                                                // args[0] => args.0
                                                t = t.replace(/\[(\d+)\]/, ".$1");
                                                t = t.split(/\s*\.\s*/);
                                                if (t.length > 1) {
                                                    m = t.pop().replace(/[()}\s]/g, '');
                                                } else {
                                                    m = t[0].replace(/[{()}\s]/g, '');
                                                    t = ["{window"];
                                                }
                                                t = t.join(".") + "}";
                                                t = ood.adjustVar(tt = t, _ns);
                                                if (!ood.isDefined(t)) t = ood.adjustVar(tt);
                                                if (t && t[m]) {
                                                    // it's function
                                                    if (ood.isFun(t[m])) {
                                                        doit = 1;
                                                    }
                                                    // it's pseudo actions or function
                                                    else if ((t[m].actions && ood.isArr(t[m].actions) && t[m].actions.length) || t[m]['return']) {
                                                        t = t[m];
                                                        doit2 = 1;
                                                        if (args && args.length && t.params && t.params.length)
                                                            for (var i = 0, l = args.length; i < l; i++)
                                                                if (y = t.params[i] && t.params[i].type)
                                                                    args[i] = y == 'String' ? (args[i] + '') : y == 'Number' ? (parseFloat(args[i]) || 0) : y == 'Boolean' ? (!!args[i]) : args[i];
                                                    }
                                                }
                                            } else if (ood.isStr(t = iparams[0]) && ood.isFun((n = ood.$cache.callback[t]) && (t = n[0]) && t && (t[m = n[1]]))) {
                                                doit = 1;
                                            }
                                            if (doit) {
                                                t = t[m].apply(t, args);
                                            } else if (doit2) {
                                                // nested call
                                                // arguemsnt of function/event is modified
                                                t = ns._callFunctions(t, args, _ns.page, _ns.temp, null, 'nested' + (t.desc || t.id || ""), (level || 1) + 1);
                                            }
                                            if (doit || doit2) {
                                                if (iparams[1] && iparams[2] && ood.get(_ns, iparams[1].split(/\s*\.\s*/))) ood.set(_ns, (iparams[1] + "." + iparams[2]).split(/\s*\.\s*/), t);
                                            }
                                            break;
                                    }
                                    break;
                                default :
                                    try {
                                        var otherModule = ood.getModule(target);
                                        if (otherModule) {
                                            if (ood.isFun(t = ood.get(otherModule, [method]))) t.apply(otherModule, iparams);
                                        }
                                    } catch (e) {
                                        console.log(e)
                                    }
                                    break;
                            }
                            break;

                    }
                };
                // asy
                if (timeout !== null) ood.asyRun(fun, timeout);
                else fun();
            }
            return conf["return"];
        },

        _callFunctions: function (pseudo, args, module, temp, holder, fromtag, level) {
            temp = temp || {};
            var ns = this, fun, resume = 0, t, rtn,
                funs = pseudo.actions || pseudo || [],
                rtn = pseudo['return'], funsrtn,
                innerE = funs.length == 1 && (typeof(funs[0]) == 'function' || typeof(funs[0]) == 'string'),
                _ns = ns.getScope(args, module, temp),
                recursive = function (data) {
                    var irtn;
                    // set prompt's global var
                    if (ood.isStr(this)) _ns.temp[this + ""] = data || "";
                    //callback from [resume]
                    for (var j = resume, l = funs.length; j < l; j++) {
                        resume = j + 1;
                        fun = funs[j];

                        if (module && typeof fun == 'string') fun = module[fun];
                        if (holder && typeof fun == 'string') fun = holder[fun];
                        if (module && ood.isHash(fun) && typeof (fun.script) == "string") fun = module[fun.script];
                        if (holder && ood.isHash(fun) && typeof (fun.script) == "string") fun = holder[fun.script];
                        if (typeof fun == 'function') {
                            // only function action can affect return
                            if (false === (irtn = ood.tryF(fun, _ns.args, _ns.page))) {
                                resume = j;
                                break;
                            }
                        } else if (ood.isHash(fun)) {
                            if ('onOK' in fun || 'onKO' in fun) {
                                var resumeFun = function (key, args, flag) {
                                    if (recursive) {
                                        if (ood.isStr(flag)) _ns.temp[flag] = true;
                                        return recursive.apply(key, args || []);
                                    }
                                };
                                // onOK
                                if ('onOK' in fun) (fun.args || fun.params || (fun.args = []))[parseInt(fun.onOK, 10) || 0] = function () {
                                    if (resumeFun) resumeFun("okData", arguments, fun.okFlag);
                                };
                                if ('onKO' in fun) (fun.args || fun.params || (fun.args = []))[parseInt(fun.onKO, 10) || 0] = function () {
                                    if (resumeFun) resumeFun("koData", arguments, fun.koFlag);
                                };
                                ns.exec(_ns, fun, resumeFun, level);
                                break;
                            } else if (false === (ns.exec(_ns, fun, null, level))) {
                                resume = j;
                                break;
                            }
                        }
                    }
                    if (resume == j) resume = recursive = null;
                    return irtn;
                };
            if (!innerE) {
                ood._debugGroup("pseudo", ood.str.repeat('  ', (level || 1) - 1), '"' + fromtag + '"', {pseudo: pseudo}, {scope: _ns});
                ood._debugInfo("pseudo", ood.str.repeat('  ', (level || 1) - 1), "{");
            }
            funsrtn = recursive();
            if (!innerE) {
                ood._debugInfo("pseudo", ood.str.repeat('  ', (level || 1) - 1), "}");
                ood._debugGroupEnd("pseudo", ood.str.repeat('  ', (level || 1) - 1));
            }
            if (rtn) {
                rtn = ood.adjustVar(t = rtn, _ns);
                if (!ood.isDefined(rtn)) rtn = ood.adjustVar(t);
            } else {
                // for system beforeXXX events
                rtn = funsrtn;
            }
            return rtn;
        }/*,
        toCode:function(conf, args, module,temp){
        }*/
    };
};

/*serialize/unserialize
*/
new function () {
    var M = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '\"': '\\"',
            '\\': '\\\\',
            '/': '\\/',
            '\x0B': '\\u000b'
        },
        H = {'@window': 'window', '@this': 'this'},
        // A1/A2 for avoiding IE's lastIndex problem
        A1 = /\uffff/.test('\uffff') ? /[\\\"\x00-\x1f\x7f-\uffff]/ : /[\\\"\x00-\x1f\x7f-\xff]/,
        A2 = /\uffff/.test('\uffff') ? /[\\\"\x00-\x1f\x7f-\uffff]/g : /[\\\"\x00-\x1f\x7f-\xff]/g,
        D = /^(-\d+|\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(?:\.(\d{1,3}))?((?:[+-](\d{2})(\d{2}))|Z)?$/,
        E = function (t, i, a, v, m, n, p) {
            for (i in t)
                if ((a = typeof (v = t[i])) == 'string' && (v = D.exec(v))) {
                    m = v[8] && v[8].charAt(0);
                    if (m != 'Z') n = (m == '-' ? -1 : 1) * ((+v[9] || 0) * 60) + (+v[10] || 0);
                    else n = 0;
                    m = new Date(+v[1], +v[2] - 1, +v[3], +v[4], +v[5], +v[6], +v[7] || 0);
                    n += m.getTimezoneOffset();
                    if (n) m.setTime(m.getTime() + n * 60000);
                    t[i] = m;
                } else if (a == 'object' && t[i] && (ood.isObj(t[i]) || ood.isArr(t[i]))) E(t[i]);
            return t;
        },

        F = 'function',
        N = 'number',
        L = 'boolean',
        S = 'string',
        O = 'object',
        T = {},
        PS = function (v, n) {
            return ("000" + (v || 0)).slice(-n)
        },
        Z = (function (a, b) {
            a = -(new Date).getTimezoneOffset() / 60;
            b = a > 0 ? '+' : '-';
            a = '' + Math.abs(a);
            return b + (a.length == 1 ? '0' : '') + a + '00'
        })();

    T['undefined'] = function () {
        return 'null'
    };
    T[L] = function (x) {
        return String(x)
    };
    T[N] = function (x) {
        return ((x || x === 0) && isFinite(x)) ? String(x) : 'null'
    };
    T[S] = function (x) {
        return H[x] ||
            '"' +
            (
                A1.test(x)
                    ?
                    x.replace(A2, function (a, b) {
                        if (b = M[a]) return b;
                        return '\\u' + ((b = a.charCodeAt()) < 16 ? '000' : b < 256 ? '00' : b < 4096 ? '0' : '') + b.toString(16)
                    })
                    :
                    x
            )
            + '"'
    };
    T[O] = function (x, filter, dateformat, deep, max, MAXL, MAXS) {
        var map = {
            prototype: 1,
            constructor: 1,
            toString: 1,
            valueOf: 1,
            toLocaleString: 1,
            propertyIsEnumerable: 1,
            isPrototypeOf: 1,
            hasOwnProperty: 1
        };
        deep = deep || 1;
        max = max || 0;
        MAXL = MAXL || ood.SERIALIZEMAXLAYER;
        MAXS = MAXS || ood.SERIALIZEMAXSIZE;
        if (deep > MAXL || max > MAXS) return ood.$_outofmilimted;
        max++;
        if (x) {
            var a = [], b = [], f, i, l, v;
            if (x === window) return "window";
            if (x === document) return "document";
            //for ie alien
            if ((typeof x == O || typeof x == F) && !ood.isFun(x.constructor))
                return x.nodeType ? "document.getElementById('" + x.id + "')" : "$alien";
            else if (ood.isArr(x)) {
                a[0] = '[';
                l = x.length;
                for (i = 0; i < l; ++i) {
                    if (typeof filter == 'function' && false == filter.call(x, x[i], i, b)) continue;

                    if (ood.isNaN(v = x[i])) b[b.length] = "NaN";
                    else if (ood.isNull(v)) b[b.length] = "null";
                    else if (!ood.isDefined(v)) b[b.length] = "undefined";
                    else if (f = T[typeof v]) {
                        if (typeof (v = f(v, filter, dateformat, deep + 1, max, MAXL, MAXS)) == S)
                            b[b.length] = v;
                    }
                }
                a[2] = ']';
            } else if (ood.isDate(x)) {
                if (dateformat == 'utc')
                    return '"' + PS(x.getUTCFullYear(), 4) + '-' +
                        PS(x.getUTCMonth() + 1, 2) + '-' +
                        PS(x.getUTCDate(), 2) + 'T' +
                        PS(x.getUTCHours(), 2) + ':' +
                        PS(x.getUTCMinutes(), 2) + ':' +
                        PS(x.getUTCSeconds(), 2) + '.' +
                        PS(x.getUTCMilliseconds(), 3) +
                        'Z"';
                else if (dateformat == 'gmt')
                    return '"' + PS(x.getFullYear(), 4) + '-' +
                        PS(x.getMonth() + 1, 2) + '-' +
                        PS(x.getDate(), 2) + 'T' +
                        PS(x.getHours(), 2) + ':' +
                        PS(x.getMinutes(), 2) + ':' +
                        PS(x.getSeconds(), 2) + '.' +
                        PS(x.getMilliseconds(), 3) +
                        Z + '"';
                else
                    return 'new Date(' + [x.getFullYear(), x.getMonth(), x.getDate(), x.getHours(), x.getMinutes(), x.getSeconds(), x.getMilliseconds()].join(',') + ')';
            } else if (ood.isReg(x)) {
                return String(x);
            } else {
                if (typeof x.serialize == F)
                    x = x.serialize();
                if (typeof x == O) {
                    if (x.nodeType) {
                        return "document.getElementById('" + x.id + "')";
                    } else {
                        a[0] = '{';
                        for (i in x) {
                            if (map[i] ||
                                (filter === true ? i.charAt(0) == '_' : typeof filter == 'function' ? false === filter.call(x, x[i], i, b) : 0))
                                continue;
                            if (ood.isNaN(v = x[i])) b[b.length] = T.string(i) + ':' + "NaN";
                            else if (ood.isNull(v)) b[b.length] = T.string(i) + ':' + "null";
                            else if (!ood.isDefined(v)) b[b.length] = T.string(i) + ':' + "undefined";
                            else if (f = T[typeof v]) {
                                if (typeof (v = f(v, filter, dateformat, deep + 1, max, MAXL, MAXS)) == S)
                                    b[b.length] = T.string(i) + ':' + v;
                            }
                        }
                        a[2] = '}';
                    }
                } else return String(x);
            }
            a[1] = b.join(', ');
            return a[0] + a[1] + a[2];
        }
        return 'null'
    };
    T[F] = function (x) {
        return x.$path ? x.$path : String(x)
    };

    ood.$_outofmilimted = '"\x01...\x01"';

    //serialize object to string (bool/string/number/array/hash/simple function)
    ood.serialize = function (obj, filter, dateformat, MAXL, MAXS) {
        return ood.isNaN(obj) ? "NaN" :
            ood.isNull(obj) ? "null" :
                !ood.isDefined(obj) ? "undefined" :
                    T[typeof obj](obj, filter, ood.getDateFormat(dateformat), 0, 0, MAXL, MAXS) || '';
    };
    ood.stringify = function (obj, filter, dateformat, MAXL, MAXS) {
        return ood.fromUTF8(ood.serialize(obj, filter, ood.getDateFormat(dateformat), 0, 0, MAXL, MAXS));
    };
    // for safe global
    var safeW;
    //unserialize string to object
    ood.unserialize = function (str, dateformat) {
        if (typeof str != "string") return str;
        if (!str) return false;
        if (!safeW) {
            var ifr = document.createElement(ood.browser.ie && ood.browser.ver < 9 ? "<iframe>" : "iframe"), w;
            document.body.appendChild(ifr);
            w = frames[frames.length - 1].window;
            safeW = {};
            for (var i in w) safeW[i] = null;
            document.body.removeChild(ifr);
        }
        str = '({_:(function(){with(this){return ' + str + '}}).call(safeW)})';
        try {
            str = eval(str);
        } catch (e) {
            return false;
        }
        if (ood.getDateFormat(dateformat)) E(str);
        str = str._;
        return str;
    };
};

/*26 based id, some number id can crash opera9
*/
new function () {
    ood.id = function () {
        var self = this;
        if (self.constructor !== ood.id || self.a)
            return (ood.id._ || (ood.id._ = new ood.id)).next();
        self.a = [-1];
        self.b = [''];
        self.value = '';
    };
    ood.id.prototype = {
        constructor: ood.id,
        _chars: "abcdefghijklmnopqrstuvwxyz".split(''),
        next: function (i) {
            with (this) {
                i = (i || i === 0) ? i : b.length - 1;
                var m, k, l;
                if ((m = a[i]) >= 25) {
                    m = 0;
                    if (i === 0) {
                        a.splice(0, 0, 1);
                        b.splice(0, 0, 'a');
                        l = a.length;
                        for (k = 1; k < l; ++k) {
                            a[k] = 0;
                            b[k] = '0';
                        }
                        ++i;
                    } else
                        next(i - 1);
                } else ++m;
                a[i] = m;
                b[i] = _chars[m];
                return value = b.join('');
            }
        }
    };
};


// Some basic Classes

/* ood.Thread
    Dependencies: ood
    parameters:
        id: id of this thread, if input null, thread will create a new id
        tasks: [task,task,task ...] or [{},{},{} ...]
            task: function
            or
            {
              task,      //function
              args,      //args array for task
              scope,     //this object for task
              delay ,    //ms number
              callback   //function for callback
           }
        delay:default delay time;
        callback:default calback function;
        onStart: on start function
        onEnd: on end function
        cycle: is the thread circular
*/
ood.Class('ood.Thread', null, {
    Constructor: function (id, tasks, delay, callback, onStart, onEnd, cycle) {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        //for api call directly
        var self = this, me = arguments.callee, t = ood.$cache.thread;
        // ood.Thread() => self.constructor!==me
        // in an inner method => !!self.id is true
        if (self.constructor !== me || !!self.id)
            return new me(id, tasks, delay, callback, onStart, onEnd, cycle);

        if (typeof id != 'string') id = '$' + (self.constructor.$xid++);
        self.id = id;
        //thread profile
        self.profile = t[id] || (t[id] = {
            id: id,
            _start: false,
            time: 0,
            _left: 0,
            _asy: 0.1,
            //sleep_flag:-1,
            index: 0,

            tasks: tasks || [],
            delay: delay || 0,
            callback: callback,
            onStart: onStart,
            onEnd: onEnd,
            cache: {},
            status: "ini",
            cycle: !!cycle,
            instance: self
        });
    },
    Instance: {
        _fun: ood.fun(),
        __gc: function () {
            var m = ood.$cache.thread, t = m[this.id];
            if (t) {
                delete m[this.id];
                t.tasks.length = 0;
                for (var i in t) t[i] = null;
            }
        },
        _task: function () {
            var self = this, p = self.profile;

            // maybe abort or no task
            if (!p || !p.status || !p.tasks)
                return;
            // reset the asy flag
            p._asy = 0.1;

            var t = {}, value = p.tasks[p.index], r, i, type = typeof value;

            //function
            if (type == 'function') t.task = value;
            //hash
            else if (type == 'object')
                for (i in value) t[i] = value[i];

            //default callback
            if (typeof t.callback != 'function')
                t.callback = p.callback

            if (typeof t.task == 'function') {
                t.args = t.args || [];
                //last arg is threadid
                t.args.push(p.id);
            }

            // to next pointer
            p.index++;
            p.time = ood.stamp();

            // the real task
            if (typeof t.task == 'function') {
                r = ood.tryF(t.task, t.args || [p.id], t.scope || self, null);
            }

            // maybe abort called in abover task
            if (!p.status)
                return;

            // cache return value
            if (t.id)
                p.cache[t.id] = r;

            // if callback return false, stop.
            if (t.callback && false === ood.tryF(t.callback, [p.id], self, true))
                return self.abort('callback');
            // if set suspend at t.task or t.callback , stop continue running
            if (p.status !== "run")
                return;

            self.start();
        },
        start: function (time, delaycb) {
            var self = this, p = self.profile;

            if (p.__delaycb) {
                ood.tryF(p.__delaycb, [p.id], self);
                delete p.__delaycb;
            }
            if (delaycb) {
                p.__delaycb = delaycb;
            }

            if (p._start === false) {
                p._start = true;
                //call onstart
                if (p.onStart) {
                    var r = ood.tryF(p.onStart, [p.id], self);
                    if (false === r) {
                        return self.abort('start');
                    } else if (true === r) {
                        return;
                    } else if (ood.isNumb(r)) {
                        self.suspend(r);
                        return;
                    }
                }
            }

            p.status = "run";

            if (!p.tasks.length)
                return self.abort('empty');

            if (p.index >= p.tasks.length) {
                if (p.cycle === true)
                    self.profile.index = 0;
                else
                    return self.abort('normal');
            }
            var task = p.tasks[p.index],
                delay = typeof task == 'number' ? task : (task && typeof task.delay == 'number') ? task.delay : p.delay;

            p._left = (time || time === 0) ? time : delay;

            // clear the mistake trigger task
            if (p._asy != 0.1)
                ood.clearTimeout(p._asy);

            p._asy = ood.asyRun(self._task, p._left, [], self);
            p.time = ood.stamp();
            return self;
        },
        suspend: function (time, delaycb) {
            var n, p = this.profile;
            if (p.status == "pause") return;
            p.status = "pause";

            if (p._asy !== 0.1) {
                ood.clearTimeout(p._asy);
                if (p.index > 0) p.index--;
            }
            n = p._left - (ood.stamp() - p.time);

            p._left = (n >= 0 ? n : 0);

            if ((Number(time) || 0))
                this.resume(time, delaycb);

            return this;
        },
        /*time
        number:set timeout to number
        true:set timeout to default
        false:set timeout to 0
        undefined: timetou to left
        */
        resume: function (time, delaycb) {
            var self = this, p = self.profile;
            if (p.status == "run") return self;

            time = time === undefined ? p._left :
                time === true ? p.delay :
                    time === false ? 0 :
                        (Number(time) || 0);

            p.status = "run";
            self.start(time, delaycb);
            return self;
        },
        abort: function (flag) {
            var self = this, p = self.profile;
            if (p.status == "stop") return;
            p.status = "stop";

            var onEnd = p.onEnd, id = p.id;
            ood.clearTimeout(p._asy);
            this.__gc();
            // at last
            ood.tryF(onEnd, [id, flag]);
        },
        links: function (thread) {
            var p = this.profile, onEnd = p.onEnd, id = p.id;
            p.onEnd = function (tid, flag) {
                ood.tryF(onEnd, [tid, flag]);
                thread.start()
            };
            return this;
        },
        insert: function (arr, index) {
            var self = this, o = self.profile.tasks, l = o.length, a;
            if (!ood.isArr(arr)) arr = [arr];
            index = index || self.profile.index;
            if (index < 0) index = -1;
            if (index == -1) {
                Array.prototype.push.apply(o, arr);
            } else {
                if (index > l) index = l;
                a = o.splice(index, l - index);
                o.push.apply(o, arr);
                o.push.apply(o, a);
            }
            return self;
        },
        getCache: function (key) {
            return this.profile.cache[key];
        },
        setCache: function (key, value) {
            this.profile.cache[key] = value;
            return this;
        },
        isAlive: function () {
            return !!ood.$cache.thread[this.id];
        },
        getStatus: function () {
            return this.profile.status;
        }
    },
    After: function () {
        /*
        give shortcut to some functions
        only for the existing thread
        */
        var self = this, f = function (i) {
                self[i] = function (id) {
                    var t;
                    if (ood.$cache.thread[id])
                        return (t = ood.Thread(id))[i].apply(t, Array.prototype.slice.call(arguments, 1));
                }
            },
            a = 'start,suspend,resume,abort,links,insert,isAlive,getStatus'.split(',');
        for (var i = 0, l = a.length; i < l; i++) f(a[i]);
    },
    Static: {
        $asFunction: 1,
        $xid: 1,
        __gc: function () {
            ood.$cache.thread = {};
        },
        get: function (id) {
            id = ood.$cache.thread[id];
            return id && id.instance;
        },
        isAlive: function (id) {
            return !!ood.$cache.thread[id];
        },
        //Dependencies: ood.Dom
        observableRun: function (tasks, onEnd, threadid, busyMsg) {
            var thread = ood.Thread, dom = ood.Dom;
            if (!ood.isArr(tasks)) tasks = [tasks];
            //if thread exists, just inset task to the next positiong
            if (ood.$cache.thread[threadid]) {
                if (typeof onEnd == 'function')
                    tasks.push(onEnd);
                thread.insert(threadid, tasks);
                //if does not exist, create a new thread
            } else {
                thread(threadid, tasks,
                    0, null,
                    //set busy status to UI
                    function (threadid) {
                        // if (dom) dom.busy(threadid, busyMsg);
                    },
                    //set free status to UI
                    function (threadid) {
                        ood.tryF(onEnd, arguments, this);
                        if (dom) dom.free(threadid);
                    }
                ).start();
            }
        },
        /*group thread run once
        group: hash include thread or threadid
        callback: call after a thread finish
        onStart:before all threads start
        onEnd:after all threads end
        */
        group: function (id, group, callback, onStart, onEnd) {
            var bak = {},
                thread = ood.Thread,
                f = function (o, i, threadid) {
                    if (typeof o == 'string') o = thread(o);
                    if (o) {
                        var f = function () {
                            var me = arguments.callee;
                            ood.tryF(me.onEnd, arguments, this);
                            me.onEnd = null;
                            delete bak[i];
                            //call callback here
                            ood.tryF(callback, [i, threadid], this);
                            if (ood.isEmpty(bak))
                                thread.resume(threadid);
                        };
                        f.onEnd = o.profile.onEnd;
                        o.profile.onEnd = f;
                        o.start();
                    }
                };
            for (var i in group) bak[i] = 1;
            return thread(id, [function (threadid) {
                if (!ood.isEmpty(group)) {
                    thread.suspend(threadid);
                    for (var i in group) f(group[i], i, threadid);
                }
            }], 0, null, onStart, onEnd);
        },
        repeat: function (task, interval, onStart, onEnd) {
            return ood.Thread(null, [null], interval || 0, task, onStart, onEnd, true).start();
        }
    }
});

/*ood.absIO/ajax
    Dependencies: ood.Thread

            get     post    get(cross domain)   post(corss domain)  post file   return big data(corss domain)
    ajax    +       +       -                   -                   -           -
    sajax   +       -       +                   -                   -           * JSONP
    iajax   +       +       +                   *                   *           * IDMI
*/
ood.Class('ood.absIO', null, {
    Constructor: function (uri, query, onSuccess, onFail, threadid, options) {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        //get properties
        if (typeof uri == 'object')
            options = uri;
        else {
            options = options || {};
            ood.merge(options, {
                uri: uri,
                query: query,
                onSuccess: onSuccess,
                onFail: onFail,
                threadid: threadid
            });
        }
        //for cache
        var self = this, me = arguments.callee, con = self.constructor;
        if ((con !== me) || self.id)
            return new me(options);

        //give defalut value to those members
        ood.merge(options, {
            id: options.id || ('' + (con._id++)),
            uid: ('' + (con.uid++)),
            uri: options.uri ? ood.adjustRes(options.uri, 0, 1, 1) : '',
            username: options.username || undefined,
            password: options.password || undefined,
            query: options.query || '',
            contentType: options.contentType || '',
            Accept: options.Accept || '',
            header: options.header || null,
            asy: options.asy !== false
        }, 'all');
        var m = (options.method || con.method).toUpperCase();
        options.method = 'POST' == m ? 'POST' : 'PUT' == m ? 'PUT' : 'DELETE' == m ? 'DELETE' : 'PATCH' == m ? 'PATCH' : 'GET';

        var a = 'retry,timeout,reqType,rspType,optimized,customQS'.split(',');
        for (var i = 0, l = a.length; i < l; i++) {
            options[a[i]] = (a[i] in options) ? options[a[i]] : con[a[i]];
            if (typeof options[a[i]] == "string")
                options[a[i]] = options[a[i]].toLowerCase();
        }

        ood.merge(self, options, 'all');

        if (self.reqType == 'xml')
            self.method = "POST";

        if (con.events)
            ood.merge(self, con.events);

        self.query = self.customQS(self.query, options && options.exData);

        // remove all undefined item
        if (typeof self.query == 'object' && self.reqType != "xml")
            self.query = ood.copy(self.query, function (o) {
                return o !== undefined
            });

        if (!self._useForm && ood.isHash(self.query) && self.reqType != "xml")
            self.query = con._buildQS(self.query, self.reqType == "json", self.method == 'POST');

        return self;
    },
    Instance: {
        _fun: ood.fun(),
        _flag: 0,
        _response: false,
        _txtresponse: '',
        _retryNo: 0,

        _time: function () {
            var self = this, c = self.constructor;
            self._clear();
            if (self._retryNo < self.retry) {
                self._retryNo++;
                ood.tryF(self.onRetry, [self._retryNo], self);
                self.start();
            } else {
                if (false !== ood.tryF(self.onTimeout, [], self))
                    self._onError(new Error("Request timeout"));
            }
        },
        _onEnd: function () {
            var self = this;
            if (!self._end) {
                self._end = true;
                if (self._flag > 0) {
                    ood.clearTimeout(self._flag);
                    self._flag = 0
                }
                ood.Thread.resume(self.threadid);
                ood.tryF(self.$onEnd, [], self);
                ood.tryF(self.onEnd, [], self);
                self._clear();
            }
        },
        _onStart: function () {
            var self = this;
            ood.Thread.suspend(self.threadid);
            ood.tryF(self.$onStart, [], self);
            ood.tryF(self.onStart, [], self);
        },
        _onResponse: function () {
            var self = this;
            if (false !== ood.tryF(self.beforeSuccess, [self._response, self.rspType, self.threadid], self))
                ood.tryF(self.onSuccess, [self._response, self.rspType, self.threadid], self);
            self._onEnd();
        },
        _onError: function (e) {
            var self = this;
            if (false !== ood.tryF(self.beforeFail, [e, self.threadid], self))
                ood.tryF(self.onFail, [e.name ? (e.name + ": " + e.message) : e, self.rspType, self.threadid, e], self);
            self._onEnd();
        },
        isAlive: function () {
            return !this._end;
        },
        abort: function () {
            this._onEnd();
        }
    },
    Static: {
        $abstract: true,
        get: function (uri, query, onSuccess, onFail, threadid, options) {
            options = options || {};
            options.method = "GET";
            return this.apply(this, arguments).start();
        },
        post: function (uri, query, onSuccess, onFail, threadid, options) {
            options = options || {};
            options.method = "POST";
            return this.apply(this, arguments).start();
        },
        _id: 1,
        uid: 1,
        method: 'GET',
        retry: 0,
        timeout: 60000,
        //form, xml, or json
        reqType: 'form',
        //json, xml, text, script
        rspType: 'json',

        optimized: false,

        callback: 'callback',

        _buildQS: function (hash, flag, post) {
            hash = ood.clone(hash, function (o, i) {
                return !(ood.isNaN(o) || !ood.isDefined(o))
            });
            return flag ? ((flag = ood.serialize(hash)) && (post ? flag : encodeURIComponent(flag))) : ood.urlEncode(hash);
        },
        customQS: function (obj, ex) {
            if (ex) {
                if (typeof obj == 'string') {
                    obj = (obj || "") + "&" + ood.urlEncode(ex);
                } else {
                    ood.merge(obj, ex, 'all');
                }
            }
            return obj;
        },
        _if: function (doc, id, onLoad) {
            var ie8 = ood.browser.ie && ood.browser.ver < 9,
                scr = ie8
                    ? ("<iframe " + (id ? ("name='" + "ood_xdmi:" + id + "'") : "") + (onLoad ? (" onload='ood.XDMI._o(\"" + id + "\")'") : "") + ">")
                    : "iframe";
            var n = doc.createElement(scr), w;
            if (id) n.id = n.name = "ood_xdmi:" + id;
            if (!ie8 && onLoad) n.onload = onLoad;
            n.style.display = "none";
            doc.body.appendChild(n);
            w = frames[frames.length - 1].window;
            return [n, w, w.document];
        },
        isCrossDomain: function (uri) {
            var b = ood._localParts;
            uri = uri.replace(/#.*$/, "").replace(/^\/\//, b[1] + "//");
            var a = ood._uriReg.exec((uri || '').toLowerCase());
            return !!(a && (
                    a[1] !== b[1] ||
                    a[2] !== b[2] ||
                    (a[3] || (a[1] === "http:" ? 80 : 443)) !== (b[3] || (b[1] === "http:" ? 80 : 443))
                )
            );
        },
        //get multi ajax results once
        groupCall: function (hash, callback, onStart, onEnd, threadid) {
            var i, f = function (o, i, hash) {
                hash[i] = ood.Thread(null, [function (threadid) {
                    o.threadid = threadid;
                    o.start();
                }]);
            };
            for (i in hash) f(hash[i], i, hash);
            return ood.Thread.group(null, hash, callback, function () {
                ood.Thread.suspend(threadid);
                ood.tryF(onStart, arguments, this);
            }, function () {
                ood.tryF(onEnd, arguments, this);
                ood.Thread.resume(threadid);
            }).start();
        }
    }
});
// AJAX
ood.Class('ood.Ajax', 'ood.absIO', {
    Instance: {
        _XML: null,
        _unsafeHeader: "Accept-Charset,Accept-Encoding,Access-Control-Request-Headers,Access-Control-Request-Method,Connection,Content-Length,Cookie,Cookie2,Date,DNT,Expect,Host,Keep-Alive,Origin,Referer,TE,Trailer,Transfer-Encoding,Upgrade,User-Agent,Via".toLowerCase().split(","),
        _isunsafe: function (k) {
            return ood.browser.isWebKit && (ood.str.startWith("Proxy-", k) || ood.str.startWith("Sec-", k) || ood.arr.indexOf(this._unsafeHeader, k.toLowerCase()) !== -1);
        },
        _header: function (n, v) {
            if (!this._isunsafe(n)) {
                if (this._XML) this._XML.setRequestHeader(n, v);
            }
        },
        start: function () {
            var self = this;
            if (false === ood.tryF(self.beforeStart, [], self)) {
                self._onEnd();
                return;
            }
            if (!self._retryNo)
                self._onStart();
            try {
                with (self) {
                    //must use "self._XML", else opera will not set the new one
                    self._XML = new window.XMLHttpRequest();
                    if (asy)
                        self._XML.onreadystatechange = function () {
                            if (self && self._XML && self._XML.readyState == 4) {
                                /*//Checking responseXML for Terminated unexpectedly in firfox
                               if(ood.browser.gek && !self._XML.responseXML)
                                    self._onError(new Error('errXMLHTTP:Terminated unexpectedly!'));
                               else*/
                                self._complete.apply(self);
                                //must clear here, else memory leak
                                self._clear();
                            }
                        };

                    if (!_retryNo && method != "POST") {
                        if (query)
                            uri = uri.split("?")[0] + "?" + query;
                        query = null;
                    }
                    if (username && password)
                        self._XML.open(method, uri, asy, username, password);
                    else
                        self._XML.open(method, uri, asy);

                    self._header("Accept", Accept ? Accept :
                        (rspType == 'json' ? "application/json,text/javascript,*/*;q=0.01" : rspType == 'xml' ? "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" : "*/*")
                    );
                    self._header("Content-Type", contentType ? contentType : (
                        (reqType == 'xml' ? "text/xml; " : reqType == 'json' ? "application/json; " : method == "POST" ? "application/x-www-form-urlencoded; " : "") + "charset=" + (self.charset || "UTF-8")
                    ));
                    self._header("X-Requested-With", "XMLHttpRequest");
                    if (optimized) {
                        try {
                            self._header("User-Agent", null);
                            self._header("Accept-Language", null);
                            self._header("Connection", "keep-alive");
                            self._header("Keep-Alive", null);
                            self._header("Cookie", null);
                            self._header("Cookie", "");
                        } catch (e) {
                        }
                    }
                    try {
                        if (ood.isHash(header))
                            ood.each(header, function (o, i) {
                                self._header(i, o);
                            });
                    } catch (e) {
                    }

                    if (false === ood.tryF(self.beforeSend, [self._XML], self)) {
                        self._onEnd();
                        return;
                    }

                    //for firefox syc GET bug
                    try {
                        self._XML.send(query);
                    } catch (e) {
                    }

                    if (asy) {
                        if (self._XML && timeout > 0)
                            _flag = ood.asyRun(function () {
                                if (self && !self._end) {
                                    self._time()
                                }
                            }, self.timeout);
                    } else
                        return _complete();
                }
            } catch (e) {
                self._onError(e);
            }
            return self;
        },
        abort: function () {
            var self = this;
            if (self._XML) {
                self._XML.onreadystatechange = self._fun;
                self._XML.abort();
                self._XML = null;
            }
            arguments.callee.upper.call(self);
        },
        _clear: function () {
            var self = this;
            if (self._XML) {
                self._XML.onreadystatechange = self._fun;
                self._XML = null;
            }
        },
        _complete: function () {
            with (this) {
                //this is for opera
                var ns = this, obj, status = ns._XML.status;
                _txtresponse = rspType == 'xml' ? ns._XML.responseXML : ns._XML.responseText;
                // try to get js object, or the original
                _response = rspType == "json" ?
                    /^\s*\</.test(_txtresponse) && (obj = ood.XML.xml2json(ood.XML.parseXML(_txtresponse))) && obj.data ? obj.data
                        : ((obj = ood.unserialize(_txtresponse)) === false ? _txtresponse : obj)
                    : _txtresponse;

                // crack for some local case ( OK but status is 0 in no-IE browser)
                if (!status && ood._localReg.test(ood._localParts[1])) {
                    status = ns._XML.responseText ? 200 : 404;
                }

                // for IE7
                if (status == 1223) status = 204;

                if (status >= 200 && status < 300 || status == 304)
                    _onResponse();
                // offline or other Network problems
                else if (status === undefined || status < 10)
                    _onError(new Error('Network problems--' + status));
                else
                    _onError(new Error('XMLHTTP returns--' + status));
            }
            return this._response;
        }
    },
    Static: {
        $asFunction: 1
    }
});

// JSONP
ood.Class('ood.JSONP', 'ood.absIO', {
    Instance: {
        start: function () {
            var self = this, id, c = self.constructor, t, n, ok = false;
            if (false === ood.tryF(self.beforeStart, [], self)) {
                self._onEnd();
                return;
            }
            if (!self._retryNo)
                self._onStart();
            //dont retry for loading script
            if (self.rspType == 'script')
                self.retry = 0;

            //first
            id = self.id;
            if (c._pool[id])
                c._pool[id].push(self);
            else
                c._pool[id] = [self];

            c.No["_" + id] = function (rsp) {
                c.$response(rsp, id);
            };

            var w = document,
                _cb = function () {
                    if (!ok) {
                        ok = true;
                        if (self.rspType == 'script') {
                            if (typeof self.checkKey == 'string')
                                ood.setTimeout(function () {
                                    ood.exec("!function(t){"
                                        + "if(t=ood.get(ood.JSONP,['_pool','" + id + "',0])) {"
                                        + "if(ood.SC.get('" + self.checkKey + "'))t._onResponse();"
                                        + "else t._loaded();"
                                        + "}"
                                        + "}()");
                                    // ensure using setTimeout, for the case:
                                    //    When the page loading, if you switch to the other page, and return back after timeout, the ood.JSONP._pool["1"] will be deleted
                                    //    In this case: setTimeout will be executed first (it'll clear the JSONP), and requestAnimationFrame will be executed later
                                }, false);
                            else
                                self._onResponse();
                        } else
                            self._loaded();
                    }
                };
            n = self.node = w.createElement("script");

            var uri = self.uri, tag = self.tag || self.$tag;

            if (tag) {
                if (self.query && self.query.indexOf("tag=") == -1) {
                    uri = uri.split("?")[0] + "?" + self.query + "&tag=" + tag;
                } else {
                    uri = uri + "?tag=" + tag;
                }
            }


            n.src = uri;
            n.type = 'text/javascript';
            n.charset = self.charset || 'UTF-8';
            n.onload = n.onreadystatechange = function () {
                if (ok)
                    return;
                var t = this.readyState;
                if (!t || t == "loaded" || t == "complete")
                    _cb();

                if (t == 'interactive' && ood.browser.opr) {
                    ood.Thread.repeat(function () {
                        if (ok)
                            return false;
                        if (/loaded|complete/.test(document.readyState)) {
                            _cb();
                            return false;
                        }
                    }, 50);
                }
            };

            if ('onerror' in n)
                n.onerror = function (e) {
                    //clear first
                    self._clear();
                    self._onError(new Error("Not Found - " + uri));
                    self = null;
                    return;
                };

            (w.body || w.getElementsByTagName("head")[0]).appendChild(n);

            n = null;

            //set timeout
            if (self.timeout > 0)
                self._flag = ood.asyRun(function () {
                    if (self && !self._end) {
                        self._time()
                    }
                }, self.timeout);
        },
        _clear: function () {
            var self = this, n = self.node, c = self.constructor, id = self.id, _pool = c._pool;
            if (_pool[id]) {
                _pool[id].length = 0;
                delete _pool[id];
            }
            delete c.No["_" + id];

            if (n) {
                self.node = n.onload = n.onreadystatechange = n.onerror = null;

                var div = document.createElement('div');
                //in ie + add script with url(remove script immediately) + add the same script(remove script immediately) => crash
                //so, always clear it later
                div.appendChild(n.parentNode && n.parentNode.removeChild(n) || n);
                if (ood.browser.ie)
                    ood.asyRun(function () {
                        div.innerHTML = n.outerHTML = '';
                        if (ood.isEmpty(_pool)) c._id = 1;
                        _pool = c = n = div = null;
                    });
                else {
                    ood.asyRun(function () {
                        div.innerHTML = '';
                        n = div = null;
                        if (ood.isEmpty(_pool)) c._id = 1;
                    });
                }
            } else {
                if (ood.isEmpty(_pool)) c._id = 1;
            }
        },
        _loaded: function () {
            var self = this;
            ood.asyRun(function () {
                if (self.id && self.constructor._pool[self.id])
                    self._onError(new Error("JSONP return script doesn't match"));
            }, 500);
        }
    },
    Static: {
        $asFunction: 1,
        _pool: {},
        "No": {},
        $response: function (obj, id) {
            var self = this, o;
            if (obj && (o = self._pool[id])) {
                for (var i = 0, l = o.length; i < l; i++) {
                    o[i]._response = obj;
                    o[i]._onResponse();
                }
            } else
                self._onError(new Error("JSONP return value formatting error--" + obj));
        },
        customQS: function (obj, ex) {
            var c = this.constructor, b = c.callback, nr = (this.rspType != 'script'), r;
            if (typeof obj == 'string') {
                obj = (obj || "") + (nr ? ("&" + b + '=ood.JSONP.No._' + this.id) : '');
                if (ex) obj = (obj || "") + (nr ? ("&" + ood.urlEncode(ex)) : '');
            } else {
                if (nr) {
                    obj[b] = "ood.JSONP.No._" + this.id;
                    if (ex) ood.merge(obj, ex, 'all');
                }
            }
            return obj;
        }
    }
});

// XDMI : Cross-Domain Messaging with iframes
ood.Class('ood.XDMI', 'ood.absIO', {
    Instance: {
        _useForm: true,
        start: function () {
            var self = this, w = window, c = self.constructor, i, id, t, n, k, o, b, form, onload;
            if (false === ood.tryF(self.beforeStart, [], self)) {
                self._onEnd();
                return;
            }
            if (!self._retryNo)
                self._onStart();

            //first
            id = self.id;
            if (c._pool[id])
                c._pool[id].push(self);
            else
                c._pool[id] = [self];

            //create form
            var a = c._if(document, id, onload);
            self.node = a[0];
            self.frm = a[1];
            //create form
            form = self.form = document.createElement('form');
            form.style.display = 'none';

            // use postmessage
            if (w['postMessage']) {
                self._msgcb = function (e) {
                    if (!self.node) return;
                    // only take self message
                    if (e.source !== self.frm) {
                        return;
                    }
                    e = e.data;
                    if (self.rspType == "json") {
                        e = ood.unserialize(e) || e;
                    }
                    if (e && (t = c._pool[self.id])) {
                        for (var i = 0, l = t.length; i < l; i++) {
                            t[i]._response = e;
                            t[i]._onResponse();
                        }
                    } else {
                        //clear first
                        self._clear();
                        self._onError(new Error("XDMI return value formatting error"));
                    }
                };
                if (w.addEventListener) w.addEventListener('message', self._msgcb, false);
                else w.attachEvent('onmessage', self._msgcb);
            }
            // use window.name
            else {
                self._onload = onload = function (id) {
                    //in some situation, this function will be triggered twice.
                    if (self.OK) return;
                    //in IE/opera, "setting an image file as dummy" will trigger the second onload event with 'self.node == null'
                    if (!self.node) return;
                    var w = self.node.contentWindow, c = ood.XDMI, o, t;
                    //in opera, "set location" will trigger location=='about:blank' at first
                    if (ood.browser.opr) try {
                        if (w.location == 'about:blank') return
                    } catch (e) {
                    }
                    self.OK = 1;
                    // first round: try to syn domain
                    var flag = 0;
                    try {
                        if (w.name === undefined) flag = 1
                    } catch (e) {
                        flag = 1
                    }
                    if (flag) {
                        w.location.replace(c._getDummy() + '#' + ood.ini.dummy_tag);
                    }

                    // get data
                    var getData = function () {
                        // second round: try to get data
                        var flag = 0;
                        try {
                            if (w.name === undefined) flag = 1
                        } catch (e) {
                            flag = 1
                        }
                        if (flag) {
                            return ood.asyRun(getData);
                        }

                        var data;
                        if (("ood_xdmi:" + self.id) == w.name) {
                            //clear first
                            self._clear();
                            self._onError(new Error('XDMI no return value'));
                            return;
                        } else {
                            data = w.name;
                        }

                        if (data && (o = ood.unserialize(data)) && (t = c._pool[self.id])) {
                            for (var i = 0, l = t.length; i < l; i++) {
                                t[i]._response = o;
                                t[i]._onResponse();
                            }
                        } else {
                            //clear first
                            self._clear();
                            self._onError(new Error("XDMI return value formatting error, or no matched 'id'-- " + data));
                        }
                    };
                    getData();
                };
            }

            var uri = self.uri;
            if (self.method != 'POST')
                uri = uri.split("?")[0];

            form.action = self.uri;
            form.method = self.method;
            form.target = "ood_xdmi:" + id;

            k = self.query || {};
            var file, files = [];
            for (i in k) {
                if (k[i] && k[i]['ood.UIProfile'] && k[i].$oodFileCtrl) {
                    if (file = k[i].boxing().getUploadObj()) {
                        files.push({id: k[i].$xid, file: file});
                        file.id = file.name = i;
                        form.appendChild(file);
                        b = true;
                    }
                } else if (k[i] && k[i].nodeType == 1) {
                    k[i].id = k[i].name = i;
                    form.appendChild(k[i]);
                    b = true;
                } else {
                    if (ood.isDefined(k[i])) {
                        t = document.createElement('textarea');
                        t.id = t.name = i;
                        t.value = typeof k[i] == 'string' ? k[i] : ood.serialize(k[i], function (o) {
                            return o !== undefined
                        });
                        form.appendChild(t);
                    }
                }
            }
            if (self.method == 'POST' && b) {
                form.enctype = 'multipart/form-data';
                if (form.encoding)
                    form.encoding = form.enctype;
            }
            document.body.appendChild(form);
            //submit
            form.submit();

            if (files.length) {
                ood.arr.each(files, function (o, i) {
                    if (i = ood.getObject(o.id)) {
                        if (i['ood.UIProfile'] && i.boxing() && i.boxing().setUploadObj) {
                            i.boxing().setUploadObj(o.file);
                        }
                    }
                });
            }

            t = form = null;
            //set timeout
            if (self.timeout > 0)
                self._flag = ood.asyRun(function () {
                    if (self && !self._end) {
                        self._time()
                    }
                }, self.timeout);
        },
        _clear: function () {
            var self = this, n = self.node, f = self.form, c = self.constructor, w = window,
                div = document.createElement('div'), id = self.id, _pool = c._pool;
            if (_pool[id]) {
                _pool[id].length = 0;
                delete _pool[id];
            }
            if (n && w['postMessage']) {
                if (w.removeEventListener) w.removeEventListener('message', self._msgcb, false);
                else w.detachEvent('onmessage', self._msgcb);
                self._msgcb = null;
            } else {
                if (ood.browser.gek && n) try {
                    n.onload = null;
                    var d = n.contentWindow.document;
                    d.write(" ");
                    d.close()
                } catch (e) {
                }
            }
            self.form = self.node = self.frm = null;
            if (n) div.appendChild(n.parentNode.removeChild(n));
            if (f) div.appendChild(f.parentNode.removeChild(f));
            div.innerHTML = '';
            if (ood.isEmpty(_pool)) c._id = 1;
            f = div = null;
        }
    },
    Static: {
        $asFunction: 1,
        method: 'POST',
        _pool: {},
        _o: function (id) {
            var self = this, p = self._pool[id], o = p[p.length - 1];
            ood.tryF(o._onload);
        },
        _getDummy: function (win) {
            win = win || window;
            var ns = this,
                arr, o,
                d = win.document,
                ini = ood.ini,
                b = ood.browser,
                f = ns.isCrossDomain;
            if (ns.dummy) return ns.dummy;
            //can get from ood.ini;
            if (ini.dummy) return ns.dummy = ini.dummy;
            if (!f(ini.path)) {
                //not for 'ex-domain include ood' case
                if (!d.getElementById('ood:img:bg')) {
                    o = d.createElement('img');
                    o.id = 'ood:img:bg';
                    o.src = ini.img_bg;
                    o.style.display = 'none';
                    d.body.appendChild(o);
                    o = null;
                }
            }
            if (o = d.getElementById('ood:img:bg')) {
                return ns.dummy = o.src.split('#')[0];
            } else {
                arr = d.getElementsByTagName("img");
                for (var i = 0, j = arr.length; i < j; i++) {
                    o = arr[i];
                    if (o.src && !f(o.src))
                        return ns.dummy = o.src.split('#')[0];
                }

                if (b.gek) {
                    arr = d.getElementsByTagName("link");
                    for (var i = 0, j = arr.length; i < j; i++) {
                        o = arr[i];
                        if (o.rel == "stylesheet" && !f(o.href))
                            return ns.dummy = o.href.split('#')[0];
                    }
                }
            }
            //get from parent, not for opera in this case
            try {
                if (win != win.parent)
                    if ((win = win.parent) && !f('' + win.document.location.href))
                        return ns._getDummy(win);
            } catch (e) {
            }
            //for the last change, return a file name whether it existes or does not exist, and not cache it.
            return '/favicon.ico';
        },
        customQS: function (obj, ex) {
            var s = this, c = s.constructor, t = c.callback, w = window;
            if (window['postMessage'])
                obj[t] = obj.parentDomain = w.location.origin || (w.location.protocol + "//" + w.location.hostname + (w.location.port ? ':' + w.location.port : ''));
            else
                obj[t] = 'window.name';
            if (ex) ood.merge(obj, ex, 'all');
            return obj;
        }
    }
});

new function () {
    // for compitable
    ood.SAjax = ood.JSONP;
    ood.IAjax = ood.XDMI;
};

/*ood.SC for straight call
  Dependencies: ood.Thread; ood.absIO/ajax
*/
ood.Class('ood.SC', null, {
    Constructor: function (path, callback, isAsy, threadid, options, force) {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        var p = ood.$cache.SC, r;
        if (r = p[path] || (p[path] = ood.get(window, path.split('.'))))
            ood.tryF(callback, [path, null, threadid], r);
        else {
            options = options || {};
            options.$cb = callback;
            if (isAsy) options.threadid = threadid;
            r = p[path] = ood.SC._call(path || '', options, isAsy, force);
        }
        return r;
    },
    Static: {
        $asFunction: 1,
        __gc: function (k) {
            ood.$cache.SC = {};
        },
        // default, SC will get script from url:
        //        App.Name => ./App/Name.js
        //onSucess(text,rspType,threadid)
        //onFail(text,rspType,threadid)
        // "return false" will stop the default Ajax calling
        beforeGetScript: function (path, onSucess, onFail) {
        },
        //get object from obj string
        get: function (path, obj1, obj2, v) {
            // a[1][2].b[3] => a,1,2,b,3
            path = (path || '').replace(/\]$/g, '').split(/[\[\]\.]+/);

            if (obj1) v = ood.get(obj1, path);
            if (obj2 && v === undefined) v = ood.get(obj2, path);
            if (v === undefined) v = ood.get(window, path);
            return v;
        },
        /* function for "Straight Call"
        *   asy     loadSnips use
        *   true    true    ajax
        *   true    false   sajax JSONP
        *   false   ture    ajax
        *   false   false   ajax
        */
        _call: function (s, options, isAsy, force) {
            isAsy = !!isAsy;
            var i, t, r, o, funs = [], ep = ood.SC.get, ct = ood.$cache.snipScript,
                f = function (text, rspType, threadid) {
                    var self = this, t, uri = this.uri;
                    if (text) {
                        //test again when asy end.
                        if (!ep(s)) {
                            //loadSnips only
                            if (self.$p)
                                (self.$cache || ct)[self.$tag] = text;
                            else
                            //for sy xmlhttp ajax
                                try {
                                    ood.exec(text, s)
                                } catch (e) {
                                    throw e.name + ": " + e.message + " " + self.$tag
                                }
                        }
                    }
                    t = ood.Class._last;
                    ood.Class._ignoreNSCache = ood.Class._last = null;
                    // specified class must be in the first, maybe multi classes in code
                    // and give a change to load the last class in code, if specified class doesn't exist
                    ood.tryF(self.$cb, [self.$tag, text, threadid], ep(s) || t || {});
                    if (!ep(s) && t && t.KEY != s)
                        ood.log("[ood] > '" + s + "' doesn't in '" + uri + "'. The last class '" + t.KEY + "' was triggered.");
                }, fe = function (text, rspType, threadid) {
                    var self = this;
                    //for loadSnips resume with error too
                    ood.tryF(self.$cb, [null, null, self.threadid], self);
                };
            //get from object first
            if (force || !(r = ep(s))) {
                //if script in cache
                if (!force && (t = ct[s])) {
                    isAsy = false;
                    f.call({$cb: options.$cb}, t);
                    //delete it
                    delete ct[s];
                }
                //get from object second
                if (force || !(r = ep(s))) {
                    options = options || {};
                    //load from sy ajax
                    if (s.indexOf(".cls") > 0) {
                        o = ood.getPath(s, '.cls', '', options);

                    } else if (s.indexOf(".dyn") > 0) {
                        o = ood.getPath(s, '.dyn', '', options);
                    } else {
                        o = ood.getPath(s, '.jsx', '', options);
                    }

                    options.$tag = s;
                    ood.Class._ignoreNSCache = 1;
                    ood.Class._last = null;
                    var ajax;
                    //asy and not for loadSnips
                    if (isAsy && !options.$p) {
                        options.rspType = "script";
                        ajax = ood.JSONP;
                    } else {
                        options.asy = isAsy;
                        ajax = ood.Ajax;
                    }
                    //get text from sy ajax
                    if (ood.SC.beforeGetScript(o, f, fe) !== false) {
                        ajax(o, ood._rnd(), f, fe, null, options).start();
                    }
                    //for asy once only
                    if (!isAsy)
                        r = ep(s);
                }
            } else if (options.$cb)
                f.call(options);
            return r;
        },
        /*
        arr: key array, ['ood.UI.Button','ood.UI.Input']
        callback: fire this function after all js loaded
        */
        loadSnips: function (pathArr, cache, callback, onEnd, threadid, options, isAsy) {
            if (!pathArr || !pathArr.length) {
                ood.tryF(onEnd, [threadid]);
                return;
            }
            var bak = {}, options = ood.merge(options || {}, {$p: 1, $cache: cache || ood.$cache.snipScript});
            for (var i = 0, l = pathArr.length; i < l; i++)
                bak[pathArr[i]] = 1;

            if (callback || onEnd) {
                options.$cb = function (path) {
                    //give callback call
                    if (callback) ood.tryF(callback, arguments, this);
                    delete bak[path || this.$tag];
                    if (ood.isEmpty(bak)) {
                        ood.tryF(onEnd, [threadid]);
                        onEnd = null;
                        ood.Thread.resume(threadid);
                    }
                };
            }
            ood.Thread.suspend(threadid);
            for (var i = 0, s; s = pathArr[i++];)
                this._call(s, ood.merge({$tag: s}, options, isAsy), true);
        },
        runInBG: function (pathArr, callback, onStart, onEnd) {
            var i = 0, j, t, self = this, fun = function (threadid) {
                while (pathArr.length > i && (t = self.get(j = pathArr[i++]))) ;
                if (!t)
                    self._call(j, {threadid: threadid}, true);
                //set abort function to the next step
                if (pathArr.length < i)
                    ood.Thread.abort(threadid);
                if (pathArr.length == i) i++;
            };
            ood.Thread(null, [fun], 1000, callback, onStart, onEnd, true).start();
        },
        execSnips: function (cache) {
            var i, h = cache || ood.$cache.snipScript;
            for (i in h)
                try {
                    ood.exec(h[i], i)
                } catch (e) {
                    throw e
                }
            h = {};
        },
        //asy load multi js file, whatever Dependencies
        /*
        *1.busy UI
        *3.ood.SC.groupCall some js/class
        *4.resume thread
        *5.ood.SC.loadSnips other js/class
        *6.execute other ..
        *7.free UI
        */
        groupCall: function (pathArr, onEnd, callback, threadid, options, isAsy) {
            if (pathArr) {
                //clear first
                var self = this;
                self.execSnips();
                ood.Thread.suspend(threadid);
                self.loadSnips(pathArr, 0, callback, function () {
                    self.execSnips();
                    ood.tryF(onEnd, [threadid]);
                    onEnd = null;
                    ood.Thread.resume(threadid);
                }, null, options, isAsy);
            } else
                ood.tryF(onEnd, [threadid]);
        }
    }
});

//ood.absBox
ood.Class('ood.absBox', null, {
    Constructor: function () {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        this._nodes = [];
        this.Class = this.constructor;
    },
    Before: function (key) {
        var t = ood.absBox;
        if (t) (t = t.$type)[key.replace('ood.', '')] = t[key] = key;
    },
    Instance: {
        __gc: function () {
            this.each(function (profile) {
                ood.tryF(profile.__gc);
            });
            this._nodes = 0;
        },
        _get: function (index) {
            var t = this._nodes;
            return ood.isNumb(index) ? t[index] : t;
        },
        _empty: function () {
            this._nodes.length = 0;
            return this;
        },
        getProfile: function (all) {
            return all ? this._nodes : this._nodes[0];
        },
        get: function (index) {
            return this._get(index);
        },
        size: function () {
            return this._nodes.length;
        },
        _each: function (fun, scope, desc) {
            var self = this, j = self._nodes, l = j.length, i, n;
            if (desc) {
                for (i = l; i >= 0; i--)
                    if (n = j[i])
                        if (false === fun.call(scope || self, n, i))
                            break;
            } else {
                for (i = 0; i < l; i++)
                    if (n = j[i])
                        if (false === fun.call(scope || self, n, i))
                            break;
            }
            n = null;
            return self;
        },
        each: function (fun, scope, desc) {
            return this._each(fun, scope, desc);
        },
        isEmpty: function () {
            return !this._nodes.length;
        },
        merge: function (obj) {
            if (this == ood.win || this == ood.doc || this == ood('body') || this == ood('html')) return this;
            var self = this, c = self.constructor, obj = obj._nodes, i = 0, t, n = self._nodes;
            if (obj.length) {
                for (; t = obj[i++];) n[n.length] = t;
                self._nodes = c._unique(n);
            }
            return self;
        },
        reBoxing: function (key, ensureValue) {
            var self = this, t = ood.absBox.$type[key || 'Dom'];
            if (!t) return ood.UI.pack([]);
            if (t == self.KEY) return self;
            if (t = ood.SC(t)) return t.pack(self._nodes, ensureValue);
        }
    },
    Static: {
        $abstract: true,
        $type: {},
        pack: function (arr, ensureValue) {
            var o = new this(false);

            o._nodes = !arr
                ? []
                : ensureValue === false
                    ? ood.isArr(arr)
                        ? arr
                        : [arr]
                    : typeof this._ensureValues == 'function'
                        ? this._ensureValues(arr)
                        : ood.isArr(arr)
                            ? arr
                            : [arr];
            o.n0 = o._nodes[0];
            return o;
        },
        _unique: function (arr) {
            var h = {}, a = [], i = 0, l = arr.length, t, k;
            for (; i < l; i++) a[i] = arr[i];
            arr.length = 0;
            i = 0;
            for (; t = a[i++];) {
                k = typeof t == 'string' ? t : t.$xid;
                if (!h[k]) {
                    h[k] = 1;
                    arr.push(t);
                }
            }
            return arr;
        },
        plugIn: function (name, fun) {
            this.prototype[name] = fun;
            return this;
        }
    }
});

ood.Class('ood.absProfile', null, {
    Constructor: function () {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        if (!this.$xid) this.$xid = ood.absProfile.$xid.next();
    },
    Instance: {
        getId: function () {
            return this.$xid;
        },
        getUid: function (ext) {
            return this.key + ":" + this.$xid + (ext ? (":" + ext) : "");
        },
        link: function (obj, id, target, index) {
            return ood.absProfile.prototype.$link(this, obj, id, target, index);
        },
        $link: function (self, obj, id, target, index) {
            var uid = '$' + self.$xid;

            target = target || self;
            if (obj[uid]) self.unLink(id);

            //double link
            obj[uid] = target;
            if (ood.isArr(obj))
                ood.arr.insertAny(obj, target, index, true);

            //antilink track
            self._links[id] = obj;
            return self;
        },
        unLink: function (id) {
            return ood.absProfile.prototype.$unLink(this, id);
        },
        $unLink: function (self, id) {
            var o, index,
                //avoid Number;
                uid = '$' + self.$xid;
            if (!self._links) return;
            if (!(o = self._links[id])) return;

            //remove from target
            if (ood.isArr(o)) {
                index = ood.arr.indexOf(o, o[uid]);
                if (index != -1) {
                    ood.arr.removeFrom(o, index);
                }
            }
            delete o[uid];

            //remove from self
            delete self._links[id];

            return index;
        },
        unLinkAll: function () {
            return ood.absProfile.prototype.$unLinkAll(this);
        },
        $unLinkAll: function (self) {
            var id = '$' + self.$xid,
                l = self._links,
                o, i;
            for (i in l) {
                o = l[i];
                if (ood.isArr(o)) ood.arr.removeValue(o, o[id]);
                delete o[id];
            }
            self._links = {};
            return self;
        },
        getModule: function (top) {
            var prf = this, getUpperModule = function (module) {
                // if it's a inner module
                if (module.moduleClass && module.moduleXid) {
                    var pm = ood.SC.get(module.moduleClass);
                    if (pm && (pm = pm.getInstance(module.moduleXid))) {
                        return getUpperModule(pm);
                    }
                }
                return module;
            }, t;

            if (prf.moduleClass && prf.moduleXid) {
                if (t = ood.SC.get(prf.moduleClass)) {
                    if (t = t.getInstance(prf.moduleXid)) {
                        return top ? getUpperModule(t) : t;
                    }
                }
            }
        },
        getParent: function () {
            return this.parent && this.parent.boxing();
        },
        getChildrenId: function () {
            return this.childrenId;
        }
    },
    Static: {
        $xid: new ood.id,
        $abstract: true
    }
});

ood.Class('ood.Profile', 'ood.absProfile', {
    Constructor: function (host, key, alias, box, properties, events, options) {
        var upper = arguments.callee.upper, args = ood.toArr(arguments);
        upper.apply(this, args);
        upper = null;
        var self = this;
        ood.merge(self, options);

        self.key = key || self.key || '';
        self.alias = alias || self.alias || '',
            self.properties = properties ? ood.copy(properties) : (self.properties || {});
        self.events = events ? ood.copy(events) : (self.events || {});
        self.host = host || self.host || self;
        self.Class = self.constructor;
        self.box = box || self.box;
        if (self.events) {
            self.setEvents(self.events);
            delete self.events;
        }
        self._links = {};
    },
    Instance: {
        setEvents: function (key, value) {
            var evs = this.box.$EventHandlers;
            if (ood.isHash(key)) {
                return ood.merge(this, key, 'all', function (o, i) {
                    return evs[i]
                });
            } else {
                if (evs[key])
                    this[key] = value;
            }
        },
        getEvents: function (key) {
            if (key) {
                return this[key];
            } else {
                var self = this, t, hash = {};
                ood.each(self.box.$EventHandlers, function (o, i) {
                    if (self[i]) hash[i] = self[i];
                });
                return hash;
            }
        },
        getProperties: function (key) {
            var self = this, prop = self.properties;
            if (ood.isFun(self._propGetter)) prop = self._propGetter(prop);
            if (ood.isFun(self.propGetter)) prop = self.propGetter(prop);
            return key ? prop[key] : ood.copy(prop);
        },
        setProperties: function (key, value) {
            var self = this;
            if (ood.isHash(key)) {
                ood.merge(key, self.box.$DataStruct, function (o, i) {
                    if (!(i in key)) {
                        key[i] = ood.isObj(o) ? ood.clone(o) : o;
                    }
                });
                self.properties = key;
                if (ood.isFun(self._propSetAction)) self._propSetAction(key);
                if (ood.isFun(self.propSetAction)) self.propSetAction(key);
            } else
                self.properties[key] = value;
        },
        _applySetAction: function (fun, value, ovalue, force, tag, tag2) {
            return fun.call(this, value, ovalue, force, tag, tag2);
        },
        __gc: function () {
            var ns = this, args = ood.toArr(arguments);
            if (ns.$beforeDestroy) {
                ood.each(ns.$beforeDestroy, function (f) {
                    ood.tryF(f, args, ns);
                });
                delete ns.$beforeDestroy;
            }
            ood.tryF(ns.$ondestory, args, ns);
            if (ns.onDestroy) ns.boxing().onDestroy();
            if (ns.destroyTrigger) ns.destroyTrigger();

            // try to clear parent host
            var o;
            if (ns.alias && ns.host && (o = ns.host[ns.alias]) && (o = o._nodes) && (o.length === 0 || o.length === 1 && o[0] == ns)) {
                delete ns.host[ns.alias];
            }

            ns.unLinkAll();
            ood.tryF(ns.clearCache, [], ns);

            //set once
            ns.destroyed = true;
            //afterDestroy
            if (ns.$afterDestroy) {
                ood.each(ns.$afterDestroy, function (f) {
                    ood.tryF(f, args, ns);
                });
                delete ns.$afterDestroy;
            }
            if (ns.afterDestroy) ns.boxing().afterDestroy(ns);
            ood.breakO([ns.properties, ns.events, ns], 2);
            //set again
            ns.destroyed = true;
        },
        boxing: function () {
            //cache boxing
            var self = this, t;
            //for destroyed UIProfile
            if (!self.box) return null;
            if (!((t = self.Instace) && t.get(0) == self && t._nodes.length == 1))
                t = self.Instace = self.box.pack([self], false);
            return t;
        },
        serialize: function (rtnString, keepHost) {
            var t,
                self = this,
                o = (t = self.box._beforeSerialized) ? t(self) : self,
                r = {
                    alias: o.alias,
                    key: o.key,
                    host: o.host
                };
            //host
            if (r.host === self) {
                delete r.host;
            } else if (o.host && !keepHost) {
                if (rtnString !== false)
                    r.host = '@this';
                else
                    delete r.host;
            }

            //properties
            var c = {}, p = o.box.$DataStruct, map = ood.absObj.$specialChars;
            ood.merge(c, o.properties, function (o, i) {
                return (i in p) && p[i] !== o && !map[i.charAt(0)]
            });
            if (!ood.isEmpty(c)) r.properties = c;

            //events
            if (!ood.isEmpty(t = this.getEvents())) r.events = t;
            var eh = o.box.$EventHandlers;
            ood.filter(r.events, function (o, i) {
                return o != eh[i];
            });
            if (ood.isEmpty(r.events)) delete r.events;
            return rtnString === false ? r : ood.serialize(r);
        }
    }
});

ood.Class('ood.absObj', "ood.absBox", {
    //properties, events, host
    Constructor: function () {
        var upper = arguments.callee.upper, args = ood.toArr(arguments);
        upper.apply(this, args);
        upper = null;
        //for pack function
        if (args[0] !== false && typeof this._ini == 'function')
            return this._ini.apply(this, args);
    },
    Before: function (key, parent_key, o) {
        ood.absBox.$type[key] = key;
        return true;
    },
    After: function () {
        var self = this, me = arguments.callee,
            temp, t, k, u, m, i, j, l, v, n, b;
        self._nameId = 0;
        self._nameTag = self.$nameTag || (self.KEY.replace(/\./g, '_').toLowerCase());
        self._cache = [];
        m = me.a1 || (me.a1 = ood.toArr('$Keys,$DataStruct,$EventHandlers,$DataModel'));
        for (j = 0; v = m[j++];) {
            k = {};
            if ((t = self.$parent) && (i = t.length))
                while (i--)
                    ood.merge(k, t[i][v]);
            self[v] = k;
        }

        self.setDataModel(self.DataModel);
        delete self.DataModel;

        self.setEventHandlers(self.EventHandlers);
        delete self.EventHandlers;

        m = me.a5 || (me.a5 = ood.toArr('RenderTrigger,LayoutTrigger'));
        for (j = 0; v = m[j++];) {
            temp = [];
            if ((t = self.$parent) && (l = t.length))
                for (i = 0; i < l; i++) {
                    u = t[i]
                    if (u = u['$' + v])
                        temp.push.apply(temp, u);
                }
            if (self[v])
                temp.push(self[v]);

            // sort sub node
            ood.arr.stableSort(temp, function (x, y) {
                x = x.$order || 0;
                y = y.$order || 0;
                return x > y ? 1 : x == y ? 0 : -1;
            });

            self['$' + v] = temp;
            delete self[v];
        }
    },
    //don't add any other function or member to absObj
    Static: {
        $abstract: true,
        $specialChars: {_: 1, $: 1},

        // *** non-abstract child must have this
        //_objectProp:{tagVar:1,propBinder:1},
        DataModel: {
            "name": '',
            desc: '',
            tag: '',
            tagVar: {
                ini: {},
                action: function () {
                    var r = this.properties.renderer;
                    if (r && /^\s*[a-zA-Z]+([\w]+\.?)+[\w]+\s*$/.test(r))
                        this.boxing().refresh();
                }
            },
            propBinder: {
                hidden: 1,
                ini: {}
            },
            dataBinder: {
                ini: '',
                set: function (value) {
                    var profile = this,
                        p = profile.properties,
                        ovalue = p.dataBinder;
                    if (ovalue)
                        ood.DataBinder._unBind(ovalue, profile);
                    p.dataBinder = value;
                    ood.DataBinder._bind(value, profile);
                }
            },
            dataField: {
                ini: ''
            }
        },
        get: function (index) {
            return this.pack([this._cache[index || 0]]);
        },
        getAll: function () {
            return this.pack(this._cache);
        },
        pickAlias: function () {
            return ood.absObj.$pickAlias(this);
        },
        $pickAlias: function (cls) {
            var a = cls._nameTag, p = cls._cache, t;
            while (t = (a + (++cls._nameId))) {
                for (var i = 0, l = p.length; i < l; i++) {
                    if (p[i].alias === t) {
                        t = -1;
                        break;
                    }
                }
                if (t == -1) continue;
                else return t;
            }
        },
        setDataModel: function (hash) {
            var self = this,
                sc = ood.absObj.$specialChars,
                ds = self.$DataStruct,
                dm = self.$DataModel,
                ps = self.prototype,
                i, j, t, o, n, m, r;

            //merge default value and properties
            for (i in hash) {
                if (!dm[i]) dm[i] = {};
                o = hash[i];
                if (null === o || undefined === o) {
                    r = ood.str.initial(i);
                    delete ds[i];
                    delete dm[i]
                    if (ps[j = 'get' + r] && ps[j].$auto$) delete ps[j];
                    if (ps[j = 'set' + r] && ps[j].$auto$) delete ps[j];
                    //Here, if $DataModel inherites from it's parent class, properties[i] will pointer to parent's object.
                } else {
                    t = typeof o;
                    if (t != 'object' || o.constructor != Object)
                        o = {ini: o};
                    ds[i] = ('ini' in o) ? o.ini : (i in ds) ? ds[i] : '';

                    t = dm[i];
                    for (j in t)
                        if (!(j in o))
                            o[j] = t[j];
                    dm[i] = o;
                }
            }

            ood.each(hash, function (o, i) {
                if (null === o || undefined === o || sc[i.charAt(0)]) return;
                r = ood.str.initial(i);
                n = 'set' + r;
                //readonly properties
                if (o.set !== null && !(o && (o.readonly || o.inner))) {
                    //custom set
                    var $set = o.set;
                    m = ps[n];
                    ps[n] = (typeof $set != 'function' && typeof m == 'function') ? m : ood.Class._fun(function (value, force, tag, tag2) {
                        return this.each(function (v) {
                            if (!v.properties) return;

                            var t, nfz;
                            // *** force to em/px
                            if (!force) {
                                if (dm[i] && dm[i]['$spaceunit']) {
                                    if (v.$forceu && value != 'auto') {
                                        t = ood.$us(v);
                                        value = v.$forceu(value, t == 2 ? 'em' : t == -2 ? 'px' : null);
                                    }
                                }
                            }
                            //if same return
                            if (v.properties[i] === value && !force) return;

                            if (v.$beforePropSet && false === v.$beforePropSet(i, value, force, tag, tag2)) {
                                return;
                            } else {
                                var ovalue = v.properties[i];
                                if (v.beforePropertyChanged && false === v.boxing().beforePropertyChanged(v, i, value, ovalue))
                                    return;

                                if (typeof $set == 'function') {
                                    $set.call(v, value, force, tag, tag2);
                                } else {
                                    var m = ood.get(v.box.$DataModel, [i, 'action']);
                                    v.properties[i] = value;
                                    if (typeof m == 'function' && v._applySetAction(m, value, ovalue, force, tag, tag2) === false)
                                        v.properties[i] = ovalue;
                                }

                                if (v.afterPropertyChanged) v.boxing().afterPropertyChanged(v, i, value, ovalue);
                                if (v.$afterPropertyChanged) ood.tryF(v.$afterPropertyChanged, [v, i, value, ovalue], v);
                            }
                        });
                    }, n, self.KEY, null, 'instance');
                    //delete o.set;
                    if (ps[n] !== m) ps[n].$auto$ = 1;
                } else
                    delete ps[n];
                n = 'get' + r;
                if (!(o && o.inner)) {
                    // get custom getter
                    var $get = o.get;
                    m = ps[n];
                    ps[n] = (typeof $get != 'function' && typeof m == 'function') ? m : ood.Class._fun(function () {
                        if (typeof $get == 'function')
                            return $get.apply(this.get(0), arguments);
                        else
                            return this.get(0).properties[i];
                    }, n, self.KEY, null, 'instance');
                    //delete o.get;
                    if (ps[n] !== m) ps[n].$auto$ = 1;
                } else
                    delete ps[n];
            });
            return self;
        },
        setEventHandlers: function (hash) {
            var self = this;
            ood.each(hash, function (o, i) {
                if (null === o) {
                    delete self.$EventHandlers[i];
                    delete self.prototype[i];
                } else {
                    self.$EventHandlers[i] = o;
                    var f = function (fun) {
                        var l = arguments.length, j;
                        if (l == 1 && (typeof fun == 'function' || typeof fun == 'string' || ood.isHash(fun) || ood.isArr(fun)))
                            return this.each(function (v) {
                                if (v.renderId)
                                    v.clearCache();
                                if (v.box._addEventHanlder) v.box._addEventHanlder(v, i, fun);
                                v[i] = fun;
                            });
                        else if (l == 1 && null === fun)
                            return this.each(function (v) {
                                v.clearCache();
                                if (v.box._removeEventHanlder) v.box._removeEventHanlder(v, i, v[i]);
                                delete v[i];
                            });
                        else {
                            var args = [], prf = this.get(0);
                            if (prf) {
                                var events = prf[i], host = prf.host || prf;
                                if (events && (!ood.isArr(events) || events.length)) {
                                    if (prf.$inDesign) return;
                                    prf.$lastEvent = i;
                                    if (arguments[0] != prf) args[0] = prf;
                                    for (j = 0; j < l; j++) args[args.length] = arguments[j];
                                    if (ood.isStr(events) || ood.isFun(events)) events = [events];
                                    if (ood.isArr(events.actions || events) && (events.actions && ood.isArr(events.actions) && events.actions.length > 0) && ood.isNumb(j = (events.actions || events)[0].event)) args[j] = args[j] ? ood.Event.getEventPara(args[j]) : {};

                                    return ood.pseudocode._callFunctions(events, args, host, null, prf.$holder, ((host && host.alias) || (prf.$holder && prf.$holder.alias)) + "." + prf.alias + "." + i);
                                }
                            }
                        }
                    };
                    f.$event$ = 1;
                    f.$original$ = o.$original$ || self.KEY;
                    f.$name$ = i;
                    f.$type$ = 'event';
                    self.plugIn(i, f);
                }
            });
            return self;
        },
        unserialize: function (target, keepSerialId) {
            if (typeof target == 'string') target = ood.unserialize(target);
            var f = function (o) {
                if (ood.isArr(o)) o = o[0];
                delete o.serialId;
                if (o.children) ood.arr.each(o.children, f);
            }, a = [];
            ood.arr.each(target, function (o) {
                if (!keepSerialId) f(o);
                a.push((new (ood.SC(o.key))(o)).get(0));
            });
            return this.pack(a, false);
        }
    },
    Instance: {
        clone: function () {
            var arr = [], clrItems = arguments, f = function (p) {
                //remove those
                delete p.alias;
                for (var i = 0; i < clrItems.length; i++)
                    delete p[clrItems[i]];
                if (p.children)
                    for (var i = 0, c; c = p.children[i]; i++)
                        f(c[0]);
            };
            this.each(function (o) {
                o = o.serialize(false, true);
                f(o);
                arr.push(o);
            });
            return this.constructor.unserialize(arr);
        },
        serialize: function (rtnString, keepHost) {
            var a = [];
            this.each(function (o) {
                a[a.length] = o.serialize(false, keepHost);
            });
            return rtnString === false ? a : a.length == 1 ? " new " + a[0].key + "(" + ood.serialize(a[0]) + ")" : "ood.UI.unserialize(" + ood.serialize(a) + ")";
        },
        getProperties: function (key) {
            var h = {}, prf = this.get(0), prop = prf.properties, funName;
            if (key === true)
                return ood.copy(prop);
            else if (typeof key == 'string')
                return prop[key];
            else {
                for (var k in prop) {
                    funName = "get" + ood.str.initial(k);
                    if (typeof this[funName] == 'function')
                        h[k] = this[funName].call(this);
                }
                return h;
            }
        },
        setProperties: function (key, value, force) {
            if (typeof key == "string") {
                var h = {};
                h[key] = value;
                key = h;
            }
            return this.each(function (o) {
                ood.each(key, function (v, k) {
                    var funName = "set" + ood.str.initial(k), ins = o.boxing();
                    if (ins && typeof ins[funName] == 'function') {
                        ins[funName].call(ins, v, !!force);
                    }
                    // can set hidden prop here
                    else {
                        o.properties[k] = v;
                    }
                });
            });
        },
        getEvents: function (key) {
            return this.get(0).getEvents(key);
        },
        setEvents: function (key, value) {
            if (typeof key == "string") {
                var h = {};
                h[key] = value;
                key = h;
            }
            return this.each(function (o) {
                var ins = o.boxing();
                ood.each(key, function (v, k) {
                    if (typeof ins[k] == 'function')
                        ins[k].call(ins, v);
                });
            });
        },
        alias: function (value) {
            return value ? this.setAlias(value) : this.getAlias();
        },
        host: function (value, alias) {
            return value ? this.setHost(value, alias) : this.getHost();
        },
        setHost: function (host, alias) {
            return this._setHostAlias(host, alias);
        },
        _setHostAlias: function (host, alias) {
            var self = this,
                prf = this.get(0),
                oldAlias = prf.alias;

            alias = alias || prf.alias;

            if (oldAlias) {
                if (prf.host && prf.host !== prf) {
                    try {
                        delete prf.host[oldAlias]
                    } catch (e) {
                        prf.host[oldAlias] = undefined
                    }
                    if (prf.host._ctrlpool)
                        delete prf.host._ctrlpool[oldAlias];
                }
            }
            prf.alias = alias;
            if (prf.box && prf.box._syncAlias) {
                prf.box._syncAlias(prf, oldAlias, alias);
            }

            if (host) prf.host = host;
            if (prf.host && prf.host !== prf) {
                prf.host[alias] = self;
                if (prf.host._ctrlpool)
                    prf.host._ctrlpool[alias] = self.get(0);
            }
            return self;
        },
        setAlias: function (alias) {
            return this._setHostAlias(null, alias);
        },
        getAlias: function () {
            return this.get(0).alias;
        },
        getHost: function () {
            return this.get(0).host;
        },
        reBindProp: function (dataMap, scope_set, scope_clear, _scope_handled) {
            if (!_scope_handled) {
                scope_set = scope_set || ood._scope_set;
                scope_clear = scope_clear || ood._scope_clear;
            }

            var ns = this, prop, ins, fn, r;
            try {
                if (!_scope_handled) scope_set.call(this, dataMap);
                ns.each(function (prf) {
                    prop = prf.properties;
                    if (prop.propBinder && !ood.isEmpty(prop.propBinder)) {
                        ins = prf.boxing();
                        ood.each(prop.propBinder, function (get_prop_value, key) {
                            if (ood.isDefined(r = ood.isFun(get_prop_value) ? get_prop_value(prf) : ood.adjustVar(get_prop_value))) {
                                if (false !== ood.tryF(ins._reBindProp, [prf, r, key, get_prop_value], ins)) {
                                    switch (key) {
                                        case "CA":
                                            ins.setCustomAttr(r);
                                            break;
                                        case "CC":
                                            ins.setCustomClass(r);
                                            break;
                                        case "CS":
                                            ins.setCustomStyle(r);
                                            break;
                                        default:
                                            if (ood.isFun(ins[fn = 'set' + ood.str.initial(key)])) ins[fn](r, true);
                                    }
                                }
                            }
                        });
                    }
                });
            } catch (e) {
                if (!_scope_handled) scope_clear.call(this);
            }


            return this;
        }
        /*non-abstract inheritance must have those functions:*/
        //1. destroy:function(){this.get(0).__gc();}
        //2. _ini(properties, events, host, .....){/*set _nodes with profile*/return this;}
        //3. render(){return this}
    }
});

ood.Class("ood.Timer", "ood.absObj", {
    Instance: {
        _ini: function (properties, events, host) {
            var self = this,
                c = self.constructor,
                profile,
                options,
                alias, temp;
            if (properties && properties['ood.Profile']) {
                profile = properties;
                alias = profile.alias || c.pickAlias();
            } else {
                if (properties && properties.key && ood.absBox.$type[properties.key]) {
                    options = properties;
                    properties = null;
                    alias = options.alias || c.pickAlias();
                } else
                    alias = c.pickAlias();
                profile = new ood.Profile(host, self.$key, alias, c, properties, events, options);
            }
            profile._n = profile._n || [];

            for (var i in (temp = c.$DataStruct))
                if (!(i in profile.properties))
                    profile.properties[i] = typeof temp[i] == 'object' ? ood.copy(temp[i]) : temp[i];

            //set anti-links
            profile.link(c._cache, 'self').link(ood._pool, 'ood');

            self._nodes.push(profile);
            profile.Instace = self;
            self.n0 = profile;

            if (self._after_ini) self._after_ini(profile, alias);
            return self;
        },
        _after_ini: function (profile) {
            if (profile.$inDesign) return;
            ood.asyRun(function () {
                if (profile && profile.box && profile.properties.autoStart) profile.boxing().start();
            });
        },
        destroy: function () {
            this.each(function (profile) {
                if (profile._threadid) ood.Thread.abort(profile._threadid);
                //free profile
                profile.__gc();
            });
        },
        start: function () {
            return this.each(function (profile) {
                if (profile.$inDesign) return;
                if (profile._threadid) {
                    ood.Thread.resume(profile._threadid);
                } else {
                    var p = profile.properties, box = profile.boxing(),
                        t = ood.Thread.repeat(function (threadId) {
                            if (profile.$onTime && false === profile.$onTime(profile, threadId)) return false;
                            if (profile.onTime && false === box.onTime(profile, threadId)) return false;
                        }, p.interval, function (threadId) {
                            profile.onStart && box.onStart(profile, threadId);
                        }, function (threadId) {
                            profile.onEnd && box.onEnd(profile, threadId);
                        });
                    profile._threadid = t.id;
                }
            });
        },
        suspend: function () {
            return this.each(function (profile) {
                if (profile._threadid) ood.Thread.suspend(profile._threadid);
                profile.onSuspend && box.onSuspend(profile, threadId);
            });
        },
        getParent: function () {
            return this.parent && this.parent.boxing();
        },
        getChildrenId: function () {
            return this.childrenId;
        }
    },
    Static: {
        _objectProp: {tagVar: 1, propBinder: 1},
        _beforeSerialized: function (profile) {
            var o = {};
            ood.merge(o, profile, 'all');
            var p = o.properties = ood.clone(profile.properties, true);
            if (profile.box._objectProp) {
                for (var i in profile.box._objectProp)
                    if ((i in p) && p[i] && ood.isHash(p[i]) && ood.isEmpty(p[i])) delete p[i];
            }
            return o;
        },
        DataModel: {
            autoStart: true,
            "interval": 1000
        },
        EventHandlers: {
            // return false will stop the Timer
            onTime: function (profile, threadId) {
            },
            onStart: function (profile, threadId) {
            },
            onSuspend: function (profile, threadId) {
            },
            onEnd: function (profile, threadId) {
            }
        }
    }
});

ood.Class("ood.MessageService", "ood.absObj", {
    Instance: {
        _ini: ood.Timer.prototype._ini,
        _after_ini: function (profile) {
            if (profile.$inDesign) return;
            var t, p = profile.properties;
            if (t = p.recipientType || p.msgType) profile.boxing().setRecipientType(t, true);
        },
        destroy: function () {
            this.each(function (profile) {
                if (profile.$inDesign) return;
                //** unsubscribe
                var t, id = profile.$xid;
                if (t = profile.properties.msgType) {
                    ood.arr.each(t.split(/[\s,;:]+/), function (t) {
                        ood.unsubscribe(t, id);
                    });
                }
                //free profile
                profile.__gc();
            });
        },
        broadcast: function (recipientType, msg1, msg2, msg3, msg4, msg5, readReceipt) {
            return this.each(function (profile) {
                var ins = profile.boxing();
                ood.arr.each(recipientType.split(/[\s,;:]+/), function (t) {
                    ood.publish(t, [msg1, msg2, msg3, msg4, msg5, function () {
                        ood.tryF(readReceipt);
                        if (profile.onReceipt) ins.onReceipt.apply(ins, [profile, t, ood.toArr(arguments)]);
                    }], null, ins);
                });
            });
        },
        getParent: ood.Timer.prototype.getParent,
        getChildrenId: ood.Timer.prototype.getChildrenId
    },
    Static: {
        _objectProp: ood.Timer._objectProp,
        _beforeSerialized: ood.Timer._beforeSerialized,
        DataModel: {
            dataBinder: null,
            dataField: null,
//兼容旧版本
            msgType: {
                ini: "",
                set: function (value) {
                    var profile = this, t, p = profile.properties, id = profile.$xid;
                    if (t = p.msgType) {
                        ood.arr.each(t.split(/[\s,;:]+/), function (t) {
                            ood.unsubscribe(t, id);
                        });
                    }
                    if (t = p.msgType = value || "") {
                        ood.arr.each(t.split(/[\s,;:]+/), function (t) {
                            ood.subscribe(t, id, function () {
                                var a = ood.toArr(arguments), ins = profile.boxing();
                                a.unshift(profile);
                                if (profile.onMessageReceived) ins.onMessageReceived.apply(ins, a);
                            }, p.asynReceive);
                        });
                    }
                }
            },


            recipientType: {
                ini: "",
                set: function (value) {
                    var profile = this, t, p = profile.properties, id = profile.$xid;
                    if (t = p.recipientType) {
                        ood.arr.each(t.split(/[\s,;:]+/), function (t) {
                            ood.unsubscribe(t, id);
                        });
                    }
                    if (t = p.recipientType = value || "") {
                        ood.arr.each(t.split(/[\s,;:]+/), function (t) {
                            ood.subscribe(t, id, function () {
                                var a = ood.toArr(arguments), ins = profile.boxing();
                                a.unshift(profile);
                                if (profile.onMessageReceived) ins.onMessageReceived.apply(ins, a);
                            }, p.asynReceive);
                        });
                    }
                }
            },
            asynReceive: false
        },
        EventHandlers: {
            onMessageReceived: function (profile, msg1, msg2, msg3, msg4, msg5, readReceipt) {
            },
            onReceipt: function (profile, recipientType, args) {
            }
        }
    }
});

/*** ood.ExcelFormula.calculate
 * formula :
 *      "=FIXED(SUM(1:1, AVERAGE(A:A, B3)) + ROUND(B5)*C6 + MAX(A1:B2, B3) + MIN(10, B3)/ 1000, 2)"
 *      "=FIXED(SUM(1, AVERAGE(1, 3)) + ROUND(3.3)*1 + MAX(4, 2) + MIN(10, 5)/ 3, 2)" => 11.67
 *      "=CHOOSE(2,'a','b','c')" => 'b'
 * cellsMap :
 *      true: force to return something without cell value maps
 *      {}: returns the result of the formula with cell value maps
 ***/
ood.Class("ood.ExcelFormula", null, {
    Static: {
        MAXCOUNT: 256,
        // support functions: +,-,*,/,%,SUM, AVERAGE, MIN, MAX, ROUND, FIXED, CHOOSE
        Supported: (function () {
            var flatten = function (args) {
                var arr = [], t, args = ood.toArr(args), i = 0, l = args.length;
                for (; i < l; i++) {
                    if (ood.isArr(t = args[i])) arr = arr.concat(t);
                    else arr.push(t);
                }
                return arr;
            };
            return {
                SUM: function () {
                    var result = 0, arr = flatten(arguments), i = 0, l = arr.length, v, parsed;
                    for (; i < l; ++i) {
                        v = arr[i];
                        if (typeof v === 'number') {
                            result += v;
                        } else if (typeof v === 'string') {
                            parsed = parseFloat(v);
                            if (!ood.isNaN(parsed))
                                result += parsed;
                        }
                    }
                    return result;
                },
                AVERAGE: function () {
                    var result = 0, arr = flatten(arguments), i = 0, l = arr.length, v, parsed;
                    for (; i < l; ++i) {
                        v = arr[i];
                        if (typeof v === 'number') {
                            result += v;
                        } else if (typeof v === 'string') {
                            parsed = parseFloat(v);
                            if (!ood.isNaN(parsed))
                                result += parsed;
                        }
                    }
                    return result / l;
                },
                COUNT: function () {
                    var result = 0, arr = flatten(arguments), i = 0, l = arr.length, v;
                    for (; i < l; ++i) {
                        v = typeof(arr[i]);
                        if (v === 'string' || v === 'number') result++;
                    }
                    return result;
                },
                MIN: function () {
                    return Math.min.apply(Math, flatten(arguments));
                },
                MAX: function () {
                    return Math.max.apply(Math, flatten(arguments));
                },
                ROUND: function () {
                    return Math.round.apply(Math, arguments);
                },
                FIXED: function () {
                    return ood.toFixedNumber.apply(ood, arguments);
                },
                CHOOSE: function () {
                    var a = arguments;
                    return (ood.isNumb(a[0]) && (a[a[0]])) || '';
                },
                CONCATENATE: function () {
                    return flatten(arguments).join('')
                },
                ABS: function (a) {
                    return Math.abs(a)
                },
                ISNUMBER: function (v) {
                    return ood.isFinite(v)
                },
                NOW: function () {
                    return new Date
                },
                TODAY: function () {
                    return ood.Date.getTimSpanStart(new Date, 'DAY')
                },
                IF: function (a, b, c) {
                    return eval(a) ? b : c
                },
                AND: function () {
                    return !!eval(ood.toArr(arguments).join("&&"))
                },
                OR: function () {
                    return !!eval(ood.toArr(arguments).join("||"))
                },
                NOT: function (a) {
                    return !a
                }
            };
        })(),
        toColumnChr: function (num) {
            var s = "";
            num = num - 1;
            while (num >= 0) {
                s = String.fromCharCode(num % 26 + 97) + s;
                num = Math.floor(num / 26) - 1;
            }
            return s.toUpperCase();
        },
        toColumnNum: function (chr) {
            chr = chr.split('');
            var base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(''),
                i = 0, j = chr.length - 1, result = 0;

            for (; i < chr.length; i += 1, j -= 1) {
                result += Math.pow(base.length, j) * (base.indexOf(chr[i]) + 1);
            }
            return result;
        },
        toCoordinate: function (cell, offset, rtnArr) {
            var alpha = /[A-Z]+/,
                num = /[0-9]+/,
                cellU = cell.toUpperCase(), row, col;
            if (!offset && offset !== 0) offset = -1;
            row = parseInt(cellU.match(num)[0], 10) + offset;
            col = this.toColumnNum(cellU.match(alpha)[0]) + offset;
            return rtnArr ? [row, col] : {col: col, row: row};
        },
        toCellId: function (col, row, offset) {
            return this.toColumnChr(col + (offset || 1)) + (row + (offset || 1));
        },
        getCellRanges: function (cellFrom, cellEnd, colLimited, rowLimited) {
            var ns = this,
                alpha = /[A-Z]+/,
                num = /[0-9]+/;

            if (!alpha.test(cellFrom)) cellFrom = "A" + cellFrom;
            if (!num.test(cellFrom)) cellFrom = cellFrom + "1";
            if (!alpha.test(cellEnd)) cellEnd = ns.toColumnChr(colLimited || ns.MAXCOUNT) + cellEnd;
            if (!num.test(cellEnd)) cellEnd = cellEnd + (rowLimited || ns.MAXCOUNT);

            var cellStart = ns.toCoordinate(cellFrom, 0),
                cellStop = ns.toCoordinate(cellEnd, 0),
                colStart = cellStart.col,
                colStop = cellStop.col,
                rowStart = cellStart.row,
                rowStop = cellStop.row,
                cellRange = [],
                row,
                col;

            if (colStart < colStop) {
                for (col = colStart; col <= colStop; col++) {
                    if (rowStart < rowStop) {
                        for (row = rowStart; row <= rowStop; row++) {
                            cellRange.push(ns.toColumnChr(col) + row);
                        }
                    } else {
                        for (row = rowStart; row >= rowStop; row--) {
                            cellRange.push(ns.toColumnChr(col) + row);
                        }
                    }
                }
            } else {
                for (col = colStart; col >= colStop; col--) {
                    if (rowStart < rowStop) {
                        for (row = rowStart; row <= rowStop; row++) {
                            cellRange.push(ns.toColumnChr(col) + row);
                        }
                    } else {
                        for (row = rowStart; row >= rowStop; row--) {
                            cellRange.push(ns.toColumnChr(col) + row);
                        }
                    }
                }
            }
            return cellRange;
        },
        validate: function (formula) {
            var str;
            if (ood.isFun(formula)) str = formula + '';
            else {
                if (!/^\s*\=\s*/.test(formula))
                    return false;
                str = formula.replace(/^\s*\=\s*/, '');
            }
            // for col/row fomula in the grid
            str = str.replace(/(\b)([\?_])([0-9]+\b)/g, '1').replace(/(\b[A-Z]+)([\?_])(\b)/g, '1');

            if (/function\s*\(/.test(str)) {
                try {
                    str = ood.fun.body(str);
                    new Function("", str);
                } catch (e) {
                    ood._debugInfo("throw", "#VALUE! ", formula, str, e);
                    return false;
                }
            } else {
                var fake = function () {
                        return 1;
                    },
                    reg = new RegExp(ood.toArr(this.Supported, true).join('|'), 'g');
                str = ood.replace(str, [
                    // protect "" and ''
                    [/"(\\.|[^"\\])*"/, '1'],
                    [/'(\\.|[^'\\])*'/, '1'],
                    // replace cells
                    [/\{[^}]+\}/, '1'],
                    [/([A-Z\d]+\s*\:\s*[A-Z\d]+)/, '1'],
                    [/([A-Z]+[\d]+)/, '1'],
                    // replace expressions
                    [/[=<>]+/g, function (a) {
                        return a[0] == '=' ? '==' : a[0] == '<>' ? '!=' : a[0]
                    }]
                ]);
                if (/[A-Z_$]/.test(str.replace(reg, '')))
                    return false;
                str = str.replace(reg, 'fake');
                try {
                    eval(str);
                } catch (e) {
                    ood._debugInfo("throw", "#VALUE! ", formula, str, e);
                    return false;
                }
            }
            return true;
        },
        getRefCells: function (formula, colLimited, rowLimited) {
            return this._parse(formula, false, colLimited, rowLimited);
        },
        parse: function (formula) {
            return this._parse(formula, null);
        },
        calculate: function (formula, cellsMap, colLimited, rowLimited) {
            return this._parse(formula, cellsMap || true, colLimited, rowLimited);
        },
        _parse: function (formula, cellsMap, colLimited, rowLimited) {
            var ret, ns = this,
                Supported = ns.Supported,
                RANGE = function (cellsMap, cellStart, cellStop) {
                    var arr = ns.getCellRanges(cellStart, cellStop, colLimited, rowLimited), i = 0, l = arr.length;
                    for (; i < l; i++)
                        arr[i] = cellsMap[arr[i]];
                    return arr;
                },
                doParse = function (formula, CELLS) {
                    var cellHash, rtn, str = formula,
                        f = function (a) {
                            if (a[8]) {
                                if (cellHash) {
                                    if (!(a[8] in cellHash)) cellHash[a[8]] = 1;//ns.toCoordinate(a[8],-1);
                                }
                                return 'CELLS["' + a[8] + '"]';
                            } else if (a[6] && a[7]) {
                                if (cellHash) {
                                    var arr = ns.getCellRanges(a[6], a[7], colLimited, rowLimited);
                                    for (var i = 0, l = arr.length; i < l; i++)
                                        if (!(arr[i] in cellHash)) cellHash[arr[i]] = 1;//ns.toCoordinate(arr[i],-1);
                                }
                                return 'RANGE(CELLS, "' + a[6] + '","' + a[7] + '")';
                            } else if (a[10] && a[11]) {
                                return 'Supported["' + a[10] + '"]' + a[11];
                            }
                        };
                    cellHash = {};
                    if (!ns.validate(str))
                        return false;
                    if (ood.isFun(str)) str = str + '';
                    else str = str.replace(/^\s*\=\s*/, '');
                    if (/function\s*\(/.test(str)) {
                        str = ood.fun.body(str);
                        str = ood.replace(str, [
                            // protect all
                            [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//, '$0'],
                            [/\/\/[^\n]*/, '$0'],
                            [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                            [/"(\\.|[^"\\])*"/, '$0'],
                            [/'(\\.|[^'\\])*'/, '$0'],
                            // replace cells
                            [/\b([A-Z]+[\d]+)\b/, function (a) {
                                cellHash[a[0]] = 1;
                                return a[0];
                            }]
                        ]);
                        try {
                            if (cellsMap === false) {
                                rtn = cellHash;
                            } else {
                                if (cellsMap === true) cellsMap = {};

                                var pre = "var map=arguments[0]";
                                ood.each(cellHash, function (o, i) {
                                    pre += ", \n";
                                    pre += i + " = map['" + i + "']"
                                });
                                str = pre + ";\n" + str;

                                rtn = ood.isHash(cellsMap) ? (new Function("", str)).call(null, CELLS, formula) : str;
                            }
                        } catch (e) {
                            ood._debugInfo("throw", "#VALUE! ", formula, str, e);
                        } finally {
                            return rtn;
                        }
                    } else {
                        str = ood.replace(str, [
                            // protect "" and ''
                            [/"(\\.|[^"\\])*"/, '$0'],
                            [/'(\\.|[^'\\])*'/, '$0'],
                            // replace cells
                            [/([A-Z\d]+)\s*\:\s*([A-Z\d]+)/, f],
                            [/[A-Z]+[\d]+/, f],
                            [/([A-Z]+)(\s*\()/, f],
                            // replace expressions
                            [/[=<>]+/g, function (a) {
                                return a[0] == '=' ? '==' : a[0] == '<>' ? '!=' : a[0]
                            }]
                        ]);
                        try {
                            if (cellsMap === false) {
                                rtn = cellHash;
                            } else {
                                if (cellsMap === true) cellsMap = {};
                                rtn = ood.isHash(cellsMap) ? eval(str) : str;
                            }
                        } catch (e) {
                            ood._debugInfo("throw", "#VALUE! ", formula, str, e);
                        } finally {
                            return rtn;
                        }
                    }
                };

            ret = doParse(formula, cellsMap);

            return ood.isNaN(ret) ? false : ret;
        }
    }
});


// Add filter method to Array.prototype for direct calling
if (!Array.prototype.forEach || !Array.prototype.forEach.call || typeof Array.prototype.forEach !== 'function') {
    Array.prototype.forEach = function (fn) {
        var results = [];
        for (var i = 0, l = this.length; i < l; i++) {
            if (fn(this[i], i, this)) {
                results.push(this[i]);
            }
        }
        // Ensure results can chain call css method
        if (typeof ood !== 'undefined') {
            var wrapped = ood(results);
            // If wrapped object doesn't have css method, add it
            if (!wrapped.css && typeof wrapped.style === 'function') {
                wrapped.css = wrapped.style;
            }
            return wrapped;
        }
        return results;
    };
} else if (typeof ood !== 'undefined') {
    // Enhance existing filter method to support chaining with css
    var originalFilter = Array.prototype.filter;
    Array.prototype.filter = function (fn) {
        var results = originalFilter.call(this, fn);
        // Wrap results with ood to support css method chaining
        var wrapped = ood(results);
        // Ensure css method is available
        if (!wrapped.css && typeof wrapped.style === 'function') {
            wrapped.css = wrapped.style;
        }
        return wrapped;
    };
}