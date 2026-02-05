/**
 * APICaller 使用示例（规范版本）
 * 演示如何在业务组件中使用 APICaller 进行数据交互
 * 遵循OOD开发规范重构
 */
ood.Class("ood.Mobile.OA.new.APICallerExample", 'ood.Module', {
    Instance: {
        initialize: function() {
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
        },
        
        Dependencies: [],
        Required: [],
        
        // 移除properties定义，交由OOD底层实现
        // properties: {
        //     // 组件基础属性
        //     width: '100%',
        //     height: 'auto',
        //     caption: 'APICaller示例'
        // },

        // 移除events中的内部函数实现
        // events: {
        //     // 数据加载完成事件
        //     onDataLoad: function(profile, data) {
        //         console.log("数据加载完成:", data);
        //     },
        //     // 数据加载错误事件
        //     onDataError: function(profile, error) {
        //         console.log("数据加载错误:", error);
        //     },
        //     // 数据创建完成事件
        //     onDataCreate: function(profile, data) {
        //         console.log("数据创建完成:", data);
        //     },
        //     // 数据删除完成事件
        //     onDataDelete: function(profile, data) {
        //         console.log("数据删除完成:", data);
        //     }
        // },
        
        // 移除冗余的get/set方法
        functions: {
            // // 获取选中的通知
            // getSelectedNotice: function() {
            //     return this._selectedNotice;
            // },
            // 
            // // 设置选中的通知
            // setSelectedNotice: function(notice) {
            //     this._selectedNotice = notice;
            // }
        },
        
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
            noticeApiCaller.setResponseType("JSON");
            
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
            
            // 创建容器组件
            var container = ood.create("ood.UI.Div");
            container.setHost(host, "container");
            container.setLeft(0);
            container.setTop(0);
            container.setWidth("100%");
            container.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            container.setCustomStyle({
                "KEY": {
                    "padding": "var(--mobile-spacing-md)"
                }
            });
            append(container);
            
            // 创建 List 组件用于显示通知列表 - 使用 OOD 的低代码组件
            var noticeList = ood.create("ood.UI.List");
            noticeList.setHost(host, "noticeList");
            noticeList.setName("noticeList");
            noticeList.setLeft(0);
            noticeList.setTop(0);
            noticeList.setWidth("100%");
            noticeList.setHeight("100%");
            noticeList.setSelMode("single"); // 单选模式
            noticeList.setItems([]); // 初始空数据
            
            // 设置列表项选中事件 - 使用 List 组件支持的 onItemSelected 事件
            noticeList.onItemSelected([
                {
                    "desc": "触发点击事件",
                    "type": "event",
                    "target": "events.onNoticeClick",
                    "args": ["{args[2]}", "{args[1]}"] // args[2] 是索引, args[1] 是项数据
                }
            ]);
            
            // 将 List 组件添加到容器中
            host.container.append(noticeList);
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    // 移除Static中的Templates和Appearances
    Static: {
        // Templates: {
        //     tagName: 'div',
        //     className: 'ood-mobile-api-caller-example',
        //     style: '{_style}',
        //     
        //     CONTAINER: {
        //         tagName: 'div',
        //         className: 'ood-mobile-api-caller-container'
        //     }
        // },
        // 
        // Appearances: {
        //     KEY: {
        //         position: 'relative',
        //         width: '100%',
        //         'background-color': 'var(--mobile-bg-primary)'
        //     },
        //     
        //     CONTAINER: {
        //         padding: 'var(--mobile-spacing-md)'
        //     }
        // }
    }
});