
ood.Class('RAD.api.APIList', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/api/APIList.cls",
            "personId":"devdev",
            "personName":"系统管理员",
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
                    .setQueryURL("/admin/getAPIConfig")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_pagebar3",
                            "path":"",
                            "type":"pagebar"
                        },
                        {
                            "name":"ood_ui_panel12",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treegrid10",
                            "path":"data",
                            "type":"treegrid"
                        },
                        {
                            "name":"ood_ui_pagebar3",
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
                    .setHost(host,"delete")
                    .setName("delete")
                    .setQueryURL("/admin/deleteAPIConfig")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_treegrid10",
                            "path":"",
                            "type":"treegrid"
                        },
                        {
                            "name":"ood_ui_panel12",
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
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"reload",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_panel12")
                    .setDock("fill")
                    .setLeft("2.5em")
                    .setTop("6.666666666666667em")
                    .setWidth("25em")
                    .setHeight("25.833333333333332em")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div43")
                    .setDock("bottom")
                    .setLeft("17.5em")
                    .setTop("35em")
                    .setHeight("3em")
            );

            host.ood_ui_div43.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host,"ood_ui_pagebar3")
                    .setLeft("9.5em")
                    .setTop("0.75em")
                    .setCaption("翻页")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_ui_treegrid10")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setAltRowsBg(true)
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption":"id",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":true,
                            "id":"id",
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.name') || "名称",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"caption",
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.path') || "路径",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"path",
                            "type":"label",
                            "width":"15em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.serviceClass') || "服务类",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"classpath",
                            "type":"label",
                            "width":"5em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.description') || "描述",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"desc",
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption": ood.getRes('RAD.api.serverAddress') || "服务器地址",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"serverurl",
                            "type":"label",
                            "width":"10em"
                        }
                    ])
                    .setUidColumn("id")
                    .setValue("")
            );

            host.ood_ui_panel12.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"ood_ui_toolbar12")
                    .setItems([
                        {
                            "caption":"common",
                            "hidden":false,
                            "id":"common",
                            "sub":[
                                {
                                    "caption": ood.getRes('RAD.api.add') || "添加",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"add",
                                    "imageClass":"ri-calendar-event-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption": ood.getRes('RAD.api.remove') || "移除",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"delete",
                                    "imageClass":"ri-close-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption": ood.getRes('RAD.api.refresh') || "刷新",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ri-loader-line",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
                    .setTop("40.833333333333336em")
                    .onClick([
                        {
                            "args":[
                                "{page.reload.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"reload",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"reload",
                            "type":"control",
                            "redirection":"other:callback:call"
                        },
                        {
                            "args":[
                                "{page.show2()}",
                                undefined,
                                undefined,
                                undefined,
                                undefined,
                                undefined,
                                "{page.getData()}",
                                "{page}",
                                "{true}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"add",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"show2",
                            "target":"RAD.api.APITree",
                            "type":"page",
                            "className":"RAD.api.APITree",
                            "redirection":"page"
                        },
                        {
                            "args":[
                                "{page.delete.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"delete",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 3",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"delete",
                            "type":"control",
                            "redirection":"other:callback:call"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"PAGECTX")
                    .setVisibility("hidden")
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
