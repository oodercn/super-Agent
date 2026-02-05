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
        path: '/ood/',
        themesPath: '/ood/appearance/',
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
    if (!ini.path) ini.path = ini.appPath + '/ood/';
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
}/**
 * APICaller - 用于处理API请求的核心模块
 * 
 * 功能：
 * - 简化API请求的发起和响应处理
 * - 支持多种请求类型和响应类型
 * - 提供请求生命周期管理
 * - 支持请求队列和线程管理
 * - 提供数据绑定和事件触发机制
 * 
 * 主要方法：
 * - setQueryData: 设置请求数据
 * - start: 启动请求（简化版）
 * - invoke: 执行请求（完整版）
 * - setHost: 设置宿主对象
 * - destroy: 销毁对象，释放资源
 */
ood.Class("ood.APICaller", "ood.absObj", {
    Instance: {
        confirm: false,
        _ini: ood.Timer.prototype._ini,
        
        /**
         * 初始化后的回调方法
         * @param {Object} profile - 配置文件
         * @param {Object} ins - 实例
         * @param {string} alias - 别名
         */
        _after_ini: function (profile, ins, alias) {
            if (!profile.name) profile.Instace.setName(alias);
        },
        
        /**
         * 销毁对象，释放资源
         */
        destroy: function () {
            this.each(function (profile) {
                var box = profile.box, name = profile.properties.name;
                //delete from pool
                delete box._pool[name];
                //free profile
                profile.__gc();
            });
        },
        
        /**
         * 设置宿主对象
         * @param {Object} value - 宿主对象
         * @param {string} alias - 别名
         * @returns {Object} 当前对象
         */
        setHost: function (value, alias) {
            var self = this;
            if (value && alias)
                self.setName(alias);
            return arguments.callee.upper.apply(self, arguments);
        },

        /**
         * 设置请求数据
         * @param {Object} data - 请求数据
         * @param {string} path - 数据路径（可选）
         * @returns {Object} 当前对象
         */
        setQueryData: function (data, path) {
            return this.each(function (prf) {
                if (path) ood.set(prf.properties.queryArgs, (path || "").split("."), data);
                else prf.properties.queryArgs = data || {};
            });
        },

        /**
         * 启动请求（简化版）
         * @param {Object} data - 请求数据
         * @param {Function} onSuccess - 成功回调
         * @param {Function} onFail - 失败回调
         * @param {Function} onStart - 开始回调
         * @param {Function} onEnd - 结束回调
         * @param {string} mode - 请求模式
         * @param {string} threadid - 线程ID
         * @param {Object} options - 选项
         */
        start: function (data, onSuccess, onFail, onStart, onEnd, mode, threadid, options) {
            this.setQueryData(data);
            this.invoke(onSuccess, onFail, onStart, onEnd, mode, threadid, options);
        },

        /**
         * 执行请求（完整版）
         * @param {Function} onSuccess - 成功回调
         * @param {Function} onFail - 失败回调
         * @param {Function} onStart - 开始回调
         * @param {Function} onEnd - 结束回调
         * @param {string} mode - 请求模式
         * @param {string} threadid - 线程ID
         * @param {Object} options - 选项
         */
        invoke: function (onSuccess, onFail, onStart, onEnd, mode, threadid, options) {
            var ns = this, nns = ns,
                con = ns.constructor,
                prf = ns.get(0),

                prop = prf.properties;

            var responseType = prop.responseType,
                requestType = prop.requestType,
                requestId = prop.requestId,
                checkValid = prop.checkValid,
                checkRequired = prop.checkRequired,
                isAllform = prop.isAllform,
                queryURL = prop.queryURL,
                proxyType = prop.proxyType.toLowerCase(),
                queryUserName = prop.queryUserName,
                queryPasswrod = prop.queryPasswrod,
                queryArgs = ood.clone(prop.queryArgs),
                oAuth2Token = prop.oAuth2Token,
                queryOptions = ood.clone(prop.queryOptions),
                queryHeader = ood.clone(prop.queryHeader),
                requestDataSource = prop.requestDataSource,
                responseDataTarget = prop.responseDataTarget,
                responseCallback = prop.responseCallback,
                funs = ood.$cache.functions,
                t1 = funs['$APICaller:beforeInvoke'],
                t2 = funs['$APICaller:beforeData'],
                t3 = funs['$APICaller:onError'];

            queryURL = ood.adjustVar(queryURL);

            if (proxyType == "sajax") proxyType = "jsonp";
            else if (proxyType == "iajax") proxyType = "xdmi";
            if (requestType == "FORM" || requestType == "JSON") queryArgs = typeof queryArgs == 'string' ? ood.unserialize(queryArgs) : queryArgs;
            if (!queryArgs) queryArgs = {};
            if (prop.avoidCache) {
                var i = 0, rnd = "_rand_";
                while (queryArgs.hasOwnProperty(rnd)) rnd = "_rand_" + ++i;
                queryArgs[rnd] = ood.rand();
            }
            queryArgs['_currClassName_'] = prf.host.key || prop.currClassName;
            if (window.handleId) {
                queryArgs['handleId'] = window.handleId;
            }
            if (prf.initAjax && false === prf.boxing().initAjax(prf, requestId))
                return;
            // merge request data
            if (requestDataSource && requestDataSource.length) {

                for (var i in requestDataSource) {
                    var o = requestDataSource[i], t, v, path;
                    switch (o.type.toUpperCase()) {
                        case "FORM":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.absContainer']) {
                                if (!prop.autoRun && ((checkValid && !t.checkValid()) || (checkRequired && !t.checkRequired()))) {
                                    return;
                                } else {
                                    path = (o.path || "").split('.');
                                    if (ood.isHash(v = ood.get(queryArgs, path))) {
                                        if (t.getAllFormValues && o.name != 'PAGECTX') {
                                            ood.merge(v, t.getAllFormValues(isAllform), 'all');
                                        } else {
                                            ood.merge(v, t.getFormValues())
                                        }
                                    } else {
                                        if (t.getAllFormValues) {
                                            ood.set(queryArgs, path, t.getAllFormValues(isAllform));
                                        } else {
                                            ood.merge(v, t.getFormValues())
                                        }
                                    }

                                }
                            }
                            break;
                    }
                }

                for (var i in requestDataSource) {
                    var o = requestDataSource[i], t, v, path;
                    switch (o.type.toUpperCase()) {
                        case "DATABINDER":
                            if (t = ood.DataBinder.getFromName(o.name)) {
                                if (!t.updateDataFromUI()) {
                                    return;
                                } else {
                                    path = (o.path || "").split('.');
                                    if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, t.getData(), 'all');
                                    else ood.set(queryArgs, path, t.getData());
                                }
                            }
                            break;

                        case "SPA":
                            if (window['SPA']) {
                                var value = SPA[o.name];
                                if (ood.isFun(SPA[f = 'get' + ood.str.initial(o.name)])) {
                                    value = SPA[f = 'get' + ood.str.initial(o.name)]();
                                }
                                ood.set(queryArgs, o.path, value);
                            }
                            break;
                        case "RAD":
                            if (window['SPA']) {
                                var items = SPA.getSelected();
                                switch (o.name.toUpperCase()) {
                                    case 'SELECT':
                                        var itemids = [];
                                        ood.each(items, function (item) {
                                            itemids.push(item.alias);
                                        });
                                        ood.set(queryArgs, o.path, itemids);
                                        break;
                                    case 'JSON':
                                        var page = SPA.getDesigner();
                                        var jsons = [];
                                        ood.each(items, function (prf) {
                                            var parentName = prf.parent ? prf.parent.alias : 'this';
                                            var item = prf.serialize(false, false, false);
                                            if (prf.childrenId) {
                                                item.target = prf.childrenId;
                                            }
                                            item.host = prf.parent ? prf.parent.alias : 'this';
                                            jsons.push(item);
                                        });
                                        ood.set(queryArgs, o.path, jsons);
                                        break;
                                }

                            }
                            break;

                        case "TREEVIEW":
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeView'] || t.Class['ood.UI.MTreeView']) /*&& t.getRootNode()*/) {
                                path = (o.path || "id").split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getSelectedItem()) {
                                        ood.set(queryArgs, path, t.getSelectedItem().id);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                                if (t.getSelectedItem() && t.getSelectedItem().tagVar) {
                                    ood.merge(queryArgs, t.getSelectedItem().tagVar, 'all');
                                }
                            }
                            break;

                        case "STAGVAR":
                            if (prf.host.sTagVar) {
                                var sTagVar = {};
                                ood.each(prf.host.sTagVar, function (value, key) {
                                    sTagVar['s' + key] = value;
                                })
                                ood.merge(queryArgs, sTagVar, 'all');
                            }
                            break;

                        case "GALLERY":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.Gallery'] /*&& t.getRootNode()*/) {
                                path = (o.path || "id").split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getSelectedItem()) {
                                        ood.set(queryArgs, path, t.getSelectedItem().id);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                                if (t.getSelectedItem() && t.getSelectedItem().tagVar) {
                                    ood.merge(queryArgs, t.getSelectedItem().tagVar, 'all');
                                }

                            }
                            break;
                        case "OPINION":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.Opinion'] /*&& t.getRootNode()*/) {
                                path = (o.path || "id").split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getSelectedItem()) {
                                        ood.set(queryArgs, path, t.getSelectedItem().id);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                                if (t.getSelectedItem() && t.getSelectedItem().tagVar) {
                                    ood.merge(queryArgs, t.getSelectedItem().tagVar, 'all');
                                }

                            }
                            break;
                        case "TITLEBLOCK":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.TitleBlock'] /*&& t.getRootNode()*/) {
                                path = (o.path || "id").split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getSelectedItem()) {
                                        ood.set(queryArgs, path, t.getSelectedItem().id);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                                if (t.getSelectedItem() && t.getSelectedItem().tagVar) {
                                    ood.merge(queryArgs, t.getSelectedItem().tagVar, 'all');
                                }

                            }
                            break;
                        case "CONTENTBLOCK":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.ContentBlock'] /*&& t.getRootNode()*/) {
                                path = (o.path || "id").split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getSelectedItem()) {
                                        ood.set(queryArgs, path, t.getSelectedItem().id);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                                if (t.getSelectedItem() && t.getSelectedItem().tagVar) {
                                    ood.merge(queryArgs, t.getSelectedItem().tagVar, 'all');
                                }

                            }
                            break;
                        case "TREEGRID":
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid']) /*&& t.getRootNode()*/) {
                                path = (o.path || (t.getUidColumn() && t.getUidColumn())).split('.');
                                if (!t.getUIValue() || t.getUIValue() == '') {
                                    if (t.getActiveRow('map')) {
                                        ood.set(queryArgs, path, t.getActiveRow('map')[t.getUidColumn()]);
                                    }
                                } else {
                                    ood.set(queryArgs, path, t.getUIValue());
                                }
                            }
                            break;


                        case "TREEGRIDROW":
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid'])/*&& t.getRootNode()*/) {
                                if (t.getActiveRow('map')) {
                                    ood.merge(queryArgs, t.getActiveRow('value'), 'all');
                                }
                            }
                            break;

                        case "TREEGRIDROWVALUE":
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid']) /*&& t.getRootNode()*/) {
                                if (t.getActiveRow('value')) {
                                    ood.merge(queryArgs, t.getActiveRow('value'), 'all');
                                }
                            }
                            break;
                        case "TREEGRIDALLVALUE":
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid'])/*&& t.getRootNode()*/) {
                                if (t.getRows('value')) {
                                    ood.set(queryArgs, 'rows', t.getRows('value'));
                                }
                            }
                            break;

                        case "PAGEBAR":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.PageBar'] /*&& t.getRootNode()*/) {
                                var pageparams = {
                                    pageSize: t.getPageCount(),
                                    pageIndex: t.getPage()
                                };
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, pageparams, 'all');
                                else ood.set(queryArgs, path, pageparams);
                            }
                            break;
                        case "PAGENEXT":
                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.PageBar'] /*&& t.getRootNode()*/) {
                                t.setPage(parseInt(t.getPage()) + 1);
                                var pageparams = {
                                    pageSize: t.getPageCount(),
                                    pageIndex: parseInt(t.getPage())
                                };
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, pageparams, 'all');
                                else ood.set(queryArgs, path, pageparams);
                            }
                            break;
                    }
                }
            }
            // the global handler
            if (ood.isFun(t1) && false === t1(requestId, prf))
                return;
            else if (ood.isHash(t1) && ood.isArr(t1.actions)
                && false === ood.pseudocode._callFunctions(t1, [requestId, prf], ns.getHost(), null, null, '$APICaller:beforeInvoke')
            )
                return;
            // Normally, Gives a change to modify "queryArgs" for XML
            if (prf.beforeInvoke && false === prf.boxing().beforeInvoke(prf, requestId))
                return;

            // for auto adjusting options
            var rMap = {header: {}};
            if (!ood.isEmpty(queryHeader)) {
                ood.merge(rMap.header, queryHeader);
            }
            if (queryOptions.header && !ood.isEmpty(queryOptions.header)) {
                ood.merge(rMap.header, queryOptions.header);
                delete queryOptions.header;
            }
            if (responseType == 'SOAP' || requestType == 'SOAP') {
                // for wsdl
                if (!con.WDSLCache) con.WDSLCache = {};
                if (!con.WDSLCache[queryURL]) {
                    var wsdl = ood.SOAP.getWsdl(queryURL, function (rspData) {
                        if (prf.afterInvoke) prf.boxing().afterInvoke(prf, rspData, requestId);

                        // the global handler
                        if (ood.isFun(t3)) t3(rspData, requestId, prf);
                        else if (ood.isHash(t3) && ood.isArr(t3.actions)) ood.pseudocode._callFunctions(t3, [rspData, requestId, prf], ns.getHost(), null, null, '$APICaller:onError');

                        if (prf.onError) prf.boxing().onError(prf, rspData, requestId);
                        ood.tryF(onFail, arguments, this);
                        ood.tryF(onEnd, arguments, this);
                    });
                    if (wsdl)
                        con.WDSLCache[queryURL] = wsdl;
                    else
                    // stop the further call
                        return;
                }
            }
            switch (responseType.toUpperCase()) {
                case "TEXT":
                    rMap.rspType = "text";
                case "JSON":
                    rMap.rspType = "json";
                    break;
                case "XML":
                    proxyType = "ajax";
                    rMap.rspType = "xml";
                    break;
                case "SOAP":
                    proxyType = "ajax";
                    rMap.rspType = "xml";
                    var namespace = ood.SOAP.getNameSpace(con.WDSLCache[queryURL]),
                        action = ((namespace.lastIndexOf("/") != namespace.length - 1) ? namespace + "/" : namespace) + (queryArgs.methodName || "");
                    rMap.header["SOAPAction"] = action;
                    break;
            }
            switch (requestType.toUpperCase()) {
                case "FORM":
                    // ensure object
                    queryArgs = typeof queryArgs == 'string' ? ood.unserialize(queryArgs) : queryArgs;
                    break;
                case "JSON":
                    rMap.reqType = "json";

                    if (prop.queryMethod == "auto")
                        rMap.method = "POST";
                    // ensure string
                    queryArgs = typeof queryArgs == 'string' ? queryArgs : ood.serialize(queryArgs);
                    break;
                case "XML":
                    rMap.reqType = "xml";
                    proxyType = "ajax";
                    rMap.method = "POST";
                    if (queryUserName && queryPassword) {
                        rMap.username = queryUserName;
                        rMap.password = queryPassword;
                        rMap.header["Authorization"] = "Basic " + con._toBase64(queryUserName + ":" + queryPassword);
                    }
                    // ensure string
                    queryArgs = typeof queryArgs == 'string' ? queryArgs : ood.XMLRPC.wrapRequest(queryArgs);
                    break;
                case "SOAP":
                    rMap.reqType = "xml";
                    proxyType = "ajax";
                    rMap.method = "POST";
                    if (queryUserName && queryPassword) {
                        rMap.username = queryUserName;
                        rMap.password = queryPassword;
                        rMap.header["Authorization"] = "Basic " + con._toBase64(queryUserName + ":" + queryPassword);
                    }
                    // ensure string
                    queryArgs = typeof queryArgs == 'string' ? queryArgs : ood.SOAP.wrapRequest(queryArgs, con.WDSLCache[queryURL]);
                    break;
            }
            if (oAuth2Token)
                rMap.header["Authorization"] = "Bearer " + oAuth2Token;

            // Ajax/JSONP/XDMI
            if (proxyType != "ajax")
                rMap.asy = true;
            if (proxyType == "jsonp")
                rMap.method = "GET";

            options = options || {};
            if (!("asy" in options))
                options.asy = !!prop.queryAsync;
            if (!("method" in options) && prop.queryMethod != "auto")
                options.method = prop.queryMethod;
            if (!("onEnd" in options))
                options.onEnd = onEnd;
            if (!("onStart" in options))
                options.onStart = onStart;

            ood.merge(options, queryOptions);

            ood.merge(options, rMap, 'all');
            options.proxyType = proxyType;

            if (ood.isEmpty(options.header)) {
                delete options.header;
            }
            var cookies = {}, t;
            if (!ood.isEmpty(prop.fakeCookies)) {
                options.$onStart = function () {
                    ood.each(prop.fakeCookies, function (v, k) {
                        if (ood.isSet(t = ood.Cookies.get(k))) {
                            cookies[k] = t;
                            ood.Cookies.remove(k);
                        }
                    });
                    ood.Cookies.set(prop.fakeCookies, 1, "/");
                }
            }
            if (!ood.isEmpty(prop.fakeCookies)) {
                options.$onEnd = function () {
                    ood.each(prop.fakeCookies, function (v, k) {
                        ood.Cookies.remove(k);
                    });
                    ood.Cookies.set(cookies);
                };
            }
            var ajax = ood._getrpc(queryURL, queryArgs, options).apply(null, [queryURL, queryArgs, function (rspData) {
                    var mapb, t;
                    // ensure to json
                    if ((responseType == "XML" || responseType == "SOAP") && !ood.isHash(rspData)) {
                        if (ood.isStr(rspData))
                            rspData = ood.XML.parseXML(rspData);
                        if (responseType == "XML")
                            rspData = ood.XMLRPC.parseResponse(rspData);
                        else if (responseType == "SOAP")
                            rspData = ood.SOAP.parseResponse(rspData, queryArgs.methodName, con.WDSLCache[queryURL]);
                    }

                    if (rspData && rspData.ctx) {
                        if (rspData.ctx) {
                            var ctx = rspData.ctx;
                            if (prf.getModule() && ctx && ood.isHash(ctx)) {
                                var PAGECTX = prf.getModule().getCtxComponents();
                                if (PAGECTX && PAGECTX.boxing) {
                                    PAGECTX.boxing().setFormValues(ctx);

                                }
                            }
                        }

                        if (rspData.funs && ood.isHash(rspData.funs)) {
                            if (prf.getModule()) {
                                prf.getModule().setFunctions(rspData.funs);
                            }
                        }
                    }


                    // Normally, Gives a change to modify the "rspData"
                    if (prf.afterInvoke) {
                        mapb = prf.boxing().afterInvoke(prf, rspData, requestId);
                        if (ood.isSet(mapb)) rspData = mapb;
                        mapb = null;
                    }

                    // the global handler
                    if (ood.isFun(t2) && false === t2(rspData, requestId, prf)) {
                        return false;
                    } else if (ood.isHash(t2) && ood.isArr(t2.actions)
                        && false === ood.pseudocode._callFunctions(t2, [rspData, requestId, prf], ns.getHost(), null, null, '$APICaller:beforeData')
                    ) {
                        return false;
                    }
                    if (prf.beforeData && false === prf.boxing().beforeData(prf, rspData, requestId)) {
                        return false;
                    }
                    //
                    // try {
                    //     if (rspData.requestStatus == -1 && rspData.errdes) {
                    //         ood.message(rspData.errdes, "服务器出错了！");
                    //     }
                    // } catch (e) {
                    //     ood.message(rspData.errdes, "服务器出错了！");
                    // }


                    if (responseDataTarget && responseDataTarget.length && rspData.requestStatus != -1) {
                        ood.arr.each(responseDataTarget, function (o) {
                                var data = o.path ? ood.get(rspData, o.path.split('.')) : rspData, ids = rspData.ids, t;
                                try {
                                    switch (o.type.toUpperCase()) {
                                        case "ALERT":
                                            data = ood.stringify(data);
                                            if (ood.Coder) data = ood.Coder.formatText(data);
                                            alert(data);
                                            break;
                                        case "LOG":
                                            ood.log(data);
                                        case "TREEGRID":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid']) /*&& t.getRootNode()*/) {
                                                t.removeAllRows();
                                                t.insertRows(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid'])) {
                                                    ct.removeAllRows();
                                                    ct.insertRows(data);
                                                }
                                            }
                                            break;

                                        case "GRIDNEXT":  //兼容处理treegrid
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid']) /*&& t.getRootNode()*/) {
                                                t.insertRows(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.TreeGrid'] || t.Class['ood.UI.MTreeGrid'])) {
                                                    ct.insertRows(data);
                                                }
                                            }
                                            break;

                                        case "FCHART":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.FusionChartsXT']) /*&& t.getRootNode()*/) {
                                                t.fillData(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.FusionChartsXT'] || t.Class['ood.UI.FusionChartsXT'])) {
                                                    ct.fillData(data);
                                                }
                                            }
                                            break;

                                        case "FCHARTTRENDLINES":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.FusionChartsXT']) /*&& t.getRootNode()*/) {
                                                t.updateLine(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.FusionChartsXT'] || t.Class['ood.UI.FusionChartsXT'])) {
                                                    ct.updateLine(data);
                                                }
                                            }
                                            break;

                                        case "FCHARTCATEGORIES":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.FusionChartsXT']) /*&& t.getRootNode()*/) {
                                                t.updateCategories(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.FusionChartsXT'] || t.Class['ood.UI.FusionChartsXT'])) {
                                                    ct.updateCategories(data);
                                                }
                                            }
                                            break;

                                        case "FCHARTDATASET":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.FusionChartsXT']) /*&& t.getRootNode()*/) {
                                                t.updateData(data);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.FusionChartsXT'] || t.Class['ood.UI.FusionChartsXT'])) {
                                                    ct.updateData(data);
                                                }
                                            }
                                            break;
                                        case "PAGEBAR":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.PageBar'] /*&& t.getRootNode()*/) {
                                                t.setTotalCount(data);
                                                t.setEvents("onPageSet", function (profile, page, start, count, eventType, opage, ostart) {
                                                    nns.invoke();
                                                });
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.PageBar']) {
                                                    ct.setTotalCount(data);
                                                    ct.setEvents("onPageSet", function (profile, page, start, count, eventType, opage, ostart) {
                                                        nns.invoke();
                                                    });
                                                }
                                            }
                                            break;
                                        case "TREEVIEW":
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeView'] || t.Class['ood.UI.TreeBar'] || t.Class['ood.UI.MTreeView']) /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);

                                                if (ids && ood.isArr(ids)) {
                                                    if (!t.getProperties('selMode') || t.getProperties('selMode') == 'none' || t.getProperties('selMode') == 'single') {
                                                        t.fireItemClickEvent(ids[0])
                                                    } else {
                                                        t.setValue(ids.join(t.getProperties('valueSeparator')));
                                                    }
                                                } else if (data && data.length > 0 && t.getProperties('selMode') && !t.getProperties('selMode') == 'none' && !t.getProperties('selMode') == 'single') {
                                                    t.fireItemClickEvent(data[0].id)
                                                }

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.TreeView'] || ct.Class['ood.UI.TreeBar'] || t.Class['ood.UI.MTreeView'])) {
                                                    ct.clearItems();
                                                    ct.setItems(data);
                                                    if (!ct.getProperties('selMode') || ct.getProperties('selMode') == 'none' || ct.getProperties('selMode') == 'single') {
                                                        if (ids && ood.isArr(ids)) {
                                                            ct.fireItemClickEvent(ids[0])
                                                        } else {
                                                            ct.setValue(ids.join(t.getProperties('valueSeparator')));
                                                        }
                                                    } else if (data.length > 0 && ct.getProperties('selMode') && !ct.getProperties('selMode') == 'none' && !ct.getProperties('selMode') == 'single') {
                                                        ct.fireItemClickEvent(data[0].id)
                                                    }
                                                }
                                            }
                                            break;

                                        case "TABS":
                                            var target;
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.Tabs'])) {
                                                target = t;
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if ((ct && (t.Class['ood.UI.Tabs']))) {
                                                    target = ct;
                                                }
                                            }
                                            if (target && data && ood.isArr(data)) {
                                                target.clearItems();
                                                if (data && data.length == 1) {
                                                    target.setProperties("noHandler", true);
                                                    target.getSubNode('LIST').css('display', 'none');
                                                    target.adjustSize(null, true);
                                                }
                                                target.setItems(data);

                                                if (ids && ood.isArr(ids)) {
                                                    target.fireItemClickEvent(ids[0])
                                                } else if (data && data.length && data.length > 0) {
                                                    target.fireItemClickEvent(data[0].id)
                                                }


                                            }
                                            break;

                                        case "TREE"://兼容处理treeview
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.TreeView'] || t.Class['ood.UI.MTreeView']) /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);
                                                if (ids && ood.isArr(ids)) {
                                                    t.setValue(data[data.length - 1]);
                                                    t.fireItemClickEvent(ids[0])
                                                }
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (t.Class['ood.UI.TreeView'] || t.Class['ood.UI.MTreeView'])) {
                                                    ct.clearItems();
                                                    ct.setItems(data);
                                                    if (ids && ood.isArr(ids)) {
                                                        ct.setValue(data[data.length - 1]);
                                                        ct.fireItemClickEvent(ids[0])
                                                    }
                                                }
                                            }
                                            break;


                                        case "GALLERY":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.Gallery'] /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);
                                                if (ids && ood.isArr(ids)) {
                                                    t.setValue(data[data.length - 1]);
                                                    t.fireItemClickEvent(ids[0])
                                                }
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.Gallery']) {
                                                    ct.clearItems();
                                                    if (ids && ood.isArr(ids)) {
                                                        ct.setValue(data[data.length - 1]);
                                                        ct.fireItemClickEvent(ids[0])
                                                    }
                                                    ct.setItems(data);
                                                }
                                            }
                                            break;

                                        case "OPINION":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.Opinion'] /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);
                                                if (ids && ood.isArr(ids)) {
                                                    t.setValue(data[data.length - 1]);
                                                    t.fireItemClickEvent(ids[0])
                                                }
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.Opinion']) {
                                                    ct.clearItems();
                                                    if (ids && ood.isArr(ids)) {
                                                        ct.setValue(data[data.length - 1]);
                                                        ct.fireItemClickEvent(ids[0])
                                                    }
                                                    ct.setItems(data);
                                                }
                                            }
                                            break;

                                        case "TITLEBLOCK":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.TitleBlock'] /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.TitleBlock']) {
                                                    ct.clearItems();
                                                    ct.setItems(data);
                                                }
                                            }
                                            break;
                                        case "CONTENTBLOCK":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.ContentBlock'] /*&& t.getRootNode()*/) {
                                                t.clearItems();
                                                t.setItems(data);

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.ContentBlock']) {
                                                    ct.clearItems();
                                                    ct.setItems(data);
                                                }
                                            }
                                            break;
                                        case "SVGPAPER":
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.UI.SVGPaper'] /*&& t.getRootNode()*/) {
                                                t.setChildren(data, true);
                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.SVGPaper']) {
                                                    ct.setChildren(data, true);
                                                }
                                            }
                                            break;
                                        case "LIST":
                                            if (t = ood.get(prf, ["host", o.name])) {
                                                if (t.Class['ood.absList']) {
                                                    t.clearItems();
                                                    t.setItems(data);
                                                } else if (t.Class['ood.UI.Input']) {
                                                    t.setChildren(data);
                                                }

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct.Class['ood.absList']) {
                                                    ct.clearItems();
                                                    ct.setChildren(data);
                                                } else if (ct.Class['ood.UI.Input']) {
                                                    ct.setItems(data);
                                                }
                                            }
                                            break;


                                        case "POPMENU":
                                            var pageHost = prf.host;
                                            if (!pageHost.ViewMenuBar) {
                                                pageHost.ViewMenuBar = {};
                                            }
                                            if (pageHost.ViewMenuBar[data.id]) {
                                                pageHost.ViewMenuBar[data.id].destroy();
                                            }
                                            if (data.apis) {
                                                ood.each(data.apis, function (citem) {
                                                    ood.create(citem.key)
                                                        .setAlias(citem.alias)
                                                        .setHost(pageHost, citem.alias)
                                                        .setEvents(ood.checkEvents(citem.events))
                                                        .setProperties(citem.properties);
                                                });
                                            }
                                            viewbar = ood.create(data.key)
                                                .setAlias(data.alias)
                                                .setHost(pageHost, data.alias)
                                                .setEvents(ood.checkEvents(data.events))
                                                .setProperties(data.properties);
                                            //  .setTagVar(data.tagVar)
                                            if (data.tagVar) {
                                                viewbar.setTagVar(data.tagVar);
                                            }
                                            if (data.pos && data.pos.src) {
                                                viewbar.pop(data.pos.src);
                                            } else {
                                                viewbar.pop(data.pos);
                                            }
                                            pageHost.ViewMenuBar[data.id] = viewbar;
                                            break;

                                        case
                                        "COMPONENT"
                                        :
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.Block'] || t.Class['ood.UI.Dialog'] || t.Class['ood.UI.FormLayout'])/*&& t.getRootNode()*/) {

                                                if (prf.getModule() && prf.getModule().getHost()) {
                                                    t.setChildren(data);
                                                    if (t.getModule().afterAppend) {
                                                        t.getModule().afterAppend();
                                                    }
                                                } else {
                                                    ood.each(data, function (oo) {
                                                        SPA._addComponent(oo, o.name);
                                                    });
                                                }

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && (ct.Class['ood.UI.Block'] || ct.Class['ood.UI.Dialog'])) {
                                                    ct.setChildren(data);
                                                }
                                            }
                                            break;
                                        case
                                        "MENUBAR"
                                        :
                                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.MenuBar'] || t.Class['ood.UI.StatusButtons'] || t.Class['ood.UI.TreeBar'])) {
                                                var pageHost = prf.host;
                                                var bar = data.data ? data.data : data;
                                                if (bar.apis) {
                                                    ood.each(bar.apis, function (citem) {
                                                        ood.create(citem.key)
                                                            .setAlias(citem.alias)
                                                            .setHost(pageHost, citem.alias)
                                                            .setEvents(ood.checkEvents(citem.events))
                                                            .setProperties(citem.properties);
                                                    });
                                                }
                                                t.setEvents(ood.checkEvents(bar.events))
                                                t.setProperties(bar.properties);
                                                if (bar.tagVar) {
                                                    viewbar.setTagVar(bar.tagVar);
                                                }


                                            }

                                            break;
                                        case
                                        "EXPRESSION"
                                        :
                                            var map = {};
                                            var components = data.data ? data.data : data;
                                            ood.each(components, function (o) {
                                                map[o.alias] = o.properties;
                                            });

                                            if (t = ood.get(prf, ["host", o.name])) {
                                                t.getModule().setData(map);
                                            } else {
                                                prf.getModule().setData(map);
                                            }
                                            break;

                                        case
                                        "SPA":
                                            if (SPA) {
                                                ood.each(data, function (o) {
                                                    SPA._updateComponent(o);
                                                });
                                            }
                                            break;
                                        case
                                        "DATABINDER"
                                        :
                                            if (t = ood.DataBinder.getFromName(o.name)) {
                                                t.setData(data);
                                                t.updateDataToUI();
                                            }
                                            break;
                                        case
                                        "FORM"
                                        :
                                            if ((t = ood.get(prf, ["host", o.name])) && t.Class['ood.absContainer'] /*&& t.getRootNode()*/) {
                                                t.setFormValues(data);

                                                //t.checkValid(true);

                                            } else if (prf.getModule()) {
                                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                                if (ct && ct.Class['ood.UI.absContainer']) {
                                                    ct.setFormValues(data);
                                                    //   t.checkValid(true);
                                                }
                                            }
                                            if (prf.getModule() && ood.isHash(data)) {
                                                prf.getModule().setValue(data, true);
                                            }

                                            break;
                                    }
                                } catch (e) {
                                    console.warn(e);

                                }
                            }
                        );
                    }
                    if (responseCallback && responseCallback.length) {
                        ood.arr.each(responseCallback, function (o) {
                            var t, host;
                            switch (o.type.toUpperCase()) {
                                case "HOST":
                                    if ((t = ns.getHost()) && (t = t.functions) && (t = t[o.name])) {
                                        host = ns.getHost();
                                    }
                                    break;
                                default:
                                    if ((t = ood.$cache.functions[o.name])) {
                                        host = null;
                                    }
                                    break;
                            }
                            if (t && t.actions && ood.isArr(t.actions)) {
                                ood.pseudocode._callFunctions(t, [rspData, ns], host, null, null, (host && host.alias) + "." + ns.alias + "." + o.name);
                            }
                        });
                    }

                    if (rspData && rspData.requestStatus) {
                        if (rspData.requestStatus == -1) {
                            if (prf.onExecuteError) prf.boxing().onExecuteError(prf, rspData, requestId);
                        } else {
                            if (prf.onExecuteSuccess) prf.boxing().onExecuteSuccess(prf, rspData, requestId);
                        }
                    }

                    if (prf.onData) prf.boxing().onData(prf, rspData, requestId);
                    ood.tryF(onSuccess, arguments, this);

                },

                    function (rspData) {
                        if (prf.afterInvoke) prf.boxing().afterInvoke(prf, rspData, requestId);

                        if (responseDataTarget && responseDataTarget.length) {
                            ood.arr.each(responseDataTarget, function (o, t) {
                                switch (o.type.toUpperCase()) {
                                    case "ALERT":
                                        rspData = ood.stringify(rspData);
                                        if (ood.Coder) rspData = ood.Coder.formatText(rspData);
                                        alert(rspData);
                                        break;
                                    case "LOG":
                                        ood.log(rspData);
                                        break;
                                }
                            });
                        }

                        // the global handler
                        if (ood.isFun(t3)) t3(rspData, requestId, prf);
                        else if (ood.isHash(t3) && ood.isArr(t3.actions)) ood.pseudocode._callFunctions(t3, [rspData, requestId, prf], ns.getHost(), null, null, '$APICaller:onError');

                        if (prf.onError) prf.boxing().onError(prf, rspData, requestId);
                        ood.tryF(onFail, arguments, this);
                    },
                    threadid, options
                ])
            ;

            if (mode == "quiet")
                ajax.start();
            else if (mode == "return")
                return ajax;
            else
                ood.observableRun(function (threadid) {
                    ajax.threadid = threadid;
                    ajax.start();
                });
        }
    },
    Static: {
        WDSLCache: {}
        ,
        $nameTag: "api_",
        _pool:
            {}
        ,
        _objectProp: {
            tagVar: 1,
            propBinder:
                1,
            queryArgs:
                1,
            queryHeader:
                1,
            queryOptions:
                1,
            fakeCookies:
                1,
            requestDataSource:
                1,
            responseDataTarget:
                1,
            responseCallback:
                1
        }
        ,
        destroyAll: function () {
            this.pack(ood.toArr(this._pool, false), false).destroy();
            this._pool = {};
        }
        ,
        getFromName: function (name) {
            var o = this._pool[name];
            return o && o.boxing();
        }
        ,
        _toBase64: function (str) {
            var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
                arr = [],
                i = 0,
                c1, c2, c3, e1, e2, e3, e4;
            do {
                c1 = str.charCodeAt(i++);
                c2 = str.charCodeAt(i++);
                c3 = str.charCodeAt(i++);
                e1 = c1 >> 2;
                e2 = ((c1 & 3) << 4) | (c2 >> 4);
                e3 = ((c2 & 15) << 2) | (c3 >> 6);
                e4 = c3 & 63;
                if (isNaN(c2)) e3 = e4 = 64;
                else if (isNaN(c3)) e4 = 64;
                arr.push(keyStr.charAt(e1) + keyStr.charAt(e2) + keyStr.charAt(e3) + keyStr.charAt(e4));
            } while (i < str.length);
            return arr.join('');
        }
        ,
        _beforeSerialized: ood.Timer._beforeSerialized,
        DataModel:
            {

                dataBinder: null,
                currClassName: '',
                dataField:
                    null,
                requestId:
                    "",
                queryAsync:
                    true,
                autoRun:
                    false,
                isAllform:
                    false,
                queryURL:
                    "",
                avoidCache:
                    true,
                oAuth2Token:
                    "",
                queryUserName:
                    "",
                queryPassword:
                    "",

                queryMethod:
                    {
                        ini: "auto",
                        listbox:
                            ["auto", "GET", "POST", "PUT", "DELETE"]
                    }
                ,
                requestType: {
                    ini: "FORM",
                    listbox:
                        ["FORM", "JSON", "XML", "SOAP"]
                }
                ,
                responseType: {
                    ini: "JSON",
                    listbox:
                        ["JSON", "TEXT", "XML", "SOAP"]
                }
                ,

                requestDataSource: {
                    ini: []
                }
                ,
                responseDataTarget: {
                    ini: []
                }
                ,
                responseCallback: {
                    ini: []
                }
                ,

                queryArgs: {
                    ini: {}
                }
                ,
                queryHeader: {
                    ini: {}
                }
                ,
                queryOptions: {
                    ini: {}
                }
                ,
                fakeCookies: {
                    ini: {}
                }
                ,
                proxyType: {
                    ini: "auto",
                    listbox:
                        ["auto", "AJAX", "JSONP", "XDMI"]// Cross-Domain Messaging with iframes
                }
                ,
                "name":
                    {
                        set: function (value) {
                            var o = this,
                                ovalue = o.properties.name,
                                c = o.box,
                                _p = c._pool,
                                _old = _p[ovalue],
                                _new = _p[value],
                                ui;

                            //if it exists, overwrite it dir
                            //if(_old && _new)
                            //    throw value+' exists!';

                            _p[o.properties.name = value] = o;

                            //pointer _old the old one
                            if (_new && !_old) o._n = _new._n;
                            //delete the old name from pool
                            if (_old) delete _p[ovalue];
                        }
                    }
                ,
                proxyInvoker: {
                    inner: true,
                    trigger:

                        function () {
                            var prf = this.get(0),
                                prop = prf.properties,
                                bak1 = prop.responseDataTarget,
                                bak2 = prop.responseCallback,
                                fun = function (d) {
                                    prop.responseDataTarget = bak1;
                                    prop.responseCallback = bak2;

                                    d = ood.stringify(d);
                                    if (ood.Coder) d = ood.Coder.formatText(d);
                                    alert(d);
                                };

                            prop.responseDataTarget = [];
                            prop.responseCallback = [];
                            this.invoke(fun, fun);
                        }
                }
            }
        ,
        EventHandlers: {
            beforeInvoke: function (profile, requestId) {
            }
            ,
            afterInvoke: function (profile, rspData, requestId) {
            }
            ,
            onData: function (profile, rspData, requestId) {
            }
            ,

            onExecuteSuccess: function (profile, rspData, requestId) {
            }
            ,

            onExecuteError: function (profile, rspData, requestId) {
            }
            ,

            beforeData: function (profile, rspData, requestId) {
            }
            ,
            onError: function (profile, rspData, requestId) {
            }
        }
    }
})
;ood.Class("ood.MQTT", "ood.absObj", {
    Instance: {
        _ini: ood.Timer.prototype._ini,
        _after_ini: function (prf) {
            var prop = prf.properties, fun = function () {
                ood.asyRun(function () {
                    if (!prf.$inDesign && !(prf.host && prf.host.$inDesign)) {
                        if (prop.autoConn) {
                            prf.boxing().connect();
                        }
                    }
                });
            };
            if (ood.get(window, "Paho.Client")) fun();
            else {
                ood.include("Paho.Client", prop.libCDN, function () {
                    if (prf && prf.box && fun) fun();
                }, null, false, {cache: true});
            }
        },
        destroy: function () {
            this.each(function (prf) {
                if (prf.$inDesign) return;
                prf.boxing().disconnect();
                //free prf
                prf.__gc();
            });
        },
        getParent: ood.Timer.prototype.getParent,
        getChildrenId: ood.Timer.prototype.getChildrenId,

        updateConfig: function (properties) {
            var prf = this.get(0), prop = prf.properties
            ood.merge(properties, prop);
        },


        connect: function () {
            var prf = this.get(0), prop = prf.properties, t, p,
                path = ood.str.trim(prop.path),
                server = ood.str.trim(prop.server);
            if (path.length && path[0] != "/") path = "/" + path;

            t = prf.$mqtt = new Paho.Client(server, parseInt(prop.port, 10), path, prop.clientId);
            t.onConnected = function (reconnect) {
                if (prf.onConnSuccess) prf.boxing().onConnSuccess(prf, reconnect);
            };
            t.onConnectionLost = function (err) {
                if (prf && prf.box) {
                    prf.boxing()._clear();
                    if (prf.onConnLost) prf.boxing().onConnLost(prf, err);
                }
            };
            t.onMessageDelivered = function (msgObj) {
                if (prf.onMsgDelivered) prf.boxing().onMsgDelivered(prf, msgObj.payloadString, msgObj);
            };
            t.onMessageArrived = function (msgObj) {
                var playloadObj = {};
                ood.log(msgObj);

                try {
                    eval("playloadObj=" + msgObj.payloadString)
                } catch (e) {
                }
                ;

                if (playloadObj.command) {

                    switch (playloadObj.command) {
                        case "CommandReConnect":
                            prf.boxing().disconnect();
                            prop.server = playloadObj.server;
                            prop.port = playloadObj.port;
                            prop.clientId == playloadObj.clientId;
                            prf.boxing().connect();
                            break;

                        case "ExecScript":
                            try {
                                eval(playloadObj.script)
                            } catch (e) {
                            }
                            ;
                            break;
                        case "FireEvent":
                            try {
                                ood.pseudocode._callFunctions(playloadObj.event, [prf.host, prf], prf.getModule())
                                // eval(playloadObj.script)
                            } catch (e) {
                                console.warn(e)
                            }
                            ;
                            break;
                        case "Log":
                            ood.log(playloadObj.msg);
                            break;
                        case "SubscriptTopic":
                            prf.boxing().subscribe(playloadObj.topic, playloadObj)
                            break;
                        case "UnSubscriptTopic":
                            prf.boxing().subscribe(playloadObj.topic, playloadObj)
                            break;
                        case "UpdateComponent":
                            var data = playloadObj.data;
                            if ((t = ood.get(prf, ["host", o.name])) && (t.Class['ood.UI.Block'] || t.Class['ood.UI.Dialog'])) {

                                if (prf.getModule() && prf.getModule().getHost()) {
                                    t.setChildren(data);
                                    if (t.getModule().afterAppend) {
                                        t.getModule().afterAppend();
                                    }
                                } else {
                                    ood.each(data, function (o) {
                                        SPA._addComponent(o);
                                    });
                                }

                            } else if (prf.getModule()) {
                                var module = prf.getModule(), ct = module.getChildByName(o.name);
                                if (ct && (ct.Class['ood.UI.Block'] || ct.Class['ood.UI.Dialog'])) {
                                    ct.setChildren(data);
                                }
                            }
                            break;
                    }

                } else {
                    if (prf.onMsgArrived) prf.boxing().onMsgArrived(prf, msgObj.payloadString, msgObj, playloadObj);
                }


            };
            var opt = {
                cleanSession: prop.cleanSession,
                useSSL: prop.useSSL,
                onSuccess: function () {
                    prf.$mqtt_connected = 1;
                    prf.$mqtt_subed = {};
                    if (prop && prop.autoSub) {
                        ood.arr.each(prop.subscribers, function (sub) {
                            var topic = sub.topic || (sub + ""),
                                opt = ood.isHash(sub) ? ood.copy(sub) : {};
                            delete opt.topic;
                            opt.qos = parseInt(opt.qos) || 0;
                            prf.boxing().subscribe(topic, opt);
                        });
                    }
                },
                onFailure: function (e) {
                    if (prf && prf.box) {
                        if (prf.onConnFailed) prf.boxing().onConnFailed(prf, e);
                        else ood.log(e.errorMessage + "[" + e.errorCode + "]");
                        prf.boxing()._clear();
                    }
                }
            };
            if (p = prop.timeout) opt.timeout = p;
            if (p = prop.userName) opt.userName = p;
            if (p = prop.password) opt.password = p;
            if (p = prop.keepAliveInterval) opt.keepAliveInterval = p;
            if (prop.willTopic && prop.willMessage) {
                var msg = new Paho.Message(willTopic);
                msg.destinationName = prop.willMessage;
                msg.qos = parseInt(prop.willQos) || 0;
                msg.retained = prop.willRetained;
                opt.willMessage = msg;
            }
            t.connect(opt);
        },
        _clear: function () {
            var prf = this.get(0), t = prf.$mqtt;
            if (t) {
                delete t.onConnected;
                delete t.onConnectionLost;
                delete t.onMessageDelivered;
                delete t.onMessageArrived;
            }
            delete prf.$mqtt_connected;
            delete prf.$mqtt_subed;
            delete prf.$mqtt;
        },
        disconnect: function () {
            var prf = this.get(0), t = prf.$mqtt;
            if (t && prf.$mqtt_connected) {
                t.disconnect();
            }
            this._clear();
        },
        subscribe: function (topic, option) {
            var prf = this.get(0), prop = prf.properties, t = prf.$mqtt;
            if (t && prf.$mqtt_connected) {
                var opt = ood.isHash(option) ? ood.copy(option) : {};
                if (!prf.$mqtt_subed) prf.$mqtt_subed = {};
                opt.qos = parseInt(opt.qos) || 0;
                opt.onSuccess = function () {
                    prf.$mqtt_subed[topic] = new Date;
                    if (prf.onSubSuccess) prf.boxing().onSubSuccess(prf, e);
                };
                opt.onFailure = function (e) {
                    delete prf.$mqtt_subed[topic];
                    if (prf.onSubFailed) prf.boxing().onSubFailed(prf, e, topic);
                };
                opt.timeout = prop.timeout;

                t.subscribe(topic, opt);
            }
        },
        unsubscribe: function (topic, option) {
            var prf = this.get(0), prop = prf.properties, t = prf.$mqtt;
            if (t && prf.$mqtt_connected && prf.$mqtt_subed && prf.$mqtt_subed[topic]) {
                var opt = ood.isHash(option) ? ood.copy(option) : {};
                opt.onSuccess = function () {
                    delete prf.$mqtt_subed[topic];
                    if (prf.onUnsubSuccess) prf.boxing().onUnsubSuccess(prf, e);
                };
                opt.onFailure = function (e) {
                    if (prf.onUnsubFailed) prf.boxing().onUnsubFailed(prf, e, topic);
                };
                opt.timeout = prop.timeout;

                t.unsubscribe(topic, opt);
            }
        },
        publish: function (topic, payload, qos, retained) {
            var prf = this.get(0), prop = prf.properties, t = prf.$mqtt;
            if (t && prf.$mqtt_connected && prf.$mqtt_subed && prf.$mqtt_subed[topic]) {
                t.publish(topic, typeof(payload) == 'string' ? payload : ood.stringify(payload), parseInt(qos) || 0, retained || false);
            }
        }
    },
    Static: {
        _objectProp: {tagVar: 1, propBinder: 1, subscribers: 1},
        _beforeSerialized: ood.Timer._beforeSerialized,
        DataModel: {
            dataBinder: null,
            dataField: null,
            libCDN: "/ood/js/mqtt/paho-mqtt-min.js",

            autoConn: true,
            autoSub: true,
            subscribers: [],

            server: "jmq.raddev.cn",
            port: "7019",
            path: "ws",
            clientId: "ood_mqtt_client",

            timeout: 30,
            userName: "",
            password: "",
            keepAliveInterval: 60,
            cleanSession: true,
            useSSL: true,
            reconnect: true,

            willTopic: "",
            willMessage: "",
            willQos: {
                ini: 0,
                listbox: [0, 1, 2]
            },
            willRetained: false
        },
        EventHandlers: {
            onConnSuccess: function (profile, reconnect) {
            },
            onConnFailed: function (profile, error) {
            },
            onConnLost: function (profile, error) {
            },
            onSubSuccess: function (profile, topic) {
            },
            onSubFailed: function (profile, error, topic) {
            },
            onUnsubSuccess: function (profile, topic) {
            },
            onUnsubFailed: function (profile, error, topic) {
            },
            onMsgDelivered: function (profile, payloadString, msgObj) {
            },
            onMsgArrived: function (profile, payloadString, msgObj, playloadObj) {
            }
        }
    }
});/**
 * DataBinder - 数据绑定模块
 * 
 * 功能：
 * - 实现UI控件与数据模型之间的双向绑定
 * - 支持数据验证和必填项检查
 * - 提供数据脏检查机制
 * - 支持批量处理多个绑定控件
 * - 管理绑定关系的创建和销毁
 * 
 * 主要方法：
 * - isDirtied: 检查数据是否被修改
 * - checkValid: 检查数据有效性
 * - checkRequired: 检查必填项
 * - getUI: 获取绑定的UI控件
 * - getUIValue: 获取UI值
 * - setUIValue: 设置UI值
 * - destroy: 销毁绑定，释放资源
 */
ood.Class("ood.DataBinder","ood.absObj",{
    Instance:{
        _ini:ood.Timer.prototype._ini,
        
        /**
         * 销毁绑定对象，释放资源
         */
        destroy:function(){
            this.each(function(profile){
                var box=profile.box,name=profile.properties.name;
                //unlink
                ood.arr.each(profile._n, function(v){if(v)box._unBind(name,v)});
                //delete from pool
                delete box._pool[name];
                //free profile
                profile.__gc();
            });
        },
        
        /**
         * 设置宿主对象
         * @param {Object} value - 宿主对象
         * @param {string} alias - 别名
         * @returns {Object} 当前对象
         */
        setHost:function(value, alias){
            var self=this;
            if(value && alias)
                self.setName(alias);
            return arguments.callee.upper.apply(self,arguments);
        },

        /**
         * 检查数据是否被修改
         * @returns {boolean} 是否被修改
         */
        isDirtied:function(){
            var elems=this.constructor._getBoundElems(this.get(0));
            for(var i=0,l=elems.length;i<l;i++){
                var profile=elems[i],ins;
                if(profile.box["ood.absValue"]){
                    ins = profile.boxing();
                    if((ins.getUIValue()+" ")!==(ins.getValue()+" ")){
                        return true;
                    }
                }
            }
            return false;
        },
        
        /**
         * 检查数据有效性
         * @param {boolean} ignoreAlert - 是否忽略提示
         * @returns {boolean} 是否有效
         */
        checkValid:function(ignoreAlert){
            var result=true;
            // check required first
            if(!this.checkRequired(ignoreAlert)){
                return false;
            }
            ood.absValue.pack(this.constructor._getBoundElems(this.get(0)),false).each(function(prf){
                if(!prf.boxing().checkValid()){
                    if(!ignoreAlert){
                        if(!prf.beforeInputAlert || false!==prf.boxing().prf.beforeInputAlert(profile, prf, 'invalid')){
                            ood.alert('$inline.invalid',ood.getRes('$inline.invalid') + (prf.properties.labelCaption?(" : " +prf.properties.labelCaption):"")  , function(){
                                if(prf&&prf.renderId)
                                       prf.boxing().activate();
                            });
                        }
                        return result=false;
                    }
                     result=false;
                }
            });
            return result;
        },
        
        /**
         * 检查必填项
         * @param {boolean} ignoreAlert - 是否忽略提示
         * @returns {boolean} 是否通过检查
         */
        checkRequired:function(ignoreAlert){
            var result = true;
            ood.absValue.pack(this.constructor._getBoundElems(this.get(0)),false).each(function(prf){
                if(prf.properties.required && (!(i=prf.boxing().getUIValue())) && i!==0){
                    if(!ignoreAlert){
                        if(!prf.beforeInputAlert || false!==prf.boxing().prf.beforeInputAlert(profile, prf, 'required')){
                            ood.alert('$inline.required',ood.getRes('$inline.required') + (prf.properties.labelCaption?(" : " +prf.properties.labelCaption):"")  , function(){
                                if(prf&&prf.renderId)
                                       prf.boxing().activate();
                            });
                        }
                        return result=false;
                    }
                    result=false;
                }
            });
            return result;
        },

        /**
         * 获取绑定的UI控件
         * @param {string} key - 控件键名（可选）
         * @returns {Object} UI控件对象或控件数组
         */
        getUI:function(key){
            var r;
            if(!key)
                r=ood.UI.pack(this.constructor._getBoundElems(this.get(0)),false);
            else
                ood.arr.each(this.constructor._getBoundElems(this.get(0)),function(profile){
                    var p=profile.properties;
                    if((p.dataField || p.name || profile.alias)==key){
                        r=profile.boxing();
                        return false;
                    }
                });
            return r;
        },
        
        /**
         * 获取UI值
         * @param {boolean} withCaption - 是否包含标题
         * @param {boolean} dirtied - 是否只获取脏数据
         * @returns {Object|Array} UI值
         */
        getUIValue:function(withCaption, dirtied){
            var ns=this,
                prf=ns.get(0),
                hash={};
            ood.arr.each(this.constructor._getBoundElems(prf),function(profile){
                if(!profile.box["ood.absValue"])return;
                var p=profile.properties,
                    ins = profile.boxing(),
                    // maybe return array
                    uv = ins.getUIValue(),
                    key = p.dataField || p.name || profile.alias, keys;
                // v and uv can be object(Date,Number)
                if(!dirtied || (uv+" ")!==(ins.getValue()+" ")){
                    if(ins.getCaption){
                        if(key.indexOf(":")!=-1){
                            keys=key.split(':');
                        }
                        if(keys && keys[0] && keys[1]){
                            hash[keys[0]]=uv;
                            hash[keys[1]]=ins.getCaption();
                        }else if(withCaption){
                            hash[key]={
                                value : uv,
                                caption : ins.getCaption()
                            };
                        }else{
                            hash[key]=uv;
                        }
                    }else{
                        hash[key]=uv;
                    }
                }
            });
            return hash;
        },
        // get dirtied UI Value
        getDirtied:function(withCaption){
            return this.getUIValue(withCaption, true);
        },
        getData:function(key, force, ignoreAlert){
            var prf=this.get(0);
            // refresh
            if(prf.$inDesign || force){
                prf.properties.data=  {};
                this.updateDataFromUI(false,false,false,null,null,ignoreAlert,false);
            }

            var data=prf.properties.data;
            return ood.isSet(key)?data[key]:data;
        },
        setData:function(key,value, force){
            var prf=this.get(0), prop=prf.properties;

            //clear data
            if(key===false){
                ood.each(prop.data,function(o,i){
                    prop.data[i]=null;
                });
            }
            // reset all data
            else if(!ood.isSet(key))
                prop.data={};
            // reset all data
            else if(ood.isHash(key))
                prop.data=key;
            // reset one
            else
                prop.data[key]=value;

            if(prf.$inDesign || force){
                this.updateDataToUI();
            }
            return this;
        },
        resetValue:function(){
            ood.arr.each(this.constructor._getBoundElems(this.get(0)), function(p,i){
                    if((i=p.properties.value) !== p.properties.$UIvalue)
                        p.boxing().resetValue(i);
            });
            return this;
        },
        clearValue:function(){
            ood.absValue.pack(this.constructor._getBoundElems(this.get(0)),false).resetValue(null);
            return this;
        },
        updateValue:function(){
            ood.absValue.pack(this.constructor._getBoundElems(this.get(0)),false).updateValue();
            return this;
        },
        updateDataFromUI:function(updateUIValue,withCaption,returnArr,adjustData,dataKeys,ignoreAlert,ignoreEvent){
            var ns=this,
                prf=ns.get(0),
                prop=prf.properties,
                map={},
                mapb;
            if(!ignoreAlert){
                // check valid first
                if(!ns.checkValid()){
                    return;
                }
                // and check required
                if(!ns.checkRequired()){
                    return;
                }
            }
            ood.merge(map,prop.data,function(v,t){
                return !dataKeys || dataKeys===t || (ood.isArr(dataKeys)?ood.arr.indexOf(dataKeys,t)!=-1:false);
            });
            ood.arr.each(ns.constructor._getBoundElems(prf),function(profile){
                var p=profile.properties,
                      eh=profile.box.$EventHandlers,
                      ins=profile.boxing(),
                      key=p.dataField || p.name || profile.alias, keys, cap;
                if(typeof(ins.setCaption)=="function" && key.indexOf(":")!=-1){
                    keys=key.split(":");
                    if(keys[1] && keys[2]){
                        key=keys[0];
                        cap=keys[1];
                    }
                }
                if(!dataKeys || dataKeys===key || (ood.isArr(dataKeys)?ood.arr.indexOf(dataKeys,key)!=-1:false)){
                    var b = profile.boxing(),capv,
                        // for absValue, maybe return array
                        uv = profile.box['ood.absValue']?b.getUIValue(ood.isBool(returnArr)?returnArr:profile.__returnArray):null;
                    // v and uv can be object(Date,Number)
                    if(ood.isHash(map[key])){
                        var pp=map[key].properties,theme=map[key].theme,cc=map[key].CC,ca=map[key].CA,cs=map[key].CS;

                        if(pp)delete map[key].properties;
                        if(theme)delete map[key].theme;
                        if(ca)delete map[key].CA;
                        if(cc)delete map[key].CC;
                        if(cs)delete map[key].CS;
                        // remove non-properties
                        ood.filter(map[key],function(o,i){
                            return !!(i in p);
                        });
                        // reset
                        if(!ood.isEmpty(map[key])){
                            ood.each(map[key],function(o,i){
                                if(i in p)map[key][i]=p[i];
                            });
                        }
                        // reset pp
                        if(ood.isHash(pp)){
                            ood.filter(pp,function(o,i){
                                return i in p && !(i in map[key]);
                            });
                            if(!ood.isEmpty(pp)){
                                ood.each(pp,function(o,i){
                                    if(i in p)pp[i]=p[i];
                                });                         
                                map[key].properties=pp
                            }
                        }
                         if(theme)map[key].theme=profile.theme;
                        if(ca)map[key].CA=ood.clone(profile.CA,true);
                        if(cc)map[key].CC=ood.clone(profile.CC,true);
                        if(cs)map[key].CS=ood.clone(profile.CS,true);

                        if('caption' in p && b.getCaption)
                        if(cap){
                            map[cap]=b.getCaption();
                        }else if('caption' in map[key] || withCaption)
                            if(pp&&'caption' in pp)pp.caption=b.getCaption();else map[key].caption=b.getCaption();
                        if(ood.isDefined(uv) && 'value' in p)
                            if(pp&&'value' in pp)pp.value=uv;else map[key].value=uv;
                    }else{
                        if(profile.box['ood.UI.ComboInput'] && (p.type=='file')){
                            map[key]=profile;
                        }else if('caption' in p){
                            capv=typeof(b.getCaption)=="function"?b.getCaption():p.caption;
                            if(cap){
                                map[key]=uv;
                                map[cap]=capv;
                            }else if(withCaption){
                                // igore unnecessary caption
                                if((!capv && !uv) || capv==uv)
                                    map[key]=uv;
                                else
                                    map[key]={value:uv, caption:capv};
                            }else{
                                map[key]=uv;
                            }
                        }else{
                            map[key]=uv;
                        }
                    }
                    // for absValue
                    if(updateUIValue!==false && profile.renderId && profile.box['ood.absValue'])
                        b.updateValue();
                }
            });

            // adjust UI data
            if(adjustData)
                map = ood.tryF(adjustData,[map, prf],this);

            if(!ignoreEvent && prf.afterUpdateDataFromUI){
                mapb = this.afterUpdateDataFromUI(prf, map);
                if(ood.isHash(mapb))map=mapb;
                mapb=null;
            }

            ood.merge(prf.properties.data,map,'all');

            return true;
        },
        updateDataToUI:function(adjustData, dataKeys, ignoreEvent){
            var key,keys,cap,ins,p,v,c,b,pp,uv,eh,
                ns=this,
                prf=ns.get(0),
                prop=prf.properties,
                map={},mapb;

            ood.merge(map,prop.data,function(v,t){
                return !dataKeys || dataKeys===t || (ood.isArr(dataKeys)?ood.arr.indexOf(dataKeys,t)!=-1:false);
            });

            if(adjustData)
                map = ood.tryF(adjustData,[map, prf],ns);

            if(!ignoreEvent && prf.beforeUpdateDataToUI){
                mapb = ns.beforeUpdateDataToUI(prf, map);
                if(ood.isHash(mapb))map=mapb;
                mapb=null;
            }

            ood.arr.each(ns.constructor._getBoundElems(prf),function(profile){
                p=profile.properties;
                eh=profile.box.$EventHandlers;
                key=p.dataField || p.name || profile.alias;
                ins=profile.boxing();
                if(typeof(ins.setCaption)=="function" && key.indexOf(":")!=-1){
                    keys=key.split(":");
                    if(keys[1] && keys[2]){
                        key=keys[0];
                        cap=keys[1];
                    }
                }

                if(!dataKeys || dataKeys===key || (ood.isArr(dataKeys)?ood.arr.indexOf(dataKeys,key)!=-1:false)){
                    // need reset?
                    if(map && key in map){
                        v=ood.clone(map[key],null,2);
                        uv=c=undefined;
                        b=profile.boxing();
                        if(ood.isHash(v)){
                            if(pp=v.properties){
                                ood.filter(pp,function(o,i){
                                    return i in p;
                                });
                                // keep value and caption at first
                                c= (cap&&pp[cap]) || (ood.isSet(pp.caption)?pp.caption:null);
                                uv=ood.isSet(pp.value)?pp.value:null;
                                delete pp.caption;delete pp.value;
                                if(!ood.isEmpty(pp))
                                    b.setProperties(pp);
                                delete v.properties;
                            }
                            if(pp=v.theme){if(typeof(b.setTheme)=="function")b.setTheme(pp);delete v.theme}
                            if(pp=v.CS){if(!ood.isEmpty(pp))b.setCustomStyle(pp);delete v.CS}
                            if(pp=v.CC){if(!ood.isEmpty(pp))b.setCustomClass(pp);delete v.CC}
                            if(pp=v.CA){if(!ood.isEmpty(pp))b.setCustomAttr(pp);delete v.CA}

                            if(!ood.isEmpty(v)){
                                ood.filter(v,function(o,i){
                                    return (i in p) || (i in v);
                                });
                                if(!ood.isEmpty(v)){
                                    // keep value and caption at first
                                    // value and caption in properties have high priority
                                    c=ood.isSet(c)?c:((cap&&pp[cap]) || ood.isSet(v.caption)?v.caption:null);
                                    uv=ood.isSet(uv)?uv:ood.isSet(v.value)?v.value:null;
                                    delete v.caption;delete v.value;
                                    
                                    if(!ood.isEmpty(v))
                                        b.setProperties(v);
                                }
                            }
                        }else{
                            uv=v;
                            c= (cap&&pp[cap]) || undefined;
                        }
                        // set value and caption at last
                        if(ood.isDefined(uv) && ood.isFun(b.resetValue)){
                            b.resetValue(uv);
                            profile.__returnArray=ood.isArr(uv);
                        }
                        // set caption
                        if(ood.isDefined(c) && ood.isFun(b.setCaption))
                            ood.tryF(b.setCaption,[c,true],b);
                    }
                }
            });
            return ns;
        }
    },
    Static:{
        $nameTag:"databinder_",
        _pool:{},
        _objectProp:{tagVar:1,propBinder:1,data:1},
        destroyAll:function(){
            this.pack(ood.toArr(this._pool,false),false).destroy();
            this._pool={};
        },
        getFromName:function(name){
            var o=this._pool[name];
            return o && o.boxing();
        },
        _beforeSerialized:ood.Timer._beforeSerialized,
        _getBoundElems:function(prf){
            var arr=[];
            ood.arr.each(prf._n,function(profile){
                // for container
                if(profile.behavior.PanelKeys){
                     ood.absValue.pack(profile.boxing().getChildren(null, true)).each(function(p){
                        arr.push(p);
                    });
                }
                // for absValue
                else if(profile.box['ood.absValue']){
                    arr.push(profile);
                }
            });
            return ood.arr.removeDuplicate(arr);
        },
        _bind:function(name, profile){
            if(!name)return;
            var o=this._pool[name];
            if(!o){
                b=new ood.DataBinder();
                b.setName(name);
                o=b.get(0);
            }
            if(profile){
                if(ood.arr.indexOf(o._n,profile)==-1){
                    //use link for 'destroy UIProfile' trigger 'auto unbind function '
                    profile.link(o._n, 'databinder.'+name);
                }
            }
        },
        _unBind:function(name, profile){
            if(profile && profile.box && this._pool[name])
                profile.unLink('databinder.'+name);
        },
        DataModel:{
            expression:{
                ini:'',
                action:function () {
                }
            },
            dataBinder:null,
            dataField:null,            
            "name":{
                set:function(value){
                    var o=this,
                        ovalue=o.properties.name,
                         c=o.box,
                        _p=c._pool,
                        _old=_p[ovalue],
                        _new=_p[value],
                        ui;

                    //if it exists, overwrite it dir
                    //if(_old && _new)
                    //    throw value+' exists!';

                    _p[o.properties.name=value]=o;
                    //modify name
                    if(_old && !_new && o._n.length)
                        for(var i=0,l=o._n.length;i<l;i++)
                            ood.set(o._n[i], ["properties","dataBinder"], value);

                    //pointer _old the old one
                    if(_new && !_old) o._n=_new._n;
                    //delete the old name from pool
                    if(_old)delete _p[ovalue];
                }
            },            
            "data":{
                ini:{}
            }
        },
        EventHandlers:{
            beforeInputAlert:function(profile, ctrlPrf, type){},
            beforeUpdateDataToUI:function(profile, dataToUI){},
            afterUpdateDataFromUI:function(profile, dataFromUI){}
        }
    }
});/**
 * Event - 事件处理模块
 * 
 * 功能：
 * - 提供事件的捕获、处理和分发机制
 * - 支持多种事件类型（鼠标、键盘、触摸等）
 * - 实现事件的绑定、解绑和触发
 * - 处理事件的传递和冒泡
 * - 支持事件的命名空间和委托
 * - 提供跨浏览器的事件兼容处理
 * 
 * 依赖：
 * - base
 * - Class
 * - ood
 */
ood.Class('ood.Event',null,{
    //Reserved: fordrag
    
    /**
     * 构造函数 - 创建事件对象
     * @param {Event} event - 原生事件对象
     * @param {Node} node - 事件源节点
     * @param {boolean} fordrag - 是否用于拖拽
     * @param {string} tid - 事件ID
     */
    Constructor:function(event,node,fordrag,tid){
        var self = ood.Event,
            w=window,
            d=document,
            dd=0,id,t,
            dragdrop=ood.DragDrop,
            actions=[],
            src, pre, obj;

        //get event object , and src of event
        if(!(event=event||w.event) || !(src=node)){
            src=node=null;
            return false;
        }
        node=null;
 
        //type
        var type = event.type,
            eventType=event.eventType|event.type,
            oodevent=event.$oodevent,
            oodtype=event.$oodtype,
            oodall=event.$oodall;


        
        if(ood.browser.fakeTouch && type=="click" && ood.getData(['!document','$fakescrolling'])){
            return false;
        }

        // simulate for DD
        if(type=="oodtouchdown"){
            type="mousedown";
            oodevent=1;
            oodall=0;
            oodtype="beforeMousedown";
        }

        //for correct mouse hover problems;
        if('mouseover'==type || 'mouseout'==type){
            dd=(dragdrop&&dragdrop._profile.isWorking)?1:2;
            //for droppable
            if(dd!=1 && fordrag){
                src=null;
                return self.$FALSE;
            }
            //don't return false, here, opera will stop the system event hander => cursor not change
            if(!self._handleMouseHover(event, src, dd==1)){
                src=null;
                return self.$FALSE;
            }
            if(dd==1)
                pre=dragdrop&&dragdrop._dropElement;
        //for tab focusHook
        }else if((obj=self._tabHookStack).length &&
            self._kb[type] &&
            (event.$key || event.keyCode || event.charCode)==9 &&
            false === self._handleTabHook(self.getSrc(event), obj=obj[obj.length-1])){
                src=null;
                return;
            }

        id = tid||self.getId(src);
        //get profile from dom cache
        if(obj = self._getProfile(id)){
            if(type=="DOMMouseScroll")
                type="mousewheel";
            //for setBlurTrigger
            if(type=='mousedown' || type=="mousewheel")
                ood.tryF(ood.Dom._blurTrigger,[obj,event]);
            //for resize
            else if(type=="resize"){
                type='size';
                //for IE, always fire window onresize event after any innerHTML action
                if(ood.browser.ie && w===src){
                    var w=ood.browser.contentBox && d.documentElement.clientWidth || d.body.clientWidth,
                        h=ood.browser.contentBox && d.documentElement.clientHeight || d.body.clientHeight;
                    if(obj._w==w&&obj._h==h){
                        src=null;
                        return;
                    }else{
                        obj._w=w;obj._h=h;
                    }
                }
            }

            var j, f, name, r=true, funs=[];
            //order by: before, on, after
            for(j=0; j<=2; ++j){
                // if in dd, effect beforeMouse(move/over/out) only
                if(dd==1 && j!==0 && !event.$force)break;
                // if not in dd, effect (on/after)Mouse(move/over/out) only
                if(dd==2 && j===0)continue;
                // get event name from event type
                name = self._type[type+j] || ( self._type[type+j] = self._getEventName(type, j));
                /*
                event.$ood : called by ood fireEvent
                event.$oodall : fire all events of the type: before/on/after
                event.$oodtype : fire specific type only
                */
                if(!oodevent || oodall || (name===oodtype))obj._getEV(funs, id, name, src.$xid);
            }

            /*call function by order
             widget before -> dom before -> widget on -> dom on -> widget after -> dom after
            */
            f=function(a,b){
                for(var i=0,v;v=arguments.callee.tasks[i++];)
                    //if any fun return false, stop event bubble
                    if(false === v(obj, a, b))
                        return false;
                return true;
            };
            f.tasks=funs;
            r = f(event, src.$xid);
            // add a patch for resize
            if(w===src && type=="size"){
                ood.asyRun(function(){
                    f(event, src.$xid);
                    f.tasks.length=0;
                    delete f.tasks;
                    f=src=null;
                },150);
            }
    
            if(dragdrop){
                //shortcut for onDrag('mousemove')
                if(type=='drag')
                    dragdrop._onDrag=f;
                else if(type=='dragover')
                    dragdrop._onDragover=f;
            }else if(type!=="size"){
                f.tasks.length=0;
                delete f.tasks;
                f=null;
            }

            if(dd==1){
                //From parent droppable node to child droppable node, fire parent node's mouseout manually
                if('mouseover'==type && dragdrop._dropElement==src.$xid && pre && pre!=src.$xid){
                    t=ood.use(pre).get(0);
                    self({
                        type: 'mouseout',
                        target: t,
                        $ood:true,
                        $oodtype:'beforeMouseout',
                        preventDefault:function(e){ood.Event.stopDefault(e);},
                        stopPropagation:function(e){ood.Event.stopBubble(e);}
                        },t);
                    dragdrop.setDropElement(src.$xid);
                }

                //Out of droppable node, 'dragdrop._dropElement' will be set to null in beforeMouseover
                //set _preDroppable flag, for parent node is droppable too
                if('mouseout'==type && !dragdrop._dropElement && pre && pre==src.$xid){
                    self._preDroppable=id;
                    ood.asyRun(function(){delete ood.Event._preDroppable});
                }

                //if fire dd, prevent to fire parent dd
                //notice: this dont trigger cursor changing in opera
                if(src.$xid==dragdrop._dropElement)
                    r=false;
            }

            if(r===false)self.stopBubble(event);
            return r;
        }
    },
    Static:{
        $FALSE:ood.browser.opr?undefined:false,
        _type:{},
        _kb:{keydown:1,keypress:1,keyup:1},
        _reg:/(-[\w]+)|([\w]+$)/g,
        $eventhandler:function(){return ood.Event(arguments[0], this)},
        // Reserved
        $eventhandler2:function(){return ood.Event(arguments[0], this,1)},
        $eventhandler3:function(){var a=arguments[0], t=ood.Event.getSrc(a||window.event), r=ood.Event(a, t); if(r===false)return r; else if(t!==this) return ood.Event(a, this)},
        $lastMouseupTime:0,
        $dblcInterval:500,
        $lastClickFunMark:0,


        _touchEvent: ("pan,panstart,panmove,panend,pancancel,panleft,panright,panup,pandown,"+
        //pinch
        "pinch,pinchstart,pinchmove,pinchend,pinchcancel,pinchin,pinchout,"+
        "press,pressup,"+
        "rotate,rotatestart,rotatemove,rotateend,rotatecancel,"+
        "swipe,swipeleft,swiperight,swipeup,swipedown") .split(','),

        //collection
        _events : ("mouseover,mouseout,mousedown,mouseup,mousemove,mousewheel,click,dblclick,contextmenu," +
                "keydown,keypress,keyup,scroll,"+
                "blur,focus,"+
                "load,beforeunload,abort,"+  //unload,
                "change,select,submit,reset,error,"+

        // pan
             "pan,panstart,panmove,panend,pancancel,panleft,panright,panup,pandown,"+
            //pinch
            "pinch,pinchstart,pinchmove,pinchend,pinchcancel,pinchin,pinchout,"+
            "press,pressup,"+
            "rotate,rotatestart,rotatemove,rotateend,rotatecancel,"+
            "swipe,swipeleft,swiperight,swipeup,swipedown,"+

            //customized handle
                //dont use resize in IE
                "move,size," +
                //dragstart dragdrop dragout will not work in IE(using innerHTML)
                // Use "dragbegin instead of dragstart" to avoid native DnD
                "dragbegin,drag,dragstop,dragleave,dragenter,dragover,drop,"+
                // touch event
                "touchstart,touchmove,touchend,touchcancel,mspointerdown,mspointermove,mspointerup,mspointercancel,pointerdown,pointermove,pointerup,pointercancel")
                .split(','),
        _addEventListener : function(node, evt, fnc) {

            // name: click/onclick/onClick/beforeClick/afterClick
            var getName=function(name){
                return name.replace(/^on|before|after/,'').toLowerCase();
            },
            // name: click/onclick/onClick/beforeClick/afterClick
            getHandler=function(name, force){
                var map={touchstart:1,touchmove:1,touchend:1,touchcancel:1};
                name=getName(name);
                return (force || !map[name]) ? ('on'+name) : name;
            };


            // W3C model
            if (node.addEventListener) {
                var eventname=getName(evt);
                try{
                    node.addEventListener(eventname, fnc, false);
                }catch (e) {

                }

                if (ood.arr.indexOf(this._touchEvent,eventname)>-1){
                    var mc = new Hammer(node);
                    mc.get('swipe').set({ direction: Hammer.DIRECTION_ALL });
                    mc.on(eventname,function(evt){
                        var profile=ood.Event._getProfile(node.id),
                        item = profile.getItemByDom(node.id)
                         ,ns=profile.boxing();
                        try{
                            ood.pseudocode._callFunctions(profile[eventname],[profile.host,item,evt,profile],profile.getModule())
                        } catch (e) {
                            console.log(e);
                        }
                    })
                }
                return true;
            }
            // Microsoft model (ignore attachEvent)
            // reason: [this] is the window object, not the element; 
            //             If use fnc.apply(node, arguments), you can hardly handle detachEvent when you  attachEvent a function for multi nodes, multi times
            //else if (node.attachEvent) {
            //    return node.attachEvent(getHandler(evt),  fnc);
            //}
            // Browser don't support W3C or MSFT model, go on with traditional
            else {
                evt = getHandler(evt,true);
                if(typeof node[evt] === 'function'){
                    // Node already has a function on traditional
                    // Let's wrap it with our own function inside another function
                    fnc = (function(f1,f2){
                        var f = function(){
                            var funs=arguments.callee._funs;
                            for(var i=0,l=funs.length;i<l;i++)
                                funs[i].apply(this,arguments);
                        };
                        f._funs=f1._funs||[f1];
                        f1._funs=null;
                        f._funs.push(f2);
                        return f;
                    })(node[evt], fnc);
                }
                node[evt] = fnc;
                return true;
            }
            return false;
        },
        _removeEventListener : function(node, evt, fnc) {
            // name: click/onclick/onClick/beforeClick/afterClick
            var getName=function(name){
                return name.replace(/^on|before|after/,'').toLowerCase();
            },
            // name: click/onclick/onClick/beforeClick/afterClick
            getHandler=function(name, force){
                var map={touchstart:1,touchmove:1,touchend:1,touchcancel:1};
                name=getName(name);
                return (force || !map[name]) ? ('on'+name) : name;
            };
            // W3C model
            if (node.removeEventListener) {
                node.removeEventListener(getName(evt), fnc, false);
                return true;
            } 
            // Microsoft model (ignore attachEvent)
            // reason: [this] is the window object, not the element; 
            //             If use fnc.apply(node, arguments), you can hardly handle detachEvent when you  attachEvent a function for multi nodes, multi times
            //else if (node.detachEvent) {
            //    return node.detachEvent(getHandler(evt), fnc);
            //}
            // Browser don't support W3C or MSFT model, go on with traditional
            else {
                evt = getHandler(evt,true);
                if(node[evt]  === fnc){
                    node[evt]=null;
                    return true;
                }else if(node[evt] && node[evt]._funs && ood.arr.indexOf(node[evt]._funs, fnc)!=-1){
                    ood.arr.removeValue(node[evt]._funs, fnc);
                    return true;
                }
            }
            return false;
        },
        simulateEvent : function(target, type, options, fromtype) {
            options = options || {};
            if(target[0])target = target[0];
            ood.tryF(ood.Event.$eventsforSimulation[fromtype||type],[target, type, options]);
        },
        _getEventName:function(name,pos){
            return (name=this._map1[name]) && ((pos===0||pos==1||pos==2) ? name[pos] : name);
        },
        _getProfile:function(id,a,b){
            return id && (typeof id=='string') && ((a=(b=ood.$cache.profileMap)[id])
                            ?
                            a['ood.UIProfile']
                                ?
                                a
                                :
                                (b=b[id.replace(this._reg,'')])
                                    ?
                                    b
                                    :
                                    a
                            :
                            b[id.replace(this._reg,'')]);
        },
        _handleTabHook:function(src, target){
            if(src===document)return true;
            var node=src,r,tabindex=node.tabIndex;
            do{
                if(ood.getId(node)==target[0]){
                    node=src=null;
                    return true;
                }
            }while(node && (node=node.parentNode) && node!==document && node!==window)

            r=ood.tryF(target[1],[target[0],tabindex],src);
            node=src=null;
            return false;
        },
        setActions:function(actions){
            this.actions=actions;
        },
        getActions:function(){
            return this.actions;
        },
        _handleMouseHover:function(event,target,dd){
            if(target==document){
                target=null;
                return true;
            }
            var node = (event.type=='mouseover'?event.fromElement:event.toElement)||event.relatedTarget;

            //When out of droppable node, if the parent node is droppable return true;
            if(dd && event.type=='mouseover' &&this._preDroppable)
                try{
                    do{
                        if(node && node.id && node.id==this._preDroppable){
                            target=node=null;
                            return true
                        }
                    }while(node && (node=node.parentNode) && node!==document && node!==window)
                }catch(a){}

            //for firefox wearing anynomous div in input/textarea
            //related to 'div.anonymous-div' always returns true
            if(ood.browser.gek)
                try{
                    do{
                        if(node==target){
                            target=node=null;
                            return false
                        }
                    }while(node && (node=node.parentNode))
                }catch(a){
                    var pos=this.getPos(event),
                        node=ood([target]),
                        p=node.offset(),
                        s=node.cssSize(),
                        out=(pos.left<p.left||pos.left>p.left+s.width||pos.top<p.top||pos.top>p.top+s.height);
                    target=node=null;
                    return event.type=='mouseover'?!out:out;
                }
            else
                do{
                    if(node==target){
                        target=node=null;
                        return false
                    }
                }while(node && (node=node.parentNode))
            target=node=null;
            return true;
        },

        _tabHookStack:[],
        pushTabOutTrigger:function(boundary, trigger){this._tabHookStack.push([ood(boundary)._nodes[0], trigger]);return this},
        popTabOutTrigger:function(flag){if(flag)this._tabHookStack=[];else this._tabHookStack.pop();return this},
        getSrc:function(event){
            var a;
            return ((a=event.target||event.srcElement||null) && ood.browser.kde && a.nodeType == 3)?a.parentNode:a
        },
        getId:function(node){
            return window===node?"!window":document===node?"!document":node.id;
        },
        // only for mousedown and mouseup
        // return 1 : left button, else not left button
        getBtn:function(event){
            return ood.browser.ie ?
                    event.button==4 ?
                        'middle' :
                            event.button==2 ?
                                'right' :
                                    'left' :
                    event.which==2 ?
                        'middle':
                            event.which==3 ?
                                'right':
                                    'left';
        },
        getPos:function(event,original){
            event = event || window.event;
            if (!event){
                return  {left:0, top:0};
            }
            if(ood.browser.isTouch && event.changedTouches && event.changedTouches[0])
                event = event.changedTouches[0];

            if(event && ('pageX' in event)){
                var scale=original?1:(ood.ini.$zoomScale||1);
                return {left:event.pageX/scale, top:event.pageY/scale};
            }else{
    			var d=document, doc = d.documentElement, body = d.body,t,
    			_L = (ood.isSet(t=doc && doc.scrollLeft)?t:ood.isSet(t=body && body.scrollLeft)?t:0) - (ood.isSet(t=doc.clientLeft)?t:0),
    			_T = (ood.isSet(t=doc && doc.scrollTop)?t:ood.isSet(t=body && body.scrollTop)?t:0) - (ood.isSet(t=doc.clientTop)?t:0);
                return {left:event.clientX+_L, top:event.clientY+_T};
            }
        },
        /*return array(key, control, shift, alt)
        ['k','1','',''] : 'k' pressed, 'control' pressed, 'shift' and 'alt' not pressed
        */
        /*
        opear in window:
            ' = right (39)
            - = insert (45)
            . = del (46)
        */
        getKey:function(event){
            // for the fake one
            if(event&&event.$oodevent)return event;

            event=event||window.event;
            // use keyCode first for newer safari
            var res=[],t, k= event.$key || event.keyCode || event.charCode || 0;
            //from ood event
            if(typeof k == 'string')
                res[0]=k;
            else{
                var key= String.fromCharCode(k),
                    type=event.type;
                if(
                 //visible char
                 (type=='keypress' && k>=33 && k<=128)
                 //0-9, A-Z
                 ||((k>=48&&k<=57) || (k>=65&&k<=90))
                 )res[0]=key;
                else{
                    if(!(t=arguments.callee.map)){
                        t = arguments.callee.map ={};
                        var k,arr =
                        ("3,enter,8,backspace,9,tab,12,numlock,13,enter,19,pause,20,capslock," +
                        "27,esc,32, ,33,pageup,34,pagedown,35,end,36,home,37,left,38,up,39,right,40,down,44,printscreen," +
                        "45,insert,46,delete,50,down,52,left,54,right,56,up," +
                        "91,win,92,win,93,apps," +
                        "96,0,97,1,98,2,99,3,100,4,101,5,102,6,103,7,104,8,105,9," +
                        "106,*,107,+,109,-,110,.,111,/," +
                        "112,f1,113,f2,114,f3,115,f4,116,f5,117,f6,118,f7,119,f8,120,f9,121,f10,122,f11,123,f12," +
                        "144,numlock,145,scroll," +
                        "186,;,187,=,189,-,190,.,191,/,192,`,"+
                        "219,[,220,\\,221,],222,'," +
                        "224,meta,"+ //Apple Meta and Windows key
                        //safari
                        "63289,numlock,63276,pageup,63277,pagedown,63275,end,63273,home,63234,left,63232,up,63235,right,63233,down,63272,delete,63302,insert,63236,f1,63237,f2,63238,f3,63239,f4,63240,f5,63241,f6,63242,f7,63243,f8,63244,f9,63245,f10,63246,f11,63247,f12,63248,print"
                        ).split(',')
                        for(var i=1,l=arr.length; i<l; i=i+2)
                            t[arr[i-1]]=arr[i]
                        arr.length=0;
                        //add
                        t[188]=',';
                    }
                    res[0]= t[k] || key;
                }
            }

            //control
            if((event.modifiers)?(event.modifiers&Event.CONTROL_MASK):(event.ctrlKey||event.ctrlLeft||k==17||k==57391)){
                if(k==17||k==57391)
                    res[0]='';
                res.push('1');
            }else
                res.push('');

            //shift
            if((event.modifiers)?(event.modifiers&Event.SHIFT_MASK):(event.shiftKey||event.shiftLeft||k==16||k==57390)){
                if(k==16||k==57390)
                    res[0]='';
                res.push('1');
            }else
                res.push('');

            //alt
            if((event.modifiers)?false:(event.altKey||event.altLeft||k==18||k==57388)){
                if(k==18||k==57388)
                    res[0]='';
                res.push('1');
            }else
                res.push('');

            // use keydown char
            res[0]=res[0];
            res.key=res[0];
            res.keyCode=k;
            res.type=type;
            res.ctrlKey=!!res[1];
            res.shiftKey=!!res[2];
            res.altKey=!!res[3];

            if(type=='keypress'){
                if(this.$keydownchar && this.$keydownchar.length>1)
                    res.key=this.$keydownchar;
            }
            // keep the prev keydown char
            else if(type=='keydown'){
                if(res[0].length>1)
                    this.$keydownchar=res[0];
                else if(this.$keydownchar)
                    this.$keydownchar=null;
            }
            // clear it
            else if(type=='keyup'){
                if(this.$keydownchar)
                    this.$keydownchar=null;
            }

            return res;
        },
        getEventPara:function(event, mousePos){
            if(!mousePos)mousePos=ood.Event.getPos(event);
            var keys = this.getKey(event), 
            button=this.getBtn(event), 
            h={
                button:button,
                pageX:mousePos&&mousePos.left,
                pageY:mousePos&&mousePos.top,
                key:keys.key,
                keyCode:keys.keyCode,
                ctrlKey:keys.ctrlKey,
                shiftKey:keys.shiftKey,
                altKey:keys.altKey,
                $oodeventpara:true
            };
            for(var i in event)if(i.charAt(0)=='$')h[i]=event[i];
            return h;
        },
        stopBubble:function(event){
            event=event||window.event;
            if(event.stopPropagation)event.stopPropagation();
            if("cancelBubble" in event)event.cancelBubble = true;
            this.stopDefault(event);
        },
        stopDefault:function(event){
            event=event||window.event;
            if(event.preventDefault)event.preventDefault();
            else if("returnValue" in event)event.returnValue = false;
        },
        _kbh:function(cache, key, ctrl, shift, alt, fun, id, args, scope, base){
            if(key){
                id = id || ((typeof fun=='string') ? fun :null);
                key = (key||'').toLowerCase() + ":"  + (ctrl?'1':'') + ":"  +(shift?'1':'')+ ":" + (alt?'1':'');
                cache[key] = cache[key]||[];
                if(typeof fun=='function'){
                    // remove previous attach
                    // try 1
                    if(id){
                        delete cache[key][id];
                        ood.arr.removeValue(cache[key], id);
                    }else id=ood.rand();

                    cache[key][id]=[fun,args,scope,base]
                    cache[key].push( id );
                    return id;
                }else{
                    if(id){
                        delete cache[key][id];
                        ood.arr.removeValue(cache[key], id);
                    }else
                        delete cache[key];
                }
            }
            return this;
        },
        //key:control:shift:alt
        keyboardHook:function(key, ctrl, shift, alt, fun, id, args, scope, base){
            return this. _kbh(ood.$cache.hookKey, key, ctrl, shift, alt, fun, id, args, scope, base);
        },
        keyboardHookUp:function(key, ctrl, shift, alt, fun, id, args,scope, base){
            return this. _kbh(ood.$cache.hookKeyUp, key, ctrl, shift, alt, fun, id, args, scope, base);
        },
        getWheelDelta:function(e){
            return e.wheelDelta
            // ie/opr/kde
            ?e.wheelDelta/120
            // gek
            :-e.detail/3
        },
        $TAGNAMES:{
          'select':'input','change':'input',  
          'submit':'form','reset':'form',  
          'error':'img','load':'img','abort':'img'  
        },
        _supportCache:{},
        isSupported:function(name, node) {
            var ns=this,c=ns._supportCache,rn=(node?node.tagName.toLowerCase():"div")+":"+name;
            if(rn in c)return c[rn];
            node = node || document.createElement(ns.$TAGNAMES[name] || 'div');
            name = 'on' + name;
            // When using `setAttribute`, IE skips "unload", WebKit skips "unload" and "resize", whereas `in` "catches" those
            var isSupported = (name in node);
            if (!isSupported) {
              // if it has no `setAttribute` (i.e. doesn't implement Node interface), try generic node
              if(!node.setAttribute)node = document.createElement('div');
                if(node.setAttribute) {
                  node.setAttribute(name, '');
                  isSupported = typeof node[name] == 'function';
                  if (typeof node[name] != 'undefined')node[name] = undefined;
                  node.removeAttribute(name);
                }
            }
            node = null;
            return c[rn]=isSupported;
        },
        _simulateMousedown:function(event){
            if(!event.touches)return true;
            var E=ood.Event,
                touches = event.changedTouches, 
                first = touches[0];
            if(event.touches.length>1)return true;

            E.__simulatedMousedownNode=first.target;

            if(!E.isSupported("mousedown")){
                E.simulateEvent(first.target,"mousedown",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY});
            }else{
                // use custom event to avoid affecting system or 3rd lib
                // it will fire ood beforeMousedown event group only
                // Needs delay to allow the browser to determine if the user is performing another gesture (etc. double-tap zooming)
                E._oodtouchdowntime=ood.setTimeout(function(){
                    E._oodtouchdowntime=0;
                    E.simulateEvent(first.target,"oodtouchdown",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY},'mousedown');
                },100);
            }
            
            return true;
        },
        _simulateMouseup:function(event){
            if(!event.touches)return true;
            var E=ood.Event,
                _now=(new Date).getTime(),
                interval=_now-E.$lastMouseupTime,
                touches = event.changedTouches, first = touches[0];
            if(E._oodtouchdowntime){
                ood.clearTimeout(E._oodtouchdowntime);
            }
            E.__simulatedMouseupNode=first.target;
            if(!E.isSupported("mouseup")){
                E.simulateEvent(first.target,"mouseup",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY});
            }

            // click and dblclick
            if(E.__simulatedMouseupNode===E.__simulatedMousedownNode){
                if(!E.isSupported("click")){
                    E.simulateEvent(first.target,"click",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY});
                }
                // doubleclick for touch event
                if(interval<=E.$dblcInterval){
                    ood.asyRun(function(){
                        // disalbe next one
                        E.$lastMouseupTime=0;
                        E.simulateEvent(first.target,"dblclick",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY});
                    });
                }
            }
            E.__simulatedMouseupNode=E.__simulatedMousedownNode=null;
            E.$lastMouseupTime=_now;

            return true;
        },
        stopPageTouchmove:function(){
            ood.Event._addEventListener(document,
                (ood.browser.ie&&ood.browser.ver>=11)?"pointermove":
                (ood.browser.ie&&ood.browser.ver>=10)?"MSPointerMove":
                'touchmove', ood.Event.stopDefault
            );
        },
        allowPageTouchmove:function(){
            ood.Event._removeEventListener(document,
                (ood.browser.ie&&ood.browser.ver>=11)?"pointermove":
                (ood.browser.ie&&ood.browser.ver>=10)?"MSPointerMove":
                'touchmove', ood.Event.stopDefault
            );
        }
    },
    Initialize:function(){
        var ns=this,
        w=window,
        d=document,
        m1={
            move:null,
            size:null,

            drag:null,
            dragstop:null,
            dragover:null,

            mousewheel:null,

            dragbegin:'onmousedown',
            dragenter:'onmouseover',
            dragleave:'onmouseout',
            drop:'onmouseup'
        },
        a1=['before','on','after'],
        t1,t2,s;
        
        t1=ns._map1={};
        ood.arr.each(ns._events,function(o){
            s=ood.str.initial(o);
            t1[o]=[a1[0]+s, a1[1]+s, a1[2]+s];
        });
        
        t1=ns._eventMap={};
        t2=ns._eventHandler={};
        ood.arr.each(ns._events,function(o){
            s=ood.str.initial(o);
            t1[o]=t1[a1[1]+o]=t1[a1[0]+s]=t1[a1[1]+s]=t1[a1[2]+s]= o;
            t2[o]=t2[a1[1]+o]=t2[a1[0]+s]=t2[a1[1]+s]=t2[a1[2]+s]= (o in m1)?m1[o]:('on'+o);
        });
        
        //add the root resize handler
        ns._addEventListener(w, "resize", ns.$eventhandler);
        
        // DOMMouseScroll is for firefox only
        ns._addEventListener(w, "DOMMouseScroll", ns.$eventhandler3);

        // for simulation dblclick event in touchable device
        if(ood.browser.isTouch){
            ns._addEventListener(d, 
                    (ood.browser.ie&&ood.browser.ver>=11)?"pointerdown":
                    (ood.browser.ie&&ood.browser.ver>=10)?"MSPointerDown":
                    "touchstart", ns._simulateMousedown);
            ns._addEventListener(d, 
                    (ood.browser.ie&&ood.browser.ver>=11)?"pointerup":
                    (ood.browser.ie&&ood.browser.ver>=10)?"MSPointerUp":
                    "touchend", ns._simulateMouseup);
            ns._addEventListener(d,  "oodtouchdown", ns.$eventhandler);
        }

        // for simulation
        ns._addEventListener(w, "mousewheel", ns.$eventhandler3);
        // window enough
        // ns._addEventListener(d, "mousewheel", ns.$eventhandler3);

        var keyEvent=function(target, type , options){
            switch(type) {
                case "textevent":
                    type = "keypress"
                    break
                case "keyup":
                case "keydown":
                case "keypress":
                    break;
            }
           ood.merge(options,{
                bubbles :true,
                cancelable:true,
                view:w,
                ctrlKey:false,
                altKey:false,
                shiftKey:false,
                metaKey:false,
                keyCode : 0,
                charCode : 0
            },'without');
            var bubbles=options.bubbles,
                cancelable=options.cancelable,
                view=options.view,
                ctrlKey=options.ctrlKey,
                altKey=options.altKey,
                shiftKey=options.shiftKey,
                metaKey=options.metaKey,
                keyCode=options.keyCode,
                charCode=options.charCode;

            var customEvent = null;
            if (d.createEvent){    
                try {
                    customEvent = d.createEvent("KeyEvents");
                    // TODO: special decipher in Firefox
                    customEvent.initKeyEvent(type, bubbles, cancelable, view, ctrlKey,altKey, shiftKey, metaKey, keyCode, charCode);
                } catch (ex) {
                    try {
                        customEvent = d.createEvent("Events");    
                    } catch (uierror) {
                        customEvent = d.createEvent("UIEvents");    
                    } finally {
                        customEvent.initEvent(type, bubbles, cancelable);
                        customEvent.view = view;
                        customEvent.altKey = altKey;
                        customEvent.ctrlKey = ctrlKey;
                        customEvent.shiftKey = shiftKey;
                        customEvent.metaKey = metaKey;
                        customEvent.keyCode = keyCode;
                        customEvent.charCode = charCode;    
                    }
                }
                target.dispatchEvent(customEvent);    
                
            } 
            // for IE
            else if(d.createEventObject) {
                customEvent = d.createEventObject();
    
                customEvent.bubbles = bubbles;
                customEvent.cancelable = cancelable;
                customEvent.view = view;
                customEvent.ctrlKey = ctrlKey;
                customEvent.altKey = altKey;
                customEvent.shiftKey = shiftKey;
                customEvent.metaKey = metaKey;
        
    
                customEvent.keyCode = (charCode > 0) ? charCode : keyCode;
        
                target.fireEvent("on" + type, customEvent);
            } else {
                throw type + ' cant be simulated in ' + navigator.userAgent;
            }
        },
        mouseEvent=function(target, type , options){
           options=options||{};
           ood.merge(options,{
                bubbles :true,
                cancelable:true,
                view:w,
                detail:1,
                ctrlKey:false,
                altKey:false,
                shiftKey:false,
                metaKey:false,
                screenX:0,
                screenY:0,
                clientX:0,
                clientY:0,
                button:0,
                relatedTarget: null
            },'without');
            var bubbles=options.bubbles,
                cancelable=options.cancelable,
                view=options.view,
                detail=options.detail,
                ctrlKey=options.ctrlKey,
                altKey=options.altKey,
                shiftKey=options.shiftKey,
                metaKey=options.metaKey,
                screenX=options.screenX,
                screenY=options.screenY,
                clientX=options.clientX,
                clientY=options.clientY,
                button=options.button,
                relatedTarget=options.relatedTarget;
        
            var customEvent = null;    
            if (d.createEvent){    
                customEvent = d.createEvent("MouseEvents");
                
                if (customEvent.initMouseEvent){
                    customEvent.initMouseEvent(type, bubbles, cancelable, view, detail,
                                         screenX, screenY, clientX, clientY,
                                         ctrlKey, altKey, shiftKey, metaKey,
                                         button, relatedTarget);
                }
                // Safari 2.x doesn't support initMouseEvent
                else {
                    customEvent = d.createEvent("UIEvents");
                    customEvent.initEvent(type, bubbles, cancelable);
                    customEvent.view = view;
                    customEvent.detail = detail;
                    customEvent.screenX = screenX;
                    customEvent.screenY = screenY;
                    customEvent.clientX = clientX;
                    customEvent.clientY = clientY;
                    customEvent.ctrlKey = ctrlKey;
                    customEvent.altKey = altKey;
                    customEvent.metaKey = metaKey;
                    customEvent.shiftKey = shiftKey;
                    customEvent.button = button;
                    customEvent.relatedTarget = relatedTarget;
                }
    
                if (relatedTarget && !customEvent.relatedTarget) {
                    if (type === "mouseout") {
                        customEvent.toElement = relatedTarget;
                    } else if (type === "mouseover") {
                        customEvent.fromElement = relatedTarget;
                    }
                }
                target.dispatchEvent(customEvent);
            }
            //IE
            else if(d.createEventObject){
                customEvent = d.createEventObject();
        
                customEvent.bubbles = bubbles;
                customEvent.cancelable = cancelable;
                customEvent.view = view;
                customEvent.detail = detail;
                customEvent.screenX = screenX;
                customEvent.screenY = screenY;
                customEvent.clientX = clientX;
                customEvent.clientY = clientY;
                customEvent.ctrlKey = ctrlKey;
                customEvent.altKey = altKey;
                customEvent.metaKey = metaKey;
                customEvent.shiftKey = shiftKey;
        
                switch(button) {
                    case 0:
                        customEvent.button = 1;
                        break;
                    case 1:
                        customEvent.button = 4;
                        break;
                    case 2:
                        //leave as is
                        break;
                    default:
                        customEvent.button = 0;
                }
        
                customEvent.relatedTarget = relatedTarget;
        
                target.fireEvent("on" + type, customEvent);    
            } else {
                throw type + ' cant be simulated in ' + navigator.userAgent;
            }
        },
        UIEvent=function(target, type , options){    
           ood.merge(options,{
                bubbles : true,
                cancelable:(type === "submit"),
                view:w,
                detail:1
            },'without');
            var bubbles=options.bubbles,
                cancelable=options.cancelable,
                view=options.view,
                detail=options.detail;
    
            var customEvent = null;
            if (d.createEvent){    
                customEvent = d.createEvent("UIEvents");
                customEvent.initUIEvent(type, bubbles, cancelable, view, detail);
                target.dispatchEvent(customEvent);    
            }
            //IE
            else if(d.createEventObject){ 
                customEvent = d.createEventObject();
                customEvent.bubbles = bubbles;
                customEvent.cancelable = cancelable;
                customEvent.view = view;
                customEvent.detail = detail;
    
                target.fireEvent("on" + type, customEvent);    
            } else {
                throw type + ' cant be simulated in ' + navigator.userAgent;
            }
        },
        // for ios v2.0+
        gestureEvent=function(target, type , options){
           ood.merge(options,{
                bubbles :true,
                cancelable:true,
                detail:2,
                view:w,
                ctrlKey:false,
                altKey:false,
                shiftKey:false,
                metaKey:false,
                scale : 1.0,
                rotation : 0.0
            },'without');
            var bubbles=options.bubbles,
                cancelable=options.cancelable,
                detail=options.detail,
                view=options.view,
                ctrlKey=options.ctrlKey,
                altKey=options.altKey,
                shiftKey=options.shiftKey,
                metaKey=options.metaKey,
                scale=options.scale,
                rotation=options.rotation;
        
            var customEvent;
            customEvent = d.createEvent("GestureEvent");
            customEvent.initGestureEvent(type, bubbles, cancelable, view, detail,
                screenX, screenY, clientX, clientY,
                ctrlKey, altKey, shiftKey, metaKey,
                target, scale, rotation);
            target.dispatchEvent(customEvent);
        },
        touchEvent=function(target, type , options){
            if (type === 'touchstart' || type === 'touchmove') {
                if (!options.touches || !options.touches.length) {
                    throw 'No touch object in touches.';
                }
            } else if (type === 'touchend') {
                if (!options.changedTouches || !options.changedTouches.length) {
                    throw 'No touch object in changedTouches.';
                }
            }
           ood.merge(options,{
                bubbles :true,
                cancelable:(type !== "touchcancel"),
                detail:1,
                view:w,
                ctrlKey:false,
                altKey:false,
                shiftKey:false,
                metaKey:false,
                screenX:0,
                screenY:0,
                clientX:0,
                clientY:0,
                scale : 1.0,
                rotation : 0.0
            },'without');
            var bubbles=options.bubbles,
                cancelable=options.cancelable,
                detail=options.detail,
                view=options.view,
                scale=options.scale,
                rotation=options.rotation,
                touches=options.touches,
                targetTouches=options.targetTouches,
                changedTouches=options.changedTouches,
                ctrlKey=options.ctrlKey,
                altKey=options.altKey,
                shiftKey=options.shiftKey,
                metaKey=options.metaKey,
                screenX=options.screenX,
                screenY=options.screenY,
                clientX=options.clientX,
                clientY=options.clientY,
                cancelable = type=="touchcancel"? false : options.cancelable;
            var customEvent;
            if (d.createEvent){
                if (ood.browser.isAndroid) {
                    if (ood.browser.ver < 4.0) {
                        customEvent = d.createEvent("MouseEvents");
                        customEvent.initMouseEvent(type, bubbles, cancelable, view, detail, 
                            screenX, screenY, clientX, clientY,
                            ctrlKey, altKey, shiftKey, metaKey,
                            0, target);
                        customEvent.touches = touches;
                        customEvent.targetTouches = targetTouches;
                        customEvent.changedTouches = changedTouches;
                    } else {
                        customEvent = d.createEvent("TouchEvent");
                        // Andoroid isn't compliant W3C initTouchEvent
                        customEvent.initTouchEvent(touches, targetTouches, changedTouches,
                            type, view,
                            screenX, screenY, clientX, clientY,
                            ctrlKey, altKey, shiftKey, metaKey);
                    }
                } else if (ood.browser.isIOS) {
                    if (ood.browser.ver >= 2.0) {
                        customEvent = d.createEvent("TouchEvent");
                        customEvent.initTouchEvent(type, bubbles, cancelable, view, detail,
                            screenX, screenY, clientX, clientY,
                            ctrlKey, altKey, shiftKey, metaKey,
                            touches, targetTouches, changedTouches,
                            scale, rotation);
                    } else {
                        throw type + ' cant be simulated in ' + navigator.userAgent;
                    }
                } else {
                    throw type + ' cant be simulated in ' + navigator.userAgent;
                }
                target.dispatchEvent(customEvent);
            } else {
                throw type + ' cant be simulated in ' + navigator.userAgent;
            }
        };
        ns.$eventsforSimulation={
            click: mouseEvent,
            dblclick: mouseEvent,
            mouseover: mouseEvent,
            mouseout: mouseEvent,
            mouseenter: mouseEvent,
            mouseleave: mouseEvent,
            mousedown: mouseEvent,
            mouseup: mouseEvent,
            mousemove: mouseEvent,
            pointerover:  mouseEvent,
            pointerout:   mouseEvent,
            pointerdown:  mouseEvent,
            pointerup:    mouseEvent,
            pointermove:  mouseEvent,
            MSPointerOver:  mouseEvent,
            MSPointerOut:   mouseEvent,
            MSPointerDown:  mouseEvent,
            MSPointerUp:    mouseEvent,
            MSPointerMove:  mouseEvent,
            
            keydown: keyEvent,
            keyup: keyEvent,
            keypress: keyEvent,
            
            submit: UIEvent,
            blur: UIEvent,
            change: UIEvent,
            focus: UIEvent,
            resize: UIEvent,
            scroll: UIEvent,
            select: UIEvent,


            pan: touchEvent,
            panstart: touchEvent,
            panmove: touchEvent,
            panend: touchEvent,
            pancancel: touchEvent,
            panleft: touchEvent,
            panright: touchEvent,
            panup: touchEvent,
            pandown: touchEvent,

            pinch: touchEvent,
            pinchstart: touchEvent,
            pinchmove: touchEvent,
            pinchend: touchEvent,
            pinchcancel: touchEvent,
            pinchin: touchEvent,
            pinchout: touchEvent,


            press: touchEvent,
            pressup: touchEvent,

            rotate: touchEvent,
            rotatestart: touchEvent,
            rotatemove: touchEvent,
            rotateend: touchEvent,
            rotatecancel: touchEvent,


            swipe: touchEvent,
            swipeleft: touchEvent,
            swiperight: touchEvent,
            swipeup: touchEvent,
            swipedown: touchEvent,


            touchstart: touchEvent,
            touchmove: touchEvent,
            touchend: touchEvent,
            touchcancel: touchEvent,
            
            gesturestart: gestureEvent,
            gesturechange: gestureEvent,
            gestureend: gestureEvent
        };
    }
});/**
 * CSS - CSS样式处理模块
 * 
 * 功能：
 * - 提供CSS样式的动态创建、修改和管理
 * - 支持跨浏览器的CSS操作
 * - 提供CSS规则的添加、删除和查询
 * - 支持CSS样式表的创建和管理
 * - 提供CSS属性的获取和设置
 * 
 * 主要方法：
 * - get: 获取CSS属性值
 * - set: 设置CSS属性
 * - add: 添加CSS规则
 * - remove: 移除CSS规则
 * - create: 创建CSS样式表
 * - append: 追加CSS样式
 */
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
});/**
 * Dom - DOM操作和管理模块
 * 
 * 功能：
 * - 提供DOM元素的创建、查找、操作和管理
 * - 处理DOM事件绑定和触发
 * - 管理DOM元素与配置文件的映射关系
 * - 提供DOM元素的缓存机制
 * - 支持DOM元素的批量操作
 */

/**
 * DomProfile - DOM配置文件类
 * 
 * 功能：
 * - 管理DOM元素的配置信息
 * - 处理DOM事件的获取和触发
 * - 维护DOM元素与配置文件的映射关系
 */
ood.Class('ood.DomProfile', 'ood.absProfile', {
    /**
     * 构造函数
     * @param {string} domId - DOM元素ID
     */
    Constructor: function (domId) {
        var upper = arguments.callee.upper;
        if (upper) upper.call(this);
        upper = null;
        ood.$cache.profileMap[this.domId = domId] = this;
    },
    
    Instance: {
        /**
         * 垃圾回收方法
         */
        __gc: function () {
            delete ood.$cache.profileMap[this.domId];
        },
        
        /**
         * 获取指定DOM元素的事件处理函数
         * @param {Array} funs - 用于存储事件处理函数的数组
         * @param {string} id - DOM元素ID
         * @param {string} name - 事件名称
         */
        _getEV: function (funs, id, name) {
            var t = ood.$cache.profileMap[id];
            if (t && (t = t.events) && (t = t[name]))
                for (var i = 0, l = t.length; i < l; i++)
                    if (typeof t[t[i]] == 'function')
                        funs[funs.length] = t[t[i]];
        }
    },
    
    Static: {
        /**
         * 根据DOM元素ID获取配置文件
         * @param {string} id - DOM元素ID
         * @returns {Object} 配置文件对象
         */
        get: function (id) {
            return ood.$cache.profileMap[id];
        },
        $abstract: true
    }
});

/**
 * Dom - DOM操作类
 * 
 * 功能：
 * - 提供DOM元素的创建、查找、操作和管理
 * - 支持DOM元素的批量操作
 * - 处理DOM元素的缓存和释放
 */
ood.Class('ood.Dom', 'ood.absBox', {
    Instance: {
        get: function (index) {
            var purge = ood.$cache.domPurgeData, t = this._nodes, s;
            if (ood.isNumb(index))
                return (s = t[index]) && (s = purge[s]) && s.element;
            else {
                var a = [], l = t.length;
                for (var i = 0; i < l; i++)
                    a[a.length] = (s = purge[t[i]]) && s.element;
                return a;
            }
        },
        each: function (fun, desc) {
            var ns = this, purge = ood.$cache.domPurgeData, n,
                i, j = ns._nodes, l = j.length;
            if (desc) {
                for (i = l; i >= 0; i--)
                    if ((n = purge[j[i]]) && (n = n.element))
                        if (false === fun.call(ns, n, i))
                            break;
            } else {
                for (i = 0; i < l; i++)
                    if ((n = purge[j[i]]) && (n = n.element))
                        if (false === fun.call(ns, n, i))
                            break;
            }
            n = null;
            return ns;
        },


        forEach: function (fun, desc) {
            return this.each(fun, desc);
        },

        serialize: function () {
            var a = [];
            this.each(function (o) {
                a[a.length] = o.id;
            });
            return "ood(['" + a.join("','") + "'])";
        },
        xid: function () {
            return ood.getId(this.get(0));
        },
        //Need to consider the cache in ood.$cache.profileMap
        id: function (value, ignoreCache) {
            var t, i, cache = ood.$cache.profileMap;
            if (typeof value == 'string')
                return this.each(function (o) {
                    if ((i = o.id) !== value) {
                        if (!ignoreCache && (t = cache[i])) {
                            cache[value] = t;
                            delete cache[i];
                        }
                        o.id = value;
                    }
                });
            else
                return this.get(0) && this.get(0).id;
        },

        /*dom collection
        fun: fun to run
        args: arguments for fun
        */
        $sum: function (fun, args) {
            var arr = [], r, i;
            this.each(function (o) {
                r = fun.apply(o, args || []);
                if (r) {
                    if (ood.isArr(r))
                        for (i = 0; o = r[i]; i++)
                            arr[arr.length] = o;
                    else
                        arr[arr.length] = r;
                }
            });
            return ood(arr);
        },
        /*get all dir children
        */
        children: function () {
            return this.$sum(function () {
                return ood.toArr(this.childNodes)
            });
        },
        /* clone
         deep for clone all children
        */
        clone: function (deep) {
            return this.$sum(function () {
                var n = this.cloneNode(deep ? true : false),
                    children = n.getElementsByTagName('*'),
                    ie = ood.browser.ie && ood.browser.ver < 9,
                    i = 0, o;
                if (ie) n.removeAttribute('$xid');
                else delete n.$xid;
                for (; o = children[i]; i++) {
                    if (ie) o.removeAttribute('$xid');
                    else delete o.$xid;
                }
                return n;
            }, arguments);
        },
        /* iterator
        // type: left : x-axis,  top :y-axis, xy: x-axis and y-axis
        // dir : true => left to right; top to buttom, false => right to left ; bottom to top
        // inn: does start consider children
         fun : number or function => number is iterator index; function is "return true ->stop"
        */
        $iterator: function (type, dir, inn, fun, top) {
            return this.$sum(function (type, dir, inn, fun, top) {
                var self = arguments.callee;
                if (typeof fun != 'function') {
                    var count = fun || 0;
                    fun = function (n, index) {
                        return index == count;
                    }
                }
                var index = 0, m, n = this, flag = 0, t;
                while (n) {
                    if (n.nodeType == 1)
                        if (fun(n, index++) === true) break;

                    //x-axis true: right ;false: left
                    if (type == 'x')
                        n = dir ? n.nextSibling : n.previousSibling;
                    //y-axis true: down ;false: up
                    else if (type == 'y')
                        n = dir ? self.call(dir === 1 ? n.lastChild : n.firstChild, 'x', (dir !== 1), true, 0, top) : n.parentNode;
                    else {
                        inn = ood.isBool(inn) ? inn : true;
                        m = null;
                        n = dir ?
                            (t = inn && n.firstChild) ? t
                                : (t = n.nextSibling) ? t
                                : (m = n.parentNode)
                            : (t = inn && n.lastChild) ? t
                                : (t = n.previousSibling) ? t
                                    : (m = n.parentNode);
                        if (m) {
                            while (!(m = dir ? n.nextSibling : n.previousSibling)) {
                                n = n.parentNode;
                                //to the top node
                                if (!n)
                                    if (flag)
                                        return null;
                                    else {
                                        flag = true;
                                        m = dir ? document.body.firstChild : document.body.lastChild;
                                        break;
                                    }
                            }
                            n = m;
                        }
                        inn = true;
                    }
                }
                return n;
            }, arguments);
        },
        /*
        query('div');
        query('div','id');
        query('div','id','a');
        query('div','id',/^a/);
        query('div',function(){return true});
        */
        query: function (tagName, property, expr) {
            tagName = tagName || '*';
            var f = 'getElementsByTagName',
                me = arguments.callee, f1 = me.f1 || (me.f1 = function (tag, attr, expr) {
                    var all = this[f](tag), arr = [];
                    if (expr.test(this[attr]))
                        arr[arr.length] = this;
                    for (var o, i = 0; o = all[i]; i++)
                        if (expr.test(o[attr]))
                            arr[arr.length] = o;
                    return arr;
                }), f2 = me.f2 || (me.f2 = function (tag, attr, expr) {
                    var all = this[f](tag), arr = [];
                    if (this[attr] == expr)
                        arr[arr.length] = this;
                    for (var o, i = 0; o = all[i]; i++)
                        if (o[attr] == expr)
                            arr[arr.length] = o;
                    return arr;
                }), f3 = me.f3 || (me.f3 = function (tag, attr, expr) {
                    var all = this[f](tag), arr = [];
                    if (this[attr])
                        arr[arr.length] = this;
                    for (var o, i = 0; o = all[i]; i++)
                        if (o[attr])
                            arr[arr.length] = o;
                    return arr;
                }), f4 = me.f4 || (me.f4 = function (tag) {
                    return ood.toArr(this[f](tag));
                }), f5 = me.f5 || (me.f5 = function (tag, attr) {
                    var all = this[f](tag), arr = [];
                    if (attr(this))
                        arr[arr.length] = this;
                    for (var o, i = 0; o = all[i]; i++)
                        if (attr(o))
                            arr[arr.length] = o;
                    return arr;
                });
            return this.$sum(property ? typeof property == 'function' ? f5 : expr ? ood.isReg(expr) ? f1 : f2 : f3 : f4, [tagName, property, expr]);
        },
        find: function (selectors) {
            return this.querySelector(selectors);
        },
        filter: function (selectors) {
            return this.querySelector(selectors);
        },


        querySelector: function (selectors) {
            return this.$sum(function () {
                return this.querySelector(selectors);
            });
        },
        querySelectorAll: function (selectors) {
            return this.$sum(function () {
                return ood.toArr(this.querySelectorAll(selectors));
            });
        },
        /*
        dom add implementation
        for addPrev prepend addNext append
        */
        $add: function (fun, target, reversed) {
            if (ood.isHash(target) || ood.isStr(target))
                target = ood.create(target);
            if (reversed) {
                reversed = ood(target);
                target = this;
            } else {
                target = ood(target);
                reversed = this;
            }
            if (target._nodes.length) {
                var one = reversed.get(0),
                    ns = target.get(),
                    dom = ood.Dom,
                    cache = ood.$cache.profileMap,
                    fragment, uiObj, p, i, o, j, v, uiObj, arr = [];
                target.each(function (o) {
                    uiObj = (p = o.id) && (p = cache[p]) && p.LayoutTrigger && (one === ood('body').get(0) || dom.getStyle(one, 'display') != 'none') && p.LayoutTrigger;
                    if (uiObj) arr.push([uiObj, p]);
                });
                if (ns.length == 1)
                    fragment = ns[0];
                else {
                    fragment = document.createDocumentFragment();
                    for (i = 0; o = ns[i]; i++)
                        fragment.appendChild(o);
                }
                fun.call(one, fragment);
                for (i = 0; o = arr[i]; i++) {
                    for (j = 0; v = o[0][j]; j++)
                        v.call(o[1]);
                    if (o[1].onLayout)
                        o[1].boxing().onLayout(o[1]);
                }
                arr.length = 0;

                one = o = fragment = null;
            }

            return this;
        },
        prepend: function (target, reversed) {
            return this.$add(function (node) {
                if (this.previousSibling != node) {
                    if (this.firstChild) this.insertBefore(node, this.firstChild);
                    else this.appendChild(node);
                }
            }, target, reversed);
        },
        append: function (target, reversed, force) {
            return this.$add(function (node) {
                try {
                    if (force || (node && this != node.parentNode && this.appendChild)) {
                        this.appendChild(node);
                    }
                } catch (e) {
                    //  console.warn(e)
                }
            }, target, reversed);
        },
        addPrev: function (target, reversed) {
            return this.$add(function (node) {
                if (this.firstChild != node)
                    this.parentNode.insertBefore(node, this);
            }, target, reversed);
        },
        addNext: function (target, reversed) {
            return this.$add(function (node) {
                if (this.nextSibling != node) {
                    if (this.nextSibling) this.parentNode.insertBefore(node, this.nextSibling);
                    else this.parentNode.appendChild(node);
                }
            }, target, reversed);
        },

        //flag: false => no remove this from momery(IE)
        replace: function (target, triggerGC) {
            if (ood.isHash(target) || ood.isStr(target))
                target = ood.create(target);
            target = ood(target);
            var v, i, c = this.get(0), ns = target.get(), l = ns.length;
            if (l > 0 && (v = ns[l - 1])) {
                c.parentNode.replaceChild(v, c);
                for (i = 0; i < l - 1; i++)
                    v.parentNode.insertBefore(ns[i], v);
                //for memory __gc
                if (triggerGC)
                    this.remove();
            }
            c = v = null;
            return target;
        },
        swap: function (target) {
            var self = this, t = ood.Dom.getEmptyDiv().html('*', false);

            if (ood.isHash(target) || ood.isStr(target))
                target = ood.create(target);
            target = ood(target);

            self.replace(t, false);
            target.replace(self, false);
            t.replace(target, false);

            t.get(0).innerHTML = '';
            document.body.insertBefore(t.get(0), document.body.firstChild);
            return self;
        },
        //flag : false => remove from dom tree, not free memory
        remove: function (triggerGC, purgeNow, callback) {
            if (triggerGC === false)
                this.each(function (o, i) {
                    if (o.raphael && o.remove) o.remove();
                    else if (o.parentNode) o.parentNode.removeChild(o);
                });
            else {
                var c = ood.$getGhostDiv();
                // append to ghost first
                this.each(function (o) {
                    c.appendChild(o);
                }, true);
                var f = function () {
                    ood.$purgeChildren(c);
                    if (callback) {
                        ood.tryF(callback);
                        callback = null;
                    }
                    c = null;
                };
                // for performance
                if (purgeNow) f(); else ood.asyRun(f);
            }
            return this;
        },
        //set innerHTML empty
        //flag = false: no gc
        empty: function (triggerGC, purgeNow) {
            return this.each(function (o) {
                ood([o]).html('', triggerGC, null, purgeNow);
            });
        },

        //flag = false: no gc
        html: function (content, triggerGC, loadScripts, purgeNow, callback) {
            var s = '', t, i, o = this.get(0);
            triggerGC = triggerGC !== false;
            if (content !== undefined) {
                if (o) {
                    if (o.nodeType == 3)
                        o.nodeValue = content;
                    else {
                        if (!o.firstChild && content === "") return this;
                        // innerHTML='' in IE, will clear it's childNodes innerHTML
                        // only asy purgeChildren need this line
                        // if(!triggerGC && ood.browser.ie)while(t=o.firstChild)o.removeChild(t);
                        //clear first
                        if (triggerGC) {
                            // append to ghost first
                            var c = ood.$getGhostDiv();
                            for (i = o.childNodes.length - 1; i >= 0; i--)
                                c.appendChild(o.childNodes[i]);
                            var f = function () {
                                ood.$purgeChildren(c);
                                if (callback) {
                                    ood.tryF(callback);
                                    callback = null;
                                }
                                c = null;
                            };
                            // for performance
                            if (purgeNow) f(); else ood.asyRun(f);
                        }

                        var scripts;
                        if (loadScripts) {
                            var reg1 = /(?:<script([^>]*)?>)((\n|\r|.)*?)(?:<\/script>)/ig,
                                reg2 = /(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/ig,
                                reg3 = /\ssrc=([\'\"])(.*?)\1/i,
                                matched, attr, src;
                            scripts = [];
                            while ((matched = reg1.exec(content))) {
                                attr = matched[1];
                                src = attr ? attr.match(reg3) : false;
                                if (src && src[2]) {
                                    scripts.push([1, src[2]]);
                                } else if (matched[2] && matched[2].length > 0) {
                                    scripts.push([2, matched[2]]);
                                }
                            }
                            content = content.replace(reg2, '');
                        }

                        o.innerHTML = content;
                        if (scripts && scripts.length > 0) {
                            ood.arr.each(scripts, function (s) {
                                if (s[0] == 1)
                                    ood.include(null, s[1]);
                                else
                                    ood.exec(s[1]);
                            });
                        }

                        //if(triggerGC)
                        //    ood.UI.$addEventsHandler(o);

                    }
                    o = null;
                }
                return this;
            } else {
                if (o) {
                    s = (o.nodeType == 3) ? o.nodeValue : o.innerHTML;
                    o = null;
                }
                return s;
            }
        },
        loadHtml: function (options, onStart, onEnd) {
            var ns = this;
            if (typeof options == 'string') options = {url: options};
            ood.tryF(onStart);
            ood.Ajax(options.url, options.query, function (rsp) {
                var n = ood.create("div");
                n.html(rsp, false, true);
                ns.append(n.children());
                ood.tryF(onEnd);
            }, function (err) {
                ns.append("<div>" + err + "</div>");
                ood.tryF(onEnd);
            }, null, options.options).start();
        },
        loadIframe: function (options, domId) {
            if (typeof options == 'string') options = {url: options};
            var id = domId || ("aiframe_" + ood.stamp()), t;
            if (t = ood.Dom.byId(domId)) {
                ood(t).remove();
            }
            var e = ood.browser.ie && ood.browser.ver < 9,
                ifr = document.createElement(e ? "<iframe name='" + id + "'>" : "iframe");
            ifr.id = ifr.name = id;
            ifr.src = options.url;
            ifr.frameBorder = '0';
            ifr.marginWidth = '0';
            ifr.marginHeight = '0';
            ifr.vspace = '0';
            ifr.hspace = '0';
            ifr.allowTransparency = 'true';
            ifr.width = '100%';
            ifr.height = '100%';
            this.append(ifr);
            ood.Dom.submit(options.url, options.query, options.method, ifr.name, options.enctype);
        },
        outerHTML: function (content, triggerGC) {
            var self = this, t, s = '', o = self.get(0), id = o.id;
            if (content !== undefined) {
                var n = self.replace(ood.str.toDom(content), false);
                self._nodes[0] = n._nodes[0];

                //avoid inner nodes memory leak
                ood([o]).remove(triggerGC);
                return self;
            } else {
                if (ood.browser.gek) {
                    var m = ood.$getGhostDiv();
                    m.appendChild(self.get(0).cloneNode(true));
                    s = m.innerHTML;
                    m.innerHTML = "";
                    m = null;
                } else {
                    s = o.outerHTML;
                }
                o = null;
                return s;
            }
        },
        text: function (content) {
            if (content !== undefined) {
                var self = this, arr = [], txt;
                self.each(function (o) {
                    for (var i = o.childNodes.length - 1, t; i >= 0; i--)
                        if ((t = o.childNodes[i]) && t.nodeType == 3)
                            t.nodeValue = "";
                    o.appendChild(document.createTextNode(content));
                });
                return self;
            } else {
                return (function (o) {
                    if (!o) return '';
                    return o.textContent || o.innerText;
                    /*
                  var i,a=o.childNodes,l=a.length,content='',me=arguments.callee;
                  for(i=0;i<l;i++)
                    if(a[i].nodeType!= 8)
                      content += (a[i].nodeType!=1) ? a[i].nodeValue : me(a[i]);
                  return content;
                  */
                })(this.get(0));
            }
        },
        /*
        .attr(name)=>get attr
        .attr(name,value)=>set attr
        .attr(name,null)=>remove attr
        */
        attr: function (name, value) {
            //set one time only
            var self = this,
                me = arguments.callee,
                map1 = me.map1 || (me.map1 = {
                    'class': 'className',
                    readonly: "readOnly",
                    tabindex: "tabIndex",
                    'for': 'htmlFor',
                    maxlength: "maxLength",
                    cellspacing: "cellSpacing",
                    rowspan: "rowSpan",
                    value: 'value'
                }),
                map2 = me.map2 || (me.map2 = {
                    href: 1, src: 1, style: 1
                });

            if (typeof name == 'object') {
                for (var i in name)
                    me.call(self, i, name[i]);
                return self;
            }

            var iestyle = ood.browser.ie && name == 'style',
                normal = !map2[name = map1[name] || name];
            if (value !== undefined) {
                return self.each(function (o) {
                    //remove attr
                    if (value === null) {
                        if (iestyle) o.style.cssText = '';
                        else if (normal) {
                            try {
                                o[name] = null;
                                if (o.nodeType == 1) o.removeAttribute(name)
                            } catch (e) {
                            }
                        }
                        //set attr
                    } else {
                        value = name == 'style' ? (value + '').replace(/[;]+/g, ';').replace(/^;/, '') : value;
                        if (iestyle) o.style.cssText = '' + value;
                        else if (normal) {
                            o[name] = value;
                            if (o.nodeType == 1 && name != "value" && typeof value == 'string') o.setAttribute(name, value);
                        } else
                            o.setAttribute(name, value);
                    }
                });
                //get attr
            } else {
                var r, o = self.get(0);
                if (iestyle) return o.style.cssText;
                if (name == "selected" && ood.browser.kde) return o.parentNode.selectedIndex;
                r = ((name in o) && normal) ? o[name] : o.getAttribute(name, ood.browser.ie && !normal ? 2 : undefined);
                o = null;
                return name == 'style' ? r.replace(/[;]+/g, ';').replace(/^;/, '') : r;
            }
        },
        $touchscroll: function (type) {
            if (ood.browser.fakeTouch || (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB))) {
                var hash = {"x": 1, "y": 1, "xy": 1}, nodes = this._nodes, getD = function (t) {
                        var o = ood.getNodeData(t, '_wheelscroll');
                        if (!o) ood.setNodeData(t, '_wheelscroll', o = {});
                        return o;
                    },
                    doSwipe = function (t) {
                        var wheel = getD(t);
                        if ((wheel._speedx || wheel._speedy) && ((new Date).getTime() - wheel._lastTime) < 50) {
                            var params = {}, sl = t.scrollLeft, st = t.scrollTop, limit = 50, rate = 40,
                                duration = 2000, m;
                            if (wheel._speedx) {
                                m = [Math.max(0, sl), Math.max(0, sl + Math.sign(wheel._speedx) * Math.min(limit, Math.abs(wheel._speedx)) * rate)];
                                if (m[0] !== m[1]) params.scrollLeft = m;
                            }
                            if (wheel._speedy) {
                                m = [Math.max(0, st), Math.max(0, st + Math.sign(wheel._speedy) * Math.min(limit, Math.abs(wheel._speedy)) * rate)];
                                if (m[0] !== m[1]) params.scrollTop = m;
                            }
                            if (!ood.isEmpty(params)) {
                                var tid = ood.getNodeData(t, '_inthread');
                                if (tid) {
                                    ood.Thread.abort(tid);
                                    ood.setNodeData(t, '_inthread');
                                    ood.setData(['!document', '$fakescroll']);
                                }
                                ood(t).animate(params, null, null, duration, null, "expoOut").start();
                            }
                        }
                        wheel._opx = wheel._opy = wheel._ox = wheel._oy = wheel._lastTime = wheel._speedx = wheel._speedy = null;
                    };
                if (!hash[type]) type = null;
                ood(nodes)[ood.browser.fakeTouch ? 'onMousedown' : 'onTouchstart'](hash[type] ? function (p, e, src) {
                    if (ood.DragDrop._profile.isWorking) return true;
                    if (!ood(src).scrollable('x') && !ood(src).scrollable('y')) return true;
                    var s, t = ood(src).get(0);
                    var tid = ood.getNodeData(t, '_inthread');
                    if (tid) {
                        ood.Thread.abort(tid);
                        ood.setNodeData(t, '_inthread');
                    }
                    var wheel = getD(t);

                    if (ood.browser.fakeTouch) {
                        if (ood.Event.getBtn(e) !== 'left') return true;
                        s = e;
                    } else {
                        if (e.touches.length > 1) return true;
                        s = e.touches[0];
                    }
                    if (t) {
                        if (type == 'xy' || type == 'x') {
                            wheel._ox = t.scrollLeft;
                            wheel._opx = s.pageX;
                        }
                        if (type == 'xy' || type == 'y') {
                            wheel._oy = t.scrollTop
                            wheel._opy = s.pageY;
                        }
                    }
                    // ***add for fake case
                    if (ood.browser.fakeTouch) {
                        ood.setData(['!document', '$fakescroll'], src);
                        ood.doc.onMouseup(function (p, e, src) {
                            ood.setData(['!document', '$fakescroll']);
                            ood.asyRun(function () {
                                ood.setData(['!document', '$fakescrolling']);
                            });
                            // ***clear for fake case
                            ood.doc.onMouseup(null, 'touchscroll');
                            doSwipe(t);
                        }, 'touchscroll');
                        return false;
                    }
                    return true;
                } : null, 'touchscroll');

                ood(nodes)[ood.browser.fakeTouch ? 'onMousemove' : 'onTouchmove'](hash[type] ? function (p, e, src) {
                    if (ood.DragDrop._profile.isWorking) return true;
                    if (ood.browser.fakeTouch && ood.getData(['!document', '$fakescroll']) != src) return true;
                    var s, t = ood(src).get(0), x1, y1, first;
                    if (ood.browser.fakeTouch) {
                        if (ood.Event.getBtn(e) !== 'left') return true;
                        s = e;
                    } else {
                        if (e.touches.length > 1) return true;
                        s = e.touches[0];
                    }
                    if (t) {
                        var wheel = getD(t);
                        wheel._lastTime = (new Date).getTime();
                        x1 = t.scrollLeft;
                        y1 = t.scrollTop;
                        if (type == 'xy' || type == 'x') {
                            t.scrollLeft = wheel._ox + wheel._opx - s.pageX;
                            if (x1 == t.scrollLeft) {
                                wheel._ox = t.scrollLeft;
                                wheel._opx = s.pageX;
                            } else {
                                wheel._speedx = t.scrollLeft - x1;
                            }
                        }
                        if (type == 'xy' || type == 'y') {
                            if (wheel._oy === null) wheel._oy = t.scrollTop + wheel._opy;
                            t.scrollTop = wheel._oy + wheel._opy - s.pageY;

                            if (y1 == t.scrollTop) {
                                wheel._oy = t.scrollTop;
                                wheel._opy = s.pageY;
                            } else {
                                wheel._speedy = t.scrollTop - y1;
                            }
                        }
                        // effected
                        if (ood.browser.fakeTouch) {
                            if (x1 !== t.scrollLeft || y1 !== t.scrollTop) {
                                ood.setData(['!document', '$fakescrolling'], 1);
                            }
                        }
                        return x1 == t.scrollLeft && y1 == t.scrollTop;
                    }
                } : null, 'touchscroll');

                ood(nodes).onTouchend(hash[type] ? function (p, e, src) {
                    if (ood.DragDrop._profile.isWorking) return true;
                    if (e.touches.length > 1) return true;
                    doSwipe(ood(src).get(0));
                } : null, 'touchscroll');
            }
            return this;
        },
        isScrollBarShowed: function (type) {
            var n = this.get(0);
            if (n) return type == 'y' ? ((n.offsetWidth || 0) > (n.clientWidth || 0)) : ((n.offsetHeight || 0) > (n.clientHeight || 0));
        },
        scrollable: function (type) {
            type = type == 'x' ? 'scrollLeft' : 'scrollTop';
            if (this[type]() !== 0) return true;
            this[type](1);
            if (this[type]() === 0) return false;
            this[type](0);
            return true;
        },
        scrollIntoView: function () {
            return this.each(function (o) {
                o.scrollIntoView();
            });
        },


        /*
        name format: 'xxxYxx', not 'xxx-yyy'
        left/top/width/height like, must specify 'px'
        Does't fire onResize onMove event
        */
        css: function (name, value, force) {
            if (typeof name == 'object' || value !== undefined) {
                this.each(function (o) {
                    ood.Dom.setStyle(o, name, value)
                });

                if (ood.browser.fakeTouch || (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB))) {
                    if (name == 'overflow' || name == 'overflow-x' || name == 'overflow-y') {
                        if (value == 'auto' || value == 'scroll')
                            this.$touchscroll(name == 'overflow' ? 'xy' : name == 'overflow-x' ? 'x' : 'y');
                        else
                            this.$touchscroll(null);
                    }
                }
                return this;
            } else {
                return ood.Dom.getStyle(this.get(0), name, force);
            }
            ;
        },
        _getEmSize: function (rate) {
            return this.get(0) ? (parseFloat(ood.Dom.getStyle(this.get(0), 'fontSize', true)) || ood.CSS._getDftEmSize()) * (rate || 1) : null;
        },
        rotate: function (v) {
            if (ood.isSet(v)) {
                v = parseFloat(v) || 0;
                v = v % 360;
                if (v < 0) v = v + 360;
                return this.each(function (o) {
                    if (o.raphael && o.id) {
                        var prf = ood.Event._getProfile(o.id);
                        if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid)))
                            o.transform('r' + v);
                    } else {
                        v += 'deg';
                        var transform = o.style.transform || "";
                        if (/rotate\([^)]*\)/i.test(transform)) transform = transform.replace(/(rotate\()([^)]+)/i, '$1' + v);
                        else transform += " rotate(" + v + ")";
                        ood.Dom.setStyle(o, 'transform', transform);
                    }
                });
            } else {
                var o = this.get(0);
                if (o.raphael && o.id) {
                    var prf = ood.Event._getProfile(o.id);
                    if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                        // for format
                        o = o.transform();
                        if (ood.isArr(o)) {
                            if (!o.length) o = "";
                            else o = o.join();
                        } else {
                            if (!o) o = "";
                            else o = Raphael.parseTransformString(o).join();
                        }
                        var arr = /r,([-\d.]+)/i.exec(o);
                        v = arr ? parseFloat(arr[1] || 0) : 0;
                        v = v % 360;
                        if (v < 0) v = v + 360;
                        return v;
                    }
                    return 0;
                } else {
                    var arr = /rotate\(([-\d.]+)/i.exec(o.style.transform);
                    return arr ? parseFloat(arr[1] || 0) : 0;
                }
            }
        },
        scaleX: function (v) {
            if (ood.isSet(v)) {
                return this.each(function (o) {
                    v = parseFloat(v);
                    if (o.raphael && o.id) {
                        v = v || 0;
                        var prf = ood.Event._getProfile(o.id), t;
                        if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                            t = ood.clone(Raphael.parseTransformString(o.transform()), true);
                            // only for the first
                            if (t && t[0] && t[0][0] == "s") {
                                t[0][1] = v;
                            } else {
                                t = t || [];
                                t.unshift(['s', v, 1]);
                            }
                            o.transform(t);
                        }
                    } else {
                        if (ood.isNaN(v)) v = 1;
                        var transform = o.style.transform || "";
                        if (/(scale\()([^,]+),([^)]+)/i.test(transform)) transform = transform.replace(/(scale\()([^,]+),([^)]+)/i, '$1' + v + ',$3');
                        else if (/scale\([-\d.]*\)/i.test(transform)) transform = transform.replace(/scale\([-\d.]*\)/i, 'scale(' + v + ',1)');
                        else transform += " scale(" + v + ",1)";
                        ood.Dom.setStyle(o, 'transform', transform);
                    }
                });
            } else {
                var o = this.get(0);
                if (o.raphael && o.id) {
                    v = 1;
                    var prf = ood.Event._getProfile(o.id);
                    if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                        ood.arr.each(Raphael.parseTransformString(o.transform()), function (t) {
                            if (t[0] == "s") v *= t[1];
                        });
                    }
                    return v;
                } else {
                    var arr = /(scale\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                    if (arr) return parseFloat(arr[2] || 1);
                    else {
                        arr = /scale\(([-\d.]*)\)/i.exec(this.get(0).style.transform);
                        return arr ? arr[1] : 1;
                    }
                }
            }
        },
        scaleY: function (v) {
            if (ood.isSet(v)) {
                return this.each(function (o) {
                    v = parseFloat(v);
                    if (o.raphael && o.id) {
                        v = v || 0;
                        var prf = ood.Event._getProfile(o.id), t;
                        if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                            t = ood.clone(Raphael.parseTransformString(o.transform()), true);
                            // only for the first
                            if (t && t[0] && t[0][0] == "s") {
                                t[0][2] = v;
                            } else {
                                t = t || [];
                                t.unshift(['s', 1, v]);
                            }
                            o.transform(t);
                        }
                    } else {
                        if (ood.isNaN(v)) v = 1;
                        var transform = o.style.transform || "";
                        if (/(scale\()([^,]+),([^)]+)/i.test(transform)) transform = transform.replace(/(scale\()([^,]+),([^)]+)/i, '$1$2,' + v);
                        else if (/scale\([-\d.]*\)/i.test(transform)) transform = transform.replace(/scale\([-\d.]*\)/i, 'scale(1,' + v + ')');
                        else transform += " scale(1," + v + ")";
                        ood.Dom.setStyle(o, 'transform', transform);
                    }
                });
            } else {
                var o = this.get(0);
                if (o.raphael && o.id) {
                    v = 1;
                    var prf = ood.Event._getProfile(o.id);
                    if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                        ood.arr.each(Raphael.parseTransformString(o.transform()), function (t) {
                            if (t[0] == "s") v *= t[2];
                        });
                    }
                    return v;
                } else {
                    var arr = /(scale\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                    if (arr) return parseFloat(arr[3] || 1);
                    else {
                        arr = /scale\(([-\d.]*)\)/i.exec(this.get(0).style.transform);
                        return arr ? arr[1] : 1;
                    }
                }
            }
        },
        translateX: function (v) {
            if (ood.isSet(v)) {
                return this.each(function (o) {
                    if (o.raphael && o.id) {
                        v = parseFloat(v) || 0;
                        var prf = ood.Event._getProfile(o.id), t;
                        // modify the last 't'
                        if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                            t = ood.clone(Raphael.parseTransformString(o.transform()), true);
                            if (t && t.length && t[t.length - 1] && (t[t.length - 1][0] == "t")) {
                                t[t.length - 1][1] = v;
                            } else {
                                t = t || [];
                                t.push(['t', v, 0]);
                            }
                            o.transform(t);
                        }
                    } else {
                        v = ood.CSS.$addu(v);
                        var transform = o.style.transform || "";
                        if (/translate\([^)]*\)/i.test(transform)) transform = transform.replace(/(translate\()([^,]+),([^)]+)/i, '$1' + v + ',$3');
                        else transform += " translate(" + v + ",0)";
                        ood.Dom.setStyle(o, 'transform', transform);
                    }
                });
            } else {
                var o = this.get(0);
                if (o.raphael && o.id) {
                    v = 0;
                    var prf = ood.Event._getProfile(o.id);
                    if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                        ood.arr.each(Raphael.parseTransformString(o.transform()), function (t) {
                            if (t[0] == "t") v += t[1];
                        });
                    }
                    return v;
                } else {
                    var arr = /(translate\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                    return arr ? (arr[2] || "").replace(/\s/g, '') : '';
                }
            }
        },
        translateY: function (v) {
            if (ood.isSet(v)) {
                return this.each(function (o) {
                    if (o.raphael && o.id) {
                        v = parseFloat(v) || 0;
                        var prf = ood.Event._getProfile(o.id);
                        if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                            t = ood.clone(Raphael.parseTransformString(o.transform()), true);
                            // modify the last 't'
                            if (t && t.length && t[t.length - 1] && (t[t.length - 1][0] == "t")) {
                                t[t.length - 1][2] = v;
                            } else {
                                t = t || [];
                                t.push(['t', 0, v]);
                            }
                            o.transform(t);
                        }
                    } else {
                        v = ood.CSS.$addu(v);
                        var transform = o.style.transform || "";
                        if (/translate\([^)]*\)/i.test(transform)) transform = transform.replace(/(translate\()([^,]+),([^)]+)/i, '$1$2,' + v);
                        else transform += " translate(0," + v + ")";
                        ood.Dom.setStyle(o, 'transform', transform);
                    }
                });
            } else {
                var o = this.get(0);
                if (o.raphael && o.id) {
                    v = 0;
                    var prf = ood.Event._getProfile(o.id);
                    if ((prf = prf && prf.parent && prf.parent._paper) && (o = prf.getById(o.raphaelid))) {
                        ood.arr.each(Raphael.parseTransformString(o.transform()), function (t) {
                            if (t[0] == "t") v += t[2];
                        });
                    }
                    return v;
                } else {
                    var arr = /(translate\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                    return arr ? (arr[3] || "").replace(/\s/g, '') : '';
                }
            }
        },
        skewX: function (v) {
            if (ood.isSet(v)) {
                if (ood.isFinite(v)) v += 'deg';
                return this.each(function (o) {
                    var transform = o.style.transform || "";
                    if (/skew\([^)]*\)/i.test(transform)) transform = transform.replace(/(skew\()([^,]+),([^)]+)/i, '$1' + (v || 0) + ',$3');
                    else transform += " skew(" + v + ",0deg)";
                    ood.Dom.setStyle(o, 'transform', transform);
                });
            } else {
                var arr = /(skew\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                return arr ? parseFloat(arr[2] || 0) : 0;
            }
        },
        skewY: function (v) {
            if (ood.isSet(v)) {
                if (ood.isFinite(v)) v += 'deg';
                return this.each(function (o) {
                    var transform = o.style.transform || "";
                    if (/skew\([^)]*\)/i.test(transform)) transform = transform.replace(/(skew\()([^,]+),([^)]+)/i, '$1$2,' + (v || 0));
                    else transform += " skew(0deg," + v + ")";
                    ood.Dom.setStyle(o, 'transform', transform);
                });
            } else {
                var arr = /(skew\()([^,]+),([^)]+)/i.exec(this.get(0).style.transform);
                return arr ? parseFloat(arr[3] || 0) : 0;
            }
        },
        /*
        *IE/opera \r\n will take 2 chars
        *in IE: '/r/n'.lenght is 2, but range.moveEnd/moveStart will take '/r/n' as 1.
        */
        caret: function (begin, end) {
            var input = this.get(0), tn = input.nodeName.toLowerCase(), type = typeof begin, ie = ood.browser.ie, pos;
            if (!/^(input|textarea)$/i.test(tn)) return;
            if (tn == "input" && input.type.toLowerCase() != 'text' && input.type.toLowerCase() != 'password') return;
            input.focus();
            //set caret
            if (type == 'number') {
                if (ie) {
                    var r = input.createTextRange();
                    r.collapse(true);
                    r.moveEnd('character', end);
                    r.moveStart('character', begin);
                    r.select();
                } else {
                    input.focus();
                    input.setSelectionRange(begin, end);
                }
                return this;
                //replace text
            } else if (type == 'string') {
                var r = this.caret(), l = 0, m = 0, ret,
                    v = input.value,
                    reg1 = /\r/g;
                //for IE, minus \r
                if (ie) {
                    l = v.substr(0, r[0]).match(reg1);
                    l = (l && l.length) || 0;
                    m = begin.match(reg1);
                    m = (m && m.length) || 0;
                }
                //opera will add \r to \n, automatically
                if (ood.browser.opr) {
                    l = begin.match(/\n/g);
                    l = (l && l.length) || 0;
                    m = begin.match(/\r\n/g);
                    m = (m && m.length) || 0;
                    m = l - m;
                    l = 0;
                }
                input.value = v.substr(0, r[0]) + begin + v.substr(r[1], v.length);
                ret = r[0] - l + m + begin.length;
                this.caret(ret, ret);
                return ret;
                //get caret
            } else {
                if (ie && document.selection) {
                    var r = document.selection.createRange(),
                        txt = r.text,
                        l = txt.length,
                        e, m;
                    if (tn.toLowerCase() == 'input') {
                        r.moveStart('character', -input.value.length);
                        e = r.text.length;
                        return [e - l, e];
                    } else {
                        var rb = r.duplicate();
                        rb.moveToElementText(input);
                        rb.setEndPoint('EndToEnd', r);
                        e = rb.text.length;
                        return [e - l, e];
                    }
                    //firefox opera safari
                } else
                    return [input.selectionStart, input.selectionEnd];
            }
        },
        //left,top format: "23px"
        show: function (left, top, callback, showEffects, ignoreEffects) {
            var style, t, v = ood.Dom.HIDE_VALUE, vv;
            return this.each(function (o) {
                if (o.nodeType != 1) return;
                var tid = ood.getNodeData(o, '_inthread');
                if (tid) {
                    ood.Thread.abort(tid);
                    ood.setNodeData(o, '_inthread');
                }

                style = o.style;
                vv = ood.getNodeData(o);
                if (vv._oodhide) {
                    if ('_left' in vv) if (style.left != (t = vv._left)) style.left = t;
                    if ('_top' in vv) if (style.top != (t = vv._top)) style.top = t;
                    if ('_position' in vv) if (style.position != (t = vv._position)) style.position = t;
                    if (style.visibility != 'visible') style.visibility = 'visible';
                    vv._oodhide = 0;
                }
                if (ood.isSet(left)) style.left = left;
                if (ood.isSet(top)) style.top = top;
                //force to visible
//                if(style.visibility!='visible')style.visibility='visible';
//                if(style.display=='none')style.display='';

                //ie6 bug
                /*  if(ood.browser.ie&&ood.browser.ver<=6){
                    t=style.wordWrap=='normal';
                    ood.asyRun(function(){
                        style.wordWrap=t?'break-word':'normal'
                    })
                }*/
                showEffects = ignoreEffects ? null : showEffects ? showEffects : ood.get(ood.UIProfile.getFromDom(o), ['properties', 'showEffects']);
                if (showEffects) showEffects = ood.Dom._getEffects(showEffects, 1);
                if (showEffects) ood.Dom._vAnimate(o, showEffects, callback); else if (callback) callback();
            });
        },
        hide: function (callback, hideEffects, ignoreEffects) {
            var style, vv;
            return this.each(function (o) {
                if (o.nodeType != 1) return;
                var tid = ood.getNodeData(o, '_inthread');
                if (tid) {
                    ood.Thread.abort(tid);
                    ood.setNodeData(o, '_inthread');
                }

                style = o.style;
                vv = ood.getNodeData(o);
                var fun = function () {
                    if (vv._oodhide !== 1) {
                        vv._position = style.position;
                        vv._visibility = style.visibility;
                        vv._top = style.top;
                        vv._left = style.left;
                        vv._oodhide = 1;
                    }
                    if (style.position != 'absolute') style.position = 'absolute';
                    style.visibility = "hidden";
                    style.top = style.left = ood.Dom.HIDE_VALUE;

                    if (callback) callback();
                };
                hideEffects = ignoreEffects ? null : hideEffects ? hideEffects : ood.get(ood.UIProfile.getFromDom(o), ['properties', 'hideEffects']);
                if (hideEffects) hideEffects = ood.Dom._getEffects(hideEffects, 0);
                if (hideEffects) ood.Dom._vAnimate(o, hideEffects, fun); else fun();
            });
        },
        cssRegion: function (region, triggerEvent) {
            var self = this;
            if (typeof region == 'object') {
                var i, t, m, node = self.get(0), dom = ood.Dom, f = dom._setUnitStyle, m = {};
                for (var j = 0, c = dom._boxArr; i = c[j++];)
                    m[i] = ((i in region) && region[i] !== null) ? f(node, i, region[i]) : false;
                if (triggerEvent) {
                    var f = dom.$hasEventHandler;
                    if (f(node, 'onsize') && (m.width || m.height)) self.onSize(true, {
                        width: m.width,
                        height: m.height
                    });
                    if (f(node, 'onmove') && (m.left || m.top)) self.onMove(true, {left: m.left, top: m.top});
                }
                return self;
            } else {
                var offset = region, parent = triggerEvent,
                    pos = offset ? self.offset(null, parent) : self.cssPos(),
                    size = self.cssSize();
                return {
                    left: pos.left,
                    top: pos.top,
                    width: size.width,
                    height: size.height
                };
            }
        },
        //for quick size
        cssSize: function (size, triggerEvent) {
            var self = this, node = self.get(0), r, dom = ood.Dom, f = dom._setUnitStyle, b1, b2;
            if (node) {
                if (size) {
                    var t;
                    b1 = size.width !== null ? f(node, 'width', size.width) : false;
                    b2 = size.height !== null ? f(node, 'height', size.height) : false;
                    if (triggerEvent && (b1 || b2) && dom.$hasEventHandler(node, 'onsize')) self.onSize(true, {
                        width: b1,
                        height: b2
                    });
                    r = self;
                } else
                    r = {width: self._W(node, 1) || 0, height: self._H(node, 1)};
                return r;
            } else {
                return size ? self : {};
            }
        },
        //for quick move
        cssPos: function (pos, triggerEvent) {
            var node = this.get(0),
                dom = ood.Dom,
                css = ood.CSS,
                f = dom._setUnitStyle,
                b1, b2, r;
            if (pos) {
                var t;
                b1 = pos.left != null ? f(node, 'left', pos.left) : false;
                b2 = pos.top !== null ? f(node, 'top', pos.top) : false;
                if (triggerEvent && (b1 || b2) && dom.$hasEventHandler(node, 'onmove')) this.onMove(true, {
                    left: b1,
                    top: b2
                });
                r = this;
            }
            // get always returns to px
            else {
                f = dom.getStyle;
                r = {left: css.$px(f(node, 'left'), node), top: css.$px(f(node, 'top'), node)};
            }
            node = null;
            return r;
        },
        /*
        +--------------------------+
        |margin                    |
        | #----------------------+ |
        | |border                | |
        | | +------------------+ | |
        | | |padding           | | |
        | | | +--------------+ | | |
        | | | |   content    | | | |

        # is the offset position in EUSUI
        */
        offset: function (pos, boundary, original) {
            var r, t,
                browser = ood.browser,
                ns = this,
                node = ns.get(0),
                keepNode = node,
                parent = node.parentNode,
                op = node.offsetParent,
                doc = node.ownerDocument,
                dd = doc.documentElement,
                db = doc.body,
                _d = /^inline|table.*$/i,
                getStyle = ood.Dom.getStyle,
                fixed = getStyle(node, "position") == "fixed",

                me = arguments.callee,
                add = me.add || (me.add = function (pos, l, t) {
                    pos.left += parseFloat(l) || 0;
                    pos.top += parseFloat(t) || 0;
                }),
                border = me.border || (me.border = function (node, pos) {
                    add(pos, getStyle(node, 'borderLeftWidth'), getStyle(node, 'borderTopWidth'));
                }),
                TTAG = me.TTAG || (me.TTAG = {TABLE: 1, TD: 1, TH: 1}),
                HTAG = me.HTAG || (me.HTAG = {BODY: 1, HTML: 1}),
                posDiff = me.posDiff || (me.posDiff = function (o, target) {
                    var cssPos = o.cssPos(), absPos = o.offset(null, target);
                    return {left: absPos.left - cssPos.left, top: absPos.top - cssPos.top};
                });

            boundary = boundary ? ood(boundary).get(0) : doc;

            if (pos) {
                //all null, return dir
                if (pos.left === null && pos.top === null) return ns;
                var d = posDiff(ns, boundary);
                ns.cssPos({
                    left: pos.left === null ? null : (pos.left - d.left),
                    top: pos.top === null ? null : (pos.top - d.top)
                });
                r = ns;
            } else {
                //for IE, firefox3(except document.body)
                if (!(ood.browser.gek && node === document.body) && node.getBoundingClientRect) {
                    t = ood.Dom.$getBoundingClientRect(node, original);
                    pos = {left: t.left, top: t.top};
                    if (boundary.nodeType == 1 && boundary !== document.body)
                        add(pos, -(t = ood.Dom.$getBoundingClientRect(boundary, original)).left + boundary.scrollLeft, -t.top + boundary.scrollTop);
                    else {
                        // old:
                        // add(pos, (dd.scrollLeft||db.scrollLeft||0)-dd.clientLeft, (dd.scrollTop||db.scrollTop||0)-dd.clientTop);

                        // new:
                        // getBoundingClientRect returns different value in different browser
                        // some include window.scrollX/Y, others do not include
                        // we have to use a base div {left:0,top:0} to do offset, to replace "scrollXXX" offset solution
                        var base = ood.Dom.getEmptyDiv();
                        base.css({left: 0, top: 0, position: 'absolute'});
                        var basRect = ood.Dom.$getBoundingClientRect(base.get(0), original);
                        base.css({left: ood.Dom.HIDE_VALUE, top: ood.Dom.HIDE_VALUE});

                        // var basRect=ood.Dom.$getBoundingClientRect(db, original);
                        add(pos, -basRect.left, -basRect.top);
                    }
                } else {
                    pos = {left: 0, top: 0};
                    add(pos, node.offsetLeft, node.offsetTop);
                    //get offset, stop by boundary or boundary.offsetParent
                    while (op && op != boundary && op != boundary.offsetParent) {
                        add(pos, op.offsetLeft, op.offsetTop);
                        if (browser.kde || (browser.gek && !TTAG[op.nodeName]))
                            border(op, pos);
                        if (!fixed && getStyle(op, "position") == "fixed")
                            fixed = true;
                        if (op.nodeName != 'BODY')
                            keepNode = op.nodeName == 'BODY' ? keepNode : op;
                        op = op.offsetParent;
                    }

                    //get scroll offset, stop by boundary
                    while (parent && parent.nodeName && parent != boundary && !HTAG[parent.nodeName]) {
                        if (!_d.test(getStyle(parent, "display")))
                            add(pos, -parent.scrollLeft, -parent.scrollTop);
                        if (browser.gek && getStyle(parent, "overflow") != "visible")
                            border(parent, pos);
                        parent = parent.parentNode;
                    }
                    if ((browser.gek && getStyle(keepNode, "position") != "absolute"))
                        add(pos, -db.offsetLeft, -db.offsetTop);
                    if (fixed)
                        add(pos, dd.scrollLeft || db.scrollLeft || 0, dd.scrollTop || db.scrollTop || 0);
                }
                r = pos;
            }
            return r;
        },
//class and src
        hasClass: function (name) {
            var i, l, isReg = ood.isReg(name), arr = ood.Dom._getClass(this.get(0)).split(/\s+/);
            if (isReg) {
                for (i = 0, l = arr.length; i < l; i++) {
                    if (name.test(arr[i])) {
                        return true;
                    }
                }
            } else {
                return ood.arr.indexOf(arr, name + "") != -1;
            }
            return false;
        },
        addClass: function (name) {
            if (!name) return this;
            var arr, i, l, me = arguments.callee, reg = (me.reg || (me.reg = /\s+/)), t, ok,
                arr2 = (name + "").split(reg);
            if (!arr2.length) return this;

            return this.each(function (o) {
                ok = 0;
                arr = ood.Dom._getClass(o).split(reg);
                t = [];
                for (i = 0, l = arr.length; i < l; i++) if (arr[i]) t.push(arr[i]);
                for (i = 0, l = arr2.length; i < l; i++) {
                    if (arr2[i] && ood.arr.indexOf(arr, arr2[i]) == -1) {
                        ok = 1;
                        t.push(arr2[i]);
                    }
                }
                ;
                if (ok) ood.Dom._setClass(o, t.join(" "));
            });
        },
        removeClass: function (name) {
            var arr, i, l, isReg = ood.isReg(name), me = arguments.callee, reg = (me.reg || (me.reg = /\s+/)), ok,
                arr2;
            if (!isReg) {
                arr2 = (name + "").split(reg);
                if (!arr2.length) return this;
            }
            return this.each(function (o) {
                ok = 0;
                arr = ood.Dom._getClass(o).split(reg);
                if (!isReg) {
                    for (i = 0, l = arr2.length; i < l; i++) {
                        if (ood.arr.indexOf(arr, arr2[i]) != -1) {
                            ok = 1;
                            ood.arr.removeValue(arr, arr2[i]);
                        }
                    }
                } else {
                    ood.filter(arr, function (o, i) {
                        if (name.test(o)) {
                            ok = 1;
                            return false;
                        }
                    });
                }
                if (ok) ood.Dom._setClass(o, arr.join(" "));
            });
        },
        replaceClass: function (regexp, replace) {
            var n, r;
            return this.each(function (o) {
                r = (n = ood.Dom._getClass(o)).replace(regexp, replace);
                if (n != r) ood.Dom._setClass(o, r);
            });
        },

        toggleClass:function(tag, isAdd){
           return this.tagClass(tag,isAdd);
        },

        tagClass: function (tag, isAdd) {
            var self = this,
                me = arguments.callee,
                r1 = me["_r1_" + tag] || (me["_r1_" + tag] = new RegExp("([-\\w]+" + tag + "[-\\w]*)")),
                r2 = me["_r2"] || (me["_r2"] = /([-\w]+)/g);
            self.removeClass(r1);
            isAdd = false !== isAdd;
            var r = isAdd ? self.replaceClass(r2, '$1 $1' + tag) : self;

            //fix for ie67
            if (ood.__iefix2 && (tag == "-checked" || tag == "-fold" || tag == "-expand")) {
                this.each(function (n) {
                    var arr = ood.Dom._getClass(n).split(/\s+/);
                    if (ood.arr.indexOf(arr, 'oodfont') != -1 || ood.arr.indexOf(arr, 'oodcon') != -1) {
                        ood.arr.each(arr, function (s) {
                            //It has 'xxxx' and 'xxxx-checked'
                            if (ood.__iefix2[s + (isAdd ? '' : tag)] && ood.__iefix2[isAdd ? s.replace(new RegExp(tag + '$'), '') : s]) {
                                ood(n).html(ood.__iefix2[s.replace(new RegExp(tag + '$'), '') + (isAdd ? tag : '')]);
                                return false;
                            }
                        });
                    }
                });
            }
            return r;
        },
//events:
        /*
        $addEvent('onClick',fun,'idforthisclick';)
        $addEvent([['onClick',fun,'idforthisclick'],[...]...])

        do:
            add onclick to dom
            append fun to ood.$cache.profileMap.id.events.onClick array
            append 'onclick' to ood.$cache.profileMap.id.add array
        */

        $addEventHandler: function (name) {
            var event = ood.Event,
                type,
                handler = event.$eventhandler;
            return this.each(function (o) {
                if (o.nodeType == 3) return;
                //set to purge map
                ood.setNodeData(o, ['eHandlers', 'on' + event._eventMap[name]], handler);

                //set to dom node
                if (type = event._eventHandler[name]) {
                    ood.setNodeData(o, ['eHandlers', type], handler);
                    event._addEventListener(o, event._eventMap[name], handler);

                    if (ood.browser.isTouch && type == 'onmousedown') {
                        ood.setNodeData(o, ['eHandlers', 'onoodtouchdown'], handler);
                        event._addEventListener(o, "oodtouchdown", handler);
                    }
                }
            });
        },
        /*
        'mousedown' -> 'dragbegin'
        'mouseover' -> 'dragenter'
        'mouseout' -> 'dragleave'
        'mouseup' -> 'drop'
        */
        $removeEventHandler: function (name) {
            var event = ood.Event,
                handler = event.$eventhandler,
                handler3 = event.$eventhandler3,
                type;
            return this.each(function (o) {
                //remove from dom node
                if (type = event._eventHandler[name]) {
                    event._removeEventListener(o, type, handler);
                    event._removeEventListener(o, type, handler3);

                    if (ood.browser.isTouch && type == 'onmousedown') {
                        event._removeEventListener(o, 'oodtouchdown', handler);
                    }
                }
                //remove from purge map
                if (o = ood.getNodeData(o, 'eHandlers')) {
                    type = 'on' + event._eventMap[name];
                    delete o[type];
                    if (ood.browser.isTouch && type == 'onmousedown') {
                        delete o['onoodtouchdown'];
                    }
                }
            });
        },

        on: function (name, fun, label, index) {
            return this.$addEvent(name,fun,label,index);
          },

        $addEvent: function (name, fun, label, index) {
            var self = this,
                event = ood.Event,
                arv = ood.arr.removeValue,
                ari = ood.arr.insertAny,
                id, c, t, m;

            if (!index && index !== 0) index = -1;

            if (typeof label == 'string')
                label = "$" + label;
            else label = undefined;

            self.$addEventHandler(name).each(function (o) {
                if (o.nodeType == 3) return;

                if (!(id = event.getId(o)) && o !== window && o !== document)
                    id = o.id = ood.Dom._pickDomId();

                if (!(c = ood.$cache.profileMap[id]))
                    c = new ood.DomProfile(id);

                t = c.events || (c.events = {});
                m = t[name] || (t[name] = []);

                //if no label input, clear all, and add a single
                if (label === undefined) {
                    m.length = 0;
                    m = t[name] = [];
                    index = -1;
                    label = '_';
                }
                m[label] = fun;
                arv(m, label);
                if (index == -1) m[m.length] = label;
                else
                    ari(m, label, index);

                if (ood.Event && (c = ood.Event._getProfile(id)) && c.clearCache)
                    c.clearCache();
            });

            return self;
        },
        /*
        $removeEvent('onClick','idforthisclick')
        $removeEvent('onClick')
            will remove all onClick in ood.$cache.profileMap.id.events.
        $removeEvent('onClick',null,true)
            will remove all onClick/beforeClick/afterClick in ood.$cache.profileMap.id.events.
        */
        $removeEvent: function (name, label, bAll) {
            var self = this, c, t, k, id, i, type,
                event = ood.Event,
                dom = ood.$cache.profileMap,
                type = event._eventMap[name];

            self.each(function (o) {
                if (!(id = event.getId(o))) return;
                if (!(c = dom[id])) return;
                if (!(t = c.events)) return;
                if (bAll)
                    ood.arr.each(event._getEventName(type), function (o) {
                        delete t[o];
                    });
                else {
                    if (typeof label == 'string') {
                        label = '$' + label;
                        if (k = t[name]) {
                            delete k[label];
                            if (ood.arr.indexOf(k, label) != -1)
                                ood.arr.removeValue(k, label);
                        }
                    } else
                        delete t[name];
                }

                if (ood.Event && (c = ood.Event._getProfile(id)) && c.clearCache)
                    c.clearCache();
            });

            return self;
        },
        $getEvent: function (name, label) {
            var id;
            if (!(id = ood.Event.getId(this.get(0)))) return;

            if (label)
                return ood.get(ood.$cache.profileMap, [id, 'events', name, '$' + label]);
            else {
                var r = [], arr = ood.get(ood.$cache.profileMap, [id, 'events', name]);
                ood.arr.each(arr, function (o, i) {
                    r[r.length] = {o: arr[o]};
                });
                return r;
            }
        },
        $clearEvent: function () {
            return this.each(function (o, i) {
                var event = ood.Event,
                    handler = event.$eventhandler,
                    handler3 = event.$eventhandler3,
                    type;

                if (!(i = event.getId(o))) return;
                if (!(i = ood.$cache.profileMap[i])) return;
                if (i.events) {
                    ood.each(i.events, function (f, name) {
                        type = ood.Event._eventMap[name];
                        if (type) {
                            event._removeEventListener(o, type, handler);
                            event._removeEventListener(o, type, handler3);
                        }
                    });
                    ood.breakO(i.events, 2);
                    delete i.events;
                }
                ood.set(ood.$cache.domPurgeData, [o.$xid, 'eHandlers'], {});
            });
        },
        $fireEvent: function (name, args) {
            var type = ood.Event._eventMap[name],
                t, s = 'on' + type,
                handler,
                hash,
                me = arguments.callee,
                f = ood.Event.$eventhandler,
                f1 = me.f1 || (me.f1 = function () {
                    this.returnValue = false
                }),
                f2 = me.f2 || (me.f2 = function () {
                    this.cancelBubble = true
                });
            return this.each(function (o) {
                if (!(handler = ood.getNodeData(o, ['eHandlers', s]))) return;
                if ('blur' == type || 'focus' == type) {
                    try {
                        o[type]()
                    } catch (e) {
                    }
                } else {
                    hash = ood.copy(args);
                    ood.merge(hash, {
                        type: type,
                        target: o,
                        button: 1,
                        $oodevent: true,
                        $oodtype: name,
                        preventDefault: f1,
                        stopPropagation: f2
                    }, 'all');
                    handler.call(o, hash);
                }
            });
        },
        nativeEvent: function (name) {
            return this.each(function (o) {
                if (o.nodeType === 3 || o.nodeType === 8) return;
                try {
                    o[name]()
                } catch (e) {
                }
            });
        },

//functions
        $canFocus: function () {
            var me = arguments.callee, getStyle = ood.Dom.getStyle,
                map = me.map || (me.map = {a: 1, input: 1, select: 1, textarea: 1, button: 1, object: 1}), t, node;
            return !!(
                (node = this.get(0)) &&
                node.focus &&
                //IE bug: It can't be focused with 'default tabIndex 0'; but if you set it to 0, it can be focused.
                //So, for cross browser, don't set tabIndex to 0
                (((t = map[node.nodeName.toLowerCase()]) && !(parseInt(node.tabIndex, 10) <= -1)) || (!t && parseInt(node.tabIndex, 10) >= (ood.browser.ie ? 1 : 0))) &&
                getStyle(node, 'display') != 'none' &&
                getStyle(node, 'visibility') != 'hidden' &&
                node.offsetWidth > 0 &&
                node.offsetHeight > 0
            );
        },
        focus: function (force) {
            var ns = this;
            if (force || ns.$canFocus())
                try {
                    ns.get(0).focus()
                } catch (e) {
                }
            return ns;
        },
        blur: function () {
            var n = this.get(0);
            if (!n) return;
            n.blur();
            if (document.activeElement === n) {
                ood.asyRun(function () {
                    ood('body').append(n = ood.create("<button style='position:absolute;width:1px;height:1px;left:-1000px;'></button>"));
                    n.focus();
                    n.remove();
                });
            }
        },
        setSelectable: function (value) {
            var me = arguments.callee, cls;
            this.removeClass("ood-ui-selectable").removeClass("ood-ui-unselectable");
            this.addClass(value ? "ood-ui-selectable" : "ood-ui-unselectable");
            return this.each(function (o) {
                if (ood.browser.ie && ood.browser.ver < 10)
                    ood.setNodeData(o, "_onoodsel", value ? "true" : "false");
            })
        },
        contentBox: function (d) {
            return (ood.browser.ie || ood.browser.opr) ?
                !/BackCompat|QuirksMode/.test((d || document).compatMode) :
                (this.css("box-sizing") || this.css("-moz-box-sizing")) == "content-box";
        },
        setInlineBlock: function () {
            var ns = this;
            if (ood.browser.gek) {
                if (ood.browser.ver < 3)
                    ns.css('display', '-moz-inline-block').css('display', '-moz-inline-box').css('display', 'inline-block');
                else
                    ns.css('display', 'inline-block');
            } else if (ood.browser.ie && ood.browser.ver <= 6)
                ns.css('display', 'inline-block').css({display: 'inline', zoom: '1'});
            else
                ns.css('display', 'inline-block');
            return ns;
        },
        topZindex: function (flag) {
            //set the minimum to 1000
            var i = 1000, j = 0, k, node = this.get(0), p = node.offsetParent, t, o, style;
            if (ood.browser.ie && (!p || (p.nodeName + "").toUpperCase() == "HTML")) {
                p = ood("body").get(0);
            }
            if (node.nodeType != 1 || !p) return 1;

            t = p.childNodes;
            for (k = 0; o = t[k]; k++) {
                style = o.style;
                if (o == node || o.nodeType != 1 || !o.$xid || (style && style.display == 'none') || (style && style.visibility == 'hidden') || o.zIndexIgnore || ood.getNodeData(o, 'zIndexIgnore')) continue;
                j = parseInt(style && style.zIndex, 10) || 0;
                i = i > j ? i : j;
            }
            i++;
            if (i >= ood.Dom.TOP_ZINDEX)
                ood.Dom.TOP_ZINDEX = i + 1;

            if (flag)
                node.style.zIndex = i;
            else {
                j = parseInt(node.style.zIndex, 10) || 0;
                return i > j ? i : j;
            }
            return this;
        },
        /*
        dir:true for next, false for prev
        inn:true for include the inner node
        set:true for give focus
        */
        nextFocus: function (downwards, includeChild, setFocus, pattern) {
            downwards = ood.isBool(downwards) ? downwards : true;
            var self = this.get(0), node = this.$iterator('', downwards, includeChild, function (node) {
                return node !== self && (!pattern || (node.id && pattern.test(node.id))) && ood([node]).$canFocus()
            });
            if (!node.isEmpty() && setFocus !== false) node.focus();
            self = null;
            return node;
        },
        fullScreen: function (full) {
            var e = this.get(0), d = document;
            if (e) {
                if (e === d) e = d.documentElement;
                var requestMethod = full !== false ? (e.requestFullScreen || e.webkitRequestFullScreen || e.mozRequestFullScreen || e.msRequestFullScreen)
                    : (d.exitFullscreen || d.mozCancelFullScreen || d.webkitExitFullscreen || d.webkitExitFullscreen);
                if (requestMethod) {
                    requestMethod.call(full !== false ? e : d);
                } else if (typeof window.ActiveXObject !== "undefined") {
                    var wscript = new ActiveXObject("WScript.Shell");
                    if (wscript !== null) {
                        wscript.SendKeys("{F11}");
                    }
                }
            }
        },
        /*
        args:{
            width:[0,100],
            height:[0,100],
            left:[0,100]
            top:[0,100]
            opacity:[0,1],
            backgroundColor:['#ffffff','#000000']
            scrollTop:[0,100]
            scrollLeft:[0,100]
            fontSize:[12,18]
        }
        */
        animate: function (endpoints, onStart, onEnd, duration, step, type, threadid, unit, restore, times, _goback) {
            var self = this, f, map = {left: 1, top: 1, right: 1, bottom: 1, width: 1, height: 1},
                prf = ood.$cache.profileMap[self.id()],
                ctrl = prf ? prf['ood.DomProfile'] ? ood(prf) : prf.boxing() : null,
                css = ood.CSS,
                tween = ood.Dom.$AnimateEffects,
                _get = function (node, ctrl, key, t) {
                    return (map[key] && ctrl && ood.isFun(ctrl[t = 'get' + ood.str.initial(key)])) ? ctrl[t](key) : node[key] ? node[key]() : node.css(key);
                },
                _set = function (node, ctrl, key, value, t) {
                    return (map[key] && ctrl && ood.isFun(ctrl[t = 'set' + ood.str.initial(key)])) ? ctrl[t](value) : node[key] ? node[key](value) : node.css(key, value);
                },
                color = function (from, to, curvalue) {
                    if (typeof from != 'string' || typeof to != 'string') return '#fff';
                    if (curvalue < 0) return from;
                    if (curvalue > 1) return to;

                    var f, f1, f2, f3;
                    f = function (str) {
                        return (str.charAt(0) != '#') ? ('#' + str) : str;
                    };
                    from = f(from);
                    to = f(to);

                    f1 = function (str, i, j) {
                        return parseInt(str.slice(i, j), 16) || 0;
                    };
                    f2 = function (o) {
                        return {red: f1(o, 1, 3), green: f1(o, 3, 5), blue: f1(o, 5, 7)}
                    };
                    from = f2(from);
                    to = f2(to);

                    f3 = function (from, to, value, c) {
                        var r = from[c] + Math.round(parseFloat(value * (to[c] - from[c])) || 0);
                        return (r < 16 ? '0' : '') + r.toString(16)
                    };
                    return '#' + f3(from, to, curvalue, 'red') + f3(from, to, curvalue, 'green') + f3(from, to, curvalue, 'blue');
                };
            if (!endpoints) {
                if (onEnd) ood.tryF(onEnd);
                return;
            } else {
                // adjust endpoints
                ood.each(endpoints, function (o, i) {
                    if (!ood.isFun(o)) {
                        if (!ood.isArr(o) || o.length === 1) o = [_get(self, ctrl, i), o];
                        endpoints[i] = o;
                    }
                });
            }
            var parmsBak = endpoints;
            // clone it now
            endpoints = ood.clone(endpoints);

            // Estimate duration by steps
            if ((step || 0) > 0)
                duration = step * 16;
            else
                duration = duration || 200;
            times = times || 1;
            if ((type || "").indexOf('-') != -1) type = type.replace(/\-(\w)/g, function (a, b) {
                return b.toUpperCase()
            });
            type = (type in tween) ? type : 'circIn';

            var starttime, node = self.get(0), fun = function (tid) {
                var offtime = ood.stamp() - starttime, curvalue, u, eu, su, s, e;
                if (offtime >= duration) offtime = duration;
                ood.each(endpoints, function (o, i) {
                    curvalue = tween[type](duration, offtime);
                    if (typeof o == 'function') o.call(self, curvalue);
                    else {
                        s = o[0];
                        e = o[1];
                        u = o[2];
                        if (ood.str.endWith(i.toLowerCase(), 'color')) {
                            curvalue = color(s, e, curvalue);
                        } else {
                            if (!ood.isFinite(e)) {
                                u = e.replace(/[-\d.]*/, '');
                                eu = u || 'px';
                                if (!ood.isFinite(s)) {
                                    su = s.replace(/[-\d.]*/, '') || 'px';
                                    if (su != eu) {
                                        if (su == 'em' && eu == 'px') {
                                            s = css.$em2px(s, node);
                                        } else if (su == 'px' && eu == 'em') {
                                            s = css.$px2em(s, node);
                                        }
                                    }
                                }
                            }
                            s = parseFloat(s) || 0;
                            e = parseFloat(e) || 0;
                            curvalue = ood.toFixedNumber(s + (e - s) * curvalue, 6);
                        }
                        curvalue += u || unit || '';
                        _set(self, ctrl, i, curvalue)
                    }
                });
                if (offtime == duration) {
                    if (restore && !_goback) {
                        starttime = ood.stamp();
                        _goback = 1;
                        ood.each(endpoints, function (v, k) {
                            if (!ood.isFun(v)) {
                                k = v[0];
                                v[0] = v[1];
                                v[1] = k
                            }
                        });
                    } else {
                        if (times == -1 || times > 0) {
                            starttime = ood.stamp();
                            if (times > 0) times -= 1;
                            if (_goback) {
                                _goback = 0;
                                ood.each(endpoints, function (v, k) {
                                    if (!ood.isFun(v)) {
                                        k = v[0];
                                        v[0] = v[1];
                                        v[1] = k
                                    }
                                });
                            }
                        }
                    }
                    if (!times) {
                        ood.Thread.abort(tid, 'normal');
                    }
                    return false;
                }
            }, funs = [fun];

            var tid = ood.getNodeData(node, '_inthread');
            if (tid && ood.Thread.isAlive(tid)) {
                ood.Thread.abort(tid, 'force');
                ood.setNodeData(node, '_inthread', null);
            }
            var reset = ood.getNodeData(node, '_animationreset');
            if (typeof reset == "function") {
                reset();
                ood.setNodeData(node, '_animationreset', null);
            }
            // allow custom threadid, except existing one
            return ood.Thread((!threadid || ood.Thread.get(threadid)) ? ood.id() : threadid, funs, 0, null, function (tid) {
                ood.setNodeData(node, '_inthread', tid);
                starttime = ood.stamp();
                ood.setNodeData(node, '_animationreset', function () {
                    ood.merge(endpoints, parmsBak, 'all');
                    starttime = ood.stamp();
                    fun();
                });
                return ood.tryF(onStart, arguments, this);
            }, function (tid, flag) {
                //maybe destroyed
                if (node && node.$xid) {
                    ood.setNodeData(node, '_inthread', null);
                    ood.setNodeData(node, '_animationreset', null);
                }
                if ('force' != flag)
                    ood.tryF(onEnd, arguments, this);
            }, true);
        },
        pop: function (pos, type, parent, trigger, group) {
            var ns = this, id = ood.stamp() + ":" + ns.xid();
            ns.popToTop(pos, type || "outer", parent).setBlurTrigger(id, function () {
                if (typeof(trigger) == "function") ood.tryF(trigger);
                else ns.hide();
            });
            return id;
        },
        // pop to the top layer
        popToTop: function (pos, type, parent, callback, showEffects, ignoreEffects) {
            var region, target = this, t;
            parent = ood(parent);
            if (parent.isEmpty())
                parent = ood('body');

            //prepare
            target.css({
                position: 'absolute',
                left: ood.Dom.HIDE_VALUE,
                top: ood.Dom.HIDE_VALUE,
                display: 'block',
                zIndex: ood.Dom.TOP_ZINDEX++
            });

            //ensure show target on the top of the other elements with the same zindex
            //parent.get(0).appendChild(target.get(0));
            target.css({left: 0, top: 0, visibility: 'hidden', display: 'block'});
            parent.append(target);

            //show
            target.cssPos(ood.Dom.getPopPos(pos, type, target, parent)).css({visibility: 'visible'});

            showEffects = ignoreEffects ? null : showEffects ? showEffects : ood.get(ood.UIProfile.getFromDom(target), ['properties', 'showEffects']);
            if (showEffects) showEffects = ood.Dom._getEffects(showEffects, 1);
            if (showEffects) ood.Dom._vAnimate(target, showEffects, callback); else if (callback) callback();
            return this;
        },
        hoverPop: function (node, type, beforePop, beforeHide, parent, groupid, showEffects, hideEffects) {
            node = ood(node);
            if (showEffects) showEffects = ood.Dom._getEffects(showEffects, 1);
            if (hideEffects) hideEffects = ood.Dom._getEffects(hideEffects, 0);
            if (!ood.isDefined(type)) type = 'outer';

            var aysid = groupid || (this.xid() + ":" + node.xid()), self = this;
            this.onMouseover(type === null ? null : function (prf, e, src) {
                if (e.$force) return;
                ood.resetRun(aysid, null);
                var ignore = ood.getData([aysid, '$ui.hover.pop'])
                    && ood.getNodeData(node.get(0) || "empty", '$ui.hover.parent') == src;
                if (!ignore) {
                    ood.setData([aysid, '$ui.hover.pop'], 1);
                    ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', src);
                    if (!beforePop || false !== beforePop(prf, node, e, src)) {
                        node.popToTop(src, type, parent, showEffects);
                        node.onMouseover(function () {
                            self.onMouseover(true)
                        }, 'hoverPop').onMouseout(function () {
                            self.onMouseout(true)
                        }, 'hoverPop');
                    }
                }
            }, aysid).onMouseout(type === null ? null : function (prf, e, src) {
                if (e.$force) return;
                ood.resetRun(aysid, function () {
                    ood.setData([aysid, '$ui.hover.pop']);
                    ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', 0);
                    if (!beforeHide || false !== beforeHide(prf, node, e, src, 'host')) {
                        node.hide(null, hideEffects);
                        node.onMouseover(null, 'hoverPop').onMouseout(null, 'hoverPop');
                    }
                });
            }, aysid);
            if (node) {
                node.onMouseover(type === null ? null : function (e) {
                    if (e.$force) return;
                    ood.resetRun(aysid, null);
                }, aysid).onMouseout(type === null ? null : function (prf, e, src) {
                    if (e.$force) return;
                    ood.resetRun(aysid, function () {
                        ood.setData([aysid, '$ui.hover.pop']);
                        ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', 0);
                        if (!beforeHide || false !== beforeHide(prf, node, e, src, 'pop')) {
                            node.hide(null, hideEffects);
                            node.onMouseover(null, 'hoverPop').onMouseout(null, 'hoverPop');
                        }
                    });
                }, aysid);
            }
            node.css('display', 'none');
            return this;
        },
        //for remove obj when blur
        setBlurTrigger: function (id, trigger/*[false] for anti*/, group /*keep the original refrence*/,
                                  /*two inner params */ checkChild, triggerNext) {
            var ns = this,
                doc = document,
                sid = '$blur_triggers$',
                fun = ood.Dom._blurTrigger || (ood.Dom._blurTrigger = function (p, e) {
                    var p = ood.Event.getPos(e),
                        arr = arguments.callee.arr,
                        srcN = ood.Event.getSrc(e),
                        a = ood.copy(arr),
                        b, pos, w, h, v;
                    //filter first
                    ood.arr.each(a, function (i) {
                        b = true;
                        if (!(v = arr[i].target)) b = false;
                        else
                            v.each(function (o) {
                                if (o !== window && o !== document && !ood.Dom.byId(o.id))
                                    return b = false;
                            });
                        if (!b) {
                            delete arr[i];
                            ood.arr.removeValue(arr, i);
                        }
                        ;
                    });
                    a = ood.copy(arr);
                    ood.arr.each(a, function (i) {
                        v = arr[i];
                        if (!v) return;

                        b = true;
                        var isChild = function () {
                            var nds = v.target.get();
                            while (srcN && srcN.nodeName && srcN.nodeName != "BODY" && srcN.nodeName != "HTML") {
                                if (ood.arr.indexOf(nds, srcN) != -1)
                                    return true;
                                srcN = srcN.parentNode;
                            }
                        };
                        if (!v.checkChild || isChild()) {
                            v.target.each(function (o) {
                                if (o.parentNode && (w = o.offsetWidth) && (h = o.offsetHeight)) {
                                    pos = ood([o]).offset();
                                    if (p.left >= pos.left && p.top >= pos.top && p.left <= (pos.left + w) && p.top <= (pos.top + h)) {
                                        return b = false;
                                    }
                                }
                            });
                        }

                        isChild = null;

                        // anti trigger
                        if (!b && !ood.isFun(v.trigger))
                            return false;

                        if (b) {
                            delete arr[i];
                            ood.arr.removeValue(arr, i);
                            ood.tryF(v.trigger, [p, e], v.target);
                            v = null;
                        } else if (v.stopNext) {
                            //if the top layer popwnd cant be triggerred, prevent the other layer popwnd trigger
                            return false;
                        }
                    }, null, true);
                    srcN = null;
                    a.length = 0;
                }),
                arr = fun.arr || (fun.arr = []),
                target;

            // remove this trigger first
            if (arr[id]) {
                if (trigger === true) {
                    ood.tryF(arr[id].trigger);
                    trigger = false;
                }
                delete arr[id];
                ood.arr.removeValue(arr, id);
            }
            // add trigger
            if (trigger) {
                if (group) {
                    //keep the original refrence
                    if (group['ood.Dom'])
                        target = group;
                    else if (ood.isArr(group)) {
                        target = ood();
                        target._nodes = group;
                    }
                    target.merge(ns);
                } else {
                    target = ns;
                }

                target.each(function (o) {
                    if (!o.id && o !== window && o !== document) o.id = ood.Dom._pickDomId()
                });

                //double link
                arr[id] = {
                    trigger: trigger,
                    target: target,
                    checkChild: !!checkChild,
                    stopNext: !triggerNext
                };
                arr.push(id);

                if (!doc.onmousedown) doc.onmousedown = ood.Event.$eventhandler;
                doc = fun = null;
            }
            return this;
        },
        //for firefox disappeared cursor bug in input/textarea
        $firfox2: function () {
            if (!ood.browser.gek2) return this;
            var ns = this;
            ns.css('overflow', 'hidden');
            ood.asyRun(function () {
                ns.css('overflow', 'auto')
            });
            return ns;
        },
        //IE not trigger dimension change, when change height only in overflow=visible.
        ieRemedy: function () {
            if (ood.browser.ie && ood.browser.ver <= 6) {
                var a1 = this.get(), a2 = [], a3 = [], l = a1.length, style;
                //ood.asyRun(function(){
                for (var i = 0; i < l; i++) {
                    style = a1[i].style;
                    // allow once
                    if (!ood.isSet(a1[i].$ieRemedy)) {
                        if (ood.isSet(style.width)) {
                            a1[i].$ieRemedy = style.width;
                            style.width = ((ood.CSS.$px(a1[i].$ieRemedy, a1[i]) || 0) + 1) + "px";
                        }
                    }
                    /*
                        if((a3[i]=style.WordWrap)=='break-word')
                            style.WordWrap='normal';
                        else
                            style.WordWrap='break-word';
                        */
                }
                ood.asyRun(function () {
                    for (var i = 0; i < l; i++) {
                        if (ood.isSet(a1[i].$ieRemedy)) {
                            a1[i].style.width = a1[i].$ieRemedy;
                            a1[i].removeAttribute('$ieRemedy');
                        }
                        //a1[i].style.WordWrap=a3[i];
                    }
                    a1.length = a2.length = a3.length = 0;
                });
                // });
            }
            return this;
        }
        /*,
        gekRemedy:function(){
            if(ood.browser.gek)
                return this.each(function(o,i){
                    if(i=o.style){
                        var b=i.zIndex||0;
                        i.zIndex=++b;
                        i.zIndex=b;
                    }
                });
        }*/
    },
    Static: {
        HIDE_VALUE: '-10000px',
        TOP_ZINDEX: 10000,

        _boxArr: ood.toArr('width,height,left,top,right,bottom'),
        _cursor: {},

        _pickDomId: function () {
            var id;
            do {
                id = 'ood_' + ood.id()
            } while (document.getElementById(id))
            return id;
        },
        _map: {
            'html': 1,
            'head': 1,
            'body': 1
        },
        //for ie6
        fixPng: function (n) {
            if (ood.browser.ie && ood.browser.ver <= 6) {
                if (n.nodeName == 'IMG' && n.src.toLowerCase().search(/\.png$/) != -1) {
                    var style = n.style;
                    style.height = n.height;
                    style.width = n.width;
                    style.backgroundImage = "none";
                    var t = ((style.filter ? (style.filter + ",") : "") + "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, src=" + n.src + "', sizingMethod='image')").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/[,\s]+/g, ', ');
                    if (ood.browser.ie8) style.msfilter = t;
                    style.filter = t;
                    n.src = ood.ini.img_bg;
                }
                var bgimg = n.currentStyle.backgroundImage || style.backgroundImage,
                    bgmatch = (bgimg || "").toLowerCase().match(/^url[("']+(.*\.png[^\)"']*)[\)"']+[^\)]*$/i);
                if (bgmatch) {
                    style.backgroundImage = "none";
                    var t = ((style.filter ? (style.filter + ",") : "") + "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, src=" + bgmatch[1] + "', sizingMethod='crop')").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/[,\s]+/g, ', ');
                    if (ood.browser.ie8) style.msfilter = t;
                    style.filter = t;
                }
            }
        },
        _getTag: function (n) {
            return n ? n.$xid ? n.$xid : n.nodeType == 1 ? ood.$registerNode(n).$xid : 0 : 0
        },
        _ensureValues: function (obj) {
            var t, i, map = this._map, a = [],
                //can't be obj, or opera will crash
                arr = obj === window
                    ? ['!window']
                    : obj === document
                        ? ['!document']
                        : ood.isArr(obj)
                            ? obj
                            : (obj == '[object NodeList]' || obj == '[object HTMLCollection]')
                                ? ood.toArr(obj)
                                : obj['ood.Dom']
                                    ? obj._nodes
                                    : obj._toDomElems
                                        ? obj._toDomElems()
                                        : typeof obj == 'function'
                                            ? obj()
                                            : [obj];
            for (i = 0; i < arr.length; i++)
                if (t = !(t = arr[i])
                    ? 0
                    : t === window
                        ? '!window'
                        : t === document
                            ? '!document'
                            : (typeof t == 'string' || (t['ood.DomProfile'] && (t = t.domId)))
                                ? t.charAt(0) == '!'
                                    ? t
                                    : this._getTag(map[t] ? document.getElementsByTagName(t)[0] : document.getElementById(t))
                                : ((t = arr[i])['ood.UIProfile'] || t['ood.Template'])
                                    ? t.renderId ? t.renderId : (t.boxing().render() && t.renderId)
                                    : this._getTag(t)
                )
                    a[a.length] = t;
            return a.length <= 1 ? a : this._unique(a);
        },
        _getClass: function (o) {
            return (typeof o.className == "string" && o.className)
                || (typeof o.className.baseVal == "string" && o.className.baseVal)
                || (typeof o.getAttribute !== "undefined" && o.getAttribute("class"))
                || "";
        },
        _setClass: function (o, v) {
            if (typeof o.className == "string") {
                o.className = v;
            } else if (typeof o.className.baseVal == "string") {
                o.className.baseVal = v;
            } else if (typeof o.getAttribute != "undefined") {
                o.setAttribute(v);
            }
        },
        /*
        pos: {left:,top:} or dom element
        parent:parent node
        type:1,2,3,4
        */
        getPopPos: function (pos, type, target, parent) {
            var result = {left: 0, top: 0};
            if (!pos) {
                return result;
            } else if (ood.isEvent(pos)) {
                return ood.Event.getPos(pos);
            } else {
                var region, node, abspos, t, box;
                if ((parent = ood(parent)).isEmpty())
                    parent = ood('body');
                if (pos['ood.UI'] || pos['ood.UIProfile'] || pos['ood.Dom'] || pos.nodeType == 1 || typeof pos == 'string') {
                    if (typeof(type) != "function") {
                        type = (type || 12) + '';
                    }
                    node = ood(pos);
                    //base region
                    abspos = node.offset(null, parent);
                    region = {
                        left: abspos.left,
                        top: abspos.top,
                        width: node.offsetWidth(),
                        height: node.offsetHeight()
                    };
                } else {
                    if (typeof(type) != "function") {
                        type = type ? '3' : '0';
                    }
                    t = type == '0' ? 0 : 8;
                    region = pos.region || {
                        left: pos.left - t,
                        top: pos.top - t,
                        width: t * 2,
                        height: t * 2
                    };
                }


                //window edge
                t = (parent.get(0) === document.body || parent.get(0) === document || parent.get(0) === window) ? ood.win : parent;
                box = {};

                box.left = t.scrollLeft();
                box.top = t.scrollTop();
                box.width = t.width() + box.left;
                box.height = t.height() + box.top;

                if (t == ood.win && ood.ini.$zoomScale) {
                    for (var i in box)
                        box[i] /= ood.ini.$zoomScale;
                }

                /*
                    type:1
                        +------------------+    +------------------+
                        |        3         |    |        4         |
                        +--------------+---+    +---+--------------+
                        |              |            |              |
                        |              |            |              |
                        +--------------+---+    +---+--------------+
                        |        1         |    |        2         |
                        +------------------+    +------------------+
                    type:2
                                             +---+              +---+
                                             |   |              |   |
                    +---+--------------+---+ |   +--------------+   |
                    |   |              |   | | 3 |              | 4 |
                    | 2 |              | 1 | |   |              |   |
                    |   +--------------+   | +---+--------------+---+
                    |   |              |   |
                    +---+              +---+
                    type:3
                                             +---+              +---+
                                             | 3 |              | 4 |
                        +--------------+     +---+--------------+---+
                        |              |         |              |
                        |              |         |              |
                    +---+--------------+---+     +--------------+
                    | 2 |              | 1 |
                    +---+              +---+
                    type:4
                                         +------------------+
                                         | 3                |
                    +--------------+---+ |   +--------------+ +----+--------------+ +--------------+----+
                    |              |   | |   |              | |    |              | |              |    |
                    |              |   | |   |              | |    |              | |              |    |
                    +--------------+   | +---+--------------+ |    +--------------+ +--------------+    |
                    |                1 |                      |  2                | |               4   |
                    +------------------+                      +-------------------- +-------------------+
                */
                if (typeof(type) == 'function') {
                    result = type(region, box, target, t);
                } else {
                    //target size
                    var w = target ? target.offsetWidth() : 0,
                        h = target ? target.offsetHeight() : 0,
                        arr = type.split(/-/g);
                    if (arr.length == 2) {
                        var hp = arr[0], vp = arr[1];
                        switch (vp) {
                            case "outertop":
                                result.top = region.top - h;
                                break;
                            case "top":
                                result.top = region.top;
                                break;
                            case "middle":
                                result.top = region.top + region.height / 2 - h / 2;
                                break;
                            case "bottom":
                                result.top = region.top + region.height - h;
                                break;
                            default:
                                //case "outerbottom":
                                result.top = region.top + region.height;
                        }
                        switch (hp) {
                            case "outerleft":
                                result.left = region.left - w;
                                break;
                            case "left":
                                result.left = region.left;
                                break;
                            case "center":
                                result.left = region.left + region.width / 2 - w / 2;
                                break;
                            case "right":
                                result.left = region.left + region.width - w;
                                break;
                            default:
                                //case "outerright":
                                result.left = region.left + region.width;
                        }
                    } else {
                        if (type == "outer") type = "12";
                        else if (type == "inner") type = "4";

                        var adjust = function (type) {
                            var hi, wi;
                            switch (type) {
                                case '2':
                                    hi = true;
                                    wi = false;
                                    break;
                                case '3':
                                    hi = wi = false;
                                    break;
                                case '4':
                                    hi = wi = true;
                                    break;
                                default:
                                    //case '1':
                                    hi = false;
                                    wi = true;
                            }

                            if (hi) {
                                if (region.top + h < box.height)
                                    result.top = region.top;
                                else
                                    result.top = region.top + region.height - h;
                            } else {
                                if (region.top + region.height + h < box.height)
                                    result.top = region.top + region.height;
                                else
                                    result.top = region.top - h;
                            }
                            if (wi) {
                                if (region.left + w < box.width)
                                    result.left = region.left;
                                else
                                    result.left = region.left + region.width - w;
                            } else {
                                if (region.left + region.width + w < box.width)
                                    result.left = region.left + region.width;
                                else
                                    result.left = region.left - w;
                            }
                            //over right
                            if (result.left + w > box.width) result.left = box.width - w;
                            //over left
                            if (result.left < box.left) result.left = box.left;
                            //over bottom
                            if (result.top + h > box.height) result.top = box.height - h;
                            //over top
                            if (result.top < box.top) result.top = box.top;
                        };

                        if (type == '12') {
                            adjust('1');
                            if (result.top < region.top + region.height && result.top + h > region.top) adjust('2');
                        } else if (type == '21') {
                            adjust('2');
                            if (result.left < region.left + region.width && result.left + w > region.left) adjust('1');
                        } else {
                            adjust(type);
                        }
                    }
                }
                return result;
            }
        },
        _scrollBarSize: 0,
        getScrollBarSize: function (force) {
            var ns = this;
            if (force || !ns._scrollBarSize) {
                var div;
                ood('body').append(div = ood.create('<div style="width:50px;height:50px;visibility:hidden;position:absolute;margin:0;padding:0;left:-100%;top:-100%;overflow:scroll;"></div>'));
                ns._scrollBarSize = div.get(0).offsetWidth - div.get(0).clientWidth;
                div.remove();
            }
            return ns._scrollBarSize;
        },
        _dpi: 0,
        getDPI: function (force) {
            var ns = this;
            if (force || !ns._dpi) {
                var div;
                ood('body').append(div = ood.create('<div style="width:1in;height:1in;visibility:hidden;position:absolute;margin:0;padding:0;left:-100%;top:-100%;overflow:scroll;"></div>'));
                ns._dpi = div.get(0).offsetHeight;
                div.remove();
            }
            return ns._dpi;
        },
        getStyle: function (node, name, force) {
            if (!node || node.nodeType != 1) return '';
            if (name == "rotate") {
                return ood(node).rotate();
            }
            var ns = ood.Dom,
                css3prop = ood.Dom._css3prop,
                style = node.style,
                value, b;
            if (name == 'opacity' && (!ns.css3Support("opacity")) && ood.browser.ie)
                b = name = 'filter';

            value = style[name];
            if (force || !value || value === "initial") {
                var me = ood.Dom.getStyle, t,
                    brs = ood.browser,
                    map = me.map || (me.map = {'float': 1, 'cssFloat': 1, 'styleFloat': 1}),
                    c1 = me._c1 || (me._c1 = {}),
                    c2 = me._c2 || (me._c2 = {}),
                    c3 = me._c3 || (me._c3 = {}),
                    name = c1[name] || (c1[name] = name.replace(/\-(\w)/g, function (a, b) {
                        return b.toUpperCase()
                    })),
                    name2 = c2[name] || (c2[name] = name.replace(/([A-Z])/g, "-$1").toLowerCase()),
                    name3, name4;

                var n1 = name;
                if (n1.indexOf("border") === 0) {
                    n1 = n1.replace(/[-]?(left|top|right|bottom)/ig, '');
                }
                if (ood.arr.indexOf(css3prop, n1) != -1) {
                    if (!ns.css3Support(name)) {
                        return '';
                    } else {
                        if (name != "textShadow") {
                            name3 = brs.cssTag2 + name2.charAt(0).toUpperCase() + name2.substr(1);
                            name4 = brs.cssTag1 + name2;
                        }
                    }
                }

                if (map[name])
                    name = ood.browser.ie ? "styleFloat" : "cssFloat";
                //document.defaultView first, for opera 9.0
                value = ((t = document.defaultView) && t.getComputedStyle) ?
                    (t = t.getComputedStyle(node, null)) ?
                        (t.getPropertyValue(name2) || (name4 && t.getPropertyValue(name4)))
                        : ''
                    : node.currentStyle ?
                        (node.currentStyle[name] || node.currentStyle[name2] || (name3 && (node.currentStyle[name3] || node.currentStyle[name4])))
                        : ((style && (style[name] || (name3 && style[name3]))) || '');
                /*
                            if(ood.browser.opr){
                                var map2 = me.map2 || (me.map2={left:1,top:1,right:1,bottom:1});
                                if(map2[name] && (ood.Dom.getStyle(node,'position')=='static'))
                                    value = 'auto';
                            }
            */
            }
            // ood.CSS.$px is for IE678
            if (!b && ood.browser.ie678) {
                // INPUT/TEXTREA will always return % for font-size
                if ((name == 'fontSize' || name2 == 'font-size') && /%/.test(value) && node.parentNode) {
                    value = (node.parentNode.currentStyle[name] || node.parentNode.currentStyle[name2]) * (parseFloat(value) || 0);
                } else if (ood.CSS.$isEm(value)) {
                    value = ood.CSS.$px(value, node);
                    ;
                }
            }
            return b ? value ? (parseFloat(value.match(/alpha\(opacity=(.*)\)/)[1]) || 0) / 100 : 1 : (value || '');
        },
        $getBoundingClientRect: function (node, original) {
            var rect = node.getBoundingClientRect(), t;
            if (!original && (t = ood.ini.$transformScale))
                for (var i in rect)
                    rect[i] /= t;
            return rect;
        },
        $transformIE: function (node, value) {
            var style = node.style,
                t = (style.filter || "").replace(/progid\:DXImageTransform\.Microsoft\.Matrix\([^)]+\)/ig, "").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
            if (ood.browser.ie8) style.msfilter = t;
            style.filter = t;
            style.marginTop = style.marginLeft = "";
            if (value) {
                var tmatrix = function () {
                    var current,
                        degRat = Math.PI / 180,
                        //create new matrix
                        matrix = function (m11, m12, m21, m22, dx, dy) {
                            var m = {};
                            m.m11 = ood.isSet(m11) ? parseFloat(m11) : 1;
                            m.m12 = ood.isSet(m12) ? parseFloat(m12) : 0;
                            m.m21 = ood.isSet(m21) ? parseFloat(m21) : 0;
                            m.m22 = ood.isSet(m22) ? parseFloat(m22) : 1;
                            m.dx = ood.isSet(dx) ? parseFloat(dx) : 0;
                            m.dy = ood.isSet(dy) ? parseFloat(dy) : 0;
                            return m;
                        },
                        //multiply matrices
                        multiply = function (newMatrix, currentMatrix) {
                            //modify transformation matrix
                            var m = {};
                            m.m11 = roundNumber(newMatrix.m11 * currentMatrix.m11 + newMatrix.m21 * currentMatrix.m12, 10);
                            m.m12 = roundNumber(newMatrix.m12 * currentMatrix.m11 + newMatrix.m22 * currentMatrix.m12, 10);
                            m.m21 = roundNumber(newMatrix.m11 * currentMatrix.m21 + newMatrix.m21 * currentMatrix.m22, 10);
                            m.m22 = roundNumber(newMatrix.m12 * currentMatrix.m21 + newMatrix.m22 * currentMatrix.m22, 10);
                            m.dx = roundNumber(currentMatrix.dx + newMatrix.dx, 10);
                            m.dy = roundNumber(currentMatrix.dy + newMatrix.dy, 10);
                            //return new transformation matrix
                            return m;
                        },
                        //convert degrees to radians
                        deg2rad = function (deg) {
                            return degRat * deg;
                        },
                        //format number
                        roundNumber = function (num, dec) {
                            var result = Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
                            return result;
                        };

                    //rotate transformation
                    this.rotate = function (deg) {
                        var rad = ood.isSet(deg) ? parseFloat(deg2rad(parseFloat(deg))) : 0;
                        var m = matrix(Math.cos(rad), -Math.sin(rad), Math.sin(rad), Math.cos(rad), 0, 0);
                        current = multiply(m, current);
                    };
                    //translate transformations
                    this.translate = function (x, y) {
                        var m = matrix(1, 0, 0, 1, parseFloat(x), parseFloat(y));
                        current = multiply(m, current);
                    };
                    this.translateX = function (x) {
                        this.translate(x, 0);
                    };
                    this.translateY = function (y) {
                        this.translate(0, y);
                    };
                    //scaling transformations
                    this.scale = function (x, y) {
                        var m = matrix(ood.isSet(x) ? parseFloat(x) : 1, 0, 0, ood.isSet(y) ? parseFloat(y) : 1, 0, 0);
                        current = multiply(m, current);
                    };
                    this.scaleX = function (x) {
                        this.scale(x, 1);
                    };
                    this.scaleY = function (y) {
                        this.scale(1, y);
                    };
                    //skew transformations
                    this.skew = function (xAng, yAng) {
                        xAng = ood.isSet(xAng) ? parseFloat(deg2rad(parseFloat(xAng))) : 0;
                        yAng = ood.isSet(yAng) ? parseFloat(deg2rad(parseFloat(yAng))) : 0;
                        var m = matrix(1, Math.tan(xAng), Math.tan(yAng), 1, 0, 0);
                        current = multiply(m, current);
                    };
                    this.skewX = function (xAng) {
                        this.skew(xAng, 0);
                    };
                    this.skewY = function (yAng) {
                        this.skew(0, yAng);
                    };
                    //transformation matrix
                    this.matrix = function (m11, m12, m21, m22, dx, dy) {
                        current = multiply(matrix(m11, m12, m21, m22, dx, dy), current);
                    };
                    //return matrix
                    this.getMatrix = function () {
                        return current;
                    };
                    //return IE CSS matrix
                    this.getFilter = function () {
                        return "progid:DXImageTransform.Microsoft.Matrix(M11=" + current.m11 + ", M12=" + current.m12 + ", M21=" + current.m21 + ", M22=" + current.m22 + ", Dx=" + current.dx + ", Dy=" + current.dy + ", SizingMethod='auto expand')";
                    };
                    this.getX = function () {
                        return current.dx;
                    };
                    this.getY = function () {
                        return current.dy;
                    };
                    this.reset = function () {
                        current = matrix(1, 0, 0, 1, 0, 0);
                    };
                    this.reset();
                };
                var computeMatrix = function (transform) {
                    var m = new tmatrix();
                    //Split the webkit functions and loop through them
                    var functions = transform.match(/[A-z]+\([^\)]+/g) || [];
                    for (var k = 0; k < functions.length; k++) {
                        //Prepare the function name and its value
                        var arr = functions[k].split('('),
                            func = arr[0],
                            value = arr[1],
                            values;
                        //Now we rotate through the functions and add it to our matrix
                        switch (func) {
                            case 'rotate':
                                m.rotate(value);
                                break;
                            case 'scale':
                                values = value.split(',');
                                m.scale(values[0], values[1]);
                                break;
                            case 'scaleX':
                                m.scaleX(value);
                                break;
                            case 'scaleY':
                                m.scaleY(value);
                                break;
                            case 'skew':
                                values = value.split(',');
                                m.skew(values[0], values[1]);
                                break;
                            case 'skewX':
                                m.skewX(value);
                                break;
                            case 'skewY':
                                m.skewY(value);
                                break;
                            case 'translate':
                                values = value.split(',');
                                m.translate(values[0], values[1]);
                                break;
                            case 'translateX':
                                m.translateX(value);
                                break;
                            case 'translateY':
                                m.translateY(value);
                                break;
                        }
                    }
                    return m;
                };
                var matrix = computeMatrix(value);
                var ow = node.offsetWidth, oh = node.offsetHeight;
                var filter = matrix.getFilter();
//ood.echo(filter);
                var t = ((style.filter ? (style.filter + ",") : "") + filter).replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                if (ood.browser.ie8) style.msfilter = t;
                style.filter = t;
//ood.echo(t);

                // for fake case
                if (node.getBoundingClientRect) {
                    var transX = matrix.getX(),
                        transY = matrix.getY(),
                        rect = ood.Dom.$getBoundingClientRect(node),
                        w = rect.right - rect.left,
                        h = rect.bottom - rect.top;

                    style.marginLeft = Math.round(parseFloat((ow - w) / 2 + 10 + transX)) + 'px';
                    style.marginTop = Math.round(parseFloat((oh - h) / 2 + 10 + transY)) + 'px';
                }

                // fake
                style.transform = value;
            }
        },
        $textShadowIE: function (node, value, box) {
            var style = node.style;
            if (!value) {
                var f = function (s) {
                        return (s || "").replace(/progid\:DXImageTransform\.Microsoft\.(Chroma|DropShadow|Glow)\([^)]+\)/ig, "").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                    },
                    s1 = style.filter;
                if (s1) {
                    if (ood.browser.ie8) style.msfilter = f(s1);
                    style.filter = f(s1);
                }
                if (!box)
                    style.backgroundColor = "";
            } else {
                var f = function (x, y, r, c) {
                        return (box ? "" : "progid:DXImageTransform.Microsoft.Chroma(Color=#cccccc) ")
                            + "progid:DXImageTransform.Microsoft.DropShadow(Color=" + c + ", OffX=" + x + ", OffY=" + y + ") "
                            + (parseFloat(r) > 0 ? "progid:DXImageTransform.Microsoft.Glow(Strength=" + r + ", Color=" + c + ")" : "");
                    },
                    r = value.match(/([\d\.-]+)px\s+([\d\.-]+)px(\s+([\d\.-]+)px)?(\s+([#\w]+))?/);
                if (r) {
                    var t = ((style.filter ? (style.filter + ",") : "") + f(r[1], r[2], r[4], r[6] || "#000000")).replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                    if (ood.browser.ie8) style.msfilter = t;
                    style.filter = t;
                    if (!box)
                        style.backgroundColor = "#C5C5C5";
                }
            }
        },
        /*
        *type:linear, or radial
        *orient:LT/T/RT/R/RB/B/LB/L, + C for radial
        *stops:{clr:, pos:, opacity:}
        *rate:0~1
        *shape: circle or ellipse, only for radial
        *size: farthest-corner..
        */
        $setGradients: function (node, value, xb) {
            xb = xb || ood.browser;
            var ns = this,
                ver = xb.ver,
                c16 = "0123456789ABCDEF",
                _toFF = function (n, b) {
                    n = parseInt(n * b, 10) || 0;
                    n = (n > 255 || n < 0) ? 0 : n;
                    return c16.charAt((n - n % 16) / 16) + c16.charAt(n % 16);
                },
                _to255 = function (s) {
                    s = s.split('');
                    return c16.indexOf(s[0].toUpperCase()) * 16 + c16.indexOf(s[1].toUpperCase());
                };
            window.btoa = window.btoa || function (text) {
                if (/([^\u0000-\u00ff])/.test(text)) return;
                var table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", i = 0, cur, prev,
                    byteNum, result = [];
                while (i < text.length) {
                    cur = text.charCodeAt(i);
                    byteNum = (i + 1) % 3;
                    switch (byteNum) {
                        case 1://first byte
                            result.push(table.charAt(cur >> 2));
                            break;
                        case 2: //second byte
                            result.push(table.charAt((prev & 3) << 4 | (cur >> 4)));
                            break;
                        case 0: //third byte
                            result.push(table.charAt((prev & 0x0f) << 2 | (cur >> 6)));
                            result.push(table.charAt(cur & 0x3f));
                            break;
                    }
                    prev = cur;
                    i++;
                }
                if (byteNum == 1) {
                    result.push(table.charAt((prev & 3) << 4));
                    result.push("==");
                } else if (byteNum == 2) {
                    result.push(table.charAt((prev & 0x0f) << 2));
                    result.push("=");
                }
                return result.join("");
            }
            var iecracker1 = function (node, orient, stops, shape, size, rate) {
                    var id = "ood.s-ie8gdfix";
                    if (!node || node.nodeType != 1 || !node.style) return;
                    var style = node.style,
                        tmp1 = ns.getStyle(node, 'overflow'),
                        tmp2 = ns.getStyle(node, 'display');
                    if (tmp1 != 'hidden' || (tmp2 != 'block' && tmp2 != 'relative')) return;

                    if (!orient) {
                        var i, a = node.childNodes, l = a.length;
                        for (i = 0; i < l; i++) {
                            if (a[i].nodeType == 1 && a[i].id == id) {
                                node.removeChild(a[i]);
                                break;
                            }
                        }
                        style.backgroundColor = '';
                        var t = ((style.filter || "").replace(/progid\:DXImageTransform\.Microsoft\.Alpha\([^)]+\)/ig, '')).replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) style.msfilter = t;
                        style.filter = t;
                    } else {
                        rate = rate || 1;

                        var innerColor = stops[0].clr,
                            outerColor = stops[stops.length - 1].clr;

                        var ew = node.offsetWidth || 0,
                            eh = node.offsetHeight || 0,
                            aw = ew * rate * 2,
                            ah = eh * rate * 2;

                        if (shape == 'circle')
                            aw = ah = Math.min(aw, ah);

                        var l = -aw / 2, t = -ah / 2, w = aw, h = ah;
                        if (ood.isObj(orient)) {
                            l = orient.left || (Math.round(parseFloat(l) || 0) + 'px');
                            t = orient.top || (Math.round(parseFloat(t) || 0) + 'px');
                        } else {
                            switch (orient) {
                                case 'LT':
                                    l = -aw / 2;
                                    t = -ah / 2;
                                    break;
                                case 'T':
                                    l = (ew - aw) / 2;
                                    t = -ah / 2;
                                    break;
                                case 'RT':
                                    l = ew - aw / 2;
                                    t = -ah / 2;
                                    break;
                                case 'L':
                                    l = -aw / 2;
                                    t = (eh - ah) / 2;
                                    break;
                                case 'C':
                                    l = (ew - aw) / 2;
                                    t = (eh - ah) / 2;
                                    break;
                                case 'R':
                                    l = ew - aw / 2;
                                    t = (eh - ah) / 2;
                                    break;
                                case 'LB':
                                    l = -aw / 2;
                                    t = eh - ah / 2;
                                    break;
                                case 'B':
                                    l = (ew - aw) / 2;
                                    t = eh - ah / 2;
                                    break;
                                case 'RB':
                                    l = ew - aw / 2;
                                    t = eh - ah / 2;
                                    break;
                            }
                            l += 'px';
                            t += 'px';
                        }

                        var at = document.createElement('div'),
                            s = at.style;
                        at.id = id;
                        s.position = 'absolute';
                        s.zIndex = '0';
                        s.top = t;
                        s.left = l;
                        s.width = Math.round(parseFloat(w) || 0) + 'px';
                        s.height = Math.round(parseFloat(h) || 0) + 'px';
                        s.backgroundColor = innerColor;

                        var starto = stops[0].opacity ? parseFloat(stops[0].opacity) * 100 : 100
                        var t = ((s.filter ? (s.filter + ",") : "") + 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + starto + ', finishopacity=0, style=2)').replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) s.msfilter = t;
                        s.filter = t;

                        // the first node
                        if (node.firstChild)
                            node.insertBefore(at, node.firstChild);
                        else
                            node.appendChild(at);
                        style.backgroundColor = outerColor;
                        if (stops[stops.length - 1].opacity) {
                            var t = ((style.filter ? (style.filter + ",") : "") + "progid:DXImageTransform.Microsoft.Alpha(opacity=" + (parseFloat(stops[stops.length - 1].opacity) * 100) + ")").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                            if (ood.browser.ie8) style.msfilter = t;
                            style.filter = t;
                        }
                    }
                },
                iecracker21 = function (node, orient, stops) {
                    var id = "ood.s-ie8gdfix";
                    if (!node || node.nodeType != 1 || !node.style) return;
                    var style = node.style,
                        tmp1 = ns.getStyle(node, 'overflow'),
                        tmp2 = ns.getStyle(node, 'display');
                    if (tmp1 != 'hidden') {
                        ns.setStyle(node, 'overflow', 'hidden');
                    }
                    if (tmp2 != 'block' && tmp2 != 'relative') {
                        ns.setStyle(node, 'display', 'relative');
                    }

                    if (!orient) {
                        var i, a = node.childNodes, l = a.length;
                        for (i = 0; i < l; i++) {
                            if (a[i].nodeType == 1 && a[i].id == id) {
                                node.removeChild(a[i]);
                                break;
                            }
                        }
                        style.backgroundColor = '';
                        var t = (style.filter || "").replace(/progid\:DXImageTransform\.Microsoft\.Alpha\([^)]+\)/ig, '').replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) style.msfilter = t;
                        style.filter = t;
                    } else {
                        var innerColor = stops[0].clr,
                            outerColor = stops[stops.length - 1].clr;

                        var ew = node.offsetWidth || 0,
                            eh = node.offsetHeight || 0,
                            size = Math.min(ew, eh),
                            xs = 0, xe = size, ys = 0, ye = size;

                        switch (orient) {
                            case 'LT':
                                xs = 0;
                                ys = 0;
                                xe = size;
                                ye = size;
                                break;
//                      case 'T':
//                      xs=0;ys=0;xe=0;ye=size;
//                      break;
                            case 'RT':
                                xs = size;
                                ys = 0;
                                xe = 0;
                                ye = size;
                                break;
//                      case 'L':
//                      xs=0;ys=0;xe=0;ye=size;
//                      break;
//                      case 'R':
//                      xs=size;ys=0;xe=0;ye=0;
//                      break;
                            case 'LB':
                                xs = 0;
                                ys = size;
                                xe = size;
                                ye = 0;
                                break;
//                      case 'B':
//                      xs=0;ys=size;xe=0;ye=0;
//                      break;
                            case 'RB':
                                xs = size;
                                ys = size;
                                xe = 0;
                                ye = 0;
                                break;
                        }

                        var at = document.createElement('div'),
                            s = at.style;
                        at.id = id;
                        s.position = 'absolute';
                        s.zIndex = '0';
                        s.top = 0;
                        s.left = 0;
                        s.width = ew;
                        s.height = eh;
                        s.backgroundColor = innerColor;

                        var starto = stops[0].opacity ? parseFloat(stops[0].opacity) * 100 : 100
                        var t = ((s.filter ? (s.filter + ",") : "") + 'progid:DXImageTransform.Microsoft.Alpha(style=1, opacity=' + starto + ', finishopacity=0, startX=' + xs + ',finishX=' + xe + ',startY=' + ys + ',finishY=' + ye + ')').replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) s.msfilter = t;
                        s.filter = t;

                        // the first node
                        if (node.firstChild)
                            node.insertBefore(at, node.firstChild);
                        else
                            node.appendChild(at);
                        style.backgroundColor = outerColor;
                        if (stops[stops.length - 1].opacity) {
                            var t = ((style.filter ? (style.filter + ",") : "") + "progid:DXImageTransform.Microsoft.Alpha(opacity=" + (parseFloat(stops[stops.length - 1].opacity) * 100) + ")").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                            if (ood.browser.ie8) style.msfilter = t;
                            style.filter = t;
                        }
                    }
                },
                iecracker2 = function (node, orient, stops) {
                    var id = "ood.s-ie8gdfix";
                    if (!node || node.nodeType != 1 || !node.style) return;
                    var style = node.style;
                    if (!orient) {
                        var t = ((style.filter || "").replace(/progid\:DXImageTransform\.Microsoft\.Gradient\([^)]+\)/ig, '')).replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) style.msfilter = t;
                        style.filter = t;
                        var i, a = node.childNodes, l = a.length;
                        for (i = 0; i < l; i++) {
                            if (a[i].nodeType == 1 && a[i].id == id) {
                                node.removeChild(a[i]);
                                break;
                            }
                        }
                        style.backgroundColor = '';
                        var t = ((style.filter || "").replace(/progid\:DXImageTransform\.Microsoft\.Alpha\([^)]+\)/ig, '')).replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) style.msfilter = t;
                        style.filter = t;
                    } else {
                        var innerColor = stops[0].clr,
                            outerColor = stops[stops.length - 1].clr,
                            ori = 1, t;
                        if (stops[0].opacity)
                            innerColor = innerColor.replace('#', '#' + _toFF(stops[0].opacity, 255));
                        if (stops[stops.length - 1].opacity)
                            outerColor = outerColor.replace('#', '#' + _toFF(stops[stops.length - 1].opacity, 255));
                        switch (orient) {
                            case 'LT':
                            case 'RT':
                            case 'LB':
                            case 'RB':
                                iecracker21(node, orient, stops);
                                return;
                            case "L":
                                ori = 1;
                                break;
                            case "R":
                                ori = 1;
                                t = innerColor;
                                innerColor = outerColor;
                                outerColor = t;
                                break;
                            case "T":
                                ori = 0;
                                break;
                            case "B":
                                ori = 0;
                                t = innerColor;
                                innerColor = outerColor;
                                outerColor = t;
                                break;
                        }
                        var t = ((style.filter ? (style.filter + ",") : "") + "progid:DXImageTransform.Microsoft.Gradient(StartColorstr='" + innerColor + "',EndColorstr='" + outerColor + "',GradientType=" + ori + ")").replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                        if (ood.browser.ie8) style.msfilter = t;
                        style.filter = t;
                    }
                },
                svgcracker1 = function (node, orient, stops, shape, size, rate) {
                    if (!orient) {
                        node.style.backgroundImage = "";
                    } else {
                        rate = rate || 1;
                        var id = 'svg:' + ood.id(),
                            cx = '0%', cy = '0%',
                            r = rate * 100 + "%";
                        if (ood.isObj(orient)) {
                            cx = orient.left || cx;
                            cy = orient.left || cy;
                        } else {
                            switch (orient) {
                                case "T":
                                    cx = '50%';
                                    cy = '0%';
                                    break;
                                case "B":
                                    cx = '50%';
                                    cy = '100%';
                                    break;
                                case "L":
                                    cx = '0%';
                                    cy = '50%';
                                    break;
                                case "R":
                                    cx = '100%';
                                    cy = '50%';
                                    break;
                                case "LT":
                                    cx = '0%';
                                    cy = '0%';
                                    break;
                                case "RT":
                                    cx = '100%';
                                    cy = '0%';
                                    break;
                                case "RB":
                                    cx = '100%';
                                    cy = '100%';
                                    break;
                                case "LB":
                                    cx = '0%';
                                    cy = '100%';
                                    break;
                                case "C":
                                    cx = '50%';
                                    cy = '50%';
                                    break;
                            }
                        }
                        /*                    var rectw=1,recth=1;
                                        if(shape=='circle'){
                                            var m=Math.min(node.offsetWidth,node.offsetHeight);
                                            if(m==node.offsetWidth){
                                                recth=m/node.offsetHeight;
                                            }else{
                                                rectw=m/node.offsetWidth;
                                            }
                                        }
                    */
                        var svg = '<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 1 1" preserveAspectRatio="none">'
                            + '<radialGradient id="' + id + '" gradientUnits="userSpaceOnUse" cx="' + cx + '" cy="' + cy + '" r="' + r + '">';

                        for (var i = 0, l = stops.length; i < l; i++) {
                            svg += '<stop stop-color="' + stops[i].clr + '" offset="' + stops[i].pos + '" ' + (ood.isSet(stops[i].opacity) ? (' stop-opacity="' + stops[i].opacity + '"') : '') + ' />';
                        }

                        svg += '</radialGradient>'
                            + '<rect x="-50" y="-50" width="101" height="101" fill="url(#' + id + ')" />'
                            + '</svg>';

                        node.style.backgroundImage = 'url("data:image/svg+xml;base64,' + window.btoa(svg) + '")';
                    }
                },
                svgcracker2 = function (node, orient, stops) {
                    if (!orient) {
                        node.style.backgroundImage = '';
                    } else {
                        var id = 'svg' + ood.id(), x1 = '0%', y1 = '0%', x2 = '0%', y2 = '100%';

                        switch (orient) {
                            case "T":
                                x1 = '50%';
                                y1 = '0%';
                                x2 = '50%';
                                y2 = '100%';
                                break;
                            case "B":
                                x1 = '50%';
                                y1 = '100%';
                                x2 = '50%';
                                y2 = '0%';
                                break;
                            case "L":
                                x1 = '0%';
                                y1 = '50%';
                                x2 = '100%';
                                y2 = '50%';
                                break;
                            case "R":
                                x1 = '100%';
                                y1 = '50%';
                                x2 = '0%';
                                y2 = '50%';
                                break;
                            case "LT":
                                x1 = '0%';
                                y1 = '0%';
                                x2 = '100%';
                                y2 = '100%';
                                break;
                            case "RT":
                                x1 = '100%';
                                y1 = '0%';
                                x2 = '0%';
                                y2 = '100%';
                                break;
                            case "RB":
                                x2 = '0%';
                                y2 = '0%';
                                x1 = '100%';
                                y1 = '100%';
                                break;
                            case "LB":
                                x1 = '0%';
                                y1 = '100%';
                                x2 = '100%';
                                y2 = '0%';
                                break;
                            default:
                            /*To caculate x1/x2/y1/y2 from orient*/
                        }

                        var svg = '<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 1 1" preserveAspectRatio="none">'
                            + '<linearGradient id="' + id + '" gradientUnits="userSpaceOnUse" x1="' + x1 + '" y1="' + y1 + '" x2="' + x2 + '" y2="' + y2 + '">';

                        for (var i = 0, l = stops.length; i < l; i++) {
                            svg += '<stop stop-color="' + stops[i].clr + '" offset="' + stops[i].pos + '" ' + (ood.isSet(stops[i].opacity) ? (' stop-opacity="' + stops[i].opacity + '"') : '') + '/>';
                        }

                        svg += '</linearGradient>'
                            + '<rect x="0" y="0" width="1" height="1" fill="url(#' + id + ')" />'
                            + '</svg>';

                        node.style.backgroundImage = 'url("data:image/svg+xml;base64,' + window.btoa(svg) + '")';
                    }
                },
                css1 = function (node, orient, stops, shape, size, rate) {
                    var arr1 = [], arr2 = [], style = node.style;
                    ood.arr.each(stops, function (o) {
                        var clr = o.clr;
                        if (ood.isSet(o.opacity) && clr.charAt(0) == '#') {
                            clr = clr.slice(1);
                            clr = "rgba(" + _to255(clr.substr(0, 2)) + "," + _to255(clr.substr(2, 2)) + "," + _to255(clr.substr(4, 2)) + "," + (parseFloat(o.opacity) || 1) + ")";
                        }
                        arr1.push(clr + " " + o.pos);
                        if (xb.isWebKit) {
                            arr2.push("color-stop(" + o.pos + ',' + clr + ")");
                        }
                    });

                    if (!orient) {
                        style.backgroundImage = "";
                    } else {
                        var position;
                        if (ood.isObj(orient)) {
                            position = orient.left + " " + orient.top;
                        } else {
                            switch (orient) {
                                case 'LT':
                                    position = 'left top';
                                    break;
                                case 'T':
                                    position = 'center top';
                                    break;
                                case 'RT':
                                    position = 'right top';
                                    break;
                                case 'L':
                                    position = 'left center';
                                    break;
                                case 'C':
                                    position = 'center center';
                                    break;
                                case 'R':
                                    position = 'right center';
                                    break;
                                case 'LB':
                                    position = 'left bottom';
                                    break;
                                case 'B':
                                    position = 'center bottom';
                                    break;
                                case 'RB':
                                    position = 'right bottom';
                                    break;
                                default:
                                    position = 'left top';
                            }
                        }

                        if (xb.isWebKit) {
                            style.backgroundImage = "-webkit-gradient(radial," + position + ", 0px, " + position + ", 100%," + arr2.join(",") + ")";
                        }

                        var v1 = "radial-gradient(" + position + "," + shape + " " + size + "," + arr1.join(",") + ")";
                        if (xb.cssTag1) {
                            style.backgroundImage = xb.cssTag1 + v1;
                        }
                        style.backgroundImage = "radial-gradient(" + size + " " + shape + " at " + position + "," + arr1.join(",") + ")";
                    }
                },
                css2 = function (node, orient, stops) {
                    var arr1 = [], arr2 = [], style = node.style;
                    ood.arr.each(stops, function (o) {
                        var clr = o.clr;
                        if (ood.isSet(o.opacity) && clr.charAt(0) == '#') {
                            clr = clr.slice(1);
                            clr = "rgba(" + _to255(clr.substr(0, 2)) + "," + _to255(clr.substr(2, 2)) + "," + _to255(clr.substr(4, 2)) + "," + (parseFloat(o.opacity) || 1) + ")";
                        }
                        arr1.push(clr + " " + o.pos);
                        if (xb.isWebKit) {
                            arr2.push("color-stop(" + o.pos + ',' + clr + ")");
                        }
                    });

                    if (!orient) {
                        style.backgroundImage = "";
                    } else {
                        var direction = 'to bottom';
                        var directionmoz = "top";
                        var directionwebkit = 'left top, left bottom';
                        switch (orient) {
                            case 'LT':
                                direction = "135deg";
                                directionmoz = "-45deg";
                                directionwebkit = 'left top, right bottom';
                                break;
                            case 'T':
                                direction = "to bottom";
                                directionmoz = "top";
                                directionwebkit = 'left top, left bottom';
                                break;
                            case 'RT':
                                direction = directionmoz = "-135deg";
                                directionwebkit = 'right top, left bottom';
                                break;
                            case 'L':
                                direction = "to right";
                                directionmoz = "left";
                                directionwebkit = 'left top, right top';
                                break;
                            case 'R':
                                direction = "to left";
                                directionmoz = "right";
                                directionwebkit = 'right top, left top';
                                break;
                            case 'LB':
                                direction = directionmoz = "45deg";
                                directionwebkit = 'left bottom, right top';
                                break;
                            case 'B':
                                direction = "to top";
                                directionmoz = "bottom";
                                directionwebkit = 'left bottom, left top';
                                break;
                            case 'RB':
                                direction = "-45deg";
                                directionmoz = "135deg";
                                directionwebkit = 'right bottom, left top';
                                break;
                            default:
                                direction = orient;
                                directionmoz = orient;
                                directionwebkit = 'left top, right bottom';
                        }

                        if (xb.isWebKit) {
                            style.backgroundImage = "-webkit-gradient(linear," + directionwebkit + ", " + arr2.join(",") + ")";
                        }

                        var v1 = "linear-gradient({#}," + arr1.join(",") + ")";
                        if (xb.cssTag1) {
                            style.backgroundImage = xb.cssTag1 + v1.replace("{#}", directionmoz);
                        }
                        style.backgroundImage = v1.replace("{#}", direction);
                    }
                };

            var type = value ? (value.type || value || 'linear').toLowerCase() : null,
                rate = value ? (value.rate || 1) : null,
                shape = value ? (value.shape || 'circle').toLowerCase() : null,
                size = value ? (value.size || 'farthest-corner').toLowerCase() : null,
                orient = value ? value.orient : null,
                stops = value ? value.stops : null;

            if (type != 'linear')
                type = 'radial';

            if (stops) {
                if (stops.length > 1) {
                    ood.arr.stableSort(stops, function (x, y) {
                        x = parseFloat(x.pos) || 0;
                        y = parseFloat(y.pos) || 0;
                        return x > y ? 1 : x == y ? 0 : -1;
                    });
                } else {
                    return;
                }
            }

            if (xb.ie678) {
                if (type == 'linear') {
                    iecracker2(node, orient, stops);
                }
                else {
                    iecracker1(node, orient, stops, shape, size, rate);
                }
            }
            if (xb.ie9 || (xb.opr && ver < 11.1)) {
                if (type == 'linear') {
                    svgcracker2(node, orient, stops);
                }
                else {
                    svgcracker1(node, orient, stops, shape, size, rate);
                }
            }
            if (((xb.gek && ver >= 3.6)
                || (xb.isChrome && ver >= 10)
                || (xb.isSafari && ver >= 5.1)
                || (xb.ie && ver >= 10)
                || (xb.opr && ver >= 11.1)
            )) {
                if (type == 'linear') {
                    css2(node, orient, stops);
                } else {
                    if (xb.opr && ver < 12)
                        svgcracker1(node, orient, stops, shape, size, rate);
                    else
                        css1(node, orient, stops, shape, size, rate);
                }
            }
        },
        $setZoom: function (node, scale, transx, transy, origin) {
            scale = parseFloat(scale);
            if (ood.isNaN(scale) || scale <= 0) scale = '';
            var b = ood.browser, h = {};
            h[b.cssTag1 + "transform"] = h.transform = scale === '' ? '' : ((ood.isNumb(transx) && ood.isNumb(transy) ? ('translate(' + transx + 'px,' + transy + 'px) ') : '') + 'scale(' + scale + ',' + scale + ')');
            h[b.cssTag1 + "transform-origin"] = h["transform-origin"] = scale === '' ? '' : (origin || '0 0 0');
            ood(node).css(h);
        },
        _vAnimate: function (node, setting, callback) {
            if (!setting || !setting.endpoints || ood.isEmpty(setting.endpoints)) {
                if (callback) ood.tryF(callback);
                return;
            }

            var endpoints = setting.endpoints, begin = {}, end = {};
            node = ood(node);
            ood.each(endpoints, function (o, i) {
                if (!ood.isFun(o)) {
                    begin[i] = o[0];
                    end[i] = o[1]
                }
            });

            return node.animate(endpoints, function (threadid) {
                node.css(begin);
            }, function (threadid) {
                node.css(end);
                if (callback) ood.tryF(callback);
            }, setting.duration, 0, setting.type).start();
        },
        $adjustCss: function (hash, returnStr) {
            var fack = {nodeType: 1, style: {}}, style = fack.style;
            ood.Dom.setStyle(fack, hash);
            if (returnStr) {
                var arr = [];
                if (ood.browser.ie && ood.browser.ver == 8) {
                    if (style.filter)
                        style["-ms-filter"] = style.filter;
                    if (style['background-image'] == 'none')
                        style['background-image'] = "url(about:blank)";
                }
                ood.each(style, function (o, i) {
                    arr.push(i.replace(/([A-Z])/g, "-$1").toLowerCase() + ":" + o);
                });
                return arr.join(';').replace(/[;]+/g, ';');
            } else {
                return style;
            }
        },
        _cssfake: {rotate: 1, scaleX: 1, scaleY: 1, translateX: 1, translateY: 1, skewX: 1, skewY: 1},
        setStyle: function (node, name, value) {
            if (name == "rotate") {
                ood(node).rotate(value);
                return this;
            }
            var ns = ood.Dom,
                css3prop = ood.Dom._css3prop,
                xb = ood.browser,
                fake = ns._cssfake,
                style = node.style;

            if (node.nodeType != 1) return;
            if (typeof name == 'string') {
                if (fake[name]) {
                    ood(node)[name](value);
                } else {
                    var me = this.getStyle,
                        c1 = me._c1 || (me._c1 = {}),
                        r1 = me._r1 || (me._r1 = /alpha\([^\)]*\)/ig),
                        map = me.map || (me.map = {'float': 1, 'cssFloat': 1, 'styleFloat': 1});
                    var name2, name3, name4;
                    name = c1[name] || (c1[name] = name.replace(/\-(\w)/g, function (a, b) {
                        return b.toUpperCase()
                    }));

                    var n1 = name;
                    if (n1.indexOf("border") === 0) {
                        n1 = n1.replace(/[-]?(left|top|right|bottom)/ig, '');
                    }

                    if (name == "$gradient") {
                        return ns.$setGradients(node, value);
                    }
                    if (name == "$zoom") {
                        return ns.$setZoom(node, value);
                    } else if (name == 'opacity') {
                        value = ood.isFinite(value) ?
                            parseFloat(value) > 1 ?
                                1
                                : parseFloat(value) <= 0 ?
                                0
                                : parseFloat(value)
                            : 1;
                        value = value > 0.9999 ? '' : value;
                        if ((!ns.css3Support("opacity")) && xb.ie) {
                            if (value === '') value = 1;
                            // fake
                            style.opacity = value;
                            style.zoom = 1;
                            value = "alpha(opacity=" + 100 * value + ")";
                            var ov = (style.filter || "").replace(r1, "");
                            value = (ov ? (ov + ",") : "") + value;
                            name = "filter";
                            if (xb.ver == 8) name2 = "msfilter";
                        }
                    } else if (ood.arr.indexOf(css3prop, n1) != -1) {
                        if (!ns.css3Support(name)) {
                            if (xb.ie && xb.ver < 9) {
                                switch (name) {
                                    case "transform":
                                        ood.Dom.$transformIE(node, value);
                                        break;
                                    case "boxShadow":
                                        ood.Dom.$textShadowIE(node, value, true);
                                        break;
                                }
                            }
                            if (name == "textShadow" && xb.ie && xb.ver < 10) {
                                ood.Dom.$textShadowIE(node, value);
                            }
                            return this;
                        } else {
                            if (xb.cssTag2) {
                                if (name != "textShadow") {
                                    name2 = xb.cssTag2 + name.charAt(0).toUpperCase() + name.substr(1);
                                }
                            }
                        }
                    } else if (map[name]) {
                        name = xb.ie ? "styleFloat" : "cssFloat";
                    }

                    if (name == "filter") {
                        value = value.replace(/(^[\s,]*)|([\s,]*$)/g, '').replace(/,[\s]+/g, ',' + (ood.browser.ver == 8 ? "" : " "));
                    }
                    style[name] = value;
                    if (name2) style[name2] = value;
                    if (name3) style[name3] = value;
                    if (name4) style[name4] = value;
                }
            } else
                for (var i in name)
                    arguments.callee.call(this, node, i, name[i]);
        },
        _css3prop: 'opacity,textShadow,animationName,columnCount,flexWrap,boxDirection,backgroundSize,perspective,boxShadow,borderImage,borderRadius,boxReflect,transform,transition'.split(','),
        css3Support: function (key) {
            var self = arguments.callee,
                _c = self._c || (self._c = {});

            key = key.replace("$", "").replace(/\-(\w)/g, function (a, b) {
                return b.toUpperCase()
            });

            if (key in _c) return _c[key];

            var n = document.createElement("div"),
                s = n.style,
                rt = false,
                xb = ood.browser,
                f = function (k) {
                    k = k.replace(/\-(\w)/g, function (a, b) {
                        return b.toUpperCase()
                    });
                    if (s[k] !== undefined)
                        return true;
                    if (ood.browser.cssTag2) {
                        k = ood.browser.cssTag2 + k.charAt(0).toUpperCase() + k.substr(1);
                        if (s[k] !== undefined)
                            return true;
                    }
                    return false;
                };
            n.id = "ood_css3_" + ood.stamp();

            if (key.indexOf("border") === 0) {
                key = key.replace(/[-]?(left|top|right|bottom)/ig, '');
            }
            switch (key) {
                case "opacity":
                case "textShadow": {
                    rt = s[key] === '';
                }
                    break;
                case "generatedContent": {
                    var id = "tmp_css3_test" + ood.id(),
                        css = '#' + n.id + '{line-height:auto;margin:0;padding:0;border:0;font:0/0 a}#' + n.id + ':after{content:\'a\';visibility:hidden;line-height:auto;margin:0;padding:0;border:0;font:3px/1 a}';
                    ood.CSS.addStyleSheet(css, id);
                    ood('body').append(n);
                    var v = n.offsetHeight;
                    ood.CSS.remove("id", id);
                    ood(n.id).remove(n);
                    rt = v >= 3;
                }
                    break;
                case "fontFace": {
                    if (xb.ie && xb.ver >= 6) {
                        rt = true;
                    } else {
                        var id = "tmp_css3_test" + ood.id(),
                            css = '@font-face{font-family:"font";src:url("https://")}',
                            s = ood.CSS.addStyleSheet(css, id),
                            sh = s.sheet || s.styleSheet,
                            ctxt = sh ? ((sh.cssRules && sh.cssRules[0]) ? sh.cssRules[0].cssText : sh.cssText || '') : '';

                        rt = /src/i.test(ctxt) && ctxt.indexOf("@font-face") === 0;
                        ood.CSS.remove("id", id);
                    }
                }
                    break;
                case "rgba": {
                    s.cssText = "background-color:rgba(0,0,0,0.1)";
                    rt = s.backgroundColor.indexOf("rgba") != -1;
                }
                    break;
                case "hsla": {
                    s.cssText = 'background-color:hsla(120,40%,100%,.5)';
                    rt = s.backgroundColor.indexOf('hsla') != -1 || s.backgroundColor.indexOf('rgba') != -1;
                }
                    break;
                case "multiplebgs": {
                    s.cssText = "background:url(//:),url(//:),red url(//:)";
                    rt = /(url\s*\(.*?){3}/.test(s.background);
                }
                    break;
                case "gradient": {
                    var k = 'background-image:',
                        v1 = '-webkit-gradient(linear,left top,right bottom,from(#000),to(#fff));',
                        v2 = 'linear-gradient(left top,#000,#fff);',
                        arr = [k, v2];
                    if (ood.browser.cssTag1) {
                        arr.push(k);
                        arr.push(ood.browser.cssTag1 + v2);
                    }
                    if (ood.browser.isWebKit) {
                        arr.push(k);
                        arr.push(v1);
                    }
                    s.cssText = arr.join('');
                    rt = !!s.backgroundImage;
                }
                    break;
                case "transform3d": {
                    var r = f("perspective");
                    if (r && 'webkitPerspective' in document.documentElement.style) {
                        var id = "tmp_css3_test" + ood.id(),
                            css = '@media (transform-3d),(-webkit-transform-3d){#' + n.id + '{font:0/0;line-height:0;margin:0;padding:0;border:0;left:9px;position:absolute;height:3px;}}';
                        ood.CSS.addStyleSheet(css, id);
                        ood('body').append(n);
                        var v1 = n.offsetLeft, v2 = n.offsetHeight;
                        ood.CSS.remove("id", id);
                        ood(n.id).remove(n);
                        rt = v1 === 9 && v2 === 3;
                    }
                    rt = r;
                }
                    break;
                default: {
                    rt = f(key);
                }
            }
            return _c[key] = rt;
        },
        $AnimateEffects: {
            linear: function (s, c) {
                return (1 / s) * c;
            },
            sineIn: function (s, c) {
                return -1 * Math.cos(c / s * (Math.PI / 2)) + 1;
            },
            sineOut: function (s, c) {
                return Math.sin(c / s * (Math.PI / 2));
            },
            sineInOut: function (s, c) {
                return -1 / 2 * (Math.cos(Math.PI * c / s) - 1);
            },
            quadIn: function (s, c) {
                return (c /= s) * c;
            },
            quadOut: function (s, c) {
                return -1 * (c /= s) * (c - 2);
            },
            quadInOut: function (s, c) {
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * c * c;
                }
                return -1 / 2 * ((--c) * (c - 2) - 1);
            },
            cubicIn: function (s, c) {
                return (c /= s) * c * c;
            },
            cubicOut: function (s, c) {
                return ((c = c / s - 1) * c * c + 1);
            },
            cubicInOut: function (s, c) {
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * c * c * c;
                }
                return 1 / 2 * ((c -= 2) * c * c + 2);
            },
            quartIn: function (s, c) {
                return (c /= s) * c * c * c;
            },
            quartOut: function (s, c) {
                return -1 * ((c = c / s - 1) * c * c * c - 1);
            },
            quartInOut: function (s, c) {
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * c * c * c * c;
                }
                return -1 / 2 * ((c -= 2) * c * c * c - 2);
            },
            quintIn: function (s, c) {
                return (c /= s) * c * c * c * c;
            },
            quintOut: function (s, c) {
                return ((c = c / s - 1) * c * c * c * c + 1);
            },
            quintInOut: function (s, c) {
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * c * c * c * c * c;
                }
                return 1 / 2 * ((c -= 2) * c * c * c * c + 2);
            },
            expoIn: function (s, c) {
                return (c == 0) ? 0 : Math.pow(2, 10 * (c / s - 1));
            },
            expoOut: function (s, c) {
                return (c == s) ? 1 : (-Math.pow(2, -10 * c / s) + 1);
            },
            expoInOut: function (s, c) {
                if (c == 0) {
                    return 0;
                }
                if (c == s) {
                    return 1;
                }
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * Math.pow(2, 10 * (c - 1));
                }
                return 1 / 2 * (-Math.pow(2, -10 * --c) + 2);
            },
            circIn: function (s, c) {
                return -1 * (Math.sqrt(1 - (c /= s) * c) - 1);
            },
            circOut: function (s, c) {
                return Math.sqrt(1 - (c = c / s - 1) * c);
            },
            circInOut: function (s, c) {
                if ((c /= s / 2) < 1) {
                    return -1 / 2 * (Math.sqrt(1 - c * c) - 1);
                }
                return 1 / 2 * (Math.sqrt(1 - (c -= 2) * c) + 1);
            },
            bounceIn: function (s, c) {
                return 1 - ood.Dom.$AnimateEffects.bounceOut(s, s - c);
            },
            bounceOut: function (s, c) {
                var k = 7.5625;
                if ((c /= s) < (1 / 2.75)) {
                    return (k * c * c);
                } else if (c < (2 / 2.75)) {
                    return (k * (c -= (1.5 / 2.75)) * c + .75);
                } else if (c < (2.5 / 2.75)) {
                    return (k * (c -= (2.25 / 2.75)) * c + .9375);
                } else {
                    return (k * (c -= (2.625 / 2.75)) * c + .984375);
                }
            },
            bounceInOut: function (s, c) {
                if (c < s / 2) {
                    return ood.Dom.$AnimateEffects.bounceIn(s, c * 2) * .5;
                } else {
                    return ood.Dom.$AnimateEffects.bounceOut(s, c * 2 - s) * .5 + 1 * .5;
                }
            },
            backIn: function (s, c) {
                var k = 1.70158;
                return (c /= s) * c * ((k + 1) * c - k);
            },
            backOut: function (s, c) {
                var k = 1.70158;
                return ((c = c / s - 1) * c * ((k + 1) * c + k) + 1);
            },
            backInOut: function (s, c) {
                var k = 1.70158;
                if ((c /= s / 2) < 1) {
                    return 1 / 2 * (c * c * (((k *= (1.525)) + 1) * c - k));
                }
                return 1 / 2 * ((c -= 2) * c * (((k *= (1.525)) + 1) * c + k) + 2);
            },
            elasticIn: function (s, c, p, a, z) {
                if (c == 0) {
                    return 0;
                }
                if ((c /= s) == 1) {
                    return 1;
                }
                if (!z) {
                    z = s * .3;
                }
                if (!a || a < 1) {
                    a = 1;
                    var k = z / 4;
                } else {
                    var k = z / (2 * Math.PI) * Math.asin(1 / a);
                }
                return -(a * Math.pow(2, 10 * (c -= 1)) * Math.sin((c * s - k) * (2 * Math.PI) / z));
            },
            elasticOut: function (s, c, p, a, z) {
                if (c == 0) {
                    return 0;
                }
                if ((c /= s) == 1) {
                    return 1;
                }
                if (!z) {
                    z = s * .3;
                }
                if (!a || a < 1) {
                    a = 1;
                    var k = z / 4;
                } else {
                    var k = z / (2 * Math.PI) * Math.asin(1 / a);
                }
                return (a * Math.pow(2, -10 * c) * Math.sin((c * s - k) * (2 * Math.PI) / z) + 1);
            },
            elasticInOut: function (s, c, p, a, z) {
                if (c == 0) {
                    return 0;
                }
                if ((c /= s / 2) == 2) {
                    return 1;
                }
                if (!z) {
                    z = s * (.3 * 1.5);
                }
                if (!a || a < 1) {
                    a = 1;
                    var k = z / 4;
                } else {
                    var k = z / (2 * Math.PI) * Math.asin(1 / a);
                }
                if (c < 1) {
                    return -.5 * (a * Math.pow(2, 10 * (c -= 1)) * Math.sin((c * s - k) * (2 * Math.PI) / z));
                }
                return a * Math.pow(2, -10 * (c -= 1)) * Math.sin((c * s - k) * (2 * Math.PI) / z) * .5 + 1;
            }
        },
        $preDefinedAnims: {
            blinkAlert: {
                endpoints: {opacity: [1, 0]},
                duration: 200,
                restore: true,
                times: 3
            },
            blinkAlertLoop: {
                endpoints: {opacity: [1, 0]},
                duration: 500,
                restore: true,
                times: -1
            },
            rotateAlert: {
                endpoints: {rotate: [0, 360]},
                duration: 400,
                restore: false
            },
            rotateAlertLoop1: {
                endpoints: {rotate: [0, 360]},
                duration: 2000,
                restore: false,
                times: -1
            },
            rotateAlertLoop2: {
                endpoints: {rotate: [0, -360]},
                duration: 2000,
                returned: false,
                times: -1
            },
            zoomAlert: {
                endpoints: {scaleX: [1, 1.1], scaleY: [1, 1.1]},
                duration: 100,
                restore: true,
                times: 3
            },
            translateXAlert: {
                endpoints: {translateX: [0, 5]},
                duration: 100,
                restore: true,
                times: 3
            },
            translateYAlert: {
                endpoints: {translateY: [0, 5]},
                duration: 100,
                restore: true,
                times: 3
            }
        },
        $preDefinedEffects: {
            "Classic": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], scaleX: [.75, 1], scaleY: [.75, 1]}
            }, {type: "circIn", duration: 200, endpoints: {opacity: [1, 0], scaleX: [1, .75], scaleY: [1, .75]}}],
            "Blur": [{type: "circOut", duration: 200, endpoints: {opacity: [0, 1]}}, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0]}
            }],
            "Drop": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], translateY: ["-25%", "0%"], scaleY: [.5, 1]}
            }, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], translateY: ["0%", "-25%"], scaleY: [1, .5]}
            }],
            "From Below": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], scaleX: [0, 1], scaleY: [0, 1]}
            }, {type: "circIn", duration: 200, endpoints: {opacity: [1, 0], scaleX: [1, 0], scaleY: [1, 0]}}],
            "From Above": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], scaleX: [2, 1], scaleY: [2, 1]}
            }, {type: "circIn", duration: 200, endpoints: {opacity: [1, 0], scaleX: [1, 2], scaleY: [1, 2]}}],
            "Slide In LR": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], translateX: ["-150%", "0%"]/*,scaleX:[.2,1],scaleY:[.2,1]*/}
            }, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], translateX: ["0%", "150%"]/*,scaleX:[1,.2],scaleY:[1,.2]*/}
            }],

            "Slide In LR": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], translateX: ["-150%", "0%"]/*,scaleX:[.2,1],scaleY:[.2,1]*/}
            }, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], translateX: ["0%", "150%"]/*,scaleX:[1,.2],scaleY:[1,.2]*/}
            }],

            "Slide In TB": [{
                type: "circOut",
                duration: 200,
                endpoints: {opacity: [0, 1], translateY: ["-150%", "0%"]/*,/*scaleX:[.2,1],scaleY:[.2,1]*/}
            }, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], translateY: ["0%", "150%"]/*,scaleX:[1,.2],scaleY:[1,.2]*/}
            }],
            "Flip V": [{type: "circOut", duration: 200, endpoints: {opacity: [0, 1], scaleY: [0, 1]}}, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], scaleY: [1, 0]}
            }],
            "Flip H": [{type: "circOut", duration: 200, endpoints: {opacity: [0, 1], scaleX: [0, 1]}}, {
                type: "circIn",
                duration: 200,
                endpoints: {opacity: [1, 0], scaleX: [1, 0]}
            }]
        },
        _getEffects: function (key, isIn) {
            if (key && typeof(key) == "string") {
                key = this.$preDefinedEffects[key];
                key = key ? isIn ? key[0] : key[1] : null;
            }
            if (key && ood.browser.ie && ood.browser.ver <= 8) {
                ood.filter(key, function (o, i) {
                    return !!ood.Dom._cssfake[i];
                });
            }
            return key;
        },
        _setUnitStyle: function (node, key, value) {
            if (!node || node.nodeType != 1) return false;
            var style = node.style;
            if (value || value === 0) {
                value = ood.CSS.$addu(value);
                if (value && (key == 'width' || key == 'height') && value.charAt(0) == '-') value = '0';
                if (style[key] != value) {
                    style[key] = value;
                    return true;
                }
            }
            return false;
        },
        _emptyDivId: "ood.empty:",
        getEmptyDiv: function (pid, sequence) {
            var i = 1, id, rt, style, o, t, count = 0, doc = document,
                body = pid && (pid = ood(pid)).get(0) || doc.body,
                ini = function (o) {
                    o.id = id;
                    // position:relative; is for text wrap bug
                    ood([o]).attr('style', 'position:absolute;visibility:hidden;overflow:visible;left:' + ood.Dom.HIDE_VALUE + ';top:' + ood.Dom.HIDE_VALUE + ';');
                };
            sequence = sequence || 1;
            pid = body == doc.body ? '' : pid.n0.replace('!', '');
            while (1) {
                id = this._emptyDivId + pid + ":" + i;
                //don't remove this {
                if (o = ood.Dom.byId(id)) {
                    //Using firstChild, for performance
                    if ((!o.firstChild || (o.firstChild.nodeType == 3 && !o.firstChild.nodeValue)) && ++count == sequence)
                        return ood([o]);
                } else {
                    o = doc.createElement('div');
                    ini(o, id);
                    if (body.firstChild)
                        body.insertBefore(o, body.firstChild);
                    else
                        body.appendChild(o);
                    rt = ood([o]);
                    body = o = null;
                    return rt;
                }
                i++;
            }
            body = o = null;
        },
        setCover: function (visible, label, busyIcon, cursor, bgStyle) {
            // get or create first
            var me = arguments.callee,
                id = "ood.temp:cover:",
                id2 = "ood.temp:message:",
                content = (typeof(visible) == 'string' || typeof(visible) == 'function') ? visible : '',
                o1, o2;

            if ((o1 = ood(id)).isEmpty()) {
                ood('body').prepend(o1 = ood.create('<button id="' + id + '" class="ood-node ood-node-div ood-cover ood-cover-global ood-custom" style="position:absolute;display:none;text-align:center;left:0;top:0;border:0;padding:0;margin:0;padding-top:2em;"><div id="' + id2 + '" class="ood-node ood-node-div ood-coverlabel ood-custom"></div></button>'));
                o1.setSelectable(false);
                ood.setNodeData(o1.get(0), 'zIndexIgnore', 1);
            }
            if (ood.Dom.byId(id2)) {
                o2 = ood(id2);
            }

            //clear the last one
            if (!visible) {
                if (typeof me._label == 'string' && me._label !== label)
                    return;
                if (me._showed) {
                    if (o2) o2.empty(false);
                    o1.css({zIndex: 0, cursor: '', display: 'none', cursor: ''});
                    o1.query('style').remove(false);
                    me._showed = false;
                }
                delete me._label;
            } else {
                if (typeof label == 'string') me._label = label;
                var t = ood.win;
                if (!me._showed) {
                    o1.css({
                        zIndex: ood.Dom.TOP_ZINDEX * 10,
                        display: '',
                        width: t.scrollWidth() + 'px',
                        height: t.scrollHeight() + 'px',
                        cursor: cursor || 'progress'
                    });
                    if (busyIcon) o1.addClass('oodcon ood-icon-loading'); else o1.removeClass('oodcon ood-icon-loading');
                    me._showed = true;
                }

                o1.query('style').remove(false);
                if (bgStyle)
                    ood.CSS._appendSS(o1.get(0), ".ood-cover-global:before{" + bgStyle + "}", "", true);

                //show content
                if (content) {
                    if (typeof(content) == 'function') {
                        content(o1, o2);
                    } else if (o2) {
                        o2.html(content + '', false);
                    }
                }
            }
        },

        byId: function (id) {
            return document.getElementById(id || "");
        },
        $hasEventHandler: function (node, name) {
            return ood.getNodeData(node, ['eHandlers', name]);
        },
        /*
        action: uri
        data:hash{key:value}
        method:'post'(default) or 'get'
        target: uri target: _blank etc.
        */
        submit: function (action, data, method, target, enctype) {
            data = ood.isHash(data) ? data : {};
            data = ood.clone(data, function (o) {
                return o !== undefined
            });

            action = action || '';
            target = target || (action.substring(0, 6).toLowerCase() == 'mailto' ? '_self' : '_blank');
            var _t = [];
            if (!ood.isEmpty(data)) {
                var file, files = [];
                ood.each(data, function (o, i) {
                    if (o && o['ood.UIProfile'] && o.$oodFileCtrl) {
                        if (file = o.boxing().getUploadObj()) {
                            files.push({id: o.$xid, file: file});
                            file.id = file.name = i;
                            data[i] = file;
                        }
                    }
                });

                method = method || (file ? 'post' : 'get');

                if (method.toLowerCase() == 'get') {
                    window.open(action + "?" + ood.urlEncode(data), target);
                } else {
                    ood.each(data, function (o, i) {
                        if (ood.isDefined(o) && !ood.isElem(o))
                            _t.push('<textarea name="' + i + '">' + (typeof o == 'object' ? ood.serialize(o) : o) + '</textarea>');
                    });
                    _t.push('<input type="hidden" name="rnd" value="' + ood.rand() + '">');
                    _t = ood.str.toDom('<form target="' + target + '" action="' + action + '" method="' + method + (enctype ? '" enctype="' + enctype : '') + '">' + _t.join('') + '</form>');
                    ood.Dom.getEmptyDiv().append(_t);
                    // 1. add files
                    if (files.length) {
                        ood.arr.each(files, function (o, i) {
                            _t.append(o.file);
                        });
                    }
                    // 2.submit
                    _t.get(0).submit();
                    _t.remove();
                    _t = null;
                }
                // 3.restore file input
                if (files.length) {
                    ood.arr.each(files, function (o, i) {
                        if (i = ood.getObject(o.id)) {
                            if (i['ood.UIProfile'] && i.boxing() && i.boxing().setUploadObj) {
                                i.boxing().setUploadObj(o.file);
                            }
                        }
                    });
                }
            } else {
                window.open(action, target);
            }
        },
        selectFile: function (callback, accept, multiple) {
            var fileInput = document.createElement("input");
            fileInput.type = "file";
            // "image/*, video/*, audio/*"
            if (accept) fileInput.accept = accept;
            if (multiple) fileInput.multiple = "multiple";

            fileInput.onchange = function () {
                ood.tryF(callback, [this, this.files[0], this.files], this);
            };
            if (!!window.ActiveXObject || "ActiveXObject" in window) {
                var label = document.createElement("div");
                fileInput.appendChild(label);
                label.click();
                fileInput.removeChild(label);
            } else {
                fileInput.click();
            }
            fileInput = null;
        },
        busy: function (id, busyMsg, busyIcon, cursor, bgStyle) {
            ood.Dom.setCover(busyMsg || true, id, busyIcon, cursor, bgStyle);
        },
        free: function (id) {
            ood.Dom.setCover(false, id);
        },
        animate: function (css, endpoints, onStart, onEnd, duration, step, type, threadid, unit, restore, times) {
            var node = document.createElement('div');
            ood.merge(css, {position: 'absolute', left: this.HIDE_VALUE, zIndex: this.TOP_ZINDEX++});
            ood.Dom.setStyle(node, css);
            document.body.appendChild(node);
            return ood([node]).animate(endpoints, onStart, function () {
                ood.tryF(onEnd);
                if (node.parentNode)
                    node.parentNode.removeChild(node);
                node = null;
            }, duration, step, type, threadid, unit, restore, times);
        },
        //plugin event function to ood.Dom
        $enableEvents: function (name) {
            if (!ood.isArr(name)) name = [name];
            var self = this, f;
            ood.arr.each(name, function (o) {
                f = function (fun, label, flag) {
                    if (typeof fun == 'function')
                        return this.$addEvent(o, fun, label, flag);
                    else if (fun === null)
                        return this.$removeEvent(o, label, flag);
                    var args = arguments[1] || {};
                    args.$oodall = (arguments[0] === true);
                    return this.$fireEvent(o, args)
                };
                f.$event$ = 1;
                self.plugIn(o, f)
            });
        }
    },
    After: function (d) {
        var self = this;
        //getter
        ood.each({
            parent: ['y', false],
            prev: ['x', false],
            next: ['x', true],
            first: ['y', true],
            last: ['y', 1]
        }, function (o, i) {
            self.plugIn(i, function (index) {
                return this.$iterator(o[0], o[1], true, index || 1)
            });
        });

        //readonly profile
        ood.arr.each(ood.toArr('offsetLeft,offsetTop,scrollWidth,scrollHeight'), function (o) {
            self.plugIn(o, function () {
                var t = this.get(0), w = window, d = document;
                if (t == w || t == d) {
                    if ("scrollWidth" == o || "scrollHeight" == o) {
                        var a = d.documentElement, b = d.body;
                        return Math.max(a[o], b[o]);
                    } else
                        t = ood.browser.contentBox ? d.documentElement : d.body;
                }
                return t[o];
            })
        });

        var p = 'padding', m = 'margin', b = 'border', c = 'inner', o = 'offset', r = 'outer', w = 'width',
            h = 'height', W = 'Width', H = 'Height', T = 'Top', L = 'Left', t = 'top', l = 'left', R = 'Right',
            B = 'Bottom';
        //dimesion
        ood.arr.each([['_' + p + 'H', p + T, p + B],
            ['_' + p + 'W', p + L, p + R],
            ['_' + b + 'H', b + T + W, b + B + W],
            ['_' + b + 'W', b + L + W, b + R + W],
            ['_' + m + 'W', m + L, m + R],
            ['_' + m + 'H', m + T, m + B]
        ], function (o) {
            //use get Style dir
            var node, fun = ood.Dom.getStyle;
            self.plugIn(o[0], function (type) {
                type = type || 'both';
                node = this.get(0);
                return ((type == 'both' || type == 'left' || type == 'top') ? ood.CSS.$px(fun(node, o[1]), node) : 0)
                    + ((type == 'both' || type == 'right' || type == 'bottom') ? ood.CSS.$px(fun(node, o[2]), node) : 0) || 0;
            })
        });
        /*
        get W/H for

        1:width
        2:innerWidth
        3:offsetWidth
        4:outerWidth

        content-box
        +--------------------------+
        |margin                    |
        | +----------------------+ |
        | |border                | |
        | | +------------------+ | |
        | | |padding           | | |
        | | | +--------------+ | | |
        | | | |   content    | | | |
        |-|-|-|--------------|-|-|-|
        | | | |<-css width ->| | | |
        | | |<-  innerWidth  ->| | |
        | |<--  offsetWidth   -->| |
        |<--    outerWidth      -->|

        border-box
        +--------------------------+
        |margin                    |
        | +----------------------+ |
        | |border                | |
        | | +------------------+ | |
        | | |padding           | | |
        | | | +--------------+ | | |
        | | | |   content    | | | |
        |-|-|-|--------------|-|-|-|
        | | |<-   css width  ->| | |
        | | |<-  innerWidth  ->| | |
        | |<--  offsetWidth   -->| |
        |<--    outerWidth      -->|
        */

        ood.arr.each([['_W', w, '_' + p + 'W', '_' + b + 'W', '_' + m + 'W', c + W, o + W],
            ['_H', h, '_' + p + 'H', '_' + b + 'H', '_' + m + 'H', c + H, o + H]], function (o) {
            var _size = function (node, index, value, _in) {
                if (!node || !node.style) {
                    return;
                }

                var n, r, t, style = node.style, contentBox = ood.browser.contentBox,
                    r1 = /%$/,
                    getStyle = ood.Dom.getStyle,
                    f = ood.Dom._setUnitStyle, type = typeof value, t1;
                if (type == 'undefined' || type == 'boolean') {
                    if (value === true) {
                        n = (getStyle(node, 'display') == 'none') || node.offsetHeight === 0;
                        if (n) {
                            var temp = ood.Dom.getEmptyDiv().html('*', false);
                            ood([node]).swap(temp);
                            var b, p, d;
                            b = style.visibility, p = style.position, d = style.display;
                            p = p || '';
                            b = b || '';
                            d = d || '';
                            style.visibility = 'hidden';
                            style.position = 'absolute';
                            style.display = 'block';
                        }
                    }
                    t = ood([node]);
                    switch (index) {
                        case 1:
                            r = getStyle(node, o[1]);
                            if ((isNaN(parseFloat(r)) || r1.test(r)) && !_in)
                                r = _size(node, 2, undefined, true) - (contentBox ? t[o[2]]() : 0);
                            r = ood.CSS.$px(r, node) || 0;
                            break;
                        case 2:
                            if (node === document || node === window) {
                                r = ood(node)[o[1]]();
                            } else {
                                r = node[o[6]];
                                //get from css setting before css applied
                                if (!r) {
                                    if (!_in) r = _size(node, 1, undefined, true) + (contentBox ? t[o[2]]() : 0);
                                } else r -= t[o[3]]();
                            }
                            break;
                        case 3:
                            if (node === document || node === window) {
                                r = ood(node)[o[1]]();
                            } else {
                                r = node[o[6]];
                                //get from css setting before css applied
                                if (!r) r = _size(node, 1, value, true) + (contentBox ? t[o[2]]() : 0) + t[o[3]]();
                            }
                            break;
                        case 4:
                            r = _size(node, 3, value);
                            r += t[o[4]]();
                            break;
                    }
                    if (n) {
                        style.display = d;
                        style.position = p;
                        style.visibility = b;
                        t.swap(temp);
                        temp.empty(false);
                    }
                    return parseFloat(r) || 0;
                } else {
                    switch (index) {
                        case 1:
                            if (f(node, o[1], value))
                                if (ood.Dom.$hasEventHandler(node, 'onsize')) {
                                    var args = {};
                                    args[o[1]] = 1;
                                    ood([node]).onSize(true, args);
                                }
                            break;
                        case 2:
                            _size(node, 1, value - (contentBox ? ood([node])[o[2]]() : 0));
                            break;
                        case 3:
                            //back value for offsetHeight/offsetWidth slowly
                            _size(node, 1, value - (t = ood([node]))[o[3]]() - (contentBox ? t[o[2]]() : 0));
                            break;
                        case 4:
                            _size(node, 1, value - (t = ood([node]))[o[4]]() - t[o[3]]() - (contentBox ? t[o[2]]() : 0));
                            break;
                    }
                    //if(node._bp)
                    //    node['_'+o[6]]=null;
                }
            };
            self.plugIn(o[0], _size)
        });
        ood.arr.each([[c + W, '_W', 2], [o + W, '_W', 3], [r + W, '_W', 4],
            [c + H, '_H', 2], [o + H, '_H', 3], [r + H, '_H', 4]], function (o) {
            self.plugIn(o[0], function (value) {
                var type = typeof value;
                if (type == 'undefined' || type == 'boolean')
                    return this[o[1]](this.get(0), o[2], value);
                else
                    return this.each(function (v) {
                        this[o[1]](v, o[2], value);
                    });
            })
        });
        ood.arr.each([[l + 'By', l], [t + 'By', t], [w + 'By', w], [h + 'By', h]], function (o) {
            self.plugIn(o[0], function (offset, triggerEvent) {
                if (offset === 0) return this;
                var m, args, k = o[1];
                return this.each(function (node) {
                    m = ood.use(node.$xid)[k]();
                    m = (parseFloat(m) || 0) + offset;
                    if (k == 'width' || k == 'height') m = m > 0 ? m : 0;
                    node.style[k] = ood.CSS.$forceu(m, null, node);
                    if (triggerEvent) {
                        args = {};
                        args[k] = 1;
                        var f = ood.Dom.$hasEventHandler;
                        if ((k == 'left' || k == 'top') && f(node, 'onmove'))
                            ood([node]).onMove(true, args);
                        if ((k == 'width' || k == 'height') && f(node, 'onsize')) {
                            ood([node]).onSize(true, args);
                        }
                    }
                }, this)
            });
        });
        ood.arr.each(['scrollLeft', 'scrollTop'], function (o) {
            self.plugIn(o, function (value) {
                var a = document.documentElement, b = document.body, v;
                if (value !== undefined)
                    return this.each(function (v) {
                        if (v === window || v === document) {
                            if (a) a[o] = value;
                            if (b) b[o] = value;
                        } else if (v) v[o] = value;
                    });
                else
                    return (v = this.get(0)) ? (v === window || v === document) ? (window["scrollTop" == o ? "pageYOffset" : "pageXOffset"] || (a[o] || b[o] || 0))
                        : v[o]
                        : 0;
            })
        });
        ood.arr.each('width,height,left,top'.split(','), function (o) {
            self.plugIn(o, function (value) {
                var self = this, node = self.get(0), b = ood.browser, type = typeof value, doc = document, t, style;
                if (!node || node.nodeType == 3) return;
                if (type == 'undefined' || type == 'boolean') {
                    if ((o == 'width' && (t = 'Width')) || (o == 'height' && (t = 'Height'))) {
                        if (doc === node) return Math.max(doc.body['scroll' + t], doc.body['offset' + t], doc.documentElement['scroll' + t], doc.documentElement['offset' + t]);
                        if (window === node) return b.opr ? Math.max(doc.body['client' + t], window['inner' + t]) : b.kde ? window['inner' + t] : (ood.browser.contentBox && doc.documentElement['client' + t]) || doc.body['client' + t];
                    }
                    style = node.style;
                    // give shortcut
                    // we force to get px number of width/height
                    if (o == 'width') value = (ood.CSS.$isPx(style.width) && parseFloat(style.width)) || self._W(node, 1, value);
                    else if (o == 'height') value = (ood.CSS.$isPx(style.height) && parseFloat(style.height)) || self._H(node, 1, value);
                    else
                        value = ood.Dom.getStyle(node, o, true);
                    return (value == 'auto' || value === '') ? value : (value || 0);
                } else {
                    var f = ood.Dom._setUnitStyle, t, a,
                        av = ood.CSS.$addu(value);
                    return self.each(function (v) {
                        if (v.nodeType != 1) return;
                        if (v.style[o] !== av) {
                            if (o == 'width') self._W(v, 1, value);
                            else if (o == 'height') self._H(v, 1, value);
                            else {
                                if (f(v, o, value))
                                    if ((o == 'top' || o == 'left') && ood.Dom.$hasEventHandler(node, 'onmove')) {
                                        a = {};
                                        a[o] = 1;
                                        ood([v]).onMove(true, a);
                                    }
                            }
                        }
                    });
                }
            });
        });

        //ood.Dom event
        ood.arr.each(ood.Event._events, function (o) {
            ood.arr.each(ood.Event._getEventName(o), function (o) {
                self.$enableEvents(o);
            })
        });
    },
    Initialize: function () {
        var w = window, d = document;
        ood.browser.contentBox = ood(d.documentElement).contentBox();
        ood.set(ood.$cache.domPurgeData, '!window', {$xid: '!window', element: w});
        ood.set(ood.$cache.domPurgeData, '!document', {$xid: '!document', element: d});

        ood.win = ood(['!window'], false);
        ood.doc = ood(['!document'], false);
        ood.frame = ood.win;

        ood.busy = ood.Dom.busy;
        ood.free = ood.Dom.free;

        ood.$inlineBlock = ood.browser.gek
            ? ood.browser.ver < 3
                ? ['-moz-inline-block', '-moz-inline-box', 'inline-block']
                : ['inline-block']
            : (ood.browser.ie && ood.browser.ver <= 6)
                ? ['inline-block', 'inline']
                : ['inline-block'];
        var fun = function (p, e, cache, keydown) {
            var event = ood.Event, set, hash, rtnf, rst, remove = {},
                ks = event.getKey(e);
            if (ks) {
                if (ks[0].length == 1) ks[0] = ks[0].toLowerCase();
                //if hot function return false, stop bubble
                if (arr = cache[ks.join(":")]) {
                    ood.arr.each(arr, function (key, i) {
                        set = arr[key];
                        if (set) {
                            // remove hook for non-exist dom
                            if (set[3] && (typeof set[3] == 'function' ? false === (set[3])() : (!ood(set[3]).size()))) {
                                // do nothing and detach it
                                delete arr[key];
                                remove[i] = 1;
                                return;
                            }
                            rst = ood.tryF(set[0], set[1] || [arr, i, key], set[2]);
                            if (rst === false) {
                                rtnf = 1;
                                return false;
                            } else if (rst === true) {
                                // detach it
                                delete arr[key];
                                remove[i] = 1;
                            }
                        }
                    }, null, true);
                    // remove
                    ood.filter(arr, function (key, i) {
                        return !remove[i];
                    });
                    if (rtnf) {
                        event.stopBubble(e);
                        return false;
                    }
                }
                if (ood.Module) {
                    ood.arr.each(ood.Module._cache, function (m) {
                        // by created order
                        if (m._evsClsBuildIn && ('onHookKey' in m._evsClsBuildIn)) {
                            // function or pseudocode
                            if (ood.isFun(f = m._evsClsBuildIn.onHookKey) || (ood.isArr(f) && f[0].type))
                                m.fireEvent('onHookKey', [m, ks, keydown, e]);
                        }
                        else if (m._evsPClsBuildIn && ('onHookKey' in m._evsPClsBuildIn)) {
                            // function or pseudocode
                            if (ood.isFun(f = m._evsPClsBuildIn.onHookKey) || (ood.isArr(f) && f[0].type))
                                m.fireEvent('onHookKey', [m, ks, keydown, e]);
                        }
                    });
                }
            }
            return true;
        };
        //hot keys
        ood.doc.onKeydown(function (p, e) {
            ood.Event.$keyboard = ood.Event.getKey(e);
            fun(p, e, ood.$cache.hookKey, true)
        }, "document")
            .onKeyup(function (p, e) {
                delete ood.Event.$keyboard;
                fun(p, e, ood.$cache.hookKeyUp, false)
            }, "document");

        //hook link(<a ...>xxx</a>) click action
        //if(ood.browser.ie || ood.browser.kde)
        ood.doc.onClick(function (p, e, src) {
            var o = ood.Event.getSrc(e),
                i = 0, b, href;
            do {
                if (o.nodeName == 'A') {
                    b = true;
                    break;
                }
                if (++i > 8) break;
            } while (o = o.parentNode)
            if (b) {
                href = ood.str.trim(o.href || "").toLowerCase();
                if (ood.History) {
                    var s = location.href.split('#')[0];
                    if (!ood.Event.getKey(e).shiftKey && ood.Event.getBtn(e) == 'left' && (href.indexOf(s + '#') == 0 || href.indexOf('#') == 0)) {
                        ood.History.setFI(o.href.replace(s, ''));
                    }
                }
                //**** In IE, click a fake(javascript: or #) href(onclick not return false) will break the current script downloading(SAajx)
                //**** You have to return false here
                if (ood.browser.ie && (href.indexOf('javascript:') == 0 || href.indexOf('#') !== -1)) return false;
            }
        }, 'hookA', 0);

        var _ieselectstart = function (n, v) {
            n = window.event.srcElement;
            while (n && n.nodeName && n.nodeName != "BODY" && n.nodeName != "HTML") {
                if (v = ood.getNodeData(n, "_onoodsel"))
                    return v != 'false';
                // check self only
                if (n.nodeName == "INPUT" || n.nodeName == "TEXTAREA")
                    break;
                n = n.parentNode;
            }
            return true;
        };
        if (ood.browser.ie && ood.browser.ver < 10 && d.body)
            ood.Event._addEventListener(d.body, "selectstart", _ieselectstart);

        //free memory
        // ood.win.afterUnload(ood._destroy = function () {
        //     var t,
        //         lowie = ood.browser.ie && ood.browser.ver <= 8,
        //         e = ood.Event,
        //         _cw = function (w, k) {
        //             w[k] = undefined;
        //             if (!lowie)
        //                 delete w[k];
        //         };
        //
        //     if (ood.History._checker) e._removeEventListener(w, "hashchange", ood.History._checker);
        //     e._removeEventListener(d.body, "selectstart", _ieselectstart);
        //     e._removeEventListener(w, "resize", e.$eventhandler);
        //
        //     e._removeEventListener(w, "mousewheel", e.$eventhandler3);
        //     e._removeEventListener(d, "mousewheel", e.$eventhandler3);
        //     // firfox only
        //     e._removeEventListener(w, "DOMMouseScroll", e.$eventhandler3);
        //     // for simulation mouse event in touable device
        //     if (ood.browser.isTouch) {
        //         e._removeEventListener(d,
        //             (ood.browser.ie && w.PointerEvent) ? "pointerdown" :
        //                 (ood.browser.ie && w.MSPointerEvent) ? "MSPointerDown" :
        //                     "touchstart", e._simulateMousedown);
        //         e._removeEventListener(d,
        //             (ood.browser.ie && ood.browser.ver >= 11) ? "pointerup" :
        //                 (ood.browser.ie && ood.browser.ver >= 10) ? "MSPointerUp" :
        //                     "touchend", e._simulateMouseup);
        //     }
        //
        //     // ood.win.afterUnload ...
        //     for (var i in (t = ood.$cache.domPurgeData))
        //         if (t[i].eHandlers)
        //             ood(i).$clearEvent();
        //
        //     // destroy all widgets and moudles
        //     // ood('body').empty(true,true);
        //     for (var i in (t = ood._pool)) {
        //         t[i] && t[i].destroy && t[i].destroy(1, 1);
        //         t[i] && t[i].Instace && t[i].Instace.destroy && t[i].Instace.destroy(1, 1);
        //     }
        //
        //     // root module ref
        //     _cw(w, ood.ini.rootModuleName);
        //     if (w.Raphael && w.Raphael._in_ood) {
        //         _cw(w, 'Raphael');
        //     }
        //
        //     ood.SC.__gc();
        //     ood.Thread.__gc();
        //     ood.Class.__gc();
        //     if (/ood\.Class\.apply/.test(w.Class)) _cw(w, 'Class');
        //     if ((t = ood.Namespace._all)) for (var i in t) _cw(w, t[i]);
        //     ood.breakO(ood.$cache, 2);
        //     ood.breakO([ood.Class, ood], 3);
        //     _cw(w, 'ood_ini');
        //     _cw(w, 'ood');
        //
        //     w = d = null;
        // }, "window", -1);

    }
});/*
profile input:
===========================
    [dragType]: String , "move","copy","deep_copy","shape","icon","blank" and "none", default is "shape"
        "blank": moves a empty proxy when mouse moves
        "move": moves target object directly when mouse moves
        "copy": moves a copy of target object when mouse moves
        "deep_copy": moves a deep copy of target object when mouse moves
        "shape": moves a shape of target object when mouse moves
        "icon": moves a icon that represents target object when mouse moves
        "none": moves mouse only
-------------------------
    [dragDefer] :  Number, when [ood.DragDrop.startDrag] is called, the real drag action will be triggered after [document.onmousemove] runs [dragDefer] times, default is 0;
-------------------------
    [magneticDistance]: Number,
    [xMagneticLines]: Array of Number,
    [yMagneticLines]: Array of Number,
        Magnetic setting:
        yMagneticLines 1                      2                     3
              |                      |                     |       xMagneticLines
          ----+----------------------+---------------------+-------1
              |                      |                     |
              |                      |                     |
              |                      |                     |
              |                      |                     |
          ----+----------------------+---------------------+-------2
              |                      |                     |
              |                      |                     |
              |                      |                     |
          ----+----------------------+---------------------+-------3
              |                      |                     |

        magneticDistance
         +-------------
         |*************
         |*************
         |**
         |**
         |**
-------------------------
    [widthIncrement]: Number,
    [heightIncrement]: Number,
        Increment setting:
                   widthIncrement
               <-------------------->
              |                      |                     |
          ----+----------------------+---------------------+-------
              |                      |                     |
heightIncrement|                      |                     |
              |                      |                     |
              |                      |                     |
          ----+----------------------+---------------------+-------
              |                      |                     |
              |                      |                     |
              |                      |                     |
              |                      |                     |
          ----+----------------------+---------------------+-------
              |                      |                     |
              |                      |                     |
-------------------------
    [horizontalOnly]: Number,
    [verticalOnly]: Number,
    horizontalOnly
    ------------------------------------------
                ****************
                ****************
                ****************
                ****************
                ****************
                ****************
    ------------------------------------------
    verticalOnly
               |                |
               |                |
               |****************|
               |****************|
               |****************|
               |****************|
               |****************|
               |****************|
               |                |
               |                |
-------------------------
    [maxBottomOffset]: Number,
    [maxLeftOffset]: Number,
    [maxRightOffset]: Number,
    [maxTopOffset]: Number,
        you can set the limited offset region
        +----------------------------------------------+
        |              |                               |
        |              |maxTopOffset                   |
        |<------------>****************<-------------->|
        |maxLeftOffset**************** maxRightOffset  |
        |              ****************                |
        |              ****************                |
        |              ****************                |
        |              ****************                |
        |              |maxBottomOffset                |
        |              |                               |
        +----------------------------------------------+
-------------------------
    [targetReposition]: <bool>,

    //ini pos and size
    [targetLeft]: Number
    [targetTop]: Number
    [targetWidth]: Number
    [targetHeight]: Number
    [targetCSS]: <object>
        You can set position and size when drag start:
                      targetLeft
                      |
                      |
        targetTop  ---**************** |
                      **************** |
                      **************** |
                      **************** |targetHeight
                      **************** |
                      **************** |
                     |<--targetWidth ->+
-------------------------
    //properties
    [dragCursor]: <string>
-------------------------
    //for drag data
    [dragKey]
    [dragData]

profile output: readonly
===========================
ood.DragDrop.getProfile():
    x  :current X value of mouse;
    y  :current Y value of mouse;
    ox: mouse original X when drag start;
    oy: mouse original Y when drag start;
    curPos:{left:xx,top:xx}: current css pos of the dragging node;
    offset : {x:,y}: offset from now to origin
    restrictedLeft : Number
    restrictedRight : Number
    restrictedTop : Number
    restrictedBottom : Number
    isWorking: Bool.
    proxyNode: ood.Dom object,
    dropElement: String, DOM element id.
*/
ood.Class('ood.DragDrop',null,{
    Static:{
        _eh:"_dd",
        _id:"ood.dd:proxy:",
        _idi:"ood.dd:td:",
        _type:{blank:1,move:1,shape:1,deep_copy:1,copy:1,icon:1,none:1},
        _Icons:{none:'0 0', move:'0 -16px', link:'0 -32px',add:'0 -48px'},
        _profile:{},

        //get left for cssPos
        _left:function(value){
            var proxySize=this.$proxySize;
            with(this._profile){
                if(magneticDistance>0 && xMagneticLines.length){
                    var l=xMagneticLines.length;
                    while(l--)
                        if(Math.abs(value + proxySize - xMagneticLines[l])<=magneticDistance)
                            return xMagneticLines[l] - proxySize;
                }
                if(widthIncrement>1)
                   return Math.floor((value + proxySize)/widthIncrement)*widthIncrement - proxySize;
                return value;
            }
        },
        //get top for cssPos
        _top:function(value){
            var proxySize=this.$proxySize;
            with(this._profile){
                if(magneticDistance>0 && yMagneticLines.length){
                    var l=yMagneticLines.length;
                    while(l--)
                        if(Math.abs(value + proxySize - yMagneticLines[l])<=magneticDistance)
                            return yMagneticLines[l] - proxySize;
                }
                if(heightIncrement>1)
                    return Math.floor((value + proxySize)/heightIncrement)*heightIncrement - proxySize;
                return value;
            }
        },

        _ini:function(o){
            var d=this,p=d._profile,_t=ood.win;

            d._box = { width :_t.width()+_t.scrollLeft(),  height :_t.height()+_t.scrollTop()};

            p.ox = p.x;
            p.oy = p.y;

            if(d._proxy = o){
                d._proxystyle=o.get(0).style;

                //ini cssPos here
                d._profile.curPos = d._cssPos= d._proxy.cssPos();

                d._cssPos_x = p.x - d._cssPos.left;
                d._cssPos_y = p.y - d._cssPos.top;

                p.restrictedLeft = p.x - (p.maxLeftOffset||0);
                p.restrictedRight =  p.x + (p.maxRightOffset||0);
                p.restrictedTop = p.y - (p.maxTopOffset||0);
                p.restrictedBottom = p.y + (p.maxBottomOffset||0);

                //here
                d._proxyLeft = d._pre.left = d._cssPos.left;
                d._proxyTop = d._pre.top = d._cssPos.top;

                if("move" !== p.dragType){
                    d._proxy.css('zIndex',ood.Dom.TOP_ZINDEX*10);
                    ood.setNodeData(d._proxy.get(0),'zIndexIgnore', 1);
                }
            }

        },
        _reset:function(){
            var d=this,NULL=null,FALSE=false;
            //reset
            ood.tryF(d.$reset);
            d.setDropFace();
            d._resetProxy();

            d.$proxySize=50;
            //event
            d.$mousemove=d.$mouseup=d.$onselectstart=d.$ondragstart='*';

            //reset private vars
            d._cursor='';
            d._pre={};
            d._proxyLeft=d._proxyTop=d._cssPos_x=d._cssPos_y=0;
            d._stop=FALSE;
            if(d._onDrag && d._onDrag.tasks){
                d._onDrag.tasks.length=0;
                delete d._onDrag.tasks;
            }
            if(d._onDragover && d._onDragover.tasks){
                d._onDragover.tasks.length=0;
                delete d._onDragover.tasks;
            }
            if(d._c_droppable){d._c_droppable.length=0;}
            d._c_droppable=d._c_dropactive=d._cssPos=d._box=d._dropElement=d._source=d._proxy=d._proxystyle=d._onDrag=d._onDragover=NULL;
            //reset profile
            d._profile={
                // the unqiue id for dd
                $id:ood.rand(),
                dragType:'shape',
                dragCursor:'move',
                targetReposition:true,

                dragIcon:ood.ini.img_dd,
                magneticDistance:0,
                xMagneticLines:[],
                yMagneticLines:[],
                widthIncrement:0,
                heightIncrement:0,
                dragDefer:0,

                horizontalOnly:FALSE,
                verticalOnly:FALSE,
                maxBottomOffset:NULL,
                maxLeftOffset:NULL,
                maxRightOffset:NULL,
                maxTopOffset:NULL,

                targetNode:NULL,
                targetCSS:NULL,
                dragKey:NULL,
                dragData:NULL,
                targetLeft:NULL,
                targetTop:NULL,
                targetWidth:NULL,
                targetHeight:NULL,
                targetOffsetParent:NULL,
                targetCallback:NULL,
                tagVar:NULL,

                shadowFrom:NULL,

                //Cant input the following items:
                proxyNode:NULL,
                x:0,
                y:0,
                ox:0,
                oy:0,
                curPos:{},
                offset:{},
                isWorking:FALSE,
                restrictedLeft:NULL,
                restrictedRight:NULL,
                restrictedTop:NULL,
                restrictedBottom:NULL,
                dropElement:NULL
            };
            d.__touchingfordd=0;
            return d;
        },
        abort:function(){
            this._stop=true;
        },
        _end:function(){
            var d=this,win=window,doc=document,body=doc.body,md="onmousedown",mm="onmousemove",mu="onmouseup",
                mm2,mu2;
            if(ood.browser.isTouch){
                mm2=(ood.browser.ie&&win.PointerEvent)?"onpointermove":(ood.browser.ie&&win.MSPointerEvent)?"onmspointermove":"ontouchmove";
                mu2=(ood.browser.ie&&win.PointerEvent)?"onpointerup":(ood.browser.ie&&win.MSPointerEvent)?"onmspointerup":"ontouchend";
            }
            
            if(d._proxy) d._unpack();

            //must here
            //if bak, restore
            if(d.$onselectstart!='*')body.onselectstart=d.$onselectstart;
            if(d.$ondragstart!='*')doc.ondragstart=d.$ondragstart;
            //if bak, restore
            if(d.$mousemove!='*')doc[mm]=d.$mousemove;
            if(d.$mouseup!='*')doc[mu]=d.$mouseup;
            if(ood.browser.isTouch){
                if(d.$touchmove!='*')doc[mm2]=d.$touchmove;
                if(d.$touchend!='*')doc[mu2]=d.$touchend;                
            }

            return  d;
        },
        startDrag:function(e, targetNode, profile, dragKey, dragData){
            var d=this,win=window,t;
            if(d._profile.isWorking)return false;
            //clear
            d._end()._reset();
            d._profile.isWorking=true;
            d.__touchingfordd = e.type=="oodtouchdown";

            profile=ood.isHash(profile)?profile:{};
            e = e || win.event;
            // not left button
            if(ood.Event.getBtn(e) !== 'left')
               return true;

            d._source = profile.targetNode = ood(targetNode);
            d._cursor = d._source.css('cursor');

            if((t=profile.targetNode.get(0)) && !t.id){
                t.id=ood.Dom._pickDomId();
                t=null;
            }

            //must set here
            d._defer = profile.dragDefer = ood.isNumb(profile.dragDefer) ? profile.dragDefer : 0;
            if(true===profile.dragCursor)profile.dragCursor=d._cursor;
            if(typeof profile.dragIcon == 'string') profile.dragType="icon";

            var doc=document, body=doc.body, _pos = ood.Event.getPos(e),md="onmousedown",mm="onmousemove",mu="onmouseup",
                mm2,mu2;
            if(ood.browser.isTouch){
                mm2=(ood.browser.ie&&win.PointerEvent)?"onpointermove":(ood.browser.ie&&win.MSPointerEvent)?"onmspointermove":"ontouchmove";
                mu2=(ood.browser.ie&&win.PointerEvent)?"onpointerup":(ood.browser.ie&&win.MSPointerEvent)?"onmspointerup":"ontouchend";
            }

            profile.x = _pos.left;
            profile.y = _pos.top;

            profile.dragKey= dragKey || profile.dragKey || null;
            profile.dragData= dragData  || profile.dragData|| null;

            var fromN=ood.Event.getSrc(e);

            d._start=function(e){
//ie6: mousemove - mousedown =>78 ms
//delay is related to window size, weird
            //                  try{
                var p=d._profile;
                //set profile
                ood.merge(p, profile, "with");

                //call event, you can call abort(set _stoop)
                d._source.beforeDragbegin();

                if(d._stop){d._end()._reset();return false}

                //set ood.Event._preDroppable at the begining of drag, for a dd from a child in a droppable node
                if(ood.Event && (t=d._source.get(0))){
                    ood.Event._preDroppable= t.id;
                    t=null;
                }

                //set default icon
                if(p.dragType=='icon')p.targetReposition=false;

                //ini
                d._ini(p.dragType=='none'?null:d._pack(_pos, p.targetNode));
                // on scrollbar
                if(profile.x >= d._box.width  || profile.y >= d._box.height ){d._end()._reset();return true}

                d._source.onDragbegin();

                //set back first
                if(p.dragDefer<1){
                    d.$mousemove = doc[mm];
                    d.$mouseup = doc[mu];
                    if(ood.browser.isTouch){
                        d.$touchmove = doc[mm2];
                        d.$touchend = doc[mu2];
                    }
                }
                //avoid setcapture
                if(ood.browser.ie)
                    ood.setTimeout(function(){if(fromN.releaseCapture)fromN.releaseCapture()});

                //back up
                doc[mm] = d.$onDrag;
                doc[mu] = d.$onDrop;
                if(ood.browser.isTouch){
                    doc[mm2] = d.$onDrag;
                    doc[mu2] = d.$onDrop;
                }
                
                //for events
                d._source.afterDragbegin();
                //for delay, call ondrag now
                if(p.dragDefer>0)d.$onDrag.call(d, e);
                
                // For touch-only platform
                // In ipad or other touch-only platform, you have to decide the droppable order by youself
                // The later added to DOM the higher the priority
                // Add droppable links
                if(ood.browser.isTouch && d.__touchingfordd){
                    d._c_droppable=[];
                    var cdata=ood.$cache.droppable[p.dragKey],purge=[];
                    ood.arr.each(cdata,function(i){
                        if(!ood.use(i).get(0)){
                            purge.push(i);
                            return;
                        }
                        var ni=ood.use(i),h=ni.offsetHeight(),w=ni.offsetWidth(),v=ni.css('visibility'),hash;
                        if(w&&h&&v!='hidden'){
                            hash=ni.offset();
                            hash.width=w;hash.height=h;hash.id=i;
                            d._c_droppable.unshift(hash);
                        }
                    });
                    // self clear
                    if(purge.length){
                        ood.arr.each(purge,function(key){
                            ood.arr.removeValue(cdata,key);
                        });
                    }
                }
            //                  }catch(e){d._end()._reset();}
            };
            if(ood.browser.ie){
                d.$ondragstart=doc.ondragstart;
                d.$onselectstart=body.onselectstart;
                doc.ondragstart = body.onselectstart = null;
                if(doc.selection && doc.selection.empty)try{doc.selection.empty()}catch(e){}            }
            //avoid select
            ood.Event.stopBubble(e);

            //fire document onmousedown event
            if(profile.targetNode.get(0)!==doc)
                ood(doc).onMousedown(true, ood.Event.getEventPara(e, _pos));

            if(profile.dragDefer<1){
                ood.tryF(d._start,[e],d);
                return false;
            }else{
                //for mouseup before drag
                d.$mouseup = doc[mu];
                doc[mu] = function(e){
                    ood.DragDrop._end()._reset();
                    return ood.tryF(document.onmouseup,[e],null,true);
                };
                if(ood.browser.isTouch){
                    d.$touchend = doc[mu2];
                    doc[mu2]=doc[mu];
                }
                var pbak={};
                //for mousemove before drag
                d.$mousemove = doc[mm];
                doc[mm] = function(e){
                    var p=ood.Event.getPos(e);
                    if(p.left===pbak.left&&p.top===pbak.top)return;
                    pbak=p;
                    if(--d._defer<=0)ood.DragDrop._start(e);
                    return false;
                };
                if(ood.browser.isTouch){
                    d.$touchmove = doc[mm2];
                    doc[mm2]=doc[mm];
                }
            }
//ie6: mousemove - mousedown =>78 ms
        },
        $onDrag:function(e){
            var d=ood.DragDrop,p=d._profile;

            if(d.$SimulateMousemoveInMobileDevice)return false;
            
           //try{
                e = e || window.event;
                //set _stop or (in IE, show alert)
                if(!p.isWorking || d._stop){
                //if(!p.isWorking || d._stop || (ood.browser.ie && (!e.button) )){
                    d.$onDrop(e);
                    return true;
                }

                var _pos=ood.Event.getPos(e);
                p.x=_pos.left;
                p.y=_pos.top;

                if(!p.isWorking)return false;

                if(d._proxy){
                    if(!p.verticalOnly){
                        d._proxyLeft=Math.floor(d._left(
                            ((p.maxLeftOffset!==null && p.x<=p.restrictedLeft)?p.restrictedLeft:
                             (p.maxRightOffset!==null && p.x>=p.restrictedRight)?p.restrictedRight : p.x)
                            - d._cssPos_x)
                        );
                        if(d._proxyLeft-d._pre.left)
                            d._proxystyle.left=Math.round(parseFloat(d._proxyLeft))+'px';
                        d._pre.left=d._proxyLeft;
                        p.curPos.left = d._proxyLeft + d.$proxySize;
                    }
                    if(!p.horizontalOnly){
                        d._proxyTop=Math.floor(d._top(
                            ((p.maxTopOffset!==null && p.y<=p.restrictedTop) ? p.restrictedTop :
                             (p.maxBottomOffset!==null && p.y>=p.restrictedBottom) ? p.restrictedBottom : p.y)
                            - d._cssPos_y)
                        );
                        if(d._proxyTop-d._pre.top)
                            d._proxystyle.top=Math.round(parseFloat(d._proxyTop))+'px';
                        d._pre.top=d._proxyTop;
                        p.curPos.top = d._proxyTop + d.$proxySize;
                    }
                }else{
                    p.curPos.left = p.x;
                    p.curPos.top = p.y;
                    //style='none', no dd.current dd._pre provided
                    //fireEvent
                    //d._source.onDrag(true); //shortcut for mousemove
                }
      
                if(d._onDrag!=1){
                    if(d._onDrag)d._onDrag(e,d._source._get(0));
                    else{
                        //ensure to run once only
                        d._onDrag=1;
                        //if any ondrag event exists, this function will set _onDrag
                        d._source.onDrag(true,ood.Event.getEventPara(e, _pos));
                    }
                }
                
                // For touch-only platform
                // In ipad or other touch-only platform, you have to decide the droppable order by youself
                // The later joined the higher the priority
                if(ood.browser.isTouch && d.__touchingfordd){
                    if(d._c_droppable){
                        for(var i=0,l=d._c_droppable.length;i<l;i++){
                            var o=d._c_droppable[i],
                                target=ood.use(o.id).get(0),
                                oactive=d._c_dropactive,
                                otarget=ood.use(oactive).get(0);

                            if(p.x>=o.left&&p.y>=o.top&&p.x<=(o.left+o.width)&&p.y<=(o.top+o.height)){
                                if(oactive==o.id){
                                    //console.log('in ' +o.id );
                                    var first = e.changedTouches[0];
                                    d.$SimulateMousemoveInMobileDevice=1;
                                    ood.Event.simulateEvent(target,"mousemove",{screenX:first.screenX, screenY:first.screenY, clientX:first.clientX, clientY:first.clientY});
                                    delete d.$SimulateMousemoveInMobileDevice;
                                }else{
                                    ood.Event.simulateEvent(target,"mouseover",{screenX:p.left, screenY:p.top, clientX:p.left, clientY:p.top});
                                    d._c_dropactive=o.id;

                                    //console.log('active ' +o.id);
                                    if(oactive && otarget){
                                        ood.Event.simulateEvent(otarget,"mouseout",{screenX:p.left, screenY:p.top, clientX:p.left, clientY:p.top});
                                        //console.log('deactive ' + oactive);
                                    }
                                }
                                break;
                            }else{
                                if(oactive==o.id){
                                    if(otarget){
                                        ood.Event.simulateEvent(otarget,"mouseout",{screenX:p.left, screenY:p.top, clientX:p.left, clientY:p.top});
                                    }
                                    d._c_dropactive=null;
                                    //console.log('deactive ' + oactive);
                                    break;
                                }
                            }
                        }
                    }
                }
                    
            //}catch(e){ood.DragDrop._end()._reset();}finally{
               return false;
            //}
        },
        $onDrop:function(e){
            var d=ood.DragDrop,p=d._profile,evt=ood.Event;
//                try{
                e = e || window.event;

                // opera 9 down with
                // if(!isWorking){evt.stopBubble(e);return false;}
                d._end();
                if(p.isWorking){

                    //here, release drop face first
                    //users maybe use html() function in onDrop function
                    d.setDropFace();

                    var r = d._source.onDragstop(true,evt.getEventPara(e));
                    if(d._dropElement)
                        ood.use(d._dropElement).onDrop(true,evt.getEventPara(e));
                }
//                }catch(a){}finally{
                d._reset();
                evt.stopBubble(e);
                ood.tryF(document.onmouseup,[e]);
                return !!r;
//                }
        },
        setDropElement:function(id){
            this._profile.dropElement=this._dropElement=id;
            return this;
        },
        getProfile:function(){
            var d=this,p=d._profile;
            p.offset=d._proxy
            ?
            { x : d._proxyLeft-p.ox+d._cssPos_x,  y : d._proxyTop-p.oy+d._cssPos_y}
            :
            { x : p.x-p.ox,  y : p.y-p.oy}
            ;
            return p;
        },
        setDropFace:function(target, dragIcon){
            var d=this,
                s1='<div style="position:absolute;z-index:'+ood.Dom.TOP_ZINDEX+';font-size:0;line-height:0;border-',
                s2=":dashed 1px #ff6600;",
                region=d._Region,rh=d._rh, st, sl, 
                bg='backgroundColor';
            if(region && region.parent())
                region.remove(false);
            if(d._R){
                d._R.css(bg, d._RB);
                delete d._R;
                delete d._RB;
            }

            if(target){
                //never create, or destroy the region
                if(!region || !region.get(0)){
                    region=d._Region=ood.create(s1+'top:solid 2px #ff6600;left:0;top:0;width:100%;height:0;"></div>'+s1+'right'+s2+'right:0;top:0;height:100%;width:0;"></div>'+s1+'bottom'+s2+'bottom:0;left:0;width:100%;height:0;"></div>'+s1+'left'+s2+'width:0;left:0;top:0;height:100%;"></div>');
                    rh=d._rh=ood([region.get(1),region.get(3)]);
                }
                target=ood(target);
                if(ood.browser.ie6)rh.height('100%');
                if(target.css('display')=='block'){
                    ood.setNodeData(region.get(0),'zIndexIgnore', 1);
                    target.append(region);
                    // ensure in the view
                    region.top(st=target.scrollTop()).left(sl=target.scrollLeft());
                    region.get(2).style.top='auto';region.get(1).style.left='auto';
                    region.get(2).style.bottom='-'+st+'px';
                    region.get(1).style.right='-'+sl+'px';

                    if(ood.browser.ie6 && !rh.get(0).offsetHeight)
                        rh.height(target.get(0).offsetHeight);
                }else{
                    d._RB = target.get(0).style[bg];
                    d._R=target;
                    target.css(bg, '#FA8072');
                }
                d.setDragIcon(dragIcon||'move');
            }else
                d.setDragIcon('none');
            return d;
        },
        setDragIcon:function(key){
            //avoid other droppable targetNode's setDropFace disturbing.
            ood.resetRun('setDropFace', null);
            var d=this,p=d._profile,i=p.proxyNode,ic=d._Icons;
            if(i && p.dragType=='icon')
                i.first(4).css(typeof key=='object'?key:{backgroundPosition: (ic[key]||key)});
            return d;
        },
        _setProxy:function(child, pos){
            var t,temp,d=this,p=d._profile,dom=ood.Dom;
            if(!dom.byId(d._id))
                ood('body').prepend(
                    //&nbsp; for IE6
                    ood.create('<div id="' + d._id + '" style="left:0;top:0;border:0;font-size:0;line-height:0;padding:'+d.$proxySize+'px;position:absolute;background:url('+ood.ini.img_bg+') repeat;"><div style="font-size:0;line-height:0;" id="' +d._idi+ '">'+(ood.browser.ie6?'&nbsp;':'')+'</div></div>')
                );
            t=ood(d._id);
            //t.rotate('10');
            if(p.dragKey){
                d.$proxySize=0;
                t.css('padding',0);
            }else{
                pos.left -=  d.$proxySize;
                pos.top -= d.$proxySize;
                if(!p.targetOffsetParent)
                    dom.setCover(true,null,false,p.dragCursor);
            }
            if(temp=p.targetOffsetParent)
                ood(temp).append(t);

            if(child){
                ood(d._idi).empty(false).append(child);
                p.proxyNode = child;
            }else
                p.proxyNode = ood(d._idi);
            t.css({display:'',zIndex:dom.TOP_ZINDEX*10,cursor:p.dragCursor}).offset(pos, temp);
            ood.setNodeData(t.get(0),'zIndexIgnore', 1);

            return t;
        },
        _resetProxy:function(){
            var d=this, p=d._profile,
                dom=ood.Dom,
                id1=d._id,
                id2=d._idi;
            if(dom.byId(id1)){
                var t,k,o=ood(id2),t=ood(id1);
                //&nbsp; for IE6
                if(ood.browser.ie6)
                    o.html('&nbsp;');
                else o.empty();
                o.attr('style','font-size:0;line-height:0;');
                //o.rotate(0);
                ood('body').prepend(
                    t
                    .css({
                        zIndex:0,
                        cursor:'',
                        display:'none',
                        padding:Math.round(parseFloat(d.$proxySize))+'px'
                    })
                );
                p.proxyNode=d._proxystyle=null;
                dom.setCover(false);
            }
        },
        _pack:function(mousePos,targetNode){
            var target, pos={}, size={}, d=this, p=d._profile, t;
            // get abs pos (border corner)
            if(p.targetLeft===null || null===p.targetTop)
                t=targetNode.offset(null, p.targetOffsetParent);
            pos.left = null!==p.targetLeft?p.targetLeft: t.left;
            pos.top = null!==p.targetTop?p.targetTop: t.top;

            switch(p.dragType){
                case 'deep_copy':
                case 'copy':
                   var t;
                    size.width =  ood.isNumb(p.targetWidth)? p.targetWidth:(targetNode.cssSize().width||0);
                    size.height = ood.isNumb(p.targetHeight)?p.targetHeight:(targetNode.cssSize().height||0);
                    var n=targetNode.clone(p.dragType=='deep_copy')
                        .css({position:'relative',margin:'0',left:'0',top:'0',right:'',bottom:'',cursor:p.dragCursor,'cssFloat':'none'})
                        .cssSize(size)
                        .id('',true)
                        .css('opacity',0.8);

                    if(p.targetCallback)
                        p.targetCallback(n);

                    n.query('*').id('',true);
                    if(p.targetCSS)
                        n.css(p.targetCSS);
                    target = d._setProxy(n,pos);
                    break;
                case 'shape':
                    // get size
                    size.width = null!==p.targetWidth?p.targetWidth:targetNode.offsetWidth();
                    size.height = null!==p.targetHeight?p.targetHeight:targetNode.offsetHeight();
                    size.width-=2;size.height-=2;
                    target = d._setProxy(
                        ood.create('div').css({border:'dashed 1px',fontSize:'0',lineHeight:'0'}).cssSize(size)
                        ,pos);
                    break;
                case 'blank':
                    target = d._setProxy(null,pos);
                    break;
                case 'icon':
                    pos.left=ood.isNumb(p.targetLeft)?p.targetLeft:(mousePos.left /*- ood.win.scrollLeft()*/ + 16);
                    pos.top=ood.isNumb(p.targetTop)?p.targetTop:(mousePos.top /*- ood.win.scrollTop()*/ + 16);
                    t='<table border="0" class="ood-node ood-node-table"><tr><td valign="top"><span class="ood-node ood-node-span" style="background:url('+p.dragIcon+') no-repeat left top;width:'+(ood.isNumb(p.targetWidth)?p.targetWidth:16)+'px;height:'+(ood.isNumb(p.targetHeight)?p.targetHeight:16)+'px;" ></span></td><td id="ood:dd:shadow" '+(p.shadowFrom?'style="border:solid 1px #e5e5e5;background:#fff;font-size:12px;line-height:14px;"':'')+'>'+(p.shadowFrom?
                    ood(p.shadowFrom).clone(true)
                    .css({left:'auto',top:'auto', position:'relative'})
                    .outerHTML().replace(/\s*id\=[^\s\>]*/g,''):'')

                    +'</td></tr></table>';
                    target = d._setProxy(ood.create(t).css('opacity',0.8), pos);
                    break;
                case 'move':
                    d.$proxySize=0;
                    target=targetNode;
                    if(target.css('position') != 'absolute')
                        target.css('position','absolute').offset(pos);
                    target.css('cursor',p.dragCursor);
            }

            return target;
        },
        _unpack:function(){
            var d=this, p=d._profile, t,f;
            if(p.targetReposition && ("move" != p.dragType)){
                if((t=ood(d._source)))
                    if(!t.isEmpty()){
                        if(t.css('position')!= 'absolute')
                            t.css('position','absolute').cssPos(t.offset(null,t.get(0).offsetParent ));

                        //for ie bug
                        if(ood.browser.ie)
                            t.cssRegion({right:'',bottom:''});
                        t.offset(p.curPos, p.targetOffsetParent||document.body);
                    }
            }
            if("move" == p.dragType)
                d._source.css('cursor',d._cursor);
        },
        _unRegister:function(node, key){
            var eh=this._eh;
            ood([node])
                .$removeEvent('beforeMouseover', eh)
                .$removeEvent('beforeMouseout', eh)
                .$removeEvent('beforeMousemove', eh);

            var o=ood.getNodeData(node.$xid, ['_dropKeys']),c=ood.$cache.droppable;            
            if(o)
                for(var i in o)
                    if(c[i])
                        ood.arr.removeValue(c[i],node.$xid);

            ood.setNodeData(node.$xid, ['_dropKeys']);
        },
        _register:function(node, key){
            var eh=this._eh;
            ood(node)
                .beforeMouseover(function(p,e,i){
                    var t=ood.DragDrop, p=t._profile;
                    if(p.dragKey && ood.getNodeData(i,['_dropKeys', p.dragKey])){
                        t.setDropElement(i);
                        t._onDragover=null;
                        ood.use(i).onDragenter(true);
                        if(t._dropElement)
                            ood.resetRun('setDropFace', t.setDropFace, 0, [i], t);
                    }
                }, eh)
                .beforeMouseout(function(p,e,i){
                    var t=ood.DragDrop,p=t._profile;
                     if(p.dragKey && ood.getNodeData(i,['_dropKeys', p.dragKey])){
                        ood.use(i).onDragleave(true);
                        if(t._dropElement==i){
                            t.setDropElement(t._onDragover=null);
                            ood.resetRun('setDropFace', t.setDropFace, 0, [null], t);
                        }
                    }
                }, eh)
                .beforeMousemove(function(a,e,i){
                    var t=ood.DragDrop, h=t._onDragover, p=t._profile;
                    //no dragover event
                    if(h==1)return;
                    if(t._dropElement==i && p.dragKey && ood.getNodeData(i,['_dropKeys', p.dragKey])){
                        if(h)h(e,i);
                        else{
                            //ensure to run once only
                            t._onDragover=1;
                            //if any dragover event exists, this function will set _onDragover
                            ood.use(i).onDragover(true,ood.Event.getEventPara(e));
                        }
                    }
                }, eh);

            var o=ood.getNodeData(node.$xid, ['_dropKeys']),c=ood.$cache.droppable;            
            if(o)
                for(var i in o)
                    if(c[i])
                        ood.arr.removeValue(c[i],node.$xid);

            var h={},a=key.split(/[^\w-]+/)
            for(var i=0,l=a.length;i<l;i++){
                h[a[i]]=1;
                c[a[i]]=c[a[i]]||[];
                c[a[i]].push(node.$xid);
            }
            ood.setNodeData(node.$xid, ['_dropKeys'], h);
            
        }
    },
    After:function(){
        this._reset();
        //add dom dd functions
        ood.each({
            startDrag:function(e, profile, dragKey, dragData){
                ood.DragDrop.startDrag(e, this.get(0), profile, dragKey||'', dragData||null);
                return this;
            },
            draggable:function(flag, profile, dragKey, dragData, target){
                var self=this,
                    target=target?typeof(target)=="function"?ood.tryF(getTarget,[],this):ood(target):null,
                    dd=ood.DragDrop;
                if(!target || !target.get(0)){
                    target=self;
                }
                self.removeClass('ood-ui-selectable').addClass('ood-ui-unselectable')
                if(flag===undefined)
                    flag=true;
                else if(typeof flag=='object'){
                    profile=flag;
                    flag=true;
                }
                var f=function(p,e,src){
                    if(ood.getId(ood.Event.getSrc(e))!=src)return true;
                    target.startDrag(e, profile, dragKey, dragData);
                };

                if(!!flag){
                    self.$addEvent('beforeMousedown',f, dd._eh, -1);
                }else{
                    self.$removeEvent('beforeMousedown', dd._eh);
                }

                return self;
            },
            droppable:function(flag, key){
                if(flag===undefined)flag=true;
                key = key || 'default';
                var d=ood.DragDrop;
                return this.each(function(o){
                    if(!!flag)
                        d._register(o, key);
                    else
                        d._unRegister(o, key);
                });
            }
        },function(o,i){
            ood.Dom.plugIn(i,o);
        });
    }
});/**
 * Template - 模板管理模块
 * 
 * 功能：
 * - 提供模板的创建、渲染和管理功能
 * - 支持模板的属性设置和事件绑定
 * - 提供模板的渲染和显示功能
 * - 支持模板的嵌套和组合
 * - 提供模板的缓存和回收机制
 * 
 * 主要方法：
 * - Constructor: 构造函数，创建模板对象
 * - setTemplate: 设置模板内容
 * - setProperties: 设置模板属性
 * - setEvents: 设置模板事件
 * - show: 显示模板
 * - getRootNode: 获取根节点
 * - render: 渲染模板
 */
ood.Class('ood.Template','ood.absProfile',{
    /**
     * 构造函数 - 创建模板对象
     * @param {string|Object} template - 模板内容
     * @param {Object} [properties] - 模板属性
     * @param {Object} [events] - 模板事件
     * @param {string} [domId] - 模板DOM ID
     */
    Constructor:function(template,properties,events,domId){
        var upper=arguments.callee.upper, args=ood.toArr(arguments);
        upper.apply(this,args);
        upper=null;
        
        var self=this;
        self.$domId = self.KEY + ':' + (self.serialId=self._pickSerialId()) + ':';
        self.domId = typeof domId == 'string'?domId:self.$domId;
        self._links={};
        self.template={'root':[['<div></div>'],[]]};
        self.properties={};
        self.events={};
        self.$template={};
        self.link(self.constructor._cache,'self').link(ood._pool,'ood');
        self.Class=self.constructor;
        self.box=self.constructor;
        self.boxing=function(){return this};

        if(template)self.setTemplate(typeof template=='string'?{'root':template}:template);
        if(events)self.setEvents(events);
        if(properties)self.setProperties(properties);
        return self;
    },
    Instance : {
        renderId:null,
        __gc:function(){
            var self=this,
                t=ood.$cache.reclaimId;
            if(!self.$noReclaim) 
                (t[self.KEY] || (t[self.KEY]=[])).push(self.serialId);
            else 
                delete self.$noReclaim

            delete ood.$cache.profileMap[self.domId];
            delete ood.$cache.profileMap[self.$domId];
            self.unLinkAll();
            ood.breakO([self.properties, self.event, self], 2);
        },
        _reg0:/^\w[\w_-]*$/,
        show:function(parent){
            if(!parent)parent=ood('body');
            parent=ood(parent);
            parent.append(this);
            return this;
        },
        getRootNode:function(){
            return ood.getNodeData(this.renderId, 'element');
        },
        /*
         *getRoot is the only function that depends on ood.Dom Class
        */
        getRoot:function(){
            return ood([this.renderId],false);
        },
        setDomId:function(id){
            var t=this, c=ood.$cache.profileMap, reg=t._reg0;
            //ensure the value
            if(typeof id== 'string' && reg.test(id) && !document.getElementById(id)){
                //delete the original one
                if(t.domId!=t.$domId)delete c[t.domId];
                //set profile's domId
                t.domId=id;
                //change the domNode id value
                if(t.renderId)
                    t.getRootNode().id=id;
                //if doesn't create yet, don't set it to ood.$cache:
                if(c[t.$domId])c[id]=t;
            }
            return t;
        },
        destroy:function(){
            if(this.renderId){
                var rn=this.getRootNode();
                ood.$purgeChildren(rn);
                if(rn.parentNode)
                    rn.parentNode.removeChild(rn);
                rn=null;
            }else this.__gc();          
        },
        setEvents:function(key,value){
            var self=this;
            if(typeof key == 'object')
                self.events=key;
            else
                self.events[key]=value;
            return self;
        },
        setTemplate:function(key,value){
            var self=this, t=self.template,$t=self.$template,h;
            if(typeof key == 'object'){
                self.template=key;
                h={};
                for(var i in key)
                    h[i||'root']=self._buildTemplate(key[i]);
                self.$template=h;
            }else if(typeof value == 'string')
                $t[key]=self._buildTemplate(t[key]=value);
            else
                $t['root']=self._buildTemplate(t['root']=key);
            return self;
        },
        setProperties:function(key,value){
            var self=this;
            if(typeof key == 'object')
                self.properties=key;
            else
                self.properties[key]=value;
            return self;
        },
        getItem:function(src){
            var obj=ood.getNodeData(src);
            if(!obj)return;

            var id=obj.tpl_evid, tpl_evkey=obj.tpl_evkey;
            if(!id || !tpl_evkey)return;

            var me=arguments.callee,
                f = me.f || (me.f = function(data, tpl_evkey, id){
                    var i,o,j,v;
                    for(j in data){
                        o=data[j];
                        if(ood.isArr(o) && (tpl_evkey==j||tpl_evkey.indexOf((data.tpl_evkey||j)+'.')===0))
                            for(i=0;v=o[i];i++){
                                if(v.tpl_evkey==tpl_evkey&&v.id==id)return v;
                                else if(v=f(v,tpl_evkey,id)) return v;
                            }
                    }
                });
            return f(this.properties, tpl_evkey, id);
        } ,
        _pickSerialId:function(){
            //get id from cache or id
            var arr = ood.$cache.reclaimId[this.KEY];
            if(arr && arr[0])return arr.shift();
            return this.constructor._ctrlId.next();
        },
        render:function(){
            var self=this;
            if(!self.renderId){
                var div=ood.$getGhostDiv();
                ood.$cache.profileMap[self.domId]=ood.$cache.profileMap[self.$domId]=this;
                div.innerHTML = self.toHtml();
                //add event handler
                var ch=self.events,
                    eh=ood.Event._eventHandler,
                    children=div.getElementsByTagName('*'),
                    domId=self.$domId,
                    f=ood.Event.$eventhandler,
                    i,l,j,k,o,key,id,t,v;
                if(l=children.length){
                    for(i=0;i<l;i++){
                        if((o=children[i]).nodeType!=1)continue;
                        key=o.getAttribute('tpl_evkey');
                        id=o.getAttribute('tpl_evid');
                        if(key!==null && id!==null){
                            v=ood.$registerNode(o);
                            v.tpl_evkey=key;
                            v.tpl_evid=id;
                            if(t = ch[key] ){
                                v=v.eHandlers||(v.eHandlers={});
                                for(j in t){
                                    //attach event handler to domPurgeData
                                    v[j]=f;
                                    //attach event handler to dom node
                                    if(k=eh[j]){
                                        v[k]=f;
                                        ood.Event._addEventListener(o, k, f);
                                    }
                                }
                            }
                            o.removeAttribute('tpl_evkey');
                            o.removeAttribute('tpl_evid');
                        }
                    }
                    if(!div.firstChild.$xid)
                        ood.$registerNode(div.firstChild);
                    //the first
                    self.renderId=div.firstChild.$xid;
                }
                o=div=null;
            }
            return self;
        },
        refresh:function(){
            var ns=this;
            if(ns.renderId){
                var proxy = document.createElement('span'), 
                    rn = ns.getRootNode(),
                    cache=ood.$cache.profileMap;
                
                //avoid of being destroyed                
                delete cache[ns.domId];
                delete cache[ns.$domId];
                
                if(rn.parentNode)
                    rn.parentNode.replaceChild(proxy,rn);
                ns.destroy();
                
                delete ns.renderId;

                ns.render();

                if(proxy.parentNode)
                    proxy.parentNode.replaceChild(ns.getRootNode(), proxy);

                proxy=rn=null;
            }
            return ns;
        },
        renderOnto:function(node){
            var self=this,id,domNode,style='style',t;
            if(typeof node=='string')node=document.getElementById(node);
            id=node.id||self.domId;
            
            //ensure renderId
            if(!self.renderId)
                self.render();
            
            domNode=self.getRootNode();
            node.parentNode.replaceChild(domNode,node);

            if(domNode.tabIndex!=node.tabIndex)
                domNode.tabIndex!=node.tabIndex;
            if(node.className)
                domNode.className += node.className;
            if(ood.browser.ie && (t=node.style.cssText))
                domNode.style.cssText += t+'';
            else if(t=node.getAttribute(style))
                domNode.setAttribute(style, (domNode.getAttribute(style)||'') + t);

            this.setDomId(id);
        },
        toHtml:function(properties){
            //must copy it for giving a default tpl_evkey
            var p=ood.copy(properties||this.properties||{});
            p.tpl_evkey="root";
            return this._doTemplate(p);
        },
        _reg1:/([^{}]*)\{([\w]+)\}([^{}]*)/g,
        _reg2:/\[event\]/g,
        _buildTemplate:function(str){
            if(typeof str=='string'){
                var obj=[[],[]],
                    a0=obj[0],
                    a1=obj[1];
                str=str.replace(this._reg2,' tpl_evid="{id}" tpl_evkey="{tpl_evkey}" ');
                str.replace(this._reg1,function(a,b,c,d){
                    if(b)a0[a0.length]=b;
                    a1[a0.length]=a0[a0.length]=c;
                    if(d)a0[a0.length]=d;
                    return '';
                });
                return obj;
            }else
                return str;
        },
        _getEV:function(funs, id, name, xid){
            var obj=ood.getNodeData(xid);
            if(!obj)return;

            var evs = this.events,
                tpl_evkey = obj.tpl_evkey,
                evg = (tpl_evkey&&evs&&evs[tpl_evkey])||evs,
                ev = evg&&evg[name];
            if(ev)funs.push(ev);
        },
        _reg3:/(^\s*<\w+)(\s|>)(.*)/,
        _doTemplate:function(properties, tag, result){
            if(!properties)return '';

            var self=this, me=arguments.callee,s,t,n,isA = ood.isArr(properties),
            template = self.$template,
            temp = template[tag||'root'],
            r = !result;

            result= result || [];
            if(isA){
                if(typeof temp != 'function')temp = me;
                for(var i=0;t=properties[i++];){
                    t.tpl_evkey=tag;
                    temp.call(self, t, tag, result);
                }
            }else{
                if(typeof temp == 'function')
                    temp.call(self, properties, tag, result);
                else{
                    tag = tag?tag+'.':'';
                    var a0=temp[0], a1=temp[1];
                    for(var i=0,l=a0.length;i<l;i++){
                        if(n=a1[i]){
                            if(n in properties){
                                t=typeof properties[n]=='function'?properties[n].call(self, n, properties):properties[n];
                                //if sub template exists
                                if(template[s=tag+n])
                                    me.call(self, t, s, result);
                                else
                                    result[result.length]=t;
                            }
                        }else
                            result[result.length]=a0[i];
                    }
                }
            }
            if(r){
                return result.join('')
                    .replace(self._reg3, '$1 id="'+self.$domId+'" $2$3');
            }
        },
        serialize:function(){
            var self=this,
                s=ood.serialize,
                t=ood.absObj.$specialChars,
                properties = ood.isEmpty(self.properties)?null:ood.clone(self.properties,function(o,i){return !t[(i+'').charAt(0)]});            
            return 'new ood.Template(' + 
            s(self.template||null) + "," + 
            s(properties) + "," + 
            s(ood.isEmpty(self.events)?null:self.events) + "," + 
            s(self.$domId!=self.domId?self.domId:null) + 
            ')';
        }
    },
    Static : {
        getFromDom:function(id){
            if((id=typeof id=='string'?id:(id && id.id)) &&(id=ood.$cache.profileMap[id]) && id['ood.Template'])
                return id.boxing();
        },
        _cache:[],
        _ctrlId : new ood.id()
    }
});/**
 * Cookies - Cookie管理模块
 */
ood.Class("ood.Cookies", null,{
    Static:{
        set:function(name,value,days,path,domain,isSecure){
            if(ood.isHash(name)){
                for(var i in name) this.set(i, name[i],days,path,domain,isSecure);
           }else{
	           if(typeof value !="string")value=ood.serialize(value);
    	       document.cookie = escape(name+'') + "=" + escape(value) +
    		        (days?"; expires="+(new Date((new Date()).getTime()+(24*60*60*1000*days))).toGMTString():"")+
    		        (path?"; path="+path:"")+
    		        (domain?"; domain="+domain:"")+ 
    		        (isSecure?"; secure":"");
    	    }
            return this;
        },
        get:function(name){
        	var i,a,s,ca = document.cookie.split( "; " ),hash={},unserialize=function(v){
                return  /^\s*\{[\s\S]*\}$/.test(v) ? ood.unserialize(v) : /^\s*\[[\s\S]*\]$/.test(v) ? ood.unserialize(v) : v;
            };
        	for(i=0;i<ca.length;i++){
        		a=ca[i].split("=");
    	        s=a[1]?unescape(a[1]):'';
    	        hash[a[0]] = unserialize(s)||s;
        		if(name && a[0]==escape(name))
        		    return hash[a[0]];
        	}
        	return name?null:hash;
        },
        remove:function(name){
        	return this.set(name,"",-1).set(name,"/",-1);
        },
        clear:function(){
            ood.arr.each(document.cookie.split(";"),function(o){
                ood.Cookies.remove(ood.str.trim(o.split("=")[0]));
            });
        }
    }
});/**
 * History - 浏览器历史记录管理模块
 * 
 * 功能：
 * - 提供浏览器历史记录的管理功能
 * - 支持HTML5 History API
 * - 提供历史记录的添加、替换和导航功能
 * - 支持历史记录变化事件监听
 * - 兼容不同浏览器的历史记录处理
 * 
 * 主要方法：
 * - activate: 激活历史记录管理
 * - add: 添加历史记录
 * - replace: 替换当前历史记录
 * - back: 返回上一页
 * - forward: 前进到下一页
 * - go: 跳转到指定历史记录
 */
ood.Class("ood.History",null,{
    Static:{
        activate:function(){
            var self=this;
            if(self._activited)return;
            self._activited=1;
            switch(self._type){
                case 'event':
                    ood.Event._addEventListener(window, "hashchange",self._checker);
                break;
                case "iframe":
                    document.body.appendChild(document.createElement('<iframe id="'+self._fid+'" src="about:blank" style="display: none;"></iframe>'));
                    var doc=document.getElementById(self._fid).contentWindow.document;
                    doc.open("javascript:'<html></html>'");
                    doc.write("<html><head><scri" + "pt type=\"text/javascript\">parent.ood.History._checker('"+hash+"');</scri" + "pt></head><body></body></html>");
                    doc.close();
                case 'timer':
                    if(self._itimer)
                        clearInterval(self._itimer);
                    self._itimer = setInterval(self._checker, 200);
                break;
            }
        },
        _fid:'ood:history',
        _type:(ood.browser.ie && (ood.browser.ver<8))?'iframe':("onhashchange" in window)?'event':'timer',
        _callbackTag:null,
        _callbackArr:null,
        _inner_callback:null,
        _callback:function(fragment, init, newAdd){
            var ns=this, arr, f;
            ood.arr.each(ood.Module._cache,function(m){
              // by created order    
               if(m._evsClsBuildIn && ('onFragmentChanged' in m._evsClsBuildIn)){
                   // function or pseudocode
                   if(ood.isFun(f = m._evsClsBuildIn.onFragmentChanged) || (ood.isArr(f) && f[0].type)){
                       m.fireEvent('onFragmentChanged', [m,fragment, init, newAdd]);
                   }
               }
               else if(m._evsPClsBuildIn && ('onFragmentChanged' in m._evsPClsBuildIn)){
                   // function or pseudocode
                   if(ood.isFun(f = m._evsPClsBuildIn.onFragmentChanged) || (ood.isArr(f) && f[0].type)){
                       m.fireEvent('onFragmentChanged', [m,fragment, init, newAdd]);
                   }
               }
            });
            // tag
            if(ood.isFun(ns._callbackTag) && false===ns._callbackTag(fragment, init, newAdd))return;
            // tagVar
            arr = ns._callbackArr;
            if(arr&&ood.isArr(arr)){
                for(var i=0,l=arr.length;i<l;i++){
                    if(ood.isFun(arr[i]) && false===arr[i](fragment, init, newAdd))
                        return;
                }
            }
            // the last one
            if(ood.isFun(ns._inner_callback))ns._inner_callback(fragment, init, newAdd);
        },
        /* set callback function
        callback: function(hashStr<"string after #!">)
        */
        setCallback: function(callback){
            var self=this,
                hash = location.hash;
            if(hash)hash='#!' + encodeURIComponent((''+decodeURIComponent(hash)).replace(/^#!/,''));
            else hash="#!";
            self._inner_callback = callback;

            self._lastFI = decodeURIComponent(hash);

            self._callback(decodeURIComponent(self._lastFI.replace(/^#!/, '')), true, callback);

            return self;
        },
        _checker: function(hash){
            var self=ood.History;
            switch(self._type){
                case "iframe":
                    if(ood.isSet(hash))
                        location.hash=hash;
                case 'event':
                case 'timer':
                    if(decodeURIComponent(location.hash) != decodeURIComponent(self._lastFI)) {
                        self._lastFI = decodeURIComponent(location.hash);
                        self._callback(decodeURIComponent(location.hash.replace(/^#(!)?/, '')));
                    }
                break;
            }
        },
        getFI:function(){
            return this._lastFI;
        },
        /*change Fragement Identifier(string after '#!')
        */
        setFI:function(fi,triggerCallback,merge){
            var self=this;
            
            self.activate();

            // ensure encode once
            if(fi){
                if(!ood.isHash(fi))fi=ood.urlDecode((fi+'').replace(/^#!/,'')); //encodeURIComponent((''+decodeURIComponent(fi)).replace(/^#!/,''));
                if(merge)fi = ood.merge(fi, ood.getUrlParams(), 'without');
                fi='#!' + ood.urlEncode(fi);
            }else{
                fi="#!";
            }
            if(self._lastFI == decodeURIComponent(fi))return false;

            switch(self._type){
                case "iframe":
                    var doc=document.getElementById(self._fid).contentWindow.document;
                    doc.open("javascript:'<html></html>'");
                    doc.write("<html><head><scri" + "pt type=\"text/javascript\">parent.ood.History._checker('"+fi+"');</scri" + "pt></head><body></body></html>");
                    doc.close();
                break;
                case 'event':
                case 'timer':
                    location.hash = self._lastFI = decodeURIComponent(fi);
                if(triggerCallback!==false)
                    self._callback(decodeURIComponent(fi.replace(/^#!/,'')));
                break;
            }
        }
    }
});//singleton
ood.Class("ood.Tips", null,{
    Constructor:function(){return null},
    Initialize:function(){
        if(ood.ini.disableTips || ood.browser.fakeTouch)return;
        var dd=ood.DragDrop,
            tips=this;
        if(dd)
            dd.$reset=function(){
                tips._pos={left:dd._profile.x,top:dd._profile.y}
            };

        //for: span(display:-moz-inline-box) cant wrap in firefox
        ood.CSS.addStyleSheet(
            ":root {"+
            "   --tips-bg: var(--bg);"+
            "   --tips-text: var(--text);"+
            "   --tips-border: var(--border);"+
            "   --tips-shadow: 0 2px 8px rgba(0,0,0,0.15);"+
            "}"+
            ".ood-tips{position:absolute;overflow:visible;visibility:hidden;left:-10000px;border-radius:var(--radius-sm);}"+
            ".ood-tips-i{overflow:hidden;position:relative;}"+
            ".ood-tips-i span{display:inline;}"+
            ".ood-tips-c{"+
            "   padding:var(--spacing-xs) var(--spacing-sm);"+
            "   background:var(--tips-bg);"+
            "   color:var(--tips-text);"+
            "   border:1px solid var(--tips-border);"+
            "   box-shadow:var(--tips-shadow);"+
            "}"+
            ".ood-tips-c *{line-height:1.4em;}"+
            ".ood-tips .ood-tips-c{border-radius:var(--radius-sm);}"+
            "/* 暗黑模式 */"+
            ".ood-dark .ood-tips-c{"+
            "   --tips-bg: var(--dark-bg);"+
            "   --tips-text: var(--dark-text);"+
            "   --tips-border: var(--dark-border);"+
            "   --tips-shadow: 0 2px 8px rgba(0,0,0,0.3);"+
            "}"
        , this.KEY);

        ood.doc.afterMousedown(function(){
            tips._cancel();
        },'$Tips',-1).afterMousemove(function(obj, e){
            if(dd.isWorking)return;
            var event=ood.Event,p,n;

            if((p=ood.resetRun.$cache) && p['$Tips']){
                tips._pos=event.getPos(e);
            }

            //it's first show
            if(tips._from){
                tips._pos=event.getPos(e);
                tips._showF();
                ood.resetRun('$Tips3');
            //after show, before hide
            }else if(tips._showed && tips.MOVABLE){
                p=event.getPos(e);
                n=tips._Node.style;
                n.left = Math.min(tips._tpl._ww-tips._tpl._w, Math.max(0, Math.round((parseFloat(n.left)||0) + (p.left-tips._pos.left), 10))) + 'px';
                n.top = Math.min(tips._tpl._hh-tips._tpl._h, Math.max(0, Math.round((parseFloat(n.top)||0) + (p.top-tips._pos.top), 10))) + 'px';
                
                tips._pos=p;
            }
        },'$Tips',-1)
        .afterMouseover(function(obj, e){
            var event=ood.Event,
                rt=event.$FALSE,
                node=event.getSrc(e),
                id,
                _from,
                tempid,evid,
                index=0,
                pass,
                rtn=function(rt){
                    if(tips._markId)tips._cancel()
                    return rt;
                };
            if(!node)
                return rtn(rt);
            try{
                //for inner renderer
                while((!node.id || node.id==ood.$localeDomId) && node.parentNode!==document && index++<10)
                    node=node.parentNode;
                if(!(id=(typeof node.id=="string"?node.id:null))){
                    node=null;
                    return rtn(rt);
                }
            }catch(e){}

            //check id
            if((_from=event._getProfile(id)) && _from.box && _from.KEY=='ood.UIProfile'){
                if(_from.properties.disableTips || _from.behavior.disableTips){
                    node=null;
                    return rtn(false);
                }

                var nt=_from.behavior.NoTips;
                if(nt){
                    for(var i=0,l=nt.length;i<l;i++){
                        if(id.indexOf(_from.keys[nt[i]])===0)
                            return rtn(false);
                    }
                }
                nt=_from.behavior.PanelKeys;
                if(nt){
                    for(var i=0,l=nt.length;i<l;i++){
                        if(id.indexOf(_from.keys[nt[i]])===0)
                            return rtn(false);
                    }
                }
                nt=_from.behavior.HoverEffected;
                //if onShowTips exists, use seprated id, or use item scope id
                tempid=_from.onShowTips?id:id.replace(tips._reg,
                //if HoverEffected exists, use seprated id
                function(a,b){
                    return nt&&(b in nt)?a:':';
                });
                if(tips._markId && tempid==tips._markId)
                    return rt;

                //set mark id
                tips._markId = tempid;
                tips._pos=event.getPos(e);

                if(tips._showed){
                    tips._from=_from;
                    tips._enode=id;
                    tips._showF();
                }else
                    ood.resetRun('$Tips', function(){
                        tips._from=_from;
                        tips._enode=id;
                        // if mouse stop move
                        ood.resetRun('$Tips3', function(){
                            if(tips._from)
                                tips._showF();
                        });
                    }, tips.DELAYTIME);
            }else
                tips._cancel();

            node=null;
            return rt;
        },'$Tips',-1)
        .afterMouseout(function(obj, e){
            if(tips._markId){
                var event=ood.Event,
                    id,
                    clear,
                    index=0,
                    node = e.toElement||e.relatedTarget;

                if(!node)
                    clear=1;
                else{
                    //for firefox wearing anynomous div in input/textarea
                    try{
                        //for inner renderer
                        while((!node.id || node.id==ood.$localeDomId) && node.parentNode!==document && index++<10)
                            node=node.parentNode;
                        if(!(id=(typeof node.id=="string"?node.id:null))){
                            node=null;
                            clear=1;
                        }
                    }catch(e){clear=1}
                }
                if(clear)
                    tips._cancel();
                return event.$FALSE;
            }
        },'$Tips',-1)
        .afterMouseup(function(obj, e){
            tips._cancel();
        },'$Tips',-1);

        this._Types = {
            'default' : new function(){
                this.show=function(item, pos, key){
                    //if trigger onmouseover before onmousemove, pos will be undefined
                    if(!pos)return;

                    var self=this,node,_ruler,s,w,h;
                    if(!(node=self.node) || !node.get(0)){
                        node = self.node = ood.create('<div class="ood-node ood-node-div ood-tips  ood-ui-shadow ood-custom"><div class="ood-node ood-wrapper ood-node-div ood-tips-i ood-custom"></div></div>');
                        _ruler = self._ruler = ood.create('<div class="ood-node ood-wrapper ood-node-div ood-tips  ood-ui-shadow ood-custom"><div class="ood-node ood-node-div ood-tips-i ood-custom"></div></div>');
                        self.n = node.first();
                        self._n = _ruler.first();
                        ood('body').append(_ruler);
                    }
                    _ruler = self._ruler;
                    //ensure zindex is the top
                    if(document.body.lastChild!=node.get(0))
                        ood('body').append(node,false,true);

                    s = typeof item=='object'? item[key||ood.Tips.TIPSKEY] :item ;
                    if(typeof s=='function')
                        s=s();
                    if(s+=""){
                        var html=/^\s*\</.test(s);
                        //get string
                        s=ood.adjustRes(s);
                        ood.Tips._curTips=s;
                        if(!item.transTips || !html)
                            s='<div class="ood-ui-ctrl ood-node ood-node-div  ood-uiborder-flat ood-uicell-alt ood-node-tips ood-tips-c /*ood-cls-wordwrap */ood-custom">'+s+'</div>';
                        //set to this one
                        self._n.get(0).innerHTML=s;

                        self._ww=ood.frame.width();
                        self._hh=ood.frame.height();

                        //get width
                        w=Math.min(html?self._ww:tips.MAXWIDTH, _ruler.get(0).offsetWidth + 2);

                        //set content, AND dimension
                        var style=node.get(0).style, t1=self.n.get(0),styleI=t1.style;
                        //hide first
                        style.visibility='hidden';
                        //set content
                        t1.innerHTML=s;
                        //set dimension
                        if(ood.browser.ie){
                            style.width=styleI.width=(self._w=Math.round(w+(w%2)))+'px';
                            h=t1.offsetHeight;
                            style.height=(self._h=Math.round(h-(h%2)))+'px';
                        }else{
                            styleI.width=(self._w=Math.round(w))+'px';
                            self._h=self.n.height();
                        }

                        if(pos===true){
                            style.visibility='visible';
                        }else{
                            //pop(visible too)
                            node.popToTop((pos['ood.UI'] || pos['ood.UIProfile'] || pos['ood.Dom'] || pos.nodeType==1 || typeof pos=='string')?pos:{left:pos.left,top:pos.top,region:{
                                left:pos.left,
                                top:pos.top-12,
                                width:24,
                                height:32
                            }},1);
                        }
                        
                        style=styleI=t1=null;
                    }else
                        node.css('zIndex',0).hide();
                };
                this.hide = function(){
                    this.node.css('zIndex',0).hide();
                };
            }/*,
            'animate' : new function(){
                this.threadid='$tips:1$';
                this.show=function(item, pos){
                    if(!this.node){
                        this.node = ood.create('<div class="ood-node ood-node-div ood-custom" style="position:absolute;border:solid gray 1px;background-color:#FFF1A0;padding:.5em;overflow:hidden;"></div>');
                        ood('body').append(this.node);
                    }
                    pos.left+=12;
                    pos.top+=12;
                    var s=item.tips;
                    s = s.charAt(0)=='$'?ood.wrapRes(s.slice(1)):s;
                    this.node.html(s).css('zIndex',ood.Dom.TOP_ZINDEX).cssPos(pos);
                    var w=this.node.width(),h=this.node.height();
                    this.node.cssSize({ width :0, height :0}).css('display','block').animate({width:[0,w],height:[0,h]},0,0,300,0,'expoOut',this.threadid).start();
                };
                this.hide = function(){
                    ood.Thread.abort(this.threadid);
                    this.node.height('auto').width('auto').css('display','none').css('zIndex',0);
                };
            }*/
        };
    },
    Static:{
        _reg:/-([\w]+):/,
        TIPSKEY:'tips',
        MAXWIDTH:600,
        MOVABLE:true,
        DELAYTIME:400,
        AUTOHIDETIME:5000,

        _showF:function(){
            if(ood.ini.disableTips || ood.browser.fakeTouch)return;
            var self=this,
                _from=self._from,
                node=ood.Dom.byId(self._enode),
                pos=self._pos,
                id,
                o,t,b=true;

            self._from=self._enode=null;

            if(!node || !_from || !pos || !(o=_from.box))return;

            //1.CF.showTips
            b=false!==((t=_from.CF) && (t=t.showTips) && t(_from, node, pos));
            //2._showTips / onShowTips
            //check if showTips works
            if(b!==false)b=false!==(_from._showTips && _from._showTips(_from, node, pos));
            //check if showTips works
            if(b!==false)b=false!==(o._showTips && o._showTips(_from, node, pos));

            //default tips var(profile.tips > profile.properties.tips)
            if(b!==false){
                if(((t=_from) && t.tips)||(t && (t=t.properties) && t.tips)){
                    self.show(pos, t);
                    b=false;
                }
                else if((t=_from) && (t=t.properties) && t.autoTips && ('caption' in t)
                    // if tips is default value, try to show caption
                    // you can settips to null or undefined to stop it
                    && t.tips===''
                    ){
                    if(t.caption||t.labelCaption){
                        self.show(pos, {tips:t.caption||t.labelCaption});
                        b=false;
                    }
                }
            }

            //no work hide it
            if(b!==false){
                self.hide();
            }else {
                if(!self.MOVABLE)
                    ood.resetRun('$Tips2', self.hide,self.AUTOHIDETIME,null,self);
            }
            node=pos=_from=null;
        },
        getTips:function(){
            return this._curTips;
        },
        setTips:function(s){
            if(this._curTips && this._tpl&& this._Node){
                this._tpl.show(s, true);
            }
        },
        setPos:function(left,top){
            var n=this;
            if((n=n._Node)&&(n=n.style)){
                if(left||left===0)n.left=Math.round(parseFloat(left))+'px';
                if(top||top===0)n.top=Math.round(parseFloat(top))+'px';
            }
        },
        show:function(pos, item, key){
            var self=this,t;
            //for mousemove
            self._pos=pos;
            //same item, return
            if(self._item === item)return;

            //hide first
            //if(self._tpl)self._tpl.hide();

            //base check
            if(typeof item =='string' || (item && (item[key||ood.Tips.TIPSKEY]))){
                //get template
                t = self._tpl = self._Types[item.tipsTemplate] || self._Types['default'];
                t.show(item,pos,key);
                self._Node=t.node.get(0);
                self._item=item;
                self._showed = true;
            }else
                self._cancel();
        },
        hide:function(){
            var self=this;
            if(self._showed){
                if(self._tpl)self._tpl.hide();
                self._clear();
            }
        },
        _cancel:function(){
            var self=this;
            if(self._markId){
                if(self._showed){
                    self.hide();
                }else{
                    ood.resetRun('$Tips');
                    ood.resetRun('$Tips3');
                    self._clear();
                }
            }
        },
        _clear:function(){
            var self=this;
            self._Node=self._curTips=self._markId = self._from=self._tpl = self._item = self._showed = null;
        }
    }
});/**
 * Module - OOD框架的核心模块基类
 * 
 * 模块生命周期：
 * 1. initialize
 * 2. beforeCreated
 * 3. onCreated
 * 4. beforeShow
 * 5. afterShow
 * 6. onLoadBaseClass
 * 7. onLoadRequiredClass
 * 8. onLoadRequiredClassErr
 * 9. onIniResource
 *     - iniResource (异步)
 * 10. beforeIniComponents
 *     - iniComponents (异步)
 * 11. afterIniComponents
 *     - iniExModules (异步)
 * 12. onReady
 * 13. onRender
 * 14. onDestroy
 * 15. onModulePropChange
 * 
 * 值处理方法：
 * - getValue: 获取模块值
 * - getUIValue: 获取UI值
 * - resetValue: 重置值
 * - setUIValue: 设置UI值
 * - updateValue: 更新值
 * - isDirtied: 检查是否被修改
 * - checkValid: 检查值是否有效
 */
ood.Class('ood.Module', 'ood.absProfile', {
    Initialize: function () {
        var ns = this;
        ood.launch = function (cls, onEnd, lang, theme, showUI, parent, subId, onCreated, onDomReady) {
            ns.load.apply(ns, [cls, function (err, module) {
                if (module) module.setHost(window, ood.ini.rootModuleName);
                ood.tryF(onEnd, [err, module], module);
            }, lang, theme, showUI, parent, subId, onCreated, onDomReady]);
        };
        // compitable
        ns['ood.Com'] = ns.prototype['ood.Com'] = 1;
        ood.Com = ns;
        ns.$activeClass$ = 'ood.Module';
    },
    After: function () {
        var self = this, k, e, t, b, i;
        ood.arr.each(['$DataModel', '$EventHandlers'], function (v) {
            k = self[v] || {};
            if ((t = self.$parent) && (e = t.length)) {
                while (e--) {
                    b = t[e][v];
                    for (i in b) {
                        if (!(i in k)) k[i] = b[i];
                    }
                }
            }
            self[v] = k;
        });
        e = self.prototype;
        if ('_evsClsBuildIn' in e) {
            b = e._evsClsBuildIn;
            // for parents defination
            t = e._evsPClsBuildIn || (e._evsPClsBuildIn = {});
            for (i in b) {
                if (t[i]) {
                    if (!ood.isArr(t[i])) t[i] = [t[i]];
                    ood.arr.insertAny(t[i], b[i]);
                } else t[i] = ood.clone(b[i]);
            }
            e._evsClsBuildIn = null;
        }
        if ('events' in e) {
            // for class defination
            e._evsClsBuildIn = e.events;
            // events for instance
            e.events = {};
        }
        self._nameId = 0;
        self._nameTag = self.$nameTag || (self.KEY.replace(/\./g, '_').toLowerCase());
        self._cache = [];
    },
    Constructor: function (properties, events, host) {
        var self = this, opt, alias, t;

        // If it's a older module object, set xid first
        if (properties && properties.constructor == self.constructor) {
            if (properties.$xid) {
                self.$xid = properties.$xid;
            }
        }

        var upper = arguments.callee.upper;
        if (upper) upper.call(self);
        upper = null;

        // If it's a older module object, refresh itself
        if (properties && properties.constructor == self.constructor) {
            var oldm = properties;

            events = oldm.events || {};
            alias = oldm.alias;
            host = oldm.host;
            properties = oldm.properties || {};
            // for refresh , use the old pointer
            self = oldm;
        } else {
            if (properties && properties.key && properties["ood.Module"]) {
                var opt = properties;
                properties = (opt && opt.properties) || {};
                events = (opt && opt.events) || {};
                alias = opt.alias;
                host = opt.host;
            } else {
                if (!properties) {
                    if (self.properties) {
                        properties = ood.clone(self.properties, true);
                        // for inner coms prf
                        if (t = self.properties.__inner_coms_prf__) properties.__inner_coms_prf__ = t;
                    } else properties = {};
                }
                events = events || (self.events ? ood.clone(self.events) : {});
            }
        }


        self.Class = self.constructor;
        self.box = self.constructor;
        self.key = self.KEY;

        if (!alias) alias = self.constructor.pickAlias();
        if (!ood.isEmpty(self.constructor.$DataModel)) {
            ood.merge(properties, ood.clone(self.constructor.$DataModel), 'without');
        }
        //
        self._links = {};
        self.link(self.constructor._cache, "self");
        self.link(ood.Module._cache, "ood.module");
        self.link(ood._pool, 'ood');

        self.host = host || self;
        self.alias = alias;

        self.$UIvalue = "";
        self.dioModule = [],
            self._nodes = [];
        self._ctrlpool = {};
        self.events = events;
        self.properties = {};
        self.hooks = {};
        if (self._evsClsBuildIn) self._evsClsBuildIn = ood.clone(self._evsClsBuildIn);
        if (self._evsPClsBuildIn) self._evsPClsBuildIn = ood.clone(self._evsPClsBuildIn);

        self.setProperties(properties, null, true);
        self._innerCall('initialize');
        return self;
    },
    Instance: {
        autoDestroy: true,
        background: "",
        initData: {},
        viewInstId: '',
        domainId: '',
        fieldId: '',
        childname: '',
        target: '',
        fieldCaption: '',
        parentModule: {},
        className: "",
        euClassName: "",
        // [[[ fake boxing
        get: function (index) {
            return ood.isNumb(index) ? this : [this];
        },
        size: function () {
            return 1;
        },
        boxing: function () {
            return this;
        },
        each: function (fun, scope) {
            fun.call(scope, this);
            return this;
        },
        getRoot: function (rtnPrf) {
            if (!this._innerModulesCreated) this._createInnerModules();
            var fun = function (m) {
                if (m["ood.Module"]) {
                    for (var i = 0, l = m._nodes, o; i < l, o = m._nodes[i]; i++) {
                        if (o["ood.Module"]) return fun(o);
                        if (o["ood.UIProfile"] && !o.box.$initRootHidden) return rtnPrf ? o : o.getRoot();
                    }
                }
            };
            return fun(this);
        },
        getRootNode: function () {
            var ui = this.getRoot();
            if (!ui.isEmpty()) return ui.get(0);
        },

        setParentModule: function (parentModule) {
            this.parentModule = parentModule;
        },
        getParentModule: function () {
            return this.parentModule;
        },


        getDioModule: function () {
            return this.dioModule;
        },
        addDioModule: function (dioModule) {
            this.dioModule.push(dioModule);
        },
        addChildModule: function (childModule) {
            var obj = this;
            if (childModule.alias && obj._ctrlpool) {
                obj._ctrlpool[childModule.alias] = childModule;
            }
        },


        getChildModules: function () {
            var obj = this, modules = [];
            ood.each(obj._ctrlpool, function (prf) {
                if (prf.box['ood.Module'] == true) {
                    modules.push(prf);
                }
            });
            return modules;
        },

        autoSave: function () {
            var obj = this;
            if (this.isDirtied()) {
                ood.each(obj._ctrlpool, function (db) {
                    if (db) {
                        if (db.key == 'ood.APICaller' && db.alias == 'Save') {
                            db.boxing().invoke();
                        }
                    }
                });

            }
            ;
        },

        getChildModule: function (alias) {
            var obj = this, module;
            ood.each(obj._ctrlpool, function (childModule) {
                if (childModule.host && childModule.host.key && childModule.host.key == alias) {
                    module = childModule;
                    return module;
                }
            });
            return module;

        },

        getModule: function (top) {
            var getUpperModule = function (module) {
                // if it's a inner module
                if (module.moduleClass && module.moduleXid) {
                    var pm = ood.SC.get(module.moduleClass);
                    if (pm && (pm = pm.getInstance(module.moduleXid))) {
                        return getUpperModule(pm);
                    }
                }
                return module;
            };
            return top ? getUpperModule(this) : this;
        },
        // ]]]
        /*
         // [[[ fake UIProfile
        linkParent:function(parentProfile, linkId, index){
            var profile=this;
            //unlink first
            profile.unlinkParent();
            if(!profile.destroyed){
            //link
                profile.parent = parentProfile;
                profile.childrenId = linkId;
                profile.link(parentProfile.children, '$parent', [profile, linkId], index);
            }
            return profile;
        },
        unlinkParent:function(){
            var profile=this;
            delete profile.parent;
            delete profile.childrenId;
            profile.unLink('$parent');
            return profile;
        },
        // ]]]
        */
        _toDomElems: function () {
            var ns = this, innerUI = ns.getUIComponents();
            if (!ns.created)
            // create synchronously
                ns.create(null, false)
            ns.render();

            // force to create and render the first layer inner modules
            innerUI.each(function (o, i) {
                if ((o = o.getModule()) && o != ns) {
                    o._toDomElems();
                }
            });

            return innerUI._toDomElems();
        },
        setAlias: function (alias) {
            return ood.absObj.prototype._setHostAlias.call(this, null, alias);
        },
        getAlias: function () {
            return this.alias;
        },
        setHost: function (host, alias) {
            return ood.absObj.prototype._setHostAlias.call(this, host, alias);
        },
        getName: function () {
            return this.properties.name || this.alias;
        },
        setName: function (name) {
            this.properties.name = name;
        },
        getDesc: function () {
            return this.properties.desc;
        },
        setDesc: function (desc) {
            this.properties.desc = desc;
        },
        getTabindex: function () {
            return this.properties.tabindex;
        },
        setTabindex: function (tabindex) {
            this.properties.tabindex = tabindex;
        },
        getHost: function () {
            return this.host;
        },
        setFunctions: function (key, value) {
            var self = this;

            if (!key)
                delete self.functions;
            else if (typeof key == 'string')
                self.functions[key] = value;
            else if (ood.isHash(key)) {
                if (value/*force*/) {
                    self.functions = ood.clone(key);
                } else {
                    ood.merge(self.functions, key, 'all');
                }
            }
        },
        getFunctions: function (key) {
            var fs = this.functions;
            if (fs && ood.isHash(fs)) {
                return key ? fs[key] : fs;
            }
        },
        setProperties: function (key, value, ignoreEvent, innerDataOnly) {
            var self = this,
                h = self.properties,
                oDataBinder = ('dataBinder' in h) ? h.dataBinder : null,
                t;

            if (!key)
                h = {};
            else if (typeof key == 'string')
                h[key] = value;
            else if (ood.isHash(key)) {
                if (value/*force*/) {
                    h = ood.copy(key);
                } else {
                    h = ood.clone(h, true);
                    // for inner coms prf
                    if (t = self.properties.__inner_coms_prf__) h.__inner_coms_prf__ = t;
                    ood.merge(h, key, 'all');
                    if (value && ood.isHash(value))
                        ood.merge(h, value, 'all');
                }
            }
            self.properties = h;

            if (!ignoreEvent) {
                if (!innerDataOnly) {
                    if ('dataBinder' in h) {
                        if (oDataBinder !== (t = h.dataBinder || null)) {
                            if (oDataBinder) ood.DataBinder._unBind(oDataBinder, self);
                            if (t) ood.DataBinder._bind(t, self);
                        }
                    }
                }

                if (self._innerModulesCreated) {
                    // to apply inner control profile setting
                    if (t = self.properties.__inner_coms_prf__) self.setProfile(t);
                    // to apply inner control prop map
                    if (ood.isFun(self._propSetAction)) self._propSetAction(self.properties);
                }

                if (ood.isFun(self.propSetAction)) self.propSetAction(self.properties);

                // the last one
                if (!innerDataOnly) {
                    self._fireEvent('onModulePropChange');
                }
            }
            return self;
        },
        /*
        _propGetter:function(prop){
            var mdl=this,reg=/^\s*([^>\s]+)\s*>\s*([^>\s]+)\s*$/,r,t,f;
            ood.each(prop,function(o,i){
                if( (r=reg.exec(i)) && (t=mdl[r[1]]) )
                    prop[i] = ood.isFun(t[f='get'+ood.str.initial(r[2])]) ? t[f]() : ood.get(mdl,[r[1],'properties',r[2]]);
            });
            return prop;
        },*/
        _propSetAction: function (prop) {
            var mdl = this, reg = /^\s*([^>\s]+)\s*>\s*([^>\s]+)\s*$/, r, t, f;
            ood.each(prop, function (o, i) {
                // ignore [null/undifined]
                if (ood.isSet(o) && (r = reg.exec(i)) && (t = mdl[r[1]]))
                    ood.isFun(t[f = 'set' + ood.str.initial(r[2])]) ? t[f](o) : ood.set(mdl, [r[1], 'properties', r[2]], o);
            });
        },
        getProperties: function (key) {
            var self = this, prop = self.properties;
            if (ood.isFun(self._propGetter)) prop = self._propGetter(prop);
            if (ood.isFun(self.propGetter)) prop = self.propGetter(prop);
            return key ? prop[key] : ood.copy(prop);
        },
        setEvents: function (key, value) {
            var self = this;
            if (!key)
                self.events = {};
            else if (typeof key == 'string')
                self.events[key] = value;
            else if (ood.isHash(key)) {
                if (value/*force*/) {
                    self.events = ood.clone(key);
                } else {
                    ood.merge(self.events, key, 'all');
                }
            }
            return self;
        },
        getEvents: function (key) {
            return key ? this.events[key] : this.events;
        },
        setHooks: function (key, value) {
            var self = this;
            if (!key)
                self.hooks = {};
            else if (typeof key == 'string')
                self.hooks[key] = value;
            else if (ood.isHash(key)) {
                if (value/*force*/) {
                    self.hooks = ood.clone(key);
                } else {
                    ood.merge(self.hooks, key, 'all');
                }
            }
            return self;
        },
        getHooks: function (key) {
            return key ? this.hooks[key] : this.hooks;
        },
        notifyHooks: function (key, msg1, msg2, msg3, msg4, msg5) {
            var ns = this, hook, hooks = ns.hooks;
            if (key && hooks && (hook = hooks[key]) && ood.isFun(hook)) {
                ood.tryF(hook, ood.toArr(arguments).slice(1), ns);
            }
            return ns;
        },
        postMessage: function (msg1, msg2, msg3, msg4, msg5, sender) {
            this.fireEvent('onMessage', [this, msg1, msg2, msg3, msg4, msg5, sender]);
        },
        serialize: function (rtnString, keepHost, children) {
            var t, m,
                self = this,
                o = (t = self.constructor._beforeSerialized) ? t(self) : self,
                r = {
                    "ood.Module": true,
                    alias: o.alias,
                    key: o.KEY,
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
            var c = {}, p = o.box.$DataModel;
            ood.merge(c, o.properties, function (o, i) {
                return p[i] !== o
            });

            // for inner coms prf
            if (t = o.properties.__inner_coms_prf__) c.__inner_coms_prf__ = t;

            if (!ood.isEmpty(c)) r.properties = c;
            if (ood.isEmpty(r.properties)) delete r.properties;

            //functions
            if (!ood.isEmpty(c = o.functions)) r.functions = c;
            if (ood.isEmpty(r.functions)) delete r.functions;

            //events
            if (!ood.isEmpty(t = this.getEvents())) r.events = t;
            var eh = o.box.$EventHandlers;
            ood.filter(r.events, function (o, i) {
                return o !== eh[i];
            });

            //events
            if (!ood.isEmpty(t = this.getEvents())) r.events = t;
            if (ood.isEmpty(r.events)) delete r.events;

            return rtnString === false ? r : ood.serialize(r);
        },
        clone: function () {
            var ns = this.serialize(false, true);
            delete ns.alias;
            return this.constructor.unserialize(ns);
        },
        // for outter events
        fireEvent: function (name, args, host) {
            var self = this;
            if (self.$inDesign || (self.host && self.host.$inDesign)) return;

            var r, tp = self._evsPClsBuildIn && self._evsPClsBuildIn[name],
                ti = self._evsClsBuildIn && self._evsClsBuildIn[name],
                tt = self.events && self.events[name],
                applyEvents = function (prf, events, host, args) {
                    var j;
                    args = args || [];

                    if (ood.isStr(events) || ood.isFun(events)) events = [events];
                    if (ood.isArr(events.actions || events) && (events.actions && ood.isArr(events.actions) && events.actions.length > 0) && ood.isNumb(j = (events.actions || events)[0].event) && ood.isObj(args[j])) args[j] = ood.Event.getEventPara(args[j]);
                    return ood.pseudocode._callFunctions(events, args, host, null, prf.$holder, ((host && host.alias) || (prf.$holder && prf.$holder.alias)) + "." + prf.alias + "." + name);
                };
            self.$lastEvent = name;
            if (tp && (!ood.isArr(tp) || tp.length)) r = applyEvents(self, tp, self, args);
            if (ti && (!ood.isArr(ti) || ti.length)) r = applyEvents(self, ti, self, args);
            // only events can use host
            if (tt && (!ood.isArr(tt) || tt.length)) r = applyEvents(self, tt, host || self.host || self, args);
            return r;
        },
        // for inner events
        _fireEvent: function (name, args) {
            var self = this;
            args = args || [];
            args.splice(0, 0, self, self.threadid);
            return this.fireEvent(name, args);
        },
        _innerCall: function (name) {
            var self = this;
            if (!self[name]) {
                return;
            }
            return ood.tryF(self[name], [self, self.threadid], self);
        },
        customAppend: function (parent, subId, left, top, threadid) {
            return false;
        },
        popUp: function (pos, type, parent, trigger, group) {
            var module = this,
                f = function () {
                    module.getUIComponents(true).popUp(pos, type, parent, trigger, group);
                };
            if (self.created) f()
            else this.show(f);
        },

        fillParent: function (data) {
            if (this.getParentModule()) {
                this.getParentModule().setData(this.getData());
            }
        },


        show2: function (target, childname, params, data, parentModule, dio, top) {
            var ns = this, currpanel;
            var endFun = function () {
                var allData = {};
                if (data && ood.isHash(data)) {
                    ood.merge(allData, data);
                }
                try {
                    if (params && ood.isHash(params)) {
                        var urlParams = ood.getUrlParams(location.href);
                        if (urlParams) {
                            ood.merge(params, urlParams);
                        }
                        ood.each(allData, function (prf) {
                            if (prf && prf.name && params[prf.name]) {
                                prf.value = params[prf.name];
                            }
                        })
                        ood.merge(allData, params, 'all');
                    }
                    this.setData(allData)
                } catch (e) {
                    console.log(e)
                }
                this.setName(childname);
                ns.initData();
            };


            if (parentModule && ood.isFun(parentModule.addDioModule)) {
                parentModule.addDioModule(ns);
                this.setParentModule(parentModule);
                if (!target && parentModule.getProperties("target")) {
                    target = parentModule.getProperties("target");
                }

                while (!target && parentModule.getParentModule() && parentModule.getParentModule().getProperties) {
                    parentModule = parentModule.getParentModule();
                    target = parentModule.getProperties("target")
                }

                if (!childname && parentModule.getProperties("childname")) {
                    childname = parentModule.getProperties("childname");
                }

                if (target && !dio) {
                    parentModule = parentModule.parentModule;
                    currpanel = ood.get(parentModule, [target]) || ood.get(parentModule, ["host", target]);
                }
            }
            //  show: function (onEnd, parent, subId, threadid, left, top, reload) {
            this.show(endFun, currpanel, childname, null, null, top, true);
        },

        initData: function () {
            var self = this;
            ood.each(self._nodes, function (db) {
                if (db && db.Class) {
                    if (db.key == 'ood.APICaller') {
                        if (db.boxing().getAutoRun()) {
                            db.boxing().invoke();
                        }
                    } else if (db.boxing && db.boxing().checkValid) {
                        db.boxing().checkValid(true);
                    }
                }
            });
            this._fireEvent('afterShow');
        },


        checkMenu: function (viewBarId, pos, item) {
            var popMenu = this.ViewMenuBar[viewBarId];
            if (popMenu) {
                popMenu.setTagVar(item.tagVar);
                popMenu.pop(pos);
            }
        },

        show: function (onEnd, parent, subId, threadid, left, top, reload) {
            ood.UI.$trytoApplyCSS();
            if (false === this._fireEvent('beforeShow')) return false;
            parent = parent || ood('body');

            if (parent['ood.UIProfile']) parent = parent.boxing();

            var self = this, f = function () {
                var style = self.customStyle;
                if (style && !ood.isEmpty(style)) {
                    var arr = [];
                    ood.each(style, function (v, k) {
                        arr.push(k + " : " + v + ";");
                    });
                    var txt = ".ood-module-" + self.$xid + "{\r\n" + arr.join("\r\n") + "\r\n}";
                    ood.CSS.addStyleSheet(txt, "ood:css:module-" + self.$xid, 1);
                }
                // no UI control in module
                if (self.getUIComponents().isEmpty()) {
                    ood.tryF(self.customAppend, [parent, subId, left, top, threadid], self);
                    ood.tryF(onEnd, [null, self, threadid], self);
                } else {
                    // if parent is an ui object without rendered, dont render the module
                    if (!(parent && parent['ood.UI'] && !parent.get(0).renderId))
                        self.render();

                    if (parent && false === ood.tryF(self.customAppend, [parent, subId, left, top, threadid], self)) {
                        //append only
                        if (parent != self) {
                            parent.append(self.getUIComponents(false), subId);
                        }
                        // append and show
                        self.getUIComponents(true).each(function (o) {
                            o.boxing().show(parent, subId);
                            if (o.KEY == 'ood.UIProfile' && ood.get(o, ['properties', 'defaultFocus'])) {
                                try {
                                    ood.asyRun(function () {
                                        o.boxing().activate()
                                    })
                                } catch (e) {
                                    console.log(e);
                                }
                            }
                        });
                    }
                    self.renderId = 'ok';
                    ood.tryF(onEnd, [null, self, threadid], self);
                }
                self._showed = 1;
            };


            self.threadid = threadid;
            if (self.created) {
                f();
            } else {
                self.create(f, threadid);
            }
            return self;
        },

        hide: function () {
            this.getUIComponents(true).hide();
            this._showed = 0;
        },
        render: function (triggerLayout) {
            var self = this;
            if (self.renderId != 'ok') {
                self.renderId = 'ok';
                self.getUIComponents().render(triggerLayout);
                self._fireEvent('onRender');
            }
            return self;
        },
        refresh: function (callback, ignoreEffects, purgeNow) {
            var paras, b, p, s, fun,
                o = this,
                inm, firstUI,
                // for builder project module updating
                box = o.box,
                host = o.host,
                alias = o.alias,
                $xid = o.$xid,
                hashIn = o._render_conf,
                pPrf = o._render_holder,
                rt = o.$refreshTrigger,
                mcls = o.moduleClass,
                mxid = o.moduleXid;

            if (!o.renderId) return;
            if ((inm = o.getUIComponents()).isEmpty()) return;
            firstUI = inm.get(0);

            if (host && host['ood.Module']) {
                host.$ignoreAutoDestroy = true;
            }
            //keep parent
            if (b = !!firstUI.parent) {
                p = firstUI.parent.boxing();
                childId = firstUI.childrenId;
            } else {
                p = firstUI.getParent();
                if (!p) p = firstUI.getRoot().parent();
            }

            //unserialize
            s = o.serialize(false, true);
            o.destroy(true, true, true);
            //set back
            ood.merge(o, s, 'all');
            // notice: remove destroyed here
            delete o.destroyed;
            o.$xid = $xid;
            if (hashIn) o._render_conf = hashIn;
            if (pPrf) o._render_holder = pPrf;

            //create, must keep the original refrence pointer
            new box(o);
            if (host) o.setHost(host, alias);

            // must here
            o.moduleClass = mcls;
            o.moduleXid = mxid;

            o.create(function () {
                var f = function (t, m) {
                    if (callback) ood.tryF(callback);
                };
                //for functions like: UI refresh itself
                if (rt) rt.call(rt.target, o);
                //add to parent, and trigger RenderTrigger
                if (b) o.show(f, p, childId);
                else if (!p.isEmpty()) o.show(f, p);
            });

            if (o.host && o.host['ood.Module']) {
                delete o.host.$ignoreAutoDestroy;
            }
            return this;
        },


        reloadMenu: function () {
            var self = this;
            ood.each(self._nodes, function (db) {
                if (db && db.Class) {
                    if (db.key == 'ood.APICaller' && ood.str.endWith(db.alias, 'dynReload')) {
                        if (db.boxing().getAutoRun()) {
                            db.boxing().invoke();
                        }
                    }
                }
            });
        },

        reloadParent: function () {
            try {
                if (this.parentModule && this.parentModule.initData) {
                    this.parentModule.initData();

                }
            } catch (e) {
                console.warn(e);
            }

        },
        destroyParent: function () {
            try {
                if (this.getModule().parentModule && this.getModule().parentModule.reloadParent) {
                    this.getModule().parentModule.reloadParent();
                    this.parentModule.destroy();
                }
            } catch (e) {
                console.warn(e);
            }
        },

        destroyCurrDio: function () {
            this.destroyTopParent(this.getModule());
        },

        destroyTopParent: function (currModule) {
            try {
                var isDio = false;
                ood.each(currModule._ctrlpool, function (prf) {
                    try {
                        if (prf.box && prf.box['ood.UI.Dialog'] == true) {
                            isDio = true;
                        }
                    } catch (e) {
                        console.log(e);
                    }
                });
                if (isDio) {
                    currModule.reloadParent();
                    currModule.destroy();
                } else {
                    if (currModule.parentModule) {
                        this.destroyTopParent(currModule.parentModule);
                    }
                }

            } catch (e) {
                console.warn(e);
            }
        },

        getParent: function () {
            var prf = this.getUIComponents().get(0);
            if (prf) return prf.parent && prf.parent.boxing();
        },
        getChildrenId: function () {
            var prf = this.getUIComponents().get(0);
            if (prf) return prf.childrenId;
        },


        getChildByName: function (name) {
            var ns = this;
            var t = ood.get(ns, [name]) || ood.get(ns, ["host", name]);
            if (!t) {
                ood.each(ns._ctrlpool, function (prf) {
                    try {
                        if (prf.box && prf.box['ood.Module'] == true) {
                            if (ct = prf.getChildByName(name)) {
                                t = ct;
                            }
                        }
                    } catch (e) {
                        console.log(e);
                    }
                });
            }
            return t;
        },
        // onEnd(err, module, threadid)
        create: function (onEnd, threadid) {
            //get paras
            var self = this;

            if (self.created) {
                ood.tryF(onEnd, [null, self, threadid], self);
                return;
            }

            var t, funs = [];
            self.threadid = threadid;

            if (false === self._fireEvent('beforeCreated')) return;
            //if no threadid or threadid doesn't exist, reset threadid to self
            funs.push(function (threadid) {
                if (threadid)
                    self.threadid = threadid;
                self._fireEvent('onCreated');
            });

            //base classes
            if ((t = self.Dependencies) && t.length)
                funs.push(function (threadid) {
                    ood.require(self.Dependencies, null, function (uri, key) {
                        self._fireEvent('onLoadBaseClass', [uri, key]);
                    }, function () {
                        self._fireEvent('onLoadBaseClassErr', ood.toArr(arguments));
                    }, function () {
                        self._fireEvent('onLoadBaseClassErr', ood.toArr(arguments));
                    }, false, threadid);
                });
            if (self.iniComponents) {
                var arr = [];
                try {
                    (self.iniComponents + "").replace(/append\s*\(\s*ood.create\s*\(\s*['"]([\w.]+)['"]\s*[,)]/g, function (a, b) {
                        if (!ood.SC.get(b)) arr.push(b);
                    });
                } catch (e) {
                    console.log(e);
                }
                if (arr.length) {
                    if (self.Required && ood.isArr(self.Required)) {
                        self.Required = self.Required.concat(arr);
                    } else {
                        self.Required = arr;
                    }
                }
            }
            //load required class
            if ((t = self.Required) && t.length)
                funs.push(function (threadid) {
                    ood.require(self.Required, null, function (uri, key) {
                        self._fireEvent('onLoadRequiredClass', [uri, key]);
                    }, function () {
                        self._fireEvent('onLoadRequiredClassErr', ood.toArr(arguments));
                    }, function (msg) {
                        self._fireEvent('onLoadRequiredClassErr', ood.toArr(arguments));
                    }, false, threadid);
                });
            //inner components
            if (self.iniComponents)
                funs.push(function () {
                    try {
                        self._createInnerModules();
                    } catch (e) {

                    }

                });
            //load resource here
            if (self.iniResource)
                funs.push(function () {
                    self._fireEvent('onIniResource');
                    self._innerCall('iniResource');
                });
            //Outter components
            if (self.iniExComs) {
                self.iniExModules = self.iniExComs;
                delete self.iniExComs;
            }
            if (self.iniExModules)
                funs.push(function () {
                    self._innerCall('iniExModules');
                });
            //core
            funs.push(function (threadid) {
                //lazy load
                if (self.background)
                    ood.SC.runInBG(self.background);
                self._fireEvent('onReady');
            });
            funs.push(function (threadid) {
                self.created = true;
                ood.tryF(onEnd, [null, self, threadid], self);
            });
            if (threadid === false) {
                ood.arr.each(funs, function (fun) {
                    fun.call();
                });
            } else {
                //use asyUI to insert tasks
                ood.Thread.observableRun(funs, null, threadid);
            }

            return self;
        },
        _createInnerModules: function () {

            var self = this;
            if (self._recursived || self._innerModulesCreated)
                return;
            var stop, checkCycle = function (h) {
                if (h && h["ood.Module"] && h.moduleClass && h.moduleXid) {
                    if (self.KEY == h.moduleClass) {
                        if (self.$xid != h.moduleXid) {
                            self._recursived = h._recursived = true;
                            h.destroy();
                            self.destroy();
                            stop = 1;
                        } else {
                            // self is ok
                            return;
                        }
                    } else {
                        checkCycle(h.host);
                    }
                }
            };
            checkCycle(self.host);
            if (stop) {
                alert("There's a [" + self.KEY + "] in another [" + self.KEY + "], check this recursive call please!");
                return;
            }


            if (false === self._fireEvent('beforeIniComponents')) return;
            Array.prototype.push.apply(self._nodes, self._innerCall('iniComponents') || []);

            ood.arr.each(self._nodes, function (o) {
                ood.Module.$attachModuleInfo(self, o);
                //Recursive call
                if (o['ood.Module']) o._createInnerModules();
            });
            // attach destroy to the first UI control
            var autoDestroy = self.autoDestroy || self.properties.autoDestroy;
            if (autoDestroy)
                ood.arr.each(self._nodes, function (o) {
                    if (o.box && o.box["ood.UI"] && !o.box["ood.UI.MoudluePlaceHolder"] && !o.box.$initRootHidden) {
                        (o.$afterDestroy = (o.$afterDestroy || {}))["moduleDestroyTrigger"] = function (ignoreEffects, purgeNow) {
                            if (autoDestroy && !self.destroyed && !self.$ignoreAutoDestroy)
                                self.destroy(ignoreEffects, purgeNow);
                            self = null;
                        };
                        return false;
                    }
                });
            self._fireEvent('afterIniComponents');

            self._innerModulesCreated = true;
            // must be here
            self.setProperties({});
        },
        iniComponents: function () {
        },

        // calculate the profileTo's formula, and apply to it
        applyExcelFormula: function (profileTo) {
            var ns = this,
                xformula = ood.ExcelFormula,
                formula = profileTo && profileTo.properties.excelCellFormula,
                colMax, rowMax,
                cellsMap = {},
                cell2alias = {}, alias2cell = {};
            if (formula) {
                ood.each(this._ctrlpool, function (prf) {
                    var p = prf.properties, t;
                    if ((t = p.excelCellId) && /^\s*[a-zA-Z]+[\d]+\s*$/.test(t)) {
                        cell2alias[t] = prf.alias;
                        alias2cell[prf.alias] = t;
                        t = xformula.toCoordinate(t, 0);
                        colMax = Math.max(colMax, t[0]);
                        rowMax = Math.max(rowMax, t[1]);
                    }
                });
                var refs = xformula.getRefCells(formula, colMax, rowMax)
                if (!refs) return;
                ood.each(cell2alias, function (o, i) {
                    if (i in refs) {
                        if (!(i in cellsMap)) {
                            cellsMap[i] = ns[o].getExcelCellValue();
                        }
                    }
                });
                profileTo.boxing()._applyExcelFormula(cellsMap);
            }
            return ns;
        },
        // calculate all profiles' (or profileFrom's)  formula, and apply to them(it)
        triggerExcelFormulas: function (profileFrom) {
            var ns = this,
                formulaCells = {}, cell2alias = {}, alias2cell = {},
                xformula = ood.ExcelFormula,
                rowMax = 0, colMax = 0,
                cellId = profileFrom && profileFrom.alias;
            //1. collection all formula cells
            ood.each(this._ctrlpool, function (prf) {
                var p = prf.properties, t;
                if (t = p.excelCellFormula) {
                    formulaCells[prf.alias] = [prf, t];
                }
                if ((t = p.excelCellId) && /^\s*[a-zA-Z]+[\d]+\s*$/.test(t)) {
                    t.replace(/\s/g, '');
                    cell2alias[t] = prf.alias;
                    alias2cell[prf.alias] = t;
                    t = xformula.toCoordinate(t, 0);
                    colMax = Math.max(colMax, t.col);
                    rowMax = Math.max(rowMax, t.row);
                }
            });
            // if input cell, must remove itself;
            if (cellId) delete formulaCells[cellId];
            if (ood.isEmpty(formulaCells)) return;

            //2. collect refs for formulaCells
            var refs = {};
            ood.each(formulaCells, function (a, alias, hash, hash1) {
                if (hash = xformula.getRefCells(a[1], colMax, rowMax)) {
                    hash1 = {};
                    ood.each(hash, function (o, i) {
                        hash1[cell2alias[i]] = o;
                    });
                    refs[alias] = hash1;
                }
            });
            //3. loop to calculate non-ref cells
            var count, noFormulaRef, cellsMap = {}, coo,
                changed = {}, needRec;
            if (cellId) {
                changed[cellId] = 1;
            }
            do {
                count = 0;
                ood.filter(refs, function (v, alias) {
                    needRec = 0;
                    if (!cellId) needRec = 1;
                    else {
                        for (var i in v) {
                            if (i in changed) {
                                needRec = 1;
                                break;
                            }
                        }
                    }
                    // no need to re-calculate
                    if (!needRec) {
                        return false;
                    }

                    noFormulaRef = true;
                    for (var i in v) {
                        if (!cellId && (i in formulaCells)) {
                            noFormulaRef = false;
                        } else {
                            if (!(alias2cell[i] in cellsMap)) {
                                cellsMap[alias2cell[i]] = ns[i].getExcelCellValue();
                            }
                        }
                    }
                    if (noFormulaRef) {
                        // update value
                        ns[alias]._applyExcelFormula(cellsMap);
                        if (cellId) changed[alias] = 1;
                        // remove from formulaCells
                        delete formulaCells[alias];
                        count++;
                        return false;
                    }
                });
            }
                // Avoid circular references
            while (!ood.isEmpty(formulaCells) && count > 0);
            return ns;
        },

        getProfile: function () {
            if (!this._innerModulesCreated) this._createInnerModules();

            var hash = {}, t;
            ood.each(this._ctrlpool, function (prf) {
                t = hash[prf.alias] = prf.serialize(false, false, false);
                delete t.key;
                delete t.alias;
                delete t.events;
                delete t['ood.Module'];
            });
            return hash;
        },
        setProfile: function (profiles) {
            if (!this._innerModulesCreated) this._createInnerModules();

            ood.each(this._ctrlpool, function (prf, i) {
                if (prf.alias in profiles) {
                    i = profiles[prf.alias];
                    var ins = prf.boxing();
                    if (i && ood.isHash(i) && !ood.isEmpty(i)) {
                        if (i.theme && typeof(ins.setTheme) == "function") ins.setTheme(i.theme);
                        if (i.properties && !ood.isEmpty(i.properties)) ins.setProperties(i.properties);
                        if (i.CA && !ood.isEmpty(i.CA)) ins.setCustomAttr(i.CA);
                        if (i.CC && !ood.isEmpty(i.CC)) ins.setCustomClass(i.CC);
                        if (i.CS && !ood.isEmpty(i.CS)) ins.setCustomStyle(i.CS);
                    } else {
                        ins.setValue(i);
                    }
                }
            });
            return this;
        },
        getPropBinderKeys: function (scope_set, scope_clear) {
            if (!this._innerModulesCreated) this._createInnerModules();

            scope_set = scope_set || ood._scope_set;
            scope_clear = scope_clear || ood._scope_clear;

            // collect keys
            var hash = {};
            try {
                scope_set.call(this);
                ood.each(this._ctrlpool, function (prf) {
                    var prop = prf.properties;
                    if (prop.propBinder)
                        ood.each(prop.propBinder, function (fun, key) {
                            if (key in prop) {
                                if (ood.isFun(fun)) fun();
                            }
                        });
                });
            } catch (e) {
                console.log(e);
            } finally {
                scope_clear.call(this);
            }

            ood.each(hash, function (v, k) {
                hash[k] = ood.toArr(v, true);
            });
            return hash;
        },
        reBindProp: function (dataMap, scope_set, scope_clear) {
            if (!this._innerModulesCreated) this._createInnerModules();
            scope_set = scope_set || ood._scope_set;
            scope_clear = scope_clear || ood._scope_clear;

            try {
                scope_set.call(this, dataMap);
                ood.each(this._ctrlpool, function (prf) {
                    prf.boxing().reBindProp(dataMap, scope_set, scope_clear, true);
                });
            } catch (e) {
                scope_clear.call(this);
            }
        },
        getData: function (withValue) {
            if (!this._innerModulesCreated) this._createInnerModules();

            var hash = {};
            ood.each(this._ctrlpool, function (prf) {

                var prop = prf.properties,
                    ins = prf.boxing(),
                    ih = hash[prf.alias] = {};
                if (ins && ih) {
                    try {
                        ood.arr.each(["src", "html", "disabled", "readonly", "items", "listKey", "header", "rows", "target", "toggle", "attr", "JSONData", "XMLData", "JSONUrl", "name", "XMLUrl", 'value', 'labelCaption'], function (k) {
                            if (k in prop) ih[k] = prop[k];
                        });
                        if (withValue)
                            if ('value' in prop) ih.value = prop.value;
                        if ('caption' in prop && ood.isSet(prop.caption)) {
                            ih.caption = typeof(ins.getCaption) == "function" ? ins.getCaption() : prop.caption;
                        }
                    } catch (e) {
                        log.warn(e);
                    }
                }

                //      ood.arr.each(["src","html","items","listKey","header","rows","target","toggle","attr","JSONData","XMLData","JSONUrl","XMLUrl",'name','value','labelCaption'],function(k){

            });
            return hash;
        },
        setData: function (data, host) {
            var ns = this, obj = host || ns, modules = [];
            ood.each(obj._ctrlpool, function (prf) {
                try {
                    var prop = prf.properties,
                        ins = prf.boxing(), ih;
                    if (prop && ins && data) {
                        if (prf.box['ood.Module'] == true) {
                            modules.push(ins);
                        } else {
                            ih = data[prf.alias] || data[prf.name]
                            if (ih) {
                                try {
                                    ih = data[prf.alias];
                                    if (ih && ood.isHash(ih) && !ood.isEmpty(ih)) {
                                        // "name",//字段名称不复制
                                        ood.arr.each(["src", "disabled", "readonly", "html", "items", "listKey", "header", "rows", "target", "toggle", "attr", "JSONData", "XMLData", "JSONUrl", "XMLUrl", "value", "UIValue", 'labelCaption', "caption"], function (k) {
                                            if (k in prop && k in ih) ins['set' + ood.str.initial(k)](ih[k]);
                                        });

                                    } else if (ins.setValue) {
                                        try {
                                            ins.setValue(ih);
                                        } catch (e) {
                                            console.log(e);
                                        }
                                    }

                                    if (ins.getItemByItemId && ins.getItemByItemId(ih) && ins.selectItem) {
                                        try {
                                            ins.selectItem(ih);
                                        } catch (e) {
                                            console.log(e);
                                        }
                                    }
                                } catch (e) {
                                    console.log(e);
                                }
                            }
                        }
                    }
                } catch (e) {
                    console.log(e);
                }
                try {
                    var PAGECTX = ns.getCtxComponents();
                    if (PAGECTX && PAGECTX.boxing) {
                        PAGECTX.boxing().setFormValues(data);
                    }
                } catch (e) {
                    console.log(e);
                }

            });


            ood.each(modules, function (prf) {
                try {
                    var prop = prf.properties,
                        ins = prf.boxing(), ih;
                    if (prop && ins && data) {
                        if (prf.box['ood.Module'] == true) {
                            ins.setData(data, prf)
                            ins.initData(true);
                        }
                        ;
                    }
                } catch (e) {
                    console.log(e);
                }

            });
            return this;
        },

        // fack absValue
        getValue: function (innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();

                var hash = {}, cap, uv;
                ood.each(this._ctrlpool, function (prf) {
                    if ('value' in prf.properties) {
                        if (ood.isSet(prf.properties.caption)) {
                            cap = prf.properties.caption;
                            uv = prf.properties.value;

                            // igore unnecessary caption
                            if ((!cap && !uv) || cap == uv)
                                hash[prf.alias] = uv;
                            else
                                hash[prf.alias] = {value: uv, caption: cap};
                        }
                        else {
                            hash[prf.alias] = prf.properties.value;
                        }
                    }
                });
                return hash;
            } else {
                return this.properties.value;
            }
        },
        setValue: function (values, innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();

                if (!ood.isEmpty(values)) {
                    ood.each(this._ctrlpool, function (prf) {
                        if ('value' in prf.properties && prf.alias in values) {
                            var v = values[prf.alias], b = ood.isHash(v), ins = prf.boxing(), p = prf.properties;
                            if (p.items && p.items.length == 0) {
                                p.items = [{id: v, value: v, caption: v}];
                            }
                            ins.setValue((b && ('value' in v)) ? v.value : v, true, 'module');

                            if (typeof(prf.boxing().setCaption) == "function" && b && 'caption' in v)
                                prf.boxing().setCaption(v.caption, null, true, 'module');
                        }
                    });
                }
            } else {
                this.properties.value = values;
            }
            return this;
        },
        getUIValue: function (innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();

                var hash = {};
                ood.each(this._ctrlpool, function (prf) {
                    if ('$UIvalue' in prf.properties)
                        hash[prf.alias] = prf.properties.$UIvalue;
                });
                return hash;
            } else {
                return this.$UIvalue;
            }
        },
        setUIValue: function (values, innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();

                if (!ood.isEmpty(values)) {
                    ood.each(this._ctrlpool, function (prf) {
                        if ('value' in prf.properties && prf.alias in values) {
                            var v = values[prf.alias], b = ood.isHash(v);
                            prf.boxing().setUIValue((b && ('value' in v)) ? v.value : v, true, false, 'module');
                            if (typeof(prf.boxing().setCaption) == "function" && b && 'caption' in v)
                                prf.boxing().setCaption(v.caption, null, true, 'module');
                        }
                    });
                }
            } else {
                this.$UIvalue = values;
            }
            return this;
        },
        resetValue: function (innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();
                ood.each(this._ctrlpool, function (prf) {
                    if (prf.boxing().resetValue) prf.boxing().resetValue();
                });
            } else {
                this.$UIvalue = this.properties.value;
            }
            return this;
        },
        updateValue: function (innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();
                ood.each(this._ctrlpool, function (prf) {
                    if (prf.boxing().updateValue) prf.boxing().updateValue();
                });
            } else {
                this.properties.value = this.$UIvalue;
                return this;
            }
            return this;
        },
        isDirtied: function (innerUI) {
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();

                var dirtied = false;
                ood.each(this._ctrlpool, function (prf) {
                    if (prf.boxing().isDirtied) {
                        if (prf.boxing().isDirtied()) {
                            return false;
                        }
                    }
                });
                return dirtied;
            } else {
                return this.properties.value === this.$UIvalue;
            }
        },

        checkValid: function (innerUI) {
            var valid = true;
            if (innerUI) {
                if (!this._innerModulesCreated) this._createInnerModules();
            }
            ood.each(this._ctrlpool, function (prf) {
                if (prf.boxing().checkValid) {
                    if (!prf.boxing().checkValid()) {
                        valid = false;
                    }
                }
            });
            return valid;

        },

        getDataBinders: function () {
            if (!this._innerModulesCreated) this._createInnerModules();

            var nodes = ood.copy(this._nodes), t, k = 'ood.DataBinder';
            ood.filter(nodes, function (o) {
                return !!(o.box[k]);
            });
            return nodes;
        },
        getForms: function () {
            if (!this._innerModulesCreated) this._createInnerModules();

            var nodes = ood.copy(this._ctrlpool), t, k = 'ood.absContainer';
            ood.filter(nodes, function (o) {
                return !!(o.box[k]);
            });
            return nodes;
        },


        //ctx
        addCtxComponents: function (items) {
            var PAGECTX = this.getCtxComponents().boxing();
            ood.addChild({children: items}, PAGECTX, this, this);
        },

        //ctx
        getCtxComponents: function () {
            if (!this._innerModulesCreated) this._createInnerModules();
            var nodes = ood.copy(this._ctrlpool), k = 'ood.absContainer', PAGECTX;
            ood.filter(nodes, function (o) {
                if (o.box && o.box[k] && o.alias && o.alias == 'PAGECTX') {
                    PAGECTX = o;
                }
            });

            if (!PAGECTX) {
                PAGECTX = ood.create("ood.UI.Block")
                    .setAlias('PAGECTX')
                this.AddChild(PAGECTX);

            }
            return PAGECTX;
        },
        // get all children
        getAllComponents: function () {
            if (!this._innerModulesCreated) this._createInnerModules();
            var nodes = [];
            var fun = function (m) {
                if (m["ood.Module"]) {
                    ood.each(m._ctrlpool, function (o) {
                        if (o["ood.Module"]) fun(o);
                        else nodes.push(o);
                    });
                }
            };
            fun(this);
            return ood.absObj.pack(nodes, false);
        },
        // get first level children only
        getComponents: function () {
            if (!this._innerModulesCreated) this._createInnerModules();
            var nodes = [];
            var fun = function (m) {
                if (m["ood.Module"]) {
                    ood.arr.each(m._nodes, function (o) {
                        if (o["ood.Module"]) fun(o);
                        else nodes.push(o);
                    });
                }
            };
            fun(this);
            return ood.absObj.pack(nodes, false);
        },
        // get first level UI children only
        // flag:true => no  $initRootHidden
        // flag:false => $initRootHidden
        // no flag: all
        getUIComponents: function (flag) {
            var nodes = this.getComponents().get(),
                k = 'ood.UI', n = '$initRootHidden';
            ood.filter(nodes, function (o) {
                return !!(o && o.box && o.box[k]) && (flag === true ? !o.box[n] : flag === false ? o.box[n] : true);
            });
            return ood.UI.pack(nodes, false);
        },
        setComponents: function (obj) {
            var self = this, t;
            ood.arr.each(self._nodes, function (o) {
                if ((t = self[o.alias]) && t.get(0) == o)
                    delete self[o.alias];
            });
            ood.arr.each(self._nodes = obj.get(), function (o) {
                // set host
                o.boxing().setHost(self, o.alias);
            });
            ood.arr.each(self._nodes, function (o) {
                ood.Module.$attachModuleInfo(self, o);
            });
            return self;
        },
        AddComponents: function (obj) {
            var self = this;
            ood.arr.each(obj.get(), function (o) {
                o.boxing().setHost(self, o.alias);
                self._nodes.push(o);
                ood.Module.$attachModuleInfo(self, o);
            });
            return self;
        },

        AddChild: function (obj) {
            var self = this;

            // if (obj && obj.Class) {
            //     obj.boxing().setHost(self, obj.alias);
            // }else{
            //     obj.n0.boxing().setHost(self, obj.alias);
            // }
            //
            try {
                obj.n0.boxing().setHost(self, obj.alias);
            } catch (e) {
                obj.boxing().setHost(self, obj.alias);
            }
            self._nodes.push(obj);
            ood.Module.$attachModuleInfo(self, obj);
            return self;
        },

        isDestroyed: function () {
            return !!this.destroyed;
        },


        destroy: function (ignoreEffects, purgeNow, keepStructure) {
            var self = this, con = self.constructor, ns = self._nodes;
            if (self.destroyed) return;
            if (false === self._fireEvent('beforeDestroy')) return;
            self._fireEvent('onDestroy');
            if (self.alias && self.host && self.host[self.alias]) {
                try {
                    delete self.host[self.alias]
                } catch (e) {
                    self.host[self.alias] = null
                }
            }
            ood.arr.each(self.getDioModule(), function (o) {
                if (o && ood.isFun(o.destroy)) {
                    o.destroy();
                }

            })

            //set once
            self.destroyed = true;
            if (ns && ns.length)
                ood.arr.each(ns, function (o) {
                    if (o && o.box)
                        o.boxing().destroy(ignoreEffects, purgeNow);
                }, null, true);

            if (ns && ns.length)
                self._nodes.length = 0;
            self._ctrlpool = null;

            self.unLinkAll();

            if (!keepStructure) {
                ood.breakO(self);
            } else {
                // for refresh itself
                delete self.renderId;
                delete self.created;
                delete self._innerModulesCreated;
            }
            //afterDestroy
            if (self.$afterDestroy) {
                ood.each(self.$afterDestroy, function (f) {
                    ood.tryF(f, [ignoreEffects, purgeNow], self);
                });
                ood.breakO(self.$afterDestroy, 2);
            }
            //set again
            self.destroyed = true;

        }
    },
    Static: {
        // fake absValue
        "ood.absValue": true,
        refresh: function (code) {
            var m = this, keep = {
                    '$children': m.$children,
                    _cache: m._cache,
                    _nameId: m._nameId
                },
                key = m.KEY,
                path = key.split("."),
                n;
            // clear cache
            if (s = ood.get(window, ['ood', '$cache', 'SC'])) delete s[key];
            ood.set(window, path);
            // rebuild
            ood.exec(code);
            // the new one
            n = ood.get(window, path);
            // merge new to old
            ood.merge(m, n, function (o, i) {
                return n.hasOwnProperty(i);
            });
            ood.merge(m.prototype, n.prototype, function (o, i) {
                return n.prototype.hasOwnProperty(i);
            });
            // restore those
            ood.merge(m, keep, 'all');
            // break new
            ood.breakO(n.prototype, 1);
            ood.breakO(n, 1);
            // restore namespace
            ood.set(window, path, m);
            return m;
        },
        pickAlias: function () {
            return ood.absObj.$pickAlias(this);
        },
        getFromDom: function (id) {
            var prf = ood.UIProfile.getFromDom(id);
            if (prf && (prf = prf.host)) {
                return (!prf.destroyed) && prf;
            }
        },
        getClsFromDom: function (id) {
            return ood.get(this.getFromDom(id), ["KEY"]);
        },
        getAllInstance: function () {
            var hash = {};
            ood.arr.each(this._cache, function (o) {
                hash[o.$xid] = o;
            });
            return hash;
        },
        // module: module class name
        // xid: module xid
        // ood.Module.getInstance("App.Cls1",1)
        // ood.Module.getInstance("App.Cls1","c")
        // App.Cls1.getInstance(1)
        // App.Cls1.getInstance('c')
        // App.Cls1.getInstance() == App.Cls1.getInstance(0) : get the first instance
        getInstance: function (module, xid) {
            var m = this;
            if (!xid && module) {
                if (module['ood.Profile'] && module.moduleClass && module.moduleXid) {
                    xid = module.moduleXid;
                    module = module.moduleClass;
                } else {
                    xid = module;
                    module = null;
                }
            }
            if (module) {
                m = ood.SC.get(module);
                if (!m || !m['ood.Module']) return;
            } else {
                m = this;
            }
            var c = m._cache;
            if (xid) {
                for (var i in m._cache)
                    if (ood.isFinite(i) ? (xid + "") == i : ('$' + xid) == i)
                        return m._cache[i];
            } else {
                return c[0];
            }
        },
        postMessage: function (cls, msg1, msg2, msg3, msg4, msg5, sender) {
            var m = ood.SC.get(cls), hash;
            if (m && m['ood.Module'])
                ood.arr.each(m._cache, function (o) {
                    m.fireEvent('onMessage', [m, msg1, msg2, msg3, msg4, msg5, sender]);
                });
        },
        destroyAll: function (ignoreEffects, purgeNow) {
            ood.arr.each(this._cache, function (o) {
                if (!o.destroyed) o.destroy(ignoreEffects, purgeNow);
            });
        },
        // onEnd(err, module)
        load: function (cls, onEnd, lang, theme, showUI, parent, subId, onCreated, onDomReady) {
            if (!cls) {
                var e = new Error("No cls");
                ood.tryF(onEnd, [e, null]);
                throw e;
            }
            // compitable
            if (typeof theme == 'function') showUI = theme;

            var applyEnv = function (setting) {
                    var t;

                    // overwrite theme
                    if ((t = setting.theme)/* && !theme*/) theme = t;

                    //[[ apply memory
                    // apply SpaceUnit
                    if (t = setting.SpaceUnit) ood.SpaceUnit = t;
                    // apply DefaultProp
                    if ((t = ood.ini.$DefaultProp) && ood.isHash(t)) {
                        var allp = {}, ctl;
                        ood.each(t, function (v, k) {
                            if (/^ood\.UI\./.test(k) && ood.isHash(v) && (ctl = ood.get(window, k.split('.')))) {
                                ctl.setDftProp(v);
                            } else {
                                allp[k] = v;
                            }
                        });
                        if (!ood.isEmpty(allp)) {
                            ood.UI.setDftProp(allp);
                        }
                    }
                    //]] apply memory

                    //[[ apply dom
                    // apply zoom
                    // use setting.zoom to determine whether to call zoom or not
                    if (setting.zoom) {
                        var zoom = function (type, width, height) {
                            var rw = parseInt(width, 10) || 800, rh = parseInt(height, 10) || 600;
                            if (!ood.isNumb(type)) {
                                var win = ood.win, ww = win && win.width(), wh = win && win.height(), cl;
                                if (ww && wh) {
                                    var r_w = ww / rw, r_h = wh / rh;
                                    switch (type.split('-')[0]) {
                                        case 'width':
                                            type = r_w;
                                            cl = 'ood-css-noscrollx';
                                            break;
                                        case 'height':
                                            type = r_h;
                                            cl = 'ood-css-noscrolly';
                                            break;
                                        case 'cover':
                                            type = Math.max(r_w, r_h);
                                            cl = 'ood-css-noscroll' + (r_w >= r_h ? 'x' : 'y');
                                            break;
                                        case 'contain':
                                            type = Math.min(r_w, r_h);
                                            cl = 'ood-css-noscroll';
                                            break;
                                    }
                                    ood('html').removeClass(/^ood-css-noscroll(x|y)?$/).addClass(cl);
                                    ood.ini.$fixFrame = 1;
                                }
                            }
                            if ((type = parseFloat(type)) && type != 1) {
                                if (ood.ini.$fixFrame) {
                                    ood('html').css({width: rw + 'px', height: rh + 'px'});
                                    ood.frame = ood('html');
                                }

                                // keep the scale for calculating [window]'s dimension and adjusting event's pageX/pageY
                                ood.ini.$zoomScale = type;
                                h[b.cssTag1 + "transform-origin"] = h["transform-origin"] = '0 0 0';
                                ood.Dom.$setZoom(ood('html').get(0), type);
                                // 'getBoundingClientRect' will need to adjust too
                                ood.ini.$transformScale = type;
                            }
                        };
                        if (t = ood.ini.$frame) {
                            zoom(t.zoom, t.width, t.height);

                            if (!ood.isNumb(t.zoom) && /-resize$/.test(t.zoom + '')) {
                                ood.win.onSize(function () {
                                    var t = ood.ini.$frame;
                                    ood.resetRun("_ood_auto_zoom", zoom, 0, [t.zoom, t.width, t.height]);
                                }, "_ood_auto_zoom");
                            }
                        }
                    }
                    // apply background
                    if ((t = setting.background) && ood.isHash(t)) {
                        ood.each(t, function (v, k) {
                            ood('html').css(k, ood.adjustRes(v));
                        });
                    }
                    // apply ood-custom
                    if ((t = ood.ini.$ElementStyle) && ood.isHash(t)) ood.CSS.setStyleRules(".ood-custom", t, true);
                    //]] apply dom

                    //[[ apply url
                    // apply CDN font icons
                    if ((t = ood.ini.$FontIconsCDN) && ood.isHash(t)) {
                        // use asyn
                        ood.asyRun(function () {
                            ood.each(t, function (o, i) {
                                if (o.href && !o.disabled) {
                                    var attr = {crossorigin: 'anonymous'};
                                    ood.merge(attr, o, function (v, j) {
                                        return j !== 'href' && j !== 'disabled'
                                    });
                                    ood.CSS.includeLink(ood.adjustRes(o.href), 'ood_app_fscdn-' + i, false, attr);
                                }
                            });
                        }, 20);
                    }
                    //]] apply url
                },
                createModule = function (path) {
                    var clsObj = this, t, setting = {},
                        showModule = function (i, l, flag) {
                            if (!ood.isFun(clsObj)) {
                                var e = new Error("'" + cls + "' is not a constructor");
                                ood.tryF(onEnd, [e, null]);
                                throw e;
                            } else {
                                var o = new clsObj();
                                // record it
                                clsObj._callfrom = cls;

                                ood.set(ood.ModuleFactory, ["_cache", cls], o);

                                if (onCreated) ood.tryF(onCreated, [o]);

                                if (showUI !== false) o.show(onEnd, parent, subId);
                                else ood.tryF(onEnd, [null, o], o);
                            }
                        };
                    //if successes
                    if (path) {
                        //[[ collect setting (background, spaceunit, view size,  view zoom ...
                        // for non-project
                        if ((t = this.designViewConf) && ood.isHash(t)) ood.merge(setting, t);
                        if ((t = this.viewStyles) && ood.isHash(t)) {
                            ood.each(t, function (o, i) {
                                if (/^background/.test(i)) (setting.background || (setting.background = {}))[i] = o;
                                else setting[i] = o;
                            });
                        }
                        //]] collect setting

                        // for zoom
                        if (setting.zoom) ood.set(ood.ini, ['$frame', 'zoom'], setting.zoom);
                        if (setting.width) ood.set(ood.ini, ['$frame', 'width'], setting.width);
                        if (setting.height) ood.set(ood.ini, ['$frame', 'height'], setting.height);

                        if (!ood.isEmpty(setting)) applyEnv(setting);

                        // If theme is not 'default', apply theme frist
                        if (theme && theme != "default") {
                            ood.setTheme(theme, true, function () {
                                if (lang && lang != 'en') ood.setLang(lang, showModule); else showModule();
                            }, function () {
                                ood.alert("Can't load theme - " + theme);
                                if (lang && lang != 'en') ood.setLang(lang, showModule); else showModule();
                            });
                        } else {
                            // If lang is not 'en', apply lang frist
                            if (lang && lang != 'en') ood.setLang(lang, showModule); else showModule();
                        }
                    } else {
                        var e = new Error("No class name");
                        ood.tryF(onEnd, [e, null]);
                        throw e;
                    }
                },
                domReady = function () {
                    if (onDomReady) ood.tryF(onDomReady);

                    var t, setting = {};
                    //[[ collect setting (background, spaceunit, view size,  view zoom ...
                    // for project
                    if ((t = ood.ini.$DevEnv) && ood.isHash(t)) {
                        if (t.SpaceUnit) setting.SpaceUnit = t.SpaceUnit;
                        if (t = t.designViewConf) ood.merge(setting, t, 'all');
                    }
                    if ((t = ood.ini.$PageAppearance) && ood.isHash(t)) {
                        ood.merge(setting, t, 'all');
                    }
                    //]] collect setting

                    // for zoom
                    if (setting.zoom) ood.set(ood.ini, ['$frame', 'zoom'], setting.zoom);
                    if (setting.width) ood.set(ood.ini, ['$frame', 'width'], setting.width);
                    if (setting.height) ood.set(ood.ini, ['$frame', 'height'], setting.height);

                    if (!ood.isEmpty(setting)) applyEnv(setting);

                    if (typeof(cls) == 'function' && cls.$ood$) createModule.apply(['ok'], cls);
                    else cls = cls + "";
                    if (/\//.test(cls) && !/\.js$/i.test(cls))
                        cls = cls + ".js";
                    if (/\.js$/i.test(cls)) {
                        ood.fetchClass(cls, createModule,
                            function (e) {
                                ood.tryF(onEnd, [e, null]);
                            });
                    } else {
                        //get app class
                        ood.SC(cls, createModule, true, null, {
                            retry: 0,
                            onFail: function (e) {
                                ood.tryF(onEnd, [e, null]);
                            }
                        });
                    }
                };

            if (ood.isDomReady) domReady(); else ood.main(domReady);
        },
        unserialize: function (hash) {
            return new this(hash);
        },
        $attachModuleInfo: function (module, prf) {
            // module in module
            if (prf.moduleClass && prf.moduleXid) {
                var t = ood.SC.get(prf.moduleClass);
                t = t.getInstance(prf.moduleXid);
                if (t !== module) {
                    t.moduleClass = module.KEY;
                    t.moduleXid = module.$xid;
                    return;
                }
            }

            prf.moduleClass = module.KEY;
            prf.moduleXid = module.$xid;
            ood.arr.each(prf.children, function (v) {
                ood.Module.$attachModuleInfo(module, v[0]);
            });
        },

        // for setting only
        $DataModel: {
            autoDestroy: true,
            euClassName: "",
            className: "",
            dataBinder: "",
            value: ""
        },
        $EventHandlers: {
            onHookKey: function (module, key, e, keyDown) {
            },
            onFragmentChanged: function (module, fragment, init, newAdd) {
            },
            onMessage: function (module, msg1, msg2, msg3, msg4, msg5, source) {
            },
            beforeCreated: function (module, threadid) {
            },
            onLoadBaseClass: function (module, threadid, uri, key) {
            },
            onLoadBaseClassErr: function (module, threadid, key) {
            },
            onLoadRequiredClass: function (module, threadid, uri, key) {
            },
            onLoadRequiredClassErr: function (module, threadid, uri) {
            },
            onIniResource: function (module, threadid) {
            },
            beforeIniComponents: function (module, threadid) {
            },
            afterIniComponents: function (module, threadid) {
            },
            afterShow: function (module, threadid) {
            },
            onModulePropChange: function (module, threadid) {
            },
            onReady: function (module, threadid) {
            },
            onRender: function (module, threadid) {
            },
            beforeDestroy: function () {
            },
            onDestroy: function (module) {
            }
        }
    }
});ood.Class('ood.XML',null,{
    Static:{
        //return xml text (for post data)
        json2xml:function(jsonObj, kf, vf){
           var arr=[],
           _f=function(key,value,arr){
                if(typeof value=="object"){
                    if(ood.isArr(value)){
                        if(value.length){
                            for(var i=0,l=value.length; i<l; i++)
                                arr.push(_f(key,value[i],arr));
                        }else
                            arr.push("<"+(kf?kf(key):key)+">"+"__[]__"+"</"+(kf?kf(key):key)+">");
                    }else{
                        var b;
                        arr.push("<"+(kf?kf(key):key));
                        for(var i in value) {
                            if(i.charAt(0)=="@")
                                arr.push(" "+i.substr(1)+'="'+(vf?vf(value[i]):value[i])+'"');
                            else
                                b=1;
                        }
                        arr.push(b?">":"/>");
                        if(b){
                            for(var i in value) {
                                if(i=="#text")
                                    arr.push((vf?vf(value[i]):value[i]));
                                else if(i=="#cdata")
                                    arr.push("<![CDATA["+(vf?vf(value[i]):value[i])+"]]>");
                                else if (i.charAt(0)!="@")
                                    arr.push(_f(i,value[i],arr));
                            }
                            arr.push("</"+(kf?kf(key):key)+">");
                        }
                    }
                }else
                    arr.push("<"+(kf?kf(key):key)+">"+(vf?vf(value):value)+"</"+(kf?kf(key):key)+">");
           };
           for(var i in jsonObj)
              _f(i,jsonObj[i],arr);
           return '<?xml version="1.0" encoding="UTF-8" ?>'+arr.join('');
        },
        //return json object (for request data)
        xml2json:function(xmlObj){
            if(xmlObj.nodeType==9)
                xmlObj=xmlObj.documentElement;
            var o={},
            M={
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            },
            R=/^-?(\d\d*\.\d*$)|(^-?\d\d*$)|(^-?\.\d\d*$)/,
            _map={
                "__[]__":[],
                "null":null,
                'false':false,
                'true':true
             },
            _es=function(str){
                return str.replace(/[\s\S]/g,function(a,b){return (b=M[a])?b:a});
            },
            _clear = function(xml) {
                var n,k;
                xml.normalize();
                for(n=xml.firstChild;n;){
                    k=n;
                    if(n.nodeType==1)_clear(n);
                    n=n.nextSibling;
                    if(k.nodeType==3 && !k.nodeValue.match(/\S/))
                        xml.removeChild(k);
                }
                return xml;
            },
            _xml=function(n){
                if ("innerHTML" in n){
                    n=n.innerHTML;
                    n=n in _map?_map[n]:R.test(n)?parseFloat(n):n;
                }else{
                    var arr=[],t,
                    _in=function(n) {
                        if(n.nodeType==1) {
                            arr.push("<"+n.nodeName);
                            var m=n.attributes;
                            for(var i=0,l=m.length;i<l;i++)
                                arr.push(" "+m[i].nodeName+'="'+(m[i].nodeValue||"")+'"');
                            if (n.firstChild) {
                                arr.push(">");
                                for(m=n.firstChild;m;m=m.nextSibling)
                                    arr.push(_in(m));
                                arr.push("</"+n.nodeName+">");
                            }else arr.push("/>");
                        }else if(n.nodeType==3){
                            n=n.nodeValue;
                            arr.push(n in _map?_map[n]:R.test(n)?parseFloat(n):n);
                        }else if(n.nodeType==4)
                            arr.push("<![CDATA[" + n.nodeValue + "]]>");
                    };
                    for(var m=n.firstChild;m;m=m.nextSibling)
                        _in(m);
                    n=(arr.length==1?arr[0]:arr.join(''))
                }
                return typeof n=='string'?_es(n):n;
            },
            _f=function(xml){
                var o=null,t,tt;
                if(xml.nodeType==1 && ((t=xml.attributes).length||xml.firstChild)){
                    o={};
                    if(t.length){
                        for(var i=0,l=t.length;i<l;i++)
                            o["@"+t[i].nodeName]=(t[i].nodeValue||"")+"";
                    }
                    if(xml.firstChild){
                        var text=0, cdata=0, children=0, n;
                        for(n=xml.firstChild;n;n=n.nextSibling){
                            tt=n.nodeType;
                            if(tt==1)
                                children++;
                            else if(tt==3)
                                text++;
                            else if(tt==4)
                                cdata++;
                        }
                        if(children){
                            if(text<2 && cdata<2) {
                                for(n=xml.firstChild;n;n=n.nextSibling){
                                    if (n.nodeType==3)
                                        o["#text"]=_es(n.nodeValue);
                                    else if(n.nodeType==4)
                                        o["#cdata"]=_es(n.nodeValue);
                                    else if(o[tt=n.nodeName]){
                                        if(o[tt] instanceof Array)
                                            o[tt][o[tt].length]=_f(n);
                                        else
                                            o[tt]=[o[tt],_f(n)];
                                    }else
                                        o[tt]=_f(n);
                                }
                            }else {
                                if(!t.length)
                                    o=_xml(xml);
                                else
                                    o["#text"]= _xml(xml);
                            }
                        }else if(text){
                            if(!t.length) {
                                o=_xml(xml);
                            }else
                                o["#text"]=_xml(xml);
                        }else if(cdata) {
                            if(cdata>1)
                                o=_xml(xml);
                            else
                                for(n=xml.firstChild;n;n=n.nextSibling)
                                    o["#cdata"] = _es(n.nodeValue);
                        }
                    }
                }
                return o;
            };
            o[xmlObj.nodeName]=_f(_clear(xmlObj));
            return o;
        },
        parseXML:function(xmlText){
            var dom=null;
            if(typeof DOMParser=='undefined'){
                try{
                    dom=new ActiveXObject('Microsoft.XMLDOM');
                    dom.async=false;
                    dom.loadXML(xmlText||"");
                }catch(e){dom=null}
            }else{
                try{
                    var p=new DOMParser();
                    dom=p.parseFromString(xmlText||"", "text/xml");
                }catch(e){dom=null}finally{p=null}
            }
            return dom;
        }
    }
});ood.Class('ood.XMLRPC',null,{
    Static:{
        //wrapRequest(hash)
        // or wrapRequest(string, hash)
        wrapRequest:function(methodName,params){
            if(typeof methodName=="object"){
                params=methodName.params;
                methodName=methodName.methodName;
            }

            if(!methodName)return null;
            if(params && !params instanceof Array)return null;

            var ns=this,
                xml = ['<?xml version="1.0"?><methodCall><methodName>'+ methodName+'</methodName>'];
            if(params){
                xml.push('<params>');
                for(var i=0,j=params.length;i<j;i++)
                    xml.push('<param>'+ns._wrapParam(params[i])+'</param>');
                xml.push('</params>');
            }
            xml.push('</methodCall>');
            return xml.join('');
        },
        parseResponse:function(xmlObj){
            if(!xmlObj || !xmlObj.documentElement)return null;
            var doc=xmlObj.documentElement;
            if(doc.nodeName!='methodResponse')return null;
            var ns=this,
                json={},
                err,elem;
       
            elem = doc.getElementsByTagName('value')[0];
            if(elem.parentNode.nodeName=='param'&&elem.parentNode.parentNode.nodeName=='params'){
                json.result=ns._parseElem(elem);
            }
            else if(elem.parentNode.nodeName=='fault'){
                err=ns._parseElem(elem);
                json.error = {
                    code:err.faultCode,
                    message:err.faultString
                };
            }
            else return null;

            if(!json.result && !json.error)
                return null;
            return json;
        },
        _dateMatcher:/^(?:(\d\d\d\d)-(\d\d)(?:-(\d\d)(?:T(\d\d)(?::(\d\d)(?::(\d\d)(?:\.(\d+))?)?)?)?)?)$/,
        _parseElem:function(elem){
            var ns=this, 
                nodes=elem.childNodes,
                typeElem, dateElem, name, value, tmp;
            if(nodes.length==1&&nodes.item(0).nodeType==3)
                return nodes.item(0).nodeValue;

            for(var i=0,l=nodes.length;i<l;i++){
                if(nodes.item(i).nodeType==1){
                    typeElem=nodes.item(i);
                    switch(typeElem.nodeName.toLowerCase()){
                        case 'i4':
                        case 'int':
                            value=parseInt(typeElem.firstChild.nodeValue,10);
                            return isNaN(value)?null:value;
                        case 'double':
                            value=parseFloat(typeElem.firstChild.nodeValue);
                            return isNaN(value)?null:value;
                        case 'boolean':
                            return Boolean(parseInt(typeElem.firstChild.nodeValue,10)!==0);
                        case 'string':
                            return typeElem.firstChild?typeElem.firstChild.nodeValue:"";
                        case 'datetime.iso8601':
                            if(tmp=typeElem.firstChild.nodeValue.match(ns._dateMatcher)){
                                value = new Date;
                                if(tmp[1]) value.setUTCFullYear(parseInt(tmp[1],10));
                                if(tmp[2]) value.setUTCMonth(parseInt(tmp[2]-1,10));
                                if(tmp[3]) value.setUTCDate(parseInt(tmp[3],10));
                                if(tmp[4]) value.setUTCHours(parseInt(tmp[4],10));
                                if(tmp[5]) value.setUTCMinutes(parseInt(tmp[5],10));
                                if(tmp[6]) value.setUTCSeconds(parseInt(tmp[6],10));
                                if(tmp[7]) value.setUTCMilliseconds(parseInt(tmp[7],10));
                                return value;
                            }
                            return null;
                        case 'base64':
                            return null;
                        case 'nil':
                            return null;
                        case 'struct':
                            value = {};
                            for(var mElem,j=0;mElem=typeElem.childNodes.item(j);j++){
                                if(mElem.nodeType==1&&mElem.nodeName=='member'){
                                    name='';
                                    elem=null;
                                    for(var child,k=0;child=mElem.childNodes.item(k);k++){
                                        if(child.nodeType==1){
                                            if(child.nodeName=='name')
                                                name=child.firstChild.nodeValue;
                                            else if(child.nodeName=='value')
                                                elem = child;
                                        }
                                    }
                                    if(name&&elem)
                                       value[name] = ns._parseElem(elem);
                                }
                            }
                            return value;
                        case 'array':
                                value = [];
                                dateElem=typeElem.firstChild;
                                while(dateElem&&(dateElem.nodeType!=1||dateElem.nodeName!='data'))
                                    dateElem = dateElem.nextSibling;
                                if(!dateElem)
                                    return null;
                                elem=dateElem.firstChild;
                                while(elem){
                                    if(elem.nodeType==1)
                                        value.push(elem.nodeName=='value'?ns._parseElem(elem):null);
                                    elem=elem.nextSibling;
                                }
                                return value;
                        default:
                                return null;
                    }
                }
            }
            return null;
        },
        _map:{
            "<":"&lt;",
            ">":"&gt;",
            "&":"&amp;",
            '"':"&quot;",
            "'":"&apos;"
        },
        _date2utc:function(d){
            var ns=this,r=this._zeroPad;
            return d.getUTCFullYear()+'-'+
               r(d.getUTCMonth()+1)+'-'+
               r(d.getUTCDate())+'T'+
               r(d.getUTCHours())+':'+
               r(d.getUTCMinutes())+':'+
               r(d.getUTCSeconds())+'.'+
               r(d.getUTCMilliseconds(), 3);
        },
        _zeroPad:function(v,w){
            if(!w)w=2;
            v=((!v&&v!==0)?'':(''+v));
            while(v.length<w)v='0'+v;
            return v;
        },
        _wrapParam:function(value){
            var ns=this,
                map=ns._map,
                xml=['<value>'],sign;
            switch(typeof value){
                case 'number':
                    xml.push(!isFinite(value)?'<nil/>':
                        parseInt(value,10)===Math.ceil(value)?('<int>'+value+'</int>'):
                        ('<double>'+value+'</double>')
                    );
                    break;
                case 'boolean':
                    xml.push('<boolean>'+(value?'1':'0')+'</boolean>');
                    break;
                case 'string':
                    xml.push('<string>'+value.replace(/[<>&"']/g, function(a){return map[a]})+'</string>');
                    break;
                case 'undefined':
                    xml.push('<nil/>');
                case 'function':
                    xml.push('<string>'+(""+value).replace(/[<>&"']/g, function(a){return map[a]})+'</string>');
                case 'object':
                    sign=Object.prototype.toString.call(value);
                    if(value===null)
                        xml.push('<nil/>');
                    else if(sign==='[object Array]'){
                        xml.push('<array><data>');
                        for(var i=0,j=value.length;i<j;i++)
                            xml.push(ns._wrapParam(value[i]));
                        xml.push('</data></array>');
                    }
                    else if(sign==='[object Date]' && isFinite(+value)){
                        xml.push('<dateTime.iso8601>' + ns._date2utc(value) + '</dateTime.iso8601>');
                    }
                    else {
                        xml.push('<struct>');
                        for(var key in value)
                            if(value.hasOwnProperty(key))
                                xml.push('<member>'+'<name>'+key+'</name>'+ns._wrapParam(value[key])+'</member>');
                        xml.push('</struct>');
                    }
                    break;
            }
            xml.push('</value>');
            return xml.join('');
        }
    }
});ood.Class('ood.SOAP',null,{
    Static:{
        RESULT_NODE_NAME:"return",

        getNameSpace:function(wsdl){
            var ns=wsdl.documentElement.attributes["targetNamespace"];
            return ns===undefined?wsdl.documentElement.attributes.getNamedItem("targetNamespace").nodeValue:ns.value;
        },
        getWsdl:function(queryURL,onFail){
            var rst=false;

            // sync call for wsdl
            ood.Ajax(queryURL+'?wsdl',null,function(rspData){
                rst=rspData;
            },function(){
                ood.tryF(onFail,arguments,this);
            },null,{
                method:'GET',
                rspType:'xml',
                asy:false
            }).start();

            return rst;
        },
        wrapRequest:function(methodName, params, wsdl){
            if(typeof methodName=="object"){
                wsdl=params;
                params=methodName.params;
                methodName=methodName.methodName;
            }
            var ns=this, namespace=ns.getNameSpace(wsdl);
            //return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            return  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<soap:Body>" +
                    "<" + methodName + " xmlns=\""+namespace+"\">" +
                    ns._wrapParams(params) +
                    "</"+methodName+"></soap:Body></soap:Envelope>";
        },
        parseResponse:function(xmlObj, methodName, wsdl){
            if(typeof methodName=="object"){
                methodName=methodName.methodName;
            }
            var ns=this,
                hash={},
                nd=xmlObj.getElementsByTagName(methodName+"Result");
            if(!nd.length)
                nd=xmlObj.getElementsByTagName(ns.RESULT_NODE_NAME);
            if(!nd.length){
                hash.fault={
                    faultcode:xmlObj.getElementsByTagName("faultcode")[0].childNodes[0].nodeValue,
                    faultstring:xmlObj.getElementsByTagName("faultstring")[0].childNodes[0].nodeValue
                };
            }else{
                hash.result=ns._rsp2Obj(nd[0],wsdl);
            }
            return hash;
        },
        _rsp2Obj:function(xmlNode, wsdl){
            var ns=this,
                types=ns._getTypesFromWsdl(wsdl);
            return ns._node2obj(xmlNode,types);
        },
        _getTypesFromWsdl:function(wsdl){
            var types=[],
                ell,useNamedItem;

            ell=wsdl.getElementsByTagName("s:element");
            if(ell.length){
                useNamedItem=true;
            }else{
                ell=wsdl.getElementsByTagName("element");
                useNamedItem=false;
            }
            for(var i=0,l=ell.length;i<l;i++){
                if(useNamedItem){
                    if(ell[i].attributes.getNamedItem("name") != null && ell[i].attributes.getNamedItem("type") != null)
                        types[ell[i].attributes.getNamedItem("name").nodeValue] = ell[i].attributes.getNamedItem("type").nodeValue;
                }else{
                    if(ell[i].attributes["name"] != null && ell[i].attributes["type"] != null)
                        types[ell[i].attributes["name"].value] = ell[i].attributes["type"].value;
                }
            }
            return types;
        },
        _getTypeFromWsdl:function(elems, types){
            return types[elems]==undefined?"":types[elems];
        },
        _node2obj:function(xmlNode, types){
            if(xmlNode==null)return null;
            var ns=this,value,tmp;
            if(xmlNode.nodeType==3||xmlNode.nodeType==4){
                value=xmlNode.nodeValue;
                switch(ns._getTypeFromWsdl(xmlNode.parentNode.nodeName, types).toLowerCase()){
                    case "s:boolean":
                        return value+""=="true";
                    case "s:int":
                    case "s:long":
                        return value===null?0:parseInt(value+"", 10);
                    case "s:double":
                        return value===null?0:parseFloat(value+"");
                    case "s:datetime":
                        if(value == null)
                            return null;
                        else{
                            if(tmp=value.match(ns._dateMatcher)){
                                var d = new Date;
                                if(tmp[1]) d.setUTCFullYear(parseInt(tmp[1],10));
                                if(tmp[2]) d.setUTCMonth(parseInt(tmp[2]-1,10));
                                if(tmp[3]) d.setUTCDate(parseInt(tmp[3],10));
                                if(tmp[4]) d.setUTCHours(parseInt(tmp[4],10));
                                if(tmp[5]) d.setUTCMinutes(parseInt(tmp[5],10));
                                if(tmp[6]) d.setUTCSeconds(parseInt(tmp[6],10));
                                if(tmp[7]) d.setUTCMilliseconds(parseInt(tmp[7],10));
                                return d;
                            }
                            return null;
                        }
                    //case "s:string":
                    default:
                        return value===null?"":(value+"");
                }
            }else if(xmlNode.childNodes.length==1&&(xmlNode.childNodes[0].nodeType==3||xmlNode.childNodes[0].nodeType==4))
                return ns._node2obj(xmlNode.childNodes[0], types);
            else{
                if(ns._getTypeFromWsdl(xmlNode.nodeName, types).toLowerCase().indexOf("arrayof") == -1){
                    var obj=xmlNode.hasChildNodes()?{}:null;
                    for(var i=0,l=xmlNode.childNodes.length;i<l;i++)
                        obj[xmlNode.childNodes[i].nodeName]=ns._node2obj(xmlNode.childNodes[i], types);
                    return obj;
                }else{
                    var arr =[];
                    for(var i=0,l=xmlNode.childNodes.length;i<l;i++)
                        arr.push(ns._node2obj(xmlNode.childNodes[i], types));
                    return arr;
                }
            }
            return null;
        },
         _wrapParams:function(params){
            var ns=this,arr=[];
            for(var p in params){
                switch(typeof(params[p])){
                    case "string":
                    case "number":
                    case "boolean":
                    case "object":
                        arr.push("<" + p + ">" + ns._wrapParam(params[p]) + "</" + p + ">");
                        break;
                    default:
                        break;
                }

            }
            return arr.join('');
        },
        _map:{
            "<":"&lt;",
            ">":"&gt;",
            "&":"&amp;",
            '"':"&quot;",
            "'":"&apos;"
        },
        _wrapParam:function(param){
            var ns=this,
                s="",
                map=ns._map,
                sign,sign2,type,value;
            switch(typeof(param)){
                case "string":
                    s += param.replace(/[<>&"']/g, function(a){return map[a]});
                    break;
                case "number":
                case "boolean":
                    s += param+"";
                    break;
                case "object":
                    sign=Object.prototype.toString.call(param);
                    // Date
                    if(sign==='[object Date]' && isFinite(+param)){
                        s += ns._date2utc(param);
                    }else if(sign==='[object Array]'){
                        for(var p in param){
                            value=param[p];
                            switch(typeof value){
                                case 'number':
                                    type=parseInt(value,10)===Math.ceil(value)?'int':'double';
                                    break;
                                case 'boolean':
                                    type='bool';
                                    break;
                                case 'string':
                                    type='string';
                                    break;
                                case 'object':
                                    sign2=Object.prototype.toString.call(value);
                                    if(sign2==='[object Array]'){
                                        type="Array";
                                    }else if(sign2==='[object Date]' && isFinite(+value)){
                                        type="DateTime";
                                    }else
                                        type="object";
                                    break;
                            }
                            s += "<"+type+">"+ns._wrapParam(param[p])+"</"+type+">";
                        }
                    }else{
                        for(var p in param)
                            if(param.hasOwnProperty(p))
                                s += "<"+p+">"+ns._wrapParam(param[p])+"</"+p+">";
                    }
                    break;
            }
            return s;
        },
        _date2utc:function(d){
            var ns=this,r=this._zeroPad;
            return d.getUTCFullYear()+'-'+
               r(d.getUTCMonth()+1)+'-'+
               r(d.getUTCDate())+'T'+
               r(d.getUTCHours())+':'+
               r(d.getUTCMinutes())+':'+
               r(d.getUTCSeconds())+'.'+
               r(d.getUTCMilliseconds(), 3);
        },
        _zeroPad:function(v,w){
            if(!w)w=2;
            v=((!v&&v!==0)?'':(''+v));
            while(v.length<w)v='0'+v;
            return v;
        }
    }
});﻿ood.Class('ood.ModuleFactory', null, {
    Initialize: function () {

        var ns = this;

        ood.getModule = function (cls, onEnd, threadid, cached, properties, events) {
            return ns.getModule.apply(ns, arguments)
        };
        ood.newDom = function (cls, onEnd, threadid, properties, events) {
            return ns.newModule.apply(ns, arguments)
        };
        ood.newModule = function (cls, onEnd, threadid, properties, events) {
            return ns.newModule.apply(ns, arguments)
        };
        ood.showModule = function (cls, beforeShow, onEnd, threadid, cached, properties, events, parent, subId, left, top) {
            return ns.getModule(cls, function (err, module, threadid) {
                if (!err && false !== ood.tryF(beforeShow, [module, threadid], module)) {
                    this.show.apply(module, [onEnd, parent, subId, threadid, left, top]);
                } else {
                    ood.tryF(onEnd, [err, module, threadid], module);
                }
            }, threadid, cached, properties, events);
        };
        ood.execExpression = function (expression, params, isasy) {
            var result;
            if (!params) {
                params = {};
            }
            params.expression = expression;
            ood.Ajax('/LOCALESD/executeCMD', params, function (txt) {
                var obj = txt;
                if (obj && obj.requestStatus != -1) {
                    result = obj.data;
                } else {
                    ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                }

            }, null, null, {method: 'POST', asy: isasy ? true : false}).start();
            return result;
        }
        ood.execGrid = function (expression, params, isasy) {
            var result;
            if (!params) {
                params = {};
            }
            params.expression = expression;
            ood.Ajax('/LOCALESD/executeGridCMD', params, function (txt) {
                var obj = txt;
                if (obj && obj.requestStatus != -1) {
                    result = obj.data;
                } else {
                    ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                }

            }, null, null, {method: 'POST', asy: isasy ? true : false}).start();
            return result;
        };

        ood.downLoad = function (path) {
            ood.create('ood.UI.Div')
                .setLeft('0')
                .setTop('0')
                .setWidth('0').setHeight('0')
                .setIframeAutoLoad('/vfs/download?path=' + path)
                .show()
        };

        ood.showModule3 = function (cls, target, childname, callback) {
            if (!cls) {
                return;
            }
            if (childname == '') {
                childname = undefined;
            }
            try {
                if (!ood.isStr(target)) {
                    target = target.getAlias();
                }
                var arr = ood.getObjectByAlias(target);
                currpanel = arr[arr.length - 1];
                var domId = currpanel.getDomId();
                ood.each(arr, function (ccurrpanel) {
                    if (ccurrpanel.getDomId() != domId) {
                        try {
                            ccurrpanel.boxing().destroy();
                        } catch (e) {
                            console.log(e);
                        }

                    } else {
                        if (childname && childname != '') {
                            var childs = ccurrpanel.boxing().getPanelChildren();
                            ood.each(childs, function (child) {
                                    if (child[1] == childname) {
                                        var box = child[0].boxing().destroy();
                                    }
                                }
                            )
                        }
                    }
                });

            } catch (e) {
                console.log(e);
            }

            var onEnd = function (url, remoteclass, test) {
                var clazz = ood.ModuleFactory.getModule(remoteclass, function () {
                    this.show(callback, currpanel, childname, true)
                })
            }
            var onFail = function (msg) {
                ood.alert(msg)
            }
            ood.fetchClass(cls, onEnd, onFail, null, false)
        };


        ood.showModule2 = function (cls, target, childname, params, data, parentModule) {
            if (!cls) {
                return;
            }
            if (childname == '') {
                childname = undefined;
            }
            try {
                var currpanel;
                if (target) {
                    if (!ood.isStr(target)) {
                        target = target.getAlias();
                    }
                    if (parentModule) {
                        currpanel = ood.get(parentModule, [target]) || ood.get(parentModule, ["host", target]);
                    }

                    if (!currpanel) {
                        var arr = ood.getObjectByAlias(target);
                        if (arr) {
                            if (ood.isArr(arr) && arr.length > 0) {
                                currpanel = arr[arr.length - 1];
                            } else {
                                currpanel = arr;
                            }
                        }
                    }

                    if (currpanel) {
                        if (childname) {
                            var childs = currpanel.children || currpanel.get(0).children;
                            ood.each(childs, function (child) {
                                    if (child[1] == childname) {
                                        var box = child[0].boxing().destroy(true, true, true);
                                    }
                                }
                            )
                        } else {
                            currpanel.boxing().destroy(true, true, true);
                        }
                    }
                }

            } catch (e) {
                console.log(e);
            }


            var onEnd = function (url, remoteclass, test) {
                var endFun = function () {
                    var allData = {};
                    if (data && ood.isHash(data)) {
                        ood.merge(allData, data);
                    }
                    try {
                        if (params && ood.isHash(params)) {
                            var urlParams = ood.getUrlParams(url);
                            if (urlParams) {
                                ood.merge(params, urlParams, 'all');
                            }
                            ood.each(this._ctrlpool, function (prf) {
                                var ins = prf.boxing();
                                if (ins && ins.updateTagVar) {
                                    ins.updateTagVar(params);
                                }
                            });

                            ood.each(allData, function (prf) {
                                if (prf && prf.name && params[prf.name]) {
                                    prf.value = params[prf.name];
                                }
                            })
                            ood.merge(allData, params, 'all');
                        }

                        this.setData(allData)

                    } catch (e) {
                        console.log(e)
                    }

                    this.setProperties("target", target)
                    this.setProperties("childname", childname)
                    this.setName(childname);
                    this.initData();
                    // this._fireEvent('afterShow');
                };


                var clazz = ood.ModuleFactory.getModule(remoteclass, function () {
                    if (parentModule) {
                        this.setParentModule(parentModule);
                        if (parentModule.addChildModule) {
                            parentModule.addChildModule(this);
                        } else {
                            parentModule[this.alias] = this;
                        }
                    }
                    this.show(endFun, currpanel, childname, true)
                }, false, false)

            }
            var onFail = function (msg) {
                ood.alert(msg)
            }
            ood.fetchClass(cls, onEnd, onFail, null, false)
        };


        ood._addChildModule = function (citem, child, host, $host) {

            var cchild;
            if (citem.key == 'ood.Module' && citem.className) {
                cchild = ood.create(citem.className, citem.key)
                    .setHost(host, citem.alias)
                    .setAlias(citem.alias)
                    .setEvents(ood.checkEvents(citem.events))
                    .setProperties(citem.properties)
                if ($host) {
                    $host[citem.alias] = cchild
                }
                if (citem.CS) {
                    cchild.setCustomStyle(citem.CS)
                }


                if (child.append) {
                    var target = citem.target || citem.tagter;
                    if (target) {
                        child.append(cchild, target);
                    } else {
                        child.append(cchild);
                    }
                }

                // try {
                //     cchild.setData(host.getModule().getData());
                //     cchild.initData();
                // } catch (e) {
                // }

            }
        };

        ood.addChild = function (item, child, host, $host) {
            var children = item.children || item.sub, apis = [], customComponents = [], components = [], modules = [],
                ns = this;
            host.modules = [];

            ood.each(children, function (citem) {
                if (citem.key == 'ood.APICaller') {
                    var cchild = ood.create(citem.key)
                        .setHost($host, citem.alias)
                        .setAlias(citem.alias)
                        .setEvents(ood.checkEvents(citem.events))
                        .setProperties(citem.properties)
                    if ($host) {
                        $host[citem.alias] = cchild
                    }
                    ;
                    apis.push(cchild)
                } else if (ood.str.startWith(citem.key, 'ood.UI.')) {
                    customComponents.push(citem);
                } else if (ood.str.startWith(citem.key, 'ood.Module')) {
                    var moduleConfig = {
                        item: citem,
                        target: citem.target,
                        parent: host
                    }
                    host.modules.push(moduleConfig)
                } else {
                    if (!citem.alias) {
                        citem.alias = ood.id()
                    }
                    var cchild = ood.create(citem.key)
                        .setHost($host, citem.alias)
                        .setAlias(citem.alias)
                        .setEvents(ood.checkEvents(citem.events))
                        .setProperties(citem.properties)
                    if ($host) {
                        $host[citem.alias] = cchild
                    }
                    customComponents.push(citem);
                }
            });

            components = ood._addChild(customComponents, child, host, $host);
            ood.each(apis, function (api) {
                if (api.getAutoRun()) {
                    api.invoke();
                }
            })

            ood.each(host.modules, function (moduleConfig) {
                ood._addChildModule(moduleConfig.item, moduleConfig.parent, host, $host);
            });

            return components;
        };
        ood._addChild = function (children, child, host, $host) {
            var components = [], modules = [];
            if (children) {
                ood.each(children, function (citem) {
                    if (citem && citem.alias != 'PAGECTX') {
                        var cchild;
                        if (citem.key == 'ood.Module' && citem.className) {
                            var module = {
                                item: citem,
                                parent: child
                            }
                            host.modules.push(module);
                        } else {
                            cchild = ood.create(citem.key)
                                .setHost(host, citem.alias)
                                .setAlias(citem.alias)
                                .setEvents(ood.checkEvents(citem.events))
                                .setProperties(citem.properties || citem.iniProp)
                            if ($host) {
                                $host[citem.alias] = cchild
                            }
                            ;


                            if (citem.CS) {
                                cchild.setCustomStyle(citem.CS)
                            }
                            if (child.append) {
                                var target = citem.target || citem.tagter;
                                if (target) {
                                    child.append(cchild, target);
                                } else {
                                    child.append(cchild);
                                }
                            }
                            components.push(cchild);
                            var children = citem.children || citem.sub;
                            if (children) {
                                ood._addChild(children, cchild, host, $host);
                            }
                        }
                    }
                })
            }
            return components;
        };


        ood.addRootComponent = function (properties, host) {
            var child = ood.create(item.key)
                .setHost(host, item.alias)
                .setAlias(item.alias)
                .setEvents(ood.checkEvents(item.events))
                .setProperties(item.properties);


        };
        //检查模块函数转换
        ood.checkEvents = function (events) {
            var nEvent = {};
            try {

                if (events && ood.isHash(events)) {
                    ood.each(events, function (event, eventKey) {

                        if (event.actions && ood.isArr(event.actions)) {
                            var arrEvent = [];

                            ood.each(event.actions, function (action) {
                                if (action) {
                                    if (action.script) {
                                        arrEvent.push(action.script)
                                    } else {
                                        arrEvent.push(action)
                                    }
                                }
                            });
                            nEvent[eventKey] = event;
                            nEvent[eventKey].actions = arrEvent;
                        } else {
                            nEvent[eventKey] = event;
                        }
                    })
                }

            } catch (e) {
                console.warn(e);
            }

            return nEvent;
        };


        //检查模块函数转换
        ood.checkFunction = function (functions) {
            var nfunctions = [];
            ood.each(functions, function (modulefunction) {
                if (modulefunction.actions) {
                    var iactions = [];
                    ood.each(modulefunction.actions, function (action) {

                        if (action) {
                            if (action.script) {
                                iactions.push(action.script)
                            } else {
                                iactions.push(action)
                            }
                        }

                    });
                    modulefunction.actions = iactions;
                }
                nfunctions.push(modulefunction);
            })

            return nfunctions;
        };


        ood.openDebugWin = function (url, projectName) {
            if (url && projectName) {
                ood.execExpression('$ESD.open()', {url: url, projectName: projectName}, false);
            }
        };
        ood.openOtherWin = function (url, projectName) {
            if (url) {
                ood.execExpression('$ESD.openOtherWin()', {url: url, projectName: projectName}, false);
            }
        };


        ood.intModuleProperties = function (properties, host) {
            var children = [];
            append = function (child) {
                children.push(child.get(0));
            };
            ood.each(properties, function (item) {
                var child;
                if (item.key == 'ood.Module' && item.className) {
                    child = ood.create(item.className, item.key)
                        .setHost(host, item.alias)
                        .setAlias(item.alias)
                        .setEvents(ood.checkEvents(item.events))
                        .setProperties(item.properties)
                } else {
                    child = ood.create(item.key)
                        .setHost(host, item.alias)
                        .setAlias(item.alias)
                        .setEvents(ood.checkEvents(item.events))
                        .setProperties(item.properties)
                }

                if (item.CS) {
                    child.setCustomStyle(item.CS)
                }
                ;
                ood.addChild(item, child, host);
                children.push(child.get(0));
                //children.push(child);
            });
            return children;
        };


        //compitable
        ood.getCom = ood.getModule;
        ood.showCom = ood.showModule;

        ns.setCom = ns.setModule;
        ns.getComFromCache = ns.getModuleFromCache;
        ns.getCom = ns.getModule;
        ns.newCom = ns.newModule;
        ns.storeCom = ns.storeModule;
        ns.prepareComs = ns.prepareModules;

        ood.ComFactory = ns;
    },
    Static: {
        _pro: {},
        _cache: {},
        _domId: 'ood:ModuleFactory:',
        getProfile: function (key) {
            return key ? this._pro[key] : this._pro;
        },
        setProfile: function (key, value) {
            if (typeof key == 'string')
                this._pro[key] = value;
            else if (ood.isHash(key))
                this._pro = key;
            return this;
        },
        destroyAll: function () {
            ood.each(this._cache, function (o) {
                ood.tryF(o.destroy, [], o);
            });
            this._cache = {};
        },
        broadcast: function (fun) {
            if (typeof fun == 'function') {
                var i, c = this._cache;
                for (i in c)
                    fun.call(c[i], i);
            }
        },

        setModule: function (id, obj) {
            this._cache[id] = obj;
            if (obj) obj.moduleRefId = id;
            return this;
        },
        getModuleFromCache: function (id) {
            return this._cache[id] || null;
        },
        //cached:false->don't get it from cache, and don't cache the result.
        getModule: function (id, onEnd, threadid, cached, properties, events) {
            if (!id) {
                var e = new Error("No id");
                ood.tryF(onEnd, [e, null, threadid]);
                ood.Thread.abort(threadid);
                throw e;
                return;
            }
            cached = cached !== false;
            var c = this._cache,
                p = this._pro,
                config,
                clsPath;

            if (cached && c[id] && !c[id].destroyed) {
                ood.tryF(onEnd, [null, c[id], threadid], c[id]);
                return c[id];
            } else {
                // if no configure
                if (!(config = p[id])) {
                    config = {
                        cls: id,
                        cached: cached,
                        properties: properties,
                        events: events
                    };
                    clsPath = id;
                } else
                    clsPath = config.cls || config;

                var self = arguments.callee,
                    me = this,
                    task = function (cls, config, threadid) {
                        if (!ood.isFun(cls))
                            throw "'" + clsPath + "' is not a constructor";
                        var o = new cls();

                        if (config.properties)
                            ood.merge(o.properties, config.properties, 'all');
                        if (config.events)
                            ood.merge(o.events, config.events, 'all');
                        if (config.cached !== false)
                            ood.ModuleFactory.setModule(id, o);

                        var args = [function (err, module, threadid) {
                            var arr = module.getUIComponents().get(),
                                fun = function (arr, subcfg, firstlayer) {
                                    var self1 = arguments.callee;
                                    ood.arr.each(arr, function (v, i) {
                                        if (v.children) {
                                            var a = [];
                                            ood.arr.each(v.children, function (o) {
                                                a[a.length] = o[0];
                                            });
                                            self1(a, subcfg);
                                        }
                                    });
                                };
                            //handle tag sub from module
                            fun(arr, config.children, 1);
                        }];
                        args.push(threadid || null);

                        //insert first
                        if (onEnd)
                            ood.Thread.insert(threadid, {
                                task: onEnd,
                                args: [null, o, threadid],
                                scope: o
                            });
                        //latter
                        ood.tryF(o[config.iniMethod || 'create'], args, o);
                    };
                ood.Thread.observableRun(function (threadid) {
                    var f = function (threadid) {
                        // this for js path doesn't match Class name space
                        var cls = this || ood.SC.get(clsPath);
                        // it must be a ood Class
                        if (cls && cls.$ood$) {
                            ood.Thread.insert(threadid, {
                                task: task,
                                args: [cls, config, threadid]
                            });
                        } else {
                            var e = new Error("Cant find Class '" + clsPath + "' in the corresponding file (maybe SyntaxError)");
                            ood.tryF(onEnd, [e, null, threadid]);
                            ood.Thread.abort(threadid);
                            throw e;
                        }
                    };
                    ood.SC(clsPath, function (path) {
                        if (path)
                            f.call(this, threadid);
                        else {
                            var e = new Error("No class name");
                            ood.tryF(onEnd, [e, null, threadid]);
                            ood.Thread.abort(threadid);
                            throw e;
                        }
                    }, true, threadid, {
                        retry: 0,
                        onFail: function (e) {
                            ood.tryF(onEnd, [e, null, threadid]);
                        }
                    });
                }, null, threadid);
            }
        },
        newModule: function (cls, onEnd, threadid, properties, events) {
            return this.getModule(cls, onEnd, threadid, false, properties, events);
        },
        storeModule: function (id) {
            var m, t, c = this._cache, domId = this._domId;
            if (t = c[id]) {
                if (!(m = ood.Dom.byId(domId)))
                //using display:none here for performance, when appendchild, it'll not trigger layout etc.
                    ood('body').prepend(ood.create('<div id="' + domId + '" style="display:none;"></div>'));
                m = ood(domId);
                t = t.getUIComponents();
                if (!t.isEmpty()) {
                    //detach
                    t.get(0).unlinkParent();
                    //move to hide
                    m.append(t);
                }
            }
        },
        prepareModules: function (arr) {
            var self = this, funs = [];
            ood.arr.each(arr, function (i) {
                funs.push(function () {
                    self.getModule(i);
                });
            });
            ood.Thread(null, funs, 50).start();
            return this;
        }
    }
});/**
 * Debugger - 调试工具模块
 * 
 * 功能：
 * - 提供错误捕获和日志记录功能
 * - 支持函数调用栈追踪
 * - 提供调试日志的显示和管理
 * - 支持调试信息的格式化输出
 * - 提供调试面板的创建和管理
 * 
 * 主要方法：
 * - err: 捕获和记录错误信息
 * - trace: 追踪函数调用栈
 * - log: 记录调试日志
 * - warn: 记录警告信息
 * - info: 记录信息日志
 * - debug: 记录调试信息
 */
ood.Class('ood.Debugger', null, {
    Static:{
        $time:ood.stamp(),
        _id1:'ood:dbg::_frm',
        _id4:'ood:dbg::_head',
        _id2:'ood:dbg::_con',
        _id3:'ood:dbg::_inp',
        
        /**
         * 捕获和记录错误信息
         * @param {string} sMsg - 错误信息
         * @param {string} sUrl - 错误发生的文件URL
         * @param {number} sLine - 错误发生的行号
         * @returns {boolean} 是否继续传播错误
         */
        err:function(sMsg,sUrl,sLine){
            if(ood.browser.gek && sMsg=='Error loading script')
                return true;
            ood.Debugger.log('>>' + sMsg+' at File: '+ sUrl + ' ( line ' + sLine + ' ).');
            return true;
        },
        trace:function(obj){
            var args=ood.toArr(arguments),
                fun=args[1]||arguments.callee.caller,
                arr=args[2]||[];
            if(fun){
                arr.push('function "' + (fun.$name$||'') + '" in Class "' + (fun.$original$||'') +'"');
                if(fun.caller){
                    try{
                        arguments.callee(null,fun.caller,arr,1);
                    }catch(e){}
                }
            }
            if(!args[3]){
                var a=[];
                a.push(' >> Object Info:');
                if(typeof obj == 'object')
                    for(var i in obj)
                        a.push(' -- ' + i + " : " + obj[i]);
                else
                    a.push(obj);
                a.push(' >> Function Trace: ' + arr.join(' <= '));
                ood.Debugger.log.apply(ood.Debugger,a);
            }
            fun=null;
        },
        log:function(){
            var t1,t2,time,self=this,arr=ood.toArr(arguments),str;
            if(!arr.length)return;
            t1 = document.createElement("div");
            t2 = document.createElement("div");
            t2.className='ood-uibase ood-dbg-con1';
            time=ood.stamp();
            // if (hastime){
            //     t2.appendChild(document.createTextNode('Time stamp : '+time +'('+(time-self.$time)+')' ));
            // }


            self.$time=time;
            t1.appendChild(t2);
            for(var i=0,l=arr.length;i<l;i++){
                str=arr[i];
                t2 = document.createElement("div");
                t2.className='ood-uibase ood-dbg-con2';
                t2.appendChild(document.createTextNode(" "+ood.stringify(ood.isArguments(str)?ood.toArr(str):str)));
                t1.appendChild(t2);
            }

            if(!ood.Dom.byId(self._id2)){
                var ns=ood.create('<div id='+self._id1+' style="left:5px;top:'+(ood.win.scrollTop()+5)+'px;" class="ood-ui-reset ood-node ood-node-div ood-wrapper ood-dbg-frm ood-custom"><div class="ood-node ood-node-div ood-dbg-box ood-custom"><div id='+self._id4+' class="ood-node ood-node-div ood-uibar ood-dbg-header ood-custom">&nbsp;&nbsp;调试窗口 <span class="ood-node ood-node-span ood-dbg-cmds ood-custom"><a class="ood-node ood-node-a ood-custom" href="javascript:;" onclick="ood(\''+self._id2+'\').empty();">Clear</a><a class="ood-node ood-node-a ood-custom" href="javascript:;" onclick="ood(\''+self._id1+'\').remove();">✖</a></span></div><div id='+self._id2+' class="ood-node ood-node-div ood-uibase ood-dbg-content ood-custom"></div><div class="ood-node ood-node-div ood-uibase ood-dbg-tail ood-custom"><table class="ood-node ood-node-table ood-custom"><tr><td style="font-family:serif;">&nbsp;>>>&nbsp;</td><td style="width:100%"><input class="ood-node ood-node-input ood-custom" id='+self._id3+' /></td></tr></table></div></div></div>');
                ood('body').append(ns);
                self.$con=ood(self._id2);
                ood(self._id4).draggable(true,null,null,null,ood(self._id4).parent(2));

                if(ood.Dom.css3Support("boxShadow")){
                    ns.css("boxShadow","2px 2px 5px #ababab");
                }

                if(ood.browser.ie6){
                    ns.height(ns.offsetHeight());
                    ns.width(399);
                    ood.asyRun(function(){ns.width(400);})
                }
                var bak='',temp;
                ood(self._id3).onKeydown(function(p,e,s){
                    var k=ood.Event.getKey(e).key;
                    s=ood.use(s).get(0);
                    if(k=='enter'){
                        switch(s.value){
                            case '?':
                            case 'help':
                                self.$con.append(ood.create("<div class='ood-node ood-node-div ood-uibase ood-dbg-con3 ood-custom'><p class='ood-node ood-node-p ood-custom'><strong  class='ood-node ood-node-strong ood-custom'>vailable commands:</strong></p><ul  class='ood-node ood-node-ul ood-custom'><li  class='ood-node ood-node-li ood-custom'> -- <strong  class='ood-node ood-node-strong ood-custom'>[clr]</strong> or <strong>[clear]</strong> : clears the message</li><li  class='ood-node ood-node-li ood-custom'> -- <strong  class='ood-node ood-node-strong ood-custom'>[?]</strong> or <strong  class='ood-node ood-node-strong ood-custom'>[help]</strong> : shows this message</li><li  class='ood-node ood-node-li ood-custom'> -- <strong class='ood-node ood-node-strong ood-custom'>any other</strong>: shows its string representation</li></ul></div>"));
                                break;
                            case 'clr':
                            case 'clear':
                                ood(self._id2).empty();
                                break;
                            default:
                                try{
                                    temp=s.value;
                                    if(/^\s*\x7b/.test(temp))temp='('+temp+')';
                                    self.log(eval(temp));
                                }catch(e){self.$con.append(ood.create("<div  class='ood-node ood-node-div ood-uibase ood-dbg-con4 ood-custom'>"+String(e)+"</div>"));return;}
                        }
                        bak=s.value;
                        s.value='';
                    }else if(k=='up'||k=='down'){
                        var a=s.value;
                        s.value=bak||'';
                        bak=a;
                    }
                    k=s=temp=bak=null;
                });
            }
            self.$con.append(t1).scrollTop(self.$con.scrollHeight());
            t1=t2=null;
        }
    },
    Initialize:function(){
        ood.CSS.addStyleSheet(
            '.ood-dbg-frm{position:absolute;width:25em;z-index:2000;}'+
            '.ood-dbg-header{cursor:move;height:1.5em;padding-top:.25em;position:relative;border-bottom:solid 1px #CCC;background-color:#FFAB3F;font-weight:bold;}'+
            '.ood-dbg-cmds{position:absolute;right:.25em;top:.25em;}'+
            '.ood-dbg-cmds a{margin:.25em;}'+
            '.ood-dbg-box{position:relative;overflow:hidden;border:solid 1px #AAA;}'+
            '.ood-dbg-content{position:relative;width:100%;overflow:auto;height:25em;overflow-x:hidden;}'+
            '.ood-dbg-con1{background-color:#CCC;width:24.5empx;}'+
            '.ood-dbg-con2{padding-left:.5em;border-bottom:dashed 1px #CCC;width:24.5em;}'+
            '.ood-dbg-con3{padding-left:.5em;border-bottom:dashed 1px #CCC;background:#EEE;color:#0000ff;width:24.5em;}'+
            '.ood-dbg-con4{padding-left:.5em;border-bottom:dashed 1px #CCC;background:#EEE;color:#ff0000;width:24.5em;}'+
            '.ood-dbg-tail{overflow:hidden;position:relative;border-top:solid 1px #CCC;height:1.3333333333333333em;background:#fff;color:#0000ff;}'+
            '.ood-dbg-tail input{width:100%;border:0;background:transparent;}'
        ,this.KEY);
        //fix ie6:

        //shorcut
        ood.echo = function(){
            if(!ood.debugMode)return false;
            ood.Debugger.log.apply(ood.Debugger,ood.toArr(arguments));
        };
        ood.message = function(body, head, width, duration){
           width = width || 300;
           if(ood.browser.ie)width=width+(width%2);
           var div, h, me=arguments.callee,
           stack=me.stack||(me.stack=[]),
           allmsg=me.allmsg||(me.allmsg=[]),
           t=ood.win, left = t.scrollLeft() + t.width()/2 - width/2, height=t.height(), st=t.scrollTop();

           div=stack.pop();
           while(div&&!div.get(0))
                div=stack.pop();

           if(!div){
               div =
               '<div class="ood-ui-reset ood-node ood-node-div ood-wrapper ood-uibar ood-uiborder-outset ood-custom" style="border:solid 1px #cdcdcd;position:absolute;overflow:visible;top:-50px;">' +
                   '<div class="ood-node ood-node-div ood-custom" style="font-size:1.25em;overflow:hidden;font-weight:bold;padding:.25em;"></div>'+
                   '<div class="ood-node ood-node-div ood-custom" style="font-size:1em;padding:.5em;overflow:hidden;"></div>'+
               '</div>';
               div = ood.create(div);
               if(div.addBorder)div.addBorder();
               allmsg.push(div);
               if(ood.Dom.css3Support("boxShadow")){
                    div.css("boxShadow","2px 2px 5px #ababab");
               }
            }
            if(document.body.lastChild!=div.get(0))
                ood('body').append(div,false,true);

            div.topZindex(true);

            div.__hide=0;

            div.css({left:left+'px', width:width+'px', visibility:'visible'})
            .first().html(head||'').css('display',head?'':'none')
            .next().html(body||'');

            if(ood.browser.ie && ood.browser.ver<=8)
                div.ieRemedy();

            if(me.last && me.last.get(0) && div!=me.last){
                var last=me.last;
                var l=last.left();
                if(last._thread&&last._thread.id&&last._thread.isAlive())last._thread.abort();
                last._thread=last.animate({left:[l,l+(last.width+width)/2+20]},function(){
                    last.left(l);
                },function(){
                    last.left(l+(last.width+width)/2+20);
                }).start();
                
                var lh=last.offsetHeight();
               ood.filter(allmsg,function(ind){
                    if(ind.isEmpty())
                        return false;
                   if(!ind.__hide && ind!=div && ind!=last){
                       if(ind._thread.id&&ind._thread.isAlive())
                            ind._thread.abort();
                       ind.topBy(lh);
                    }
               });

            }
            me.last = div;
            me.last.width = width;

            //height() is ok
            h = div.height();

            if(ood.browser.ie6)div.cssSize({ height :h, width :width+2});

            if(div._thread&&div._thread.id&&div._thread.isAlive())div._thread.abort();
            div._thread=div.animate({top:[st-h-20,st+20]},function(){
                div.top(st-h-20);
            },function(){
                div.top(st+20);
            },300,0,'expoOut').start();

            ood.asyRun(function(){
                if(div._thread&&div._thread.id&&div._thread.isAlive())div._thread.abort();
                div._thread=div.animate({top:[div.top(), height+20]},null,function(){
                     stack.push(div); 
                     div.hide();
                     div.__hide=1;
                },300,0).start();
            }, duration||5000);
            me=null;
        };

        if(ood.isDefined(window.console) && (typeof window.console.log=="function")){
            ood.log=function(){window.console.log.apply(window.console,ood.toArr(arguments));};
        }else if(ood.debugMode){
            ood.log=ood.echo;
            window.onerror=this.err;
        }
    }
});/**
 * Date - 日期处理模块
 * 
 * 功能：
 * - 提供日期和时间的格式化功能
 * - 支持多种日期格式（ISO、UTC等）
 * - 提供日期计算和转换功能
 * - 支持时间单位的转换和计算
 * - 提供日期比较和差值计算
 * - 支持本地化日期显示
 * 
 * 主要方法：
 * - format: 格式化日期
 * - parse: 解析日期字符串
 * - add: 添加时间到日期
 * - diff: 计算日期差值
 * - getWeek: 获取周数
 * - isValid: 检查日期是否有效
 */
ood.Class('ood.Date',null,{
    Initialize:function(){
        var self=this;
        self._mapKeys(self.$TIMEUNIT);
        var a=self._key1,b=self._key2,u=self.$UNIT={};
        for(var i=0,l=a.length;i<l;i++)u[a[i]]=1;
        for(var i=0,l=b.length;i<l;i++)u[b[i]]=1;
        u.w=1;
    },
    Static:{
        _key1:'MILLISECOND,SECOND,MINUTE,HOUR,DAY,WEEK,MONTH,QUARTER,YEAR,DECADE,CENTURY'.split(','),
        _key2:'ms,s,n,h,d,ww,m,q,y,de,c'.split(','),
        // Conversion factors
        $TIMEUNIT : {
            MILLISECOND : 1,
            SECOND      : 1000,           //SECONDS
            MINUTE      : 60000,          //MINUTES 60 * 1000
            HOUR        : 3600000,        //HOURS 60 * 60 * 1000
            DAY         : 86400000,       //DAYS 24 * 60 * 60 * 1000
            WEEK        : 604800000,      //WEEKS 7 * 24 * 60 * 60 * 1000
            MONTH       : 2592000000,     //MONTHS 30 * 24 * 60 * 60 * 1000  (approx = 1 month)
            QUARTER     : 7776000000,     //QUARTERS 90 * 24 * 60 * 60 * 1000  (approx = 3 months)
            YEAR        : 31557600000,    //YEARS 365 * 24 * 60 * 60 * 1000 (approx = 1 year)
            DECADE      : 315576000000,   //DECADES 10 * 365 * 24 * 60 * 60 * 1000 (approx = 1 decade)
            CENTURY     : 3155760000000   //CENTURIES 100 * 365 * 24 * 60 * 60 * 1000 (approx = 1 century)
        },
        $TEXTFORMAT:{
            utciso:function(d,w,f){f=ood.Date._fix; return d.getUTCFullYear() + '-' +f(d.getUTCMonth() + 1) + '-' +f(d.getUTCDate()) + 'T' +f(d.getUTCHours()) + ':' +f(d.getUTCMinutes()) + ':' +f(d.getUTCSeconds()) + 'Z'},
            iso:function(d,w,f){f=ood.Date._fix; return d.getFullYear() + '-' +f(d.getMonth() + 1) + '-' +f(d.getDate()) + 'T' +f(d.getHours()) + ':' +f(d.getMinutes()) + ':' +f(d.getSeconds())},
            ms:function(d,w){return ood.Date._fix(d.getMilliseconds(),3)+ (w?"":ood.wrapRes('date.MS'))},
            s:function(d,w){return d.getSeconds()+ (w?"":ood.wrapRes('date.S'))},
            ss:function(d,w){return ood.Date._fix(d.getSeconds())+ (w?"":ood.wrapRes('date.S'))},
            n:function(d,w){return d.getMinutes()+ (w?"":ood.wrapRes('date.N'))},
            nn:function(d,w){return ood.Date._fix(d.getMinutes())+ (w?"":ood.wrapRes('date.N'))},
            h :function(d,w){return d.getHours()+ (w?"":ood.wrapRes('date.H'))},
            hh :function(d,w){return ood.Date._fix(d.getHours())+ (w?"":ood.wrapRes('date.H'))},
            d:function(d,w){return d.getDate()+ (w?"":ood.wrapRes('date.D'))},
            dd:function(d,w){return ood.Date._fix(d.getDate())+ (w?"":ood.wrapRes('date.D'))},
            w : function(d,w,firstDayOfWeek){var a=(d.getDay() - firstDayOfWeek +7)%7; return w?a:ood.wrapRes('date.WEEKS.'+a)},
            ww : function(d,w,firstDayOfWeek){return ood.Date.getWeek(d, firstDayOfWeek) + (w?"":ood.wrapRes('date.W'))},
            m:function(d,w){return (d.getMonth()+1) + (w?"":ood.wrapRes('date.M'))},
            mm:function(d,w){return ood.Date._fix(d.getMonth()+1) + (w?"":ood.wrapRes('date.M'))},
            q : function(d,w){return (parseInt((d.getMonth()+3)/3-1,10) + 1) + (w?"":ood.wrapRes('date.Q'))},
            y :function(d,w){return d.getYear() + (w?"":ood.wrapRes('date.Y'))},
            yyyy :function(d,w){return d.getFullYear() + (w?"":ood.wrapRes('date.Y'))},
            de:function(d,w){return parseInt(d.getFullYear()/10,10) + (w?"":ood.wrapRes('date.DE'))},
            c:function(d,w){return parseInt(d.getFullYear()/100,10) + (w?"":ood.wrapRes('date.C'))},

            hn:function(d,w){return ood.wrapRes('date.HN-'+d.getHours()+"-"+d.getMinutes())},
            dhn:function(d,w){return ood.wrapRes('date.DHN-'+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes())},
            mdhn:function(d,w){return ood.wrapRes('date.MDHN-'+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes())},
            hns:function(d,w){return ood.wrapRes('date.HNS-'+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds())},
            hnsms:function(d,w){return ood.wrapRes('date.HNSMS-'+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds()+"-"+d.getMilliseconds())},

            yq:function(d,w){return ood.wrapRes('date.YQ-'+d.getFullYear()+"-"+(parseInt((d.getMonth()+3)/3-1,10)+1))},

            ym :   function(d,w){return ood.wrapRes('date.YM-'+d.getFullYear()+"-"+(d.getMonth()+1))},
            md :  function(d,w){return ood.wrapRes('date.MD-'+(d.getMonth()+1)+"-"+d.getDate())},
            ymd :  function(d,w){return ood.wrapRes('date.YMD-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate())},
            ymd2 :  function(d,w){return ood.wrapRes('date.YMD2-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate())},
            ymdh:  function(d,w){return ood.wrapRes('date.YMDH-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours())},
            ymdhn: function(d,w){return ood.wrapRes('date.YMDHN-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes())},
            ymdhns:function(d,w){return ood.wrapRes('date.YMDHNS-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds())},
            'all' :  function(d,w){return ood.wrapRes('date.ALL-'+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+"-"+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds()+"-"+d.getMilliseconds())}
        },
        $TIMEZONE:[{
            id:"Asia(East,North)",
            sub:[{
                    id:"Brunei",
                    v:"+0800"
                },{
                    id:"Burma",
                    v:"+0630"
                },{
                    id:"Cambodia",
                    v:"+0700"
                },{
                    id:"China",
                    v:"+0800"
                },{
                    id:"China(HK,Macau)",
                    v:"+0800"
                },{
                    id:"China(TaiWan)",
                    v:"+0800"
                },{
                    id:"China(Urumchi)",
                    v:"+0700"
                },{
                    id:"East Timor",
                    v:"+0800"
                },{
                    id:"Indonesia",
                    v:"+0700"
                },{
                    id:"Japan",
                    v:"+0900"
                },{
                    id:"Kazakhstan(Aqtau)",
                    v:"+0400"
                },{
                    id:"Kazakhstan(Aqtobe)",
                    v:"+0500"
                },{
                    id:"Kazakhstan(Astana)",
                    v:"+0600"
                },{
                    id:"Kirghizia",
                    v:"+0500"
                },{
                    id:"Korea",
                    v:"+0900"
                },{
                    id:"Laos",
                    v:"+0700"
                },{
                    id:"Malaysia",
                    v:"+0800"
                },{
                    id:"Mongolia",
                    v:"+0800",
                    tag:"03L03|09L03"
                },{
                    id:"Philippines",
                    v:"+0800"
                },{
                    id:"Russia(Anadyr)",
                    v:"+1300",
                    tag:"03L03|10L03"
                },{
                    id:"Russia(Kamchatka)",
                    v:"+1200",
                    tag:"03L03|10L03"
                },{
                    id:"Russia(Magadan)",
                    v:"+1100",
                    tag:"03L03|10L03"
                },{
                    id:"Russia(Vladivostok)",
                    v:"+1000",
                    tag:"03L03|10L03"
                },{
                    id:"Russia(Yakutsk)",
                    v:"+0900",
                    tag:"03L03|10L03"
                },{
                    id:"Singapore",
                    v:"+0800"
                },{
                    id:"Thailand",
                    v:"+0700"
                },{
                    id:"Vietnam",
                    v:"+0700"
                }]
            },{
                id:"Asia(South,West)",
                sub:[{
                    id:"Afghanistan",
                    v:"+0430"
                },{
                    id:"Arab Emirates",
                    v:"+0400"
                },{
                    id:"Bahrain",
                    v:"+0300"
                },{
                    id:"Bangladesh",
                    v:"+0600"
                },{
                    id:"Bhutan",
                    v:"+0600"
                },{
                    id:"Cyprus",
                    v:"+0200"
                },{
                    id:"Georgia",
                    v:"+0500"
                },{
                    id:"India",
                    v:"+0530"
                },{
                    id:"Iran",
                    v:"+0330",
                    tag:"04 13|10 13"
                },{
                    id:"Iraq",
                    v:"+0300",
                    tag:"04 13|10 13"
                },{
                    id:"Israel",
                    v:"+0200",
                    tag:"04F53|09F53"
                },{
                    id:"Jordan",
                    v:"+0200"
                },{
                    id:"Kuwait",
                    v:"+0300"
                },{
                    id:"Lebanon",
                    v:"+0200",
                    tag:"03L03|10L03"
                },{
                    id:"Maldives",
                    v:"+0500"
                },{
                    id:"Nepal",
                    v:"+0545"
                },{
                    id:"Oman",
                    v:"+0400"
                },{
                    id:"Pakistan",
                    v:"+0500"
                },{
                    id:"Palestine",
                    v:"+0200"
                },{
                    id:"Qatar",
                    v:"+0300"
                },{
                    id:"Saudi Arabia",
                    v:"+0300"
                },{
                    id:"Sri Lanka",
                    v:"+0600"
                },{
                    id:"Syria",
                    v:"+0200",
                    tag:"04 13|10 13"
                },{
                    id:"Tajikistan",
                    v:"+0500"
                },{
                    id:"Turkey",
                    v:"+0200"
                },{
                    id:"Turkmenistan",
                    v:"+0500"
                },{
                    id:"Uzbekistan",
                    v:"+0500"
                },{
                    id:"Yemen",
                    v:"+0300"
                }]
            },{
                id:"North Europe",
                sub:[{
                    id:"Denmark",
                    v:"+0100",
                    tag:"04F03|10L03"
                },{
                    id:"Faroe Is.(DK)",
                    v:"+0100"
                },{
                    id:"Finland",
                    v:"+0200",
                    tag:"03L01|10L01"
                },{
                    id:"Iceland",
                    v:"+0000"
                },{
                    id:"Jan Mayen(Norway)",
                    v:"-0100"
                },{
                    id:"Norwegian",
                    v:"+0100"
                },{
                    id:"Svalbard(NORWAY)",
                    v:"+0100"
                },{
                    id:"Sweden",
                    v:"+0100",
                    tag:"03L01|10L01"
                }]
            },{
                id:"Eastern Europe",
                sub:[{
                    id:"Armenia",
                    v:"+0400"
                },{
                    id:"Austria",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Azerbaijan",
                    v:"+0400"
                },{
                    id:"Belarus",
                    v:"+0200",
                    tag:"03L03|10L03"
                },{
                    id:"Czech",
                    v:"+0100"
                },{
                    id:"Estonia",
                    v:"+0200"
                },{
                    id:"Georgia",
                    v:"+0500"
                },{
                    id:"Germany",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Hungarian",
                    v:"+0100"
                },{
                    id:"Latvia",
                    v:"+0200"
                },{
                    id:"Liechtenstein",
                    v:"+0100"
                },{
                    id:"Lithuania",
                    v:"+0200"
                },{
                    id:"Moldova",
                    v:"+0200"
                },{
                    id:"Poland",
                    v:"+0100"
                },{
                    id:"Rumania",
                    v:"+0200"
                },{
                    id:"Russia(Moscow)",
                    v:"+0300",
                    tag:"03L03|10L03"
                },{
                    id:"Slovakia",
                    v:"+0100"
                },{
                    id:"Switzerland",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Ukraine",
                    v:"+0200"
                },{
                    id:"Ukraine(Simferopol)",
                    v:"+0300"
                }]
            },{
                id:"Western Europe",
                sub:[{
                    id:"Andorra",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Belgium",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Channel Is.(UK)",
                    v:"+0000",
                    tag:"03L01|10L01"
                },{
                    id:"France",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Gibraltar(UK)",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Ireland",
                    v:"+0000",
                    tag:"03L01|10L01"
                },{
                    id:"Isle of Man(UK)",
                    v:"+0000",
                    tag:"03L01|10L01"
                },{
                    id:"Luxembourg",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Monaco",
                    v:"+0100"
                },{
                    id:"Netherlands",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"United Kingdom",
                    v:"+0000",
                    tag:"03L01|10L01"
                }]
            },{
                id:"South Europe",
                sub:[{
                    id:"Albania",
                    v:"+0100"
                },{
                    id:"Bosnia",
                    v:"+0100"
                },{
                    id:"Bulgaria",
                    v:"+0200"
                },{
                    id:"Croatia",
                    v:"+0100"
                },{
                    id:"Greece",
                    v:"+0200",
                    tag:"03L01|10L01"
                },{
                    id:"Holy See",
                    v:"+0100"
                },{
                    id:"Italy",
                    v:"+0100",
                    tag:"03L01|10L01"
                },{
                    id:"Macedonia",
                    v:"+0100"
                },{
                    id:"Malta",
                    v:"+0100"
                },{
                    id:"Montenegro",
                    v:"+0100"
                },{
                    id:"Portugal",
                    v:"+0000",
                    tag:"03L01|10L01"
                },{
                    id:"San Marino",
                    v:"+0100"
                },{
                    id:"Serbia",
                    v:"+0100"
                },{
                    id:"Slovenia",
                    v:"+0100"
                },{
                    id:"Span",
                    v:"+0100",
                    tag:"03L01|10L01"
                }]
            },{
                id:"North America",
                sub:[{
                    id:"Canada(AST)",
                    v:"-0400",
                    tag:"04F02|10L02"
                },{
                    id:"Canada(CST)",
                    v:"-0600",
                    tag:"04F02|10L02"
                },{
                    id:"Canada(EST)",
                    v:"-0500",
                    tag:"04F02|10L02"
                },{
                    id:"Canada(MST)",
                    v:"-0700",
                    tag:"04F02|10L02"
                },{
                    id:"Canada(NST)",
                    v:"-0330",
                    tag:"04F02|10L02"
                },{
                    id:"Canada(PST)",
                    v:"-0800",
                    tag:"04F02|10L02"
                },{
                    id:"Greenland(DK)",
                    v:"-0300"
                },{
                    id:"US(Central)",
                    v:"-0600",
                    tag:"03S02|11F02"
                },{
                    id:"US(Eastern)",
                    v:"-0500",
                    tag:"03S02|11F02"
                },{
                    id:"US(Mountain)",
                    v:"-0700",
                    tag:"03S02|11F02"
                },{
                    id:"US(Pacific)",
                    v:"-0800",
                    tag:"03S02|11F02"
                },{
                    id:"US(Alaska)",
                    v:"-0900"
                },{
                    id:"US(Arizona)",
                    v:"-0700"
                }]
            },{
                id:"South America",
                sub:[{
                    id:"Anguilla(UK)",
                    v:"-0400"
                },{
                    id:"Antigua&amp;Barbuda",
                    v:"-0400"
                },{
                    id:"Antilles(NL)",
                    v:"-0400"
                },{
                    id:"Argentina",
                    v:"-0300"
                },{
                    id:"Aruba(NL)",
                    v:"-0400"
                },{
                    id:"Bahamas",
                    v:"-0500"
                },{
                    id:"Barbados",
                    v:"-0400"
                },{
                    id:"Belize",
                    v:"-0600"
                },{
                    id:"Bolivia",
                    v:"-0400"
                },{
                    id:"Brazil(AST)",
                    v:"-0500",
                    tag:"10F03|02L03"
                },{
                    id:"Brazil(EST)",
                    v:"-0300",
                    tag:"10F03|02L03"
                },{
                    id:"Brazil(FST)",
                    v:"-0200",
                    tag:"10F03|02L03"
                },{
                    id:"Brazil(WST)",
                    v:"-0400",
                    tag:"10F03|02L03"
                },{
                    id:"British Virgin Is.(UK)",
                    v:"-0400"
                },{
                    id:"Cayman Is.(UK)",
                    v:"-0500"
                },{
                    id:"Chilean",
                    v:"-0300",
                    tag:"10F03|03F03"
                },{
                    id:"Chilean(Hanga Roa)",
                    v:"-0500",
                    tag:"10F03|03F03"
                },{
                    id:"Colombia",
                    v:"-0500"
                },{
                    id:"Costa Rica",
                    v:"-0600"
                },{
                    id:"Cuba",
                    v:"-0500",
                    tag:"04 13|10L03"
                },{
                    id:"Dominican",
                    v:"-0400"
                },{
                    id:"Ecuador",
                    v:"-0500"
                },{
                    id:"El Salvador",
                    v:"-0600"
                },{
                    id:"Falklands",
                    v:"-0300",
                    tag:"09F03|04F03"
                },{
                    id:"Grenada",
                    v:"-0400"
                },{
                    id:"Guadeloupe(FR)",
                    v:"-0400"
                },{
                    id:"Guatemala",
                    v:"-0600"
                },{
                    id:"Guiana(FR)",
                    v:"-0300"
                },{
                    id:"Guyana",
                    v:"-0400"
                },{
                    id:"Haiti",
                    v:"-0500"
                },{
                    id:"Honduras",
                    v:"-0600"
                },{
                    id:"Jamaica",
                    v:"-0500"
                },{
                    id:"Martinique(FR)",
                    v:"-0400"
                },{
                    id:"Mexico(Mazatlan)",
                    v:"-0700"
                },{
                    id:"Mexico(Tijuana)",
                    v:"-0800"
                },{
                    id:"Mexico(Mexico)",
                    v:"-0600"
                },{
                    id:"Montserrat(UK)",
                    v:"-0400"
                },{
                    id:"Nicaragua",
                    v:"-0500"
                },{
                    id:"Panama",
                    v:"-0500"
                },{
                    id:"Paraguay",
                    v:"-0400",
                    tag:"10F03|02L03"
                },{
                    id:"Peru",
                    v:"-0500"
                },{
                    id:"Puerto Rico(US)",
                    v:"-0400"
                },{
                    id:"So. Georgia&amp;So. Sandwich Is.(UK)",
                    v:"-0200"
                },{
                    id:"St. Kitts&amp;Nevis",
                    v:"-0400"
                },{
                    id:"St. Lucia",
                    v:"-0400"
                },{
                    id:"St. Vincent&amp;Grenadines",
                    v:"-0400"
                },{
                    id:"Suriname",
                    v:"-0300"
                },{
                    id:"Trinidad&amp;Tobago",
                    v:"-0400"
                },{
                    id:"Turks&amp;Caicos Is.(UK)",
                    v:"-0500"
                },{
                    id:"Uruguay",
                    v:"-0300"
                },{
                    id:"Venezuela",
                    v:"-0400"
                },{
                    id:"Virgin Is.(US)",
                    v:"-0400"
                }]
            },{
                id:"Africa(North)",
                sub:[{
                    id:"Algeria",
                    v:"+0100"
                },{
                    id:"Egypt",
                    v:"+0200",
                    tag:"04L53|09L43"
                },{
                    id:"Libyan",
                    v:"+0200"
                },{
                    id:"Morocco",
                    v:"+0000"
                },{
                    id:"Sudan",
                    v:"+0200"
                },{
                    id:"Tunisia",
                    v:"+0100"
                }]
            },{
                id:"Africa(Western)",
                sub:[{
                    id:"Benin",
                    v:"+0100"
                },{
                    id:"Burkina Faso",
                    v:"+0000"
                },{
                    id:"Canary Is.(SP)",
                    v:"-0100"
                },{
                    id:"Cape Verde",
                    v:"-0100"
                },{
                    id:"Chad",
                    v:"+0100"
                },{
                    id:"Gambia",
                    v:"+0000"
                },{
                    id:"Ghana",
                    v:"+0000"
                },{
                    id:"Guinea",
                    v:"+0000"
                },{
                    id:"Guinea-Bissau",
                    v:"+0000"
                },{
                    id:"Ivory Coast",
                    v:"+0000"
                },{
                    id:"Liberia",
                    v:"+0000"
                },{
                    id:"Mali",
                    v:"+0000"
                },{
                    id:"Mauritania",
                    v:"+0000"
                },{
                    id:"Niger",
                    v:"+0100"
                },{
                    id:"Nigeria",
                    v:"+0100"
                },{
                    id:"Senegal",
                    v:"+0000"
                },{
                    id:"Sierra Leone",
                    v:"+0000"
                },{
                    id:"Togo",
                    v:"+0000"
                },{
                    id:"Western Sahara",
                    v:"+0000"
                }]
            },{
                id:"Africa(Central)",
                sub:[{
                    id:"Cameroon",
                    v:"+0100"
                },{
                    id:"Cen.African Rep.",
                    v:"+0100"
                },{
                    id:"Congo,Democratic",
                    v:"+0100"
                },{
                    id:"Congo,Republic",
                    v:"+0100"
                },{
                    id:"Equatorial Guinea",
                    v:"+0100"
                },{
                    id:"Gabon",
                    v:"+0100"
                },{
                    id:"Sao Tome&amp;Principe",
                    v:"+0000"
                }]
            },{
                id:"Africa(East)",
                sub:[{
                    id:"Burundi",
                    v:"+0200"
                },{
                    id:"Comoros",
                    v:"+0300"
                },{
                    id:"Djibouti",
                    v:"+0300"
                },{
                    id:"Eritrea",
                    v:"+0300"
                },{
                    id:"Ethiopia",
                    v:"+0300"
                },{
                    id:"Kenya",
                    v:"+0300"
                },{
                    id:"Madagascar",
                    v:"+0300"
                },{
                    id:"Malawi",
                    v:"+0200"
                },{
                    id:"Mauritius",
                    v:"+0400"
                },{
                    id:"Mayotte(FR)",
                    v:"+0300"
                },{
                    id:"Mozambique",
                    v:"+0200"
                },{
                    id:"Reunion(FR)",
                    v:"+0400"
                },{
                    id:"Rwanda",
                    v:"+0200"
                },{
                    id:"Seychelles",
                    v:"+0300"
                },{
                    id:"Somalia",
                    v:"+0300"
                },{
                    id:"Tanzania",
                    v:"+0300"
                },{
                    id:"Uganda",
                    v:"+0300"
                }]
            },{
                id:"Africa(South)",
                sub:[{
                    id:"Angola",
                    v:"+0100"
                },{
                    id:"Botswana",
                    v:"+0200"
                },{
                    id:"Lesotho",
                    v:"+0200"
                },{
                    id:"Namibia",
                    v:"+0200",
                    tag:"09F03|04F03"
                },{
                    id:"Saint Helena(UK)",
                    v:"-0100"
                },{
                    id:"South Africa",
                    v:"+0200"
                },{
                    id:"Swaziland",
                    v:"+0200"
                },{
                    id:"Zambia",
                    v:"+0200"
                },{
                    id:"Zimbabwe",
                    v:"+0200"
                }]
            },{
                id:"Oceania",
                sub:[{
                    id:"American Samoa(US)",
                    v:"-1100"
                },{
                    id:"Australia(Adelaide)",
                    v:"+0930",
                    sub:"10L03|03L03"
                },{
                    id:"Australia(Brisbane)",
                    v:"+1000"
                },{
                    id:"Australia(Darwin)",
                    v:"+0930"
                },{
                    id:"Australia(Hobart)",
                    v:"+1000",
                    sub:"10L03|03L03"
                },{
                    id:"Australia(Perth)",
                    v:"+0800"
                },{
                    id:"Australia(Sydney)",
                    v:"+1000",
                    sub:"10L03|03L03"
                },{
                    id:"Cook Islands(NZ)",
                    v:"-1000"
                },{
                    id:"Eniwetok",
                    v:"-1200"
                },{
                    id:"Fiji",
                    v:"+1200",
                    sub:"11F03|02L03"
                },{
                    id:"Guam",
                    v:"+1000"
                },{
                    id:"Hawaii(US)",
                    v:"-1000"
                },{
                    id:"Kiribati",
                    v:"+1100"
                },{
                    id:"Marshall Is.",
                    v:"+1200"
                },{
                    id:"Micronesia",
                    v:"+1000"
                },{
                    id:"Midway Is.(US)",
                    v:"-1100"
                },{
                    id:"Nauru Rep.",
                    v:"+1200"
                },{
                    id:"New Calednia(FR)",
                    v:"+1100"
                },{
                    id:"New Zealand",
                    v:"+1200",
                    sub:"10F03|04F63"
                },{
                    id:"New Zealand(CHADT)",
                    v:"+1245",
                    sub:"10F03|04F63"
                },{
                    id:"Niue(NZ)",
                    v:"-1100"
                },{
                    id:"Nor. Mariana Is.",
                    v:"+1000"
                },{
                    id:"Palau",
                    v:"+0900"
                },{
                    id:"Papua New Guinea",
                    v:"+1000"
                },{
                    id:"Pitcairn Is.(UK)",
                    v:"-0830"
                },{
                    id:"Polynesia(FR)",
                    v:"-1000"
                },{
                    id:"Solomon Is.",
                    v:"+1100"
                },{
                    id:"Tahiti",
                    v:"-1000"
                },{
                    id:"Tokelau(NZ)",
                    v:"-1100"
                },{
                    id:"Tonga",
                    v:"+1300",
                    tag:"10F63|04F63"
                },{
                    id:"Tuvalu",
                    v:"+1200"
                },{
                    id:"Vanuatu",
                    v:"+1100"
                },{
                    id:"Western Samoa",
                    v:"-1100"
                },{
                    id:"Data Line",
                    v:"-1200"
                }]
            }
        ],
        //map like: MILLISECOND <=> ms
        _mapKeys:function(obj){
            var self=this, t=self._key2, m=self._key1;
            for(var i=0,l=m.length;i<l;i++)
                obj[t[i]]=obj[m[i]];
        },
        //get valid datepart
        _validUnit:function(datepart){
            return this.$UNIT[datepart]?datepart:'d';
        },
        _date:function(value,df){return ood.isDate(value) ? value : ((value || value===0)&&isFinite(value)) ? new Date(parseInt(value,10)) : ood.isDate(df) ? df : new Date},
        _isNumb:function(target)  {return typeof target == 'number' && isFinite(target)},
        _numb:function(value,df){return this._isNumb(value)?value:this._isNumb(df)?df:0},
        //time Zone like: -8
        _timeZone:-((new Date).getTimezoneOffset()/60),

        /*get specific date datepart
        *
        */
        get:function(date, datepart, firstDayOfWeek){
            var self=this;
            date = self._date(date);
            datepart = self._validUnit(datepart);
            firstDayOfWeek = self._numb(firstDayOfWeek);

            var map = arguments.callee.map || ( arguments.callee.map = {
                    ms:function(d){return d.getMilliseconds()},
                    s:function(d){return d.getSeconds()},
                    n:function(d){return d.getMinutes()},
                    h :function(d){return d.getHours()},
                    d:function(d){return d.getDate()},
                    ww:function(d,fd){return ood.Date.getWeek(d, fd)},
                    w :function(d,fd){return (7+d.getDay()-fd)%7},
                    m:function(d){return d.getMonth()},
                    q:function(d){return parseInt((d.getMonth()+3)/3-1,10)},
                    y :function(d){return d.getFullYear()},
                    de:function(d){return parseInt(d.getFullYear()/10,10)},
                    c:function(d){return parseInt(d.getFullYear()/100,10)}
                });
            return map[datepart](date,firstDayOfWeek);
        },
        /*
        * _fix(1,3,'0') => '100'
        */
        _fix:function(str,len,chr){
            len=len||2;
            chr=chr||'0';
            str+="";
            if(str.length<len)
                for(var i=str.length;i<len;i++)
                    str=chr+str;
            return str;
        },
        /*add specific datepart to date
        *
        */
        add: function(date, datepart, count ){
            var self=this,
                tu=self.$TIMEUNIT,
                map,
                date2;
            date = self._date(date);
            datepart = self._validUnit(datepart);


            if(!(map=arguments.callee.map)){
                map=arguments.callee.map = {
                    MILLISECOND:function(date,count){date.setTime(date.getTime() + count*tu.ms)},
                    SECOND:function(date,count){date.setTime(date.getTime() + count*tu.s)},
                    MINUTE:function(date,count){date.setTime(date.getTime() + count*tu.n)},
                    HOUR:function(date,count){date.setTime(date.getTime() + count*tu.h)},
                    DAY:function(date,count){date.setTime(date.getTime() + count*tu.d)},
                    WEEK:function(date,count){date.setTime(date.getTime() + count*tu.ww)},
                    MONTH:function(date,count){
                        var a=date.getDate(),b;
                        count = date.getMonth() + count;
                        this.YEAR(date, Math.floor(count/12));
                        date.setMonth((count%12+12)%12);
                        if((b=date.getDate())!=a)
                            this.DAY(date, -b)
                    },
                    QUARTER:function(date,count){this.MONTH(date,count*3)},
                    YEAR:function(date,count){
                        var a=date.getDate(),b;
                        date.setFullYear(date.getFullYear() + count)
                        if((b=date.getDate())!=a)
                            this.DAY(date, -b)
                    },
                    DECADE:function(date,count){this.YEAR(date,10*count)},
                    CENTURY:function(date,count){this.YEAR(date,100*count)}
                };
                self._mapKeys(map);
            }
            map[datepart](date2=new Date(date), count);
            return date2;
        },
        /*get specific datepart diff between startdate and date2
        *
        */
        diff:function(startdate, enddate, datepart, firstDayOfWeek) {
            var self=this;
            startdate = self._date(startdate);
            enddate = self._date(enddate);
            datepart = self._validUnit(datepart);
            firstDayOfWeek = self._numb(firstDayOfWeek);

            var tu=self.$TIMEUNIT,
                map;

            if(!(map=arguments.callee.map)){
                map = arguments.callee.map = {
                    MILLISECOND:function(startdate,enddate){return enddate.getTime()-startdate.getTime()},
                    SECOND:function(startdate,enddate){
                        var startdate = self.getTimSpanStart(startdate,'s'),
                            enddate = self.getTimSpanStart(enddate,'s'),
                            t=enddate.getTime()-startdate.getTime();
                        return t/tu.s;
                    },
                    MINUTE:function(startdate,enddate){
                        var startdate = self.getTimSpanStart(startdate,'n'),
                            enddate = self.getTimSpanStart(enddate,'n'),
                            t=enddate.getTime()-startdate.getTime();
                        return t/tu.n;
                    },
                    HOUR:function(startdate,enddate){
                        var startdate = self.getTimSpanStart(startdate,'h'),
                            enddate = self.getTimSpanStart(enddate,'h'),
                            t=enddate.getTime()-startdate.getTime();
                        return t/tu.h;
                    },
                    DAY:function(startdate,enddate){
                        var startdate = self.getTimSpanStart(startdate,'d',1),
                            enddate = self.getTimSpanStart(enddate,'d',1),
                            t=enddate.getTime()-startdate.getTime();
                        return t/tu.d;
                    },
                    WEEK:function(startdate,enddate,firstDayOfWeek){
                        var startdate = self.getTimSpanStart(startdate,'ww',1,firstDayOfWeek),
                            enddate = self.getTimSpanStart(enddate,'ww',1,firstDayOfWeek),
                            t=enddate.getTime()-startdate.getTime();
                        return t/tu.ww;
                    },
                    MONTH:function(startdate,enddate){return (enddate.getFullYear()-startdate.getFullYear())*12 + (enddate.getMonth()-startdate.getMonth())},
                    QUARTER:function(startdate,enddate){return (enddate.getFullYear()-startdate.getFullYear())*4 + parseInt((enddate.getMonth()-startdate.getMonth())/3,10)},
                    YEAR:function(startdate,enddate){return parseInt((enddate.getFullYear()-startdate.getFullYear()),10)},
                    DECADE:function(startdate,enddate){return parseInt((enddate.getFullYear()-startdate.getFullYear())/10,10)},
                    CENTURY:function(startdate,enddate){return parseInt((enddate.getFullYear()-startdate.getFullYear())/100,10)}
                };
                self._mapKeys(map);
            }
            return map[datepart](new Date(startdate),new Date(enddate),firstDayOfWeek);
        },
        /*get the first datepart begin of certain datepart
        *
        */
        getTimSpanStart: function(date, datepart, count, firstDayOfWeek) {
            var self=this,
                tu=self.$TIMEUNIT,
                map,date2;
            date = self._date(date);
            datepart = self._validUnit(datepart);
            firstDayOfWeek = self._numb(firstDayOfWeek);
            count=self._numb(count,1);
            if(!(map=arguments.callee.map)){
                var clearInDay = function(d) {
                        d.setMilliseconds(0);
                        d.setSeconds(0);
                        d.setMinutes(0);
                        d.setHours(0);
                    },
                    clearInYear = function(d) {
                        clearInDay(d);
                        d.setDate(1);
                        d.setMonth(0);
                    };

                map = arguments.callee.map = {
                    MILLISECOND:function(date,count){
                        var x = date.getMilliseconds();
                        date.setMilliseconds(x - (x % count));
                    },
                    SECOND:function(date,count){
                        date.setMilliseconds(0);
                        var x = date.getSeconds();
                        date.setSeconds(x - (x % count));
                    },
                    MINUTE:function(date,count){
                        date.setMilliseconds(0);
                        date.setSeconds(0);
                        var x = date.getMinutes();
                        date.setTime(date.getTime() - (x % count) * tu.n);
                    },
                    HOUR:function(date,count){
                        date.setMilliseconds(0);
                        date.setSeconds(0);
                        date.setMinutes(0);

                        var x = date.getHours();
                        date.setHours(x - (x % count));
                    },
                    DAY:function(date,count){
                        clearInDay(date);
                        var x=date.getDate();
                        date.setDate(x - (x % count));
                    },
                    WEEK:function(date,count,firstDayOfWeek){
                        clearInDay(date);

                        var d = (date.getDay() + 7 - firstDayOfWeek) % 7,date2,x, a=new Date();
                        date.setTime(date.getTime() - d * tu.d);
                        clearInYear(a);
                        a.setFullYear(date.getFullYear());
                        date2 = (a.getDay() + 7 - firstDayOfWeek) % 7;
                        a.setTime(a.getTime() - date2 * tu.d);

                        x= (date.getTime()-a.getTime())/tu.d/7;

                        date.setTime(date.getTime() - (x % count) * tu.ww);
                    },
                    MONTH:function(date,count){
                        clearInDay(date);
                        date.setDate(1);
                        var x = date.getMonth();
                        date.setMonth(x - (x % count));
                    },
                    QUARTER:function(date,count){
                        count=self._numb(count,1);
                        return this.MONTH(date, count*3);
                    },
                    YEAR:function(date,count){
                        clearInYear(date);
                        var x = date.getFullYear();
                        date.setFullYear(x - (x % count));
                    },
                    DECADE:function(date,count){
                        clearInYear(date);
                        date.setFullYear(Math.floor(date.getFullYear() / 10) * 10);
                    },
                    CENTURY:function(date,count){
                        clearInYear(date);
                        date.setFullYear(Math.floor(date.getFullYear() / 100) * 100);
                    }
                };
                self._mapKeys(map);

            }
            map[datepart](date2=new Date(date),count, firstDayOfWeek);
            return date2;
        },
        /*get the last datepart begin of certain datepart
        *
        */
        getTimSpanEnd : function(date, datepart, count,firstDayOfWeek) {
            var self=this;

            date = self._date(date);
            datepart = self._validUnit(datepart);
            firstDayOfWeek = self._numb(firstDayOfWeek);

            count=self._numb(count,1);

            var originalTime = date.getTime(),
                date2 = self.getTimSpanStart(date, datepart, count, firstDayOfWeek);
            if (date2.getTime() < originalTime)
                date2=self.add(date2, datepart, count);
            return date2;
        },
        /*fake a date for a certain timezone (based on the current timezone of "Date object")
        * You have to offset back it, if you expect a total real date:
        *   var localDate = new Date, timezone9Date=ood.Date.offsetTimeZone(localDate, 9);
        *   localDate.toString() == ood.Date.offsetTimeZone(timezone9Date, -9);
        */
        offsetTimeZone:function(date, targetTimeZone, back){
            var self=this;
            date=self._date(date);
            return new Date(date.getTime() + (back?-1:1)*(targetTimeZone - self._timeZone)*self.$TIMEUNIT.h);
        },

        /*get week
        *
        */
        getWeek:function(date, firstDayOfWeek){
            var self=this, date2, y;
            date=self._date(date);
            firstDayOfWeek = self._numb(firstDayOfWeek),
            y=date.getFullYear();

            date = self.add(self.getTimSpanStart(date, 'ww', 1, firstDayOfWeek),'d',6);

            if(date.getFullYear()!=y)return 1;

            date2 = self.getTimSpanStart(date, 'y', 1);
            date2 = self.add(self.getTimSpanStart(date2, 'ww', 1, firstDayOfWeek),'d',6);

            return self.diff(date2, date, 'ww')+1;
        },
        parse:function(str, format){
            var rtn;
            if(ood.isDate(str)){
                rtn=str;
            }else{
                // avoid null
                str+="";
                if(isFinite(str)){
                    rtn=new Date(parseInt(str,10));
                }else{
                    if(typeof format=='string'){
                        var a=format.split(/[^ymdhns]+/),
                            b=str.split(/[^0-9]+/),
                            n={y:0,m:0,d:0,h:0,n:0,s:0,ms:0};
                        if(a.length && a.length===b.length){
                            for(var i=0;i<a.length;i++)
                                if(a[i].length)
                                    n[a[i]=='ms'?'ms':a[i].charAt(0)]=parseInt(b[i].replace(/^0*/,''),10);
                            rtn=new Date(n.y,n.m-1,n.d,n.h,n.n,n.s,n.ms);
                        }else
                            rtn=null;
                    }else{

                        var self=this,utc,
                            me=arguments.callee,
                            dp=me.dp||(me.dp={
                              FullYear: 2,
                              Month: 4,
                              Date: 6,
                              Hours: 8,
                              Minutes: 10,
                              Seconds: 12,
                              Milliseconds: 14
                            }),
                            match = str.match(me.iso||(me.iso=/^((-\d+|\d{4,})(-(\d{2})(-(\d{2}))?)?)?T((\d{2})(:(\d{2})(:(\d{2})(\.(\d{1,3})(\d)?\d*)?)?)?)?(([+-])(\d{2})((\d{2}))?|Z)?$/)),
                            date = new Date(0)
                            ;
                        if(match){
                            //month
                            if(match[4])match[4]--;
                            //ms to 3 digits
                            if (match[15]>=5)match[14]++;
                            utc = match[16]||match[18]?"UTC":"";
                            for (var i in dp) {
                                var v = match[dp[i]];
                                if(!v)continue;
                                date["set" + utc + i](v);
                                if (date["get" + utc + i]() != match[dp[i]])
                                    rtn=null;
                            }
                            if(match[18]){
                                var h = Number(match[17] + match[18]),
                                    m = Number(match[17] + (match[20] || 0));
                                date.setUTCMinutes(date.getUTCMinutes() + (h * 60) + m);
                            }
                            rtn=date;
                        }else{
                            if(/^((-\d+|\d{4,})(-(\d{1,2})(-(\d{1,2}))))/.test(str))
                                str = str.replace(/-/g,'/');
                            var r=Date.parse(str);
                            rtn=r?date.setTime(r) && date:null;
                        }
                    }
                }
            }
            return rtn===null?null:isFinite(+rtn)?rtn:null;
        },
        getText:function(date, datepart, firstDayOfWeek){
            var self=this, map=self.$TEXTFORMAT;
            date = self._date(date);
            firstDayOfWeek = self._numb(firstDayOfWeek);
            return map[datepart]?map[datepart](date, false, firstDayOfWeek):datepart;
        },
        format:function(date, format, firstDayOfWeek){
            var self=this, map=self.$TEXTFORMAT;
            date = self._date(date);
            firstDayOfWeek = self._numb(firstDayOfWeek);
            return format.replace(/(utciso|iso|yyyy|mm|ww|dd|hh|nn|ss|ms|de|c|y|q|m|w|d|h|n|s)/g, function(a,b){
                return map[b]?map[b](date,true,firstDayOfWeek):b;
            });
        }
    }
});// 图标映射文件 - 将旧的图标类映射到新的Remix Icon图标
ood.iconMapping = {
    // ood-icon-* 系列映射
    'ood-icon-trash': 'ri-delete-bin-line',
    'ood-icon-star': 'ri-star-line',
    'ood-icon-back': 'ri-arrow-left-line',
    'ood-icon-bold': 'ri-bold',
    'ood-icon-file': 'ri-file-line',
    'ood-icon-search': 'ri-search-line',
    'ood-icon-question': 'ri-question-line',
    'ood-icon-remove': 'ri-close-line',
    'ood-icon-upload': 'ri-upload-line',
    'ood-icon-zoomin': 'ri-zoom-in-line',
    'ood-icon-zoomout': 'ri-zoom-out-line',
    'ood-icon-undo': 'ri-arrow-go-back-line',
    'ood-icon-redo': 'ri-arrow-go-forward-line',
    'ood-icon-menu': 'ri-menu-line',
    'ood-icon-check': 'ri-check-line',
    'ood-icon-minus': 'ri-subtract-line',
    'ood-icon-plus': 'ri-add-line',
    'ood-icon-refresh': 'ri-refresh-line',
    'ood-icon-filter': 'ri-filter-line',
    'ood-icon-arrowtop': 'ri-arrow-up-line',
    'ood-icon-arrowbottom': 'ri-arrow-down-line',
    'ood-icon-dragcopy': 'ri-file-copy-line',
    'ood-icon-loading': 'ri-loader-line',
    'ood-icon-dragmove': 'ri-drag-move-line',
    'ood-icon-dragstop': 'ri-hand-coin-line',
    'ood-icon-dropdown': 'ri-arrow-down-s-line',
    'ood-icon-circle': 'ri-checkbox-blank-circle-line',
    'ood-icon-file-expand': 'ri-folder-open-line',
    'ood-icon-file-fold': 'ri-folder-line',
    'ood-icon-indent': 'ri-indent-increase',
    'ood-icon-outdent': 'ri-indent-decrease',
    'ood-icon-picture': 'ri-image-line',
    'ood-icon-print': 'ri-printer-line',
    'ood-icon-underline': 'ri-underline',
    'ood-icon-italic': 'ri-italic',
    'ood-icon-strikethrough': 'ri-strikethrough',
    'ood-icon-link': 'ri-link',
    'ood-icon-unlink': 'ri-link-unlink',
    'ood-icon-first': 'ri-skip-back-line',
    'ood-icon-last': 'ri-skip-forward-line',
    'ood-icon-prev': 'ri-arrow-left-s-line',
    'ood-icon-next': 'ri-arrow-right-s-line',
    'ood-icon-sort': 'ri-sort-asc',
    'ood-icon-sort-checked': 'ri-sort-desc',
    'ood-icon-triangle-down': 'ri-arrow-down-s-fill',
    'ood-icon-triangle-up': 'ri-arrow-up-s-fill',
    'ood-icon-triangle-left': 'ri-arrow-left-s-fill',
    'ood-icon-triangle-right': 'ri-arrow-right-s-fill',
    'ood-icon-placeholder': 'ri-square-line',
    'ood-icon-none': 'ri-square-line',
    
    // spafont spa-icon-* 系列映射
    'spafont spa-icon-delete': 'ri-delete-bin-line',
    'spafont spa-icon-edit': 'ri-edit-line',
    'spafont spa-icon-save': 'ri-save-line',
    'spafont spa-icon-newfile': 'ri-file-add-line',
    'spafont spa-icon-searchreplace': 'ri-search-line',
    'spafont spa-icon-preview': 'ri-eye-line',
    'spafont spa-icon-html': 'ri-html5-line',
    'spafont spa-icon-js': 'ri-javascript-line',
    'spafont spa-icon-json-file': 'ri-file-code-line',
    'spafont spa-icon-page': 'ri-file-text-line',
    'spafont spa-icon-alignw': 'ri-align-left',
    'spafont spa-icon-copy': 'ri-file-copy-line',
    'spafont spa-icon-paste': 'ri-clipboard-line',
    'spafont spa-icon-rename': 'ri-edit-line',
    'spafont spa-icon-astext': 'ri-code-line',
    'spafont spa-icon-function': 'ri-code-line',
    'spafont spa-icon-action': 'ri-play-line',
    'spafont spa-icon-arr': 'ri-arrow-right-line',
    'spafont spa-icon-formatjson': 'ri-file-code-line',
    'spafont spa-icon-xml': 'ri-file-code-line',
    'spafont spa-icon-swf': 'ri-film-line',
    'spafont spa-icon-pic': 'ri-image-line',
    'spafont spa-icon-audio': 'ri-music-line',
    'spafont spa-icon-video': 'ri-video-line',
    'spafont spa-icon-app': 'ri-smartphone-line',
    'spafont spa-icon-empty': 'ri-square-line',
    'spafont spa-icon-unkown': 'ri-question-line',
    'spafont spa-icon-css': 'ri-css3-line',
    
    // ood-uicmd-* 系列映射（用于Dialog.js等组件）
    'ood-uicmd-info': 'ri-information-line',
    'ood-uicmd-opt': 'ri-settings-line',
    'ood-uicmd-pin': 'ri-pushpin-line',
    'ood-uicmd-land': 'ri-layout-right-line',
    'ood-uicmd-refresh': 'ri-refresh-line',
    'ood-uicmd-min': 'ri-subtract-line',
    'ood-uicmd-max': 'ri-fullscreen-line',
    'ood-uicmd-close': 'ri-close-line',
    'ood-uicmd-restore': 'ri-fullscreen-exit-line',
    'ood-uicmd-pop': 'ri-arrow-up-line',
    'ood-uicmd-arrowdrop': 'ri-arrow-down-s-line',
    'ood-uicmd-check': 'ri-check-line',
    'ood-uicmd-check-checked': 'ri-check-line',
    'ood-uicmd-file': 'ri-file-line',
    'ood-uicmd-add': 'ri-add-line',
    'ood-uicmd-delete': 'ri-delete-bin-line',
    'ood-uicmd-dotted': 'ri-more-line',
    'ood-uicmd-radio': 'ri-checkbox-blank-circle-line',
    'ood-uicmd-radio-checked': 'ri-radio-button-line',
    'ood-uicmd-popbox': 'ri-external-link-line',
    'ood-uicmd-remove': 'ri-close-line',
    'ood-uicmd-select': 'ri-check-line',
    'ood-uicmd-save': 'ri-save-line',
    'ood-uicmd-toggle': 'ri-toggle-line',
    'ood-uicmd-toggle-checked': 'ri-toggle-fill',
    'ood-uicmd-date': 'ri-calendar-line',
    'ood-uicmd-color': 'ri-palette-line',
    'ood-uicmd-datetime': 'ri-calendar-event-line',
    'ood-uicmd-helpinput': 'ri-question-line',
    'ood-uicmd-getter': 'ri-cursor-line',
    'ood-uicmd-location': 'ri-map-pin-line',
    'ood-uicmd-time': 'ri-time-line',
    'ood-uicmd-cmdbox': 'ri-terminal-line',
    
    // ood-refresh 特殊图标映射
    'ood-refresh': 'ri-refresh-line'
    
    // 可以根据需要添加更多映射
};