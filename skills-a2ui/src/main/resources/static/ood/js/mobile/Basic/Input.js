/**
 * 移动端输入框组件
 * 继承自 ood.UI（UI基类）和 ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持多种输入类型、验证、格式化和移动端优化
 */
ood.Class("ood.Mobile.Input", ["ood.UI", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['CONTAINER', 'LABEL', 'INPUT', 'CLEAR', 'HELP', 'ERROR']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            width: '18em',
            height: '2.5em',
            caption: '输入框',
            placeholder: '请输入内容',
            type: 'text',
            theme: 'light',
            responsive: true
        },

        Initialize: function() {
            // 调用父类初始化
         //   this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化移动端输入框特性
            this.initMobileInputFeatures();
            
            // 自动注册到主题管理器
            if (typeof ood.Mobile !== 'undefined' && ood.Mobile.ThemeManager) {
                ood.Mobile.ThemeManager.register(this);
                ood.Mobile.ThemeManager.ResponsiveManager.register(this);
            }
        },
        
        initMobileInputFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            // 添加移动端输入框CSS类
            profile.getRoot().addClass('ood-mobile-input ood-mobile-component');
            
            // 初始化输入框功能
            this.initInputFeatures();
            
            // 初始化触摸事件
            this.bindTouchEvents();
            
            // 初始化响应式
            this.initResponsive();
            
            // 初始化可访问性
            this.initAccessibility();
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
            
            var input = profile.getSubNode('INPUT');
            var label = profile.getSubNode('LABEL');
            
            // 为输入框添加ARIA属性
            if (input && !input.isEmpty()) {
                var inputId = profile.getDomId() + '_input';
                input.attr({
                    'id': inputId,
                    'aria-describedby': profile.getDomId() + '_help',
                    'aria-invalid': 'false'
                });
                
                // 关联标签
                if (label && !label.isEmpty()) {
                    label.attr('for', inputId);
                }
            }
        },
        
        // 响应式布局调整
        adjustLayout: function() {
            return this.each(function(profile) {
                var root = profile.getRoot();
                var screenSize = ood.Mobile.utils.getScreenSize();
                var width = window.innerWidth;
                
                // 清除旧的尺寸类
                root.removeClass('input-xs input-sm input-md input-lg input-xl');
                
                // 添加当前尺寸类
                root.addClass('input-' + screenSize);
                
                // 超小屏幕特殊处理
                if (width < 480) {
                    root.addClass('input-tiny');
                } else {
                    root.removeClass('input-tiny');
                }
            });
        },
        // 重写 _setCtrlValue 方法 - 设置输入框的值
        _setCtrlValue: function(value) {
            return this.each(function(profile) {
                var inputNode = profile.getSubNode('INPUT');
                if (inputNode && inputNode.get(0)) {
                    inputNode.get(0).value = value || '';
                    
                    // 触发变化事件以更新UI状态
                    var changeEvent = document.createEvent('Event');
                    changeEvent.initEvent('input', true, true);
                    inputNode.get(0).dispatchEvent(changeEvent);
                }
            });
        },
        
        // 重写 _getCtrlValue 方法 - 获取输入框的值
        _getCtrlValue: function() {
            var profile = this.get(0);
            if (!profile) return '';
            
            var inputNode = profile.getSubNode('INPUT');
            if (inputNode && inputNode.get(0)) {
                return inputNode.get(0).value || '';
            }
            return '';
        },
        
        // 重写 _setDirtyMark 方法 - 设置输入框的脏标记
        _setDirtyMark: function(key) {
            return this.each(function(profile) {
                if (!profile.renderId) return;
                
                var properties = profile.properties;
                var flag = properties.value !== properties.$UIvalue;
                var container = profile.getRoot();
                var dirtyClass = 'ood-mobile-input-dirty';
                
                if (profile._dirtyFlag !== flag) {
                    if (properties.dirtyMark && properties.showDirtyMark) {
                        if (profile.beforeDirtyMark && false === profile.boxing().beforeDirtyMark(profile, flag)) {
                            // 不做处理
                        } else {
                            if (flag) {
                                container.addClass(dirtyClass);
                            } else {
                                container.removeClass(dirtyClass);
                            }
                        }
                    }
                    profile._dirtyFlag = flag;
                }
            });
        },
        
        // 激活输入框（聚焦）
        activate: function() {
            var profile = this.get(0);
            if (profile) {
                var inputNode = profile.getSubNode('INPUT');
                if (inputNode && inputNode.get(0)) {
                    inputNode.get(0).focus();
                }
            }
            return this;
        },
        
        // 组件初始化
        initInputFeatures: function() {
            // 原有的输入框特性初始化逻辑
            this.initInputState();
            this.initValidation();
            this.initFormatting();
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
            
            // 聚焦事件 - 符合 ood 三阶段事件机制
            profile.beforeFocus = function(profile, e, src) {
                if (profile.properties.disabled || profile.properties.readonly) {
                    return false;
                }
                return true;
            };
            
            profile.onFocus = function(profile, e, src) {
                var container = profile.getRoot();
                container.addClass('ood-mobile-input-focused');
                self.handleInputFocus(profile, e);
            };
            
            profile.afterFocus = function(profile, e, src) {
                // 聚焦后的处理
                self.afterInputFocus(profile, e);
            };
            
            // 失焦事件
            profile.beforeBlur = function(profile, e, src) {
                return true;
            };
            
            profile.onBlur = function(profile, e, src) {
                var container = profile.getRoot();
                container.removeClass('ood-mobile-input-focused');
                self.handleInputBlur(profile, e);
                self.validateInput();
            };
            
            profile.afterBlur = function(profile, e, src) {
                // 失焦后的处理
                self.afterInputBlur(profile, e);
            };
            
            // 输入变化事件
            profile.beforeChange = function(profile, e, src) {
                return true;
            };
            
            profile.onChange = function(profile, e, src) {
                self.handleInputChange(profile, e);
                self.formatInput();
                self.validateInput();
            };
            
            profile.afterChange = function(profile, e, src) {
                // 变化后的处理
                self.afterInputChange(profile, e);
            };
            
            // 键盘事件
            profile.beforeKeydown = function(profile, e, src) {
                return true;
            };
            
            profile.onKeydown = function(profile, e, src) {
                self.handleKeydown(profile, e);
            };
            
            profile.afterKeydown = function(profile, e, src) {
                // 键盘事件后的处理
            };
            
            // 点击事件（针对清除按钮等）
            profile.beforeClick = function(profile, e, src) {
                return true;
            };
            
            profile.onClick = function(profile, e, src) {
                // 处理点击事件，如清除按钮
                var target = e.target || e.srcElement;
                if (ood(target).hasClass('ood-mobile-input-clear') || ood(target).closest('.ood-mobile-input-clear').length > 0) {
                    e.preventDefault();
                    self.clearInput();
                }
            };
            
            profile.afterClick = function(profile, e, src) {
                // 点击后的处理
            };
        },
        
        // 初始化输入框状态
        initInputState: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 设置输入框类型
            this.setInputType(properties.type || 'text');
            
            // 设置初始值
            if (properties.value) {
                this.setValue(properties.value);
            }
            
            // 设置占位符
            if (properties.placeholder) {
                this.setPlaceholder(properties.placeholder);
            }
            
            // 设置禁用状态
            if (properties.disabled) {
                this.setDisabled(true);
            }
            
            // 设置只读状态
            if (properties.readonly) {
                this.setReadonly(true);
            }
        },
        
        // 初始化验证
        initValidation: function() {
            this._validators = [];
            this._isValid = true;
            this._errorMessage = '';
            
            // 添加内置验证规则
            this.addBuiltinValidators();
        },
        
        // 添加内置验证规则
        addBuiltinValidators: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 必填验证
            if (properties.required) {
                this.addValidator('required', function(value) {
                    return value && value.trim().length > 0;
                }, '此字段为必填项');
            }
            
            // 最小长度验证
            if (properties.minLength) {
                this.addValidator('minLength', function(value) {
                    return !value || value.length >= properties.minLength;
                }, '输入长度不能少于' + properties.minLength + '个字符');
            }
            
            // 最大长度验证
            if (properties.maxLength) {
                this.addValidator('maxLength', function(value) {
                    return !value || value.length <= properties.maxLength;
                }, '输入长度不能超过' + properties.maxLength + '个字符');
            }
            
            // 邮箱验证
            if (properties.type === 'email') {
                this.addValidator('email', function(value) {
                    if (!value) return true;
                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    return emailRegex.test(value);
                }, '请输入有效的邮箱地址');
            }
            
            // 手机号验证
            if (properties.type === 'tel') {
                this.addValidator('tel', function(value) {
                    if (!value) return true;
                    var telRegex = /^1[3-9]\d{9}$/;
                    return telRegex.test(value);
                }, '请输入有效的手机号码');
            }
            
            // 数字验证
            if (properties.type === 'number') {
                this.addValidator('number', function(value) {
                    if (!value) return true;
                    return !isNaN(value);
                }, '请输入有效的数字');
            }
        },
        
        // 添加验证器
        addValidator: function(name, validator, message) {
            this._validators.push({
                name: name,
                validator: validator,
                message: message
            });
        },
        
        // 移除验证器
        removeValidator: function(name) {
            this._validators = this._validators.filter(function(v) {
                return v.name !== name;
            });
        },
        
        // 验证输入
        validateInput: function() {
            var value = this.getValue();
            var isValid = true;
            var errorMessage = '';
            
            for (var i = 0; i < this._validators.length; i++) {
                var validator = this._validators[i];
                if (!validator.validator(value)) {
                    isValid = false;
                    errorMessage = validator.message;
                    break;
                }
            }
            
            this._isValid = isValid;
            this._errorMessage = errorMessage;
            
            // 更新UI状态
            this.updateValidationState();
            
            return isValid;
        },
        
        // 更新验证状态UI
        updateValidationState: function() {
            var profile = this.get(0);
            var container = profile.getRoot();
            var errorNode = profile.getSubNode('ERROR');
            
            if (this._isValid) {
                container.removeClass('ood-mobile-input-error');
                errorNode.css('display', 'none');
            } else {
                container.addClass('ood-mobile-input-error');
                errorNode.html(this._errorMessage);
                errorNode.css('display', 'block');
            }
        },
        
        // 初始化格式化
        initFormatting: function() {
            var profile = this.get(0);
            var properties = profile.properties;
            
            // 根据类型设置格式化器
            if (properties.formatter) {
                this._formatter = properties.formatter;
            } else {
                this._formatter = this.getDefaultFormatter(properties.type);
            }
        },
        
        // 获取默认格式化器
        getDefaultFormatter: function(type) {
            switch(type) {
                case 'tel':
                    return this.formatPhoneNumber;
                case 'number':
                    return this.formatNumber;
                case 'currency':
                    return this.formatCurrency;
                default:
                    return null;
            }
        },
        
        // 格式化输入
        formatInput: function() {
            if (!this._formatter) return;
            
            var value = this.getValue();
            var formattedValue = this._formatter.call(this, value);
            
            if (formattedValue !== value) {
                this.setValue(formattedValue);
            }
        },
        
        // 手机号格式化
        formatPhoneNumber: function(value) {
            if (!value) return value;
            
            // 移除所有非数字字符
            var numbers = value.replace(/\D/g, '');
            
            // 限制长度
            if (numbers.length > 11) {
                numbers = numbers.substring(0, 11);
            }
            
            // 格式化为 xxx xxxx xxxx
            if (numbers.length > 7) {
                return numbers.substring(0, 3) + ' ' + numbers.substring(3, 7) + ' ' + numbers.substring(7);
            } else if (numbers.length > 3) {
                return numbers.substring(0, 3) + ' ' + numbers.substring(3);
            }
            
            return numbers;
        },
        
        // 数字格式化
        formatNumber: function(value) {
            if (!value) return value;
            
            // 移除非数字和小数点字符
            var numbers = value.replace(/[^\d.]/g, '');
            
            // 确保只有一个小数点
            var parts = numbers.split('.');
            if (parts.length > 2) {
                numbers = parts[0] + '.' + parts.slice(1).join('');
            }
            
            return numbers;
        },
        
        // 货币格式化
        formatCurrency: function(value) {
            if (!value) return value;
            
            var numbers = this.formatNumber(value);
            if (!numbers) return numbers;
            
            var num = parseFloat(numbers);
            if (isNaN(num)) return numbers;
            
            return num.toFixed(2);
        },
        
        // 设置输入框类型
        setInputType: function(type) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.attr('type', type);
            profile.properties.type = type;
            
            // 移动端优化
            this.setMobileAttributes(type);
        },
        
        // 设置移动端属性
        setMobileAttributes: function(type) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            switch(type) {
                case 'tel':
                    input.attr('inputmode', 'tel');
                    input.attr('pattern', '[0-9]*');
                    break;
                case 'number':
                    input.attr('inputmode', 'numeric');
                    input.attr('pattern', '[0-9]*');
                    break;
                case 'email':
                    input.attr('inputmode', 'email');
                    break;
                case 'url':
                    input.attr('inputmode', 'url');
                    break;
                case 'search':
                    input.attr('inputmode', 'search');
                    break;
            }
        },
        
        // 设置值
        setValue: function(value) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.val(value || '');
            profile.properties.value = value;
            
            // 更新清除按钮显示
            this.updateClearButton();
            
            // 触发change事件
            this.onInputChange();
        },
        
        // 获取值
        getValue: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            return input.val() || '';
        },
        
        // 设置占位符
        setPlaceholder: function(placeholder) {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.attr('placeholder', placeholder);
            profile.properties.placeholder = placeholder;
        },
        
        // 设置禁用状态
        setDisabled: function(disabled) {
            return this.each(function(profile) {
                var container = profile.getRoot();
                var input = profile.getSubNode('INPUT');
                
                profile.properties.disabled = disabled;
                
                if (disabled) {
                    container.addClass('ood-mobile-input-disabled');
                    input.attr('disabled', true);
                } else {
                    container.removeClass('ood-mobile-input-disabled');
                    input.removeAttr('disabled');
                }
            });
        },
        
        // 设置只读状态
        setReadonly: function(readonly) {
            return this.each(function(profile) {
                var container = profile.getRoot();
                var input = profile.getSubNode('INPUT');
                
                profile.properties.readonly = readonly;
                
                if (readonly) {
                    container.addClass('ood-mobile-input-readonly');
                    input.attr('readonly', true);
                } else {
                    container.removeClass('ood-mobile-input-readonly');
                    input.removeAttr('readonly');
                }
            });
        },
        
        // 清除输入
        clearInput: function() {
            this.setValue('');
            this.focus();
        },
        
        // 聚焦
        focus: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            setTimeout(function() {
                input.get(0).focus();
            }, 0);
        },
        
        // 失焦
        blur: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            input.get(0).blur();
        },
        
        // 更新清除按钮显示
        updateClearButton: function() {
            var profile = this.get(0);
            var clearBtn = profile.getSubNode('CLEAR');
            var value = this.getValue();
            
            if (value && value.length > 0 && !profile.properties.disabled && !profile.properties.readonly) {
                clearBtn.css('display', 'block');
            } else {
                clearBtn.css('display', 'none');
            }
        },
        
        // 输入框聚焦事件处理 - 符合 ood 规范
        handleInputFocus: function(profile, e) {
            // 触发用户自定义事件
            if (profile.properties.onFocus && typeof profile.properties.onFocus === 'function') {
                profile.properties.onFocus.call(this, profile, e);
            }
        },
        
        // 聚焦后处理
        afterInputFocus: function(profile, e) {
            if (profile.properties.onAfterFocus && typeof profile.properties.onAfterFocus === 'function') {
                profile.properties.onAfterFocus.call(this, profile, e);
            }
        },
        
        // 输入框失焦事件处理 - 符合 ood 规范
        handleInputBlur: function(profile, e) {
            // 触发用户自定义事件
            if (profile.properties.onBlur && typeof profile.properties.onBlur === 'function') {
                profile.properties.onBlur.call(this, profile, e);
            }
        },
        
        // 失焦后处理
        afterInputBlur: function(profile, e) {
            if (profile.properties.onAfterBlur && typeof profile.properties.onAfterBlur === 'function') {
                profile.properties.onAfterBlur.call(this, profile, e);
            }
        },
        
        // 输入变化事件处理 - 符合 ood 规范 
        handleInputChange: function(profile, e) {
            var value = this.getValue();
            
            // 更新清除按钮显示
            this.updateClearButton();
            
            // 触发用户自定义事件
            if (profile.properties.onChange && typeof profile.properties.onChange === 'function') {
                profile.properties.onChange.call(this, profile, e, value);
            }
        },
        
        // 变化后处理
        afterInputChange: function(profile, e) {
            if (profile.properties.onAfterChange && typeof profile.properties.onAfterChange === 'function') {
                profile.properties.onAfterChange.call(this, profile, e);
            }
        },
        
        // 键盘事件处理
        handleKeydown: function(profile, e) {
            if (profile.properties.onKeydown && typeof profile.properties.onKeydown === 'function') {
                profile.properties.onKeydown.call(this, profile, e);
            }
        },
        
        // 检查是否为交互式组件
        isInteractive: function() {
            return true;
        },
        
        // 检查是否为动态组件
        isDynamic: function() {
            return true; // 输入框内容会变化
        },
        
        // 激活事件处理（键盘激活）
        onActivate: function(e) {
            if (!this.get(0).properties.disabled && !this.get(0).properties.readonly) {
                this.focus();
            }
        },
        
        // 主题变化事件处理
        onThemeChange: function(oldTheme, newTheme) {
            var profile = this.get(0);
            var root = profile.getRoot();
            
            // 移除旧主题类
            root.removeClass('input-theme-' + oldTheme);
            
            // 添加新主题类
            if (newTheme && newTheme !== 'light') {
                root.addClass('input-theme-' + newTheme);
            }
        },
        
        // 移动端布局事件
        onMobileLayout: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            // 移动端增大输入框尺寸
            input.css('min-height', 'var(--mobile-touch-target-lg)');
            input.css('font-size', 'var(--mobile-font-lg)');
        },
        
        // 桌面端布局事件
        onDesktopLayout: function() {
            var profile = this.get(0);
            var input = profile.getSubNode('INPUT');
            
            // 恢复默认尺寸
            input.css('min-height', 'var(--mobile-touch-target)');
            input.css('font-size', 'var(--mobile-font-md)');
        },
        
        // 添加屏幕阅读器专用文本
        addScreenReaderText: function() {
            var profile = this.get(0);
            var root = profile.getRoot();
            var properties = profile.properties;
            
            // 为必填字段添加说明
            if (properties.required) {
                var srRequired = ood('<span class="mobile-sr-only">必填字段</span>');
                root.append(srRequired);
            }
            
            // 为错误状态添加说明
            if (!this._isValid && this._errorMessage) {
                var srError = ood('<span class="mobile-sr-only">输入错误：' + this._errorMessage + '</span>');
                root.append(srError);
            }
        },
    },
    
    Static: {
        // 模板定义
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-input {_sizeClass} {_statusClass}',
            style: '{_style}',
            
            LABEL: {
                tagName: 'label',
                className: 'ood-mobile-input-label',
                text: '{label}',
                style: 'display: {_labelDisplay}'
            },
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-input-container',
                
                PREFIX: {
                    tagName: 'div',
                    className: 'ood-mobile-input-prefix',
                    style: 'display: {_prefixDisplay}',
                    text: '{prefix}'
                },
                
                INPUT: {
                    tagName: 'input',
                    className: 'ood-mobile-input-field',
                    type: '{type}',
                    value: '{value}',
                    placeholder: '{placeholder}',
                    maxlength: '{maxLength}',
                    minlength: '{minLength}'
                },
                
                SUFFIX: {
                    tagName: 'div',
                    className: 'ood-mobile-input-suffix',
                    style: 'display: {_suffixDisplay}',
                    text: '{suffix}'
                },
                
                CLEAR: {
                    tagName: 'button',
                    className: 'ood-mobile-input-clear',
                    type: 'button',
                    style: 'display: none',
                    
                    ICON: {
                        tagName: 'i',
                        className: 'ood-mobile-input-clear-icon'
                    }
                }
            },
            
            ERROR: {
                tagName: 'div',
                className: 'ood-mobile-input-error-message',
                style: 'display: none'
            },
            
            HINT: {
                tagName: 'div',
                className: 'ood-mobile-input-hint',
                text: '{hint}',
                style: 'display: {_hintDisplay}'
            }
        },
        
        // 外观样式定义
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'margin-bottom': 'var(--mobile-spacing-md)'
            },
            
            // 标签
            LABEL: {
                display: 'block',
                'margin-bottom': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-sm)',
                'font-weight': '500',
                color: 'var(--mobile-text-secondary)'
            },
            
            // 输入容器
            CONTAINER: {
                position: 'relative',
                display: 'flex',
                'align-items': 'center',
                'background-color': 'var(--mobile-bg-primary)',
                border: '1px solid var(--mobile-border-color)',
                'border-radius': 'var(--mobile-border-radius)',
                overflow: 'hidden',
                transition: 'all 0.2s ease-in-out'
            },
            
            // 前缀
            PREFIX: {
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)',
                'white-space': 'nowrap'
            },
            
            // 输入字段
            INPUT: {
                flex: '1',
                border: 'none',
                outline: 'none',
                'background-color': 'transparent',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-text-primary)',
                'line-height': '1.4',
                'min-height': 'var(--mobile-touch-target)'
            },
            
            // 后缀
            SUFFIX: {
                padding: '0 var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)',
                'white-space': 'nowrap'
            },
            
            // 清除按钮
            CLEAR: {
                position: 'absolute',
                right: 'var(--mobile-spacing-sm)',
                width: '20px',
                height: '20px',
                border: 'none',
                'background-color': 'transparent',
                cursor: 'pointer',
                'border-radius': '50%',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center'
            },
            
            // 清除图标
            'CLEAR ICON': {
                'font-size': '14px',
                color: 'var(--mobile-text-tertiary)'
            },
            
            // 错误消息
            ERROR: {
                'margin-top': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-danger)',
                'line-height': '1.4'
            },
            
            // 提示信息
            HINT: {
                'margin-top': 'var(--mobile-spacing-xs)',
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-quaternary)',
                'line-height': '1.4'
            },
            
            // 聚焦状态
            'KEY.ood-mobile-input-focused CONTAINER': {
                'border-color': 'var(--mobile-primary)',
                'box-shadow': '0 0 0 2px rgba(0, 122, 255, 0.1)'
            },
            
            // 错误状态
            'KEY.ood-mobile-input-error CONTAINER': {
                'border-color': 'var(--mobile-danger)',
                'box-shadow': '0 0 0 2px rgba(255, 59, 48, 0.1)'
            },
            
            // 禁用状态
            'KEY.ood-mobile-input-disabled': {
                opacity: 'var(--mobile-disabled-opacity, 0.5)',
                'pointer-events': 'none'
            },
            
            'KEY.ood-mobile-input-disabled CONTAINER': {
                'background-color': 'var(--mobile-disabled-bg)'
            },
            
            // 只读状态
            'KEY.ood-mobile-input-readonly CONTAINER': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            // 主题支持 - 暗黑模式
            'KEY.input-theme-dark CONTAINER': {
                'background-color': 'var(--mobile-bg-tertiary)',
                'border-color': 'var(--mobile-border-color)'
            },
            
            'KEY.input-theme-dark INPUT': {
                color: 'var(--mobile-text-primary)'
            },
            
            // 主题支持 - 高对比度
            'KEY.input-theme-light-hc CONTAINER, KEY.input-theme-dark-hc CONTAINER': {
                'border-width': 'var(--mobile-border-width)',
                'border-style': 'solid'
            },
            
            'KEY.input-theme-light-hc.ood-mobile-input-focused CONTAINER, KEY.input-theme-dark-hc.ood-mobile-input-focused CONTAINER': {
                'box-shadow': '0 0 0 3px rgba(0, 122, 255, 0.3)'
            },
            
            // 聚焦状态
            'KEY INPUT:focus-visible': {
                outline: 'none'
            },
            
            'KEY:focus-within CONTAINER': {
                'border-color': 'var(--mobile-primary)',
                'box-shadow': '0 0 0 2px rgba(0, 122, 255, 0.1)'
            },
            
            // 高对比度聚焦状态
            'KEY.input-theme-light-hc:focus-within CONTAINER, KEY.input-theme-dark-hc:focus-within CONTAINER': {
                'box-shadow': '0 0 0 3px rgba(0, 122, 255, 0.3)'
            },
            
            // 移动端响应式样式
            'KEY.mobile-responsive INPUT': {
                'min-height': 'var(--mobile-touch-target-lg)',
                'font-size': 'var(--mobile-font-lg)',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)'
            },
            
            'KEY.mobile-tiny INPUT': {
                'font-size': 'var(--mobile-font-md)',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)'
            },
            
            // 尺寸样式
            'KEY.ood-mobile-input-sm INPUT': {
                'min-height': '32px',
                padding: 'var(--mobile-spacing-xs) var(--mobile-spacing-sm)',
                'font-size': 'var(--mobile-font-sm)'
            },
            
            'KEY.ood-mobile-input-lg INPUT': {
                'min-height': '48px',
                padding: 'var(--mobile-spacing-md) var(--mobile-spacing-lg)',
                'font-size': 'var(--mobile-font-lg)'
            }
        },
        
        // 行为定义
        Behaviors: {
            HotKeyAllowed: true,
            DroppableKeys: []
        },
        
        // 数据模型
        DataModel: {
            // ===== 基础必需属性 =====
            // 标签文本（显示值）- 对应模板中的 {label} 或 {caption}
            caption: {
                caption: '输入框标签',
                ini: '输入框',
                action: function(value) {
                    var profile = this;
                    var labelNode = profile.getSubNode('LABEL');
                    if (labelNode && !labelNode.isEmpty()) {
                        labelNode.html(value || '');
                    }
                }
            },
            
            // 输入框宽度
            width: {
                caption: '输入框宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            // 输入框高度
            height: {
                caption: '输入框高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            // 标签颜色（颜色选择器）
            labelColor: {
                caption: '标签颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var labelNode = this.getSubNode('LABEL');
                        if (labelNode && !labelNode.isEmpty()) {
                            labelNode.css('color', value);
                        }
                    }
                }
            },
            
            // 输入框边框色（颜色选择器）
            borderColor: {
                caption: '边框颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    if (value) {
                        var inputNode = this.getSubNode('INPUT');
                        if (inputNode && !inputNode.isEmpty()) {
                            inputNode.css('border-color', value);
                        }
                    }
                }
            },
            
            // 前缀图标（图标选择器）
            prefixIcon: {
                caption: '前缀图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var prefixNode = profile.getSubNode('PREFIX_ICON');
                    
                    if (value) {
                        if (prefixNode && !prefixNode.isEmpty()) {
                            prefixNode.attr('class', 'ood-mobile-input-prefix-icon ' + value);
                            prefixNode.css('display', 'block');
                        }
                    } else {
                        if (prefixNode && !prefixNode.isEmpty()) {
                            prefixNode.css('display', 'none');
                        }
                    }
                }
            },
            
            // 后缀图标（图标选择器）
            suffixIcon: {
                caption: '后缀图标',
                ini: '',
                combobox: function() {
                    return 'FONTICON';
                },
                action: function(value) {
                    var profile = this;
                    var suffixNode = profile.getSubNode('SUFFIX_ICON');
                    
                    if (value) {
                        if (suffixNode && !suffixNode.isEmpty()) {
                            suffixNode.attr('class', 'ood-mobile-input-suffix-icon ' + value);
                            suffixNode.css('display', 'block');
                        }
                    } else {
                        if (suffixNode && !suffixNode.isEmpty()) {
                            suffixNode.css('display', 'none');
                        }
                    }
                }
            },
            
            // ===== 继承基类的主题和可访问性属性 =====
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
            
            ariaLabel: {
                caption: 'ARIA标签',
                ini: '',
                action: function(value) {
                    this.getSubNode('INPUT').attr('aria-label', value);
                }
            },
            
            ariaDescribedBy: {
                caption: 'ARIA描述关联',
                ini: '',
                action: function(value) {
                    this.getSubNode('INPUT').attr('aria-describedby', value);
                }
            },
            
            // 输入值（继承自 ood.absValue）
            value: {
                caption: '输入值',
                ini: ''
                // set 方法由 ood.absValue 提供
            },
            
            // 继承 ood.absValue 的表单字段属性
            isFormField: {
                caption: '是否为表单字段',
                ini: true
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: true
            },
            
            showDirtyMark: {
                caption: '显示脏标记',
                ini: true
            },
            
            readonly: {
                caption: '只读状态',
                ini: false,
                action: function(value) {
                    this.boxing().setReadonly(value);
                }
            },
            
            // 输入类型
            type: {
                caption: '输入类型',
                ini: 'text',
                listbox: ['text', 'password', 'email', 'tel', 'number', 'url', 'search'],
                action: function(value) {
                    this.boxing().setInputType(value);
                }
            },
            
            // 占位符
            placeholder: {
                caption: '占位符文本',
                ini: '',
                action: function(value) {
                    this.boxing().setPlaceholder(value);
                }
            },
            
            // 标签
            label: {
                caption: '输入框标签',
                ini: ''
            },
            
            // 前缀
            prefix: {
                caption: '前缀内容',
                ini: ''
            },
            
            // 后缀
            suffix: {
                caption: '后缀内容',
                ini: ''
            },
            
            // 提示信息
            hint: {
                caption: '提示信息',
                ini: ''
            },
            
            // 尺寸
            size: {
                caption: '输入框尺寸',
                ini: 'md',
                listbox: ['sm', 'md', 'lg'],
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    
                    // 移除所有尺寸类
                    root.removeClass('ood-mobile-input-sm ood-mobile-input-lg');
                    
                    // 添加新尺寸类
                    if (value && value !== 'md') {
                        root.addClass('ood-mobile-input-' + value);
                    }
                }
            },
            
            // 是否必填
            required: {
                caption: '必填验证',
                ini: false,
                action: function(value) {
                    var profile = this;
                    var root = profile.getRoot();
                    var inputNode = profile.getSubNode('INPUT');
                    
                    if (value) {
                        root.addClass('ood-mobile-input-required');
                        inputNode.attr('required', 'required');
                    } else {
                        root.removeClass('ood-mobile-input-required');
                        inputNode.removeAttr('required');
                    }
                }
            },
            
            // 最小长度
            minLength: {
                caption: '最小输入长度',
                ini: null
            },
            
            // 最大长度
            maxLength: {
                caption: '最大输入长度',
                ini: null
            },
            
            // 是否禁用
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    this.boxing().setDisabled(value);
                }
            },
            
            // 是否只读
            readonly: {
                caption: '只读状态',
                ini: false,
                action: function(value) {
                    this.boxing().setReadonly(value);
                }
            },
            
            // 格式化器
            formatter: {
                caption: '内容格式化器',
                ini: null
            },
            
            // ood 规范事件处理器
            onFocus: {
                caption: '聚焦事件处理器',
                ini: null
            },
            
            onAfterFocus: {
                caption: '聚焦后事件处理器',
                ini: null
            },
            
            onBlur: {
                caption: '失焦事件处理器',
                ini: null
            },
            
            onAfterBlur: {
                caption: '失焦后事件处理器',
                ini: null
            },
            
            onChange: {
                caption: '变化事件处理器',
                ini: null
            },
            
            onAfterChange: {
                caption: '变化后事件处理器',
                ini: null
            },
            
            onKeydown: {
                caption: '键盘事件处理器',
                ini: null
            },
            
            // 宽度
            width: {
                caption: '输入框宽度',
                $spaceunit: 1,
                ini: '100%'
            }
        },
        
        // 表单字段检查
        _isFormField: function(profile) {
            return profile.properties.isFormField;
        },
        
        // 值确保方法
        _ensureValue: function(profile, value) {
            // 确保输入值为字符串
            return value === null || value === undefined ? '' : String(value);
        },
        
        // 校验方法
        _checkValid: function(profile, value) {
            var properties = profile.properties;
            
            // 必填校验
            if (properties.required && (!value || !value.trim())) {
                return false;
            }
            
            // 长度校验
            if (properties.minLength && value && value.length < properties.minLength) {
                return false;
            }
            
            if (properties.maxLength && value && value.length > properties.maxLength) {
                return false;
            }
            
            // 类型校验
            if (properties.type === 'email' && value) {
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                return emailRegex.test(value);
            }
            
            if (properties.type === 'tel' && value) {
                var phoneRegex = /^[\d\s\-\+\(\)]+$/;
                return phoneRegex.test(value);
            }
            
            return true;
        },
        
        // 渲染触发器
        RenderTrigger: function() {
            var profile = this;
            
            // 初始化输入框
            ood.asyRun(function() {
                profile.boxing().Initialize();
            });
        },
        
        // 数据准备
        _prepareData: function(profile) {
            var data = arguments.callee.upper.call(this, profile);
            var properties = profile.properties;
            
            // 设置尺寸类
            data._sizeClass = properties.size && properties.size !== 'md' ? 'ood-mobile-input-' + properties.size : '';
            
            // 设置状态类
            data._statusClass = '';
            if (properties.disabled) data._statusClass += ' ood-mobile-input-disabled';
            if (properties.readonly) data._statusClass += ' ood-mobile-input-readonly';
            
            // 设置显示状态
            data._labelDisplay = properties.label ? 'block' : 'none';
            data._prefixDisplay = properties.prefix ? 'block' : 'none';
            data._suffixDisplay = properties.suffix ? 'block' : 'none';
            data._hintDisplay = properties.hint ? 'block' : 'none';
            
            // 设置输入框属性
            data.placeholder = properties.placeholder || '';
            data.type = properties.type || 'text';
            data.value = properties.value || '';
            
            // 设置图标
            data.prefixIcon = properties.prefixIcon || '';
            data.suffixIcon = properties.suffixIcon || '';
            
            // 设置主题类
            data._themeClass = properties.theme && properties.theme !== 'light' ? 'input-theme-' + properties.theme : '';
            
            // 设置响应式类
            data._responsiveClass = properties.responsive ? 'mobile-responsive' : '';
            
            // 设置ARIA属性
            data._ariaLabel = properties.ariaLabel || properties.label || '输入框';
            data._ariaDescribedBy = properties.ariaDescribedBy || '';
            
            // 设置必填状态
            data._required = properties.required ? 'required' : '';
            
            // 设置只读和禁用状态
            data._readonly = properties.readonly ? 'readonly' : '';
            data._disabled = properties.disabled ? 'disabled' : '';
            
            // 设置样式
            data._style = properties.style || '';
            
            return data;
        },
        
        // 事件处理器
        EventHandlers: {
            // 输入框事件
            onInputFocus: function(profile, event) {
                // 输入框聚焦事件处理器
            },
            
            onInputBlur: function(profile, event) {
                // 输入框失焦事件处理器
            },
            
            onInputChange: function(profile, event, value) {
                // 输入框变化事件处理器
            },
            
            // 继承 ood.absValue 的事件处理器
            beforeValueSet: function(profile, oldValue, newValue, force, tag) {
                // 值设置前的处理
            },
            
            afterValueSet: function(profile, oldValue, newValue, force, tag) {
                // 值设置后的处理
            },
            
            onChange: function(profile, oldValue, newValue, force, tag) {
                // 值改变处理
            },
            
            beforeUIValueSet: function(profile, oldValue, newValue, force, tag) {
                // UI值设置前的处理
            },
            
            afterUIValueSet: function(profile, oldValue, newValue, force, tag) {
                // UI值设置后的处理
            },
            
            // 继承 ood.UI 的事件处理器
            beforeRender: function(profile) {
                // 渲染前处理
            },
            
            onRender: function(profile) {
                // 渲染后处理
            },
            
            beforeDestroy: function(profile) {
                // 销毁前处理
            },
            
            afterDestroy: function(profile) {
                // 销毁后处理
            },
            
            beforeDirtyMark: function(profile, dirty) {
                // 脏标记前处理
            }
        }
    }
});

