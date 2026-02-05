
ood.Class('RAD.expression.ExpressionTemp', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/expression/ExpressionTemp.cls",
            "personId":"devdev",
            "personName":"devdev",
            "projectName":"projectManager"
        },
        events:{
            "afterShow":  "_page_onrender"
        },
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.APICaller")
                    .setHost(host,"loadapi")
                    .setName("loadapi")
                    .setAutoRun(true)
                    .setQueryURL("/admin/el/getServcieByType")
                    .setQueryMethod("POST")
                    .setRequestDataSource([
                        {
                            "name":"ood_ui_dialog9",
                            "path":"",
                            "type":"form"
                        }
                    ])
                    .setResponseDataTarget([
                        {
                            "name":"ood_ui_treeview19",
                            "path":"data",
                            "type":"treeview"
                        }
                    ])
                    .setResponseCallback([ ])
            );

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host,"ood_ui_dialog9")
                    .setLeft("1.6666666666666667em")
                    .setTop("3.3333333333333335em")
                    .setWidth("84.16666666666667em")
                    .setHeight("50.833333333333336em")
                    .setCaption("MIX代码编辑器")
                    .setImageClass("ri-code-line")
            );

            host.ood_ui_dialog9.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"className")
                    .setName("className")
                    .setValue("")
            );

            host.ood_ui_dialog9.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block49")
                    .setName("buttongroup")
                    .setDock("bottom")
                    .setLeft("0em")
                    .setTop("0em")
                    .setHeight("3.5em")
            );

            host.ood_ui_block49.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"savebutton")
                    .setName("savebutton")
                    .setLeft("32.416666666666664em")
                    .setTop("0.75em")
                    .setCaption("$RAD.widgets.esd.buttonsave")
                    .setImageClass("ood-icon-right")
            );

            host.ood_ui_block49.append(
                ood.create("ood.UI.Button")
                    .setHost(host,"closebutton")
                    .setName("closebutton")
                    .setLeft("42.416666666666664em")
                    .setTop("0.75em")
                    .setCaption("$RAD.widgets.esd.buttonclose")
                    .setImageClass("ri-close-line")
                    .onChange([
                        {
                            "args":[ ],
                            "desc":"动作 1",
                            "method":"destroy",
                            "target":"RAD.expression.ExpressionTemp",
                            "type":"page"
                        }
                    ])
            );

            host.ood_ui_dialog9.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"resourcemain")
                    .setName("resourcemain")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.resourcemain.append(
                ood.create("ood.UI.Layout")
                    .setHost(host,"resourcelayout")
                    .setName("resourcelayout")
                    .setItems([
                        {
                            "cmd":true,
                            "folded":false,
                            "id":"left",
                            "locked":false,
                            "min":10,
                            "pos":"before",
                            "size":200,
                            "hidden":false
                        },
                        {
                            "id":"main",
                            "min":10,
                            "size":80
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setType("horizontal")
            );

            host.resourcelayout.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"content")
                    .setName("content")
                    .setBorderType("none")
                    .setDock("fill")
                    .setLeft("15.833333333333334em")
                    .setTop("13.333333333333334em"),
                "main"
            );

            host.resourcelayout.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block60")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em"),
                "left"
            );

            host.ood_ui_block60.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host,"projectName")
                    .setName("projectName")
                    .setValue("")
            );

            host.ood_ui_block60.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host,"pattern")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("18em")
                    .setLabelSize("6em")
                    .setLabelCaption("Expression:")
                    .setType("helpinput")
                    .setImageClass("ood-icon-code")
                    .onChange([
                        {
                            "args":[
                                "{page.loadapi.setQueryData()}",
                                null,
                                null,
                                "{args[2]}",
                                "pattern"
                            ],
                            "desc":"动作 1",
                            "method":"setQueryData",
                            "redirection":"other:callback:call",
                            "target":"loadapi",
                            "type":"control"
                        },
                        {
                            "args":[
                                "{page.loadapi.invoke()}",
                                null,
                                null,
                                null,
                                null,
                                ""
                            ],
                            "desc":"动作 2",
                            "koFlag":"_DI_fail",
                            "method":"invoke",
                            "okFlag":"_DI_succeed",
                            "redirection":"other:callback:call",
                            "target":"loadapi",
                            "type":"control"
                        }
                    ])
            );

            host.ood_ui_block60.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"servicetool")
                    .setItems([
                        {
                            "caption":"any",
                            "hidden":false,
                            "id":"any",
                            "sub":[
                                {
                                    "caption":"",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"refresh",
                                    "imageClass":"ri-refresh-line",
                                    "position":"absolute",
                                    "tips":"$RAD.tool2.refresh"
                                },
                                {
                                    "caption":"",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"import",
                                    "imageClass":"ood-uicmd-add",
                                    "position":"absolute",
                                    "tips":"$RAD.toolbox.importapi"
                                }
                            ]
                        }
                    ])
                    .onClick([
                        "_toolbar_onclick"
                    ])
                    .setCustomStyle({
                        "ITEMS":{
                            "border-top":"0",
                            "border-bottom":"0"
                        }
                    })
            );

            host.ood_ui_block60.append(
                ood.create("ood.UI.TreeBar")
                    .setHost(host,"treebarCom")
                    .setSelMode("none")
                    .onChange([
                        "_ood_ui_treeview19_onchange"
                    ])
                    .onGetContent([
                        "_treebarcom_ongetcontent"
                    ])
                    .onItemSelected([
                        "_ood_ui_treeview19_onitemselected"
                    ])
                    .setCustomStyle({
                        "KEY":{
                            "background-color":"#FFFFFF"
                        }
                    })
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        },

        _reload:function (profile, item) {
            ns = this;
            ns.treebarCom.setItems(ns._buildItems(this.topNodes));
            ns.treebarCom.fireItemClickEvent('pageService');

        },
        _ood_ui_treeview19_onitemselected:function (profile, item, e, src, type) {
            var ns = this, uictrl = profile.boxing(),
                prop = ns.properties, callback = function () {

                };
            ood.getModule("RAD.expression.ExpressionEditor").setDataToEditor(ns.$host , ns.$cls, item);
            // ood.showModule3("RAD.expression.ExpressionEditor","content",null,function () {
            //     this.setDataToEditor(ns.$host , ns.$cls, item);
            //     this.setEvents("onValueChanged", function (profile,expression) {
            //         ns.$api.queryArgs.expression=expression;
            //         ns.fireEvent("onchange", [this,expression]);
            //     });
            //
            // });

            // ood.getModule("RAD.expression.ExpressionEditor", function () {
            //
            //     this.setDataToEditor(ns.$host , ns.$cls, item);
            //     this.setEvents("onValueChanged", function (profile,expression) {
            //         ns.$api.queryArgs.expression=expression;
            //         ns.fireEvent("onchange", [this,expression]);
            //     });
            //
            //     //this.show();
            // });
        },
        _treebarcom_ongetcontent:function (profile, item, callback) {
            var ns = this;
            if (item.key == "jds.localService") {
                ood.Ajax(CONF.getLocalServiceToolBoxService, {
                    projectName: SPA.curProjectName,
                    pattern: ns.pattern.getUIValue()
                }, function (txt) {
                    var obj = txt;
                    if (!obj || obj.error) {
                        ood.message("No module in this project!");
                        callback(false);
                    } else {

                        callback(ns._buildItems(obj.data));
                    }
                }, function (txt) {
                    callback(false);
                }, null, {method: 'post'}).start();
            } else if (item.key == "jds.getPageServiceToolBoxService") {
                ood.Ajax(CONF.getPageServiceToolBoxService, {
                    projectName: SPA.curProjectName,
                    className:SPA.getCurrentClassName(),
                    pattern: ns.pattern.getUIValue()
                }, function (txt) {
                    var obj = txt;
                    if (!obj || obj.error) {
                        ood.message("No module in this project!");
                        callback(false);
                    } else {
                        callback(ns._buildItems(obj.data));
                    }
                }, function (txt) {
                    callback(false);
                }, null, {method: 'post'}).start();
            } else if (item.key == "jds.getProjectServiceToolBoxService") {
                ood.Ajax(CONF.getProjectServiceToolBoxService, {
                    projectName: SPA.curProjectName,
                    pattern: ns.pattern.getUIValue()
                }, function (txt) {
                    var obj = txt;
                    if (!obj || obj.error) {
                        ood.message("No module in this project!");
                        callback(false);
                    } else {
                        var items = [];

                        callback(ns._buildItems(obj.data));
                        //callback(obj.data);
                    }
                }, function (txt) {
                    callback(false);
                }, null, {method: 'post'}).start();
            } else if (item.key == "jds.Server") {
                ood.Ajax(CONF.getRemoteServiceByKeyService, {
                    projectName: SPA.curProjectName,
                    pattern: ns.pattern.getUIValue(),
                    serverId: item.id
                }, function (txt) {

                    var obj = txt;
                    if (!obj || obj.error) {
                        ood.message("No service in this project!");
                        callback(false);
                    } else {
                        var items = [];
                        callback(obj.data);
                    }
                }, function (txt) {
                    callback(false);
                }, null, {method: 'post'}).start();
            }


        },
        _toolbar_onclick:function (profile, item, group, e, src) {
            var ns = this;
            switch (item.id) {
                case 'refresh':
                    ns.treebarCom.setItems(ns._buildItems(this.topNodes));
                    break;

                case 'import':
                    ood.getModule("RAD.api.APITree", function () {
                        this.setData({projectName: SPA.curProjectName});
                        var ns=this;
                        var endFun = function () {
                            ns.initData();
                            ns._fireEvent('afterShow');
                        };

                        this.show(endFun);
                    });
                    break;

            }
        },
        _buildItems:function (items) {
            var nitems = [], ns = this;
            ood.each(items, function (item) {
                if (item.key && CONF.mapWidgets[item.key]) {
                    var p = CONF.mapWidgets[item.key];
                    item.imageClass = CONF.mapWidgets[item.key].imageClass;
                }
                item.children = item.sub;
                item.properties = item.iniProp;
                if (item.sub) {
                    item.sub = ns._buildItems(item.sub);
                };

                item.getProperties=function () {
                    return this.properties;
                }


            })
            return items;
        },
        _ood_ui_treeview19_onchange:function (profile, oldValue, newValue, force, tag) {
            var ns = this, uictrl = profile.boxing();
            ood.echo( ood.getModule("RAD.api.URLConfig").getDataFromEditor());
        },

        setDataToEditor:function (host,cls,api) {
            this.$cls = cls;
            this.$host = host;
            this.$api=api;
            this.treebarCom.setItems(this._buildItems(this.topNodes));

        },




        _page_onrender:function (module, threadid) {
            var ns=this;
            ood.showModule3("RAD.expression.ExpressionEditor","content",null,function () {
                this.setDataToEditor(ns.$host , ns.$cls, ns.$api);
                this.setEvents("onValueChanged", function (profile,expression) {
                    ns.$api.queryArgs.expression=expression;
                    ns.fireEvent("onchange", [this,expression]);
                });
            });


        },
        topNodes:[{"id":"pageService", "key":"jds.getPageServiceToolBoxService", "caption":"$RAD.toolbox.pageService", "group":true, "initFold":false, "imageClass":"spafont spa-icon-action1", "sub":true}, {"id":"projectService", "key":"jds.getProjectServiceToolBoxService", "caption":"$RAD.toolbox.projectService", "group":true, "initFold":false, "imageClass":"ri-3d-box-line", "sub":true}, {"id":"localService", "key":"jds.localService", "caption":"$RAD.toolbox.cls", "group":true, "initFold":true, "imageClass":"spafont spa-icon-options", "sub":true}]
    } ,
    Static:{
        "designViewConf":{
            "height":1024,
            "mobileFrame":false,
            "width":1280
        }
    }



});
