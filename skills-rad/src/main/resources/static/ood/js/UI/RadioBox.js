ood.Class("ood.UI.RadioBox", "ood.UI.List", {
        Initialize: function () {
            //modify default template for shell
            var t = this.getTemplate();
            t.className = '{_className}';
            t.ITEMS.className = '{_bordertype}';
            t.$submap = {
                items: {
                    ITEM: {
                        className: 'ood-showfocus {_itemRow} {itemClass} {disabled} {readonly}',
                        style: '{itemStyle}{_itemDisplay}',
                        tabindex: '{_tabindex}',
                        MARK: {
                            $order: 0,
                            className: 'oodfont',
                            $fonticon: '{_fi_markcls}'
                        },
                        ICON: {
                            $order: 1,
                            className: 'oodcon {imageClass}  {picClass}',
                            style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                            text: '{iconFontCode}'
                        },
                        CAPTION: {
                            text: '{caption}',
                            $order: 2
                        }
                    }
                }
            };
            this.setTemplate(t);
        },
        Instance: {

            iniProp: {
                items: [
                    {id: 'option1', caption: '选项1', value: '1'},
                    {id: 'option2', caption: '选项2', value: '2'},
                    {id: 'option3', caption: '选项3', value: '3'},
                    {id: 'option4', caption: '选项4', value: '4', disabled: true}
                ],

            value: 'option1',
            checkBox: true,
            selMode: "multibycheckbox"
            , caption: ood.getRes('RAD.widgets.selection') || "选择"
        },
        // 设置主题 (优化版)
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 动态加载主题CSS
                if (!document.getElementById('radiobox-theme-css')) {
                    var link = document.createElement('link');
                    link.id = 'radiobox-theme-css';
                    link.rel = 'stylesheet';
                    link.href = '/ood/css/radiobox-themes.css';
                    document.head.appendChild(link);
                }


                // 应用主题类
                root.removeClass('radiobox-dark radiobox-light radiobox-hc');
                root.addClass('radiobox-' + theme);

                // 保存主题设置
                localStorage.setItem('radiobox-theme', theme);

                // 触发主题变更事件
                //  ood(root).trigger('themeChanged', {theme: theme});
            });
        },

        // 获取当前主题
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('radiobox-theme') || 'light';
        },


        RadioBoxTrigger: function () {
            var profile = this.get(0);
            var prop = profile.properties,
                boxing = this;

            // 初始化现代化功能
            // 初始化主题
            if (prop.theme) {
                boxing.setTheme(prop.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('radiobox-theme');
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
        toggleDarkMode: function () {
            var currentTheme = this.getTheme();
            this.setTheme(currentTheme === 'light' ? 'dark' : 'light');
            return this;
        },

        // 响应式布局调整
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    width = ood(document.body).cssSize().width,
                    prop = profile.properties;

                // 清除所有响应式类
                root.removeClass('radiobox-mobile radiobox-tiny radiobox-small');

                // 根据屏幕宽度应用响应式类
                if (width < 480) {
                    root.addClass('radiobox-tiny');
                } else if (width < 768) {
                    root.addClass('radiobox-mobile');
                } else if (width < 1024) {
                    root.addClass('radiobox-small');
                }

                // 触发resize事件让CSS生效
                //ood(window).fireEvent('resize');
            });
        },

        // 增强可访问性支持 (优化版)
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNode('ITEM', true),
                    marks = profile.getSubNode('MARK', true),
                    properties = profile.properties;

                // 为容器添加ARIA属性
                root.attr({
                    'role': 'radiogroup',
                    'aria-label': properties.caption || '单选按钮组',
                    'aria-orientation': properties.vertical ? 'vertical' : 'horizontal'
                });

                // 为每个选项添加ARIA属性
                items.each(function (item) {
                    var itemNode = ood(item);
                    var itemId = itemNode.id();
                    if (itemId) {
                        var itemData = profile.getItemByDom(item);
                        if (itemData) {
                            itemNode.attr({
                                'role': 'radio',
                                'aria-checked': itemData.selected ? 'true' : 'false',
                                'aria-label': itemData.caption || itemData.id,
                                'tabindex': itemData.selected ? '0' : '-1',
                                'aria-disabled': itemData.disabled ? 'true' : 'false'
                            });

                            // 添加键盘导航支持
                            itemNode.on('keydown', function (e) {
                                if (e.key === 'Enter' || e.key === ' ') {
                                    // itemNode.fireEvent('click');
                                    e.preventDefault();
                                }
                            });
                        }
                    }
                });

                // 为标记添加ARIA属性
                marks.each(function (mark) {
                    var markNode = ood(mark);
                    markNode.attr({
                        'aria-hidden': 'true',
                        'focusable': 'false'
                    });
                });
            });
        }
    },
    Static
