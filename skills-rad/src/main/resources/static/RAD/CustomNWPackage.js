ood.Class('RAD.CustomNWPackage', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ctl_dialog")
                .setLeft("7.5em")
                .setTop("5.625em")
                .setWidth("39.166666666666664em")
                .setHeight("22.583333333333332em")
                .setCaption("$(RAD.nwpackage.Build Options)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
            );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group1")
                .setLeft("12.5em")
                .setTop("10em")
                .setWidth("11.666666666666666em")
                .setHeight("8.333333333333334em")
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
                .setLeft("25em")
                .setTop("2.5em")
                .setWidth("12.416666666666666em")
                .setHeight("13.333333333333334em")
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
                .setHeight("8.375em")
                .setSelMode("multi")
                .setItemRow(true)
                .setCheckBox(true)
                .setValue("en.js")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group4")
                .setLeft("12.5em")
                .setTop("2.5em")
                .setWidth("11.666666666666666em")
                .setHeight("6.666666666666667em")
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
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group8")
                .setLeft("0.625em")
                .setTop("2.5em")
                .setWidth("11.25em")
                .setHeight("15.833333333333334em")
                .setOverflow("hidden")
                .setCaption("$(RAD.nwpackage.Node modules)")
                .setToggleBtn(false)
                );
            
            host.ctl_group8.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_lmodule")
                .setDirtyMark(false)
                .setLeft("0.5625em")
                .setTop("0.375em")
                .setHeight("11.5em")
                .setSelMode("multi")
                .setItemRow(true)
                .setCheckBox(true)
                .setValue("")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_ok")
                .setLeft("31.5em")
                .setTop("16.666666666666668em")
                .setWidth("4.375em")
                .setCaption("$inline.ok")
                .onClick("_ctl_ok_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_cancel")
                .setLeft("25.833333333333332em")
                .setTop("16.666666666666668em")
                .setWidth("4.375em")
                .setCaption("$inline.cancel")
                .onClick("_ctl_cancel_onclick")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Input")
                .setHost(host,"ctl_name")
                .setDirtyMark(false)
                .setLeft("0.625em")
                .setTop("0.25em")
                .setWidth("36.75em")
                .setLabelSize(150)
                .setLabelCaption("$(RAD.nwpackage.Executable file name)")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(){
            var ns=this,
                filename=SPA.curProjectPath + "/.settings/project.json",
                status=CONF.getStatus(filename);
            status=status.Compile||{};
            
            ns.ctl_lmodule.setItems(this.properties.nodeModules||[]);
            
            ns.ctl_name.setValue(status.exefile||SPA.curProjectName);
            
            if(status.lib){
                ns.ctl_llib.setValue(status.lib);
            }
            if(status.appearance){
                ns.ctl_lappearance.setValue(status.appearance);
            }
            if(status.locale){
                ns.ctl_llocale.setValue(status.locale);
            }
            if(status.module){
                ns.ctl_lmodule.setValue(status.module);
            }

            ns.ctl_dialog.showModal();
            return true;
        },
        _ctl_cancel_onclick:function (profile, e, src, value){
            this.ctl_dialog.close();
        },
        _ctl_ok_onclick:function (profile, e, src, value){
            var ns=this, 
                name=ood.str.trim(ns.ctl_name.getUIValue());
            if(!CONF.fileNames.test(name)){
                ood.message(ood.getRes('RAD.addfile.invalidName'));
                return;
            }

            var conf={Compile:{
                exefile : name,
                lib : ns.ctl_llib.getValue(true), 
                appearance : ns.ctl_lappearance.getValue(true), 
                locale : ns.ctl_llocale.getValue(true),
                module : ns.ctl_lmodule.getValue(true)
            }},
            filename=SPA.curProjectPath + "/.settings/project.json";
            
            NWUtil.ensureConfigAndIcon(SPA.curProjectPath);
            
            var status=CONF.getStatus(filename);
            ood.merge(status,conf,'all');
            CONF.saveStatus(status, filename);
            
            ns.fireEvent("onOK", [conf.Compile.exefile, conf.Compile.lib, conf.Compile.appearance, conf.Compile.locale, conf.Compile.module]);

            ns.ctl_dialog.close();
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
