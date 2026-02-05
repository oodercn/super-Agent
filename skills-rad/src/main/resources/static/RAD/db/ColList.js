
ood.Class('RAD.db.ColList', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "autoDestroy":true,
            "path":"form/myspace/versionspace/projectManager/0/RAD/db/ColList.cls",
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
                    .setHost(host,"GetAllCollByTableNameAjax")
                    .setName("GetAllCollByTableNameAjax")
                    .setAutoRun(true)
                    .setQueryURL("/admin/fdt/magager/GetAllCollByTableName")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_pagebar8",
                            "path":"",
                            "type":"pagebar"
                        },
                        {
                            "name":"ood_ui_block46",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"addTable",
                            "path":"data",
                            "type":"treegrid"
                        },
                        {
                            "name":"ood_ui_pagebar8",
                            "path":"size",
                            "type":"pagebar"
                        }
                    ])
                    .setResponseCallback([ ])
                    .setProxyType("AJAX")
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_panel24")
                    .setDock("fill")
            );

            host.ood_ui_panel24.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block46")
                    .setDock("fill")
                    .setLeft("5.833333333333333em")
                    .setTop("5em")
                    .setWidth("46.666666666666664em")
                    .setHeight("35.833333333333336em")
            );

            host.ood_ui_block46.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"configKey")
                    .setName("configKey")
                    .setValue("")
            );

            host.ood_ui_block46.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"tablename")
                    .setName("tablename")
                    .setValue("")
            );

            host.ood_ui_block46.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block47")
                    .setDock("bottom")
                    .setLeft("19.166666666666668em")
                    .setTop("45em")
                    .setHeight("3.25em")
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host,"ood_ui_pagebar8")
                    .setLeft("0.5833333333333334em")
                    .setTop("0.8333333333333334em")
                    .setCaption("翻页")
            );

            host.ood_ui_block46.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"ood_ui_toolbar10")
                    .setItems([
                        {
                            "caption":"toolbar",
                            "hidden":false,
                            "id":"toolbar",
                            "sub":[
                                {
                                    "caption":"增加",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"add",
                                    "imageClass":"iconfont iconicon_tianjia",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"删除",
                                    "hidden":true,
                                    "iconFontSize":"",
                                    "id":"delete",
                                    "imageClass":"ri-close-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"刷新",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ood-refresh",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
                    .onClick([
                        {
                            "args":[
                                "{page.GetAllCollByTableNameAjax.invoke()}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"reload",
                                    "left":"{args[1].id}"
                                }
                            ],
                            "desc":"动作 5",
                            "method":"invoke",
                            "redirection":"other:callback:call",
                            "return":false,
                            "target":"GetAllCollByTableNameAjax",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{page.getData()}",
                                "{page}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"add",
                                    "left":"{args[5]}"
                                }
                            ],
                            "desc":"动作 2",
                            "method":"show2",
                            "redirection":"page",
                            "target":"RAD.db.ColInfo",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_block46.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"addTable")
                    .setName("addTable")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setRowNumbered(true)
                    .setEditable(true)
                    .setHeader([
                        {
                            "caption":"表名",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"tablename",
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"中文注解",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"cnname",
                            "type":"label",
                            "width":"16em"
                        },
                        {
                            "caption":"字段名称",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"name",
                            "col":"label",
                            "width":"16em"
                        },
                        {
                            "caption":"字段类型",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"colType",
                            "colType":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"字段长度",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"length",
                            "type":"label",
                            "width":"8em"
                        },
                        {
                            "caption":"JAVA名称",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"fieldname",
                            "type":"label",
                            "width":"12em"
                        }
                    ])
                    .setUidColumn("tablename")
                    .setTreeMode("none")
                    .setValue("")
                    .onRender([ ])
                    .onDblclickRow([
                        {
                            "args":[
                                "{page.show2()}",
                                null,
                                null,
                                null,
                                null,
                                null,
                                "{args[1]}",
                                "{page}"
                            ],
                            "desc":"动作 6",
                            "method":"show2",
                            "redirection":"page",
                            "target":"RAD.db.ColInfo",
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
