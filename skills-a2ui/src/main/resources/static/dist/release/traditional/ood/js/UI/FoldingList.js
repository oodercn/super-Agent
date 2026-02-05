ood.Class("ood.UI.FoldingList", ["ood.UI.List"],{
    Instance:{
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {id: 'a', caption: 'tab1', title: 'title1', text: 'text1'},
                {id: 'b', caption: 'tab2', title: 'title2', text: 'text2'},
                {id: 'c', caption: 'tab3', title: 'title3', text: 'text3'},
                {id: 'd', caption: 'tab4', title: 'title4', text: 'text4'},
                {id: 'e', caption: 'tab5', title: 'title5', text: 'text5'}
            ]
        },

        // 设置主题
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 添加基础类名
                root.addClass('foldinglist-themed');

                // 移除所有主题类
                root.removeClass('foldinglist-dark foldinglist-hc');

                // 应用当前主题类
                if (theme === 'dark') {
                    root.addClass('foldinglist-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('foldinglist-hc');
                }

                // 保存主题设置
                localStorage.setItem('foldinglist-theme', theme);
            });
        },

        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('foldinglist-theme') || 'light';
        },

        // 切换主题
        toggleTheme: function() {
            const themes = ['light', 'dark', 'high-contrast'];
            const currentTheme = this.getTheme();
            const nextIndex = (themes.indexOf(currentTheme) + 1) % themes.length;
            this.setTheme(themes[nextIndex]);
            return this;
        },

        fillContent:function(id, obj){
            var profile=this.get(0),t,item;
            if(profile.renderId){
                if(item=profile.getItemByItemId(id)){
                    t=profile.getSubNodeByItemId('BODYI',id).html('');
                    if(obj){
                        item._obj = obj;
                        item._fill=true;
                        if(typeof obj=='string')t.html(obj);
                        else t.append(obj.render(true));
                    }else
                        item._obj=item._fill=null;
                }
            }
            return this;
        },
        toggle:function(id){
            var profile=this.get(0);
            if(profile.renderId){
                var properties = profile.properties,
                    items=properties.items,
                    item = profile.getItemByItemId(id),
                    subId = profile.getSubIdByItemId(id),
                    node = profile.getSubNode('ITEM',subId),
                    toggle = profile.getSubNode('TOGGLE',subId),
                    nodenext = node.next(),t
                ;
                if(item._show){
                    if(properties.activeLast && items.length)
                        if(items[items.length-1].id==item.id)
                            return false;

                    node.tagClass('-checked',false);
                    toggle.tagClass('-checked',false);
                    if(nodenext)
                        nodenext.tagClass('-prechecked',false);
                }else{
                    node.tagClass('-checked');
                    toggle.tagClass('-checked');
                    if(nodenext)
                        nodenext.tagClass('-prechecked');
                    //fill value
                    if(!item._fill){
                        var callback=function(o){
                            profile.boxing().fillContent(item.id, item._body=o);
                        };
                        if(profile.onGetContent){
                            var r = profile.boxing().onGetContent(profile, item, callback);
                            if(r) callback(r);
                        }else
                            callback(profile.box._buildBody(profile, item));
                    }
                }
                item._show=!item._show
            }
            return this;
        }
    },
    Initialize:function(){
        //modify default template for modern UI
        var t = this.getTemplate();
        t.ITEMS.className='{_bordertype} ood-foldinglist-items';
        t.$submap.items={
            ITEM:{
                tagName : 'div',
                className:'ood-uiborder-flat ood-uiborder-radius {_checked} {_precheked} {itemClass} {disabled} {readonly}',
                style:'{itemStyle}',
                HEAD:{
                    tagName : 'div',
                    className:'ood-uibar ood-foldinglist-header',
                    TITLE:{
                        tabindex: '{_tabindex}',
                        TLEFT:{
                            $order:0,
                            tagName:'div',
                            TOGGLE:{
                                $order:0,
                                className:'oodfont ood-foldinglist-toggle',
                                $fonticon:'{_fi_tlg}'
                            },
                            LTAGCMDS:{
                                $order:2,
                                tagName:'span',
                                text:"{ltagCmds}"
                            },
                            CAP1:{
                                $order:1,
                                className:"ood-title-node ood-foldinglist-title",
                                text:'{title}'
                            }
                        },
                        TRIGHT:{
                            $order:1,
                            tagName:'div',
                            style:'{_capDisplay}',
                            CAP2:{
                                $order:0,
                                text:'{caption}'
                            },
                            TAGCMDS:{
                                $order:60,
                                tagName:'span',
                                text:"{rtagCmds}"
                            }
                        }/*,
                        TCLEAR:{
                            $order:2,
                            tagName:'div'
                        }*/
                    }
                },
                BODY:{
                    $order:1,
                    tagName : 'div',
                    className:'ood-uibase ood-foldinglist-body',
                    BODYI:{
                        $order:0,
                        tagName : 'div',
                        text:'{_body}'
                    }
                }
            }
        }
        this.setTemplate(t);
    },
    Static:{
        Appearances:{
            KEY:{
                padding:'var(--ood-spacing-xs)'
            },
            ITEMS:{
                border:0,
                position:'relative',
                padding: 'var(--ood-spacing-md)',
                'background-color': 'var(--ood-bg)',
                'border-radius': 'var(--ood-radius-lg)'
            },
            ITEM:{
                marginBottom: 'var(--ood-spacing-md)',
                padding:0,
                position:'relative',
                overflow:'hidden',
                'border-radius': 'var(--ood-radius-md)',
                'box-shadow': 'var(--ood-shadow-sm)',
                transition: 'all var(--ood-transition-normal)',
                'background-color': 'var(--ood-bg-card)'
            },
            'HEAD, BODY, BODYI, TAIL':{
                position:'relative'
            },
            BODY:{
                display:'none',
                position:'relative',
                overflow:'auto',
                maxHeight: '500px',
                transition: 'all var(--ood-transition-normal)',
                'background-color': 'var(--ood-bg)'
            },
            BODYI:{
                padding: 'var(--ood-spacing-md)',
                position:'relative'
            },
            'ITEM-hover':{
                'box-shadow': 'var(--ood-shadow-md)',
                transform: 'translateY(-2px)',
                transition: 'all var(--ood-transition-normal)'
            },
            'ITEM-checked':{
                $order:2,
                'margin-bottom':'var(--ood-spacing-md)'
             },
            'ITEM-checked BODY':{
                $order:2,
                display:'block',
                animation: 'ood-foldinglist-open 0.3s ease-out'
            },
            HEAD:{
                cursor:'pointer',
                position:'relative',
                overflow:'hidden',
                transition: 'all var(--ood-transition-fast)',
                'background-color': 'var(--ood-bg-header)',
                'border-bottom': '1px solid var(--ood-border)'
            },
            TITLE:{
                $order:1,
                display:'flex',
                'align-items':'center',
                position:'relative',
                'white-space':'nowrap',
                overflow:'hidden',
                padding: 'var(--ood-spacing-md)'
            },
            'CAP1, CAP2':{
                padding:'var(--ood-spacing-xs)',
                'vertical-align':'middle'
            },
            CAP1:{
                cursor:'pointer',
                'white-space':'nowrap',
                flex: 1,
                fontSize: 'var(--ood-font-size-lg)',
                fontWeight: 500,
                color: 'var(--ood-text-heading)'
            },
            'ITEM-checked CAP1':{
                $order:2,
                'font-weight':'600',
                color: 'var(--ood-primary)'
            },
            TLEFT:{
                display: 'flex',
                'align-items': 'center',
                position:'relative',
                'white-space':'nowrap',
                overflow:'hidden',
                flex: 1
            },
            TRIGHT:{
                display: 'flex',
                'align-items': 'center',
                position:'relative',
                'white-space':'nowrap',
                overflow:'hidden',
                marginLeft: 'auto'
            },
            '.ood-foldinglist-toggle':{
                fontSize: 'var(--ood-font-size-xl)',
                marginRight: 'var(--ood-spacing-md)',
                transition: 'transform var(--ood-transition-normal)',
                color: 'var(--ood-text-muted)'
            },
            '.ood-foldinglist-toggle.-checked':{
                transform: 'rotate(90deg)',
                color: 'var(--ood-primary)'
            },
            '.ood-foldinglist-title':{
                transition: 'color var(--ood-transition-fast)',
                fontSize: 'var(--ood-font-size-lg)',
                fontWeight: 500,
                color: 'var(--ood-text-heading)'
            },
            '.ood-foldinglist-header:hover .ood-foldinglist-title':{
                color: 'var(--ood-primary)'
            },
            '.ood-foldinglist-header':{
                borderRadius: 'var(--ood-radius-md) var(--ood-radius-md) 0 0',
                borderBottom: 'none'
            },
            '.ood-foldinglist-body':{
                borderRadius: '0 0 var(--ood-radius-md) var(--ood-radius-md)',
                borderTop: 'none'
            }
        },
        Behaviors:{
            HoverEffected:{ITEM:'ITEM',HEAD:'HEAD',OPT:'OPT'},
            ClickEffected:{ITEM:null,HEAD:'HEAD'},
            ITEM:{onClick:null,onKeydown:null},
            HEAD:{
                onClick:function(profile, e, src){
                    profile.boxing().toggle(profile.getItemIdByDom(src));
                    return false;
                }
            },
            OPT:{
                onMousedown:function(){
                    return false;
                },
                onClick:function(profile, e, src){
                    profile.boxing().onShowOptions(profile, profile.getItemByDom(src), e, src);
                    return false;
                }
            }
        },
        DataModel:{
            expression:{
                ini:'',
                action:function () {
                }
            },
            value: null,
            borderType: null,
            activeLast: true,
            // 新增现代化配置项
            transitionDuration: 'normal', // 可选: 'fast', 'normal', 'slow'
            collapsible: true,
            // 主题设置
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            // 响应式布局
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            }
        },
        EventHandlers:{
            onGetContent:function(profile,item,onEnd){},
            onShowOptions:function(profile,item,e,src){}
        },
        RenderTrigger:function(){
            var self=this, pro=self.properties, items=pro.items, item;
            if(pro.activeLast && items.length>0){
                item=items[items.length-1];
                self.boxing().fillContent(item.id, item._body);
            }

            // 添加动画样式
            if(!document.getElementById('ood-foldinglist-animations')){
                var style = document.createElement('style');
                style.id = 'ood-foldinglist-animations';
                style.textContent = `
                    @keyframes ood-foldinglist-open {
                        from { opacity: 0; maxHeight: 0; }
                        to { opacity: 1; maxHeight: 500px; }
                    }
                `;
                document.head.appendChild(style);
            }

            // 初始化主题
            if (pro.theme) {
                self.boxing().setTheme(pro.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('foldinglist-theme');
                if (savedTheme) {
                    self.boxing().setTheme(savedTheme);
                }
            }

            // 初始化响应式设计
            if (pro.responsive !== false) {
                self.boxing().adjustLayout();
            }

            // 初始化可访问性
            self.boxing().enhanceAccessibility();
        },
        _prepareItems:function(profile, arr, pid){
            if(arr.length){
                arr[0]._precheked = profile.getClass('ITEM','-prechecked');
                if(profile.properties.activeLast){
                    //for properties.data
                    var item = arr[arr.length-1];
                    item._show = true;
                    item._fill = true;
                    item._body = profile.onGetContent?profile.boxing().onGetContent(profile,item,function(o){
                        profile.boxing().fillContent(item.id, item._body=o);
                    }) : profile.box._buildBody(profile, item);
                }
            }
            return arguments.callee.upper.apply(this, arguments);
        },
        _prepareItem:function(profile, item){
            var p = profile.properties,o,
                dpn = 'display:none';
            item._tabindex = p.tabindex;
            if(!item.caption)
                item._capDisplay=dpn;
            else
                item.caption = item.caption.replace(/</g,"&lt;");
            item._body= item._body || '加载中...';

            if(item._show){
                item._checked = profile.getClass('ITEM','-checked');
                item._fi_tlg = 'oodfont-checked ood-uicmd-toggle ood-uicmd-toggle-checked';
            }else{
                item._fi_tlg = 'ood-uicmd-toggle';
            }

            this._prepareCmds(profile, item);
        },
        _buildBody:function(profile,item){
            return item.text?'<pre class="ood-node ood-node-div">'+item.text.replace(/</g,"&lt;")+'</pre>':'';
        },


        // 增强可访问性支持
        enhanceAccessibility: function() {
            return this.each(function(profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNodes('ITEM'),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'tree',
                    'aria-label': '可折叠列表'
                });

                // 为每个项目添加ARIA属性
                items.forEach(function(item, index) {
                    var itemId = profile.getItemIdByDom(item),
                        isExpanded = profile.getItemByItemId(itemId)._show || false;

                    item.attr({
                        'role': 'treeitem',
                        'aria-expanded': isExpanded,
                        'aria-level': 1,
                        'aria-posinset': index + 1,
                        'aria-setsize': items.length
                    });

                    // 为标题添加ARIA属性
                    var title = profile.getSubNode('TITLE', profile.getSubIdByItemId(itemId));
                    if (title) {
                        title.attr({
                            'role': 'button',
                            'tabindex': '0'
                        });
                    }
                });
            });
        }
    }
});
