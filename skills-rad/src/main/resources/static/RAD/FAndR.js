ood.Class('RAD.FAndR', 'ood.Module',{
    Instance:{
        _editor:null,
        initialize : function(){
            this.autoDestroy = true;
            this.properties = {};
        },
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ctl_dialog")
                .setShowEffects("Slide In TB")
                .setHideEffects("Slide In TB")
                .setLeft("0em")
                .setTop("0em")
                .setHeight("18em")
                .setSelectable(false)
                .setResizer(false)
                .setInitPos("auto")
                .setCaption("$RAD.pageEditor.searchreplace")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
                .onHotKeydown("_ctl_dialog_onhotkeydown")
                .beforeClose("_ctl_dialog_beforeclose")
            );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel1")
                .setLeft("1.25em")
                .setTop("0.875em")
                .setWidth("5.25em")
                .setCaption("$RAD.pageEditor.search")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel2")
                .setLeft("1.25em")
                .setTop("3.5em")
                .setWidth("5.25em")
                .setCaption("$RAD.pageEditor.replacewith")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Input")
                .setHost(host,"ctl_i1")
                .setDirtyMark(false)
                .setLeft("6.875em")
                .setTop("0.625em")
                .setWidth("14.666666666666666em")
                .onHotKeydown("_ctl_i1_onhotkeydown")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Input")
                .setHost(host,"ctl_i2")
                .setDirtyMark(false)
                .setLeft("6.875em")
                .setTop("3.25em")
                .setWidth("14.666666666666666em")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbtnSearch")
                .setDefaultFocus(true)
                .setLeft("2.4166666666666665em")
                .setTop("6.666666666666667em")
                .setWidth("9.166666666666666em")
                .setCaption("$RAD.pageEditor.search")
                .onClick("_ctl_sbtnsearch_onclick")
                );


            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbtnReplace")
                .setLeft("2.4166666666666665em")
                .setTop("9.166666666666666em")
                .setWidth("9.166666666666666em")
                .setCaption("$RAD.pageEditor.replace")
                .onClick("_ctl_sbtnreplace_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbtnSR")
                .setLeft("12.75em")
                .setTop("6.666666666666667em")
                .setWidth("9.166666666666666em")
                .setCaption("$RAD.pageEditor.replacesearch")
                .onClick("_ctl_sbtnsr_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbtnRA")
                .setLeft("12.75em")
                .setTop("9.166666666666666em")
                .setWidth("9.166666666666666em")
                .setCaption("$RAD.pageEditor.replaceall")
                .onClick("_ctl_sbtnra_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block91")
                .setLeft("1.6666666666666667em")
                .setTop("5.833333333333333em")
                .setWidth("20.75em")
                .setHeight("0em")
                .setBorderType("inset")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            var ns=this;
            if(ns._init){
                ns.ctl_i1.setUIValue(ns._init);
            }
            ns.ctl_i2.setUIValue("");
            ns.ctl_dialog.show(parent, false, left, top,function(){
                ns.ctl_i1.activate();
            });
        },
        _ctl_dialog_beforeclose : function (profile) {
            this.ctl_dialog.hide();
            this._SearchCursor=null;
            return false;
        },
        getCM:function(){
            var ns=this;
            if(ns._editor){
                return ns._editor.getCM();
            }
        },
        _ctl_sbtnsearch_onclick : function (profile, e, src, value) {
            var ns=this, query=ns.ctl_i1.getUIValue(),cm;
            if(query &&(cm=ns.getCM())){
                var cs = ns._SearchCursor=cm.getSearchCursor(query, cm.doc.getCursor(), true);
                if(cs.findNext()){
                    cm.doc.setSelection(cs.from(),cs.to());
                }else{
                    cs = ns._SearchCursor=cm.getSearchCursor(query, 0, true);
                    if(cs.findNext()){
                        cm.doc.setSelection(cs.from(),cs.to());
                    }else
                        ood.message(ood.getRes('RAD.pageEditor.findnone'));
                }
            }
        },
        _ctl_sbtnreplace_onclick : function (profile, e, src, value) {
            var ns=this, query=ns.ctl_i1.getUIValue(),rep=ns.ctl_i2.getUIValue(),cm;
            if(query &&(cm=ns.getCM())){
                if(ns._SearchCursor && cm.getSelection()==query){
                    ns._SearchCursor.replace(rep);
                }
            }
        },
        _ctl_sbtnsr_onclick : function (profile, e, src, value) {
            var ns=this, query=ns.ctl_i1.getUIValue(),rep=ns.ctl_i2.getUIValue(),cm;
            if(query &&(cm=ns.getCM())){
                if(ns._SearchCursor && cm.getSelection()==query){
                    ns._SearchCursor.replace(rep);
                }
                ns._ctl_sbtnsearch_onclick();
            }
        },
        _ctl_sbtnra_onclick : function (profile, e, src, value) {
            var ns=this, query=ns.ctl_i1.getUIValue(),rep=ns.ctl_i2.getUIValue(),cm;
            if(query &&(cm=ns.getCM())){
                var count=0;
                var cs = ns._SearchCursor=cm.getSearchCursor(query, 0, true);
                while(cs.findNext()){
                    cs.replace(rep);
                    count++;
                }
                ood.message(ood.getRes('RAD.pageEditor.replaceCount', count));
            }
        },
        _ctl_dialog_onhotkeydown : function (profile, keyboard){
            if(keyboard.key=='esc'){
                this.ctl_dialog.close();
            }
        },
        _ctl_i1_onhotkeydown:function (profile, keyboard, e, src){
            if(keyboard.key=='enter'){
                this.ctl_sbtnSearch.onClick();
            }
        }
    }
});
