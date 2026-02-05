/**
 * 移动端列表组件
 * 继承自 ood.UI（UI基类）、ood.absList（列表功能）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 */
ood.Class("ood.Mobile.List", ["ood.UI", "ood.absList", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['CONTAINER', 'HEADER', 'ITEMS', 'ITEM', 'FOOTER', 'LOADING']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {
                    id: 'item1',
                    title: '列表项1',
                    subtitle: '子标题1',
                    avatar: 'ri ri-user-line',
                    status: 'active'
                },
                {
                    id: 'item2',
                    title: '列表项2',
                    subtitle: '子标题2',
                    avatar: 'ri ri-user-2-line',
                    status: 'inactive'
                },
                {
                    id: 'item3',
                    title: '列表项3',
                    subtitle: '子标题3',
                    avatar: 'ri ri-user-3-line',
                    status: 'pending'
                },
                {
                    id: 'item4',
                    title: '列表项4',
                    subtitle: '子标题4',
                    avatar: 'ri ri-user-4-line',
                    status: 'active',
                    badge: 5
                },
                {
                    id: 'item5',
                    title: '列表项5',
                    subtitle: '子标题5',
                    avatar: 'ri ri-user-5-line',
                    status: 'disabled'
                }
            ],
            theme: 'light',
            responsive: true,
            showAvatar: true,
            showBadge: true
        },

        Initialize: function() {
            // 调用父类初始化
             // 初始化移动端列表特性
            this.initMobileListFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileListFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端列表CSS类
            profile.getRoot().addClass('ood-mobile-list ood-mobile-component');
            
            // 初始化列表功能
            this.initListFeatures();
            
            // 初始化触摸事件
            this.bindTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // ood.absValue 必需方法 - 控制值管理
        _setCtrlValue: function(value) {
            this.each(function(profile) {
                // 设置列表的选中值
                if (value) {
                    var items = profile.getSubNode('ITEMS').find('.ood-mobile-list-item');
                    items.removeClass('ood-mobile-list-item-selected');
                    
                    var targetIndex = parseInt(value);
                    if (!isNaN(targetIndex) && targetIndex >= 0 && targetIndex < items.length) {
                        ood(items[targetIndex]).addClass('ood-mobile-list-item-selected');
                    }
                }
            });
        },
        
        _getCtrlValue: function() {
            var profile = this.get(0);
            if (!profile) return null;
            
            var selectedItem = profile.getSubNode('ITEMS').find('.ood-mobile-list-item-selected');
            if (selectedItem.length > 0) {
                return selectedItem.attr('data-index');
            }
            return null;
        },
        
        activate: function() {
            return this.each(function(profile) {
                // 激活列表（聚焦到第一个项）
                var firstItem = profile.getSubNode('ITEMS').find('.ood-mobile-list-item').eq(0);
                if (firstItem.length > 0) {
                    firstItem.focus();
                }
            });
        },
        
        resetValue: function(value) {
            // 重写基类的 resetValue 方法
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
            });
            return this;
        },

        // 表单字段支持
        _isFormField: function() {
            var profile = this.get(0);
            return profile && profile.properties.isFormField;
        },
        
        // 主题支持方法
        setTheme: function(theme) {
            return this.each(function(profile) {
                profile.properties.theme = theme || 'light';
                var root = profile.getRoot();
                
                // 清除现有主题类
                root.removeClass('ood-mobile-list-dark ood-mobile-list-hc');
                
                // 应用新主题类
                switch (theme) {
                    case 'dark':
                        root.addClass('ood-mobile-list-dark');
                        break;
                    case 'light-hc':
                    case 'dark-hc':
                        root.addClass('ood-mobile-list-hc');
                        break;
                }
                
                // 持久化主题偏好
                try {
                    if (typeof localStorage !== 'undefined') {
                        localStorage.setItem('mobile-list-theme', theme);
                    }
                } catch (e) {
                    console.warn('主题偏好保存失败:', e);
                }
            });
        },
        
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme || 'light' : 'light';
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var width = ood(document.body).cssSize().width;
                // 移除所有响应式类
                root.removeClass('ood-mobile-list-sm ood-mobile-list-xs');
                // 应用响应式类
                if (width < 480) {
                    root.addClass('ood-mobile-list-xs');
                } else if (width < 768) {
                    root.addClass('ood-mobile-list-sm');
                }
            });
        },
        
        initListFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-list');
          //  this.initData();
            this.initPullRefresh();
            
            // 初始化主题
            if (profile.properties.theme) {
                this.setTheme(profile.properties.theme);
            }
            
            // 初始化响应式布局
            if (profile.properties.responsive !== false) {
                this.adjustLayout();
            }
        },
        
        // 绑定触摸事件 - 符合 ood 事件规范
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            
            // 注册 ood 事件处理器
            this.registerEventHandlers();
        },
        
        // 注册 ood 事件处理器
        registerEventHandlers: function() {
            var self = this;
            var profile = this.get(0);
            
            // 列表项点击事件 - 符合 ood 三阶段事件机制
            profile.beforeClick = function(profile, e, src) {
                var target = e.target || e.srcElement;
                var item = ood(target).closest('.ood-mobile-list-item');
                
                if (item.length === 0) return false;
                if (item.hasClass('ood-mobile-list-item-disabled')) return false;
                
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                var target = e.target || e.srcElement;
                var item = ood(target).closest('.ood-mobile-list-item');
                
                if (item.length > 0) {
                    var index = parseInt(item.attr('data-index'));
                    var data = self.getItemData(index);
                    self.handleItemClick(profile, index, data, e);
                }
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理
                self.afterItemClick(profile, e);
            };
        },

        initPullRefresh: function() {
            var profile = this.get(0);
            if (!profile || !profile.properties.pullRefresh) return;
            
            this._pullRefresh = {
                enabled: true,
                threshold: 80,
                isRefreshing: false
            };
        },

        getItemData: function(index) {
            return this._data[index];
        },

        // 列表项点击事件处理 - 符合 ood 规范
        handleItemClick: function(profile, index, data, event) {
            // 触发用户自定义事件
            if (profile.properties.onItemClick && typeof profile.properties.onItemClick === 'function') {
                profile.properties.onItemClick.call(this, profile, index, data, event);
            }
        },
        
        // 点击后处理
        afterItemClick: function(profile, event) {
            if (profile.properties.onAfterItemClick && typeof profile.properties.onAfterItemClick === 'function') {
                profile.properties.onAfterItemClick.call(this, profile, event);
            }
        }
    },
    
    Static: {
        _DIRTYKEY: 'ITEM',
        Templates: {
            tagName: 'div',
            className: '{_className} ood-mobile-list',
            style: '{_style}',
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign}',
                text: '{labelCaption}'
            },
            ITEMS: {
                $order: 10,
                tagName: 'div',
                className: 'ood-mobile-list-items ood-uibase',
                style: '{_itemsStyle}',
                text: "{items}"
            },
            $submap: {
                items: {
                    ITEM: {

                        className: 'ood-mobile-list-item {itemClass} {disabled} {readonly} {_itemState}',
                        style: '{itemStyle}{_itemDisplay}',
                        tabindex: '{_tabindex}',
                        'data-index': '{_itemIndex}',
                        'data-id': '{id}',

                        ICON: {
                            $order: 5,
                            className: 'ood-mobile-list-item-icon {imageClass}',
                            style: '{iconStyle}{imageDisplay}',
                            text: '{iconFontCode}'
                        },
                        ITEMC: {
                            $order: 10,
                            className: 'ood-mobile-list-item-content',
                            TITLE: {
                                $order: 1,
                                className: 'ood-mobile-list-item-title',
                                text: '{title}'
                            },
                            SUBTITLE: {
                                $order: 2,
                                className: 'ood-mobile-list-item-subtitle',
                                style: '{_subtitleDisplay}',
                                text: '{subtitle}'
                            }
                        },
                        RIGHT: {
                            $order: 20,
                            className: 'ood-mobile-list-item-right',
                            style: '{_rightDisplay}',
                            text: '{rightText}'
                        },
                        BADGE: {
                            $order: 30,
                            className: 'ood-mobile-list-item-badge',
                            style: '{_badgeDisplay}',
                            text: '{badge}'
                        }
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: 'auto',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            LABEL: {
                'z-index': 1,
                top: 0,
                left: 0,
                display: 'flex',
                position: 'absolute',
                'padding-top': '.333em',
                'color': 'var(--mobile-text-heading)'
            },
            
            ITEMS: {
                position: 'relative',
                width: '100%',
                'overflow-y': 'auto',
                '-webkit-overflow-scrolling': 'touch',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            ITEM: {
                position: 'relative',
                display: 'flex',
                'align-items': 'center',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                'background-color': 'var(--mobile-bg-primary)',
                cursor: 'pointer',
                transition: 'all 0.2s ease-in-out',
                'min-height': 'var(--mobile-touch-target)',
                'color': 'var(--mobile-text-primary)'
            },
            
            'ITEM:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            'ITEM-selected': {
                'background-color': 'var(--mobile-primary-light)',
                'border-left': '4px solid var(--mobile-primary)',
                color: 'var(--mobile-primary-dark)'
            },
            
            'ITEM-disabled': {
                opacity: '0.5',
                cursor: 'not-allowed'
            },
            
            'KEY-readonly ITEM': {
                cursor: 'default'
            },
            
            ICON: {
                $order: 1,
                'margin-right': 'var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-lg)',
                color: 'var(--mobile-primary)'
            },
            
            ITEMC: {
                flex: 1,
                'min-width': 0
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4'
            },
            
            SUBTITLE: {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'line-height': '1.4',
                'margin-top': '2px'
            },
            
            RIGHT: {
                'margin-left': 'var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)'
            },
            
            BADGE: {
                'margin-left': 'var(--mobile-spacing-xs)',
                'background-color': 'var(--mobile-danger)',
                color: 'white',
                'border-radius': '50%',
                'min-width': '1.5em',
                'min-height': '1.5em',
                'font-size': 'var(--mobile-font-xs)',
                'text-align': 'center',
                'line-height': '1.5em'
            },
            
            // 主题支持
            'KEY-dark': {
                'background-color': 'var(--mobile-bg-dark)',
                color: 'var(--mobile-text-dark)'
            },
            
            'KEY-dark ITEM': {
                'background-color': 'var(--mobile-bg-dark)',
                'border-bottom-color': 'var(--mobile-border-dark)',
                color: 'var(--mobile-text-dark)'
            },
            
            'KEY-dark ITEM:hover': {
                'background-color': 'var(--mobile-bg-dark-hover)'
            },
            
            'KEY-hc': {
                'background-color': 'var(--mobile-hc-bg)',
                'border': '2px solid var(--mobile-hc-border)',
                color: 'var(--mobile-hc-text)'
            },
            
            'KEY-hc ITEM': {
                'background-color': 'var(--mobile-hc-bg)',
                'border-bottom': '2px solid var(--mobile-hc-border)',
                color: 'var(--mobile-hc-text)',
                'font-weight': 'bold'
            },
            
            'KEY-hc ITEM-selected': {
                'background-color': 'var(--mobile-hc-primary)',
                color: 'var(--mobile-hc-bg)',
                'border': '2px solid var(--mobile-hc-border)'
            },
            
            // 响应式支持
            'KEY-sm ITEM': {
                padding: 'var(--mobile-spacing-sm)',
                'font-size': '0.9em'
            },
            
            'KEY-xs ITEM': {
                padding: 'var(--mobile-spacing-xs)',
                'font-size': '0.85em',
                'min-height': '2.5em'
            },
            
            'KEY-xs RIGHT': {
                display: 'none'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            // 列表标题（显示值）
            caption: {
                caption: '列表标题',
                ini: '列表',
                action: function(value) {
                    var profile = this;
                    var titleNode = profile.getSubNode('TITLE');
                    if (titleNode && !titleNode.isEmpty()) {
                        titleNode.html(value || '');
                    }
                }
            },
            
            // 列表宽度
            width: {
                caption: '列表宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // 列表高度
            height: {
                caption: '列表高度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 列表背景色（颜色选择器）
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
            
            // 列表项文字色（颜色选择器）
            textColor: {
                caption: '文字颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var items = this.getSubNode('ITEM', true);
                        if (items && !items.isEmpty()) {
                            items.css('color', value);
                        }
                    }
                }
            },
            
            // 列表项图标（图标选择器）
            itemIcon: {
                caption: '列表项图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var iconNodes = profile.getSubNode('ICON', true);
                    
                    if (value && iconNodes && !iconNodes.isEmpty()) {
                        iconNodes.each(function(icon) {
                            var iconNode = ood(icon);
                            iconNode.attr('class', 'ood-mobile-list-item-icon ' + value);
                            iconNode.css('display', 'inline-block');
                        });
                    } else if (iconNodes && !iconNodes.isEmpty()) {
                        iconNodes.css('display', 'none');
                    }
                }
            },
            
            // ===== 继承 ood.UI 的主题和可访问性属性 =====
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
            
            // ood.absValue 必需属性
            value: {
                caption: '选中值',
                ini: null,
                action: function(value) {
                    this.boxing()._setCtrlValue(value);
                }
            },
            
            isFormField: {
                caption: '表单字段',
                ini: false
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: false
            },
            
            showDirtyMark: {
                caption: '显示脏标记',
                ini: true
            },
            
            readonly: {
                caption: '只读',
                ini: false,
                action: function(value) {
                    this.getRoot().toggleClass('ood-mobile-list-readonly', value);
                }
            },
            
            // ood.absList 必需属性  
            selMode: {
                caption: '选择模式',
                ini: 'single',
                listbox: ['single', 'multi', 'none']
            },
            
            valueSeparator: {
                caption: '值分隔符',
                ini: ','
            },
            
            // 列表特定属性
            data: {
                caption: '列表数据',
                ini: [],
                action: function(value) {
                    this.boxing().setData(value);
                }
            },
            
            pullRefresh: {
                caption: '下拉刷新',
                ini: false
            },
            
            height: {
                caption: '列表高度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // ood 规范事件处理器
            onItemClick: {
                caption: '列表项点击事件处理器',
                ini: null
            },
            
            onAfterItemClick: {
                caption: '列表项点击后事件处理器',
                ini: null
            },
            
            // ood.absValue 事件处理器
            onChanged: {
                caption: '值改变事件处理器',
                ini: null
            },
            
            onChecked: {
                caption: '选中事件处理器', 
                ini: null
            },
            
            // ood.absList 事件处理器
            onItemSelected: {
                caption: '列表项选中事件处理器',
                ini: null
            }
        },

        // ===== GET/SET 方法 - 便于编辑器读写属性 =====
        // 基础属性的getter/setter
        getCaption: function() {
            var profile = this.get(0);
            return profile ? profile.properties.caption : '列表';
        },
        
        setCaption: function(value) {
            return this.each(function(profile) {
                profile.properties.caption = value || '列表';
                var titleNode = profile.getSubNode('TITLE');
                if (titleNode && !titleNode.isEmpty()) {
                    titleNode.html(value || '');
                }
            });
        },
        
        getWidth: function() {
            var profile = this.get(0);
            return profile ? profile.properties.width : '100%';
        },
        
        setWidth: function(value) {
            return this.each(function(profile) {
                profile.properties.width = value || '100%';
                if (profile.getRoot) {
                    profile.getRoot().css('width', value || '100%');
                }
            });
        },
        
        getHeight: function() {
            var profile = this.get(0);
            return profile ? profile.properties.height : '100%';
        },
        
        setHeight: function(value) {
            return this.each(function(profile) {
                profile.properties.height = value || '100%';
                if (profile.getRoot) {
                    profile.getRoot().css('height', value || '100%');
                }
            });
        },
        
        // 颜色属性的getter/setter
        getBackgroundColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.backgroundColor : '';
        },
        
        setBackgroundColor: function(value) {
            return this.each(function(profile) {
                profile.properties.backgroundColor = value || '';
                if (value && profile.getRoot) {
                    profile.getRoot().css('background-color', value);
                }
            });
        },
        
        getTextColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.textColor : '';
        },
        
        setTextColor: function(value) {
            return this.each(function(profile) {
                profile.properties.textColor = value || '';
                if (value) {
                    var items = profile.getSubNode('ITEM', true);
                    if (items && !items.isEmpty()) {
                        items.css('color', value);
                    }
                }
            });
        },
        
        getItemIcon: function() {
            var profile = this.get(0);
            return profile ? profile.properties.itemIcon : '';
        },
        
        setItemIcon: function(value) {
            return this.each(function(profile) {
                profile.properties.itemIcon = value || '';
                var iconNodes = profile.getSubNode('ICON', true);
                
                if (value && iconNodes && !iconNodes.isEmpty()) {
                    iconNodes.each(function(icon) {
                        var iconNode = ood(icon);
                        iconNode.attr('class', 'ood-mobile-list-item-icon ' + value);
                        iconNode.css('display', 'inline-block');
                    });
                } else if (iconNodes && !iconNodes.isEmpty()) {
                    iconNodes.css('display', 'none');
                }
            });
        },
        
        // 主题和功能属性的getter/setter
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme : 'light';
        },
        
        setTheme: function(value) {
            return this.each(function(profile) {
                profile.properties.theme = value || 'light';
                profile.boxing().setTheme(value || 'light');
            });
        },
        
        getResponsive: function() {
            var profile = this.get(0);
            return profile ? profile.properties.responsive : true;
        },
        
        setResponsive: function(value) {
            return this.each(function(profile) {
                profile.properties.responsive = value !== false;
                if (value) {
                    profile.boxing().adjustLayout();
                }
            });
        },
        
        // 值管理属性的getter/setter (继承自absValue)
        getValue: function() {
            var profile = this.get(0);
            return profile ? profile.properties.value : null;
        },
        
        setValue: function(value) {
            return this.each(function(profile) {
                profile.properties.value = value;
                profile.boxing()._setCtrlValue(value);
            });
        },
        
        getIsFormField: function() {
            var profile = this.get(0);
            return profile ? profile.properties.isFormField : false;
        },
        
        setIsFormField: function(value) {
            return this.each(function(profile) {
                profile.properties.isFormField = !!value;
            });
        },
        
        getDirtyMark: function() {
            var profile = this.get(0);
            return profile ? profile.properties.dirtyMark : false;
        },
        
        setDirtyMark: function(value) {
            return this.each(function(profile) {
                profile.properties.dirtyMark = !!value;
            });
        },
        
        getShowDirtyMark: function() {
            var profile = this.get(0);
            return profile ? profile.properties.showDirtyMark : true;
        },
        
        setShowDirtyMark: function(value) {
            return this.each(function(profile) {
                profile.properties.showDirtyMark = value !== false;
            });
        },
        
        getReadonly: function() {
            var profile = this.get(0);
            return profile ? profile.properties.readonly : false;
        },
        
        setReadonly: function(value) {
            return this.each(function(profile) {
                profile.properties.readonly = !!value;
                profile.getRoot().toggleClass('ood-mobile-list-readonly', !!value);
            });
        },
        
        // 列表管理属性的getter/setter (继承自absList)
        getSelMode: function() {
            var profile = this.get(0);
            return profile ? profile.properties.selMode : 'single';
        },
        
        setSelMode: function(value) {
            return this.each(function(profile) {
                profile.properties.selMode = value || 'single';
            });
        },
        
        getValueSeparator: function() {
            var profile = this.get(0);
            return profile ? profile.properties.valueSeparator : ',';
        },
        
        setValueSeparator: function(value) {
            return this.each(function(profile) {
                profile.properties.valueSeparator = value || ',';
            });
        },

        getPullRefresh: function() {
            var profile = this.get(0);
            return profile ? profile.properties.pullRefresh : false;
        },
        
        setPullRefresh: function(value) {
            return this.each(function(profile) {
                profile.properties.pullRefresh = !!value;
            });
        },
        
        // 事件处理器的getter/setter
        getOnItemClick: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onItemClick : null;
        },
        
        setOnItemClick: function(value) {
            return this.each(function(profile) {
                profile.properties.onItemClick = value;
            });
        },
        
        getOnAfterItemClick: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onAfterItemClick : null;
        },
        
        setOnAfterItemClick: function(value) {
            return this.each(function(profile) {
                profile.properties.onAfterItemClick = value;
            });
        },
        
        getOnChanged: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onChanged : null;
        },
        
        setOnChanged: function(value) {
            return this.each(function(profile) {
                profile.properties.onChanged = value;
            });
        },
        
        getOnChecked: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onChecked : null;
        },
        
        setOnChecked: function(value) {
            return this.each(function(profile) {
                profile.properties.onChecked = value;
            });
        },
        
        getOnItemSelected: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onItemSelected : null;
        },
        
        setOnItemSelected: function(value) {
            return this.each(function(profile) {
                profile.properties.onItemSelected = value;
            });
        },
        
        // 数据准备方法（模板渲染前调用）
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 处理根级别样式类
            var classes = ['ood-mobile-list', 'ood-mobile-component'];
            if (props.theme === 'dark') classes.push('ood-mobile-list-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classes.push('ood-mobile-list-hc');
            if (props.readonly) classes.push('ood-mobile-list-readonly');
            
            data._styleClasses = classes.join(' ');
            data._itemsStyle = this._buildItemsStyle(props);
            
            // 设置响应式类
            var screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            var isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            data._screenSize = screenSize;
            data._isMobile = isMobile;
            
            // 合并className
            var classNames = ['ood-mobile-list', 'ood-mobile-component'];
            if (props.theme === 'dark') classNames.push('ood-mobile-list-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classNames.push('ood-mobile-list-hc');
            if (props.readonly) classNames.push('ood-mobile-list-readonly');
            if (isMobile) classNames.push('mobile-device');
            classNames.push('list-' + screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            // 设置样式
            data._style = props.style || '';
            
            // 设置标签显示控制
            data._labelShow = props.label ? 'block' : 'none';
            
            // 设置标签尺寸
            data._labelSize = props.labelSize || 'auto';
            
            // 设置标签对齐方式
            data._labelHAlign = props.labelHAlign || '';
            data._labelVAlign = props.labelVAlign || '';
            
            // 设置必填标记
            data._required = props.required ? 'required' : '';
            
            // 设置标签标题
            data.labelCaption = props.labelCaption || props.label || '';
            return data;
        },
        
        // 构建容器样式
        _buildItemsStyle: function(props) {
            var styles = [];
            if (props.backgroundColor) {
                styles.push('background-color: ' + props.backgroundColor);
            }
            if (props.height && props.height !== '100%') {
                styles.push('height: ' + props.height);
            }
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 准备单个列表项数据 - 处理所有模板变量
        _prepareItem: function(profile, dataItem, item, pid, i, l, mapCache, serialId) {
            // 调用父类方法
            if (arguments.callee.upper) {
                arguments.callee.upper.call(this, profile, dataItem, item, pid, i, l, mapCache, serialId);
            }
            
            var props = profile.properties;
            var isSelected = item.selected || false;
            var isDisabled = item.disabled || false;
            var isReadonly = item.readonly || props.readonly || false;
            
            // 基础数据属性
            dataItem.id = item.id || 'list-item-' + i;
            dataItem.title = item.title || item.text || item.caption || '';
            dataItem.subtitle = item.subtitle || item.description || '';
            dataItem.rightText = item.rightText || item.value || '';
            dataItem.badge = item.badge;
            dataItem.disabled = isDisabled ? 'disabled' : '';
            dataItem.readonly = isReadonly ? 'readonly' : '';
            
            // 索引和状态
            dataItem._itemIndex = i;
            dataItem._tabindex = isDisabled ? '-1' : '0';
            
            // 状态类和样式
            dataItem._itemState = this._buildItemStateClass(isSelected, isDisabled, isReadonly);
            dataItem._itemDisplay = 'display: flex;';
            
            // 图标相关变量
            dataItem.imageClass = item.imageClass || item.icon || '';
            dataItem.iconFontCode = this._getIconFontCode(item.icon || item.imageClass);
            dataItem.iconStyle = this._buildIconStyle(item, props);
            dataItem.imageDisplay = (item.imageClass || item.icon) ? 'display: inline-block;' : 'display: none;';
            
            // 显示控制变量
            dataItem._subtitleDisplay = dataItem.subtitle ? 'display: block;' : 'display: none;';
            dataItem._rightDisplay = dataItem.rightText ? 'display: block;' : 'display: none;';
            dataItem._badgeDisplay = (item.badge !== null && item.badge !== undefined) ? 'display: inline-block;' : 'display: none;';
            
            // 自定义样式
            dataItem.itemClass = item.itemClass || item.className || '';
            dataItem.itemStyle = this._buildItemStyle(item, isSelected, props);
            
            return dataItem;
        },
        
        // 构建项目状态类
        _buildItemStateClass: function(isSelected, isDisabled, isReadonly) {
            var classes = [];
            if (isSelected) classes.push('selected');
            if (isDisabled) classes.push('disabled');
            if (isReadonly) classes.push('readonly');
            return classes.join(' ');
        },
        
        // 获取图标字体代码
        _getIconFontCode: function(icon) {
            if (!icon) return '';
            // 处理不同的图标字体格式
            if (icon.startsWith('ri-')) {
                return ''; // Remix Icon 使用CSS类
            } else if (icon.startsWith('fa-')) {
                return ''; // Font Awesome 使用CSS类
            } else if (/^\\[a-fA-F0-9]{4}$/.test(icon)) {
                return icon; // Unicode 字符
            }
            return '';
        },
        
        // 构建图标样式
        _buildIconStyle: function(item, props) {
            var styles = [];
            
            // 基础图标样式
            if (item.iconStyle) {
                styles.push(item.iconStyle);
            }
            
            // 主题颜色
            if (props.textColor) {
                styles.push('color: ' + props.textColor);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 构建项目样式
        _buildItemStyle: function(item, isSelected, props) {
            var styles = [];
            
            // 基础项目样式
            if (item.itemStyle) {
                styles.push(item.itemStyle);
            }
            
            // 选中状态背景
            if (isSelected && props.selectedBackgroundColor) {
                styles.push('background-color: ' + props.selectedBackgroundColor);
            }
            
            // 自定义宽度
            if (item.width) {
                styles.push('width: ' + item.width);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // ood.absValue 静态方法
        _isFormField: function() {
            return true; // 列表组件可以作为表单字段
        },
        
        _ensureValue: function(value) {
            // 确保值的类型正确
            if (value === null || value === undefined) return null;
            if (typeof value === 'string' || typeof value === 'number') {
                return value.toString();
            }
            return null;
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                // 使用 ood 规范的初始化
                profile.boxing().initListFeatures();
                
                // 设置默认值
                var value = profile.properties.value;
                if (value !== null && value !== undefined) {
                    profile.boxing()._setCtrlValue(value);
                }
                
                // 触发准备就绪事件
                if (profile.onReady) {
                    profile.boxing().onReady(profile);
                }
            });
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // List组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                containerNode = profile.getSubNode('CONTAINER'),
                itemsNode = profile.getSubNode('ITEMS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整列表容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    containerNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整列表容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    containerNode.css('height', '100%');
                    
                    // 调整列表项容器的高度以适应容器
                    if (itemsNode && !itemsNode.isEmpty()) {
                        // 计算可用高度（减去头部和底部的高度）
                        var headerHeight = profile.getSubNode('HEADER').offsetHeight(true) || 0;
                        var footerHeight = profile.getSubNode('FOOTER').offsetHeight(true) || 0;
                        var availableHeight = pxHeight - headerHeight - footerHeight;
                        
                        itemsNode.css('height', adjustunit(availableHeight));
                    }
                }
            }

            // 调整内部布局以适应新尺寸
            this.boxing().adjustLayout();
        },

        EventHandlers: {
            // ood.UI 生命周期事件处理器
            onReady: function(profile) {
                // 组件就绪时的初始化
                profile.boxing().initListFeatures();
            },
            
            onCreated: function(profile) {
                // 组件创建完成时的处理
            },
            
            onDestroy: function(profile) {
                // 组件销毁时的清理
            },
            
            // ood.absValue 事件处理器
            onChanged: function(profile, e, src, value) {
                // 值改变事件
            },
            
            onChecked: function(profile, e, src, value) {
                // 选中事件
            },
            
            onValueSet: function(profile, oldValue, newValue) {
                // 值设置事件
            },
            
            // ood.absList 事件处理器
            onItemSelected: function(profile, item, e, src, type) {
                // 列表项选中事件
            },
            
            onItemAdded: function(profile, items, index) {
                // 列表项添加事件
            },
            
            onItemRemoved: function(profile, items, indices) {
                // 列表项删除事件
            },
            
            // 列表特定事件处理器
            onItemClick: function(profile, index, data, event) {
                // 列表项点击事件处理器
            },
            
            onAfterItemClick: function(profile, event) {
                // 列表项点击后事件处理器
            }
        }
    }
});