
ood.Class('RAD.db.console.SqlConsole2', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/console/SqlConsole.cls",
            "projectName":"projectManager"
        },
        events:{},
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"startSqlMonitor")
                    .setName("startSqlMonitor")
                    .setQueryURL("/admin/fdt/magager/startSqlMonitor")
                    .setQueryMethod("GET")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_block62",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryArgs({
                        "pageIndex":"",
                        "pageSize":""
                    })
                    .onData([
                        {
                            "args":[
                                "成功开启！",
                                "成功开启！",
                                200,
                                5000
                            ],
                            "conditions":[
                                {
                                    "symbol":"!=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "method":"message",
                            "return":false,
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[
                                "启动失败",
                                "启动失败"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"alert",
                            "target":"msg",
                            "type":"other"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"load")
                    .setName("load")
                    .setAutoRun(true)
                    .setQueryURL("/admin/fdt/magager/getSqlCountInfo")
                    .setQueryMethod("GET")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_pagebar6",
                            "path":"",
                            "type":"pagebar"
                        },
                        {
                            "name":"ood_ui_block62",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treegrid51",
                            "path":"data",
                            "type":"treegrid"
                        },
                        {
                            "name":"ood_ui_pagebar6",
                            "path":"size",
                            "type":"pagebar"
                        }
                    ])
                    .setResponseCallback([ ])
                    .setQueryArgs({
                        "pageIndex":"",
                        "pageSize":""
                    })
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"resetStatistics")
                    .setName("resetStatistics")
                    .setQueryURL("/admin/fdt/magager/resetStatistics")
                    .setQueryMethod("GET")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_block62",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryArgs({
                        "pageIndex":"",
                        "pageSize":""
                    })
                    .onData([
                        {
                            "args":[
                                "成功重置！",
                                "成功重置！",
                                200,
                                5000
                            ],
                            "conditions":[
                                {
                                    "symbol":"!=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "method":"message",
                            "return":false,
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[
                                "重置失败",
                                "重置失败"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"alert",
                            "target":"msg",
                            "type":"other"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"stopSqlMonitor")
                    .setName("stopSqlMonitor")
                    .setQueryURL("/admin/fdt/magager/stopSqlMonitor")
                    .setQueryMethod("GET")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_block62",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryArgs({
                        "pageIndex":"",
                        "pageSize":""
                    })
                    .onData([
                        {
                            "args":[
                                null,
                                "成功关闭！",
                                200,
                                5000
                            ],
                            "conditions":[
                                {
                                    "symbol":"!=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "method":"message",
                            "return":false,
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[
                                "启动失败"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"alert",
                            "target":"msg",
                            "type":"other"
                        }
                    ])
            );


            // append(
            //     ood.create("ood.UI.Dialog")
            //         .setHost(host,"ood_ui_block29")
            //         .setLeft("5em")
            //         .setTop("1.6666666666666667em")
            //         .setWidth("59.166666666666664em")
            //         .setHeight("37.5em")
            //         .setCaption("sql执行监控")
            //         .setImageClass("ood-icon-dragmove")
            // );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block29")
                    .setDesc("主框架")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block29.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block62")
                    .setDock("fill")
                    .setLeft("20em")
                    .setTop("20em")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setValue("")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_ui_treegrid51")
                    .setName("sqlinfolist")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption":"驱动标识",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"configKey",
                            "readonly":true,
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"SQL 类型",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"type",
                            "readonly":true,
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"执行次数",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"queryConnt",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        },
                        {
                            "caption":"执行总耗时",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"queryTime",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        },
                        {
                            "caption":"平均执行时间",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"perSecond",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        }
                    ])
                    .setUidColumn("configKey")
                    .setValue("")
                    .onCmd([
                        {
                            "args":[
                                "{page.api_17.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"del",
                                    "left":"{args[2]}"
                                }
                            ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"api_17",
                            "type":"control"
                        }
                    ])
                    .onDblclickRow([
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{args[1]}",
                                ""
                            ],
                            "desc":"动作 2",
                            "method":"show2",
                            "redirection":"page",
                            "target":"RAD.db.console.SqlInfo",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"sqlinfotoolbar")
                    .setName("sqlinfotoolbar")
                    .setItems([
                        {
                            "caption":"grp1",
                            "hidden":false,
                            "id":"grp1",
                            "sub":[
                                {
                                    "caption":"启动",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"start",
                                    "imageClass":"ri-add-circle-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"停止",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"stop",
                                    "imageClass":"ri-close-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"重置",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reset",
                                    "imageClass":"ood-icon-inserthr",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"刷新",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ri-refresh-line",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
                    .setLeft("Infinityem")
                    .setTop("3.3333333333333335em")
                    .onClick([
                        {
                            "args":[
                                "{page.load.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"reload",
                                    "left":"{args[1].id}"
                                }
                            ],
                            "desc":"动作 3",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"load",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.startSqlMonitor.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"start",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"startSqlMonitor",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.stopSqlMonitor.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"stop",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 4",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"stopSqlMonitor",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.resetStatistics.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"reset",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 5",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"resetStatistics",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block63")
                    .setDock("bottom")
                    .setLeft("15em")
                    .setTop("45em")
                    .setHeight("2.8333333333333335em")
            );

            host.ood_ui_block63.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host,"ood_ui_pagebar6")
                    .setName("personpagebar")
                    .setLeft("1.3333333333333333em")
                    .setTop("0.5em")
                    .setCaption("页码自定义")
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        }  } ,
    Static:{
        "designViewConf":{

        }
    }



});
