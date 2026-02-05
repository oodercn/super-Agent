/**
 * 移动端会议管理组件
 * 用于显示和管理企业会议安排
 */
ood.Class("ood.Mobile.OA.MeetingManager", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initMeetingFeatures();
        },
        
        initMeetingFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-meeting ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // MeetingManager组件的尺寸调整逻辑

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
                    
                    // 设置会议列表的溢出滚动
                    var meetingList = container.find('.ood-mobile-meeting-list');
                    if (!meetingList.isEmpty()) {
                        meetingList.css('flex', '1');
                        meetingList.css('overflow-y', 'auto');
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
            
            // 会议项点击事件
            container.on('click', '.ood-mobile-meeting-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var meeting = self._meetings[index];
                
                if (meeting && !item.hasClass('ood-mobile-meeting-item-disabled')) {
                    self.onMeetingClick(index, meeting);
                }
            });
            
            // 操作按钮点击事件
            container.on('click', '.ood-mobile-meeting-action', function(e) {
                e.stopPropagation();
                var button = ood(this);
                var action = button.attr('data-action');
                var item = button.closest('.ood-mobile-meeting-item');
                var index = parseInt(item.attr('data-index'));
                var meeting = self._meetings[index];
                
                if (meeting && !item.hasClass('ood-mobile-meeting-item-disabled')) {
                    self.onActionClick(index, meeting, action);
                }
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-meeting-item', function(e) {
                ood(this).addClass('ood-mobile-meeting-item-active');
            });
            
            container.on('touchend', '.ood-mobile-meeting-item', function(e) {
                ood(this).removeClass('ood-mobile-meeting-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-meeting-item', function(e) {
                ood(this).removeClass('ood-mobile-meeting-item-active');
            });
            
            // 操作按钮触摸事件
            container.on('touchstart', '.ood-mobile-meeting-action', function(e) {
                ood(this).addClass('ood-mobile-meeting-action-active');
            });
            
            container.on('touchend', '.ood-mobile-meeting-action', function(e) {
                ood(this).removeClass('ood-mobile-meeting-action-active');
            });
            
            container.on('touchcancel', '.ood-mobile-meeting-action', function(e) {
                ood(this).removeClass('ood-mobile-meeting-action-active');
            });
        },
        
        setMeetings: function(meetings) {
            this._meetings = meetings || [];
            this.renderMeetings();
        },
        
        getMeetings: function() {
            return this._meetings || [];
        },
        
        renderMeetings: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            // 渲染导航栏
            var navbar = this.createNavbar();
            container.append(navbar);
            
            // 渲染会议列表
            var meetingList = this.createMeetingList();
            container.append(meetingList);
            
            // 渲染底部操作栏
            var footer = this.createFooter();
            container.append(footer);
        },
        
        createNavbar: function() {
            var navbar = ood('<div class="ood-mobile-meeting-navbar"></div>');
            
            var todayBtn = ood('<button class="ood-mobile-meeting-nav-btn ood-mobile-meeting-nav-btn-active" data-view="today">今天</button>');
            var weekBtn = ood('<button class="ood-mobile-meeting-nav-btn" data-view="week">本周</button>');
            var monthBtn = ood('<button class="ood-mobile-meeting-nav-btn" data-view="month">本月</button>');
            
            navbar.append(todayBtn);
            navbar.append(weekBtn);
            navbar.append(monthBtn);
            
            return navbar;
        },
        
        createMeetingList: function() {
            var list = ood('<div class="ood-mobile-meeting-list"></div>');
            
            for (var i = 0; i < this._meetings.length; i++) {
                var meeting = this._meetings[i];
                var meetingElement = this.createMeetingElement(meeting, i);
                list.append(meetingElement);
            }
            
            return list;
        },
        
        createMeetingElement: function(meeting, index) {
            var meetingEl = ood('<div class="ood-mobile-meeting-item" data-index="' + index + '"></div>');
            
            // 会议时间
            var timeContainer = ood('<div class="ood-mobile-meeting-time-container"></div>');
            
            if (meeting.startTime && meeting.endTime) {
                var time = ood('<div class="ood-mobile-meeting-time">' + meeting.startTime + ' - ' + meeting.endTime + '</div>');
                timeContainer.append(time);
            }
            
            // 状态指示器
            var statusIndicator = ood('<div class="ood-mobile-meeting-status-indicator ood-mobile-meeting-status-' + (meeting.status || 'scheduled') + '"></div>');
            timeContainer.append(statusIndicator);
            
            meetingEl.append(timeContainer);
            
            // 会议信息容器
            var info = ood('<div class="ood-mobile-meeting-info"></div>');
            
            // 会议主题
            if (meeting.title) {
                var title = ood('<div class="ood-mobile-meeting-title">' + meeting.title + '</div>');
                info.append(title);
            }
            
            // 会议室
            if (meeting.room) {
                var room = ood('<div class="ood-mobile-meeting-room">📍 ' + meeting.room + '</div>');
                info.append(room);
            }
            
            // 参会人员
            if (meeting.participants && meeting.participants.length > 0) {
                var participants = ood('<div class="ood-mobile-meeting-participants">👥 ' + meeting.participants.length + '人参会</div>');
                info.append(participants);
            }
            
            meetingEl.append(info);
            
            // 操作按钮
            var actions = ood('<div class="ood-mobile-meeting-actions"></div>');
            
            // 根据会议状态显示不同按钮
            switch (meeting.status) {
                case 'scheduled':
                    var joinBtn = ood('<button class="ood-mobile-meeting-action ood-mobile-meeting-action-join" data-action="join">加入会议</button>');
                    var cancelBtn = ood('<button class="ood-mobile-meeting-action ood-mobile-meeting-action-cancel" data-action="cancel">取消</button>');
                    actions.append(joinBtn);
                    actions.append(cancelBtn);
                    break;
                case 'in-progress':
                    var joinBtn = ood('<button class="ood-mobile-meeting-action ood-mobile-meeting-action-join" data-action="join">加入会议</button>');
                    actions.append(joinBtn);
                    break;
                case 'completed':
                    var detailBtn = ood('<button class="ood-mobile-meeting-action ood-mobile-meeting-action-detail" data-action="detail">查看详情</button>');
                    actions.append(detailBtn);
                    break;
                case 'cancelled':
                    var detailBtn = ood('<button class="ood-mobile-meeting-action ood-mobile-meeting-action-detail" data-action="detail">查看详情</button>');
                    actions.append(detailBtn);
                    break;
            }
            
            meetingEl.append(actions);
            
            // 禁用状态
            if (meeting.disabled) {
                meetingEl.addClass('ood-mobile-meeting-item-disabled');
            }
            
            return meetingEl;
        },
        
        createFooter: function() {
            var footer = ood('<div class="ood-mobile-meeting-footer"></div>');
            
            var createBtn = ood('<button class="ood-mobile-meeting-create-btn">+ 创建会议</button>');
            footer.append(createBtn);
            
            return footer;
        },
        
        switchView: function(view) {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 更新导航按钮状态
            container.find('.ood-mobile-meeting-nav-btn').removeClass('ood-mobile-meeting-nav-btn-active');
            container.find('.ood-mobile-meeting-nav-btn[data-view="' + view + '"]').addClass('ood-mobile-meeting-nav-btn-active');
            
            // 这里可以添加视图切换逻辑
            if (profile.onViewChange) {
                profile.boxing().onViewChange(profile, view);
            }
        },
        
        onMeetingClick: function(index, meeting) {
            var profile = this.get(0);
            
            if (profile.onMeetingClick) {
                profile.boxing().onMeetingClick(profile, index, meeting);
            }
        },
        
        onActionClick: function(index, meeting, action) {
            var profile = this.get(0);
            
            // 根据操作更新会议状态
            if (action === 'cancel') {
                meeting.status = 'cancelled';
                // 重新渲染该项
                var container = profile.getSubNode('CONTAINER');
                var item = container.find('.ood-mobile-meeting-item[data-index="' + index + '"]');
                var newElement = this.createMeetingElement(meeting, index);
                item.replaceWith(newElement);
            }
            
            if (profile.onActionClick) {
                profile.boxing().onActionClick(profile, index, meeting, action);
            }
        },
        
        addMeeting: function(meeting) {
            this._meetings.push(meeting);
            this.renderMeetings();
        },
        
        removeMeeting: function(index) {
            if (index < 0 || index >= this._meetings.length) return;
            
            this._meetings.splice(index, 1);
            this.renderMeetings();
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentMeetings = self.getMeetings();
                if (typeof index === 'undefined') {
                    currentMeetings = currentMeetings.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentMeetings.splice.apply(currentMeetings, [insertIndex, 0].concat(items));
                }
                
                self.setMeetings(currentMeetings);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentMeetings = self.getMeetings();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentMeetings.length) {
                        currentMeetings.splice(idx, 1);
                    }
                }
                
                self.setMeetings(currentMeetings);
            });
        },
        
        clearItems: function() {
            return this.setMeetings([]);
        },
        
        getItems: function() {
            return this.getMeetings();
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
            className: 'ood-mobile-meeting',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-meeting-container'
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
                display: 'flex',
                'flex-direction': 'column'
            },
            
            '.ood-mobile-meeting-navbar': {
                display: 'flex',
                'justify-content': 'space-around',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-meeting-nav-btn': {
                'border': 'none',
                'background-color': 'transparent',
                color: 'var(--mobile-text-secondary)',
                'font-size': 'var(--mobile-font-md)',
                padding: 'var(--mobile-spacing-sm) var(--mobile-spacing-md)',
                'border-radius': 'var(--mobile-border-radius)'
            },
            
            '.ood-mobile-meeting-nav-btn-active': {
                'background-color': 'var(--mobile-primary-light)',
                color: 'var(--mobile-primary)'
            },
            
            '.ood-mobile-meeting-list': {
                flex: 1,
                'overflow-y': 'auto',
                padding: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-meeting-item': {
                display: 'flex',
                'flex-direction': 'column',
                padding: 'var(--mobile-spacing-md)',
                'border-radius': 'var(--mobile-border-radius)',
                'border': '1px solid var(--mobile-border-color)',
                'margin-bottom': 'var(--mobile-spacing-md)',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            '.ood-mobile-meeting-item:hover': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            '.ood-mobile-meeting-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-meeting-time-container': {
                display: 'flex',
                'justify-content': 'space-between',
                'align-items': 'center',
                'margin-bottom': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-meeting-time': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-meeting-status-indicator': {
                width: '10px',
                height: '10px',
                'border-radius': '50%'
            },
            
            '.ood-mobile-meeting-status-scheduled': {
                'background-color': 'var(--mobile-warning)'
            },
            
            '.ood-mobile-meeting-status-in-progress': {
                'background-color': 'var(--mobile-success)'
            },
            
            '.ood-mobile-meeting-status-completed': {
                'background-color': 'var(--mobile-text-tertiary)'
            },
            
            '.ood-mobile-meeting-status-cancelled': {
                'background-color': 'var(--mobile-danger)'
            },
            
            '.ood-mobile-meeting-info': {
                flex: 1,
                'margin-bottom': 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-meeting-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-meeting-room': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-meeting-participants': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)'
            },
            
            '.ood-mobile-meeting-actions': {
                display: 'flex',
                'justify-content': 'flex-end',
                'gap': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-meeting-action': {
                'border': 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'padding': '6px 12px',
                'font-size': 'var(--mobile-font-sm)',
                cursor: 'pointer'
            },
            
            '.ood-mobile-meeting-action-join': {
                'background-color': 'var(--mobile-success)',
                color: 'white'
            },
            
            '.ood-mobile-meeting-action-cancel': {
                'background-color': 'var(--mobile-danger)',
                color: 'white'
            },
            
            '.ood-mobile-meeting-action-detail': {
                'background-color': 'var(--mobile-primary)',
                color: 'white'
            },
            
            '.ood-mobile-meeting-footer': {
                padding: 'var(--mobile-spacing-md)',
                'border-top': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-meeting-create-btn': {
                width: '100%',
                'border': 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'padding': 'var(--mobile-spacing-md)',
                'font-size': 'var(--mobile-font-md)',
                'background-color': 'var(--mobile-primary)',
                color: 'white',
                cursor: 'pointer'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '会议管理标题',
                ini: '会议管理',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '会议管理');
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
            
            // 会议数据
            meetings: {
                caption: '会议数据',
                ini: [
                    {
                        id: '1',
                        title: '项目周会',
                        startTime: '2025-09-15 09:00',
                        endTime: '2025-09-15 10:00',
                        room: '会议室A',
                        participants: ['张三', '李四', '王五'],
                        status: 'scheduled',
                        disabled: false
                    },
                    {
                        id: '2',
                        title: '产品评审会',
                        startTime: '2025-09-15 14:00',
                        endTime: '2025-09-15 15:30',
                        room: '会议室B',
                        participants: ['赵六', '钱七', '孙八'],
                        status: 'in-progress',
                        disabled: false
                    },
                    {
                        id: '3',
                        title: '季度总结会',
                        startTime: '2025-09-14 10:00',
                        endTime: '2025-09-14 12:00',
                        room: '大会议室',
                        participants: ['全体成员'],
                        status: 'completed',
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setMeetings(value);
                }
            },
            
            // 事件处理器
            onMeetingClick: {
                caption: '会议项点击事件处理器',
                ini: null
            },
            
            onActionClick: {
                caption: '会议操作点击事件处理器',
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
                profile.boxing().setMeetings(profile.properties.meetings);
            });
        }
    }
});