
ood.Class('RAD.db.DBConfig', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/DBConfig.cls",
            "personId":"devdev",
            "personName":"系统管理员",
            "projectName":"projectManager"
        },
        events:{
            "afterShow":{
                "actions":[
                    {
                        "args":[
                            "dblist"
                        ],
                        "desc":"动作 1",
                        "method":"fireItemClickEvent",
                        "target":"dbconfigtab",
                        "type":"control"
                    }
                ]
            }
        },
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"saveDBConfig")
                    .setName("saveDBConfig")
                    .setQueryURL("/admin/addDBConfig")
                    .setQueryMethod("POST")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_formlayout10",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setProxyType("AJAX")
                    .onData([
                        {
                            "args":[
                                "{args[1].errdes}",
                                "保存失败！"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"alert",
                            "okFlag":"_DI_succeed",
                            "return":false,
                            "target":"msg",
                            "type":"other",
                            "onOK":2
                        },
                        {
                            "args":[
                                null,
                                "保存成功",
                                200,
                                5000
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{0}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"message",
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[
                                "{page.reloadParent()}"
                            ],
                            "desc":"动作 3",
                            "method":"call",
                            "target":"callback",
                            "type":"other"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog6")
                    .setLeft("0.8333333333333334em")
                    .setTop("5em")
                    .setWidth("67.5em")
                    .setHeight("48.333333333333336em")
                    .setCaption("数据源维护")
                    .setImageClass("ood-icon-dragadd")
            );

            host.ood_ui_dialog6.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block81")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("6.666666666666667em")
            );

            host.ood_ui_block81.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block83")
                    .setDock("fill")
                    .setLeft("15em")
                    .setTop("13.333333333333334em")
            );

            host.ood_ui_block83.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block84")
                    .setDock("fill")
                    .setLeft("34.166666666666664em")
                    .setTop("16.666666666666668em")
                    .setWidth("52.5em")
                    .setHeight("18.833333333333332em")
            );

            host.ood_ui_block84.append(
                ood.create("ood.UI.FormLayout")
                    .setHost(host,"ood_ui_formlayout10")
                    .setName("DBConfig")
                    .setDock("top")
                    .setLeft("3.25em")
                    .setTop("2.4166666666666665em")
                    .setWidth("66.21212121212122em")
                    .setHeight("13.636363636363637em")
                    .setFloatHandler(false)
                    .setDefaultRowHeight(30)
                    .setLayoutData({
                        "cells":{
                            "A1":{
                                "value":"驱动标识"
                            },
                            "A2":{
                                "value":"数据库驱动"
                            },
                            "C4":{
                                "value":"密码："
                            },
                            "A3":{
                                "value":"数据库连接串："
                            },
                            "C5":{
                                "value":"最小连接数"
                            },
                            "A4":{
                                "value":"用户名："
                            },
                            "A5":{
                                "value":"最大连接数："
                            }
                        },
                        "colSetting":{
                            "A":{
                                "manualWidth":155
                            },
                            "B":{
                                "manualWidth":156
                            },
                            "C":{
                                "manualWidth":70
                            }
                        },
                        "cols":4,
                        "merged":[
                            {
                                "col":1,
                                "colspan":3,
                                "removed":false,
                                "row":1,
                                "rowspan":1
                            },
                            {
                                "col":1,
                                "colspan":3,
                                "removed":false,
                                "row":2,
                                "rowspan":1
                            },
                            {
                                "col":1,
                                "colspan":3,
                                "removed":false,
                                "row":0,
                                "rowspan":1
                            }
                        ],
                        "rowSetting":{
                            "1":{
                                "manualHeight":30
                            },
                            "2":{
                                "manualHeight":30
                            },
                            "3":{
                                "manualHeight":30
                            },
                            "4":{
                                "manualHeight":30
                            },
                            "5":{
                                "manualHeight":30
                            }
                        },
                        "rows":5
                    })
                    .setRendererCDNJS("RAD/formlayout/handsontable.full.min.js")
                    .setRendererCDNCSS("RAD/formlayout/handsontable.full.min.css")
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("52.5em")
                    .setHeight("2.272727272727273em")
                    .setLabelPos("none")
                    .setMultiLines(true),
                "B1"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"password")
                    .setName("password")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("35.37878787878788em")
                    .setHeight("2.1969696969696972em")
                    .setLabelPos("none")
                    .setMultiLines(true),
                "D4"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"driver")
                    .setName("driver")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("52.5em")
                    .setHeight("2.1969696969696972em")
                    .setLabelPos("none"),
                "B2"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"minConnections")
                    .setName("minConnections")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("35.37878787878788em")
                    .setHeight("2.4242424242424243em")
                    .setLabelPos("none")
                    .setType("number")
                    .setValue(10),
                "D5"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"serverURL")
                    .setName("serverURL")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("52.5em")
                    .setHeight("2.1969696969696972em")
                    .setLabelPos("none"),
                "B3"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"ood_ui_input67")
                    .setName("D3")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("0.6060606060606061em")
                    .setHeight("1.6666666666666667em")
                    .setLabelPos("none"),
                "D3"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"username")
                    .setName("username")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("11.742424242424242em")
                    .setHeight("2.1969696969696972em")
                    .setLabelPos("none"),
                "B4"
            );

            host.ood_ui_formlayout10.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"maxConnections")
                    .setName("maxConnections")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("11.742424242424242em")
                    .setHeight("2.4242424242424243em")
                    .setLabelPos("none")
                    .setType("number")
                    .setValue(50),
                "B5"
            );

            host.ood_ui_block84.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host,"dbconfigtab")
                    .setName("dbconfigtab")
                    .setItems([
                        {
                            "caption":"库表管理",
                            "hidden":false,
                            "id":"dblist",
                            "imageClass":"ood-icon-dialog",
                            "tag":"RAD.db.TableManager"
                        },
                        {
                            "caption":"连接监控",
                            "hidden":false,
                            "id":"console",
                            "imageClass":"ood-icon-clock",
                            "tag":"RAD.db.console.SqlConsole2"
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setValue("console")
                    .onItemSelected([
                        {
                            "args":[
                                "{ood.showModule2()}",
                                null,
                                null,
                                "{args[1].tag}",
                                "dbconfigtab",
                                "{args[1].id}",
                                null,
                                "{page.getData()}",
                                ""
                            ],
                            "desc":"动作 1",
                            "method":"showModule2",
                            "redirection":"other:callback:call",
                            "target":"url",
                            "type":"other"
                        }
                    ])
            );

            host.ood_ui_block81.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block82")
                    .setDock("bottom")
                    .setLeft("10.833333333333334em")
                    .setTop("22.5em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block82.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button24")
                    .setName("save")
                    .setLeft("30.25em")
                    .setTop("0.75em")
                    .setWidth("4em")
                    .setCaption("取消")
                    .setImageClass("ood-icon-error")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"destroy",
                            "okFlag":"_DI_succeed",
                            "target":"RAD.db.DBConfig",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_block82.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button18")
                    .setName("save")
                    .setLeft("24.416666666666668em")
                    .setTop("0.75em")
                    .setCaption("保存")
                    .setImageClass("ri-check-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"saveDBConfig",
                            "type":"control"
                        }
                    ])
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        }  } ,
    Static:{
        "designViewConf":{
            "height":1024,
            "mobileFrame":false,
            "width":1280
        }
    }



});
