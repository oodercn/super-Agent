/**
 * APICaller 使用示例
 * 演示如何在业务组件中使用 APICaller 进行数据交互
 */
ood.Class("ood.Mobile.OA.APICallerExample", 'ood.Module',{
    Instance:{
        initialize : function(){
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
        },
        Dependencies:[],
        Required:[],
        properties : {
            // 组件基础属性
            width: '100%',
            height: 'auto',
            caption: 'APICaller示例'
        },

        events:{
            // 数据加载完成事件
            onDataLoad: function(profile, data) {
                console.log("数据加载完成:", data);
            },
            // 数据加载错误事件
            onDataError: function(profile, error) {
                console.log("数据加载错误:", error);
            }
        },
        functions:{
            // 初始化 APICaller
            initAPICaller: function() {
                // 创建 APICaller 实例
                this._apiCaller = ood.create("ood.APICaller");
                this._apiCaller.setHost(this, "noticeApiCaller");
                
                // 配置 APICaller
                this._apiCaller.setQueryURL("/api/notices");  // API 地址
                this._apiCaller.setQueryMethod("GET");        // 请求方法
                this._apiCaller.setProxyType("AJAX");         // 代理类型
                this._apiCaller.setRequestType("JSON");       // 请求类型
                
                // 设置成功回调
                this._apiCaller.onData(function(profile, response) {
                    if (response && response.data) {
                        // 触发数据加载完成事件
                        if (this.events.onDataLoad) {
                            this.events.onDataLoad(this.get(0), response.data);
                        }
                    }
                }.bind(this));
                
                // 设置错误回调
                this._apiCaller.onError(function(profile, error) {
                    console.error("API调用失败:", error);
                    // 触发数据加载错误事件
                    if (this.events.onDataError) {
                        this.events.onDataError(this.get(0), error);
                    }
                }.bind(this));
            },
            
            // 加载通知数据
            loadNotices: function(params) {
                // 初始化 APICaller（如果尚未初始化）
                if (!this._apiCaller) {
                    this.initAPICaller();
                }
                
                // 设置请求参数
                if (params) {
                    this._apiCaller.setQueryData(params);
                }
                
                // 发起请求
                this._apiCaller.invoke();
            },
            
            // 创建通知
            createNotice: function(noticeData) {
                // 创建一个新的 APICaller 实例用于创建操作
                var createApiCaller = ood.create("ood.APICaller");
                createApiCaller.setHost(this, "createNoticeApiCaller");
                
                // 配置 APICaller
                createApiCaller.setQueryURL("/api/notices");   // API 地址
                createApiCaller.setQueryMethod("POST");        // POST 请求
                createApiCaller.setProxyType("AJAX");          // 代理类型
                createApiCaller.setRequestType("JSON");        // 请求类型
                
                // 设置请求数据
                createApiCaller.setQueryData(noticeData);
                
                // 设置成功回调
                createApiCaller.onData(function(profile, response) {
                    console.log("通知创建成功:", response);
                    // 重新加载数据
                    this.loadNotices();
                }.bind(this));
                
                // 设置错误回调
                createApiCaller.onError(function(profile, error) {
                    console.error("创建通知失败:", error);
                }.bind(this));
                
                // 发起请求
                createApiCaller.invoke();
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
            className: 'ood-mobile-api-caller-example',
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