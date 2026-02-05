ood.Class('ood.ModuleFactory', null, {
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
});