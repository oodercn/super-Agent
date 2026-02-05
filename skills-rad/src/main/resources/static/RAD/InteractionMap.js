ood.Class('RAD.InteractionMap', 'ood.Module', {
    Instance: {
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0));
            };

            append(
                ood.create("ood.UI.Dialog")
                    .setHost(host, "ood_ui_dialog1")
                    .setLeft("0em")
                    .setTop("0em")
                    .setWidth("66.66666666666667em")
                    .setHeight("37.5em")
                    .setCaption("$(RAD.inter.Interaction Map)")
                    .setMinBtn(false)
                    .setModal(true)
                    .beforeClose("_close")
            );

            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Block")
                    .setHost(host, "ood_ui_block2")
                    .setDock("fill")
                    .setLeft("16.666666666666668em")
                    .setTop("4.166666666666667em")
                    .setBorderType("inset")
            );

            host.ood_ui_block2.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host, "grid")
                    .setDisableHoverEffect("CELL")
                    .setLeft("0em")
                    .setTop("0em")
                    .setRowHandler(false)
                    .setColSortable(false)
                    .setHeader([
                        {
                            "id": "name",
                            "caption": "$(RAD.inter.Name)",
                            "flexSize": true,
                            "width": "10em",
                            "type": "label"
                        },
                        {
                            "id": "cat",
                            "caption": "$(RAD.inter.Category)",
                            "flexSize": true,
                            "width": "10em",
                            "type": "label"
                        },
                        {
                            "id": "action",
                            "caption": "$(RAD.inter.Action)",
                            "flexSize": true,
                            "width": "10em",
                            "type": "label"
                        },
                        {
                            "id": "cond",
                            "caption": "$(RAD.inter.Conditions)",
                            "width": "6em",
                            "type": "label",
                            "cellStyle": "text-align:center"
                        },
                        {
                            "id": "asyn",
                            "caption": "$(RAD.inter.Asyn)",
                            "width": "4em",
                            "type": "checkbox"
                        },
                        {
                            "id": "stop",
                            "caption": "$(RAD.inter.Abort)",
                            "width": "4em",
                            "type": "checkbox"
                        }
                    ])
                    .setTagCmds([
                        {
                            "id": "add",
                            "itemClass": "ri-add-line",
                            "tips": "Add an event handler"
                        }
                    ])
                    .setActiveMode("none")
                    .setTreeMode("infirstcell")
                    .onCmd("_grid_oncmd")
            );

            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        refreshInterMap: function (page) {
            var ns = this;
            if (page) ns._page = page;
            page = ns._page;

            //get all widgets
            var items = [],
                map = CONF.mapWidgets, item,
                getCap = function (profile) {
                    var prop = profile.properties,
                        type = profile['ood.Module'] ? "module" : "control",
                        cls = profile.key.split(".").pop(),
                        cap;
                    if (type == "module") {
                        cap = /*"$RAD.action.Module " + */profile.getAlias() + " ( " + profile.key + " )";
                    } else {
                        cap = /*"$RAD.action.Control " + */profile.alias + " ( " + cls + " )";
                        if (prop.name || prop.desc) cap += " <span style='color:#888;font-style: italic;font-weight:normal;'>[ " + (prop.name || "") + (prop.name ? " - " : "") + (prop.desc || "") + " ]</span>";
                        if (prop.caption || prop.labelCaption || prop.html) cap += " <span style='color:#888;font-style: italic;font-weight:normal;'>[ " + (prop.caption || prop.labelCaption || prop.html).substr(0, 10) + " ]</span>";
                    }
                    return cap;
                },
                buildEvent = function (id, cap, conf, name) {
                    var sub = [];
                    if (!conf) {
                       return false;
                    }
                    if (conf.actions) {
                        conf = conf.actions;
                    }
                    if (!ood.isArr(conf)) conf = [conf];
                    ood.arr.each(conf, function (a, i) {
                        if (a) {
                            var isStr = ood.isStr(a), map = RAD.ActionsEditor.prototype.CAPMAP,
                                map1 = map.actiongroup,
                                map2 = map.action;
                            sub.push({
                                id: id + ":" + i,
                                caption: isStr ? ("<span style='font-weight: normal;' class='ri-code-line'></span> " + a + " [code ] ") : null,
                                group: isStr,
                                cells: isStr ? null : [
                                    " <span style='font-weight: normal;' class='oodcon spafont spa-icon-action'></span> " + (a.desc || ""),
                                    "$(RAD.action." + map1[a.type == "page" ? "page" :
                                        a.type == "control" ? "control" :
                                            a.type == "module" ? "module" :
                                                a.type == "other" ? (a.type + ":" + a.target) : "-"]
                                    + ")"
                                    + (a.type == "control" || a.type == "module" ? (" - " + a.target) : ""),
                                    "$(RAD.action." + map2[a.method] + ")",
                                    a.conditions && a.conditions.length || "",
                                    ('timeout' in a),
                                    a['return'] === false
                                ],
                                tagCmds: isStr ? ns._actionCmds2 : ns._actionCmds
                            });
                        }

                    });
                    if (sub.length) {
                        var preName = cap.charAt(0) == 'b' ? "$(RAD.designer.eventbfrtag)" :
                            cap.charAt(0) == 'a' ? "$(RAD.designer.eventafttag)" :
                                "$(RAD.designer.eventontag)";
                        return {
                            id: id,
                            caption: " " + preName + "<span style='font-weight: normal;' class='oodcon spafont  spa-icon-arr'></span>" + RAD.EditorTool.getEventName(name, cap, true)
                            + " - <span style='color:#888;font-style: italic;font-weight:normal;'>" + (ood.get(RAD.EditorTool.getDoc(name + ".prototype." + cap, null, true), ["$desc"]) || "").split('.')[0] + "</span>",
                            group: true,
                            rowClass: "ood-treegrid-cells1-alt",
                            initFold: false,
                            tagCmds: ns._eventCmds,
                            sub: sub
                        };
                    } else {
                        return null;
                    }
                },
                rows = [], sub,
                scr = ns.grid.getSubNode('SCROLL22'),
                scrollTop = scr.get(0) && scr.scrollTop();

            // page
            sub = [];
            ood.each(ood.get(page._cls, ['Instance', 'events']), function (o, i) {
                if (i = buildEvent('-page:' + i, i, o, "ood.Module"))
                    sub.push(i);
            });

            if (sub.length) {
                rows.push({
                    group: true,
                    rowClass: "ood-treegrid-cells1-alt",
                    initFold: false,

                    id: "-page",
                    caption: "<span style='font-weight: normal;' class='" + "oodcon spafont spa-icon-page" + "'></span> $(RAD.action.var.Current Page) ( " + page._className + " )",
                    tagVar: page,
                    tagCmds: ns._pageCmds,
                    sub: sub
                });
            }

            // inner controls & modules
            ood.each(page.$host, function (ins, i) {
                if (i == "$inDesign" || !ins['ood.absBox'] || !ins.n0) return;

                var profile = ins.get(0),
                    cls = profile.key.split(".").pop(),
                    t = map[profile.box.KEY];
                sub = [];
                ood.each(profile.getEvents(), function (o, i) {
                    if (!o) return;
                    if (i = buildEvent(profile.alias + ':' + i, i, o, profile.box.KEY))
                        sub.push(i);
                });
                if (sub.length) {
                    rows.push({
                        group: true,
                        rowClass: "ood-treegrid-cells1-alt",
                        initFold: false,

                        id: profile.alias,
                        caption: "<span class='" + (t && t.imageClass || '') + "'></span> " + getCap(profile),
                        tagVar: profile,
                        tagCmds: ns._pageCmds,
                        sub: sub
                    });
                }
            });

            ns.grid.setRows(rows);

            if (scrollTop) ns.grid.getSubNode('SCROLL22').scrollTop(scrollTop);
        },
        events: {
            "onReady": "_page_onready"
        },
        _page_onready: function (module, threadid) {
            var ns = this;
            ns._pageCmds = [
                {
                    "id": "add1",
                    "itemClass": "oodcon ood-uicmd-add",
                    "tips": "Add an event handler"
                }
            ];
            ns._eventCmds = [
                {
                    "id": "edit2",
                    "itemClass": "ri-external-link-line",
                    "tips": "Edit this event handler"
                },
                {
                    "id": "remove2",
                    "itemClass": "ri-close-line",
                    "tips": "Remove this event handler"
                }
            ];
            ns._actionCmds = [
                {
                    "id": "edit3",
                    "itemClass": "ri-check-line",
                    "tips": "Edit this action"
                },
                {
                    "id": "remove3",
                    "itemClass": "oodcon ood-uicmd-close",
                    "tips": "Remove this action"
                }
            ];
            ns._actionCmds2 = [
                {
                    "id": "none",
                    "itemClass": "ri-square-line"
                },
                {
                    "id": "remove3",
                    "itemClass": "oodcon ood-uicmd-close",
                    "tips": "Remove this action"
                }
            ];
        },
        _close: function () {
            this._page._refreshProfileGrid(null, null, true);
        },
        _grid_oncmd: function (profile, row, cmdkey, e, src) {
            var ns = this,
                page = ns._page,
                grid = profile.boxing(),
                ae = RAD.Designer.prototype.$showActionEditor,
                arr = row ? row.id.split(":") : ["-page", null, 0],
                isCanvas = arr[0] === "-page",
                target = (isCanvas ? ns._page.canvas : ns._page.$host[arr[0]]).get(0),
                evtKey = arr[1],
                cap = "$(RAD.action." + (isCanvas ? "Page" : target['ood.Module'] ? "Module" : "Control") + " Action) => " + (isCanvas ? "" : target.alias + " - ") + RAD.EditorTool.getEventName(isCanvas ? 'ood.Module' : target.key, evtKey, true),
                selected = arr[2];
            // refersh after added or modified
            var callback = function () {
                ns.refreshInterMap();
            };
            if (row) {
                switch (cmdkey) {
                    // add to control
                    case 'add1':
                        ood.getModule('RAD.EventPicker', function (prf) {
                            this.refreshList(page, isCanvas ? "-page" : target.alias);
                            this.setEvents({
                                onSel: function (id, evtKey) {
                                    isCanvas = id === "-page";
                                    target = (isCanvas ? ns._page.canvas : ns._page.$host[id]).get(0);
                                    cap = "$(RAD.action." + (isCanvas ? "Page" : target['ood.Module'] ? "Module" : "Control") + " Action) => " + (isCanvas ? "" : target.alias + " - ") + RAD.EditorTool.getEventName(isCanvas ? 'ood.Module' : target.key, evtKey, true);
                                    ae(page, target, evtKey, cap, null, null, callback, profile, null);
                                }
                            });
                            this.show();
                        });
                        break;


                    // edit event
                    case 'edit2':
                        ae(page, target, evtKey, cap, null, null, callback, profile, null);
                        break;
                    // remove event
                    case 'remove2':
                        ood.confirm(ood.getRes('RAD.designer.confirmdel'), ood.getRes('RAD.action.Do you wanna delete the event handler') + '?', function () {
                            if (isCanvas) {
                                var em = ood.get(page._cls, ['Instance', 'events']);
                                if (!em) ood.set(page._cls, ['Instance', 'events'], em = {});
                                delete em[evtKey];
                                page._canvaseventdirty = true;
                            } else if (target['ood.Module']) {
                                target.events[evtKey] = null;
                                page._dirty = 1;
                            } else {
                                target[evtKey] = null;
                                page._dirty = 1;
                            }
                            grid.removeRows(row);
                        });
                        break;

                    // edit action
                    case 'edit3':
                        ae(page, target, evtKey, cap, selected, null, callback, profile, null);
                        break;
                    // remove action
                    case 'remove3':
                        ood.confirm(ood.getRes('RAD.designer.confirmdel'), ood.getRes('RAD.action.Do you wanna delete the action') + '?', function () {
                            var evs;
                            if (isCanvas) {
                                var em = ood.get(page._cls, ['Instance', 'events']);
                                if (!em) ood.set(page._cls, ['Instance', 'events'], em = {});
                                evs = em[evtKey];
                                var oarr= evs.actions?evs.actions:evs;
                                ood.arr.removeFrom(oarr, selected);
                                page._canvaseventdirty = true;
                            } else if (target['ood.Module']) {
                                evs = target.events[evtKey];
                                var oarr= evs.actions?evs.actions:evs;
                                ood.arr.removeFrom(oarr, selected);
                                page._dirty = 1;
                            } else {
                                evs = target[evtKey];
                                var oarr= evs.actions?evs.actions:evs;
                                ood.arr.removeFrom(oarr, selected);
                                page._dirty = 1;
                            }
                            grid.removeRows(row);
                        });
                        break;
                }
            } else {
                switch (cmdkey) {
                    // add all
                    case 'add':
                        ood.getModule('RAD.EventPicker', function (prf) {
                            this.refreshList(page, "-page");
                            this.setEvents({
                                onSel: function (id, evtKey) {
                                    isCanvas = id === "-page";
                                    target = (isCanvas ? ns._page.canvas : ns._page.$host[id]).get(0);
                                    cap = "$(RAD.action." + (isCanvas ? "Page" : target['ood.Module'] ? "Module" : "Control") + " Action) => " + (isCanvas ? "" : target.alias + " - ") + RAD.EditorTool.getEventName(isCanvas ? 'ood.Module' : target.key, evtKey, true);
                                    ae(page, target, evtKey, cap, null, null, callback, profile, null);
                                }
                            });
                            this.show();
                        });
                }
            }
        }
    }
});
