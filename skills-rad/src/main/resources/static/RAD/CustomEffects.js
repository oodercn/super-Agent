ood.Class('RAD.CustomEffects', 'ood.Module',{
    Instance:{
        customAppend:function(){
            var ns=this,
                prop=ns.properties;
            ns.setTargetProfile(prop.targetProfile);
            ns.dialog.showModal();
            return true;
        },
        setTargetProfile:function(profile){
            var ns=this,
                prop=profile.properties,
                showEffects=prop.showEffects,
                hideEffects=prop.hideEffects;
            ns._resetEffects(showEffects, "show");
            ns._resetEffects(hideEffects, "hide");
        },
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Gallery")
                .setHost(host,"gallery")
                .setShowEffects("Blur")
                .setLeft("0em")
                .setTop("0em")
                .setWidth(430)
                .setHeight(256)
                .setSelMode("none")
                .setItemWidth(84)
                .setItemHeight(84)
                .setImgWidth(80)
                .setImgHeight(80)
                .onItemSelected("_ctl_gallery_onitemselected")
                .setCustomStyle({
                    "ITEMS":{
                        "background-color":"#FFFFFF"
                    }
                }
                )
            );
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("9.875em")
                .setTop("1.75em")
                .setWidth("33.916666666666664em")
                .setHeight("23.416666666666668em")
                .setCaption("$(RAD.designer.effects.Custom Effects)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setRestoreBtn(false)
                .setOverflow("hidden")
                .beforeClose("_ctl_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block192")
                .setDock("fill")
                .setDockMargin({
                    "left":0,
                    "top":0,
                    "right":0,
                    "bottom":34
                }
                )
                .setBorderType("inset")
                .setOverflow("hidden")
                );
            
            host.ctl_block192.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"tg")
                .setDirtyMark(false)
                .setEditable(true)
                .setGridHandlerCaption("$RAD.designer.effects.Attributes")
                .setRowHandlerWidth("7.5em")
                .setColSortable(false)
                .setHeader([{
                    "id":"show",
                    "caption":"$(RAD.designer.effects.Show Effects)",
                    "width":"11.666666666666666em",
                    "type":"input"
                },
                {
                    "id":"hide",
                    "caption":"$(RAD.designer.effects.Hide Effects)",
                    "width":"11.666666666666666em",
                    "type":"input"
                }])
                .setActiveMode("none")
                .setTreeMode('none')
                .setValue(",")
                .afterCellUpdated("_tg_aftercellupdated")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_button15")
                .setLeft("18.25em")
                .setTop("17.75em")
                .setWidth("6.666666666666667em")
                .setImageClass("spafont spa-icon-cancel")
                .setCaption("$RAD.cancel")
                .onClick("_btncancel_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_button16")
                .setLeft("25.75em")
                .setTop("17.75em")
                .setWidth("6.666666666666667em")
                .setImageClass("spafont spa-icon-ok")
                .setCaption("$RAD.ok")
                .onClick("_btnok_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btn_prev")
                .setTag("show")
                .setLeft("0.375em")
                .setTop("17.75em")
                .setWidth("7.5em")
                .setHeight("2.1666666666666665em")
                .setZIndex(1003)
                .setCaption("$(RAD.designer.effects.Preview Show)")
                .onClick("_ctl_preview_onclick")
                );
            
            host.dialog.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_next")
                .setTag("hide")
                .setLeft("8.333333333333334em")
                .setTop("17.75em")
                .setWidth("7.5em")
                .setHeight("2.1666666666666665em")
                .setZIndex(1003)
                .setCaption("$(RAD.designer.effects.Preview Hide)")
                .onClick("_ctl_preview_onclick")
                );
            
            append(
                ood.create("ood.UI.List")
                .setHost(host,"ctl_list")
                .setDirtyMark(false)
                .setLeft("0.5em")
                .setTop("13.625em")
                .setWidth("9.375em")
                .setHeight("auto")
                .onItemSelected("_ctl_list20_onitemselected")
            );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }, 
        _sbutton3_onclick:function (profile, e, src, value) {
            var ns=this, args={},type=ns.list2.getUIValue(),fun=function(){
                if(ns.cWidth.getUIValue())args.width=[50,250];
                if(ns.cHeight.getUIValue())args.height=[50,250];
                if(ns.cLeft.getUIValue())args.left=[100,300];
                if(ns.cTop.getUIValue())args.top=[100,300];
                if(ns.cOpacity.getUIValue())args.opacity=[1,0.2];
                if(ns.cBackgroundColor.getUIValue())args.backgroundColor=['#ffffff','#000000'];
                if(ns.cScrollTop.getUIValue())args.scrollTop=[0,100];
                if(ns.cRotate.getUIValue())args.rotate=[0,360];
                if(ns.cscaleX.getUIValue())args.scaleX=[1,5];
                if(ns.cscaleY.getUIValue())args.scaleY=[1,5];
                if(ns.ctranslateX.getUIValue())args.translateX=[0,200];
                if(ns.ctranslateY.getUIValue())args.translateY=[0,200];
                if(ns.cskewX.getUIValue())args.skewX=[0,180];
                if(ns.cskewY.getUIValue())args.skewY=[0,180];

                if(!ood.isEmpty(args) && type){
                    var args2={}, target = ns.divDemo,
                        node=target.getRoot();
                    ood.each(args,function(o,i){
                        if(o[1])
                            args2[i]=[o[1],o[0]];
                        else
                            args2[i]=o;
                    });
                    node.animate(args,null,function(){
                        ood.asyRun(function(){
                            ns.divDemo.refresh();
                        },1000);
                    },300,0,type).start();
                }
            };
            fun();
        },
        events:{"onReady":"_com_onready", "onRender":"_com_onrender"},
        _com_onready:function (com, threadid){
            var ns=this,
                tg=ns.tg;

            var  rows = [];
            rows.push({
                "id":"predefined", 
                "caption":"$RAD.designer.effects.Predefined",
                "rowStyle":"font-weight:bold;text-align:center",
                "firstCellStyle":"font-weight:bold", 
                "type":"label",
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"type", 
                "caption":"$RAD.designer.effects.Type", 
                "type":"label",
                height:80,
                "rowStyle":"bold;text-align:center",
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"duration", 
                "caption":"$RAD.designer.effects.Duration(ms)", 
                "type":"spin", 
                numberTpl:'* ms',
                editorProperties:{
                    min:100,
                    max:5000,
                    increment:100,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"opacity", 
                "caption":"$RAD.designer.effects.Opacity", 
                "type":"spin", 
                "value":100,
                numberTpl:'* %',
                editorProperties:{
                    min:0,
                    max:100,
                    increment:10,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"rotate", 
                "caption":"$RAD.designer.effects.Rotate", 
                "type":"spin", 
                numberTpl:'* deg',
                editorProperties:{
                    min:0,
                    max:360,
                    increment:10,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"scaleX", 
                "caption":"$(RAD.custom_dlg.trans.Scale X)", 
                "type":"spin", 
                precision:2,
                increment:0.1,
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"scaleY", 
                "caption":"$(RAD.custom_dlg.trans.Scale Y)", 
                "type":"spin", 
                precision:2,
                increment:0.1,
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"translateX", 
                "caption":"$(RAD.custom_dlg.trans.Translate X)", 
                "type":"spin", 
                numberTpl:'* %',
                editorProperties:{
                    min:-500,
                    max:500,
                    increment:10,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"translateY", 
                "caption":"$(RAD.custom_dlg.trans.Translate Y)", 
                "type":"spin", 
                numberTpl:'* %',
                editorProperties:{
                    min:-500,
                    max:500,
                    increment:10,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"skewX", 
                "caption":"$(RAD.custom_dlg.trans.Skew X)", 
                "type":"spin", 
                numberTpl:'* deg',
                editorProperties:{
                    min:0,
                    max:360,
                    increment:5,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            rows.push({
                "id":"skewY", 
                "caption":"$(RAD.custom_dlg.trans.Skew Y)", 
                "type":"spin", 
                numberTpl:'* deg',
                editorProperties:{
                    min:0,
                    max:360,
                    increment:5,
                    precision:0
                },
                cells:[{
                },{
                }]
            });
            tg.setRows(rows);
            
            
            var items=[{
                    id:'None',
                    caption:"$(RAD.designer.effects.None)"
                }];
            ood.each(ood.Dom.$preDefinedEffects,function(o,i){
                items.push({
                    id:i,
                    caption:'$(RAD.designer.effects.'+i+")",
                    data:o
                });
            });
            ns.ctl_list.setItems(items);

            items=[{
                id:"none",
                name:"none",
                imgWidth:60,
                imgHeight:60,
                image:ood.ini.img_blank
            }];
            ood.each(ood.Dom.$AnimateEffects,function(o,id){
                items.push({
                    id:id,
                    name:id,
                    image:ood.getPath("/RAD/img/","animate/"+id+".gif")
                });
            });
            ns.gallery.setItems(items);
        },
        _ctl_preview_onclick:function (profile, e, src, value){
            var ns = this,
                tag=profile.properties.tag,
                actor = ns.dialog.getRoot(),
                arr= ns._collectEffects();

            if(tag=="show"){
                var effects = arr[0] || ood.Dom._getEffects(arr[2],1);
                if(effects){
                    actor.css('visibility','hidden');
                    ood.asyRun(function(){
                        actor.css('visibility','visible');
                        actor.animate(effects.endpoints, null,null, effects.duration, null, effects.type).start();
                    },800);
                }
            }

            if(tag=="hide"){
                var effects = arr[1] || ood.Dom._getEffects(arr[3],0);
                if(effects){
                    actor.animate(effects.endpoints, null, function(){
                        ood.asyRun(function(){
                            actor.css({transform:"",opacity:"1", visibility:'visible'});
                        },800);
                    }, effects.duration, null, effects.type).start();
                }
            }
        },
        _collectEffects:function(){
            var ns=this, tg=ns.tg, rows=tg.getRows("data"), 
                showEffects={
                    endpoints:{}
                }, hideEffects={
                    endpoints:{}
                },s2,h2;
            
            ood.arr.each(rows,function(row){
                var cells= row.cells,v1=cells[0].value,v2=cells[1].value;
                switch(row.id){
                    case "predefined":
                        if(v1!="None")s2=v1;
                        if(v2!="None")h2=v2;
                        break;
                    case "type":
                        if(v1)showEffects.type=v1;
                        if(v2)hideEffects.type=v2;
                        break;
                    case "duration":
                        if(v1)showEffects.duration=v1;
                        if(v2)hideEffects.duration=v2;
                        break;
                    case "opacity":
                        if(v1||v1===0)
                            showEffects.endpoints.opacity=[parseFloat(v1)/100,1];
                        if(v2||v2===0)
                            hideEffects.endpoints.opacity=[1,parseFloat(v2)/100];
                        break;
                    case "rotate":
                        if(v1||v1===0)
                            showEffects.endpoints.rotate=[parseFloat(v1),0];
                        if(v2||v2===0)
                            hideEffects.endpoints.rotate=[0,parseFloat(v2)];
                        break;
                    case "scaleX":
                        if(v1||v1===0)
                            showEffects.endpoints.scaleX=[parseFloat(v1),1];
                        if(v2||v2===0)
                            hideEffects.endpoints.scaleX=[1,parseFloat(v2)];
                        break;
                    case "scaleY":
                        if(v1||v1===0)
                            showEffects.endpoints.scaleY=[parseFloat(v1),1];
                        if(v2||v2===0)
                            hideEffects.endpoints.scaleY=[1,parseFloat(v2)];
                        break;
                    case "translateX":
                        if(v1||v1===0)
                            showEffects.endpoints.translateX=[parseFloat(v1)+"%","0%"];
                        if(v2||v2===0)
                            hideEffects.endpoints.translateX=["0%",parseFloat(v2)+"%"];
                        break;
                    case "translateY":
                        if(v1||v1===0)
                            showEffects.endpoints.translateY=[parseFloat(v1)+"%","0%"];
                        if(v2||v2===0)
                            hideEffects.endpoints.translateY=["0%",parseFloat(v2)+"%"];
                        break;
                    case "skewX":
                        if(v1||v1===0)
                            showEffects.endpoints.skewX=[parseFloat(v1),0];
                        if(v2||v2===0)
                            hideEffects.endpoints.skewX=[0,parseFloat(v2)];
                        break;
                    case "skewY":
                        if(v1||v1===0)
                            showEffects.endpoints.skewY=[parseFloat(v1),0];
                        if(v2||v2===0)
                            hideEffects.endpoints.skewY=[0,parseFloat(v2)];
                        break;

                }
            });
            
            if(!showEffects.type||!showEffects.duration||!showEffects.endpoints||ood.isEmpty(showEffects.endpoints))showEffects=null;
            if(!hideEffects.type||!hideEffects.duration||!hideEffects.endpoints||ood.isEmpty(hideEffects.endpoints))hideEffects=null;
            return [showEffects, hideEffects, s2, h2];
        },
        _ctl_dialog_beforeclose:function (profile){
            profile.boxing().hide();
            return false;
        },
        _resetEffects:function(effects, colId){
            var ns=this, 
                tg=ns.tg,
                index=colId=="show"?0:1,
                values={}, endpoints,code="None",
                fun=function(o){return o||o===0?o:"";};

            if(ood.isStr(effects)){
                var conf=ood.Dom.$preDefinedEffects[effects];
                if(conf){
                    code=effects;
                    effects=conf[index];
                }
            }
            tg.updateCellByRowCol("predefined",colId, {value:code, caption:"$(RAD.designer.effects."+code+")"},false,false);

            endpoints=effects && effects.endpoints;

            tg.updateCellByRowCol("type", colId, {value:effects&&effects.type||""},false,true);
            tg.updateCellByRowCol("duration", colId, {value:fun(effects&&effects.duration||"")});
            tg.updateCellByRowCol("opacity", colId, {value:fun(endpoints&&endpoints.opacity && parseInt(endpoints.opacity[index]*100,10))});
            tg.updateCellByRowCol("rotate", colId, {value:fun(endpoints&&endpoints.rotate&&endpoints.rotate[index])});
            tg.updateCellByRowCol("scaleX", colId, {value:fun(endpoints&&endpoints.scaleX&&endpoints.scaleX[index])});
            tg.updateCellByRowCol("scaleY", colId, {value:fun(endpoints&&endpoints.scaleY&&endpoints.scaleY[index])});
            tg.updateCellByRowCol("translateX", colId, {value:fun(endpoints&&endpoints.translateX&&endpoints.translateX[index])});
            tg.updateCellByRowCol("translateY", colId, {value:fun(endpoints&&endpoints.translateY&&endpoints.translateY[index])});
            tg.updateCellByRowCol("skewX", colId, {value:fun(endpoints&&endpoints.skewY&&endpoints.skewY[index])});
            tg.updateCellByRowCol("skewY", colId, {value:fun(endpoints&&endpoints.skewY&&endpoints.skewY[index])});
        },
        _tg_aftercellupdated:function (profile, cell, options, isHotRow){
            if(!('value' in options))return ;
            
            var ns = this, uictrl = profile.boxing(),
                tg= ns.tg,
                col = uictrl.getColByCell(cell),
                row = uictrl.getRowbyCell(cell);
            switch(row.id){
                case "predefined":
                    ns._resetEffects(options.value, col.id);
                    break;

                case "type":
                    tg.getSubNodeInGrid("CELL","type",col.id)
                    .css('background', (cell.value&&cell.value!='none')? ('url(' +ood.getPath('/RAD/img/animate/',cell.value+".gif")+') center center no-repeat '): 'transparent');
                    if(cell.value=='none'){
                        ns._resetEffects({}, col.id);
                    }
                    break;
                default:
                    tg.updateCellByRowCol("predefined",col.id, {value:"None", caption:"$(RAD.designer.effects.None)"},false,false);
            }
                    
        },
        _btncancel_onclick:function (profile, e, src, value){
            this.dialog.close();
        },
        _btnok_onclick:function (profile, e, src, value){
            var ns = this,
                prop=ns.properties,
                target= prop.targetProfile,
                arr= ns._collectEffects();

            target.boxing().setShowEffects(arr[2]||arr[0]||"").setHideEffects(arr[3]||arr[1]||"");
            ns.fireEvent("onDirty");
            ns.fireEvent('onFinished');
            
            this.dialog.close();
        },
        _ctl_gallery_onitemselected:function(profile, item){
            profile.boxing().hide();
            this.tg.updateCellByRowCol("type",profile.properties.tag, {value:item.id},false,true);
        },
        _ctl_list20_onitemselected:function (profile, item, e, src, type){
            profile.boxing().hide();
            this.tg.updateCellByRowCol("predefined",profile.properties.tag, {value:item.id, caption:item.caption},false,true);
        },
        _com_onrender:function (com, threadid){
            com.tg.getSubNodeInGrid("CELL","type","show").hoverPop(com.gallery,'12',function(){
                com.gallery.setUIValue(null,true);
                com.gallery.setTag('show');
            },null,null,"$asdfa");
            com.tg.getSubNodeInGrid("CELL","type","hide").hoverPop(com.gallery,'12',function(){
                com.gallery.setUIValue(null,true);
                com.gallery.setTag('hide');
            },null,null,"$asdfa");
            com.tg.getSubNodeInGrid("CELL","predefined","show").hoverPop(com.ctl_list,'12',function(){
                com.ctl_list.setUIValue(null,true);
                com.ctl_list.setTag('show');
            },null,null,"$asdfb");
            com.tg.getSubNodeInGrid("CELL","predefined","hide").hoverPop(com.ctl_list,'12',function(){
                com.ctl_list.setUIValue(null,true);
                com.ctl_list.setTag('hide');
            },null,null,"$asdfb");
        }
    }
});
