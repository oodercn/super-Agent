ood.Class('RAD.api.URLConfig', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],
        Required: [
            "ood.Module.JSONEditor"
        ],
        properties: {
            apis: {},
            "events": {
                "onReady": {
                    "actions": [
                        {
                            "args": [],
                            "desc": "函数",
                            "script": "_onready",
                            "type": "page"
                        }
                    ]
                }
            },
            "path": "form/myspace/versionspace/projectManager/0/RAD/api/URLConfig.cls",
            "personId": "devdev",
            "personName": "devdev",
            "projectName": "projectManager"
        },
        events: {
            "onReady": {
                "actions": [
                    {
                        "args": [],
                        "desc": "函数",
                        "script": "_onready",
                        "type": "page"
                    }
                ]
            }
        },
        functions: {},
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_dialog2")
                    .setDock('fill')
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane23")
                    .setDock("top")
                    .setWidth("8.334em")
                    .setHeight("2.8334em")
            );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "isAllform")
                    .setDirtyMark(false)
                    .setTop("0.91667em")
                    .setRight("14em")
                    .setCaption("级联")
            );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "ood_asyn")
                    .setDirtyMark(false)
                    .setTop("0.91667em")
                    .setRight("0.5em")
                    .setCaption("$(RAD.api_dlg.Asynchronous)")
                    .setValue(true)
            );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "autoRun")
                    .setDirtyMark(false)
                    .setTop("0.91667em")
                    .setRight("7.5em")
                    .setCaption("自动运行")
            );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_url")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "top": 0,
                        "left": 0,
                        "bottom": 0,
                        "right": 220
                    })
                    .setDockMinW("33.334em")
                    .setLeft("5em")
                    .setTop("0.75em")
                    .setWidth("16em")
                    .setHeight("1.75em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("远程服务（RUL）:")
                    .setType("helpinput")
                    .setImageClass("ood-icon-filter")
                    .setCommandBtn("delete")
                    .onChange([
                        "_ctl_ood_url_onchange"
                    ])
                    .onHotKeypress([
                        "_ctl_ood_url_onhotkey"
                    ])
                    .onCommand([
                        "_ctl_ood_url_oncommand"
                    ])
                    .beforePopShow([
                        "_ctl_ood_url_beforshow"
                    ])
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_paneleft")
                    .setDock("left")
                    .setTop("5.8334em")
                    .setWidth("19.1667em")
                    .setHeight("32.5em")
            );

            host.ood_ui_paneleft.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane272")
                    .setDock("top")
                    .setDockIgnoreFlexFill(true)
                    .setLeft("0em")
                    .setWidth("20em")
                    .setPosition("true")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_reqtype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("2.5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Request Type)")
                    .setType("listbox")
                    .setItems([
                        {
                            "caption": "FORM",
                            "hidden": false,
                            "id": "FORM"
                        },
                        {
                            "caption": "JSON",
                            "hidden": false,
                            "id": "JSON"
                        },
                        {
                            "caption": "XML",
                            "hidden": false,
                            "id": "XML"
                        },
                        {
                            "caption": "SOAP",
                            "hidden": false,
                            "id": "SOAP"
                        }
                    ])
                    .setValue("FORM")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_rsptype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Response Type)")
                    .setType("listbox")
                    .setItems([
                        {
                            "caption": "JSON",
                            "hidden": false,
                            "id": "JSON"
                        },
                        {
                            "caption": "TEXT",
                            "hidden": false,
                            "id": "TEXT"
                        },
                        {
                            "caption": "XML",
                            "hidden": false,
                            "id": "XML"
                        },
                        {
                            "caption": "SOAP",
                            "hidden": false,
                            "id": "SOAP"
                        }
                    ])
                    .setValue("JSON")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_proxytype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Proxy Type)")
                    .setType("listbox")
                    .setItems([
                        {
                            "caption": "auto",
                            "hidden": false,
                            "id": "auto"
                        },
                        {
                            "caption": "AJAX",
                            "hidden": false,
                            "id": "AJAX"
                        },
                        {
                            "caption": "JSONP",
                            "hidden": false,
                            "id": "JSONP"
                        },
                        {
                            "caption": "XDMI",
                            "hidden": false,
                            "id": "XDMI"
                        }
                    ])
                    .setValue("auto")
                    .afterValueSet([
                        "_ood_ui_comboinput207_aftervalueset"
                    ])
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_method")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("7.5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Query Method)")
                    .setType("listbox")
                    .setItems([
                        {
                            "caption": "auto",
                            "hidden": false,
                            "id": "auto"
                        },
                        {
                            "caption": "GET",
                            "hidden": false,
                            "id": "GET"
                        },
                        {
                            "caption": "POST",
                            "hidden": false,
                            "id": "POST"
                        },
                        {
                            "caption": "PUT",
                            "hidden": false,
                            "id": "PUT"
                        },
                        {
                            "caption": "DELETE",
                            "hidden": false,
                            "id": "DELETE"
                        }
                    ])
                    .setValue("auto")
            );

            host.ood_ui_paneleft.append(
                ood.create("ood.UI.Stacks")
                    .setHost(host, "ood_ui_foldingtabs1")
                    .setItems([
                        {
                            "caption": "$(RAD.api_dlg.Authorization)",
                            "hidden": false,
                            "id": "a"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Request Header)",
                            "hidden": false,
                            "id": "b"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Cookies Smulation)",
                            "hidden": false,
                            "id": "c"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Other Query Options)",
                            "hidden": false,
                            "id": "d"
                        }
                    ])
                    .setDockMargin({
                        "top": 120,
                        "left": 0,
                        "bottom": 0,
                        "right": 0
                    })
                    .setWidth("18.91667em")
                    .setHeight("25.5em")
                    .setValue("a")
            );

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_username")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("3.334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.User Name)")
                    .setType("input"),
                "a"
            );

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_token")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0.8334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.OAuth2Token)")
                    .setType("input"),
                "a"
            );

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_password")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("5.8334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Password)")
                    .setType("input"),
                "a"
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane22")
                    .setDock("fill")
                    .setDockMargin({
                        "top": 0,
                        "left": 6,
                        "bottom": 0,
                        "right": 0
                    })
                    .setLeft("25.833333333333332em")
                    .setTop("9.1667em")
                    .setWidth("12.1667em")
                    .setHeight("12.91667em")
            );

            host.ood_ui_pane22.append(
                ood.create("ood.UI.HTMLButton")
                    .setHost(host, "ood_ui_htmlbutton3")
                    .setDock("bottom")
                    .setDockOrder(false)
                    .setDockMargin({
                        "top": 2,
                        "left": 6,
                        "bottom": 2,
                        "right": 6
                    })
                    .setWidth("11em")
                    .setHeight("1.8334em")
                    .setCaption("$(RAD.api_dlg.Test the Web API)")
                    .onClick([
                        "_ood_ui_htmlbutton3_onclick"
                    ])
            );

            host.ood_ui_pane22.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "ood_ui_layout7")
                    .setItems([
                        {
                            "cmd": false,
                            "folded": false,
                            "id": "before",
                            "locked": false,
                            "min": 10,
                            "pos": "before",
                            "size": 100,
                            "hidden": false
                        },
                        {
                            "id": "main",
                            "min": 10,
                            "size": 120
                        }
                    ])
                    .setType("vertical")
                    .setLeft("0em")
                    .setTop("0em")
                    .setFlexSize("true")
                    .setTransparent(true)
            );

            host.ood_ui_layout7.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group10")
                    .setDock("fill")
                    .setLeft("6.667em")
                    .setWidth("8.334em")
                    .setHeight("19.1667em")
                    .setCaption("$(RAD.api_dlg.Response Data Setting)"),
                "main"
            );

            host.ood_ui_group10.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel17")
                    .setLeft("20.41667em")
                    .setTop("1.667em")
                    .setWidth("24.5em")
                    .setHeight("17.08333em")
                    .setCaption("$(RAD.api_dlg.Response Callback)")
            );

            host.ood_ui_panel17.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_rspfun")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("2em")
                    .setHeader([
                        {
                            "caption": "$(RAD.api_dlg.Callback Type)",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "type",
                            "readonly": true,
                            "type": "label",
                            "width": "6em"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Callback)",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "source",
                            "readonly": true,
                            "type": "label",
                            "width": "12em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onRowSelected([
                        "_ontrows"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
            );

            host.ood_ui_group10.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel45")
                    .setDock("left")
                    .setTop("1.667em")
                    .setWidth("26em")
                    .setHeight("17.08333em")
                    .setCaption("$(RAD.api_dlg.Response to Target)")
                    .setBorderType("flat")
            );

            host.ood_ui_panel45.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_rsptarget")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("4em")
                    .setHeader([
                        {
                            "caption": "$(RAD.api_dlg.Data Path)",
                            "colResizer": true,
                            "editable": true,
                            "flexSize": true,
                            "hidden": false,
                            "id": "path",
                            "readonly": true,
                            "type": "input",
                            "width": "6em"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Target)",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "source",
                            "readonly": true,
                            "type": "label",
                            "width": "10em"
                        },
                        {
                            "caption": "参数用途",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "desc",
                            "readonly": true,
                            "type": "label",
                            "width": "10em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onRowSelected([
                        "_ontrows2"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
                    .beforeCellUpdated([
                        "_acu"
                    ])
            );

            host.ood_ui_layout7.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group7")
                    .setDock("fill")
                    .setDockOrder(false)
                    .setLeft("5.8334em")
                    .setWidth("8.334em")
                    .setHeight("15em")
                    .setCaption("$(RAD.api_dlg.Request Data Setting)"),
                "before"
            );

            host.ood_ui_group7.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane15")
                    .setDock("left")
                    .setTop("0.8334em")
                    .setWidth("26em")
                    .setHeight("8.334em")
            );

            host.ood_ui_pane15.append(
                ood.create("ood.UI.Stacks")
                    .setHost(host, "ood_ui_foldingtabs11")
                    .setItems([
                        {
                            "caption": "$(RAD.api_dlg.Request Datasource)",
                            "height": "180",
                            "hidden": false,
                            "id": "b"
                        },
                        {
                            "caption": "$(RAD.api_dlg.Request Parameters)",
                            "height": "170",
                            "hidden": false,
                            "id": "c",
                            "imageClass": ""
                        }
                    ])
                    .setLeft("9.58334em")
                    .setTop("0em")
                    .setWidth("8.41667em")
                    .setHeight("21em")
                    .setValue("b")
            );

            host.ood_ui_foldingtabs11.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_reqds")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("4em")
                    .setHeader([
                        {
                            "caption": "$(RAD.api_dlg.Datasource)",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "source",
                            "readonly": true,
                            "type": "label",
                            "width": "6.666666666666667em"
                        },
                        {
                            "caption": "$(RAD.api_dlg.To Data Path)",
                            "colResizer": true,
                            "editable": true,
                            "flexSize": true,
                            "hidden": false,
                            "id": "path",
                            "readonly": true,
                            "type": "input",
                            "width": "6.666666666666667em"
                        },
                        {
                            "caption": "参数用途",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "desc",
                            "readonly": true,
                            "type": "input",
                            "width": "6.666666666666667em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onChange([
                        "_ood_reqds_onchange"
                    ])
                    .onRowSelected([
                        "_ontrows"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
                    .beforeCellUpdated([
                        "_acu"
                    ])
                    .afterCellUpdated([
                        "_ood_reqds_bcellupdated"
                    ]),
                "b"
            );

            host.ood_ui_foldingtabs11.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "m_reqparams")
                    .setEvents({
                        "onchange": [
                            "_m_reqparams_onchange"
                        ]
                    }),
                "c"
            );

            host.ood_ui_group7.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel44")
                    .setLeft("19.58334em")
                    .setTop("0.8334em")
                    .setWidth("24.5em")
                    .setHeight("17.083333333333332em")
                    .setCaption("$(RAD.api_dlg.Request Data)")
                    .setBorderType("none")
            );

            host.ood_ui_panel44.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "ood_reqtext")
                    .setDisabled(true)
                    .setDock("fill")
                    .setLeft("6.667em")
                    .setTop("4.1667em")
                    .setHeight("10em")
                    .setMultiLines(true)
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend: function (parent, subId, left, top) {
            return false;
        },
        _ctl_ood_url_beforshow: function (profile) {
            var ns = this,
                newValue = ns.ood_url.getValue();
            if (newValue && newValue == '') {
                this._reSetValue(profile);
            }
        },
        _ctl_ood_url_oncommand: function (profile, src) {
            profile.boxing().setValue("", true);
            this._reSetValue(profile, "");
        },
        _ontrows2: function (profile, row, e, src, type) {
            //if (type == -1)
            //    profile.boxing().updateCell(row.cells[0], {value: ""}, false, false);
            this.updateRequestData();
        },
        _reSetValue: function (profile, newValue) {
            var ns = this,
                prop = ns.properties;
            //    newValue=ns.ood_url.getValue();
            var ns = this, ins = profile.boxing(), t = SPA.curProjectConfig.$ConstantData;
            var items = [];
            ns._filter = ".*" + newValue + ".*";
            ood.Dom.busy();
            var apipath = '';
            if (SPA.curProjectConfig.$ConstantData && SPA.curProjectConfig.$ConstantData.apipath) {
                apipath = SPA.curProjectConfig.$ConstantData.apipath;
            }
            ood.Ajax(CONF.getAPIService, {"path": apipath, pattern: ns._filter},
                function (txt) {
                    var obj = txt.data;
                    if (obj && !obj.error) {
                        ood.arr.each(obj, function (o) {
                            items.push({
                                id: o.id,
                                caption: o.caption,
                                disabled: o.disabled,
                                properties: o.properties,
                                getProperties: function () {
                                    return this.properties;
                                }

                            });
                        });
                    }

                    if (obj.length == 0) {
                        items.push({
                            id: newValue,
                            caption: '没有发现匹配的API!',
                        });
                    }

                    try {
                        ins.expand();
                    } catch (e) {

                    }
                    ins.setItems(items);

                    ood.Dom.free();
                }, function (obj) {
                    ins.setItems(items);
                    ood.Dom.free();
                }, null, {method: 'post'}).start();

        },
        _ctl_ood_url_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            var item = profile.boxing().getItemByItemId(newValue);
            if (item) {
                this.setDataToEditor(item, ns._host)
            } else {
                this._reSetValue(profile, newValue);
            }

        },
        _m_reqparams_onchange: function (module) {
            this.updateRequestData();
        },
        updateRequestData: function (queryArgs, requestDataSource) {
            var ns = this;

            queryArgs = queryArgs ? ood.clone(queryArgs) : ns.m_reqparams.getValue(true);
            requestDataSource = requestDataSource || [];
            if (!requestDataSource.length) {
                ood.arr.each(ns.ood_reqds.getRows(), function (o) {
                    if (o._selected) {
                        requestDataSource.push({
                            type: o.bindertype,
                            name: o.cells[0].value,
                            path: o.cells[1].value
                        });
                    }
                });
            }

            // merge request data
            if (requestDataSource && requestDataSource.length) {
                for (var i in requestDataSource) {
                    var o = requestDataSource[i], t, v, path;
                    switch (o.type) {
                        case "databinder":
                            if ((t = ns._host[o.name]) && t.Class['ood.DataBinder']) {
                                if (!t.updateDataFromUI()) {
                                    return;
                                } else {
                                    path = (o.path || "").split('.');
                                    if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, t.getData());
                                    else ood.set(queryArgs, o.path.split('.'), t.getData());
                                }
                            }
                            break;
                        case "pagebar":
                            if (t = ns._host[o.name]) {
                                var pageparams = {pageSize: t.getPageCount(), pageIndex: t.getPage()};
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, pageparams);
                                else ood.set(queryArgs, path, pageparams);
                            }
                            break;

                        case "treegrid":
                            if (t = ns._host[o.name]) {
                                path = (o.path || t.getUidColumn()).split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                            }
                            break;
                        case "treeview":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                            }
                            break;
                        case "form":
                            if ((t = ns._host[o.name]) && t.Class['ood.absContainer'] && t.getFormElements().size() && t.getRootNode()) {
                                // if(!t.checkValid() || !t.checkRequired()){
                                //     return;
                                // }else{
                                path = (o.path || "").split('.');
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, t.getFormValues());
                                else ood.set(queryArgs, path, t.getFormValues());
                                //}
                            }
                            break;
                        case "module":
                            if ((t = ns._host[o.name]) && t.Class['ood.Module']) {
                                path = (o.path || o.name).split('.');
                                ood.set(queryArgs, path, t.getValue(true));

                            }
                            break;
                    }
                }
            }

            var code = ood.stringify(queryArgs);
            if (ood.Coder) {
                code = ood.Coder.formatText(code);
            }

            ns.ood_reqtext.setValue(code);
        },
        _onready: function () {
            if (CONF.getClientMode() != 'project') this.ood_url.setType('none');
        },
        _ood_reqds_onchange: function () {
            this.updateRequestData();
        },
        _ood_ui_comboinput207_aftervalueset: function (profile, oldValue, newValue, force, tag) {
            var ns = this,
                req = ns.ood_reqtype.getValue(),
                m = ns.ood_method.getValue(),

                reqItems = "FORM,JSON,XML,SOAP".split(","),
                mItems = "auto,GET,POST,PUT,DELETE".split(",");

            switch (newValue) {
                case "JSONP":
                    reqItems.pop();
                    reqItems.pop();
                    mItems.pop();
                    mItems.pop();
                    mItems.pop();

                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
                    if (req == 'XML' || req == 'SOAP') {
                        ns.ood_reqds.setValue('FORM');
                    }
                    if (m == 'POST' || req == 'PUB' || req == 'DELETE') {
                        ns.ood_method.setValue('auto');
                    }
                    break;
                case "XDMI":
                    reqItems.pop();
                    reqItems.pop();
                    mItems.pop();
                    mItems.pop();

                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
                    if (req == 'XML' || req == 'SOAP') {
                        ns.ood_reqds.setValue('FORM');
                    }
                    if (req == 'PUB' || req == 'DELETE') {
                        ns.ood_method.setValue('auto');
                    }
                    break;
                default:
                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
            }
        },
        setDataToEditor: function (api, host, cls, designer) {
            var apis = this.properties.apis;
            if (api && api.alias) {
                if (apis[api.alias]) {
                    api = apis[api.alias];
                }
            }

            var ns = this,
                prop = api.iniProp || api.getProperties(),
                arr, v, values, t, m;
            if (!prop)return;
            if (ns.oldapi && ns.oldapi.alias) {
                var apicompont = designer.getByAlias(ns.oldapi.alias);
                if (apicompont) {
                    apicompont.setProperties(ood.clone(ns.getDataFromEditor()));
                    apis[ns.oldapi.alias] = apicompont.n0;
                }

            }


            ns.oldapi = api;
            ns._oldprop = prop;
            ns._host = host;
            if (ns.ood_url){
                ns.ood_url.setValue(prop.queryURL);
            }
            if (!prop.queryAsync && (prop.queryAsync == false || prop.queryAsync == 'false')) {
                ns.ood_asyn.setValue(false);
            } else {
                ns.ood_asyn.setValue(true);
            }

            ns.autoRun.setValue(prop.autoRun);
            ns.isAllform.setValue(prop.isAllform);
            ns.ood_method.setValue(prop.queryMethod);
            ns.ood_username.setValue(prop.queryUserName);
            ns.ood_password.setValue(prop.queryPassword);
            ns.ood_reqtype.setValue(prop.requestType);
            ns.ood_rsptype.setValue(prop.responseType);
            ns.ood_proxytype.setValue(prop.proxyType);
            ns.ood_token.setValue(prop.oAuth2Token);


            // requestDataSource
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.requestDataSource, function (o) {
                    v[o.type + "-" + o.name] = o.path;
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.DataBinder'])
                        return;
                    t = db.getName();
                    m = "databinder" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "databinder",
                        cells: [t, v[m] || "", "自定义"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;

                    t = db.getAlias();

                    m = "pagebar" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "pagebar",
                        cells: [t, v[m] || "", "分页参数"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();

                    m = "treeview" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treeview",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();

                    m = "gallery" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "gallery",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();

                    m = "module" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "module",
                        cells: [t, v[m] || t, "子模块"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();

                    m = "treegrid" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treegrid",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });


                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "form" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "form",
                        cells: [t, v[m] || "", "表单数据"]
                    });
                });

                ns.ood_reqds.setRows(arr).setValue(values.join(";"));
            }());

            // responseDataTarget
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.responseDataTarget, function (o) {
                    v[o.type + "-" + (o.name || "")] = o.path;
                });


                if (m in v) values.push(m);

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.DataBinder'])
                        return;
                    t = db.getName();
                    m = "databinder" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "databinder",
                        cells: [v[m] || "", t, "数据绑定"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();
                    m = "treegrid" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treegrid",
                        cells: [v[m] || "data", t, "填充行数据"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Dialog'])
                        return;
                    t = db.getAlias();
                    m = "component" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "component",
                        cells: [v[m] || "data", t, "动态装载"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Block'])
                        return;
                    t = db.getAlias();
                    m = "component" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "component",
                        cells: [v[m] || "data", t, "动态装载"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();
                    m = "treeview" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treeview",
                        cells: [v[m] || "data", t, "装载树节点"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();
                    m = "gallery" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "gallery",
                        cells: [v[m] || "data", t, "装载数据"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.SVGPaper'])
                        return;
                    t = db.getAlias();
                    m = "svgpagper" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "svgpaper",
                        cells: [v[m] || "data", t, "填充图形"]
                    });
                });
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;
                    t = db.getAlias();
                    m = "pagebar" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "pagebar",
                        cells: [v[m] || "size", t, "设置最大记录数"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();
                    m = "module" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "module",
                        cells: [v[m] || 'data.' + t, t, "填充子组件数据"]
                    });
                });


                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "form" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "form",
                        cells: [v[m] || "data", t, "填充表单数据"]
                    });
                });
                m = "log-console.log";
                arr.push({
                    id: m,
                    bindertype: "log",
                    cells: [v[m] || "", "console.log"]
                });
                if (m in v) values.push(m);

                m = "alert-alert";
                arr.push({
                    id: m,
                    bindertype: "alert",
                    cells: [v[m] || "", "alert"]
                });
                ns.ood_rsptarget.setRows(arr).setValue(values.join(";"));
            }());

            // responseCallback
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.responseCallback, function (o) {
                    v[o.type + "-" + (o.name || "")] = o.type;
                });
                if (cls && cls.Instance && cls.Instance.functions) {
                    ood.each(cls.Instance.functions, function (conf, id) {
                        t = id;
                        m = "host" + "-" + t;
                        if (m in v) values.push(m);
                        arr.push({
                            id: m,
                            bindertype: "host",
                            cells: ["page", t]
                        });
                    });
                }
                ood.each(SPA.curProjectConfig.$GlobalFunctions, function (conf, id) {
                    t = id;
                    m = "global" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "global",
                        cells: ["global", t]
                    });
                });

                ns.ood_rspfun.setRows(arr).setValue(values.join(";"));
            }());

            // queryArgs
            if (ns.m_reqparams) {
                ns.m_reqparams.setProperties("value", prop.queryArgs);
            }

            // fake cookies
            if (ns.m_cookies) {
                ns.m_cookies.setProperties("value", prop.fakeCookies);

            }

            // header
            if (prop.queryHeader && !ood.isEmpty(prop.queryHeader)) {
                ns.m_header.setProperties("value", prop.queryHeader);
            }

            // queryOptions
            if (ns.m_cookies) {
                ns.m_options.setProperties("value", prop.queryOptions);
            }


            // here, must give prop.queryArgs
            ns.updateRequestData(prop.queryArgs, prop.requestDataSource);
        },
        _ctl_ood_url_onhotkey: function (profile, keyboard, e, src) {
            if (keyboard[0] == '/') {
                this._reSetValue(profile);
            }
        },
        _ood_ui_dialog2_beforeclose: function (profile) {
            var ns = this, prop = ns.getDataFromEditor();
            ns.fireEvent("onchange", [prop]);
            delete ns._oldprop;
        },
        getDataFromEditor: function () {
            var ns = this,
                oldprop = ns._oldprop,
                hash = {
                    requestDataSource: oldprop.requestDataSource,
                    responseDataTarget: oldprop.responseDataTarget,
                    responseCallback: oldprop.responseCallback
                },
                arr, v;

            hash.queryURL = ns.ood_url.getValue();
            hash.queryAsync = ns.ood_asyn.getValue();
            hash.autoRun = ns.autoRun.getValue();
            hash.queryMethod = ns.ood_method.getValue();
            hash.queryUserName = ns.ood_username.getValue();
            hash.queryPassword = ns.ood_password.getValue();
            hash.isAllform = ns.isAllform.getValue();
            hash.proxyType = ns.ood_proxytype.getValue();
            hash.requestType = ns.ood_reqtype.getValue();
            hash.responseType = ns.ood_rsptype.getValue();
            hash.oAuth2Token = ns.ood_token.getValue();


            // requestDataSource
            if (ns.ood_reqds.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_reqds.getRows(), function (o) {
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[0].value,
                            path: o.cells[1].value
                        });
                    }
                });
                hash.requestDataSource = arr;
            }
            // responseDataTarget
            if (ns.ood_rsptarget.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_rsptarget.getRows(), function (o) {
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[1].value,
                            path: o.bindertype == 'log' || o.bindertype == 'alert' ? "" : o.cells[0].value
                        });
                    }
                });
                hash.responseDataTarget = arr;
            }
            // responseCallback
            if (ns.ood_rspfun.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_rspfun.getRows(), function (o) {
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[1].value
                        });
                    }
                });
                hash.responseCallback = arr;
            }

            // queryArgs
            // m_reqparams
            v = ns.m_reqparams.getValue(true);
            hash.queryArgs = v || {};


            if (ns.m_cookies) {
                v = ns.m_cookies.getValue(true);
                hash.fakeCookies = v || {};
            }            // fake cookies


            // header
            if (ns.m_header) {
                v = ns.m_header.getValue(true);
                hash.queryHeader = v || {};
            }

            // queryOptions
            if (ns.m_options) {
                v = ns.m_options.getValue(true);
            }
            if (v && v.header && ood.isHash(v.header)) {
                ood.merge(hash.queryHeader, v.header);
                delete v.header;
            }

            hash.queryOptions = v || {};

            return hash;
        },
        _acu: function (profile, cell, options) {
            if (!cell._row._selected && options.value) {
                var ins = profile.boxing();
                var arr = ins.getValue(true);
                arr.push(cell._row.id);
                ood.arr.removeDuplicate(arr);
                ood.arr.removeValue(arr, "");
                ins.setValue((arr && arr.length) ? arr.join(";") : null);
            }
            this.updateRequestData();
        },
        _ood_ui_htmlbutton3_onclick: function () {
            var ns = this,
                prop = ns.getDataFromEditor();

            var url = prop.queryURL || "";
            if (/\s*\{ood.constant\.[\w]+\}\s*/.test(url)) {
                url = ood.adjustVar(url, {ood: {constant: SPA.curProjectConfig.$ConstantData}});
            }
            prop.queryURL = url;

            if (!prop.queryURL) {
                ood.alert(ood.adjustRes("$inline.required"), ood.adjustRes(ns.ood_url.getLabelCaption()), function () {
                    ns.ood_url.activate();
                });
                return;
            }

            delete prop.responseDataTarget;
            delete prop.responseCallback;

            var api = new ood.APICaller(),
                callback = function (state, rsp) {
                    console.log(state, rsp);
                    if (ood.isObj(rsp)) {
                        rsp = ood.stringify(rsp);
                        if (ood.Coder) {
                            rsp = "Response Object:\n\r\n\r" + ood.Coder.formatText(rsp);
                        }
                    }
                    api.destroy();
                    window.alert(rsp);
                };
            api.setHost(ns._host);
            api.setProperties(prop);

            api.invoke(function (rsp) {
                callback('Response Sucessful', rsp);
            }, function (rsp) {
                callback('Response Error', rsp);
            });
        },
        _ood_reqds_bcellupdated: function () {
            this.updateRequestData();
        },
        _ontrows: function (profile, row, e, src, type) {
            //if (type == -1)
            //   profile.boxing().updateCell(row.cells[1], {value: ""}, false, false);
            this.updateRequestData();
        },
        _tg_beforerowactive: function () {
            return false;
        }
    },
    Static: {
        "designViewConf": {
            "height": 1024,
            "mobileFrame": false,
            "width": 1280
        }
    }


});
