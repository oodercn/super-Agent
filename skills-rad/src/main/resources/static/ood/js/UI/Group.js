ood.Class("ood.UI.Group", "ood.UI.Panel",{
    Instance: {
        iniProp: {caption: '组容器', toggleBtn: false, width: '18em'}
    },

    Static:{
        Templates:{
            tagName : 'div',
            style:'{_style}',
            className:'{_className}',
            BORDER:{
                tagName : 'div', // 改为div标签，更灵活
                className: '{_fieldCls} ood-group-container',
                TBAR:{
                    tagName : 'div',
                    style:'{_align} ',
                    className: 'ood-group-header',
                    BARCMDL:{
                        LTAGCMDS:{
                            tagName:'span',
                            className:'ood-ltag-cmds',
                            style:'{_ltagDisplay}',
                            text:"{ltagCmds}"
                        },
                        TOGGLE:{
                            $order:1,
                            className: 'oodfont ood-group-toggle',
                            $fonticon:'{_fi_toggleCls2}',
                            style:'{toggleDisplay}',
                            $order:0
                        },
                        ICON:{
                            $order:2,
                            className:'oodcon {imageClass} {picClass} ood-group-icon',
                            style:'{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                            text:'{iconFontCode}'
                        },
                        CAPTION : {
                            tabindex: '{tabindex}',
                            className:"ood-title-node ood-group-caption",
                            text:   '{caption}',
                            $order:3
                        }
                    },
                    BARCMDR:{
                        $order:4,
                        tagName: 'div',
                        className:'ood-uibar-cmdr ood-uibase',
                        RTAGCMDS:{
                            $order:0,
                            tagName:'span',
                            className:'ood-rtag-cmds',
                            style:'{_rtagDisplay}',
                            text:"{rtagCmds}"
                        } ,
                        INFO:{className:'oodcon', $fonticon:'ood-uicmd-info', style:'{infoDisplay}', $order:1},
                        OPT:{className:'oodcon', $fonticon:'ood-uicmd-opt', style:'{optDisplay}', $order:2},
                        POP:{className:'oodcon', $fonticon:'ood-uicmd-pop', style:'{popDisplay}', $order:3},
                        REFRESH:{className:'oodcon', $fonticon:'ood-uicmd-refresh', style:'{refreshDisplay}', $order:4},
                        CLOSE:{className:'oodcon', $fonticon:'ood-uicmd-close', style:'{closeDisplay}', $order:5}
                    }
                },
                PANEL:{
                    $order:1,
                    tagName:'div',
                    style:'{panelDisplay};{_panelstyle};{_overflow};',
                    className:'ood-uicontainer ood-group-panel',
                    text:'{html}'+ood.UI.$childTag
                }
            },
            $submap:ood.UI.$getTagCmdsTpl()
        },
        Appearances:{
            KEY:{},
            BORDER:{
                position:'relative',
                overflow:'visible',
                backgroundColor: 'var(--ood-bg-card)',
                borderRadius: 'var(--ood-radius-md)',
                border: '1px solid var(--ood-border)',
                boxShadow: 'var(--ood-shadow-sm)',
                transition: 'all var(--ood-transition-normal)'
            },
            'LTAGCMDS, RTAGCMDS':{
                padding:0,
                margin:0,
                'vertical-align': 'middle'
            },
            'TBAR':{
                width: 'auto',
                padding: 'var(--ood-spacing-sm) var(--ood-spacing-md)',
                margin: 0,
                border: 0,
                'font-size': 'inherit',
                'border-bottom': '1px solid var(--ood-border)',
                cursor: 'pointer',
                transition: 'background-color var(--ood-transition-fast)',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'background-color': 'var(--ood-bg-header)'
            },
            'TBAR:hover':{
                backgroundColor: 'var(--ood-bg-hover)'
            },
            'BORDER-checked TBAR':{
                'margin-left':'0'
            },
            'BORDER-checked BARCMDL':{
                'padding-left':'0'
            },
            'BARCMDL':{
                cursor:'default',
                'padding-right':'var(--ood-spacing-xs)',
                display:ood.$inlineBlock,
                'align-items': 'center',
                gap: 'var(--ood-spacing-sm)'
            },
            'PANEL':{
                position:'relative',
                overflow:'auto',
                'line-height':'normal',
                padding: 'var(--ood-spacing-md)',
                backgroundColor: 'var(--ood-bg)',
                transition: 'all var(--ood-transition-normal)',
            },
            'CAPTION':{
                display:'inline',
                'vertical-align':'middle',
                color: 'var(--ood-text-heading)',
                'font-weight': '500',
                'font-size': 'var(--ood-font-size-lg)'
            },
            'TOGGLE':{
                padding:'0 var(--ood-spacing-xs) 0 0',
                color: 'var(--ood-text-muted)',
                transition: 'transform var(--ood-transition-normal), color var(--ood-transition-fast)'
            },
            'TOGGLE-checked':{
                transform: 'rotate(180deg)',
                color: 'var(--ood-primary)'
            },
            'TOGGLE:hover':{
                color: 'var(--ood-text-heading)'
            },
            'ICON':{
                margin: '0 var(--ood-spacing-sm) 0 0',
                color: 'var(--ood-text-muted)'
            },
            'BARCMDR':{
                display: 'flex',
                'align-items': 'center',
                gap: 'var(--ood-spacing-xs)'
            },
            'BARCMDR .oodcon':{
                padding: 'var(--ood-spacing-xs)',
                borderRadius: 'var(--ood-radius-sm)',
                transition: 'all var(--ood-transition-fast)'
            },
            'BARCMDR .oodcon:hover':{
                backgroundColor: 'var(--ood-bg-hover)',
                color: 'var(--ood-text-heading)'
            }
        },
        DataModel:{
            expression:{ini:'',action:function () {}},
            euClassName:'',
            dock:'none',
            noFrame:null,
            borderType:null,
            toggleBtn:{ini:true},

            transitionDuration: {
                ini: 'normal', // fast, normal, slow
                action: function(value) {
                    var transitionMap = {'fast': 'var(--transition-fast)', 'normal': 'var(--transition-normal)', 'slow': 'var(--transition-slow)'},
                        duration = transitionMap[value] || transitionMap.normal,
                        profile = this.get(0),
                        border = profile.getSubNode('BORDER'),
                        tbar = profile.getSubNode('TBAR');
                     
                    if (border) border.css('transition', 'all ' + duration);
                    if (tbar) tbar.css('transition', 'background-color ' + duration);
                }
            },
            // 响应式断点配置
            responsiveBreakpoint: {
                ini: 'md', // sm, md, lg, xl
                action: function(value) {
                    var profile = this.get(0),
                        border = profile.getSubNode('BORDER');
                    if (border) {
                        border.removeClass('responsive-sm responsive-md responsive-lg responsive-xl');
                        border.addClass('responsive-' + value);
                    }
                }
            },
            // 可访问性支持
            ariaLabel: {
                ini: '',
                action: function(value) {
                    var profile = this.get(0),
                        border = profile.getSubNode('BORDER');
                    if (border && value) {
                        border.attr('aria-label', value);
                    }
                }
            },
            // 键盘导航支持
            tabIndex: {
                ini: 0,
                action: function(value) {
                    var profile = this.get(0),
                        tbar = profile.getSubNode('TBAR'),
                        caption = profile.getSubNode('CAPTION');
                    if (tbar) tbar.attr('tabindex', value);
                    if (caption) caption.attr('tabindex', value);
                }
            }
        },
        _prepareData:function(profile){
            var data=arguments.callee.upper.call(this, profile);
            if(!profile.properties.toggle)data.height="auto";
            data._fieldCls = data.toggleBtn && !data.toggle ? ' ood-group-collapsed' : '';
            
            // 设置过渡动画持续时间
            if (data.transitionDuration) {
                var transitionMap = {'fast': 'var(--transition-fast)', 'normal': 'var(--transition-normal)', 'slow': 'var(--transition-slow)'},
                    duration = transitionMap[data.transitionDuration] || transitionMap.normal;
                data._transition = 'all ' + duration;
            }
            
            return data;
        },
        _toggle:function(profile, value, ignoreEvent){
            var p=profile.properties, ins=profile.boxing();

            //event
            if(value){
                ins.iniPanelView();
            }
            if(ignoreEvent || profile._toggle !== !!value){
                //set toggle mark
                profile._toggle = p.toggle = !!value;
                if(!ignoreEvent){
                    if(value){
                        if(ins.beforeExpand && false===ins.beforeExpand(profile))return;
                    }else{
                        if(ins.beforeFold && false===ins.beforeFold(profile))return;
                    }
                }
                //chang toggle button
                if(p.toggleBtn){
                    var toggleNode = profile.getSubNode('TOGGLE');
                    if (toggleNode) toggleNode.tagClass('-checked', !!value);
                }
                
                var border=profile.getSubNode('BORDER'),
                    panel=profile.getSubNode('PANEL');
                
                if(border){
                    border.toggleClass('ood-group-collapsed', !value);
                }
                
                // 应用平滑过渡效果
                if(panel){
                    if(value){
                        panel.css({'display': 'block', 'max-height': '0', 'opacity': '0'});
                        // 强制重排以触发过渡
                       //anel[0].offsetHeight;
                        panel.css({'max-height': '2000px', 'opacity': '1'});
                    }else{
                        panel.css({'max-height': '0', 'opacity': '0'});
                        // 使用延时确保动画完成后再隐藏
                        setTimeout(function(){
                            if(!profile._toggle){
                                panel.css('display', 'none');
                            }
                        }, 300);
                    }
                }
                
                // use onresize function
                profile.adjustSize(true);

                if(!ignoreEvent){
                    if(value){
                        if(ins.afterExpand) ins.afterExpand(profile);
                    }else{
                        if(ins.afterFold) ins.afterFold(profile);
                    }
                }
                // try redock
                if(p.dock && p.dock!='none'){
                    ins.adjustDock(true);
                }
            }
        },
        _onresize:function(profile,width,height){
            var prop=profile.properties,
                
                // compare with px
                us = ood.$us(profile),
                adjustunit = function(v,emRate){return profile.$forceu(v, us>0?'em':'px', emRate)},

                border = profile.getSubNode('BORDER'),
                panel =profile.getSubNode('PANEL'), 
                root = profile.getRoot(),
                cb = border.contentBox(),
                h0=border._borderH(),
                // caculate by px
                ww=width?profile.$px(width):null, 
                hh=height?profile.$px(height):null;
            
            if(height){
                if(height=='auto'){
                    if(panel) panel.height('auto');
                    if(border) border.height('auto');
                    if(root) root.height('auto');
                }else{
                    if(profile._toggle && panel && border && root){
                        panel.height(adjustunit(hh - profile.getSubNode('TBAR').offsetHeight(true) - h0/2, panel));
                        border.height(adjustunit(hh - (cb?h0:0), border));
                        root.height(adjustunit(hh));
                    }else if(!profile._toggle && border && root){
                        // here, panel's display is 'none'
                        border.height('auto');
                        root.height('auto');
                    }
                }
            }

            if(width && width!='auto' && ww>=2 && profile._toggle && panel){
                panel.width(ww = adjustunit(ww-2));
                ood.UI._adjustConW(profile, panel, ww);
            }
        },

        // 现代化功能：设置主题
        setTheme: function(profile, theme) {
            var border = profile.getSubNode('BORDER');
            if (border) {
                // 移除旧的主题类名
                border.removeClass('group-themed-light group-themed-dark group-themed-highcontrast');
                // 添加新的主题类名
                if (theme === 'dark') {
                    border.addClass('group-themed-dark');
                } else if (theme === 'highcontrast') {
                    border.addClass('group-themed-highcontrast');
                } else {
                    border.addClass('group-themed-light');
                }
                // 添加基础类名
                border.addClass('group-themed');
            }
        },

        // 切换主题
        toggleTheme: function(profile) {
            var border = profile.getSubNode('BORDER');
            if (border) {
                var currentTheme = 'light';
                if (border.hasClass('group-themed-dark')) {
                    currentTheme = 'dark';
                } else if (border.hasClass('group-themed-highcontrast')) {
                    currentTheme = 'highcontrast';
                }
                
                var nextTheme = currentTheme === 'light' ? 'dark' : 
                               currentTheme === 'dark' ? 'highcontrast' : 'light';
                this.setTheme(profile, nextTheme);
                return nextTheme;
            }
        },

        // 键盘导航支持
        enableKeyboardNavigation: function(profile) {
            var tbar = profile.getSubNode('TBAR'),
                caption = profile.getSubNode('CAPTION');
            
            if (tbar) {
                tbar.attr('tabindex', '0');
                tbar.on('keydown', function(e) {
                    if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault();
                        profile._toggle(profile, !profile.properties.toggle);
                    }
                });
            }
            
            if (caption) {
                caption.attr('tabindex', '0');
            }
        },

        // 屏幕阅读器支持
        setScreenReaderLabel: function(profile, label) {
            var border = profile.getSubNode('BORDER');
            if (border) {
                border.attr('aria-label', label);
                
                // 添加隐藏的屏幕阅读器文本
                var srText = border.find('.sr-only');
                if (srText.length === 0) {
                    border.append('<span class="sr-only">' + label + '</span>');
                } else {
                    srText.text(label);
                }
            }
        },

        // 响应式布局更新
        updateResponsiveLayout: function(profile, breakpoint) {
            var border = profile.getSubNode('BORDER');
            if (border) {
                // 移除旧的响应式类
                border.removeClass('responsive-sm responsive-md responsive-lg responsive-xl');
                // 添加新的响应式类
                border.addClass('responsive-' + breakpoint);
                // 更新DataModel
                profile.properties.responsiveBreakpoint = breakpoint;
            }
        }
    }
});