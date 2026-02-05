/**
 * 移动端时间选择器组件 - 符合腾讯低代码移动端规范
 * 继承 ood.UI.MobilePicker，实现时间选择功能
 */

ood.Class("TDesignMobile.TimePicker", "TDesignMobile.Picker", {
    Initialize: function() {
        this.$activeClass$ = 'TDesignMobile.TimePicker';
    },
    
    Static: {
        Templates: {
            // 继承并扩展 Picker 的模板
            tagName: 'div',
            className: 'tdesign-mobile-timepicker {_className}',
            style: '{_style}',
            
            TRIGGER: {
                tagName: 'div',
                className: 'tdesign-timepicker__trigger',
                style: 'position: relative;',
                
                INPUT: {
                    tagName: 'input',
                    className: 'tdesign-timepicker__input',
                    type: 'text',
                    placeholder: '{placeholder}',
                    readonly: 'readonly',
                    style: 'width: 100%;'
                },
                
                ARROW: {
                    tagName: 'span',
                    className: 'tdesign-timepicker__arrow',
                    html: '⌄',
                    style: 'position: absolute; right: 12px; top: 50%; transform: translateY(-50%); pointer-events: none;'
                }
            },
            
            PANEL: {
                tagName: 'div',
                className: 'tdesign-timepicker__panel',
                style: 'display: none; position: fixed; bottom: 0; left: 0; right: 0; background: #fff; z-index: 1000; border-radius: 16px 16px 0 0; max-height: 50vh;',
                
                HEADER: {
                    tagName: 'div',
                    className: 'tdesign-timepicker__header',
                    style: 'display: flex; justify-content: space-between; align-items: center; padding: 16px; border-bottom: 1px solid var(--td-color-border);',
                    
                    CANCEL: {
                        tagName: 'button',
                        className: 'tdesign-timepicker__cancel',
                        html: '{cancelText}',
                        style: 'background: none; border: none; color: var(--td-color-text-secondary); font-size: 16px;'
                    },
                    
                    TITLE: {
                        tagName: 'div',
                        className: 'tdesign-timepicker__title',
                        html: '{title}',
                        style: 'font-weight: 500; font-size: 16px;'
                    },
                    
                    CONFIRM: {
                        tagName: 'button',
                        className: 'tdesign-timepicker__confirm',
                        html: '{confirmText}',
                        style: 'background: none; border: none; color: var(--td-color-brand); font-size: 16px; font-weight: 500;'
                    }
                },
                
                CONTENT: {
                    tagName: 'div',
                    className: 'tdesign-timepicker__content',
                    style: 'padding: 20px;',
                    
                    // 时间选择器特定内容
                    TIME_CONTAINER: {
                        tagName: 'div',
                        className: 'tdesign-timepicker__time-container',
                        style: 'display: flex; justify-content: center; align-items: center; gap: 16px;',
                        
                        // 小时选择器
                        HOURS: {
                            tagName: 'div',
                            className: 'tdesign-timepicker__hours',
                            style: 'flex: 1; max-height: 240px; overflow-y: auto;',
                            
                            HOURS_LIST: {
                                tagName: 'div',
                                className: 'tdesign-timepicker__hours-list',
                                style: 'display: flex; flex-direction: column; gap: 4px;'
                            }
                        },
                        
                        // 分隔符
                        SEPARATOR: {
                            tagName: 'div',
                            className: 'tdesign-timepicker__separator',
                            html: ':',
                            style: 'font-size: 20px; font-weight: 500; color: var(--td-color-text-primary);'
                        },
                        
                        // 分钟选择器
                        MINUTES: {
                            tagName: 'div',
                            className: 'tdesign-timepicker__minutes',
                            style: 'flex: 1; max-height: 240px; overflow-y: auto;',
                            
                            MINUTES_LIST: {
                                tagName: 'div',
                                className: 'tdesign-timepicker__minutes-list',
                                style: 'display: flex; flex-direction: column; gap: 4px;'
                            }
                        },
                        
                        // 上午/下午选择器
                        AMPM: {
                            tagName: 'div',
                            className: 'tdesign-timepicker__ampm',
                            style: 'flex: 0 0 auto; margin-left: 16px;',
                            
                            AMPM_LIST: {
                                tagName: 'div',
                                className: 'tdesign-timepicker__ampm-list',
                                style: 'display: flex; flex-direction: column; gap: 4px;'
                            }
                        }
                    }
                }
            },
            
            OVERLAY: {
                tagName: 'div',
                className: 'tdesign-timepicker__overlay',
                style: 'display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); z-index: 999;'
            }
        },
        
        Appearances: {
            // 继承基础样式
            KEY: arguments.callee.upper.KEY,
            TRIGGER: arguments.callee.upper.TRIGGER,
            INPUT: arguments.callee.upper.INPUT,
            ARROW: arguments.callee.upper.ARROW,
            PANEL: arguments.callee.upper.PANEL,
            OVERLAY: arguments.callee.upper.OVERLAY,
            
            // 时间选择器特定样式
            TIME_ITEM: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                'padding': '12px 16px',
                'border-radius': '8px',
                'font-size': '16px',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                'min-width': '60px',
                'text-align': 'center',
                '&:hover': {
                    'background-color': 'var(--td-color-bg-secondary)'
                }
            },
            
            TIME_ITEM_SELECTED: {
                'background-color': 'var(--td-color-brand)',
                'color': 'white',
                'font-weight': '500'
            },
            
            TIME_ITEM_DISABLED: {
                'color': 'var(--td-color-text-secondary)',
                'cursor': 'not-allowed',
                'opacity': '0.4',
                '&:hover': {
                    'background-color': 'transparent'
                }
            },
            
            TIME_LIST: {
                'display': 'flex',
                'flex-direction': 'column',
                'gap': '4px',
                'max-height': '240px',
                'overflow-y': 'auto',
                'scroll-behavior': 'smooth'
            }
        },
        
        Behaviors: {
            // 继承基础行为
            TRIGGER: arguments.callee.upper.TRIGGER,
            CANCEL: arguments.callee.upper.CANCEL,
            CONFIRM: arguments.callee.upper.CONFIRM,
            OVERLAY: arguments.callee.upper.OVERLAY,
            
            // 时间选择器特定行为
            HOUR_ITEM: {
                click: function(profile, e) {
                    var hour = parseInt(this.attr('data-hour'));
                    this.selectHour(hour);
                }
            },
            
            MINUTE_ITEM: {
                click: function(profile, e) {
                    var minute = parseInt(this.attr('data-minute'));
                    this.selectMinute(minute);
                }
            },
            
            AMPM_ITEM: {
                click: function(profile, e) {
                    var period = this.attr('data-period');
                    this.selectAmPm(period);
                }
            }
        },
        
        DataModel: {
            // 继承基础数据模型
            value: arguments.callee.upper.value,
            placeholder: arguments.callee.upper.placeholder,
            title: arguments.callee.upper.title,
            cancelText: arguments.callee.upper.cancelText,
            confirmText: arguments.callee.upper.confirmText,
            disabled: arguments.callee.upper.disabled,
            visible: arguments.callee.upper.visible,
            
            // 时间选择器特定属性
            format: {
                caption: '时间格式',
                type: 'string',
                ini: 'HH:mm',
                action: 'updateDisplayValue'
            },
            
            minTime: {
                caption: '最小时间',
                type: 'string',
                ini: null,
                action: 'renderTimeLists'
            },
            
            maxTime: {
                caption: '最大时间',
                type: 'string',
                ini: null,
                action: 'renderTimeLists'
            },
            
            step: {
                caption: '时间步长',
                type: 'number',
                ini: 30,
                action: 'renderTimeLists'
            },
            
            use12Hours: {
                caption: '12小时制',
                type: 'boolean',
                ini: false,
                action: 'renderTimeLists'
            },
            
            currentHour: {
                caption: '当前小时',
                type: 'number',
                ini: 0,
                action: 'updateSelection'
            },
            
            currentMinute: {
                caption: '当前分钟',
                type: 'number',
                ini: 0,
                action: 'updateSelection'
            },
            
            currentPeriod: {
                caption: '当前时段',
                type: 'enum',
                options: ['AM', 'PM'],
                ini: 'AM',
                action: 'updateSelection'
            }
        },
        
        RenderTrigger: {
            // 继承基础渲染触发器
            value: arguments.callee.upper.RenderTrigger.value,
            visible: arguments.callee.upper.RenderTrigger.visible,
            
            // 时间选择器特定渲染触发器
            step: ['renderTimeLists'],
            use12Hours: ['renderTimeLists'],
            minTime: ['renderTimeLists'],
            maxTime: ['renderTimeLists'],
            format: ['updateDisplayValue']
        },
        
        _prepareData: function(profile, data) {
            var data = arguments.callee.upper.call(this, profile);
            
            // 准备时间选择器数据
            data._currentTime = this.getFormattedTime();
            data._showAmPm = this.getUse12Hours();
            
            return data;
        },
        
        EventHandlers: {
            // 继承基础事件处理器
            change: arguments.callee.upper.EventHandlers.change,
            confirm: arguments.callee.upper.EventHandlers.confirm,
            cancel: arguments.callee.upper.EventHandlers.cancel,
            show: arguments.callee.upper.EventHandlers.show,
            hide: arguments.callee.upper.EventHandlers.hide,
            
            // 时间选择器特定事件
            hourChange: function(hour) {
                console.log('Hour changed to:', hour);
            },
            
            minuteChange: function(minute) {
                console.log('Minute changed to:', minute);
            },
            
            periodChange: function(period) {
                console.log('Period changed to:', period);
            }
        },
        
        // ==================== 时间选择器特定方法 ====================
        
        // 渲染时间列表
        renderTimeLists: function() {
            this.renderHours();
            this.renderMinutes();
            if (this.getUse12Hours()) {
                this.renderAmPm();
            }
        },
        
        // 渲染小时列表
        renderHours: function() {
            var hoursList = this.getSubNode('HOURS_LIST');
            var use12Hours = this.getUse12Hours();
            var minHour = use12Hours ? 1 : 0;
            var maxHour = use12Hours ? 12 : 23;
            var currentHour = this.getCurrentHour();
            
            hoursList.empty();
            
            for (var hour = minHour; hour <= maxHour; hour++) {
                var displayHour = use12Hours ? hour : hour.toString().padStart(2, '0');
                var isSelected = hour === currentHour;
                var isDisabled = this.isHourDisabled(hour);
                
                var hourNode = $('<div>')
                    .addClass('tdesign-timepicker__hour-item')
                    .attr('data-hour', hour)
                    .text(displayHour)
                    .css(this.Appearances.TIME_ITEM);
                
                if (isSelected) {
                    hourNode.addClass('tdesign-timepicker__hour-item--selected');
                    hourNode.css(this.Appearances.TIME_ITEM_SELECTED);
                }
                
                if (isDisabled) {
                    hourNode.addClass('tdesign-timepicker__hour-item--disabled');
                    hourNode.css(this.Appearances.TIME_ITEM_DISABLED);
                }
                
                hoursList.append(hourNode);
                
                // 滚动到选中的小时
                if (isSelected) {
                    setTimeout(function() {
                        hourNode[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }, 100);
                }
            }
        },
        
        // 渲染分钟列表
        renderMinutes: function() {
            var minutesList = this.getSubNode('MINUTES_LIST');
            var step = this.getStep();
            var currentMinute = this.getCurrentMinute();
            
            minutesList.empty();
            
            for (var minute = 0; minute < 60; minute += step) {
                var displayMinute = minute.toString().padStart(2, '0');
                var isSelected = minute === currentMinute;
                var isDisabled = this.isMinuteDisabled(minute);
                
                var minuteNode = $('<div>')
                    .addClass('tdesign-timepicker__minute-item')
                    .attr('data-minute', minute)
                    .text(displayMinute)
                    .css(this.Appearances.TIME_ITEM);
                
                if (isSelected) {
                    minuteNode.addClass('tdesign-timepicker__minute-item--selected');
                    minuteNode.css(this.Appearances.TIME_ITEM_SELECTED);
                }
                
                if (isDisabled) {
                    minuteNode.addClass('tdesign-timepicker__minute-item--disabled');
                    minuteNode.css(this.Appearances.TIME_ITEM_DISABLED);
                }
                
                minutesList.append(minuteNode);
                
                // 滚动到选中的分钟
                if (isSelected) {
                    setTimeout(function() {
                        minuteNode[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }, 100);
                }
            }
        },
        
        // 渲染上午/下午列表
        renderAmPm: function() {
            var ampmList = this.getSubNode('AMPM_LIST');
            var currentPeriod = this.getCurrentPeriod();
            var periods = ['AM', 'PM'];
            
            ampmList.empty();
            
            periods.forEach(function(period) {
                var isSelected = period === currentPeriod;
                
                var periodNode = $('<div>')
                    .addClass('tdesign-timepicker__ampm-item')
                    .attr('data-period', period)
                    .text(period)
                    .css(this.Appearances.TIME_ITEM);
                
                if (isSelected) {
                    periodNode.addClass('tdesign-timepicker__ampm-item--selected');
                    periodNode.css(this.Appearances.TIME_ITEM_SELECTED);
                }
                
                ampmList.append(periodNode);
            });
        },
        
        // 选择小时
        selectHour: function(hour) {
            this.setCurrentHour(hour);
            this.updateTimeValue();
           this.triggerEvent('hourChange', hour);
        },
        
        // 选择分钟
        selectMinute: function(minute) {
            this.setCurrentMinute(minute);
            this.updateTimeValue();
           this.triggerEvent('minuteChange', minute);
        },
        
        // 选择上午/下午
        selectAmPm: function(period) {
            this.setCurrentPeriod(period);
            this.updateTimeValue();
          //  this.triggerEvent('periodChange', period);
        },
        
        // 更新时间值
        updateTimeValue: function() {
            var hour = this.getCurrentHour();
            var minute = this.getCurrentMinute();
            var period = this.getCurrentPeriod();
            var use12Hours = this.getUse12Hours();
            
            // 转换为24小时制
            if (use12Hours) {
                if (period === 'PM' && hour < 12) {
                    hour += 12;
                } else if (period === 'AM' && hour === 12) {
                    hour = 0;
                }
            }
            
            var timeValue = hour.toString().padStart(2, '0') + ':' + 
                           minute.toString().padStart(2, '0');
            
            this.setValue(timeValue);
        },
        
        // 更新选择状态
        updateSelection: function() {
            this.renderTimeLists();
        },
        
        // ==================== 时间验证方法 ====================
        
        // 检查小时是否禁用
        isHourDisabled: function(hour) {
            // TODO: 实现基于 minTime/maxTime 的验证
            return false;
        },
        
        // 检查分钟是否禁用
        isMinuteDisabled: function(minute) {
            // TODO: 实现基于 minTime/maxTime 的验证
            return false;
        },
        
        // 检查时间是否在范围内
        isTimeInRange: function(time) {
            var minTime = this.parseTime(this.getMinTime());
            var maxTime = this.parseTime(this.getMaxTime());
            var currentTime = this.parseTime(time);
            
            if (minTime && currentTime < minTime) return false;
            if (maxTime && currentTime > maxTime) return false;
            return true;
        },
        
        // ==================== 时间工具方法 ====================
        
        // 格式化时间
        getFormattedTime: function() {
            var hour = this.getCurrentHour();
            var minute = this.getCurrentMinute();
            var period = this.getCurrentPeriod();
            var use12Hours = this.getUse12Hours();
            
            if (use12Hours) {
                return hour.toString().padStart(2, '0') + ':' + 
                       minute.toString().padStart(2, '0') + ' ' + period;
            } else {
                return hour.toString().padStart(2, '0') + ':' + 
                       minute.toString().padStart(2, '0');
            }
        },
        
        // 解析时间值
        parseTimeValue: function(timeValue) {
            if (!timeValue) return { hour: 0, minute: 0, period: 'AM' };
            
            var parts = timeValue.split(':');
            if (parts.length === 2) {
                var hour = parseInt(parts[0]);
                var minute = parseInt(parts[1]);
                var use12Hours = this.getUse12Hours();
                var period = 'AM';
                
                if (use12Hours) {
                    if (hour >= 12) {
                        period = 'PM';
                        if (hour > 12) hour -= 12;
                    }
                    if (hour === 0) hour = 12;
                }
                
                return { hour, minute, period };
            }
            
            return { hour: 0, minute: 0, period: 'AM' };
        },
        
        // 解析时间字符串
        parseTime: function(timeStr) {
            if (!timeStr) return null;
            
            try {
                var parts = timeStr.split(':');
                if (parts.length === 2) {
                    var hours = parseInt(parts[0]);
                    var minutes = parseInt(parts[1]);
                    return new Date(0, 0, 0, hours, minutes);
                }
            } catch (e) {
                console.error('Time parse error:', e);
            }
            return null;
        },
        
        // 更新显示值
        updateDisplayValue: function() {
            var value = this.getValue();
            var displayValue = '';
            
            if (value) {
                var parsed = this.parseTimeValue(value);
                if (this.getUse12Hours()) {
                    displayValue = parsed.hour.toString().padStart(2, '0') + ':' + 
                                  parsed.minute.toString().padStart(2, '0') + ' ' + parsed.period;
                } else {
                    displayValue = value;
                }
            }
            
            this.getSubNode('INPUT').val(displayValue);
        },
        
        // ==================== 重写父类方法 ====================
        
        showPicker: function() {
            if (this.getDisabled()) return;
            
            // 初始化当前时间
            var value = this.getValue();
            if (value) {
                var parsed = this.parseTimeValue(value);
                this.setCurrentHour(parsed.hour);
                this.setCurrentMinute(parsed.minute);
                this.setCurrentPeriod(parsed.period);
            } else {
                // 默认当前时间
                var now = new Date();
                this.setCurrentHour(now.getHours());
                this.setCurrentMinute(now.getMinutes());
                this.setCurrentPeriod(now.getHours() >= 12 ? 'PM' : 'AM');
            }
            
            this.setVisible(true);
            this.triggerEvent('show');
        },
        
        confirmSelection: function() {
            var value = this.getValue();
            this.hidePicker();
            this.triggerEvent('confirm', value);
        },
        
        // ==================== 生命周期方法 ====================
        
        onRender: function() {
            this.renderTimeLists();
            this.updateDisplayValue();
        }
    },
    
    // ==================== 实例方法 ====================
    
    // 获取和设置时间相关属性
    getFormat: function() {
        return this.profile().format;
    },
    
    setFormat: function(format) {
        this.profile().format = format;
    },
    
    getStep: function() {
        return this.profile().step;
    },
    
    setStep: function(step) {
        this.profile().step = step;
    },
    
    getUse12Hours: function() {
        return this.profile().use12Hours;
    },
    
    setUse12Hours: function(use12Hours) {
        this.profile().use12Hours = use12Hours;
    },
    
    getCurrentHour: function() {
        return this.profile().currentHour;
    },
    
    setCurrentHour: function(hour) {
        this.profile().currentHour = hour;
    },
    
    getCurrentMinute: function() {
        return this.profile().currentMinute;
    },
    
    setCurrentMinute: function(minute) {
        this.profile().currentMinute = minute;
    },
    
    getCurrentPeriod: function() {
        return this.profile().currentPeriod;
    },
    
    setCurrentPeriod: function(period) {
        this.profile().currentPeriod = period;
    },
    
    // 工具方法
    setCurrentTime: function(hour, minute, period) {
        this.setCurrentHour(hour);
        this.setCurrentMinute(minute);
        if (period) this.setCurrentPeriod(period);
        this.updateTimeValue();
    },
    
    setNow: function() {
        var now = new Date();
        this.setCurrentHour(now.getHours());
        this.setCurrentMinute(now.getMinutes());
        this.setCurrentPeriod(now.getHours() >= 12 ? 'PM' : 'AM');
        this.updateTimeValue();
    },
    
    clearValue: function() {
        this.setValue(null);
    },
    
    // 验证方法
    isValidTime: function(timeValue) {
        var parsed = this.parseTimeValue(timeValue);
        return parsed.hour >= 0 && parsed.hour <= 23 && 
               parsed.minute >= 0 && parsed.minute <= 59;
    }
});

// 注册组件类型
ood.absBox.$type['MobileTimePicker'] = 'TDesignMobile.TimePicker';

// 全局样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .tdesign-timepicker__time-container {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 16px;
        }
        
        .tdesign-timepicker__hours,
        .tdesign-timepicker__minutes,
        .tdesign-timepicker__ampm {
            max-height: 240px;
            overflow-y: auto;
            scroll-behavior: smooth;
        }
        
        .tdesign-timepicker__hours-list,
        .tdesign-timepicker__minutes-list,
        .tdesign-timepicker__ampm-list {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }
        
        .tdesign-timepicker__hour-item,
        .tdesign-timepicker__minute-item,
        .tdesign-timepicker__ampm-item {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px 16px;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.2s ease;
            min-width: 60px;
            text-align: center;
        }
        
        .tdesign-timepicker__hour-item:hover,
        .tdesign-timepicker__minute-item:hover,
        .tdesign-timepicker__ampm-item:hover {
            background-color: var(--td-color-bg-secondary);
        }
        
        .tdesign-timepicker__hour-item--selected,
        .tdesign-timepicker__minute-item--selected,
        .tdesign-timepicker__ampm-item--selected {
            background-color: var(--td-color-brand);
            color: white;
            font-weight: 500;
        }
        
        .tdesign-timepicker__hour-item--disabled,
        .tdesign-timepicker__minute-item--disabled,
        .tdesign-timepicker__ampm-item--disabled {
            color: var(--td-color-text-secondary);
            cursor: not-allowed;
            opacity: 0.4;
        }
        
        .tdesign-timepicker__hour-item--disabled:hover,
        .tdesign-timepicker__minute-item--disabled:hover,
        .tdesign-timepicker__ampm-item--disabled:hover {
            background-color: transparent;
        }
        
        .tdesign-timepicker__separator {
            font-size: 20px;
            font-weight: 500;
            color: var(--td-color-text-primary);
        }
        
        @keyframes td-timepicker-slide-up {
            from {
                transform: translateY(100%);
            }
            to {
                transform: translateY(0);
            }
        }
    `;
    document.head.appendChild(style);
}

console.log('TDesign Mobile TimePicker 组件加载完成');