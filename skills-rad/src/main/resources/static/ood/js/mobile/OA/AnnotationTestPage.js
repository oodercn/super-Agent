/**
 * 注解测试页面
 * 用于验证ooder注解规范在Code First模式下的正确实现
 */
ood.Class("ood.Mobile.OA.AnnotationTestPage", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            this.constructor.upper.prototype.initialize.apply(this, arguments);
        },
        
        Dependencies: [],
        Required: [],

        events: {
            // 页面初始化完成后触发
            onFormLoaded: function() {
                console.log("注解测试页面加载完成");
            }
        },

        functions: {
            // 页面初始化逻辑
            initPage: function() {
                console.log("初始化注解测试页面");
            }
        },

        iniComponents: function() {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], properties = {}, append = function(child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);
            
            // 创建页面容器
            var container = ood.create("ood.UI.Div");
            container.setHost(host, "container");
            container.setLeft(0);
            container.setTop(0);
            container.setWidth("100%");
            container.setHeight("100%");
            container.setCustomStyle({
                "KEY": {
                    "padding": "var(--mobile-spacing-md)"
                }
            });
            append(container);
            
            // 页面标题
            var title = ood.create("ood.UI.Label");
            title.setHost(host, "title");
            title.setCaption("ooder注解规范测试页面");
            title.setWidth("100%");
            title.setHeight(40);
            title.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-size-lg)",
                    "font-weight": "bold",
                    "text-align": "center",
                    "margin-bottom": "var(--mobile-spacing-md)"
                }
            });
            container.add(title);
            
            // 说明文本
            var description = ood.create("ood.UI.Label");
            description.setHost(host, "description");
            description.setCaption("本页面用于测试ooder注解规范在Code First模式下的正确实现，包括视图绑定、参数传递等功能。");
            description.setWidth("100%");
            description.setHeight(60);
            description.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-size-sm)",
                    "color": "var(--mobile-color-text-secondary)",
                    "margin-bottom": "var(--mobile-spacing-md)"
                }
            });
            container.add(description);
            
            // 测试按钮区域
            var buttonContainer = ood.create("ood.UI.Div");
            buttonContainer.setHost(host, "buttonContainer");
            buttonContainer.setWidth("100%");
            buttonContainer.setHeight(200);
            buttonContainer.setTop(120);
            buttonContainer.setCustomStyle({
                "KEY": {
                    "display": "flex",
                    "flex-direction": "column",
                    "gap": "var(--mobile-spacing-sm)"
                }
            });
            container.add(buttonContainer);
            
            // 获取表单按钮
            var getFormBtn = ood.create("ood.UI.Button");
            getFormBtn.setHost(host, "getFormBtn");
            getFormBtn.setCaption("获取表单");
            getFormBtn.setWidth("100%");
            getFormBtn.setHeight(40);
            getFormBtn.on("onClick", function() {
                // 通过APICaller调用后端服务
                var apiCaller = ood.create("ood.APICaller");
                apiCaller.setRequestDataSource({
                    url: "/test/annotation/getForm",
                    method: "POST",
                    params: {
                        id: "1"
                    }
                });
                apiCaller.setResponseDataTarget({
                    // 响应数据将自动绑定到表单组件
                });
                apiCaller.execute();
            });
            buttonContainer.add(getFormBtn);
            
            // 保存数据按钮
            var saveBtn = ood.create("ood.UI.Button");
            saveBtn.setHost(host, "saveBtn");
            saveBtn.setCaption("保存数据");
            saveBtn.setWidth("100%");
            saveBtn.setHeight(40);
            saveBtn.setTop(50);
            saveBtn.on("onClick", function() {
                // 通过APICaller调用后端服务
                var apiCaller = ood.create("ood.APICaller");
                apiCaller.setRequestDataSource({
                    url: "/test/annotation/save",
                    method: "POST",
                    data: {
                        id: "1",
                        name: "测试名称",
                        description: "测试描述",
                        category: "测试分类",
                        status: "active"
                    }
                });
                apiCaller.execute();
            });
            buttonContainer.add(saveBtn);
            
            // 查询数据按钮
            var queryBtn = ood.create("ood.UI.Button");
            queryBtn.setHost(host, "queryBtn");
            queryBtn.setCaption("查询数据");
            queryBtn.setWidth("100%");
            queryBtn.setHeight(40);
            queryBtn.setTop(100);
            queryBtn.on("onClick", function() {
                // 通过APICaller调用后端服务
                var apiCaller = ood.create("ood.APICaller");
                apiCaller.setRequestDataSource({
                    url: "/test/annotation/query",
                    method: "POST",
                    params: {
                        keyword: "测试",
                        category: "测试分类"
                    }
                });
                apiCaller.execute();
            });
            buttonContainer.add(queryBtn);
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    Static: {}
});