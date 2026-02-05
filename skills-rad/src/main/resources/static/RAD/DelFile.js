ood.Class('RAD.DelFile', 'ood.Module',{
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
                    path:pp.curProjectPath
                }
            } ,function(txt){
                var obj = txt;
                if(!obj || obj.error)
                    ood.message(ood.get(obj,['error','errdes'])||'no response!');
                else{
                    var root=pp.buildFileItems(pp.curProjectPath, obj.data.files||obj.data);
                    self.treebar.setItems([root]).toggleNode(root.id, true);
                }
            });
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        iniComponents:function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("15.4375em")
                .setTop("7.5em")
                .setWidth("27.0625em")
                .setHeight("15.083333333333334em")
                .setResizer(false)
                .setCaption("$RAD.tool2.del")
                .setImageClass("spafont spa-icon-deletefile")
                .setMinBtn(false)
                .setMaxBtn(false)
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"panelbar2")
                .setDock("top")
                .setDockMargin({
                    "left":6,
                    "top":0,
                    "right":6,
                    "bottom":0
                }
                )
                .setHeight("8.75em")
                .setCaption("$RAD.delfile.sel")
                .setToggleBtn(false)
                .setCustomStyle({
                    "PANEL":"overflow:auto"
                }
                )
                );
            
            host.panelbar2.append(
                ood.create("ood.UI.TreeBar")
                .setHost(host,"treebar")
                .setDock("none")
                .setWidth("auto")
                .setHeight("auto")
                .setPosition("relative")
                .setSelMode("multi")
                .setValue("")
                .beforeUIValueSet("_treebar_beforevalueupdated")
                .onGetContent("_treebar_ongetcontent")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setLeft("5em")
                .setTop("9.375em")
                .setWidth("6.916666666666667em")
                .setImageClass("spafont spa-icon-cancel")
                .setCaption("$RAD.cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setLeft("15.625em")
                .setTop("9.375em")
                .setWidth("6.916666666666667em")
                .setImageClass("spafont spa-icon-ok")
                .setCaption("$RAD.ok")
                .onClick("_btnok_onclick")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _btncancel_onclick:function (profile, e, value) {
            this.dialog.close();
        },
        _btnok_onclick:function (profile, e, value) {
            var s = this.treebar.getUIValue(), self=this;;
            if(!s){
                ood.message(ood.getRes('RAD.delfile.notarget'));
            }else{
                ood.UI.Dialog.confirm(ood.getRes('RAD.delfile.confirmdel'), ood.getRes('RAD.delfile.confirmdel2', s.split(';').length), function(){
                    ood.tryF(self.properties.onOK, [s], self.host);
                    self.dialog.close();
                });
            }
        },
        _treebar_beforevalueupdated:function (profile, oldValue, newValue) {
            if(!newValue)return;
            var arr = newValue.split(';');
            arr.sort();
            ood.filter(arr,function(o,j){
                for(var i=0,l=this.length;i<l;i++){
                    if(i==j)break;
                    var s=this[j].replace(this[i],'');
                    if(s!=this[j] && (s.indexOf("/")!=-1 || s.indexOf("\\")!=-1) ){
                        return false;
                    }
                }
            });
            return arr.join(';');
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
                    var root = ns.properties.parent.buildFileItems(item.id, obj.data.files||obj.data);
                    callback(root.sub);
                }else ood.message(obj.error.message);
            });
        }
    }
});
