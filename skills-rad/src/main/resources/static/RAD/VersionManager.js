﻿ood.Class('RAD.VersionManager', 'ood.Module', {
    Instance: {
        Dependencies: [],
        Required: [],
        properties: {},

        initialize: function () {
        },
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "api_3")
                    .setName("api_3")
                    .setQueryURL(CONF.getAllFileVersionService)
                    .setQueryMethod("POST")
                    .setRequestDataSource([])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .setQueryArgs({
                        "projectName": SPA.curProjectName,
                        "pageSize": 20,
                        "path": SPA.tabsMain.getItemByItemId(SPA.tabsMain.getUIValue()).id,
                        "pageIndex": 1
                    })
                    .setProxyType("AJAX")
                    .onData([
                        {
                            "desc": "动作 1",
                            "type": "control",
                            "target": "ood_ui_treegrid10",
                            "args": [],
                            "method": "removeAllRows"
                        },
                        {
                            "desc": "动作 2",
                            "type": "control",
                            "target": "ood_ui_treegrid10",
                            "args": [
                                "{args[1].data}",
                                "",
                                "",
                                false
                            ],
                            "method": "insertRows"
                        },
                        {
                            "desc": "动作 3",
                            "type": "control",
                            "target": "ood_ui_pagebar4",
                            "args": [
                                "{page.ood_ui_pagebar4.setTotalCount()}",
                                undefined,
                                undefined,
                                "{args[1].size}"
                            ],
                            "method": "setTotalCount",
                            "redirection": "other:callback:call"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "api_2")
                    .setName("api_2")
                    .setQueryURL(CONF.delFileVersionService)
                    .setRequestDataSource([])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .setQueryArgs({
                        "paths": ""
                    })
                    .onData([
                        {
                            "desc": "动作 1",
                            "type": "control",
                            "target": "api_3",
                            "args": [],
                            "method": "invoke",
                            "onOK": 0,
                            "onKO": 1,
                            "okFlag": "_DI_succeed",
                            "koFlag": "_DI_fail"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_panel11")
                    .setLeft("12.5em")
                    .setTop("4.166666666666667em")
                    .setWidth("54.166666666666664em")
                    .setHeight("30.833333333333332em")
                    .setCaption("点击左侧图标下载或编辑")
            );

            host.ood_ui_panel11.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host, "ood_ui_pagebar4")
                    .setDock("bottom")
                    .setLeft("21.666666666666668em")
                    .setTop("78.33333333333333em")
                    .setCaption("页码自定义")
                    .setTextTpl("[*]")
                    .onPageSet([
                        {
                            "desc": "动作 4",
                            "type": "control",
                            "target": "api_3",
                            "args": [
                                "{page.api_3.setQueryData()}",
                                undefined,
                                undefined,
                                "{page.properties.path}",
                                "path"
                            ],
                            "method": "setQueryData",
                            "redirection": "other:callback:call",
                            "conditions": [
                                {
                                    "left": "{page.properties}",
                                    "symbol": "objhaskey",
                                    "right": "path"
                                }
                            ]
                        },
                        {
                            "desc": "动作 1",
                            "type": "control",
                            "target": "api_3",
                            "args": [
                                "{page.api_3.setQueryData()}",
                                undefined,
                                undefined,
                                "{page.ood_ui_pagebar4.getPageCount()}",
                                "pageSize"
                            ],
                            "method": "setQueryData",
                            "redirection": "other:callback:call"
                        },
                        {
                            "desc": "动作 2",
                            "type": "control",
                            "target": "api_3",
                            "args": [
                                "{page.api_3.setQueryData()}",
                                undefined,
                                undefined,
                                "{args[1]}",
                                "pageIndex"
                            ],
                            "method": "setQueryData",
                            "redirection": "other:callback:call"
                        },
                        {
                            "desc": "动作 3",
                            "type": "control",
                            "target": "api_3",
                            "args": [
                                function () {
                                    if (resumeFun) resumeFun("okData", arguments, fun.okFlag);
                                },
                                function () {
                                    if (resumeFun) resumeFun("koData", arguments, fun.koFlag);
                                }
                            ],
                            "method": "invoke",
                            "onOK": 0,
                            "onKO": 1,
                            "okFlag": "_DI_succeed",
                            "koFlag": "_DI_fail"
                        }
                    ])
            );

            host.ood_ui_panel11.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_ui_treegrid10")
                    .setLeft("0em")
                    .setTop("0em")
                    .setRowHeight("4em")
                    .setGridHandlerCaption("常用操作")
                    .setRowHandlerWidth("8em")

                    .setHeader([
                        {
                            "id": "versionID",
                            "caption": "版本ID",
                            "type": "label",
                            "width": "8em",
                            "hidden": true
                        },
                        {
                            "id": "fileName",
                            "caption": "文件名称",
                            "type": "label",
                            "flexSize": true,
                            "width": "12em"
                        },

                        {
                            "id": "index",
                            "caption": "版本号",
                            "type": "label",
                            "width": "3em"
                        },
                        {
                            "id": "createTime",
                            "caption": "创建时间",
                            "type": "datetime",
                            "width": "12em"
                        },
                        {
                            "id": "length",
                            "caption": "文件大小",
                            "type": "label",
                            "width": "4em"
                        },
                        {
                            "id": "personName",
                            "caption": "上传人",
                            "type": "label",
                            "width": "6em"
                        },
                        {
                            "id": "fileId",
                            "caption": "文件ID",
                            "type": "label",
                            "width": "8em",
                            "hidden": true,
                            "colResizer": false
                        },
                        {
                            "id": "hash",
                            "caption": "HASH",
                            "type": "label",
                            "hidden": true,
                            "width": "8em"
                        },
                        {
                            "id": "path",
                            "caption": "VFS路径",
                            "hidden": true,
                            "type": "label",
                            "width": "8em"
                        },
                        {
                            "id": "name",
                            "caption": "版本名称",
                            "type": "label",
                            "hidden": true,
                            "width": "8em"
                        }
                    ])
                    .setUidColumn("versionID")
                    .setTagCmds([
                        {
                            "id": "delete",
                            "type": "button",
                            "location": "left",
                            "itemClass": "ri-close-line",
                            "tips": "删除",
                            "tag": "row"
                        },
                        {
                            "id": "download",
                            "type": "button",
                            "location": "left",
                            "itemClass": "ri-download-line", 
                            "tips": "下载",
                            "tag": "row"
                        },
                        {
                            "id": "edit",
                            "type": "button",
                            "location": "left",
                            "itemClass": "ri-external-link-line",
                            "tips": "编辑",
                            "tag": "row"
                        }
                    ])
                    .setValue("")
                    .onCmd([
                        {
                            "desc": "动作 1",
                            "type": "control",
                            "target": "api_2",
                            "args": [
                                "{page.api_2.setQueryData()}",
                                undefined,
                                undefined,
                                "{args[1].versionID}",
                                "paths"
                            ],
                            "method": "setQueryData",
                            "conditions": [
                                {
                                    "left": "{args[2]}",
                                    "symbol": "=",
                                    "right": "delete"
                                }
                            ],
                            "redirection": "other:callback:call"
                        },
                        {
                            "desc": "动作 2",
                            "type": "control",
                            "target": "api_2",
                            "args": [],
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "koFlag": "_DI_fail",
                            "conditions": [
                                {
                                    "left": "{args[2]}",
                                    "symbol": "=",
                                    "right": "delete"
                                }
                            ],
                            "return": false,
                            "onOK": 0,
                            "onKO": 1
                        },
                        {
                            "desc": "动作 4",
                            "type": "other",
                            "target": "callback",
                            "args": [
                                "{page.functions.downloadfile}",
                                undefined,
                                undefined,
                                "{args[1].path}"
                            ],
                            "method": "call",
                            "conditions": [
                                {
                                    "left": "{args[2]}",
                                    "symbol": "=",
                                    "right": "download"
                                }
                            ]
                        },
                        "_ood_ui_treegrid10_oncmd"
                    ])
                    .onDblclickRow([
                        {
                            "desc": "动作 1",
                            "type": "other",
                            "target": "callback",
                            "args": [
                                "{page.functions.downloadfile}",
                                undefined,
                                undefined,
                                "{args[1].path}"
                            ],
                            "method": "call"
                        }
                    ])
                    .onCommand([])
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend: function (parent, subId, left, top) {
            return false;
        },
        functions: {
            "downloadfile": {
                "desc": "下载文件",
                "params": [
                    {
                        "id": "path",
                        "type": "String",
                        "desc": "文件地址"
                    }
                ],
                "actions": [
                    function (path) {
                        var url = '/vfs/download?path=' + path + "&tiem=" + new Date();
                        var dio = ood.create("ood.UI.Div").setLeft("0").setTop("0").setWidth("0").setHeight("0").setIframeAutoLoad(url).show();
                    }
                ]
            }
        },
        events: {
            "onRender": [
                {
                    "desc": "动作 1",
                    "type": "control",
                    "target": "ood_ui_panel11",
                    "args": [
                        {},
                        {
                            "caption": "{page.properties.path}"
                        }
                    ],
                    "method": "setProperties"
                }
            ]
        },
        _ood_ui_treegrid10_oncmd: function (profile, row, cmdkey, e, src) {
            var ns = this, uictrl = profile.boxing();
            switch (cmdkey) {
                case  'edit':
                    var item = SPA.tabsMain.getItemByItemId(SPA.tabsMain.getUIValue());
                    item.value = row.path;
                    item.name = row.fileName;
                    SPA._openfile(item, row.path);
                    break;
                default:

            }
        }

    }
});
