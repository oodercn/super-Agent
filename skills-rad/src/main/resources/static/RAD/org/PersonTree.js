
ood.Class('RAD.org.PersonTree', 'ood.Module',{
    Instance:{
        initialize : function(){
        },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/org/PersonTree.cls",
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
                    .setHost(host,"api_96")
                    .setName("api_96")
                    .setAutoRun(true)
                    .setQueryURL("/admin/org/getPersonTree")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog53",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview27",
                            "path":"data",
                            "type":"treeview"
                        }
                    ])
                    .setResponseCallback([ ])
                    .onData({
                        "actions":[
                            {
                                "args":[
                                    "{page.lefttree.setUIValue()}",
                                    null,
                                    null,
                                    "{page.parentId.getValue()}"
                                ],
                                "desc":"动作 1",
                                "method":"call",
                                "target":"callback",
                                "type":"other"
                            }
                        ]
                    })
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"api_46")
                    .setRequestDataSource([
                        {
                            "type":"treeview",
                            "name":"ood_ui_treeview27",
                            "path":""
                        },
                        {
                            "type":"form",
                            "name":"ood_ui_dialog53",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "type":"treeview",
                            "name":"ood_ui_treeview27",
                            "path":"data"
                        }
                    ])
                    .setResponseCallback([ ])
                    .setQueryURL("/admin/org/addDevPersons")
                    .setQueryMethod("POST")
                    .onData({
                        "actions":[
                            {
                                "args":[ ],
                                "desc":"动作 1",
                                "method":"destroy",
                                "target":"RAD.org.PersonTree"
                            }
                        ]
                    })
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog53")
                    .setLeft("10.833333333333334em")
                    .setTop("6.666666666666667em")
                    .setHeight("37.5em")
                    .setCaption("选择人员")
            );

            host.ood_ui_dialog53.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_dialog53.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"group")
                    .setName("group")
                    .setValue("")
            );

            host.ood_ui_dialog53.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host,"ood_ui_treeview27")
                    .setName("lefttree")
                    .setItems([
                        {
                            "caption":"组织机构",
                            "disabled":true,
                            "hidden":false,
                            "id":"root",
                            "initFold":false
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setInitFold(false)
                    .setSelMode("multibycheckbox")
                    .setOptBtn("ood-icon-singleright")
                    .setTogglePlaceholder(true)
                    .setValue("")
            );

            host.ood_ui_dialog53.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block249")
                    .setDock("bottom")
                    .setLeft("7.5em")
                    .setTop("18.333333333333332em")
                    .setHeight("3.5833333333333335em")
            );

            host.ood_ui_block249.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button38")
                    .setLeft("8.333333333333334em")
                    .setTop("0.8333333333333334em")
                    .setCaption("确定 ")
                    .setImageClass("iconfont iconduigoux")
                    .onClick([
                        {
                            "args":[
                                "{page.api_46.invoke()}"
                            ],
                            "desc":"动作 2",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"api_46",
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
    Static:{
        "designViewConf":{
            "height":1024,
            "mobileFrame":false,
            "width":1280
        }
    }
});
