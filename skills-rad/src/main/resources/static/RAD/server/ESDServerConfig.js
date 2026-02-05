
ood.Class('RAD.server.ESDServerConfig', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/server/ESDServerConfig.cls",
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
                    .setHost(host,"api_20")
                    .setName("api_20")
                    .setQueryURL("/admin/proxyhost/updateLocalServer")
                    .setQueryMethod("POST")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_block59",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .setProxyType("AJAX")
                    .onData([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"close",
                            "target":"ood_ui_dialog17",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"PAGECTX")
                    .setVisibility("hidden")
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog17")
                    .setLeft("7.5em")
                    .setTop("5.833333333333333em")
                    .setWidth("36.666666666666664em")
                    .setHeight("26.666666666666668em")
                    .setCaption("配置本地服务器")
                    .setImageClass("ri-arrow-left-right-line")
                    .beforeClose([
                        {
                            "args":[
                                "{page.reloadParent()}"
                            ],
                            "desc":"动作 1",
                            "method":"call",
                            "target":"callback",
                            "type":"other"
                        },
                        {
                            "args":[ ],
                            "desc":"动作 2",
                            "method":"destroy",
                            "target":"RAD.server.ESDServerConfig",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_dialog17.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block59")
                    .setDock("fill")
                    .setLeft("12.5em")
                    .setTop("7.5em")
            );

            host.ood_ui_block59.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div140")
                    .setDock("top")
                    .setLeft("19.166666666666668em")
                    .setTop("6.666666666666667em")
                    .setHeight("15.166666666666666em")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"serverId")
                    .setName("serverId")
                    .setValue("")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"configfile")
                    .setName("configfile")
                    .setLeft("12.833333333333334em")
                    .setTop("4.5em")
                    .setWidth("15.083333333333334em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("6em")
                    .setLabelCaption("启动文件：")
                    .setValue("server.properties")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"proxyUrl")
                    .setName("proxyUrl")
                    .setLeft("-1.3333333333333333em")
                    .setTop("7.833333333333333em")
                    .setWidth("29.416666666666668em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("服务代理：")
                    .setMultiLines(true)
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"strartScript")
                    .setName("strartScript")
                    .setLeft("-1.3333333333333333em")
                    .setTop("14.5em")
                    .setWidth("29.666666666666668em")
                    .setHeight("4.333333333333333em")
                    .setLabelSize("8em")
                    .setLabelCaption("启动脚本")
                    .setMultiLines(true)
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"proxyPort")
                    .setName("proxyPort")
                    .setLeft("1.1666666666666667em")
                    .setTop("4.5em")
                    .setWidth("10.083333333333334em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("6em")
                    .setLabelCaption("端口：")
                    .setValue("80")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"path")
                    .setName("path")
                    .setLeft("-1.3333333333333333em")
                    .setTop("11.166666666666666em")
                    .setWidth("29.416666666666668em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("本地路径：")
                    .setMultiLines(true)
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"name")
                    .setName("name")
                    .setLeft("1.1666666666666667em")
                    .setTop("1.1666666666666667em")
                    .setWidth("26.75em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("6em")
                    .setLabelCaption("名称：")
                    .setValue("LocalServer")
            );

            host.ood_ui_block59.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block112")
                    .setName("buttongroup")
                    .setDock("bottom")
                    .setLeft("0em")
                    .setTop("27.5em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block112.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"savebutton")
                    .setName("savebutton")
                    .setLeft("9.083333333333334em")
                    .setTop("0.75em")
                    .setCaption("保存")
                    .setImageClass("ri-arrow-right-line")
                    .onClick([
                        {
                            "args":[
                                "{page.api_20.invoke()}"
                            ],
                            "desc":"动作 1",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "target":"api_20",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block112.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"closebutton")
                    .setName("closebutton")
                    .setLeft("17.416666666666668em")
                    .setTop("0.75em")
                    .setCaption("关闭")
                    .setImageClass("ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"close",
                            "target":"ood_ui_dialog17",
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
            "height":600,
            "mobileFrame":false,
            "width":800
        }
    }



});
