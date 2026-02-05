
ood.Class("ood.UI.CheckBox", ["ood.UI","ood.absValue"],{

    Initialize:function(){

        // compitable
        ood.UI.SCheckBox = ood.UI.CheckBox;
        var key="ood.UI.SCheckBox";
        ood.absBox.$type[key.replace("ood.UI.","")]=ood.absBox.$type[key]=key;
    },
    Instance:{
        iniProp: {
            caption: 'ri ri-checkbox-line',
            value: true
        },
        fireClickEvent:function(){
            this.getRoot().onClick();
            return this;
        },
        activate:function(){
            this.getSubNode('FOCUS').focus(true);
            return this;
        },
        _setCtrlValue:function(value){
            return this.each(function(profile){
               profile.getSubNode('MARK').tagClass('-checked', !!value);
            });
        },
        //update UI face
        _setDirtyMark:function(){
            return arguments.callee.upper.apply(this,['CAPTION']);
        },
        notifyExcel:ood.UI.Input.prototype.notifyExcel,
        // get control's fake cexcel cell value
        getExcelCellValue:ood.UI.Input.prototype.getExcelCellValue,
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();
                root.attr('data-theme', theme);
                
                // 保存主题设置
                localStorage.setItem('checkbox-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('checkbox-theme') || 'light';
        },



        CheckBoxTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties,
                boxing = this;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('checkbox-theme');
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
                    width = ood(document.body).cssSize().width;

                // 对于小屏幕，调整样式
                if (width < 768) {
                    root.addClass('checkbox-mobile');
                } else {
                    root.removeClass('checkbox-mobile');
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('checkbox-tiny');
                } else {
                    root.removeClass('checkbox-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var focus = profile.getSubNode('FOCUS'),
                    mark = profile.getSubNode('MARK'),
                    caption = profile.getSubNode('CAPTION'),
                    properties = profile.properties;

                // 为焦点元素添加ARIA属性
                focus.attr({
                    'role': 'checkbox',
                    'aria-checked': properties.value ? 'true' : 'false',
                    'aria-label': properties.caption || ood.getRes('UI.checkbox.default'),
                    'aria-describedby': properties.tips ? profile.serialId + '_tips' : null,
                    'aria-disabled': properties.disabled ? 'true' : 'false',
                    'aria-live': 'polite'
                });
                
                // 为标记添加ARIA属性
                mark.attr({
                    'aria-hidden': 'true' // 隐藏装饰性图标
                });

                // 为说明文字添加适当的标签
                if (caption && !caption.isEmpty()) {
                    caption.attr({
                        'id': profile.serialId + '_label'
                    });
                    focus.attr('aria-labelledby', profile.serialId + '_label');
                }

                // 键盘导航支持
                focus.on('keydown', function(e) {
                    var key = e.keyCode;
                    // 空格键切换选中状态
                    if (key === 32) {
                        e.preventDefault();
                        profile.getRoot().onClick();
                    }
                });
            });
        }
    },
    Static:{
        Templates:{
            className:'{_className} ',
            style:'{_style} {_hAlign}',
            VALIGN:{
                $order:0,
                style:'{_vAlign}'
            },
            FOCUS:{
                tabindex: '{tabindex}',
                MARK:{
                    $order:0,
                    className:'{_iconPosCls} oodfont',
                    $fonticon:'ood-uicmd-check'
                },
                ICON:{
                    $order:1,
                    className:'oodcon {imageClass}  {picClass}',
                    style:'{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                    text:'{iconFontCode}' 
                },
                CAPTION:{
                    $order:2,
                    text:'{caption}'
                }
            }
        },
        Appearances:{
            KEY:{
                className: 'ood-checkbox'
            },
            MARK:{
                className: 'ood-checkbox-mark'
            },
            VALIGN: {
                className: 'ood-checkbox-valign'
            },
            FOCUS:{
                className: 'ood-checkbox-focus'
            },
            CAPTION:{
                className: 'ood-checkbox-caption'
            },
            
            // 响应式样式
            'checkbox-mobile MARK': {
                className: 'checkbox-mobile .ood-checkbox-mark'
            },
            'checkbox-mobile CAPTION': {
                className: 'checkbox-mobile .ood-checkbox-caption'
            },
            'checkbox-tiny ICON': {
                className: 'checkbox-tiny .ood-checkbox-icon'
            },
            'checkbox-tiny CAPTION': {
                className: 'checkbox-tiny .ood-checkbox-caption'
            }
        },
        Behaviors:{
            HoverEffected:{KEY:'MARK',ICON:'ICON'},
            ClickEffected:{KEY:'MARK'},
            NavKeys:{FOCUS:1},
            onClick:function(profile, e, src){
                var p=profile.properties,b=profile.boxing();
                if(p.disabled)return false;
                if(p.readonly)return false;
                b.setUIValue(!p.$UIvalue,null,null,'click');
                if(profile.onChecked)b.onChecked(profile, e, p.$UIvalue);
                profile.getSubNode('FOCUS').focus(true);
            },
            FOCUS:{
                onKeydown:function(profile, e, src){
                    var key = ood.Event.getKey(e).key;
                    if(key ==' ' || key=='enter'){
                        profile.getRoot().onClick(true);
                        return false;
                    }
                }
            }
        },
        DataModel:{
            // 现代化属性
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
            
            value:false,
            expression:{
                ini:'',
                action:function () {
                }
            },
            hAlign:{
                ini:'left',
                listbox:['left','center','right'],
                action: function(v){
                    this.getRoot().css('textAlign',v);
                }
            },
            vAlign:{
                ini:'top',
                listbox:['top','middle','bottom'],
                action: function(v){
                    this.getSubNode('VALIGN').css('verticalAlign', v||'');
                }
            },
            iconPos:{
                ini:'left',
                listbox:['left','right'],
                action:function(v){
                    this.getSubNode("MARK").removeClass('ood-float-left ood-float-right').addClass('ood-float-'+v);
                }
            },
            image:{
                format:'image',
                action: function(v){
                    ood.UI.$iconAction(this);
                }
            },
            imagePos:{
                action: function(value){
                    this.getSubNode('ICON').css('backgroundPosition', value||'center');
                }
            },
            imageBgSize:{
                action: function(value){
                    this.getSubNode('ICON').css('backgroundSize', value||'');
                }
            },
            imageClass: {
                ini:'',
                action:function(v,ov){
                    ood.UI.$iconAction(this, 'ICON', ov);
                }
            },
            iconFontCode:{
                action:function(v){
                    ood.UI.$iconAction(this);
                }
            },
            caption:{
                ini:undefined,
                action: function(v){
                    v=(ood.isSet(v)?v:"")+"";
                    this.getSubNode('CAPTION').html(ood.adjustRes(v,true));
                }
            },
            excelCellId:{
                ini:"",
                action:function(){
                    this.boxing().notifyExcel(false);
                }
            }
        },
        EventHandlers:{
            onChecked:function(profile, e, value){},
            onGetExcelCellValue:function(profile, excelCellId, dftValue){}
        },
        RenderTrigger:function(){
            var ns=this,p=ns.properties;
            if(p.excelCellId)
                ns.boxing().notifyExcel();
                
            // 现代化功能初始化
            var self = this;
            ood.asyRun(function(){
                self.boxing().CheckBoxTrigger();
            });
        },

        _prepareData:function(profile){
            var data=arguments.callee.upper.call(this, profile);
            data._hAlign = 'text-align:'+data.hAlign+';';
            data._vAlign = 'vertical-align:'+(data.vAlign||'');
            data._iconPosCls = 'ood-float-'+data.iconPos;
            return data;
        },
        _ensureValue:function(profile, value){
            return value==="0"?false:!!value;
        }
    }
});
