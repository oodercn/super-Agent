/**
 * 移动端通知公告组件（设计器兼容版本-重构版）
 * 用于在可视化设计器中使用
 * 完全遵循OOD开发规范
 */
ood.Class("ood.Mobile.OA.refactored.NoticeDesigner", 'ood.Module', {
    Instance: {
        initialize: function() {
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
        },
        
        Dependencies: [],
        Required: [],

        events: {},

        functions: {},

        iniComponents: function() {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], properties = {}, append = function(child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);

            // 创建APICaller实例 - 在iniComponents中静态初始化
            var noticeApiCaller = ood.create("ood.APICaller");
            noticeApiCaller.setHost(host, "noticeApiCaller");
            noticeApiCaller.setName("noticeApiCaller");
            // 设置默认配置
            noticeApiCaller.setQueryURL("/api/notices");
            noticeApiCaller.setQueryMethod("GET");
            noticeApiCaller.setProxyType("AJAX");
            noticeApiCaller.setRequestType("JSON");
            
            // 设置成功回调 - 使用标准的 OOD 框架动作定义方式
            noticeApiCaller.onData([
                {
                    "desc": "数据加载成功",
                    "type": "function",
                    "target": "setNotices",
                    "args": ["{temp$.okData}"]
                },
                {
                    "desc": "触发数据加载完成事件",
                    "type": "event",
                    "target": "onDataLoad",
                    "args": ["{temp$.okData}"]
                }
            ]);
            
            // 设置错误回调 - 使用标准的 OOD 框架动作定义方式
            noticeApiCaller.onError([
                {
                    "desc": "数据加载失败",
                    "type": "event",
                    "target": "onDataError",
                    "args": ["{temp$.koData}"]
                }
            ]);
            
            // 将APICaller添加到组件中
            append(noticeApiCaller);

            // 使用命令方式组合视图并设置组件属性
            var noticeContainer = ood.create("ood.UI.Div");
            noticeContainer.setHost(host, "notice_container");
            noticeContainer.setLeft(0);
            noticeContainer.setTop(0);
            noticeContainer.setWidth("100%");
            noticeContainer.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeContainer.setCustomStyle({
                "KEY": {
                    "padding": "var(--mobile-spacing-md)"
                }
            });
            append(noticeContainer);

            // 创建示例通知项
            var noticeItem1 = ood.create("ood.UI.Div");
            noticeItem1.setHost(host, "notice_item1");
            noticeItem1.setLeft("0em");
            noticeItem1.setTop("0em");
            noticeItem1.setWidth("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeItem1.setCustomStyle({
                "KEY": {
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
                }
            });
            // 添加点击事件到具体的组件中
            noticeItem1.onClick([
                {
                    "desc": "设置选中通知",
                    "type": "function",
                    "target": "setSelectedNotice",
                    "args": [
                        {
                            id: '1',
                            title: '关于五一假期安排的通知',
                            time: '2025-04-25',
                            summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                            important: true,
                            read: false,
                            disabled: false
                        }
                    ]
                },
                {
                    "desc": "触发点击事件",
                    "type": "event",
                    "target": "onNoticeClick",
                    "args": [0, {
                        id: '1',
                        title: '关于五一假期安排的通知',
                        time: '2025-04-25',
                        summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                        important: true,
                        read: false,
                        disabled: false
                    }]
                }
            ]);
            host.notice_container.append(noticeItem1);

            // 通知标题
            var noticeTitle1 = ood.create("ood.UI.Label");
            noticeTitle1.setHost(host, "notice_title1");
            noticeTitle1.setLeft("0em");
            noticeTitle1.setTop("0em");
            noticeTitle1.setWidth("100%");
            noticeTitle1.setCaption("关于五一假期安排的通知");
            // 使用 KEY:VALUE 方式组合样式
            noticeTitle1.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-md)",
                    "font-weight": "600",
                    "color": "var(--mobile-text-primary)",
                    "margin-bottom": "var(--mobile-spacing-xs)"
                }
            });
            host.notice_item1.append(noticeTitle1);

            // 通知时间
            var noticeTime1 = ood.create("ood.UI.Label");
            noticeTime1.setHost(host, "notice_time1");
            noticeTime1.setLeft("0em");
            noticeTime1.setTop("2em");
            noticeTime1.setWidth("100%");
            noticeTime1.setCaption("2025-04-25");
            // 使用 KEY:VALUE 方式组合样式
            noticeTime1.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-xs)",
                    "color": "var(--mobile-text-secondary)",
                    "margin-bottom": "var(--mobile-spacing-xs)"
                }
            });
            host.notice_item1.append(noticeTime1);

            // 通知摘要
            var noticeSummary1 = ood.create("ood.UI.Label");
            noticeSummary1.setHost(host, "notice_summary1");
            noticeSummary1.setLeft("0em");
            noticeSummary1.setTop("3.5em");
            noticeSummary1.setWidth("100%");
            noticeSummary1.setHeight("3em");
            noticeSummary1.setCaption("根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...");
            // 使用 KEY:VALUE 方式组合样式
            noticeSummary1.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-sm)",
                    "color": "var(--mobile-text-secondary)",
                    "line-height": "1.4"
                }
            });
            host.notice_item1.append(noticeSummary1);

            // 第二个示例通知项
            var noticeItem2 = ood.create("ood.UI.Div");
            noticeItem2.setHost(host, "notice_item2");
            noticeItem2.setLeft("0em");
            noticeItem2.setTop("7em");
            noticeItem2.setWidth("100%");
            // 使用 KEY:VALUE 方式组合样式
            noticeItem2.setCustomStyle({
                "KEY": {
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
                }
            });
            // 添加已读状态样式
            noticeItem2.setCustomClass({"KEY": "ood-mobile-notice-read"});
            // 添加点击事件到具体的组件中
            noticeItem2.onClick([
                {
                    "desc": "设置选中通知",
                    "type": "function",
                    "target": "setSelectedNotice",
                    "args": [
                        {
                            id: '2',
                            title: '新员工入职培训安排',
                            time: '2025-04-20',
                            summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                            important: false,
                            read: true,
                            disabled: false
                        }
                    ]
                },
                {
                    "desc": "触发点击事件",
                    "type": "event",
                    "target": "onNoticeClick",
                    "args": [1, {
                        id: '2',
                        title: '新员工入职培训安排',
                        time: '2025-04-20',
                        summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                        important: false,
                        read: true,
                        disabled: false
                    }]
                }
            ]);
            host.notice_container.append(noticeItem2);

            // 通知标题
            var noticeTitle2 = ood.create("ood.UI.Label");
            noticeTitle2.setHost(host, "notice_title2");
            noticeTitle2.setLeft("0em");
            noticeTitle2.setTop("0em");
            noticeTitle2.setWidth("100%");
            noticeTitle2.setCaption("新员工入职培训安排");
            // 使用 KEY:VALUE 方式组合样式
            noticeTitle2.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-md)",
                    "font-weight": "600",
                    "color": "var(--mobile-text-primary)",
                    "margin-bottom": "var(--mobile-spacing-xs)"
                }
            });
            host.notice_item2.append(noticeTitle2);

            // 通知时间
            var noticeTime2 = ood.create("ood.UI.Label");
            noticeTime2.setHost(host, "notice_time2");
            noticeTime2.setLeft("0em");
            noticeTime2.setTop("2em");
            noticeTime2.setWidth("100%");
            noticeTime2.setCaption("2025-04-20");
            // 使用 KEY:VALUE 方式组合样式
            noticeTime2.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-xs)",
                    "color": "var(--mobile-text-secondary)",
                    "margin-bottom": "var(--mobile-spacing-xs)"
                }
            });
            host.notice_item2.append(noticeTime2);

            // 通知摘要
            var noticeSummary2 = ood.create("ood.UI.Label");
            noticeSummary2.setHost(host, "notice_summary2");
            noticeSummary2.setLeft("0em");
            noticeSummary2.setTop("3.5em");
            noticeSummary2.setWidth("100%");
            noticeSummary2.setHeight("3em");
            noticeSummary2.setCaption("欢迎新员工加入公司，现将入职培训相关安排通知如下...");
            // 使用 KEY:VALUE 方式组合样式
            noticeSummary2.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-sm)",
                    "color": "var(--mobile-text-secondary)",
                    "line-height": "1.4"
                }
            });
            host.notice_item2.append(noticeSummary2);

            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    Static: {}
});