/**
 * 移动端表单组件
 * 继承自ood.UI（面板基类）和ood.absContainer（容器功能），符合ood框架规范
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持表单验证、数据收集、提交等功能
 */
ood.Class("ood.Mobile.Form", ["ood.UI", "ood.absContainer", "ood.absList"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['FORM', 'FIELDS', 'ACTIONS', 'SUBMIT', 'RESET']);
    },
    Instance: {
        Initialize: function() {
            // 调用父类初始化
            //this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端Form特性
            this.initMobileFormFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileFormFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端表单CSS类
            profile.getRoot().addClass('ood-mobile-form ood-mobile-component');
            
            // 初始化表单功能
            this.initFormFeatures();
            
            // 初始化触摸事件
            this.initTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
        },
        
        initFormFeatures: function() {
            // 原有的表单特性初始化逻辑
            this.initFields();
            this.initValidation();
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
            
            // 监听窗口大小变化（使用ood框架的事件机制）
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
            var form = profile.getSubNode('FORM');
            
            // 为表单添加ARIA属性
            root.attr({
                'role': 'form',
                'aria-label': '移动端表单'
            });
            
            if (form && !form.isEmpty()) {
                form.attr({
                    'novalidate': 'novalidate', // 禁用浏览器默认验证
                    'autocomplete': 'on'
                });
            }
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var form = profile.getSubNode('FORM');
            
            if (form && !form.isEmpty()) {
                // 表单提交
                form.on('submit', function(e) {
                    e.preventDefault();
                    self.submit();
                });
                
                // 重置按钮
                form.on('click', '.ood-mobile-form-reset', function(e) {
                    e.preventDefault();
                    self.reset();
                });
                
                // 移动端特有事件：长按重置（可选）
                if (ood.Mobile.config.touch.longPressDelay) {
                    var longPressTimer;
                    form.on('touchstart', '.ood-mobile-form-field', function(e) {
                        var field = e.currentTarget;
                        longPressTimer = setTimeout(function() {
                            self.showFieldOptions(field);
                        }, ood.Mobile.config.touch.longPressDelay);
                    });
                    
                    form.on('touchend touchcancel', '.ood-mobile-form-field', function() {
                        if (longPressTimer) {
                            clearTimeout(longPressTimer);
                            longPressTimer = null;
                        }
                    });
                }
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                var fieldsContainer = profile.getSubNode('FIELDS');
                var actionsContainer = profile.getSubNode('ACTIONS');
                
                // 清除旧的尺寸类
                root.removeClass('form-xs form-sm form-md form-lg form-xl');
                
                // 添加当前尺寸类
                root.addClass('form-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('form-tiny');
                    
                    // 调整字体大小
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'font-size': '0.9em',
                            'gap': 'var(--mobile-spacing-sm)'
                        });
                    }
                    
                    // 调整按钮布局为垂直排列
                    if (actionsContainer && !actionsContainer.isEmpty()) {
                        actionsContainer.css({
                            'flex-direction': 'column',
                            'gap': 'var(--mobile-spacing-sm)'
                        });
                    }
                } else {
                    root.removeClass('form-tiny');
                    
                    // 恢复正常尺寸
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'font-size': '',
                            'gap': ''
                        });
                    }
                    
                    if (actionsContainer && !actionsContainer.isEmpty()) {
                        actionsContainer.css({
                            'flex-direction': '',
                            'gap': ''
                        });
                    }
                }
                
                // 平板模式调整
                if (width >= 768 && width < 1024) {
                    root.addClass('form-tablet');
                    
                    // 两列布局优化
                    if (fieldsContainer && !fieldsContainer.isEmpty()) {
                        fieldsContainer.css({
                            'display': 'grid',
                            'grid-template-columns': 'repeat(auto-fit, minmax(250px, 1fr))',
                            'gap': 'var(--mobile-spacing-md)'
                        });
                    }
                } else {
                    root.removeClass('form-tablet');
                    
                    if (fieldsContainer && !fieldsContainer.isEmpty() && width < 768) {
                        fieldsContainer.css({
                            'display': '',
                            'grid-template-columns': '',
                            'gap': ''
                        });
                    }
                }
            });
        },
        
        // 设置主题
        setTheme: function(theme, persist) {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var oldTheme = profile.properties.theme || 'light';
                
                // 更新属性
                profile.properties.theme = theme;
                
                // 移除旧主题类
                root.removeClass('mobile-theme-' + oldTheme);
                root.removeAttr('data-theme');
                
                // 应用新主题
                if (theme && theme !== 'light') {
                    root.addClass('mobile-theme-' + theme);
                    root.attr('data-theme', theme);
                }
                
                // 持久化主题设置
                if (persist !== false) {
                    try {
                        localStorage.setItem('ood-mobile-form-theme', theme);
                    } catch(e) {
                        // 忽略localStorage错误
                    }
                }
                
                // 触发主题变化事件
                if (profile.onThemeChange) {
                    profile.boxing().onThemeChange(oldTheme, theme);
                }
            });
        },
        
        // 获取当前主题
        getTheme: function() {
            var profile = this.get(0);
            if (profile && profile.properties.theme) {
                return profile.properties.theme;
            }
            
            // 从 localStorage 获取
            try {
                var savedTheme = localStorage.getItem('ood-mobile-form-theme');
                if (savedTheme) return savedTheme;
            } catch(e) {
                // 忽略localStorage错误
            }
            
            // 检测系统主题
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                return 'dark';
            }
            
            return 'light';
        },
        
        // 显示字段选项（长按触发）
        showFieldOptions: function(field) {
            // 可以在这里实现字段的上下文菜单
            // 例如：清空、复制、粘贴等功能
            console.log('长按字段：', field);
        },
        
        initFields: function() {
            this._fields = {};
            this._fieldValidators = {};
        },
        
        initValidation: function() {
            this._isValid = true;
            this._errors = {};
        },
        
        addField: function(name, element) {
            this._fields[name] = element;
        },
        
        removeField: function(name) {
            delete this._fields[name];
            delete this._fieldValidators[name];
            delete this._errors[name];
        },
        
        getField: function(name) {
            return this._fields[name];
        },
        
        setFieldValue: function(name, value) {
            var field = this._fields[name];
            if (field && field.setValue) {
                field.setValue(value);
            }
        },
        
        getFieldValue: function(name) {
            var field = this._fields[name];
            if (field && field.getValue) {
                return field.getValue();
            }
            return null;
        },
        
        getFormData: function() {
            var data = {};
            for (var name in this._fields) {
                data[name] = this.getFieldValue(name);
            }
            return data;
        },
        
        setFormData: function(data) {
            for (var name in data) {
                this.setFieldValue(name, data[name]);
            }
        },
        
        validate: function() {
            this._isValid = true;
            this._errors = {};
            
            for (var name in this._fields) {
                var field = this._fields[name];
                var validators = this._fieldValidators[name];
                
                if (validators) {
                    var value = this.getFieldValue(name);
                    var fieldValid = this.validateField(name, value, validators);
                    
                    if (!fieldValid) {
                        this._isValid = false;
                    }
                }
            }
            
            this.updateValidationUI();
            return this._isValid;
        },
        
        validateField: function(name, value, validators) {
            var isValid = true;
            
            for (var i = 0; i < validators.length; i++) {
                var validator = validators[i];
                if (!validator.validate(value)) {
                    this._errors[name] = validator.message;
                    isValid = false;
                    break;
                }
            }
            
            return isValid;
        },
        
        updateValidationUI: function() {
            var profile = this.get(0);
            var form = profile.getSubNode('FORM');
            
            // 清除所有错误状态
            form.find('.ood-mobile-form-field-error').removeClass('ood-mobile-form-field-error');
            form.find('.ood-mobile-form-error-message').remove();
            
            // 显示错误
            for (var name in this._errors) {
                var field = this._fields[name];
                if (field && field.getRoot) {
                    var fieldRoot = field.getRoot();
                    fieldRoot.addClass('ood-mobile-form-field-error');
                    
                    var errorMsg = ood('<div class="ood-mobile-form-error-message">' + this._errors[name] + '</div>');
                    fieldRoot.after(errorMsg);
                }
            }
        },
        
        submit: function() {
            var profile = this.get(0);
            
            if (this.validate()) {
                var formData = this.getFormData();
                this.onSubmit(formData);
            }
        },
        
        reset: function() {
            for (var name in this._fields) {
                var field = this._fields[name];
                if (field && field.setValue) {
                    field.setValue('');
                }
            }
            
            this._errors = {};
            this.updateValidationUI();
            this.onReset();
        },
        
        onReset: function() {
            var profile = this.get(0);
            
            if (profile.onReset) {
                profile.boxing().onReset(profile);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            // Form组件通过addField方法管理字段，这里提供适配实现
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                // 对于表单组件，我们将items视为字段定义
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.name) {
                        // 注意：这里需要实际创建字段组件，简化实现只记录字段信息
                        self.addField(item.name, item);
                    }
                }
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                // 获取当前所有字段名
                var fieldNames = Object.keys(self._fields);
                
                // 从后往前删除，保证索引不变
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var index = parseInt(indices[i]);
                    if (index >= 0 && index < fieldNames.length) {
                        var fieldName = fieldNames[index];
                        self.removeField(fieldName);
                    }
                }
            });
        },
        
        clearItems: function() {
            var self = this;
            return this.each(function(profile) {
                // 清空所有字段
                var fieldNames = Object.keys(self._fields);
                for (var i = 0; i < fieldNames.length; i++) {
                    self.removeField(fieldNames[i]);
                }
            });
        },
        
        getItems: function() {
            var profile = this.get(0);
            var items = [];
            
            // 将字段信息转换为items格式返回
            for (var name in this._fields) {
                items.push({
                    name: name,
                    field: this._fields[name]
                });
            }
            
            return items;
        },
        
        getSelectedItems: function() {
            // Form组件不支持选择项，但为了符合接口规范返回空数组
            return [];
        },
        
        selectItem: function(value) {
            // Form组件不支持选择项，但为了符合接口规范提供空实现
            return this;
        },
        
        unselectItem: function(value) {
            // Form组件不支持取消选择项，但为了符合接口规范提供空实现
            return this;
        }
    },
    
    Static: {
        // 四分离架构 - 模板定义（Template）
        Templates: {
            tagName: 'div',
            className: '{_className} ood-mobile-form',
            style: '{_style}',
            tabindex: '{tabindex}',
            
            LABEL: {
                className: '{_required} ood-ui-ellipsis',
                style: '{labelShow};width:{_labelSize};{_labelHAlign};{_labelVAlign}',
                text: '{labelCaption}'
            },
            
            FORM: {
                tagName: 'form',
                className: 'ood-mobile-form-element',
                novalidate: 'novalidate',
                autocomplete: 'on',
                
                ITEMS: {
                    $order: 10,
                    tagName: 'div',
                    className: 'ood-mobile-form-fields ood-uibase',
                    style: '{_itemsStyle}',
                    text: "{items}"
                },
                
                ACTIONS: {
                    $order: 20,
                    tagName: 'div',
                    className: 'ood-mobile-form-actions',
                    role: 'group',
                    'aria-label': '表单操作按钮',
                    
                    SUBMIT: {
                        tagName: 'button',
                        className: 'ood-mobile-form-submit ood-mobile-button ood-mobile-button-primary',
                        type: 'submit',
                        text: '{_submitText}',
                        'aria-describedby': 'submit-help'
                    },
                    
                    RESET: {
                        tagName: 'button',
                        className: 'ood-mobile-form-reset ood-mobile-button ood-mobile-button-ghost',
                        type: 'button',
                        text: '{_resetText}',
                        style: 'display: {_resetDisplay}',
                        'aria-describedby': 'reset-help'
                    }
                }
            },
            
            $submap: {
                items: {
                    ITEM: {
                        tagName: 'div',
                        className: 'ood-mobile-form-field {itemClass} {disabled} {readonly} {_itemState}',
                        style: '{itemStyle}{_itemDisplay}',
                        'data-field': '{fieldName}',
                        'data-id': '{id}',

                        /* 实例数据示例:
                         * {
                         *   id: 'field-1',
                         *   fieldName: 'username',
                         *   label: '用户名',
                         *   fieldType: 'text',
                         *   fieldId: 'username-field',
                         *   placeholder: '请输入用户名',
                         *   required: true,
                         *   readonly: false,
                         *   disabled: false,
                         *   errorMessage: '用户名不能为空',
                         *   itemClass: 'custom-form-field',
                         *   itemStyle: 'margin-bottom: 12px;',
                         *   _itemState: 'error',
                         *   _itemDisplay: 'display: flex;',
                         *   _textareaDisplay: 'display: none;',
                         *   _selectDisplay: 'display: none;',
                         *   _errorDisplay: 'display: block;'
                         * }
                         */

                        FIELDLABEL: {
                            $order: 5,
                            tagName: 'label',
                            className: 'ood-mobile-form-field-label',
                            text: '{label}',
                            'for': '{fieldId}'
                        },
                        FIELDINPUT: {
                            $order: 10,
                            tagName: 'input',
                            className: 'ood-mobile-form-field-input',
                            type: '{fieldType}',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            placeholder: '{placeholder}',
                            required: '{required}',
                            readonly: '{readonly}',
                            disabled: '{disabled}'
                        },
                        FIELDTEXTAREA: {
                            $order: 10,
                            tagName: 'textarea',
                            className: 'ood-mobile-form-field-textarea',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            placeholder: '{placeholder}',
                            required: '{required}',
                            readonly: '{readonly}',
                            disabled: '{disabled}',
                            style: '{_textareaDisplay}'
                        },
                        FIELDSELECT: {
                            $order: 10,
                            tagName: 'select',
                            className: 'ood-mobile-form-field-select',
                            id: '{fieldId}',
                            name: '{fieldName}',
                            required: '{required}',
                            disabled: '{disabled}',
                            style: '{_selectDisplay}'
                        },
                        FIELDERROR: {
                            $order: 20,
                            className: 'ood-mobile-form-error-message',
                            style: '{_errorDisplay}',
                            text: '{errorMessage}'
                        }
                    }
                }
            }
        },
        
        // 四分离架构 - 外观定义（Appearances）
        Appearances: {
            KEY: {
                'width': '100%',
                'max-width': '100%',
                'box-sizing': 'border-box',
                'font-family': 'var(--mobile-font-family, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif)',
                'line-height': 'var(--mobile-line-height, 1.5)'
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
            
            FORM: {
                'width': '100%',
                'display': 'flex',
                'flex-direction': 'column',
                'gap': 'var(--mobile-spacing-lg, 16px)'
            },
            
            ITEMS: {
                'display': 'flex',
                'flex-direction': 'column',
                'gap': 'var(--mobile-spacing-md, 12px)',
                'margin-bottom': 'var(--mobile-spacing-lg, 16px)'
            },
            
            ITEM: {
                'display': 'flex',
                'flex-direction': 'column',
                'margin-bottom': 'var(--mobile-spacing-md, 12px)'
            },
            
            FIELDLABEL: {
                'font-size': 'var(--mobile-font-sm, 14px)',
                'font-weight': '600',
                'color': 'var(--mobile-text-primary, #000000)',
                'margin-bottom': 'var(--mobile-spacing-xs, 4px)',
                'line-height': '1.4'
            },
            
            'FIELDLABEL::after': {
                'content': '" *"',
                'color': 'var(--mobile-danger, #FF3B30)',
                'display': 'var(--field-required-display, none)'
            },
            
            'FIELDINPUT, FIELDTEXTAREA, FIELDSELECT': {
                'width': '100%',
                'padding': 'var(--mobile-spacing-md, 12px)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'background': 'var(--mobile-bg-primary, #FFFFFF)',
                'color': 'var(--mobile-text-primary, #000000)',
                'transition': 'all 0.2s ease',
                'box-sizing': 'border-box'
            },
            
            'FIELDINPUT:focus, FIELDTEXTAREA:focus, FIELDSELECT:focus': {
                'outline': 'none',
                'border-color': 'var(--mobile-primary, #007AFF)',
                'box-shadow': '0 0 0 2px var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'FIELDINPUT:disabled, FIELDTEXTAREA:disabled, FIELDSELECT:disabled': {
                'background': 'var(--mobile-disabled-bg, #F2F2F7)',
                'color': 'var(--mobile-disabled-text, #8E8E93)',
                'cursor': 'not-allowed'
            },
            
            'FIELDINPUT:readonly, FIELDTEXTAREA:readonly': {
                'background': 'var(--mobile-readonly-bg, #F9F9F9)',
                'cursor': 'default'
            },
            
            FIELDTEXTAREA: {
                'min-height': '6em',
                'resize': 'vertical'
            },
            
            FIELDSELECT: {
                'appearance': 'none',
                'background-image': 'url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 16 16\'%3E%3Cpath fill=\'%23999\' d=\'M8 12L3 7h10z\'/%3E%3C/svg%3E")',
                'background-repeat': 'no-repeat',
                'background-position': 'right 12px center',
                'background-size': '12px',
                'padding-right': '36px'
            },
            
            FIELDERROR: {
                'font-size': 'var(--mobile-font-xs, 12px)',
                'color': 'var(--mobile-danger, #FF3B30)',
                'margin-top': 'var(--mobile-spacing-xs, 4px)',
                'line-height': '1.4',
                'display': 'flex',
                'align-items': 'center',
                'gap': 'var(--mobile-spacing-xs, 4px)'
            },
            
            'FIELDERROR::before': {
                'content': '"\u26A0"',
                'font-size': '1em',
                'flex-shrink': '0'
            },
            
            ACTIONS: {
                'display': 'flex',
                'gap': 'var(--mobile-spacing-md, 12px)',
                'margin-top': 'var(--mobile-spacing-lg, 16px)',
                'justify-content': 'space-between',
                'flex-wrap': 'wrap'
            },
            
            SUBMIT: {
                'flex': '1',
                'min-width': '120px',
                'background': 'var(--mobile-primary, #007AFF)',
                'color': 'var(--mobile-primary-text, #FFFFFF)',
                'border': 'none',
                'border-radius': 'var(--mobile-radius, 8px)',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '600',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                'touch-action': 'manipulation'
            },
            
            'SUBMIT:hover': {
                'background': 'var(--mobile-primary-hover, #0056CC)',
                'transform': 'translateY(-1px)',
                'box-shadow': '0 4px 8px rgba(0, 122, 255, 0.3)'
            },
            
            'SUBMIT:active': {
                'transform': 'translateY(0)',
                'box-shadow': '0 2px 4px rgba(0, 122, 255, 0.3)'
            },
            
            'SUBMIT:disabled': {
                'background': 'var(--mobile-disabled, #C7C7CC)',
                'color': 'var(--mobile-disabled-text, #8E8E93)',
                'cursor': 'not-allowed',
                'transform': 'none',
                'box-shadow': 'none'
            },
            
            RESET: {
                'flex': '1',
                'min-width': '120px',
                'background': 'transparent',
                'color': 'var(--mobile-text-secondary, #8E8E93)',
                'border': '1px solid var(--mobile-border, #C7C7CC)',
                'border-radius': 'var(--mobile-radius, 8px)',
                'padding': 'var(--mobile-spacing-md, 12px) var(--mobile-spacing-lg, 16px)',
                'font-size': 'var(--mobile-font-md, 16px)',
                'font-weight': '500',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                'touch-action': 'manipulation'
            },
            
            'RESET:hover': {
                'border-color': 'var(--mobile-primary, #007AFF)',
                'color': 'var(--mobile-primary, #007AFF)',
                'background': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.1))'
            },
            
            'RESET:active': {
                'background': 'var(--mobile-primary-light, rgba(0, 122, 255, 0.2))'
            },
            
            // 错误状态样式
            'ITEM-error FIELDINPUT, ITEM-error FIELDTEXTAREA, ITEM-error FIELDSELECT': {
                'border-color': 'var(--mobile-danger, #FF3B30) !important',
                'box-shadow': '0 0 0 2px var(--mobile-danger-light, rgba(255, 59, 48, 0.1)) !important'
            },
            
            // 响应式断点样式
            '@media (max-width: 479px)': {
                'ACTIONS': {
                    'flex-direction': 'column',
                    'gap': 'var(--mobile-spacing-sm, 8px)'
                },
                'SUBMIT, RESET': {
                    'width': '100%',
                    'flex': 'none'
                }
            },
            
            '@media (min-width: 768px) and (max-width: 1023px)': {
                'ITEMS': {
                    'display': 'grid',
                    'grid-template-columns': 'repeat(auto-fit, minmax(250px, 1fr))',
                    'gap': 'var(--mobile-spacing-md, 12px)'
                }
            },
            
            // 主题支持
            '[data-theme="dark"]': {
                '--mobile-primary': '#0A84FF',
                '--mobile-primary-hover': '#0056CC',
                '--mobile-primary-light': 'rgba(10, 132, 255, 0.1)',
                '--mobile-text-secondary': '#8E8E93',
                '--mobile-border': '#38383A',
                '--mobile-danger': '#FF453A',
                '--mobile-danger-light': 'rgba(255, 69, 58, 0.1)'
            },
            
            '[data-theme="highcontrast"]': {
                '--mobile-primary': '#0000FF',
                '--mobile-primary-hover': '#000080',
                '--mobile-text-secondary': '#000000',
                '--mobile-border': '#000000',
                '--mobile-danger': '#FF0000'
            }
        },
        
        // 四分离架构 - 数据模型（DataModel）
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '表单标题',
                ini: '表单',
                action: function(value) {
                    var profile = this;
                    // 更新formLabel属性保持同步
                    profile.properties.formLabel = value;
                    profile.getRoot().attr('aria-label', value || '表单');
                }
            },
            
            // 继承自UI基类的基本属性
            width: {
                caption: '表单宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            height: {
                caption: '表单高度',
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
            
            // ===== 表单特有属性 =====
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    var profile = this;
                    var submit = profile.getSubNode('SUBMIT');
                    var reset = profile.getSubNode('RESET');
                    
                    if (submit && !submit.isEmpty()) {
                        submit.prop('disabled', value);
                    }
                    if (reset && !reset.isEmpty()) {
                        reset.prop('disabled', value);
                    }
                }
            },
            
            // 表单特有属性
            showReset: {
                ini: true,
                action: function(value) {
                    this.getRoot().removeClass('form-hide-reset');
                    if (!value) {
                        this.getRoot().addClass('form-hide-reset');
                    }
                }
            },
            
            submitText: {
                ini: '提交',
                action: function(value) {
                    var submit = this.getSubNode('SUBMIT');
                    if (submit && !submit.isEmpty()) {
                        submit.text(value);
                    }
                }
            },
            
            resetText: {
                ini: '重置',
                action: function(value) {
                    var reset = this.getSubNode('RESET');
                    if (reset && !reset.isEmpty()) {
                        reset.text(value);
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
            
            // 验证相关
            autoValidate: {
                ini: true
            },
            
            validateOnSubmit: {
                ini: true
            },
            
            showValidationSummary: {
                ini: false
            },
            
            // 可访问性支持
            formLabel: {
                ini: '表单',
                action: function(value) {
                    this.getRoot().attr('aria-label', value);
                }
            },
            
            // 触摸事件支持
            enableLongPress: {
                ini: false
            },
            
            longPressDelay: {
                ini: 500
            },
            
            // 动画效果
            enableAnimations: {
                ini: true
            },
            
            // 继承自UI基类的其他属性
            tabindex: {
                ini: -1
            },
            
            // 支持容器功能（继承自absContainer）
            childrenAlign: {
                ini: 'stretch'
            },
            
            overflow: {
                ini: 'visible'
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
                           localStorage.getItem('ood-mobile-form-theme') || 
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
            
            // 准备表单特定数据
            data._resetDisplay = props.showReset ? 'inline-flex' : 'none';
            data._submitText = props.submitText || '提交';
            data._resetText = props.resetText || '重置';
            
            // 主题相关数据
            data._theme = props.theme || 'light';
            data._themeClass = props.theme && props.theme !== 'light' ? ('mobile-theme-' + props.theme) : '';
            
            // 响应式数据
            data._screenSize = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.getScreenSize() : 'md';
            data._isMobile = ood.Mobile && ood.Mobile.utils ? ood.Mobile.utils.isMobile() : true;
            
            // 可访问性数据
            data._formLabel = props.formLabel || '表单';
            
            // 合并className
            var classNames = ['ood-mobile-form', 'ood-mobile-component'];
            if (data._themeClass) classNames.push(data._themeClass);
            if (data._isMobile) classNames.push('mobile-device');
            classNames.push('form-' + data._screenSize);
            if (props.className) classNames.push(props.className);
            
            data._className = classNames.join(' ');
            
            return data;
        },
        
        // 准备单个表单项数据 - 处理所有模板变量
        _prepareItem: function(profile, dataItem, item, pid, i, l, mapCache, serialId) {
            // 调用父类方法
            if (arguments.callee.upper) {
                arguments.callee.upper.call(this, profile, dataItem, item, pid, i, l, mapCache, serialId);
            }
            
            var props = profile.properties;
            var hasError = item.hasError || false;
            var isDisabled = item.disabled || props.disabled || false;
            var isReadonly = item.readonly || false;
            
            // 基础数据属性
            dataItem.id = item.id || 'form-field-' + i;
            dataItem.fieldName = item.fieldName || item.name || '';
            dataItem.label = item.label || item.caption || '';
            dataItem.fieldType = item.fieldType || 'text';
            dataItem.fieldId = item.fieldId || (dataItem.fieldName + '-field');
            dataItem.placeholder = item.placeholder || '';
            dataItem.required = item.required ? 'required' : '';
            dataItem.readonly = isReadonly ? 'readonly' : '';
            dataItem.disabled = isDisabled ? 'disabled' : '';
            dataItem.errorMessage = item.errorMessage || '';
            
            // 状态类和样式
            dataItem._itemState = this._buildItemStateClass(hasError, isDisabled, isReadonly);
            dataItem._itemDisplay = 'display: flex;';
            
            // 显示控制变量
            var isTextarea = dataItem.fieldType === 'textarea';
            var isSelect = dataItem.fieldType === 'select';
            
            dataItem._textareaDisplay = isTextarea ? 'display: block;' : 'display: none;';
            dataItem._selectDisplay = isSelect ? 'display: block;' : 'display: none;';
            dataItem._errorDisplay = dataItem.errorMessage ? 'display: block;' : 'display: none;';
            
            // 自定义样式
            dataItem.itemClass = item.itemClass || item.className || '';
            dataItem.itemStyle = this._buildItemStyle(item, props);
            
            return dataItem;
        },
        
        // 构建项目状态类
        _buildItemStateClass: function(hasError, isDisabled, isReadonly) {
            var classes = [];
            if (hasError) classes.push('error');
            if (isDisabled) classes.push('disabled');
            if (isReadonly) classes.push('readonly');
            return classes.join(' ');
        },
        
        // 构建项目样式
        _buildItemStyle: function(item, props) {
            var styles = [];
            
            // 基础项目样式
            if (item.itemStyle) {
                styles.push(item.itemStyle);
            }
            
            // 自定义宽度
            if (item.width) {
                styles.push('width: ' + item.width);
            }
            
            return styles.length > 0 ? styles.join('; ') + ';' : '';
        },
        
        // 四分离架构 - 行为定义（Behaviors）
        Behaviors: {
            // 鼠标悬停效果支持
            HoverEffected: {
                KEY: 'SUBMIT,RESET'
            },
            
            // 支持面板操作（继承自UI）
            PanelKeys: ['KEY'],
            
            // 支持拖放操作（可选）
            DroppableKeys: ['KEY'],
            
            // 表单提交行为
            onSubmit: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 验证表单
                if (props.validateOnSubmit && !profile.boxing().validate()) {
                    return false;
                }
                
                // 触发提交事件
                if (profile.onSubmit) {
                    var formData = profile.boxing().getFormData();
                    return profile.boxing().onSubmit(profile, formData, e);
                }
                
                return true;
            },
            
            // 表单重置行为
            onReset: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 触发重置事件
                if (profile.onReset) {
                    return profile.boxing().onReset(profile, e);
                }
                
                return true;
            },
            
            // 点击事件处理
            onClick: function(profile, e, src) {
                var props = profile.properties;
                if (props.disabled) return false;
                
                // 根据点击目标处理不同逻辑
                var target = ood(e.target);
                
                if (target.hasClass('ood-mobile-form-submit')) {
                    return this.onSubmit(profile, e, src);
                } else if (target.hasClass('ood-mobile-form-reset')) {
                    return this.onReset(profile, e, src);
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
            // 表单提交事件
            onSubmit: function(profile, event) {
                // 子类可以重写此方法来处理表单提交
                console.log('表单提交');
                return true;
            },
            
            // 表单重置事件
            onReset: function(profile, event) {
                // 子类可以重写此方法来处理表单重置
                console.log('表单重置');
                return true;
            },
            
            // 表单验证事件
            onValidate: function(profile, isValid, errors) {
                // 验证结果处理
                return true;
            },
            
            // 字段值变化事件
            onFieldChange: function(profile, fieldName, newValue, oldValue) {
                // 字段值变化处理
                return true;
            },
            
            // 主题变化事件
            onThemeChange: function(profile, oldTheme, newTheme) {
                // 主题切换处理
                console.log('主题切换:', oldTheme, '->', newTheme);
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
                console.error('表单错误:', error, context);
                return true;
            }
        },

        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Form组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                formNode = profile.getSubNode('FORM'),
                fieldsContainer = profile.getSubNode('FIELDS'),
                actionsContainer = profile.getSubNode('ACTIONS'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整表单容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    formNode.css('width', '100%');
                }
            }

            // 如果提供了高度，调整表单容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    formNode.css('height', '100%');
                    
                    // 调整字段容器的高度以适应表单
                    var actionsHeight = actionsContainer.offsetHeight(true) || 0;
                    var availableHeight = pxHeight - actionsHeight;
                    
                    fieldsContainer.css('height', adjustunit(availableHeight));
                }
            }

            // 根据新的尺寸调整布局
            this.boxing().adjustLayout();
        }
    }
});