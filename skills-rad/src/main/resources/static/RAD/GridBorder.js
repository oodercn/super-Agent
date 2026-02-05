ood.Class('RAD.GridBorder', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Block")
                .setHost(host,"mainPane")
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("19.166666666666668em")
                .setHeight("12em")
                .setShadow(true)
                .setBorderType("flat")
                .setOverflow("hidden")
            );
            
            host.mainPane.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"b_style")
                .setDirtyMark(false)
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("11.666666666666666em")
                .setDisplay("none")
                .setLabelSize("5em")
                .setLabelGap("auto")
                .setLabelCaption("$RAD.custom_dlg.borderdlg.Style")
                .setLabelHAlign("")
                .setHAlign("center")
                .setType("listbox")
                .setItems([
                    {
                        "id":"solid",
                        "caption":"$RAD.custom_dlg.borderdlg.solid"
                    },
                    {
                        "id":"dashed",
                        "caption":"$RAD.custom_dlg.borderdlg.dashed"
                    },
                    {
                        "id":"dotted",
                        "caption":"$RAD.custom_dlg.borderdlg.dotted"
                    },
                    {
                        "id":"double",
                        "caption":"$RAD.custom_dlg.borderdlg.double"
                    }
                ])
                .setShowMode("compact")
                .setValue("solid")
                .afterPopShow("_afterPop")
                );
            
            host.mainPane.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"b_bold")
                .setDirtyMark(false)
                .setLeft("0.8333333333333334em")
                .setTop("4.166666666666667em")
                .setWidth("11.666666666666666em")
                .setDisplay("none")
                .setLabelSize("5em")
                .setLabelGap("auto")
                .setLabelCaption("$RAD.custom_dlg.borderdlg.Width")
                .setLabelHAlign("left")
                .setType("counter")
                .setShowMode("compact")
                .setPrecision(0)
                .setIncrement(1)
                .setMin(1)
                .setMax(10)
                .setValue(1)
                );
            
            host.mainPane.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"b_color")
                .setDirtyMark(false)
                .setLeft("1.6666666666666667em")
                .setTop("0.5em")
                .setWidth("15.833333333333334em")
                .setLabelSize("5em")
                .setLabelGap("auto")
                .setLabelCaption("$RAD.custom_dlg.borderdlg.Color")
                .setLabelHAlign("left")
                .setType("color")
                .setShowMode("compact")
                .setValue("#000000")
                .afterPopShow("_afterPop")
                );
            
            host.mainPane.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_block7")
                .setLeft("0.8333333333333334em")
                .setTop("2.8333333333333335em")
                .setWidth("17.5em")
                .setHeight("8.333333333333334em")
                .setBorderType("flat")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_all")
                .setClassName("ood-css-cyz")
                .setLeft("0.8333333333333334em")
                .setTop("0.5em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("all")
                .onClick("_btn_all_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_inner")
                .setClassName("ood-css-cyz")
                .setLeft("9.166666666666666em")
                .setTop("0.5em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("inner")
                .onClick("_btn_inner_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_outer")
                .setClassName("ood-css-cyz")
                .setLeft("5em")
                .setTop("0.5em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("outer")
                .onClick("_btn_outer_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_none")
                .setClassName("ood-css-cyz")
                .setLeft("13.333333333333334em")
                .setTop("0.5em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("none")
                .onClick("_btn_none_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_left")
                .setClassName("ood-css-cyz")
                .setLeft("0.8333333333333334em")
                .setTop("4.416666666666667em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("left")
                .onClick("_btn_left_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_top")
                .setClassName("ood-css-cyz")
                .setLeft("9.166666666666666em")
                .setTop("4.416666666666667em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("top")
                .onClick("_btn_top_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_right")
                .setClassName("ood-css-cyz")
                .setLeft("5em")
                .setTop("4.416666666666667em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("right")
                .onClick("_btn_right_onclick")
                );
            
            host.ood_ui_block7.append(
                ood.create("ood.UI.HTMLButton")
                .setHost(host,"btn_bottom")
                .setClassName("ood-css-cyz")
                .setLeft("13.333333333333334em")
                .setTop("4.416666666666667em")
                .setWidth("3.25em")
                .setHeight("3.25em")
                .setCaption("bottom")
                .onClick("_btn_bottom_onclick")
                );
            
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host,"ood_ui_cssbox1")
                .setClassName("ood-css-cyz")
                .setNormalStatus({
                    "color":"#222222",
                    "background-color":"#F4F5F5",
                    "background-image":"none",
                    "border-top":"none",
                    "border-right":"none",
                    "border-bottom":"none",
                    "border-left":"none",
                    "border-radius":"6px",
                    "text-shadow":"0 -1px 0 #ffffff",
                    "box-shadow":"none",
                    "cursor":"pointer"
                })
            );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        events:{
            "onReady":"_page_onready"
        },
        _afterPop:function(profile, drop){
            drop.$parentPopMenu = profile.$parentPopMenu;
        },
        _page_onready:function(module, threadid){
            this.fillBtns();
        },
        fillBtns:function(){
            var ns=this,
                makeTable = function(h1,h2,h3,v1,v2,v3){
                    var s = 'width:1em;height:1em;', solid = ':solid 1px #000000;', shadow = ':solid 1px #ccc;';
                    v1 = 'border-left' + (v1?solid:shadow);
                    v2 = 'border-right' + (v2?solid:shadow);
                    v3 = 'border-right' + (v3?solid:shadow);
                    h1 = 'border-top' + (h1?solid:shadow);
                    h2 = 'border-bottom' + (h2?solid:shadow);
                    h3 = 'border-bottom' + (h3?solid:shadow);
                    return '<table style="border-collapse:collapse"><tbody><tr>'+
                        '<td style="'+s+v1+v2+h1+h2+'"></td>'+
                        '<td style="'+s+v3+h1+h2+'"></td>'+ '</tr><tr style="">'+
                        '<td style="'+s+v1+v2+h3+'"></td>'+ 
                        '<td style="'+s+v3+h3+'"></td>'+ '</tr></tbody></table>';
                };
            ns.btn_all.setCaption(makeTable(1,1,1,1,1,1));
            ns.btn_outer.setCaption(makeTable(1,0,1,1,0,1));
            ns.btn_inner.setCaption(makeTable(0,1,0,0,1,0));
            ns.btn_none.setCaption(makeTable());
            ns.btn_left.setCaption(makeTable(0,0,0,1,0,0));
            ns.btn_right.setCaption(makeTable(0,0,0,0,0,1));
            ns.btn_top.setCaption(makeTable(1));
            ns.btn_bottom.setCaption(makeTable(0,0,1,0,0,0));
        },
        _btn_all_onclick:function(profile, e, src){
            this.selectBorder("all");
        },
        _btn_outer_onclick:function(profile, e, src){
            this.selectBorder("outer");
        },
        _btn_inner_onclick:function(profile, e, src){
            this.selectBorder("inner");
        },
        _btn_none_onclick:function(profile, e, src){
            this.selectBorder("none");
        },
        _btn_left_onclick:function(profile, e, src){
            this.selectBorder("left");
        },
        _btn_right_onclick:function(profile, e, src){
            this.selectBorder("right");
        },
        _btn_top_onclick:function(profile, e, src){
            this.selectBorder("top");
        },
        _btn_bottom_onclick:function(profile, e, src){
            this.selectBorder("bottom");
        },
        selectBorder:function(type){
            var ns=this,
                style = ns.b_style.getUIValue(),
                width = ns.b_bold.getUIValue(),
                color = ns.b_color.getUIValue();
            ns.fireEvent("onApplyBorder", [type,style,width,color], ns);
        }
    }
});
