ood.Class('RAD.CustomPackage', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ctl_dialog")
                .setLeft("8.5em")
                .setTop("1.8333333333333333em")
                .setWidth("28.166666666666668em")
                .setHeight("21.416666666666668em")
                .setCaption("$(RAD.nwpackage.Build Options)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
                .beforeClose("_ctl_dialog_beforeclose")
            );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group1")
                .setLeft("0.625em")
                .setTop("7.416666666666667em")
                .setWidth("11.833333333333334em")
                .setOverflow("hidden")
                .setCaption("$RAD.nwpackage.Appearance")
                .setToggleBtn(false)
                );
            
            host.ctl_group1.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_lappearance")
                .setDirtyMark(false)
                .setDock("fill")
                .setLeft("0.625em")
                .setTop("0em")
                .setWidth("6.25em")
                .setHeight("6.25em")
                .setSelMode("multi")
                .setItemRow(true)
                .setCheckBox(true)
                .setValue("")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group3")
                .setLeft("13.333333333333334em")
                .setTop("0.625em")
                .setWidth("13.25em")
                .setHeight("13.416666666666666em")
                .setOverflow("hidden")
                .setCaption("$(RAD.nwpackage.Locale files)")
                .setToggleBtn(false)
                );
            
            host.ctl_group3.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_llocale")
                .setDirtyMark(false)
                .setItems([{
                    "id":"en.js",
                    "caption":"$RAD.nwpackage.English",
                    "disabled":true
                },
                {
                    "id":"ru.js",
                    "caption":"$RAD.nwpackage.Russian",
                    "image":""
                },
                {
                    "id":"ja.js",
                    "caption":"$RAD.nwpackage.Japanese "
                },
                {
                    "id":"cn.js",
                    "caption":"$(RAD.nwpackage.Simplified Chinese)"
                },
                {
                    "id":"tw.js",
                    "caption":"$(RAD.nwpackage.Traditional Chinese)",
                    "image":""
                }])
                .setDock("fill")
                .setLeft("0.5625em")
                .setTop("0.375em")
                .setWidth("11.916666666666666em")
                .setHeight("10.5em")
                .setSelMode("multi")
                .setItemRow(true)
                .setCheckBox(true)
                .setValue("en.js")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group4")
                .setLeft("0.625em")
                .setTop("0.625em")
                .setWidth("11.833333333333334em")
                .setHeight("5.916666666666667em")
                .setOverflow("hidden")
                .setCaption("$(RAD.nwpackage.OOD core)")
                .setToggleBtn(false)
                );
            
            host.ctl_group4.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_llib")
                .setDirtyMark(false)
                .setItems([{
                    "id":"ood-all.js",
                    "caption":"ood lib",
                    "image":"",
                    "disabled":true
                }])
                .setDock("fill")
                .setLeft("0.625em")
                .setTop("0.375em")
                .setWidth("6.25em")
                .setHeight("3.375em")
                .setSelMode("multi")
                .setItemRow(true)
                .setCheckBox(true)
                .setValue("ood-all.js")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_ok")
                .setLeft("19.75em")
                .setTop("15.75em")
                .setWidth("4.375em")
                .setCaption("$inline.ok")
                .onClick("_ctl_ok_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_cancel")
                .setLeft("14.166666666666666em")
                .setTop("15.75em")
                .setWidth("4.375em")
                .setCaption("$inline.cancel")
                .onClick("_ctl_cancel_onclick")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(){
            var ns=this;
            var filename=SPA.curProjectPath + "/.settings/project.json",
                status=CONF.getStatus(filename),locale=["en.js"];

            status=status.Compile||{};
            
            if(status.lib){
                ns.ctl_llib.setValue(status.lib);
            }
            if(status.appearance){
                ns.ctl_lappearance.setValue(status.appearance);
            }
            
            if(status.locale){
                for(var i=0,l=status.locale.length,o;i<l;i++){
                    o=status.locale[i];
                    if(!ood.str.endWith(o,".js"))o=o+".js";
                    if(ood.arr.indexOf(locale, o)==-1){
                        locale.push(o);
                    }
                }
            }else  if(CONF.dftPackageLang){
                for(var i=0,l=CONF.dftPackageLang.length,o;i<l;i++){
                    o=CONF.dftPackageLang[i];
                    if(!ood.str.endWith(o,".js"))o=o+".js";
                    if(ood.arr.indexOf(locale, o)==-1){
                        locale.push(o);
                    }
                }
            }
            ns.ctl_llocale.setValue(locale);
            
            ns.ctl_dialog.showModal();
            return true;
        },
        _ctl_cancel_onclick:function (profile, e, src, value){
            this.ctl_dialog.close();
        },
        _ctl_ok_onclick:function (profile, e, src, value){
            var ns=this, conf={Compile:{
                lib:ns.ctl_llib.getValue(true), 
                appearance:ns.ctl_lappearance.getValue(true), 
                locale:ns.ctl_llocale.getValue(true)
            }};

            var filename=SPA.curProjectPath + "/.settings/project.json";
            var status=CONF.getStatus(filename);
            ood.merge(status,conf,'all');
            CONF.saveStatus(status, filename);
            
            ns.fireEvent("onOK", [conf.Compile.lib, conf.Compile.appearance, conf.Compile.locale]);
            ns._ok=1;
            ns.ctl_dialog.close();
        },
        _ctl_dialog_beforeclose:function (profile){
            if(!this._ok) this.fireEvent("onCancel");
        },
        events:{
            "onReady" : "_onReady"
        },
        _onReady:function (com, threadid){
            var items = [];
            ood.arr.each(CONF.designer_themes2,function(o){
                if(!o)return;
                items.push({
                    id:o,
                    caption:ood.str.initial(o),
                    disabled:o=="default"
                });
            });
            this.ctl_lappearance.setItems(items).setValue("default",true);
        }
    }
});
