ood.Class('ood.Module.JSONEditor', 'ood.Module',{
    Instance:{ 
        activate:function(){},
        setValue:function(str){
            var ns=this,
                obj=ood.isStr(str)?str?ood.unserialize(str):false:str,
                rows=ns._json2rows(obj, ns._rootArr = ood.isArr(obj));
         
            ns.tg.setRows(rows).free();
        },
        getValue:function(returnObj){
            var rows=this.tg.getRows();
            var str = this._rows2json(rows, this._rootArr);
            return returnObj?ood.unserialize(str):str;
        },
        getEditor:function(){
            return this.tg;
        },
        iniComponents:function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"tg")
                .setTogglePlaceholder(true)
                .setEditable(true)
                .setInitFold(false)
                .setRowHandler(false)
                .setColSortable(false)
                .setHeader([
                    {
                    "id" : "key",
                    "width" : 100,
                    "type" : "input",
                    "caption" : "key",
                    "editorCacheKey" : "input",
                    "colResizer":true,
                    "flexSize" : true
                },{
                    "id" : "value",
                    "width" : 200,
                    "type" : "textarea",
                    "caption" : "value",
                    "editorCacheKey" : "textarea",
                    "flexSize" : true
                    }
                ])
                .setTagCmds([
                    {
                    "id" : "add",
                    "type" : "text",
                    "caption" : "",
                    "location": "right",
                    "itemClass":"oodcon ood-uicmd-getter",
                    "pos" : "header row",
                    "tips" : "Append a child"
                    },
                    {
                    "id" : "up",
                    "type" : "text",
                    "location": "right",
                    "itemClass":"oodcon ood-icon-arrowtop",
                    "pos" : "row",
                    "tips" : "Add a node to the front of the node" 
                    },
                    {
                    "id" : "down",
                    "type" : "text",
                    "location": "right",
                    "itemClass":"oodcon ood-icon-arrowbottom",
                    "pos" : "row",
                    "tips" : "Add a node at the back of this node"
                    },
                    {
                    "id" : "del",
                    "type" : "text",
                    "location": "right",
                    "itemClass":"oodcon ood-uicmd-close",
                    "pos" : "row",
                    "tips" : "Delete this node"
                    }
                ])
                .setTreeMode("infirstcell")
                .onCmd("_tg_oncmd")
                .beforeRowActive("_tg_beforerowactive")
                .beforeCellUpdated("_tg_beforecellupdated")
                .beforeIniEditor("_tg_beforeIniEditor")
                .onBeginEdit("_tg_onEdit")
                .beforeEditApply("_tg_beforecellapply")
            );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _getCellValue:function(n){
            var ns=this, v;
            try{
                v=ood.str.trim(n);
                //special string
                if(/^'/.test(v) && !ns._isString(v.slice(1))){
                    v=['string', v.slice(1)];
                }else{
                    v=v.replace(/^\s*/,'').replace(/\s*$/,'');
                    v= v=='null'? ['null','null'] :
                      //number
                        ood.isFinite(v) ? ['number',v]  :
                      //reg
                        /^\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*$/.test(v) ? ['regexp', v]  :
                      //bool
                        /^(true|false)$/.test(v) ? ['boolean',v.toLowerCase()] :
                      //date
                        /^new Date\([0-9 \,]*\)$/i.test(v) ? ['date', ood.serialize(ood.unserialize(v))] :
                      //function
                        /^((\s*function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)(\{([^\{\}]*)\}))\s*$/i.test(v) && ood.isFun(ood.unserialize(v)) ? ['function',v] :
                      //hash
                        /^\{[\s\S]*\}$/.test(v) && ood.isHash(ood.unserialize(v)) ? ['hash',ood.stringify(ood.unserialize(v))] :
                      //array
                        /^\[[\s\S]*\]$/.test(v) && ood.isArr(ood.unserialize(v)) ? ['array', ood.stringify(ood.unserialize(v))] :
                      ['string', n];
                  }
              }catch(e){
                  v=null;
              }
              if(v[0]=='string'){
                if(v[1]===false)
                    return null;
                v[1]=ood.stringify(v[1]);
            }
            if(v[1]==="false" && v[0]!='string')
                v[0]='boolean';
            return v;
        },
        _json2rows:function(obj,array,rows){
            var ns=this, me=arguments.callee;
            if(!rows)rows=[];
            if(obj){
                ood.each(obj,function(o,i){
                    if(!obj.hasOwnProperty(i))return;
                    var row={},type=ns._getType(o);
                    i={value:array?'['+i+']':i,readonly:array};

                    if(type=='hash'){
                        row.sub=[];
                        row.cells=[i,{value:'{...}'},''];
                        me.call(ns, o,false,row.sub);
                    }else if(type=='array'){
                        row.sub=[];
                        row.cells=[i,{value:'[...]'},''];
                        me.call(ns, o,true,row.sub);
                    }else{
                        //ns._getType(o);
                        row.cells=[i,ood.stringify(o),''];
                    }
                    row._type=type;
                    row.caption="";
                    rows.push(row);
                });
            }
            return rows;
        },
        _getType:function(o){
            return o===null?null:
                    ood.isStr(o)?'string':
                    ood.isNumb(o)?'number':
                    ood.isHash(o)?'hash':
                    ood.isArr(o)?'array':
                    ood.isBool(o)?'boolean':
                    ood.isDate(o)?'date':
                    ood.isReg(o)?'regexp':
                    ood.isFun(o)?'function':
                    'undefined';
        },
        _rows2json:function(arr,array){
            var me=arguments.callee,
                a=[], key,value;
            ood.arr.each(arr, function(o){
                key=((typeof o.cells[0]=='object')?o.cells[0].value:o.cells[0]);
                if(o._type=='hash')
                    value=me(o.sub);
                else if(o._type=='array')
                    value=me(o.sub, true);
                else
                    value=(typeof o.cells[1]=='object')?o.cells[1].value:o.cells[1];
                if(array)
                    a.push(value);
                else
                    a.push('"'+key + '":' + value);
            });
            return array ? '['+a.join(',')+']' : '{'+a.join(',')+'}';
        },
        _tg_onEdit:function(profile, obj, editor, type){
            if(profile.properties.multiLineValue)
                editor.getSubNode("INPUT").scrollTop(0);
            this.fireEvent("onEdit", [obj._col.id, editor]);
        },
        // for value 
        _tg_beforeIniEditor:function(profile, obj, cellNode, pNode, type){
            var ns=this;
            if(type!='cell')return;
            if(obj._col.id!='value')return;

            var type=obj._row._type;
            if(type=='hash'||type=='array'){
                var str=this._rows2json(obj._row.sub, type=='array');
                if(ood.Coder)str = ood.Coder.formatText(str);
                obj.$editorValue = str;
            }else if(type=='string'){
                var v=ood.unserialize(obj.value);
                      //number
                if(  !ns._isString(v) ){
                    obj.$editorValue = "'" + v;
                }else{
                    obj.$editorValue = v;
                }
            }
        },
        _isString:function(v){
            return !(v=='undifined' || v=='null' || v=='NaN' ||
                       ood.isFinite(v) ||
                      //reg
                        /^\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*$/.test(v)  ||
                      //bool
                        /^(true|false)$/.test(v)  ||
                      //date
                        /^new Date\([0-9 \,]*\)$/i.test(v)  ||
                      //function
                        (/^((function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)(\{([^\{\}]*)\}))$/i.test(v) && ood.isFun(ood.unserialize(v)))  ||
                      //hash
                        (/^\{[\s\S]*\}$/.test(v) && ood.isHash(ood.unserialize(v))) ||
                      //array
                        (/^\[[\s\S]*\]$/.test(v)  ) && ood.isArr(ood.unserialize(v))) ;
        },
        _tg_beforecellapply:function(profile, cc, options, editor, tag){
            if(tag!=='onchange')return false;
        },
        _tg_beforecellupdated:function (profile, cell, options) {
            var ns=this,
                map={'hash':1,'array':2},
                row=cell._row,
                rowId=row.id,
                tg=profile.boxing();
            if(cell._col.id=='value'){
                var  va=this._getCellValue(options.value);
                if(!va){
                    alert('Text format is not valid!');
                    return false;
                }else{
                    var ops={};
                    options.value=va[1];
                    
                    if(map[va[0]]){
                        ops.sub=this._json2rows(ood.unserialize(va[1]),va[0]=='array');
                        options.caption=va[0]=='hash'?'{...}':'[...]';
                    }else{
                        if(row.sub)ops.sub=null;
                    }
                    ood.asyRun(function(){
                        if(tg.isDestroyed())return;
                        tg.updateRow(rowId, ops);
                        // must get
                        row = tg.getRowbyRowId(rowId);
                        if(row)row._type=va[0];

                        ns.fireEvent("onchange", [ns]);
                    },100);
                }
            }else{
                if(!/^"(\\.|[^"\\])*"$/.test('"'+options.value+'"')){
                    alert('Text format is not valid!');
                    return false;
                }
                ood.asyRun(function(){
                    ns.fireEvent("onchange", [ns]);
                },100);
            }
        },
        _tg_beforerowactive:function(){
            return false;
        },
        _tg_oncmd:function (profile, row, cmdkey, e, src){
            var ns = this, 
                tg = profile.boxing(),
                type = row ? row._type : ns._rootArr ? 'array': 'hash',
                ptype, prow, nid;

            if(row && row._pid) {
                prow = profile.rowMap[row._pid];
                ptype = prow&&prow._type;
            }else{
                prow = {sub:profile.properties.rows};
                ptype = ns._rootArr ? 'array': 'hash';
            }
            switch(cmdkey){
                case 'add': 
                    nid=ood.stamp();
                    if(row){
                        if(type=="array"||type=="hash"){
                            tg.insertRows([{id:nid, cells:[{value:type=='array'?'[index]':ood.rand(),readonly:type=='array'},'null','']}],row.id);
                        }else{
                            var id=row.id;
                            ood.confirm("Hash or Array", "Modify this node as an Hash or Array?",function(){
                                tg.updateCellByRowCol(id, "value", "{"+ood.rand()+":"+row.cells[1].value+"}", false, true);
                                ood.asyRun(function(){
                                    tg.editCellbyRowCol(id, "value");
                                },200);
                            },function(type){
                                if(type=='close')return;
                                var id=row.id;
                                tg.updateCellByRowCol(id, "value", "["+row.cells[1].value+"]", false, true);
                                ood.asyRun(function(){
                                    tg.editCellbyRowCol(id,"value");
                                },200);
                            },'As a Hash','As an Array');
                            return ;
                        }
                    }else{
                        tg.insertRows([{id:nid, cells:[{value:type=='array'?'[index]':ood.rand(),readonly:type=='array'},'null','']}]);
                    }
                    break;
                case 'up': 
                     nid=ood.stamp();
                    tg.insertRows([{id:nid, cells:[{value:ptype=='array'?'[index]':ood.rand(),readonly:ptype=='array'},'null','']}],null,row.id,true);
                    break;
                case 'down':
                     nid=ood.stamp();
                    tg.insertRows([{id:nid, cells:[{value:ptype=='array'?'[index]':ood.rand(),readonly:ptype=='array'},'null','']}],null,row.id,false);
                    break;
                case 'del': 
                   // ood.confirm('confirm','Do you want to delete this node?',function(){
                          tg.removeRows([row.id]);
                  //  });
                    break;
            }
            if(row && type=='array'){
                // re index for array
                ood.arr.each(row.sub, function(row, i){
                    var cell=row.cells[0];
                    profile.boxing().updateCell(cell, {caption:'['+i+']'});
                });
            }
            else if(prow && ptype=='array'){
                // re index for array
                ood.arr.each(prow.sub, function(row, i){
                    var cell=row.cells[0];
                    profile.boxing().updateCell(cell, {caption:'['+i+']'});
                });
            }
            if( nid ){
                ood.asyRun(function(){
                    tg.editCellbyRowCol(nid+'', ptype=='array'?"value":"key");
                });
            }
            ood.asyRun(function(){
                ns.fireEvent("onchange", [ns]);
            },100);
        },
        events:{
            afterIniComponents:function(module){
                var prop=module.properties;
                module.tg.updateHeader("key", ood.adjustRes(prop.keyCaption)||"key");
                module.tg.updateHeader("value", ood.adjustRes(prop.valueCaption)||"value");
                module.tg.updateHeader("value", {type:prop.multiLineValue?'textarea':'input'});

                if('value' in prop) module.setValue(prop.value);
                if(('tg' in module) && ('notree' in prop) && prop.notree){
                    module.tg.setTreeMode('none');
                    var cmds = module.tg.getTagCmds();
                    cmds[0].tag="header";
                }
            }
        },
        propSetAction:function(prop){
            var module=this;
            if(module._innerModulesCreated && module.tg){
                if('keyCaption' in prop) module.tg.updateHeader("key", ood.adjustRes(prop.keyCaption)||"");
                if('valueCaption' in prop) module.tg.updateHeader("value", ood.adjustRes(prop.valueCaption)||"");
                if('multiLineValue' in prop) module.tg.updateHeader("value", {type:prop.multiLineValue?'textarea':'input'});

                if('value' in prop) module.setValue(prop.value);
            }
        }
    },
    Static:{
        $DataModel:{
            keyCaption:"key",
            valueCaption:"value",
            multiLineValue:true,
            notree:false
        },
        $EventHandlers:{
            onchange:function(module/*ood.Module, the current module*/){},
            onEdit:function(column/*String, the column id*/, editor/*the editor*/){}
        }
    }
});