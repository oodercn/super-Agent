
ood.Class('RAD.expression.ExpressionEditor', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/expression/ExpressionEditor.cls",
            "personId":"devdev",
            "personName":"devdev",
            "projectName":"projectManager"
        },
        events:{
            "onRender":{
                "actions":[
                    {
                        "args":[
                            "{ood.showModule2()}",
                            null,
                            null,
                            "RAD.PageEditor",
                            "editor"
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
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog14")
                    .setLeft("0.8333333333333334em")
                    .setTop("3.3333333333333335em")
                    .setWidth("75em")
                    .setHeight("60em")
                    .setCaption("公式编辑器")
                    .setImageClass("ri-code-line")
            );

            host.ood_ui_dialog14.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block65")
                    .setDock("fill")
                    .setLeft("17.5em")
                    .setTop("9.166666666666666em")
            );

            host.ood_ui_block65.append(
                ood.create("ood.UI.ButtonViews")
                    .setHost(host,"ood_ui_buttonviews9")
                    .setItems([
                        {
                            "caption":"手工编写",
                            "hidden":false,
                            "id":"custom",
                            "imageClass":"ri-keyboard-line" // 原"spafont spa-icon-action1"
                        },
                        {
                            "caption":"常用模板库",
                            "hidden":false,
                            "id":"temp",
                            "imageClass":"ri-palette-line" // 原"spafont spa-icon-designview"
                        },
                        {
                            "caption":"常用函数查询",
                            "hidden":false,
                            "id":"fun",
                            "imageClass":"ri-code-line"
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setBarLocation("left")
                    .setBarSize("11.666666666666666em")
                    .setValue("custom")
            );

            host.ood_ui_buttonviews9.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block67")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("10em"),
                "custom"
            );

            host.ood_ui_block67.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block69")
                    .setDock("fill")
                    .setLeft("7.5em")
                    .setTop("28.333333333333332em")
            );

            host.ood_ui_block69.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host,"centerdown")
                    .setName("centerdown")
                    .setItems([
                        {
                            "caption":"数据",
                            "hidden":false,
                            "id":"data",
                            "imageClass":"ri-database-2-line" // 原"spafont spa-icon-c-dateinput"
                        },
                        {
                            "caption":"JSON",
                            "hidden":false,
                            "id":"json",
                            "imageClass":"ri-network-line" // 原"spafont spa-icon-c-treeview"
                        },
                        {
                            "caption":"VIEW",
                            "hidden":false,
                            "id":"veiw",
                            "imageClass":"ri-eye-line" // 原"spafont spa-icon-designview"
                        },
                        {
                            "caption":"日志",
                            "hidden":false,
                            "id":"log",
                            "imageClass":"ri-time-line" // 原"spafont spa-icon-coin"
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setValue("data")
                    .onItemSelected([
                        {
                            "args":[
                                "{ood.showModule2()}",
                                null,
                                null,
                                "RAD.PageEditor",
                                "centerdown",
                                "{args[1].id}"
                            ],
                            "conditions":[
                                {
                                    "symbol":"=",
                                    "right":"json",
                                    "left":"{args[1].id}"
                                }
                            ],
                            "desc":"动作 1",
                            "method":"showModule2",
                            "redirection":"other:callback:call",
                            "target":"url",
                            "type":"other"
                        }
                    ])
            );

            host.ood_ui_block67.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block68")
                    .setDock("top")
                    .setLeft("1.6666666666666667em")
                    .setTop("20em")
                    .setHeight("26.083333333333332em")
            );

            host.ood_ui_block68.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"editor")
                    .setName("editor")
                    .setDock("fill")
                    .setLeft("1.6666666666666667em")
                    .setTop("11.666666666666666em")
            );

            host.ood_ui_block68.append(
                ood.create("ood.UI.MenuBar")
                    .setHost(host,"ood_ui_menubar2")
                    .setItems([
                        {
                            "caption":"文件",
                            "hidden":false,
                            "id":"file"
                        },
                        {
                            "caption":"函数",
                            "hidden":false,
                            "id":"fun",
                            "sub":[
                                {
                                    "caption":"取整",
                                    "hidden":false,
                                    "id":"abs"
                                }
                            ]
                        },
                        {
                            "caption":"集合",
                            "hidden":false,
                            "id":"arr"
                        },
                        {
                            "caption":"调试",
                            "hidden":false,
                            "id":"debug"
                        }
                    ])
            );

            host.ood_ui_dialog14.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block64")
                    .setName("buttongroup")
                    .setDock("bottom")
                    .setLeft("0em")
                    .setTop("29.166666666666668em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block64.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"savebutton")
                    .setName("savebutton")
                    .setLeft("28.666666666666668em")
                    .setTop("0.6666666666666666em")
                    .setCaption("$RAD.widgets.esd.buttonsave")
                    .setImageClass("ood-icon-right")
            );

            host.ood_ui_block64.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"closebutton")
                    .setName("closebutton")
                    .setLeft("39.5em")
                    .setTop("0.6666666666666666em")
                    .setCaption("$RAD.widgets.esd.buttonclose")
                    .setImageClass("ri-close-line")
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
