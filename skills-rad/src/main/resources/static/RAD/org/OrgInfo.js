
ood.Class('RAD.org.OrgInfo', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/org/OrgInfo.cls",
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
                    .setHost(host,"api_6")
                    .setName("api_6")
                    .setQueryURL("/admin/org/saveOrg")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog4",
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
                                "{args[1].errDes}",
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
                            "method":"alert",
                            "return":false,
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[ ],
                            "desc":"动作 4",
                            "method":"destroy",
                            "target":"ood_ui_dialog4",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog4")
                    .setLeft("21.666666666666668em")
                    .setTop("5.833333333333333em")
                    .setWidth("35em")
                    .setHeight("26.666666666666668em")
                    .setCaption("编辑-机构信息")
            );

            host.ood_ui_dialog4.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block22")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("8.333333333333334em")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"iD")
                    .setName("iD")
                    .setValue("")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"leaderId")
                    .setName("leaderId")
                    .setValue("")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"brief")
                    .setName("brief")
                    .setLeft("2em")
                    .setTop("7.833333333333333em")
                    .setWidth("25.5em")
                    .setHeight("10em")
                    .setLabelSize("8em")
                    .setLabelCaption("备               注：")
                    .setMultiLines(true)
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"leaderName")
                    .setName("leaderName")
                    .setLeft("2.8333333333333335em")
                    .setTop("4.5em")
                    .setWidth("23.833333333333332em")
                    .setVisibility("hidden")
                    .setLabelSize("8em")
                    .setLabelCaption("设定管理员：")
                    .setType("getter")
                    .onClick([
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{page.getData()}"
                            ],
                            "desc":"动作 1",
                            "method":"show2",
                            "redirection":"page",
                            "target":"RAD.org.PersonTree",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block23")
                    .setDock("bottom")
                    .setLeft("10em")
                    .setTop("26.666666666666668em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block23.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button5")
                    .setLeft("8.333333333333334em")
                    .setTop("0.75em")
                    .setCaption("保存")
                    .setImageClass("ri-edit-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"api_6",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block23.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button6")
                    .setLeft("19.166666666666668em")
                    .setTop("0.75em")
                    .setCaption("取消")
                    .setImageClass("ri-close-line")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"name")
                    .setName("name")
                    .setLeft("2.8333333333333335em")
                    .setTop("1.1666666666666667em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("机构名称：")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"orgId")
                    .setName("parentId")
                    .setLeft("1.1666666666666667em")
                    .setTop("17em")
                    .setWidth("24.666666666666668em")
                    .setVisibility("hidden")
                    .setLabelSize("8em")
                    .setLabelCaption("parentId")
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
