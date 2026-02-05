// 自动生成的 APIMethodBaseView 组件
ood.Class("ooder.APIMethodBaseView", "ood.Module", {
    Instance: {
        // 初始化方法
        initialize: function() {
            this.iniComponents();
        },

        // 组件依赖
        Dependencies: [],

        // 必需的组件
        Required: [],

        // 组件属性
        properties: {
        "autoDestroy": true,
        "bindClass": [],
        "caption": "API信息",
        "currComponentAlias": "APIMethodBaseView",
        "dock": "fill",
        "title": "API信息"
},

        // 事件定义
        events: {},

        // 视图菜单栏
        ViewMenuBar: {},

        // 函数定义
        functions: {},

        // 初始化组件
        iniComponents: function() {
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // PAGECTX
            var PAGECTX = ood.create("ood.UI.Block")
                .setHost(host, "PAGECTX")
                .setName("PAGECTX")
                .setBorderType("none")
                .setTabindex(0)
                .setVisibility("hidden")
            ;
            append(PAGECTX);

            // APIMethodBaseViewMain
            var APIMethodBaseViewMain = ood.create("ood.UI.Block")
                .setHost(host, "APIMethodBaseViewMain")
                .setName("APIMethodBaseViewMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(APIMethodBaseViewMain);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("API信息")
                .setImageClass("ri-code-line")
                .setIsAllform(false)
                .setMethodName("getAPIMethodBaseView")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/api/config/APIMethodBaseView")
                .setRequestDataSource([{"name":"APIMethodBaseViewMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"APIMethodBaseViewMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.api.method.APIMethodInfo")
                .setTabindex(2)
                .setTips("API信息")
            ;
            append(RELOAD);

            // currentClassName
            var currentClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "currentClassName")
                .setName("currentClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(0)
            ;
            PAGECTX.append(currentClassName);

            // xpath
            var xpath = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "xpath")
                .setName("xpath")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(1)
            ;
            PAGECTX.append(xpath);

            // sourceMethodName
            var sourceMethodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceMethodName")
                .setName("sourceMethodName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(2)
            ;
            PAGECTX.append(sourceMethodName);

            // sourceClassName
            var sourceClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceClassName")
                .setName("sourceClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(3)
            ;
            PAGECTX.append(sourceClassName);

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(4)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectName);

            // domainId
            var domainId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "domainId")
                .setName("domainId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(5)
            ;
            PAGECTX.append(domainId);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(6)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // APIMethodBaseView
            var APIMethodBaseView = ood.create("ood.UI.FormLayout")
                .setHost(APIMethodBaseViewMain, "APIMethodBaseView")
                .setName("APIMethodBaseView")
                .setCaption("API信息")
                .setDefaultColWidth(150)
                .setDefaultColumnSize(-1)
                .setDefaultFocus(false)
                .setDefaultRowHeight(35)
                .setDefaultRowSize(-1)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("fill")
                .setFloatHandler(false)
                .setImageClass("ri-code-line")
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"访问地址"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"异步调用"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"自动显示"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"级联处理"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"JSON格式"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"自动运行"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"HttpMethod"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"显示顺序"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":0,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":4,"rowspan":1}],"rowSetting":{"1":{"manualHeight":30},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":60}},"rows":5})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            APIMethodBaseViewMain.append(APIMethodBaseView);

            // url
            var url = ood.create("ood.UI.Input")
                .setHost(APIMethodBaseView, "url")
                .setName("url")
                .setCaption("访问地址")
                .setDefaultFocus(false)
                .setDesc("访问地址")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("访问地址")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(url);

            // autoDisplay
            var autoDisplay = ood.create("ood.UI.CheckBox")
                .setHost(APIMethodBaseView, "autoDisplay")
                .setName("autoDisplay")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动显示")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(1)
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(autoDisplay);

            // index
            var index = ood.create("ood.UI.ComboInput")
                .setHost(APIMethodBaseView, "index")
                .setName("index")
                .setCaption("显示顺序")
                .setCurrencyTpl("$ *")
                .setDecimalSeparator(".")
                .setDefaultFocus(false)
                .setDesc("显示顺序")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setForceFillZero(true)
                .setIncrement(1)
                .setItems([])
                .setLabelCaption("显示顺序")
                .setPrecision(0)
                .setReadonly(false)
                .setTabindex(2)
                .setType("spin")
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(index);

            // responseBody
            var responseBody = ood.create("ood.UI.CheckBox")
                .setHost(APIMethodBaseView, "responseBody")
                .setName("responseBody")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("JSON格式")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(3)
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(responseBody);

            // queryAsync
            var queryAsync = ood.create("ood.UI.CheckBox")
                .setHost(APIMethodBaseView, "queryAsync")
                .setName("queryAsync")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("异步调用")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(queryAsync);

            // autoRun
            var autoRun = ood.create("ood.UI.CheckBox")
                .setHost(APIMethodBaseView, "autoRun")
                .setName("autoRun")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动运行")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(autoRun);

            // allform
            var allform = ood.create("ood.UI.CheckBox")
                .setHost(APIMethodBaseView, "allform")
                .setName("allform")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("级联处理")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(6)
                .setVisibility("visible")
            ;
            APIMethodBaseView.append(allform);

            // method
            var method = ood.create("ood.UI.List")
                .setHost(APIMethodBaseView, "method")
                .setName("method")
                .setBorderType("flat")
                .setDefaultFocus(false)
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("java.lang.Enum")
                .setEvents({})
                .setHeight("15.0em")
                .setItemRow("cell")
                .setItems([{"bindClass":[],"caption":"GET","id":"GET","tabindex":0,"tagVar":{"name":"GET","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"GET"},{"bindClass":[],"caption":"HEAD","id":"HEAD","tabindex":1,"tagVar":{"name":"HEAD","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"HEAD"},{"bindClass":[],"caption":"POST","id":"POST","tabindex":2,"tagVar":{"name":"POST","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"POST"},{"bindClass":[],"caption":"PUT","id":"PUT","tabindex":3,"tagVar":{"name":"PUT","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"PUT"},{"bindClass":[],"caption":"PATCH","id":"PATCH","tabindex":4,"tagVar":{"name":"PATCH","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"PATCH"},{"bindClass":[],"caption":"DELETE","id":"DELETE","tabindex":5,"tagVar":{"name":"DELETE","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"DELETE"},{"bindClass":[],"caption":"OPTIONS","id":"OPTIONS","tabindex":6,"tagVar":{"name":"OPTIONS","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"OPTIONS"},{"bindClass":[],"caption":"TRACE","id":"TRACE","tabindex":7,"tagVar":{"name":"TRACE","clazz":"org.springframework.web.bind.annotation.RequestMethod"},"title":"TRACE"}])
                .setSelMode("multibycheckbox")
                .setTabindex(7)
                .setVisibility("visible")
                .setWidth("auto")
            ;
            APIMethodBaseView.append(method);

            return children;
        }
    }
});
