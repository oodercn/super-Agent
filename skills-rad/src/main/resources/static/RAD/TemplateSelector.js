ood.Class('RAD.TemplateSelector', 'ood.Module',{
    Instance:{
        customAppend:function(parent){
            var ns=this,
                prop = ns.properties,
                pp = prop.parent,
                dlg=ns.dialog;

            if(!dlg.get(0).renderId){   
                dlg.render();
            }
            dlg.setCaption(ood.adjustRes("$(RAD.tpl.Add file)$(RAD.tpl. to )" +"[" + prop.parentPath +"]"));
            //asy
            dlg.showModal(parent, null,null,function(){
                ns.ctl_buttonviews4.setUIValue(prop.fileType=="Module"?"module":"page", true);
            });
             var dft="page";
            ood.arr.each(ns.ctl_buttonviews4.getItems('min'),function(key){
                // only can upload image
                var show=false;
                if(prop.fileType=="root"){
                    show=true;
                }else if(prop.fileType=="oodOnly"){
                    show = key=="page" || key=="ood.page.js" || key=="/";
                }else{
                    if(prop.fileType=="Module"&& (key=="module" || key=="ood.page.js"|| key=="/")){
                        show=true;
                    }else if(prop.fileType=="App"&& (key=="page" || key=="/")){
                        show=true;
                    }else if(prop.fileType=="Data" && (key=="page" || key==".json"|| key=="/")){
                        show=true;
                    }else if(prop.fileType=="css" && (key=="page" || key==".css"|| key=="/")){
                        show=true;
                    }else if(prop.fileType=="img" && (key=="page" || key=="img"|| key=="/")){
                        show=true;
                    }else if(/^[\w]+$/.test(prop.fileType)){
                        show=true;
                    }
                }
                ns.ctl_buttonviews4.updateItem(key, { hidden: !show});
            });
 

            ns.ctl_tabs2.setUIValue("local");
            
            CONF.checkRegisterStatus(null,function(){
                if(CONF.ads2_url){
                    ns.ctl_ads.setIframeAutoLoad(CONF.ads2_url,true);
                    ns.ctl_ads.setHeight(CONF.ads2_height||80);
                }
            });
        },
        iniComponents:function(){

            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"dialog")
                .setDockMargin({
                    "left":10,
                    "top":10,
                    "right":10,
                    "bottom":10
                }
                )
                .setLeft("0em")
                .setTop("0em")
                .setWidth("41.625em")
                .setResizer(false)
                // 1. 替换对话框标题图标
                .setCaption("$(RAD.tpl.Add file)")
                .setImageClass("ri ri-file-text-line")
                .setMinBtn(false)
                .setStatus("max")
                .onHotKeydown("_dialog_onhotkey")
                .beforeClose("_dialog_beforeclose")
            );
            
            host.dialog.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ctl_block10")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.ctl_block10.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_pane56")
                .setClassName("ood-uibg-base")
                .setDock("fill")
                );
            
            host.ctl_pane56.append(
                ood.create("ood.UI.Tabs")
                .setHost(host,"ctl_tabs2")
                .setItems([{
                    "id":"local",
                    "caption":"$(RAD.tpl.Page Templates)",
                    "image":""
                }/*,
                {
                    "id":"online",
                    "caption":"$(RAD.tpl.Online Page Templates)",
                    "image":""
                }
                */])
                .setDock("top")
                .setDockOrder(3)
                .setHeight("auto")
                .setLazyAppend(false)
                .setNoPanel(true)
                .beforeUIValueSet("_ctl_tabs2_beforepage")
                .setCustomStyle({
                    "ITEMS":{
                        "padding-left":"4px"
                    }
                }
                )
                );
            
            host.ctl_pane56.append(
                ood.create("ood.UI.Layout")
                .setHost(host,"ctl_layout6")
                .setItems([{
                    "id":"before",
                    "pos":"before",
                    "size":132,
                    "min":10,
                    "locked":false,
                    "folded":false,
                    "hidden":false,
                    "cmd":true
                },
                {
                    "id":"main",
                    "min":10,
                    "size":524
                },
                {
                    "id":"after",
                    "pos":"after",
                    "size":80,
                    "min":10,
                    "locked":false,
                    "folded":false,
                    "hidden":true,
                    "cmd":false,
                    "itemDisplay":"display:none;"
                }])
                .setType("horizontal")
                );
            
            host.ctl_layout6.append(
                ood.create("ood.UI.Div")
                .setHost(host,"ctl_ads")
                .setDock("bottom")
                .setHeight("0em")
                , "main");
            
            host.ctl_layout6.append(
                ood.create("ood.UI.Gallery")
                .setHost(host,"ctl_gallery")
                .setDirtyMark(false)
                .setDisableTips(true)
                .setDock("fill")
                .setSelMode("none")
                .setBorderType("none")
                .setItemMargin(2)
                .setItemWidth(124)
                .setItemHeight(120)
                .setImgWidth(120)
                .setImgHeight(80)
                .onItemSelected("_ctl_gallery_onitemselected")
                .setCustomStyle({
                    "IMAGE":{
                        "border":"solid #dddddd 1px"
                    }
                }
                )
                , "main");
            
            host.ctl_layout6.append(
                ood.create("ood.UI.TreeView")
                .setHost(host,"ctl_list")
                .setDirtyMark(false)
        .onGetContent("_ctl_list_ongetcontent")
                .onItemSelected("_ctl_list_onitemselected")
                , "before");
            
            host.dialog.append(
                ood.create("ood.UI.ButtonViews")
                .setHost(host,"ctl_buttonviews4")
                .setItems([

                    {
                    "id":"page",
                    "caption":"<strong>$RAD.addfile.iXuiTpl</strong>"
                },
                {
                    "id":"module",
                    "caption":"<strong>$RAD.addfile.iModule</strong>"
                }])
                .setDock("top")
                .setHeight("2.6666666666666665em")
                .setNoPanel(true)
                .setBarSize(28)
                .beforeUIValueSet("_ctl_list1_beforeuivalueset")
                .onItemSelected("_ignoreMouseDown")
                .setCustomStyle({
                    "LIST":{
                        "border":"none"
                    }
                }
                )
                );
            
            return children;
            // ]]Code created by ESDUI RAD Studio
        },
        _ignoreMouseDown:function(){
            return false;
        },
        events:{"onReady":"_onready","onRender":"_onrender"},
        _onready:function(){
            var ns=this;
           
            ns._preview = new RAD.TemplatePreview();
            ns._preview.setEvents({onOK:function(){
                ns.dialog.hide();
                ns.fireEvent('onOK',ood.toArr(arguments),ns);
            }});
            ns._preview.render();
        },
        _onrender:function(){
            var ns=this, 
                preview=ns._preview,
                items=ns.ctl_gallery.getItems();
            ns.ctl_gallery.hoverPop(preview.blkMain,12,function(profile, node, e, src, item){
                preview.init(item, items, ns.properties);
            },function(){
                return !preview.isStopHover();
            });
        },
        _dialog_beforeclose:function(profile){
            this.ctl_tabs2.setUIValue(null); 
            profile.boxing().hide();
            return false;
        },
        _dialog_onhotkey:function(profile, key){
            if(key.key=='esc')
                profile.boxing().close();
        },
        getImagePreTag: function(type){
            return type=="online"? CONF.onlineTempalteLibPath:
                       type=="local"? CONF.serviceType=="node-webkit"  ? CONF.adjustProtocol('') : '' :
                        "";
       },
        _ctl_tabs2_beforepage:function (profile,oldValue, newValue){
            var ns = this,
                prop=ns.properties,
                item=profile.getItemByItemId(newValue),
                type = ns.ctl_buttonviews4.getUIValue(),
                name;
            switch(newValue){

                case "ood.page.js":
                    name="Name";//type=="module"?"ModuleName":"PageName";
                    ood.showModule("RAD.AddFile",function(){
                        this.setProperties({
                            fileName:name,
                            parentPath:prop.parentPath,
                            typeCaption:item.caption,
                            type:type,
                            forcedir:true,
                            blankmodule:1,
                            tailTag:".js"
                        });
                        this.setEvents({onOK:function(){
                            ns.dialog.hide();
                            ns.fireEvent('onOK',ood.toArr(arguments),ns);
                        }});
                    },null,null,true);
                    return false;

                case "internet":
                    var dlg=ood.prompt("$RAD.img.URL","$(RAD.img.Type picture URL here)",'http://',function(value){
                        if(CONF.isZipURL(value)){
                            CONF.callService({
                                key:CONF.requestKey,
                                curProjectPath:SPA.curProjectPath,
                                paras:{
                                    action:'fetchsavewebfile',
                                    hashCode:ood.id(),
                                    path:value,
                                    npath:SPA.curProjectPath + "/img",
                                    force:true
                                }
                            },function(txt){
                                var obj = txt;
                                if(obj && !obj.error){
                                    ood.tryF(ns.properties.onOK, [ns, obj.data.path], ns.host);
                                    ns.dialog.close();
                                }else ood.message(obj.error.message);
                            });
                            ns.dialog.close();
                        }else{
                            ood.alert("$(RAD.img.This is not a valid zip file URL address)");
                            return false;
                        }
                        dlg=null;
                    },null,null,null,null,null,ns.dialog);
                    dlg._$input.setMultiLines(false);//.setHeight(26);
                    return false;
            }

            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_list.resetValue().clearItems();

            ood.asyRun(function(){
                var callback=function(txt){
                    ns.ctl_list.free();
                    var obj = txt;
                    if(!obj || obj.error){
                        ood.message(ood.get(obj,['error','message'])||'no response!');
                    }else{
                        var items=[],
                              conf = obj.data.conf?ood.unserialize(obj.data.conf):false,
                              sortby={},
                              map={};
                        if(conf){
                            if(conf.list)
                                ood.arr.each(conf.list,function(o,i){
                                    map[o.id]=o;
                                    sortby[o.id]=i;
                                });
                        }
                        if(obj.data.files){
                            ood.arr.each(obj.data.files,function(o,i){
                                items.push({
                                    id: o.location,
                                    name:o.name, 
                                    sub:false,
                                    caption: ood.get(map,[o.name,'caption', ood.getLang()])||o.name
                                });
                            });
                        }
                        if(!ood.isEmpty(sortby)){
                            ood.arr.stableSort(items,function(x,y){
                                x=sortby[x.name]||-1;y=sortby[y.name]||-1;
                                 return x>y?1:x==y?0:-1;
                            });
                        }
                        ns.ctl_list.setItems(items);
                        if(items[0]){
                            ns.ctl_list.fireItemClickEvent(items[0].id);
                        }
                    }
                };
                var type = ns.ctl_buttonviews4.getUIValue();
                switch(newValue){
                    case "local":
                    case "self":
                        ns.ctl_list.busy(true);
                        ood.Ajax(
                            CONF.getProjectTemp,
                            {
                                path:type=="page"?CONF.myTempalteLibPath:CONF.localModlueLibPath,
                                curProjectPath:SPA.curProjectPath
                            }
                            ,callback
                        ).start();

                        break;

                }
            });
        },
        _ctl_list_onitemselected:function (profile, item, e, src, type){
            var ns = this,
                tabs = ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                remote=tabpage=="online",
                type = ns.ctl_buttonviews4.getUIValue(),
                preTag = ns.getImagePreTag(tabpage),

                callback=function(txt){
                    ns.ctl_gallery.free();
                    var obj = txt;
                    if(!obj || obj.error)
                        ood.message(ood.get(obj,['error','message'])||'no response!');
                    else{
                        var items=[],
                              conf = obj.data.conf?ood.unserialize(obj.data.conf):false,
                              sortby={},
                              map={};
                        if(conf){
                            if(conf.list)
                                ood.arr.each(conf.list,function(o,i){
                                    map[o.id]=o;
                                    sortby[o.id]=i;
                                });
                        };
                        if(obj.data.files){
                            var tag = type=="page"?CONF.ood_page_tpl_tag:CONF.ood_module_tpl_tag;
                            ood.arr.each(obj.data.files,function(o,i){
                                if(CONF.getClientMode() == "project" ){
                                    var item={
                                        id:o.location,
                                        url:CONF.adjustFileAbsPath(preTag)+o.location,
                                        remote:remote,
                                        name:o.name,
                                        caption: null, 
                                        comment:ood.get(map,[o.name,'caption', ood.getLang()])||o.name.replace(tag+"_","").replace(tag+"code_",""),
                                        image:preTag+o.location+"/snapshot.png"
                                    };
                                    var prop=ood.get(map,[o.name,'prop']);
                                    if(prop&&prop.charged){
                                        item.flagStyle="top:0;font-size:1.5em;";
                                        item.prop=prop;
                                        item.flagClass = "ri ri-currency-line";
                                    }
                                    items.push(item);
                                }
                            });
                        }
                        
                        if(!ood.isEmpty(sortby)){
                            ood.arr.stableSort(items,function(x,y){
                                x=sortby[x.name]||-1;y=sortby[y.name]||-1;
                                 return x>y?1:x==y?0:-1;
                            });
                        }
                        
                        ns.ctl_gallery.resetValue();
                        ns.ctl_gallery.setItems(items);
                    }
                };
            ns.ctl_gallery.resetValue().clearItems();
            ns.ctl_gallery.busy(true);
            var type = ns.ctl_buttonviews4.getUIValue();
            switch(tabpage){
                case "local":
                case "self":

                    ood.Ajax(CONF.getProjectTemp,  {path:item.id},callback).start();

                    // CONF.callService({
                    //     key:CONF.requestKey,
                    //     curProjectPath:SPA.curProjectPath,
                    //     paras:{
                    //         action:'open',
                    //         pattern: "("+(type=="page" ? CONF.ood_page_tpl_tag: CONF.ood_module_tpl_tag)+").*",
                    //         withConfig:true,
                    //         hashCode:ood.id(),
                    //         type:0,
                    //         path:item.id
                    //     }
                    // } ,function(txt){
                    //     callback.call(ns,txt);
                    // });
                break;

            }
            if(item.sub && !item._checked)
                ns.ctl_list.toggleNode(item.id, true);
        },
        _ctl_list_ongetcontent:function (profile, item, setsub){
            var ns = this,
                tabs=ns.ctl_tabs2,
                tabpage = tabs.getUIValue(),
                type = ns.ctl_buttonviews4.getUIValue(),
                callback=function(txt){
                    var obj = txt;
                    if(!obj || obj.error)
                        ood.message(ood.get(obj,['error','message'])||'no response!');
                    else{
                        var items=[],
                              conf = obj.data.conf?ood.unserialize(obj.data.conf):false,
                              sortby={},
                              map={};
                        if(conf){
                            if(conf.list)
                                ood.arr.each(conf.list,function(o,i){
                                    map[o.id]=o;
                                    sortby[o.id]=i;
                                });
                        };
                        if(obj.data.files){
                            ood.arr.each(obj.data.files,function(o,i){
                                if(o.name.charAt(0)!=".'" && !ood.str.startWith(o.name, type=="page"?CONF.ood_page_tpl_tag:CONF.ood_module_tpl_tag) )
                                    items.push({id:o.location, name:o.name, sub:true, caption: ood.get(map,[o.name,'caption', ood.getLang()])||o.name});
                            });
                        }
                        if(!ood.isEmpty(sortby)){
                            ood.arr.stableSort(items,function(x,y){
                                x=sortby[x.name]||-1;y=sortby[y.name]||-1;
                                 return x>y?1:x==y?0:-1;
                            });
                        }
                        setsub(items.length===0?false:items);
                        //if(items&&items.length)
                        //    profile.boxing().fireItemClickEvent(items[0].id);
                    }
                };
            var type = ns.ctl_buttonviews4.getUIValue();
            switch(tabpage){
                case "self":
                case "local":
                 ood.Ajax(CONF.openFolderService, {path: item.id, hashCode:ood.id(), curProjectPath: SPA.curProjectPath}
                ,callback, null, null, {method: 'POST'});
                break;

            }
        },

        openUploadWin:function(path,closeFun){


        },
        _ctl_list1_beforeuivalueset:function (profile, oldValue, newValue){
            var ns=this,
                prop=this.properties,
                item=profile.getItemByItemId(newValue),
                name;
            switch(item.id){
                case "img":
                    var pPath=prop.parentPath.indexOf( SPA.curProjectPath + "/img/")!=-1 ? prop.parentPath : (SPA.curProjectPath + "/img");
                    var uploadPath=CONF.uploadPath;
                    var dio= ood.create("ood.UI.Dialog")
                        .setLeft("8.333333333333334em")
                        .setTop("5.833333333333333em")
                        .setWidth("40.833333333333336em")
                        .setHeight("30em")
                        .setIframeAutoLoad(uploadPath+"?uploadpath="+pPath+"&filter=jpg;gif;png")
                        .setCaption("添加文件")
                        .setMaxBtn(false)
                        .beforeClose(function(){
                            ns.fireEvent('onOK',[pPath, "", "", false],ns);
                        })
                        .setMaxBtn(false);

                    dio.show();

                case "img":

                case "page":
                    ns.ctl_tabs2.removeItems(["disk","empty","ood.page.js","hire"]);
                    ns.ctl_buttonviews4.removeItems("empty");

                    ns.ctl_tabs2.updateItem("local", {caption:"$(RAD.tpl.Page Templates)"});
//                    ns.ctl_tabs2.updateItem("online", {caption:"$(RAD.tpl.Online Page Templates)"});


                    ns.ctl_tabs2.insertItems([{"id":"ood.page.js", "caption":"$RAD.addfile.iNewPage"}]);

                    ns.ctl_tabs2.setUIValue("local", true);
                    return;
                case "module":
                    ns.ctl_tabs2.removeItems(["disk","empty","ood.page.js","hire"]);
                    ns.ctl_buttonviews4.removeItems("empty");

                    ns.ctl_tabs2.updateItem("local", {caption:"$(RAD.tpl.Modules Templates)"});
//                    ns.ctl_tabs2.updateItem("online", {caption:"$(RAD.tpl.Online Modules)"});

                    // add local disk
                    if(CONF.serviceType=="node-webkit"){
                        //ns.ctl_tabs2.insertItems([{"id":"self", "caption":"$(RAD.tpl.My Templates)"}]);
                        //ns.ctl_tabs2.insertItems([{"id":"disk", "caption":"$(RAD.tpl.From Local File)"}]);
                        ns.ctl_buttonviews4.insertItems([{"id":"empty", "caption":"$RAD.addfile.empty"}]);
                    }
                    ns.ctl_tabs2.insertItems([{"id":"ood.page.js", "caption":"$RAD.addfile.iNewModule"}]);
                    //ns.ctl_buttonviews4.insertItems([{"id":"file", "caption":"$RAD.addfile.iUpload"}]);

                    ns.ctl_tabs2.setUIValue("local", true);
                    return;

                case "/":
                    name="newPath";
                break;
                case "file":
                    name="";
                break;
                case "empty":
                    name="newEmptyFile";
                break;
                default:
                    name="newFileName";
            }
            ood.showModule("RAD.AddFile",function(){
                this.setProperties({
                    fileName:name,
                    parentPath:prop.parentPath,
                    typeCaption:item.caption,
                    type:item.id,
                    forcedir:true,
                    tailTag:item.id=="ood.page.js"?".js":(item.id=="/"||item.id=="file"||item.id=="empty")?"":item.id
                });
                this.setEvents({onOK:function(){
                    ns.dialog.hide();
                    ns.fireEvent('onOK',ood.toArr(arguments),ns);
                }});
            },null,null,true);
            return false;
        },
        _ctl_gallery_onitemselected:function (profile, item, e, src, type){
            this._preview._apply(item);
        }
    }
 });
