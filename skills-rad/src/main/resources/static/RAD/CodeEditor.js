
// A wrap for codemirror(http://codemirror.net/)
ood.Class("RAD.CodeEditor", ["ood.UI.Widget","ood.absValue"] ,{
    Instance:{
        activate:function(){
            var profile = this.get(0);
            if(profile.$cm)profile.$cm.focus();
            if(profile.$cm)profile.$cm.doc.setCursor(profile.readonly_head||0+1,0);
            return this;
        },
        getCM:function(){
            return this.get(0).$cm;
        },
        resetEditorStyle:function(theme, fs, lineWrapping, cm){
            this.addCMTheme(theme);

            var profile = this.get(0);
            if(!cm)cm=profile.$cm;
            if(cm){
                cm.setOption("theme", theme);
                cm.setOption("lineWrapping", !!lineWrapping);
                var n=cm.getWrapperElement();
                if(n){
                    n.style.fontSize=fs+"px";
                }
            }
        },
        showModeSelDlg:function(mm){
           var ns=this,
           cm=ns.getCM(),
           profile=ns.get(0),
           callback=function(mode){
                // force to finish render
                if(!mode || mode.mode==="none"){
                    if(!profile.$initialized)
                        profile.$initialized=true;
                    if(profile.onRendered)
                        profile.boxing().onRendered(profile, true);
                }else{
                    cm.setOption('mode', mode.mime||mode.mode);
                    CodeMirror.autoLoadMode(cm, mode.mode);

                    if(profile.onCodeModeSet)
                        profile.boxing().onCodeModeSet(profile, mode);
                }
                profile=cm=null;
           };
           ood.ModuleFactory.newCom("RAD.SelRenderMode",function(){
                this.setEvents("onSel",callback);
                this.mainDlg.showModal();
                this.modeList.setValue(mm.mime+":"+mm.mode);
           });
        },
        addCMTheme:function(theme){
            var profile = this.get(0);
            var id="cm-theme-"+theme;
            if(theme!="default" && !ood.CSS.get("id",id)){
                ood.CSS.includeLink("codemirror5/theme/"+theme+".css",false,id)
            }
        },
        clearHistory:function(){
            var obj=this.get(0).$cm;
			if(obj)obj.clearHistory();
        },
        reindent:function(){
            var cm=this.get(0).$cm,
                doc=cm.doc;
            if(doc.somethingSelected()){
                var p1=doc.getCursor("start"),
                    p2=doc.getCursor("end");
                for(var i=p1.line,l=p2.line;i<=l;i++)
                    cm.indentLine(i)
            }else{
                var p1=doc.firstLine(),
                    p2=doc.lastLine();
                for(var i=p1,l=p2;i<=l;i++)
                    cm.indentLine(i)
            }
        },
        callEditor:function(name,args,isDoc){
            var obj=this.get(0).$cm;
            if(isDoc)obj=obj.doc;
            return obj[name].apply(obj,args||[]);
        },
        searchCode:function(code){
            var profile = this.get(0),
                cm=profile.$cm;
            if(cm){
                var cursor = cm.getSearchCursor(code, cm.hasFocus()?cm.doc.getCursor():{pos:0,line:0}, true);
                if(cursor.findNext()){
                    cm.focus();
                    cm.doc.setSelection(cursor.from(), cursor.to());
                }
            }            
        },
        // locate to declare key
        locateTo:function(key1, key2, toSelect){
            var ns=this,
                profile = this.get(0),
                cm=profile.$cm,
                struct,info,codefrom,codeto;
            if(cm){
                struct=RAD.ClassTool.getClassStruct2(cm.doc.getValue());
                if(key1){
                    if(key2 && (key1=="Static"||key1=="Instance"))
                        info=ood.get(struct,["sub",key1,"sub",key2]);
                    else
                        info=ood.get(struct,["sub",key1]);
                }
                if(info){
                    if(toSelect)
                        cm.doc.setSelection(cm.doc.posFromIndex(info.from),cm.doc.posFromIndex(info.to));
                    else{
                        var pos=cm.doc.posFromIndex(info.from);
                        cm.focus();
                        cm.doc.setCursor(pos);
                        cm.scrollIntoView(pos);
                    }
                    return true;
                }
            }
            return false;
        },
        focusLine:function(line){
            var ns=this,
                profile = this.get(0),
                cm=profile.$cm,
                pos={line:line,ch:0};
            if(cm){
                    cm.focus();
                    cm.doc.setCursor(pos);
                    cm.scrollIntoView(pos);
            }
        },
        getLine:function(key1, key2, toSelect){
            var ns=this,
                profile = this.get(0),
                cm=profile.$cm,
                struct,info,codefrom,codeto;
            if(cm){
                struct=RAD.ClassTool.getClassStruct2(cm.doc.getValue());
                if(key1){
                    if(key2 && (key1=="Static"||key1=="Instance"))
                        info=ood.get(struct,["sub",key1,"sub",key2]);
                    else
                        info=ood.get(struct,["sub",key1]);
                }
                if(info){
                        return cm.doc.posFromIndex(info.from);
                }
            }
            return null;
        },
        // add code to a hash, or replace the original one
        addCodeInto:function(key1, key2, code, isFun, comments){
            comments=comments||"";
            var ns=this,
                profile = this.get(0),
                cm=profile.$cm;
            if(cm){
                var struct=RAD.ClassTool.getClassStructWithScope(cm.getValue()),
                    fo=ood.get(struct,["sub",key1,"sub",key2]),
                    fo2;
                // There is a function => replace code
                if(fo/* && (fo.type=='function'||fo.type=='object')*/){
                    cm.doc.replaceRange(code,cm.doc.posFromIndex(fo.from),cm.doc.posFromIndex(fo.to));
                    //cm.doc.setSelection(cm.doc.posFromIndex(fo.from), cm.doc.posFromIndex(fo.from+code.length));
                    //ns.reindent();
                }else{
                    if(comments)comments +="\n";

                    fo=ood.get(struct,["sub",key1]);
                    // add first layer
                    if(!fo){
                        fo2=ood.get(struct,["sub",struct.arr[struct.arr.length-1]]);
                        if(fo2)
                            code=",\n"+comments +key1+":{\n"+key2+":"+(isFun?'function()':'')+code+"\n"+"}";
                        else{
                            code=key1+":{\n"+comments +key2+":"+(isFun?'function()':'')+code+"\n"+"}\n";
                            fo2=struct;
                            fo2.to--;
                        }
                    }else{
                        // find the last one
                        fo2=ood.get(fo,["sub",fo.arr[fo.arr.length-1]]);
                        if(fo2)
                            code=",\n"+comments +key2+":"+(isFun?'function()':'')+code;
                        else{
                            code=comments +key2+":"+(isFun?'function()':'')+code+"\n";
                            fo2=fo;
                            fo2.to--;
                        }
                    }
                    var pos=cm.doc.posFromIndex(fo2.to);
                    cm.doc.replaceRange(code, pos, pos);
                    cm.doc.setSelection(pos, cm.doc.posFromIndex(fo2.to+code.length));
                    ns.reindent();
                }
                //locate in the UI
                //ns.locateTo(key1,key2);
            }
        },
        getBlockInfo:function(key1, key2){
            var ns=this,
                profile = this.get(0),
                cm=profile.$cm;
            return cm && ood.get(RAD.ClassTool.getClassStructWithScope(cm.getValue()),["sub",key1,"sub",key2]);
        },
        _refreshCM:function(){
            if(this.get(0).$cm)this.get(0).$cm.refresh();
        },
        _setCtrlValue:function(value){
            if(!ood.isSet(value))value='';
            return this.each(function(profile){
                if(profile.$cm && profile.$cm.getValue()!=value){
                    profile.$cm.doc.setValue(value);
                    if(profile.$tm1)profile.$tm1.clear();
                    if(profile.$tm2)profile.$tm2.clear();
                    delete profile.$tm1;
                    delete profile.$tm2; 
                    delete profile.readonly_head;
                    delete profile.readonly_tail;
                }
            });
        },
        _getCtrlValue:function(){
            var profile = this.get(0);
            if(profile.$cm)
                return profile.$cm.doc.getValue();
            else
                return profile.properties.$UIvalue;
        },
        setReadonlyLines:function(head, tail){
            var profile = this.get(0), doc=profile.$cm.doc;
            if(profile.$tm1)profile.$tm1.clear();
            if(profile.$tm2)profile.$tm2.clear();

            if(head){
                profile.$tm1 = doc.markText({line:0, ch:0}, {line:head,ch:0}, {atomic:true, readOnly:true, css:'background-color:#ccc;'});
            }
            if(tail){
                var last = doc.lastLine();
                profile.$tm2 = doc.markText({line:last-tail}, {line:last}, {atomic:true, readOnly:true, css:'background-color:#ccc;'});
            }
            profile.readonly_head=head;
            profile.readonly_tail=tail;
        }
    },
    Initialize:function(){
        //modify default template for shell
        var t = this.getTemplate();
        ood.merge(t.FRAME.BORDER,{
            BOX:{
                tagName:'div'
            }
        },'all');
        this.setTemplate(t);
        
        this.__trim=function(s){
            if(/[^\s\uFEFF\xA0]/.test(s)){
                return ood.str.trim(s);
            }else
                return s;
        };
    },
    Static:{
        $tempsandbox:"_$temp_for_ood_sandbox",
        $tempsandbox2:"_$temp_for_ood_sandbox2",
        $temppool:"_$temp_for_ood_pool",
        Appearances:{
            BOX:{
                width:'100%',
                height:'100%',
                left:0,
                top:0,
                position:'absolute',
                background:'#fff'
            }
        },
        Behaviors:{},
        DataModel:{
            left:0,
            top:0,
            width:200,
            height:200,
            position:'absolute',
            codeType:{
                ini:'js',
                action:function(codeType){
                    if(this.$cm){
                        var mode = CONF.getRenderMode(codeType);
                        if(!mode)mode=CONF.getRenderMode("txt");
                        this.$cm.setOption('mode',  mode.mime||mode.mode);
                        if(this.onCodeModeSet)this.boxing().onCodeModeSet(this, mode);
                    }
                }
            },
            readonly:{
                ini:false,
                action:function(value){
                    if(this.$cm)
                        this.$cm.setOption("readOnly",!!value);
                }
            }
        },
        EventHandlers:{
            onValueChanged:function(profile, flag){},
            onRendered:function(profile){},
            onGetHelpInfo:function(profile, key){},
            onCodeModeSet:function(profile, mode){},
            onSaveCommand:function(){}
        },
        RenderTrigger:function(){
            var key=this.box.$temppool,
                subkey=this.$xid;

            this.box._loadEditor(this);

            this.destroyTrigger=function(){
                this.$cm.clearGutter();
                this.$cm.clearHistory();
                delete this.$cm;
                delete this.$tm1;
                delete this.$tm2;
                delete this.$editor;
                
                // destroy related UI
                if(this.$popList)this.$popList.destroy();
                if(this.$popHelp)this.$popHelp.destroy();
                if(this.$popCustom)this.$popCustom.destroy();
                
                if(window[key])delete window[key][subkey];
            };
        },
        clearSandbox:function(name){
            for(var i=1,n;i<9;i++){
                n=name+i;
                if(n && document.getElementById(n)){
                    document.body.removeChild(document.getElementById(n));
                    try{delete frames[n]}catch(e){}
                }
            }
        },
        purge:function(){
                var ns=this,z;
                if((z=window[ns.$temppool])&&z.Instance&&z.Instance._ctrlpool){
                   ood.each(z.Instance._ctrlpool,function(o,i){
                      if(o&&!o.destroyed){
                          if(o.destroy)o.destroy();
                          if(o.boxing && o.boxing())o.boxing().destroy();
                      }
                   });
                   ood.breakO(z.Instance);
                }
        },
       _alias:1,
        evalInSandbox:function(code, funScope, name, subCacheKey, purge, beforeCode, afterCode, cleanFun){
            if(name && document.getElementById(name)){
                document.body.removeChild(document.getElementById(name));
                try{delete frames[name]}catch(e){}
            }
            var ns=this,
                iframe = document.createElement("iframe"), result, wnd, line, errmsg, type;
            
            ns.purge();

            if(name){
                iframe.name=name;
                iframe.id=name;
            }


            iframe.src="about:blank";
            iframe.style.display = "none";
            document.body.appendChild(iframe);
            wnd=frames[frames.length - 1];
            wnd.document.open();
            wnd.document.write('<meta http-equiv="content-type" content="text/html; charset=utf-8" />');
            wnd.document.write("<script>\n");
            if(beforeCode)wnd.document.write(beforeCode);
            wnd.document.write(""                
                // stop Node.js
                +"global=process=Buffer=require=module=exports=root=null;\n"
                
                // copy object
                +"window.ood=parent.ood;\n"
                +"window.Class=function(){return ood.Class.apply(ood,arguments);};\n"
                +"window.CONF={mixed_prop: ood.clone(parent.CONF.mixed_prop) };\n"

                + (CONF.SandBoxExtCode||"") 

                //get this scope
                +"window.__cache={};window.__getThis=function(pname,type){\n"
                +"      if(__cache[pname+':'+type])return __cache[pname+':'+type];\n"
                +"      var obj=type==1?function(){}:{};\n"
                +"      if(pname){\n"
                +"          if(ood.isArr(pname)){\n"
                +"              ood.each(pname,function(cls){\n"
                +"                  cls=ood.SC(cls);\n"
                +"                  obj=ood.merge(obj, type==1?(cls||ood.Module):(cls?(new cls):(new ood.Module)),'all',true)\n"
                +"              });\n"
                +"          }else{\n"
                +"              pname=ood.SC(pname);\n"
                +"              obj=ood.merge(obj, type==1?(pname||ood.Module):(pname?(new pname):(new ood.Module)),'all',true)\n"
                +"          }\n"
                +"      }\n"
                +"      if(window['"+ns.$temppool+"'] && window['"+ns.$temppool+"']['"+subCacheKey+"']){\n"
                +"          obj=ood.merge(obj, window['"+ns.$temppool+"']['"+subCacheKey+"'][type==1?'Static':'Instance'],'all')\n"
                +"      }\n"
                +"      return __cache[pname+':'+type]=obj;\n"
                +"  };\n"

                +"window['"+ns.$temppool+"']=parent['"+ns.$temppool+"'];\n"
                // "var b=arguments.callee;" for fix firefox 3.5 bug
                +"parent['"+ns.$tempsandbox+"']=/*@cc_on !@*/0?this:{eval:function(s){return (function(){var b=arguments.callee;return window.eval.call(window,s)})()}};\n"
                +"parent['"+ns.$tempsandbox2+"']={getErrLine:function(s,cb){window.onerror=function(){cb.apply(null,arguments);return true;};var n=document.createElement('script');n.type='text/javascript';if(/*@cc_on !@*/0){n.text=s;}else{n.appendChild(document.createTextNode(s));};document.body.appendChild(n);window.onerror=null;}};\n"
                +"try{top=parent=null;}catch(e){}"
            );
            if(afterCode)wnd.document.write(afterCode);
            wnd.document.write("<\/script><body><span id='abc'></span></body>");
            wnd.document.close();
            var bak=ood.Dom.pack,
                bak1=ood.absProfile.prototype.$link,
                bak11=ood.absObj.$pickAlias,
                bak12=ood.UIProfile.prototype.setDomId,
                bak2=ood.require,
                bak3=ood.$cache,
                bak4=ood.asyRun,
                bak5=ood.resetRun,
                bak6=ood.exec,
                bak7=ood.pseudocode._callFunctions;

            try{
                // avoid lib or dom affected
                ood.Dom.pack=function(){var o=new ood.Dom(false),d=document.getElementById('abc');o._nodes=[d];return o};
                ood.absProfile.prototype.$link=function(){return this;};
                // stop all default events
                ood.Class.$instanceCreated=function(instance){instance.$inDesign=1;};
                ood.absObj.$pickAlias = function(){return ns._alias++};
                ood.UIProfile.prototype.setDomId=function(){return this;};
                ood.require=function(){};
                ood.$cache={};
                ood.each(bak3,function(o,i){
                    ood.$cache[i]=ood.isArr(o)?[]:{};
                });
                ood.asyRun=ood.resetRun=ood.exec=ood.pseudocode._callFunctions=function(){};
            	
                result = window[ns.$tempsandbox].eval(code);
                if(funScope){
                    if(typeof(result)=='function')result.call(funScope);
                }

                type = (result===wnd || result===wnd.content)? 'window' :
                       result===wnd.console ? 'console' :
                       result===wnd.history ? 'history' :
                       result===wnd.screen ? 'screen' :
                       result===wnd.location ? 'location' :
                       result===wnd.navigator ? 'navigator' :
                       result===wnd.document ? 'document' :
                       null;
                // to avoid : Can't execute code from a freed script
                if(ood.browser.newie||(ood.browser.ie && ood.browser.ver>=9)){
                    var fun=function(hash){
                        for(var i in hash){
                            if(ood.isFun(hash[i]))hash[i]._____string=hash[i]+"";
                            else if(ood.isHash(hash[i]))fun(hash[i]);
                            else if(ood.isArr(hash[i]))fun(hash[i]);
                        }
                    };
                    if(type===null && (ood.isHash(result)||ood.isArr(result)))fun(result);
                }
            }catch(e){       
                //firefox or opera
                console.error(e);
                if(ood.isSet(e.line||e.lineNumber)){
                    line=e.line||e.lineNumber;
                }
                // safari chrome IE
                else{
                    var base=0;
                    if(ood.browser.ie){
                        window[ns.$tempsandbox2].getErrLine("var _a={;",function(a,b,c){
                            base=c-1;
                            return true;
                        });                        
                    }
                        window[ns.$tempsandbox2].getErrLine(code,function(a,b,c){
                        line=c-base;
                        return true;
                    });
                }   
                errmsg=((e.name?e.name+' : ':'') + (e.description||e.message||'') + (line?'\n line : '+line:''));
                if(!ood.isSet(line) && /.*at line ([\d]+).*/.test(errmsg))
                    line=errmsg.replace(/.*at line ([\d]+).*/,'$1');
            }finally{

                if(purge)ns.purge();

                delete ood.Class.$instanceCreated;

                ood.Dom.pack=bak;
                ood.absProfile.prototype.$link=bak1;
                ood.absObj.$pickAlias = bak11;
                ood.UIProfile.prototype.setDomId=bak12;
                ood.require = bak2;
                ood.$cache = bak3;
                ood.asyRun = bak4;
                ood.resetRun = bak5;
                ood.exec = bak6;
                ood.pseudocode._callFunctions = bak7;

                if(cleanFun)cleanFun(wnd, iframe);
                 
                wnd.CONF=wnd.Class=wnd.ood=wnd[ns.$temppool]=wnd.__getThis=wnd.__cache=null;
                if(CONF.ClearSandBoxExtCode){
                    CONF.ClearSandBoxExtCode(wnd);
                }
                window[ns.$tempsandbox]=null;
                window[ns.$tempsandbox2]=null;
 
                var rsl={
                    ok:result,
                    type:type,
                    ko:errmsg,
                    line:line
                };

                ns=bak=result=line=errmsg=type=null;   
                if(!name)
                    document.body.removeChild(iframe);
                iframe=null;

                return rsl;
            }
        },
        _buildItem:function(id, type, key, o, caption, value, order, itemStyle){
            return {
                id:id,
                caption:caption||id,
                value:value||id,
                tag: (order||(type=='property'?'b':type=='function'?'c':type=='class'?'d':type=='event'?'e':'a')) + ":" + id.toLowerCase(),
                path: (key?(key+"."):"")+id,
                itemStyle:itemStyle,
                imageClass:!type?null:
                    (type=='property'?
                        (ood.isSet(o)&&ood.isHash(o))?'spafont spa-icon-hash':
                        (ood.isSet(o)&&ood.isArr(o))?'spafont spa-icon-array':
                    'spafont spa-icon-var':
                    type=='var'?'spafont spa-icon-var':
                    type=='event'?'ri ri-play-line':
                    type=='function'?'ri ri-code-s-slash-line':
                    type=='class'?'spafont spa-icon-class':
                    "spafont spa-icon-prop")
             };
        },
        _intellcache:{},
        _enumKeys:function(target, key, normalObj, ownOnly){
            // non-object or array return empty array
            if(!ood.isObj(target) || ood.isArr(target))return [];

            // default is normal object
            normalObj=normalObj!==false;
            var ns=this,
                isoodobj= target.$ood$,
                o,isfun,
                list=[];

            for(var i in target){
                if('prototype'==i || 'constructor' == i)
                    continue;
                if(isoodobj){
                    if('upper'==i || 'After'==i || 'Before'==i || 'Instance'==i || 'Static'==i || 'Constructor'==i)
                        continue
                }
                try{
                    if(i.charAt(0)!="_" &&  i.charAt(0)!="$" && /^[\w_]+$/.test(i)){
                        o=target[i];
                        if(normalObj && (o && o.$abstract)){
                            continue;
                        }
    
                        isfun = typeof o == 'function' && (!(o && o.$ood$) || o.$asFunction);
                        
                        if(ownOnly && (!target.hasOwnProperty(i) ||  !target.propertyIsEnumerable(i))){
                            continue;
                        }
                        var value = i;
                        if(isfun){
                            if(normalObj){
                                var arr = ood.fun.args(o);
                                var isInstance=false;
                                if(ood.str.endWith(key, ".prototype")){
                                    isInstance = true;
                                }
                                var paras = RAD.ClassTool.getArgsFromAPI(key.replace(".prototype",""),i,isInstance);
                                if(paras){
                                    // use doc's paramters
                                    arr=[];
                                    for(var k=0,l=paras.length;k<l;k++){
                                        arr[k] = paras[k] && paras[k].name;
                                    }
                                }
                                value = i + "(" + arr.join(", ") +  ")";
//                                console.log(key.replace(".prototype",""), i , isInstance, arr, value);
                            }else{
                                value = i + "( )";
                            }
                        }
                        list.push(ns._buildItem(i,
                            (typeof o=='function')? (o && o.$ood$ && !o.$asFunction) ?'class': (o && o.$event$) ? "event" : 'function':'property',
                            key,
                            o,
                            value,
                            value 
                            ));
                    }
                }catch(e){
                    console.log(e);
                }
            
            }

            return list;
        },
        _getIntellisenseList:function(key, cachedata){
            if(!key)return null;
            cachedata = cachedata!==false;

            var ns=this,
                cache = ns._intellcache,
                buildItem = ns._buildItem;

            if(cachedata && cache[key])return cache[key];

            var list=[];
            if(key==ns.$temppool){
                return [];
            }
            
            switch(key){
                case ' variables ':{
                    list=[
                        buildItem("PK",0,0,0,0,0,4,"font-weight:bold;"),

                        //buildItem("absField",0,0,0,0,0,4,"font-weight:bold;color:#ff0000;"),



                    // global
                        buildItem("ood",0,0,0,0,0,2,"font-weight:bold;color:#ff0000;"),
                        buildItem("ood()",0,0,0,"ood()","ood()",2,"font-weight:bold;color:#ff0000;"),

                        buildItem("top",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("parent",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("window",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("document",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("navigator",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("history",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("location",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("screen",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("Math",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("Number",0,0,0,0,0,3,"font-weight:bold;"),
                        buildItem("console",0,0,0,0,0,3,"font-weight:bold;"),
                    // js
                        buildItem("if  ",0,0,0,"if...","if(){\n\n}",5),
                        buildItem("ifelse",0,0,0,"if...else...","if(){\n\n}else{\n\n}",5),
                        buildItem("while",0,0,0,"while...","while(){\n\n};",5),
                        buildItem("do  ",0,0,0,"do...while","do{\n\n}while();",5),
                        buildItem("try  ",0,0,0,"try...catch...finally","try{\n\n}catch(e){\n\n}finally{\n\n}",5),
                        buildItem("for  ",0,0,0,"for...","for(;;;){\n\n}",5),
                        buildItem("forin  ",0,0,0,"for...in","for( in ){\n\n}",5),
                        buildItem("function",0,0,0,"function...","function(){\n\n}",5),
                        buildItem("switch",0,0,0,"switch...","switch(){\n  case : \n\n  break;\n  case : \n\n  break;\ndefault: \n\n}",5),
                        buildItem("with",0,0,0,"with...","with(){\n\n}",5),

                        buildItem("return",0,0,0,"return","return ",4),
                        buildItem("break",0,0,0,"break","break;",4),
                        buildItem("continue",0,0,0,"continue","continue;",4),
                        buildItem("throw",0,0,0,"throw","throw ",4),
                        buildItem("new ",0,0,0,"new","new ",4),
                        buildItem("delete",0,0,0,"delete","delete ",4),
                        buildItem("arguments",0,0,0,"arguments","arguments",4),
                        buildItem("var",0,0,0,"var","var ",4),
                        buildItem("case",0,0,0,"case","case :",4),
                        buildItem("default",0,0,0,"default","default :",4),

                        buildItem("typeof",0,0,0,"typeof","typeof ",4),
                        buildItem("instanceof",0,0,0,"instanceof","instanceof ",4),

                        buildItem("void",0,0,0,"void","void",4),
                        buildItem("true",0,0,0,"true","true",4),
                        buildItem("false",0,0,0,"false","false",4),
                        buildItem("null",0,0,0,"null","null",4),
                        buildItem("NaN",0,0,0,"NaN","NaN",4),
                        buildItem("undefined",0,0,0,"undefined","undefined",4),
                        buildItem("Infinity",0,0,0,"Infinity","Infinity",4),


                        buildItem("decodeURI()","function"),
                        buildItem("decodeURIComponent()","function"),
                        buildItem("encodeURI()","function"),
                        buildItem("encodeURIComponent()","function"),
                        buildItem("escape()","function"),
                        buildItem("eval()","function"),
                        buildItem("isFinite()","function"),
                        buildItem("parseFloat()","function"),
                        buildItem("parseInt()","function"),
                        buildItem("unescape()","function"),
                        buildItem("Number()","function"),
                        buildItem("String()","function"),

                        //**********************
                        buildItem("keySet[",0,0,0,"PK[0]","PK[0]",5),
                        buildItem("absField",0,0,0,"absField()","absField(filed1,filed2,value,units)",5)
                      ];
                        break;
                }
                case 'new':{
                     list=[
                     
                        buildItem("String","class",0,0,0,"String();",1),
                        buildItem("Object","class",0,0,0,"Object();",1),
                        buildItem("Array","class",0,0,0,"Array();",1),
                        buildItem("RegExp","class",0,0,0,"RegExp();",1),
                        buildItem("Boolean","class",0,0,0,"Boolean();",1),
                        buildItem("Number","class",0,0,0,"Number();",1),
                        buildItem("Date","class",0,0,0,"Date();",1),

                        buildItem("ood.Dom","class",0,0,0,"ood.Dom();",2),
                        buildItem("ood.UIProfile","class",0,0,0,"ood.UIProfile();",2),
                        buildItem("ood.Module","class",0,0,0,"ood.Module();",2),
                        buildItem("ood.Template","class",0,0,0,"ood.Template();",2),
                        buildItem("ood.UI","class",0,0,0,"ood.UI();",2),
                        buildItem("ood.DataBinder","class",0,0,0,"ood.DataBinder();",2)
                    ];
                    for(var i in ood.UI){
                        if(i!='upper' && ood.UI[i].$ood$){
                            list.push(buildItem("ood.UI."+i,"class",0,0,0," "+"ood.UI."+i+"();",2));
                        }
                    }
                    break;
                }
                case 'history':
                case 'screen':
                    break;

                case 'PK[':
                    buildItem("PK['0']",0,0,0,"PK['0']","PK[0]",5)
                    break;

                case 'console':
                    list=[
                        buildItem("log()","function",key)
                    ];
                    break;
                case 'arguments':{
                    list=[
                        buildItem("callee","property",key),
                        buildItem("length","property",key)
                    ];
                    break;
                }
                case 'arguments.callee':{
                    list=[
                        buildItem("caller","property",key),
                        buildItem("length","property",key),
                        buildItem("call()","function",key),
                        buildItem("apply()","function",key)
                    ];
                    break;
                }
                case 'top':
                case 'parent':
                case 'window':{
                    var iframe = document.createElement("iframe");
                    iframe.style.display = "none";
                    document.body.appendChild(iframe);
                    var target=frames[frames.length - 1];
                    list=ns._enumKeys(target,key,false);
                    document.body.removeChild(iframe);
                    break;
                }
                case 'document':{
                    var iframe = document.createElement("iframe");
                    iframe.style.display = "none";
                    document.body.appendChild(iframe);
                    var target=frames[frames.length - 1];
                    list=ns._enumKeys(target.document,key,false);
                    document.body.removeChild(iframe);
                    break;
                }
                case 'Element.prototype':{
                    var iframe = document.createElement("iframe");
                    iframe.style.display = "none";
                    document.body.appendChild(iframe);
                    var target=frames[frames.length - 1];
                    target.document.write("<span>a</span>");
                    list=ns._enumKeys(target.document.body.firstChild,key,false);
                    document.body.removeChild(iframe);
                    break;
                }
                case 'Math':{
                    list=[
                        buildItem("E","property",key),
                        buildItem("LN2","property",key),
                        buildItem("LN10","property",key),
                        buildItem("LOG2E","property",key),
                        buildItem("LOG10E","property",key),
                        buildItem("PI","property",key),
                        buildItem("SQRT1_2","property",key),
                        buildItem("SQRT2","property",key),

                        buildItem("abs(x)","function",key),
                        buildItem("acos(x)","function",key),
                        buildItem("asin(x)","function",key),
                        buildItem("atan(x)","function",key),
                        buildItem("atan2(y,x)","function",key),
                        buildItem("ceil(x)","function",key),
                        buildItem("cos(x)","function",key),
                        buildItem("exp(x)","function",key),
                        buildItem("floor(x)","function",key),
                        buildItem("log(x)","function",key),
                        buildItem("max(x,y,z,...,n)","function",key),
                        buildItem("min(x,y,z,...,n)","function",key),
                        buildItem("pow(x,y)","function",key),
                        buildItem("random()","function",key),
                        buildItem("round(x)","function",key),
                        buildItem("sin(x)","function",key),
                        buildItem("sqrt(x)","function",key),
                        buildItem("tan(x)","function",key)
                    ];
                    break;
                }
                case 'Number':{
                     list=[
                        buildItem("MAX_VALUE","property",key),
                        buildItem("MIN_VALUE","property",key),
                        buildItem("NEGATIVE_INFINITY","property",key),
                        buildItem("POSITIVE_INFINITY","property",key)
                    ];
                    break;
                }
                case "Array.prototype":{
                    list=[
                        buildItem("length","property",key),
                        buildItem("concat()","function",key),
                        buildItem("join()","function",key),
                        buildItem("pop()","function",key),
                        buildItem("push()","function",key),
                        buildItem("reverse()","function",key),
                        buildItem("shift()","function",key),
                        buildItem("sort()","function",key),
                        buildItem("concat()","function",key),
                        buildItem("splice()","function",key),
                        buildItem("toString()","function",key),
                        buildItem("unshift()","function",key)
                    ];
                    break;
                }
                case "Date.prototype":{
                    list=[
                        buildItem("getDate()","function",key),
                        buildItem("getDay()","function",key),
                        buildItem("getFullYear()","function",key),
                        buildItem("getHours()","function",key),
                        buildItem("getMilliseconds()","function",key),
                        buildItem("getMinutes()","function",key),
                        buildItem("getMonth()","function",key),
                        buildItem("getTime()","function",key),
                        buildItem("getSeconds()","function",key),
                        buildItem("getTimezoneOffset()","function",key),
                        buildItem("getUTCDate()","function",key),
                        buildItem("getUTCDay()","function",key),
                        buildItem("getUTCFullYear()","function",key),
                        buildItem("getUTCHours()","function",key),
                        buildItem("getUTCMilliseconds()","function",key),
                        buildItem("getUTCMinutes()","function",key),
                        buildItem("getUTCMonth()","function",key),
                        buildItem("getUTCSeconds()","function",key),
                        buildItem("getYear()","function",key),
                        buildItem("parse()","function",key),
                        buildItem("setDate()","function",key),
                        buildItem("setFullYear()","function",key),
                        buildItem("setHours()","function",key),
                        buildItem("setMilliseconds()","function",key),
                        buildItem("setMinutes()","function",key),
                        buildItem("setMonth()","function",key),
                        buildItem("setSeconds()","function",key),
                        buildItem("setTime()","function",key),
                        buildItem("setUTCDate()","function",key),
                        buildItem("setUTCFullYear()","function",key),
                        buildItem("setUTCHours()","function",key),
                        buildItem("setUTCMilliseconds()","function",key),
                        buildItem("setUTCMinutes()","function",key),
                        buildItem("setUTCSeconds()","function",key),
                        buildItem("setUTCMonth()","function",key),
                        buildItem("setYear()","function",key),
                        buildItem("toDateString()","function",key),
                        buildItem("toGMTString()","function",key),
                        buildItem("toLocaleDateString()","function",key),
                        buildItem("toLocaleTimeString()","function",key),
                        buildItem("toLocaleString()","function",key),
                        buildItem("toString()","function",key),
                        buildItem("toUTCString()","function",key),
                        buildItem("toTimeString()","function",key),
                        buildItem("UTC()","function",key)
                    ];
                    break;
                }
                case "Number.prototype":{
                    list=[
                        buildItem("toExponential()","function",key),
                        buildItem("toFixed()","function",key),
                        buildItem("toPrecision()","function",key),
                        buildItem("toString()","function",key)
                    ];
                    break;
                }
                case "Boolean.prototype":{
                    list=[
                        buildItem("toString()","function",key)
                    ];
                    break;
                }
                case "Function.prototype":{
                    list=[
                        buildItem("toString()","function",key),
                        buildItem("call()","function",key),
                        buildItem("apply()","function",key)
                    ];
                    break;
                }
                case "RegExp.prototype":{
                    list=[
                        buildItem("global","property",key),
                        buildItem("ignoreCase","property",key),
                        buildItem("multiline","property",key),
                        buildItem("lastIndex","property",key),
                        buildItem("source","property",key),
                        buildItem("compile()","function",key),
                        buildItem("exec()","function",key),
                        buildItem("test()","function",key)
                    ];
                    break;
                }
                case "String.prototype":{
                    list=[
                        buildItem("length","property",key),
                        buildItem("charAt()","function",key),
                        buildItem("charCodeAt()","function",key),
                        buildItem("concat()","function",key),
                        buildItem("fromCharCode()","function",key),
                        buildItem("indexOf()","function",key),
                        buildItem("lastIndexOf()","function",key),
                        buildItem("match()","function",key),
                        buildItem("replace()","function",key),
                        buildItem("search()","function",key),
                        buildItem("slice()","function",key),
                        buildItem("split()","function",key),
                        buildItem("substr()","function",key),
                        buildItem("substring()","function",key),
                        buildItem("toLowerCase()","function",key),
                        buildItem("toUpperCase()","function",key)
                    ];
                    break;
                }
                case "Object.prototype":{
                    list=[
                        buildItem("toString()","function",key,0,0,0,'z'),
                        buildItem("toLocaleString()","function",key,0,0,0,'z'),
                        buildItem("hasOwnProperty(key)","function",key,0,0,0,'z'),
                        buildItem("propertyIsEnumerable(key)","function",key,0,0,0,'z'),
                        buildItem("isPrototypeOf(obj)","function",key,0,0,0,'z')
                     ];
                    break;
                }
                default:{
                    if(/\.prototype$/.test(key)){
                        // [[[[[ bak
                        var bak=ood.Dom.pack,
                            bak1=ood.absProfile.prototype.$link,
                            bak11 = ood.absObj.$pickAlias;

                        ood.Dom.pack=function(){var o=new ood.Dom(false),d=document.getElementById('abc');o._nodes=[d];return o};
                        ood.absProfile.prototype.$link=function(){return this;};
                        ood.absObj.$pickAlias = function(){return ns._alias++};
                        // ]]]]]
                        try{
                            var target= eval("new "+key.replace(/\.prototype$/,''));
                            list=ns._enumKeys(target,key);
                        }catch(e){}finally{
                        //[[[[[ restore
                            ood.Dom.pack=bak;
                            ood.absProfile.prototype.$link=bak1;
                            ood.absObj.$pickAlias = bak11;
                        //]]]]]
                        }
                    }

                    if(!list.length){
                        var target=ood.get(window,key.split("."));
                        list=ns._enumKeys(target,key);
                        cachedata=false;
                    }
                }
            }

            if(cachedata)
                cache[key]=list;

            return list;
        },
        _getFromFunctionExp:function(code){
            var type;
            if(/toString\([^)]*\)$/.test(code))
                type='String.prototype';
            // functions patterns
            else if(/^\s*\(?\s*new\s+([\wood.]+)\s*\(?\s*[^)]*\)?\s*\)?(\s*\.\s*(set|on|before|after)[\w]+\s*\([^)]*\)\s*)*$/.test(code)){
                code  = code.replace(/^\s*\(?\s*new\s+([\wood.]+)\s*\(?\s*[^)]*\)?\s*\)?(\s*\.\s*(set|on|before|after)[\w]+\s*\([^)]*\)\s*)*$/, "$1");
                type=code+'.prototype';
            }
            else if(/.*\.reBoxing\(([^)]*)\)(\s*\.\s*(set|on|before|after)[\w]+\s*\([^)]*\)\s*)*$/.test(code)){
                code = code.replace(/.*\.reBoxing\(([^)]*)\)(\s*\.\s*(set|on|before|after)[\w]+\s*\([^)]*\)\s*)*$/,"$1");
                if(code)
                    type=code.replace(/[\'\"]/g,"")+'.prototype';
                else
                    type='ood.Dom.prototype';
            }
            else if(code.indexOf("ood.alert(")==0 || code.indexOf("ood.prompt(")==0|| code.indexOf("ood.pop(")==0|| code.indexOf("ood.confirm(")==0)
                type='ood.UI.Dialog.prototype';
            else if(code.indexOf("ood(")==0 || code.indexOf("ood.use(")==0)
                type='ood.Dom.prototype';
            else if(code.indexOf("ood.Thread(")==0 || code.indexOf("ood.Ajax(")==0
                || code.indexOf("ood.SAjax(")==0 ||  code.indexOf("ood.IAjax(")==0 
                || code.indexOf("ood.JSONP(")==0 ||  code.indexOf("ood.XDMI(")==0 )
                type='ood.Thread.prototype';
            else if(/.*\.(getRoot|getSubNode|getSubNodes)\s*\(\s*[^\)]*\)\s*$/.test(code))
                type='ood.Dom.prototype';
            else if(/.*\.getElementById\s*\(\s*[^\)]*\)\s*$/.test(code))
                type='Element.prototype';
            return type;
        },
        _getFromObject:function(target){
            if(!ood.isSet(target))
                return null;
            else if(target===ood.str)
                return "ood.str";
            else if(target===ood.arr)
                return "ood.arr";
            else if(target===ood)
                return "ood";
            else if(target.$ood$){
                if(typeof target == 'function')
                    return target.KEY;
                else
                    return target.KEY+".prototype";
            }else if(ood.isArr(target))
                return "Array.prototype";
            else if(ood.isDate(target))
                return "Date.prototype";
            else if(ood.isReg(target))
                return "RegExp.prototype";
            else if(ood.isNumb(target))
                return "Number.prototype";
            else if(ood.isStr(target))
                return "String.prototype";
            else if(ood.isBool(target))
                return "Boolean.prototype";
            else if(ood.isFun(target))
                return "Function.prototype";
            else if(ood.isObj(target)){
                return "Object.prototype";
            }
            // dont use ood.isHash here
        },
        _findVars:function(profile, tonode, braceId){
            var ns=this,
                cm=profile.$editor,
                arr=[];
            if(cm && tonode){
                var doc=cm.win.document,
                    isBr=function(elem){return elem.tagName=="BR" || elem.tagName=="br"},
                    isB=function(elem){return elem.tagName=="B" || elem.tagName=="b"},
                    elem=doc.body.firstChild,
                    scope="",
                    cls,txt,temp=[],cursor;

                cursor=arr;

                cursor.push(["this",""]);

                while((elem=elem.nextSibling) && elem!=tonode){
                    if(isB(elem) && elem._uid && elem.id.charAt(0)=='b'){
                        // if sibling {, jump to it's }
                        if(!braceId || braceId.indexOf(elem.id)!==0){
                            elem=doc.getElementById('e'+elem.id.slice(1));
                        }else{
                            scope=elem._uid;
                            // reset scope
                            ood.arr.each(temp,function(o){
                                o[1]=scope;
                            });
                            arr = arr.concat(temp);
                        }
                        temp=[];
                        cursor=arr;
                    }

                    cls=ns.__trim(elem.className);
                    txt=ns.__trim(elem.currentText);

                    if(cls=='js-keyword' && txt=="function"){
                        
                        // find "function abc(){}"
                        while((elem=elem.nextSibling) && elem!=tonode && (isBr(elem)||ns.__trim(elem.className)=="whitespace"||ns.__trim(elem.className)=="js-comment"));
                        cls=ns.__trim(elem.className);
                        txt=ns.__trim(elem.currentText);
                        if(cls=='js-variabledef'){
                            cursor.push([txt,scope]);
                        }

                        // temp find
                        cursor=temp;
                        cursor.push(["this",scope]);
                    }

                    if(cls=='js-variabledef')
                        cursor.push([txt,scope]);
                }
            }
            return arr;
        },
        
        _getIntellisense:function(profile, key, index, nolist){
            var ns=this,
            _this=RAD.ClassTool._this;

            // get type
            if(key.indexOf('new ')==0){
                type='new';
            }else if(key.charAt(key.length-1)=='.'){
                type="properties";
                key=key.slice(0,-1);
            }else{
                if(key.indexOf('.')!=-1){
                    type="properties";
                    key = key.slice(0,key.lastIndexOf('.'));
                }else{
                    // return avialable var
                    type="var";
                }
            }
            if(type=="properties"){
                if(key.indexOf('(')!=-1){
                    type="function";
                }
            }

            var target,code,
                list=[],
                // object type string
                otype, 
                // object code string
                ocode, 
                // extra array
                oextra=[];
            switch(type){
                case 'new':
                    otype=type;
                    break;
                case "function":
                    type=ns._getFromFunctionExp(key);
                    if(type){
                        otype=type;
                        break;
                    }
                case "properties":
                    if(key=="arguments"||key=="arguments.callee"){
                        otype=key;
                        break;
                    }else{                        
                        // try exec all code dir
                        var arrcode=[],
                            vars=[],
                            code=profile.$cm.getValue(),
                            allcode=RAD.ClassTool.getContextAssignments(code,index),
                            struct=RAD.ClassTool.getClassStructWithScope(code,index, ns.$temppool, profile.$xid);

                        arrcode.push("(function(){try{\n");
                        arrcode.push("var "+_this+"=window;\n");
                        vars.push(_this);
                        var added1=false,added2=false,
                            len=allcode.vars.length;
                        for(var i=0;i<len;i++){
                            arrcode.push("var "+allcode.vars[i]+"=undefined;\n");
                            vars.push(allcode.vars[i]);
                        }
                        // run once at least
                        for(var i=-1;i<len;i++){
                            // no assignment, or assignment is in special scope
                            if(allcode.scopeS==-1 || i>=allcode.scopeS){
                                // add only once
                                if(!added1){
                                    added1=true;
                                    var o;
                                    // get scope info
                                    if(allcode.secondKey && allcode.secondType=="function"){
                                        o=ood.get(struct,['sub',allcode.firstKey,'sub',allcode.secondKey]);                                                
                                    }else if(allcode.firstKey&&allcode.firstType=="function"){
                                        o=ood.get(struct,['sub',allcode.firstKey]);
                                    }
                                    if(o){
                                        // add scope code
                                        arrcode.push("try{"+_this+"="+o._this+"}catch(e){};\n");
                                        // add args code
                                        ood.arr.each(o.argsv,function(arg,i){
                                            // no matching argument
                                            if(!o.args||!o.args[i])return false;

                                            // use dynamic code first
                                            if(arg.code){
                                                arrcode.push("try{"+o.args[i]+"="+ arg.code +"}catch(e){};\n");
                                                vars.push(o.args[i]);
                                            }else{
                                                if (/\.prototype$/.test(arg.type)){
                                                    arrcode.push("try{"+o.args[i]+"="+ arg.type.replace(/(.+)(\.prototype)$/, "new $1();") +"}catch(e){};\n");
                                                    vars.push(o.args[i]);
                                                }else{
                                                    arrcode.push("try{"+o.args[i]+"="+ arg.type +"}catch(e){};\n");
                                                    vars.push(o.args[i]);
                                                }
                                            }   
                                        });
                                    }
                                }
                            }
                            // out of special scope
                            if(allcode.scopeS!=-1 && i>=allcode.scopeE){
                                if(!added2){
                                    added2=true;
                                    arrcode.push("try{"+_this+"=window;}catch(e){};\n");
                                }
                            }
                            // ignore when i is -1
                            if(i>=0){
                                arrcode.push("try{"+allcode.vars[i]+"="+allcode.code[i]+";}catch(e){};\n");
                                vars.push(allcode.vars[i]);
                            }
                        }
                        if(allcode.scopeS!=-1 && allcode.outscope){
                            if(!added2){
                                arrcode.push("try{"+_this+"={};}catch(e){};\n");
                            }
                        }
                        
                        arrcode.push("return (" + key + ");\n}catch(e){}finally{\n");
                        ood.arr.removeValue(vars,key);
                        arrcode.push(vars.join("=")+"=null;\n");
                        arrcode.push("}\n}).call({});\n");
                        ocode = arrcode.join('').replace(/\bthis\b/g,_this);
//console.log(ocode);
                    }
                    break;
                case "var":
                    otype=" variables ";
                    
                    // caching according to line
                   /*
                    var hash,cache=ns.__varscache||(ns.__varscache={});
                    if(cache[index]){
                        hash=cache[index];
                    }else{
                        cache=ns.__varscache={};
                        cache[index]=hash=RAD.ClassTool.getContextVars(profile.$cm.getValue(),index);
                    }
                   */
                    var hash=RAD.ClassTool.getContextVars_fast(profile.$cm, index);

                    if(hash["this"]){
                        oextra.push(ns._buildItem("this",0,0,0,0,0,1,"color:#ff0000;"));
                        delete hash["this"];
                    }
                    if(hash["arguments"]){
                        oextra.push(ns._buildItem("arguments",0,0,0,0,0,1,"color:#ff0000;"));
                        delete hash["arguments"];
                    }
                        
                    ood.each(hash,function(o,i){
                        oextra.push(ns._buildItem(i,"var",0,0,0,0,1,"color:#666;"));
                    });
                    break;
            }
            // from extra array
            if(oextra && oextra.length){
                if(!nolist)
                    Array.prototype.push.apply(list, oextra);
            }
            // then "from code"
            if(ocode){
                result = ns.evalInSandbox(ocode,false,null,profile.$xid);
                if(ood.isSet(result.ok)){
                    // if result.type exists, igonre result.ok
                    if(result.type)
                        otype=result.type;
                    else{
                        otype = ns._getFromObject(result.ok);
                        if(!nolist)
                            Array.prototype.push.apply(list, ns._enumKeys(result.ok, otype, true, true));
                    }
                }
            }
            // the last one is "from type"
            if(otype){
                if(!nolist)
                    Array.prototype.push.apply(list, ns._getIntellisenseList(otype));
            }

            if(list.length){
                list = ood.arr.removeDuplicate(list, 'id');

                ood.arr.stableSort(list,function(x,y){
                    return x.tag>y.tag?1:x.tag==y.tag?0:-1;
                });
            }

            return [otype, list, key];
        },
        _loadEditor:function(profile){
            var ns=this,
                _this=RAD.ClassTool._this;

            // is suggestion window showed
            var $suggestionShowed=false,
                $suggestionBaseScope=null,
                $suggestionBaseScopeShadow=null,
                showSuggetionID="CodeEditor.showSuggetion.resetRun",
                adjustSuggetionID="CodeEditor.adjustSuggetion.resetRun",
                $currentText="",
                $willshowDotHint=false,
                // suggestion window for
                $activeWord;

            // get short Suggestion code
            var getShortContentCode=function(index){
                var reg=/\s+/g,
                    arr=[],
                    extxt="",
                    cm=profile.$cm,
                    pos=cm.doc.posFromIndex(index),
                    line=pos.line,
                    elem=cm.getTokenAt(pos),
                    prev=function(elem){
                        if(elem.start===0){
                            pos.line--;
                            if(pos.line===0){
                                return null;
                            }
                            var line=cm.doc.getLine(pos.line);
                            if(line){
                                pos.ch=line.length;
                            }else{
                                return null;
                            }
                        }else{
                            pos.ch=elem.start;
                        }
                        return cm.getTokenAt(pos);
                    };

                // for dblclick case, find key text
                if(!(elem.type===null && elem.string==".")){
                    extxt=ns.__trim(elem.string);
                    elem=prev(elem);
                }
                // for dot case 
                if(elem && (elem.type===null && elem.string==".")){
                    do{
                        if(elem.type===null && elem.string=="."){
                            // ignore space and comments
                            while(elem=prev(elem)){
                                if(!(!elem || elem.type=="comment" ||elem.type=="tab" || (elem.type===null && /^\s+$/.test(elem.string)))){
                                    break;
                                }
                            }
                            if(elem){
                                // for (xxx)
                                var con1="";
                                if(elem.type===null && elem.string===")"){
                                    var deep1=0;
                                    do{
                                        if(elem.type===null && elem.string===")")
                                            deep1++;
                                        if(elem.type===null && elem.string==="(")
                                            deep1--;
                                        if(elem.string)
                                            con1 = elem.string+con1;
                                    }while((elem=prev(elem)) && deep1!==0)
                                }
                                if(elem){
                                    if(elem.type=="property" || elem.type=='variable' || elem.type=='variable-2'
                                    || elem.type=='oodglobal'
                                    ||(elem.type=="keyword"&&elem.string=="this")){
                                        arr.push(elem.string.replace(reg,'') + con1);
                                    }else if(con1){
                                        arr.push(con1);
                                    }else{
                                        break;
                                    }
                                        
                                }
                            }
                        }else{
                            break;
                        }
                    }while(elem=prev(elem))
                }
                // for space case
                else if(elem && (elem.type==null && elem.string===" ")){
                    // ignore space and comments
                    while(elem=prev(elem)){
                        if(!(!elem || elem.type=="comment" || (elem.type==null && elem.string===" "))){
                            break;
                        }
                    }
                    if(elem.type=="keyword" && elem.string=="new"){
                        extxt = "new " + extxt;
                    }
                }

                return (arr.length?(arr.reverse().join('.')+'.'):"")+extxt;
            };

            var iniComponents=function(){
                // [[Code created by ESDUI RAD Studio
                var host=this, children=[], append=function(child){children.push(child.get(0))};
                
                append(
                    (new ood.UI.Pane)
                    .setHost(host,"$popList")
                    .setLeft(10)
                    .setTop(10)
                    .setWidth(300)
                    .setHeight(180)
                );
                
                host.$popList.append(
                    (new ood.UI.List)
                    .setHost(host,"$lstsug")
                    .setTheme("codemirror")
                    .setDirtyMark(false)
                    .setTop(18)
                    .setWidth(300)
                    .setHeight(162)
                    .setItemRow(true)
                    .setDisableHoverEffect(true)
                );
                
                host.$popList.append(
                    (new ood.UI.Block)
                    .setHost(host,"block1")
                    .setLeft(0)
                    .setTop(0)
                    .setWidth(300)
                    .setHeight(24)
                    .setBorderType("flat")
                );
                
                host.block1.append(
                    (new ood.UI.Label)
                    .setHost(host,"$objType")
                    .setLeft(6)
                    .setTop(2)
                    .setWidth(252)
                    .setCaption("")
                    .setHAlign("left")
                    .setCustomStyle({KEY:'font-weight:bold;'})
                );
                
                append(
                    (new ood.UI.Block)
                    .setHost(host,"$popHelp")
                    .setLeft(410)
                    .setTop(10)
                    .setWidth(300)
                    .setHeight(180)
                    .setBorderType("flat")
                    .setOverflow('hidden')
                    .setBackground("#FFF8DC")
                );
                
                host.$popHelp.append(
                    (new ood.UI.Div)
                    .setHost(host,"$divhelp")
                    .setTheme("codemirror")
                    .setLeft(-1)
                    .setTop(19)
                    .setWidth(300)
                    .setHeight(160)
                    .setCustomStyle({"KEY":"overflow:auto; padding:4px;"})
                );
                
                host.$popHelp.append(
                    (new ood.UI.Block)
                    .setHost(host,"block3")
                    .setLeft(-1)
                    .setTop(-1)
                    .setWidth(300)
                    .setHeight(20)
                    .setBorderType("flat")
                );
                
                host.block3.append(
                    (new ood.UI.Link)
                    .setHost(host,"$linkAPI")
                    .setLeft('auto')
                    .setRight(2)
                    .setTop(1)
                    .setCaption("$RAD.JSEditor.clickapi")
                );
                
                host.block3.append(
                    (new ood.UI.Label)
                    .setHost(host,"$lblTips")
                    .setLeft(6)
                    .setTop(2)
                    .setWidth(133)
                    .setCaption("")
                    .setHAlign("left")
                );
                
                return children;

                // ]]Code created by ESDUI RAD Studio
            };
            
            var handle={
                moveFocus:function(step){
                 var list = profile.$lstsug,
                    items = list.get(0).properties.items,
                    length=items.length,
                    value = list.getUIValue(),
                    index = ood.arr.subIndexOf(items, 'id', value);
                    if(step===true){
                        index=0;
                    }else if(step===false){
                        index=length-1;
                    }else{
                        index+=step;
                        if(index<0)index=0;
                        if(index>length-1)index=length-1;
                    }
                    if(items[index])
                        list.setUIValue(items[index].id,true);
                },
                close:function(){
                    hideSuggestion();
                },
                pick:function(){
                 var list = profile.$lstsug,
                    items = list.get(0).properties.items,
                    length=items.length,
                    value = list.getUIValue(),
                    index = ood.arr.subIndexOf(items, 'id', value);
                    
                    if(items[index])
                        applySuggestion(items[index].value||items[index].caption);
                    hideSuggestion();
                }
            };
            var keyMap={
              Up: function() {handle.moveFocus(-1);},
              Down: function() {handle.moveFocus(1);},
              PageUp: function() {handle.moveFocus(-8, true);},
              PageDown: function() {handle.moveFocus(8, true);},
              Home: function() {handle.moveFocus(true);},
              End: function() {handle.moveFocus(false);},
              Enter: function() {handle.pick();},
              Tab: function() {handle.pick();},
              Esc: function() {handle.close();}
            };
            // show suggestion
            var showSuggestion=function(key, index, fromDot, dftTxt){
                // get intellisense
                var rtn = profile.box._getIntellisense(profile, key, index),
                    keyword=rtn[0],
                    items=rtn[1];
                dftTxt=dftTxt||key||"";

                if(!keyword && !(items && items.length)){
                    hideSuggestion();
                    return ;
                }

                if(!fromDot){
                    var found=false,str=dftTxt.toLowerCase();
                    ood.arr.each(items,function(item){
                        if(ood.str.startWith(item.id.toLowerCase(),str)){
                            found=true;
                            return false;
                        }
                    });
                    if(!found){
                        return;
                    }
                }
                var coordinates =cm.cursorCoords(true,"page"),
                    left=coordinates.left ,
                    top=coordinates.top ;

                if(!profile.$popList){
                    iniComponents.call(profile);
                    ood('body').append(profile.$popList.setDisplay('none'));
                    ood('body').append(profile.$popHelp.setDisplay('none'));
                    
                    profile.$divhelp.getRoot().setSelectable(true);
                    
                    profile.$linkAPI.onClick(function(p){
                        CONF.openAPI(p.properties.tag);
                    });
                    var out=true,
                        tryToClose=function(){
                            ood.resetRun("codemirror:aaa",function(){
                                if(out && profile.$popCustom){
                                    profile.$popCustom.setDisplay('none');
                                }
                            });
                        };
                    
                    profile.$lstsug.afterUIValueSet(function(profile,ov,v){
                        showTips2(profile.getItemByItemId(v));
                    })
                    .onClick(function(profile,item){
                        applySuggestion(item.value||item.caption);
                        hideSuggestion();
                    });
                }

                if((items && items.length)|| fromDot){
                    $suggestionShowed=fromDot?1:true;
                    if(items && items.length){
                        profile.$lstsug.setItems(items);
                    }
                    var fun=function(){
                        hideSuggestion();
                    },
                    group=ood([profile.$popList.getRootNode(), profile.$popHelp.getRootNode(), profile.getSubNode("BOX").get(0)]);
                    
                    profile.$popList.getRoot().popToTop({region:{
                        left:left,
                        top:top,
                        width:2,
                        height:16
                    }},1)
                    .setBlurTrigger(showSuggetionID,fun,group);
                    ood.Event.keyboardHook('esc',0,0,0,fun);

                    profile.$popList.setDisplay('');
                    
                    $activeWord=rtn[2]||null;
                    
                    if(items && items.length){
                        adjustSuggetion(left, top, dftTxt,"",true,true);
                    }
                }

                profile.$objType.setCaption(keyword||"none");
                
                // remove first
                profile.$cm.removeKeyMap(keyMap);
                profile.$cm.addKeyMap(keyMap);
            };
            
            // hide suggestion
            var hideSuggestion=function(){
                ood.Event.keyboardHook('esc',0,0,0);
                
                profile.$cm.removeKeyMap(keyMap);
                
                ood.resetRun(showSuggetionID);
                ood.resetRun(adjustSuggetionID);

                $suggestionBaseScope=null;
                $suggestionBaseScopeShadow=null;
                $suggestionShowed=false;
                $willshowDotHint=false;

                if(profile.$popList){
                    profile.$objType.setCaption('');
                    profile.$lstsug.setItems([]).setUIValue(null);                    
                    profile.$popList.setDisplay('none');

                    profile.$lblTips.setCaption('');
                    profile.$divhelp.setHtml("");
                    profile.$popHelp.setDisplay('none');
                    
                    if(profile.$popCustom)
                        profile.$popCustom.setDisplay('none');
                    
                    $currentText=null;
                }
            };
            
            // adjust selection and position for suggestion win
            var adjustSuggetion=function(left, top, txt, extxt, forVisibleOnly, asnyc){
                if(!profile.$lstsug)return;
                var prf=profile.$lstsug.get(0),
                    items=prf.properties.items,
                    f=function(){
                        if(forVisibleOnly && !$suggestionShowed)return;
                        
                        var fid;
                        if(extxt)txt+=extxt;
                        if(!ood.isSet($currentText) || txt!==$currentText){
                            $currentText=txt;
                            // try to find startWith
                            ood.arr.each(items,function(item){
                                if(ood.str.startWith(item.id.toLowerCase(), $currentText.toLowerCase())){
                                    fid=item.id;
                                    return false;
                                }
                            });
                            // try to find similiar
                            while(!fid && txt.length){
                                txt = txt.substring(0,txt.length-1);
                                if(!txt)break;
                                ood.arr.each(items,function(item){
                                    if(ood.str.startWith(item.id.toLowerCase(), txt.toLowerCase())){
                                        fid=item.id;
                                        return false;
                                    }
                                });
                            }
                            // the frist one
                            if(!fid){
                                fid=items[0].id;
                            }
                        }
                        if(!prf.properties.$UIvalue && !fid && prf.properties.items && prf.properties.items.length)
                            fid=prf.properties.items[0].id;
                        if(fid)
                            prf.boxing().setUIValue(fid);
                        else{
                            hideSuggestion();
                            return;
                        }
                        
                        // POSITION
                        profile.$popList.getRoot().popToTop({region:{
                            left:left,
                            top:top,
                            width:2,
                            height:16
                        }},1)
                        if(profile.$popHelp && profile.$popHelp.getDisplay()!='none'){
                            profile.$popHelp.getRoot().popToTop(profile.$popList.getRoot(),2)
                        }
                    };
                if(!asnyc)
                    f();
                 else
                    ood.resetRun(adjustSuggetionID, f,100);
            };
            
            var showTips=function(key, str){
                if(!profile.$popHelp)return;
                if(profile.$popList.getDisplay()!='none'){
                    profile.$linkAPI.setTag(key);
                    profile.$lblTips.setCaption(key);

                    profile.$divhelp.setHtml(str);
                    profile.$popHelp.getRoot().popToTop(profile.$popList.getRoot(),2)
                    profile.$popHelp.setDisplay('');
                }
            };
            var hideTips=function(key, str){
                if(!profile.$popHelp)return;

                profile.$linkAPI.setTag(null);
                profile.$lblTips.setCaption("");
                profile.$divhelp.setHtml("");
                profile.$popHelp.setDisplay("none");
            };
            // show tips
            var showTips1=function(elem){
                if(!profile.$popHelp)return;
                var key=getShortContentCode(elem);
                var str = profile.onGetHelpInfo &&  profile.boxing().onGetHelpInfo(profile, key.key);
//                if(!str)
//                    str=key.key;
                if(str){
                    showTips(key.key,str);
                }else{
                    hideTips();
                }
            };
            var showTips2=function(item){
                if(!profile.$popHelp)return;
                if(!item)return;
                var str = profile.onGetHelpInfo &&  profile.boxing().onGetHelpInfo(profile, item.path);
//                if(!str)
//                   str=item.path;
                if(str){
                    showTips(item.path,str);
                }else{
                    hideTips();
                }
            };

            // show Intellisense  from dot
            var showDotHint=function(index,scope,dftTxt,scopyShadow){
                $suggestionBaseScope=$suggestionBaseScopeShadow=null;
                // hide list first
                //hideSuggestion();
                $willshowDotHint=true;
                ood.resetRun(showSuggetionID,function(){
                    var key=getShortContentCode(index);
                    if(!key){
                        hideSuggestion();
                    }else{
                        $willshowDotHint=false;

                        if(key!=dftTxt)
                            showSuggestion(key, index, true, dftTxt);
                    }
                },100);
                $suggestionBaseScope=scope;
                $suggestionBaseScopeShadow=scopyShadow;
            };
            // show vars hint
            var showVarsHint=function(key, index, scope){
                ood.resetRun(showSuggetionID,function(){
                    showSuggestion(key, index);
                });
                // refresh
                $suggestionBaseScope=scope;
            };
            // show vars hint
            var tryToAdjustHint=function(key,index, scope){
                var sc=$suggestionBaseScopeShadow||$suggestionBaseScope;
                if(sc && sc.line==scope.line && sc.start==scope.start){
                    var coordinates =cm.cursorCoords(true,"page"),
                        left=coordinates.left ,
                        top=coordinates.top ;    
                    adjustSuggetion(left, top, key, "", true);
                }
                // refresh
                $suggestionBaseScope=scope;
            };
            // apply suggestion to code
            var applySuggestion=function(value){
                var editor=profile.$cm,o=$suggestionBaseScope;
                editor.replaceRange(value, 
                    {line:o.line, ch:o.start},
                    {line:o.line, ch:o.end});
                
                // indentat multi line
                var line=o.line, hasE;
                value.replace(/\n/g,function(){
                    editor.indentLine(++line);
                    hasE=1;
                });
                editor.setCursor({line:o.line+(hasE?1:0), ch:o.start+value.length});
                editor.execCommand("indentAuto");
            };
            var codeType=profile.properties.codeType,
                mode = CONF.getRenderMode(profile.properties.codeType);
            if(!mode)mode=CONF.getRenderMode("txt");

            // load codemirror
            var options={
                value:profile.properties.value||"",

                smartIndent:true,
                indentUnit:4,
                tabSize:4,
                indentWithTabs:false,

                "mode": mode.mime,
                keyMap: "sublime",
                
                matchBrackets: true,
                continueComments: "Enter",
                autoCloseBrackets:true,
                lineNumbers:true,
                lint:(codeType=='js'||codeType=='json'||codeType=='css'||codeType=='html')?true:false,
                readOnly:profile.properties.readonly,
                styleActiveLine: true,
                lineWrapping: false,
                flattenSpans:false,
                highlightSelectionMatches: {showToken: /^[$\w]\w*/},
    	        foldGutter:true,
                gutters: ["CodeMirror-lint-markers","CodeMirror-linenumbers", "CodeMirror-foldgutter"],
                extraKeys: {
                    'Ctrl':function(){
                        hideSuggestion();
                    },
                    "Ctrl-Q": function(cm){ cm.foldCode(cm.getCursor()); },
                    "Ctrl-S": function(){
                        hideSuggestion();
                        if(profile.onSaveCommand)
                            profile.boxing().onSaveCommand(profile);
                    },
                    "Ctrl-F": function(cm){
                        hideSuggestion();
                        RAD.EditorTool.showFindWnd(null,profile.boxing(),{left:0,top:0},cm.getSelection());
                    },
                    "Tab": function(cm) {
                        if(cm.doc.somethingSelected())return CodeMirror.Pass;
                        var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
                        cm.replaceSelection(spaces, "end", "+input");
                    },
                    "Ctrl-D":function(cm){
                        cm.execCommand("deleteLine");
                    }
                }
            };
            
            var obj=CONF.getStatus(),
                editorTheme=obj.editorTheme,
                editorFontSize=obj.editorFontSize,
                lineWrapping=obj.lineWrapping;

            if(editorTheme){
                // include CSS first
                profile.boxing().addCMTheme(editorTheme);
                options.theme=editorTheme;
            }
            if(ood.isSet(lineWrapping)){
                options.lineWrapping=!!lineWrapping;
            }

            var cm = profile.$cm = new CodeMirror(function(elt){
                profile.getSubNode('BOX').append(elt);
                
                if(editorFontSize)
                    elt.style.fontSize=editorFontSize+"px";
            }, options);

            //cm.setOption("mode", mode);
            CodeMirror.modeURL = "/plugins/codemirror5/mode/%N/%N.js";

            CodeMirror.autoLoadMode(cm, mode.mode);

            if(profile.onCodeModeSet){
                ood.asyRun(function(){
                    if(!profile.box)return;
                    profile.boxing().onCodeModeSet(profile, mode);
                });
            }
            
            if(mode.mode=="null"){
                if(!profile.$initialized)
                    profile.$initialized=true;
                if(profile.onRendered)
                    profile.boxing().onRendered(profile, true);
            }else{
                cm.on("rendered",function(cm,finished){
                    if(!profile.$initialized)
                        profile.$initialized=true;
                    if(profile.onRendered)
                        profile.boxing().onRendered(profile, finished);
                });
            }
            cm.on("change",function(cm,obj){
                if(profile.$initialized && profile.onValueChanged)
                    profile.boxing().onValueChanged(profile, cm.doc.getValue()!=profile.properties.value);
                if(codeType!='js'&&codeType!='html') return;

                var handled;
                if(obj.origin=="+input"){
                    // igore redo/undo
                    if(obj.text[0]){
                        if(obj.text[0]=="."){
                            // only for js file
                            if(codeType!='js') return;
                            
                            obj.to.ch++;
                            var token=cm.getTokenAt(obj.to),type=token.type;
                            if(type!==null)
                                return;
                            // show suggestion hint for : token.string
                            var pos=cm.getCursor();
                            showDotHint(cm.doc.indexFromPos(pos), {start:++token.start, end:++token.end, line:pos.line});
                            handled=true;
                        }else if(obj.text[0]!==" "){
                            obj.to.ch++;
                            var token=cm.getTokenAt(obj.to),type=token.type;
                            if(type=="variable"||type=="variable-2"||type=="keyword"||type=="property"){
                                var pos=cm.getCursor();
                                if(pos.line==obj.to.line && pos.ch>=token.start && pos.ch<=token.end){
                                    if($suggestionShowed)
                                        tryToAdjustHint(token.string, cm.doc.indexFromPos(pos), {start:token.start, end:token.end, line:pos.line});
                                    else{
                                        if(type=='property'){
                                            // only for js file
                                            if(codeType!='js') return;
                                            var t2=cm.getTokenAt({line:obj.to.line, ch:obj.to.ch - token.string.length});
                                            if(t2.type!==null || t2.string!=='.')
                                                return;
                                            // show suggestion hint for : token.string
                                            showDotHint(cm.doc.indexFromPos(pos) - obj.text.length - 1,null,token.string,{start:++t2.start, end:++t2.end, line:pos.line});
                                        }else{
                                            showVarsHint(token.string, cm.doc.indexFromPos(pos), {start:token.start, end:token.end, line:pos.line});
                                        }
                                    }
                                    handled=true;
                                }
                            }
                        }
                    }
                }else if(obj.origin=="+delete"){
                    if($suggestionShowed){
                        var token=cm.getTokenAt(obj.to),type=token.type,str=token.string;
                        if($suggestionShowed===true){
                            if(type=="variable"||type=="variable-2"||type=="keyword"){
                                var pos=cm.getCursor();
                                if(pos.line==obj.to.line && pos.ch>=token.start && pos.ch<=token.end){
                                    tryToAdjustHint(token.string, cm.doc.indexFromPos(pos), {start:token.start, end:token.end, line:pos.line});
                                    handled=true;
                                }
                            }
                        }else if($suggestionShowed===1){
                             if(type=="property"){
                                var pos=cm.getCursor();
                                tryToAdjustHint(token.string, cm.doc.indexFromPos(pos), {start:token.start, end:token.end, line:pos.line});
                                handled=true;
                             }else if(type===null && str=="."){
                                var pos=cm.getCursor();
                                showDotHint(cm.doc.indexFromPos(pos), {start:++token.start, end:++token.end, line:pos.line});
                                handled=true;
                             }
                        }
                    }
                }
                if(!handled){
                    //if hint/hint2 showed, hide it
                    hideSuggestion();
                }
            });
            cm.on("blur",function(){
                //hideSuggestion();
            });
            cm.on("keypress",function(cm,key){
                if(codeType!='js'&&codeType!='html') return;
                if($willshowDotHint && ood.Event.getKey(key)[0]!="."){
                    hideSuggestion();
                }
            });
            cm.on("keydown",function(cm,key){
                if(codeType!='js'&&codeType!='html') return;
                if($willshowDotHint && ood.Event.getKey(key)[0]!="."){
                    hideSuggestion();
                }
            });
            cm.on("cursorActivity",function(cm){
                if(!cm.hasFocus())return;
                
                if(codeType!='js'&&codeType!='html') return;
                if($suggestionBaseScope){
                    var pos=cm.getCursor(),
                        scope=$suggestionBaseScope;
                    // not in region
                    if(pos.line!==scope.line || pos.ch<scope.start || pos.ch>scope.end){
                        hideSuggestion();
                    }
                }
                ood.resetRun("cm_showtips",function(){
                    var state = cm.state.matchHighlighter;
                        if(state){
                        //only for selection, and matchhighlight
                        if(state.overlay && state.style=="matchhighlight" && cm.doc.somethingSelected()) {
                            var cur = cm.getCursor(),
                                sel = cm.doc.getSelection(),
                                token = cm.getTokenAt(cur);
                            // only for whole word
                            if(sel===token.string){
                                showDotHint(cm.doc.indexFromPos(cur), {start:token.start, end:token.end, line:cur.line}, token.string);                        }
                        }
                    }
                },200);
            });
        },
        _onresize:function(profile,width,height){
            var size = arguments.callee.upper.apply(this,arguments);
            profile.getSubNode('BOX').cssSize(size);
            // have to use px here
            if(profile.$cm)profile.$cm.setSize(profile.$px(size.width),profile.$px(size.height));
        }
    }
});
