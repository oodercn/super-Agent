/**
 * 移动端开关组件
 * 继承自 ood.UI（UI基类）、ood.absValue（值管理）
 * 实现四分离设计模式：样式、模板、行为、数据完全分离
 * 支持触摸交互、动画效果、禁用状态等
 */
ood.Class("ood.Mobile.Switch", ["ood.UI", "ood.absValue"], {
    Initialize: function() {
        // 注册为移动端UI组件，确保继承UI基类的所有功能
        this.addTemplateKeys(['LABEL', 'SWITCH', 'TRACK', 'THUMB', 'INPUT']);
    },
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            width: '3em',
            height: '1.5em',
            caption: '开关控件',
            value: false,
            theme: 'light',
            responsive: true
        },

        Initialize: function() {
            // 调用父类的初始化方法
         //   this.constructor.upper.prototype.Initialize.call(this);
            
            // 初始化开关特性
            this.initSwitchFeatures();
            
            // 绑定事件
            this.bindTouchEvents();
        },
        
        // ood.absValue 必需方法 - 控制值管理
        _setCtrlValue: function(value) {
            // Switch 的值为布尔值
            var checked = !!value;
            this.setChecked(checked);
        },
        
        _getCtrlValue: function() {
            // 返回当前的开关状态
            return this.getChecked();
        },
        
        activate: function() {
            return this.each(function(profile) {
                // 激活开关（聚焦）
                var switchEl = profile.getSubNode('SWITCH');
                if (switchEl) {
                    switchEl.focus();
                }
            });
        },
        
        resetValue: function(value) {
            // 重写基类的 resetValue 方法
            this._setCtrlValue(value);
            this.each(function(profile) {
                profile.properties.value = value;
                profile.properties.checked = !!value;
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
                root.removeClass('ood-mobile-switch-dark ood-mobile-switch-hc');
                
                // 应用新主题类
                switch (theme) {
                    case 'dark':
                        root.addClass('ood-mobile-switch-dark');
                        break;
                    case 'light-hc':
                    case 'dark-hc':
                        root.addClass('ood-mobile-switch-hc');
                        break;
                }
            });
        },
        
        getTheme: function() {
            var profile = this.get(0);
            return profile ? profile.properties.theme || 'light' : 'light';
        },
        
        initSwitchFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-switch');
            this.updateSwitchState();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var switchEl = profile.getSubNode('SWITCH');
            
            switchEl.on('click', function(e) {
                if (profile.properties.disabled) return;
                
                e.preventDefault();
                self.toggle();
            });
            
            // 触摸反馈
            switchEl.on('touchstart', function(e) {
                if (profile.properties.disabled) return;
                switchEl.addClass('ood-mobile-switch-pressed');
            });
            
            switchEl.on('touchend touchcancel', function(e) {
                switchEl.removeClass('ood-mobile-switch-pressed');
            });
        },
        
        updateSwitchState: function() {
            var profile = this.get(0);
            var props = profile.properties;
            var root = profile.getRoot();
            var switchEl = profile.getSubNode('SWITCH');
            
            // 更新状态类
            if (props.checked) {
                root.addClass('ood-mobile-switch-checked');
                switchEl.attr('aria-checked', 'true');
            } else {
                root.removeClass('ood-mobile-switch-checked');
                switchEl.attr('aria-checked', 'false');
            }
            
            // 更新禁用状态
            if (props.disabled) {
                root.addClass('ood-mobile-switch-disabled');
                switchEl.attr('aria-disabled', 'true');
            } else {
                root.removeClass('ood-mobile-switch-disabled');
                switchEl.attr('aria-disabled', 'false');
            }
        },
        
        toggle: function() {
            var profile = this.get(0);
            if (profile.properties.disabled) return;
            
            this.setChecked(!profile.properties.checked);
        },
        
        setChecked: function(checked) {
            var profile = this.get(0);
            var oldValue = profile.properties.checked;
            
            profile.properties.checked = checked;
            this.updateSwitchState();
            
            // 触发change事件
            if (oldValue !== checked) {
                this.onChange(checked);
            }
        },
        
        getChecked: function() {
            var profile = this.get(0);
            return profile.properties.checked;
        },
        
        setDisabled: function(disabled) {
            var profile = this.get(0);
            profile.properties.disabled = disabled;
            this.updateSwitchState();
        },
        
        onChange: function(checked) {
            var profile = this.get(0);
            
            if (profile.onChange) {
                profile.boxing().onChange(profile, checked);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-switch',
            style: '{_style}',
            
            LABEL: {
                tagName: 'label',
                className: 'ood-mobile-switch-label',
                text: '{label}',
                style: 'display: {_labelDisplay}'
            },
            
            SWITCH: {
                tagName: 'div',
                className: 'ood-mobile-switch-element',
                role: 'switch',
                tabindex: '0',
                'aria-checked': '{_checked}',
                
                TRACK: {
                    tagName: 'div',
                    className: 'ood-mobile-switch-track'
                },
                
                THUMB: {
                    tagName: 'div',
                    className: 'ood-mobile-switch-thumb'
                }
            }
        },
        
        Appearances: {
            KEY: {
                display: 'inline-flex',
                'align-items': 'center',
                gap: 'var(--mobile-spacing-sm)',
                cursor: 'pointer'
            },
            
            LABEL: {
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-text-primary)',
                'user-select': 'none'
            },
            
            SWITCH: {
                position: 'relative',
                width: '48px',
                height: '28px',
                outline: 'none',
                cursor: 'pointer'
            },
            
            TRACK: {
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                'background-color': '#E5E5E7',
                'border-radius': '14px',
                transition: 'background-color 0.3s ease'
            },
            
            THUMB: {
                position: 'absolute',
                top: '2px',
                left: '2px',
                width: '24px',
                height: '24px',
                'background-color': '#FFFFFF',
                'border-radius': '50%',
                'box-shadow': '0 2px 4px rgba(0,0,0,0.2)',
                transition: 'transform 0.3s ease'
            },
            
            // 选中状态
            'KEY.ood-mobile-switch-checked TRACK': {
                'background-color': 'var(--mobile-primary)'
            },
            
            'KEY.ood-mobile-switch-checked THUMB': {
                transform: 'translateX(20px)'
            },
            
            // 禁用状态
            'KEY.ood-mobile-switch-disabled': {
                opacity: '0.5',
                cursor: 'not-allowed'
            },
            
            // 按下状态
            'KEY:not(.ood-mobile-switch-disabled) SWITCH.ood-mobile-switch-pressed THUMB': {
                transform: 'scale(1.1)'
            },
            
            'KEY:not(.ood-mobile-switch-disabled).ood-mobile-switch-checked SWITCH.ood-mobile-switch-pressed THUMB': {
                transform: 'translateX(20px) scale(1.1)'
            }
        },
        
        Behaviors: {
            HotKeyAllowed: true
        },
        
        DataModel: {
            // ===== 基础必需属性 =====
            caption: {
                caption: '开关标题',
                ini: '开关控件',
                action: function(value) {
                    var profile = this;
                    // 同步更新label属性
                    profile.properties.label = value;
                    profile.getRoot().attr('aria-label', value || '开关控件');
                }
            },
            
            width: {
                caption: '开关宽度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            height: {
                caption: '开关高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // ===== 设计器特殊类型属性 =====
            trackColor: {
                caption: '轨道颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var trackNode = this.getSubNode('TRACK');
                    if (value && trackNode && !trackNode.isEmpty()) {
                        trackNode.css('background-color', value);
                    }
                }
            },
            
            thumbColor: {
                caption: '拇指颜色',
                ini: '',
                combobox: function() {
                    return 'COLOR';
                },
                action: function(value) {
                    var thumbNode = this.getSubNode('THUMB');
                    if (value && thumbNode && !thumbNode.isEmpty()) {
                        thumbNode.css('background-color', value);
                    }
                }
            },
            
            // ===== 开关特有属性 =====
            // ood.UI 主题属性
            theme: {
                caption: '主题模式',
                ini: 'light',
                listbox: ['light', 'dark', 'light-hc', 'dark-hc'],
                action: function(value) {
                    this.boxing().setTheme(value);
                }
            },
            
            // ood.absValue 必需属性
            value: {
                caption: '开关值',
                ini: false,
                action: function(value) {
                    this.boxing()._setCtrlValue(value);
                }
            },
            
            isFormField: {
                caption: '表单字段',
                ini: true
            },
            
            dirtyMark: {
                caption: '脏标记',
                ini: false
            },
            
            readonly: {
                caption: '只读',
                ini: false
            },
            
            // Switch 特定属性
            checked: {
                caption: '选中状态',
                ini: false,
                action: function(value) {
                    this.boxing().setChecked(value);
                }
            },
            
            disabled: {
                caption: '禁用状态',
                ini: false,
                action: function(value) {
                    this.boxing().setDisabled(value);
                }
            },
            
            label: {
                caption: '开关标签',
                ini: ''
            },
            
            size: {
                caption: '开关尺寸',
                ini: 'md',
                listbox: ['sm', 'md', 'lg']
            },
            
            // 事件处理器
            onChange: {
                caption: '状态改变事件处理器',
                ini: null
            },
            
            onChanged: {
                caption: '值改变事件处理器',
                ini: null
            }
        },
        
        // ood.absValue 静态方法
        _isFormField: function() {
            return true; // Switch 组件可以作为表单字段
        },
        
        _ensureValue: function(value) {
            // 确保值为布尔类型
            return !!value;
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().initSwitchFeatures();
                
                // 设置默认值
                var value = profile.properties.value !== undefined ? profile.properties.value : profile.properties.checked;
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
            
            data._checked = props.checked ? 'true' : 'false';
            data._labelDisplay = props.label ? 'block' : 'none';
            
            // 设置开关属性
            data.label = props.label || props.caption || '';
            
            // 设置禁用和只读状态
            data._disabled = props.disabled ? 'disabled' : '';
            data._readonly = props.readonly ? 'readonly' : '';
            
            // 设置尺寸类
            data._sizeClass = props.size && props.size !== 'md' ? 'ood-mobile-switch-' + props.size : '';
            
            // 设置主题类
            data._themeClass = props.theme && props.theme !== 'light' ? 'switch-theme-' + props.theme : '';
            
            // 设置ARIA标签
            data._ariaLabel = props.ariaLabel || props.label || props.caption || '开关';
            
            // 设置表单字段属性
            data._isFormField = props.isFormField ? 'form-field' : '';
            
            // 设置样式
            data._style = props.style || '';
            
            return data;
        },
        
        EventHandlers: {
            // ood.UI 生命周期事件处理器
            onReady: function(profile) {
                // 组件就绪时的初始化
                profile.boxing().initSwitchFeatures();
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
            
            // Switch 特定事件处理器
            onChange: function(profile, checked) {
                // 开关状态变化事件处理器
            }
        }
    }
});
