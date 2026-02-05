
ood.Class('RAD.db.ModuleConfig', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/ModuleConfig.cls",
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
                    .setHost(host,"api_3")
                    .setName("api_3")
                    .setQueryURL("/api/fdt/CreateView")
                    .setQueryMethod("POST")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog1",
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
                                "生成成功!"
                            ],
                            "conditions":[
                                {
                                    "symbol":"!=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "koFlag":"_confirm_no",
                            "method":"pop",
                            "okFlag":"_confirm_yes",
                            "target":"msg",
                            "type":"other"
                        }
                    ])
            );

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"api_1")
                    .setName("api_1")
                    .setQueryURL("/api/fdt/CreateModule")
                    .setQueryMethod("POST")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog1",
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
                                "生成失败！"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"alert",
                            "target":"msg",
                            "type":"other"
                        },
                        {
                            "args":[
                                "生成成功!"
                            ],
                            "conditions":[
                                {
                                    "symbol":"!=",
                                    "right":"{-1}",
                                    "left":"{args[1].requestStatus}"
                                }
                            ],
                            "desc":"动作 1",
                            "koFlag":"_confirm_no",
                            "method":"pop",
                            "okFlag":"_confirm_yes",
                            "target":"msg",
                            "type":"other"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog1")
                    .setLeft("10.833333333333334em")
                    .setTop("4.166666666666667em")
                    .setWidth("31.666666666666668em")
                    .setHeight("28.333333333333332em")
                    .setCaption("模板配置")
                    .setImageClass("bpmfont bpmgongzuoliuxitongpeizhi")
                    .setMinBtn(false)
            );

            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div15")
                    .setDock("fill")
                    .setLeft("8.333333333333334em")
                    .setTop("11.666666666666666em")
            );

            host.ood_ui_div15.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div31")
                    .setDock("fill")
                    .setLeft("12.083333333333334em")
                    .setTop("16.666666666666668em")
                    .setWidth("30.833333333333332em")
                    .setHeight("28.75em")
                    .setPosition("")
                    .setPanelBgClr("#FFFFFF")
                    .onRender([
                        {
                            "args":[
                                "{page.getValue()}"
                            ],
                            "desc":"动作 1",
                            "method":"setFormValues",
                            "target":"ood_ui_div31",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.tableName.setUIValue()}",
                                null,
                                null,
                                "{page.properties.tableName}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"defined",
                                    "right":"",
                                    "left":"{page.properties.tableName}"
                                },
                                {
                                    "symbol":"non-empty",
                                    "right":"",
                                    "left":"{page.properties.tableName}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"setUIValue",
                            "redirection":"other:callback:call",
                            "target":"tableName",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div32")
                    .setDock("bottom")
                    .setLeft("7em")
                    .setTop("17.166666666666668em")
                    .setHeight("3.5833333333333335em")
            );

            host.ood_ui_div32.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button31")
                    .setIsFormField(false)
                    .setLeft("17.916666666666668em")
                    .setTop("0.8333333333333334em")
                    .setWidth("6.833333333333333em")
                    .setCaption("取消")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"ood_ui_dialog1",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_div32.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button16")
                    .setIsFormField(false)
                    .setLeft("7.083333333333333em")
                    .setTop("0.8333333333333334em")
                    .setWidth("8.5em")
                    .setCaption("生成代码")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "target":"api_1",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.List")
                    .setHost(host,"ood_ui_list8")
                    .setName("config")
                    .setItems([
                        {
                            "caption":"DAO,VO",
                            "hidden":false,
                            "id":"dao",
                            "imageClass":"ri-hashtag"
                        },
                        {
                            "caption":"UI(From、List)",
                            "hidden":false,
                            "id":"ui",
                            "imageClass":"ri-list-check"
                        },
                        {
                            "caption":"公共组件",
                            "hidden":false,
                            "id":"lib",
                            "imageClass":"ri-network-line"
                        }
                    ])
                    .setLeft("-1.25em")
                    .setTop("13.75em")
                    .setWidth("27.666666666666668em")
                    .setHeight("7.5em")
                    .setSelMode("multibycheckbox")
                    .setLabelSize("8em")
                    .setLabelCaption("代码生成：")
                    .setValue("lib;ui")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"tableName")
                    .setName("tableName")
                    .setLeft("-1.25em")
                    .setTop("0.4166666666666667em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("表名：")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"ood_ui_input19")
                    .setName("packageName")
                    .setLeft("-1.25em")
                    .setTop("2.9166666666666665em")
                    .setWidth("24.833333333333332em")
                    .setPosition("relative")
                    .setLabelSize("8em")
                    .setLabelCaption("包路径：")
                    .setValue("net.itjds.fdt.dao")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"ood_ui_input20")
                    .setName("schema")
                    .setLeft("-1.25em")
                    .setTop("3.5833333333333335em")
                    .setWidth("24.833333333333332em")
                    .setPosition("relative")
                    .setLabelSize("8em")
                    .setLabelCaption("数据库l链接：")
                    .setValue("bpm")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"ood_ui_input21")
                    .setName("vfsPath")
                    .setLeft("5.416666666666667em")
                    .setTop("37.083333333333336em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("VFS路径：")
                    .setValue("bsi/")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"ood_ui_comboinput126")
                    .setName("tempPath")
                    .setLeft("-1.25em")
                    .setTop("11.25em")
                    .setWidth("23.833333333333332em")
                    .setLabelSize("8em")
                    .setLabelCaption("模板库：")
                    .setItems([
                        {
                            "caption":"temp/default",
                            "hidden":false,
                            "id":"default",
                            "imageClass":"ood-icon-number1"
                        },
                        {
                            "caption":"temp/opennew",
                            "hidden":false,
                            "id":"at"
                        },
                        {
                            "caption":"temp/info",
                            "hidden":false,
                            "id":"au"
                        }
                    ])
                    .setValue("default")
            );

            host.ood_ui_div31.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"ood_ui_comboinput316")
                    .setName("publicPath")
                    .setLeft("-1.25em")
                    .setTop("7.916666666666667em")
                    .setWidth("24.666666666666668em")
                    .setLabelSize("8em")
                    .setLabelCaption("发布目录：")
                    .setType("cmdbox")
                    .onClick([
                        {
                            "args":[
                                "{page.popUp()}",
                                null,
                                null,
                                null,
                                null,
                                "{page.ood_ui_dialog1}"
                            ],
                            "desc":"动作 1",
                            "koFlag":"_DI_fail",
                            "method":"popUp",
                            "okFlag":"_DI_succeed",
                            "redirection":"page::",
                            "target":"App.dbmanager.selectPath",
                            "type":"page"
                        }
                    ])
            );

            append(
                ood.create("ood.MessageService")
                    .setHost(host,"selectPathMsg")
                    .setName("selectPathMsg")
                    .setDesc("selectPathMsg")
                    .setMsgType("selectPath")
                    .onMessageReceived([
                        {
                            "args":[
                                "{page.ood_ui_comboinput316.setUIValue()}",
                                null,
                                null,
                                "{args[1]}"
                            ],
                            "desc":"动作 1",
                            "method":"setUIValue",
                            "redirection":"other:callback:call",
                            "target":"ood_ui_comboinput316",
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
