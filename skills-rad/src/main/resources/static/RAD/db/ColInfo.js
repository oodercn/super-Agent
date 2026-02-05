ood.Class('RAD.db.ColInfo', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],
        Required: [],
        properties: {
            "autoDestroy": true,
            "path": "form/myspace/versionspace/projectManager/0/RAD/db/ColInfo.cls",
            "personId": "devdev",
            "personName": "系统管理员",
            "projectName": "projectManager"
        },
        events: {
            "onMessage": {
                "actions": [
                    {
                        "args": [
                            "{page.api_1.setQueryData()}",
                            null,
                            null,
                            "{args[1]}",
                            "tableName"
                        ],
                        "desc": "动作 1",
                        "method": "setQueryData",
                        "redirection": "other:callback:call",
                        "target": "api_1",
                        "type": "control"
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
                ood.create("ood.APICaller")
                    .setHost(host, "addColAjax")
                    .setRequestDataSource([
                        {
                            "type": "form",
                            "name": "ood_ui_dialog62",
                            "path": ""
                        }
                    ])
                    .setResponseDataTarget([])
                    .setResponseCallback([])
                    .setQueryURL("/admin/fdt/magager/AddCol")
                    .setQueryMethod("POST")
                    .setProxyType("AJAX")
                    .setRequestType("JSON")
                    .onData([
                        {
                            "args": [
                                "{args[1].errDes}"
                            ],
                            "conditions": [
                                {
                                    "symbol": "!=",
                                    "right": "{0}",
                                    "left": "{args[1].requestStatus}"
                                }
                            ],
                            "desc": "动作 3",
                            "method": "alert",
                            "return": false,
                            "target": "msg",
                            "type": "other"
                        },
                        {
                            "args": [],
                            "desc": "动作 1",
                            "method": "destroy",
                            "target": "RAD.db.ColInfo",
                            "type": "page"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog62")
                    .setName("addCol")
                    .setLeft("25em")
                    .setTop("5em")
                    .setHeight("32.5em")
                    .setVisibility("visible")
                    .setResizer(false)
                    .setCaption("添加字段")
                    .setImageClass("ood-icon-bullet")
                    .setMinBtn(false)
                    .setMaxBtn(false)
            );

            host.ood_ui_dialog62.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "canNull")
                    .setName("canNull")
                    .setLeft("5.416666666666667em")
                    .setTop("17.083333333333332em")
                    .setWidth("12em")
                    .setZIndex(1002)
                    .setCaption("可否为空：")
                    .setValue(true)
            );

            host.ood_ui_dialog62.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block148")
                    .setDataBinder("colvalue")
                    .setDock("fill")
                    .setLeft("10.833333333333334em")
                    .setTop("13.333333333333334em")
                    .setFormMethod("post")
                    .setFormTarget("[APICaller]")
                    .onRender([
                        {
                            "args": [
                                "{page.tableName.setUIValue()}",
                                null,
                                null,
                                "{page.properties.tableName}"
                            ],
                            "desc": "动作 1",
                            "method": "setUIValue",
                            "redirection": "other:callback:call",
                            "target": "tableName",
                            "type": "control"
                        },
                        {
                            "args": [
                                "{page.getValue()}"
                            ],
                            "desc": "动作 3",
                            "method": "setFormValues",
                            "target": "ood_ui_dialog62",
                            "type": "control"
                        }
                    ])
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "configKey")
                    .setName("configKey")
                    .setValue("")
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ood_ui_button71")
                    .setLeft("5.333333333333333em")
                    .setTop("21.166666666666668em")
                    .setCaption("确认")
                    .onClick([
                        {
                            "args": [],
                            "desc": "动作 3",
                            "koFlag": "_DI_fail",
                            "method": "invoke",
                            "okFlag": "_DI_succeed",
                            "target": "addColAjax",
                            "type": "control"
                        }
                    ])
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ood_ui_button72")
                    .setLeft("13.666666666666666em")
                    .setTop("21.166666666666668em")
                    .setCaption("取消")
                    .onClick([
                        {
                            "args": [],
                            "desc": "动作 1",
                            "method": "destroy",
                            "target": "ood_ui_dialog62",
                            "type": "control"
                        }
                    ])
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "cnname")
                    .setName("cnname")
                    .setLeft("0.3333333333333333em")
                    .setTop("7em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("中文名称：")
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "name")
                    .setName("name")
                    .setLeft("0.3333333333333333em")
                    .setTop("3.6666666666666665em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("字段名称：")
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "length")
                    .setName("length")
                    .setLeft("0.3333333333333333em")
                    .setTop("13.666666666666666em")
                    .setWidth("18em")
                    .setHeight("2em")
                    .setLabelSize("8em")
                    .setLabelCaption("长度：")
                    .setType("number")
                    .setGroupingSeparator("")
                    .setForceFillZero(false)
                    .setPrecision(0)
                    .setIncrement(1)
                    .setValue(20)
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "colType")
                    .setName("colType")
                    .setLeft("0.3333333333333333em")
                    .setTop("1.1666666666666667em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("字段类型：")
            );

            host.ood_ui_block148.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "tablename")
                    .setName("tablename")
                    .setLeft("0.3333333333333333em")
                    .setTop("1.1666666666666667em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("表名称：")
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
            "height": 600,
            "mobileFrame": false,
            "width": 800
        }
    }


});
