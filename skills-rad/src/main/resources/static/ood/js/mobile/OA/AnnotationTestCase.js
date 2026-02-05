/**
 * ooder注解规范测试用例
 * 用于验证注解的强关联设计、生命周期管理和服务参数设计规范
 */
ood.Class("ood.Mobile.OA.AnnotationTestCase", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this.testService = new ood.Mobile.OA.AnnotationTestService();
        },
        
        Dependencies: [],
        Required: [],

        events: {
            onTestSimpleParameter: function() {
                this.testSimpleParameter();
            },
            
            onTestComplexParameter: function() {
                this.testComplexParameter();
            },
            
            onTestQueryParameter: function() {
                this.testQueryParameter();
            }
        },

        functions: {
            // 测试简单参数使用
            testSimpleParameter: function() {
                console.log("开始测试简单参数使用...");
                this.testService.getDataById("123").then(function(data) {
                    console.log("简单参数测试结果:", data);
                }).catch(function(error) {
                    console.error("简单参数测试失败:", error);
                });
            },
            
            // 测试复杂视图对象参数使用
            testComplexParameter: function() {
                console.log("开始测试复杂视图对象参数使用...");
                var viewData = {
                    name: "测试表单",
                    description: "这是一个测试表单数据",
                    category: "测试分类"
                };
                
                this.testService.submitData(viewData).then(function(result) {
                    console.log("复杂参数测试结果:", result);
                }).catch(function(error) {
                    console.error("复杂参数测试失败:", error);
                });
            },
            
            // 测试查询参数使用
            testQueryParameter: function() {
                console.log("开始测试查询参数使用...");
                var queryParams = {
                    keyword: "测试",
                    category: "文档",
                    page: 1,
                    size: 10
                };
                
                this.testService.queryData(queryParams).then(function(results) {
                    console.log("查询参数测试结果:", results);
                }).catch(function(error) {
                    console.error("查询参数测试失败:", error);
                });
            }
        },

        iniComponents: function() {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], properties = {}, append = function(child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);
            
            // 创建测试容器
            var container = ood.create("ood.UI.Div");
            container.setHost(host, "container");
            container.setLeft(0);
            container.setTop(0);
            container.setWidth("100%");
            container.setHeight("100%");
            container.setCustomStyle({
                "KEY": {
                    "padding": "var(--mobile-spacing-md)",
                    "display": "flex",
                    "flex-direction": "column",
                    "gap": "var(--mobile-spacing-md)"
                }
            });
            append(container);
            
            // 简单参数测试按钮
            var simpleParamBtn = ood.create("ood.UI.Button");
            simpleParamBtn.setHost(host, "simpleParamBtn");
            simpleParamBtn.setCaption("测试简单参数");
            simpleParamBtn.setWidth("100%");
            simpleParamBtn.setHeight(40);
            simpleParamBtn.on("onClick", function() {
                host.fireEvent("onTestSimpleParameter");
            });
            container.add(simpleParamBtn);
            
            // 复杂参数测试按钮
            var complexParamBtn = ood.create("ood.UI.Button");
            complexParamBtn.setHost(host, "complexParamBtn");
            complexParamBtn.setCaption("测试复杂参数");
            complexParamBtn.setWidth("100%");
            complexParamBtn.setHeight(40);
            complexParamBtn.setTop(50);
            complexParamBtn.on("onClick", function() {
                host.fireEvent("onTestComplexParameter");
            });
            container.add(complexParamBtn);
            
            // 查询参数测试按钮
            var queryParamBtn = ood.create("ood.UI.Button");
            queryParamBtn.setHost(host, "queryParamBtn");
            queryParamBtn.setCaption("测试查询参数");
            queryParamBtn.setWidth("100%");
            queryParamBtn.setHeight(40);
            queryParamBtn.setTop(100);
            queryParamBtn.on("onClick", function() {
                host.fireEvent("onTestQueryParameter");
            });
            container.add(queryParamBtn);
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    Static: {}
});