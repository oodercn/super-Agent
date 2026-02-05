
ood.Class('RAD.expression.ExpressionEditor', 'ood.Module',{
    Instance:{
        initialize : function(){ },
        Dependencies:[],
        Required:[
            "ood.Module.JSONEditor"
        ],
        properties : {
            "path":"form/myspace/versionspace/projectManager/0/RAD/expression/ExpressionEditor.cls",
            "personId":"devdev",
            "personName":"devdev",
            "projectName":"projectManager"
        },
        events:{
            "onRender":{
                "actions":[
                    {
                        "args":[],
                        "desc":"函数",
                        "script":"_page_onrender",
                        "type":"page"
                    }
                ]
            }
        },
        functions:{},
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            append(
                ood.create("ood.UI.Block")
                    .setHost(host,"content")
                    .setDock("fill")
                    .setBorderType("none")
            );

            host.content.append(
                ood.create("ood.UI.Layout")
                    .setHost(host,"ood_ui_layout19")
                    .setType("vertical")
                    .setItems([
                        {
                            "cmd":true,
                            "folded":false,
                            "id":"before",
                            "locked":false,
                            "min":300,
                            "pos":"before",
                            "size":350,
                            "hidden":false
                        },
                        {
                            "id":"main",
                            "min":10,
                            "size":200
                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_layout19.append(
                ood.create("ood.UI.Tabs")
                    .setHost(host,"ood_ui_tabs11")
                    .setName("centerdown")
                    .setItems([
                        {                            "caption":"结果",                            "hidden":false,                            "id":"result",                            "imageClass":"ri-eye-line"                        },                        {                            "caption":"动态参数",                            "hidden":false,                            "id":"params",                            "imageClass":"ri-calendar-line"                        },                        {                            "caption":"绑定视图",                            "hidden":false,                            "id":"viewbind",                            "imageClass":"ri-network-line"                        },                        {                            "caption":"日志",                            "hidden":false,                            "id":"log",                            "imageClass":"ri-time-line"                        }
                    ])
                    .setLeft("0em")
                    .setTop("0em")
                    .setValue("params"),
                "main"
            );

            host.ood_ui_tabs11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_group13")
                    .setDock("fill")
                    .setConLayoutColumns(2),
                "params"
            );

            host.ood_ui_group13.append(
                ood.create("ood.UI.Panel")
                    .setHost(host,"ood_ui_panel37")
                    .setLeft("19.58334em")
                    .setWidth("45.75em")
                    .setHeight("17.083333333333332em")
                    .setCaption("参数编码")
                    .setBorderType("none")
            );

            host.ood_ui_panel37.append(
                ood.create("ood.UI.Input")
                    .setHost(host,"ood_reqtext")
                    .setName("ood_reqtext")
                    .setDisabled(true)
                    .setDock("fill")
                    .setLeft("6.667em")
                    .setTop("4.1667em")
                    .setHeight("10em")
                    .setMultiLines(true)
            );

            host.ood_ui_group13.append(
                ood.create("ood.UI.Div")
                    .setHost(host,"ood_ui_div117")
                    .setDock("left")
                    .setTop("0.8334em")
                    .setWidth("30.75em")
                    .setHeight("8.334em")
            );

            host.ood_ui_div117.append(
                ood.create("ood.UI.Stacks")
                    .setHost(host,"ood_ui_stacks17")
                    .setItems([
                        {
                            "caption":"动态参数",
                            "hidden":false,
                            "id":"b"
                        },
                        {
                            "caption":"固定参数",
                            "hidden":false,
                            "id":"c",
                            "imageClass":""
                        }
                    ])
                    .setLeft("9.58334em")
                    .setTop("0em")
                    .setWidth("45.75em")
                    .setHeight("21em")
                    .setValue("b")
            );

            host.ood_ui_stacks17.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_reqds")
                    .setName("ood_reqds")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("4em")
                    .setHeader([
                        {
                            "caption":"$(RAD.api_dlg.Datasource)",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"source",
                            "readonly":true,
                            "type":"label",
                            "width":"6.666666666666667em"
                        },
                        {
                            "caption":"$(RAD.api_dlg.To Data Path)",
                            "colResizer":true,
                            "editable":true,
                            "flexSize":true,
                            "hidden":false,
                            "id":"path",
                            "readonly":true,
                            "type":"input",
                            "width":"6.666666666666667em"
                        },
                        {
                            "caption":"参数用途",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"desc",
                            "readonly":true,
                            "type":"input",
                            "width":"6.666666666666667em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onChange([
                        "_ood_reqds_onchange"
                    ])
                    .onRowSelected([
                        "_ontrows"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
                    .beforeCellUpdated([
                        "_acu"
                    ])
                    .afterCellUpdated([
                        "_ood_reqds_bcellupdated"
                    ]),
                "b"
            );

            host.ood_ui_stacks17.append(
                ood.create("ood.Module.JSONEditor", "ood.Module")
                    .setHost(host,"m_reqparams")
                    .setProperties({
                        "className":"ood.Module.JSONEditor"
                    })
                    .setEvents({
                        "onchange":[
                            "_m_reqparams_onchange"
                        ]
                    }),
                "c"
            );

            host.ood_ui_tabs11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_group23")
                    .setDock("fill")
                    .setLeft("7.5em")
                    .setWidth("8.334em")
                    .setHeight("19.1667em"),
                "viewbind"
            );

            host.ood_ui_group23.append(
                ood.create("ood.UI.Panel")
                    .setHost(host,"ood_ui_panel50")
                    .setDock("left")
                    .setTop("1.667em")
                    .setWidth("26em")
                    .setHeight("17.08333em")
                    .setCaption("视图")
                    .setBorderType("flat")
            );

            host.ood_ui_panel50.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_rsptarget")
                    .setName("ood_rsptarget")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("4em")
                    .setHeader([
                        {
                            "caption":"$(RAD.api_dlg.Data Path)",
                            "colResizer":true,
                            "editable":true,
                            "flexSize":true,
                            "hidden":false,
                            "id":"path",
                            "readonly":true,
                            "type":"input",
                            "width":"6em"
                        },
                        {
                            "caption":"$(RAD.api_dlg.Target)",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"source",
                            "readonly":true,
                            "type":"label",
                            "width":"10em"
                        },
                        {
                            "caption":"参数用途",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"desc",
                            "readonly":true,
                            "type":"label",
                            "width":"10em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onRowSelected([
                        "_ontrows2"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
                    .beforeCellUpdated([
                        "_acu"
                    ])
            );

            host.ood_ui_group23.append(
                ood.create("ood.UI.Panel")
                    .setHost(host,"ood_ui_panel49")
                    .setLeft("20.41667em")
                    .setTop("1.667em")
                    .setWidth("24.5em")
                    .setHeight("17.08333em")
                    .setCaption("$(RAD.api_dlg.Response Callback)")
            );

            host.ood_ui_panel49.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"ood_rspfun")
                    .setName("ood_rspfun")
                    .setLeft("0em")
                    .setTop("0em")
                    .setSelMode("multibycheckbox")
                    .setEditable(true)
                    .setRowHandlerWidth("2em")
                    .setHeader([
                        {
                            "caption":"$(RAD.api_dlg.Callback Type)",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"type",
                            "readonly":true,
                            "type":"label",
                            "width":"6em"
                        },
                        {
                            "caption":"$(RAD.api_dlg.Callback)",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"source",
                            "readonly":true,
                            "type":"label",
                            "width":"12em"
                        }
                    ])
                    .setTreeMode("none")
                    .setValue("")
                    .onRowSelected([
                        "_ontrows"
                    ])
                    .beforeRowActive([
                        "_tg_beforerowactive"
                    ])
            );

            host.ood_ui_tabs11.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"resultgrid")
                    .setName("resultgrid")
                    .setDock("fill")
                    .setLeft("15em")
                    .setTop("10em"),
                "result"
            );

            host.resultgrid.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block47")
                    .setDock("fill")
                    .setLeft("0em")
                    .setTop("0em")
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block48")
                    .setDock("bottom")
                    .setHeight("2.5em")
            );

            host.ood_ui_block48.append(
                ood.create("ood.UI.PageBar")
                    .setHost(host,"ood_ui_pagebar1")
                    .setCaption("")
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.TreeGrid")
                    .setHost(host,"result")
                    .setName("result")
                    .setLeft("0em")
                    .setTop("0em")
                    .setRowNumbered(true)
                    .setHeader([
                        {
                            "caption":"col1",
                            "colResizer":true,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"a",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col2",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"b",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col3",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":false,
                            "hidden":false,
                            "id":"c",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        },
                        {
                            "caption":"col4",
                            "colResizer":false,
                            "editable":false,
                            "flexSize":true,
                            "hidden":false,
                            "id":"d",
                            "readonly":true,
                            "type":"input",
                            "width":"8em"
                        }
                    ])
            );

            host.ood_ui_block47.append(
                ood.create("ood.UI.ToolBar")
                    .setHost(host,"ood_ui_toolbar16")
                    .setItems([
                        {
                            "caption":"common",
                            "hidden":false,
                            "id":"common",
                            "sub":[
                                {
                                    "caption":"$RAD.widgets.esd.buttonnew",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"new",
                                    "imageClass":"ri-calendar-event-line",
                                    "iconFontSize":"1.333em",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"$RAD.widgets.esd.buttondelete",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"delete",
                                    "imageClass":"ri-close-line",
                                    "iconFontSize":"1.333em",
                                    "position":"absolute"
                                },
                                {
                                    "caption":"$RAD.widgets.esd.buttonreload",
                                    "hidden":false,
                                    "iconFontSize":"",
                                    "id":"reload",
                                    "imageClass":"ri-loader-line",
                                    "iconFontSize":"1.333em",
                                    "position":"absolute"
                                }
                            ]
                        }
                    ])
            );

            host.ood_ui_layout19.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"ood_ui_block63")
                    .setDock("fill")
                    .setLeft("13.333333333333334em")
                    .setTop("5.833333333333333em"),
                "before"
            );

            host.ood_ui_block63.append(
                ood.create("ood.UI.Block")
                    .setHost(host,"editor")
                    .setName("editor")
                    .setDock("fill")
                    .setLeft("2.5em")
                    .setTop("12.5em")
            );

            host.editor.append(
                ood.create("ood.UI.MenuBar")
                    .setHost(host,"ood_ui_menubar2")
                    .setItems([
                        {
                            "caption":"数据源",
                            "disabled":false,
                            "hidden":false,
                            "id":"data",
                            sub:[
                                {
                                    "caption":"JSON文件",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadJson"
                                },
                                {
                                    "caption":"Execl文件",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadExecl"
                                },
                                {
                                    "caption":"数据库",
                                    "disabled":false,
                                    "hidden":false,
                                    "id":"loadDb"
                                },
                            ]
                        },
                        {
                            "caption":"公式",
                            "disabled":false,
                            "hidden":false,
                            "id":"editor"
                        },
                        {
                            "caption":"模板",
                            "disabled":false,
                            "hidden":false,
                            "id":"temp"
                        },
                        {
                            "caption":"工具",
                            "disabled":false,
                            "hidden":false,
                            "id":"tool"
                        }
                    ])
                    .setTop("2.5em")
            );

            return children;
            // ]]Code created by JDSEasy RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        },
        setDataToEditor:function (host, cls, api) {
            var ns = this,
                prop = api,
                arr, v, values, t, m;
            this.$cls = cls;
            this.$host = host;
            this.$api = api;
            ns._oldprop = prop;
            ns._host = host;

            // requestDataSource
            (function () {
                arr = [];
                v = {};
                values = [];
                ood.arr.each(prop.requestDataSource, function (o) {
                    v[o.type + "-" + o.name] = o.path;
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.DataBinder'])
                        return;
                    t = db.getName();
                    m = "databinder" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "databinder",
                        cells: [t, v[m] || "", "自定义"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;

                    t = db.getAlias();

                    m = "pagebar" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "pagebar",
                        cells: [t, v[m] || "", "分页参数"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();

                    m = "treeview" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treeview",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();

                    m = "gallery" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "gallery",
                        cells: [t, v[m] || "", "当前选中"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();

                    m = "module" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "module",
                        cells: [t, v[m] || t, "子模块"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();

                    m = "treegrid" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treegrid",
                        cells: [t, v[m] || "", "当前选中行"]
                    });
                });


                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "form" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "form",
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
                    m = "databinder" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "databinder",
                        cells: [v[m] || "", t, "数据绑定"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeGrid'])
                        return;
                    t = db.getAlias();
                    m = "treegrid" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treegrid",
                        cells: [v[m] || "data", t, "填充行数据"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Dialog'])
                        return;
                    t = db.getAlias();
                    m = "component" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "component",
                        cells: [v[m] || "data", t, "动态装载"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Block'])
                        return;
                    t = db.getAlias();
                    m = "component" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "component",
                        cells: [v[m] || "data", t, "动态装载"]
                    });
                });


                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.TreeView'])
                        return;
                    t = db.getAlias();
                    m = "treeview" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "treeview",
                        cells: [v[m] || "data", t, "装载树节点"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.Gallery'])
                        return;
                    t = db.getAlias();
                    m = "gallery" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "gallery",
                        cells: [v[m] || "data", t, "装载数据"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.SVGPaper'])
                        return;
                    t = db.getAlias();
                    m = "svgpagper" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "svgpaper",
                        cells: [v[m] || "data", t, "填充图形"]
                    });
                });
                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.UI.PageBar'])
                        return;
                    t = db.getAlias();
                    m = "pagebar" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "pagebar",
                        cells: [v[m] || "size", t, "设置最大记录数"]
                    });
                });

                ood.each(host, function (db) {
                    if (!db || !db.Class || !db.Class['ood.Module'])
                        return;
                    t = db.getName();
                    m = "module" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "module",
                        cells: [v[m] || 'data.' + t, t, "填充子组件数据"]
                    });
                });


                ood.each(host, function (ui) {
                    if (!ui || !ui.Class || !ui.Class['ood.absContainer'] || ui.getFormElements().isEmpty())
                        return;
                    t = ui.getAlias();
                    m = "form" + "-" + t;

                    if (m in v) values.push(m);

                    arr.push({
                        id: m,
                        bindertype: "form",
                        cells: [v[m] || "data", t, "填充表单数据"]
                    });
                });
                m = "log-console.log";
                arr.push({
                    id: m,
                    bindertype: "log",
                    cells: [v[m] || "", "console.log"]
                });
                if (m in v) values.push(m);

                m = "alert-alert";
                arr.push({
                    id: m,
                    bindertype: "alert",
                    cells: [v[m] || "", "alert"]
                });
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

                ns.ood_rspfun.setRows(arr).setValue(values.join(";"));
            }());

            // queryArgs
            ns.m_reqparams.setProperties("value", prop.queryArgs);
            // here, must give prop.queryArgs
            ns.updateRequestData(prop.queryArgs, prop.requestDataSource);
        },
        getExpression:function () {
            var ns = this;
            return ns.editor.getValue();
        },
        _ontrows2:function (profile, row, e, src, type) {
            //if (type == -1)
            //    profile.boxing().updateCell(row.cells[0], {value: ""}, false, false);
            this.updateRequestData();
        },
        setResult:function (data) {
            var ns = this;
            ns.ood_ui_tabs11.fireItemClickEvent('result');
            ns.resultgrid.setChildren([data]);
        },
        updateRequestData:function (queryArgs, requestDataSource) {
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
                    switch (o.type) {
                        case "databinder":
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
                        case "pagebar":
                            if (t = ns._host[o.name]) {
                                var pageparams = {pageSize: t.getPageCount(), pageIndex: t.getPage()};
                                if (ood.isHash(v = ood.get(queryArgs, path))) ood.merge(v, pageparams);
                                else ood.set(queryArgs, path, pageparams);
                            }
                            break;

                        case "treegrid":
                            if (t = ns._host[o.name]) {
                                path = (o.path || t.getUidColumn()).split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                            }
                            break;
                        case "treeview":
                            if (t = ns._host[o.name]) {
                                path = (o.path || "").split('.');
                                ood.set(queryArgs, path, t.getUIValue());
                            }
                            break;
                        case "form":
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
                        case "module":
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
        getDataFromEditor:function () {
            var ns = this,
                oldprop = ns._oldprop,
                hash = {
                    requestDataSource: oldprop.requestDataSource,
                    responseDataTarget: oldprop.responseDataTarget,
                    responseCallback: oldprop.responseCallback
                },
                arr, v;


            hash.queryAsync = ns.ood_asyn.getValue();
            hash.autoRun = ns.autoRun.getValue();
            hash.isAllform = ns.isAllform.getValue();


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
                    if (o._selected) {
                        arr.push({
                            type: o.bindertype,
                            name: o.cells[1].value,
                            path: o.bindertype == 'log' || o.bindertype == 'alert' ? "" : o.cells[0].value
                        });
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

            return hash;
        },
        _acu:function (profile, cell, options) {
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
        _page_onrender:function (module, threadid) {
            var ns = this;
            ood.showModule3("RAD.expression.PageEditor", "editor", null, function () {
                //this.setDataToEditor(ns.$host, ns.$cls, ns.$api);

                this.setEvents("onRunExpression", function (profile,expression) {
                    var data=  ood.execGrid(expression, {}, false);
                    ns.setResult(data);
                });
                this.setEvents("onValueChanged", function () {

                });

            })
        },
        _ontrows:function (profile, row, e, src, type) {
            //if (type == -1)
            //   profile.boxing().updateCell(row.cells[1], {value: ""}, false, false);
            this.updateRequestData();
        },
        _tg_beforerowactive:function () {
            return false;
        },
        _callCmd:function(expression) {
            return ood.execGrid(expression, {}, false);
        }  } ,
    Static:{
        "designViewConf":{
            "height":1024,
            "mobileFrame":false,
            "width":1280
        },
        viewStyles:{ }
    }



});
