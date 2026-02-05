/**
 * 移动端布局组件
 * 继承自 ood.UI（UI基类）和 ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持flex布局、安全区域适配、响应式布局
 */
ood.Class("ood.Mobile.Layout",  ["ood.UI", "ood.absList"], {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['CONTAINER']);
    },
    Instance: {
        Initialize: function() {
              // 初始化移动端布局特性
            this.initMobileLayoutFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileLayoutFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端布局CSS类
            profile.getRoot().addClass('ood-mobile-layout ood-mobile-component');
            
            // 初始化布局功能
            this.initLayoutFeatures();
        },
        
        initLayoutFeatures: function() {
            // 原有的布局特性初始化逻辑
            this.updateLayoutStyle();
            this.applySafeArea();
        },
        
        updateLayoutStyle: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var container = profile.getSubNode('CONTAINER');
            
            container.css({
                'flex-direction': props.direction || 'column',
                'justify-content': props.justifyContent || 'flex-start',
                'align-items': props.alignItems || 'stretch',
                'gap': props.gap || 'var(--mobile-spacing-md)',
                'padding': props.padding || '0'
            });
        },
        
        applySafeArea: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.safeArea) {
                profile.getRoot().addClass('ood-mobile-layout-safe');
            }
        },
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-layout {_directionClass} {_safeAreaClass}',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-layout-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: '100%'
            },
            
            CONTAINER: {
                display: 'flex',
                width: '100%',
                height: '100%'
            },
            
            'KEY-safe': {
                'padding-top': 'var(--mobile-safe-top)',
                'padding-right': 'var(--mobile-safe-right)',
                'padding-bottom': 'var(--mobile-safe-bottom)',
                'padding-left': 'var(--mobile-safe-left)'
            },
            
            'KEY-row CONTAINER': {
                'flex-direction': 'row'
            },
            
            'KEY-column CONTAINER': {
                'flex-direction': 'column'
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '布局标题',
                ini: '布局容器',
                action: function(value) {
                    var profile = this;
                    // 为布局容器添加无障碍标签
                    profile.getRoot().attr('aria-label', value || '布局容器');
                }
            },
            
            width: {
                caption: '布局宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '布局高度',
                $spaceunit: 1,
                ini: '100%'
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
            
            // ===== 布局特有属性 =====
            direction: {
                caption: '布局方向',
                ini: 'column',
                listbox: ['row', 'column'],
                action: function(value) {
                    this.boxing().setDirection(value);
                }
            },
            
            justifyContent: {
                caption: '主轴对齐',
                ini: 'flex-start',
                listbox: ['flex-start', 'flex-end', 'center', 'space-between', 'space-around', 'space-evenly'],
                action: function(value) {
                    this.boxing().setJustifyContent(value);
                }
            },
            
            alignItems: {
                caption: '交叉轴对齐',
                ini: 'stretch',
                listbox: ['flex-start', 'flex-end', 'center', 'stretch', 'baseline'],
                action: function(value) {
                    this.boxing().setAlignItems(value);
                }
            },
            
            gap: {
                caption: '内容间距',
                ini: 'var(--mobile-spacing-md)'
            },
            
            padding: {
                caption: '内边距',
                ini: '0'
            },
            
            safeArea: {
                caption: '安全区域适配',
                ini: false
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
            data._style=props.style;
            data.gap=props.gap;
            data._directionClass = props.direction ? 'ood-mobile-layout-' + props.direction : '';
            data._safeAreaClass = props.safeArea ? 'ood-mobile-layout-safe' : '';
            
            return data;
        }
    }
});