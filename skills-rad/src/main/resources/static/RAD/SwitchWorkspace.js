ood.Class('RAD.SwitchWorkspace', 'ood.Module',{
    Instance:{
        customAppend:function(){
            var self=this,
                dlg=self.dialog;
            dlg.show(self.parent, true);
        },
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("3.125em")
                .setTop("1.875em")
                .setWidth("32.666666666666664em")
                .setHeight("18.833333333333332em")
                .setCaption("$(RAD.ws.Move/Switch Workspace)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("visible")
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_pane68")
                .setDock("bottom")
                .setHeight("3.5em")
                .setOverflow("visible")
                );
            
            host.ctl_pane68.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setTop("0.75em")
                .setWidth("7em")
                .setRight("10em")
                .setZIndex("1")
                .setImageClass("ri-close-line") // 替换 "spafont spa-icon-cancel"
                .setCaption("$RAD.cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.ctl_pane68.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setTop("0.75em")
                .setWidth("8em")
                .setRight("1em")
                .setZIndex("1")
                .setImageClass("ri-check-line") // 替换 "spafont spa-icon-ok"
                .setCaption("$RAD.ok")
                .onClick("_btnok_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group1")
                .setLeft("0.625em")
                .setTop("3.3333333333333335em")
                .setWidth("31em")
                .setHeight("8.333333333333334em")
                .setOverflow("hidden")
                .setCaption("$(RAD.ws.Select target folder)")
                .setToggleBtn(false)
                );
            
            host.ctl_group1.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"ctl_comboinput14")
                .setDirtyMark(false)
                .setLeft("9.083333333333334em")
                .setTop("4.75em")
                .setWidth("20.833333333333332em")
                .setType("popbox")
                .setCachePopWnd(false)
                .setInputReadonly(true)
                .beforeComboPop("_beforepopshow")
                );
            
            host.ctl_group1.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_radiobox4")
                .setDirtyMark(false)
                .setItems([{
                    "id":"move",
                    "caption":"$RAD.ws.Move"
                },
                {
                    "id":"switch",
                    "caption":"$RAD.ws.Switch"
                }])
                .setLeft("0.375em")
                .setTop("0.0625em")
                .setWidth("28.666666666666668em")
                .setHeight("2.5em")
                .afterUIValueSet("_ctl_radiobox4_afteruivalueset")
                );
            
            host.ctl_group1.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_tips")
                .setLeft("1.25em")
                .setTop("2.5em")
                .setWidth("28.666666666666668em")
                .setHeight("2.1875em")
                .setCaption("")
                .setHAlign("left")
                .setCustomStyle({
                    "KEY":{
                        "color":"#544444",
                        "text-decoration":"underline"
                    }
                }
                )
                );
            
            host.ctl_group1.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel8")
                .setLeft("0.1875em")
                .setTop("5.083333333333333em")
                .setWidth("8.083333333333334em")
                .setHeight("1em")
                .setCaption("$(RAD.ws.Target Folder)")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel1")
                .setLeft("1.6666666666666667em")
                .setTop("0.5em")
                .setWidth("28.166666666666668em")
                .setHeight("2.8333333333333335em")
                .setCaption("$(RAD.ws.ESDUI stores your projects in a  folder, called OODWorkspace$. Choose a folder to move/switch your OODWorkspace to).")
                .setHAlign("left")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        init:function(){
            this.ctl_radiobox4.setUIValue("switch",true);
            this.ctl_comboinput14.setUIValue(CONF.wsPath,true);
        },
        _beforepopshow : function (profile, popCtl){
            var ctrl=profile.boxing();
            NWUtil.popSaveAsDlg("", "", function(v){
                v=v.replace(/\\/g,"/");
                ctrl.setValue(v,true);
            }, true);
            return false;
        },
        _btncancel_onclick:function(){
            this.dialog.close();
        },
        _btnok_onclick:function(){
             var ns=this,
                 path = ns.ctl_comboinput14.getUIValue(),
                 b=ns.ctl_radiobox4.getUIValue();
            if(path==CONF.wsPath)
                return;

            NWUtil.moveWorksapceTo(path, b, function(){
                ns.dialog.close();
                ood.alert(ood.adjustRes("$(RAD.ws.Your OODWorkSpace has been $0 to '$1' successfully-"+(b?"moved":"switched")+"-"+path+")!"));
            },function(error){
                ood.alert(ood.get(error,['error','errdes'])||error||'no response!');
            });
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        _dialog_onhotkey:function(profile, key){
            if(key.key=='esc')
                profile.boxing().close();
        },
        _ctl_radiobox4_afteruivalueset:function (profile, oldValue, newValue){
            if(newValue=="move"){
                this.ctl_tips.setCaption("$(RAD.ws.Switch OOD Workspace to following target folder, move all project files into it, and remove the old OOD Workspace folder).");
            }else{
                this.ctl_tips.setCaption("$(RAD.ws.Switch OOD Workspace to following target folder only, don't move any project files, don't remove the old OOD Workspace)");
            }
            
        }
    }
});
