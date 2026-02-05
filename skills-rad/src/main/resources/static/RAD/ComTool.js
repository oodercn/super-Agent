ood.Class('RAD.ComTool', 'ood.Module', {

    Instance: {
        initialize: function () {
            this.autoDestroy = false;
            this.properties = {};
        },
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                (new ood.UI.Panel())
                    .setHost(host, "dlg")
                    .setLeft(0)
                    .setTop(0)
                    .setWidth(200)
                    .setHeight(500)
                    .setCaption("$RAD.designer.toolsbox")
                    .setImageClass("spafont spa-icon-coms")
                    .setPopBtn(true)
                    .beforePop("_ctl_beforepop")
                    .setCustomStyle({
                        "PANEL": "overflow:hidden"
                    })
            );

            host.dlg.append(
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
        events: {"onRender": "_com_onrender"},
        clearContent: function () {
            this.treebarCom.clearItems();
        },
        _dlgpos: {},
        _dlgsize: {},
        _ctl_beforepop: function (profile, options) {
            var ns = this;
            ns.fireEvent('afterPop');
            options.properties = {
                fill: 'none',
                minBtn: true,
                maxBtn: false,
                landBtn: false,
                pinBtn: true
            };
            options.events = {
                beforePin: function (dlgprf) {
                    ns._dlgpos.left = dlgprf.properties.left;
                    ns._dlgpos.top = dlgprf.properties.top;
                    ns._dlgsize.width = dlgprf.properties.width;
                    ns._dlgsize.height = dlgprf.properties.height;

                    ns.fireEvent('afterLand', [ns]);
                    dlgprf.boxing().removeChildren().destroy();
                }
            };
            options.init = function (dialog, profile, options) {
                if (ns._dlgpos.left) dialog.setLeft(ns._dlgpos.left);
                if (ns._dlgpos.top) dialog.setTop(ns._dlgpos.top);
                if (ns._dlgsize.width) dialog.setWidth(ns._dlgsize.width);
                if (ns._dlgsize.height) dialog.setHeight(ns._dlgsize.height);

                dialog.setInitPos("auto");
                dialog.show(options.parent || ood('body'));
                var arr = [];
                ood.arr.each(profile.children, function (o) {
                    arr.push(o[0]);
                });
                if (arr.length) {
                    dialog.append(ood.UI.pack(arr, false));
                }
                return false;
            }
        },
        fillContent: function (com, threadid) {
            buildItems = function (items) {

                var nitems = [];
                ood.each(items, function (item) {
                    if (item.key && CONF.mapWidgets[item.key]) {
                        var p = CONF.mapWidgets[item.key];
                        item.imageClass = CONF.mapWidgets[item.key].imageClass;
                        if (!ood.str.startWith(item.id, item.className)) {
                            ood.id = item.className + '.' + item.id
                        }
                    }
                    item.children = item.sub;
                    item.properties = item.iniProp;
                    if (item.sub) {
                        item.sub = buildItems(item.sub);
                    }

                })
                return items;
            };


            this.treebarCom
                .setItems(ood.clone(CONF.widgets, function (o, i) {
                    if (o && o.ProjectOnly && CONF.clientMode != 'project')
                        return false;
                }))
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
                            var moduletype = item.className;
                            if (item.className && item.key == 'ood.Module') {
                                moduletype = item.className;
                            } else {
                                moduletype = item.key
                            }


                            if (item.draggable) {
                                profile.getSubNode('ITEMICON', itemId).startDrag(e, {
                                    dragType: 'icon',
                                    shadowFrom: src,
                                    dragCursor: 'pointer',
                                    targetLeft: pos.left + 12,
                                    targetTop: pos.top + 12,
                                    dragKey: item.dragKey || profile.properties.dragKey,
                                    dragData: {
                                        cls: item.cls || item.key,
                                        type: moduletype,
                                        children: item.sub,
                                        caption: item.caption,
                                        iniProp: item.iniProp,
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
                .toggleNode('ood.UI.absForm', true)
                .beforePrepareItem(function (profile, item, pid) {
                    if (item.key && CONF.mapWidgets[item.key]) {
                        var p = CONF.mapWidgets[item.key];
                        item.imageClass = CONF.mapWidgets[item.key].imageClass;
                    }
                    if (CONF.widgets_hidden && (CONF.widgets_hidden[item.id] || CONF.widgets_hidden[item.key])) {
                        return false;
                    }
                    if (CONF.widgets_hidden_filter && false === CONF.widgets_hidden_filter(item.id, item.key)) {
                        return false;
                    }

                });

        },
        customAppend: function (parent, subId, left, top) {
            return true;
        },
        show2: function () {
            this.dlg.show();
        },
        hide: function () {
            this.dlg.hide();
        },
        _treebarcom_ongetcontent: function (profile, item, callback) {
            switch (item.key) {
                case "jds.extcomproject":
                    ood.Ajax(CONF.GetExtComProjectTreeService, null, function (txt) {
                        var obj = txt;
                        if (!obj || obj.error) {
                            ood.message("No module in this project!");
                            callback(false);
                        } else {
                            var items = [];
                            ood.arr.each(obj.data, function (o, i) {
                                if (o.projectName != SPA.curProjectName) {
                                    items.push({
                                        id: o.id,
                                        cls: o.cls,
                                        key: "jds.package",
                                        packName: o.location,
                                        draggable: false,
                                        sub: true,
                                        initFold: false,
                                        imageClass:  o.imageClass|'ri-3d-box-line',
                                        name: o.caption,
                                        caption: o.caption,
                                        projectName: o.projectName,
                                        iniProp: o.iniProp,
                                        caption: o.caption
                                    });
                                }
                            });
                            callback(items);

                        }
                    }, function (txt) {
                        callback(false);
                    }).start();
                    break;
                case "jds.localModule":
                    ood.Ajax(CONF.getAllComponentsByPath, {
                        path: item.path,
                        projectName: item.projectName ? item.projectName : SPA.curProjectName
                    }, function (txt) {
                        var obj = txt;
                        if (!obj || obj.error) {
                            ood.message("No module in this project!");
                            callback(false);
                        } else {

                            var items = [];
                            ood.arr.each(obj.data, function (o, i) {
                                var item = ood.clone(o);
                                if (o.alias) {
                                    item.caption = o.alias;
                                }
                                items.push(item);

                            });

                            callback(items);

                        }
                    }, function (txt) {
                        callback(false);
                    }).start();
                    break;

                case "jds.package":
                    ood.Ajax(CONF.GetAllPackages, {projectName: item.projectName ? item.projectName : SPA.curProjectName}, function (txt) {
                        var obj = txt;
                        if (!obj || obj.error) {
                            ood.message("No module in this project!");
                            callback(false);
                        } else {
                            var items = [];
                            ood.arr.each(obj.data, function (o, i) {
                                items.push({
                                    id: o.projectName + '.' + o.id,
                                    cls: o.cls,
                                    key: "jds.localModule",
                                    path: o.location,
                                    draggable: false,
                                    sub: true,

                                    projectName: o.projectName,
                                    imageClass:  o.imageClass|'ri-grid-line',
                                    name: o.caption,
                                    iniProp: o.iniProp,
                                    caption: o.className
                                });

                            });
                            callback(items);
                        }
                    }, function (txt) {
                        callback(false);
                    }).start();
                    break;
            }


        }
    }

});
