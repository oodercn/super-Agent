ood.Class("ood.UI.Video", "ood.UI.Audio",{
    Instance:{
        // Video-specific methods
        enterFullscreen: function() {
            return this.each(function(profile) {
                var h5 = profile.getSubNode('H5').get(0);
                if (h5 && h5.requestFullscreen) {
                    h5.requestFullscreen();
                } else if (h5 && h5.webkitRequestFullscreen) {
                    h5.webkitRequestFullscreen();
                } else if (h5 && h5.mozRequestFullScreen) {
                    h5.mozRequestFullScreen();
                } else if (h5 && h5.msRequestFullscreen) {
                    h5.msRequestFullscreen();
                }
            });
        },
        
        exitFullscreen: function() {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.msExitFullscreen) {
                document.msExitFullscreen();
            }
            return this;
        },
        
        // Override Audio's adjustLayout method for video features
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    h5 = profile.getSubNode('H5'),
                    canvas = profile.getSubNode('CANVAS'),
                    prop = profile.properties;

                // Adjust layout for small screens
                if (width < 768) {
                    root.addClass('video-mobile');
                    
                    // Adjust video player size for mobile
                    h5.css({
                        'width': '100%',
                        'height': 'auto',
                        'max-height': 'var(--ood-video-max-height, 60vh)',
                        'border-radius': 'var(--ood-border-radius-md)',
                        'box-shadow': 'var(--ood-shadow-sm)'
                    });
                    
                    if (canvas && !canvas.isEmpty()) {
                        canvas.css({
                            'width': '100%',
                            'height': 'auto',
                            'border-radius': 'var(--ood-border-radius-md)'
                        });
                    }
                } else {
                    root.removeClass('video-mobile');
                    
                    // Restore desktop styles
                    h5.css({
                        'width': '',
                        'height': '',
                        'max-height': ''
                    });
                    
                    if (canvas && !canvas.isEmpty()) {
                        canvas.css({
                            'width': '',
                            'height': ''
                        });
                    }
                }

                // Special handling for extra small screens
                if (width < 480) {
                    root.addClass('video-tiny');
                    
                    // Limit maximum height
                    h5.css({
                        'max-height': 'var(--ood-video-max-height-tiny, 40vh)',
                        'border-radius': 'var(--ood-border-radius-sm)'
                    });
                } else {
                    root.removeClass('video-tiny');
                }
            });
        }
    },


    // 设置主题
    setTheme: function(theme) {
        return this.each(function(profile) {
            var root = profile.getRoot();
            
            // 应用主题属性
            root.attr('data-theme', theme);
            
            // 添加基础类名
            root.addClass('ood-video');
            
            // 保存主题设置
            localStorage.setItem('video-theme', theme);
            profile.properties.theme = theme;
        });
    },
            


    // Get current theme
    getTheme: function() {
        var profile = this.get(0);
        return profile.properties.theme || localStorage.getItem('video-theme') || 'light';
    },

    VideoTrigger: function() {
        var profile = this.get(0);
        var prop = profile.properties;
        var boxing = this;

        // Initialize theme
        if (prop.theme) {
            boxing.setTheme(prop.theme);
        } else {
            // Restore theme from local storage
            var savedTheme = localStorage.getItem('video-theme') || localStorage.getItem('audio-theme');
            if (savedTheme) {
                boxing.setTheme(savedTheme);
            } else {
                // Use system theme by default
                var darkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
                boxing.setTheme(darkMode ? 'dark' : 'light');
            }
        }

    // Initialize responsive design
    if (prop.responsive !== false) {
        boxing.adjustLayout();
    }

    // Initialize accessibility
    boxing.enhanceAccessibility();
}   ,


    // Enhance accessibility support
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    h5 = profile.getSubNode('H5'),
                    canvas = profile.getSubNode('CANVAS'),
                    properties = profile.properties;

                // Add ARIA attributes to container
                root.attr({
                    'role': 'application',
                    'aria-label': ood.getRes('UI.video.player')
                });
                
                // Add ARIA attributes to video element
                if (h5 && !h5.isEmpty()) {
                    h5.attr({
                        'aria-label': ood.getRes('UI.video.controls'),
                        'role': 'application'
                    });
                    
                    // Set attributes based on state
                    if (properties.controls) {
                        h5.attr('aria-controls', 'true');
                    }
                    
                    if (properties.muted) {
                        h5.attr('aria-label', ood.getRes('UI.video.player.muted'));
                    }
                    
                    if (properties.poster) {
                        h5.attr('aria-describedby', ood.getRes('UI.video.poster'));
                    }
                }
                
                // Add ARIA attributes to canvas
                if (canvas && !canvas.isEmpty()) {
                    canvas.attr({
                        'role': 'img',
                        'aria-label': ood.getRes('UI.video.canvas')
                    });
                }
            });
        },
    Static:{
        Templates:{
            tagName:'div',
            crossOrigin:  'anonymous',
            className:'{_className}',
            style:'{_style}',
            H5:{
                tagName:'video',
                autoplay:'{_autoplay}',
                controls:'{_controls}',
                loop:'{_loop}',
                muted:'{_muted}',
                type:'application/x-mpegURL',
                preload:'{preload}',
                volume:'{volume}',
                src:'{src}',
                width:'{width}',
                height:'{height}',
                text:'Your browser does not support the video element.'
            },
            CANVAS:{
                tagName:'canvas',
                style:"background-image:url("+ood.ini.img_bg+");"
            },
            COVER:{
                tagName:'div',
                style:"background-image:url("+ood.ini.img_bg+");"
            }
        },
        DataModel:{
            // Modern properties
            theme: {
                ini: 'dark',
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
            
            width:{
                $spaceunit:1,
                ini:'34em'
            },
            height:{
                $spaceunit:1,
                ini:'25em'
            },
            poster:{
                format:'image',
                ini: '',
                action:function(v){
                    this.getSubNode("H5").attr("poster", v||null);
                }
            }
        },
        RenderTrigger:function(){
            var prf=this,
                H5 = prf.getSubNode('H5'),
                prop = prf.properties,
                t;
            if(t=prop.poster)H5.attr("poster",t);
            
            // Modern feature initialization
            ood.asyRun(function(){
                prf.boxing().VideoTrigger();
            });
        }
    }
});