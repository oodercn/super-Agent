ood.Class('ood.UI.DatePicker', ['ood.UI',"ood.absValue"], {
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
                    p = profile.properties;
                cls._to(profile,value,true);
                if(profile.keys.CAPTION)
                    profile.getSubNode('CAPTION').html(ood.Date.getText(value,'ymd',p.firstDayOfWeek),false);
            });
        },
        getDateFrom:function(){
            return this.get(0)._realstart;
        },
        
        // Set theme
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot(),
                    border = profile.getSubNode('BORDER'),
                    bar = profile.getSubNode('BAR'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    btns = root.query('.oodfont'),
                    inputs = root.query('.ood-ui-draggable'),
                    table = profile.getSubNode('BODY'),
                    cells = root.query('.ood-node-td'),
                    headers = root.query('.ood-node-th');

                // Add base classes
                root.addClass('datepicker-themed');
                
                // Add component specific classes
                border.addClass('datepicker-border');
                bar.addClass('datepicker-bar');
                main.addClass('datepicker-main');
                tail.addClass('datepicker-tail');
                btns.addClass('datepicker-button');
                inputs.addClass('datepicker-input');
                cells.addClass('datepicker-cell');
                headers.addClass('datepicker-header');
                if (table && !table.isEmpty()) {
                    table.addClass('datepicker-table');
                }

                // Apply theme class
                root.removeClass('datepicker-dark');
                if (theme === 'dark') {
                    root.addClass('datepicker-dark');
                }
                
                // Save theme settings
                localStorage.setItem('datepicker-theme', theme);
            });
        },
        
        // Get current theme
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('datepicker-theme') || 'light';
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
                    bar = profile.getSubNode('BAR'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    inputs = root.query('.ood-ui-draggable'),
                    btns = root.query('.oodfont'),
                    prop = profile.properties;

                // Adjust layout for small screens
                if (width < 768) {
                    root.addClass('datepicker-mobile');
                    
                    // Adjust button size for mobile
                    btns.css({
                        'min-width': '2.2em',
                        'min-height': '2.2em',
                        'font-size': '1.1em'
                    });
                    
                    // Adjust inputs for mobile
                    inputs.css({
                        'min-height': '2em',
                        'font-size': '0.9em',
                        'padding': '0.3em'
                    });
                } else {
                    root.removeClass('datepicker-mobile');
                    
                    // Restore desktop styles
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
                }

                // Special handling for extra small screens
                if (width < 480) {
                    root.addClass('datepicker-tiny');
                    
                    // Adjust calendar table display
                    main.css({
                        'font-size': '0.8em'
                    });
                    
                    tail.css({
                        'font-size': '0.8em'
                    });
                } else {
                    root.removeClass('datepicker-tiny');
                    
                    main.css({
                        'font-size': ''
                    });
                    
                    tail.css({
                        'font-size': ''
                    });
                }
            });
        },
        
        // Enhance accessibility support
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    bar = profile.getSubNode('BAR'),
                    main = profile.getSubNode('MAIN'),
                    tail = profile.getSubNode('TAIL'),
                    inputs = root.query('.ood-ui-draggable'),
                    btns = root.query('.oodfont'),
                    cells = root.query('.ood-node-td'),
                    headers = root.query('.ood-node-th'),
                    properties = profile.properties;

                // Add ARIA attributes to container
                root.attr({
                    'role': 'application',
                    'aria-label': ood.getRes('UI.datepicker.label')
                });
                
                // Add ARIA attributes to toolbar
                if (bar && !bar.isEmpty()) {
                    bar.attr({
                        'role': 'toolbar',
                        'aria-label': ood.getRes('UI.datepicker.toolbar')
                    });
                }
                
                // Add ARIA attributes to calendar grid
                if (main && !main.isEmpty()) {
                    main.attr({
                        'role': 'grid',
                        'aria-label': ood.getRes('UI.datepicker.calendar')
                    });
                }
                
                // Add ARIA attributes to navigation buttons
                btns.each(function(btn) {
                    var btnElement = ood(btn),
                        btnClass = btnElement.attr('class') || '',
                        btnLabel = ood.getRes('UI.datepicker.button');
                    
                    if (btnClass.indexOf('doubleleft') > -1) {
                        btnLabel = ood.getRes('UI.datepicker.prevYear');
                    } else if (btnClass.indexOf('singleleft') > -1) {
                        btnLabel = ood.getRes('UI.datepicker.prevMonth');
                    } else if (btnClass.indexOf('singleright') > -1) {
                        btnLabel = ood.getRes('UI.datepicker.nextMonth');
                    } else if (btnClass.indexOf('doubleright') > -1) {
                        btnLabel = ood.getRes('UI.datepicker.nextYear');
                    } else if (btnClass.indexOf('close') > -1) {
                        btnLabel = ood.getRes('UI.datepicker.close');
                    }
                    
                    btnElement.attr({
                        'role': 'button',
                        'aria-label': btnLabel,
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // Add ARIA attributes to inputs
                inputs.each(function(input) {
                    var inputElement = ood(input);
                    inputElement.attr({
                        'role': 'spinbutton',
                        'aria-label': ood.getRes('UI.datepicker.dateInput'),
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // Add ARIA attributes to date cells
                cells.each(function(cell) {
                    var cellElement = ood(cell);
                    cellElement.attr({
                        'role': 'gridcell',
                        'tabindex': properties.disabled ? '-1' : '0'
                    });
                });
                
                // Add ARIA attributes to headers
                headers.each(function(header) {
                    var headerElement = ood(header);
                    headerElement.attr({
                        'role': 'columnheader'
                    });
                });
            });
        }
    },
    Initialize:function(){
        var self=this,
            id=ood.UI.$ID,
            tag=ood.UI.$tag_special,
            cls=ood.UI.$CLS,
            key=self.KEY;
            
        self.addTemplateKeys(['H', 'COL', 'W','TBODY', 'THEADER','TD']);
        var colgroup = '<colgroup id="'+key+'-COL:'+id+':"  class="'+tag+'COL_CS'+tag+' ood-custom {comcls}"  style="'+tag+'COL_CS'+tag+'"><col width="1px"/><col width=""/><col width=""/><col width=""/><col width=""/><col width=""/><col width=""/><col width=""/></colgroup>',
            thead1='<thead ID="'+key+'-THEADER:'+id+':" class="'+tag+'THEADER_CS'+tag+' ood-custom {comcls}"  style="'+tag+'THEADER_CS'+tag+'" ><tr height="1px"><th id="'+key+'-H:'+id+':7" class="ood-node  ood-node-th ood-uiborder-b ood-uiborder-r '+cls+'-h '+cls+'-w '+tag+'H_CC'+tag+' ood-custom {comcls}" style="'+tag+'H_CS'+tag+'"></th>',
            thead2='</tr></thead>',
            th='<th id="'+key+'-H:'+id+':@" class="ood-node ood-node-th ood-uiborder-b ood-uiborder-r '+cls+'-h '+tag+'H_CC'+tag+' ood-custom {comcls}"  style="'+tag+'H_CS'+tag+'">@</th>',
            tbody1 = '<tbody id="'+key+'-TBODY:'+id +':"  class="'+tag+'TBODY_CS'+tag+' ood-custom {comcls}"  style="'+tag+'TBODY_CS'+tag+'" >',
            tbody2 = '</tbody>',
            tr1='<tr>',
            tr2='</tr>',
            td1='<th id="'+key+'-W:'+id+':@"  class="ood-node ood-node-th ood-uiborder-b ood-uiborder-r '+cls+'-w '+tag+'W_CC'+tag+' ood-custom {comcls}"  style="'+tag+'W_CS'+tag+'">@</th>',
            td2='<td id="'+key+'-TD:'+id+':@" class="ood-node ood-uicell  ood-node-td ood-uiborder-b ood-uiborder-r '+cls+'-td '+tag+'TD_CC'+tag+' ood-custom {comcls}"  style="'+tag+'TD_CS'+tag+'" '+ood.$IEUNSELECTABLE()+' >'+
                '</td>',
            body,i,j,k,l,a=[],b=[];
        for(i=0;i<7;i++)
            b[b.length]= th.replace(/@/g,i);

        k=l=0;
        for(i=0;i<48;i++){
            j=i%8;
            a[a.length]= (j==0?tr1:'') + (j==0?td1:td2).replace(/@/g,j==0?l:k) + (j==7?tr2:'');
            if(j!==0)k++;
            else l++;
        }

        body=colgroup+thead1+b.join('')+thead2+tbody1+a.join('')+tbody2;

        self.setTemplate({
            tagName : 'div',
            style:'{_style};height:auto;',
            //onselectstart:'return false',
            BORDER:{
                tagName : 'div',
                className: 'ood-uiborder-outset ood-uiborder-box ood-uiborder-radius-big',
                BAR:{
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
                        tagName:'div',
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
                        YEAR:{
                            $order:2,
                            className:'ood-ui-draggable ood-uibase ood-uiborder-flat ood-uiborder-radius'
                        },
                        YTXT:{$order:3,text:'-'},
                        MONTH:{
                            $order:4,
                            className:'ood-ui-draggable ood-uibase ood-uiborder-flat ood-uiborder-radius'
                        },
                        MTXT:{$order:5,text:'-'},
                        DAY:{
                            $order:6,
                            className:'ood-ui-draggable ood-uibase ood-uiborder-flat ood-uiborder-radius'
                        },
                        NEXT:{
                            $order:7,
                            className:'oodfont',
                            $fonticon:'ood-icon-singleright',
                            tabindex: '{tabindex}'
                        },
                        NEXT2:{
                            $order:8,
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
                        className:'ood-uicon-maini ood-uicon-maini ood-uibar',
                        CON:{
                            tagName:'div',
                            className:'ood-uiborder-inset',
                            BODY:{
                                tagName:'table',
                                cellpadding:"0",
                                cellspacing:"0",
                                text:body
                            }
                        }
                    }
                },
                TAIL:{
                    $order:3,
                    tagName:'div',
                    className:'ood-uicon-main ood-uibar',
                    TAILI:{
                        tagName:'div',
                        className:'ood-uicon-maini ood-uibar',
                        CAPTION:{
                            tagName:'div',
                            style:'{_nocap}',
                            text : '{caption}',
                            $order:0
                        },
                        TIME:{
                            style:"{_timectrl}",
                            tagName:'div',
                            TPRE2:{
                                $order:0,
                                className:'oodfont',
                                $fonticon:'ood-icon-doubleleft',
                                tabindex: '{tabindex}'
                            },
                            TPRE:{
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
                            TNEXT:{
                                $order:6,
                                className:'oodfont',
                                $fonticon:'ood-icon-singleright',
                                tabindex: '{tabindex}'
                            },
                            TNEXT2:{
                                $order:7,
                                className:'oodfont',
                                $fonticon:'ood-icon-doubleright',
                                tabindex: '{tabindex}'
                            }
                        },
                        TODAY:{
                             tabindex: '{tabindex}',
                             className:'oodfont',
                            $fonticon:'ood-icon-date',
                             title:"{_todaytitle}"
                        },
                        SET:{
                            tagName:"button",
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
        Appearances:{
            KEY:{
            },
            'TBART, BBART':{
                'border-spacing':0,
                'border-collapse':'separate'
            },
            BORDER:{
            },
            BODY:{
                position:'relative'
            },
            BARCMDL:{
                top:'.125em'
            },
            TAILI:{
                position:'relative',
                 padding:'.5em 0 0 0'
            },
            TIME:{
                'padding':'.25em 1.5em'
            },
            SET:{
                position:'absolute',
                display:'none',
                top:'.125em',
                right:'.5em'
            },
            TODAY:{
                position:'absolute',
                top:'.25em',
                left:'.125em',
                display:ood.$inlineBlock,
                cursor:'default'
            },
            'PRE,PRE2,NEXT,NEXT2,TPRE,TPRE2,TNEXT,TNEXT2':{
                $order:0,
                display:ood.$inlineBlock,
                position:'relative',
                margin:'0 .25em',
                'vertical-align': 'middle',
                cursor:'default'
            },
            'YEAR,MONTH,DAY,HOUR,MINUTE':{
                $order:4,
                'font-weight':'bold',
                'vertical-align': 'middle',
                cursor:'e-resize',
                margin:'0 .25em',
                'padding':'0 .25em'
            },
            YEAR:{
            },
            'MONTH, DAY,HOUR, MINUTE':{
            },
            CAPTION:{
                'text-align':'center',
                'vertical-align':ood.browser.ie6?'baseline':'middle',
                'font-size':'1em'
            },
            MAINI:{
                 'padding':'.5em .3333em .3333em 0'
            },
            BODY:{
                overflow: 'visible'
            },
            TD:{
                $order:1,
                'text-align':'center'
            },
            'TD-checked':{
                $order:4,
                'font-weight':'bold'
            },
            'W,H':{
                $order:3,
                'vertical-align':'middle',
                'text-align':'center',
                'padding':'.25em'
            },
            W:{
                $order:4,
                padding:'.125em'
            },
            H:{
                $order:4,
                padding:'.25em 0.6666667em'
            }
        },
        Behaviors:{
            HoverEffected:{CLOSE:'CLOSE',TD:'TD',PRE:'PRE',PRE2:'PRE2',NEXT:'NEXT',NEXT2:'NEXT2',TPRE:'TPRE',TPRE2:'TPRE2',TNEXT:'TNEXT',TNEXT2:'TNEXT2',SET:'SET', TODAY:'TODAY'},
            ClickEffected:{CLOSE:'CLOSE',TD:'TD',PRE:'PRE',PRE2:'PRE2',NEXT:'NEXT',NEXT2:'NEXT2',TPRE:'TPRE',TPRE2:'TPRE2',TNEXT:'TNEXT',TNEXT2:'TNEXT2',SET:'SET', TODAY:'TODAY'},
            KEY:{onClick:function(){return false}},
            TD:{
                onClick:function(profile, e, src){
                    var p=profile.properties,
                        id=profile.getSubId(src),
                        map=profile.$daymap,
                        v=map[id];
                    if(p.disabled||p.readonly)return false;

                    ood.use(src).onMouseout(true,{$force:true});

                    v = ood.Date.add(profile.$tempValue, 'd', ood.Date.diff(profile.$tempValue, v, 'd', p.firstDayOfWeek));
                    profile.box._to(profile,v);
                    
                    // set dir
                    if(!p.timeInput)
                        //onClick event
                        profile.boxing().setUIValue(v,null,null,'click');
                },
                onDblclick:function(profile,e,src){
                    var p=profile.properties;
                    if(p.timeInput){
                        ood.use(src).onMouseout(true,{$force:true});
                        profile.boxing().setUIValue(profile.$tempValue, true,null,'dblclick');
                    }
                }
            },
            TODAY:{
                onClick:function(profile,e,src){
                    ood.use(src).onMouseout(true,{$force:true});
                    profile.boxing().setUIValue(
                        profile.properties.timeInput ?
                        new Date :
                        ood.Date.getTimSpanStart(new Date,'d',1)
                    ,true,null,'today');
                }
            },
            SET:{
                onClick:function(profile,e,src){
                    ood.use(src).onMouseout(true,{$force:true});
                    profile.boxing().setUIValue(profile.$tempValue, true,null,'set');
                }
            },
            CLOSE:{
                onClick:function(profile, e, src){
                    var p = profile.properties,
                        instance = profile.boxing();
                    if(p.disabled||p.readonly)return;
                    if(false===instance.beforeClose(profile, src)) return;
                    instance.destroy(true);
                }
            },
            PRE:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'m',-1));
                }
            },
            NEXT:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'m',1));
                }
            },
            PRE2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'y',-1));
                }
            },
            NEXT2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'y',1));
                }
            },
            TPRE:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'n',-1));
                }
            },
            TNEXT:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'n',1));
                }
            },
            TPRE2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'h',-1));
                }
            },
            TNEXT2:{
                onClick:function(profile, e, src){
                    var p = profile.properties;
                    if(p.disabled||p.readonly)return;
                    profile.box._to(profile,ood.Date.add(profile.$tempValue,'h',1));
                }
            },
            YEAR:{
                beforeMousedown:function(profile, e, src){
                    return profile.box._ondown(profile,e,src,10);
                },
                onDrag:function(profile, e, src){
                    var count,off = ood.DragDrop.getProfile().offset;
                    count=parseInt(profile.$year,10)+parseInt(off.x/10,10);
                    if(profile.$temp!=count){
                        profile.$temp2=parseInt(off.x/10,10);
                        profile.getSubNode('YEAR').html(count,false);
                    }
                },
                onDragstop:function(profile, e, src){
                    return profile.box._onds(profile,e,src,'y');
                }
            },
            MONTH:{
                beforeMousedown:function(profile, e, src){
                    return profile.box._ondown(profile,e,src,20);
                },
                onDrag:function(profile, e, src){
                    var count,off = ood.DragDrop.getProfile().offset;
                    count=parseInt(profile.$month,10)+(parseInt(off.x/20,10)%12);
                    count=(count%12+12)%12;
                    if(profile.$temp!=count){
                        profile.$temp=count;
                        profile.$temp2=count-profile.$month+1;
                        profile.getSubNode('MONTH').html(((count+1)<=9?"0":"")+(count+1),false);
                    }
                },
                onDragstop:function(profile, e, src){
                    return profile.box._onds(profile,e,src,'m');
                }
            },
            DAY:{
                beforeMousedown:function(profile, e, src){
                    return profile.box._ondown(profile,e,src,10);
                },
                onDrag:function(profile, e, src){
                    var date=new Date(profile.$year,profile.$month,0),
                        days=date.getDate();

                    var p=profile.properties,
                        count,
                        off = ood.DragDrop.getProfile().offset;
                    count=parseInt(profile.$day,10)+(parseInt(off.x/10,10)%days);
                    count=(count%days+days)%days + 1;
                    if(profile.$temp!=count){
                        profile.$temp=count;
                        profile.$temp2=count-profile.$day;
                        profile.getSubNode('DAY').html((count<=9?"0":"")+count,false);
                    }
                },
                onDragstop:function(profile, e, src){
                    return profile.box._onds(profile,e,src,'d');
                }
            },
            HOUR:{
                beforeMousedown:function(profile, e, src){
                    return profile.box._ondown(profile,e,src,20);
                },
                onDrag:function(profile, e, src){
                    return profile.box._ondrag(profile,20,24,'HOUR',profile.$hour);
                },
                onDragstop:function(profile, e, src){
                    return profile.box._onds(profile,e,src,'h');
                }
            },
            MINUTE:{
                beforeMousedown:function(profile, e, src){
                    return profile.box._ondown(profile,e,src,10);
                },
                onDrag:function(profile, e, src){
                    return profile.box._ondrag(profile,10,60,'MINUTE',profile.$minute);
                },
                onDragstop:function(profile, e, src){
                    return profile.box._onds(profile,e,src,'n');
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
            
            timeInput:{
                ini:false,
                action:function(v){
                    this.getSubNode('CAPTION').css('display',v?'none':'block');
                    this.getSubNode('SET').css('display',v?'block':'none');
                    this.getSubNode('TIME').css('display',v?'block':'none');
                    this.getSubNode('TODAY').attr("title",ood.getRes(v?"inline.now":"inline.today"));
                }
            },
            height:{
                $spaceunit:1,
                ini:'auto',
                readonly:true
            },
            width:{
                $spaceunit:1,
                ini:'auto',
                readonly:true
            },
            value:{
                ini:new Date,
                format:'date'
            },
            closeBtn:{
                ini:true,
                action:function(v){
                    this.getSubNode('CLOSE').css('display',v?'':'none');
                }
            },
            firstDayOfWeek:{
                ini:0,
                action:function(){
                    this.boxing().refresh();
                }
            },
            offDays:{
                ini:'60',
                action:function(){
                    this.boxing().refresh();
                }
            },
            hideWeekLabels:{
                ini:false,
                action:function(){
                    this.boxing().refresh();
                }
            },
            dateInputFormat:{
                ini:"yyyy-mm-dd",
                listbox:["yyyy-mm-dd","mm-dd-yyyy","dd-mm-yyyy"],
                action:function(){
                    this.boxing().refresh();
                }
            }
        },
        EventHandlers:{
            beforeClose:function(profile, src){}
        },
        _prepareData:function(profile){
            var data=arguments.callee.upper.call(this, profile);
            var nodisplay='display:none';
            data.closeDisplay = data.closeBtn?'':nodisplay;
            
            var none="display:none;";
            if(profile.properties.timeInput){
                data._todaytitle=ood.getRes("inline.now");
                data._nocap=none;
            }else{
                data._todaytitle=ood.getRes("inline.today");
                data._timectrl=none;
            }
            data._set = ood.wrapRes('inline.set');

            return data;
        },
        _ensureValue:function(profile, value){
            var d;
            if(value){
                if(ood.isDate(value))
                    d=value;
                else if(ood.isFinite(value))
                    d=new Date(parseInt(value,10));
            }
            d = d||new Date;
            if(!profile.properties.timeInput)
                d=ood.Date.getTimSpanStart(d,'d');
            return d;
        },
        RenderTrigger:function(){
            var self=this, p=self.properties, o=self.boxing(), b=self.box;
            b._setWeekLabel(self);
            
            var hash={yyyy:'YEAR',mm:'MONTH',dd:'DAY'},arr=p.dateInputFormat.split('-');
            if(hash[arr[0]] && hash[arr[1]] && hash[arr[2]]){
                self.getSubNode('YTXT').addPrev(self.getSubNode(hash[arr[0]]));
                self.getSubNode('MTXT').addPrev(self.getSubNode(hash[arr[1]]));
                self.getSubNode('MTXT').addNext(self.getSubNode(hash[arr[2]]));
            }
            
//            self.getSubNode('YTXT').html(ood.wrapRes('date.Y'),false);
//            self.getSubNode('MTXT').html(ood.wrapRes('date.M'),false);
            
            // Modern feature initialization
            ood.asyRun(function(){
                self.boxing().DatePickerTrigger();
            });
        },
        
        DatePickerTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // Initialize theme
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('datepicker-theme');
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
        _getWeekNodes:function(profile){
            return profile.$weeks || (profile.$weeks=profile.getSubNode('W',true));
        },
        _getTDNodes:function(profile){
            return profile.$tds || (profile.$tds=profile.getSubNode('TD',true));
        },
        _getLabelNodes:function(profile){
            return profile.$days || (profile.$days=profile.getSubNode('TD',true));
        },
        _getHeaderNodes:function(profile){
            return profile.$header || (profile.$header=profile.getSubNode('H',true));
        },
        _setWeekLabel:function(profile){
            var p=profile.properties;

            // for week
            var fw=p.firstDayOfWeek,
                f=function(id){
                id=profile.getSubId(id); 

                // The special one
                if(id=='7')return id;
                
                id=(parseInt(id,10)+fw);
                return id<7?id:(id-7);
            };

            profile.box._getHeaderNodes(profile).each(function(node,i){
                node.innerHTML=ood.wrapRes('date.WEEKS.'+f(node.id))
            });
            
            // for weeklable
            if(p.hideWeekLabels){
                profile.getSubNode('BODY').query('TR').first().remove();
                profile.getSubNode('COL').first().remove();
            }

            // for free days            
            var cls2="ood-uicell-alt",
                fdmap={};
            if(p.offDays){
                ood.arr.each(p.offDays.split(""),function(i){
                    i=parseInt(i,10);
                    if(i>=0 && i<=6)
                        fdmap[i]=1;
                });
                profile.box._getTDNodes(profile).each(function(node,i){
                    i = ((i+fw) - 7*parseInt((i+fw)/7,10)) ;
                    if(fdmap[i])node.className=node.className + " " +cls2;
                    else node.className=node.className.replace(cls2,"");
                });
            }
            
        },
        _setBGV:function(profile, v, m){
            var date=ood.Date,
                p=profile.properties,
                daymap=profile.$daymap||(profile.$daymap=[]),
                t,n;
            profile.box._getLabelNodes(profile).each(function(node,i){
                n=date.add(v,'d',i);
                daymap[i]=n;
                t=date.get(n,'m')==m?'#':'<p class="ood-node ood-node-p ood-ui-readonly ood-custom {comcls}">#</p>';
                n=date.get(n,'d');
                node.innerHTML = t.replace('#',n);
            });

            if(!p.hideWeekLabels)
                profile.box._getWeekNodes(profile).each(function(node,i){
                    node.innerHTML=date.get(date.add(v,'ww',i),'ww',p.firstDayOfWeek);
                });
        },
        _to:function(profile, time, force){
            var p = profile.properties,
                fw = p.firstDayOfWeek,
                date=ood.Date,
                keys=profile.keys,
                uiv=p.$UIvalue,
                index=-1,
                node,
                temp,
                _realstart = date.getTimSpanStart(date.getTimSpanStart(time,'m'),'ww',1,fw),
                m=date.get(time,'m',fw);

            profile.$tempValue=time;
            this._setBGV(profile, profile._realstart=_realstart, m);

            //remove checked css class
            if(profile.$selnode)
                profile.$selnode.tagClass('-checked',false);
            //[[add cecked css class
            ood.arr.each(profile.$daymap,function(o,i){
                if(date.get(o,'m',fw)+'-'+date.get(o,'d',fw)==date.get(time,'m',fw)+'-'+date.get(time,'d',fw)){
                    index=i;
                    return false;
                }
            });
            node=this._getTDNodes(profile).get()[index];
            (profile.$selnode=ood([node]).tagClass('-checked'));
            //]]
            
            //[[ show dirty
            profile.getSubNode('SET').css('display',(force||uiv.getTime()==time.getTime())?'none':'block');
            profile.getSubNode('CAPTION').css('color',(force||uiv.getTime()==time.getTime())?'':'var(--error-color)');
            //]]

            temp=date.get(time,'y',fw);
            if(profile.$year!=temp){
                profile.$year=temp;
                profile.getSubNode('YEAR').html(temp,false);
            }
            temp=date.get(time,'m',fw)+1;
            if(profile.$month!=temp){
                profile.$month=temp;
                profile.getSubNode('MONTH').html((temp<=9?"0":"")+temp,false);
            }
            temp=date.get(time||time,'d',fw);
            if(profile.$day!=temp){
                profile.$day=temp;
                profile.getSubNode('DAY').html((temp<=9?"0":"")+temp,false);
            }
            temp=date.get(time,'h',fw);
            if(profile.$hour!=temp){
                profile.$hour=temp;
                profile.getSubNode('HOUR').html((temp<=9?"0":"")+temp,false);
            }
            temp=date.get(time,'n',fw);
            if(profile.$minute!=temp){
                profile.$minute=temp;
                profile.getSubNode('MINUTE').html((temp<=9?"0":"")+temp,false);
            }
        },
        _ondown:function(profile, e, src,increment){
            if(ood.Event.getBtn(e)!="left")return;
            ood.use(src).startDrag(e, {
                dragType:'blank',
                targetReposition:false,
                widthIncrement:increment,
                dragCursor:true
            });
            profile.$temp=profile.$temp2=0;
        },
        _ondrag:function(profile,increment,max,key,data){
            var p=profile.properties,
                count,
                off = ood.DragDrop.getProfile().offset;
            count=parseInt(data,10)+(parseInt(off.x/increment,10)%max);
            count=(count%max+max)%max;
            if(profile.$temp!=count){
                profile.$temp=count;
                profile.$temp2=count-data;
                profile.getSubNode(key).html((count<=9?"0":"")+count,false);
            }
        },
        _onds:function(profile, e, src, type){
            if(profile.$temp2){
                var p=profile.properties,
                    v = ood.Date.add(profile.$tempValue,type,profile.$temp2);
                profile.box._to(profile,v);
            }
            profile.$temp=profile.$temp2=0;
        },
        _onresize:function(){}
    }
});