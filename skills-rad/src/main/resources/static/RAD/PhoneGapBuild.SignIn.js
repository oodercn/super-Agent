ood.Class('PhoneGapBuild.SignIn', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by ESDUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ctl_dialog")
                .setLeft("10.625em")
                .setTop("3.125em")
                .setWidth("27.583333333333332em")
                .setHeight("18.5em")
                .setResizer(false)
                .setCaption("$(RAD.phonegap.Build$.PhoneGap Sign In)")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
            );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"ctl_usr")
                .setDirtyMark(false)
                .setLeft("0.8333333333333334em")
                .setTop("0.75em")
                .setWidth("24.333333333333332em")
                .setHeight("2.1666666666666665em")
                .setLabelSize(120)
                .setLabelCaption("$(RAD.phonegap.Email/AdobeId)")
                .setType("input")
                .setValue("phonegap_freeplan@ESDUI.com")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"ctl_pwd")
                .setDirtyMark(false)
                .setLeft("0.8333333333333334em")
                .setTop("3.4166666666666665em")
                .setWidth("24.333333333333332em")
                .setHeight("2.1666666666666665em")
                .setLabelSize(120)
                .setLabelCaption("$RAD.phonegap.password")
                .setType("password")
                .setValue("123456abcd")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Button")
                // 在第56行左右找到并替换登录按钮的图标
                .setHost(host,"ctl_signin")
                .setLeft("10.666666666666666em")
                .setTop("6.583333333333333em")
                .setWidth("14.333333333333334em")
                .setHeight("3em")
                .setImageClass("ri-login-box-line") // 替换 "spafont spa-icon-login"
                .setCaption("$(RAD.phonegap.Sign In)")
                .onClick("_ctl_signin_onclick")
                .setCustomStyle({
                    "ICON":{
                        "font-size":"2em"
                    }
                }
                )
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block1")
                .setDock("width")
                .setDockMargin({
                    "left":6,
                    "top":0,
                    "right":6,
                    "bottom":0
                }
                )
                .setTop("10.75em")
                .setHeight("3em")
                .setHtml("<div style=\"padding:4px\"><font color=\"FF0000\"><b>Strongly recommended to use your own account.</b><br>You can <font color=\"FF0000\">create account at http://build.phonegap.com.</font></font></div>")
                .setBorderType("groove")
                .setOverflow("hidden")
                );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Link")
                .setHost(host,"ctl_link177")
                .setLeft("7.916666666666667em")
                .setTop("14.083333333333334em")
                .setCaption("$(RAD.phonegap.You can use build$.phonegap website too)")
                .onClick("_ctl_link1_onclick")
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            var ns=this;
            // try to get phonegap usr/pwd

            this.ctl_dialog.showModal();
            return true;
        },
        _ctl_signin_onclick:function (profile, e, src, value){
            var ns = this, uictrl = ns.ctl_dialog,
                usr=ns.ctl_usr.getValue(),
                pwd=ns.ctl_pwd.getValue();
            uictrl.busy(ood.adjustRes("$(RAD.phonegap.Try to sign in)..."));
            
            PhoneGapBuild.tryToSignIn(function(e, api){
                uictrl.free("");

                if(!e){          

                    
                    ood.tryF(ns._callback);
                    ns.destroy();
                }else{
                    ood.alert(e.message);
                }
            },usr, pwd, false);
        },
        showManul:function(){
            ood.ComFactory.getCom("MobileInstruction",function(){
                this.dlg.showModal();
            });
        },
        _ctl_link1_onclick:function (profile, e){
            this.showManul();
        }
    }
});
