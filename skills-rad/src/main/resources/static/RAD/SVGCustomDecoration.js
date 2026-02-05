ood.Class('RAD.SVGCustomDecoration', 'ood.Module',{
    Instance:{
        initialize : function(){
            this.autoDestroy = true;
            this.properties = {};
        },
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("13.125em")
                .setTop("1.25em")
                .setWidth("21.75em")
                .setHeight("36.25em")
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
                .setWidth("20.916666666666668em")
                .setHeight("10.5em")
                .setCaption("grpNodes")
                .setToggleBtn(false)
                );
            
            host.grpNodes.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"tv")
                .setDirtyMark(false)
                .onItemSelected("_tv_onitemselected")
                .setCustomStyle({
                    "BOX":"background-color:transparent;"
                })
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block3")
                .setLeft("0em")
                .setTop("12.75em")
                .setWidth("20.916666666666668em")
                .setHeight("20em")
                .setBorderType("inset")
                .setBackground("#fff")
                );
            
            host.ctl_block3.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"tg")
                .setDirtyMark(false)
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
                    "type":"input",
                    "caption":"$RAD.custom_dlg.Value"
                }])
                .afterCellUpdated("_tg_aftercellupdated")
                .beforeComboPop("_tg_beforeComboPop")
                .setCustomStyle({
                    "SCROLL":"overflow-x:hidden;overflow-y:auto;"
                })
                );
            
            host.dialog.append(
                ood.create("ood.UI.Input")
                .setHost(host,"ctl_input1")
                .setReadonly(true)
                .setLeft("0em")
                .setTop("10.833333333333334em")
                .setWidth("20.916666666666668em")
                .setHeight("1.8333333333333333em")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
             var ns=this,
                prop=ns.properties;

            ns.setTargetProfile(prop.targetProfile);
                        
            ood("body").append(ns.dialog);
            ns.dialog.getRoot()
            .popToTop(ood(prop.src), 2)
            .setBlurTrigger("RAD.SVGCustomDecoration",function(){
                ns.dialog.close();
            });
            
            ns.dialog.activate();

            return true;
        },
        setTargetProfile:function(profile){
            var ns=this,items=[],rootItem,
                tagName,hash,item;
            ns.$target = profile;

            tagName =  profile.box.$tagName.KEY;
            items.push(rootItem={
                id:"KEY",
                caption:"ROOT" + " ["+tagName+"]",
                tagName:tagName,
                imageClass:'ri ri-git-branch-line',
                sub:[]
            });
            
            hash=profile.properties.attr;
            if(hash.KEY&&ood.isHash(hash.KEY)){
                for(var i in hash){
                    if(i!=="KEY"){
                        tagName = profile.box.$tagName[i]|| i.split("_")[0].toLowerCase();
                        item={
                            id:i,
                            caption:i+" ["+tagName+"]",
                            tagName:tagName,
                            imageClass:'ri ri-git-branch-line'
                        };
                        rootItem.sub.push(item);
                    }
                }
            }
            if(!rootItem.sub.length)
                delete rootItem.sub;

            ns.grpNodes.setCaption(profile.key.slice(profile.key.lastIndexOf('.')+1) +" : "+profile.alias +" $RAD.custom_dlg.Nodes");
            ns.tv.setTagVar(profile);
            ns.tv.setItems(items);

            ns.tg.setDisabled(true);
            ns.ctl_input1.setValue("");

            ns.tv.toggleNode('KEY', true, true);
            ns.tv.fireItemClickEvent('KEY');            
        },
        _tv_onitemselected : function (profile, item, e) {
            var ns = this,
                ignorelist={href:1,target:1,'letter-spacing':1, blur:1, font:1/*, cx:1, cy:1, x:1, y:1, width:1, height:1*/},
                prf=profile.boxing().getTagVar(),
                rows=[],
                getDftValue=function(tagName, key){
                    if(tagName=='text'){
                        if(key=='stroke') return "none";
                        if(key=='fill') return "#000";
                    }
                    return Raphael._availableAttrs[key];
                };
            
            ns.tg.removeAllRows().setDisabled(false);
                        
            var node=prf.getSubNode(item.id), attr=prf.boxing().getAttr(item.id);

            if(node=node.get(0)){
                var nodeText = "<"+node.tagName+" id='"+node.id+"' style='"+ood(node).attr('style')+"'>";
                ns.ctl_input1.setValue(nodeText).setTips(nodeText.replace(/</,'&lt;'));                            
            }else
                ns.ctl_input1.setValue("").setTips("");

            ood.each(ood.svg.$attr[item.tagName],function(v,k){
                if(ignorelist[k])
                    return;
                switch(item.tagName){
                case "text":
                    if( k=='arrow-start' 
                        || k=='arrow-end' 
                        || k=="stroke-dasharray"
                        || k=="stroke-linecap"
                        || k=="stroke-linejoin"
                        || k=="stroke-miterlimit"
                        || k=="stroke-opacity"
                        || k=="stroke-width")
                        return;
                    if( prf.box["ood.svg.absComb"] &&  (k=='transform'||k=='text-anchor') )
                        return;
                    break;
                case "rect":
                case "circle":
                case "ellipse":
                case "image":
                    if( k=='arrow-start' 
                        || k=='arrow-end')
                        return;
                break;
                }                
                
                var row=null,
                    dftV = (k in attr)?attr[k]:getDftValue(item.tagName,k);
                switch(k){
                    case "arrow-end":
                        row={id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.arrow$-end)"},{
                            type:"popbox", value: dftV || ""
                        }]};
                    break;
                    case "arrow-start":
                        row={id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.arrow$-start)" },{
                            type:"popbox" , value: dftV || ""
                        }]};
                    break;
                    case "fill":
