
ood.Class('RAD.resource.ImgConfigList', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/App/resource/img/ImgConfigList.cls",
            "personId":"devdev",
            "personName":"devdev",
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
                    .setHost(host,"api_4")
                    .setName("api_4")
                    .setAutoRun(true)
                    .setQueryURL("/admin/plugs/img/getProjectImgs")
                    .setQueryMethod("POST")
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
                ood.create("ood.APICaller")
                    .setHost(host,"delfontAjax")
                    .setRequestDataSource([
                        {
                            "type":"treegrid",
                            "name":"ood_ui_treegrid51",
                            "path":""
                        },
                        {
                            "type":"form",
                            "name":"ood_ui_block62",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryURL("/admin/plugs/img/delImg")
                    .setQueryMethod("POST")
                    .onData([
                        {
                            "args":[
                                "{page.api_4.invoke()}"
                            ],
                            "desc":"动作 1",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"api_4",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block28")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block28.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block62")
                    .setDock("fill")
                    .setLeft("20em")
                    .setTop("20em")
            );

            host.ood_ui_block62.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
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
                    .setHeader([
                        {
                            "caption":"fontid",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":true,
                            "id":"id",
                            "readonly":true,
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"名称",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"caption",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        },
                        {
                            "caption":"过滤规则",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"pattern",
                            "readonly":true,
                            "type":"label",
                            "width":"16em"
                        },
                        {
                            "caption":"图片宽度",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"imageWidth",
                            "readonly":true,
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"图片高度",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"imageHeight",
                            "readonly":true,
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"描述",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"description",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        }
                    ])
                    .setUidColumn("id")
                    .setTagCmds([
                        {
                            "caption":"删除",
                            "hidden":false,
                            "id":"del",
                            "itemClass":"ri-subtract-line",
                            "location":"right",
                            "pos":"row"
                        }
                    ])
                    .setValue("")
                    .onCmd([
                        {
                            "args":[
                                "{page.delfontAjax.invoke()}"
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
                            "target":"delfontAjax",
                            "type":"control"
                        }
                    ])
                    .setCustomStyle({
                        "KEY":{ }
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
                                    "caption":"添加",
                                    "hidden":false,
                                    "id":"add",
                                    "imageClass":"ri-add-circle-line",
                                    "position":"absolute",
                                    "iconFontSize":""
                                },
                                {
                                    "caption":"删除",
                                    "hidden":true,
                                    "id":"delete",
                                    "imageClass":"ri-close-line",
                                    "position":"absolute",
                                    "iconFontSize":""
                                },
                                {
                                    "caption":"刷新",
                                    "hidden":false,
                                    "id":"reload",
                                    "imageClass":"ri-refresh-line",
                                    "position":"absolute",
                                    "iconFontSize":""
                                }
                            ]
                        }
                    ])
                    .setLeft("Infinityem")
                    .setTop("3.3333333333333335em")
                    .onClick([
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{page.getData()}",
                                "{page}",
                                true

                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"add",
                                    "left":"{args[1].id}"
                                }
                            ],
                            "desc":"动作 3",
                            "koFlag":"_DI_fail",
                            "method":"show2",
                            "okFlag":"_DI_succeed",
                            "redirection":"page",
                            "return":false,
                            "target":"RAD.resource.ImageTree"
                        },
                        {
                            "args":[
                                "{page.api_4.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"reload",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"api_4",
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
    Static:{}



});
