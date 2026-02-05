
ood.Class('RAD.db.project.TableSelectTree', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/project/TableSelectTree.cls",
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
                    .setHost(host,"addTableNames")
                    .setName("addTableNames")
                    .setQueryURL("/admin/addTableNames")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "type":"treeview",
                            "name":"ood_ui_treeview21",
                            "path":""
                        },
                        {
                            "type":"form",
                            "name":"ood_ui_block216",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .onData([
                        {
                            "desc":"动作 2",
                            "type":"other",
                            "target":"callback",
                            "args":[
                                "{page.reloadParent()}"
                            ],
                            "method":"call"
                        },
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"ood_ui_dialog35",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"getAllTableTrees")
                    .setName("getAllTableTrees")
                    .setAutoRun(true)
                    .setQueryURL("/admin/fdt/magager/getAllTableTrees")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog35",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview21",
                            "path":"data",
                            "type":"treeview"
                        }
                    ])
                    .setResponseCallback([ ])
                    .onData([
                        {
                            "args":[
                                "{page.ood_ui_treeview21.setUIValue()}",
                                null,
                                null,
                                "{page.tableNames.getValue()}"
                            ],
                            "desc":"动作 1",
                            "method":"setUIValue",
                            "redirection":"other:callback:call",
                            "target":"ood_ui_treeview21",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog35")
                    .setLeft("15.833333333333334em")
                    .setTop("2.5em")
                    .setWidth("28.333333333333332em")
                    .setHeight("41.666666666666664em")
                    .setCaption("所有库表")
                    .setImageClass("ood-icon-dialog")
            );

            host.ood_ui_dialog35.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block216")
                    .setDock("fill")
                    .setLeft("1.6666666666666667em")
                    .setTop("10.833333333333334em")
            );

            host.ood_ui_block216.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"tableNames")
                    .setName("tableNames")
                    .setValue("")
            );

            host.ood_ui_block216.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setValue("")
            );

            host.ood_ui_block216.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_block216.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host,"ood_ui_treeview21")
                    .setItems([
                        {
                            "caption":"所有库表",
                            "hidden":false,
                            "id":"all",
                            "initFold":false
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

            host.ood_ui_block216.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"ood_ui_comboinput346")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("18em")
                    .setLabelSize("6em")
                    .setLabelCaption("数据库表")
                    .setType("helpinput")
                    .setImageClass("ood-icon-code")
                    .onChange([
                        {
                            "args":[
                                "{page.getAllTableTrees.invoke()}"
                            ],
                            "desc":"动作 1",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"getAllTableTrees",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block216.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block217")
                    .setDock("bottom")
                    .setLeft("9.166666666666666em")
                    .setTop("30.833333333333332em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block217.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button42")
                    .setLeft("6.583333333333333em")
                    .setTop("0.75em")
                    .setCaption("确定")
                    .setImageClass("ri-check-square-line")
                    .onClick([
                        {
                            "args":[
                                "{page.addTableNames.invoke()}"
                            ],
                            "desc":"动作 3",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"addTableNames",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block217.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button43")
                    .setLeft("14.916666666666666em")
                    .setTop("0.8333333333333334em")
                    .setCaption("关闭")
                    .setImageClass("ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"RAD.db.project.TableSelectTree",
                            "type":"otherModuleCall"
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
