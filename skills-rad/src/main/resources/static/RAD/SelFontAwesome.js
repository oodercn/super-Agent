ood.Class('RAD.SelFontAwesome', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog2")
                    .setLeft("1.6666666666666667em")
                    .setTop("1.6666666666666667em")
                    .setWidth("62.5em")
                    .setHeight("31.666666666666668em")
                    .setCaption("图标选择")
                    .setMinBtn(false)
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_panel9")
                    .setDock("left")
                    .setWidth("13.25em")
                    .setZIndex(1)
                    //   .setBorderType("none")
                    .setLeft("undefinedem")
                    .setRight("undefinedem")
                    .setBottom("undefinedem")
            );


            host.ood_ui_panel9.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "ood_ui_lst")
                    .setDirtyMark(false)
                    .setInitFold(false)
                    .setSingleOpen(true)
                    .setValue("allicon")
                    .setDock("fill")
                    .setTop("0em")
                    .setWidth("18.166666666666666em")
                    .onChange("_ood_ui_lst_onchange")
            );

            host.ood_ui_panel9.append(
                (new ood.UI.ToolBar())
                    .setHost(host, "servicetool")
                    // .setDisplay("none")
                    .setHandler(false)
                    .onClick("_toolbar_onclick")
                // .setCustomStyle({
                //     "ITEMS": "border-width:0 0 1px 0;"
                // }
                // )
            );

            var items = [{
                id: 'any', sub: [
                    {
                        id: 'refresh',
                        imageClass: 'ri ri-refresh-line',
                        caption: '刷新',
                        tips: '$RAD.tool2.refresh'
                    },
                    {
                        id: 'import',
                        imageClass: 'ri ri-upload-line',
                        caption: '导入',
                        tips: '$RAD.esdmenu.localicon'
                    }
                ]
            }];

            host.servicetool.setItems(items);

            host.ood_ui_panel9.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block4")
                    .setDock("top")
                    .setLeft("0em")
                    .setHeight("2.8333333333333335em")
                    .setBorderType("none")
                    .setTop("undefinedem")
                    .setRight("undefinedem")
                    .setBottom("undefinedem")
            );

            host.ood_ui_block4.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_ui_comboinput33")
                    .setDock("width")
                    .setLeft("0em")
                    .setTop("0.25em")
                    .setHeight("2.3333333333333335em")
                    .setDynCheck(true)
                    .setType("input")
                    .setImageClass("ri ri-filter-line")
                    .setCommandBtn("delete")
                    .onChange("_ood_ui_comboinput33_onchange")
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_div15")
                    .setDock("fill")
                    .setLeft("23.333333333333332em")
                    .setTop("0.8333333333333334em")
            );

            host.ood_ui_div15.append(
                ood.create("ood.UI.Gallery")
                    .setHost(host, "ood_ui_gallery1")
                    .setDirtyMark(false)
                    .setDock("fill")
                    .setIconFontSize('2em')
                    .setLeft("22.5em")
                    .setColumns(6)
                    .setTop("8.333333333333334em")
                    .setBorderType("none")
                    .setValue("a")
                    .onItemSelected("_ood_ui_gallery1_onitemselected")
            );

            host.ood_ui_div15.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block4")
                    .setDock("bottom")
                    .setLeft("13.333333333333334em")
                    .setHeight("2.8333333333333335em")
                    .setOverflow("hidden")
                    .setTop("undefinedem")
                    .setRight("undefinedem")
                    .setBottom("undefinedem")
            );

            host.ood_ui_block4.append(
                ood.create("ood.UI.StatusButtons")
                    .setHost(host, "ood_ui_statusbuttons2")
                    .setDirtyMark(false)
                    .setItems([{
                        "id": "normal",
                        "caption": "normal",
                        "imageClass": "ri ri-shield-line"
                    },
                        {
                            "id": "fa-rotate-90",
                            "caption": "Rotate 90",
                            "imageClass": "ri ri-shield-line fa-rotate-90"
                        },
                        {
                            "id": "fa-rotate-180",
                            "caption": "Rotate 180",
                            "imageClass": "ri ri-shield-line fa-rotate-180"
                        },
                        {
                            "id": "fa-rotate-270",
                            "caption": "Rotate 270",
                            "imageClass": "ri ri-shield-line fa-rotate-270"
                        },
                        {
                            "id": "fa-flip-horizontal",
                            "caption": "Flip Horizontal",
                            "imageClass": "ri ri-shield-line fa-flip-horizontal"
                        },
                        {
                            "id": "fa-flip-vertical",
                            "caption": "Flip Vertical",
                            "imageClass": "ri ri-shield-line fa-flip-vertical"
                        }])
                    .setDock("width")
                    .setLeft("0.16666666666666666em")
                    .setTop("0.08333333333333333em")
                    .setWidth("39.916666666666664em")
                    .setHeight("3.25em")
                    .setBorderType("none")
                    .setItemMargin("2px 4px")
                    .setConnected(true)
                    .setValue("normal")
                    .onItemSelected("_ood_ui_statusbuttons2_onitemselected")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        events: {
            "onReady": "_page_onready",
            "onRender": "_page_onrender"
        },
        _reload: function () {

            var ns = this;
            ns._db = {};
            addTodb = function (data) {
                if (data.glyphs) {
                    ood.each(data.glyphs, function (item) {
                        item.code = data.font_family + " " + data.css_prefix_text;
                    })
                    ns._db[data.id] = data.glyphs
                }
                ood.each(data.sub, function (item) {
                    addTodb(item)
                })

            };


            ns.ood_ui_statusbuttons2.setValue('normal');

            ood.Ajax(CONF.getSelFontServcie, {
                projectName: SPA.curProjectName
            }, function (txt) {
                ns.ood_ui_lst.setItems(txt.data);
                ood.each(txt.data, function (item) {
                    addTodb(item)
                })
                ns._refreshIcons(txt.data[0].id, ns._rotateType);
            }, function (txt) {

            }, null, null, {method: 'POST'}).start();
        }
        ,
        _page_onready: function (module, threadid) {
            this._reload();
        },

        _toolbar_onclick: function (profile, item, group, e, src) {

            var ns = this;
            switch (item.id) {
                case 'import':
                    ood.showModule("RAD.resource.FontTree", function () {
                        this.setEvents("onOk", function (lib, appearance, locale) {
                             ood.CSS.includeLink(path + 'theme.css', id);
                            ns._reload();
                            ood.message("添加成功，您需要刷新浏览器以便于更改！");
                        });
                        this.setData({projectName: SPA.curProjectName});
                        this.show();
                        this.initData();
                        this._fireEvent('afterShow');
                    });
                    break;
                case 'refresh':
                    ns._reload();
                    break;
            }

        },

        _page_onrender: function (module, threadid) {
            this.ood_ui_lst.fireItemClickEvent("allicon");
        },
        _ood_ui_lst_onchange: function (profile, oldValue, newValue, force, tag) {
            this._refreshIcons(newValue, this._rotateType);
        },

        _ood_ui_lst_onchange: function (profile, oldValue, newValue, force, tag) {
            this._refreshIcons(newValue, this._rotateType);
        },

        _refreshIcons: function (newValue, type) {
            var ns = this;
            var items = [];
            ood.arr.each(ns._db[newValue], function (s) {
                var comment = {comment: s.name, imageClass: s.code + s.font_class, caption: '', code: s.code};
                items.push(comment);
            });
            ns.ood_ui_gallery1.setItems(items);
        },
        _ood_ui_statusbuttons2_onitemselected: function (profile, item, e, src, type) {
            var ns = this;
            ns.ood_ui_gallery1.getSubNode('ICON', true).removeClass("fa-rotate-90 fa-rotate-180 fa-rotate-270 fa-flip-vertical fa-flip-horizontal");
            if ((ns._rotateType = item.id == 'normal' ? '' : ' ' + item.id)) {
                ns.ood_ui_gallery1.getSubNode('ICON', true).addClass(ns._rotateType);
            }
        },
        _ood_ui_gallery1_onitemselected: function (profile, item, e, src, type) {
            var ns = this, uictrl = profile.boxing();
            ns.fireEvent("onSel", [item.imageClass]);
            ns.destroy();
        },
        _ood_ui_comboinput33_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, uictrl = profile.boxing();
            ood.resetRun(ns.$xid + ':_ood_ui_comboinput33_onchange', function () {
                ns.ood_ui_lst.setUIValue(null);
                ns.ood_ui_lst.setDisabled(true);
                var results = [];
                if (newValue) {
                    ood.each(ns._db, function (o) {
                        ood.arr.each(o, function (s) {
                            if (s.code.indexOf(newValue) != -1
                                || s.unicode.indexOf(newValue) != -1
                                || s.name.indexOf(newValue) != -1
                                || s.font_class.indexOf(newValue) != -1
                            ) {
                                results.push(s);
                            }
                        });
                    });

                    var items = [];
                    ood.arr.each(results, function (s) {
                        var comment = {comment: s.name, imageClass: s.code + s.font_class, caption: '', code: s.code};
                        items.push(comment);
                    });
                    ns.ood_ui_gallery1.setItems(items);
                } else {
                    ns._setEmpty();
                }
            }, 500);
        },
        _setEmpty: function () {
            var ns = this;
            // dont trigger onchange event
            ns.ood_ui_comboinput33.setValue(null, true);
            ns.ood_ui_lst.setDisabled(false);
            ns.ood_ui_lst.fireItemClickEvent("allicon");
        }
    }
});
