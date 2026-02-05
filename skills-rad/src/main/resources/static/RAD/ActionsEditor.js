ood.Class('RAD.ActionsEditor', 'ood.Module', {
    Instance: {
        CAPMAP: {
            actiongroup: {
                'none:none': "None",
                'other:callback': "Function Calls",
                'other:var': "Assignment",
                'other:msg': "Message",
                'other:url': "System Functions",
                page: 'Page',
                'control': 'Control',
                module: 'Module'
            },
            action: {
                none: "None",
                'anim apply': 'Apply the animation to',
                setQueryData: "Set Query Data",
                setProperties: "Modify Properties",
                invoke: "Invoke",
                destroy: "Destroy",
                updateValue: "Update Value",
                resetValue: "Reset Value",
                clearValue: "Clear Value",
                getDirtied: "Get Dirtied",
                updateDataFromUI: "Update Data from UI",
                updateDataToUI: "Update Data to UI",
                start: "Start",
                suspend: "Suspend",
                animate: "Animate",
                activate: "Activate",
                disable: "Disable",
                enable: "Enable",
                hideItems: "Hide Items",
                showItems: "Show Items",
                busy: "Lock Screen",
                free: "Unlock Screen",
                show: "Show",
                hide: "Hide",
                popUp: "Pop to Front",
                setFormValues: "Fill Form",
                checkValid: "Form Validation",
                isDirtied: "Determine dirtied",
                setData: "Set Data",
                showModule2: "showModule2",
                getDate: "Get Data",
                checkRequired: "Check Required",
                formSubmit: "Submit Form",
                getFormValues: "Get form values",
                formReset: "Reset Form",
                formClear: "Clear Form",
                updateFormValues: "Update Form",
                getUIValue: "Get value",
                setUIValue: "Set value",
                setCaption: "Set caption",
                activeItem: "Activate Item",
                fireClickEvent: "Click it",
                fireItemClickEvent: "Click Item",
                dumpContainer: "Dump Container",
                setToggle: "Expand or Folding",
                toggleNode: "Expand or Folding",
                getHotRow: "Get hot row",
                isCellDirtied: "Is cell dirtied",
                updateCellValue: "Remove cell dirty",
                isRowDirtied: "Is row dirtied",
                updateRowValue: "Remove row dirty",
                updateHotRow: "Update hot row",
                getRowMap: "Get Cells Data",
                getHeaderByCell: "Get column by cell",
                getRowbyCell: "Get row by cell",
                resetCellValue: "Reset cell value",
                updateGridValue: "Remove grid dirty",
                toggleRow: "Expand or Folding",
                insertItems: "Add Items",
                updateItem: "Update Item",
                removeItems: "Remove Items",
                clearItems: "Clear Items",
                updateCellByRowCol2: "Update Cell",
                triggerFormulas: "Calculate grid formulas",
                setActiveRow: "Active Row",

                sortColumn: "Sort column",
                showColumn: "Show column",
                getActiveRow: "Get active row",
                autoRowHeight: "Auto row height",
                getCellbyRowCol: "Get cell",
                editCellbyRowCol: "To edit cell",
                focusCellbyRowCol: "Focus cell",
                getActiveCell: "Get active cell",
                setActiveCell: "Set active cell",
                getEditor: "Get editor",
                getEditCell: "Get cell in edit",
                offEditor: "Off editor",
                updateEditor: "Update editor",

                getRawData: "Get mapped rows",
                setRawData: "Set mapped rows",
                insertRows: "Add Rows",
                updateRow: "Update Row",
                setRowMap: "Update Cells",
                removeRows: "Remove Rows",
                removeAllRows: "Clear Rows",
                insertCol: "Add Column",
                updateHeader: "Update Header",
                removeCols: "Remove Columns",
                setHeader: "Clear All",
                editItem: "Edit Item",
                close: "Close Window",
                close2: "Close Dialog",
                showModal: "show modal",
                "open----_self": "Open in Currrent Window",
                "open----_blank": "Open to New Window",
                mailTo: "Send Email",
                selectFile: "Select File from Local Disk",
                readText: "Read Text form URL",
                readJSON: "Read JSON form URL",
                temp: "to Temp Object",
                "global": "to Global Object",
                "constant": "Constant",
                page: "to Page Object",
                "page.properties": "to Page Properties",
                "call": "Call Function",
                "set": "Set Function Global Reference",
                setCookies: "Set Cookies",
                setFI: "Set Fragment Identifier",
                message: "Open Float Message",
                pop: "Open Message Dialog",
                "alert": "Open Alert Dialog",
                "confirm": "Open Confirm Dialog",
                "prompt": "Open Prompt Dialog",
                "log": "Log to Console",
                "echo": "Echo to Debug Window",
                setProfiles: "Fill profile",
                setData: "Fill data",
                setValue: "Fill value",
                open: "Open in New Window",
                show2: "Switch to the page",
                initData: "initData",
                reloadParent: "reloadParent",
                postMessage: "Post Message to",
                broadcast: "Broadcast Message",
                getHooks: "Get hook",
                setHooks: "Set hook",
                notifyHooks: 'Notify hook',
                callFunction: 'Call function',
                fireEvent: 'Fire event',
                setTotalCount: "Set total count",
                getPage: "Get page",
                setPage: "Set page",
                getTotalPages: "get total page number",
                getJSONData: 'Get JSON Data',
                setJSONData: 'Set JSON Data',
                getXMLData: 'Get XML Data',
                setXMLData: 'Set XML Data',
                updateData: 'Update Data',
                setTheme: 'Set Theme',
                fillData: 'Fill Data',
                refreshChart: 'Refresh Chart',
                setSrc: 'Set media source',
                play: "Play",
                pause: "Pause",

                connect: "Connect",
                disconnect: "Disconnect",
                subscribe: "Subscribe",
                unsubscribe: "Unsubscribe",
                publish: "Publish"
            }
        },
        customAppend: function (parent, subId, left, top) {
            var ns = this,
                map1 = ns.CAPMAP.actiongroup,
                prop = ns.properties;

            ns.propFilter.setValue("", true);

            ns._hiddenDiv = ood.Dom.getEmptyDiv();
            //occupy it
            ns._hiddenDiv.html(" ");

            var fun = function (o) {
                o.id = ood.getClassName(o.id);
                if (!o.type) o.group = true;
                o._type = "page";
                delete o.tips;
                o.sub = true;
                // if (o.sub) {
                //     ood.filter(o.sub, function (o) {
                //         return o.id != SPA.currentPage;
                //     });
                //     ood.arr.each(o.sub, fun);
                // }
            };
            // for global functions setting dlg
            if (ood.get(prop.parent, ["_cls"])) {
                ood.filter(prop.pages, function (o) {
                    return o.id != SPA.currentPage;
                });
            }
            ood.arr.each(prop.pages, fun);


            var items = [{
                id: "none:none",
                caption: "$RAD.action." + map1["none:none"],
                imageClass: 'ri-checkbox-blank-line',
                cat: 'none',
                _type: "none"
            }, {
                id: 'other:msg',
                caption: "$(RAD.action." + map1["other:msg"] + ")",
                mageClass: 'ri-notification-line',
                _type: 'other'
            }, {
                id: 'other:var',
                caption: "$(RAD.action." + map1["other:var"] + ")",
                "imageClass": 'ri-text',
                _type: 'other'
            }, {
                id: 'other:callback',
                caption: "$(RAD.action." + map1["other:callback"] + ")",
                "imageClass": 'ri-function-line',
                _type: 'other'
            }, {
                id: 'other:url',
                caption: "$(RAD.action." + map1["other:url"] + ")",
                imageClass: 'ri-settings-line',
                _type: 'other'
            }];
            // for global functions setting dlg
            if (ood.get(prop.parent, ["_cls"])) {
                items.push({
                    id: SPA.currentPage ? ood.getClassName(SPA.currentPage) : (prop.className || "App"),
                    caption: "$(RAD.action." + map1['page'] + ")",
                    value: SPA.currentPage || "CurPage",
                    "imageClass": 'ri-file-list-line',
                    sub: prop.controls,
                    _type: "page"
                });
            }

            ns._tagVar = null;

            // if (prop.pages) {
            //     items.push({
            //         id: 'group:pages',
            //         caption: "$(RAD.action.Other Pages)",
            //         group: true,
            //         sub: prop.pages
            //     });
            // }

            items.push({
                id: 'group:pages',
                caption: "$(RAD.action.Other Pages)",
                group: true,
                sub: true
            });

            ns.tb_cat.setItems(items).setUIValue(null);

            ns.dialog.setCaption("$(RAD.action.Actions Editor)  " + (prop._cap ? (" : " + prop._cap) : ""));

            var panelitems = [{
                _iscon: 1,
                id: "none",
                caption: "$RAD.action.None",
                imageClass: 'ri-forbid-2-line'
            }], fun = function (alias, children) {
                if (children && children.length) {
                    ood.arr.each(children, function (o) {
                        var prf = o["0"];
                        if (prf.behavior && prf.behavior.PanelKeys && prf.behavior.PanelKeys.length) {
                            var a = ood.copy(alias);
                            var item = {};
                            a.push(prf.alias);
                            ood.arr.each(a, function (s) {
                                item["parent:" + s] = 1;
                            });

                            if (prf.properties.items) {
                                var ivs = [];
                                ood.arr.each(prf.properties.items, function (v) {
                                    var iv = ood.copy(item);
                                    iv.id = "{page." + prf.alias + "." + v.id + "}";
                                    iv.con = "{page." + prf.alias + "}";
                                    iv.consub = v.id;
                                    iv.caption = v.caption || v.id;
                                    iv._iscon = 1;
                                    ivs.push(iv);
                                });
                                var iv = ood.copy(item);
                                iv.id = "{page." + prf.alias + ".[key]}";
                                iv.con = "{page." + prf.alias + "}";
                                iv.consub = "[key]";
                                iv._iscon = 1;
                                iv.caption = "$(RAD.action.Other sub$-container)";
                                ivs.push(iv);

                                item.id = "{page." + prf.alias + "}";
                                item.caption = o.caption;
                                item.image = o.image;
                                item.imagePos = o.imagePos;
                                item.group = true;
                                item._iscon = 1;
                                item.sub = ivs;
                            } else {
                                item.id = item.con = "{page." + prf.alias + "}";
                                item.consub = null;
                                item.caption = o.caption;
                                item.image = o.image;
                                item.imagePos = o.imagePos;
                                item._iscon = 1;
                            }
                            panelitems.push(item);

                            if (o.sub) fun(a, o.sub);
                        }
                    });
                }
            };
            fun([], prop.controls);
            ns._panelitems = panelitems;
            ns._initctrls(true, true);

            ns.btn_ok.activate();

            ns.dialog.showModal(null, null, null, function () {
                var listitems = [];
                // set
                if (prop.actionConf) {
                    if (ood.isArr(prop.actionConf) || ood.isHash(prop.actionConf)) {
                        var arr = ood.isArr(prop.actionConf) ? prop.actionConf : prop.actionConf.actions,
                            rtn = ood.isHash(prop.actionConf) ? prop.actionConf['return'] : null;
                        if (arr) {
                            ood.arr.each(arr, function (o, i) {
                                if (ood.isHash(o)) {
                                    var item = {
                                        id: i + "",
                                        caption: o.desc || "",
                                        imageClass: 'ri-settings-line',
                                        tagVar: ood.clone(o, true)
                                    };
                                    // compatible
                                    if (item.tagVar && ('params' in item.tagVar)) {
                                        item.tagVar.args = item.tagVar.params;
                                        delete item.tagVar.params;
                                    }
                                    listitems.push(item);
                                }
                                else {
                                    // ignore string or function
                                }
                            });

                            ns.tb_actions.setItems(listitems);
                            if (listitems.length) {
                                ns._enablectrls();
                                ns.tb_actions.setUIValue(null);
                                // fire onchange,
                                // *** must aysc first time for load deep coms
                                ood.asyRun(function () {
                                    if (ns.tb_actions)
                                        ns.tb_actions.setUIValue(listitems[prop._selected || 0].id);
                                });
                            }

                            ns.btn_ok.activate();
                        }
                        if (rtn) {
                            ns.ood_returns.setValue(rtn, true);
                        }
                    } else {
                        // string or function
                    }
                }


                if (!listitems.length)
                    ns.addAction(ood.getRes("RAD.action.Action") + " 1");
                ns.markDirty(0);
            });

            ns._targetshadow = ns._targetshadow2 = null;
            return true;
        },
        markDirty: function (flag) {
            var ns = this;
            if (!flag || !ns._initvalue) {
                ns._dirty = flag;
            }
        },
        _initctrls: function (all, includeRtn) {
            var ns = this;
            if (all) ns.tb_actions.clearItems();
            ns.tb_actions.setUIValue(null).setDisabled(true);

            ns.ctl_grpp.setHtml("", true);

            // after tb_actions
            ns._initvalue = 1;
            if (includeRtn) ns.ood_returns.setValue("", true);
            ns.i_conl1.setValue("", true).setDisabled(true);
            ns.ctl_symbol1.setValue("defined", true).setDisabled(true);
            ns.i_conr1.setValue("", true).setDisabled(true);
            ns.i_conl2.setValue("", true).setDisabled(true);
            ns.ctl_symbol2.setValue("defined", true).setDisabled(true);
            ns.i_conr2.setValue("", true).setDisabled(true);
            ns.i_conl3.setValue("", true).setDisabled(true);
            ns.ctl_symbol3.setValue("defined", true).setDisabled(true);
            ns.i_conr3.setValue("", true).setDisabled(true);

            ns.add_pos.setValue("before", true).setDisabled(true);
            ns.add_target.setValue("", true).setDisabled(true);

            ns.log_v1.setValue("", true).setDisabled(false);
            ns.log_v2.setValue("", true).setDisabled(false);
            ns.log_v3.setValue("", true).setDisabled(false);
            ns.log_v4.setValue("", true).setDisabled(false);
            ns.log_v5.setValue("", true).setDisabled(false);

            ns.tb_mixsub.setValue("", true).setDisabled(true);
            ns.tb_consub.setValue("", true).setDisabled(true);

            ns.ctl_animate.setValue("blinkAlert", true).setDisabled(true);
            ns.ctl_animate.setTagVar(null).setInputReadonly(false, true).setCommandBtn("none", true);

            ns.ctl_varfrom.setValue("", true).setDisabled(true);
            ns.ctl_varfrom.setTagVar(null).setInputReadonly(false, true).setCommandBtn("none", true);
            ns.ctl_varfrompath.setValue("", true).setTagVar(null);

            ns.ctl_varto.setValue("", true).setDisabled(true);
            ns.ctl_adjust.setValue("", true).setDisabled(true);


            ns.cb_asy.setValue(false).setDisabled(true);
            ns.spin_asy.setValue(0).setDisabled(true);
            ns.cb_false.setValue(false).setDisabled(true);
            ns.tb_cat.setDisabled(true);
            ns.tb_action.setDisabled(true);

            ns.tb_cat.setValue("none:none", true);

            ns.cb_setid.setValue("").setDisabled(true);
            ns.cb_setvalue.setValue("").setDisabled(true);
            ns.cb_callid.setValue("").setDisabled(true);
            ns.grid_params.removeAllRows();
            ns.cb_callreturnto.setValue("none").setDisabled(true);
            ns.cb_callreturn.setValue("").setDisabled(true);
            ns.conf_return_exp.setHtml('');

            ns._initvalue = 0;
            // others
        },
        _enablectrls: function () {
            var ns = this;
            ns.tb_actions.setDisabled(false);
            ns.i_conl1.setDisabled(false);
            ns.ctl_symbol1.setDisabled(false);
            ns.i_conr1.setDisabled(true);
            ns.i_conl2.setDisabled(false);
            ns.ctl_symbol2.setDisabled(false);
            ns.i_conr2.setDisabled(true);
            ns.i_conl3.setDisabled(false);
            ns.ctl_symbol3.setDisabled(false);
            ns.i_conr3.setDisabled(true);
            ns.add_pos.setDisabled(false);

            ns.log_v1.setDisabled(false);
            ns.log_v2.setDisabled(false);
            ns.log_v3.setDisabled(false);
            ns.log_v4.setDisabled(false);
            ns.log_v5.setDisabled(false);

            ns.add_target.setDisabled(false);
            ns.ctl_animate.setDisabled(false);
            ns.ctl_varfrom.setDisabled(false);
            ns.ctl_varfrompath.setDisabled(false);
            ns.ctl_varto.setDisabled(false);
            ns.ctl_adjust.setDisabled(false);
            ns.tb_mixsub.setDisabled(false);
            ns.tb_consub.setDisabled(false);
            ns.cb_asy.setDisabled(false);
            ns.cb_false.setDisabled(false);
            ns.tb_cat.setDisabled(false);
            ns.tb_action.setDisabled(false);

            ns.cb_setid.setDisabled(false);
            ns.cb_setvalue.setDisabled(false);
            ns.cb_callid.setDisabled(false);
            ns.cb_callreturnto.setDisabled(false);
            ns.cb_callreturn.setDisabled(false);
        },
        iniComponents: function () {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "dialog")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("70em")
                    .setHeight("40em")
                    .setZIndex(1004)
                    .setCaption("$(RAD.action.Actions Editor)")
                    .setImageClass("ri-settings-line")
                    .setMinBtn(false)
                    .beforeClose("_beforeclose")
            );

            host.dialog.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block18")
                    .setDock("fill")
                    .setBorderType("none")
            );

            host.ctl_block18.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "ctl_layout11")
                    .setItems([
                        {
                            "id": "before",
                            "pos": "before",
                            "size": 130,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": false,
                            "cmd": false
                        },
                        {
                            "id": "main",
                            "size": 642,
                            "min": 10
                        },
                        {
                            "id": "after",
                            "pos": "after",
                            "size": 29,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": true,
                            "cmd": false
                        }
                    ])
                    .setType("horizontal")
                    .setCustomClass({
                        "PANEL": "ri-bg-bar"
                    })
            );

            host.ctl_layout11.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel16")
                    .setCaption("$(RAD.action.Actions List)")
                    .setBorderType("none")
                    .setNoFrame(true)
                , "before");

            host.ctl_panel16.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tb_actions")
                    .setInitFold(false)
                    .setDropKeys("_tb_action_configure")
                    .setDragKey("_tb_action_configure")
                    .onChange("_tb_actions_onchange")
                    .beforeEditApply("_tb_beforeapply")
                    .onDblclick("_tb_actions_ondblclick")
                    .afterDrop("_tb_action_drop")
            );

            host.ctl_panel16.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block66")
                    .setDock("top")
                    .setDockMargin({
                        "left": -3,
                        "top": 0,
                        "right": -3,
                        "bottom": 0
                    })
                    .setHeight("2.4166666666666665em")
                    .setBorderType("ridge")
                    .setOverflow("hidden")
            );

            host.ctl_block66.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host, "toolbar")
                    .setItems([
                        {
                            "id": "grp1",
                            "sub": [
                                {
                                    "id": "add",
                                    "caption": "",
                                    "imageClass": "ri-add-line",
                                    "tips": "$(RAD.action.Add Action)"
                                },
                                {
                                    "id": "remove",
                                    "caption": "",
                                    "imageClass": "ri-subtract-line",
                                    "tips": "$(RAD.action.Remove Action)"
                                },
                                {
                                    "id": "edit",
                                    "imageClass": "ri-edit-line",
                                    "caption": "",
                                    "tips": "$(RAD.action.Edit Item)"
                                }
                            ],
                            "caption": "grp1"
                        },
                        {
                            "id": "grp2",
                            "sub": [
                                {
                                    "id": "clear",
                                    "imageClass": "ri-delete-bin-line",
                                    "caption": "",
                                    "tips": "$(RAD.action.Clear All)"
                                }
                            ],
                            "caption": "grp2"
                        }
                    ])
                    .setHandler(false)
                    .setHAlign("center")
                    .onClick("_toolbar_onclick")
                    .setCustomStyle({
                        "ITEMS": "border-width:0 "
                    })
            );

            host.ctl_panel16.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block32")
                    .setDock("bottom")
                    .setLeft("5em")
                    .setTop("31.666666666666668em")
                    .setHeight("4.333333333333333em")
                    .setBorderType("none")
            );

            host.ood_ui_block32.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "ood_returns")
                    .setTag(1)
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "left": 6,
                        "top": 0,
                        "right": 6,
                        "bottom": 0
                    })
                    .setLeft("0.75em")
                    .setTop("0.25em")
                    .setWidth("6.583333333333333em")
                    .setHeight("3.8333333333333335em")
                    .setLabelSize("1.8em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.action.Return value)")
                    .setLabelHAlign("left")
                    .onChange("_i_rtn_onchange")
            );

            host.ctl_layout11.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ctl_group1")
                    .setDock("width")
                    .setDockMargin({
                        "left": 10,
                        "top": 0,
                        "right": 100,
                        "bottom": 0
                    })
                    .setTop("0.4166666666666667em")
                    .setHeight("8.25em")
                    .setOverflow("hidden")
                    .setCaption("$(RAD.action.Action Conditions)")
                    .setToggleBtn(false)
                , "main");

            host.ctl_group1.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "pan_condition")
                    .setDock("width")
                    .setLeft("0.8333333333333334em")
                    .setTop("0em")
                    .setWidth("40em")
                    .setHeight("2.1666666666666665em")
                    .setOverflow("hidden")
            );

            host.pan_condition.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel1")
                    .setLeft("0.75em")
                    .setTop("0.375em")
                    .setWidth("2.6666666666666665em")
                    .setCaption("$RAD.action.If")
            );

            host.pan_condition.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conl1")
                    .setTag(1)
                    .setDirtyMark(false)
                    .setLeft("4.25em")
                    .setTop("0.125em")
                    .setWidth("19.666666666666668em")
                    .setType("none")
                    .onChange("_i_conl_onchange")
            );

            host.pan_condition.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conr1")
                    .setTag(1)
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("31.416666666666668em")
                    .setTop("0.125em")
                    .setWidth("8em")
                    .setType("none")
                    .onChange("_i_conr_onchange")
            );

            host.pan_condition.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_symbol1")
                    .setTag(1)
                    .setDirtyMark(false)
                    .setLeft("24.25em")
                    .setTop("0.125em")
                    .setWidth("7em")
                    .setType("listbox")
                    .setDropListWidth(160)
                    .setItems([
                        {
                            "id": "defined",
                            "caption": "$(RAD.action.symbol.defined)"
                        },
                        {
                            "id": "undefined",
                            "caption": "$(RAD.action.symbol.undefined)"
                        },
                        {
                            "id": "empty",
                            "caption": "$(RAD.action.symbol.Is empty)"
                        },
                        {
                            "id": "non-empty",
                            "caption": "$(RAD.action.symbol.Is not empty)"
                        },
                        {
                            "id": "=",
                            "caption": "$(RAD.action.symbol.is)"
                        },
                        {
                            "id": "!=",
                            "caption": "$(RAD.action.symbol.is not)"
                        },
                        {
                            "id": ">",
                            "caption": "$(RAD.action.symbol.>)"
                        },
                        {
                            "id": "<",
                            "caption": "$(RAD.action.symbol.<)"
                        },
                        {
                            "id": ">=",
                            "caption": "$(RAD.action.symbol.>=)"
                        },
                        {
                            "id": "<=",
                            "caption": "$(RAD.action.symbol.<=)"
                        },
                        {
                            "id": "include",
                            "caption": "$(RAD.action.symbol.Contains)"
                        },
                        {
                            "id": "exclude",
                            "caption": "$(RAD.action.symbol.Does't Contain)"
                        },
                        {
                            "id": "start",
                            "caption": "$(RAD.action.symbol.Starts With)"
                        },
                        {
                            "id": "end",
                            "caption": "$(RAD.action.symbol.Ends With)"
                        },
                        {
                            "id": "objhaskey",
                            "caption": "$(RAD.action.symbol.Object contains key)"
                        },
                        {
                            "id": "objnokey",
                            "caption": "$(RAD.action.symbol.Object no key)"
                        },
                        {
                            "id": "arrhasvalue",
                            "caption": "$(RAD.action.symbol.Array contains value)"
                        },
                        {
                            "id": "arrnovalue",
                            "caption": "$(RAD.action.symbol.Array no value)"
                        },
                        {
                            "id": "objarrhaskey",
                            "caption": "$(RAD.action.symbol.Items contains id)"
                        },
                        {
                            "id": "objarrnokey",
                            "caption": "$(RAD.action.symbol.Items no id)"
                        }
                    ])
                    .setValue("defined")
                    .onChange("_ctl_symbol_onchange")
            );

            host.ctl_group1.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ctl_pane39")
                    .setDock("width")
                    .setLeft("0.8333333333333334em")
                    .setTop("2.1666666666666665em")
                    .setWidth("40em")
                    .setHeight("2.1666666666666665em")
                    .setOverflow("hidden")
            );

            host.ctl_pane39.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel41")
                    .setLeft("0.75em")
                    .setTop("0.375em")
                    .setWidth("2.6666666666666665em")
                    .setCaption("$(RAD.action.And)")
            );

            host.ctl_pane39.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conl2")
                    .setTag(2)
                    .setDirtyMark(false)
                    .setLeft("4.25em")
                    .setTop("0.125em")
                    .setWidth("19.666666666666668em")
                    .setType("none")
                    .onChange("_i_conl_onchange")
            );

            host.ctl_pane39.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conr2")
                    .setTag(2)
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("31.416666666666668em")
                    .setTop("0.125em")
                    .setWidth("8em")
                    .setType("none")
                    .onChange("_i_conr_onchange")
            );

            host.ctl_pane39.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_symbol2")
                    .setTag(2)
                    .setDirtyMark(false)
                    .setLeft("24.25em")
                    .setTop("0.125em")
                    .setWidth("7em")
                    .setType("listbox")
                    .setDropListWidth(160)
                    .setItems([
                        {
                            "id": "defined",
                            "caption": "$(RAD.action.symbol.defined)"
                        },
                        {
                            "id": "undefined",
                            "caption": "$(RAD.action.symbol.undefined)"
                        },
                        {
                            "id": "empty",
                            "caption": "$(RAD.action.symbol.Is empty)"
                        },
                        {
                            "id": "non-empty",
                            "caption": "$(RAD.action.symbol.Is not empty)"
                        },
                        {
                            "id": "=",
                            "caption": "$(RAD.action.symbol.is)"
                        },
                        {
                            "id": "!=",
                            "caption": "$(RAD.action.symbol.is not)"
                        },
                        {
                            "id": ">",
                            "caption": "$(RAD.action.symbol.>)"
                        },
                        {
                            "id": "<",
                            "caption": "$(RAD.action.symbol.<)"
                        },
                        {
                            "id": ">=",
                            "caption": "$(RAD.action.symbol.>=)"
                        },
                        {
                            "id": "<=",
                            "caption": "$(RAD.action.symbol.<=)"
                        },
                        {
                            "id": "include",
                            "caption": "$(RAD.action.symbol.Contains)"
                        },
                        {
                            "id": "exclude",
                            "caption": "$(RAD.action.symbol.Does't Contain)"
                        },
                        {
                            "id": "start",
                            "caption": "$(RAD.action.symbol.Starts With)"
                        },
                        {
                            "id": "end",
                            "caption": "$(RAD.action.symbol.Ends With)"
                        },
                        {
                            "id": "objhaskey",
                            "caption": "$(RAD.action.symbol.Object contains key)"
                        },
                        {
                            "id": "objnokey",
                            "caption": "$(RAD.action.symbol.Object no key)"
                        },
                        {
                            "id": "arrhasvalue",
                            "caption": "$(RAD.action.symbol.Array contains value)"
                        },
                        {
                            "id": "arrnovalue",
                            "caption": "$(RAD.action.symbol.Array no value)"
                        },
                        {
                            "id": "objarrhaskey",
                            "caption": "$(RAD.action.symbol.Items contains id)"
                        },
                        {
                            "id": "objarrnokey",
                            "caption": "$(RAD.action.symbol.Items no id)"
                        }
                    ])
                    .setValue("defined")
                    .onChange("_ctl_symbol_onchange")
            );

            host.ctl_group1.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ctl_pane41")
                    .setDock("width")
                    .setLeft("0.8333333333333334em")
                    .setTop("4.25em")
                    .setWidth("40em")
                    .setHeight("2.1666666666666665em")
                    .setOverflow("hidden")
            );

            host.ctl_pane41.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel42")
                    .setLeft("0.75em")
                    .setTop("0.375em")
                    .setWidth("2.6666666666666665em")
                    .setCaption("$(RAD.action.And)")
            );

            host.ctl_pane41.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conl3")
                    .setTag(3)
                    .setDirtyMark(false)
                    .setLeft("4.25em")
                    .setTop("0.125em")
                    .setWidth("19.666666666666668em")
                    .setType("none")
                    .onChange("_i_conl_onchange")
            );

            host.ctl_pane41.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "i_conr3")
                    .setTag(3)
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("31.416666666666668em")
                    .setTop("0.125em")
                    .setWidth("8em")
                    .setType("none")
                    .onChange("_i_conr_onchange")
            );

            host.ctl_pane41.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_symbol3")
                    .setTag(3)
                    .setDirtyMark(false)
                    .setLeft("24.25em")
                    .setTop("0.125em")
                    .setWidth("7em")
                    .setType("listbox")
                    .setDropListWidth(160)
                    .setItems([
                        {
                            "id": "defined",
                            "caption": "$(RAD.action.symbol.defined)"
                        },
                        {
                            "id": "undefined",
                            "caption": "$(RAD.action.symbol.undefined)"
                        },
                        {
                            "id": "empty",
                            "caption": "$(RAD.action.symbol.Is empty)"
                        },
                        {
                            "id": "non-empty",
                            "caption": "$(RAD.action.symbol.Is not empty)"
                        },
                        {
                            "id": "=",
                            "caption": "$(RAD.action.symbol.is)"
                        },
                        {
                            "id": "!=",
                            "caption": "$(RAD.action.symbol.is not)"
                        },
                        {
                            "id": ">",
                            "caption": "$(RAD.action.symbol.>)"
                        },
                        {
                            "id": "<",
                            "caption": "$(RAD.action.symbol.<)"
                        },
                        {
                            "id": ">=",
                            "caption": "$(RAD.action.symbol.>=)"
                        },
                        {
                            "id": "<=",
                            "caption": "$(RAD.action.symbol.<=)"
                        },
                        {
                            "id": "include",
                            "caption": "$(RAD.action.symbol.Contains)"
                        },
                        {
                            "id": "exclude",
                            "caption": "$(RAD.action.symbol.Does't Contain)"
                        },
                        {
                            "id": "start",
                            "caption": "$(RAD.action.symbol.Starts With)"
                        },
                        {
                            "id": "end",
                            "caption": "$(RAD.action.symbol.Ends With)"
                        },
                        {
                            "id": "objhaskey",
                            "caption": "$(RAD.action.symbol.Object contains key)"
                        },
                        {
                            "id": "objnokey",
                            "caption": "$(RAD.action.symbol.Object no key)"
                        },
                        {
                            "id": "arrhasvalue",
                            "caption": "$(RAD.action.symbol.Array contains value)"
                        },
                        {
                            "id": "arrnovalue",
                            "caption": "$(RAD.action.symbol.Array no value)"
                        },
                        {
                            "id": "objarrhaskey",
                            "caption": "$(RAD.action.symbol.Items contains id)"
                        },
                        {
                            "id": "objarrnokey",
                            "caption": "$(RAD.action.symbol.Items no id)"
                        }
                    ])
                    .setValue("defined")
                    .onChange("_ctl_symbol_onchange")
            );

            host.ctl_layout11.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block14")
                    .setDock("fill")
                    .setDockMargin({
                        "left": 0,
                        "top": 110,
                        "right": 0,
                        "bottom": 0
                    })
                    .setBorderType("none")
                , "main");

            host.ctl_block14.append(
                ood.create("ood.UI.Div")
                    .setHost(host, "ctl_pane17")
                    .setDock("fill")
            );

            host.ctl_pane17.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "ctl_layout8")
                    .setItems([
                        {
                            "id": "before",
                            "size": 200,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": false,
                            "cmd": false,
                            "pos": "before"
                        },
                        {
                            "id": "main",
                            "size": 492,
                            "min": 10
                        },
                        {
                            "id": "after",
                            "size": 80,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": true,
                            "cmd": false,
                            "pos": "after"
                        }
                    ])
                    .setType("horizontal")
                    .setCustomClass({
                        "PANEL": "ri-bg-bar"
                    })
            );

            host.ctl_layout8.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel7")
                    .setCaption("1.$(RAD.action.Select category/target)")
                    .setBorderType("none")
                    .setNoFrame(true)
                , "before");

            host.ctl_panel7.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tb_cat")
                    .setInitFold(false)
                    .onChange("_tb_cat_onchange")
                    .onGetContent("_treebarprj_onGetContent")
            );

            host.ctl_panel7.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "blk_target")
                    .setDock("top")
                    .setDockMargin({
                        "left": -3,
                        "top": 0,
                        "right": -3,
                        "bottom": 0
                    })
                    .setHeight("2.25em")
                    .setBorderType("ridge")
                    .setOverflow("hidden")
                    .setCustomStyle({
                        "PANEL": "white-space: nowrap;"
                    })
            );

            host.blk_target.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "lbl_target")
                    .setLeft("0.625em")
                    .setTop("0.3125em")
                    .setWidth("7.6875em")
                    .setCaption("")
                    .setHAlign("left")
                    .setCustomStyle({
                        "KEY": {
                            "overflow": "visible"
                        }
                    })
            );

            host.ctl_layout8.append(
                ood.create("ood.UI.Layout")
                    .setHost(host, "ctl_layout17")
                    .setItems([
                        {
                            "id": "before",
                            "size": 180,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": false,
                            "cmd": false,
                            "pos": "before"
                        },
                        {
                            "id": "main",
                            "size": 372,
                            "min": 10
                        },
                        {
                            "id": "after",
                            "size": 29,
                            "min": 10,
                            "locked": false,
                            "folded": false,
                            "hidden": true,
                            "cmd": false,
                            "pos": "after"
                        }
                    ])
                    .setType("horizontal")
                    .setCustomClass({
                        "PANEL": "ri-bg-bar"
                    })
                , "main");

            host.ctl_layout17.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel8")
                    .setCaption("2.$(RAD.action.Select action)")
                    .setBorderType("none")
                    .setNoFrame(true)
                , "before");

            host.ctl_panel8.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tb_action")
                    .setInitFold(false)
                    .onChange("_tb_action_onchange")
            );

            host.ctl_panel8.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_action")
                    .setDock("top")
                    .setDockMargin({
                        "left": -3,
                        "top": 0,
                        "right": -3,
                        "bottom": 0
                    })
                    .setHeight("2.25em")
                    .setBorderType("ridge")
                    .setOverflow("hidden")
                    .setCustomStyle({
                        "PANEL": "white-space: nowrap;"
                    })
            );

            host.ctl_action.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "lbl_action")
                    .setLeft("0.75em")
                    .setTop("0.3125em")
                    .setWidth("5.5625em")
                    .setCaption("")
                    .setHAlign("left")
                    .setCustomStyle({
                        "KEY": {
                            "overflow": "visible"
                        }
                    })
            );

            host.ctl_layout17.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel9")
                    .setPanelBgClr("#FFFFFF")
                    .setCaption("3.$(RAD.action.Configure action)")
                    .setBorderType("none")
                    .setNoFrame(true)
                , "main");

            host.ctl_panel9.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block103")
                    .setDock("top")
                    .setDockMargin({
                        "left": -3,
                        "top": 0,
                        "right": -3,
                        "bottom": 0
                    })
                    .setHeight("2.25em")
                    .setZIndex(5)
                    .setBorderType("ridge")
                    .setOverflow("hidden")
            );

            host.ctl_block103.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "cb_asy")
                    .setDirtyMark(false)
                    .setLeft("0.9375em")
                    .setTop("0.08333333333333333em")
                    .setWidth("7.333333333333333em")
                    .setHeight("1.5833333333333333em")
                    .setCaption("$RAD.action.Async")
                    .onChange("_cb_asy_onchange")
            );

            host.ctl_block103.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "spin_asy")
                    .setDirtyMark(false)
                    .setLeft("8.25em")
                    .setTop("0.08333333333333333em")
                    .setWidth("5.833333333333333em")
                    .setHeight("1.75em")
                    .setType("spin")
                    .setPrecision(0)
                    .setIncrement(100)
                    .setValue(0)
                    .onChange("_spin_asy_onchange")
            );

            host.ctl_block103.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel25")
                    .setLeft("14.25em")
                    .setTop("0.3333333333333333em")
                    .setHeight("1.0833333333333333em")
                    .setCaption("$RAD.action.ms")
            );

            host.ctl_block103.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "cb_false")
                    .setDirtyMark(false)
                    .setLeft("17.333333333333332em")
                    .setTop("0.08333333333333333em")
                    .setWidth("23.333333333333332em")
                    .setHeight("1.5833333333333333em")
                    .setCaption("$(RAD.action.Abort subsequent execution)")
                    .onChange("_false_change")
            );

            host.ctl_panel9.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block33")
                    .setDock("fill")
                    .setDockMargin({
                        "left": -3,
                        "top": -3,
                        "right": 0,
                        "bottom": 0
                    })
                    .setZIndex(4)
                    .setBorderType("inset")
            );

            host.ctl_block33.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host, "tab_conf")
                    .setItems([
                        {
                            "id": "none",
                            "caption": "none"
                        },
                        {
                            "id": "selfile",
                            "caption": "selfile"
                        },
                        {
                            "id": "page",
                            "caption": "page"
                        },
                        {
                            "id": "fmsg",
                            "caption": "fmsg"
                        },
                        {
                            "id": "datatransfer",
                            "caption": "datatransfer"
                        },
                        {
                            "id": "url",
                            "caption": "url"
                        },
                        {
                            "id": "prop",
                            "caption": "prop"
                        },
                        {
                            "id": "json",
                            "caption": "json"
                        },
                        {
                            "id": "con",
                            "caption": "con"
                        },
                        {
                            "id": "msg",
                            "caption": "msg"
                        },
                        {
                            "id": "data",
                            "caption": "data"
                        },
                        {
                            "id": "switch",
                            "caption": "switch"
                        },
                        {
                            "id": "var",
                            "caption": "var"
                        },
                        {
                            "id": "mix",
                            "caption": "mix"
                        },
                        {
                            "id": "setcb",
                            "caption": "setcb"
                        },
                        {
                            "id": "callcb",
                            "caption": "callcb"
                        },
                        {
                            "id": "log",
                            "caption": "log"
                        },
                        {
                            "id": "animate",
                            "caption": "animate"
                        }
                    ])
                    .setNoHandler(true)
                    .setValue("callcb")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "ctl_span1")
                    .setLeft("15.416666666666666em")
                    .setTop("11.083333333333334em")
                    .setWidth("1.625em")
                    .setHtml("px")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "ctl_span2")
                    .setLeft("15.416666666666666em")
                    .setTop("13.75em")
                    .setWidth("2.25em")
                    .setHtml("$RAD.action.ms")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_tocon")
                    .setCaption("$(RAD.action.Select Target Node)")
                , "con");

            host.ctl_tocon.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tb_con")
                    .setInitFold(false)
                    .onChange("_tb_con_onchange")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "msg_inputm")
                    .setLeft("0.9166666666666666em")
                    .setTop("11.583333333333334em")
                    .setWidth("24.25em")
                    .setHeight("1.3333333333333333em")
                    .setHtml("$(RAD.action.msg.Notice: The prompt input will be set to {temp$.okData})")
                , "msg");

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "msg_m")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 10,
                        "top": 0,
                        "right": 10,
                        "bottom": 2
                    })
                    .setHeight("auto")
                    .setHtml("<div style='color:red;'> $(RAD.action.msg.Asynchronous action causes the later actions to be blocked until it succeeds or fails) </div>")
                , "msg");

            host.tab_conf.append(
                ood.create("ood.UI.Panel")
                    .setHost(host, "ctl_panel29")
                    .setCaption("$(RAD.action.Select Target Node)")
                , "mix");

            host.ctl_panel29.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "add_target")
                    .setInitFold(false)
                    .onChange("_add_target_onchange")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ctl_group35")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 6,
                        "top": 4,
                        "right": 4,
                        "bottom": 8
                    })
                    .setHeight("5.333333333333333em")
                    .setOverflow("hidden")
                    .setCaption("$(RAD.action.Set return value to)")
                    .setToggleBtn(false)
                , "callcb");

            host.ctl_group35.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "conf_return_exp")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 10,
                        "top": 0,
                        "right": 12,
                        "bottom": 2
                    })
                    .setHeight("auto")
                    .setHtml("<font color=\"#ff0000\">&nbsp;</font>")
                    .setCustomStyle({
                        "KEY": {
                            "color": "#FF0000",
                            "text-align": "right"
                        }
                    })
            );

            host.ctl_group35.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "cb_callreturn")
                    .setTagVar("none")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "left": 140,
                        "top": 0,
                        "right": 4,
                        "bottom": 0
                    })
                    .setTop("0.1875em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Key)")
                    .onChange("_cb_callreturn_onchange")
            );

            host.ctl_group35.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "cb_callreturnto")
                    .setDirtyMark(false)
                    .setLeft("1em")
                    .setTop("0.25em")
                    .setType("listbox")
                    .setItems([
                        {
                            "id": "none",
                            "caption": "$(RAD.action.Doesn't set at all)"
                        },
                        {
                            "id": "temp",
                            "caption": "$(RAD.action.to Temp Object)",
                            "image": ""
                        },
                        {
                            "id": "global",
                            "caption": "$(RAD.action.to Global Object)",
                            "image": ""
                        },
                        {
                            "id": "page",
                            "caption": "$(RAD.action.to Page Object)",
                            "image": "",
                            "disabled": false
                        },
                        {
                            "id": "page.properties",
                            "caption": "$(RAD.action.to Page Properties)",
                            "image": ""
                        }
                    ])
                    .setValue("none")
                    .onChange("_cb_callreturnto_onchange")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ctl_grpp")
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("8.583333333333334em")
                    .setWidth("25.25em")
                    .setHeight("auto")
                    .setCaption("$RAD.action.Parameters")
                    .setToggleBtn(false)
                , "setcb");

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "msg_m2")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 0,
                        "top": 0,
                        "right": 10,
                        "bottom": 2
                    })
                    .setHeight("1.1666666666666667em")
                    .setHtml("<div style='color:red;text-align:right'> $(RAD.action.msg.Asynchronous action: the later actions will be blocked until it succeeds or fails. 'Stop' will not prevent next actions, but can stop the follow up process for 'beforeXXX' event.) </div>")
                , "datatransfer");

            host.tab_conf.append(
                ood.create("ood.UI.Group")
                    .setHost(host, "ood_ui_g21")
                    .setDock("fill")
                    .setDockMargin({
                        "left": 0,
                        "top": 42,
                        "right": 0,
                        "bottom": 0
                    })
                    .setCaption("$RAD.action.Parameters")
                    .setToggleBtn(false)
                , "url");

            host.ood_ui_g21.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "_jsoneditor2")
                    .setProperties({
                        "multiLineValue": false
                    })
                    .setEvents({
                        "onEdit": "_jsonedit",
                        "onchange": "_jsoneditorchanged2"
                    })
            );

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "ood_var_right")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 10,
                        "top": 0,
                        "right": 10,
                        "bottom": 2
                    })
                    .setHeight("auto")
                    .setHtml("DataAdjustment( ValueSource[ValuePath] )")
                    .setCustomStyle({
                        "KEY": {
                            "text-align": "right"
                        }
                    })
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.Span")
                    .setHost(host, "conf_var_left")
                    .setDock("bottom")
                    .setDockMargin({
                        "left": 10,
                        "top": 0,
                        "right": 10,
                        "bottom": 2
                    })
                    .setHeight("auto")
                    .setHtml("<font color=\"#ff0000\">&nbsp;</font>")
                    .setCustomStyle({
                        "KEY": {
                            "color": "#FF0000"
                        }
                    })
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "propFilter")
                    .setTagVar("none")
                    .setDirtyMark(false)
                    .setDock("top")
                    .setDockMargin({
                        "left": 4,
                        "top": 4,
                        "right": 4,
                        "bottom": 4
                    })
                    .setTips("$(RAD.designer.tool.Filter) ")
                    .setHeight("2em")
                    .setDynCheck(true)
                    .setType("input")
                    .setImageClass("ri-filter-line")
                    .setUnit("Show All")
                    .setUnits("Show All;Modified Only")
                    .setCommandBtn("delete")
                    .onChange("_propFilter_onchange")
                    .onCommand("_propFilter_oncmd")
                    .afterUnitUpdated("_propFilter_unit")
                , "prop");

            host.tab_conf.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "profileGrid")
                    .setDirtyMark(false)
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandler(false)
                    .setColSortable(false)
                    .setHeader([
                        {
                            "id": "key",
                            "caption": "$RAD.designer.gridcol1",
                            "width": "10em",
                            "type": "label"
                        },
                        {
                            "id": "value",
                            "caption": "$RAD.designer.gridcol2",
                            "width": "10em",
                            "type": "input",
                            "flexSize": 1
                        },
                        {
                            "id": "value2",
                            "caption": "$(RAD.designer.Variable)",
                            "width": "10em",
                            "type": "input",
                            "flexSize": 1
                        }
                    ])
                    .setRowOptions({
                        "rowRenderer": function (prf, row) {
                            if (row.group) prf.getSubNode('MARK', row._serialId).css('display', 'none');
                        }
                    })
                    .setTreeMode("infirstcell")
                    .setValue("")
                    .onShowTips("$tg_tips")
                    .onRowSelected("$profilegrid__rowsel")
                    .beforeRowActive(function () {
                        return false;
                    })
                    .beforeCellUpdated("$profilegrid_beforecellvalueset")
                    .afterCellUpdated("$tg_cellupd")
                    .onDblclickRow("$tg_dblclick")
                    .onBeginEdit("$tg_beginedit")
                    .setCustomStyle({
                        "HFMARK": "display:none"
                    })
                , "prop");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "url_address")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("0.9375em")
                    .setWidth("26.166666666666668em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.url.Address")
                    .onChange("_url_address_onchange")
                , "url");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "fileurl_address")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("0.9375em")
                    .setWidth("26.166666666666668em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.url.File URL)")
                    .onChange("_url_address_onchange")
                , "datatransfer");

            host.tab_conf.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel57")
                    .setLeft("0.5em")
                    .setTop("4em")
                    .setCaption("$(RAD.action.msg.Notice: The result will be set to {temp$.okData}, and the error will be set to {temp$.koData})")
                    .setHAlign("left")
                , "datatransfer");

            host.tab_conf.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host, "_jsoneditor")
                    .setProperties({
                        "multiLineValue": false
                    })
                    .setEvents({
                        "onEdit": "_jsonedit",
                        "onchange": "_jsoneditorchanged"
                    })
                , "json");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "msg_cap")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("0.9375em")
                    .setWidth("25.333333333333332em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.fmsg.Title")
                    .onChange("_msg_cap_onchange")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "msg_body")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("3.25em")
                    .setWidth("25.333333333333332em")
                    .setHeight("6.875em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.fmsg.Body")
                    .setMultiLines(true)
                    .onChange("_msg_body_onchange")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "msg_width")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("10.75em")
                    .setWidth("14.333333333333334em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.fmsg.Width")
                    .setType("spin")
                    .setPrecision(0)
                    .setIncrement(100)
                    .setMin(0)
                    .setMax(10000)
                    .onChange("_msg_width_onchange")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "msg_duration")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("13.333333333333334em")
                    .setWidth("14.416666666666666em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.fmsg.Duration")
                    .setType("spin")
                    .setPrecision(0)
                    .setIncrement(100)
                    .setMin(0)
                    .setMax(10000)
                    .onChange("_msg_duration_onchange")
                , "fmsg");

            host.tab_conf.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_blockcon")
                    .setDock("bottom")
                    .setHeight("2.75em")
                    .setOverflow("hidden")
                , "con");

            host.ctl_blockcon.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "tb_consub")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.625em")
                    .setTop("0.25em")
                    .setWidth("21.75em")
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Selected Node) :")
                    .onChange("_tb_consub_onchange")
            );

            host.ctl_blockcon.append(
                ood.create("ood.UI.RadioBox")
                    .setHost(host, "toggle_value")
                    .setDirtyMark(false)
                    .setItems([
                        {
                            "id": "expand",
                            "caption": "$RAD.action.Expand"
                        },
                        {
                            "id": "folding",
                            "caption": "$RAD.action.Folding"
                        }
                    ])
                    .setLeft("13.75em")
                    .setTop("0.08333333333333333em")
                    .setWidth("0em")
                    .setHeight("2.4166666666666665em")
                    .setValue("expand")
                    .onChange("_toggle_value_onchange")
            );

            host.tab_conf.append(
                ood.create("ood.UI.CheckBox")
                    .setHost(host, "ctl_cache")
                    .setDirtyMark(false)
                    .setLeft("1.875em")
                    .setTop("0.9375em")
                    .setCaption("$(RAD.action.Cache Page)")
                    .onChange("_ctl_cache_onchange")
                , "switch");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "msg_title")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("0.9375em")
                    .setWidth("24.5em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.msg.Title")
                    .onChange("_msg_title_onchange")
                , "msg");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "msg_message")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("3.25em")
                    .setWidth("24.5em")
                    .setHeight("3.6666666666666665em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$RAD.action.msg.Message")
                    .setMultiLines(true)
                    .onChange("_msg_message_onchange")
                , "msg");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "msg_input")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("7.416666666666667em")
                    .setWidth("24.5em")
                    .setHeight("3.6666666666666665em")
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.msg.Default Input)")
                    .setMultiLines(true)
                    .onChange("_msg_input_onchange")
                , "msg");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "data_source")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.5em")
                    .setTop("0.9375em")
                    .setWidth("25.333333333333332em")
                    .setLabelSize("8.333333333333334em")
                    .setLabelCaption("$(RAD.action.Data Source)")
                    .setType("helpinput")
                    .onChange("_ds_onchange")
                    .beforeComboPop("_data_source_beforecombopop")
                , "data");

            host.tab_conf.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "blk_itempos")
                    .setDock("bottom")
                    .setDockOrder(3)
                    .setHeight("9em")
                    .setOverflow("hidden")
                , "mix");

            host.blk_itempos.append(
                ood.create("ood.UI.RadioBox")
                    .setHost(host, "add_pos")
                    .setDirtyMark(false)
                    .setItems([
                        {
                            "id": "last",
                            "caption": "$(RAD.action.To the last)"
                        },
                        {
                            "id": "first",
                            "caption": "$(RAD.action.To the top)"
                        },
                        {
                            "id": "before",
                            "caption": "$(RAD.action.Before the selected node)"
                        },
                        {
                            "id": "after",
                            "caption": "$(RAD.action.After the selected node)"
                        },
                        {
                            "id": "curbefore",
                            "caption": "$(RAD.action.Before the current node)"
                        },
                        {
                            "id": "curafter",
                            "caption": "$(RAD.action.After the current node)"
                        },
                        {
                            "id": "sublast",
                            "caption": "$(RAD.action.To the last of selected node)"
                        },
                        {
                            "id": "subfirst",
                            "caption": "$(RAD.action.To the first of selected node)"
                        },
                        {
                            "id": "cursubfirst",
                            "caption": "$(RAD.action.To the first of current node)"
                        },
                        {
                            "id": "cursublast",
                            "caption": "$(RAD.action.To the last of current node)"
                        }
                    ])
                    .setDock("fill")
                    .setLeft("0.4375em")
                    .setTop("0.125em")
                    .setWidth("auto")
                    .setHeight("11.5em")
                    .setValue("last")
                    .onChange("_tb_mixsub_onchange")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block143")
                    .setDock("bottom")
                    .setHeight("2.5833333333333335em")
                    .setOverflow("hidden")
                , "mix");

            host.ctl_block143.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_itemvar")
                    .setTag("purevar")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.75em")
                    .setTop("0.4166666666666667em")
                    .setWidth("25em")
                    .setHeight("1.8333333333333333em")
                    .setZIndex(1002)
                    .setLabelSize("4em")
                    .setLabelCaption("$(RAD.action.Data)")
                    .setType("none")
                    .onChange("_ctl_itemvar_onchange")
                    .onCommand("_ctl_varfrom_cmd")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "ctl_varto")
                    .setTagVar("none")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.125em")
                    .setTop("1.25em")
                    .setWidth("25.5em")
                    .setZIndex(1002)
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Key)")
                    .setValueFormat("^[\\w]*$")
                    .onChange("_ctl_varto_onchange")
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_varfrom")
                    .setTag("purevar")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.125em")
                    .setTop("3.8333333333333335em")
                    .setWidth("25.5em")
                    .setHeight("1.8333333333333333em")
                    .setZIndex(1002)
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Value Source)")
                    .setType("helpinput")
                    .onChange("_ctl_varfrom_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                    .beforeComboPop("_data_source_beforecombopop")
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_animate")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.125em")
                    .setTop("1.25em")
                    .setWidth("25.5em")
                    .setHeight("1.8333333333333333em")
                    .setZIndex(1002)
                    .setLabelSize("8em")
                    .setLabelCaption("$(RAD.action.Animate Setting)")
                    .setType("listbox")
                    .beforeUIValueSet("_animate_buis")
                    .onChange("_ctl_animate_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                    .beforeComboPop("_animate_beforecombopop")
                , "animate");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_varfrompath")
                    .setTag("purevar")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.125em")
                    .setTop("6.333333333333333em")
                    .setWidth("25.5em")
                    .setHeight("1.8333333333333333em")
                    .setZIndex(1002)
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Value Path)")
                    .setType("input")
                    .onChange("_ctl_varfrompath_onchange")
                    .onCommand("_ctl_varfrompath_cmd")
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "ctl_adjust")
                    .setTagVar("adjustdata")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.125em")
                    .setTop("8.833333333333334em")
                    .setWidth("25.5em")
                    .setHeight("1.8333333333333333em")
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Data Adjustment)")
                    .setType("helpinput")
                    .setItems([
                        {
                            "id": "serialize",
                            "caption": "$RAD.action.Serialize"
                        },
                        {
                            "id": "stringify",
                            "caption": "$RAD.action.Stringify"
                        },
                        {
                            "id": "unserialize",
                            "caption": "$RAD.action.Unserialize"
                        },
                        {
                            "id": "urlDecode",
                            "caption": "$RAD.action.urlDecode"
                        },
                        {
                            "id": "urlEncode",
                            "caption": "$RAD.action.urlEncode"
                        }
                    ])
                    .onChange("_ctl_adjust_onchange")
                , "var");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "log_v1")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("1.0833333333333333em")
                    .setWidth("24.583333333333332em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Parameter) 1")
                    .setType("input")
                    .onChange("_log_v1_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                , "log");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "log_v2")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("3.6666666666666665em")
                    .setWidth("24.583333333333332em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Parameter) 2")
                    .setType("input")
                    .onChange("_log_v2_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                , "log");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "log_v3")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("6.166666666666667em")
                    .setWidth("24.583333333333332em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Parameter) 3")
                    .setType("input")
                    .onChange("_log_v3_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                , "log");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "log_v4")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("8.666666666666666em")
                    .setWidth("24.583333333333332em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Parameter) 4")
                    .setType("input")
                    .onChange("_log_v4_onchange")
                    .onCommand("_ctl_varfrom_cmd")
                , "log");

            host.tab_conf.append(
                ood.create("ood.UI.ComboInput")
                    .setHost(host, "log_v5")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("11.166666666666666em")
                    .setWidth("24.583333333333332em")
                    .setZIndex(1002)
                    .setLabelSize("6.666666666666667em")
                    .setLabelCaption("$(RAD.action.Parameter)  5")
                    .setType("input")
                    .onChange("_log_v5_onchange")
                , "log");

            host.tab_conf.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_group13")
                    .setDock("fill")
                    .setDockMargin({
                        "left": 6,
                        "top": 54,
                        "right": 4,
                        "bottom": 4
                    })
                    .setBorderType("flat")
                    .setOverflow("hidden")
                , "callcb");

            host.ctl_group13.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "grid_params")
                    .setDirtyMark(false)
                    .setRowHandler(false)
                    .setColSortable(false)
                    .setHeader([
                        {
                            "id": "key",
                            "caption": "$RAD.action.Parameter",
                            "width": "14em",
                            "type": "label"
                        },
                        {
                            "id": "value",
                            "caption": "$RAD.action.Value",
                            "width": "10em",
                            "type": "input",
                            "editable": true,
                            "flexSize": true,
                            "editMode": "inline"
                        }
                    ])
                    .setTreeMode('none')
                    .afterCellUpdated("_grid_params_aftercellupdated")
                    .onBeginEdit("_grid_params_onbe")
            );

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "cb_callid")
                    .setTagVar("callback")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "left": 6,
                        "top": 0,
                        "right": 4,
                        "bottom": 0
                    })
                    .setTop("0.375em")
                    .setHeight("3.6666666666666665em")
                    .setZIndex(1002)
                    .setLabelSize("1.8333333333333333em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.action.Function Expression, or Global Reference Key)")
                    .setLabelHAlign("left")
                    .onChange("_cb_callid_onchange")
                , "callcb");

            host.tab_conf.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel55")
                    .setLeft("1.75em")
                    .setTop("0.9375em")
                    .setCaption("$(RAD.action.No configuration items)")
                    .setHAlign("left")
                , "none");

            host.tab_conf.append(
                ood.create("ood.UI.Label")
                    .setHost(host, "ctl_slabel56")
                    .setLeft("1.75em")
                    .setTop("0.9375em")
                    .setCaption("$(RAD.action.msg.Notice: The local file object will be set to {temp$.okData})")
                    .setHAlign("left")
                , "selfile");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "cb_setid")
                    .setTagVar("none")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("0.625em")
                    .setWidth("25.416666666666668em")
                    .setHeight("3.75em")
                    .setZIndex(1002)
                    .setLabelSize("1.8333333333333333em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.action.Global Reference Key)")
                    .setLabelHAlign("left")
                    .onChange("_cb_setid_onchange")
                , "setcb");

            host.tab_conf.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "cb_setvalue")
                    .setTagVar("callback")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("1.25em")
                    .setTop("4.333333333333333em")
                    .setWidth("25.416666666666668em")
                    .setHeight("3.6666666666666665em")
                    .setZIndex(1002)
                    .setLabelSize("1.8333333333333333em")
                    .setLabelPos("top")
                    .setLabelCaption("$(RAD.action.Target Function Expression)")
                    .setLabelHAlign("left")
                    .onChange("_cb_setvalue_onchange")
                , "setcb");

            host.tab_conf.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_block_mix")
                    .setDock("bottom")
                    .setHeight("2.5833333333333335em")
                    .setOverflow("hidden")
                , "mix");

            host.ood_block_mix.append(
                ood.create("ood.UI.Input")
                    .setHost(host, "tb_mixsub")
                    .setDirtyMark(false)
                    .setDock("width")
                    .setDockMargin({
                        "right": 12,
                        "top": undefined,
                        "left": undefined,
                        "bottom": undefined
                    })
                    .setDockStretch("rearward")
                    .setLeft("0.75em")
                    .setTop("0.25em")
                    .setWidth("25em")
                    .setLabelSize("10em")
                    .setLabelCaption("$(RAD.action.Selected Node) :")
                    .onChange("_tb_mixsub_onchange")
            );

            host.ctl_layout11.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ctl_block37")
                    .setTop("0.6666666666666666em")
                    .setWidth("8.166666666666666em")
                    .setHeight("8.416666666666666em")
                    .setRight("0em")
                    .setBorderType("none")
                    .setBackground("transparent")
                    .setOverflow("hidden")
                , "main");

            host.ctl_block37.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "ctl_button23")
                    .setLeft("0.625em")
                    .setTop("5.166666666666667em")
                    .setWidth("6.833333333333333em")
                    .setHeight("2.5em")
                    .setImageClass("ri-close-line")
                    .setCaption("$RAD.cancel")
                    .onClick("_btncancel_onclick")
            );

            host.ctl_block37.append(
                ood.create("ood.UI.Button")
                    .setHost(host, "btn_ok")
                    .setDefaultFocus(true)
                    .setLeft("0.6875em")
                    .setTop("0.9166666666666666em")
                    .setWidth("6.833333333333333em")
                    .setHeight("2.5em")
                    .setImageClass("ri-check-line")
                    .setCaption("$RAD.ok")
                    .onClick("_btnonok_onclick")
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "blk_float")
                    .setLeft("0em")
                    .setTop("31.25em")
                    .setWidth("40em")
                    .setHeight("20em")
                    .setZIndex(1002)
                    .setVisibility("visible")
                    .setShadow(true)
                    .setResizer(true)
                    .setBorderType("flat")
            );

            host.blk_float.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tv_var")
                    .setAnimCollapse(false)
                    .setSingleOpen(true)
                    .onChange("_tv_var_onchange")
                    .onClick("_tv_var_onclick")
                    .onCmd("_tv_fun_opt")
                    .onGetContent("_tv_var_ongetcontent")
                    .onItemSelected("_tv_var_onsel")
            );
            host.blk_float.append(
                ood.create("ood.UI.Icon")
                    .setHost(host, "ood_ui_icon1")
                    .setTop("0em")
                    .setRight("0em")
                    .setImageClass("ri-close-line")
                    .setIconFontSize("2em")
                    .setCustomStyle({KEY: {margin: 0}})
                    .onClick("_ood_closefloat")
            );

            append(
                ood.create("ood.UI.Block")
                    .setHost(host, "blk_float2")
                    .setLeft("25.625em")
                    .setTop("31.25em")
                    .setWidth("40em")
                    .setHeight("20em")
                    .setVisibility("visible")
                    .setShadow(true)
                    .setResizer(true)
                    .setBorderType("flat")
            );


            host.blk_float2.append(
                ood.create("ood.UI.TreeView")
                    .setHost(host, "tv_fun")
                    .setAnimCollapse(false)
                    .setSingleOpen(true)
                    .onChange("_tv_var_onchange")
                    .onClick("_tv_var_onclick")
                    .onCmd("_tv_fun_opt")
                    .onGetContent("_tv_fun_ongetcontent")
                    .onItemSelected("_tv_var_onsel")
            );

            host.blk_float2.append(
                ood.create("ood.UI.Icon")
                    .setHost(host, "ood_ui_icon2")
                    .setTop("0em")
                    .setRight("0em")
                    .setImageClass("ri-close-line")
                    .setIconFontSize("2em")
                    .setCustomStyle({KEY: {margin: 0}})
                    .onClick("_ood_closefloat")
            );
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _treebarprj_onGetContent: function (profile, item, callback) {
            buildComponent = function (subitems) {
                ood.arr.each(subitems, function (item) {
                    if (item.sub) {
                        item.sub = buildComponent(item.sub);
                    }
                    if (item.key && CONF.mapWidgets[item.key]) {
                        var p = CONF.mapWidgets[item.key];
                        item.imageClass = CONF.mapWidgets[item.key].imageClass;
                    }
                });
                return subitems;
            };

            if (item.id == 'group:pages') {
                ood.Ajax(CONF.getAllClasses, {
                    projectName: item.projectName ? item.projectName : SPA.curProjectName,
                    className:SPA.getCurrentClassName(),
                    draggable: 'false'
                }, function (txt) {
                    var obj = txt;
                    if (!obj || obj.error) {
                        ood.message("No module in this project!");
                        callback(false);
                    } else {
                        var items = [];
                        ood.arr.each(obj.data, function (o, i) {
                            var subitem = [];
                            if (o.sub && ood.isArr(o.sub)) {
                                ood.arr.each(o.sub, function (o) {
                                    if (o.id != SPA.getCurrentClassName()) {
                                        subitem.push(o)
                                    }
                                });
                            }
                            items.push({
                                id: o.id,
                                cls: o.cls,
                                key: "jds.localModule",
                                path: o.location,
                                draggable: false,
                                sub: subitem,
                                //  type: 'otherModuleCall',
                                //initFold: true,
                                projectName: o.projectName,
                                imageClass: 'ri-grid-line',
                                name: o.caption,
                                iniProp: o.iniProp,
                                caption: o.className
                            });

                        });
                        callback(items);
                    }
                }, function (txt) {
                    callback(false);
                }, 0, {asy: false}).start();

            } else {

                ood.Ajax(CONF.getAllComponentsByClass, {
                    className: item.className,
                    draggable: 'false',
                    projectName: item.projectName ? item.projectName : SPA.curProjectName
                }, function (txt) {
                    var obj = txt;
                    if (obj && !obj.error) {
                        var root = {
                            sub: buildComponent(obj.data)
                        };
                        callback(root.sub);
                    } else ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                }, function (txt) {
                    callback(false);
                }, 0, {asy: true}).start();


            }
        },
        _buildsubtree: function (item) {

            if (!item["0"]) {
                if (item.key && item.key != 'ood.Module') {
                    var component = ood.create(item.key)
                        .setAlias(item.alias)
                        .setProperties(item.properties)
                    item["0"] = component["n0"];
                    item._type = "control";
                } else if (item.className && item.className != SPA.getCurrentClassName()) {
                    item._type = "otherModuleCall";
                } else if (item.id && item.id == SPA.getCurrentClassName()) {
                    item._type = "page";
                }
            }

            var items, options = {
                    imageClass: 'ri-settings-2-line',
                    className: item.className
                },
                map2 = this.CAPMAP.action;

            switch (item._type) {
                case "none":
                    items = [{
                        id: "none",
                        caption: "$RAD.action." + map2["none"],
                        imageClass: 'ri-forbid-2-line',
                        cat: "none"
                    }];
                    break;
                case 'control':
                    if (item["0"].key == "ood.MQTT") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "connect",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.connect()}",
                            caption: "$(RAD.action." + map2["connect"] + ")",
                            "0": item["0"]
                        }, {
                            id: "disconnect",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.disconnect()}",
                            caption: "$(RAD.action." + map2["disconnect"] + ")",
                            "0": item["0"]
                        }, {
                            id: "subscribe",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.subscribe()}",
                            caption: "$(RAD.action." + map2["subscribe"] + ")",
                            "0": item["0"]
                        }, {
                            id: "unsubscribe",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.unsubscribe()}",
                            caption: "$(RAD.action." + map2["unsubscribe"] + ")",
                            "0": item["0"]
                        }, {
                            id: "publish",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.publish()}",
                            caption: "$(RAD.action." + map2["publish"] + ")",
                            "0": item["0"]
                        }, {type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else if (item["0"].key == "ood.APICaller") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "setQueryData",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.setQueryData()}",
                            caption: "$(RAD.action." + map2["setQueryData"] + ")"
                        }, {
                            id: "invoke",
                            // cat: 'datatransfer',

                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.invoke()}",
                            caption: "$(RAD.action." + map2["invoke"] + ")"
                        }, {type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else if (item["0"].key == "ood.DataBinder") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "updateValue",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["updateValue"] + ")"
                        }, {
                            id: "resetValue",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["resetValue"] + ")"
                        }, {
                            id: "clearValue",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["clearValue"] + ")"
                        }, {
                            id: "updateDataFromUI",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["updateDataFromUI"] + ")"
                        }, {
                            id: "updateDataToUI",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["updateDataToUI"] + ")"
                        }, {
                            id: "getDirtied",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.getDirtied()}",
                            caption: "$(RAD.action." + map2["getDirtied"] + ")",
                            "0": item["0"]
                        },
                            {type: 'split'},
                            {
                                id: "checkValid",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["checkValid"] + ")",
                                "0": item[0]
                            },
                            {
                                id: "checkRequired",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["checkRequired"] + ")",
                                "0": item[0]
                            }, {
                                id: "isDirtied",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.isDirtied()}",
                                caption: "$(RAD.action." + map2["isDirtied"] + ")",
                                "0": item["0"]
                            }, {
                                id: "getData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getData()}",
                                caption: "$(RAD.action." + map2["getDate"] + ")",
                                "0": item["0"]
                            }, {
                                id: "setData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setData()}",
                                caption: "$(RAD.action." + map2["setData"] + ")",
                                "0": item["0"]
                            },
                            {type: 'split'}, {
                                id: "destroy",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["destroy"] + ")"
                            }];
                    } else if (item["0"].key == "ood.UI.CSSBox") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else if (item["0"].key == "ood.AnimBinder") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "apply",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.apply()}",
                            caption: "$(RAD.action." + map2["anim apply"] + ")",
                            "0": item["0"]
                        }, {type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else if (item["0"].box["ood.UI.Audio"]) {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        },
                            {type: 'split'},
                            {
                                id: "busy",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["busy"] + ")"
                            },
                            {
                                id: "free",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["free"] + ")"
                            },
                            {type: 'split'},
                            {
                                id: "setSrc",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setSrc()}",
                                caption: "$(RAD.action." + map2["setSrc"] + ")"
                            }, {
                                id: "play",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["play"] + ")"
                            }, {
                                id: "pause",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["pause"] + ")"
                            }, {type: 'split'}, {
                                id: "destroy",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["destroy"] + ")"
                            }];
                    } else if (item["0"].box["ood.UI.FileUpload"]) {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        },
                            {
                                id: "setUploadUrl",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setUploadUrl()}",
                                caption: "$RAD.action.esd.setUploadUrl"
                            },
                            {
                                id: "setQueryData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setQueryData()}",
                                caption: "$RAD.action.esd.setQueryData"
                            }, {type: 'split'}, {
                                id: "destroy",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["destroy"] + ")"
                            }];
                    } else if (item["0"].key == "ood.Timer") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "start",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["start"] + ")"
                        }, {
                            id: "suspend",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["suspend"] + ")"
                        }, {type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else if (item["0"].key == "ood.MessageService") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "broadcast",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.broadcast()}",
                            caption: "$(RAD.action." + map2["broadcast"] + ")",
                            "0": item["0"]
                        }, {type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    } else {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "animate",
                            caption: "$RAD.action." + map2["animate"],
                            cat: "animate"
                        }];
                        var prf = item["0"];
                        if (prf.boxing().activate) {
                            items.push({
                                id: "activate",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["activate"] + ")"
                            });
                        }
                        items.push({type: 'split'});

                        Array.prototype.push.apply(items, [{
                            id: "setProperties",
                            cat: 'prop',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "popUp",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.popUp()}",
                            caption: "$(RAD.action." + map2["popUp"] + ")",
                            "0": item["0"]
                        }, {
                            id: "disable",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["disable"] + ")"
                        }, {
                            id: "enable",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["enable"] + ")"
                        }, {
                            id: "show",
                            cat: 'con',
                            caption: "$(RAD.action." + map2["show"] + ")"
                        }, {
                            id: "hide",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["hide"] + ")"
                        }]);
                        if (prf.box["ood.UI.FusionChartsXT"]) {
                            items.push({type: 'split'});
                            items.push({
                                id: "getJSONData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getJSONData()}",
                                caption: "$(RAD.action." + map2["getJSONData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setJSONData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setJSONData()}",
                                caption: "$(RAD.action." + map2["setJSONData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getXMLData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getXMLData()}",
                                caption: "$(RAD.action." + map2["getXMLData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setXMLData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setXMLData()}",
                                caption: "$(RAD.action." + map2["setXMLData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateData()}",
                                caption: "$(RAD.action." + map2["updateData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "fillData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.fillData()}",
                                caption: "$(RAD.action." + map2["fillData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setTheme",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setTheme()}",
                                caption: "$(RAD.action." + map2["setTheme"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "refreshChart",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.refreshChart()}",
                                caption: "$(RAD.action." + map2["refreshChart"] + ")",
                                "0": item["0"]
                            });
                        }
                        if (prf.box['ood.absValue']) {
                            items.push({type: 'split'});
                            items.push({
                                id: "getUIValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getUIValue()}",
                                caption: "$(RAD.action." + map2["getUIValue"] + ")",
                                "0": item["0"]
                            });

                            items.push({
                                id: "setUIValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setUIValue()}",
                                caption: "$(RAD.action." + map2["setUIValue"] + ")",
                                "0": item["0"]
                            });
                        }
                        if (prf.boxing().setCaption && ('caption' in prf.properties)) {
                            items.push({
                                id: "setCaption",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setCaption()}",
                                caption: "$(RAD.action." + map2["setCaption"] + ")",
                                "0": item["0"]
                            });
                        }

                        if (prf.boxing().hideItems) {
                            items.push({
                                id: "hideItems",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["hideItems"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "showItems",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["showItems"] + ")",
                                "0": item["0"]
                            });
                        }
                        if (prf.behavior && prf.behavior.PanelKeys && prf.behavior.PanelKeys.length) {
                            items.push({type: 'split'});
                            items.push({
                                id: "busy",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["busy"] + ")"
                            });
                            items.push({
                                id: "free",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["free"] + ")"
                            });
                            items.push({type: 'split'});
                            items.push({
                                id: "setFormValues",
                                cat: 'mix',
                                args: [],
                                caption: "$(RAD.action." + map2["setFormValues"] + ")",
                                "0": item[0]
                            });
                            items.push({
                                id: "checkValid",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["checkValid"] + ")",
                                "0": item[0]
                            });
                            items.push({
                                id: "checkRequired",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["checkRequired"] + ")",
                                "0": item[0]
                            });
                            items.push({
                                id: "isDirtied",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.isDirtied()}",
                                caption: "$(RAD.action." + map2["isDirtied"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getFormValues",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getFormValues()}",
                                caption: "$(RAD.action." + map2["getFormValues"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "formSubmit",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["formSubmit"] + ")",
                                "0": item[0]
                            });

                            items.push({
                                id: "setChildren",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setChildren()}",
                                caption: ood.getRes('RAD.actions.dynamicLoad') || "动态装载组件",
                                "0": item[0]
                            });

                            items.push({
                                id: "formReset",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["formReset"] + ")",
                                "0": item[0]
                            });
                            items.push({
                                id: "updateFormValues",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["updateFormValues"] + ")",
                                "0": item[0]
                            });
                            items.push({
                                id: "formClear",
                                cat: 'con',
                                args: [],
                                caption: "$(RAD.action." + map2["formClear"] + ")",
                                "0": item[0]
                            });
                        }

                        if (prf.boxing().fireClickEvent) {
                            items.push({type: 'split'});
                            items.push({
                                id: "fireClickEvent",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["fireClickEvent"] + ")"
                            });
                        }
                        if (prf.boxing().fireItemClickEvent) {
                            items.push({type: 'split'});
                            items.push({
                                id: "fireItemClickEvent",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["fireItemClickEvent"] + ")",
                                "0": item["0"]
                            });
                        }
                        if (prf.boxing().dumpContainer) {
                            items.push({
                                id: "dumpContainer",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["dumpContainer"] + ")",
                                "0": item["0"]
                            });
                        }

                        if ('toggle' in prf.properties) {
                            items.push({type: 'split'});
                            items.push({
                                id: "setToggle",
                                cat: 'con',
                                _toggle: true,
                                caption: "$(RAD.action." + map2["setToggle"] + ")"
                            });
                        } else if (prf.box["ood.UI.Dialog"]) {
                            items.push({type: 'split'});
                            items.push({
                                id: "close",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["close2"] + ")"
                            });
                            items.push({
                                id: "showModal",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["showModal"] + ")"
                            });
                        } else if (prf.box["ood.UI.PageBar"]) {
                            items.push({type: 'split'});
                            items.push({
                                id: "getPage",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getPage()}",
                                caption: "$(RAD.action." + map2["getPage"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getTotalPages",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getTotalPages()}",
                                caption: "$(RAD.action." + map2["getTotalPages"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "buindAPI",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.buindAPI()}",
                                caption: "$(RAD.action.esd.buindAPI)",
                                "0": item["0"]
                            });

                            items.push({
                                id: "setPage",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setPage()}",
                                caption: "$(RAD.action." + map2["setPage"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setTotalCount",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setTotalCount()}",
                                caption: "$(RAD.action." + map2["setTotalCount"] + ")",
                                "0": item["0"]
                            });
                        } else if (prf.box["ood.UI.TreeBar"]) {
                            items.push({type: 'split'});
                            items.push({
                                id: "toggleNode",
                                cat: 'con',
                                _toggle: true,
                                hassub: 1,
                                caption: "$(RAD.action." + map2["toggleNode"] + ")",
                                "0": item["0"]
                            });
                        }
                        if ((prf.properties.items && !prf.box['ood.UI.ComboInput']) ||
                            ((prf.properties.type == "helpinput" || prf.properties.type == "combobox" || prf.properties.type == "listbox") && prf.box['ood.UI.ComboInput'])
                        ) {
                            items.push({type: 'split'});
                            if (!prf.box['ood.UI.ComboInput']) {
                                if (prf.boxing().setValue) {
                                    items.push({
                                        id: "setValue",
                                        cat: 'con',
                                        caption: "$(RAD.action." + map2["activeItem"] + ")",
                                        "0": item["0"]
                                    });
                                }
                            }
                            if (prf.key != "ood.UI.ToolBar" && prf.key != "ood.UI.Layout") {
                                items.push({
                                    id: "insertItems",
                                    cat: 'mix',
                                    caption: "$(RAD.action." + map2["insertItems"] + ")",
                                    "0": item["0"]
                                });
                            }
                            items.push({
                                id: "updateItem",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["updateItem"] + ")",
                                "0": item["0"]
                            });
                            if (prf.key != "ood.UI.ToolBar" && prf.key != "ood.UI.Layout") {
                                items.push({
                                    id: "removeItems",
                                    cat: 'con',
                                    caption: "$(RAD.action." + map2["removeItems"] + ")",
                                    "0": item["0"]
                                });
                            }
                            items.push({
                                id: "clearItems",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["clearItems"] + ")"
                            });
                        } else if (prf.box["ood.UI.TreeGrid"]) {
                            items.push({type: 'split'});
                            items.push({
                                id: "isDirtied",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["isDirtied"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateGridValue",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["updateGridValue"] + ")",
                                "0": item["0"]
                            });

                            items.push({
                                id: "toggleRow",
                                cat: 'con',
                                _toggle: true,
                                hassub: 1,
                                caption: "$(RAD.action." + map2["toggleRow"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "triggerFormulas",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["triggerFormulas"] + ")"
                            });
                            items.push({type: 'split'});
                            items.push({
                                id: "isCellDirtied",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.isCellDirtied()}",
                                caption: "$(RAD.action." + map2["isCellDirtied"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateCellValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateCellValue()}",
                                caption: "$(RAD.action." + map2["updateCellValue"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "focusCellbyRowCol",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.focusCellbyRowCol()}",
                                caption: "$(RAD.action." + map2["focusCellbyRowCol"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getCellbyRowCol",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getCellbyRowCol()}",
                                caption: "$(RAD.action." + map2["getCellbyRowCol"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getActiveCell",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getActiveCell()}",
                                caption: "$(RAD.action." + map2["getActiveCell"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setActiveCell",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setActiveCell()}",
                                caption: "$(RAD.action." + map2["setActiveCell"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateCellByRowCol2",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateCellByRowCol2()}",
                                caption: "$(RAD.action." + map2["updateCellByRowCol2"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateCellValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateCellValue()}",
                                caption: "$(RAD.action." + map2["updateCellValue"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "resetCellValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.resetCellValue()}",
                                caption: "$(RAD.action." + map2["resetCellValue"] + ")",
                                "0": item["0"]
                            });
                            items.push({type: 'split'});
                            items.push({
                                id: "isRowDirtied",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.isRowDirtied()}",
                                caption: "$(RAD.action." + map2["isRowDirtied"] + ")",
                                "0": item["0"]
                            });

                            items.push({
                                id: "updateRowValue",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateRowValue()}",
                                caption: "$(RAD.action." + map2["updateRowValue"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "editCellbyRowCol",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.editCellbyRowCol()}",
                                caption: "$(RAD.action." + map2["editCellbyRowCol"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getEditor",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["getEditor"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getEditCell",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["getEditCell"] + ")"
                            });
                            items.push({
                                id: "updateEditor",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateEditor()}",
                                caption: "$(RAD.action." + map2["updateEditor"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "offEditor",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["offEditor"] + ")",
                                "0": item["0"]
                            });

                            items.push({type: 'split'});
                            items.push({
                                id: "getRowbyCell",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getRowbyCell()}",
                                caption: "$(RAD.action." + map2["getRowbyCell"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getActiveRow",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getActiveRow()}",
                                caption: "$(RAD.action." + map2["getActiveRow"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "autoRowHeight",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["autoRowHeight"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setActiveRow",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["setActiveRow"] + ")",
                                "0": item["0"]
                            });

                            items.push({
                                id: "getRawData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getRawData()}",
                                caption: "$(RAD.action." + map2["getRawData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setRawData",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setRawData()}",
                                caption: "$(RAD.action." + map2["setRawData"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "insertRows",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["insertRows"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateRow",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["updateRow"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setRowMap",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.setRowMap()}",
                                caption: "$(RAD.action." + map2["setRowMap"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getRowMap",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getRowMap()}",
                                caption: "$(RAD.action." + map2["getRowMap"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "getHotRow",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getHotRow()}",
                                caption: "$(RAD.action." + map2["getHotRow"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "updateHotRow",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.updateHotRow()}",
                                caption: "$(RAD.action." + map2["updateHotRow"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "removeRows",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["removeRows"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "removeAllRows",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["removeAllRows"] + ")"
                            });
                            /*
                            items.push({
                                id:"insertCol",
                                cat:'mix',
                                caption:"$(RAD.action."+map2["insertCol"]+")",
                                "0":item["0"]
                            });
                            */
                            items.push({type: 'split'});
                            items.push({
                                id: "getHeaderByCell",
                                cat: 'callcb',
                                redirection: 'other:callback:call',
                                _tpl: "{page.*.getHeaderByCell()}",
                                caption: "$(RAD.action." + map2["getHeaderByCell"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "sortColumn",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["sortColumn"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "showColumn",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["showColumn"] + ")",
                                "0": item["0"]
                            });

                            items.push({
                                id: "updateHeader",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["updateHeader"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "removeCols",
                                cat: 'mix',
                                caption: "$(RAD.action." + map2["removeCols"] + ")",
                                "0": item["0"]
                            });
                            items.push({
                                id: "setHeader",
                                cat: 'none',
                                caption: "$(RAD.action." + map2["setHeader"] + ")"
                            });
                        }
                        if (prf.boxing().editItem) {
                            items.push({
                                id: "editItem",
                                cat: 'con',
                                caption: "$(RAD.action." + map2["editItem"] + ")",
                                "0": item["0"]
                            });
                        }
                        items.push({type: 'split'}, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        });
                    }
                    break;
                case 'other':
                    if (item.id == "other:url") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {

                            id: "showModule2",
                            caption: "$RAD.action.esd.showModule2",
                            redirection: 'other:callback:call',
                            _tpl: "{ood.showModule2()}",
                            cat: 'callcb',

                        }, {
                            id: "close",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["close"] + ")"
                        }, {
                            id: "open----_self",
                            cat: 'url',
                            caption: "$(RAD.action." + map2["open----_self"] + ")"
                        }, {
                            id: "open----_blank",
                            cat: 'url',
                            caption: "$(RAD.action." + map2["open----_blank"] + ")"
                        }, {
                            id: "mailTo",
                            cat: 'url',
                            caption: "$(RAD.action." + map2["mailTo"] + ")"
                        },
                            {type: 'split'},
                            {
                                id: "selectFile",
                                cat: 'selfile',
                                caption: "$(RAD.action." + map2["selectFile"] + ")"
                            }, {
                                id: "readText",
                                cat: 'datatransfer',
                                caption: "$(RAD.action." + map2["readText"] + ")"
                            }, {
                                id: "readJSON",
                                cat: 'datatransfer',
                                caption: "$(RAD.action." + map2["readJSON"] + ")"
                            }];
                    } else if (item.id == "other:var") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "temp",
                            cat: 'var',
                            args: [],
                            caption: "$(RAD.action." + map2["temp"] + ")"
                        }, {
                            id: "global",
                            cat: 'var',
                            args: [],
                            caption: "$(RAD.action." + map2["global"] + ")"
                        }, {
                            id: "page",
                            cat: 'var',
                            args: [],
                            caption: "$(RAD.action." + map2["page"] + ")"
                        }, {
                            id: "page.properties",
                            cat: 'var',
                            args: [],
                            caption: "$(RAD.action." + map2["page.properties"] + ")"
                        }];
                    } else if (item.id == "other:callback") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        }, {
                            id: "call",
                            cat: 'callcb',
                            args: [],
                            caption: "$(RAD.action." + map2["call"] + ")"
                        }, {
                            id: "set",
                            cat: 'setcb',
                            args: [],
                            caption: "$(RAD.action." + map2["set"] + ")"
                        }, {id: "xxx", type: 'split'}, {
                            id: "setCookies",
                            cat: 'json',
                            caption: "$(RAD.action." + map2["setCookies"] + ")"
                        }, {
                            id: "setFI",
                            cat: 'json',
                            caption: "$(RAD.action." + map2["setFI"] + ")"
                        }
                        ];
                    } else if (item.id == "other:msg") {
                        items = [{
                            id: "none",
                            caption: "$RAD.action." + map2["none"],
                            imageClass: 'ri-forbid-2-line',
                            cat: "none"
                        },
                            {
                                id: "log",
                                cat: 'log',
                                caption: "$(RAD.action." + map2["log"] + ")"
                            }, {
                                id: "echo",
                                cat: 'log',
                                caption: "$(RAD.action." + map2["echo"] + ")"
                            },
                            {type: 'split'},
                            {
                                id: "message",
                                cat: 'fmsg',
                                args: [],
                                caption: "$(RAD.action." + map2["message"] + ")"
                            }, {
                                id: "pop",
                                cat: 'msg',
                                args: [],
                                caption: "$(RAD.action." + map2["pop"] + ")"
                            }, {
                                id: "alert",
                                cat: 'msg',
                                args: [],
                                caption: "$(RAD.action." + map2["alert"] + ")"
                            }, {
                                id: "confirm",
                                cat: 'msg',
                                args: [],
                                caption: "$(RAD.action." + map2["confirm"] + ")"
                            }, {
                                id: "prompt",
                                cat: 'msg',
                                args: [],
                                caption: "$(RAD.action." + map2["prompt"] + ")"
                            },
                            {type: 'split'},
                            {
                                id: "busy",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["busy"] + ")"
                            }, {
                                id: "free",
                                cat: 'none',
                                args: [],
                                caption: "$(RAD.action." + map2["free"] + ")"
                            }];
                    }
                    break;
                case 'module':
                    items = [{
                        id: "popUp",
                        cat: 'callcb',
                        redirection: 'other:callback:call',
                        _tpl: "{page.*.popUp()}",
                        caption: "$(RAD.action." + map2["popUp"] + ")"
                    }, {
                        id: "show",
                        cat: 'con',
                        caption: "$(RAD.action." + map2["show"] + ")"
                    }, {
                        id: "hide",
                        cat: 'none',
                        caption: "$(RAD.action." + map2["hide"] + ")"
                    },
                        {type: 'split'},
                        {
                            id: "postMessage",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.postMessage()}",
                            caption: "$(RAD.action." + map2["postMessage"] + ")"
                        },
                        {
                            id: "setHooks",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.setHooks()}",
                            caption: "$(RAD.action." + map2["setHooks"] + ")"
                        },
                        {
                            id: "notifyHooks",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.notifyHooks()}",
                            caption: "$(RAD.action." + map2["notifyHooks"] + ")"
                        },
                        {
                            id: "callFunction",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.callFunction()}",
                            caption: "$(RAD.action." + map2["callFunction"] + ")"
                        },
                        {
                            id: "fireEvent",
                            cat: 'callcb',
                            redirection: 'other:callback:call',
                            _tpl: "{page.*.fireEvent()}",
                            caption: "$(RAD.action." + map2["fireEvent"] + ")"
                        },
                        {type: 'split'},
                        {
                            id: "setProperties",
                            cat: 'json',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "setProfiles",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setProfiles"] + ")"
                        },
                        {
                            id: "initData",
                            cat: 'none',
                            caption: "$(RAD.action.esd.initData)"
                        },
                        {
                            id: "reloadParent",
                            cat: 'none',
                            caption: "$(RAD.action.esd.reloadParent)"
                        },

                        {
                            id: "setData",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setData"] + ")"
                        }, {
                            id: "setValue",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setValue"] + ")"
                        }, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        }];
                    items.unshift({
                        id: "none",
                        caption: "$RAD.action." + map2["none"],
                        imageClass: 'ri-forbid-2-line',
                        cat: "none"
                    });
                    break;
                case "otherModuleCall":
                    items = [{
                        id: "open",
                        cat: 'none',
                        caption: "$(RAD.action." + map2["open"] + ")"
                    },
                        {
                            id: "show2",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.show2()}",
                            caption: "$(RAD.action." + map2["show2"] + ")"
                        },
                        {
                            id: "initData",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.initData()}",
                            caption: "$(RAD.action.esd.initData)"
                        },

                        {
                            id: "popUp",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.popUp()}",
                            caption: "$(RAD.action." + map2["popUp"] + ")"
                        }, {
                            id: "show",
                            cat: 'con',
                            caption: "$(RAD.action." + map2["show"] + ")"
                        }, {
                            id: "hide",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["hide"] + ")"
                        }, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        },
                        {type: 'split'},
                        {
                            id: "postMessage",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.postMessage()}",
                            caption: "$(RAD.action." + map2["postMessage"] + ")"
                        },
                        {
                            id: "setHooks",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.setHooks()}",
                            caption: "$(RAD.action." + map2["setHooks"] + ")"
                        },
                        {
                            id: "notifyHooks",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.notifyHooks()}",
                            caption: "$(RAD.action." + map2["notifyHooks"] + ")"
                        },
                        {
                            id: "callFunction",
                            cat: 'callcb',
                            // redirection: 'other:callback:call',
                            caption: "$(RAD.action." + map2["callFunction"] + ")"
                        },
                        {
                            id: "fireEvent",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.fireEvent()}",
                            caption: "$(RAD.action." + map2["fireEvent"] + ")"
                        },
                        {type: 'split'},
                        {
                            id: "setProperties",
                            cat: 'json',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "setProfiles",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setProfiles"] + ")"
                        }, {
                            id: "setData",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setData"] + ")"
                        }, {
                            id: "setValue",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setValue"] + ")"
                        }];
                    if (SPA.currentPage == item.id)
                        items.shift();
                    items.unshift({
                        id: "none",
                        caption: "$RAD.action." + map2["none"],
                        imageClass: 'ri-forbid-2-line',
                        cat: "none"
                    });
                    break;
                case "page":
                    items = [{
                        id: "open",
                        cat: 'none',
                        caption: "$(RAD.action." + map2["open"] + ")"
                    },
                        {
                            id: "checkValid",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.checkValid()}",
                            caption: ood.getRes('RAD.actions.validateData') || "验证数据"
                        },
                        {
                            id: "show2",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.show2()}",
                            caption: "$(RAD.action." + map2["show2"] + ")"
                        },
                        {
                            id: "initData",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.initData()}",
                            caption: "$(RAD.action.esd.initData)"
                        },
                        {
                            id: "reloadParent",
                            cat: 'callcb',
                            redirection: 'page',
                            _tpl: "{page.reloadParent()}",
                            caption: "$(RAD.action.esd.reloadParent)"
                        },


                        {
                            id: "popUp",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.popUp()}",
                            caption: "$(RAD.action." + map2["popUp"] + ")"
                        }, {
                            id: "show",
                            cat: 'con',
                            caption: "$(RAD.action." + map2["show"] + ")"
                        }, {
                            id: "hide",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["hide"] + ")"
                        }, {
                            id: "destroy",
                            cat: 'none',
                            caption: "$(RAD.action." + map2["destroy"] + ")"
                        },
                        {type: 'split'},
                        {
                            id: "postMessage",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.postMessage()}",
                            caption: "$(RAD.action." + map2["postMessage"] + ")"
                        },
                        {
                            id: "setHooks",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.setHooks()}",
                            caption: "$(RAD.action." + map2["setHooks"] + ")"
                        },
                        {
                            id: "notifyHooks",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.notifyHooks()}",
                            caption: "$(RAD.action." + map2["notifyHooks"] + ")"
                        },
                        {
                            id: "callFunction",
                            cat: 'callcb',
                            // redirection: 'other:callback:call',
                            caption: "$(RAD.action." + map2["callFunction"] + ")"
                        },
                        {
                            id: "fireEvent",
                            cat: 'callcb',
                            redirection: 'page::',
                            _tpl: "{page.fireEvent()}",
                            caption: "$(RAD.action." + map2["fireEvent"] + ")"
                        },
                        {type: 'split'},
                        {
                            id: "setProperties",
                            cat: 'json',
                            caption: "$(RAD.action." + map2["setProperties"] + ")"
                        }, {
                            id: "setProfiles",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setProfiles"] + ")"
                        }, {
                            id: "setData",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setData"] + ")"
                        }, {
                            id: "setValue",
                            cat: 'data',
                            caption: "$(RAD.action." + map2["setValue"] + ")"
                        }];
                    if (SPA.currentPage == item.id)
                        items.shift();
                    items.unshift({
                        id: "none",
                        caption: "$RAD.action." + map2["none"],
                        imageClass: 'ri-forbid-2-line',
                        cat: "none"
                    });
                    break;

                default:

            }


            var t = ood.get(CONF, ["CustomActions", item._type, item.id.split(":")[1], "items"]);
            if (t) ood.arr.insertAny(items, t);

            ood.arr.each(items, function (o) {
                ood.merge(o, options);
            });
            this.tb_action.setItems(items);
            return items;
        },
        _beforeclose: function () {
            var ns = this;
            ns.blk_float.hide();
            var prf = ns.blk_float.getTag();
            if (prf) prf.getSubNode('INPUT').blur();

            if (ns._hiddenDiv) {
                ns._hiddenDiv.html('');
                ns._hiddenDiv = null;
            }
            if (ns._dirty) {
                ood.confirm(ood.getRes('RAD.notsave'), ood.getRes('RAD.notsave2'), function () {
                    ns.dialog.hide();
                });
            } else
                ns.dialog.hide();
            return false;
        },
        _tb_action_drop: function (profile, e, src, key, data, item) {
            if (data._new)
                profile.boxing().setUIValue(data._new.id, true);
            this.markDirty(1);
        },
        _tb_actions_onchange: function (profile, oldValue, newValue) {
            if (!newValue) return;
            var ns = this, t, o,
                subvalue = "none:none",
                item = profile.getItemByItemId(newValue),
                tagVar = item.tagVar;
            ns._enablectrls();

            ns._initvalue = 1;

            // set target event object
            ns._tagVar = tagVar;

            if ('timeout' in tagVar) {
                // 1
                ns.spin_asy.setUIValue(parseInt(tagVar.timeout, 10) || 0);
                // 2
                ns.cb_asy.setUIValue(true);
            } else {
                ns.cb_asy.setUIValue(false);
            }
            if (('return' in tagVar) && tagVar['return'] === false) {
                ns.cb_false.setUIValue(true);
            } else {
                ns.cb_false.setUIValue(false);
            }

            if ((t = tagVar["target"])) {
                if (tagVar.type == "none") {
                    subvalue = "none:" + t;
                } else if (tagVar.type == "page") {
                    subvalue = t;
                } else if (tagVar.type == "other") {
                    subvalue = "other:" + t;
                } else {
                    subvalue = t;
                }
            }

            ns.i_conl1.setValue("", true);
            ns.i_conr1.setValue("", true);
            ns.ctl_symbol1.setValue("defined", true);
            ns.i_conl2.setValue("", true);
            ns.i_conr2.setValue("", true);
            ns.ctl_symbol2.setValue("defined", true);
            ns.i_conl3.setValue("", true);
            ns.i_conr3.setValue("", true);
            ns.ctl_symbol3.setValue("defined", true);

            if ((t = tagVar["conditions"])) {
                if ((o = t[0])) {
                    if (o.left) ns.i_conl1.setValue(o.left, true);
                    if (o.symbol) ns.ctl_symbol1.setUIValue(o.symbol, true);
                    if (o.right) ns.i_conr1.setValue(o.right, true);
                }
                if ((o = t[1])) {
                    if (o.left) ns.i_conl2.setValue(o.left, true);
                    if (o.symbol) ns.ctl_symbol2.setUIValue(o.symbol, true);
                    if (o.right) ns.i_conr2.setValue(o.right, true);
                }
                if ((o = t[2])) {
                    if (o.left) ns.i_conl3.setValue(o.left, true);
                    if (o.symbol) ns.ctl_symbol3.setUIValue(o.symbol, true);
                    if (o.right) ns.i_conr3.setValue(o.right, true);
                }
            }

            // if (tagVar.className && tagVar.className != SPA.currentClassName){
            //     item.className=tagVar.className;
            // }
            item.className = tagVar.className;

            if (tagVar.type == "page") {
                ns.tb_cat.setUIValue(subvalue, true);
            } else if (tagVar.className && tagVar.className != SPA.getCurrentClassName()) {
                if (ns.tb_cat.getItemByItemId(subvalue)) {
                    ns.tb_cat.setUIValue(subvalue);
                } else {
                    ood.getModule(tagVar.className);
                    ns.tb_cat.toggleNode(tagVar.className, true, true, true, function () {
                        this.setUIValue(subvalue);
                    });
                }

            } else {
                ns.tb_cat.setUIValue(ns.tb_cat.getItemByItemId(subvalue) ? subvalue : "none:none", true);
            }
            //


            ns._initvalue = 0;
        },
        _tb_con_onchange: function (profile, oldValue, newValue) {
            var ns = this, tagVar = ns._tagVar,
                arr = newValue.split(profile.properties.valueSeparator),
                id = arr[0],
                item = profile.getItemByItemId(id);
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                // for selecting container
                if (item) {
                    if (item._iscon) {
                        if (!newValue) return;
                        if (item && newValue != "none") {
                            if (item.con) tagVar.args[0] = item.con;
                            if (item.consub) {
                                if (item.consub != "[key]") {
                                    ns.tb_consub.setReadonly(true);
                                    ns.tb_consub.setUIValue(item.consub, true);
                                } else {
                                    ns.tb_consub.setReadonly(false);
                                    ns.tb_consub.setUIValue("", true);
                                    //ood.asyRun(function(){ns.tb_consub.activate()});
                                }
                            } else {
                                ns.tb_consub.setUIValue("");
                                ns.tb_consub.setReadonly(true);
                            }
                        }
                    } else {
                        var other;
                        for (var i = 0, l = arr.length; i < l; i++) {
                            if (arr[i] == "[key]" || !profile.getItemByItemId(arr[i])) {
                                other = true;
                                break;
                            }
                        }

                        if (!other) {
                            ns.tb_consub.setReadonly(true);
                            ns.tb_consub.setUIValue(newValue == '[key]' ? '' : newValue, true);
                        } else {
                            ns.tb_consub.setReadonly(false);
                            ns.tb_consub.setUIValue(newValue == '[key]' ? '' : newValue, true);
                            // ood.asyRun(function(){ns.tb_consub.activate()});
                        }
                    }
                } else {
                    ns.tb_consub.setUIValue("").setReadonly(true);
                    tagVar.args = [];
                }
                ns.markDirty(1);
            }
        },
        _add_target_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this;

            ns.tb_mixsub.setReadonly(newValue !== "[key]", true);
            ns.tb_mixsub.setUIValue(newValue == "[key]" ? "" : newValue, true);
        },
        _tb_mixsub_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this,
                tagVar = ns._tagVar,
                pos = ns.add_pos.getUIValue(),
                sub = ns.tb_mixsub.getUIValue(),
                mem = ns.add_target.getTagVar(),
                hasSub = mem.hasSub,
                args;
            if (tagVar) {
                args = tagVar.args = tagVar.args || [];
                if (mem.insert) {
                    //treebar/treeview
                    if (hasSub) {
                        switch (pos) {
                            case "before":
                                args[1] = "";
                                args[2] = sub;
                                args[3] = true;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "after":
                                args[1] = "";
                                args[2] = sub;
                                args[3] = false;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "first":
                                args[1] = "";
                                args[2] = "";
                                args[3] = true;
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "last":
                                args[1] = "";
                                args[2] = "";
                                args[3] = false;
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "curbefore":
                                args[1] = "";
                                args[2] = true;
                                args[3] = true;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "curafter":
                                args[1] = "";
                                args[2] = true;
                                args[3] = false;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "subfirst":
                                args[1] = sub;
                                args[2] = "";
                                args[3] = true;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "sublast":
                                args[1] = sub;
                                args[2] = "";
                                args[3] = false;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "cursubfirst":
                                args[1] = true;
                                args[2] = "";
                                args[3] = true;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "cursublast":
                                args[1] = true;
                                args[2] = "";
                                args[3] = false;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                        }
                    } else {
                        switch (pos) {
                            case "before":
                                args[1] = sub;
                                args[2] = true;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "after":
                                args[1] = sub;
                                args[2] = false;
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                break;
                            case "first":
                                args[1] = "";
                                args[2] = true;
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                break;
                            case "last":
                                args[1] = "";
                                args[2] = false;
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                            case "curbefore":
                                args[1] = true;
                                args[2] = true;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                break;
                            case "curafter":
                                args[1] = true;
                                args[2] = false;
                                ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                ns.tb_mixsub.setValue("", true);
                                ns.add_target.setValue("", true);
                                break;
                                break;
                        }
                    }
                } else {
                    args[0] = sub;
                }
                ns.markDirty(1);
            }
        },
        _tb_consub_onchange: function (profile, oldValue, newValue, te, tag) {
            var ns = this, tagVar = ns._tagVar,
                tag = profile.properties.tag;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                if (tag == "con") {
                    tagVar.args[1] = newValue || null;
                } else {
                    tagVar.args[0] = newValue || null;
                }
                ns.markDirty(1);
            }
        },
        _toggle_value_onchange: function (profile, oldValue, newValue) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[profile.properties.tag ? 1 : 0] = newValue == "expand";
                ns.markDirty(1);
            }
        },
        _cb_setid_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[0] = newValue;
                ns.markDirty(1);
            }
        },
        _cb_setvalue_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[1] = newValue;
                ns.markDirty(1);

                ns.ctl_grpp.setHtml("", true);
                var t = newValue, m, gotit, _ns = ns.getScope();
                if (/^\s*\{[\w][\w\.\s*]+[\w]\s*(\[[\d\s]+\])?(\(\s*\))?\s*\}\s*$/.test(newValue)) {
                    t = newValue.split(/\s*\.\s*/);
                    if (t.length === 1) {
                        m = newValue.replace(/[{}()\s]/g, '');
                        t = '{window}';
                    } else {
                        m = t.pop().replace(/[()}\s]/g, '');
                        t = t.join(".") + "}";
                    }
                    t = ood.adjustVar(t, _ns);
                    if (t && ood.isFun(t[m])) {
                        gotit = 1;
                    }
                }
                if (gotit) {
                    if (t.$key) {
                        path = t.$key;
                        if (!t.$oodclass$) path += ".prototype";
                        path += "." + m;
                    } else {
                        path = newValue.replace(/[{\s}]/g, "")
                    }

                    var doc = RAD.EditorTool.getDoc(path, false, true);
                    var arr = doc && doc.$paras;
                    if (!arr && ood.str.trim(ood.fun.body(t[m])) != "[native code]") arr = ood.fun.args(t[m]);
                    if (arr && arr.length) {
                        for (var i = 0, j, l = arr.length; i < l; i++) {
                            j = arr[i].indexOf("[");
                            if (j != -1)
                                arr[i] = "<strong>" + arr[i].substr(0, j) + "</strong>" + arr[i].substr(j, arr[i].length);
                        }
                        ns.ctl_grpp.setHtml("<ol style='padding-left:20px;margin: 8px;line-height: 20px;'><li>" + arr.join("</li><li>") + "</li></ol>", true);
                    }
                }
            }
        },
        _cb_callid_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                if (!newValue) {
                    tagVar.args = [];
                    ns.grid_params.removeAllRows();
                    return;
                }

                tagVar.args = tagVar.args || [];
                tagVar.args[0] = newValue;
                ns.markDirty(1);

                var t = newValue, tt, m, funType, _ns = ns.getScope();
                if (/^\s*\{[\w][\w\.\s*]+[\w]\s*(\[[\d\s]+\])?(\(\s*\))?\s*\}\s*$/.test(newValue)) {
                    t = newValue.split(/\s*\.\s*/);
                    if (t.length === 1) {
                        m = newValue.replace(/[{}()\s]/g, '');
                        t = '{window}';
                    } else {
                        m = t.pop().replace(/[()}\s]/g, '');
                        t = t.join(".") + "}";
                    }
                    if (tagVar.className && ood.getModule(tagVar.className)) {
                        t = ood.adjustVar(tt = t, {page: ood.getModule(tagVar.className)});
                    } else {
                        t = ood.adjustVar(tt = t, _ns);
                    }

                    if (!ood.isDefined(t)) {
                        funType = ood.str.trim(ood.fun.body(m)) == "[native code]" ? 'nvative' : 'external';
                    } else if (t && t[m]) {
                        if (ood.isFun(t[m])) {
                            // for native function
                            funType = ood.str.trim(ood.fun.body(t[m])) == "[native code]" ? 'native' : 'function';
                        } else if (t[m].args && ood.isArr(t[m].args) && t[m].args.length) {
                            t = t[m].args;
                            funType = 'pusedo';
                        } else {
                            funType = 'unknow';
                        }
                    }

                } else {
                    tagVar.args = [];
                    ns.grid_params.removeAllRows();
                    return;
                }

                var rows = ns.grid_params.getRows(),
                    len = rows.length,
                    maxlen = 10,
                    path;

                if (funType == 'function') {
                    if (t.$key) {
                        path = t.$key;
                        if (!t.$oodclass$) path += ".prototype";
                        path += "." + m;
                    } else {
                        path = newValue.replace(/[{\s}]/g, "")
                    }

                    var doc = RAD.EditorTool.getDoc(path, false, true);
                    var arr = doc && doc.$paras;
                    if (!arr) arr = ood.fun.args(t[m]);
                    if (arr && arr.length) {
                        var len2 = arr.length;
                        if (len > len2) {
                            for (var i = len; i >= len2; i--)
                                ns.grid_params.removeRows("" + (i + 3));
                        } else if (len < len2) {
                            for (var i = len, l = len2; i < l; i++)
                                ns.grid_params.insertRows([{id: "" + (i + 3), cells: ["", ""]}]);
                        }
                        for (var i = 0, l = len2; i < l; i++)
                            ns.grid_params.updateCellByRowCol("" + (i + 3), "key", arr[i], false, false);
                        return;
                    }
                }
                else if (funType == 'pusedo') {
                    var arr = t;
                    var len2 = arr.length;
                    if (len > len2) {
                        for (var i = len; i >= len2; i--)
                            ns.grid_params.removeRows("" + (i + 3));
                    } else if (len < len2) {
                        for (var i = len, l = len2; i < l; i++)
                            ns.grid_params.insertRows([{id: "" + (i + 3), cells: ["", ""]}]);
                    }
                    for (var i = 0, l = len2; i < l; i++)
                        ns.grid_params.updateCellByRowCol("" + (i + 3), "key", arr[i].id + " : " + arr[i].type + (arr[i].des ? ", " + arr[i].des : ""), false, false);
                    return;
                }

                if (len < maxlen) {
                    for (var i = len, l = maxlen; i < l; i++)
                        ns.grid_params.insertRows([{id: "" + (i + 3), cells: ["", ""]}]);
                }
                for (var i = 0, l = maxlen; i < l; i++) {
                    ns.grid_params.updateCellByRowCol("" + (i + 3), "key", " " + ood.getRes("$RAD.action.Parameter") + " " + (i + 1), false, false);
                }
            }
        },
        _cb_callreturnto_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[1] = newValue;
                ns.markDirty(1);
                var tt = tagVar.args;
                ns.conf_return_exp.setHtml(tt[1] && tt[1] != 'none' ? '${' + (tt[1] + '.') + (tt[2] ? tt[2] : '?') + '}' : '');
            }
        },
        _cb_callreturn_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[2] = newValue;
                ns.markDirty(1);
                var tt = tagVar.args;
                ns.conf_return_exp.setHtml(tt[1] && tt[1] != 'none' ? '${' + (tt[1] + '.') + (tt[2] ? tt[2] : '?') + '}' : '');
            }
        },

        _tb_cat_onchange: function (profile, oldValue, newValue) {
            if (!newValue) return;
            var ns = this, t,
                subvalue = "none",
                item = profile.getItemByItemId(newValue);

            if (item) {
                ns.lbl_target.setCaption((item && item.caption || "").split("<br />")[0]);
                var items = ns._buildsubtree(item);
                if (items && items.length) subvalue = items[0].id;

                var method;
                if (ns._tagVar) {
                    if (item.className && item.className != SPA.getCurrentClassName()) {
                        ns._tagVar["className"] = item.className;
                    }
                    if (item.euClassName && item.euClassName != SPA.getCurrentClassName()) {
                        ns._tagVar["euClassName"] = item.euClassName;
                    } else {
                        //  delete ns._tagVar.className ;
                    }
                    ns._tagVar["type"] = item._type;
                    t = item.id;
                    if (item._type == "none") {
                        t = t.replace("none:", "");
                    }
                    if (item._type == "page" || item._type == "module") {
                        //t=t;
                    } else if (item._type == "other") {
                        t = t.replace("other:", "");
                    }

                    if (oldValue && oldValue != 'none:none') {
                        //ns._tagVar["args"]=[] ;
                        // delete  ns._tagVar["method"] ;
                    }
                    ns._tagVar["target"] = t;

                    if (ood.arr.subIndexOf(items, "id", ns._tagVar.method) != -1) {
                        subvalue = ns._tagVar.method;
                    }
                    ns.markDirty(1);
                }
            }
            ns.tb_action.setUIValue(ns.tb_action.getItemByItemId(subvalue) ? subvalue : "none", true);
        },
        _tb_action_onchange: function (profile, oldValue, newValue) {
            if (!newValue) return;
            var ns = this,
                prop = ns.properties,
                parent = prop.parent,
                tagVar = ns._tagVar,
                subvalue = "none",
                item = profile.getItemByItemId(newValue);

            if (tagVar && !ns._initvalue && oldValue) {
                tagVar.args = ood.isSet(item.args) ? ood.copy(item.args) : [];
                delete tagVar.onOK;
                delete tagVar.onKO;
                delete tagVar.adjust;
                delete tagVar.redirection;
            }
            ns.markDirty(1);
            ns._targetshadow = ns._targetshadow2 = null;

            var nv, ov = ns.tab_conf.getUIValue();
            if (item) {
                if (ns.tab_conf.getItemByItemId(item.cat)) {
                    nv = item.cat;
                } else if (item.customUI) {
                    item.customUI(ns, ns.tab_conf);
                    nv = item.cat;
                }
            }
            if (!nv) nv = "none";

            ns.tab_conf.setUIValue(nv, true);

            if (tagVar && !ns._initvalue && nv != ov) tagVar["args"] = ood.isSet(item.args) ? ood.copy(item.args) : tagVar["args"];

            if (item) {
                ns.lbl_action.setCaption(item && item.caption || "");

                if (tagVar) {
                    tagVar["method"] = item.id;
                    delete tagVar.onOK;
                    delete tagVar.onKO;
                }

                // must reset here
                ns.add_target.setTagVar(null, true);

                switch (item.cat) {
                    case 'animate':
                        tagVar.onOK = 1;
                        var value = ood.get(tagVar, ["args", 0]) || "blinkAlert";
                        ns.iniPureValue(ns.ctl_animate, value);
                        break;
                    case 'selfile':
                        if (item.id == 'selectFile') {
                            tagVar.onOK = 0;
                        }
                        break;
                    case 'fmsg':
                        ns.msg_body.setUIValue(ood.get(tagVar, ["args", 0]) || "");
                        ns.msg_cap.setUIValue(ood.get(tagVar, ["args", 1]) || "");
                        ns.msg_width.setUIValue(ood.get(tagVar, ["args", 2]) || 200);
                        ns.msg_duration.setUIValue(ood.get(tagVar, ["args", 3]) || 5000);
                        break;
                    case "log":
                        ns.log_v1.setValue(ood.get(tagVar, ["args", 0]) || "");
                        ns.log_v2.setValue(ood.get(tagVar, ["args", 1]) || "");
                        ns.log_v3.setValue(ood.get(tagVar, ["args", 2]) || "");
                        ns.log_v4.setValue(ood.get(tagVar, ["args", 3]) || "");
                        ns.log_v5.setValue(ood.get(tagVar, ["args", 4]) || "");
                        break;
                    case "msg":
                        ns.msg_title.setUIValue(ood.get(tagVar, ["args", 0]) || "");
                        ns.msg_message.setUIValue(ood.get(tagVar, ["args", 1]) || "");
                        if (item.id == "alert" || item.id == "confirm") {
                            ns.msg_m.setDisplay('');
                            tagVar.onOK = 2;
                            if (item.id == "confirm") {
                                tagVar.onKO = 3;
                                tagVar.okFlag = "_confirm_yes";
                                tagVar.koFlag = "_confirm_no";
                            }
                        } else if (item.id == "prompt") {
                            ns.msg_m.setDisplay('');
                            tagVar.onOK = 3;
                            tagVar.onKO = 4;
                            tagVar.okFlag = "_prompt_ok";
                            tagVar.koFlag = "_prompt_cancel";
                        } else
                            ns.msg_m.setDisplay('none');
                        if (item.id == "prompt") {
                            ns.msg_input.setDisplay('');
                            ns.msg_inputm.setDisplay('');
                            ns.msg_input.setUIValue(ood.get(tagVar, ["args", 2]) || "");
                        } else {
                            ns.msg_input.setDisplay('none');
                            ns.msg_inputm.setDisplay('none');
                        }
                        break;
                    case "url":
                        ns.url_address.setUIValue(ood.get(tagVar, ["args", 0]) || "");
                        ns._jsoneditor2.render();
                        ns._jsoneditor2.setValue(ood.get(tagVar, ["args", 1]) || {});
                        break;
                    case "datatransfer":
                        if (item.id == "invoke") {
                            ns.fileurl_address.setDisplay('none');
                            ns.ctl_slabel57.setTop('.5em');
                            tagVar.onOK = 0;
                            tagVar.onKO = 1;

                            tagVar.okFlag = "_DI_succeed";
                            tagVar.koFlag = "_DI_fail";
                        } else {
                            ns.ctl_slabel57.setTop('4em');
                            ns.fileurl_address.setDisplay('')
                                .setUIValue(ood.get(tagVar, ["args", 0]) || "");
                            if (item.id == 'readText') {
                                tagVar.onOK = 1;
                                tagVar.onKO = 2;
                                tagVar.okFlag = "_DI_succeed";
                                tagVar.koFlag = "_DI_fail";
                            } else if (item.id == 'readJSON') {
                                tagVar.onOK = 1;
                                tagVar.onKO = 2;
                                tagVar.okFlag = "_DI_succeed";
                                tagVar.koFlag = "_DI_fail";
                            }
                        }
                        break;
                    case 'mix':
                        var prf = item["0"],
                            prop = prf.properties,
                            method = tagVar.method,
                            hasSub = prf.box['ood.UI.TreeBar'] || (prf.box['ood.UI.TreeGrid'] && method == "insertRows"),
                            insert = method == "insertItems" || method == "insertRows" || method == "insertCol",
                            specialUpdate = method == "setFormValues",
                            update = /^(update|set)/.test(method),
                            keyN = (method == "insertRows" || method == "updateRow" || method == "setActiveRow" || method == "updateCellByRowCol2") ? "rows" :
                                (method == "insertCol" || method == "updateHeader" || method == "removeCols" || method == "showColumn" || method == "sortColumn") ? "header" :
                                    "items",
                            org = prop[keyN],
                            items;

                        if (method == "updateCellByRowCol2") {
                            items = [];
                            var fun = function (rows, items, pk) {
                                ood.arr.each(rows, function (row, i) {
                                    if (row.id == ood.UI.TreeGrid._temprowid) return;

                                    var sub = [];
                                    items.push({
                                        id: pk + ":" + i,
                                        caption: ood.getRes("RAD.action.Row $0", pk + i),
                                        group: true,
                                        sub: sub
                                    });
                                    ood.arr.each(prop.header, function (col, j) {
                                        sub.push({
                                            id: pk + i + ":" + j,
                                            row: i,
                                            col: j,
                                            caption: ood.getRes("RAD.action.Row $0", pk + i) + "  , " + ood.getRes("RAD.action.Column $0", j)
                                        })
                                    });
                                });
                            };
                            fun(prop.rows, items, "");
                        } else {
                            items = ood.clone(org, true);
                            ood.filter(items, function (o) {
                                if (o.id == ood.UI.TreeGrid._temprowid) return false;
                                return true;
                            });
                        }
                        ;
                        if (!items || !items.length) items = [];
                        if (method == "setFormValues") {
                        } else {
                            items.push({
                                id: "[key]",
                                caption: "$(RAD.action.Other sub$-item (Empty means current$))"
                            });
                        }
                        ns.add_target.setItems(items);

                        // List.insertItems(arr, base, before
                        // TreeBar.insertItems(arr, pid, base, before
                        ns.add_target.setTagVar({
                            hasSub: hasSub,
                            insert: insert,
                            update: update,
                            specialUpdate: specialUpdate,
                            clsN: prf.box.KEY,
                            keyN: keyN
                        });

                        //init
                        ns.blk_itempos.setDisplay(insert ? '' : 'none', true).setDockIgnore(!insert, true);
                        if (hasSub) {
                            ns.add_pos.showItems(['subfirst', 'sublast', 'cursubfirst', 'cursublast']);
                        } else {
                            ns.add_pos.showItems(['subfirst', 'sublast', 'cursubfirst', 'cursublast'], false);
                        }
                        var item_id, value;
                        if (insert) {
                            //default is string
                            value = ood.get(tagVar, ["args", 0]) || "";

                            //treebar/treeview/treegrid.rows
                            if (hasSub) {
                                var pid = ood.get(tagVar, ["args", 1]) || "",
                                    baseid = ood.get(tagVar, ["args", 2]) || "",
                                    before = ood.get(tagVar, ["args", 3]),
                                    pos

                                if (pid) {
                                    if (pid === true) {
                                        pos = before ? "cursubfirst" : "cursublast";
                                        ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                        ns.add_target.setValue("[key]", true);
                                        ns.add_pos.setValue(pos, true);
                                    } else {
                                        pos = before ? "subfirst" : "sublast";
                                        ns.ood_block_mix.setDisplay("").adjustDock();
                                        ns.tb_mixsub.setReadonly(pid != "[key]" && !!ns.add_target.getItemByItemId(pid), true);
                                        ns.tb_mixsub.setValue(pid, true);
                                        if (ns.add_target.getItemByItemId(pid))
                                            ns.add_target.setValue(pid, true);
                                        else
                                            ns.add_target.setValue("[key]", true);
                                        ns.add_pos.setValue(pos, true);
                                    }
                                } else {
                                    if (baseid) {
                                        if (baseid === true) {
                                            pos = before ? "curbefore" : "curafter";
                                            ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                        } else {
                                            pos = before ? "before" : "after";
                                            ns.ood_block_mix.setDisplay("").adjustDock();
                                            ns.tb_mixsub.setReadonly(baseid != "[key]" && !!ns.add_target.getItemByItemId(baseid), true);
                                        }
                                    } else {
                                        pos = before ? "first" : "last";
                                        ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                    }
                                    ns.tb_mixsub.setValue(baseid, true);
                                    if (ns.add_target.getItemByItemId(baseid))
                                        ns.add_target.setValue(baseid, true);
                                    else
                                        ns.add_target.setValue("[key]", true);
                                    ns.add_pos.setUIValue(pos, true);
                                }
                            } else {
                                var baseid = ood.get(tagVar, ["args", 1]) || "",
                                    before = ood.get(tagVar, ["args", 2]),
                                    pos;
                                if (baseid) {
                                    if (baseid === true) {
                                        pos = before ? "curbefore" : "curafter";
                                        ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                    } else {
                                        pos = before ? "before" : "after";
                                        ns.ood_block_mix.setDisplay("").adjustDock();
                                        ns.tb_mixsub.setReadonly(baseid != "[key]" && !!ns.add_target.getItemByItemId(baseid), true);
                                    }
                                } else {
                                    pos = before ? "first" : "last";
                                    ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                }
                                ns.tb_mixsub.setValue(baseid, true);
                                if (ns.add_target.getItemByItemId(baseid))
                                    ns.add_target.setValue(baseid, true);
                                else
                                    ns.add_target.setValue("[key]", true);
                                ns.add_pos.setUIValue(pos, true);
                            }
                        } else {
                            if (specialUpdate) {
                                value = ood.get(tagVar, ["args", 0]) || "";
                                item_id = null;
                            } else {
                                //default is string
                                value = ood.get(tagVar, ["args", 1]) || "";
                                item_id = ood.get(tagVar, ["args", 0]) || "";
                            }

                            if (ns.add_target.getItemByItemId(item_id)) {
                                ns.add_target.setValue(item_id, true);
                                ns.tb_mixsub.setValue(item_id, true);
                                ns.ood_block_mix.setDisplay("").adjustDock();
                                ns.tb_mixsub.setReadonly(item_id != "[key]", true);
                            } else {
                                if (item_id) {
                                    ns.add_target.setValue("[key]", true);
                                    ns.tb_mixsub.setValue(item_id, true);
                                    ns.ood_block_mix.setDisplay("").adjustDock();
                                    ns.tb_mixsub.setReadonly(false, true);
                                } else {
                                    if (items.length) {
                                        ns.add_target.setUIValue(item_id, true);
                                        item_id = items[0].id
                                        ns.tb_mixsub.setUIValue(item_id == "[key]" ? "" : item_id, true);
                                        ns.ood_block_mix.setDisplay("").adjustDock();
                                        ns.tb_mixsub.setReadonly(true, true);
                                    }
                                    // no-items case for [setFormValues]
                                    else {
                                        ns.ood_block_mix.setDisplay("none").getParent().adjustDock(true);
                                        ns.tb_mixsub.setReadonly(false, false);
                                    }
                                }
                            }
                        }

                        ns.iniPureValue(ns.ctl_itemvar, value);
                        break;
                    case "con":
                        // prepare UI
                        ns.tb_con.setSelMode('single');
                        ns.ctl_blockcon.setDisplay("block");
                        ns.ctl_tocon.setDisplay("block");

                        switch (tagVar.method) {
                            case "showItems":
                            case "hideItems":
                            case "setValue":
                            case "fireItemClickEvent":
                            case "editItem":
                            case "removeItems":
                            case "setActiveRow":
                            case "removeRows":
                            case "removeCols ":
                            case "dumpContainer":
                            case "isDirtied":
                            case "checkValid":
                            case "checkRequired":
                            case "getFormValues":
                            case "formSubmit":
                            case "formReset":
                            case "formClear":
                            case "updateFormValues":
                                // mark it first
                                ns.tb_consub.setTag('fireitem');

                                if (tagVar.method != "setValue"
                                    && tagVar.method != "fireItemClickEvent"
                                    && tagVar.method != "editItem"
                                    && tagVar.method != "checkValid"
                                    && tagVar.method != "isDirtied"
                                    && tagVar.method != "checkRequired"
                                    && tagVar.method != "getFormValues"
                                    && tagVar.method != "formSubmit"
                                    && tagVar.method != "formReset"
                                    && tagVar.method != "formClear"
                                    && tagVar.method != "updateFormValues"
                                ) {
                                    ns.tb_con.setSelMode('multibycheckbox');
                                }
                                ns.toggle_value.setDisplay('none');

                                var org = item["0"].properties[tagVar.method == "removeRows" || tagVar.method == "setActiveRow" ? "rows" : tagVar.method == "removeCols" ? "header" : "items"],
                                    items = [];
                                if (org && ood.isArr(org)) {
                                    items = ood.clone(org, true);
                                    items.push({
                                        id: "[key]",
                                        caption: "$(RAD.action.Other sub$-item (Empty means current$))"
                                    });
                                } else {
                                    ns.ctl_blockcon.setDisplay("none");
                                    ns.ctl_tocon.setDisplay("none");
                                }
                                ns.tb_con.setItems(items, true);
                                break;
                            case "setToggle":
                            case "toggleNode":
                            case "toggleRow":
                                // mark it first
                                ns.tb_consub.setTag('toggle');

                                ns.toggle_value.setDisplay('');
                                ns.toggle_value.setTag(item.hassub);
                                // setToggle/toggleRow/toggleNode has different args
                                if (item.hassub) {
                                    var items = ood.clone(item["0"].properties.items || item["0"].properties.rows, function (v, i) {
                                        if (v && v.group) delete v.group;
                                        return (i == "sub" && typeof(v[0]) == "object") || (typeof(v) == "object" ? !!v.sub : (i + "").charAt(0) != "_");
                                    });
                                    items.push({
                                        id: "[key]",
                                        caption: "$(RAD.action.Other sub$-item (Empty means current$))"
                                    });
                                    ns.tb_con.setItems(items);
                                    ns.tb_consub.setDisplay('');
                                } else {
                                    ns.tb_con.setItems([]);
                                    ns.tb_consub.setDisplay('none');
                                }
                                break;
                            case "show":
                                var id = ood.get(tagVar, ["args", 0]);

                                // mark it first
                                ns.tb_consub.setTag('con');

                                ns.toggle_value.setDisplay('none');
                                // keep _iscon
                                ns.tb_con.setItems(ood.clone(ns._panelitems));

                                var ii = ns.tb_con.getItems();
                                for (var i = 0, l = ii.length; i < l; i++) {
                                    if (ii[i].id != "none" && (
                                        // currentpage no container
                                        ((ood.getClassName(SPA.currentPage) == tagVar["target"]) || ii[i]["parent:" + tagVar["target"]])
                                    )
                                    ) {
                                        ns.tb_con.showItems(ii[i].id, false);
                                    } else
                                        ns.tb_con.showItems(ii[i].id);
                                }
                                break;
                        }
                        // set value to UI
                        switch (tagVar.method) {
                            case "setValue":
                            case "fireItemClickEvent":
                            case "editItem":
                                var id = ood.get(tagVar, ["args", 0]) || "";
                                if (ns.tb_con.getItemByItemId(id)) {
                                    ns.tb_consub.setReadonly(true, true);
                                    ns.tb_con.setValue(id);
                                } else {
                                    ns.tb_con.setValue("[key]");
                                    ns.tb_consub.setReadonly(false, true);
                                }
                                ns.tb_consub.setUIValue(id == '[key]' ? '' : id, true);
                                break;
                            case "showItems":
                            case "hideItems":
                            case "removeItems":
                            case "setActiveRow":
                            case "removeRows":
                            case "removeCols ":
                            case "dumpContainer":
                                var id = ood.get(tagVar, ["args", 0]) || "";
                                var arr = (id + "").split(";"), other;
                                for (var i = 0, l = arr.length; i < l; i++) {
                                    if (!ns.tb_con.getItemByItemId(arr[i])) {
                                        other = true;
                                        break;
                                    }
                                }
                                if (other) {
                                    ns.tb_con.setValue("[key]", true);
                                    ns.tb_consub.setReadonly(false, true);
                                } else {
                                    ns.tb_con.setValue(id, true);
                                    ns.tb_consub.setReadonly(true, true);
                                }
                                ns.tb_consub.setValue(id, true);
                                break;
                            case "setToggle":
                            case "toggleNode":
                            case "toggleRow":
                                if (!item.hassub) {
                                    ns.toggle_value.setUIValue(ood.get(tagVar, ["args", 0]) ? "expand" : "folding", true);
                                    ns.tb_con.setValue("", true);
                                    ns.tb_consub.setValue("", true);
                                } else {
                                    ns.toggle_value.setUIValue(ood.get(tagVar, ["args", 1]) ? "expand" : "folding", true);
                                    var id = ood.get(tagVar, ["args", 0]) || "";
                                    if (!ns.tb_con.getItemByItemId(id)) {
                                        ns.tb_con.setValue("[key]", true);
                                        ns.tb_consub.setReadonly(false, true);
                                    } else {
                                        ns.tb_con.setValue(id, true);
                                        ns.tb_consub.setReadonly(true, true);
                                    }
                                    ns.tb_consub.setValue(id, true);
                                }
                                break;
                            case "show":
                                var id = ood.get(tagVar, ["args", 0]) || "";
                                var subid = ood.get(tagVar, ["args", 1]) || "";
                                if (subid) {
                                    var t = id.replace('}', "." + subid + '}');
                                    if (ns.tb_con.getItemByItemId(t)) {
                                        ns.tb_con.setValue(t, true);
                                        ns.tb_consub.setReadonly(true, true);
                                    } else {
                                        ns.tb_con.setValue(id.replace('}', ".[key]}"), true);
                                        ns.tb_consub.setReadonly(false, true);
                                    }
                                    ns.tb_consub.setValue(subid, true);
                                } else {
                                    ns.tb_con.setValue(id, true);
                                    ns.tb_consub.setValue("", true);
                                }
                                break;
                        }
                        break;
                    case "json":
                        if (!tagVar["args"]) tagVar["args"] = [];
                        if (!ood.isHash(tagVar["args"][0])) tagVar["args"][0] = {};
                        ns._jsoneditor.render();
                        ns._jsoneditor.setValue(tagVar["args"][0]);
                        break;
                    case "prop":
                        var alias = tagVar.target,
                            target = parent.getByAlias(alias, true);

                        if (!tagVar["args"]) tagVar["args"] = [];
                        if (!ood.isHash(tagVar["args"][0])) tagVar["args"][0] = {};
                        if (!ood.isHash(tagVar["args"][1])) tagVar["args"][1] = {};

                        var targetshadow = target.boxing().clone().get(0),
                            targetshadow2 = {properties: {}};

                        ns._hiddenDiv.html("");
                        if (targetshadow.box['ood.UI'])
                            ns._hiddenDiv.append(targetshadow);
                        var param = tagVar["args"][0],
                            param1 = tagVar["args"][1],
                            grid = ns.profileGrid,
                            arr = [];

                        for (var i in param) {
                            if (i == 'CC') {
                                targetshadow.CC = ood.clone(param.CC);
                                if (ood.arr.indexOf(arr, 'CS') == -1) arr.push('CS');
                            } else if (i == 'CS') {
                                targetshadow.CS = ood.clone(param.CS);
                                if (ood.arr.indexOf(arr, 'CS') == -1) arr.push('CS');
                            } else {
                                targetshadow.properties[i] = typeof(param[i]) == "object" ? ood.clone(param[i]) : param[i];
                                if (ood.arr.indexOf(arr, i) == -1) arr.push("properties:" + i);
                            }
                        }
                        for (var i in param1) {
                            if (i == 'CC') {
                                targetshadow2.CC = ood.clone(param.CC);
                                if (ood.arr.indexOf(arr, 'CS') == -1) arr.push('CS');
                            } else if (i == 'CS') {
                                targetshadow2.CS = ood.clone(param.CS);
                                if (ood.arr.indexOf(arr, 'CS') == -1) arr.push('CS');
                            } else {
                                targetshadow2.properties[i] = typeof(param[i]) == "object" ? ood.clone(param[i]) : param[i];
                                if (ood.arr.indexOf(arr, i) == -1) arr.push("properties:" + i);
                            }
                        }

                        ns._targetshadow = targetshadow;
                        ns._targetshadow2 = targetshadow2;
                        grid.get(0).$widget = targetshadow.boxing();
                        var rows = parent._buildPropRows(targetshadow, null, null, true, grid), cellv;
                        // set row manually
                        ood.arr.each(rows, function (row) {
                            row.initFold = false;
                            ood.arr.each(row.sub, function (r) {
                                r.rulerWidth = '.5em';
                                r.cells[1]._ov = r.cells[1].value;

                                // for value var column
                                r.cells[2] = {value: ""};
                                if (param1 && (cellv = param1[r.id.replace("properties:", "")])) {
                                    r.cells[2].value = cellv;
                                    if (/^\s*\{([\S]+)\}\s*$/.test(cellv)) {
                                        r.cells[2]._$caption = '$' + cellv;
                                    }
                                }
                            });
                        });
                        grid.setRows(rows);
                        //ns.propFilter.setValue("",true);
                        grid.setValue(arr);
                        this._propFilter_();
                        break;
                    case "data":
                        ns.data_source.setUIValue((ood.get(tagVar, ["args", 0]) || ""));
                        break;
                    case "switch":
                        var v = ood.get(tagVar, ["args", 0]);
                        ns.ctl_cache.setUIValue(v !== false, true);
                        break;
                    case "var":
                        ns.ctl_varto.setUIValue(ood.get(tagVar, ["args", 0]) || "");

                        var value = ood.get(tagVar, ["args", 1]) || "",
                            vpath = ood.get(tagVar, ["args", 2]) || "";

                        ns.iniPureValue(ns.ctl_varfrom, value);

                        ns.ctl_varfrompath.setValue(vpath, true).setTagVar(vpath || null);

                        ns.ctl_adjust.setUIValue(ood.get(tagVar, ["adjust"]) || "");

                        var s = "${" + tagVar.method + "." + (ood.get(tagVar, ["args", 0]) || "?") + "} = ";
                        ns.conf_var_left.setHtml(s);
                        break;
                    case "setcb":
                        ns.cb_setid.setUIValue(ood.get(tagVar, ["args", 0]) || "");
                        ns.cb_setvalue.setUIValue(ood.get(tagVar, ["args", 1]) || "");
                        break;
                    case "callcb":
                        if (item.redirection && item._tpl) {
                            var alias = tagVar.target;
                            if (alias.indexOf(".") > -1) {
                                alias = alias.split(".")[alias.split(".").length - 1]
                            }
                            var value = item._tpl.replace('*', alias);
                            // tagVar.itaget = alias;
                            //  var value=value.replace(item.className+".","");
                            //   var value = item._tpl.replace('*', tagVar.target);

                            // if (item.className) {
                            //     value = value.replace("page." + item.className, item.className + ".getInstance()");
                            // }

                            ns.cb_callid.setUIValue(value, true);
                            ns.cb_callid.setDisabled(true);
                            tagVar.redirection = item.redirection;
                        } else {
                            ns.cb_callid.setDisabled(false);
                            ns.cb_callid.setUIValue(ood.get(tagVar, ["args", 0]) || "", true);

                            if (item.redirection) tagVar.redirection = item.redirection;
                            else delete tagVar.redirection;
                        }
                        var w = ood.get(tagVar, ["args", 1]) || "none", itms = ns.cb_callreturnto.getItems();
                        if (ood.arr.subIndexOf(itms, 'id', w) == -1) w = 'none';
                        ns.cb_callreturnto.setUIValue(w);
                        ns.cb_callreturn.setUIValue(ood.get(tagVar, ["args", 2]) || "");

                        var tt = tagVar.args;
                        ns.conf_return_exp.setHtml(tt[1] && tt[1] != 'none' ? '${' + (tt[1] + '.') + (tt[2] ? tt[2] : '?') + '}' : '');

                        var l = ns.grid_params.getRows().length;
                        if (!l) l = 10;
                        for (var i = 0; i < l; i++) {
                            ns.grid_params.updateCellByRowCol("" + (i + 3), "value", {
                                value: tagVar.args[i + 3]
                            }, false, false);
                        }
                        ns.grid_params.offEditor(true);
                        break;
                    default:
                        if (item.initUI) item.initUI(ns, tagVar);
                        break;
                }
            }
        },
        _toolbar_onclick: function (profile, tool, grp, e, src) {
            var ns = this,
                tb = ns.tb_actions,
                items = tb.getItems(),
                cur = tb.getUIValue(),
                item = tb.getItemByItemId(cur);
            switch (tool.id) {
                case "add":
                    var i = items.length + 1,
                        name = ood.adjustRes("$RAD.action.Action");
                    while (ood.arr.subIndexOf(items, "caption", name + " " + i) !== -1) i++;
                    var nitem = ns.addAction(name + " " + i);
                    tb.editItem(nitem.id);
                    break;
                case "edit":
                    if (cur) tb.editItem(cur);
                    break;
                case "remove":
                    var v = tb.getUIValue();
                    if (!v) return;
                    ood.confirm("$RAD.Remove", ood.adjustRes("$(RAD.Are You Sure to remove this row)?"), function () {
                        tb.removeItems(v);
                        var items = tb.getItems();
                        if (items && items.length)
                            tb.setUIValue(items[0].id);
                        else
                            ns._initctrls();
                        ns.markDirty(1);
                    });
                    break;
                case "clear":
                    ood.confirm("$RAD.Remove", ood.adjustRes("$(RAD.Are You Sure to remove all)?"), function () {
                        ns._initctrls(true);
                        ns.markDirty(1);
                    });
                    break;
                default:
            }
        },
        _tb_beforeapply: function (profile, item, nv, editor) {
            var items = profile.properties.items,
                index = ood.arr.subIndexOf(items, 'caption', nv);
            if (index != -1 && items[index] != item) {
                ood.alert(ood.adjustRes("$RAD.Message"), ood.adjustRes("$(RAD.action.This Action Name exists alerady)"), function () {
                    editor.activate();
                });
                return false;
            }
            if (!ood.str.trim(nv)) {
                ood.alert(ood.adjustRes("$RAD.Message"), ood.adjustRes("$(RAD.action.Specify Action Description please)"), function () {
                    editor.activate();
                });
                return false;
            }
            item.tagVar.desc = nv;
            this.markDirty(1);
        },
        addAction: function (desc) {
            var ns = this,
                items = ns.tb_actions.getItems();
            if (ood.arr.subIndexOf(items, 'caption', desc) != -1) {
                ood.alert(ood.adjustRes("$(RAD.action.This Action Name exists alerady)"));
                return false;
            }
            if (!ood.str.trim(desc)) {
                ood.alert(ood.adjustRes("$RAD.Message"), ood.adjustRes("$(RAD.action.Specify Action Description please)"));
                return false;
            }
            var hash = {
                desc: desc,
                type: "none",
                target: "none",
                args: []
            };
            var id = ood.stamp() + "",
                item = {
                    id: id,
                    caption: desc,
                    imageClass: "ri-settings-line",
                    tagVar: hash
                };
            ns.tb_actions.insertItems([item]);

            ns._enablectrls();
            ns.markDirty(1);
            ns.tb_actions.setUIValue(id, true);
            return item;
        },

        _url_address_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][0] = newValue;
            ns.markDirty(1);
        },
        _msg_title_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][0] = newValue;
            ns.markDirty(1);
        },
        _msg_message_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][1] = newValue;
            ns.markDirty(1);
        },
        _msg_input_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][2] = newValue;
            ns.markDirty(1);
        },
        _msg_cap_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][1] = newValue;
            ns.markDirty(1);
        },
        _msg_body_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][0] = newValue;
            ns.markDirty(1);
        },
        _msg_duration_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][3] = newValue;
            ns.markDirty(1);
        },
        _msg_width_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][2] = newValue;
            ns.markDirty(1);
        },
        _ds_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][0] = newValue || "";
            ns.markDirty(1);
        },
        _ctl_cache_onchange: function (profile, oldValue, newValue) {
            var ns = this;
            ns._tagVar["args"][0] = !!newValue;
            ns.markDirty(1);
        },
        _ctl_symbol_onchange: function (profile, oldValue, newValue, force, tag) {
            if (oldValue == newValue) return;
            var ns = this, tagVar = ns._tagVar, tag = profile.properties.tag;
            if (tagVar) {
                if (!tagVar.conditions) tagVar.conditions = [];
                if (!tagVar.conditions[tag - 1])
                    tagVar.conditions[tag - 1] = {
                        left: "",
                        symbol: "defined",
                        right: ""
                    };
                var con = tagVar.conditions[tag - 1];
                con.symbol = newValue;
                ns.markDirty(1);
                if (newValue == "defined" || newValue == "undefined" || newValue == "empty" || newValue == "non-empty") {
                    ns['i_conr' + tag].setUIValue("", true);
                    ns['i_conr' + tag].setDisabled(true, true);
                } else {
                    ns['i_conr' + tag].setDisabled(false, true);
                }
            }
        },
        _grid_params_onbe: function (profile, cell, editor) {
            editor.setTag('purevar');
            this.iniPureValue(editor, cell.value);
            this._enhanceInputs(editor.reBoxing('ood.UI.Input'));
        },
        _grid_params_aftercellupdated: function (profile, cell, options, isHotRow) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar)
                tagVar.args = tagVar.args || [];
            if ("value" in cell) {
                // from 3
                tagVar.args[parseInt(cell._row.id, 10)] = cell.tagVar || cell.value;
                ns.markDirty(1);
            }
        },
        _cb_asy_onchange: function (profile, oldValue, newValue) {
            var ns = this, tagVar = ns._tagVar;
            ns.spin_asy.setDisabled(!newValue);
            if (tagVar) {
                if (newValue) {
                    tagVar.timeout = ns.spin_asy.getUIValue();
                } else {
                    ns.spin_asy.setUIValue(0, true);
                    delete tagVar.timeout;
                }
                ns.markDirty(1);
            }
        },
        _spin_asy_onchange: function (profile, oldValue, newValue) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.timeout = newValue || 0;
                ns.markDirty(1);
            }
        },
        _jsoneditorchanged: function (module) {
            var ns = this,
                tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args[0] = module.getValue(true);
                ns.markDirty(1);
            }
        },
        _jsoneditorchanged2: function (module) {
            var ns = this,
                tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args[1] = module.getValue(true);
                ns.markDirty(1);
            }
        },
        _false_change: function (profile, oldValue, newValue) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                if (newValue)
                    tagVar["return"] = false;
                else if (tagVar["return"] === false)
                    delete tagVar["return"];
                ns.markDirty(1);
            }
        },
        $tg_tips: function () {
            var ns = this,
                prop = ns.properties,
                parent = prop.parent;
            return parent.$tg_tips.apply(parent, arguments);
        },
        markProperty: function (key, value, value2, setParam) {
            var ns = this,
                grid = ns.profileGrid,
                //CC and CSS are not prop, they are special in the editor
                // ** only CS here, CC will be included in CS
                rowid = key == "CS" ? "CS" : key == "CC" ? "CS" : ("properties:" + key),
                tagVar = ns._tagVar,
                args = tagVar.args || [],
                param, param1;
            if (!ood.isHash(args[0])) args[0] = {};
            if (!ood.isHash(args[1])) args[1] = {};
            param = args[0];
            param1 = args[1];

            if (ood.isSet(value2)) {
                if (rowid == "CS") {
                    ns._targetshadow2[key] = value2;
                } else {
                    ns._targetshadow2.properties[key] = value2;
                }
            } else if (ood.isSet(value)) {
                if (rowid == "CS") {
                    ns._targetshadow[key] = value;
                    delete ns._targetshadow2[key];
                } else {
                    ns._targetshadow.properties[key] = value;
                    delete ns._targetshadow2.properties[key];
                }
            }

            if (setParam) {
                delete param[key];
                delete param1[key];
                if (ood.isSet(value2))
                    param1[key] = value2;
                else if (ood.isSet(value))
                    param[key] = ood.clone(value, true);

                ns.markDirty(1);
            }
        },
        $profilegrid__rowsel: function (profile, row, e, src, type) {
            var ns = this,
                rowid = row.id,
                sh = ns._targetshadow,
                sh2 = ns._targetshadow2;

            if (rowid == "CS") {
                ns.markProperty("CC", type == -1 ? null : sh.CC, type == -1 ? null : sh2.CC, true);
                ns.markProperty("CS", type == -1 ? null : sh.CS, type == -1 ? null : sh2.CS, true);
            } else {
                var key = rowid.replace("properties:", "");
                ns.markProperty(key, type == -1 ? null : sh.properties[key], type == -1 ? null : sh2.properties[key], true);
            }
        },
        _propFilter_oncmd: function () {
            var w = this.propFilter;
            if (w.getUnit() != 'Show All') {
                w.setUnit("Show All");
            } else {
                w.resetValue("");
            }
            this._propFilter_();
        },
        '_propFilter_unit': function (profile, unit) {
            var w = this.propFilter;
            if (unit == 'Show All') {
                w.setInputReadonly(false);
            } else {
                w.resetValue("").setInputReadonly(true);
            }
            this._propFilter_();
        },
        _propFilter_onchange: function (profile, oldValue, newValue) {
            ood.resetRun('_propFilter_2', this._propFilter_, 500, [], this);
        },
        _propFilter_: function (newValue) {
            var page = this,
                newValue = page.propFilter.getUIValue(),
                unit = page.propFilter.getUnit(),
                makeReg = function () {
                    return new RegExp("\:\\w*" + newValue || "", 'i');
                };

            this.profileGrid.doFilter(unit != "Show All" || newValue ? function (row, helper, profile) {
                var self = arguments.callee, rtn;
                if (row == 'begin') {
                    if (unit == "Show All") {
                        if (!self._$reg)
                            self._$reg = makeReg(newValue);
                    }
                }
                else if (row == 'end') {
                    if (helper != 'prepareItem') {
                    }
                }
                else {
                    if (unit != "Show All") {
                        rtn = row.group || row.sub ? false : !row._selected;
                    } else if (newValue) {
                        rtn = row.group || row.sub ? false : !self._$reg.test(row.id);
                    }

                    if (helper != 'prepareItem' && row.id && row.sub && !row._checked) {
                        profile.boxing().toggleRow(row.id, true, false, true);
                    }

                    return rtn;
                }
            } : null);
        },
        $profilegrid_beforecellvalueset: function (profile, cell, hash, h, ext) {
            var ns = this,
                row = cell._row;
            if (cell._col.id == 'value') {
                if (ext && ext._ext) {
                    ood.each(ext._ext, function (value, key) {
                        ns.markProperty(key, value, null, !!row._selected);
                    });
                } else {
                    if (!('value' in hash) && !('unit' in hash)) return;

                    var target = profile.$widget, attr, funName, property, value;

                    value = hash.value;
                    // check properties name
                    if ((attr = cell._row.id.split(':')).length > 1) {
                        type = attr[0];
                        property = attr[1];
                    } else {
                        property = cell._row.id;
                    }
                    if (property == "width" && (ood.get(target.get(0), ['parent', 'properties', 'conDockRelative']) || ood.get(target.get(0), ['parent', 'properties', 'conLayoutColumns']))) {
                        return false;
                    } else {
                        var precision = cell.unit == 'em' ? 4 : 0;
                        if (target.get(0).box.$DataModel[property].$spaceunit) {
                            if ('value' in hash) {
                                hash.value = String(
                                    (value == 'auto') ? value :
                                        (parseFloat(value) === 0 ? 0 + (hash.unit || cell.unit || "") :
                                                parseFloat(value) ? parseFloat(value) : 'auto'
                                        )
                                );
                            }
                            hash.precision = precision;
                            //if the value changed in the process
                            if (value !== hash.value || hash.unit !== cell.unit || hash.precision !== precision) {
                                profile.boxing().updateCell(cell, hash);
                            }
                            value = hash.value == 'auto' ? 'auto' : cell.value + cell.unit;
                        }
                    }
                    var key = row.id.replace("properties:", "");
                    ns.markProperty(key, value, null, !!row._selected);
                }
            } else if (cell._col.id == 'value2') {
                if (!('value' in hash) && !('unit' in hash)) return;

                var target = profile.$widget, attr, funName, property, value;

                value = hash.value;
                // check properties name
                if ((attr = cell._row.id.split(':')).length > 1) {
                    type = attr[0];
                    property = attr[1];
                } else {
                    property = cell._row.id;
                }
                if (property == "width" && (ood.get(target.get(0), ['parent', 'properties', 'conDockRelative']) || ood.get(target.get(0), ['parent', 'properties', 'conLayoutColumns']))) {
                    return false;
                }

                if (/^\s*\{([\S]+)\}\s*$/.test(value)) {
                    hash._$caption = '$' + value;
                }

                var key = row.id.replace("properties:", "");
                // if has value, set it
                ns.markProperty(key, null, value || null, !!value);
            }
        },
        $tg_cellupd: function (profile, cell) {
            var ns = this, grid = profile.boxing(), arr;
            if (!cell._row._selected) {
                arr = grid.getUIValue(true);
                arr.push(cell._row.id);
                grid.setValue(arr);
                // add a row, trigger event
                grid.onRowSelected(profile, cell._row, null, null, 1);
            } else {
                if (cell._col.id == 'value') {
                    arr = grid.getUIValue(true);
                    if (cell.value === cell._ov) {
                        ood.arr.removeValue(arr, cell._row.id);
                    }
                    grid.setValue(arr);
                    // remove a row, trigger event
                    grid.onRowSelected(profile, cell._row, null, null, 0);
                } else if (cell._col.id == 'value2') {
                    arr = grid.getUIValue(true);
                    if (!cell.value) {
                        ood.arr.removeValue(arr, cell._row.id);
                    }
                    grid.setValue(arr);
                    // remove a row, trigger event
                    grid.onRowSelected(profile, cell._row, null, null, 0);
                }
            }
            // set the counterpart to original value
            if (cell._col.id == 'value') {
                grid.updateCell(cell._row.cells[2], {value: ""}, false, false);
            } else if (cell._col.id == 'value2') {
                grid.updateCell(cell._row.cells[1], {value: cell._row.cells[1]._ov}, false, false);
            }
        },
        $tg_beginedit: function (profile, cell, editor, type) {
            if (cell._col.id == 'value2') {
                this._enhanceInputs(editor.reBoxing('ood.UI.Input'));
            }
        },
        $tg_dblclick: function () {
            var ns = this,
                prop = ns.properties,
                parent = prop.parent;
            parent.$tg_dblclick.apply(ns, arguments);
        },
        _animate_buis: function (prf, ov, nv) {
            if (nv == "[customize]") {
                //****** todo
                return false;
            }
        },
        _animate_beforecombopop: function (profile, pos, e, src) {
            if (profile.properties.items.length) return;

            var ns = this, ins = profile.boxing();
            var items = [];
            ood.each(ood.Dom.$preDefinedAnims, function (v, i) {
                items.push(i);
            });
            /*
           /***** todo
           items.push({type:'split'});
           items.push({
               id:"[customize]",
               caption:"$(RAD.action.[customize])"
           });
           */
            ins.setItems(items);
        },
        _data_source_beforecombopop: function (profile, pos, e, src) {
            var ns = this, ins = profile.boxing();
            ood.Dom.busy();
            // avoid syn call
            ood.asyRun(function () {

                ood.Ajax(CONF.openFolderService, {
                        path: SPA.curProjectPath + "/data",
                        hashCode: ood.id(),
                        curProjectPath: SPA.curProjectPath
                    }
                    , function (txt) {
                        var obj = txt;
                        if (obj && !obj.error) {
                            var items = [];
                            obj.data.files = ood.arr.stableSort(obj.data.files, function (x, y) {
                                return x.layer > y.layer ? -1 : x.layer < y.layer ? 1 : x.location > y.location ? 1 : -1;
                            });
                            ood.arr.each(obj.data.files, function (o) {
                                if (o.type === 1 && /.+\.(js|json|txt)$/.test(o.location)) {
                                    o = o.location.replace(SPA.curProjectPath + "/", "");
                                    items.push({
                                        id: '[data]' + o,
                                        caption: o
                                    });
                                }
                            });
                            ins.setItems(items);
                            ins.expand();
                        }
                        // dont show message
                        ood.Dom.free();
                    }, function (obj) {
                        // dont show message
                        //ood.message(ood.get(obj,['error','message'])||obj||'no response!');
                        ood.Dom.free();
                    });
            });
        },
        _btncancel_onclick: function (profile, e, src, value) {
            var ns = this;
            ns.markDirty(0);
            ns.tab_conf.setValue('none', true);
            ns.dialog.close();
        },
        _btnonok_onclick: function (profile, e, src, value) {
            var ns = this,
                prop = ns.properties,
                items = ns.tb_actions.getItems(),
                evs = [],
                tmp = [];
            ns._tagVar = null;
            if (ns._dirty) {
                // collect string/function first
                if (prop.actionConf) {
                    if (ood.isFun(prop.actionConf) || ood.isStr(prop.actionConf)) {
                        tmp.push(prop.actionConf);
                    } else {
                        var arr = ood.isArr(prop.actionConf) ? prop.actionConf : prop.actionConf.actions;
                        ood.arr.each(arr, function (o, i) {
                            if (ood.isStr(o) || ood.isFun(o)) {
                                tmp.push(o);
                            }
                        });
                    }
                }
                if (items.length) {
                    ood.arr.each(items, function (o) {
                        if (o.tagVar && !o.tagVar._ignore &&
                            (("return" in o.tagVar) || (o.tagVar.target != "none" && o.tagVar.method != "none"))
                        ) {
                            var t = o.tagVar;
                            if (t.type == "other" && t.target == "var" && (!t.args || !t.args[0])) return;
                            if (t.type == "control" && t.method == "setProperties" && (!t.args || (!t.args[0] && !t.args[1]) || (ood.isEmpty(t.args[0]) && ood.isEmpty(t.args[1])))) return;

                            // save expression first
                            if (t.type == "control" && t.method == "setProperties") {
                                ood.filter(t.args[0], function (i) {
                                    return !t.args[1][i];
                                });
                            }

                            var cons = t.conditions;
                            t.conditions = [];
                            ood.arr.each(cons, function (v) {
                                if (!v) return;
                                if (v.symbol == "defined" || v.symbol == "undefined"
                                    || v.symbol == "exists" || v.symbol == "non-exists"
                                    || v.symbol == "empty" || v.symbol == "non-empty") {
                                    if (v.left) {
                                        t.conditions.push(v);
                                    }
                                } else {
                                    if (v.left && v.right) {
                                        t.conditions.push(v);
                                    }
                                }
                            });
                            if (!t.conditions.length) delete t.conditions;
                            t.desc = o.caption;

                            if (t.target.indexOf(".") == -1) {
                                if (t.redirection && ood.str.startWith('otherModuleCall', t.redirection)) {
                                    delete t.redirection;
                                }
                                delete t.className;
                                if (t.type == 'page' || t.type == 'otherModuleCall') {
                                    t.type = 'control';
                                }

                            } else if (t.className && t.className != SPA.getCurrentClassName()) {
                                var tagter = t.target;

                                if (tagter != t.className) {
                                    var itagter = tagter.split(".")[tagter.split(".").length - 1]
                                    tagter = t.className + "##" + itagter;
                                    t.redirection = "otherModuleCall:" + tagter + ":" + t.method;
                                    t.type = "otherModuleCall"
                                } else {
                                    t.type = 'page';
                                }

                            } else if (t && ood.str.startWith("otherModuleCall:")) {
                                delete t.redirection;
                            }
                            evs.push(t);
                        }
                    });
                }

                var eventPos = -1;
                if (prop.argsvar && evs.length) {
                    eventPos = ood.arr.indexOf(prop.argsvar, "e");
                    if (-1 != eventPos) evs[0].event = eventPos;
                    else delete evs[0].event;
                }
                ood.arr.insertAny(evs, tmp, -1);

                var evsConf = {}, t = ns.ood_returns.getUIValue();
                if (t) evsConf['return'] = t;
                if (evs.length) {
                    if (t) evsConf.actions = evs;
                    else evsConf = evs;
                }

                ns.fireEvent('onDirty', [prop.target, prop._eventkey, ood.isEmpty(evsConf) ? null : evsConf]);
            }
            ns.markDirty(0);
            ns._targetshadow = null;

            ns.tab_conf.setValue('none', true);
            ns.dialog.close();
        },
        _tv_var_onclick: function (profile, item) {
            if (profile.boxing().getUIValue() == item.id) {
                this._tv_var_onchange(profile, '', item.id, false, 'click');
            }
        },
        _tv_var_onchange: function (profile, oldValue, newValue, a, from) {
            if (!newValue || oldValue == newValue) return;
            var ns = this, uictrl = profile.boxing(),
                target = uictrl.getTag(),
                item = profile.getItemByItemId(newValue);

            if (item.realValue) newValue = item.realValue;

            if (target && (target = target.boxing())) {
                var vtype = newValue;
                if (vtype == "hash" || vtype == "array" || vtype == "function") {
                    var label = "#" + ood.adjustRes(item.caption) + "#",
                        imageClass = vtype == "function" ? 'ri-function-line' : vtype == "array" ? 'ri-braces-line' : ' ',
                        inicode = vtype == "function" ? 'function(){\n//code here\n}' : vtype == "array" ? '[\n//\n]' : '{\n//\n}';

                    ns._editMixData(target, vtype, inicode, label, imageClass);

                    ood().setBlurTrigger("-action-float-inoodb", true);
                    return;
                }

                target.setUIValue(newValue);

                if (from == "click") {
                    ood.asyRun(function () {
                        var ip = target.getSubNode("INPUT"), pos, pos1 = 0, pos2 = 0;
                        if (newValue == "{}") pos1 = pos2 = 1;
                        else if (/\(\'[^']*\'\)/.test(newValue)) {
                            pos1 = newValue.indexOf("\'") + 1;
                            pos2 = newValue.lastIndexOf("\'");
                        } else if (/\(\"[^"]*\"\)/.test(newValue)) {
                            pos1 = newValue.indexOf("\"") + 1;
                            pos2 = newValue.lastIndexOf("\"");
                        } else {
                            pos = newValue.lastIndexOf("}");
                            if (pos != -1) pos1 = pos2 = pos;
                        }
                        // stop the default focus event handler
                        target.get(0).$ignoreFocus = 1;
                        ip.caret(pos1, pos2);
                        delete target.get(0).$ignoreFocus;
                    });
                }
            }
        },
        _getFunCap: function (str) {
            var arr = [];
            if (ood.isHash(str)) {
                doc = str;
            } else {
                doc = RAD.EditorTool.getDoc(str, false, true);
            }
            if (doc) {
                if (doc.$paras && doc.$paras.length) {
                    ood.arr.each(doc.$paras, function (o) {
                        if (o = /([\w]+)[^:]+:\s*([\w/.]+[\w])/.exec(o)) {
                            arr.push(o[1] + '<span style="color:#888">/*' + o[2] + '*/</span>');
                        } else {
                            arr.push("?")
                        }
                    });
                }
                return "(" + arr.join(', ') + " ) " + (doc.$desc ? (": " + doc.$desc) : "");
            } else {
                return "( )";
            }
        },
        _tiggerNavTreeCur: function () {
        },
        _autoNavTree: function (type, value, noui) {
            var ns = this,
                tv = type == "callback" || type == "adjustdata" ? ns.tv_fun : ns.tv_var,
                item, info = [];
            var adjustId = function (str) {
                var arr = (str || "").split("(");
                if (arr.length == 2) {
                    return arr[0] + "()}";
                }
                return str;
            };
            delete ns._potentialStep;

            value = ood.str.trim(value || "");
            if (value.charAt(0) == "{") {
                //var arr=value.replace(".n0.properties","\x01").replace(/[{}]/g,"").split("."),step,t="";
                var arr = value.replace(/[{}]/g, "").split("."), step, step2, t = "";
                for (var i = 0, l = arr.length; i < l; i++) {
                    //if(!arr[i])continue;
                    t += (t ? "." : "") + arr[i];//.replace("\x01",".n0.properties");
                    step = "{" + t + "}";
                    if (noui) {
                        var items = tv.getItems();
                        if (!items || !items[0]) {
                            if (type == "callback" || type == "adjustdata")
                                ns._buildVarTree2(type == "adjustdata");
                            else
                                ns._buildVarTree(ns.tv_var.get(0));
                        }
                        var rs = tv.get(0).queryItems(items, function (v, k) {
                            return v.id == step;
                        }, 1, 1), item;
                        if (rs && rs[0]) {
                            item = rs[0];
                            if (item.sub === true) {
                                if (type == "callback" || type == "adjustdata") {
                                    ns._tv_fun_ongetcontent(tv.get(0), item, function (r) {
                                        item.sub = r;
                                    });
                                } else {
                                    items.sub = ns._tv_var_ongetcontent(tv.get(0), item, function (r) {
                                        item.sub = r;
                                    });
                                }
                            }
                            if (item.id != "[key]")
                                info.push(ood.adjustRes(item.caption));
                        } else {
                            info.push(arr[i]);
                        }
                    } else {
                        if (item = tv.getItemByItemId(step)) {
                            // for native var
                            if (item.pid) {
                                var pi = tv.getItemByItemId(item.pid);
                                if (!item._checked) {
                                    tv.toggleNode(item.pid, true, false);
                                }
                            }

                            if (item.sub && !item._checked) {
                                tv.toggleNode(step, true, false);
                                if (i == l - 1 && !item.group) {
                                    if (adjustId(tv.getUIValue()) !== adjustId(step)) {
                                        tv.setValue(adjustId(ns._potentialStep = step), true);
                                    }
                                }
                            } else {
                                if (adjustId(tv.getUIValue()) !== adjustId(step)) {
                                    tv.setValue(adjustId(ns._potentialStep = step), true);
                                }
                            }
                            if (item.id != "[key]")
                                info.push(ood.adjustRes(item.caption));
                        } else {
                            info.push(arr[i]);
                            var ok, item;
                            step2 = step.replace(/\.([^.]+)\}$/, "}");
                            step = step.replace(/\}$/, "");

                            if (item = tv.getItemByItemId(step2)) {
                                if (item.sub) {
                                    ood.arr.each(item.sub, function (o) {
                                        if (ood.str.startWith(adjustId(o.id), adjustId(step))) {
                                            if (adjustId(tv.getUIValue()) !== adjustId(o.id)) {
                                                tv.setValue(adjustId(ns._potentialStep = o.id), true);
                                            }
                                            ok = 1;
                                        }
                                    });
                                }
                            }
                            if (!ok) {
                                if (adjustId(tv.getUIValue()) !== adjustId(step)) {
                                    var id = adjustId(step2), id2;
                                    // to the input one
                                    if (tv.getItemByItemId(id2 = id.replace("}", ".}"))) {
                                        tv.setValue(ns._potentialStep = id2, true);
                                        ok = 1;
                                    }
                                    if (tv.getItemByItemId(id2 = id.replace(".}", "}"))) {
                                        tv.setValue(ns._potentialStep = id2, true);
                                        ok = 1;
                                    }
                                    if (!ok) {
                                        // to first
                                        tv.setValue(ns._potentialStep = "{}", true);
                                        ok = 1;
                                    }
                                }
                                if (!ok) {
                                    if (adjustId(tv.getUIValue()) !== adjustId(step2)) {
                                        var id = adjustId(step2);
                                        // to the right one
                                        if (tv.getItemByItemId(id)) {
                                            tv.setValue(ns._potentialStep = id, true);
                                        } else {
                                            // to first
                                            tv.setValue(ns._potentialStep = "{}", true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (!noui) {
                    //tid="{}";
                    //if(tv.getUIValue()!==tid){
                    //    tv.setUIValue(tid, true);
                    //}
                }
            }
            return info;
        },
        _buildGlobalfuns: function () {
            var infuns = [], imgcls = 'ri-function-line';

            // SPA.curProjectConfig.$GlobalFunctions =>map to=> ood.$cache.functions
            if (ood.get(SPA, ["curProjectConfig", '$GlobalFunctions'])) {
                ood.each(SPA.curProjectConfig.$GlobalFunctions, function (o, i) {

                    if (i.charAt(0) == '$' || i.charAt(0) == '!') return;

                    var str = [];
                    ood.arr.each(o.params, function (o) {
                        str.push(o.id + (o.type ? ("<span style='color:#888'>/*" + o.type + "*/</span>") : ""));
                    });

                    infuns.push({
                        id: "{functions." + i + "}",
                        caption: i + "( " + str.join(", ") + " )" + (o.desc ? (" : " + o.desc) : ""),
                        imageClass: imgcls
                    });
                });
            }
            infuns.push({
                id: "{functions.}",
                caption: "$(RAD.action.fun.[key])",
                imageClass: imgcls
            });
            return infuns;
        },
        _buildConstantData: function (type) {
            var indata = [], imgcls = 'ri-book-open-line';

            // SPA.curProjectConfig.$ConstantData =>map to=> ood.$cache.data
            if (ood.get(SPA, ["curProjectConfig", '$ConstantData'])) {
                ood.each(SPA.curProjectConfig.$ConstantData, function (o, i) {
                    if (i.charAt(0) == '$' || i.charAt(0) == '!') return;
                    indata.push({
                        id: "{constant." + i + "}",
                        caption: i,
                        imageClass: imgcls
                    });
                });
            }
            indata.push({
                id: "{constant.}",
                caption: "$(RAD.action." + type + ".[key])",
                imageClass: imgcls
            });
            return indata;
        },
        _buildGlobalData: function (type) {
            var indata = [], imgcls = type == 'var' ? 'ri-book-open-line' : 'ri-function-line';

            // SPA.curProjectConfig.$GlobalData =>map to=> ood.$cache.data
            if (ood.get(SPA, ["curProjectConfig", '$GlobalData'])) {
                ood.each(SPA.curProjectConfig.$GlobalData, function (o, i) {
                    if (i.charAt(0) == '$' || i.charAt(0) == '!') return;
                    indata.push({
                        id: "{global." + i + "}",
                        caption: i,
                        imageClass: imgcls
                    });
                });
            }
            indata.push({
                id: "{global.}",
                caption: "$(RAD.action." + type + ".[key])",
                imageClass: imgcls
            });
            return indata;
        },
        _buildVarTree3: function (alias) {
            var ns = this,
                tv = ns.tv_fun,
                prop = ns.properties,
                controls = prop.controls,
                _ns = ns.getScope(),
                imageClassMan = 'ri-book-open-line',
                imageClassFun = 'ri-function-line',
                items = [{
                    id: "{}",
                    caption: "$(RAD.action.fun.Input Function)",
                    imageClass: imageClassMan
                }];


            ood.arr.each(['getProperties', 'triggerExcelFormulas'], function (i) {
                if (ns._ignoreFuns1[i]) return;
                if (i.charAt(0) == "_" || i.charAt(0) == "$") return;
                items.push({
                    id: "{page." + (alias ? alias + "." : "") + i + "()}",
                    caption: i + ns._getFunCap("ood.Module.prototype." + i),
                    imageClass: imageClassFun
                });
            });


            ood.arr.each(['getProperties', 'triggerExcelFormulas'], function (i) {
                if (ns._ignoreFuns1[i]) return;
                if (i.charAt(0) == "_" || i.charAt(0) == "$") return;
                items.push({
                    id: "{page." + (alias ? alias + "." : "") + i + "()}",
                    caption: i + ns._getFunCap("ood.Module.prototype." + i),
                    imageClass: imageClassFun
                });
            });
            if (alias) {
                items.push({type: 'split'});
                var getArts = function (fun, argsvar, argsdoc) {
                    var arr = (fun + "").split("("), str;
                    arr.shift();
                    str = arr.join('(');
                    str.replace(/\s*((\w)+)(\s*\/\*[^*]*\*+([^\/][^*]*\*+)*\/)?|(\s*\/\/[^\n]*)|(\)[\s\S]*)/g, function (o, a, b, c, d, e) {
                        if (o.charAt(0) != ')') {
                            e = c ? c.replace(/^\s*\/\*/, '').replace(/\s*\*\/\s*$/, '').replace(/\s*[\n\r]+\s*\*\s*/g, ' ').replace(/^\s*/, '') : '';
                            o = e.split(/[^\w\.]+/);
                            d = o[0];

                            argsvar.push({
                                ori: d || "Object",
                                type: (d || "Object") + ".prototype"
                            });
                            argsdoc.push(a + " : " + e);
                        }
                    });
                };
                var customFuns = ood.get(_ns, ['page', alias, 'Class', '$Functions']);
                if (customFuns) {
                    ood.each(customFuns, function (o, i) {
                        var argsvar = [], argsdoc = [];
                        getArts(o, argsvar, argsdoc);
                        items.push({
                            id: "{page." + alias + "." + i + "()}",
                            caption: i + ns._getFunCap({
                                $paras: argsdoc,
                                $desc: ""
                            }),
                            imageClass: imageClassFun
                        });
                    });
                }
            }
            tv.setItems(items).setUIValue("");

        },
        _buildVarTree2: function (adjustFun) {
            var ns = this,
                tv = ns.tv_fun,
                prop = ns.properties,
                controls = prop.controls,
                _ns = ns.getScope(),
                imageClassMan = 'ri-book-open-line',
                imageClassFun = 'ri-function-line',
                items = [{
                    id: "{}",
                    caption: "$(RAD.action.fun.Input Function)",
                    imageClass: imageClassMan
                }];


            var f1 = function (items) {
                ood.arr.each(items, function (o) {
                    var a = ood.copy(o);
                    a.id = "{page." + o.id + "}";
                    a.__ = o["0"].key;
                    a.group = true;
                    a.sub = true;
                    inpage.push(a);
                    if (o.sub) f1(o.sub);
                });
            }, f4 = function () {
                var itemsi = [];
                ood.arr.each(prop.args, function (o, i) {
                    if (prop.argsdoc && (o == "callback" || o == "callback" || /[\w\s]+\:\s*(Function)/.test(prop.argsdoc[i] || ""))) {
                        itemsi.push({
                            id: "{args[" + i + "]()}",
                            caption: prop.argsdoc && prop.argsdoc[i] || o,
                            imageClass: imageClassFun
                        });
                    }
                });
                if (itemsi.length) {
                    items.push({
                        id: "{args}",
                        caption: "$(RAD.action.fun.in Parameters)",
                        group: true,
                        sub: itemsi
                    });
                }
            };

            //inargs
            f4();

            var inargs = [], inpage = [], _ignore = {}, inmodule = [];
            f1(controls);


            // inpage
            ood.each(ood.Module.prototype, function (o, i) {
                if (ns._ignoreFuns1[i]) return;
                if (i.charAt(0) == "_" || i.charAt(0) == "$") return;
                if (typeof o == "function") {
                    if (!o.$oodclass$) {
                        _ignore[i] = 1;
                        inpage.push({
                            id: "{page." + i + "()}",
                            caption: i + ns._getFunCap("ood.Module.prototype." + i),
                            imageClass: imageClassFun
                        });
                    }
                }
            });
            if (ood.get(prop.parent, ["_cls", "Instance"])) {
                ood.each(prop.parent._cls.Instance, function (o, i) {
                    if (ns._ignoreFuns1[i]) return;
                    if (i.charAt(0) == "_" || i.charAt(0) == "$") return;
                    if (typeof o == "function") {
                        if (!o.$oodclass$ && !_ignore[i]) {
                            inpage.push({
                                id: "{page." + i + "()}",
                                caption: i + ns._getFunCap("ood.Module.prototype." + i),
                                imageClass: imageClassFun
                            });
                        }
                    }
                });
            }
            ood.arr.stableSort(inpage, function (x, y) {
                if (x.__) {
                    if (!y.__) return -1;
                    else return 0
                } else {
                    if (y.__) return 1;
                    else return x.id.toLowerCase() > y.id.toLowerCase() ? 1 : x.id.toLowerCase() < y.id.toLowerCase() ? -1 : 0;
                }
            });

            //inmodule
            if (ood.get(prop.parent, ["_cls", "Instance", 'functions'])) {
                ood.each(prop.parent._cls.Instance.functions, function (o, i) {
                    var str = [];
                    ood.arr.each(o.params, function (o) {
                        str.push(o.id + (o.type ? ("<span style='color:#888'>/*" + o.type + "*/</span>") : ""));
                    });

                    inmodule.push({
                        id: "{page.functions." + i + "}",
                        caption: i + "( " + str.join(", ") + " )" + (o.desc ? (" : " + o.desc) : ""),
                        imageClass: imageClassFun
                    });
                });
            }
            inmodule.push({
                id: "{functions.}",
                caption: "$(RAD.action.fun.[key])",
                imageClass: imageClassFun
            });
            Array.prototype.push.apply(items, [{
                id: "{page.functions}",
                caption: "$(RAD.action.fun.Module Function Collection)",
                group: true,
                initFold: false,
                sub: inmodule
            }, {
                id: "{functions}",
                caption: "$(RAD.action.fun.Global Function Collection)",
                group: true,
                sub: ns._buildGlobalfuns()
            }, {
                id: "{temp}",
                caption: "$(RAD.action.fun.in Temp Object)",
                group: true,
                sub: [
                    {
                        id: "{temp.}",
                        caption: "$(RAD.action.fun.[key])",
                        imageClass: imageClassMan
                    }
                ]
            }, {
                id: "{global}",
                caption: "$(RAD.action.fun.in Global Object)",
                group: true,
                sub: ns._buildGlobalData('fun')
            }]);

            if (ood.get(prop.parent, ["_cls"])) {
                items.push({
                    id: "{page}",
                    caption: "$(RAD.action.fun.in Current Page)",
                    group: true,
                    sub: inpage
                });
            }

            if (!adjustFun)
                items.push({
                    id: "{ood}",
                    caption: "$(RAD.action.fun.in ood Namespace)",
                    group: true,
                    sub: true
                });

            tv.setItems(items).setUIValue("");
        },
        _buildVarTree: function (tprf) {
            var ns = this,
                tv = ns.tv_var,
                map2 = ns.CAPMAP.action,
                alias = tprf.alias,
                mem = alias == "ctl_itemvar" && ns.add_target.getTagVar(),
                imageClassMan = 'ri-book-open-line',
                imageClassFun = 'ri-function-line',
                imageClassArr = 'ri-list-unordered',
                imageClassHash = 'ri-braces-line',
                imageClassVar = 'ri-text';

            var args = [],
                // maybe changed
                global = [], temp = [], pageprop = [], funs = [],
                pagectls = [{
                    id: "{page.getProfile()}",
                    caption: "$(RAD.designer.tool.Profile Map)",
                    tagCmds: [{buttonType: 'text', 'tagCmdsAlign': 'left', caption: '', itemClass: 'ri-question-line'}],
                    idn: "ood.Module.prototype.getProfile",
                    imageClass: imageClassHash
                }, {
                    id: "{page.getData()}",
                    caption: "$(RAD.designer.tool.Data Map)",
                    tagCmds: [{buttonType: 'text', 'tagCmdsAlign': 'left', caption: '', itemClass: 'ri-question-line'}],
                    idn: "ood.Module.prototype.getData",
                    imageClass: imageClassHash
                }, {
                    id: "{page.getValue()}",
                    caption: "$(RAD.designer.tool.Value Map)",
                    tagCmds: [{buttonType: 'text', 'tagCmdsAlign': 'left', caption: '', itemClass: 'ri-question-line'}],
                    idn: "ood.Module.prototype.getValue",
                    imageClass: imageClassHash
                }, {
                    id: "{page.properties}",
                    caption: "$(RAD.action.var.Page Properties)",
                    imageClass: imageClassHash,
                    tagCmds: [{buttonType: 'text', 'tagCmdsAlign': 'left', caption: '', itemClass: 'ri-question-line'}],
                    idn: "ood.Module.prototype.getProperties",
                    sub: pageprop
                }/*,{
                    id:"{page.functions}",
                    caption:"$(RAD.action.var.Page functions)",
                    imageClass:imageClassHash,
                    tagCmds:[{type:'text','location':'left',caption:'',itemClass:'ri-question-line'}],
                    idn:"ood.Module.prototype.getFunctions",
                    sub:funs
                }*/];

            var prop = ns.properties,
                target = prop.target,
                pp = ood.get(prop.cls, ["Instance", "properties"]) || {},
                //ff=ood.get(prop.cls,["Instance","functions"])||{},
                controls = prop.controls,
                f1 = function (items) {
                    ood.arr.each(items, function (o) {
                        var a = ood.copy(o);
                        a.id = "{page." + o.id + "}";
                        a.__ = o["0"].key;
                        a.sub = true;
                        pagectls.push(a);
                        if (o.sub) f1(o.sub);
                    });
                }, f2 = function (pp) {
                    for (var i  in pp) {
                        var t = pp[i];
                        pageprop.push({
                            id: "{page.properties." + i + "}",
                            caption: i,
                            imageClass: ood.isArr(t) ? imageClassArr : ood.isHash(t) ? imageClassHash : imageClassVar,
                            sub: (ood.isArr(t) || ood.isHash(t)) ? true : null
                        });
                    }
                    pageprop.push({
                        id: "{page.properties.}",
                        caption: "$(RAD.action.var.[key])",
                        imageClass: imageClassMan
                    });
                }, f3 = function (ff) {
                    for (var i  in ff) {
                        var str = [], o = ff[i];
                        ood.arr.each(o.params, function (o) {
                            str.push(o.id + (o.type ? ("<span style='color:#888'>/*" + o.type + "*/</span>") : ""));
                        });

                        funs.push({
                            id: "{page.functions." + i + "}",
                            caption: i + "( " + str.join(", ") + " )" + (o.desc ? (" : " + o.desc) : ""),
                            imageClass: imageClassFun
                        });

                    }
                    funs.push({
                        id: "{page.functions.}",
                        caption: "$(RAD.action.var.[key])",
                        imageClass: imageClassFun
                    });
                }, f4 = function () {
                    var argsvar = prop.argsvar || [], t;
                    ood.arr.each(prop.args, function (o, i) {
                        args.push({
                            id: "{args[" + i + "]}",
                            caption: prop.argsdoc && prop.argsdoc[i] || o,
                            imageClass: ood.isArr(t) ? imageClassArr : ood.isHash(t) ? imageClassHash : imageClassVar,
                            _paramName: argsvar[i] == "e" ? "keyboard" : null,
                            sub: argsvar[i] == "e" ? true :
                                // onlly for event handler
                                argsvar[i] == "curProfile" ? [{
                                        id: "{args[" + i + "].boxing()}",
                                        caption: "$(RAD.action.var.Current Control)",
                                        imageClass: imageClassHash,
                                        __: target ? target.key : null,
                                        referer: target ? "{page." + target.alias + "}" : "",
                                        sub: true
                                    }] :
                                    (ood.isArr(argsvar[i]) || ood.isHash(argsvar[i]) || ood.isObj(argsvar[i])) ? true :
                                        null
                        });
                    });
                };
            f1(controls);
            f2(pp);
            //f3(ff);
            f4();

            var items = [{
                id: "{}",
                caption: "$(RAD.action.var.Input Var)",
                imageClass: imageClassMan
            }, {
                id: "{getCookies()}",
                hidden: !!mem,
                realValue: "{getCookies(\"key\")}",
                caption: "$(RAD.action.var.Get Cookies)",
                imageClass: imageClassMan
            }, {
                id: "{getFI()}",
                hidden: !!mem,
                realValue: "{getFI(\"key\")}",
                caption: "$(RAD.action.var.Get Fragment Identifier)",
                imageClass: imageClassMan
            }, {
                id: "{native}",
                caption: "$(RAD.action.var.Native Var)",
                hidden: !!mem,
                imageClass: "ri-code-box-line",
                group: true,
                sub: [{
                    pid: "{native}",
                    id: "{true}",
                    caption: "true",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{false}",
                    caption: "false",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{NaN}",
                    caption: "NaN",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{null}",
                    caption: "null",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{undefined}",
                    caption: "undefined",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{}",
                    caption: "$(RAD.action.var.Empty String)",
                    imageClass: imageClassVar
                }, {
                    pid: "{native}",
                    id: "{-0.00}",
                    caption: "$(RAD.action.var.Number)",
                    imageClass: imageClassVar
                }]
            }, {
                id: "{args}",
                caption: "$(RAD.action.var.Parameters)",
                group: true,
                initFold: false,
                imageClass: "ri-arrow-right-circle-line",
                tagCmds: [{buttonType: 'text', 'tagCmdsAlign': 'left', caption: '', itemClass: 'ri-question-line'}],
                idn: target ? target.key + ".prototype." + prop._eventkey : "",
                sub: args
            }, {
                id: "{temp}",
                caption: "$(RAD.action.var.Temp)",
                imageClass: "ri-time-line",
                group: true,
                sub: [{
                    id: "{temp.okData}",
                    caption: "$(RAD.action.var.Returned data)",
                    imageClass: imageClassVar
                }, {
                    id: "{temp.koData}",
                    caption: "$(RAD.action.var.Failure message)",
                    imageClass: imageClassVar
                },
                    {type: 'split'},
                    {
                        id: "{temp._confirm_yes}",
                        caption: "$(RAD.action.var.Select [yes] in confirm dialog)",
                        imageClass: imageClassVar
                    },
                    {
                        id: "{temp._confirm_no}",
                        caption: "$(RAD.action.var.Select [no] in confirm dialog)",
                        imageClass: imageClassVar
                    },
                    {type: 'split'},
                    {
                        id: "{temp._prompt_ok}",
                        caption: "$(RAD.action.var.Select [ok] in prompt dialog)",
                        imageClass: imageClassVar
                    },
                    {
                        id: "{temp._prompt_cancel}",
                        caption: "$(RAD.action.var.Select [cancel] in prompt dialog)",
                        imageClass: imageClassVar
                    },
                    {type: 'split'},
                    {
                        id: "{temp._DI_succeed}",
                        caption: "$(RAD.action.var.Data interaction succeeded)",
                        imageClass: imageClassVar
                    },
                    {
                        id: "{temp._DI_fail}",
                        caption: "$(RAD.action.var.Data interaction failed)",
                        imageClass: imageClassVar
                    },
                    {type: 'split'},
                    {
                        id: "{temp.}",
                        caption: "$(RAD.action.var.[key])",
                        imageClass: imageClassMan
                    }]
            }, {
                id: "{global}",
                caption: "$(RAD.action.var.Global)",
                imageClass: "ri-globe-line",
                group: true,
                sub: ns._buildGlobalData('var')
            }, {
                id: "{constant}",
                imageClass: "ri-check-double-line",
                caption: "$(RAD.action.var.Constant)",
                group: true,
                sub: ns._buildConstantData('var')
            }];
            if (ood.get(prop.parent, ["_cls"])) {
                items.push({
                    id: "{page}",
                    caption: "$(RAD.action.Current Page)",
                    imageClass: "ri-file-list-line",
                    sub: pagectls
                });
            }
            if (tprf.properties.tag == "purevar") {
                var mem = ns.add_target.getTagVar(),
                    iitems = [];
                // not for items /insert case
                if (!(mem && mem.insert)) {
                    iitems.push({
                        id: "hash",
                        caption: "$(RAD.action.var.Hash Var)",
                        imageClass: imageClassHash
                    });
                }
                // not for items /update case
                if (!(mem && (mem.update || mem.specialUpdate))) {
                    iitems.push({
                        id: "array",
                        caption: "$(RAD.action.var.Array Var)",
                        imageClass: imageClassArr
                    });
                }
                // not for items case
                if (!mem) {
                    iitems.push({
                        id: "function",
                        caption: "$(RAD.action.var.Function Var)",
                        imageClass: imageClassFun
                    });
                }

                ood.arr.insertAny(items, iitems, 1);
            }
            tv.setItems(items).setUIValue("");
        },
        _log_v5_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[4] = newValue;
                ns.markDirty(1);
            }
        },
        _log_v4_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[3] = newValue;
                ns.markDirty(1);
            }
        },
        _log_v3_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[2] = newValue;
                ns.markDirty(1);
            }
        },
        _log_v2_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[1] = newValue;
                ns.markDirty(1);
            }
        },
        _log_v1_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[0] = newValue;
                ns.markDirty(1);
            }
        },
        _i_rtn_onchange: function (profile, oldValue, newValue) {
            if (oldValue == newValue) return;
            this._dirty = true;
        },
        _i_conl_onchange: function (profile, oldValue, newValue, force, tag) {
            if (oldValue == newValue) return;
            var ns = this, tagVar = ns._tagVar, tag = profile.properties.tag;

            var tagVar = ns._tagVar;
            if (tagVar) {
                if (!tagVar.conditions) tagVar.conditions = [];
                if (!tagVar.conditions[tag - 1])
                    tagVar.conditions[tag - 1] = {
                        left: "",
                        symbol: "defined",
                        right: ""
                    };
                var con = tagVar.conditions[tag - 1];
                con.left = newValue;
                ns.markDirty(1);
            }
        },
        _i_conr_onchange: function (profile, oldValue, newValue, force, tag) {
            if (oldValue == newValue) return;
            var ns = this, tagVar = ns._tagVar, tag = profile.properties.tag;

            var tagVar = ns._tagVar;
            if (tagVar) {
                if (!tagVar.conditions) tagVar.conditions = [];
                if (!tagVar.conditions[tag - 1])
                    tagVar.conditions[tag - 1] = {
                        left: "",
                        symbol: "defined",
                        right: ""
                    };
                var con = tagVar.conditions[tag - 1];
                con.right = newValue;
                ns.markDirty(1);
            }
        },
        _ctl_adjust_onchange: function (profile, oldValue, newValue, force, tag) {
            if (oldValue == newValue) return;
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.adjust = newValue;
            }
            // do Map UI and Function
        },
        _ctl_varto_onchange: function (profile, oldValue, newValue, force, tag) {
            if (oldValue == newValue) return;
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[0] = newValue;

                var s = "${" + tagVar.method + "." + (newValue || "?") + "} = ";
                ns.conf_var_left.setHtml(s);

                ns.markDirty(1);
            }
        },
        _ctl_varfrom_cmd: function (profile) {
            profile.boxing().setTagVar(null, true);
            profile.boxing().setCommandBtn('none', true).setInputReadonly(false, true).setUIValue("", true);
            ood.asyRun(function () {
                profile.boxing().activate();
            });
        },
        _ctl_varfrompath_cmd: function (profile) {
            profile.boxing().setTagVar(null, true);
            profile.boxing().setCommandBtn('none', true).setInputReadonly(false, true).setUIValue("", true);
            ood.asyRun(function () {
                profile.boxing().activate();
            });
        },
        _ctl_animate_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[0] = profile.properties.tagVar || newValue;
                ns.markDirty(1);
                if (ood.isStr(tagVar.args[0]))
                    profile.boxing().animate(tagVar.args[0]);
            }
        },
        _ctl_itemvar_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, prop = ns.properties,
                tagVar = ns._tagVar,
                mem = ns.add_target.getTagVar();
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                if (mem.insert || mem.specialUpdate) {
                    tagVar.args[0] = profile.properties.tagVar || newValue;
                } else {
                    tagVar.args[1] = profile.properties.tagVar || newValue;
                }
                ns.markDirty(1);
            }
        },
        _ctl_varfrom_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[1] = profile.properties.tagVar || newValue;
                ns.markDirty(1);
            }
        },
        _ctl_varfrompath_onchange: function (profile, oldValue, newValue, force, tag) {
            var ns = this, tagVar = ns._tagVar;
            if (tagVar) {
                tagVar.args = tagVar.args || [];
                tagVar.args[2] = profile.properties.tagVar || newValue;
                ns.markDirty(1);
            }
        },
        _tv_var_onsel: function (profile, item) {
            if (item.sub && !item._checked) {
                profile.boxing().toggleNode(item.id, true, false);
            }
        },
        getScope: function (type) {
            var ns = this,
                prop = ns.properties,
                page = {};
            return {
                "ood": ood,
                "page": ood.get(prop.parent, ["_cls"]) ? ood.merge(ood.merge(ood.merge(page, prop.parent._cls.Instance, 'without'), prop.parent.$host, 'without'), ood.Module.prototype, 'without') : {},
                "prop": ood.get(prop.cls, ["Instance", "properties"]) || {},
                "args": prop.argsvar || [],
                "functions": SPA.curProjectConfig && SPA.curProjectConfig.$GlobalFunctions,//ood.$cache.functions,
                "constant": SPA.curProjectConfig && SPA.curProjectConfig.$ConstantData,//ood.constant
                "global": SPA.curProjectConfig && SPA.curProjectConfig.$GlobalData//ood.$cache.data
            };
        },
        _ignoreFuns1: {
            "upper": 1,
            "Before": 1,
            "After": 1,
            "Constructor": 1,
            "constructor": 1,
            "Initialize": 1
        },
        _ignoreFuns2: {
            addTemplateKeys: 1,
            getTemplateKeys: 1,
            buildCSSText: 1,
            cacheData: 1,
            getCachedData: 1,
            setAppearance: 1,
            getAppearance: 1,
            getTemplate: 1,
            setTemplate: 1,
            pickAlias: 1,
            plugIn: 1,
            getBehavior: 1,
            setBehavior: 1,
            setDataModel: 1,
            setEventHandlers: 1,
            pickAlias: 1,
            pack: 1,
            plugIn: 1
        },
        _tv_fun_opt: function (profile, item) {
            if (item.idn) {
                CONF.openAPI(item.idn);
            }
        },
        _tv_fun_ongetcontent: function (profile, item, callback) {
            var ns = this,
                tv = ns.tv_var,
                imageClassMan = 'ri-book-open-line',
                imageClassFun = 'ri-function-line',
                imageClassArr = 'ri-list-unordered',
                imageClassHash = 'ri-braces-line',
                imageClassVar = 'ri-text',
                id = item.id,
                iid = id.replace(/[{}]/g, ""),
                arr = /^\s*\{([\w]+)/.exec(id),
                key = arr && arr[1],
                items = [];

            if (!key) callback(items);

            switch (key) {
                case "ood":
                    ood.each(ood.SC(iid), function (o, i) {
                        var idn = iid + "." + i;

                        if (idn == "ood.Locale" || idn == "ood.ini" || idn == "ood.browser" || idn == "ood.pseudocode") return;
                        if (ns._ignoreFuns1[i]) return;
                        if (o && o.$abstract) return;
                        if (ns._ignoreFuns2[i] && (idn.indexOf("ood.CSSBox") === 0 || idn.indexOf("ood.Timer") === 0 || idn.indexOf("ood.AnimBinder") === 0 || idn.indexOf("ood.MessageService") === 0 || idn.indexOf("ood.DataBinder") === 0 || idn.indexOf("ood.APICaller") === 0 || idn.indexOf("ood.UI") === 0 || idn.indexOf("ood.svg") === 0)) return;
                        if (i.charAt(0) == "_" || i.charAt(0) == "$") return;

                        if (typeof o == "function") {
                            var info = ood.get(RAD.EditorTool.getDoc(idn, false, true), "$desc") || "";
                            if (o.$oodclass$) {
                                items.push({
                                    id: "{" + idn + "}",
                                    caption: i + (info ? (" : " + info) : ""),
                                    tagCmds: [{
                                        buttonType: 'text',
                                        'tagCmdsAlign': 'left',
                                        caption: '',
                                        itemClass: 'ri-question-line'
                                    }],
                                    idn: idn,
                                    group: true,
                                    sub: true
                                });
                            } else {
                                //    if(o.$original$ && o.$original$!=iid)return;
                                items.push({
                                    id: "{" + idn + "()}",
                                    caption: i + ns._getFunCap(idn),
                                    tagCmds: [{
                                        buttonType: 'text',
                                        'tagCmdsAlign': 'left',
                                        caption: '',
                                        itemClass: 'ri-question-line'
                                    }],
                                    idn: idn,
                                    imageClass: imageClassFun
                                });
                            }
                        } else if (ood.isHash(o)) {
                            items.push({
                                id: "{" + idn + "}",
                                tagCmds: [{
                                    buttonType: 'text',
                                    'tagCmdsAlign': 'left',
                                    caption: '',
                                    itemClass: 'ri-question-line'
                                }],
                                idn: idn,
                                caption: i,
                                group: true,
                                sub: true
                            });
                        }
                    });
                    ood.arr.stableSort(items, function (x, y) {
                        if (x.group) {
                            if (!y.group) return 1;
                            else return x.id.toLowerCase() > y.id.toLowerCase() ? 1 : x.id.toLowerCase() < y.id.toLowerCase() ? -1 : 0;
                        } else {
                            if (y.group) return -1;
                            else return x.id.toLowerCase() > y.id.toLowerCase() ? 1 : x.id.toLowerCase() < y.id.toLowerCase() ? -1 : 0;
                        }
                    });
                    break;
                default:
                    if (item.__) {
                        ood.each(item["0"].boxing(), function (o, i) {
                            var idn = item.__ + ".prototype." + i;
                            if (i.charAt(0) == "_" || i.charAt(0) == "$") return;
                            if (typeof o == "function") {
                                if (i == "toHtml" || i == "size" || i == "get" || i == "merge" || i == "each" || i == "host" || i == "render" || i == "renderOnto") return;
                                if (o.$event$) return;
                                items.push({
                                    id: "{" + iid + "." + i + "()}",
                                    caption: i + ns._getFunCap(idn),
                                    tagCmds: [{
                                        buttonType: 'text',
                                        'tagCmdsAlign': 'left',
                                        caption: '',
                                        itemClass: 'ri-question-line'
                                    }],
                                    idn: idn,
                                    imageClass: imageClassFun
                                });
                            }
                        });
                    }
            }
            ood.arr.stableSort(items, function (x, y) {
                if (x.group) {
                    if (!y.group) return -1;
                    else return x.id.toLowerCase() > y.id.toLowerCase() ? 1 : x.id.toLowerCase() < y.id.toLowerCase() ? -1 : 0;
                } else {
                    if (y.group) return 1;
                    return x.id.toLowerCase() > y.id.toLowerCase() ? 1 : x.id.toLowerCase() < y.id.toLowerCase() ? -1 : 0;
                }
            });
            callback(items);
        },
        _tv_var_ongetcontent: function (profile, item, callback) {
            var ns = this,
                prop = ns.properties,
                argsdoc = prop.argsdoc,
                args = prop.args,
                tv = ns.tv_var,
                imageClassMan = 'ri-book-open-line',
                imageClassArr = 'ri-list-unordered',
                imageClassHash = 'ri-braces-line',
                imageClassVar = 'ri-text',
                items = [];
            var scope = ns.getScope();

            var v = ood.adjustVar(item.referer || item.id, scope), t;
            if (item._paramName == "keyboard") {
                items = [{
                    id: "{" + item.id.replace(/[{}]/g, "") + ".key}",
                    caption: "$(RAD.action.var.Keyboard Code)",
                    imageClass: imageClassVar
                }, {
                    id: "{" + item.id.replace(/[{}]/g, "") + ".ctrlKey}",
                    caption: "$(RAD.action.var.Ctrl Key)",
                    imageClass: imageClassVar
                }, {
                    id: "{" + item.id.replace(/[{}]/g, "") + ".shiftKey}",
                    caption: "$(RAD.action.var.Shift key)",
                    imageClass: imageClassVar
                }, {
                    id: "{" + item.id.replace(/[{}]/g, "") + ".altKey}",
                    caption: "$(RAD.action.var.Alt Key)",
                    imageClass: imageClassVar
                }];
                if (item._paramName == "keyboard") {
                    Array.prototype.unshift.apply(items, [{
                        id: "{" + item.id.replace(/[{}]/g, "") + ".button}",
                        caption: "$(RAD.action.var.Mouse Button)",
                        imageClass: imageClassVar
                    }, {
                        id: "{" + item.id.replace(/[{}]/g, "") + ".pageX}",
                        caption: "$(RAD.action.var.Cursor PageX)",
                        imageClass: imageClassVar
                    }, {
                        id: "{" + item.id.replace(/[{}]/g, "") + ".pageY}",
                        caption: "$(RAD.action.var.Cursor PageY)",
                        imageClass: imageClassVar
                    }]);
                }
            } else if (ood.isArr(v)) {
                for (var i = 0, l = v.length; i < l; i++) {
                    t = v[i];
                    items.push({
                        id: "{" + item.id.replace(/[{}]/g, "") + "." + i + "}",
                        caption: (ood.isArr(v[i]) || ood.isHash(v[i])) ? i : v[i],
                        imageClass: ood.isArr(t) ? imageClassArr : ood.isHash(t) ? imageClassHash : imageClassVar,
                        sub: (ood.isArr(t) || ood.isHash(t)) ? true : null
                    });
                }
                items.push({
                    id: "{" + item.id.replace(/[{}]/g, "") + ".length}",
                    caption: "$(RAD.action.var.Array Length)",
                    imageClass: imageClassMan
                });
            } else if (ood.isHash(v)) {
                for (var i in v) {
                    t = v[i];
                    items.push({
                        id: "{" + item.id.replace(/[{}]/g, "") + "." + i + "}",
                        caption: item.id.indexOf("{args[") === 0 ? v[i] : i,
                        imageClass: ood.isArr(t) ? imageClassArr : ood.isHash(t) ? imageClassHash : imageClassVar,
                        sub: (ood.isArr(t) || ood.isHash(t)) ? true : null
                    });
                }
                items.push({
                    id: "{" + item.id.replace(/[{}]/g, "") + ".}",
                    caption: "$(RAD.action.var.[key])",
                    imageClass: imageClassMan
                });
            } else if (item.__) {
                var pp = v, special = {
                        getAlias: true,
                        getProperties: true,
                        serialize: true,

                        isDestroyed: true,
                        busy: true,
                        free: true,
                        reLayout: true,
                        hide: true,
                        show: true,
                        refresh: true,
                        adjustDock: true,

                        isDirtied: true,
                        checkValid: true,
                        checkRequired: true,
                        getUIValue: true,
                        getData: true,
                        setData: true,
                        resetValue: true,
                        clearValue: true,
                        updateValue: true,
                        updateDataFromUI: true,
                        updateDataToUI: true,

                        play: true,
                        pause: true
                    },
                    getName = function (i) {
                        return special[i] ? i : "get" + ood.str.initial(i);
                    };
                v = v.get(0).properties;

                v = ood.copy(v);
                if (pp.Class['ood.absObj']) {
                    ood.merge(v, {
                        getAlias: true,
                        getProperties: true,
                        selectedItem: true,
                        getCaptionValue: true,
                        serialize: true
                    });
                }
                if (pp.Class['ood.UI']) {
                    ood.merge(v, {
                        isDestroyed: true,
                        busy: true,
                        free: true,
                        reLayout: true,
                        hide: true,
                        show: true,
                        refresh: true,
                        adjustDock: true
                    });
                }

                // add extra prop
                if (pp.Class["ood.DataBinder"]) {
                    ood.merge(v, {
                        isDirtied: true,
                        checkValid: true,
                        checkRequired: true,
                        getUIValue: true,
                        getData: true,
                        setData: true,

                        resetValue: true,
                        clearValue: true,
                        updateValue: true,
                        updateDataFromUI: true,
                        updateDataToUI: true
                    });
                } else if (pp.Class["ood.UI.Audio"]) {
                    ood.merge(v, {
                        play: true,
                        pause: true
                    });
                } else if (pp.Class["ood.UI.FusionChartsXT"]) {
                    ood.merge(v, {
                        ChartAttribute: {},
                        updateData: true,
                        setTheme: true,
                        fillData: true,
                        refreshChart: true
                    });
                } else if (pp.Class["ood.absContainer"]) {
                    ood.merge(v, {
                        formValues: {},
                        addPanel: true,
                        removePanel: true,
                        getPanelPara: true,
                        dumpContainer: true,
                        getPanelChildren: true,
                        getFormValues: true,
                        setFormValues: true,
                        getFormElements: true,
                        isDirtied: true,
                        checkValid: true,
                        checkRequired: true,
                        formClear: true,
                        formReset: true,
                        updateFormValues: true,
                        formSubmit: true
                    });
                } else if (pp.Class["ood.UI.TreeGrid"]) {
                    ood.merge(v, {
                        isDirtied: true,
                        sortColumn: true,
                        showColumn: true,

                        getActiveRow: true,
                        setActiveRow: true,
                        updateRow: true,
                        autoRowHeight: true,

                        getCellbyRowCol: true,
                        editCellbyRowCol: true,
                        focusCellbyRowCol: true,
                        getActiveCell: true,
                        setActiveCell: true,
                        updateCellByRowCol2: true,

                        getEditor: true,
                        updateEditor: true,
                        getEditCell: true,
                        offEditor: true
                    });
                }

                for (var i in v) {
                    if (i.charAt(0) == "$" || i.charAt(0) == "_") continue;
                    t = v[i];
                    var idn = item.__ + ".prototype." + getName(i);

                    var item1 = {
                        id: "{" + item.id.replace(/[{}]/g, "") + "." + getName(i) + "()}",
                        caption: getName(i) + ns._getFunCap(idn),
                        tagCmds: [{
                            buttonType: 'text',
                            'tagCmdsAlign': 'left',
                            caption: '',
                            itemClass: 'ri-question-line'
                        }],
                        idn: idn,
                        imageClass: ood.isArr(t) ? imageClassArr : ood.isHash(t) ? imageClassHash : imageClassVar,
                        sub: (ood.isArr(t) || ood.isHash(t)) ? true : null
                    };
                    if (item.referer)
                        item1.referer = "{" + item.referer.replace(/[{}]/g, "") + "." + getName(i) + "()}";
                    items.push(item1);
                }
                ood.arr.stableSort(items, function (x, y) {
                    return x.id > y.id ? 1 : x.id == y.id ? 0 : -1;
                });
            }
            return items;
        },
        _tb_actions_ondblclick: function (profile, item, e, src) {
            profile.boxing().editItem(item.id);
        },
        events: {"onReady": "_onready"},
        _onready: function () {
            var inputs = this.getAllComponents().reBoxing('ood.UI.Input');
            ood.filter(inputs._nodes, function (o) {
                if (o.alias == 'propFilter') return false;
                if (o.alias == 'ctl_animate') return false;
            });

            this._enhanceInputs(inputs);
        },
        _editMixData: function (target, vtype, inicode, label, imageClass, editorPrf) {
            var ns = this,
                mem = ns.add_target.getTagVar();
            // for those item setting
            if (target.getAlias() == "ctl_itemvar" && mem.keyN) {
                var prop = ns.properties,
                    parent = prop.parent,
                    src = target.getRoot().xid(),
                    tagVar = ns._tagVar,
                    targetCtrl = parent.getByAlias(tagVar.target, true);
                if (mem.insert) {
                    var obj = ood.get(tagVar, ["args", 0]) || [];
                    parent._showMixedEditor(targetCtrl, targetCtrl.key, "tagVar", mem.keyN, obj, src, function (data) {
                        var tagVar = ns._tagVar;
                        if (tagVar) {
                            tagVar.args = tagVar.args || [];
                            if (data && data.length) {
                                delete tagVar._ignore;
                            } else {
                                //if empty, ignore it
                                tagVar._ignore = 1;
                            }

                            // first
                            target.setTagVar(data, true);
                            target.setInputReadonly(true).setCommandBtn("delete");
                            target.setUIValue(label, true);
                        }
                    });
                } else {
                    var obj = ood.get(tagVar, ["args", 1]) || {};
                    parent._showEditor(targetCtrl, targetCtrl.key, mem.keyN, obj, src, function (data) {
                        var tagVar = ns._tagVar;
                        if (tagVar) {
                            tagVar.args = tagVar.args || [];
                            if (data && !ood.isEmpty(data)) {
                                delete tagVar._ignore;
                            } else {
                                //if empty, ignore it
                                tagVar._ignore = 1;
                            }

                            // first
                            target.setTagVar(data, true);
                            target.setInputReadonly(true).setCommandBtn("delete");
                            target.setUIValue(label, true);
                        }
                    }, tagVar.method == "updateCellByRowCol2" ?
                        CONF.widgets_itemsProp.rows["ood.UI.TreeGrid"].subheader :
                        null);
                }
            }
            else {
                var text = ood.Coder.formatText(inicode),
                    type = vtype == "function" ? 'funEditor' : 'jsonEditor',
                    _cb = function (code) {
                        var o = vtype == "function" ? ood.unserialize("function(" + ood.fun.args(code).join(", ") + "){" + ood.fun.body(code) + "}") : ood.unserialize(code);
                        // first
                        target.setTagVar(o, true);
                        target.setInputReadonly(true).setCommandBtn("delete");
                        target.setUIValue(label, true);
                    };
                if (window.SPA && false === SPA.fe("beforeObjectEditorPop", [
                    type, null, text, _cb,
                    "RAD.ActionsEditor", target, editorPrf && editorPrf.getSubNode('BOX').get(0), null, null, editorPrf && editorPrf.boxing()])) return;

                ood.getModule(type, function () {
                    this.setProperties({
                        caption: label,
                        imageClass: imageClass,
                        text: text,
                        onOK: function (obj) {
                            _cb(obj.properties.result.code);
                        }
                    }, true);
                    this.show(function (tid, module) {
                        //if(module.activate)module.activate();
                    });
                });
            }
        },
        iniPureValue: function (ctl, value) {
            if (ood.isObj(value)) {
                ctl.setTagVar(value);
                var label = label = "#" + ood.adjustRes("$(RAD.action.var." +
                    (typeof value == "function" ? "Function" : ood.isArr(value) ? "Array" : "Hash") +
                    " Var)") + "#";
                ctl.setInputReadonly(true).setCommandBtn("delete");
                ctl.setValue(label, true);
            } else {
                ctl.setTagVar(null);
                ctl.setInputReadonly(false, true).setCommandBtn("none", true);
                ctl.setValue(value, true);
            }
        },
        _jsonedit: function (col, input) {
            if (col == 'value') this._enhanceInputs(input, true);
        },
        _enhanceInputs: function (inputs, noDyncheck) {
            var ns = this,
                _check = function (profile) {
                    var type = profile.properties.tagVar;
                    if (profile.properties.multiLines) return false;
                    if (profile.properties.inputReadonly) return 0;
                    if (type == "none") return false;
                    switch (profile.properties.type) {
                        case 'file':
                        case 'spin':
                        case 'currency':
                        case 'number':
                        case 'listbox':
                            return false;
                    }
                    ;
                    return true;
                },
                _onchange = function (profile, oldValue, newValue, force, tag) {
                    if (!_check(profile)) return;
                    var type = profile.properties.tagVar;
                    if (oldValue != newValue) {
                        ns._autoNavTree(type, newValue);
                    }
                },
                _onTips = function (profile, node, pos) {
                    if (!_check(profile)) return true;
                    var type = profile.properties.tagVar;
                    var uictrl = profile.boxing(), v = uictrl.getUIValue();
                    if (v && /^\s*\{([\S]+)\}\s*$/.test(v)) {

                        var arr = ns._autoNavTree(type, v, true),
                            info = ood.adjustRes("$(RAD.action.Var Path is)") + "&nbsp;<b>:</b>&nbsp;<span style='text-decoration: underline;'>" + arr.join("</span>&nbsp;<b>&gt;</b>&nbsp;<span style='text-decoration: underline;'>") + "</span>";

                        ood.Tips.show(pos, "<div style='line-height:20px;'>" + info + "</div>");
                    } else if (v) ood.Tips.show(pos, v);
                    return false;
                },
                _onfocus = function (profile) {
                    if (profile.properties.disabled || profile.properties.readonly) return false;

                    var c = _check(profile);
                    if (c === 0) {
                        var inicode = profile.properties.tagVar,
                            alias = profile.alias,
                            target = profile.boxing();

                        if (!inicode || typeof(inicode) == "string") return;

                        var vtype = typeof inicode,
                            label = profile.properties.value,
                            imageClass = vtype == "function" ? 'ri-function-line' : vtype == "array" ? 'ri-braces-line' : ' ';
                        inicode = ood.serialize(inicode);

                        ns._editMixData(target, vtype, inicode, label, imageClass, profile);
                        return;
                    }

                    if (!_check(profile)) {
                        return;
                    }

                    //for sepcial function list
                    if (profile.alias == 'cb_callid' && 'callFunction' == ns.tb_action.getUIValue() && ('module' == ns._tagVar.type || 'page' == ns._tagVar.type)) {
                        if ((!ns.tv_fun.getTag() || profile.alias != ns.tv_fun.getTag().alias) && profile.getSubNode('BOX').get(0).offsetWidth) {
                            ns._buildVarTree3('module' == ns._tagVar.type ? ns._tagVar.target : null);
                            ns.tv_fun.setTag(profile);
                            ns._helperPop = ns.tv_fun;
                            ns.blk_float2.getRoot().popToTop(profile.getSubNode('BOX'))
                                .setBlurTrigger("-action-float-inoodb", function () {
                                    ns._helperPop = null;
                                    ns.tv_fun.setTag(null);
                                    ns.blk_float2.hide();
                                    profile.getSubNode('INPUT').blur();
                                }, ood([ns.blk_float2.getRoot().xid(), profile.getSubNode('BOX').xid()]));

                            ns._autoNavTree(type, profile.boxing().getUIValue());
                        }
                    } else {
                        var type = profile.properties.tagVar;
                        if (type == "callback" || type == "adjustdata") {
                            if ((!ns.tv_fun.getTag() || profile.alias != ns.tv_fun.getTag().alias) && profile.getSubNode('BOX').get(0).offsetWidth) {
                                ns._buildVarTree2(type == "adjustdata");
                                ns.tv_fun.setTag(profile);
                                ns._helperPop = ns.tv_fun;
                                ns.blk_float2.getRoot().popToTop(profile.getSubNode('BOX'))
                                    .setBlurTrigger("-action-float-inoodb", function () {
                                        ns._helperPop = null;
                                        ns.tv_fun.setTag(null);
                                        ns.blk_float2.hide();
                                        profile.getSubNode('INPUT').blur();
                                    }, ood([ns.blk_float2.getRoot().xid(), profile.getSubNode('BOX').xid()]));

                                ns._autoNavTree(type, profile.boxing().getUIValue());
                            }
                        } else {
                            if ((!ns.tv_var.getTag() || profile.alias != ns.tv_var.getTag().alias) && profile.getSubNode('BOX').get(0).offsetWidth) {
                                ns._buildVarTree(profile);
                                ns.tv_var.setTag(profile);
                                ns._helperPop = ns.tv_var;
                                ns.blk_float.getRoot().popToTop(profile.getSubNode('BOX'))
                                    .setBlurTrigger("-action-float-inoodb", function () {
                                        ns.tv_var.setTag(null);
                                        ns._helperPop = null;
                                        ns.blk_float.hide();
                                        profile.getSubNode('INPUT').blur();
                                    }, ood([ns.blk_float.getRoot().xid(), profile.getSubNode('BOX').xid()]));

                                ns._autoNavTree(type, profile.boxing().getUIValue());
                            }
                        }
                    }

                    ood.asyRun(function () {
                        var newValue = profile.boxing().getUIValue();
                        var ip = profile.getSubNode("INPUT"), pos, pos1 = 0, pos2 = 0;
                        if (newValue == "{}") pos1 = pos2 = 1;
                        else if (/\(\'[^']*\'\)/.test(newValue)) {
                            pos1 = newValue.indexOf("\'") + 1;
                            pos2 = newValue.lastIndexOf("\'");
                        } else if (/\(\"[^"]*\"\)/.test(newValue)) {
                            pos1 = newValue.indexOf("\"") + 1;
                            pos2 = newValue.lastIndexOf("\"");
                        } else {
                            pos = newValue.lastIndexOf("}");
                            if (pos != -1) pos1 = pos2 = pos;
                        }
                        // stop the default focus event handler
                        profile.$ignoreFocus = 1;
                        ip.focus();
                        ip.caret(pos1, pos2);
                        delete profile.$ignoreFocus;
                    });
                    return false;
                },
                _tabhander = function (profile, keyboard) {
                    if (ns._potentialStep && (keyboard.key == 'tab')) {
                        profile.boxing().setUIValue(ns._potentialStep, true);
                        return false;
                    } else if (keyboard.key == 'up' || keyboard.key == 'esc') {
                        ns._ood_closefloat(profile);

                        return false;
                    } else if (keyboard.key == 'down') {
                        _onfocus(profile);
                    }
                };

            if (!noDyncheck)
                inputs.setDynCheck(true, true);

            inputs.onShowTips(_onTips)
                .beforeFocus(_onfocus)
                // use the inner onchange
                ._onChange(_onchange)
                .onHotKeydown(_tabhander)
                .each(function (profile) {
                    if (profile.properties.multiLines) return;
                    if (profile.properties.tagVar == "none") return;
                    switch (profile.properties.type) {
                        case 'file':
                        case 'spin':
                        case 'currency':
                        case 'number':
                        case 'listbox':
                            return;
                    }
                    ;
                    var flag = profile.properties.tagVar == "callback" || profile.properties.tagVar == "adjustdata";
                    profile.boxing().setCustomStyle({
                        "INPUT": "background-color:" + (flag ? "#00FFFF" : "#FFF8DC") + ";color:" + (flag ? "#000000" : "#000000")
                    });
                });

            ns.grid_params.updateHeader("value", {
                editorProperties: {
                    dynCheck: true
                },
                editorCS: {
                    "INPUT": "background-color:#FFF8DC"
                },
                editorEvents: {
                    onShowTips: _onTips,
                    beforeFocus: _onfocus,
                    _onChange: _onchange,
                    onHotKeydown: _tabhander,
                    onCommand: function (profile) {
                        profile.boxing().setTagVar(null, true);
                        profile.boxing().setCommandBtn('none', true).setInputReadonly(false, true).setUIValue("", true);
                        ood.asyRun(function () {
                            profile.boxing().activate();
                        });
                    }
                }
            });
        },
        _ood_closefloat: function (profile, e, value) {
            var ns = this;
            if (ns._helperPop == ns.tv_fun) {
                ns.tv_fun.setTag(null);
                ns.blk_float2.hide();
            } else {
                ns.tv_var.setTag(null);
                ns.blk_float.hide();
            }
            ns._helperPop = null;
        }
    }
});