// add gradient after fill
                        row={id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"popbox", value: dftV||""
                        }]};
                    break;
                    case "stroke":
                        row={id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"color", value: dftV||""
                        }]};
                    break;
                    case "font-family":
                        row={id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-")+")" },{
                            type:"helpinput", editorListItems:CONF.designer_data_fontfamily, value: dftV||""
                        }]};
                    break;
                    case "font-size":
                        row={id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-")+")" },{
                            type:"combobox", editorListItems:CONF.designer_data_fontsize, value: dftV||""
                        }]};
                    break;
                    case "font-style":
                        row={id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-")+")" },{
                            type:"combobox", editorListItems:CONF.designer_data_fontstyle, value: dftV||""
                        }]};
                    break;
                    case "font-weight":
                        row = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-") +")" },{
                            type:"combobox", editorListItems:CONF.designer_data_fontweight, value: dftV||""
                        }]};
                    break;
                    case "opacity":
                    case "stroke-opacity":
                    case "fill-opacity":
                        row = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-")+")" },{
                            type:"progress", editorProperties:{
                                min:0,max:1
                            },
                            value : (parseFloat(dftV)||parseFloat(dftV)===0)?dftV:1
                        }]};
                    break;
                    case "src":
                        row  = {id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            value: dftV||"",
                            type:CONF.getClientMode() == 'singlepage'?"input":"popbox",
                            event :  function(profile, cell,editorprf){
                                var node = profile.getSubNode('CELL', cell._serialId),
                                    _cb = function(path){
                                        editorprf.boxing().setUIValue(CONF.adjustRelativePath(path));
                                        node.focus();
                                    };
                                if(window.SPA && false===SPA.fe("beforeURISelectorPop", 
                                ['src', cell.value, _cb,
                                    'RAD.SVGCustomDecoration',ns.$target.boxing(),node.get(0), cell, profile.boxing(), editorprf&&editorprf.boxing()
                                ] ))return;
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
                    }]};
                    break;
                    break;
                    case "path":
                    case "text":
                        row  = {id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"textarea", value: dftV||""
                        }]};
                    break;

                    case "cx":
                    case "cy":
                    case "x":
                    case "y":
                    case "width":
                    case "height":
                    case "r":
                    case "rx":
                    case "ry":
                    case "stroke-width":
                        row  = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg."+k.replace(/-/g,"$-")+")"},{
                            type:"spin", increment:1, precision:0, min:0, value: dftV||0
                        }]};
                     break;
                    case "stroke-miterlimit":
                        row  = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.stroke$-miterlimit)"},{
                            type:"spin", increment:1, precision:0, min:0, value: dftV||0
                        }]};
                    break;
                    case "stroke-dasharray":
                        row = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.stroke$-dasharray)"},{
                        type:"combobox", editorListItems:CONF.designer_data_strokedasharray, value: dftV||""
                        }]};
                    break;
                    case "stroke-linecap":
                        row = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.stroke$-linecap)"},{
                            type:"listbox", editorListItems:CONF.designer_data_strokelinecap, value: dftV||""
                        }]};
                    break;
                    case "stroke-linejoin":
                        row = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.stroke$-linejoin)" },{
                            type:"listbox", editorListItems:CONF.designer_data_strokelinejoin, value: dftV||""
                        }]};
                    break;
                    case "text-anchor":
                        row  = {id:k,cells:[{id:k,caption:"$(RAD.custom_dlg.text$-anchor)"},{
                            type:"listbox", editorListItems:CONF.designer_data_textalign
                        }]};
                    break;
                    case "transform":
                        row={id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"popbox", value: dftV||""
                        }]};
                    break;
                    case "cursor":
                        row={id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"combobox", editorListItems:CONF.designer_data_cursor, value: dftV || ""
                        }]};
                    break;
                    case "title":
                        row  = {id:k,cells:[{id:k,caption:"$RAD.custom_dlg."+k },{
                            type:"input", value: dftV||""
                        }]};
                    break;
                }
                if(row){
                    rows.push(row);
                }
            });

            // Give a chance to modify editor setting for rows
            window.SPA && SPA.fe("onInitEditorRows", ['attr', item.tagName, ns.$target.properties.attr, 
                'RAD.SVGCustomDecoration', ns.$target,  rows, ns.tg]);

            if(window.SPA && SPA.events.onInitPropValueCell){
                ood.arr.each(rows,function(row){
                    ood.arr.each(row.cells,function(cell,i){
                         // Give a chance to modify editor setting for value cell
                        window.SPA && SPA.fe("onInitPropValueCell", ['attr', item.tagName, cell.value, "RAD.SVGCustomDecoration", ns.$target, cell, ns.tg] );
                    });
                });
            }
            ns.tg.setRows(rows).setTag(item.tagName);
 
            profile.boxing().setTag(item.id);
        },
        _ctl_sbutton1_onclick : function (profile) {
            this.setTargetProfile(this.ctl_panel3.get(0));
        },
        _ctl_sbutton37_onclick : function (profile, e, src, value) {
            this.setTargetProfile(this.ctl_tabs2.get(0));
        },
        _dialog_beforeclose : function (profile) {
            this.dialog.getRoot().setBlurTrigger("RAD.SVGCustomDecoration");
            profile.boxing().hide();
            this.tv.setValue(null,true);
            this.fireEvent('onFinished');
            return false;
        },
        _tg_beforeComboPop:function(profile, cell, pro, pos, e, src){
            var ns=this,
                prf=ns.tv.getTagVar(),
                tplKey=ns.tv.getTag(),
                cls,onChange;
            
            if(window.SPA && false===SPA.fe("beforeEditorComboPop", [
                'attr:'+tplKey,cell._row.id, cell.value, function(value){
                    editorprf.boxing().setUIValue(value||"",true);
                }, "RAD.SVGCustomDecoration", ns.properties.targetProfile.boxing(), pos, profile.getSubNode('CELL', cell._serialId).get(0), cell, ns.tg ,pro&&pro.boxing()
                ] ))return false;

            switch(cell._row.id){
                case "arrow-start":
                    cls="RAD.CustomArrows";
                    onChange = function(str){
                        var pro=this.properties.pro,
                            grid=ns.tg,
                            start=str[0]&&str[0].split('-'),
                            end=str[1]&&str[1].split('-'),
                            cell;

                        if(start[0]=='none')str[0]="none";
                        cell=grid.getCellbyRowCol("arrow-start","value");
                        if(cell)grid.updateCell(cell,{value:str[0]},false,true);

                        if(end[0]=='none')str[1]="none";
                        cell=grid.getCellbyRowCol("arrow-end","value");
                        if(cell)grid.updateCell(cell,{value:str[1]},false,true);
                        
                        if(!pro.destroyed)pro.boxing().setValue(str[0],true);
                    }
                    break;                    
                case "arrow-end":
                    cls="RAD.CustomArrows";
                    onChange = function(str){
                        var pro=this.properties.pro,
                            grid=ns.tg,
                            start=str[0]&&str[0].split('-'),
                            end=str[1]&&str[1].split('-'),
                            cell;
                        
                        if(start[0]=='none')str[0]="none";
                        cell=grid.getCellbyRowCol("arrow-start","value");
                        if(cell)grid.updateCell(cell,{value:str[0]},false,true);

                        if(end[0]=='none')str[1]="none";
                        cell=grid.getCellbyRowCol("arrow-end","value");
                        if(cell)grid.updateCell(cell,{value:str[1]},false,true);

                        if(!pro.destroyed)pro.boxing().setValue(str[1],true);
                    }
                    break;
                case "transform":
                    cls="RAD.CustomTransform";
                    onChange = function(str){
                        var pro=this.properties.pro,
                            cell=this.properties.cell;

                        if(!pro.destroyed)pro.boxing().setValue(ood.isStr(str)?str:"[object]",true);
                        profile.boxing().updateCell(cell,{value:str},false,true);
                    }
                    break;
                case "fill":
                    cls="RAD.SVGCustomGradients";
                    onChange = function(str){
                        var pro=this.properties.pro,
                            cell=this.properties.cell;

                        if(!pro.destroyed)pro.boxing().setValue(ood.isStr(str)?str:"[object]",true);
                        profile.boxing().updateCell(cell,{value:str},false,true);
                    }
                    break;
            }
            if(cls){
                ood.ComFactory.getCom(cls,function(){
                    this.init(prf,tplKey,{
                        pro:pro,
                        cell:cell,
                        grid:ns.tg,
                        isSvg:1
                    });
                    this.setEvents({
                        onChange:onChange
                    });
                    this.render();
                    var r=this.mainPane.getRoot();
                    r.popToTop(pro.getRoot());
                    r.setBlurTrigger(r.$xid, function(){
                        r.setBlurTrigger(r.$xid);
                        r.hide();
                    });
                });
            }
        },
        _tg_aftercellupdated : function (profile, cell, options) {
            var map={"path":1,"text":1,"cx":1,"cy":1,"x":1,"y":1,"width":1,"height":1,"r":1,"rx":1,"ry":1,"stroke-width":1};
            if("value" in options){
                var ns=this,
                    prf=ns.tv.getTagVar(),
                    hash={},
                    tplKey=ns.tv.getTag();
                if(prf && tplKey){
                    hash[cell._row.id]=options.value;
                    prf.boxing().setAttr(tplKey, hash, false);
                    ns.fireEvent('onDirty',[map[cell._row.id]]);
                }
            }
        }
    }
});
