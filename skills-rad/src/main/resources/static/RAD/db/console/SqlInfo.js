
ood.Class('RAD.db.console.SqlInfo', 'ood.Module',{
    Instance:{
        initialize : function(){
        },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/console/SqlInfo.cls",
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
                    .setHost(host,"reload")
                    .setName("reload")
                    .setAutoRun(true)
                    .setQueryURL("/admin/fdt/magager/getSqlExcuteInfos")
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
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog12")
                    .setLeft("4.166666666666667em")
                    .setTop("5em")
                    .setWidth("59.166666666666664em")
                    .setHeight("35.833333333333336em")
                    .setCaption("SQL详细信息")
                    .setImageClass("ri ri-code-line")
            );

            host.ood_ui_dialog12.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block79")
                    .setDesc("主框架")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block79.append(
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
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"type")
                    .setName("type")
                    .setValue("")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_ui_treegrid51")
                    .setName("personlist")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setRowNumbered(true)
                    .setRowHeight("4em")
                    .setHeader([
                        {
                            "caption":"次数",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"count",
                            "readonly":true,
                            "type":"label",
                            "width":"4em"
                        },
                        {
                            "caption":"执行时间",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"totalTime",
                            "readonly":true,
                            "type":"label",
                            "width":"4em"
                        },
                        {
                            "caption":"SQL",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"sql",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        }
                    ])
                    .setTagCmds([
                        {
                            "caption":"删除",
                            "hidden":false,
                            "id":"del",
                            "itemClass":"ri ri-subtract-line",
                            "location":"right",
                            "pos":"row"
                        }
                    ])
                    .setValue("")
                    .onCmd({
                        "actions":[
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
                        ]
                    })
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
                    .setLeft("20.833333333333332em")
                    .setTop("0.8333333333333334em")
                    .setCaption("页码自定义")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"ood_ui_toolbar100")
                    .setName("persontoolbar")
                    .setItems([
                        {
                            "caption":"grp1",
                            "hidden":false,
                            "id":"grp1",
                            "sub":[
                                {
                                    "caption":"刷新",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ri ri-refresh-line",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
                    .setLeft("Infinityem")
                    .setTop("3.3333333333333335em")
                    .onClick({
                        "actions":[
                            {
                                "args":[
                                    "{page.reload.invoke()}"
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
                                "target":"reload",
                                "type":"control"
                            }
                        ]
                    })
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        }
    } ,
    Static:{
        "designViewConf":{
            "height":600,
            "mobileFrame":false,
            "width":800
        }
    }
});
