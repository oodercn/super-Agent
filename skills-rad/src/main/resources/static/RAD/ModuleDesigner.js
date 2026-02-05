ood.Class('RAD.ModuleDesigner', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setDockMargin({
                    "left":20,
                    "top":20,
                    "right":20,
                    "bottom":20
                })
                .setLeft("0em")
                .setTop("0em")
                .setWidth("50em")
                .setHeight("30em")
                .setCaption("$(RAD.custom_dlg.Module Designer)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setMovable(false)
                .setStatus("max")
                .beforeClose("_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"paneMain")
                .setDock("fill")
                .setLeft("13.333333333333334em")
                .setTop("3.3333333333333335em")
                .setBorderType("inset")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block3")
                .setDock("bottom")
                .setLeft("14.166666666666666em")
                .setTop("4.166666666666667em")
                .setHeight("3.3333333333333335em")
                .setBorderType("none")
                );
            
            host.ood_ui_block3.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ood_ui_div15")
                .setTop("0.25em")
                .setWidth("22.916666666666668em")
                .setHeight("3em")
                .setRight("1em")
                );
            
            host.ood_ui_div15.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnCancel")
                .setLeft("4.083333333333333em")
                .setTop("0.5em")
                .setWidth("7.333333333333333em")
                .setImageClass("ri ri-close-line") // 替换 spafont spa-icon-cancel
                .setCaption("$RAD.Cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.ood_ui_div15.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnOK")
                .setLeft("12.75em")
                .setTop("0.5em")
                .setWidth("7.333333333333333em")
                .setImageClass("ri ri-check-line") // 替换 spafont spa-icon-ok
                .setCaption("$RAD.OK")
                .onClick("_btnok_onclick")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        iniExComs:function(){
            var ns = this;
            var designer = new RAD.Designer();
            designer.$moduleInnerMode=1;

            designer.setHost(ns, '_designer');
            designer.setEvents('onValueChanged',function(ipage, flag){
                // must consider the code side
                ns._dirty=ns._dirty||flag;
                ns.fireEvent('onValueChanged', [ns, ns._dirty]);
                ns.fireEvent('onDirty');
            });
            ns.paneMain.append(designer);
            ns._designer=designer;
        },
        events:{
            onModulePropChange:'_onModulePropChange', 
            onRender:'_onRender'
        },
        // Give a chance to determine which UI controls will be appended to parent container
        customAppend : function(parent, subId, left, top){
            this.dialog.showModal();
            return true;
        } ,
        _onModulePropChange:function(){
            var ns=this,
                prop = ns.properties;
            if(!('page' in prop))return;

            var page = prop.page, 
                tp = prop.targetProfile;

            ns.$iniCode = page.getJSCode(page.getWidgets(true, tp), tp.KEY, tp.$xid);
        },
        _onRender:function(ns, threadid){
            var prjConf = ood.get(SPA, ['curProjectConfig','$PageAppearance']);
            if(prjConf && prjConf.theme){
                ns.paneMain.setSandboxTheme(prjConf.theme,true,'[class~="ood-designer-inner-control"]');
            }
            ns._designer.refreshViewSize(null, false);
            ood.asyRun(function(){
                ns._designer.refreshView(ns.$iniCode, true);
            });
        },
        _beforeclose:function(){
            var ns=this;
            if(ns._dirty){
                ood.confirm(ood.getRes('RAD.notsave'), ood.getRes('RAD.notsave2'), function(){
                    ns._dirty=false;
                    ns.dialog.close();
                });
                return false;
            }
            return true;
        },
        _btncancel_onclick:function (profile, e, src, value){
            var ns=this;
            ns._dirty=false;
            ns.dialog.close();
        },
        _btnok_onclick:function (profile, e, src, value){
            var ns = this, hash={}, hash2={}, t;
            ood.each(ns._designer.$host, function(ins,i){
                if(i=="$inDesign"||!ins['ood.absBox']||!ins.n0)return;

                var prf=ins.get(0);
                t=hash[prf.alias]=prf.serialize(false,false,false);
                hash2[prf.alias]=prf;
                delete t.key;
                delete t.alias;
                delete t.events;
                delete t['ood.Module'];
            });

            ns.fireEvent('onFinished',[hash, hash2]);
            
            ns._dirty=false;
            ns.dialog.close();
        }
    }
});
