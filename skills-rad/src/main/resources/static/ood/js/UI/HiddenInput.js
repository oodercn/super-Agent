ood.Class("ood.UI.HiddenInput", ["ood.UI.Widget","ood.absValue"] ,{
    Instance:{
        activate:function(){
            return this;
        }

    },


    Static:{
      //  $initRootHidden:true,
        Templates:{
            className:'ood-display-none',
            style:'display:none',
            tagName:'input',
            type:'hidden'
        },
        DataModel:{
            expression:null,
            locked:null,
            required:null,
            dataBinder:null,
            dataField:null,
            display:null,
            visibility:null,
            position:null,
            left:null,
            top:null,
            right:null,
            bottom:null,
            width:null,
            height:null,
            rotate:null,
            showEffects:null,
            hideEffects:null,
            activeAnim:null,
            tabindex:null,
            zIndex:null,
            defaultFocus:null,
            hoverPop:null,
            hoverPopType:null,
            disabled:null,
            readonly:null,
            disableClickEffect:null,
            disableHoverEffect:null,
            dock:null,
            dockOrder:null,
            dockMargin:null,
            dockMinW:null,
            dockMinH:null,
            dockMaxW:null,
            dockMaxH:null,
            dockFloat:null,
            dockIgnore:null,
            dirtyMark:null,
            showDirtyMark:null,
            selectable:null,
            autoTips:null,
            tips:null,
            disableTips:null,
            renderer:null,
            className:null
        },
        EventHandlers:{
            beforeDirtyMark:null,
            onContextmenu:null,
            onDock:null,
            onLayout:null,
            onMove:null,
            onRender:null,
            onResize:null,
            onShowTips:null,
            beforeAppend:null,
            afterAppend:null,
            beforeRender:null,
            afterRender:null,
            beforeRemove:null,
            afterRemove:null,
            onHotKeydown:null,
            onHotKeypress:null,
            onHotKeyup:null
        },
        setUIValue:function(value, force, triggerEventOnly, tag){
            var upper=arguments.callee.upper,
                v = upper.apply(this,ood.toArr(arguments));
        },
        getUIValue:function(){
            var upper=arguments.callee.upper,
                v = upper.apply(this,ood.toArr(arguments));
            upper=null;
            return v;
        },
        _ensureValue:function(profile, value){
            // ensure return string
            return ""+(ood.isSet(value)?value:"");
        }
    }
});