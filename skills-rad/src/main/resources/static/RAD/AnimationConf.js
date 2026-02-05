ood.Class('RAD.AnimationConf', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ood_ui_dialog1")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("66.66666666666667em")
                .setHeight("46.666666666666664em")
                .setCaption("Animation editor")
                .setMinBtn(false)
                .setModal(true)
            );
            
            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ood_ui_panel3")
                .setDock("right")
                .setLeft("40.833333333333336em")
                .setTop("8.333333333333334em")
                .setWidth("18.75em")
                .setCaption("Frame Setting")
                .setBorderType("none")
                );
            
            host.ood_ui_panel3.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block35")
                .setDock("top")
                .setLeft("2.5em")
                .setTop("4.166666666666667em")
                .setHeight("8.25em")
                .setBorderType("none")
                .setOverflow("hidden")
                );
            
            host.ood_ui_block35.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"c_type")
                .setName("type")
                .setShowDirtyMark(false)
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("16.583333333333332em")
                .setLabelSize("6em")
                .setLabelCaption("Type")
                .setType("listbox")
                .setItems([
                    {
                        "id":"a",
                        "caption":"item 1",
                        "imageClass":"ood-icon-number1"
                    },
                    {
                        "id":"b",
                        "caption":"item 2",
                        "imageClass":"ood-icon-number2"
                    },
                    {
                        "id":"c",
                        "caption":"item 3",
                        "imageClass":"ood-icon-number3"
                    },
                    {
                        "id":"d",
                        "caption":"item 4",
                        "imageClass":"ood-icon-number4",
                        "disabled":true
                    }
                ])
                .setValue("liner")
                .onChange("_c_onchange")
                );
            
            host.ood_ui_block35.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"c_duration")
                .setName("duration")
                .setShowDirtyMark(false)
                .setLeft("0.75em")
                .setTop("3.3333333333333335em")
                .setWidth("16.666666666666668em")
                .setLabelSize("6em")
                .setLabelCaption("Duration")
                .setType("spin")
                .setUnit("ms")
                .setPrecision(0)
                .setIncrement(100)
                .setMin(100)
                .setMax(600000)
                .setValue(200)
                .onChange("_c_onchange")
                );
            
            host.ood_ui_block35.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"c_times")
                .setName("times")
                .setShowDirtyMark(false)
                .setLeft("0.75em")
                .setTop("5.833333333333333em")
                .setLabelSize("6em")
                .setLabelCaption("Times")
                .setType("spin")
                .setPrecision(0)
                .setIncrement(1)
                .setMin(1)
                .setMax(99)
                .setValue(1)
                .onChange("_c_onchange")
                );
            
            host.ood_ui_block35.append(
                ood.create("ood.UI.CheckBox")
                .setHost(host,"c_restore")
                .setName("restore")
                .setShowDirtyMark(false)
                .setLeft("11.166666666666666em")
                .setTop("5.916666666666667em")
                .setWidth("6.25em")
                .setCaption("Restore")
                .onChange("_c_onchange")
                );
            
            host.ood_ui_panel3.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"c_grid")
                .setShowDirtyMark(false)
                .setLeft("0em")
                .setTop("0em")
                .setEditable(true)
                .setRowHandler(false)
                .setHeader([
                    {
                        "id":"key",
                        "caption":"Property",
                        "type":"label",
                        "width":"8em"
                    },
                    {
                        "id":"value",
                        "caption":"Value",
                        "type":"input",
                        "flexSize":true,
                        "width":"8em"
                    }
                ])
                .setActiveMode("none")
                .afterCellUpdated("_c_grid_aftercellupdated")
                );
            
            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block2")
                .setDock("fill")
                .setDockMargin({
                    "left":0,
                    "top":0,
                    "right":0,
                    "bottom":-1
                })
                .setLeft("12.5em")
                .setTop("15.833333333333334em")
                .setBorderType("inset")
                .setBackground("#FFFFFF")
                );
            
            host.ood_ui_block2.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ood_ui_div13")
                .setDock("origin")
                .setLeft("20em")
                .setTop("7.5em")
                .setWidth("6em")
                .setHeight("6em")
                .setOverflow("visible")
                .setCustomStyle({
                    "KEY":{
                        "background-color":"#FAFAD2",
                        "opacity":0
                    }
                })
                );
            
            host.ood_ui_div13.append(
                ood.create("ood.UI.Div")
                .setHost(host,"c_demo")
                .setClassName("ood-uibar")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("6em")
                .setHeight("6em")
                .setSelectable(false)
                .setHtml("<div style=\"text-align: center;\">&lt; l i n e 1 &gt;</div><div style=\"text-align: center;\">&lt; l i n e 2 &gt;</div><div style=\"text-align: center;\">&lt; l i n e 3 &gt;</div><div style=\"text-align: center;\">&lt; l i n e 4 &gt;</div><div style=\"text-align: center;\">&lt; l i n e 5 &gt;</div>")
                );
            
            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block14")
                .setDock("bottom")
                .setDockOrder(2)
                .setLeft("44.583333333333336em")
                .setTop("3.75em")
                .setHeight("4.5em")
                .setBorderType("inset")
                .setBackground("#FFFFF0")
                );
            
            host.ood_ui_block14.append(
                ood.create("ood.UI.Icon")
                .setHost(host,"ood_ui_icon1")
                .setTips("Add a frame")
                .setTop("0.75em")
                .setWidth("3.8333333333333335em")
                .setHeight("3.3333333333333335em")
                .setRight("0em")
                .setImageClass("ood-uicmd-add")
                .setIconFontSize("3em")
                .onClick("_ood_ui_icon1_onclick")
                .setCustomStyle({
                    "KEY":{
                        "cursor":"pointer"
                    }
                })
                );
            
            host.ood_ui_block14.append(
                ood.create("ood.UI.Icon")
                .setHost(host,"c_play")
                .setTips("Play the animation")
                .setLeft("0em")
                .setTop("0.75em")
                .setWidth("3.8333333333333335em")
                .setHeight("3.3333333333333335em")
                .setImageClass("ood-uicmd-toggle")
                .setIconFontSize("3em")
                .onClick("_c_play_onclick")
                .setCustomStyle({
                    "KEY":{
                        "cursor":"pointer"
                    }
                })
                );
            
            host.ood_ui_block14.append(
                ood.create("ood.UI.Gallery")
                .setHost(host,"c_gallery")
                .setShowDirtyMark(false)
                .setItems([
                    {
                        "id":"frame1",
                        "caption":"",
                        "imageClass":"ood-icon-number1",
                        "flagText":"",
                        "flagClass":"",
                        "flagStyle":""
                    }
                ])
                .setDock("bottom")
                .setDockMargin({
                    "left":50,
                    "top":0,
                    "right":50,
                    "bottom":0
                })
                .setLeft("10.833333333333334em")
                .setTop("23.333333333333332em")
                .setHeight("4.333333333333333em")
                .setBorderType("none")
                .setIconOnly(true)
                .setIconFontSize("2.5em ")
                .setValue("")
                .onItemSelected("_c_gallery_onitemselected")
                .onFlagClick("_c_gallery_onflagclick")
                .setCustomStyle({
                    "ITEMS":{
                        "overflow":"overflow-x:auto;overflow-y:hidden"
                    }
                })
                );
            
            host.ood_ui_dialog1.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block18")
                .setDock("bottom")
                .setLeft("52.5em")
                .setTop("0em")
                .setHeight("3em")
                .setBorderType("none")
                .setOverflow("hidden")
                );
            
            host.ood_ui_block18.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_ba")
                .setLeft("6.583333333333333em")
                .setTop("0.6666666666666666em")
                .setWidth("6.583333333333333em")
                .setHeight("2em")
                .setCaption("$RAD.designer.attreditor.Apply")
                .onClick("_ctl_ba_onclick")
                );
            
            host.ood_ui_block18.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_bc")
                .setLeft("19.083333333333332em")
                .setTop("0.6666666666666666em")
                .setWidth("6.583333333333333em")
                .setHeight("2em")
                .setCaption("$RAD.designer.attreditor.Cancel")
                .onClick("_ctl_bc_onclick")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _c_gallery_onflagclick:function (profile,item,e,src){
            var ns = this, uictrl = profile.boxing(),
                oitem = uictrl.getSelectedItem(),
                items = profile.properties.items;
            if(item.id==oitem.id){
                uictrl.fireItemClickEvent(items[0].id);
            }

            uictrl.removeItems(item.id);
            ns._dirty = 1;

            ood.arr.each(items,function(item, i){
                if(i<1)return;
                uictrl.updateItem(item.id, {"imageClass":"ood-icon-number" + (i+1)});
            });
        },
        setUItoForm:function(frame){
            var ns=this;
            if(frame){
                ns.c_duration.setUIValue(frame.duration||200,true).setDisabled(false);
                ns.c_restore.setUIValue(frame.restore||false,true).setDisabled(false);
                ns.c_times.setUIValue(frame.times||1,true).setDisabled(false);
                ns.c_type.setUIValue(frame.type||"liner",true).setDisabled(false);
            }else{
                ns.c_duration.setValue('',true).setDisabled(true);
                ns.c_restore.setValue(false,true).setDisabled(true);
                ns.c_times.setValue('',true).setDisabled(true);
                ns.c_type.setValue('',true).setDisabled(true);
            }
        },
        getDefaultStatus:function(){
            return {

            };
        },
        addFrame:function(){
            var ns = this,
                gal = ns.c_gallery,
                items = gal.getItems(),
                num = items.length + 1,
                id  = "frame" + ood.rand(),
                frame;
            // 1. copy setting from the previous one 
            frame = ood.clone(items[items.length-1]._frame);
            // If it's first frame, no following setting
            frame = ood.merge(frame, {
                type:'liner',
                times:1,
                duration:200,
                restore:false
            });

            // 2. add to gallery
            gal.insertItems([
                {
                    "id":id,
                    "caption":"",
                    "imageClass":"ood-icon-number" + num,
                    "flagClass":"oodcon ood-uicmd-close",
                    "flagStyle":"margin:0; cursor: default;",
                    _frame:frame
                }
            ]);
            // 3. select the new one
            gal.fireItemClickEvent(id);
        },
        _c_gallery_onitemselected:function (profile, item){
            var ns=this,tg=ns.c_grid;
            ns._currentFrame = item._frame;
            // set this frame's setting to ui
            ns.setUItoForm( item.id=="frame1" ? null :item._frame );

            // status
            var rows=ood.clone(ns._dft_rows);
            ood.arr.each(profile.properties.items,function(ii){
                ood.arr.each(rows,function(row){
                    if(row.id in ii._frame.status){
                        row.cells[1].value = ii._frame.status[row.id];
                    }
                });
                if(ii==item){
                    return false;
                }
            });

            tg.setRows(rows);
            ood.arr.each(rows,function(row){
                ns.resetDemo(row.id, row.cells[1].value, row.cells[1].unit);
            });
        },
        _ood_ui_icon1_onclick:function (profile, e, value){
            var ns = this, uictrl = profile.boxing();
            if(ns.c_gallery.getItems().length >= 9){
                ood.alert("It allowes 9 frames only");
            }else{
                ns.addFrame();
                ns._dirty = 1;
            }
        },
        _ctl_bc_onclick:function (profile, e, src, value){
            this.ood_ui_dialog1.close();
        },
        getAnimFrames:function(){
            var ns = this, 
                conf = [];
            ood.each(ns.c_gallery.getItems(),function(item){
                conf.push(item._frame);
            });
            return conf;
        },
        _ctl_ba_onclick:function (profile, e, src, value){
            var ns = this,
                conf = ns.getAnimFrames();
            ns.fireEvent("onchange",[conf]);
            ns.ood_ui_dialog1.close();
        },
        events:{
            "onRender":"_page_onrender",
            "onReady":"_page_onready"
        },
        _dft_rows:[
            {id:'left',cells:[{id:'left',caption:"$(RAD.custom_dlg.left)"},{
                type:"spin", unit:'em', editorProperties:{
                    precision:1,increment:1, units:'px;em'
                }
            }]},
            {id:'top',cells:[{id:'top',caption:"$(RAD.custom_dlg.top)"},{
                type:"spin", unit:'em', editorProperties:{
                    precision:1,increment:1, units:'px;em'
                }
            }]},
            {id:'width',cells:[{id:'width',caption:"$(RAD.custom_dlg.width)"},{
                type:"spin", unit:'em',editorProperties:{
                    precision:1,increment:1,  units:'px;em'
                }
            }]},
            {id:'height',cells:[{id:'height',caption:"$(RAD.custom_dlg.height)"},{
                type:"spin", unit:'em',editorProperties:{
                    precision:1,increment:1,  units:'px;em'
                }
            }]},
            {id:'opacity',cells:[{id:'opacity',caption:"$RAD.custom_dlg.opacity"},{
                type:"progress", value :1, editorProperties:{
                    min:0,max:1
                }
            }]},
            {id:'rotate',cells:[{id:'rotate',caption:"$(RAD.custom_dlg.rotate)"},{
                type:"spin", unit:'deg', editorProperties:{
                    precision:0,increment:1, min:0,max:360
                }
            }]},
            {id:'translateX',cells:[{id:'translateX',caption:"$(RAD.custom_dlg.translateX)"},{
                type:"spin", editorProperties:{
                    precision:1,increment:1
                }
            }]},
            {id:'translateY',cells:[{id:'translateY',caption:"$(RAD.custom_dlg.translateY)"},{
                type:"spin", editorProperties:{
                    precision:1,increment:1
                }
            }]},
            {id:'scaleX',cells:[{id:'scaleX',value:1,caption:"$(RAD.custom_dlg.scaleX)"},{
                type:"spin", editorProperties:{
                    precision:1,increment:0.1
                }
            }]},
            {id:'scaleY',cells:[{id:'scaleY',value:1,caption:"$(RAD.custom_dlg.scaleY)"},{
                type:"spin", editorProperties:{
                    precision:1,increment:0.1
                }
            }]},
            {id:'skewX',cells:[{id:'skewX',caption:"$(RAD.custom_dlg.skewX)"},{
                type:"spin", unit:'deg', editorProperties:{
                    precision:0,increment:1, min:-180,max:180
                }
            }]},
            {id:'skewY',cells:[{id:'skewY',caption:"$(RAD.custom_dlg.skewY)"},{
                type:"spin", unit:'deg', editorProperties:{
                    precision:0,increment:1, min:-180,max:180
                }
            }]},
            {id:'color',cells:[{id:'color',caption:"$RAD.custom_dlg.color"},{
                type:"color"
            }]},
            {id:'fontSize',cells:[{id:'fontSize',caption:"$(RAD.custom_dlg.font$-size)"},{
                type:"spin", unit:'em', editorProperties:{
                    min:0,max:10,precision:0,increment:1, units:'em;px;pt'
                }
            }]},
            {id:'lineHeight',cells:[{id:'lineHeight',caption:"$(RAD.custom_dlg.line$-height)"},{
                type:"spin", editorProperties:{
                    min:0,max:10,precision:1,increment:0.1
                }
            }]},
            {id:'backgroundColor',cells:[{id:'backgroundColor',caption:"$(RAD.custom_dlg.background$-color)"},{
                type:"color"
            }]},
            {id:'backgroundPositionX',cells:[{id:'backgroundPositionX',caption:"$(RAD.custom_dlg.background$-position$-x)"},{
                type:"spin", unit:'%', editorProperties:{
                    precision:1,increment:1,  units:'px;em;%'
                }
            }]},
            {id:'backgroundPositionY',cells:[{id:'backgroundPositionY',caption:"$(RAD.custom_dlg.background$-position$-y)"},{
                type:"spin", unit:'%', editorProperties:{
                    precision:1,increment:1, units:'px;em;%'
                }
            }]}
        ],
        _page_onready:function(){
            var ns=this;
            ns.c_type.setItems((function(h){
                var arr=[];
                ood.each(h,function(o,i){
                    arr.push({
                        id:i,
                        tips:'<img width=80 height=80 src="/RAD/img/animate/'+i+'.gif" /> '
                    });
                });
                return arr;
            })(ood.Dom.$AnimateEffects));    
        },
        _page_onrender:function (module, threadid){
            var ns=this;
            // ensure frames
            var prop = this.properties, frames=prop.frames;
            
            var h=ns._initedRegion={};
            h.left = ns.c_demo.getLeft();
            h.top = ns.c_demo.getTop();
            h.right = ns.c_demo.getRight();
            h.bottom = ns.c_demo.getBottom();
            h.width = ns.c_demo.getWidth();
            h.height = ns.c_demo.getHeight();

            if(!frames || frames.length===0 || (frames.length===1 && !frames[0].status) ){
                frames = [{status: ns.getDefaultStatus()}];
            }

            // set to gallery
            var items = [];
            ood.arr.each(frames, function(frame, num){
                frame.status = frame.status ||{};
                num += 1;
                if(num===1){
                    frame = {status:frame.status};
                    items.push({
                        "id":'frame' + num,
                        "imageClass":"ood-icon-number" + num,
                        "flagStyle":"margin:0; cursor: default;",
                        _frame:frame
                    });         
                }else{
                    items.push({
                        "id":'frame' + num,
                        "imageClass":"ood-icon-number" + num,
                        "flagClass":"oodcon ood-uicmd-close",
                        "flagStyle":"margin:0; cursor: default;",
                        _frame:frame
                    }); 
                } 
            });
            ns.c_gallery.setItems(items);

            // select the first one
            ns.c_gallery.selectItem("frame1");
        },
        _c_onchange:function (profile, oldValue, newValue, force, tag){
            var ns = this;
            ns._currentFrame[profile.properties.name] = newValue;
        },
        _c_grid_aftercellupdated:function (profile, cell, options, isHotRow){
            var ns = this, 
                obj = ns._currentFrame.status,
                rowId = cell._row.id;
            ns.resetStatus(obj, rowId, cell.value, cell.unit);
            ns.resetDemo(rowId, cell.value, cell.unit);
        },
        resetStatus:function(status, key, value, unit){
            switch(key){
                case 'left':
                case 'top':
                case 'right':
                case 'bottom':
                case 'width':
                case 'height':
                case 'rotate':
                case 'skewX':
                case 'skewY':
                case 'fontSize':
                case 'backgroundPositionX':
                case 'backgroundPositionY':
                    if(ood.isEmpty(value))delete status[key];
                    else status[key]=ood.isSet(value)?(value + (unit||'')):null;
                    break;
                case 'opacity':
                case 'translateX':
                case 'translateY':
                case 'scaleX':
                case 'scaleY':
                case 'lineHeight':
                case 'color':
                case 'backgroundColor':
                    if(ood.isEmpty(value))delete status[key];
                    else status[key]=value;
                    break;
                      }
        },
        _map:{left:1,right:1,top:1,bottom:1,width:1,height:1},
        _get : function(node, ctrl, key, t){
            var ns=this;
            return (ns._map[key] && ctrl && ood.isFun(ctrl[t='get'+ood.str.initial(key)])) ? ns._initedRegion[key] : node[key] ? node[key]() :node.css(key);
        },
        _set : function(node, ctrl, key, value, t){
            var ns=this;
            return (ns._map[key] && ctrl && ood.isFun(ctrl[t='set'+ood.str.initial(key)])) ? ctrl[t](value) : node[key] ? node[key](value) :node.css(key, value);
        },        
        resetDemo:function(key, value, unit){
            var ns=this;
            value = ood.isSet(value)?(value + (unit||'')):'';

            var node=ns.c_demo.getRoot(),
                ctrl=ns.c_demo;
            value  = ns._map[key] ? (value || ns._get(node, ctrl, key)) : value;
            return ns._set(node, ctrl, key, value);
        },
        _c_play_onclick:function (profile, e, value){
            var ns = this, items = ns.c_gallery.getItems();
            ood.Dom.busy("temp_animation");

            ns.c_gallery.selectItem(items[0].id);
            ood.asyRun(function(){
                var anm = new ood.AnimBinder(), i=0;
                anm.setFrames(ns.getAnimFrames());
                anm.beforeFrame(function(p, frame){
                    ns.c_gallery.setUIValue(items[++i].id,true);
                });
                anm.onEnd(function(){
                    // set to the last frame
                    var offset = (items[items.length-1]._frame.restore?1:0);
                    ns.c_gallery.selectItem(items[items.length-1-offset].id);
                    ns._c_gallery_onitemselected(ns.c_gallery.get(0), items[items.length-1 - offset]);
                    ood.Dom.free("temp_animation");
                });
                anm.apply(ns.c_demo.getRoot());
            },200);
        }
    }
});
