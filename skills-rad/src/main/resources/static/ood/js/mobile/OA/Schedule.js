/**
 * 移动端日程安排组件
 * 用于显示和管理用户的日程安排
 */
ood.Class("ood.Mobile.OA.Schedule", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initScheduleFeatures();
        },
        
        initScheduleFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-schedule ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Schedule组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                container = profile.getSubNode('CONTAINER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    container.css('width', '100%');
                }
            }

            // 如果提供了高度，调整容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    container.css('height', '100%');
                    
                    // 设置溢出滚动
                    container.css('overflow-y', 'auto');
                }
            }

            // 调整内部布局以适应新尺寸
            this.adjustLayout();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 日程项点击事件
            container.on('click', '.ood-mobile-schedule-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var schedule = self._schedules[index];
                
                if (schedule && !item.hasClass('ood-mobile-schedule-item-disabled')) {
                    self.onScheduleClick(index, schedule);
                }
            });
            
            // 日期导航
            container.on('click', '.ood-mobile-schedule-nav-prev', function(e) {
                self.previousMonth();
            });
            
            container.on('click', '.ood-mobile-schedule-nav-next', function(e) {
                self.nextMonth();
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-schedule-item', function(e) {
                ood(this).addClass('ood-mobile-schedule-item-active');
            });
            
            container.on('touchend', '.ood-mobile-schedule-item', function(e) {
                ood(this).removeClass('ood-mobile-schedule-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-schedule-item', function(e) {
                ood(this).removeClass('ood-mobile-schedule-item-active');
            });
            
            // 导航按钮触摸事件
            container.on('touchstart', '.ood-mobile-schedule-nav-prev, .ood-mobile-schedule-nav-next', function(e) {
                ood(this).addClass('ood-mobile-schedule-nav-active');
            });
            
            container.on('touchend', '.ood-mobile-schedule-nav-prev, .ood-mobile-schedule-nav-next', function(e) {
                ood(this).removeClass('ood-mobile-schedule-nav-active');
            });
            
            container.on('touchcancel', '.ood-mobile-schedule-nav-prev, .ood-mobile-schedule-nav-next', function(e) {
                ood(this).removeClass('ood-mobile-schedule-nav-active');
            });
        },
        
        setSchedules: function(schedules) {
            this._schedules = schedules || [];
            this.renderSchedules();
        },
        
        getSchedules: function() {
            return this._schedules || [];
        },
        
        renderSchedules: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            // 渲染日历头部
            var header = this.createCalendarHeader();
            container.append(header);
            
            // 渲染日程列表
            var scheduleList = this.createScheduleList();
            container.append(scheduleList);
        },
        
        createCalendarHeader: function() {
            var header = ood('<div class="ood-mobile-schedule-header"></div>');
            
            var prevBtn = ood('<button class="ood-mobile-schedule-nav-prev">‹</button>');
            var nextBtn = ood('<button class="ood-mobile-schedule-nav-next">›</button>');
            
            var title = ood('<div class="ood-mobile-schedule-title">2025年9月</div>');
            
            header.append(prevBtn);
            header.append(title);
            header.append(nextBtn);
            
            return header;
        },
        
        createScheduleList: function() {
            var list = ood('<div class="ood-mobile-schedule-list"></div>');
            
            // 按日期分组显示日程
            var groupedSchedules = this.groupSchedulesByDate();
            
            for (var date in groupedSchedules) {
                var dateGroup = ood('<div class="ood-mobile-schedule-date-group"></div>');
                
                var dateHeader = ood('<div class="ood-mobile-schedule-date-header">' + date + '</div>');
                dateGroup.append(dateHeader);
                
                var items = groupedSchedules[date];
                for (var i = 0; i < items.length; i++) {
                    var schedule = items[i];
                    var itemElement = this.createScheduleElement(schedule, i);
                    dateGroup.append(itemElement);
                }
                
                list.append(dateGroup);
            }
            
            return list;
        },
        
        groupSchedulesByDate: function() {
            var grouped = {};
            
            for (var i = 0; i < this._schedules.length; i++) {
                var schedule = this._schedules[i];
                var date = schedule.date || '未指定日期';
                
                if (!grouped[date]) {
                    grouped[date] = [];
                }
                grouped[date].push(schedule);
            }
            
            return grouped;
        },
        
        createScheduleElement: function(schedule, index) {
            var scheduleEl = ood('<div class="ood-mobile-schedule-item" data-index="' + index + '"></div>');
            
            // 时间
            if (schedule.time) {
                var time = ood('<div class="ood-mobile-schedule-time">' + schedule.time + '</div>');
                scheduleEl.append(time);
            }
            
            // 标题
            if (schedule.title) {
                var title = ood('<div class="ood-mobile-schedule-title">' + schedule.title + '</div>');
                scheduleEl.append(title);
            }
            
            // 地点
            if (schedule.location) {
                var location = ood('<div class="ood-mobile-schedule-location">' + schedule.location + '</div>');
                scheduleEl.append(location);
            }
            
            // 参与人
            if (schedule.participants && schedule.participants.length > 0) {
                var participants = ood('<div class="ood-mobile-schedule-participants">' + schedule.participants.join(', ') + '</div>');
                scheduleEl.append(participants);
            }
            
            // 重要性标记
            if (schedule.important) {
                scheduleEl.addClass('ood-mobile-schedule-important');
            }
            
            // 禁用状态
            if (schedule.disabled) {
                scheduleEl.addClass('ood-mobile-schedule-item-disabled');
            }
            
            return scheduleEl;
        },
        
        previousMonth: function() {
            // 切换到上一个月的逻辑
            this.onMonthChange(-1);
        },
        
        nextMonth: function() {
            // 切换到下一个月的逻辑
            this.onMonthChange(1);
        },
        
        onMonthChange: function(direction) {
            var profile = this.get(0);
            
            if (profile.onMonthChange) {
                profile.boxing().onMonthChange(profile, direction);
            }
        },
        
        onScheduleClick: function(index, schedule) {
            var profile = this.get(0);
            
            if (profile.onScheduleClick) {
                profile.boxing().onScheduleClick(profile, index, schedule);
            }
        },
        
        addSchedule: function(schedule) {
            this._schedules.push(schedule);
            this.renderSchedules();
        },
        
        removeSchedule: function(index) {
            if (index < 0 || index >= this._schedules.length) return;
            
            this._schedules.splice(index, 1);
            this.renderSchedules();
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentSchedules = self.getSchedules();
                if (typeof index === 'undefined') {
                    currentSchedules = currentSchedules.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentSchedules.splice.apply(currentSchedules, [insertIndex, 0].concat(items));
                }
                
                self.setSchedules(currentSchedules);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentSchedules = self.getSchedules();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentSchedules.length) {
                        currentSchedules.splice(idx, 1);
                    }
                }
                
                self.setSchedules(currentSchedules);
            });
        },
        
        clearItems: function() {
            return this.setSchedules([]);
        },
        
        getItems: function() {
            return this.getSchedules();
        },
        
        getSelectedItems: function() {
            return [];
        },
        
        selectItem: function(value) {
            return this;
        },
        
        unselectItem: function(value) {
            return this;
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-schedule',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-schedule-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            CONTAINER: {
                padding: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-schedule-header': {
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'space-between',
                'margin-bottom': 'var(--mobile-spacing-md)',
                padding: '0 var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-schedule-nav-prev, .ood-mobile-schedule-nav-next': {
                'background-color': 'var(--mobile-bg-secondary)',
                border: 'none',
                'border-radius': '50%',
                width: '32px',
                height: '32px',
                'font-size': 'var(--mobile-font-lg)',
                cursor: 'pointer',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-schedule-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)'
            },
            
            '.ood-mobile-schedule-date-group': {
                'margin-bottom': 'var(--mobile-spacing-lg)'
            },
            
            '.ood-mobile-schedule-date-header': {
                'font-size': 'var(--mobile-font-sm)',
                'font-weight': '600',
                color: 'var(--mobile-primary)',
                'padding': 'var(--mobile-spacing-sm) 0',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-schedule-item': {
                display: 'flex',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-schedule-item:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-schedule-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-schedule-item-active': {
                'background-color': 'var(--mobile-bg-secondary)',
                'transform': 'scale(0.98)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-schedule-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-schedule-item-important': {
                'border-left': '3px solid var(--mobile-danger)'
            },
            
            '.ood-mobile-schedule-time': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-secondary)',
                'min-width': '60px',
                'margin-right': 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-schedule-content': {
                flex: 1
            },
            
            '.ood-mobile-schedule-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-schedule-location': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-schedule-participants': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '日程安排标题',
                ini: '日程安排',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '日程安排');
                }
            },
            
            width: {
                caption: '组件宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '组件高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // 日程数据
            schedules: {
                caption: '日程数据',
                ini: [
                    {
                        id: '1',
                        date: '2025-09-15',
                        time: '09:00-10:00',
                        title: '部门周会',
                        location: '会议室A',
                        participants: ['张三', '李四', '王五'],
                        important: true,
                        disabled: false
                    },
                    {
                        id: '2',
                        date: '2025-09-15',
                        time: '14:00-15:30',
                        title: '项目评审',
                        location: '会议室B',
                        participants: ['赵六', '钱七'],
                        important: false,
                        disabled: false
                    },
                    {
                        id: '3',
                        date: '2025-09-16',
                        time: '10:00-11:00',
                        title: '客户拜访',
                        location: '客户公司',
                        participants: ['孙八'],
                        important: true,
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setSchedules(value);
                }
            },
            
            // 事件处理器
            onScheduleClick: {
                caption: '日程项点击事件处理器',
                ini: null
            },
            
            onMonthChange: {
                caption: '月份切换事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setSchedules(profile.properties.schedules);
            });
        }
    }
});