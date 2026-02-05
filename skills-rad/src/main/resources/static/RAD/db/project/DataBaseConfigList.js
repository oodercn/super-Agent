ood.Class('RAD.db.project.DataBaseConfigList', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],
        Required: [],
        properties: {
            "path": "form/myspace/versionspace/projectManager/0/RAD/db/project/DataBaseConfigList.cls",
            "personId": "devdev",
            "personName": "devdev",
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
                    .setHost(host, "api_4")
                    .setName("api_4")
                    .setAutoRun(true)
                    .setQueryURL("/admin/loadDBConfig")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name": "ood_ui_pagebar6",
                            "path": "",
                            "type": "pagebar"
                        },
                        {
                            "name": "ood_ui_block16",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name": "ood_ui_treegrid51",
                            "path": "data",
                            "type": "treegrid"
                        },
                        {
                            "name": "ood_ui_pagebar6",
                            "path": "size",
                            "type": "pagebar"
                        }
                    ])
                    .setResponseCallback([])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "api_17")
                    .setRequestDataSource([
                        {
                            "type": "treegrid",
                            "name": "ood_ui_treegrid51",
                            "path": ""
                        },
                        {
                            "type": "form",
                            "name": "ood_ui_block16",
                            "path": ""
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .setQueryURL("/admin/removeDBformPrj")
                    .setQueryMethod("POST")
                    .onData([
                        {
                            "args": [
                                "{page.api_4.invoke()}"
                            ],
                            "desc": "动作 1",
                            "method": "invoke",
                            "redirection": "other:callback:call",
                            "target": "api_4",
                            "type": "control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog16")
                    .setLeft("1.6666666666666667em")
                    .setTop("7.5em")
                    .setWidth("52.5em")
                    .setHeight("35em")
                    .setCaption("导入库表")
                    .setImageClass("ri-database-2-line")
            );

            host.ood_ui_dialog16.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block16")
                    .setDesc("主框架")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block16.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block62")
                    .setDock("fill")
                    .setLeft("20em")
                    .setTop("20em")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_ui_treegrid51")
                    .setName("personlist")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption": "驱动标识",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "configKey",
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": "表名前缀",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "simpleName",
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "caption": "模板",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": false,
                            "hidden": false,
                            "id": "ftlTemps",
                            "type": "label",
                            "width": "12em"
                        },
                        {
                            "caption": "数据库表名",
                            "colResizer": true,
                            "editable": false,
                            "flexSize": true,
                            "hidden": false,
                            "id": "tableNames",
                            "type": "label",
                            "width": "12em"
                        }
                    ])
                    .setUidColumn("configKey")
                    .setTagCmds([
                        {
                            "caption": "删除",
                            "hidden": false,
                            "id": "del",
                            "itemClass": "ri-subtract-line",
                            "location": "right",
                            "tag": "row"
                        }
                    ])
                    .setValue("")
                    .onCmd([
                        {
                            "args": [
                                "{page.api_17.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "del",
                                    "left": "{args[2]}"
                                }
                            ],
                            "desc": "动作 1",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "return": false,
                            "target": "api_17",
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
                            "desc": "动作 2",
                            "method": "show2",
                            "redirection": "page",
                            "target": "RAD.db.project.TableSelectTree",
                            "type": "page"
                        }
                    ])
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block63")
                    .setDock("bottom")
                    .setLeft("15em")
                    .setTop("45em")
                    .setHeight("2.8333333333333335em")
            );

            host.ood_ui_block63.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host, "ood_ui_pagebar6")
                    .setName("personpagebar")
                    .setLeft("0.16666666666666666em")
                    .setTop("1em")
                    .setCaption("页码自定义")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host, "ood_ui_toolbar100")
                    .setName("persontoolbar")
                    .setItems([
                        {
                            "caption": "grp1",
                            "hidden": false,
                            "id": "grp1",
                            "sub": [
                                {
                                    "caption": "添加",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "add",
                                    "imageClass": "ri-add-circle-line",
                                    "position": "absolute"
                                },
                                {
                                    "caption": "删除",
                                    "hidden": true,
                                    "iconFontSize": "",
                                    "id": "delete",
                                    "imageClass": "ri-close-line",
                                    "position": "absolute"
                                },
                                {
                                    "caption": "刷新",
                                    "hidden": false,
                                    "iconFontSize": "",
                                    "id": "reload",
                                    "imageClass": "ri-refresh-line",
                                    "position": "absolute"
                                }
                            ]
                        }
                    ])
                    .setLeft("Infinityem")
                    .setTop("3.3333333333333335em")
                    .onClick([
                        {
                            "args": [
                                "{page.api_4.invoke()}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "=",
                                    "right": "reload",
                                    "left": "{args[1].id}"
                                }
                            ],
                            "desc": "动作 3",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "redirection": "other:callback:call",
                            "return": false,
                            "target": "api_4",
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
                            "return": false,
                            "target": "RAD.db.project.ConfigTree",
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
