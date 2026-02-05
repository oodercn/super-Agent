
ood.Class('RAD.db.TableInfo', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/TableInfo.cls",
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
                    .setHost(host,"api_16")
                    .setName("api_16")
                    .setQueryURL("/admin/fdt/magager/AddTable")
                    .setQueryMethod("POST")
                    .setRequestType("JSON")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog11",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([ ])
                    .setResponseCallback([ ])
                    .onData([
                        {
                            "args":[ ],
                            "desc":"动作 2",
                            "koFlag":"_DI_fail",
                            "method":"destroy",
                            "okFlag":"_DI_succeed",
                            "target":"RAD.db.TableInfo",
                            "type":"page"
                        }
                    ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog11")
                    .setLeft("5.833333333333333em")
                    .setTop("9.166666666666666em")
                    .setHeight("16.666666666666668em")
                    .setVisibility("visible")
                    .setResizer(false)
                    .setCaption("添加数据库表")
                    .setImageClass("spafont spa-icon-data")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setModal("true")
            );

            host.ood_ui_dialog11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block33")
                    .setDock("fill")
                    .setLeft("10.833333333333334em")
                    .setTop("13.333333333333334em")
            );

            host.ood_ui_block33.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setValue("")
            );

            host.ood_ui_block33.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"name")
                    .setName("name")
                    .setLeft("1.1666666666666667em")
                    .setTop("2em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("数据库表名：")
                    .setValue("FDT_")
            );

            host.ood_ui_block33.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"cnname")
                    .setName("cnname")
                    .setLeft("1.1666666666666667em")
                    .setTop("5.333333333333333em")
                    .setWidth("18em")
                    .setLabelSize("8em")
                    .setLabelCaption("中文名称：")
            );

            host.ood_ui_block33.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button22")
                    .setLeft("6.166666666666667em")
                    .setTop("9.5em")
                    .setCaption("确认")
                    .setImageClass("ood-icon-right")
                    .onClick([
                        {
                            "args":[
                                "{page.api_16.invoke()}"
                            ],
                            "desc":"动作 3",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
                            "target":"api_16",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block33.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"ood_ui_button23")
                    .setLeft("13.666666666666666em")
                    .setTop("9.5em")
                    .setCaption("取消")
                    .setImageClass("ri-close-line")
                    .onClick([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"RAD.db..TableInfo",
                            "type":"page"
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
