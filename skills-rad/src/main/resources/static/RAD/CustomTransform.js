ood.Class('RAD.CustomTransform', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"mainPane")
                .setLeft("3.125em")
                .setTop("0em")
                .setWidth("32.666666666666664em")
                .setHeight("14.916666666666666em")
                .setShadow(false)
                .setResizer(false)
                .setCaption("$(RAD.custom_dlg.trans.Custom Transform)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
                .beforeClose("_ctl_panel4_beforeclose")
            );
            
            host.mainPane.append(
                ood.create("ood.UI.Block")
                .setHost(host,"bg")
                .setDock("fill")
                .setBorderType("inset")
                .setOverflow("hidden")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel4")
                .setLeft("0.9166666666666666em")
                .setTop("6.25em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Scale X)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel63")
                .setLeft("16.916666666666668em")
                .setTop("6.25em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Scale Y)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_lblSkewX")
                .setLeft("0.9166666666666666em")
                .setTop("8.75em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Skew X)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel6")
                .setLeft("0.9166666666666666em")
                .setTop("3.75em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Translate X)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_Rotate")
                .setDirtyMark(false)
                .setLeft("6.666666666666667em")
                .setTop("0.8333333333333334em")
                .setWidth("25em")
                .setHeight("1.875em")
                .setSteps(360)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_ScaleY")
                .setDirtyMark(false)
                .setLeft("22.25em")
                .setTop("5.916666666666667em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(200)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_ScaleX")
                .setDirtyMark(false)
                .setLeft("6.583333333333333em")
                .setTop("5.916666666666667em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(200)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel3")
                .setLeft("0.9166666666666666em")
                .setTop("1.25em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Rotate)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_lblSkewY")
                .setLeft("16.916666666666668em")
                .setTop("8.75em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Skew Y)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel65")
                .setLeft("16.916666666666668em")
                .setTop("3.75em")
                .setWidth("5.25em")
                .setCaption("$(RAD.custom_dlg.trans.Translate Y)")
                .setHAlign("left")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_SkewX")
                .setDirtyMark(false)
                .setLeft("6.583333333333333em")
                .setTop("8.416666666666666em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(360)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_SkewY")
                .setDirtyMark(false)
                .setLeft("22.25em")
                .setTop("8.416666666666666em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(360)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_TransY")
                .setDirtyMark(false)
                .setLeft("22.25em")
                .setTop("3.3333333333333335em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(200)
                .setIsRange(false)
                .setValue("0")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            host.bg.append(
                ood.create("ood.UI.Slider")
                .setHost(host,"t_TransX")
                .setDirtyMark(false)
                .setLeft("6.583333333333333em")
                .setTop("3.3333333333333335em")
                .setWidth("9.375em")
                .setHeight("1.875em")
                .setSteps(200)
                .setIsRange(false)
                .setValue("4")
                .afterUIValueSet("_t_cs_afteruivalueset")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _getValues:function(){
            var ns=this;
            return {
                rotate:parseInt(ns.t_Rotate.getValue()),
                scale:[parseInt(ns.t_ScaleX.getValue())/100,  parseInt(ns.t_ScaleY.getValue())/100],
                skew:[parseInt(ns.t_SkewX.getValue())-180, parseInt(ns.t_SkewY.getValue())-180],
                translate:[parseInt(ns.t_TransX.getValue())-100, parseInt(ns.t_TransY.getValue())-100]
            };
        },
        _t_cs_afteruivalueset : function (profile){
            ood.resetRun("border_transform",this.setBR,0,null,this);
        },
        setBR:function(){
            var ns=this,prop=ns.properties,prop;
            var values = this._getValues(),v;
            if(prop.isSvg){
                if(!(parseInt(ns.t_Rotate.getValue()) || parseInt(ns.t_ScaleX.getValue(100)) || parseInt(ns.t_ScaleY.getValue(100))
                     || parseInt(ns.t_TransX.getValue()) || parseInt(ns.t_TransY.getValue())
                    )){
                    v='';
                }else{
                    v = "r"+values.rotate+"s"+values.scale.join(',')+"t"+values.translate.join(',');
                    v = v.replace("t0,0","").replace("s1,1","").replace("r0","");
                }
            }else{
                if(!(parseInt(ns.t_Rotate.getValue()) || parseInt(ns.t_ScaleX.getValue(100)) || parseInt(ns.t_ScaleY.getValue(100))
                     || parseInt(ns.t_SkewX.getValue()) || parseInt(ns.t_SkewY.getValue()) 
                     || parseInt(ns.t_TransX.getValue()) || parseInt(ns.t_TransY.getValue())
                    )){
                    v='';
                }else{
                    v =  ((values.translate[0]||values.translate[1])?("translate("+values.translate.join('px,')+"px) "):"") + 
                          (values.rotate?("rotate("+values.rotate+"deg) "):"") +  
                          ((values.scale[0]!=1||values.scale[1]!=1)?("scale("+values.scale.join(',')+") "):"") +  
                          ((values.skew[0]||values.skew[1])?("skew("+values.skew.join('deg,')+"deg)"):"") ;
                }
            }
            this.fireEvent("onChange",[v]);
        },
        init:function(prf,tplkey,prop,type){
            var ns=this;
            ood.merge(ns.properties,prop,'all');
            if(prop.isSvg){
                ns.ctl_lblSkewX.setDisplay('none',true);
                ns.ctl_lblSkewY.setDisplay('none',true);
                ns.t_SkewX.setDisplay('none',true);
                ns.t_SkewY.setDisplay('none',true);
                
                var v=prop.cell.value||"",
                    arr=Raphael.parseTransformString(v),
                    rotate=0,sx=100,sy=100,tx=100,ty=100;
                ood.arr.each(arr,function(a){
                    switch(a[0]){
                        case 'r':
                            rotate=ood.isSet(a[1])?a[1]:0;
                        break;
                        case 's':
                            sx=(ood.isSet(a[1])?a[1]:0)*100;
                            sy=(ood.isSet(a[2])?a[2]:0)*100;
                        break;
                        case 't':
                            tx=(ood.isSet(a[1])?a[1]:0)+100;
                            ty=(ood.isSet(a[2])?a[2]:0)+100;
                        break;
                    }
                });
                ns.t_Rotate.setValue(rotate);
                ns.t_ScaleX.setValue(sx);
                ns.t_ScaleY.setValue(sy);
                ns.t_TransX.setValue(tx);
                ns.t_TransY.setValue(ty);
            }else{
                var v=type==2?tplkey:ood.get(prf.CS,[tplkey, "transform"]),
                    arr=[null,0,1,1,0,0,0,0];
                if(v){
                    var functions = (v||"").match(/[A-z]+\([^\)]+/g) || [];
                    for (var k=0; k < functions.length; k++) {
                        //Prepare the function name and its value
                        var aa=functions[k].split('('),
                            func=aa[0],
                            value=aa[1],
                            values;
                        //Now we rotate through the functions and add it to our matrix
                        switch(func) {
                            case 'rotate':
                                arr[1]=parseFloat(value);
                                break;
                            case 'scale':
                                values = value.split(',');
                                arr[2]=parseFloat(values[0]);
                                arr[3]=parseFloat(values[1]);
                                break;
                            case 'scaleX':
                                arr[2]=parseFloat(values);
                                break;
                            case 'scaleY':
                                arr[3]=parseFloat(value);
                                break;
                            case 'skew':
                                values = value.split(',');
                                arr[4]=parseFloat(values[0]);
                                arr[5]=parseFloat(values[1]);
                                break;
                            case 'skewX':
                                arr[4]=parseFloat(value);
                                break;
                            case 'skewY':
                                arr[5]=parseFloat(value);
                                break;
                            case 'translate':
                                values = value.split(',');
                                arr[6]=parseFloat(values[0]);
                                arr[7]=parseFloat(values[1]);
                                break;
                            case 'translateX':
                                arr[6]=parseFloat(value);
                                break;
                            case 'translateY':
                                arr[7]=parseFloat(value);
                                break;
                            }
                    }
                }
 
                ns.ctl_lblSkewX.setDisplay('',true);
                ns.ctl_lblSkewY.setDisplay('',true);
                ns.t_SkewX.setDisplay('',true);
                ns.t_SkewY.setDisplay('',true);
    
                arr[1]=parseFloat(arr[1]);
                arr[2]=100*parseFloat(arr[2]);
                arr[3]=100*parseFloat(arr[3]);
                arr[4]=180+parseInt(arr[4]);
                arr[5]=180+parseInt(arr[5]);
                arr[6]=100+parseInt(arr[6]);
                arr[7]=100+parseInt(arr[7]);

                ns.t_Rotate.setValue(arr[1]);
                ns.t_ScaleX.setValue(arr[2]);
                ns.t_ScaleY.setValue(arr[3]);
                ns.t_SkewX.setValue(arr[4]);
                ns.t_SkewY.setValue(arr[5]);
                ns.t_TransX.setValue(arr[6]);
                ns.t_TransY.setValue(arr[7]);
            }
        },
        _ctl_panel4_beforeclose : function (profile){
            profile.boxing().hide();
            return false;
        }
    }
});
