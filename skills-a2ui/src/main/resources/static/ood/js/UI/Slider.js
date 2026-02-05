ood.Class("ood.UI.Slider", ["ood.UI","ood.absValue"],{
    Instance:{
        iniProp: {
            labelSize: '6em', height: '4em', labelPos: 'top', caption: '$RAD.widgets.slider', labelHAlign: 'left',
            isRange: false
        },
        _setCtrlValue:function(value){
            return this.each(function(profile){
                var p=profile.properties,
                    steps=p.steps,
                    fun=function(k){return profile.getSubNode(k)},
                    a=fun('IND1'),
                    b=fun('IND2'),
                    c=fun('BG'),
                    v=p.type=='vertical',
                    arr = profile.box._v2a(profile,value),
                    ori = v?'top':'left',
                    orj = v?'height':'width',
                    label, cap=ood.adjustRes(p.labelCaption,true)||"";
                if(p.isRange){
                    a[ori](arr[0]+'%');
                    b[ori](arr[1]+'%');
                    c[ori](arr[0]+'%')[orj]((arr[1]-arr[0])+'%');
                    label = (p.numberTpl+"")
                        .replace(/[*12]/g,function(a){
                            return a=='1'?ood.formatNumeric(arr[0],p.precision||0,null,null,true):
                            a=='2'? ood.formatNumeric(arr[1],p.precision||0,null,null,true)
                            : cap;
                        });
                }else{
                    a[ori](arr[0]+'%');
                    c[orj](arr[0]+'%');
                    label = (p.numberTpl+"")
                        .replace(/1[^2]*2/, '1' )
                        .replace('1', ood.formatNumeric(arr[0],p.precision||0,null,null,true) );
                    if(label.indexOf('*')!=-1)
                        label=label.replace('*', cap);
                    else label += cap;
                }
                if(p.labelPos!='none'&&parseFloat(p.labelSize)){
                    profile.getSubNode('LABEL').html(label );
                }
            });
        },
        _setDirtyMark:function(){
            return arguments.callee.upper.apply(this,['BOX']);
        },
        
        // Set theme
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // Clear all theme classes
                root.removeClass('slider-dark slider-light slider-hc');
                
                // Apply theme class
                root.addClass('slider-' + theme);
                
                // Save theme settings
                localStorage.setItem('slider-theme', theme);
            });
        },
        
        // Get current theme
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('slider-theme') || 'light';
        },
        
        // Toggle dark mode
        toggleDarkMode: function() {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },
        
        // Responsive layout adjustment
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // Adjust layout for small screens
                if (width < 768) {
                    root.addClass('slider-mobile');
                    
                    // Increase touch area for mobile
                    var inds = profile.getSubNode('IND1');
                    inds.css({
                        'width': '1.2em',
                        'height': '1.2em'
                    });
                    
                    if (prop.isRange) {
                        var ind2 = profile.getSubNode('IND2');
                        if (ind2 && !ind2.isEmpty()) {
                            ind2.css({
                                'width': '1.2em',
                                'height': '1.2em'
                            });
                        }
                    }
                } else {
                    root.removeClass('slider-mobile');
                    
                    // Restore desktop styles
                    var inds = profile.getSubNode('IND1');
                    inds.css({
                        'width': '',
                        'height': ''
                    });
                    
                    if (prop.isRange) {
                        var ind2 = profile.getSubNode('IND2');
                        if (ind2 && !ind2.isEmpty()) {
                            ind2.css({
                                'width': '',
                                'height': ''
                            });
                        }
                    }
                }

                // Special handling for extra small screens
                if (width < 480) {
                    root.addClass('slider-tiny');
                    
                    // Further increase touch area
                    var inds = profile.getSubNode('IND1');
                    inds.css({
                        'width': '1.4em',
                        'height': '1.4em'
                    });
                } else {
                    root.removeClass('slider-tiny');
                }
            });
        },
        
        // Enhance accessibility support
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    ind1 = profile.getSubNode('IND1'),
                    ind2 = profile.getSubNode('IND2'),
                    label = profile.getSubNode('LABEL'),
                    properties = profile.properties,
                    currentValue = properties.$UIvalue || properties.value || properties.min || 0;

                // Add ARIA attributes to slider
                root.attr({
                    'role': 'slider',
                    'aria-label': ood.getRes('UI.slider.label') || 'Slider control',
                    'aria-orientation': properties.type === 'vertical' ? 'vertical' : 'horizontal',
                    'aria-valuetext': properties.isRange ? 
                        ood.formatNumeric(Array.isArray(currentValue) ? currentValue[0] : currentValue, properties.precision || 0) + 
                        ' to ' + 
                        ood.formatNumeric(Array.isArray(currentValue) ? currentValue[1] : currentValue, properties.precision || 0) :
                        ood.formatNumeric(currentValue, properties.precision || 0),
                    'aria-valuemin': properties.min || 0,
                    'aria-valuemax': properties.max || 100,
                    'aria-valuenow': Array.isArray(currentValue) ? currentValue[0] : currentValue
                });
                
                // Add ARIA attributes to main slider
                if (ind1 && !ind1.isEmpty()) {
                    ind1.attr({
                        'role': 'slider',
                        'aria-valuemin': properties.min || 0,
                        'aria-valuemax': properties.max || 100,
                        'aria-valuenow': Array.isArray(currentValue) ? currentValue[0] : currentValue,
                        'aria-label': properties.isRange ? ood.getRes('UI.slider.rangeStart') : ood.getRes('UI.slider.value'),
                        'aria-orientation': properties.type === 'vertical' ? 'vertical' : 'horizontal'
                    });
                }
                
                // Add ARIA attributes to second slider (range)
                if (properties.isRange && ind2 && !ind2.isEmpty()) {
                    ind2.attr({
                        'role': 'slider',
                        'aria-valuemin': properties.min || 0,
                        'aria-valuemax': properties.max || 100,
                        'aria-valuenow': Array.isArray(currentValue) ? currentValue[1] : currentValue,
                        'aria-label': ood.getRes('UI.slider.rangeEnd'),
                        'aria-orientation': properties.type === 'vertical' ? 'vertical' : 'horizontal'
                    });
                }

                // Add attributes to label
                if (label && !label.isEmpty() && properties.labelPos !== 'none') {
                    label.attr({
                        'aria-live': 'polite'
                    });
                }
            });
        }
    },
    Static:{
        Templates:{
            style:'{_style}',
            className:'{_className} ood-ui-ellipsis {_cls}',
            LABEL:{
                className:'{_required}',
                style:'{labelShow};width:{_labelSize};{labelHAlign}',
                text:'{labelCaption}'
            },
            BOX:{
                tagName:'div',
                RULER:{
                    $order:1,
                    tagName:'div',
                    className:'ood-uiborder-flat ood-uibase ood-uiborder-radius',
                    BG:{
                        tagName:'div',
                        className:'ood-uibar ood-uiborder-radius'
                    }
                },
                IND:{
                    $order:2,
                    IND1:{
                        tagName:'span',
                        className:'ood-uibar ood-uigradient ood-ui-btn ood-uiborder-circle oodcon ood-icon-placeholder',
                        style:'{_showD}',
                        tabindex:'{tabindex}'
                    },
                    IND2:{
                        tagName:'span',
                        className:'ood-uibar ood-uigradient ood-ui-btn ood-uiborder-circle oodcon ood-icon-placeholder',
                        style:'{_showD2}',
                        tabindex:'{tabindex}'
                    }
                },
                DECREASE:{
                    style:'{_showDes}',
                    className:'oodfont',
                    $fonticon:'{_fi_decls}',
                    tabindex:'{tabindex}'
                },
                INCREASE:{
                    style:'{_showIns}',
                    className:'oodfont',
                    $fonticon:'{_fi_incls}',
                    tabindex:'{tabindex}'
                }
            }
        },
        // Upgrade Appearances section
        Appearances: {
        'RULER': {
        'background-color': 'var(--ood-slider-track-bg)',
        'border-radius': 'var(--ood-slider-border-radius)',
        'border': 'var(--ood-slider-track-border)',
        'height': 'var(--ood-slider-height)',
        transition: 'background-color 0.3s ease'
        },
        'BG': {
        'background': 'var(--ood-slider-fill-bg)',
        'border-radius': 'var(--ood-slider-border-radius)'
        },
        'IND1, IND2': {
        'border-radius': '50%',
        'box-shadow': 'var(--ood-slider-thumb-shadow)',
        'background': 'var(--ood-slider-thumb-bg)',
        'border': 'var(--ood-slider-thumb-border)',
        'width': 'var(--ood-slider-thumb-size)',
        'height': 'var(--ood-slider-thumb-size)',
        transition: 'all 0.2s ease'
        },
        'IND1:hover, IND2:hover': {
        'transform': 'scale(1.1)',
        'box-shadow': 'var(--ood-slider-thumb-hover-shadow)',
        'background': 'var(--ood-slider-thumb-hover)'
        }
        },
        Behaviors:{
            HoverEffected:{IND1:'IND1',IND2:'IND2',DECREASE:'DECREASE',INCREASE:'INCREASE'},
            ClickEffected:{IND1:'IND1',IND2:'IND2',DECREASE:'DECREASE',INCREASE:'INCREASE'},
            IND:{
                onClick:function(profile, e, src){
                    var p=profile.properties;
                    if(p.disabled || p.readonly)return false;
                    var p1=ood.use(src).offset(),
                        p2=ood.Event.getPos(e),
                        arr=profile.box._v2a(profile,profile.properties.$UIvalue),
                        i1,i2,
                        type=p.type=='vertical',
                        k1=type?'top':'left',
                        k2=type?'top':'left',
                        k3=type?'height':'width',
                        cur=p2[k1]-p1[k1],
                        v=(cur/ood.use(src)[k3]())*100;
                    if(!p.isRange)
                        arr[0]=v;
                    else{
                        i1=profile.getSubNode('IND1')[k2](),
                        i2=profile.getSubNode('IND2')[k2]();
                        if(Math.abs(i1-cur)<Math.abs(i2-cur))
                            arr[0]=v;
                        else arr[1]=v;
                    }
                    profile.boxing().setUIValue(profile.box._adjustValue(profile,arr),null,null,'click');
                }
            },
            IND1:{
                onKeydown:function(profile, e, src){
                    var p=profile.properties;
                    if(p.disabled || p.readonly)return;
                    var type=p.type=='vertical',
                        key=ood.Event.getKey(e).key;
                    if(key==(type?'up':'left'))
                        profile.box._auto(profile, false);
                    if(key==(type?'down':'right'))
                        profile.box._auto(profile, true);
                },
                onKeyout:function(profile){
                    ood.Thread.abort(profile.$xid+':auto');
                },
                onKeyup:function(profile){
                    ood.Thread.abort(profile.$xid+':auto');
                },
                beforeMousedown:function(profile, e, src){
                    if(ood.Event.getBtn(e)!="left")return;
                    var p=profile.properties;
                    if(p.disabled || p.readonly)return;
                    var type=p.type=='vertical',
                        k2=type?'top':'left',
                        k3=type?'height':'width',
                        box=profile.box;
                    ood.use(src).startDrag(e,{
                        widthIncrement:p.steps?profile._size/p.steps:null,
                        dragType:'none',
                        targetReposition:true,
                        horizontalOnly:type?true:null,
                        verticalOnly:type?null:true,
                        maxLeftOffset: ood.use(src)[k2](),
                        maxRightOffset: ood.use(src).parent()[k3]()-ood.use(src)[k2](),
                        dragCursor:'default'
                    });

                    ood.use(src).css('zIndex',10).focus(true);
                    profile.getSubNode('IND2').css('zIndex',5);
                },
                beforeDragbegin:function(profile, e, src){
                    var type=profile.properties.type=='vertical';
                    ood(src)[type?'top':'left'](profile.__x=profile.$px(ood.use(src)[type?'top':'left']()));
                },
                onDrag:function(profile, e, src){
                    var offset=ood.DragDrop.getProfile().offset,
                        type=profile.properties.type=='vertical',
                        arr=profile.box._v2a(profile,profile.properties.$UIvalue);
                    arr[0]=((profile.__x+offset[type?'y':'x'])/ood.use(src).parent()[type?'height':'width']())*100;
                    profile.boxing().setUIValue(profile.box._adjustValue(profile,arr),null,null,'drag');
                },
                onDragstop:function(profile, e, src){
                    ood(src).onMouseout(true,{$force:true}).onMouseup(true);
                },
                onClick:function(){return false}
            },
            IND2:{
                onKeydown:function(profile, e, src){
                    var p=profile.properties;
                    if(p.disabled || p.readonly)return;
                    var type=p.type=='vertical',
                        key=ood.Event.getKey(e).key;
                    if(key==(type?'up':'left'))
                        profile.box._auto(profile, false);
                    if(key==(type?'down':'right'))
                        profile.box._auto(profile, true);
                },
                onKeyout:function(profile){
                    ood.Thread.abort(profile.$xid+':auto');
                },
                onKeyup:function(profile){
                    ood.Thread.abort(profile.$xid+':auto');
                },
                beforeMousedown:function(profile, e, src){
                    if(ood.Event.getBtn(e)!="left")return;
                    var p=profile.properties;
                    if(p.disabled || p.readonly)return;
                    var type=p.type=='vertical',
                        k2=type?'top':'left',
                        k3=type?'height':'width',
                        box=profile.box;
                    ood.use(src).startDrag(e,{
                        widthIncrement:p.steps?profile._size/p.steps:null,
                        dragType:'none',
                        targetReposition:true,
                        horizontalOnly:type?true:null,
                        verticalOnly:type?null:true,
                        maxLeftOffset: ood.use(src)[k2](),
                        maxRightOffset: ood.use(src).parent()[k3]()-ood.use(src)[k2](),
                        dragCursor:'default'
                    });

                    ood.use(src).css('zIndex',10).focus(true);
                    profile.getSubNode('IND1').css('zIndex',5);
                },
                beforeDragbegin:function(profile, e, src){
                    var type=profile.properties.type=='vertical';
                    ood(src)[type?'top':'left'](profile.__x=profile.$px(ood.use(src)[type?'top':'left']()));
                },
                onDrag:function(profile, e, src){
                    var offset=ood.DragDrop.getProfile().offset,
                        type=profile.properties.type=='vertical',
                        arr=profile.box._v2a(profile,profile.properties.$UIvalue);
                    arr[1]=((profile.__x+offset[type?'y':'x'])/ood.use(src).parent()[type?'height':'width']())*100;
                    profile.boxing().setUIValue(profile.box._adjustValue(profile,arr),null,null,'drag2');
                },
                onDragstop:function(profile, e, src){
                    ood(src).onMouseout(true,{$force:true}).onMouseup(true);
                },
                onClick:function(){return false}
            },
            DECREASE:{
                onMousedown:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    profile.box._auto(profile, false);
                },
                onMouseout:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    ood.Thread.abort(profile.$xid+':auto');
                },
                onMouseup:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    ood.Thread.abort(profile.$xid+':auto');
                }
            },
            INCREASE:{
                onMousedown:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    profile.box._auto(profile, true);
                },
                onMouseout:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    ood.Thread.abort(profile.$xid+':auto');
                },
                onMouseup:function(profile){
                    if(profile.properties.disabled || profile.properties.readonly)return;
                    ood.Thread.abort(profile.$xid+':auto');
                }
            },
            LABEL:{
                onClick:function(profile, e, src){
                    if(profile.properties.disabled)return false;
                    if(profile.onLabelClick)
                        profile.boxing().onLabelClick(profile, e, src);
                },
                onDblClick:function(profile, e, src){
                    if(profile.properties.disabled)return false;
                    if(profile.onLabelDblClick)
                        profile.boxing().onLabelDblClick(profile, e, src);
                },
                onMousedown:function(profile, e, src){
                    if(ood.Event.getBtn(e)!='left')return;
                    if(profile.properties.disabled)return false;
                     if(profile.onLabelActive)
                        profile.boxing().onLabelActive(profile, e, src);
                }
            }
        },
        DataModel:{
            // Modern properties
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
            
            position:'absolute',
            expression:{
                ini:'',
                action:function () {
                }
            },
            width:{
                $spaceunit:1,
                ini:'15em'
            },
            height:{
                $spaceunit:1,
                ini:'4em'
            },
            precision:0,
            numberTpl:'* - 1% ~ 2%',
            steps:0,
            value:'0:0',
            type:{
                listbox:['vertical', 'horizontal'],
                ini:'horizontal',
                action:function(v){
                    this.boxing().refresh();
                }
            },
            isRange:{
                ini:true,
                action:function(v){
                    this.boxing().refresh();
                }
            },
            showIncreaseHandle:{
                ini:true,
                action:function(v){
                    this.adjustSize();
                }
            },
            showDecreaseHandle:{
                ini:true,
                action:function(v){
                    this.adjustSize();
                }
            },
            // label
            labelSize:{
                $spaceunit:2,
                ini:0,
                action: function(v){
                    this.getSubNode('LABEL').css({display:v?'':'none'});
                    ood.UI.$doResize(this,this.properties.width,this.properties.height,true);
                }
            },
            labelPos:{
                ini:"left",
                listbox:['none','left','top', 'right', 'bottom'],
                action: function(v){
                    ood.UI.$doResize(this,this.properties.width,this.properties.height,true);
                }                
            },
            labelGap:{
                $spaceunit:2,
                ini:4,
                action: function(v){
                    ood.UI.$doResize(this,this.properties.width,this.properties.height,true);
                }
            },
            labelCaption:{
                ini:"",
                action: function(v){
                     var p=this.properties;
                     if(p.labelPos!='none'&& parseFloat(p.labelSize))
                        this.getSubNode('LABEL').html(ood.adjustRes((ood.isSet(v)?v:"")+"", true));
                }
            },
            labelHAlign:{
                ini:'right',
                listbox:['','left','center','right'],
                action: function(v){
                    this.getSubNode('LABEL').css({
                        'textAlign': v||'',
                        'justifyContent':v=='right'?'flex-end':v=='center'?'center':v=='left'?'flex-start':''
                    });
                }
            },
            labelVAlign:{
                ini:'top',
                listbox:['','top','middle','bottom'],
                action: function(v){
                    this.getSubNode('LABEL').css('align-items',v=='bottom'?'flex-end':v=='middle'?'center':v=='top'?'flex-start':'');
                }
            }
        },
        EventHandlers:{
            onLabelClick:function(profile, e, src){},
            onLabelDblClick:function(profile, e, src){},
            onLabelActive:function(profile, e, src){}
        },
        
        RenderTrigger: function() {
            // Modern feature initialization
            var self = this;
            ood.asyRun(function(){
                self.boxing().SliderTrigger();
            });
        },
        
        SliderTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // Initialize theme
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('slider-theme');
                if (savedTheme) {
                    boxing.setTheme(savedTheme);
                }
            }

            // Initialize responsive design
            if (prop.responsive !== false) {
                boxing.adjustLayout();
            }

            // Initialize accessibility
            boxing.enhanceAccessibility();
        },
        _prepareData:function(profile){
            var d=arguments.callee.upper.call(this, profile),
                N='display:none',t,v;
            d._showDes=d.showDecreaseHandle?'':N,
            d._showIns=d.showIncreaseHandle?'':N,
            d._showD2=d.isRange?'':N;
            d._cls=profile.getClass('KEY',d.type=='vertical'?'-v':'-h');
            d._labelHAlign = 'text-align:'+(v=d.labelHAlign||'')+';justify-content:'+(v=='right'?'flex-end':v=='center'?'center':v=='left'?'flex-start':'');
            d._labelVAlign = 'align-items:'+((v=d.labelVAlign)=='bottom'?'flex-end':v=='middle'?'center':v=='top'?'flex-start':'');
            d.labelShow=d.labelPos!='none'&&d.labelSize&&d.labelSize!='auto'?"":"display:none";
            d._labelSize=d.labelSize?'':0+profile.$picku();

            // adjustRes for labelCaption
            if(d.labelPos!='none'&& d.labelSize && d.labelCaption)
                d.labelCaption=ood.adjustRes(d.labelCaption,true);

            d._fi_decls = 'ood-icon-single'+(d.type=='vertical'?'up':'left');
            d._fi_incls = 'ood-icon-single'+(d.type=='vertical'?'down':'right');
            return d;
        },
        _adjustValue:function(profile,value){
            var p = profile.properties,
                b=[];
            b[0]=parseFloat(value[0])||0;
            b[1]=parseFloat(value[1])||0;
            if(p.steps){
                value=100/p.steps;
                b[0]=Math.ceil(b[0]/value);
                if(p.isRange)
                    b[1]=Math.ceil(b[1]/value);
            }
            return p.isRange?b.join(':'):(b[0]+'');
        },
        _ensureValue:function(profile, value){
            var p = profile.properties,
                a = String(value).split(':'),
                min=0,
                max=p.steps?p.steps:100,
                b=[],
                f1=function(a){return parseFloat(a)||0},
                f2=function(a){return Math.min(max, Math.max(min,a))};
            b[0]= f1(a[0]);
            if(p.isRange){
                b[1]= f1(a[1]);
                if(b[0]>b[1]){
                    a=b[1];
                    b[1]=b[0];
                    b[0]=a;
                }
            }
            b[0]= f2(b[0]);
            if(p.isRange)
                b[1]= f2(b[1]);
            return p.isRange?b.join(':'):(b[0]+'');
        },
        _v2a:function(profile,v){
            var steps=profile.properties.steps,t;
            v = typeof v == 'string'? v.split(':') : v;
            v[0]=parseFloat(v[0])||0;v[1]=parseFloat(v[1])||0;
            if(steps)v[0]=v[0]*100/steps;
            if(steps)v[1]=v[1]*100/steps;
            if(v[0]>v[1]){
                t=v[0];
                v[1]=v[0];
                v[0]=t;
            }
            return v;
        },
        _auto:function(profile, flag){
            var id=profile.$xid+':auto';
            if(ood.Thread.isAlive(id))return;
            var p=profile.properties,t,
                //%
                off=(p.steps?100/p.steps:1)*(flag?1:-1),
                task={delay:300},
                arr=profile.box._v2a(profile,p.$UIvalue),
                fun=function(){
                    arr[0] += off;
                    if(p.isRange)
                        arr[1] += off;
                    profile.boxing().setUIValue(profile.box._adjustValue(profile,arr),null,null,'auto');
                    task.delay *=0.8;
                };
            task.task=fun;
            ood.Thread(id,[task],500,null,fun,null,true).start();
        },
        _onresize:function(profile, width, height){
            var prop=profile.properties,
                type=prop.type,
                f=function(k){return profile.getSubNode(k)},
                root = f('KEY'),
                ruler = f('RULER'),
                ind = f('IND'),
                indb = f('IND1'),
                offset = profile.$px('.75em'),
                box = f('BOX'),
                label = f('LABEL'),
                cmd1 = f('INCREASE'),
                cmd2 = f('DECREASE'),
                cb=ood.browser.contentBox,
                us = ood.$us(profile),
                adjustunit = function(v,emRate){return profile.$forceu(v, us>0?'em':'px', emRate)},

                fzrate=profile.getEmSize()/root._getEmSize(),
                labelfz=label._getEmSize(fzrate),
                rulerfz = ruler._getEmSize(fzrate),
                indfz = ind._getEmSize(fzrate),

                label = profile.getSubNode('LABEL'),
                labelPos=prop.labelPos,
                labelSize=(labelPos=='none'||!labelPos)?0:profile.$px(prop.labelSize,labelfz)||0,
                labelGap=(labelPos=='none'||!labelPos)?0:profile.$px(prop.labelGap)||0,
                ll, tt, ww, hh;

            // calculate by px
            if(width && width!='auto')width=profile.$px(width);
            if(height && height!='auto')height=profile.$px(height);

            box.cssPos({
                left : adjustunit(ll = labelPos=='left'?labelSize:0),
                top : adjustunit(tt = labelPos=='top'?labelSize:0)
            });
            cmd2.css('display',prop.showDecreaseHandle?'':'none');
            cmd1.css('display',prop.showIncreaseHandle?'':'none');

            if(type=='vertical'){
                box.cssSize({
                    width : '',
                    height : adjustunit(hh = height===null?null:Math.max(0,(height - ((labelPos=='top'||labelPos=='bottom')?labelSize:0))))
                });
                var w=profile.$px('1em'),
                    w1=prop.showIncreaseHandle?w:0,
                    w2=prop.showDecreaseHandle?w:0,
                    w3=ood.browser.contentBox?indb._borderH('top'):0;
                if(hh){
                    ruler.top(adjustunit(w1+offset,rulerfz))
                        .height(adjustunit(hh-w1-w2-2*offset,rulerfz));
                    ind.top(adjustunit(w1+offset-w3,indfz))
                        .height(adjustunit(hh-w1-w2-2*offset,indfz));
                }

            if(labelSize)
                label.cssRegion({
                    left: adjustunit(width===null?null:Math.max(0,labelPos=='right'?(box.width()+labelGap):0),labelfz),
                    top:  adjustunit(height===null?null:Math.max(0,labelPos=='bottom'?(height-labelSize+labelGap):0),labelfz), 
                    width: '',
                    height: adjustunit(height===null?null:Math.max(0,((labelPos=='top'||labelPos=='bottom')?(labelSize-labelGap):height)),labelfz)
                });
            }else{
                box.cssSize({
                    width : adjustunit(ww = width===null?null:Math.max(0,(width - ((labelPos=='left'||labelPos=='right')?labelSize:0)))),
                    height : ''
                });
                var w=profile.$px('1em'),
                    w1=prop.showDecreaseHandle?w:0,
                    w2=prop.showIncreaseHandle?w:0,
                    w3=!cb?0:indb._borderW('left');
                if(ww){
                    ruler.left(adjustunit(w1+offset, rulerfz))
                        .width(adjustunit(ww-w1-w2-2*offset,rulerfz));
                    ind.left(adjustunit(w1+offset-w3, indfz))
                        .width(adjustunit(ww-w1-w2-2*offset,indfz));
                }

            if(labelSize)
                label.cssRegion({
                    left: adjustunit(width===null?null:Math.max(0,labelPos=='right'?(width-labelSize+labelGap):0),labelfz),
                    top:  adjustunit(height===null?null:Math.max(0,labelPos=='bottom'?(box.height()+labelGap):0),labelfz), 
                    width: adjustunit(width===null?null:Math.max(0,((labelPos=='left'||labelPos=='right')?(labelSize-labelGap):width)),labelfz),
                    height: adjustunit(height===null?null:Math.max(0,((labelPos=='top'||labelPos=='bottom')?(labelSize-labelGap):height)),labelfz)
                });

            }
        }
    }
});