ood.Class('RAD.ProjectTree', 'ood.Module', {
    Instance: {
        Dependencies: [],
        Required: [],
        events: {},
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
                    .setHost(host, "openFolder")
                    .setName("openFolder")
                    .setQueryAsync(false)
                    .setQueryURL(CONF.openOtherFolderService)
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "type":"treeview",
                            "name":"ood_ui_treeview27",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "getProjectTree")
                    .setName("getProjectTree")
                    .setAutoRun(true)
                    .setQueryURL(CONF.getAllProjectTreeSrvice)
                    .setRequestDataSource([
                        {
                            "name": "dialog",
                            "path": "",
                            "type": "form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name": "ood_ui_treeview27",
                            "path": "data",
                            "type": "treeview"
                        },
                        {
                            "name": "dialog",
                            "path": "data",
                            "type": "form"
                        }
                    ])
                    .setResponseCallback([])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host, "copyTo")
                    .setName("copyTo")
                    .setQueryAsync(false)
                    .setQueryURL(CONF.importFileService)
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "type": "treeview",
                            "name": "ood_ui_treeview27",
                            "path": ""
                        },
                        {
                            "type": "form",
                            "name": "dialog",
                            "path": ""
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .onData("_copyto_ondata")
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("12.5em")
                    .setTop("7.5em")
                    .setHeight("37.5em")
                    .setCaption("$RAD.toolbox.selectResource")
                    .setImageClass("ri-3d-box-line")
            );

            host.dialog.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "pattern")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("15em")
                    .setLabelSize("3em")
                    .setLabelCaption("$RAD.toolbox.search")
                    .setType("helpinput")
                    .onChange("_reload")
            );

            host.dialog.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "projectName")
                    .setName("projectName")
                    .setValue(SPA.curProjectName)
            );
            host.dialog.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "tpath")
                    .setName("tpath")
                    .setValue("")
            );


            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block249")
                    .setDock("bottom")
                    .setLeft("7.5em")
                    .setTop("18.333333333333332em")
                    .setHeight("3.5833333333333335em")
            );

            host.ood_ui_block249.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ood_ui_button38")
                    .setLeft("8.333333333333334em")
                    .setTop("0.8333333333333334em")
                    .setCaption("确定")
                    .setImageClass("iconfont iconduigoux")
                    .onClick([
                        {
                            "args": [
                                "$RAD.toolbox.selectResource",
                                "$RAD.toolbox.selectResource"
                            ],
                            "conditions": [
                                {
                                    "left": "{page.ood_ui_treeview27.getUIValue()}",
                                    "right": "",
                                    "symbol": "empty"
                                }
                            ],
                            "desc": "动作 3",
                            "method": "alert",
                            "target": "msg",
                            "type": "other",
                            "onOK": 2
                        },
                        {
                            "args": [
                                "{page.copyTo.invoke()}"
                            ],
                            "desc": "动作 2",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "target": "copyTo",
                            "type": "control",
                            "redirection": "other:callback:call"
                        }
                    ])
            );

            host.dialog.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "ood_ui_treeview27")
                    .setName("lefttree")
                    .setItems([
                        {
                            "caption": "$RAD.toolbox.allProject",
                            "disabled": true,
                            "hidden": false,
                            "id": "root",
                            "initFold": false
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setInitFold(false)
                    .setSelMode("multibycheckbox")
                    .setOptBtn("ood-uicmd-opt")
                    .setTogglePlaceholder(true)
                    .setValue("")
                    .onGetContent({
                        "actions": [
                            {
                                "args": [
                                    "{page.openFolder.setQueryData()}",
                                    null,
                                    null,
                                    "{args[1]}",
                                    ""
                                ],
                                "desc": "动作 1",
                                "method": "setQueryData",
                                "redirection": "other:callback:call",
                                "target": "openFolder",
                                "type": "control"
                            },
                            {
                                "args": [
                                    "{page.openFolder.invoke()}",
                                    null,
                                    null,
                                    "{args[2]}"
                                ],
                                "desc": "动作 3",
                                "method": "call",
                                "target": "callback",
                                "type": "other"
                            }
                        ],
                        "return": "{temp.okData.data}"
                    })
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        _reload: function (profile, item) {
            ns = this;
            ns.treebarCom.setItems(buildItems(this.topNodes));
            ns.treebarCom.fireItemClickEvent('localService');

        },
        _copyto_ondata: function (profile, rspData, requestId) {
            var ns = this, uictrl = profile.boxing();
            var obj = rspData;
            // ns.dialog.free();
            if (obj && obj.requestStatus != -1) {
                ns.fireEvent('onOK', [obj.data]);
                ns.dialog.close();
                ns.destroy();
             
            } else if (obj && !obj.error) {
                ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!')

            } else ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');


        },
        customAppend: function (parent, subId, left, top) {
            return false;
        }
    }
});
