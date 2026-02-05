ood.Class('RAD.ObjectEditor2', 'ood.Module',{
    Instance:{
        $PageEditor:null,
        activate:function(){
            this.$PageEditor.activate();
        },
        setValue:function(text){
            this.$PageEditor.setValue(text||"");
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        _btncancel_onclick:function(){
            this.setValue("");
            this.dialog.close();
        },
        _btnSave_onclick:function(){
            var self=this,
                prop=self.properties,
                txt = self.$PageEditor.getValue();
            if(txt===false)return false;
            //check dirty
            if(txt){
                var arr=SPA.currentPage.split("/"),
                    name=arr.pop().replace(/\.js/i,"");
                ood.showCom("RAD.AddFile",function(){
                    this.setProperties({
                        cls:'save',
                        fileName:name,
                        parentPath:SPA.curProjectPath+"/data",
                        typeCaption:ood.adjustRes("$RAD.pm.normaljs"),
                        type:".json",
                        tailTag:".json",
                        forcedir:true,
                        content:txt
                    });
                    this.setEvents({onOK:function(){
                        self.setValue(prop.text = "");
                        self.dialog.close();
                    }});
                },null,null,true);
            }
        },
        customAppend:function(parent){
            var page=this,
                prop = page.properties,
                dlg=page.dialog;

            if(prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);

            if(!page.$PageEditor){
                var pe = new RAD.PageEditor;
                pe.setHost(page, "$PageEditor");
                pe.setType(page._codetype);
                page.panelMain.append(pe);
            }

            page.setValue(prop.text||"");

            if(!SPA.currentPage){
                page.btnSave.setDisplay('none');
            }

            dlg.showModal(parent,null,null,function(){
                if(page.$PageEditor)page.$PageEditor._refreshCM();
            });
        },
        iniComponents:function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("4.375em")
                .setTop("-0.125em")
                .setWidth("41.416666666666664em")
                .setHeight("22.5625em")
                .setCaption("$(RAD.pageEditor.Page Data)")
                .setMinBtn(false)
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelB")
                .setDock("bottom")
                .setHeight("2.6666666666666665em")
                );
            
            host.panelB.append(
                ood.create("ood.UI.Div")
                .setHost(host,"panelR")
                .setDock("right")
                .setWidth("30.916666666666668em")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setLeft("4.083333333333333em")
                .setTop("0.25em")
                .setWidth("7.166666666666667em")
                .setImageClass("ood-icon-back")
                .setCaption("$RAD.close")
                .onClick("_btncancel_onclick")
                );
            
            host.panelR.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnSave")
                .setLeft("14.083333333333334em")
                .setTop("0.25em")
                .setWidth("7.166666666666667em")
                .setImageClass("ood-uicmd-save")
                .setCaption("$RAD.save")
                .onClick("_btnSave_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"panelMain")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        }
    }
});
