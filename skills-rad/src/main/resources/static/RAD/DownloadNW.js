ood.Class('RAD.DownloadNW', 'ood.Module',{
    Instance:{
        customAppend:function(parent){
            var ns=this,
                prop=ns.properties,
                url=prop.url,
                dest=prop.dest,
                entryName=prop.entryName,
                callback=prop.callback,
                temp=dest+"/temp_" + url.split("/").pop(); 

            ns.btn_cancel.setDisabled(false);

            ns._downloadres = NWUtil.download(url, temp, function(precentage){
                if(ns.progressbar)
                    ns.progressbar.setValue(precentage,true);
            }, function(buffer){
                var fs=require('fs');
                if(fs.existsSync(temp)){
                    ns.btn_cancel.setDisabled(true);
                    ns.ctl_slabel5.setCaption("$(RAD.download.Extracting)...");
                    // adm-zip cant work in OSX
                    var path = require('path'), ext=path.extname(temp);
                    if(ext==".zip"){
                        var extract = require('extract-zip'), files=[];
                        extract(temp, { 
                            dir: dest,
                            onEntry: function(entry){
                                files.push({
                                    mode: entry.externalFileAttributes >>> 16,
                                    path: entry.fileName
                                });
                            }
                        }, function(err){
                            if(err){
                                ood.alert(err.message);
                                ns.destroy();
                            }
                            // Setup chmodSync to fix permissions
                            files.forEach(function(file) {
                                fs.chmodSync(path.join(dest, file.path), file.mode);
                             });

                            fs.unlinkSync(temp);
                            if(fs.existsSync(dest+"/"+entryName)){
                                callback(dest+"/"+entryName);
                            }else{
                                ood.alert(entryName +" donesnt in the package!");
                            }
                            ns.destroy();
                        });
/*
                        var DecompressZip = require('decompress-zip');
                        var unzipper = new DecompressZip(temp)
                        unzipper.on('error', function (err) {
                            ood.alert(err.message);
                            ns.destroy();
                        });
                        unzipper.on('extract', function (log) {
                            fs.unlinkSync(temp);
                            if(fs.existsSync(dest+"/"+entryName)){
                                callback(dest+"/"+entryName);
                            }else{
                                ood.alert(entryName +" donesnt in the package!");
                            }
                            ns.destroy();
                        });
                        unzipper.extract({
                            path: dest,
                            filter: function (file) {
                                return file.type !== "SymbolicLink";
                            }
                        });
*/
                    }
                    // adm-zip cant work for gzip too
                    else if(ext==".gz"){
                         var zlib = require('zlib'),
                            tar = require('tar-fs'),
                            gunzip = zlib.createGunzip(),
                            isWin = /^win/.test(process.platform);

                        fs.createReadStream(temp)
                            .pipe(zlib.createGunzip())
                            .pipe(tar.extract(dest, {
                                umask: (isWin ? false : 0)
                            }))
                            .on('finish', function (err) {
                                if (!err) {
                                        fs.unlinkSync(temp);
                                        if(fs.existsSync(dest+"/"+entryName)){
                                            callback(dest+"/"+entryName);
                                        }else{
                                            ood.alert(entryName +" donesnt in the package!");
                                        }
                                        ns.destroy();
                                }else {
                                    ood.alert(err.message);
                                    ns.destroy();
                                }
                            });
                    }else{
                        ood.alert("Target is not a zip or gzip file!");
                        ns.destroy();                        
                    }
                }else{
                    ood.alert("Download failed!");
                    ns.destroy();
                }
            }, function(msg){
                ood.message(ood.adjustRes(msg));
                ns.destroy();
            });

            ns.ctl_slabel5.setCaption(entryName + " $(RAD.download.will be download to) : ");
            ns.ctl_slabel6.setCaption(dest);

            ns.dlg.showModal();
        },
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dlg")
                .setLeft("5.833333333333333em")
                .setTop("2.5em")
                .setWidth("51em")
                .setHeight("10.833333333333334em")
                .setResizer(false)
                .setCaption("$(RAD.download.Before the first deploy for this platform, we need to download the corresponding packager)...")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setRestoreBtn(false)
                .setCloseBtn(false)
                .setOverflow("hidden")
            );
            
            host.dlg.append(
                ood.create("ood.UI.ProgressBar")
                .setHost(host,"progressbar")
                .setLeft("0.625em")
                .setTop("4.25em")
                .setWidth("36em")
                .setHeight("2em")
                );
            
            host.dlg.append(
                ood.create("ood.UI.Button")
                .setHost(host,"btn_cancel")
                .setLeft("38.333333333333336em")
                .setTop("4.166666666666667em")
                .setWidth("10.166666666666666em")
                .setCaption("$(RAD.download.Cancel Download)")
                .onClick("_btn_cancel_onclick")
                );
            
            host.dlg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel5")
                .setLeft("0.625em")
                .setTop("0.5em")
                .setWidth("48.5em")
                .setHeight("1.1875em")
                .setCaption("")
                .setHAlign("left")
                );
            
            host.dlg.append(
                ood.create("ood.UI.Label")
                .setHost(host,"ctl_slabel6")
                .setLeft("0.625em")
                .setTop("2em")
                .setWidth("48.5em")
                .setHeight("1.1666666666666667em")
                .setCaption("")
                .setHAlign("left")
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },
        _btn_cancel_onclick:function (profile, e, src){
            var ns = this;
            ns._downloadres.abort();
            ood.alert(ood.adjustRes("$(RAD.download.In order to deploy the application, you have to try it again later)."));
            ns.destroy();
        }
    },
    Static:{
        viewStyles:{ }
    }
});
