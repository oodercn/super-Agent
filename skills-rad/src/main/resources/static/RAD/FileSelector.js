ood.Class('RAD.FileSelector', 'ood.Module',{
    Instance:{
        customAppend:function(parent){
            var self=this,
                prop = self.properties,
                pp = prop.parent,
                dlg=self.dialog;
            if(prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);

            if(!dlg.get(0).renderId)
                dlg.render();

            self.treebar.resetValue();

            //asy
            dlg.show(parent, true);

            CONF.callService({
                key:CONF.requestKey,
                curProjectPath:SPA.curProjectPath,
                paras:{
                    action:'open',
                    hashCode:ood.id(),
                    path:SPA.curProjectPath
                }
            } ,function(txt){
                var obj = txt;
                if(!obj || obj.error)
                    ood.message(ood.get(obj,['error','errdes'])||'no response!');
                else{
                    var root=SPA.buildFileItems(SPA.curProjectPath, obj.data.files||obj.data);
                    self.treebar.setItems([root]).toggleNode(root.id, true);
                }
            });
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("15.4375em")
                .setTop("7.5em")
                .setWidth("27.0625em")
                .setHeight("25.833333333333332em")
                .setResizer(false)
                .setCaption("$RAD.selFile")
                .setImageClass("ri-folder-open-line") // 原"spafont spa-icon-select"
                .setMinBtn(false)
                .setMaxBtn(false)
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"panelbar2")
                .setDock("top")
                .setHeight("19.166666666666668em")
                .setCaption("$RAD.selFilePath")
                .setToggleBtn(false)
                .setCustomStyle({
                    "PANEL":{
                        "overflow":"auto"
                    }
                })
                .setCustomClass({
                    "PANEL":"ood-uibase"
                })
                );
            
            host.panelbar2.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"treebar")
                .setDirtyMark(false)
                .setDock("none")
                .setWidth("auto")
                .setHeight("auto")
                .setPosition("relative")
                .beforeUIValueSet("_treebar_beforevalueupdated")
                .onClick("_treebar_onclick")
                .onGetContent("_treebar_ongetcontent")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setDirtyMark(false)
                .setLeft("5em")
                .setTop("20em")
                .setWidth("6.916666666666667em")
                .setImageClass("ri-close-line") // 原"spafont spa-icon-cancel"
                .setCaption("$RAD.cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setDirtyMark(false)
                .setLeft("15.625em")
                .setTop("20em")
                .setWidth("6.916666666666667em")
                .setImageClass("ri-check-line") // 原"spafont spa-icon-ok"
                .setCaption("$RAD.ok")
                .onClick("_btnok_onclick")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _btncancel_onclick:function (profile, e, value) {
            this.dialog.close();
        },
        _btnok_onclick:function (profile, e, value) {
            var s = this.treebar.getUIValue(), self=this;;
            if(s){
                ood.tryF(self.properties.onOK, [this, s], self.host);
                self.dialog.close();
            }
        },
        _treebar_beforevalueupdated:function (profile, oldValue, newValue) {
            if(newValue){
                var file=newValue.split("/").pop();
                if(file.indexOf(".")==-1) {
                    return false;
                }
            }else{
                return false;
            }
        },
        _dialog_onhotkey:function(profile, key){
            if(key.key=='esc')
                profile.boxing().close();
        },
        _treebar_ongetcontent : function (profile, item, callback) {
            var ns=this;
            CONF.callService({
                key:CONF.requestKey,
                curProjectPath:SPA.curProjectPath,
                paras:{
                    action:'open',
                    hashCode:ood.id(),
                    path:item.id
                }
            },function(txt){
                var obj = txt;
                if(obj && !obj.error){
                    var root = SPA.buildFileItems(item.id, obj.data.files||obj.data);
                    callback(root.sub);
                }else ood.message(obj.error.message);
            });
        },
        _treebar_onclick:function(profile, item, e, src){
            if(item.sub){
                profile.boxing().toggleNode(item.id);
            }
        }
    }
});
