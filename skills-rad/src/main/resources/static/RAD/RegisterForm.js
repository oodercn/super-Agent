ood.Class('RAD.RegisterForm', 'ood.Module',{
    Instance:{
        customAppend:function(){
            var ns=this;
            if(ns.properties.registered){
                ns.ctl_sbutton9.setCaption("$RAD.Close");
                ns.ctl_input2.setDisabled(true);
                ns.ctl_sbutton5.setDisabled(true);
                ood.alert(ood.adjustRes("$(RAD.designer.license.You'v registered already)!"));
            }
            if(!ns.properties.autoClose){
                ns.dlg.showModal();
//                ns.msgLabel.setDisplay("none");
            }else{
                ns.dlg.show();
                ns.dlg.getRoot().setBlurTrigger("$registerWnd",function(){
                    if(!ns.isDestroyed()){
                        ns.dlg.close();
                    }
                });
/*                ns.msgLabel.setDisplay("");
                // auto destroy
                ood.asyRun(function(){
                    if(!ns.isDestroyed()){
                        ns.dlg.close();
                    }
                },5000);
*/
            }
            return true;
        },
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dlg")
                .setLeft("11.75em")
                .setTop("1.75em")
                .setWidth("31.5em")
                .setHeight("15.25em")
                .setResizer(false)
                .setCaption("$(RAD.designer.license.EUSUI License Statements)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setCloseBtn(false)
                .setOverflow("hidden")
                .onHotKeydown("_dlg_onhotkeydown")
            );
            
            host.dlg.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbutton9")
                .setDefaultFocus(true)
                .setLeft("1.25em")
                .setTop("9.083333333333334em")
                .setWidth("27.916666666666668em")
                .setHeight("2.25em")
                .setCaption("$(RAD.designer.license.Continue Free Trial)")
                .onClick("_ctl_sbutton9_onclick")
                .setCustomStyle({
                    "CAPTION":{
                        "font-size":"14pt",
                        "font-weight":"bold"
                    }
                }
                )
                );
            
            host.dlg.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block171")
                .setLeft("0.5625em")
                .setTop("0.1875em")
                .setWidth("30em")
                .setHeight("8.083333333333334em")
                .setBorderType("groove")
                .setOverflow("hidden")
                );
            
            host.ctl_block171.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel1")
                .setLeft("0.75em")
                .setTop("0.25em")
                .setWidth("28.333333333333332em")
                .setHeight("1.25em")
                .setBottom("0em")
                .setCaption("$RAD.designer.msg.annoncement1")
                .setHAlign("left")
                );
            
            host.ctl_block171.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel37")
                .setLeft("0.75em")
                .setTop("2.0833333333333335em")
                .setWidth("28.333333333333332em")
                .setHeight("3em")
                .setBottom("0em")
                .setCaption("$RAD.designer.msg.annoncement2")
                .setHAlign("left")
                );
            
            host.ctl_block171.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbutton5")
                .setLeft("19.1875em")
                .setTop("5.333333333333333em")
                .setWidth("9.916666666666666em")
                .setCaption("$(RAD.designer.license.Register the license)")
                .onClick("_ctl_sbutton5_onclick")
                );
            
            host.ctl_block171.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"ctl_input2")
                .setDirtyMark(false)
                .setLeft("0.3125em")
                .setTop("5.333333333333333em")
                .setWidth("18.5em")
                .setLabelSize(130)
                .setLabelCaption("$(RAD.designer.license.Enter License Code)")
                .setType("none")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _ctl_sbutton5_onclick : function (profile, e, src, value){
            var ns=this,
                code=ns.ctl_input2.getUIValue();
            code=ood.str.trim(code);
            if(!code)return ;
            
            ns.dlg.busy(ood.adjustRes("$(RAD.designer.license.Try to register) ..."));
            this.fireEvent("onOK", [code,function(text){
                SPA.statusBar.setHtml(text);
                ns.dlg.free();
                ood.alert(ood.adjustRes("$(RAD.designer.license.Congratulations, you have successfully registered)!"));
                if(!ns.isDestroyed()){
                    ns.dlg.close();
                }
            },function(err){
                ns.dlg.free();
                ood.alert(err);
            }]);
        },
        _ctl_sbutton9_onclick : function (profile,e,src,value){
            if(!this.isDestroyed()){
                this.dlg.close();
            }
        },
        _dlg_onhotkeydown:function (profile, keyboard, e, src){
            if(keyboard.key=='esc'){
                if(!this.isDestroyed()){
                    this.dlg.close();
                }
            }
        }
    }
});
