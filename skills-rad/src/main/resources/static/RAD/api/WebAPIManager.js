ood.Class('RAD.api.WebAPIManager', 'ood.Module', {
    Instance: {
        initialize: function () {
        },
        Dependencies: [],

        Required: [
            "RAD.api.URLConfig"
        ],
        properties: {
            "path": "form/myspace/versionspace/projectManager/0/RAD/api/WebAPIManager.cls",
            "projectName": "projectManager"
        },
        events: {
            "onRender": {
                "actions": [
                    {
                        "args": [
                            "{page.show2()}",
                            null,
                            null,
                            "resourcelayout",
                            "main",
                            null,
                            null,
                            "{page}"
                        ],
                        "className": "RAD.api.URLConfig",
                        "desc": "动作 1",
                        "method": "show2",
                        "redirection": "page",
                        "target": "RAD.api.URLConfig",
                        "type": "page"
                    }
                ]
            }
        },
        properties: {},
        functions: {},
        iniComponents: function () {
            // [[Code created by JDSEasy RAD Studio
            var host = this, children = [], properties = {}, append = function (child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);
            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog9")
                    .setLeft("1.6666666666666667em")
                    .setTop("3.3333333333333335em")
                    .setWidth("84.16666666666667em")
                    .setHeight("50.833333333333336em")
                    .setCaption("API管理")
                    .setImageClass("spafont spa-icon-rpc")
            );

            host.ood_ui_dialog9.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "className")
                    .setName("className")
                    .setValue("")
            );

            host.ood_ui_dialog9.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "resourcemain")
                    .setName("resourcemain")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.resourcemain.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "resourcelayout")
                    .setName("resourcelayout")
                    .setItems([
                        {
                            "cmd": true,
                            "folded": false,
                            "id": "left",
                            "locked": false,
                            "min": 10,
                            "pos": "before",
                            "size": 200,
                            "hidden": false
                        },
                        {
                            "id": "main",
                            "min": 10,
                            "size": 80
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setType("horizontal")
            );

            host.resourcelayout.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block60")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em"),
                "left"
            );

            host.ood_ui_block60.append(
                ood.create("ood.UI.HiddenInput")
                    .setHost(host, "projectName")
                    .setName("projectName")
                    .setValue("")
            );


            host.ood_ui_block60.append(
                (new ood.UI.TreeBar())
                    .setHost(host, "treebarCom")
                    .setSelMode("none")
                    .onItemSelected("_ood_ui_treeview19_onitemselected")
                    .onGetContent("_treebarcom_ongetcontent")
                    .onChange("_ood_ui_treeview19_onchange")
                    .setCustomStyle({
                        "KEY": {
                            "background-color": "#FFFFFF"
                        }
                    })
            );


            host.ood_ui_block60.append(
                (new ood.UI.ToolBar())
                    .setHost(host, "servicetool")
                    .setHandler(false)
                    .onClick("_toolbar_onclick")
                    .setCustomStyle({
                        "ITEMS": "border-width:0 0 1px 0;"
                    })
            );



            var items = [{
                id: 'any', sub: [
                    {id: 'refresh', imageClass: 'ri-refresh-line', caption: '', tips: '$RAD.tool2.refresh'},
                    {id: 'import', imageClass: 'ri-add-line', caption: '', tips: '$RAD.toolbox.importapi'}
                ]
            }];

            host.servicetool.setItems(items);
            host.ood_ui_block60.append(
                (new ood.UI.ComboInput())
                    .setHost(host, "pattern")
                    .setName("pattern")
                    .setDock("top")
                    .setLeft("3.6666666666666665em")
                    .setTop("2em")
                    .setWidth("15em")
                    .setLabelSize("3em")
                    .setLabelCaption("$RAD.toolbox.search")
                    .setType("helpinput")
                    .onChange("_reload")
            );


            host.resourcelayout.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "content")
                    .setName("content")
                    .setDock("fill")
                    .setLeft("15.833333333333334em")
                    .setTop("13.333333333333334em"),
                "main"
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },
        _buildItems: function (items) {
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

        topNodes: [

            {
                id: 'pageService',
                key: 'jds.getPageServiceToolBoxService',
                caption: '$RAD.toolbox.pageService',
                group: true,
                initFold: false,
                imageClass: 'ri-flashlight-line',
                sub: true
            },
            {
                id: 'projectService',
                key: 'jds.getProjectServiceToolBoxService',
                caption: '$RAD.toolbox.projectService',
                group: true,

                initFold: false,
                imageClass: 'ri-3d-box-line',
                sub: true
            },
            {
                id: 'localService',
                key: 'jds.localService',
                caption: '$RAD.toolbox.cls',
                group: true,
                initFold: true,
                imageClass: 'ri-settings-3-line',
                sub: true
            }

        ],

        _reload: function (profile, item) {
            ns = this;
            ns.treebarCom.setItems(ns._buildItems(this.topNodes));
            ns.treebarCom.fireItemClickEvent('pageService');

        },

        _toolbar_onclick: function (profile, item, group, e, src) {
            var ns = this;
            switch (item.id) {
                case 'refresh':
                    ns.treebarCom.setItems(ns._buildItems(this.topNodes));
                    break;

                case 'import':
                    ood.getModule("RAD.api.APITree", function () {
                        this.setData({projectName: SPA.curProjectName});
                        this.show();
                        this.initData();
                        this._fireEvent('afterShow');
                    });
                    break;

            }
        },

        _treebarcom_ongetcontent: function (profile, item, callback) {
            ns = this;
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
                    className: SPA.getCurrentClassName(),
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

        _ood_ui_treeview19_onchange:function (profile, oldValue, newValue, force, tag) {
            var ns = this, uictrl = profile.boxing();
            ood.echo( ood.getModule("RAD.api.URLConfig").getDataFromEditor());
        } ,
        _ood_ui_treeview19_onitemselected: function (profile, item, e, src, type) {
            var ns = this, uictrl = profile.boxing(),
                prop = ns.properties, callback = function () { };
            ood.getModule("RAD.api.URLConfig", function () {
               this.setDataToEditor(item, ns.$host, ns.$cls,ns.$designer);
                this.setEvents({
                    "onchange": callback
                });

                //this.show();
            });
        },

        setDataToEditor: function (host, cls,designer) {
            this.$cls = cls;
            this.$host = host;
            this.$designer=designer;
            this.treebarCom.setItems(this._buildItems(this.topNodes));

        },

        customAppend: function (parent, subId, left, top) {
            return false;
        }
    },
    Static: {
        "designViewConf": {
            "height": 1024,
            "mobileFrame": false,
            "width": 1280
        }
    }


});
