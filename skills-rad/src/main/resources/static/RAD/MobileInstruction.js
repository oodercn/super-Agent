ood.Class('RAD.MobileInstruction', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dlg")
                .setLeft("8.125em")
                .setTop("5em")
                .setWidth("46em")
                .setHeight("29.916666666666668em")
                .setResizer(false)
                .setCaption("Mobile Deploy Instruction")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setOverflow("hidden")
                .beforeClose("_dlg_beforeclose")
            );
            
            host.dlg.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_div11")
                .setLeft("0.8125em")
                .setTop("0.3125em")
                .setWidth("43.916666666666664em")
                .setHeight("22.75em")
                .onRender("_ondivr")
                .setCustomStyle({
                    "KEY":"background-color:#FFFFF0;border:solid 1px #ababab;overflow:auto;"
                }
                )
                );
            
            host.dlg.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_sbutton5")
                .setLeft("19.916666666666668em")
                .setTop("23.833333333333332em")
                .setWidth("8.125em")
                .setHeight("2.5em")
                .setCaption("O K")
                .onClick("_ctl_sbutton5_onclick")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _ctl_sbutton5_onclick : function (profile, e, src, value){
            this.dlg.close();
        },
        _dlg_beforeclose : function (profile){
            this.dlg.hide();
            return false;
        },
        _ondivr:function(profile){
            profile.boxing().setHtml("<font face=\"Arial\"><br>&nbsp; <font size=\"2\">Instruction for Mobile Application Deploy:<br></font></font><ol><li><font face=\"Arial\" size=\"2\"><b>Create </b>\"Web deploy package\"(zip file) from the EUSUI RAD Studio;<br><img src=\""
            +ood.getPath('img/','BuildWeb.png')
            +"\"></font></li><li><font face=\"Arial\" size=\"2\"><b>Upload </b>the zip file to <a target=\"_blank\" href=\"https://build.phonegap.com/\">PhoneGap</a>;</font></li><li><font face=\"Arial\" size=\"2\"><b>Build </b>it in PhoneGap;<br><img src=\""
            +ood.getPath('img/','PhoneGap.png')
            +"\"></font></li><li><font face=\"Arial\" size=\"2\"><b>Download </b>output files from PhoneGap's \"Public Page\"(like <a target=\"_blank\" href=\"https://build.phonegap.com/apps/359497/share\">this</a>)</font></li></ol>")
        }

    }
});
