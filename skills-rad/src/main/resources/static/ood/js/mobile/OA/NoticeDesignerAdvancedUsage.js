/**
 * 通知公告组件（高级版）使用示例
 * 展示如何在实际项目中使用 NoticeDesignerAdvanced 组件
 */
ood.Class("ood.Mobile.OA.NoticeDesignerAdvancedUsage", 'ood.Module',{
    Instance:{
        initialize : function(){
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
        },
        Dependencies:[],
        Required:[],
        properties : {
            width: '100%',
            height: 'auto'
        },

        events:{
        },
        functions:{
            // 初始化通知数据
            initNotices: function() {
                // 模拟从服务器获取数据
                var sampleNotices = [
                    {
                        id: '1',
                        title: '关于五一假期安排的通知',
                        time: '2025-04-25',
                        summary: '根据国家规定，结合公司实际情况，现将2025年五一劳动节放假安排通知如下...',
                        important: true,
                        read: false
                    },
                    {
                        id: '2',
                        title: '新员工入职培训安排',
                        time: '2025-04-20',
                        summary: '欢迎新员工加入公司，现将入职培训相关安排通知如下...',
                        important: false,
                        read: true
                    },
                    {
                        id: '3',
                        title: '系统维护通知',
                        time: '2025-04-15',
                        summary: '为了提供更好的服务，系统将于本周六晚进行维护...',
                        important: true,
                        read: false
                    }
                ];
                
                // 直接设置数据
                this.noticeDesignerAdvanced.setNotices(sampleNotices);
            },
            
            // 从API加载通知数据
            loadNoticesFromAPI: function() {
                // 模拟API调用
                if (this.noticeDesignerAdvanced && this.noticeDesignerAdvanced.noticeApiCaller) {
                    this.noticeDesignerAdvanced.noticeApiCaller.invoke();
                }
            },
            
            // 添加新通知
            addNewNotice: function() {
                if (this.noticeDesignerAdvanced) {
                    var newNotice = {
                        id: Date.now().toString(),
                        title: '新通知标题',
                        time: new Date().toISOString().split('T')[0],
                        summary: '这是新添加的通知内容摘要...',
                        important: false,
                        read: false
                    };
                    
                    // 获取现有通知
                    var notices = this.noticeDesignerAdvanced.getNotices();
                    notices.push(newNotice);
                    this.noticeDesignerAdvanced.setNotices(notices);
                }
            },
            
            // 全部标记为已读
            markAllAsRead: function() {
                if (this.noticeDesignerAdvanced) {
                    var notices = this.noticeDesignerAdvanced.getNotices();
                    for (var i = 0; i < notices.length; i++) {
                        notices[i].read = true;
                    }
                    this.noticeDesignerAdvanced.setNotices(notices);
                }
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 创建通知公告组件（高级版）
            var noticeDesignerAdvanced = ood.create("ood.Mobile.OA.NoticeDesignerAdvanced");
            noticeDesignerAdvanced.setHost(host, "noticeDesignerAdvanced");
            noticeDesignerAdvanced.setName("noticeDesignerAdvanced");
            noticeDesignerAdvanced.setLeft(0);
            noticeDesignerAdvanced.setTop(0);
            noticeDesignerAdvanced.setWidth("100%");
            noticeDesignerAdvanced.setHeight("100%");
            
            // 配置数据源
            noticeDesignerAdvanced.setDataSource({
                url: "/api/notices",
                method: "GET"
            });
            
            // 配置点击事件
            noticeDesignerAdvanced.events.onNoticeClick = function(profile, index, notice) {
                console.log("通知点击:", index, notice.title);
                alert("点击了通知: " + notice.title);
            };
            
            // 配置状态改变事件
            noticeDesignerAdvanced.events.onNoticeStatusChange = function(profile, index, notice, status) {
                console.log("通知状态改变:", index, notice.title, status);
            };
            
            // 将通知公告组件添加到组件中
            append(noticeDesignerAdvanced);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-notice-usage',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-notice-usage-container'
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
            }
        }
    }
});