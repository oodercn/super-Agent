/**
 * 移动端网格布局组件
 * 继承自 ood.UI（UI基类）和 ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持响应式布局、间距控制、对齐方式等
 */
ood.Class("ood.Mobile.Grid", ["ood.UI", "ood.absContainer"], {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['CONTAINER']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
          //  this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端网格特性
            this.initMobileGridFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileGridFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端网格CSS类
            profile.getRoot().addClass('ood-mobile-grid ood-mobile-component');
            
            // 初始化网格功能
            this.initGridFeatures();
        },
        
        initGridFeatures: function() {
            // 原有的网格特性初始化逻辑
            this.updateGridLayout();
        },
        
        updateGridLayout: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var container = profile.getSubNode('CONTAINER');
            
            // 设置网格布局
            container.css({
                'grid-template-columns': this.getGridColumns(props.columns),
                'gap': this.getGridGap(props.gap),
                'justify-content': props.justifyContent || 'start',
                'align-content': props.alignContent || 'start'
            });
        },
        
        getGridColumns: function(columns) {
            if (typeof columns === 'number') {
                return 'repeat(' + columns + ', 1fr)';
            } else if (typeof columns === 'string') {
                return columns;
            }
            return 'repeat(auto-fit, minmax(250px, 1fr))';
        },
        
        getGridGap: function(gap) {
            if (typeof gap === 'number') {
                return gap + 'px';
            } else if (typeof gap === 'string') {
                return gap;
            }
            return 'var(--mobile-spacing-md)';
        },
        
        addGridItem: function(content, gridArea) {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 修正：使用正确的DOM创建方法
            var item = document.createElement('div');
            item.className = 'ood-mobile-grid-item';
            
            if (gridArea) {
                item.style.gridArea = gridArea;
            }
            
            if (typeof content === 'string') {
                item.innerHTML = content;
            } else if (content && content.nodeType) {
                // 如果是DOM节点
                item.appendChild(content);
            } else if (content && content.get && content.get(0)) {
                // 如果是ood对象
                item.appendChild(content.get(0));
            }
            
            // 使用ood框架的DOM操作方法
            var containerNode = container.get(0);
            if (containerNode) {
                containerNode.appendChild(item);
            }
            
            // 返回ood包装的元素（使用ood的正确调用方式）
            return ood([item], false);
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-grid',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-grid-container'
            }
        },
        
        Appearances: {
            KEY: {
                width: '100%'
            },
            
            CONTAINER: {
                display: 'grid',
                width: '100%'
            },
            
            '.ood-mobile-grid-item': {
                'min-width': 0,
                'min-height': 0
            }
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '网格标题',
                ini: '网格布局',
                action: function(value) {
                    var profile = this;
                    // 可以添加标题显示节点的逻辑
                    profile.getRoot().attr('aria-label', value || '网格布局');
                }
            },
            
            width: {
                caption: '网格宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '网格高度',
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
            
            // ===== 网格特有属性 =====
            columns: {
                caption: '网格列数',
                ini: 'auto'
            },
            
            gap: {
                caption: '网格间距',
                ini: 'var(--mobile-spacing-md)'
            },
            
            justifyContent: {
                caption: '水平对齐',
                ini: 'start',
                listbox: ['start', 'end', 'center', 'stretch', 'space-around', 'space-between', 'space-evenly']
            },
            
            alignContent: {
                caption: '垂直对齐',
                ini: 'start',
                listbox: ['start', 'end', 'center', 'stretch', 'space-around', 'space-between', 'space-evenly']
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        }
    }
});