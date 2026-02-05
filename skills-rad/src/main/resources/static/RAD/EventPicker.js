ood.Class('RAD.EventPicker', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ood_ui_dialog1")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("59.166666666666664em")
                .setHeight("33.333333333333336em")
                .setCaption("$(RAD.inter.Event Picker)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setModal(true)
                .beforeClose("_close")
            );
            
            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block2")
                .setDock("fill")
                .setLeft("16.666666666666668em")
                .setTop("4.166666666666667em")
                .setBorderType("inset")
                );
            
            host.ood_ui_block2.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"ood_ui_layout3")
                .setItems([
                    {
                        "id":"before",
                        "size":80,
                        "min":10,
                        "locked":false,
                        "folded":false,
                        "hidden":false,
                        "cmd":false,
                        "pos":"before"
                    },
                    {
                        "id":"main",
                        "size":120,
                        "min":10
                    },
                    {
                        "id":"after",
                        "size":80,
                        "min":10,
                        "locked":false,
                        "folded":false,
                        "hidden":true,
                        "cmd":false,
                        "pos":"after"
                    }
                ])
                .setLeft("0em")
                .setTop("0em")
                .setType("horizontal")
                .setFlexSize(true)
                );
            
            host.ood_ui_layout3.append(
                ood.create("ood.UI.List")
                .setHost(host,"list_all")
                .setDirtyMark(false)
                .setDock("fill")
                .setLeft("3.3333333333333335em")
                .setTop("1.6666666666666667em")
                .setWidth("26.666666666666668em")
                .setBorderType("none")
                .setLabelSize("8.333333333333334em")
                .setLabelPos("none")
                .onItemSelected("_list_all_onitemselected")
                , "before");
            
            host.ood_ui_layout3.append(
                ood.create("ood.UI.TreeBar")
                .setHost(host,"event_all")
                .setDirtyMark(false)
                .setLeft("0em")
                .setTop("0em")
                .setSelMode("none")
                .onItemSelected("_event_all_onitemselected")
                , "main");
            
            host.ood_ui_layout3.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"filter1")
                .setDirtyMark(false)
                .setDock("top")
                .setDockMargin({
                    "left":0,
                    "top":0,
                    "right":0,
                    "bottom":4
                })
                .setTips("$(RAD.designer.tool.Filter) ")
                .setHeight("4.25em")
                .setDynCheck(true)
                .setLabelSize("2em")
                .setLabelPos("top")
                .setLabelCaption("$(RAD.inter.Select the target Modue or Control)")
                .setLabelHAlign("center")
                .setType("input")
                .setImageClass("ood-icon-filter")
                .setCommandBtn("delete")
                .onChange("_propFilter_onchange")
                .onCommand("_propFilter_oncmd")
                .setCustomClass({
                    "KEY":"ood-uibar"
                })
                , "before");
            
            host.ood_ui_layout3.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"filter2")
                .setDirtyMark(false)
                .setDock("top")
                .setDockMargin({
                    "left":0,
                    "top":0,
                    "right":0,
                    "bottom":4
                })
                .setTips("$(RAD.designer.tool.Filter) ")
                .setHeight("4.25em")
                .setDynCheck(true)
                .setLabelSize("2em")
                .setLabelPos("top")
                .setLabelCaption("$(RAD.inter.Pick the target event)")
                .setLabelHAlign("center")
                .setType("input")
                .setImageClass("ood-icon-filter")
                .setCommandBtn("delete")
                .onChange("_propFilter2_onchange")
                .onCommand("_propFilter2_oncmd")
                .setCustomClass({
                    "KEY":"ood-uibar"
                })
                , "main");
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        events:{onRender:'_onrender'},
        _onrender:function(){
            var ns=this;
            if(ns._alias)
                ns.list_all.selectItem(ns._alias);
        },
        refreshList:function(page, alias){
            var ns=this;
            ns._alias=alias;

            //get items
            var items=[], moduleHash={},
                getCap=function(profile){
                    var prop=profile.properties,
                        type = (profile['ood.Module'] || profile.moduleClass) ?"module":"control", 
                        cls = profile.key.split(".").pop(),
                        cap;
                     if(type=="module"){
                        cap = "$(RAD.action.Module) - " + profile.getAlias() + " ( "+ profile.key +" )";
                    }else{
                        cap = "$(RAD.action.Control) - " + profile.alias + " ( "+cls+" )";
                        if(prop.name || prop.desc)cap += " <br />[ " + (prop.name ||"") + (prop.name?" - ":"") + (prop.desc||"")  +" ]";
                        if(prop.caption||prop.labelCaption) cap += " [ "+  (prop.caption||prop.labelCaption)  +" ]";
                    }
                    return cap;
                },
                fun = function(profile, items, map, layer){
                    if((page.$inplaceEditor&&page.$inplaceEditor.get(0))==profile)
                        return;
                    var type = profile.moduleClass?"module":"control", 
                          key = type=="module"?"ood.Module":profile.box.KEY,
                          z;
                    ++layer;
                    if(type=="module"){
                        /// get the top module only
                        profile = profile.getModule(true);
                        if(moduleHash[z = profile.KEY+"["+profile.$xid+"]"])return;
                        moduleHash[z] = 1;
                    }
                    var self=arguments.callee,
                        t = map[key],
                        item = {
                            id:layer==1?"-page":profile.alias, 
                            type:type,
                            target:layer==1?(new ood.Module):profile,
                            itemStyle:"padding-left:"+(layer-1)*2+"em;",
                            imageClass: layer==1?"oodcon spafont spa-icon-page":t&&t.imageClass||'',
                            tag: layer==1?"page":getCap(profile).replace(/\<[^>]+\>/g,''),
                            caption: layer==1?"$(RAD.action.var.Current Page)":getCap(profile)
                        };
                    items.push(item);
                    if(type=="module"){
                        return ;
                    }
                    if(profile.children && profile.children.length){
                        ood.arr.each(profile.children,function(o){
                            self.call(null, o[0], items, map, layer);
                        });
                    }
                };
            fun(page.canvas.get(0), items, CONF.mapWidgets, 0);
            
            ns.list_all.setItems(items);
        },
        _propFilter_oncmd:function(){
            this._propFilter1_("");
        },
        _propFilter_onchange:function(profile, oldValue, newValue){
            ood.resetRun('_propFilter1_',this._propFilter1_,500,[newValue],this);
        },
        _propFilter1_:function(newValue){
            var  v=this.list_all.getValue();
            this.list_all.doFilter(newValue ? function(item){
                if(v==item.id)return false;
                 var reg = new RegExp(newValue||"",'i');
                  return !reg.test(item.tag);
            } : null);
        },
        _propFilter2_oncmd:function(){
            this._propFilter2_("");
        },
        _propFilter2_onchange:function(profile, oldValue, newValue){
            ood.resetRun('_propFilter2_',this._propFilter2_,500,[newValue],this);
        },
        _propFilter2_:function(newValue){
            this.event_all.doFilter(newValue ? function(item){
                 var reg = new RegExp(newValue||"",'i');
                  return !reg.test(item.caption);
            } : null);
        },
        _list_all_onitemselected:function (profile, item, e, src, type){
            var ns = this, uictrl = profile.boxing(),
                target = item.target,
                isCanvas = item.id=='-page',
                arr=[];
            ood.each(isCanvas?ood.Module.$EventHandlers : target.box.$EventHandlers,function(o,i){
                if(i.charAt(0)=="_")return;
                if(CONF.widgets_hiddenEvents[i])return;
                if(ood.get(CONF.widgets_hiddenEvents,[target.box.KEY, i]))return;
                if(CONF.widgets_hiddenEvents_filter && false===CONF.widgets_hiddenEvents_filter(target.box.KEY, i)) return;

                var preName = i.charAt(0)=='b'?"$(RAD.designer.eventbfrtag)":
                            i.charAt(0)=='a'?"$(RAD.designer.eventafttag)":
                            "$(RAD.designer.eventontag)",
                    ic = RAD.EditorTool.getEventName(isCanvas?'ood.Module':target.key, i, true);

                arr.push( {
                        id: i, 
                        caption: preName + ic + " - <span style='color:#888;font-style: italic;font-weight:normal;'>" + (ood.get(RAD.EditorTool.getDoc(target.box.KEY+".prototype."+i,null,true),["$desc"])||"").split('.')[0]  + '</span>'
                    });
            });

 
            var arrCat=[],a;
            ood.arr.each(CONF.widgets_events_cat,function(sub){
                a=[];
                ood.arr.each(sub.items,function(id, k){
                    if((k=ood.arr.subIndexOf(arr, 'id', id)) != -1){
                        a.push(arr[k]);
                        arr[k]=null;
                    }
                });
                if(a.length)
                    arrCat.push({
                        id:"sub:"+sub.id,
                        caption:"<span style='font-weight:normal'>$(RAD.designer.conf.propcat."+sub.id+")</span>",
                        group:true,
                        initFold:false,
                        cellStyle:'font-weight:normal',
                        sub: a
                    });
            });
            ood.filter(arr,function(o){
                return !!o;
            });
            if(arr.length){
                ood.arr.stableSort(arr,function(x,y){
                    return x.id>y.id?1:-1;
                });
                arrCat.push({
                    id:"sub:featured",
                    caption:"<span style='font-weight:normal'>$(RAD.designer.conf.propcat.Featured Events)</span>",
                    group:true,
                    initFold:false,
                    sub: arr
                });
                if(arrCat.length>=2){
                    var a = arrCat[arrCat.length-1];
                    arrCat[arrCat.length-1] = arrCat[arrCat.length-2];
                    arrCat[arrCat.length-2] = a;
                }
            }
            ns.event_all.setItems(arrCat);
        },
        _event_all_onitemselected:function (profile, item, e, src, type){
            var ns = this, uictrl = profile.boxing(),
                v1 = ns.list_all.getValue(),
                v2 = item.id;
            ns.fireEvent("onSel", [v1, v2]);
            ns.ood_ui_dialog1.close();
        }
    }
});
