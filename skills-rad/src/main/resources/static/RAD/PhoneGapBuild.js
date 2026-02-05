ood.Class('PhoneGapBuild', 'ood.Module',{
    Static:{
        token : null,
        api : null,
        maxRetryTimes:10,
        url : '/apps',
        platforms:[ 'ios', 'android', 'winphone'/*, 'blackberry', 'symbian', 'webos'*/],
        
        tryToSignIn:function(callback, user, pwd, autoShowLogIn){
            var client = require('phonegap-build-api'),
                options={};
            var ns=PhoneGapBuild;
            if(user && pwd){
                options.username=user;
                options.password=pwd;
            }else if(ns.token){
                options.token=ns.token;
            }else{
                // have to use UI to login
                if(autoShowLogIn!==false){
                    ood.ComFactory.newCom("PhoneGapBuild.SignIn",function(){
                        this._callback=callback;
                        this.show();
                    });
                }
                return ;
            }
            
            process.env['NODE_TLS_REJECT_UNAUTHORIZED'] = '0'
            
            client.auth(options, function(e, api) {
                if(e){
                    if(autoShowLogIn!==false){
                        // login again
                        ood.ComFactory.newCom("PhoneGapBuild.SignIn",function(){
                            this._callback=callback;
                            this.show();
                        });
                    }
                }else{
                    ns.token=api.token;
                    ns.api=api;
                }
                callback(e, api);
            });
        },
        getApps:function(callback, onMessage){
            var ns=PhoneGapBuild,
                client = require('phonegap-build-api'),
                options={};
            onMessage("Getting existing applications in phonegap build ...");
            ns.api.get(ns.url,function(e, data) {
                if(e){
                    ood.alert(e.message);
                }else{
                    onMessage("Got " + data.apps.length + " existing applications.");
                    callback(data);
                }
            });
        },
        getAppBuildStatus:function(phonegapId, platforms, onMessage, onResult, onFinish, retry){
            var ns=PhoneGapBuild, self=arguments.callee;
            if(!retry)retry = ns.maxRetryTimes;
            var urlId = ns.url + "/" + phonegapId;
            
            onMessage("Getting status of " + urlId + " ...");
            
            ns.api.get(urlId, function(e, data) {
                if(e){
                    onMessage(urlId +" returns error : "+ e.message);
                    ood.tryF(onFinish);
                    return;
                }
                var tryAgain;
                for(var i=0,platform;platform=platforms[i++];){
                    var urlpf=urlId + "/" + platform,
                        status = data.status[platform];
                    if (status === 'error') {
                        onMessage(urlpf + " raises error : " + data.error[platform]);
                    }else if (status === 'pending') {
                        onMessage(urlpf + " is pending.");
                        tryAgain=true;
                    }else if (status === 'complete') {
                        onMessage(urlpf + " is OK.");
                    }else {
                        onMessage(urlpf + " is " + status + ". " );
                    }
                }
                
                onResult(data);
                
                if(tryAgain){
                    if((retry++)<=ns.maxRetryTimes){
                        ood.setTimeout(function(){
                            self.apply(ns, [phonegapId, platforms, onMessage, onResult, onFinish, retry]);
                        },5000);
                    }else{
                        onMessage(url +" returns error : retried "+ ns.maxRetryTimes +" time.");
                        ood.tryF(onFinish);
                    }
                }else{
                    ood.tryF(onFinish);
                }
            });
        },
        build:function(phonegapid, properties, platforms,onMessage,onResult,onFinish,onStart, onCancel){
            var ns=PhoneGapBuild,
                zipTmpPath = CONF.mapServerPath("_phonegap.zip"),
                appName="EUSUI Application";
            ood.showCom("RAD.CustomPackage",function(){
                this.setEvents("onOK",function(lib,appearance,locale){
                    var paras={
                        files_lib:lib,
                        files_appearance:appearance,
                        files_locale:locale
                    };
                    ood.merge(paras,properties);
                    

                    NWUtil.ensureConfigAndIcon(properties.path, function(){
                        var zip = NWUtil.zipSimple(paras),            
                            fs = require('fs'),
                            headers = phonegapid?{
                                form: {
                                    file: zipTmpPath
                                }                
                            }:{
                                form: {
                                    data: {
                                        title: appName,
                                        create_method: 'file'
                                    },
                                    file: zipTmpPath
                                }
                            };
                        zip.addLocalFile(properties.path+"/.settings/phonegap/config.xml");
                        if(fs.existsSync(properties.path+"/.settings/phonegap/res/")){
                            zip.addLocalFolder(properties.path+"/.settings/phonegap/res/", "res/");
                        }
                        zip.toBuffer(function(buffer){
                            onMessage('Uploading package ...');
                            fs.writeFileSync(zipTmpPath, buffer);
                           
                            ns.api[phonegapid?"put":"post"](phonegapid?(ns.url+"/"+phonegapid):ns.url, headers, function(e, response) {
                                // remove temp zip file
                                if(fs.existsSync(zipTmpPath)){
                                    fs.unlink(zipTmpPath);
                                }
                                if (e) {
                                    onMessage('Failed to upload: ' + e.message);
                                    onFinish();
                                    
                                    if(/limit reached/.test(e.message)){
                                        ood.alert(ood.adjustRes("$(RAD.phonegap.Private app limit reached, maybe you need to delete an app first!<br/> Or upload current project to an existing App)"));
                                    }
                                    
                                    return;
                                }
                                
                                ood.tryF(onStart);
                                
                                onMessage('Uploaded!');
                                onMessage('Building...');
                                
                                var options = {
                                    form: {
                                        data: {
                                            platforms: ns.platforms
                                        }
                                    }
                                };
                                ns.api.post(ns.url + "/" + response.id + "/build", options, function(e, data) {
                                    if (e) {
                                        onMessage('Build error: ' + e.message);
                                        onFinish();
                                        return;
                                    }
                                    
                                    onMessage('Download checking ...');
                                    
                                    ns.getAppBuildStatus(response.id, platforms, onMessage, onResult, onFinish, 1);
                                });
                            });
                        });
                    }, onMessage);
                });
                this.setEvents("onCancel",onCancel);
            });
        }
    },
    Instance:{
        iniComponents:function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ctl_dialog")
                .setLeft("0em")
                .setTop("0em")
                .setWidth("56.583333333333336em")
                .setHeight("49.916666666666664em")
                .setResizer(false)
                .setCaption("$(RAD.phonegap.OOD PhoneGap Builder)")
                .setImageClass("ri ri-smartphone-line")
                .setMinBtn(false)
            );
            
            host.ctl_dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block10")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.ctl_block10.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block11")
                .setDock("top")
                .setHeight("4.083333333333333em")
                );
            
            host.ctl_block11.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_btn_new")
                .setDock("width")
                .setDockMargin({
                    "left":10,
                    "top":0,
                    "right":10,
                    "bottom":0
                }
                )
                .setTop("0.375em")
                .setHeight("3.25em")
                .setImageClass("ri ri-settings-3-line")
                .setCaption("$(RAD.phonegap.Add the current project to build$.phonegap  as a new private application, and build it)")
                .onClick("_ctl_btn_new_onclick")
                .setCustomStyle({
                    "ICON":{
                        "font-size":"2em"
                    }
                }
                );
            
            host.ctl_block10.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"ctl_layout4")
                    .setType("vertical")
                .setItems([{
                    "id":"before",
                    "pos":"before",
                    "size":180,
                    "min":10,
                    "locked":false,
                    "folded":false,
                    "hidden":false,
                    "cmd":false
                },
                {
                    "id":"main",
                    "size":80,
                    "min":10
                },
                {
                    "id":"after",
                    "pos":"after",
                    "size":180,
                    "min":10,
                    "locked":false,
                    "folded":false,
                    "hidden":false,
                    "cmd":true
                }])
                .setCustomStyle({
                    "PANEL":{}
                }
                )
                .setCustomClass({
                    "PANEL":"loading"
                }
                )
                );
            
            host.ctl_layout4.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ctl_log")
                .setZIndex(1)
                .setOverflow("overflow-x:hidden;overflow-y:auto;")
                .setCaption("$RAD.phonegap.Log")
                .setCustomStyle({
                    "PANEL":{
                        "background-color":"#FFFFFF"
                    }
                }
                )
                , "after");
            
            host.ctl_log.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_div71")
                .setDomId("phonegap_log_div")
                .setDock("fill")
                );
            
            host.ctl_layout4.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ctl_list")
                .setZIndex(1)
                .setCaption("$(RAD.phonegap.Existing Applications in Build$.PhoneGap)")
                .setRefreshBtn(true)
                .onRefresh("_refreshList")
                .setCustomStyle({
                    "PANEL":{}
                }
                )
                .setCustomClass({
                    "PANEL":"loading"
                }
                )
                , "before");
            
            host.ctl_list.append(
                ood.create("ood.UI.TreeGrid")
                .setHost(host,"ctl_tglist")
                .setSelMode("single")
                .setRowNumbered(true)
                .setRowHandlerWidth("1.25em")
                .setHeader([{
                    "id":"appid",
                    "caption":"$(RAD.phonegap.App ID)",
                    "width":"5em",
                    "type":"input"
                },
                {
                    "id":"name",
                    "caption":"$(RAD.phonegap.Title)",
                    "width":"10em",
                    "type":"input"
                },
                {
                    "id":"package",
                    "caption":"$(RAD.phonegap.Package)",
                    "width":"6.666666666666667em",
                    "type":"input"
                },
                {
                    "id":"version",
                    "caption":"$(RAD.phonegap.Version)",
                    "width":"6.666666666666667em",
                    "type":"input"
                },
                {
                    "id":"phonegap_version",
                    "caption":"$(RAD.phonegap.Phonegap Version)",
                    "width":"6.666666666666667em",
                    "type":"input"
                },
                {
                    "id":"description",
                    "caption":"$(RAD.phonegap.Description)",
                    "width":"6.666666666666667em",
                    "type":"input",
                    "flexSize":true
                }])
                .setTreeMode('none')
                .afterUIValueSet("_ctl_tglist_afteruivalueset")
                );
            
            host.ctl_list.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block312")
                .setDock("bottom")
                .setHeight("3.5em")
                .setBorderType("none")
                .setOverflow("hidden")
                );
            
            host.ctl_block312.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_update")
                .setDisabled(true)
                .setTop("0.3125em")
                .setWidth("28em")
                .setHeight("2.9166666666666665em")
                .setRight("0.625em")
                .setZIndex(2)
                .setImageClass("ri ri-upload-line")
                .setCaption("$(RAD.phonegap.Upload current project to the selected existing App)")
                .onClick("_ctl_btn_update_onclick")
                .setCustomStyle({
                    "ICON":{
                        "font-size":"2em"
                    }
                }
                )
                );
            
            host.ctl_block312.append(
                ood.create("ood.UI.Button")
                .setHost(host,"ctl_delete")
                .setDisabled(true)
                .setLeft("0.625em")
                .setTop("0.3125em")
                .setWidth("19.25em")
                .setHeight("2.9166666666666665em")
                .setImageClass('ood-uicmd-delete')
                .setCaption("$(RAD.phonegap.Delete the selected existing App)")
                .onClick("_ctl_btn_config_onclick")
                .setCustomStyle({
                    "ICON":{
                        "font-size":"2em"
                    }
                }
                )
                );
            
            host.ctl_layout4.append(
                ood.create("ood.UI.Panel")
                .setHost(host,"ctl_builds")
                .setZIndex(1)
                .setCaption("$(RAD.phonegap.Build results)")
                .setRefreshBtn(true)
                .onRefresh("_onrefreshBuild")
                , "main");
            
            host.ctl_builds.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block313")
                .setDock("fill")
                .setBorderType("none")
                );
            
            host.ctl_block313.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_result")
                .setDock("fill")
                .setBorderType("none")
                .setBackground("#fff")
                .setCustomStyle({
                    "PANEL":"text-align:center;padding-top:16px;"
                }
                )
                );
            
            host.ctl_block313.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block1233")
                .setDock("bottom")
                .setHeight("2em")
                );
            
            host.ctl_block1233.append(
                ood.create("ood.UI.Link")
                .setHost(host,"ctl_link181")
                .setTop("0.3333333333333333em")
                .setRight("0.625em")
                .setCaption("$(RAD.phonegap.Please go to build$.phonegap website to do advanced build)")
                .onClick("_ctl_link181_onclick")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        customAppend : function(parent, subId, left, top){
            var ns=this;
            PhoneGapBuild.tryToSignIn(function(e,api){
                if(!e){
                    ns.ctl_dialog.showModal();
                    
                    ns._refreshList();
                }
            });
            return true;
        },
        onMessage:function(msg){
            var ns=this,
                logNode=ood('phonegap_log_div');
            
            logNode.prepend("<"+"div>" + ood.Date.format(new Date,"yyyy-mm-dd hh:nn:ss") + " : " + msg + "<"+"/div"+">");
        },
        _refreshList:function (){
            var ns = this;
            ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Loading)..."));
            PhoneGapBuild.getApps(function(data){
                ns.ctl_dialog.free();
                
                var rows=[];
                ood.arr.each(data.apps,function(app){
                    rows.push({
                        id:app.id, 
                        title:app.title,
                        cells:[app.id, app.title, app["package"], app.version, app.phonegap_version, app.description]
                    });
                });
                
                ns.ctl_tglist.setRows(rows);
                
                ns.ctl_result.setHtml('',true);
            }, ns.onMessage);
        },
        _ctl_tglist_afteruivalueset:function (profile, oldValue, newValue){
            var ns = this;
            ns._refreshBuild(newValue);
        },
        _onrefreshBuild:function(){
            var ns=this;
            if(!ns._selectedApp){
                ood.alert(ood.adjustRes("$(RAD.phonegap.Select An Application please)!"));
                return ;
            }
            ns._refreshBuild(ns._selectedApp);
        },
        _refreshBuild:function (phonegapid){
            var ns = this;

            ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
            PhoneGapBuild.getAppBuildStatus(phonegapid, PhoneGapBuild.platforms, ns.onMessage, function(data){
                ns.ctl_dialog.free();
                
                ns._selectedApp=phonegapid;
                
                ns.ctl_delete.setDisabled(false);
                ns.ctl_update.setDisabled(false);
                
                ns.ctl_result.setHtml('',true);
                // build results
                var img=ood.getPath('img/',"pg_platform-icons.png"),
                    pos={
                        ios:'0 -32px',
                        android:'-32px -32px',
                        winphone:'-64px -32px',
                        blackberry:'-96px -32px',
                        webos:'-128px -32px',
                        symbian:'-160px -32px'
                    },
                    files={
                        ios:'app.ipa',
                        android:'app.apk',
                        winphone:'app.xap',
                        blackberry:'app',
                        webos:'app',
                        symbian:'app'
                    };

                ood.each(data.download,function(o,i){
                    var btn=new ood.UI.Button({
                        image:img,
                        imagePos:pos[i],
                        caption:'$(RAD.phonegap.Download) '+ i.replace(/./,function(a){return a.toUpperCase()}) + ' $(RAD.phonegap.App) ',
                        position:'relative'
                    },{
                        onClick:function(){
                            var filename=files[i];

                            NWUtil.popSaveAsDlg(filename, "*."+filename.split(".")[1],function(path){
                                var fs=require('fs');
                                var writer = fs.createWriteStream(path);
                                writer.on('finish', function() {
                                   ns.ctl_dialog.free();
                                   ood.alert(ood.adjustRes("$(RAD.phonegap.File has been successfully downloaded to) ")+"\""+path+"\".");
                                });
                                writer.on('error', function(e) {
                                    ood.alert(e.message);
                                    ns.ctl_dialog.free();
                                });
                                ns.ctl_dialog.busy(ood.adjustRes('$RAD.phonegap.Downloading ... '));
                                PhoneGapBuild.api.get(PhoneGapBuild.url+"/"+data.id+"/"+i).pipe(writer);
                            });
                        }
                    });
                    btn.setCustomStyle({"ICON":{"vertical-align":"middle","width":"32px", "height":"32px"}});
                    ns.ctl_result.append(btn);
                    ns.ctl_result.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
                });
            });
        },
        _ctl_btn_rebuildall_onclick:function (profile, e, src, value){
            var ns = this;
            if(!ns._selectedApp){
                ood.alert(ood.adjustRes("$(RAD.phonegap.Select An Application please)!"));
                return ;
            }
        },
        _ctl_btn_update_onclick:function (profile, e, src, value){
            var ns = this,
                lastdata;
            if(!ns._selectedApp){
                ood.alert(("$(RAD.phonegap.Select An Application please)!"));
                return ;
            }


            ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
            PhoneGapBuild.build(ns._selectedApp, ns.properties, PhoneGapBuild.platforms, ns.onMessage, function(data){
                ns.ctl_result.setHtml('',true);
                lastdata=data;
            },function(){
                if(lastdata){
                    var row={
                        title:lastdata.title,
                        cells:[lastdata.id, lastdata.title, lastdata["package"], lastdata.version, lastdata.phonegap_version, lastdata.description]
                    };
                    
                    ns.ctl_tglist.updateRow(ns._selectedApp, row);
                    
                    ns.ctl_tglist.setUIValue(lastdata.id,true);
                    ns.ctl_dialog.free();
                }
            },function(){
                ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
            },function(){
                ns.ctl_dialog.free();
            });
        },
        _ctl_btn_config_onclick:function (profile, e, src, value){
            var ns = this;

            ood.confirm(ood.getRes('RAD.delfile.confirmdel'), ood.getRes('RAD.delfile.confirmdel3'), function(){
                ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
                PhoneGapBuild.api.del(PhoneGapBuild.url + "/" + ns._selectedApp, function(e,data){
                    if(e){
                        ns.onMessage(e.msg);
                    }else{
                        ood.alert(ood.adjustRes("$RAD.phonegap.Deleted"));
                        
                        ns.ctl_tglist.removeRows([ns._selectedApp]);
                        
                        delete ns._selectedApp;
                                        
                        ns.ctl_delete.setDisabled(true);
                        ns.ctl_update.setDisabled(true);
                        
                        ns.ctl_result.removeChildren(null, true);
                    }
                    ns.ctl_dialog.free();
                });
            });

        },
        _ctl_btn_new_onclick:function (profile, e, src, value){
            var ns = this,
                lastdata;
            
            ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
            PhoneGapBuild.build(null, ns.properties, PhoneGapBuild.platforms, ns.onMessage, function(data){
                ns.ctl_result.setHtml('',true);
                lastdata=data;
            },function(){
                if(lastdata){
                    //add to list
                    var row={
                        id:lastdata.id, 
                        title:lastdata.title,
                        cells:[lastdata.id, lastdata.title, lastdata["package"], lastdata.version, lastdata.phonegap_version, lastdata.description]
                    };
                    
                    ns.ctl_tglist.insertRows([row]);
                    
                    ns.ctl_tglist.setUIValue(lastdata.id);
                }
                ns.ctl_dialog.free();
            },function(){
                ns.ctl_dialog.busy(ood.adjustRes("$(RAD.phonegap.Working)..."));
            },function(){
                ns.ctl_dialog.free();
            });
            
        },
        _ctl_link181_onclick:function (profile, e){
            nw.Shell.openExternal("http://build.phonegap.com/");
        }
    }
});
