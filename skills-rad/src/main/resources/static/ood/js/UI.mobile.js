new function () {
    // 移动端组件库 - 基于 ood 框架
    // 所有组件继承 ood.UI，符合 ood 规范

    // ==================== 移动端基础组件 ====================

    /**
     * 移动端按钮组件
     */
    ood.Class("ood.UI.MobileButton", "ood.UI", {
        Initialize: function () {
            this.$activeClass$ = 'ood.UI.MobileButton';
        },
        Instance: {
            // 激活按钮
            activate: function () {
                this.getRoot().focus(true);
                return this;
            },
            
            // 设置按钮值
            _setCtrlValue: function (value) {
                if (ood.isNull(value) || !ood.isDefined(value)) value = false;
                return this.each(function (profile) {
                    profile.getRoot().tagClass('-checked', value);
                });
            }
        },
        Static: {
            Templates: {
                tagName: 'button',
                className: 'ood-mobile-btn ood-mobile-uibar ood-mobile-uigradient ood-mobile-uiborder-radius {_className}',
                style: '{_align}{_style}{_fc}{_fw}{_fs}{_ff};',
                tabindex: '{tabindex}',
                
                ICON: {
                    $order: 1,
                    className: 'ood-mobile-con {imageClass}',
                    style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle}',
                    text: '{iconFontCode}'
                },
                
                CAPTION: {
                    $order: 2,
                    className: 'ood-mobile-caption',
                    text: '{caption}'
                }
            },
            
            Appearances: {
                // 移动端按钮基础样式
                KEY: {
                    'min-height': '44px',
                    'padding': '12px 16px',
                    'font-size': '16px',
                    'border-radius': '8px',
                    'touch-action': 'manipulation'
                },
                
                ICON: {
                    'margin-right': '8px',
                    'font-size': '20px'
                }
            },
            
            Behaviors: {
                // 移动端触摸行为
                HoverEffected: {KEY: ['KEY']},
                
                onClick: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return false;
                    
                    var b = profile.boxing();
                    if (profile.onClick)
                        return b.onClick(profile, e, src);
                },
                
                onTouchStart: function (profile, e, src) {
                    profile.getRoot().tagClass('-active', true);
                },
                
                onTouchEnd: function (profile, e, src) {
                    profile.getRoot().tagClass('-active', false);
                }
            },
            
            DataModel: {
                caption: {
                    ini: '',
                    action: function (v) {
                        v = (ood.isSet(v) ? v : "") + "";
                        this.getSubNode('CAPTION').html(ood.adjustRes(v, 0, 1));
                    }
                },
                
                image: {
                    format: 'image',
                    action: function (v) {
                        ood.UI.$iconAction(this);
                    }
                },
                
                iconFontCode: {
                    action: function (v) {
                        ood.UI.$iconAction(this);
                    }
                },
                
                fontSize: {
                    combobox: ["14px", "16px", "18px", "20px"],
                    action: function (value) {
                        this.getSubNode("CAPTION").css('fontSize', value);
                    }
                },
                
                disabled: {
                    ini: false,
                    action: function (value) {
                        this.getRoot().tagClass('-disabled', value);
                    }
                }
            },
            
            _prepareData: function (profile, data) {
                var data = arguments.callee.upper.call(this, profile);
                var v;
                
                if (v = data.fontSize) data._fs = 'font-size:' + v + ';';
                if (v = data.fontWeight) data._fw = 'font-weight:' + v + ';';
                if (v = data.fontColor) data._fc = 'color:' + v + ';';
                if (v = data.fontFamily) data._ff = 'font-family:' + v + ';';
                
                return data;
            },
            
            RenderTrigger: function () {
                var self = this, p = self.properties;
                // 移动端渲染触发逻辑
                if (p.disabled) {
                    self.getRoot().tagClass('-disabled', true);
                }
            },
            
            EventHandlers: {
                onClick: function (profile, e, src) {
                    // 移动端点击事件处理
                }
            }
        }
    });

    /**
     * 移动端输入框组件
     */
    ood.Class("ood.UI.MobileInput", "ood.UI", {
        Initialize: function () {
            this.$activeClass$ = 'ood.UI.MobileInput';
        },
        Static: {
            Templates: {
                tagName: 'input',
                className: 'ood-mobile-input ood-mobile-uiborder {_className}',
                style: '{_style}{_fc}{_fw}{_fs}{_ff};',
                type: '{inputType}',
                placeholder: '{placeholder}',
                value: '{value}'
            },
            
            Appearances: {
                KEY: {
                    'min-height': '44px',
                    'padding': '12px 16px',
                    'font-size': '16px',
                    'border-radius': '8px',
                    'border': '1px solid #ccc',
                    'touch-action': 'manipulation'
                }
            },
            
            Behaviors: {
                onFocus: function (profile, e, src) {
                    profile.getRoot().tagClass('-focus', true);
                },
                
                onBlur: function (profile, e, src) {
                    profile.getRoot().tagClass('-focus', false);
                },
                
                onInput: function (profile, e, src) {
                    var value = src.value;
                    profile.properties.value = value;
                    
                    if (profile.onChange)
                        profile.onChange(profile, e, value);
                }
            },
            
            DataModel: {
                value: {
                    ini: '',
                    action: function (v) {
                        this.getRoot().val(v);
                    }
                },
                
                placeholder: {
                    ini: '',
                    action: function (v) {
                        this.getRoot().attr('placeholder', v);
                    }
                },
                
                inputType: {
                    ini: 'text',
                    listbox: ['text', 'password', 'email', 'tel', 'number', 'search']
                },
                
                disabled: {
                    ini: false,
                    action: function (value) {
                        this.getRoot().prop('disabled', value);
                    }
                }
            },
            
            _prepareData: function (profile, data) {
                var data = arguments.callee.upper.call(this, profile);
                // 移动端输入框数据准备
                return data;
            },
            
            EventHandlers: {
                onChange: function (profile, e, value) {
                    // 输入变化事件处理
                }
            }
        }
    });

    /**
     * 移动端开关组件
     */
    ood.Class("ood.UI.MobileSwitch", "ood.UI", {
        Initialize: function () {
            this.$activeClass$ = 'ood.UI.MobileSwitch';
        },
        Static: {
            Templates: {
                tagName: 'div',
                className: 'ood-mobile-switch {_className}',
                style: '{_style}',
                
                TRACK: {
                    className: 'ood-mobile-switch-track',
                    style: '{trackStyle}'
                },
                
                THUMB: {
                    className: 'ood-mobile-switch-thumb',
                    style: '{thumbStyle}'
                }
            },
            
            Appearances: {
                KEY: {
                    'width': '51px',
                    'height': '31px',
                    'position': 'relative',
                    'touch-action': 'manipulation'
                },
                
                TRACK: {
                    'width': '100%',
                    'height': '100%',
                    'border-radius': '16px',
                    'background-color': '#e9e9e9',
                    'transition': 'background-color 0.3s'
                },
                
                THUMB: {
                    'position': 'absolute',
                    'width': '27px',
                    'height': '27px',
                    'border-radius': '50%',
                    'background-color': 'white',
                    'top': '2px',
                    'left': '2px',
                    'transition': 'left 0.3s',
                    'box-shadow': '0 2px 4px rgba(0,0,0,0.3)'
                }
            },
            
            Behaviors: {
                onClick: function (profile, e, src) {
                    var p = profile.properties;
                    if (p.disabled) return false;
                    
                    var newValue = !p.value;
                    profile.boxing().setValue(newValue);
                    
                    if (profile.onChange)
                        profile.onChange(profile, e, newValue);
                }
            },
            
            DataModel: {
                value: {
                    ini: false,
                    action: function (value) {
                        var track = this.getSubNode('TRACK');
                        var thumb = this.getSubNode('THUMB');
                        
                        if (value) {
                            track.css('background-color', '#4cd964');
                            thumb.css('left', '22px');
                        } else {
                            track.css('background-color', '#e9e9e9');
                            thumb.css('left', '2px');
                        }
                    }
                },
                
                disabled: {
                    ini: false,
                    action: function (value) {
                        this.getRoot().tagClass('-disabled', value);
                    }
                }
            },
            
            _prepareData: function (profile, data) {
                var data = arguments.callee.upper.call(this, profile);
                // 开关组件数据准备
                data.trackStyle = '';
                data.thumbStyle = '';
                return data;
            },
            
            EventHandlers: {
                onChange: function (profile, e, value) {
                    // 开关状态变化事件处理
                }
            }
        }
    });

    // ==================== 移动端组件注册 ====================

    // 注册移动端组件类型
    ood.absBox.$type['MobileButton'] = 'ood.UI.MobileButton';
    ood.absBox.$type['MobileInput'] = 'ood.UI.MobileInput';
    ood.absBox.$type['MobileSwitch'] = 'ood.UI.MobileSwitch';

    // 移动端组件初始化
    ood.ready(function() {
        // 移动端触摸事件支持
        if (ood.browser.isTouch) {
            // 添加移动端特定的事件处理
        }
    });

}();