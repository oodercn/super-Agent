new function () {

    // IE67 don't support :before/:after
    // and, IE8 is buggy, force to ignore :before/:after
    if (ood.browser.ie678) {
        // fonticon fixed
        ood.__iefix2 = ood.builtinFontIcon;
    }
};

//UIProfile Class
ood.Class('ood.UIProfile', 'ood.Profile', {
    Instance: {


        //readonly please
        renderId: null,
        _render: function () {
            var ns = this, ins = ns.boxing(), t, map = ood.$cache.profileMap;

            if (ns.beforeRender && false === ins.beforeRender(ns))
                return;

            //first render
            if (!ns.renderId) {
                var ele = ood.Dom.byId(ns.$domId);

                //for dynRender
                if (!ele) return;

                if (ns.domId != ns.$domId)
                    ele.id = ns.domId;

                map[ns.domId] = map[ns.$domId] = ns;

                //e.g. use div.innerHTML = ui.toHtml();
                if (!ele.$xid)
                    ood.UI.$addEventsHandler(ns, ele, true);

                // for svg widget
                if (ns._elset) {
                    for (var i = 1, l = ns._elset.length; i < l; i++)
                        ood.UI.$addEventsHandler(ns, ns._elset[i].node, true);
                }

                // unselectable="on" will kill onBlur
                if (ood.browser.ie && ood.browser.ver < 10 && 'selectable' in ns.properties)
                    ood.setNodeData(ele, "_onoodsel", ns.properties.selectable ? "true" : "false");

                ns.rendered = ns.renderId = ele.$xid;

                ele = null;
            }

            if (ns.CA && !ood.isEmpty(ns.CA)) {
                ins.setCustomAttr(ns.CA);
            }
            if (ns.CS && !ood.isEmpty(ns.CS)) {
                ins.setCustomStyle(ns.CS);
            }

            // For touch-only platform
            // In ipad or other touch-only platform, you have to decide the droppable order by youself
            // The later added to DOM the higher the priority
            // Add droppable links
            if (ood.browser.isTouch) {
                if ((t = ns.box.$Behaviors.DroppableKeys) && t.length) {
                    ood.arr.each(t, function (o) {
                        ins.getSubNode(o, true).each(function (node) {
                            var key = ns.box.getDropKeys(ns, node.$xid);
                            if (key) {
                                var c = ood.$cache.droppable, a = key.split(/[^\w-]+/);
                                for (var i = 0, l = a.length; i < l; i++) {
                                    c[a[i]] = c[a[i]] || [];
                                    c[a[i]].push(node.$xid);
                                }
                            }
                        });
                    });
                }
            }

            if (ood.browser.fakeTouch || (ood.browser.isTouch && (ood.browser.isAndroid || ood.browser.isBB))) {
                var check = {'auto': 1, 'scroll': 1}, dir;
                // for UI's appearances overflow
                ood.each(ns.box.$Appearances, function (o, i) {
                    dir = '';
                    if (check[o.overflow]) {
                        dir = 'xy';
                    } else {
                        if (check[o['overflow-x']]) dir += 'x';
                        if (check[o['overflow-y']]) dir += 'y';
                    }
                    if (dir) ns.getSubNode(i, true).$touchscroll(dir);
                });
                // for UI's overflow property
                if ('overflow' in ns.properties) {
                    if (ood.browser.fakeTouch) {
                        ood.asyRun(function () {
                            var dir = '', root = ns.getRoot();
                            if (root && !root.isEmpty()) {
                                if (root.isScrollBarShowed('x')) dir += 'x';
                                if (root.isScrollBarShowed('y')) dir += 'y';
                                if (dir) root.$touchscroll(dir);
                            }
                        });
                    } else {
                        if (check[ns.properties.overflow]) {
                            ins.setOverflow(ns.properties.overflow, true);
                        }
                    }
                }
            }

            //RenderTrigger
            if (t = ns.RenderTrigger) {
                for (var i = 0, l = t.length; i < l; i++)
                    t[i].call(ns);
                delete ns.RenderTrigger;
            }

            if (ns.onRender)
                ins.onRender(ns);
            ood.tryF(ns.$onrender, [], ns);

            if (arguments[0] === true && (t = ns.LayoutTrigger)) {
                for (var i = 0, l = t.length; i < l; i++)
                    t[i].call(ns);
                if (ns.onLayout)
                    ins.onLayout(ns);
            }
            if (!ns.properties.lazyAppend) {
                if (ns.children)
                    for (var i = 0, v; v = ns.children[i++];)
                        if (v[0]._render)
                            v[0]._render(true);

                if (ns.$attached) {
                    for (var i = 0, v; v = ns.$attached[i++];) {
                        //(ns.$innerObj||(ns.$innerObj=[])).push(v);
                        if (v._render) v._render(true);
                        if ((t = v._render_holder || v._inline_holder) && t.$afterAttached)
                            ood.tryF(t.$afterAttached, [v], t);
                        else
                            ood.tryF(ns.$afterAttached, [v], ns);
                    }
                    delete ns.$attached;
                }
                if (ns.exchildren) {
                    var arr = [];
                    for (var i = 0, v; v = ns.exchildren[i++];)
                        ins.append(v[0], v[1]);
                    delete ns.exchildren;
                }
                if (ns.exmodules) {
                    var arr = [];
                    for (var i = 0, v; v = ns.exmodules[i++];) {
                        v[0].show(null, ins, v[1], false);
                    }
                    delete ns.exmodules;
                }
            }


            ns.renderCompleted = 1;
        },
        __gc: function (ignoreEffects, purgeNow) {
            var ns = this, t, args = ood.toArr(arguments);
            if (ns.destroyed) return;
            if (ns.$beforeDestroy) {
                ood.each(ns.$beforeDestroy, function (f) {
                    ood.tryF(f, args, ns);
                });
                ood.breakO(ns.$beforeDestroy, 2);
            }
            ood.tryF(ns.$ondestory, args, ns);
            if (ns.onDestroy) ns.boxing().onDestroy();
            if (ns.destroyTrigger) ns.destroyTrigger();

            //gc already
            if (!ns.serialId) return;
            if (t = ns._$composed)
                ood.each(t, function (v) {
                    v.__gc(ignoreEffects, purgeNow);
                });

            //clear cache things
            ns.clearCache();

            //for refresh function
            if (!ns.$noReclaim) {
                //restore dom id
                t = ood.$cache.reclaimId;
                (t[ns.key] || (t[ns.key] = [])).push(ns.serialId);
            } else delete ns.$noReclaim

            //clear cache point
            delete ood.$cache.profileMap[ns.domId];
            delete ood.$cache.profileMap[ns.$domId];

            // try to clear parent host
            var o;
            if (ns.alias && ns.host && (o = ns.host[ns.alias]) && (o = o._nodes) && (o.length === 0 || o.length === 1 && o[0] == ns)) {
                ns.host[ns.alias] = null;
                if (!(ns.host === window && ood.browser.ie && ood.browser.ver <= 8))
                    delete ns.host[ns.alias];
            }

            //clear anti link
            ns.unLinkAll();

            if (ns.LayoutTrigger)
                ns.LayoutTrigger.length = 0;
            if (ns.RenderTrigger)
                ns.RenderTrigger.length = 0;

            //gc children
            if ((t = ns.children).length) {
                t = ood.copy(t);
                for (var i = 0; i < t.length; i++) {
                    t[i][0].__gc(ignoreEffects, purgeNow);
                    t[i].length = 0;
                }
                t.length = 0;
            }

            //set once
            ns.destroyed = true;
            //afterDestroy
            if (ns.$afterDestroy) {
                ood.each(ns.$afterDestroy, function (f) {
                    ood.tryF(f, args, ns);
                });
                ood.breakO(ns.$afterDestroy, 2);
            }
            if (ns.afterDestroy) ns.boxing().afterDestroy(ns);
            ood.breakO([ns.properties, ns.events, ns.CF, ns.CB, ns.CC, ns.CA, ns.CS, ns], 2);
            //set again
            ns.destroyed = true;
        },
        linkParent: function (parentProfile, linkId, index) {
            var profile = this;
            //unlink first
            profile.unlinkParent();
            if (!profile.destroyed) {
                //link
                profile.parent = parentProfile;
                profile.childrenId = linkId;
                profile.link(parentProfile.children, '$parent', [profile, linkId], index);
            }
            return profile;
        },
        unlinkParent: function () {
            var profile = this;
            delete profile.parent;
            delete profile.childrenId;
            profile.unLink('$parent');
            return profile;
        },
        getRootNode: function () {
            return ood.getNodeData(this.renderId, 'element');
        },
        getRoot: function () {
            var ns = this;
            return ns.renderId ? (ns['*'] || (ns['*'] = ood([ns.renderId], false))) : ood([], false);
        },
        getAllNodes: function () {
            return this.getRoot().query().merge(this.getRoot());
        },
        getContainer: function (subId) {
            if (subId !== true && (subId = typeof subId == 'string' ? subId : null)) subId = this.getSubIdByItemId(subId);
            return this.box._CONTAINERKEY ? this.getSubNodes(this.box._CONTAINERKEY, subId) : this.keys.PANEL ? this.getSubNodes(this.keys.PANEL, subId) : this.getRoot();
        },
        // wrap these functions from ood.CSS
        getEmSize: function (force) {
            var prf = this,
                root = prf.getRoot(),
                node = root.get(0);
            // for special parent css
            return (!force && prf._nodeEmSize) || (node && (prf._nodeEmSize = root._getEmSize())) || ood.CSS._getDftEmSize();
        },
        adjustSize: function (useProp, asy, flag) {
            var prf = this,
                t = prf.getRootNode();
            if (t && (useProp || (t = t.style))) {
                var f = function () {
                    ood.UI.$tryResize(prf, useProp ? prf.properties.width : t.width, useProp ? prf.properties.height : t.height, true, flag);
                };
                if (asy) ood.asyRun(f);
                f();
            }
        },
        // have to call these after rendered
        $px: function (value, node, roundPx) {
            var ns = this;
            return ood.CSS.$px(value, node || function () {
                return ns.getEmSize()
            }, roundPx);
        },
        $em: function (value, node, roundPx) {
            var ns = this;
            return ood.CSS.$em(value, node || function () {
                return ns.getEmSize()
            }, roundPx);
        },
        $px2em: function (value, node, roundPx) {
            var ns = this;
            return ood.CSS.$px2em(value, node || function () {
                return ns.getEmSize()
            }, roundPx);
        },
        $em2px: function (value, node, roundPx) {
            var ns = this;
            return ood.CSS.$em2px(value, node || function () {
                return ns.getEmSize()
            }, roundPx);
        },
        $forceu: function (value, u, node, roundPx) {
            var ns = this;
            return ood.CSS.$forceu(value, u, node || function () {
                return ns.getEmSize()
            }, roundPx);
        },
        $addpx: function (a, b, node) {
            var ns = this;
            return ood.CSS.$addpx(a, b, node || function () {
                return ns.getEmSize()
            });
        },
        $isEm: function (value) {
            return ood.CSS.$isEm(value);
        },
        $isPx: function (value) {
            return ood.CSS.$isPx(value);
        },
        $picku: function (value) {
            return ood.CSS.$picku(value);
        },
        $addu: function (value) {
            return ood.CSS.$addu(value);
        },

        _cacheR1: /^\w[\w_-]*$/,
        setDomId: function (id) {
            var t = this, c = ood.$cache.profileMap;
            //ensure the value
            if (typeof id == 'string' && (t._cacheR1.test(id) || id == t.$domId) && !ood.Dom.byId(id)) {
                //delete the original one
                if (t.domId != t.$domId) delete c[t.domId];
                //set profile's domId
                t.domId = id;

                //change the dom Node id value
                if (t.renderId)
                    t.getRootNode().id = id;

                //if doesn't create yet, don't set it to ood.$cache:
                if (c[t.$domId]) c[id] = t;
            }
            return t;
        },
        getDomId: function () {
            return this.domId;
        },
        clearCache: function () {
            var ns = this,
                t = ns.$_egetter;
            for (var i in t) {
                t[i].length = 0;
                delete t[i];
            }

            t = ns.$_domid;
            for (var i in t) {
                t[i].__gc(true, true);
                delete t[i];
            }
            delete ns['*'];

            return ns;
        },
        //get events function from profile
        _getEV: function (funs, id, name) {
            var self = this,
                $k = id + "+" + name,
                g = self.$_egetter || (self.$_egetter = {}),
                cache;
            if (g[$k]) {
                Array.prototype.push.apply(funs, g[$k]);
                return;
            } else cache = g[$k] = [];

            var dom = ood.$cache.profileMap, t, key;
            //for event attached on dom node
            if ((t = dom[id]) && (t = t.events) && (t = t[name]))
                for (var i = 0, l = t.length; i < l; i++)
                    if (typeof t[t[i]] == 'function')
                        cache.push(funs[funs.length] = t[t[i]]);

            //for event attached on ood widgets
            //get event function path of cache
            key = id.split(":")[0].split("-")[1];

            //for priority intercept
            if (typeof (((t = self._CB) && (key ? (t = t[key]) : 1)) && (t = t[name])) == 'function')
                cache.push(funs[funs.length] = t);
            else {
                //get event function from customBehavior first
                if (typeof (((t = self.CB) && (key ? (t = t[key]) : 1)) && (t = t[name])) == 'function')
                    cache.push(funs[funs.length] = t);
                else {
                    //get event function from public behavior
                    if (typeof (((t = self.behavior) && (key ? (t = t[key]) : 1)) && (t = t[name])) == 'function')
                        cache.push(funs[funs.length] = t);
                }
            }
        },
        _cacheR2: /<!--\x03([^>^\s]*)\x04-->/g,
        toHtml: function (force) {
            var self = this,
                prop = self.properties,
                c = self.box,
                h = {},
                str,
                k1 = 'ood.UIProfile',
                k2 = 'ood.Module',
                id, i, l, o, m, a, b, data;
            if (self.destroyed) return "";

            // create first
            if (c['ood.svg']) {
                c._RenderSVG(self);
                return "";
            } else {
                //before _dynamicTemplate
                data = c._prepareData(self);
                if (c._dynamicTemplate) c._dynamicTemplate(self);
                str = c._build(self, data);

                if ((!prop.lazyAppend || force) && (m = self.children)) {
                    for (i = 0, l = m.length; i < l; i++) {
                        o = m[i];
                        if (o && o[0]) {
                            if (o[0][k2]) {
                                var mh = new ood.UI.MoudluePlaceHolder({
                                    host: o[0].host,
                                    alias: o[0].alias
                                });
                                mh.get(0)._module = o[0];
                                o[0] = mh.get(0);
                            }
                            if (o[0][k1]) {
                                id = o[1] || '';
                                a = h[id] || (h[id] = []);
                                a[a.length] = o[0].toHtml(force);
                            }
                        }
                    }
                }

                return str.replace(self._cacheR2, function (a, b) {
                    return h[b] ? h[b].join('') : '';
                });
            }
        },
        _buildItems: function (key, items, addEventHandler) {
            var ns = this,
                box = ns.box,
                str = box._rpt(ns, ood.UI.$doTemplate(ns, ood.get(ood.$cache.template, [box.KEY, ns._hash]), items, key)),
                nodes = ood.UI.$toDom(ns, str.replace(ns._cacheR2, ''), addEventHandler);
            if (ns.CA && !ood.isEmpty(ns.CA)) {
                ns.boxing().setCustomAttr(ns.CA, undefined, nodes);
            }
            // set custom styles for the given nodes only
            if (ns.CS && !ood.isEmpty(ns.CS)) {
                ns.boxing().setCustomStyle(ns.CS, undefined, nodes);
            }
            return nodes;
        },
        serialize: function (rtnString, keepHost, children) {
            var t, m, moduleHash = {},
                self = this,
                o = (t = self.box._beforeSerialized) ? t(self) : self,
                r = {
                    alias: o.alias,
                    key: o.key,
                    host: o.host
                },
                zz = o.moduleClass + "[" + o.moduleXid + "]";
            //host
            if (r.host === self) {
                delete r.host;
            } else if (o.host && !keepHost) {
                if (rtnString !== false)
                    r.host = '@this';
                else
                    delete r.host;
            }
            if (typeof o.theme == "string")
                r.theme = o.theme;

            //domId
            if (o.$domId != o.domId) r.domId = o.domId;

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

            if (!ood.isEmpty(o.CB)) r.CB = ood.copy(o.CB);
            if (!ood.isEmpty(o.CC)) r.CC = ood.copy(o.CC);
            if (!ood.isEmpty(o.CF)) r.CF = ood.copy(o.CF);
            if (!ood.isEmpty(o.CS)) r.CS = ood.clone(o.CS, function (o, i) {
                return !((i + "").charAt(0) == "$" && !o)
            });
            if (!ood.isEmpty(o.CA)) r.CA = ood.copy(o.CA);
            if (typeof o.theme == "string") r.theme = o.theme;

            //children
            if (false !== children && o.children && o.children.length) {
                if (o.box.KEY != "ood.UI.SVGPaper") {
                    ood.arr.stableSort(o.children, function (x, y) {
                        x = (x[0].properties.tabindex || 0);
                        y = (y[0].properties.tabindex || 0);
                        return x > y ? 1 : x == y ? 0 : -1;
                    });
                }
                t = r.children = [];
                ood.arr.each(o.children, function (v, w, y, z) {
                    w = v[0];
                    if (w.moduleClass && w.moduleXid && (y = ood.SC.get(w.moduleClass)) && (y = y.getInstance(w.moduleXid)) && y["ood.Module"]) {
                        z = w.moduleClass + "[" + w.moduleXid + "]";
                        // same module with the parent
                        if (z !== zz) {
                            // same module with another sibling
                            if (moduleHash[z]) {
                                return;
                            } else {
                                moduleHash[z] = 1;
                                w = y;
                            }
                        }
                    }
                    m = [w.serialize(false, keepHost)];
                    if (v[1]) m[1] = v[1];
                    t[t.length] = m
                });
            }
            if (false !== children && o.exchildren && o.exchildren.length) {
                r.exchildren = o.exchildren;
            }
            moduleHash = null;
            return rtnString === false ? r : ood.serialize(r);
        },
        _applySetAction: function (fun, value, ovalue, force, tag, tag2) {
            if (this.renderId)
                return fun.call(this, value, ovalue, force, tag, tag2);
        },
        getKey: function (id, tagOnly) {
            var t;
            if (id.charAt(0) == '!') id = ood.use(id).id();
            if (id.indexOf(':') == -1) id = (t = ood.$cache.profileMap[id]) && (t.$domId);
            if (id) {
                id = id.split(":")[0];
                if (tagOnly) id = id.split('-')[1] || "KEY";
            }
            return id || "";
        },
        getSubId: function (id) {
            var t;
            if (id.charAt(0) == '!') id = ood.use(id).id();
            if (id.indexOf(':') == -1) id = (t = ood.$cache.profileMap[id]) && (t.$domId);
            return id ? id.split(":")[2] : "";
        },
        pickSubId: function (key) {
            var self = this, r, o = self.cache_subid || (self.cache_subid = {});
            if ((o[key] || (o[key] = []))[0]) return o[key].shift();
            o = self.subId || (self.subId = {});
            r = (o[key] || (o[key] = new ood.id)).next();
            return r;
        },
        reclaimSubId: function (id, key) {
            var o = this.cache_subid || (this.cache_subid = {});
            (o[key] || (o[key] = [])).push(id);
        },
        /*
        *('KEY','-hover',false);
        */
        _cacheR3: /\./g,
        _cacheH1: {},
        getClass: function (key, tag) {
            key = this.keys[key] || key;
            var self = this,
                hash = key + ":" + (tag || '');
            return self._cacheH1[hash] || (self._cacheH1[hash] = key.replace(self._cacheR3, '-').toLowerCase().replace('ood-ui', 'ood') + (tag || ''));
        },
        _getSubNodeId: function (key, subId, tag) {
            var arr = this.$domId.split(':');
            arr[0] = key;
            arr[2] = ood.isSet(subId) ? (subId + "") : '';
            if (tag) arr[2] += '_' + tag;
            key = arr.join(':');
            return key == this.$domId
                ? ood.$cache.profileMap[key].domId
                : key;
        },
        //flag : remove from cache
        getSubNode: function (key, subId, tag) {
            var ns = this;

            // destroyed already
            if (!ns.renderId) return ood();

            var key = ns.keys[key] || key, r,
                h = ns.$_domid || (ns.$_domid = {});

            // by key only
            if (subId === true) {
                //key==ns.keys.KEY for domId!=$domId
                if (key == ns.keys.KEY) r = ood([ns.renderId]);
                if (!r || !r.get(0)) {
                    r = ood([ns.renderId]).query('*', 'id', new RegExp('^' + key + ':' + ns.serialId + (tag ? ("_" + tag) : "")));
                }
            } else {
                if (!ood.isSet(subId) && h[key] && h[key]._nodes.length == 1) return h[key];
                r = ood(ns._getSubNodeId(key, subId, tag));
                if (r._nodes.length == 1 && !ood.isSet(subId)) h[key] = r;
            }
            return r;
        },
        getSubNodes: function (arr, subId, tag) {
            var ns = this, a = [], s1 = !ood.isArr(arr), s2 = !ood.isArr(subId), a, o, v, push = Array.prototype.push;
            if (subId === true || ood.isNull(subId)) {
                if (!s1) {
                    a = [];
                    for (var i = 0; o = arr[i++];) a.push(ns.keys[o] || o);
                    arr = '(' + a.join('|') + ')';
                }
                // get call once for better performance
                push.apply(a, ns.getSubNode(arr, true, tag).get());
            } else {
                if (s1) {
                    if (s2)
                        push.apply(a, ns.getSubNode(arr, subId, tag).get());
                    else
                        for (var j = 0; v = subId[j++];)
                            push.apply(a, ns.getSubNode(arr, v, tag).get());
                } else {
                    for (var i = 0; o = arr[i++];) {
                        if (s2)
                            push.apply(a, ns.getSubNode(o, subId, tag).get());
                        else
                            for (var j = 0; v = subId[j++];)
                                push.apply(a, ns.getSubNode(o, v, tag).get());
                    }
                }
            }
            return ood(a);
        },

        getSubNodeByItemId: function (key, itemId, tag) {
            return (itemId = this.getSubIdByItemId(itemId)) ? this.getSubNode(key, itemId, tag) : ood();
        },
        getItemByItemId: function (itemId) {
            var prf = this, t;
            if (ood.isNumb(itemId)) itemId = ood.get(prf.properties.items, [itemId, "id"]);
            if ((t = prf.ItemIdMapSubSerialId) && (t = t[itemId]))
                return prf.SubSerialIdMapItem[t];
            t = prf.queryItems(prf.properties.items, function (v, k) {
                return v.id == itemId;
            }, 1, 1);
            return t && t[0];
        },
        getItemByItemCaption: function (caption) {
            var prf = this, t;
            if (ood.isNumb(caption)) caption = ood.get(prf.properties.items, [caption, "caption"]);
            if ((t = prf.ItemIdMapSubSerialId) && (t = t[caption]))
                return prf.SubSerialIdMapItem[t];
            t = prf.queryItems(prf.properties.items, function (v, k) {
                return v.caption == caption;
            }, 1, 1);
            return t && t[0];
        },
        getSubIdByItemId: function (itemId) {
            var prf = this, t;
            if (ood.isNumb(itemId)) itemId = ood.get(prf.properties.items, [itemId, "id"]);
            return (t = this.ItemIdMapSubSerialId) && t[itemId];
        },

        getItemByDom: function (src) {
            return this.SubSerialIdMapItem && this.SubSerialIdMapItem[
                this.getSubId(typeof src == 'string'
                    ? src.charAt(0) == '!'
                        ? ((src = ood.use(src).get(0)) && src.id)
                        : src
                    : src.id)
                ];
        },
        getItemIdByDom: function (src) {
            var t;
            return (t = this.getItemByDom(src)) && t.id;
        },
        queryItems: function (items, fun, deep, single, flag) {
            var r = [],
                me = arguments.callee,
                f = me.f || (me.f = function (items, fun, deep, single, flag, r) {
                    ood.arr.each(items, function (o, i) {
                        if (fun === true || fun.call(null, o, i, items)) {
                            r.push(flag ? [o, i, items] : o);
                            if (single)
                                return false;
                        }
                        if (deep && o.sub && o.sub.length)
                            f(o.sub, fun, deep, single, flag, r);
                    });
                });
            f(items, fun, deep, single, flag, r);
            return r;
        }
    },
    Static: {
        getFromDom: function (id) {
            if (
                (id = (id && id.KEY == "ood.Dom") ? id.get(0).id
                        : typeof id == 'string' ? id.charAt(0) == '!'
                            ? ((id = ood.use(id).get(0)) && id.id)
                            : id
                            : (id && id.id)
                ) &&
                (id = ood.Event._getProfile(id)) && id['ood.UIProfile']
            )
                return id;
        }
    }
});

//UI Class
ood.Class("ood.UI", "ood.absObj", {
    Before: function (key, parent_key, o) {
        ood.absBox.$type[key.replace("ood.UI.", "").replace("ood.", "")] = ood.absBox.$type[key] = key;
        return true;
    },
    After: function () {
        var self = this, me = arguments.callee,
            temp, t, k, u, c, i, j, e, w, v, b, d;

        ood.absObj.After.apply(this, arguments);

        // remove datafield for containers
        if (self.Behaviors && self.Behaviors.PanelKeys && self.$DataModel && self.$DataModel.dataField) {
            delete self.$DataModel.dataField;
            delete self.$DataStruct.dataField;
            delete self.prototype.setDataField;
            delete self.prototype.getDataField;
        }

        self._ctrlId = new ood.id();
        self._idCache = [];
        self.$cssKeys = {};

        /*change keys*/
        t = self.$Keys;
        t.KEY = t.$key = self.KEY;
        self.addTemplateKeys(ood.toArr(t, true));

        //Inheriates Behaviors
        v = '$Behaviors';
        k = {};
        if ((t = self.$parent) && (e = t.length)) {
            while (e--) {
                b = t[e][v];
                for (i in b) {
                    if (typeof b[i] == 'object') {
                        if (ood.isArr(b[i])) {
                            u = k[i] || (k[i] = []);
                            u.push.apply(u, b[i]);
                        } else {
                            u = k[i] || (k[i] = {});
                            ood.merge(u, b[i]);
                        }
                    } else
                        k[i] = b[i];
                }
            }
        }
        self[v] = k;

        //Inheriates Templates
        v = '$Templates';
        k = {};
        if ((t = self.$parent) && (e = t[0]))
            for (i in e[v])
                if (i.charAt(0) != '$')
                    k[i] = e[v][i];
        self[v] = ood.clone(k);

        //Inheriates Appearances
        v = '$Appearances';
        k = {};
        if ((t = self.$parent) && (e = t.length))
            while (e--) {
                b = t[e];
                for (i in b[v]) {
                    t = b[v][i];
                    u = k[i] || (k[i] = {});
                    ood.merge(u, t);
                }
            }
        self[v] = k;

        self.setTemplate(self.Templates);
        delete self.Templates;

        self.setBehavior(self.Behaviors || {});
        delete self.Behaviors;

        self.setAppearance(self.Appearances);
        delete self.Appearances;

        if (t = self.PublicAppearance) {
            ood.UI.$cache_css_before += self.buildCSSText(t);
            delete self.PublicAppearance;
        }
    },
    Instance: {
        animate: function (key, callback) {
            // only for the first profile
            var prf = this.get(0), t,
                node = prf.getRootNode(),
                tid = ood.getNodeData(node, '_inthread'),
                reset = ood.getNodeData(node, '_animationreset'), t;
            if (tid && ood.Thread.isAlive(tid)) {
                ood.Thread.abort(tid, 'force');
                ood.setNodeData(node, '_inthread', null);
            }
            if (typeof reset == "function") {
                reset();
                ood.setNodeData(node, '_animationreset', null);
            }

            if (key && (t = ood.AnimBinder.getFromName(key === true ? "blinkAlert" : key))) {
                return t.apply(node, callback);
            } else {
                if (!key) key = "blinkAlert";
                var item = (ood.isHash(key) && key.endpoints) ? key : ood.Dom.$preDefinedAnims[key];
                if (item && item.endpoints) {
                    var onEnd;
                    if (ood.isFun(callback)) {
                        if (ood.isFun(item.onEnd)) {
                            onEnd = function () {
                                item.onEnd.apply(this, arguments);
                                callback.apply(this, arguments);
                            };
                        } else {
                            onEnd = callback;
                        }
                    } else if (ood.isFun(item.onEnd)) {
                        onEnd = item.onEnd;
                    }
                    return prf.getRoot().animate(item.endpoints, item.onStart, onEnd, item.duration || 200, null, item.type || "linear", null, item.unit, item.restore, item.times).start();
                }
            }
        },
        hoverPop: function (node, type, beforePop, beforeHide, parent, groupid) {
            var prf = this.get(0), source = prf.boxing(), popmenu;
            if (!prf.box.$EventHandlers.beforeHoverEffect) {
                source.getRoot().hoverPop(node, type, beforePop, beforeHide, parent, groupid);
                return this;
            }
            node = ood(node);
            popmenu = ood.UIProfile.getFromDom(node.id());
            if (popmenu && popmenu.key === 'ood.UI.PopMenu') {
                popmenu = popmenu.boxing();
            } else {
                popmenu = null;
            }
            if (!ood.isDefined(type)) type = 'outer';
            var aysid = groupid || (source.getRoot().xid() + ":" + node.xid());
            source.each(function (o) {
                o.$beforeHover = type === null ? null : function (prf, item, e, src, mtype) {
                    if (e.$force) return;
                    if (mtype == 'mouseover') {
                        ood.resetRun(aysid, null);
                        var ignore = ood.getData([aysid, '$ui.hover.pop'])
                            && ood.getNodeData(node.get(0) || "empty", '$ui.hover.parent') == src;
                        if (!ignore) {
                            ood.setData([aysid, '$ui.hover.pop'], {item: item});
                            ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', src);
                            if (!beforePop || false !== beforePop(prf, node, e, src, item)) {
                                if (popmenu) popmenu.pop(src, type, parent);
                                else node.popToTop(src, type, parent);
                                node.onMouseover(function () {
                                    ood(src).onMouseover(true)
                                }, 'hoverPop')
                                node.onMouseout(function () {
                                    ood(src).onMouseout(true)
                                }, 'hoverPop');
                            }
                        }
                    } else {
                        ood.resetRun(aysid, function () {
                            ood.setData([aysid, '$ui.hover.pop']);
                            ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', 0);
                            if (!beforeHide || false !== beforeHide(prf, node, e, src, 'host', item)) {
                                if (popmenu) popmenu.hide();
                                else node.hide();
                                node.onMouseover(null, 'hoverPop').onMouseout(null, 'hoverPop');
                            }
                        });
                    }
                };
            });
            if (node) {
                node.onMouseover(type === null ? null : function (e) {
                    if (e.$force) return;
                    if (!ood.getData([aysid, '$ui.hover.pop'])) return;
                    ood.resetRun(aysid, null);
                }, 'hoverPop');
                node.onMouseout(type === null ? null : function (prf, e, src) {
                    if (e.$force) return;
                    if (!ood.getData([aysid, '$ui.hover.pop'])) return;
                    ood.resetRun(aysid, function () {
                        ood.setData([aysid, '$ui.hover.pop'])
                        ood.setNodeData(node.get(0) || "empty", '$ui.hover.parent', 0);
                        var item = ood.getData([aysid, '$ui.hover.pop']);
                        if (!beforeHide || false !== beforeHide(prf, node, e, src, 'pop', item && item.item)) {
                            if (popmenu) popmenu.hide();
                            else node.hide();
                            node.onMouseover(null, 'hoverPop').onMouseout(null, 'hoverPop');
                        }
                    });
                }, 'hoverPop');
                node.css('display', 'none');
            }
            return this;
        },
        setTheme: function (key) {
            if (typeof key != "string" || !key) key = null;
            var k, arr = [];
            this.each(function (o) {
                if (key != o.theme) {
                    if (key === null)
                        delete o.theme;
                    else
                        o.theme = key;
                    arr.push(o);
                }
            });
            ood.UI.pack(arr, false).refresh();
            return this;
        },
        getTheme: function () {
            return this.get(0) && this.get(0).theme;
        },
        getModule: function (top) {
            var prf = this.get(0);
            if (prf) return prf.getModule(top);
        },
        destroy: function (ignoreEffects, purgeNow) {
            var ns = this;
            this.each(function (o, i) {
                if (o.destroyed) return;
                var fun = function () {
                    if (o.beforeDestroy && false === o.boxing().beforeDestroy(ignoreEffects, purgeNow)) return;

                    if (o.$beforeDestroy) {
                        ood.each(o.$beforeDestroy, function (f) {
                            ood.tryF(f, [ignoreEffects, purgeNow], o);
                        });
                        ood.breakO(o.$beforeDestroy, 2);
                    }

                    if (o.renderId) {
                        o.getRoot().remove(true, purgeNow);
                    }
                    else {
                        o.__gc(ignoreEffects, purgeNow);
                    }
                    ood.arr.removeFrom(ns._nodes, i);

                    if (o.$afterDestroy) {
                        ood.each(o.$afterDestroy, function (f) {
                            ood.tryF(f, [ignoreEffects, purgeNow], o);
                        });
                        ood.breakO(o.$afterDestroy, 2);
                    }
                };
                var p = o.properties,
                    a = ignoreEffects ? null : ood.Dom._getEffects(p.hideEffects, 0);
                if (a) ood.Dom._vAnimate(o.getRoot(), false, fun);
                else fun();
            }, null, true);
        },
        isDestroyed: function () {
            return !!(this.get(0) ? this.get(0).destroyed : 1);
        },
        _toDomElems: function () {
            var arr = [];
            //collect those need to be rendered
            ood.arr.each(this._nodes, function (o) {
                if (!o.renderId)
                    arr.push(o);
            });
            //render those
            if (arr.length)
                ood.UI.pack(arr, false).render();

            //get rendered
            arr.length = 0;
            ood.arr.each(this._nodes, function (o) {
                arr.push(o.renderId);
            });
            return arr;
        },

        _ini: function (properties, events, host, theme, CS, CC, CB, CF, CA) {
            var self = this,
                c = self.constructor,
                profile,
                t = 'default',
                options,
                df1 = ood.UI.__resetDftProp,
                df2 = c.__resetDftProp,
                df3 = c.$adjustProp,
                ds = c.$DataStruct,
                alias, temp;
            if (properties && properties['ood.Profile']) {
                profile = properties;
                alias = profile.alias || c.pickAlias();
                ood.UIProfile.apply(profile, [host, self.$key, alias, c, null, events]);
            } else {
                if (properties && properties.key && ood.absBox.$type[properties.key]) {
                    options = properties;
                    properties = null;
                    alias = options.alias || c.pickAlias();
                } else
                    alias = c.pickAlias();
                profile = new ood.UIProfile(host, self.$key, alias, c, properties, events, options);
            }

            for (var i in ds) {
                if (!(i in profile.properties)) {
                    temp = df2 && (i in df2) ? df2[i] : df1 && (i in df1) ? df1[i] : ds[i];
                    profile.properties[i] = typeof temp == 'object' ? ood.clone(temp, true) : temp;
                }
            }
            if (typeof(df3) == "function") df3(profile);

            profile.keys = c.$Keys;

            // custom
            profile.CS = CS ? ood.copy(CS) : (profile.CS || {});
            profile.CB = CB ? ood.copy(CB) : (profile.CB || {});
            profile.CC = CC ? ood.copy(CC) : (profile.CC || {});
            profile.CF = CF ? ood.copy(CF) : (profile.CF || {});
            profile.CA = CA ? ood.copy(CA) : (profile.CA || {});
            if (typeof theme == "string") profile.theme = theme;

            profile.template = c.getTemplate();
            profile.behavior = c.$Behaviors;

            if (!profile.serialId) profile.serialId = c._pickSerialId();

            profile.$domId = profile.key + ":" + profile.serialId + ":";
            profile.domId = profile.domId || profile.$domId;

            profile.RenderTrigger = ood.copy(c.$RenderTrigger);
            profile.LayoutTrigger = ood.copy(c.$LayoutTrigger);

            //set links
            profile.link(ood.UI._cache, 'UI').link(c._cache, 'self').link(ood._pool, 'ood');

            temp = profile.children;
            profile.children = [];
            if (temp && temp.length) {
                for (var i = 0, v; v = temp[i++];) {
                    //from serialize
                    if (!v[0]['ood.UIProfile']) v[0] = ood.create(v[0]).get(0);
                    if (v[0]['ood.UIProfile']) v[0].linkParent(profile, v[1]);
                    else if (v[0]['ood.Module'])
                        v[0].getUIComponents().each(function (p) {
                            p.linkParent(profile, v[1]);
                        });
                }
            }
            self._nodes.push(profile);
            profile.Instace = self;
            self.n0 = profile;
            if (c.$onInited) ood.tryF(c.$onInited, [], profile);
            return self;
        },
        busy: function (coverAll, html, key, subId, bgStyle) {
            html = typeof html == 'string' ? html : 'Loading...';
            // busy dom too
            if (coverAll === true) ood.Dom.busy();

            return this.each(function (profile) {
                ood.resetRun(profile.$xid + ':busy', function (profile, key, subId) {
                    // destroyed
                    if (!profile.box) return;

                    var keys = profile.keys, node;
                    key = keys[key] || keys['BORDER'] || keys['PANEL'] || keys['KEY'];
                    var parentNode = profile.getSubNode(key, subId);
                    if (parentNode.isEmpty())
                        return;

                    if (!profile.$busy || profile.$busy.isEmpty()) {
                        node = profile.$busy = ood.create('<button class="ood-node ood-node-div oodcon ood-icon-loading ood-cover ood-custom" style="position:absolute;text-align:center;left:0;top:0;z-index:10;border:0;padding:0;margin:0;width:100%;height:100%;"><div class="ood-node ood-node-div ood-coverlabel ood-custom"></div></button>');
                    }
                    node = profile.$busy;

                    node.first().html(html, false);

                    parentNode.append(node);

                    node.removeClass(/^ood-rand-css-/);
                    node.query('style').remove(false);
                    if (bgStyle) {
                        var clsName = "ood-rand-css-" + ood.rand();
                        node.addClass(clsName);
                        ood.CSS._appendSS(node.get(0), "." + clsName + ":before{" + bgStyle + "}", clsName, true);
                    }
                    var pn = parentNode.get(0);
                    node._parentOST = pn.scrollTop || 0;
                    node._parentOSL = pn.scrollLeft || 0;
                    node._parentOverflow = pn.style.overflow || '';
                    node._busyP = parentNode.xid();

                    pn.scrollTop = pn.scrollLeft = 0;
                    pn.style.overflow = 'hidden';

                }, 50, [profile, key, subId]);
            });
        },
        free: function () {
            ood.Dom.free();
            return this.each(function (profile) {
                ood.resetRun(profile.$xid + ':busy');
                if (node = profile.$busy) {
                    var pn = ood(node._busyP).get(0);
                    if (pn) {
                        pn.scrollTop = node._parentOST || 0;
                        pn.scrollLeft = node._parentOSL || 0;
                        pn.style.overflow = node._parentOverflow || '';
                    }
                    node._parentOST = node._parentOSL = node._parentOverflow = node._busyP = null;
                    node.remove();
                    delete profile.$busy;
                }
            });
        },
        adjustSize: function (useProp, asy, flag) {
            return this.each(function (prf) {
                prf.adjustSize(useProp, asy, flag);
            });
        },
        reLayout: function (force) {
            return this.each(function (o) {
                if (!o.renderId) return;

                // have to refresh this
                delete o._nodeEmSize;

                var p = o.properties;

                if ((!o.$noB) && p.border && o.boxing()._border)
                    o.boxing()._border(null, false);
                o.$forceRelayout = 1;
                if (p.position == 'absolute' && p.dock && p.dock != 'none') {
                    o.boxing().adjustDock(force);
                } else {
                    if (force) {
                        o._resize_h = o._resize_w = -1;
                    }
                    ood.UI.$tryResize(o, p.width, p.height, force);
                }
                delete o.$forceRelayout;
            });
        },
        toHtml: function (force) {
            var a = [];
            ood.arr.each(this._nodes, function (o) {
                a[a.length] = o.toHtml(force);
            });
            return a.join('');
        },
        render: function (triggerLayOut) {
            var ns = this, arr = [], i, l, o, n = ns._nodes, matrix, a = [], byId = ood.Dom.byId;

            ood.UI.$trytoApplyCSS();

            //get those no-html items
            for (i = 0; o = n[i++];)
                if (!o.renderId && !ood.Dom.byId(o.domId) && !ood.Dom.byId(o.$domId))
                    arr[arr.length] = o;

            //build html and to dom
            if (l = arr.length) {
                for (i = 0; i < l; i++)
                    if (o = arr[i].toHtml())
                        a[a.length] = o;
                if (a.length)
                    ood.UI.$toDom(ns.get(0)/*first represents all*/, a.join(''));
            }

            //render all UIProfiles
            for (i = 0; o = n[i++];)
                o._render(triggerLayOut);

            a.length = arr.length = 0;
            return ns;
        },
        renderOnto: function (node, host, alias) {
            node = ood(node);
            if (node.isEmpty()) return this;

            var self = this,
                pro = self.get(0),
                me = arguments.callee,
                paras = me.paras || (me.paras = function (node) {
                    var r = node.cssRegion();
                    r.tabindex = node.attr('tabIndex');
                    if (r.tabindex <= 0) delete r.tabindex;
                    r.zIndex = node.css('zIndex');
                    r.position = node.css('position');
                    return r;
                }),
                CS = node.attr('style').replace(/\s*(left|top|width|height|right|bottom|position|z-index)\s*\:\s*[\w-.]+\s*/g, ''),
                CC = node.attr('class'),
                id = node.id();

            ood.merge(pro.properties, paras(node), 'all');
            pro.properties.dock = 'none';

            if (CS) self.setCustomStyle('KEY', CS);
            if (CC) self.setCustomClass('KEY', CC);

            if (id) self.setDomId(id);
            if (alias || id) self.setHost(host || window, alias || id);

            self.render(true);
            node.replace(self.getRoot());

            return self;
        },
        setDomId: function (id) {
            this.get(0).setDomId(id);
            return this;
        },
        hide: function (ignoreEffects) {
            return this.each(function (o) {
                if (o.renderId) {
                    var t = o.properties, a = ignoreEffects ? null : ood.Dom._getEffects(t.hideEffects, 0);
                    o.getRoot().hide(function () {
                        t.top = t.left = Math.round(parseFloat(ood.Dom.HIDE_VALUE) || 0);
                        t.dockIgnore = true;
                    }, a);
                }
            });
        },
        popUp: function (pos, type, parent, trigger, group) {
            var prf = this.get(0), t = prf.getRoot();
            if (t = t.get(0)) {
                ood(t).pop(pos, type, parent, trigger, group);
            }
        },
        show: function (parent, subId, left, top, ignoreEffects) {
            return this.each(function (o) {
                var t = o.properties,
                    ins = o.boxing(),
                    b,
                    root = o.getRoot();
                left = (left || left === 0) ? (left || 0) : null;
                top = (top || top === 0) ? (top || 0) : null;
                if (left !== null) t.left = left;
                if (top !== null) t.top = top;
                if (ood.getNodeData(o.renderId, '_oodhide')) {
                    b = 1;
                    t.dockIgnore = false;
                    root.show(left && o.$forceu(left), top && o.$forceu(top), null, ignoreEffects);
                    if (t.position == 'absolute' && t.dock && t.dock != 'none')
                        ood.UI.$dock(o, false, true);
                    //first call show
                } else {
                    parent = parent || o.parent;
                    if (!parent && (!o.renderId || (o.getRootNode().id || "").indexOf(ood.Dom._emptyDivId) === 0))
                        parent = ood('body');
                }
                var p = parent, n;
                if (p) {
                    if (p['ood.UIProfile']) {
                        n = p.renderId;
                        p = p.boxing()
                    }
                    else if (p['ood.UI']) n = (n = p.get(0)) && n.renderId;
                    else n = (p = ood(p)) && p._nodes[0];
                    if (n) {
                        p.append(ins, subId);
//                        if(t.visibility=="hidden")ins.setVisibility("",true);
//                        if(t.display=="none")ins.setDisplay("",true);
                        if (!b) root.show(left && o.$forceu(left), top && o.$forceu(top));
                    }
                }
            });
        },
        clone: function () {
            return arguments.callee.upper.apply(this, ["domId"]);
        },
        refresh: function (remedy) {
            var paras, node, b, p, s, $xid, $inDesign, locked, serialId, renderConf, renderHolder, inlineConf,
                inlineHolder, mcls, mxid, ar, fun, box, children, uiv, save, special, ns = this;
            return ns.each(function (o, i) {
                if (!o.renderId) return;

                box = o.box;

                var host = o.host,
                    alias = o.alias;
                if (o.host && o.host['ood.Module']) {
                    o.host.$ignoreAutoDestroy = true;
                }
                //save related id
                $xid = o.$xid;
                $inDesign = o.$inDesign;
                locked = o.locked;

                serialId = o.serialId;
                mcls = o.moduleClass;
                mxid = o.moduleXid;
                renderConf = o._render_conf;
                renderHolder = o._render_holder;
                inlineConf = o._inline_conf;
                inlineHolder = o._inline_holder;
                ar = o.$afterRefresh;
                special = o.$handleCustomVars;
                if (special) save = special();

                if (typeof o.boxing().getUIValue == 'function') {
                    uiv = o.boxing().getUIValue();
                    if ((o.boxing().getValue() + " ") == (uiv + " "))
                        uiv = null;
                }

                //keep parent
                if (b = !!o.parent) {
                    p = o.parent.boxing();
                    paras = o.childrenId;
                } else
                    p = o.getRoot().parent();

                //protect children's dom node
                //no need to trigger layouttrigger here
                //for example: if use getGhostDiv, upload input cant show file name
                node = remedy ? ood.Dom.getEmptyDiv(o.$inDesign).get(0) : ood.$getGhostDiv();
                o.boxing().getChildren().reBoxing().each(function (v) {
                    node.appendChild(v);
                });
                node = null;

                //keep children
                children = ood.copy(o.children);
                o.boxing().removeChildren();

                //unserialize
                s = o.serialize(false, true);
                fun = o.$refreshTrigger;

                //replace
                var replace = ood.create('span');
                o.getRoot().replace(replace);

                //destroy it
                //avoid reclaiming serialId
                o.$noReclaim = 1;

                // keep cache refrence
                var _c = o.Instace;
                o.boxing().destroy(true, true);

                //set back
                ood.merge(o, s, 'all');
                // notice: remove destroyed here
                delete o.destroyed;
                o.$xid = $xid;
                o.$inDesign = $inDesign;
                o.locked = locked;
                o.$handleCustomVars = special;
                o.serialId = serialId;
                o.moduleClass = mcls;
                o.moduleXid = mxid;
                if (renderConf) o._render_conf = renderConf;
                if (renderHolder) o._render_holder = renderHolder;
                if (inlineConf) o._inline_conf = inlineConf;
                if (inlineHolder) o._inline_holder = inlineHolder;

                // set children link first
                if (children) o.children = children;
                //create
                var n = new box(o).render();

                // set cache refrence
                if (_c) {
                    ood.merge(_c, n, 'all');
                    n.get(0).Instace = _c;
                    // must reset it to keep memory pointer
                    n = _c;
                }
                ns[i] = n.get(0);

                //for functions like: UI refresh itself
                if (fun)
                    fun.call(fun.target, n.get(0));

                //add to parent, and trigger RenderTrigger
                if (b)
                    p.append(n, paras);
                else
                    p.append(n);

                if (host) n.setHost(host, alias);

                //restore children
                ood.arr.each(children, function (v) {
                    delete v[0].$dockParent;
                    n.append.apply(n, v);
                });

                //back to original position
                replace.replace(n.get(0).getRoot());
                replace.remove();
                replace = null;

                if (uiv)
                    n.setUIValue(uiv, true, null, 'refresh');

                if (ar) {
                    n.get(0).$afterRefresh = ar;
                    ar(n.get(0));
                }
                // call it anyway => another $afterRefresh
                if (special && save) o.$handleCustomVars(save);

                if (n.host && n.host['ood.Module']) {
                    delete n.host.$ignoreAutoDestroy;
                }
            });
        },
        append: function (target, subId, pre, base) {
            var pro = this.get(0), prop = pro.properties;
            // default is append to last
            var index, baseN,
                inParent = arguments[4],
                parentNode = arguments[5];
            // add to first, or previous of base
            pre = !!pre;
            if (base) {
                if (base['ood.UI']) {
                    base = base.get(0);
                }
                ood.arr.each(pro.children, function (o, i) {
                    if (o[0] === base) {
                        index = i;
                        return false;
                    }
                });
                if (ood.isNumb(index)) {
                    index = pre ? index : (index + 1);
                    baseN = base.getRoot();
                    if (baseN.isEmpty()) baseN = null;
                }
            } else {
                index = pre ? 0 : -1;
            }

            if (ood.isHash(target) || ood.isStr(target))
                target = ood.create(target);
            if (target['ood.UIProfile']) target = target.boxing();

            // illegal nesting
            /* for performance
            var detect = function(arr){
                ood.arr.each(arr, function(c){
                    if((c[0]||c)==pro)throw new Error('Illegal nesting!');
                    else detect(c.children);
                });
            };
            detect(target._nodes);
            */
            if (!pro.box) {
                return true;
            }
            if (pro.box._IllegalDetect)
                pro.box._IllegalDetect(pro, target, true);

            if (pro.box.$beforeAppend && false === pro.box.$beforeAppend(pro, target, subId, pre, base))
                return;
            if (pro.beforeAppend && false === this.beforeAppend(pro, target, subId, pre, base))
                return;
            if (!target) {
                return;
            }
            if (target['ood.Module']) {
                if (subId !== false) {
                    var i = index;
                    target.getUIComponents().each(function (profile) {
                        profile.linkParent(pro, subId, base ? (i++) : i);
                    });
                }
                if (pro.renderId) {
                    parentNode = inParent ? parentNode : pro.getContainer(subId);
                    if (parentNode && (!parentNode.isEmpty()) && (!prop.lazyAppend || parentNode.css('display') != 'none')) {
                        if (!base) {
                            parentNode[pre ? 'prepend' : 'append'](target);
                        } else if (baseN) {
                            baseN[pre ? 'addPrev' : 'addNext'](target);
                        }
                    }
                }
                else {
                    ood.arr.insertAny(pro.exmodules || (pro.exmodules = []), [target, subId], index, true);
                }
            } else {
                if (subId !== false) {
                    if (target['ood.UI']) {
                        var i = index;
                        target.each(function (profile) {
                            if (profile.linkParent) profile.linkParent(pro, subId, base ? (i++) : i);
                        });
                    }
                }
                if (pro.renderId) {
                    var oldp;
                    parentNode = inParent ? parentNode : pro.getContainer(subId);
                    if (parentNode && (!parentNode.isEmpty()) && (!prop.lazyAppend || parentNode.css('display') != 'none')) {
                        if (pro.parent && ood.get(pro, ["properties", "dock"]) != 'none' && 'absolute' == ood.get(pro, ["properties", "position"]) && !ood.get(pro, ["properties", "dockIgnore"]) && !ood.get(pro, ["properties", "dockFloat"])) {
                            if (target['ood.absBox'])
                                oldp = target.reBoxing().parent();
                        }
                        if (!base) {
                            parentNode[pre ? 'prepend' : 'append'](target);
                        } else if (baseN) {
                            baseN[pre ? 'addPrev' : 'addNext'](target);
                        }
                        //adjust old parent
                        if (oldp && oldp.get(0))
                            oldp.onSize();
                    }
                } else {
                    if (!target['ood.UI']) {
                        ood.arr.insertAny(pro.exchildren || (pro.exchildren = []), [target, subId], index, true);
                    }
                }
            }

            if (pro.box.$afterAppend)
                pro.box.$afterAppend(pro, target, subId, pre, base);
            if (pro.afterAppend)
                this.afterAppend(pro, target, subId, pre, base);
            return this;
        },
        getParent: function () {
            var prf = this.get(0);
            if (prf) return prf.parent && prf.parent.boxing();
        },
        getChildrenId: function () {
            var prf = this.get(0);
            if (prf) return prf.childrenId;
        },
        // type: [true]/penetrate, all even in sub moudles
        // type: [false]/include, with moudles
        // type: [other], ignore moudles
        getChildren: function (subId, type) {
            // return array only, don't recursive call in any module
            if (type === false || type == "include") {
                var prf = this.get(0),
                    moduleHash = {},
                    a = [], z,
                    moduleClass = prf.moduleClass,
                    moduleXid = prf.moduleXid,
                    getModlue = function (p) {
                        if (p.moduleClass && p.moduleXid) {
                            // exclude the container's module
                            if (p.moduleClass !== moduleClass && p.moduleXid !== moduleXid) {
                                // got it already
                                if (moduleHash[z = p.moduleClass + "'" + p.moduleXid + "]"]) {
                                    return null;
                                } else {
                                    moduleHash[z] = 1;
                                    var q = p.getModule();
                                    // module in module, we use the top mudule only( exclude the container's module )
                                    // look up toward top layer
                                    if (q && q.moduleClass && q.moduleXid && q.moduleClass !== moduleClass && q.moduleXid !== moduleXid) {
                                        return getModlue(q);
                                    } else if (q) {
                                        return q;
                                    }
                                }
                            }
                        }
                        return p;
                    },
                    f = function (p) {
                        ood.arr.each(p.children, function (v, t) {
                            t = getModlue(v[0]);
                            if (t) {
                                a.push(t);
                                if (t['ood.UIProfile'] && t.children && t.children.length)
                                    f(t);
                            }
                        });
                    };
                ood.arr.each(prf.children, function (v, t) {
                    if ((subId && typeof(subId) == "string") ? v[1] === subId : 1) {
                        t = getModlue(v[0]);
                        if (t) {
                            a.push(t);
                            if (t['ood.UIProfile'] && t.children && t.children.length)
                                f(t);
                        }
                    }
                });
                // return array only
                return a;
            } else {
                var a = [], f = function (prf) {
                    ood.arr.each(prf.children, function (v) {
                        a.push(v[0]);
                        if (v[0].children && v[0].children.length)
                            f(v[0]);
                    });
                };
                ood.arr.each(this.get(0).children, function (v) {
                    if ((subId && typeof(subId) == "string") ? v[1] === subId : 1) {
                        a.push(v[0]);
                        if ((type === true || type == "penetrate") && v[0].children && v[0].children.length)
                            f(v[0]);
                    }
                });
                return ood.UI.pack(a);
            }
        },
        /**
         * subId:
         *     "id1"
         *     ["id1","id2"]
         *     ["id1;id2"]
         *     [ood.UIProfile]
         *     [ood.UIProfile, [ood.UIProfile]
         *     [ood.UI]
         *     [ood.UI, [ood.UI]
         **/
        removeChildren: function (subId, bDestroy, purgeNow) {
            return this.each(function (o) {
                var c = ood.copy(o.children),
                    s = o.box.$DataModel.valueSeparator || ";",
                    b, arr;
                ood.arr.each(c, function (v) {
                    b = 0;
                    if (!subId || subId === true) {
                        b = 1;
                    } else {
                        if (ood.isStr(subId) || ood.isArr(subId)) {
                            arr = ood.isArr(subId) ? subId : (subId + "").split(s);
                            b = ood.arr.indexOf(arr, v[1]) != -1 || ood.arr.indexOf(arr, v[0]) != -1 || ood.arr.indexOf(arr, v[0].boxing()) != -1;
                        } else {
                            b = v[0] == subId["ood.UI"] ? subId.get(0) : subId;
                        }
                    }
                    if (b) {
                        if (o.beforeRemove && false === o.boxing().beforeRemove(o, v[0], v[1], bDestroy))
                            return;

                        v[0].unlinkParent();

                        if (o.afterRemove)
                            o.boxing().afterRemove(o, v[0], v[1], bDestroy);

                        if (bDestroy && !v[0].destroyed)
                            v[0].boxing().destroy(true, purgeNow);
                    }
                });
            });
        },
        draggable: function (dragKey, dragData, key, options, target) {
            return this.each(function (o) {
                o.getSubNode(o.keys[key] || 'KEY', true)
                    .removeClass('ood-ui-selectable').addClass('ood-ui-unselectable')
                    .beforeMousedown(dragKey ? function (pro, e, src) {
                        if (ood.Event.getBtn(e) != "left") return;
                        if (pro.properties.disabled) return;

                        var target = target ? typeof(target) == "function" ? ood.tryF(getTarget, [], o) : ood(target) : null;
                        if (!target || !target.get(0)) {
                            target = ood(src);
                        }

                        options = options || {};
                        options.dragKey = dragKey;
                        options.dragData = typeof dragData == 'function' ? dragData() : dragData;
                        ood.merge(options, {
                            dragCursor: 'pointer',
                            dragType: 'icon',
                            dragDefer: 2
                        });
                        target.startDrag(e, options);
                    } : null, '_d', -1)
                    .beforeDragbegin(dragKey ? function (profile, e, src) {
                        ood.use(src).onMouseout(true, {$force: true}).onMouseup(true);
                    } : null, '_d', -1);
                if (!dragKey)
                    o.clearCache();
            });
        },
        setCustomFunction: function (key, value) {
            return this.each(function (o) {
                if (typeof key == 'string') {
                    if (value) o.CF[key] = value;
                    else delete o.CF[key];
                } else
                    o.CF = key || {};
            });
        },
        setCustomClass: function (key, value) {
            var me = arguments.callee,
                fun = (me.fun || (me.fun = function (pro, i, h, flag) {
                    if (!h[i]) return;
                    var node = pro.getSubNode(i, true), b;
                    if (!node.isEmpty())
                        ood.arr.each(h[i].split(/\s+/), function (o) {
                            node[flag ? 'removeClass' : 'addClass'](o);
                        });
                }));
            return this.each(function (o) {
                var bak = ood.copy(o.CC), t;

                //set key and value
                if (typeof key == 'string') {
                    t = key;
                    key = ood.copy(o.CC);
                    key[t] = value;
                }
                ood.filter(key, function (o, i) {
                    if (!/^[A-Z][A-Z0-9]*$/.test(i)) {
                        t = key.KEY = key.KEY || {};
                        if (!(i in t)) t[i] = o;
                        return false;
                    }
                    if (!o) return false;
                });
                if (key && typeof key == 'object') {
                    if (o.renderId) {
                        for (var i in bak)
                            fun(o, i, bak, true);
                        for (var i in key)
                            fun(o, i, key);
                    }
                    o.CC = key;
                    //clear all
                } else {
                    if (o.renderId)
                        for (var i in bak)
                            fun(o, i, bak, true);
                    o.CC = {};
                }
                delete o._nodeEmSize;
            });
        },
        setCustomAttr: function (key, value, nodes) {
            var me = arguments.callee,
                fun = (me.fun || (me.fun = function (pro, key, CAObj, clear, nodes) {
                    if (!CAObj[key]) return;
                    var hkey = pro.keys[key] || key,
                        tnodes, b;
                    // get target nodes fromin given nodes
                    if (nodes) {
                        tnodes = nodes.query('*', 'id', hkey == pro.keys.KEY ? pro.domId : new RegExp('^' + hkey + ':' + pro.serialId));
                    }
                    // get target nodes from the whole widget
                    else {
                        tnodes = pro.getSubNode(key, true);
                    }
                    if (!tnodes.isEmpty()) {
                        if (ood.isHash(CAObj[key])) {
                            ood.each(CAObj[key], function (o, i) {
                                tnodes.attr(i, clear ? '' : (o && typeof o == "string") ? ood.adjustRes(o, 0, 1) : o);
                            });
                        }
                    }
                }));
            return this.each(function (o) {
                var bak = ood.copy(o.CA), t;
                if (typeof key == 'string') {
                    t = key;
                    key = ood.copy(o.CA);
                    key[t] = value;
                }
                ood.filter(key, function (o, i) {
                    if (!/^[A-Z][A-Z0-9]*$/.test(i)) {
                        t = key.KEY = key.KEY || {};
                        if (!(i in t)) t[i] = o;
                        return false;
                    }
                    if (!o) return false;
                });
                //set key and value
                if (!!key && typeof key == 'object') {
                    if (key) {
                        ood.filter(key, function (o, i) {
                            return i != 'id' && i != 'class' && i != 'style' && i != '$xid'
                        });
                    }
                    if (o.renderId) {
                        for (var i in key)
                            fun(o, i, bak, true, nodes);
                        for (var i in key)
                            fun(o, i, key, false, nodes);
                    }
                    o.CA = key;
                    //clear all
                } else {
                    if (o.renderId)
                        for (var i in bak)
                            fun(o, i, bak, true, nodes);
                    o.CA = {};
                }
            });
        },
        setCustomStyle: function (key, value, nodes) {
            var me = arguments.callee,
                fun = (me.fun || (me.fun = function (prf, key, CSObj, clear, nodes) {
                    if (!CSObj[key]) return;
                    var hkey = prf.keys[key] || key,
                        tnodes, b;
                    // get target nodes fromin given nodes
                    if (nodes) {
                        tnodes = nodes.query('*', 'id', hkey == prf.keys.KEY ? prf.domId : new RegExp('^' + hkey + ':' + prf.serialId));
                    }
                    // get target nodes from the whole widget
                    else {
                        tnodes = prf.getSubNode(key, true);
                    }
                    if (!tnodes.isEmpty()) {
                        if (ood.isStr(CSObj[key]))
                            ood.arr.each(CSObj[key].split(';'), function (o, i) {
                                if ((b = o.split(':')).length >= 2) {
                                    i = b.shift();
                                    o = b.join(':');
                                    i = i.replace(/\-(\w)/g, function (a, b) {
                                        return b.toUpperCase()
                                    });
                                    tnodes.css(i, (clear ? '' : (o && typeof o == "string") ? ood.adjustRes(o, 0, 1) : o) || "");
                                }
                            });
                        else if (ood.isHash(CSObj[key]))
                            ood.each(CSObj[key], function (v, i) {
                                if (ood.isStr(v)) {
                                    // "cursor":"point"
                                    if (i.indexOf('background') === 0 || v.indexOf(';') == -1) {
                                        tnodes.css(i, (clear ? '' : (v && typeof v == "string") ? ood.adjustRes(v, 0, 1) : v) || "");
                                    }
                                    // "overflow":"overflow-x:auto;overflow-y:hidden"
                                    else {
                                        ood.arr.each(v.split(';'), function (o) {
                                            if ((b = o.split(':')).length >= 2) {
                                                i = b.shift();
                                                o = b.join(':');
                                                i = i.replace(/\-(\w)/g, function (a, b) {
                                                    return b.toUpperCase()
                                                });
                                                tnodes.css(i, (clear ? '' : (o && typeof o == "string") ? ood.adjustRes(o, 0, 1) : o) || "");
                                            }
                                        });
                                    }
                                } else {
                                    i = i.replace(/\-(\w)/g, function (a, b) {
                                        return b.toUpperCase()
                                    });
                                    tnodes.css(i, (clear ? '' : (v && typeof v == "string") ? ood.adjustRes(v, 0, 1) : v) || "");
                                }
                            });
                    }
                }));
            return this.each(function (o) {
                var bak = ood.copy(o.CS), t;

                if (typeof key == 'string') {
                    t = key;
                    key = ood.copy(o.CS);
                    key[t] = value;
                }
                ood.filter(key, function (o, i) {
                    if (!/^[A-Z][A-Z0-9]*$/.test(i)) {
                        t = key.KEY = key.KEY || {};
                        if (!(i in t)) t[i] = o;
                        return false;
                    }
                    if (!o) return false;
                });
                //set hash dir
                if (!!key && typeof key == 'object') {
                    if (o.renderId) {
                        for (var i in bak)
                            fun(o, i, bak, true, nodes);
                        for (var i in key)
                            fun(o, i, key, false, nodes);
                    }
                    o.CS = key;
                    //clear all
                } else {
                    if (o.renderId)
                        for (var i in bak)
                            fun(o, i, bak, true, nodes);
                    o.CS = {};
                }
                delete o._nodeEmSize;
            });
        },
        setCustomBehavior: function (key, value) {
            return this.each(function (o) {
                if (typeof key == 'string') {
                    if (o.keys[key])
                        o.CB[key] = value || {};
                } else
                    o.CB = key || {};
                if (o.CB.KEY) {
                    ood.merge(o.CB, o.CB.KEY, 'all');
                    delete o.CB.KEY;
                }
                o.clearCache();
            });
        },
        adjustDock: function (force) {
            return this.each(function (o) {
                if (!o.renderId) return;
                var prop = o.properties;
                if (prop.conDockRelative || prop.conLayoutColumns) {
                    o.boxing().adjustSize();
                }
                // adjust self
                if (prop.position == 'absolute') {
                    if ('dock' in prop && prop.dock && prop.dock != 'none' && o.renderId) {
                        var n = o.getRootNode();
                        // ensure display
                        if (n && n.clientHeight) {
                            if (force) {
                                var style = n.style;
                                // ensure force 1
                                style.width = ((parseFloat(o.$px(style.width)) || 0) + 1) + 'px';
                                style.height = ((parseFloat(o.$px(style.height)) || 0) + 1) + 'px';
                                // ensure force
                                o._resize_h = o._resize_w = -1;
                            }
                            ood.UI.$dock(o, true, true);
                        }
                    }
                } else {
                    if (ood.get(o, ['parent', 'properties', 'conDockRelative']) || ood.get(o, ['parent', 'properties', 'conLayoutColumns'])) {
                        //ood.resetRun('conLayoutColumns:'+o.parent.$xid, function(){
                        //    if(!o.destroyed && !o.parent.destroyed)
                        ood.UI._adjustConW(o.parent, o.getRoot().parent(), o);
                        //});
                    }
                }
                // adjust children
                if (o.$onDock) {
                    var n = o.boxing().getContainer(true);
                    if (n && n.onSize && n.get(0)) n.onSize();
                }
            });
        }
    },
    Initialize: function () {
        var ns = this.prototype;
        ood.arr.each('getSubNode,getSubNodes,getDomId,getRootNode,getRoot,getContainer'.split(','), function (o) {
            if (!ns[o]) {
                ns[o] = function () {
                    var p = this.get(0);
                    return p ? p[o].apply(p, arguments) : null;
                };
                ns[o].$original$ = 'ood.UI';
                ns[o].$type$ = 'instance';
                ns[o].$name$ = o;
            }
        });

        var self = this, hash = {};
        ood.each(ood.UI.$ps, function (i, o) {
            hash[o] = {
                $spaceunit: 1,
                ini: 'auto',
                action: function (value) {
                    var self = this,
                        p = self.properties, b = false,
                        args;
                    self.getRoot()[o] ? self.getRoot()[o](value) : ood.Dom._setUnitStyle(self.getRootNode(), o, value);
                    if (o == 'width' || o == 'height') {
                        // for no _onresize widget only
                        if (!self.box._onresize && self.onResize)
                            self.boxing().onResize(self, o == 'width' ? value : null, o == 'height' ? value : null)
                    } else {
                        if (self.onMove)
                            self.boxing().onMove(self, o == 'left' ? value : null, o == 'top' ? value : null, o == 'right' ? value : null, o == 'bottom' ? value : null)
                    }

                    if (p.position == 'absolute' && p.dock != 'none') {
                        args = {
                            $type: p.dock,
                            $dockid: ood.arr.indexOf(['width', 'height', 'fill', 'cover'], p.dock) != -1 ? self.$xid : null
                        };
                        switch (p.dock) {
                            case 'middle':
                                if (o != 'height' && o != 'top') return;
                                args.top = args.height = 1;
                                break;
                            case 'center':
                                if (o != 'width' && o != 'left') return;
                                args.left = args.width = 1;
                                break;
                            case 'top':
                                if (o != 'height' && o != 'top') return;
                                args.width = args.height = 1;
                                break;
                            case 'bottom':
                                if (o != 'height' && o != 'bottom') return;
                                args.width = args.height = 1;
                                break;
                            case 'left':
                                if (o != 'width' && o != 'left') return;
                                args.width = args.height = 1;
                                break;
                            case 'right':
                                if (o != 'width' && o != 'right') return;
                                args.width = args.height = 1;
                                break;
                            case 'width':
                                if ('width' == o) return;
                                args.width = 1;
                                break;
                            case 'height':
                                if ('height' == o) return;
                                args.height = 1;
                                break;
                            case 'fill':
                            case 'cover':
                                if (o == 'width' && o == 'height') return;
                                args.width = args.height = 1;
                                break;
                        }
                        var pp = ood.UIProfile.getFromDom(self.$dockParent);
                        if (pp) pp.boxing().adjustDock(true);
                    }
                }
            }
        });
        ood.merge(hash, {
            renderer: {
                ini: null
            },
            //invalid after dom dom Node
            zIndex: {
                ini: 1,
                action: function (value) {
                    this.getRoot().css('zIndex', value);
                }
            },
            tabindex: {
                ini: 1,
                action: function (value) {
                    var ns = this,
                        reg = new RegExp("^" + ns.key + "[-\\w]*" + ":" + ns.serialId + ":");
                    ns.getRoot().query("*", function (n) {
                        return n.id && reg.test(n.id) && n.getAttribute('tabIndex');
                    }).attr('tabIndex', value);
                }
            },
            position: {
                ini: 'absolute',
                listbox: ['', 'static', 'relative', 'absolute'],
                action: function (value) {
                    var prf = this, prop = prf.properties;
                    prf.getRoot().css('position', value);
                    if (('dock' in prf.box.$DataModel) && prop.dock != 'none' && !prop.dockIgnore) {
                        value = prop.dock;
                        prop.dock = 'none';
                        prf.boxing().adjustDock(true);
                        prop.dock = value;
                    }
                }
            },
            visibility: {
                listbox: ['', 'visible', 'hidden'],
                action: function (value) {
                    if (this.box['ood.svg']) this.boxing().getAllNodes().css('visibility', value);
                    else this.getRoot().css('visibility', value);
                    // special for resizer
                    if (this.$resizer) {
                        if (value == 'hidden')
                            this.$resizer.hide();
                        else
                            this.$resizer.show();
                    }

                    ood.setNodeData(this.getRootNode(), '_setVisibility', 1);
                }
            },
            display: {
                listbox: ['', 'none', 'block', 'inline', 'inline-block'],
                action: function (value) {
                    var n = this.box['ood.svg'] ? this.boxing().getAllNodes() : this.getRoot();
                    if (value == 'inline-block')
                        n.setInlineBlock();
                    else
                        n.css('display', value);
                }
            },
            selectable: {
                ini: false,
                action: function (value) {
                    this.getRoot().setSelectable(!!value);
                }
            }
        });

        self.setDataModel(hash);

        ood.UI.$cache_css_before += ood.UI.buildCSSText({
                '.ood-css-viewport': {
                    '-webkit-text-size-adjust': '100%',
                    '-ms-text-size-adjust': '100%',
                    '-ms-overflow-style': 'scrollbar',
                    '-webkit-tap-highlight-color': 'transparent',
                    'font-size': '75%'
                },
                '.ood-css-viewport, .ood-css-viewport body': {
                    height: '100%',
                    border: '0 none',
                    margin: '0',
                    padding: '0'
                },
                '.ood-css-viewport body': {
                    'font-size': '1rem'
                },
                '@-ms-viewport': {
                    width: 'device-width'
                },
                '.ood-ui-draggable': {},
                '.ood-inline-block': {
                    display: ood.$inlineBlock,
                    zoom: ood.browser.ie ? 1 : null
                },
                ".ood-ui-input": {},
                '.ood-ui-shadow-input': {
                    '-moz-box-shadow': 'inset 2px 2px 2px #EEEEEE',
                    '-webkit-box-shadow': 'inset 2px 2px 2px #EEEEEE',
                    'box-shadow': 'inset 2px 2px 2px #EEEEEE'
                },
                '.ood-ui-shadow': {
                    '-moz-box-shadow': '2px 2px 4px #CCC',
                    '-webkit-box-shadow': '2px 2px 4px #CCC',
                    'box-shadow': '2px 2px 4px #CCC',
                    /* For IE 8 */
                    '-ms-filter': (ood.browser.ie && ood.browser.ver == 8) ? "progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#9f9f9f')" : null,
                    /* For IE 5.5 - 7 */
                    'filter': (ood.browser.ie && ood.browser.ver <= 8) ? "progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#9f9f9f')" : null
                },
                '.ood-ui-shadow-r': {
                    '-moz-box-shadow': '1px 0  1px #CCC',
                    '-webkit-box-shadow': '1px 0  1px #CCC',
                    'box-shadow': '1px 0  1px #CCC',
                    'z-index': 10
                },
                '.ood-ui-shadow-b': {
                    '-moz-box-shadow': '0 1px 1px #CCC',
                    '-webkit-box-shadow': '0 1px 1px #CCC',
                    'box-shadow': '0 1px 1px #CCC',
                    'z-index': 10
                },
                '.ood-ui-image': {
                    'vertical-align': 'middle',
                    width: '1.3333333333333333em',
                    height: '1.3333333333333333em',
                    'background-repeat': 'no-repeat'
                },
                '.ood-uicmd-none, .ood-display-none': {
                    display: 'none'
                },
                ".ood-uitembg": {
                    padding: '.25em .5em',
                    'background-color': 'transparent'
                    //  border: 'solid 1px transparent'
                },
                ".ood-uitembg-hover": {
                    $order: 1,

                },
                ".ood-uitembg-active": {
                    $order: 2,


                },
                ".ood-uitembg-checked": {
                    $order: 3,

                },

                ".ood-uicell": {},
                ".ood-uicell-hover": {
                    $order: 1,

                },
                ".ood-uicell-checked": {
                    $order: 2,

                },
                ".ood-uicell-alt": {
                    $order: 3,

                },
                '.ood-special-icon': {},
                '.ood-uibar-top, .ood-uibar-bottom, .ood-uibar-top-s, .ood-uibar-bottom-s': {
                    position: 'relative',
                    //for avoiding extra space after table in IE
                    'vertical-align': 'baseline'
                },
                '.ood-uibar-top td, .ood-uibar-top-s td, .ood-uibar-bottom td, .ood-uibar-bottom-s td': {},
//uibar-top
                '.ood-uibar-top': {
                    height:'2em'
                },
                '.ood-uibar-top .ood-uibar-tdl': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    left: 0,
                    top: 0,
                    height: '100%'
                },
                '.ood-uibar-top .ood-uibar-tdm': {
                    $order: 1,
                    position: 'absolute',
                    top: 0,
                    left: '12px',
                    right: '12px',
                    height: '100%',
                    width: ood.browser.ie && ood.browser.ver <= 7 ? "expression((this.parentNode.offsetWidth - 24)+'px')" : null
                },
                '.ood-uibar-top .ood-uibar-tdr': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    top: 0,
                    right: 0,
                    height: '100%'
                },
                '.ood-uibar-top .ood-uibar-tdlt': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    left: 0,
                    top: 0,
                    height: '1.5em'
                },
                '.ood-uibar-top .ood-uibar-tdmt': {
                    $order: 1,
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    height: '1.5em',
                    width: ood.browser.ie && ood.browser.ver <= 7 ? "expression((this.parentNode.offsetWidth)+'px')" : null
                },
                '.ood-uibar-top .ood-uibar-tdrt': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    right: 0,
                    top: 0,
                    height: '1.5em'
                },
                '.ood-uibar-focus, .ood-uibar-top-focus .ood-uibar-tdl, .ood-uibar-top-focus .ood-uibar-tdm, .ood-uibar-top-focus .ood-uibar-tdr': {},
                '.ood-uibar-top-focus .oodfont, .ood-uibar-top-focus .oodcon, .ood-uibar-top-focus .ood-uicaption': {},
                '.ood-uibar-top .ood-uibar-cmdl': {
                    overflow: 'hidden',
                    position: 'relative',
                    'padding': '.125em .75em .125em',
                    'white-space': 'nowrap'
                },
                '.ood-uibar-top2 .ood-uibar-cmdr': {
                    position: 'absolute',
                    top: '-.334em',
                    right: '0',
                    'text-align': 'right'
                },
                '.ood-uibar-top .ood-uibar-cmdr': {
                    position: 'absolute',
                    top: '.5em',
                    right: '.5em',
                    'text-align': 'right'
                },
                '.ood-uibar-top .ood-uibar-tdb': {
                    position: 'relative',
                    display: 'none',
                    margin: '.16667em .16667em 0 .16667em'
                },
                '.ood-uicon-main': {
                    position: 'relative',
                    'padding-left': '.3333em',
                    'z-index': 1,
                    overflow: 'visible'
                },
                '.ood-uicon-maini': {
                    'padding-right': '.3333em'
                },
//uibar-bottom
                '.ood-uibar-bottom': {
                    'padding': '3px 0'
                },
                '.ood-uibar-bottom .ood-uibar-tdl': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    left: 0,
                    bottom: 0,
                    height: '100%'
                },
                '.ood-uibar-bottom .ood-uibar-tdm': {
                    $order: 1,
                    position: 'absolute',
                    bottom: 0,
                    left: '12px',
                    right: '12px',
                    height: '100%',
                    width: ood.browser.ie && ood.browser.ver <= 7 ? "expression((this.parentNode.offsetWidth - 24)+'px')" : null
                },
                '.ood-uibar-bottom .ood-uibar-tdr': {
                    $order: 1,
                    position: 'absolute',
                    width: '12px',
                    right: 0,
                    bottom: 0,
                    height: '100%'
                },
//uibar-top-s
                '.ood-uibar-top-s, .ood-uibar-bottom-s, .ood-uibar-top-s .ood-uibar-t': {
                    $order: 3,
                    height: '5px'
                },
                '.ood-uibar-top-s .ood-uibar-tdl': {
                    $order: 3,
                    position: 'absolute',
                    width: '12px',
                    left: 0,
                    top: 0,
                    height: '100%'
                },
                '.ood-uibar-top-s .ood-uibar-tdm': {
                    $order: 3,
                    position: 'absolute',
                    top: 0,
                    left: '12px',
                    right: '12px',
                    height: '100%',
                    width: ood.browser.ie && ood.browser.ver <= 7 ? "expression((this.parentNode.offsetWidth - 24)+'px')" : null
                },
                '.ood-uibar-top-s .ood-uibar-tdr': {
                    $order: 3,
                    position: 'absolute',
                    width: '12px',
                    top: 0,
                    right: 0,
                    height: '100%'
                },
                '.ood-uibar-top-s .ood-uibar-cmdl': {
                    $order: 3,
                    display: 'none'
                },
                '.ood-uibar-top-s .ood-uibar-cmdr': {
                    $order: 3,
                    display: 'none'
                },
//uibar-bottom-s
                '.ood-uibar-bottom-s .ood-uibar-tdl': {
                    $order: 3,
                    position: 'absolute',
                    width: '12px',
                    left: 0,
                    bottom: 0,
                    height: '100%'
                },
                '.ood-uibar-bottom-s .ood-uibar-tdm': {
                    $order: 3,
                    position: 'absolute',
                    bottom: 0,
                    left: '12px',
                    right: '12px',
                    height: '100%',
                    width: ood.browser.ie && ood.browser.ver <= 7 ? "expression((this.parentNode.offsetWidth - 24)+'px')" : null
                },
                '.ood-uibar-bottom-s .ood-uibar-tdr': {
                    $order: 3,
                    position: 'absolute',
                    width: '12px',
                    right: 0,
                    bottom: 0,
                    height: '100%'
                }
            })
            + ood.UI.buildCSSText({
                '.ood-ui-unselectable': {
                    $order: 0,
                    '-moz-user-select': ood.browser.gek ? '-moz-none' : null,
                    '-khtml-user-select': ood.browser.kde ? 'none' : null,
                    '-webkit-user-select': ood.browser.kde ? 'none' : null,
                    '-o-user-select': ood.browser.opr ? 'none' : null,
                    '-ms-user-select': (ood.browser.ie || ood.browser.newie) ? 'none' : null,
                    'user-select': 'none',
                    'touch-action': 'none'
                },
                '.ood-ui-selectable': {
                    $order: 1,
                    '-moz-user-select': ood.browser.gek ? 'text' : null,
                    '-khtml-user-select': ood.browser.kde ? 'text' : null,
                    '-webkit-user-select': ood.browser.kde ? 'text' : null,
                    '-o-user-select': ood.browser.opr ? 'text' : null,
                    '-ms-user-select': (ood.browser.ie || ood.browser.newie) ? 'text' : null,
                    'user-select': 'text',
                    'touch-action': 'auto'
                },
                '.ood-uiw-shell': {
//                background:'transparent',
                    display: ood.$inlineBlock,
                    zoom: ood.browser.ie && ood.browser.ver <= 7 ? 1 : null,
                    //overflow:'hidden',
                    /*opera must be 0 not 'none'*/
                    border: 0,
                    padding: 0,
                    margin: 0
                },
                /*span*/
                '.ood-uiw-frame': {
                    $order: 1,
                    display: 'block',
                    position: 'relative',
                    //overflow:'hidden',
                    border: 0,
                    padding: 0,
                    margin: 0,
                    width: '100%',
                    height: '100%',
                    '-moz-box-flex': '1'
                },
                /*span*/
                '.ood-uiw-border': {
                    $order: 2,
                    display: 'block',
                    //position:'absolute',
                    // modify to relative for 'auto' height
                    position: 'relative',
                    border: 0,
                    padding: 0,
                    margin: 0,
                    left: 0,
                    top: 0,
                    width: '100%',
                    height: '100%'
                }
            })
            + ood.UI.buildCSSText({
                '.ood-uibase': {},
                '.ood-uicontainer': {},
                '.ood-uibar': {
                    $order: 1
                },
                '.ood-uibar-hover': {
                    $order: 1
                },
                ".ood-uibar-active, .ood-uibar-checked, .ood-uibar-expand, .ood-uimenu-hover, .ood-uimenu-active": {
                    $order: 2
                },
                ".ood-ui-ctrl-highlight, .ood-node-highlight, .ood-uibar-checked,  .ood-uibar-expand, .ood-uimenu-hover, .ood-uimenu-active": {
                    $order: 3,

                },
                ".ood-ui-btn::-moz-focus-inner": {
                    $order: 3,
                    padding: "0 !important",
                    border: "0 none !important"
                },
                ".ood-uigradient": {
                    $order: 4
                },
                ".ood-uigradient-hover, .ood-uigradient:hover": {
                    $order: 5
                },
                ".ood-uigradient-active, .ood-uigradient-checked, .ood-uigradient-expand, .ood-uigradient:active, .ood-uigradient-active:hover, .ood-uigradient-checked:hover, .ood-uigradient-expand:hover": {
                    $order: 6
                },
                ".ood-ui-btn": {
                    $order: 7,
                    padding: ".334em",
                    'white-space': 'normal',
                    cursor: 'pointer',

                    display: ood.$inlineBlock,
                    'text-align': 'center',
                    'line-height': '1',
                    zoom: ood.browser.ie ? 1 : null,
                    // for IE6
                    'width_1': (ood.browser.ie && ood.browser.ver <= 7) ? 'auto' : null,
                    'overflow': (ood.browser.ie && ood.browser.ver <= 7) ? 'visible' : null
                },
                ".ood-ui-btn:hover, .ood-ui-btn-hover": {
                    $order: 8,

                },
                '.ood-uiborder-l': {
                    'border-left-style': 'solid',
                    'border-left-width': '1px',

                },
                '.ood-uiborder-r': {
                    'border-right-style': 'solid',
                    'border-right-width': '1px',

                },
                '.ood-uiborder-t': {
                    'border-top-style': 'solid',
                    'border-top-width': '1px',

                },
                '.ood-uiborder-b, .ood-uitem-split': {
                    'border-bottom-style': 'solid',
                    'border-bottom-width': '1px',

                },
                '.ood-uiborder-nob': {
                    $order: 1,
                    'border-bottom-width': 0,
                    'border-bottom-style': 'none'
                },
                '.ood-uiborder-hidden': {
                    border: 'solid 1px transparent',
                    background: 'none'
                },
                '.ood-uiborder-hidden-active, .ood-uiborder-hidden-checked': {},
                '.ood-uiborder-flat': {},
                '.ood-uiborder-outset': {
                    $order: 8,
                    border: 'solid 1px',

                },
                // '.ood-uiborder-inset, .ood-uiborder-hidden-active, .ood-uiborder-hidden-checked': {
                //     $order: 10,
                //     border: 'solid 1px',
                //     'border-color': '#B6B6B6 #F6F6F6 #F6F6F6 #B6B6B6'
                // },
                '.ood-uiborder-dark, .ood-uiborder-flat-hover': {},
                '.ood-uiborder-light, .ood-uiborder-hidden-hover': {},
                '.ood-uiborder-radius': {
                    $order: 11,
                    //  'border-radius': '3px',
                    '-moz-border-radius': '3px',
                    //  '-webkit-border-radius': '3px',
                    '-o-border-radius': '3px',
                    '-ms-border-radius': '3px',
                    '-khtml-border-radius': '3px'
                },
                '.ood-uiborder-radius-big': {
                    $order: 11,
                    //   'border-radius': '6px',
                    '-moz-border-radius': '6px',
                    //      '-webkit-border-radius': '6px',
                    '-o-border-radius': '6px',
                    '-ms-border-radius': '6px',
                    '-khtml-border-radius': '6px'
                },
                '.ood-uiborder-radius-tl': {
                    $order: 12,
                    //    'border-top-left-radius': '3px',
                    '-moz-border-top-left-radius': '3px',
                    //     '-webkit-border-top-left-radius': '3px',
                    '-o-border-top-left-radius': '3px',
                    '-ms-border-top-left-radius': '3px',
                    '-khtml-border-top-left-radius': '3px'
                },
                '.ood-uiborder-radius-tr': {
                    $order: 12,
                    //   'border-top-right-radius': '3px',
                    '-moz-border-top-right-radius': '3px',
                    //    '-webkit-border-top-right-radius': '3px',
                    '-o-border-top-right-radius': '3px',
                    '-ms-border-top-right-radius': '3px',
                    '-khtml-border-top-right-radius': '3px'
                },
                '.ood-uiborder-radius-br': {
                    $order: 12,
                    //     'border-bottom-right-radius': '3px',
                    '-moz-border-bottom-right--radius': '3px',
                    // '-webkit-border-bottom-right--radius': '3px',
                    '-o-border-bottom-right--radius': '3px',
                    '-ms-border-bottom-right--radius': '3px',
                    '-khtml-border-bottom-right--radius': '3px'
                },
                '.ood-uiborder-radius-bl': {
                    $order: 12,
                    //    'border-bottom-left-radius': '3px',
                    '-moz-border-bottom-left-radius': '3px',
                    //    '-webkit-border-bottom-left-radius': '3px',
                    '-o-border-bottom-left-radius': '3px',
                    '-ms-border-bottom-left-radius': '3px',
                    '-khtml-border-bottom-left-radius': '3px'
                },
                '.ood-uiborder-radius-big-tl': {
                    $order: 13,
                    //   'border-top-left-radius': '6px',
                    '-moz-border-top-left-radius': '6px',
                    //'-webkit-border-top-left-radius': '6px',
                    '-o-border-top-left-radius': '6px',
                    '-ms-border-top-left-radius': '6px',
                    '-khtml-border-top-left-radius': '6px'
                },
                '.ood-uiborder-radius-big-tr': {
                    $order: 13,
                    //  'border-top-right-radius': '6px',
                    '-moz-border-top-right-radius': '6px',
                    // '-webkit-border-top-right-radius': '6px',
                    '-o-border-top-right-radius': '6px',
                    '-ms-border-top-right-radius': '6px',
                    '-khtml-border-top-right-radius': '6px'
                },
                '.ood-uiborder-radius-big-br': {
                    $order: 13,
                    //   'border-bottom-right-radius': '6px',
                    '-moz-border-bottom-right--radius': '6px',
                    // '-webkit-border-bottom-right--radius': '6px',
                    '-o-border-bottom-right--radius': '6px',
                    '-ms-border-bottom-right--radius': '6px',
                    '-khtml-border-bottom-right--radius': '6px'
                },
                '.ood-uiborder-radius-big-bl': {
                    $order: 13,
                    //  'border-bottom-left-radius': '6px',
                    '-moz-border-bottom-left-radius': '6px',
                    //   '-webkit-border-bottom-left-radius': '6px',
                    '-o-border-bottom-left-radius': '6px',
                    '-ms-border-bottom-left-radius': '6px',
                    '-khtml-border-bottom-left-radius': '6px'
                },

                '.ood-uiborder-noradius': {
                    $order: 15,
                    'border-radius': '0 !important',
                    '-moz-border-radius': '0 !important',
                    '-webkit-border-radius': '0 !important',
                    '-o-border-radius': '0 !important',
                    '-ms-border-radius': '0 !important',
                    '-khtml-border-radius': '0 !important'
                },
                '.ood-uiborder-noradius-l': {
                    $order: 15,
                    'border-top-left-radius': '0 !important',
                    '-moz-border-top-left-radius': '0 !important',
                    '-webkit-border-top-left-radius': '0 !important',
                    '-o-border-top-left-radius': '0 !important',
                    '-ms-border-top-left-radius': '0 !important',
                    '-khtml-border-top-left-radius': '0 !important',

                    'border-bottom-left-radius': '0 !important',
                    '-moz-border-bottom-left-radius': '0 !important',
                    '-webkit-border-bottom-left-radius': '0 !important',
                    '-o-border-bottom-left-radius': '0 !important',
                    '-ms-border-bottom-left-radius': '0 !important',
                    '-khtml-border-bottom-left-radius': '0 !important'
                },
                '.ood-uiborder-noradius-r': {
                    $order: 15,
                    'border-top-right-radius': '0 !important',
                    '-moz-border-top-right-radius': '0 !important',
                    '-webkit-border-top-right-radius': '0 !important',
                    '-o-border-top-right-radius': '0 !important',
                    '-ms-border-top-right-radius': '0 !important',
                    '-khtml-border-top-right-radius': '0 !important',

                    'border-bottom-right-radius': '0 !important',
                    '-moz-border-bottom-right-radius': '0 !important',
                    '-webkit-border-bottom-right-radius': '0 !important',
                    '-o-border-bottom-right-radius': '0 !important',
                    '-ms-border-bottom-right-radius': '0 !important',
                    '-khtml-border-bottom-right-radius': '0 !important'
                },
                '.ood-uiborder-noradius-t': {
                    $order: 15,
                    'border-top-left-radius': '0 !important',
                    '-moz-border-top-left-radius': '0 !important',
                    '-webkit-border-top-left-radius': '0 !important',
                    '-o-border-top-left-radius': '0 !important',
                    '-ms-border-top-left-radius': '0 !important',
                    '-khtml-border-top-left-radius': '0 !important',

                    'border-top-right-radius': '0 !important',
                    '-moz-border-top-right-radius': '0 !important',
                    '-webkit-border-top-right-radius': '0 !important',
                    '-o-border-top-right-radius': '0 !important',
                    '-ms-border-top-right-radius': '0 !important',
                    '-khtml-border-top-right-radius': '0 !important'
                },
                '.ood-uiborder-noradius-b': {
                    $order: 15,
                    'border-bottom-left-radius': '0 !important',
                    '-moz-border-bottom-left-radius': '0 !important',
                    '-webkit-border-bottom-left-radius': '0 !important',
                    '-o-border-bottom-left-radius': '0 !important',
                    '-ms-border-bottom-left-radius': '0 !important',
                    '-khtml-border-bottom-left-radius': '0 !important',

                    'border-bottom-right-radius': '0 !important',
                    '-moz-border-bottom-right-radius': '0 !important',
                    '-webkit-border-bottom-right-radius': '0 !important',
                    '-o-border-bottom-right-radius': '0 !important',
                    '-ms-border-bottom-right-radius': '0 !important',
                    '-khtml-border-bottom-right-radius': '0 !important'
                },
                '.ood-ui-noshadow, .ood-ui-noshadow .ood-ui-shadow-input, .ood-ui-noshadow .ood-ui-shadow, .ood-ui-noshadow .ood-ui-shadow-b, .ood-ui-noshadow .ood-ui-shadow-r,  .ood-ui-disabled .ood-ui-shadow-input,  .ood-ui-readonly .ood-ui-shadow-input,  .ood-ui-inputreadonly .ood-ui-shadow-input': {
                    $order: 15,
                    '-moz-box-shadow': 'none !important',
                    '-webkit-box-shadow': 'none !important',
                    'box-shadow': 'none !important'
                },
                '.ood-uiborder-circle': {
                    $order: 16,
                    'border-radius': '50%',
                    '-moz-border-radius': '50%',
                    // '-webkit-border-radius': '50%',
                    '-o-border-radius': '50%',
                    '-ms-border-radius': '50%',
                    '-khtml-border-radius': '50%'
                },
                '.ood-uiflag-1': {
                    $order: 16,
                    'border-radius': '50%',
                    '-moz-border-radius': '50%',
                    //  '-webkit-border-radius': '50%',
                    '-o-border-radius': '50%',
                    '-ms-border-radius': '50%',
                    '-khtml-border-radius': '50%',

                    width: ood.browser.contentBox ? '1em' : '1.625em',
                    height: ood.browser.contentBox ? '1em' : '1.625em',
                    padding: '.334em',
                    'background-color': '#eb6e1a',
                    color: '#fff !important',
                    overflow: 'hidden',
                    'text-align': 'center'
                },

                '.ood-uiborder-none': {
                    $order: 20,
                    border: 'none'
                },
                '.ood-uisb': {
                    position: 'absolute'
                },
                '.ood-uisb-none': {
                    display: 'none'
                },
                '.ood-uisb-left': {
                    left: 0,
                    top: 0,
                    width: '3em',
                    height: '100%'
                },
                '.ood-uisb-right': {
                    top: 0,
                    right: 0,
                    width: '3em',
                    height: '100%'
                },
                '.ood-uisb-top': {
                    left: 0,
                    top: 0,
                    width: '100%',
                    height: '3em'
                },
                '.ood-uisb-bottom': {
                    left: 0,
                    bottom: 0,
                    width: '100%',
                    height: '3em'
                },
                '.ood-uisbbtn': {
                    position: 'absolute',
                    cursor: 'pointer',
                    'z-index': 1,
                    width: '1em',
                    height: '1em'
                },
                '.ood-uisb-left .ood-uisbbtn': {
                    left: 0,
                    top: 0
                },
                '.ood-uisb-right .ood-uisbbtn': {
                    right: 0,
                    top: 0
                },
                '.ood-uisb-top .ood-uisbbtn': {
                    right: 0,
                    bottom: 0
                },
                '.ood-uisb-bottom .ood-uisbbtn': {
                    right: 0,
                    top: 0
                },
                '.ood-uisbcap': {
                    position: 'relative',
                    'text-align': 'center',
                    width: '100%',
                    height: '100%'
                },
                '.ood-uisb-left .ood-uisbcap, .ood-uisb-right .ood-uisbcap': {
                    'writing-mode': 'tb-rl',
                    filter: 'flipv fliph'
                },
                '.ood-ltag-cmds': {
                    margin: 0,
                    padding: 0,
                    'vertical-align': 'middle'
                },
                '.ood-rtag-cmds': {
                    margin: 0,
                    padding: 0,
                    'vertical-align': 'middle'
                },
                '.ood-tag-cmd': {
                    "margin": '0 .125em',
                    "padding": '.1667em',
                    'vertical-align': 'middle'
                },
                '.ood-inline-object': {
                    'vertical-align': 'middle',
                    'margin': '0 .16666667em'
                }
            });

        ood.UI.$cache_css_after += ood.UI.buildCSSText({
            '.ood-css-innerimage': {
                'vertical-align': 'middle'
            },
            '.ood-uitem-split': {
                display: 'block',
                position: 'relative',
                overflow: 'visible',
                'white-space': 'nowrap',
                margin: '2px 0',
                padding: 0
            },
            '.ood-css-noscroll, .ood-css-noscroll body': {
                overflow: 'hidden',
                'overflow-x': 'hidden',
                'overflow-y': 'hidden'
            },
            '.ood-css-noscrollx, .ood-css-noscroll body': {
                'overflow-x': 'hidden'
            },
            '.ood-css-noscrolly, .ood-css-noscroll body': {
                'overflow-y': 'hidden'
            },
            '.ood-css-dockparent': {
                overflow: 'hidden'
            },
            '.ood-ui-dirty': {
                $order: 1,
                'background-image': 'url(data:image/gif;base64,R0lGODlhBwAHAPcAAAAAADDSEwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAP8ALAAAAAAHAAcAAAgYAAMIHPhvoMB/BQkiVLgwAMKHDh9KnBgQADs=)',
                'background-repeat': 'no-repeat',
                'background-position': 'left top'
            },
            '.ood-float-clear': {
                'clear': 'both'
            },
            '.ood-float-left': {
                'float': 'left'
            },
            '.ood-float-right': {
                'float': 'right'
            },
            /*
            ".ood-ui-diry:after":{
                content: "";
                position: 'absolute',
                top: 0,
                left: 0,
                width: 0,
                height: '1px',
                'z-index':100,
                'border-top': '8px solid #f44',
                'border-right': '8px solid transparent'
            },*/
            '.ood-ui-ellipsis': {
                "max-width": "100%",
                "text-overflow": "ellipsis",
                "white-space": "nowrap",
                "overflow": "hidden"
            },
            '.ood-ui-ruler': {
                width: 0,
                height: '1.22em',
                'vertical-align': 'middle'
            },
            '.ood-nodatauri .ood-ui-dirty': {
                $order: 2,
                'background-image': ood.UI.$oldBg('dirtymark.gif', 'no-repeat left top')
            },
            // Firefox will ignore input:read-only
            '.ood-ui-ctrl-readonly, .ood-node-readonly, input[readonly], textarea[readonly], input:read-only, textarea:read-only, .ood-ui-readonly, .ood-ui-itemreadonly, .ood-ui-readonly .ood-node, .ood-ui-itemreadonly .ood-node, ood-ui-inputreadonly input, ood-ui-inputreadonly textarea': {
                $order: 2,
                color: 'var( --text-input-readonly)'
            },
            'button::-moz-focus-inner, input::-moz-focus-inner': {
                padding: 0,
                border: 0
            },
            '.ood-ui-ctrl-disabled, .ood-node-disabled, button:disabled, a:disabled, input:disabled, textarea:disabled,  .ood-ui-disabled,  .ood-ui-itemdisabled,  .ood-ui-disabled .ood-node, .ood-ui-itemdisabled .ood-node, .ood-uicell-disabled, .ood-uicell-disabled .ood-node': {
                $order: 2,
                cursor: 'not-allowed',
                color: '#808080 '
            },
            '.ood-ui-ctrl-disabled, .ood-node-disabled, button:disabled, a:disabled, input:disabled, textarea:disabled,  .ood-ui-disabled input,.ood-ui-disabled textarea, .ood-ui-itemdisabled input, .ood-ui-itemdisabled textarea, .ood-uicell-disabled': {
                $order: 3,
                'background-color': '#eee '
            },
            '.ood-ui-invalid, .ood-ui-invalid .ood-node': {
                $order: 1,
                'background-color': '#FFEBCD !important'
            },
            '.ood-item-row': {
                display: "block",
                'white-space': 'nowrap'
            },
            '.ood-item-cell': {
                display: ood.$inlineBlock,
                zoom: ood.browser.ie ? 1 : null,
                'white-space': 'normal'
            },
            ".ood-required": {
                "color": "#ff0000 !important"
            },
            '.ood-alert': {
                'background-color': '#ff6600 !important'
            },
            ".ood-uisyle-mobile": {
                "background-clip": "padding-box",
                border: "10px solid #333333",
                "border-radius": "12px",
                "box-shadow": "0 0 15px rgba(0, 0, 0, 0.28), 0 1px 1px rgba(255, 255, 255, 0.45) inset, 0 0 2px rgba(255, 255, 255, 0.2) inset"
            },
            ".ood-uisyle-mobile, .ood-uisyle-mobile *, .ood-cursor-touch, .ood-cursor-touch *": {
                cursor: 'url(' + ood.ini.img_touchcur + ') 8 8,auto!important'
            },
            '.ood-icon-loading': {
                $order: 7,
                width: (ood.browser.ie && ood.browser.ver <= 8) ? '1em' : null,
                height: (ood.browser.ie && ood.browser.ver <= 8) ? '1em' : null,
                'background-image': (ood.browser.ie && ood.browser.ver < 10) ? 'url(' + ood.ini.img_busy + ')' : null,
                "background-repeat": (ood.browser.ie && ood.browser.ver < 10) ? "no-repeat" : null,
                "background-position": (ood.browser.ie && ood.browser.ver < 10) ? "center center" : null
            },
            ".ood-icon-loading:before": {
                $order: 7,
                visibility: (ood.browser.ie && ood.browser.ver < 10) ? 'hidden' : null
            },
            ".ood-load-error": {
                width: (ood.browser.ie && ood.browser.ver <= 8) ? '1em' : null,
                height: (ood.browser.ie && ood.browser.ver <= 8) ? '1em' : null,
                "background-image": (ood.browser.ie && ood.browser.ver <= 8) ? "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAQAAAC1+jfqAAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAAEgAAABIAEbJaz4AAAAJdnBBZwAAABAAAAAQAFzGrcMAAADdSURBVCjPfdGxTsJQGIbhpw20GgN1IZQBDQnGxvu/FEU3IjEBB5SKUsGkdTiCYeGb/pw803uixum1iMJ1KZdp40dpYQWNqBERuXItRY1YrPJspmm0wNAYlZkvdPR1jTVmtJAZgUhliaWFQs9IqYwxkKhUUnf64NuTD6kBMTK8mKgkigNZaGQBJGqfVh6PyFotCWC/NxMbiUL+/xhjJ9Y5kEriVk9HbBdAKZI7A+8ebLQVhigDmNvqKg7k3kZbamceOpSmbvSkXq1xIfSfKh2lPlf/pWa7Tx3A6c86vV+v4FNOkQDWwAAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxMC0wMi0xMVQxMTo1MDowOC0wNjowMNYQZfsAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMDYtMDUtMDVUMTM6MjI6NDAtMDU6MDC/5P4aAAAAAElFTkSuQmCC)" : null,
                "background-repeat": (ood.browser.ie && ood.browser.ver <= 8) ? "no-repeat" : null,
                "background-position": (ood.browser.ie && ood.browser.ver <= 8) ? "center center" : null
            },
            ".ood-load-error:before": {
                $order: 7,
                visibility: (ood.browser.ie && ood.browser.ver <= 8) ? 'hidden' : null
            },
            ".ood-ui-clear": {
                $order: 10,
                border: 'none',
                background: 'none'
            }
        });
    },
    $End: function () {
        var hash = {}, keys = this.$Keys;
        ood.filter(this.getAppearance(), function (o, i) {
            var arr1 = i.split(/\s*,\s*/), arr2;
            for (var l = arr1.length - 1; l >= 0; l--) {
                arr2 = arr1[l].match(/[A-Z][A-Z0-9]*/g);
                if (arr2 && arr2.length) {
                    for (var j = 0, m = arr2.length; j < m; j++) {
                        if (!keys[arr2[j]]) {
                            arr1.splice(l, 1);
                            break;
                        }
                    }
                }
            }
            if (arr1.length) hash[arr1.join(", ")] = o;
        });
        this.setAppearance(hash);
        ood.UI.$cache_css_before += this.buildCSSText(this.$Appearances);
    },
    Static: {
        $cache_css_before: '',
        $cache_css_after: '',
        $css_tag_dirty: "ood-ui-dirty",
        $css_tag_invalid: "ood-ui-invalid",
        $tag_left: "{",
        $tag_right: "}",
        $tag_subId: "_serialId",
        $tag_subId_c: "_serialId_c",

        $x01: /\x01/img,
        $x01r: / \x01 /img,

        $tag_special: '\x01',
        $ID: "\x01id\x01",
        $DOMID: '\x01domid\x01',
        $CLS: "\x01cls\x01",
        $MODULECLS: "\x01modulecls\x01",
        $TAGCLASS: "\x01tagcls\x01",
        $childTag: "<!--\x03{id}\x04-->",

        $ps: {left: 1, top: 1, width: 1, height: 1, right: 1, bottom: 1},
        objectProp: {},
        $toDom: function (profile, str, addEventHandler) {
            if (addEventHandler === false)
                return ood.str.toDom(str);

            //must use empty div for RenderTriggers
            var matrix = ood.Dom.getEmptyDiv(profile.$inDesign).get(0), r = [], style = matrix.style;
            // for control size
            style.position = 'relative';
            style.display = 'none';
            matrix.innerHTML = str;
            //add event handlers
            this.$addEventsHandler(profile, matrix);
            for (var i = 0, t = matrix.childNodes, l = t.length; i < l; i++) {
                //ensure the root nodes
                ood.$registerNode(t[i]);
                r[r.length] = t[i].$xid;
            }
            style.display = '';
            matrix = null;
            return ood(r, false);
        },
        $evtsindesign: {
            "onload": 1,
            "onerror": 1,
            "onscroll": 1,
            "onunload": 1,
            "onsize": 1,
            "onmousedown": 1,
            "onmouseup": 1
        },
        _handleEventConf: function (conf, args) {
            var ns = this;
        },
        $addEventsHandler: function (profile, node, includeSelf) {
            var ch = ood.$cache.UIKeyMapEvents,
                event = ood.Event,
                eh = event._eventHandler,
                hash = this.$evtsindesign,
                handler = event.$eventhandler,
                children = ood.toArr(node.getElementsByTagName('*')),
                i, l, j, k, id, t, v;

            if (includeSelf)
                children.push(node);
            if (l = children.length) {
                for (i = 0; i < l; i++) {
                    if ((node = children[i]).nodeType != 1) continue;
                    if (id = node.id) {
                        if (t = ch[id] || ch[id.substr(0, id.indexOf(':'))]) {
                            v = ood.$registerNode(node);
                            v = v.eHandlers || (v.eHandlers = {});
                            for (j in t) {
                                if (profile.$inDesign && !hash[j]) continue;
                                //attach event handler to domPurgeData
                                v[j] = t[j];
                                //attach event handler to dom node
                                if (k = eh[j]) {
                                    v[k] = t[j];
                                    event._addEventListener(node, k, t[j]);
                                    if (ood.browser.isTouch && k == 'onmousedown') {
                                        v['onoodtouchdown'] = handler;
                                        event._addEventListener(node, "oodtouchdown", handler);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            children.length = 0;
            node = t = null;
        },
        setDftProp: function (prop) {
            this.__resetDftProp = prop;
            return this;
        },
        getFromDom: function (id) {
            if (id = ood.UIProfile.getFromDom(id))
                return id.boxing();
        },
        _ensureValues: function (arr) {
            var a = [], i = 0, k = 0, o, key = this.KEY, cache = ood.$cache.profileMap, getData = ood.getNodeData;
            if (arr['ood.absBox']) arr = arr._nodes;
            for (; o = arr[i++];)
                if ((o.box && o.box[key]) || ((o = cache[getData(o.renderId ? o.renderId : o, ['element', 'id'])]) && o.box && o.box[key]))
                    a[k++] = o;
            return a.length <= 1 ? a : this._unique(a);
        },

        __gc: function () {
            var self = this, k = self.$key, cache = ood.$cache;
            //clear templates memory in ood.$cache
            ood.breakO([cache.template[k], cache.reclaimId[k], self._cache, self._idCache, self.$DataModel, self.$Templates, self.$Behaviors, self], 2);
            delete ood.absBox.$type[k.replace("ood.UI.", "")];
            delete ood.absBox.$type[k];
            ood.filter(ood.$cache.UIKeyMapEvents, function (o, i) {
                return !(i == k || i.indexOf(k + '-') == 0);
            });
            // add for base class
            ood.Class.__gc(k);
        },
        _pickSerialId: function () {
            //get id from cache or id
            var arr = ood.$cache.reclaimId[this.$key];
            if (arr && arr[0]) return arr.pop();
            return this._ctrlId.next();
        },
        $oldBg: function (path, paras, forceKey, root) {
            return function (key) {
                //ood.asyRun(function(){new Image().src=p;});
                return 'url(' + ood.ini.path + 'appearance/_oldbrowser/' + path + ') ' + (paras || '');
            }
        },
        $ieOldBg: function (path) {
            return function (key) {
                //ood.asyRun(function(){new Image().src=p;});
                return 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' + ood.ini.path + 'appearance/_oldbrowser/' + path + '",sizingMethod="crop")';
            }
        },
        /* deep template function
           template: string
           properties: hash

           $doTemplate("{a}{b}{c}{a}{b}{/c}", {a:'*',b:'#',c:[{a:'1',b:'.'},{a:'2',b:'.'},{a:'3',b:'.'},{a:'4',b:'.'}]})
               will return "*#1.2.3.4."
           doTemplate("{a}{b}{c}{}{/c}", {a:'*',b:'#',c:['1','2','3','4']})
               will return "*#1234"

           flag: default flase => no clear not mactched symbols
         */
        $doTemplate: function (profile, template, properties, tplTag, result, index, realTag) {
            var self = ood.UI.$doTemplate,
                s, t, n,
                x01 = ood.UI.$x01,
                x01r = ' \x01 ',
                str = '',
                isA = ood.isArr(properties),
                // this one maybe a fake tamplate tag, for switch function
                temp = template[tplTag || ''],
                r = !result,
                result = result || [];
            // get the real tag
            tplTag = realTag || tplTag;
            if (isA) {
                if (typeof temp != 'function') temp = self;
                for (var i = 0; t = properties[i++];) {
                    if (false === temp(profile, template, t, tplTag, result, i)) {
                        break;
                    }
                }
            } else {
                if (t = properties['object']) {
                    //[properties] is for ood.Template
                    result[result.length] = t.toHtml();
                } else {
                    if (typeof temp == 'function') {
                        t = temp(profile, template, properties, tplTag, result);
                        if (t) tplTag = t;
                    } else {
                        tplTag = tplTag ? tplTag + '.' : '';
                        var a0 = temp[0], a1 = temp[1];
                        for (var i = 0, l = a0.length; i < l; i++) {
                            if (n = a1[i]) {
                                if (n in properties) {
                                    t = properties[n];
                                    if (n.substr(0, 4) == "_fi_") {
                                        // for ie67 fonticon text
                                        if (ood.__iefix2 && (a0[i - 1] == "_the_next_is_fonticon_" || n == "_fi_")) {
                                            t = ood.str.trim(t).split(/\s+/).pop();
                                            t = (t in ood.__iefix2) ? ood.__iefix2[t] : str;
                                        } else if (n == "_fi_") {
                                            t = str;
                                        }
                                    }
                                    //if sub template exists
                                    if (template[s = tplTag + n] && t)
                                        self(profile, template, t, s, result);
                                    else
                                        result[result.length] = (t === undefined || t === null || t === NaN) ? str : typeof t == 'string' ? t.replace(x01, x01r) : t;
                                }
                            } else
                                result[result.length] = (a0[i] === undefined || a0[i] === null || a0[i] === NaN) ? str : a0[i];
                        }
                    }
                }
            }
            if (r) return result.join('');
        },
        /*
        set properties default map and set properties handler
        It's a merge function, not replace

        this.$DataStruct: {a:,b:,c}
        this.$DataModel: from hash, for example:
        hash:{
            key1:{
                ini:xx,
                set:fun..,
                get:fun..,
                action: fun
            },
            key2:null,
            key3:'abc
        }
        */
        $buildTemplate: function (profile, template, key, obj, arr) {
            if (template && (template.tagName + "").toLowerCase() == 'text') {
                arr[arr.length] = template.text;
                return;
            }
            var self = arguments.callee,
                behavior = profile.behavior ? key ? profile.behavior[key] : profile.behavior : null,
                prop = profile.properties,
                map1 = self.map1 || (self.map1 = {tagName: 1, text: 1}),
                map2 = self.map2 || (self.map2 = {image: 1, input: 1, br: 1, meta: 1, hr: 1, abbr: 1, embed: 1}),
                map3 = self.map3 || (self.map3 = {input: 1, textarea: 1, pre: 1, code: 1}),
                r2 = self.r2 || (self.r2 = /[a-z]/),
                r3 = self.r3 || (self.r3 = /^(on|before|after)/),
                r7 = self.r7 || (self.r7 = /([^{}]*)\{([\w]+)\}([^{}]*)/g),
                first = false,
                u = ood.UI,
                ts = u.$tag_special,
                t, o, bak, tagN, cls1, lkey;

            if (!template) template = profile.template;
            lkey = key ? profile.keys[key] : profile.key;

            //tagName
            if (!template.tagName) template.tagName = "span";

            if (template.id !== null) {
                //id
                template.id = key ? lkey + ":" + u.$ID + ":" + u.$tag_left + (template.$customId ? u.$tag_subId_c : u.$tag_subId) + u.$tag_right : u.$DOMID;
            } else
                delete template.id;

            if (template.className !== null) {
                //save bak
                bak = template.className || '';
                if (!template._NativeElement) {
                    //className
                    t = u.$CLS + (key ? '-' + key.toLowerCase() : '');
                    tagN = template.tagName.charAt(0) != "{" ? template.tagName.toLowerCase() : template.tagName;
                    cls1 = (tagN == "button" || tagN == "input" || tagN == "textarea" || tagN == "select" || tagN == "keygen") ? 'ood-ui-reset' : '';

                    //default class first
                    template['class'] = 'ood-node' + (cls1 ? (' ' + cls1) : '') + ' ood-node-' + tagN + (t ? (' ' + t) : '') +
                        //custom class here
                        (bak ? (' ' + bak) : '') +
                        (template.$fonticon ? (' ' + template.$fonticon) : '') +
                        //add a special
                        (lkey == profile.key ? (' ood-ui-ctrl ' + ((ood.browser.ie && ood.browser.ver < 10) ? '' : '{_selectable} ')) : '');
                } else {
                    //default class first
                    template['class'] = bak + ' ' +
                        //add a special
                        (lkey == profile.key ? ((ood.browser.ie && ood.browser.ver < 10) ? '' : '{_selectable} ') : '');
                }
                template['class'] += ' ' +
                    //custom style
                    ts + (key || 'KEY') + '_CT' + ts + ' ' +
                    //custom class
                    ts + (key || 'KEY') + '_CC' + ts + ' ' +
                    u.$MODULECLS + ' ' + u.$TAGCLASS + (prop._tagClass ? ' ' + prop._tagClass : '') + " ood-custom"
            }
            delete template.className;

            template.style = (template.style ? (template.style + ';') : '')
                + ts + (key || 'KEY') + '_CS' + ts;

            var a = [], b = {},
                tagName = template.tagName.charAt(0) != "{" ? template.tagName.toLowerCase() : template.tagName,
                text = template.text,
                sc = ood.absObj.$specialChars;

            for (var i in template) {
                if (!template[i]) continue;
                if (!sc[i.charAt(0)] && !map1[i]) {
                    o = template[i];
                    if (!r2.test(i)) {
                        // collect sub node
                        if (typeof o == 'object') {
                            if (!o.$order) o.$order = 0;
                            o.$key = i;
                            a[a.length] = o;
                        }
                    } else
                        b[i] = o;
                }
            }
            // sort sub node
            ood.arr.stableSort(a, function (x, y) {
                x = x.$order || 0;
                y = y.$order || 0;
                return x > y ? 1 : x == y ? 0 : -1;
            });

            //first
            if (!arr) {
                first = true;
                arr = [];
            }
            //<span id="" style="">
            arr[arr.length] = '<' + tagName + ' ';

            for (var i in b)
                if (b[i])
                    arr[arr.length] = i + '="' + b[i] + '" ';

            //set className bak
            if (template.className !== null)
                template.className = bak;

            delete template['class'];

            arr[arr.length] = ts + (key || 'KEY') + '_CA' + ts;
            arr[arr.length] = '>';

            if (!map2[tagName] && text)
                arr[arr.length] = text;
            // for ie67
            if (template.$fonticon && ood.__iefix2) {
                template.$fonticon = ood.str.trim(template.$fonticon);
                if (ood.__iefix2[template.$fonticon]) arr[arr.length] = ood.__iefix2[template.$fonticon];
                else if (/^\s*\{\s*_fi_[\w\s]+\}\s*$/.test(template.$fonticon)) arr[arr.length] = "{_the_next_is_fonticon_}" + template.$fonticon;
            }
            delete template.$fonticon;

            // add sub node
            for (var i = 0, l = a.length; i < l;) {
                o = a[i++];
                self(profile, o, o.$key, obj, arr)
            }
            if (!map2[tagName])
                arr[arr.length] = '</' + tagName + '>';

            if (first) {
                var a0 = obj[0], a1 = obj[1], str = arr.join(''), has = false;
                str.replace(r7, function (a, b, c, d) {
                    if (b) a0[a0.length] = b;
                    a1[a0.length] = a0[a0.length] = c;
                    if (d) a0[a0.length] = d;
                    has = true;
                    return '';
                });
                if (!has) {
                    a0[0] = str;
                }
            }
        },
        _rpt: function (profile, temp) {
            var me = arguments.callee,
                host = profile.host,
                moduleCls = (host && host['ood.Module'] && host.customStyle && !ood.isEmpty(host.customStyle)) ? (" ood-module-" + host.$xid) : null,
                ui = ood.UI,
                tag = ui.$tag_special,
                ca = function (h, s, i) {
                    s = "";
                    for (i in h) s += (i + '="' + h[i] + '" ');
                    return s;
                },
                r = me._r || (me._r = new RegExp(tag + '([0-9A-Z_]+)_C([CTA])' + tag + '|' + tag + '([\\w_\\-\\.]*)' + tag, 'img')),
                h1 = {
                    id: profile.serialId,
                    cls: profile.getClass('KEY'),
                    domid: profile.$domId,
                    modulecls: moduleCls,
                    tagcls: (profile.tagcls || '')
                },
                h2 = {
                    A: profile.CA,
                    C: profile.CC,
                    T: profile._CT
                };
            return temp.replace(r, function (a, b, c, d) {
                return h1[d] || (h2[c] ? (c == "A" ? ca(h2[c][b]) : (h2[c][b] || "")) : '');
            }).replace(ui.$x01r, '\x01');
        },
        _build: function (profile, data) {
            var template, t, m,
                u = ood.UI,
                temp = [[], []],
                self = this,
                key = self.KEY,
                cache = ood.$cache.template,
                hash = profile._hash =
                    'b:' + (profile.template._subid || '') + ';' +
                    '!' + (profile._exhash || '');

            //build custom theme hash here
            if (typeof profile.theme == "string") {
                var h = profile._CT = {},
                    pre = profile.key.replace(/\./g, '-').toLowerCase().replace('ood-ui', 'ood') + "-";
                ood.each(profile.keys, function (o, i) {
                    if (i.charAt(0) != '$')
                        h[i] = pre + profile.theme + "-" + i.toLowerCase();
                });
            }
            //get template
            if (!(template = ood.get(cache, [key, hash]))) {

                //get main template
                u.$buildTemplate(profile, null, null, temp);
                //split sub template from main template

                //set main template
                ood.set(cache, [key, hash, ''], temp);
                //set sub template
                if (t = profile.template.$submap)
                    for (var i in t) {
                        if (typeof (m = t[i]) != 'function') {
                            var temp = [[], []];
                            for (var j in m)
                                if (typeof m[j] == 'object')
                                    u.$buildTemplate(profile, m[j], j, temp);
                            m = temp;
                        }
                        ood.set(cache, [key, hash, i], m);
                    }

                template = ood.get(cache, [key, hash]);
            }
            if (!template) return '';

            //replace main template
            return self._rpt(profile, u.$doTemplate(profile, template, data));
        },
        /*
        allow function input, for some css bug
        */
        _setDefaultBehavior: function (hash) {
            var self = this,
                me = arguments.callee,
                map = me._m || (me._m = {'': 1, KEY: 1, $key: 1}),
                f = me._f1 || (me._f1 = function (arr, type, mode) {
                    var fun = function (profile, e, src) {
                        var t,
                            id = ood.use(src).id(),
                            item,
                            cid = profile.getSubId(id),
                            prop = profile.properties, nodes, funs, box;
                        if (prop.disabled || prop.readonly) return;
                        item = profile.SubSerialIdMapItem && profile.SubSerialIdMapItem[cid];
                        if (item && item.disabled) return;
                        if (item && item.readonly) return;
                        switch (typeof arr) {
                            case 'string':
                                nodes = profile.getSubNode(arr, cid)._get();
                                break;
                            case 'function':
                                funs = [arr];
                                break;
                            case 'object':
                                nodes = [];
                                funs = [];
                                for (var o, i = 0, l = arr.length; i < l; i++) {
                                    o = arr[i];
                                    if (typeof o == 'string')
                                        nodes.push.apply(nodes, profile.getSubNode(o, cid)._get());
                                    else
                                        funs.push(o);
                                }
                        }

                        if (nodes && nodes.length) {
                            nodes = ood(nodes);
                            box = profile.boxing();
                            if (mode == 1) {
                                if (/*!ood.browser.fakeTouch &&*/ ood.browser.deviceType != 'touchOnly' && type == 'mouseover') {
                                    if (profile.$beforeHover && false == profile.$beforeHover(profile, item, e, src, 'mouseover'))
                                        return;
                                    if (prop.disableHoverEffect === true) return;
                                    if (prop.disableHoverEffect && (new RegExp("\\b" + profile.getKey(src, true) + "\\b")).test(prop.disableHoverEffect || "")) return;
                                    if (profile.beforeHoverEffect && false === box.beforeHoverEffect(profile, item, e, src, 'mouseover'))
                                        return;
                                }
                                if (type == 'mousedown') {
                                    if (profile.$beforeClick && false == profile.$beforeClick(profile, item, e, src, 'mousedown'))
                                        return;
                                    if (prop.disableClickEffect)
                                        return;
                                    if (profile.beforeClickEffect && false === box.beforeClickEffect(profile, item, e, src, 'mousedown'))
                                        return;
                                }

                                //default action
                                nodes.tagClass('-' + ((/*!ood.browser.fakeTouch &&*/ ood.browser.deviceType != 'touchOnly') && type == 'mouseover' ? 'hover' : type == 'mousedown' ? 'active' : type));
                            } else {
                                if (type == 'mouseup') {
                                    if (profile.$beforeClick && false == profile.$beforeClick(profile, item, e, src, 'mouseup'))
                                        return;
                                    if (prop.disableClickEffect)
                                        return;
                                    if (profile.beforeClickEffect && false === box.beforeClickEffect(profile, item, e, src, 'mouseup'))
                                        return;
                                    nodes.tagClass('-active', false);
                                } else {
                                    if (profile.$beforeHover && false == profile.$beforeHover(profile, item, e, src, 'mouseout'))
                                        return;
                                    if (prop.disableHoverEffect === true) return;
                                    if (prop.disableHoverEffect && (new RegExp("\\b" + profile.getKey(src, true) + "\\b")).test(prop.disableHoverEffect || "")) return;

                                    if (profile.beforeHoverEffect && false === box.beforeHoverEffect(profile, item, e, src, 'mouseout'))
                                        return;
                                    nodes.tagClass('(-hover|-active)', false);
                                }
                            }
                        }
                        if (funs && funs.length) {
                            ood.arr.each(funs, function (o) {
                                ood.tryF(o, [profile], profile)
                            });
                            funs.length = 0;
                        }
                    };
                    return fun;
                }),
                hls = {}, t;
            if (!ood.SC.get('ood.absContainer'))
                ood.Class('ood.absContainer', 'ood.absObj', {
                    Instance: {
                        addPanel: function (paras, children, item) {
                            var pro = ood.clone(ood.UI.Panel.$DataStruct, true);
                            ood.merge(pro, paras, 'with');
                            ood.merge(pro, {
                                dock: 'fill',
                                tag: paras.tag || paras.id
                            }, 'all');

                            var pb = new ood.UI.Panel(pro), arr = [];
                            this.append(pb, item && item.id);
                            ood.arr.each(children, function (o) {
                                arr.push(o[0]);
                            });
                            pb.append(ood.UI.pack(arr, false));
                            return this;
                        },
                        removePanel: function () {
                            this.destroy(true);
                        },
                        getPanelPara: function () {
                            return ood.clone(this.get(0).properties, true);
                        },
                        dumpContainer: function (subId, purgeNow) {
                            return this.each(function (profile) {
                                var dm = profile.box.$DataModel,
                                    s = dm.valueSeparator || ";",
                                    p = profile.properties,
                                    hasitems = 'items' in p,
                                    b, id, arr, con;
                                if (!hasitems) {
                                    if (con = profile.boxing().getContainer()) con.html("", true, false, purgeNow);
                                } else {
                                    ood.arr.each(p.items, function (item) {
                                        id = item.id;
                                        if (!subId || subId === true) {
                                            b = 1;
                                        } else {
                                            arr = ood.isArr(subId) ? subId : (subId + "").split(s);
                                            b = ood.arr.indexOf(arr, id) != -1;
                                        }
                                        if (b) {
                                            if (con = profile.boxing().getContainer(id)) con.html("", true, false, purgeNow);
                                        }
                                    });
                                }
                            });
                        },
                        getPanelChildren: function () {
                            return this.get(0).children;
                        },
                        getFormValues: function (dirtiedOnly, subId, penetrate, withCaption, withCaptionField) {
                            var hash = {};
                            this.getFormElements(false, subId, penetrate).each(function (prf) {
                                var p = prf.properties, key = p.name || prf.alias, keys,
                                    ins = prf.boxing(),
                                    // maybe return array
                                    uv = ins.getUIValue();
                                // v and uv can be object(Date,Number)
                                if (!dirtiedOnly || (uv + " ") !== (ins.getValue() + " ")) {
                                    if (ins.getCaption && (withCaption || withCaptionField)) {
                                        if (withCaptionField && key.indexOf(":") != -1) {
                                            keys = key.split(':');
                                        }
                                        if (keys && keys[0] && keys[1]) {
                                            hash[keys[0]] = uv;
                                            hash[keys[1]] = ins.getCaption();
                                        } else if (withCaption) {
                                            hash[key] = {
                                                value: uv,
                                                caption: ins.getCaption()
                                            };
                                        } else {
                                            hash[key] = uv;
                                        }
                                    } else {
                                        if (!hash[key.split(':')[0]]) {
                                            hash[key.split(':')[0]] = uv;
                                        } else {
                                            hash[key.split(':')[0]] = uv;
                                        }

                                    }
                                }
                            });


                            return hash;
                        },
                        setFormValues: function (values, subId, penetrate) {
                            if (!ood.isEmpty(values)) {
                                this.getFormElements(false, subId, penetrate).each(function (prf) {
                                    var prop = prf.properties, ins = prf.boxing(), key = prop.name || prf.alias, keys,
                                        cap;
                                    if (typeof(ins.setCaption) == "function" && key.indexOf(":") != -1) {
                                        keys = key.split(':');
                                        if (keys && keys[0] && keys[1]) {
                                            key = keys[0];
                                            cap = keys[1];
                                        }
                                    }
                                    var v = values[key], b = ood.isHash(v);
                                    if ('value' in prop && key in values) {
                                        ins.setValue((b && ('value' in v)) ? v.value : v, true, 'form');
                                    }
                                    if (typeof(ins.setCaption) == "function") {
                                        if (cap in values)
                                            ins.setCaption(values[cap], null, true, 'form');
                                        else if (b && ('caption' in v))
                                            ins.setCaption(v.caption, null, true, 'form');
                                    }
                                });
                            }
                            return this;
                        },
                        getFormElements: function (dirtiedOnly, subId, penetrate) {
                            var a = this.getChildren(subId, penetrate !== false),
                                elems = ood.absValue.pack(a);
                            ood.filter(elems._nodes, function (prf) {
                                return prf.box._isFormField ? prf.box._isFormField(prf) : !!ood.get(prf, ['properties', 'isFormField']);
                            });
                            if (dirtiedOnly) {
                                var arr = [], ins, t;
                                elems.each(function (p, z) {
                                    ins = p.boxing();
                                    if ((ins.getUIValue() + " ") !== (ins.getValue() + " ")) {
                                        arr.push(p);
                                    }
                                });
                                return ood.absValue.pack(arr);
                            }
                            return elems;
                        },
                        isDirtied: function (subId, penetrate) {
                            var elems = this.getFormElements(false, subId, penetrate).get();
                            for (var i = 0, l = elems.length; i < l; i++) {
                                var profile = elems[i], ins;
                                if (profile.box["ood.absValue"]) {
                                    ins = profile.boxing();
                                    if ((ins.getUIValue() + " ") !== (ins.getValue() + " ")) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        },
                        checkValid: function (ignoreAlert, subId, penetrate) {
                            var ns = this, profile = ns.get(0), result = true;
                            // check required first
                            if (!ns.checkRequired(ignoreAlert, subId, penetrate)) {
                                return false;
                            }
                            ns.getFormElements(false, subId, penetrate).each(function (prf) {
                                var prop = prf.properties, ins = prf.boxing();
                                if (!ins.checkValid(ignoreAlert, subId, penetrate)) {
                                    if (!ignoreAlert) {
                                        if (!profile.beforeInputAlert || false !== profile.boxing().beforeInputAlert(profile, prf, 'invalid')) {
                                            ood.alert('$inline.invalid', ood.getRes('$inline.invalid2') + (prop.labelCaption ? (" : " + prop.labelCaption) : prop.name), function () {
                                                if (prf && prf.renderId)
                                                    ins.activate();
                                            });
                                        }
                                        return result = false;
                                    }
                                    result = false;
                                }
                            });
                            return result;
                        },
                        checkRequired: function (ignoreAlert, subId, penetrate) {
                            var profile = this.get(0), result = true;
                            this.getFormElements(false, subId, penetrate).each(function (prf, i) {
                                var prop = prf.properties, ins = prf.boxing();
                                if (prop.required && (!(i = ins.getUIValue())) && i !== 0) {
                                    if (!ignoreAlert) {
                                        if (!profile.beforeInputAlert || false !== profile.boxing().beforeInputAlert(profile, prf, 'required')) {
                                            ood.alert('$inline.required', ood.getRes('$inline.required2') + (prop.labelCaption ? (" : " + prop.labelCaption) : ""), function () {
                                                if (prf && prf.renderId)
                                                    ins.activate();
                                            });
                                        }
                                        return result = false;
                                    }
                                    result = false;
                                }
                            });
                            return result;
                        },
                        formClear: function (subId, penetrate) {
                            return this.each(function (prf) {
                                prf.boxing().getFormElements(false, subId, penetrate).resetValue(null);
                            });
                        },
                        formReset: function (subId, penetrate) {
                            return this.each(function (prf) {
                                var p = prf.properties,
                                    elems = prf.boxing().getFormElements(false, subId, penetrate);
                                if (prf.beforeFormReset && false === prf.boxing().beforeFormReset(prf, elems, subId, penetrate)) {
                                    return;
                                }
                                elems.each(function (p, i) {
                                    if ((i = p.properties.value) !== p.properties.$UIvalue)
                                        p.boxing().resetValue(i);
                                });
                                if (prf.afterFormReset) {
                                    prf.boxing().afterFormReset(prf, elems, subId, penetrate);
                                }
                            });
                        },
                        updateFormValues: function (subId, penetrate) {
                            this.getFormElements(false, subId, penetrate).updateValue();
                        },
                        formSubmit: function (ignoreAlert, subId, penetrate, withCaption, withCaptionField) {
                            var ns = this;
                            // check valid first
                            if (!ignoreAlert && !ns.checkValid(false, subId, penetrate)) {
                                return;
                            }
                            var prf = ns.get(0),
                                p = prf.properties, f,
                                data = ns.getFormValues(false, subId, penetrate, withCaption, withCaptionField),
                                apicaller;
                            // call before event
                            if (prf.beforeFormSubmit && false === prf.boxing().beforeFormSubmit(prf, data, subId, penetrate, withCaption, withCaptionField)) {
                                return;
                            }

                            if (p.formTarget == "Alert") {
                                data = ood.stringify(data);
                                if (ood.Coder && ood.Coder.formatText)
                                    data = ood.Coder.formatText(data);
                                alert(data);
                            } else if (/^\s*\{[^}]+\}\s*$/.test(p.formTarget)) {
                                f = ood.adjustVar(p.formTarget);
                                f(data);
                            } else if (/^((\s*function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)(\{([^\{\}]*)\}))\s*$/.test(p.formTarget)) {
                                if (f = ood.unserialize(p.formTarget)) {
                                    f(data);
                                }
                            }
                            else {
                                // try to get APICaller
                                if (ood.APICaller && ood.arr.indexOf(['_blank', '_self', '_parent', '_top'], p.formTarget) == -1) {
                                    apicaller = ood.APICaller.getFromName(p.formTarget);
                                }
                                if (apicaller) {
                                    apicaller.setQueryData(data, p.formDataPath);
                                    apicaller.invoke();
                                } else {
                                    ood.Dom.submit(p.formAction, data, p.formMethod, p.formTarget, p.formEnctype);
                                }
                            }
                            // update UI
                            ns.getFormElements(dirtiedOnly, subId, penetrate).updateValue();

                            if (prf.afterFormSubmit) prf.boxing().afterFormSubmit(prf, data, subId, penetrate, withCaption, withCaptionField);
                        },
                        // use refrence to keep the Class's function mark
                        _e1: function (profile, item, e, src, type) {
                        },
                        _e2: function (profile, keyboard, e, src) {
                        },
                        _e3: function (profile, e, shiftKey, src) {
                        },
                        _e4: function (profile, e, src, dragKey, dragData, item) {
                        },
                        _e5: function (profile, e, src) {
                        },
                        _e6: function (profile, ctrlPrf, type) {
                        },
                        _e7: function (profile, elems, subId) {
                        },
                        _e8: function (profile, data, subId) {
                        }
                    },
                    Static: {
                        $abstract: true,
                        DataModel: {
                            dragKey: '',
                            dropKeys: '',
                            overflow: {
                                ini: ood.browser.deviceType == "touchOnly" ? 'auto' : undefined,
                                combobox: ['', 'visible', 'hidden', 'scroll', 'auto', 'overflow-x:hidden;overflow-y:auto', 'overflow-x:auto;overflow-y:hidden'],
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        var node = prf.getSubNode(k, true);
                                        if (v) {
                                            if (v.indexOf(':') != -1) {
                                                ood.arr.each(v.split(/\s*;\s*/g), function (s) {
                                                    var a = s.split(/\s*:\s*/g);
                                                    if (a.length > 1) node.css(ood.str.trim(a[0]), ood.str.trim(a[1] || ''));
                                                });
                                                return;
                                            }
                                        }
                                        node.css('overflow', v || '');
                                    });
                                }
                            },
                            panelBgClr: {
                                type: 'color',
                                ini: "",
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        prf.getSubNode(k, true).css('background-color', v);
                                    });
                                }
                            },
                            panelBgImg: {
                                format: 'image',
                                ini: "",
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        prf.getSubNode(k, true).css('background-image', v ? ('url(' + ood.adjustRes(v || '') + ')') : '');
                                    });
                                }
                            },
                            panelBgImgPos: {
                                ini: "",
                                combobox: ["", "top left", "top center", "top right", "center left", "center center", "center right", "bottom left", "bottom center", "bottom right", "0% 0%", "-0px -0px"],
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        prf.getSubNode(k, true).css('background-position', v);
                                    });
                                }
                            },
                            panelBgImgRepeat: {
                                ini: "",
                                combobox: ["", "repeat", "repeat-x", "repeat-y", "no-repeat"],
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        prf.getSubNode(k, true).css('background-repeat', v);
                                    });
                                }
                            },
                            panelBgImgAttachment: {
                                ini: "",
                                combobox: ["", "scroll", "fixed"],
                                action: function (v) {
                                    var prf = this;
                                    ood.arr.each(prf.box.$Behaviors.PanelKeys, function (k) {
                                        prf.getSubNode(k, true).css('background-attachment', v);
                                    });
                                }
                            },
                            conLayoutColumns: {
                                ini: 0,
                                action: function () {
                                    this.adjustSize();
                                }
                            },
                            conDockRelative: {
                                hidden: true,
                                ini: false,
                                get: function () {
                                    return this.boxing().getConLayoutColumns();
                                },
                                set: function (value) {
                                    return this.boxing().setConLayoutColumns(value ? 1 : 0);
                                }
                            },
                            conDockPadding: {
                                ini: {left: 0, top: 0, right: 0, bottom: 0},
                                action: function () {
                                    this.boxing().adjustDock(true);
                                }
                            },
                            conDockSpacing: {
                                ini: {width: 0, height: 0},
                                action: function () {
                                    this.boxing().adjustDock(true);
                                }
                            },
                            conDockFlexFill: {
                                ini: "",
                                combobox: ['none', 'width', 'height', 'both'],
                                action: function () {
                                    this.boxing().adjustDock(true);
                                }
                            },
                            conDockStretch: {
                                ini: "",
                                combobox: ['fixed', 'forward', 'rearward', 'stretch', '0.25', '0.33', '0.5', '0.25,0.5,0.25'],
                                action: function () {
                                    this.boxing().adjustDock(true);
                                }
                            },
                            sandboxTheme: {
                                ini: "",
                                action: function (v, ov, force, tag1, tag2) {
                                    ood.UI._refreshSBTheme(this, v, tag1, tag2);
                                }
                            },
                            formMethod: {
                                ini: 'get',
                                listbox: ['get', 'post']
                            },
                            formTarget: {
                                ini: 'Alert',
                                combobox: ['Alert', '_blank', '_self', '_parent', '_top', '[framename]', '[APICaller]', 'function(d){ood.log(d)}']
                            },
                            formDataPath: "",
                            formAction: "",
                            formEnctype: {
                                ini: 'application/x-www-form-urlencoded',
                                listbox: ['application/x-www-form-urlencoded', 'multipart/form-data', 'text/plain']
                            },
                            readonly: {
                                ini: false,
                                action: function (v) {
                                    this.boxing().getChildren().each(function (prf) {
                                        var ins = prf.boxing();
                                        if (typeof ins.setReadonly == 'function')
                                            ins.setReadonly(v || false);
                                        else if (typeof ins.setDisabled == 'function')
                                            ins.setDisabled(v || false);
                                    });
                                }
                            }
                        }
                    }
                });
            var src = ood.absContainer.prototype;

            if (hash.HoverEffected) {
                ood.each(hash.HoverEffected, function (o, i) {
                    t = map[i] ? hash : (hash[i] || (hash[i] = {}));
                    if (!o)
                        t.afterMouseover = t.afterMouseout = null;
                    else {
                        t.afterMouseover = f(o, 'mouseover', 1);
                        t.afterMouseout = f(o, 'mouseout', 2);
                    }
                });
                hls.beforeHoverEffect = src._e1;
            }
            if (hash.ClickEffected) {
                ood.each(hash.ClickEffected, function (o, i) {
                    t = map[i] ? hash : (hash[i] || (hash[i] = {}));
                    if (!o)
                        t.afterMousedown = t.afterMouseup = null;
                    else {
                        t.afterMousedown = f(o, 'mousedown', 1);
                        t.afterMouseup = f(o, 'mouseup', 2);
                    }
                });
                hls.beforeClickEffect = src._e1;
            }

            if (hash.HotKeyAllowed) {
                //for onHotKey
                ood.merge(hash, {
                    beforeKeydown: function (profile, e, src) {
                        if (profile.onHotKeydown)
                            return false !== profile.boxing().onHotKeydown(profile, ood.Event.getKey(e), e, src);
                    },
                    beforeKeypress: function (profile, e, src) {
                        if (profile.onHotKeypress)
                            return false !== profile.boxing().onHotKeypress(profile, ood.Event.getKey(e), e, src);
                    },
                    beforeKeyup: function (profile, e, src) {
                        if (profile.onHotKeyup)
                            return false !== profile.boxing().onHotKeyup(profile, ood.Event.getKey(e), e, src);
                    }
                });

                hls.onHotKeydown = hls.onHotKeypress = hls.onHotKeyup = src._e2;
            }

            //for focus action
            if (hash.NavKeys) {
                ood.each(hash.NavKeys, function (o, i) {
                    var map = arguments.callee, k, m1 = map.m1 || (map.m1 = {KEY: 1, $key: 1});
                    if (m1[i]) return;
                    var m2 = map.m2 || (map.m2 = {input: 1, textarea: 1}),
                        m3 = map.m3 || (map.m3 = {tab: 1, enter: 1, up: 1, down: 1, left: 1, right: 1}),
                        m4 = map.m4 || (map.m4 = {tab: 1, up: 1, down: 1, left: 1, right: 1}),
                        t = hash[i] || (hash[i] = {});

                    var t = hash[i] || (hash[i] = {});

                    if (null === o)
                        t.afterKeydown = null;
                    else {
                        t.afterKeydown = function (profile, e, src) {
                            var k = ood.Event.getKey(e), key = k.key, ctrl = k.ctrlKey, shift = k.shiftKey,
                                alt = k.altKey, b = false, smartnav = profile._smartnav;
                            if (smartnav) {
                                var node = ood.use(src).get(0);
                                if (m2[k = node.tagName.toLowerCase()]) {
                                    if (key && k == "input" && node.type.toLowerCase() != 'text' && node.type.toLowerCase() != 'password') {
                                        b = true;
                                    } else if (m3[key]) {
                                        var reg = ood.use(src).caret(), txt = ood.use(src).get(0).value;

                                        switch (key) {
                                            case 'up':
                                                if (!/[\n\r]/.test(txt.substr(0, reg[0]))) b = true;
                                                break;
                                            case 'left':
                                                if (!shift && (ctrl || (reg[0] === 0 && (reg[1] !== txt.length || reg[1] === 0)))) b = true;
                                                break;
                                            case 'down':
                                                if (!/[\n\r]/.test(txt.substr(reg[1], txt.length))) b = true;
                                                break;
                                            case 'right':
                                                if (!shift && (ctrl || (reg[1] === txt.length && (reg[0] !== 0 || reg[1] === 0)))) b = true;
                                                break;
                                            case 'enter':
                                                if (k == 'input' || alt) b = true;
                                                break;
                                            case "tab":
                                                b = true;
                                                break;
                                        }
                                    }
                                } else {
                                    if (m4[key])
                                        b = true;
                                }
                                node = null;
                            } else
                                b = key === 'tab';

                            //hanlder focus
                            if (b) {
                                //export event
                                if (profile.beforeNextFocus && false === profile.boxing().beforeNextFocus(profile, e, k.shiftKey, src)) {
                                    // fake a tab key, to envoke onHotKeydown/onHotKeyup
                                    switch (k.key) {
                                        case 'tab':
                                        case 'enter':
                                            if (profile.onHotKeydown) profile.boxing().onHotKeydown(profile, ood.Event.getKey(e), e, src);
                                        case 'esc':
                                            if (profile.onHotKeyup) profile.boxing().onHotKeyup(profile, ood.Event.getKey(e), e, src);
                                            break;
                                    }
                                    return false;
                                }

                                if (smartnav) {
                                    if (key != 'tab')
                                        ood.use(src).nextFocus(('up' == key || 'left' == key) ? false : true);
                                }
                            }
                        }
                    }
                });
                hls.beforeNextFocus = src._e3;
            }
            if ((t = hash.DroppableKeys) && t.length) {
                ood.arr.each(t, function (o) {
                    self._droppable(o)
                });

                t = self.prototype;
                ood.arr.each('getDropKeys,setDropKeys'.split(','), function (o) {
                    if (!t[o]) t[o] = src[o];
                });
                if (hash.PanelKeys) {
                    ood.arr.each('addPanel,removePanel,dumpContainer,getPanelPara,getPanelChildren,getFormValues,setFormValues,getFormElements,isDirtied,checkValid,checkRequired,formClear,formReset,updateFormValues,formSubmit'.split(','), function (o) {
                        if (!t[o]) t[o] = src[o];
                    });
                }
                self.$DataModel.dropKeys = self.$DataStruct.dropKeys = '';
                hls.onDragEnter = hls.onDragLeave = hls.beforeDrop = hls.onDrop = hls.afterDrop = hls.onDropTest = hls.onDropMarkShow = hls.onDropMarkClear = src._e4;
            }
            if ((t = hash.DraggableKeys) && t.length) {
                ood.arr.each(t, function (o) {
                    self._draggable(o)
                });
                t = self.prototype;
                ood.arr.each('getDragKey,setDragKey'.split(','), function (o) {
                    if (!t[o]) t[o] = src[o];
                });
                self.$DataModel.dragKey = self.$DataStruct.dragKey = '';
                hls.onGetDragData = hls.onStartDrag = hls.onDragStop = src._e5;
            }
            if ((t = hash.NoDraggableKeys) && t.length) {
                self.NoDraggableKeys = t;
            }
            if ((t = hash.NoDroppableKeys) && t.length) {
                self.NoDroppableKeys = t;
            }
            if ((t = hash.PanelKeys) && t.length) {
                ood.each(hash.PanelKeys, function (i) {
                    t = map[i] ? hash : (hash[i] || (hash[i] = {}));
                    t.onMousewheel = function (profile, e, src) {
                        var id = ood.use(src).id(),
                            cid = profile.getSubId(id),
                            item = profile.SubSerialIdMapItem && profile.SubSerialIdMapItem[cid];
                        if (profile.onMousewheel)
                            return profile.boxing().onMousewheel(profile, ood.Event.getWheelDelta(e), item, e, src);
                    };
                });

                t = self.prototype;
                ood.arr.each('overflow,panelBgClr,panelBgImg,panelBgImgPos,panelBgImgRepeat,panelBgImgAttachment,conDockRelative,conLayoutColumns,conDockPadding,conDockSpacing,conDockFlexFill,conDockStretch,sandboxTheme,formMethod,formTarget,formDataPath,formAction,formEnctype,readonly'.split(','), function (o) {
                    var f = 'get' + ood.str.initial(o), dm;
                    if (!t[f]) t[f] = src[f];
                    f = 'set' + ood.str.initial(o);
                    if (!t[f]) t[f] = src[f];
                    dm = ood.absContainer.$DataModel[o];
                    if (!(self._NoProp && self._NoProp[o])) {
                        self.$DataStruct[o] = ood.isSet(dm.ini) ? ood.copy(dm.ini) : "";
                        self.$DataModel[o] = ood.copy(dm);
                    }
                });

                ood.merge(hls, ood.absContainer.$EventHandlers);
                // form

                hls.beforeInputAlert = src._e6;
                hls.beforeFormReset = hls.afterFormReset = src._e7;
                hls.beforeFormSubmit = hls.afterFormSubmit = src._e8;
                hls.onMousewheel = function (profile, delta, item, e, src) {
                };

                self['ood.absContainer'] = true;
            }
            self.setEventHandlers(hls);
            self.$RenderTrigger = self.$RenderTrigger || [];
            self.$RenderTrigger.push(function () {
                if (this.properties.readonly) {
                    this.boxing().setReadonly(true, true);
                }
            });

        },

        addTemplateKeys: function (arr) {
            var self = this, key = self.KEY, me = arguments.callee, reg = me._reg || (me._reg = /\./g);
            ood.arr.each(arr, function (i) {
                self.$cssKeys[i] = (self.$Keys[i] = i == 'KEY' ? key : key + "-" + i).replace(reg, '-').toLowerCase().replace('ood-ui', 'ood');
            });
            return self;
        },
        _refreshSBTheme: function (profile, cssSetting, tag, callback, relayout) {
            var domId = profile.getDomId(),
                id = domId + "sandboxtheme",
                // escape special char
                prevId = this._getThemePrevId(profile),
                old = ood(id).get(0),
                applyCss = function (css) {
                    if (profile.destroyed) return;
                    ood.CSS._appendSS(profile.getRootNode(), ood.UI._adjustCSS(css, prevId, tag), id, true);

                    if (relayout || profile.$inDesign) {
                        profile.boxing().reLayout(true)
                            .getChildren(true, "penetrate").reLayout(true);
                    }
                };
            if (old) {
                old.disabled = true;
                ood(id).remove(false);
            }
            if (cssSetting) {
                if (/^[a-zA-Z-]+$/.test(cssSetting + '') && (cssSetting + '') !== 'default') {
                    var path = ood.getPath('ood.appearance.' + cssSetting, '');
                    ood.getFileAsync(path + 'theme.css', function (rsp) {
                        applyCss(rsp.replace(/\.setting-uikey\{[^}]+\}/, '').replace(/url\(([^)]+)\)/g, "url(" + path + "$1)"));
                        ood.tryF(callback, [profile, cssSetting]);
                    });
                } else {
                    applyCss(cssSetting + '');
                }
            }
        },
        _getThemePrevId: function (profile/*UIProfile or dom id*/) {
            return profile ? '#' + (profile['ood.UIProfile'] ? profile.getDomId() : profile).replace(/([.:])/g, "\\$1") : "";
        },
        _adjustCSS: function (css, prevId, tag) {
            prevId = prevId || "";
            if (tag) {
                css = ood.replace(css, [
                    [/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/, '$0'],
                    [/\{[^}]*\}/, '$0'],
                    [/([^\/{},]+)/, function (a) {
                        return a[0].replace(/([^\s>]+)/, "$1" + tag)
                    }]
                ]);
            }
            return css
                .replace(/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/g, '')
                .replace(/^\s*(\.)/, function (a, b) {
                    return prevId + " " + b;
                }).replace(/([},])\s*(\.)/g, function (a, b, c) {
                    return b + "\n" + prevId + " " + c;
                }).replace(/([{;])\s*(.)/g, function (a, b, c) {
                    return b + '\n' + (c == '}' ? '' : '    ') + c;
                }).replace(/url\(([^)]+)\)/g, function (a, b) {
                    return "url(" + ood.adjustRes(b, 0, 1) + ")";
                });
        },
        setAppearance: function (hash) {
            ood.merge(this.$Appearances, hash, 'all');
            return this;
        },
        getAppearance: function () {
            return this.$Appearances;
        },
        /*replace mode*/
        setTemplate: function (hash, cacheId) {
            if (hash) {
                var self = this,
                    tagNames = self.$tagName || (self.$tagName = {}),
                    me = arguments.callee,
                    r2 = me.r2 || (me.r2 = /[a-z]/),
                    sc = ood.absObj.$specialChars,
                    _ks = ['KEY'],
                    fun = me._fun || (me._fun = function (hash, arr, tagNames) {
                        var o, i;
                        for (i in hash) {
                            if (!sc[i.charAt(0)])
                                if (!r2.test(i)) {
                                    arr[arr.length] = i;
                                    o = hash[i];
                                    if (o && typeof o == 'object') {
                                        tagNames[i] = o.tagName || '';
                                        arguments.callee(o, arr, tagNames);
                                    }
                                }
                        }
                        ;
                    })
                    , t;
                tagNames.KEY = hash.tagName || '';
                fun(hash, _ks, tagNames);
                self.addTemplateKeys(_ks);

                t = self.$Templates;

                // for sub template,
                if (typeof cacheId == 'string') {
                    hash._subid = cacheId;
                    t[cacheId] = hash;
                } else
                    t._ = hash;

                //set sub
                if (t = hash.$submap)
                    for (var i in t)
                        for (var j in t[i])
                            me.call(self, t[i], j);
            }
            return this;
        },
        getTemplate: function (cacheId) {
            return this.$Templates[cacheId || '_'];
        },
        /*replace mode*/
        setBehavior: function (hash) {
            if (hash) {
                var self = this,
                    ch = ood.$cache.UIKeyMapEvents,
                    skey = self.$key,
                    check = ood.absObj.$specialChars,
                    event = ood.Event,
                    handler = event.$eventhandler,
                    eventType = event._eventMap,
                    me = arguments.callee,
                    r1 = me.r1 || (me.r1 = /[a-z]/),
                    r2 = me.r2 || (me.r2 = /^(on|before|after)/),
                    t = self.$Behaviors,
                    m, i, j, k, o, v, type;


                //remove all handler cache
                ood.filter(ch, function (o, i) {
                    return !(i == skey || i.indexOf(skey + '-') == 0);
                });

                //set shortcut first
                self._setDefaultBehavior(hash);
                //merge KEY
                if (hash.KEY) {
                    ood.merge(hash, hash.KEY, 'all');
                    delete hash.KEY;
                }

                //merge hash
                for (i in hash) {
                    o = hash[i];
                    if (!check[i.charAt(0)]) {
                        //only two layer
                        if (!r1.test(i)) {
                            m = t[i] || (t[i] = {});
                            for (j in o) {
                                v = o[j];
                                if (!check[j.charAt(0)]) {
                                    /*set to behavior*/
                                    if (v)
                                        m[j] = v;
                                    else
                                        delete m[j];
                                }
                            }
                        } else if (r2.test(i)) {
                            /*set to behavior*/
                            if (o)
                                t[i] = o;
                            else
                                delete t[i];
                            //for those special keys
                        } else
                            t[i] = o;
                    }
                }

                //add handler cache
                for (i in t) {
                    o = t[i];
                    if (!check[i.charAt(0)]) {
                        //only two layer
                        if (!r1.test(i)) {
                            for (j in o) {
                                if (!check[j.charAt(0)] && o[j]) {
                                    k = skey + '-' + i;
                                    ch[k] = ch[k] || {};
                                    ch[k]['on' + eventType[j]] = ch[k]['on' + eventType[j]] || handler;
                                }
                            }
                        } else if (r2.test(i) && o) {
                            k = skey;
                            ch[k] = ch[k] || {};
                            ch[k]['on' + eventType[i]] = ch[k]['on' + eventType[i]] || handler;
                        }
                    }
                }
            }

            return self;
        },
        getBehavior: function () {
            return this.$Behaviors;
        },
        $trytoApplyCSS: function () {
            var self = ood.UI, css = ood.CSS, id = 'ood.UI-CSS', cache1 = self.$cache_css_before,
                cache2 = self.$cache_css_after;
            // only the first time
            if (!self.$cssNo) {
                self.$cssNo = 1;
                var b = ood.browser;
                ood('body').addClass(
                    (b.ie ? ("ood-css-ie ood-css-ie" + b.ver + " ") :
                        b.gek ? ("ood-css-gek ood-css-gek" + b.ver + " ") :
                            b.kde ? ("ood-css-kde ood-css-kde" + b.ver + " ") :
                                b.opr ? ("ood-css-opr ood-css-opr" + b.ver + " ") : "")
                    + (b.isSafari ? "ood-css-safari " : b.isChrome ? "ood-css-chrome " : "")
                    + (b.isMac ? "ood-css-mac" : b.isLinux ? "ood-css-linux " : "")
                );
                ood('html').addClass("ood-css-base ood-css-viewport ood-uicontainer" + (b.isStrict ? " ood-css-strict" : ""));
                css.includeLink(ood.ini.path + "iconfont/iconfont.css", 'ood-font-icon', true);
            }
            // maybe more times for new UI widgets
            if (cache1) {
                css.addStyleSheet(cache1, id + (self.$cssNo++));
                self.$cache_css_before = '';
            }
            // only the first time
            if (cache2) {
                css.addStyleSheet(cache2, id + (self.$cssNo++), true);
                self.$cache_css_after = '';
            }
        },
        buildCSSText: function (hash) {
            var self = this,
                r1 = /(^|\s|,)([0-9A-Z_]+)/g,
                h = [], r = [],
                browser = ood.browser,
                ie6 = browser.ie6,
                ie = browser.ie,
                gek = browser.gek,
                ks = self.$cssKeys,
                t, v, o;

            for (var i in hash) {
                if (o = hash[i]) {
                    t = i.replace(r1, function (a, b, c) {
                        return b + '.' + (ks[c] || c)
                    }).toLowerCase();
                    o.$order = parseInt(o.$order, 10) || 0;
                    o.$ = t;
                    h[h.length] = o;
                }
            }
            ;
            ood.arr.stableSort(h, function (x, y) {
                x = x.$order || 0;
                y = y.$order || 0;
                return x > y ? 1 : x == y ? 0 : -1;
            });

            for (var i = 0, l = h.length; i < l;) {
                o = h[i++];
                r[r.length] = o.$ + "{";
                if (t = o.$before) r[r.length] = t;
                if (t = o.$text) r[r.length] = t;
                for (var j in o) {
                    if (j.charAt(0) == '$') continue;
                    //neglect '' or null
                    if ((v = o[j]) || o[j] === 0) {
                        j = j.replace(/_[0-9]+$/, '');
                        //put string dir
                        switch (typeof v) {
                            case 'string':
                            case 'number':
                                r[r.length] = j + ":" + v + ";";
                                break;
                            case 'function':
                                r[r.length] = j + ":" + v(self.KEY) + ";";
                                break;
                            //arrray
                            default:
                                ood.arr.each(v, function (k) {
                                    //neglect '' or null
                                    if (k) r[r.length] = j + ":" + k + ";";
                                });
                        }
                    }
                }
                if (v = o.$after) r[r.length] = v;
                r[r.length] = "}";
            }
            return r.join('');
        },
        _prepareCmds: function (profile, item, filter) {
            var ns = this,
                p = profile.properties,
                cmds = item.tagCmds || ood.clone(p.tagCmds, true);
            if (cmds && cmds.length) {
                var sid = ood.UI.$tag_subId, a = [], b = [], c;
                for (var i = 0, t = cmds, l = t.length; i < l; i++) {
                    if (typeof t[i] == 'string') c = {id: t[i]};
                    else c = ood.clone(t[i]);

                    if (filter && ood.isFun(filter)) {
                        if (!filter(c)) {
                            continue;
                        }
                    } else {
                        if (item.tag && item.tag.match((new RegExp("\\b" + "no~" + c.id + "\\b"))))
                            continue;
                    }

                    if ('id' in c) c.id += ''; else c.id = 'cmds' + profile.$xid + i;
                    if (c['object']) {
                        c['object'] = ns._prepareInlineObj(profile, c, p.tabindex);
                        c.type = 'profile';
                    } else {
                        if (!'caption' in c) c.caption = c.id;
                        if (!('tips' in c)) c.tips = c.caption;

                        c.id = c.id.replace(/[^0-9a-zA-Z]/g, '');
                        if (!c.buttonType) c.buttonType = "text";
                        if (!c.tagCmdsAlign) c.tagCmdsAlign = "left";
                        if (!c.itemClass) c.itemClass = c.imageClass;
                        if (!c.pos) c.pos = "row";

                        if (c.caption) c.caption = ood.adjustRes(c.caption);
                        if (c.tips) c.tips = ood.adjustRes(c.tips);
                        if (c.image) c.image = ood.adjustRes(c.image) || ood.ini.img_bg;
                        c._style = "";
                        if ('width' in c) c._style += c.width + (ood.isFinite(c.width) && "px") + ";";
                        if ('height' in c) c._style += c.height + (ood.isFinite(c.height) && "px") + ";";
                        c[sid] = (item[sid] ? item[sid] : "") + '_' + c.id;
                    }

                    var tagCmdsAlign = c["location"] || c["tagCmdsAlign"] || 'right';

                    if (tagCmdsAlign == "left")
                        b.push(c);
                    else {
                        a.push(c);
                        if (tagCmdsAlign == 'right-float' || tagCmdsAlign == 'floatright')
                            c._exstyle = 'float:right;';
                    }
                }
                item.ltagCmds = b
                item.rtagCmds = a;
            }
            item._ltagDisplay = item.ltagCmds ? '' : 'display:none';
            item._rtagDisplay = item.rtagCmds ? '' : 'display:none';
        },


        $getTagCmdsTpl: function (key) {

            var tpl = {
                'ltagCmds': function (profile, template, v, tag, result) {
                    var buttonType = v.buttonType || v.type;
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "tagCmds" + (map[buttonType] || '.button'), result)
                },
                'rtagCmds': function (profile, template, v, tag, result) {
                    var buttonType = v.buttonType || v.type;
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "tagCmds" + (map[buttonType] || '.button'), result)
                },
                'tagCmds.text': {
                    CMD: {
                        tagName: "span",
                        title: "{tips}",
                        style: '{_style}{itemStyle}',
                        className: 'ood-node ood-tag-cmd {itemClass}',
                        tabindex: '{_tabindex}',
                        text: "{caption}",
                        tips: "{tips}",
                        alt: "{tips}"
                    }
                },
                'tagCmds.button': {
                    CMD: {
                        tagName: "button",
                        title: "{tips}",
                        style: '{_style}{itemStyle}',
                        className: 'ood-node ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius ood-list-cmd ood-tag-cmd {itemClass}',
                        tabindex: '{_tabindex}',
                        text: "{caption}",
                        alt: "{tips}"
                    }
                },
                'tagCmds.image': {
                    CMD: {
                        title: "{tips}",
                        tagName: "image",
                        src: "{image}",
                        border: "0",
                        style: '{_style}{itemStyle}',
                        className: 'ood-node ood-tag-cmd {itemClass}',
                        tabindex: '{_tabindex}',
                        alt: "{tips}"
                    }
                }
            };
            return key ? tpl['tagCmds.' + key] : tpl;
        },
        _droppable: function (key) {
            var self = this,
                h2 = ood.Event.$eventhandler,
                o = self.$Behaviors,
                v = key == 'KEY' ? o : (o[key] || (o[key] = {})),
                handler = ood.$cache.UIKeyMapEvents,
                k2 = key == 'KEY' ? self.KEY : (self.KEY + '-' + key),
                ch = handler[k2] || (handler[k2] = {});

            //attach Behaviors
            ood.merge(v, {
                beforeMouseover: function (profile, e, src) {
                    var prop = profile.properties;
                    if (prop.disabled || prop.readonly) return;

                    // avoid no droppable keys
                    if (profile.behavior.NoDroppableKeys) {
                        var sk = profile.getKey(ood.Event.getSrc(e).id || "").split('-')[1];
                        if (sk && ood.arr.indexOf(profile.behavior.NoDroppableKeys, sk) != -1) return;
                    }

                    var ns = src, t,
                        dd = ood.DragDrop,
                        pp = dd.getProfile(),
                        key = pp.dragKey,
                        data = pp.dragData,
                        isPanelN = (t = profile.box.$Behaviors.PanelKeys) && ood.arr.indexOf(t, profile.getKey(src, true)) !== -1,
                        item, box, args;

                    //not include the dragkey
                    if (data &&
                        (
                            (prop.dragSortable && profile.$xid == ood.get(data, ['profile', '$xid']) && !isPanelN) ||
                            (key && (new RegExp('\\b' + key + '\\b')).test(profile.box.getDropKeys(profile, ns)))
                        )
                    ) {
                        box = profile.boxing();
                        if (box.getItemByDom)
                            item = box.getItemByDom(src);

                        args = [profile, e, ns, key, data, item];
                        if ((t = profile.onDropTest) && (false === box.onDropTest.apply(box, args)))
                            return;
                        if ((t = profile.box._onDropTest) && (false === t.apply(profile.host || profile, args)))
                            return;
                        //for trigger onDrop
                        dd.setDropElement(src);
                        if (profile.onDropMarkShow && (false === box.onDropMarkShow.apply(box, args))) {
                        }
                        else if ((t = profile.box._onDropMarkShow) && (false === t.apply(profile.host || profile, args))) {
                        }
                        else
                        //show region
                            ood.resetRun('setDropFace', dd.setDropFace, 0, [ns], dd);

                        if (t = profile.box._onDragEnter) t.apply(profile.host || profile, args);
                        if (profile.onDragEnter) box.onDragEnter.apply(box, args);
                        //dont return false, multi layer dd wont work well
                        //return false;
                    }
                },
                beforeMouseout: function (profile, e, src) {
                    if (profile.properties.disabled || profile.properties.readonly) return;
                    var dd = ood.DragDrop,
                        pp = dd.getProfile(),
                        key = pp.dragKey,
                        data = pp.dragData,
                        item, box, args;

                    //not include the dragkey
                    if (pp.dropElement == src) {
                        box = profile.boxing();
                        if (box.getItemByDom)
                            item = box.getItemByDom(src);

                        args = [profile, e, src, key, data, item];
                        if (profile.onDropMarkClear && (false === box.onDropMarkClear.apply(box, args))) {
                        }
                        else if ((t = profile.box._onDropMarkClear) && (false === t.apply(profile.host || profile, args))) {
                        }
                        else ood.resetRun('setDropFace', dd.setDropFace, 0, [null], ood.DragDrop);

                        if (t = profile.box._onDragLeave) t.apply(profile.host || profile, args);
                        if (profile.onDragLeave) box.onDragLeave.apply(box, args);
                        dd.setDropElement(null);
                    }
                    //return false;
                },
                beforeDrop: function (profile, e, src) {
                    var dd = ood.DragDrop,
                        pp = dd.getProfile(),
                        key = pp.dragKey,
                        data = pp.dragData,
                        item, t, args,
                        box = profile.boxing();
                    if (box.getItemByDom)
                        item = box.getItemByDom(src);
                    args = [profile, e, src, key, data, item];

                    if (profile.onDropMarkClear && (false === box.onDropMarkClear.apply(box, args))) {
                    }
                    else if ((t = profile.box._onDropMarkClear) && (false === t.apply(profile.host || profile, args))) {
                    }

                    if (profile.beforeDrop && (false === box.beforeDrop.apply(box, args)))
                        return;

                    if (!profile.onDrop || (profile.onDrop && false === box.onDrop.apply(box, args))) {
                        if (profile.box._onDrop)
                            profile.box._onDrop.apply(profile.host || profile, args);
                    }

                    if (profile.afterDrop)
                        box.afterDrop.apply(box, args);
                }
            }, 'all');

            ood.merge(ch, {
                onmouseover: h2,
                onmouseout: h2,
                ondrop: h2,
                afterDrop: h2,
                beforeDrop: h2
            });
            return self;
        },
        _draggable: function (key) {
            var self = this,
                h2 = ood.Event.$eventhandler,
                o = self.$Behaviors,
                v = key == 'KEY' ? o : (o[key] || (o[key] = {})),
                handler = ood.$cache.UIKeyMapEvents,
                k2 = key == 'KEY' ? self.KEY : (self.KEY + '-' + key),
                ch = handler[k2] || (handler[k2] = {});
            //attach Behaviors
            ood.merge(v, {
                beforeMousedown: function (profile, e, src) {
                    if (ood.Event.getBtn(e) != "left") return;
                    var prop = profile.properties;
                    if (prop.disabled) return;
                    // not resizable or drag
                    if (!(prop.dragSortable || prop.dragKey)) return;

                    // avoid nodraggable keys
                    if (profile.behavior.NoDraggableKeys) {
                        var sk = profile.getKey(ood.Event.getSrc(e).id || "").split('-')[1];
                        if (sk && ood.arr.indexOf(profile.behavior.NoDraggableKeys, sk) != -1) return;
                    }

                    var pos = ood.Event.getPos(e), box = profile.boxing(), args = [profile, e, src], t;
                    if (profile.onStartDrag && (false === box.onStartDrag.apply(box, args))) {
                    }
                    else if ((t = profile.box._onStartDrag) && (false === t.apply(profile.host || profile, args))) {
                    }
                    else {
                        var con = profile.box;
                        ood.use(src).startDrag(e, {
                            dragType: 'icon',
                            targetLeft: pos.left + 12,
                            targetTop: pos.top + 12,
                            dragCursor: 'pointer',
                            dragDefer: 2,
                            dragKey: con.getDragKey(profile, src),
                            dragData: con.getDragData(profile, e, src)
                        });
                    }
                },
                beforeDragbegin: function (profile, e, src) {
                    ood.use(src).onMouseout(true, {$force: true}).onMouseup(true);
                },
                beforeDragstop: function (profile, e, src) {
                    var t;
                    if (profile.onDragStop) profile.boxing().onDragStop(profile.e, src);
                    if (t = profile.box._onDragStop) t.apply(profile.host || profile, arguments);
                }
            }, 'all');
            ood.merge(ch, {
                onmousedown: h2,
                ondragbegin: h2
            });

            return self;
        },
        //for relative , static children
        _adjustConW: function (profile, container, trigger, _adjust) {
            var prop = profile.properties, cols, containerWidth;
            if (!(cols = prop.conLayoutColumns))
                cols = prop.conDockRelative ? 1 : 0;
            if (!cols) return;
            // container width
            containerWidth = profile._containerWidth = container.scrollWidth();

            // if it's an adjust action, check first
            if (_adjust && _adjust == containerWidth) return;

            var pad = prop.conDockPadding,
                spc = prop.conDockSpacing,
                c = container.children(),
                l = c.size(), off, pw, w, tw, index = 0, ww, rowtotal = 0,
                allCtrls = [],
                // redo last row
                redoLastRow = function (pw, row, allCtrls) {
                    var rowLen = row.length,
                        colW = Math.floor(pw / rowLen),
                        rowtotal = 0,
                        rw, ww, prf, node, min, max;
                    for (var i = 0; i < rowLen; i++) {
                        node = row[i][0];
                        prf = row[i][1];
                        min = row[i][2];
                        max = row[i][3];
                        if (i == rowLen - 1) {
                            ww = prf.$forceu(Math.min(max, Math.max(min, pw - rowtotal)));
                        } else {
                            ww = prf.$forceu(rw = Math.min(max, Math.max(min, colW)));
                            rowtotal += rw;
                        }
                        allCtrls.push([node, prf, ww, rw]);
                    }
                };

            if (!l) return;
            // ensure inline block
            c.each(function (n, i, prf, p) {
                //if(trigger && n!=trigger)return;
                if (n.id && (prf = ood.$cache.profileMap[n.id]) && prf.Class && prf.Class['ood.UIProfile']) {
                    p = ood(n).css('position');
                    if (p == 'relative' || p == 'static')
                        ood(n).setInlineBlock();
                }
            });
            off = pad.left + pad.right + (cols - 1) * spc.width;
            pw = containerWidth - off;
            // It must be integer
            w = Math.floor(pw / cols);
            var curCtrl, lastRow = [], min, max;
            // set width
            c.each(function (n, i, prf, p, rw) {
                //if(trigger && n!=trigger)return;
                if (n.id && (prf = ood.$cache.profileMap[n.id]) && prf.Class && prf.Class['ood.UIProfile']) {
                    curCtrl = ood(n);
                    p = curCtrl.css('position');
                    if (p == 'relative' || p == 'static') {
                        if (prf) {
                            curCtrl.css({
                                position: 'relative',
                                left: 'auto',
                                top: 'auto',
                                right: 'auto',
                                bottom: 'auto',
                                'margin-left': (i === 0 ? pad.left : spc.width) + 'px',
                                'margin-top': (i === 0 ? pad.top : spc.height) + 'px'
                            });
                            min = ood.CSS.$px(prf.properties.dockMinW) || 0;
                            max = ood.CSS.$px(prf.properties.dockMaxW) || (pw + off);
                            ww = prf.$forceu(rw = Math.min(max, Math.max(min, w)));
                            rowtotal += rw;
                            if (rowtotal > pw) {
                                // redo last row
                                redoLastRow(pw + (cols - lastRow.length) * spc.width, lastRow, allCtrls);
                                lastRow = [];
                                rowtotal = rw;
                            }
                            lastRow.push([curCtrl, prf, min, max]);
                        }
                    }
                }
            });
            // redo last row
            if (lastRow.length)
                redoLastRow(pw + (cols - lastRow.length) * spc.width, lastRow, allCtrls);

            // set width to ctrls (properties.width + dom width);
            for (var i = 0, l = allCtrls.length; i < l; i++) {
                var node = allCtrls[i][0];
                //if(trigger && node.get(0)!=trigger)return;
                // to trigger onsize
                node.width(allCtrls[i][1].properties.width = allCtrls[i][2]);
                allCtrls[i][1]._conLayout = 1;
                // set %
                // node.get(0).style.width = Math.floor(allCtrls[i][3]  / containerWidth * 10000)/100 +'%';
            }
            var checkScroll = function (profile, container) {
                if (profile.destroyed || !profile._containerWidth || container.isEmpty()) return;
                if (profile._containerWidth != container.scrollWidth() &&profile._containerWidth != container.scrollWidth()-24  ){
                    ood.UI._adjustConW(profile, container, profile, profile._containerWidth);
                }

            };
            // caculate again for container's scrollbar show/hide
            checkScroll(profile, container);
            // caculate asyn for parent's scrollbar show/hide
            ood.asyRun(checkScroll, 0, [profile, container]);
        },
        /*copy item to hash, use 'without'
        exception: key start with $
        value(start with $) get a change to get value from setting
        */
        adjustData: function (profile, hashIn, hashOut, type) {
            if (!hashOut) hashOut = {};

            var box = profile.box, dm = box.$DataModel, i, o;

            for (i in hashIn) {
                if (i.charAt(0) == '$' || i == 'renderer') continue;
                if (hashIn.hasOwnProperty(i) && !hashOut.hasOwnProperty(i)) {
                    hashOut[i] = typeof (o = hashIn[i]) == 'string' ? i == 'html' ? ood.adjustRes(o, 0, 1, false, null, null, type == 'sub' && hashIn) : ood.adjustRes(o, true, false, null, null, type == 'sub' && hashIn) : o;
                }
            }
            if ('hidden' in hashIn)
                hashOut.hidden = hashIn.hidden;
            // filter: hidden
            var itemFilter = hashIn.itemFilter || profile.$itemFilter;
            if (itemFilter) hashOut.hidden = !!itemFilter(hashIn, 'prepareItem', profile);

            hashOut._itemDisplay = hashIn.hidden ? 'display:none;' : '';

            if ('disabled' in dm)
                hashOut.disabled = (ood.isSet(hashOut.disabled) && hashOut.disabled) ? 'ood-ui-itemdisabled' : '';
            if ('readonly' in dm)
                hashOut.readonly = (ood.isSet(hashOut.readonly) && hashOut.readonly) ? 'ood-ui-itemreadonly' : '';

            //todo:remove the extra paras
            hashOut.imageDisplay = (hashOut.imageClass || hashOut.image || hashOut.iconFontCode) ? '' : 'display:none';
            var ifc;
            if (hashOut.iconFontCode) {
                // iconFontCode + imageClass
                if (hashOut.imageClass) {
                    // filter built-in class
                    var arr = hashOut.imageClass.split(/\s+/);
                    ood.filter(arr, function (s) {
                        return !ood.builtinFontIcon[s];
                    });
                    hashOut.imageClass = arr.join(' ');
                }
            } else {
                // for ie687
                if (hashOut.imageClass) {
                    var arr = hashOut.imageClass.split(/\s+/);
                    ood.arr.each(arr, function (s) {
                        if (ood.builtinFontIcon[s]) {
                            ifc = ood.builtinFontIcon[s];
                            return;
                        }
                    }, null, true);

                    if (ifc && ood.__iefix2) {
                        hashOut.iconFontCode = ifc;
                        ood.filter(arr, function (s) {
                            return !ood.builtinFontIcon[s];
                        });
                    }
                    hashOut.imageClass = arr.join(' ');
                }
                if (!ifc) {
                    hashOut.picClass = 'ood-css-innerimage';
                    // imageClass + image
                    if (hashOut.image) {
                        hashOut.imageClass = 'ood-icon-placeholder';
                        hashOut.backgroundImage = "background-image:url(" + hashOut.image + ");";
                    }
                    if (hashOut.imagePos)
                        hashOut.backgroundPosition = 'background-position:' + hashOut.imagePos + ';';
                    else if (hashOut.image)
                        hashOut.backgroundPosition = 'background-position:center;';

                    if (hashOut.imageBgSize)
                        hashOut.backgroundSize = 'background-size:' + hashOut.imageBgSize + ';';
                    else if (hashOut.image)
                        hashOut.backgroundSize = 'background-size:initial;';

                    if (hashOut.imageRepeat)
                        hashOut.backgroundRepeat = 'background-repeat:' + hashOut.imageRepeat + ';';
                    else if (hashOut.image)
                        hashOut.backgroundRepeat = 'background-repeat:no-repeat;';
                }
            }
            if (hashOut.iconFontSize)
                hashOut.iconFontSize = 'font-size:' + hashOut.iconFontSize + ';';
            //must be here
            //Avoid Empty Image src
            // ensoure to trigger load event of img
            if (!hashOut.image && box.IMGNODE) hashOut.image = ood.ini.img_blank;
            if (o = hashOut.renderer || hashIn.renderer) {
                hashOut.caption = ood.UI._applyRenderer(profile, o, hashIn, hashOut);
            }
            return hashOut;
        },
        _applyRenderer: function (profile, renderer, hashIn, hashOut) {
            if (ood.isFun(renderer)) {
                return ood.adjustRes(renderer.call(profile, hashIn, hashOut));
            } else if (ood.isStr(renderer)) {
                var obj, prf, alias, prop = {}, events = {}, t,
                    clsReg = /^\s*[a-zA-Z]+([\w]+\.?)+[\w]+\s*$/,
                    adjustRenderer = function (hash, prop, events) {
                        if (hash) {
                            var mapReg = /^\s*([^>\s]+)?\s*>\s*([^>\s]+)\s*$/;
                            // 'alias > propName' in item
                            ood.each(hash, function (o, i) {
                                // alias > key =>alias > key
                                // > key => key
                                if (mapReg.test(i)) prop[i.replace(/^\s*>\s*/, '')] = o;
                            });
                            // 'ModuleProp' in item
                            if (ood.isHash(hash.ModuleProp)) prop = ood.merge(prop, hash.ModuleProp, 'all');
                            // 'ModuleEvents' in item
                            if (ood.isHash(hash.ModuleEvents)) events = ood.merge(events, hash.ModuleEvents, 'all');
                        }
                    };

                if (clsReg.test(renderer) && (obj = ood.SC.get(renderer))) {
                    if (obj['ood.UI'] || obj['ood.Module']) {
                        obj = new obj();
                        prf = obj.get(0);
                        alias = ood.get(hashOut, ['tagVar', 'alias']) || prf.alias;
                        if (hashIn) {
                            hashIn._render_xid = prf.$xid;
                            hashIn._render_obj = prf;
                            prf._render_conf = hashIn;
                        }
                        prf._render_holder = profile;

                        if (obj['ood.Module']) {
                            var mp = (new ood.UI.MoudluePlaceHolder());
                            prf = mp.get(0);
                            prf._module = obj;
                            obj = mp;
                        } else {
                            if (obj.setPosition) obj.setPosition('relative');
                            if (obj.setDisplay) obj.setDisplay('inline-block');
                        }

                        obj.setHost(profile.host, alias);

                        // after host setting
                        // for item/cell/row/col etc.
                        if (hashIn !== profile.properties) {
                            if (t = profile.box._applyRendererEx) t.call(profile.box, profile, prop, events, hashOut, adjustRenderer);
                            // 'alias > propName' in item
                            // 'ModuleProp' in item
                            // 'ModuleEvents' in item
                            adjustRenderer(hashOut, prop, events);
                        }
                        // 'alias > propName' in tagVar
                        // 'ModuleProp' in tagVar
                        // 'ModuleEvents' in tagVar
                        adjustRenderer(hashOut.tagVar, prop, events);

                        // ensure no renderer
                        delete prop.renderer;
                        if (!ood.isEmpty(prop)) obj.setProperties(prop);
                        if (!ood.isEmpty(events)) obj.setEvents(events);

                        (profile.$attached || (profile.$attached = [])).push(prf);
                        return obj.toHtml();
                    }
                }
                return ood.adjustVar(renderer, hashOut)
            }
        },
        $iconAction: function (profile, key, oldImageClass) {
            var p = profile.properties,
                icon = profile.getSubNode(key || 'ICON'),
                dispaly = (p.imageClass || p.image || p.iconFontCode) ? '' : 'display:none',
                ifc;

            // clear all first
            icon.css('backgroundImage', "none");
            icon.removeClass('ood-icon-placeholder');
            if (p.imageClass) icon.removeClass(p.imageClass);
            if (oldImageClass) icon.removeClass(oldImageClass);
            icon.html('');

            if (p.iconFontCode) {
                icon.html(p.iconFontCode);
                // iconFontCode + imageClass
                if (p.imageClass) {
                    // filter built-in class
                    var arr = p.imageClass.split(/\s+/);
                    ood.filter(arr, function (s) {
                        return !ood.builtinFontIcon[s];
                    });
                    icon.addClass(arr.join(' '));
                }
            } else {
                // for ie687
                if (p.imageClass) {
                    var arr = p.imageClass.split(/\s+/);
                    ood.arr.each(arr, function (s) {
                        if (ood.builtinFontIcon[s]) {
                            ifc = ood.builtinFontIcon[s];
                            return;
                        }
                    }, null, true);
                    if (ifc && ood.__iefix2) {
                        p.iconFontCode = ifc;
                        ood.filter(arr, function (s) {
                            return !ood.builtinFontIcon[s];
                        });
                    }
                    icon.addClass(arr.join(' '));
                }
                if (p.iconFontCode) {
                    icon.html(p.iconFontCode);
                }
                if (!ifc) {
                    // imageClass + image
                    if (p.image) {
                        icon.addClass('ood-icon-placeholder');
                        icon.css('backgroundImage', 'url(' + ood.adjustRes(p.image) + ')');
                    }
                }
            }
            icon.css('display', dispaly);
        },
        cacheData: function (key, obj) {
            ood.set(ood.$cache, ['UIDATA', key], obj);
            return this;
        },
        getCachedData: function (key) {
            var r = ood.get(ood.$cache, ['UIDATA', key]);
            if (typeof r == 'function') r = r();
            return r;
        },

        Behaviors: {
            onSize: function (profile, e) {
                var root = profile.getRootNode(), style = root && root.style;
                if (style && (e.width || e.height))
                    ood.UI.$tryResize(profile, style.width, style.height);
            },
            HotKeyAllowed: true,
            onContextmenu: function (profile, e, src) {
                if (profile.onContextmenu)
                    return profile.boxing().onContextmenu(profile, e, src, null, ood.Event.getPos(e)) !== false;
            }
        },
        DataModel: {
            autoTips: true,
            "className": {
                ini: "",
                action: function (v, ov) {
                    if (ov)
                        this.getRoot().removeClass(ov);
                    this.getRoot().addClass(v);
                    delete this._nodeEmSize;
                }
            },
            disableClickEffect: false,
            disableHoverEffect: false,
            disableTips: false,
            disabled: {
                ini: false,
                action: function (v) {
                    var i = this.getRoot();
                    if (v)
                        i.addClass('ood-ui-disabled');
                    else
                        i.removeClass('ood-ui-disabled');
                }
            },
            spaceUnit: {
                listbox: ['', 'px', 'em'],
                action: function () {
                    this.boxing().reLayout(true).getChildren(true, "penetrate").reLayout(true);
                }
            },
            defaultFocus: false,
            hoverPop: {
                ini: '',
                action: function (v, ov) {
                    var ns = this, b = ns.boxing(), p = ns.properties, t;
                    if (!ns.destroyed && ns.host) {
                        if (ov && (t = ns.host[ov]) && (t = t.get(0)) && t.renderId && !t.destroyed)
                            b.hoverPop(t, null);
                        if (v && (t = ns.host[v]) && (t = t.get(0)) && t.renderId && !t.destroyed)
                            b.hoverPop(t, p.hoverPopType, function () {
                                t.properties.tagVar.hoverFrom = arguments;
                            }, function () {
                                delete t.properties.tagVar.hoverFrom;
                            }, t.getRoot().parent(), v);
                    }
                }
            },
            hoverPopType: {
                ini: 'outer',
                dftWidth: 180,
                listbox: ['outer', 'inner',
                    'outerleft-outertop', 'left-outertop', 'center-outertop', 'right-outertop', 'outerright-outertop',
                    'outerleft-top', 'left-top', 'center-top', 'right-top', 'outerright-top',
                    'outerleft-middle', 'left-middle', 'center-middle', 'right-middle', 'outerright-middle',
                    'outerleft-bottom', 'left-bottom', 'center-bottom', 'right-bottom', 'outerright-bottom',
                    'outerleft-outerbottom', 'left-outerbottom', 'center-outerbottom', 'right-outerbottom', 'outerright-outerbottom'
                ]
            },
            locked: {
                ini: false,
                action: function () {
                    if (this.$inDesign) {
                        this.boxing().refresh(true);
                    }
                }
            },
            dock: {
                ini: 'none',
                listbox: ['none', 'top', 'bottom', 'left', 'right', 'center', 'middle', 'origin', 'width', 'height', 'fill', 'cover'],
                action: function (v) {
                    ood.UI.$dock(this, true, true);
                }
            },
            dockIgnore: {
                ini: false,
                action: function (v) {
                    var self = this;
                    if (self.properties.dock != 'none')
                        ood.UI.$dock(self, true, true);
                }
            },
            dockFloat: {
                ini: false,
                action: function (v) {
                    var self = this;
                    if (self.properties.dock != 'none')
                        ood.UI.$dock(self, true, true);
                }
            },
            // dockOrder is for compitable with older version only
            dockOrder: {
                hidden: true
            },
            showEffects: "",
            hideEffects: "",
            dockMargin: {
                ini: {left: 0, top: 0, right: 0, bottom: 0},
                action: function (v) {
                    var self = this;
                    v = v || {};
                    v.left = v.left || 0;
                    v.top = v.top || 0;
                    v.right = v.right || 0;
                    v.bottom = v.bottom || 0;
                    if (self.properties.dock != 'none')
                        ood.UI.$dock(self, true, true);
                }
            },

            dockMinW: {
                $spaceunit: 1,
                ini: 0
            },
            dockMinH: {
                $spaceunit: 1,
                ini: 0
            },
            dockMaxW: {
                $spaceunit: 1,
                ini: 0
            },
            dockMaxH: {
                $spaceunit: 1,
                ini: 0
            },

            // to stop conDockFlexFill
            dockIgnoreFlexFill: {
                ini: false,
                action: function (v) {
                    var self = this;
                    if (self.properties.dock != 'none')
                        ood.UI.$dock(self, true, true);
                }
            },
            // for top/left/right/bottom only
            // "" can be reset by container's conDockStretch
            dockStretch: {
                ini: "",
                combobox: ['fixed', 'forward', 'rearward', 'stretch', '0.25', '0.33', '0.5'],
                set: function (value) {
                    var o = this, t = o.properties;
                    t.dockStretch = value;
                    if (t.dock == "fill" || t.dock == "cover" || t.dock == "width" || t.dock == "height") {
                        if (value != 'forward' && value != 'rearward' && value != 'stretch') {
                            t.dockStretch = "stretch";
                        }
                    }
                    if (o.rendered && t.dock != 'none') {
                        ood.UI.$dock(o, true, true);
                    }
                }
            },
            tips: {
                ini: '',
                action: function (v) {
                    var t = ood.Tips;
                    if (t && t._showed) {
                        if (ood.UIProfile.getFromDom(t._markId) == this) {
                            t.setTips(v, true);
                        }
                    }
                }
            },
            rotate: {
                ini: 0,
                action: function (v) {
                    var root = this.getRoot(), ins = this.boxing();
                    v = parseFloat(v) || 0;
                    v = v % 360;
                    if (v < 0) v = v + 360;
                    if (this.box['ood.svg']) {
                        ins.setAttr("KEY", {transform: 'r' + v}, false);
                    } else {
                        root.rotate(v);
                    }
                }
            },
            activeAnim: {
                ini: "",
                caption: ood.getResText("DataModel.activeAnim") || '激活动画',
                action: function (key) {
                    var prf = this, okey, t;
                    if (okey = prf._activeAnim) {
                        ood.Thread.abort(okey);
                        delete prf._activeAnim;
                    }
                    if (key) prf._activeAnim = (t = prf.boxing().animate(key)) && t.id;
                }
            }
        },
        EventHandlers: {
            beforeRender: function (profile) {
            },
            onRender: function (profile) {
            },
            onLayout: function (profile) {
            },
            onResize: function (profile, width, height) {
            },
            onMove: function (profile, left, top, right, bottom) {
            },
            onDock: function (profile, region) {
            },
            beforePropertyChanged: function (profile, name, value, ovalue) {
            },
            afterPropertyChanged: function (profile, name, value, ovalue) {
            },
            beforeAppend: function (profile, child) {
            },
            afterAppend: function (profile, child) {
            },
            beforeRemove: function (profile, child, subId, bdestroy) {
            },
            afterRemove: function (profile, child, subId, bdestroy) {
            },
            onDestroy: function (profile) {
            },
            beforeDestroy: function (profile) {
            },
            afterDestroy: function (profile) {
            },
            onShowTips: function (profile, node, pos) {
            },
            onContextmenu: function (profile, e, src, item, pos) {
            }
        },
        RenderTrigger: function () {
            var prf = this, b = prf.boxing(), p = prf.properties, t,
                node = prf.getRootNode(),
                style = node.style;

            if (p.sandboxTheme) {
                ood.UI._refreshSBTheme(prf, p.sandboxTheme);
            }

            if (prf.box._onresize) {
                //avoid UI blazzing
                if (!prf._syncResize && !prf.box._syncResize) {
                    var style = prf.getRootNode().style, t
                    if ((t = style.visibility) != 'hidden') {
                        prf._$visibility = t;
                        style.visibility = 'hidden';
                    }
                    style = null;
                }
                ood.UI.$tryResize(prf, p.width, p.height);
            }
            if (p.disabled) b.setDisabled(true, true);
            if (p.rotate) b.setRotate(p.rotate, true);
            if (!prf.$inDesign && p.hoverPop) {
                ood.asyRun(function () {
                    b.setHoverPop(p.hoverPop, true);
                });
            }
            // set dataBinder for container
            if (prf.behavior.PanelKeys) {
                if (t = p.dataBinder) b.setDataBinder(t, true);
            }

            // attention animation
            if (p.activeAnim) {
                ood.asyRun(function () {
                    if (prf && !prf.destroyed)
                        prf.boxing().setActiveAnim(p.activeAnim, true);
                });
            }
            (prf.$beforeDestroy = (prf.$beforeDestroy || {}))["activeAnim"] = function (t) {
                if (t = prf._activeAnim)
                    ood.Thread.abort(t);
            }

            prf._inValid = 1;
        },
        $doResize: function (profile, w, h, force, key) {
            if (force || ((w || h) && (profile._resize_w != w || profile._resize_h != h))) {
                var root = profile.getRootNode(), con;
                //destroyed before resize
                if (!root) return false;

                profile._resize_w = w;
                profile._resize_h = h;
                ood.tryF(profile.box._onresize, [profile, w, h, force, key], profile.box);

                // to resize auto-width children for flow layout
                if ((force || w)
                    && profile.box && profile.box['ood.absContainer']
                    && (con = profile.getContainer(true))
                ) {
                    con.children().each(function (o, i, p) {
                        if ((i = ood.UIProfile.getFromDom(o.id))
                            && i.box && i.box._onresize
                            && (p = i.properties)
                            && (('position' in p) && ('width' in p))
                            && (p.position == 'static' || p.position == 'relative')
                            && (p.width === '' || p.width == 'auto')
                        ) ood.UI.$doResize(i, ood(o).width(), null, force, key);
                    });
                }
                // for have _onresize widget only
                if (profile.onResize)
                    profile.boxing().onResize(profile, w, h);
            }

            //some control will set visible to recover the css class
            if ('_$visibility' in profile) {
                var node = profile.getRootNode(),
                    style = node.style;
                if (style.visibility != 'visible' && !ood.getNodeData(node, '_setVisibility'))
                    style.visibility = profile._$visibility;
                node = style = null;
                ood.clearTimeout(profile._$rs_timer);
                delete profile._$rs_timer;
                delete profile._$rs_args;
                delete profile._$visibility;
            }
        },
        $tryResize: function (profile, w, h, force, key) {
            var s = profile.box, t = s && s._onresize;
            if (t && (force || w || h)) {
                //adjust width and height
                //w=parseFloat(w)||null;
                w = ((w === "" || w == 'auto') ? "auto" : ((ood.isFinite(w) || profile.$isPx(w)) ? (parseFloat(w) || 0) : w)) || null
                h = ((h === "" || h == 'auto') ? "auto" : ((ood.isFinite(h) || profile.$isPx(h)) ? (parseFloat(h) || 0) : h)) || null;

                //if it it has delay resize, overwrite arguments
                if ('_$visibility' in profile) {
                    var args = profile._$rs_args;
                    // asyrun once only
                    if (!args) {
                        args = profile._$rs_args = [profile, null, null];
                        profile._$rs_timer = ood.asyRun(function () {
                            // destroyed
                            if (!profile.box) return;
                            try {
                                if (profile && profile._$rs_args)
                                    ood.UI.$doResize.apply(null, profile._$rs_args);
                            } catch (e) {

                            }

                        });
                    }
                    //keep the last one, neglect zero and 'auto'
                    args[1] = w;
                    args[2] = h;
                    args[3] = force;
                    args[4] = key;
                    //else, call resize right now
                } else {
//for performance checking
//console.log('resize',profile.$xid,w,h,force,key);
                    ood.UI.$doResize(profile, w, h, force, key);
                }
            }
        },
        LayoutTrigger: function () {
            var self = this, b = self.boxing(), p = self.properties;
            // have to refresh this
            delete self._nodeEmSize;

            if (p.position == 'absolute') {
                if (p.dock && p.dock != 'none') {
                    //first time, ensure _onresize to be executed.
                    if (!self.$laidout) {
                        self.$laidout = 1;
                        var stl = self.getRootNode().style,
                            wu = 0 + self.$picku(stl.width),
                            hu = 0 + self.$picku(stl.height);
                        switch (p.dock) {
                            case 'top':
                            case 'bottom':
                            case 'width':
                                stl.width = 0;
                                break;
                            case 'left':
                            case 'right':
                            case 'height':
                                stl.height = 0;
                                break;
                            default:
                                stl.width = 0;
                                stl.height = 0;
                        }
                    }
                    ood.UI.$dock(this, false, true);
                }
            } else {
                if (ood.get(self, ['parent', 'properties', 'conDockRelative']) || ood.get(self, ['parent', 'properties', 'conLayoutColumns'])) {
                    if (!self._conLayout) {
                        ood.resetRun('conLayoutColumns:' + self.parent.$xid, function () {
                            if (!self._conLayout && !self.destroyed && !self.parent.destroyed)
                                ood.UI._adjustConW(self.parent, self.getRoot().parent(), self);
                        });
                    }
                }
            }
        },
        $dock_args: ['top', 'bottom', 'left', 'right', 'center', 'middle', 'width', 'height'],
        $dock_map: {middle: 1, center: 1},
        $dock: function (profile, force, trigger) {
            var node = profile.getRoot(),
                isSVG = profile.box['ood.svg'],
                ins = profile.boxing(),
                i1 = -1, i2 = -1, i3 = -1, i4 = -1,
                p = ood((node.get(0) && node.get(0).parentNode) || profile.$dockParent),
                //for ie6 1px bug
                _adjust = function (v) {
                    return ood.browser.ie && ood.browser.ver <= 6 ? v - v % 2 : v
                },
                adjustOverflow = function (p, isWin) {
                    var f, t, c, x, y;
                    if (isWin) {
                        // $frame.type has high priority
                        if (ood.ini.$frame && !ood.isNumb(ood.ini.$frame.zoom)) return;

                        f = ood.win.$getEvent('onSize', 'dock');
                    } else if (p && p.get(0)) {
                        f = p.$getEvent('onSize', 'dock');
                    }

                    if (f && f.dockall && f.dockall.length) {
                        for (var i = 0, l = f.dockall.length, s; i < l; i++) {
                            s = f.dockall[i].$dockType;
                            switch (s) {
                                case "fill":
                                case "cover":
                                    x = y = 1;
                                    break;
                                case "top":
                                case "bottom":
                                case "width":
                                    x = 1;
                                    break;
                                case "left":
                                case "right":
                                case "height":
                                    y = 1;
                                    break;
                            }
                        }
                    }
                    if (x && y) {
                        c = "ood-css-noscroll";
                    } else if (x) {
                        c = "ood-css-noscrollx";
                    } else if (y) {
                        c = "ood-css-noscrolly";
                    }
                    if (isWin) {
                        ood('html').removeClass(/^ood-css-noscroll(x|y)?$/);
                        t = ood('body').get(0);
                        if (t) t.scroll = '';
                        if (c) {
                            if (x) ood.win.scrollLeft(0);
                            if (y) ood.win.scrollTop(0);
                            ood('html').addClass(c);
                            if (x && y && t) {
                                t.scroll = 'no';
                            }
                        }
                    } else {
                        p.removeClass(/^ood-css-noscroll(x|y)?$/);
                        if (c) {
                            if (x) p.scrollLeft(0);
                            if (y) p.scrollTop(0);
                            p.addClass(c);
                        }
                    }
                },
                // adjust min/max
                checkLimits = function (profile, prop, dir, value) {
                    var t;
                    if (parseFloat(prop['dockMin' + dir]) && (t = profile.$px(prop['dockMin' + dir]))) value = _adjust(t <= value ? value : t);
                    if (parseFloat(prop['dockMax' + dir]) && (t = profile.$px(prop['dockMax' + dir]))) value = _adjust(t <= value ? t : value);
                    return value;
                },
                apply2Ctrl = function (profile, isSvg, ins, node, style, region) {
                    var us = ood.$us(profile),
                        adjustunit = function (v) {
                            return profile.$forceu(v, us > 0 ? 'em' : 'px', null, true)
                        };
                    // apply to UI
                    if (('left' in region) && profile.$px(style.left) !== region.left) region.left = adjustunit(region.left);
                    if (('top' in region) && profile.$px(style.top) !== region.top) region.top = adjustunit(region.top);
                    if (('right' in region) && profile.$px(style.right) !== region.right) region.right = adjustunit(region.right);
                    if (('bottom' in region) && profile.$px(style.bottom) !== region.bottom) region.bottom = adjustunit(region.bottom);
                    if (('width' in region) && profile.$px(style.width) !== region.width) region.width = adjustunit(region.width);
                    if (('height' in region) && profile.$px(style.height) !== region.height) region.height = adjustunit(region.height);
                    if (!ood.isEmpty(region)) {
                        if (isSVG) ins._setBBox(region);
                        else node.cssRegion(region, true);
                    }
                },
                applyPrevLine = function (obj, space/*spaceW*/, all/*obj.ww*/, p2/*width*/, p3/*dockMaxW*/, p4/*left*/, p5/*right*/) {
                    if (!obj.prevLine.length) return;
                    var pct = [], size, t, l, region, node, prop, margin, style,
                        ll = obj.prevLine.length - 1, preAll = 0,
                        start = obj[p4],
                        css = ood.CSS;

                    all -= ll * space;
                    // get [all] available
                    ood.arr.each(obj.prevLine, function (n) {
                        if (n.pct) {
                            pct.push(n);
                        } else {
                            all -= n.region[p2] + n.margin[p4] + n.margin[p5];
                        }
                    });

                    // determine pct's size
                    if (l = pct.length) {
                        // sum pct
                        var pctSum = 0;
                        ood.arr.each(pct, function (n, i) {
                            pctSum += n.pct;
                        });
                        ood.arr.each(pct, function (n, i) {
                            prop = n.prop;
                            region = n.region;
                            nodefz = n.nodefz;

                            // recaculate pct
                            n.pct = n.pct / pctSum;

                            // caculate size
                            size = all * n.pct - n.margin[p4] - n.margin[p5];
                            // adjust max only
                            if (parseFloat(prop[p3]) && (t = css.$px(prop[p3], nodefz))) size = _adjust(t <= size ? t : size);
                            if (i == l - 1) {
                                size = all - preAll;
                                // adjust max only
                                if (parseFloat(prop[p3]) && (t = css.$px(prop[p3], nodefz))) size = _adjust(t <= size ? t : size);
                                region[p2] = size;
                            } else {
                                preAll += (region[p2] = parseInt(size, 10));
                            }
                        });
                    }

                    // determine pct's start pos, and set to UI
                    ood.arr.each(obj.prevLine, function (n, i) {
                        region = n.region;
                        // left, top, bottom, right
                        region[p4] = parseInt(start + n.margin[p4], 10);
                        start += region[p2] + n.margin[p4] + n.margin[p5] + space;
                        apply2Ctrl(n.profile, n.isSVG, n.ins, n.node, n.style, region);
                    });
                    // rest the prev line
                    obj.prevLine = [];
                };

            if (!p.get(0))
                return;
            var prop = profile.properties,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate, true)
                },
                margin = prop.dockMargin,
                auto = 'auto',
                value = prop.dock || 'none',
                pid = ood.Event.getId(p.get(0)),
                order = function (x, y) {
                    // _dockOrder is for designer mode only
                    // dockOrder is for compitable with older version only
                    x = parseInt(x.properties._dockOrder || x.properties.dockOrder, 10) || 0;
                    y = parseInt(y.properties._dockOrder || y.properties.dockOrder, 10) || 0;
                    return x > y ? 1 : x == y ? 0 : -1;
                },
                region,
                inMatrix = '$inMatrix',
                f, t, isWin,
                rootfz = node._getEmSize(),
                umargin = {
                    top: adjustunit(margin.top || 0, rootfz),
                    left: adjustunit(margin.left || 0, rootfz),
                    right: adjustunit(margin.right || 0, rootfz),
                    bottom: adjustunit(margin.bottom || 0, rootfz)
                };

            if (isSVG) {
                var bbox = ins._getBBox();
                prop.left = bbox.x;
                prop.top = bbox.y;
                prop.width = bbox.width;
                prop.height = bbox.height;
            }
            if (p.get(0) === document.body || p.get(0) === document || p.get(0) === window) {
                pid = '!document';
                isWin = true;
            }

            //attached to matrix
            if (pid && (pid == ood.Dom._ghostDivId || ood.str.startWith(pid, ood.Dom._emptyDivId)))
                return;

            if (profile.$dockParent != pid || profile.$dockType != value || force) {
                profile.$dockParent = pid;
                profile.$dockType = value;

                //unlink first
                i1 = profile.unLink('$dockall');
                i2 = profile.unLink('$dock');
                i3 = profile.unLink('$dock1');
                i4 = profile.unLink('$dock2');

                //set the fix value first
                switch (value) {
                    case 'middle':
                        region = {
                            right: prop.right == auto ? auto : (prop.right || ''),
                            bottom: auto,
                            left: prop.left == auto ? auto : (prop.left || ''),
                            width: prop.width || '',
                            height: prop.height || ''
                        };
                        break;
                    case 'center':
                        region = {
                            right: auto,
                            bottom: prop.bottom == auto ? auto : (prop.bottom || ''),
                            top: prop.top == auto ? auto : (prop.top || ''),
                            width: prop.width || '',
                            height: prop.height || ''
                        };
                        break;
                    case 'origin':
                        region = {right: auto, bottom: auto, width: prop.width || '', height: prop.height || ''};
                        break;
                    case 'top':
                        region = {left: umargin.left, right: umargin.right, bottom: auto, height: prop.height || ''};
                        //width top
                        break;
                    case 'bottom':
                        region = {left: umargin.left, right: umargin.right, top: auto, height: prop.height || ''};
                        //width bottom
                        break;
                    case 'left':
                        region = {right: auto, width: prop.width || ''};
                        //height top left
                        break;
                    case 'right':
                        region = {left: auto, width: prop.width || ''};
                        //height top right
                        break;
                    case 'width':
                        region = {bottom: auto, height: prop.height || '', top: prop.top || ''};
                        //width left
                        break;
                    case 'height':
                        region = {right: auto, width: prop.width || '', left: prop.left || ''};
                        //height top
                        break;
                    case 'fill':
                    case 'cover':
                        region = {right: auto, bottom: auto};
                        break;
                    case 'none':
                        region = {left: prop.left, top: prop.top, width: prop.width || '', height: prop.height || ''};
                        break;
                }
                if (node.get(0)) {
                    if (isSVG)
                        ins._setBBox(region);
                    else
                        node.cssRegion(region, true);
                }
                //if in body, set to window
                if (isWin) {
                    p = ood.win;
                    if (!ood.$cache._resizeTime) ood.$cache._resizeTime = 1;
                }
                //set dynamic part
                if (value != 'none') {
                    f = p.$getEvent('onSize', 'dock');
                    if (!f) {
                        f = function (arg) {
                            //get self vars
                            var me = arguments.callee,
                                css = ood.CSS,
                                map = ood.UI.$dock_map,
                                arr = ood.UI.$dock_args,
                                rePos = me.rePos,
                                pid = me.pid,
                                // the dock parent is window
                                isWin = me.pid == "!window" || me.pid == "!document",
                                pprf = isWin ? 0 : ood.UIProfile.getFromDom(pid),
                                pprop = pprf && pprf.properties,
                                proot = pprf && pprf.getRoot(),
                                pstyle = proot && proot.get(0) && proot.get(0).style,
                                prootfz = pstyle && proot._getEmSize(),
                                conDockSpacing = (pprop && ('conDockSpacing' in pprop)) ? pprop.conDockSpacing : {
                                    width: 0,
                                    height: 0
                                },
                                conDockPadding = (pprop && ('conDockPadding' in pprop)) ? pprop.conDockPadding : {
                                    left: 0,
                                    top: 0,
                                    right: 0,
                                    bottom: 0
                                },
                                conDockFlexFill = (pprop && ('conDockFlexFill' in pprop)) ? pprop.conDockFlexFill : '',
                                conDockStretch = (pprop && ('conDockStretch' in pprop)) ? pprop.conDockStretch.split(/[,;\s]+/) : [],
                                perW = conDockFlexFill == "width" || conDockFlexFill == "both",
                                perH = conDockFlexFill == "height" || conDockFlexFill == "both",
                                // if enable zoom, use a visualized frame (in <html> tag style)
                                node = isWin ? ood.frame : ood(pid);

                            if (!node.get(0))
                                return;

                            var pn = node.get(0),
                                style = pn.style,
                                us = ood.$us(profile),
                                nodefz = node._getEmSize(),
                                adjustunit = function (v, emRate) {
                                    return css.$forceu(v, us > 0 ? 'em' : 'px', emRate || nodefz)
                                },
                                obj, i, k, o, key, target,
                                ofs = isWin ? ood('body').get(0).style : style,
                                old_of = ofs.overflow,
                                old_ofx = ofs.overflowX,
                                old_ofy = ofs.overflowY;

                            // 1. set overflow for size
                            if (style) style.overflow = style.overflowX = style.overflowY = "hidden";
                            var hasCover;
                            for (var i in me.dockall) {
                                if (me.dockall[i].$dockType == "cover") {
                                    hasCover = 1;
                                    break;
                                }
                            }

                            //2. get width / height
                            var width = (style && css.$px(style.width, nodefz)) || node.width() || 0,
                                height = (style && css.$px(style.height, nodefz)) || node.height() || 0,
                                offsetH = hasCover ? node.offsetHeight() : height,
                                offsetW = hasCover ? node.offsetWidth() : width;

                            if (width == 'auto') width = 0;
                            if (height == 'auto') height = 0;
                            //width=Math.max( node.scrollWidth()||0,  (style&&css.$px(style.width,nodefz))||node.width()||0);
                            //height=Math.max( node.scrollHeight()||0, (style&&css.$px(style.height,nodefz))||node.height()||0);

                            // 3.reset overflow
                            if (style) {
                                style.overflow = old_of;
                                style.overflowX = old_ofx;
                                style.overflowY = old_ofy;
                            }
                            conDockSpacing = {
                                width: css.$px(conDockSpacing.width || 0, prootfz),
                                height: css.$px(conDockSpacing.height || 0, prootfz)
                            };
                            conDockPadding = {
                                left: css.$px(conDockPadding.left || 0, prootfz),
                                top: css.$px(conDockPadding.top || 0, prootfz),
                                right: css.$px(conDockPadding.right || 0, prootfz),
                                bottom: css.$px(conDockPadding.bottom || 0, prootfz)
                            };

                            //window resize: check time span, for window resize in firefox
                            //force call when input $dockid
                            //any node resize
                            if (arg.$dockid || !isWin || ((ood.stamp() - ood.$cache._resizeTime) > 50)) {
                                //recruit call, give a short change
                                obj = {
                                    // padding
                                    left: conDockPadding.left,
                                    top: conDockPadding.top,
                                    right: conDockPadding.right,
                                    bottom: conDockPadding.bottom,
                                    // size
                                    width: width,
                                    height: height,
                                    // for dock='cover'
                                    offsetW: offsetW || width,
                                    offsetH: offsetH || height
                                };
                                obj.preX = obj.oX = obj.left;
                                obj.preY = obj.oY = obj.top;
                                obj.prevLine = [];
                                // space avaiable
                                obj.ww = obj.width - obj.left - obj.right;
                                obj.hh = obj.height - obj.top - obj.bottom;
                                obj.leftHolder = obj.topHolder = obj.rightHolder = obj.bottomHolder = 0;


                                // adjust  aother dimension first
                                if (perW || perH) {
                                    var wCount = 0, wSum = 0, hCount = 0, hSum = 0, tmp, hMax = 0,
                                        adjustMM = function (prf, direction, numb, prop, t) {
                                            prop = prf.properties;
                                            if (parseFloat(t = prop['dockMin' + direction])) numb = Math.max(prf.$px(t), numb);
                                            if (parseFloat(t = prop['dockMax' + direction])) numb = Math.min(prf.$px(t), numb);
                                            return numb;
                                        }, originalSize = function (prf, dir) {
                                            return parseFloat(prf.$px(prf.properties[dir])) || 0;
                                        };
                                    // collect controls (w/h) to be percentaged, no dockIgnore
                                    for (k = 0; key = arr[k++];) {
                                        target = me[key];
                                        if (target.length) {
                                            for (i = 0; o = target[i++];) {
                                                if (!(o.properties.position != 'absolute' || o.properties.dockIgnore || o.properties.dockIgnoreFlexFill)) {
                                                    var node = o.getRoot();
                                                    if (perW && (key == 'left' || key == 'right' || key == 'width')) {
                                                        wCount++;
                                                        tmp = adjustMM(o, "W", originalSize(o, 'width'));
                                                        wSum += tmp;
                                                        if (o.properties.dock != "fill") {
                                                            hMax = Math.max(hMax, adjustMM(o, "H", originalSize(o, 'height')));
                                                        }
                                                    }
                                                    if (perH && (key == 'top' || key == 'bottom' || key == 'height')) {
                                                        hCount++;
                                                        hSum += adjustMM(o, "H", originalSize(o, 'height'));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (hSum && hMax) {
                                        hSum += hMax;
                                    }
                                    // for w percent
                                    if (wCount >= 1 && wSum && obj.width) {
                                        var innerW = obj.width - conDockPadding.left - conDockPadding.right - (wCount - 1) * conDockSpacing.width;
                                        for (k = 0; key = arr[k++];) {
                                            target = me[key];
                                            if (target.length) {
                                                for (i = 0; o = target[i++];) {
                                                    if (o.properties.position == 'absolute' && !o.properties.dockIgnore && o.properties.dockIgnoreFlexFill) {
                                                        var node = o.getRoot();
                                                        if (key == 'left' || key == 'right' || key == 'width') {
                                                            innerW -= adjustMM(o, "W", originalSize(o, 'width'));
                                                            innerH -= conDockSpacing.width;
                                                        }
                                                    }
                                                }
                                                for (i = 0; o = target[i++];) {
                                                    if (!(o.properties.position != 'absolute' || o.properties.dockIgnore || o.properties.dockIgnoreFlexFill)) {
                                                        var node = o.getRoot();
                                                        if (key == 'left' || key == 'right' || key == 'width') {
                                                            node.width(adjustunit(adjustMM(o, "W", Math.min(1, originalSize(o, 'width') / wSum) * innerW)));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // for h percent
                                    if (hCount >= 1 && hSum && obj.height) {
                                        var innerH = obj.height - conDockPadding.top - conDockPadding.bottom - (hCount - 1) * conDockSpacing.height;
                                        for (k = 0; key = arr[k++];) {
                                            target = me[key];
                                            if (target.length) {
                                                for (i = 0; o = target[i++];) {
                                                    if (o.properties.position == 'absolute' && !o.properties.dockIgnore && o.properties.dockIgnoreFlexFill) {
                                                        var node = o.getRoot();
                                                        if (key == 'top' || key == 'bottom' || key == 'height') {
                                                            innerH -= adjustMM(o, "H", originalSize(o, 'height'));
                                                            innerH -= conDockSpacing.height;
                                                        }
                                                    }
                                                }
                                                for (i = 0; o = target[i++];) {
                                                    if (!(o.properties.position != 'absolute' || o.properties.dockIgnore || o.properties.dockIgnoreFlexFill)) {
                                                        var node = o.getRoot();
                                                        if (key == 'top' || key == 'bottom' || key == 'height') {
                                                            node.height(adjustunit(adjustMM(o, "H", Math.min(1, originalSize(o, 'height') / hSum) * innerH)));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (pprf && perW) pprf._conDockFlexFillW = 1;
                                    if (pprf && perH) pprf._conDockFlexFillH = 1;
                                }
                                if (conDockFlexFill != "both") {
                                    for (k = 0; key = arr[k++];) {
                                        target = me[key];
                                        if (target.length) {
                                            for (i = 0; o = target[i++];) {
                                                if (!(o.properties.position != 'absolute' || o.properties.dockIgnore || o.properties.dockIgnoreFlexFill)) {
                                                    var node = o.getRoot();
                                                    if (pprf && pprf._conDockFlexFillW && !perW && (key == 'left' || key == 'right' || key == 'width')) {
                                                        node.width(adjustunit(o.properties.width));
                                                    }
                                                    if (pprf && pprf._conDockFlexFillH && !perH && (key == 'top' || key == 'bottom' || key == 'height')) {
                                                        node.height(adjustunit(o.properties.height));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (pprf && pprf._conDockFlexFillW) delete pprf._conDockFlexFillW;
                                if (pprf && pprf._conDockFlexFillH) delete pprf._conDockFlexFillH;

                                // repos && resize
                                for (k = 0; key = arr[k++];) {
                                    obj.preX = obj.oX;
                                    obj.preY = obj.top;

                                    target = me[key];
                                    var ii = 0, ll;
                                    if (ll = target.length) {
                                        if (!map[key]) arg.width = arg.height = 1;
                                        for (i = 0; o = target[i++];) {
                                            if (o.properties.position == 'absolute' && !o.properties.dockIgnore) {
                                                rePos(o, ii++, ll, obj, key, arg.$dockid, isWin || arg.width, isWin || arg.height, conDockSpacing.width, conDockSpacing.height, conDockStretch);
                                            }
                                        }
                                    }
                                }

                                if (obj.later) {
                                    ood.each(obj.later, function (o) {
                                        var profile;
                                        //for safari
                                        try {
                                            if (o.isSVG)
                                                o.ins._setBBox(o);
                                            else
                                                o.node.cssRegion(o, true);
                                            if (profile = ood.UIProfile.getFromDom(o.node.get(0))) {
                                                delete o.node;
                                                // for no _onresize widget only
                                                if (!profile.box._onresize && profile.onResize && (o.width !== null || o.height !== null))
                                                    profile.boxing().onResize(profile, o.width, o.height);
                                                if (profile.onDock) profile.boxing().onDock(profile, o);
                                                if (profile.$onDock) profile.$onDock(profile, o);
                                            }
                                        } catch (e) {
                                            ood.asyRun(function () {
                                                // destroyed
                                                if (!o.node) return;

                                                var ow = o.width,
                                                    oh = o.height;
                                                o.width = ((parseFloat(o.width) || 0) + 1) + ood.CSS.$picku(o.width);
                                                o.height = ((parseFloat(o.width) || 0) + 1) + ood.CSS.$picku(o.height);

                                                if (o.isSVG) o.ins._setBBox(o);
                                                else o.node.cssRegion(o);

                                                o.width = ow;
                                                o.height = oh;

                                                if (o.isSVG) o.ins._setBBox(o);
                                                else o.node.cssRegion(o, true);

                                                if (profile = ood.UIProfile.getFromDom(o.node.get(0))) {
                                                    delete o.node;
                                                    // for no _onresize widget only
                                                    if (!profile.box._onresize && profile.onResize && (o.width !== null || o.height !== null))
                                                        profile.boxing().onResize(profile, o.width, o.height);
                                                    if (profile.onDock) profile.boxing().onDock(profile, o);
                                                    if (profile.$onDock) profile.$onDock(profile, o);
                                                }
                                            })
                                        }
                                    });
                                }
                                // for those are not in obj.later
                                for (k = 0; key = arr[k++];) {
                                    target = me[key];
                                    if (target.length) {
                                        for (i = 0; o = target[i++];) {
                                            if (o.properties.position == 'absolute' && !o.properties.dockIgnore) {
                                                if (!obj.later || !obj.later[o.$xid]) {
                                                    if (o.onDock) o.boxing().onDock(o);
                                                    if (o.$onDock) o.$onDock(o);
                                                }
                                            }
                                        }
                                    }
                                }

                                //if window resize, keep the timestamp
                                if (isWin)
                                    ood.$cache._resizeTime = ood.stamp();
                            }

                            me = rePos = node = style = null;
                        };
                        f.pid = pid;
                        ood.arr.each(ood.UI.$dock_args, function (key) {
                            f[key] = [];
                        });
                        f.dockall = [];
                        f.rePos = function (profile, index, ll, obj, value, id, w, h, spaceW, spaceH, conDockStretch) {
                            //if $dockid input, and not the specific node, return
                            var flag = false;
                            if (id && profile.$xid != id) flag = true;
                            var prop = profile.properties,
                                css = ood.CSS,
                                flt = prop.dockFloat,
                                margin = prop.dockMargin,
                                stretch = prop.dockStretch,
                                sStart, sEnd, noStretch, pct, isCover,
                                tempW = 0, tempH = 0,
                                node = profile.getRoot(),
                                ins = profile.boxing(),
                                root = node.get(0),
                                style = root.style,
                                us = ood.$us(profile),
                                nodefz = node._getEmSize(),
                                adjustunit = function (v) {
                                    return css.$forceu(v, us > 0 ? 'em' : 'px', nodefz)
                                },
                                left, top, right, bottom, temp, other,
                                x = css.$px(prop._dockBorderWidth, nodefz) || 0,
                                y = css.$px(prop._dockBorderHeight, nodefz) || 0,
                                region = {}, t,
                                isSVG = profile.box['ood.svg'], bbox;

                            // caculate with px
                            margin.top = css.$px(margin.top, nodefz);
                            margin.left = css.$px(margin.left, nodefz);
                            margin.right = css.$px(margin.right, nodefz);
                            margin.bottom = css.$px(margin.bottom, nodefz);

                            if (isSVG) {
                                bbox = ins._getBBox();
                                prop.left = bbox.x;
                                prop.top = bbox.y;
                                prop.width = bbox.width;
                                prop.height = bbox.height;
                            }

                            if (style.display == 'none')
                                return;
                            switch (value) {
                                case 'middle':
                                case 'center':
                                    sStart = sEnd = 0;
                                    break;
                                default:
                                    // flow stretch can be copy from container
                                    stretch = stretch || ((value == "width" || value == "height") ? "" : conDockStretch[index % conDockStretch.length]) || "stretch";

                                    // width/height support 3 only:
                                    if (value == "width" || value == "height") {
                                        if (stretch != "stretch" && stretch != "forward" && stretch != "rearward") {
                                            stretch = 'stretch';
                                        }
                                    }
                                    switch (stretch) {
                                        case 'stretch':
                                            sStart = 1;
                                            sEnd = 1;
                                            noStretch = 0;
                                            break;
                                        case 'forward':
                                            sStart = 1;
                                            sEnd = 0;
                                            noStretch = 0;
                                            break;
                                        case 'rearward':
                                            sStart = 0;
                                            sEnd = 1;
                                            noStretch = 0;
                                            break;
                                        case 'fixed':
                                            noStretch = 1;
                                            break;
                                        default:
                                            noStretch = 1;
                                            pct = Math.min(1, Math.max(0, parseFloat(stretch) || 0));
                                    }
                            }

                            isCover = prop.dock == 'cover';
                            //top/bottom/left/right must be set by order first
                            switch (value) {
                                case 'middle':
                                    //use height() is ok
                                    if (isSVG)
                                        ins.setTop(Math.max(0, (obj.height - bbox.height) / 2));
                                    else
                                        node.top(adjustunit(Math.max(0, (obj.height - node.height()) / 2)));
                                    break;
                                case 'center':
                                    if (isSVG)
                                        ins.setLeft(Math.max(0, (obj.width - bbox.width) / 2));
                                    else
                                        node.left(adjustunit(Math.max(0, (obj.width - node.width()) / 2)));
                                    break;
                                case 'top':
                                    if (!flag) {
                                        if (noStretch) {
                                            // calculate width
                                            temp = pct ? (parseFloat(obj.ww * pct) || 0) : _adjust(isSVG ? bbox.width : css.$px(prop.width, nodefz)) + margin.left + margin.right;
                                            if (pct) {
                                                temp -= spaceW;
                                                tempW = checkLimits(profile, prop, 'W', temp - margin.left - margin.right);
                                                temp = tempW + margin.left + margin.right;
                                            }

                                            // if overflow, wrap to new line
                                            if ((obj.preX + temp - obj.oX) > obj.ww) {
                                                // new line
                                                obj.top += obj.topHolder;
                                                obj.topHolder = 0;
                                                obj.preX = obj.oX;

                                                applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');
                                            }

                                            obj.prevLine.push({
                                                profile: profile,
                                                prop: prop,
                                                node: node,
                                                style: style,
                                                isSVG: isSVG,
                                                ins: ins,
                                                pct: pct,
                                                margin: margin,
                                                region: {
                                                    top: obj.top + margin.top,
                                                    width: temp
                                                }
                                            });

                                            // keep for next calculation
                                            obj.preX += temp + spaceW;
                                            obj.topHolder = Math.max(obj.topHolder, (isSVG ? bbox.height : node.offsetHeight()) + margin.top + margin.bottom + spaceH);
                                        } else {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');

                                            if (obj.topHolder) {
                                                obj.top += obj.topHolder;
                                                obj.preX = obj.oX;
                                            }
                                            left = sStart ? ((flt ? 0 : obj.left) + margin.left) : css.$px(prop.left, nodefz);
                                            right = sEnd ? ((flt ? 0 : obj.right) + margin.right) : (obj.width - css.$px(prop.width, nodefz) - css.$px(prop.left, nodefz));
                                            top = (flt ? 0 : obj.top) + margin.top;

                                            temp = checkLimits(profile, prop, 'W', obj.width - left - right - x);

                                            apply2Ctrl(profile, isSVG, ins, node, style, {
                                                left: left,
                                                top: top,
                                                width: _adjust(temp)
                                            });

                                            if (!flt)
                                                obj.top += (isSVG ? bbox.height : node.offsetHeight()) + margin.top + margin.bottom + spaceH;
                                        }
                                        // the last pct one
                                        if (ll == index + 1) {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');
                                        }
                                    }
                                    break;
                                case 'bottom':
                                    if (!flag) {
                                        if (noStretch) {
                                            // calculate width
                                            temp = pct ? (parseFloat(obj.ww * pct) || 0) : _adjust(isSVG ? bbox.width : css.$px(prop.width, nodefz)) + margin.left + margin.right;
                                            if (pct) {
                                                temp -= spaceW;
                                                tempW = checkLimits(profile, prop, 'W', temp - margin.left - margin.right);
                                                temp = tempW + margin.left + margin.right;
                                            }

                                            // if overflow, wrap to new line
                                            if ((obj.preX + temp - obj.oX) > obj.ww) {
                                                // new line
                                                obj.bottom += obj.bottomHolder;
                                                obj.bottomHolder = 0;
                                                obj.preX = obj.oX;

                                                applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');
                                            }

                                            obj.prevLine.push({
                                                profile: profile,
                                                prop: prop,
                                                node: node,
                                                style: style,
                                                isSVG: isSVG,
                                                ins: ins,
                                                pct: pct,
                                                margin: margin,
                                                region: {
                                                    bottom: obj.bottom + margin.bottom,
                                                    width: temp
                                                }
                                            });

                                            // keep for next calculation
                                            obj.preX += temp + spaceW;
                                            obj.bottomHolder = Math.max(obj.bottomHolder, (isSVG ? bbox.height : node.offsetHeight()) + margin.top + margin.bottom + spaceH);
                                        }
                                        else {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');

                                            if (obj.bottomHolder) {
                                                obj.bottom += obj.bottomHolder;
                                                obj.bottomHolder = 0;
                                                obj.preX = obj.oX;
                                            }
                                            left = sStart ? ((flt ? 0 : obj.left) + margin.left) : css.$px(prop.left, nodefz);
                                            right = sEnd ? ((flt ? 0 : obj.right) + margin.right) : (obj.width - css.$px(prop.width, nodefz) - css.$px(prop.left, nodefz));
                                            bottom = (flt ? 0 : obj.bottom) + margin.bottom;

                                            temp = checkLimits(profile, prop, 'W', obj.width - left - right);

                                            apply2Ctrl(profile, isSVG, ins, node, style, {
                                                left: left,
                                                bottom: bottom,
                                                width: _adjust(temp)
                                            });

                                            if (!flt)
                                                obj.bottom += (isSVG ? bbox.height : node.offsetHeight()) + margin.top + margin.bottom + spaceH;
                                        }
                                        // the last pct one
                                        if (ll == index + 1) {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.ww, 'width', 'dockMaxW', 'left', 'right');
                                        }
                                    }
                                    break;
                                case 'left':
                                    if (!flag) {
                                        if (obj.topHolder) {
                                            obj.top += obj.topHolder + spaceH;
                                            obj.topHolder = 0;
                                            // reset preY
                                            obj.oY = obj.preY = obj.top;
                                        }
                                        if (obj.bottomHolder) {
                                            obj.bottom += obj.bottomHolder + spaceH;
                                            obj.bottomHolder = 0;
                                        }
                                        // reset hh
                                        obj.hh = obj.height - obj.top - obj.bottom;
                                        if (obj.hh <= 0) return;

                                        if (noStretch) {
                                            // calculate height
                                            temp = pct ? (parseFloat(obj.hh * pct) || 0) : _adjust(isSVG ? bbox.height : css.$px(prop.height, nodefz)) + margin.top + margin.bottom;
                                            if (pct) {
                                                temp -= spaceH;
                                                tempH = checkLimits(profile, prop, 'H', temp - margin.top - margin.bottom);
                                                temp = tempH + margin.top + margin.bottom;
                                            }

                                            // if overflow, wrap to new line
                                            if ((obj.preY + temp - obj.oY) > obj.hh) {
                                                // new line
                                                obj.left += obj.leftHolder;
                                                obj.leftHolder = 0;
                                                obj.preY = obj.oY;

                                                applyPrevLine(obj, spaceH, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');
                                            }

                                            obj.prevLine.push({
                                                profile: profile,
                                                prop: prop,
                                                node: node,
                                                style: style,
                                                isSVG: isSVG,
                                                ins: ins,
                                                pct: pct,
                                                margin: margin,
                                                region: {
                                                    left: obj.left + margin.left,
                                                    height: temp
                                                }
                                            });

                                            // keep for next calculation
                                            obj.preY += temp + spaceH;
                                            obj.leftHolder = Math.max(obj.leftHolder, (isSVG ? bbox.width : node.offsetWidth()) + margin.left + margin.right + spaceW);
                                        }
                                        else {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');

                                            if (obj.leftHolder) {
                                                obj.left += obj.leftHolder;
                                                obj.leftHolder = 0;
                                                obj.preY = obj.top;
                                            }

                                            left = (flt ? 0 : obj.left) + margin.left;
                                            top = sStart ? ((flt ? 0 : obj.top) + margin.top) : css.$px(prop.top, nodefz);
                                            bottom = sEnd ? ((flt ? 0 : obj.bottom) + margin.bottom) : (obj.height - css.$px(prop.height, nodefz) - css.$px(prop.top, nodefz));

                                            temp = checkLimits(profile, prop, 'H', obj.height - top - bottom - y);

                                            apply2Ctrl(profile, isSVG, ins, node, style, {
                                                left: left,
                                                top: top,
                                                height: _adjust(temp)
                                            });

                                            if (!flt)
                                                obj.left += (isSVG ? bbox.width : node.offsetWidth()) + margin.left + margin.right + spaceW;
                                        }
                                        // the last pct one
                                        if (ll == index + 1) {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');
                                        }
                                    }
                                    break;
                                case 'right':
                                    //if no top/bottom and change w only
                                    if (!flag) {
                                        if (obj.topHolder) {
                                            obj.top += obj.topHolder;
                                            obj.topHolder = 0;
                                            // reset preY
                                            obj.oY = obj.preY = obj.top;
                                        }
                                        if (obj.bottomHolder) {
                                            obj.bottom += obj.bottomHolder;
                                            obj.bottomHolder = 0;
                                        }
                                        // reset hh
                                        obj.hh = obj.height - obj.top - obj.bottom;

                                        if (obj.hh <= 0) return;

                                        if (noStretch) {
                                            // calculate height
                                            temp = pct ? (parseFloat(obj.hh * pct) || 0) : _adjust(isSVG ? bbox.height : css.$px(prop.height, nodefz)) + margin.top + margin.bottom;
                                            if (pct) {
                                                temp -= spaceH;
                                                tempH = checkLimits(profile, prop, 'H', temp - margin.top - margin.bottom);
                                                temp = tempH + margin.top + margin.bottom;
                                            }

                                            // if overflow, wrap to new line
                                            if ((obj.preY + temp - obj.oY) > obj.hh) {
                                                // new line
                                                obj.right += obj.rightHolder;
                                                obj.rightHolder = 0;
                                                obj.preY = obj.oY;

                                                applyPrevLine(obj, spaceH, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');
                                            }

                                            obj.prevLine.push({
                                                profile: profile,
                                                prop: prop,
                                                node: node,
                                                style: style,
                                                isSVG: isSVG,
                                                ins: ins,
                                                pct: pct,
                                                margin: margin,
                                                region: {
                                                    right: obj.right + margin.right,
                                                    height: temp
                                                }
                                            });

                                            // keep for next calculation
                                            obj.preY += temp + spaceH;
                                            obj.rightHolder = Math.max(obj.rightHolder, (isSVG ? bbox.width : node.offsetWidth()) + margin.left + margin.right + spaceW);
                                        }
                                        else {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');

                                            if (obj.rightHolder) {
                                                obj.right += obj.rightHolder;
                                                obj.rightHolder = 0;
                                                obj.preY = obj.top;
                                            }

                                            right = (flt ? 0 : obj.right) + margin.right;
                                            top = sStart ? ((flt ? 0 : obj.top) + margin.top) : (css.$px(prop.top, nodefz));
                                            bottom = sEnd ? ((flt ? 0 : obj.bottom) + margin.bottom) : (obj.height - css.$px(prop.height, nodefz) - css.$px(prop.top, nodefz));

                                            temp = checkLimits(profile, prop, 'H', obj.height - top - bottom - y);

                                            apply2Ctrl(profile, isSVG, ins, node, style, {
                                                right: right,
                                                top: top,
                                                height: _adjust(temp)
                                            });

                                            if (!flt)
                                                obj.right += (isSVG ? bbox.width : node.offsetWidth()) + margin.left + margin.right + spaceW;
                                        }
                                        // the last pct one
                                        if (ll == index + 1) {
                                            // new line
                                            applyPrevLine(obj, spaceW, obj.hh, 'height', 'dockMaxH', 'top', 'bottom');
                                        }
                                    }
                                    break;
                                case 'width':
                                    //if no top/bottom/left/right and change h only
                                    if (!w) return;
                                    if (obj.leftHolder) {
                                        obj.left += obj.leftHolder;
                                        obj.leftHolder = 0;
                                    }
                                    if (obj.rightHolder) {
                                        obj.right += obj.rightHolder;
                                        obj.rightHolder = 0;
                                    }

                                    left = sStart ? ((isCover ? 0 : (flt ? 0 : obj.left)) + margin.left) : css.$px(prop.left, nodefz);
                                    right = sEnd ? ((isCover ? 0 : (flt ? 0 : obj.right)) + margin.right) : (obj.width - css.$px(prop.width, nodefz) - css.$px(prop.left, nodefz));
                                    top = prop.dock == 'width' ? (css.$px(prop.top, nodefz) || 0) : (sStart ? ((isCover ? 0 : (flt ? 0 : obj.top)) + margin.top) : css.$px(prop.top, nodefz));
                                    //later call for w/h change once
                                    temp = checkLimits(profile, prop, 'W', (isCover ? obj.offsetW : obj.width) - left - right - x);

                                    obj.later = obj.later || {};
                                    obj.later[profile.$xid] = obj.later[profile.$xid] || {};
                                    ood.merge(obj.later[profile.$xid], {
                                        isSVG: isSVG,
                                        ins: ins,
                                        node: node,
                                        width: adjustunit(temp),
                                        left: adjustunit(left),
                                        top: adjustunit(top)
                                    }, 'all');
                                    break;
                                case 'height':
                                    //if no top/bottom/left/right and change w only
                                    if (!h) return;
                                    if (obj.topHolder) {
                                        obj.top += obj.topHolder;
                                        obj.topHolder = 0;
                                        obj.preX = obj.oX;
                                    }
                                    if (obj.bottomHolder) {
                                        obj.bottom += obj.bottomHolder;
                                        obj.bottomHolder = 0;
                                        obj.preX = obj.oX;
                                    }

                                    top = sStart ? ((isCover ? 0 : (flt ? 0 : obj.top)) + margin.top) : css.$px(prop.top, nodefz);
                                    bottom = sEnd ? ((isCover ? 0 : (flt ? 0 : obj.bottom)) + margin.bottom) : (obj.height - css.$px(prop.height, nodefz) - css.$px(prop.top, nodefz));
                                    left = prop.dock == 'height' ? (css.$px(prop.left, nodefz) || 0) : (sStart ? ((isCover ? 0 : (flt ? 0 : obj.left)) + margin.left) : css.$px(prop.left, nodefz));

                                    //later call for w/h change once
                                    temp = checkLimits(profile, prop, 'H', (isCover ? obj.offsetH : obj.height) - top - bottom - y);

                                    obj.later = obj.later || {};
                                    obj.later[profile.$xid] = obj.later[profile.$xid] || {};
                                    ood.merge(obj.later[profile.$xid], {
                                        isSVG: isSVG,
                                        ins: ins,
                                        node: node,
                                        height: adjustunit(temp),
                                        left: adjustunit(left),
                                        top: adjustunit(top)
                                    }, 'all');

                                    break;
                            }
                        };

                        //add handler to window or node
                        p.onSize(f, 'dock');
                    }
                    //set link to node
                    if (value == 'fill' || value == 'cover') {
                        profile.link(f.height, '$dock1', null, i3);
                        profile.link(f.width, '$dock2', null, i4);
                        ood.arr.stableSort(f.height, order);
                        ood.arr.stableSort(f.width, order);
                    } else if (value == 'origin') {
                        profile.link(f.center, '$dock1', null, i3);
                        profile.link(f.middle, '$dock2', null, i4);
                    } else {
                        profile.link(f[value], '$dock', null, i2);
                        ood.arr.stableSort(f[value], order);
                    }
                    profile.link(f.dockall, '$dockall', null, i1);

                    //
                    ood.$cache._resizeTime = 1;

                    //set shortuct
                    profile.$dockFun = f;
                }

                // adjust dock parent's overflow
                adjustOverflow(p, isWin);

                if (value != 'none') {
                    (profile.$beforeDestroy = (profile.$beforeDestroy || {}))["releaseDock"] = function () {
                        profile.unLink('$dockall');
                        profile.unLink('$dock');
                        profile.unLink('$dock1');
                        profile.unLink('$dock2');
                        adjustOverflow(p, isWin);

                        if (p && p.get(0) && (p = ood.UIProfile.getFromDom(p.id()))) {
                            // affect dock parent
                            p.boxing().adjustDock();
                            ood.tryF(p.clearCache, [], p);
                        }
                        profile = p = null;
                    }
                } else {
                    if (profile.$beforeDestroy)
                        delete profile.$beforeDestroy["releaseDock"];
                }

            }

            //run once now
            if (trigger)
                p.onSize();
        },

        _beforeSerialized: function (profile) {
            var b, t, r, o = {};
            ood.merge(o, profile, 'all');
            var p = o.properties = ood.clone(profile.properties, true),
                ds = o.box.$DataStruct, t,
                dm = o.box.$DataModel;

            for (var i in ood.UI.$ps) {
                if ((i in p) && p[i] === '') p[i] = 'auto';
            }

            // *** force to rem/px
            for (var i in dm) {
                if (dm[i].ini === p[i]) delete p[i];
                else if (dm[i] && dm[i]['$spaceunit']) {
                    if ((dm[i].ini === 0 || dm[i].ini === '0' || dm[i].ini === '0px' || dm[i].ini === '0em')
                        && (p[i] === 0 || p[i] === '0' || p[i] === '0px' || p[i] === '0em')) delete p[i];
                    else if ((dm[i].ini + (ood.isFinite(dm[i].ini) ? 'px' : '')) == (p[i] + (ood.isFinite(p[i]) ? 'px' : ''))) delete p[i];
                    else if (p[i] != 'auto') {
                        t = ood.$us(o);
                        p[i] = profile.$forceu(p[i], t == 2 ? 'em' : t == -2 ? 'px' : null);
                    }
                }
            }
            if (p.position == 'absolute') {
                switch (p.dock) {
                    case 'top':
                    case 'bottom':
                        delete delete p.top;
                        delete p.bottom;
                        delete p.right;
                        break;
                    case 'left':
                    case 'right':
                        delete p.left;
                        delete p.right;
                        delete p.bottom;
                        break;
                }
            }
            for (var i in ood.UI.$ps) {
                if ((i in p) && ood.isNaN(p[i])) delete p[i];
            }

            if (p.items && p.items.length) {
                t = ood.absObj.$specialChars;
                p.items = ood.clone(p.items, function (o, i, d) {
                    return !t[((d === 1 ? o.id : i) + '').charAt(0)] && o != undefined;
                });
            }
            if (p.tagCmds && p.tagCmds.length) {
                t = ood.absObj.$specialChars;
                p.tagCmds = ood.clone(p.tagCmds, function (o, i, d) {
                    return !t[((d === 1 ? o.id : i) + '').charAt(0)] && o != undefined;
                });
            }
            // for empty object
            for (var i in profile.box._objectProp)
                if ((i in p) && p[i] && (ood.isHash(p[i]) || ood.isArr(p[i])) && ood.isEmpty(p[i])) delete p[i];
            // special for tagVar
            var i = 'tagVar';
            if ((i in p) && p[i] && (ood.isHash(p[i]) || ood.isArr(p[i])) && ood.isEmpty(p[i])) delete p[i];

            ood.arr.each(["dockMargin", "conDockPadding", "conDockSpacing", "sandboxTheme", "propBinder"], function (key) {
                if (t = p[key]) {
                    if (!ood.isHash(t)) {
                        return;
                    }
                    r = ds[key];
                    for (var i in t) {
                        if (r[i] !== t[i]) {
                            return;
                        }
                    }
                    delete p[key];
                }
            });

            if (ood.isEmpty(p.resizerProp))
                delete p.resizerProp;
            if (p.items && (p.items.length == 0 || p.listKey))
                delete p.items;
            if (p.tagCmds && (p.tagCmds.length == 0 || p.listKey))
                delete p.tagCmds;

            return o;
        },
        getDropKeys: function (profile, node) {
            return profile.properties.dropKeys;
        },
        getDragKey: function (profile, node) {
            return profile.properties.dragKey || (profile.properites.dragSortable && (profile.key + ":" + profile.$xid));
        },
        getDragData: function (profile, event, node) {
            return {
                profile: profile,
                domId: ood.use(node).id(),
                data: profile.onGetDragData ? profile.boxing().onGetDragData(profile, event, node) : null
            };
        },
        _prepareData: function (profile, data) {
            var prop = profile.properties,
                dm = this.$DataModel,
                me = arguments.callee,
                map = me.map || (me.map = ood.toArr('left,top,bottom,right,width,height')),
                a = [],
                box = profile.box,
                ajd = profile.box.adjustData,
                t;
            data = data || {};
            //can't input id in properties
            if (prop.id) delete prop.id;

            if ('required' in dm) {
                //prop._required = prop.required ? "ood-required" : "";
                prop._requiredfld = prop.required ? "ood-required-field" : "";
            }

            // cant null
            if ('nodeName' in dm && !prop.nodeName)
                prop.nodeName = "ood";

            //give default caption
            if ('caption' in dm && prop.caption !== null)
                prop.caption = prop.caption === undefined ? profile.alias : prop.caption;

            if ('html' in dm && prop.html)
                data.html = ood.adjustRes(prop.html, 0, 1);
            if ('src' in dm && prop.src)
                data.src = ood.adjustRes(prop.src, 0, 1);

            // *** force to em
            ood.each(prop, function (o, i) {
                if (dm[i] && dm[i]['$spaceunit']) {
                    if (ood.$us(profile) > 0)
                        if (prop[i] === 0 || prop[i] == '0') prop[i] = '0em';
                        else if (prop[i] === 0 || prop[i] == '0') prop[i] = '0px';
                }
            });

            //give border width
            if ('$hborder' in dm && dm.$hborder) {
                if (profile.$isEm(prop.width)) {
                    data.bWidth = profile.$px2em(profile.$em2px(prop.width) - prop.$hborder * 2, null, true) + 'em';
                } else {
                    data.bWidth = (parseFloat(prop.width) || 0) - prop.$hborder * 2;
                }
            }
            if ('$vborder' in dm && dm.$vborder) {
                if (profile.$isEm(prop.height)) {
                    data.bHeight = profile.$px2em(profile.$em2px(prop.height) - prop.$vborder * 2, null, true) + 'em';
                } else {
                    data.bHeight = (parseFloat(prop.height) || 0) - prop.$vborder * 2;
                }
            }
            //set left,top,bottom,right,width,height
            for (var j = 0, i; i = map[j]; j++) {
                var t = (i in data) ? data[i] : prop[i];
                if (t || t === 0) {
                    if (t != 'auto') a[a.length] = i + ':' + profile.$addu(t);
                }
            }
            // position,z-index,visibility,display
            if (prop.position) a[a.length] = 'position:' + prop.position;
            if (prop.visibility) a[a.length] = 'visibility:' + prop.visibility;
            if ('zIndex' in prop) a[a.length] = 'z-index:' + prop.zIndex;
            if (prop.display) a[a.length] = prop.display == 'inline-block' ? ('display:' + ood.$inlineBlock.join('; display:') + ';') : ('display:' + prop.display);

            data._style = a.join(';') + ';';
            if (box.$Behaviors.PanelKeys && !box["ood.absList"]) {
                a = [];
                if (prop.panelBgClr) a[a.length] = 'background-color:' + prop.panelBgClr;
                if (prop.panelBgImg) a[a.length] = 'background-image:url(' + ood.adjustRes(prop.panelBgImg) + ')';
                if (prop.panelBgImgPos) a[a.length] = 'background-position:' + prop.panelBgImgPos;
                if (prop.panelBgImgRepeat) a[a.length] = 'background-repeat:' + prop.panelBgImgRepeat;
                if (prop.panelBgImgAttachment) a[a.length] = 'background-attachment:' + prop.panelBgImgAttachment;
                data._panelstyle = a.join(';');
            }

            if ('className' in dm)
                data._className = prop.className || "";

            if ('readonly' in dm) data.readonly = prop.readonly ? "ood-ui-readonly" : "";
            if ('href' in dm) data.href = prop.href || ood.$DEFAULTHREF;
            if ('tabindex' in dm) data.tabindex = prop.tabindex || '-1';
            if ('items' in dm) {
                profile.ItemIdMapSubSerialId = {};
                profile.SubSerialIdMapItem = {};

                prop.items = profile.box._adjustItems(prop.items);
                data.items = this._prepareItems(profile, prop.items);
            }

            if ('selectable' in dm)
                data._selectable = (ood.browser.ie && ood.browser.ver < 10)
                    ? ""
                    : (prop.selectable ? "ood-ui-selectable" : "ood-ui-unselectable");

            //default prepare
            data = ajd(profile, prop, data);

            profile.prepared = true;
            return data;
        },
        _prepareInlineObj: function (profile, item, tabindex) {
            var obj = item['object'];
            obj = obj['ood.absBox'] ? obj.get(0) : obj['ood.UIProfile'] ? obj : ood.create(obj).get(0);
            if (obj.destroyed) return null;
            if (obj['ood.UIProfile']) {
                obj.properties.position = 'relative';
                if ('tabindex' in obj.properties) obj.properties.tabindex = tabindex;
                var addcls = 'ood-inline-object', cck = obj.CC.KEY || (cck = obj.CC.KEY = '');
                if (cck.indexOf(addcls) === -1) obj.CC.KEY = cck + " " + addcls;
            }

            item._inline_xid = obj.$xid;
            item._inline_obj = obj;
            obj._inline_conf = item;
            obj._inline_holder = profile;

            if (obj.alias && (!obj.host || obj.host === obj)) obj.boxing().setHost(profile.host, obj.alias);
            (profile.$attached || (profile.$attached = [])).push(obj);
            return obj;
        },
        _prepareItems: function (profile, items, pid, mapCache, serialId) {
            var ns = this,
                result = [],
                item, dataItem, t,
                SubID = ood.UI.$tag_subId, id,
                prop = profile.properties,
                tabindex = prop.tabindex,
                ajd = profile.box.adjustData,
                itemFilter = profile.$itemFilter;
            if (itemFilter) itemFilter('begin', 'prepareItem', profile);

            //set map
            for (var i = 0, l = items.length; i < l; i++) {
                if (ood.isHash(items[i])) {
                }
                else if (ood.isArr(items[i])) {
                    items[i] = {id: items[i][0]};
                    if (items.length > 1) items[i].caption = items[i][1];
                } else {
                    items[i] = {id: items[i]};
                }

                if (items[i].id === '?') items[i].id = ood.rand();

                item = items[i];

                if (profile.beforePrepareItem && false === profile.boxing().beforePrepareItem(profile, item, pid, mapCache, serialId)) {
                    continue;
                }

                if (!item.hasOwnProperty('caption')) item.caption = item.id;

                dataItem = {id: item.id};
                if (pid) dataItem._pid = pid;
                dataItem.renderer = item.renderer || prop.renderer;

                id = dataItem[SubID] = typeof serialId == 'string' ? serialId : profile.pickSubId('items');

                if (false !== mapCache) {
                    profile.ItemIdMapSubSerialId[item.id] = id;
                    profile.SubSerialIdMapItem[id] = item;
                }
                if (item['object']) {
                    dataItem['object'] = ns._prepareInlineObj(profile, item, tabindex);
                } else {
                    dataItem._tabindex = tabindex;

                    //others
                    ajd(profile, item, dataItem, 'sub');
                    if (ns._prepareItem)
                        ns._prepareItem(profile, dataItem, item, pid, i, l, mapCache, serialId);
                }
                result.push(dataItem);
            }

            if (itemFilter) itemFilter('end', 'prepareItem', profile);
            return result;
        },
        _showTips: function (profile, node, pos) {
            if (profile.properties.disableTips) return;
            if (profile.onShowTips)
                return profile.boxing().onShowTips(profile, node, pos);
        }
    }
});

//absList Class
ood.Class("ood.absList", "ood.absObj", {
    Instance: {
        activate: function () {
            var profile = this.get(0),
                items = profile.getSubNode('ITEM', true);
            if (!items.isEmpty())
                items.focus(true);
            return this;
        },


        _initIconColors: function (type, p) {
            var colors = [],
                iconColors = ["#E6945C", "#46C37B", "#195ead", "#9E8CE0", "#1f8d9b", "#87CEEB", "#e04d7f"],
                fontColors = ["#E6945C", "#46C37B", "#195ead", "#9E8CE0", "#1f8d9b", "#87CEEB", "#e04d7f"],
                itemColors = ["#F19B60", "#49CD81", "#0277bd", "#A693EB", "#42a3af", "#B0E0E6", "#F06292"];

            switch (type) {
                case 'icon':
                    colors = iconColors;
                    if (p.iconColors && p.iconColors.length > 0) {
                        colors = p.iconColors;
                    }
                    break;
                case 'font':
                    colors = fontColors;
                    if (p.fontColors && p.fontColors.length > 0) {
                        colors = p.fontColors;
                    }
                    break;
                case 'item':
                    colors = itemColors;
                    if (p.itemColors && p.itemColors.length > 0) {
                        colors = p.itemColors;
                    }
                    break;
            }
            return colors;
        },

        _autoColor: function (item, index, p) {
            index = index || 0;

            if (p.autoFontColor || item.fontColor) {
                var fontColors = this._initIconColors('font', p);
                while (index && index > (fontColors.length - 1)) {
                    index = index - fontColors.length;
                }

                if (!item.fontColor) {
                    item.fontColor = fontColors[index];
                }
                item._fontColor = item.fontColor ? ";color:" + item.fontColor : '';
            }

            if (p.autoIconColor || item.iconColor) {
                var iconColors = this._initIconColors('icon', p);
                while (index && index > (iconColors.length - 1)) {
                    index = index - iconColors.length;
                }
                if (!item.iconColor) {
                    item.iconColor = iconColors[index];
                }
                item._iconColor = item.iconColor ? ";color:" + item.iconColor : '';
            }

            if (p.autoItemColor || item.itemColor) {
                var itemColors = this._initIconColors('item', p);
                while (index && index > (itemColors.length - 1)) {
                    index = index - itemColors.length;
                }
                if (!item.itemColor) {
                    item.itemColor = itemColors[index];
                }
                item._itemColor = item.itemColor ? ";background-color:" + item.itemColor : '';
                item._fontColor = ';color:#ffffff';
                item._iconColor = ';color:#ffffff';
            }
        },


        updateTagVar: function (tagVar) {
            var items = this.getItems();
            ood.each(items, function (item) {
                if (!item.tagVar) {
                    item.tagVar = {};
                }
                ood.merge(item.tagVar, tagVar, 'all');
            })
        },


        /*
        [x] ,valid id   ,true  => insert [x] before node
        [x] ,valid id   ,false => insert [x ]after node
        [x] ,null ,true  => insert [x ] to head
        [x] ,null ,false => insert [x ] to tail
        */
        insertItems2: function (arr, base/*true: the current item*/, before, all) {
            var arr2 = ood.clone(arr);
            if (ood.isHash(arr2)) {
                delete arr2.sub;
                delete arr2.bindClass;
                delete arr2.currentClassName;
            }
            this.insertItems(arr2, base/*true: the current item*/, before, all);
        },
        insertItems: function (arr, base/*true: the current item*/, before, all) {
            var node, arr2,
                items, index, r, v, prop,
                data, box,
                ns = this,
                b = ns._afterInsertItems;
            return this.each(function (profile) {
                box = profile.box;
                arr2 = box._adjustItems(arr);
                prop = profile.properties;

                if (base === true) {
                    v = prop.$UIvalue || prop.value;
                    if (v) v = (v + '').split(prop.valueSeparator);
                    k = profile.getItemByItemId(v[0]);
                    base = k ? k.id : null;
                }

                items = profile.properties.items;
                index = ood.arr.subIndexOf(items, 'id', base);

                //if in dom, create it now
                if (profile.renderId) {
                    // prepare properties format
                    data = box._prepareItems(profile, arr2);

                    r = profile._buildItems('items', data);

                    // try to render inner ood.UI
                    if (profile.$attached) {
                        for (var i = 0, v; v = profile.$attached[i++];) {
                            //(profile.$innerObj||(profile.$innerObj=[])).push(v);
                            if (v._render) v._render(true);
                            if ((t = v._render_holder || v._inline_holder) && t.$afterAttached)
                                ood.tryF(t.$afterAttached, [v], t);
                            else
                                ood.tryF(ns.$afterAttached, [v], ns);
                        }
                        delete profile.$attached;
                    }

                    if (index == -1) {
                        //if no base specified
                        node = profile.getSubNode(box._ITEMSKEY || profile.keys.ITEMS || profile.keys.KEY);
                        //items.length==1 for that one have fake item(for example: editable poll)
                        if (before)
                            node.prepend(r);
                        else
                            node.append(r);
                    } else {
                        node = profile.getSubNodeByItemId(box._ITEMKEY || 'ITEM', base);
                        if (before)
                            node.addPrev(r);
                        else
                            node.addNext(r);
                    }
                }

                //must be here
                if (index == -1) {
                    ood.arr.insertAny(items, arr2, before ? 0 : -1);
                } else
                    ood.arr.insertAny(items, arr2, before ? index : index + 1);

                if (b)
                    profile.boxing()._afterInsertItems(profile, data, base, before);

                // try to hide ui-no-children node
                // logic must same to doFilter
                if (profile.$itemFilter) {
                    var hideItems = [];
                    ood.arr.each(arr, function (item) {
                        if (item.sub && !item.hidden) {
                            //  if(!item._checked && item.id)
                            //      ns.toggleNode(item.id, true, false, true);
                            if (true !== profile.$itemFilter(item, 'checkSub', profile)) {
                                var flag;
                                for (var i = 0, l = item.sub.length; i < l; i++) {
                                    if (!item.sub[i].hidden) {
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (!flag) hideItems.push(item.id);
                            } else {
                                hideItems.push(item.id);
                            }
                        }
                    });
                    if (hideItems.length) ns.showItems(hideItems, false);
                }
            });
        },
        removeItems: function (arr/*default is the current*/, key, purgeNow) {
            var obj, v,
                b = this._afterRemoveItems;
            remove = function (profile, arr, target, data, ns, force) {
                var self = arguments.callee;
                if (!ns) ns = ood();
                ood.filter(arr, function (o) {
                    var serialId, b;
                    if (force || (b = (ood.arr.indexOf(target, o.id) != -1))) {
                        if (profile.renderId) {
                            if (serialId = profile.ItemIdMapSubSerialId[o.id]) {
                                data.push(ood.copy(profile.SubSerialIdMapItem[serialId]));
                                // clear maps
                                delete profile.SubSerialIdMapItem[serialId];
                                delete profile.ItemIdMapSubSerialId[o.id];
                                profile.reclaimSubId(serialId, 'items');

                                //parent node is deleted
                                if (!force) {
                                    if (!(obj = profile.getSubNode(profile.keys[key] ? key : (profile.box._ITEMKEY || 'ITEM'), serialId)).isEmpty())
                                        ns.merge(obj);
                                    //for inner template or ood.UI
                                    if (o.$xid) ns.get().push(ood.getObject(o.$xid).getRootNode());
                                }
                            }
                        }
                    }
                    //check sub
                    if (o.sub) self(profile, o.sub, target, data, ns, force || b);
                    //filter it
                    if (b) {
                        for (var i in o) o[i] = null;
                        return false;
                    }
                });
                ns.remove(true, purgeNow);
            };
            return this.each(function (profile) {
                var p = profile.properties, data = [];
                arr = ood.isHash(arr) ? [arr.id] : ood.isArr(arr) ? arr : arr === 0 ? [0] : arr ? (arr + "").split(p.valueSeparator) : null;
                if (!arr) arr = ((p.$UIvalue || p.value) + "").split(p.valueSeparator);
                ood.arr.each(arr, function (o, i) {
                    arr[i] = '' + (ood.isNumb(o) ? p.items && p.items[o].id : ood.isHash(o) ? o.id : o)
                });
                // clear properties
                remove(profile, p.items, arr, data);
                // clear value
                if (v = p.$UIvalue) {
                    if ((v = ('' + v).split(p.valueSeparator)).length > 1) {
                        ood.filter(v, function (o) {
                            return ood.arr.indexOf(arr, o) == -1;
                        });
                        p.$UIvalue = v.join(p.valueSeparator);
                    } else {
                        if (ood.arr.indexOf(arr, p.$UIvalue) != -1)
                            p.$UIvalue = null;
                    }
                }
                if (b && profile.renderId)
                    profile.boxing()._afterRemoveItems(profile, data);
            });
        },
        clearItems: function (purgeNow) {
            return this.each(function (profile) {
                var prop = profile.properties;
                if (profile.SubSerialIdMapItem) {
                    //empty dom
                    if (profile.renderId) {
                        profile.getSubNode(profile.keys[profile.box._ITEMKEY || 'ITEM'], true).remove(true, purgeNow);
                    }
                    //save subid
                    ood.each(profile.SubSerialIdMapItem, function (o, serialId) {
                        profile.reclaimSubId(serialId, 'items');
                    });
                    //clear cache
                    profile.SubSerialIdMapItem = {};
                    profile.ItemIdMapSubSerialId = {};
                }

                //delete items
                prop.items.length = 0;
                prop.$UIvalue = null;
                //keep the value
                //prop.value=null;
            });
        },
        updateItem: function (itemId/*default is the current*/, options) {
            var self = this,
                profile = self.get(0), v,
                prop = profile.properties;

            itemId = ood.isHash(itemId) ? itemId.id : itemId === 0 ? 0 : itemId ? (itemId + '') : null;
            if (ood.isNumb(itemId)) itemId = ood.get(prop.items, [itemId, "id"]);
            if (!itemId) {
                v = prop.$UIvalue || prop.value;
                if (v) v = (v + '').split(prop.valueSeparator);
                v = profile.getItemByItemId(v[0]);
                itemId = v ? v.id : null;
            }
            var box = profile.box,
                items = prop.items,
                rst = profile.queryItems(items, function (o) {
                    return typeof o == 'object' ? o.id === itemId : o == itemId
                }, true, true, true),
                nid, item, serialId, arr, node, oldsub, t;
            if (!ood.isHash(options)) options = {caption: options + ''};

            if (rst && rst.length) {
                rst = rst[0];
                if (typeof rst[0] != 'object')
                    item = rst[2][rst[1]] = {id: rst[0]};
                else
                    item = rst[0];

                // [[modify id
                if (ood.isSet(options.id)) options.id += "";
                if (options.id && itemId !== options.id) {
                    nid = options.id;
                    var m2 = profile.ItemIdMapSubSerialId, v;
                    if (!m2[nid]) {
                        if (v = m2[itemId]) {
                            m2[nid] = v;
                            delete m2[itemId];
                            profile.SubSerialIdMapItem[v].id = nid;
                        } else {
                            item.id = nid;
                        }
                    }
                }
                delete options.id;
                // modify id only
                if (ood.isEmpty(options))
                    return self;
                //]]

                //in dom already?
                node = profile.getSubNodeByItemId('ITEM', nid || itemId);
                if (!node.isEmpty()) {
                    //for the sub node
                    if ('sub' in options) {
                        delete item._created;
                        delete item._checked;
                        delete item._inited;

                        // destroy all sub dom
                        if (item.sub) {
                            var sub = [];
                            ood.arr.each(item.sub, function (o) {
                                sub.push(o.id);
                            });
                            self.removeItems(sub);
                        }
                    }
                    // keep sub nodes
                    else if (item.sub) {
                        oldsub = profile.getSubNodeByItemId('SUB', nid || itemId);
                    }

                    //merge options
                    ood.merge(item, options, 'all');
                    //prepared already?
                    serialId = ood.get(profile, ['ItemIdMapSubSerialId', nid || itemId]);
                    arr = box._prepareItems(profile, [item], item._pid, false, serialId);
                    node.replace(profile._buildItems(arguments[2] || 'items', arr), false);

                    // restore sub nodes
                    if (oldsub && !oldsub.isEmpty()) {
                        if (!(t = profile.getSubNodeByItemId('SUB', nid || itemId)).isEmpty())
                            t.replace(oldsub);
                    }
                    if (typeof self.setUIValue == 'function') {
                        var uiv = prop.$UIvalue || "", arr = ('' + uiv).split(prop.valueSeparator);
                        if (arr.length && ood.arr.indexOf(arr, itemId) != -1) {
                            if (nid) {
                                ood.arr.removeValue(arr, itemId);
                                arr.push(item.id);
                                // id changed
                                self.setUIValue(arr.join(prop.valueSeparator), true, null, 'update');
                            } else {
                                // id didn't change, but item refreshed
                                self._setCtrlValue(uiv)
                            }
                        }
                    }
                } else {
                    //merge options
                    ood.merge(item, options, 'all');
                }

                if (box.$Behaviors.PanelKeys) {
                    var hash = {};
                    if (options.hasOwnProperty('panelBgClr')) hash["background-color"] = options.panelBgClr;
                    if (options.hasOwnProperty('panelBgImg')) {
                        hash["background-image"] = options.panelBgImg ? ("url(" + ood.adjustRes(options.panelBgImg) + ")") : "";
                    }
                    if (options.hasOwnProperty('panelBgImgPos')) hash["position-color"] = options.panelBgImgPos;
                    if (options.hasOwnProperty('panelBgImgRepeat')) hash["background-repeat"] = options.panelBgImgRepeat;
                    if (options.hasOwnProperty('panelBgImgAttachment')) hash["background-attachment"] = options.panelBgImgAttachment;
                    if (options.hasOwnProperty('overflow')) {
                        var v = options.overflow;
                        if (v) {
                            if (v.indexOf(':') != -1) {
                                ood.arr.each(v.split(/\s*;\s*/g), function (s) {
                                    var a = s.split(/\s*:\s*/g);
                                    if (a.length > 1) {
                                        hash[ood.str.trim(a[0])] = ood.str.trim(a[1] || '');
                                    }
                                });
                            }
                        }
                        hash.overflow = v || "";
                    }
                    if (!ood.isEmpty(hash)) {
                        ood.arr.each(box.$Behaviors.PanelKeys, function (k) {
                            panel = profile.getSubNode(k, nid || itemId).css(hash);
                        });
                    }
                }
            }
            return self;
        },
        // filter: [true] => filter out
        doFilter: function (itemFilter, helper, reLayout) {
            var ns = this,
                profile = ns.get(0);
            if (profile) {
                if (!itemFilter) {
                    if (profile.$itemFilter) {
                        delete profile.$itemFilter;
                        itemFilter = function () {
                            return false
                        };
                    } else return this;
                } else if (ood.isFun(itemFilter)) {
                    profile.$itemFilter = itemFilter;
                } else if (itemFilter === true) {
                    itemFilter = profile.$itemFilter;
                }
                ood.resetRun(profile.$xid + ':itemFilter', function () {
                    itemFilter('begin', 'doFilter', profile)

                    var prop = profile.properties,
                        items = prop['rows'] || prop['items'],
                        showItems = [],
                        hideItems = [],
                        f1 = function (items, showItems, hideItems) {
                            var count = 0, rtn;
                            ood.arr.each(items, function (item) {
                                if (item.sub) {
                                    // check parent node first
                                    if (true === itemFilter(item, helper, profile)) {
                                        count++;
                                        if (item.hidden) showItems.push(item.id);
                                    } else {
                                        // ensure open all tree nodes
                                        //if(!item._checked && item.id)
                                        //    (ns['toggleRow']||ns['toggleNode']).call(ns, item.id, true, false, true);
                                        // check sub showed count next
                                        if (f1(item.sub, showItems, hideItems)) {
                                            count++;
                                            if (item.hidden) showItems.push(item.id);
                                        } else {
                                            if (!item.hidden) hideItems.push(item.id);
                                        }
                                    }
                                } else {
                                    if (itemFilter(item, helper, profile)) {
                                        if (!item.hidden) hideItems.push(item.id);
                                    } else {
                                        count++;
                                        if (item.hidden) showItems.push(item.id);
                                    }
                                }
                            });
                            return count;
                        };
                    f1(items, showItems, hideItems);
                    itemFilter('end', 'doFilter', profile)

                    // reflect to dom
                    if (showItems.length) (ns['showRows'] || ns['showItems']).call(ns, showItems);
                    if (hideItems.length) (ns['showRows'] || ns['showItems']).call(ns, hideItems, false);
                    if (reLayout !== false) ns.reLayout(true);
                });
            }
            return this;
        },


        hideItems: function (itemId) {
            return this.showItems(itemId, false);
        },
        showItems: function (itemId/*default is the current*/, show) {
            var ns = this,
                profile = ns.get(0),
                showNodes = ood(),
                hideNodes = ood(),
                prop = profile.properties;
            itemId = ood.isHash(itemId) ? itemId.id : ood.isArr(itemId) ? itemId : itemId === 0 ? [0] : itemId ? (itemId + "").split(prop.valueSeparator) : null;
            if (!itemId) itemId = ((prop.$UIvalue || prop.value) + "").split(prop.valueSeparator);
            if (itemId && itemId.length) {
                ood.arr.each(itemId, function (r, item) {
                    if (item = ns.getItemByItemId(r)) {
                        if (show === false) {
                            if (!item.hidden) hideNodes.merge(ns.getSubNodeByItemId('ITEM', item.id));
                        } else {
                            if (item.hidden) showNodes.merge(ns.getSubNodeByItemId('ITEM', item.id));
                        }
                        item.hidden = show === false;
                    }
                });
            }
            // reflect to dom
            if (!showNodes.isEmpty()) showNodes.css('display', '');
            if (!hideNodes.isEmpty()) hideNodes.css('display', 'none');
            return this;
        },
        getItems: function (type, v) {
            v = v || this.get(0).properties.items;
            if (type == 'data')
                return ood.clone(v, true);
            else if (type == 'min') {
                var a = ood.clone(v, true), b;
                ood.arr.each(a, function (o, i) {
                    a[i] = o.id;
                });
                return a;
            } else
                return v;
        },
        focusItem: function (itemId) {
            this.getSubNodeByItemId(this.constructor._focusNodeKey, itemId).focus(true);
            return this;
        },
        scrollIntoView: function (itemId) {
            itemId = this.getSubNodeByItemId(this.constructor._focusNodeKey, itemId);
            if (itemId = itemId.get(0)) itemId.scrollIntoView();
            return this;
        },
        selectItem: function (itemId) {
            return this.fireItemClickEvent(itemId);
        },
        fireItemClickEvent: function (itemId) {
            this.getSubNodeByItemId(this.constructor._focusNodeKey, itemId).onClick();
            return this;
        },
        editItem: function (itemId/*default is the current*/) {
            var profile = this.get(0),
                prop = profile.properties,
                item, source, v;
            if (profile && profile.renderId && !profile.destroyed) {
                itemId = ood.isHash(itemId) ? itemId.id : itemId === 0 ? 0 : itemId ? (itemId + '') : null;
                if (ood.isNumb(itemId)) itemId = ood.get(prop.items, [itemId, "id"]);
                if (!itemId) {
                    v = prop.$UIvalue || prop.value;
                    if (v) v = (v + '').split(prop.valueSeparator);
                    v = profile.getItemByItemId(v[0]);
                    itemId = v ? v.id : null;
                }
                if (itemId) {
                    if (item = profile.getItemByItemId(itemId)) {
                        source = profile.getSubNodeByItemId('ITEMCAPTION', itemId);
                        if (source.isEmpty()) source = profile.getSubNodeByItemId('CAPTION', itemId);
                        if (!source.isEmpty()) {
                            var pp = source.parent(),
                                cb = ood.browser.contentBox,
                                pos = source.offset(null, pp.get(0)),
                                size = source.cssSize(),
                                pos2 = pp.offset(),
                                size2 = pp.cssSize();

                            // adjust
                            pos2.left += !cb ? 0 : source._paddingW('left');
                            pos2.top += !cb ? 0 : source._paddingH('top');
                            size2.height += !cb ? 0 : source._paddingH();

                            var editor;
                            if (profile.beforeIniEditor) {
                                editor = profile.boxing().beforeIniEditor(profile, item, source);
                                if (editor === false)
                                    return;
                            }

                            if (!editor || !editor['ood.UI']) {
                                var editor = new ood.UI.ComboInput({type: "input"});
                                editor.setWidth(Math.max(size2.width - pos.left, 40))
                                    .setHeight(Math.max(size2.height, 20))
                                    .setResizer(true)
                                    .setValue(item.caption || "");
                                if (profile.onBeginEdit) profile.boxing().onBeginEdit(profile, item, editor);
                                var undo = function () {
                                    // ays is a must
                                    ood.resetRun('absList_editor_reset', function () {
                                        if (editor && !editor.isDestroyed()) {
                                            editor.getRoot().setBlurTrigger("absList_editor_blur", null);
                                            editor.destroy();
                                            editor = null;
                                        }
                                    });
                                };
                                editor.beforeUIValueSet(function (prf, ov, nv, force, tag) {
                                    if (false !== (profile.beforeEditApply && profile.boxing().beforeEditApply(profile, item, nv, editor, tag))) {
                                        profile.boxing().updateItem(item.id, {caption: nv});
                                        if (profile.onEndEdit) profile.boxing().onEndEdit(profile, item, editor);
                                        undo();
                                    }
                                }).onCancel(function () {
                                    undo();
                                });
                                ood('body').append(editor);
                                var root = editor.getRoot();

                                root.popToTop({
                                    left: pos.left + pos2.left,
                                    top: pos2.top
                                });
                                // For scroll to undo
                                root.setBlurTrigger("absList_editor_blur", function () {
                                    undo();
                                });
                                editor.activate();
                            }
                        }
                    }
                }
            }
            return this;
        },
        getSelectedItem: function () {
            var uiv = this.getUIValue(true),
                prf = this.get(0),
                items = [],
                item;
            if (ood.isArr(uiv)) {
                if (uiv.length) {
                    ood.arr.each(uiv, function (id) {
                        if (item = prf.getItemByItemId(id)) {
                            items.push(item);
                        }
                    });
                    return items;
                }
            } else if (uiv) {
                return prf.getItemByItemId(uiv);
            }
        }
    },
    Initialize: function () {
        var o = this.prototype;
        ood.arr.each(ood.toArr('getItemByItemId,getItemByItemCaption,getItemByDom,getSubIdByItemId,getSubNodeByItemId'), function (s) {
            o[s] = function () {
                var t = this.get(0);
                return t[s].apply(t, arguments);
            };
            ood.Class._fun(o[s], s, o.KEY, null, 'instance');
        });
    },
    Static: {
        _focusNodeKey: 'ITEM',
        $abstract: true,
        // for item in box array
        _ensureValues: ood.UI._ensureValues,

        DataModel: {
            listKey: {
                set: function (value) {
                    var o = this,
                        t = o.box.getCachedData(value);
                    if (t)
                        o.boxing().setItems(ood.clone(t));
                    else
                        o.boxing().setItems(o.properties.items);
                    o.properties.listKey = value;
                }
            },
            items: {
                ini: [],
                set: function (value) {
                    var o = this,
                        ins = o.boxing(),
                        items = o.properties.items,
                        children, ia, bv;

                    //bak value
                    if (typeof ins.setValue == 'function') {
                        bv = o.properties.value;
                        if (bv && value && value.length) {
                            var i = ood.arr.indexOf(value, bv);
                            if (i === -1)
                                i = ood.arr.subIndexOf(value, "id", o.properties.value);
                            //    if(i===-1)
                            //       bv=value?value[0]?value[0].id?value[0].id:value[0]:"":"";
                        }
                    }

                    // keep children objects
                    if (items && items.length) {
                        if (o.children && o.children.length) {
                            // use UIProfile's serialize function for module case
                            ood.arr.each(children = o.serialize(false, true).children, function (arr) {
                                if (arr[1]) {
                                    var i = ood.arr.indexOf(items, arr[1]);
                                    if (i === -1)
                                        i = ood.arr.subIndexOf(items, "id", arr[1]);
                                    if (i !== -1)
                                        arr[2] = i;
                                }
                            });
                            // destroy all
                            ins.removeChildren(true, true, true);
                        }
                        ins.clearItems(true, true);
                    }

                    ins.insertItems(value ? ood.copy(value) : null, null, null, true);

                    // restore children
                    if (value && value.length && children) {
                        var hash = {}, rhash = {}, len = value.length;
                        ood.arr.each(value, function (item, i) {
                            hash[item.id || item] = i;
                            rhash[i] = item.id || item;
                        });
                        ood.arr.each(children, function (arr) {
                            var added, t;
                            if (ood.isSet(arr[1])) {
                                // add by id
                                if (ood.isSet(hash[arr[1]])) {
                                    t = ood.create(arr[0]);
                                    if (o.$panelRestore) o.$panelRestore(t.get(0));
                                    ins.append(t, arr[1]);
                                    added = 1;
                                } else {
                                    // add by index
                                    if (rhash[arr[2]]) {
                                        t = ood.create(arr[0]);
                                        if (o.$panelRestore) o.$panelRestore(t.get(0));
                                        ins.append(t, rhash[arr[2]]);
                                        added = 1;
                                    }
                                }
                            }
                            if (!added) {
                                t = ood.create(arr[0]);
                                if (o.$panelRestore) o.$panelRestore(t.get(0));
                                ins.append(t, bv);
                            }
                        });
                    }

                    //try to set value
                    if (ood.isSet(bv)) {
                        ins.setValue(bv, true, 'items');
                    }

                    if (o.renderId) {
                        o.adjustSize();
                    }
                }
            },
            dragSortable: false,
            valueSeparator: ';'
        },
        RenderTrigger: function () {
            this.destroyTrigger = function () {
                ood.each(this.SubSerialIdMapItem, function (o) {
                    ood.breakO(o)
                });
                this.properties.items.length = 0;
            };
        },
        EventHandlers: {
            beforePrepareItem: function (profile, item, pid) {
            },
            beforeIniEditor: function (profile, item, captionNode) {
            },
            onBeginEdit: function (profile, item, editor) {
            },
            beforeEditApply: function (profile, item, caption, editor, tag) {
            },
            onEndEdit: function (profile, item, editor) {
            }
        },
        getDropKeys: function (profile, node) {
            var item = profile.getItemByDom(node);
            return (item && item.dropKeys) || profile.properties.dropKeys;
        },
        getDragKey: function (profile, node) {
            var item = profile.getItemByDom(node);
            return (item && item.dragKey) || profile.properties.dragKey || (profile.properties.dragSortable && (profile.key + ":" + profile.$xid));
        },
        _adjustItems: function (arr) {
            if (!ood.isSet(arr)) arr = [];
            if (!ood.isArr(arr)) arr = [arr];
            var a = ood.copy(arr), m;
            ood.arr.each(a, function (o, i) {
                if (ood.isArr(o) && o.length) {
                    a[i] = {id: o[0]};
                    a[i].id = ood.isSet(a[i].id) ? (a[i].id + '') : ood.id();
                    if (ood.isSet(o[1])) a[i].caption = o[1] + '';
                } else if (ood.isHash(o)) {
                    a[i] = ood.copy(o);
                    a[i].id = ood.isSet(a[i].id) ? (a[i].id + '') : ood.id();
                } else
                    a[i] = {id: o + ''};
            });
            return a;
        },
        //
        _showTips: function (profile, node, pos) {
            if (profile.properties.disableTips) return;
            if (profile.onShowTips)
                return profile.boxing().onShowTips(profile, node, pos);
            if (!ood.Tips) return;

            var t = profile.properties,
                id = node.id,
                sid = profile.getSubId(id),
                map = profile.SubSerialIdMapItem,
                item = map && map[sid];

            if (item && ('tips' in item)) {
                if (item.tips) ood.Tips.show(pos, item);
                else ood.Tips.hide();
                return false;
            } else if (profile.properties.autoTips && item && 'caption' in item) {
                if (item.caption || item.comment) ood.Tips.show(pos, {tips: ood.adjustRes((item.caption || '') + (item.caption || item.comment ? '<br/>' : '') + (item.comment || ''), true, false, null, null, item)});
                else ood.Tips.hide();
                return false;
            } else
                return true;
        }
    }
});

//absValue Class
ood.Class("ood.absValue", "ood.absObj", {
    Instance: {
        /*
        getUIValue:         return $UIvalue
        setUIValue:         set $UIvalue,and _setCtrlValue                   beforeUIValueSet/afterUIValueSet
        getValue:           return value
        setValue:           set value, set $UIvalue, and _setCtrlValue       beforeValueSet/afterValueSet
        resetValue:         reset value,UIvalue,Ctrlvalue not trigger event
        updateValue:        set $UIvalue to value

        _setCtrlValue:      change control value                *need to be overwritten
        _getCtrlValue:      get value from control              *need to be overwritten
        _setDirtyMark:      mark UI ctrl when value!==UIvalue   *need to be overwritten
        */
        _getCtrlValue: function () {
            return this.get(0).properties.$UIvalue
        },
        _getCtrlCatpionValue: function () {
            return this.get(0).properties.$UICatpionValue
        },
        _setCtrlValue: function (value) {
            return this
        },
        _setDirtyMark: function (key) {
            return this.each(function (profile) {
                if (!profile.renderId) return;
                var properties = profile.properties,
                    flag = properties.value !== properties.$UIvalue,
                    o = profile.getSubNode(key || profile.box.DIRYMARKICON || "KEY"),
                    d = ood.UI.$css_tag_dirty;
                if (profile._dirtyFlag !== flag) {
                    if (properties.dirtyMark && properties.showDirtyMark) {
                        if (profile.beforeDirtyMark && false === profile.boxing().beforeDirtyMark(profile, flag)) {
                        }
                        else {
                            if (flag) o.addClass(d);
                            else o.removeClass(d);
                        }
                    }
                    profile._dirtyFlag = flag;
                }
            });
        },
        getValue: function (returnArr) {
            var prf = this.get(0),
                prop = prf.properties,
                v = prop.value;

            if (prf.box.$valuemode == 'multi')
                if (returnArr)
                    if (ood.isStr(v))
                        v = v.split(prop.valueSeparator);

            if (prf.box.$DataModel.selMode && (prop.selMode == 'multi' || prop.selMode == 'multibycheckbox') && returnArr) {
                if (ood.isStr(v))
                    v = v.split(prop.valueSeparator);
                if (v && ood.isArr(v) && v.length > 1)
                    v.sort();
            }
            return v;
        },


        getSelectedItem: function () {
            var uiv = this.getUIValue(true),
                prf = this.get(0),
                items = [],
                item;
            if (ood.isArr(uiv)) {
                if (uiv.length) {
                    ood.arr.each(uiv, function (id) {
                        if (item = prf.getItemByItemCaption(id)) {
                            items.push(item);
                        }
                    });
                    return items;
                }
            } else if (uiv) {
                return prf.getItemByItemCaption(uiv);
            }
        },


        setCaptionValue: function (value) {
            var prf = this.get(0),
                prop = prf.properties;
            prop.$CaptionValue = value;

        },


        _eachValue: function (captionarr, item, key) {
            var values = this.getUIValue(true);
            var ui = this;
            ood.each(item.sub, function (subitem) {
                if (ood.arr.indexOf(values, subitem[key]) > -1) {
                    captionarr.push(subitem.caption)
                }

                if (subitem.sub) {
                    captionarr = ui._eachValue(captionarr, subitem, key);
                }
            });
            return captionarr;
        },

        getCaptionValue: function (key) {
            var prf = this.get(0), prop = prf.properties;
            var cationarr = [];
            var ui = this;
            var items = this.getItems('data');
            var values = this.getUIValue(true);
            if (!key) {
                key = 'id'
            }
            ood.each(items, function (item) {
                if (ood.isArr(values)) {
                    if (ood.arr.indexOf(values, item[key]) > -1) {
                        cationarr.push(item.caption)
                    }
                }
                cationarr = ui._eachValue(cationarr, item, key);
            })
            captionvalue = cationarr.join(prop.valueSeparator);
            return captionvalue;

        },

        getUICationValue: function (returnArr) {
            var prf = this.get(0),
                prop = prf.properties;

            if (!prf.renderId)
                return prop && prop.value;

            var cv = this._getCtrlValue(), v;
            if (!prf.box._checkValid || false !== prf.box._checkValid(prf, cv))
                prop.caption = cv;
            v = prop.caption;

            if (prf.box.$valuemode == 'multi')
                if (returnArr)
                    if (ood.isStr(v))
                        v = v.split(prop.valueSeparator);

            if (prf.box.$DataModel.selMode && (prop.selMode == 'multi' || prop.selMode == 'multibycheckbox') && returnArr) {
                if (ood.isStr(v))
                    v = v.split(prop.valueSeparator);
                if (v && ood.isArr(v) && v.length > 1)
                    v.sort();
            }
            return v;
        },

        getUIValue: function (returnArr) {
            var prf = this.get(0),
                prop = prf.properties;

            if (!prf.renderId)
                return prop && prop.value;

            var cv = this._getCtrlValue(), v;
            if (!prf.box._checkValid || false !== prf.box._checkValid(prf, cv))
                prop.$UIvalue = cv;
            v = prop.$UIvalue;

            if (prf.box.$valuemode == 'multi')
                if (returnArr)
                    if (ood.isStr(v))
                        v = v.split(prop.valueSeparator);

            if (prf.box.$DataModel.selMode && (prop.selMode == 'multi' || prop.selMode == 'multibycheckbox') && returnArr) {
                if (ood.isStr(v))
                    v = v.split(prop.valueSeparator);
                if (v && ood.isArr(v) && v.length > 1)
                    v.sort();
            }
            return v;
        },
        resetValue: function (value) {
            var self = this;
            self.each(function (profile) {
                var r, pro = profile.properties, ins = profile.boxing(),
                    v = typeof (r = profile.box._ensureValue) == 'function' ? r.call(profile.box, profile, value) : value;
                if (pro.value !== v || pro.$UIvalue !== v) {
                    if (profile.box._beforeResetValue) profile.box._beforeResetValue(profile);
                    if (typeof(r = profile.$onValueSet) == 'function') {
                        r = r.call(profile, pro.value, v);
                        if (ood.isSet(r)) v = r;
                    }

                    // _setCtrlValue maybe use $UIvalue
                    profile.boxing()._setCtrlValue(pro.value = v);
                    // So, maintain $UIvalue during _setCtrlValue call
                    pro.$UIvalue = v;
                }
                profile._inValid = 1;
                ins._setDirtyMark();
            });
            return self;
        },
        setUIValue: function (value, force, triggerEventOnly, tag) {
            var self = this;
            this.each(function (profile) {
                var prop = profile.properties, r,
                    ovalue = prop.$UIvalue,
                    box = profile.boxing();
                if (force || (ovalue !== value)) {
                    if (
                        (profile.box._checkValid && false === profile.box._checkValid(profile, value)) ||
                        (profile.beforeUIValueSet && false === (r = box.beforeUIValueSet(profile, ovalue, value, force, tag)))
                    )
                        return;

                    //can get return value
                    if (r !== undefined && typeof r !== 'boolean') value = r;
                    //before _setCtrlValue
                    if (profile.box && (typeof (r = profile.box._ensureValue) == 'function'))
                        value = r.call(profile.box, profile, value);
                    if (typeof(r = profile.$onUIValueSet) == 'function') {
                        r = r.call(profile, value, force, tag);
                        if (ood.isSet(r)) value = r;
                    }

                    //before value copy
                    var cv;
                    if (profile.renderId && !triggerEventOnly) {
                        cv = 1;
                        box._setCtrlValue(value);
                    }

                    //value copy
                    prop.$UIvalue = value;

                    if (profile.renderId && !triggerEventOnly) box._setDirtyMark();

                    if (profile.afterUIValueSet) box.afterUIValueSet(profile, ovalue, value, force, tag);
                    if (profile._onChange) box._onChange(profile, ovalue, value, force, tag);
                    if (profile.onChange) box.onChange(profile, ovalue, value, force, tag);

                    if (!prop.dirtyMark)
                        box.setValue(value, false, 'uiv', cv || triggerEventOnly);

                    if (prop.excelCellId && box.notifyExcel) {
                        box.notifyExcel(false);
                    }
                }
            });
            return this;
        },
        updateValue: function () {
            return this.each(function (profile) {
                var prop = profile.properties;
                if (prop.value !== prop.$UIvalue) {
                    var ins = profile.boxing();
                    if (ins.checkValid()) {
                        // prop.value = ins.getUIValue();
                        ins.setValue(ins.getUIValue(), true, 'update');
                        ins._setDirtyMark();
                    }
                }
            });
        },
        isDirtied: function () {
            var dirtied = false;
            this.each(function (profile) {
                var p = profile.properties;

                // inner value is alway string
                dirtied = (p.value + " ") !== (p.$UIvalue + " ");
                if (dirtied)
                    return false;
            });
            return dirtied
        },
        checkValid: function (value) {
            var prop, tr, r = true, outv = ood.isSet(value);
            this.each(function (profile) {
                prop = profile.properties;
                tr = true;

                // for checking html ctrl valid, <input> only
                if (profile.box._checkValid2)
                // r must be at the end
                    r = (tr = profile.box._checkValid2(profile)) && r;
                if (tr && profile.box._checkValid)
                //r must be at the end
                    r = profile.box._checkValid(profile, outv ? value : prop.$UIvalue) && r;

                if (!outv && profile.renderId)
                    profile.boxing()._setDirtyMark();
            });
            return r;
        }
    },
    Static: {
        $abstract: true,
        DataModel: {
            readonly: {
                ini: false,
                action: function (v) {
                    var i = this.getRoot();
                    if (v)
                        i.addClass('ood-ui-readonly');
                    else
                        i.removeClass('ood-ui-readonly');
                }
            },
            required: {
                ini: false,
                // mark required
                action: function (v) {
                    // if (this.keys['LABEL']) {
                    //     var node = this.getSubNode('LABEL');
                    //     if (v) node.addClass('ood-required');
                    //     else node.removeClass('ood-required');
                    // }
                    if (v) this.getRoot().addClass('ood-required-field');
                    else this.getRoot().removeClass('ood-required-field');
                }
            },
            // setValue and getValue
            value: {
                ini: null,
                set: function (value, force, tag, triggerEventOnly) {
                    var profile = this,
                        p = profile.properties, r,
                        ovalue = p.value,
                        box = profile.boxing();

                    //check format
                    if (profile.box._checkValid && profile.box._checkValid(profile, value) === false) return;
                    //if return false in beforeValueSet, not set
                    if (profile.beforeValueSet && false === (r = box.beforeValueSet(profile, ovalue, value, force, tag))) return;
                    // can get return value
                    if (r !== undefined) value = r;
                    //before _setCtrlValue
                    //ensure value
                    if (typeof (r = profile.box._ensureValue) == 'function')
                        value = r.call(profile.box, profile, value);

                    if (typeof(r = profile.$onValueSet) == 'function') {
                        r = r.call(profile, ovalue, value, force, tag);
                        if (ood.isSet(r)) value = r;
                    }

                    //before value copy
                    if (profile.renderId && !triggerEventOnly) box._setCtrlValue(value);
                    //value copy
                    p.value = p.$UIvalue = value;

                    if (!profile._inValid) profile._inValid = 1;
                    if (profile.renderId) box._setDirtyMark();
                    if (profile.afterValueSet) box.afterValueSet(profile, ovalue, value, force, tag);
                    if (profile.onValueChange) box.onValueChange(profile, ovalue, value, force, tag);
                }
            },
            isFormField: true,
            dirtyMark: true,
            showDirtyMark: true
        },
        // for item in box array
        _ensureValues: ood.UI._ensureValues,
        // for value
        _ensureValue: function (profile, value) {
            var prop = profile.properties;
            if (profile.box.$DataModel.selMode && (prop.selMode == 'multi' || prop.selMode == 'multibycheckbox')) {
                if (!ood.isArr(value)) {
                    value = (value ? ('' + value) : '').split(prop.valueSeparator);
                }
                value.sort();
                return value.join(prop.valueSeparator);
            } else
                return ood.isArr(value) ? value[0] : value;
        },
        EventHandlers: {
            //real value set
            beforeValueSet: function (profile, oldValue, newValue, force, tag) {
            },
            afterValueSet: function (profile, oldValue, newValue, force, tag) {
            },
            onValueChange: function (profile, oldValue, newValue, force, tag) {
            },

            //ui value set
            beforeUIValueSet: function (profile, oldValue, newValue, force, tag) {
            },
            afterUIValueSet: function (profile, oldValue, newValue, force, tag) {
            },
            onChange: function (profile, oldValue, newValue, force, tag) {
            },
            _onChange: function (profile, oldValue, newValue, force, tag) {
            },

            beforeDirtyMark: function (profile, dirty) {
            }
        },
        RenderTrigger: function () {
            var self = this, b = self.boxing(), p = self.properties, t;
            // disable dataField for container control
            if (!self.behavior.PanelKeys) {
                if (t = p.dataBinder) b.setDataBinder(t, true);
                if (t = p.dataField) b.setDataField(t);
            }

            if (p.value !== undefined) {
                if (typeof (t = self.box._ensureValue) == 'function') {
                    p.value = t.call(self.box, self, p.value);
                    if (p.$UIvalue)
                        p.$UIvalue = t.call(self.box, self, p.$UIvalue);
                }
                if (!p.$UIvalue)
                    p.$UIvalue = p.value;
                b._setCtrlValue(p.$UIvalue, true);
            }
        }
    }
});

ood.Class("ood.UI.Widget", "ood.UI", {
    Static: {
        Appearances: {
            KEY: {},
            IE67_SHADOW: (ood.browser.ie && ood.browser.ver <= 8) ? {
                'z-index': '-1',
                position: 'absolute',
                left: 0,
                top: 0,
                width: '100%',
                height: '100%',
                overflow: 'visible'
            } : null
        },
        Templates: {
            className: 'ood-uiw-shell {_className} {_requiredfld}',
            style: '{_style}',

            IE67_SHADOW: (ood.browser.ie && ood.browser.ver <= 8) ? {} : null,
            FRAME: {
                $order: 2,
                className: 'ood-uiw-frame ',
                BORDER: {
                    $order: 1,
                    style: 'width:{bWidth};height:{bHeight};',
                    className: 'ood-uiw-border'
                }
            }
        },
        DataModel: {
            width: {
                $spaceunit: 1,
                ini: '10em'
            },
            height: {
                $spaceunit: 1,
                ini: '10em'
            },
            shadow: {
                ini: false,
                action: function (v) {
                    if (ood.browser.ie && ood.browser.ver <= 8) {
                        var node = this.getSubNode('IE67_SHADOW');
                        if (v) node.addClass('ood-ui-shadow ood-uiborder-r ood-uiborder-b ood-uiborder-radius-br');
                        else node.removeClass('ood-ui-shadow ood-uiborder-r ood-uiborder-b ood-uiborder-radius-br');
                    } else {
                        var node = this.getSubNode('BORDER');
                        if (v) node.addClass('ood-ui-shadow');
                        else node.removeClass('ood-ui-shadow');
                    }
                }
            },
            //hide props( with px)
            $hborder: 0,
            $vborder: 0
        },
        RenderTrigger: function () {
            var self = this, p = self.properties, o = self.boxing();

            if (self.renderId)
                if ((!self.$noB) && p.border && o._border) o._border();

            if ((!self.$noR) && p.resizer && o.setResizer) o.setResizer(p.resizer, true);
            if ((!self.$noS) && p.shadow && o.setShadow) o.setShadow(true, true);
        },
        _onresize: function (profile, width, height) {
            var prop = profile.properties,
                border = profile.getSubNode('BORDER'),
                cb = border.contentBox(),
                shadow = (ood.browser.ie && ood.browser.ver <= 8) ? profile.getSubNode('IE67_SHADOW') : null,
                region,
                us = ood.$us(profile),
                adjustunit = function (v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate)
                },
                //caculate with px
                ww = profile.$px(width),
                hh = profile.$px(height),
                left = !cb ? 0 : Math.max(0, (prop.$b_lw || 0) - (prop.$hborder || 0)),
                top = !cb ? 0 : Math.max(0, (prop.$b_tw || 0) - (prop.$vborder || 0));

            if (ww && 'auto' !== ww) {
                ww -= !cb ? 0 : Math.max((prop.$hborder || 0) * 2, (prop.$b_lw || 0) + (prop.$b_rw || 0));
                /*for ie6 bug*/
                /*for example, if single number, 100% width will add 1*/
                /*for example, if single number, attached shadow will overlap*/
                if (ood.browser.ie && ood.browser.ver <= 6) ww = (profile.$px(ww / 2)) * 2;
            }
            if (hh && 'auto' !== hh) {
                hh -= !cb ? 0 : Math.max((prop.$vborder || 0) * 2, (prop.$b_lw || 0) + (prop.$b_rw || 0));

                if (ood.browser.ie && ood.browser.ver <= 6) hh = (profile.$px(hh / 2)) * 2;
                /*for ie6 bug*/
                if (ood.browser.ie && ood.browser.ver <= 6 && null === width) {
                    border.ieRemedy();
                    if (shadow) shadow.ieRemedy();
                }
            }
            region = {
                left: adjustunit(left),
                top: adjustunit(top),
                width: adjustunit(ww),
                height: adjustunit(hh)
            };
            border.cssRegion(region);
            if (shadow) shadow.cssRegion(region);

            /*for ie6 bug*/
            if ((profile.$resizer) && ood.browser.ie) {
                border.ieRemedy();
                if (shadow) shadow.ieRemedy();
            }
            return region;
        }
    }
});

ood.Class("ood.UI.Link", "ood.UI", {
    Instance: {
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        }
    },
    Static: {
        Appearances: {
            KEY: {
                cursor: 'pointer'
            }
        },
        Templates: {
            tagName: 'a',
            className: '{_className}',
            style: '{_style}',
            href: "{href}",
            target: '{target}',
            tabindex: '{tabindex}',
            text: '{caption}'
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY'},
            ClickEffected: {KEY: 'KEY'},
            onClick: function (profile, e, src) {
                var r;
                if (!profile.properties.disabled && profile.onClick)
                    r = profile.boxing().onClick(profile, e, src);
                //**** if dont return false, this click will break jsonp in IE
                //**** In IE, click a fake(javascript: or #) href(onclick not return false) will break the current script downloading
                var href = ood.use(src).attr('href');
                return typeof r == 'boolean' ? r : (href.indexOf('javascript:') === 0 || href.indexOf('#') === 0) ? false : true;
            }
        },
        DataModel: {
            caption: {
                ini: undefined,
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getRoot().html(ood.adjustRes(v, true));
                }
            },
            href: {
                ini: ood.$DEFAULTHREF,
                action: function (v) {
                    this.getRoot().attr('href', v);
                }
            },
            target: {
                action: function (v) {
                    this.getRoot().attr('target', v);
                }
            }
        },
        EventHandlers: {
            onClick: function (profile, e) {
            }
        }
    }
});

ood.Class("ood.UI.Element", "ood.UI", {
    Instance: {
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        }
    },
    Static: {
        _objectProp: {attributes: 1},
        Templates: {
            _NativeElement: true,
            tagName: '{nodeName}',
            // dont set class to HTML Element
            className: 'ood-node ood-wrapper {_className}',
            style: '{_style};',
            //for firefox div focus bug: outline:none; tabindex:'-1'
            tabindex: '{tabindex}',
            text: '{html}' + ood.UI.$childTag
        },
        DataModel: {
            width: {
                $spaceunit: 1,
                ini: '8em'
            },
            height: {
                $spaceunit: 1,
                ini: '1em'
            },
            nodeName: {
                ini: "ood",
                action: function (v) {
                    this.boxing().refresh();
                }
            },
            selectable: true,
            html: {
                html: 1,
                action: function (v, ov, force) {
                    this.getRoot().html(ood.adjustRes(v, 0, 1), null, null, force);
                }
            },
            attributes: {
                ini: {},
                action: function (v, ov) {
                    var root = this.getRoot();
                    if (!ood.isEmpty(ov)) {
                        ood.each(ov, function (o, i) {
                            root.attr(i, null);
                        });
                    }
                    if (!ood.isEmpty(v)) {
                        ood.each(v, function (o, i) {
                            root.attr(i, o);
                        });
                    }
                }
            },
            tabindex: -1
        },
        Appearances: {
            KEY: {
                'line-height': 'normal'
            }
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY'},
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            }
        },
        EventHandlers: {
            onClick: function (profile, e, value) {
            }
        },
        RenderTrigger: function () {
            var v = this.properties.attributes;
            if (!ood.isEmpty(v)) {
                var root = this.getRoot();
                ood.each(v, function (o, i) {
                    root.attr(i, o);
                });
            }
        }
    }
});

ood.Class("ood.UI.Icon", "ood.UI", {
    Instance: {
        iniProp: {
            imageClass: 'ri-image-line',
            fontSize: '3em'
        },
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        }
    },
    Static: {
        Templates: {
            className: 'ood-node ood-wrapper {_className}  {picClass}',
            style: '{_style};',
            ICON: {
                className: 'oodcon {imageClass}',
                style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{imageDisplay}{_fontsize}{_fontclr}{iconStyle}',
                text: '{iconFontCode}'
            }
        },
        DataModel: {
            selectable: null,
            html: null,
            attributes: null,
            renderer: null,
            defaultFocus: null,
            tabindex: -1,
            image: {
                format: 'image',
                action: function (v) {
                    ood.UI.$iconAction(this);
                }
            },
            imagePos: {
                action: function (v) {
                    this.getSubNode('ICON').css('backgroundPosition', v || 'center');
                }
            },
            imageBgSize: {
                action: function (v) {
                    this.getSubNode('ICON').css('backgroundSize', v || '');
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
            iconFontSize: {
                action: function (v) {
                    this.getSubNode('ICON').css('fontSize', v || '');
                }
            },
            iconColor: {
                type: 'color',
                action: function (v) {
                    this.getSubNode('ICON').css('color', v || '');
                }
            }
        },
        Appearances: {
            KEY: {
                'overflow': 'visible'
            },
            ICON: {
                'position': 'relative',
                display: ood.$inlineBlock,
                zoom: ood.browser.ie ? 1 : null
            }
        },
        Behaviors: {
            HoverEffected: {ICON: 'ICON'},
            ClickEffected: {ICON: 'ICON'},
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            }
        },
        EventHandlers: {
            onClick: function (profile, e, value) {
            }
        },
        _prepareData: function (profile) {
            var data = arguments.callee.upper.call(this, profile);
            if (data.iconFontSize) data._fontsize = data.iconFontSize + ';';
            if (data.iconColor) data._fontclr = 'color:' + data.iconColor + ';';
            return data;
        }
    }
});

ood.Class("ood.UI.HTMLButton", "ood.UI.Element", {
    Instance: {
        activate: function () {
            this.getRoot().focus(true);
            return this;
        }
    },
    Static: {
        Templates: {
            tagName: 'button',
            className: 'ood-ui-unselectable ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius {_className}',
            style: '{_style}{_fc}{_fw}{_fs}{_ff};',
            tabindex: '{tabindex}',
            text: '{caption}' + ood.UI.$childTag
        },
        Appearances: {
            KEY: {
                cursor: 'pointer',
                padding: '.334em'
            }
        },
        DataModel: {
            nodeName: null,
            tabindex: 1,
            width: 'auto',
            height: 'auto',
            html: {
                hidden: 1,
                get: function () {
                    return this.boxing().getCaption()
                },
                set: function (v, ov) {
                    return this.boxing().setCaption(v, ov)
                }
            },
            caption: {
                ini: undefined,
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getRoot().html(ood.adjustRes(v, 0, 1));
                }
            },
            disabled: {
                ini: false,
                action: function (v) {
                    var i = this.getRoot(),
                        cls = "ood-ui-disabled";

                    if (v) this.getRoot().addClass(cls);
                    else this.getRoot().removeClass(cls);
                    i.attr('disabled', v ? "1" : null);
                }
            },
            shadow: {
                ini: false,
                action: function (v) {
                    var node = this.getRoot();
                    if (v) node.addClass('ood-ui-shadow');
                    else node.removeClass('ood-ui-shadow');
                }
            },
            fontColor: {
                ini: '',
                type: "color",
                action: function (value) {
                    this.getRoot().css('color', value);
                }
            },
            fontSize: {
                combobox: ["", "14px", "18px", "22px", "30px"],
                action: function (value) {
                    this.getRoot().css('fontSize', value);
                }
            },
            fontWeight: {
                combobox: ["", "normal", "bolder", "bold", "lighter", "100", "200", "300", "400", "500", "600", "700", "800", "900"],
                action: function (value) {
                    this.getRoot().css('fontWeight', value);
                }
            },
            fontFamily: {
                combobox: ["", "arial", "sans-serif", "comic", "courier new", "monospace", "serif", "times new roman", "wingdings"],
                action: function (value) {
                    this.getRoot().css('fontFamily', value);
                }
            }
        },
        _prepareData: function (profile, data) {
            data = arguments.callee.upper.call(this, profile, data);
            var v;
            if (data.clock) data.caption = '';
            if (v = data.fontSize) data._fs = 'font-size:' + v + ';';
            if (v = data.fontWeight) data._fw = 'font-weight:' + v + ';';
            if (v = data.fontColor) data._fc = 'color:' + v + ';';
            if (v = data.fontFamily) data._ff = 'font-family:' + v + ';';
            data._hAlign = 'text-align:' + (data.hAlign || '');
            data._vAlign = 'vertical-align:' + (data.vAlign || '');
            return data;
        },

        RenderTrigger: function () {
            var self = this, p = self.properties, o = self.boxing();
            if ((!self.$noS) && p.shadow && o.setShadow) o.setShadow(true, true);
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY'}
        }
    }
});

ood.Class("ood.UI.Button", ["ood.UI.HTMLButton", "ood.absValue"], {
    Initialize: function () {
        // compitable
        ood.UI.SButton = ood.UI.Button;
        var key = "ood.UI.SButton";
        ood.absBox.$type[key.replace("ood.UI.", "")] = ood.absBox.$type[key] = key;
        this.$activeClass$ = 'ood.UI.Button';
    },
    Instance: {
        activate: function () {
            this.getRoot().focus(true);
            return this;
        },
        _setCtrlValue: function (value) {
            if (ood.isNull(value) || !ood.isDefined(value)) value = false;
            return this.each(function (profile) {
                var pp = profile.properties;
                if (pp.type != 'status') return;
                profile.getRoot().tagClass('-checked', value);
            });
        },
        resetValue: function (value) {
            this.each(function (p) {
                if (p.properties.type == 'drop')
                    p.boxing().setCaption("", true);
            });
            var upper = arguments.callee.upper,
                rtn = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return rtn;
        },
        setUIValue: function (value, force) {
            this.each(function (profile) {
                var p = profile.properties;
                if (p.$UIvalue !== value && p.type == 'drop')
                    profile.boxing().setCaption("", true);
            });
            var upper = arguments.callee.upper,
                rtn = upper.apply(this, ood.toArr(arguments));
            upper = null;
            return rtn;
        }
    },
    Static: {
        //for IE67 and dirtymark
        DIRYMARKICON: "BACKGROUND",
        Templates: {
            tagName: 'button',
            className: 'ood-ui-unselectable ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius {_className}',
            style: '{_align}{_style}{_fc}{_fw}{_fs}{_ff};',

            tabindex: '{tabindex}',
            BACKGROUND: {
                tagName: 'div'
            },
            ICON: {
                $order: 1,
                className: 'oodcon {imageClass}  {picClass}',
                style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                text: '{iconFontCode}'
            },
            CAPTION: {
                $order: 2,
                className: '',

                text: '{caption}'
            },
            DROP: {
                $order: 3,
                className: 'oodcon ood-special-icon',
                style: '{_showDrop}',
                $fonticon: 'ood-uicmd-arrowdrop'
            }
        },
        Appearances: {
            //for IE67 and dirtymark
            BACKGROUND: {
                'z-index': -1,
                position: 'absolute',
                left: 0,
                top: 0,
                width: '100%',
                height: '100%'
            },
            DROP: {
                'vertical-align': 'middle',
                'padding-left': '.66667em'
            }
        },
        Behaviors: {
            HoverEffected: {KEY: ['KEY', 'DROP']},
            NavKeys: {KEY: 1},
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                var b = profile.boxing();
                if (p.type == 'status') {
                    if (p.readonly) return false;
                    b.setUIValue(!p.$UIvalue, null, null, 'click');
                    if (profile.onChecked)
                        b.onChecked(profile, e, p.$UIvalue);
                }
                //onClick event
                if (profile.onClick)
                    return b.onClick(profile, e, src, p.$UIvalue);
                if (p.type == 'drop' && profile.onClickDrop)
                    return b.onClickDrop(profile, e, src, p.$UIvalue);
            },
            onKeydown: function (profile, e, src) {
                var keys = ood.Event.getKey(e), key = keys.key;
                if (key == ' ' || key == 'enter') {
                    profile.getSubNode('KEY').afterMousedown();
                    profile.__fakeclick = 1;
                }
            },
            onKeyup: function (profile, e, src) {
                var keys = ood.Event.getKey(e), key = keys.key;
                if (key == ' ' || key == 'enter') {
                    profile.getSubNode('KEY').afterMouseup();
                    if (profile.__fakeclick)
                        ood.use(src).onClick();
                }
                delete profile.__fakeclick;
            }
        },
        DataModel: {
            html: null,
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
            caption: {
                ini: undefined,
                action: function (v) {
                    v = (ood.isSet(v) ? v : "") + "";
                    this.getSubNode('CAPTION').html(ood.adjustRes(v, 0, 1));
                }
            },
            hAlign: {
                ini: 'center',
                listbox: ['left', 'center', 'right'],
                action: function (v) {
                    this.getRoot().css('textAlign', v);
                }
            },
            vAlign: {
                ini: 'middle',
                listbox: ['top', 'middle', 'bottom'],
                action: function (v) {
                    //todo
                }
            },
            value: {
                ini: false
            },
            fontColor: {
                action: function (value) {
                    this.getSubNode("CAPTION").css('color', value);
                }
            },
            fontSize: {
                combobox: ["", "14px", "18px", "22px", ".75em", "1.5em", "2em", "3em"],
                action: function (value) {
                    this.getSubNode("CAPTION").css('fontSize', value);
                }
            },
            fontWeight: {
                action: function (value) {
                    this.getSubNode("CAPTION").css('fontWeight', value);
                }
            },
            fontFamily: {
                action: function (value) {
                    this.getSubNode("CAPTION").css('fontFamily', value);
                }
            },
            type: {
                ini: 'normal',
                listbox: ['normal', 'status', 'drop'],
                action: function (value) {
                    var self = this,
                        drop = self.getSubNode('DROP');
                    if (value == 'drop') {
                        drop.css('display', '');
                    }
                    else {
                        drop.css('display', 'none');
                    }
                }
            }
        },
        _isFormField: function (profile) {
            return profile.properties.type == "status" && profile.properties.isFormField;
        },
        _ensureValue: function (profile, value) {
            if (profile.properties.type == "status")
                return !!value;
            else
                return value;
        },
        _prepareData: function (profile, data) {
            var data = arguments.callee.upper.call(this, profile);
            var v;
            if (data.clock) data.caption = '';
            if (v = data.fontSize) data._fs = 'font-size:' + v + ';';
            if (v = data.fontWeight) data._fw = 'font-weight:' + v + ';';
            if (v = data.fontColor) data._fc = 'color:' + v + ';';
            if (v = data.fontFamily) data._ff = 'font-family:' + v + ';';
            data._hAlign = 'text-align:' + (data.hAlign || '');
            data._vAlign = 'vertical-align:' + (data.vAlign || '');

            data._showDrop = data.type == 'drop' ? '' : 'display:none';
            return data;
        },


        RenderTrigger: function () {
            var self = this, p = self.properties, o = self.boxing();
            //set value later
            if (p.type == 'status' && p.value)
                o.setValue(true, true, 'render');
        },
        EventHandlers: {
            onClick: function (profile, e, src, value) {
            },
            onClickDrop: function (profile, e, src, value) {
            },
            onChecked: function (profile, e, value) {
            }
        }
    }
});

ood.Class("ood.UI.Span", "ood.UI", {
    Instance: {
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        }
    },
    Static: {
        Templates: {
            className: '{_className}',
            style: '{_style};{_overflow};',
            //for firefox div focus bug: outline:none; tabindex:'-1'
            tabindex: '{tabindex}',
            text: '{html}' + ood.UI.$childTag
        },
        DataModel: {
            width: {
                $spaceunit: 1,
                ini: '2em'
            },
            height: {
                $spaceunit: 1,
                ini: '1em'
            },
            selectable: true,
            html: {
                html: 1,
                action: function (v, ov, force) {
                    this.getRoot().html(ood.adjustRes(v, 0, 1), null, null, force);
                }
            },
            overflow: {
                ini: ood.browser.deviceType == "touchOnly" ? 'auto' : undefined,
                combobox: ['', 'visible', 'hidden', 'scroll', 'auto', 'overflow-x:hidden;overflow-y:auto', 'overflow-x:auto;overflow-y:hidden'],
                action: function (v) {
                    var node = this.getContainer();
                    if (v) {
                        if (v.indexOf(':') != -1) {
                            ood.arr.each(v.split(/\s*;\s*/g), function (s) {
                                var a = s.split(/\s*:\s*/g);
                                if (a.length > 1) node.css(ood.str.trim(a[0]), ood.str.trim(a[1] || ''));
                            });
                            return;
                        }
                    }
                    node.css('overflow', v || '');
                }
            },
            tabindex: -1
        },
        Appearances: {
            KEY: {
                'line-height': 'normal'
            }
        },
        Behaviors: {
            HoverEffected: {KEY: 'KEY', ICON: 'ICON'},
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            }
        },
        EventHandlers: {
            onClick: function (profile, e, value) {
            }
        },
        _prepareData: function (profile, data) {
            data = arguments.callee.upper.call(this, profile, data);
            if (ood.isStr(data.overflow))
                data._overflow = data.overflow.indexOf(':') != -1 ? (data.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");
            return data;
        }
    }
});

ood.Class("ood.UI.Div", "ood.UI", {
    Initialize: function () {
        // compitable
        ood.UI.Pane = ood.UI.Div;
        var key = "ood.UI.Pane";
        ood.absBox.$type[key.replace("ood.UI.", "")] = ood.absBox.$type[key] = key;
        this.$activeClass$ = 'ood.UI.Div';
    },
    Instance: {
        fireClickEvent: function () {
            this.getRoot().onClick();
            return this;
        }
    },
    Static: {
        Appearances: {
            KEY: {
                // overflow:(ood.browser.gek && !ood.browser.gek3)?'auto':null,
                outline: ood.browser.gek ? 'none' : null,
                zoom: (ood.browser.ie && ood.browser.ver < 9) ? '1' : null,
                background: ood.browser.ie ? 'url(' + ood.ini.img_bg + ') no-repeat left top' : null,
                'line-height': 'normal'
            }
        },
        Templates: {
            tagName: 'div',
            className: 'ood-uicontainer {_className}',
            style: '{_style};{_panelstyle};{_overflow};',
            //for firefox div focus bug: outline:none; tabindex:'-1'
            tabindex: '{tabindex}',
            text: '{html}' + ood.UI.$childTag
        },
        DataModel: {
            iframeAutoLoad: {
                ini: "",
                action: function () {
                    this.box._applyAutoLoad(this);
                }
            },
            ajaxAutoLoad: {
                ini: "",
                action: function () {
                    this.box._applyAutoLoad(this);
                }
            },
            width: {
                $spaceunit: 1,
                ini: 'auto'
            },
            height: {
                $spaceunit: 1,
                ini: 'auto'
            },
            selectable: true,
            html: {
                html: 1,
                action: function (v, ov, force) {
                    this.getRoot().html(ood.adjustRes(v, 0, 1), null, null, force);
                }
            },
            overflow: {
                ini: ood.browser.deviceType == "touchOnly" ? 'auto' : undefined,
                combobox: ['', 'visible', 'hidden', 'scroll', 'auto', 'overflow-x:hidden;overflow-y:auto', 'overflow-x:auto;overflow-y:hidden'],
                action: function (v) {
                    var node = this.getContainer();
                    if (v) {
                        if (v.indexOf(':') != -1) {
                            ood.arr.each(v.split(/\s*;\s*/g), function (s) {
                                var a = s.split(/\s*:\s*/g);
                                if (a.length > 1) node.css(ood.str.trim(a[0]), ood.str.trim(a[1] || ''));
                            });
                            return;
                        }
                    }
                    node.css('overflow', v || '');
                }
            },
            tabindex: -1
        },
        RenderTrigger: function () {
            // only div
            var ns = this;
            if (ns.box.KEY == "ood.UI.Div")
                if (ns.properties.iframeAutoLoad || ns.properties.ajaxAutoLoad)
                    ns.box._applyAutoLoad(this);
        },
        Behaviors: {
            DroppableKeys: ['KEY'],
            PanelKeys: ['KEY'],
            onClick: function (profile, e, src) {
                var p = profile.properties;
                if (p.disabled) return false;
                if (profile.onClick)
                    return profile.boxing().onClick(profile, e, src);
            }
        },
        EventHandlers: {
            onClick: function (profile, e, value) {
            }
        },
        _prepareData: function (profile, data) {
            data = arguments.callee.upper.call(this, profile, data);
            if (ood.isStr(data.overflow))
                data._overflow = data.overflow.indexOf(':') != -1 ? (data.overflow) : (data.overflow ? ("overflow:" + data.overflow) : "");
            return data;
        },
        _applyAutoLoad: function (prf) {
            var prop = prf.properties, ins = prf.boxing();
            if (prop.iframeAutoLoad) {
                ins.getContainer().css('overflow', 'hidden');
                var _if = typeof prop.iframeAutoLoad == 'string' ? {url: prop.iframeAutoLoad} : ood.clone(prop.iframeAutoLoad, true),
                    id = "biframe_" + ood.stamp(),
                    e = ood.browser.ie && ood.browser.ver < 9,
                    ifr = document.createElement(e ? "<iframe name='" + id + "'>" : "iframe");

                _if.url = ood.adjustRes(_if.url, false, true);

                ifr.id = ifr.name = id;
                if (ood.isHash(prop.iframeAutoLoad)) prop.iframeAutoLoad.frameName = id;
                prop._frameName = id;

                if (!_if.query) _if.query = {};
                _if.query._rand = ood.rand();
                ifr.frameBorder = '0';
                ifr.marginWidth = '0';
                ifr.marginHeight = '0';
                ifr.vspace = '0';
                ifr.hspace = '0';
                ifr.allowTransparency = 'true';
                ifr.width = '100%';
                ifr.height = '100%';
                ins.getContainer().html("", false);
                ins.append(ifr);

                if ((_if.method || "").toLowerCase() == "post")
                    ood.Dom.submit(_if.url, _if.query, "post", id, _if.enctype);
                else
                    ifr.src = _if.url;
                if (prf.$afterAutoLoad) prf.$afterAutoLoad.call(prf.boxing(), prf);
            } else if (prop.ajaxAutoLoad) {
                var _ajax = typeof prop.ajaxAutoLoad == 'string' ? {url: prop.ajaxAutoLoad} : ood.clone(prop.ajaxAutoLoad, true),
                    options = {rspType: "text"};
                if (!_ajax.query) _ajax.query = {};
                _ajax.query._rand = ood.rand();
                ood.merge(options, _ajax.options);
                ins.busy();
                var node = ins.getContainer();
                ood.Ajax(ood.adjustRes(_ajax.url, false, true), _ajax.query, function (rsp) {
                    node.html(rsp, true, true);
                    if (prf.$afterAutoLoad) prf.$afterAutoLoad.call(prf.boxing(), prf);
                    ins.free();
                }, function (err) {
                    node.html("<div>" + err + "</div>", true, false);
                    if (prf.$afterAutoLoad) prf.$afterAutoLoad.call(prf.boxing(), prf);
                    ins.free();
                }, null, options).start();
            }
        },
        _onresize: function (profile, width) {
            if (width) ood.UI._adjustConW(profile, profile.getRoot(), profile);
        }
    }
});

ood.Class("ood.UI.CSSBox", "ood.UI.Span", {
    Instance: {
        fireClickEvent: null,
        adjustDock: null,
        draggable: null,
        busy: null,
        free: null
    },
    Static: {
        $initRootHidden: true,
        _objectProp: {normalStatus: 1, hoverStatus: 1, activeStatus: 1, focusStatus: 1},
        Templates: {
            style: 'left:' + ood.Dom.HIDE_VALUE + ';top:' + ood.Dom.HIDE_VALUE + ';width:12.5em;height:5em;visibility:hidden;display:none;position:absolute;z-index:0;',
            className: '{_className}',
            text: '{_html}'
        },
        DataModel: {
            customCss: {
                ini: null,
                action: function () {
                    this.box._refreshCSS(this);
                }
            },
            className: {
                ini: null,
                action: function () {
                    this.box._refreshCSS(this);
                }
            },
            sandbox: {
                ini: "",
                action: function (v) {
                    this.box._refreshCSS(this);
                }
            },
            normalStatus: {
                ini: {},
                action: function (v) {
                    this.box._refreshCSS(this);
                }
            },
            hoverStatus: {
                ini: {},
                action: function (v) {
                    this.box._refreshCSS(this);
                }
            },
            activeStatus: {
                ini: {},
                action: function (v) {
                    this.box._refreshCSS(this);
                }
            },
            focusStatus: {
                ini: {},
                action: function (v) {
                    this.box._refreshCSS(this);
                }
            },
            spaceUnit: null,
            showEffects: null,
            hideEffects: null,
            position: null,
            display: null,
            visibility: null,
            zIndex: null,
            left: null,
            top: null,
            width: null,
            height: null,
            right: null,
            bottom: null,
            rotate: null,
            activeAnim: null,
            hoverPop: null,
            hoverPopType: null,
            dock: null,
            dockStretch: null,
            dockIgnoreFlexFill: null,
            renderer: null,
            display: null,
            html: null,
            selectable: null,
            overflow: null,
            tabindex: null,
            autoTips: null,
            disableClickEffect: null,
            disableHoverEffect: null,
            disableTips: null,
            disabled: null,
            defaultFocus: null,
            dockStretch: null,
            dockIgnore: null,
            dockOrder: null,
            dockMargin: null,
            dockFloat: null,
            dockMinW: null,
            dockMinH: null,
            dockMaxW: null,
            dockMaxH: null,
            tips: null
        },
        $adjustProp: function (profile, force) {
            var prop = profile.properties, cls = (!force && prop.className) || ('ood-css-' + profile.$xid),
                ko, i = 1, hash = {};
            profile.box.getAll().each(function (prf) {
                if (prf !== profile) hash[prf.properties.className] = 1;
            });
            while (hash[cls]) cls = cls + (i++);
            prop.className = cls;
        },
        RenderTrigger: function () {
            var prf = this;
            if (!prf.$inDesign) {
                ood('body').append(prf.getRoot());
            }
        },
        _prepareData: function (profile, data) {
            data = arguments.callee.upper.call(this, profile, data);
            var css = this._getCon(profile);
            data._html = "Text" + (css ? ("<" + "style id='" + profile.getDomId() + "cssnode' type='text/css'>" + css + "<" + "/style>") : "");
            return data;
        },
        _getCon: function (prf) {
            var css = "", prevId = "", t,
                prop = prf.properties,
                cls = prop.className,
                customCss = prop.customCss,
                hash1 = prop.normalStatus,
                hash2 = prop.hoverStatus,
                hash3 = prop.activeStatus,
                hash4 = prop.focusStatus;

            if (!customCss) {
                if (hash1 && !ood.isEmpty(hash1)) {
                    css += "." + cls + "{" + ood.Dom.$adjustCss(hash1, true) + "}\n";
                    if (hash1.color) css += "." + cls + " .ood-node{color:" + hash1.color + "}";
                }
            } else {
                css = customCss;
            }
            if (hash2 && !ood.isEmpty(hash2)) {
                css += "." + cls + ":hover, ." + cls + "-hover{" + ood.Dom.$adjustCss(hash2, true) + "}\n";
                if (hash2.color) css += "." + cls + " .ood-node{color:" + hash2.color + "}";
            }
            // cover :hover effect for -chekced / -active
            if (hash3 && !ood.isEmpty(hash3)) {
                css += "." + cls + ":active, ." + cls + ":checked, ." + cls + "-active, ." + cls + "-checked, ." + cls + "-checked:hover, ." + cls + "-active:hover{" + ood.Dom.$adjustCss(hash3, true) + "}";
                if (hash3.color) css += "." + cls + " .ood-node{color:" + hash3.color + "}";
            }
            if (hash4 && !ood.isEmpty(hash4)) {
                css += "." + cls + ":focus, ." + cls + "-focus{" + ood.Dom.$adjustCss(hash4, true) + "}";
                if (hash4.color) css += "." + cls + " .ood-node{color:" + hash4.color + "}";
            }

            if (t = prop.sandbox) {
                // alias or domId
                t = (prf.host && prf.host[t] && prf.host[t].get(0)) || (ood(t).get(0) && t);
                if (t) prevId = ood.UI._getThemePrevId(t);
            } else if (prf.$inDesign) {
                // the canvas
                if (t = prf.parent) prevId = ood.UI._getThemePrevId(t);
            }

            return ood.UI._adjustCSS(css, prevId);
        },
        _refreshCSS: function (prf) {
            var ns = this;
            ood.resetRun(prf.key + ":" + prf.$xid, function () {
                if (prf.destroyed) return;
                var id = prf.getDomId() + "cssnode",
                    prop = prf.properties,
                    root = prf.getRoot(),
                    css = ns._getCon(prf);

                root.query('style').remove(false);

                ood.Dom._setClass(root.get(0), prop.className);
                if (css) ood.CSS._appendSS(root.get(0), css, id, false);
            });
        },
        EventHandlers: {
            beforeInputAlert: null,
            onContextmenu: null,
            onClick: null,
            onDock: null,
            onLayout: null,
            onMove: null,
            onRender: null,
            onResize: null,
            onShowTips: null,
            beforeHoverEffect: null,
            beforeAppend: null,
            afterAppend: null,
            beforeRender: null,
            afterRender: null,
            beforeRemove: null,
            afterRemove: null,
            onHotKeydown: null,
            onHotKeypress: null,
            onHotKeyup: null
        }
    }
});

ood.Class("ood.UI.MoudluePlaceHolder", "ood.UI.Div", {
    Instance: {
        destroy: function (ignoreEffects, purgeNow) {
            var o = this.get(0);
            if (!o) return;
            (o.$afterDestroy = (o.$afterDestroy || {}))["destroyAttachedModule"] = function () {
                if (!this._replaced && this._module) {
                    this._module.destroy(ignoreEffects, purgeNow);
                }
            };
            return arguments.callee.upper.apply(this, [ignoreEffects, purgeNow]);
        },
        adjustDock: null,
        draggable: null,
        busy: null,
        free: null,
        // for Module
        setProperties: function (key, value) {
            var self = this.get(0);
            if (!self._properties) self._properties = {};
            if (!key) self._properties = {};
            else if (typeof key == 'string') self._properties[key] = value;
            else {
                ood.merge(self._properties, key, 'all');
                if (value && ood.isHash(value))
                    ood.merge(self._properties, value, 'all');
            }
            return this;
        },
        getValue: function () {
            return ood.get(this.get(0), ['_properties', 'value']);
        },
        setValue: function (value) {
            ood.set(this.get(0), ['_properties', 'value'], value);
            return this;
        },
        getUIValue: function () {
            return this.get(0)._$UIvalue;
        },
        setUIValue: function (value) {
            this.get(0)._$UIvalue = value;
            return this;
        },
        getProperties: function (key) {
            var self = this.get(0);
            if (!self._properties) self._properties = {};
            return key ? self._properties[key] : self._properties;
        },
        setEvents: function (key, value) {
            var self = this.get(0);
            if (!self._events) self._events = {};
            if (!key)
                self._events = {};
            else if (typeof key == 'string')
                self._events[key] = value;
            else
                ood.merge(self._events, key, 'all');
            return this;
        },
        getEvents: function (key) {
            var self = this.get(0);
            if (!self._events) self._events = {};
            return key ? this._events[key] : this._events;
        },
        replaceWithModule: function (module) {
            var self = this,
                prf = self.get(0),
                m, t, parent, subId,
                onEnd = function (t) {
                    if (prf.$afterReplaced) prf.$afterReplaced.call(module);
                    if ((t = prf._render_holder || prf._inline_holder) && t.$afterAttached)
                        module.getUIComponents().each(function (prf) {
                            ood.tryF(t.$afterAttached, [prf], t);
                        });
                    // Avoid being removed from host
                    prf.alias = null;
                    prf._module = null;
                    if (prf.box) prf.boxing().destroy();
                };

            if (!prf || prf.destroyed || prf._replaced || !prf.getRootNode()) return;
            prf._replaced = 1;

            if (prf.$beforeReplaced) prf.$beforeReplaced.call(module);
            // host and alias
            if (prf.host || prf.alias) module.setHost(prf.host, prf.alias);
            if ('_$UIvalue' in prf) module.$UIvalue = prf._$UIvalue;
            if (t = prf._events) module.setEvents(t);
            if (t = prf._properties) module.setProperties(t);
            // maybe in other module
            if (prf.moduleClass && prf.moduleXid) {
                if (m = ood.Module.getInstance(prf.moduleClass, prf.moduleXid)) {
                    m.AddComponents(module);
                }
            }
            if (parent = prf.parent) {
                subId = prf.childrenId;
                module.show(onEnd, parent, subId);
            } else if (prf.rendered && (parent = prf.getRoot().parent()) && !parent.isEmpty()) {
                module.show(onEnd, parent);
            }

            if (prf.$afterReplaced) prf.$afterReplaced.call(module);
            // Avoid being removed from host
            prf.alias = null;
            prf._module = null;
            self.destroy();
        }
    },
    Static: {
        Templates: {
            tagName: 'div',
            style: 'left:0;top:0;width:0;height:0;visibility:hidden;display:none;position:absolute;z-index:0;'
        },
        DataModel: {
            showEffects: null,
            hideEffects: null,
            activeAnim: null,
            hoverPop: null,
            hoverPopType: null,
            dock: null,
            dockStretch: null,
            renderer: null,
            html: null,
            disableClickEffect: null,
            disableHoverEffect: null,
            disableTips: null,
            disabled: null,
            defaultFocus: null,
            dockStretch: null,
            dockIgnore: null,
            dockOrder: null,
            dockMargin: null,
            dockFloat: null,
            dockMinW: null,
            dockMinH: null,
            dockMaxW: null,
            dockMaxH: null,
            tips: null
        },
        EventHandlers: {
            onContextmenu: null,
            onDock: null,
            onLayout: null,
            onMove: null,
            onRender: null,
            onResize: null,
            onShowTips: null,
            beforeAppend: null,
            afterAppend: null,
            beforeRender: null,
            afterRender: null,
            beforeRemove: null,
            afterRemove: null,
            onHotKeydown: null,
            onHotKeypress: null,
            onHotKeyup: null
        },
        // for parent UIProfile toHtml case
        RenderTrigger: function () {
            var prf = this;
            if (prf && !prf._replaced && prf._module) {
                prf.boxing().replaceWithModule(prf._module);
            }
        },

        // Refresh theme for all UI components
        refreshAllThemes: function (theme) {
            var self = this;
            // Use the provided theme or fallback to the global theme
            if (!theme || (theme !== 'light' && theme !== 'dark')) {
                theme = ood.getGlobalTheme ? ood.getGlobalTheme() : 'light';
            }

            try {
                // Traverse all UI components and refresh their themes
                ood.each(ood.UIProfile.___all, function (profile) {
                    if (profile && profile.boxing && profile.boxing().setTheme) {
                        try {
                            profile.boxing().setTheme(theme);
                        } catch (e) {
                            if (window.console && window.console.warn) {
                                window.console.warn('Failed to refresh theme for component:', e);
                            }
                        }
                    }
                });

                // 同步各组件的本地存储主题设置与全局主题
                // 这是为了确保组件在初始化时能使用全局主题
                localStorage.setItem('layout-theme', theme);
                localStorage.setItem('menubar-theme', theme);
                localStorage.setItem('dialog-theme', theme);
                localStorage.setItem('buttonlayout-theme', theme);
                localStorage.setItem('panel-theme', theme);
                localStorage.setItem('tabs-theme', theme);
                localStorage.setItem('statusbuttons-theme', theme);
                localStorage.setItem('infoblock-theme', theme);
                localStorage.setItem('formlayout-theme', theme);
                localStorage.setItem('popmenu-theme', theme);
                localStorage.setItem('datepicker-theme', theme);
                localStorage.setItem('toolbar-theme', theme);
                localStorage.setItem('treeview-theme', theme);
                localStorage.setItem('radiobox-theme', theme);
                localStorage.setItem('progressbar-theme', theme);
                localStorage.setItem('contentblock-theme', theme);
            } catch (e) {
                if (window.console && window.console.error) {
                    window.console.error('Error in refreshAllThemes:', e);
                }
            }
        }
    }
});

ood.Class("ood.AnimBinder", "ood.absObj", {
    Instance: {
        _ini: ood.Timer.prototype._ini,
        _after_ini: function (profile, ins, alias) {
            if (!profile.name) profile.Instace.setName(alias);
        },
        getParent: ood.Timer.prototype.getParent,
        getChildrenId: ood.Timer.prototype.getChildrenId,
        destroy: function () {
            this.each(function (profile) {
                var box = profile.box, name = profile.properties.name;
                //delete from pool
                delete box._pool[name];
                //free profile
                profile.__gc();
            });
        },
        setHost: function (value, alias) {
            var self = this;
            if (value && alias)
                self.setName(alias);
            return arguments.callee.upper.apply(self, arguments);
        },
        apply: function (node, onEnd) {
            var prf = this.get(0), fs = prf.properties['frames'], arr = [], frame, frame1, frame2, endpoints;
            for (var i = 0, l = fs.length; i < l - 1; i++) {
                endpoints = {};
                frame1 = fs[i];
                frame2 = fs[i + 1];
                frame = ood.copy(frame2);
                delete frame.status;
                for (var j in frame2.status) endpoints[j] = frame2.status[j];
                frame.endpoints = endpoints;
                frame['start'] = frame1;
                frame['end'] = frame2;
                arr.push(frame);
            }
            var fun = function () {
                if (!arr.length) {
                    if (prf.onEnd) prf.boxing().onEnd(prf);
                    if (ood.isFun(onEnd)) ood.tryF(onEnd);
                    return;
                }
                var frame = arr.shift();
                if (prf.beforeFrame && false === prf.boxing().beforeFrame(prf, frame)) return;

                return ood(node).animate(frame.endpoints, null, fun, frame.duration, frame.step, frame.type, null, null, frame.restore, frame.times).start();
            };
            return fun();
        }
    },
    Static: {
        _objectProp: {tagVar: 1, propBinder: 1, "frames": 1},
        _beforeSerialized: ood.Timer._beforeSerialized,
        $nameTag: "ani_",
        _pool: {},
        destroyAll: function () {
            this.pack(ood.toArr(this._pool, false), false).destroy();
            this._pool = {};
        },
        getFromName: function (name) {
            var o = this._pool[name];
            return o && o.boxing();
        },
        DataModel: {
            dataBinder: null,
            dataField: null,
            "name": {
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
            },
            "frames": {
                ini: []
            }
        },
        EventHandlers: {
            beforeFrame: function (profile, frame) {
            },
            onEnd: function (profile) {
            }
        }
    }
});