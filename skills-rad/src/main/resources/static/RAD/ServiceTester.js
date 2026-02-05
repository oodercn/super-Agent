ood.Class('RAD.ServiceTester', 'ood.Module', {
    Instance: {
        initialize: function () {
            this.autoDestroy = true;
            this.properties = {};
        },
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dlg")
                    .setLeft("11.416666666666666em")
                    .setTop("3.75em")
                    .setWidth("44.416666666666664em")
                    .setHeight("33.75em")
                    .setCaption("$RAD.spabuilder.st_title")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setOverflow("visible")
                    .beforeClose("_dlg_beforeclose")
            );

            host.dlg.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ctl_group1")
                    .setLeft("0.625em")
                    .setTop("3.3333333333333335em")
                    .setWidth("42.583333333333336em")
                    .setConDockPadding({
                            "left": 4,
                            "top": 4,
                            "right": 4,
                            "bottom": 4
                        }
                    )
                    .setCaption("$RAD.spabuilder.st_queryobj")
                    .setToggleBtn(false)
                    .setCustomStyle({
                            "PANEL": "overflow:visible"
                        }
                    )
            );

            host.ctl_group1.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "ctl_query")
                    .setProperties({
                            "value": "{a:1,b:2}"
                        }
                    )
            );

            host.dlg.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ctl_group3")
                    .setLeft("0.625em")
                    .setTop("16.666666666666668em")
                    .setWidth("42.583333333333336em")
                    .setConDockPadding({
                            "left": 4,
                            "top": 4,
                            "right": 4,
                            "bottom": 4
                        }
                    )
                    .setCaption("$RAD.spabuilder.st_result")
                    .setToggleBtn(false)
                    .setCustomStyle({
                            "PANEL": "overflow:visible"
                        }
                    )
            );

            host.ctl_group3.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "ctl_result")
                    .setDock("fill")
                    .setLeft("3.125em")
                    .setTop("0em")
                    .setMultiLines(true)
                    .setDirtyMark(false)
            );

            host.dlg.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel1")
                    .setLeft("0.625em")
                    .setTop("0.625em")
                    .setWidth("6.833333333333333em")
                    .setHeight("0.5833333333333334em")
                    .setCaption("$RAD.spabuilder.st_uri")
            );

            host.dlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton5")
                    .setLeft("27.833333333333332em")
                    .setTop("27.25em")
                    .setWidth("6.875em")
                    .setCaption("$RAD.close")
                    .onClick("_ctl_sbutton5_onclick")
            );

            host.dlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton6")
                    .setLeft("16.583333333333332em")
                    .setTop("27.25em")
                    .setWidth("10.625em")
                    .setCaption("$RAD.spabuilder.st_createcode")
                    .onClick("_ctl_sbutton6_onclick")
            );

            host.dlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton15")
                    .setLeft("28.25em")
                    .setTop("14.166666666666666em")
                    .setWidth("13.125em")
                    .setHeight("2.25em")
                    .setCaption("<strong>$RAD.spabuilder.st_send</strong>")
                    .onClick("_ctl_sbutton15_onclick")
            );

            host.dlg.append(
                ood.create("ood.UI.RadioBox")
                    .setHost(host, "ctl_method")
                    .setItems([{
                        "id": "auto",
                        "caption": "Auto"
                    },
                        {
                            "id": "get",
                            "caption": "Get"
                        },
                        {
                            "id": "post",
                            "caption": "Post"
                        }])
                    .setLeft("8.25em")
                    .setTop("14.25em")
                    .setWidth("19.166666666666668em")
                    .setHeight("2.5em")
                    .setValue("auto")
                    .setDirtyMark(false)
            );

            host.dlg.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel13")
                    .setLeft("0em")
                    .setTop("14.75em")
                    .setWidth("8.25em")
                    .setHeight("0.25em")
                    .setCaption("$RAD.spabuilder.st_method")
            );

            host.dlg.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_uri")
                    .setLeft("8.25em")
                    .setTop("0.375em")
                    .setWidth("34.833333333333336em")
                    .setValueFormat("^(http|https)\\:")
                    .setType("popbox")
                    .setDirtyMark(false)
                    .setValue("")
                    .onClick("_ctl_uri_onclick")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend: function (parent, subId, left, top) {
            var ns = this;
            ns.dlg.showModal();
            return true;
        },
        init: function (force) {
            var ns = this;
            if (force) {
                ns.ctl_uri.setValue("", true);
            }
            ns.ctl_method.setValue("auto", true);
        },
        _ctl_sbutton5_onclick: function (profile, e, src, value) {
            var ns = this, uictrl = profile.boxing();
            ns.dlg.close();
        },
        _ctl_sbutton15_onclick: function (profile, e, src, value) {
            var ns = this,
                ctrl = profile.boxing(),
                options = {},
                uri = ns.ctl_uri.getValue(),
                str_query = ns.ctl_query.getValue();
            if (uri) {
                var query = ood.unserialize(str_query);
                if (!query) query = {};
                query._rand = ood.rand();
                var method = ns.ctl_method.getValue();
                if (method != 'auto') {
                    options.method = method;
                }
                ctrl.getRoot().onMouseout(true);
                ctrl.setDisabled(true).setCaption("<strong>$RAD.spabuilder.st_sending</strong>");
                ood.request(uri, query || "", function (rsp) {
                    var rspobj = rsp;
                    if (rspobj) {
                        ns.showResult(rspobj);
                    } else {
                        ns.showErr(rsp);
                    }
                    ctrl.setDisabled(false).setCaption("<strong>$RAD.spabuilder.st_send</strong>");
                }, function (rsp) {
                    ns.showErr(rsp);
                    ctrl.setDisabled(false).setCaption("<strong>$RAD.spabuilder.st_send</strong>");
                }, null, options);
            } else {
                ood.message(ood.getRes("RAD.spabuilder.st_nodata"));
            }
        },
        showErr: function (msg) {
            ood.alert(msg);
        },
        showResult: function (obj) {
            var ns = this;
            ns.ctl_result.setValue(ood.Coder.formatText(ood.stringify(obj)), true);
        },
        _ctl_sbutton6_onclick: function (profile, e, src, value) {
            var ns = this, uictrl = profile.boxing(),
                uri = ns.ctl_uri.getValue(),
                str_query = ns.ctl_query.getValue(),
                method = ns.ctl_method.getValue();
            var code = 'ood.request("$1", ' +
                '$2, ' + '\n' +
                'function(rsp){' + '\n' +
                '    var rspobj = rsp;' + '\n' +
                '    if(rspobj){' + '\n' +
                '        // handle result' + '\n' +
                '    }else{' + '\n' +
                '        // handle exception' + '\n' +
                '    }' + '\n' +
                '}, function(rsp){' + '\n' +
                '    // handle exception' + '\n' +
                '}, null, $3);';
            code = code.replace('$1', uri).replace('$2', ood.Coder.formatText(str_query || "")).replace('$3', method == "auto" ? "null" : ("{method:'" + method + "'}"));
            ns.ctl_result.setValue(code, true);
        },
        _dlg_beforeclose: function (profile) {
            this.dlg.hide();
            return false;
        },
        _ctl_uri_onclick: function (profile, node) {
            var ns = this, uictrl = profile.boxing();
            ood.Dom.submit(uictrl.getValue());
        }
    }
});
