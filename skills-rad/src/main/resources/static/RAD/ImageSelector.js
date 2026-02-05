ood.Class('RAD.ImageSelector', 'ood.Module', {
    Instance: {
        customAppend: function (parent) {
            var self = this,
                prop = self.properties,
                pp = prop.parent,
                dlg = self.dialog;
            if (!dlg.get(0).renderId) {
                dlg.render();
            }
            //asy
            dlg.show(parent, true);
            self.ctl_tabs2.setUIValue(null).setUIValue("inner");
        },
        _dialog_beforeclose: function (profile) {
            this.dialog.hide();
            return false;
        },
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("3.125em")
                    .setTop("1.25em")
                    .setWidth("54.333333333333336em")
                    .setHeight("30em")
                    .setCaption("$(RAD.img.Image Selector)")
                    .setImageClass("spafont spa-icon-gallery")
                    .setMinBtn(false)
                    .onHotKeydown("_dialog_onhotkey")
                    .beforeClose("_dialog_beforeclose")
            );

            host.dialog.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_div13")
                    .setDock("bottom")
                    .setLeft("5.833333333333333em")
                    .setHeight("3.1666666666666665em")
                    .setTop("0em")
                    .setRight("0em")
                    .setBottom("0em")
            );

            host.ood_ui_div13.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnCancel")
                    .setTop("0.5833333333333334em")
                    .setWidth("7.75em")
                    .setRight("12em")
                    .setImageClass("spafont spa-icon-cancel")
                    .setCaption("$RAD.cancel")
                    .onClick("_btncancel_onclick")
            );

            host.ood_ui_div13.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnOK")
                    .setTop("0.5833333333333334em")
                    .setWidth("7.75em")
                    .setRight("2em")
                    .setImageClass("spafont spa-icon-ok")
                    .setCaption("$RAD.ok")
                    .onClick("_btnok_onclick")
            );

            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block10")
                    .setDock("fill")
                    .setHeight("20.625em")
                    .setBorderType("inset")
            );

            host.ctl_block10.append(
                ood.create("ood.UI.ButtonViews")
                    .setHost(host, "ctl_tabs2")
                    .setItems([
                        {
                            "id": "inner",
                            "caption": "本地图片",
                            "image": ""
                        }

                    ])
                    .setNoHandler(true)
                    .setDock("top")
                    .setHeight("0em")
                    .setLazyAppend(false)
                    .setNoPanel(true)
                    .setTop("0em")
                    .setRight("0em")
                    .setBottom("0em")
                    .setValue(null)
                    .beforeUIValueSet("_ctl_tabs2_beforepage")
            );

            host.ctl_block10.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "ctl_layout6")
                    .setItems([{
                        "id": "before",
                        "pos": "before",
                        "size": 160,
                        "min": 10,
                        "locked": false,
                        "folded": false,
                        "hidden": false,
                        "cmd": true
                    },
                        {
                            "id": "main",
                            "min": 10,
                            "size": 540
                        },
                        {
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
                ood.create("ood.UI.Gallery")
                    .setHost(host, "ctl_gallery")
                    .setDirtyMark(false)
                    .setDock("fill")
                    .setBorderType("none")
                    .onDblclick("_ctl_gallery_ondblclick")
                    .setCustomStyle({
                            "ITEMS": {
                                "overflow": "overflow-x:hidden;overflow-y:auto"
                            }
                        }
                    )
                , "main");

            host.ctl_layout6.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block12")
                    .setDock("top")
                    .setHeight("2.4166666666666665em")
                    .setOverflow("hidden")
                    .setTop("0em")
                    .setRight("0em")
                    .setBottom("0em")
                , "main");


            host.ctl_block12.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host, "ctl_pagebar1")
                    .setTop("0.3333333333333333em")
                    .setRight("0.125em")
                    .setCaption("")
                    .onClick("_ctl_pagebar1_onclick")
            );

            host.ctl_block12.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_input1")
                    .setDirtyMark(false)
                    .setLeft("0.3125em")
                    .setTop("0.25em")
                    .setWidth("21.333333333333332em")
                    .setDynCheck(true)
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.img.Search in Category)")
                    .setType("none")
                    .setImageClass("ood-icon-search")
                    .setCommandBtn("delete")
                    .onChange("_ctl_input1_onchange")
                    .onCommand("_ctl_input1_oncommand")
            );


            host.ctl_layout6.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "leftPanle")
                    .setDock("fill")
                    .setLeft("0em")
                    .setBorderType("none")
                , "before");


            host.leftPanle.append(
                (new ood.UI.ToolBar())
                    .setHost(host, "servicetool")
                    .setHandler(false)
                    .onClick("_toolbar_onclick")
            );

            var items = [{
                id: 'any', sub: [
                    {id: 'refresh', imageClass: 'ri-refresh-line', caption: '刷新', tips: '$RAD.tool2.refresh'},
                    //{id: 'import', imageClass: 'ri-add-line', caption: '导入', tips: '导入图标'},
                    {id: 'upload', imageClass: "ri-upload-line", caption: '上传', tips: '上传图片'}
                ]
            }];

            host.servicetool.setItems(items);

            host.leftPanle.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "ctl_list")
                    .setDirtyMark(false)
                    .onGetContent("_ctl_list_ongetcontent")
                    .onItemSelected("_ctl_list_onitemselected")
            );


            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _btncancel_onclick: function (profile, e, value) {
            this.dialog.close();
        },
        _btnok_onclick: function (profile, e, value) {
            this._selectImage();
        },
        _selectImage: function () {
            var ns = this,
                tabid = ns.ctl_tabs2.getUIValue();
            // check selected
            if (!ns.ctl_gallery.getUIValue()) {
                ood.alert(ood.adjustRes('$RAD.selFilePath'));
                return;
            }
            var value = ns.ctl_gallery.getUIValue();
            ns.dialog.busy(ood.adjustRes('$(RAD.img.Fechting image)...'));
            ood.tryF(ns.properties.onOK, [ns, value], ns.host);
            ns.dialog.free();
            ns.dialog.close();
        },
        _dialog_onhotkey: function (profile, key) {
            if (key.key == 'esc')
                profile.boxing().close();
        },
        _ctl_tabs2_beforepage: function (profile, oldValue, newValue) {
            var ns = this;
            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_list.resetValue().clearItems();
            ns.ctl_list.busy(true);
            var callback = function (txt) {
                    ns.ctl_list.free();
                    var obj = txt;
                    if (!obj || obj.requestStatus == -1 || obj.error)
                        ood.message(ood.get(obj, ['error', 'errdes']) || 'no response!');
                    else {
                        var items = [], outherItems = [],
                            conf = obj.data.config ? ood.unserialize(obj.data.config) : false,
                            sortby = {},
                            map = {};

                        if (obj.data.sub) {
                            ood.arr.each(obj.data.sub, function (o, i) {
                                outherItems.push({
                                    id: o.location ? o.location : o.id,
                                    name: o.name,
                                    caption: ood.get(map, [o.name, 'caption', ood.getLang()]) || o.name,
                                    imageWidth: ood.get(map, [o.name, 'width']) || ood.get(conf, ['imageSize', 'width']),
                                    imageHeight: ood.get(map, [o.name, 'height']) || ood.get(conf, ['imageSize', 'height'])
                                });
                            });
                        }


                        if (!ood.isEmpty(sortby)) {
                            ood.arr.stableSort(items, function (x, y) {
                                x = sortby[x.name] || -1;
                                y = sortby[y.name] || -1;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        } else {
                            ood.arr.stableSort(items, function (x, y) {
                                x = x.name;
                                y = y.name;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        }

                        var imgs = [];
                        if (obj.data.imgs) {
                            ood.arr.each(obj.data.imgs, function (o, i) {
                                imgs.push({
                                    id: o.location,
                                    caption: '',
                                    image: o.location,
                                    comment: o.name, tips: o.name
                                })
                            });
                        }
                    }
                    ns.ctl_gallery.setImgWidth(conf.imageWidth || 64);
                    ns.ctl_gallery.setImgHeight(conf.imageHeight || 64);
                    ns.ctl_gallery.resetValue();
                    ns.ctl_gallery.setItems(imgs);

                    items.push({id: obj.data.id, path: obj.data.location || obj.data.name, caption: "工程内图片"});
                    if (outherItems && outherItems.length > 0) {
                        items.push({id: 'all', caption: '本地图片', sub: items, initFold: false})
                    }
                    ns.ctl_list.setItems(items);
                    if (items[0]) {
                        ns.ctl_list.fireItemClickEvent(items[0].id);
                    }
                }
            ;

            ood.asyRun(function () {
                ood.Ajax(CONF.getInnerImgServcie, {
                    projectName: SPA.curProjectName,
                    path: 'img'
                }, callback, function (txt) {

                }, null, null, {method: 'POST'}).start();
            });
        },

        getImagePreTag: function (type) {
            return type == "online" ? CONF.onlineGalleryPath :
                type == "local" ? CONF.serviceType == "node-webkit" ? CONF.adjustProtocol('') : '' :
                    type == "inner" ? CONF.serviceType == "node-webkit" ? CONF.adjustProtocol('') : '' :
                        "";
        }
        ,
        _ctl_list_onitemselected: function (profile, item, e, src, type) {
            var ns = this;
            ns._curPage = 1;
            ns._item = item;
            ns._filter = null;
            ns._refreshContent();
        }
        ,
        _refreshContent: function () {
            var ns = this,
                curPage = ns._curPage || 1,
                item = ns._item,
                newValue = ns.ctl_input1.getValue(),
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                pretag = ns.getImagePreTag(tabpage),
                callback = function (txt) {
                    ns.ctl_gallery.free();
                    var obj = txt;

                    var fillImgs = function (imgs) {
                        ood.arr.each(imgs, function (o, i) {
                            if (!/^.+\.(gif|jpg|jpeg|bmp|png)$/.test(o.name))
                                return;
                            if (newValue && !new RegExp(".*" + newValue + ".*").test(o.name)) {
                                return;
                            } else {
                                o.location = o.location ? o.location : o.path;
                                items.push({
                                    id: o.location,
                                    caption: '',
                                    image: o.location,
                                    comment: o.name, tips: o.name
                                });
                            }
                        });
                    };

                    if (!obj || obj.requestStatus == -1 || obj.error)
                        ood.message(ood.get(obj, ['error', 'errdes']) || 'no response!');
                    else {
                        var sum = obj.data && obj.data.sum;
                        if (sum) {
                            var pages = Math.ceil(sum / 100);
                            ns.ctl_pagebar1.setValue("1:" + curPage + ":" + pages);
                        }
                        var items = [];


                        if (obj.data.imgs) {
                            fillImgs(obj.data.imgs);
                        } else {
                            ood.arr.each(obj.data.sub || obj.data, function (sub, i) {
                                fillImgs(sub.imgs);
                            });
                        }


                        ns.ctl_gallery.resetValue();
                        ns.ctl_gallery.setItems(items);
                    }
                };


            ns.ctl_gallery.setImgWidth(item.imageWidth || 64);
            ns.ctl_gallery.setImgHeight(item.imageHeight || 64);
            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_gallery.busy(true);
            ood.Ajax(CONF.getSelImgByPathServcie, {
                projectName: SPA.curProjectName,
                imgConfigId: item.id,
                path: item.path,
            }, callback, function (txt) {

            }, null, null, {method: 'POST'}).start();
            if (item.sub && !item._checked)
                ns.ctl_list.toggleNode(item.id, true);
        }
        ,

        _reload: function (path) {

            var ns = this,
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                callback = function (txt) {
                    var obj = txt;
                    if (!obj || obj.requestStatus == -1 || obj.error)
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
                        if (obj.data.files || obj.data.imgs || obj.data) {
                            ood.arr.each(obj.data.files || obj.data, function (o, i) {
                                if (o.name.charAt(0) != ".'" && !ood.str.startWith(o.name, CONF.ood_page_tpl_tag))
                                    items.push(
                                        {
                                            id: o.location,
                                            name: o.name,
                                            // sub: true,
                                            caption: ood.get(map, [o.name, 'caption', ood.getLang()]) || o.name,
                                            imageWidth: ood.get(map, [o.name, 'width']) || ood.get(conf, ['imageSize', 'width']),
                                            imageHeight: ood.get(map, [o.name, 'height']) || ood.get(conf, ['imageSize', 'height'])
                                        });
                            });
                        }
                        if (!ood.isEmpty(sortby)) {
                            ood.arr.stableSort(items, function (x, y) {
                                x = sortby[x.name] || -1;
                                y = sortby[y.name] || -1;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        } else {
                            ood.arr.stableSort(items, function (x, y) {
                                x = x.name;
                                y = y.name;
                                return x > y ? 1 : x == y ? 0 : -1;
                            });
                        }

                    }
                };

            if (path) {
                ood.Ajax(CONF.getSelImgByPathServcie, {
                    projectName: SPA.curProjectName,
                    path: (path),
                }, callback, function (txt) {

                })
            } else {
                ood.Ajax(CONF.getSelImgServcie, {
                    projectName: SPA.curProjectName
                }, callback, function (txt) {

                }, null, null, {method: 'POST'}).start();
            }


        }
        ,

        _toolbar_onclick: function (profile, item, group, e, src) {
            var ns = this;
            switch (item.id) {
                case 'import':
                    ood.showModule("RAD.resource.ImageTree", function () {
                        this.setData({projectName: SPA.curProjectName});
                        this.show();
                        this.initData();
                    });
                    break;
                case 'refresh':
                    ns._refreshContent();
                    break;
                case 'upload':
                    var pPath = "img";
                    var uploadPath = CONF.uploadPath;
                    var dio = ood.create("ood.UI.Dialog")
                        .setLeft("8.333333333333334em")
                        .setTop("5.833333333333333em")
                        .setWidth("40.833333333333336em")
                        .setHeight("30em")
                        .setIframeAutoLoad(uploadPath + "?uploadUrl=" + CONF.upload + "&filter=jpg;gif;png;jpeg;bmp&projectName=" + SPA.curProjectName + "&uploadpath=" + pPath)
                        .setCaption("添加文件")
                        .setMaxBtn(false)
                        .beforeClose(function () {
                            //ood.tryF(ns.properties.onOK, [ns, "{/}img/"], ns.host)
                            ns._refreshContent();
                            //ood.tryF(ns.properties.onOK, [ns, "img/"], ns.host)
                        })
                        .setMaxBtn(false);
                    dio.show();
            }

        }
        ,

        _ctl_list_ongetcontent: function (profile, item, setsub) {
            var ns = this,
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue();
            if (item) {
                this._reload(item.id);
            } else {
                this._reload();
            }

        }
        ,
        _ctl_gallery_ondblclick: function (profile, item, e, src) {
            this._selectImage(item);
        }
        ,
        _ctl_pagebar1_onclick: function (profile, page) {
            var ns = this;
            // set to this page
            ns._curPage = page;
            ns._refreshContent();
        }
        ,
        _ctl_input1_onchange: function (profile, oldValue, newValue) {
            var ns = this, uictrl = profile.boxing();
            ood.resetRun("image_search", function () {
                ns._refreshContent();
            }, 1000);
        }
        ,
        _ctl_input1_oncommand: function (profile, src) {
            profile.boxing().setUIValue("");
        }
    }
})
;
