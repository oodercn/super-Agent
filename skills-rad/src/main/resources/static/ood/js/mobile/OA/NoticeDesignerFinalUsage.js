/**
 * 最终版通知公告组件使用示例
 * 演示如何使用 NoticeDesignerFinal 组件并配置标准动作
 */
ood.Class("ood.Mobile.OA.NoticeDesignerFinalUsage", 'ood.Module',{
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
            caption: '通知公告组件使用示例'
        },

        events:{
            // 自定义事件处理
            onNoticeSelected: function(profile, notice) {
                console.log("选中的通知:", notice);
            }
        },
        functions:{
            // 初始化通知组件
            initNoticeComponent: function() {
                // 创建通知组件实例
                this._noticeComponent = ood.create("ood.Mobile.OA.NoticeDesignerFinal");
                this._noticeComponent.setHost(this, "noticeList");
                
                // 配置数据源
                this._noticeComponent.setDataSource({
                    url: "/api/notices",
                    method: "GET",
                    params: {
                        pageSize: 10,
                        pageIndex: 1
                    }
                });
                
                // 配置动作 - 使用标准的 OOD 框架动作定义方式
                this._noticeComponent.properties.actions = {
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
                            },
                            {
                                "args": [
                                    "{args[0]}"
                                ],
                                "conditions": [],
                                "desc": "触发自定义事件",
                                "method": "onNoticeSelected",
                                "target": "host",
                                "type": "module"
                            }
                        ]
                    },
                    
                    // 通知状态改变时的动作
                    onStatusChange: {
                        "actions": [
                            {
                                "args": [],
                                "conditions": [],
                                "desc": "刷新通知列表",
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
                                "args": [],
                                "conditions": [],
                                "desc": "隐藏加载指示器",
                                "method": "hideLoading",
                                "target": "host",
                                "type": "module"
                            }
                        ]
                    },
                    
                    // 数据加载错误时的动作
                    onError: {
                        "actions": [
                            {
                                "args": [
                                    "{args[0].message || '数据加载失败'}"
                                ],
                                "conditions": [],
                                "desc": "显示错误消息",
                                "method": "showErrorMessage",
                                "target": "host",
                                "type": "module"
                            }
                        ]
                    }
                };
                
                // 将组件添加到容器中
                this.noticeContainer.append(this._noticeComponent);
            },
            
            // 隐藏加载指示器
            hideLoading: function() {
                console.log("隐藏加载指示器");
                // 实际实现中会隐藏加载动画
            },
            
            // 显示错误消息
            showErrorMessage: function(message) {
                console.log("显示错误消息:", message);
                // 实际实现中会显示错误提示
            },
            
            // 添加新通知
            addNewNotice: function() {
                var newNotice = {
                    id: Date.now(),
                    title: '新通知标题',
                    time: new Date().toISOString().split('T')[0],
                    summary: '这是新添加的通知内容摘要...',
                    important: false,
                    read: false,
                    disabled: false
                };
                
                // 使用组件的添加方法
                this._noticeComponent.addNotice(newNotice);
            },
            
            // 标记所有通知为已读
            markAllAsRead: function() {
                // 使用组件的批量标记方法
                this._noticeComponent.markAllAsRead();
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 主容器
            var mainContainer = ood.create("ood.UI.Div");
            mainContainer.setHost(host,"mainContainer");
            mainContainer.setLeft(0);
            mainContainer.setTop(0);
            mainContainer.setWidth("100%");
            mainContainer.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            mainContainer.setStyle({
                "padding": "var(--mobile-spacing-md)"
            });
            append(mainContainer);

            // 通知容器
            var noticeContainer = ood.create("ood.UI.Div");
            noticeContainer.setHost(host,"noticeContainer");
            noticeContainer.setLeft(0);
            noticeContainer.setTop(0);
            noticeContainer.setWidth("100%");
            noticeContainer.setHeight("calc(100% - 60px)");
            // 使用 KEY:VALUE 方式组合样式
            noticeContainer.setStyle({
                "margin-bottom": "var(--mobile-spacing-md)"
            });
            host.mainContainer.append(noticeContainer);

            // 操作按钮容器
            var buttonContainer = ood.create("ood.UI.Div");
            buttonContainer.setHost(host,"buttonContainer");
            buttonContainer.setLeft(0);
            buttonContainer.setTop("calc(100% - 60px)");
            buttonContainer.setWidth("100%");
            buttonContainer.setHeight("60px");
            host.mainContainer.append(buttonContainer);

            // 添加通知按钮
            var addButton = ood.create("ood.UI.Button");
            addButton.setHost(host,"addButton");
            addButton.setLeft(0);
            addButton.setTop(0);
            addButton.setWidth("45%");
            addButton.setHeight("100%");
            addButton.setCaption("添加通知");
            // 使用 KEY:VALUE 方式组合样式
            addButton.setStyle({
                "margin-right": "var(--mobile-spacing-sm)"
            });
            // 添加点击事件
            addButton.onClick([
                {
                    "desc": "添加新通知",
                    "type": "function",
                    "target": "addNewNotice",
                    "args": []
                }
            ]);
            host.buttonContainer.append(addButton);

            // 标记已读按钮
            var readButton = ood.create("ood.UI.Button");
            readButton.setHost(host,"readButton");
            readButton.setLeft("50%");
            readButton.setTop(0);
            readButton.setWidth("45%");
            readButton.setHeight("100%");
            readButton.setCaption("全部已读");
            // 添加点击事件
            readButton.onClick([
                {
                    "desc": "标记所有通知为已读",
                    "type": "function",
                    "target": "markAllAsRead",
                    "args": []
                }
            ]);
            host.buttonContainer.append(readButton);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-notice-final-usage',
            style: '{_style}'
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            }
        }
    }
});