
ood.Class('RAD.api.APITree', 'ood.Module',{
    Instance:{
        initialize : function(){
        },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/App/projectmanager/api/APITree.cls",
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
                    .setHost(host,"loadapi")
                    .setName("loadapi")
                    .setAutoRun(true)
                    .setQueryURL("/admin/getAllService")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_treeview19",
                            "path":"",
                            "type":"form"
                        },
                        {
                            "name":"ood_ui_dialog13",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview19",
                            "path":"data",
                            "type":"treeview"
                        }
                    ])
                    .setResponseCallback([ ])
                    .onData([
                        {
                            "args":[
                                {
                                    "imageClass":"ood-icon-code"
                                },
                                { }
                            ],
                            "desc":"动作 1",
                            "method":"setProperties",
                            "target":"pattern",
                            "type":"control"
                        }
                    ])
                    .beforeData([
                        {
                            "args":[
                                {
                                    "imageClass":"ood-icon-loading"
                                },
                                { }
                            ],
                            "desc":"动作 1",
                            "method":"setProperties",
                            "target":"pattern",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"addApi")
                    .setName("addApi")
                    .setQueryURL("/admin/addAPI")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_block55",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog13")
                    .setLeft("15.833333333333334em")
                    .setTop("3.3333333333333335em")
                    .setWidth("28.333333333333332em")
                    .setHeight("41.666666666666664em")
                    .setCaption(ood.getRes('RAD.apitree.allAPIs') || "所有API")
                    .setImageClass("ood-icon-bullet")
            );

            host.ood_ui_dialog13.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block55")
                    .setDock("fill")
                    .setLeft("1.6666666666666667em")
                    .setTop("10.833333333333334em")
            );

            host.ood_ui_block55.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_block55.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host,"ood_ui_treeview19")

                    .setItems([
                        {
                            "caption": ood.getRes('RAD.apitree.allAPIs') || "所有API",
                            "hidden":false,
                            "id":"all",
                            "imageClass":"ood-uicmd-cmdbox",
                            "initFold":false,
                            "sub":[
                                {
                                    "caption":"管理API",
                                    "hidden":false,
                                    "id":"admin",
                                    "imageClass":"ood-icon-ood"
                                }
                            ]
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setValue("")
                    .onGetContent([
                        {
                            "args":[
                                "{page.loadapi.invoke()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{args[2]}"
                            ],
                            "desc":"动作 1",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"loadapi",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block55.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block59")
                    .setDock("bottom")
                    .setLeft("9.166666666666666em")
                    .setTop("30.833333333333332em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block59.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button17")
                    .setLeft("6.583333333333333em")
                    .setTop("0.75em")
                    .setCaption("确定")
                    .setImageClass("ri-check-square-line")
                    .onClick([
                        {
                            "args":[
                                "{page.addApi.setQueryData()}",
                                null,
                                null,
                                "{page.ood_ui_treeview19.getUIValue()}",
                                "id"
                            ],
                            "desc":"动作 2",
                            "method":"setQueryData",
                            "redirection":"other:callback:call",
                            "target":"addApi",
                            "type":"control"
                        },
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"addApi",
                            "type":"control"
                        },
                        {
                            "args":[ ],
                            "desc":"动作 3",
                            "method":"destroy",
                            "target":"ood_ui_dialog13",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block59.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button18")
                    .setLeft("14.916666666666666em")
                    .setTop("0.8333333333333334em")
                    .setCaption("关闭")
                    .setImageClass("ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"RAD.api.APITree",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_block55.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"pattern")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("18em")
                    .setLabelSize("6em")
                    .setLabelCaption("API-URL:")
                    .setType("helpinput")
                    .setImageClass("ood-icon-code")
                    .onChange([
                        {
                            "args":[
                                "{page.loadapi.setQueryData()}",
                                null,
                                null,
                                "{args[2]}",
                                "pattern"
                            ],
                            "desc":"动作 1",
                            "method":"setQueryData",
                            "redirection":"other:callback:call",
                            "target":"loadapi",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.loadapi.invoke()}",
                                null,
                                null,
                                null,
                                null,
                                ""
                            ],
                            "desc":"动作 2",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
                            "target":"loadapi",
                            "type":"control"
                        }
                    ])
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        }
    } ,
    Static:{}
});
