// 自动生成的 ConfigBaseInfo 组件
ood.Class("ooder.ConfigBaseInfo", "ood.Module", {
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
        "caption": "WEB配置",
        "currComponentAlias": "ConfigBaseInfo",
        "dock": "fill",
        "title": "WEB配置"
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

            // ConfigBaseInfoMain
            var ConfigBaseInfoMain = ood.create("ood.UI.Block")
                .setHost(host, "ConfigBaseInfoMain")
                .setName("ConfigBaseInfoMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(ConfigBaseInfoMain);

            // SAVE
            var SAVE = ood.create("ood.APICaller")
                .setHost(host, "SAVE")
                .setName("SAVE")
                .setAllform(true)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("保存")
                .setImageClass("ri-code-box-line")
                .setIsAllform(true)
                .setMethodName("saveAggMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/menu/config/saveAggMenu")
                .setRequestDataSource([{"name":"ConfigBaseInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.AggMenuService")
                .setTabindex(2)
                .setTips("保存")
            ;
            append(SAVE);

            // RELOAD
            var RELOAD = ood.create("ood.APICaller")
                .setHost(host, "RELOAD")
                .setName("RELOAD")
                .setAllform(false)
                .setAutoRun(true)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("WEB配置")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getConfigBaseInfo")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/agg/menu/config/ConfigBaseInfo")
                .setRequestDataSource([{"name":"ConfigBaseInfoMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"ConfigBaseInfoMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.aggregation.config.menu.AggMenuNav")
                .setTabindex(3)
                .setTips("WEB配置")
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

            // sourceClassName
            var sourceClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceClassName")
                .setName("sourceClassName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(2)
            ;
            PAGECTX.append(sourceClassName);

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(3)
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
                .setTabindex(4)
            ;
            PAGECTX.append(domainId);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(5)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // ConfigBaseInfo
            var ConfigBaseInfo = ood.create("ood.UI.FormLayout")
                .setHost(ConfigBaseInfoMain, "ConfigBaseInfo")
                .setName("ConfigBaseInfo")
                .setCaption("WEB配置")
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
                .setImageClass("ri-file-text-line")
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"访问地址"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"名称"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"描述"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"类名"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":0,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":1,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":2,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":3,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":50},"4":{"manualHeight":35}},"rows":4})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ConfigBaseInfoMain.append(ConfigBaseInfo);

            // url
            var url = ood.create("ood.UI.Input")
                .setHost(ConfigBaseInfo, "url")
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
            ConfigBaseInfo.append(url);

            // name
            var name = ood.create("ood.UI.Input")
                .setHost(ConfigBaseInfo, "name")
                .setName("name")
                .setCaption("名称")
                .setDefaultFocus(false)
                .setDesc("名称")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("名称")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(1)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ConfigBaseInfo.append(name);

            // desc
            var desc = ood.create("ood.UI.RichEditor")
                .setHost(ConfigBaseInfo, "desc")
                .setName("desc")
                .setCmdList("none")
                .setDefaultFocus(false)
                .setDesc("描述")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(2)
                .setTextType("text")
                .setVisibility("visible")
            ;
            ConfigBaseInfo.append(desc);

            // sourceClassName
            var sourceClassName = ood.create("ood.UI.Input")
                .setHost(ConfigBaseInfo, "sourceClassName")
                .setName("sourceClassName")
                .setCaption("类名")
                .setDefaultFocus(false)
                .setDesc("类名")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("类名")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(3)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ConfigBaseInfo.append(sourceClassName);

            return children;
        }
    }
});
