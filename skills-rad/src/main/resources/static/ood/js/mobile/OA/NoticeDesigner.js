/**
 * 移动端通知公告组件（设计器版本）
 * 用于在可视化设计器中使用
 */
ood.Class("ood.Mobile.OA.NoticeDesigner", 'ood.Module',{
    Instance:{
        initialize : function(){
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
            this._dataSource = null; // 数据源关联
            // 初始化 APICall 组件用于数据交互
            this._apiCaller = ood.create("ood.net.Ajax");
        },
        Dependencies:[],
        Required:[],
        properties : {
            // 组件基础属性
            width: '100%',
            height: 'auto',
            caption: '通知公告',
            // 数据关联属性
            dataSource: null, // 数据源配置
            dataField: 'notices', // 数据字段映射
            // 动作关联属性
            actions: {
                onClick: null,
                onStatusChange: null
            }
        },

        events:{
            // 通知点击事件
            onNoticeClick: function(profile, index, notice) {
                // 默认事件处理
            },
            // 通知状态改变事件
            onNoticeStatusChange: function(profile, index, notice, status) {
                // 默认事件处理
            },
            // 数据源改变事件
            onDataSourceChange: function(profile, dataSource) {
                // 数据源改变处理
            }
        },
        functions:{
            // 设置通知数据
            setNotices: function(notices) {
                this.properties.notices = notices || [];
                // 触发数据更新
                if (this._onNoticesChange) {
                    this._onNoticesChange(notices);
                }
                // 更新UI显示
                this.refreshNotices();
            },
            // 获取通知数据
            getNotices: function() {
                return this.properties.notices || [];
            },
            // 标记通知为已读
            markAsRead: function(index) {
                var notices = this.getNotices();
                if (index >= 0 && index < notices.length) {
                    notices[index].read = true;
                    // 触发状态改变事件
                    if (this.events.onNoticeStatusChange) {
                        this.events.onNoticeStatusChange(this.get(0), index, notices[index], 'read');
                    }
                    // 执行关联动作
                    if (this.properties.actions && this.properties.actions.onStatusChange) {
                        this.executeAction(this.properties.actions.onStatusChange, {
                            index: index,
                            notice: notices[index],
                            status: 'read'
                        });
                    }
                }
            },
            // 添加通知
            addNotice: function(notice) {
                var notices = this.getNotices();
                notices.push(notice);
                this.setNotices(notices);
            },
            // 删除通知
            removeNotice: function(index) {
                var notices = this.getNotices();
                if (index >= 0 && index < notices.length) {
                    notices.splice(index, 1);
                    this.setNotices(notices);
                }
            },
            // 刷新通知显示
            refreshNotices: function() {
                // 通知数据刷新逻辑
                // 这里可以重新渲染通知列表
            },
            // 设置数据源
            setDataSource: function(dataSource) {
                this.properties.dataSource = dataSource;
                this._dataSource = dataSource;
                // 触发数据源改变事件
                if (this.events.onDataSourceChange) {
                    this.events.onDataSourceChange(this.get(0), dataSource);
                }
                // 如果数据源有效，从数据源获取数据
                if (dataSource && typeof dataSource === 'object') {
                    this.bindDataSource(dataSource);
                }
            },
            // 绑定数据源
            bindDataSource: function(dataSource) {
                // 使用 APICall 组件处理数据源交互
                if (dataSource && dataSource.url) {
                    // 如果是 API 数据源配置
                    this._apiCaller.abort(); // 取消之前的请求
                    this._apiCaller.setUrl(dataSource.url);
                    this._apiCaller.setType(dataSource.method || "GET");
                    
                    if (dataSource.params) {
                        this._apiCaller.setPara(dataSource.params);
                    }
                    
                    // 设置成功回调
                    this._apiCaller.onSuccess(function(response) {
                        if (response && response.data) {
                            this.setNotices(response.data);
                        }
                    }.bind(this));
                    
                    // 设置错误回调
                    this._apiCaller.onError(function(error) {
                        console.error("数据获取失败:", error);
                    });
                    
                    // 发起请求
                    this._apiCaller.start();
                } else if (dataSource && Array.isArray(dataSource)) {
                    // 如果是直接的数据数组
                    this.setNotices(dataSource);
                }
            },
            // 执行关联动作
            executeAction: function(action, params) {
                if (action && typeof action === 'function') {
                    action(params);
                } else if (action && typeof action === 'string') {
                    // 如果是字符串，可能是动作名称，需要查找并执行
                    // 这里可以根据具体实现来处理
                    console.log('执行动作:', action, params);
                }
            },
            // 获取选中的通知
            getSelectedNotice: function() {
                return this._selectedNotice;
            },
            // 设置选中的通知
            setSelectedNotice: function(notice) {
                this._selectedNotice = notice;
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 使用命令方式组合视图并设置组件属性
            var noticeContainer = ood.create("ood.UI.Div");
            noticeContainer.setHost(host,"notice_container");
            noticeContainer.setLeft(0);
            noticeContainer.setTop(0);
            noticeContainer.setWidth("100%");
            noticeContainer.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeContainer.setStyle({
                "padding": "var(--mobile-spacing-md)"
            });
            append(noticeContainer);

            // 创建示例通知项
            var noticeItem1 = ood.create("ood.UI.Div");
            noticeItem1.setHost(host,"notice_item1");
            noticeItem1.setLeft("0em");
            noticeItem1.setTop("0em");
            noticeItem1.setWidth("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeItem1.setStyle({
                "border-radius": "var(--mobile-border-radius)",
                "border": "1px solid var(--mobile-border-color)",
                "padding": "var(--mobile-spacing-md)",
                "margin-bottom": "var(--mobile-spacing-md)",
                "cursor": "pointer",
                "transition": "all 0.2s ease",
                "background-color": "var(--mobile-bg-primary)",
                "-webkit-tap-highlight-color": "rgba(0, 0, 0, 0)",
                "-webkit-touch-callout": "none",
                "-webkit-user-select": "none"
            });
            // 添加点击事件到具体的组件中
            noticeItem1.onClick(function() {
                var notice = {
                    id: '1',
                    title: '关于五一假期安排的通知',
                    time: '2025-04-25',
                    summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                    important: true,
                    read: false,
                    disabled: false
                };
                
                // 设置为选中通知
                host.boxing().setSelectedNotice(notice);
                
                // 触发点击事件
                host.events.onNoticeClick(host, 0, notice);
                
                // 执行关联动作
                if (host.properties.actions && host.properties.actions.onClick) {
                    host.boxing().executeAction(host.properties.actions.onClick, {
                        index: 0,
                        notice: notice
                    });
                }
            });
            host.notice_container.append(noticeItem1);

            // 通知标题
            var noticeTitle1 = ood.create("ood.UI.Label");
            noticeTitle1.setHost(host,"notice_title1");
            noticeTitle1.setLeft("0em");
            noticeTitle1.setTop("0em");
            noticeTitle1.setWidth("100%");
            noticeTitle1.setCaption("关于五一假期安排的通知");
            // 使用 KEY:VALUE 方式组合样式
            noticeTitle1.setStyle({
                "font-size": "var(--mobile-font-md)",
                "font-weight": "600",
                "color": "var(--mobile-text-primary)",
                "margin-bottom": "var(--mobile-spacing-xs)"
            });
            host.notice_item1.append(noticeTitle1);

            // 通知时间
            var noticeTime1 = ood.create("ood.UI.Label");
            noticeTime1.setHost(host,"notice_time1");
            noticeTime1.setLeft("0em");
            noticeTime1.setTop("2em");
            noticeTime1.setWidth("100%");
            noticeTime1.setCaption("2025-04-25");
            // 使用 KEY:VALUE 方式组合样式
            noticeTime1.setStyle({
                "font-size": "var(--mobile-font-xs)",
                "color": "var(--mobile-text-secondary)",
                "margin-bottom": "var(--mobile-spacing-xs)"
            });
            host.notice_item1.append(noticeTime1);

            // 通知摘要
            var noticeSummary1 = ood.create("ood.UI.Label");
            noticeSummary1.setHost(host,"notice_summary1");
            noticeSummary1.setLeft("0em");
            noticeSummary1.setTop("3.5em");
            noticeSummary1.setWidth("100%");
            noticeSummary1.setHeight("3em");
            noticeSummary1.setCaption("根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...");
            // 使用 KEY:VALUE 方式组合样式
            noticeSummary1.setStyle({
                "font-size": "var(--mobile-font-sm)",
                "color": "var(--mobile-text-secondary)",
                "line-height": "1.4"
            });
            host.notice_item1.append(noticeSummary1);

            // 第二个示例通知项
            var noticeItem2 = ood.create("ood.UI.Div");
            noticeItem2.setHost(host,"notice_item2");
            noticeItem2.setLeft("0em");
            noticeItem2.setTop("7em");
            noticeItem2.setWidth("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeItem2.setStyle({
                "border-radius": "var(--mobile-border-radius)",
                "border": "1px solid var(--mobile-border-color)",
                "padding": "var(--mobile-spacing-md)",
                "margin-bottom": "var(--mobile-spacing-md)",
                "cursor": "pointer",
                "transition": "all 0.2s ease",
                "background-color": "var(--mobile-bg-primary)",
                "-webkit-tap-highlight-color": "rgba(0, 0, 0, 0)",
                "-webkit-touch-callout": "none",
                "-webkit-user-select": "none"
            });
            // 添加已读状态样式
            noticeItem2.setCustomClass({"KEY": "ood-mobile-notice-read"});
            // 添加点击事件到具体的组件中
            noticeItem2.onClick(function() {
                var notice = {
                    id: '2',
                    title: '新员工入职培训安排',
                    time: '2025-04-20',
                    summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                    important: false,
                    read: true,
                    disabled: false
                };
                
                // 设置为选中通知
                host.boxing().setSelectedNotice(notice);
                
                // 触发点击事件
                host.events.onNoticeClick(host, 1, notice);
                
                // 执行关联动作
                if (host.properties.actions && host.properties.actions.onClick) {
                    host.boxing().executeAction(host.properties.actions.onClick, {
                        index: 1,
                        notice: notice
                    });
                }
            });
            host.notice_container.append(noticeItem2);

            // 通知标题
            var noticeTitle2 = ood.create("ood.UI.Label");
            noticeTitle2.setHost(host,"notice_title2");
            noticeTitle2.setLeft("0em");
            noticeTitle2.setTop("0em");
            noticeTitle2.setWidth("100%");
            noticeTitle2.setCaption("新员工入职培训安排");
            // 使用 KEY:VALUE 方式组合样式
            noticeTitle2.setStyle({
                "font-size": "var(--mobile-font-md)",
                "font-weight": "600",
                "color": "var(--mobile-text-primary)",
                "margin-bottom": "var(--mobile-spacing-xs)"
            });
            host.notice_item2.append(noticeTitle2);

            // 通知时间
            var noticeTime2 = ood.create("ood.UI.Label");
            noticeTime2.setHost(host,"notice_time2");
            noticeTime2.setLeft("0em");
            noticeTime2.setTop("2em");
            noticeTime2.setWidth("100%");
            noticeTime2.setCaption("2025-04-20");
            // 使用 KEY:VALUE 方式组合样式
            noticeTime2.setStyle({
                "font-size": "var(--mobile-font-xs)",
                "color": "var(--mobile-text-secondary)",
                "margin-bottom": "var(--mobile-spacing-xs)"
            });
            host.notice_item2.append(noticeTime2);

            // 通知摘要
            var noticeSummary2 = ood.create("ood.UI.Label");
            noticeSummary2.setHost(host,"notice_summary2");
            noticeSummary2.setLeft("0em");
            noticeSummary2.setTop("3.5em");
            noticeSummary2.setWidth("100%");
            noticeSummary2.setHeight("3em");
            noticeSummary2.setCaption("欢迎新员工加入公司，现将入职培训相关安排通知如下...");
            // 使用 KEY:VALUE 方式组合样式
            noticeSummary2.setStyle({
                "font-size": "var(--mobile-font-sm)",
                "color": "var(--mobile-text-secondary)",
                "line-height": "1.4"
            });
            host.notice_item2.append(noticeSummary2);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
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
                'border-radius': 'var(--mobile-border-radius)',
                'border': '1px solid var(--mobile-border-color)',
                padding: 'var(--mobile-spacing-md)',
                'margin-bottom': 'var(--mobile-spacing-md)',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                'background-color': 'var(--mobile-bg-primary)',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-notice-item:hover': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-notice-item-active': {
                'box-shadow': 'var(--mobile-shadow-light)',
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
        }
    }
});