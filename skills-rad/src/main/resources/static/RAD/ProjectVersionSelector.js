ood.Class('RAD.ProjectVersionSelector', 'ood.Module',{
    Instance:{

        customAppend:function(){
            var ns=this,
                prop = ns.properties,
                dlg=ns.dialog;
            ns.listName.setItems([]).setUIValue('',true);
            if(prop.fromRegion)
                dlg.setFromRegion(prop.fromRegion);
            dlg.show(ns.parent, true);

            ns._curPage=1;
            ns._filter=null;
            ns._refreshContent();
        },
        _refreshContent:function(){
            var ns = this,
                prop = ns.properties,
                curPage = ns._curPage||1,
                newValue=ns.ctl_input1.getValue();
            ns._filter = ".*"+newValue+".*";
            ns.dialog.busy();


            ood.Ajax(CONF.getProjectVersionListService,  {projectName:prop.projectName,pattern:ns._filter},
                function(txt){
                    ns.dialog.free();
                    var obj = txt;
                    if(!obj || obj.error)
                        ood.message(ood.get(obj,['error','errdes'])||'no response!');
                    else{
                        var sum=obj.data&&obj.data.sum;
                        if(sum){
                            var pages=Math.ceil(sum/100);
                            ns.ctl_pagebar1.setValue("1:"+curPage+":"+pages);
                        }
                        obj=obj.data.files||obj.data;
                        ns.properties.projectList=[];
                        if(obj && obj.length){
                            ood.arr.each(obj,function(i){
                                    ns.properties.projectList.push({id:i.location,name:i.versionName,caption:i.versionName});
                            });
                        }
                        ns.listName.setItems(prop.projectList);
                    }
                },
                function(msg){
                    ns.dialog.free();
                    ood.message(msg);
                }).start();

        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("5.625em")
                .setTop("2.5em")
                .setWidth("33.416666666666664em")
                .setHeight("24.083333333333332em")
                .setCaption("切换工程分支")
                .setImageClass("spafont spa-icon-select")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("visible")
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_pane70")
                .setDock("bottom")
                .setHeight("2.8333333333333335em")
                );
            
            host.ctl_pane70.append(
                ood.create("ood.UI.PageBar")
                .setHost(host,"ctl_pagebar1")
                .setDock("bottom")
                .setHeight("2.1666666666666665em")
                .setCaption("")
                .onClick("_ctl_pagebar1_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.List")
                .setHost(host,"listName")
                .setDirtyMark(false)
                .setDock("fill")
                .setSelMode("none")
                .setBorderType("inset")
                .onClick("_listname_onclick")
                );

            host.dialog.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"ctl_input1")
                .setDirtyMark(false)
                .setDock("top")
                .setDockMargin({
                    "left":0,
                    "top":3,
                    "right":0,
                    "bottom":4
                }
                )
                .setHeight("1.8333333333333333em")
                .setDynCheck(true)
                .setLabelSize(120)
                .setLabelCaption("$(RAD.Search)")
                .setType("none")
                .setImageClass("ood-icon-filter")
                .setCommandBtn("delete")
                .onChange("_ctl_input1_onchange")
                .onCommand("_ctl_input1_oncommand")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _dialog_beforeclose:function(profile){
            this.dialog.hide();
            return false;
        },
        _dialog_onhotkey:function(profile, key){
            if(key.key=='esc')
                profile.boxing().close();
        },
        _listname_onclick:function (profile, item, e, src){
            var ns=this,
                pm = ns.projectName = item.name;
            if(!ns.projectName){
                ood.message(ood.getRes('RAD.ps.noselected'));
                return;
            }

            $ESD.openProject( ns.projectName)
            //
            // ns.dialog.close();
            // CONF.openProject(pm, ns.properties.onOK1,ns.properties.onOK2,function(){
            // },ns.properties.namespace);
        },
        _ctl_input1_oncommand:function (profile, src){
            profile.boxing().setValue("",true);
            this._refreshContent();
        },
        _ctl_input1_onchange:function (profile, oldValue, newValue, force, tag){
            var ns = this, uictrl = profile.boxing();
            ood.resetRun("image_search",function(){
                ns._refreshContent();
            }, 500);
        },
        _ctl_pagebar1_onclick:function (profile, page){
            var ns = this;
            // set to this page
            ns._curPage = page;
            ns._refreshContent();
        }
    }
});
