ood.Class('RAD.ServiceTree', 'ood.Module', {
    Instance: {
        initialize: function () {
            this.autoDestroy = false;
            this.properties = {};
        },
        iniComponents: function () {
            // [[Code created by  RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                (new ood.UI.Block())
                    .setHost(host, "servicedlg")
                    .setDock("fill")
                    .setBorderType("ridge")
                    .setCustomStyle({
                        "PANEL": "overflow:hidden"
                    })
            );

            host.servicedlg.append(
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
            host.servicedlg.append(
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


            host.servicedlg.append(
                (new ood.UI.TreeBar())
                    .setHost(host, "treebarCom")
                    .setSelMode("none")
                    .setDragKey("___iDesign")
                    .onGetContent("_treebarcom_ongetcontent")
                    .setCustomStyle({
                        "KEY": {
                            "background-color": "#FFFFFF"
                        }
                    })
            );

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        //  events: {"onRender": "_com_onrender"},
        clearContent: function () {
            this.treebarCom.clearItems();
        },

        topNodes: [

            {
                id: 'pageService',
                key: 'jds.getPageServiceToolBoxService',
                caption: '$RAD.toolbox.pageService',
                group: true,
                initFold: false,
                imageClass: 'ri-tools-line',
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
                }
            })
            return items;
        },

        fillContent: function (com, threadid) {
            var clss = [], ns = this;
            ns.treebarCom.setItems(ns._buildItems(this.topNodes))
                .setCustomBehavior({
                    BAR: {
                        beforeMousedown: function (profile, e, src) {
                            if (ood.Event.getBtn(e) != "left") return;
                            if (profile.properties.disabled) return;
                            // not resizable or drag
                            if (!profile.properties.dragKey) return;

                            // avoid nodraggable keys
                            if (profile.behavior.NoDraggableKeys) {
                                var sk = profile.getKey(ood.Event.getSrc(e).id || "").split('-')[1];
                                if (sk && ood.arr.indexOf(profile.behavior.NoDraggableKeys, sk) != -1) return;
                            }
                            var id = ood.use(src).id(),
                                itemId = profile.getSubId(id),
                                properties = profile.properties,
                                item = profile.getItemByDom(id),
                                pos = ood.Event.getPos(e);

                            if (item.draggable) {
                                profile.getSubNode('ITEMICON', itemId).startDrag(e, {
                                    dragType: 'icon',
                                    shadowFrom: src,
                                    dragCursor: 'pointer',
                                    targetLeft: pos.left + 12,
                                    targetTop: pos.top + 12,
                                    sub: true,
                                    dragKey: item.dragKey || profile.properties.dragKey,
                                    dragData: {
                                        cls: item.cls || item.key,
                                        type: item.key,
                                        caption: item.caption,
                                        iniProp: item.iniProp,
                                        children: item.sub,
                                        iniEvents: item.iniEvents,
                                        customRegion: item.customRegion,
                                        image: item.image,
                                        imagePos: item.imagePos,
                                        imageClass: item.imageClass
                                    }
                                });
                                ood.use(src).tagClass('-hover', false);
                                ood.use(src).tagClass('-active', false);

                                if (RAD.Designer) {
                                    ood.each(RAD.Designer.getAllInstance(), function (page) {
                                        if (page && !page.destroyed) {
                                            page._clearSelect();
                                            if (page.resizer)
                                                page._setSelected([], true);
                                        }
                                    });
                                }
                                return false;
                            }
                        }
                    }
                })
                .beforePrepareItem(function (profile, item, pid) {
                    if (item.key && CONF.mapWidgets[item.key]) {
                        var p = CONF.mapWidgets[item.key];
                        item.imageClass = CONF.mapWidgets[item.key].imageClass;
                    }
                });
            ns._reload();

        },



        customAppend: function (parent, subId, left, top) {
            return true;
        },
        show2: function () {
            this.servicedlg.show();
        },
        hide: function () {
            this.servicedlg.hide();
        },

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


        }
    }
});
