/*
An editor for js EUSUI class or normal js file
*/
ood.Class('RAD.JSEditor', 'ood.Module',{
    Instance:{
        events:{onReady:"_onready",onRender:"_onRender",onDestroy:"_ondestroy"},
        _onready:function(){
            var items=
                [{
                    "id":"fun",
                    "sub":[
                        {"id":"searchreplace","caption":"$RAD.pageEditor.searchreplace","imageClass":"ri-search-line","type":"button","tips":"$RAD.pageEditor.replacetips"},
                        {"id":"jumpto","caption":"$RAD.pageEditor.jumpto","imageClass":"ri-arrow-right-up-line","type":"button","tips":"$RAD.pageEditor.jumptotips"},
                        {"id":"indentall","caption":"$RAD.pageEditor.indentall","imageClass":"ri-indent-increase","type":"button","tips":"$RAD.pageEditor.indentalltips"}
                    ]
                },
                    {"id":"g3","sub":[
                            {"id":"rendermode", "caption":"mode", "imageClass":"ri-code-line"}
                        ]
                    }
                ];
            if(CONF.enable_codeEdtor){
                items.unshift({"id":"todesign","sub":[
                        {"id":"design","caption":"$RAD.JSEditor.to$RAD.JSEditor.dv","imageClass":"ri-brush-line","tips":"$RAD.JSEditor.dvtips"}
                    ]
                });
            }
            this.toolbar4.setItems(items);
        },
        _onRender:function(com){
        },
        _ondestroy:function(com){
            var ns=this;
            RAD.CodeEditor.clearSandbox("com:JSEditor:" + com.$xid +":");

            ns.___buildNSCode=ns.___treeHash=null;
            if(com._designer)com._designer.destroy();
        },
        resetEditorStyle:function(theme, fs, wrap){
            this.codeeditor.resetEditorStyle(theme,fs, wrap);
        },
        initialize:function(){
            var ns=this;

            ns.$transTaskList=[];
            ns.$transTaskListCached={};

            ns._ovalue="";
            ns._dirty=false;

            ns._uicode=null;

            ns._evalcache={};

            ns.properties={$pageviewType:'ood.UI.ButtonViews'};
        },
        _regClass:/\b(ood\.)?Class\s*\(\s*["'\w\$\.]+\s*\,\s*["'\w\$\.]+\s*\,\s*\{/,
        _rebuildNameSpace:function(value,cache){
            var ns = this,
                value=value||ns.getValue();
            // if no change
            if(value===ns.___buildNSCode){
                return;
            }

            var editor = ns.codeeditor,
                bv=ns.buttonview,
                lo=ns.layoutFill,
                tb=ns.toolbar4;
            // not a class
            if(!ns._regClass.test(value)){
                if(bv && !bv.getItemByItemId('design').disabled){
                    lo.updateItem("after",{hidden:true});
                    bv.updateItem("design",{disabled:true});
                    bv.reLayout(true);
                    tb.updateItem("outline",{value:true});
                    tb.showGroup("g1",false);
                }
                return;
            }else{
                if(bv && bv.getItemByItemId('design').disabled){
                    lo.updateItem("after",{hidden:false});
                    bv.updateItem("design",{disabled:false});
                    bv.reLayout(true);
                    tb.updateItem("outline",{value:true});
                    tb.showGroup("g1",true);
                }
            }

            if(cache!==false){
                ns.___buildNSCode=value;
            }
//var t1=new Date;
            // get Class Struct With Scope info
            var globalCache={},
                struct = RAD.ClassTool.getClassStructWithScope(value);

//console.log('build name space',struct,new Date-t1)
            // try to rebuild left class struct tree
            ns._rebuildTree(struct);
        },
        // 替换类结构树中的图标类
        _rebuildTree:function(struct){
            var ns=this,
                hashArr=[],item,
                items=[];

            ood.each(ood.get(struct,["sub"]),function(obj,i){
                hashArr.push(i);
                item={id:i,
                    caption:i,
                    tips:i
                };
                switch(i){
                    case 'Static':
                        item.imageClass='ri-braces-line';
                        item.sub=[];
                        hashArr.push("[");
                        ood.each(obj.sub,function(o,i){
                            hashArr.push(i);
                            var iconPos=o.type=='function'? 'ri-code-line':o.type=='object'?'ri-braces-line':o.type=='array'?'ri-list-unordered':'ri-code-line';
                            item.sub.push({id:'Static.'+i,tips:i, caption:i, imageClass:iconPos})
                        });
                        hashArr.push("]");
                        break;
                    case 'Instance':
                        item.imageClass='ri-braces-line';
                        item.sub=[];
                        hashArr.push("[");
                        ood.each(obj.sub,function(o,i){
                            hashArr.push(i);
                            var iconPos=o.type=='function'? 'ri-code-line':o.type=='object'?'ri-braces-line':o.type=='array'?'ri-list-unordered':'ri-function-line';
                            item.sub.push({id:'Instance.'+i,tips:i, caption:i, imageClass:iconPos})
                        });
                        hashArr.push("]");
                        break;
                    case 'Dependency':
                        item.imageClass='ri-code-line';
                        break;
                    case 'Constructor':
                    case 'Initialize':
                    case 'Before':
                    case 'After':
                        item.imageClass='ri-code-line';
                        break;
                    default:
                        item.imageClass='ri-code-line';
                }
                items.push(item);
            });

            var hash=hashArr.join("#");
            // rebuild the class struct tree in this case only
            if(hash!==ns.___treeHash){
                ns.___treeHash=hash;
                ns.treebarClass.setItems(items);
            }
        },
        _getEval:function(text){
            var ns=this,
                code=(text || ns.getValue()),
                result;

            if(ns._evalcache[code]){
                result = ns._evalcache[code];
            }else{
                result = RAD.CodeEditor.evalInSandbox(code,false, "com:JSEditor:" + ns.$xid +":2",false,false,"","window.___Class=ood.Class; ood.Class=function(a,b,c){return c;}; ",function(wnd){wnd.ood.Class=wnd.___Class; wnd.___Class=null});
                if(result.ok){
                    ood.breakO(ns._evalcache);
                    ns._evalcache[code] = result;
                    ns._designer.setLinkObj(result.ok);
                }
            }
            return result;
        },
        activate:function(){
            var ns=this,
                view=ns.buttonview.getUIValue();
            if(view=='code')
                ns.codeeditor.activate();
        },
        checkEditorCode:function(){
            var ns=this;
            if(ns._dirty && ns.buttonview.getUIValue()=='code'){
                return !!ns.codeeditor.getUIValue();
            }else{
                return true;
            }
        },
        getValue:function(){
            var ns=this;
            if(ns._dirty){
                if(ns.buttonview.getUIValue()=="design"){
                    // use syn
                    ns._designer.resetCodeFromDesigner(true);
                }
                return ns.codeeditor.getUIValue();
            }else{
                return ns._ovalue;
            }
        },
        setValue:function(value, silence, force){
            // adjust
            value=(value||'').replace(/\t/g, '    ').replace(/\u00A0/g, " ").replace(/\r\n?/g, "\n");

            var ns=this;
            if(force || ns._ovalue!=value){
                ns._ovalue=value;
                ns._dirty=false;

                ns.codeeditor.setValue(value, force);

                ns._uicode = null;

                if(ns.buttonview.getUIValue()=='design'){
                    ns._beforeValueUpdated(null,null,'design');
                }
            }
            if(!silence){
                var isCls=ns._regClass.test(value);
                ns.layoutFill.updateItem("after",{hidden:!isCls});
                ns.buttonview.updateItem("design",{disabled:!isCls});
                ns.toolbar4.showGroup("g1",isCls);
            }

            return ns;
        },
        customAppend:function(parent,subId,left,top){
            var ns = this;
            parent.append(ns.paneMain, subId);

            ns.buttonview.getSubNode('ITEMS').append(
                (new ood.UI.Image({
                    src:CONF.img_progress,
                    position:'relative',
                    left:4,
                    top:6
                })).setHost(ns,"imgProgress")
            );

            ns._disableUI();
        },
        iniExComs:function(){
            var ns = this;
            var pageview = (new (ood.SC.get(ns.properties.$pageviewType)))
                .setHost(ns,"buttonview")
                .setNoPanel(true)
                .setDock('top')
                .setHeight('auto')
                .setItems([
                    {"id":"code","caption":"$RAD.JSEditor.sv", imageClass:"ri-code-line","tips":"$RAD.JSEditor.svtips"},
                    {"id":"design","caption":"$RAD.JSEditor.dv",imageClass:"ri-brush-line","tips":"$RAD.JSEditor.dvtips"}])
                .beforeUIValueSet("_beforeValueUpdated")

            if(ns.properties.$pageviewType=="ood.UI.ButtonViews"){
                pageview.setDock("bottom");
            }

            if(ns.properties.$pageviewType=='ood.UI.ButtonViews'){
                pageview.setBarSize(28);
                pageview.setCustomStyle({"ITEMS":"position:relative","LIST":"position:relative"});
            }

            ns.paneMain.append(pageview);

            // apend designer
            var designer = new RAD.Designer();
            designer.setHost(ns, '_designer');
            designer.setEvents('onValueChanged',function(ipage, flag, fore){
                // must consider the code side
                ns._dirty=fore?flag:ns._dirty||flag;
                ns.fireEvent('onValueChanged', [ns, ns._dirty]);
            });
            designer.resetTaskList=function(){
                ns._disableUI();
                ns.$transTaskList=[];
                ns.$transTaskListCached={};
            };
            designer.addTask=function(task){
                ns.$transTaskList.push(task);
            };
            designer.hasTask=function(task){
                return !!ns.$transTaskList.length;
            };
            designer.startTaskList=function(){
                ns.checkRenderStatus(ns);
            };
            designer.resetCode=function(pkey, key, code, syn, isFun,comments){
                var fun=function(){
                    if(ns.$transTaskListCached[pkey+":"+key]===code)
                        return;
                    ns.addCodeToEditor(pkey, key, code,isFun,comments);
                    ns.$transTaskListCached[pkey+":"+key]=code;
                };

                if(syn){
                    fun();
                }else
                    ns.$transTaskList.push(fun);
            };
            designer.addCode=function(pkey, key, code, syn, isFun,comments){
                var fun=function(){
                    if(ns.$transTaskListCached[pkey+":"+key]===code)
                        return;
                    // replace or add code
                    ns.addCodeToEditor("Instance", key, code, isFun,comments);
                    ns.$transTaskListCached[pkey+":"+key]=code;
                };

                if(syn){
                    fun();
                }else
                    ns.$transTaskList.push(fun);
            };
            designer.focusEditor=function(pkey, key){
                ns.showPage('code');
                ns._enable();
                ns.tryToLocale([pkey,key]);
            };
            designer.searchInEditor=function(code){
                ns.showPage('code');
                ns._enable();
                var cursor = ns.codeeditor.searchCode(code);
            };
            designer.refreshFromCode=function(nomessage){
                var obj=ns._getEval(ns._ovalue = ns.codeeditor.getUIValue());
                if(obj.ok){
                    if(CONF.getClientMode() != 'project'||true){
                        ns.$transTaskListCached["Static:viewSize"]=this.refreshViewSize(obj.ok,false);
                    }

                    ns.$transTaskListCached["Instance:iniComponents"]=this.refreshView(obj.ok,false,RAD.ClassTool.getClassName(ns._ovalue) );

                    if(!nomessage)
                        ood.message(ood.getRes('RAD.designer.refreshOK'));
                }
            };
            designer.toCodeView=function(){
                ns.buttonview.fireItemClickEvent('code');
            };
            designer.saveDesign=function(){
                ns.fireEvent('onCommandSave', [ns]);
            };
            ns.paneDesign.append(designer);
        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0))};

            append(
                (new ood.UI.Pane)
                    .setHost(host,"paneMain")
                    .setDock("fill")
            );

            host.paneMain.append(
                (new ood.UI.Pane)
                    .setHost(host,"paneCode")
                    .setVisibility('hidden')
                    .setDock("fill")
                    .setDockIgnore(true)
                    .setLeft(-1000)
            );
            host.paneMain.append(
                (new ood.UI.Pane)
                    .setVisibility('hidden')
                    .setHost(host,"paneDesign")
                    .setDock("fill")
            );

            host.paneCode.append((new ood.UI.Layout)
                .setHost(host,"layoutFill")

                .setItems([
                    {"id":"after", "pos":"after", "locked":false, "size":150, "min":50, "max":200, "cmd":true, "folded":false, "hidden":false}, {"id":"main", "min":10}
                ])
                .setType("horizontal")
            );

            host.layoutFill.append((new ood.UI.TreeBar)
                    .setHost(host,"treebarClass")
                    .setInitFold(false)
                    .setSelMode("none")
                    .setAnimCollapse(false)
                    .onItemSelected("_treebarclass_onitemselected")
                , 'after');

            host.paneCode.append((new ood.UI.ToolBar)
                .setHost(host,"toolbar4")
                .onClick("_toolbar_onclick")
            );

            host.layoutFill.append((new RAD.CodeEditor)
                    .setHost(host,"codeeditor")
                    .setDock("fill")
                    .onValueChanged("_codeeditor_onChange")
                    .onRendered("_codeeditor_onrender")
                    .onSaveCommand("_codeeditor_onsave")
                    .onGetHelpInfo("_codeeditor_ongett")
                    .onCodeModeSet("_codeeditor_oncodemodeset")
                ,'main');

            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        setDftPage:function(key){
            var ns=this;
            ns.$dftpage=key;
        },
        showPage:function(key){
            var ns=this;
            if(ns._once)return;
            ns.buttonview.setUIValue(key,true);
        },
        _codeeditor_ongett:function(profile,key){
            ood.asyRun(function(){
                ood.Coder.applyById("doc:code",true);
            });
            return RAD.EditorTool.getDoc(key);
        },
        _codeeditor_oncodemodeset:function(profile, mode){
            if(mode){
                this.toolbar4.updateItem('rendermode',{
                    caption :"&nbsp;"+mode.name,
                    mime: mode.mime,
                    mode: mode.mode,
                    tips : 'Code was rendered as '+ "["+mode.name+"]"
                });
            }
        },
        _beforeValueUpdated:function(profile, ov, nv){
            var ns=this;
            if(ov === nv)return;
            ns._once=true;
            if(nv=='code'){
                var code;
                if(ns._designer.isDirty()){
                    // refresh code from designer
                    code=ns._designer.resetCodeFromDesigner();
                    // clear desinger's dirtymark
                    ns._designer.clearDirty();
                }
                // clear history
                //ns.codeeditor.clearHistory();
                if(code){
                    ns._uicode=code.substr(1,code.length-2);
                }

                ns.paneDesign.setDisplay('none').setZIndex(1);
                ns.paneCode.setDockIgnore(false).setVisibility('visible').setZIndex(100).reLayout();

                //show structure imme
                ns._rebuildNameSpace(null,false);
            }else if(nv=='design'){
                //1. must valid
                var result=ns._getEval(), className;
                if(!result.ok){
                    ood.message(ood.getRes('RAD.JSEditor.codeerr'));
                    ns._beforeValueUpdated(null,null,'code');
                    ns.codeeditor.focusLine(result.line-1);
                    return false;
                }
                //2. It's a Class, and from ood.com
                if(!(className = RAD.ClassTool.getClassName(ns.codeeditor.getUIValue()||""))){
                    ood.message(ood.getRes('RAD.classtool.noClass'));
                    return false;
                }

                var needRef=false;
                if(CONF.getClientMode() != 'project'||true){
                    var needRefSize=false;
                }
                var f=ood.get(result.ok,['Instance','iniComponents']);
                if(!f){
                    if(ns._uicode){
                        needRef=true;
                        ns._uicode=null;
                    }
                }else{
                    var code=f._____string || (f+"");
                    code=code.slice(code.indexOf('{')+1, code.lastIndexOf('}'));
                    if(code!=ns._uicode){
                        var pos=ns.codeeditor.getLine("Instance","iniComponents"),
                            line=pos?(pos.line):0,
                            pretag=ood.str.repeat("\n",line);
                        var result2 = RAD.CodeEditor.evalInSandbox(pretag+"(function(){"+code+"}());", false, "com:JSEditor:"+ns.$xid +":1",null,true);
                        if(!ood.isSet(result2.ok)){
                            ood.alert(ood.getRes('RAD.designer.comCodeErr') +"<br/><br/>" + result2.ko);
                            ns.buttonview.setUIValue('code');

                            ns.codeeditor.focusLine(result2.line-1);
                            return false;
                        }
                        // reset _uicode
                        ns._uicode=code;
                        needRef=true;
                    }
                }
                if(CONF.getClientMode() != 'project'||true){
                    if(!ns._uiviewsize)
                        needRefSize=true;
                    else{
                        var w=parseInt(ood.get(result.ok,['Static','viewSize','width']),10),
                            h=parseInt(ood.get(result.ok,['Static','viewSize','height']),10);
                        if(!w||!h){
                            needRefSize=true;
                        }else{
                            if(w!=ns._uiviewsize.w || h!=ns._uiviewsize.h){
                                // reset _uiviewsize
                                ns._uiviewsize={w:w,h:h};
                                needRefSize=true;
                            }
                        }
                    }
                }

                ns.paneCode
                    .setDockIgnore(true)
                    .setLeft(-10000)
                    .setVisibility('hidden')
                    .setZIndex(1);
                // must be here, for avoid the case: display:none will affect the inner controls renderring
                ns.paneDesign
                    .setDisplay('')
                    .setVisibility('visible')
                    .setZIndex(100)
                    .reLayout(true);

                // for com/canvas event changed
                if(!ns._designer.tempSelected||!ns._designer.tempSelected.length)
                    needRef=true;

                if(CONF.getClientMode() != 'project'||true){
                    if(needRefSize)
                        ns.$transTaskListCached["Static:viewSize"]=ns._designer.refreshViewSize(result.ok,false);
                }else{
                    ns._designer.refreshViewSize(null,false);
                }

                if(needRef)
                    ns.$transTaskListCached["Instance:iniComponents"]=ns._designer.refreshView(result.ok,null,className);

                ns._designer.reLayout(true);

                // clear history
                // ns.codeeditor.clearHistory();
            }

            ns._once=false;
        },
        _disableUI:function(){
            var ns=this,
                id=ns._uiresetId||(ns._uiresetId=ns.KEY+":"+ns.$xid+":progress");
            ood.resetRun(id, function(){
                if(!ns.__UIdisabled){
                    if(!ns.toolbar4)return;
                    ns.codeeditor.setReadonly(true);

                    ns.toolbar4.setDisabled(true);
                    ns.treebarClass.setDisabled(true);

                    if(ns.imgProgress)ns.imgProgress.setDisplay('');

                    ns.__UIdisabled=true;
                }
            });
        },
        _enable:function(){
            var ns=this;
            ood.resetRun(ns._uiresetId);
            ns.__UIdisabled=false;

            ns.codeeditor.setReadonly(false);
            ns.toolbar4.setDisabled(false);
            ns.treebarClass.setDisabled(false);

            if(ns.imgProgress)ns.imgProgress.setDisplay('none');
        },
        checkRenderStatus:function(ns){
            if(ns._renderStatus &&  ns.$transTaskList &&  ns.$transTaskList.length){
                var fun=ns.$transTaskList.shift();
                ood.tryF(fun);
            }
        },
        _codeeditor_onrender:function(profile, finished){
            var ns=this;

            if(finished){
                // default rebuild name space time : 1 s
                var id=ns.KEY+":"+ns.$xid+":rebuildNS";
                ood.resetRun(id,function(){
                    ns._rebuildNameSpace();
                },1000);

                // if UI was disabled
                if(ns.__UIdisabled){
                    ns._enable();
                }
                // if UI will be disabled
                else if (ood.resetRun.exists(ns._uiresetId)){
                    ood.resetRun(id);
                }

                if(!ns.$initializd){
                    // start a repeat thread
                    ns.$tasksthread=ood.Thread.repeat(function(){
                        if(ns.destroyed)return false;

                        try{
                            ns.checkRenderStatus(ns);
                        }catch(e){}
                    },300);
                    ns.$tasksthread.start();

                    ns.$initializd=true;
                    ns.showPage(ns.$dftpage);
                    ns.fireEvent('afterRendered');
                }
            }else{
                ns._disableUI();
            }
            ns._renderStatus=!!finished;
        },
        _codeeditor_onChange:function(profile, flag){
            var ns=this;

            // dont consider the designer side
            ns._dirty=flag;

            ns.fireEvent('onValueChanged', [ns, ns._dirty]);
        },
        _codeeditor_onsave:function(profile){
            var ns=this;
            ns.fireEvent('onCommandSave', [ns]);
        },
        // select the second layer keys
        _treebarclass_onitemselected:function(profile, item, node){
            var ns = this,
                arr=item.id.split('.');
            ns.tryToLocale(arr);
        },
        addCodeToEditor:function(pkey, key, code, isFun, comments){
            var ns=this,
                editor=this.codeeditor;
            editor.addCodeInto(pkey, key, code, isFun, comments);
        },
        tryToLocale:function(arr){
            var ns=this,
                editor=this.codeeditor;
            editor.locateTo(arr[0],arr[1]);
        },
        _toolbar_onclick: function(profile, item, grp, e, src){
            var ns=this,
                editor=this.codeeditor,
                pos=ood.use(src).offset();
            switch(item.id){
                case 'design':
                    ns.buttonview.fireItemClickEvent('design');
                    break;
                case 'indentall':
                    editor.reindent();
                    break;
                case 'searchreplace':
                    RAD.EditorTool.showFindWnd(null,editor, pos,editor.getCM().getSelection());
                    break;
                case 'jumpto':
                    RAD.EditorTool.showJumpToWnd(null,editor, pos);
                    break;
                case 'outline':
                    ns.layoutFill.updateItem("after",{hidden:!item.value});
                    break;
                case 'rendermode':
                    editor.showModeSelDlg(item);
                    break;
            }
        },
        focusEdit:function(pkey,key){
            var ns=this,
                editor=this.codeeditor,
                info = editor.getBlockInfo(pkey, key),
                cm=editor.getCM(),
                bfrom = cm.doc.posFromIndex(info.from+1),bfrom2={},
                bto = cm.doc.posFromIndex(info.to),bto2={},
                ll=cm.doc.posFromIndex(cm.doc.getValue().length+100),
                span1,span2;

            bfrom2.ch=0;
            bfrom2.line=bfrom.line;
            bfrom2=cm.doc.indexFromPos(bfrom2);
            bfrom2-=1;
            bfrom2 = cm.doc.posFromIndex(bfrom2);

            bto2.ch=0;
            bto2.line=bto.line;

            span1 = document.createElement("span");
            span1.innerHTML="...";
            span1.className="cm-readonly";
            span2 = document.createElement("span");
            span2.innerHTML="<br/>...";
            span2.className="cm-readonly";

            cm.markText({line:0,ch:0},bfrom2,{
                replacedWith:span1,
                inclusiveLeft:true,
                inclusiveRight:true
            });
            cm.markText(bfrom2,bfrom,{
                className:"cm-readonly",
                readOnly:true,
                inclusiveLeft:true,
                inclusiveRight:true
            });
            cm.markText(bto,ll,{
                replacedWith:span2,
                inclusiveLeft:true,
                inclusiveRight:true
            });
            cm.markText(bto2,bto,{
                className:"cm-readonly",
                readOnly:true,
                inclusiveLeft:true,
                inclusiveRight:true
            });
        }
    }
});