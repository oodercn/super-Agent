ood.Class('RAD.PropEditor', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("10em")
                .setTop("2.5em")
                .setWidth("21.666666666666668em")
                .setHeight("22.5em")
                .setShadow(false)
                .setResizer(false)
                .setCaption("$(RAD.designer.attreditor.Attributes Editor)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block1")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.ctl_block1.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"grid")
                .setShowDirtyMark(false)
                .setEditable(true)
                .setRowHandler(false)
                .setColSortable(false)
                .setHeader([
                    {
                        "id":"name",
                        "caption":"$RAD.designer.attreditor.Attribute",
                        "width":"13.583333333333334em",
                        "type":"label"
                    },

                    {
                    "id":"value",
                    "caption":"$RAD.designer.attreditor.Value",
                    "flexSize":true,
                    "width":"10.666666666666666em",
                    "type":"input"
                }])
                .setTreeMode('none')
                .afterCellUpdated("_grid_aftercellupdated")
                .beforeComboPop('_grid_beforepop')
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            var ns=this,
                prop=ns.properties,
                target = prop.targetProfile.boxing()
                ;

            ns.DATA=prop.realData||{};

            // Give a chance to modify editor setting for rows
            //propName, subKey, propValue, clsName, target, cell, grid
            window.SPA && SPA.fe("onInitEditorRows", [prop.propKey, '', prop.realData, 
                'RAD.PropEditor', target,  prop.uiData, ns.grid]);

            if(window.SPA && SPA.events.onInitPropValueCell){
                ood.arr.each(prop.uiData,function(row){
                    ood.arr.each(row.cells,function(cell,i){
                         // Give a chance to modify editor setting for value cell
                        window.SPA && SPA.fe("onInitPropValueCell", [prop.propKey,prop.uiHeader[i].id, cell.value, "RAD.PropEditor", target, cell, ns.grid] );
                    });
                });
            }
            ns.grid.setRows(prop.uiData,true);
            
            var unFun=function(){
                ood.Event.keyboardHook('esc');
                ns.dialog.close();
            };
            ood("body").append(ns.dialog);
            ns.dialog.getRoot()
                .popToTop(ood(prop.src))
                .setBlurTrigger("RAD.PropEditor",unFun);
            ood.Event.keyboardHook('esc',0,0,0,unFun);

            ns.dialog.activate();
            
            ns._dirty=0;
            
            return true;
        },
        _grid_aftercellupdated:function (profile, cell, options, isHotRow){
            var ns=this;
            if("value" in cell){
                if(ns.properties.fc){
                    if(!ood.isSet(cell.value)||cell.value==="")
                        delete ns.DATA[cell.id.toLowerCase()] ;
                    else{
                        if(ood.isStr(cell.value) && cell.value.charAt(0)=='#'){
                            cell.value=cell.value.replace('#','');
                        }
                        ns.DATA[cell.id.toLowerCase()] = cell.value===false?"0":cell.value===true?"1":cell.value;
                    }
                }else{
                    if(!ood.isSet(cell.value)||cell.value==="")
                        delete ns.DATA[cell.id] ;
                    else{
                        ns.DATA[cell.id] = cell.value;
                    }
                }
            }
            var id = ns.properties.labelId;
            if(id && cell._row.id===id){
                ns.fireEvent('onLabelChanged',[cell.value]);
            }
            ns._dirty=1;
        },
        _grid_beforepop:function(profile, cell, editorprf, pos, e, src){
            var ns=this,
                target = prop.targetProfile.boxing(),
                prop=ns.properties;
            if(window.SPA && false===SPA.fe("beforeEditorComboPop", [
                prop.propKey, cell._col.id, cell.value, function(value){
                        editorprf.boxing().setUIValue(value||"",true);                    
                    }, "RAD.PropEditor", target, pos, profile.getSubNode('CELL', cell._serialId).get(0), cell, profile.boxing() ,editorprf&&editorprf.boxing()
                ] ))return false;
        },
        _dialog_beforeclose : function (profile) {
            var ns=this;

            if(ns._dirty){
                ns.fireEvent('onChanged',[ns.DATA]);
            }

            ns.DATA=null;
            profile.boxing().hide();
            ns.grid.removeAllRows();
            ns._dirty=0;
            return false;
        }
    }
});
