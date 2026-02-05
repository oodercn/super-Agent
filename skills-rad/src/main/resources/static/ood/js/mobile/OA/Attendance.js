/**
 * 移动端考勤打卡组件
 * 用于员工上下班打卡和考勤记录查看
 */
ood.Class("ood.Mobile.OA.Attendance", "ood.UI", {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initAttendanceFeatures();
        },
        
        initAttendanceFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-attendance ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Attendance组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                container = profile.getSubNode('CONTAINER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us >  0 ? 'em' : 'px', emRate);
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
                    
                    // 设置记录列表的溢出滚动
                    var recordsList = container.find('.ood-mobile-attendance-records-list');
                    if (!recordsList.isEmpty()) {
                        recordsList.css('max-height', '300px');
                        recordsList.css('overflow-y', 'auto');
                    }
                }
            }

            // 调整内部布局以适应新尺寸
            this.adjustLayout();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 打卡按钮点击事件
            container.on('click', '.ood-mobile-attendance-punch-btn', function(e) {
                var btn = ood(this);
                var type = btn.attr('data-type');
                
                if (!btn.hasClass('ood-mobile-attendance-punch-btn-disabled')) {
                    self.punch(type);
                }
            });
            
            // 日期导航
            container.on('click', '.ood-mobile-attendance-nav-prev', function(e) {
                self.previousMonth();
            });
            
            container.on('click', '.ood-mobile-attendance-nav-next', function(e) {
                self.nextMonth();
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-attendance-punch-btn', function(e) {
                ood(this).addClass('ood-mobile-attendance-punch-btn-active');
            });
            
            container.on('touchend', '.ood-mobile-attendance-punch-btn', function(e) {
                ood(this).removeClass('ood-mobile-attendance-punch-btn-active');
            });
            
            container.on('touchcancel', '.ood-mobile-attendance-punch-btn', function(e) {
                ood(this).removeClass('ood-mobile-attendance-punch-btn-active');
            });
            
            // 导航按钮触摸事件
            container.on('touchstart', '.ood-mobile-attendance-nav-prev, .ood-mobile-attendance-nav-next', function(e) {
                ood(this).addClass('ood-mobile-attendance-nav-active');
            });
            
            container.on('touchend', '.ood-mobile-attendance-nav-prev, .ood-mobile-attendance-nav-next', function(e) {
                ood(this).removeClass('ood-mobile-attendance-nav-active');
            });
            
            container.on('touchcancel', '.ood-mobile-attendance-nav-prev, .ood-mobile-attendance-nav-next', function(e) {
                ood(this).removeClass('ood-mobile-attendance-nav-active');
            });
        },
        
        setAttendanceData: function(data) {
            this._attendanceData = data || {};
            this.renderAttendance();
        },
        
        getAttendanceData: function() {
            return this._attendanceData || {};
        },
        
        renderAttendance: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            // 渲染打卡面板
            var punchPanel = this.createPunchPanel();
            container.append(punchPanel);
            
            // 渲染统计信息
            var statsPanel = this.createStatsPanel();
            container.append(statsPanel);
            
            // 渲染考勤记录
            var recordsPanel = this.createRecordsPanel();
            container.append(recordsPanel);
        },
        
        createPunchPanel: function() {
            var panel = ood('<div class="ood-mobile-attendance-punch-panel"></div>');
            
            // 当前时间
            var now = new Date();
            var timeStr = now.getFullYear() + '年' + (now.getMonth() + 1) + '月' + now.getDate() + '日 ' +
                         now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');
            var time = ood('<div class="ood-mobile-attendance-time">' + timeStr + '</div>');
            panel.append(time);
            
            // 打卡按钮容器
            var btnContainer = ood('<div class="ood-mobile-attendance-punch-container"></div>');
            
            // 上班打卡按钮
            var punchInBtn = ood('<button class="ood-mobile-attendance-punch-btn ood-mobile-attendance-punch-in" data-type="in">上班打卡</button>');
            
            // 下班打卡按钮
            var punchOutBtn = ood('<button class="ood-mobile-attendance-punch-btn ood-mobile-attendance-punch-out" data-type="out">下班打卡</button>');
            
            // 根据当前打卡状态设置按钮状态
            var todayRecord = this.getTodayRecord();
            if (todayRecord && todayRecord.punchInTime) {
                punchInBtn.addClass('ood-mobile-attendance-punch-btn-done');
                punchInBtn.text('已打卡 ' + todayRecord.punchInTime);
                punchInBtn.addClass('ood-mobile-attendance-punch-btn-disabled');
            }
            
            if (todayRecord && todayRecord.punchOutTime) {
                punchOutBtn.addClass('ood-mobile-attendance-punch-btn-done');
                punchOutBtn.text('已打卡 ' + todayRecord.punchOutTime);
                punchOutBtn.addClass('ood-mobile-attendance-punch-btn-disabled');
            }
            
            btnContainer.append(punchInBtn);
            btnContainer.append(punchOutBtn);
            
            panel.append(btnContainer);
            
            return panel;
        },
        
        createStatsPanel: function() {
            var panel = ood('<div class="ood-mobile-attendance-stats-panel"></div>');
            
            var title = ood('<div class="ood-mobile-attendance-panel-title">本月统计</div>');
            panel.append(title);
            
            var stats = ood('<div class="ood-mobile-attendance-stats"></div>');
            
            // 出勤天数
            var attendanceDays = ood('<div class="ood-mobile-attendance-stat-item">' +
                '<div class="ood-mobile-attendance-stat-value">' + (this._attendanceData.attendanceDays || 0) + '</div>' +
                '<div class="ood-mobile-attendance-stat-label">出勤(天)</div>' +
                '</div>');
            stats.append(attendanceDays);
            
            // 迟到次数
            var lateCount = ood('<div class="ood-mobile-attendance-stat-item">' +
                '<div class="ood-mobile-attendance-stat-value">' + (this._attendanceData.lateCount || 0) + '</div>' +
                '<div class="ood-mobile-attendance-stat-label">迟到(次)</div>' +
                '</div>');
            stats.append(lateCount);
            
            // 早退次数
            var earlyLeaveCount = ood('<div class="ood-mobile-attendance-stat-item">' +
                '<div class="ood-mobile-attendance-stat-value">' + (this._attendanceData.earlyLeaveCount || 0) + '</div>' +
                '<div class="ood-mobile-attendance-stat-label">早退(次)</div>' +
                '</div>');
            stats.append(earlyLeaveCount);
            
            panel.append(stats);
            
            return panel;
        },
        
        createRecordsPanel: function() {
            var panel = ood('<div class="ood-mobile-attendance-records-panel"></div>');
            
            // 标签页
            var tabs = ood('<div class="ood-mobile-attendance-tabs"></div>');
            var dayTab = ood('<div class="ood-mobile-attendance-tab ood-mobile-attendance-tab-active" data-view="day">日视图</div>');
            var monthTab = ood('<div class="ood-mobile-attendance-tab" data-view="month">月视图</div>');
            tabs.append(dayTab);
            tabs.append(monthTab);
            panel.append(tabs);
            
            // 记录列表
            var recordsList = ood('<div class="ood-mobile-attendance-records-list"></div>');
            
            // 显示最近的考勤记录
            var records = this._attendanceData.records || [];
            var recentRecords = records.slice(0, 10); // 显示最近10条记录
            
            for (var i = 0; i < recentRecords.length; i++) {
                var record = recentRecords[i];
                var recordElement = this.createRecordElement(record);
                recordsList.append(recordElement);
            }
            
            panel.append(recordsList);
            
            return panel;
        },
        
        createRecordElement: function(record) {
            var recordEl = ood('<div class="ood-mobile-attendance-record"></div>');
            
            // 日期
            var date = ood('<div class="ood-mobile-attendance-record-date">' + record.date + '</div>');
            recordEl.append(date);
            
            // 打卡时间
            var times = ood('<div class="ood-mobile-attendance-record-times"></div>');
            
            if (record.punchInTime) {
                var punchIn = ood('<div class="ood-mobile-attendance-record-time">上班: ' + record.punchInTime + '</div>');
                times.append(punchIn);
            }
            
            if (record.punchOutTime) {
                var punchOut = ood('<div class="ood-mobile-attendance-record-time">下班: ' + record.punchOutTime + '</div>');
                times.append(punchOut);
            }
            
            recordEl.append(times);
            
            // 状态
            var status = ood('<div class="ood-mobile-attendance-record-status ood-mobile-attendance-record-status-' + (record.status || 'normal') + '">' + 
                this.getStatusText(record.status) + '</div>');
            recordEl.append(status);
            
            return recordEl;
        },
        
        getTodayRecord: function() {
            var today = new Date();
            var todayStr = today.getFullYear() + '-' + 
                          (today.getMonth() + 1).toString().padStart(2, '0') + '-' + 
                          today.getDate().toString().padStart(2, '0');
            
            var records = this._attendanceData.records || [];
            for (var i = 0; i < records.length; i++) {
                if (records[i].date === todayStr) {
                    return records[i];
                }
            }
            return null;
        },
        
        getStatusText: function(status) {
            var statusMap = {
                'normal': '正常',
                'late': '迟到',
                'early': '早退',
                'absent': '缺勤'
            };
            return statusMap[status] || status;
        },
        
        punch: function(type) {
            var profile = this.get(0);
            var now = new Date();
            var timeStr = now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');
            
            // 更新考勤数据
            var todayRecord = this.getTodayRecord();
            if (!todayRecord) {
                todayRecord = {
                    date: now.getFullYear() + '-' + 
                          (now.getMonth() + 1).toString().padStart(2, '0') + '-' + 
                          now.getDate().toString().padStart(2, '0'),
                    punchInTime: null,
                    punchOutTime: null,
                    status: 'normal'
                };
                if (!this._attendanceData.records) {
                    this._attendanceData.records = [];
                }
                this._attendanceData.records.unshift(todayRecord);
            }
            
            if (type === 'in') {
                todayRecord.punchInTime = timeStr;
            } else if (type === 'out') {
                todayRecord.punchOutTime = timeStr;
            }
            
            // 重新渲染
            this.renderAttendance();
            
            // 触发打卡事件
            if (profile.onPunch) {
                profile.boxing().onPunch(profile, type, timeStr, todayRecord);
            }
        },
        
        switchView: function(view) {
            var profile = this.get(0);
            
            if (profile.onViewChange) {
                profile.boxing().onViewChange(profile, view);
            }
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-attendance',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-attendance-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            CONTAINER: {
                height: '100%',
                position: 'relative',
                padding: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-attendance-punch-panel': {
                'background-color': 'var(--mobile-bg-secondary)',
                'border-radius': 'var(--mobile-border-radius)',
                padding: 'var(--mobile-spacing-lg)',
                'margin-bottom': 'var(--mobile-spacing-lg)',
                'text-align': 'center'
            },
            
            '.ood-mobile-attendance-time': {
                'font-size': 'var(--mobile-font-md)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-lg)'
            },
            
            '.ood-mobile-attendance-punch-container': {
                display: 'flex',
                'justify-content': 'space-around'
            },
            
            '.ood-mobile-attendance-punch-btn': {
                'border': 'none',
                'border-radius': '50%',
                width: '80px',
                height: '80px',
                'font-size': 'var(--mobile-font-sm)',
                cursor: 'pointer',
                display: 'flex',
                'flex-direction': 'column',
                'align-items': 'center',
                'justify-content': 'center',
                transition: 'all 0.2s ease'
            },
            
            '.ood-mobile-attendance-punch-in': {
                'background-color': 'var(--mobile-success)',
                color: 'white'
            },
            
            '.ood-mobile-attendance-punch-out': {
                'background-color': 'var(--mobile-danger)',
                color: 'white'
            },
            
            '.ood-mobile-attendance-punch-btn:hover': {
                transform: 'scale(1.05)'
            },
            
            '.ood-mobile-attendance-punch-btn-disabled': {
                opacity: 0.6,
                cursor: 'not-allowed',
                transform: 'none !important'
            },
            
            '.ood-mobile-attendance-punch-btn-done': {
                'background-color': 'var(--mobile-text-tertiary)',
                'font-size': 'var(--mobile-font-xs)'
            },
            
            '.ood-mobile-attendance-stats-panel': {
                'background-color': 'var(--mobile-bg-secondary)',
                'border-radius': 'var(--mobile-border-radius)',
                padding: 'var(--mobile-spacing-md)',
                'margin-bottom': 'var(--mobile-spacing-lg)'
            },
            
            '.ood-mobile-attendance-panel-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-attendance-stats': {
                display: 'flex',
                'justify-content': 'space-around'
            },
            
            '.ood-mobile-attendance-stat-item': {
                'text-align': 'center'
            },
            
            '.ood-mobile-attendance-stat-value': {
                'font-size': 'var(--mobile-font-lg)',
                'font-weight': '600',
                color: 'var(--mobile-primary)'
            },
            
            '.ood-mobile-attendance-stat-label': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-attendance-records-panel': {
                'background-color': 'var(--mobile-bg-secondary)',
                'border-radius': 'var(--mobile-border-radius)',
                padding: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-attendance-tabs': {
                display: 'flex',
                'margin-bottom': 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-attendance-tab': {
                flex: 1,
                'text-align': 'center',
                padding: 'var(--mobile-spacing-sm)',
                cursor: 'pointer',
                'border-bottom': '2px solid transparent'
            },
            
            '.ood-mobile-attendance-tab-active': {
                'border-bottom-color': 'var(--mobile-primary)',
                color: 'var(--mobile-primary)'
            },
            
            '.ood-mobile-attendance-record': {
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-attendance-record:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-attendance-record-date': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-attendance-record-times': {
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-attendance-record-time': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-attendance-record-status': {
                display: 'inline-block',
                'font-size': 'var(--mobile-font-xs)',
                'padding': '2px 8px',
                'border-radius': 'var(--mobile-border-radius)',
                'font-weight': '500'
            },
            
            '.ood-mobile-attendance-record-status-normal': {
                'background-color': 'var(--mobile-success-light)',
                color: 'var(--mobile-success)'
            },
            
            '.ood-mobile-attendance-record-status-late': {
                'background-color': 'var(--mobile-warning-light)',
                color: 'var(--mobile-warning)'
            },
            
            '.ood-mobile-attendance-record-status-early': {
                'background-color': 'var(--mobile-warning-light)',
                color: 'var(--mobile-warning)'
            },
            
            '.ood-mobile-attendance-record-status-absent': {
                'background-color': 'var(--mobile-danger-light)',
                color: 'var(--mobile-danger)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '考勤打卡标题',
                ini: '考勤打卡',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '考勤打卡');
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
                ini: '100%'
            },
            
            // 考勤数据
            attendanceData: {
                caption: '考勤数据',
                ini: {
                    attendanceDays: 18,
                    lateCount: 2,
                    earlyLeaveCount: 1,
                    records: [
                        {
                            date: '2025-09-14',
                            punchInTime: '08:55',
                            punchOutTime: '18:10',
                            status: 'normal'
                        },
                        {
                            date: '2025-09-13',
                            punchInTime: '09:15',
                            punchOutTime: '18:05',
                            status: 'late'
                        },
                        {
                            date: '2025-09-12',
                            punchInTime: '08:50',
                            punchOutTime: '17:30',
                            status: 'early'
                        }
                    ]
                },
                action: function(value) {
                    this.boxing().setAttendanceData(value);
                }
            },
            
            // 事件处理器
            onPunch: {
                caption: '打卡事件处理器',
                ini: null
            },
            
            onViewChange: {
                caption: '视图切换事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setAttendanceData(profile.properties.attendanceData);
            });
        }
    }
});