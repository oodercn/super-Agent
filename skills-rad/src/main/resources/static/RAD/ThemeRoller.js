ood.Class('RAD.ThemeRoller', 'ood.Module',{
    Instance:{
        iniComponents : function(){
            // [[Code created by EUSUI RAD Studio
            var host=this, children=[], append=function(child){children.push(child.get(0));};
            
            append(
                ood.create("ood.UI.Dialog")
                .setHost(host,"ood_ui_dialog41")
                .setLeft("0.8333333333333334em")
                .setTop("0.8333333333333334em")
                .setWidth("19.166666666666668em")
                .setHeight("45em")
                .setCaption("EUSUI Theme Roller")
                .setMinBtn(false)
                .setMaxBtn(false)
                .setRestoreBtn(false)
            );
            
            host.ood_ui_dialog41.append(
                ood.create("ood.UI.Block")
                .setHost(host,"_themeBlcok")
                .setDock("top")
                .setLeft("7.5em")
                .setHeight("2.9166666666666665em")
                .setBorderType("none")
                .setOverflow("hidden")
                );
            
            host._themeBlcok.append(
                ood.create("ood.UI.ComboInput")
                .setHost(host,"_theme")
                .setDirtyMark(false)
                .setLeft("0.25em")
                .setTop("0.5em")
                .setWidth("17.166666666666668em")
                .setHeight("2.08333em")
                .setLabelSize("8em")
                .setLabelGap("0.3333333333333333em")
                .setLabelCaption("Base Theme")
                .setType("listbox")
                .setParentID("ood_config_block")
                .setItems([{
                    "id":"default",
                    "caption":"default"
                },
                {
                    "id":"army",
                    "caption":"army"
                },
                {
                    "id":"classic",
                    "caption":"classic"
                },
                {
                    "id":"darkblue",
                    "caption":"darkblue"
                },
                {
                    "id":"electricity",
                    "caption":"electricity"
                },
                {
                    "id":"lightblue",
                    "caption":"lightblue"
                },
                {
                    "id":"moonify",
                    "caption":"moonify"
                },
                {
                    "id":"orange",
                    "caption":"orange"
                },
                {
                    "id":"pink",
                    "caption":"pink"
                },
                {
                    "id":"red",
                    "caption":"red"
                },
                {
                    "id":"vista",
                    "caption":"vista"
                },
                {
                    "id":"webflat",
                    "caption":"webflat"
                }])
                .setValue("default")
                .afterUIValueSet("_theme_afteruivalueset")
                );
            
            host.ood_ui_dialog41.append(
                ood.create("ood.UI.Block")
                .setHost(host,"ood_ui_panel3")
                .setDock("fill")
                .setBorderType("inset")
                );
            
            host.ood_ui_panel3.append(
                ood.create("ood.UI.Div")
                .setHost(host,"_paneBottom")
                .setDock("fill")
                .setOverflow("overflow-y:auto")
                );
            
            host._paneBottom.append(
                ood.create("ood.UI.FoldingTabs")
                .setHost(host,"_tabs")
                .setShowDirtyMark(false)
                .setSelMode("multi")
                .setOverflow("hidden")
                .beforePagePop("__tabs_beforepagepop")
                .setCustomStyle({
                    "PANEL":"line-height:2.2em"
                }
                )
                );
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        },

        customAppend : function(parent, subId, left, top){
            return false;
        },
        events:{
            "onRender" : "_page_onrender"
        },
        _page_onrender:function (module, threadid){
            module._prepare(module);
        },
        setTargetContainer:function(targetContainer, theme){
             this._themeRoller = targetContainer;
             if(theme){
                 if(this._theme.getItemByItemId(theme)){
                     this._theme.setUIValue(theme);
                 }
             }
        },
        fireChangeEvent:function(value){
            this.fireEvent("onChange",[value, this._dirty1, this._theme.getUIValue()]);
        },
        _prepare:function(module){
            var ns=this,
                // to cover image background
                prev=''+
                /*reset image background */
                '.ood-uicon-main, .ood-uicon-maini, .ood-uibar-tdl, .ood-uibar-tdm, .ood-uibar-tdr, .ood-uibar-tdlt, .ood-uibar-tdmt, .ood-uibar-tdrt{background-image:none !important;}'+
                '.ood-tabs-item{background-image:none !important;}'+
                '.ood-hcell, .ood-treegrid-fhcell, .ood-treegrid-hcell, .ood-treegrid-hscell, .ood-treegrid-header1, .ood-treegrid-header2{background-image:none !important;}'+
                '.ood-uiborder-box{ border-width:1px;}',
                items=[
                    // Font
                    {
                        id:'Font Setting',
                        setting:[ 
                            {
                                id:'Font Family',
                                map:[{
                                    clsname:'.ood-ui-ctrl', 
                                    stylename:'font-family'
                                }]
                            },
                            {
                                id:'Font Weight',
                                map:[{
                                    clsname:'.ood-ui-ctrl', 
                                    stylename:'font-weight'
                                }]
                            },
                            {
                                id:'Font Size',
                                map:[{
                                    clsname:'.ood-ui-ctrl', 
                                    stylename:'font-size'
                                }]
                            },
                            {
                                id:'Font Color',
                                status:['default','readonly','disabled','highlight'],
                                map:[{
                                    clsname:'.ood-ui-ctrl', 
                                    stylename:'color'
                                }]
                            },
                            {
                                id:'FontIcon Color',
                                status:['default','hover','active','checked'],
                                map:[{
                                    clsname:'.oodcon', 
//                                    pclsname:'.ood-ui-ctrl',
                                    stylename:'color'
                                }]
                            }
                        ],
                        template: '' +
                        '.ood-ui-ctrl, .ood-ui-reset{' +
                        '    color:[Font Color];' +
                        '    font-family:[Font Family];' +
                        '    font-size:[Font Size];' +
                        '    font-weight:[Font Weight];' +
                        '}' +
                        '.ood-title-node{'+
                        '    font-size:1.1667em;'+
                        '}'+                    
                        '.oodfont, .oodcon{' +
                        '    font-size:1.3333333333333333em;'+
                        '    color:[FontIcon Color];' +
                        '}' +
                        '.oodfont-hover, .oodcon-hover{' +
                        '    color:[FontIcon Color>hover];' +
                        '}' +
                        '.oodfont-active, .oodcon-active{' +
                        '    color:[FontIcon Color>active];' +
                        '}' +
                        '.oodfont-checked, .oodcon-checked{' +
                        '    color:[FontIcon Color>checked];' +
                        '}' +
                        '.ood-node-readonly, ' +
                        'input[readonly], textarea[readonly], input:read-only, textarea:read-only,' +
                        '.ood-ui-readonly, .ood-ui-readonly .ood-node,' +
                        '.ood-ui-itemreadonly, .ood-ui-itemreadonly .ood-node{' +
                        '    color:  [Font Color>readonly] !important;' +
                        '}' +
                        '.ood-node-disabled,' +
                        'button:disabled, a:disabled, input:disabled, textarea:disabled, ' +
                        '.ood-ui-disabled, .ood-ui-disabled .ood-node,' +
                        '.ood-ui-itemdisabled, .ood-ui-itemdisabled .ood-node,' +
                        '.ood-uicell-disabled, .ood-uicell-disabled .ood-node{' +
                        '   color: [Font Color>disabled] !important;' +
                        '}' +
                        '.ood-ui-ctrl-highlight, .ood-node-highlight, .ood-uibar-checked, .ood-uibar-expand, '+
                        '.ood-uimenu-hover, .ood-uimenu-active{'+
                        '    color: [Font Color>highlight];'+
                        '}'+
                        '.ood-uitembg-checked{'+
                        '    color: [Font Color>highlight];'+
                        '}'+
                        '.ood-uicell-checked,'+
                        '.ood-treegrid-cell-checked, .ood-treegrid-cells1-checked, .ood-treegrid-cells2-checked{'+
                        '    color: [Font Color>highlight];'+
                        '}'
                    },
                    // Background
                    {
                        id:'Background Setting',
                        setting:[
                            {
                                id:'Base Background',
                                map:[{
                                    clsname:'.ood-uibase', 
                                    stylename:'background-color'
                                }]
                            },
                            {
                                id:'Bar Background',
                                status:['default','hover','active','focus'],
                                map:[{
                                    clsname:'.ood-uibar', 
                                    stylename:'background-color'
                                }]
                            }
                        ],
                        template: '' +
                        '.ood-uibase{'+
                        '    background-color:[Base Background];'+
                        '}'+
                        '.ood-uicontainer{'+
                        '    background-color:[Base Background];'+
                        '}'+
                        '.ood-uibar{'+
                        '    background-color:[Bar Background];'+
                        '}'+
                        '.ood-uibar-hover{'+
                        '    background-color:[Bar Background>hover];'+
                        '}'+
                        '.ood-uibar-active, .ood-uibar-checked, .ood-uibar-expand, .ood-uimenu-hover, .ood-uimenu-active{'+
                        '    background-color:[Bar Background>active];'+
                        '}'+
                        '.ood-uibar-focus, .ood-uibar-top-focus .ood-uibar-tdl, .ood-uibar-top-focus .ood-uibar-tdm, .ood-uibar-top-focus .ood-uibar-tdr{'+
                        '  background-color:[Bar Background>focus];'+
                        '}'
                    },
                    // Border
                    {
                        id:'Border Setting',
                        setting:[
                            {
                                id:'Border Style',
                                map:[{
                                    clsname:'.ood-uiborder-flat', 
                                    stylename:'border-left-style'
                                }]
                            },
                            {
                                id:'Border Width',
                                map:[{
                                    clsname:'.ood-uiborder-flat', 
                                    stylename:'border-left-width'
                                }]
                            },
                            {
                                id:'Border Color',
                                status:['inset','outset','light','dark'],
                                map:[{
                                    clsname:'.ood-uiborder-flat', 
                                    stylename:'border-color'
                                },{
                                    clsname:'.ood-uiborder-l', 
                                    stylename:'border-left-color'
                                },{
                                    clsname:'.ood-uiborder-light', 
                                    stylename:'border-color'
                                },{
                                    clsname:'.ood-uiborder-dark', 
                                    stylename:'border-color'
                                }]
                            }
                        ],
                        template: '' +
                        '.ood-uiborder-box{'+
                        '    border-width: [Border Width];'+
                        '}'+                    
                        '.ood-uiborder-l{' +
                        '    border-left-style: [Border Style];' +
                        '    border-left-width: [Border Width];' +
                        '    border-left-color: [Border Color>outset];' +
                        '}' +
                        '.ood-uiborder-t{' +
                        '    border-top-style: [Border Style];' +
                        '    border-top-width: [Border Width];' +
                        '    border-top-color: [Border Color>outset];' +
                        '}' +
                        '.ood-uiborder-r{' +
                        '    border-right-style: [Border Style];' +
                        '    border-right-width: [Border Width];' +
                        '    border-right-color: [Border Color];' +
                        '}' +
                        '.ood-uiborder-b, .ood-uitem-split{' +
                        '    border-bottom-style: [Border Style];' +
                        '    border-bottom-width: [Border Width];' +
                        '    border-bottom-color: [Border Color];' +
                        '}' +
                        '.ood-uiborder-flat{' +
                        '    border-style: [Border Style];' +
                        '    border-width: [Border Width];' +
                        '    border-color: [Border Color];' +
                        '}' +
                        '.ood-uiborder-outset{' +
                        '    border-style: [Border Style];' +
                        '    border-width: [Border Width];' +
                        '    border-color: [Border Color>outset] [Border Color] [Border Color] [Border Color>outset];' +
                        '}' +
                        '.ood-uiborder-inset, .ood-uiborder-hidden-active, .ood-uiborder-hidden-checked{' +
                        '    border-style: [Border Style];' +
                        '    border-width: [Border Width];' +
                        '    border-color: [Border Color] [Border Color>outset] [Border Color>outset] [Border Color];' +
                        '}' +
                        '.ood-uiborder-nob{'+
                        '    border-bottom-width:0;'+
                        '    border-bottom-style:none;'+
                        '}'+
                        '.ood-uiborder-hidden-active, .ood-uiborder-hidden-checked{' +
                        '    background-color:[Border Color>light];' +
                        '}' +
                        ' .ood-uiborder-dark, .ood-uiborder-flat-hover, .ood-tabs-item{' +
                        '    border-color:[Border Color];' +
                        ' }' +
                        ' .ood-uiborder-light, .ood-uiborder-hidden-hover{' +
                        '    border-color:[Border Color>light];' +
                        '}'
                    },
                    // Gradient
                    {
                        id:"Gradients Bar",
                        setting:[{
                            id:"Gradients Bar",
                            map:[{
                                clsname:'.ood-uigradient', 
                                stylename:'Gradients Bar'
                            }]
                        }],
                        template:''+
                            '.ood-uigradient{'+
                            '[normalStatus]'+
                            '}'+
                            '.ood-uigradient-hover, .ood-uigradient:hover{'+
                            '[hoverStatus]'+
                            '}'+
                            '.ood-uigradient-active, .ood-uigradient-checked,  .ood-uigradient-expand, '+
                            '.ood-uigradient:active, '+
                            '.ood-uigradient-active:hover, .ood-uigradient-checked:hover, .ood-uigradient-expand:hover{'+
                            '[activeStatus]'+
                            '}'
                    },
                    // List Item
                    {
                        id:"List Setting",
                        setting:[{
                            id:"Item Background",
                            status:['default','hover','active','checked'],
                            map:[{
                                clsname:'.ood-uitembg', 
                                stylename:'background-color'
                            }]
                        },{
                            id:"Item Border Color",
                            status:['default','hover','active','checked'],
                            map:[{
                                clsname:'.ood-uitembg', 
                                stylename:'border-left-color'
                            }]
                        }],
                        template:''+
                        '.ood-uitembg{'+
                        '    background-image:none; '+
                        '    background-color: [Item Background];'+
                        '    border-color: [Item Border Color];'+
                        '}'+
                        '.ood-uitembg-hover{'+
                        '    background-color: [Item Background>hover];'+
                        '    border-color: [Item Border Color>hover];'+
                        '}'+
                        '.ood-uitembg-active{'+
                        '    background-color: [Item Background>active];'+
                        '    border-color: [Item Border Color>active];'+
                        '}'+
                        '.ood-uitembg-checked{'+
                        '    background-color: [Item Background>checked];'+
                        '    border-color: [Item Border Color>checked];'+
                        '}'
                    },
                    //Grid
                    {
                        id:"Grid Setting",
                        setting:[{
                            id:"Row Background",
                            status:['default','alt','hover','active','checked','hot'],
                            map:[{
                                clsname:'.ood-treegrid-cells1', 
                                stylename:'background-color'
                            }]
                        },{
                            id:"Cell Hover Background",
                            map:[{
                                clsname:'.ood-uicell-hover', 
                                stylename:'background-color'
                            }]
                        }],
                        template:''+
                        '.ood-uicell,'+
                        '.ood-treegrid-cells1, .ood-treegrid-cells2{'+
                        '        background-color: [Row Background];'+
                        '}'+
                        '.ood-uicell-alt,'+
                        '.ood-treegrid-cells1-alt, .ood-treegrid-cells2-alt{'+
                        '        background-color: [Row Background>alt];'+
                        '}'+
                        '.ood-treegrid-cells1-hover, .ood-treegrid-cells2-hover{'+
                        '    background-color: [Row Background>hover];'+
                        '}'+
                        '.ood-treegrid-cells1-active, .ood-treegrid-cells2-active, '+
                        '.ood-treegrid-cell-active, .ood-treegrid-cell-active .ood-treegrid-cella{'+
                        '    background-color: [Row Background>active];'+
                        '}'+
                        '.ood-treegrid-cells1-hot, .ood-treegrid-cells2-hot{'+
                        '    background-color: [Row Background>hot];'+
                        '}'+
                        '.ood-uicell-checked, '+
                        '.ood-treegrid-cell-checked, '+
                        '.ood-treegrid-cells1-checked, '+
                        '.ood-treegrid-cells2-checked{'+
                        '    background-color: [Row Background>checked];'+
                        '}'+
                        '.ood-uicell-hover,'+
                        '.ood-treegrid-cell-hover{'+
                        '    background-color:[Cell Hover Background];'+
                        '}'
                    },
                    // Corner Radius
                    {
                        id:'Corner Setting',
                        setting:[
                            {
                                id:'Normal Radius',
                                map:[{
                                    clsname:'.ood-uiborder-radius', 
                                    stylename:'border-top-left-radius'
                                }]
                            },{
                                id:'Large Radius',
                                map:[{
                                    clsname:'.ood-uiborder-radius-big', 
                                    stylename:'border-top-left-radius'
                                }]
                            }
                        ],
                        template:''+
                        '.ood-uiborder-radius{ '+
                        '    border-radius: [Normal Radius];'+
                        '    -moz-border-radius: [Normal Radius];'+
                        '    -webkit-border-radius: [Normal Radius];'+
                        '    -o-border-radius: [Normal Radius];'+
                        '    -ms-border-radius: [Normal Radius];'+
                        '    -khtml-border-radius: [Normal Radius];'+
                        '}'+
                        '.ood-uiborder-radius-tl{'+
                        '    border-top-left-radius: [Normal Radius];'+
                        '    -moz-border-top-left-radius: [Normal Radius];'+
                        '    -webkit-border-top-left-radius: [Normal Radius];'+
                        '    -o-border-top-left-radius: [Normal Radius];'+
                        '    -ms-border-top-left-radius: [Normal Radius];'+
                        '     -khtml-border-top-left-radius: [Normal Radius];'+
                        '}'+
                        '.ood-uiborder-radius-tr{'+
                        '    border-top-right-radius: [Normal Radius];'+
                        '    -moz-border-top-right-radius: [Normal Radius];'+
                        '    -webkit-border-top-right-radius: [Normal Radius];'+
                        '    -o-border-top-right-radius: [Normal Radius];'+
                        '    -ms-border-top-right-radius: [Normal Radius];'+
                        '    -khtml-border-top-right-radius: [Normal Radius];'+
                        '}'+
                        '.ood-uiborder-radius-bl{'+
                        '    border-bottom-left-radius: [Normal Radius];'+
                        '    -moz-border-bottom-left-radius: [Normal Radius];'+
                        '    -webkit-border-bottom-left-radius: [Normal Radius];'+
                        '    -o-border-bottom-left-radius: [Normal Radius];'+
                        '    -ms-border-bottom-left-radius: [Normal Radius];'+
                        '    -khtml-border-bottom-left-radius: [Normal Radius];'+
                        '}'+
                        '.ood-uiborder-radius-br{'+
                        '    border-bottom-right-radius: [Normal Radius];'+
                        '    -moz-border-bottom-right-radius: [Normal Radius];'+
                        '    -webkit-border-bottom-right-radius: [Normal Radius];'+
                        '    -o-border-bottom-right-radius: [Normal Radius];'+
                        '    -ms-border-bottom-right-radius: [Normal Radius];'+
                        '    -khtml-border-bottom-right-radius: [Normal Radius];'+    
                        '}'+
                        '.ood-uiborder-radius-big{'+
                        '    border-radius: [Large Radius];'+
                        '    -moz-border-radius: [Large Radius];'+
                        '    -webkit-border-radius: [Large Radius];'+
                        '    -o-border-radius: [Large Radius];'+
                        '    -ms-border-radius: [Large Radius];'+
                        '    -khtml-border-radius: [Large Radius];'+
                        '}'+
                        '.ood-uiborder-radius-big-tl{'+
                        '    border-top-left-radius: [Large Radius];'+
                        '    -moz-border-top-left-radius: [Large Radius];'+
                        '    -webkit-border-top-left-radius: [Large Radius];'+
                        '    -o-border-top-left-radius: [Large Radius];'+
                        '    -ms-border-top-left-radius: [Large Radius];'+
                        '     -khtml-border-top-left-radius: [Large Radius];'+
                        '}'+
                        '.ood-uiborder-radius-big-tr{'+
                        '    border-top-right-radius: [Large Radius];'+
                        '    -moz-border-top-right-radius: [Large Radius];'+
                        '    -webkit-border-top-right-radius: [Large Radius];'+
                        '    -o-border-top-right-radius: [Large Radius];'+
                        '    -ms-border-top-right-radius: [Large Radius];'+
                        '    -khtml-border-top-right-radius: [Large Radius];'+
                        '}'+
                        '.ood-uiborder-radius-big-bl{'+
                        '    border-bottom-left-radius: [Large Radius];'+
                        '    -moz-border-bottom-left-radius: [Large Radius];'+
                        '    -webkit-border-bottom-left-radius: [Large Radius];'+
                        '    -o-border-bottom-left-radius: [Large Radius];'+
                        '    -ms-border-bottom-left-radius: [Large Radius];'+
                        '    -khtml-border-bottom-left-radius: [Large Radius];'+
                        '}'+
                        '.ood-uiborder-radius-big-br{'+
                        '    border-bottom-right-radius: [Large Radius];'+
                        '    -moz-border-bottom-right-radius: [Large Radius];'+
                        '    -webkit-border-bottom-right-radius: [Large Radius];'+
                        '    -o-border-bottom-right-radius: [Large Radius];'+
                        '    -ms-border-bottom-right-radius: [Large Radius];'+
                        '    -khtml-border-bottom-right-radius: [Large Radius];'+    
                        '}'
                    },
                    // Shadow
                    {
                        id:"Shadow Setting",
                        setting:[
                            {
                                id:"Dialog Shadow",
                                map:[{
                                    clsname:'.ood-ui-shadow',
                                    stylename:'box-shadow'
                                }]
                            },{
                                id:"Input Shadow",
                                map:[{
                                    clsname:'.ood-ui-shadow-input',
                                    stylename:'box-shadow'
                                }]
                            }],
                        template:''+
                        '.ood-ui-shadow{'+
                        '    -moz-box-shadow: [Dialog Shadow];'+
                        '    -webkit-box-shadow: [Dialog Shadow];'+
                        '    box-shadow: [Dialog Shadow];'+
                        '}'+
                        '.ood-ui-shadow-r{'+
                        '    -moz-box-shadow: [Dialog Shadow];'+
                        '    -webkit-box-shadow: [Dialog Shadow];'+
                        '    box-shadow: [Dialog Shadow];'+
                        '}'+
                        '.ood-ui-shadow-b{'+
                        '    -moz-box-shadow: [Dialog Shadow];'+
                        '    -webkit-box-shadow: [Dialog Shadow];'+
                        '    box-shadow: [Dialog Shadow];'+
                        '}'+
                        '.ood-ui-shadow-input{'+
                        '    -moz-box-shadow: [Input Shadow];'+
                        '    -webkit-box-shadow: [Input Shadow];'+
                        '    box-shadow: [Input Shadow];'+
                        '}'
                    },
                    // Focus Outline
                    {
                        id:'Focus Outline',
                        setting:[{
                            id:'Outline Style',
                            map:[{
                                clsname:'.ood-showfocus:focus',
                                stylename:'outline-style'
                            }]
                        },{
                            id:'Outline Width',
                            map:[{
                                clsname:'.ood-showfocus:focus',
                                stylename:'outline-width'
                            }]
                        }],
                        template:''+
                        '.ood-showfocus:focus {'+
                        '    outline-width: [Outline Width];'+
                        '    outline-style: [Outline Style];'+
                        '}'
                    },
                    // Modal Screen for Overlays
                    {
                        id:'Modal Screen for Overlays',
                        setting:[{
                            id:'Overlay Background',
                            map:[{
                                clsname:'.ood-cover',
                                stylename:'background-color'
                            }]
                        },{
                            id:'Overlay Opacity',
                            map:[{
                                clsname:'.ood-cover',
                                stylename:'opacity'
                            }]
                        }],
                        template:''+
                        '.ood-cover, .ood-cover-modal{'+
                        '    background-color: [Overlay Background];'+
                        '    opacity: [Overlay Opacity];'+
                        '}'
                    },{
                        id:'Custom CSS',
                        popBtn:true
                    }
                ];

            var arr=[],hash={};
            ood.arr.each(items,function(o,index){
                if(o.setting){
                    arr.push("\n\n/*"+o.id+"*/\n"+o.template);
                    hash[o.id]=index;
                }
            });

            ns._prev=prev;
            ns._conf_arr=arr;
            ns._conf_index_map=hash;
            ns._setting={};

            module._tabs.setItems(items).setUIValue('Font Setting');

            ns._cssscope="";
            ood.arr.each(ns._tabs.getItems(),function(item){
                //collect classes
                if(item.template){
                    ns._cssscope += item.template.replace(/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/g,'')
                        .replace(/\,([^\s])/g,", $1")
                        .replace(/\s+/g," ");
                }
                ns.__tabs_oninipanelview(ns._tabs, item);
            });
        },
        __tabs_oninipanelview:function (ctrl, item){
            var ns=this, 
                group, 
                subid;
            if(item.setting){
                ood.arr.each(item.setting, function(subItem){
                    subid = subItem.id;
                    if(subItem.status){
                        group = new ood.UI.Group({
                            position:'relative',
                            caption:subid,
                            width:"15em",
                            height:"auto",
                            overflow:'hidden'
                        });
                        group.setCustomStyle({KEY:'margin:0.25em 0;padding-left:1em;',PANEL:'line-height:2.2em'});
                        ood.arr.each(subItem.status,function(stt, index){
                            group.append(ns._createEditor(item, subItem, stt, index, stt));
                        });
                        ctrl.append(group, item.id);
                        group.getRoot().setInlineBlock();
                    }else{
                        ctrl.append(ns._createEditor(item, subItem, subid, 0), item.id);
                    }
                });
            }else{
                if(item.id=="Custom CSS"){
                    ns._cstomCSS = new ood.UI.Input({
                        position:'relative',
                        width:"15em",
                        height:"20em",
                        multiLines:true,
                        dirtyMark:false
                    });
                    ns._cstomCSS.onChange(function(){
                        ns._refreshTheme();
                    });
                    ctrl.append(ns._cstomCSS, item.id);
                }
            }
        },
        _createEditor: function(item, subItem, title, index, status){
            var ns=this,
                map=subItem.map[index||0],
                key = "\\["+subItem.id+((status&&status!='default'&&status!='inset')?('>'+status):'')+"\\]",
                editor = new ood.UI.ComboInput({
                    position:"relative",
                    dirtyMark:false,
                    labelSize:status?'7em':'8em',
                    width:status?'14em':'15em',
                    labelCaption:title
                }),
                value, 
                status2 = map?null:status;

            if(!map){
                map = subItem.map[0];
            }

            value = ood.CSS.$getCSSValue(ood.UI._getThemePrevId(ns._themeRoller.get(0)) +" " +map.clsname+(status2?('-'+status2):''), map.stylename);
            if(!value && map.pclsname){
                value = ood.CSS.$getCSSValue(ood.UI._getThemePrevId(ns._themeRoller.get(0)) +" " +map.pclsname+(status2?('-'+status2):''), map.stylename);
            }
            if(!value){
                value = ood.CSS.$getCSSValue(map.clsname+(status2?('-'+status2):''), map.stylename);
            }
            if(!value && map.pclsname){
                value = ood.CSS.$getCSSValue(map.pclsname+(status2?('-'+status2):''), map.stylename);
            }

            editor.setValue(value);
            // ini here
            ood.set(ns._setting,[item.id, key], editor.getValue());

            switch(map.stylename){
                case 'box-shadow':
                    editor.setType("popbox")
                        .beforeComboPop(function(prf,pos, e, src){
                        ood.ModuleFactory.getModule("RAD.CustomBoxShadow",function(){
                            this.init(prf,prf.boxing().getUIValue(),{},2);
                            this.setEvents({
                                onChange:function(str){
                                    prf.boxing().setUIValue(str);
                                }
                            });
                            this.render();
                            var r=this.mainPane.getRoot();
                            r.popToTop(prf.getRoot());
                            r.setBlurTrigger(r.$xid, function(){
                                r.setBlurTrigger(r.$xid);
                                r.hide();
                                prf.boxing().setPopWnd(null);    
                            });
                            prf.boxing().setPopWnd(this);
                        });                            
                    });
                    break;
                case 'Gradients Bar':
                    var clsname,
                        normalStatus ={},
                        hoverStatus ={},
                        activeStatus ={},
                        ls1,ls2, ls;
                    ls=[];
                    // normal status
                    clsname=".ood-uigradient";
                    ls1 = ood.CSS.$getCSSValue(ood.UI._getThemePrevId(ns._themeRoller.get(0)) +" " +clsname+(status2?('-'+status2):''));
                    ls2 = ood.CSS.$getCSSValue(clsname+(status2?('-'+status2):''));
                    Array.prototype.push.apply(ls, ls1);
                    Array.prototype.push.apply(ls, ls2);
                    if(ls && ls.length){
                        ls = ls.reverse();
                        ood.arr.each(ls, function(value){
                            ood.arr.each((value.style.cssText || value.style.rules || "").split(/\s*;\s*/),function(o, i){
                                o = o.split(/\s*:\s*/); 
                                if(o[0]){
                                    if(o[0]=='background'&& o[1]=='none')normalStatus['background-image'] = o[1];
                                    else normalStatus[o[0]] = o[1];
                                }
                            });                            
                        });
                    }

                    // hover status
                    ls=[];
                    clsname = ".ood-uigradient-hover";
                    ls1 = ood.CSS.$getCSSValue(ood.UI._getThemePrevId(ns._themeRoller.get(0)) +" " +clsname+(status2?('-'+status2):''));
                    ls2 = ood.CSS.$getCSSValue(clsname+(status2?('-'+status2):''));
                    Array.prototype.push.apply(ls, ls1);
                    Array.prototype.push.apply(ls, ls2);
                    if(ls && ls.length){
                        ls = ls.reverse();
                        ood.arr.each(ls, function(value){
                            ood.arr.each((value.style.cssText || value.style.rules || '').split(/\s*;\s*/),function(o, i){
                                o = o.split(/\s*:\s*/); 
                                if(o[0]){
                                    if(o[0]=='background'&& o[1]=='none')hoverStatus['background-image'] = o[1];
                                    else hoverStatus[o[0]] = o[1];
                                }
                            });                            
                        });
                    }

                    // active status
                    ls=[];
                    clsname = ".ood-uigradient-active";
                    ls1 = ood.CSS.$getCSSValue(ood.UI._getThemePrevId(ns._themeRoller.get(0)) +" " +clsname+(status2?('-'+status2):''));
                    ls2 = ood.CSS.$getCSSValue(clsname+(status2?('-'+status2):''));
                    Array.prototype.push.apply(ls, ls1);
                    Array.prototype.push.apply(ls, ls2);
                    if(ls && ls.length){
                        ls = ls.reverse();
                        ood.arr.each(ls, function(value){
                            ood.arr.each((value.style.cssText || value.style.rules || '').split(/\s*;\s*/),function(o, i){
                                o = o.split(/\s*:\s*/); 
                                if(o[0]){
                                    if(o[0]=='background'&& o[1]=='none')activeStatus['background-image'] = o[1];
                                    else activeStatus[o[0]] = o[1];
                                }
                            });                            
                        });
                    }
                    ood.filter(hoverStatus,function(o,i){
                        return normalStatus[i] !== o;
                    });
                    ood.filter(activeStatus,function(o,i){
                        return normalStatus[i] !== o;
                    });

                    ood.each([normalStatus,hoverStatus,activeStatus],function(k){
                        ood.each(k, function(o,i ){
                            k[i] = o = o.replace(/(?:rgb\((\d+), ?(\d+), ?(\d+)\)([^,]*))+/g,function(a,b,c,d,e){
                                return '#'+ood.UI.ColorPicker.rgb2hex(b, c, d) + e;
                            });
                            k[i] = o = o.replace(/(?:rgba\((\d+), ?(\d+), ?(\d+), ?([.\d]+)\)([^,]*))+/g,function(a,b,c,d,e,f){
                                return '#'+ood.UI.ColorPicker.rgb2hex(b, c, d) + ":" +e + f;
                            });
                            if(i=='background' || i=='background-image'){
                                if(/gradient\([^)]+\)/.test(o)){
                                    var moz = /-moz-/.test(o), g;
                                    if(/\blinear\b/.test(o)){
                                        g = {stops:[]},
                                            o = o.replace(/[^(]+\(([^)]+)\)/,'$1');
                                        g.type = 'liner';
                                        var a1=o.split(/,\s*/);
                                        ood.each(a1, function(s,i){
                                            switch(s){
                                                case 'left top':
                                                    if(a1[i+1] == 'right bottom'){
                                                        g.orient='LT';
                                                    }else if(a1[i+1]=='left bottom'){
                                                        g.orient='T';
                                                    }else if(a1[i+1]=='right top'){
                                                        g.orient='L';
                                                    }
                                                    break;
                                                case 'right top':
                                                    if(a1[i+1] == 'left top'){
                                                        g.orient='R';
                                                    }else if(a1[i+1]=='left bottom'){
                                                        g.orient='RT';
                                                    }
                                                    break;
                                                case 'left bottom':
                                                    if(a1[i+1] == 'right top'){
                                                        g.orient='LB';
                                                    }else if(a1[i+1]=='left top'){
                                                        g.orient='B';
                                                    }
                                                    break;
                                                case 'right bottom':
                                                    if(a1[i+1] == 'left top'){
                                                        g.orient='RB';
                                                    }
                                                    break;

                                                case '45deg':
                                                    g.orient='LB';
                                                    break;
                                                case '-45deg':
                                                    g.orient=moz?'LT':'RB';
                                                case '135deg': 
                                                    g.orient=moz?'RB':'LT';
                                                    break;
                                                case '-135deg': 
                                                    g.orient='RT';
                                                    break;

                                                case 'to bottom': 
                                                case 'top': 
                                                    g.orient='T';
                                                    break;
                                                case 'to right':
                                                case 'left':
                                                    g.orient="L";
                                                    break;
                                                case 'to left':
                                                case 'right':
                                                    g.orient="R";
                                                    break;
                                                case 'to top':
                                                case 'bottom':
                                                    g.orient="B";
                                                    break;
                                                default: 
                                                    i = s.split(' ');
                                                    if(/^#[0-f]{6}$/.test(i[0])){
                                                        g.stops.push({
                                                            pos:i[1],
                                                            clr:i[0].split(':')[0],
                                                            opacity:i[0].split(':')[1]||1
                                                        });
                                                    }

                                            }
                                        });
                                    }else if(/\bradial\b/.test(o) || /\bcircle\b/.test(o)){
                                        g = {stops:[]};
                                        o = o.replace(/[^(]+\(([^)]+)\)/,'$1');
                                        g.type = 'radial';
                                        var a2=o.split(/,\s*/);
                                        ood.each(a1, function(s,i){
                                            switch(s){
                                                case 'center center':g.orient = 'C';break;
                                                case 'left top':g.orient = 'LT';break;
                                                case 'right top':g.orient = 'RT';break;
                                                case 'left bottom':g.orient = 'LB';break;
                                                case 'right bottom':g.orient = 'RB';break;
                                                case 'top': case 'center top': g.orient = 'T';break;
                                                case 'left': case 'left center': g.orient = 'L'; break;
                                                case 'right': case 'right center': g.orient = 'R'; break;
                                                case 'bottom': case 'center bottom':g.orient = 'B'; break;
                                                default:
                                                    i = s.split(' ');
                                                    if(/^#[0-f]{6}/.test(i[0])){
                                                        g.stops.push({
                                                            pos:i[1],
                                                            clr:i[0].split(':')[0],
                                                            opacity:i[0].split(':')[1]||1
                                                        });
                                                    }                                                    
                                            }                                            
                                        });
                                    }
                                    if(g)k.$gradient=g;
                                }
                            }
                        });
                    });
                    // remove these 
                    delete normalStatus.background;
                    delete normalStatus['background-image'];
                    delete hoverStatus.background;
                    delete hoverStatus['background-image'];
                    delete activeStatus.background;
                    delete activeStatus['background-image'];

                    var prop={
                        normalStatus: normalStatus,
                        hoverStatus: hoverStatus,
                        activeStatus: activeStatus
                    };
                    //console.log(prop);

                    editor.setType('button')
                        .setCaption("Normal | Hover | Active")
                        .setLabelSize(0)
                        .onClick(function(prf,pos, e, src){
                        ood.ModuleFactory.getModule("RAD.CustomDecoration2",function(){
                            this.setProperties({
                                // getter & setter
                                targetProfile:{
                                    _noFocusStatus:true,
                                    _title:"Gradients Bar Setting",
                                    properties: prop,
                                    boxing:function(){
                                        return {
                                            setNormalStatus:function(hash){
                                                prop.normalStatus=hash;
                                                ns._updateGradient(prop);
                                            },
                                            setHoverStatus:function(hash){
                                                prop.hoverStatus=hash;
                                                ns._updateGradient(prop);
                                            },
                                            setActiveStatus:function(hash){
                                                prop.activeStatus=hash;
                                                ns._updateGradient(prop);
                                            },
                                            setFocusStatus:function(hash){}
                                        };
                                    }
                                },
                                src:prf.getRoot()
                            });
                            this.setEvents({
                                onFinished:function(){
                                    var nstyle=ood.clone(prf.properties.normalStatus,true),
                                        hstyle=ood.clone(prf.properties.hoverStatus,true),
                                        astyle=ood.clone(prf.properties.activeStatus,true);
                                }
                            });
                            this.show();
                        });                            
                    });

                    return editor;

                case 'font-family':
                    editor.setType("listbox")
                        .setItems([
                        {caption:"<span style='font-family:arial,helvetica,clean,sans-serif'>Arial</span>",id:"arial,helvetica,clean,sans-serif"},
                        {caption:"<span style='font-family:'arial black',avant garde'>Arial Black</span>",id:"'arial black',avant garde"},
                        {caption:"<span style='font-family:'comic sans ms',cursive'>Comic Sans MS</span>",id:"'comic sans ms',cursive"},
                        {caption:"<span style='font-family:'courier new',courier,monospace'>Courier New</span>",id:"'courier new',courier,monospace"},
                        {caption:"<span style='font-family:georgia,serif'>Georgia</span>",id:"georgia,serif"},
                        {caption:"<span style='font-family:'PT Sans', Tahoma'>PT Sans</span>",id:"'PT Sans', Tahoma"},
                        {caption:"<span style='font-family:impact,chicago'>impact</span>",id:"impact,chicago"},
                        {caption:"<span style='font-family:'lucida sans unicode','lucida grande',sans-serif'>Lucida Sans Unicode</span>",id:"'lucida sans unicode','lucida grande',sans-serif"},
                        {caption:"<span style='font-family:tahoma,geneva,sans-serif'>Tahoma</span>",id:"tahoma,geneva,sans-serif"},
                        {caption:"<span style='font-family:'times new roman',times,serif'>Times New Roman</span>",id:"'times new roman',times,serif"},
                        {caption:"<span style='font-family:'trebuchet ms',helvetica,sans-serif'>Trebuchet MS</span>",id:"'trebuchet ms',helvetica,sans-serif"},
                        {caption:"<span style='font-family:verdana,geneva,sans-serif'>Verdana</span>",id:"verdana,geneva,sans-serif"},
                        {caption:"<span style='font-family:Roboto, Arial'>Roboto</span>",id:"Roboto, Arial"}
                    ]);
                    break;
                case 'font-size':
                    editor.setType("listbox")
                        .setItems([".75rem","1rem","1.25rem","1.5rem","2rem","3rem","4rem"]);
                    break;
                case 'font-weight':
                    editor.setType("listbox")
                        .setItems(["normal","bolder","bold","lighter","600","700","800","900","1000"]);
                    break;
                case 'border-left-width':
                case 'outline-width':
                    editor.setType("listbox")
                        .setItems(["0", "1px", "2px", "3px", "4px", "5px"]);
                    break;
                case 'border-left-style':
                case 'outline-style':
                    editor.setType("listbox")
                        .setItems(["solid", "dashed", "dotted", "double", "groove", "hidden", "inset", "none", "outset", "ridge"]);
                    break;
                case 'opacity':
                    editor.setType("spin")
                        .setMin(0)
                        .setMax(1)
                        .setIncrement(0.1);
                    break;
                default:
                    if(ood.str.endWith(map.stylename,"color")){
                        editor.setType("color");
                    }else if(ood.str.endWith(map.stylename,"radius")){
                        editor.setType("listbox")
                            .setItems(["0","1px","2px","3px","4px","5px","6px","7px","8px","9px","10px"]);
                    }                    
            }

            editor.onChange(function (profile, oldValue, newValue){
                ns._onchange(item, subItem, profile, newValue, status);
            });
            return editor;
        },
        _updateGradient:function(gradient){
            var ns=this,
                fun=function(hash, clearFirst, g){
                    if(hash.$gradient && hash.$gradient.stops && hash.$gradient.stops.length){
                        delete hash['background-image'];
                        delete hash['background'];
                    }
                    var rst=clearFirst?['background-image:none']:[];
                    
                    var newO=function(){
                        var arr = this.arr=[];
                        Object.defineProperty(this, 'backgroundImage', {
                            set:function(v){
                               arr.push('background-image: ' + v);
                            }
                        });                        
                    };
                    
                    ood.each(hash,function(v,k){
                        switch(k){
                            case 'box-shadow':
                                rst.push("-moz-box-shadow: " + v);
                                rst.push("-webkit-box-shadow: " + v);
                                rst.push("box-shadow: " + v);
                                break;
                            case '$gradient':
                                if(v && v.stops&& v.stops.length){
                                    rst.push("filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='"+v.stops[0].clr+"', endColorstr='"+(v.stops[1]||v.stops[0]).clr+"')");
                                    rst.push("-ms-filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='"+v.stops[0].clr+"', endColorstr='"+(v.stops[1]||v.stops[0]).clr+"')");
                                    
                                    var fakeStyle=new newO(),
                                        fakeNode = {
                                            nodeType:1,
                                            style:fakeStyle
                                        };
                                    ood.Dom.$setGradients(fakeNode, v, {
                                        gek:1,
                                        ver:100,
                                        cssTag1:'-moz-'
                                    });
                                    ood.Dom.$setGradients(fakeNode, v, {
                                        opr:1,
                                        ver:100,
                                        cssTag1:'-o-'
                                    });
                                    ood.Dom.$setGradients(fakeNode, v, {
                                        isWebKit:1,
                                        isChrome:1,
                                        isSafari:1,
                                        ver:100,
                                        cssTag1:'-webkit-'
                                    });
                                    ood.arr.removeDuplicate(fakeStyle.arr);
                                    if(fakeStyle.arr.length){
                                        ood.arr.insertAny(rst,fakeStyle.arr);
                                        if(g)g.length = fakeStyle.arr.length;
                                    }
                                } 
                                break;
                          default :
                              rst.push(k+": "+v);
                        }
                    });
                    return '    ' + rst.join(';\n') +";";  
                },
                t, normalGradient={};

            if((t=gradient.normalStatus)){
                ood.set(ns._setting,['Gradients Bar', '\\[normalStatus\\]'], fun(t,true,normalGradient));
            }
            if((t=gradient.hoverStatus)){
                ood.set(ns._setting,['Gradients Bar', '\\[hoverStatus\\]'], fun(t, !normalGradient.length));
            }
            if((t=gradient.activeStatus)){
                ood.set(ns._setting,['Gradients Bar', '\\[activeStatus\\]'], fun(t, !normalGradient.length));
            }
            ns._refreshTheme();
        },
        _onchange:function (item, subItem, profile, newValue, status){
            var ns=this, 
                key = "\\["+subItem.id+((status&&status!='default'&&status!='inset')?('>'+status):'')+"\\]";

            ood.set(ns._setting,[item.id, key], newValue);

            ns._refreshTheme();

            if("Font Size"==subItem.id){
                ood.CSS.adjustFont();
            }
        },
        _refreshTheme:function(){
            var ns=this;
            ns._dirty1=true;
            ns.fireChangeEvent(ns._buildTheme());
        },
        _buildTheme:function(){
            var ns=this, 
                // must be the original one
                arr=ood.copy(ns._conf_arr),
                index,themeStr;

            ood.each(ns._setting,function(subMap,itemId){
                index = ns._conf_index_map[itemId];
                ood.each(subMap,function(value,key){
                    if(key)arr[index] = arr[index].replace(new RegExp(key,'g'), value);                    
                });
            });
            arr = arr.join('\n').replace(/([},])\s*(\.)/g,function(a,b,c){
                return b + "\n" + c;
            }).replace(/([{;])\s*(.)/g,function(a,b,c){
                return b + '\n' + c;
            }).split('\n');
            ood.filter(arr,function(s){
                return !(/\:\s*\[/.test(s) || /\]\s*\;/.test(s)|| /\:\s*;/.test(s));
            });
            themeStr=arr.join('');
            themeStr=themeStr.replace(/[^}]+\{\s*\}/g,'');
            return (ns._prev + themeStr + (ns._cstomCSS ? "\n\n"+ns._cstomCSS.getValue() : ""))
                .replace(/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/g,'\n\n$1\n')
                .replace(/([},])\s*(\.)/g,function(a,b,c){
                    return b + "\n" + "" + c;
                })
                .replace(/([{;])\s*(.)/g,function(a,b,c){
                    return b + '\n' + (c=='}'?'':'    ') + c;
                })
                .replace(/\{\s+;\s*\}/g,'{}');
        },
        _theme_afteruivalueset:function (profile,oldValue,newValue){            
            var ns=this;
            ns._setting={};
            ns.fireChangeEvent("");

            delete ns._cstomCSS;

            ood.busy("id","Loading theme ...",true);
            ood.getFileAsync(ood.getPath('ood.appearance.' + newValue,'/theme.css'), function(rsp){
                
                ns._dirty1=false;

                rsp = rsp.replace(/\.setting-uikey\{[^}]+\}/,'');
                ns.fireChangeEvent(ns._prev + rsp);

                ns._tabs.resetPanelView(true,true,true);

                ood.arr.each(ns._tabs.getItems(),function(item){
                    ns.__tabs_oninipanelview(ns._tabs, item);
                });

                // [[ get non-standard css
                rsp = ood.replace(rsp, [[/(\/\*[^*]*\*+([^\/][^*]*\*+)*\/)/,''],
                                              [/\{[^}]*\}/,'$0'],
                                              [/\s+/," "],
                                              [/\,([^\s])/,", $1"]
                                             ]);
                var v=[],k=[],hash={},
                    aa1=ns._cssscope.split(/\s*\{[^}]*\}\s*/);

                rsp.replace(/\s*\{[^}]*\}\s*/g,function(a){
                    v.push(a);
                });
                k=rsp.split(/\s*\{[^}]*\}\s*/);

                ood.arr.each(aa1,function(o,i){aa1[i]=ood.str.trim(o);});
                ood.arr.each(k,function(o,i){k[i]=ood.str.trim(o);});

                ood.arr.each(v,function(o,i){
                    hash[k[i]]=o;
                });

                hash = ood.filter(hash,function(v,k){
                    return ood.arr.indexOf(aa1, k)==-1; 
                });
                var aa2=[];
                ood.each(hash,function(o,i){
                    aa2.push(i+o+"\n");
                });
                ns._cstomCSS.setValue(aa2.join("\n"));
                // ]]


                ood.CSS.adjustFont();

                ood.free("id");
            });

            // ood.setTheme(newValue,true,function(){});
        },
        __tabs_beforepagepop:function (profile, item, options, e, src){
            var ns=this,
                dlg = ood.prompt("Custom CSS", "CSS Code", ns._cstomCSS.getUIValue(), function(value){
                    ood.Coder.formatText(ns._cstomCSS.setValue(value,true),'css');
                },0, 0,0,0,0,0,0,true);
            dlg.setStatus('max');
            return false;
        }
    }
});
