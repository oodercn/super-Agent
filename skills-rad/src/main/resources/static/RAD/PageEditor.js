ood.Class('RAD.PageEditor', 'ood.Module',{
    Instance:{
        Initialize:function(){
            var ns=this;
            ns._ovalue="";
            ns._dirty=false;
        },
        resetEditorStyle:function(theme, fs, wrap){
            this.codeeditor.resetEditorStyle(theme,fs, wrap);
        },
        // notice: use it before rendered
        setType:function(type){
            if(this.codeeditor)this.codeeditor.setCodeType(type, true);
            this._codetype = type && type.split(".")[0];
            if(this.toolbar4){
                this.toolbar4.updateItem('jsoneditor',{
                    hidden: type!='json'
                });
            }
        },
        activate:function(){
            this.codeeditor.activate();
        },
        getValue:function(){
            var ns=this;
            return ns._dirty ? ns.codeeditor.getUIValue(): ns._ovalue;
        },
        _refreshCM:function(){
            if(this.codeeditor){
                this.codeeditor._refreshCM();
            }
        },
        setValue:function(value){
            // adjust
            value=(value||'').replace(/\t/g, '    ').replace(/\u00A0/g, " ").replace(/\r\n?/g, "\n");
            
            var ns=this;
            if(ns._ovalue!=value){
                ns._ovalue=value;
                ns._dirty=false;

                if(ns.codeeditor){
                    ns.codeeditor.setValue(ns._ovalue);
                }
            }
            return ns;
        },
        reindent:function(){
            this.codeeditor.reindent();
        },
        setReadonlyLines:function(head, tail){
            this.codeeditor.setReadonlyLines(head, tail);
        },
        iniComponents:function(com, threadid){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0))};
            
            append((new ood.UI.ToolBar)
                .setHost(host,"toolbar4")
                .setItems([
                    {"id":"fun","sub":[
                            // 在toolbar4的items中找到以下图标并替换
                            {
                              "id":"searchreplace",
                              "caption":"$RAD.pageEditor.searchreplace",
                              "imageClass":"ri-search-line", // 替换 'spafont spa-icon-searchreplace'
                              "type":"button",
                              "tips":"$RAD.pageEditor.replacetips"
                            },
                            {
                              "id":"jumpto",
                              "caption":"$RAD.pageEditor.jumpto",
                              "imageClass":"ri-arrow-right-up-line", // 替换 'spafont spa-icon-jumpto'
                              "type":"button",
                              "tips":"$RAD.pageEditor.jumptotips"
                            },
                            {
                              "id":"indentall",
                              "caption":"$RAD.pageEditor.indentall",
                              "imageClass":"ri-indent-increase",
                              "type":"button",
                              "tips":"$RAD.pageEditor.indentalltips"
                            },
                            {
                              "id":"rendermode", 
                              "caption":"mode", 
                              "imageClass":"ri-code-line"
                            },
                            {
                              "id":"jsoneditor", 
                              "hidden":false, 
                              "caption":"$RAD.spabuilder.menubar.jsoneditor", 
                              "imageClass":"ri-braces-line", // 替换 'spafont spa-icon-hash'
                              "type":"button", 
                              "tips":"$(RAD.pageEditor.Edit in JSON Editor)"
                            },
                            
                            // 在177行左右找到并替换
                           // imageClass: 'ri-braces-line' // 替换 'spafont spa-icon-hash'
                            
                            {"id":"progress", "object":(new ood.UI.Image({src:CONF.img_progress})).setHost(host,"imgProgress")}
                        ]
                    }
                ])
                .onClick("_toolbar_onclick")
            );

            append((new RAD.CodeEditor)
                .setHost(host,"codeeditor")
                .setDock("fill")
                .setValue(host._ovalue||"")
                .onValueChanged("_codeeditor_onChange")
                .onSaveCommand("_codeeditor_onsave")
                .onGetHelpInfo("_codeeditor_ongett")
                .onRendered("_codeeditor_onrender")
                .onCodeModeSet("_codeeditor_oncodemodeset")
            );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events:{onReady:'_onready'},
        _onready:function(com){
            if(com._codetype)
                com.codeeditor.setCodeType(com._codetype,true);
        },
        _codeeditor_onrender:function(profile, finished){
            var ns=this;
            if(!ns.codeeditor)return;
            
            var id=ns.KEY+":"+ns.$xid+":progress";
            if(finished){
                ood.resetRun(id);

                ns.toolbar4.setDisabled(false);
                ns.codeeditor.setReadonly(false);

                ns.imgProgress.setDisplay('none');
                
                if(!ns.$initializd){
                    ns.$initializd=true;
                    ns.fireEvent('afterRendered');
                }
            }else{
                ood.resetRun(id, function(){
                    if(!ns.destroyed){
                        ns.toolbar4.setDisabled(true);
                        ns.codeeditor.setReadonly(true);
                        ns.imgProgress.setDisplay('');
                    }
                });
            }
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
        _codeeditor_onChange:function(profile, flag){
           this._dirty=true;
           this.fireEvent("onValueChanged",[this, flag]);
        },
        _codeeditor_onsave:function(profile){       
            var ns=this;
            ns.fireEvent('onCommandSave', [ns]);
        },
        _toolbar_onclick: function(profile, item, grp, e, src){
            var ns=this,
                editor=this.codeeditor,
                pos=ood.use(src).offset();
            switch(item.id){
                case 'indentall':
                    editor.reindent();
                break;
                case 'searchreplace':
                    RAD.EditorTool.showFindWnd(null,editor, pos,editor.getCM().getSelection());
                break;
                case 'jumpto':
                    RAD.EditorTool.showJumpToWnd(null,editor, pos);
                break;
                case 'rendermode':
                    editor.showModeSelDlg(item);
                break;
                case 'jsoneditor':
                    var text = ns.getValue();
                    try{
                        ood.unserialize(text);
                    }catch(e){
                        ood.alert(e);
                        return;
                    }
                    if(window.SPA && false===SPA.fe("beforeObjectEditorPop", [
                            "jsonEditor", null, text, function(txt){
                                ns.setValue(typeof(txt)=='string'?txt:ood.stringify(txt));
                            },
                        "RAD.PageEditor", ns,  ood(src).get(0), null, editor] )) return;
                    ood.ModuleFactory.getCom('jsonEditor',function(){
                        this.setProperties({
                                caption:"$RAD.spabuilder.menubar.jsoneditor",
                                imageClass: 'ri-braces-line',
                                text:text,
                                onOK:function(obj, txt){
                                    ns.setValue(txt);
                               }
                        });
                        this.show();
                    });
                break;
            }
        }
    }
});