:
{
    _DIRTYKEY:'MARK',
        _ITEMMARKED
:
    true,
        Appearances
:
    {
        ITEM:{
            className: 'ood-radiobox-item'
        }
    ,
        'ITEM-checked'
    :
        {
            className: 'ood-radiobox-item-checked'
        }
    ,
        CAPTION:{
            className: 'ood-radiobox-caption'
        }
    ,
        ITEMS:{
            className: 'ood-radiobox-items'
        }
    ,
        MARK:{
            className: 'ood-radiobox-mark'
        }
    ,

        // 响应式样式
        'radiobox-mobile ITEM'
    :
        {
            className: 'radiobox-mobile .ood-radiobox-item'
        }
    ,
        'radiobox-mobile MARK'
    :
        {
            className: 'radiobox-mobile .ood-radiobox-mark'
        }
    ,
        'radiobox-mobile CAPTION'
    :
        {
            className: 'radiobox-mobile .ood-radiobox-caption'
        }
    ,
        'radiobox-tiny ICON'
    :
        {
            className: 'radiobox-tiny .ood-radiobox-icon'
        }
    ,
        'radiobox-tiny CAPTION'
    :
        {
            className: 'radiobox-tiny .ood-radiobox-caption'
        }
    ,
        'radiobox-tiny ITEM'
    :
        {
            className: 'radiobox-tiny .ood-radiobox-item'
        }
    }
,
    DataModel:{
        // 现代化属性
        theme: {
            ini: 'light',
                listbox
        :
            ['light', 'dark', 'high-contrast'],
                action
        :

            function (value) {
                this.boxing().setTheme(value);
            }
        }
    ,
        responsive: {
            ini: true,
                action
        :

            function (value) {
                if (value) {
                    this.boxing().adjustLayout();
                }
            }
        }
    ,

        expression:{
            ini:'',
                action
        :

            function () {
            }
        }
    ,
        tagCmds:null,
            borderType
    :
        {
            ini:'none'
        }
    ,
        checkBox:{
            ini:false,
                action
        :

            function (v) {
                this.getSubNode('MARK', true).replaceClass(v ? /(uicmd-radio)|(\s+uicmd-radio)/g : /(^uicmd-check)|(\s+uicmd-check)/g, v ? ' ood-uicmd-check' : ' ood-uicmd-radio');
            }
        }
    }
,
    Behaviors:{
        HoverEffected:{
            ITEM:null, MARK
        :
            'MARK'
        }
    ,
        ClickEffected:{
            ITEM:null, MARK
        :
            'MARK'
        }
    }
,
    EventHandlers:{
        onCmd:null
    }
,
    _prepareItem:function (profile, item) {
        item._fi_markcls = profile.properties.checkBox ? 'ood-uicmd-check' : 'ood-uicmd-radio';
        item._itemRow = profile.properties.itemRow ? 'ood-item-row' : '';
    }
,

    RenderTrigger: function () {
        // 现代化功能初始化
        var self = this;
        ood.asyRun(function () {
            self.boxing().RadioBoxTrigger();
        });
    }


}
})
;
