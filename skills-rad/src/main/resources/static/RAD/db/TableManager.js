
ood.Class('RAD.db.TableManager', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[
            "RAD.db.ColList"
        ],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/TableManager.cls",
            "personId":"devdev",
            "personName":"系统管理员",
            "projectName":"projectManager"
        },
        events:{
            "afterShow":{
                "actions":[
                    {
                        "args":[
                            "{ood.showModule2()}",
                            null,
                            null,
                            "RAD.db.TableList",
                            "tableLayout",
                            "main",
                            null,
                            "{page.getData()}"
                        ],
                        "desc":"动作 1",
                        "method":"showModule2",
                        "redirection":"other:callback:call",
                        "target":"url",
                        "type":"other"
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
                    .setHost(host,"main-tree-api")
                    .setName("main-tree-api")
                    .setAutoRun(true)
                    .setQueryURL("/admin/fdt/magager/getAllTableTrees")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"dbwork",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview12",
                            "path":"data",
                            "type":"treeview"
                        }
                    ])
                    .setResponseCallback([ ])
                    .setProxyType("AJAX")
                    .onData([ ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"dbwork")
                    .setName("dbwork")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.dbwork.append(
                ood.create("ood.UI.Layout")
                    .setHost(host,"tableLayout")
                    .setName("tableLayout")
                    .setItems([
                        {
                            "cmd":true,
                            "folded":false,
                            "id":"left",
                            "locked":false,
                            "min":10,
                            "pos":"before",
                            "size":200,
                            "hidden":false
                        },
                        {
                            "id":"main",
                            "min":10,
                            "size":80
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setType("horizontal")
            );

            host.tableLayout.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"content")
                    .setName("content")
                    .setDock("fill")
                    .setLeft("15.833333333333334em")
                    .setTop("13.333333333333334em")
                    .onRender([ ]),
                "main"
            );

            host.tableLayout.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host,"ood_ui_treeview12")
                    .setName("menuTree")
                    .setItems([
                        {
                            "caption":"数据库管理",
                            "hidden":false,
                            "id":"service",
                            "imageClass":"ri-building-line",
                            "initFold":false
                        },
                        {
                            "caption":"数据库表",
                            "hidden":false,
                            "id":"all",
                            "imageClass":"iconfont iconchucun"
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setInitFold(false)
                    .setSelMode("none")
                    .setSingleOpen(true)
                    .setValue("a")
                    .onItemSelected([
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                "tableLayout",
                                "main",
                                "{args[1]}",
                                "{page.getData()}",
                                ""
                            ],

                            "conditions":[
                                {
                                    "symbol":"defined",
                                    "right":"",
                                    "left":"{args[1].tablename}"
                                }
                            ],
                            "desc":"动作 1",
                            "method":"show2",
                            "redirection":"page",
                            "target":"RAD.db.ColList",
                            "type":"page"
                        },
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                "tableLayout",
                                "main",
                                null,
                                "{args[1]}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"service",
                                    "left":"{args[1].id}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"show2",
                            "redirection":"page",
                            "return":false,
                            "target":"RAD.db.TableList",
                            "type":"page"
                        }
                    ]),
                "left"
            );

            host.dbwork.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block14")
                    .setDock("bottom")
                    .setLeft("19.083333333333332em")
                    .setTop("12.416666666666666em")
                    .setWidth("0em")
                    .setHeight("0em")
                    .setVisibility("hidden")
            );

            host.ood_ui_block14.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setValue("")
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
