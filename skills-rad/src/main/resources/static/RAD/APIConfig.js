ood.Class('RAD.APIConfig', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog2")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("66.667em")
                    .setHeight("43.333333333333336em")
                    .setCaption("$(RAD.api_dlg.Web API Caller)")
                    .setMinBtn(false)
                    .setMinWidth(780)
                    .setMinHeight(450)
                    .setModal(true)
                    .setConDockPadding({
                        "left": 4,
                        "top": 0,
                        "right": 4,
                        "bottom": 0
                    })
                    .setConDockSpacing({
                        "width": 0,
                        "height": 6
                    })
                    .beforeClose("_ood_ui_dialog2_beforeclose")
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane23")
                    .setDock("top")
                    .setWidth("8.334em")
                    .setHeight("2.8334em")
                    .setOverflow("hidden")
            );

            // host.ood_ui_pane23.append(
            //     ood.create("ood.UI.ComboInput")
            //         .setHost(host,"ood_url")
            //         .setDirtyMark(false)
            //         .setDock("top")
            //         .setDockMargin({
            //                 "left":0,
            //                 "top":3,
            //                 "right":0,
            //                 "bottom":4
            //             }
            //         )
            //         .setHeight("1.8333333333333333em")
            //         .setDynCheck(true)
            //         .setLabelSize(120)
            //         .setLabelCaption("$(RAD.Search)")
            //         .setType("none")
            //         .setImageClass("ood-icon-filter")
            //         .setCommandBtn("delete")
            //         .onChange("_ood_url_onChange")
            //         .onCommand("_ctl_input1_oncommand")
            // );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_url")
                    //  .setRequired(true)
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "left": 0,
                        "top": 0,
                        "right": 260,
                        "bottom": 0
                    })
                    .setDockMinW("33.334em")
                    .setLeft("7em")
                    .setTop("0.75em")
                    .setWidth("16em")
                    .setHeight("1.75em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption(ood.getRes('$RAD.apiconfig.remoteURL') || "远程RUL:")
                    .setType("helpinput")
                    .setImageClass("ood-icon-filter")
                    .setCommandBtn("pop")
                    .onHotKeypress("_ctl_ood_url_onhotkey")
                    .onChange("_ctl_ood_url_onchange")
                    .onCommand("_ctl_ood_url_oncommand")
                    .beforePopShow("_ctl_ood_url_beforshow")
                // .onChange("_ood_url_onChange")
            );

            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "ood_asyn")
                    .setDirtyMark(false)
                    //.setDisabled(true)
                    .setTop("0.91667em")
                    .setRight("0.5em")
                    .setCaption("$(RAD.api_dlg.Asynchronous)")
                    .setValue(true)
            );
            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "autoRun")
                    .setDirtyMark(false)
                    //.setDisabled(true)
                    .setTop("0.91667em")
                    .setRight("7.5em")
                    .setCaption(ood.getRes('RAD.apiconfig.autoRun') || "自动运行")
                    .setValue(false)
            );
            host.ood_ui_pane23.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "isAllform")
                    .setDirtyMark(false)
                    //.setDisabled(true)
                    .setTop("0.91667em")
                    .setRight("15em")
                    .setCaption(ood.getRes('RAD.apiconfig.cascade') || "级联")
                    .setValue(false)
            );

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_paneleft")
                    .setDock("left")
                    .setTop("5.8334em")
                    .setWidth("19.1667em")
                    .setHeight("32.5em")
            );

            host.ood_ui_paneleft.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane272")
                    .setDock("top")
                    .setDockIgnoreFlexFill(true)
                    .setLeft("0em")
                    .setWidth("20em")
                    .setPosition(true)
                    .setConDockPadding({
                        "left": 4,
                        "top": 4,
                        "right": 4,
                        "bottom": 4
                    })
                    .setConDockSpacing({
                        "width": 0,
                        "height": 4
                    })
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_reqtype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("2.5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Request Type)")
                    .setType("listbox")
                    .setItems([{
                        "id": "FORM",
                        "caption": "FORM"
                    },
                        {
                            "id": "JSON",
                            "caption": "JSON"
                        },
                        {
                            "id": "XML",
                            "caption": "XML"
                        },
                        {
                            "id": "SOAP",
                            "caption": "SOAP"
                        }])
                    .setValue("FORM")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_rsptype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Response Type)")
                    .setType("listbox")
                    .setItems([{
                        "id": "JSON",
                        "caption": "JSON"
                    },
                        {
                            "id": "TEXT",
                            "caption": "TEXT"
                        },
                        {
                            "id": "XML",
                            "caption": "XML"
                        },
                        {
                            "id": "SOAP",
                            "caption": "SOAP"
                        }])
                    .setValue("JSON")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_method")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("7.5em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Query Method)")
                    .setType("listbox")
                    .setItems([{
                        "id": "auto",
                        "caption": "auto"
                    },
                        {
                            "id": "GET",
                            "caption": "GET"
                        },
                        {
                            "id": "POST",
                            "caption": "POST"
                        },
                        {
                            "id": "PUT",
                            "caption": "PUT"
                        },
                        {
                            "id": "DELETE",
                            "caption": "DELETE"
                        }])
                    .setValue("auto")
            );

            host.ood_ui_pane272.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_proxytype")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Proxy Type)")
                    .setType("listbox")
                    .setItems([{
                        "id": "auto",
                        "caption": "auto"
                    },
                        {
                            "id": "AJAX",
                            "caption": "AJAX"
                        },
                        {
                            "id": "JSONP",
                            "caption": "JSONP"
                        },
                        {
                            "id": "XDMI",
                            "caption": "XDMI"
                        }])
                    .setValue("auto")
                    .afterValueSet("_ood_ui_comboinput207_aftervalueset")
            );

            host.ood_ui_paneleft.append(
                ood.create("ood.UI.Stacks")
                    .setHost(host, "ood_ui_foldingtabs1")
                    .setItems([{
                        "id": "a",
                        "caption": "$(RAD.api_dlg.Authorization)"
                    },
                        {
                            "id": "b",
                            "caption": "$(RAD.api_dlg.Request Header)"
                        },
                        {
                            "id": "c",
                            "caption": "$(RAD.api_dlg.Cookies Smulation)"
                        },
                        {
                            "id": "d",
                            "caption": "$(RAD.api_dlg.Other Query Options)"
                        }])
                    .setDockMargin({
                        "left": 0,
                        "top": 120,
                        "right": 0,
                        "bottom": 0
                    })
                    .setWidth("18.91667em")
                    .setHeight("25.5em")
                    .setValue("a")
            );

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "m_header")
                    .setProperties({
                        "keyCaption": "$(RAD.api_dlg.Key)",
                        "valueCaption": "$(RAD.api_dlg.Value)",
                        "multiLineValue": false,
                        "notree": true
                    })
                , "b");

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "m_cookies")
                    .setProperties({
                        "keyCaption": "$(RAD.api_dlg.Key)",
                        "valueCaption": "$(RAD.api_dlg.Value)",
                        "multiLineValue": false,
                        "notree": true
                    })
                , "c");

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "m_options")
                    .setProperties({
                        "keyCaption": "$(RAD.api_dlg.Key)",
                        "valueCaption": "$(RAD.api_dlg.Value)",
                        "multiLineValue": false,
                        "notree": true
                    })
                , "d");

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_token")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0.8334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.OAuth2Token)")
                    .setType("input")
                , "a");

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_username")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("3.334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.User Name)")
                    .setType("input")
                , "a");

            host.ood_ui_foldingtabs1.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ood_password")
                    .setDirtyMark(false)
                    .setLeft("0em")
                    .setTop("5.8334em")
                    .setWidth("18.334em")
                    .setLabelSize("8.334em")
                    .setLabelGap("0.334em")
                    .setLabelCaption("$(RAD.api_dlg.Password)")
                    .setType("input")
                , "a");

            host.ood_ui_dialog2.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane22")
                    .setDock("fill")
                    .setDockMargin({
                        "left": 6,
                        "top": 0,
                        "right": 0,
                        "bottom": 0
                    })
                    .setLeft("25.833333333333332em")
                    .setTop("9.1667em")
                    .setWidth("12.1667em")
                    .setHeight("12.91667em")
                    .setConDockPadding({
                        "left": 0,
                        "top": 0,
                        "right": 0,
                        "bottom": 2
                    })
                    .setConDockSpacing({
                        "width": 0,
                        "height": 4
                    })
            );

            host.ood_ui_pane22.append(
                ood.create("ood.UI.HTMLButton")
                    .setHost(host, "ood_ui_htmlbutton3")
                    .setDock("bottom")
                    .setDockOrder(3)
                    .setDockMargin({
                        "left": 6,
                        "top": 2,
                        "right": 6,
                        "bottom": 2
                    })
                    .setWidth("11em")
                    .setHeight("1.8334em")
                    .setHtml("$(RAD.api_dlg.Test the Web API)")
                    .onClick("_ood_ui_htmlbutton3_onclick")
            );

            host.ood_ui_pane22.append(
                ood.create("ood.UI.Layout")
                    .setType("vertical")
                    .setHost(host, "ood_ui_layout7")
                    .setItems([{
                        "id": "before",
                        "size": 100,
                        "min": 10,
                        "locked": false,
                        "folded": false,
                        "hidden": false,
                        "cmd": false,
                        "pos": "before"
                    },
                        {
                            "id": "main",
                            "size": 120,
                            "min": 10
                        }])
                    .setLeft("0em")
                    .setTop("0em")
                    .setFlexSize(true)
                    .setTransparent(true)
            );

            host.ood_ui_layout7.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group7")
                    .setDock("fill")
                    .setDockOrder(2)
                    .setLeft("5.8334em")
                    .setWidth("8.334em")
                    .setHeight("15em")
                    .setConDockPadding({
                        "left": 4,
                        "top": 4,
                        "right": 4,
                        "bottom": 4
                    })
                    .setConDockSpacing({
                        "width": 4,
                        "height": 0
                    })
                    .setCaption("$(RAD.api_dlg.Request Data Setting)")
                    .setToggleBtn(false)
                , "before");

            host.ood_ui_group7.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_pane15")
                    .setDock("left")
                    .setTop("0.8334em")
                    .setWidth("26em")
                    .setHeight("8.334em")
                    .setConDockSpacing({
                        "width": 0,
                        "height": 4
                    })
                    .setConDockFlexFill("height")
            );

            host.ood_ui_pane15.append(
                ood.create("ood.UI.Stacks")
                    .setHost(host, "ood_ui_foldingtabs11")
                    .setItems([
                        {
                            "id": "b",
                            "caption": "$(RAD.api_dlg.Request Datasource)",
                            "height": 180
                        }, {
                            "id": "c",
                            "caption": "$(RAD.api_dlg.Request Parameters)",
                            "height": 170,
                            "imageClass": ""
                        }])
                    .setLeft("9.58334em")
                    .setTop("0em")
                    .setWidth("8.41667em")
                    .setHeight("21em")
                    .setValue("b")
            );

            host.ood_ui_foldingtabs11.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_reqds")
                    .setDirtyMark(false)
                    .setShowDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("4em")
                    .setColSortable(false)
                    .setHeader([{
                        "id": "source",
                        "caption": "$(RAD.api_dlg.Datasource)",
                        "width": "6.667em",
                        "type": "label"
                    },
                        {
                            "id": "path",
                            "caption": "$(RAD.api_dlg.To Data Path)",
                            "width": "6.667em",
                            "flexSize": true,
                            "type": "input",
                            "editable": true
                        },

                        {
                            "id": "desc",
                            "caption": ood.getRes('RAD.apiconfig.paramUsage') || "参数用途",
                            "width": "6.667em",
                            "flexSize": true,
                            "type": "input",
                            "editable": false
                        }])
                    .setGrpCols({})
                    .setRows({})
                    .setTreeMode("none")
                    .setValue("")
                    .onChange("_ood_reqds_onchange")
                    .onRowSelected("_ontrows")
                    .beforeRowActive("_tg_beforerowactive")
                    .beforeCellUpdated("_acu")
                    .afterCellUpdated("_ood_reqds_bcellupdated")
                    .setCustomClass({
                        "HFMARK": "ood-uicmd-none"
                    })
                , "b");

            host.ood_ui_foldingtabs11.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "m_reqparams")
                    .setProperties({
                        "keyCaption": "$(RAD.api_dlg.Key)",
                        "valueCaption": "$(RAD.api_dlg.Value)",
                        "multiLineValue": false
                    })
                    .setEvents({
                        "onchange": "_m_reqparams_onchange"
                    })
                , "c");

            host.ood_ui_group7.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel44")
                    .setLeft("19.58334em")
                    .setTop("0.8334em")
                    .setWidth("24.5em")
                    .setHeight("17.083333333333332em")
                    .setConDockSpacing({
                        "width": 0,
                        "height": 4
                    })
                    .setCaption("$(RAD.api_dlg.Request Data)")
                    .setBorderType("none")
                    .setNoFrame(true)
                    .setCustomClass({
                        "INFO": "ood-uicmd-land"
                    })
            );

            host.ood_ui_panel44.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "ood_reqtext")
                    .setDisabled(true)
                    .setDock("fill")
                    .setLeft("6.667em")
                    .setTop("4.1667em")
                    .setHeight("10em")
                    .setLabelGap("0.334em")
                    .setMultiLines(true)
            );

            host.ood_ui_layout7.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_group10")
                    .setDock("fill")
                    .setLeft("6.667em")
                    .setWidth("8.334em")
                    .setHeight("19.1667em")
                    .setConDockPadding({
                        "left": 4,
                        "top": 4,
                        "right": 4,
                        "bottom": 4
                    })
                    .setConDockSpacing({
                        "width": 4,
                        "height": 0
                    })
                    .setCaption("$(RAD.api_dlg.Response Data Setting)")
                    .setToggleBtn(false)
                , "main");

            host.ood_ui_group10.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel45")
                    .setDock("left")
                    .setTop("1.667em")
                    .setWidth("30em")
                    .setHeight("17.08333em")
                    .setCaption("$(RAD.api_dlg.Response to Target)")
                    .setBorderType("flat")
                    .setNoFrame(true)
                    .setCustomClass({
                        "INFO": "ood-uicmd-land"
                    })
            );

            host.ood_ui_panel45.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_rsptarget")
                    .setDirtyMark(false)
                    .setShowDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("5em")
                    .setColSortable(false)
                    .setHeader(
                        [
                            {
                                "id": "name",
                                "caption": ood.getRes('RAD.apiconfig.componentName') || "组件名称",
                                "width": "10em",
                                "flexSize": true,
                                "type": "label",
                                "editable": true
                            },
                            {
                                "id": "path",
                                "caption": ood.getRes('RAD.apiconfig.mappingAddress') || "映射地址",
                                "width": "10em",
                                "flexSize": true,
                                "type": "input"
                            },
                            {
                                "id": "type",
                                "caption": ood.getRes('RAD.apiconfig.componentType') || "组件类型",
                                "width": "8em",
                                "flexSize": true,
                                "type": "label",
                                "editable": false
                            }
                        ])
                    //.setTreeMode("none")
                    .setValue("")
                    .onRowSelected("_ontrows2")
                    .beforeRowActive("_tg_beforerowactive")
                    .beforeCellUpdated("_acu")
                    .setCustomClass({
                        "HFMARK": "ood-uicmd-none"
                    })
            );

            host.ood_ui_group10.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ood_ui_panel17")
                    .setLeft("20.41667em")
                    .setTop("1.667em")
                    .setWidth("24.5em")
                    .setHeight("17.08333em")
                    .setConDockSpacing({
                        "width": 0,
                        "height": 4
                    })
                    .setCaption("$(RAD.api_dlg.Response Callback)")
                    .setNoFrame(true)
            );

            host.ood_ui_panel17.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "ood_rspfun")
                    .setDirtyMark(false)
                    .setShowDirtyMark(false)
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("2em")
                    .setColSortable(false)
                    .setHeader([{
                        "id": "type",
                        "caption": "$(RAD.api_dlg.Callback Type)",
                        "flexSize": true,
                        "width": "6em",
                        "type": "label"
                    },
                        {
                            "id": "source",
                            "flexSize": true,
                            "caption": "$(RAD.api_dlg.Callback)",
                            "width": "12em",
                            "type": "label"
                        }])
                    .setTreeMode("none")
                    .setValue("")
                    .onRowSelected("_ontrows")
                    .beforeRowActive("_tg_beforerowactive")
                    .setCustomClass({
                        "HFMARK": "ood-uicmd-none"
                    })
            );

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events: {
            onReady: "_onready"
        },
        _onready: function () {

            if (CONF.getClientMode() != 'project') this.ood_url.setType('none');

        },

        _ctl_ood_url_onhotkey: function (profile, keyboard, e, src) {
            if (keyboard[0] == '/') {
                this._reSetValue(profile);
            }
        },

        _ctl_ood_url_beforshow: function (profile) {
            var ns = this,
                newValue = ns.ood_url.getValue();
            newValue = newValue || '/' + SPA.getCurrentClassName().replace('.', '/');
            this._reSetValue(profile, newValue);
            // if (newValue && newValue == '') {
            //     this._reSetValue(profile);
            // }
        },


        _reSetValue: function (profile, newValue) {
            var ns = this,
                prop = ns.properties;
            //    newValue=ns.ood_url.getValue();
            var ns = this, ins = profile.boxing(), t = SPA.curProjectConfig.$ConstantData;
            var items = [];

            ns._filter = ".*" + newValue + ".*";
            ood.Dom.busy();
            var apipath = '';
            if (SPA.curProjectConfig.$ConstantData && SPA.curProjectConfig.$ConstantData.apipath) {
                apipath = SPA.curProjectConfig.$ConstantData.apipath;
            }
            ood.Ajax(CONF.getAPIService, {
                    "path": apipath,
                    'currentClassName': SPA.getCurrentClassName(),
                    pattern: ns._filter
                },
                function (txt) {
                    var obj = txt.data;
                    if (obj && !obj.error) {
                        ood.arr.each(obj, function (o) {
                            items.push({
                                id: o.id,
                                caption: o.caption,
                                disabled: o.disabled,
                                properties: o.properties,
                                getProperties: function () {
                                    return this.properties;
                                }

                            });
                        });
                    }

                    if (!obj || obj.length == 0) {
                        items.push({
                            id: newValue,
                            caption: ood.getRes('RAD.apiconfig.noAPIFound') || '没有发现匹配的API!',
                        });
                    }

                    try {
                        ins.expand();
                    } catch (e) {

                    }
                    ins.setItems(items);

                    ood.Dom.free();
                }, function (obj) {
                    ins.setItems(items);
                    ood.Dom.free();
                }, null, {method: 'post'}).start();

        },


        _ctl_ood_url_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            var item = profile.boxing().getItemByItemId(newValue);
            if (item) {
                this.setDataToEditor(item, ns._host)
            } else {
                this._reSetValue(profile, newValue);
            }

        },

        _ctl_ood_url_oncommand: function (profile, src) {
            var ns = this, prop = this.getDataFromEditor(), queryArgs = prop.queryArgs;

            profile.boxing().setValue("/LOCALESD/executeCMD", true);

            var expression = queryArgs.expression;

            ood.getModule("RAD.expression.ExpressionTemp", function () {
                this.setData({projectName: SPA.curProjectName, className: SPA.getCurrentClassName()});
                this.setDataToEditor(ns._host, ns._cls, prop);
                this.setEvents("onDestroy", function () {
                    ns.m_reqparams.setProperties("value", prop.queryArgs);
                    ns.updateRequestData(prop.queryArgs);

                });
                this.show();
                this.initData();
                this._fireEvent('afterShow');

            });
            // this._reSetValue(profile, "");
        },


        // object to editor
        setDataToEditor: function (api, host, cls) {
            var ns = this,
                prop = api.getProperties(),
                arr, v, values, t, m;
            ns._oldprop = prop;
            ns._host = host;
            ns.cls = cls;

            var packageName =SPA.getCurrentClassName().substring(0, SPA.getCurrentClassName().lastIndexOf("."));

            var queryURL = prop.queryURL || '/' + packageName.replaceAll('.', '/');
            ns.ood_url.setValue(queryURL);
            if (!prop.queryAsync && (prop.queryAsync == false || prop.queryAsync == 'false')) {
                ns.ood_asyn.setValue(false);
            } else {
                ns.ood_asyn.setValue(true);
            }

            ns.autoRun.setValue(prop.autoRun);
            ns.isAllform.setValue(prop.isAllform);
            ns.ood_method.setValue(prop.queryMethod);
            ns.ood_username.setValue(prop.queryUserName);
            ns.ood_password.setValue(prop.queryPassword);
            ns.ood_reqtype.setValue(prop.requestType);
            ns.ood_rsptype.setValue(prop.responseType);
            ns.ood_proxytype.setValue(prop.proxyType);
            ns.ood_token.setValue(prop.oAuth2Token);


            // requestDataSource
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.requestDataSource, function (o) {
                    v[o.type + "-" + o.name] = o.path;

                    if (o.type == 'SPA') {
                        m = "SPA" + "-" + o.name;
                        values.push(m);
                        arr.push({
                            id: m,
                            bindertype: "SPA",
                            cells: [o.name, o.path, "SPA参数"]
                        });
                    }
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.DataBinder'])
                        return;
                    t = db.getName();
                    m = "DATABINDER" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "DATABINDER",
                        cells: [t, v[m] || "", "自定义"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;

                    t = db.getAlias();

                    m = "PAGEBAR" + "-" + t;

                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "PAGEBAR",
                        cells: [t, v[m] || "", "分页参数"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();

                    m = "TREEVIEW" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "TREEVIEW",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.FusionChartsXT'])
                        return;
                    t = db.getAlias();

                    m = "FCHART" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "FCHART",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();
                    m = "GALLERY" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "GALLERY",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.ContentBlock'])
                        return;
                    t = db.getAlias();
                    m = "CONTENTBLOCK" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "CONTENTBLOCK",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TitleBlock'])
                        return;
                    t = db.getAlias();
                    m = "TITLEBLOCK" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "TITLEBLOCK",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();

                    m = "MODULE" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "MODULE",
                        cells: [t, v[m] || t, "子模块"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();

                    m = "TREEGRID" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "TREEGRID",
                        cells: [t, v[m] || "", "所有选中行ID"]
                    });
                });
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();

                    m = "TREEGRID" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "TREEGRID",
                        cells: [t, v[m] || "", "所有选中行ID"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();

                    m = "TREEGRIDROW" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "TREEGRIDROW",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });


                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "FORM" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "FORM",
                        cells: [t, v[m] || "", "表单数据"]
                    });
                });


                ns.ood_reqds.setRows(arr).setValue(values.join(";"));
            }());

            // responseDataTarget
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.responseDataTarget, function (o) {
                    v[o.type + "-" + (o.name || "")] = o.path;
                });


                if (m in v) values.push(m);

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.DataBinder'])
                        return;
                    t = db.getName();
                    m = "DATABINDER" + "-" + t;
                    if (m in v) values.push(m);
                    arr.push({
                        id: m,
                        bindertype: "DATABINDER",
                        cells: ["数据绑定", t, v[m] || ""]
                    });
                });

                //表单数据
                var formRow = [], hasFormValue = false;

                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "FORM" + "-" + t;

                    if (m in v) {
                        values.push(m)
                        hasFormValue = true;
                    }
                    formRow.push({
                        id: m,
                        bindertype: "FORM",
                        cells: [t, v[m] || "data", "表单"]
                    });
                });

                if (formRow.length > 0) {
                    arr.push({
                        id: 'ALLFORM',
                        bindertype: "FORM",
                        initFold: !hasFormValue,
                        cells: ["<b>表单</b>"],
                        sub: formRow
                    })
                }


                //组件列表
                var gridRow = [], hasGridValue = false;

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();
                    m = "TREEGRID" + "-" + t;

                    if (m in v) {
                        values.push(m)
                        hasGridValue = true;
                    }

                    gridRow.push({
                        id: m,
                        bindertype: "TREEGRID",
                        cells: [t, v[m] || "data", "GRID"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();
                    m = "GALLERY" + "-" + t;

                    if (m in v) values.push(m);
                    gridRow.push({
                        id: m,
                        bindertype: "GALLERY",
                        cells: [t, v[m] || "data", "GALLERY"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;
                    t = db.getAlias();
                    m = "PAGEBAR" + "-" + t;
                    if (m in v) values.push(m);
                    gridRow.push({
                        id: m,
                        bindertype: "PAGEBAR",
                        cells: [t, v[m] || "size", "设置最大记录数"]
                    });
                });

                if (gridRow.length > 0) {
                    arr.push({
                        id: 'GRID',
                        initFold: !hasGridValue,
                        bindertype: "GRID",
                        cells: ["<b>Grid列表</b>"],
                        sub: gridRow
                    })
                }


                //TitleBlcok列表
                var titleBlockRow = [], hasTitleBlockValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TitleBlock'] )
                        return;
                    t = db.getAlias();
                    m = "TITLEBLOCK" + "-" + t;
                    if (m in v) {
                        values.push(m)
                        hasTitleBlockValue = true;
                    }
                    ;
                    titleBlockRow.push({
                        id: m,
                        bindertype: "TITLEBLOCK",
                        cells: [t, v[m] || "data", "摘要项填充"]
                    });
                });

                if (titleBlockRow.length > 0) {
                    arr.push({
                        id: 'TITLEBLOCK',
                        initFold: !hasTitleBlockValue,
                        bindertype: "TITLEBLOCK",
                        cells: ["<b>标题摘要</b>"],
                        sub: titleBlockRow
                    })
                }


                //TitleBlcok列表
                var contentBlockRow = [], hasContentBlockValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.ContentBlock'] )
                        return;
                    t = db.getAlias();
                    m = "CONTENTBLOCK" + "-" + t;
                    if (m in v) {
                        values.push(m)
                        hasContentBlockValue = true;
                    }

                    contentBlockRow.push({
                        id: m,
                        bindertype: "CONTENTBLOCK",
                        cells: [t, v[m] || "data", "内容块填充"]
                    });
                });

                if (contentBlockRow.length > 0) {
                    arr.push({
                        id: 'CONTENTBLOCK',
                        initFold: !hasContentBlockValue,
                        bindertype: "CONTENTBLOCK",
                        cells: ["<b>内容块</b>"],
                        sub: contentBlockRow
                    })
                }



                //组件列表
                var tabsRow = [], hasTabsValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Tabs'] || !db.Class['ood.UI.Tabs'])
                        return;
                    t = db.getAlias();
                    m = "TABS" + "-" + t;
                    if (m in v) {
                        values.push(m)
                        hasTabsValue = true;
                    }
                    ;
                    tabsRow.push({
                        id: m,
                        bindertype: "TABS",
                        cells: [t, v[m] || "data", "动态Tabs"]
                    });
                });

                if (tabsRow.length > 0) {
                    arr.push({
                        id: 'TABS',
                        initFold: !hasTabsValue,
                        bindertype: "TABS",
                        cells: ["<b>Tabs导航</b>"],
                        sub: tabsRow
                    })
                }


                //组件列表
                var treeRow = [], hasTreeValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();
                    m = "TREEVIEW" + "-" + t;

                    if (m in v) {
                        values.push(m);
                        hasTreeValue = true;
                    }
                    treeRow.push({
                        id: m,
                        bindertype: "TREEVIEW",
                        cells: [t, v[m] || "data", "绑定树形"]
                    });
                });

                if (treeRow.length > 0) {
                    arr.push({
                        id: 'TREEVIEW',
                        initFold: !hasTreeValue,
                        bindertype: "TREEVIEW",
                        cells: ["<b>Tree树形</b>"],
                        sub: treeRow
                    })
                }


                //组件列表
                var fchartRow = [], hasFchartValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.FusionChartsXT'])
                        return;
                    t = db.getAlias();
                    m = "FCHART" + "-" + t;


                    fchartRow.push({
                        id: m,
                        bindertype: "FCHART",
                        cells: [t, v[m] || "data", "绑定统计图"]
                    });
                    if (m in v) {
                        values.push(m);
                        hasFchartValue = true;
                    }

                    var mm = "FCHARTTRENDLINES-" + t;
                    fchartRow.push({
                        id: mm,
                        bindertype: "FCHARTTRENDLINES",
                        cells: [t, v[mm] || "data", "分析线"]
                    });


                    if (mm in v) {
                        values.push(mm);
                        hasFchartValue = true;
                    }
                    mm = "FCHARTDATASET-" + t;
                    fchartRow.push({
                        id: mm,
                        bindertype: "FCHARTDATASET",
                        cells: [t, v[mm] || "data", "数据集合"]
                    });
                    if (mm in v) {
                        values.push(mm);
                        hasFchartValue = true;
                    }
                    mm = "FCHARTCATEGORIES-" + t;
                    fchartRow.push({
                        id: mm,
                        bindertype: "FCHARTCATEGORIES",
                        cells: [t, v[mm] || "data", "数据分类"]
                    });
                    if (mm in v) {
                        values.push(mm);
                        hasFchartValue = true;
                    }

                });

                if (fchartRow.length > 0) {
                    arr.push({
                        id: 'FCHART',
                        initFold: !hasFchartValue,
                        bindertype: "FCHART",
                        cells: ["<b>统计图</b>"],
                        sub: fchartRow
                    })
                }


                //组件列表
                var svgpaperRow = [], hasSvgValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.SVGPaper'])
                        return;
                    t = db.getAlias();
                    m = "SVGPAGPER" + "-" + t;
                    if (m in v) {
                        values.push(m);
                        hasSvgValue = true;
                    }
                    svgpaperRow.push({
                        id: m,
                        bindertype: "SVGPAPER",
                        cells: [t, v[m] || "data", "SVG图形"]
                    });
                });
                if (svgpaperRow.length > 0) {
                    arr.push({
                        id: 'SVGPAGPER',
                        initFold: !hasSvgValue,
                        bindertype: "SVGPAGPER",
                        cells: ["<b>SVG图形</b>"],
                        sub: svgpaperRow
                    })
                }

                //组件列表
                var dicRow = [], hasDivValue = false;
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.absList'])
                        return;
                    t = db.getAlias();
                    m = "LIST" + "-" + t;

                    if (m in v) {
                        values.push(m);
                        hasDivValue = true;
                    }

                    dicRow.push({
                        id: m,
                        bindertype: "LIST",
                        cells: [t, v[m] || "data", "List字典表"]
                    });
                });
                if (dicRow.length > 0) {
                    arr.push({
                        id: 'LIST',
                        initFold: !hasDivValue,
                        bindertype: "LIST",
                        cells: ["<b>List字典表</b>"],
                        sub: dicRow
                    })
                }

                //组件更新
                var componentRow = [], hasComValue = false;


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Dialog'])
                        return;
                    t = db.getAlias();
                    m = "COMPONENT" + "-" + t;

                    if (m in v) values.push(m);
                    componentRow.push({
                        id: m,
                        bindertype: "COMPONENT",
                        cells: [t, v[m] || "data", "Dialog"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Block'])
                        return;
                    t = db.getAlias();
                    m = "COMPONENT" + "-" + t;

                    if (m in v) {
                        values.push(m);
                        hasComValue = true;
                    }

                    componentRow.push({
                        id: m,
                        bindertype: "COMPONENT",
                        cells: [t, v[m] || "data", "BLOCK"]
                    });
                });

                if (componentRow.length > 0) {
                    arr.push({
                        id: 'COMPONENT',
                        initFold: !hasComValue,
                        bindertype: "COMPONENT",
                        cells: ["<b>动态重绘</b>"],
                        sub: componentRow
                    })
                }

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();
                    m = "MODULE" + "-" + t;

                    if (m in v) {
                        values.push(m);
                    }

                    arr.push({
                        id: m,
                        bindertype: "MODULE",
                        cells: [t, "模块", v[m] || 'data.' + t]
                    });
                });

                var expressionRow = [], hasExpressionValue = false;
                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "EXPRESSION" + "-" + t;

                    if (m in v) {
                        values.push(m);
                        hasExpressionValue = true;
                    }

                    expressionRow.push({
                        id: m,
                        bindertype: "EXPRESSION",
                        cells: [t, ui.key, v[m] || "data"]

                    });
                });
                if (expressionRow.length > 0) {
                    arr.push({
                        id: 'EXPRESSION',
                        initFold: !hasExpressionValue,
                        bindertype: "EXPRESSION",
                        cells: ["<b>表达式</b>"],
                        sub: expressionRow
                    })
                }

                var systemRow = [];

                m = "log-console.log";
                systemRow.push({
                    id: m,
                    bindertype: "LOG",
                    cells: ["console.log", v[m] || ""]
                });
                if (m in v) values.push(m);

                m = "alert-alert";
                systemRow.push({
                    id: m,
                    bindertype: "ALERT",
                    cells: ["ALERT", v[m] || ""]
                });

                if (systemRow.length > 0) {
                    arr.push({
                        id: 'LOG',
                        bindertype: "LOG",
                        cells: ["", '', "本地功能"],
                        sub: systemRow
                    })
                }

                ns.ood_rsptarget.setRows(arr).setValue(values.join(";"));
            }());

            // responseCallback
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.responseCallback, function (o) {
                    v[o.type + "-" + (o.name || "")] = o.type;
                });
                if (cls && cls.Instance && cls.Instance.functions) {
                    ood.each(cls.Instance.functions, function (conf, id) {
                        t = id;
                        m = "host" + "-" + t;
                        if (m in v) values.push(m);
                        arr.push({
                            id: m,
                            bindertype: "host",
                            cells: ["page", t]
                        });
                    });
                }
                if (SPA.curProjectConfig && SPA.curProjectConfig.$GlobalFunctions){
                    ood.each(SPA.curProjectConfig.$GlobalFunctions, function (conf, id) {
                        t = id;
                        m = "global" + "-" + t;
                        if (m in v) values.push(m);
                        arr.push({
                            id: m,
                            bindertype: "global",
                            cells: ["global", t]
                        });
                    });
                }


                ns.ood_rspfun.setRows(arr).setValue(values.join(";"));
            }());

            // queryArgs
            ns.m_reqparams.setProperties("value", prop.queryArgs);

            // fake cookies
            ns.m_cookies.setProperties("value", prop.fakeCookies);

            // header
            if (prop.queryHeader && !ood.isEmpty(prop.queryHeader)) {
                ns.m_header.setProperties("value", prop.queryHeader);
            }

            // queryOptions
            ns.m_options.setProperties("value", prop.queryOptions);

            // here, must give prop.queryArgs 
            ns.updateRequestData(prop.queryArgs, prop.requestDataSource);
        },
        // editor to object
        getDataFromEditor: function () {
            var ns = this,
                oldprop = ns._oldprop,
                hash = {
                    requestDataSource: oldprop.requestDataSource,
                    responseDataTarget: oldprop.responseDataTarget,
                    responseCallback: oldprop.responseCallback
                },
                arr, v;

            hash.queryURL = ns.ood_url.getValue();
            hash.queryAsync = ns.ood_asyn.getValue();
            hash.autoRun = ns.autoRun.getValue();
            hash.queryMethod = ns.ood_method.getValue();
            hash.queryUserName = ns.ood_username.getValue();
            hash.queryPassword = ns.ood_password.getValue();
            hash.isAllform = ns.isAllform.getValue();
            hash.proxyType = ns.ood_proxytype.getValue();
            hash.requestType = ns.ood_reqtype.getValue();
            hash.responseType = ns.ood_rsptype.getValue();
            hash.oAuth2Token = ns.ood_token.getValue();


            // requestDataSource
            if (ns.ood_reqds.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_reqds.getRows(), function (o) {
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[0].value,
                            path: o.cells[1].value
                        });
                    }
                });
                hash.requestDataSource = arr;
            }
            // responseDataTarget
            if (ns.ood_rsptarget.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_rsptarget.getRows(), function (o) {
                    if (o.sub && o.sub.length > 0) {
                        ood.arr.each(o.sub, function (oo) {
                            if (oo._selected) {
                                arr.push({
                                    type: oo.bindertype,
                                    name: oo.cells[0].value,
                                    path: oo.bindertype == 'log' || oo.bindertype == 'alert' ? "" : oo.cells[1].value
                                });
                            }
                        })

                    } else {
                        if (o._selected) {
                            arr.push({
                                type: o.bindertype,
                                name: o.cells[0].value,
                                path: o.bindertype == 'log' || o.bindertype == 'alert' ? "" : o.cells[1].value
                            });
                        }
                    }

                });
                hash.responseDataTarget = arr;
            }
            // responseCallback
            if (ns.ood_rspfun.getRootNode()) {
                arr = [];
                ood.arr.each(ns.ood_rspfun.getRows(), function (o) {
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[1].value
                        });
                    }
                });
                hash.responseCallback = arr;
            }

            // queryArgs
            // m_reqparams
            v = ns.m_reqparams.getValue(true);
            hash.queryArgs = v || {};


            // fake cookies
            v = ns.m_cookies.getValue(true);
            hash.fakeCookies = v || {};

            // header
            v = ns.m_header.getValue(true);
            hash.queryHeader = v || {};

            // queryOptions
            v = ns.m_options.getValue(true);
            if (v && v.header && ood.isHash(v.header)) {
                ood.merge(hash.queryHeader, v.header);
                delete v.header;
            }
            hash.queryOptions = v || {};

            return hash;
        },

        // update request data
        updateRequestData: function (queryArgs, requestDataSource) {
            var ns = this;

            queryArgs = queryArgs ? ood.clone(queryArgs) : ns.m_reqparams.getValue(true);
            requestDataSource = requestDataSource || [];
            if (!requestDataSource.length) {
                ood.arr.each(ns.ood_reqds.getRows(), function (o) {
                    if (o._selected) {
                        requestDataSource.push({
                            type: o.bindertype,
                            name: o.cells[0].value,
                            path: o.cells[1].value
                        });
                    }
                });
            }

            // merge request data
            if (requestDataSource && requestDataSource.length) {
                for (var i in requestDataSource) {
                    var o = requestDataSource[i], t, v, path;
                    switch (o.type.toUpperCase() ) {
                        case "DATABINDER":
                            if ((t = ns._host[o.name]) && t.Class['ood.DataBinder']) {
                                if (!t.updateDataFromUI()) {
                                    return;
                                } else {
                                    path = (o.path || "").split('.');
                                    if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, t.getData());
                                    else ood.set(queryArgs, o.path.split('.'), t.getData());
                                }
                            }
                            break;
                        case "PAGEBAR":
                            if (t = ns._host[o.name]) {
                                var pageparams = {pageSize: t.getPageCount(), pageIndex: t.getPage()};
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, pageparams);
                                else ood.set(queryArgs, path, pageparams);
                            }
                            break;

                        case "STAGVAR":
                            if (ns.sTagVar) {
                                var sTagVar = {};
                                ood.each(ns.sTagVar, function (value, key) {
                                    sTagVar['s' + key] = value;
                                })
                                ood.set(queryArgs, path, sTagVar);
                            }
                            break;


                        case "TREEGRID":
                            if (t = ns._host[o.name]) {
                                path = (o.path || t.getUidColumn()).split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                            }
                            break;

                        case "TREEGRIDROW":
                            if (t = ns._host[o.name]) {
                                path = (o.path || t.getUidColumn()).split('.');
                                if (t.getActiveRow('map')) {
                                    ood.set(queryArgs, path, t.getActiveRow('map')[t.getUidColumn()]);
                                }
                            }
                            break;

                        case "TREEVIEW":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                                ood.set(queryArgs, path + 'Caption', t.getUICationValue());
                            }
                            break;
                        case "FCHART":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                                ood.set(queryArgs, path + 'Caption', t.getUICationValue());
                            }
                            break;
                        case "FCHARTCATEGORIES":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                                ood.set(queryArgs, path + 'Caption', t.getUICationValue());
                            }
                            break;
                        case "FCHARTDATASET":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                                ood.set(queryArgs, path + 'Caption', t.getUICationValue());
                            }
                            break;
                        case "FCHARTTRENDLINES":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                                ood.set(queryArgs, path + 'Caption', t.getUICationValue());
                            }
                            break;
                        case "FORM":
                            if ((t = ns._host[o.name]) && t.Class['ood.absContainer'] && t.getFormElements().size() && t.getRootNode()) {
                                // if(!t.checkValid() || !t.checkRequired()){
                                //     return;
                                // }else{
                                path = (o.path || "").split('.');
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, t.getFormValues());
                                else ood.set(queryArgs, path, t.getFormValues());
                                //}
                            }
                            break;
                        case "MODULE":
                            if ((t = ns._host[o.name]) && t.Class['ood.Module']) {
                                path = (o.path || o.name).split('.');
                                ood.set(queryArgs, path, t.getValue(true));

                            }
                            break;
                    }
                }
            }

            var code = ood.stringify(queryArgs);
            if (ood.Coder) {
                code = ood.Coder.formatText(code);
            }

            ns.ood_reqtext.setValue(code);
        },
        _m_reqparams_onchange: function (module) {
            this.updateRequestData();
        },
        _ood_reqds_onchange: function () {
            this.updateRequestData();
        },
        _ood_reqds_bcellupdated: function () {
            this.updateRequestData();
        },


        // try to call api
        _ood_ui_htmlbutton3_onclick: function () {
            var ns = this,
                prop = ns.getDataFromEditor();

            var url = prop.queryURL || "";
            if (/\s*\{ood.constant\.[\w]+\}\s*/.test(url)) {
                url = ood.adjustVar(url, {ood: {constant: SPA.curProjectConfig.$ConstantData}});
            }
            prop.queryURL = url;

            if (!prop.queryURL) {
                ood.alert(ood.adjustRes("$inline.required"), ood.adjustRes(ns.ood_url.getLabelCaption()), function () {
                    ns.ood_url.activate();
                });
                return;
            }

            delete prop.responseDataTarget;
            delete prop.responseCallback;

            var api = new ood.APICaller(),
                callback = function (state, rsp) {
                    console.log(state, rsp);
                    if (ood.isObj(rsp)) {
                        rsp = ood.stringify(rsp);
                        if (ood.Coder) {
                            rsp = "Response Object:\n\r\n\r" + ood.Coder.formatText(rsp);
                        }
                    }
                    api.destroy();
                    window.alert(rsp);
                };
            api.setHost(ns._host);
            api.setProperties(prop);

            api.invoke(function (rsp) {
                callback('Response Sucessful', rsp);
            }, function (rsp) {
                callback('Response Error', rsp);
            });
        },

        // setBack
        _ood_ui_dialog2_beforeclose: function (profile) {
            var ns = this, prop = ns.getDataFromEditor();
            ns.fireEvent("onchange", [prop]);
            delete ns._oldprop;
        },

        _ontrows: function (profile, row, e, src, type) {
            //if (type == -1)
            //   profile.boxing().updateCell(row.cells[1], {value: ""}, false, false);
            this.updateRequestData();
        },

        _ontrows2: function (profile, row, e, src, type) {
            //if (type == -1)
            //    profile.boxing().updateCell(row.cells[0], {value: ""}, false, false);
            this.updateRequestData();
        },
        _acu: function (profile, cell, options) {
            if (!cell._row._selected && options.value) {
                var ins = profile.boxing();
                var arr = ins.getValue(true);
                arr.push(cell._row.id);
                ood.arr.removeDuplicate(arr);
                ood.arr.removeValue(arr, "");
                ins.setValue((arr && arr.length) ? arr.join(";") : null);
            }
            this.updateRequestData();
        },
        _tg_beforerowactive: function () {
            return false;
        },
        _ood_ui_comboinput207_aftervalueset: function (profile, oldValue, newValue, force, tag) {
            var ns = this,
                req = ns.ood_reqtype.getValue(),
                m = ns.ood_method.getValue(),

                reqItems = "FORM,JSON,XML,SOAP".split(","),
                mItems = "auto,GET,POST,PUT,DELETE".split(",");

            switch (newValue.toUpperCase() ) {
                case "JSONP":
                    reqItems.pop();
                    reqItems.pop();
                    mItems.pop();
                    mItems.pop();
                    mItems.pop();

                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
                    if (req == 'XML' || req == 'SOAP') {
                        ns.ood_reqds.setValue('FORM');
                    }
                    if (m == 'POST' || req == 'PUB' || req == 'DELETE') {
                        ns.ood_method.setValue('auto');
                    }
                    break;
                case "XDMI":
                    reqItems.pop();
                    reqItems.pop();
                    mItems.pop();
                    mItems.pop();

                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
                    if (req == 'XML' || req == 'SOAP') {
                        ns.ood_reqds.setValue('FORM');
                    }
                    if (req == 'PUB' || req == 'DELETE') {
                        ns.ood_method.setValue('auto');
                    }
                    break;
                default:
                    ns.ood_reqtype.setItems(reqItems);
                    ns.ood_method.setItems(mItems);
            }
        }
    }
});
