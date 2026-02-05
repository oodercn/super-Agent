/**
 * 通知公告组件动作配置示例
 * 展示如何使用标准的 OOD 框架动作定义方式
 */
ood.Class("ood.Mobile.OA.NoticeActionConfig", 'ood.Module',{
    Instance:{
        initialize : function(){
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
        },
        Dependencies:[],
        Required:[],
        properties : {
            // 组件基础属性
            width: '100%',
            height: 'auto',
            caption: '通知公告动作配置示例'
        },

        events:{
        },
        functions:{
            // 初始化数据源
            initDataSource: function() {
                // 创建 APICaller 实例用于获取通知数据
                var noticeApiCaller = ood.create("ood.APICaller")
                    .setName("getNotices")
                    .setQueryURL("/api/notices")
                    .setQueryMethod("GET");
                
                // 设置成功回调 - 使用标准的 OOD 框架动作定义方式
                noticeApiCaller.onData([
                    {
                        "desc": "设置通知数据",
                        "type": "function",
                        "target": "setNotices",
                        "args": ["{temp$.okData}"]
                    }
                ]);
                
                // 设置错误回调 - 使用标准的 OOD 框架动作定义方式
                noticeApiCaller.onError([
                    {
                        "desc": "处理错误",
                        "type": "function",
                        "target": "handleError",
                        "args": ["{temp$.koData}"]
                    }
                ]);
                
                // 发起请求
                noticeApiCaller.invoke();
            },
            
            // 处理错误
            handleError: function(error) {
                console.error("数据加载失败:", error);
            },
            
            // 设置通知数据
            setNotices: function(notices) {
                this._notices = notices || [];
                console.log("通知数据已设置:", this._notices);
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 使用命令方式组合视图并设置组件属性
            var container = ood.create("ood.UI.Div");
            container.setHost(host,"container");
            container.setLeft(0);
            container.setTop(0);
            container.setWidth("100%");
            container.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            container.setStyle({
                "padding": "var(--mobile-spacing-md)"
            });
            append(container);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-notice-action-config',
            style: '{_style}'
        },
        
        // 动作关联属性 - 使用标准的 OOD 框架动作定义方式
        actions: {
            // 点击通知时的动作
            onClick: {
                "actions": [
                    {
                        "args": [
                            "{args[0]}"
                        ],
                        "conditions": [
                            {
                                "symbol": "defined",
                                "right": "",
                                "left": "{args[0]}"
                            }
                        ],
                        "desc": "设置选中通知",
                        "method": "setSelectedNotice",
                        "target": "this",
                        "type": "module"
                    },
                    {
                        "args": [
                            "{args[0]}",
                            "{args[1]}"
                        ],
                        "conditions": [],
                        "desc": "触发点击事件",
                        "method": "onNoticeClick",
                        "target": "this",
                        "type": "module"
                    }
                ]
            },
            
            // 通知状态改变时的动作
            onStatusChange: {
                "actions": [
                    {
                        "args": [
                            "{args[0]}"
                        ],
                        "conditions": [],
                        "desc": "更新UI显示",
                        "method": "refreshNotices",
                        "target": "this",
                        "type": "module"
                    }
                ]
            },
            
            // 数据加载完成时的动作
            onLoad: {
                "actions": [
                    {
                        "args": [
                            "{args[0]}"
                        ],
                        "conditions": [],
                        "desc": "显示加载完成消息",
                        "method": "showLoadSuccessMessage",
                        "target": "this",
                        "type": "module"
                    }
                ]
            },
            
            // 数据加载错误时的动作
            onError: {
                "actions": [
                    {
                        "args": [
                            "{args[0]}"
                        ],
                        "conditions": [],
                        "desc": "显示错误消息",
                        "method": "showErrorMessage",
                        "target": "this",
                        "type": "module"
                    }
                ]
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            }
        }
    }
});