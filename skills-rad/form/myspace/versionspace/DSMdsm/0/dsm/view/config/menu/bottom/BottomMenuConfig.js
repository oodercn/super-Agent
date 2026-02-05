// 自动生成的 BottomMenuConfig 组件
ood.Class("ooder.BottomMenuConfig", "ood.Module", {
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
        "currComponentAlias": "BottomMenuConfig",
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

            // BottomMenuConfigMain
            var BottomMenuConfigMain = ood.create("ood.UI.Block")
                .setHost(host, "BottomMenuConfigMain")
                .setName("BottomMenuConfigMain")
                .setDock("fill")
                .setPanelBgClr("transparent")
                .setTabindex(1)
            ;
            append(BottomMenuConfigMain);

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
                .setMethodName("getBottomMenuConfig")
                .setProxyType("auto")
                .setQueryAsync(true)
                .setQueryMethod("POST")
                .setQueryURL("/dsm/view/config/menu/bottom/BottomMenuConfig")
                .setRequestDataSource([{"name":"BottomMenuConfigMain","path":"","type":"FORM"},{"name":"PAGECTX","path":"","type":"FORM"}])
                .setRequestType("FORM")
                .setResponseCallback([])
                .setResponseDataTarget([{"name":"BottomMenuConfigMain","path":"data","type":"FORM"},{"name":"PAGECTX","path":"data","type":"FORM"}])
                .setResponseType("JSON")
                .setSourceClassName("net.ooder.dsm.view.config.menu.bottommenu.BottomBarNav")
                .setTabindex(2)
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

            // BottomMenuConfig
            var BottomMenuConfig = ood.create("ood.UI.FormLayout")
                .setHost(BottomMenuConfigMain, "BottomMenuConfig")
                .setName("BottomMenuConfig")
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
                .setLayoutData({"cells":{"C1":{"cellName":"C1","style":{"textAlign":"center"},"value":"容器高度"},"C2":{"cellName":"C2","style":{"textAlign":"center"},"value":"容器边框"},"A1":{"cellName":"A1","style":{"textAlign":"center"},"value":"容器位置"},"C3":{"cellName":"C3","style":{"textAlign":"center"},"value":"按钮位置"},"A2":{"cellName":"A2","style":{"textAlign":"center"},"value":"按钮对齐方式"},"C4":{"cellName":"C4","style":{"textAlign":"center"},"value":"按钮边框"},"A3":{"cellName":"A3","style":{"textAlign":"center"},"value":"按钮类型"},"C5":{"cellName":"C5","style":{"textAlign":"center"},"value":"按钮间距"},"A4":{"cellName":"A4","style":{"textAlign":"center"},"value":"按钮对齐"},"C6":{"cellName":"C6","style":{"textAlign":"center"},"value":"按钮宽度"},"A5":{"cellName":"A5","style":{"textAlign":"center"},"value":"自动对齐"},"A6":{"cellName":"A6","style":{"textAlign":"center"},"value":"按钮边距"},"A7":{"cellName":"A7","style":{"textAlign":"center"},"value":"按钮高度"}},"colSetting":{"A":{"manualWidth":150},"B":{"manualWidth":150},"C":{"manualWidth":150},"D":{"manualWidth":150}},"cols":4,"rowSetting":{"1":{"manualHeight":35},"2":{"manualHeight":35},"3":{"manualHeight":35},"4":{"manualHeight":35},"5":{"manualHeight":35},"6":{"manualHeight":35},"7":{"manualHeight":35}},"rows":7})
                .setLineSpacing(10)
                .setMode("write")
                .setSolidGridlines(true)
                .setStretchH("all")
                .setStretchHeight("none")
                .setTabindex(0)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfigMain.append(BottomMenuConfig);

            // barDock
            var barDock = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "barDock")
                .setName("barDock")
                .setDefaultFocus(false)
                .setDesc("容器位置")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.Dock")
                .setItems([{"bindClass":[],"caption":"默认","id":"none","name":"默认","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"默认","type":"none"},{"bindClass":[],"caption":"顶部对齐","id":"top","name":"顶部对齐","tabindex":1,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"顶部对齐"},{"bindClass":[],"caption":"底部对齐","id":"bottom","name":"底部对齐","tabindex":2,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"底部对齐"},{"bindClass":[],"caption":"左对齐","id":"left","name":"左对齐","tabindex":3,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"左对齐"},{"bindClass":[],"caption":"右对齐","id":"right","name":"右对齐","tabindex":4,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"右对齐"},{"bindClass":[],"caption":"居中对齐","id":"center","name":"居中对齐","tabindex":5,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"居中对齐"},{"bindClass":[],"caption":"垂直居中","id":"middle","name":"垂直居中","tabindex":6,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"垂直居中"},{"bindClass":[],"caption":"自适应排列","id":"origin","name":"自适应排列","tabindex":7,"tagVar":{"name":"origin","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自适应排列"},{"bindClass":[],"caption":"宽度自适应","id":"width","name":"宽度自适应","tabindex":8,"tagVar":{"name":"width","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"宽度自适应"},{"bindClass":[],"caption":"高度自适应","id":"height","name":"高度自适应","tabindex":9,"tagVar":{"name":"height","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"高度自适应"},{"bindClass":[],"caption":"自动填充","id":"fill","name":"自动填充","tabindex":10,"tagVar":{"name":"fill","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自动填充"},{"bindClass":[],"caption":"覆盖","id":"cover","name":"覆盖","tabindex":11,"tagVar":{"name":"cover","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"覆盖"}])
                .setLabelCaption("容器位置")
                .setReadonly(false)
                .setTabindex(0)
                .setType("listbox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(barDock);

            // height
            var height = ood.create("ood.UI.Input")
                .setHost(BottomMenuConfig, "height")
                .setName("height")
                .setCaption("容器高度")
                .setDefaultFocus(false)
                .setDesc("容器高度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("容器高度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(1)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(height);

            // itemAlign
            var itemAlign = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "itemAlign")
                .setName("itemAlign")
                .setCaption("按钮对齐方式")
                .setDefaultFocus(false)
                .setDesc("按钮对齐方式")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.AlignType")
                .setItems([{"bindClass":[],"caption":"center","id":"center","tabindex":0,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"center"},{"bindClass":[],"caption":"left","id":"left","tabindex":1,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":2,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.AlignType"},"title":"right"}])
                .setLabelCaption("按钮对齐方式")
                .setReadonly(false)
                .setTabindex(2)
                .setType("combobox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemAlign);

            // barBorderType
            var barBorderType = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "barBorderType")
                .setName("barBorderType")
                .setCaption("容器边框")
                .setDefaultFocus(false)
                .setDesc("容器边框")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.BorderType")
                .setItems([{"bindClass":[],"caption":"none","id":"none","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"none"},{"bindClass":[],"caption":"flat","id":"flat","tabindex":1,"tagVar":{"name":"flat","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"flat"},{"bindClass":[],"caption":"inset","id":"inset","tabindex":2,"tagVar":{"name":"inset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"inset"},{"bindClass":[],"caption":"outset","id":"outset","tabindex":3,"tagVar":{"name":"outset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"outset"}])
                .setLabelCaption("容器边框")
                .setReadonly(false)
                .setTabindex(3)
                .setType("combobox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(barBorderType);

            // itemType
            var itemType = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "itemType")
                .setName("itemType")
                .setCaption("按钮类型")
                .setDefaultFocus(false)
                .setDesc("按钮类型")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.StatusItemType")
                .setItems([{"bindClass":[],"caption":"text","id":"text","tabindex":0,"tagVar":{"name":"text","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"text"},{"bindClass":[],"caption":"button","id":"button","tabindex":1,"tagVar":{"name":"button","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"button"},{"bindClass":[],"caption":"dropButton","id":"dropButton","tabindex":2,"tagVar":{"name":"dropButton","clazz":"net.ooder.esd.annotation.ui.StatusItemType"},"title":"dropButton"}])
                .setLabelCaption("按钮类型")
                .setReadonly(false)
                .setTabindex(4)
                .setType("combobox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemType);

            // dock
            var dock = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "dock")
                .setName("dock")
                .setDefaultFocus(false)
                .setDesc("按钮位置")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.Dock")
                .setItems([{"bindClass":[],"caption":"默认","id":"none","name":"默认","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"默认","type":"none"},{"bindClass":[],"caption":"顶部对齐","id":"top","name":"顶部对齐","tabindex":1,"tagVar":{"name":"top","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"顶部对齐"},{"bindClass":[],"caption":"底部对齐","id":"bottom","name":"底部对齐","tabindex":2,"tagVar":{"name":"bottom","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"底部对齐"},{"bindClass":[],"caption":"左对齐","id":"left","name":"左对齐","tabindex":3,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"左对齐"},{"bindClass":[],"caption":"右对齐","id":"right","name":"右对齐","tabindex":4,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"右对齐"},{"bindClass":[],"caption":"居中对齐","id":"center","name":"居中对齐","tabindex":5,"tagVar":{"name":"center","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"居中对齐"},{"bindClass":[],"caption":"垂直居中","id":"middle","name":"垂直居中","tabindex":6,"tagVar":{"name":"middle","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"垂直居中"},{"bindClass":[],"caption":"自适应排列","id":"origin","name":"自适应排列","tabindex":7,"tagVar":{"name":"origin","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自适应排列"},{"bindClass":[],"caption":"宽度自适应","id":"width","name":"宽度自适应","tabindex":8,"tagVar":{"name":"width","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"宽度自适应"},{"bindClass":[],"caption":"高度自适应","id":"height","name":"高度自适应","tabindex":9,"tagVar":{"name":"height","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"高度自适应"},{"bindClass":[],"caption":"自动填充","id":"fill","name":"自动填充","tabindex":10,"tagVar":{"name":"fill","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"自动填充"},{"bindClass":[],"caption":"覆盖","id":"cover","name":"覆盖","tabindex":11,"tagVar":{"name":"cover","clazz":"net.ooder.esd.annotation.ui.Dock"},"title":"覆盖"}])
                .setLabelCaption("按钮位置")
                .setReadonly(false)
                .setTabindex(5)
                .setType("listbox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(dock);

            // tagCmdsAlign
            var tagCmdsAlign = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "tagCmdsAlign")
                .setName("tagCmdsAlign")
                .setCaption("按钮对齐")
                .setDefaultFocus(false)
                .setDesc("按钮对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.TagCmdsAlign")
                .setItems([{"bindClass":[],"caption":"left","id":"left","tabindex":0,"tagVar":{"name":"left","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"left"},{"bindClass":[],"caption":"right","id":"right","tabindex":1,"tagVar":{"name":"right","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"right"},{"bindClass":[],"caption":"floatright","id":"floatright","tabindex":2,"tagVar":{"name":"floatright","clazz":"net.ooder.esd.annotation.ui.TagCmdsAlign"},"title":"floatright"}])
                .setLabelCaption("按钮对齐")
                .setReadonly(false)
                .setTabindex(6)
                .setType("combobox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(tagCmdsAlign);

            // borderType
            var borderType = ood.create("ood.UI.ComboInput")
                .setHost(BottomMenuConfig, "borderType")
                .setName("borderType")
                .setCaption("按钮边框")
                .setDefaultFocus(false)
                .setDesc("按钮边框")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setDynLoad(true)
                .setEnumClass("net.ooder.esd.annotation.ui.BorderType")
                .setItems([{"bindClass":[],"caption":"none","id":"none","tabindex":0,"tagVar":{"name":"none","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"none"},{"bindClass":[],"caption":"flat","id":"flat","tabindex":1,"tagVar":{"name":"flat","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"flat"},{"bindClass":[],"caption":"inset","id":"inset","tabindex":2,"tagVar":{"name":"inset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"inset"},{"bindClass":[],"caption":"outset","id":"outset","tabindex":3,"tagVar":{"name":"outset","clazz":"net.ooder.esd.annotation.ui.BorderType"},"title":"outset"}])
                .setLabelCaption("按钮边框")
                .setReadonly(false)
                .setTabindex(7)
                .setType("combobox")
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(borderType);

            // connected
            var connected = ood.create("ood.UI.CheckBox")
                .setHost(BottomMenuConfig, "connected")
                .setName("connected")
                .setCaption("")
                .setDefaultFocus(false)
                .setDesc("自动对齐")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDock("none")
                .setEvents({})
                .setTabindex(8)
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(connected);

            // itemPadding
            var itemPadding = ood.create("ood.UI.Input")
                .setHost(BottomMenuConfig, "itemPadding")
                .setName("itemPadding")
                .setCaption("按钮间距")
                .setDefaultFocus(false)
                .setDesc("按钮间距")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("按钮间距")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(9)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemPadding);

            // itemMargin
            var itemMargin = ood.create("ood.UI.Input")
                .setHost(BottomMenuConfig, "itemMargin")
                .setName("itemMargin")
                .setCaption("按钮边距")
                .setDefaultFocus(false)
                .setDesc("按钮边距")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("按钮边距")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(10)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemMargin);

            // itemWidth
            var itemWidth = ood.create("ood.UI.Input")
                .setHost(BottomMenuConfig, "itemWidth")
                .setName("itemWidth")
                .setCaption("按钮宽度")
                .setDefaultFocus(false)
                .setDesc("按钮宽度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("按钮宽度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(11)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemWidth);

            // itemHeight
            var itemHeight = ood.create("ood.UI.Input")
                .setHost(BottomMenuConfig, "itemHeight")
                .setName("itemHeight")
                .setCaption("按钮高度")
                .setDefaultFocus(false)
                .setDesc("按钮高度")
                .setDisableClickEffect(false)
                .setDisableHoverEffect(false)
                .setDisableTips(false)
                .setDisabled(false)
                .setDock("none")
                .setEvents({})
                .setImageClass("")
                .setItems([])
                .setLabelCaption("按钮高度")
                .setLabelPos("left")
                .setLabelSize("6.0em")
                .setReadonly(false)
                .setTabindex(12)
                .setTagVar({})
                .setVisibility("visible")
            ;
            BottomMenuConfig.append(itemHeight);

            return children;
        }
    }
});
