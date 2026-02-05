ood.Class('RAD.EditorTheme', 'ood.Module', {
    Instance: {
        // To initialize internal components (mostly UI controls)
        // *** If you're not a skilled, dont modify this function manually ***
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dlg")
                    .setLeft("2.5em")
                    .setTop("3.125em")
                    .setWidth("45.833333333333336em")
                    .setHeight("26.416666666666668em")
                    .setCaption("$RAD.editorTheme.name")
                    .setMinBtn(false)
                    .setMaxBtn(false)
                    .setOverflow("hidden")
            );

            host.dlg.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel7")
                    .setDock("none")
                    .setLeft("0.625em")
                    .setTop("13.75em")
                    .setWidth("15.916666666666666em")
                    .setHeight("8.75em")
                    .setZIndex(1)
                    .setOverflow("visible")
                    .setCaption("$RAD.editorTheme.selFontSize")
            );

            host.ctl_panel7.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "_ctl_fs")
                    .setDirtyMark(false)
                    .setLeft("9.083333333333334em")
                    .setTop("3em")
                    .setWidth("5.25em")
                    .setZIndex(1003)
                    .setType("spin")
                    .setUnit("px")
                    .setPrecision(0)
                    .setIncrement(1)
                    .setMin(1)
                    .setMax(100)
                    .afterValueSet("_ctl_fs_aftervalueset")
            );

            host.ctl_panel7.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel124")
                    .setLeft("0em")
                    .setTop("3.3333333333333335em")
                    .setWidth("8.25em")
                    .setHeight("1.5em")
                    .setCaption("$RAD.editorTheme.selFontSize")
            );

            host.ctl_panel7.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "ctl_wrap")
                    .setDirtyMark(false)
                    .setLeft("3.5833333333333335em")
                    .setTop("0.6666666666666666em")
                    .setWidth("10.833333333333334em")
                    .setIconPos("right")
                    .setCaption("$RAD.editorTheme.lineWrapping")
                    .afterUIValueSet("_ctl_wrap_afteruivalueset")
            );

            host.dlg.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel4")
                    .setDock("none")
                    .setLeft("0.625em")
                    .setTop("0.625em")
                    .setWidth("15.916666666666666em")
                    .setHeight("12.75em")
                    .setZIndex(1)
                    .setCaption("$RAD.editorTheme.selTheme")
            );

            host.ctl_panel4.append(
                ood.create("ood.UI.List")
                    .setHost(host, "_ctl_theme")
                    .setDirtyMark(false)
                    .setItems([{
                        "id": "default",
                        "caption": "default"
                    },
                        {
                            "id": "3024-day",
                            "caption": "3024-day"
                        },
                        {
                            "id": "3024-night",
                            "caption": "3024-night"
                        },
                        {
                            "id": "abcdef",
                            "caption": "abcdef"
                        },
                        {
                            "id": "ambiance-mobile",
                            "caption": "ambiance-mobile"
                        },
                        {
                            "id": "ambiance",
                            "caption": "ambiance"
                        },
                        {
                            "id": "base16-dark",
                            "caption": "base16-dark"
                        },
                        {
                            "id": "base16-light",
                            "caption": "base16-light"
                        },
                        {
                            "id": "bespin",
                            "caption": "bespin"
                        },
                        {
                            "id": "blackboard",
                            "caption": "blackboard"
                        },
                        {
                            "id": "cobalt",
                            "caption": "cobalt"
                        },
                        {
                            "id": "colorforth",
                            "caption": "colorforth"
                        },
                        {
                            "id": "darcula",
                            "caption": "darcula"
                        },
                        {
                            "id": "duotone-dark",
                            "caption": "duotone-dark"
                        },
                        {
                            "id": "duotone-light",
                            "caption": "duotone-light"
                        },
                        {
                            "id": "eclipse",
                            "caption": "eclipse"
                        },
                        {
                            "id": "elegant",
                            "caption": "elegant"
                        },
                        {
                            "id": "erlang-dark",
                            "caption": "erlang-dark"
                        },
                        {
                            "id": "gruvbox-dark",
                            "caption": "gruvbox-dark"
                        },
                        {
                            "id": "hopscotch",
                            "caption": "hopscotch"
                        },
                        {
                            "id": "icecoder",
                            "caption": "icecoder"
                        },
                        {
                            "id": "idea",
                            "caption": "idea"
                        },
                        {
                            "id": "isotope",
                            "caption": "isotope"
                        },
                        {
                            "id": "lesser-dark",
                            "caption": "lesser-dark"
                        },
                        {
                            "id": "liquibyte",
                            "caption": "liquibyte"
                        },
                        {
                            "id": "lucario",
                            "caption": "lucario"
                        },
                        {
                            "id": "material",
                            "caption": "material"
                        },
                        {
                            "id": "mbo",
                            "caption": "mbo"
                        },
                        {
                            "id": "mdn-like",
                            "caption": "mdn-like"
                        },
                        {
                            "id": "midnight",
                            "caption": "midnight"
                        },
                        {
                            "id": "nord",
                            "caption": "nord"
                        },
                        {
                            "id": "monokai",
                            "caption": "monokai"
                        },
                        {
                            "id": "neat",
                            "caption": "neat"
                        },
                        {
                            "id": "neo",
                            "caption": "neo"
                        },
                        {
                            "id": "night",
                            "caption": "night"
                        },
                        {
                            "id": "oceanic-next",
                            "caption": "oceanic-next"
                        },
                        {
                            "id": "panda-syntax",
                            "caption": "panda-syntax"
                        },
                        {
                            "id": "paraiso-dark",
                            "caption": "paraiso-dark"
                        },
                        {
                            "id": "paraiso-light",
                            "caption": "paraiso-light"
                        },
                        {
                            "id": "pastel-on-dark",
                            "caption": "pastel-on-dark"
                        },
                        {
                            "id": "railscasts",
                            "caption": "railscasts"
                        },
                        {
                            "id": "rubyblue",
                            "caption": "rubyblue"
                        },
                        {
                            "id": "seti",
                            "caption": "seti"
                        },
                        {
                            "id": "shadowfox",
                            "caption": "shadowfox"
                        },
                        {
                            "id": "solarized",
                            "caption": "solarized"
                        },
                        {
                            "id": "ssms",
                            "caption": "ssms"
                        },
                        {
                            "id": "the-matrix",
                            "caption": "the-matrix"
                        },
                        {
                            "id": "tomorrow-night-bright",
                            "caption": "tomorrow-night-bright"
                        },
                        {
                            "id": "tomorrow-night-eighties",
                            "caption": "tomorrow-night-eighties"
                        },
                        {
                            "id": "ttcn",
                            "caption": "ttcn"
                        },
                        {
                            "id": "twilight",
                            "caption": "twilight"
                        },
                        {
                            "id": "vibrant-ink",
                            "caption": "vibrant-ink"
                        },
                        {
                            "id": "xq-dark",
                            "caption": "xq-dark"
                        },
                        {
                            "id": "xq-light",
                            "caption": "xq-light"
                        },
                        {
                            "id": "yeti",
                            "caption": "yeti"
                        },
                        {
                            "id": "yonce",
                            "caption": "yonce"
                        },
                        {
                            "id": "zenburn",
                            "caption": "zenburn"
                        }])
                    .setDock("fill")
                    .setBorderType("none")
                    .setValue("")
                    .afterValueSet("_ctl_theme_aftervalueset")
            );

            host.dlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnOK")
                    .setLeft("31.833333333333332em")
                    .setTop("20.625em")
                    .setWidth("8.083333333333334em")
                    .setImageClass("ri-check-line") // 替换 spafont spa-icon-ok
                    .setCaption("$RAD.ok")
                    .onClick("_btnok_onclick")
            );

            host.dlg.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_sbutton13")
                    .setLeft("22.5em")
                    .setTop("20.625em")
                    .setWidth("8.083333333333334em")
                    .setImageClass("ri-close-line") // 替换 spafont spa-icon-cancel
                    .setCaption("$RAD.cancel")
                    .onClick("_btncancel_onclick")
            );

            host.dlg.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "codePreview")
                    .setLeft("17.416666666666668em")
                    .setTop("0.625em")
                    .setWidth("26.25em")
                    .setHeight("19.375em")
                    .setBorderType("inset")
                    .setBackground("#FFFFFF")
                    .setOverflow("hidden")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _btnok_onclick: function (profile, e, src, value) {
            this.fireEvent('onOK', [this._theme, this._fontsize, this._lineWrapping], this);
            this.dlg.close();
        },
        _btncancel_onclick: function (profile, e, src, value) {
            this.dlg.close();
        },
        customAppend: function (parent) {
            var ns = this,
                fontsize = parseInt(ood.CSS.$getCSSValue(".codemirror", "fontSize"), 10) || 13,
                theme = 'default',
                lineWrapping = false;

            var obj = CONF.getStatus();
            if (obj) {
                if (ood.isFinite(obj.editorFontSize)) {
                    fontsize = obj.editorFontSize;
                }
                if (obj.editorTheme) {
                    theme = obj.editorTheme;
                }
                if (ood.isSet(obj.lineWrapping)) {
                    lineWrapping = obj.lineWrapping;
                }
            }

            ns._ctl_fs.setUIValue(ns._fontsize = fontsize);
            ns._ctl_theme.setUIValue(ns._theme = theme);
            ns.ctl_wrap.setUIValue(ns._lineWrapping = lineWrapping);

            ns.dlg.showModal(parent);
            return false;
        },
        _refreshCode: function () {
            var ns = this,
                id = "cm-theme-" + ns._theme,
                path = CONF.mapServerPath(CONF.mapServerPath('/RAD/template/' + ood.getLang() + "/index.js"));

            if (ns._theme != "default" && !ood.CSS.get("id", id)) {
                ood.CSS.includeLink("/plugins/codemirror5/theme/" + ns._theme + ".css", false, id);
            }
            CONF.getLocalFile(path, function (code) {
                ns.codePreview.getSubNode("PANEL").html("", true);
                var cm = new CodeMirror(function (elt) {
                    ns.codePreview.getSubNode("PANEL").append(elt);
                    elt.style.fontSize = ns._fontsize + "px";
                }, {
                    theme: ns._theme,
                    lineWrapping: !!ns._lineWrapping,
                    readOnly: true,
                    value: code
                });
            }, function () {
                alert(ood.getRes('RAD.builder.noexist', path));
            });
        },
        _ctl_fs_aftervalueset: function (prf) {
            var ns = this;
            ns._fontsize = prf.boxing().getValue();
            ood.resetRun(this.$xid, function () {
                ns._refreshCode();
            }, 100);
        },
        _ctl_theme_aftervalueset: function (prf) {
            var ns = this;
            ns._theme = prf.boxing().getValue();
            ood.resetRun(this.$xid, function () {
                ns._refreshCode();
            }, 100);
        },
        _ctl_wrap_afteruivalueset: function (profile, oldValue, newValue) {
            var ns = this;
            ns._lineWrapping = !!newValue;
            ood.resetRun(this.$xid, function () {
                ns._refreshCode();
            }, 100);
        }
    }
});
