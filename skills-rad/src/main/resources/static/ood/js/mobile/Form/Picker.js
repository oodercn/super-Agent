/**
 * 移动端选择器组件
 * 继承自 ood.UI（UI基类）、ood.absList（列表功能）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持单选、多选、级联选择等功能
 */
ood.Class("ood.Mobile.Picker", ["ood.UI", "ood.absList", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['TRIGGER', 'TRIGGER_TEXT', 'ARROW', 'OVERLAY', 'POPUP', 'HEADER', 'OPTIONS']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化 - 修复不符合ood架构规范的调用方式
            //this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端选择器特性
            this.initMobilePickerFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobilePickerFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端选择器CSS类
            profile.getRoot().addClass('ood-mobile-picker ood-mobile-component');
            
            // 初始化选择器功能
            this.initPickerFeatures();
            
            // 初始化触摸事件
            this.initTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        // 移动端触摸事件初始化
        initTouchEvents: function() {
            if (!ood.Mobile || !ood.Mobile.config.touch.enabled) return;
            
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 添加触摸支持类
            root.addClass('ood-touch-enabled');
            
            // 绑定触摸事件
            this.bindTouchEvents();
        },
        
        // 响应式设计初始化
        initResponsive: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var self = this;
            
            // 初始调整布局
            this.adjustLayout();
            
            // 监听窗口大小变化
            if (window.addEventListener) {
                var resizeHandler = ood.Mobile.utils.debounce(function() {
                    self.adjustLayout();
                }, 300);
                
                window.addEventListener('resize', resizeHandler);
                window.addEventListener('orientationchange', resizeHandler);
                
                // 在组件销毁时清理事件监听
                profile.$beforeDestroy = profile.$beforeDestroy || [];
                profile.$beforeDestroy.push(function() {
                    window.removeEventListener('resize', resizeHandler);
                    window.removeEventListener('orientationchange', resizeHandler);
                });
            }
        },
        
        // 可访问性初始化
        initAccessibility: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            var root = profile.getRoot();
            var trigger = profile.getSubNode('TRIGGER');
            var overlay = profile.getSubNode('OVERLAY');
            
            // 为触发器添加ARIA属性
            if (trigger && !trigger.isEmpty()) {
                trigger.attr({
                    'role': 'button',
                    'aria-haspopup': 'listbox',
                    'aria-expanded': 'false',
                    'aria-label': profile.properties.placeholder || '选择器'
                });
            }
            
            // 为弹窗添加ARIA属性
            if (overlay && !overlay.isEmpty()) {
                overlay.attr({
                    'role': 'dialog',
                    'aria-modal': 'true',
                    'aria-label': profile.properties.title || '选择器'
                });
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                var popup = profile.getSubNode('POPUP');
                
                // 清除旧的尺寸类
                root.removeClass('picker-xs picker-sm picker-md picker-lg picker-xl');
                
                // 添加当前尺寸类
                root.addClass('picker-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('picker-tiny');
                    
                    // 调整弹窗高度
                    if (popup && !popup.isEmpty()) {
                        popup.css({
                            'max-height': '70vh'
                        });
                    }
                } else {
                    root.removeClass('picker-tiny');
                    
                    if (popup && !popup.isEmpty()) {
                        popup.css({
                            'max-height': ''
                        });
                    }
                }
            });
        },
        
        // ood.absValue 必需方法
        _setCtrlValue: function(value) {
            this.setValue(value);
        },
        
        _getCtrlValue: function() {
            return this.getValue();
        },
        
        activate: function() {
            return this.each(function(profile) {
                var trigger = profile.getSubNode('TRIGGER');
                if (trigger) {
                    trigger.focus();
                }
            });
        },
        
        resetValue: function(value) {
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
            });
            return this;
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentOptions = self.getOptions();
                if (typeof index === 'undefined') {
                    currentOptions = currentOptions.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentOptions.splice.apply(currentOptions, [insertIndex, 0].concat(items));
                }
                
                self.setOptions(currentOptions);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentOptions = self.getOptions();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var index = parseInt(indices[i]);
                    if (index >= 0 && index < currentOptions.length) {
                        currentOptions.splice(index, 1);
                    }
                }
                
                self.setOptions(currentOptions);
            });
        },
        
        clearItems: function() {
            return this.setOptions([]);
        },
        
        getItems: function() {
            return this.getOptions();
        },
        
        getSelectedItems: function() {
            return this.getSelectedOptions();
        },
        
        selectItem: function(value) {
            this.selectOption(value);
            return this;
        },
        
        unselectItem: function(value) {
            var index = this._selectedValues.indexOf(value);
            if (index > -1) {
                this._selectedValues.splice(index, 1);
                this.updateSelection();
            }
            return this;
        },
        
        initPickerFeatures: function() {
            // 原有的选择器特性初始化逻辑
            this.initData();
            this.initSelection();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var trigger = profile.getSubNode('TRIGGER');
            var overlay = profile.getSubNode('OVERLAY');
            var closeBtn = profile.getSubNode('CLOSE');
            var confirmBtn = profile.getSubNode('CONFIRM');
            
            // 触发器点击
            trigger.on('click', function(e) {
                if (profile.properties.disabled) return;
                self.show();
            });
            
            // 遮罩点击关闭
            overlay.on('click', function(e) {
                if (e.target === overlay.get(0)) {
                    self.hide();
                }
            });
            
            // 关闭按钮
            closeBtn.on('click', function(e) {
                self.hide();
            });
            
            // 确认按钮
            confirmBtn.on('click', function(e) {
                self.confirm();
            });
            
            // 选项点击
            overlay.on('click', '.ood-mobile-picker-option', function(e) {
                var option = ood(this);
                var value = option.attr('data-value');
                self.selectOption(value);
            });
        },
        
        initData: function() {
            this._options = [];
            this._selectedValues = [];
            
            var profile = this.get(0);
            if (profile.properties.options) {
                this.setOptions(profile.properties.options);
            }
        },
        
        initSelection: function() {
            var profile = this.get(0);
            if (profile.properties.value) {
                this.setValue(profile.properties.value);
            }
        },
        
        setOptions: function(options) {
            this._options = options || [];
            this.renderOptions();
        },
        
        getOptions: function() {
            return this._options;
        },
        
        renderOptions: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('OPTIONS');
            
            container.html('');
            
            for (var i = 0; i < this._options.length; i++) {
                var option = this._options[i];
                var optionEl = this.createOptionElement(option);
                container.append(optionEl);
            }
        },
        
        createOptionElement: function(option) {
            // 修正：使用正确的DOM创建方法
            var optionEl = document.createElement('div');
            optionEl.className = 'ood-mobile-picker-option';
            optionEl.setAttribute('data-value', option.value);
            optionEl.innerHTML = option.label || option.text || option.value;
            
            // 返回ood包装的元素（使用ood的正确调用方式）
            var oodEl = ood([optionEl], false);
            
            if (option.disabled) {
                oodEl.addClass('ood-mobile-picker-option-disabled');
            }
            
            return oodEl;
        },
        
        selectOption: function(value) {
            var profile = this.get(0);
            var props = profile.properties;
            
            if (props.multiple) {
                this.selectMultiple(value);
            } else {
                this.selectSingle(value);
            }
            
            this.updateSelection();
        },
        
        selectSingle: function(value) {
            this._selectedValues = [value];
        },
        
        selectMultiple: function(value) {
            var index = this._selectedValues.indexOf(value);
            if (index > -1) {
                this._selectedValues.splice(index, 1);
            } else {
                this._selectedValues.push(value);
            }
        },
        
        updateSelection: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('OPTIONS');
            var options = container.find('.ood-mobile-picker-option');
            
            var self = this;
            options.each(function() {
                var option = ood(this);
                var value = option.attr('data-value');
                
                if (self._selectedValues.indexOf(value) > -1) {
                    option.addClass('ood-mobile-picker-option-selected');
                } else {
                    option.removeClass('ood-mobile-picker-option-selected');
                }
            });
            
            this.updateTriggerText();
        },
        
        updateTriggerText: function() {
            var profile = this.get(0);
            var triggerText = profile.getSubNode('TRIGGER_TEXT');
            var text = this.getSelectedText();
            
            if (text) {
                triggerText.html(text);
                triggerText.removeClass('ood-mobile-picker-placeholder');
            } else {
                triggerText.html(profile.properties.placeholder || '请选择');
                triggerText.addClass('ood-mobile-picker-placeholder');
            }
        },
        
        getSelectedText: function() {
            var selectedOptions = this.getSelectedOptions();
            return selectedOptions.map(function(option) {
                return option.label || option.text || option.value;
            }).join(', ');
        },
        
        getSelectedOptions: function() {
            var self = this;
            return this._options.filter(function(option) {
                return self._selectedValues.indexOf(option.value) > -1;
            });
        },
        
        setValue: function(value) {
            var profile = this.get(0);
            
            if (profile.properties.multiple) {
                this._selectedValues = Array.isArray(value) ? value : [value];
            } else {
                this._selectedValues = value ? [value] : [];
            }
            
            this.updateSelection();
        },
        
        getValue: function() {
            var profile = this.get(0);
            
            if (profile.properties.multiple) {
                return this._selectedValues.slice();
            } else {
                return this._selectedValues[0] || null;
            }
        },
        
        show: function() {
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var popup = profile.getSubNode('POPUP');
            
            overlay.css('display', 'flex');
            
            // 动画显示
            setTimeout(function() {
                overlay.addClass('ood-mobile-picker-overlay-show');
                popup.addClass('ood-mobile-picker-popup-show');
            }, 10);
        },
        
        hide: function() {
            var profile = this.get(0);
            var overlay = profile.getSubNode('OVERLAY');
            var popup = profile.getSubNode('POPUP');
            
            overlay.removeClass('ood-mobile-picker-overlay-show');
            popup.removeClass('ood-mobile-picker-popup-show');
            
            setTimeout(function() {
                overlay.css('display', 'none');
            }, 300);
        },
        
        confirm: function() {
            var profile = this.get(0);
            var value = this.getValue();
            
            profile.properties.value = value;
            this.onChange(value);
            this.hide();
        },
        
        onChange: function(value) {
            var profile = this.get(0);
            
            if (profile.onChange) {
                profile.boxing().onChange(profile, value);
            }
        }
    },
    
    Static: {
        // 四分离架构 - 模板定义（Template）
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-picker {_className}',
            style: '{_style}',
            tabindex: '{tabindex}',
            'aria-label': '{_ariaLabel}',
            
            TRIGGER: {
                tagName: 'div',
                className: 'ood-mobile-picker-trigger',
                role: 'button',
                'aria-haspopup': 'listbox',
                'aria-expanded': 'false',
                tabindex: '0',
                
                TRIGGER_TEXT: {
                    tagName: 'span',
                    className: 'ood-mobile-picker-trigger-text {_placeholderClass}',
                    text: '{_triggerText}'
                },
                
                ARROW: {
                    tagName: 'i',
                    className: 'ood-mobile-picker-arrow',
                    'aria-hidden': 'true'
                }
            },
            
            OVERLAY: {
                tagName: 'div',
                className: 'ood-mobile-picker-overlay',
                style: 'display: none',
                role: 'dialog',
                'aria-modal': 'true',
                'aria-label': '{title}',
                
                POPUP: {
                    tagName: 'div',
                    className: 'ood-mobile-picker-popup',
                    
                    HEADER: {
                        tagName: 'div',
                        className: 'ood-mobile-picker-header',
                        
                        CLOSE: {
                            tagName: 'button',
                            className: 'ood-mobile-picker-close',
                            type: 'button',
                            'aria-label': '取消',
                            text: '取消'
                        },
                        
                        TITLE: {
                            tagName: 'div',
                            className: 'ood-mobile-picker-title',
                            text: '{title}'
                        },
                        
                        CONFIRM: {
                            tagName: 'button',
                            className: 'ood-mobile-picker-confirm',
                            type: 'button',
                            'aria-label': '确定',
                            text: '确定'
                        }
                    },
                    
                    OPTIONS: {
                        tagName: 'div',
                        className: 'ood-mobile-picker-options',
                        role: 'listbox',
                        'aria-multiselectable': '{_ariaMulti}'
                    }
                }
            }
        },
        
        // 四分离架构 - 外观定义（Appearances）
        Appearances: {
            KEY: {
                'position': 'relative',
                'width': '100%',
                'box-sizing': 'border-box',
                'font-family': 'var(--mobile-font-family, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif)'
            },
            
            TRIGGER: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'padding': 'var(--mobile-spacing-sm, 8px) var(--mobile-spacing-md, 12px)',
                'background-color': 'var(--mobile-bg-primary, #FFFFFF)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'cursor': 'pointer',
                'min-height': 'var(--mobile-touch-target, 44px)',
                'transition': 'all 0.2s ease',
                'outline': 'none'
            },
            
            'TRIGGER:hover': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'TRIGGER:focus': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.2))'
            },
            
            'TRIGGER:disabled': {
                'background-color': 'var(--mobile-disabled-bg, #F7F7F7)',
                'border-color': 'var(--mobile-disabled-border, #E0E0E0)',
                'cursor': 'not-allowed',
                'opacity': '0.6'
            },
            
            TRIGGER_TEXT: {
                'flex': '1',
                'font-size': 'var(--mobile-font-md, 16px)',
                'color': 'var(--mobile-text-primary, #000000)',
                'white-space': 'nowrap',
                'overflow': 'hidden',
                'text-overflow': 'ellipsis',
                'text-align': 'left'
            },
            
            'TRIGGER_TEXT.ood-mobile-picker-placeholder': {
                'color': 'var(--mobile-text-tertiary, #C7C7CC)'
            },
            
            ARROW: {
                'width': '0',
                'height': '0',
                'border-left': '4px solid transparent',
                'border-right': '4px solid transparent',
                'border-top': '4px solid var(--mobile-text-tertiary, #C7C7CC)',
                'margin-left': 'var(--mobile-spacing-sm, 8px)',
                'transition': 'transform 0.2s ease'
            },
            
            'TRIGGER[aria-expanded="true"] ARROW': {
                'transform': 'rotate(180deg)'
            },
            
            OVERLAY: {
                'position': 'fixed',
                'top': '0',
                'left': '0',
                'width': '100%',
                'height': '100%',
                'background-color': 'rgba(0, 0, 0, 0.5)',
                'z-index': '1000',
                'display': 'flex',
                'align-items': 'flex-end',
                'justify-content': 'center',
                'opacity': '0',
                'transition': 'opacity 0.3s ease'
            },
            
            'OVERLAY.ood-mobile-picker-overlay-show': {
                'opacity': '1'
            },
            
            POPUP: {
                'background-color': 'var(--mobile-bg-primary, #FFFFFF)',
                'border-radius': 'var(--mobile-radius-lg, 12px) var(--mobile-radius-lg, 12px) 0 0',
                'max-height': '60vh',
                'width': '100%',
                'max-width': '500px',
                'box-shadow': '0 -4px 20px rgba(0, 0, 0, 0.1)',
                'transform': 'translateY(100%)',
                'transition': 'transform 0.3s ease'
            },
            
            'POPUP.ood-mobile-picker-popup-show': {
                'transform': 'translateY(0)'
            },
            
            HEADER: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'border-bottom': '1px solid var(--mobile-border, #C7C7CC)',
                'min-height': '56px'
            },
            
            CLOSE: {
                'background-color': 'transparent',
                'border': 'none',
                'color': 'var(--mobile-text-secondary, #8E8E93)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'cursor': 'pointer',
                'padding': 'var(--mobile-spacing-xs, 4px) var(--mobile-spacing-sm, 8px)',
                'border-radius': 'var(--mobile-radius-sm, 4px)',
                'transition': 'background-color 0.2s ease'
            },
            
            'CLOSE:hover': {
                'background-color': 'var(--mobile-bg-secondary, #F2F2F7)'
            },
            
            TITLE: {
                'font-size': 'var(--mobile-font-lg, 18px)',
                'font-weight': '600',
                'color': 'var(--mobile-text-primary, #000000)',
                'text-align': 'center',
                'flex': '1'
            },
            
            CONFIRM: {
                'background-color': 'transparent',
                'border': 'none',
                'color': 'var(--mobile-primary, #007AFF)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '600',
                'cursor': 'pointer',
                'padding': 'var(--mobile-spacing-xs, 4px) var(--mobile-spacing-sm, 8px)',
                'border-radius': 'var(--mobile-radius-sm, 4px)',
                'transition': 'background-color 0.2s ease'
            },
            
            'CONFIRM:hover': {
                'background-color': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'CONFIRM:disabled': {
                'color': 'var(--mobile-disabled, #C7C7CC)',
                'cursor': 'not-allowed'
            },
            
            OPTIONS: {
                'max-height': '40vh',
                'overflow-y': 'auto',
                'padding': 'var(--mobile-spacing-xs, 4px) 0'
            },
            
            '.ood-mobile-picker-option': {
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'border-bottom': '1px solid var(--mobile-border-light, #F2F2F7)',
                'cursor': 'pointer',
                'font-size': 'var(--mobile-font-md, 16px)',
                'color': 'var(--mobile-text-primary, #000000)',
                'transition': 'background-color 0.2s ease',
                'position': 'relative',
                'min-height': '44px',
                'display': 'flex',
                'align-items': 'center'
            },
            
            '.ood-mobile-picker-option:hover': {
                'background-color': 'var(--mobile-bg-secondary, #F2F2F7)'
            },
            
            '.ood-mobile-picker-option:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-picker-option-selected': {
                'background-color': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))',
                'color': 'var(--mobile-primary, #007AFF)',
                'font-weight': '600'
            },
            
            '.ood-mobile-picker-option-selected::after': {
                'content': '"\u2713"',
                'position': 'absolute',
                'right': 'var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-lg, 18px)',
                'color': 'var(--mobile-primary, #007AFF)'
            },
            
            '.ood-mobile-picker-option-disabled': {
                'opacity': '0.5',
                'cursor': 'not-allowed',
                'color': 'var(--mobile-disabled, #C7C7CC)'
            },
            
            // 响应式断点样式
            '@media (max-width: 479px)': {
                'POPUP': {
                    'max-height': '70vh'
                },
                'OPTIONS': {
                    'max-height': '50vh'
                },
                'HEADER': {
                    'padding': 'var(--mobile-spacing-sm, 8px) var(--mobile-spacing-md, 12px)'
                }
            },
            
            // 主题支持
            '[data-theme="dark"]': {
                '--mobile-bg-primary': '#1C1C1E',
                '--mobile-bg-secondary': '#2C2C2E',
                '--mobile-border': '#38383A',
                '--mobile-border-light': '#2C2C2E',
                '--mobile-text-primary': '#FFFFFF',
                '--mobile-text-secondary': '#8E8E93',
                '--mobile-text-tertiary': '#6D6D70',
                '--mobile-primary': '#0A84FF',
                '--mobile-primary-light': 'rgba(10, 132, 255, 0.1)'
            },
            
            '[data-theme="highcontrast"]': {
                '--mobile-primary': '#0000FF',
                '--mobile-text-primary': '#000000',
                '--mobile-border': '#000000',
                '--mobile-bg-primary': '#FFFFFF'
            }
        },
        
        // 四分离架构 - 数据模型（DataModel）
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '选择器标题',
                ini: '选择器',
                action: function(value) {
                    var profile = this;
                    // 更新ariaLabel属性保持同步
                    profile.properties.ariaLabel = value;
                    profile.getRoot().attr('aria-label', value || '选择器');
                }
            },
            
            // 继承自UI基类的基本属性
            width: {
                caption: '选择器宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            height: {
                caption: '选择器高度',
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
            
            triggerIcon: {
                caption: '触发器图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var iconNode = this.getSubNode('ICON');
                    if (iconNode && !iconNode.isEmpty()) {
                        iconNode.html(value || '');
                    }
                }
            },
            
            // ===== 选择器特有属性 =====
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    var trigger = this.getSubNode('TRIGGER');
                    var confirm = this.getSubNode('CONFIRM');
                    
                    if (trigger && !trigger.isEmpty()) {
                        trigger.prop('disabled', value);
                        trigger.attr('aria-disabled', value.toString());
                    }
                    if (confirm && !confirm.isEmpty()) {
                        confirm.prop('disabled', value);
                    }
                }
            },
            
            // 选择器特有属性
            options: {
                ini: [],
                action: function(value) {
                    this.boxing().setOptions(value);
                }
            },
            
            value: {
                ini: null,
                action: function(value) {
                    this.boxing().setValue(value);
                }
            },
            
            placeholder: {
                ini: '请选择',
                action: function(value) {
                    this.boxing().updateTriggerText();
                }
            },
            
            title: {
                ini: '请选择'
            },
            
            multiple: {
                ini: false,
                action: function(value) {
                    var options = this.getSubNode('OPTIONS');
                    if (options && !options.isEmpty()) {
                        options.attr('aria-multiselectable', value.toString());
                    }
                }
            },
            
            // 主题支持
            theme: {
                ini: '',
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // 响应式支持
            responsive: {
                ini: true,
                action: function(value) {
                    if (value) {
                        this.boxing().adjustLayout();
                    }
                }
            },
            
            // 选项配置
            searchable: {
                ini: false
            },
            
            clearable: {
                ini: false
            },
            
            maxSelected: {
                ini: null
            },
            
            // 动画效果
            enableAnimations: {
                ini: true
            },
            
            // 可访问性支持
            ariaLabel: {
                ini: '选择器',
                action: function(value) {
                    this.getRoot().attr('aria-label', value);
                }
            },
            
            // 继承自UI基类的其他属性
            tabindex: {
                ini: -1
            }
        },
        

        // 渲染触发器（组件初始化后调用）
        RenderTrigger: function() {
            var profile = this;
            
            // 异步执行初始化，避免循环依赖
            ood.asyRun(function() {
                // 触发Boxing的Initialize方法
                if (profile.boxing && profile.boxing().Initialize) {
                    profile.boxing().Initialize();
                }
                
                // 初始化主题
                var theme = profile.properties.theme || 
                           localStorage.getItem('ood-mobile-picker-theme') || 
                           (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
                           
                if (theme !== 'light') {
                    profile.boxing().setTheme(theme, false);
                }
                
                // 初始化响应式布局
                if (profile.properties.responsive !== false) {
                    profile.boxing().adjustLayout();
                }
                
                // 触发自定义渲染事件
                if (profile.onRender) {
                    profile.boxing().onRender(profile);
                }
            });
        },
        
        // 数据准备方法（模板渲染前调用）
        _prepareData: function(profile) {
            // 调用父类的数据准备方法
            var data = arguments.callee.upper.call(this, profile);
            var props = profile.properties;
            
            // 准备选择器特定数据
            data._triggerText = props.placeholder || '请选择';
            data._placeholderClass = 'ood-mobile-picker-placeholder';
            data._ariaLabel = props.ariaLabel || '选择器';
            data._ariaMulti = props.multiple ? 'true' : 'false';
            
            // 主题相关数据
            data._theme = props.theme || 'light';
            data._themeClass = props.theme && props.theme !== 'light' ? ('mobile-theme-' + props.theme) : '';
            
            // 响应式数据
            data._screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            data._isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            
            // 合并className
            var classNames = ['ood-mobile-picker', 'ood-mobile-component'];
            if (data._themeClass) classNames.push(data._themeClass);
            if (data._isMobile) classNames.push('mobile-device');
            classNames.push('picker-' + data._screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            // 设置选择器属性
            data.title = props.title || '请选择';
            data.tabindex = props.tabindex || -1;
            
            // 设置禁用状态
            data._disabled = props.disabled ? 'disabled' : '';
            
            // 设置表单字段属性
            data._isFormField = props.isFormField ? 'form-field' : '';
            
            // 设置搜索和清除属性
            data._searchable = props.searchable ? 'searchable' : '';
            data._clearable = props.clearable ? 'clearable' : '';
            
            // 设置动画属性
            data._enableAnimations = props.enableAnimations !== false ? 'animations-enabled' : '';
            
            // 设置样式
            data._style = props.style || '';
            
            return data;
        },
        
        // 四分离架构 - 行为定义（Behaviors）
        Behaviors: {
            // 鼠标悬停效果支持
            HoverEffected: {
                KEY: 'TRIGGER,CLOSE,CONFIRM'
            },
            
            // 支持面板操作（继承自UI）
            PanelKeys: ['KEY'],
            
            // 触发器点击行为
            onTriggerClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 显示选择器
                if (profile.onShow) {
                    var result = profile.boxing().onShow(profile, e);
                    if (result !== false) {
                        profile.boxing().show();
                    }
                    return result;
                } else {
                    profile.boxing().show();
                    return true;
                }
            },
            
            // 选项点击行为
            onOptionClick: function(profile, value, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 选中选项
                profile.boxing().selectOption(value);
                
                // 触发选项点击事件
                if (profile.onOptionClick) {
                    return profile.boxing().onOptionClick(profile, value, e);
                }
                
                return true;
            },
            
            // 确认行为
            onConfirm: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                var value = profile.boxing().getValue();
                
                // 触发确认事件
                if (profile.onConfirm) {
                    var result = profile.boxing().onConfirm(profile, value, e);
                    if (result !== false) {
                        profile.boxing().confirm();
                    }
                    return result;
                } else {
                    profile.boxing().confirm();
                    return true;
                }
            },
            
            // 取消行为
            onCancel: function(profile, e, src) {
                var props = profile.properties;
                
                // 触发取消事件
                if (profile.onCancel) {
                    var result = profile.boxing().onCancel(profile, e);
                    if (result !== false) {
                        profile.boxing().hide();
                    }
                    return result;
                } else {
                    profile.boxing().hide();
                    return true;
                }
            },
            
            // 通用点击事件处理
            onClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 根据点击目标处理不同逻辑
                var target = ood(e.target);
                
                if (target.hasClass('ood-mobile-picker-trigger') || target.closest('.ood-mobile-picker-trigger').length) {
                    return this.onTriggerClick(profile, e, src);
                } else if (target.hasClass('ood-mobile-picker-option')) {
                    var value = target.attr('data-value');
                    return this.onOptionClick(profile, value, e, src);
                } else if (target.hasClass('ood-mobile-picker-confirm')) {
                    return this.onConfirm(profile, e, src);
                } else if (target.hasClass('ood-mobile-picker-close')) {
                    return this.onCancel(profile, e, src);
                }
                
                // 触发通用点击事件
                if (profile.onClick) {
                    return profile.boxing().onClick(profile, e, src);
                }
                
                return true;
            }
        },
        
        // 四分离架构 - 事件处理器（EventHandlers）
        EventHandlers: {
            // 触发器点击事件
            onTriggerClick: function(profile, event) {
                // 触发器点击处理
                return true;
            },
            
            // 弹窗显示事件
            onPopupShow: function(profile) {
                // 弹窗显示处理
                return true;
            },
            
            // 弹窗隐藏事件
            onPopupHide: function(profile) {
                // 弹窗隐藏处理
                return true;
            },
            
            // 选项点击事件
            onOptionClick: function(profile, value, event) {
                // 选项点击处理
                return true;
            },
            
            // 确认事件
            onConfirm: function(profile, value, event) {
                // 确认选择处理
                return true;
            },
            
            // 取消事件
            onCancel: function(profile, event) {
                // 取消选择处理
                return true;
            },
            
            // 主题变化事件
            onThemeChange: function(profile, oldTheme, newTheme) {
                // 主题切换处理
                console.log('选择器主题切换:', oldTheme, '->', newTheme);
                return true;
            },
            
            // 继承自UI基类的事件处理器
            onClick: function(profile, event, src) {
                // 通用点击事件处理
                return true;
            },
            
            onRender: function(profile) {
                // 渲染完成事件
                return true;
            },
            
            onResize: function(profile, width, height) {
                // 尺寸变化事件
                profile.boxing().adjustLayout();
                return true;
            },
            
            // 错误处理
            onError: function(profile, error, context) {
                console.error('选择器错误:', error, context);
                return true;
            }
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Picker组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                trigger = profile.getSubNode('TRIGGER'),
                popup = profile.getSubNode('POPUP'),
                optionsContainer = profile.getSubNode('OPTIONS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整选择器容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    trigger.css('width', '100%');
                    
                    // 调整弹窗的最大宽度
                    if (popup && !popup.isEmpty()) {
                        // 限制弹窗最大宽度为屏幕宽度的90%
                        var popupMaxWidth = Math.min(pxWidth, window.innerWidth * 0.9);
                        popup.css('max-width', adjustunit(popupMaxWidth));
                    }
                }
            }

            // 如果提供了高度，调整选择器容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    trigger.css('height', '100%');
                    
                    // 调整选项容器的高度以适应弹窗
                    if (optionsContainer && !optionsContainer.isEmpty() && popup && !popup.isEmpty()) {
                        var headerHeight = profile.getSubNode('HEADER').offsetHeight(true) || 0;
                        var popupHeight = popup.cssSize().height;
                        var availableHeight = popupHeight - headerHeight;
                        
                        optionsContainer.css('max-height', adjustunit(availableHeight));
                    }
                }
            }

            // 根据新的尺寸调整布局
            this.boxing().adjustLayout();
        }
    }
});