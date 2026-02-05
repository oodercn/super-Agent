/**
 * 移动端通知公告组件（重构版本）
 * 用于显示企业内部的通知、公告等信息
 * 完全遵循OOD开发规范
 */
ood.Class("ood.Mobile.OA.refactored.Notice", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
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