/**
 * 通知公告组件使用示例
 * 展示如何在实际项目中使用 NoticeDesigner 组件
 */
ood.Class("ood.Mobile.OA.NoticeUsageExample", 'ood.Module',{
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
            caption: '通知公告使用示例'
        },

        events:{
        },
        functions:{
            // 初始化组件
            initComponent: function() {
                // 创建标准通知公告组件实例
                this._noticeDesigner = ood.create("ood.Mobile.OA.NoticeDesignerStandard");
                this._noticeDesigner.setHost(this, "noticeDesigner");
                
                // 配置数据源
                this._noticeDesigner.setDataSource({
                    url: "/api/notices",
                    method: "GET",
                    params: {
                        pageSize: 10,
                        pageIndex: 1
                    }
                });
                
                // 配置动作
                this._noticeDesigner.properties.actions = {
                    onClick: {
                        "actions": [
                            {
                                "args": ["通知 {args[0].title} 被点击了"],
                                "conditions": [],
                                "desc": "显示点击消息",
                                "method": "showMessage",
                                "target": "this",
                                "type": "module"
                            }
                        ]
                    },
                    onLoad: {
                        "actions": [
                            {
                                "args": ["通知数据加载完成，共 {args[0].data.length} 条记录"],
                                "conditions": [],
                                "desc": "显示加载完成消息",
                                "method": "showMessage",
                                "target": "this",
                                "type": "module"
                            }
                        ]
                    }
                };
                
                // 添加到容器中
                this.container.append(this._noticeDesigner);
            },
            
            // 显示消息
            showMessage: function(message) {
                console.log("消息:", message);
                // 这里可以调用 Toast 组件显示消息
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

            // 初始化组件
            this.initComponent();

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-notice-usage-example',
            style: '{_style}'
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