// 添加清除按钮图标样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .ood-mobile-input-clear-icon:before {
            content: "×";
            font-weight: bold;
        }
    `;
    document.head.appendChild(style);
}

// 添加Getter/Setter方法
(function() {
    var proto = ood.Mobile.Input.prototype;
    
    // caption 属性的 getter/setter
    proto.getCaption = function() {
        var profile = this.get(0);
        return profile ? profile.properties.caption : '';
    };
    
    proto.setCaption = function(value) {
        return this.each(function(profile) {
            profile.properties.caption = value;
            if (profile.boxing().caption && typeof profile.boxing().caption.action === 'function') {
                profile.boxing().caption.action.call(profile, value);
            }
        });
    };
    
    // width 属性的 getter/setter
    proto.getWidth = function() {
        var profile = this.get(0);
        return profile ? profile.properties.width : '';
    };
    
    proto.setWidth = function(value) {
        return this.each(function(profile) {
            profile.properties.width = value;
            if (profile.boxing().width && typeof profile.boxing().width.action === 'function') {
                profile.boxing().width.action.call(profile, value);
            }
        });
    };
    
    // height 属性的 getter/setter
    proto.getHeight = function() {
        var profile = this.get(0);
        return profile ? profile.properties.height : '';
    };
    
    proto.setHeight = function(value) {
        return this.each(function(profile) {
            profile.properties.height = value;
            if (profile.boxing().height && typeof profile.boxing().height.action === 'function') {
                profile.boxing().height.action.call(profile, value);
            }
        });
    };
    
    // labelColor 属性的 getter/setter
    proto.getLabelColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.labelColor : '';
    };
    
    proto.setLabelColor = function(value) {
        return this.each(function(profile) {
            profile.properties.labelColor = value;
            if (profile.boxing().labelColor && typeof profile.boxing().labelColor.action === 'function') {
                profile.boxing().labelColor.action.call(profile, value);
            }
        });
    };
    
    // borderColor 属性的 getter/setter
    proto.getBorderColor = function() {
        var profile = this.get(0);
        return profile ? profile.properties.borderColor : '';
    };
    
    proto.setBorderColor = function(value) {
        return this.each(function(profile) {
            profile.properties.borderColor = value;
            if (profile.boxing().borderColor && typeof profile.boxing().borderColor.action === 'function') {
                profile.boxing().borderColor.action.call(profile, value);
            }
        });
    };
    
    // prefixIcon 属性的 getter/setter
    proto.getPrefixIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.prefixIcon : '';
    };
    
    proto.setPrefixIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.prefixIcon = value;
            if (profile.boxing().prefixIcon && typeof profile.boxing().prefixIcon.action === 'function') {
                profile.boxing().prefixIcon.action.call(profile, value);
            }
        });
    };
    
    // suffixIcon 属性的 getter/setter
    proto.getSuffixIcon = function() {
        var profile = this.get(0);
        return profile ? profile.properties.suffixIcon : '';
    };
    
    proto.setSuffixIcon = function(value) {
        return this.each(function(profile) {
            profile.properties.suffixIcon = value;
            if (profile.boxing().suffixIcon && typeof profile.boxing().suffixIcon.action === 'function') {
                profile.boxing().suffixIcon.action.call(profile, value);
            }
        });
    };
    
    // theme 属性的 getter/setter
    proto.getTheme = function() {
        var profile = this.get(0);
        return profile ? profile.properties.theme : 'light';
    };
    
    proto.setTheme = function(value) {
        return this.each(function(profile) {
            profile.properties.theme = value;
            if (profile.boxing().theme && typeof profile.boxing().theme.action === 'function') {
                profile.boxing().theme.action.call(profile, value);
            }
        });
    };
    
    // responsive 属性的 getter/setter
    proto.getResponsive = function() {
        var profile = this.get(0);
        return profile ? profile.properties.responsive : true;
    };
    
    proto.setResponsive = function(value) {
        return this.each(function(profile) {
            profile.properties.responsive = value;
            if (profile.boxing().responsive && typeof profile.boxing().responsive.action === 'function') {
                profile.boxing().responsive.action.call(profile, value);
            }
        });
    };
    
    // ariaLabel 属性的 getter/setter
    proto.getAriaLabel = function() {
        var profile = this.get(0);
        return profile ? profile.properties.ariaLabel : '';
    };
    
    proto.setAriaLabel = function(value) {
        return this.each(function(profile) {
            profile.properties.ariaLabel = value;
            if (profile.boxing().ariaLabel && typeof profile.boxing().ariaLabel.action === 'function') {
                profile.boxing().ariaLabel.action.call(profile, value);
            }
        });
    };
    
    // ariaDescribedBy 属性的 getter/setter
    proto.getAriaDescribedBy = function() {
        var profile = this.get(0);
        return profile ? profile.properties.ariaDescribedBy : '';
    };
    
    proto.setAriaDescribedBy = function(value) {
        return this.each(function(profile) {
            profile.properties.ariaDescribedBy = value;
            if (profile.boxing().ariaDescribedBy && typeof profile.boxing().ariaDescribedBy.action === 'function') {
                profile.boxing().ariaDescribedBy.action.call(profile, value);
            }
        });
    };
    
    // value 属性的 getter/setter
    proto.getValue = function() {
        var profile = this.get(0);
        return profile ? profile.properties.value : '';
    };
    
    proto.setValue = function(value) {
        return this.each(function(profile) {
            profile.properties.value = value;
            if (profile.boxing().value && typeof profile.boxing().value.action === 'function') {
                profile.boxing().value.action.call(profile, value);
            }
        });
    };
    
    // isFormField 属性的 getter/setter
    proto.getIsFormField = function() {
        var profile = this.get(0);
        return profile ? profile.properties.isFormField : true;
    };
    
    proto.setIsFormField = function(value) {
        return this.each(function(profile) {
            profile.properties.isFormField = value;
            if (profile.boxing().isFormField && typeof profile.boxing().isFormField.action === 'function') {
                profile.boxing().isFormField.action.call(profile, value);
            }
        });
    };
    
    // dirtyMark 属性的 getter/setter
    proto.getDirtyMark = function() {
        var profile = this.get(0);
        return profile ? profile.properties.dirtyMark : true;
    };
    
    proto.setDirtyMark = function(value) {
        return this.each(function(profile) {
            profile.properties.dirtyMark = value;
            if (profile.boxing().dirtyMark && typeof profile.boxing().dirtyMark.action === 'function') {
                profile.boxing().dirtyMark.action.call(profile, value);
            }
        });
    };
    
    // showDirtyMark 属性的 getter/setter
    proto.getShowDirtyMark = function() {
        var profile = this.get(0);
        return profile ? profile.properties.showDirtyMark : true;
    };
    
    proto.setShowDirtyMark = function(value) {
        return this.each(function(profile) {
            profile.properties.showDirtyMark = value;
            if (profile.boxing().showDirtyMark && typeof profile.boxing().showDirtyMark.action === 'function') {
                profile.boxing().showDirtyMark.action.call(profile, value);
            }
        });
    };
    
    // readonly 属性的 getter/setter
    proto.getReadonly = function() {
        var profile = this.get(0);
        return profile ? profile.properties.readonly : false;
    };
    
    proto.setReadonly = function(value) {
        return this.each(function(profile) {
            profile.properties.readonly = value;
            if (profile.boxing().readonly && typeof profile.boxing().readonly.action === 'function') {
                profile.boxing().readonly.action.call(profile, value);
            }
        });
    };
    
    // type 属性的 getter/setter
    proto.getType = function() {
        var profile = this.get(0);
        return profile ? profile.properties.type : 'text';
    };
    
    proto.setType = function(value) {
        return this.each(function(profile) {
            profile.properties.type = value;
            if (profile.boxing().type && typeof profile.boxing().type.action === 'function') {
                profile.boxing().type.action.call(profile, value);
            }
        });
    };
    
    // placeholder 属性的 getter/setter
    proto.getPlaceholder = function() {
        var profile = this.get(0);
        return profile ? profile.properties.placeholder : '';
    };
    
    proto.setPlaceholder = function(value) {
        return this.each(function(profile) {
            profile.properties.placeholder = value;
            if (profile.boxing().placeholder && typeof profile.boxing().placeholder.action === 'function') {
                profile.boxing().placeholder.action.call(profile, value);
            }
        });
    };
    
    // label 属性的 getter/setter
    proto.getLabel = function() {
        var profile = this.get(0);
        return profile ? profile.properties.label : '';
    };
    
    proto.setLabel = function(value) {
        return this.each(function(profile) {
            profile.properties.label = value;
            if (profile.boxing().label && typeof profile.boxing().label.action === 'function') {
                profile.boxing().label.action.call(profile, value);
            }
        });
    };
    
    // prefix 属性的 getter/setter
    proto.getPrefix = function() {
        var profile = this.get(0);
        return profile ? profile.properties.prefix : '';
    };
    
    proto.setPrefix = function(value) {
        return this.each(function(profile) {
            profile.properties.prefix = value;
            if (profile.boxing().prefix && typeof profile.boxing().prefix.action === 'function') {
                profile.boxing().prefix.action.call(profile, value);
            }
        });
    };
    
    // suffix 属性的 getter/setter
    proto.getSuffix = function() {
        var profile = this.get(0);
        return profile ? profile.properties.suffix : '';
    };
    
    proto.setSuffix = function(value) {
        return this.each(function(profile) {
            profile.properties.suffix = value;
            if (profile.boxing().suffix && typeof profile.boxing().suffix.action === 'function') {
                profile.boxing().suffix.action.call(profile, value);
            }
        });
    };
    
    // hint 属性的 getter/setter
    proto.getHint = function() {
        var profile = this.get(0);
        return profile ? profile.properties.hint : '';
    };
    
    proto.setHint = function(value) {
        return this.each(function(profile) {
            profile.properties.hint = value;
            if (profile.boxing().hint && typeof profile.boxing().hint.action === 'function') {
                profile.boxing().hint.action.call(profile, value);
            }
        });
    };
    
    // size 属性的 getter/setter
    proto.getSize = function() {
        var profile = this.get(0);
        return profile ? profile.properties.size : 'md';
    };
    
    proto.setSize = function(value) {
        return this.each(function(profile) {
            profile.properties.size = value;
            if (profile.boxing().size && typeof profile.boxing().size.action === 'function') {
                profile.boxing().size.action.call(profile, value);
            }
        });
    };
    
    // required 属性的 getter/setter
    proto.getRequired = function() {
        var profile = this.get(0);
        return profile ? profile.properties.required : false;
    };
    
    proto.setRequired = function(value) {
        return this.each(function(profile) {
            profile.properties.required = value;
            if (profile.boxing().required && typeof profile.boxing().required.action === 'function') {
                profile.boxing().required.action.call(profile, value);
            }
        });
    };
    
    // minLength 属性的 getter/setter
    proto.getMinLength = function() {
        var profile = this.get(0);
        return profile ? profile.properties.minLength : null;
    };
    
    proto.setMinLength = function(value) {
        return this.each(function(profile) {
            profile.properties.minLength = value;
            if (profile.boxing().minLength && typeof profile.boxing().minLength.action === 'function') {
                profile.boxing().minLength.action.call(profile, value);
            }
        });
    };
    
    // maxLength 属性的 getter/setter
    proto.getMaxLength = function() {
        var profile = this.get(0);
        return profile ? profile.properties.maxLength : null;
    };
    
    proto.setMaxLength = function(value) {
        return this.each(function(profile) {
            profile.properties.maxLength = value;
            if (profile.boxing().maxLength && typeof profile.boxing().maxLength.action === 'function') {
                profile.boxing().maxLength.action.call(profile, value);
            }
        });
    };
    
    // disabled 属性的 getter/setter
    proto.getDisabled = function() {
        var profile = this.get(0);
        return profile ? profile.properties.disabled : false;
    };
    
    proto.setDisabled = function(value) {
        return this.each(function(profile) {
            profile.properties.disabled = value;
            if (profile.boxing().disabled && typeof profile.boxing().disabled.action === 'function') {
                profile.boxing().disabled.action.call(profile, value);
            }
        });
    };
    
    // formatter 属性的 getter/setter
    proto.getFormatter = function() {
        var profile = this.get(0);
        return profile ? profile.properties.formatter : null;
    };
    
    proto.setFormatter = function(value) {
        return this.each(function(profile) {
            profile.properties.formatter = value;
            if (profile.boxing().formatter && typeof profile.boxing().formatter.action === 'function') {
                profile.boxing().formatter.action.call(profile, value);
            }
        });
    };
    
    // onFocus 属性的 getter/setter
    proto.getOnFocus = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onFocus : null;
    };
    
    proto.setOnFocus = function(value) {
        return this.each(function(profile) {
            profile.properties.onFocus = value;
            if (profile.boxing().onFocus && typeof profile.boxing().onFocus.action === 'function') {
                profile.boxing().onFocus.action.call(profile, value);
            }
        });
    };
    
    // onAfterFocus 属性的 getter/setter
    proto.getOnAfterFocus = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterFocus : null;
    };
    
    proto.setOnAfterFocus = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterFocus = value;
            if (profile.boxing().onAfterFocus && typeof profile.boxing().onAfterFocus.action === 'function') {
                profile.boxing().onAfterFocus.action.call(profile, value);
            }
        });
    };
    
    // onBlur 属性的 getter/setter
    proto.getOnBlur = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onBlur : null;
    };
    
    proto.setOnBlur = function(value) {
        return this.each(function(profile) {
            profile.properties.onBlur = value;
            if (profile.boxing().onBlur && typeof profile.boxing().onBlur.action === 'function') {
                profile.boxing().onBlur.action.call(profile, value);
            }
        });
    };
    
    // onAfterBlur 属性的 getter/setter
    proto.getOnAfterBlur = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterBlur : null;
    };
    
    proto.setOnAfterBlur = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterBlur = value;
            if (profile.boxing().onAfterBlur && typeof profile.boxing().onAfterBlur.action === 'function') {
                profile.boxing().onAfterBlur.action.call(profile, value);
            }
        });
    };
    
    // onChange 属性的 getter/setter
    proto.getOnChange = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onChange : null;
    };
    
    proto.setOnChange = function(value) {
        return this.each(function(profile) {
            profile.properties.onChange = value;
            if (profile.boxing().onChange && typeof profile.boxing().onChange.action === 'function') {
                profile.boxing().onChange.action.call(profile, value);
            }
        });
    };
    
    // onAfterChange 属性的 getter/setter
    proto.getOnAfterChange = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onAfterChange : null;
    };
    
    proto.setOnAfterChange = function(value) {
        return this.each(function(profile) {
            profile.properties.onAfterChange = value;
            if (profile.boxing().onAfterChange && typeof profile.boxing().onAfterChange.action === 'function') {
                profile.boxing().onAfterChange.action.call(profile, value);
            }
        });
    };
    
    // onKeydown 属性的 getter/setter
    proto.getOnKeydown = function() {
        var profile = this.get(0);
        return profile ? profile.properties.onKeydown : null;
    };
    
    proto.setOnKeydown = function(value) {
        return this.each(function(profile) {
            profile.properties.onKeydown = value;
            if (profile.boxing().onKeydown && typeof profile.boxing().onKeydown.action === 'function') {
                profile.boxing().onKeydown.action.call(profile, value);
            }
        });
    };
})();