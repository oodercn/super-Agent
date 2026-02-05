// 自动生成的 RowMenuConfig 组件
ood.Class("ooder.RowMenuConfig", "ood.Module", {
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
        "currComponentAlias": "RowMenuConfig",
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

            // RowMenuConfigMain
            var RowMenuConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "RowMenuConfigMain")
                .setName("RowMenuConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(RowMenuConfigMain);

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
                .setMethodName("saveRowMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/tree/rowmenu/saveRowMenu")
                .setRequestDataSource([{"name":"RowMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.tree.rowcmd.menuclass.TreeRowMenuService")
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
                .setMethodName("getRowMenuConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/tree/rowmenu/RowMenuConfig")
                .setRequestDataSource([{"name":"RowMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"RowMenuConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.tree.rowcmd.TreeRowMenuNav")
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

            // childViewId
            var childViewId = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "childViewId")
                .setName("childViewId")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(1)
            ;
            PAGECTX.append(childViewId);

            // xpath
            var xpath = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "xpath")
                .setName("xpath")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(2)
            ;
            PAGECTX.append(xpath);

            // sourceMethodName
            var sourceMethodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceMethodName")
                .setName("sourceMethodName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(3)
            ;
            PAGECTX.append(sourceMethodName);

            // sourceClassName
            var sourceClassName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "sourceClassName")
                .setName("sourceClassName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(4)
            ;
            PAGECTX.append(sourceClassName);

            // methodName
            var methodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "methodName")
                .setName("methodName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(5)
            ;
            PAGECTX.append(methodName);

            // projectName
            var projectName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectName")
                .setName("projectName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(6)
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
                .setTabindex(7)
            ;
            PAGECTX.append(domainId);

            // projectVersionName
            var projectVersionName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "projectVersionName")
                .setName("projectVersionName")
                .setFormField(true)
                .setIsPid(false)
                .setPid(false)
                .setTabindex(8)
                .setValue("DSMdsmVVVERSION0")
            ;
            PAGECTX.append(projectVersionName);

            // soruceMethodName
            var soruceMethodName = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "soruceMethodName")
                .setName("soruceMethodName")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(9)
            ;
            PAGECTX.append(soruceMethodName);

            // id
            var id = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "id")
                .setName("id")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(10)
            ;
            PAGECTX.append(id);

            // RowMenuConfig
            var RowMenuConfig = ood.create("ood.UI.FormLayout")
                .setHost(RowMenuConfigMain, "RowMenuConfig")
                .setName("RowMenuConfig")
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
                .setLayoutData({"cells":{"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"按钮位置"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"动态加载"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"标题"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"延迟加载"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"禁用"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"是否显示标题"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"菜单项Style"},"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"按钮类型"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"提示"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"merged":[{"col":1,"colspan":3,"removed":false,"row":4,"rowspan":1}],"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":50}},"rows":5})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            RowMenuConfigMain.append(RowMenuConfig);

            // tagCmdsAlign
            var tagCmdsAlign = ood.create("ood.UI.ComboInput")
                .setHost(RowMenuConfig, "tagCmdsAlign")
                .setName("tagCmdsAlign")
                .setCaption("按钮位置")
                .setDefaultFocus(false)
                .setDesc("按钮位置")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.TagCmdsAlign")
                .setItems([{"bindClass":[],"caption":"left","id":"left","tabindex":0,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":1,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"right"},{"bindClass":[],"caption":"floatright","id":"floatright","tabindex":2,"tagVar":{"name":"floatright","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"floatright"}])
                .setLabelCaption("按钮位置")
                .setReadonly(false)
                .setTabindex(0)
                .setType("combobox")
                .setVisibility("visible")
            ;
            RowMenuConfig.append(tagCmdsAlign);

            // buttonType
            var buttonType = ood.create("ood.UI.ComboInput")
                .setHost(RowMenuConfig, "buttonType")
                .setName("buttonType")
                .setDefaultFocus(false)
                .setDesc("按钮类型")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.CmdButtonType")
                .setItems([{"bindClass":[],"caption":"按钮","id":"button","imageClass":"ri-checkbox-blank-line","name":"按钮","tabindex":0,"tagVar":{"name":"button","clazz":"net.ooder.esd.annotation.ui.CmdButtonType"},"title":"按钮","type":"button"},{"bindClass":[],"caption":"图片","id":"image","imageClass":"ri-image-line","name":"图片","tabindex":1,"tagVar":{"name":"image","clazz":"net.ooder.esd.annotation.ui.CmdButtonType"},"title":"图片","type":"image"},{"bindClass":[],"caption":"文本","id":"text","imageClass":"ri-font-size","name":"文本","tabindex":2,"tagVar":{"name":"text","clazz":"net.ooder.esd.annotation.ui.CmdButtonType"},"title":"文本","type":"text"},{"bindClass":[],"caption":"分隔符","id":"split","imageClass":"ri-separator","name":"分隔符","tabindex":3,"tagVar":{"name":"split","clazz":"net.ooder.esd.annotation.ui.CmdButtonType"},"title":"分隔符","type":"split"}])
                .setLabelCaption("按钮类型")
                .setReadonly(false)
                .setTabindex(1)
                .setType("listbox")
                .setVisibility("visible")
            ;
            RowMenuConfig.append(buttonType);

            // caption
            var caption = ood.create("ood.UI.Input")
                .setHost(RowMenuConfig, "caption")
                .setName("caption")
                .setCaption("标题")
                .setDefaultFocus(false)
                .setDesc("标题")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("标题")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(2)
                .setTagVar({})
                .setVisibility("visible")
            ;
            RowMenuConfig.append(caption);

            // tips
            var tips = ood.create("ood.UI.Input")
                .setHost(RowMenuConfig, "tips")
                .setName("tips")
                .setCaption("提示")
                .setDefaultFocus(false)
                .setDesc("提示")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("提示")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(3)
                .setTagVar({})
                .setVisibility("visible")
            ;
            RowMenuConfig.append(tips);

            // disabled
            var disabled = ood.create("ood.UI.CheckBox")
                .setHost(RowMenuConfig, "disabled")
                .setName("disabled")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("禁用")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(4)
                .setVisibility("visible")
            ;
            RowMenuConfig.append(disabled);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(RowMenuConfig, "dynLoad")
                .setName("dynLoad")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("动态加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(5)
                .setVisibility("visible")
            ;
            RowMenuConfig.append(dynLoad);

            // showCaption
            var showCaption = ood.create("ood.UI.CheckBox")
                .setHost(RowMenuConfig, "showCaption")
                .setName("showCaption")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("是否显示标题")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(6)
                .setVisibility("visible")
            ;
            RowMenuConfig.append(showCaption);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(RowMenuConfig, "lazy")
                .setName("lazy")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("延迟加载")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(7)
                .setVisibility("visible")
            ;
            RowMenuConfig.append(lazy);

            // itemStyle
            var itemStyle = ood.create("ood.UI.Input")
                .setHost(RowMenuConfig, "itemStyle")
                .setName("itemStyle")
                .setCaption("菜单项Style")
                .setDefaultFocus(false)
                .setDesc("菜单项Style")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("菜单项Style")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(8)
                .setTagVar({})
                .setVisibility("visible")
            ;
            RowMenuConfig.append(itemStyle);

            return children;
        }
    }
});
