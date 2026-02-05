ood.Class('RAD.TemplatePreview', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Div")
                .setHost(host,"blkMain")
                .setDock('none')
                .setDockIgnore(false)
                .setDockFloat(true)
                .setShowEffects("Classic")
                .setHideEffects("Classic")
                .setLeft(0)
                .setTop(0)
                .setWidth(130)
                .setHeight(0)
                .setOverflow("visible")
            );
            
            host.blkMain.append(
                ood.create("ood.UI.Div")
                .setHost(host,"blkMain2")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("10.833333333333334em")
                .setHeight("0em")
                .setZIndex(1002)
                .setOverflow("visible")
                );
            
            host.blkMain2.append(
                ood.create("ood.UI.Image")
                .setHost(host,"ctl_image45")
                .setClassName("ood-css-kf")
                .setTips("$RAD.tpl.Apply")
                .setTop("-2.6666666666666665em")
                .setRight("0em")
                .setSrc("{ood.ini.releasePath}img/copy.png")
                .setCursor("")
                .onClick("_btn_apply_onclick")
                );
            
            host.blkMain2.append(
                ood.create("ood.UI.Image")
                .setHost(host,"ctl_image46")
                .setClassName("ood-css-kf")
                .setTips("$RAD.tpl.Preview")
                .setLeft("0em")
                .setTop("-2.6666666666666665em")
                .setSrc("{ood.ini.releasePath}img/preview.png")
                .setCursor("")
                .onClick("_btn_preview_onclick")
                );
            
            append(
                ood.create("ood.UI.CSSBox")
                .setHost(host,"ctl_cssbox2")
                .setClassName("ood-css-kf")

                .setNormalStatus({
                    "background-color":"#FFFFFF",
                    "border-radius":"16px",
                    "box-shadow":"0px 0px 6px #4B0082",
                    "cursor":"pointer"
                }
                )
                .setHoverStatus({
                    "box-shadow":"1px 1px 10px #4B0082",
                    "transform":"rotate(0deg) scale(1,1) skew(0deg,0deg) translate(-1px,-1px)"
                }
                )
                .setActiveStatus({
                    "box-shadow":"0px 0px 6px #4B0082",
                    "transform":"rotate(0deg) scale(0.99,0.99) skew(0deg,0deg) translate(0px,0px)"
                }
                )
            );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            return true;
        },
        initPage:function(item){
            if(!item)return;
            var ns=this,
                id=item.id,
                prop=item.prop;
                ns._item=item;
        },
        init:function(item,items,prop){
            var ns=this;            
            ns._items=items;
            ns.properties=ood.copy(prop);
            ns._maxed=0;
            ns.initPage(item);
        },
        isStopHover:function(){
            return this._maxed;
        },
        _btn_preview_onclick:function (profile, e, src){
            var ns = this;
                ood.Dom.submit(ns._previewURL);
        },
        _apply:function(item){
            var ns=this;
            if(item)ns._item=item;

            ns._btn_apply_onclick();
        },
         _btn_apply_onclick:function (){
            var ns = this,
                prop=ns.properties,
                fun=function(){
                    ood.showCom("RAD.AddProject",function(){
                        this.setProperties(null);
                        this.setProperties({
                            fileName:ns._item['name'].replace(/^[\w]+_tpl(code)?_/,""),
                            tempName:ns._item.name,
                            item:ns._item,
                            prop:ns.prop
                        });
                        this.setEvents({
                            onOK:function(){
                                ns._item=ns._items=null;
                                ns.blkMain.hide();
                                ns.fireEvent('onOK', ood.toArr(arguments),this);
                            }
                        });
                    },null,null,true);
                    };
                fun();
//            }
        }
    }
});
