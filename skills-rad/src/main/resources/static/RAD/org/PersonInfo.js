
ood.Class('RAD.org.PersonInfo', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/org/PersonInfo.cls",
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
                    .setRequestDataSource([
                        {
                            "type":"form",
                            "name":"ood_ui_dialog4",
                            "path":""
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setQueryURL("/admin/org/savePerson")
                    .setProxyType("AJAX")
                    .setRequestType("JSON")
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
                            "target":"RAD.org.PersonInfo",
                            "type":"page"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog4")
                    .setLeft("10em")
                    .setTop("5em")
                    .setWidth("35em")
                    .setHeight("27.5em")
                    .setCaption("编辑-人员信息")
            );

            host.ood_ui_dialog4.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block22")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("8.333333333333334em")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Group")
                    .setHost(host,"ood_ui_group1")
                    .setLeft("1.1666666666666667em")
                    .setTop("7em")
                    .setWidth("30.5em")
                    .setHeight("13.333333333333334em")
                    .setCaption("用戶信息")
            );

            host.ood_ui_group1.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"mobile")
                    .setName("mobile")
                    .setLeft("-0.08333333333333333em")
                    .setTop("0.9166666666666666em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("手机号码：")
            );

            host.ood_ui_group1.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"name")
                    .setName("name")
                    .setLeft("-0.08333333333333333em")
                    .setTop("5.916666666666667em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("用户姓名：")
            );

            host.ood_ui_group1.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"email")
                    .setName("email")
                    .setLeft("-0.08333333333333333em")
                    .setTop("3.4166666666666665em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("电子邮件：")
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
                    .setImageClass("ri ri-edit-line")
                    .onClick([
                        {
                            "args":[
                                "{page.api_6.invoke()}"
                            ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
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
                    .setImageClass("ri ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"close",
                            "target":"ood_ui_dialog4",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host," password")
                    .setName("password")
                    .setLeft("2.8333333333333335em")
                    .setTop("3.6666666666666665em")
                    .setWidth("24.333333333333332em")
                    .setLabelSize("8em")
                    .setLabelCaption("登录密码：")
                    .setType("password")
                    .setValue("123456")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"orgId")
                    .setName("orgId")
                    .setLeft("8.666666666666666em")
                    .setTop("3.6666666666666665em")
                    .setWidth("18em")
                    .setVisibility("hidden")
                    .setLabelSize("8em")
                    .setLabelCaption("输入框")
            );

            host.ood_ui_block22.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"account")
                    .setName("account")
                    .setRequired(true)
                    .setLeft("2.8333333333333335em")
                    .setTop("1.1666666666666667em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("登录账号：")
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
