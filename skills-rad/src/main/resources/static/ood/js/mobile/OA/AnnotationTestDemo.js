/**
 * ooder注解规范测试演示页面
 * 展示注解规范在实际应用中的正确使用方式
 */
ood.Class("ood.Mobile.OA.AnnotationTestDemo", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            this.constructor.upper.prototype.initialize.apply(this, arguments);
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
            title.setCaption("ooder注解规范测试演示");
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
            
            // 规范说明
            var description = ood.create("ood.UI.Label");
            description.setHost(host, "description");
            description.setCaption("本演示页面展示了ooder注解规范的核心要点，包括强关联设计、参数类型选择和生命周期管理。");
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
            
            // 测试用例组件
            var testCase = ood.create("ood.Mobile.OA.AnnotationTestCase");
            testCase.setHost(host, "testCase");
            testCase.setWidth("100%");
            testCase.setHeight(200);
            testCase.setTop(120);
            container.add(testCase);
            
            // 规范要点说明
            var specTitle = ood.create("ood.UI.Label");
            specTitle.setHost(host, "specTitle");
            specTitle.setCaption("注解规范核心要点");
            specTitle.setWidth("100%");
            specTitle.setHeight(30);
            specTitle.setTop(330);
            specTitle.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-size-md)",
                    "font-weight": "bold",
                    "margin-top": "var(--mobile-spacing-md)"
                }
            });
            container.add(specTitle);
            
            var specList = ood.create("ood.UI.Div");
            specList.setHost(host, "specList");
            specList.setWidth("100%");
            specList.setHeight(200);
            specList.setTop(370);
            specList.setCustomStyle({
                "KEY": {
                    "font-size": "var(--mobile-font-size-sm)",
                    "line-height": "1.5"
                }
            });
            specList.setHtml('\
                <div>1. 强关联设计：通过泛型参数建立视图绑定，确保类型安全</div>\
                <div>2. 生命周期管理：编译期→启动期→运行时→销毁期</div>\
                <div>3. 三级注解体系：强注解(骨干)→分支注解→叶子注解</div>\
                <div>4. 参数设计规范：根据业务场景选择简单参数或复杂视图对象</div>\
                <div>5. 默认值规范：避免显式添加@RequestParam(required=false)</div>\
            ');
            container.add(specList);
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    Static: {}
});