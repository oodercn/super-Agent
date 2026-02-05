ood.Class('RAD.CustomArrows', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"mainPane")
                .setLeft("0.625em")
                .setTop("1.25em")
                .setWidth("41.833333333333336em")
                .setHeight("28.666666666666668em")
                .setShadow(false)
                .setResizer(false)
                .setCaption("$(RAD.custom_dlg.arrow.Custom Arrows)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .beforeClose("_mainPane_beforeclose")
            );
            
            host.mainPane.append(
                ood.create("ood.UI.Block")
                .setHost(host,"bg")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block63")
                .setDock("fill")
                .setBorderType("none")
                .setOverflow("hidden")
                );
            
            host.ctl_block63.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ctl_panel3")
                .setDock("none")
                .setLeft("0.8333333333333334em")
                .setTop("3.125em")
                .setWidth("19.166666666666668em")
                .setHeight("21.75em")
                .setOverflow("hidden")
                .setCaption("$(RAD.custom_dlg.arrow.Arrow Start)")
                );
            
            host.ctl_panel3.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group142")
                .setLeft("0.6875em")
                .setTop("0.6666666666666666em")
                .setWidth("7.75em")
                .setHeight("17.333333333333332em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Type")
                );
            
            host.ctl_group142.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_type")
                .setDirtyMark(false)
                .setItems([{
                    "id":"none",
                    "caption":"$RAD.custom_dlg.arrow.none"
                },
                {
                    "id":"classic",
                    "caption":"$RAD.custom_dlg.arrow.classic"
                },
                {
                    "id":"block",
                    "caption":"$RAD.custom_dlg.arrow.block"
                },
                {
                    "id":"open",
                    "caption":"$RAD.custom_dlg.arrow.open"
                },
                {
                    "id":"oval",
                    "caption":"$RAD.custom_dlg.arrow.oval"
                },
                {
                    "id":"diamond",
                    "caption":"$RAD.custom_dlg.arrow.diamond"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("5.625em")
                .setValue("none")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_panel3.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group143")
                .setLeft("9.083333333333334em")
                .setTop("0.6666666666666666em")
                .setWidth("8.333333333333334em")
                .setHeight("8.333333333333334em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Width")
                );
            
            host.ctl_group143.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_width")
                .setDirtyMark(false)
                .setItems([{
                    "id":"narrow",
                    "caption":"$RAD.custom_dlg.arrow.narrow"
                },
                {
                    "id":"midium",
                    "caption":"$RAD.custom_dlg.arrow.midium"
                },
                {
                    "id":"wide",
                    "caption":"$RAD.custom_dlg.arrow.wide"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("5.625em")
                .setHeight("5em")
                .setValue("narrow")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_panel3.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group144")
                .setLeft("9.083333333333334em")
                .setTop("9.666666666666666em")
                .setWidth("8.333333333333334em")
                .setHeight("8.333333333333334em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Length")
                );
            
            host.ctl_group144.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_length")
                .setDirtyMark(false)
                .setItems([{
                    "id":"short",
                    "caption":"$RAD.custom_dlg.arrow.short"
                },
                {
                    "id":"midium",
                    "caption":"$RAD.custom_dlg.arrow.midium"
                },
                {
                    "id":"long",
                    "caption":"$RAD.custom_dlg.arrow.long"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("5.625em")
                .setHeight("5em")
                .setValue("short")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_block63.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ctl_panel4")
                .setDock("none")
                .setLeft("20.583333333333332em")
                .setTop("3.125em")
                .setWidth("19.416666666666668em")
                .setHeight("21.75em")
                .setOverflow("hidden")
                .setCaption("$(RAD.custom_dlg.arrow.Arrow End)")
                );
            
            host.ctl_panel4.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_gtype2")
                .setLeft("0.6875em")
                .setTop("0.6666666666666666em")
                .setWidth("8.416666666666666em")
                .setHeight("17.333333333333332em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Type")
                );
            
            host.ctl_gtype2.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_type2")
                .setDirtyMark(false)
                .setItems([{
                    "id":"none",
                    "caption":"$RAD.custom_dlg.arrow.none"
                },
                {
                    "id":"classic",
                    "caption":"$RAD.custom_dlg.arrow.classic"
                },
                {
                    "id":"block",
                    "caption":"$RAD.custom_dlg.arrow.block"
                },
                {
                    "id":"open",
                    "caption":"$RAD.custom_dlg.arrow.open"
                },
                {
                    "id":"oval",
                    "caption":"$RAD.custom_dlg.arrow.oval"
                },
                {
                    "id":"diamond",
                    "caption":"$RAD.custom_dlg.arrow.diamond"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("5.625em")
                .setValue("none")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_panel4.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group467")
                .setLeft("9.583333333333334em")
                .setTop("0.6666666666666666em")
                .setWidth("8.333333333333334em")
                .setHeight("8.333333333333334em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Width")
                );
            
            host.ctl_group467.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_width2")
                .setDirtyMark(false)
                .setItems([{
                    "id":"narrow",
                    "caption":"$RAD.custom_dlg.arrow.narrow"
                },
                {
                    "id":"midium",
                    "caption":"$RAD.custom_dlg.arrow.midium"
                },
                {
                    "id":"wide",
                    "caption":"$RAD.custom_dlg.arrow.wide"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("6.25em")
                .setHeight("5em")
                .setValue("narrow")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_panel4.append(
                ood.create("ood.UI.Group")
                .setHost(host,"ctl_group468")
                .setLeft("9.583333333333334em")
                .setTop("9.666666666666666em")
                .setWidth("8.333333333333334em")
                .setHeight("8.333333333333334em")
                .setOverflow("hidden")
                .setCaption("$RAD.custom_dlg.arrow.Length")
                );
            
            host.ctl_group468.append(
                ood.create("ood.UI.RadioBox")
                .setHost(host,"ctl_length2")
                .setDirtyMark(false)
                .setItems([{
                    "id":"short",
                    "caption":"$RAD.custom_dlg.arrow.short"
                },
                {
                    "id":"midium",
                    "caption":"$RAD.custom_dlg.arrow.midium"
                },
                {
                    "id":"long",
                    "caption":"$RAD.custom_dlg.arrow.long"
                }])
                .setDock("fill")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("5.625em")
                .setHeight("5em")
                .setValue("short")
                .afterUIValueSet("_ctl_afteruivalueset")
                .setCustomStyle({
                    "ITEM":"display:block"
                }
                )
                );
            
            host.ctl_block63.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block64")
                .setLeft("5em")
                .setTop("0.3333333333333333em")
                .setWidth("30.833333333333332em")
                .setHeight("2.25em")
                .setBorderType("flat")
                .setBackground("#fff")
                .setOverflow("hidden")
                );
            
            host.ctl_block64.append(
                ood.create("ood.UI.SVGPaper")
                .setHost(host,"ctl_svgpaper80")
                .setLeft("0.625em")
                .setTop("0.125em")
                .setWidth("28.5em")
                .setHeight("1.75em")
                );
            
            host.ctl_svgpaper80.append(
                ood.create("ood.svg.path")
                .setHost(host,"ctl_line")
                .setSvgTag("Shapes:Line")
                .setAttr({
                    "path":"M,17,10L,327,10",
                    "stroke":"#bf852f",
                    "stroke-width":3
                }
                )
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _getValues:function(){
            var ns = this,
                type1 = ns.ctl_type.getUIValue(),
                width1 = ns.ctl_width.getUIValue(),
                length1 = ns.ctl_length.getUIValue(),
                type2 = ns.ctl_type2.getUIValue(),
                width2 = ns.ctl_width2.getUIValue(),
                length2 = ns.ctl_length2.getUIValue();
            return [type1 + "-" + width1 + "-" + length1, type2 + "-" + width2 + "-" + length2];
        },
        _ctl_afteruivalueset:function (profile){
            var ns=this,
                v=ns._getValues();
            ns.ctl_line.setAttr("KEY",{
                "arrow-start": v[0] ,
                "arrow-end": v[1]
            },false);
            ns.fireEvent("onChange",[v]);
        },
        init:function(prf,tplkey,prop){
            var ns=this,grid=prop.grid, cell;
            ood.merge(ns.properties,prop,'all');            

            cell=grid.getCellbyRowCol("arrow-start","value");
            if(cell){
                var start=cell.value&&cell.value.split('-');
                ns.ctl_type.setValue(start[0]||"none",true);
                ns.ctl_width.setValue(start[1]||"narrow",true);
                ns.ctl_length.setValue(start[2]||"short",true);
            }

            cell=grid.getCellbyRowCol("arrow-end","value");
            if(cell){
                var end=cell.value&&cell.value.split('-');
                ns.ctl_type2.setValue(end[0]||"none",true);
                ns.ctl_width2.setValue(end[1]||"narrow",true);
                ns.ctl_length2.setValue(end[2]||"short",true);
            }
            
            var v=ns._getValues();
            ns.ctl_line.setAttr("KEY",{
                "arrow-start": v[0] ,
                "arrow-end": v[1]
            },false);
        },
        _mainPane_beforeclose:function (profile){
            profile.boxing().hide();
            return false;
        }
    }
});
