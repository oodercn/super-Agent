ood.Class("ood.UI.Audio", "ood.UI",{
    Instance:{
        play:function(){
            var v = this.getSubNode("H5"), vn = v.get(0);if(vn&&this.getSrc())vn.play();
        },
        pause:function(){
            var v = this.getSubNode("H5"), vn = v.get(0);if(vn&&this.getSrc())vn.pause();
        },
        load:function(){
            var v = this.getSubNode("H5"), vn = v.get(0);if(vn&&this.getSrc())vn.load();
        },
        canPlayType:function(type){
            var v = this.getSubNode("H5"), vn = v.get(0);if(vn) return vn.canPlayType(type);
        },
        
        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();
                
                // 移除所有主题类
                root.removeClass('audio-dark audio-hc');
                
                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('audio-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('audio-hc');
                }
                
                // 保存主题设置
                localStorage.setItem('audio-theme', theme);
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('audio-theme') || 'light';
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

                // 对于小屏幕，调整布局
                if (width < 768) {
                    root.addClass('audio-mobile');
                } else {
                    root.removeClass('audio-mobile');
                }

                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('audio-tiny');
                } else {
                    root.removeClass('audio-tiny');
                }
            });
        },
        
        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    h5 = profile.getSubNode('H5'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'application',
                    'aria-label': ood.getRes('UI.audio.player'),
                    'aria-live': 'polite'
                });
                
                // 为音频元素添加ARIA属性
                if (h5 && !h5.isEmpty()) {
                    h5.attr({
                        'aria-label': ood.getRes('UI.audio.controls'),
                        'role': 'application',
                        'aria-disabled': properties.disabled ? 'true' : 'false',
                        'aria-hidden': 'false'
                    });
                    
                    // 根据状态设置属性
                    if (properties.controls) {
                        h5.attr('aria-controls', 'true');
                    }
                    
                    if (properties.muted) {
                        h5.attr('aria-label', ood.getRes('UI.audio.player.muted'));
                    }
                    
                    // 键盘导航支持
                    h5.on('keydown', function(e) {
                        var key = e.keyCode;
                        // 空格键或回车键触发播放/暂停
                        if (key === 32 || key === 13) {
                            e.preventDefault();
                            var audio = h5.get(0);
                            if (audio.paused) {
                                audio.play();
                            } else {
                                audio.pause();
                            }
                        }
                    });
                }
            });
        }
    },
    Static:{
        Appearances:{
            KEY:{
                className: 'ood-audio'
            },
            H5:{
                className: 'ood-audio-controls'
            },
            COVER:{
                className: 'ood-audio-cover'
            },
            'KEY:hover COVER': {
                className: 'ood-audio:hover .ood-audio-cover'
            }
        },
        Templates:{
            tagName:'div',
            className:'{_className}',
            style:'{_style}',
            H5:{
                tagName:'audio',
                crossOrigin:  'anonymous',
                autoplay:'{_autoplay}',
                controls:'{_controls}',
                loop:'{_loop}',
                muted:'{_muted}',

                preload:'{preload}',
                volume:'{volume}',
                src:'{src}',
                text:'Your browser does not support the audio element.'
            },
            COVER:{
                tagName:'div',
                style:"background-image:url("+ood.ini.img_bg+");"
            }
        },
        Behaviors:{
            HotKeyAllowed:false
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
            
            selectable:true,
            width:{
                $spaceunit:1,
                ini:'18em'
            },
            height:{
                $spaceunit:1,
                ini:'5em'
            },
            src:{
                format:'media',
                ini:'',
                action:function(v){
                    this.getSubNode("H5").attr("src", ood.adjustRes(v));
                }
            },
            cover:false,
            controls:{
                ini: true,
                action:function(v){
                    this.getSubNode("H5").attr("controls", v?'controls':null);
                }
            },
            preload:{
                ini: "none",
                listbox:["none", "metadata", "auto" ],
                action:function(v){
                    this.getSubNode("H5").attr("preload", (!v||v=='none')?null:v);
                }
            },
            loop:{
                ini: false,
                action:function(v){
                    this.getSubNode("H5").attr("loop", v?'loop':null);
                }
            },
            muted:{
                ini: false,
                action:function(v){
                    this.getSubNode("H5").attr("muted", v?'muted':null);
                }
            },
            volume:{
                ini: 1,
                action:function(v){
                    this.getSubNode("H5").attr("volume", v);
                }
            },
            autoplay:{
                ini: false,
                action:function(v){
                    this.getSubNode("H5").attr("autoplay", v?'autoplay':null);
                }
            }
        },
        RenderTrigger:function(){
            var prf = this,
                H5 = prf.getSubNode('H5'),
                prop = prf.properties,
                ef = function(event){
                    if(prf.onMediaEvent){
                        prf.boxing().onMediaEvent(prf, (event||window.event).type,  arguments);
                    }
                },t;
   
            ood.arr.each("loadstart progress durationchange seeked seeking timeupdate playing canplay canplaythrough volumechange ratechange loadedmetadata loadeddata play pause ended".split(" "), function(event, i){
                if(i = H5&&H5.get(0))
                    ood.Event._addEventListener(i, event, ef);
            });
            
            (prf.$beforeDestroy=(prf.$beforeDestroy||{}))["detachEvents"]=function(){
                ood.arr.each("loadstart progress durationchange seeked seeking timeupdate playing canplay canplaythrough volumechange ratechange loadedmetadata loadeddata play pause ended".split(" "),function(event, i){
                    if(i=H5&&H5.get(0))
                        ood.Event._removeEventListener(i, event, ef);
                });
            };

            if(!prop.controls)H5.attr("controls",null);
            if(!prop.loop)H5.attr("loop",null);
            if(!prop.muted)H5.attr("muted",null);

            if(!prop.autoplay)H5.attr("autoplay",null);
            else ood.asyRun(function(t){
                if(prf.$inDesign)return;
                if(prop.src && ood.isStr(prop.src) && (t=H5.get(0)))t.play();}
            );
            
            // 现代化功能初始化
            ood.asyRun(function(){
                prf.boxing().AudioTrigger();
            });
        },
        
        AudioTrigger: function() {
            var profile = this.get(0);
            var prop = profile.properties;
            var boxing = this;

            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('audio-theme');
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
        _prepareData:function(profile){
            var data=arguments.callee.upper.call(this, profile);
            if(data.autoplay)data._autoplay = "autoplay";
            if(data.controls)data._controls = "controls";
            if(data.loop)data._loop = "loop";
            if(data.muted)data._muted = "muted";
            return data;
        },
        EventHandlers:{
            onMediaEvent:function(profile, eventType, params){}
        },
        _onresize:function(profile,width,height){
            var H5=profile.getSubNode('H5'), 
                size=H5.cssSize(),
                prop=profile.properties,
                us = ood.$us(profile),
                adjustunit = function(v,emRate){return profile.$forceu(v, us>0?'em':'px', emRate)},

                // caculate by px
                ww=width?profile.$px(width):width, 
                hh=height?profile.$px(height):height;

            if( (width && !ood.compareNumber(size.width,ww,6)) || (height && !ood.compareNumber(size.height,hh,6)) ){
                // reset here
                if(width){
                    H5.attr("width", ww).width(prop.width=adjustunit(ww));
                }
                if(height){
                    H5.attr("height", hh).height(prop.height=adjustunit(hh));
                }
                if(profile.$inDesign || prop.cover){
                    profile.getSubNode('COVER').cssSize({
                        width:width?prop.width:null,
                        height:height?prop.height:null
                    },true);
                }
            }
        }
    }
});