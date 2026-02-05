ood.Class('RAD.FunctionsEditor', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ood_dlg")
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("50em")
                .setHeight("24.166666666666668em")
                .setCaption("$(RAD.designer.Functions Configuration)")
                .setMinBtn(false)
                .setModal(true)
                .beforeClose("_ood_dlg_beforeclose")
            );
            
            host.ood_dlg.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_main")
                .setDock("fill")
                .setLeft("35.833333333333336em")
                .setTop("0.8333333333333334em")
                .setBorderType("inset")
                );
            
            host.ood_main.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"ood_tg_funs")
                .setDirtyMark(false)
                .setShowDirtyMark(false)
                .setLeft("0em")
                .setTop("0em")
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandler(false)
                .setHeader([{
                    "id":"id",
                    "caption":"id",
                    "width":"6em",
                    "type":"input"
                },
                {
                    "id":"desc",
                    "caption":"desc",
                    "width":"10em",
                    "type":"input"
                },
                {
                    "id":"params",
                    "caption":"parameters",
                    "width":"8em",
                    "type":"label",
                    "cellStyle":"cursor:pointer"
                },
                {
                    "id":"actions",
                    "caption":"actions",
                    "width":"8em",
                    "type":"label",
                    "cellStyle":"cursor:pointer"
                },
                    {
                        "id":"function",
                        "caption":"function",
                        "flexSize":true,
                    "width":"10em",
                    "type":"label"
                }])
                .setTagCmds([{
                    "id":"add",
                    "imageClass":"ri ri-add-line",
                    "tips":"Add a function",
                    "location": "right",
                    "pos":"header"
                },
                {
                    "id":"delete",
                    "imageClass":"ri ri-close-line",
                    "tips":"Delete this function",
                    "location": "right",
                    "pos":"row"
                }])
                .setActiveMode("none")
                .setHotRowMode("hide")
                .beforeIniEditor("_ood_ui_treegrid3_beforeeditor")
                .onInitHotRow("_ood_tg_funs_oninithotrow")
                .beforeHotRowAdded("_ood_ui_treegrid3_beforehotrowadded")
                .onCmd("_ood_ui_treegrid3_oncmd")
                .onClickCell("_ood_tg_funs_onclickcell")
                );
            
            append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_div_args")
                .setLeft("0.8333333333333334em")
                .setTop("25.833333333333332em")
                .setWidth("35em")
                .setHeight("12.5em")
                .setTabindex(10)
                .setShadow(true)
                .setBorderType("flat")
            );
            
            host.ood_div_args.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"ood_tg_args")
                .setDirtyMark(false)
                .setShowDirtyMark(false)
                .setDockMargin({
                    "left":0,
                    "top":0,
                    "right":0,
                    "bottom":36
                })
                .setLeft("0em")
                .setTop("0em")
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandlerWidth("2em")
                .setRowHandler(false)
                .setHeader([{
                    "id":"id",
                    "caption":"id",
                    "width":"6em",
                    "type":"input"
                },
                {
                    "id":"type",
                    "caption":"type",
                    "width":"10em",
                    "type":"listbox",
                    "cellStyle":"",
                    "editorListItems":[{
                        "id":"String",
                        "caption":"$(RAD.action.var.String)"
                    },
                    {
                        "id":"Number",
                        "caption":"$(RAD.action.var.Number)"
                    },
                    {
                        "id":"Boolean",
                        "caption":"$(RAD.action.var.Boolean)"
                    },
                    {
                        "id":"Hash",
                        "caption":"$(RAD.action.var.Hash)"
                    },
                    {
                        "id":"Array",
                        "caption":"$(RAD.action.var.Array)"
                    },
                    {
                        "id":"Object",
                        "caption":"$(RAD.action.var.Object)"
                    },
                    {
                        "id":"Function",
                        "caption":"$(RAD.action.var.Function)"
                    },
                    {
                        "id":"Event",
                        "caption":"$(RAD.action.var.Event)"
                    }]
                },
                {
                    "id":"desc",
                    "caption":"desc",
                    "flexSize":true,
                    "width":"10em",
                    "type":"input"
                }])
                .setTagCmds([{
                    "id":"add",
                    "itemClass":"ri ri-add-line",
                    "tips":"Add a function",
                    "pos":"header"
                },
                {
                    "id":"delete",
                    "itemClass":"ri ri-close-line",
                    "tips":"Delete this function",
                    "pos":"row"
                }])
                .setActiveMode("none")
                .setHotRowMode("hide")
                .onInitHotRow("_ood_tg_args_oninithotrow")
                .beforeHotRowAdded("_ood_ui_treegrid3_beforehotrowadded")
                .onCmd("_ood_ui_treegrid3_oncmd")
                .beforeIniEditor("_ood_ui_treegrid3_beforeeditor")
                );
            
            host.ood_div_args.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ood_ui_button14")
                .setLeft("13.333333333333334em")
                .setTop("9.833333333333334em")
                .setWidth("7.5em")
                .setImageClass("ri ri-close-line") // 原"spafont spa-icon-cancel"
                .setCaption("Cancel")
                .onClick("_ood_ui_button14_onclick")
                );
            
            host.ood_div_args.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ood_ui_button15")
                .setLeft("24.166666666666668em")
                .setTop("9.833333333333334em")
                .setWidth("7.5em")
                .setImageClass("ri ri-check-line") // 原"spafont spa-icon-ok"
                .setCaption("OK")
                .onClick("_ood_ui_button15_onclick")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _ood_ui_treegrid3_oncmd:function (profile, row, cmdkey, e, src){
            var ns = this, uictrl = profile.boxing();
            if(cmdkey=="add"){
                uictrl.addHotRow("id");
            }else if(cmdkey=="delete"){
                uictrl.removeRows(row.id);
            }
        },
        _ood_ui_treegrid3_beforeeditor:function(profile, cell, cellNode, pNode, type){
            if(cell._col.id!="id"){
                var idcell=cell._row.cells[0];
                if(!idcell.value){
                    ood.asyRun(function(){
                        if(cell._row)profile.boxing().focusCell(idcell);
                    });
                    return false;
                }
            }
        },
        _ood_ui_treegrid3_beforehotrowadded:function (profile, cellMap, row){
            var ns = this, 
                uictrl = profile.boxing(),
                id = cellMap.id,
                rows = uictrl.getRows();

            if(!id){
                return false;
            }

            for(var i=0, l=rows.length; i<l-1; i++){
                if(rows[i].cells[0].value == id){
                    ood.message("The id must be an unique string.");
                    return false;
                }
            }

            return true;
        },
        _ood_tg_funs_oninithotrow:function (profile){
            return ["","","( )",{caption:this._makeActionCap()},{caption:this._makeFunCap()}];
        },
        _ood_tg_args_oninithotrow:function (profile){
            return ["",{value:"String",caption:"$(RAD.action.var.String)"},""];
        },
        _makeActionCap:function(actions){
            return "&lt;" +  (ood.isArr(actions) ? actions.length : 0)  + " " + ood.adjustRes("$RAD.designer.actions") + ">";  
        },
        _makeFunCap:function(fun){
            return "&lt;" +  ood.adjustRes("$RAD.action."+(fun?"Function":"None")) + ">";  
        },
        _makeParamCap:function(params){
            var ns = this,param,
                str = [];
            for(var i=0,l=params.length;i<l;i++){
                str.push(params[i].id);
            }
            return "( "+str.join(", ")+" )";
        },
        _ood_ui_button14_onclick:function (profile, e, src, value){
            var ns = this;
            ns.ood_div_args.getRoot().hide();
        },
        events:{
            "onReady":"_page_onready",
            "onModulePropChange":"_onModulePropChange"
        },
        _page_onready:function (module, threadid){
            module.ood_div_args.setVisibility("hidden");
        },
        _onModulePropChange: function(module){
            var prop = module.properties;
            if(!('type' in prop))return;

            var type = prop.type == 'page' ? 'page' : 'global',
                funs = [];
            module._initFuns(type, prop.functions);
        },
        _initFuns:function(type, functions){
            var ns=this,
                cells,
                funs = [];
            
            ns.ood_tg_funs.removeAllRows();
            
            if(type=='page')
                ns.ood_dlg.setCaption("$(RAD.designer.Module Functions)");
            
            if(functions && ood.isHash(functions) && !ood.isEmpty(functions)){
                ood.each(functions, function(v, k){
                    cells=[];
                    cells[0]=k;
                    cells[1]=v.desc||"";
                    cells[2]={
                        tagVar:ood.clone(v.params),
                        value: ns._makeParamCap(v.params)
                    };
                    var fun=null;

                    ood.arr.each(v.actions, function(o){
                        if(o.script){
                           eval("var cfun="+ o.script);
                            fun=cfun;return false;
                        }
                    });

                    ood.arr.each(v.actions, function(o){
                        if(typeof o == 'function'){
                            fun=o;return false;
                        }
                    });
                    var actions = v && ood.clone(v.actions,function(o,i){
                        return typeof o != 'function'
                    });
                    cells[3]={
                        tagVar:{
                            actions: actions,
                            "return": v && v['return']
                        },
                        value: ns._makeActionCap(actions)
                    };
                    cells[4]={
                        tagVar:fun,
                        value: ns._makeFunCap(fun)
                    };
                    funs.push(cells);
                });
            }
            ns.ood_tg_funs.insertRows(funs);
            ns.ood_tg_args.updateGridValue();
        },
        _getArgsVar:function(type){
            var r;
            switch(type){
                case "String": r='';break;
                case "Number": r='';break;
                case "Boolean": r='';break;
                case "Hash": r={};break;
                case "Array": r=[];break;
                case "Object": r={};break;
                case "Function": r='';break;
                case "Event": r='e';break;
            }
            return r;
        },
        _ood_tg_funs_onclickcell:function (profile, cell, e, src){
            var ns = this, 
                uictrl = profile.boxing(),
                col = uictrl.getColByCell(cell),
                grid2 = ns.ood_tg_args;
            
            if(col.id!="id"){
                var idcell=cell._row.cells[0];
                if(!idcell.value){
                    ood.asyRun(function(){
                        if(cell._row)profile.boxing().focusCell(idcell);
                    });
                    return false;
                }
            }

            if(col.id=='params'){
                ns._cell_in_pop = cell;

                var editor = ns.ood_div_args,
                    root = editor.getRoot(),
                    rows=[];
                grid2.removeAllRows();
                
                if(cell.tagVar && ood.isArr(cell.tagVar) && cell.tagVar.length){
                    ood.arr.each(cell.tagVar,function(o){
                        rows.push([o.id, o.type||"String", o.desc || ""]);
                    });
                }
                grid2.setRows(rows);
                
                root.pop(src);
            }else if(col.id=='actions'){
                var actionConf = cell.tagVar,
                    params = cell._row.cells[2].tagVar,
                    page = ns.properties.page,
                    isCanvas=true;
                ood.showCom("RAD.ActionsEditor",function(){
                    var prop=this.properties={};

                    // some prop
                    prop.parent=page;
                    prop.actionConf = actionConf;
                    prop.cls=page._cls;
                    prop.className = page._className;

                    //prop.target=target;
                    //prop._eventkey
                    //prop._selected
                    //prop._cap

                    //get all page
                    if(CONF.getClientMode() == "project" && SPA.treebarCls){
                        prop.pages = ood.clone(SPA.treebarCls.getItems(),true);
                    }
                    //get all widgets
                    var items=[];
                    if(prop.cls){
                        var getCap=function(type,profile){
                            var prop=profile.properties,
                                cls = profile.key.split(".").pop(),
                                cap;
                             if(type=="module"){
                                cap = /*"$(RAD.action.Module) - " + */profile.getAlias() + " ( "+ profile.key +" )";
                            }else{
                                cap = /*"$(RAD.action.Control) - " + */profile.alias + " ( "+cls+" )";
                                if(prop.name || prop.desc)cap += " <br />[ " + (prop.name ||"") + (prop.name?" - ":"") + (prop.desc||"")  +" ]";
                                if(prop.caption||prop.labelCaption) cap += " [ "+  (prop.caption||prop.labelCaption)  +" ]";
                            }
                            return cap;
                        };
                        var moduleHash={}, 
                            fun = function(profile, items, map){
                                var self=arguments.callee, 
                                    type = (profile['ood.Module'] || (profile.moduleClass&&profile.moduleXid)) ?"module":"control";
                                
                                profile = (type=="module"&&!profile['ood.Module'] ) ? ood.Module.getInstance(profile.moduleClass, profile.moduleXid) : profile;

                                var cap = getCap(type, profile),
                                    t=type=="module"?{imageClass:'ri ri-file-text-line'}:map[profile.box.KEY], tt,
                                    z;
                                var item = {
                                    id:profile.alias, 
                                    caption:cap,
                                    image:t&&t.image||'', 
                                    imagePos:t&&t.imagePos||'',
                                    imageClass:t&&t.imageClass||'',
                                    "0":profile,
                                    _type:type
                                };
                                items.push(item);

                                if(type=="module"){
                                    return;
                                }

                                if(profile.children && profile.children.length){
                                    var sub=[];
                                    item.sub = sub;
                                    ood.arr.each(profile.children,function(o){
                                        self.call(null, o[0], sub, map);
                                    });
                                }
                            };

                        fun(page.canvas.get(0), items, CONF.mapWidgets);

                        if(!items[0].sub)items[0].sub=[];

                        ood.each(page.$host,function(ins,i){
                            if(i=="$inDesign"||!ins['ood.absBox']||!ins.n0)return;

                            var profile=ins.get(0), 
                                map=CONF.mapWidgets,item,t;
                            if( profile['ood.Module'] ? profile.getUIComponents().isEmpty() : profile.box['ood.UI'] ? profile.box.$initRootHidden : true ){
                                if(-1==ood.arr.subIndexOf(items[0].sub,"id", profile.alias)){
                                    var type = profile['ood.Module']?"module":"control",
                                        t=type=="module"?{imageClass:'ri ri-file-text-line'}:map[profile.box.KEY], tt,
                                        item={
                                            id: profile.alias, 
                                            caption: getCap(type, profile),
                                            image:t&&t.image||'', 
                                            imagePos:t&&t.imagePos||'',
                                            imageClass:t&&t.imageClass||'',
                                            "0": profile,
                                            _type: type
                                        };
                                    items[0].sub.push(item);
                                }
                            }
                        });
                        prop.controls = items[0].sub || [];
                    }

                    // Event paramters
                    prop.argsdoc=[];
                    prop.args=[];
                    prop.argsvar=[];
                    ood.arr.each(params,function(o){
                        prop.args.push(o.id);
                        prop.argsdoc.push(o.id + " : " + o.type + (o.desc?", " + o.desc:""));
                        prop.argsvar.push(ns._getArgsVar(o.type));
                    });

                    // make dirty ,but cant undo/redo
                    this.setEvents({onDirty:function(target, evname, evs){
                        var hash={tagVar:{}};
                        if(evs){
                            if(ood.isArr(evs)){
                                hash.tagVar.actions=evs;
                            }else{
                                hash.tagVar=evs;
                            }
                        }else{
                            hash.tagVar = evs;
                        }
                        hash.caption = ns._makeActionCap(hash.tagVar && hash.tagVar.actions);
                        uictrl.updateCell(cell, hash);
                    }});

                });
            }else if(col.id=='function'){
                // _makeFunCap
                var fun = cell.tagVar, 
                    body = (fun?ood.fun.body(fun):"").replace(/^(\s*\n)*/,'').replace(/(\n\s*)*$/,''),
                    funName = cell._row.cells[0].value,
                    desc = cell._row.cells[1].value,
                    params = cell._row.cells[2].tagVar,  paramS=[],
                    text;

                ood.arr.each(params,function(h){
                    paramS.push( h.id + "/*" +h.type + (h.desc?": " + h.desc:"") + "*/");
                });

                text = ("// " + funName + (desc?": "+desc:"") + "\n")  
                    + ("// [this]: "+(ns.properties.type == 'page' ? 'the module' : 'global')+"\n")
                    +   "function(" + paramS.join(", ") + "){\n"
                    +       body
                    +   "\n}";

                ood.getModule('funEditor',function(){
                    this.host = ns;
                    this.setProperties({
                        'imageClass':'ri ri-code-line',
                        'caption': "FUNCTION => "+funName,
                        'text': text,
                        head_readonly:3,
                        tail_readonly:1,
                        'onOK':function(obj){
                            var code=obj.properties.result.code;
                            if(ood.fun.body(code).replace(/\s/g,'')=='')code=null;
                            var hash={
                                tagVar:code?ood.unserialize(code):null,
                                caption:ns._makeFunCap(code)
                            };
                            uictrl.updateCell(cell, hash);
                        }
                    },true);
                    this.show();
                });
            }
        },
        _ood_ui_button15_onclick:function (profile, e, src, value){
            var ns = this, 
                grid1 = ns.ood_tg_funs,
                grid2 = ns.ood_tg_args,
                hotcell = ns._cell_in_pop,
                rows = grid2.getRows("data"), 
                cells, param, 
                str = [],
                params = [];
            for(var i=0,l=rows.length;i<l;i++){
                cells = rows[i].cells;
                if(cells[0].value){
                    param = {
                        id: cells[0].value,
                        type: cells[1].value ,
                        desc: cells[2].value||""
                    };
                    str.push(param.id);
                    params.push(param);
                }
            }
            grid1.updateCell(hotcell,{
                value: ns._makeParamCap(params),
                tagVar : params
            },false,false);
            delete ns._cell_in_pop;

            // hide the editor
            ns.ood_div_args.getRoot().hide();
        },
        _ood_dlg_beforeclose:function (profile){
            this.fireData();
        },
        fireData:function(force){
            var ns=this, grid = ns.ood_tg_funs, cells, funs={};
            if(force || ns.ood_tg_funs.isDirtied()){
                var rows = ns.ood_tg_funs.getRows(), actions, fun,rtn;
                if(rows.length){
                    ood.arr.each(rows, function(row,i){
                        cells = row.cells;
                        actions = ood.get(cells[3], ['tagVar','actions']);
                        fun = cells[4].tagVar;
                        if(!actions && !fun)return;

                        if(!actions)actions=[];
                        actions.push(fun);

                        rtn = cells[3].tagVar && cells[3].tagVar['return'];
                        if(actions && ood.isArr(actions) && actions.length){
                            funs[cells[0].value]={
                                desc:cells[1].value,
                                params:ood.clone(cells[2].tagVar)||[],
                                actions:ood.clone(actions)||{}
                            };
                            if(!fun && ood.isSet(rtn))funs[cells[0].value]["return"] = rtn;
                        }
                    });
                }
                ns.fireEvent("onOK",[funs]);
            }
            return funs;
        }
    }
});
