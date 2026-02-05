ood.Class('RAD.ProjectSetting', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog10")
                    .setLeft("5em")
                    .setTop("0.8333333333333334em")
                    .setWidth("56.666666666666664em")
                    .setHeight("31.666666666666668em")
                    .setZIndex(1002)
                    .setCaption("$(RAD.menu.Project Setting)")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setModal(true)
            );

            host.ood_ui_dialog10.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block11")
                    .setDock("bottom")
                    .setLeft("30em")
                    .setTop("0em")
                    .setHeight("3em")
                    .setRight("0em")
                    .setBottom("0em")
                    .setBorderType("flat")
            );

            host.ood_ui_block11.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "_btn_tocode")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("0.4166666666666667em")
                    .setWidth("7.5em")
                    .setCaption("$(RAD.projectSet.Code View)")
                    .onClick("__btn_tocode_onclick")
            );

            host.ood_ui_block11.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "_btn_ok")
                    .setDirtyMark(false)
                    .setLeft("45.75em")
                    .setTop("0.4166666666666667em")
                    .setWidth("6.583333333333333em")
                    .setCaption("$(RAD.ok)")
                    .onClick("__btn_ok_onclick")
            );

            host.ood_ui_block11.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "_btn_cancel")
                    .setDirtyMark(false)
                    .setLeft("35.75em")
                    .setTop("0.4166666666666667em")
                    .setWidth("6.583333333333333em")
                    .setCaption("$(RAD.cancel)")
                    .onClick("__btn_cancel_onclick")
            );

            host.ood_ui_dialog10.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host, "ood_ui_tabs2")
                    .setItems([
                        {
                            "id": "a",
                            "caption": "$(RAD.builder.Basic Setting)",
                            "imageClass": ""
                        },
                        {
                            "id": "b",
                            "caption": "$(RAD.builder.Font Icons)"
                        },
                        {
                            "id": "e",
                            "caption": "$(RAD.designer.Constants)"
                        },
                        {
                            "id": "c",
                            "caption": "$(RAD.designer.Global Functions)"
                        },
                        {
                            "id": "d",
                            "caption": "$(RAD.designer.Global Data)"
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setValue("a")
                    .onIniPanelView("_ood_ui_tabs2_oninipanelview")
            );

            host.ood_ui_tabs2.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group6")
                    .setLeft("15.333333333333334em")
                    .setTop("1.1666666666666667em")
                    .setWidth("14.166666666666666em")
                    .setHeight("20em")
                    .setCaption("$(RAD.designer.conf.canvas.theme)")
                    .setToggleBtn(false)
                , "a");

            host.ood_ui_group6.append(
                ood.create("ood.UI.RadioBox")
                    .setHost(host, "ctl_lappearance")
                    .setDirtyMark(false)
                    .setDock("fill")
                    .setItemRow(true)
            );

            host.ood_ui_tabs2.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group16")
                    .setLeft("30.333333333333332em")
                    .setTop("1.1666666666666667em")
                    .setWidth("24.166666666666668em")
                    .setHeight("20em")
                    .setOverflow("hidden")
                    .setCaption("$(RAD.builder.Page Background)")
                    .setToggleBtn(false)
                , "a");

            host.ood_ui_group16.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_bg_clr")
                    .setDirtyMark(false)
                    .setLeft("0.8333333333333334em")
                    .setTop("0.8333333333333334em")
                    .setWidth("22.416666666666668em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("12em")
                    .setLabelCaption("$(RAD.custom_dlg.background$-color)")
                    .setType("color")
            );

            host.ood_ui_group16.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_bg_image")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("4.166666666666667em")
                    .setWidth("22.416666666666668em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("12em")
                    .setLabelCaption("$(RAD.custom_dlg.background$-image)")
                    .setType("popbox")
                    .beforeComboPop("__bg_image_beforepopshow")
            );

            host.ood_ui_group16.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_bg_position")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("7.5em")
                    .setWidth("22.416666666666668em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("12em")
                    .setLabelCaption("$(RAD.custom_dlg.background$-position)")
            );

            host.ood_ui_group16.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_bg_repeat")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("10.833333333333334em")
                    .setWidth("22.416666666666668em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("12em")
                    .setLabelCaption("$(RAD.custom_dlg.background$-repeat)")
            );

            host.ood_ui_group16.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_bg_attachment")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("14.166666666666666em")
                    .setWidth("22.416666666666668em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("12em")
                    .setLabelCaption("$(RAD.custom_dlg.background$-attachment)")
            );

            host.ood_ui_tabs2.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group3")
                    .setLeft("0.8333333333333334em")
                    .setTop("1.1666666666666667em")
                    .setWidth("13.666666666666666em")
                    .setHeight("20em")
                    .setOverflow("hidden")
                    .setCaption("$(RAD.designer.conf.canvas.Design View)")
                    .setToggleBtn(false)
                , "a");

            host.ood_ui_group3.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_viewsize")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("5.833333333333333em")
                    .setWidth("11.666666666666666em")
                    .setHeight("4.083333333333333em")
                    .setTabindex(-1)
                    .setLabelSize("2em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.designer.conf.canvas.View Size) (px × px)")
                    .setLabelHAlign("left")
                    .beforeUIValueSet("__viewsize_beforeuivalueset")
            );

            host.ood_ui_group3.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "_touchDevice")
                    .setDirtyMark(false)
                    .setLeft("0.5833333333333334em")
                    .setTop("15.833333333333334em")
                    .setWidth("11.833333333333334em")
                    .setCaption("$(RAD.designer.conf.canvas.Mobile App)")
            );

            host.ood_ui_group3.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_SpaceUnit")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("0.8333333333333334em")
                    .setWidth("11.666666666666666em")
                    .setHeight("4.083333333333333em")
                    .setLabelSize("2em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.designer.conf.canvas.Space Unit)")
                    .setLabelHAlign("left")
                    .setType("listbox")
                    .setItems([
                        {
                            "id": "em",
                            "caption": "em"
                        },
                        {
                            "id": "px",
                            "caption": "px"
                        }
                    ])
            );

            host.ood_ui_group3.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_zoom")
                    .setDirtyMark(false)
                    .setLeft("0.75em")
                    .setTop("10.833333333333334em")
                    .setWidth("11.666666666666666em")
                    .setHeight("4.083333333333333em")
                    .setLabelSize("2em")
                    .setLabelPos("top")
                    .setLabelCaption("$RAD.designer.conf.canvas.zoom")
                    .setLabelHAlign("left")
            );

            host.ood_ui_tabs2.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_ui_treegrid3")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setEditable(true)
                    .setRowHandlerWidth("2.5em")
                    .setHeader([
                        {
                            "id": "id",
                            "caption": "ID",
                            "width": "8em",
                            "type": "input"
                        },
                        {
                            "id": "href",
                            "caption": "href",
                            "width": "18em",
                            "type": "input"
                        },
                        {
                            "id": "integrity",
                            "caption": "integrity",
                            "width": "18em",
                            "flexSize": true,
                            "type": "input"
                        },
                        {
                            "id": "disabled",
                            "caption": "disabled",
                            "width": "6em",
                            "type": "checkbox"
                        }
                    ])
                    .setTagCmds([
                        {
                            "id": "del",
                            "type": "text",
                            "location": "left",
                            "itemClass": "oodcon ood-uicmd-close"
                        }
                    ])
                    .setHotRowMode("show")
                    .beforeHotRowAdded("_ood_ui_treegrid3_beforehotrowadded")
                    .onCmd("_ood_ui_treegrid3_oncmd")
                , "b");

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events: {
            "onReady": "_page_onready",
            "onDestroy": "_destroy"
        },
        iniExModules: function (module) {
            var parent = this;
            ood.newModule('RAD.FunctionsEditor', function () {
                var ns = this;
                ns.setProperties({
                    type: "golbal",
                    page: {},
                    functions: SPA.curProjectConfig.$GlobalFunctions || {}
                });

                parent.ood_ui_tabs2.append(ns.ood_main, "c");
                //ext module
                parent._funseditor = ns;
            });
            ood.newModule('ood.Module.JSONEditor', function () {
                var ns = this;
                ns.setEvents({
                    "onchange": function (module) {
                        parent._dataDirty = 1;
                    }
                }).setValue(SPA.curProjectConfig.$GlobalData || {});

                parent.ood_ui_tabs2.append(ns, "d");
                //ext module
                parent._dataseditor = ns;
            });
            ood.newModule('ood.Module.JSONEditor', function () {
                var ns = this;
                ns.setProperties({
                    keyCaption: "Constant Name",
                    valueCaption: "Constant Value",
                    multiLineValue: false,
                    notree: true
                });
                ns.setEvents({
                    "onchange": function (module) {
                        parent._constDirty = 1;
                    }
                }).setValue(SPA.curProjectConfig.$ConstantData || {});

                parent.ood_ui_tabs2.append(ns, "e");
                //ext module
                parent._constanteditor = ns;
            });
        },
        _destroy: function () {
            var ns = this;
            ns._funseditor.destroy();
            ns._dataseditor.destroy();
            ns._constanteditor.destroy();
        },
        _page_onready: function (module, threadid) {
            var ns = this,
                items = [];
            delete ns._funsDirty;
            delete ns._dataDirty;
            delete ns._constDirty;



            ns._viewsize.setItems(["480 × 800", "480 × 854", "540 × 960", "640 × 960","640 × 1136","720 × 1280", "750 × 1334","1080 × 1920", "800 × 1280", "800 × 600",  "1024 × 768",  "1280 × 768", "1280 × 1024", "1360 × 768"]);
            ns._zoom.setItems(CONF.designer_zoom);
            ns.ood_ui_tabs2.setUIValue('a');

            ood.arr.each(CONF.designer_themes2, function (o) {
                if (!o) return;
                items.push({
                    id: o,
                    caption: ood.str.initial(o)
                });
            });
            ns.ctl_lappearance.setItems(items).setValue("default", true);

            ns._bg_position.setItems(CONF.designer_background_position);
            ns._bg_repeat.setItems(CONF.designer_background_repeat);
            ns._bg_attachment.setItems(CONF.designer_background_attachment);

            var conf = SPA.curProjectConfig, json, t;
            if ((json = conf.$PageAppearance)) {
                if (json.theme) {
                    ns.ctl_lappearance.setValue(json.theme, true);
                }
                if (json.zoom) {
                    ns._zoom.setValue(json.zoom, true);
                }
                if (json.background && ood.isHash(json.background)) {
                    t = json.background;
                    ns._bg_clr.setValue(t["background-color"] || "", true);
                    ns._bg_image.setValue(t["background-image"] || "", true);
                    ns._bg_position.setValue(t["background-position"] || "", true);
                    ns._bg_repeat.setValue(t["background-repeat"] || "", true);
                    ns._bg_attachment.setValue(t["background-attachment"] || "", true);
                }
            }
            if ((json = conf.$FontIconsCDN)) {
                var rows = [];
                ood.each(json, function (o, i) {
                    rows.push([i, o.href, o.integrity, !!o.disabled]);
                });
                ns.ood_ui_treegrid3.setRows(rows);
            } else {
                // 设置默认的Font Awesome 6 CDN配置
                var defaultFontIcons = [
                    ['Font Awesome 6', 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css', '', false]
                ];
                ns.ood_ui_treegrid3.setRows(defaultFontIcons);
            }

            // viewsize and touchDevice must have default value
            var su = ood.SpaceUnit, mf = false, vsw = 800, vsh = 600;
            if ((json = conf.$DevEnv)) {
                su = json.SpaceUnit || ood.SpaceUnit;
                if ((json = json.designViewConf)) {
                    if (json.touchDevice)
                        mf = true;
                    if (json.width)
                        vsw = parseInt(json.width, 10) || 800;
                    if (json.height)
                        vsh = parseInt(json.height, 10) || 600;
                }
            }
            ns._viewsize.resetValue(vsw + ' × ' + vsh);
            ns._touchDevice.resetValue(mf);
            ns._SpaceUnit.resetValue(su);
        },
        __bg_image_beforepopshow: function (profile, src, type) {
            var ns = this, uictrl = profile.boxing();
            ood.ModuleFactory.getCom('RAD.ImageSelector', function () {
                this.setProperties({
                    onOK: function (obj, path) {
                        if (path && SPA.curProjectPath)
                            path = path.replace(/\\/g, "/").replace(SPA.curProjectPath.replace(/\\/g, "/") + "/", "{/}");
                        uictrl.setUIValue('url(' + path + ')');
                        uictrl.activate(true);
                    }
                });
                this.show();
            });
        },
        __btn_cancel_onclick: function (profile, e, src, value) {
            var ns = this;
            ns.ood_ui_dialog10.close();
        },


        _refreshProjectConfig: function (setting, callback) {

            var ns = this,
                path = "oodconf.jsx",
                txt, json, arr, reg,
                dftHash = {
                    'background-image': 'url(' + ood.getPath('/RAD/img/designer/', 'bg.gif') + ')',
                    'background-position': 'left top',
                    'background-repeat': 'repeat',
                    "background-color": '',
                    "background-attachment": ''
                },
                cover = function (scr) {
                    scr = ood.replace(scr, [
                        [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'], //regexp
                        [/"(\\.|[^"\\])*"/, '$0'], //""
                        [/'(\\.|[^'\\])*'/, '$0'], //''
                        [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//, '']
                    ]);
                    // Page Appearance
                    arr = scr.split(/\s*\/\/ \[\[Page Appearance\s*\n|\n\s*\/\/ \]\]Page Appearance\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.ini\.\$PageAppearance\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.ini.$PageAppearance = ...");
                            return;
                        }
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$PageAppearance = json;

                            if (json.theme) {
                                ns.tabsMain.setSandboxTheme(json.theme, true, '[class~="ood-designer-inner-control"]');
                            }
                            if (json.background && ood.isHash(json.background)) {
                                var hash = ood.clone(dftHash);
                                ood.each(json.background, function (o, i) {
                                    hash[i] = ood.adjustRes(o);
                                });

                                ood.CSS.addStyleSheet(ood.UI.buildCSSText({
                                    '.ood-designer-root .ood-designer-canvas': hash
                                }), 'ood.UI.design', null, true);
                            }
                        }
                    }


                    // Font Icons CDN
                    arr = scr.split(/\s*\/\/ \[\[Font Icons CDN\s*\n|\n\s*\/\/ \]\]Font Icons CDN\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.ini\.\$FontIconsCDN\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.ini.$FontIconsCDN = ...");
                            return;
                        }
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$FontIconsCDN = json;
                            // for CDN font icons
                            ood.each(json, function (o, i) {
                                if (o.href) {
                                    var attr = {crossorigin: 'anonymous'};
                                    ood.merge(attr, o, function (v, j) {
                                        return j !== 'href'
                                    });
                                    ood.CSS.includeLink(ood.adjustRes(o.href), 'ood_app_fscdn-' + i, false, attr);
                                }
                            });
                        }
                    }

                    // Element Style
                    arr = scr.split(/\s*\/\/ \[\[Element Style\s*\n|\n\s*\/\/ \]\]Element Style\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.ini\.\$ElementStyle\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.ini.$ElementStyle = ...");
                            return;
                        }
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$ElementStyle = json;
                            ood.CSS.setStyleRules(".ood-desinger-canvas .ood-custom", json, true);
                        }
                    }

                    // Default Prop
                    arr = scr.split(/\s*\/\/ \[\[Default Prop\s*\n|\n\s*\/\/ \]\]Default Prop\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.ini\.\$DefaultProp\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.ini.$DefaultProp = ...");
                            return;
                        }
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$DefaultProp = json;

                            var allp = {}, ctl;
                            ood.each(json, function (v, k) {
                                if (/^ood.UI\./.test(k) && ood.isHash(v) && (ctl = ood.get(window, k.split('.')))) {
                                    ctl.__resetDftProp_in_Desinger = v;
                                } else {
                                    allp[k] = v;
                                }
                            });
                            if (!ood.isEmpty(allp)) {
                                ood.UI.__resetDftProp_in_Desinger = allp;
                            }
                        }
                    }

                    // Develop Env Setting
                    arr = scr.split(/\s*\/\/ \[\[Develop Env Setting\s*\n|\n\s*\/\/ \]\]Develop Env Setting\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.ini\.\$DevEnv\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.ini.$DevEnv = ...");
                            return;
                        }
                        // cover
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$DevEnv = json;

                            //view size
                            var size = {}, mf, su;
                            if ((size.width = ood.get(json, ['designViewConf', 'width'])) &&
                                (size.height = ood.get(json, ['designViewConf', 'height']))
                            ) {
                                ood.each(RAD.Designer.getAllInstance(), function (page) {
                                    if (page.canvas) {
                                        page.setViewSize(size);
                                        // for themes: classic webflat
                                        if (page._cls) {
                                            ood.asyRun(function () {
                                                if (page._cls) page.refreshView(page._cls);
                                            }, 100);
                                        }
                                    }
                                });
                            }

                            if (mf = ood.get(json, ['designViewConf', 'touchDevice'])) {
                                ood.each(RAD.Designer.getAllInstance(), function (page) {
                                    if (page.canvas)
                                        page.setMobileFrame(mf);
                                });
                            }
                            if (su = ood.get(json, ['SpaceUnit'])) {
                                ood.each(RAD.Designer.getAllInstance(), function (page) {
                                    if (page.canvas) {
                                        // for themes: classic webflat
                                        if (page._cls) {
                                            ood.asyRun(function () {
                                                if (page._cls) page.refreshView(page._cls);
                                            }, 100);
                                        }
                                    }
                                });
                            }
                        }
                    }

                    // Global Functions
                    arr = scr.split(/\s*\/\/ \[\[Global Functions\s*\n|\n\s*\/\/ \]\]Global Functions\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.\$cache\.functions\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.$cache.functions = ...");
                            return;
                        }

                        // cover
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$GlobalFunctions = json;
                        }
                    }

                    // Global Data
                    arr = scr.split(/\s*\/\/ \[\[Global Data\s*\n|\n\s*\/\/ \]\]Global Data\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.\$cache\.data\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.$cache.data = ...");
                            return;
                        }

                        // cover
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$GlobalData = json;
                        }
                    }


                    // Constant Data
                    arr = scr.split(/\s*\/\/ \[\[Constant Data\s*\n|\n\s*\/\/ \]\]Constant Data\s*/);
                    if (arr.length == 3) {
                        txt = arr[1];
                        json = null;

                        try {
                            reg = /^\s*ood.constant\s*\=\s*/;
                            if (reg.test(txt)) {
                                txt = ood.replace(txt, [
                                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'],
                                    [/"(\\.|[^"\\])*"/, '$0'],
                                    [/'(\\.|[^'\\])*'/, '$0'],
                                    [reg, ''],
                                    [/;\s*$/, ''],
                                    [/\/\/[^\n]*/, '']
                                ]);
                                json = ood.unserialize(txt);
                            }
                        } catch (e) {
                            ood.alert(e + "<br\><br\>File => " + path + "<br\><br\>at : ood.constant = ...");
                            return;
                        }

                        // cover
                        if (typeof json === "object" && json !== null) {
                            ns.curProjectConfig.$ConstantData = json;
                        }
                    }
                };

            // cached in memory
            ns.curProjectConfig = {};

            if (ood.isSet(setting)) {
                ns._resetPrjEnv();
                cover(setting || "");
                ood.tryF(callback);
            } else {
                // get file sync

                ood.Ajax(CONF.getProjectConfigService, {projectName: SPA.curProjectName}, function (scr) {
                    ns._resetPrjEnv();
                    cover(scr || "");
                    ood.tryF(callback);
                    //
                }, function (e) {

                    ns._resetPrjEnv();
                    ood.tryF(callback);
                    ood.alert("Missing file : '" + path + "'.");
                }, null, {rspType: "text", method: 'POST'}).start();
            }
            return ns.curProjectConfig;
        },

        __btn_ok_onclick: function (profile, e, src, value) {
            var ns = this, collections = [],
                path = "oodconf.jsx",
                json = {background: {}},
                bg = json.background,
                arr, skinCode, fiCode, vscode, funscode, datacode, concode, t,
                saveConifig = function (newText, skinCode, fiCode, vscode, funscode, datacode, concode) {
                    // remove all /**/
                    newText = ood.replace(newText, [
                        [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/, '$0'], //regexp
                        [/"(\\.|[^"\\])*"/, '$0'], //""
                        [/'(\\.|[^'\\])*'/, '$0'], //''
                        [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//, ''],
                        [/(\r\n|\r)/, "\n"]
                    ]);

                    skinCode = skinCode || "";
                    fiCode = fiCode || "";
                    vscode = vscode || "";
                    funscode = funscode || "";
                    datacode = datacode || "";
                    concode = concode || "";

                    // Page Appearance
                    arr = newText.split(/\s*\/\/ \[\[Page Appearance|\n\s*\/\/ \]\]Page Appearance\s*/);
                    if (arr.length == 3) {
                        newText = arr[0] + "\n\n// [[Page Appearance\n" + skinCode + "\n// ]]Page Appearance\n" + arr[2];
                    } else {
                        newText += "\n\n// [[Page Appearance\n" + skinCode + "\n// ]]Page Appearance\n";
                    }

                    // Font Icons CDN
                    arr = newText.split(/\s*\/\/ \[\[Font Icons CDN|\n\s*\/\/ \]\]Font Icons CDN\s*/);
                    if (arr.length == 3) {
                        newText = arr[0] + "\n\n// [[Font Icons CDN\n" + fiCode + "\n// ]]Font Icons CDN\n" + arr[2];
                    } else {
                        newText += "\n\n// [[Font Icons CDN\n" + fiCode + "\n// ]]Font Icons CDN\n";
                    }

                    // Develop Env Setting
                    arr = newText.split(/\s*\/\/ \[\[Develop Env Setting|\n\s*\/\/ \]\]Develop Env Setting\s*/);
                    if (arr.length == 3) {
                        newText = arr[0] + "\n\n// [[Develop Env Setting\n" + vscode + "\n// ]]Develop Env Setting\n" + arr[2];
                    } else {
                        newText += "\n\n// [[Develop Env Setting\n" + vscode + "\n// ]]Develop Env Setting\n";
                    }

                    // Global Functions
                    if (funscode) {
                        arr = newText.split(/\s*\/\/ \[\[Global Functions|\n\s*\/\/ \]\]Global Functions\s*/);
                        if (arr.length == 3) {
                            newText = arr[0] + "\n\n// [[Global Functions\n" + funscode + "\n// ]]Global Functions\n" + arr[2];
                        } else {
                            newText += "\n\n// [[Global Functions\n" + funscode + "\n// ]]Global Functions\n";
                        }
                    }

                    // Global Data
                    if (datacode) {
                        arr = newText.split(/\s*\/\/ \[\[Global Data|\n\s*\/\/ \]\]Global Data\s*/);
                        if (arr.length == 3) {
                            newText = arr[0] + "\n\n// [[Global Data\n" + datacode + "\n// ]]Global Data\n" + arr[2];
                        } else {
                            newText += "\n\n// [[Global Data\n" + datacode + "\n// ]]Global Data\n";
                        }
                    }


                    // Constant Data
                    if (concode) {
                        arr = newText.split(/\s*\/\/ \[\[Constant Data|\n\s*\/\/ \]\]Constant Data\s*/);
                        if (arr.length == 3) {
                            newText = arr[0] + "\n\n// [[Constant Data\n" + concode + "\n// ]]Constant Data\n" + arr[2];
                        } else {
                            newText += "\n\n// [[Constant Data\n" + concode + "\n// ]]Constant Data\n";
                        }
                    }

                    // save file
                    CONF.saveFiles([{
                        id: path,
                        paras: {
                            projectName: SPA.curProjectName,
                            hashCode: ood.id(),
                            curProjectPath: SPA.curProjectPath,
                            path: path,
                            fileType: 'EUFile',
                            content: newText,
                            jscontent: newText

                        },
                        onSuccess: function () {
                            // close dialog
                            ns.ood_ui_dialog10.close();
                            // refresh setting to UI
                            SPA.refreshProjectConfig(newText);

                            SPA.closeFile(path);
                        },
                        onFail: function (txt) {
                            ood.message(txt);
                        }
                    }]);
                };

            json.theme = ns.ctl_lappearance.getUIValue();
            json.zoom = ns._zoom.getUIValue() || "";
            if (!json.zoom || parseFloat(json.zoom) == "1") delete json.zoom;

            if ((t = ns._bg_clr.getUIValue())) bg["background-color"] = t;
            if ((t = ns._bg_image.getUIValue())) bg["background-image"] = t;
            if ((t = ns._bg_position.getUIValue())) bg["background-position"] = t;
            if ((t = ns._bg_repeat.getUIValue())) bg["background-repeat"] = t;
            if ((t = ns._bg_attachment.getUIValue())) bg["background-attachment"] = t;

            if (ood.isEmpty(bg)) {
                delete json.background;
            }
            skinCode = "ood.ini.$PageAppearance = " + ood.Coder.formatText(ood.stringify(json), 'js');
            skinCode = skinCode.replace(/\}\s*$/, '};');

            var rows = ns.ood_ui_treegrid3.getRows('min');
            if (rows && rows.length) {
                ood.filter(rows, function (o) {
                    return !!(o[0] && o[1]);
                });
            }
            if (rows && rows.length) {
                json = {};
                ood.arr.each(rows, function (o) {
                    json[o[0]] = {};
                    json[o[0]].href = o[1];
                    json[o[0]].integrity = o[2];
                    json[o[0]].disabled = !!o[3];
                });
                fiCode = "ood.ini.$FontIconsCDN = " + ood.Coder.formatText(ood.stringify(json), 'js');
                fiCode = fiCode.replace(/\}\s*$/, '};');
            }

            json = {designViewConf: {}};
            if (ns._touchDevice.getUIValue()) json.designViewConf.touchDevice = true;
            if ((t = ns._SpaceUnit.getUIValue()) && t != ood.SpaceUnit) json.SpaceUnit = t;
            if ((t = ns._viewsize.getUIValue())) {
                if ((t = ns._viewsize_reg.exec(t))) {
                    json.designViewConf.width = parseInt(t[1], 10);
                    json.designViewConf.height = parseInt(t[2], 10);
                }
            }
            vscode = "ood.ini.$DevEnv = " + ood.Coder.formatText(ood.stringify(json), 'js');
            vscode = vscode.replace(/\}\s*$/, '};');

            if (ns._funsDirty) {
                SPA.curProjectConfig.$GlobalFunctions = ns._funseditor.fireData(true);
                // funscode = "ood.$cache.functions = " + ood.Coder.formatText( ood.stringify(SPA.curProjectConfig.$GlobalFunctions), 'js') ;
                funscode = "ood.$cache.functions =" + ood.stringify(SPA.curProjectConfig.$GlobalFunctions);
                funscode = funscode.replace(/\}\s*$/, '};');
            }

            if (ns._dataDirty) {
                SPA.curProjectConfig.$GlobalData = ns._dataseditor.getValue(true);
                datacode = "ood.$cache.data = " + ood.Coder.formatText(ood.stringify(SPA.curProjectConfig.$GlobalData), 'js');
                datacode = datacode.replace(/\}\s*$/, '};');
            }

            if (ns._constDirty) {
                SPA.curProjectConfig.$ConstantData = ns._constanteditor.getValue(true);
                concode = "ood.constant = " + ood.Coder.formatText(ood.stringify(SPA.curProjectConfig.$ConstantData), 'js');
                concode = concode.replace(/\}\s*$/, '};');
            }
            // get original file sync
            CONF.getLocalFile("oodconf.jsx" + '?' + ood.rand(), function (scr) {
                saveConifig(scr || "", skinCode, fiCode, vscode, funscode, datacode, concode);
            }, function (e) {
                saveConifig("", skinCode, fiCode, vscode, funscode, datacode, concode);
            });
        },
        __btn_tocode_onclick: function (profile, e, src, value) {
            var ns = this;
            ns.ood_ui_dialog10.close();


            SPA.openFile("oodconf.jsx");
        },
        _ood_ui_treegrid3_beforehotrowadded: function (profile, cellMap) {
            if (cellMap.id && cellMap.href) {
                return true;
            }
            return null;
        },
        _ood_ui_treegrid3_oncmd: function (profile, row, cmdkey, e, src) {
            var ns = this, uictrl = profile.boxing();
            if (row) {
                uictrl.removeRows(row.id);
            } else {
                uictrl.removeAllRows();
            }
        },
        _viewsize_reg: /\s*([\d][\d]*)[^\d]+([\d][\d]*)\s*/,
        __viewsize_beforeuivalueset: function (profile, oldValue, newValue, force, tag) {
            var ns = this, uictrl = profile.boxing(),
                result;
            if ((result = ns._viewsize_reg.exec(newValue))) {
                return result[1] + ' × ' + result[2];
            }
            return false;
        },
        _ood_ui_tabs2_oninipanelview: function (profile, item) {
            var ns = this;
            if (item.id == 'c') {
                ns._funsDirty = 1;
            }
        }
    }
});
