ood.Class("ood.UI.Camera", "ood.UI.Audio",{
    Instance:{
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            patterned: false,
            dock: 'fill'
        }
    },
    Static:{
        Templates:{
            tagName:'div',
            crossOrigin:  'anonymous',
            className:'{_className} ood-camera-container',
            style:'{_style};background-color:var(--ood-bg);border:1px solid var(--ood-border);',
            H5:{
                tagName:'camera',
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
                style:'background-color:var(--ood-bg-secondary)',
                text:'Your browser does not support the video element.'
            },
            COVER:{
                tagName:'div',
                style:"background-image:url("+ood.ini.img_bg+");background-color:var(--ood-overlay)"
            }
        },
        DataModel:{
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
        }
    }
});