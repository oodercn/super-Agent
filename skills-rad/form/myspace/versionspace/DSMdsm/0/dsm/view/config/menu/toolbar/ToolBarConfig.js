// 自动生成的 ToolBarConfig 组件
ood.Class("ooder.ToolBarConfig", "ood.Module", {
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
        "caption": "基础信息",
        "currComponentAlias": "ToolBarConfig",
        "dock": "fill",
        "title": "基础信息"
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

            // ToolBarConfigMain
            var ToolBarConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "ToolBarConfigMain")
                .setName("ToolBarConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(ToolBarConfigMain);

            // SAVE
            var SAVE = ood.create("ood.APICaller")
                .setHost(host, "SAVE")
                .setName("SAVE")
                .setAllform(false)
                .setAutoRun(false)
                .setCheckRequired(false)
                .setCheckValid(false)
                .setDesc("保存")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("saveToolMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/menu/toolbar/saveToolMenu")
                .setRequestDataSource([{"name":"menuConfigView","path":"","type":"FORM"},{"name":"xpath","path":"","type":"FORM"},{"name":"sourceMethodName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"currentClassName","path":"","type":"FORM"},{"name":"menuConfigView","path":"","type":"FORM"},{"name":"xpath","path":"","type":"FORM"},{"name":"sourceMethodName","path":"","type":"FORM"},{"name":"domainId","path":"","type":"FORM"},{"name":"projectName","path":"","type":"FORM"},{"name":"currentClassName","path":"","type":"FORM"},{"name":"ToolBarConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("JSON")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.menu.toolbar.ToolBarConfigService")
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
                .setDesc("基础信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getToolBarConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/menu/toolbar/ToolBarConfig")
                .setRequestDataSource([{"name":"ToolBarConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"ToolBarConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.menu.toolbar.ToolBarNav")
                .setTabindex(3)
                .setTips("基础信息")
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

            // ToolBarConfig
            var ToolBarConfig = ood.create("ood.UI.FormLayout")
                .setHost(ToolBarConfigMain, "ToolBarConfig")
                .setName("ToolBarConfig")
                .setCaption("基础信息")
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
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"组ID"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"显示标题"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"字体大小"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"动态加载"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"横向对齐"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"表单字段"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"行头手柄"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"禁用"},"A6":{"cellName":"A6","style":{"textAlign":"center"},"value":"延迟加载"},"A7":{"cellName":"A7","style":{"textAlign":"center"},"value":"实现类"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"垂直对齐"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":0,"rowspan":1},{"col":1,"colspan":3,"removed":false,"row":6,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35},"6":{"manualHeight":35},"7":{"manualHeight":35}},"rows":7})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ToolBarConfigMain.append(ToolBarConfig);

            // groupId
            var groupId = ood.create("ood.UI.Input")
                .setHost(ToolBarConfig, "groupId")
                .setName("groupId")
                .setCaption("组ID")
                .setDefaultFocus(false)
                .setDesc("组ID")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("组ID")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ToolBarConfig.append(groupId);

            // iconFontSize
            var iconFontSize = ood.create("ood.UI.ComboInput")
                .setHost(ToolBarConfig, "iconFontSize")
                .setName("iconFontSize")
                .setCaption("字体大小")
                .setDefaultFocus(false)
                .setDesc("字体大小")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setItems([])
                .setLabelCaption("字体大小")
                .setReadonly(false)
                .setTabindex(1)
                .setType("input")
                .setVisibility("visible")
            ;
            ToolBarConfig.append(iconFontSize);

            // hAlign
            var hAlign = ood.create("ood.UI.ComboInput")
                .setHost(ToolBarConfig, "hAlign")
                .setName("hAlign")
                .setCaption("垂直对齐")
                .setDefaultFocus(false)
                .setDesc("垂直对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.HAlignType")
                .setItems([{"bindClass":[],"caption":"center","id":"center","tabindex":0,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"center"},{"bindClass":[],"caption":"left","id":"left","tabindex":1,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":2,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.HAlignType"},"title":"right"}])
                .setLabelCaption("垂直对齐")
                .setReadonly(false)
                .setTabindex(2)
                .setType("combobox")
                .setVisibility("visible")
            ;
            ToolBarConfig.append(hAlign);

            // vAlign
            var vAlign = ood.create("ood.UI.ComboInput")
                .setHost(ToolBarConfig, "vAlign")
                .setName("vAlign")
                .setCaption("横向对齐")
                .setDefaultFocus(false)
                .setDesc("横向对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.VAlignType")
                .setItems([{"bindClass":[],"caption":"bottom","id":"bottom","tabindex":0,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"bottom"},{"bindClass":[],"caption":"middle","id":"middle","tabindex":1,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"middle"},{"bindClass":[],"caption":"top","id":"top","tabindex":2,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.VAlignType"},"title":"top"}])
                .setLabelCaption("横向对齐")
                .setReadonly(false)
                .setTabindex(3)
                .setType("combobox")
                .setVisibility("visible")
            ;
            ToolBarConfig.append(vAlign);

            // showCaption
            var showCaption = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "showCaption")
                .setName("showCaption")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("显示标题")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(showCaption);

            // handler
            var handler = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "handler")
                .setName("handler")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("行头手柄")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(handler);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "dynLoad")
                .setName("dynLoad")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("动态加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(6)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(dynLoad);

            // disabled
            var disabled = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "disabled")
                .setName("disabled")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("禁用")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(7)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(disabled);

            // formField
            var formField = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "formField")
                .setName("formField")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("表单字段")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(formField);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(ToolBarConfig, "lazy")
                .setName("lazy")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("延迟加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(9)
                .setVisibility("visible")
            ;
            ToolBarConfig.append(lazy);

            // bindService
            var bindService = ood.create("ood.UI.Input")
                .setHost(ToolBarConfig, "bindService")
                .setName("bindService")
                .setCaption("实现类")
                .setDefaultFocus(false)
                .setDesc("实现类")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("实现类")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(10)
                .setTagVar({})
                .setVisibility("visible")
            ;
            ToolBarConfig.append(bindService);

            return children;
        }
    }
});
