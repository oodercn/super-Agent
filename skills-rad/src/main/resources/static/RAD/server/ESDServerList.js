ood.Class('RAD.server.ESDServerList', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],
        Required: [],
        properties: {
            "path": "form/myspace/versionspace/projectManager/0/RAD/server/ESDServerList.cls",
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
                    .setHost(host, "clearHosts")
                    .setName("clearHosts")
                    .setQueryURL("/admin/proxyhost/clearServers")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_block29",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData([
                        {
                            "args": [
                                "{page.initData()}"
                            ],
                            "desc": "动作 1",
                            "method": "initData",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerList"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "api_12")
                    .setName("api_12")
                    .setAutoRun(true)
                    .setQueryURL("/admin/proxyhost/getServerList")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_pagebar7",
                            "path": "",
                            "type": "pagebar"
                        },
                        {
                            "name": "ood_ui_block29",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name": "ood_ui_treegrid14",
                            "path": "data",
                            "type": "treegrid"
                        },
                        {
                            "name": "ood_ui_pagebar7",
                            "path": "size",
                            "type": "pagebar"
                        }
                    ])
                    .setResponseCallback([])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "startAjax")
                    .setName("startAjax")
                    .setQueryURL("/admin/proxyhost/startLocalServer")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_treegrid14",
                            "path": "",
                            "type": "treegrid"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData([
                        {
                            "args": [
                                "{page.initData()}"
                            ],
                            "desc": "动作 1",
                            "method": "initData",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerList",
                            "type": "page"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "deleteAjax")
                    .setName("deleteAjax")
                    .setQueryURL("/admin/proxyhost/deleteLocalServer")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_treegrid14",
                            "path": "",
                            "type": "treegrid"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData([
                        {
                            "args": [
                                "{page.initData()}"
                            ],
                            "desc": "动作 1",
                            "method": "initData",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerList",
                            "type": "page"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "stopAjax")
                    .setName("stopAjax")
                    .setQueryURL("/admin/proxyhost/stopLocalServer")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_treegrid14",
                            "path": "",
                            "type": "treegrid"
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData([
                        {
                            "args": [
                                "{page.initData()}"
                            ],
                            "desc": "动作 1",
                            "method": "initData",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerList",
                            "type": "page"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog18")
                    .setLeft("5em")
                    .setTop("1.6666666666666667em")
                    .setWidth("59.166666666666664em")
                    .setHeight("37.5em")
                    .setCaption("本地服务器配置")
                    .setImageClass("ood-icon-dragmove")
            );

            host.ood_ui_dialog18.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block29")
                    .setName("mainPanel")
                    .setDock("fill")
                    .setLeft("23.333333333333332em")
                    .setTop("9em")
            );

            host.ood_ui_block29.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_div87")
                    .setName("pagediv")
                    .setDock("bottom")
                    .setLeft("19.375em")
                    .setTop("35.625em")
                    .setHeight("2.25em")
            );

            host.ood_ui_div87.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host, "ood_ui_pagebar7")
                    .setName("pagebar")
                    .setLeft("12.5em")
                    .setTop("0.625em")
                    .setCaption("翻页")
            );

            host.ood_ui_block29.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host, "ood_ui_toolbar26")
                    .setName("toolbar")
                    .setItems([
                        {
                            "caption": "common",
                            "hidden": false,
                            "id": "common",
                            "sub": [
                                {
                                    "caption": "添加",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "add",
                                    "imageClass": "ri-calendar-event-line",
                                    "position": "absolute"
                                },
                                {
                                    "caption": "刷新",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "reload",
                                    "imageClass": "ri-loader-line",
                                    "position": "absolute"
                                },
                                {
                                    "caption": "清空",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "clear",
                                    "imageClass": "ri-close-line",
                                    "position": "absolute"
                                }
                            ]
                        }
                    ])
                    .setLeft("Infinityem")
                    .setTop("2.0833333333333335em")
                    .onClick([
                        {
                            "args": [
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{page}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "add",
                                    "left": "{args[5]}"
                                }
                            ],
                            "desc": "动作 3",
                            "method": "show2",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerConfig",
                            "type": "page"
                        },
                        {
                            "args": [
                                "{page.clearHosts.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "clear",
                                    "left": "{args[1].id}"
                                }
                            ],
                            "desc": "清空",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "return": false,
                            "target": "clearHosts",
                            "type": "control"
                        },
                        {
                            "args": [
                                "{page.api_12.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "reload",
                                    "left": "{args[1].id}"
                                }
                            ],
                            "desc": "刷新",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "return": false,
                            "target": "api_12",
                            "type": "control"
                        }
                    ])
            );

            host.ood_ui_block29.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_ui_treegrid14")
                    .setName("grid")
                    .setLeft("0em")
                    .setTop("0em")
                    .setRowNumbered(true)
                    .setRowHandlerWidth("8em")
                    .setHeader([
                        {
                            "caption": "ID",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": true,
                            "id": "serverId",
                            "readonly": true,
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": "名称",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "name",
                            "readonly": true,
                            "type": "label",
                            "width": "12em"
                        },
                        {
                            "caption": "端口",
                            "colResizer": true,
                            "editable": false,
                            "editorListItems": [
                                {
                                    "caption": "匿名访问",
                                    "hidden": false,
                                    "id": "guest"
                                },
                                {
                                    "caption": "用户服务",
                                    "hidden": false,
                                    "id": "user"
                                },
                                {
                                    "caption": "系统服务",
                                    "hidden": false,
                                    "id": "admin"
                                }
                            ],
                            "flexSize": false,
                            "hidden": false,
                            "id": "proxyPort",
                            "readonly": true,
                            "type": "label",
                            "width": "4em"
                        },
                        {
                            "caption": "路径",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "path",
                            "readonly": true,
                            "type": "label",
                            "width": "16em"
                        },
                        {
                            "caption": "配置文件",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "configFile",
                            "readonly": true,
                            "type": "label",
                            "width": "12em"
                        },
                        {
                            "caption": "启动脚本",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "startScript",
                            "readonly": true,
                            "type": "label",
                            "width": "20em"
                        }
                    ])
                    .setUidColumn("serverId")
                    .setTagCmds([
                        {
                            "hidden": false,
                            "id": "delete",
                            "itemClass": "ri-close-line",
                            "location": "left",
                            "pos": "row",
                            "tips": "删除配置"
                        },
                        {
                            "hidden": false,
                            "id": "start",
                            "itemClass": "oodcon ood-icon-triangle-right",
                            "location": "left",
                            "pos": "row",
                            "tips": "启动服务"
                        },
                        {
                            "hidden": false,
                            "id": "stop",
                            "itemClass": "oodcon ood-uicmd-location",
                            "location": "left",
                            "pos": "row",
                            "tips": "关闭服务"
                        }
                    ])
                    .setValue("")
                    .onCmd([
                        {
                            "args": [
                                "{page.deleteAjax.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "delete",
                                    "left": "{args[2]}"
                                }
                            ],
                            "desc": "动作 1",
                            "method": "invoke",
                            "redirection": "other:callback:call",
                            "target": "deleteAjax",
                            "type": "control"
                        },
                        {
                            "args": [
                                "{page.startAjax.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "start",
                                    "left": "{args[2]}"
                                }
                            ],
                            "desc": "动作 2",
                            "method": "invoke",
                            "redirection": "other:callback:call",
                            "target": "startAjax",
                            "type": "control"
                        },
                        {
                            "args": [
                                "{page.stopAjax.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "stop",
                                    "left": "{args[2]}"
                                }
                            ],
                            "desc": "动作 3",
                            "method": "invoke",
                            "redirection": "other:callback:call",
                            "target": "stopAjax",
                            "type": "control"
                        }
                    ])
                    .onDblclickRow([
                        {
                            "args": [
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{args[1]}",
                                "{page}"
                            ],
                            "desc": "动作 1",
                            "method": "show2",
                            "redirection": "page",
                            "target": "RAD.server.ESDServerConfig",
                            "type": "page"
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
    Static: {
        "designViewConf": {
            "height": 1024,
            "mobileFrame": false,
            "width": 1280
        }
    }


});
