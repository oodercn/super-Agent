
//resizer class, add a plug in to ood.Dom
ood.Class("ood.UI.AdvResizer","ood.UI.Resizer",{
    Instance:{
        //get Region for one/multi target
        cssRegion:function(){
            var profile=this.get(0),
            target = profile._target,
            cb=profile.getRoot().contentBox(),
            l,t,b,r,
            ll,tt,ww,hh,
            c=[];

            if(target){
                var purge=ood.$cache.domPurgeData;
                ood.arr.stableSort(target._nodes,function(x,y){
                    x=purge[y].element.style.zIndex;y=purge[y].element.style.zIndex;
                    return x>y?1:x==y?0:-1;
                });

                target.each(function(o,i){
                    var bbox, rotate,
                        isSVG=o.raphael,
                        id=o.id;
                    if(isSVG){
                        var svg = ood.svg.getFromDom(o.id);
                        bbox = svg._getBBox();
                        rotate = svg.getRotate();
                    }else{
                        o=ood([o]);
                        bbox={
                            x:o.offsetLeft(),
                            y:o.offsetTop(),
                            width:o.offsetWidth(),
                            height:o.offsetHeight()
                        };
                        rotate = o.rotate();
                    }
                    if(i===0){
                        l=bbox.x;
                        t=bbox.y;
                        r=l+(ww=bbox.width);
                        b=t+(hh=bbox.height);
                        c.push([{left :l, top :t},{ width :ww, height :hh},id,rotate]);
                    }else{
                        l=Math.min(l,ll=bbox.x);
                        t=Math.min(t,tt=bbox.y);
                        r=Math.max(r,ll+(ww=bbox.width));
                        b=Math.max(b,tt+(hh=bbox.height));
                        c.push([{left :ll, top :tt},{ width :ww, height :hh},id,rotate]);
                    }
                });
            }
            profile.regionBlocks = c;
            //ajust border
            ood.arr.each(c,function(o){
                o[0].left -= l;
                o[0].top -= t;
                if(cb){
                    o[1].width -= 2;
                    o[1].height -= 2;
                }
            });
            return {l:l, t:t, b:b, r:r};
        },
        setOffsetParent:function(node){
            var prf=this.get(0);
            if(prf)prf._offsetparent=node;
        },
        // reset position and size
        rePosSize:function(){
            var self=this;
            self.each(function(o){
                var t,
                    t1=o.getRoot(),
                    rgid=o.$domId,
                    rotatable=o.properties.rotatable && !(ood.browser.ie && ood.browser.ver<=8),
                    t2=o._target,
                    cb=t2.contentBox();
                if(!t2 || t2.isEmpty())return;
                if(!o.properties._attached){
                    t = o.region=o.boxing().cssRegion();
                    var offset;
                    if(o._offsetparent){
                        t2 = t2.parent();
                        if(!t2.get(0))return false;
                        offset = t2.offset(null,o._offsetparent);
                        offset.left += (cb?t2._borderW('left'):0) - t2.get(0).scrollLeft;
                        offset.top += (cb?t2._borderH('top'):0) - t2.get(0).scrollTop;
                    }
                    t1.cssPos({left :t.l+(offset?offset.left:0), top :t.t+(offset?offset.top:0)});
                    t1.offsetWidth(t.r-t.l).offsetHeight(t.b-t.t);
                    if(rotatable){
                        if(o._target.size()==1){
                            o.getSubNode("ROTATE").css("display","");
                            t1.rotate(o._target.rotate());
                        }else{
                            o.getSubNode("ROTATE").css("display","none");
                            t1.rotate(0);
                        }
                    }
                }
                if(!o.regPool)o.regPool=ood();
                if(t=o.regions){
                    o.regPool.merge(t);
                    t.css('display','none');
                }
                o.regions=ood();

                if(o.regionBlocks){
                    var t,len=o.regionBlocks.length,fun=function(p,e,src){
                        var b = o.boxing(),
                            t = b.getTarget(),
                            key = ood.Event.$keyboard;
                        if(o.onRegionClick && false!==b.onRegionClick(o,e))
                            if(t._nodes.length>1){
                                var tid=ood.getNodeData(src,'_tid');
                                if(key && key.shiftKey){
                                    ood.arr.removeValue(t._nodes,tid);
                                    b.resetTarget(t);
                                }else
                                    b.focus(tid);
                            }
                    };
                    ood.arr.each(o.regionBlocks,function(v){
                        if(!o.regPool.isEmpty()){
                            t=o.regPool._nodes.pop();
                            ood(t).cssPos(v[0]).cssSize(v[1]);
                            if(rotatable){
                                ood(t).rotate(len>1?v[3]:0);
                            }
                        }else{
                            t = ood.str.toDom('<div style="position:absolute;cursor:default;border:dashed 1px blue;left:{l}px;top:{t}px;width:{w}px;height:{h}px;"></div>'
                            .replace('{l}',v[0].left)
                            .replace('{t}',v[0].top)
                            .replace('{w}',v[1].width)
                            .replace('{h}',v[1].height)
                            );
                            t.id(rgid+o.regPool._nodes.length)
                            if(rotatable){
                                t.rotate(len>1?v[3]:0);
                            }
                            
                            t.onClick(fun);
                            t=t.get(0).$xid;
                        }
                        o.regions._nodes.push(t);
                        ood.setNodeData(t,'_tid',v[2]);
                    });
                    o.getRoot().append(o.regions.css('display',''));
                }
            });
            self.focus();
            return self;
        },
        // get target
        getTarget:function(){
            return this.get(0)._target;
        },
        // reset target and refresh
        resetTarget:function(target,flag){
            var self=this,
                profile = self.get(0),
                rb = self.reBoxing(),
                ids;
            if(profile.properties._attached)return;
            delete profile.$focus;

            if(target && !target.isEmpty()){
                profile._target = target;
                self.rePosSize();
                rb.css({zIndex:ood.Dom.TOP_ZINDEX, display:''});
            }else{
                profile._target = ood();
                rb.css({zIndex:0,display:'none'});
            }
            if(target && !target.isEmpty()){
                ids=[];
                target.reBoxing('UI').each(function(o,i){
                    ids.push(o.$xid);
                });
            }else
                ids=null;
            if(flag!==false && profile.onItemsSelected)
                profile.boxing().onItemsSelected(profile, ids, profile.$xid);
            if(profile.onResetTarget)
                profile.boxing().onResetTarget(profile, ids, profile.$xid);
            return self;
        },
        focus:function(id){
           var profile=this.get(0), index=-1;

           if(!profile.regions)return;
           profile.regions.css('border','dashed 1px blue');

           var arr = profile._target.get();

           if(id)index = ood.arr.subIndexOf(arr,'id',id);
           if(index==-1 && profile.$focus !== undefined)index=profile.$focus;
           if(index==-1 && arr.length>1)index = arr.length-1;

           if(index!=-1){
                profile.regions.css('border','dashed 1px blue');
                ood([profile.regions.get(index)]).css('border','solid 1px red');

                profile.$focus=index;
                if(profile.onFocusChange)profile.boxing().onFocusChange(profile,index);
            }

           return this;
        },
        getFocus:function(){
            return this.get(0).$focus;
        },
        active:function(flag){
            return this.each(function(profile){
                profile.getRoot().addClass("active");
                profile.getSubNode('MOVE').css('backgroundPosition','-17px -244px');
                profile.getSubNodes(['LT','T','RT','R','RB','B','LB','L'])
                .css('background',ood.browser.ie ? 'url('+ood.ini.path+'bg.gif)' : '#fff');
                if(flag!==false)profile.boxing().onActive(profile);
            });
        },
        inActive:function(){
            return this.each(function(profile){
                if(profile.$onDrag)return;
                profile.getRoot().removeClass("active");
                profile.getSubNode('MOVE').css('backgroundPosition','-34px -244px');
                profile.getSubNodes(['LT','T','RT','R','RB','B','LB','L']).css('background','#808080');
            });
        }
    },
    Static:{
        DataModel:{
            dragArgs:null,
            leftOffset:0,
            topOffset:0
        },
        EventHandlers:{
            onActive:function(profile){},
            onFocusChange:function(profile, index){},
            onItemsSelected:function(profile,ids){},
            onResetTarget:function(profile,ids){},
            onRegionClick:function(profile,e){},
            onDblclick:function(profile, e, src){}
        },
        _onMousedown:function(profile, e, src, ddparas){
             profile.boxing().active();

            if(ood.Event.getBtn(e)!="left")return;
            var ck=ood.Event.$keyboard,
                 prop=profile.properties;

            if(profile.properties.disabled && !(ck && ck.ctrlKey) )return false;

             // begin drag use blank
            if(ck && ck.ctrlKey){
                profile.boxing().resetTarget(null);
                var pos=ood.Event.getPos(e);

                var hash = {
                    dragDefer:2,
                    dragType:'icon',
                    targetLeft:pos.left+12,
                    targetTop:pos.top+12,
                    dragCursor:'pointer'
                };
                // set other args for drag
                ood.merge(hash,prop.dragArgs,'all');
                hash.widthIncrement=hash.heightIncrement=0;
                hash.dragData.pos = profile.getRoot().cssPos();

                ood().startDrag(e,hash);
            }else{
                var hash,o,absPos,pos,posbak,size;
                if(prop._attached){
                    pos=ood.Event.getPos(e);
                    ood.use(src).startDrag(e,{
                        dragDefer:2,
                        targetReposition:false,
                        dragType:'blank',
                        dragCursor:true,
                        targetLeft:pos.left,
                        targetTop:pos.top
                    });
                }else{
                    o = profile.getRoot();
                    absPos = o.offset();
                    pos=o.cssPos();
                    posbak=ood.copy(pos);

                    if(ddparas.move){
                        absPos=ood.Event.getPos(e);
                        if(prop.dragArgs.widthIncrement){
                            var off=prop.leftOffset && prop.leftOffset % prop.dragArgs.widthIncrement;
                            pos.left-=off;
                            posbak.left-=off;
                        }
                        if(prop.dragArgs.heightIncrement){
                            var off=prop.topOffset && prop.topOffset % prop.dragArgs.heightIncrement;
                            pos.top-=off;
                            posbak.top-=off;
                        }
                    }else{
                        size=o.cssSize();

                        if(ddparas.left){
                            if(ddparas.top){
                            }else if(ddparas.bottom){
                                pos.top = pos.top + size.height;
                            }else{
                                pos.top = pos.top + size.height/2;
                            }
                        }
                        if(ddparas.right){
                            pos.left = pos.left + size.width;
                            if(ddparas.top){
                            }else if(ddparas.bottom){
                                pos.top = pos.top + size.height;
                            }else{
                                pos.top = pos.top + size.height/2;
                            }
                        }
                        if(ddparas.top && !ddparas.left && !ddparas.right){
                            pos.left = pos.left + size.width/2;
                        }
                        if(ddparas.bottom && !ddparas.left && !ddparas.right){
                            pos.left = pos.left + size.width/2;
                            pos.top = pos.top + size.height;
                        }
                    }

                    if((t=prop.dragArgs) && (t=t.widthIncrement)){
                        var offx = ood.DragDrop.$proxySize % t;
                        if(ddparas.left){
                            pos.left += offx;
                        }else if(ddparas.right){
                            pos.left += offx;// + 2;
                        }else if(ddparas.move){
                            pos.left += offx;
                        }
                        pos.left += parseInt((absPos.left-posbak.left)/t)*t;
                    }
                    if((t=prop.dragArgs) && (t=t.heightIncrement)){
                        var offy = ood.DragDrop.$proxySize % t;
                        if(ddparas.top){
                            pos.top += offy;
                        }else if(ddparas.bottom){
                            pos.top += offy;// + 2;
                        }else if(ddparas.move){
                            pos.top += offy;
                        }

                        pos.top += parseInt((absPos.top-posbak.top)/t)*t;
                    }

                    var hash = {
                        dragDefer:2,
                        targetReposition:false,
                        dragType:'blank',
                        dragCursor:true,
                        targetLeft:pos.left,
                        targetTop:pos.top
                    };
                    ood.merge(hash,prop.dragArgs,'all');
                    hash.targetOffsetParent=profile._parent;
                    hash.dragKey=null;

                    ood.use(src).startDrag(e,hash);
                }
            }
        },
        LayoutTrigger:function(){
            this.boxing().rePosSize();
        }
    }
});
