ood.Class('ood.Debugger', null, {
    Static:{
        $time:ood.stamp(),
        _id1:'ood:dbg::_frm',
        _id4:'ood:dbg::_head',
        _id2:'ood:dbg::_con',
        _id3:'ood:dbg::_inp',
        err:function(sMsg,sUrl,sLine){
            if(ood.browser.gek && sMsg=='Error loading script')
                return true;
            ood.Debugger.log('>>' + sMsg+' at File: '+ sUrl + ' ( line ' + sLine + ' ).');
            return true;
        },
        trace:function(obj){
            var args=ood.toArr(arguments),
                fun=args[1]||arguments.callee.caller,
                arr=args[2]||[];
            if(fun){
                arr.push('function "' + (fun.$name$||'') + '" in Class "' + (fun.$original$||'') +'"');
                if(fun.caller){
                    try{
                        arguments.callee(null,fun.caller,arr,1);
                    }catch(e){}
                }
            }
            if(!args[3]){
                var a=[];
                a.push(' >> Object Info:');
                if(typeof obj == 'object')
                    for(var i in obj)
                        a.push(' -- ' + i + " : " + obj[i]);
                else
                    a.push(obj);
                a.push(' >> Function Trace: ' + arr.join(' <= '));
                ood.Debugger.log.apply(ood.Debugger,a);
            }
            fun=null;
        },
        log:function(){
            var t1,t2,time,self=this,arr=ood.toArr(arguments),str;
            if(!arr.length)return;
            t1 = document.createElement("div");
            t2 = document.createElement("div");
            t2.className='ood-uibase ood-dbg-con1';
            time=ood.stamp();
            // if (hastime){
            //     t2.appendChild(document.createTextNode('Time stamp : '+time +'('+(time-self.$time)+')' ));
            // }


            self.$time=time;
            t1.appendChild(t2);
            for(var i=0,l=arr.length;i<l;i++){
                str=arr[i];
                t2 = document.createElement("div");
                t2.className='ood-uibase ood-dbg-con2';
                t2.appendChild(document.createTextNode(" "+ood.stringify(ood.isArguments(str)?ood.toArr(str):str)));
                t1.appendChild(t2);
            }

            if(!ood.Dom.byId(self._id2)){
                var ns=ood.create('<div id='+self._id1+' style="left:5px;top:'+(ood.win.scrollTop()+5)+'px;" class="ood-ui-reset ood-node ood-node-div ood-wrapper ood-dbg-frm ood-custom"><div class="ood-node ood-node-div ood-dbg-box ood-custom"><div id='+self._id4+' class="ood-node ood-node-div ood-uibar ood-dbg-header ood-custom">&nbsp;&nbsp;调试窗口 <span class="ood-node ood-node-span ood-dbg-cmds ood-custom"><a class="ood-node ood-node-a ood-custom" href="javascript:;" onclick="ood(\''+self._id2+'\').empty();">Clear</a><a class="ood-node ood-node-a ood-custom" href="javascript:;" onclick="ood(\''+self._id1+'\').remove();">✖</a></span></div><div id='+self._id2+' class="ood-node ood-node-div ood-uibase ood-dbg-content ood-custom"></div><div class="ood-node ood-node-div ood-uibase ood-dbg-tail ood-custom"><table class="ood-node ood-node-table ood-custom"><tr><td style="font-family:serif;">&nbsp;>>>&nbsp;</td><td style="width:100%"><input class="ood-node ood-node-input ood-custom" id='+self._id3+' /></td></tr></table></div></div></div>');
                ood('body').append(ns);
                self.$con=ood(self._id2);
                ood(self._id4).draggable(true,null,null,null,ood(self._id4).parent(2));

                if(ood.Dom.css3Support("boxShadow")){
                    ns.css("boxShadow","2px 2px 5px #ababab");
                }

                if(ood.browser.ie6){
                    ns.height(ns.offsetHeight());
                    ns.width(399);
                    ood.asyRun(function(){ns.width(400);})
                }
                var bak='',temp;
                ood(self._id3).onKeydown(function(p,e,s){
                    var k=ood.Event.getKey(e).key;
                    s=ood.use(s).get(0);
                    if(k=='enter'){
                        switch(s.value){
                            case '?':
                            case 'help':
                                self.$con.append(ood.create("<div class='ood-node ood-node-div ood-uibase ood-dbg-con3 ood-custom'><p class='ood-node ood-node-p ood-custom'><strong  class='ood-node ood-node-strong ood-custom'>vailable commands:</strong></p><ul  class='ood-node ood-node-ul ood-custom'><li  class='ood-node ood-node-li ood-custom'> -- <strong  class='ood-node ood-node-strong ood-custom'>[clr]</strong> or <strong>[clear]</strong> : clears the message</li><li  class='ood-node ood-node-li ood-custom'> -- <strong  class='ood-node ood-node-strong ood-custom'>[?]</strong> or <strong  class='ood-node ood-node-strong ood-custom'>[help]</strong> : shows this message</li><li  class='ood-node ood-node-li ood-custom'> -- <strong class='ood-node ood-node-strong ood-custom'>any other</strong>: shows its string representation</li></ul></div>"));
                                break;
                            case 'clr':
                            case 'clear':
                                ood(self._id2).empty();
                                break;
                            default:
                                try{
                                    temp=s.value;
                                    if(/^\s*\x7b/.test(temp))temp='('+temp+')';
                                    self.log(eval(temp));
                                }catch(e){self.$con.append(ood.create("<div  class='ood-node ood-node-div ood-uibase ood-dbg-con4 ood-custom'>"+String(e)+"</div>"));return;}
                        }
                        bak=s.value;
                        s.value='';
                    }else if(k=='up'||k=='down'){
                        var a=s.value;
                        s.value=bak||'';
                        bak=a;
                    }
                    k=s=temp=bak=null;
                });
            }
            self.$con.append(t1).scrollTop(self.$con.scrollHeight());
            t1=t2=null;
        }
    },
    Initialize:function(){
        ood.CSS.addStyleSheet(
            '.ood-dbg-frm{position:absolute;width:25em;z-index:2000;}'+
            '.ood-dbg-header{cursor:move;height:1.5em;padding-top:.25em;position:relative;border-bottom:solid 1px #CCC;background-color:#FFAB3F;font-weight:bold;}'+
            '.ood-dbg-cmds{position:absolute;right:.25em;top:.25em;}'+
            '.ood-dbg-cmds a{margin:.25em;}'+
            '.ood-dbg-box{position:relative;overflow:hidden;border:solid 1px #AAA;}'+
            '.ood-dbg-content{position:relative;width:100%;overflow:auto;height:25em;overflow-x:hidden;}'+
            '.ood-dbg-con1{background-color:#CCC;width:24.5empx;}'+
            '.ood-dbg-con2{padding-left:.5em;border-bottom:dashed 1px #CCC;width:24.5em;}'+
            '.ood-dbg-con3{padding-left:.5em;border-bottom:dashed 1px #CCC;background:#EEE;color:#0000ff;width:24.5em;}'+
            '.ood-dbg-con4{padding-left:.5em;border-bottom:dashed 1px #CCC;background:#EEE;color:#ff0000;width:24.5em;}'+
            '.ood-dbg-tail{overflow:hidden;position:relative;border-top:solid 1px #CCC;height:1.3333333333333333em;background:#fff;color:#0000ff;}'+
            '.ood-dbg-tail input{width:100%;border:0;background:transparent;}'
        ,this.KEY);
        //fix ie6:

        //shorcut
        ood.echo = function(){
            if(!ood.debugMode)return false;
            ood.Debugger.log.apply(ood.Debugger,ood.toArr(arguments));
        };
        ood.message = function(body, head, width, duration){
           width = width || 300;
           if(ood.browser.ie)width=width+(width%2);
           var div, h, me=arguments.callee,
           stack=me.stack||(me.stack=[]),
           allmsg=me.allmsg||(me.allmsg=[]),
           t=ood.win, left = t.scrollLeft() + t.width()/2 - width/2, height=t.height(), st=t.scrollTop();

           div=stack.pop();
           while(div&&!div.get(0))
                div=stack.pop();

           if(!div){
               div =
               '<div class="ood-ui-reset ood-node ood-node-div ood-wrapper ood-uibar ood-uiborder-outset ood-custom" style="border:solid 1px #cdcdcd;position:absolute;overflow:visible;top:-50px;">' +
                   '<div class="ood-node ood-node-div ood-custom" style="font-size:1.25em;overflow:hidden;font-weight:bold;padding:.25em;"></div>'+
                   '<div class="ood-node ood-node-div ood-custom" style="font-size:1em;padding:.5em;overflow:hidden;"></div>'+
               '</div>';
               div = ood.create(div);
               if(div.addBorder)div.addBorder();
               allmsg.push(div);
               if(ood.Dom.css3Support("boxShadow")){
                    div.css("boxShadow","2px 2px 5px #ababab");
               }
            }
            if(document.body.lastChild!=div.get(0))
                ood('body').append(div,false,true);

            div.topZindex(true);

            div.__hide=0;

            div.css({left:left+'px', width:width+'px', visibility:'visible'})
            .first().html(head||'').css('display',head?'':'none')
            .next().html(body||'');

            if(ood.browser.ie && ood.browser.ver<=8)
                div.ieRemedy();

            if(me.last && me.last.get(0) && div!=me.last){
                var last=me.last;
                var l=last.left();
                if(last._thread&&last._thread.id&&last._thread.isAlive())last._thread.abort();
                last._thread=last.animate({left:[l,l+(last.width+width)/2+20]},function(){
                    last.left(l);
                },function(){
                    last.left(l+(last.width+width)/2+20);
                }).start();
                
                var lh=last.offsetHeight();
               ood.filter(allmsg,function(ind){
                    if(ind.isEmpty())
                        return false;
                   if(!ind.__hide && ind!=div && ind!=last){
                       if(ind._thread.id&&ind._thread.isAlive())
                            ind._thread.abort();
                       ind.topBy(lh);
                    }
               });

            }
            me.last = div;
            me.last.width = width;

            //height() is ok
            h = div.height();

            if(ood.browser.ie6)div.cssSize({ height :h, width :width+2});

            if(div._thread&&div._thread.id&&div._thread.isAlive())div._thread.abort();
            div._thread=div.animate({top:[st-h-20,st+20]},function(){
                div.top(st-h-20);
            },function(){
                div.top(st+20);
            },300,0,'expoOut').start();

            ood.asyRun(function(){
                if(div._thread&&div._thread.id&&div._thread.isAlive())div._thread.abort();
                div._thread=div.animate({top:[div.top(), height+20]},null,function(){
                     stack.push(div); 
                     div.hide();
                     div.__hide=1;
                },300,0).start();
            }, duration||5000);
            me=null;
        };

        if(ood.isDefined(window.console) && (typeof window.console.log=="function")){
            ood.log=function(){window.console.log.apply(window.console,ood.toArr(arguments));};
        }else if(ood.debugMode){
            ood.log=ood.echo;
            window.onerror=this.err;
        }
    }
});