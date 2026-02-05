/*
   initialize
    beforeCreated
    onCreated
    beforeShow
    afterShow
    onLoadBaseClass
    onLoadRequiredClass
    onLoadRequiredClassErr
    onIniResource
        iniResource (asy)
    beforeIniComponents
        iniComponents (asy)
    afterIniComponents
        iniExModules (asy)
    onReady
    onRender
    onDestroy
    onModulePropChange

    // for values
    getValue:function(){},
    getUIValue:function(){},
    resetValue:function(){},
    setUIValue:function(){},
    updateValue:function(){},
    isDirtied:function(){},
    checkValid:function(){},
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
});