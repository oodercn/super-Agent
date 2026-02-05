/**
 * 移动端标签栏组件
 * 继承自 ood.UI（UI基类）、ood.absList（列表功能）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持多标签切换、徽章显示、自定义图标等功能
 */
ood.Class("ood.Mobile.TabBar", ["ood.UI", "ood.absList", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件
        this.addTemplateKeys(['CONTAINER', 'ITEM', 'ICON', 'TEXT', 'BADGE']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
        //   this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端标签栏特性
            this.initMobileTabBarFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
            }
        },
        
        initMobileTabBarFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端标签栏CSS类
            profile.getRoot().addClass('ood-mobile-tabbar ood-mobile-component');
            
            // 初始化标签栏功能
            this.initTabBarFeatures();
            
            // 绑定事件
            this.bindTouchEvents();
        },
        
        // ood.absValue 必需方法 - 控制值管理
        _setCtrlValue: function(value) {
            // TabBar 的值为当前激活的标签索引
            var index = parseInt(value);
            if (!isNaN(index)) {
                this.setActiveTab(index);
            }
        },
        
        _getCtrlValue: function() {
            // 返回当前激活的标签索引
            return this._activeIndex;
        },
        
        activate: function() {
            return this.each(function(profile) {
                // 激活标签栏（聚焦到当前激活的标签）
                var activeItem = profile.getSubNode('CONTAINER').find('.ood-mobile-tabbar-item-active');
                if (activeItem.length > 0) {
                    activeItem.focus();
                }
            });
        },
        
        resetValue: function(value) {
            // 重写基类的 resetValue 方法
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
                profile.properties.activeIndex = value;
            });
            return this;
        },
        
        // ood.absList 必需方法 - 列表项管理
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentTabs = self.getTabs();
                if (typeof index === 'undefined') {
                    // 添加到末尾
                    currentTabs = currentTabs.concat(items);
                } else {
                    // 插入到指定位置
                    var insertIndex = isBefore ? index : index + 1;
                    currentTabs.splice.apply(currentTabs, [insertIndex, 0].concat(items));
                }
                
                self.setTabs(currentTabs);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentTabs = self.getTabs();
                // 从后往前删除，保证索引不变
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var index = parseInt(indices[i]);
                    if (index >= 0 && index < currentTabs.length) {
                        currentTabs.splice(index, 1);
                    }
                }
                
                self.setTabs(currentTabs);
            });
        },
        
        clearItems: function() {
            return this.setTabs([]);
        },
        
        getItems: function() {
            return this.getTabs();
        },
        
        getSelectedItems: function() {
            var activeIndex = this.getActiveTab();
            var tabs = this.getTabs();
            if (activeIndex >= 0 && activeIndex < tabs.length) {
                return [tabs[activeIndex]];
            }
            return [];
        },
        
        selectItem: function(index) {
            return this.setActiveTab(index);
        },
        
        unselectItem: function(index) {
            // TabBar 不支持取消选中，因为总是要有一个激活的标签
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
                root.removeClass('ood-mobile-tabbar-dark ood-mobile-tabbar-hc');
                
                // 应用新主题类
                switch (theme) {
                    case 'dark':
                        root.addClass('ood-mobile-tabbar-dark');
                        break;
                    case 'light-hc':
                    case 'dark-hc':
                        root.addClass('ood-mobile-tabbar-hc');
                        break;
                }
            });
        },
        
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme || 'light' : 'light';
        },
        
        initTabBarFeatures: function() {
            // 原有的标签栏特性初始化逻辑
            this.initTabs();
            this.applySafeArea();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 标签点击事件
            container.on('click', '.ood-mobile-tabbar-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                
                if (!item.hasClass('ood-mobile-tabbar-item-disabled')) {
                    self.setActiveTab(index);
                }
            });
        },
        
        initTabs: function() {
            this._activeIndex = 0;
            
            var profile = this.get(0);
            if (profile.properties.tabs) {
                this.setTabs(profile.properties.tabs);
            }
            
            if (typeof profile.properties.activeIndex === 'number') {
                this.setActiveTab(profile.properties.activeIndex);
            }
        },
        
        applySafeArea: function() {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.safeArea) {
                profile.getRoot().addClass('ood-mobile-tabbar-safe');
            }
        },
        
        setTabs: function(tabs) {
            this._tabs = tabs || [];
            this.renderTabs();
        },
        
        getTabs: function() {
            return this._tabs;
        },
        
        renderTabs: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            for (var i = 0; i < this._tabs.length; i++) {
                var tab = this._tabs[i];
                var tabElement = this.createTabElement(tab, i);
                container.append(tabElement);
            }
            
            // 设置激活状态
            this.updateActiveTab();
        },
        
        createTabElement: function(tab, index) {
            var tabEl = ood('<div class="ood-mobile-tabbar-item" data-index="' + index + '"></div>');
            
            // 图标
            if (tab.icon) {
                var icon = ood('<i class="ood-mobile-tabbar-icon ' + tab.icon + '"></i>');
                tabEl.append(icon);
            }
            
            // 文本
            if (tab.text) {
                var text = ood('<span class="ood-mobile-tabbar-text">' + tab.text + '</span>');
                tabEl.append(text);
            }
            
            // 徽章
            if (tab.badge) {
                var badge = this.createBadgeElement(tab.badge);
                tabEl.append(badge);
            }
            
            // 禁用状态
            if (tab.disabled) {
                tabEl.addClass('ood-mobile-tabbar-item-disabled');
            }
            
            return tabEl;
        },
        
        createBadgeElement: function(badge) {
            var badgeEl = ood('<div class="ood-mobile-tabbar-badge"></div>');
            
            if (typeof badge === 'number') {
                if (badge > 99) {
                    badgeEl.html('99+');
                } else if (badge > 0) {
                    badgeEl.html(badge.toString());
                } else {
                    badgeEl.addClass('ood-mobile-tabbar-badge-dot');
                }
            } else if (typeof badge === 'string') {
                badgeEl.html(badge);
            } else {
                badgeEl.addClass('ood-mobile-tabbar-badge-dot');
            }
            
            return badgeEl;
        },
        
        setActiveTab: function(index) {
            if (index < 0 || index >= this._tabs.length) return;
            
            var oldIndex = this._activeIndex;
            this._activeIndex = index;
            
            this.updateActiveTab();
            
            // 触发变化事件
            if (oldIndex !== index) {
                this.onTabChange(index, this._tabs[index]);
            }
        },
        
        getActiveTab: function() {
            return this._activeIndex;
        },
        
        updateActiveTab: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var items = container.find('.ood-mobile-tabbar-item');
            
            var self = this;
            items.each(function(el) {
                var item = ood(el);
                var index = parseInt(item.attr('data-index'));
                
                if (index === self._activeIndex) {
                    item.addClass('ood-mobile-tabbar-item-active');
                } else {
                    item.removeClass('ood-mobile-tabbar-item-active');
                }
            });
        },
        
        setBadge: function(index, badge) {
            if (index < 0 || index >= this._tabs.length) return;
            
            this._tabs[index].badge = badge;
            
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var item = container.find('.ood-mobile-tabbar-item[data-index="' + index + '"]');
            
            // 移除现有徽章
            item.find('.ood-mobile-tabbar-badge').remove();
            
            // 添加新徽章
            if (badge !== null && badge !== undefined) {
                var badgeEl = this.createBadgeElement(badge);
                item.append(badgeEl);
            }
        },
        
        removeBadge: function(index) {
            this.setBadge(index, null);
        },
        
        onTabChange: function(index, tab) {
            var profile = this.get(0);
            
            if (profile.onTabChange) {
                profile.boxing().onTabChange(profile, index, tab);
            }
        },
        
        // 新增方法：刷新所有项目渲染
        refreshItems: function() {
            return this.each(function(profile) {
                // 重新渲染项目
                profile.boxing().setItems(profile.properties.items || []);
            });
        },
        
        // 新增方法：设置标签数据（统一使用setItems）
        setItems: function(items) {
            return this.each(function(profile) {
                profile.properties.items = items || [];
                profile.boxing().setTabs(items);
            });
        },
        
        // 新增方法：获取标签数据（统一使用getItems）
        getItems: function() {
            var profile = this.get(0);
            return profile ? (profile.properties.items || []) : [];
        },
        
        // 继续保持原有方法以保证向后兼容
        setTabs: function(tabs) {
            this._tabs = tabs || [];
            this.renderTabs();
        },
        
        getTabs: function() {
            return this.getItems(); // 委托给getItems
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: '{_className} ood-mobile-tabbar {_styleClasses}',
            style: '{_style}',
            ITEMS: {
                $order: 10,
                tagName: 'div',
                className: 'ood-mobile-tabbar-container ood-uibase',
                style: '{_itemsStyle}',
                text: "{items}"
            },
            $submap: {
                items: {
                    ITEM: {
                        tagName: 'div',
                        className: 'ood-mobile-tabbar-item {itemClass} {disabled} {_itemState}',
                        style: '{itemStyle}{_itemDisplay}',
                        tabindex: '{_tabindex}',
                        'data-index': '{_itemIndex}',
                        'data-id': '{id}',
                        'aria-label': '{text}',

                        
                        ICON: {
                            $order: 5,
                            className: 'ood-mobile-tabbar-icon {imageClass} {iconClass}',
                            style: '{iconStyle}{_iconDisplay}',
                            text: '{iconFontCode}',
                            'data-icon': '{icon}'
                        },
                        TEXT: {
                            $order: 10,
                            className: 'ood-mobile-tabbar-text {textClass}',
                            style: '{textStyle}{_textDisplay}',
                            text: '{text}'
                        },
                        BADGE: {
                            $order: 20,
                            className: 'ood-mobile-tabbar-badge {badgeClass} {_badgeType}',
                            style: '{badgeStyle}{_badgeDisplay}',
                            text: '{badgeText}',
                            'data-badge': '{badge}'
                        }
                    }
                }
            }
        },
        
        Appearances: {
            KEY: {
               // position: 'fixed',
                bottom: 0,
                left: 0,
                right: 0,
                width: '100%',
                'background-color': 'var(--mobile-bg-primary)',
                'border-top': '1px solid var(--mobile-border-color)',
                'z-index': 100
            },
            
            'KEY-safe': {
                'padding-bottom': 'var(--mobile-safe-bottom)'
            },
            
            ITEMS: {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-around',
                height: '50px',
                'min-height': '50px'
            },
            
            ITEM: {
                position: 'relative',
                display: 'flex',
                'flex-direction': 'column',
                'align-items': 'center',
                'justify-content': 'center',
                flex: 1,
                height: '100%',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                'min-width': 0
            },
            
            'ITEM:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            'ITEM-active': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            'ITEM-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            ICON: {
                'font-size': '20px',
                color: 'var(--mobile-text-tertiary)',
                transition: 'color 0.2s ease',
                'margin-bottom': '2px'
            },
            
            'ITEM-active ICON': {
                color: 'var(--mobile-primary)'
            },
            
            TEXT: {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-tertiary)',
                transition: 'color 0.2s ease',
                'white-space': 'nowrap',
                overflow: 'hidden',
                'text-overflow': 'ellipsis',
                'max-width': '100%'
            },
            
            'ITEM-active TEXT': {
                color: 'var(--mobile-primary)'
            },
            
            BADGE: {
                position: 'absolute',
                top: '6px',
                right: '50%',
                transform: 'translateX(50%)',
                'background-color': 'var(--mobile-danger)',
                color: 'white',
                'border-radius': '10px',
                'min-width': '18px',
                height: '18px',
                'font-size': '10px',
                'line-height': '18px',
                'text-align': 'center',
                'font-weight': 'bold',
                'padding': '0 4px'
            },
            
            'BADGE-dot': {
                width: '8px',
                height: '8px',
                'border-radius': '50%',
                'min-width': 'auto',
                top: '8px'
            },
            
            // 主题支持
            'KEY-dark': {
                'background-color': 'var(--mobile-bg-dark)',
                'border-top-color': 'var(--mobile-border-dark)'
            },
            
            'KEY-dark ICON': {
                color: 'var(--mobile-text-dark-tertiary)'
            },
            
            'KEY-dark TEXT': {
                color: 'var(--mobile-text-dark-tertiary)'
            },
            
            'KEY-dark ITEM-active ICON': {
                color: 'var(--mobile-primary-light)'
            },
            
            'KEY-dark ITEM-active TEXT': {
                color: 'var(--mobile-primary-light)'
            },
            
            'KEY-hc': {
                'background-color': 'var(--mobile-hc-bg)',
                'border-top': '3px solid var(--mobile-hc-border)'
            },
            
            'KEY-hc ICON': {
                color: 'var(--mobile-hc-text)',
                'font-weight': 'bold'
            },
            
            'KEY-hc TEXT': {
                color: 'var(--mobile-hc-text)',
                'font-weight': 'bold'
            },
            
            'KEY-hc ITEM-active': {
                'background-color': 'var(--mobile-hc-primary)'
            },
            
            'KEY-hc ITEM-active ICON': {
                color: 'var(--mobile-hc-bg)'
            },
            
            'KEY-hc ITEM-active TEXT': {
                color: 'var(--mobile-hc-bg)'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: false
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '标签栏标题',
                ini: '标签栏',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '标签栏');
                }
            },
            
            width: {
                caption: '标签栏宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '标签栏高度',
                $spaceunit: 1,
                ini: '50px',
                action: function(value) {
                    this.getRoot().css('height', value);
                }
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
            
            activeColor: {
                caption: '激活标签颜色',
                ini: '#007AFF',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        // 更新激活标签的颜色
                        this.getRoot().css('--tabbar-active-color', value);
                        this.boxing().refreshItems();
                    }
                }
            },
            
            inactiveColor: {
                caption: '未激活标签颜色',
                ini: '#8E8E93',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        // 更新未激活标签的颜色
                        this.getRoot().css('--tabbar-inactive-color', value);
                        this.boxing().refreshItems();
                    }
                }
            },
            
            activeBackgroundColor: {
                caption: '激活标签背景色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            // 图标相关属性
            iconSize: {
                caption: '图标大小',
                ini: '20px',
                $spaceunit: 1,
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            // 文本相关属性
            textSize: {
                caption: '文本大小',
                ini: '12px',
                $spaceunit: 1,
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            activeFontWeight: {
                caption: '激活文本字重',
                ini: '500',
                listbox: ['normal', 'bold', '400', '500', '600', '700'],
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            // 徽章相关属性
            badgeColor: {
                caption: '徽章背景色',
                ini: '#FF3B30',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            badgeTextColor: {
                caption: '徽章文本颜色',
                ini: '#FFFFFF',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            // ===== 标签栏特有属性 =====
            // 继承 ood.UI 的主题和可访问性属性
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
                ini: true
            },
            
            hideDisabled: {
                caption: '隐藏禁用项',
                ini: false,
                action: function(value) {
                    this.boxing().refreshItems();
                }
            },
            
            // ood.absValue 必需属性
            value: {
                caption: '激活标签索引',
                ini: 0,
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
            
            readonly: {
                caption: '只读',
                ini: false,
                action: function(value) {
                    this.getRoot().toggleClass('ood-mobile-tabbar-readonly', value);
                }
            },
            
            // ood.absList 必需属性  
            selMode: {
                caption: '选择模式',
                ini: 'single',
                listbox: ['single']
            },
            
            valueSeparator: {
                caption: '值分隔符',
                ini: ','
            },
            
            // TabBar 特定属性 - 数据集合（统一命名为items）
            items: {
                caption: '标签数据',
                ini: [],
                action: function(value) {
                    this.boxing().setItems(value);
                }
            },
            
            activeIndex: {
                caption: '激活标签索引',
                ini: 0,
                action: function(value) {
                    this.boxing().setActiveTab(value);
                }
            },
            
            safeArea: {
                caption: '安全区域',
                ini: true,
                action: function(value) {
                    this.getRoot().toggleClass('ood-mobile-tabbar-safe', value);
                }
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
                caption: '标签选中事件处理器',
                ini: null
            },
            
            // TabBar 特定事件处理器
            onTabChange: {
                caption: '标签切换事件处理器',
                ini: null
            }
        },

        // ===== GET/SET 方法 - 便于编辑器读写属性 =====
        // 基础属性的getter/setter
        getCaption: function() {
            var profile = this.get(0);
            return profile ? profile.properties.caption : '';
        },
        
        setCaption: function(value) {
            return this.each(function(profile) {
                profile.properties.caption = value || '';
                profile.getRoot().attr('aria-label', value || '标签栏');
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
            return profile ? profile.properties.height : '50px';
        },
        
        setHeight: function(value) {
            return this.each(function(profile) {
                profile.properties.height = value || '50px';
                if (profile.getRoot) {
                    profile.getRoot().css('height', value || '50px');
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
        
        getActiveColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.activeColor : '#007AFF';
        },
        
        setActiveColor: function(value) {
            return this.each(function(profile) {
                profile.properties.activeColor = value || '#007AFF';
                if (profile.getRoot) {
                    profile.getRoot().css('--tabbar-active-color', value || '#007AFF');
                    profile.boxing().refreshItems();
                }
            });
        },
        
        getInactiveColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.inactiveColor : '#8E8E93';
        },
        
        setInactiveColor: function(value) {
            return this.each(function(profile) {
                profile.properties.inactiveColor = value || '#8E8E93';
                if (profile.getRoot) {
                    profile.getRoot().css('--tabbar-inactive-color', value || '#8E8E93');
                    profile.boxing().refreshItems();
                }
            });
        },
        
        getActiveBackgroundColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.activeBackgroundColor : '';
        },
        
        setActiveBackgroundColor: function(value) {
            return this.each(function(profile) {
                profile.properties.activeBackgroundColor = value || '';
                profile.boxing().refreshItems();
            });
        },
        
        // 图标和文本尺寸属性的getter/setter
        getIconSize: function() {
            var profile = this.get(0);
            return profile ? profile.properties.iconSize : '20px';
        },
        
        setIconSize: function(value) {
            return this.each(function(profile) {
                profile.properties.iconSize = value || '20px';
                profile.boxing().refreshItems();
            });
        },
        
        getTextSize: function() {
            var profile = this.get(0);
            return profile ? profile.properties.textSize : '12px';
        },
        
        setTextSize: function(value) {
            return this.each(function(profile) {
                profile.properties.textSize = value || '12px';
                profile.boxing().refreshItems();
            });
        },
        
        getActiveFontWeight: function() {
            var profile = this.get(0);
            return profile ? profile.properties.activeFontWeight : '500';
        },
        
        setActiveFontWeight: function(value) {
            return this.each(function(profile) {
                profile.properties.activeFontWeight = value || '500';
                profile.boxing().refreshItems();
            });
        },
        
        // 徽章属性的getter/setter
        getBadgeColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.badgeColor : '#FF3B30';
        },
        
        setBadgeColor: function(value) {
            return this.each(function(profile) {
                profile.properties.badgeColor = value || '#FF3B30';
                profile.boxing().refreshItems();
            });
        },
        
        getBadgeTextColor: function() {
            var profile = this.get(0);
            return profile ? profile.properties.badgeTextColor : '#FFFFFF';
        },
        
        setBadgeTextColor: function(value) {
            return this.each(function(profile) {
                profile.properties.badgeTextColor = value || '#FFFFFF';
                profile.boxing().refreshItems();
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
            });
        },
        
        getHideDisabled: function() {
            var profile = this.get(0);
            return profile ? profile.properties.hideDisabled : false;
        },
        
        setHideDisabled: function(value) {
            return this.each(function(profile) {
                profile.properties.hideDisabled = !!value;
                profile.boxing().refreshItems();
            });
        },
        
        // 值管理属性的getter/setter (继承自absValue)
        getValue: function() {
            var profile = this.get(0);
            return profile ? profile.properties.value : 0;
        },
        
        setValue: function(value) {
            return this.each(function(profile) {
                var newValue = parseInt(value) || 0;
                profile.properties.value = newValue;
                profile.boxing()._setCtrlValue(newValue);
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
        
        getReadonly: function() {
            var profile = this.get(0);
            return profile ? profile.properties.readonly : false;
        },
        
        setReadonly: function(value) {
            return this.each(function(profile) {
                profile.properties.readonly = !!value;
                profile.getRoot().toggleClass('ood-mobile-tabbar-readonly', !!value);
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
        
        // TabBar特有属性的getter/setter
        getItems: function() {
            var profile = this.get(0);
            return profile ? (profile.properties.items || []) : [];
        },
        
        setItems: function(value) {
            return this.each(function(profile) {
                profile.properties.items = value || [];
                profile.boxing().setTabs(value || []);
            });
        },
        
        getActiveIndex: function() {
            var profile = this.get(0);
            return profile ? profile.properties.activeIndex : 0;
        },
        
        setActiveIndex: function(value) {
            return this.each(function(profile) {
                var newValue = parseInt(value) || 0;
                profile.properties.activeIndex = newValue;
                profile.boxing().setActiveTab(newValue);
            });
        },
        
        getSafeArea: function() {
            var profile = this.get(0);
            return profile ? profile.properties.safeArea : true;
        },
        
        setSafeArea: function(value) {
            return this.each(function(profile) {
                profile.properties.safeArea = value !== false;
                profile.getRoot().toggleClass('ood-mobile-tabbar-safe', value !== false);
            });
        },
        
        // 事件处理器的getter/setter
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
        
        getOnTabChange: function() {
            var profile = this.get(0);
            return profile ? profile.properties.onTabChange : null;
        },
        
        setOnTabChange: function(value) {
            return this.each(function(profile) {
                profile.properties.onTabChange = value;
            });
        },
        
        // ood.absValue 静态方法
        _isFormField: function() {
            return true; // TabBar 组件可以作为表单字段
        },
        
        _ensureValue: function(value) {
            // 确保值的类型正确
            if (value === null || value === undefined) return 0;
            var index = parseInt(value);
            return isNaN(index) ? 0 : index;
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                // 使用 ood 规范的初始化
                profile.boxing().initTabBarFeatures();
                
                // 设置默认值
                var value = profile.properties.value || profile.properties.activeIndex;
                if (value !== null && value !== undefined) {
                    profile.boxing()._setCtrlValue(value);
                }
                
                // 触发准备就绪事件
                if (profile.onReady) {
                    profile.boxing().onReady(profile);
                }
            });
        },
        
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 处理根级别样式类
            var classes = ['ood-mobile-tabbar', 'ood-mobile-component'];
            if (props.safeArea) classes.push('ood-mobile-tabbar-safe');
            if (props.theme === 'dark') classes.push('ood-mobile-tabbar-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classes.push('ood-mobile-tabbar-hc');
            if (props.readonly) classes.push('ood-mobile-tabbar-readonly');
            
            // 设置响应式类
            var screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            var isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            data._screenSize = screenSize;
            data._isMobile = isMobile;
            
            // 合并className
            var classNames = ['ood-mobile-tabbar', 'ood-mobile-component'];
            if (props.safeArea) classNames.push('ood-mobile-tabbar-safe');
            if (props.theme === 'dark') classNames.push('ood-mobile-tabbar-dark');
            if (props.theme === 'light-hc' || props.theme === 'dark-hc') classNames.push('ood-mobile-tabbar-hc');
            if (props.readonly) classNames.push('ood-mobile-tabbar-readonly');
            if (isMobile) classNames.push('mobile-device');
            classNames.push('tabbar-' + screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            // 设置样式
            data._style = props.style || '';
            
            // 设置容器样式
            data._itemsStyle = this._buildItemsStyle(props);
            
            // 设置ARIA标签
            data._ariaLabel = props.ariaLabel || props.caption || '标签栏';
            
            // 设置禁用状态
            data._disabled = props.disabled ? 'disabled' : '';
            
            // 设置只读状态
            data._readonly = props.readonly ? 'readonly' : '';
            
            // 设置主题类
            data._themeClass = props.theme && props.theme !== 'light' ? 'tabbar-theme-' + props.theme : '';
            
            // 设置响应式类
            data._responsiveClass = props.responsive ? 'mobile-responsive' : '';
            
            return data;
        },
        
        // 构建容器样式
        _buildItemsStyle: function(props) {
            var styles = [];
            if (props.backgroundColor) {
                styles.push('background-color: ' + props.backgroundColor);
            }
            if (props.height && props.height !== '50px') {
                styles.push('height: ' + props.height);
            }
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 准备单个标签项数据 - 处理所有模板变量
        _prepareItem: function(profile, dataItem, item, pid, i, l, mapCache, serialId) {
            // 调用父类方法
            if (arguments.callee.upper) {
                arguments.callee.upper.call(this, profile, dataItem, item, pid, i, l, mapCache, serialId);
            }
            
            var props = profile.properties;
            var isActive = (i === (props.activeIndex || 0));
            var isDisabled = item.disabled;
            
            // 基础数据属性
            dataItem.id = item.id || 'tab-' + i;
            dataItem.text = item.text || item.caption || '';
            dataItem.icon = item.icon || item.imageClass || '';
            dataItem.badge = item.badge;
            dataItem.disabled = isDisabled ? 'disabled' : '';
            
            // 索引和状态
            dataItem._itemIndex = i;
            dataItem._tabindex = isDisabled ? '-1' : '0';
            
            // 状态类和样式
            dataItem._itemState = this._buildItemStateClass(isActive, isDisabled);
            dataItem._itemDisplay = isDisabled && props.hideDisabled ? 'display: none;' : 'display: flex;';
            
            // 图标相关变量
            dataItem.imageClass = item.imageClass || item.icon || '';
            dataItem.iconClass = item.iconClass || '';
            dataItem.iconFontCode = this._getIconFontCode(item.icon);
            dataItem.iconStyle = this._buildIconStyle(item, isActive, props);
            dataItem._iconDisplay = (item.icon || item.imageClass) ? 'display: block;' : 'display: none;';
            
            // 文本相关变量
            dataItem.textClass = item.textClass || '';
            dataItem.textStyle = this._buildTextStyle(item, isActive, props);
            dataItem._textDisplay = dataItem.text ? 'display: block;' : 'display: none;';
            
            // 徽章相关变量
            dataItem.badgeClass = item.badgeClass || '';
            dataItem.badgeText = this._formatBadgeText(item.badge);
            dataItem.badgeStyle = this._buildBadgeStyle(item, props);
            dataItem._badgeType = this._getBadgeType(item.badge);
            dataItem._badgeDisplay = (item.badge !== null && item.badge !== undefined) ? 'display: inline-block;' : 'display: none;';
            
            // 自定义样式
            dataItem.itemClass = item.itemClass || item.className || '';
            dataItem.itemStyle = this._buildItemStyle(item, isActive, props);
            
            return dataItem;
        },
        
        // 构建项目状态类
        _buildItemStateClass: function(isActive, isDisabled) {
            var classes = [];
            if (isActive) classes.push('active');
            if (isDisabled) classes.push('disabled');
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
        _buildIconStyle: function(item, isActive, props) {
            var styles = [];
            
            // 基础图标样式
            if (item.iconStyle) {
                styles.push(item.iconStyle);
            }
            
            // 主题颜色
            if (isActive) {
                styles.push('color: ' + (props.activeColor || 'var(--mobile-primary, #007AFF)'));
            } else {
                styles.push('color: ' + (props.inactiveColor || 'var(--mobile-text-tertiary, #8E8E93)'));
            }
            
            // 图标大小
            if (props.iconSize) {
                styles.push('font-size: ' + props.iconSize);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 构建文本样式
        _buildTextStyle: function(item, isActive, props) {
            var styles = [];
            
            // 基础文本样式
            if (item.textStyle) {
                styles.push(item.textStyle);
            }
            
            // 主题颜色
            if (isActive) {
                styles.push('color: ' + (props.activeColor || 'var(--mobile-primary, #007AFF)'));
                if (props.activeFontWeight) {
                    styles.push('font-weight: ' + props.activeFontWeight);
                }
            } else {
                styles.push('color: ' + (props.inactiveColor || 'var(--mobile-text-tertiary, #8E8E93)'));
            }
            
            // 字体大小
            if (props.textSize) {
                styles.push('font-size: ' + props.textSize);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 格式化徽章文本
        _formatBadgeText: function(badge) {
            if (badge === null || badge === undefined) return '';
            if (typeof badge === 'number') {
                if (badge > 99) return '99+';
                if (badge <= 0) return '';
                return badge.toString();
            }
            if (typeof badge === 'string') {
                return badge;
            }
            return '';
        },
        
        // 构建徽章样式
        _buildBadgeStyle: function(item, props) {
            var styles = [];
            
            // 基础徽章样式
            if (item.badgeStyle) {
                styles.push(item.badgeStyle);
            }
            
            // 徽章颜色
            if (props.badgeColor) {
                styles.push('background-color: ' + props.badgeColor);
            }
            if (props.badgeTextColor) {
                styles.push('color: ' + props.badgeTextColor);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 获取徽章类型
        _getBadgeType: function(badge) {
            if (badge === null || badge === undefined) return '';
            if (typeof badge === 'number' && badge <= 0) return 'dot';
            if (typeof badge === 'boolean' && badge) return 'dot';
            return 'text';
        },
        
        // 构建项目样式
        _buildItemStyle: function(item, isActive, props) {
            var styles = [];
            
            // 基础项目样式
            if (item.itemStyle) {
                styles.push(item.itemStyle);
            }
            
            // 激活状态背景
            if (isActive && props.activeBackgroundColor) {
                styles.push('background-color: ' + props.activeBackgroundColor);
            }
            
            // 自定义宽度
            if (item.width) {
                styles.push('width: ' + item.width);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        EventHandlers: {
            // ood.UI 生命周期事件处理器
            onReady: function(profile) {
                // 组件就绪时的初始化
                profile.boxing().initTabBarFeatures();
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
                // 标签选中事件
            },
            
            onItemAdded: function(profile, items, index) {
                // 标签添加事件
            },
            
            onItemRemoved: function(profile, items, indices) {
                // 标签删除事件
            },
            
            // TabBar 特定事件处理器
            onTabChange: function(profile, index, tab) {
                // 标签变化事件处理器
            }
        }
    }
});