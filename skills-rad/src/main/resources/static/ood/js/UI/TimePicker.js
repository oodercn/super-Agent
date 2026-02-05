ood.Class('ood.UI.TimePicker', ['ood.UI',"ood.absValue"], {
    Dependencies:['ood.Date'],
    Instance:{
        activate:function(){
            this.getSubNode('PRE').focus(true);
            return this;
        },
        _setCtrlValue:function(value){
            return this.each(function(profile){
                if(!profile.renderId)return;

                var cls = profile.box,
                    arr2=cls._v2a(value);
                profile.$hour=arr2[0];
                profile.$minute=arr2[1];
                
                profile.getSubNode('HI',true).removeClass(cls._excls_c3).removeClass(cls._excls_mo3);
                profile.getSubNode('HI',arr2[0]).addClass(cls._excls_c3);

                profile.getSubNode('MI',true).removeClass(cls._excls_c).removeClass(cls._excls_mo);
                profile.getSubNode('MI',arr2[1]).addClass(cls._excls_c);

                profile.getSubNode('HOUR').html(arr2[0],false);
                profile.getSubNode('MINUTE').html(arr2[1],false);
                profile.getSubNode('CAPTION').html(profile.box._showV(profile,profile.box._v2a(arr2)),false);
            });
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    border = profile.getSubNode('BORDER'),
                    bart = profile.getSubNode('BART'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    btns = root.query('.oodfont'),
                    inputs = root.query('.ood-ui-draggable'),
                    hours = profile.getSubNode('HI', true),
                    minutes = profile.getSubNode('MI', true),
                    setBtn = profile.getSubNode('SET');

                if (theme === 'dark') {
                    // 暗黑模式样式
                    root.addClass('timepicker-dark');
                    
                    // 边框和背景
                    border.css({
                        'background-color': 'var(--ood-dark-bg)',
                        'border-color': 'var(--ood-dark-border)',
                        'color': 'var(--ood-dark-text)'
                    });
                    
                    // 顶部工具栏
                    bart.css({
                        'background': 'var(--ood-dark-gradient)',
                        'border-color': 'var(--ood-dark-border-secondary)'
                    });
                    
                    // 主区域
                    main.css({
                        'background-color': 'var(--ood-dark-bg-secondary)',
                        'border-color': 'var(--ood-dark-border)'
                    });
                    
                    // 底部区域
                    tail.css({
                        'background-color': 'var(--ood-dark-bg-secondary)',
                        'border-color': 'var(--ood-dark-border)'
                    });
                    
                    // 按钮样式
                    btns.css({
                        'color': 'var(--ood-dark-text)',
                        'background': 'var(--ood-dark-bg-button)',
                        'border-color': 'var(--ood-dark-border-secondary)'
                    });
                    
                    // 输入样式
                    inputs.css({
                        'background-color': 'var(--ood-dark-bg-input)',
                        'border-color': 'var(--ood-dark-border-secondary)',
                        'color': 'var(--ood-dark-text)'
                    });
                    
                    // 小时和分钟选项
                    hours.css({
                        'background-color': 'var(--ood-dark-bg-secondary)',
                        'border-color': 'var(--ood-dark-border)',
                        'color': 'var(--ood-dark-text)'
                    });
                    
                    minutes.css({
                        'background-color': 'var(--ood-dark-bg-secondary)',
                        'border-color': 'var(--ood-dark-border)',
                        'color': 'var(--ood-dark-text)'
                    });
                    
                    // 设置按钮
                    if (setBtn && !setBtn.isEmpty()) {
                        setBtn.css({
                            'background': 'var(--ood-dark-primary)',
                            'color': 'var(--ood-dark-text-inverse)',
                            'border-color': 'var(--ood-dark-primary-dark)'
                        });
                    }
                } else {
                    // 浅色模式样式
                    root.removeClass('timepicker-dark');
                    
                    // 移除所有样式设置
                    border.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    
                    bart.css({
                        'background': '',
                        'border-color': ''
                    });
                    
                    main.css({
                        'background-color': '',
                        'border-color': ''
                    });
                    
                    tail.css({
                        'background-color': '',
                        'border-color': ''
                    });
                    
                    btns.css({
                        'color': '',
                        'background': '',
                        'border-color': ''
                    });
                    
                    inputs.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    
                    hours.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    
                    minutes.css({
                        'background-color': '',
                        'border-color': '',
                        'color': ''
                    });
                    
                    if (setBtn && !setBtn.isEmpty()) {
                        setBtn.css({
                            'background': '',
                            'color': '',
                            'border-color': ''
                        });
                    }
                }
                
                // 保存主题设置
                localStorage.setItem('timepicker-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('timepicker-theme') || 'light';
        },
        
        // 切换暗黑模式
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    bart = profile.getSubNode('BART'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    inputs = root.query('.ood-ui-draggable'),
                    btns = root.query('.oodfont'),
                    hours = profile.getSubNode('HI', true),
                    minutes = profile.getSubNode('MI', true),
                    prop = profile.properties;

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('timepicker-mobile');
                    
                    // 移动端调整按钮大小
                    btns.css({
                        'min-width': '2.2em',
                        'min-height': '2.2em',
                        'font-size': '1.1em'
                    });
                    
                    // 移动端调整输入框
                    inputs.css({
                        'min-height': '2em',
                        'font-size': '0.9em',
                        'padding': '0.3em'
                    });
                    
                    // 移动端调整时间选择器
                    hours.css({
                        'font-size': '0.9em',
                        'padding': '0.2em'
                    });
                    
                    minutes.css({
                        'font-size': '0.9em',
                        'padding': '0.2em'
                    });
                } else {
                    root.removeClass('timepicker-mobile');
                    
                    // 恢复桌面样式
                    btns.css({
                        'min-width': '',
                        'min-height': '',
                        'font-size': ''
                    });
                    
                    inputs.css({
                        'min-height': '',
                        'font-size': '',
                        'padding': ''
                    });
                    
                    hours.css({
                        'font-size': '',
                        'padding': ''
                    });
                    
                    minutes.css({
                        'font-size': '',
                        'padding': ''
                    });
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('timepicker-tiny');
                    
                    // 调整时间选择器显示
                    main.css({
                        'font-size': '0.8em'
                    });
                    
                    tail.css({
                        'font-size': '0.8em'
                    });
                } else {
                    root.removeClass('timepicker-tiny');
                    
                    main.css({
                        'font-size': ''
                    });
                    
                    tail.css({
                        'font-size': ''
                    });
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    bart = profile.getSubNode('BART'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    inputs = root.query('.ood-ui-draggable'),
                    btns = root.query('.oodfont'),
                    hours = profile.getSubNode('HI', true),
                    minutes = profile.getSubNode('MI', true),
                    setBtn = profile.getSubNode('SET'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-label': ood.getRes('UI.timepicker.label')
                });
                
                // 为工具栏添加ARIA属性
                if (bart && !bart.isEmpty()) {
                    bart.attr({
                        'role': 'toolbar',
                        'aria-label': ood.getRes('UI.timepicker.toolbar')
                    });
                }
                
                // 为主体区域添加ARIA属性
                if (main && !main.isEmpty()) {
                    main.attr({
                        'role': 'group',
                        'aria-label': ood.getRes('UI.timepicker.selection')
                    });
                }
                
                // 为导航按钮添加ARIA属性
                btns.each(function(btn) {
                    var btnElement = ood(btn),
                        btnClass = btnElement.attr('class') || '',
                        btnLabel = ood.getRes('UI.timepicker.button');
                    
                    if (btnClass.indexOf('doubleleft') > -1) {
                        btnLabel = ood.getRes('UI.timepicker.decreaseHour');
                    } else if (btnClass.indexOf('singleleft') > -1) {
                        btnLabel = ood.getRes('UI.timepicker.decreaseMinute');
                    } else if (btnClass.indexOf('singleright') > -1) {
                        btnLabel = ood.getRes('UI.timepicker.increaseMinute');
                    } else if (btnClass.indexOf('doubleright') > -1) {
                        btnLabel = ood.getRes('UI.timepicker.increaseHour');
                    } else if (btnClass.indexOf('close') > -1) {
                        btnLabel = ood.getRes('UI.timepicker.close');
                    }
                    
                    btnElement.attr({
                        'role': 'button',
                        'aria-label': btnLabel,
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // 为输入框添加ARIA属性
                inputs.each(function(input) {
                    var inputElement = ood(input),
                        inputId = inputElement.id();
                    
                    var label = ood.getRes('UI.timepicker.timeInput');
                    if (inputId && inputId.indexOf('HOUR') > -1) {
                        label = ood.getRes('UI.timepicker.hourInput');
                    } else if (inputId && inputId.indexOf('MINUTE') > -1) {
                        label = ood.getRes('UI.timepicker.minuteInput');
                    }
                    
                    inputElement.attr({
                        'role': 'spinbutton',
                        'aria-label': label,
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // 为时间选择区域添加ARIA属性
                hours.each(function(hour) {
                    var hourElement = ood(hour),
                        hourValue = hourElement.text();
                    
                    hourElement.attr({
                        'role': 'gridcell',
                        'aria-label': ood.getRes('UI.timepicker.hour') + ' ' + hourValue,
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                minutes.each(function(minute) {
                    var minuteElement = ood(minute),
                        minuteValue = minuteElement.text();
                    
                    minuteElement.attr({
                        'role': 'gridcell',
                        'aria-label': ood.getRes('UI.timepicker.minute') + ' ' + minuteValue,
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // 为设置按钮添加ARIA属性
                if (setBtn && !setBtn.isEmpty()) {
                    setBtn.attr({
                        'role': 'button',
                        'aria-label': '设置时间',
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                }
            });
        }
    },
    Initialize:function(){
        this.addTemplateKeys(['HI','MI']);

        var a,i,h,m,cls,cls2,id,t;

        cls=this._excls3;
        cls2=this._excls4;
        id=ood.UI.$ID;
        t='<span id="'+this.KEY+'-HI:'+id+':@" class="ood-node ood-node-span '+cls+' !" '+ood.$IEUNSELECTABLE()+' >@</span>';
        a=[];
        for(i=0;i<24;i++){
            a[a.length]=t.replace(/@/g,i<10?('0'+i):i).replace('!',((i%6===0)?cls2:'')+" ood-custom {comcls}");
             if(i+1==12)a[a.length]='<br />';
        }
        h=a.join('');
        a.length=0;

        cls=this._excls;
        cls2=this._excls2;
        id=ood.UI.$ID;
        t='<span id="'+this.KEY+'-MI:'+id+':@" class="ood-node ood-node-span '+cls+' !" '+ood.$IEUNSELECTABLE()+' >@</span>';
        a=[];
        for(i=0;i<60;i++){
            a[a.length]=t.replace(/@/g,i<10?('0'+i):i).replace('!',((i%5===0)?cls2:'') +" ood-custom {comcls}");
            if((i+1) % 10 ===0 && i!=59 )a[a.length]='<br />';
        }
        m=a.join('');
        a.length=0;
        
        this.setTemplate({
            tagName : 'div',
            //onselectstart:'return false',
            style:'{_style};height:auto;',
            BORDER:{
                tagName : 'div',
                className: 'ood-uiborder-outset ood-uiborder-box ood-uiborder-radius-big',
                BART:{
                    tagName:'div',
                    className:'ood-uibar-top',
                    style:'{barDisplay};',
                    BARTDL:{
                        className:'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-tl',
                        BARTDLT:{
                            className:'ood-uibar-tdlt'
                        }
                    },
                    BARTDM:{
                        $order:1,
                        className:'ood-uibar-tdm ood-uibar',
                        BARTDMT:{
                            className:'ood-uibar-tdmt'
                        }
                    },
                    BARTDR:{
                        $order:2,
                        className:'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-tr',
                        BARTDRT:{
                            className:'ood-uibar-tdrt'
                        }
                    },
                    BARCMDL:{
                        $order:3,
                        tagName: 'div',
                        className:'ood-uibar-cmdl',
                        PRE2:{
                            $order:0,
                            className:'oodfont',
                            $fonticon:'ood-icon-doubleleft',                                                        
                            tabindex: '{tabindex}'
                        },
                        PRE:{
                            $order:1,
                            className:'oodfont',
                            $fonticon:'ood-icon-singleleft',                                                        
                            tabindex: '{tabindex}'
                        },
                        HOUR:{
                            $order:2,
                            className:'ood-ui-draggable ood-uibase ood-uiborder-flat ood-uiborder-radius'
                        },
                        MTXT:{$order:3,text:':'},
                        MINUTE:{
                                $order:4,
                                className:'ood-ui-draggable ood-uibase ood-uiborder-flat ood-uiborder-radius'
                            },
                        NEXT:{
                            $order:6,
                            className:'oodfont',
                            $fonticon:'ood-icon-singleright',                            
                            tabindex: '{tabindex}'
                        },
                        NEXT2:{
                            $order:7,
                            className:'oodfont',
                            $fonticon:'ood-icon-doubleright',                            
                            tabindex: '{tabindex}'
                        }
                    },
                    BARCMDR:{
                        $order:4,
                        tagName: 'div',
                        className:'ood-uibar-cmdr',
                        CLOSE:{
                            className:'oodfont',
                            $fonticon:'ood-uicmd-close',
                            style:'{closeDisplay}'
                        }
                    },
                    TBARTDB:{
                        $order:5,
                        tagName: 'div',
                        className:'ood-uibar-tdb ood-uiborder-inset ood-uiborder-radius'
                    }
                },
                MAIN:{
                    $order:2,
                    tagName:'div',
                    className:'ood-uicon-main ood-uibar',
                    MAINI:{
                        tagName:'div',
                        className:'ood-uibar ood-uicon-maini ood-uibar',
                        CONH:{
                            tagName:'div',
                            className:'ood-uibase ood-uiborder-flat',
                            text:h
                        },
                        CONM:{
                            $order:2,
                            tagName:'div',
                            className:'ood-uibase ood-uiborder-flat',
                            text:m
                        }
                    }
                },
                TAIL:{
                    $order:3,
                    tagName:'div',
                    className:'ood-uicon-main ood-uibar',
                    TAILI:{
                        tagName:'div',
                        className:'ood-uibar ood-uicon-maini',
                        CAPTION:{
                            text : '{caption}'
                        },
                        SET:{
                            tagName:'button',
                            className:'ood-ui-btn ood-uibar ood-uigradient ood-uiborder-radius',
                            tabindex: '{tabindex}',
                            text:"{_set}"
                        }
                    }
                },
                BBAR:{
                    $order:4,
                    tagName:'div',
                    className:'ood-uibar-bottom-s',
                    BBARTDL:{
                        className:'ood-uibar-tdl ood-uibar ood-uiborder-radius-big-bl'
                    },
                    BBARTDM:{
                        $order:1,
                        className:'ood-uibar-tdm ood-uibar'
                    },
                    BBARTDR:{
                        $order:2,
                        className:'ood-uibar-tdr ood-uibar ood-uiborder-radius-big-br'
                    }
                }
            }
        });
    },
    Static:{
        _excls:'oodex-timepicker ood-uicell',
        _excls2:'oodex-timepicker ood-uicell ood-uicell-alt ',
        _excls3:'oodex-timepicker3 ood-uicell',
        _excls4:'oodex-timepicker ood-uicell ood-uicell-alt',

        _excls_mo:'ood-uicell-hover',
        _excls_c:'ood-uicell-checked',
        _excls_mo3:'ood-uicell-hover',
        _excls_c3:'ood-uicell-checked',
        _mover:function(src, type){
            var b=this,cn=src.className;
            if(type==2){
                if(cn.indexOf(b._excls_mo3)==-1)
                    src.className=cn + ' ' + b._excls_mo3;
            }else{
                if(cn.indexOf(b._excls_mo)==-1)
                    src.className=cn + ' ' + b._excls_mo;
            }
            src=null;
        },
        _mout:function(src,type){
            var b=this,cn=src.className;
            if(type==2){
                if(cn.indexOf(b._excls_mo3)!=-1)
                    src.className=cn.replace(b._excls_mo3,'');
            }else{
                if(cn.indexOf(b._excls_mo)!=-1)
                    src.className=cn.replace(b._excls_mo,'');
            }
            src=null;
        },
        Appearances:{
            KEY:{
            },
            'TBART, BBART':{
                'border-spacing':0,
                'border-collapse':'separate'
            },
            MAINI:{
                'padding':'.5em .3333em .3333em 0'
            },
            CONH:{
                'white-space': 'nowrap',
                'text-align':'center'
            },
            CONM:{
                'margin-top':'.5em',
                'white-space': 'nowrap',
                'text-align':'center'
            },
            BARCMDL:{
                top:'.125em'
            },
            'PRE,PRE2,NEXT,NEXT2':{
                position:'relative',
                margin:'0 .25em',
                'vertical-align': 'middle',
                cursor:'default'
            },
            'HOUR, MINUTE':{
                $order:3,
                'font-weight':'bold',
                'vertical-align': 'middle',
                cursor:'e-resize',
                margin:'0 .25em',
                'padding':'0 .25em'
            },
            SET:{
                position:'absolute',
                display:'none',
                top:'.125em',
                right:'.5em'
            },
            TAILI:{
                position:'relative',
                padding:'.5em 0 0 0',
                'text-align':'center'
            },
            CAPTION:{
                'vertical-align':ood.browser.ie6?'baseline':'middle',
                'font-size':'1em'
            },
            '.oodex-timepicker':{
                width:'2em',
                'text-align':'center',
                padding:'.25em 0',
                margin:0
            },
            '.oodex-timepicker3':{
                width:'1.6666em',
                'text-align':'center',
                'font-weight':'bold',
                padding:'.25em 0',
                margin:0
            }
        },
        Behaviors:{
            HoverEffected:{CLOSE:'CLOSE',PRE:'PRE',NEXT:'NEXT',PRE2:'PRE2',NEXT2:'NEXT2',SET:'SET'},
            ClickEffected:{CLOSE:'CLOSE',PRE:'PRE',NEXT:'NEXT',PRE2:'PRE2',NEXT2:'NEXT2',SET:'SET'},
            KEY:{onClick:function(){return false}},
            HOUR:{
                beforeMousedown:function(profile, e, src){
                    if(ood.Event.getBtn(e)!="left")return;
                    ood(src).startDrag(e, {
                        dragType:'blank',
                        targetReposition:false,
                        widthIncrement:5,
                        dragCursor:true
                    });
                    profile.$temp2=0;
                },
                onDrag:function(profile, e, src){
                    var count,off = ood.DragDrop.getProfile().offset,v=profile.properties.$UIvalue,a=v.split(':');
                    a[0]=Math.round( (parseFloat(a[0])||0)+parseInt(off.x/10) );
                    a[0]=(a[0]%24+24)%24;
                    profile.$temp2=(a[0]<=9?'0':'')+a[0];

                    if(v[0]!=profile.$temp2)
                        profile.getSubNode('HOUR').html(profile.$temp2,false);
                },
                onDragstop:function(profile, e, src){
                    if(profile.$temp2){
                        profile.$hour=profile.$temp2;
                        profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    }
                    profile.$temp2=0;
                    profile.box._hourC(profile);
                }
            },
             MINUTE:{
                beforeMousedown:function(profile, e, src){
                    if(ood.Event.getBtn(e)!="left")return;
                    ood(src).startDrag(e, {
                        dragType:'blank',
                        targetReposition:false,
                        widthIncrement:5,
                        dragCursor:true
                    });
                    profile.$temp2=0;
                },
                onDrag:function(profile, e, src){
                    var count,off = ood.DragDrop.getProfile().offset,v=profile.properties.$UIvalue,a=v.split(':');
                    a[0]=Math.round( (parseFloat(a[0])||0) + parseFloat(off.x/20) );
                    a[0]=(a[0]%60+60)%60;
                    profile.$temp2=(a[0]<=9?'0':'')+a[0];

                    if(v[0]!=profile.$temp2)
                        profile.getSubNode('MINUTE').html(profile.$temp2,false);
                },
                onDragstop:function(profile, e, src){
                    if(profile.$temp2){
                        profile.$minute=profile.$temp2;
                        profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    }
                    profile.$temp2=0;
                    profile.box._hourC(profile);
                }
            },
            SET:{
                onClick:function(profile){
                    var pro=profile.properties,
                        v=pro.$UIvalue,
                        a=v.split(':');
                    a[0]=profile.$hour;
                    a[1]=profile.$minute;
                    profile.boxing().setUIValue(a.join(':'),true,null,'click');
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            HI:{
                onMouseover:function(profile, e, src){
                    if(profile.properties.disableHoverEffect)return;
                    profile.box._mover(ood.use(src).get(0),2);
                },
                onMouseout:function(profile, e, src){
                    if(profile.properties.disableHoverEffect)return;
                    profile.box._mout(ood.use(src).get(0),2);
                },
                onClick:function(profile, e, src){
                    profile.$hour=profile.getSubId(src);
                    profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    if(profile.box)profile.box._hourC(profile);
                },
                onDblclick:function(profile, e, src){
                    profile.$hour=profile.getSubId(src);
                    profile.boxing().setUIValue(profile.$hour+":"+profile.$minute,true,null,'dblclick');
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            MI:{
                onMouseover:function(profile, e, src){
                    if(profile.properties.disableHoverEffect)return;
                    profile.box._mover(ood.use(src).get(0));
                },
                onMouseout:function(profile, e, src){
                    if(profile.properties.disableHoverEffect)return;
                    profile.box._mout(ood.use(src).get(0));
                },
                onClick:function(profile, e, src){
                    profile.$minute=profile.getSubId(src);
                    profile.boxing().setUIValue(profile.$hour+":"+profile.$minute,true,null,'click2');
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            PRE:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    var v=profile.$minute;
                    v=(parseFloat(v)||0)-1;
                    v=(v%60+60)%60;
                    profile.$minute=v=(v<=9?'0':'')+v;
                    profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            NEXT:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    var v=profile.$minute;
                    v=(parseFloat(v)||0)+1;
                    v=(v%60+60)%60;
                    profile.$minute=v=(v<=9?'0':'')+v;
                    profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            PRE2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    var v=profile.$hour;
                    v=(parseFloat(v)||0)-1;
                    v=(v%24+24)%24;
                    profile.$hour=v=(v<=9?'0':'')+v;
                    profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            NEXT2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    var v=profile.$hour;
                    v=(parseFloat(v)||0)+1;
                    v=(v%24+24)%24;
                    profile.$hour=v=(v<=9?'0':'')+v;
                    profile.boxing()._setCtrlValue(profile.$hour+":"+profile.$minute);
                    if(profile.box)profile.box._hourC(profile);
                }
            },
            CLOSE:{
                onClick:function(profile, e, src){
                    var properties = profile.properties,
                        instance = profile.boxing();
                    if(properties.disabled||properties.readonly)return;
                    if(false===instance.beforeClose(profile, src)) return;
                    instance.destroy(true);
                }
            }
        },
        DataModel:{
            // 现代化属性
            theme: {
                ini: 'light',
                listbox: ['light', 'dark'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            expression:{
                ini:'',
                action:function () {
                }
            },
            height:{
                ini:'auto',
                readonly:true
            },
            width:{
                ini:'auto',
                readonly:true
            },
            value:{
                ini:'00:00',
                format:'time'
            },
            closeBtn:{
                ini:true,
                action:function(v){
                    this.getSubNode('CLOSE').css('display',v?'':'none');
                }
            }
        },
        EventHandlers:{
            beforeClose:function(profile, src){}
        },
        _hourC:function(profile){
            var pro=profile.properties,
                v=pro.$UIvalue,
                a=v.split(':'),
                d = (a[0]+"")==(profile.$hour+"") && (a[1]+"")==(profile.$minute+"");
            profile.getSubNode('SET').css('display',d?'none':'block');
            profile.getSubNode('CAPTION').css('color',d?'':'var(--ood-error)');
        },
        _prepareData:function(profile){
            var data=arguments.callee.upper.call(this, profile);
            var nodisplay='display:none';
            data.closeDisplay = data.closeBtn?'':nodisplay;
            data._set = ood.wrapRes('inline.set');
            return data;
        },
        
        RenderTrigger: function() {
            // 现代化功能初始化
            var self = this;
            ood.asyRun(function(){
                self.TimePickerTrigger();
            });
        },
        
        TimePickerTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('timepicker-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (prop.responsive !== false) {
                boxing.adjustLayout();
            }

            // 初始化可访问性
            boxing.enhanceAccessibility();
        },
        _ensureValue:function(profile, value){
            var a,b=[];
            if(value&& typeof value == 'string')
                a=value.split(':')
            else if(value && typeof value=='object' && ood.isArr(value))
                a=value;
            else a=[];

            b[0]= parseFloat(a[0])||0;
            b[1]=parseFloat(a[1])||0;
            if(b[0]<0)b[0]=0;
            if(b[0]>23)b[0]=23;
            if(b[1]<0)b[1]=0;
            if(b[1]>59)b[1]=59;

            b[0]=(b[0]<=9?'0':'')+b[0];
            b[1]=(b[1]<=9?'0':'')+b[1];

            return b.join(':');
        },
        formatValue:function(value){
            return value.join(':');
        },
        _v2a:function(v){
            return typeof v == 'string'? v.split(':') : v;
        },
        _showV:function(profile, a){
            var f=profile.CF;
            if(typeof f.formatCaption == 'function')
                return f.formatCaption(a);
            else
                return a.join(':');
        },
        _onresize:function(){}
    }
});