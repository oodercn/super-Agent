ood.Class('RAD.ProjectPro', 'ood.Module', {
    Instance: {
        customAppend: function (parent) {
            var ns = this,
                prop = ns.properties,
                pp = prop.parent,
                dlg = ns.dialog;

            if (!dlg.get(0).renderId) {
                dlg.render();
            }
            ns.ctl_tabs2.setUIValue("local");

            //asy
            dlg.showModal(parent, null, null, function () {
                //ns.lastbtn.adjustDock();
            });

        },
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };


            append((new ood.UI.Dialog())
                .setHost(host, "dialog")
                //.setDockMargin({"left": 10, "top": 10, "right": 10, "bottom": 10})
                .setLeft(-2)
                .setTop(-2)
                .setWidth(700)
                .setHeight(398)
                .setResizer(false)
                .setCaption("新建工程")
                .setImageClass("spafont spa-icon-project")
                .setMinBtn(false)
                .setMinWidth(700)
                .setMinHeight(400)
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );

            host.dialog.append((new ood.UI.Block())
                .setHost(host, "ctl_block75")
                .setDock("fill")
                .setBorderType("inset")
            );

            host.ctl_block75.append((new ood.UI.Tabs())
                    .setHost(host, "ctl_tabs2")
                    .setItems([
                        {"id": "local", "caption": "从模板新建", "image": ""},
                        {"id": "online", "caption": "模板市场", "image": ""},
                        {"id": "blank", "caption": "空白工程"}])
                    .setDock("top")
                    .setDockOrder(3)
                    .setHeight("auto")
                    .setLazyAppend(false)
                    .setNoPanel(true)
                    .beforeUIValueSet("_ctl_tabs2_beforepage")
                    .setCustomStyle({"ITEMS": {"padding-left": "4px"}})
              );

            host.ctl_block75.append((new ood.UI.Block())
                    .setHost(host, "ctl_block10")
                    .setDock("fill")
                    .setBorderType("none"));

            host.ctl_block10.append((new ood.UI.Pane())
                .setHost(host, "ctl_pane56")
                .setClassName("ood-uibg-base")
                .setDock("fill")
            );

            host.ctl_pane56.append((new ood.UI.Layout())
                .setHost(host, "ctl_layout6")
                .setItems([{
                    "id": "before",
                    "pos": "before",
                    "size": 150,
                    "min": 10,
                    "locked": false,
                    "folded": false,
                    "hidden": false,
                    "cmd": true
                }, {"id": "main", "min": 10, "size": 390}, {
                    "id": "after",
                    "pos": "after",
                    "size": 80,
                    "min": 10,
                    "locked": false,
                    "folded": false,
                    "hidden": true,
                    "cmd": false,
                    "itemDisplay": "display:none;"
                }])
                .setType("horizontal")
            );

            host.ctl_layout6.append(
                (new ood.UI.Pane())
                    .setHost(host, "ctl_ads")
                    .setDock("bottom")
                    .setHeight(0)
                , "main");

            host.ctl_layout6.append((new ood.UI.Gallery())
                    .setHost(host, "ctl_gallery")
                    .setDirtyMark(false)
                    .setDisableTips(true)
                    .setDock("fill")
                    .setSelMode("none")
                    .setBorderType("none")
                    .setItemMargin(2)
                    .setItemWidth(124)
                    .setItemHeight(120)
                    .setImgWidth(120)
                    .setImgHeight(80)
                    .onItemSelected("_ctl_gallery_onitemselected")
                    .setCustomStyle({
                        "IMAGE": {
                            "border": "solid #dddddd 1px"
                        }
                    })
                , "main");

            host.ctl_layout6.append((new ood.UI.TreeView())
                    .setHost(host, "ctl_list")
                    .setDirtyMark(false)
                    .onGetContent("_ctl_list_ongetcontent")
                    .onItemSelected("_ctl_list_onitemselected")
                , "before");

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events: {"onReady": "_onready", "onRender": "_onrender"},
        _onready: function () {
            var ns = this;

            ns._preview = new RAD.TemplatePreview();
            ns._preview.setEvents({
                onOK: function (ppath, pname) {

                    ns.dialog.hide();
                    CONF.openProject(pname, ns.properties.onOK1, ns.properties.onOK2, function () {
                    }, ns.properties.namespace);
                }
            });
        },
        _onrender: function () {
            var ns = this,
                preview = ns._preview,
                items = ns.ctl_gallery.getItems();
            ns._preview.render();
            ns.ctl_gallery.hoverPop(preview.blkMain, 12, function (profile, node, e, src, item) {
                preview.init(item, items, ns.properties);
            }, function () {
                return !preview.isStopHover();
            });
        },
        _dialog_beforeclose: function (profile) {
            this.ctl_tabs2.setUIValue(null);
            profile.boxing().hide();
            return false;
        },
        _dialog_onhotkey: function (profile, key) {
            if (key.key == 'esc')
                profile.boxing().close();
        },
        getImagePreTag: function (type) {
            return type == "online" ? CONF.onlineTempalteLibPath :
                type == "local" ? CONF.serviceType == "node-webkit" ? CONF.adjustProtocol('') : '' :
                    "";
        },
        _ctl_tabs2_beforepage: function (profile, oldValue, newValue) {
            var ns = this;
            switch (newValue) {
                case 'blank':

                    ood.showCom("RAD.AddProject",function(){
                        this.setProperties(null);
                        this.setProperties({
                            fileName:ns._item['name'].replace(/^[\w]+_tpl(code)?_/,""),
                            tempName:ns._item.name,
                            item:ns._item,
                            prop:ns.prop
                        });
                        this.setEvents({
                            onOK:function(){
                                ns._item=ns._items=null;
                                ns.blkMain.hide();
                                ns.fireEvent('onOK', ood.toArr(arguments),this);
                            }
                        });
                    },null,null,true);

                    ood.showCom("RAD.AddProject", function () {
                        this.setProperties(null);
                        this.setProperties({
                            fileName: "newProject",
                            item:{desc:'空白工程',url:'http://dev.itjds.net'},
                            tempName:'defaultProjectTemplat'

                        });
                        this.setEvents({
                            onOK: function (ppath, pname) {
                                ns.dialog.hide();
                                CONF.openProject(pname, ns.properties.onOK1, ns.properties.onOK2, function () {
                                }, ns.properties.namespace);
                            }
                        });
                    }, null, null, true);
                    return false;
            }

            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_list.resetValue().clearItems();

            ood.asyRun(function () {
                var callback = function (txt) {
                    ns.ctl_list.free();
                    var obj = txt;
                    if (!obj || obj.error) {
                        switch (obj.error.id) {
                            case 3005:
                                ood.alert(ood.adjustRes('$(RAD.msg.$0 dones not exit-URI)'));
                                break;
                            default:
                                ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                        }
                    } else {
                        var items = [],
                            conf = obj.data.conf ? ood.unserialize(obj.data.conf) : false,
                            sortby = {},
                            map = {};
                        if (conf) {
                            if (conf.list)
                                ood.arr.each(conf.list, function (o, i) {
                                    map[o.id] = o;
                                    sortby[o.id] = i;
                                });
                        }
                        ;
                        if (obj.data) {
                            ood.arr.each(obj.data, function (o, i) {
                                items.push({
                                    id: o.type,
                                    name: o.name,
                                    // sub:true,
                                    caption: ood.get(map, [o.name, 'caption', ood.getLang()]) || o.name
                                });
                            });
                        }
                        if (!ood.isEmpty(sortby)) {
                            ood.arr.stableSort(items, function (x, y) {
                                x = sortby[x.name] || -1;
                                y = sortby[y.name] || -1;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        }
                        ns.ctl_list.setItems(items);
                        if (items[0]) {
                            ns.ctl_list.fireItemClickEvent(items[0].id);
                        }
                    }
                };
                switch (newValue) {
                    case "local":
                        ns.ctl_list.busy(true);
                        ood.Ajax(CONF.getProjectTempType, {path: CONF.localProjectLibPath}, callback).start();
                        break;
                    case "online":
                        // ns.ctl_list.busy(true);

                        break;
                }
            });
        },
        _ctl_list_onitemselected: function (profile, item, e, src, type) {
            var ns = this,
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                remote = tabpage == "online",
                preTag = ns.getImagePreTag(tabpage),
                callback = function (txt) {
                    ns.ctl_gallery.free();
                    var obj = txt;
                    if (!obj || obj.error)
                        ood.message(ood.get(obj, ['error', 'errdes']) || 'no response!');
                    else {
                        var items = [],
                            conf = obj.data.conf ? ood.unserialize(obj.data.conf) : false,
                            sortby = {},
                            map = {};
                        if (conf) {
                            if (conf.list)
                                ood.arr.each(conf.list, function (o, i) {
                                    map[o.id] = o;
                                    sortby[o.id] = i;
                                });
                        }
                        ;
                        if (obj.data) {
                            ood.arr.each(obj.data, function (o, i) {
                                var item = {
                                    id: o.location,
                                    url: o.url,
                                    desc: o.desc,
                                    name: o.name,
                                    caption: null,
                                    comment: o.desc || o.name,
                                    image: o.image || "/RAD/img/project.png"
                                };
                                var prop = ood.get(map, [o.name, 'prop']);
                                if (prop && prop.charged) {
                                    item.flagStyle = "top:0;font-size:1.5em;";
                                    item.prop = prop;
                                    item.flagClass = "spafont spa-icon-coin";
                                }
                                items.push(item);

                            });
                        }

                        if (!ood.isEmpty(sortby)) {
                            ood.arr.stableSort(items, function (x, y) {
                                x = sortby[x.name] || -1;
                                y = sortby[y.name] || -1;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        }

                        ns.ctl_gallery.resetValue();
                        ns.ctl_gallery.setItems(items);
                    }
                };
            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_gallery.busy(true);
            switch (tabpage) {
                case "local":
                case "self":
                    ood.Ajax(CONF.getProjectTemp,
                        {
                            type: item.id
                        }, function (txt) {
                            callback.call(ns, txt);
                        }).start();


                    break;
            }
            if (item.sub && !item._checked)
                ns.ctl_list.toggleNode(item.id, true);
        },
        _ctl_list_ongetcontent: function (profile, item, setsub) {
            var ns = this,
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                callback = function (txt) {
                    var obj = txt;
                    if (!obj || obj.error)
                        ood.message(ood.get(obj, ['error', 'errdes']) || 'no response!');
                    else {
                        var items = [],
                            conf = obj.data.conf ? ood.unserialize(obj.data.conf) : false,
                            sortby = {},
                            map = {};
                        if (conf) {
                            if (conf.list)
                                ood.arr.each(conf.list, function (o, i) {
                                    map[o.id] = o;
                                    sortby[o.id] = i;
                                });
                        }
                        ;
                        if (obj.data.files) {
                            ood.arr.each(obj.data.files, function (o, i) {
                                if (o.name.charAt(0) != ".'" && !ood.str.startWith(o.name, CONF.ood_project_tpl_tag))
                                    items.push({
                                        id: o.location,
                                        name: o.name,
                                        sub: true,
                                        caption: ood.get(map, [o.name, 'caption', ood.getLang()]) || o.name
                                    });
                            });
                        }
                        if (!ood.isEmpty(sortby)) {
                            ood.arr.stableSort(items, function (x, y) {
                                x = sortby[x.name] || -1;
                                y = sortby[y.name] || -1;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        }
                        setsub(items.length === 0 ? false : items);
                        //if(items&&items.length)
                        //    profile.boxing().fireItemClickEvent(items[0].id);
                    }
                };
            switch (tabpage) {
                case "self":
                case "local":

                    ood.Ajax(CONF.openFolderService,
                        {
                            path: item.id
                        }, function (txt) {
                            callback.call(ns, txt);
                        }, null, null, {method: 'POST'}).start();

                    break;

            }
        },
        _ctl_gallery_onitemselected: function (profile, item, e, src, type) {
            this._preview._apply(item);
        },


    }
});
