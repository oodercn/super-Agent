/**
 * 注解测试视图组件
 * 用于演示ooder注解规范的正确使用方式
 */
ood.Class("ood.Mobile.OA.AnnotationTestView", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._testData = [];
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
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    Static: {}
});