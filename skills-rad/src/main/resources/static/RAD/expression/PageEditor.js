ood.Class('RAD.expression.PageEditor', 'ood.Module', {
    Instance: {
        Initialize: function () {
            var ns = this;
            ns._ovalue = "";
            ns._dirty = false;
        },
        resetEditorStyle: function (theme, fs, wrap) {
            this.codeeditor.resetEditorStyle(theme, fs, wrap);
        },
        // notice: use it before rendered
        setType: function (type) {
            if (this.codeeditor) this.codeeditor.setCodeType(type, true);
            this._codetype = type && type.split(".")[0];
            if (this.toolbar4) {
                this.toolbar4.updateItem('jsoneditor', {
                    hidden: type != 'json'
                });
            }
        },

        activate: function () {
            this.codeeditor.activate();
        },
        getValue: function () {
            var ns = this;
            return ns._dirty ? ns.codeeditor.getUIValue() : ns._ovalue;
        },
        _refreshCM: function () {
            if (this.codeeditor) {
                this.codeeditor._refreshCM();
            }
        },
        setValue: function (value) {
            // adjust

            value = (value || '').replace(/\t/g, '    ').replace(/\u00A0/g, " ").replace(/\r\n?/g, "\n");

            var ns = this;
            if (ns._ovalue != value) {
                ns._ovalue = value;
                ns._dirty = false;

                if (ns.codeeditor) {
                    ns.codeeditor.setValue(ns._ovalue);
                }
            }
            return ns;
        },


        reindent: function () {
            this.codeeditor.reindent();
        },
        setReadonlyLines: function (head, tail) {
            this.codeeditor.setReadonlyLines(head, tail);
        },
        // 替换工具栏中的图标类
        iniComponents: function (com, threadid) {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0))
            };

            append((new ood.UI.ToolBar)
                .setHost(host, "toolbar4")
                .setItems([
                    {
                        "id": "fun", "sub": [
                            {
                                "id": "searchreplace",
                                "caption": "$RAD.pageEditor.searchreplace",
                                "imageClass": "ri-search-line",
                                "type": "button",
                                "tips": "$RAD.pageEditor.replacetips"
                            },
                            {
                                "id": "jumpto",
                                "caption": "$RAD.pageEditor.jumpto",
                                "imageClass": "ri-arrow-right-up-line",
                                "type": "button",
                                "tips": "$RAD.pageEditor.jumptotips"
                            },
                            {
                                "id": "indentall",
                                "caption": "$RAD.pageEditor.indentall",
                                "imageClass": "ri-indent-increase",
                                "type": "button",
                                "tips": "$RAD.pageEditor.indentalltips"
                            },
                            {"id": "rendermode", caption: "mode", "imageClass": "ri-code-line"},
                            {"id": "debug", caption: "调试", "imageClass": "ri-bug-line"},
                            {
                                "id": "jsoneditor",
                                hidden: false,
                                caption: "$RAD.spabuilder.menubar.jsoneditor",
                                imageClass: "ri-braces-line",
                                type: 'button',
                                tips: '$(RAD.pageEditor.Edit in JSON Editor)'
                            },
                            {
                                "id": "progress",
                                "object": (new ood.UI.Image({src: CONF.img_progress})).setHost(host, "imgProgress")
                            }
                        ]
                    }
                ])
                .onClick("_toolbar_onclick")
            );

            append((new RAD.expression.CodeEditor)
                .setHost(host, "codeeditor")
                .setDock("fill")
                .setValue(host._ovalue || "")
                .onValueChanged("_codeeditor_onChange")
                .onSaveCommand("_codeeditor_onsave")
                .onGetHelpInfo("_codeeditor_ongett")
                .onRendered("_codeeditor_onrender")
                .onCodeModeSet("_codeeditor_oncodemodeset")
            );

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events: {onReady: '_onready'},
        _onready: function (com) {
            if (com._codetype) {
                com.codeeditor.setCodeType(com._codetype, true);
            }

        },

        setDataToEditor: function (host, cls, api) {
            var ns=this;
            this.$cls = cls;
            this.$host = host;
            this.$api = api;
            if (ood.isStr(api)){
                ns.boxing().setValue(api);
            }else if (api.queryArgs && api.queryArgs.expression) {
                ns.boxing().setValue(api.queryArgs.expression);
            }
        },


        _codeeditor_onrender: function (profile, finished) {
            var ns = this;
            if (!ns.codeeditor) return;

            var id = ns.KEY + ":" + ns.$xid + ":progress";
            if (finished) {
                ood.resetRun(id);

                ns.toolbar4.setDisabled(false);
                ns.codeeditor.setReadonly(false);

                ns.imgProgress.setDisplay('none');

                if (!ns.$initializd) {
                    ns.$initializd = true;
                    ns.fireEvent('afterRendered');
                }
            } else {
                ood.resetRun(id, function () {
                    if (!ns.destroyed) {
                        ns.toolbar4.setDisabled(true);
                        ns.codeeditor.setReadonly(true);
                        ns.imgProgress.setDisplay('');
                    }
                });
            }
        },
        _codeeditor_ongett: function (profile, key) {
            ood.asyRun(function () {
                ood.Coder.applyById("doc:code", true);
            });
            return RAD.EditorTool.getDoc(key);
        },
        _codeeditor_oncodemodeset: function (profile, mode) {
            if (mode) {
                this.toolbar4.updateItem('rendermode', {
                    caption: "&nbsp;" + mode.name,
                    mime: mode.mime,
                    mode: mode.mode,
                    tips: 'Code was rendered as ' + "[" + mode.name + "]"
                });
            }
        },
        _codeeditor_onChange: function (profile, flag) {
            this._dirty = true;
            this.fireEvent("onValueChanged", [this, this.getValue()]);
        },
        _codeeditor_onsave: function (profile) {
            var ns = this;
            ns.fireEvent('onCommandSave', [ns]);
        },

        _toolbar_onclick: function (profile, item, grp, e, src) {
            var ns = this,
                editor = this.codeeditor,
                pos = ood.use(src).offset();
            switch (item.id) {
                case 'indentall':
                    editor.reindent();
                    break;
                case 'debug':
                    this.fireEvent("onRunExpression", [this, this.getValue()]);
                    break;
                case 'searchreplace':
                    RAD.EditorTool.showFindWnd(editor, pos, editor.getCM().getSelection());
                    break;
                case 'jumpto':
                    RAD.EditorTool.showJumpToWnd(editor, pos);
                    break;
                case 'rendermode':
                    editor.showModeSelDlg(item);
                    break;
                case 'jsoneditor':
                    var text = ns.getValue();
                    try {
                        ood.unserialize(text);
                    } catch (e) {
                        ood.alert(e);
                        return;
                    }
                    if (window.SPA && false === SPA.fe("beforeObjectEditorPop", [
                        "jsonEditor", null, text, function (txt) {
                            ns.setValue(typeof(txt) == 'string' ? txt : ood.stringify(txt));
                        },
                        "RAD.PageEditor", ns, ood(src).get(0), null, editor])) return;
                    ood.ModuleFactory.getCom('jsonEditor', function () {
                        this.setProperties({
                            caption: "$RAD.spabuilder.menubar.jsoneditor",
                            imageClass: "ri-braces-line",
                            text: text,
                            onOK: function (obj, txt) {
                                ns.setValue(txt);
                            }
                        });
                        this.show();
                    });
                    break;
            }
        }
    }
});
