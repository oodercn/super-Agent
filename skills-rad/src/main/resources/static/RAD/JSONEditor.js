ood.Class('RAD.JSONEditor', 'ood.Module', {
    Instance: {
        $PageEditor: null,
        activate: function () {
            this.tabs.setValue("editor");
        },
        setValue: function (str) {
            var ns = this,
                obj = ood.isStr(str) ? str ? ood.unserialize(str) : false : str;
            ns._rootArr = ood.isArr(obj);

            ns.setInnerValue(str);
        },
        setInnerValue: function (str, tab) {
            var ns = this,
                tab = tab || this.tabs.getUIValue(false);
            if (tab == "editor") {
                var obj = ood.isStr(str) ? str ? ood.unserialize(str) : false : str;
                var rows = ns._json2rows(obj, ns._rootArr);
                ns.tg.setRows(rows).free();
            } else {
                str = (ood.isStr(str) ? str : ood.serialize(str)) || "";
                if (ood.Coder) str = ood.Coder.formatText(str);
                ns.$PageEditor.setValue(str);
                ood.asyRun(function () {
                    ns.$PageEditor.activate();
                });
            }
        },
        getValue: function (returnObj, tab) {
            var str, tab = tab || this.tabs.getUIValue();
            if (tab == "editor") {
                str = this._rows2json(this.tg.getRows(), this._rootArr);
            } else {
                str = this.$PageEditor.getValue();
            }
            return returnObj ? ood.unserialize(str) : str;
        },
        getEditor: function () {
            return this.tg;
        },
        _dialog_beforeclose: function (profile) {
            var ns = this;
            ns.tg.removeAllRows();
            delete this.path;
            ns.$PageEditor.setValue("");

            ns.dialog.hide();
            return false;
        },
        _btnSave_onclick: function () {
            var self = this,
                prop = self.properties,
                txt = self.getValue();
            if (txt === false) return false;


            //check dirty
            if (txt) {

                if (!this.path) {
                    var arr = SPA.currentPage.split("/"),
                        name = "filename";
                    ood.showCom("RAD.AddFile", function () {
                        this.setProperties({
                            cls: 'save',
                            fileName: name,
                            parentPath: SPA.curProjectPath + "/data",
                            typeCaption: ood.adjustRes("$RAD.pm.normaljs"),
                            type: ".json",
                            tailTag: ".json",
                            forcedir: true,
                            content: txt
                        });
                        this.setEvents({
                            onOK: function () {
                                ood.message(ood.getRes('RAD.ps.saved', 1));
                            }
                        });
                    }, null, null, true);

                } else {

                    ood.Ajax(CONF.saveFile, {path: this.path, content: txt}, function () {
                            this.setEvents({
                                    onOK: function () {
                                        ood.message(ood.getRes('RAD.ps.saved', 1));
                                    }
                                }
                            );
                        }, function () {
                        },
                        null, {
                            rspType: 'json',
                            method: 'POST'
                        }).start();
                }


            }
            ;

        },
        _btncancel_onclick: function () {
            var ns = this;
            ns.tg.removeAllRows();
            ns.$PageEditor.setValue("");
            ns.dialog.close();
        },
        _check: function (txt) {
            var ns = this;
            //check first
            var result = RAD.CodeEditor.evalInSandbox("(" + txt + ")", false, null, null, true);
            if (result.ko) {
                ood.message(ood.getRes('RAD.JSEditor.codeerr'));
                return false;
            }
            if (ns._rootArr) {
                if (result.ok.menu) {
                    txt = ood.stringify(result.ok.menu, false);
                } else if (!ood.isArr(result.ok)) {
                    ood.message("Must be an Array!");
                    return false;
                }
            } else {
                if (!ood.isHash(result.ok)) {
                    ood.message("Must be an Object!");
                    return false;
                }
            }

            //parse comments and code, check code in the process
            result = RAD.ClassTool.parseSingleBlock(txt);
            if (false === result) {
                ood.message(ood.getRes('RAD.classtool.err1'));
                return false;
            }
            return result;
        },
        _btnok_onclick: function () {
            var self = this,
                prop = self.properties,
                txt = self.getValue(), result;
            if (result = self._check(txt)) {
                prop.result = result;
                prop.object = ood.unserialize(txt) || null;
                ood.tryF(prop.onOK, [self, txt], self.host);
                self.dialog.close();
            }
        },
        _tabs_beforeuivalueset: function (profile, oldValue, newValue, force, tag) {
            var ns = this, value = ns.getValue(false, oldValue);
            if (ns._check(value)) {
                ns.setInnerValue(value, newValue);
            } else {
                return false;
            }
        },
        customAppend: function (parent) {
            var page = this,
                prop = page.properties,
                dlg = page.dialog;
            dlg.setCaption(prop.caption).setImage(prop.image).setImagePos(prop.imagePos);

            if (prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);

            if (!page.$PageEditor) {
                var pe = new RAD.PageEditor();
                pe.setType("json.ex");
                pe.setHost(page, "$PageEditor");
                page.$PageEditor = pe;
                page.tabs.append(pe, "code");

                pe.codeeditor.setCodeType("json", true);
            }
            if (!SPA.currentPage) {
                page.btnSave.setDisplay('none');
                page.btnLoad.setDisplay('none');
            }


            page.setValue(prop.text || "");

            dlg.showModal(parent);

            ood('body').append(page.ood_ui_block5);
        },
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("4.375em")
                    .setTop("2.5em")
                    .setWidth("47.75em")
                    .setHeight("32.5em")
                    .setCaption("dialog")
                    .setMinBtn(false)
                    .beforeClose("_dialog_beforeclose")
            );

            host.dialog.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "panelB")
                    .setDock("bottom")
                    .setHeight("3em")
            );

            host.panelB.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "panelR")
                    .setDock("right")
                    .setWidth("18.5em")
            );

            host.panelR.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnCancel")
                    .setLeft("0.8333333333333334em")
                    .setTop("0.5833333333333334em")
                    .setWidth("6.083333333333333em")
                    .setCaption("$inline.cancel")
                    .setImageClass("ri-close-line") // 原"spafont spa-icon-cancel" (已更新为ri-close-line)
                    .onClick("_btncancel_onclick")
            );

            host.panelR.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnOK")
                    .setLeft("9.166666666666666em")
                    .setTop("0.5833333333333334em")
                    .setWidth("6.083333333333333em")
                    .setCaption("$inline.ok")
                    .setImageClass("ri-check-line") // 原"spafont spa-icon-ok" (已更新为ri-check-line)
                    .onClick("_btnok_onclick")
            );

            host.panelB.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btnLoad")
                    .setHoverPop("ood_ui_block5")
                    .setLeft("1em")
                    .setTop("0.6666666666666666em")
                    .setWidth("5.833333333333333em")
                    .setCaption("$RAD.Load")
                    .setImageClass("ri-arrow-left-line")
            );

            // host.panelB.append(
            //     ood.create("ood.UI.Button")
            //         .setHost(host, "btnSave")
            //         .setLeft("9.166666666666666em")
            //         .setTop("0.6666666666666666em")
            //         .setWidth("5.833333333333333em")
            //         .setCaption("$RAD.save")
            //         .setImageClass("ri-save-line")
            //         .onClick("_btnSave_onclick")
            // );

            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "panelMain")
                    .setDock("fill")
                    .setBorderType("inset")
            );

            host.panelMain.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host, "tabs")
                    .setItems([
                        {
                            "id": "code",
                            "tips": '将AI代码再此粘贴',
                            "caption": "AI-Code"
                        },
                        {
                            "id": "editor",
                            "caption": "$RAD.spabuilder.menubar.jsoneditor",
                            "imageClass": "ri-hashtag"
                        }
                    ])
                    .setLeft("15.833333333333334em")
                    .setTop("5em")
                    .setValue("editor")
                    .beforeUIValueSet("_tabs_beforeuivalueset")
            );

            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "tg")
                    .setTogglePlaceholder(true)
                    .setEditable(true)
                    .setInitFold(false)
                    .setRowHandler(false)
                    .setColSortable(false)
                    .setHeader([
                        {
                            "id": "key",
                            "width": "8.333333333333334em",
                            "type": "input",
                            "caption": "key",
                            "editorCacheKey": "input",
                            "colResizer": true,
                            "flexSize": true
                        },
                        {
                            "id": "value",
                            "width": "16.666666666666668em",
                            "type": "textarea",
                            "caption": "value",
                            "editorCacheKey": "textarea",
                            "flexSize": true
                        }
                    ])
                    .setTagCmds([
                        {
                            "id": "add",
                            "type": "text",
                            "caption": "",
                            "location": "right",
                            "itemClass": "ri-add-circle-line",
                            "pos": "header row",
                            "tips": "Append a child"
                        },
                        {
                            "id": "up",
                            "type": "text",
                            "location": "right",
                            "itemClass": "ri-arrow-up-line",
                            "pos": "row",
                            "tips": "Add a node to the front of the node"
                        },
                        {
                            "id": "down",
                            "type": "text",
                            "location": "right",
                            "itemClass": "ri-arrow-down-line",
                            "pos": "row",
                            "tips": "Add a node at the back of this node"
                        },
                        {
                            "id": "del",
                            "type": "text",
                            "location": "right",
                            "itemClass": "ri-close-line",
                            "pos": "row",
                            "tips": "Delete this node"
                        }
                    ])
                    .setTreeMode("infirstcell")
                    .onCmd("_tg_oncmd")
                    .beforeRowActive("_tg_beforerowactive")
                    .beforeCellUpdated("_tg_beforecellupdated")
                    .beforeIniEditor("_tg_beforeIniEditor")
                    .onBeginEdit("_tg_onEdit")
                    .beforeEditApply("_tg_beforecellapply")
                , "editor");

            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block5")
                    .setLeft("5.833333333333333em")
                    .setTop("34.25em")
                    .setWidth("30em")
                    .setHeight("15.833333333333334em")
                    .setBorderType("flat")
            );

            host.ood_ui_block5.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "treeview")
                    .setLeft("0em")
                    .setTop("4.166666666666667em")
                    .setSingleOpen(true)
                    .setDynDestory(true)
                    .setSelMode('none')
                    .setItems([{id: "data", sub: true}])
                    .onClick("_treebarprj_onclick")
                    .onGetContent("_treebarprj_onGetContent")
                    .onItemSelected("_treebarprj_onitemselected")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _getCellValue: function (n) {
            var ns = this, v;
            try {
                v = ood.str.trim(n);
                //special string
                if (/^'/.test(v) && !ns._isString(v.slice(1))) {
                    v = ['string', v.slice(1)];
                } else {
                    v = v.replace(/^\s*/, '').replace(/\s*$/, '');
                    v = v == 'null' ? ['null', 'null'] :
                        //number
                        ood.isFinite(v) ? ['number', v] :
                            //reg
                            /^\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*$/.test(v) ? ['regexp', v] :
                                //bool
                                /^(true|false)$/.test(v) ? ['boolean', v.toLowerCase()] :
                                    //date
                                    /^new Date\([0-9 \,]*\)$/i.test(v) ? ['date', ood.serialize(ood.unserialize(v))] :
                                        //function
                                        /^((\s*function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)(\{([^\{\}]*)\}))\s*$/i.test(v) && ood.isFun(ood.unserialize(v)) ? ['function', v] :
                                            //hash
                                            /^\{[\s\S]*\}$/.test(v) && ood.isHash(ood.unserialize(v)) ? ['hash', ood.stringify(ood.unserialize(v))] :
                                                //array
                                                /^\[[\s\S]*\]$/.test(v) && ood.isArr(ood.unserialize(v)) ? ['array', ood.stringify(ood.unserialize(v))] :
                                                    ['string', n];
                }
            } catch (e) {
                v = null;
            }
            if (v[0] == 'string') {
                if (v[1] === false)
                    return null;
                v[1] = ood.stringify(v[1]);
            }
            if (v[1] === "false" && v[0] != 'string')
                v[0] = 'boolean';
            return v;
        },
        _json2rows: function (obj, array, rows) {
            var ns = this, me = arguments.callee;
            if (!rows) rows = [];
            if (obj) {
                if (obj.menu) {
                    obj = obj.menu;
                }
                if (obj.sub_menus) {
                    obj = obj.sub_menus;
                }
                if (obj.sub) {
                    obj = obj.sub;
                }
                ood.each(obj, function (o, i) {
                    if (!obj.hasOwnProperty(i)) return;
                    var row = {}, type = ns._getType(o);
                    i = {value: array ? '[' + i + ']' : i, readonly: array};

                    if (type == 'hash') {
                        row.sub = [];
                        row.cells = [i, {value: '{...}'}, ''];
                        me.call(ns, o, false, row.sub);
                    } else if (type == 'array') {
                        row.sub = [];
                        row.cells = [i, {value: '[...]'}, ''];
                        me.call(ns, o, true, row.sub);
                    } else {
                        //ns._getType(o);
                        row.cells = [i, ood.stringify(o), ''];
                    }
                    row._type = type;
                    row.caption = "";
                    rows.push(row);
                });
            }
            return rows;
        },
        _getType: function (o) {
            return o === null ? null :
                ood.isStr(o) ? 'string' :
                    ood.isNumb(o) ? 'number' :
                        ood.isHash(o) ? 'hash' :
                            ood.isArr(o) ? 'array' :
                                ood.isBool(o) ? 'boolean' :
                                    ood.isDate(o) ? 'date' :
                                        ood.isReg(o) ? 'regexp' :
                                            ood.isFun(o) ? 'function' :
                                                'undefined';
        },
        _rows2json: function (arr, array) {
            var me = arguments.callee,
                a = [], key, value;
            ood.arr.each(arr, function (o) {
                key = ((typeof o.cells[0] == 'object') ? o.cells[0].value : o.cells[0]);
                if (o._type == 'hash'){
                    value = me(o.sub);
                }   else if (o._type == 'array'){
                    value = me(o.sub, true);
                }  else{
                    value = (typeof o.cells[1] == 'object') ? o.cells[1].value : o.cells[1];
                }
                if (array){
                    a.push(value);
                } else{
                    if (key=='icon' && !a['imageClass']){
                        a.push('"imageClass":' + value);
                    }
                    a.push('"' + key + '":' + value);
                }

            });
            return array ? '[' + a.join(',') + ']' : '{' + a.join(',') + '}';
        },
        _tg_onEdit: function (profile, obj, editor, type) {
            if (profile.properties.multiLineValue)
                editor.getSubNode("INPUT").scrollTop(0);
            this.fireEvent("onEdit", [obj._col.id, editor]);
        },
        // for value
        _tg_beforeIniEditor: function (profile, obj, cellNode, pNode, type) {
            var ns = this;
            if (type != 'cell') return;
            if (obj._col.id != 'value') return;

            var type = obj._row._type;
            if (type == 'hash' || type == 'array') {
                var str = this._rows2json(obj._row.sub, type == 'array');
                if (ood.Coder) str = ood.Coder.formatText(str);
                obj.$editorValue = str;
            } else if (type == 'string') {
                var v = ood.unserialize(obj.value);
                //number
                if (!ns._isString(v)) {
                    obj.$editorValue = "'" + v;
                } else {
                    obj.$editorValue = v;
                }
            }
        },
        _isString: function (v) {
            return !(v == 'undifined' || v == 'null' || v == 'NaN' ||
                ood.isFinite(v) ||
                //reg
                /^\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*$/.test(v) ||
                //bool
                /^(true|false)$/.test(v) ||
                //date
                /^new Date\([0-9 \,]*\)$/i.test(v) ||
                //function
                (/^((function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)(\{([^\{\}]*)\}))$/i.test(v) && ood.isFun(ood.unserialize(v))) ||
                //hash
                (/^\{[\s\S]*\}$/.test(v) && ood.isHash(ood.unserialize(v))) ||
                //array
                (/^\[[\s\S]*\]$/.test(v)) && ood.isArr(ood.unserialize(v)));
        },
        _tg_beforecellapply: function (profile, cc, options, editor, tag) {
            if (tag == 'asycheck') return false;
        },
        _tg_beforecellupdated: function (profile, cell, options) {
            var ns = this,
                map = {'hash': 1, 'array': 2},
                row = cell._row,
                rowId = row.id,
                tg = profile.boxing();
            if (cell._col.id == 'value') {
                var va = this._getCellValue(options.value);
                if (!va) {
                    alert('Text format is not valid!');
                    return false;
                } else {
                    var ops = {};
                    options.value = va[1];

                    if (map[va[0]]) {
                        ops.sub = this._json2rows(ood.unserialize(va[1]), va[0] == 'array');
                        options.caption = va[0] == 'hash' ? '{...}' : '[...]';
                    } else {
                        if (row.sub) ops.sub = null;
                    }
                    ood.asyRun(function () {
                        if (tg.isDestroyed()) return;
                        tg.updateRow(rowId, ops);
                        // must get
                        row = tg.getRowbyRowId(rowId);
                        if (row) row._type = va[0];

                        ns.fireEvent("onchange", [ns]);
                    }, 100);
                }
            } else {
                if (!/^"(\\.|[^"\\])*"$/.test('"' + options.value + '"')) {
                    alert('Text format is not valid!');
                    return false;
                }
                ood.asyRun(function () {
                    ns.fireEvent("onchange", [ns]);
                }, 100);
            }
        },
        _tg_beforerowactive: function () {
            return false;
        },
        _treebarprj_onclick: function (profile, item) {
            if (item.sub)
                profile.boxing().toggleNode(item.id);
        },
        _treebarprj_onitemselected: function (profile, item, e, src, type) {
            var ns = this;
            if (!item.sub) {
                this.path = item.id;
                ood.Ajax(CONF.getFileContent, {path: item.id, packagePath: SPA.curProjectPath}, function (obj) {
                    if (obj && obj.data && obj.data.content) {
                        ns.setValue(obj.data.content);
                    }

                }, function (msg) {
                    ood.message(msg);
                }).start();


                this.ood_ui_block5.hide();
            }
        },
        _treebarprj_onGetContent: function (profile, item, callback) {
            var path = (item.id == 'data') ? (SPA.curProjectPath + "/data") : item.id;
            var ns = this;

            ood.Ajax(CONF.openFolderService, {path: path, curProjectPath: SPA.curProjectPath, hashCode: ood.id()}
                , function (txt) {
                    var obj = txt, items = [];
                    if (obj && !obj.error) {
                        if (ood.get(obj, ['data', 'files'])) {
                            ood.arr.each(obj.data.files, function (file) {
                                items.push({
                                    id: file.location,
                                    caption: file.name,
                                    sub: file.type === 0
                                })
                            });
                        }
                        callback(items);
                    } else ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                }, null, null, {method: 'POST'});
        },
        _tg_oncmd: function (profile, row, cmdkey, e, src) {
            var ns = this,
                tg = profile.boxing(),
                type = row ? row._type : ns._rootArr ? 'array' : 'hash',
                ptype, prow, nid;

            if (row && row._pid) {
                prow = profile.rowMap[row._pid];
                ptype = prow && prow._type;
            } else {
                prow = {sub: profile.properties.rows};
                ptype = ns._rootArr ? 'array' : 'hash';
            }
            switch (cmdkey) {
                case 'add':
                    nid = ood.stamp();
                    if (row) {
                        if (type == "array" || type == "hash") {
                            tg.insertRows([{
                                id: nid,
                                cells: [{
                                    value: type == 'array' ? '[index]' : ood.rand(),
                                    readonly: type == 'array'
                                }, 'null', '']
                            }], row.id);
                        } else {
                            var id = row.id;
                            ood.confirm("Hash or Array", "Modify this node as an Hash or Array?", function () {
                                tg.updateCellByRowCol(id, "value", "{" + ood.rand() + ":" + row.cells[1].value + "}", false, true);
                                ood.asyRun(function () {
                                    tg.editCellbyRowCol(id, "value");
                                }, 200);
                            }, function (type) {
                                if (type == 'close') return;
                                var id = row.id;
                                tg.updateCellByRowCol(id, "value", "[" + row.cells[1].value + "]", false, true);
                                ood.asyRun(function () {
                                    tg.editCellbyRowCol(id, "value");
                                }, 200);
                            }, 'As a Hash', 'As an Array');
                            return;
                        }
                    } else {
                        tg.insertRows([{
                            id: nid,
                            cells: [{
                                value: type == 'array' ? '[index]' : ood.rand(),
                                readonly: type == 'array'
                            }, 'null', '']
                        }]);
                    }
                    break;
                case 'up':
                    nid = ood.stamp();
                    tg.insertRows([{
                        id: nid,
                        cells: [{
                            value: ptype == 'array' ? '[index]' : ood.rand(),
                            readonly: ptype == 'array'
                        }, 'null', '']
                    }], null, row.id, true);
                    break;
                case 'down':
                    nid = ood.stamp();
                    tg.insertRows([{
                        id: nid,
                        cells: [{
                            value: ptype == 'array' ? '[index]' : ood.rand(),
                            readonly: ptype == 'array'
                        }, 'null', '']
                    }], null, row.id, false);
                    break;
                case 'del':
                    // ood.confirm('confirm','Do you want to delete this node?',function(){
                    tg.removeRows([row.id]);
                    //  });
                    break;
            }
            if (row && type == 'array') {
                // re index for array
                ood.arr.each(row.sub, function (row, i) {
                    var cell = row.cells[0];
                    profile.boxing().updateCell(cell, {caption: '[' + i + ']'});
                });
            }
            else if (prow && ptype == 'array') {
                // re index for array
                ood.arr.each(prow.sub, function (row, i) {
                    var cell = row.cells[0];
                    profile.boxing().updateCell(cell, {caption: '[' + i + ']'});
                });
            }
            if (nid) {
                ood.asyRun(function () {
                    tg.editCellbyRowCol(nid + '', ptype == 'array' ? "value" : "key");
                });
            }
            ood.asyRun(function () {
                ns.fireEvent("onchange", [ns]);
            }, 100);
        }
    }
});
