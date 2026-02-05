ood.Class("RAD.EditorTool",null,{
    Static:{    
        getPropName:function(key,prop){
            if(!key||!prop)return '';
            var mkey="doc.propname."+key.replace(/\./g,'_')+"."+prop;
            var o=ood.getRes(mkey);
            if(o != prop){
                return ood.wrapRes(mkey);
            }else{
                    var obj = ood.get(window,key.split('.'));
                    obj=obj.prototype['get'+prop.replace(/^[a-z]/g,function(a){return a.toUpperCase();})] || obj['set'+prop.replace(/^[a-z]/g,function(a){return a.toUpperCase();})] ;
                    if(obj && obj.$original$ && obj.$original$!=key)
                        return RAD.EditorTool.getPropName(obj.$original$, prop);
                    else
                       return ood.wrapRes(mkey);
            }
        }, 
        getEventName:function(key,evt){
            if(!key||!evt)return '';
            var mkey="doc.eventname."+key.replace(/\./g,'_')+"."+evt;
            var o=ood.getRes(mkey);
            if(o != evt){
                return ood.wrapRes(mkey);
            }else{
                    var obj = ood.get(window,key.split('.'));
                    obj=obj.prototype[evt];
                    if(obj && obj.$original$ && obj.$original$!=key)
                        return RAD.EditorTool.getEventName(obj.$original$, evt);
                    else
                       return ood.wrapRes(mkey);
            }
        }, 
        getDoc:function(key, shortMsg, returnHash){
            if(!key)return '';
            if(key.indexOf('(')!=-1)
                key=key.replace(/[()]/g,'');
            var o = ood.getRes("doc."+key);
            try{
                if(typeof o == 'string'){
                    var obj = ood.get(window,key.split('.'));
                    if(obj && obj.$original$)
                        o = ood.getRes("doc." +obj.$original$+((obj.$type$=='instance'||obj.$type$=='event')?'.prototype':'')+"."+obj.$name$);
                    if(typeof o == 'string')
                        return null;
                }
                return returnHash?o:this._buildDoc(o, shortMsg);
            }catch(e){
                return null;
            }
        }, 
        _buildDoc:function(o, shortMsg){
            var arr=[];
            if(o){
                if(shortMsg){
                    return o.$desc;
                }else{
                    if(o.$desc)
                        arr.push('<div class="doc-inndiv">' + o.$desc + '</div>');
                    if(o.$rtn)
                        arr.push('<div class="doc-inndiv">' + '<strong>'+ood.getRes('app.retV')+': </strong>' + o.$rtn + '</div>');
                    if(o.$paras){
                        arr.push('<div class="doc-inndiv">' + '<div><strong>'+ood.getRes('app.param')+': </strong></div><ul>');
                        ood.arr.each(o.$paras,function(v){
                            v=v.replace(/^([^:\[]*)([^:]*):(.*)$/,"<strong>$1</strong> $2 : $3");
                            arr.push('<li> ' + v + ' </li>');
                        })
                        arr.push("</ul></div>");
                    }
    
                    if(o.$snippet){
                        arr.push('<div class="doc-inndiv">' + '<div><strong>'+ood.getRes('app.codesnip')+': </strong></div>');
                        ood.arr.each(o.$snippet,function(v){
                            arr.push('<textarea id="doc:code" class="js plain fold" style="display:none;">' + v + '</textarea><p>&nbsp;</p>');
                        })
                        arr.push("</div>");
                    }
                    if(o.$memo)
                        arr.push('<div class="doc-inndiv">' + '<strong>'+ood.getRes('app.memo')+': </strong>' + o.$memo + '</div>');
    
                    if(o.$links){
                        arr.push('<div class="doc-inndiv">' + '<div><strong>'+ood.getRes('app.seealso')+': </strong></div><ul>');
                        ood.arr.each(o.$links,function(v){
                            arr.push('<li><a target="'+(v[2]||'')+'" href="' +v[1]+ '">' + v[0] + '</a></li>');
                        })
                        arr.push("</ul></div>");
                    }
                }
            }
            return arr.join('');
        }, 
        showFindWnd:function(ajax,editor, pos, str, parent){
            ood.ComFactory.getCom("RAD.FAndR",function(){
                this._editor=editor;
                this._init=str;
                this.show(null, editor.getRoot(), null, null, 0, 0);
            });
        },
        showJumpToWnd:function(ajax,editor, pos){
            ood.ComFactory.getCom("RAD.JumpTo",function(){
                this.setEvents({
                    "onOK":function(line){
                        try{
                            editor.callEditor('focus');
                            editor.callEditor('setCursor',[line-1,0],true);
                        }catch(e){}
                    }
                });
                this.show(null, null, null, null, pos.left, pos.top);
            });
        }
    }
});
