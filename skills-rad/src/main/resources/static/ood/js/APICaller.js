ood.Class("ood.APICaller", "ood.absObj", {
    Instance: {
        confirm: false,
        _ini: ood.Timer.prototype._ini,
        _after_ini: function (profile, ins, alias) {
            if (!profile.name) profile.Instace.setName(alias);
        },
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

        setQueryData: function (data, path) {
            return this.each(function (prf) {
                if (path) ood.set(prf.properties.queryArgs, (path || "").split("."), data);
                else prf.properties.queryArgs = data || {};
            });
        },

        start: function (data, onSuccess, onFail, onStart, onEnd, mode, threadid, options) {
            this.setQueryData(data);
            this.invoke(onSuccess, onFail, onStart, onEnd, mode, threadid, options);
        },

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
;