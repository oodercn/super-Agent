ood.Class('RAD.MixPropEditor', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("10em")
                    .setTop("2.5em")
                    .setWidth("44.166666666666664em")
                    .setHeight("22.5em")
                    .setShadow(false)
                    .setResizer(false)
                    .setCaption("$(RAD.designer.attreditor.Attributes Editor)")
                    .setMinBtn(false)
                    .onHotKeydown("_dialog_onhotkeydown")
                    .beforeClose("_dialog_beforeclose")
            );

            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block1")
                    .setDock("fill")
                    .setBorderType("inset")
            );

            host.ctl_block1.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "grid")
                    .setShowDirtyMark(false)
                    .setRowNumbered(true)
                    .setEditable(true)
                    .setUidColumn('id')
                    .setRowHandlerWidth("6em")
                    .setTagCmds([
                        {
                            "id": "del1",
                            "location": "left",
                            "itemClass": "oodcon ood-uicmd-close",
                            "pos": "header",
                            "tips": "清空数据"
                        },
                        {
                            "id": "del2",
                            "location": "left",
                            "pos": "row",
                            "itemClass": "oodcon ood-uicmd-close",
                            "tips": "移除当前行"
                        }
                    ])
                    .setTreeMode('inhandler')
                    .setHotRowMode("show")
                    .setDropKeys("haha")
                    .setDragKey("haha")
                    .onInitHotRow("_grid_oninithotrow")
                    .beforeHotRowAdded("_gridTrend_beforehotrowadded")
                    .afterHotRowAdded("_gridTrend_afterHotRowAdded")
                    .onCmd("_grid_ontagcmd")
                    .beforeCellUpdated("_grid_beforecellupdated")
                    .afterCellUpdated("_grid_aftercellupdated")
                    .onClickCell("_grid_onclickcell")
                    .beforeComboPop("_grid_beforepop")
                    .onCommand("_grid_oncommand")
                    .onDrop("_grid_ondrop")
            );

            host.ctl_block1.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block4")
                    .setDock("bottom")
                    .setHeight("3.0833333333333335em")
            );

            host.ctl_block4.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ood_ui_div19")
                    .setTop("0em")
                    .setWidth("17.416666666666668em")
                    .setHeight("2.9166666666666665em")
                    .setRight("0em")
            );

            host.ood_ui_div19.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_ba")
                    .setLeft("0em")
                    .setTop("0.5833333333333334em")
                    .setWidth("6.583333333333333em")
                    .setHeight("2em")
                    .setCaption("$RAD.designer.attreditor.Apply")
                    .onClick("_ctl_ba_onclick")
            );

            host.ood_ui_div19.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_bc")
                    .setLeft("8.083333333333334em")
                    .setTop("0.5833333333333334em")
                    .setWidth("6.583333333333333em")
                    .setHeight("2em")
                    .setCaption("$RAD.designer.attreditor.Cancel")
                    .onClick("_ctl_bc_onclick")
            );

            host.ctl_block4.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ood_ui_button7")
                    .setLeft("1.5833333333333333em")
                    .setTop("0.5em")
                    .setWidth("6.583333333333333em")
                    .setHeight("2em")
                    .setCaption("[JSON]")
                    .onClick("_ood_ui_button7_onclick")
            );

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend: function (parent, subId, left, top) {
            var ns = this, prop = ns.properties
            target = prop.targetProfile.boxing()
            ;

            if (prop.subheader && prop.subheader.length) {
                prop.uiHeader.push({
                    id: "[sub]",
                    caption: '$(RAD.designer.attreditor.[sub])',
                    type: "button",
                    cellRenderer: function () {
                        return ood.getRes('RAD.designer.attreditor.' + prop.innerKey)
                    }
                });
            }

            // Give a chance to modify editor setting for rows
            window.SPA && SPA.fe("onInitEditorRows", [prop.propKey, prop.propN, prop.uiRows,
                'RAD.MixPropEditor', target, prop.uiRows, ns.grid]);

            if (window.SPA && SPA.events.onInitPropValueCell) {
                ood.arr.each(prop.uiRows, function (row) {
                    ood.arr.each(row.cells, function (cell, i) {
                        // Give a chance to modify editor setting for value cell
                        window.SPA && SPA.fe("onInitPropValueCell", [prop.propKey, prop.uiHeader[i].id, cell.value, "RAD.MixPropEditor", target, cell, ns.grid]);
                    });
                });
            }
            ns.grid.get(0)._targetProfile = prop.targetProfile;
            ns.grid.get(0)._targetPropKey = prop.propKey;

            ns.grid.setHeader(prop.uiHeader, true);
            ns.grid.setRows(prop.uiRows, true);

            ns._dirty = 0;

            var unFun = function () {
                ns.dialog.close();
            };
            ood("body").append(ns.dialog);
            ns.dialog.getRoot()
                .popToTop(ood(prop.src), null, null, function () {
                    if (prop.fixedRows) {
                        ns.grid.setHotRowMode('none');
                    } else {
                        ns.grid.setHotRowMode('show');
                        ns.grid.addHotRow();
                    }
                })
                .setBlurTrigger("RAD.MixPropEditor" + ns.$xid, unFun);

            if (prop.treeMode) {
                if (prop.treeMode == 'infirstcell') {
                    ns.grid.setTreeMode('infirstcell').setRowHandlerWidth('6em');
                } else if (prop.treeMode == 'none') {
                    ns.grid.setTreeMode('none').setRowHandlerWidth('4em');
                } else if (prop.treeMode == 'inhandler') {
                    ns.grid.setTreeMode('inhandler').setRowHandlerWidth('6em');
                }
            } else {
                ns.grid.setTreeMode('none').setRowHandlerWidth('4em');
            }


            ns.dialog.activate();

            return true;
        },
        _grid_beforecellupdated: function (profile, cell, options, isHotRow) {
            var ns = this;
            if ("value" in options && ns.properties.uniqueKey && ns.properties.uniqueKey === cell._col.id) {
                var grid = profile.boxing(), colid = cell._col.id, ok = true;
                ood.each(grid.getCells(null, colid, true), function (ac) {
                    if (cell !== ac && ac.value === options.value) return ok = false;
                });
                if (!ok) {
                    ood.message(ood.adjustRes("$(RAD.msg.$0 '$1' already exists-" + colid + "-" + options.value + ")"));
                    return false;
                }
            }
        },
        _grid_oninithotrow: function (profile) {
            var cols = profile.properties.header, cells = [];
            for (var i = 0, l = cols.length; i < l; i++)
                cells.push({});
            if (cols[cols.length - 1].id == "[sub]")
                cells[cols.length - 1].disabled = true;
            return cells;
        },
        _grid_aftercellupdated: function (profile, cell, options, isHotRow) {
            var ns = this;
            if ("value" in options) {
                if (ns.properties.fc) {
                    if (ood.isStr(cell.value) && cell.value.charAt(0) == '#') {
                        cell.value = cell.value.replace('#', '');
                    }
                }
            }
            cell.needCollect = 1;
            ns._dirty = 1;
        },
        _grid_ondrop: function (profile) {
            var ns = this;
            ns._dirty = 1;
            return false;
        },
        _grid_oncommand: function (profile, cell, proEditor, src, type) {
            var tagVar = cell._col.tagVar;
            if (tagVar) {
                if (tagVar.prop == 'imageClass') {
                    ood.getModule("RAD.SelFontAwesome", function (tid, module) {
                        module.render(true);
                        module.getRoot().popToTop(src)
                            .setBlurTrigger('design:pop:SelFontAwesome', function () {
                                module.destroy();
                            });

                        this.setEvents("onSel", function (value) {
                            if (proEditor) {
                                proEditor.boxing().setUIValue(value);
                            } else {
                                // 2. dont trigger onchange event
                                profile.boxing().updateCell(cell, {value: value});
                            }
                        });
                    });
                }
            }
        },
        _grid_ontagcmd: function (profile, row, cmdkey, e, src) {
            if (cmdkey != 'del1' && cmdkey != 'del2') return;

            var ns = this;
            ood.confirm('$RAD.Remove?', ood.getRes(row ? 'RAD.Are You Sure to remove this row' : 'Are You Sure to remove all') + '?', function () {
                if (row)
                    profile.boxing().removeRows([row.id]);
                else
                    profile.boxing().removeAllRows();
                ns._dirty = 1;
            }, null, null, null, ns.dialog.getRoot().cssRegion());
        },
        _grid_beforepop: function (profile, cell, editorprf, pos, e, src) {
            var prop = this.properties;
            if (window.SPA && false === SPA.fe("beforeEditorComboPop", [
                prop.propKey, cell._col.id, cell.value, function (value) {
                    editorprf.boxing().setUIValue(value || "", true);
                }, "RAD.MixPropEditor", prop.targetProfile.boxing(), pos, profile.getSubNode('CELL', cell._serialId).get(0), cell, profile.boxing(), editorprf && editorprf.boxing()
            ])) return false;
        },
        _gridTrend_afterHotRowAdded: function (profile, row) {
            var ns = this,
                prop = ns.properties;
            // special for treegrid row: to init cells
            if (prop.headerSize) {
                var cells = [];
                for (var i = 0, l = prop.headerSize; i < l; i++)
                    cells.push({value: ""});
                row._innerData = cells;
            }
            ood.arr.each(row.cells, function (cell) {
                if (cell.value) cell.needCollect = 1;
            });
        },
        _gridTrend_beforehotrowadded: function (profile, cellMap, row) {
            var ns = this,
                prop = ns.properties,
                uictrl = profile.boxing(),
                value = cellMap[profile.properties.header[0].id];
            if (ood.isSet(value) && value !== "") {
                if (ns.properties.treeMode)
                    row.sub = [];
                ood.arr.each(row.cells, function (cell) {
                    delete cell.disabled;
                });
                ns._dirty = 1;
                return true;
            } else {
                return false;
            }
        },

        _dialog_beforeclose: function (profile) {
            var ns = this;
            ns.dialog.getRoot().setBlurTrigger("RAD.MixPropEditor" + ns.$xid);

            if (!ns._noSave)
                ns._apply();

            ns.dialog.hide();
            ns.grid.removeAllRows();
            // reset row inner id
            ns.grid.get(0)._id = new ood.id();

            ood(ns.properties.src).focus();

            return false;
        },
        collectData: function (arows, header, brows) {
            var ns = this;
            if (!brows) brows = [];
            ood.arr.each(arows, function (row) {
                if (ood.UI.TreeGrid.isHotRow(row)) return;
                data = {};
                ood.arr.each(row.cells, function (cell, i) {
                    if (cell.needCollect) {
                        if (ns.properties.fc) {
                            data[header[i].toLowerCase()] = cell.value === false ? "0" : cell.value === true ? "1" : (cell.value + "");
                        } else {
                            data[header[i]] = cell.obj || cell.value;
                        }
                    }
                });
                if (row._innerData && ns.properties.innerKey) {
                    data[ns.properties.innerKey] = ood.clone(row._innerData, function (o, i) {
                        return (i + '').charAt(0) != '_'
                    })
                }
                brows.push(data);

                if (row.sub && row.sub.length) {
                    data.sub = [];
                    ns.collectData(row.sub, header, data.sub);
                }
            });
            return brows;
        },
        _apply: function () {
            var ns = this;
            if (ns._dirty) {
                var grid = ns.grid,
                    header = grid.getHeader('min');

                rows = ns.collectData(grid.getRows(), header);

                // avoid blurtrigger call _apply twice
                ns._dirty = 0;
                ns._noSave = 1;
                ns.fireEvent('onChanged', [rows, header]);
                delete ns._noSave;
            }
        },
        _ctl_ba_onclick: function (profile, e, src, value) {
            var ns = this;
            ns._apply();
            ns._noSave = 1;
            ns.dialog.close();
            delete ns._noSave;
        },
        _ctl_bc_onclick: function (profile, e, src, value) {
            var ns = this;
            ns._noSave = 1;
            ns.dialog.close();
            delete ns._noSave;
        },
        _grid_onclickcell: function (profile, cell, e, src) {
            var ns = this, prop = ns.properties;
            if (cell._col.id == "[sub]") {
                if (profile.box.isHotRow(cell._row)) return;

                if (!cell._row._innerData) cell._row._innerData = new Array(prop.fixedRows);

                var rows = [], row, acell;
                ood.arr.each(cell._row._innerData, function (item, index) {
                    row = {cells: []};
                    ood.arr.each(prop.subheader, function (col, j) {
                        var id = (col.id || col) + "";
                        //if(id=='renderer'/*||id=='tagVar'*/){
                        //    row.cells.push({disabled:true});
                        //    return;
                        //}
                        acell = {};
                        if (ood.isHash(item) && (id in item)) {
                            if (typeof item[id] == "object" || typeof item[id] == "function") {
                                acell.obj = item[id];
                            } else {
                                acell.value = item[id];
                            }
                            acell.needCollect = 1;
                        } else if (id == 'id') {
                            acell.value = item;
                            acell.needCollect = 1;
                        }
                        row.cells.push(acell);
                    });
                    rows.push(row);
                });

                ood.ComFactory.newCom('RAD.MixPropEditor', function () {
                    this.setProperties({
                        targetProfile: prop.targetProfile,
                        propKey: prop.propKey + "[sub]",

                        uiHeader: prop.subheader,
                        uiRows: rows,
                        treeMode: false,
                        fixedRows: prop.subFixedRows,
                        uniqueKey: prop.uniqueKey,
                        src: src
                    }).setEvents({
                        onChanged: function (items) {
                            cell._row._innerData = items;
                            ood(src).focus();

                            // parent editor
                            ns._dirty = 1;
                        }
                    });
                    this.show();
                });
            }
        },
        _dialog_onhotkeydown: function (profile, keyboard, e, src) {
            var ns = this;
            if (keyboard.key == 'esc')
                ns.dialog.close();
        },
        _ood_ui_button7_onclick: function (profile, e, src, value) {
            var ns = this,
                grid = ns.grid, target = grid;
            header = grid.getHeader('min'),
                rows = ns.collectData(grid.getRows(), header);
            var conf = {
                header: header,
                rows: rows,
                hasSub: true
            }


            ood.ModuleFactory.getCom('jsonEditor', function () {
                this.setProperties({
                    caption: 'Object Code',
                    imageClass: 'ri-braces-line',
                    text: rows,
                    onOK: function (obj, txt) {
                        ns._dirty = 1;
                        var collectData = function (items, rows) {
                            ood.arr.each(items, function (item, index) {
                                row = {'cells': []};
                                var ii = 0;
                                ood.arr.each(conf.header, function (col, j) {
                                    var id = (col.id || col) + "";
                                    //if(id=='renderer'/*||id=='tagVar'*/){
                                    //    row.cells.push({disabled:true});
                                    //    return;
                                    //}
                                    if (ood.absObj.$specialChars[id.charAt(0)]) return

                                    cell = {};
                                    if (conf.rows && conf.rows[item.id]) {
                                        cell = conf.rows[item.id].cells[j] ? ood.copy(conf.rows[item.id].cells[j]) : {};
                                        if (!('value' in cell)) {
                                            if (ood.isHash(item)) {
                                                if (id in item) {
                                                    if (typeof item[id] == "object" || typeof item[id] == "function") {
                                                        cell.obj = item[id];
                                                    } else {
                                                        cell.value = item[id];
                                                    }
                                                    cell.needCollect = 1;
                                                }
                                            } else if (id == 'id') {
                                                cell.value = item;
                                                cell.needCollect = 1;
                                            }
                                        }
                                    } else {
                                        if (ood.isHash(item) && (id in item)) {
                                            if (typeof item[id] == "object" || typeof item[id] == "function") {
                                                cell.obj = item[id];
                                            } else {
                                                cell.value = item[id];
                                            }
                                            cell.needCollect = 1;
                                        } else if (id == 'id') {
                                            cell.value = item;
                                            cell.needCollect = 1;
                                        }
                                    }
                                    // for those default is _true (For example: initFold )
                                    // if (!('value' in cell) && ('dftValue' in conf.header[ii])) {
                                    //     cell.value = header[ii].dftValue;
                                    // }
                                    row.cells.push(cell);
                                    // ignore hot row
                                    if (id == "id" && cell.value == ood.UI.TreeGrid._temprowid)
                                        row._ignore = 1;
                                    // this row cant be deleted
                                    if (conf.rows && conf.rows[item.id])
                                        row.tag = conf.rows[item.id].tag;
                                    ii++;
                                });
                                if (row._ignore) {
                                    return;
                                }
                                rows.push(row);
                                if (conf.innerKey) {
                                    row._innerData = ood.clone(item[conf.innerKey], function (o, i) {
                                        return (i + '').charAt(0) != '_'
                                    });
                                }
                                if (conf.hasSub) {
                                    row.sub = [];
                                    if (item.sub && item.sub.length) {
                                        item.sub = ood.UI.TreeGrid._adjustRows(ns, item.sub);

                                        var _id = new ood.id();
                                        ood.arr.each(item.sub, function (o, i) {
                                            if (ood.isHash(o) && !('id' in o)) o.id = item.id + "-" + _id.next();
                                        });

                                        collectData(item.sub, row.sub);
                                    }
                                }
                            });
                        };
                        var rows = [];
                        collectData(obj.properties.object, rows);
                        ns.grid.setRows(rows, true);

                    }
                });
                this.show();
            });
        }
    }
});
