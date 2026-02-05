/**
 * TDesign Mobile DatePicker 日期选择器组件
 * 继承 TDesignMobile.Picker，实现日期选择功能
 */

ood.Class("TDesignMobile.DatePicker", "TDesignMobile.Picker", {
    Initialize: function() {
        this.$activeClass$ = 'TDesignMobile.DatePicker';
    },
    
    Static: {
        Templates: {
            // 继承并扩展 Picker 的模板
            tagName: 'div',
            className: 'tdesign-mobile-datepicker {_className}',
            style: '{_style}',
            
            TRIGGER: {
                tagName: 'div',
                className: 'tdesign-datepicker__trigger',
                style: 'position: relative;',
                
                INPUT: {
                    tagName: 'input',
                    className: 'tdesign-datepicker__input',
                    type: 'text',
                    placeholder: '{placeholder}',
                    readonly: 'readonly',
                    style: 'width: 100%;'
                },
                
                ARROW: {
                    tagName: 'span',
                    className: 'tdesign-datepicker__arrow',
                    html: '⌄',
                    style: 'position: absolute; right: 12px; top: 50%; transform: translateY(-50%); pointer-events: none;'
                }
            },
            
            PANEL: {
                tagName: 'div',
                className: 'tdesign-datepicker__panel',
                style: 'display: none; position: fixed; bottom: 0; left: 0; right: 0; background: #fff; z-index: 1000; border-radius: 16px 16px 0 0; max-height: 60vh;',
                
                HEADER: {
                    tagName: 'div',
                    className: 'tdesign-datepicker__header',
                    style: 'display: flex; justify-content: space-between; align-items: center; padding: 16px; border-bottom: 1px solid var(--td-color-border);',
                    
                    CANCEL: {
                        tagName: 'button',
                        className: 'tdesign-datepicker__cancel',
                        html: '{cancelText}',
                        style: 'background: none; border: none; color: var(--td-color-text-secondary); font-size: 16px;'
                    },
                    
                    TITLE: {
                        tagName: 'div',
                        className: 'tdesign-datepicker__title',
                        html: '{title}',
                        style: 'font-weight: 500; font-size: 16px;'
                    },
                    
                    CONFIRM: {
                        tagName: 'button',
                        className: 'tdesign-datepicker__confirm',
                        html: '{confirmText}',
                        style: 'background: none; border: none; color: var(--td-color-brand); font-size: 16px; font-weight: 500;'
                    }
                },
                
                CONTENT: {
                    tagName: 'div',
                    className: 'tdesign-datepicker__content',
                    style: 'padding: 20px;',
                    
                    // 日期选择器特定内容
                    DATE_CONTAINER: {
                        tagName: 'div',
                        className: 'tdesign-datepicker__date-container',
                        style: 'display: flex; flex-direction: column; gap: 16px;',
                        
                        YEAR_MONTH: {
                            tagName: 'div',
                            className: 'tdesign-datepicker__year-month',
                            style: 'display: flex; justify-content: center; align-items: center; gap: 12px; font-size: 18px; font-weight: 500;',
                            
                            PREV: {
                                tagName: 'button',
                                className: 'tdesign-datepicker__prev',
                                html: '‹',
                                style: 'background: none; border: none; font-size: 20px; color: var(--td-color-text-secondary); padding: 8px;'
                            },
                            
                            CURRENT: {
                                tagName: 'div',
                                className: 'tdesign-datepicker__current',
                                html: '{currentYear}年{currentMonth}月',
                                style: 'min-width: 120px; text-align: center;'
                            },
                            
                            NEXT: {
                                tagName: 'button',
                                className: 'tdesign-datepicker__next',
                                html: '›',
                                style: 'background: none; border: none; font-size: 20px; color: var(--td-color-text-secondary); padding: 8px;'
                            }
                        },
                        
                        WEEK_DAYS: {
                            tagName: 'div',
                            className: 'tdesign-datepicker__week-days',
                            style: 'display: grid; grid-template-columns: repeat(7, 1fr); text-align: center; font-size: 14px; color: var(--td-color-text-secondary); margin-bottom: 12px;'
                        },
                        
                        DAYS_GRID: {
                            tagName: 'div',
                            className: 'tdesign-datepicker__days-grid',
                            style: 'display: grid; grid-template-columns: repeat(7, 1fr); gap: 4px;'
                        }
                    }
                }
            },
            
            OVERLAY: {
                tagName: 'div',
                className: 'tdesign-datepicker__overlay',
                style: 'display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); z-index: 999;'
            }
        },
        
        Appearances: {
            // 继承 Picker 的基础样式
            KEY: arguments.callee.upper.KEY,
            TRIGGER: arguments.callee.upper.TRIGGER,
            INPUT: arguments.callee.upper.INPUT,
            ARROW: arguments.callee.upper.ARROW,
            PANEL: arguments.callee.upper.PANEL,
            OVERLAY: arguments.callee.upper.OVERLAY,
            
            // 日期选择器特定样式
            DAY: {
                'display': 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                'height': '40px',
                'border-radius': '8px',
                'font-size': '16px',
                'cursor': 'pointer',
                'transition': 'all 0.2s ease',
                '&:hover': {
                    'background-color': 'var(--td-color-bg-secondary)'
                }
            },
            
            DAY_SELECTED: {
                'background-color': 'var(--td-color-brand)',
                'color': 'white',
                'font-weight': '500'
            },
            
            DAY_DISABLED: {
                'color': 'var(--td-color-text-secondary)',
                'cursor': 'not-allowed',
                'opacity': '0.4',
                '&:hover': {
                    'background-color': 'transparent'
                }
            },
            
            DAY_TODAY: {
                'border': '1px solid var(--td-color-brand)',
                'color': 'var(--td-color-brand)'
            },
            
            NAV_BUTTON: {
                'background': 'none',
                'border': 'none',
                'font-size': '20px',
                'color': 'var(--td-color-text-secondary)',
                'cursor': 'pointer',
                'padding': '8px',
                'border-radius': '6px',
                'transition': 'background-color 0.2s ease',
                '&:hover': {
                    'background-color': 'var(--td-color-bg-secondary)'
                }
            }
        },
        
        Behaviors: {
            // 继承基础行为
            TRIGGER: arguments.callee.upper.TRIGGER,
            CANCEL: arguments.callee.upper.CANCEL,
            CONFIRM: arguments.callee.upper.CONFIRM,
            OVERLAY: arguments.callee.upper.OVERLAY,
            
            // 日期选择器特定行为
            PREV: {
                click: function(profile, e) {
                    this.navigateMonth(-1);
                }
            },
            
            NEXT: {
                click: function(profile, e) {
                    this.navigateMonth(1);
                }
            },
            
            DAY: {
                click: function(profile, e) {
                    var dateStr = this.attr('data-date');
                    if (!this.hasClass('tdesign-datepicker__day--disabled')) {
                        this.selectDate(dateStr);
                    }
                }
            }
        },
        
        // 数据模型 - 符合ood规范
        DataModel: {
            // 设计器响应动作
            action: {
                caption: "动作配置",
                type: "object",
                properties: {
                    change: {
                        caption: "日期变化事件",
                        type: "function"
                    },
                    confirm: {
                        caption: "确认事件",
                        type: "function"
                    },
                    cancel: {
                        caption: "取消事件",
                        type: "function"
                    },
                    monthChange: {
                        caption: "月份变化事件",
                        type: "function"
                    },
                    dateSelect: {
                        caption: "日期选择事件",
                        type: "function"
                    }
                }
            },
            // 继承基础属性
            value: {
                caption: "选中值",
                type: "string",
                default: null
            },
            placeholder: {
                caption: "占位文本",
                type: "string",
                default: "请选择日期"
            },
            title: {
                caption: "标题",
                type: "string",
                default: "选择日期"
            },
            cancelText: {
                caption: "取消按钮文本",
                type: "string",
                default: "取消"
            },
            confirmText: {
                caption: "确认按钮文本",
                type: "string",
                default: "确定"
            },
            disabled: {
                caption: "禁用状态",
                type: "boolean",
                default: false
            },
            visible: {
                caption: "显示状态",
                type: "boolean",
                default: false
            },
            // 日期选择器特定属性
            format: {
                caption: "日期格式",
                type: "string",
                default: "YYYY-MM-DD"
            },
            minDate: {
                caption: "最小日期",
                type: "date",
                default: null
            },
            maxDate: {
                caption: "最大日期",
                type: "date",
                default: null
            },
            currentDate: {
                caption: "当前日期",
                type: "date",
                default: new Date()
            },
            showToday: {
                caption: "显示今天",
                type: "boolean",
                default: true
            },
            allowClear: {
                caption: "允许清除",
                type: "boolean",
                default: true
            }
        },
        
        RenderTrigger: {
            // 继承基础渲染触发器
            value: arguments.callee.upper.RenderTrigger.value,
            visible: arguments.callee.upper.RenderTrigger.visible,
            
            // 日期选择器特定渲染触发器
            currentDate: ['renderCalendar'],
            minDate: ['renderCalendar'],
            maxDate: ['renderCalendar'],
            format: ['updateDisplayValue']
        },
        
        _prepareData: function(profile, data) {
            var data = arguments.callee.upper.call(this, profile);
            
            // 准备日期选择器数据
            data._currentYear = this.getCurrentDate().getFullYear();
            data._currentMonth = this.getCurrentDate().getMonth() + 1;
            data._currentDateFormatted = this.formatDate(this.getCurrentDate());
            
            return data;
        },
        
        EventHandlers: {
            // 继承基础事件处理器
            change: arguments.callee.upper.EventHandlers.change,
            confirm: arguments.callee.upper.EventHandlers.confirm,
            cancel: arguments.callee.upper.EventHandlers.cancel,
            show: arguments.callee.upper.EventHandlers.show,
            hide: arguments.callee.upper.EventHandlers.hide,
            
            // 日期选择器特定事件
            monthChange: function(date) {
                console.log('Month changed to:', date);
            },
            
            dateSelect: function(date) {
                console.log('Date selected:', date);
            }
        },
        
        // ==================== 日期选择器特定方法 ====================
        
        // 渲染日历
        renderCalendar: function() {
            var currentDate = this.getCurrentDate();
            var year = currentDate.getFullYear();
            var month = currentDate.getMonth();
            
            this.renderWeekDays();
            this.renderDays(year, month);
            this.updateNavigation();
        },
        
        // 渲染星期标题
        renderWeekDays: function() {
            var weekDaysNode = this.getSubNode('WEEK_DAYS');
            var weekDays = ['日', '一', '二', '三', '四', '五', '六'];
            
            weekDaysNode.empty();
            
            weekDays.forEach(function(day) {
                var dayNode = $('<div>')
                    .text(day)
                    .css({
                        'padding': '8px 0',
                        'font-size': '14px',
                        'color': 'var(--td-color-text-secondary)'
                    });
                
                weekDaysNode.append(dayNode);
            });
        },
        
        // 渲染日期网格
        renderDays: function(year, month) {
            var daysGridNode = this.getSubNode('DAYS_GRID');
            var firstDay = new Date(year, month, 1);
            var lastDay = new Date(year, month + 1, 0);
            var daysInMonth = lastDay.getDate();
            var startingDay = firstDay.getDay();
            
            daysGridNode.empty();
            
            // 填充空白格子
            for (var i = 0; i < startingDay; i++) {
                daysGridNode.append($('<div>'));
            }
            
            // 渲染日期
            var today = new Date();
            var selectedDate = this.parseDate(this.getValue());
            var minDate = this.parseDate(this.getMinDate());
            var maxDate = this.parseDate(this.getMaxDate());
            
            for (var day = 1; day <= daysInMonth; day++) {
                var date = new Date(year, month, day);
                var dateStr = this.formatDate(date);
                var isToday = this.isSameDay(date, today);
                var isSelected = selectedDate && this.isSameDay(date, selectedDate);
                var isDisabled = this.isDateDisabled(date, minDate, maxDate);
                
                var dayNode = $('<div>')
                    .addClass('tdesign-datepicker__day')
                    .attr('data-date', dateStr)
                    .text(day)
                    .css(this.Appearances.DAY);
                
                if (isToday) {
                    dayNode.addClass('tdesign-datepicker__day--today');
                    dayNode.css(this.Appearances.DAY_TODAY);
                }
                
                if (isSelected) {
                    dayNode.addClass('tdesign-datepicker__day--selected');
                    dayNode.css(this.Appearances.DAY_SELECTED);
                }
                
                if (isDisabled) {
                    dayNode.addClass('tdesign-datepicker__day--disabled');
                    dayNode.css(this.Appearances.DAY_DISABLED);
                }
                
                daysGridNode.append(dayNode);
            }
        },
        
        // 更新导航显示
        updateNavigation: function() {
            var currentDate = this.getCurrentDate();
            var year = currentDate.getFullYear();
            var month = currentDate.getMonth() + 1;
            
            this.getSubNode('CURRENT').html(year + '年' + month + '月');
        },
        
        // 月份导航
        navigateMonth: function(delta) {
            var currentDate = new Date(this.getCurrentDate());
            currentDate.setMonth(currentDate.getMonth() + delta);
            this.setCurrentDate(currentDate);
            this.triggerEvent('monthChange', currentDate);
        },
        
        // 选择日期
        selectDate: function(dateStr) {
            this.setValue(dateStr);
            this.triggerEvent('dateSelect', dateStr);
        },
        
        // ==================== 日期工具方法 ====================
        
        // 格式化日期
        formatDate: function(date, format) {
            if (!date) return '';
            
            format = format || this.getFormat();
            var year = date.getFullYear();
            var month = (date.getMonth() + 1).toString().padStart(2, '0');
            var day = date.getDate().toString().padStart(2, '0');
            
            return format
                .replace('YYYY', year)
                .replace('MM', month)
                .replace('DD', day);
        },
        
        // 解析日期字符串
        parseDate: function(dateStr) {
            if (!dateStr) return null;
            
            try {
                var parts = dateStr.split('-');
                if (parts.length === 3) {
                    return new Date(parts[0], parts[1] - 1, parts[2]);
                }
            } catch (e) {
                console.error('Date parse error:', e);
            }
            return null;
        },
        
        // 判断是否为同一天
        isSameDay: function(date1, date2) {
            if (!date1 || !date2) return false;
            return date1.getFullYear() === date2.getFullYear() &&
                   date1.getMonth() === date2.getMonth() &&
                   date1.getDate() === date2.getDate();
        },
        
        // 判断日期是否禁用
        isDateDisabled: function(date, minDate, maxDate) {
            if (minDate && date < minDate) return true;
            if (maxDate && date > maxDate) return true;
            return false;
        },
        
        // 更新显示值
        updateDisplayValue: function() {
            var value = this.getValue();
            var displayValue = '';
            
            if (value) {
                var date = this.parseDate(value);
                if (date) {
                    displayValue = this.formatDate(date);
                }
            }
            
            this.getSubNode('INPUT').val(displayValue);
        },
        
        // ==================== 重写父类方法 ====================
        
        showPicker: function() {
            if (this.getDisabled()) return;
            
            // 初始化当前显示日期
            var value = this.getValue();
            if (value) {
                var date = this.parseDate(value);
                if (date) {
                    this.setCurrentDate(date);
                }
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
            this.renderCalendar();
            this.updateDisplayValue();
        }
    },
    
    // ==================== 实例方法 ====================
    
    // 获取和设置日期相关属性
    getFormat: function() {
        return this.profile().format;
    },
    
    setFormat: function(format) {
        this.profile().format = format;
    },
    
    getMinDate: function() {
        return this.profile().minDate;
    },
    
    setMinDate: function(minDate) {
        this.profile().minDate = minDate;
    },
    
    getMaxDate: function() {
        return this.profile().maxDate;
    },
    
    setMaxDate: function(maxDate) {
        this.profile().maxDate = maxDate;
    },
    
    getCurrentDate: function() {
        return this.profile().currentDate;
    },
    
    setCurrentDate: function(currentDate) {
        this.profile().currentDate = currentDate;
    },
    
    // 工具方法
    setToday: function() {
        this.setValue(this.formatDate(new Date()));
    },
    
    clearValue: function() {
        this.setValue(null);
    },
    
    // 验证方法
    isValidDate: function(dateStr) {
        var date = this.parseDate(dateStr);
        return date && !isNaN(date.getTime());
    }
});

// 注册组件类型
ood.absBox.$type['MobileDatePicker'] = 'TDesignMobile.DatePicker';

// 全局样式
if (typeof document !== 'undefined') {
    var style = document.createElement('style');
    style.textContent = `
        .tdesign-datepicker__days-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 4px;
        }
        
        .tdesign-datepicker__day {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 40px;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.2s ease;
        }
        
        .tdesign-datepicker__day:hover {
            background-color: var(--td-color-bg-secondary);
        }
        
        .tdesign-datepicker__day--selected {
            background-color: var(--td-color-brand);
            color: white;
            font-weight: 500;
        }
        
        .tdesign-datepicker__day--disabled {
            color: var(--td-color-text-secondary);
            cursor: not-allowed;
            opacity: 0.4;
        }
        
        .tdesign-datepicker__day--disabled:hover {
            background-color: transparent;
        }
        
        .tdesign-datepicker__day--today {
            border: 1px solid var(--td-color-brand);
            color: var(--td-color-brand);
        }
        
        .tdesign-datepicker__nav-button {
            background: none;
            border: none;
            font-size: 20px;
            color: var(--td-color-text-secondary);
            cursor: pointer;
            padding: 8px;
            border-radius: 6px;
            transition: background-color 0.2s ease;
        }
        
        .tdesign-datepicker__nav-button:hover {
            background-color: var(--td-color-bg-secondary);
        }
        
        @keyframes td-datepicker-slide-up {
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

console.log('TDesign Mobile DatePicker 组件加载完成');