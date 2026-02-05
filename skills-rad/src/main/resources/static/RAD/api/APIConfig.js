ood.Class('RAD.api.APIConfig', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],
        Required: [
            "App.service.APITree"
        ],
        properties: {
            "autoDestroy": true,
            "path": "form/myspace/versionspace/projectManager/0/App/api/APIConfig.cls",
            "personId": "devdev",
            "personName": "系统管理员",
            "projectName": "projectManager"
        },
        events: {},
        functions: {},
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "reload")
                    .setName("reload")
                    .setAutoRun(true)
                    .setQueryURL("/admin/getAPIConfig")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_pagebar3",
                            "path": "",
                            "type": "pagebar"
                        },
                        {
                            "name": "ood_ui_panel12",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name": "ood_ui_treegrid10",
                            "path": "data",
                            "type": "treegrid"
                        },
                        {
                            "name": "ood_ui_pagebar3",
                            "path": "size",
                            "type": "pagebar"
                        }
                    ])
                    .setResponseCallback([])
                    .setQueryArgs({
                        "pageIndex": "",
                        "pageSize": "",
                        "currClassName": ""
                    })
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "delete")
                    .setName("delete")
                    .setQueryURL("/admin/deleteAPIConfig")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_treegrid10",
                            "path": "",
                            "type": "treegrid"
                        },
                        {
                            "name": "ood_ui_panel12",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .setQueryArgs({
                        "pageIndex": "",
                        "pageSize": "",
                        "currClassName": ""
                    })
                    .onData([
                        {
                            "args": [],
                            "desc": "动作 1",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "target": "reload",
                            "type": "control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "PAGECTX")
                    .setVisibility("hidden")
            );

            append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel12")
                    .setLeft("15.833333333333334em")
                    .setTop("9.166666666666666em")
                    .setCaption(ood.getRes('RAD.api.webAPIManagement') || "WEB-API管理")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_div43")
                    .setDock("bottom")
                    .setLeft("17.5em")
                    .setTop("35em")
                    .setHeight("3em")
            );

            host.ood_ui_div43.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host, "ood_ui_pagebar3")
                    .setLeft("9.5em")
                    .setTop("0.75em")
                    .setCaption(ood.getRes('RAD.api.pagination') || "翻页")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_ui_treegrid10")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setAltRowsBg(true)
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption": "id",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": true,
                            "id": "id",
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.name') || "名称",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "caption",
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.path') || "路径",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "path",
                            "type": "label",
                            "width": "15em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.serviceClass') || "服务类",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "classpath",
                            "type": "label",
                            "width": "5em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.description') || "描述",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "desc",
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.serverAddress') || "服务器地址",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "serverurl",
                            "type": "label",
                            "width": "10em"
                        }
                    ])
                    .setUidColumn("id")
                    .setValue("")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host, "ood_ui_toolbar12")
                    .setItems([
                        {
                            "caption": "common",
                            "hidden": false,
                            "id": "common",
                            "sub": [
                                {
                                    "caption": ood.getRes('RAD.api.add') || "添加",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "add",
                                    "imageClass": "cb-icon-calendar-plus",
                                    "position": "absolute"
                                },
                                {
                                    "caption": ood.getRes('RAD.api.remove') || "移除",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "delete",
                                    "imageClass": "cb-icon-close",
                                    "position": "absolute"
                                },
                                {
                                    "caption": ood.getRes('RAD.api.refresh') || "刷新",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "reload",
                                    "imageClass": "cb-icon-refresh",
                                    "position": "absolute"
                                }
                            ]
                        }
                    ])
                    .setTop("40.833333333333336em")
                    .onClick([
                        {
                            "args": [
                                "{page.reload.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "reload",
                                    "left": "{args[5]}"
                                }
                            ],
                            "desc": "动作 1",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "target": "reload",
                            "type": "control"
                        },
                        {
                            "args": [
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{page.getData()}",
                                "{page}"
                            ],
                            "className": "RAD.api.APITree",
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "add",
                                    "left": "{args[5]}"
                                }
                            ],
                            "desc": "动作 2",
                            "method": "show2",
                            "redirection": "page",
                            "target": "RAD.api.APITree",
                            "type": "page"
                        },
                        {
                            "args": [
                                "{page.delete.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "delete",
                                    "left": "{args[5]}"
                                }
                            ],
                            "desc": "动作 3",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "target": "delete",
                            "type": "control"
                        }
                    ])
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend: function (parent, subId, left, top) {
            return false;
        }
    },
    Static: {}


});
