
ood.Class("ood.UI.ProgressBar", ["ood.UI.Widget","ood.absValue"] ,{
    Instance:{
        _setCtrlValue:function(value){
            return this.each(function(profile){
                var type=profile.properties.type,
                    inn=profile.getSubNode('FILL');
               if(type=="horizontal"){
                    inn.width(value+"%");
                }else{
                    inn.top((100-value)+"%").height(value+"%");
                }
                profile.getSubNode('CAP').text(profile.properties.captionTpl.replace(/\{value\}|\*/g,value));
            });
        },
        
        // Set theme
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // Clear all theme classes
                root.removeClass('progressbar-dark progressbar-light progressbar-hc');
                
                // Apply theme class
                root.addClass('progressbar-' + theme);
                
                // Save theme settings
                localStorage.setItem('progressbar-theme', theme);
            });
        },
        
        // Get current theme
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('progressbar-theme') || 'light';
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
                    root.addClass('progressbar-mobile');
                } else {
                    root.removeClass('progressbar-mobile');
                }

                // Special handling for extra small screens
                if (width < 480) {
                    root.addClass('progressbar-tiny');
                } else {
                    root.removeClass('progressbar-tiny');
                }
            });
        },
        
        // Enhance accessibility support
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    fill = profile.getSubNode('FILL'),
                    cap = profile.getSubNode('CAP'),
                    properties = profile.properties,
                    currentValue = properties.$UIvalue || properties.value || 0;

                // Add ARIA attributes to progress bar
                root.attr({
                    'role': 'progressbar',
                    'aria-valuemin': '0',
                    'aria-valuemax': '100',
                    'aria-valuenow': currentValue,
                    'aria-label': ood.getRes('UI.progressbar.label') || 'Progress indicator',
                    'aria-describedby': properties.tips ? profile.serialId + '_tips' : null,
                    'aria-live': 'polite',
                    'aria-atomic': 'true'
                });
                
                // Add ARIA attributes to fill area
                if (fill && !fill.isEmpty()) {
                    fill.attr({
                        'aria-hidden': 'true' // Hide decorative element
                    });
                }

                // Add ARIA attributes to text
                if (cap && !cap.isEmpty()) {
                    cap.attr({
                        'aria-hidden': 'true' // Text is already reflected in aria-valuenow
                    });
                }
            });
        }
    },
    Initialize:function(){
        var self=this,
            t = self.getTemplate();
        //modify
        ood.merge(t.FRAME.BORDER,{
            className:"ood-uiborder-flat ood-uiborder-radius ood-uibase",
            FILL:{
                tagName:'div',
                style:'{fillBG}',
                className:'ood-uibar',
                text:'{html}'+ood.UI.$childTag
            },
            INN:{
                $order:2,
                tagName:'div',
                CAP:{
                    tagName:'div'
                }
            }
        },'all');
        //set back
        self.setTemplate(t);

        //get default Appearance
        t = self.getAppearance();
        //modify
        ood.merge(t,{
            BORDER:{
                overflow:'hidden',
                'background-color': 'var(--ood-progressbar-track-bg)',
                'border': 'var(--ood-progressbar-track-border)',
                'height': 'var(--ood-progressbar-height)',
                'border-radius': 'var(--ood-progressbar-border-radius)'
            },
            INN:{
                display:'table',
                position:'absolute',
                left:0,
                top:0,
                width:'100%',
                height:'100%'
            },
            CAP:{
                'text-align':'center',
                'color': 'var(--progressbar-text)'
            },
            FILL:{
                border:'none',
                position:'relative',
                width:0,
                height:0,
                left:0,
                top:0,
                'background': 'var(--ood-progressbar-fill-bg)',
                'transition': 'width 0.3s ease, height 0.3s ease'
            }
        });
        //set back
        self.setAppearance(t);
    },
    Static:{
        DataModel:{
            // Modern properties
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
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
            
            value:0,
            width:{
                $spaceunit:1,
                ini:'25em'
            },
            height:{
                $spaceunit:1,
                ini:'1.5em'
            },
            captionTpl:{
                ini:'* %',
                action:function(){
                    this.boxing()._setCtrlValue(this.properties.$UIvalue);
                }
            },
            type:{
                listbox:['vertical', 'horizontal'],
                ini:'horizontal',
                action:function(v){
                    var w=this.properties.width,h=this.properties.height;
                    this.properties.height=w;this.properties.width=h;
                    this.boxing().refresh();
                }
            },
            fillBG:{
                ini:'',
                format:'color',
                action:function(v){
                    this.getSubNode('FILL').css('background',v);
                }
            },
            $hborder:1,
            $vborder:1
        },
        LayoutTrigger:function(){
            var v=this.properties,nd=this.getSubNode("BORDER");
            v.$hborder=v.$vborder=nd._borderW('left');
            
            // Modern feature initialization
            var self = this;
            ood.asyRun(function(){
                self.boxing().ProgressBarTrigger();
            });
        },
        
        ProgressBarTrigger: function() {
            var prop = this.properties,
                boxing = this.boxing();
            
            // Initialize modern features
            // Initialize theme
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // Restore theme from local storage
                var savedTheme = localStorage.getItem('progressbar-theme');
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
            var data=arguments.callee.upper.call(this, profile);
            data.fillBG = data.fillBG?'background:'+data.fillBG:'';
            return data;
        },
        _ensureValue:function(profile,value){
            return Math.max(0, Math.min(100, ((/^\s*\=/.test(value||"")) ? ood.ExcelFormula.calculate(value||"") : parseInt(value,10)) || 0));
        },
        _onresize:function(profile,width,height){
            var size = arguments.callee.upper.apply(this,arguments),v,
                p=profile.properties,
                us = ood.$us(profile),
                adjustunit = function(v,emRate){return profile.$forceu(v, us>0?'em':'px', emRate)},
                root = profile.getRoot(),
                inn = profile.getSubNode('INN'),
                cap = profile.getSubNode('CAP'),
                fill = profile.getSubNode('FILL'),
                
                fzrate=profile.getEmSize()/root._getEmSize(),
                innfz=inn._getEmSize(fzrate),
                capfz=cap._getEmSize(fzrate),
                fillfz=fill._getEmSize(fzrate);
                
            // caculate by px
            if(size.width && size.width!='auto')size.width=profile.$px(size.width);
            if(size.height && size.height!='auto')size.height=profile.$px(size.height);

            if(p.type=="horizontal"){
                if(size.height){
                    v=adjustunit(size.height, innfz);
                    inn.css({'line-height':v});
                    
                    v=adjustunit(size.height, fillfz);
                    fill.css({height:v,'line-height':v});
                    
                    v=adjustunit(size.height, capfz);
                    cap.css({height:v,'line-height':v});
                }
            }else{
                if(size.width){
                    //inn.css({width:adjustunit(size.width, innfz)});                   
                    fill.css({width:adjustunit(size.width, fillfz)});
                    cap.css({width:adjustunit(size.width, capfz)});
                }
                if(size.height){
                    inn.css({'line-height':adjustunit(size.height, innfz)});
                    fill.css({'line-height':adjustunit(size.height, fillfz)});
                    cap.css({'line-height':adjustunit(size.height, capfz)});
                }
            }
        }
    }
});