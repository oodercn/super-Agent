/**
 * 通知公告组件API调用示例
 * 演示如何使用APICaller组件进行数据交互
 */
ood.Class("ood.Mobile.OA.NoticeAPICallerExample", 'ood.Module',{
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
            caption: '通知公告API调用示例'
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
                
                // 设置成功回调
                noticeApiCaller.onData([
                    {
                        "desc": "设置通知数据",
                        "type": "function",
                        "target": "setNotices",
                        "args": ["{temp$.okData}"]
                    }
                ]);
                
                // 设置错误回调
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
            
            // 设置通知数据
            setNotices: function(notices) {
                console.log("获取到通知数据:", notices);
                // 在这里处理通知数据，例如更新UI
            },
            
            // 处理错误
            handleError: function(error) {
                console.error("获取通知数据失败:", error);
                // 在这里处理错误，例如显示错误信息
            },
            
            // 添加新通知
            addNotice: function(noticeData) {
                // 创建 APICaller 实例用于添加通知
                var addApiCaller = ood.create("ood.APICaller")
                    .setName("addNotice")
                    .setQueryURL("/api/notices")
                    .setQueryMethod("POST")
                    .setQueryData(noticeData);
                
                // 设置成功回调
                addApiCaller.onData([
                    {
                        "desc": "刷新通知列表",
                        "type": "function",
                        "target": "refreshNotices",
                        "args": []
                    }
                ]);
                
                // 设置错误回调
                addApiCaller.onError([
                    {
                        "desc": "处理错误",
                        "type": "function",
                        "target": "handleError",
                        "args": ["{temp$.koData}"]
                    }
                ]);
                
                // 发起请求
                addApiCaller.invoke();
            },
            
            // 刷新通知列表
            refreshNotices: function() {
                console.log("刷新通知列表");
                // 重新获取通知数据
                this.initDataSource();
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
    }
});