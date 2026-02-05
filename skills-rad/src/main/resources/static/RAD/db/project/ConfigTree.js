
ood.Class('RAD.db.project.ConfigTree', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/project/ConfigTree.cls",
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
                    .setHost(host,"getDbCofigTree")
                    .setName("getDbCofigTree")
                    .setAutoRun(true)
                    .setQueryURL("/admin/getDbConfigTree")
                    .setQueryMethod("POST")
                    .setRequestDataSource([ ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview36",
                            "path":"data",
                            "type":"treeview"
                        },
                        {
                            "name":"ood_ui_dialog63",
                            "path":"data",
                            "type":"form"
                        }
                    ])
                    .setResponseCallback([ ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"addProjectDBConfig")
                    .setRequestDataSource([
                        {
                            "type":"treeview",
                            "name":"ood_ui_treeview36",
                            "path":""
                        },
                        {
                            "type":"form",
                            "name":"ood_ui_dialog63",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryURL("/admin/addProjectDBConfig")
                    .setQueryMethod("POST")
                    .onData([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"ood_ui_dialog63",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog63")
                    .setLeft("24.166666666666668em")
                    .setTop("2.5em")
                    .setWidth("28.333333333333332em")
                    .setHeight("41.666666666666664em")
                    .setCaption("所有数据连接")
            );

            host.ood_ui_dialog63.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block284")
                    .setDock("fill")
                    .setLeft("1.6666666666666667em")
                    .setTop("10.833333333333334em")
            );

            host.ood_ui_block284.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_block284.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"ood_ui_comboinput550")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("18em")
                    .setLabelSize("6em")
                    .setLabelCaption("#ConigKey")
                    .setType("helpinput")
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
                            "args":[ ],
                            "desc":"动作 2",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"loadapi",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block284.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block285")
                    .setDock("bottom")
                    .setLeft("9.166666666666666em")
                    .setTop("30.833333333333332em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block285.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button80")
                    .setLeft("6.583333333333333em")
                    .setTop("0.75em")
                    .setCaption("确定")
                    .setImageClass("ri-check-square-line")
                    .onClick([
                        {
                            "args":[
                                "{page.addProjectDBConfig.invoke()}"
                            ],
                            "desc":"动作 2",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"addProjectDBConfig",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block285.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button81")
                    .setLeft("14.916666666666666em")
                    .setTop("0.8333333333333334em")
                    .setCaption("关闭")
                    .setImageClass("ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"RAD.db.project.ConfigTree",
                            "type":"otherModuleCall"
                        }
                    ])
            );

            host.ood_ui_block284.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host,"ood_ui_treeview36")
                    .setItems([
                        {
                            "caption":"所有库表",
                            "hidden":false,
                            "id":"all",
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
