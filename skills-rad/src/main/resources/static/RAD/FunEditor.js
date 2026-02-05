ood.Class('RAD.FunEditor', 'ood.Module',{
    Instance:{
        $PageEditor:null,
        activate:function(){
            this.$PageEditor.activate();
        },
        setValue:function(text){
            this.$PageEditor.setValue(text||"");
        },
        setReadonlyLines:function(head, tail){
            this.$PageEditor.setReadonlyLines(head, tail);
        },
        _dlg_onshow:function(){
            this.$PageEditor.activate();
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        _btncancel_onclick:function(){
            this.setValue("");
            this.dialog.close();
        },
        _btnok_onclick:function(){
            var self=this,
                prop=self.properties,
                txt = self.$PageEditor.getValue();
            if(txt===false)return false;
            //check dirty
 //           if((prop.text||"") != (txt||"")){
                //check first
                var result = RAD.CodeEditor.evalInSandbox("("+txt+")", false,null,null,true);
                if(result.ko){
                    ood.message(ood.getRes('RAD.JSEditor.codeerr'));
                    return false;
                }
                //parse comments and code, check code in the process
                prop.result = RAD.ClassTool.parseSingleBlock(txt);

                if(false === prop.result){
                    ood.message(ood.getRes('RAD.classtool.err1'));
                    return false;
                }

                prop.object = ood.unserialize(prop.result.code) || null;

                ood.tryF(prop.onOK,[self],self.host);
   //         }
            self.setValue(prop.text = "");
            self.dialog.close();
        },
        customAppend:function(parent){
            var page=this,
                prop = page.properties,
                dlg=page.dialog;
            dlg.setCaption(prop.caption).setImage(prop.image).setImagePos(prop.imagePos);

            if(prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);

            if(!page.$PageEditor){
                var pe = new RAD.PageEditor;
                pe.setHost(page, "$PageEditor");
                page.panelMain.append(pe);
            }
            page.$PageEditor.setType("js");

            page.setValue(prop.text||"");
            
            this.$PageEditor.reindent( );

            if(prop.head_readonly||prop.tail_readonly)page.setReadonlyLines(prop.head_readonly, prop.tail_readonly);

            dlg.showModal(parent);

        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("4.375em")
                .setTop("2.5em")
                .setWidth("41.416666666666664em")
                .setHeight("24.916666666666668em")
                .setCaption("dialog")
                .setMinBtn(false)
                .setMaxBtn(false)
                .beforeClose("_dialog_beforeclose")
                .onShow("_dlg_onshow")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelB")
                .setDock("bottom")
                .setHeight("3.3333333333333335em")
                );
            
            host.panelB.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelR")
                .setDock("right")
                .setWidth("24.25em")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setLeft("-5.25em")
                .setTop("0.5em")
                .setWidth("7.75em")
                .setImageClass("spafont spa-icon-cancel")
                .setCaption("$inline.cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setLeft("4.75em")
                .setTop("0.5em")
                .setWidth("7.75em")
                .setImageClass("spafont spa-icon-ok")
                .setCaption("$inline.ok")
                .onClick("_btnok_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"panelMain")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    }
});
