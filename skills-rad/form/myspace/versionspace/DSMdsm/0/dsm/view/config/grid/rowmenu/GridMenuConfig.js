// 自动生成的 GridMenuConfig 组件
ood.Class("ooder.GridMenuConfig", "ood.Module", {
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
        "caption": "行菜单信息",
        "currComponentAlias": "GridMenuConfig",
        "dock": "fill",
        "title": "行菜单信息"
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

            // GridMenuConfigMain
            var GridMenuConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "GridMenuConfigMain")
                .setName("GridMenuConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(GridMenuConfigMain);

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
                .setMethodName("saveRowClassMenu")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/grid/rowmenu/saveRowClassMenu")
                .setRequestDataSource([{"name":"GridMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.grid.row.rowcmd.menuclass.GridRowMenuService")
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
                .setDesc("行菜单信息")
                .setImageClass("ri-code-box-line")
                .setIsAllform(false)
                .setMethodName("getGridMenuConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/grid/rowmenu/GridMenuConfig")
                .setRequestDataSource([{"name":"GridMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"GridMenuConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.grid.row.rowcmd.GridRowMenuNav")
                .setTabindex(3)
                .setTips("行菜单信息")
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

            // id
            var id = ood.create("ood.UI.HiddenInput")
                .setHost(PAGECTX, "id")
                .setName("id")
                .setFormField(true)
                .setIsPid(true)
                .setPid(true)
                .setTabindex(7)
            ;
            PAGECTX.append(id);

            // GridMenuConfig
            var GridMenuConfig = ood.create("ood.UI.FormLayout")
                .setHost(GridMenuConfigMain, "GridMenuConfig")
                .setName("GridMenuConfig")
                .setCaption("行菜单信息")
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
            GridMenuConfigMain.append(GridMenuConfig);

            // tagCmdsAlign
            var tagCmdsAlign = ood.create("ood.UI.ComboInput")
                .setHost(GridMenuConfig, "tagCmdsAlign")
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
            GridMenuConfig.append(tagCmdsAlign);

            // buttonType
            var buttonType = ood.create("ood.UI.ComboInput")
                .setHost(GridMenuConfig, "buttonType")
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
            GridMenuConfig.append(buttonType);

            // caption
            var caption = ood.create("ood.UI.Input")
                .setHost(GridMenuConfig, "caption")
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
            GridMenuConfig.append(caption);

            // tips
            var tips = ood.create("ood.UI.Input")
                .setHost(GridMenuConfig, "tips")
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
            GridMenuConfig.append(tips);

            // disabled
            var disabled = ood.create("ood.UI.CheckBox")
                .setHost(GridMenuConfig, "disabled")
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
            GridMenuConfig.append(disabled);

            // dynLoad
            var dynLoad = ood.create("ood.UI.CheckBox")
                .setHost(GridMenuConfig, "dynLoad")
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
            GridMenuConfig.append(dynLoad);

            // showCaption
            var showCaption = ood.create("ood.UI.CheckBox")
                .setHost(GridMenuConfig, "showCaption")
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
            GridMenuConfig.append(showCaption);

            // lazy
            var lazy = ood.create("ood.UI.CheckBox")
                .setHost(GridMenuConfig, "lazy")
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
            GridMenuConfig.append(lazy);

            // itemStyle
            var itemStyle = ood.create("ood.UI.Input")
                .setHost(GridMenuConfig, "itemStyle")
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
            GridMenuConfig.append(itemStyle);

            return children;
        }
    }
});
