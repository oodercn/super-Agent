ood.Class('RAD.OODDesigner', 'ood.Module', {
    Constructor: function () {
        var ns = this,
            o = ood.message;
        arguments.callee.upper.apply(ns, arguments);
        SPA = ns;
        ns.spath = null;
        ns.tpath = null;
        ns.openAjax = null;
        ns.tabsMain = null;
        ns.curProjectPath = null;
        ns.currentPage = null;

        ns.currCom = null;
        ns.currentRootCls = null;
        ns.curProjectConfig = null;
        ns.ViewMenuBar = {};
        ns.componentMenuItems = [];
        ns.Message = [];
        ood.message = function (content) {
            if (!ns.Message)
                return;
            if (ns.Message.length > 20) ns.Message.pop();

            content = ood.stringify(content);
            if (/</.test(content))
                while (content != (content = content.replace(/<[^<>]*>/g, ""))) ;
            ns.Message.unshift({
                id: ood.id(),
                caption: content.length > 50 ? content.substr(0, 50) + '...' : content,
                tips: content,
                imageClass: 'ri-information-line'
            });

            // ns.toolbar.updateItem('info', {label: content.length > 50 ? content.substr(0, 50) + '...' : content});
            o.apply(null, ood.toArr(arguments));
        };
    },
    Instance: {
        $classEditor: null,
        $pageviewType: 'ood.UI.Tabs',
        $firstView: "design",
        $dftCodePath: '/template/',
        $dftCodeFileName: 'index.js',
        $fetchedCode: '',
        $iniCode: '',
        $url: '',
        $dirty: false,
        currentClassName: null,
        fe: function (evtName, evtParams) {
            if (this.events[evtName]) return this.fireEvent(evtName, evtParams);
        },
        onDestroy: function () {
            this.$classEditor.destroy();
        },
        events: {
            onReady: function () {
                SPA = this;
                ood.ComFactory.setProfile(CONF.ModuleFactoryProfile);
                ood.CSS.addStyleSheet(ood.UI.buildCSSText({
                    '.ood-designer-root .ood-designer-canvas': {
                        'background-image': 'url(' + ood.getPath('/RAD/img/designer/', 'bg.gif') + ')',
                        'background-position': 'left top',
                        'background-repeat': 'repeat',
                        "background-color": '',
                        "background-attachment": ''
                    }
                }), 'ood.UI.design', null, true);
            },
            onRender: function (com, threadid) {
                com.setValue(com.$fetchedCode || com.$iniCode, com.$fetchedurl);
            },
            afterIniComponents: function () {
            }
        },

        iniExComs: function (com, threadid) {
            var com = this;
            ood.ComFactory.newCom('RAD.JSEditor', function (err, icom, threadid) {
                var inn = this;
                inn.host = com;
                inn.setEvents('onValueChanged', function (page, flag) {
                    com.$dirty = flag;
                    ood.resetRun(com.KEY + ":onValueChanged", function () {
                        SPA.fe("onValueChanged", [flag]);
                    }, 200);
                });
                inn.create(function (o, threadid) {
                    inn.buttonview.setNoHandler(true);
                }, threadid);
                com.$classEditor = inn;
                com._openproject(currProjectName, {files: [{id: 'index', className: 'view.Index'}]});

            }, threadid, {$pageviewType: com.$pageviewType});
        },


        reloadPage: function () {
            if (this.tabsMain.getSelectedItem()) {
                var item = ood.clone(this.tabsMain.getSelectedItem());
                this.tabsMain.removeItems(item.id);
                this.openCls(this.openAjax, item);
                var tb = this.tabsMain;
                ood.setTimeout(function () {
                    tb.fireItemClickEvent(item.id);
                }, 1500);

            }
        },

        reOpenCls: function (ajax, item) {
            if (item && item.id) {
                this.tabsMain.removeItems(item.id);
                this.openCls(this.openAjax, item);
                var tb = this.tabsMain;
                ood.setTimeout(function () {
                    tb.fireItemClickEvent(item.id);
                }, 1500);

            }
        },

        getCurrentClassName: function () {
            var className;
            if (this.tabsMain.getSelectedItem()) {
                className = this.tabsMain.getSelectedItem().className;
            }
            return className || this.currentClassName;
        },

        reName(ajax, profile) {
            if (!profile) {
                return;
            }
            var ins1 = profile.host, tagVar = profile.tagVar, parentId = tagVar.parentId, path = tagVar.path, tail = "";
            var name = path;
            if (ood.str.endWith(name, "/")) {
                name = name.substr(0, name.length - 1);
                name = name.substr(name.lastIndexOf("/") + 1, name.length);
            } else if (name.indexOf(".") > -1) {
                tail = name.substr(name.lastIndexOf("."), name.length);
                name = name.substr(name, name.lastIndexOf("."));
            }

            var dlg = ood.prompt(ood.getRes("RAD.designer.Rename"), ood.getRes("RAD.Specify the new name") + (tail ? ("(" + tail + ")") : ""), name, function (nn) {
                if (nn) nn = ood.str.trim(nn);
                nn += tail;
                if (!CONF.fileNames.test(nn)) {
                    ood.message(ood.getRes("RAD.addfile.filenameformat"));
                    return false;
                } else {
                    ajax.setQueryData({
                        path: path,
                        projectName: SPA.curProjectName,
                        newName: name
                    });

                    ajax.invoke(function (txt) {
                        var obj = txt;
                        if (obj && !obj.error && obj.requestStatus != -1) {
                            var data = obj.data;
                            if (ins1) ins1.updateItem(path, data);
                            ins1.reloadNode(parentId);
                            ood.message(ood.getRes("$RAD.designer.msg.renamefileok"));
                        } else
                            ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                    })
                }
            });
            dlg._$input.setMultiLines(false);//.setHeight(30);
            dlg = null;
        },

        closeFile: function (path) {
            var ns = this,
                tb = ns.tabsMain;
            if (tb.getItemByItemId(path)) {
                tb.removeItems([path]);
            }
        }
        ,

        openCls: function (ajax, config) {
            if (config == null) {
                return;
            }
            this._openfile(ajax, config)
        },


        newFolder: function (ajax, propMenu) {
            if (propMenu == null) {
                return;
            }
            var tarVar = propMenu.tagVar, insOther = propMenu.host.OODFileTree;
            var tbItem = {
                id: tarVar.path,
                caption: tarVar.path
            };
            ood.showModule("RAD.AddFolder", function () {
                this.ajax = ajax;
                this.setProperties({
                    fileName: name,
                    parentPath: tarVar.path,
                    typeCaption: '$RAD.esdmenu.newFolder',
                    type: '/',
                    forcedir: true,
                    tailTag: ""
                });
                this.setEvents({
                    onOK: function (item) {
                        try {
                            ns.dialog.hide();
                        } catch (e) {
                        }
                        item.sub = true;
                        item.id = item.path;
                        item.value = item.path
                        if (insOther && (tbItem = insOther.getItemByItemId(tbItem.id)))
                            insOther.insertItems([item], tbItem.id, true);
                    }
                });
            }, null, null, true);
        },
        newTextFile: function (ajax, propMenu) {
            if (propMenu == null) {
                return;
            }
            var tarVar = propMenu.tagVar, insOther = propMenu.host.OODFileTree;
            var tbItem = {
                id: tarVar.path,
                caption: tarVar.path
            };
            ood.showModule("RAD.AddFile", function () {
                this.ajax = ajax;
                this.setProperties({
                    fileName: name,
                    parentPath: tarVar.path,
                    typeCaption: '.js',
                    type: 'file',
                    forcedir: true,
                    tailTag: ".js"
                });
                this.setEvents({
                    onOK: function (item) {
                        try {
                            ns.dialog.hide();
                        } catch (e) {
                        }
                        item.id = item.path;
                        item.value = item.path
                        if (insOther && (tbItem = insOther.getItemByItemId(tbItem.id))) {
                            insOther.insertItems([item], tbItem.id, true);
                        }
                        ns._openfile(ns.openAjax, item, item.path, false, true, false)
                    }
                });
            }, null, null, true);
        },

        newClass: function (ajax, propMenu) {
            if (propMenu == null) {
                return;
            }
            var tarVar = propMenu.tagVar, ns = this, insOther = propMenu.host.OODFileTree;
            var tbItem = {
                id: tarVar.packageName,
                caption: tarVar.packageName
            };
            ood.showModule("RAD.AddCls", function () {
                this.ajax = ajax;
                this.setProperties({
                    fileName: 'Page',
                    parentPath: tarVar.path,
                    typeCaption: '.cls',
                    type: 'class',
                    packageName:tarVar.packageName,
                    forcedir: true,
                    tailTag: ".cls"
                });
                this.setEvents({
                    onOK: function (item) {
                        try {
                            ns.dialog.hide();
                        } catch (e) {

                        }
                        if (insOther && (tbItem = insOther.getItemByItemId(tbItem.id))) {
                            item.caption = item.name ? item.name : item.caption;
                            insOther.insertItems([item], tbItem.id, true);
                        }
                        item.value = item.id;
                        ns._openfile(ns.openAjax, item, null, false, true, false)

                    }
                });
            }, null, null, true);

        },


        getValue: function () {
            return this.$classEditor.getValue();
        },
        setValue: function (str, url) {
            var self = this;
            if (str)
                self.$classEditor.setValue(str);
            if (url)
                self.$url = url;
            var dis = self.$url ? !ood.absIO.isCrossDomain(self.$url) ? '' : 'none' : 'none';

            self.$dirty = false;
        },
        iniComponents: function () {
            // [[Code created by ESDUI RAD Studio
            var host = this, children = [], append = function (child) {
                children.push(child.get(0))
            };

            append(new ood.UI.Tabs()
                .setHost(host, "tabsMain")
                .beforeUIValueSet("_tabsmain_beforeValueUpdated")
                .beforePageClose("_tabsmain_beforepageclose")
                .afterPageClose("_tabsmain_afterpageclose")
                .onItemSelected("_tabmain_onitemselected")

                .setCustomStyle({
                    "PANEL": "overflow:hidden;"
                }));
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        //////
        getDesigner: function () {
            return this.$classEditor._designer;
        },
        getViewSize: function () {
            return ood.copy(this.getDesigner().$curViewSize);
        },
        // size:{width:,height:}
        setViewSize: function (size) {
            this.getDesigner().setViewSize(size);
        },
        getSelected: function () {
            var sel = this.getDesigner().tempSelected,
                out;
            if (sel && sel.length) {
                out = [];
                ood.arr.each(sel, function (o) {
                    out.push(ood.getObject(o));
                });
            }
            return out;
        },
        selectByAlias: function (alias) {
            var des = this.getDesigner(),
                hash = des.getNames();
            if (hash[alias]) {
                des.selectWidget(hash[alias].$xid);
            }
        },
        clearSelected: function () {
            var des = this.getDesigner();
            des._clearSelect();
            des._setSelected(null, true);
        },
        delSelected: function () {
            this.getDesigner()._deleteSelected(true);
        },
        getWidgetByAlias: function (alias) {
            var des = this.getDesigner(),
                hash = des.getNames();
            return hash[alias];
        },
        addWidget: function (widget, parentAlias, subId) {
            if (widget.$key == "ood.UIProfile")
                widget = widget.boxing();

            if (widget["ood.UI"]) {
                var des = this.getDesigner(),
                    p = this.getWidgetByAlias(parentAlias);
                if (p && p.behavior.PanelKeys && p.behavior.PanelKeys.length)
                    p = p.boxing();
                else
                    p = des.canvas;

                p.append(widget, subId);
                des._designable(widget.get(0));
                des._dirty = true;
            }
        },
        removeWidgetByAlias: function (alias) {
            var des = this.getDesigner(),
                n = this.getWidgetByAlias(alias);
            if (n) {
                n.boxing().destroy();
                des._dirty = true;
            }
            this.clearSelected();
        },
        getRootWidgets: function () {
            return this.getDesigner().getWidgets();
        },
        getAllWidgets: function () {
            return this.getDesigner().getNames();
        },
        getJSCode: function () {
            var des = this.getDesigner(),
                code = des.getJSCode(des.getWidgets());
            code = code.replace(/^return ood.create\(/, '');
            code = code.replace(/\)\.get\(\);$/, '');
            return code;
        },
        getJSONCode: function () {
            var des = this.getDesigner(),
                code = des.getJSONCode(des.getWidgets());
            code = code.replace(/^return ood.create\(/, '');
            code = code.replace(/\)\.get\(\);$/, '');
            return code;
        },
        clearCode: function () {
            var des = this.getDesigner();
            des._clearSelect();
            des.canvas.removeChildren(null, true);
            des._dirty = true;
            des.resetCodeFromDesigner(true);
        },
        setJSCode: function (code) {
            var des = this.getDesigner();
            this.clearCode();
            des.refreshView(code, true);
            des._dirty = true;
            des.resetCodeFromDesigner(false);
        },
        setJSONCode: function (json) {
            this.setJSCode('return ood.UI.unserialize(' + json + ').get();');
        },
        isDirtied: function () {
            return this.getDesigner().isDirty();
        },
        markDirty: function (flag) {
            var des = this.getDesigner();
            if (flag) des._dirty = true;
            else des.clearDirty();
        },

        closeall() {
            this.tabsMain.clearItems();
        },

        save: function (ajax) {
            var tb = this.tabsMain,
                o = tb.getItemByItemId(tb.getUIValue());
            this._save(ajax, o);
        },

        saveAll(ajax) {
            var ns = this,
                items = this.tabsMain.getItems();
            ood.arr.each(items, function (o) {
                ns._save(ajax, o);
            });
        },


        _openproject: function (pm, obj, nomainjs, currentRootCls) {
            var ns = this, clsFiles = obj.files, tabs = ns.tabsMain;
            var projectName = pm, simProjectName = projectName;
            ns.currentRootCls = currentRootCls || simProjectName;
            ns.curProjectPath = pm;
            ns.tabsMain.setDisabled(false);

            var hash = CONF.getStatus(),
                prjs = hash.projects,
                index = ood.arr.subIndexOf(prjs || [], 'id', ns.curProjectPath),
                files = index != -1 ? prjs[index].files : [],
                file = index != -1 ? prjs[index].file : null;
            if (!files) files = [];
            if ((!file || !file.className)  && files[0]) file = files[0];
            if ((!file || !file.className)  && !nomainjs) {
                var className = obj.index ? obj.index + '.cls' : ns.currentRootCls + "/index.cls";
                file = {
                    id: className,
                    className: className
                }

                if (ood.arr.subIndexOf(files, file) == -1) {
                    files.push(file);
                }
            }

            if (files && files.length) {
                ood.arr.each(files, function (o, i) {
                    ns._openfile(ns.openAjax, o);
                });
            }
            ns._openfile(ns.openAjax, file);

            document.title = document.title.replace(/\s*\[.*\]\s*$/, '') + " [" + pm + "]";
        }
        ,

        _save: function (ajax, o) {
            var collections = [];
            this._saveFile(o, collections);
            ood.resetRun("保存", function () {
                var hash = {};
                ood.each(collections, function (o) {
                    ajax.setQueryData(o.paras);
                    ajax.invoke(
                        o.onSuccess, o.onFail
                    );
                });
            });
        },

        _checkEvent: function (events) {
            var ns = this;
            var subevents = {};
            ood.each(events, function (event, key, events) {
                var eventMap = events[key];
                if (eventMap) {
                    if (ood.isArr(eventMap)) {
                        aAction = [];
                        ood.each(eventMap, function (action) {
                            if (typeof(action) == "string") {
                                aAction.push({script: action, desc: '函数'});
                            } else if (ood.isHash(action)) {
                                aAction.push(action);
                            }
                        });
                        subevents[key] = {actions: aAction};
                    } else if (typeof(eventMap) == "string") {
                        subevents[key] = {actions: [{script: eventMap, desc: '函数'}]}
                    } else if (!eventMap.actions) {
                        delete  events[key];
                        var neweventMap = {actions: eventMap};
                        subevents[key] = neweventMap;
                    } else {
                        var subAction = [];
                        var actions = events[key].actions;
                        if (typeof(actions) == "string") {
                            subAction.push({script: actions.toString(), desc: '函数'})
                        } else {
                            ood.each(actions, function (action, k) {
                                if (!ood.isHash(action)) {
                                    subAction.push({script: action.toString(), desc: '函数'});
                                } else {
                                    var args = [];
                                    ood.each(action.args, function (arg, i) {
                                            if (typeof(arg) == "function") {
                                                args.push({script: arg.toString(), desc: '函数'});
                                            } else {
                                                args.push(arg);
                                            }

                                        }
                                    );
                                    if (!action.type) {
                                        action.type = 'page';
                                    }
                                    action.args = args;
                                    subAction.push(action);
                                }

                            });
                            subevents[key] = events[key];
                            subevents[key].actions = subAction;
                        }

                    }
                }
            });
            return subevents;
        },

        _tabsmain_beforepageclose: function (profile, item, src) {
            if (item._dirty) {
                ood.UI.Dialog.confirm(ood.getRes('RAD.notsave'), ood.getRes('RAD.notsave2'), function () {
                    profile.boxing().removeItems(item.id);
                });
                return false;
            }
        }
        ,
        _tabsmain_afterpageclose: function (profile, item, src) {
            if (item.$obj) item.$obj.destroy();
            SPA.currentPage = "";

        }
        ,
        _tabsmain_beforeValueUpdated: function (profile, ov, nv) {
            var item = this.tabsMain.getItemByItemId(ov), t;
            if (!item) return;
            if (t = item.$obj) {
                if (t.checkEditorCode && t.checkEditorCode() === false) return false;
            }
        }
        ,
        _tabmain_onitemselected: function (profile, item, e, src) {
            if (item._inn) {
                profile.boxing().setDisabled(true);
                profile.boxing().append(item._inn, item.id);
                delete item._inn;
            } else {
                this._openfile(ns.openAjax, item, null, true);

            }
            ood.tryF(function () {
                if (item.$obj) item.$obj.activate();
            });
            SPA.currentPage = item.id;

        },

        _openfile: function (ajax, item, jseditor, stopseleted, dontshowerror, forceAsText) {
            if (!item || !item.id || item.sub) return;

            var ns = this, tb = ns.tabsMain, pro = tb.reBoxing().cssRegion(true);
            var funOK = function (txt) {
                    if (txt.error) {
                        return;
                    }
                    if (!txt.data || !txt.data.content) {
                        return;
                    }
                    var filecon = txt.data.content;
                    if (ood.isSet(filecon)) {
                        item.closeBtn = true;
                        items = tb.getItems(),
                            callback = function (pagprofile, b) {
                                tb.markItemCaption(pagprofile.properties.keyId, b);
                            };

                        if (!tb.getItemByItemId(item.id)) {
                            tb.insertItems2([item], items.length ? items[items.length - 1].id : null);
                        }


                        if (jseditor || (!ood.str.endWith(item.id, 'js') && !ood.str.endWith(item.id, 'cls'))) {
                            var keyId = item.id;
                            ood.ModuleFactory.newModule('RAD.PageEditor', function () {
                                var inn = this, tabItem = tb.getItemByItemId(item.id);
                                tabItem.$obj = inn;
                                tabItem._inn = inn;
                                inn.host = ns;
                                inn.setProperties({
                                    keyId: keyId
                                });
                                inn.setEvents('onValueChanged', callback);
                                inn.setEvents('afterRendered', function () {
                                    tb.setDisabled(false);
                                });

                                inn.setValue(filecon);
                                if (!stopseleted)
                                    tb.fireItemClickEvent(tabItem.id);
                            });
                        } else {
                            ood.ModuleFactory.newModule('RAD.JSEditor', function () {
                                var inn = this, tb = ns.tabsMain;
                                inn.buttonview.setNoHandler(true);
                                inn.host = ns;
                                inn.setProperties({
                                    keyId: item.id
                                });
                                inn.setEvents('onValueChanged', callback);
                                var des = inn._designer;
                                var tabItem = tb.getItemByItemId(item.id);
                                tabItem.$obj = inn;
                                tabItem._inn = inn;

                                inn.setEvents('afterRendered', function () {
                                    tb.setDisabled(true);
                                    ood.setTimeout(function () {
                                        try {
                                            if (des.getWidgets() && des.getWidgets().length > 0) {
                                                var jscode = des.getJSCode(des.getWidgets(true));
                                                des.markDirty(true);
                                                des.resetCodeFromDesigner(true);
                                                des.refreshView(jscode, true);
                                                des.markDirty(false);
                                                tb.markItemCaption(item.id, false, true);
                                            }
                                        } catch (e) {
                                            console.warn(e);
                                        }
                                        tb.setDisabled(false);
                                    }, 500);
                                });

                                var clsname = RAD.ClassTool.getClassName(filecon),
                                    pclsname = RAD.ClassTool.getClassName(filecon, true);


                                if (clsname && pclsname && !forceAsText) {
                                    inn.setDftPage('design');
                                } else {
                                    inn.setDftPage('code');
                                }
                                inn.setValue(filecon);
                                if (!stopseleted)
                                    tb.fireItemClickEvent(item.id);
                            });
                        }

                    }
                },
                funFail = function (msg) {
                    if (!dontshowerror)
                        ood.message(msg);
                };
            if (ajax) {
                ajax.setQueryData({
                    path: item.id,
                    className: item.className,
                    projectName: SPA.curProjectName || 'test'
                });
                ajax.invoke(
                    funOK, funFail
                );
                ns.openAjax = ajax;
            } else {
                ood.Ajax("/rad/file/getFileContent", {
                    path: item.path,
                    className: item.className,
                    projectName: SPA.curProjectName || 'test'
                }, funOK, funFail, {method: 'POST'}).start();
            }

        },

        _itemOnStartDrag: function (profile, e, src) {
            if (!profile) {
                return;
            }
            if (ood.Event.getBtn(e) != "left") return;
            if (profile.properties.disabled) return;
            // not resizable or drag
            if (!profile.properties.dragKey) return;

            // avoid nodraggable keys
            if (profile.behavior.NoDraggableKeys) {
                var sk = profile.getKey(ood.Event.getSrc(e).id || "").split('-')[1];
                if (sk && ood.arr.indexOf(profile.behavior.NoDraggableKeys, sk) != -1) return;
            }
            var id = ood.use(src).id(),
                itemId = profile.getSubId(id),
                properties = profile.properties,
                item = profile.getItemByDom(id),
                pos = ood.Event.getPos(e);
            var moduletype = item.className;
            if (item.className && item.key == 'ood.Module') {
                moduletype = item.className;
            } else {
                moduletype = item.key ? item.key : item.tagVar.key;
            }


            // if (item.draggable) {
            var csrc = ood(src).clone(true);
            csrc.css('width', '100%');

            profile.getSubNode('ITEMICON', itemId).startDrag(e, {
                dragType: 'icon',
                shadowFrom: csrc,
                dragCursor: 'pointer',
                targetLeft: pos.left - 12,
                targetTop: pos.top + 12,
                dragKey: item.dragKey || profile.properties.dragKey,
                dragData: {
                    cls: item.cls || (item.key ? item.key : item.tagVar.key),
                    type: moduletype,
                    children: item.sub,
                    caption: item.caption,
                    iniProp: item.iniProp,
                    iniEvents: item.iniEvents,
                    customRegion: item.customRegion,
                    image: item.image,
                    imagePos: item.imagePos,
                    imageClass: item.imageClass,

                }
            });

            if (RAD.Designer) {
                ood.each(RAD.Designer.getAllInstance(), function (page) {
                    if (page && !page.destroyed) {
                        page._clearSelect();
                        if (page.resizer)
                            page._setSelected([], true);
                    }
                });
            }
            return false;
            // }
        },

        _saveFile: function (o, collections) {
            var ns = this, tb = this.tabsMain;
            if (o._dirty) {
                var newText = o.$obj.getValue(), isModule, cur, shouldbe;
                if (false === newText)
                    return false;

                if (ood.str.startWith(o.id, SPA.curProjectPath + "/" + ns.currentRootCls + "/") || (isModule = ood.str.startWith(o.id, SPA.curProjectPath + "/Module/js/"))) {
                    cur = RAD.ClassTool.getClassName(newText);
                    var currpath = o.id;
                    if (currpath.indexOf(SPA.curProjectPath) > -1) {
                        currpath = currpath.replace(SPA.curProjectPath + '/', '');
                    }
                    shouldbe = ood.getClassName(currpath);
                    if (cur != shouldbe) {
                        ood.pop(ood.getRes("RAD.designer.The class name in '$0' should be '$1', but it's '$2'", o.id, shouldbe, cur));
                        shouldbe = cur = null;
                    }
                }

                var paras = {
                    projectName: SPA.curProjectName,
                    hashCode: ood.id(),
                    curProjectPath: SPA.curProjectPath,
                    path: o.id,
                    fileType: 'EUFile',
                    content: newText,
                    jscontent: newText
                };
                var designer = o.$obj._designer;

                if (designer && designer.getWidgets() && designer.getWidgets().length > 0) {
                    var code = designer.getJSCode(designer.getWidgets());
                    var com = new ood.Module();
                    com.iniComponents = new Function([], code);
                    var hash = {}, t;
                    ood.each(designer.getNames(), function (prf) {
                        var parentName = prf.parent ? prf.parent.alias : 'this';
                        if (!prf.moduleClass) {
                            t = hash[prf.alias] = prf.serialize(false, false, false);
                            if (hash[prf.alias].events) {
                                hash[prf.alias].events = ns._checkEvent(hash[prf.alias].events);
                            }
                            hash[prf.alias].host = parentName;
                            if (prf.childrenId) {
                                hash[prf.alias].target = prf.childrenId;
                            }
                        } else {
                            var module = prf.host;
                            module.properties.className = prf.moduleClass;
                            if (!hash[module.alias]) {
                                t = hash[module.alias] = module.serialize(false, false, false);
                                if (hash[module.alias].events) {
                                    hash[module.alias].events = ns._checkEvent(hash[module.alias].events);
                                }
                                hash[module.alias].host = parentName;
                                if (prf.childrenId) {
                                    hash[module.alias].target = prf.childrenId;
                                }

                                hash[module.alias].className = prf.moduleClass;
                                hash[module.alias].key = 'ood.Module'
                            }
                        }

                        //delete t['ood.Module'];
                    });


                    //删除错误定义
                    if (designer._cls.Instance.functions) {
                        ood.each(designer._cls.Instance.functions, function (modulefunction) {
                            if (modulefunction.actions) {
                                var iactions = [];
                                ood.each(modulefunction.actions, function (action) {
                                    if (ood.isFun(action)) {
                                        iactions.push(
                                            {script: action.toString(), desc: '脚本函数'}
                                        )
                                    } else if (action && ood.isObj(action)) {
                                        if (!action.type) {
                                            delete action.type;
                                        }
                                        iactions.push(action)
                                    }
                                    ;
                                });
                                modulefunction.actions = iactions;
                            }
                        })

                    }
                    ;

                    //初始化事件

                    designer._cls.Instance.events = ns._checkEvent(designer._cls.Instance.events);
                    var modulefunctions = {};
                    ood.each(designer._cls.Instance, function (action, key) {
                        var syskeys = ['iniComponents', 'initialize', 'customAppend'];
                        if (typeof(action) == 'function') {
                            if (ood.arr.indexOf(syskeys, key) == -1) {
                                modulefunctions[key] = action.toString();
                            }
                        }
                    });

                    var moduleVar = {};

                    ood.each(designer._cls.Instance, function (action, key) {
                        var syskeys = ['Dependencies', 'Required', 'properties', 'events', 'functions'];
                        if (typeof(action) != 'function') {
                            if (ood.arr.indexOf(syskeys, key) == -1) {
                                moduleVar[key] = ood.serialize(action);
                                ;
                            }
                        }
                    });

                    //初始化
                    var customAppend = designer._cls.Instance.customAppend, customAppendStr;
                    if (customAppend && typeof(customAppend) == 'function') {
                        customAppendStr = customAppend.toString();
                    }


                    var deffunctions = {};
                    if (!designer._cls.Instance.functions && ood.isHash(designer._cls.Instance.functions)) {
                        deffunctions = designer._cls.Instance.functions;
                    }


                    var oodcode = {
                            components: hash,
                            'Static': designer._cls.Static || {},
                            events: ns._checkEvent(designer._cls.Instance.events),
                            customFunctions: modulefunctions,
                            moduleVar: moduleVar,
                            customAppend: customAppendStr,
                            dependencies:
                            designer._cls.Instance.dependencies || [],
                            properties:
                            designer._cls.Instance.properties || {},
                            required:
                            designer._cls.Instance.Required || []
                        }
                    ;


                    var code = ood.Coder.formatText(ood.stringify(oodcode), 'js');
                    paras.className = designer._className;
                    paras.content = code
                    paras.fileType = 'EUClass';
                    com.destroy();
                }


                if (CONF.grid_showPropBinder) {
                    var init = ood.get(RAD.ClassTool.getClassStruct(newText), ["sub", "Instance", "sub", "iniComponents"]);
                    if (init) {
                        var module = new ood.Module();
                        module.iniComponents = ood.unserialize(init.code);
                        paras.propKeys = module.getPropBinderKeys();
                    }
                }
                ;
                var onSuccess = function (txt) {
                    var obj = txt;
                    if (obj && !obj.error && obj.data && obj.requestStatus != -1) {
                        o.$obj.setValue(newText);
                        tb.markItemCaption(o.id, false, true);
                        if (isModule && shouldbe) {
                            var m = ood.SC(shouldbe);
                            if (m) {
                                // refresh Class first
                                m.refresh(newText);
                                // refresh  instances
                                var ins = m.getAllInstance();
                                if (!ood.isEmpty(ins)) {
                                    ood.each(ins, function (mi) {
                                        mi.refresh();
                                    });
                                }
                            }
                        }

                        if (o.id == SPA.curProjectPath + "/oodconf.js") {
                            ns.refreshProjectConfig(newText);
                        }
                        ood.message('  save success!   ');
                    } else
                        ood.message(ood.get(obj, ['error', 'errdes']) || obj || 'no response!');
                };
                var onFail = function (txt) {
                    ood.message(txt);
                };
                collections.push({id: o.id, paras: paras, onSuccess: onSuccess, onFail: onFail});

            }
            return collections;
        }
    },

    _updateComponent: function (item) {
        var designer = this._getDesigner(), currNode = designer.getByAlias(item.alias, true), ns = this;
        currNode = currNode || designer.canvas;
        if (!currNode.removeChildren) {
            currNode.boxing().removeChildren(true, true, true);
        } else {
            currNode.removeChildren(true, true, true);
        }

        if (item.children) {
            ood.each(item.children, function (o) {
                ns._addComponent(o);
            })
        }

        var dirty = designer._dirty;
        designer.refreshView(designer.getJSCode(designer.getWidgets(true)), true);
        if (dirty) designer.markDirty();
    }

    ,

    _addComponent: function (item, parentId) {
        var designer = this._getDesigner();
        var selectNode = designer.getByCacheId(this.tempSelected);
        var parent;
        if (selectNode) {
            selectNode = ood.getObject(designer.tempSelected[0]);
            if (selectNode) {
                selectNode = selectNode.boxing();
            } else if (parentId) {
                selectNodes = ood.getObjectByAlias(parentId);
                if (selectNodes && selectNodes.length > 1) {
                    selectNode = selectNodes[0].boxing();
                }
            }
        } else {
            selectNode = selectNode.boxing();
        }
        parent = selectNode || designer.canvas;

        var obj = item, children = item.children || item.sub;
        if (!children || children.length == 0) {
            obj = {children: [item]}
        }

        if (ood.str.startWith(item.key, 'ood.svg.')) {
            var components = ood.addChild(obj, parent, designer.$host, designer.$host);
            var redo = function () {
                    var components = ood.addChild(obj, parent, designer.$host, designer.$host);
                    var go = components[0].get(0);
                    designer._designable(go);
                    designer.selectWidget([go.$xid]);
                },
                undo = function () {
                    var ins = designer.getByAlias(item.alias);
                    ins.destroy(true, true);
                }

            designer.markDirty(undo, redo, "add commponents");

    //        if (!(t['ood.UI'] && !t.$initRootHidden)) {

        } else if (ood.str.startWith(item.key, 'ood.UI.') && item.key!='ood.UI.PopMenu' ) {
            var components = ood.addChild(obj, parent, designer.$host, designer.$host);
            var go = components[0].get(0), xid = go.$xid;
            designer._designable(go);
            designer.selectWidget([xid]);

            var redo = function () {
                    var components = ood.addChild(obj, parent, designer.$host, designer.$host);
                    var go = components[0].get(0);
                    designer._designable(go);
                    designer.selectWidget([go.$xid]);
                },
                undo = function () {
                    var ins = designer.getByAlias(item.alias);
                    ins.destroy(true, true);
                }

            designer.markDirty(undo, redo, "add commponents");


        } else {
            var citem = item;
            var target = ood.create(citem.key)
                .setHost(designer.$host, citem.alias)
                .setAlias(citem.alias)
                .setEvents(citem.events)
                .setProperties(citem.properties);
            if (designer.$host) {
                designer.$host[citem.alias] = target
            }

            designer.iconlist.insertItems([{
                'id': target.get(0).$xid,
                'imgWidth': '16',
                'imgHeight': '16',
                'tips': designer._bldTips(target, designer.$moduleInnerMode ? "" : CONF.enable_codeEdtor ? ("$(RAD.designer.Dblclick to edit the code)") : "")
                , 'imgStyle': item.imgStyle,
                'imageClass': item.imageClass
            }]);
            designer.iconlist.setUIValue(target.get(0).$xid);
            if (!children || children.length == 0) {
                obj = {children: [item]}
            }
            var go = target.get(0), xid = go.$xid;
            designer._designable(go);
            designer.selectWidget([go.$xid]);
            ood.addChild(obj, parent, designer.$host, designer.$host);
            var redo = function () {
                    var target = ood.create(citem.key)
                        .setHost(designer.$host, citem.alias)
                        .setAlias(citem.alias)
                        .setEvents(citem.events)
                        .setProperties(citem.properties);
                    if (designer.$host) {
                        designer.$host[citem.alias] = target
                    }
                    designer.iconlist.insertItems([{
                        'id': xid,
                        'imgWidth': '16',
                        'imgHeight': '16',
                        'tips': designer._bldTips(target, designer.$moduleInnerMode ? "" : CONF.enable_codeEdtor ? ("$(RAD.designer.Dblclick to edit the code)") : "")
                        , 'imgStyle': item.imgStyle,
                        'imageClass': item.imageClass
                    }]);
                    designer.iconlist.setUIValue(xid);
                    if (!children || children.length == 0) {
                        obj = {children: [item]}
                    }
                    designer._designable(go);
                    designer.selectWidget([xid]);
                },
                undo = function () {
                    designer.iconlist.removeItems([xid]);
                    var ins = designer.getByAlias(citem.alias);
                    ins.destroy(true, true);
                }
            designer.markDirty(undo, redo, "add commponents");
        }
    }
});
