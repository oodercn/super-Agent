
ood.Class('RAD.server.ProxyInfo', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/server/ProxyInfo.cls",
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
                    .setHost(host,"api_20")
                    .setName("api_20")
                    .setQueryURL("/admin/proxyhost/updateProxyHosts")
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
                            "method":"destroy",
                            "target":"ood_ui_dialog17",
                            "type":"control"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog17")
                    .setLeft("7.5em")
                    .setTop("5.833333333333333em")
                    .setWidth("31.666666666666668em")
                    .setHeight("23.333333333333332em")
                    .setCaption("编辑代理服务")
                    .setImageClass("ood-icon-dragmove")
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
                    .setHost(host,"proxyId")
                    .setName("proxyId")
                    .setValue("")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"proxyUrl")
                    .setName("proxyUrl")
                    .setLeft("-2.1666666666666665em")
                    .setTop("5.333333333333333em")
                    .setWidth("30.083333333333332em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("代理地址")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"pttern")
                    .setName("pttern")
                    .setLeft("-2.1666666666666665em")
                    .setTop("8.666666666666666em")
                    .setWidth("30.083333333333332em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("过滤规则")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"host")
                    .setName("host")
                    .setLeft("-2.1666666666666665em")
                    .setTop("2em")
                    .setWidth("30.083333333333332em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("域名：")
            );

            host.ood_ui_div140.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"sessionId")
                    .setName("sessionId")
                    .setLeft("-2.1666666666666665em")
                    .setTop("12em")
                    .setWidth("30.083333333333332em")
                    .setHeight("2.6666666666666665em")
                    .setLabelSize("8em")
                    .setLabelCaption("sessionId")
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
                    .setImageClass("ood-icon-right")
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
                            "desc":"动作 1",
                            "type":"page",
                            "target":"RAD.server.ProxyInfo",
                            "args":[ ],
                            "method":"destroy"
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
