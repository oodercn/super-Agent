ood.Class('RAD.HTMLEditor', 'ood.Module',{
    Instance:{
        activate:function(){
            this.rInput.activate();
        },
        setValue:function(text){
            this.rInput.setValue(text,true);
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        _btncancel_onclick:function(){
            this.setValue("",true);
            this.dialog.close();
        },
        _btnok_onclick:function(){
            var self=this,
                prop=self.properties,
                txt = self.rInput.getUIValue();
            if(txt===false)return false;
            //check dirty
            if(prop.text != txt){                    
                ood.tryF(prop.onOK,[self, txt],self.host);
            }
            self.setValue(prop.text = "", true);
            self.dialog.close();
        },
        customAppend:function(parent){
            var page=this,
                prop = page.properties,
                dlg=page.dialog;
            dlg.setCaption(prop.caption).setImage(prop.image).setImagePos(prop.imagePos);

            if(prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);

           
            page.setValue(prop.text||"",true);

            dlg.showModal(parent);
        },
        events:{
            onReady:function(page){
                page.rInput.get(0).$useOriginalText=1;
            }
        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("4.375em")
                .setTop("2.5em")
                .setWidth("45.583333333333336em")
                .setCaption("dialog")
                .setMinBtn(false)
                .setMaxBtn(false)
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelB")
                .setDock("bottom")
                .setHeight("3.0833333333333335em")
                );
            
            host.panelB.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelR")
                .setDock("right")
                .setWidth("17.75em")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setLeft("0.8333333333333334em")
                .setTop("0.5em")
                .setWidth("7.333333333333333em")
                .setImageClass("ri-close-line") // 替换 "spafont spa-icon-cancel"
                .setCaption("Cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setLeft("9.5em")
                .setTop("0.5em")
                .setWidth("7.333333333333333em")
                .setImageClass("ri-check-line") // 替换 "spafont spa-icon-ok"
                .setCaption("OK")
                .onClick("_btnok_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"panelMain")
                .setDock("fill")
                .setBorderType("inset")
                .setBackground("#fff")
                );
            
            host.panelMain.append(
                ood.create("ood.UI.RichEditor")
                .setHost(host,"rInput")
                .setDock("fill")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    }
});
