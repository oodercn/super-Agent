
ood.Class('RAD.expression.SimpleExpressionEditor', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[
            "ood.Module.JSONEditor"
        ],
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
                        "args":[],
                        "desc":"函数",
                        "script":"_page_onrender",
                        "type":"page"
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
                    .setHost(host,"ood_ui_dialog6")
                    .setLeft("10em")
                    .setTop("0.8333333333333334em")
                    .setWidth("70.83333333333333em")
                    .setHeight("49.166666666666664em")
                    .setCaption("公式编辑器")
            );

            host.ood_ui_dialog6.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"content")
                    .setDock("fill")
                    .setBorderType("none")
            );

            host.content.append(
                ood.create("ood.UI.Layout")
                    .setHost(host,"ood_ui_layout19")
                    .setType("vertical")
                    .setItems([
                        {
                            "cmd":true,
                            "folded":false,
                            "id":"before",
                            "locked":false,
                            "min":300,
                            "pos":"before",
                            "size":350,
                            "hidden":false
                        },
                        {
                            "id":"main",
                            "min":10,
                            "size":200
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_layout19.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host,"ood_ui_tabs11")
                    .setName("centerdown")
                    .setItems([
                        {
                            "id":"result",
                            "caption":"结果",
                            "imageClass":"spafont spa-icon-designview",
                            "hidden":false
                        },
                        {
                            "id":"log",
                            "caption":"日志",
                            "imageClass":"spafont spa-icon-coin",
                            "hidden":false
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setValue("result"),
                "main"
            );

            host.ood_ui_tabs11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_group13")
                    .setDock("fill")
                    .setConLayoutColumns(2),
                "log"
            );

            host.ood_ui_tabs11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"resultgrid")
                    .setName("resultgrid")
                    .setDock("fill")
                    .setLeft("15em")
                    .setTop("10em"),
                "result"
            );

            host.resultgrid.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block47")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block48")
                    .setDock("bottom")
                    .setHeight("2.5em")
            );

            host.ood_ui_block48.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host,"ood_ui_pagebar1")
                    .setCaption("")
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"result")
                    .setName("result")
                    .setLeft("0em")
                    .setTop("0em")
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption":"col1",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"a",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col2",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"b",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col3",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"c",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col4",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"d",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        }
                    ])
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"ood_ui_toolbar16")
                    .setItems([
                        {
                            "caption":"common",
                            "hidden":false,
                            "id":"common",
                            "sub":[
                                {
                                    "caption":"$RAD.widgets.esd.buttonnew",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"new",
                                    "imageClass":"ri-calendar-event-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"$RAD.widgets.esd.buttondelete",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"delete",
                                    "imageClass":"ri-close-line",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"$RAD.widgets.esd.buttonreload",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ri-loader-line",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
            );

            host.ood_ui_layout19.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block63")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("5.833333333333333em"),
                "before"
            );

            host.ood_ui_block63.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"editor")
                    .setName("editor")
                    .setDock("fill")
                    .setLeft("2.5em")
                    .setTop("12.5em")
            );

            host.editor.append(
                ood.create("ood.UI.MenuBar")
                    .setHost(host,"ood_ui_menubar2")
                    .setItems([
                        {
                            "caption":"数据源",
                            "disabled":false,
                            "hidden":false,
                            "id":"data",
                            "sub":[
                                {
                                    "caption":"JSON文件",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadJson"
                                },
                                {
                                    "caption":"Execl文件",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadExecl"
                                },
                                {
                                    "caption":"数据库",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadDb"
                                }
                            ]
                        },
                        {
                            "caption":"公式",
                            "disabled":false,
                            "hidden":false,
                            "id":"editor"
                        },
                        {
                            "caption":"模板",
                            "disabled":false,
                            "hidden":false,
                            "id":"temp"
                        },
                        {
                            "caption":"工具",
                            "disabled":false,
                            "hidden":false,
                            "id":"tool"
                        }
                    ])
                    .setTop("2.5em")
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        },

        getExpression:function () {
            var ns = this;
            return ns.editor.getValue();
        },

        setResult:function (data) {
            var ns = this;
            ns.ood_ui_tabs11.fireItemClickEvent('result');
            ns.resultgrid.setChildren([data]);
        },

        _page_onrender:function (module, threadid) {
            var ns = this;
            ood.showModule3("RAD.expression.PageEditor", "editor", null, function () {
                this.setDataToEditor(ns.$host, ns.$cls, ns.expression);

                this.setEvents("onRunExpression", function (profile,expression) {
                    var data=  ood.execGrid(expression, {}, false);
                    ns.setResult(data);
                });
                this.setEvents("onValueChanged", function (profile,expression) {
                    ns.fireEvent("onchange", [this,expression]);

                });

            })
        },

        setDataToEditor:function (host, cls, expression) {
            var ns = this;
            this.$cls = cls;
            this.$host = host;
            this.expression = expression;
        },

        _ontrows:function (profile, row, e, src, type) {
            //if (type == -1)
            //   profile.boxing().updateCell(row.cells[1], {value: ""}, false, false);
            this.updateRequestData();
        },
        _tg_beforerowactive:function () {
            return false;
        },
        _callCmd:function(expression) {
            return ood.execGrid(expression, {}, false);
        }  } ,
    Static:{
        "designViewConf":{
            "height":1024,
            "mobileFrame":false,
            "width":1280
        },
        viewStyles:{ }
    }



});
