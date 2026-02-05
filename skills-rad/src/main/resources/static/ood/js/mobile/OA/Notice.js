/**
 * 移动端通知公告组件
 * 用于显示企业内部的通知、公告等信息
 */
ood.Class("ood.Mobile.OA.Notice", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initNoticeFeatures();
        },
        
        initNoticeFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-notice ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // Notice组件的尺寸调整逻辑

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
                }
            }

            // 调整内部布局以适应新尺寸
            this.adjustLayout();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 通知项点击事件
            container.on('click', '.ood-mobile-notice-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var notice = self._notices[index];
                
                if (notice && !item.hasClass('ood-mobile-notice-item-disabled')) {
                    self.onNoticeClick(index, notice);
                }
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-notice-item', function(e) {
                ood(this).addClass('ood-mobile-notice-item-active');
            });
            
            container.on('touchend', '.ood-mobile-notice-item', function(e) {
                ood(this).removeClass('ood-mobile-notice-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-notice-item', function(e) {
                ood(this).removeClass('ood-mobile-notice-item-active');
            });
        },
        
        setNotices: function(notices) {
            this._notices = notices || [];
            this.renderNotices();
        },
        
        getNotices: function() {
            return this._notices || [];
        },
        
        renderNotices: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            for (var i = 0; i < this._notices.length; i++) {
                var notice = this._notices[i];
                var noticeElement = this.createNoticeElement(notice, i);
                container.append(noticeElement);
            }
        },
        
        createNoticeElement: function(notice, index) {
            var noticeEl = ood('<div class="ood-mobile-notice-item" data-index="' + index + '"></div>');
            
            // 标题
            if (notice.title) {
                var title = ood('<div class="ood-mobile-notice-title">' + notice.title + '</div>');
                noticeEl.append(title);
            }
            
            // 时间
            if (notice.time) {
                var time = ood('<div class="ood-mobile-notice-time">' + notice.time + '</div>');
                noticeEl.append(time);
            }
            
            // 内容摘要
            if (notice.summary) {
                var summary = ood('<div class="ood-mobile-notice-summary">' + notice.summary + '</div>');
                noticeEl.append(summary);
            }
            
            // 重要性标记
            if (notice.important) {
                noticeEl.addClass('ood-mobile-notice-important');
            }
            
            // 已读状态
            if (notice.read) {
                noticeEl.addClass('ood-mobile-notice-read');
            }
            
            // 禁用状态
            if (notice.disabled) {
                noticeEl.addClass('ood-mobile-notice-item-disabled');
            }
            
            return noticeEl;
        },
        
        markAsRead: function(index) {
            if (index < 0 || index >= this._notices.length) return;
            
            this._notices[index].read = true;
            
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var item = container.find('.ood-mobile-notice-item[data-index="' + index + '"]');
            item.addClass('ood-mobile-notice-read');
        },
        
        markAllAsRead: function() {
            for (var i = 0; i < this._notices.length; i++) {
                this._notices[i].read = true;
            }
            
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            container.find('.ood-mobile-notice-item').addClass('ood-mobile-notice-read');
        },
        
        onNoticeClick: function(index, notice) {
            var profile = this.get(0);
            
            // 标记为已读
            this.markAsRead(index);
            
            // 触发点击事件
            if (profile.onNoticeClick) {
                profile.boxing().onNoticeClick(profile, index, notice);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentNotices = self.getNotices();
                if (typeof index === 'undefined') {
                    currentNotices = currentNotices.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentNotices.splice.apply(currentNotices, [insertIndex, 0].concat(items));
                }
                
                self.setNotices(currentNotices);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentNotices = self.getNotices();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentNotices.length) {
                        currentNotices.splice(idx, 1);
                    }
                }
                
                self.setNotices(currentNotices);
            });
        },
        
        clearItems: function() {
            return this.setNotices([]);
        },
        
        getItems: function() {
            return this.getNotices();
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
            className: 'ood-mobile-notice',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-notice-container'
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
            
            '.ood-mobile-notice-item': {
                'border-bottom': '1px solid var(--mobile-border-color)',
                padding: 'var(--mobile-spacing-md) 0',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-notice-item:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-notice-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-notice-item-active': {
                'background-color': 'var(--mobile-bg-secondary)',
                'transform': 'scale(0.98)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-notice-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-notice-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-notice-important .ood-mobile-notice-title': {
                color: 'var(--mobile-danger)'
            },
            
            '.ood-mobile-notice-time': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-notice-summary': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'line-height': '1.4'
            },
            
            '.ood-mobile-notice-read .ood-mobile-notice-title': {
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-notice-read .ood-mobile-notice-summary': {
                color: 'var(--mobile-text-tertiary)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '通知公告标题',
                ini: '通知公告',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '通知公告');
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
            
            // 通知数据
            notices: {
                caption: '通知数据',
                ini: [
                    {
                        id: '1',
                        title: '关于五一假期安排的通知',
                        time: '2025-04-25',
                        summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                        important: true,
                        read: false,
                        disabled: false
                    },
                    {
                        id: '2',
                        title: '新员工入职培训安排',
                        time: '2025-04-20',
                        summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                        important: false,
                        read: true,
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setNotices(value);
                }
            },
            
            // 事件处理器
            onNoticeClick: {
                caption: '通知点击事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setNotices(profile.properties.notices);
            });
        }
    }
});