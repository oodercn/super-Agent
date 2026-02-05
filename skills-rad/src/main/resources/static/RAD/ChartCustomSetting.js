ood.Class('RAD.ChartCustomSetting', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("45em")
                .setHeight("30.833333333333332em")
                .setShadow(false)
                .setResizer(false)
                .setCaption("$(RAD.fc.Chart Configure)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Span")
                .setHost(host,"btn_del")
                .setClassName("oodcon ood-uicmd-close")
                .setTips("$(RAD.fc.Remove the row)")
                .setTop("0.125em")
                .setWidth("height")
                .setDisplay("none")
                .onClick("_remove_clicked")
                .setCustomStyle({
                    "KEY":{
                        "font-size":"1.5em",
                        "cursor":"pointer"
                    }
                }
                )
                );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block1")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.ctl_block1.append(
                ood.create("ood.UI.Tabs")
                .setHost(host,"tabs")
                .setItems([{
                    "id":"chart",
                    "caption":"$RAD.fc.grps.Chart"
                },
                {
                    "id":"data",
                    "caption":"$(RAD.fc.grps.Data Set)"
                },
                {
                    "id":"data2",
                    "caption":"$(RAD.fc.grps.Data Set)"
                },
                {
                    "id":"vsl",
                    "caption":"$(RAD.fc.grps.Vertical Lines)"
                },
                {
                    "id":"tl",
                    "caption":"$(RAD.fc.grps.Trend Lines)"
                },
                {
                    "id":"vl",
                    "caption":"$(RAD.fc.grps.Vertical Trend Lines)"
                },
                {
                    "id":"ann",
                    "caption":"$(RAD.fc.grps.Annotations)"
                }])
                .setValue("chart")
                .onIniPanelView("_tabs_oninipanelview")
                );
            
            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"gridSet")
                .setShowDirtyMark(false)
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandlerWidth("1.5em")
                .setHeader({}
                )
                .setGrpCols({}
                )
                .setRows({}
                )
                .setTreeMode('none')
                .setHotRowMode("show")
                .setDropKeys("single s")
                .setDragKey("single s")
                .beforeHotRowAdded("_gridSet_beforehotrowadded")
                .afterCellUpdated("_cellupdated")
                .onRowHover("_grid_onmousehover")
                .onDrop("_grid_ondrop")
                .onStartDrag("_grid_onstartdrag")
                , "data");
            
            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"gridSet2")
                .setShowDirtyMark(false)
                .setTips("$(RAD.fc.Click to configure categories)")
                .setEditable(true)
                .setGridHandlerCaption("$RAD.fc.Categories")
                .setHeaderHeight("1.5em")
                .setColSortable(false)
                .setColMovable(true)
                .setHeader({}
                )
                .setGrpCols({}
                )
                .setRows({}
                )
                .setActiveMode("none")
                .setTreeMode('none')
                .setDropKeys("multi s")
                .setDragKey("multi s")
                .beforeRender("_gridset2_bcr")
                .onRender("_gridset2_oncr")
                .afterCellUpdated("_cellupdated2")
                .onClickHeader("_gridSet2_onclickheader")
                .onClickRowHandler("_gridSet2_onclickrh")
                .onClickGridHandler("_gridSet2_onclickgh")
                .beforeComboPop("_gridSet2_pop")
                .onDropTest("_gridset2_dtest")
                .onDrop("_grid_ondrop")
                .onStartDrag("_gridset2_bds")
                .onGetDragData("_gridset2_dragdata")
                .setCustomStyle({
                    "FCELL":"border-left:0;border-top:0;cursor:pointer;",
                    "HCELL":"cursor:pointer;",
                    "FHCELL":"cursor:pointer;"
                }
                )
                , "data2");
            
            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"gridVSL")
                .setShowDirtyMark(false)
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandlerWidth("1.5em")
                .setHeader({}
                )
                .setGrpCols({}
                )
                .setRows({}
                )
                .setTreeMode('none')
                .setHotRowMode("show")
                .beforeHotRowAdded("_gridVSL_beforehotrowadded")
                .afterCellUpdated("_cellupdated")
                .onRowHover("_grid_onmousehover")
                , "vsl");
            
            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"gridVL")
                .setShowDirtyMark(false)
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandlerWidth("1.5em")
                .setHeader({}
                )
                .setGrpCols({}
                )
                .setRows({}
                )
                .setTreeMode('none')
                .setHotRowMode("show")
                .beforeHotRowAdded("_gridTrend_beforehotrowadded")
                .afterCellUpdated("_cellupdated")
                .onRowHover("_grid_onmousehover")
                .onStartDrag("_grid_onstartdrag")
                , "vl");
            
            host.tabs.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"gridTL")
                .setShowDirtyMark(false)
                .setRowNumbered(true)
                .setEditable(true)
                .setRowHandlerWidth("1.5em")
                .setHeader({}
                )
                .setGrpCols({}
                )
                .setRows({}
                )
                .setTreeMode('none')
                .setHotRowMode("show")
                .beforeHotRowAdded("_gridTrend_beforehotrowadded")
                .afterCellUpdated("_cellupdated")
                .onRowHover("_grid_onmousehover")
                .onStartDrag("_grid_onstartdrag")
                , "tl");
            
            host.tabs.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"ctl_layout3")
                .setItems([{
                    "id":"before",
                    "pos":"before",
                    "min":10,
                    "size":213,
                    "locked":false,
                    "folded":false,
                    "hidden":false,
                    "cmd":false
                },
                {
                    "id":"main",
                    "min":10,
                    "size":80
                }])
                .setType("horizontal")
                , "chart");
            
            host.ctl_layout3.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block4")
                .setDock("fill")
                .setBorderType("none")
                , "before");
            
            host.ctl_block4.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"tv1")
                .setShowDirtyMark(false)
                .setInitFold(false)
                .onItemSelected("_tv1_onitemselected")
                );
            
            host.ctl_block4.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block87")
                .setDock("top")
                .setHeight("1.5833333333333333em")
                .setHtml("<div style=\"text-align: center;\"><b>$(RAD.fc.Attributes Type)</b></div>")
                );
            
            host.ctl_layout3.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"propGrid1")
                .setShowDirtyMark(false)
                .setEditable(true)
                    .setRowHandler(false)
                .setColSortable(false)
                .setHeader([
                    {
                        "id":"name",
                        "caption":"$RAD.fc.Attribute",
                        "width":"13.583333333333334em",
                        "type":"label"
                    },{
                    "id":"value",
                    "caption":"$RAD.fc.Value",
                    "width":"13.583333333333334em",
                    "type":"input"
                }])
                .afterCellUpdated("_propGrid1_aftercellupdated")
                , "main");
            
            host.ctl_layout3.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block85")
                .setDock("top")
                .setHeight("1.5833333333333333em")
                .setHtml("<div style=\"text-align: center;\"><b>$(RAD.fc.Attributes Editor)</b></div>")
                , "main");
            
            host.tabs.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"ctl_layout4")
                .setItems([{
                    "id":"before",
                    "pos":"before",
                    "min":10,
                    "size":213,
                    "locked":false,
                    "folded":false,
                    "hidden":false,
                    "cmd":false
                },
                {
                    "id":"main",
                    "min":10,
                    "size":80
                }])
                .setType("horizontal")
                , "ann");
            
            host.ctl_layout4.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block5")
                .setDock("fill")
                .setBorderType("none")
                , "before");
            
            host.ctl_block5.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"tvann")
                .setShowDirtyMark(false)
                .setInitFold(false)
                .beforeClick("_tvann_beforeclick")
                .onCmd("_tvann_oncmd")
                .onItemSelected("_tvann_onitemselected")
                );
            
            host.ctl_block5.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block88")
                .setDock("top")
                .setHeight("1.6666666666666667em")
                .setHtml("<div style=\"text-align: center;\"><b>$(RAD.fc.grps.Annotations)</b></div>")
                );
            
            host.ctl_layout4.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"annGrid")
                .setShowDirtyMark(false)
                .setEditable(true)
                 .setRowHandler(false)
                .setColSortable(false)
                .setHeader([
                    {
                        "id":"name",
                        "caption":"$RAD.fc.Attribute",
                        "width":"13.583333333333334em",
                        "type":"label"
                    },{
                    "id":"value",
                    "caption":"$RAD.fc.Value",
                    "width":"13.583333333333334em",
                    "type":"input"
                }])
                .setGrpCols({}
                )
                .setRows({}
                )

                .afterCellUpdated("_anngrid_aftercellupdated")
                , "main");
            
            host.ctl_layout4.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block89")
                .setDock("top")
                .setHeight("1.6666666666666667em")
                .setHtml("<div style=\"text-align: center;\"><b>$(RAD.fc.Attributes Editor)</b></div>")
                , "main");
            
            host.ctl_block1.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btnapply")
                .setTop("0.25em")
                .setWidth("5.25em")
                .setHeight("2.1666666666666665em")
                .setRight("0.5em")
                .setCaption("$RAD.fc.Apply")
                .onClick("_apply")
                );
            
            append(
                ood.create("ood.UI.PopMenu")
                .setHost(host,"popType")
                .setItems([{
                    "id":"text",
                    "caption":"$(RAD.fc.svgType.text)"
                },
                {
                    "id":"image",
                    "caption":"$(RAD.fc.svgType.image)"
                },
                {
                    "id":"line",
                    "caption":"$(RAD.fc.svgType.line)"
                },
                {
                    "id":"rectangle",
                    "caption":"$(RAD.fc.svgType.rectangle)"
                },
                {
                    "id":"circle",
                    "caption":"$(RAD.fc.svgType.circle)"
                },
                {
                    "id":"arc",
                    "caption":"$(RAD.fc.svgType.arc)"
                },
                {
                    "id":"polygon",
                    "caption":"$(RAD.fc.svgType.polygon)"
                },
                {
                    "id":"undefined",
                    "caption":"undefined"
                },
                {
                    "id":"path",
                    "caption":"$(RAD.fc.svgType.path)"
                }])
                .onMenuSelected("_poptype_clk")
            );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            return this._init();
        },
        _init : function (targetProfile) {
            var ns=this,
                prop=ns.properties,
                chart=targetProfile?(prop.targetProfile=targetProfile):prop.targetProfile,
                data=ood.clone(chart.boxing().getJSONData(),true),
                NoData={
//                    Bulb:1,
                    HLED:1,
                    VLED:1,
                    Cylinder:1,
                    Thermometer:1
                },
                NoTL={
                    Pie2D:1,
                    Pie3D:1,
                    Doughnut2D:1,
                    Doughnut3D:1,
                    Funnel:1,
                    Pyramid:1,
                    Radar:1,
                    Cylinder:1,
                    Thermometer:1
                },
                ColorRange={
//                    Bulb:1,
                    HLED:1,
                    VLED:1,
                    HLinearGauge:1,
                    AngularGauge:1
                },
                TrendPoint={
                    HLinearGauge:1,
                    AngularGauge:1
                };


            ns._dirty=false;
            ns.btnapply.setDisplay('none');

            ns._chartType = chart.properties.chartType;
            ns.DATA = data;
            ns._mutiser = !!FCCONF.attrCat[ns._chartType]["dataset"];
  
            ns._xyPlot=ns._chartType=='Scatter'||ns._chartType=='Bubble';
            
            ns.tabs.resetPanelView(true,false,false);

            ns.tabs.updateItem('tl',{hidden: ns.noTL = NoTL[ns._chartType]});
            ns.tabs.updateItem('vl',{hidden: ns.noVL = !FCCONF.attrCat[ns._chartType]["Vertical Trend Lines"] && !FCCONF.attrCat[ns._chartType]["Trend Point"]});
            ns.tabs.updateItem('vsl',{hidden: ns.noVSL=!FCCONF.attrCat[ns._chartType]["Vertical data separator lines"]});
            ns.tabs.updateItem('data',{hidden: ns.noData = ns._mutiser || NoData[ns._chartType]});
            ns.tabs.updateItem('data2',{hidden: ns.noData2 = !ns._mutiser || NoData[ns._chartType]});
            if(!ns.noTL){
                 if(ColorRange[ns._chartType]){
                     ns._TL_Type="colorrange"
                    ns.tabs.updateItem('tl',{caption:"$(RAD.fc.grps.Color Range)"});
                 }else{
                    ns._TL_Type="trendlines"
                    ns.tabs.updateItem('tl',{caption:"$(RAD.fc.grps.Trend Lines)"});
                }
            }
            if(!ns.noVL){
                 if(TrendPoint[ns._chartType]) {
                    ns._VL_Type="trendpoints";
                    ns.tabs.updateItem('vl',{caption:"$(RAD.fc.grps.Trend Point)"});
                 }else{
                   ns._VL_Type="vtrendlines";
                    ns.tabs.updateItem('vl',{caption:"$(RAD.fc.grps.Vertical Trend Lines)"});
                }
            }
            if(!ns.noData){
                if(ns._chartType=="HLinearGauge"){
                    ns._Data_Type="pointer"
                    ns.tabs.updateItem('data',{caption:"$(RAD.fc.grps.Pointers)"});
                }else if(ns._chartType=="AngularGauge"){
                    ns._Data_Type="dial"
                    ns.tabs.updateItem('data',{caption:"$(RAD.fc.grps.Dials)"});
               }else{
                    ns._Data_Type="data"
                    ns.tabs.updateItem('data',{caption:"$(RAD.fc.grps.Data Set)"});
                }
            }

            var grps = FCCONF.attrCat[ns._chartType].chart;
            ood.arr.each(grps,function(o,i){
                if(!o.caption){
                    o.caption="$(RAD.fc.grps2."+o.id+")";
                    ood.arr.each(o.sub,function(m){
                            m.caption="$(RAD.fc.cats."+m.id.replace(/([)-])/g,"$$$1")+")";
                    });
                }
            });
            ns.tv1.setItems(grps);
 
            ood("body").append(ns.dialog);
            ns.dialog.getRoot()
            .popToTop(ood(prop.src), 2)
            .setBlurTrigger("RAD.ChartCustomSetting",function(){
                ns.dialog.close();
            });

            ns.tabs.setValue('chart',true);
            ns.tv1.fireItemClickEvent("Titles (Axis) Names");

            ns.dialog.activate();
            
            return true;
        },
        _dialog_beforeclose : function (profile) {
            var ns=this;

            ns.DATA=ns._chartType=null;
            profile.boxing().hide();
            ns.gridSet.removeAllRows().setHeader([]);
            ns.gridSet2.removeAllRows().setHeader([]);
            ns.gridTL.removeAllRows().setHeader([]);
            ns.gridVL.removeAllRows().setHeader([]);
            ns.gridVSL.removeAllRows().setHeader([]);
            ns.tabs.setValue('chart',true);
            
            ns.fireEvent('onFinished');
            return false;
        },
        _tvann_oncmd:function(prf,item,key){
            var ns=this;
            ood.confirm('$RAD.Remove?', ood.getRes('RAD.fc.Are You Sure to remove this ' + item.type) +'?',function(){
                prf.boxing().removeItems(item.id);
                prf.boxing().fireItemClickEvent("root");
                ns._change();
            });
        },
        _tvann_beforeclick:function(prf,item,e,src){
             var ns=this,
                  ins=prf.boxing(), id="n_"+ood.stamp();
            if(item.type=='label1'){
                ins.insertItems({
                    type:'group',
                    caption:'$(RAD.fc.Annotation Group)',
                    tagCmds:[{
                        "id" : "Remove",
                        "type" : "text",
                        "caption":"&times;",
                        tips:"$(RAD.fc.Remove Annotation Group)"
                    }],
                    $data:{},
                    sub:[
                            {type:'label2', itemStyle:"font-style: italic;cursor:pointer;", image:"{ood.ini.img_bg}", caption:'$(RAD.fc.[ + Annotation Item])'}
                    ]
                },item._pid,item.id,true);
                ins.fireItemClickEvent(id);
                ns._change();

                return false;
            }
            if(item.type=="label2"){
                ns.popType.pop(ood.Event.getPos(e));
                // keep it
                ns.popType.setTag(item);
                return false;
            }
        },
        _poptype_clk:function(profile, item, src){
             var ns=this, ins=ns.tvann, id="n_"+ood.stamp(),
                tvitem=profile.boxing().getTag(),
                subtype=item.id;
            ins.insertItems({
                id:id,
                type:"item",
                subtype:subtype,
                caption:'$(RAD.fc.Annotation Item) - '  + item.caption,
                tagCmds:[{
                    "id" : "Remove",
                    type:'item',
                    "type" : "text",
                    "caption":"&times;",
                    tips:"$(RAD.fc.Remove Annotation Item)"
                }],
                $data:{type:subtype}
            },tvitem._pid,tvitem.id,true);
            
            ins.fireItemClickEvent(id);
            ns._change();
        },
        _tvann_onitemselected:function (profile, item, e, src, type){
            var ns = this,rows,
                data=item.$data||(item.$data={});
            switch(item.type){
                case 'root':
                    rows=ns._buildTG1("annotations", data, FCCONF.annotations.annotation);
                break;
                case 'group':
                    rows=ns._buildTG1("annotations", data, FCCONF.annotations.group);
                break;
                case 'item':
                    rows=ns._buildTG1("annotations", data, FCCONF.annotations[item.subtype]);
                break;
            }
            ns.annGrid.setRows(rows,true);
            ns.annGrid.get(0).$data=data;
        },
        _tv1_onitemselected:function (profile, item, e, src, type){
            var ns = this,rows;
            //special for single value
            if(item.root){
                rows=ns._buildTG1(item.id, ns.DATA, item.prop);
                ood.arr.each(rows,function(row){
                    row.root=true;
                });
            }else{
                rows=ns._buildTG1(item.id, ns.DATA.chart, item.prop);
            }
            ns.propGrid1.setRows(rows,true);
        },
        _buildTG1:function(id, data, arrSet){
            var ns=this,
            getV=function(id, type, data){
                var v=data[id.toLowerCase()];
                return type=='checkbox'?(v==='1'):v;
            },adjust=function(arr){
                var rows=[],cell;
                ood.arr.each(arr,function(o,i){
                    if(ood.isHash(o)){
                        cell=o;
                        cell.value=getV(o.id,o.type,data);
                    }else{
                        o=cell={id:o,value:"[Not Configured]",caption:"$(RAD.fc.[Not Configured])"};
                    }
                    rows.push({id:o.id, caption:"$RAD.fc.keys."+o.id, tips:o.id, cells:[{id:'name',caption:"$RAD.fc.keys."+o.id?"$RAD.fc.keys."+o.id:o.id},cell]});
                });
                return rows;
            };
            
            var items=[],attr=FCCONF.gridAttr,item,t;
            ood.arr.each(arrSet,function(k){
                if(t=ood.get(attr,[id,k])){
                    item=ood.copy(t);
                    item.id=k;
                }else if(t=attr[k]){
                    item=ood.copy(t);
                    item.id=k;
                }else{
                    item=k;
                }
                items.push(item);
            });
            return adjust(items);
        },
        _getV:function(cell){
            return cell.value===false?"0":cell.value===true?"1":((cell.value+"").replace(/^#/,''));
        },
        _anngrid_aftercellupdated:function (profile, cell, options, isHotRow){
            var ns=this, data=profile.$data;
            if("value" in cell){
                if(!ood.isSet(cell.value)||cell.value===""){
                    delete data[cell.id.toLowerCase()] ;
                }else{
                    if(ood.isStr(cell.value) && cell.value.charAt(0)=='#'){
                        cell.value=cell.value.replace('#','');
                    }
                    data[cell.id.toLowerCase()] = ns._getV(cell);
                }
                ns._change();
            }
        },
        _propGrid1_aftercellupdated:function (profile, cell, options, isHotRow){
            var ns=this;
            if("value" in cell){
                //special for single value
                var isroot=cell._row.root;
                if(!ood.isSet(cell.value)||cell.value===""){
                    if(isroot) delete ns.DATA[cell.id.toLowerCase()] ;
                    else delete ns.DATA.chart[cell.id.toLowerCase()] ;
                }else{
                    if(ood.isStr(cell.value) && cell.value.charAt(0)=='#'){
                        cell.value=cell.value.replace('#','');
                    }
                    if(isroot) ns.DATA[cell.id.toLowerCase()] = ns._getV(cell);
                    else  ns.DATA.chart[cell.id.toLowerCase()] = ns._getV(cell);
                }
                if(!isHotRow)
                    ns._change();
            }
        },
        _change:function(){
            var ns=this;
            ns._dirty=true;
            ns.btnapply.setDisplay('');
            ns.fireEvent('onChange');
        },
        _cellupdated2:function (profile, cell, options, isHotRow){
            var ns=this;
            if("value" in cell){
                if(ood.isStr(cell.value) && cell.value.charAt(0)=='#'){
                    cell.value=cell.value.replace('#','');
                }

                if(!cell.DATA)cell.DATA={};
                cell.DATA.value=cell.value;
                if(!isHotRow)
                    ns._change();
            }
        },
        _apply:function (){
            var ns=this,
                prop=ns.properties,
                chart=prop.targetProfile,
                tvann=ns.tvann,
                gridTL=ns.gridTL,
                gridVL=ns.gridVL,
                gridVSL=ns.gridVSL,
                rows,header,data;

            if(ns.DATA){
                var lines=[];
                header = gridTL.getHeader('min');
                if(header.length){
                    ood.arr.each(gridTL.getRows(),function(row){
                        if(ood.UI.TreeGrid.isHotRow(row))return;
                        data={};
                        ood.arr.each(row.cells,function(cell,i){
                            if(cell.needCollect)
                                data[header[i].toLowerCase()]=ns._getV(cell);
                        });
                        lines.push(data);
                    });
                    // for gauges
                    if(ns._TL_Type=="colorrange")
                        ood.set(ns.DATA,["colorrange","color"],lines);
                    else
                        ood.set(ns.DATA,["trendlines"], [{line:lines}]);
                }
                
                lines=[];
                header = gridVL.getHeader('min');
                if(header.length){
                    ood.arr.each(gridVL.getRows(),function(row){
                        if(ood.UI.TreeGrid.isHotRow(row))return;
                        data={};
                        ood.arr.each(row.cells,function(cell,i){
                            if(cell.needCollect)
                                data[header[i].toLowerCase()]=ns._getV(cell);
                        });
                        lines.push(data);
                    });
                    // for gauges
                    if(ns._VL_Type=="trendpoints")
                        ood.set(ns.DATA,["trendpoints","point"],lines);
                    else
                        ood.set(ns.DATA,["vtrendlines"], [{line:lines}]);
                }
                
                // 2-dimensions data
                if(ns._mutiser){
                    header = ns.gridSet2.getHeader();
                    rows = ns.gridSet2.getRows();
                    var datas=[],
                        vslcols=gridVSL.getHeader('min'),
                        vslrows=gridVSL.getRows();
                    // maybe no columns for Scatter
                    if((ns.noVSL || vslcols.length) && (header.length||rows.length)){
                        var categories={category:[]};
                        ood.arr.each(header,function(col){
                            categories.category.push(col.DATA);
                        });
                        if(vslcols&&vslcols.length){
                            ood.arr.each(vslrows, function(row){
                                if(ood.UI.TreeGrid.isHotRow(row))return;

                                var i=row.cells[0].value;
                                data={vline:"1"};
                                ood.arr.each(row.cells,function(cell,j){
                                    if(cell.needCollect)
                                        data[vslcols[j].toLowerCase()]=ns._getV(cell);
                                });
                                ood.arr.insertAny(categories.category, data, i);
                            },null,false);
                        }
                        ood.merge(categories, ns.gridSet2.getTagVar(), 'all');
                        
                        ns.DATA.categories=[categories];

                        var datasets=[],dataset,idatas;
                        ood.arr.each(rows,function(row,index){
                            if(row.type=='label'||row.type=='label2')return;
                            if(row.sub){
                                dataset={
                                    dataset:[]
                                };
                                ood.arr.each(row.sub,function(row,i){
                                    if(row.type=='label'||row.type=='label2')return;
                                    idatas={
                                        data:[]
                                    };
                                    if(!ood.isEmpty(row.DATA)){
                                        ood.merge(idatas, row.DATA, 'all');
                                    }
                                    ood.arr.each(row.cells,function(cell,i){
                                        idatas.data.push(cell.DATA||{});
                                    });
                                    dataset.dataset.push(idatas);
                                });
                                // for MSStackedColumn2DLineDY
                                if(row.lineset){
                                    ood.set(ns.DATA,["lineset"],dataset.dataset);
                                }else
                                    datasets.push(dataset);
                            }else{
                                dataset={
                                    data:[]
                                };
                                if(!ood.isEmpty(row.DATA)){
                                    ood.merge(dataset, row.DATA, 'all');
                                }
                                if(ns._xyPlot){
                                    dataset.data=row.DATA2;
                                }else{
                                    ood.arr.each(row.cells,function(cell,i){
                                        dataset.data.push(cell.DATA||{});
                                    });
                                }
                                datasets.push(dataset);
                            }
                        });
                        ood.set(ns.DATA,["dataset"],datasets);
                    }
                }else{
                    header = ns.gridSet.getHeader('min');
                    rows = ns.gridSet.getRows();
                    var datas=[],
                        vslcols=gridVSL.getHeader('min'),
                        vslrows=gridVSL.getRows();
                    if((ns.noVSL || vslcols.length) && (header.length||rows.length)){
                        ood.arr.each(rows,function(row,index){
                            if(ood.UI.TreeGrid.isHotRow(row))return;
                            data={};
                            ood.arr.each(row.cells,function(cell,i){
                                if(cell.needCollect)
                                    data[header[i].toLowerCase()]=ns._getV(cell);
                            });
                            datas.push(data);
                        });
                        if(ns._Data_Type=="pointer"){
                            ood.set(ns.DATA,["pointer","pointer"],datas);
                        }else if(ns._Data_Type=="dial"){
                            ood.set(ns.DATA,["dials","dial"],datas);
                        }else{
                            ood.set(ns.DATA,["data"],datas);
                            if(vslcols&&vslcols.length){
                                ood.arr.each(vslrows, function(row){
                                    if(ood.UI.TreeGrid.isHotRow(row))return;
    
                                    var i=row.cells[0].value;
                                    data={vline:"1"};
                                    ood.arr.each(row.cells,function(cell,j){
                                        if(cell.needCollect)
                                            data[vslcols[j].toLowerCase()]=ns._getV(cell);
                                    });
                                    ood.arr.insertAny(ns.DATA.data, data, i);
                                },null,false);
                            }
                        }
                    }
                }


                var items=tvann.getItems();
                 if(ood.get(items,[0,"sub"])){
                    var grp,item,ann={
                        groups:[]
                     };
                    ood.arr.each(items[0].sub,function(firstlayer){
                        if(firstlayer.sub&&firstlayer.sub.length){
                            grp={items:[]};
                            ood.arr.each(firstlayer.sub,function(secondlayer){
                                item=secondlayer.$data;
                                // if has content
                                if(!ood.isEmpty(item)){
                                    grp.items.push(item);
                                }
                            });
                            // if has sub content
                            if(grp.items.length){
                                ood.merge(grp, firstlayer.$data,'without');
                                ann.groups.push(grp);
                            }
                        }
                    });
                    // if has sub content
                    if(ann.groups.length){
                        ood.merge(ann, items[0].$data, 'without');
                        ns.DATA.annotations=ood.clone(ann, true);
                    }else
                        delete ns.DATA.annotations;                    
                }

                chart.boxing().setJSONData(ns.DATA, true);
                ns.fireEvent('onDirty');
             }            
             
            ns._dirty=false;
            ns.btnapply.setDisplay('none');
        },
        _buildGrid:function(type,data,grid,vline){
            var ns=this,
                attrCat=FCCONF.attrCat[ns._chartType],
                attr=FCCONF.gridAttr,
                header=[], col;

            if(vline){
                header.push({
                    id:'location',
                    caption:'$(RAD.fc.[Location])',
                    type:'spin',
                    precision:0,
                    increment:1,
                    min:0,
                    max:1000
                });
            }
            ood.arr.each(attrCat[type],function(key){
                var t;
                if(t=ood.get(attr,[type, key])){
                    col=ood.copy(t);
                    col.id=key;
                    col.caption="$RAD.fc.keys."+col.id;
                }else if(t=attr[key]){
                    col=ood.copy(t);
                    col.id=key;
                    col.caption="$RAD.fc.keys."+col.id;
                }else{
                    col={id:key,caption:"$(RAD.fc.[Not Configured])"};
                }
                header.push(col);
            });
            if(grid)ns[grid].setHeader(header);                

            if(data){
                var rows=[];
                ood.arr.each(data,function(line,i){
                    if(vline){
                        if(line.vline=="1"||line.vline=="true"){
                            var cells=[];
                            ood.arr.each(header,function(col){
                                if(col.id=="location"){
                                    cells.push({
                                        value: i
                                    });
                                }else{
                                    var id=col.id.toLowerCase();
                                    cells.push({
                                        value:id in line ? (col.type=='checkbox'?line[id]==="1"?true:false:line[id]) : "",
                                        needCollect: id in line
                                    });
                                }
                            });
                            rows.push({cells:cells});
                        }
                    }else{
                        if(line.vline!=="1"&&line.vline!=="true"){
                            var cells=[];
                            ood.arr.each(header,function(col,id){
                                id=col.id.toLowerCase();
                                cells.push({
                                    value:id in line ? (col.type=='checkbox'?line[id]==="1"?true:false:line[id]) : "",
                                    needCollect: id in line
                                });
                            });
                            rows.push({cells:cells});
                        }
                    }
                });
                if(rows.length){
                    if(grid)ns[grid].setRows(rows);
                }
            }
            if(grid)ns[grid].addHotRow(header[0]);
            
            return {header:header,rows:rows};
        },
        _tabs_oninipanelview:function (profile, item){
            var ns=this;
            switch(item.id){
                case 'data2':
                    if(profile._ining2)return;

                    var header=[],cells=[],rows=[];
                    var cs=ns.DATA.categories?ns.DATA.categories[0]:{category:[]};
                    // categories object
                    ns.gridSet2.setTagVar( ood.clone(cs,function(o,i){return 'category'!=i}) );
                    ood.arr.each(cs.category,function(cat,i){
                        if(cat.vline!=="1"&&cat.vline!=="true"){
                            header.push({
                                id:cat.label + ood.stamp(),
                                caption:"$(RAD.fc.keys."+cat.label+")",
                                type:'popbox',
                                tips:'$(RAD.fc.Click to configure category)',
                                // category object
                                DATA:ood.clone(cat)
                            });
                        }
                    });

                    if(ns._xyPlot){
                        ood.arr.each(ns.DATA.dataset,function(data,i){
                            rows.push({
                                caption:data.seriesname,
                                group:true,
                                xyPlot:1,
                                firstCellClass:"ood-treegrid-hcell",
                                tips:'$(RAD.fc.Click to configure dataset)',
                                // dataset object
                                DATA:ood.clone(data,function(o,i){return 'data'!=i}),
                                DATA2:ood.clone(data.data)
                            });
                        });
                        rows.push({type:'label',xyPlot:1,group:true,cellStyle:"font-weight:normal;font-style: italic;",caption:'$(RAD.fc.[ + Dataset])',tips:'$(RAD.fc.Add dataset)'});
                        ns.gridSet2.setTreeMode('inhandler').setRowHandlerWidth(100);
                    }else{
                        var msstack;
                        ood.arr.each(ns.DATA.dataset,function(data,i){
                            cells=[];
                            //for MSStackedColumn2D and MSStackedColumn2DLineDY
                            if(data.dataset){
                                msstack=1;
                                var sub=[];
                                rows.push({
                                    caption:'$(RAD.fc.Dataset group)',
                                    group:true,
                                    tips:'$(RAD.fc.Dataset group)',
                                    dragkey:'group',
                                    sub:sub
                                });
                                
                                ood.arr.each(data.dataset,function(data,i){
                                    cells=[];
                                    ood.arr.each(data.data,function(d,i){
                                        cells.push({
                                            value:d.value,
                                            // set object
                                            DATA:ood.clone(d)
                                        });
                                    });
                                    sub.push({
                                        caption:data.seriesname,
                                        cells:cells,
                                        firstCellClass:"ood-treegrid-hcell",
                                        tips:'$(RAD.fc.Click to configure dataset)',
                                        dragkey:'dataset',
                                        // dataset object
                                        DATA:ood.clone(data,function(o,i){return 'data'!=i})
                                    });
                                });
                                sub.push({type:'label', group:true, cellStyle:"font-weight:normal;font-style: italic;",caption:'$(RAD.fc.[ + Dataset])',tips:'$(RAD.fc.Add dataset)'});
                                
                            }else{
                                ood.arr.each(data.data,function(d,i){
                                    cells.push({
                                        value:d.value,
                                        // set object
                                        DATA:ood.clone(d)
                                    });
                                });
                                rows.push({
                                    caption:data.seriesname,
                                    cells:cells,
                                    firstCellClass:"ood-treegrid-hcell",
                                    tips:'$(RAD.fc.Click to configure dataset)',
                                    dragkey:'datset',
                                    // dataset object
                                    DATA:ood.clone(data,function(o,i){return 'data'!=i})
                                });
                            }
                        });
                        if(msstack){
                            if(ns._chartType=="MSStackedColumn2DLineDY"){
                                var sub=[];
                                // first
                                ood.arr.insertAny(rows,{
                                    caption:'$(RAD.fc.Lineset group)',
                                    group:true,
                                    tips:'$(RAD.fc.Lineset group)',
                                    lineset:true,
                                    sub:sub
                                },0);
                                ood.arr.each(ns.DATA.lineset,function(data,i){
                                    cells=[];
                                    ood.arr.each(data.data,function(d,i){
                                        cells.push({
                                            value:d.value,
                                            // set object
                                            DATA:ood.clone(d),
                                            special:'line'
                                        });
                                    });
                                    sub.push({
                                        caption:data.seriesname,
                                        cells:cells,
                                        firstCellClass:"ood-treegrid-hcell",
                                        tips:'$(RAD.fc.Click to configure lineset)',
                                        dragkey:'lineset',
                                        // dataset object
                                        DATA:ood.clone(data,function(o,i){return 'data'!=i}),
                                        special:'lineset',
                                        spceialcell:"line"
                                    });
                                });
                                sub.push({type:'label', group:true, cellStyle:"font-weight:normal;font-style: italic;",caption:'$(RAD.fc.[ + Lineset])',tips:'$(RAD.fc.Add lineset)'});
                            }
                            
                            rows.push({type:'label2',group:true,cellStyle:"font-weight:normal;font-style: italic;",caption:'$(RAD.fc.[ + Group])',tips:'$(RAD.fc.Add dataset group)'});
                            ns.gridSet2.setTreeMode('inhandler').setRowHandlerWidth(150)
                        }else{
                            rows.push({type:'label',group:true,cellStyle:"font-weight:normal;font-style: italic;",caption:'$(RAD.fc.[ + Dataset])',tips:'$(RAD.fc.Add dataset)'});
                            ns.gridSet2.setTreeMode('inhandler').setRowHandlerWidth(100);
                        }
                        if(FCCONF.attrCat[ns._chartType]["Vertical data separator lines"]){
                            profile._ining2=1;
                            profile.boxing().iniPanelView('vsl');
                            delete profile._ining2;
                        }
                    }
                    ns.gridSet2.setHeader(header).setRows(rows);
                    break;
                case 'data': 
                    if(profile._ining2)return;
                    var type,data;
                    // for HLinearGauge
                    if(ns._Data_Type=="pointer"){
                        type="pointer";
                        data=ood.get(ns.DATA,["pointers","pointer"]);
                    }
                    // for AngularGauge
                    else if(ns._Data_Type=="dial"){
                        type="dial";
                        data=ood.get(ns.DATA,["dials","dial"]);
                    }else{
                        type="set";
                        data=ood.get(ns.DATA,['data']);
                    }
                    ns._buildGrid(type, data, 'gridSet');

                    if(FCCONF.attrCat[ns._chartType]["Vertical data separator lines"]){
                        profile._ining2=1;
                        profile.boxing().iniPanelView('vsl');
                        delete profile._ining2;
                    }

                    break;
                case 'vsl':
                    if(profile._ining)return;
                    if(ns._mutiser){
                        ns._buildGrid('Vertical data separator lines', ood.get(ns.DATA,['categories',0,'category']), 'gridVSL', true);
                    }else{
                        ns._buildGrid('Vertical data separator lines', ood.get(ns.DATA,['data']), 'gridVSL', true);
                    }
                    profile._ining=1;
                    profile.boxing().iniPanelView(ns._mutiser?'data2':'data');
                    delete profile._ining;

                    break;
                case 'tl':
                    if(ns._TL_Type=="colorrange"){
                        ns._buildGrid('Color Range', ood.get(ns.DATA,["colorrange","color"]), 'gridTL');
                    }else{
                        ns._buildGrid('Trend Lines', ood.get(ns.DATA,["trendlines",0,"line"]), 'gridTL');
                    }
                    break;
                case 'vl':
                    if(ns._VL_Type=="trendpoints"){
                        ns._buildGrid('Trend Point', ood.get(ns.DATA,["trendpoints","point"]), 'gridVL');
                    }else{
                        ns._buildGrid('Vertical Trend Lines', ood.get(ns.DATA,["vtrendlines",0,"line"]), 'gridVL');
                    }
                    break;
                case 'ann':
                    var ann=ns.DATA.annotations,
                        items;
                    if(!ood.get(ann, ["groups",0,"items",0])){
                        items=[{
                            id:"root",
                            type:'root',
                            caption:"$(RAD.fc.Annotations)",
                            $data:{},
                            sub:[
                                {
                                    type:'group',
                                    caption:'$(RAD.fc.Annotation Group)',
                                    tagCmds:[{
                                        "id" : "Remove",
                                        type:'group',
                                        "type" : "text",
                                        "caption":"&times;",
                                        tips:"$(RAD.fc.Remove Annotation Group)"
                                    }],
                                    $data:{},
                                    sub:[
                                            {
                                                type:'item',
                                                subtype:'text',
                                                caption:'$(RAD.fc.Annotation Item) - ' + '$(RAD.fc.svgType.text) ',
                                                tagCmds:[{
                                                    "id" : "Remove",
                                                    "type" : "text",
                                                    "caption":"&times;",
                                                    tips:"$(RAD.fc.Remove Annotation Item)"
                                                }],
                                                $data:{type:"text"}
                                            },
                                            {type:'label2', itemStyle:"font-style: italic;cursor:pointer;", image:"{ood.ini.img_bg}", caption:'$(RAD.fc.[ + Annotation Item])'}
                                    ]
                                },
                                {type:'label1', itemStyle:"font-style: italic;cursor:pointer;", image:"{ood.ini.img_bg}",caption:'$(RAD.fc.[ + Annotation Group])'}
                            ]
                        }];
                    }else{
                        // root
                        var root={
                            id:"root",
                            type:'root',
                            caption:"$(RAD.fc.Annotations)",
                            $data:{},
                            sub:[]
                        },group,item;
                        items=[root];
                        ood.merge(root.$data,ann);
                        delete root.$data.groups; 
                        
                        ood.arr.each(ann.groups,function(grp){
                            group={
                                type:'group',
                                caption:'$(RAD.fc.Annotation Group)',
                                tagCmds:[{
                                    "id" : "Remove",
                                    type:'group',
                                    "type" : "text",
                                    "caption":"&times;",
                                    tips:"$(RAD.fc.Remove Annotation Group)"
                                }],
                                $data:{},
                                sub:[]
                            };
                            ood.merge(group.$data,grp);
                            delete group.$data.items; 
                            root.sub.push(group);
                            
                            ood.arr.each(grp.items,function(ii){
                                item={
                                    type:'item',
                                    subtype:ii.type,
                                    caption:'$(RAD.fc.Annotation Item) - '+ '$(RAD.fc.svgType.'+ii.type+') ',
                                    tagCmds:[{
                                        "id" : "Remove",
                                        "type" : "text",
                                        "caption":"&times;",
                                        tips:"$(RAD.fc.Remove Annotation Item)"
                                    }],
                                    $data:{}
                                };
                                ood.merge(item.$data,ii);
                                group.sub.push(item);
                            });
                            group.sub.push({type:'label2', itemStyle:"font-style: italic;cursor:pointer;", image:"{ood.ini.img_bg}", caption:'$(RAD.fc.[ + Annotation Item])'});
                        });
                        root.sub.push({type:'label1', itemStyle:"font-style: italic;cursor:pointer;", image:"{ood.ini.img_bg}",caption:'$(RAD.fc.[ + Annotation Group])'});
                    }
                    ns.tvann.setItems(items).fireItemClickEvent("root",true);
                break;
                default: 
            }
        },
        _gridSet_beforehotrowadded:function (profile, cellMap){
            if(cellMap){
                var ns = this, uictrl = profile.boxing();
                if(!cellMap[profile.properties.header[0].id])return false;
                else{
                    ns._change();
                    return true;
                }
            }
        },
        _gridVSL_beforehotrowadded:function (profile, cellMap){
            var ns = this, uictrl = profile.boxing();
            if(ood.isNumb(cellMap[profile.properties.header[0].id])){
                ns._change();
                return true;
            }else{
                return false;
            }
        },
        _gridTrend_beforehotrowadded:function (profile, cellMap){
            var ns = this, 
                uictrl = profile.boxing(),
                startValue=cellMap[profile.properties.header[0].id];
            if(startValue||startValue===0){
                ns._change();
                return true;
            }else{
                return false;
            }
        },
        _cellupdated:function(profile, cell, options, isHotRow){
            var ns=this;
            if("value" in cell){
                if(ood.isStr(cell.value) && cell.value.charAt(0)=='#'){
                    cell.value=cell.value.replace('#','');
                }
            }            
            cell.needCollect=1;
            if(!isHotRow)
                ns._change();
        },
        _grid_onstartdrag:function(profile){
            var ns=this,btn=ns.btn_del;
            btn.setDisplay("none");
            ns.dialog.append(btn);
            delete ns._curgrid;
            delete ns._currowid;
        },
        _grid_onmousehover:function(profile, row, hover, e, src){
            if(profile.box.isHotRow(row))return;
            var ns=this,grid=profile.boxing(),btn=ns.btn_del;
            ood.resetRun("TL_DEL",function(){
                if(hover){
                    var node=grid.getSubNode('SCROLL22').get(0);
                    btn.setLeft(node.scrollLeft + node.clientWidth - 20 );
                    ood(src).append(btn);
                    btn.setDisplay("");
                    
                    ns._curgrid=grid;
                    ns._currowid=row.id;
                }else{
                    btn.setDisplay("none");
                    ns.dialog.append(btn);

                    delete ns._curgrid;
                    delete ns._currowid;
                }
            });
        },
        _remove_clicked:function(p,e){
            var ns=this,btn=ns.btn_del;
            if(ns._curgrid && ns._currowid){
                var curgrid=ns._curgrid,
                    currowid=ns._currowid;
                var pos=ood.Event.getPos(e);
                ood.confirm('$RAD.Remove?',ood.getRes('RAD.Are You Sure to remove this row') + '?',function(){
                    btn.setDisplay("none");
                    ns.dialog.append(btn);
                    curgrid.removeRows([currowid]);
                    delete ns._curgrid;
                    delete ns._currowid;

                    ns._change();
                },null,null,null,ns.dialog.getRoot().cssRegion());
            }
        },
        _gridSet2_onclickheader:function(prf, col, e, src){
            var ns=this,id="category";
            ood.ComFactory.getCom('RAD.PropEditor', function(){
                this.setProperties({
                    targetProfile:ns.properties.targetProfile,
                    propKey:"JSONData.*",

                    fc:1,
                    realData:col.DATA,
                    uiData:ns._buildTG1(id, col.DATA, FCCONF.attrCat[ns._chartType][id]),
                    labelId:"label",
                    src:src
                });
                this.setEvents({
                    onLabelChanged:function(label){
                        prf.boxing().updateColumn(col.id, {caption:label});
                    },
                    onChanged:function(){
                        ns._change();
                    }
                });
                this.show();
            });
        },
        _gridSet2_onclickgh:function(prf,e,src){
            var ns=this,id="categories";
            ood.ComFactory.getCom('RAD.PropEditor', function(){
                this.setProperties({
                    targetProfile:ns.properties.targetProfile,
                    propKey:"JSONData.*",

                    fc:1,
                    realData:prf.properties.tagVar,
                    uiData:ns._buildTG1(id, prf.properties.tagVar, FCCONF.attrCat[ns._chartType][id]),
                    src:src
                });
                this.setEvents({
                    onChanged:function(){
                        ns._change();
                    }
                });
                this.show();
            });
        },
        _gridSet2_onclickrh:function(prf, row, e, src){
            var ns=this;
            if(row.type=='label'){
                if(row.xyPlot){
                    prf.boxing().insertRows([{
                        caption:"[dataset]",
                        xyPlot:1,
                        group:1,
                        firstCellClass:"ood-treegrid-hcell",
                        tips:'$(RAD.fc.Click to configure dataset)',
                        // dataset object
                        DATA:{
                            seriesname:"[dataset]"
                        }
                    }],null,row.id,true);
                    ns._change();
                }else{
                    var cells=[];
                    ood.arr.each(prf.boxing().getHeader(),function(col){
                        cells.push({
                            value:0,
                            special:row.lineset?"line":null,
                            DATA:{
                                value:0
                            }
                        });
                    });
                    prf.boxing().insertRows([{
                        caption:"[dataset]",
                        firstCellClass:"ood-treegrid-hcell",
                        tips:'$(RAD.fc.Click to configure '+(row.lineset?"lineset":"dataset") + ")",
                        special:row.lineset?"lineset":null,
                        spceialcell:row.lineset?"line":null,
                        cells:cells,
                        // dataset object
                        DATA:{
                            seriesname:row.lineset?"[lineset]":"[dataset]"
                        }
                    }],null,row.id,true);
                    ns._change();
                }
            }else if(row.type=='label2'){
                var sub=[];                
                sub.push({type:'label', group:true, caption:'[ + Dataset]',tips:'$(RAD.fc.Add dataset)'});
                prf.boxing().insertRows([{
                    caption:'Dataset group',
                    group:true,
                    tips:'$(RAD.fc.Dataset group)',
                    datagroup:1,
                    sub:sub
                }],null,row.id,true);
                ns._change();
            }else if(!row.group || row.xyPlot){
                var ns=this,id=row.special||"dataset";
                ood.ComFactory.getCom('RAD.PropEditor', function(){
                    this.setProperties({
                        targetProfile:ns.properties.targetProfile,
                        propKey:"JSONData.*",

                        fc:1,
                        realData:row.DATA,
                        uiData:ns._buildTG1(id, row.DATA, FCCONF.attrCat[ns._chartType][id]),
                        labelId:"seriesName",
                        src:src
                    });
                    this.setEvents({
                        onLabelChanged:function(label){
                            prf.boxing().updateRow(row.id, {caption:label});
                        },
                        onChanged:function(){
                            ns._change();
                        }
                    });
                    this.show();
                });
            }
        },
        _gridSet2_pop:function(profile, cell, proEditor, pos, e, src){
            var ns=this,id=cell.special||cell._row.spceialcell||"set";
            if(!cell.DATA)cell.DATA={};
            ood.ComFactory.getCom('RAD.PropEditor', function(){
                this.setProperties({
                    targetProfile:ns.properties.targetProfile,
                    propKey:"JSONData.*",

                    fc:1,
                    realData:cell.DATA,
                    uiData:ns._buildTG1(id, cell.DATA, FCCONF.attrCat[ns._chartType][id]),
                    labelId:"value",
                    src:src
                });
                this.setEvents({
                    onLabelChanged:function(value){
                        proEditor.boxing().setUIValue(value);
                    },
                    onChanged:function(){
                        ns._change();
                    }
                });
                this.show();
            });
        },
        _gridset2_bcr:function (prf, threadid){
            var ns=this;
            prf.boxing().setRowOptions({
                rowRenderer:function(prf,row){
                    if(row.type!='label'&&row.type!='label2'&&!row.lineset){
                        prf.getSubNode('FCELL',row._serialId).first().prepend(new ood.UI.Span({
                            position:'relative',
                            className: 'oodcon ood-uicmd-close',
                            width:'auto',
                            height:'auto',
                            right:0,
                            bottom:0,
                            tips:'$(RAD.fc.Remove this dataset)'
                        },{
                            beforeHoverEffect:function(profile, item, e, src, type){
                                ood(src).css({'border':type=='mouseover'?'solid 1px #ccc':'',
                                            'padding':type=='mouseover'?'':'1px'});
                            },
                            onClick:function(p,e){
                                var pos=ood.Event.getPos(e);
                                ood.confirm('$RAD.Remove?',ood.getRes('RAD.fc.Are You Sure to remove this dataset')+'?',function(){
                                    prf.boxing().removeRows([row.id]);
                                    ns._change();
                                });
                                ood.Event.stopBubble(e);
                            }
                        },null,null,{KEY:"padding:1px;cursor:pointer;"}));
                        
                        if(row.xyPlot){
                            prf.getSubNode('LCELL',row._serialId).append(new ood.UI.SButton({
                                position:'relative',
                                "imageClass":"ood-uicmd-opt",
                                caption:" $(RAD.fc.Data Configuration)"
                            },{
                                onClick:function(profile, e, src){
                                    ood.ComFactory.getCom('RAD.MixPropEditor', function(){
                                        var data=ns._buildGrid('set', row.DATA2);
                                        this.setProperties({
                                            targetProfile:ns.properties.targetProfile,
                                            propKey:'xyPlot',

                                            uiHeader:data.header,
                                            uiRows:data.rows,
                                            fc:1,
                                            src:src
                                        });
                                        this.setEvents({
                                            onChanged:function(rows){
                                                row.DATA2=rows;
                                                ns._change();
                                            }
                                        });
                                        this.show();
                                    });
                                }
                            }));
                        }
                    }
                }
            }).setColOptions({
                colRenderer:function(prf,col){
                    prf.getSubNode('HCELLA',col._serialId).append(new ood.UI.Span({
                        className: 'oodcon ood-uicmd-close',
                        left:'auto',
                        top:'auto',
                        width:'auto',
                        height:'auto',
                        right:0,
                        bottom:0,
                        tips:'$(RAD.fc.Remove this category)'
                    },{
                        beforeHoverEffect:function(profile, item, e, src, type){
                            ood(src).css({'border':type=='mouseover'?'solid 1px #ccc':'',
                                        'padding':type=='mouseover'?'':'1px'});
                        },
                        onClick:function(p,e){
                            var pos=ood.Event.getPos(e);
                            ood.confirm('$RAD.Remove?',ood.getRes('RAD.fc.Are You Sure to remove this category')+'?',function(){
                                prf.boxing().removeCols([col.id]);
                                ns._change();
                            },null,null,null,ns.dialog.getRoot().cssRegion());

                            ood.Event.stopBubble(e);
                        }
                    },null,null,{KEY:"padding:1px;cursor:pointer"}));
                }
            })
        },
        _gridset2_oncr:function (prf, threadid){
            var ns=this;
            prf.boxing().getSubNode('FHCELL').first().append(new ood.UI.Span({
                className: 'oodcon ood-uicmd-add',
                left:'auto',
                top:'auto',
                width:'auto',
                height:'auto',
                right:0,
                bottom:0,
                tips:'$(RAD.fc.Add category)'
            },{
                beforeHoverEffect:function(profile, item, e, src, type){
                    ood(src).css({'border':type=='mouseover'?'solid 1px #ccc':'',
                                'padding':type=='mouseover'?'':'1px'});
                },
                onClick:function(p,e){
                    var nid=ood.id()+ood.stamp();
                    prf.boxing().insertCol({
                        id:nid,
                        type:'popbox',
                        caption:"[category]",
                        tips:'$(RAD.fc.Click to cofigure category)',
                        DATA:{
                            label:"[category]"
                        }
                    });
                    ns._change();

                    ood.asyRun(function(){
                        if(!prf||prf.destroyed)return;
                        var a=prf.getSubNode('HCELLA',true);
                        if(a=a.get(a.size()-1))a.scrollIntoView();
                    });
                    ood.Event.stopBubble(e);
                }
            },null,null,{KEY:"padding:1px;cursor:pointer"}));
        },
        _gridset2_bds:function (prf, e, src){
            var row=prf.boxing().getRowByDom(src);
            if(row && (row.type=='label' || row.type=='label2' || row.lineset))
                return false;
        },
        _gridset2_dragdata:function (prf, e, src){
            var row=prf.boxing().getRowByDom(src);
            return row.dragkey||"normal";
        },
        _gridset2_dtest:function (prf, e, src){
            if(prf.getKey(src).split('-')[1]!=="CELLS")
                return false;

            var dp=ood.DragDrop.getProfile(),
                row=prf.boxing().getRowByDom(src);
            if(ood.get(dp,['dragData','data'])!==row.dragkey){
                    return false;                
            }
        },
        _grid_ondrop:function(profile){
            var ns=this;
            ns._change();
            return false;
        }
    }
});
