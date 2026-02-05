ood.Class('RAD.CustomDecoration', 'ood.Module',{
    Instance:{
        initialize : function(){
            this.autoDestroy = true;
            this.properties = {};
        },
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("13.125em")
                .setTop("1.25em")
                .setWidth("17.666666666666668em")
                .setHeight("40.416666666666664em")
                .setShadow(false)
                .setResizer(false)
                .setCaption("$(RAD.custom_dlg.Custom Decoration)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("visible")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Group")
                .setHost(host,"grpNodes")
                .setLeft("0em")
                .setTop("0.25em")
                .setWidth("16.916666666666668em")
                .setHeight("11.416666666666666em")
                .setCaption("grpNodes")
                );
            
            host.grpNodes.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"tv")
                .setDirtyMark(false)
                .onItemSelected("_tv_onitemselected")
                .setCustomStyle({
                    "BOX":"background-color:transparent;"
                }
                )
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block3")
                .setLeft("0em")
                .setTop("13.416666666666666em")
                .setWidth("16.916666666666668em")
                .setHeight("23.666666666666668em")
                .setBorderType("inset")
                .setBackground("#fff")
                );
            
            host.ctl_block3.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"tg")
                .setDirtyMark(false)
                .setEditMode("hover")
                .setRowHandler(false)
                .setColSortable(false)
                .setHeader([{
                    "id":"key",
                    "width":"7.5em",
                    "type":"label",
                    "caption":"$RAD.custom_dlg.Attribute"
                },
                {
                    "id":"value",
                    "editable":true,
                    "width":"11.166666666666666em",
                    "flexSize":true,
                    "type":"input",
                    "caption":"$RAD.custom_dlg.Value"
                }])
                .afterCellUpdated("_tg_aftercellupdated")
                .beforeComboPop("_tg_beforeComboPop")
                .setCustomStyle({
                    "SCROLL":{
                        "overflowX":"hidden",
                        "overflowY":"auto"
                    }
                }
                )
                );
            
            host.dialog.append(
                ood.create("ood.UI.Input")
                .setHost(host,"ctl_input1")
                .setReadonly(true)
                .setLeft("0em")
                .setTop("11.666666666666666em")
                .setWidth("16.916666666666668em")
                .setLabelCaption("ctl_input1")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
             var ns=this,
                prop=ns.properties;

            ns.setTargetProfile(prop.targetProfile);
                        
            ood("body").append(ns.dialog);
            ns.dialog.getRoot()
            .popToTop(prop.src, 2)
            .setBlurTrigger("RAD.CustomDecoration",function(){
                ns.dialog.close();
            });
            
            ns.dialog.activate();

            return true;
        },
        setTargetProfile:function(profile){
            var ns=this,items=[],rootItem;
            var fun=function(hash, subMap, arr, tag){
                var item;
                for(var i in hash){
                    if(i.charAt(0)!='$' && hash[i]){
                        var tagName=(hash[i]&&hash[i].tagName||"span").toLowerCase();
                        if(!/[a-z]/.test(i)){    
                            item={
                                id:i,
                                caption:i+" ["+tagName.toUpperCase()+"]",
                                imageClass:'ri-code-line'
                            };
                            
                            if(subMap && hash[i].text && subMap[(tag?(tag+"."):'')+hash[i].text.replace(/^{(.*)}$/,'$1')]){
                                item.sub=[];
                                var tag=(tag?(tag+"."):'')+hash[i].text.replace(/^{(.*)}$/,'$1');
                                
                                //special for TreeGrid
                                if(tag=='rows.cells')
                                    tag='rows.cells.input';
                                
                                arguments.callee(subMap[tag], subMap, item.sub, tag);
                                if(tagName=="text" && item.sub.length)
                                    item=item.sub[0];
                            }
                            else if(tagName=="text" && !hash[i].svg){
                                continue;
                            }
                            else if(typeof hash[i] == 'object' && tagName!="text"){
                                item.sub=[];
                                arguments.callee(hash[i], subMap, item.sub, tag);
                            }

                            if(!item.sub || !item.sub.length)
                                delete item.sub;
                            arr[arr.length]=item;
                        }
                    }
                };
            };
            var i="_",
                hash=profile.box.$Templates,
                tagName=(hash[i].tagName||"span").toLowerCase();
                
            items.push(rootItem={
                id:"KEY",
                caption:"ROOT" + " ["+tagName.toUpperCase()+"]",
                imageClass:'ri-code-line',
                sub:[]
            });
            
            var subMap = hash[i].$submap;
            if(subMap && hash[i].text && subMap[hash[i].text.replace(/^{(.*)}$/,'$1')]){
                var tag=hash[i].text.replace(/^{(.*)}$/,'$1');
                fun(subMap[tag], subMap, rootItem.sub, tag);
                if(tagName=="text" && rootItem.sub.length)
                    rootItem=rootItem.sub[0];
            }

            fun(hash[i], subMap, rootItem.sub, null);
            if(!rootItem.sub.length)
                delete rootItem.sub;

            ns.grpNodes.setCaption(profile.key.slice(profile.key.lastIndexOf('.')+1) +" : "+profile.alias +" $RAD.custom_dlg.Nodes");
            ns.tv.setTagVar(profile);
            ns.tv.setItems(items);

            ns.tg.updateCellByRowCol('classname','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('font-family','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('font-size','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('font-weight','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('font-style','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('color','value',{value:tagName=="image"?"#EBEADB":"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('background-color','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-image','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-position','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-repeat','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-attachment','value',{value:""},false,false);
/*
            ns.tg.updateCellByRowCol('background-size','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-origin','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('background-clip','value',{value:""},false,false);
*/
            ns.tg.updateCellByRowCol('border-left','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('border-right','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('border-top','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('border-bottom','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('border-radius','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('padding','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('margin','value',{value:""},false,false);

            ns.tg.updateCellByRowCol('text-decoration','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('text-align','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('text-shadow','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('cursor','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('overflow','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('line-height','value',{value:"",disabled:tagName=="image"},false,false);
            ns.tg.updateCellByRowCol('opacity','value',{value:1},false,false);
            ns.tg.updateCellByRowCol('transform','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('box-shadow','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('$gradient','value',{value:""},false,false);
            ns.tg.updateCellByRowCol('$zoom','value',{value:""},false,false);


            ns.tg.setDisabled(true);
            ns.ctl_input1.setValue("");

            ns.tv.toggleNode('KEY', true, true);
            ns.tv.fireItemClickEvent('KEY');            
        },
        _tv_onitemselected : function (profile, item, e) {
            var ns = this,
                prf=profile.boxing().getTagVar();

            ns.tg.setDisabled(false);
                        
            if(!ns.$coverMark){
                ns.$coverMark=ood.create('<div style="z-index:2000;border:solid 1px;background-color:#ff0000;position:absolute;font-size:0;line-height:0;display:none;">').css('opacity',0.3);
                ood('body').append(ns.$coverMark);
                ns.$coverMarkTask=ood.Thread.repeat(function(){
                    ns.$coverMarkTask.$_mark = !ns.$coverMarkTask.$_mark;
                    ns.$coverMark.css('backgroundColor',ns.$coverMarkTask.$_mark?'#0000ff':'#ff0000');
                    ns.$coverMark.css('border-color',ns.$coverMarkTask.$_mark?'#fff':'#000');
                }, 500);
            }
            if(item.id=='KEY'){
                ns.$coverMark.css('display','none');
                var node=prf.getRootNode();

                var nodeText = "<"+node.tagName+" id='"+node.id+"' class='"+node.className+"' style='"+ood(node).attr('style')+"'>";
                ns.ctl_input1.setValue(nodeText).setTips(nodeText.replace(/</,'&lt;'));
            }else{
                var nodes=prf.getSubNode(item.id,true);
                if(nodes.get(0)){
                    var set=false;
                    nodes.each(function(node){
                        if(node.offsetWidth){
                            var region=ood(node).offset();
                            region.width=node.offsetWidth;
                            region.height=node.offsetHeight;

                            if(region.width<=0)region.width=2;else region.width-=2;
                            if(region.height<=0)region.height=2;else region.height-=2;
                            if(region.width<=0)region.width=2;
                            if(region.height<=0)region.height=2;
                            
                            ns.$coverMark.cssRegion(region).css('display','block');

                            var nodeText = "<"+node.tagName+" id='"+node.id+"' class='"+node.className+"' style='"+ood(node).attr('style')+"'>";
                            ns.ctl_input1.setValue(nodeText).setTips(nodeText.replace(/</,'&lt;'));
                            
                            set=true;
                            return false;
                        }       
                    });
                    if(!set)
                        ns.$coverMark.css('display','none');
                }else{
                    ns.$coverMark.css('display','none');
                }
            }
            
            ns.tg.updateCellByRowCol('classname','value',{value:prf.CC[item.id]||null},false,false);
            
            var cs=prf.CS[item.id],
                csaMap={}, t;
            if(cs){
                if(ood.isStr(cs)){
                    var csa=cs.split(/\s*;+\s*/);
                    ood.arr.each(csa,function(style){
                        if(style){
                            var kv=style.split(/\s*:\s*/);
                            if(kv.length==2){
                                csaMap[kv[0]]=kv[1];
                            }
                        }
                    });
                }else{
                    csaMap=cs;
                }
            }

            ns.tg.updateCellByRowCol('font-family','value',{
                value:csaMap['font-family']||""
            },false,false);
            ns.tg.updateCellByRowCol('font-size','value',{
                value:csaMap['font-size']||""
            },false,false);
            ns.tg.updateCellByRowCol('font-weight','value',{
                value:csaMap['font-weight']||""
            },false,false);
            ns.tg.updateCellByRowCol('font-style','value',{
                value:csaMap['font-style']||""
            },false,false);
            ns.tg.updateCellByRowCol('color','value',{
                value:csaMap['color']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-color','value',{
                value:csaMap['background-color']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-image','value',{
                value:csaMap['background-image']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-position','value',{
                value:csaMap['background-position']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-repeat','value',{
                value:csaMap['background-repeat']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-attachment','value',{
                value:csaMap['background-attachment']||""
            },false,false);
 /*
            ns.tg.updateCellByRowCol('background-size','value',{
                value:csaMap['background-size']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-origin','value',{
                value:csaMap['background-origin']||""
            },false,false);
            ns.tg.updateCellByRowCol('background-clip','value',{
                value:csaMap['background-clip']||""
            },false,false);
*/
            ns.tg.updateCellByRowCol('border-top','value',{
                value:(t=csaMap['border-top'])?t:(t=csaMap['border'])?t:''
            },false,false);
            ns.tg.updateCellByRowCol('border-right','value',{
                value:(t=csaMap['border-right'])?t:(t=csaMap['border'])?t:''
            },false,false);
            ns.tg.updateCellByRowCol('border-bottom','value',{
                value:(t=csaMap['border-bottom'])?t:(t=csaMap['border'])?t:''
            },false,false);
            ns.tg.updateCellByRowCol('border-left','value',{
                value: (t=csaMap['border-left'])?t:(t=csaMap['border'])?t:''
            },false,false);

            ns.tg.updateCellByRowCol('border-radius','value',{
                value:csaMap['border-radius']||""
            },false,false);
            ns.tg.updateCellByRowCol('padding','value',{
                value:(t=csaMap['padding'])?t:((t=(csaMap['padding-top']||'0') + ' ' +(csaMap['padding-right']||'0') + ' ' +(csaMap['padding-bottom']||'0') + ' ' +(csaMap['padding-left']||'0') )!= '0 0 0 0')?t:""
            },false,false);
            ns.tg.updateCellByRowCol('margin','value',{
                value:(t=csaMap['margin'])?t:((t=(csaMap['margin-top']||'0') + ' ' +(csaMap['margin-right']||'0') + ' ' +(csaMap['margin-bottom']||'0') + ' ' +(csaMap['margin-left']||'0') )!= '0 0 0 0')?t:""
            },false,false);
            ns.tg.updateCellByRowCol('text-align','value',{
                value:csaMap['text-align']||""
            },false,false);
            ns.tg.updateCellByRowCol('text-decoration','value',{
                value:csaMap['text-decoration']||""
            },false,false);
            ns.tg.updateCellByRowCol('text-shadow','value',{
                value:csaMap['text-shadow']||""
            },false,false);
            ns.tg.updateCellByRowCol('box-shadow','value',{
                value:csaMap['box-shadow']||""
            },false,false);
            ns.tg.updateCellByRowCol('$gradient','value',{
                value:csaMap['$gradient']||""
            },false,false);
            ns.tg.updateCellByRowCol('$zoom','value',{
                value:csaMap['$zoom']||""
            },false,false);
            ns.tg.updateCellByRowCol('cursor','value',{
                value:csaMap['cursor']||""
            },false,false);
            ns.tg.updateCellByRowCol('overflow','value',{
                value:csaMap['overflow']||""
            },false,false);
            ns.tg.updateCellByRowCol('line-height','value',{
                value:csaMap['line-height']||""
            },false,false);
            ns.tg.updateCellByRowCol('opacity','value',{
                value:(parseFloat(csaMap['opacity'])||parseFloat(csaMap['opacity'])===0)?csaMap['opacity']:1
            },false,false);
            ns.tg.updateCellByRowCol('transform','value',{
                value:csaMap['transform']||""
            },false,false);
             
            profile.boxing().setTag(item.id);
        },
        events : {"beforeShow":"_com_beforeShow", "onDestroy":"_com_ondestroy"},
        _com_beforeShow : function (com, threadid) {
            var ns=this,rows=[],
                  target=ns.properties.targetProfile.boxing(),
                  bcell=function(cell, key){
                    // Give a chance to modify editor setting for value cell
                    window.SPA && SPA.fe("onInitPropValueCell", ['CS:'+ns.tv.getTag(),key, cell.value, "RAD.CustomDecoration", target, cell, ns.tg]);
                    return cell;
                };
            rows.push({id:'classname',cellStyle:"font-weight:bold;",cells:[{id:'class',caption:"$RAD.custom_dlg.classname"},bcell({value:""},"classname")]});

            rows.push({id:'color',cells:[{id:'color',caption:"$RAD.custom_dlg.color"},bcell({
                type:"color"
            },"color")]});
            rows.push({id:'background-color',cells:[{id:'background-color',caption:"$(RAD.custom_dlg.background$-color)"},bcell({
                type:"color"
            },'background-color')]});
            rows.push({id:'background-image',cells:[{id:'background-image',caption:"$(RAD.custom_dlg.background$-image)"},bcell({
                type:CONF.getClientMode() == 'singlepage'?"input":"popbox",
                event : function(profile, cell,editorprf){
                    var node = profile.getSubNode('CELL', cell._serialId),
                        _cb=function(path){
                            editorprf.boxing().setUIValue("url("+CONF.adjustRelativePath(path)+")");
                            node.focus();
                        };
                    if(window.SPA && false===SPA.fe("beforeURISelectorPop", 
                    ['CS:'+ns.tv.getTag()+':background-image', cell.value, _cb,
                        'RAD.CustomDecoration',target,node.get(0), cell, profile.boxing(), editorprf&&editorprf.boxing()
                    ]))return;

                    if(CONF.getClientMode() !='singlepage'){
                        ood.ComFactory.getCom('RAD.ImageSelector',function(){
                            this.setProperties({
                                    fromRegion:node.cssRegion(true),
                                    onOK:function(obj, path){
                                        _cb(path);
                                    }
                            });
                            this.show();
                        });
                    }
                }
            },'background-image')]});
            rows.push({id:'background-position',cells:[{id:'background-position',caption:"$(RAD.custom_dlg.background$-position)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_position
            },'background-position')]});
            rows.push({id:'background-repeat',cells:[{id:'background-repeat',caption:"$(RAD.custom_dlg.background$-repeat)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_repeat
            },'background-repeat')]});
            rows.push({id:'background-attachment',cells:[{id:'background-attachment',caption:"$(RAD.custom_dlg.background$-attachment)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_attachment
            },'background-attachment')]});
/*
            rows.push({id:'background-size',cells:[{id:'background-size',caption:"$(RAD.custom_dlg.background$-size)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_size
            },'background-size')]});
           rows.push({id:'background-origin',cells:[{id:'background-origin',caption:"$(RAD.custom_dlg.background$-origin)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_origin
            },'background-origin')]});
            rows.push({id:'background-clip',cells:[{id:'background-clip',caption:"$(RAD.custom_dlg.background$-clip)"},bcell({
               type:"combobox", editorListItems:CONF.designer_background_clip
            },'background-clip')]});
*/
            rows.push({id:'font-family',cells:[{id:'font-family',caption:"$(RAD.custom_dlg.font$-family)"},bcell({
                type:"helpinput", editorListItems:CONF.designer_data_fontfamily
            },'font-family')]});
            rows.push({id:'font-size',cells:[{id:'font-size',caption:"$(RAD.custom_dlg.font$-size)"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_fontsize
            },'font-size')]});
            rows.push({id:'font-weight',cells:[{id:'font-weight',caption:"$(RAD.custom_dlg.font$-weight)"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_fontweight
            },'font-weight')]});
            rows.push({id:'font-style',cells:[{id:'font-style',caption:"$(RAD.custom_dlg.font$-style)"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_fontstyle
            },'font-style')]});
            
            rows.push({id:'border-top',cells:[{id:'border-top',caption:"$(RAD.custom_dlg.border$-top)"},bcell({
                type:"popbox"
            },'border-top')]});
            rows.push({id:'border-right',cells:[{id:'border-right',caption:"$(RAD.custom_dlg.border$-right)"},bcell({
                type:"popbox"
            },'border-right')]});
            rows.push({id:'border-bottom',cells:[{id:'border-bottom',caption:"$(RAD.custom_dlg.border$-bottom)"},bcell({
                type:"popbox"
            },'border-bottom')]});
            rows.push({id:'border-left',cells:[{id:'border-left',caption:"$(RAD.custom_dlg.border$-left)"},bcell({
                type:"popbox"
            },'border-left')]});
            if(ood.Dom.css3Support("border-radius")){
                rows.push({id:'border-radius',cells:[{id:'border-radius',caption:"$(RAD.custom_dlg.border$-radius)"},bcell({
                    type:"popbox"
                },'border-radius')]});
            }
            rows.push({id:'padding',cells:[{id:'padding',caption:"$RAD.custom_dlg.padding"},bcell({
                type:"popbox"
            },'padding')]});
            rows.push({id:'margin',cells:[{id:'margin',caption:"$RAD.custom_dlg.margin"},bcell({
                type:"popbox"
            },'margin')]});
            rows.push({id:'text-align',cells:[{id:'text-align',caption:"$(RAD.custom_dlg.text$-align)"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_textalign
            },'text-align')]});
            rows.push({id:'text-decoration',cells:[{id:'text-decoration',caption:"$(RAD.custom_dlg.text$-decoration)"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_textdecoration
            },'text-decoration')]});
            if(ood.Dom.css3Support("text-shadow")){
                rows.push({id:'text-shadow',cells:[{id:'text-shadow',caption:"$(RAD.custom_dlg.text$-shadow)"},bcell({
                    type:"popbox"
                },'text-shadow')]});
            }
            if(ood.Dom.css3Support("box-shadow")){
                rows.push({id:'box-shadow',cells:[{id:'box-shadow',caption:"$(RAD.custom_dlg.box$-shadow)"},bcell({
                    type:"popbox"
                },'box-shadow')]});
            }
            if(ood.Dom.css3Support("gradient")){
                rows.push({id:'$gradient',cells:[{id:'gradients',caption:"$RAD.custom_dlg.gradients"},bcell({
                    type:"popbox", editorReadonly:true
                },'gradient')]});
            }
            if(ood.Dom.css3Support("transform")){
                rows.push({id:'transform',cells:[{id:'transform',caption:"$RAD.custom_dlg.transform"},bcell({
                    type:"popbox"
                },'transform')]});
            }
            rows.push({id:'$zoom',cells:[{id:'zoom',caption:"$RAD.custom_dlg.zoom"},bcell({
                type:"spin", min:0.1,max:10,increment:0.1,precision:1
            },'zoom')]});
            rows.push({id:'cursor',cells:[{id:'cursor',caption:"$RAD.custom_dlg.cursor"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_cursor
            },'cursor')]});
            rows.push({id:'overflow',cells:[{id:'overflow',caption:"$RAD.custom_dlg.overflow"},bcell({
                type:"combobox", editorListItems:CONF.designer_data_overflow
            },'overflow')]});
            rows.push({id:'line-height',cells:[{id:'line-height',caption:"$(RAD.custom_dlg.line$-height)"},bcell({
                type:"combobox", editorListItems:[1,1.22,1.5,2]
            },'lien-height')]});
            if(ood.Dom.css3Support("opacity")){
                rows.push({id:'opacity',cells:[{id:'opacity',caption:"$RAD.custom_dlg.opacity"},bcell({
                    type:"progress", editorProperties:{
                        min:0,max:1
                    }
                },'opacity')]});
            }
            // Give a chance to modify editor setting for rows
            window.SPA && SPA.fe("onInitEditorRows", ['CS', null, ns.properties.targetProfile.CS, 
                'RAD.CustomDecoration', ns.properties.targetProfile,  rows, ns.tg]);

            ns.tg.setRows(rows);
        },
        _ctl_sbutton1_onclick : function (profile) {
            this.setTargetProfile(this.ctl_panel3.get(0));
        },
        _ctl_sbutton37_onclick : function (profile, e, src, value) {
            this.setTargetProfile(this.ctl_tabs2.get(0));
        },
        _com_ondestroy : function (com) {
            if(this.$coverMark)
                this.$coverMark.remove();
            if(this.$coverMarkTask)
                this.$coverMarkTask.abort();
        },
        _dialog_beforeclose : function (profile) {
            this.dialog.getRoot().setBlurTrigger("RAD.CustomDecoration");
            profile.boxing().hide();
            this.$coverMark.css('display','none');
            this.tv.setValue(null,true);
            this.fireEvent('onFinished');
            return false;
        },
        _tg_beforeComboPop:function(profile, cell, editorprf, pos, e, src){
            var ns=this,
                prf=ns.tv.getTagVar(),
                tplKey=ns.tv.getTag(),
                cls;
 
            if(window.SPA && false===SPA.fe("beforeEditorComboPop", [
                'CS:'+tplKey, cell._row.id, cell.value, function(value){
                    editorprf.boxing().setUIValue(value||"",true);
                }, "RAD.CustomDecoration", ns.properties.targetProfile.boxing(), pos, profile.getSubNode('CELL', cell._serialId).get(0), cell, ns.tg ,editorprf&&editorprf.boxing()
                ] ))return false;

            switch(cell._row.id){
                case "border-top":
                case "border-right":
                case "border-bottom":
                case "border-left":
                case "border-radius":
                case "padding":
                case "margin":
                    cls="RAD.CustomBorder";
                    break;
                case "text-shadow":
                    cls="RAD.CustomTextShadow";
                    break;
                case "box-shadow":
                    cls="RAD.CustomBoxShadow";
                    break;
                case "transform":
                    cls="RAD.CustomTransform";
                    break;
                case "$gradient":
                    cls="RAD.CustomGradients";
                    break;
            }
            if(cls){
                ood.ComFactory.getCom(cls,function(){
                    this.init(prf,tplKey,{
                        pro:editorprf,
                        cell:cell,
                        isSvg:0,
                        grid:ns.tg
                    });
                    this.setEvents({
                        onChange:function(str){
                            switch(cell._row.id){
                                case "border-top":
                                    editorprf.boxing().setUIValue(str['border']['top']||"",true);
                                    break;
                                case "border-right":
                                    editorprf.boxing().setUIValue(str['border']['right']||"",true);
                                    break;
                                case "border-bottom":
                                    editorprf.boxing().setUIValue(str['border']['bottom']||"",true);
                                    break;
                                case "border-left":
                                    editorprf.boxing().setUIValue(str['border']['left']||"",true);
                                    break;
                                case "border-radius":
                                case "padding":
                                case "margin":
                                    editorprf.boxing().setUIValue(str[cell._row.id]||"",true);
                                    break;
                                default:
                                    editorprf.boxing().setUIValue(str?ood.isStr(str)?str:"[object]":"",true);
                            }
                            switch(cell._row.id){
                                case "border-top":
                                case "border-right":
                                case "border-bottom":
                                case "border-left":
                                case "border-radius":
                                case "padding":
                                case "margin":
                                    profile.boxing().updateCellByRowCol('padding','value',{value:str['padding']||""},false,true);
                                    profile.boxing().updateCellByRowCol('margin','value',{value:str['margin']||""},false,true);
                                    profile.boxing().updateCellByRowCol('border-radius','value',{value:str['border-radius']||""},false,true);
                                    profile.boxing().updateCellByRowCol('border-left','value',{value:str['border']['left']||""},false,true);
                                    profile.boxing().updateCellByRowCol('border-right','value',{value:str['border']['right']||""},false,true);
                                    profile.boxing().updateCellByRowCol('border-top','value',{value:str['border']['top']||""},false,true);
                                    profile.boxing().updateCellByRowCol('border-bottom','value',{value:str['border']['bottom']||""},false,true);
                                    break;
                                default:
                                    profile.boxing().updateCell(cell,{value:str||""},false,true);
                                    if(ood.isHash(str)){
                                        profile.boxing().updateCell(cell,{caption:"[object]"},false,true);
                                    }
                                    if(ood.isNull(str)){
                                            profile.boxing().updateCell(cell,{caption:""},false,true);
                                    }
                            }
                        }
                    });
                    this.render();
                    var r=this.mainPane.getRoot();
                    r.popToTop(editorprf.getRoot());
                    r.setBlurTrigger(r.$xid, function(){
                        r.setBlurTrigger(r.$xid);
                        r.hide();
                    });
                    editorprf.boxing().setPopWnd(this);
                });
            }
        },
        _tg_aftercellupdated : function (profile, cell, options) {
            if("value" in options){
                var ns=this,
                    grid=ns.tg,
                    prf=ns.tv.getTagVar(),
                    tplKey=ns.tv.getTag();
                if(prf && tplKey){
                    var className="",
                        hash={},
                        cells=grid.getCells(null,'value');
                    
                    ood.each(cells,function(cell){
                        if(cell._row.id=="classname"){
                            if(ood.str.trim(cell.value)!=="")
                                className=ood.str.trim(cell.value);
                        }else{
                            var v="",rid=cell._row.id;
                            if(rid=="$gradient"){
                                hash[rid]=cell.value;
                                if(!cell.value)delete hash[rid];
                            }else if(rid=="$zoom"){
                                if(parseFloat(cell.value)!==1.0){
                                    v=parseFloat(cell.value);
                                    hash[rid]=v;
                                    if(v==1)delete hash[rid];
                                }
                            }else if(rid=="opacity"){
                                if(parseFloat(cell.value)!==1.0){
                                    v=parseFloat(cell.value);
                                    hash[rid]=v;
                                    if(v==1)delete hash[rid];
                                }
                            }else if(ood.isStr(cell.value) && ood.str.trim(cell.value)!==""){
                                v=ood.str.trim(cell.value);
                                hash[rid]=v;
                            }
                        }
                    });
                    // custom class
                    prf.boxing().setCustomClass(tplKey, className||null);
                    // custom style
                    prf.boxing().setCustomStyle(tplKey, ood.isEmpty(hash)?null:hash);
                    
                    // try to resize it
                   //ood.UI.$tryResize(prf, prf.properties.width, prf.properties.height,true);
                    // adjust  
                    prf.boxing().reLayout();

                    // try to pos+size resizer
                    ns.properties.page.resizer.rePosSize();
                    
                    ns.fireEvent('onDirty');
                }
            }
        }
    }
});
