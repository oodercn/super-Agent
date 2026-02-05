/**
 * 移动端面板组件
 * 继承自ood.Mobile.Base，符合ood框架规范
 * 支持标题、边框、阴影、圆角等样式配置
 */
ood.Class("ood.Mobile.Panel", "ood.UI.Div",  {
    Instance: {
        Initialize: function() {
           // this.constructor.upper.prototype.Initialize.call(this);
            this.initPanelFeatures();
        },
        
        initPanelFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-panel');
            this.updatePanelStyle();
        },
        
        updatePanelStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            
            // 应用样式类
            root.removeClass('ood-mobile-panel-bordered ood-mobile-panel-shadow ood-mobile-panel-rounded');
            
            if (props.bordered) root.addClass('ood-mobile-panel-bordered');
            if (props.shadow) root.addClass('ood-mobile-panel-shadow');
            if (props.rounded) root.addClass('ood-mobile-panel-rounded');
        },

    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-panel {_styleClasses}',
            style: '{_style}',
            
            HEADER: {
                tagName: 'div',
                className: 'ood-mobile-panel-header',
                style: 'display: {_headerDisplay}',
                
                TITLE: {
                    tagName: 'div',
                    className: 'ood-mobile-panel-title',
                    text: '{title}'
                },
                
                EXTRA: {
                    tagName: 'div',
                    className: 'ood-mobile-panel-extra',
                    style: 'display: {_extraDisplay}'
                }
            },

            MAIN: {
                $order: 2,
                tagName: 'div',
                className: 'ood-mobile-panel-content',
                style: "{_leftp}",
                MAINI: {
                    tagName: 'div',
                    className: 'ood-uicon-maini ood-uibar',
                    style: "{_rightp}",
                    PANEL: {
                        tagName: 'div',
                        className: 'ood-uibase ood-uicontainer {_bordertype}',
                        style: '{panelDisplay};{_panelstyle};{_overflow};',
                        text: '{html}' + ood.UI.$childTag
                    }
                }
            }
            //
            // CONTENT: {
            //     tagName: 'div',
            //     className: 'ood-mobile-panel-content'
            // }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                'background-color': 'var(--mobile-bg-primary)',
                width: '100%'
            },
            
            'KEY.ood-mobile-panel-bordered': {
                border: '1px solid var(--mobile-border-color)'
            },
            
            'KEY.ood-mobile-panel-shadow': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            'KEY.ood-mobile-panel-rounded': {
                'border-radius': 'var(--mobile-border-radius)'
            },
            
            HEADER: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4'
            },
            
            EXTRA: {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)'
            },
            
            MAIN: {
                padding: 'var(--mobile-spacing-md)'
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '面板标题',
                ini: '面板组件',
                action: function(value) {
                    var profile = this.boxing().get(0);
                    // 同步更新title属性
                    profile.properties.title = value;
                    profile.getRoot().attr('aria-label', value || '面板组件');
                }
            },
            
            width: {
                caption: '面板宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '面板高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            backgroundColor: {
                caption: '背景颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('background-color', value);
                    }
                }
            },
            
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        this.getRoot().css('border-color', value);
                    }
                }
            },
            
            titleColor: {
                caption: '标题颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var titleNode = this.getSubNode('TITLE');
                    if (value && titleNode && !titleNode.isEmpty()) {
                        titleNode.css('color', value);
                    }
                }
            },
            
            // ===== 面板特有属性 =====
            // 继承基类的主题和可访问性属性
            theme: {
                caption: '主题模式',
                ini: 'light',
                listbox: ['light', 'dark', 'light-hc', 'dark-hc'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            responsive: {
                caption: '响应式布局',
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            title: {
                caption: '面板标题',
                ini: '',
                action: function(value) {
                    var profile = this.get(0);
                    var titleNode = profile.getSubNode('TITLE');
                    var headerNode = profile.getSubNode('HEADER');
                    profile.properties.title = title;
                    if (title) {
                        titleNode.toHtml(title)
                        headerNode.css('display', 'block');
                    } else {
                        headerNode.css('display', 'none');
                    }

                }
            },
            
            bordered: {
                caption: '显示边框',
                ini: true,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
            },
            
            shadow: {
                caption: '显示阴影',
                ini: false,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
            },
            
            rounded: {
                caption: '圆角样式',
                ini: true,
                action: function(value) {
                    this.boxing().updatePanelStyle();
                }
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            var classes = [];
            if (props.bordered) classes.push('ood-mobile-panel-bordered');
            if (props.shadow) classes.push('ood-mobile-panel-shadow');
            if (props.rounded) classes.push('ood-mobile-panel-rounded');
            
            data._styleClasses = classes.join(' ');
            data._headerDisplay = props.title ? 'flex' : 'none';
            data._extraDisplay = 'none'; // 默认隐藏额外内容
            
            return data;
        }
    }
});